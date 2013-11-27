/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BytesList.java
 *
 */
package com.telenav.data.datatypes.primitive;

import java.util.Enumeration;
import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public class BytesList
{
    private Vector vector = new Vector();
    
    public void add(byte[] element)
    {
        vector.addElement(element);
    }
    
    public void remove(byte[] element)
    {
        vector.removeElement(element);
    }
    
    public byte[] elementAt(int index)
    {
        return (byte[])vector.elementAt(index);
    }
    
    public Enumeration elements()
    {
        return vector.elements();
    }
    
    public int size()
    {
        return vector.size();
    }
}
