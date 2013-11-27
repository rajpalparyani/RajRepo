/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SortTest.java
 *
 */
package com.telenav.sort;

import junit.framework.TestCase;

import com.telenav.sort.SortAlgorithm;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-14
 */
public class SortTest extends TestCase
{
    public void testBiDirectional()
    {
        doTest(SortAlgorithm.BIDIRECTION_BUBBLE_SORT);
    }
    
    public void testBubble()
    {
        doTest(SortAlgorithm.BUBBLE_SORT);
    }
    
    public void testEQ()
    {
        doTest(SortAlgorithm.EQ_SORT);
    }
    
    public void testFastQ()
    {
        doTest(SortAlgorithm.FAST_Q_SORT);
    }
    
    public void testInsertion()
    {
        doTest(SortAlgorithm.INSERTION_SORT);
    }
    
    public void testMerge()
    {
        doTest(SortAlgorithm.MERGE_SORT);
    }
    
    public void testQ()
    {
        doTest(SortAlgorithm.Q_SORT);
    }
    
    public void testSelection()
    {
        doTest(SortAlgorithm.SELECTION_SORT);
    }
    
    public void testSharker()
    {
        doTest(SortAlgorithm.SHARKER_SORT);
    }
    
    public void testStorageMerge()
    {
        doTest(SortAlgorithm.STORAGE_MERGE_SORT);
    }
    
    public void doTest(int algorithm)
    {
        ComparableObject comparableObject0 = new ComparableObject();
        comparableObject0.setValue(0);
        ComparableObject comparableObject1 = new ComparableObject();
        comparableObject1.setValue(1);
        ComparableObject comparableObject2 = new ComparableObject();
        comparableObject2.setValue(2);
        ComparableObject comparableObject3 = new ComparableObject();
        comparableObject3.setValue(3);
        ComparableObject comparableObject4 = new ComparableObject();
        comparableObject4.setValue(4);
        ComparableObject comparableObject5 = new ComparableObject();
        comparableObject5.setValue(5);

        ComparableObject[] expected = new ComparableObject[]
        { comparableObject0, comparableObject1, comparableObject2, comparableObject3, comparableObject4, comparableObject5 };

        ComparableObject[] actual = new ComparableObject[]
        { comparableObject5, comparableObject3, comparableObject0, comparableObject1, comparableObject4, comparableObject2 };

        SortAlgorithm.sort(actual, algorithm);
        assertEquals(expected, actual);
    }

    public static void assertEquals(ComparableObject[] expected, ComparableObject[] actual)
    {
        if (expected == null || actual == null)
            return;

        if (expected.length == actual.length)
        {
            for (int i = 0; i < expected.length; i++)
            {
                if (expected[i].compareTo(actual[i]) != 0)
                {
                    failNotEquals("", expected, actual);
                    return;
                }
            }
            return;
        }
        else
        {
            failNotEquals("", expected, actual);
            return;
        }
    }
}
