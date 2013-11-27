/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AmrEncoding.java
 *
 */
package com.telenav.dsr.amr;

import java.io.IOException;

import com.telenav.dsr.impl.AbstractDsrJob;
import com.telenav.dsr.impl.IRecordDataHandler;

/**
 * This is just the AMR encoding wrapper. Since AMR is the encoded format, 
 * there is no need to encode it again. We just need send it out directly.
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 14, 2011
 */

class AmrEncodingJob extends AbstractDsrJob
{
    private IRecordDataHandler recordDataHandler;
    
    public AmrEncodingJob(IRecordDataHandler recordDataHandler)
    {
        this.recordDataHandler = recordDataHandler;
    }
    
    public void addFrame(byte[] buffer)
    {
        if (this.recordDataHandler != null)
        {
            this.recordDataHandler.writeData(buffer);
        }
    }

    protected void handleFrame(byte[] tmpBuffer, int off, int len) throws IOException
    {
        //do nothing
    }

}
