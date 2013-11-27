///**
// *
// * Copyright 2011 TeleNav, Inc. All rights reserved.
// * TestAdi.java
// *
// */
//package com.telenav.nav;
//
//import java.io.BufferedReader;
//import java.io.StringReader;
//
//import junit.framework.TestCase;
//
//import org.easymock.EasyMock;
//import org.junit.Test;
//import org.powermock.api.easymock.PowerMock;
//
//import com.telenav.datatypes.nav.NavDataFactory;
//import com.telenav.datatypes.nav.NavUtil;
//import com.telenav.datatypes.nav.TnNavLocation;
//import com.telenav.datatypes.route.Route;
//import com.telenav.datatypes.route.RouteWrapper;
//import com.telenav.nav.event.NavEndEvent;
//import com.telenav.nav.event.NavEvent;
//import com.telenav.threadpool.ThreadPool;
//
///**
// *@author yning
// *@date 2011-6-9
// */
//public class AdiTest extends TestCase implements INavEngineListener
//{
//    boolean isAdi = false;
//    Object adiWaitMutex = new Object();
//
//    private static String gps_adi =
//    		"{3737902,-12201066,0,10,15,true,0}\r\n"+
//            "{3737902,-12201066,0,10,15,true,0}\r\n"+
//            "{3737902,-12201066,11,10,15,true,0}\r\n"+
//            "{3737914,-12201064,101,10,15,true,0}\r\n"+
//            "{3737927,-12201062,145,10,15,true,0}\r\n"+
//            "{3737944,-12201059,190,10,15,true,0}\r\n"+
//            "{3737965,-12201055,235,10,15,true,0}\r\n"+
//            "{3737965,-12201055,235,10,15,true,0}\r\n"+
//            "{3737965,-12201055,235,10,15,true,0}\r\n"+
//            "{3737965,-12201055,235,10,15,true,0}\r\n"+
//            "{3738020,-12201024,78,85,15,true,0}\r\n"+
//            "{3738021,-12201013,123,85,15,true,0}\r\n"+
//            "{3738021,-12201013,123,85,15,true,0}\r\n"+
//            "{3738016,-12200931,0,95,15,true,0}\r\n"+
//            "{3738016,-12200931,0,75,15,true,0}\r\n"+
//            "{3738016,-12200931,0,75,15,true,0}\r\n"+
//            "{3738016,-12200931,0,75,15,true,0}\r\n"+
//            "{3738016,-12200931,0,55,15,true,0}\r\n"+
//            "{3738016,-12200931,0,35,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}\r\n"+
//            "{3738016,-12200931,0,5,15,true,0}"; 
//    	
//    private static String gps_adi_regain =
//    	"{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,325,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,280,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,275,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,275,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,275,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,275,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,275,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,275,15,true,0}\r\n"+
//        "    {3738016,-12200939,78,275,15,true,0}\r\n"+
//        "    {3738017,-12200949,123,275,15,true,0}\r\n"+
//        "    {3738018,-12200963,134,275,15,true,0}\r\n"+
//        "    {3738024,-12201026,134,275,15,true,0}\r\n"+
//        "    {3738026,-12201044,145,275,15,true,0}\r\n"+
//        "    {3738026,-12201044,145,275,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,275,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,315,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,315,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,315,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,15,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,15,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,15,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,15,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,15,15,true,0}\r\n"+
//        "    {3738026,-12201048,0,15,15,true,0}";
//    
//    private static String gps_adi_deviate =
//    	"    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}\r\n"+
//        "    {3738016,-12200931,0,5,15,true,0}";
//    
//    
//    @Test
//    public void testAdiEvent()
//    {
//        RouteUtil.prepareRouteData();
//        NavEventJob navEventJob = new NavEventJob();
//        NavEngineJob navEngineJob = new NavEngineJob(navEventJob);
//        NavUtil navUtil = NavDataFactory.getInstance().getNavUtil();
//        Regainer regainer = new Regainer(NavDataFactory.getInstance().getNavUtil().getRegainerRules(), navUtil, navEngineJob);
//        
//        GpsProvider gpsProvider = new GpsProvider(gps_adi, true);
//        NavEngine.getInstance().gpsProvider = gpsProvider;
//        NavEngine.getInstance().isArriveDestination = false;
//        
//        //Mock
//        INavDataProvider mockDataProvider = PowerMock.createMock(INavDataProvider.class);
//        NavEngine.getInstance().navDataProvider = mockDataProvider;
//        //end Mock
//        
//        //Mock behavior
//        EasyMock.expect(mockDataProvider.isOnRoad(EasyMock.anyObject(TnNavLocation.class), EasyMock.anyObject(int[].class), EasyMock.anyBoolean())).andReturn(false).anyTimes();
//        EasyMock.expect(mockDataProvider.getDestination()).andReturn("san francisco").anyTimes();
//        PowerMock.replayAll();
//        //end mock behavior
//        
//        ThreadPool eventPool = new ThreadPool(1, false, 0);
//        eventPool.start();
//        eventPool.addJob(navEventJob);
//        NavEngine.getInstance().addListener(this);
//        
//        final Adi adi = new Adi(regainer, NavEngine.RECURING_DELAY, NavEngine.ADI_GPS_RETRY_TIMES, navUtil, navEngineJob);
//        adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
//        
//        Thread t = new Thread()
//        {
//            public void run()
//            {
//                adi.handleAdi();
//            }
//        };
//        t.start();
//        synchronized(adiWaitMutex)
//        {
//            try
//            {
//                adiWaitMutex.wait(20000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isAdi);
//    }
//    
//    public void testAdiRegain()
//    {
//        RouteUtil.prepareRouteData();
//        NavEventJob navEventJob = new NavEventJob();
//        NavEngineJob navEngineJob = new NavEngineJob(navEventJob);
//        NavUtil navUtil = NavDataFactory.getInstance().getNavUtil();
//        Regainer regainer = new Regainer(NavDataFactory.getInstance().getNavUtil().getRegainerRules(), navUtil, navEngineJob);
//        
//        GpsProvider gpsProvider = new GpsProvider(gps_adi_regain, true);
//        NavEngine.getInstance().gpsProvider = gpsProvider;
//        NavEngine.getInstance().isArriveDestination = false;
//        
//        //Mock
//        INavDataProvider mockDataProvider = PowerMock.createMock(INavDataProvider.class);
//        NavEngine.getInstance().navDataProvider = mockDataProvider;
//        //end Mock
//        
//        //Mock behavior
//        EasyMock.expect(mockDataProvider.isOnRoad(EasyMock.anyObject(TnNavLocation.class), EasyMock.anyObject(int[].class), EasyMock.anyBoolean())).andReturn(false).anyTimes();
//        EasyMock.expect(mockDataProvider.getDestination()).andReturn("san francisco").anyTimes();
//        PowerMock.replayAll();
//        //end mock behavior
//        
//        ThreadPool eventPool = new ThreadPool(1, false, 0);
//        eventPool.start();
//        eventPool.addJob(navEventJob);
//        NavEngine.getInstance().addListener(this);
//        
//        final Adi adi = new Adi(regainer, NavEngine.RECURING_DELAY, NavEngine.ADI_GPS_RETRY_TIMES, navUtil, navEngineJob);
//        adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
//        
//        byte status = adi.handleAdi();
//        assertEquals(NavEndEvent.STATUS_ON_TRACK, status);
//    }
//    
//    public void testAdiDeviate()
//    {
//        RouteUtil.prepareRouteData();
//        NavEventJob navEventJob = new NavEventJob();
//        NavEngineJob navEngineJob = new NavEngineJob(navEventJob);
//        NavUtil navUtil = NavDataFactory.getInstance().getNavUtil();
//        Regainer regainer = new Regainer(NavDataFactory.getInstance().getNavUtil().getRegainerRules(), navUtil, navEngineJob);
//        
//        GpsProvider gpsProvider = new GpsProvider(gps_adi_deviate, true);
//        NavEngine.getInstance().gpsProvider = gpsProvider;
//        NavEngine.getInstance().isArriveDestination = false;
//        
//        //Mock
//        INavDataProvider mockDataProvider = PowerMock.createMock(INavDataProvider.class);
//        NavEngine.getInstance().navDataProvider = mockDataProvider;
//        //end Mock
//        
//        //Mock behavior
//        EasyMock.expect(mockDataProvider.isOnRoad(EasyMock.anyObject(TnNavLocation.class), EasyMock.anyObject(int[].class), EasyMock.anyBoolean())).andReturn(false).times(1).andReturn(true).anyTimes();
//        EasyMock.expect(mockDataProvider.getDestination()).andReturn("san francisco").anyTimes();
//        PowerMock.replayAll();
//        //end mock behavior
//        
//        ThreadPool eventPool = new ThreadPool(1, false, 0);
//        eventPool.start();
//        eventPool.addJob(navEventJob);
//        NavEngine.getInstance().addListener(this);
//        
//        final Adi adi = new Adi(regainer, NavEngine.RECURING_DELAY, NavEngine.ADI_GPS_RETRY_TIMES, navUtil, navEngineJob);
//        adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
//        
//        byte status = adi.handleAdi();
//        assertEquals(NavEndEvent.STATUS_DEVIATED, status);
//    }
//    
//    public void eventUpdate(NavEvent navEvent)
//    {
//        int eventType = navEvent.getEventType();
//        if(eventType == NavEvent.TYPE_ADI)
//        {
//            isAdi = true;
//            synchronized(adiWaitMutex)
//            {
//                adiWaitMutex.notify();
//            }
//        }
//    }
//}
