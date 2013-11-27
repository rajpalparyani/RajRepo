/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * BillingReceiver.java
 *
 */
package com.telenav.app.android.scout_us;

import com.telenav.app.TeleNavDelegate;
import com.telenav.logger.Logger;
import com.telenav.module.marketbilling.IMarketBillingConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *@author gbwang
 *@date 2012-2-6
 */
public class BillingReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (IMarketBillingConstants.ACTION_IN_APP_NOTIFY.equals(action))
        {
            String notifyId = intent
                    .getStringExtra(IMarketBillingConstants.EXTRA_NOTIFICATION_ID);
            notify(context, notifyId);
            Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: onReceive"
                    + action);
        }
        else if (IMarketBillingConstants.ACTION_PURCHASE_STATE_CHANGED.equals(action))
        {
            if (TeleNavDelegate.getInstance().isStarted())
            {
                String signedData = intent
                .getStringExtra(IMarketBillingConstants.EXTRA_SIGNED_DATA);
                String signature = intent.getStringExtra(IMarketBillingConstants.EXTRA_SIGNATURE);
                purchaseStateChanged(context, signedData, signature);
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: onReceive"
                    + action);
            }
        }
        else if (IMarketBillingConstants.ACTION_RESPONSE_CODE.equals(action))
        {
            if (TeleNavDelegate.getInstance().isStarted())
            {
                long requestId = intent.getLongExtra(IMarketBillingConstants.EXTRA_REQUEST_ID, -1);
                int responseCode = intent.getIntExtra(IMarketBillingConstants.EXTRA_RESPONSE_CODE,
                    IMarketBillingConstants.RESULT_ERROR);
                checkResponseCode(context, requestId, responseCode);
                Logger.log(Logger.INFO, this.getClass().getName(), "MarketBilling: onReceive"
                    + action + " requestId: " + requestId + " responseCode: "
                    + responseCode);
            }
        }
    }

    private void notify(Context context, String notifyId)
    {
        Intent intent = new Intent(IMarketBillingConstants.ACTION_GET_PURCHASE_INFORMATION);
        intent.setClass(context, BillingService.class);
        intent.putExtra(IMarketBillingConstants.EXTRA_NOTIFICATION_ID, notifyId);
        context.startService(intent);
    }

    private void purchaseStateChanged(Context context, String signedData, String signature)
    {
        Intent intent = new Intent(IMarketBillingConstants.ACTION_PURCHASE_STATE_CHANGED);
        intent.setClass(context, BillingService.class);
        intent.putExtra(IMarketBillingConstants.EXTRA_SIGNED_DATA, signedData);
        intent.putExtra(IMarketBillingConstants.EXTRA_SIGNATURE, signature);
        context.startService(intent);
    }

    private void checkResponseCode(Context context, long requestId, int responseCodeIndex)
    {
        Intent intent = new Intent(IMarketBillingConstants.ACTION_RESPONSE_CODE);
        intent.setClass(context, BillingService.class);
        intent.putExtra(IMarketBillingConstants.EXTRA_REQUEST_ID, requestId);
        intent.putExtra(IMarketBillingConstants.EXTRA_RESPONSE_CODE, responseCodeIndex);
        context.startService(intent);
    }
}
