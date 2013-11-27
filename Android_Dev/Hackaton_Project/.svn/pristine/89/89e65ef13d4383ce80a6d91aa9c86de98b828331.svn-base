/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TxNodePrimitiveSerializableTest.java
 *
 */
package com.telenav.data.serializable.txnode;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.powermock.api.easymock.PowerMock;

import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.persistent.TnPersistentManager;

import junit.framework.TestCase;

/**
 *@author jxue
 *@date 2011-6-28
 */
public class TxNodePrimitiveSerializableTest extends TestCase
{
    TxNodePrimitiveSerializable txNodePrimitiveSerializable;

    byte[] dataNull;

    byte[] dataNotNull;

    Node node;

    public void setUp() throws Exception
    {
        txNodePrimitiveSerializable = new TxNodePrimitiveSerializable();
        dataNull = null;
        super.setUp();
    }

    public void testCreateStringListNull()
    {
        assertNull(txNodePrimitiveSerializable.createStringList(dataNull));
    }

    public void testCreateStringListNotNull()
    {
        StringList list = new StringList();
        node = new Node();
        node.addString("str1");
        node.addString("str2");
        dataNotNull = node.toBinary();
        list = txNodePrimitiveSerializable.createStringList(dataNotNull);
        assertEquals(2, list.size());
        assertEquals("str1", list.elementAt(0));
        assertEquals("str2", list.elementAt(1));
    }

    public void testCreateBytesListNull()
    {
        assertNull(txNodePrimitiveSerializable.createBytesList(dataNull));
    }

    public void testCreateBytesListNotNull()
    {
        BytesList list = new BytesList();
        node = new Node();
        Node child0 = new Node();
        Node child1 = new Node();
        child1.setBinaryData("str1".getBytes());
        child1.setBinaryData("str2".getBytes());
        node.addChild(child0);
        node.addChild(child1);
        dataNotNull = node.toBinary();

        list = txNodePrimitiveSerializable.createBytesList(dataNotNull);
        assertEquals(2, list.size());
        Assert.assertArrayEquals(child0.getBinaryData(), list.elementAt(0));
        Assert.assertArrayEquals(child1.getBinaryData(), list.elementAt(1));
    }

    public void testCreateStringMapNull()
    {
        assertNull(txNodePrimitiveSerializable.createStringMap(dataNull));
    }

    public void testCreateStringMapNotNull()
    {
        node = new Node();
        StringMap map = new StringMap();
        Node child0 = new Node();
        Node child1 = new Node();
        child0.addString("key");
        child1.addString("value");
        node.addChild(child0);
        node.addChild(child1);
        dataNotNull = node.toBinary();
        map = txNodePrimitiveSerializable.createStringMap(dataNotNull);
        assertEquals("value", map.get("key"));
    }
    
    public void testToBytes_StringMapNull()
    {
        StringMap map = new StringMap();
        map = null;
        assertNull(txNodePrimitiveSerializable.toBytes(map));
    }
    
    public void testToBytes_StringMapNotNull()
    {
        StringMap map = new StringMap();
        map.put("key", "value");
        node = new Node();
        Node keyNode = new Node();
        Node valueNode = new Node();
        keyNode.addString("key");
        valueNode.addString("value");
        node.addChild(keyNode);
        node.addChild(valueNode);
        dataNotNull = txNodePrimitiveSerializable.toBytes(map);
        Assert.assertArrayEquals(node.toBinary(), dataNotNull);
    }
    
    public void testToBytes_StringListNull()
    {
        StringList list = new StringList();
        list = null;
        assertNull(txNodePrimitiveSerializable.toBytes(list));
    }
    
    public void testToBytes_StringListNotNull()
    {
        StringList list = new StringList();
        list.add("str0");
        list.add("str1");
        dataNotNull = txNodePrimitiveSerializable.toBytes(list);
        node = new Node();
        node.addString("str0");
        node.addString("str1");
        Assert.assertArrayEquals(node.toBinary(), dataNotNull);
    }
    
    public void testToBytes_BytesListNull()
    {
        BytesList list = new BytesList();
        list = null;
        assertNull(txNodePrimitiveSerializable.toBytes(list));
    }
    
    public void testToBytes_BytesListNotNull()
    {
        BytesList list = new BytesList();
        list.add("str0".getBytes());
        list.add("str1".getBytes());
        dataNotNull = txNodePrimitiveSerializable.toBytes(list);
        node = new Node();
        Node child0 = new Node();
        Node child1 = new Node();
        child0.setBinaryData("str0".getBytes());
        child1.setBinaryData("str1".getBytes());
        node.addChild(child0);
        node.addChild(child1);
        Assert.assertArrayEquals(node.toBinary(), dataNotNull);
    }
    
    
   

}
