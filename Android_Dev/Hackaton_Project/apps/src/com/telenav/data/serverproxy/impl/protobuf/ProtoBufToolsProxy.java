/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ToolsProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoAdsReport;
import com.telenav.j2me.framework.protocol.ProtoDsmReq;
import com.telenav.j2me.framework.protocol.ProtoDsmResp;
import com.telenav.j2me.framework.protocol.ProtoMISReport;
import com.telenav.j2me.framework.protocol.ProtoMISReportingReq;
import com.telenav.j2me.framework.protocol.ProtoMISReportingResp;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoSyncPreference;
import com.telenav.j2me.framework.protocol.ProtoSyncPreferenceReq;
import com.telenav.j2me.framework.protocol.ProtoSyncPreferenceResp;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class ProtoBufToolsProxy extends AbstractProtobufServerProxy implements IToolsProxy
{
    private static final String C2DM_KEY = "registration_id";
    
    private Hashtable userPrefers = null;

    private byte syncType = -1;
    
    private int uploadType = -1;
    
    public ProtoBufToolsProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    public String syncPreference(byte syncType, Hashtable prefers, int uploadType)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_PREFERENCE, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(syncType));
            requestItem.params.addElement(prefers);
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(uploadType));
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_SYNC_PREFERENCE);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    public String sendMISReports(AbstractMisLog[] logs, String sessionID)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SEND_MIS_REPORTS, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(logs);
            requestItem.params.addElement(sessionID);
            Vector list = createProtoBufReq(requestItem);
            
            return this.sendRequest(list, IServerProxyConstants.ACT_SEND_MIS_REPORTS);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
        
    }
    
    public String sendC2DMRegistrationId(String registrationId)
    {
        try
        {
            if (registrationId == null || registrationId.trim().length() == 0)
            {
                //do not send empty reg id to server
                //the reg id is only retrieved at first login and save to RMS. If there is some problem and reg id is missing,
                //the client will send empty string to server to overwrite the correct one.
                return "";
            }

            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_C2DM, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(registrationId);
            Vector list = createProtoBufReq(requestItem);

            return this.sendRequest(list, IServerProxyConstants.ACT_C2DM);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if (IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() > 2)
            {
                ProtoSyncPreferenceReq.Builder builder = ProtoSyncPreferenceReq.newBuilder();

                Object syncType = requestItem.params.elementAt(0);
                Hashtable prefers = (Hashtable) requestItem.params.elementAt(1);
                int uploadType = ((Integer)requestItem.params.elementAt(2)).intValue();
                
                this.syncType = ((Byte) syncType).byteValue();
                this.uploadType = uploadType;
                
                builder.setType(this.syncType);
                
                if(this.syncType == SYNC_TYPE_UPLOAD)
                {
                    if(uploadType == UPLOAD_TYPE_USER_INFO)
                    {
                        if (prefers != null && !prefers.isEmpty())
                        {
                            Enumeration keys = prefers.keys();
                            while (keys.hasMoreElements())
                            {
                                String key = (String) keys.nextElement();
                                String value = (String) prefers.get(key);
                                ProtoProperty.Builder propertyBuilder = ProtoProperty.newBuilder();
                                propertyBuilder.setKey(key);
                                propertyBuilder.setValue(value);
                                builder.addElementUserInformation(propertyBuilder.build());
                            }
                        }
                    }
                    else
                    {
                        if(!MigrationExecutor.getInstance().isSyncEnabled())
                        {
                            return;
                        }
                        
                        boolean isHomeUpdated = ((AddressDao)AbstractDaoManager.getInstance().getAddressDao()).isHomeUpdated();
                        if(isHomeUpdated)
                        {
                            Stop homeStop = AbstractDaoManager.getInstance().getAddressDao().getHomeAddress();
                            if (homeStop != null)
                            {
                                builder.setHomeAddress(ProtoBufServerProxyUtil.convertStop(homeStop));
                            }
                            builder.setNeedUpdateHomeAddress(true);
                        }
                        else
                        {
                            builder.setNeedUpdateHomeAddress(false);
                        }
                        
                        boolean isOfficeUpdated = ((AddressDao)AbstractDaoManager.getInstance().getAddressDao()).isOfficeUpdated();
                        if(isOfficeUpdated)
                        {
                            Stop officeStop = AbstractDaoManager.getInstance().getAddressDao().getOfficeAddress();
                            if (officeStop != null)
                            {
                                builder.setOfficeAddress(ProtoBufServerProxyUtil.convertStop(officeStop));
                            }
                            builder.setNeedUpdateOfficeAddress(true);
                        }
                        else
                        {
                            builder.setNeedUpdateOfficeAddress(false);
                        }
                    }
                }
                
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_SYNC_PREFERENCE);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
        else if(IServerProxyConstants.ACT_SEND_MIS_REPORTS.equals(requestItem.action))
        {
            if(requestItem.params != null && requestItem.params.size() > 1)
            {
                AbstractMisLog[] logs = (AbstractMisLog[]) requestItem.params.elementAt(0);
                String sessionID = (String) requestItem.params.elementAt(1);
                
                MandatoryProfile profile;
                if(userProfileProvider != null)
                {
                    profile = userProfileProvider.getMandatoryNode();
                }
                else
                {
                    MandatoryNodeDao mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao();
                    profile = mandatoryNode.getMandatoryNode();
                }
                
                String homeCarrier = profile != null ?  profile.getClientInfo().deviceCarrier : "";
                
                ProtoMISReportingReq.Builder builder = ProtoMISReportingReq.newBuilder();
                builder.setSessionID(sessionID);//TODO
                builder.setMisType("");
                
                Vector adslogs = new Vector();
                Vector mislogs = new Vector();
                for(int i = 0; i< logs.length; i++)
                {
                    Vector keys = new Vector();
                    Vector data = new Vector();
                    
                    keys.addElement(PrimitiveTypeCache.valueOf((long)(IMisLogConstants.ATTR_EVENT_TYPE_ID)));
                    data.addElement(Integer.toString(logs[i].getType()));
                    keys.addElement(PrimitiveTypeCache.valueOf((long)(IMisLogConstants.ATTR_EVENT_TIMESTAMP)));
                    data.addElement(Long.toString(logs[i].getTimestamp()));
                    
                    //add mandatory home carrier type
                    
                    keys.addElement(PrimitiveTypeCache.valueOf((long)(IMisLogConstants.ATTR_HOME_CARRIER)));
                    data.addElement(homeCarrier);
                    
                    Enumeration keyenum = logs[i].getAttributeKeys();
                    if (keyenum != null)
                    {
                        while (keyenum.hasMoreElements())
                        {
                            Long key = (Long) keyenum.nextElement();
                            keys.addElement(key);
                            data.addElement(logs[i].getAttribute(key.longValue()));
                        }
                    }
                    
                    if(logs[i].getPriority() == IMisLogConstants.PRIORITY_1 && logs[i].getType() != IMisLogConstants.TYPE_SESSION_STARTUP)
                    {
                        ProtoAdsReport.Builder adsBuilder =  ProtoAdsReport.newBuilder();
                        adsBuilder.setKey(keys);
                        adsBuilder.setData(data);
                        adslogs.addElement(adsBuilder.build());
                    }
                    else
                    {
                        ProtoMISReport.Builder misBuilder =  ProtoMISReport.newBuilder();
                        misBuilder.setKey(keys);
                        misBuilder.setData(data);
                        mislogs.addElement(misBuilder.build());
                    }
                }
                builder.setAdsReport(adslogs);
                builder.setMisReport(mislogs);
                
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_SEND_MIS_REPORTS);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
        else if (IServerProxyConstants.ACT_C2DM.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() > 0)
            {
                ProtoDsmReq.Builder builder = ProtoDsmReq.newBuilder();
                
                String registrationId = (String) requestItem.params.elementAt(0);
                ProtoProperty.Builder property = ProtoProperty.newBuilder();
                property.setKey(C2DM_KEY);
                property.setValue(registrationId);
                builder.addElementDsmProperty(property.build());
                
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_C2DM);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
    }
    
    public int getPreferenceSyncType()
    {
        return this.syncType;
    }

    /**
     * implement of IToolsProxy interface.
     */
    public String syncBillingAccount()
    {
        return "";
    }
    
    public Hashtable getUserPrefers()
    {
        return userPrefers;
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_C2DM.equals(protoBuf.getObjType()))
        {
            ProtoDsmResp resp = ProtoDsmResp.parseFrom(protoBuf.getBufferData());
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
        }
        else if (IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(protoBuf.getObjType()))
        {
            ProtoSyncPreferenceResp resp = ProtoSyncPreferenceResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (status == IServerProxyConstants.SUCCESS)
            {
                if(syncType == SYNC_TYPE_UPLOAD)
                {
                    if(uploadType == UPLOAD_TYPE_USER_INFO)
                    {
                        DaoManager.getInstance().getSimpleConfigDao().remove(SimpleConfigDao.KEY_USER_PROFILE_NEED_UPLOAD);
                    }
                    if(uploadType == UPLOAD_TYPE_HOME_WORK)
                    {
                        DaoManager.getInstance().getSimpleConfigDao().remove(SimpleConfigDao.KEY_HOME_WORK_NEED_UPLOAD);
                    }
                }
                
                ProtoSyncPreference pref = resp.getSyncPreference();
                Vector v = pref.getUserInformation();
                if (v != null)
                {
                    for (int i = 0; i < v.size(); i++)
                    {
                        ProtoProperty preferItem = (ProtoProperty) v.elementAt(i);
                        if(preferItem.hasKey() && preferItem.hasValue())
                        {
                            String key = preferItem.getKey();
                            String value = preferItem.getValue();
                            if (userPrefers == null)
                            {
                                userPrefers = new Hashtable();
                            }
                            userPrefers.put(key, value);
                        }
                    }
                }

                if(MigrationExecutor.getInstance().isSyncEnabled())
                {
                    if(pref.hasHomeAddress() || pref.hasOfficeAddress())
                    {
                        Stop homeStop = ProtoBufServerProxyUtil.convertStop(pref.getHomeAddress());
                        boolean isHomeFromCloud = (homeStop != null);
                        Stop workStop = ProtoBufServerProxyUtil.convertStop(pref.getOfficeAddress());
                        boolean isWorkFromCloud = (workStop != null);
                        AbstractDaoManager.getInstance().getAddressDao().setHomeOfficeAddress(homeStop, isHomeFromCloud, workStop, isWorkFromCloud, false);
                    }
                }
            }            
        }

        else if (IServerProxyConstants.ACT_SEND_MIS_REPORTS.equals(protoBuf.getObjType()))
        {
            ProtoMISReportingResp resp = ProtoMISReportingResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            
            // if stat events successfully were sent to server then ack for logger
            if (status == IServerProxyConstants.SUCCESS)
            {
                MisLogManager.getInstance().acknowledge();
            }
            else
            {
                MisLogManager.getInstance().rollback();
            }
        }
        
        return this.status;
    }
}
