package com.telenav.media.android;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

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
import com.telenav.media.android.AndroidMediaRecorder.RecorderTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;


@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidMediaRecorder.class, Logger.class})
public class AndroidMediaRecorderTest 
{
	protected MediaRecorder mediaRecorder;
	protected Context context;
	protected ByteArrayOutputStream byteStream;
	protected AndroidMediaRecorder androidMediaRecorder;
	
	
	@Before
	public void setUp() throws Exception
	{
		context = PowerMock.createMock(Context.class);
		mediaRecorder = PowerMock.createMock(MediaRecorder.class);
		//byteStream = PowerMock.createMock(ByteArrayOutputStream.class);
		
		PowerMock.expectNew(MediaRecorder.class).andReturn(mediaRecorder);
		//PowerMock.expectNew(ByteArrayOutputStream.class).andReturn(byteStream);
		
		PowerMock.replayAll();	
		
		androidMediaRecorder = new AndroidMediaRecorder(context, "format");
	}
	
	@After
	public void tearDown() throws Exception
	{
		
	}
	
	@Test
	public void testSetRecordSizeLimitDelegate() throws Exception
	{
		PowerMock.resetAll();
		mediaRecorder.setMaxFileSize(Integer.MAX_VALUE);
		PowerMock.replayAll();
		androidMediaRecorder.setRecordSizeLimitDelegate();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetRecordSizeLimit() throws Exception
	{
		PowerMock.resetAll();
		mediaRecorder.setMaxFileSize(20);
		PowerMock.replayAll();
		androidMediaRecorder.setRecordSizeLimit(20);
		PowerMock.verifyAll();
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetRecordSizeLimit_Exception() throws Exception
	{
		androidMediaRecorder.setRecordSizeLimit(-1);
	}
	
	@Test(expected=TnMediaException.class)
	public void testSetRecordSizeLimitDelegate_Exception() throws Exception
	{
		PowerMock.resetAll();
		IllegalArgumentException e = new IllegalArgumentException();
		mediaRecorder.setMaxFileSize(Integer.MAX_VALUE);
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IllegalArgumentException.class));
		PowerMock.replayAll();
		androidMediaRecorder.setRecordSizeLimitDelegate();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testSetRecordStream() throws Exception
	{
		PowerMock.resetAll();
		OutputStream os = PowerMock.createMock(OutputStream.class);
		androidMediaRecorder.setRecordStream(os);
		assertEquals(androidMediaRecorder.getRecordStream(), os);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetRecordStream_Exception() throws Exception
	{
		PowerMock.resetAll();
		androidMediaRecorder.setRecordStream(null);
	}
	
	
	@Test
	public void testClose() throws Exception
	{
		PowerMock.resetAll();
		RandomAccessFile file = PowerMock.createMock(RandomAccessFile.class);
		androidMediaRecorder.mediaTempFile = file;
		RecorderTask task = PowerMock.createMock(RecorderTask.class);
		androidMediaRecorder.recorderTask = task;
		
		PowerMock.reset(mediaRecorder);
		file.close();
		mediaRecorder.release();
		task.cancel();
		PowerMock.replayAll();
		
		androidMediaRecorder.close();	
		PowerMock.verifyAll();
	}
	
	@Test(expected=TnMediaException.class)
	public void testClose_Exception() throws Exception
	{
		PowerMock.resetAll();
		RandomAccessFile file = PowerMock.createMock(RandomAccessFile.class);
		androidMediaRecorder.mediaTempFile = file;
		IOException e = new IOException();
		file.close();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IOException.class));
		PowerMock.replayAll();	
		androidMediaRecorder.close();	
		PowerMock.verifyAll();
	}
	
