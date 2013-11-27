/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EQSortAlgorithm.java
 *
 */
package com.telenav.sort;

/**
 * An enhanced quick sort demonstration algorithm
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * @author Jim Boritz
 * @version     1.0, 26 Jun 1995
 */
/**
 * 19 Feb 1996: Fixed to avoid infinite loop discoved by Paul Haberli.
 *              Misbehaviour expressed when the pivot element was not unique.
 *              -Jason Harrison
 *
 * 21 Jun 1996: Modified code based on comments from Paul Haeberli, and
 *              Peter Schweizer (Peter.Schweizer@mni.fh-giessen.de).  
 *              Used Daeron Meyer's (daeron@geom.umn.edu) code for the
 *              new pivoting code. - Jason Harrison
 *
 * 09 Jan 1998: Another set of bug fixes by Thomas Everth (everth@wave.co.nz)
 *              and John Brzustowski (jbrzusto@gpu.srv.ualberta.ca).
 */
class EQSortAlgorithm extends SortAlgorithm
{
    void brute(Object a[], int lo, int hi) throws Exception
    {
        if ((hi - lo) == 1)
        {
            if (((IComparable)a[hi]).compareTo(a[lo]) < 0)
            {
                Object T = a[lo];
                a[lo] = a[hi];
                a[hi] = T;
            }
        }
        if ((hi - lo) == 2)
        {
            int pmin = ((IComparable)a[lo]).compareTo(a[lo + 1]) < 0 ? lo : lo + 1;
            pmin = ((IComparable)a[pmin]).compareTo(a[lo + 2]) < 0 ? pmin : lo + 2;
            if (pmin != lo)
            {
                Object T = a[lo];
                a[lo] = a[pmin];
                a[pmin] = T;
            }
            brute(a, lo + 1, hi);
        }
        if ((hi - lo) == 3)
        {
            int pmin = ((IComparable)a[lo]).compareTo(a[lo + 1]) < 0 ? lo : lo + 1;
            pmin = ((IComparable)a[pmin]).compareTo(a[lo + 2]) < 0 ? pmin : lo + 2;
            pmin = ((IComparable)a[pmin]).compareTo(a[lo + 3]) < 0 ? pmin : lo + 3;
            if (pmin != lo)
            {
                Object T = a[lo];
                a[lo] = a[pmin];
                a[pmin] = T;
            }
            int pmax = ((IComparable)a[hi]).compareTo(a[hi - 1]) > 0 ? hi : hi - 1;
            pmax = ((IComparable)a[pmax]).compareTo(a[hi - 2]) > 0 ? pmax : hi - 2;
            if (pmax != hi)
            {
                Object T = a[hi];
                a[hi] = a[pmax];
                a[pmax] = T;
            }
            brute(a, lo + 1, hi - 1);
        }
    }

    void sort(Object a[], int lo0, int hi0) throws Exception
    {
        int lo = lo0;
        int hi = hi0;
        if ((hi - lo) <= 3)
        {
            brute(a, lo, hi);
            return;
        }

        /*
         * Pick a pivot and move it out of the way
         */
        Object pivot = a[(lo + hi) >>> 1];
        a[(lo + hi) / 2] = a[hi];
        a[hi] = pivot;

        while (lo < hi)
        {
            /*
             * Search forward from a[lo] until an element is found that is greater than the pivot or lo >= hi
             */
            while (((IComparable)a[lo]).compareTo(pivot) <= 0 && lo < hi)
            {
                lo++;
            }

            /*
             * Search backward from a[hi] until element is found that is less than the pivot, or hi <= lo
             */
            while (((IComparable)pivot).compareTo(a[hi]) <= 0 && lo < hi)
            {
                hi--;
            }

            /*
             * Swap elements a[lo] and a[hi]
             */
            if (lo < hi)
            {
                Object T = a[lo];
                a[lo] = a[hi];
                a[hi] = T;
            }
        }

        /*
         * Put the median in the "center" of the list
         */
        a[hi0] = a[hi];
        a[hi] = pivot;

        /*
         * Recursive calls, elements a[lo0] to a[lo-1] are less than or equal to pivot, elements a[hi+1] to a[hi0] are
         * greater than pivot.
         */
        sort(a, lo0, lo - 1);
        sort(a, hi + 1, hi0);
    }

    protected void sort1(Object[] a) throws Exception
    {
        sort(a, 0, a.length - 1);
    }
}

