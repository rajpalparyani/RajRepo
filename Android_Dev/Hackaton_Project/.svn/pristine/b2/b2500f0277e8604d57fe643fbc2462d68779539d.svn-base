/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ExtraStorageMergeSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * A merge sort demonstration algorithm using extra space
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * @author Jack Snoeyink@cs.ubc.ca
 * @version     1.0, 09 Jan 97
 */
class ExtraStorageMergeSortAlgorithm extends SortAlgorithm
{
    void sort(Object a[], int lo, int hi, Object scratch[]) throws Exception
    {
        if (lo >= hi)
        {
            return; /* a[lo] is sorted already */
        }

        int mid = (lo + hi) / 2;
        sort(a, lo, mid, scratch); /* Sort sublist a[lo..mid] */
        sort(a, mid + 1, hi, scratch); /* Sort sublist a[mid+1..hi] */

        int k, t_lo = lo, t_hi = mid + 1;
        for (k = lo; k <= hi; k++)
            /* Merge sorted sublists */
            if ((t_lo <= mid) && (t_hi > hi || ((IComparable)a[t_lo]).compareTo(a[t_hi]) < 0))
            {
                scratch[k] = a[t_lo++];
            }
            else
            {
                scratch[k] = a[t_hi++];
            }

        for (k = lo; k <= hi; k++)
        {
            a[k] = scratch[k]; /* Copy back to a */
        }
    }

    protected void sort1(Object[] a) throws Exception
    {
        IComparable scratch[] = new IComparable[a.length];
        sort(a, 0, a.length - 1, scratch);
    }
}

