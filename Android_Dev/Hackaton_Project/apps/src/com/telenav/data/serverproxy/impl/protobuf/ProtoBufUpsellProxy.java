/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ProtobufUpsellProxy.java
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
import com.telenav.data.serverproxy.impl.IUpsellProxy;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoCancellationReq;
import com.telenav.j2me.framework.protocol.ProtoCancellationResp;
import com.telenav.j2me.framework.protocol.ProtoOfferTerm;
import com.telenav.j2me.framework.protocol.ProtoPreparePurchaseReq;
import com.telenav.j2me.framework.protocol.ProtoPreparePurchaseResp;
import com.telenav.j2me.framework.protocol.ProtoPurchaseOption;
import com.telenav.j2me.framework.protocol.ProtoPurchaseReq;
import com.telenav.j2me.framework.protocol.ProtoPurchaseResp;
import com.telenav.j2me.framework.protocol.ProtoUpsellReq;
import com.telenav.j2me.framework.protocol.ProtoUpsellResp;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.logger.Logger;
import com.telenav.module.upsell.OfferTerm;
import com.telenav.module.upsell.UpsellOption;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2012-11-28
 */
public class ProtoBufUpsellProxy extends AbstractProtobufServerProxy implements IUpsellProxy
{
    Vector upsellOptions = new Vector();
    String orderId=null;
    
    public ProtoBufUpsellProxy(Host host, Comm comm,
            IServerProxyListener serverProxyListener,
            IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }

    protected int parseRequestSpecificData(ProtocolBuffer protoBuf) throws IOException
    {
        if(IServerProxyConstants.ACT_UPSELL.equals(protoBuf.getObjType()))
        {
            ProtoUpsellResp resp = ProtoUpsellResp.parseFrom(protoBuf.getBufferData());
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_UPSELL -- resp : \n" + ToStringUtils.toString(resp));
            this.status = resp.getStatus();
            parseOptions(resp);
        }
        else if (IServerProxyConstants.ACT_SINGLE_PURCHASE.equals(protoBuf.getObjType()))
        {
            ProtoPurchaseResp resp = ProtoPurchaseResp.parseFrom(protoBuf.getBufferData());
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_SINGLE_PURCHASE -- resp : \n" + ToStringUtils.toString(resp));
            this.errorMessage = resp.getErrorMessage();
            this.status = resp.getStatus();
        } 
        else if (IServerProxyConstants.ACT_PREPARE_PURCHASE.equals(protoBuf.getObjType()))
        {
            ProtoPreparePurchaseResp resp = ProtoPreparePurchaseResp.parseFrom(protoBuf.getBufferData());
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_PREPARE_PURCHASE -- resp : \n" + ToStringUtils.toString(resp));
            this.status = resp.getStatus();
            this.orderId = String.valueOf(resp.getOrderId());
        }
        else if (IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION.equals(protoBuf.getObjType()))
        {
            ProtoCancellationResp resp = ProtoCancellationResp.parseFrom(protoBuf.getBufferData());
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_CANCEL_SUBSCRIPTION -- resp : \n" + ToStringUtils.toString(resp));
            this.status = resp.getStatus();
        }
        return status;
    }

