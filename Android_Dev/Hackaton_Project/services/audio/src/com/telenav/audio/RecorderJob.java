/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecorderJob.java
 *
 */
package com.telenav.audio;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaPlayer;
import com.telenav.media.TnMediaRecorder;
import com.telenav.threadpool.IJob;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 27, 2010
 */
class RecorderJob implements IJob, ITnMediaListener
{
    protected String recordId;

    protected boolean isFinished = false;
    
    protected long recordTimeout;

    protected OutputStream os;

    protected IRecorderListener recorderListener;
    
    protected AudioRecorder recorder;

    protected boolean isCancelled = false;

    protected boolean isRunning = false;

    protected Object mutex = new Object();

    public RecorderJob(String recordId, long timeout, OutputStream os, IRecorderListener recorderListener, AudioRecorder recorder)
    {
        this.recordId = recordId;
        this.recordTimeout = timeout;
        this.os = os;
        if(this.os == null)
        {
            this.os = new ByteArrayOutputStream();
        }
        this.recorderListener = recorderListener;
        this.recorder = recorder;
    }

    public void cancel()
    {
        synchronized (mutex)
        {
            isCancelled = true;
            
            mutex.notify();
        }
    }
    
    public void finish()
    {
        isFinished = true;
        
        cancel();
        
        long time = System.currentTimeMillis();
        
        while(isRunning && (System.currentTimeMillis() - time) < 2000)//wait time will not longer than 2 seconds
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }

    public void execute(int handlerID)
    {
        this.isRunning = true;
        TnMediaRecorder mediaRecorder = null;
        try
        {
            synchronized (mutex)
            {
                if (recorderListener != null)
                {
                    try
                    {
                        recorderListener.beforeRecorder();
                    }
                    catch (Throwable ex)
                    {
                        Logger.log(this.getClass().getName(), ex);
                    }
                }
                
                mediaRecorder = this.recorder.mediaManager.createRecorder(this.recorder.audioFormat);
                
                if (isCancelled)
                    return;
                
                mediaRecorder.setRecordStream(this.os);
                
                if (isCancelled)
                    return;
                
                mediaRecorder.setListener(this);
                
                if (isCancelled)
                    return;
                
                mediaRecorder.realize();
                
                if (isCancelled)
                    return;

                mediaRecorder.prefetch();
                
                if (isCancelled)
                    return;

                mediaRecorder.start();
                
                if (isCancelled)
                    return;
                
                mutex.wait(this.recordTimeout);
                
                if (isCancelled && !isFinished)
                    return;
                
                mediaRecorder.commit();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            if (isCancelled && !isFinished)
            {
                try
                {
                    mediaRecorder.getRecordStream().close();
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
            try
            {
                mediaRecorder.stop();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            try
            {
                mediaRecorder.close();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            finally
            {
                mediaRecorder = null;
            }
            
            if (recorderListener != null)
            {
                try
                {
                    recorderListener.finishRecorder();
                }
                catch (Throwable ex)
                {
                    Logger.log(this.getClass().getName(), ex);
                }
            }
        }
        this.isRunning = false;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void mediaUpdate(TnMediaPlayer player, String event, Object eventData)
    {
        if(ITnMediaListener.ON_ERROR.equals(event))
        {
            this.cancel();
        }
    }

}
