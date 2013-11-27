/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ServiceLocatorProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import android.os.RemoteException;

import com.telenav.app.CommManager;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.HostProvider;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.ServiceLocator;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IServiceLocatorProxy;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoServiceItem;
import com.telenav.j2me.framework.protocol.ProtoServiceMapping;
import com.telenav.j2me.framework.protocol.ProtoSyncServiceLocatorReq;
import com.telenav.j2me.framework.protocol.ProtoSyncServiceLocatorResp;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 11, 2010
 */
public class ProtoBufServiceLocatorProxy extends AbstractProtobufServerProxy implements IServiceLocatorProxy
{

    public ProtoBufServiceLocatorProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    public String sendSyncServiceLocator()
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR, this);
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }

    protected void addRequestArgs(Vector requestVector, RequestItem requestItem) throws Exception
    {
        if (IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR.equals(requestItem.action))
        {
            ProtoSyncServiceLocatorReq.Builder builder = ProtoSyncServiceLocatorReq.newBuilder();

            String serviceMappingVersion = AbstractDaoManager.getInstance().getServiceLocatorDao().getVersion();
            if (serviceMappingVersion == null)
                serviceMappingVersion = "";
            builder.setServiceMappingVersion(serviceMappingVersion);

            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR);
            requestVector.addElement(pb);
        }
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR.equals(protoBuf.getObjType()))
        {
            ProtoSyncServiceLocatorResp resp = ProtoSyncServiceLocatorResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            if (status == IServerProxyConstants.SUCCESS)
            {
                ProtoServiceMapping mapping = resp.getServiceMapping();
                parseServiceMapping(mapping);
            }
        }

        return this.status;
    }

    public void parseServiceMapping(ProtoServiceMapping mapping)
    {
        if (mapping != null)
        {
            Logger.log(Logger.INFO, ProtoBufServiceLocatorProxy.class.getName(), "---syncCserver---" + "parseServiceMapping:");
            
            Vector items = mapping.getServiceItems();
            String version = mapping.getServiceVersion();
            StringMap aliasUrlMap = new StringMap();
            StringMap aliasSuffixMap = new StringMap();
            StringMap actionAliasMap = new StringMap();
            for (int i = 0; items != null && i < items.size(); i++)
            {
                ProtoServiceItem item = (ProtoServiceItem) items.elementAt(i);
                String serviceURL = item.getServiceDomainName();
                Vector actions = item.getActions();
                Vector urlEntries = item.getUrlEntry();
                Vector urlItems = item.getUrlItem();
                if (urlEntries != null && urlEntries.size() > 0 && urlItems != null && urlItems.size() > 0)
                {
                    for(int m = 0 ; m < urlEntries.size() ; m ++)
                    {
                        String domainAlias = (String) urlEntries.elementAt(m);
                        String domainURL = (String) urlItems.elementAt(m);
                        Logger.log(Logger.INFO, ProtoBufServiceLocatorProxy.class.getName(), "Alias: " + domainAlias + " , URL : " + domainURL);
                        aliasUrlMap.put(domainAlias, domainURL);
                        
                        if(serviceURL != null && serviceURL.trim().length() > 0)
                        {
                            Logger.log(Logger.INFO, ProtoBufServiceLocatorProxy.class.getName(), "Alias: " + domainAlias + " , suffixURL : " + serviceURL);
                            aliasSuffixMap.put(domainAlias, serviceURL);
                        }
                    }
                    
                    if (actions != null)
                    {
                        //just map actions to domainURL 0
                        String domainAlias = (String) urlEntries.elementAt(0);
                        
                        for (int j = 0; j < actions.size(); j++)
                        {
                            String action = (String) actions.elementAt(j);
                            Logger.log(Logger.INFO, ProtoBufServiceLocatorProxy.class.getName(), "Action: " + action + " , alias : " + domainAlias);
                            actionAliasMap.put(action, domainAlias);
                        }
                    }
                }
            }
            AbstractDaoManager.getInstance().getServiceLocatorDao().setServerMappings(aliasUrlMap, aliasSuffixMap, actionAliasMap, version);
            AbstractDaoManager.getInstance().getServiceLocatorDao().store();
            NavsdkUserManagementService.getInstance().serviceLocatorUpdate();
            
            if(DwfServiceConnection.getInstance().getConnection() != null)
            {
                HostProvider hostProvider = CommManager.getInstance().getComm().getHostProvider();
                ServiceLocator serviceLocator = (ServiceLocator)hostProvider;
                
                try
                {
                    DwfServiceConnection.getInstance().getConnection()
                            .setDwfServiceUrl(serviceLocator.getActionUrl(IServerProxyConstants.ACT_DWF_HTTPS));
                    DwfServiceConnection.getInstance().getConnection()
                    .setDwfWebUrl(serviceLocator.getActionUrl(IServerProxyConstants.ACT_DWF_TINY));
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
