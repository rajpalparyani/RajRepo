package com.telenav.media.android;

import org.junit.Test;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaManager.ITnVolumeUi;
import static org.junit.Assert.*;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidMediaManager.class)
public class AndroidMediaManagerTest
{
	private Context context;
	private AndroidMediaManager mediaManager;
	
	@Before
	public void setUp() throws Exception {
		context = EasyMock.createMock(Context.class);
		mediaManager = new AndroidMediaManager(context);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testBaseClass()
	{
		AndroidMediaManager.init(mediaManager);
		assertNotNull(AndroidMediaManager.getInstance());
	}
	
	@Test
	public void testCreatePlayer() throws Exception
	{
		AndroidMediaPlayer androidPlayer = EasyMock.createMock(AndroidMediaPlayer.class);
		PowerMock.expectNew(AndroidMediaPlayer.class, context, "url", "format").andReturn(androidPlayer);
		PowerMock.replay(androidPlayer,AndroidMediaPlayer.class);	
		mediaManager.createPlayer("url", "format");	
		PowerMock.verify(androidPlayer,AndroidMediaPlayer.class);	
		
		EasyMock.reset(context);
		EasyMock.reset(androidPlayer);
		
		/************************************************/
		PowerMock.reset(AndroidMediaPlayer.class);
		byte[] audio = new byte[20];
		PowerMock.expectNew(AndroidMediaPlayer.class, context, audio, "format").andReturn(androidPlayer);
		PowerMock.replay(androidPlayer,AndroidMediaPlayer.class);	
		mediaManager.createPlayer(audio, "format");
		PowerMock.verify(androidPlayer,AndroidMediaPlayer.class);
		
		EasyMock.reset(context);
		EasyMock.reset(androidPlayer);
	}
	
//	@Test
//	public void testCreateRecorder() throws Exception
//	{
//		AndroidPcmMediaRecorder mediaRecorder = EasyMock.createMock(AndroidPcmMediaRecorder.class);
//		PowerMock.expectNew(AndroidPcmMediaRecorder.class, TnMediaManager.FORMAT_PCM, 16000).andReturn(mediaRecorder);
//		PowerMock.replay(mediaRecorder, AndroidPcmMediaRecorder.class);
//		mediaManager.createRecorder(TnMediaManager.FORMAT_PCM);
//		PowerMock.verify(mediaRecorder, AndroidPcmMediaRecorder.class);
//		
//		/************************************************/
//		AndroidMediaRecorder androidMediaRecorder = EasyMock.createMock(AndroidMediaRecorder.class);
//		PowerMock.expectNew(AndroidMediaRecorder.class, context, "format").andReturn(androidMediaRecorder);
//		PowerMock.replay(androidMediaRecorder, AndroidMediaRecorder.class);
//		
//		mediaManager.createRecorder("format");	
//		PowerMock.verify(androidMediaRecorder, AndroidMediaRecorder.class);
//		
//	}
	
	
	@Test
	public void testGetMediaVolume()
	{
//		IMocksControl mockControl = EasyMock.createControl();
		AudioManager audioManager = EasyMock.createMock(AudioManager.class);
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager).times(1);
		EasyMock.expect(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)).andReturn(Integer.valueOf(10)).times(1);
//		audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		mediaManager.getMediaVolume();
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		//assertEquals(mediaManager.getMediaVolume(),10);
		
		
	}
	
	@Test
	public void testIsBluetoothHeadsetOn()
	{
		AudioManager audioManager = EasyMock.createMock(AudioManager.class);
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager).times(1);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(true).times(1);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.isBluetoothHeadsetOn();
		 
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
	}
	
	@Test
	public void testIsWireHeadsetOn()
	{
		AudioManager audioManager = EasyMock.createMock(AudioManager.class);
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager).times(1);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(true).times(1);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		boolean result = mediaManager.isWiredHeadsetOn();
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		assertEquals(false, result);
		
		
		/***************************************/		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager).times(1);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(false).times(1);
		EasyMock.expect(audioManager.isBluetoothScoOn()).andReturn(true).times(1);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		result = mediaManager.isWiredHeadsetOn();
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		assertEquals(false, result);
		
		/***************************************/		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager).times(1);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(false).times(1);
		EasyMock.expect(audioManager.isBluetoothScoOn()).andReturn(false).times(1);
		EasyMock.expect(audioManager.isSpeakerphoneOn()).andReturn(true).times(1);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		result = mediaManager.isWiredHeadsetOn();
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		assertEquals(false, result);
		
		/***************************************/		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager).times(1);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(false).times(1);
		EasyMock.expect(audioManager.isBluetoothScoOn()).andReturn(false).times(1);
		EasyMock.expect(audioManager.isSpeakerphoneOn()).andReturn(false).times(1);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		result = mediaManager.isWiredHeadsetOn();
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		assertEquals(true, result);	
		
	}
	
	@Test
	public void testSetAudioPath()
	{
		AudioManager audioManager = EasyMock.createMock(AudioManager.class);
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		
		audioManager.setSpeakerphoneOn(false);
		audioManager.setBluetoothA2dpOn(true);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.setAudioPath(TnMediaManager.PATH_BLUETOOTH_HEADSET);
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		
		/***************************************/	
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		
		audioManager.setSpeakerphoneOn(true);
		audioManager.setBluetoothA2dpOn(false);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.setAudioPath(TnMediaManager.PATH_SPEAKER_PHONE);
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		
		/***************************************/	
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		
		audioManager.setSpeakerphoneOn(false);
		audioManager.setBluetoothA2dpOn(false);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.setAudioPath(TnMediaManager.PATH_WIRED_HEADSET);
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
	
	}
	
	@Test
	public void testGetAudioPath()
	{
		AudioManager audioManager = EasyMock.createMock(AudioManager.class);
		
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(true);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		int path = mediaManager.getAudioPath();
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		assertEquals(TnMediaManager.PATH_BLUETOOTH_HEADSET, path);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		
		/*************************************************/
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(false);
		EasyMock.expect(audioManager.isSpeakerphoneOn()).andReturn(true);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		path = mediaManager.getAudioPath();
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		assertEquals(TnMediaManager.PATH_SPEAKER_PHONE, path);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		/*************************************************/
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(audioManager.isBluetoothA2dpOn()).andReturn(false);
		EasyMock.expect(audioManager.isSpeakerphoneOn()).andReturn(false);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		path = mediaManager.getAudioPath();
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		assertEquals(TnMediaManager.PATH_WIRED_HEADSET, path);	
	}
	
	@Test
	public void testSetMediaVolume()
	{
		AudioManager audioManager = EasyMock.createMock(AudioManager.class);	
		ITnVolumeUi volumeUi = EasyMock.createMock(ITnVolumeUi.class);
		
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).andReturn(10);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.setMediaVolume(-1, volumeUi);
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		/************************************************/
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).andReturn(10);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.setMediaVolume(-1, null);
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		/************************************************/
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).andReturn(10).times(2);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, AudioManager.FLAG_SHOW_UI);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.setMediaVolume(11, volumeUi);
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
		
		/************************************************/
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).andReturn(10);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 8, AudioManager.FLAG_SHOW_UI);
		
		EasyMock.replay(context);
		EasyMock.replay(audioManager);
		
		mediaManager.setMediaVolume(8, volumeUi);
		
		EasyMock.verify(context);
		EasyMock.verify(audioManager);
		
		EasyMock.reset(context);
		EasyMock.reset(audioManager);
	}
	
}
