package com.telenav.media.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaPlayer;
import static org.junit.Assert.*;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;


@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidMediaPlayer.class, Uri.class, Logger.class})
public class AndroidMediaPlayerTest 
{
	protected MediaPlayer mediaPlayer;
	protected AndroidMediaPlayer androidMediaPlayer;
	protected Context context;
	
	@Before
	public void setUp() throws Exception
	{
		context = PowerMock.createMock(Context.class);
		mediaPlayer = PowerMock.createMock(MediaPlayer.class);
		
		PowerMock.expectNew(MediaPlayer.class).andReturn(mediaPlayer);	
		PowerMock.replay(mediaPlayer, MediaPlayer.class);
		
//		PowerMock.suppressConstructor(AndroidMediaPlayer.class);
		androidMediaPlayer = new AndroidMediaPlayer(context, "url", "format");
//		androidMediaPlayer.context = context;
//		androidMediaPlayer.audioformat = "format";
//		androidMediaPlayer.url = "url";
//		androidMediaPlayer.mediaPlayer = mediaPlayer;
				
//		PowerMock.verify(mediaPlayer, MediaPlayer.class);
	}
	
	@After
	public void tearDown() throws Exception
	{
		
	}
	
	@Test
	public void testConstruct() throws Exception
	{
		PowerMock.resetAll();
		PowerMock.expectNew(MediaPlayer.class).andReturn(mediaPlayer);
		PowerMock.replayAll();
		AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer(context, new byte[10], "mp3");
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testClose() throws Exception
	{
		PowerMock.resetAll();
		ITnMediaListener mediaLisener = EasyMock.createMock(ITnMediaListener.class);
		androidMediaPlayer.setListener(mediaLisener);
		
		mediaPlayer.release();
		mediaLisener.mediaUpdate(androidMediaPlayer, ITnMediaListener.ON_CLOSE, null);
		
		EasyMock.replay(mediaPlayer);
		androidMediaPlayer.close();
		EasyMock.verify(mediaPlayer);
	}
	
	@Test
	public void testGetContentType() throws Exception
	{
		PowerMock.resetAll();
		PowerMock.expectNew(MediaPlayer.class).andReturn(mediaPlayer);	
		PowerMock.replayAll();
		AndroidMediaPlayer player = new AndroidMediaPlayer(context, "url", "myFormat");	
		PowerMock.verifyAll();
		assertEquals(player.getContentType(),"myFormat");
		
	}
	
	@Test
	public void testPrefetch() throws Exception
	{
		PowerMock.reset(mediaPlayer);
		androidMediaPlayer.fileName = "fileName";
		
		mediaPlayer.setOnCompletionListener(androidMediaPlayer);
		mediaPlayer.setOnErrorListener(androidMediaPlayer);
		mediaPlayer.setOnInfoListener(androidMediaPlayer);
		mediaPlayer.setOnPreparedListener(androidMediaPlayer);
		mediaPlayer.setDataSource( "fileName");
		mediaPlayer.prepare();
		
		PowerMock.replay(mediaPlayer);
		androidMediaPlayer.prefetch();
		PowerMock.verify(mediaPlayer);
		
		/************************************************/
		PowerMock.reset(mediaPlayer);
		androidMediaPlayer.fileName = null;
		androidMediaPlayer.url = "url";
		
		Uri uri = PowerMock.createMock(Uri.class);
		PowerMock.mockStatic(Uri.class);
		EasyMock.expect(Uri.parse("url")).andReturn(uri);
		
		
		mediaPlayer.setOnCompletionListener(androidMediaPlayer);
		mediaPlayer.setOnErrorListener(androidMediaPlayer);
		mediaPlayer.setOnInfoListener(androidMediaPlayer);
		mediaPlayer.setOnPreparedListener(androidMediaPlayer);
		mediaPlayer.setDataSource(context, uri);
		mediaPlayer.prepare();
		
		PowerMock.replayAll();
		
		androidMediaPlayer.prefetch();
		
		PowerMock.verifyAll();
		
	}
	
	@Test
	public void testRealize() throws Exception
	{
		PowerMock.resetAll();
		
		byte[] audioData = new byte[20];
		androidMediaPlayer.audioData = audioData;
		AudioManager audioManager = PowerMock.createMock(AudioManager.class);
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(
				audioManager.requestAudioFocus(EasyMock
				.anyObject(OnAudioFocusChangeListener.class), 
				EasyMock.anyInt(), EasyMock.anyInt())).andReturn(1);
		EasyMock.expect(context.deleteFile("temp_media" + ".audio")).andReturn(true);
		FileOutputStream stream = PowerMock.createMock(FileOutputStream.class);
		EasyMock.expect(context.openFileOutput(EasyMock.anyObject(String.class), EasyMock.anyInt())).andReturn(stream);
		stream.write(audioData);
		stream.close();
		File file = PowerMock.createMock(File.class);
		EasyMock.expect(file.getAbsolutePath()).andReturn("path");
		EasyMock.expect(context.getFilesDir()).andReturn(file);
		
		PowerMock.replayAll();
		androidMediaPlayer.realize();
		PowerMock.verifyAll();
	}
	
	@Test(expected=TnMediaException.class)
	public void testRealize_Exception() throws Exception
	{
		PowerMock.resetAll();
		
		byte[] audioData = new byte[20];
		androidMediaPlayer.audioData = audioData;
		AudioManager audioManager = PowerMock.createMock(AudioManager.class);
		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
		EasyMock.expect(
				audioManager.requestAudioFocus(EasyMock
						.anyObject(OnAudioFocusChangeListener.class), 
						EasyMock.anyInt(), EasyMock.anyInt())).andReturn(1);
		EasyMock.expect(context.deleteFile("temp_media" + ".audio")).andReturn(true);
		FileOutputStream stream = PowerMock.createMock(FileOutputStream.class);
		EasyMock.expect(context.openFileOutput(EasyMock.anyObject(String.class), EasyMock.anyInt())).andReturn(stream);
		stream.write(audioData);
		stream.close();
		IOException e = new IOException();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IOException.class));
		
