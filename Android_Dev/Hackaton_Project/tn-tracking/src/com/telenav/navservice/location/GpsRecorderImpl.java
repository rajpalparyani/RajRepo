package com.telenav.navservice.location;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;
import com.telenav.logger.Logger;

public class GpsRecorderImpl implements ITnLocationListener, Runnable
{
    protected String provider;
    protected LocationBuffer gpsBuffer;
    
    protected int sampleSize;
    protected long sampleIntervalInMillis;
    protected long idleTimeBeforeStopInMillis;
    protected long pauseUntilMillis;
    
    protected long startTime;
    protected int fixCounter;
    
    protected boolean isRunning, isStarted, isGettingGps;
    protected boolean isStopped;
    
    protected Object mutex = new Object();
    
    public GpsRecorderImpl(String provider, LocationBuffer buffer)
    {
        this.provider = provider;
        this.gpsBuffer = buffer;
        
        Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: timer started");
    }
    
    public void setParameters(
            int sampleSize,
            int sampleInterval, 
            int idleTimeBeforeStop,
            long pauseUntilMillis)
    {
        this.sampleSize = sampleSize;
        this.sampleIntervalInMillis = sampleInterval * 1000L;
        this.idleTimeBeforeStopInMillis = idleTimeBeforeStop * 1000L;
        this.pauseUntilMillis = pauseUntilMillis;
        Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: setParameters() sampleSize = "+this.sampleSize+", sampleInterval = "+sampleInterval+", idleTimeBeforeStop = "+idleTimeBeforeStop);
    }
    
    public void start()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "start() enter");
        if (isStarted)
        {
            throw new IllegalStateException("GpsRecorderImpl can not be restarted, create a new instance to use");
        }
        
        isRunning = true;
        new Thread(this).start();
        this.fixCounter = 0;
        this.startTime = System.currentTimeMillis();
        
        if (this.pauseUntilMillis < System.currentTimeMillis())
        {
            startGps();
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "1. need to pause GPS for "+((pauseUntilMillis - System.currentTimeMillis()) / 1000));
        }
        
        isStarted = true;
    }
    
    public void stop()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "stop() enter");
        isRunning = false;
        synchronized(mutex)
        {
            mutex.notify();
        }
        stopGps();
        this.fixCounter = 0;
        this.startTime = 0;
    }
    
    public void onLocationChanged(TnLocationProvider provider,
            TnLocation location)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: gps received");

        if (location == null || !location.isValid())
            return;
        
        //looks like there is no synchronization problem here
        if (fixCounter < this.sampleSize)
        {
            fixCounter ++;
            gpsBuffer.addLocation(location);
            Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: added fix: ("+location+") to gpsBuffer, fixCounter = "+fixCounter);
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: no need to add the fix");
        }
    }
    
    public void onStatusChanged(TnLocationProvider provider, int status)
    {
        
    }
    
    protected long getNearestStartTime(long currentTime)
    {
        return (System.currentTimeMillis() - startTime) / this.sampleIntervalInMillis * this.sampleIntervalInMillis + startTime;
    }

    public void run()
    {
        long waitTime = 1000;
        while(isRunning)
        {
            try{
                Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: wait time = "+waitTime);
                synchronized(mutex)
                {
                    mutex.wait(waitTime);
                }
                waitTime = 1000;
                
                if (!isRunning)
                    break;
                
                if (startTime <= 0)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "startTime is zero, something wrong!!!");
                    return;
                }
                
                if (pauseUntilMillis < System.currentTimeMillis())
                {
                    //find the nearest start time
                    long nearestStartTime = getNearestStartTime(System.currentTimeMillis());
                    
                    if (Logger.DEBUG)
                        Logger.log(Logger.INFO, this.getClass().getName(), "nearestStartTime = "+nearestStartTime+", "+(System.currentTimeMillis() - startTime));
                    
                    if (System.currentTimeMillis() - startTime >= this.sampleIntervalInMillis)
                    { 
                        if (nearestStartTime == startTime)
                        {
                            //It should not happen
                            startTime = nearestStartTime + this.sampleIntervalInMillis;
                        }
                        else
                        {
                            startTime = nearestStartTime;
                        }
                        
                        //looks like there is no synchronization problem here
                        fixCounter = 0;
                        
                        if (!isRunning)
                            break;
                        
                        startGps();
                    }
                    else
                    {
                        if (fixCounter >= this.sampleSize)
                        {
                            long nextStartTime = nearestStartTime + this.sampleIntervalInMillis;
                            if (nextStartTime - System.currentTimeMillis() >= this.idleTimeBeforeStopInMillis && isGettingGps)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: stop gps for "+((nextStartTime - System.currentTimeMillis())/1000)+" seconds");
                                stopGps();
                            }
                            waitTime = Math.max(1000, nextStartTime - System.currentTimeMillis());
                        }
                    }
                }
                else
                {
                    waitTime = Math.max(1000, pauseUntilMillis - System.currentTimeMillis());
                    Logger.log(Logger.INFO, this.getClass().getName(), "2. need to pause GPS for "+((pauseUntilMillis - System.currentTimeMillis()) / 1000));
                }
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
        
        isStopped = true;
    }
    
    protected synchronized void startGps()
    {
        if (isGettingGps)
            return;
        
        Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: startGps");
        if (TnLocationManager.getInstance() != null)
            TnLocationManager.getInstance().requestLocationUpdates(
                    provider, 0, 0, 60000, 0, this);
        isGettingGps = true;
    }
    
    protected synchronized void stopGps()
    {
        if (!isGettingGps)
            return;
        
        Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorderImpl: stopGps");
        if (TnLocationManager.getInstance() != null)
            TnLocationManager.getInstance().reset(provider);
        isGettingGps = false;
    }
}
