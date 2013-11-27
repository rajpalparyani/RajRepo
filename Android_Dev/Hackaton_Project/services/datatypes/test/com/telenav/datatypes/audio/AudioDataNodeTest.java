package com.telenav.datatypes.audio;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *@author gbwang
 *@date 2011-7-5
 */
public class AudioDataNodeTest extends TestCase
{
	AudioData audioData;
	AudioDataNode audioDataNode;
	AudioData childAudioData;
	AudioDataNode childAudioDataNode;
	
	protected void setUp() throws Exception
	{
		audioData = new AudioData(100);
		audioDataNode = new AudioDataNode(audioData);
		childAudioData = new AudioData(200);
		childAudioDataNode = new AudioDataNode(childAudioData);
		audioDataNode.addChild(childAudioDataNode);
		super.setUp();
	}
	
	public void testAudioDataNode()
	{
		Assert.assertEquals(audioData, audioDataNode.getAudioData());
	}
	public void testAddChild()
	{
		Assert.assertEquals(childAudioDataNode, audioDataNode.children.elementAt(0));
	}
	public void testGetChild()
	{
		Assert.assertEquals(childAudioDataNode, audioDataNode.getChild(0));
	}
	public void testGetChildrenSize()
	{
		Assert.assertEquals(1, audioDataNode.getChildrenSize());
	}
}
