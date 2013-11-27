/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StartupProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IStartupProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoCommonObject;
import com.telenav.j2me.framework.protocol.ProtoStartupReq;
import com.telenav.j2me.framework.protocol.ProtoStartupResp;
import com.telenav.j2me.framework.protocol.ProtoSyncResourceReq;
import com.telenav.logger.Logger;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class ProtoBufStartupProxy extends AbstractProtobufServerProxy implements IStartupProxy
{

    public ProtoBufStartupProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public String synchronizeStartUp(long lastSyncTime)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_STARTUP, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(lastSyncTime));
            Vector list = createProtoBufReq(requestItem);

            return this.sendRequest(list, IServerProxyConstants.ACT_STARTUP);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if(IServerProxyConstants.ACT_STARTUP.equals(requestItem.action))
        {
            if(requestItem.params != null && requestItem.params.size() > 0)
            {
                ProtoStartupReq.Builder builder = ProtoStartupReq.newBuilder();
                ProtoSyncResourceReq.Builder syncBuilder = ProtoSyncResourceReq.newBuilder();
                ProtoCommonObject.Builder commonBuilder = ProtoCommonObject.newBuilder();
                
                //last sync time
                Object lastSyncTimeObj = requestItem.params.elementAt(0);
                builder.setSharedTime(((Long)lastSyncTimeObj).longValue());
                
                //TODO where is request number
                
                //server driven version
                String serverDrivenVersion = AbstractDaoManager.getInstance().getServerDrivenParamsDao().getVersion(this.getRegion());
                if(serverDrivenVersion != null)
                {
                    syncBuilder.setSdpVersion(serverDrivenVersion);
                }
                
                //server mapping version
                String serverMappingVersion = AbstractDaoManager.getInstance().getServerDrivenParamsDao().getVersion(this.getRegion());
                if(serverMappingVersion != null)
                {
                    syncBuilder.setServiceMappingVersion(serverMappingVersion);
                }
                
                //category version
                String categoryVersion = AbstractDaoManager.getInstance().getResourceBarDao().getCategoryVersion(this.getRegion());
                if(categoryVersion != null)
                {
                    syncBuilder.setPoiCategoryTreeVersion(categoryVersion);
                }
                
                //airport version
                String airportVersion = AbstractDaoManager.getInstance().getResourceBarDao().getAirportVersion();
                if(airportVersion != null)
                {
                    syncBuilder.setAirportVersion(airportVersion);
                }
                
                //resourceFormat version
                String resourceFormatVersion = AbstractDaoManager.getInstance().getResourceBarDao().getResourceFormatVersion();//Fix me: is paramsVersion ?
                if(resourceFormatVersion != null)
                {
                    syncBuilder.setParamsVersion(resourceFormatVersion);
                }
                
//                
//                //DSR server driven version
//                String dsrDspVersion = AbstractDaoManager.getInstance().getDsrServerDrivenParamsDao().getVersion();
//                argNode.addString(dsrDspVersion);
//                
                commonBuilder.setAudioInventory(AbstractDaoManager.getInstance().getResourceBarDao().getAudioInventory() + "," + AbstractDaoManager.getInstance().getResourceBarDao().getAudioTimestamp());
                commonBuilder.setAudioRuleInventory(AbstractDaoManager.getInstance().getResourceBarDao().getAudioRuleInventory() + "," + AbstractDaoManager.getInstance().getResourceBarDao().getAudioRuleTimestamp());
                

                syncBuilder.addElementCommonObj(commonBuilder.build());
                builder.setSyncResourceReq(syncBuilder.build());
                
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_STARTUP);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_STARTUP.equals(protoBuf.getObjType()))
        {
            ProtoStartupResp resp = ProtoStartupResp.parseFrom(protoBuf.getBufferData());
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- status: " + this.status);
            if (status == IServerProxyConstants.SUCCESS)
            {
                String upgradeType = ""+resp.getUpgradeType();
                String upgradeFileName = resp.getUpgradeFileName();
                String upgradeAppVersion = resp.getUpgradeVersion();
                String upgradeUrl = resp.getUpgradeUrl();
                String upgradeSummary = resp.getUpgradeSummary();
                String upgradeBuildNumber = resp.getUpgradeBuildNumber();
                String upgradeMinOSVersion = resp.getMinOSVersion();
                String upgradeMaxOSVersion = resp.getMaxOSVersion();   
                
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- upgradeType: " + upgradeType);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- upgradeFileName: " + upgradeFileName);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- upgradeAppVersion: " + upgradeAppVersion);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- upgradeSummary: " + upgradeSummary);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- upgradeBuildNumber: " + upgradeBuildNumber);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- upgradeMinOSVersion: " + upgradeMinOSVersion);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- upgradeMaxOSVersion: " + upgradeMaxOSVersion);
                
                StringList list = new StringList();
                list.add(upgradeType);
                list.add(upgradeFileName);
                list.add(upgradeAppVersion);
                list.add(upgradeUrl);
                list.add(upgradeSummary);
                list.add(upgradeBuildNumber);                
                list.add(upgradeMinOSVersion);
                list.add(upgradeMaxOSVersion);
                
                AbstractDaoManager.getInstance().getStartupDao().setUpgradeInfo(list);
                
                int sharedAddrNumber = resp.getSharedAddressNumber();
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- sharedAddrNumber: " + sharedAddrNumber);
               ((DaoManager)DaoManager.getInstance()).getAddressDao().setNetworkUnrevieweAddressSize(sharedAddrNumber);
                  
                String newAppVersion = resp.getApplicationNum();
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- newAppVersion: " + newAppVersion);
                list = new StringList();
                list.add(newAppVersion);
                AbstractDaoManager.getInstance().getStartupDao().setNewAppInfo(list);
                DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SWITCHED_DATASET, resp.getDataProvider());
                DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_MAP_COPYRIGHT, resp.getCopyright());
                DaoManager.getInstance().getSimpleConfigDao().store();
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- DataProvider(): " + resp.getDataProvider());
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_STARTUP -- MapCopyright(): " + resp.getCopyright());

                AbstractDaoManager.getInstance().getStartupDao().store();
            }
        }

        return this.status;
    }
}
