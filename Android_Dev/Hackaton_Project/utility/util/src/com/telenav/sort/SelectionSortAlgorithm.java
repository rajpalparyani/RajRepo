/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SelectionSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * A selection sort demonstration algorithm SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 * 
 * @author Jason Harrison@cs.ubc.ca
 * @version 1.0, 23 Jun 1995
 * 
 */
class SelectionSortAlgorithm extends SortAlgorithm
{
    protected void sort1(Object[] a) throws Exception
    {
        for (int i = 0; i < a.length; i++)
        {
            int min = i;
            int j;

            /*
             * Find the smallest element in the unsorted list
             */
            for (j = i + 1; j < a.length; j++)
            {

                if (((IComparable)a[j]).compareTo(a[min]) < 0)
                {
                    min = j;
                }
            }

            /*
             * Swap the smallest unsorted element into the end of the sorted list.
             */
            Object T = a[min];
            a[min] = a[i];
            a[i] = T;
        }
    }
}
