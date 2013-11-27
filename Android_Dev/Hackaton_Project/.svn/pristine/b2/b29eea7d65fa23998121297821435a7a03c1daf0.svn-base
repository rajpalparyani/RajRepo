package com.telenav.navservice.location;

import com.telenav.logger.Logger;


public class GpsRecorder
{
    protected GpsRecorderImpl gpsRecorderImpl;
    
    protected String provider;
    
    protected LocationBuffer gpsBuffer;
    
    protected boolean isBurstMode;
    
    protected int sampleSize, sampleInterval, idleTimeBeforeStop;
    
    protected long pauseUntilMillis;
    
    public GpsRecorder(String provider, LocationBuffer gpsBuffer)
    {
        this.provider = provider;
        this.gpsBuffer = gpsBuffer;
    }
    
    public void pauseFor(int pauseTime)
    {
        this.pauseUntilMillis = System.currentTimeMillis() + pauseTime * 1000;
        restartGpsRecorderImpl();
    }
    
    public void setParameters(
            int sampleSize,
            int sampleInterval, 
            int idleTimeBeforeStop)
    {
        this.sampleSize = sampleSize;
        this.sampleInterval = sampleInterval;
        this.idleTimeBeforeStop = idleTimeBeforeStop;
        
        restartGpsRecorderImpl();
    }
    
    protected void restartGpsRecorderImpl()
    {
        if (gpsRecorderImpl != null)
        {
            gpsRecorderImpl.stop();
            gpsRecorderImpl = null;
        }
        
        if (sampleSize != 0)
        {
            if (sampleSize == 1)
                isBurstMode = false;
            else
                isBurstMode = true;
            
            gpsRecorderImpl = new GpsRecorderImpl(provider, gpsBuffer);
            gpsRecorderImpl.setParameters(this.sampleSize, this.sampleInterval, this.idleTimeBeforeStop, this.pauseUntilMillis);
            gpsRecorderImpl.start();
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "GpsRecorder: isBurstMode = "+isBurstMode);
    }
}
