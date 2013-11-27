/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestAudioPlayer.java
 *
 */
package com.telenav.audio;

import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioData.IAudioDataProvider;
import com.telenav.media.TnMediaManager;
import com.telenav.threadpool.ThreadPool;

/**
 *@author yning
 *@date 2011-6-15
 */
public class AudioPlayerTest extends TestCase
{
    TnMediaManager mockMediaManager;
    ThreadPool mockAudioThreadPool;
    IAudioDataProvider mockAudioDataProvider;
    String audioFormat;
    
    protected void setUp() throws Exception
    {
        mockMediaManager = PowerMock.createMock(TnMediaManager.class);
        mockAudioThreadPool = PowerMock.createMock(ThreadPool.class);
        mockAudioDataProvider = PowerMock.createMock(IAudioDataProvider.class);
        audioFormat = "mp3";
        super.setUp();
    }
    
    public void testCancelAll()
    {
        //record
        mockAudioThreadPool.cancelAll();
        //replay
        PowerMock.replayAll();
        
        AudioPlayer audioPlayer = new AudioPlayer(mockMediaManager, mockAudioThreadPool, mockAudioDataProvider, audioFormat);
        audioPlayer.cancelAll();
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testCancelJob()
    {
        PlayerJob mockJob1 = PowerMock.createMock(PlayerJob.class);
        PlayerJob mockJob2 = PowerMock.createMock(PlayerJob.class);
        Vector jobs = new Vector();
        jobs.addElement(mockJob1);
        jobs.addElement(mockJob2);
        mockJob1.playId = "should_not_cancel";
        mockJob2.playId = "unit_test";
        
        //record
        EasyMock.expect(mockAudioThreadPool.getPendingJobs()).andReturn(jobs);
        mockJob2.cancel();
        //replay
        PowerMock.replayAll();
        
        AudioPlayer audioPlayer = new AudioPlayer(mockMediaManager, mockAudioThreadPool, mockAudioDataProvider, audioFormat);
        audioPlayer.cancelJob("unit_test");
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testEnable()
    {
        //record
        mockAudioThreadPool.cancelAll();
        //replay
        PowerMock.replayAll();
        
        AudioPlayer audioPlayer = new AudioPlayer(mockMediaManager, mockAudioThreadPool, mockAudioDataProvider, audioFormat);
        audioPlayer.enable(false);
        assertFalse(audioPlayer.isEnabled);
        audioPlayer.enable(true);
        assertTrue(audioPlayer.isEnabled);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testIsPlaying()
    {
        //record
        EasyMock.expect(mockAudioThreadPool.isIdle()).andReturn(true).times(1).andReturn(false).times(1);
        //replay
        PowerMock.replayAll();
        
        AudioPlayer audioPlayer = new AudioPlayer(mockMediaManager, mockAudioThreadPool, mockAudioDataProvider, audioFormat);
        assertFalse(audioPlayer.isPlaying());
        assertTrue(audioPlayer.isPlaying());
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testIsEnabled()
    {
        //record
        mockAudioThreadPool.cancelAll();
        //replay
        PowerMock.replayAll();
        
        AudioPlayer audioPlayer = new AudioPlayer(mockMediaManager, mockAudioThreadPool, mockAudioDataProvider, audioFormat);
        assertTrue(audioPlayer.isEnabled());
        audioPlayer.enable(false);
        assertFalse(audioPlayer.isEnabled());
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testPlay()
    {
        Synthesizer mockSynthesizer = PowerMock.createMock(Synthesizer.class);
        IPlayerListener mockPlayerListener = PowerMock.createMock(IPlayerListener.class);
        
        AudioData[] audioData = new AudioData[2];
        AudioData mockAudioData0 = PowerMock.createMock(AudioData.class);
        AudioData mockAudioData1 = PowerMock.createMock(AudioData.class);
        audioData[0] = mockAudioData0;
        audioData[1] = mockAudioData1;
        
        //record
        EasyMock.expect(mockAudioData0.getDataProvider()).andReturn(null);
        mockAudioData0.setDataProvider(mockAudioDataProvider);
        EasyMock.expect(mockAudioData1.getDataProvider()).andReturn(null);
        mockAudioData1.setDataProvider(mockAudioDataProvider);
        EasyMock.expect(mockAudioThreadPool.addJob(EasyMock.anyObject(PlayerJob.class))).andReturn(1);
        //replay
        PowerMock.replayAll();
        
        AudioPlayer audioPlayer = new AudioPlayer(mockMediaManager, mockAudioThreadPool, mockAudioDataProvider, audioFormat);
        String playId = audioPlayer.play("unit_test", audioData, mockSynthesizer, 10000, mockPlayerListener);
        assertEquals("unit_test", playId);
        
        //verify
        PowerMock.verifyAll();
    }
}
