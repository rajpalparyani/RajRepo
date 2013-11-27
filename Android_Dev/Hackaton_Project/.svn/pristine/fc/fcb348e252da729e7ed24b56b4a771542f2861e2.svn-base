///**
// *
// * Copyright 2011 TeleNav, Inc. All rights reserved.
// * TestNavEventJob.java
// *
// */
//package com.telenav.nav;
//
//
//import com.telenav.datatypes.nav.NavDataFactory;
//import com.telenav.datatypes.nav.TnNavLocation;
//import com.telenav.nav.event.NavAdiEvent;
//import com.telenav.nav.event.NavAudioEvent;
//import com.telenav.nav.event.NavDeviationEvent;
//import com.telenav.nav.event.NavEndEvent;
//import com.telenav.nav.event.NavEvent;
//import com.telenav.nav.event.NavGpsEvent;
//import com.telenav.nav.event.NavInfoEvent;
//import com.telenav.nav.event.NavRouteChangeEvent;
//import com.telenav.nav.event.NavStartEvent;
//import com.telenav.threadpool.ThreadPool;
//
//import junit.framework.TestCase;
//
///**
// *@author yning
// *@date 2011-6-13
// */
//public class NavEventJobTest extends TestCase implements INavEngineListener
//{
//    int testType;
//    boolean isTestSuccess = false;
//    Object waitMutex = new Object();
//    public void testNavEventJob()
//    {
//        NavEngine.getInstance().addListener(this);
//        
//        ThreadPool threadPool = new ThreadPool(1, false, 0);
//        threadPool.start();
//        NavEventJob navEventJob = new NavEventJob();
//        threadPool.addJob(navEventJob);
//        //--------ADI-----------------
//        testType = NavEvent.TYPE_ADI;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavAdiEvent(null, null));
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//        
//      //--------audio-----------------
//        testType = NavEvent.TYPE_AUDIO;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavAudioEvent(0, 0, 0, (byte)0, 0, 0, true));
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//        
//        //--------deviation-----------------
//        testType = NavEvent.TYPE_DEVIATION;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavDeviationEvent(null, null));
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//        
//        //--------gps-----------------
//        testType = NavEvent.TYPE_GPS;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavGpsEvent(1));
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//        
//        //--------info-----------------
//        testType = NavEvent.TYPE_INFO;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavInfoEvent(new TnNavLocation(""), 0, 0, "", "", 0, 0, 0, 0, "", NavDataFactory.getInstance().createNavState(0), 0, false));
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//        
//      //--------info-----------------
//        testType = NavEvent.TYPE_ROUTE_CHANGE;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavRouteChangeEvent(0));
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//        
//        //--------start-----------------
//        testType = NavEvent.TYPE_START;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavStartEvent());
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//        
//        //--------end-----------------
//        testType = NavEvent.TYPE_END;
//        isTestSuccess = false;
//        postEvent(navEventJob, new NavEndEvent((byte)0));
//        synchronized(waitMutex)
//        {
//            try
//            {
//                waitMutex.wait(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        assertTrue(isTestSuccess);
//    }
//
//    private void postEvent(final NavEventJob navEventJob, NavEvent navEvent)
//    {
//        final NavEvent event = navEvent;
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
//                    e.printStackTrace();
//                }
//                navEventJob.postEvent(event);
//            }
//        };
//        t.start();
//    }
//    
//  
//    public void eventUpdate(NavEvent navEvent)
//    {
//        int type = navEvent.getEventType();
//        if(testType == type)
//        {
//            isTestSuccess = true;
//            synchronized(waitMutex)
//            {
//                waitMutex.notify();
//            }
//        }
//    }
//}
