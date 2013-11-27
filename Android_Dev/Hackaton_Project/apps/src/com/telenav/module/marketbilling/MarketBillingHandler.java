/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ResponseHandle.java
 *
 */
package com.telenav.module.marketbilling;


import android.content.Context;
import android.content.Intent;

import com.telenav.app.android.AndroidPersistentContext;

/**
 *@author gbwang
 *@date 2012-2-17
 */
public class MarketBillingHandler
{
    private MarketBillingModel handler;
    
    private Class billingServiceClass;
    
    public void doVerifyPurchase(String signedData, String signature)
    {
        this.getHandler().doVerifyPurchase(signedData, signature);
    }

    public void declinePurchase(String billingOrderId)
    {
        this.getHandler().declinePurchase(billingOrderId);
    }
    
    public void handleBillingSuccess()
    {
        this.getHandler().handlePurchaseResponse(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_SUCC);
    }
    
    public void handleBillingNotSupported()
    {
        this.getHandler().handlePurchaseResponse(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_NOT_SUPPORT);
    }
    
    public void handlePurchaseUserCancel()
    {
        this.getHandler().handlePurchaseResponse(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_USER_CANCLE);
    }
    
    public void handlePurchaseCommonFail()
    {
        this.getHandler().handlePurchaseResponse(IMarketBillingConstants.MARKET_PURCHASE_STATUS_CODE_COMMON_ERROR);
    }
    
    public void handleResponseCode(int responseCode)
    {
        switch(responseCode)
        {
            case IMarketBillingConstants.RESULT_USER_CANCELED:
            {
                handlePurchaseUserCancel();
                break;
            }
            case IMarketBillingConstants.RESULT_SERVICE_UNAVAILABLE:
            case IMarketBillingConstants.RESULT_BILLING_UNAVAILABLE:
            case IMarketBillingConstants.RESULT_ITEM_UNAVAILABLE:
            case IMarketBillingConstants.RESULT_DEVELOPER_ERROR:
            case IMarketBillingConstants.RESULT_ERROR:
            {
                handlePurchaseCommonFail();
                break;
            }
        }
    }
    
    public void addMarketListener(IMarketBillingListener listener)
    {
        this.getHandler().addListener(listener);
    }
    
    public void removeMarketListener(IMarketBillingListener listener)
    {
        this.getHandler().removeListener(listener);
    }
    
    public void confirmNotification(String notifyId)
    {
        Context mContext = AndroidPersistentContext.getInstance().getContext();
        
        Intent intent = new Intent(IMarketBillingConstants.ACTION_CONFIRM_NOTIFICATION);
        intent.setClass(mContext, billingServiceClass);
        intent.putExtra(IMarketBillingConstants.EXTRA_NOTIFICATION_ID, notifyId);
        mContext.startService(intent);
    }
    
    public void requestPurchase(final String productId, final String orderId)
    {
        Context mContext = AndroidPersistentContext.getInstance().getContext();
        
        Intent intent = new Intent(IMarketBillingConstants.ACTION_REQUEST_PURCHASE);
        intent.setClass(mContext, billingServiceClass);
        intent.putExtra(IMarketBillingConstants.EXTRA_PRODUCT_ID, productId);
        intent.putExtra(IMarketBillingConstants.EXTRA_DEVELOPER_PAYLOAD, orderId);
        mContext.startService(intent);
    }
    
    public void getPurchaseInformation(String notifyId)
    {
        Context mContext = AndroidPersistentContext.getInstance().getContext();
        
        Intent intent = new Intent(IMarketBillingConstants.ACTION_GET_PURCHASE_INFORMATION);
        intent.setClass(mContext, billingServiceClass);
        intent.putExtra(IMarketBillingConstants.EXTRA_NOTIFICATION_ID, notifyId);
        mContext.startService(intent);
    }
    
    public void setBilingServiceClass(final Class billingServiceClass)
    {
        this.billingServiceClass = billingServiceClass;
    }
    
    private MarketBillingModel getHandler()
    {
        if(this.handler == null)
        {
            this.handler = new MarketBillingModel();
        }
        return this.handler;
    }
}
