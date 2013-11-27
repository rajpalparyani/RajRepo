///**
// *
// * Copyright 2011 TeleNav, Inc. All rights reserved.
// * TestNavigation.java
// *
// */
//package com.telenav.nav;
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
//import com.telenav.nav.event.NavGpsEvent;
//import com.telenav.threadpool.ThreadPool;
//
//import junit.framework.TestCase;
//
///**
// *@author yning
// *@date 2011-6-8
// */
//public class NavigationTest extends TestCase implements INavEngineListener
//{
//    private static String gps_adi =
//		"{3737902,-12201066,0,10,15,true,0}\r\n"+
//        "{3737902,-12201066,0,10,15,true,0}\r\n"+
//        "{3737902,-12201066,11,10,15,true,0}\r\n"+
//        "{3737914,-12201064,101,10,15,true,0}\r\n"+
//        "{3737927,-12201062,145,10,15,true,0}\r\n"+
//        "{3737944,-12201059,190,10,15,true,0}\r\n"+
//        "{3737965,-12201055,235,10,15,true,0}\r\n"+
//        "{3737965,-12201055,235,10,15,true,0}\r\n"+
//        "{3737965,-12201055,235,10,15,true,0}\r\n"+
//        "{3737965,-12201055,235,10,15,true,0}\r\n"+
//        "{3738020,-12201024,78,85,15,true,0}\r\n"+
//        "{3738021,-12201013,123,85,15,true,0}\r\n"+
//        "{3738021,-12201013,123,85,15,true,0}\r\n"+
//        "{3738016,-12200931,0,95,15,true,0}\r\n"+
//        "{3738016,-12200931,0,75,15,true,0}\r\n"+
//        "{3738016,-12200931,0,75,15,true,0}\r\n"+
//        "{3738016,-12200931,0,75,15,true,0}\r\n"+
//        "{3738016,-12200931,0,55,15,true,0}\r\n"+
//        "{3738016,-12200931,0,35,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}\r\n"+
//        "{3738016,-12200931,0,5,15,true,0}"; 
//
//    private static String gps_no =
//    	"{3737890, -12201074, 10, 0, 50, true, 5}";
//    
//    private boolean isNoGps = false;
//    Object gpsMutex = new Object();
//    
//    @Test
//    public void testDeviate()
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
//        Adi adi = new Adi(regainer, NavEngine.RECURING_DELAY, NavEngine.ADI_GPS_RETRY_TIMES, navUtil, navEngineJob);
//        Navigation navigation = new Navigation(regainer, adi, NavEngine.RECURING_DELAY, NavEngine.NAV_GPS_RETRY_TIMES, navUtil,
//                navEngineJob);
//
//        adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
//
//        navigation.init(RouteWrapper.getInstance().getCurrentRouteId(),
//            RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN, NavEngine.getInstance().navDataProvider.getDestination());
//        
//        byte status = navigation.startNavigation();
//        assertEquals(NavEndEvent.STATUS_DEVIATED, status);
//    }
//    
//    @Test
//    public void testStopped()
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
//        Adi adi = new Adi(regainer, NavEngine.RECURING_DELAY, NavEngine.ADI_GPS_RETRY_TIMES, navUtil, navEngineJob);
//        final Navigation navigation = new Navigation(regainer, adi, NavEngine.RECURING_DELAY, NavEngine.NAV_GPS_RETRY_TIMES, navUtil,
//                navEngineJob);
//
//        adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
//
//        navigation.init(RouteWrapper.getInstance().getCurrentRouteId(),
//            RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN, NavEngine.getInstance().navDataProvider.getDestination());
//        
//        Thread t = new Thread()
//        {
//            public void run()
//            {
//                try
//                {
//                    Thread.sleep(1000);
//                }
//                catch (InterruptedException e)
//                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                navigation.cancel();
//            }
//        };
//        t.start();
//        byte status = navigation.startNavigation();
//        assertEquals(NavEndEvent.STATUS_STOPPED, status);
//    }
//    
//    @Test
//    public void testNoGPS()
//    {
//        RouteUtil.prepareRouteData();
//        NavEventJob navEventJob = new NavEventJob();
//        NavEngineJob navEngineJob = new NavEngineJob(navEventJob);
//        NavUtil navUtil = NavDataFactory.getInstance().getNavUtil();
//        Regainer regainer = new Regainer(NavDataFactory.getInstance().getNavUtil().getRegainerRules(), navUtil, navEngineJob);
//
//        GpsProvider gpsProvider = new GpsProvider(gps_no, false);
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
//        Adi adi = new Adi(regainer, NavEngine.RECURING_DELAY, NavEngine.ADI_GPS_RETRY_TIMES, navUtil, navEngineJob);
//        final Navigation navigation = new Navigation(regainer, adi, NavEngine.RECURING_DELAY, NavEngine.NAV_GPS_RETRY_TIMES, navUtil,
//                navEngineJob);
//
//        adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
//
//        navigation.init(RouteWrapper.getInstance().getCurrentRouteId(),
//            RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN, NavEngine.getInstance().navDataProvider.getDestination());
//        
//        ThreadPool eventPool = new ThreadPool(1, false, 0);
//        eventPool.start();
//        eventPool.addJob(navEventJob);
//        NavEngine.getInstance().addListener(this);
//        Thread t = new Thread()
//        {
//            public void run()
//            {
//                navigation.startNavigation();
//            }
//        };
//        t.start();
//        
//        try
//        {
//            synchronized(gpsMutex)
//            {
//                gpsMutex.wait(30000);
//            }
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//        
//        assertTrue(isNoGps);
//    }
//    
//    public void eventUpdate(NavEvent navEvent)
//    {
//        if(navEvent.getEventType() == NavEvent.TYPE_GPS)
//        {
//            NavGpsEvent gpsEvent = (NavGpsEvent)navEvent;
//            int satCount = gpsEvent.getSatelliteCount();
//            if(satCount == -1)
//            {
//                isNoGps = true;
//                synchronized(gpsMutex)
//                {
//                    gpsMutex.notify();
//                }
//            }
//        }
//    }
//}
