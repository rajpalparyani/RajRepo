/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BubbleSort2Algorithm.java
 *
 */
package com.telenav.sort;

/**
 * A bubble sort demonstration algorithm
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * @author James Gosling
 * @version     1.6, 31 Jan 1995
 *
 * Modified 23 Jun 1995 by Jason Harrison@cs.ubc.ca:
 *   Algorithm completes early when no items have been swapped in the 
 *   last pass.
 */
class BubbleSort2Algorithm extends SortAlgorithm
{
    protected void sort1(Object[] a) throws Exception
    {
        for (int i = a.length; --i >= 0;)
        {
            boolean flipped = false;
            for (int j = 0; j < i; j++)
            {
                if (((IComparable)a[j]).compareTo(a[j + 1]) > 0)
                {
                    Object T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                    flipped = true;
                }

            }
            if (!flipped)
            {
                return;
            }
        }
    }
}

