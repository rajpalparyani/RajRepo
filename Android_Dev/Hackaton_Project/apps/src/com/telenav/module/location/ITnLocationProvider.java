/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ITnLocationProvider.java
 *
 */
package com.telenav.module.location;

import com.telenav.gps.IGpsListener;
import com.telenav.location.TnLocation;

/**
 *@author bduan
 *@date 2012-1-19
 */
public interface ITnLocationProvider
{
    public String getGpsServiceType();
    public int getGpsServiceStatus();
    public TnLocation getLastKnownLocation(String provider);
    public int getGpsFixes(int numFixes, TnLocation [] locations);
    public int getNetworkFixes(int numFixes, TnLocation [] locations);
    public void start();
    public void stop();
    public void setListener(IGpsListener gpsListener);
    public void clearListener();
    public String convertToNativeProvider(int type);
    public void setLocationParams(long interval, long fasterInterval, int priority, boolean immediately);
}
