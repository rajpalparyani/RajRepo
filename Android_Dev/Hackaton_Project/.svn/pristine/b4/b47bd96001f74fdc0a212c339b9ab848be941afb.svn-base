/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestPlayerJob.java
 *
 */
package com.telenav.audio;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.datatypes.audio.AudioData;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaPlayer;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-15
 */
public class PlayerJobTest extends TestCase
{
    String playId;
    AudioData[] audios;
    Synthesizer synthesizer;
    long playTimeout;
    IPlayerListener playerListener;
    AudioPlayer player;
    
    protected void setUp() throws Exception
    {
        playId = "unit_test";
        audios = new AudioData[2];
        AudioData audio1 = PowerMock.createMock(AudioData.class);
        AudioData audio2 = PowerMock.createMock(AudioData.class);
        audios[0] = audio1;
        audios[1] = audio2;
        synthesizer = PowerMock.createMock(Synthesizer.class);
        playTimeout = 2000;
        playerListener = PowerMock.createMock(IPlayerListener.class);
        player = PowerMock.createMock(AudioPlayer.class);
        
        super.setUp();
    }
    
    public void testCancel()
    {
        //record
        
        //replay
        PowerMock.replayAll();
        
        PlayerJob playerJob = new PlayerJob(playId, audios, synthesizer, playTimeout, playerListener, player);
        playerJob.cancel();
        assertTrue(playerJob.isCancelled());
        //verify
        PowerMock.verifyAll();
    }
    
    public void testExecute_A()
    {
        TnMediaManager mockMediaManager = PowerMock.createMock(TnMediaManager.class);
        player.mediaManager = mockMediaManager;
        player.audioFormat = TnMediaManager.FORMAT_MP3;
        
        //record
        //format: mp3
        //case 0: player not enabled.
        EasyMock.expect(player.isEnabled()).andReturn(false);
        playerListener.finishPlayer();
        
        //replay
        PowerMock.replayAll();
        
        PlayerJob playerJob = new PlayerJob(playId, audios, synthesizer, playTimeout, playerListener, player);
        playerJob.execute(0);
        
        //verify
        PowerMock.verifyAll();
    }
    
