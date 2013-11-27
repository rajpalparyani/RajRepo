/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufShareAddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import net.jarlehansen.protobuf.javame.ByteString;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IClientLoggingProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoClientLogging;
import com.telenav.j2me.framework.protocol.ProtoClientLoggingReq;
import com.telenav.j2me.framework.protocol.ProtoClientLoggingResp;
import com.telenav.j2me.framework.protocol.ProtoLoginInfoLoggingReq;
import com.telenav.j2me.framework.protocol.ProtoLoginInfoLoggingResp;

/**
 *@author qyweng (qyweng@telenav.cn)
 *@date 2011-3-8
 */
public class ProtoClientLoggingProxy extends AbstractProtobufServerProxy implements IClientLoggingProxy
{

    public ProtoClientLoggingProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    
    
    public String sendClientLogging(byte[] clientLog,String fileName,String postfix)
    {
        if (clientLog == null || fileName == null || postfix == null)
        {
            throw new IllegalArgumentException("address/contacts is null or size is wrong.");
        }
        
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_CLIENT_LOGGING, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(clientLog);
            requestItem.params.addElement(fileName);
            requestItem.params.addElement(postfix);
            
            Vector list = createProtoBufReq(requestItem);

            return this.sendRequest(list, IServerProxyConstants.ACT_CLIENT_LOGGING);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
        
    }

    public String sendLoginInfo(String devicePtn, String deviceCarrier)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SEND_LOGIN_INFO, this);
            if(requestItem.params == null)
                requestItem.params = new Vector();
            
            requestItem.params.addElement(devicePtn);
            requestItem.params.addElement(deviceCarrier);
            
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_SEND_LOGIN_INFO);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            if(listener != null)
            {
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            }
            return "";
        }
    }
    
  
    protected void addRequestArgs(Vector requestVector,RequestItem requestItem) throws Exception
    {
        if (IServerProxyConstants.ACT_CLIENT_LOGGING.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() >= 3)
            {
                ProtoClientLoggingReq.Builder builder = ProtoClientLoggingReq.newBuilder();
                ProtoClientLogging.Builder clientLog = ProtoClientLogging.newBuilder();
                clientLog.setLogData(ByteString.copyFrom(((byte[])requestItem.params.elementAt(0))));
                clientLog.setFileName((String)requestItem.params.elementAt(1));
                clientLog.setFilePostfix((String)requestItem.params.elementAt(2));
                clientLog.setSendTime(System.currentTimeMillis());
                builder.addElementClientLogging(clientLog.build());
                
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_CLIENT_LOGGING);
                requestVector.addElement(pb);
            }
            else
            {
                throw new IllegalArgumentException("request params is null or size is wrong.");
            }
        }
        else if (IServerProxyConstants.ACT_SEND_LOGIN_INFO.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() >= 2)
            {
                String devicePTN = (String)requestItem.params.elementAt(0);
                String deviceCarrier = (String)requestItem.params.elementAt(1);
                ProtoLoginInfoLoggingReq.Builder builder = ProtoLoginInfoLoggingReq.newBuilder();
                builder.setCarrierFromDevice(deviceCarrier);
                builder.setPtnFromDevice(devicePTN);
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_SEND_LOGIN_INFO);
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
        if (IServerProxyConstants.ACT_CLIENT_LOGGING.equals(protoBuf.getObjType()))
        {
            ProtoClientLoggingResp resp = ProtoClientLoggingResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
        } 
        else if (IServerProxyConstants.ACT_SEND_LOGIN_INFO.equals(protoBuf.getObjType()))
        {
            ProtoLoginInfoLoggingResp resp = ProtoLoginInfoLoggingResp.parseFrom(protoBuf.getBufferData());
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
        }
        return this.status;
    }
}
