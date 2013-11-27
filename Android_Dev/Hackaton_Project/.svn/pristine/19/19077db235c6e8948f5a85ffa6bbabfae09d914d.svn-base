package com.telenav.media.android;

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.android.AndroidPcmMediaRecorder.RecorderTask;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidPcmMediaRecorder.class, Logger.class, Thread.class, AudioRecord.class})
public class AndroidPcmMediaRecorderTest 
{
	private AndroidPcmMediaRecorder pcmRecorder;
	
	@Before
	public void setUp() throws Exception
	{
		pcmRecorder = new AndroidPcmMediaRecorder(null, "mp3", 8000);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructor()
	{
		AndroidPcmMediaRecorder recorder = new AndroidPcmMediaRecorder(null, "test", 500);
	}
	
	@Test
	public void testCommit() throws Exception
	{
		pcmRecorder.commit();
	}

	@Test
	public void testSetRecordSizeLimitDelegate() throws Exception
	{
		pcmRecorder.setRecordSizeLimitDelegate();
	}

	@Test
	public void testClose() throws Exception
	{
		pcmRecorder.close();
	}
	
	@Test
	public void testPrefetch() throws Exception
	{
		pcmRecorder.prefetch();
	}
//	@Test
//	public void testRealize() throws Exception
//	{
//		pcmRecorder.realize();
//	}
	@Test
	public void testReset() throws Exception
	{
		pcmRecorder.reset();
	}
	
	
	@Test(expected=IllegalThreadStateException.class)
	public void testStart() throws Exception
	{
		PowerMock.resetAll();
		Thread thread = PowerMock.createMock(Thread.class, "test");
		RecorderTask task = PowerMock.createMock(RecorderTask.class);
		PowerMock.expectNew(RecorderTask.class).andReturn(task);
		PowerMock.expectNew(Thread.class, task).andReturn(thread);
		thread.start();
		PowerMock.replayAll();
		pcmRecorder.start();
	}
	
//	@Test
//	public void testStop() throws Exception
//	{
//		pcmRecorder.stop();
//	}
	
	@Test
	public void testOnError()
	{
		ITnMediaListener listener = PowerMock.createMock(ITnMediaListener.class);
		pcmRecorder.setListener(listener);
		assertEquals(pcmRecorder.getListener(), listener);
		listener.mediaUpdate(pcmRecorder, ITnMediaListener.ON_ERROR, "1,2");
		PowerMock.replayAll();
		pcmRecorder.onError(null, 1, 2);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testOnInfo()
	{
		PowerMock.resetAll();
		ITnMediaListener listener = PowerMock.createMock(ITnMediaListener.class);
		pcmRecorder.setListener(listener);
		listener.mediaUpdate(pcmRecorder, ITnMediaListener.ON_INFO, "1,2");
		PowerMock.replayAll();
		pcmRecorder.onInfo(null, 1, 2);
		PowerMock.verifyAll();
	}
	
//	@Test
//	public void testRecorderTask() throws Exception
//	{
//		PowerMock.resetAll();
//		
//		ByteArrayOutputStream os2 = PowerMock.createMock(ByteArrayOutputStream.class);	
//		PowerMock.expectNew(ByteArrayOutputStream.class).andReturn(os2);
//		PowerMock.replayAll();
//		pcmRecorder = new AndroidPcmMediaRecorder(null, "mp3", 8000);
//		PowerMock.verifyAll();
//		
//		ITnMediaListener listener = PowerMock.createMock(ITnMediaListener.class);
//		pcmRecorder.setListener(listener);
//		listener.mediaUpdate(pcmRecorder, ITnMediaListener.ON_PREPARE, null);
//		
//		PowerMock.mockStatic(AudioRecord.class);
//		EasyMock.expect(AudioRecord.getMinBufferSize(EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt())).andReturn(300);
//		
//		BufferedOutputStream bos = PowerMock.createMock(BufferedOutputStream.class);
//		DataOutputStream dos = PowerMock.createMock(DataOutputStream.class);
//		PowerMock.expectNew(BufferedOutputStream.class, EasyMock.anyObject(OutputStream.class), EasyMock.anyInt()).andReturn(bos);
//		PowerMock.expectNew(DataOutputStream.class, bos).andReturn(dos);
//		
//		AudioRecord record = PowerMock.createMock(AudioRecord.class);
//		PowerMock.expectNew(AudioRecord.class, EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt()).andReturn(record);
//		record.startRecording();
//		IOException e = new IOException();
//		EasyMock.expect(record.read(EasyMock.anyObject(short[].class), EasyMock.anyInt(), EasyMock.anyInt())).andReturn(60);
//		dos.write(EasyMock.anyInt());
//		dos.write(EasyMock.anyInt());
//		EasyMock.expectLastCall().andThrow(e);
//		
//		record.stop();
//		dos.close();
//		bos.close();	
//		EasyMock.expectLastCall().andThrow(e);
//		
//		listener.mediaUpdate(pcmRecorder, ITnMediaListener.ON_COMPLETION, null);
//		listener.mediaUpdate(pcmRecorder, ITnMediaListener.ON_CLOSE, null);
//		
//		RecorderTask task = pcmRecorder.new RecorderTask();	
//		
//		PowerMock.replayAll();
//		task.run();
//		PowerMock.verifyAll();
//		
//	}
}
