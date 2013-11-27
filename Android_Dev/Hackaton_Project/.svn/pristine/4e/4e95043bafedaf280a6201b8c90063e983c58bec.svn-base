/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidPcmMediaRecorder.java
 *
 */
package com.telenav.media.android;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Build;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaRecorder;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 15, 2011
 */

class AndroidPcmMediaRecorder extends TnMediaRecorder implements OnErrorListener, OnInfoListener
{
    private final static int MAX_RECORD_TIME =  30 * 1000;
    
    private boolean isStarted;
    private boolean isCancelled;
    private Context context;
    
    private Object mutex = new Object();
    
    private int frequency;
    private final static int RETRY_TIMES = 20;

    public AndroidPcmMediaRecorder(Context context, String format, int frequency)
    {
        super(format);
        
        this.context = context;
        
        if (frequency != 8000 && frequency != 16000)
        {
            throw new IllegalArgumentException("Only support 8000 and 16000 frequency record !!!");
        }
        
        this.frequency = frequency;
    }

    public void commit() throws TnMediaException
    {
        //do nothing
    }

    protected void setRecordSizeLimitDelegate() throws TnMediaException
    {
        //do nothing
    }

    public void close() throws TnMediaException
    {
        this.isCancelled = true;
        synchronized (mutex)
        {
            mutex.notify();
        }
    }

    public void prefetch() throws TnMediaException
    {
        //do nothing
    }

    public void realize() throws TnMediaException
    {
        //do nothing
        if (Integer.parseInt(Build.VERSION.SDK) >= 8)
        {
            AndroidMediaUtil.requestAudioFocus(this.context);
        }
    }

    public void reset() throws TnMediaException
    {
        //do nothing
    }

    public void start() throws TnMediaException
    {
        if (isStarted) return;
        
        isStarted = true;
        isCancelled = false;
        
        new Thread(new RecorderTask()).start();
    }

    public void stop() throws TnMediaException
    {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8)
        {
            AndroidMediaUtil.abandonAudioFocus(this.context);
        }
        
        if (!isStarted) return;
        
        close();
        
        isStarted = false;
    }

    public void onError(MediaRecorder mr, int what, int extra)
    {
        if(this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_ERROR, what + "," + extra);
        }
    }

    public void onInfo(MediaRecorder mr, int what, int extra)
    {
        if (this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_INFO, what + "," + extra);
        }
    }
    
    class RecorderTask implements Runnable
    {
        public void run()
        {
            long startTime = System.currentTimeMillis();
            
            if(mediaListener != null)
            {
                mediaListener.mediaUpdate(AndroidPcmMediaRecorder.this, ITnMediaListener.ON_PREPARE, null);
                try
                {
                    Thread.sleep(200);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
                
            int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
            int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
            
            int frameBuffer = frequency * 20 / 1000; //20 ms frame in short
            
           
            
            int miniBufferByteSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            
            int bufferByteSize = (miniBufferByteSize / frameBuffer) * frameBuffer;
            if(miniBufferByteSize % frameBuffer != 0)
            {
                bufferByteSize += frameBuffer;
            }
            
            if(bufferByteSize < frameBuffer * 10) //minimum is 10 frames
            {
                bufferByteSize = frameBuffer * 10;
            }
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, frequency, channelConfiguration, audioEncoding,
                    bufferByteSize);
            int count = 0;
            while (audioRecord.getState() != AudioRecord.STATE_INITIALIZED && count < AndroidPcmMediaRecorder.RETRY_TIMES)
            {
            	try 
            	{
					Thread.sleep(10);
				} 
            	catch (InterruptedException e) 
            	{
					Logger.log(this.getClass().getName(), e);
				}
            	count ++;
            	Logger.log(Logger.INFO, this.getClass().getName(), "AndroidPcmMediaRecorder#recreate audiRecord after sleep 10 milliseconds! retry times is " + count);
            	audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, frequency, channelConfiguration, audioEncoding,
                        bufferByteSize);
            }
    
            //check the state of AudioRecord again
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED)
            {
                //create the stream after the record state is ready.
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os, frameBuffer + 2));
                
                short[] buffer = new short[bufferByteSize];
                audioRecord.startRecording();
                
                while (!isCancelled && (System.currentTimeMillis() - startTime) < MAX_RECORD_TIME)
                {
                    try
                    {
                        int bufferReadResult = audioRecord.read(buffer, 0, bufferByteSize);
                        for (int i = 0; i < bufferReadResult; i++)
                        {
                            if(isCancelled)
                            {
                                break;
                            }
                            
                            dos.write((buffer[i] >>> 0) & 0xFF);
                            dos.write((buffer[i] >>> 8) & 0xFF);
                        }
                        
                        synchronized (mutex)
                        {
                            mutex.wait(10);
                        }
                    }
                    catch(Exception e)
                    {
                        Logger.log(this.getClass().getName(), e, "AndroidPcmMediaRecorder#Recording");
                        e.printStackTrace();
                        break;
                    }
                }
                
                try
                {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                    dos.close();
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);                
                }
                
            }
            
            if(mediaListener != null)
            {
                mediaListener.mediaUpdate(AndroidPcmMediaRecorder.this, ITnMediaListener.ON_COMPLETION, null);
            }
            
            if(mediaListener != null)
            {
                mediaListener.mediaUpdate(AndroidPcmMediaRecorder.this, ITnMediaListener.ON_CLOSE, null);
            }
    
        }
    }

    @Override
    public void abandonAudioFocus() throws TnMediaException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAllowAbandon()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
