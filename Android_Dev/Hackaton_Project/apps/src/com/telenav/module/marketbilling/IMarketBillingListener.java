/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IMarketListener.java
 *
 */
package com.telenav.module.marketbilling;

/**
 *@author gbwang
 *@date 2012-5-10
 */
public interface IMarketBillingListener
{
    public void purchaseFinished(String statusCode);
}
