package com.telenav.data.serverproxy.impl.txnode;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 *@author gbwang
 *@date 2011-7-6
 */

public class NodeTest extends TestCase
{	
	Node node;
	byte[] binary = {48, -99, -101, 58, 0, 0, 0, 44, 57, 1, 0, 0, 0, 0, 33, 1, 48, -99, -101, 58, 6, 12, 0, 2, 2, 4, 6, 14, 12, 26, 82, 111, 117, 116, 101, 32, 83, 101, 116, 116, 105, 110, 103, 0, 14, 70, 97, 115, 116, 101, 115, 116, 16, 83, 104, 111, 114, 116, 101, 115, 116, 26, 65, 118, 111, 105, 100, 32, 72, 105, 103, 104, 119, 97, 121, 20, 80, 101, 100, 101, 115, 116, 114, 105, 97, 110, 0, 0, 92, 0, 0, 0, 0, 82, 48, -99, -101, 58, 4, 10, -99, 1, 0, 0, 2, 8, 22, 76, 97, 110, 101, 32, 65, 115, 115, 105, 115, 116, 8, 83, 104, 111, 119, 8, 83, 104, 111, 119, 8, 72, 105, 100, 101, 0, 0, 1, 1, 0, 0, 0, 0, 118, 48, -99, -101, 58, 4, 8, 2, 2, 0, 2, 8, 28, 68, 105, 115, 116, 97, 110, 99, 101, 32, 85, 110, 105, 116, 115, 18, 75, 109, 47, 77, 101, 116, 101, 114, 115, 18, 75, 109, 47, 77, 101, 116, 101, 114, 115, 20, 77, 105, 108, 101, 115, 47, 70, 101, 101, 116, 0, 0, 88, 0, 0, 0, 0, 78, 48, -99, -101, 58, 4, 10, -111, 1, 2, 0, 2, 8, 28, 84, 114, 97, 102, 102, 105, 99, 32, 67, 97, 109, 101, 114, 97, 4, 79, 110, 4, 79, 110, 6, 79, 102, 102, 0, 0, 118, 0, 0, 0, 0, 108, 48, -99, -101, 58, 4, 8, 4, 4, 4, 2, 8, 18, 77, 97, 112, 32, 83, 116, 121, 108, 101, 0, 28, 51, 68, 32, 77, 111, 118, 105, 110, 103, 32, 77, 97, 112, 115, 28, 50, 68, 32, 77, 111, 118, 105, 110, 103, 32, 77, 97, 112, 115, 0, 0, -127, 1, 0, 0, 0, 0, 105, 1, 48, -99, -101, 58, 6, 12, 6, 6, 6, 4, 2, 0, 12, 28, 65, 117, 100, 105, 111, 32, 71, 117, 105, 100, 97, 110, 99, 101, 8, 78, 111, 110, 101, 40, 68, 105, 114, 101, 99, 116, 105, 111, 110, 115, 32, 38, 32, 84, 114, 97, 102, 102, 105, 99, 30, 68, 105, 114, 101, 99, 116, 105, 111, 110, 115, 32, 79, 110, 108, 121, 24, 84, 114, 97, 102, 102, 105, 99, 32, 79, 110, 108, 121, 8, 78, 111, 110, 101, 0, 0, 80, 0, 0, 0, 0, 70, 48, -99, -101, 58, 4, 10, -107, 1, 0, 0, 2, 8, 20, 83, 112, 101, 101, 100, 32, 84, 114, 97, 112, 4, 79, 110, 4, 79, 110, 6, 79, 102, 102, 0, 0, 102, 0, 0, 0, 0, 92, 48, -99, -101, 58, 4, 8, 14, 0, 0, 2, 8, 44, 84, 114, 97, 102, 102, 105, 99, 32, 73, 110, 99, 105, 100, 101, 110, 116, 32, 65, 108, 101, 114, 116, 4, 79, 110, 4, 79, 110, 6, 79, 102, 102, 0, 0, 126, 0, 0, 0, 0, 116, 48, -99, -101, 58, 4, 8, 18, 0, 0, 2, 8, 16, 76, 97, 110, 103, 117, 97, 103, 101, 22, 69, 110, 103, 108, 105, 115, 104, 40, 85, 83, 41, 22, 69, 110, 103, 108, 105, 115, 104, 40, 85, 83, 41, 22, 83, 112, 97, 110, 105, 115, 104, 40, 77, 88, 41, 0, 0, 104, 0, 0, 0, 0, 94, 48, -99, -101, 58, 3, 6, 20, 0, 0, 6, 12, 82, 101, 103, 105, 111, 110, 26, 78, 111, 114, 116, 104, 32, 65, 109, 101, 114, 105, 99, 97, 26, 78, 111, 114, 116, 104, 32, 65, 109, 101, 114, 105, 99, 97, 0, 0, 116, 0, 0, 0, 0, 106, 48, -99, -101, 58, 4, 8, 24, 2, 2, 0, 8, 20, 71, 80, 83, 32, 83, 111, 117, 114, 99, 101, 18, 66, 108, 117, 101, 116, 111, 111, 116, 104, 16, 73, 110, 116, 101, 114, 110, 97, 108, 18, 66, 108, 117, 101, 116, 111, 111, 116, 104, 0, 0, 53, 1, 0, 0, 0, 0, 29, 1, 48, -99, -101, 58, 5, 10, 26, 0, 0, 4, 2, 10, 18, 66, 97, 99, 107, 76, 105, 103, 104, 116, 18, 65, 108, 119, 97, 121, 115, 32, 79, 110, 18, 65, 108, 119, 97, 121, 115, 32, 79, 110, 22, 79, 110, 32, 97, 116, 32, 84, 117, 114, 110, 115, 28, 68, 101, 118, 105, 99, 101, 32, 68, 101, 102, 97, 117, 108, 116, 0, 0, 92, 0, 0, 0, 0, 82, 48, -99, -101, 58, 4, 10, -95, 1, 0, 0, 2, 8, 22, 83, 112, 101, 101, 100, 32, 76, 105, 109, 105, 116, 8, 83, 104, 111, 119, 8, 83, 104, 111, 119, 8, 72, 105, 100, 101, 0, 0, 97, 1, 0, 0, 0, 0, 73, 1, 48, -99, -101, 58, 5, 10, 30, 10, 8, 4, 0, 10, 10, 65, 118, 111, 105, 100, 34, 85, 115, 101, 32, 67, 97, 114, 112, 111, 111, 108, 32, 76, 97, 110, 101, 115, 26, 65, 118, 111, 105, 100, 32, 84, 114, 97, 102, 102, 105, 99, 22, 65, 118, 111, 105, 100, 32, 84, 111, 108, 108, 115, 34, 85, 115, 101, 32, 67, 97, 114, 112, 111, 111, 108, 32, 76, 97, 110, 101, 115, 0, 0, 114, 0, 0, 0, 0, 104, 48, -99, -101, 58, 4, 8, 36, 0, 0, 2, 8, 34, 65, 117, 100, 105, 111, 32, 68, 117, 114, 105, 110, 103, 32, 67, 97, 108, 108, 14, 83, 117, 115, 112, 101, 110, 100, 14, 83, 117, 115, 112, 101, 110, 100, 8, 80, 108, 97, 121, 0, 0, 106, 0, 0, 0, 0, 96, 48, -99, -101, 58, 5, 10, 46, 6, 6, 2, 4, 10, 18, 77, 97, 112, 32, 67, 111, 108, 111, 114, 0, 8, 65, 117, 116, 111, 14, 68, 97, 121, 116, 105, 109, 101, 18, 78, 105, 103, 104, 116, 116, 105, 109, 101, 0, 0, 44, 0, 0, 0, 0, 34, 48, -99, -101, 58, 2, 4, 50, 0, 4, 8, 78, 97, 109, 101, 0, 0, 0, 46, 0, 0, 0, 0, 36, 48, -99, -101, 58, 2, 4, 48, 0, 4, 10, 69, 109, 97, 105, 108, 0, 0, 0, 66, 0, 0, 0, 0, 56, 48, -99, -101, 58, 2, 4, 54, 0, 4, 10, 80, 104, 111, 110, 101, 20, 52, 48, 56, 51, 54, 56, 55, 53, 52, 49, 0, 0, 54, 0, 0, 0, 0, 44, 48, -99, -101, 58, 2, 4, 52, 0, 4, 18, 76, 97, 115, 116, 32, 78, 97, 109, 101, 0, 0, 0, 124, 0, 0, 0, 0, 114, 48, -99, -101, 58, 5, 10, 56, 0, 0, 2, 4, 10, 30, 67, 111, 110, 118, 101, 110, 105, 101, 110, 99, 101, 32, 75, 101, 121, 8, 76, 101, 102, 116, 8, 76, 101, 102, 116, 10, 82, 105, 103, 104, 116, 20, 68, 111, 32, 110, 111, 116, 32, 117, 115, 101, 0, 0, 54, 0, 0, 0, 0, 44, 48, -99, -101, 58, 2, 6, -31, 1, 0, 4, 16, 85, 115, 101, 114, 110, 97, 109, 101, 0, 0, 0, 0};
	int startIndex = 0;
	
