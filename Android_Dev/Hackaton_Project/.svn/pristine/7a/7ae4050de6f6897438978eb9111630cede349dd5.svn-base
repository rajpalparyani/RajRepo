/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestRecordJob.java
 *
 */
package com.telenav.audio;

import java.io.OutputStream;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaRecorder;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-15
 */
public class RecorderJobTest extends TestCase
{
    public void testExecute()
    {
        //String recordId, long timeout, OutputStream os, IRecorderListener recorderListener, AudioRecorder recorder
        String recordId = "unit_test";
        long timeout = 1000;
        OutputStream mockOs = PowerMock.createMock(OutputStream.class);
        IRecorderListener mockRecorderListener = PowerMock.createMock(IRecorderListener.class);
        AudioRecorder mockRecorder = PowerMock.createMock(AudioRecorder.class);
        TnMediaManager mockMediaManager = PowerMock.createMock(TnMediaManager.class);
        mockRecorder.mediaManager = mockMediaManager;
        mockRecorder.audioFormat = TnMediaManager.FORMAT_MP3;
        TnMediaRecorder mockMediaRecorder = PowerMock.createMock(TnMediaRecorder.class);
        
        //record
        mockRecorderListener.beforeRecorder();
        try
        {
            EasyMock.expect(mockMediaManager.createRecorder(EasyMock.anyObject(String.class))).andReturn(mockMediaRecorder);
        }
        catch (TnMediaException e)
        {
            e.printStackTrace();
        }
        
        mockMediaRecorder.setRecordStream(mockOs);
        mockMediaRecorder.setListener(EasyMock.anyObject(ITnMediaListener.class));
        try
        {
            mockMediaRecorder.realize();
            mockMediaRecorder.prefetch();
            mockMediaRecorder.start();
            mockMediaRecorder.commit();
        }
        catch (TnMediaException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                mockMediaRecorder.stop();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
            
            try
            {
                mockMediaRecorder.close();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
            
            mockRecorderListener.finishRecorder();
        }
        
        
        //replay
        PowerMock.replayAll();
        
        RecorderJob recorderJob = new RecorderJob(recordId, timeout, mockOs, mockRecorderListener, mockRecorder);
        recorderJob.execute(0);
        
        //verify
        PowerMock.verifyAll();
    }
}
