/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * InsertionSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * An insertion sort demonstration algorithm
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * @author Jason Harrison@cs.ubc.ca
 * @version     1.0, 23 Jun 1995
 *
 */
class InsertionSortAlgorithm extends SortAlgorithm
{
    protected void sort1(Object[] a) throws Exception
    {
        for (int i = 1; i < a.length; i++)
        {
            int j = i;
            Object B = a[i];
            while ((j > 0) && ((IComparable)a[j - 1]).compareTo(B) > 0)
            {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = B;
        }
    }
}


