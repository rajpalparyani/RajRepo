/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FastQSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * A quick sort demonstration algorithm
 * SortAlgorithm.java
 *
 * @author James Gosling
 * @author Kevin A. Smith
 * @version     @(#)QSortAlgorithm.java 1.3, 29 Feb 1996
 * extended with TriMedian and InsertionSort by Denis Ahrens
 * with all the tips from Robert Sedgewick (Algorithms in C++).
 * It uses TriMedian and InsertionSort for lists shorts than 4.
 * <fuhrmann@cs.tu-berlin.de>
 */
public class FastQSortAlgorithm extends SortAlgorithm 

{
    /**
     * This is a generic version of C.A.R Hoare's Quick Sort algorithm. This will handle arrays that are already sorted,
     * and arrays with duplicate keys.<BR>
     * 
     * If you think of a one dimensional array as going from the lowest index on the left to the highest index on the
     * right then the parameters to this function are lowest index or left and highest index or right. The first time
     * you call this function it will be with the parameters 0, a.length - 1.
     * 
     * @param a an integer array
     * @param lo0 left boundary of array partition
     * @param hi0 right boundary of array partition
     */
    private void QuickSort(Object a[], int l, int r) throws Exception
    {
        int M = 4;
        int i;
        int j;
        Object v;

        if ((r - l) > M)
        {
            i = (r + l) >>> 1;
            if (((IComparable)a[l]).compareTo(a[i]) > 0)
                swap(a, l, i); // Tri-Median Methode!
            if (((IComparable)a[l]).compareTo(a[r]) > 0)
                swap(a, l, r);
            if (((IComparable)a[i]).compareTo(a[r]) > 0)
                swap(a, i, r);

            j = r - 1;
            swap(a, i, j);
            i = l;
            v = a[j];
            for (;;)
            {
                while (((IComparable)a[++i]).compareTo(v) < 0)
                    ;
                while (((IComparable)a[--j]).compareTo(v) > 0)
                    ;
                if (j < i)
                    break;
                swap(a, i, j);
            }
            swap(a, i, r - 1);
            QuickSort(a, l, j);
            QuickSort(a, i + 1, r);
        }
    }

    private void swap(Object a[], int i, int j)
    {
        Object T;
        T = a[i];
        a[i] = a[j];
        a[j] = T;
    }

    private void InsertionSort(Object a[], int lo0, int hi0) throws Exception
    {
        int i;
        int j;
        Object v;

        for (i = lo0 + 1; i <= hi0; i++)
        {
            v = a[i];
            j = i;
            while ((j > lo0) && ((IComparable)a[j - 1]).compareTo(v) > 0)
            {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = v;
        }
    }

    protected void sort1(Object[] a) throws Exception
    {
        QuickSort(a, 0, a.length - 1);
        InsertionSort(a, 0, a.length - 1);
    }
}


