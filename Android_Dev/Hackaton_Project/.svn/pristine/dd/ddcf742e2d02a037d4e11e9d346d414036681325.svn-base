/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MarketModel.java
 *
 */
package com.telenav.module.marketbilling;

import java.util.Vector;

import android.content.Context;
import android.widget.Toast;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.AndroidBillingDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.billing.VerifiedPurchase;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IAndroidBillingProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.logger.Logger;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author gbwang
 *@date 2012-5-10
 */
public class MarketBillingModel extends AbstractCommonNetworkModel implements IMarketBillingConstants
{
    private static String statusCode = IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_COMMON_ERROR;
    
    protected Vector listeners = new Vector();

    public void addListener(IMarketBillingListener listener)
    {
        listeners.addElement(listener);
    }

    public void removeListener(IMarketBillingListener listener)
    {
        listeners.remove(listener);
    }
    
    public void handlePurchaseResponse(String statusCode)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: handlePurchaseResponse statusCode" + statusCode);
        for (int i = 0; i < listeners.size(); i++)
        {
            final IMarketBillingListener listener = (IMarketBillingListener) listeners.elementAt(i);
            listener.purchaseFinished(statusCode);
        }
        
        if(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_SUCC.equals(statusCode))
        {
            final String toastMsg = ResourceManager.getInstance().getCurrentBundle().getString(
                IStringCommon.RES_MARKET_PURCHASE_SUCC, IStringCommon.FAMILY_COMMON);
            if(toastMsg != null && toastMsg.trim().length() > 0)
            {
                ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Context mContext = AndroidPersistentContext.getInstance().getContext();
                        Toast toast = Toast.makeText(mContext, toastMsg, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }
    }

    public void doVerifyPurchase(String signedData, String signature)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
        IAndroidBillingProxy proxy = ServerProxyFactory.getInstance()
                .createAndroidBillingProxy(null, CommManager.getInstance().getComm(),
                    this, userProfileProvider);

        proxy.requestVerifyPurchase(signedData, signature);
    }

    public void declinePurchase(String billingOrderId)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
        IAndroidBillingProxy proxy = ServerProxyFactory.getInstance()
                .createAndroidBillingProxy(null, CommManager.getInstance().getComm(),
                    this, userProfileProvider);

        proxy.declinePurchase(billingOrderId);
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof IAndroidBillingProxy)
        {
            String action = proxy.getRequestAction();
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: " + action
                    + " transactionFinished");

            if (IServerProxyConstants.ACT_MESSAGE_PROCESS.equals(action))
            {
                Vector verifiedPurchases = ((IAndroidBillingProxy) proxy)
                        .getVerifiedPurchases();
                if (verifiedPurchases != null)
                {
                    int size = verifiedPurchases.size();
                    for (int i = 0; i < size; i++)
                    {
                        VerifiedPurchase purchase = (VerifiedPurchase) verifiedPurchases
                                .elementAt(i);
                        if (purchase != null)
                        {
                            String productId = purchase.getProductId();
                            int purchaseState = purchase.getPurchaseState();
                            String notificationId = purchase.getNotificationId();
                            int verifyStatus = purchase.getStatus();
                            Logger
                                    .log(Logger.INFO, this.getClass().getSimpleName(),
                                        "MarketBilling: transactionFinished productId: "
                                                + productId + " notificationId: "
                                                + notificationId + " purchaseState: "
                                                + purchaseState + "verifyStatus: "
                                                + verifyStatus);

                            purchaseResponse(productId, purchaseState, verifyStatus);
                            
                            MarketBillingHandler handler = TeleNavDelegate.getInstance()
                                    .getBillingHandler();
                            if (handler != null)
                            {
                                handler.confirmNotification(notificationId);
                            }
                        }
                    }
                }
                handlePurchaseResponse(statusCode);
            }
        }
    }

    private void purchaseResponse(String productId, int purchaseState, int verifyStatus)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: purchaseResponse productId " + productId
            + " purchaseState " + purchaseState + " verifyStatus " + verifyStatus);
        AndroidBillingDao androidBillingDao = DaoManager.getInstance()
                .getAndroidBillingDao();
        if (androidBillingDao != null)
        {
            androidBillingDao.updateStatus(productId, purchaseState);
            androidBillingDao.store();
        }

        if (purchaseState == IMarketBillingConstants.PURCHASED)
        {
            boolean isValidPurchase = (verifyStatus == IAndroidBillingProxy.VERIFY_SUCCESS);
            if (isValidPurchase)
            {
                statusCode = IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_SUCC;
                
                // if application has login need to syncPurchaseStatus
                // if application has not login, the backgroundStartUp() will syncPurchaseStatus.
                if (DaoManager.getInstance().getBillingAccountDao().isLogin())
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: purchaseResponse productId " + productId
                        + " isLogin true");
                    syncPurchaseStatus();
                }
            }
        }
        else if (purchaseState == IMarketBillingConstants.CANCELED)
        {
            boolean isValidPurchase = (verifyStatus == IAndroidBillingProxy.VERIFY_SUCCESS);
            if (isValidPurchase)
            {
                statusCode = IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_COMMON_ERROR;
            }
        }
        else if (purchaseState == IMarketBillingConstants.REFUNDED)
        {
            syncPurchaseStatus();
        }
    }

    protected void syncPurchaseStatus()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
        ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance()
                .createSyncPurchaseProxy(null, CommManager.getInstance().getComm(), this,
                    userProfileProvider);
        syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IAndroidBillingProxy)
        {
            String action = proxy.getRequestAction();
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: " + action
                    + " transactionError");
            if (IServerProxyConstants.ACT_MESSAGE_PROCESS.equals(action))
            {
                handlePurchaseResponse(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_COMMON_ERROR);
            }
        }
        else
        {
            super.transactionError(proxy);
        }
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if (proxy instanceof IAndroidBillingProxy)
        {
            String action = proxy.getRequestAction();
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: " + action
                    + " networkError");
            if (IServerProxyConstants.ACT_MESSAGE_PROCESS.equals(action))
            {
                handlePurchaseResponse(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_COMMON_ERROR);
            }
        }
        else
        {
            super.networkError(proxy, statusCode, jobId);
        }
    }

    protected void doActionDelegate(int actionId)
    {

    }
}
