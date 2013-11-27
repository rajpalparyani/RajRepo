package com.telenav.module.location;

import com.telenav.location.TnLocation;

/**
 *@author bduan
 *@date Nov 23, 2010
 */
class LocationListenerHandler
{
    private LocationListener listener;
    
    private long timeoutTimestamp;
    
    private int gpsLocationValidTime;
    
    private int networkLocationValidTime;
    
    private int type; 
    
    private int remainCount;
    
    private TnLocation[] locations;
    
    LocationListenerHandler(LocationListener listener, long timeoutTimestamp, int type, int leftCount, TnLocation[] locations)
    {
        this.listener = listener;
        this.timeoutTimestamp = timeoutTimestamp;
        this.type = type;
        this.remainCount = leftCount;
        this.locations = locations;
    }
    
    public LocationListener getListener()
    {
        return this.listener;
    }
    
    public int getType()
    {
        return this.type;
    }
    
    public int getRemainCount()
    {
        return this.remainCount;
    }
    
    public long getTimeoutTimestamp()
    {
        return this.timeoutTimestamp;
    }
    
    public void setGpsLocationValidTime(int time)
    {
        this.gpsLocationValidTime = time;
    }
    
    public int getGpsLocationValidTime()
    {
        return this.gpsLocationValidTime;
    }
    
    public void setNetworkLocationValidTime(int time)
    {
        this.networkLocationValidTime = time;
    }
    
    public int getNetworkLocationValidTime()
    {
        return this.networkLocationValidTime;
    }
    
    public void addLocation(TnLocation currentLocation)
    {
        int length = locations.length;
        boolean isExist = false;
        for(int i = 0 ; i < length - remainCount; i ++)
        {
            if(isLocationEqual(locations[i], currentLocation))
            {
                isExist = true;
                break;
            }
        }
        
        if(!isExist)
        {
            locations[length - remainCount] = currentLocation;
            remainCount --;
        }
    }
    
    public TnLocation[] getLocations()
    {
        return locations;
    }
    
    protected boolean isLocationEqual(TnLocation location0, TnLocation location1)
    {
        if(location0 == null || location1 == null)
            return false;
        
        if (location0.getTime() == location1.getTime() 
                && location0.getLatitude() == location1.getLatitude()
                && location0.getLongitude() == location1.getLongitude())
            return true;
        
        return false;
    }
}

