/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NotificationManager.java
 *
 */
package com.telenav.app;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Mar 8, 2011
 */

public abstract class NotificationManager
{
    private static NotificationManager instance;
    private static int initCount;
    
    public static NotificationManager getInstance()
    {
        return instance;
    }
    
    public synchronized static void init(NotificationManager manager)
    {
        if(initCount >= 1)
            return;
        
        instance = manager;
        initCount++;
    }
    
    public abstract void showNotification(String title, String label);
    
    public abstract void showMarketBillingNotification(String lable, String notifyMsg, String action);
    
    public abstract void cancelNotification();
    
    public abstract void cancelMarketBillingNotification();
    
}