	protected void setUp() throws Exception
	{
		node = new Node(binary, 0);
		super.setUp();
	}
	
	public void testGetVersion()
	{
		int version = Node.VERSION_55;
		node.setVersion(version);
		Assert.assertEquals(version, node.getVersion());
	}
	
	public void testNode()
	{
		Assert.assertFalse(node.isEmpty());
		Assert.assertEquals(0, node.getValuesSize());
		Assert.assertEquals(0, node.getStringsSize());
		Assert.assertNull(node.getParent());
	}
	
	public void testAddValue()
	{
		Node testNode = new Node(binary, 0);
		testNode.addValue(1000);
		Assert.assertEquals(1, testNode.nValues);
		Assert.assertEquals(2, testNode.valuesBuffOffset);
	}
	
	
	public void testGetValueAt()
	{
		Node testNode = new Node(binary, 0);
		testNode.setValueAt(10, 3);
		Assert.assertEquals(10, testNode.getValueAt(3));
	}
	
	public void testGetStringAt()
	{
		Node testNode = new Node(binary, 0);
		testNode.setStringAt("100", 3);
		
		Assert.assertEquals("100", testNode.getStringAt(3));
	}
	
	public void testRemoveAllStrings()
	{
		Node testNode = new Node(binary, 0);
		testNode.removeAllStrings();
		Assert.assertNull(testNode.msgs);
		Assert.assertNull(testNode.msgsBuff);
		Assert.assertEquals(0, testNode.nMsgs);
	}
	
