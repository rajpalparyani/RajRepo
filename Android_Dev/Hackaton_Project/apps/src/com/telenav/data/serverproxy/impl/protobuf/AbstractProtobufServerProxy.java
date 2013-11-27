/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractProtobufServerProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import net.jarlehansen.protobuf.javame.AbstractOutputWriter;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.ClientInfo;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserPrefers;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.ServerProxyConfig;
import com.telenav.data.serverproxy.impl.protobuf.log.ProtoBufConverter;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.framework.protocol.ProtoSsoToken;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.telephony.TnDeviceInfo;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public abstract class AbstractProtobufServerProxy extends AbstractServerProxy
{

    public AbstractProtobufServerProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    
    protected Vector createProtoBufReq(RequestItem requestItem)
    {
        if (requestItem.action == null || requestItem.action.length() == 0)
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "createRequestNode() - ERROR: request action is null or empty.");
            
            throw new IllegalArgumentException("request action is null or empty.");
        }

        Vector requestVector = new Vector();

        try
        {
            addRequestArgs(requestVector, requestItem);
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return requestVector;
    }
    
    protected void addRequestArgs(Vector requestVector, RequestItem requestItem) throws Exception
    {
        
    }

    protected void handleResponse(byte[] response)
    {
        ProtocolBuffer[] bufs = ProtocolBufferUtil.toBufArray(response);
        if (bufs == null)
            return;
        
        Vector<ProtocolBuffer> probufVector=new Vector<ProtocolBuffer>();
        
        for (int i=0; i<bufs.length; i++)
        {
            probufVector.add(bufs[i]);
            handleProtocolBuffer(bufs[i]);         
        }
        ProtoBufConverter.convertToString(probufVector, false);
       
        
    }

    protected boolean parseCommonObject(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_SSO_TOKEN_PROTO_BUF.equals(protoBuf.getObjType()))
        {
            ProtoSsoToken resp = ProtoSsoToken.parseFrom(protoBuf.getBufferData());

            if (resp.hasSsoToken() && resp.getSsoToken().length() > 0)
            {
                DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().ssoToken = resp.getSsoToken();
                //DaoManager.getInstance().getMandatoryNodeDao().store();
                return true;
            }
        }
        return false;
    }
    
    protected abstract int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException;

    protected void handleProtocolBuffer(ProtocolBuffer buf)
    {
        try
        {
            if (!parseCommonObject(buf) && status != IServerProxyConstants.FAILED
                    && this.status != IServerProxyConstants.EXCEPTION)
            {
                this.status = parseRequestSpecificData(buf);
            }
            
            if (this.status == IServerProxyConstants.FAILED || this.status == IServerProxyConstants.EXCEPTION)
            {
                if (this.listener != null)
                    this.listener.transactionError(this);
            }
        }
        catch (Exception t)
        {
            Logger.log(this.getClass().getName(), t, "handleProtocolBuffer() -server proxy handle ProtocolBuffer exception.");

            if (this.listener != null)
                this.listener.transactionError(this);
        }
    }
    
    protected String sendRequest(Vector list, String action) throws IOException
    {
        addMandatoryNode(list);
        ProtoBufConverter.convertToString(list, true);
        return sendData(ProtocolBufferUtil.listToByteArray(list), action, ServerProxyConfig.defaultRetries, ServerProxyConfig.defaultTimeout);
    }
    
    protected String sendRequest(Vector list, String action, TnLocation[] gpsFixes, int fixCount) throws IOException
    {
        if (gpsFixes != null && fixCount > 0)
            addGpsList(list, gpsFixes, fixCount);
        
        addMandatoryNode(list);
        ProtoBufConverter.convertToString(list, true);
        return sendData(ProtocolBufferUtil.listToByteArray(list), action, ServerProxyConfig.defaultRetries, ServerProxyConfig.defaultTimeout);
    }
    
    protected String sendRequest(Vector list, String action, int retryTime, int timeout) throws IOException
    {
        addMandatoryNode(list);
        ProtoBufConverter.convertToString(list, true);
        return sendData(ProtocolBufferUtil.listToByteArray(list), action, (byte)retryTime, timeout);
    }
    
    protected String sendRequest(Vector list, String action, TnLocation[] gpsFixes, int fixCount,
            int retryTime, int timeout) throws IOException
    {
        if (gpsFixes != null && fixCount > 0)
            addGpsList(list, gpsFixes, fixCount);
        
        addMandatoryNode(list);
        ProtoBufConverter.convertToString(list, true);
        return sendData(ProtocolBufferUtil.listToByteArray(list), action, (byte)retryTime, timeout);
    }
    
    protected void addGpsList(Vector list, TnLocation[] gpsFixes, int fixCount) throws IOException
    {
        ProtoGpsList gpsList = ProtoBufServerProxyUtil.convertGpsList(gpsFixes, fixCount);
        ProtocolBuffer protoBuf = new ProtocolBuffer();
        protoBuf.setBufferData(convertToByteArray(gpsList));
        protoBuf.setObjType("gps");
        list.addElement(protoBuf);
    }
    
    protected void addMandatoryNode(Vector list) throws IOException
    {
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
        list.addElement(convertUserProfile(profile));
    }

    protected String getRegion()
    {
        MandatoryProfile profile;
        if (userProfileProvider != null)
        {
            profile = userProfileProvider.getMandatoryNode();
        }
        else
        {
            MandatoryNodeDao mandatoryNode = DaoManager.getInstance()
                    .getMandatoryNodeDao();
            profile = mandatoryNode.getMandatoryNode();
        }
        return profile.getUserInfo().region;
    }
    
    
    private ProtocolBuffer convertUserProfile(MandatoryProfile mandatoryProfile) throws IOException
    {
        ProtoUserProfile.Builder userProfileBuilder = ProtoUserProfile.newBuilder();
        UserInfo userInfo = mandatoryProfile.getUserInfo();
        userProfileBuilder.setMin(userInfo.phoneNumber);
        userProfileBuilder.setPassword(userInfo.pin);
        userProfileBuilder.setUserId(userInfo.userId);
        userProfileBuilder.setEqpin(userInfo.eqpin);
        userProfileBuilder.setLocale(userInfo.locale);
        userProfileBuilder.setRegion(userInfo.region);
        userProfileBuilder.setSsoToken(userInfo.ssoToken);
        userProfileBuilder.setGuideTone(userInfo.guideTone);
        int ptnType = 2;
        if(userInfo.ptnType != null && userInfo.ptnType.trim().length() > 0)
        {
            try
            {
                ptnType = Integer.valueOf(userInfo.ptnType);
            }
            catch (Exception e) 
            {
                Logger.log(AbstractProtobufServerProxy.class.getName(), e);
            }
        }
        userProfileBuilder.setPtnSource(ptnType);
        
        ClientInfo clientInfo = mandatoryProfile.getClientInfo();
        userProfileBuilder.setProgramCode(clientInfo.programCode);
        userProfileBuilder.setPlatform(clientInfo.platform);
        userProfileBuilder.setVersion(clientInfo.version);
        userProfileBuilder.setDevice(clientInfo.device);
        userProfileBuilder.setBuildNumber(clientInfo.buildNumber);
        userProfileBuilder.setGpsType(clientInfo.gpsTpye);
        userProfileBuilder.setProduct(clientInfo.productType);
        userProfileBuilder.setDeviceCarrier(clientInfo.deviceCarrier);
        
        UserPrefers userPref = mandatoryProfile.getUserPrefers();
        userProfileBuilder.setAudioFormat(userPref.audioFormat);
        userProfileBuilder.setImageType(userPref.imageType);
        userProfileBuilder.setAudioLevel(userPref.audioLevel);
        
        userProfileBuilder.setScreenWidth(""+ ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth());
        userProfileBuilder.setScreenHeight(""+((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight());
        
        //add device id into request
        TnDeviceInfo deviceInfo = TnTelephonyManager.getInstance().getDeviceInfo();
        if (deviceInfo != null)
        {
            userProfileBuilder.setDeviceID(deviceInfo.deviceId);
        }
        CredentialInfo credentialInfo = mandatoryProfile.getCredentialInfo();
        userProfileBuilder.setCredentialType(credentialInfo.credentialType);
        userProfileBuilder.setCredentialID(credentialInfo.credentialID);
        userProfileBuilder.setCredentialValue(credentialInfo.credentialValue);
        userProfileBuilder.setCredentialPassword(credentialInfo.credentialPassword);
        
        ProtocolBuffer buf = new ProtocolBuffer();
        buf.setBufferData(convertToByteArray(userProfileBuilder.build()));
        buf.setObjType(IServerProxyConstants.MANDATORY_NODE_MSG);
        return buf;
    }
    
    
    protected byte[] convertToByteArray(AbstractOutputWriter writer) throws IOException
    {
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.INFO, AbstractProtobufServerProxy.class.getName(), ToStringUtils.toString(writer));
        }
        
        return writer.toByteArray();
    }
}
