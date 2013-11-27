/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LookupAabUriTest.java
 *
 */
package com.telenav.sdk.maitai.impl;

import java.util.Vector;

import org.easymock.EasyMock;
import org.json.tnme.JSONObject;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;

import junit.framework.TestCase;

/**
 *@author chrbu (chrbu@telenav.cn)
 *@date 2012-1-30
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ MaiTaiUtil.class})
public class MaiTaiUtilTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        PowerMock.resetAll();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void test_parseRequestUri() throws Exception
    {
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");      
        PowerMock.replayAll();
        String requestUri = "http://maps.google.com/maps?f=d&saddr=37.373919+-121.999303&daddr=1576+Halford+Ave+Santa+Clara+CA+95051";
        Vector list=Whitebox.invokeMethod(mockMaiTaiUtil, "parseRequestUri",requestUri);
        assertEquals(list.size(), 5);
        PowerMock.verifyAll();
    }
    
    public void test_getValue() throws Exception
    {
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"parseRequestUri");      
        PowerMock.replayAll();
        Vector list=new Vector();
        String[] schema=new String[]{"schema","http"};
        list.add(schema);
        String[] action=new String[]{"action","maps"};
        list.add(action);
        
        Object[] args=new Object[]{list,"action"};
        String result=Whitebox.invokeMethod(mockMaiTaiUtil, "getValue",args);
        assertNotNull(result);
        PowerMock.verifyAll();
    }
    
    
    public void test_getValues() throws Exception
    {
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"parseRequestUri");      
        PowerMock.replayAll();
        Vector list=new Vector();
        String[] schema=new String[]{"schema","http"};
        list.add(schema);
        String[] action=new String[]{"action","maps"};
        list.add(action);
        
        Object[] args=new Object[]{list,"action"};
        Vector result=Whitebox.invokeMethod(mockMaiTaiUtil, "getValues",args);
        assertEquals(result.size(), 1);
        PowerMock.verifyAll();
    }
    
    
}
