package com.telenav.datatypes.audio;

import org.junit.Assert;

import com.telenav.datatypes.audio.AudioData.IAudioDataProvider;

import junit.framework.TestCase;

/**
 *@author gbwang
 *@date 2011-7-5
 */
public class AudioDataTest extends TestCase
{
	AudioData testAudioData;
	
	protected void setUp() throws Exception
	{
		testAudioData = new AudioData(100);
		super.setUp();
	}
	
	public void testAudioData_A()
	{
		int resourceId = 100;
		AudioData audioData = new AudioData(resourceId);
		Assert.assertEquals(resourceId, audioData.resourceId);	
	}
	
	public void testAudioData_B()
	{
		byte[] data = "100".getBytes();
		AudioData audioData = new AudioData(data);
		Assert.assertArrayEquals(data, audioData.audioData);		
	}
	
	public void testAudioData_C()
	{
		String resourceUri = "/telenav/tn701";
		AudioData audioData = new AudioData(resourceUri);
		assertEquals(resourceUri, audioData.resourceUri);
	}
	
	public void testSetAudio_A()
	{
		byte[] data = "100".getBytes();
		testAudioData.setAudio(data);
		assertEquals(data, testAudioData.getData());
	}
	
	public void testSetAudio_B()
	{
		int resourceId = 100;
		testAudioData.setAudio(resourceId);
		assertEquals(resourceId, testAudioData.getResourceId());
	}
	
	public void testSetAudio_C()
	{
		String resourceUri = "/telenav/tn701";
		testAudioData.setAudio(resourceUri);
		assertEquals(resourceUri, testAudioData.getResourceUri());
	}	

	public void testCategory()
	{
		String category = "Nav";		
		testAudioData.setCategory(category);
		assertEquals(category, testAudioData.getCategory());
	}	
	
	public void testDataProvider()
	{
		IAudioDataProvider audioDataProvider = new IAudioDataProvider()
		{
			public byte[] getData(AudioData clipData) throws Throwable
			{
				return clipData.getData();
			}
			
		};
		testAudioData.setDataProvider(audioDataProvider);
		assertEquals(audioDataProvider, testAudioData.getDataProvider());
	}
	
	public void testGetData()
	{
		byte[] data = null;
		testAudioData.setAudio(data);
		
		IAudioDataProvider audioDataProvider = new IAudioDataProvider()
		{
			public byte[] getData(AudioData clipData) throws Throwable
			{
				return clipData.getData();
			}
			
		};
		testAudioData.setDataProvider(audioDataProvider);
		
		assertEquals(null, testAudioData.getData());
	}
	
}
