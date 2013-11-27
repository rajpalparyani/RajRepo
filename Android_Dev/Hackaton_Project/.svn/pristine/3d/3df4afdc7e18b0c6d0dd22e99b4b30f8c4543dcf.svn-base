/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * RegionDetectResultTrasitor.java
 *
 */
package com.telenav.module.region;

import com.telenav.logger.Logger;
import com.telenav.module.region.RegionDetector.IRegionDetectorListener;

/**
 *@author yning
 *@date 2012-5-25
 */
public class RegionDetectResultHandler implements IRegionDetectorListener
{
    protected boolean isRegionDetectFinished = false;
    
    protected String region;
    
    protected IRegionDetectMvcListener listener;
    
    private static RegionDetectResultHandler instance = new RegionDetectResultHandler();
    
    boolean isChecked = false;
    
    private RegionDetectResultHandler()
    {
        
    }
    
    public static RegionDetectResultHandler getInstance()
    {
        return instance;
    }
    
    public void onRegionDetected(String region, int resultType)
    {
        Logger.log(Logger.INFO, getClass().toString(),
            "resultTransitor, onRegionDetected, region detected is " + region);

        setRegion(region);
    }
    
    public synchronized void setRegion(String region)
    {
        this.region = region;
        this.isRegionDetectFinished = true;
        if(listener != null)
        {
            listener.regionAlreadyDetected(region);
        }
    }

    public synchronized void checkRegionStatus(IRegionDetectMvcListener listener)
    {
        if(!isChecked)
        {
            if(isRegionDetectFinished)
            {
                listener.regionAlreadyDetected(region);
            }
            else
            {
                this.listener = listener;
                listener.regionNotDetected();
            }
            
            isChecked = true;
        }
        else
        {
            listener.regionAlreadyDetected("");
        }
    }
    
    public synchronized void reset()
    {
        isChecked = false;
        isRegionDetectFinished = false;
        region = null;
        listener = null;
    }
}
