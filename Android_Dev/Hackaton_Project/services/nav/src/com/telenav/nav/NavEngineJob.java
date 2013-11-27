/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavEngineJob.java
 *
 */
package com.telenav.nav;

import com.telenav.datatypes.nav.NavDataFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.NavUtil;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.logger.Logger;
import com.telenav.nav.event.NavDeviationEvent;
import com.telenav.nav.event.NavEndEvent;
import com.telenav.nav.event.NavEvent;
import com.telenav.nav.event.NavStartEvent;
import com.telenav.threadpool.IJob;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-14
 */
class NavEngineJob implements IJob
{
    private boolean isCancelled = false;
    private boolean isRunning = true;
    protected Navigation navigation;
    protected Adi adi;
    protected NavEventJob navEventJob;
    
    public NavEngineJob(NavEventJob navEventJob)
    {
        this.navEventJob = navEventJob;
        
        NavUtil navUtil = NavDataFactory.getInstance().getNavUtil();
        Regainer regainer = new Regainer(NavDataFactory.getInstance().getNavUtil().getRegainerRules(), navUtil, this);

        adi = new Adi(regainer, NavEngine.RECURING_DELAY, NavEngine.ADI_GPS_RETRY_TIMES, navUtil, this);
        this.navigation = new Navigation(regainer, adi, NavEngine.RECURING_DELAY, NavEngine.NAV_GPS_RETRY_TIMES, navUtil, this);
    }
    
    protected void postEvent(NavEvent event)
    {
        this.navEventJob.postEvent(event);
    }
    
    public NavState getCurrentNavState()
    {
        return this.navigation.getNavState();
    }
    
    public void cancel()
    {
        this.isCancelled = true;
        this.navEventJob.cancel();
        this.adi.cancel();
        this.navigation.cancel();
    }

    public void execute(int handlerID)
    {
        isRunning = true;

        try
        {
            run();
        }
        catch (Throwable e)
        {
            this.postEvent(new NavEndEvent(NavEndEvent.STATUS_EXCEPTION));

            Logger.log(this.getClass().getName(), e, "[Navigation] - navigation error.");
        }
        finally
        {
            isRunning = false;
        }
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        return isRunning;
    }
    
    protected void run()
    {
        this.postEvent(new NavStartEvent());

        byte navStatus = NavEndEvent.STATUS_DEVIATED;

        adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
        
        this.navigation.init(RouteWrapper.getInstance().getCurrentRouteId(), RouteWrapper
                .getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN, NavEngine.getInstance().navDataProvider.getDestination());

        while (!isCancelled)
        {
            navStatus = this.navigation.startNavigation();
            if (navStatus == NavEndEvent.STATUS_DEVIATED)
            {
                TnNavLocation[] gpsData = new TnNavLocation[]
                { new TnNavLocation("") };

                boolean hasData = false;
                hasData = (NavEngine.getInstance().gpsProvider.getFixes(1, gpsData) == 1);

                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "hasData = "+hasData);
                }
                if (hasData)
                {
                    adi.init(RouteWrapper.getInstance().getRouteStyle() == Route.ROUTE_PEDESTRIAN);
                    byte adiStatus = adi.handleAdi();
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "adiStatus = "+adiStatus);
                    }
                    if (adiStatus != NavEndEvent.STATUS_ON_TRACK)
                    {
                        if(!isCancelled)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "post NavDeviationEvent");
                            }
                            this.postEvent(new NavDeviationEvent(gpsData[0], navigation.getDeviationCount()));
                        }
                        break;
                    }
                    else
                    {
                        navigation.pauseHeadingTest();
                    }
                }
            }
            else
            {
                break;
            }
        }

        if (!isCancelled)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "post NavEndEvent, status = "+navStatus);
            }
            this.postEvent(new NavEndEvent(navStatus));
        }
    }
}
