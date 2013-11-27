/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NodeTest.java
 *
 */

package com.telenav.searchwidget.data;

import junit.framework.TestCase;

/**
 * @author xinrongl
 * @date Oct 10, 2011
 */

public class NodeTest extends TestCase 
{
	public void testNode()
	{
		Node node = new Node();
		node.addValue(1);
		node.addValue(2);
		node.addString("string1");
		node.addString("string2");
		
		Node child1 = new Node();
		child1.addString("child1");
		node.addChild(child1);
		
		Node child2 = new Node();
		child2.addString("child2");
		node.addChild(child2);
		
		
		byte[] nodeBuf = node.toBinary();
		Node clone = new Node(nodeBuf, 0);
		
		assertEquals(2, clone.getValuesSize());
		assertEquals(2, clone.getStringsSize());
		assertEquals(2, clone.getChildrenSize());
		
		assertEquals(1, clone.getValueAt(0));
		assertEquals(2, clone.getValueAt(1));
		
		assertEquals("string1", clone.getStringAt(0));
		assertEquals("string2", clone.getStringAt(1));
		
		Node cloneChild = clone.getChildAt(0);
		assertEquals("child1", cloneChild.getStringAt(0));
		
		cloneChild = clone.getChildAt(1);
		assertEquals("child2", cloneChild.getStringAt(0));
	}
}

