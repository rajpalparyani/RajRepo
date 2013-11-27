/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkUserManagementProxyHelper.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.helper;

import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.UserManagementEvents.DayNightModeStatusResponse;
import com.telenav.navsdk.events.UserManagementEvents.DebugSettingsRequest;
import com.telenav.navsdk.events.UserManagementEvents.FeatureListUpdateRequest;
import com.telenav.navsdk.events.UserManagementEvents.ServiceLocatorUpdateRequest;
import com.telenav.navsdk.events.UserManagementEvents.UserProfileUpdateRequest;
import com.telenav.navsdk.services.UserManagementListener;
import com.telenav.navsdk.services.UserManagementServiceProxy;

/**
 *@author pwang
 *@date 2012-11-21
 */
public class NavSdkUserManagementProxyHelper implements UserManagementListener, INavSdkProxyConstants
{
    private static NavSdkUserManagementProxyHelper instance;

    private NavSdkUserManagementProxyHelper()
    {
        
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavSdkUserManagementProxyHelper();
            instance.setEventBus(bus);
        }
    }

    public static NavSdkUserManagementProxyHelper getInstance()
    {
        return instance;
    }

    private UserManagementServiceProxy serverProxy;

    private void setEventBus(EventBus bus)
    {
        serverProxy = new UserManagementServiceProxy(bus);
        serverProxy.addListener(this);
    }


    public void requestServiceLocatorUpdate(ServiceLocatorUpdateRequest event)
    {
        serverProxy.serviceLocatorUpdate(event);
    }

    public void requestFeatureListUpdate(FeatureListUpdateRequest event)
    {
        serverProxy.featureListUpdate(event);
    }

    public void requestUserProfileUpdateRequest(UserProfileUpdateRequest event)
    {
        serverProxy.userProfileUpdate(event);
    }
    
    public void requestDebugSettingUpdate(DebugSettingsRequest event)
    {
        serverProxy.debugSettings(event);
    }

    public void onDayNightModeStatusResponse(DayNightModeStatusResponse event)
    {
        
    }
}
