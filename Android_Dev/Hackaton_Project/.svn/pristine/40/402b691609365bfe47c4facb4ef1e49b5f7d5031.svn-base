/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IAddressProxy
{
    public static final String NOTIFICATION_GET_STOP_BY_ID_TYPE = "FAVORITE";
    
    public String syncStops(long lastSyncTime, Vector recentStops, Vector favorites, Vector favCatalogs, Vector receivedAddresses);
    
    public String fetchAllStops(boolean isParseInThread);
    
    public String updateCities(int lat, int lon);
    
    public long getLastFavoriteSyncTime();
    
    public long getLastRecentStopSyncTime();

    public String fetchStopsById(String type, String  notificationAddressId);
    
    public Vector getNotificationVector();
}
