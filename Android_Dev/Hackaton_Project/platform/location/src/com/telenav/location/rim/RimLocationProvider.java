/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimLocationProvider.java
 *
 */
package com.telenav.location.rim;

import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationException;
import com.telenav.location.TnLocationProvider;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
class RimLocationProvider extends TnLocationProvider implements LocationListener
{
    protected LocationProvider locationProvider;
    protected ITnLocationListener listener;
    
    public RimLocationProvider(String name, LocationProvider locationProvider)
    {
        super(name);
        
        this.locationProvider = locationProvider;
    }

    protected TnLocation getLastKnownLocation() throws TnLocationException
    {
        Location location = LocationProvider.getLastKnownLocation();
        
        TnLocation tnLocation = RimLocationUtil.convert(location, this.name);
        
        tnLocation.setSatellites(getSatellites(location));
        return tnLocation;
    }

    protected TnLocation getLocation(int timeout) throws TnLocationException
    {
        try
        {
            return RimLocationUtil.convert(this.locationProvider.getLocation(timeout), this.name);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            
            throw new TnLocationException(e.getMessage());
        }
    }

    protected void requestLocationUpdates(long minTime, float minDistance, int timeout, int maxAge, ITnLocationListener listener)
    {
        this.listener = listener;
        
        this.locationProvider.setLocationListener(this, (int)minTime, timeout, maxAge);
    }

    protected void reset()
    {
        this.locationProvider.reset();
        this.locationProvider.setLocationListener(null, 1, 1, -1);
        
        this.locationProvider = null;
        this.listener = null;
    }

    public void locationUpdated(LocationProvider provider, Location location)
    {
        if(this.listener != null)
        {
            TnLocation tnLocation = RimLocationUtil.convert(location, this.name);
            
            tnLocation.setSatellites(getSatellites(location));
            
            this.listener.onLocationChanged(this, tnLocation);
        }
    }

    public void providerStateChanged(LocationProvider provider, int newState)
    {
        if(this.listener != null)
        {
            this.listener.onStatusChanged(this, RimLocationUtil.convertStatus(newState));
        }
    }

    private int getSatellites(Location location)
    {
        String extraInfo = location.getExtraInfo("application/X-jsr179-location-nmea");
        String gpgga = getGPGGA(extraInfo);

        return getNumSattelites(gpgga);
    }
    
    private String getGPGGA(String extraInfo)
    {
        String gga = null;

        if (extraInfo != null && extraInfo.length() > 0)
        {
            if (extraInfo.startsWith("$GPGGA"))
            {
                int endIndex = extraInfo.indexOf("$GPGLL");
                if (endIndex > 0)
                {
                    gga = extraInfo.substring(0, endIndex);
                }
            }
        }

        return gga;
    }
    
    private byte getNumSattelites(String str)
    {
        byte numSats = 0;
        if (str != null && str.length() > 0)
            try
            {
                int ind = str.indexOf(',');
                if (ind != -1)
                    str = str.substring(ind + 1);

                String[] buff = new String[15];
                int i = 0;
                while (str != null && i < 15)
                {
                    ind = str.indexOf(',');
                    if (ind == -1)
                        break;
                    buff[i] = str.substring(0, ind).trim();
                    str = str.substring(ind + 1);
                    i++;
                }

                numSats = Byte.parseByte(buff[6]);

            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }

        return numSats;
    }
}
