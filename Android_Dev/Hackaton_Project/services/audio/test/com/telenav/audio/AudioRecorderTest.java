/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestAudioRecorder.java
 *
 */
package com.telenav.audio;

import java.io.OutputStream;
import java.util.Vector;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.media.TnMediaManager;
import com.telenav.threadpool.ThreadPool;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-15
 */
public class AudioRecorderTest extends TestCase
{
    TnMediaManager mockMediaManager;
    ThreadPool mockRecordPool;
    String audioFormat;
    
    protected void setUp() throws Exception
    {
        //mock
        mockMediaManager = PowerMock.createMock(TnMediaManager.class);
        mockRecordPool = PowerMock.createMock(ThreadPool.class);
        audioFormat = "mp3";
        super.setUp();
    }
    
    public void testCancel()
    {
        //record
        mockRecordPool.cancelAll();
        //replay
        PowerMock.replayAll();
        
        AudioRecorder recorder = new AudioRecorder(mockMediaManager, mockRecordPool, audioFormat);
        recorder.cancel();
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testFinish()
    {
        RecorderJob job1 = PowerMock.createMock(RecorderJob.class);
        RecorderJob job2 = PowerMock.createMock(RecorderJob.class);
        OutputStream os = PowerMock.createMock(OutputStream.class);
        job1.os = os;
        Vector jobs = new Vector();
        jobs.addElement(job1);
        jobs.addElement(job2);
        
        //record
        EasyMock.expect(mockRecordPool.getCurrentJobs()).andReturn(jobs);
        job1.finish();
        
        //replay
        PowerMock.replayAll();
        
        AudioRecorder recorder = new AudioRecorder(mockMediaManager, mockRecordPool, audioFormat);
        OutputStream result = recorder.finish();
        assertEquals(result, os);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testIsRecording()
    {
        //record
        EasyMock.expect(mockRecordPool.isIdle()).andReturn(true).andReturn(false);
        //replay
        PowerMock.replayAll();
        
        AudioRecorder recorder = new AudioRecorder(mockMediaManager, mockRecordPool, audioFormat);
        assertFalse(recorder.isRecording());
        assertTrue(recorder.isRecording());
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testRecord()
    {
        //record
        mockRecordPool.cancelAll();
        EasyMock.expect(mockRecordPool.addJob(EasyMock.anyObject(RecorderJob.class))).andReturn(1);
        //replay
        PowerMock.replayAll();
        
        AudioRecorder recorder = new AudioRecorder(mockMediaManager, mockRecordPool, audioFormat);
        recorder.record(1000, null, null);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testSetAudioFormat()
    {
        //record
        
        //replay
        PowerMock.replayAll();
        
        AudioRecorder recorder = new AudioRecorder(mockMediaManager, mockRecordPool, audioFormat);
        recorder.setAudioFormat("amr");
        assertEquals("amr", recorder.audioFormat);
        recorder.setAudioFormat("pcm");
        assertEquals("pcm", recorder.audioFormat);
        
        //verify
        PowerMock.verifyAll();
    }
}
