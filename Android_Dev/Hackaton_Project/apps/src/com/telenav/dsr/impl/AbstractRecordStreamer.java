/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractRecordStreamer.java
 *
 */
package com.telenav.dsr.impl;

import java.io.OutputStream;
import java.util.Hashtable;

import com.telenav.audio.IRecorderListener;
import com.telenav.log.ILogConstants;
import com.telenav.logger.Logger;
import com.telenav.module.media.MediaManager;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 14, 2011
 */

public abstract class AbstractRecordStreamer extends OutputStream implements IDsrConstants
{
    protected ThreadPool threadPool;
    
    protected Hashtable configMap;
    
    protected AbstractDsrJob endpointerJob;
    protected AbstractDsrJob encodingJob;

    public AbstractRecordStreamer(Hashtable configMap, ThreadPool threadPool)
    {
        this.threadPool = threadPool;
        
        this.configMap = configMap;
    }
    
    public void start(long timeout, IRecorderListener recorderListener)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "", new Object[] {ILogConstants.DSR_START_RECORD_STREAMER});
        //Logger.log(Logger.INFO, ILogConstants.DSR_START_RECORD_STREAMER, "");
        
        MediaManager.getInstance().getRecordPlayer().record(timeout, this, recorderListener);
    }

    protected Object getThreshold(int key)
    {
        if (configMap != null)
        {
            return configMap.get(PrimitiveTypeCache.valueOf(key));
        }
        
        return null;
    }
    
    public void close()
    {
        if (this.encodingJob != null)
        {
            this.encodingJob.cancel();
        }
        if (this.endpointerJob != null)
        {
            this.endpointerJob.cancel();
        }
    }
    
    public AbstractDsrJob getEndpointerJob()
    {
        return this.endpointerJob;
    }
    
}
