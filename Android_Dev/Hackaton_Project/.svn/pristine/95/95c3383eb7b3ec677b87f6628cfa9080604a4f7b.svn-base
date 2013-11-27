/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SortAlgorithm.java
 *
 */
package com.telenav.sort;

import com.telenav.logger.Logger;

/**
 * A generic sort demonstration algorithm
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * @author James Gosling
 * @version     1.6f, 31 Jan 1995
 */

public abstract class SortAlgorithm
{
    /**
     * A bi-directional bubble sort demonstration algorithm  
     */
    public static final int BIDIRECTION_BUBBLE_SORT = 0;

    /**
     * A bubble sort demonstration algorithm
     */
    public static final int BUBBLE_SORT = 1;

    /**
     * An enhanced quick sort demonstration algorithm 
     */
    public static final int EQ_SORT = 2;

    /**
     * A merge sort demonstration algorithm using extra space 
     */
    public static final int STORAGE_MERGE_SORT = 3;

    /**
     * A fast quick sort demonstration algorithm 
     */
    public static final int FAST_Q_SORT = 4;

    /**
     * An insertion sort demonstration algorithm 
     */
    public static final int INSERTION_SORT = 5;

    /**
     * A merge sort demonstration algorithm 
     */
    public static final int MERGE_SORT = 6;

    /**
     * A quick sort demonstration algorithm 
     */
    public static final int Q_SORT = 7;

    /**
     * A selection sort demonstration algorithm 
     */
    public static final int SELECTION_SORT = 8;

    /**
     * A shaker sort demonstration algorithm 
     */
    public static final int SHARKER_SORT = 9;
    
    /**
     * This method will be called to sort an array of IComparable objects.
     */
    protected abstract void sort1(Object[] a) throws Exception;
    
    /**
     * This method will be called to sort an array of IComparable objects with default FAST_Q_SORT.
     * 
     * @param a an array of IComparable objects
     */
    public static void sort(Object[] a)
    {
        sort(a, FAST_Q_SORT);
    }
    
    /**
     * This method will be called to sort an array of IComparable objects.
     * 
     * @param a an array of IComparable objects
     * @param sortAlgorithm sort algorithm.
     */
    public static void sort(Object[] a, int sortAlgorithm)
    {
        SortAlgorithm sa = null;
        switch(sortAlgorithm)
        {
            case BIDIRECTION_BUBBLE_SORT:
            {
                sa = new BidirectionalBubbleSortAlgorithm();
                break;
            }
            case BUBBLE_SORT:
            {
                sa = new BubbleSort2Algorithm();
                break;
            }
            case EQ_SORT:
            {
                sa = new EQSortAlgorithm();
                break;
            }
            case STORAGE_MERGE_SORT:
            {
                sa = new ExtraStorageMergeSortAlgorithm();
                break;
            }
            case FAST_Q_SORT:
            {
                sa = new FastQSortAlgorithm();
                break;
            }
            case INSERTION_SORT:
            {
                sa = new InsertionSortAlgorithm();
                break;
            }
            case MERGE_SORT:
            {
                sa = new MergeSortAlgorithm();
                break;
            }
            case Q_SORT:
            {
                sa = new QSortAlgorithm();
                break;
            }
            case SELECTION_SORT:
            {
                sa = new SelectionSortAlgorithm();
                break;
            }
            case SHARKER_SORT:
            {
                sa = new ShakerSortAlgorithm();
                break;
            }
            default:
            {
                sa = new FastQSortAlgorithm();
            }
        }
        
        try
        {
            sa.sort1(a);
        }
        catch (Exception e)
        {
            Logger.log(SortAlgorithm.class.getName(), e);
        }
    }
}

