/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IDimProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IDimProxy
{
    public String requestGetToken();
    
    public String requestGetPtn(String token, boolean isEncrypted, byte retryTimes, int timeout);
   
    //public void requestGetCarrier(String ptn);
    
    public String getToken();
    
    public String getDestination();
    
    public String getPort();
    
    public String getPTN();
    
    //public String getCarrier();
    
    public String getDimStatus();
    
    public boolean isAllowFallback();
    
    public boolean isPtnEncrypted();
    
    public int[] getRetryIntervals();
    
    public int getTimeout();
}