    public String requestUpsellInfo(boolean isNeedActiveOffer)
    {
        try
        {
            upsellOptions.removeAllElements();
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_UPSELL, this);
            if(requestItem.params == null)
                requestItem.params = new Vector();
            
            requestItem.params.addElement(PrimitiveTypeCache.valueOf(isNeedActiveOffer));
            
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_UPSELL);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if(listener != null)
            {
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            }
            return "";
        }
    }

    public String preparePurchase(String offerCode)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_PREPARE_PURCHASE, this);
            if(requestItem.params == null)
                requestItem.params = new Vector();
            
            requestItem.params.addElement(offerCode);
            
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_PREPARE_PURCHASE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if(listener != null)
            {
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            }
            return "";
        }
    }
    
    public String submitUpsellSelection(String offerCode)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SINGLE_PURCHASE, this);
            if(requestItem.params == null)
                requestItem.params = new Vector();
            
            requestItem.params.addElement(offerCode);
            
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_SINGLE_PURCHASE);
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
    

    @Override
    public String cancelSubscription(String offerCode)
    {
        try
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION, this);
            if(requestItem.params == null)
                requestItem.params = new Vector();
            requestItem.params.addElement(offerCode);
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION);
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
    
    protected void addRequestArgs(Vector requestVector, RequestItem requestItem)
            throws Exception
    {
        if(IServerProxyConstants.ACT_UPSELL.equals(requestItem.action))
        {
            ProtoUpsellReq.Builder builder = ProtoUpsellReq.newBuilder();
            Boolean isNeedsctiveOffer = (Boolean)requestItem.params.elementAt(0);
            builder.setIsNeedActiveOffer(isNeedsctiveOffer.booleanValue());
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_UPSELL);
            requestVector.addElement(pb);
        }
        else if(IServerProxyConstants.ACT_SINGLE_PURCHASE.equals(requestItem.action))
        {
            String offerCode = (String)requestItem.params.elementAt(0);
            ProtoPurchaseReq.Builder builder = ProtoPurchaseReq.newBuilder();
            builder.setOfferCode(offerCode);
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_SINGLE_PURCHASE);
            requestVector.addElement(pb);
        } 
        else if(IServerProxyConstants.ACT_PREPARE_PURCHASE.equals(requestItem.action))
        {
            String offerCode = (String)requestItem.params.elementAt(0);
            ProtoPreparePurchaseReq.Builder builder = ProtoPreparePurchaseReq.newBuilder();
            builder.setOfferCode(offerCode);
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_PREPARE_PURCHASE);
            requestVector.addElement(pb);
        }
        else if(IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION.equals(requestItem.action))
        {
            String offerCode = (String)requestItem.params.elementAt(0);
            ProtoCancellationReq.Builder builder = ProtoCancellationReq.newBuilder();
            builder.setOffercode(offerCode);
            ProtocolBuffer pb = new ProtocolBuffer();
            pb.setBufferData(convertToByteArray(builder.build()));
            pb.setObjType(IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION);
            requestVector.addElement(pb);
        }
    }

    private void parseOptions(ProtoUpsellResp resp)
    {
        Vector options = resp.getPurchaseOptions();
        for (int i = 0; i < options.size(); i++)
        {
            ProtoPurchaseOption option = (ProtoPurchaseOption) options.elementAt(i);
            UpsellOption upsellOption = new UpsellOption();
            upsellOption.setSourceTypeCode(option.getBillingSourceType());
            upsellOption.setSocCode(option.getSocCode());
            upsellOption.setDisplayDesc(option.getDisplayDesc());
            upsellOption.setDisplayName(option.getDisplayName());
            upsellOption.setOfferCode(option.getOfferCode());
            upsellOption.setPriceUnit(option.getPriceUnit());
            upsellOption.setPriceValue(option.getPriceValue());
            if (option.hasOfferTerm())
            {
                OfferTerm offerTerm = new OfferTerm();
                ProtoOfferTerm protoOfferTerm = option.getOfferTerm();
                offerTerm.setOfferTermId(protoOfferTerm.getOfferTermId());
                offerTerm.setPayPeriod(protoOfferTerm.getPayPeriod());
                offerTerm.setPayPeriodUnit(protoOfferTerm.getPayPeriodUnit());
                offerTerm.setPayType(protoOfferTerm.getPayType());
                offerTerm.setPromoPeriod(protoOfferTerm.getPromoPeriod());
                offerTerm.setPromoPeriodUnit(protoOfferTerm.getPromoPeriodUnit());
                upsellOption.setOfferTerm(offerTerm);
            }
            upsellOptions.addElement(upsellOption);
        }
    }
    
    public Vector getUpsellOptions()
    {
        return upsellOptions;
    }

    public String getOrderId()
    {
        return orderId;
    }

}
