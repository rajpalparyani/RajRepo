/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimLocationUtil.java
 *
 */
package com.telenav.location.rim;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationProvider;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
class RimLocationUtil
{
    static Criteria convert(TnCriteria tnCriteria)
    {
        if(tnCriteria == null)
            return null;
        
        Criteria criteria = new Criteria();

        criteria.setHorizontalAccuracy(convertAccuracy(tnCriteria.getHorizontalAccuracy()));
        criteria.setVerticalAccuracy(convertAccuracy(tnCriteria.getVerticalAccuracy()));
        criteria.setAltitudeRequired(tnCriteria.isAltitudeRequired());
        criteria.setCostAllowed(tnCriteria.isCostAllowed());
        criteria.setSpeedAndCourseRequired(tnCriteria.isSpeedRequired());
        criteria.setPreferredPowerConsumption(convertPower(tnCriteria.getPowerRequirement()));
        
        return criteria;
    }
    
    private static int convertAccuracy(int tnAccuracy)
    {
        switch(tnAccuracy)
        {
            case TnCriteria.NO_REQUIREMENT:
                return Criteria.NO_REQUIREMENT;
        }
        
        return tnAccuracy;
    }
    
    private static int convertPower(int tnPower)
    {
        switch(tnPower)
        {
            case TnCriteria.NO_REQUIREMENT:
                return Criteria.NO_REQUIREMENT;
            case TnCriteria.POWER_HIGH:
                return Criteria.POWER_USAGE_HIGH;
            case TnCriteria.POWER_LOW:
                return Criteria.POWER_USAGE_LOW;
            case TnCriteria.POWER_MEDIUM:
                return Criteria.POWER_USAGE_MEDIUM;
        }
        
        return Criteria.NO_REQUIREMENT;
    }
    
    static TnLocation convert(Location location, String provider)
    {
        if(location == null)
            return null;
        
        TnLocation tnLocation = new TnLocation(provider);
        tnLocation.setAccuracy((int)location.getQualifiedCoordinates().getHorizontalAccuracy() * 7358 >> 13);
        tnLocation.setAltitude((int)location.getQualifiedCoordinates().getAltitude());
        tnLocation.setHeading((int)location.getCourse());
        tnLocation.setLatitude((int)(location.getQualifiedCoordinates().getLatitude() * 100000));
        tnLocation.setLongitude((int)(location.getQualifiedCoordinates().getLongitude() * 100000));
        tnLocation.setSpeed((int)location.getSpeed()* 9);
        tnLocation.setTime(location.getTimestamp() / 10);
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
}
