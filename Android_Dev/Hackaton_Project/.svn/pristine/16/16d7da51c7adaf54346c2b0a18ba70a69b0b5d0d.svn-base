/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AmrRecordStreamer.java
 *
 */
package com.telenav.dsr.amr;

import java.io.IOException;
import java.util.Hashtable;

import com.telenav.dsr.impl.AbstractDsrJob;
import com.telenav.dsr.impl.AbstractRecordStreamer;
import com.telenav.dsr.impl.IRecordDataHandler;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-10-28
 */
public class AmrRecordStreamer extends AbstractRecordStreamer
{
    protected static int[] blockSize =
    { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };//empirical value.
    
    protected int headerReadThreshold = 6;
    
    protected int block = 0;

    protected int amrFrameOffset;

    protected byte[] amrFrame;

    protected int headerNumber = 0;
    
    private IRecordDataHandler recordDataHandler;
    
    /**
     * Constructor of AmrRecordStreamer. It's used by RecordJob to output data.
     * @param headReadThreshold
     */
    public AmrRecordStreamer(Hashtable configMap, ThreadPool threadPool, IRecordDataHandler recordDataHandler)
    {
        super(configMap, threadPool);
        
        this.recordDataHandler = recordDataHandler;
        
        if (configMap != null)
        {
            Object obj = configMap.get(PrimitiveTypeCache.valueOf(CONFIG_HEADER_SIZE));
            if (obj != null)
            {
                headerReadThreshold = ((Integer)obj).intValue();
            }
        }
                
        endpointerJob = createEndpointerJob();
        encodingJob = createEncodingJob();

    }
    
    protected AbstractDsrJob createEndpointerJob()
    {
        return new AmrEndpointerJob(threadPool, this.configMap);
    }
    
    protected AbstractDsrJob createEncodingJob()
    {
        return new AmrEncodingJob(recordDataHandler);
    }

    public void write(int b) throws IOException
    {
        if (headerNumber < headerReadThreshold)
        {
            headerNumber++;
            this.encodingJob.addFrame(b);
            return;
        }
        
        if (block == 0)
        {
            block = blockSize[(b & 0xFF) >> 3] + 1;
            amrFrameOffset = 0;
            amrFrame = new byte[block];
        }

        amrFrame[amrFrameOffset++] = (byte) b;

        if (amrFrameOffset < block)
        {
            return;
        }
        
        this.endpointerJob.addFrame(amrFrame);
        this.encodingJob.addFrame(amrFrame);
        block = 0;
    }
    
    /**
     * reset the RecordOutputStream.
     */
    public void reset()
    {
        block = 0;
        amrFrameOffset = 0;
        amrFrame = null;
    }

}
