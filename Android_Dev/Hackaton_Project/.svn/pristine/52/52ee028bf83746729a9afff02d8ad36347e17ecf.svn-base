/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * UpSellModel.java
 *
 */
package com.telenav.module.upsell;

import java.util.Iterator;
import java.util.Vector;

import android.content.Context;
import android.widget.Toast;

import com.telenav.ads.MobileAppConversionTracker;
import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.AndroidBillingDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.billing.MarketPurchaseRequest;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.IUpsellProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.marketbilling.IMarketBillingConstants;
import com.telenav.module.marketbilling.IMarketBillingListener;
import com.telenav.module.marketbilling.MarketBillingHandler;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPreference;
import com.telenav.res.IStringUpSell;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
class UpSellModel extends AbstractCommonNetworkModel implements IUpSellConstants, IMarketBillingListener
{
    private long lastPurchaseTime = -1;
    private Object marketPurchaseMutex = new Object();
    private String marketPurchaseStatus = IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_COMMON_ERROR;

    private boolean isCancelSucc = false;
    private boolean isCancellingSubscription = false;
    
    private boolean isPurchasing = false;
    private String singlePurchaseErrorMsg = null;

    public UpSellModel()
    {
        
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                boolean isNeedActiveOffer = true;
                getUpsellInfo(isNeedActiveOffer);
                break;
            }
            case ACTION_CHECK_AMP_PROMOTE_TOAST:
            {
                UpsellOption upsellOption = (UpsellOption) get(KEY_O_UPSELL_OPTIONS_SELECT);
                if (upsellOption != null)
                {
                    OfferTerm offerTerm = upsellOption.getOfferTerm();
                    if (offerTerm != null)
                    {
                        int promoPeriod = offerTerm.getPromoPeriod();

                        if (promoPeriod > 0) // means hasFree
                        {
                            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Context mContext = AndroidPersistentContext.getInstance().getContext();
                                    String freeTrialMsg  = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.RES_CONFIRM_FREE_TRIAL, IStringUpSell.FAMILY_UPSELL);
                                    Toast toast = Toast.makeText(mContext, freeTrialMsg, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                    }
                }
                break;
            }
            case ACTION_EXIT_CHECK:
            {
                if (this.getBool(KEY_B_IS_START_UP))
                {
                    TeleNavDelegate.getInstance().exitApp();
                }
                else
                {
                    postModelEvent(EVENT_MODEL_GOTO_PREV);
                }
                break;
            }
            case ACTION_UPSELL_OPTION_SUBMIT:
            {
                if(!isPurchasing)
                {
                    isPurchasing = true;
                    UpsellOption upsellOption = ((UpsellOption) this.get(KEY_O_UPSELL_OPTIONS_SELECT));
                    String offerCode = upsellOption.getOfferCode();
                    
                    //if source type is carrier
                    if (UpsellOption.CARRIER_BILLING_SOURCE_TYPE.equalsIgnoreCase(upsellOption.getSourceTypeCode()))
                    {
                        submitUpsellSelection(offerCode);
                    }
                    else
                    {
                        //if price is 0, goto carrier
                        if (upsellOption.getPriceValue() - 0 == 0)
                        {
                            submitUpsellSelection(offerCode);
                        }
                        else //goto android market
                        {
                            preparePurchase(offerCode);
                        }
                    }
                }
                break;
            }
            case ACTION_CANCEL_SUBSCRIPTION:
            {
                if(!isCancellingSubscription)
                {
                    isCancellingSubscription = true;
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                    IUpsellProxy upsellProxy = ServerProxyFactory.getInstance().createUpsellProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
                    String offerCode = DaoManager.getInstance().getBillingAccountDao().getOfferCode();
                    if(offerCode != null && offerCode.length() > 0)
                    {
                        upsellProxy.cancelSubscription(offerCode);
                    }
                    else 
                    {
                        isCancellingSubscription = false;
                        this.postModelEvent(EVENT_MODEL_CANCEL_EXCEPTION);
                    }
                }
                break;
            }
        }
    }

    protected void getUpsellInfo(boolean isNeedActiveOffer)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        IUpsellProxy upsellProxy = ServerProxyFactory.getInstance().createUpsellProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        upsellProxy.requestUpsellInfo(isNeedActiveOffer);
    }
    
    protected void submitUpsellSelection(String offerCode)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        IUpsellProxy upsellProxy = ServerProxyFactory.getInstance().createUpsellProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        upsellProxy.submitUpsellSelection(offerCode);
    }
    
    protected void preparePurchase(String offerCode)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        IUpsellProxy upsellProxy = ServerProxyFactory.getInstance().createUpsellProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
        upsellProxy.preparePurchase(offerCode);
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        if (proxy instanceof IUpsellProxy)
        {
            String action = proxy.getRequestAction();
            if (action.equalsIgnoreCase(IServerProxyConstants.ACT_UPSELL))
            {
                Vector options = ((IUpsellProxy) proxy).getUpsellOptions();
                if (options != null)
                {
                    Iterator it = options.iterator();
                    while (it.hasNext())
                    {
                        UpsellOption upsellOption =  (UpsellOption) it.next();
                        if (upsellOption.getDisplayName() == null || upsellOption.getDisplayName().trim().length() == 0 )
                        {
                            it.remove();
                        }
                    }
                }
                put(KEY_V_UPSELL_OPTIONS, options);
                if (options != null && options.size() > 0)
                {
                    if (this.getBool(KEY_B_IS_START_UP))
                        saveData();
                    postModelEvent(EVENT_MODEL_UPSELL_OPTIONS);
                }
                else
                {
                    syncPurchase();
                }
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_SINGLE_PURCHASE))
            {
                int status = proxy.getStatus();
                if (status == IServerProxyConstants.SUCCESS)
                {
                    isPurchasing = false;
                    syncPurchase();
                }
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_PREPARE_PURCHASE))
            {
                int status = proxy.getStatus();
                if (status == IServerProxyConstants.SUCCESS)
                {
                    UpsellOption upsellOption = ((UpsellOption) this.get(KEY_O_UPSELL_OPTIONS_SELECT));
                    String productId = upsellOption.getSocCode();
                    String orderId = ((IUpsellProxy) proxy).getOrderId();
                    completeAppstorePurchase(productId, orderId);
                }
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION))
            {
                int status = proxy.getStatus();
                if (status == IServerProxyConstants.SUCCESS)
                {
                    //SyncPurchase used to update cancelable and offerCode
                    isCancelSucc = true;
                    syncPurchase();
                }
            }
        }
        else if(proxy instanceof ISyncPurchaseProxy)
        {
            int status = proxy.getStatus();
            if (status == IServerProxyConstants.SUCCESS)
            {
                if (this.isCancellingSubscription) // if this is cancel subscription
                {
                    handleCancelSubscription(null);
                }
                else
                {
                    Vector options = this.getVector(KEY_V_UPSELL_OPTIONS);
                    if (options != null && options.size() > 0)
                    {
                        // handle the case of duplicate purchase
                        if (singlePurchaseErrorMsg != null)
                        {
                            if (FeaturesManager.getInstance().isAllFeaturePurchased())
                            {
                                postSuccessEvent();
                            }
                            else
                            {
                                handleSinglePurchaseError(proxy);
                            }
                        }
                        else
                        {
                            postSuccessEvent();
                        }
                    }
                    else
                    {
                        postModelEvent(EVENT_MODEL_SCOUT_UPSELL_PURCHASE_SUCCESS);
                    }
                }
            }
        }
    }

    private void postSuccessEvent()
    {
        logKtUpsellEvent();
        if (isScoutUser())
        {
            postModelEvent(EVENT_MODEL_SCOUT_UPSELL_PURCHASE_SUCCESS);
        }
        else
        {
            postModelEvent(EVENT_MODEL_NON_SCOUT_UPSELL_PURCHASE_SUCCESS);
        }
    }

    private void logKtUpsellEvent()
    {
        UpsellOption upsellOption = ((UpsellOption) get(KEY_O_UPSELL_OPTIONS_SELECT));
        if(upsellOption != null)
        {
            OfferTerm offerTerm = upsellOption.getOfferTerm();
            if(offerTerm != null)
            {
                int payPeriod = offerTerm.getPayPeriod();
                int payPeriodUnit = offerTerm.getPayPeriodUnit();
                
                if(payPeriod == 1 && payPeriodUnit == OfferTerm.PERIOD_YEAR_UNIT) //means 1 year
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_UPGRADE,
                          KontagentLogger.UPGRADE_YEARLY_UPGRADE);
                    MobileAppConversionTracker.getInstance().trackActionDetail(MobileAppConversionTracker.ACTION_PREMIUM_UPGRADE,
                        upsellOption.getPriceValue(), upsellOption.getPriceUnit());
                }
                else if(payPeriod == 0) //means free
                {
                    //MONTHFREE
                    //DO NOTHING
                }
                else
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_UPGRADE,
                        KontagentLogger.UPGRADE_MONTHLY_UPGRADE);
                    MobileAppConversionTracker.getInstance().trackActionDetail(MobileAppConversionTracker.ACTION_PREMIUM_UPGRADE,
                        upsellOption.getPriceValue(), upsellOption.getPriceUnit());
                }
            }
        }
    }
    
    private void saveData()
    {
        updateDisplayTime();
    }

    private void updateDisplayTime()
    {
        int displayTimes = DaoManager.getInstance().getBillingAccountDao().getUpsellDisplayTimes();
        DaoManager.getInstance().getBillingAccountDao().setUpsellDisplayTimes(displayTimes + 1);
        DaoManager.getInstance().getBillingAccountDao().store();
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if(proxy instanceof IUpsellProxy)
        {

            String action = proxy.getRequestAction();
            if (action.equalsIgnoreCase(IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION))
            {
                //Even though cancel subscription fails, it will still do syncPurchase. This will avoid the case
                //that cancel subscription is success,but synPurchase failed(although it has low probability)
                //and will result in cancel the subscription which had been cancelled before.
                //reset isSuccessCancelSubscription and isCancellingSubscription after handling
                String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);

                handleCancelSubscription(errorMessage);
                return;
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_SINGLE_PURCHASE))
            {
                isPurchasing = false;
                super.networkError(proxy, statusCode, jobId);
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_UPSELL))
            {
                handleGetUpsellOptionsError(proxy);
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_PREPARE_PURCHASE))
            {
                isPurchasing = false;
                super.networkError(proxy, statusCode, jobId);
            }
        }
        else
        {
            super.networkError(proxy, statusCode, jobId);
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if(proxy instanceof IUpsellProxy)
        {
          
            String action = proxy.getRequestAction();
            if (action.equalsIgnoreCase(IServerProxyConstants.ACT_CANCEL_SUBSCRIPTION))
            {
                //Even though cancel subscription fails, it will still do syncPurchase. This will avoid the case
                //that cancel subscription is success,but synPurchase failed(although it has low probability)
                //and will result in cancel the subscription which had been cancelled before.
                isCancelSucc = false;
                this.syncPurchase();
                return;
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_SINGLE_PURCHASE))
            {
                handleSinglePurchaseError(proxy);
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_UPSELL))
            {
                handleGetUpsellOptionsError(proxy);
            }
            else if (action.equalsIgnoreCase(IServerProxyConstants.ACT_PREPARE_PURCHASE))
            {
                isPurchasing = false;
                super.transactionError(proxy);
            }
        }
        else
        {
            if (proxy instanceof ISyncPurchaseProxy)
            {
                if(this.isCancellingSubscription)
                {
                    handleCancelSubscription(null);
                }
                else
                {
                    // do nothing, as sync purchase will trigger next time if fails
                }
            }
            else
            {
                super.transactionError(proxy);
            }
        }
    }
    
    private void handleGetUpsellOptionsError(AbstractServerProxy proxy)
    {
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        this.put(KEY_S_ERROR_MESSAGE, errorMessage);
        this.postModelEvent(EVENT_MODEL_GET_UPSELL_OPTION_ERROR);
        
    }
    
    
    private void handleSinglePurchaseError(AbstractServerProxy proxy)
    {
        if (singlePurchaseErrorMsg == null)
        {
            syncPurchase();
            String msg = proxy.getErrorMsg();
            if (msg != null && msg.length() > 0)
            {
                singlePurchaseErrorMsg = msg;
            }
            else
            {
                singlePurchaseErrorMsg = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringUpSell.RES_PURCHASE_ERROR, IStringUpSell.FAMILY_UPSELL);
            }
        }
        else
        {
            isPurchasing = false; 
            postErrorMessage(singlePurchaseErrorMsg);
        }
    }
    
    protected void handleCancelSubscription(String errorMessage)
    {
        boolean isSubscriptionCancellable = DaoManager.getInstance().getBillingAccountDao().getSubscriptionCancellable();
        if(isSubscriptionCancellable)
        {
            if(isCancelSucc)
            {
                this.put(KEY_B_IS_CANCEL_SUBSCRIPTION_SUCC, true);
                this.postModelEvent(EVENT_MODEL_CANCEL_SUBSCRIPTION_SUCCESS);
            }
            else
            {
                if(errorMessage == null || errorMessage.trim().length() == 0)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.CANCEL_SUBSCRIPTION_FAIL, IStringPreference.FAMILY_PREFERENCE);
                }
                
                this.put(ICommonConstants.KEY_S_ERROR_MESSAGE, errorMessage);
                this.postModelEvent(EVENT_MODEL_CANCEL_EXCEPTION);
            }
        }
        else
        {
            this.put(KEY_B_IS_CANCEL_SUBSCRIPTION_SUCC, true);
            this.postModelEvent(EVENT_MODEL_CANCEL_SUBSCRIPTION_SUCCESS);
        }
        
        //reset isSuccessCancelSubscription and isCancellingSubscription after handling
        isCancelSucc = false;
        isCancellingSubscription = false;
    }
    
    protected boolean isScoutUser()
    {
        boolean isScountUser = false;
        if (AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName)
                || AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
        {
            isScountUser = true;
        }
        return isScountUser;
    }
    
    
    protected void syncPurchase()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
        ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance().createSyncPurchaseProxy(null,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
    }
    
    
    protected void completeAppstorePurchase(String productId, String orderId)
    {
        long interval = System.currentTimeMillis() - lastPurchaseTime;
        if (interval >= 0 && interval < 3000)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: Click too frequently!");
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: completeAppstorePurchase Start"
                    + " productId: " + productId + " orderId: " + orderId);
            boolean lastPurchaseUncompleted = false;
            String signedData = "";
            String signature = "";
            AndroidBillingDao androidBillingDao = DaoManager.getInstance().getAndroidBillingDao();
            if (androidBillingDao != null)
            {
                Vector pendingPurchases = androidBillingDao.getPurchaseRequests();
                if (pendingPurchases != null)
                {
                    for (int i = 0; i < pendingPurchases.size(); i++)
                    {
                        MarketPurchaseRequest request = (MarketPurchaseRequest) pendingPurchases.elementAt(i);
                        if (request != null && request.getStatus() == -1)
                        {
                            lastPurchaseUncompleted = true;
                            signedData = request.getSignedData();
                            signature = request.getSignature();
                            Logger.log(Logger.INFO, this.getClass().getName(),
                                "MarketBilling: completeAppstorePurchase lastPurchaseUncompleted "
                                        + lastPurchaseUncompleted);
                            break;
                        }
                    }
                }
            }

            MarketBillingHandler handler = TeleNavDelegate.getInstance().getBillingHandler();
            if (handler != null)
            {
                handler.addMarketListener(this);
                if (!lastPurchaseUncompleted)
                {
                    handler.requestPurchase(productId, orderId);
                    lastPurchaseTime = System.currentTimeMillis();

                    synchronized (marketPurchaseMutex)
                    {
                        try
                        {
                            marketPurchaseMutex.wait(10 * 60 * 1000);
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
                else
                {
                    handler.doVerifyPurchase(signedData, signature);
                }
            }

            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: completeAppstorePurchase finished"
                    + " marketPurchaseStatus: " + marketPurchaseStatus);
        }
    }
    
    protected void deactivateDelegate()
    {
        try
        {
            CommManager.getInstance().getComm().cancelAllJobs(IServerProxyConstants.ACT_UPSELL);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    @Override
    public void purchaseFinished(String statusCode)
    {
        marketPurchaseStatus = statusCode;

        synchronized (marketPurchaseMutex)
        {
            try
            {
                marketPurchaseMutex.notifyAll();
            }
            catch (Exception e)
            {
            }
        }
        
        isPurchasing = false;
        lastPurchaseTime = -1L;
        
        if(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_SUCC.equals(statusCode))
        {
            syncPurchase();
        }
        else if(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_USER_CANCLE.equals(statusCode))
        {
            this.postModelEvent(EVENT_MODEL_COMMON_BACK);
        }
        else
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
            postErrorMessage(errorMessage);
        }
    }

}