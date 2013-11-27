/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * IKontagentLogger.java
 *
 */
package com.telenav.sdk.kontagent;

import com.telenav.res.converter.StringConverter;
/**
 *@author jyxu
 *@date 2013-7-18
 */
public class KontagentAssistLogger
{
    private static long mapDisplayStartTime = -1;
    private static long navManualExitStartTime = -1;
    private static long navDurationStartTime = -1;

    public static void startKtLogMapDisplay()
    {
        mapDisplayStartTime = System.currentTimeMillis();
    }

    public static void endLogMapDisplayTime()
    {
        if(mapDisplayStartTime <= 0)
            return;
        long duration = (System.currentTimeMillis() - mapDisplayStartTime)/1000; //convert to second unit

        ///Level 1: 0 - 20 seconds
        //Level 2: 21- 40 seconds
        //Level 3: 41 - 60 seconds
        //Level 4: 61 - 90 seconds
        //Level 5: 90 - 120 seconds
        //Level 6: > 120 secondsion
        int level = 0;
        if(duration <= 20)
        {
            level = 1;
        }
        else if(duration <= 40)
        {
            level = 2;
        }
        else if(duration <= 60)
        {
            level = 3;
        }
        else if(duration <= 90)
        {
            level = 4;
        }
        else if(duration <= 120)
        {
            level = 5;
        }
        else
        {
            level = 6;
        }
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MAP, KontagentLogger.MAP_TIME_SPENT, level);
        mapDisplayStartTime = -1;
    }
    
    public static void ktLogNavigationDistance(int meters)
    {
        double distanceInMiles = (meters * StringConverter.KM_TO_MILE) / StringConverter.KM_TO_METER;
        int level = 0;
        //Level 1: 0-10 miles
        //Level 2: 11-20 miles
        //Level 3: 21-40 miles
        //Level 4: 41-50 miles
        //Level 5: 51 - 100 miles
        //Level 6: 101-250 miles
        //Level 7: 251-500 miles
        //Level 8: 501-1000 miles
        //Level 9: >1000 miles
        if(distanceInMiles <= 10)
        {
            level = 1;
        }
        else if(distanceInMiles <= 20)
        {
            level = 2;
        }
        else if(distanceInMiles <= 40)
        {
            level = 3;
        }
        else if(distanceInMiles <= 50)
        {
            level = 4;
        }
        else if(distanceInMiles <= 100)
        {
            level = 5;
        }
        else if(distanceInMiles <= 250)
        {
            level = 6;
        }
        else if(distanceInMiles <= 500)
        {
            level = 7;
        }
        else if(distanceInMiles <= 1000)
        {
            level = 8;
        }
        else
        {
            level = 9;
        }
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION, KontagentLogger.NAVIGATION_DISTANCE, level);
    }
    
    public static void startKtLogNavigation()
    {
        navManualExitStartTime = System.currentTimeMillis();
        navDurationStartTime = navManualExitStartTime;
    }
    
    public static void endLogNavigationTimeWhenExit()
    {
        if(navManualExitStartTime < 0) return;
        long minutes = (System.currentTimeMillis() - navManualExitStartTime)/6000; //convert to minutes unit
        //Level 1: 0-2 minutes
        //Level 2: 2-5 minutes
        //Level 3: 5-10 minutes
        //Level 4: 10+ minutes
        int level = 0;
        if(minutes <  2)
        {
            level = 1;
        }
        else if(minutes <  5)
        {
            level = 2;
        }
        else if(minutes <  10)
        {
            level = 3;
        }
        else
        {
            level = 4;
        }
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
            KontagentLogger.NAVIGATION_EXIT_CLICKED, level);
        navManualExitStartTime = -1;
    }
    
    //count navigation duration
    public static void endLogNavigationDuration()
    {
        if(navDurationStartTime < 0) return;
        long minutes = (System.currentTimeMillis() - navDurationStartTime)/6000; //convert to minutes unit
        //Level 1: 0-10 minutes
        //Level 2: 11-20 minutes
        //Level 3: 21-30 minutes
        //Level 4: 31-40 minutes
        //Level 5: 41-50 minutes
        //Level 6: 51-60 minutes
        //Level 7: >60 minutes
        int level =0;
        if(minutes <=  10)
        {
            level = 1;
        }
        else if(minutes <=  20)
        {
            level = 2;
        }
        else if(minutes <=  30)
        {
            level = 3;
        }
        else if(minutes <=  40)
        {
            level = 4;
        }
        else if(minutes <=  50)
        {
            level = 5;
        }
        else if(minutes <=  60)
        {
            level = 6;
        }
        else
        {
            level = 7;
        }
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
            KontagentLogger.NAVIGATION_DURATION, level);
        navDurationStartTime = -1;
    }
    
    public static void ktLogDwfArrivedStatus(double percent)
    {
        int level =0;
        if(percent < 0.25)
        {
            level = 1;
        }
        else if(percent <= 0.5)
        {
            level = 2;
        }
        else if(percent <= 0.75)
        {
            level = 3;
        }
        else
        {
            level = 4;
        }
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DWF,
            KontagentLogger.DWF_PERCENTAGE_ACTIVE, level);
    }
    
    
    public static void ktLogDwfInitialFriendsCount(int size)
    {
        int level = 0;
        //Level 1: Invite sent to only 1 user
        //Level 2: Invite sent to between 2-5 users
        //Level 3: Invite sent to >5 users
        if(size == 1)
        {
            level = 1;
        }
        else if(size >= 2 && size <= 5)
        {
            level = 2;
        }
        else
        {
            level = 3;
        }
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DWF,
            KontagentLogger.DWF_INITIAL_INVITES_SENT, level);
    }
}
