package com.telenav.navservice.location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.navservice.TestUtil;

public class CellRecorderTest
{

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testSetParameters() throws Exception
    {
        CellRecorder cellRecorder = new CellRecorder("", new LocationBuffer(100, null));
        cellRecorder.setParameters(10);
        assertEquals(cellRecorder.sampleIntervalInMillis, 10 * 1000);
        assertTrue(cellRecorder.isGettingCellLocation);
        
        cellRecorder.setParameters(0);
        assertFalse(cellRecorder.isGettingCellLocation);
        
        cellRecorder.setParameters(-1);
        assertFalse(cellRecorder.isGettingCellLocation);
    }
}