		PowerMock.replayAll();
		androidMediaPlayer.realize();
		PowerMock.verifyAll();
	}
	
	
	@Test(expected=TnMediaException.class)
	public void testPrefetch_Exception() throws Exception
	{
		PowerMock.resetAll();
		androidMediaPlayer.fileName = "fileName";
		
		mediaPlayer.setOnCompletionListener(androidMediaPlayer);
		mediaPlayer.setOnErrorListener(androidMediaPlayer);
		mediaPlayer.setOnInfoListener(androidMediaPlayer);
		mediaPlayer.setOnPreparedListener(androidMediaPlayer);
		mediaPlayer.setDataSource( "fileName");
		IOException e = new IOException();
		EasyMock.expectLastCall().andThrow(e);
		//mediaPlayer.prepare();
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IOException.class));
		
		PowerMock.replay(mediaPlayer);
		androidMediaPlayer.prefetch();
		PowerMock.verify(mediaPlayer);
	}
	
	@Test
	public void testReset() throws Exception
	{
		PowerMock.reset(mediaPlayer);
		mediaPlayer.reset();
		PowerMock.replayAll();
		androidMediaPlayer.reset();
		PowerMock.verifyAll();	
	}

	@Test(expected=TnMediaException.class)
	public void testReset_Exception() throws Exception
	{
		PowerMock.reset(mediaPlayer);
		mediaPlayer.reset();
		RuntimeException e = new RuntimeException();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
		PowerMock.replayAll();
		androidMediaPlayer.reset();
		PowerMock.verifyAll();	
	}

	@Test
	public void testStart() throws Exception
	{
		PowerMock.reset(mediaPlayer);
		mediaPlayer.start();
		PowerMock.replayAll();
		androidMediaPlayer.start();
		PowerMock.verifyAll();	
	}
	
	@Test(expected=TnMediaException.class)
	public void testStart_Exception() throws Exception
	{
		PowerMock.reset(mediaPlayer);
		IllegalStateException e = new IllegalStateException();
		mediaPlayer.start();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IllegalStateException.class));
		PowerMock.replayAll();
		androidMediaPlayer.start();
		PowerMock.verifyAll();	
	}

//	@Test
//	public void testStop() throws Exception
//	{
//		PowerMock.resetAll();
//		androidMediaPlayer.mediaPlayer = mediaPlayer;
//		mediaPlayer.stop();
//		
//		AudioManager audioManager = PowerMock.createMock(AudioManager.class);
//		EasyMock.expect(context.getSystemService(Context.AUDIO_SERVICE)).andReturn(audioManager);
//		EasyMock.expect(audioManager.abandonAudioFocus(EasyMock.anyObject(OnAudioFocusChangeListener.class))).andReturn(1);
//		PowerMock.replayAll();
//		androidMediaPlayer.stop();
//		PowerMock.verifyAll();	
//	}
	
	@Test(expected=TnMediaException.class)
	public void testStop_Excepton() throws Exception
	{
		PowerMock.resetAll();
		androidMediaPlayer.mediaPlayer = mediaPlayer;
		IllegalStateException e = new IllegalStateException();
		mediaPlayer.stop();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IllegalStateException.class));
		PowerMock.replayAll();
		androidMediaPlayer.stop();
		PowerMock.verifyAll();	
	}
	
	@Test
	public void testOnCompletion()
	{
		ITnMediaListener mediaListener = PowerMock.createMock(ITnMediaListener.class);
		androidMediaPlayer.setListener(mediaListener);
		mediaListener.mediaUpdate(androidMediaPlayer, ITnMediaListener.ON_COMPLETION, null);
		PowerMock.replayAll();
		androidMediaPlayer.onCompletion(null);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testOnError()
	{
		ITnMediaListener mediaListener = PowerMock.createMock(ITnMediaListener.class);
		androidMediaPlayer.setListener(mediaListener);
		mediaListener.mediaUpdate(androidMediaPlayer, ITnMediaListener.ON_ERROR, "1,2");
		PowerMock.replayAll();
		androidMediaPlayer.onError(null, 1, 2);
		PowerMock.verifyAll();
	}

	@Test
	public void testOnInfo()
	{
		ITnMediaListener mediaListener = PowerMock.createMock(ITnMediaListener.class);
		androidMediaPlayer.setListener(mediaListener);
		mediaListener.mediaUpdate(androidMediaPlayer, ITnMediaListener.ON_INFO, "1,2");
		PowerMock.replayAll();
		androidMediaPlayer.onInfo(null, 1, 2);
		PowerMock.verifyAll();
	}

	@Test
	public void testOnPrepared()
	{
		ITnMediaListener mediaListener = PowerMock.createMock(ITnMediaListener.class);
		androidMediaPlayer.setListener(mediaListener);
		mediaListener.mediaUpdate(androidMediaPlayer, ITnMediaListener.ON_PREPARE, null);
		PowerMock.replayAll();
		androidMediaPlayer.onPrepared(null);
		PowerMock.verifyAll();
	}
}
