/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TxNodePrimitiveSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import java.util.Enumeration;

import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.IPrimitiveSerializable;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
class TxNodePrimitiveSerializable implements IPrimitiveSerializable
{

    public StringList createStringList(byte[] data)
    {
        if(data == null)
            return null;
        
        return createStringList(new Node(data, 0));
    }
    
    static StringList createStringList(Node node)
    {
        if(node == null)
            return null;
        
        StringList list = new StringList();
        for(int i = 0; i < node.getStringsSize(); i++)
        {
            list.add(node.getStringAt(i));
        }
        
        return list;
    }
    

    public BytesList createBytesList(byte[] data)
    {
        if(data == null)
            return null;
        
        return createBytesList(new Node(data, 0));
    }
    
    static BytesList createBytesList(Node node)
    {
        if(node == null)
            return null;
        
        BytesList list = new BytesList();
        for(int i = 0; i < node.getChildrenSize(); i++)
        {
            list.add(node.getChildAt(i).getBinaryData());
        }
        
        return list;
    }
    
    public StringMap createStringMap(byte[] data)
    {
        if(data == null)
            return null;
        
        return createStringMap(new Node(data, 0));
    }
    
    static StringMap createStringMap(Node node)
    {
        if(node == null)
            return null;
        
        StringMap map = new StringMap();
        Node keyNode = node.getChildAt(0);
        Node valueNode = node.getChildAt(1);
        for(int i = 0; i < keyNode.getStringsSize(); i++)
        {
            map.put(keyNode.getStringAt(i), valueNode.getStringAt(i));
        }
        
        return map;
    }

    public byte[] toBytes(StringList list)
    {
        if(list == null)
            return null;
        
        Node node = new Node();
        for(int i = 0; i < list.size(); i++)
        {
            node.addString(list.elementAt(i));
        }
        
        return node.toBinary();
    }

    public byte[] toBytes(StringMap map)
    {
        if(map == null)
            return null;
        
        Node node = new Node();
        Node keyNode = new Node();
        Node valueNode = new Node();
        Enumeration keyEnum = map.keys();
        while(keyEnum.hasMoreElements())
        {
            String key = (String)keyEnum.nextElement();
            keyNode.addString(key);
            String value = map.get(key);
            valueNode.addString(value);
        }
        
        node.addChild(keyNode);
        node.addChild(valueNode);
        
        return node.toBinary();
    }
    
    public byte[] toBytes(BytesList list)
    {
        if(list == null)
            return null;
        
        Node node = new Node();
        for(int i = 0; i < list.size(); i++)
        {
            Node child = new Node();
            child.setBinaryData(list.elementAt(i));
            node.addChild(child);
        }
        
        return node.toBinary();
    }

}
