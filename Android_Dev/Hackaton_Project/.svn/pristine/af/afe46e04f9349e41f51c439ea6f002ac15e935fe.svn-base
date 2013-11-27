/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ProtoBufAndroidBillingProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.datatypes.billing.VerifiedPurchase;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IAndroidBillingProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoDeclinePurchaseReq;
import com.telenav.j2me.framework.protocol.ProtoDeclinePurchaseResp;
import com.telenav.j2me.framework.protocol.ProtoMessage;
import com.telenav.j2me.framework.protocol.ProtoMessageProcessReq;
import com.telenav.j2me.framework.protocol.ProtoMessageProcessResp;
import com.telenav.logger.Logger;

/**
 *@author gbwang
 *@date 2012-2-16
 */
public class ProtoBufAndroidBillingProxy extends AbstractProtobufServerProxy implements
        IAndroidBillingProxy
{
    Vector verifiedPurchases = new Vector();
    
    int minorStatus = -1;

    public ProtoBufAndroidBillingProxy(Host host, Comm comm,
            IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    public String requestVerifyPurchase(String signedData,
            String signature)
    {
        Logger.log(Logger.INFO, this.getClass().getName(),
            "requestVerifyPurchase ");
        try
        {
            RequestItem requestItem = new RequestItem(
                    IServerProxyConstants.ACT_MESSAGE_PROCESS, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(signedData);
            requestItem.params.addElement(signature);

            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_MESSAGE_PROCESS);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }

    public String declinePurchase(String orderId)
    {
        Logger.log(Logger.INFO, this.getClass().getName(),
            "declinePurchase " + "billingOrderId = " + orderId);
        try
        {
            long billingOrderId = Long.parseLong(orderId);
            RequestItem requestItem = new RequestItem(
                    IServerProxyConstants.ACT_DECLINE_PURCHASE, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(billingOrderId);

            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_DECLINE_PURCHASE);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
    }

    protected void addRequestArgs(Vector requestVector, RequestItem requestItem)
            throws Exception
    {
        if (IServerProxyConstants.ACT_MESSAGE_PROCESS.equals(requestItem.action))
        {
            String signedData = (String) requestItem.params.elementAt(0);
            String signature = (String) requestItem.params.elementAt(1);

            ProtoMessageProcessReq.Builder builder = ProtoMessageProcessReq.newBuilder();
            builder.setSignedData(signedData);
            builder.setSignature(signature);

            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_MESSAGE_PROCESS);
            requestVector.addElement(pb);
        }
        else if (IServerProxyConstants.ACT_DECLINE_PURCHASE.equals(requestItem.action))
        {
            long billingOrderId = (Long) requestItem.params.elementAt(0);
            ProtoDeclinePurchaseReq.Builder builder = ProtoDeclinePurchaseReq
                    .newBuilder();
            builder.setOrderId(billingOrderId);

            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_DECLINE_PURCHASE);
            requestVector.addElement(pb);
        }
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if (IServerProxyConstants.ACT_MESSAGE_PROCESS.equals(protoBuf.getObjType()))
        {
            ProtoMessageProcessResp resp = ProtoMessageProcessResp.parseFrom(protoBuf
                    .getBufferData());
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
            if (status == IServerProxyConstants.SUCCESS && null != resp.getAmpMessages())
            {
                Vector  messages  = resp.getAmpMessages();
                ProtoMessage message;
                VerifiedPurchase vp;
                for (int i = 0; i < messages.size(); i++)
                {
                    message = (ProtoMessage) messages.elementAt(i);
                    vp = new VerifiedPurchase();
                    vp.setNotificationId(message.getNotificationId());
                    vp.setproductId(message.getProductId());
                    vp.setPurchaseState(message.getPurchaseState());
                    vp.setStatus(message.getStatus());
                    this.verifiedPurchases.addElement(vp);
                }
            }
            else if (status == IServerProxyConstants.FAILED)
            {
                this.minorStatus = resp.getMinorStatus();
            }
        }
        else if (IServerProxyConstants.ACT_DECLINE_PURCHASE.equals(protoBuf.getObjType()))
        {
            ProtoDeclinePurchaseResp resp = ProtoDeclinePurchaseResp.parseFrom(protoBuf
                    .getBufferData());
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
        }
        Logger.log(Logger.INFO, this.getClass().getName(),
            "action = " + protoBuf.getObjType() + "status = " + status);
        return this.status;
    }

    public Vector getVerifiedPurchases()
    {
        return this.verifiedPurchases;
    }

    public int getMinorStatus()
    {
        return this.minorStatus;
    }

}
