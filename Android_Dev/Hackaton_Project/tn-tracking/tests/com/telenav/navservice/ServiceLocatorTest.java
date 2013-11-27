package com.telenav.navservice;


import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.telenav.navservice.model.App;
import com.telenav.navservice.network.TnNetwork;

public class ServiceLocatorTest
{
    private TnNetwork network;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        TestUtil.initLog();
    }
    
    @Before
    public void setUp() throws Exception
    {
        network = createMock(TnNetwork.class);
        byte[] b = new byte[]{18, 0, 0, 0, 83, 121, 110, 99, 83, 101, 114, 118, 105, 99, 101, 76, 111, 99, 97, 116, 111, 114, -84, 0, 0, 0, 8, 0, 18, 0, 26, -91, 1, 10, 13, 49, 46, 48, 46, 48, 49, 45, 49, 46, 48, 46, 48, 49, 18, 110, 10, 14, 116, 114, 97, 99, 107, 105, 110, 103, 112, 111, 108, 105, 99, 121, 18, 19, 116, 114, 97, 99, 107, 105, 110, 103, 112, 111, 108, 105, 99, 121, 46, 104, 116, 116, 112, 26, 69, 104, 116, 116, 112, 58, 47, 47, 113, 97, 45, 117, 110, 105, 116, 48, 51, 46, 116, 101, 108, 101, 110, 97, 118, 46, 99, 111, 109, 58, 56, 48, 56, 48, 47, 114, 101, 115, 111, 117, 114, 99, 101, 45, 99, 115, 101, 114, 118, 101, 114, 47, 112, 111, 108, 105, 99, 121, 95, 108, 101, 118, 101, 108, 48, 46, 106, 115, 111, 110, 34, 0, 18, 36, 10, 3, 108, 100, 108, 18, 7, 108, 100, 108, 46, 117, 100, 112, 26, 18, 54, 51, 46, 50, 51, 55, 46, 50, 50, 48, 46, 51, 54, 58, 54, 48, 48, 48, 34, 0};
        expect(network.sendHttpPost((String)anyObject(), (byte[])anyObject())).andReturn(b).anyTimes();
        
        replay(network);
    }

    @After
    public void tearDown() throws Exception
    {
        network = null;
    }

    @Test
    public void testGetNavServiceUrls() throws Exception
    {
        App app = new App();
        ServiceLocator serviceLocator = new ServiceLocator();
        serviceLocator.network = network;
        String[] urls = serviceLocator.getNavServiceUrls("", app);
        assertEquals(urls.length, 2);
        assertEquals(urls[0], "http://qa-unit03.telenav.com:8080/resource-cserver/policy_level0.json");
        assertEquals(urls[1], "63.237.220.36:6000");
    }
}
