/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PcmEncodingJob.java
 *
 */
package com.telenav.dsr.pcm;

import java.io.IOException;

import com.telenav.audio.codec.AbstractEncoder;
import com.telenav.dsr.DsrManager;
import com.telenav.dsr.impl.AbstractDsrJob;
import com.telenav.dsr.impl.IRecordDataHandler;
import com.telenav.logger.Logger;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 15, 2011
 */

class PcmEncodingJob extends AbstractDsrJob
{
    private IRecordDataHandler dataHandler;
    
    public PcmEncodingJob(IRecordDataHandler dataHandler)
    {
        this.dataHandler = dataHandler;
        
        AbstractEncoder.getInstance().destroyEncoder();
        AbstractEncoder.getInstance().reset();
        
        int mode = AbstractEncoder.SPEEX_MODEID_NB;
        switch (DsrManager.PCM_FREQUENCY)
        {
            case 16000:
            {
                mode = AbstractEncoder.SPEEX_MODEID_WB;
                break;
            }
        }
        
        try
        {
            AbstractEncoder.getInstance().initEncoder(mode);
        }
        catch(Exception e)
        {
            Logger.log(getClass().getName(), e);
        }
    }
    
    protected void handleFrame(byte[] tmpBuffer, int off, int len)
            throws IOException
    {
        AbstractEncoder.getInstance().writePCM(tmpBuffer, off, len);
        byte[] speexData = AbstractEncoder.getInstance().getEncodedData();
        dataHandler.writeData(speexData);
    }

}