	public void testGetBinaryData()
	{
		Node testNode = new Node();
		testNode.setBinaryData(binary);
		byte[] actual = testNode.getBinaryData();
		Assert.assertArrayEquals(binary, actual);
	}	
	
	public void testAddString()
	{
		Node testNode = new Node(binary, 0);
		testNode.addString("test");
		Assert.assertArrayEquals("test".getBytes(), testNode.msgsBuff[testNode.nMsgs -1]);
	}
	
	public void testAddChild()
	{
		Node testNode = new Node(binary, 0);
		Node childNode = new Node(binary, 0);
		int nChildren = testNode.nChildren;
		
		testNode.addChild(childNode);
		Node actual = testNode.children[nChildren].parent;
		
		Assert.assertEquals(nChildren + 1, testNode.nChildren);
		Assert.assertEquals(testNode, actual);
		
		testNode.removeChildAt(nChildren);
		Assert.assertEquals(nChildren, testNode.nChildren);
	}
	
	public void testInsertChildAt()
	{
		Node testNode = new Node(binary, 0);
		Node childNode = new Node(binary, 0);
		int nChildren = testNode.nChildren;
		testNode.insertChildAt(childNode, nChildren);
		
		Assert.assertEquals(testNode, testNode.children[nChildren].parent);
	}
	
	public void testGetChildAt()
	{
		Node testNode = new Node(binary, 0);
		Node childNode = new Node(binary, 0);
		byte[] childBuf = {48, -99, -101, 58, 4, 10, -99, 1, 0, 0, 2, 8, 22, 76, 97, 110, 101, 32, 65, 115, 115, 105, 115, 116, 8, 83, 104, 111, 119, 8, 83, 104, 111, 119, 8, 72, 105, 100, 101, 0, 0};
		                  
		testNode.setChildAt(childNode, 2);
		
		Assert.assertEquals(childNode, testNode.getChildAt(2));
		Assert.assertArrayEquals(childNode.toBinary(), testNode.getChildBinaryAt(2));
		Assert.assertArrayEquals(childBuf, node.getChildAt(1).binData);
	}

	public void testToString()
	{
		node.toString();
	}
}
