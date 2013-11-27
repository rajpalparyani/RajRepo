/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StringList.java
 *
 */
package com.telenav.data.datatypes.primitive;

import java.util.Enumeration;
import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public class StringList
{
    private Vector vector = new Vector();
    
    public void add(String element)
    {
        vector.addElement(element);
    }
    
    public void remove(String element)
    {
        vector.removeElement(element);
    }
    
    public String elementAt(int index)
    {
        return (String)vector.elementAt(index);
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
