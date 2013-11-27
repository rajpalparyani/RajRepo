/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IS4AProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

/**
 *@author wchshao
 *@date Mar 12, 2013
 */
public interface IS4AProxy
{
    public String getTinyUrl();
    
    public void requestTinyUrl(String address, String lat, String lng, String name, boolean isDrving);
    
    public void updateETA(String session, String status, String userName, String eta, String lat, String lon, String speed, String accuracy, String routeId);
}
