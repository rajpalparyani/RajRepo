/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PcmEndpointerJob.java
 *
 */
package com.telenav.dsr.pcm;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.dsr.DsrManager;
import com.telenav.dsr.IRecordEventListener;
import com.telenav.dsr.impl.AbstractDsrJob;
import com.telenav.dsr.impl.IDsrConstants;
import com.telenav.dsr.pcm.endpointer.DSPUtil;
import com.telenav.dsr.pcm.endpointer.EPParameters;
import com.telenav.dsr.pcm.endpointer.JEndPointer;
import com.telenav.dsr.util.DsrUtil;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 15, 2011
 */

class PcmEndpointerJob extends AbstractDsrJob implements IRecordEventListener
{
    private static final int FRAME = DsrManager.PCM_FRAME_IN_BYTE;
    
    private ThreadPool threadPool;
    
    private PcmEncodingJob encodingJob;
    
    private JEndPointer ep;
    private JEndPointer.EPSTATE epState;
    
    private boolean isNowRecording;
    private boolean isSpeechMannualStop = false;
    private int totalFrame;
    private int[] buffers;
    private int startFrameIndex;
    private Vector bufferByte = new Vector();
    private long speechStartTime;
    
    private long maxSpeechTime = 5000;

    public PcmEndpointerJob(PcmEncodingJob encodingJob, ThreadPool threadPool, Hashtable configMap)
    {
        this.encodingJob = encodingJob;
        this.threadPool = threadPool;
        
        ep = initEndPointerParameters(configMap);
        ep.reset();
        
        Object obj = configMap.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_MAX_SPEECH_TIME));
        if (obj != null)
        {
            this.maxSpeechTime = ((Long)obj).longValue();
        }        
    }
    
    protected void handleFrame(byte[] tmpBuffer, int off, int len)
        throws IOException
    {
        if(len == FRAME)
        {
            int numSamples = len/2;
            if(buffers == null)
            {
                buffers = new int[numSamples];
            }
            
            for ( int i=0; i< numSamples; i++ ) 
            {
                // swap by the bytes for little Endian
                int sample = (short) ((tmpBuffer[2*i + 1]& 0xFF) << 8 | (tmpBuffer[2 *i] & 0xFF) << 0);
                buffers[i] = sample;
            }
            
            epState = ep.addFrame(buffers);
            
            int volumeLevel = DSPUtil.fastEnergy(buffers, 10);
            checkCurrentVolumeLevel(volumeLevel);
            
            if (epState == JEndPointer.EPSTATE.ERROR)
            {
                //TODO
            }
            else if (epState == JEndPointer.EPSTATE.INUTT && !isNowRecording)
            {
                startFrameIndex = ep.getStartFrame();
                
                speechStartTime = System.currentTimeMillis();         

                DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_START, null);
                
                if(startFrameIndex >= 0 && startFrameIndex < bufferByte.size())
                {
                    for(int j = startFrameIndex; j < bufferByte.size(); j ++)
                    {
                        encodingJob.addFrame((byte[])bufferByte.elementAt(j));
                        speechStartTime -= 20; // 20ms each frame
                    }
                }
                isNowRecording = true;
                bufferByte.removeAllElements();
                encodingJob.addFrame(tmpBuffer);
            }
            else if (epState == JEndPointer.EPSTATE.OUTUTT || epState == JEndPointer.EPSTATE.END || isSpeechMannualStop)
            {
                int endFrameIndex = ep.getEndFrame();
                if(endFrameIndex == totalFrame)
                {
                    encodingJob.addFrame(tmpBuffer);
                }
                
                if(encodingJob.getFrameSize() == 0)
                {
                    DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_STOP, PrimitiveTypeCache.valueOf(IRecordEventListener.EVENT_STOP_TYPE_SPEECH_END));
                    encodingJob.cancel();
                    cancel();
                }
                else
                {
                    DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_SPEECH_STOP, PrimitiveTypeCache.valueOf(IRecordEventListener.EVENT_STOP_TYPE_SPEECH_END));
                    if(!encodingJob.isCancelled())
                    {
                        encodingJob.cancel();
                    }
                    if(!isCancelled())
                    {
                        cancel();
                    }
                }
                
            }
            else if(isNowRecording)
            {
                long speechTime = System.currentTimeMillis() - speechStartTime;
                
                if (speechTime > this.maxSpeechTime)
                {
                    DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_STOP, PrimitiveTypeCache.valueOf(IRecordEventListener.EVENT_STOP_TYPE_SPEECH_END));
                    encodingJob.cancel();                    
                    cancel();                    
                }
                
                encodingJob.addFrame(tmpBuffer);
            }
            else
            {
                bufferByte.addElement(tmpBuffer);
            }
        }
        
        totalFrame ++;

    }
    
    private void reset()
    {
        this.ep.reset();
        this.totalFrame = 0;
        startFrameIndex = 0;
        buffers = null;
        isNowRecording = false;
        isSpeechMannualStop = false;
        speechStartTime = 0;
    }
    
    private JEndPointer initEndPointerParameters(Hashtable map)
    {
        Integer samplingRate =  (Integer)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_SAMPLINGRATE));
        if(samplingRate == null)
        {
            samplingRate = PrimitiveTypeCache.valueOf(EPParameters.DEFAULT_SAMPLING_RATE);
        }
        
        Integer frameLengthInMs =  (Integer)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_FRAME_LENGTH_IN_MS));
        if(frameLengthInMs == null)
        {
            frameLengthInMs = PrimitiveTypeCache.valueOf(EPParameters.DEFAULT_FRAME_LENGTH_IN_MS);
        }
        
        Integer frameShiftInMs =  (Integer)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_FRAME_SHIFT_IN_MS));
        if(frameShiftInMs == null)
        {
            frameShiftInMs = PrimitiveTypeCache.valueOf(EPParameters.DEFAULT_FRAME_SHIFT_IN_MS);
        }
        
        Float engFactorSOU = (Float)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_ENG_FACTOR_SOU));
        if(engFactorSOU == null)
        {
            engFactorSOU = new Float(EPParameters.DEFAULT_ENG_FACTOR_SOU);
        }
        else
        {
            engFactorSOU = new Float(engFactorSOU.floatValue()/ 1000);
        }
        
        Float engFactorEOU = (Float)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_ENG_FACTOR_EOU));
        if(engFactorEOU == null)
        {
            engFactorEOU = new Float(EPParameters.DEFAULT_ENG_FACTOR_EOU);
        }
        else
        {
            engFactorEOU = new Float(engFactorEOU.floatValue()/ 1000);
        }
            
        
        Integer engRank = (Integer)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_ENG_RANK));
        if(engRank == null)
        {
            engRank = PrimitiveTypeCache.valueOf(EPParameters.DEFAULT_ENG_RANK);
        }
        
        Integer maxPauseMs = (Integer)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_MAX_IW_PAUSE_MS));
        if(maxPauseMs == null)
        {
            maxPauseMs = PrimitiveTypeCache.valueOf(EPParameters.DEFAULT_MAX_IW_PAUSE_MS);
        }
        
        Integer startOffsetMs = (Integer)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_START_OFFSET_MS));
        if(startOffsetMs == null)
        {
            startOffsetMs = PrimitiveTypeCache.valueOf(EPParameters.DEFAULT_START_OFFSET_MS);
        }
        
        Integer endOffsetMs = (Integer)map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_END_OFFSET_MS));
        if(endOffsetMs == null)
        {
            endOffsetMs = PrimitiveTypeCache.valueOf(EPParameters.DEFAULT_END_OFFSET_MS);
        }
        
        return new JEndPointer(samplingRate.intValue(), frameLengthInMs.intValue(), 
                frameShiftInMs.intValue(), engFactorSOU.floatValue(), 
                engFactorEOU.floatValue(), maxPauseMs.intValue(), 
                engRank.intValue(),startOffsetMs.intValue(), endOffsetMs.intValue());
    }

    private void checkCurrentVolumeLevel(int voiceAmount)
    {
        int volumeLevel = 0;
        
        if(voiceAmount <= 100)
        {
            volumeLevel = 0;
        }
        else if(voiceAmount <= 1000)
        {
            volumeLevel = 19;
        }
        else if(voiceAmount <= 3000)
        {
            volumeLevel = 39;
        }
        else if(voiceAmount <= 5000)
        {
            volumeLevel = 59;
        }
        else
        {
            volumeLevel = voiceAmount;
        }
        
        DsrUtil.notifyVolumeChanged(threadPool, volumeLevel);
    }

    @Override
    public void recordStatusUpdate(int event, Object eventData)
    {
        if(event == IRecordEventListener.EVENT_TYPE_SPEECH_MANNUAL_STOP)
        {
            this.isSpeechMannualStop = true;
        }
    }

}