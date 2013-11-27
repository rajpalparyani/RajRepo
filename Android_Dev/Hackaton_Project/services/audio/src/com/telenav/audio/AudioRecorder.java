/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AudioRecorder.java
 *
 */
package com.telenav.audio;

import java.io.OutputStream;
import java.util.Vector;

import com.telenav.media.TnMediaManager;
import com.telenav.threadpool.ThreadPool;

/**
 * AudioRecorder class can be used to record the audio with streams. <br />
 * Only allow one record job one time.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 27, 2010
 */
public class AudioRecorder
{
    protected TnMediaManager mediaManager;

    protected ThreadPool recorderPool;

    protected String audioFormat;
    
    /**
     * construct a audio recorder.
     * 
     * @param mediaManager platform's media manager.
     * @param recorderPool a thread pool to record audio.
     * @param audioFormat audio data's format
     */
    public AudioRecorder(TnMediaManager mediaManager, ThreadPool recorderPool, String audioFormat)
    {
        if (mediaManager == null)
        {
            throw new IllegalArgumentException("The mediaManager is null.");
        }
        if (recorderPool == null)
        {
            throw new IllegalArgumentException("The recorderPool is null.");
        }
        if (audioFormat == null)
        {
            throw new IllegalArgumentException("The audioFormat is null.");
        }
        
        this.mediaManager = mediaManager;
        this.recorderPool = recorderPool;
        this.audioFormat = audioFormat;
    }
    
    /**
     * set audio's format.
     * @param audioFormat audio data's format
     */
    public void setAudioFormat(String audioFormat)
    {
        this.audioFormat = audioFormat;        
    }
    
    /**
     * record the audio.
     * 
     * @param timeout the time out when record the audio.
     */
    public void record(long timeout)
    {
        record(timeout, null, null);
    }

    /**
     * record the audio.
     * 
     * @param timeout the time out when record the audio.
     * @param os the output stream from outside.
     * @param recorderListener the call back of recorder.
     */
    public void record(long timeout, OutputStream os, IRecorderListener recorderListener)
    {
        if(timeout <= 0)
        {
            timeout = 30 * 1000;
        }
        
        String recordId = "record_" + System.currentTimeMillis();

        this.recorderPool.cancelAll();
        
        RecorderJob recorderJob = new RecorderJob(recordId, timeout, os, recorderListener, this);

        this.recorderPool.addJob(recorderJob);
    }

    /**
     * finish the recorder.<br />
     * <b>Need close the output stream at outside.</b>
     * 
     * @return Get the output stream from the recorder.
     */
    public OutputStream finish()
    {
        Vector jobs = this.recorderPool.getCurrentJobs();

        if(jobs.size() > 0)
        {
            RecorderJob job = (RecorderJob) jobs.elementAt(0);
            job.finish();
    
            return job.os;
        }
        return null;
    }
    
    /**
     * Cancel the job of the recorder.
     */
    public void cancel()
    {
        this.recorderPool.cancelAll();
    }

    /**
     * check currently if it is recording the audio.
     * 
     * @return true means is recording the audio, false otherwise.
     */
    public boolean isRecording()
    {
        return !this.recorderPool.isIdle();
    }
}
