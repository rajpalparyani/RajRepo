/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IAndroidBillingProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

/**
 *@author gbwang
 *@date 2012-2-16
 */
public interface IAndroidBillingProxy
{
    public static final byte VERIFY_SUCCESS = 0;
    
    public String requestVerifyPurchase(String signedData, String signature);
    
    public String declinePurchase(String billingOrderId);
    
    public Vector getVerifiedPurchases();
    
    public int getMinorStatus();
}
  
