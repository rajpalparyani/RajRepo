/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ComparableObject.java
 *
 */
package com.telenav.sort;

import com.telenav.sort.IComparable;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-18
 */
public class ComparableObject implements IComparable
{
    private int i;
    
    public ComparableObject()
    {
        
    }
    
    public void setValue(int i)
    {
        this.i = i;
    }

    public int compareTo(Object another)
    {
        if (another instanceof ComparableObject)
        {
            ComparableObject obj = (ComparableObject) another;
            if (this.i > obj.i)
            {
                return 1;
            }
            else if (this.i < obj.i)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }

        return 1;
    }

}
