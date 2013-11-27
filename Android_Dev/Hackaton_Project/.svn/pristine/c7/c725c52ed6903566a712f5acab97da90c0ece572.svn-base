/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ShakerSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * A shaker sort demonstration algorithm SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 * 
 * @author Jason Harrison@cs.ubc.ca
 * @version 1.0, 26 Jun 1995
 * 
 */
class ShakerSortAlgorithm extends SortAlgorithm
{
    protected void sort1(Object[] a) throws Exception
    {
        int i = 0;
        int k = a.length - 1;
        while (i < k)
        {
            int min = i;
            int max = i;
            int j;
            for (j = i + 1; j <= k; j++)
            {

                if (((IComparable)a[j]).compareTo(a[min]) < 0)
                {
                    min = j;
                }
                if (((IComparable)a[j]).compareTo(a[max]) > 0)
                {
                    max = j;
                }
            }
            Object T = a[min];
            a[min] = a[i];
            a[i] = T;

            if (max == i)
            {
                T = a[min];
                a[min] = a[k];
                a[k] = T;
            }
            else
            {
                T = a[max];
                a[max] = a[k];
                a[k] = T;
            }
            i++;
            k--;
        }
    }
}
