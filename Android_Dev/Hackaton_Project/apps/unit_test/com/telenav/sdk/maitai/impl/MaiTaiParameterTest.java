/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * LookupAabUriTest.java
 *
 */
package com.telenav.sdk.maitai.impl;

import java.util.Vector;

import org.easymock.EasyMock;
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
{ MaiTaiParameter.class, MaiTaiUtil.class, MaiTaiHandler.class ,MaiTaiAuthenticate.class,
    AbstractDaoManager.class,LocationProvider.class})
public class MaiTaiParameterTest extends TestCase
{
	/*
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
    
    public void testnotify() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "validateParams","parseParams","getContext");
        PowerMock.mockStatic(MaiTaiUtil.class);
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "parseRequestUri");
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "cancel","addResult");
        Object[] param=new Object[]{EasyMock.anyObject(String.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult",param);
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil);
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Vector list = new Vector();
        list.add("test1");
        PowerMock.expectPrivate(mockMaiTaiUtil, "parseRequestUri",EasyMock.anyObject(String.class)).andReturn(list);      
        
        PowerMock.expectPrivate(mockMaiTaiParameter, "validateParams",EasyMock.anyObject(Vector.class)).andReturn(true);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseParams",EasyMock.anyObject(Vector.class)).andReturn(true);
        PowerMock.expectPrivate(mockMaiTaiParameter, "getContext").andReturn("1111").anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        
        PowerMock.replayAll();
        Whitebox.invokeMethod(mockMaiTaiParameter, "notify","adasdasd");
        assertEquals(mockMaiTaiParameter.getContext(), "1111");
        PowerMock.verifyAll();
    }
    
    public void testnotify_cancel() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "validateParams","parseParams","getContext");
        PowerMock.mockStatic(MaiTaiUtil.class);
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "parseRequestUri");
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "cancel");
        PowerMock.expectPrivate(mockMaiTaiHandler, "cancel");
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil);
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Vector list = new Vector();
        list.add("test1");
        PowerMock.expectPrivate(mockMaiTaiUtil, "parseRequestUri",EasyMock.anyObject(String.class)).andReturn(list);      
        
        PowerMock.expectPrivate(mockMaiTaiParameter, "validateParams",EasyMock.anyObject(Vector.class)).andReturn(true);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseParams",EasyMock.anyObject(Vector.class)).andReturn(false);
        PowerMock.expectPrivate(mockMaiTaiParameter, "getContext").andReturn("1111").anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        
        PowerMock.replayAll();
        Whitebox.invokeMethod(mockMaiTaiParameter, "notify","adasdasd");
        assertEquals(mockMaiTaiParameter.getContext(), "1111");
        PowerMock.verifyAll();
    }
    
    public void testvalidateParams_versionNotMatch() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseParams");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] param=new Object[]{EasyMock.anyObject(Vector.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue",param).andReturn("3.0");
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam=new Object[]{EasyMock.anyObject(String.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult",addResultParam);
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        
        PowerMock.replayAll();
        Vector validateVector=new Vector();
        validateVector.add("");
        boolean flag=Whitebox.invokeMethod(mockMaiTaiParameter, "validateParams",validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
        
    }
    
    public void testvalidateParams_actionNotFind() throws Exception
    {
        PowerMock.resetAll();
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseParams");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] param=new Object[]{EasyMock.anyObject(Vector.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue",param).andReturn("1.0").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam=new Object[]{EasyMock.anyObject(String.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult",addResultParam);
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        
        PowerMock.replayAll();
        Vector validateVector=new Vector();
        validateVector.add("");
        boolean flag=Whitebox.invokeMethod(mockMaiTaiParameter, "validateParams",validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
        
    }
    
    
    public void testvalidateParams_authenticateFalse() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseParams");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] param=new Object[]{EasyMock.anyObject(Vector.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue",param).andReturn("navTo").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam=new Object[]{EasyMock.anyObject(String.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult",addResultParam).anyTimes();;
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        MaiTaiAuthenticate mockMaiTaiAuthenticate=PowerMock.createPartialMock(MaiTaiAuthenticate.class, "validate","getLastError");
        PowerMock.expectPrivate(mockMaiTaiAuthenticate, "validate",EasyMock.anyObject(String.class)).andReturn(false).anyTimes();
        PowerMock.expectPrivate(mockMaiTaiAuthenticate, "getLastError").andReturn(1).anyTimes();
        
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        Whitebox.setInternalState(mockMaiTaiParameter, "authenticator", mockMaiTaiAuthenticate);
        
        PowerMock.replayAll();
        Vector validateVector=new Vector();
        validateVector.add("");
        boolean flag=Whitebox.invokeMethod(mockMaiTaiParameter, "validateParams",validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
    }
    
    public void testvalidateParams() throws Exception
    {
        Vector validateVector=new Vector();
        validateVector.add("");
        
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseParams");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramVersion=new Object[]{validateVector,"v"};
        Object[] paramAction=new Object[]{validateVector,"action"};
        Object[] paramKey=new Object[]{validateVector,"k"};
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue",paramVersion).andReturn("2.0").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue",paramAction).andReturn("navTo").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue",paramKey).andReturn("navTo").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam=new Object[]{EasyMock.anyObject(String.class),EasyMock.anyObject(String.class)};
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult",addResultParam).anyTimes();;
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        MaiTaiAuthenticate mockMaiTaiAuthenticate=PowerMock.createPartialMock(MaiTaiAuthenticate.class, "validate","getLastError");
        PowerMock.expectPrivate(mockMaiTaiAuthenticate, "validate",EasyMock.anyObject(String.class)).andReturn(true).anyTimes();
        PowerMock.expectPrivate(mockMaiTaiAuthenticate, "getLastError").andReturn(1).anyTimes();
        
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        Whitebox.setInternalState(mockMaiTaiParameter, "authenticator", mockMaiTaiAuthenticate);
        
        PowerMock.replayAll();
        boolean flag=Whitebox.invokeMethod(mockMaiTaiParameter, "validateParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseNavToParams_nullAddress() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseLatAndLon");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,
            "getValue");
        Object[] param = new Object[]
        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(
            MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam)
                .anyTimes();
        ;
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler)
                .anyTimes();
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLon", EasyMock.anyObject(Vector.class));
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        Whitebox.setInternalState(mockMaiTaiParameter, "lat", 0);
        Whitebox.setInternalState(mockMaiTaiParameter, "lon", 0);

        PowerMock.replayAll();
        Vector validateVector = new Vector();
        validateVector.add("");
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseNavToParams",
            validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
    }
    
    public void test_parseNavToParams() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseLatAndLon");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,
            "getValue");
        Object[] param = new Object[]
        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("1111").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(
            MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam)
                .anyTimes();
        ;
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler)
                .anyTimes();
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLon", EasyMock.anyObject(Vector.class));
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        Whitebox.setInternalState(mockMaiTaiParameter, "lat2", 0);
        Whitebox.setInternalState(mockMaiTaiParameter, "lon2", 0);
        
        PowerMock.replayAll();
        Vector validateVector = new Vector();
        validateVector.add("");
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseNavToParams",
            validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseDirectionsParams_nullAddress() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseLatAndLonOrig","parseLatAndLonDest");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,
            "getValue");
        Object[] param = new Object[]
        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(
            MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam)
                .anyTimes();
        ;
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler)
                .anyTimes();
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLonOrig", EasyMock.anyObject(Vector.class));
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLonDest", EasyMock.anyObject(Vector.class));
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);

        PowerMock.replayAll();
        Vector validateVector = new Vector();
        validateVector.add("");
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseDirectionsParams",
            validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
    }
    
    
    public void test_parseDirectionsParams() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseLatAndLonOrig","parseLatAndLonDest");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,
            "getValue");
        Object[] param = new Object[]
        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("1111").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(
            MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam)
                .anyTimes();
        ;
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler)
                .anyTimes();
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLonOrig", EasyMock.anyObject(Vector.class));
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLonDest", EasyMock.anyObject(Vector.class));
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);

        PowerMock.replayAll();
        Vector validateVector = new Vector();
        validateVector.add("");
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseDirectionsParams",
            validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseDriveToParams_nullhomestop() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseLatAndLonOrig","parseLatAndLonDest");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,
            "getValue");
        Object[] param = new Object[]
        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("HOME").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(
            MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam)
                .anyTimes();
        ;
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler)
                .anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);

        PowerMock.mockStatic(AbstractDaoManager.class);
        DaoManager daoManager = PowerMock.createMock(DaoManager.class);
        EasyMock.expect(DaoManager.getInstance()).andReturn(daoManager);        
        AddressDao addressDao = PowerMock.createPartialMock(AddressDao.class,"getHomeAddress");
        PowerMock.expectPrivate(addressDao, "getHomeAddress").andReturn(null).anyTimes();
        EasyMock.expect(daoManager.getAddressDao()).andReturn(addressDao);    
        
        
        PowerMock.replayAll();
        Vector validateVector = new Vector();
        validateVector.add("");
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseDriveToParams",validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
    }
    
    
    public void test_parseDriveToParams_dymanicroad() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
        MaiTaiParameter.class, "parseLatAndLonOrig","parseLatAndLonDest","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"getValue");
        Object[] paramAddr1 = new Object[]{ validateVector, "namedAddr1" };
        Object[] paramAddr2 = new Object[]{ validateVector, "namedAddr2" };
        Object[] paramType = new Object[]{ validateVector, "type" };
        Object[] param = new Object[]{ validateVector, "addr2" };
        Object[] paramCoord2 = new Object[]{ validateVector, "coord2" };
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr1).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr2).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramType).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn(null).anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCoord2).andReturn("CURRENT").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam).anyTimes();
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        int[] coord=new int[2];
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseCoordinate",EasyMock.anyObject(String.class)).andReturn(coord).anyTimes();     
        
        PowerMock.mockStatic(LocationProvider.class);
        LocationProvider locationProvider = PowerMock.createPartialMock(LocationProvider.class,"getCurrentLocation","getLastKnownLocation");
        TnLocation tnLocation= new TnLocation("11111");
        tnLocation.setLatitude(0);
        tnLocation.setLongitude(0);
        PowerMock.expectPrivate(locationProvider, "getCurrentLocation", 3).andReturn(tnLocation).anyTimes();
        PowerMock.expectPrivate(locationProvider, "getLastKnownLocation", 3).andReturn(tnLocation).anyTimes();
        EasyMock.expect(LocationProvider.getInstance()).andReturn(locationProvider).anyTimes();   
          
        PowerMock.replayAll();
        
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseDriveToParams",validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
    }
    
    
    public void test_parseDriveToParams_notDymanicroad() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
        MaiTaiParameter.class, "parseLatAndLonOrig","parseLatAndLonDest","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"getValue");
        Object[] paramAddr1 = new Object[]{ validateVector, "namedAddr1" };
        Object[] paramAddr2 = new Object[]{ validateVector, "namedAddr2" };
        Object[] paramType = new Object[]{ validateVector, "type" };
        Object[] paramadd1 = new Object[]{ validateVector, "addr1" };
        Object[] paramadd2 = new Object[]{ validateVector, "addr2" };
        Object[] paramCoord1 = new Object[]{ validateVector, "coord1" };
        Object[] paramCoord2 = new Object[]{ validateVector, "coord2" };
        Object[] paramlbl = new Object[]{ validateVector, "lbl" };
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr1).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr2).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramType).andReturn("DIR").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramadd2).andReturn(null).anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCoord2).andReturn("coord2").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramadd1).andReturn(null).anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCoord1).andReturn("coord1").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramlbl).andReturn("lbl").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam).anyTimes();
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);        
        int[] coord1=new int[2];
        int[] coord2=new int[2];
        coord2[0]=55;
        coord2[1]=44;
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseCoordinate","coord1").andReturn(coord1).anyTimes(); 
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseCoordinate","coord2").andReturn(coord2).anyTimes();     
        
        PowerMock.mockStatic(LocationProvider.class);
        LocationProvider locationProvider = PowerMock.createPartialMock(LocationProvider.class,"getCurrentLocation","getLastKnownLocation");
        TnLocation tnLocation= new TnLocation("11111");
        tnLocation.setLatitude(0);
        tnLocation.setLongitude(0);
        PowerMock.expectPrivate(locationProvider, "getCurrentLocation", 3).andReturn(tnLocation).anyTimes();
        PowerMock.expectPrivate(locationProvider, "getLastKnownLocation", 3).andReturn(tnLocation).anyTimes();
        EasyMock.expect(LocationProvider.getInstance()).andReturn(locationProvider).anyTimes();   
          
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseDriveToParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_createStop_label() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
         MaiTaiParameter.class,"parseCoordinate");
        PowerMock.replayAll();
        Stop from=new Stop();
        String label="lbl";
        Object[] para=new Object[]{label,from};
        Stop stop = Whitebox.invokeMethod(mockMaiTaiParameter, "createStop",para);
        assertEquals(stop.getLabel(), label);
        PowerMock.verifyAll();
    }
    
    public void test_createStop() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
         MaiTaiParameter.class,"parseCoordinate");
        PowerMock.replayAll();
        int[] args=new int[2];
        args[0]=111;
        args[1]=222;
        String label="lbl";
        Object[] para=new Object[]{label, args[0], args[1]};
        Stop stop = Whitebox.invokeMethod(mockMaiTaiParameter, "createStop",para);
        assertEquals(stop.getLabel(), label);
        PowerMock.verifyAll();
    }
    
    
    public void test_parseMapsParams() throws Exception
    {
        Vector markers = new Vector();
        markers.add("1111");
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseLatAndLon","parseMarkers");
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLon", EasyMock.anyObject(Vector.class));
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseMarkers", EasyMock.anyObject(String.class)).andReturn(new Vector()).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "lat", 0);        
        Whitebox.setInternalState(mockMaiTaiParameter, "lon", 0);        
        
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"getValue","getValues");
        Object[] paramValue = new Object[]{ markers, "addr" };       
        Object[] paramValues = new Object[]{ markers, "markers" }; 
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramValue).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValues", paramValues).andReturn(markers).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "markersStops", new Vector());
        
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);        
         
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseMapsParams",markers);
        assertEquals(flag, false);
        PowerMock.verifyAll();
    }
    
    public void test_parseMapsParams_err() throws Exception
    {
        Vector markers = new Vector();
        markers.add("1111");
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseLatAndLon","parseMarkers");
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLon", EasyMock.anyObject(Vector.class));
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseMarkers", EasyMock.anyObject(String.class)).andReturn(new Vector()).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "lat", 0);        
        Whitebox.setInternalState(mockMaiTaiParameter, "lon", 0);        
        
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"getValue","getValues");
        Object[] paramValue = new Object[]{ markers, "addr" };       
        Object[] paramValues = new Object[]{ markers, "markers" }; 
        Object[] paramErr = new Object[]{ markers, "err" }; 
        Object[] paramTerm = new Object[]{ markers, "term" }; 
        Object[] paramLbl = new Object[]{ markers, "lbl" }; 
        
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramValue).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValues", paramValues).andReturn(new Vector()).anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramErr).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramTerm).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramLbl).andReturn("CURRENT").anyTimes();
     
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "markersStops", new Vector());
        
         
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseMapsParams",markers);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseMaker() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "createStop","parseCoordinate");

        PowerMock.mockStatic(LocationProvider.class);
        LocationProvider locationProvider = PowerMock.createPartialMock(LocationProvider.class,"getCurrentLocation");
        TnLocation tnLocation= new TnLocation("11111");
        tnLocation.setLatitude(0);
        tnLocation.setLongitude(0);
        PowerMock.expectPrivate(locationProvider, "getCurrentLocation", 3).andReturn(tnLocation).anyTimes();
        EasyMock.expect(LocationProvider.getInstance()).andReturn(locationProvider).anyTimes();   
        
        Object[] args=new Object[]{"",0,0};
        Stop stop=new Stop();
        PowerMock.expectPrivate(mockMaiTaiParameter, "createStop", args).andReturn(stop).anyTimes();
        
        PowerMock.mockStatic(AbstractDaoManager.class);
        DaoManager daoManager = PowerMock.createMock(DaoManager.class);
        EasyMock.expect(DaoManager.getInstance()).andReturn(daoManager);        
        AddressDao addressDao = PowerMock.createPartialMock(AddressDao.class,"getHomeAddress");
        PowerMock.expectPrivate(addressDao, "getHomeAddress").andReturn(null).anyTimes();
        EasyMock.expect(daoManager.getAddressDao()).andReturn(addressDao);   
        
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);        
        
        int[] coord2=new int[2];
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseCoordinate", EasyMock.anyObject(String.class)).andReturn(coord2).anyTimes();     
        
        PowerMock.replayAll();
        String makers="CURRENT|HOME";
        Vector vector = Whitebox.invokeMethod(mockMaiTaiParameter, "parseMarkers",makers);
        assertEquals(vector.size(), 1);
        PowerMock.verifyAll();

    }
    
    public void test_parseSearchParams_noHomeStop() throws Exception
    {
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseLatAndLon");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] param = new Object[]
        { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("HOME").anyTimes();
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(
            MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam).anyTimes();
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "lat", 0);
        Whitebox.setInternalState(mockMaiTaiParameter, "lon", 0);
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLon",EasyMock.anyObject(Vector.class) )
        .anyTimes();
        
        PowerMock.mockStatic(AbstractDaoManager.class);
        DaoManager daoManager = PowerMock.createMock(DaoManager.class);
        EasyMock.expect(DaoManager.getInstance()).andReturn(daoManager);        
        AddressDao addressDao = PowerMock.createPartialMock(AddressDao.class,"getHomeAddress");
        PowerMock.expectPrivate(addressDao, "getHomeAddress").andReturn(null).anyTimes();
        EasyMock.expect(daoManager.getAddressDao()).andReturn(addressDao);    
        
        
        PowerMock.replayAll();
        Vector validateVector = new Vector();
        validateVector.add("");
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseSearchParams",validateVector);
        assertEquals(flag, false);
        PowerMock.verifyAll();
    }
    
    public void test_parseSearchParams_currentStop() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
        MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseLatAndLon","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramValue = new Object[]{ validateVector, "addr" };   
        Object[] paramNamedAddr= new Object[]{ validateVector, "namedAddr" }; 
        Object[] paramCood= new Object[]{ validateVector, "coord" }; 
        Object[] paramErr = new Object[]{ validateVector, "err" }; 
        Object[] paramTerm = new Object[]{ validateVector, "term" }; 
        Object[] paramCat = new Object[]{ validateVector, "cat" }; 
        
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramValue).andReturn(null).anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramNamedAddr).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCood).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramErr).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramTerm).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCat).andReturn("CURRENT").anyTimes();
  
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        PowerMock.mockStatic(MaiTaiHandler.class);
        MaiTaiHandler mockMaiTaiHandler = PowerMock.createPartialMock(MaiTaiHandler.class, "addResult");
        Object[] addResultParam = new Object[]
        { EasyMock.anyObject(String.class), EasyMock.anyObject(String.class) };
        PowerMock.expectPrivate(mockMaiTaiHandler, "addResult", addResultParam).anyTimes();
        EasyMock.expect(MaiTaiHandler.getInstance()).andReturn(mockMaiTaiHandler).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "lat", 0);
        Whitebox.setInternalState(mockMaiTaiParameter, "lon", 0);
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        Whitebox.setInternalState(mockMaiTaiParameter, "maiTaiHandler", mockMaiTaiHandler);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseLatAndLon",EasyMock.anyObject(Vector.class) )
        .anyTimes();
        int[] coord2=new int[2];
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseCoordinate", EasyMock.anyObject(String.class)).andReturn(coord2).anyTimes();     
   
        
        PowerMock.mockStatic(LocationProvider.class);
        LocationProvider locationProvider = PowerMock.createPartialMock(LocationProvider.class,"getCurrentLocation");
        TnLocation tnLocation= new TnLocation("11111");
        tnLocation.setLatitude(0);
        tnLocation.setLongitude(0);
        PowerMock.expectPrivate(locationProvider, "getCurrentLocation", 3).andReturn(tnLocation).anyTimes();
        EasyMock.expect(LocationProvider.getInstance()).andReturn(locationProvider).anyTimes();   
        
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseSearchParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    
    public void test_parseViewParams() throws Exception
    {
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseLatAndLon","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramValue = new Object[]{ EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };   
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramValue).andReturn(null).anyTimes();     
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        
        PowerMock.replayAll();
        Vector validateVector = new Vector();
        validateVector.add("");
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseViewParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseParams_navToAction() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseNavToParams","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramAddr = new Object[]{ validateVector, "action" };   
        Object[] paramCb= new Object[]{ validateVector, "cb" }; 
        Object[] paramC= new Object[]{ validateVector, "c" }; 
     
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr).andReturn("navTo").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCb).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramC).andReturn("CURRENT").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseNavToParams", validateVector).andReturn(true).anyTimes();     
        
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseParams_directionsAction() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseDirectionsParams","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramAddr = new Object[]{ validateVector, "action" };   
        Object[] paramCb= new Object[]{ validateVector, "cb" }; 
        Object[] paramC= new Object[]{ validateVector, "c" }; 
     
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr).andReturn("directions").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCb).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramC).andReturn("CURRENT").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseDirectionsParams", validateVector).andReturn(true).anyTimes();     
        
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseParams_driveToAction() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseDriveToParams","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramAddr = new Object[]{ validateVector, "action" };   
        Object[] paramCb= new Object[]{ validateVector, "cb" }; 
        Object[] paramC= new Object[]{ validateVector, "c" }; 
     
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr).andReturn("driveTo").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCb).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramC).andReturn("CURRENT").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseDriveToParams", validateVector).andReturn(true).anyTimes();     
        
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseParams_mapAction() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseMapsParams","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramAddr = new Object[]{ validateVector, "action" };   
        Object[] paramCb= new Object[]{ validateVector, "cb" }; 
        Object[] paramC= new Object[]{ validateVector, "c" }; 
     
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr).andReturn("map").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCb).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramC).andReturn("CURRENT").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseMapsParams", validateVector).andReturn(true).anyTimes();     
        
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseParams_searchAction() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseSearchParams","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramAddr = new Object[]{ validateVector, "action" };   
        Object[] paramCb= new Object[]{ validateVector, "cb" }; 
        Object[] paramC= new Object[]{ validateVector, "c" }; 
     
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr).andReturn("search").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCb).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramC).andReturn("CURRENT").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseSearchParams", validateVector).andReturn(true).anyTimes();     
        
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseParams_viewAction() throws Exception
    {
        Vector validateVector = new Vector();
        validateVector.add("");
        
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
            MaiTaiParameter.class, "parseViewParams","parseCoordinate");
        PowerMock.mockStatic(MaiTaiUtil.class);
        MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class, "getValue");
        Object[] paramAddr = new Object[]{ validateVector, "action" };   
        Object[] paramCb= new Object[]{ validateVector, "cb" }; 
        Object[] paramC= new Object[]{ validateVector, "c" }; 
     
        
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramAddr).andReturn("view").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramCb).andReturn("CURRENT").anyTimes();
        PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", paramC).andReturn("CURRENT").anyTimes();
        
        EasyMock.expect(MaiTaiUtil.getInstance()).andReturn(mockMaiTaiUtil).anyTimes();
        Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        PowerMock.expectPrivate(mockMaiTaiParameter, "parseViewParams", validateVector).andReturn(true).anyTimes();     
        
        PowerMock.replayAll();
        boolean flag = Whitebox.invokeMethod(mockMaiTaiParameter, "parseParams",validateVector);
        assertEquals(flag, true);
        PowerMock.verifyAll();
    }
    
    public void test_parseCoordinate() throws Exception
    {
        
         MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(
         MaiTaiParameter.class, "parseViewParams");

        PowerMock.replayAll();
        String strCoord="123123,12312321";
        int[] coord = Whitebox.invokeMethod(mockMaiTaiParameter, "parseCoordinate",strCoord);
        assertEquals(coord.length, 2);
        PowerMock.verifyAll();
    }
    
    public void test_parseLatAndLon() throws Exception
    {
        
     MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseViewParams");

     MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"getValue");
     Object[] param = new Object[] { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
     PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("123123").anyTimes();
     Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        
     PowerMock.replayAll();
     Vector validateVector = new Vector();
     validateVector.add("");
     Whitebox.invokeMethod(mockMaiTaiParameter, "parseLatAndLon",validateVector);
     assertNotNull(mockMaiTaiParameter.getLat());
     PowerMock.verifyAll();
     
    }
    
    public void test_parseLatAndLonOrig() throws Exception
    {
        
     MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseViewParams");

     MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"getValue");
     Object[] param = new Object[] { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
     PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("123123").anyTimes();
     Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        
     PowerMock.replayAll();
     Vector validateVector = new Vector();
     validateVector.add("");
     Whitebox.invokeMethod(mockMaiTaiParameter, "parseLatAndLonOrig",validateVector);
     assertNotNull(mockMaiTaiParameter.getLatOrig());
     PowerMock.verifyAll();
     
    }
    
    public void test_parseLatAndLonDest() throws Exception
    {
        
     MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseViewParams");

     MaiTaiUtil mockMaiTaiUtil = PowerMock.createPartialMock(MaiTaiUtil.class,"getValue");
     Object[] param = new Object[] { EasyMock.anyObject(Vector.class), EasyMock.anyObject(String.class) };
     PowerMock.expectPrivate(mockMaiTaiUtil, "getValue", param).andReturn("123123").anyTimes();
     Whitebox.setInternalState(mockMaiTaiParameter, "util", mockMaiTaiUtil);
        
     PowerMock.replayAll();
     Vector validateVector = new Vector();
     validateVector.add("");
     Whitebox.invokeMethod(mockMaiTaiParameter, "parseLatAndLonDest",validateVector);
     assertNotNull(mockMaiTaiParameter.getLatDest());
     PowerMock.verifyAll();
     
    }
    
    public void test_composeStopAddress() throws Exception
    {
        
     MaiTaiParameter mockMaiTaiParameter = PowerMock.createPartialMock(MaiTaiParameter.class, "parseViewParams");
     PowerMock.replayAll();
     Stop stop= new Stop();
     stop.setFirstLine("firstline");
     stop.setCity("city");
     stop.setProvince("province");
     stop.setPostalCode("postalcode");
     stop.setCountry("country");
     PowerMock.replayAll();
     String result= Whitebox.invokeMethod(mockMaiTaiParameter, "composeStopAddress",stop);
     assertNotNull(result);
     PowerMock.verifyAll();
     
    }
    */
    
}
