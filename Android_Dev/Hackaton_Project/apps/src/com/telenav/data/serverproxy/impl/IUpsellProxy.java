/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IUpsellProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

/**
 *@author yning
 *@date 2012-11-28
 */
public interface IUpsellProxy
{
    public String requestUpsellInfo(boolean isNeedActiveOffer);
    
    public String submitUpsellSelection(String offerCode);
    
    public Vector getUpsellOptions();
    
    public String getOrderId();
    
    public String preparePurchase(String offerCode);
    
    public String cancelSubscription(String offerCode);
}