	@Test
	public void testPrefetch() throws Exception
	{
		PowerMock.resetAll();		
		PowerMock.expectNew(MediaRecorder.class).andReturn(mediaRecorder);	
		PowerMock.replayAll();
		androidMediaRecorder = new AndroidMediaRecorder(context, "audio/mpeg");
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		androidMediaRecorder.fileName = "fileName";
		mediaRecorder.setOnErrorListener(androidMediaRecorder);
		mediaRecorder.setOnInfoListener(androidMediaRecorder);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile("fileName");
		mediaRecorder.prepare();
		RecorderTask task = PowerMock.createMock(RecorderTask.class);
		androidMediaRecorder.recorderTask = task;		
		PowerMock.replayAll();
		
		androidMediaRecorder.prefetch();
		
		PowerMock.verifyAll();
		
		/**********************************************************/
		PowerMock.resetAll();		
		PowerMock.expectNew(MediaRecorder.class).andReturn(mediaRecorder);	
		PowerMock.replayAll();
		androidMediaRecorder = new AndroidMediaRecorder(context, "audio/3gpp");
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		androidMediaRecorder.fileName = "fileName";
		mediaRecorder.setOnErrorListener(androidMediaRecorder);
		mediaRecorder.setOnInfoListener(androidMediaRecorder);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile("fileName");
		mediaRecorder.prepare();
		task = PowerMock.createMock(RecorderTask.class);
		androidMediaRecorder.recorderTask = task;		
		PowerMock.replayAll();
		
		androidMediaRecorder.prefetch();
		
		PowerMock.verifyAll();
		
		/*******************************************************/
		PowerMock.resetAll();		
		PowerMock.expectNew(MediaRecorder.class).andReturn(mediaRecorder);	
		PowerMock.replayAll();
		androidMediaRecorder = new AndroidMediaRecorder(context, "audio/amr");
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		androidMediaRecorder.fileName = "fileName";
		mediaRecorder.setOnErrorListener(androidMediaRecorder);
		mediaRecorder.setOnInfoListener(androidMediaRecorder);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile("fileName");
		mediaRecorder.prepare();
		task = PowerMock.createMock(RecorderTask.class);
		androidMediaRecorder.recorderTask = task;		
		PowerMock.replayAll();
		
		androidMediaRecorder.prefetch();
		
		PowerMock.verifyAll();
	}
	
	
	@Test(expected=TnMediaException.class)
	public void testPrefetch_Exception() throws Exception
	{
		PowerMock.resetAll();
		MediaRecorder recorder = PowerMock.createMock(MediaRecorder.class);
		androidMediaRecorder.mediaRecorder = recorder;
		mediaRecorder.setOnErrorListener(androidMediaRecorder);
		mediaRecorder.setOnInfoListener(androidMediaRecorder);
		IllegalStateException e = new IllegalStateException();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		EasyMock.expectLastCall().andThrow(e);	
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IllegalStateException.class));
		PowerMock.replayAll();		
		androidMediaRecorder.prefetch();	
		PowerMock.verifyAll();
	}
	
//	@Test
//	public void testRealize() throws Exception
//	{
//		PowerMock.resetAll();
//		
//		FileOutputStream fileStream = PowerMock.createMock(FileOutputStream.class);
//		File file = PowerMock.createMock(File.class);
//		File file2 = PowerMock.createMock(File.class);
//		RandomAccessFile randomFile = PowerMock.createMock(RandomAccessFile.class);
//		
//		EasyMock.expect(context.deleteFile("temp_media" + ".recorder")).andReturn(true);
//		EasyMock.expect(context.openFileOutput("temp_media.recorder", Context.MODE_WORLD_READABLE)).andReturn(fileStream);
//		EasyMock.expect(context.getFilesDir()).andReturn(file);
//		EasyMock.expect(file.getAbsolutePath()).andReturn("path");
//		fileStream.close();
//		PowerMock.expectNew(File.class, "path/temp_media.recorder").andReturn(file2);
//		PowerMock.expectNew(RandomAccessFile.class, file2, "rw").andReturn(randomFile);
//		
//		PowerMock.replayAll();
//		androidMediaRecorder.realize();
//		PowerMock.verifyAll();
//	}
	
//	@Test(expected=TnMediaException.class)
//	public void testRealize_Exception() throws Exception
//	{
//		PowerMock.resetAll();	
//		EasyMock.expect(context.deleteFile("temp_media" + ".recorder")).andReturn(true);
//		FileNotFoundException e = new FileNotFoundException(); 
//		EasyMock.expect(context.openFileOutput("temp_media.recorder", Context.MODE_WORLD_READABLE)).andThrow(e);	
//		PowerMock.mockStatic(Logger.class);
//		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(FileNotFoundException.class));
//		PowerMock.replayAll();
//		androidMediaRecorder.realize();
//		PowerMock.verifyAll();
//	}
	
	@Test
	public void reset() throws Exception
	{
		PowerMock.resetAll();
		mediaRecorder.reset();
		PowerMock.replayAll();
		androidMediaRecorder.reset();
		PowerMock.verifyAll();	
	}
	
	@Test(expected=TnMediaException.class)
	public void reset_Exceptoin() throws Exception
	{
		PowerMock.resetAll();
		RuntimeException e = new RuntimeException();
		mediaRecorder.reset();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
		PowerMock.replayAll();
		androidMediaRecorder.reset();
		PowerMock.verifyAll();	
	}
	
	@Test
	public void start() throws Exception
	{
		PowerMock.resetAll();
		
		RecorderTask task = PowerMock.createMock(RecorderTask.class);
		androidMediaRecorder.recorderTask = task;
		
		//Runnable run = new Runnable(){ public void run(){}};
		Thread theThread = PowerMock.createMock(Thread.class, "test");
		mediaRecorder.start();
		PowerMock.expectNew(Thread.class, (Runnable)task).andReturn(theThread);
		//theThread.start();
		PowerMock.replayAll();
		androidMediaRecorder.start();
		PowerMock.verifyAll();
	}
	
	@Test(expected=TnMediaException.class)
	public void start_Exception() throws Exception
	{
		PowerMock.resetAll();
		
		IllegalStateException e = new IllegalStateException();
		mediaRecorder.start();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
		
		PowerMock.replayAll();
		androidMediaRecorder.start();
		PowerMock.verifyAll();
	}
	
