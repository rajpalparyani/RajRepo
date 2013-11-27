package com.telenav.navservice.location;

import com.telenav.location.TnLocation;

public class StationaryMonitor
{
    //the client will be in stationary mode if the speed of all the "fixCountThreshold" fixes are less than stationarySpeed
    //both fixCount and duration judgements must be true before client goes to stationary mode
    protected int fixCountThreshold;
    
    //the client will be in stationary mode if the speed of all fixes in the duration are less than stationarySpeed
    //both fixCount and duration judgements must be true before client goes to stationary mode
    protected long duration;
    
    //the speed threshold for stationary, meter / second
    protected float stationarySpeedInMS;
    
    protected int currentFixCount;
    protected long lastTime;
    
    public StationaryMonitor(int fixCountThreshold, long duration, float stationarySpeedInMS)
    {
        this.fixCountThreshold = fixCountThreshold;
        this.duration = duration;
        this.stationarySpeedInMS = stationarySpeedInMS;
    }
    
    public boolean isStationary(TnLocation location)
    {
        if (location == null || !location.isValid())
            return false;
        
        if (location.getSpeed() / 9.0 <= stationarySpeedInMS)
        {
            currentFixCount ++;
            if (lastTime == 0)
            {
                lastTime = System.currentTimeMillis();
                return false;
            }
            if (System.currentTimeMillis() - lastTime >= duration && currentFixCount >= fixCountThreshold)
            {
                return true;
            }
        }
        else
        {
            lastTime = 0;
            currentFixCount = 0;
        }
        
        return false;
    }
    
    public boolean isMoving(TnLocation location)
    {
        if (location == null || !location.isValid())
            return false;
        
        return location.getSpeed() / 9.0 > stationarySpeedInMS;
    }
}
