/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PcmRecordStreamer.java
 *
 */
package com.telenav.dsr.pcm;

import java.io.IOException;
import java.util.Hashtable;

import com.telenav.dsr.DsrManager;
import com.telenav.dsr.impl.AbstractDsrJob;
import com.telenav.dsr.impl.AbstractRecordStreamer;
import com.telenav.dsr.impl.IRecordDataHandler;
import com.telenav.threadpool.ThreadPool;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 15, 2011
 */

public class PcmRecordStreamer extends AbstractRecordStreamer
{
    private static final int FRAME = DsrManager.PCM_FRAME_IN_BYTE;
    
    private IRecordDataHandler recordDataHandler;
    
    private byte[] frameBuffer = new byte[FRAME]; 
    private int currentIndex;

    public PcmRecordStreamer(Hashtable configMap, ThreadPool threadPool, IRecordDataHandler recordDataHandler)
    {
        super(configMap, threadPool);
        
        this.recordDataHandler = recordDataHandler;
        
        this.encodingJob = createEncodingJob();
        this.endpointerJob = createEndpointerJob();        
    }

    protected AbstractDsrJob createEncodingJob()
    {
        return new PcmEncodingJob(recordDataHandler);
    }

    protected AbstractDsrJob createEndpointerJob()
    {
        return new PcmEndpointerJob((PcmEncodingJob)encodingJob, threadPool, configMap);
    }

    public void write(int b) throws IOException
    {
        if (currentIndex >= FRAME)
        {
            frameBuffer = new byte[FRAME];
            currentIndex = 0;
        }
        
        frameBuffer[currentIndex] = (byte)b;
        currentIndex ++;
        
        if (currentIndex == FRAME)
        {
            //Just put the frame to the end pointer job which will handle
            //the encoding job in it.
            this.endpointerJob.addFrame(frameBuffer);
        }
    }
}