//	@Test
//	public void testStop() throws Exception
//	{
//		PowerMock.resetAll();
//		
//		mediaRecorder.stop();
//		PowerMock.replayAll();
//		androidMediaRecorder.stop();
//		PowerMock.verifyAll();
//	}

	@Test(expected=TnMediaException.class)
	public void testStop_Exception() throws Exception
	{
		PowerMock.resetAll();
		
		IllegalStateException e = new IllegalStateException();
		mediaRecorder.stop();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(RuntimeException.class));
		
		PowerMock.replayAll();
		androidMediaRecorder.stop();
		PowerMock.verifyAll();
	}
	
	@Test
	public void testCommit() throws Exception
	{
		PowerMock.resetAll();		
		
		ByteArrayOutputStream stream = PowerMock.createMock(ByteArrayOutputStream.class);
		
		PowerMock.expectNew(MediaRecorder.class).andReturn(mediaRecorder);	
		PowerMock.expectNew(ByteArrayOutputStream.class).andReturn(stream);
		PowerMock.replayAll();
		androidMediaRecorder = new AndroidMediaRecorder(context, "audio/mpeg");
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		stream.flush();
		PowerMock.replayAll();
		androidMediaRecorder.commit();
		PowerMock.verifyAll();	
	}
	
	@Test(expected=TnMediaException.class)
	public void testCommit_Exception() throws Exception
	{
		PowerMock.resetAll();		
		
		ByteArrayOutputStream stream = PowerMock.createMock(ByteArrayOutputStream.class);
		
		PowerMock.expectNew(MediaRecorder.class).andReturn(mediaRecorder);	
		PowerMock.expectNew(ByteArrayOutputStream.class).andReturn(stream);
		PowerMock.replayAll();
		androidMediaRecorder = new AndroidMediaRecorder(context, "audio/mpeg");
		PowerMock.verifyAll();
		
		PowerMock.resetAll();
		IOException e = new IOException();
		stream.flush();
		EasyMock.expectLastCall().andThrow(e);
		PowerMock.mockStatic(Logger.class);
		Logger.log(EasyMock.anyObject(String.class), EasyMock.anyObject(IOException.class));
		
		PowerMock.replayAll();
		androidMediaRecorder.commit();
		PowerMock.verifyAll();	
	}
	
	@Test
	public void testOnError()
	{
		PowerMock.resetAll();
		
		ITnMediaListener listener = PowerMock.createMock(ITnMediaListener.class);
		androidMediaRecorder.setListener(listener);
		listener.mediaUpdate(androidMediaRecorder, ITnMediaListener.ON_ERROR, "1,2");
		PowerMock.replayAll();
		androidMediaRecorder.onError(null, 1, 2);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testOnInfo()
	{
		PowerMock.resetAll();
		
		ITnMediaListener listener = PowerMock.createMock(ITnMediaListener.class);
		androidMediaRecorder.setListener(listener);
		listener.mediaUpdate(androidMediaRecorder, ITnMediaListener.ON_INFO, "1,2");
		PowerMock.replayAll();
		androidMediaRecorder.onInfo(null, 1, 2);
		PowerMock.verifyAll();
	}
	
	@Test public void testRecorderTask() throws Exception
	{
		PowerMock.resetAll();		
		
		ByteArrayOutputStream stream = PowerMock.createMock(ByteArrayOutputStream.class);
		
		PowerMock.expectNew(MediaRecorder.class).andReturn(mediaRecorder);	
		PowerMock.expectNew(ByteArrayOutputStream.class).andReturn(stream);
		PowerMock.replayAll();
		androidMediaRecorder = new AndroidMediaRecorder(context, "audio/mpeg");
		PowerMock.verifyAll();

		PowerMock.resetAll();
		androidMediaRecorder.fileName = "fileName";
		mediaRecorder.setOnErrorListener(androidMediaRecorder);
		mediaRecorder.setOnInfoListener(androidMediaRecorder);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile("fileName");
		mediaRecorder.prepare();	
		PowerMock.replayAll();
		androidMediaRecorder.prefetch();	
		PowerMock.verifyAll();
			
		ITnMediaListener listener = PowerMock.createMock(ITnMediaListener.class);
		androidMediaRecorder.setListener(listener);
		RandomAccessFile file = PowerMock.createMock(RandomAccessFile.class);
		androidMediaRecorder.mediaTempFile = file;
		
		
		PowerMock.resetAll();
		
		listener.mediaUpdate(androidMediaRecorder, ITnMediaListener.ON_PREPARE, null);
		EasyMock.expect(file.length()).andReturn(50L);
		EasyMock.expect(file.getFilePointer()).andReturn(30L);
		EasyMock.expect(file.read(EasyMock.anyObject(byte[].class))).andReturn(20);
		IndexOutOfBoundsException e = new IndexOutOfBoundsException();
		stream.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		EasyMock.expectLastCall().andThrow(e);
		listener.mediaUpdate(androidMediaRecorder, ITnMediaListener.ON_COMPLETION, null);
		listener.mediaUpdate(androidMediaRecorder, ITnMediaListener.ON_CLOSE, null);
		
		PowerMock.replayAll();
		androidMediaRecorder.recorderTask.run();	
		PowerMock.verifyAll();
		
	}
	
}
