/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AmrEndpointerJob.java
 *
 */
package com.telenav.dsr.amr;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.dsr.IRecordEventListener;
import com.telenav.dsr.amr.endpointor.SpeechClassifierEndpointer;
import com.telenav.dsr.amr.endpointor.SpeechEndpointorUtil;
import com.telenav.dsr.amr.endpointor.interf_dec;
import com.telenav.dsr.impl.AbstractDsrJob;
import com.telenav.dsr.impl.IDsrConstants;
import com.telenav.dsr.util.DsrUtil;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;
import com.telenav.util.StringTokenizer;

/**
 *@author bduan
 *@date 2010-10-28
 */
class AmrEndpointerJob extends AbstractDsrJob
{
    protected static int thresholdStart = 18; //empirical value
    
    protected static int voiceFrameThreshold = 8; //empirical value
    
    protected static boolean isSpeechEndThresholdParsed;

    protected static Vector speechEndThresholds = new Vector();
    
    protected String serverDrivenParamsForEndThreshold;
    
    protected int defaultEndpointThreshold = 30;
    
    protected int counter = 0;

    protected int speechSampleCounter;
    
    protected int nonVoiceFrameThreshold = 60;

    protected boolean isNowRecording = false;

    protected boolean isSpeech = false;
    
    protected boolean isFinish = false;

    protected interf_dec.dec_interface_State state = null;

    protected SpeechClassifierEndpointer speechClassifer = null;

    protected double volumeLevel = 0;

    protected double backgroundSignalLevel = 100;

    protected double volumeLevelAtBeginning = 0;
    
    protected short[] synchoronizedSpeech = new short[160];
    
    protected long recordStartTime;

    protected long speechStartTime;
    
    protected long timeout = 15000L;
    
    protected long maxSpeechTime = 5000;
    
    protected ThreadPool threadPool;
    
    /**
     * Constructor of AmrEndpointerJob.
     * 
     * @param threadPool  the threadPool
     * @param serverDrivenEndThresHold  serverDrivenEndThresHold
     */
    public AmrEndpointerJob(ThreadPool threadPool, Hashtable configMap)
    {
        this.threadPool = threadPool;
        
        parseConfigs(configMap);
        
        init();
    }

