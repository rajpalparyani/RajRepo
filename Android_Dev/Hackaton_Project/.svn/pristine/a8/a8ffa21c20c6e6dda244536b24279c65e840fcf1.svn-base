/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ApplicationLifecycleProxyHelper.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.helper;

import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.ApplicationLifecycleData.ApplicationLifecycleTransition;
import com.telenav.navsdk.events.ApplicationLifecycleEvents.ApplicationLifecycletransitionRequest;
import com.telenav.navsdk.services.ApplicationLifecycleServiceProxy;

/**
 * @author hchai
 * @date 2012-1-10
 */
public class NavSdkApplicationLifecycleProxyHelper
{
    private static NavSdkApplicationLifecycleProxyHelper instance;

    private ApplicationLifecycleServiceProxy serverProxy;

    public NavSdkApplicationLifecycleProxyHelper()
    {
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavSdkApplicationLifecycleProxyHelper();
            instance.setEventBus(bus);
        }
    }

    public static NavSdkApplicationLifecycleProxyHelper getInstance()
    {
        return instance;
    }

    private void setEventBus(EventBus bus)
    {
        serverProxy = new ApplicationLifecycleServiceProxy(bus);
    }
    
    public void applicationLifeToForeground()
    {
        ApplicationLifecycletransitionRequest.Builder builder = ApplicationLifecycletransitionRequest
                .newBuilder();
        builder.setTransition(ApplicationLifecycleTransition
                .valueOf(ApplicationLifecycleTransition.ApplicationLifecycleTransition_ToForeground_VALUE));
        serverProxy.applicationLifecycletransition(builder.build());
    }
    
    public void applicationLifeToBackground()
    {
        ApplicationLifecycletransitionRequest.Builder builder = ApplicationLifecycletransitionRequest
                .newBuilder();
        builder.setTransition(ApplicationLifecycleTransition
                .valueOf(ApplicationLifecycleTransition.ApplicationLifecycleTransition_ToBackground_VALUE));
        serverProxy.applicationLifecycletransition(builder.build());
    }

    public void exitApplicationLifecycle()
    {
        ApplicationLifecycletransitionRequest.Builder builder = ApplicationLifecycletransitionRequest
                .newBuilder();
        builder.setTransition(ApplicationLifecycleTransition
                .valueOf(ApplicationLifecycleTransition.ApplicationLifecycleTransition_Suspend_VALUE));
        serverProxy.applicationLifecycletransition(builder.build());

        builder = ApplicationLifecycletransitionRequest.newBuilder();
        builder.setTransition(ApplicationLifecycleTransition
                .valueOf(ApplicationLifecycleTransition.ApplicationLifecycleTransition_Shutdown_VALUE));
        serverProxy.applicationLifecycletransition(builder.build());
    }
}
