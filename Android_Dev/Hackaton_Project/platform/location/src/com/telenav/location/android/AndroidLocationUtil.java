/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidLocationUtil.java
 *
 */
package com.telenav.location.android;

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
class AndroidLocationUtil
{
    static String convertNativeProvider(String provider)
    {
        if(provider == null)
            return null;
        
        if(TnLocationManager.GPS_179_PROVIDER.equals(provider))
        {
            return LocationManager.GPS_PROVIDER;
        }
        else if(TnLocationManager.NETWORK_PROVIDER.equals(provider))
        {
            return LocationManager.NETWORK_PROVIDER;
        }
        else if(TnLocationManager.PASSIVE_PROVIDER.equals(provider))
		{
			return "passive";
		}

        return null;
    }
    
    static Criteria convert(TnCriteria tnCriteria)
    {
        if(tnCriteria == null)
            return null;
        
        Criteria criteria = new Criteria();

        criteria.setAccuracy(convertAccuracy(tnCriteria.getAccuracy()));
        criteria.setAltitudeRequired(tnCriteria.isAltitudeRequired());
        criteria.setBearingRequired(tnCriteria.isBearingRequired());
        criteria.setCostAllowed(tnCriteria.isCostAllowed());
        criteria.setPowerRequirement(convertPower(tnCriteria.getPowerRequirement()));
        criteria.setSpeedRequired(tnCriteria.isSpeedRequired());

        return criteria;
    }
    
    private static int convertAccuracy(int tnAccuracy)
    {
        switch(tnAccuracy)
        {
            case TnCriteria.NO_REQUIREMENT:
                return Criteria.NO_REQUIREMENT;
            case TnCriteria.ACCURACY_COARSE:
                return Criteria.ACCURACY_COARSE;
            case TnCriteria.ACCURACY_FINE:
                return Criteria.ACCURACY_FINE;
        }
        
        return Criteria.NO_REQUIREMENT;
    }
    
    private static int convertPower(int tnPower)
    {
        switch(tnPower)
        {
            case TnCriteria.NO_REQUIREMENT:
                return Criteria.NO_REQUIREMENT;
            case TnCriteria.POWER_HIGH:
                return Criteria.POWER_HIGH;
            case TnCriteria.POWER_LOW:
                return Criteria.POWER_LOW;
            case TnCriteria.POWER_MEDIUM:
                return Criteria.POWER_MEDIUM;
        }
        
        return Criteria.NO_REQUIREMENT;
    }
    
    static TnLocation convert(Location location)
    {
        if(location == null)
            return null;
        
        TnLocation tnLocation = new TnLocation(convertProvider(location.getProvider()));
        tnLocation.setAccuracy((int)location.getAccuracy() * 7358 >> 13);
        tnLocation.setAltitude((int)location.getAltitude());
        tnLocation.setHeading((int)location.getBearing());
        tnLocation.setLatitude((int)(location.getLatitude() * 100000));
        tnLocation.setLongitude((int)(location.getLongitude() * 100000));
        tnLocation.setSpeed((int)location.getSpeed()* 9);
        tnLocation.setTime(location.getTime()/10);
        tnLocation.setValid(true);
        
        return tnLocation;
    }
    
    static int convertStatus(int nativeStatus)
    {
        switch(nativeStatus)
        {
            case LocationProvider.AVAILABLE:
                return TnLocationProvider.AVAILABLE;
            case LocationProvider.OUT_OF_SERVICE:
                return TnLocationProvider.OUT_OF_SERVICE;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                return TnLocationProvider.TEMPORARILY_UNAVAILABLE;
        }
        
        return TnLocationProvider.AVAILABLE;
    }
    
    static String convertProvider(String nativeProvider)
    {
        if(nativeProvider == null)
            return null;
        
        if(LocationManager.GPS_PROVIDER.equals(nativeProvider))
        {
            return TnLocationManager.GPS_179_PROVIDER;
        }
        else if(LocationManager.NETWORK_PROVIDER.equals(nativeProvider))
        {
            return TnLocationManager.NETWORK_PROVIDER;
        }
        else if("passive".equals(nativeProvider))
		{
			return TnLocationManager.PASSIVE_PROVIDER;
		}

        return null;
    }
}
