package com.telenav.datatypes.audio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 *@author gbwang
 *@date 2011-7-12
 */

public class AbstractRuleTest extends TestCase
{
	AbstractRule abstractRule;
	InputStream is;
	byte[] buf;
	AudioData audioData;
	AudioData audioDataChild;

	protected void setUp() throws Exception
	{
		createBuf();
		is = new ByteArrayInputStream(buf);
		audioData = new AudioData(100);	
		audioDataChild = new AudioData(200);	

		abstractRule = new AbstractRule(is)
		{
			public AudioDataNode createAudioNode(Hashtable hashtable)
			{
				AudioDataNode audioDataNode = new AudioDataNode(audioData);
				AudioDataNode audioDataNodeChild = new AudioDataNode(audioDataChild);
				audioDataNode.addChild(audioDataNodeChild);
				
				return audioDataNode;
			}			
		};
		super.setUp();
	}
	
	public void createBuf()
	{
		buf = new byte[40];
		buf[0] = 0x00;
		buf[1] = 0x00;
		buf[2] = 0x00;
		buf[3] = 0x08;
		
		buf[4] = 0x00;
		buf[5] = 0x00;
		buf[6] = 0x00;
		buf[7] = 0x03;
//		rule[0]	-- ID_FIELD	
		buf[8] = 0x00;
		buf[9] = 0x00;
		buf[10] = 0x00;
		buf[11] = 0x00;		
//		rule[1]	-- VALUE_FIELD
		buf[12] = 0x00;
		buf[13] = 0x00;
		buf[14] = 0x00;
		buf[15] = 0x0A;
//		rule[2]	-- NUM_CHILDREN_FIELD
		buf[16] = 0x00;
		buf[17] = 0x00;
		buf[18] = 0x00;
		buf[19] = 0x01;	
//		rule[3]	-- CHILDREN_FIELD
		buf[20] = 0x00;
		buf[21] = 0x00;
		buf[22] = 0x00;
		buf[23] = 0x04;			
//		rule[4]	-- ID_FIELD
		buf[24] = 0x00;
		buf[25] = 0x00;
		buf[26] = 0x00;
		buf[27] = 0x0F;
//		rule[5]	-- VALUE_FIELD
		buf[28] = 0x00;
		buf[29] = 0x00;
		buf[30] = 0x00;
		buf[31] = 0x00;	
//		rule[6]	-- NUM_CHILDREN_FIELD
		buf[32] = 0x00;
		buf[33] = 0x00;
		buf[34] = 0x00;
		buf[35] = 0x00;			
//		rule[7]	-- CHILDREN_FIELD
		buf[36] = 0x00;
		buf[37] = 0x00;
		buf[38] = 0x00;
		buf[39] = 0x00;		
	}
	
	public void testAbstractRule()
	{
		Assert.assertNotNull(abstractRule);
		
		RuleNode ruleNode = abstractRule.ruleNode;
		Assert.assertNotNull(ruleNode);
		
		int[] expect = new int[8];
		expect[0] = 0x00;
		expect[1] = 0x0A;
		expect[2] = 0x01;
		expect[3] = 0x04;
		
		expect[4] = 0x0F;
		expect[5] = 0x00;
		expect[6] = 0x00;
		expect[7] = 0x00;
		
		Assert.assertArrayEquals(expect, ruleNode.rule);
		Assert.assertEquals(3, ruleNode.varCount);
	}
	
	public void testFlattenSequence()
	{
		Hashtable hashtable = new Hashtable();
		AudioDataNode audioDataNode = abstractRule.createAudioNode(hashtable);
		AudioData[] seq = abstractRule.flattenSequence(audioDataNode);
		Assert.assertEquals(2, seq.length);
		Assert.assertEquals(100, seq[0].resourceId);
		Assert.assertEquals(200, seq[1].resourceId);
	}
	
	public void testGetSequenceSize()
	{
		Hashtable hashtable = new Hashtable();
		AudioDataNode audioDataNode = abstractRule.createAudioNode(hashtable);
		int size = AbstractRule.getSequenceSize(audioDataNode, 0);
		Assert.assertEquals(2, size);
	}
	
	public void testCreateAudioData()
	{
		Hashtable hashtable = new Hashtable();
		AudioData[] playList = abstractRule.createAudioData(hashtable);
		Assert.assertEquals(2, playList.length);
		Assert.assertEquals(100, playList[0].resourceId);
		Assert.assertEquals(200, playList[1].resourceId);		
	}
	
	public void testGetVersion()
	{
		abstractRule.version = 1;
		Assert.assertEquals(1, abstractRule.getVersion());
	}
	
	public void testCreateAudioNode()
	{
		Hashtable hashtable = new Hashtable();
		AudioDataNode audioDataNode = abstractRule.createAudioNode(hashtable);		
		Assert.assertNotNull(audioDataNode);

		Assert.assertEquals(100, audioDataNode.getAudioData().resourceId);
		Assert.assertEquals(1, audioDataNode.getChildrenSize());	
		Assert.assertEquals(200, audioDataNode.getChild(0).getAudioData().resourceId);	
	}
	
	public void testIntWrapper()
	{
		int i = 1;
		AbstractRule.IntWrapper intWrapper = new AbstractRule.IntWrapper(i);
		Assert.assertEquals(1, intWrapper.intValue);
	}

}