    public void addFrame(byte[] amrFrame)
    {
        if(amrFrame == null || amrFrame.length == 0)
            return;
        
        int block = amrFrame.length;
        
        byte[] amrFrameTemp = new byte[block];
        
        counter++;

        if (!isNowRecording || (!isSpeech && counter % 2 != 0) || counter % 6 == 0)
        {
            System.arraycopy(amrFrame, 0, amrFrameTemp, 0, block);
            interf_dec.Decoder_Interface_Decode(state, amrFrameTemp, 0, synchoronizedSpeech, 0, 0);
            volumeLevel = 0;

            for (int i = 0; i < 160; i++)
            {
                volumeLevel += (synchoronizedSpeech[i] * synchoronizedSpeech[i]);

                synchoronizedSpeech[i] = 0;
            }

            volumeLevel = volumeLevel / 160;
            volumeLevel = Math.sqrt(volumeLevel);
            volumeLevel = Math.max(volumeLevel, 1);
            volumeLevel = SpeechEndpointorUtil.log10x((int) volumeLevel) * 20;
            if (volumeLevel == 0)
            {
                block = 0;
                return;
            }
        }

        if (isNowRecording)
        {
            isSpeech = speechClassifer.isSpeech(volumeLevel);
        }
        else
        {
            // to detect speech start, we need more sensitive threshold
            isSpeech = speechClassifer.isSpeech(volumeLevel, thresholdStart);
        }

        block = 0;
        if (!isNowRecording)
        {
            volumeLevelAtBeginning += volumeLevel;

            if (isSpeech)
            {

                speechSampleCounter++;
                if (speechSampleCounter >= voiceFrameThreshold)
                {
                    isNowRecording = true;

                    // According to Niu, Xiaochuan
                    volumeLevelAtBeginning -= volumeLevel;

                    // calculate current noisy level
                    if (counter > 1) // The (cnt - 1) could be zero, we need check it.
                    {
                        volumeLevelAtBeginning = volumeLevelAtBeginning / (counter - 1);
                    }

                    adjustThreshold();

                    speechSampleCounter = 0;

                    speechStartTime = System.currentTimeMillis();
                    DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_START, null);
                }
            }
            else
            {
                if (speechSampleCounter != 0)
                {
                    // voiceBs.reset();
                    speechSampleCounter = 0;
                }
            }

            if (recordStartTime == 0)
            {
                recordStartTime = System.currentTimeMillis();
            }

            if (!isFinish && System.currentTimeMillis() - recordStartTime > this.timeout)
            {
                isFinish = true;
                DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_STOP, null);
            }

        }
        else
        {
            if (counter % 6 == 0)
            {
                checkCurrentVolumeLevel();
            }

            long speechTime = System.currentTimeMillis() - speechStartTime;

            if (!isFinish && speechTime > this.maxSpeechTime)
            {
                isFinish = true;
                DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_STOP, PrimitiveTypeCache.valueOf(IRecordEventListener.EVENT_STOP_TYPE_SPEECH_END));
            }

            if (isSpeech)
            {
                speechSampleCounter = 0;
            }
            else
            {
                speechSampleCounter++;
                if (!isFinish && speechSampleCounter >= nonVoiceFrameThreshold)
                {
                    isFinish = true;
                    DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_STOP, PrimitiveTypeCache.valueOf(IRecordEventListener.EVENT_STOP_TYPE_SPEECH_END));
                }
            }
        }}

    public void reset()
    {
        init();
    }
    
    protected void init()
    {
        counter = 0;
        speechClassifer = new SpeechClassifierEndpointer(defaultEndpointThreshold);
        speechClassifer.backgroundSingalLevel = backgroundSignalLevel;
        volumeLevel = 0;
        volumeLevelAtBeginning = 0;
        speechSampleCounter = 0;
        isNowRecording = false;
        isSpeech = false;
        state = interf_dec.Decoder_Interface_init();
        recordStartTime = 0;
    }
    
    private void checkCurrentVolumeLevel()
    {
        final int volume = (int) volumeLevel - 10;
        if (volume > 0)
        {
            DsrUtil.notifyVolumeChanged(threadPool, volume);
        }
    }
    
    /**
     * Adjust speech end threshold according to background noisy level
     */
    protected void adjustThreshold()
    {
        if (!isSpeechEndThresholdParsed && serverDrivenParamsForEndThreshold != null)
        {
            try
            {
                // serverDrivenParam = "0$30::50$30::110$0";
                StringTokenizer st = new StringTokenizer(serverDrivenParamsForEndThreshold, "::");

                while (st.hasMoreTokens())
                {
                    String token = st.nextToken();

                    if (token.length() > 0)
                    {
                        StringTokenizer st2 = new StringTokenizer(token, "$");
                        double key = Double.parseDouble(st2.nextToken());
                        double value = Double.parseDouble(st2.nextToken());
                        speechEndThresholds.addElement(new double[]
                        { key, value });
                    }
                }
            }
            catch (Exception e)
            {
                // #if DEBUG
                e.printStackTrace();
                // #endif
            }
            finally
            {
                isSpeechEndThresholdParsed = true;
            }
        }

        if (speechEndThresholds.size() > 0)
        {
            // we have server driven parameter
            boolean isThresholdGot = false;

            for (int i = 0; i < speechEndThresholds.size(); i++)
            {
                double[] temp = (double[]) speechEndThresholds.elementAt(i);
                if (temp[0] > volumeLevelAtBeginning)
                {
                    if (i > 0)
                    {
                        // yes, we got it.

                        double[] start = (double[]) speechEndThresholds.elementAt(i - 1);
                        double[] end = temp;

                        // calculate linear result in this small area
                        speechClassifer.threshold = (volumeLevelAtBeginning - start[0]) * (end[1] - start[1]) / (end[0] - start[0]) + start[1];
                        isThresholdGot = true;
                        break;
                    }
                }
            }

            if (!isThresholdGot)
            {
                speechClassifer.threshold = ((double[]) speechEndThresholds.elementAt(speechEndThresholds.size() - 1))[1];
            }

        }
        else
        {
            // this is the default algorithm without server driven parameter
            if (volumeLevelAtBeginning > 50)
            {
                speechClassifer.threshold -= (volumeLevelAtBeginning - 50) / 2;
            }
        }
    }
    
    protected void handleFrame(byte[] tmpBuffer, int off, int len) throws IOException
    {
        //do nothing
    }
    
    private void parseConfigs(Hashtable map)
    {
        if (map != null)
        {
            Object obj = map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_SPEECH_END_THRESHOLD));
            this.serverDrivenParamsForEndThreshold = (String)obj;
            
            obj = map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_NON_VOICE_FRAME_THRESHOLD));
            if (obj != null)
            {
                this.nonVoiceFrameThreshold = ((Integer)obj).intValue();
            }
            
            obj = map.get(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_MAX_SPEECH_TIME));
            if (obj != null)
            {
                this.maxSpeechTime = ((Long)obj).longValue();
            }
        }
    }

}
