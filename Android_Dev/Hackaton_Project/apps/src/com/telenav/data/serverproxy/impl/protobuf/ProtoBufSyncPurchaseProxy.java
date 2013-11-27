/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ProtoBufSyncPurchaseProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.io.IOException;
import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoSyncPurchasedReq;
import com.telenav.j2me.framework.protocol.ProtoSyncPurchasedResp;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.dashboard.AccountChangeListener;
import com.telenav.module.upsell.FeaturesManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-3-3
 */
public class ProtoBufSyncPurchaseProxy extends AbstractProtobufServerProxy implements ISyncPurchaseProxy
{
    protected boolean isNeedLogin = false;
    protected boolean isNeedPurchase = false;
    
    public ProtoBufSyncPurchaseProxy(Host host, Comm comm, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
    }
    
    public String sendSyncPurchaseRequest(String strAppCode)
    {
        try
        {
            if(strAppCode == null)
            {
                strAppCode = "";
            }
            
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_PURCHASE, this);
            requestItem.params = new Vector();
            requestItem.params.addElement(strAppCode);
            Vector list = createProtoBufReq(requestItem);
            return this.sendRequest(list, IServerProxyConstants.ACT_SYNC_PURCHASE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (listener != null)
                listener.networkError(this, IServerProxyListener.EXCEPTION_SEND, null);
            return "";
        }
        
    }
    
    public boolean isNeedLogin()
    {
        return isNeedLogin;
    }
    
    public boolean isNeedPurchase()
    {
        return isNeedPurchase;
    }
    
