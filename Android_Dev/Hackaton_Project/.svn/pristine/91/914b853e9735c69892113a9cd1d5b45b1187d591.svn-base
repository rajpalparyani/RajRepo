package com.telenav.datatypes.audio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.datatypes.audio.RuleNode.EvalResult;

/**
 *@author gbwang
 *@date 2011-7-12
 */

public class RuleNodeTest extends TestCase
{
	InputStream is;
	byte[] buf;
	RuleNode ruleNode;	
	int INT_NULL = Integer.MIN_VALUE ;
    int JJTROOT = 0;
    int JJTIFBLOCK = 1;
    int JJTVOID = 2;
    int JJTSTATEMENTS = 3;
    int JJTAND = 4;
    int JJTOR = 5;
    int JJTNOT = 6;
    int JJTGT = 7;
    int JJTLT = 8;
    int JJTEQ = 9;
    int JJTNE = 10;
    int JJTGTEQ = 11;
    int JJTLTEQ = 12;
    int JJTDEFINED = 13;
    int JJTRETURNTERMS = 14;
    int JJTADDTERMS = 15;
    int JJTADD = 16;
    int JJTSUBTRACT = 17;
    int JJTMULT = 18;
    int JJTDIV = 19;
    int JJTMOD = 20;
    int JJTASSIGNMENT = 21;
    int JJTNODEARGINDEX = 22;
    int JJTINTARGINDEX = 23;
    int JJTINTLITERAL = 24;
    int JJTFINALIDENTIFIER = 25;
    int JJTIDENTIFIER = 26;
	
	protected void setUp() throws Exception
	{
		createBuf();
		is = new ByteArrayInputStream(buf);
		ruleNode = new RuleNode(is);	
		
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
		buf[27] = 0x18;
//		rule[5]	-- VALUE_FIELD
		buf[28] = 0x00;
		buf[29] = 0x00;
		buf[30] = 0x00;
		buf[31] = 0x01;	
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
	
	public void resetRuleNode(int i, int id)
	{
		buf[i] = (byte) id;
		is = new ByteArrayInputStream(buf);
		try
		{
			ruleNode = new RuleNode(is);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void testRuleNode()
	{
		try
		{
			int[] expect = new int[8];
			expect[0] = buf[11];
			expect[1] = buf[15];
			expect[2] = buf[19];
			expect[3] = buf[23];
			
			expect[4] = buf[27];
			expect[5] = buf[31];
			expect[6] = buf[35];
			expect[7] = buf[39];
			Assert.assertArrayEquals(expect, ruleNode.rule);
			Assert.assertEquals(buf[7], ruleNode.varCount);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void testEval_A()
	{
		resetRuleNode(11, JJTROOT);		
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);

		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testEval_B()
	{
		resetRuleNode(11, JJTADDTERMS);
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		AudioDataNode child = (AudioDataNode)(terms.children.elementAt(0));
		Assert.assertEquals(1, terms.children.size());
		Assert.assertEquals(buf[31], child.audioData.resourceId);
	}
	
	public void testEval_C()
	{
		resetRuleNode(11, JJTRETURNTERMS);
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		AudioDataNode child = (AudioDataNode)(terms.children.elementAt(0));
		Assert.assertEquals(1, terms.children.size());
		Assert.assertEquals(buf[31], child.audioData.resourceId);
	}
		
	public void testTraverse_JJTROOT()
	{
		resetRuleNode(11, JJTROOT);
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTIFBLOCK()
	{
		resetRuleNode(11, JJTIFBLOCK);
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTSTATEMENTS()
	{
		resetRuleNode(11, JJTSTATEMENTS);
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTNOT()
	{
		resetRuleNode(11, JJTNOT);
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
		
	public void testTraverse_JJTINTARGINDEX()
	{
		resetRuleNode(11, JJTSTATEMENTS);
		resetRuleNode(27, JJTINTARGINDEX);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTNODEARGINDEX()
	{
		resetRuleNode(11, JJTSTATEMENTS);
		resetRuleNode(27, JJTNODEARGINDEX);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void xtestTraverse_JJTIDENTIFIER()
	{
		resetRuleNode(11, JJTSTATEMENTS);
		resetRuleNode(27, JJTIDENTIFIER);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTAND()
	{
		resetRuleNode(11, JJTAND);
		resetRuleNode(27, JJTINTLITERAL);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}

	public void testTraverse_JJTOR()
	{
		resetRuleNode(11, JJTOR);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTGT()
	{
		resetRuleNode(11, JJTGT);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTLT()
	{
		resetRuleNode(11, JJTLT);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTEQ()
	{
		resetRuleNode(11, JJTEQ);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTNE()
	{
		resetRuleNode(11, JJTNE);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTGTEQ()
	{
		resetRuleNode(11, JJTGTEQ);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTLTEQ()
	{
		resetRuleNode(11, JJTLTEQ);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}	
	
	public void testTraverse_JJTDEFINED_A()
	{
		resetRuleNode(11, JJTDEFINED);
		resetRuleNode(27, JJTINTARGINDEX);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTDEFINED_B()
	{
		resetRuleNode(11, JJTDEFINED);
		resetRuleNode(27, JJTNODEARGINDEX);
		resetRuleNode(23, 0x04);
		resetRuleNode(31, 0x01);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	
	public void testTraverse_JJTASSIGNMENT()
	{
		resetRuleNode(11, JJTASSIGNMENT);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		expect[0] = 0;
		for (int i = 1;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	public void testTraverse_JJTADD()
	{
		resetRuleNode(11, JJTADD);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	public void testTraverse_JJTSUBTRACT()
	{
		resetRuleNode(11, JJTSUBTRACT);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	public void testTraverse_JJTMULT()
	{
		resetRuleNode(11, JJTMULT);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x00);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	public void testTraverse_JJTDIV()
	{
		resetRuleNode(11, JJTDIV);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x01);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);
		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
	public void testTraverse_JJTMOD()
	{
		resetRuleNode(11, JJTMOD);
		resetRuleNode(23, 0x05);
		resetRuleNode(27, 0x05);
		resetRuleNode(31, JJTINTLITERAL);
		resetRuleNode(35, 0x01);
		
		int[] intArgs = new int[2];
		intArgs[0] = 0x00;
		intArgs[1] = 0x01;
		AudioDataNode[] nodeArgs = new AudioDataNode[2];
		nodeArgs[0] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(1));
		nodeArgs[1] = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(2));
		
		AudioDataNode terms = ruleNode.eval(intArgs, nodeArgs);

		Assert.assertNull(terms.audioData);
		Assert.assertNull(terms.children);	
		int count = buf[7];
		int[] expect = new int[count] ;
		for (int i = 0;  i< expect.length; i++)
			expect[i] = INT_NULL ;
		Assert.assertArrayEquals(expect, ruleNode.variables);	
	}
}
