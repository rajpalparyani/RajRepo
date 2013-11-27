///**
// *
// * Copyright 2011 TeleNav, Inc. All rights reserved.
// * TestNavEngineJob.java
// *
// */
//package com.telenav.nav;
//
//import junit.framework.TestCase;
//
//import org.easymock.EasyMock;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.powermock.api.easymock.PowerMock;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import com.telenav.location.TnLocation;
//import com.telenav.nav.event.NavDeviationEvent;
//import com.telenav.nav.event.NavEndEvent;
//import com.telenav.nav.event.NavStartEvent;
//import com.telenav.util.Queue;
//
///**
// *@author yning
// *@date 2011-6-13
// */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({NavEngine.class})
//public class NavEngineJobTest extends TestCase
//{
//    @Test
//    public void testDeviated()
//    {
//        NavEventJob navEventJob = new NavEventJob();
//        NavEngineJob navEngineJob = new NavEngineJob(navEventJob);
//        
//        Adi adi = PowerMock.createMock(Adi.class);
//        Navigation navigation = PowerMock.createMock(Navigation.class);
//        PowerMock.mockStatic(NavEngine.class);
//        NavEngine navEngine = PowerMock.createMock(NavEngine.class);
//        INavGpsProvider gpsProvider = PowerMock.createMock(INavGpsProvider.class);
//        navEngine.gpsProvider = gpsProvider;
//        INavDataProvider navDataProvider = PowerMock.createMock(INavDataProvider.class);
//        navEngine.navDataProvider = navDataProvider;
//        
//        navEngineJob.adi = adi;
//        navEngineJob.navigation = navigation;
//        
//        adi.init(EasyMock.anyBoolean());
//        EasyMock.expect(NavEngine.getInstance()).andReturn(navEngine).anyTimes();
//        EasyMock.expect(navDataProvider.getDestination()).andReturn("San Francisco");
//        navigation.init(EasyMock.anyInt(), EasyMock.anyBoolean(), EasyMock.anyObject(String.class));
//        EasyMock.expect(navigation.startNavigation()).andReturn(NavEndEvent.STATUS_DEVIATED);
//        EasyMock.expect(gpsProvider.getFixes(EasyMock.anyInt(), EasyMock.anyObject(TnLocation[].class))).andReturn(1).anyTimes();
//        adi.init(EasyMock.anyBoolean());
//        EasyMock.expect(adi.handleAdi()).andReturn(NavEndEvent.STATUS_DEVIATED).anyTimes();
//        EasyMock.expect(navigation.getDeviationCount()).andReturn(new int[]{1, 1});
//        PowerMock.replayAll();
//        
//        navEngineJob.execute(0);
//        
//        PowerMock.verifyAll();
//        
//        Queue eventQueue = navEventJob.eventQueue;
//        Object obj = eventQueue.pop();
//        assertTrue(obj instanceof NavStartEvent);
//        obj = eventQueue.pop();
//        assertTrue(obj instanceof NavDeviationEvent);
//        obj = eventQueue.pop();
//        assertTrue(obj instanceof NavEndEvent);
//    }
//}
