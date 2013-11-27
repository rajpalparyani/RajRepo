/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BidirectionalBubbleSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * A bi-directional bubble sort demonstration algorithm
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * @author James Gosling
 * @version     1.6f, 31 Jan 1995
 */
class BidirectionalBubbleSortAlgorithm extends SortAlgorithm
{
    protected void sort1(Object[] a) throws Exception
    {
        int j;
        int limit = a.length;
        int st = -1;
        while (st < limit)
        {
            boolean flipped = false;
            st++;
            limit--;
            for (j = st; j < limit; j++)
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
            for (j = limit; --j >= st;)
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

