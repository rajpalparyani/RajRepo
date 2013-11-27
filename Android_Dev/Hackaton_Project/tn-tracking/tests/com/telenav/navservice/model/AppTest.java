package com.telenav.navservice.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.navservice.TestUtil;

public class AppTest
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
    public void testSetRouteId() throws Exception
    {
        App app = new App();
        app.setRouteId(null);
        assertFalse(app.isDriveTo());
        
        app.setRouteId("");
        assertFalse(app.isDriveTo());
        
        app.setRouteId("-1");
        assertFalse(app.isDriveTo());
        
        app.setRouteId("1");
        assertTrue(app.isDriveTo());
        
        app.setRouteId("routeid");
        assertTrue(app.isDriveTo());
        
    }
}