    protected void addRequestArgs(Vector requestVector, RequestItem requestItem) throws Exception
    {
        if(IServerProxyConstants.ACT_SYNC_PURCHASE.equals(requestItem.action))
        {
            if(requestItem.params != null && requestItem.params.size() > 0)
            {
                Object lastSyncTimeObj = requestItem.params.elementAt(0);
                
                ProtoSyncPurchasedReq.Builder builder = ProtoSyncPurchasedReq.newBuilder();
                builder.setAppCode((String)lastSyncTimeObj);
                
                ProtocolBuffer pb = new ProtocolBuffer();
                pb.setBufferData(convertToByteArray(builder.build()));
                pb.setObjType(IServerProxyConstants.ACT_SYNC_PURCHASE);
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
        if (IServerProxyConstants.ACT_SYNC_PURCHASE.equals(protoBuf.getObjType()))
        {
            ProtoSyncPurchasedResp resp = ProtoSyncPurchasedResp.parseFrom(protoBuf.getBufferData());       
            this.status = resp.getStatus();
            this.errorMessage = resp.getErrorMessage();
            this.isNeedLogin = resp.getNeedReLogin();
            Logger.log(Logger.INFO, this.getClass().getName(), "ACT_SYNC_PURCHASE -- resp.getNeedReLogin(): " + isNeedLogin);
            if(AppConfigHelper.isLoggerEnable)
            {
                if(isNeedLogin)
                {
                    Logger.log(Logger.ERROR, this.getClass().getName(), "XXX isNeedLogin = ture XXX ");
                }
                
                Logger.log(Logger.INFO, this.getClass().getName(), "-- ACT_SYNC_PURCHASE -- ");
                Logger.log(Logger.INFO, this.getClass().getName(), ToStringUtils.toString(resp));
            }
            
            //store the purchase status and then check this when launch the app next time.
			isNeedPurchase = false;
            if (resp.hasNeedPurchase() && resp.getFeatureList() != null && resp.getFeatureList().size() > 0)
            {
                isNeedPurchase = resp.getNeedPurchase();
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_SYNC_PURCHASE -- isNeedPurchase: " + isNeedPurchase);
                Logger.log(Logger.INFO, this.getClass().getName(), "ACT_SYNC_PURCHASE -- resp.getNeedReLogin(): " + isNeedLogin);
                ((DaoManager) DaoManager.getInstance()).getBillingAccountDao().setNeedPurchase(isNeedPurchase);
                if (!isNeedPurchase)
                {
                    DaoManager.getInstance().getBillingAccountDao().setCarrierStatus(BillingAccountDao.CARRIER_STATUS_FRESH);
                }
            }
            if(resp.hasAccountType())
            {
                String account_type = resp.getAccountType();
                if (account_type != null && account_type.trim().length() > 0)
                {
                    boolean needRefreshTraffic = !account_type
                            .equalsIgnoreCase(DaoManager.getInstance()
                                    .getMandatoryNodeDao().getMandatoryNode()
                                    .getClientInfo().productType);
                    Logger.log(Logger.INFO, this.getClass().getName(),
                        "-- Referesh Product Type -- " + account_type);
                    DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                            .getClientInfo().productType = account_type;
                    DaoManager.getInstance().getBillingAccountDao().setAccountType(account_type);
                    if (needRefreshTraffic)
                    {
                        AccountChangeListener.getInstance().accountChanged();
                        NavsdkUserManagementService.getInstance().userProfileUpdate();
                    }
                }
            }
            
            if(resp.hasPurchasedOfferName())
            {
                DaoManager.getInstance().getBillingAccountDao().setPurchaseOrderName(resp.getPurchasedOfferName());
            }
            
            if(resp.hasOfferPrice())
            {
                DaoManager.getInstance().getBillingAccountDao().setOfferPrize(String.valueOf(resp.getOfferPrice()));
            }
           
            if(resp.hasOfferCurrency())
            {
                DaoManager.getInstance().getBillingAccountDao().setOfferCurrrency(resp.getOfferCurrency());
            }
            
            if(resp.hasExpiredTime())
            {
                DaoManager.getInstance().getBillingAccountDao().setOfferExpireDate(resp.getExpiredTime());
            }
            
            if (resp.hasSocCodeOfCurrentProduct())
            {
                DaoManager.getInstance().getBillingAccountDao().setSocType(resp.getSocCodeOfCurrentProduct());
            }

            if(resp.hasNeedCancelSubscription())
            {
                boolean isSubscriptionCancellable = resp.getNeedCancelSubscription();
                DaoManager.getInstance().getBillingAccountDao().setSubscriptionCancellable(isSubscriptionCancellable);
            }
            
            if(resp.hasOfferCode())
            {
                String offerCode = resp.getOfferCode();
                DaoManager.getInstance().getBillingAccountDao().setOfferCode(offerCode);
            }
            
            if (resp.hasAccountStatus())
            {
                int accountStatus = Integer.parseInt(resp.getAccountStatus());
                DaoManager.getInstance().getBillingAccountDao().setAccountStatus(accountStatus);
            }
            
            if (status == IServerProxyConstants.SUCCESS)
            {
                Vector features = resp.getFeatureList();
                StringMap stringMap = createStringMap(features);
                if (stringMap.size() > 0)
                {
                    ((DaoManager) DaoManager.getInstance()).getUpsellDao().setUpsellData(stringMap);
                    ((DaoManager) DaoManager.getInstance()).getUpsellDao().store();
                    ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().initTrafficLayerDaoSetting();
                    resetMapSetting();
                    NavsdkUserManagementService.getInstance().featureListUpdate();
                }
            }
            Vector regions =resp.getSupportedRegions();
             if(regions !=null && regions.size()>0)
             {
                 int size = regions.size();
                 StringMap stringMap = createStringMap(regions);
                 if(size > 0)
                 {
                     DaoManager.getInstance().getBillingAccountDao().setSupportedRegion(stringMap);
                     
                 }
            }  
            DaoManager.getInstance().getBillingAccountDao().store();
        }
        return this.status;
    }

    
    private void resetMapSetting()
    {
        int layerSetting = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_MAP_LAYER_SETTING);
        boolean isTraffic = (layerSetting & 0x01) != 0;
        boolean isShowSatelliteOverlay = ((layerSetting & 0x02) != 0);  
        boolean isShowTrafficCameraOverlay = ((layerSetting & 0x04) != 0);
        
        //JY: If feature disabled, we should reset the map layer setting
        int trafficStatus = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW);
        if(isTraffic && (trafficStatus == FeaturesManager.FE_UNPURCHASED 
                || trafficStatus == FeaturesManager.FE_DISABLED))
        {
            layerSetting  ^= 0x01; 
        }
        
        int satelliteStatus = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_SATELLITE);
        if(isShowSatelliteOverlay && (satelliteStatus == FeaturesManager.FE_UNPURCHASED || satelliteStatus == FeaturesManager.FE_DISABLED))
        {
            layerSetting  ^=  0x02; 
        }
        
        int cameraStatus = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_MAP_LAYER_CAMERA);
        if(isShowTrafficCameraOverlay && (cameraStatus == FeaturesManager.FE_UNPURCHASED  || cameraStatus == FeaturesManager.FE_DISABLED))
        {
            layerSetting  ^=  0x04; 
        }
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_MAP_LAYER_SETTING, layerSetting);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    private StringMap createStringMap(Vector vector)
    {
        int size = vector.size();
        StringMap stringMap = new StringMap();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                ProtoProperty feature = (ProtoProperty) vector.elementAt(i);
                if (feature.hasKey())
                {
                    stringMap.put(feature.getKey(), feature.getValue());
                }
            }
        }
        return stringMap;
    }

}
