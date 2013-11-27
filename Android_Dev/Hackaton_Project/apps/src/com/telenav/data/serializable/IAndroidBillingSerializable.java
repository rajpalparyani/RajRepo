/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * iMarketBillingSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.data.datatypes.billing.MarketPurchaseRequest;

/**
 *@author gbwang
 *@date 2012-2-11
 */
public interface IAndroidBillingSerializable
{
    public MarketPurchaseRequest createPurchaseRequest(byte[] data);

    public byte[] toBytes(MarketPurchaseRequest purchaseRequest);
}
