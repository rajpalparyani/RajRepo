/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TnLinkProviderManager.java
 *
 */
package com.telenav.carconnect.host;

import java.util.List;

import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.carconnect.provider.tnlink.HttpProvider;
import com.telenav.carconnect.provider.tnlink.LocationProvider;
import com.telenav.carconnect.provider.tnlink.NavigationProvider;
import com.telenav.carconnect.provider.tnlink.module.dsr.DsrProvider;
import com.telenav.carconnect.provider.tnlink.module.weather.WeatherProvider;

/**
 *@author chihlh
 *@date Jun 7, 2012
 */
class TnLinkProviderManager
{
    static void registerProviders(List<AbstractProvider> providers)
    {
        AbstractProvider ap = null;
        ap = new HttpProvider();
        ap.register();
        providers.add(ap);
        ap = new LocationProvider();
        ap.register();
        providers.add(ap);
        ap = new NavigationProvider();
        ap.register();
        providers.add(ap);
        ap = new DsrProvider();
        ap.register();
        providers.add(ap);
        ap = new WeatherProvider();
        ap.register();
        providers.add(ap);
    }
}
