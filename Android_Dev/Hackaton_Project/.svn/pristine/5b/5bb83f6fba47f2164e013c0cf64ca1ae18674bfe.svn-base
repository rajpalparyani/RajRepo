/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FeedbackProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IFeedbackProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoFeedbackReq;
import com.telenav.j2me.framework.protocol.ProtoFeedbackResp;
import com.telenav.location.TnLocation;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class ProtoBufFeedbackProxy extends AbstractProtobufServerProxy implements IFeedbackProxy
{

    public ProtoBufFeedbackProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    

    public String sendFeedback(int feedbackType, String screenTitle, String userComment, long timeStamp, TnLocation[] gpsFixes, int fixes)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SEND_FEEDBACK, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(feedbackType));
            requestItem.params.addElement(screenTitle);
            requestItem.params.addElement(userComment);
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(timeStamp));
            requestItem.params.addElement(gpsFixes);
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(fixes));
            Vector list = createProtoBufReq(requestItem);
            
            return this.sendRequest(list, IServerProxyConstants.ACT_SEND_FEEDBACK);
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
        if (IServerProxyConstants.ACT_SEND_FEEDBACK.equals(requestItem.action))
        {
            if (requestItem.params != null && requestItem.params.size() > 5)
            {
                Integer feedbackType = (Integer) requestItem.params.elementAt(0);
                String screenTitle = (String) requestItem.params.elementAt(1);
                String userComment = (String) requestItem.params.elementAt(2);
                Long timeStamp = (Long) requestItem.params.elementAt(3);
                TnLocation[] gpsFixes = (TnLocation[]) requestItem.params.elementAt(4);
                Integer fixes = (Integer) requestItem.params.elementAt(5);

                ProtoFeedbackReq.Builder builder = ProtoFeedbackReq.newBuilder();
                builder.setFeedbackType("" + feedbackType);
                builder.setScreenTitle(screenTitle==null?"":screenTitle);
                builder.setUserComments(userComment==null?"":userComment);
                builder.setTimeStamp(timeStamp.longValue());
                


                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_SEND_FEEDBACK);
                requestVector.addElement(pb);
                
                if (gpsFixes != null)
                {
                    addGpsList(requestVector, gpsFixes, fixes.intValue());
                }
            }
        }
    }
    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_SEND_FEEDBACK.equals(protoBuf.getObjType()))
        {
            ProtoFeedbackResp resp = ProtoFeedbackResp.parseFrom(protoBuf.getBufferData());
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
        }
        
        return this.status;
    }

}