    public void testExecute_B()
    {
        TnMediaManager mockMediaManager = PowerMock.createMock(TnMediaManager.class);
        player.mediaManager = mockMediaManager;
        player.audioFormat = TnMediaManager.FORMAT_MP3;
        TnMediaPlayer mockMediaPlayer = PowerMock.createMock(TnMediaPlayer.class);
        byte[] audioData = new byte[]{0,0};
        
        //case 1: player is enabled. isWiredHeadsetOn: false, isBluetoothHeadsetOn: false
        EasyMock.expect(player.isEnabled()).andReturn(true);
        EasyMock.expect(player.isNeedAbandonFocus()).andReturn(true);
        EasyMock.expect(mockMediaManager.isWiredHeadsetOn()).andReturn(false).times(2);
        EasyMock.expect(mockMediaManager.isBluetoothHeadsetOn()).andReturn(false).times(2);
        mockMediaManager.setAudioPath(TnMediaManager.PATH_SPEAKER_PHONE);
        mockMediaManager.setAudioPath(TnMediaManager.PATH_SPEAKER_PHONE);
        EasyMock.expect(synthesizer.synthesize(audios)).andReturn(audioData);
        try
        {
            EasyMock.expect(mockMediaManager.createPlayer(audioData, TnMediaManager.FORMAT_MP3)).andReturn(mockMediaPlayer);
            EasyMock.expect(mockMediaPlayer.isAllowAbandon()).andReturn(true);
            mockMediaPlayer.abandonAudioFocus();
        }
        catch (TnMediaException e)
        {
            e.printStackTrace();
        }
        mockMediaPlayer.setListener(EasyMock.anyObject(ITnMediaListener.class));
        try
        {
            mockMediaPlayer.realize();
            mockMediaPlayer.prefetch();
            mockMediaPlayer.start();
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
                mockMediaPlayer.stop();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
            try
            {
                mockMediaPlayer.close();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
        }
        playerListener.finishPlayer();
        
        PowerMock.replayAll();
        
        PlayerJob playerJob = new PlayerJob(playId, audios, synthesizer, playTimeout, playerListener, player);
        
        playerJob.execute(1);
        
        PowerMock.verifyAll();
    }
    
    public void testExecute_C()
    {
        TnMediaManager mockMediaManager = PowerMock.createMock(TnMediaManager.class);
        player.mediaManager = mockMediaManager;
        player.audioFormat = TnMediaManager.FORMAT_MP3;
        TnMediaPlayer mockMediaPlayer = PowerMock.createMock(TnMediaPlayer.class);
        byte[] audioData = new byte[]{0,0};
        
        //case 2: player is enabled. isWiredHeadsetOn: true, isBluetoothHeadsetOn: false
        EasyMock.expect(player.isEnabled()).andReturn(true);
        EasyMock.expect(player.isNeedAbandonFocus()).andReturn(true);
        EasyMock.expect(mockMediaManager.isWiredHeadsetOn()).andReturn(true).times(2);
        EasyMock.expect(mockMediaManager.isBluetoothHeadsetOn()).andReturn(false).times(2);
        mockMediaManager.setAudioPath(TnMediaManager.PATH_WIRED_HEADSET);
        mockMediaManager.setAudioPath(TnMediaManager.PATH_WIRED_HEADSET);
        EasyMock.expect(synthesizer.synthesize(audios)).andReturn(audioData);
        try
        {
            EasyMock.expect(mockMediaManager.createPlayer(audioData, TnMediaManager.FORMAT_MP3)).andReturn(mockMediaPlayer);
            EasyMock.expect(mockMediaPlayer.isAllowAbandon()).andReturn(true);
            mockMediaPlayer.abandonAudioFocus();
        }
        catch (TnMediaException e)
        {
            e.printStackTrace();
        }
        mockMediaPlayer.setListener(EasyMock.anyObject(ITnMediaListener.class));
        try
        {
            mockMediaPlayer.realize();
            mockMediaPlayer.prefetch();
            mockMediaPlayer.start();
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
                mockMediaPlayer.stop();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
            try
            {
                mockMediaPlayer.close();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
        }
        playerListener.finishPlayer();
        
        PowerMock.replayAll();
        
        PlayerJob playerJob = new PlayerJob(playId, audios, synthesizer, playTimeout, playerListener, player);
        
        playerJob.execute(2);
        
        PowerMock.verifyAll();
    }
    
    public void testExecute_D()
    {
        TnMediaManager mockMediaManager = PowerMock.createMock(TnMediaManager.class);
        player.mediaManager = mockMediaManager;
        player.audioFormat = TnMediaManager.FORMAT_MP3;
        TnMediaPlayer mockMediaPlayer = PowerMock.createMock(TnMediaPlayer.class);
        byte[] audioData = new byte[]{0,0};
        
        //case 3: player is enabled. isWiredHeadsetOn: false, isBluetoothHeadsetOn: true
        EasyMock.expect(player.isEnabled()).andReturn(true);
        EasyMock.expect(player.isNeedAbandonFocus()).andReturn(true).times(2);
        EasyMock.expect(mockMediaManager.isWiredHeadsetOn()).andReturn(false);
        EasyMock.expect(mockMediaManager.isBluetoothHeadsetOn()).andReturn(true).times(2);
        mockMediaManager.setAudioPath(TnMediaManager.PATH_BLUETOOTH_HEADSET);
        EasyMock.expect(mockMediaManager.getAudioPath()).andReturn(TnMediaManager.PATH_BLUETOOTH_HEADSET);
        
        try
        {
            EasyMock.expect(mockMediaManager.createPlayer(EasyMock.anyObject(byte[].class), EasyMock.anyObject(String.class))).andReturn(mockMediaPlayer);
//            mockMediaPlayer.abandonAudioFocus();
        }
        catch (TnMediaException e)
        {
            e.printStackTrace();
        }
        try
        {
            mockMediaPlayer.realize();
            mockMediaPlayer.prefetch();
            mockMediaPlayer.start();
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
                mockMediaPlayer.stop();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
            try
            {
                mockMediaPlayer.close();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
        }
        
        EasyMock.expect(synthesizer.synthesize(audios)).andReturn(audioData);
        try
        {
            EasyMock.expect(mockMediaManager.createPlayer(audioData, TnMediaManager.FORMAT_MP3)).andReturn(mockMediaPlayer);
            EasyMock.expect(mockMediaPlayer.isAllowAbandon()).andReturn(true);
            mockMediaPlayer.abandonAudioFocus();
        }
        catch (TnMediaException e)
        {
            e.printStackTrace();
        }
        mockMediaPlayer.setListener(EasyMock.anyObject(ITnMediaListener.class));
        try
        {
            mockMediaPlayer.realize();
            mockMediaPlayer.prefetch();
            mockMediaPlayer.start();
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
                mockMediaPlayer.stop();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
            try
            {
                mockMediaPlayer.close();
            }
            catch (TnMediaException e)
            {
                e.printStackTrace();
            }
        }
        playerListener.finishPlayer();
        
        //replay
        PowerMock.replayAll();
        
        PlayerJob playerJob = new PlayerJob(playId, audios, synthesizer, playTimeout, playerListener, player);
        playerJob.execute(3);
        
        //verify
        PowerMock.verifyAll();
    }
}
