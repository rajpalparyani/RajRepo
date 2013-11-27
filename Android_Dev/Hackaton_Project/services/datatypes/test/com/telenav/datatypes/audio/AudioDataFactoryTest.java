package com.telenav.datatypes.audio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.junit.Assert;
import org.powermock.reflect.Whitebox;

import com.telenav.logger.Logger;

/**
 *@author gbwang
 *@date 2011-7-7
 */
public class AudioDataFactoryTest extends TestCase
{
	AudioDataFactory audioDataFactory;
	int initCount;
	
	protected void setUp() throws Exception
	{
		audioDataFactory = AudioDataFactory.getInstance();
		super.setUp();
	}
	
	public void util()
	{
		Field initCountField = Whitebox.getField(AudioDataFactory.class, "initCount");
		try
		{
			initCount = initCountField.getInt(audioDataFactory);
		} 
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	public void testGetInstance()
	{
		Assert.assertNotNull(audioDataFactory);
	}
	
	public void testInit()
	{
		AudioDataFactory.init(audioDataFactory);
		Assert.assertEquals(audioDataFactory, AudioDataFactory.instance);
		util();
		Assert.assertEquals(1, initCount);
	}
	
	public void testCreateAudioData_A()
	{
		int resourceId = 1;
		AudioData audioData = audioDataFactory.createAudioData(resourceId);
		Assert.assertNotNull(audioData);
		Assert.assertEquals(resourceId, audioData.resourceId);
	}
	
	public void testCreateAudioData_B()
	{
		byte[] data = "100".getBytes();
		AudioData audioData = audioDataFactory.createAudioData(data);
		Assert.assertNotNull(audioData);
		Assert.assertArrayEquals(data, audioData.getData());
	}
	
	public void testCreateAudioData_C()
	{
		String resourceUri = "/telenav/tn70";
		AudioData audioData = audioDataFactory.createAudioData(resourceUri);
		Assert.assertNotNull(audioData);
		Assert.assertEquals(resourceUri, audioData.resourceUri);
	}
	
	public void testCteateRuleNode()
	{
		InputStream is;
		byte[] buf = new byte[16];
		buf[0] = 0x00;
		buf[1] = 0x00;
		buf[2] = 0x00;
		buf[3] = 0x02;
		
		buf[4] = 0x00;
		buf[5] = 0x00;
		buf[6] = 0x00;
		buf[7] = 0x03;		
		
		buf[8] = 0x00;
		buf[9] = 0x00;
		buf[10] = 0x00;
		buf[11] = 0x06;
		
		buf[12] = 0x00;
		buf[13] = 0x00;
		buf[14] = 0x00;
		buf[15] = 0x07;
		try
		{
			is = new ByteArrayInputStream(buf);			
			RuleNode ruleNode = audioDataFactory.createRuleNode(is);
			Assert.assertNotNull(ruleNode);
			int[] expect = new int[2];
			expect[0] = 6;
			expect[1] = 7;
			Assert.assertArrayEquals(expect, ruleNode.rule);
			Assert.assertEquals(3, ruleNode.varCount);
			
		}  
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void testCreateAudioDataNode()
	{
		int resourceId = 2;
		AudioData audioData = new AudioData(resourceId);
		AudioDataNode audioDataNode = audioDataFactory.createAudioDataNode(audioData);
		Assert.assertNotNull(audioDataNode);
		Assert.assertEquals(resourceId, audioData.resourceId);
	}
	
}
