/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MergeSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * A merge sort demonstration algorithm SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 * 
 * @author Jason Harrison@cs.ubc.ca
 * @version 1.1, 12 Jan 1998
 */
class MergeSortAlgorithm extends SortAlgorithm
{
    void sort(Object a[], int lo0, int hi0) throws Exception
    {
        int lo = lo0;
        int hi = hi0;
        if (lo >= hi)
        {
            return;
        }
        int mid = (lo + hi) / 2;

        /*
         * Partition the list into two lists and sort them recursively
         */
        sort(a, lo, mid);
        sort(a, mid + 1, hi);

        /*
         * Merge the two sorted lists
         */
        int end_lo = mid;
        int start_hi = mid + 1;
        while ((lo <= end_lo) && (start_hi <= hi))
        {
            if (((IComparable)a[lo]).compareTo(a[start_hi]) < 0)
            {
                lo++;
            }
            else
            {
                /*
                 * a[lo] >= a[start_hi] The next element comes from the second list, move the a[start_hi] element into
                 * the next position and shuffle all the other elements up.
                 */
                Object T = a[start_hi];
                for (int k = start_hi - 1; k >= lo; k--)
                {
                    a[k + 1] = a[k];
                }
                a[lo] = T;
                lo++;
                end_lo++;
                start_hi++;
            }
        }
    }

    protected void sort1(Object[] a) throws Exception
    {
        sort(a, 0, a.length - 1);
    }
}
