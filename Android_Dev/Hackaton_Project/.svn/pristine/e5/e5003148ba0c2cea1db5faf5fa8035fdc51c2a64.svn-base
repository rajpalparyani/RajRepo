/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufSettingDataProxy.java
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
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoClientSettingData;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoSyncClientSettingDataReq;
import com.telenav.j2me.framework.protocol.ProtoSyncClientSettingDataResp;
import com.telenav.logger.Logger;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-3-30
 */
public class ProtoBufSettingDataProxy extends AbstractProtobufServerProxy implements ISettingDataProxy
{
    boolean isUpload = false;
    public ProtoBufSettingDataProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA.equals(protoBuf.getObjType()))
        {
            ProtoSyncClientSettingDataResp resp = ProtoSyncClientSettingDataResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            if (status == IServerProxyConstants.SUCCESS)
            {
                if(isUpload)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "Preference delay sync: success, remove flag", null, null, true);
                    DaoManager.getInstance().getSimpleConfigDao().remove(SimpleConfigDao.KEY_SETTING_DATA_NEED_UPLOAD);
                    DaoManager.getInstance().getSimpleConfigDao().store();
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "SyncClientSettingData, " + "parseProtoClientSettingData version : " + resp.getTimeStamp());
                if(resp.hasSettingData())
                {
                    DaoManager.getInstance().getPreferenceDao().setSyncPreferenceSettingDataTime(resp.getTimeStamp() + "", this.getRegion());
                    parseSettingData(resp.getSettingData());
                }
            }
        }

        return this.status;
    }

    private void parseSettingData(ProtoClientSettingData settingData)
    {
        Vector details = settingData.getDetail();
        
        Logger.log(Logger.INFO, this.getClass().getName(), "SyncClientSettingData, " + "parseProtoClientSettingData:");
        
        int size = details.size();
        for(int i = 0; i < size; i++)
        {
            ProtoProperty property = (ProtoProperty) details.elementAt(i);
            String key = property.getKey();
            String value = property.getValue();
            
            Logger.log(Logger.INFO, this.getClass().getName(), "SyncClientSettingData -- ProtoClientSettingData:(key, value) i = " + i + "( " + key + ", " + value + " )");
            DaoManager.getInstance().getPreferenceDao().setIntValue(Integer.parseInt(key), Integer.parseInt(value));
        }
        DaoManager.getInstance().getPreferenceDao().store(this.getRegion());
    }

    public String syncSettingData(String version, long timeStamp)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(version);
            requestItem.params.addElement(timeStamp);
            Vector list = createProtoBufReq(requestItem);
            
            return this.sendRequest(list, IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
    
    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if (IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() >= 2)
            {
                ProtoSyncClientSettingDataReq.Builder builder = ProtoSyncClientSettingDataReq.newBuilder();
                builder.setVersion((String) requestItem.params.elementAt(0));
                builder.setTimeStamp((Long) requestItem.params.elementAt(1));
                if(requestItem.params.size() >= 3)
                {
                    isUpload = true;
                    Hashtable settings = (Hashtable) requestItem.params.elementAt(2);
                    ProtoClientSettingData.Builder settingDataBuilder = ProtoClientSettingData.newBuilder();
                    Vector details = new Vector();
                    for(Enumeration e = settings.keys();e.hasMoreElements();)
                    {
                        Object obj = e.nextElement();
                        String key = (String)obj;
                        String value = (String) settings.get(key);
                        ProtoProperty.Builder propertyBuilder = ProtoProperty.newBuilder();
                        propertyBuilder.setKey(key);
                        propertyBuilder.setValue(value);
                        ProtoProperty property = propertyBuilder.build();
                        details.addElement(property);
                    }
                    settingDataBuilder.setDetail(details);
                    ProtoClientSettingData settingData = settingDataBuilder.build();
                    builder.setSetting(settingData);
                }
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
    }

    public String syncSettingData(String version, long timeStamp, Hashtable settings)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(version);
            requestItem.params.addElement(timeStamp);
            requestItem.params.addElement(settings);
            Vector list = createProtoBufReq(requestItem);
            
            return this.sendRequest(list, IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA);
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }
}
