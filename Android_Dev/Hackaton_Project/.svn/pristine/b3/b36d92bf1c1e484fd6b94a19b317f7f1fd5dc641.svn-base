///**
// *
// * Copyright 2011 TeleNav, Inc. All rights reserved.
// * TestNavEngine.java
// *
// */
//package com.telenav.nav;
//
//import com.telenav.datatypes.nav.TnNavLocation;
//import com.telenav.nav.event.NavEvent;
//import com.telenav.nav.event.NavGpsEvent;
//import com.telenav.threadpool.ThreadPool;
//
//import junit.framework.TestCase;
//
///**
// *@author yning
// *@date 2011-6-13
// */
//public class NavEngineTest extends TestCase implements INavEngineListener
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
//    private static String gps_deviate = 
//    	"    {3737890, -12201074}\r\n"+
//        "    {3737910, -12201070}\r\n"+
//        "    {3737930, -12201065}\r\n"+
//        "    {3737950, -12201061}\r\n"+
//        "    {3737970, -12201056}\r\n"+
//        "    {3737990, -12201052}\r\n"+
//        "    {3738010, -12201047}\r\n"+
//        "    {3738030, -12201043}\r\n"+
//        "    {3738050, -12201038}\r\n"+
//        "    {3738070, -12201034}\r\n"+
//        "    {3738090, -12201029}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}\r\n"+
//        "    {3737980, -12200905}";
//    
//    private static String gps_deviation = 
//    	"    {3737863,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737863,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737863,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737863,-12201068,0,20,15,true,0}\r\n"+
//        "    {3737863,-12201068,0,40,15,true,0}\r\n"+
//        "    {3737863,-12201068,0,60,15,true,0}\r\n"+
//        "    {3737863,-12201068,0,80,15,true,0}\r\n"+
//        "    {3737864,-12201063,56,90,15,true,0}\r\n"+
//        "    {3737864,-12201054,101,90,15,true,0}\r\n"+
//        "    {3737864,-12201054,101,90,15,true,0}\r\n"+
//        "    {3737864,-12201054,101,90,15,true,0}\r\n"+
//        "    {3737864,-12201030,123,90,15,true,0}\r\n"+
//        "    {3737864,-12201030,123,90,15,true,0}\r\n"+
//        "    {3737864,-12201021,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200996,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200996,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200996,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200996,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200984,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200971,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200959,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200934,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200922,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200909,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200885,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200875,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200863,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200851,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200826,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200814,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200801,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200789,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200755,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200755,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200740,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200733,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200733,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200733,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200733,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200733,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200733,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200721,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200699,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200699,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200699,123,90,15,true,0}\r\n"+
//        "    {3737864,-12200699,123,90,15,true,0}";
//    
//    private static String gps_nav_change_route =
//    	"    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737875,-12201068,0,0,15,true,0}\r\n"+
//        "    {3737882,-12201068,67,5,15,true,0}\r\n"+
//        "    {3737888,-12201067,67,10,15,true,0}\r\n"+
//        "    {3737895,-12201066,67,5,15,true,0}\r\n"+
//        "    {3737902,-12201065,67,15,15,true,0}\r\n"+
//        "    {3737915,-12201062,67,10,15,true,0}\r\n"+
//        "    {3737921,-12201060,67,10,15,true,0}\r\n"+
//        "    {3737928,-12201059,67,10,15,true,0}\r\n"+
//        "    {3737935,-12201058,67,10,15,true,0}\r\n"+
//        "    {3737941,-12201057,67,10,15,true,0}\r\n"+
//        "    {3737955,-12201055,67,10,15,true,0}\r\n"+
//        "    {3737961,-12201053,67,10,15,true,0}\r\n"+
//        "    {3737968,-12201052,67,10,15,true,0}\r\n"+
//        "    {3737975,-12201051,67,10,15,true,0}\r\n"+
//        "    {3737985,-12201049,0,10,15,true,0}\r\n"+
//        "    {3737985,-12201049,0,10,15,true,0}\r\n"+
//        "    {3737985,-12201049,0,10,15,true,0}\r\n"+
//        "    {3737985,-12201049,0,10,15,true,0}\r\n"+
//        "    {3737986,-12201049,22,10,15,true,0}\r\n"+
//        "    {3737991,-12201048,67,10,15,true,0}\r\n"+
//        "    {3738000,-12201046,112,10,15,true,0}\r\n"+
//        "    {3738010,-12201045,44,10,15,true,0}\r\n"+
//        "    {3738010,-12201045,0,10,15,true,0}\r\n"+
//        "    {3738010,-12201045,0,10,15,true,0}\r\n"+
//        "    {3738010,-12201045,0,10,15,true,0}";
//    
//    private static String gps_no =
//    	"{3737890, -12201074, 10, 0, 50, true, 5}";
//    
//    int testType;
//    Object waitMutex = new Object();
//    boolean isAdiSuccess = false;
//    boolean isAdiRegainSuccess = false;
//    boolean isNoGpsSuccess = false;
//    boolean isDeviationSuccess = false;
//    boolean isRouteChangeSuccess = false;
//    public void testAdiAndRegain()
//    {
//        RouteUtil.prepareRouteData();
//        NavEngine navEngine = NavEngine.getInstance();
//        //prepare parameters
//        ThreadPool threadPool = new ThreadPool(1, false, 0);
//        ThreadPool eventPool = new ThreadPool(1, false, 1);
//        threadPool.start();
//        eventPool.start();
//        
//        //first test Adi
//        testType = NavEvent.TYPE_ADI;
//        isAdiSuccess = false;
//        GpsProvider gpsProvider = new GpsProvider(gps_adi, true);
//        NavDataProvider navDataProvider = new NavDataProvider(false);
//        navEngine.addListener(this);
//        navEngine.start(threadPool, eventPool, gpsProvider, navDataProvider);
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(30000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isAdiSuccess);
//        
//        //then test adi regain
//        testType = NavEvent.TYPE_INFO;
//        isAdiRegainSuccess = false;
//        gpsProvider = new GpsProvider(gps_adi_regain, true);
//        navEngine.gpsProvider = gpsProvider;
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(30000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        
//        assertTrue(isAdiRegainSuccess);
//        
//        navEngine.stop();
//        threadPool.stop();
//        eventPool.stop();
//        
//        waitForNavEngineStop();
//    }
//    
//    public void testNoGps()
//    {
//        RouteUtil.prepareRouteData();
//        NavEngine navEngine = NavEngine.getInstance();
//        //prepare parameters
//        ThreadPool threadPool = new ThreadPool(1, false, 0);
//        ThreadPool eventPool = new ThreadPool(1, false, 1);
//        threadPool.start();
//        eventPool.start();
//        
//        testType = NavEvent.TYPE_GPS;
//        isNoGpsSuccess = false;
//        GpsProvider gpsProvider = new GpsProvider(gps_no, false);
//        NavDataProvider navDataProvider = new NavDataProvider(false);
//        navEngine.addListener(this);
//        navEngine.start(threadPool, eventPool, gpsProvider, navDataProvider);
//        
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(30000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        
//        assertTrue(isNoGpsSuccess);
//        
//        navEngine.stop();
//        threadPool.stop();
//        eventPool.stop();
//        
//        waitForNavEngineStop();
//    }
//    
//    public void testDeviation()
//    {
//        RouteUtil.prepareRouteData();
//        NavEngine navEngine = NavEngine.getInstance();
//        //prepare parameters
//        ThreadPool threadPool = new ThreadPool(1, false, 0);
//        ThreadPool eventPool = new ThreadPool(1, false, 1);
//        threadPool.start();
//        eventPool.start();
//        
//        //first test Adi
//        testType = NavEvent.TYPE_DEVIATION;
//        isDeviationSuccess = false;
//        GpsProvider gpsProvider = new GpsProvider(gps_deviation, true);
//        NavDataProvider navDataProvider = new NavDataProvider(true);
//        navEngine.addListener(this);
//        navEngine.start(threadPool, eventPool, gpsProvider, navDataProvider);
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(30000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isDeviationSuccess);
//        navEngine.stop();
//        threadPool.stop();
//        eventPool.stop();
//        
//        waitForNavEngineStop();
//    }
//    
//    public void testRouteChange()
//    {
//
//        RouteUtil.prepareRouteData();
//        NavEngine navEngine = NavEngine.getInstance();
//        //prepare parameters
//        ThreadPool threadPool = new ThreadPool(1, false, 0);
//        ThreadPool eventPool = new ThreadPool(1, false, 1);
//        threadPool.start();
//        eventPool.start();
//        
//        //test route change
//        testType = NavEvent.TYPE_ROUTE_CHANGE;
//        isRouteChangeSuccess = false;
//        GpsProvider gpsProvider = new GpsProvider(gps_nav_change_route, true);
//        NavDataProvider navDataProvider = new NavDataProvider(true);
//        navEngine.addListener(this);
//        navEngine.start(threadPool, eventPool, gpsProvider, navDataProvider);
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(30000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isRouteChangeSuccess);
//        navEngine.stop();
//        threadPool.stop();
//        eventPool.stop();
//    
//        waitForNavEngineStop();
//    }
//    
//    private void waitForNavEngineStop()
//    {
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(2000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//    
//    class NavDataProvider implements INavDataProvider
//    {
//        boolean isOnRoad;
//        NavDataProvider(boolean isOnRoad)   
//        {
//            this.isOnRoad = isOnRoad;
//        }
//        
//     
//        public String getDestination()
//        {
//            return "san francisco";
//        }
//
//
//        public boolean isOnRoad(TnNavLocation gpsFix, int[] threshold, boolean ignoreHeading)
//        {
//            return isOnRoad;
//        }
//        
//    }
//
//
//    public void eventUpdate(NavEvent navEvent)
//    {
//        int type = navEvent.getEventType();
//        switch(type)
//        {
//            case NavEvent.TYPE_ADI:
//            {
//                if(testType == type)
//                {
//                    isAdiSuccess = true;
//                    synchronized(waitMutex)
//                    {
//                        waitMutex.notify();
//                    }
//                }
//                break;
//            }
//            case NavEvent.TYPE_INFO:
//            {
//                if(testType == type)
//                {
//                    isAdiRegainSuccess = true;
//                    synchronized(waitMutex)
//                    {
//                        waitMutex.notify();
//                    }
//                }
//                break;
//            }
//            case NavEvent.TYPE_GPS:
//            {
//                if(testType == type)
//                {
//                    int satelliteNum = ((NavGpsEvent)navEvent).getSatelliteCount();
//                    if(satelliteNum == -1)
//                    {
//                        isNoGpsSuccess = true;
//                        synchronized(waitMutex)
//                        {
//                            waitMutex.notify();
//                        }
//                    }
//                }
//                break;
//            }
//            case NavEvent.TYPE_DEVIATION:
//            {
//                if(testType == type)
//                {
//                    isDeviationSuccess = true;
//                    synchronized(waitMutex)
//                    {
//                        waitMutex.notify();
//                    }
//                }
//                break;
//            }
//            case NavEvent.TYPE_ROUTE_CHANGE:
//            {
//                if(testType == type)
//                {
//                    isRouteChangeSuccess = true;
//                    synchronized(waitMutex)
//                    {
//                        waitMutex.notify();
//                    }
//                }
//                break;
//            }
//        }
//    }
//}
