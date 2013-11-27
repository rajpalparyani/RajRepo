/*
 *
 * Copyright Televigation Inc. 1999-2004 *
 */
package com.telenav.nav;

import java.util.Vector;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.map.MapUtil;
import com.telenav.datatypes.nav.INavEngineConstants;
import com.telenav.datatypes.nav.NavDataFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.NavUtil;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.logger.Logger;
import com.telenav.nav.event.NavAudioEvent;
import com.telenav.nav.event.NavEndEvent;
import com.telenav.nav.event.NavGpsEvent;
import com.telenav.nav.event.NavInfoEvent;
import com.telenav.nav.event.NavRouteChangeEvent;

/**
 * Copyright 2008 TeleNav Inc. Main navigation algorithm. Responsible for updating UI adapter based on results of the
 * local navigation. It will run until it gets to the destination, some error happens: devigation or GPS error; or user
 * force-exit the navigation.
 * 
 * @version 1.0
 * @author Ruslan Meshenberg
 */
class Navigation
{
    protected int recurringDelay;

    protected int GPS_RETRIES;

    /** flag if we're deviating */
    protected boolean isDeviating;

    protected boolean forceDeviate;

    /** counter of how many consecutive tries we failed to obtain gps signal */
    protected int gpsFailedCount;

    /**
     * the difference between the gps fix and the time update fix, with the time tag of the gps fix
     */
    protected int innovationLat;
    
    protected int innovationLon;
    
    protected int innovationVn;
    
    protected int innovationVe;
    
    protected TnNavLocation lastValidGPS;

    /** our status (position) on the route */
    protected NavState onRoute;

    protected NavState predictOnRoute; // do some prediction for nav map rendering

    protected NavState turnOnRoute; // on route only used for snapping in turn

//    protected Route[] routes4TurnSnapping = new Route[1];

    /** count of the deviations so far */
    protected int deviationCount;

    /** individual diviation count (head, pos, dev) so far */
    protected int[] devCounter = new int[3];

    /** timeupdate to measurement update vector */
    protected int[] routeToMeas;

    /** head of shape point to the head of next shape point vector */
    protected int[] headToHead;

    /** fake deltat for the time when we dont have new measurement */
    protected int FAKE_DELTAT;

    protected long lastGpsLocalTimeStamp;

    /** accumulated range, used to detect if we walk backards on a route too far */
    protected int accumulatedRange;

    protected int lastRange;

    protected Regainer regainer;

    protected Adi adi;

    protected int lastRegainerStatus = Regainer.UNINITIALIZED;

    /** debug string */
    protected static final String loginfo = "com.telenav.nav.Navigation";

    protected static final String loginfoADI = "com.telenav.nav.ADITrace";

    protected boolean isPedestrian;

    protected String destLine;

    protected int[] thresholdRules;

    protected int[] devTuningRules;

    protected int[][] kTables;
    protected NavUtil navUtil;
    protected boolean isCancelled;
    protected NavEngineJob navEngineJob;
    protected boolean isMergeContinue;
    
    protected boolean pauseHeadingTest = false;
    protected int ignoreHeadingCounter = 0;
    protected static final int IGNORE_HEADING_MAX_COUNT = 6;

    /** constructor */
    protected Navigation(Regainer regainer, Adi adi, int recurringDelay, int gpsRetries, NavUtil navUtil, NavEngineJob navEngineJob)
    {
        this.recurringDelay = recurringDelay;
        this.FAKE_DELTAT = recurringDelay / 10;
        this.GPS_RETRIES = gpsRetries;

        this.navUtil = navUtil;

        this.thresholdRules = this.navUtil.getThresholdRules();
        this.devTuningRules = this.navUtil.getDevTuningRules();
        this.kTables = this.navUtil.getKTables();

        if (Logger.DEBUG)
        {
            StringBuffer s = new StringBuffer();
            for (int i=0; i<this.thresholdRules.length; i++)
                s.append(this.thresholdRules[i] + ", ");
            Logger.log(Logger.INFO, this.getClass().getName(), "start Navigation with thresholdRules: " + s.toString());
            
            s = new StringBuffer();
            for (int i=0; i<this.devTuningRules.length; i++)
                s.append(this.devTuningRules[i] + ", ");
            Logger.log(Logger.INFO, this.getClass().getName(), "start Navigation with devTuningRules: " + s.toString());
        }
        
        this.regainer = regainer;
        this.adi = adi;
        this.navEngineJob = navEngineJob;
        //XXX keep merge continue consistent during navigation life cycle.
        this.isMergeContinue = NavEngine.isMergeContinue;
    }

    void cancel()
    {
        isCancelled = true;

    }

    /**
     * initializes navcore for navigation
     */
    private void initForNav()
    {
        // convert data into absolute values
        // trace.trace("transforming new route", this.loginfo);
        // nomRoute = NominalRoute.transformNewData(nomRoute);
        // Log.log("----------- nominal route: ==============\n" + nomRoute, Log.DEBUG, this.loginfo);
        this.gpsFailedCount = 0;

        resetDeviationCounters();

        // init the vectors
        routeToMeas = new int[3];
        headToHead = new int[3];

        // reset accumulated range
        accumulatedRange = 0;
        lastRange = -1;

    } // initForNav

    protected NavState getNavState()
    {
        return this.onRoute;
    }

    /**
     * initialize route for navigation
     * 
     * @param nomRoute [in] nominal route
     */
    private void initRoute(int routeId)
    {
        initForNav();

        this.onRoute = NavDataFactory.getInstance().createNavState(routeId);
//        System.out.println("----onRoute: " + this.onRoute);
        Route route = RouteWrapper.getInstance().getRoute(this.onRoute.getRoute());
        this.predictOnRoute = NavDataFactory.getInstance().createNavState(RouteWrapper.ROUTE_NONE);
        this.turnOnRoute = NavDataFactory.getInstance().createNavState(RouteWrapper.ROUTE_NONE);

        TnNavLocation currPos = onRoute.getPosition();

        this.lastValidGPS = null;
        
        Logger.log(Logger.INFO, "nav", "Navigation :: initRoute :: " + System.currentTimeMillis());

        Segment seg = route.segmentAt(0);

        if (seg.edgesSize() > 0)
        {
            RouteEdge edge = seg.getEdge(0);
            int lat = edge.latAt(0);
            int lon = edge.lonAt(0);

            currPos.setLatitude(lat);
            currPos.setLongitude(lon);
            
            long serverOriginTimestamp = route.getOriginTimeStamp();
            if (serverOriginTimestamp * 10 > System.currentTimeMillis() + 5 * 60 * 1000 ||
                serverOriginTimestamp * 10 < System.currentTimeMillis() - 5 * 60 * 1000)
            {
                currPos.setTime(System.currentTimeMillis()/10);
            }
            else
            {
                currPos.setTime(serverOriginTimestamp);
            }
            
            Logger.log(Logger.INFO, "nav", "server origin timeStamp = " + serverOriginTimestamp);
        }

        currPos.setHeading(DataUtil.bearing(route.getOriginVn(), route.getOriginVe()));
        
        int serverSpeed = DataUtil.distance(route.getOriginVn(), route.getOriginVe()); 
        if (serverSpeed < 800) currPos.setSpeed(serverSpeed);
        
        Logger.log(Logger.INFO, "nav", "server speed = " + serverSpeed);

    } // initRoute

    protected void init(int routeId, boolean isPedestrian, String destLine)
    {
        this.isPedestrian = isPedestrian;
        this.destLine = destLine;
        initRoute(routeId);
    }

    /**
     * start the local nav loop
     * 
     * @param nominal route
     */
    protected byte startNavigation()
    {
        resetDeviationCounters();
        accumulatedRange = 0;
        lastRange = -1;

        this.kTables = this.navUtil.getKTables();

        byte returnStatus = NavEndEvent.STATUS_ERROR;
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "start navigation, currentMode=ADI_OFF_ROUTE");
        }
        
        if (RouteWrapper.getInstance().getRoutes() != null)
        {
            try
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "nav,enter");
                }

                returnStatus = continueNavigation();

                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "nav,done: " + returnStatus);
                }
            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        else
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "nav,route NULL");
        }

        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "exiting startNavigation()");
        }

        return returnStatus;
    }

    private byte continueNavigation()
    {
        NavEngine.getInstance().isArriveDestination = false;
        byte returnStatus = NavEndEvent.STATUS_ERROR;

        try
        {
            TnNavLocation[] gpsData = new TnNavLocation[]
            { new TnNavLocation("") };

            boolean hasData = false;
            TnNavLocation gpsfix = null;

            hasData = (NavEngine.getInstance().getGpsProvider().getFixes(1, gpsData) == 1);

            if (hasData)
            {
                // Guoyuan: During navigation, should use encrypted GPS fix to judge
                // Because route is encrypted.
                // By the time below code is executed
                // if (navController.gpsEncrypter != null)
                // navController.gpsEncrypter.getEncFixes(1, gpsData);

                gpsfix = gpsData[0];
            }

            boolean useOriginOnRoute = false;
            if (hasData)
            {
                lastGpsLocalTimeStamp = gpsfix.getLocalTimeStamp();

                int regainStatus = this.regainer.regain(gpsfix, this.onRoute, false, false, true);
                
                // start of new code to avoid machine gun deviation
                if (regainStatus != Regainer.SNAP_TO_SUCCESS)
                {
                    try
                    {
                        if (adi.checkFindMeOnTile(gpsfix) == NavEndEvent.STATUS_DEVIATED)
                        {
                            // machine gun deviation will occur, use default onRoute to navigate
                            regainStatus = Regainer.SNAP_TO_SUCCESS;
                            onRoute.getPosition().setTime(gpsfix.getTime());
                            useOriginOnRoute = true;
                        }
                    }
                    catch (Throwable e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }
                // end of new code to avoid machine gun deviation

                if (regainStatus != Regainer.SNAP_TO_SUCCESS)
                {
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "Deviation immediately after the route");
                    }
                    return NavEndEvent.STATUS_DEVIATED;
                }
                else
                {
                    if (!useOriginOnRoute)
                    {
                        this.onRoute = this.regainer.getOnRoute();
                    }
                }
            }

            while (!isCancelled)
            {
                try
                {
                    long startTime = System.currentTimeMillis();
                    hasData = (NavEngine.getInstance().getGpsProvider().getFixes(1, gpsData) == 1);

                    gpsfix = null;
                    if (hasData)
                    {
                        // Guoyuan: During navigation, should use encrypted GPS fix to judge
                        // Because route is encrypted.
                        // if (navController.gpsEncrypter != null)
                        // {
                        // navController.gpsEncrypter.getEncFixes(1, gpsData);
                        // }
                        gpsfix = gpsData[0];
                    }

                    byte status = navigate(gpsfix);
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "nav status returned = "+status);
                    }

                    if (NavEndEvent.STATUS_ON_TRACK != status && NavEndEvent.STATUS_NO_GPS != status)
                    {
                        returnStatus = status;
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "nav,break2:" + returnStatus);
                        }
                        break; // get out of while loop
                    } // if

                    if (NavEngine.getInstance().isArriveDestination && thresholdRules.length > Route.RULE_DEV_GEOFENCE_RADIUS)// fix
                                                                                               // java.lang.ArrayIndexOutOfBoundsException
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "check for geofence");
                        }
                        int geofence = thresholdRules[Route.RULE_DEV_GEOFENCE_RADIUS];
                        geofence = Math.max(geofence, onRoute.getCurrentSegment().getThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION));
                        int lat = onRoute.getPosition().getLatitude();
                        int lon = onRoute.getPosition().getLongitude();
                        if (gpsfix != null
                                && DataUtil.gpsDistance(lat - gpsfix.getLatitude(), lon - gpsfix.getLongitude(), onRoute.getCurrentSegment()
                                        .getCosLat()) > geofence)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "out of geofence, deviating");
                            }
                            forceDeviate = true;
                            NavEngine.getInstance().isArriveDestination = false;
                        }
                    }
                    else if (onRoute.isAtTheEndOfRoute())
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "arrived destination");
                        }
                        NavEngine.getInstance().isArriveDestination = true;
                    }

                    long delta = System.currentTimeMillis() - startTime;
                    delta = (delta < recurringDelay ? recurringDelay - delta : 0);

                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "Navigation, delta = "+delta);
                    }
                    if (delta > 0)
                    {
                        DataUtil.sleep(delta);
                    }
                }
                catch (Throwable e)
                {
                    // handle it ...
                    Logger.log(this.getClass().getName(), e);
                    returnStatus = NavEndEvent.STATUS_DEVIATED; //trigger deviation to recover the exception
                    break;
                }
            }
            
            if (isCancelled)
            {
                returnStatus = NavEndEvent.STATUS_STOPPED;
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "nav,break1:" + returnStatus);
                }
            }
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
            returnStatus = NavEndEvent.STATUS_ERROR;
        }
        finally
        {
        }

        return returnStatus;
    }

    /**
     * reset deviation counters and flags
     */
    private void resetDeviationCounters()
    {
        // reset the counters
        devCounter[INavEngineConstants.DEV_HEAD_COUNT] = 0;
        devCounter[INavEngineConstants.DEV_POS_COUNT] = 0;
        devCounter[INavEngineConstants.DEV_VEL_COUNT] = 0;
        isDeviating = false; // always reset on new data
    }

    /**
     * 
     * do the recurring process of updating the latest snap-fix
     * 
     * 
     */
    private byte navigate(TnNavLocation gpsfix) throws Exception
    {
        int deltat = FAKE_DELTAT;

        int[] rules = this.navUtil.getThresholdRules();

        boolean needTimeUpdate = true;

        Vector routes = RouteWrapper.getInstance().getRoutes();
        Route route = RouteWrapper.getInstance().getRoute(this.onRoute.getRoute());
        
        if (gpsfix != null)
        {
            // find deltat as difference from gps fix and onroute fix time
            // NOTE: first time around, the time tag of onRoute comes from backend.
            deltat = ((int) (gpsfix.getTime() - onRoute.getPosition().getTime()));

            if (deltat < 0)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "deltat < 0");
                if (onRoute.getPosition().getTime() == route.getOriginTimeStamp())
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "time equals");
                    deltat = FAKE_DELTAT; // make sure we go into "good gpsfix" here
                }
                else
                {
                    deltat = 0; // make sure we fall into "no gps" case
                    if (lastGpsLocalTimeStamp != 0 && lastGpsLocalTimeStamp < gpsfix.getLocalTimeStamp())
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "set it to current real time");
                        // there is some problem in time calculation
                        // set it to current real time
                        needTimeUpdate = false;
                        this.onRoute.getPosition().setTime(gpsfix.getTime() - 1);
                        deltat = 1;
                    }
                }
            }

            lastGpsLocalTimeStamp = gpsfix.getLocalTimeStamp();
        } // if

        if (gpsfix == null || deltat == 0)
        {
            // RM - 04/12/04 - make sure deltat is not 0 !!!
            if (deltat == 0)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "Deltat == 0 !!!");
                }
                deltat = FAKE_DELTAT;
            }
            else
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "gpsfix = null !!!");
                }
            }

            gpsFailedCount++;
            // check if the count is greater then some number and
            // then display error message

            // also need to check i88 gps error codes here !!!
//            if (!NavEngine.getInstance().getGpsProvider().isGpsAvailable())
//            {
                // trace.trace("returning gps errors", loginfo);
                // RM: here we're ok with GPS_ERROR, since GPSReader
                // gives us more information about specific GPS error

//                return NavEndEvent.STATUS_GPS_ERROR;
//            }

            Logger.log(Logger.INFO, this.getClass().getName(), "gpsFailedCount = "+gpsFailedCount);
            if (gpsFailedCount >= GPS_RETRIES)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "post no gps event");
                if(!isCancelled)
                {
                    this.navEngineJob.postEvent(new NavGpsEvent(-1));
                }
                
                int gpsSpeed = 0;
                if (gpsfix != null)
                {
                    gpsSpeed = gpsfix.getSpeed();
                }
                navUpdateUI(this.onRoute, gpsSpeed, deltat, gpsfix, this.lastValidGPS);
                return NavEndEvent.STATUS_NO_GPS;
            }

            // NOTE: we dont want coasting, so use deltat for timeUpdate
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "do time update without valid GPS");
            }
            doTimeUpdate(deltat);
            TnNavLocation d = onRoute.getPosition();
            d.setTime(d.getTime() + deltat);

            // return; DONT RETURN, just update count
        }
        else
        // good gps fix
        {
            this.lastValidGPS = gpsfix;

            if(!isCancelled)
            {
                this.navEngineJob.postEvent(new NavGpsEvent(gpsfix.getSatellites()));
            }
            
            gpsFailedCount = 0;// reset the counter

            {
                byte roadType = DataUtil.RAMP;
                Segment currentSegment = onRoute.getCurrentSegment();
                if (currentSegment != null)
                {
                    roadType = currentSegment.getRoadType();
                }
                boolean isRamp = (roadType == DataUtil.RAMP || roadType == DataUtil.HIGHWAYRAMP)
                        && (devTuningRules[INavEngineConstants.RULE_USE_HEADING_FOR_RAMPS] == 0);

                if (needTimeUpdate)
                {
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "do time update");
                    }
                    doTimeUpdate(deltat);
                }
                onRoute.getPosition().setTime(gpsfix.getTime()); // DONT USE += DELTAT HERE !!!

                this.navUtil.adjustDeviationThresholds(gpsfix, isRamp, onRoute);

                if (!NavEngine.getInstance().isArriveDestination)
                {
                    if (gpsfix.getSpeed() >= gpsfix.getMinUsableSpeed())
                    {
                        boolean needHeadingTest = checkRegainFromAdi(gpsfix);
                        if (needHeadingTest)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "do heading test");
                            }
                            doHeadingTest(gpsfix.getHeading(), isRamp, gpsfix);
                        }
                    }
                    else
                    {
                        // walk down the heading dev count, since we're stopped.
                        devCounter[INavEngineConstants.DEV_HEAD_COUNT] = Math.max(0, devCounter[INavEngineConstants.DEV_HEAD_COUNT] - 1);
                    }
                    // ------------------- innovation -------------------

                    // innovation.xxx = gpsfix.xxx - timeUpdate.xxx:
                    TnNavLocation currPos = onRoute.getPosition();
                    innovationLat = gpsfix.getLatitude() - currPos.getLatitude();
                    innovationLon = gpsfix.getLongitude() - currPos.getLongitude();
                    
                    innovationVn = getVelocityNorth(gpsfix.getSpeed(), gpsfix.getHeading()) -
                                      getVelocityNorth(currPos.getSpeed(), currPos.getHeading());
                    innovationVe = getVelocityEast(gpsfix.getSpeed(), gpsfix.getHeading()) - 
                                      getVelocityEast(currPos.getSpeed(), currPos.getHeading());
                    
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "do meas update");
                    }
                    doMeasUpdate(deltat, gpsfix, isRamp);

                    isDeviating = isDeviating || (devCounter[INavEngineConstants.DEV_HEAD_COUNT] > rules[Route.RULE_HEAD_DEV_COUNT]);

                    // force deviate checking if current route is generated locally by SPT and
                    // last route of the routes is nominal route and thus its route ID is greater than 0
                    forceDeviate = forceDeviate
                            || (onRoute.getPosition() != null
                                    && onRoute.getPosition().getSpeed() > (onRoute.getPosition()).getMinUsableSpeed()
                                    && route != null && route.isLocallyGenerated()
                                    && onRoute.getCurrentEdge() != null && !onRoute.getCurrentEdge().isLocallyGenerated() && routes != null
                                    && routes.size() > 1 && !((Route)routes.elementAt(routes.size() - 1)).isLocallyGenerated());

                    // if position or heading over REGAIN_DEVIATION_COUNT, try to regain early for
                    // multiroutes. Of if actual deviation past MAX_DEVIATION_COUNT, try to regain
                    if ((isDeviating) || forceDeviate
                            || (devCounter[INavEngineConstants.DEV_HEAD_COUNT] > rules[Route.RULE_HEAD_DEV_REGAIN_COUNT])
                            || (devCounter[INavEngineConstants.DEV_POS_COUNT] > rules[Route.RULE_POS_DEV_REGAIN_COUNT])
                            || (devCounter[INavEngineConstants.DEV_VEL_COUNT] > rules[Route.RULE_HEAD_DEV_REGAIN_COUNT]))
                    {
                        forceDeviate = false;
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "deviated, attempting to regain...");
                        }

                        this.lastRegainerStatus = this.regainer.regain(gpsfix, this.onRoute, false, false, false);
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "nav,regainer:" + lastRegainerStatus);
                        }
                        if (Regainer.SNAP_TO_SUCCESS == this.lastRegainerStatus)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "regain success 1");
                            }

                            boolean routeChanged = route.getRouteID() != this.regainer.getOnRoute().getRoute();
                            if (route.isLocallyGenerated()
                                    && !RouteWrapper.getInstance().getRoute(this.regainer.getOnRoute().getRoute()).isLocallyGenerated())
                            {
                                // route is changed from local generated route to original nominal route
                                routeChanged = false;
                            }

                            this.onRoute = this.regainer.getOnRoute();
                            this.lastRegainerStatus = Regainer.UNINITIALIZED;
                            resetDeviationCounters();
                            accumulatedRange = 0;
                            lastRange = -1;

                            if (routeChanged)
                            {
                                if(!isCancelled)
                                {
                                    this.navEngineJob.postEvent(new NavRouteChangeEvent(this.onRoute.getRoute()));
                                }
                            }

                        } // if
                        else
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "deviated, exiting nav loop");
                                Logger.log(Logger.INFO, this.getClass().getName(), "devCounters: "+devCounter[0]+", "+devCounter[1]+", "+devCounter[2]);
                            }
                            return NavEndEvent.STATUS_DEVIATED;
                        }
                    } // if

                    // check for walk back condition
                    // calculate the accumulated range
                    int nCurrRange = onRoute.calcDistanceFromHead();

                    // if last range < 0, this is the first accumulation
                    if (lastRange < 0)
                    {
                        accumulatedRange = 0;
                    }
                    else
                    {
                        accumulatedRange += nCurrRange - lastRange;
                    }

                    // acccumulated range goes back to positive, reset it
                    if (accumulatedRange > 0)
                    {
                        accumulatedRange = 0;
                    }

                    // if accumulated range is more than tolerence deviate
                    if (Math.abs(accumulatedRange) > route.getWalkBackTolerance())
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(),
                                "walkback tolerence exceeded, switching nav mode to ADI wrong way");
                        }

                        isDeviating = true;
                        return NavEndEvent.STATUS_DEVIATED;
                    } // if

                    // save current range
                    lastRange = nCurrRange;

                }
            }
        }

        // do this in case gpsfix is null:
        int gpsSpeed = 0;

        if (gpsfix != null)
        {
            gpsSpeed = gpsfix.getSpeed();
        }

        if (!isCancelled) // we could have been disabled from outside here
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "navUpdateUI");
            }
            navUpdateUI(onRoute, gpsSpeed, deltat, gpsfix, this.lastValidGPS);
        }

        return NavEndEvent.STATUS_ON_TRACK;
    }
    
    /**
     * Check whether we should do heading test.
     * If we are just regained from ADI mode, it's not safe to do heading test immediately.
     * Because when the car still has some distance to the route, the status has already become ON_TRACK.
     * In this case, if the car still drive towards the route, the heading test will fail.
     * And at that time, the car is very near the route. In the judgment in Adi.java, isOnRoad() is true. This will trigger deviation.
     * Machine-gun deviation is possible in this case.
     * In two situations we will recover heading test:
     * 1. The intersection angle between car heading and route heading is within one of below range:
     * [0, 60] [120, 240] [300, 360)
     * 2. After 6 navigate cycle.
     * @param gpsfix
     * @return
     */
    protected boolean checkRegainFromAdi(TnNavLocation gpsfix)
    {
        boolean needHeadingTest = true;
        if (pauseHeadingTest)
        {
            needHeadingTest = false;
            if (ignoreHeadingCounter < IGNORE_HEADING_MAX_COUNT)
            {
                int currentLat = onRoute.getCurrentShapePointLat();
                int currentLon = onRoute.getCurrentShapePointLon();
                int nextLat = onRoute.nextLat();
                int nextLon = onRoute.nextLon();

                int routeHeading = DataUtil.bearing(currentLat, currentLon, nextLat,
                    nextLon);
                int currentHeading = gpsfix.getHeading();

                currentHeading = currentHeading % 360;
                if (currentHeading < 0)
                {
                    currentHeading += 360;
                }

                int deltaHeading = currentHeading - routeHeading;
                if (deltaHeading < 0)
                {
                    deltaHeading += 360;
                }

                if (deltaHeading >= 0 && deltaHeading <= 60)
                {
                    needHeadingTest = true;
                }

                if (deltaHeading >= 120 && deltaHeading <= 240)
                {
                    needHeadingTest = true;
                }

                if (deltaHeading >= 300 && deltaHeading < 360)
                {
                    needHeadingTest = true;
                }
            }
            else
            {
                needHeadingTest = true;
            }

            if (needHeadingTest)
            {
                pauseHeadingTest = false;
                ignoreHeadingCounter = 0;
                Logger.log(Logger.INFO, this.getClass().getName(), "recover heading test");
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "ignore heading test");
                ignoreHeadingCounter++;
            }
        }

        return needHeadingTest;
    }

    protected void navUpdateUI(NavState onRoute, int gpspeed, int deltat, TnNavLocation gpsfix, TnNavLocation lastValidGPS)
            throws Exception
    {
        Route route = RouteWrapper.getInstance().getRoute(onRoute.getRoute());
        int distanceToTurn;
        int nonProjDistToTurn = -onRoute.getCurrentRange();
        int mergedSegLength = 0;
        boolean needDelayAudio = false;

        // current onRoute segment
        Segment currSeg = onRoute.getCurrentSegment();
        
        //For the segment which road type is intersection and turn type is continue,
        //we will delay the audio
        //http://jira.telenav.com:8080/browse/TNANDROID-2914
        if (MapUtil.convertRoadType(currSeg.getRoadType()) == DataUtil.INTERSECTION
                && currSeg.getTurnType() == Route.TURN_TYPE_CONTINUE)
        {
            needDelayAudio = true;
        }

        // current position of onRoute
        TnNavLocation onRoutePos = onRoute.getPosition();

        // calculate the distance traversed in this route segment
        distanceToTurn = nonProjDistToTurn - (onRoutePos == null ? 0 : (onRoutePos.getSpeed() >> 2)); // projecting ahead 2.5 secs
        int remainingDist = onRoute.getDistanceToTurn();

        distanceToTurn += remainingDist;
        nonProjDistToTurn += remainingDist;

        int currentSegDistanceToTurn = distanceToTurn;
        Segment lastContinueSegment = onRoute.getCurrentSegment();

        if (lastContinueSegment.getCalculatedLength() > 0)
            mergedSegLength += lastContinueSegment.getCalculatedLength();
        else
            mergedSegLength += lastContinueSegment.getLength();

        int lastContinueSegmentIndex = onRoute.getSegmentIndex();
        int lastTurnType = lastContinueSegment.getTurnType();
        if (isMergeContinue)
        {
            if (lastTurnType == Route.TURN_TYPE_CONTINUE)
            {
                // merge continue
                Route rp = route;
                for (int i = onRoute.getSegmentIndex() + 1; i < rp.segmentsSize(); i++)
                {
                    // don't merge continue if it is L2H
                    if (lastContinueSegment.getRoadType() != DataUtil.HIGHWAY && rp.segmentAt(i).getRoadType() == DataUtil.HIGHWAY)
                    {
                        break;
                    }

                    lastContinueSegment = rp.segmentAt(i);
                    lastContinueSegmentIndex = i;
                    lastTurnType = lastContinueSegment.getTurnType();
                    if (rp.segmentAt(i).getCalculatedLength() > 0)
                    {
                        distanceToTurn += rp.segmentAt(i).getCalculatedLength();
                        nonProjDistToTurn += rp.segmentAt(i).getCalculatedLength();
                        mergedSegLength += rp.segmentAt(i).getCalculatedLength();
                    }
                    else
                    {
                        distanceToTurn += rp.segmentAt(i).getLength();
                        nonProjDistToTurn += rp.segmentAt(i).getLength();
                        mergedSegLength += rp.segmentAt(i).getLength();
                    }
                    if (lastTurnType != Route.TURN_TYPE_CONTINUE)
                    {
                        break;
                    }
                }// end for
            }// end if
        }
        distanceToTurn = Math.max(distanceToTurn, 0);

        int heading = NavEngine.getInstance().getGpsProvider().getLastKnownHeading();
        while (heading >= 360)
            heading -= 360;
        while (heading < 0)
            heading += 360;
        int newHeading = heading;

        long distanceToDest;
        String tempNextStreetName1;
        String tempNextStreetName2 = null;
        String tempCurrStreetName;

        int distToTurnRaw = distanceToTurn;
        distanceToDest = onRoute.getCurrentSegment().getDistanceToDest() - onRoute.getCurrentSegment().getLength()
                + currentSegDistanceToTurn;
        distanceToDest = Math.max(distanceToDest, 0);

        // calculate ETA
        // Guoyuan for bug 13671. Force ETA to 0 if distanceToDest is 0
        int eta = 0;
        if (distanceToDest > 0)
        {
            eta = route.calcETA(onRoute.getSegmentIndex(), onRoute.getEdgeIndex(), onRoute.getPointIndex(), onRoute.getRange(),
                isPedestrian, Route.PEDESTRIAN_SPEED);
        }

        // snapping while approaching a turn
        boolean isInTurnStatus = false;
        if (onRoute.getCurrentSegment() != null && onRoutePos != null && 
        	onRoute.getCurrentSegment().getTurnType() != Route.TURN_TYPE_CONTINUE &&
            nonProjDistToTurn <= (onRoutePos.getSpeed() >> 2) && 
            gpspeed >= gpsfix.getMinUsableSpeed() &&
            !(onRoute.getSegmentIndex() == 0 &&     
              onRoute.getCurrentSegment() != null && 
              onRoute.getCurrentSegment().getLength() < 200))
        {
            isInTurnStatus = true;

            this.turnOnRoute.set(onRoute);
            int regainStatus = regainer.regain(gpsfix, turnOnRoute, false, false, false);

            if (regainStatus == NavEndEvent.STATUS_ON_TRACK && turnOnRoute.getSegmentIndex() != this.onRoute.getSegmentIndex()
                    && turnOnRoute.getCurrentSegment().getRoadType() != DataUtil.RAMP)
            {
                // GPS fix should be snapped to next segment
                this.onRoute.set(turnOnRoute);
                onRoute.getPosition().setTime(gpsfix.getTime());

                navUpdateUI(onRoute, gpspeed, deltat, gpsfix, lastValidGPS);
                return;

            }// end if
        }// end if

        predictOnRoute.set(onRoute);
        if (isInTurnStatus)
        {
            // rotate map at turn point according to current GPS heading
            predictOnRoute.walkRouteFromCurrentShapePoint(predictOnRoute.getRange() + nonProjDistToTurn - 1);
        }
        else
        {
            // predict ahead 2.5 seconds
            predictOnRoute.walkRouteFromCurrentShapePoint(predictOnRoute.getRange() + (onRoutePos.getSpeed() >> 2));
        }

        TnNavLocation predictPos = predictOnRoute.getPosition();
        int snappedHeading = 0;
        try
        {
            snappedHeading = NavUtil.calcEdgeHeading(predictOnRoute.getCurrentEdge(), predictOnRoute.getPointIndex());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (isInTurnStatus)
        {
            snappedHeading = gpsfix.getHeading();
        }

        TnNavLocation snappedGps = new TnNavLocation("");
        snappedGps.setTime(predictPos.getTime());
        snappedGps.setLatitude(predictPos.getLatitude());
        snappedGps.setLongitude(predictPos.getLongitude());
        snappedGps.setHeading(snappedHeading);

//        boolean showContinue = false;
        boolean playPrep = false;
        boolean playAction = false;
        boolean playInfo = false;

        boolean wait4SteamingAudio = false;
        if ( onRoute.getSegmentIndex() == 0 && onRoute.getCurrentSegment() != null &&
               !onRoute.getCurrentSegment().isAudioReady())
        {
            wait4SteamingAudio = true;
        }

        // RM: only play audio if not deviating & not suspended !
        playPrep = !lastContinueSegment.isPlayed(Route.AUDIO_TYPE_PREP) && !wait4SteamingAudio && !needDelayAudio;
        playAction = !lastContinueSegment.isPlayed(Route.AUDIO_TYPE_ACTION) && !wait4SteamingAudio && !needDelayAudio;
        playInfo = !lastContinueSegment.isPlayed(Route.AUDIO_TYPE_INFO) && !wait4SteamingAudio && !needDelayAudio;

        // determine how far away to schedule the action message
        long distToActionPlayable = Math.max(currSeg.getMinAudioDistance(Route.AUDIO_TYPE_ACTION), gpspeed
                * (currSeg.getMinAudioTime(Route.AUDIO_TYPE_ACTION) + (gpspeed >> 6))) >> 2;

        // determine how far away to schedule the preparation message
        long distToPrep = Math.max(currSeg.getMinAudioDistance(Route.AUDIO_TYPE_PREP), gpspeed
                * (currSeg.getMinAudioTime(Route.AUDIO_TYPE_PREP) + (gpspeed >> 4))) >> 2;

        // boolean flag that indicates whether we want to promote prep to play immediately
        boolean prepPromoted = false;

        // information messages are played ASAP following a turn, unless blocked by a pending prep message
        if (playInfo) // true if not previously played
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "play info audio for segment :: " + lastContinueSegment.getStreetName()
                        + " :: distance to Turn :: " + distanceToTurn);
            }

            // avoid replay attempts, even if info msg is blocked by prep msg
            lastContinueSegment.setPlayed(Route.AUDIO_TYPE_INFO, true);

            // play Info if not blocked by prep distance + 30 secs * speed
            // NOTE: because of gpspeed's DM6 scaling, gpspeed is 10 secs * DM5_speed
            // so gpspeed * 3 == 30 seconds * speed
            long blockInfo = distToPrep + (gpspeed * 3L);
            playInfo = blockInfo < distanceToTurn;
            if (playInfo)
                playPrep = playAction = false; // not this time, playing info
            else
                prepPromoted = true;// promote prep to be played immediately
        }

        // prep message is played when close enough to turn, and not blocked by upcoming action message
        if (playPrep) // true if not previously played, and info is not going to play
        {
            playPrep = (distanceToTurn < distToPrep) || prepPromoted;
            // see if close enough to play prep
            if (playPrep)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "play prep audio for segment :: "
                            + lastContinueSegment.getStreetName() + " :: distance to turn :: " + distanceToTurn);
                }

                // avoid replay attempts, even if prep msg is blocked by action message
                lastContinueSegment.setPlayed(Route.AUDIO_TYPE_PREP, true);
                // see if blocked by action distance + 10 secs * speed
                long blockPrep = distToActionPlayable + gpspeed;
                playPrep = blockPrep < distanceToTurn;
                if (playPrep)
                    playAction = false; // not this time, playing prep
            }
        }

        if (playAction) // true if not previously played and info and prep are not going to play
        {
            // see if close enough to play action
            playAction = distanceToTurn < distToActionPlayable;

            if (playAction)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "play action audio for segment :: "
                            + lastContinueSegment.getStreetName() + " :: distance to turn :: " + distanceToTurn);
                }

                lastContinueSegment.setPlayed(Route.AUDIO_TYPE_ACTION, true);
            }
        }

        // if the route segment has changed, then the turn type and the street
        // names need to be updated
        int turnToken = lastContinueSegment.getTurnType();
        //XXX showContinue is always false, so remove the flag.
//        if (!showContinue)
//        {
            if (lastContinueSegmentIndex < route.segmentsSize() - 1)
            {
                int nextSegIndex = lastContinueSegmentIndex + 1;
                tempNextStreetName1 = route.segmentAt(nextSegIndex).getStreetName();
                tempNextStreetName2 = route.segmentAt(nextSegIndex).getStreetAlias();
            }
            else
            {
                tempNextStreetName1 = destLine; // first line
                tempNextStreetName2 = "";
            }
//        }
//        else
//        {
//            tempNextStreetName1 = onRoute.getCurrentSegment().getStreetName();
//            tempNextStreetName2 = onRoute.getCurrentSegment().getStreetAlias();
//        }

        tempCurrStreetName = onRoute.getCurrentSegment().getStreetName();

        if (tempNextStreetName2 == null)
            tempNextStreetName2 = "";

        int nextTurnToken = -1;
        // have to check to make sure we're not on the last segment !
        if (lastContinueSegment.getSegmentType() == INavEngineConstants.SEGMENT_TIGHT_TURN && distanceToTurn < distToPrep
                && lastContinueSegmentIndex < (route.segmentsSize() - 1))
        {
            nextTurnToken = route.segmentAt(lastContinueSegmentIndex + 1).getTurnType();
        }

        if(!isCancelled)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "navUpdateUI = "+snappedHeading+", "+eta+", "+tempNextStreetName1+", "+tempNextStreetName2+", "
                       +turnToken+", "+nextTurnToken+", "+distanceToTurn+", "+distanceToDest+", "+tempCurrStreetName+", "+newHeading+", "+NavEngine.getInstance().isArriveDestination);
            }
            this.navEngineJob.postEvent(
                new NavInfoEvent(predictPos, snappedHeading, eta, tempNextStreetName1, tempNextStreetName2, turnToken, nextTurnToken, distanceToTurn, distanceToDest,
                        tempCurrStreetName, onRoute, newHeading, NavEngine.getInstance().isArriveDestination));
        }
        
        if (playInfo && !isCancelled)
        {
            this.navEngineJob.postEvent(
                new NavAudioEvent(this.onRoute.getRoute(), lastContinueSegmentIndex, distToTurnRaw, Route.AUDIO_TYPE_INFO, -1, -1, false));
        }
        else if (playPrep && !isCancelled)
        {
            this.navEngineJob.postEvent(
                new NavAudioEvent(this.onRoute.getRoute(), lastContinueSegmentIndex, distToTurnRaw, Route.AUDIO_TYPE_PREP, -1, -1, false));
        }
        else if (playAction && !isCancelled)
        {
            this.navEngineJob.postEvent(
                new NavAudioEvent(this.onRoute.getRoute(), lastContinueSegmentIndex, distToTurnRaw, Route.AUDIO_TYPE_ACTION, -1, -1, false));
        }
    }

    /**
     * do the timeupdate
     */
    private void doTimeUpdate(int deltat) throws Exception
    {
        // find speed as rss of vn and ve from onroute fix
        // RWR lets compute onRoute.velocityToSpeed from data from server,
        // before this method is invoked, and just use it here
        // int deltar = deltat * onRoute.velocityToSpeed(); // deprecated

        // QH: if deltat is bigger than 10 min, there must be something wrong
        if (deltat > 600 * 100)
            deltat = 0;
        int deltar = deltat * onRoute.getPosition().getSpeed();

        // trace.trace("deltar = " + deltar, loginfo);

        // divide by 100 since time is in 10ms steps + divide by 10
        // since v is 10 times finer granularity then pos
        // x/1000 ~ x >> 10

        onRoute.setRange(onRoute.getCurrentRange() + (deltar >> 10));

        // ------------------- timeupdate ---------------------------------------
        // onRoute becomes timeupdate

        // walk the route, updating lat/lon of timeupdate
        // route.range = route.walkRoute(route.range);
        onRoute.walkRouteFromCurrentShapePoint(onRoute.getCurrentRange());

    }

    /**
     * perform heading test
     */
    private void doHeadingTest(int heading, boolean isRamp, TnNavLocation gpsfix) throws Exception
    {
        if (onRoute.isAtTheEndOfRoute() || isRamp)
        {
            // do nothing
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "skipping heading test");
            }
        }
        else
        {
            long deltalat = onRoute.nextLat() - onRoute.getCurrentShapePointLat();
            long deltalon = onRoute.nextLon() - onRoute.getCurrentShapePointLon();
            long deltaR = onRoute.getCurrDeltaR();

            long headingTest = DataUtil.xCosY(deltalat * DataUtil.SCALE, heading);
            headingTest += DataUtil.xSinY(deltalon * onRoute.getCurrentSegment().getCosLat(), heading);

            boolean maybeDeviating = false;

            int strictHeadingThresh = devTuningRules[INavEngineConstants.RULE_HEADING_STRICT];
//            int mediumHeadingThresh = devTuningRules[INavEngineConstants.RULE_HEADING_MEDIUM];
            int looseHeadingThresh = devTuningRules[INavEngineConstants.RULE_HEADING_LOOSE];

            if (headingTest < ((strictHeadingThresh * deltaR * DataUtil.SCALE) >> 4)
                    && navUtil.getLastDevThreshold() < devTuningRules[INavEngineConstants.RULE_DISTANCE_LOOSE]
                    && navUtil.getLastDevThreshold() != -1)
            {
                // test failed
                devCounter[INavEngineConstants.DEV_HEAD_COUNT]++;
                maybeDeviating = true;
            }

            // Comment out below logic by 
            // http://jira.telenav.com:8080/browse/TNANDROID-2187
            
            // Bump up more
            // if (headingTest < ((mediumHeadingThresh * deltaR * DataUtil.SCALE) >> 4)
            // && (navUtil.getLastDevThreshold() < devTuningRules[INavEngineConstants.RULE_DISTANCE_MEDIUM])
            // && navUtil.getLastDevThreshold() != -1)
            // {
            // test failed
            // devCounter[INavEngineConstants.DEV_HEAD_COUNT] += 1;
            // maybeDeviating = true;
            // }
            // Bump up furthermore
            if (headingTest < ((looseHeadingThresh * deltaR * DataUtil.SCALE) >> 4)
                    && (navUtil.getLastDevThreshold() < devTuningRules[INavEngineConstants.RULE_DISTANCE_STRICT])
                    && navUtil.getLastDevThreshold() != -1)
            {
                // test failed
                devCounter[INavEngineConstants.DEV_HEAD_COUNT] += 1;
                maybeDeviating = true;
            }

            if (!maybeDeviating)
            {
                devCounter[INavEngineConstants.DEV_HEAD_COUNT] = 0;
            }
        }
    }

    /**
     * do measupdate
     */
    private boolean doMeasUpdate(int deltat, TnNavLocation gpsfix, boolean isRamp) throws Exception
    {
        Segment seg = onRoute.getCurrentSegment();

        // RM - 2 intermediate long's for holding the components of usePos result
        long rssv = DataUtil.gpsDistance(innovationLat, innovationLon, seg.getCosLat());
        rssv <<= DataUtil.SHIFT;
        long rangeThreshold = DataUtil.SCALE * seg.getThreshold(Route.THRESHOLD_INDEX_RANGE_DEVIATION);

        boolean usePos = rssv < rangeThreshold;
        // no coslat for velocities test
        // this accomodates DM6 units
        boolean useVel = DataUtil.distance(innovationVn, innovationVe) < deltat; // 1 * G * deltat with
                                                                                             // units corrections

        // K-gains are stored in 4 final arrays
        // form the gain index first
        // add 65 ticks = 1/2 gain index increment
        // NOTE: -64 is so gainIndex is 0 based instead of 1 based !!!
        int gainIndex = deltat - 64;
        gainIndex = gainIndex >> 7; // divide by 128
        gainIndex = Math.max(gainIndex, 0); // not less then 1 second
        gainIndex = Math.min(gainIndex, 7); // not more then 5 seconds

        TnNavLocation currPos = onRoute.getPosition();
        if (useVel)
        {
            devCounter[INavEngineConstants.DEV_VEL_COUNT] = Math.max(0, devCounter[INavEngineConstants.DEV_VEL_COUNT] - 1);
            int vn = getVelocityNorth(currPos.getSpeed(), currPos.getHeading());
            int ve = getVelocityEast(currPos.getSpeed(), currPos.getHeading());
            vn += (kTables[INavEngineConstants.K33][gainIndex] * innovationVn) >> DataUtil.SHIFT;
            ve += (kTables[INavEngineConstants.K44][gainIndex] * innovationVe) >> DataUtil.SHIFT;
                    
            if (usePos)
            {
                // cross term gains : NOTE += here !!!
                vn += ((kTables[INavEngineConstants.K31][gainIndex] * innovationLat) >> DataUtil.SHIFT);
                ve += ((kTables[INavEngineConstants.K42][gainIndex] * innovationLon) >> DataUtil.SHIFT);
            }
            
            currPos.setSpeed(DataUtil.distance(vn, ve));
            currPos.setHeading(DataUtil.bearing(vn, ve));
        }
        else
            devCounter[INavEngineConstants.DEV_VEL_COUNT]++;

        if (usePos && !isRamp)
        {
            devCounter[INavEngineConstants.DEV_POS_COUNT] = Math.max(0, devCounter[INavEngineConstants.DEV_POS_COUNT] - 1);

            // diagonal dominant gains
            int lat = currPos.getLatitude();
            int lon = currPos.getLongitude();

            lat += (kTables[INavEngineConstants.K11][gainIndex] * innovationLat) >> DataUtil.SHIFT;
            lon += (kTables[INavEngineConstants.K22][gainIndex] * innovationLon) >> DataUtil.SHIFT;

            if (useVel)
            {
                // cross term gains : NOTE += here !!!
                lat += (kTables[INavEngineConstants.K13][gainIndex] * innovationVn) >> DataUtil.SHIFT;
                lon += (kTables[INavEngineConstants.K24][gainIndex] * innovationVe) >> DataUtil.SHIFT;
            }

            currPos.setLatitude(lat);
            currPos.setLongitude(lon);
        }
        else
        {
            int multipler = devTuningRules[INavEngineConstants.RULE_RANGE_RATIO_NUMERATOR];
            int substract = devTuningRules[INavEngineConstants.RULE_RANGE_RATIO_SUBSTRACT];

            long ratio = (multipler * rssv / rangeThreshold) - substract;
            devCounter[INavEngineConstants.DEV_POS_COUNT] += (ratio > 0 ? ratio : 0);
        }

        if (!useVel || !usePos)
        {
            isDeviating = ++deviationCount > this.thresholdRules[Route.RULE_POS_DEV_COUNT];
        }
        else
        {
            if (!isRamp)
                deviationCount = Math.max(0, deviationCount - 1);
        }

        // ----------------------- measure update --------------------------------

        /*
         * the measurement fix is not on the route, so projections are used to create a new onroute fix. first take the
         * dot product of the vector from head of the shape point to the measureupdate with the vector from the head of
         * the shape point to the head of the next shape point
         */
        routeToMeas[DataUtil.LAT] = currPos.getLatitude() - onRoute.getCurrentShapePointLat();
        routeToMeas[DataUtil.LON] = currPos.getLongitude() - onRoute.getCurrentShapePointLon();

        // Log.log("routeToMeas = " + routeToMeas, Log.DEBUG, loginfo);

        // only update head-head if not on the last shape point
        if (onRoute.isAtTheEndOfRoute())
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "last segment, skipping next shape");
            }
        }
        else
        {
            headToHead[DataUtil.LAT] = onRoute.nextLat() - onRoute.getCurrentShapePointLat();
            headToHead[DataUtil.LON] = onRoute.nextLon() - onRoute.getCurrentShapePointLon();
            headToHead[DataUtil.DELTAR] = onRoute.getCurrDeltaR();
        }

        onRoute.setRange(DataUtil.project(headToHead, routeToMeas, seg.getCosLat()));

        // projection along headToHead

        // walk the route and update onRoute lat/lon
        // and produce new Vn, Ve
        onRoute.walkRouteFromCurrentShapePoint(onRoute.getCurrentRange());

        if (onRoute.isWalkingOffEnd())
            onRoute.setRange(onRoute.getCurrDeltaR());

        return (useVel && usePos);
    }

    /**
     * reset the route shape and segment indecies based on where the server thinks we are
     * 
     * @param solRouteID - route id
     * @param solSegIndex - solution segment index
     * @param shapeTailIndex - solution shape point index from the TAIL of the segment
     * @param solDeltaR - solution deltaR
     * @param solTimeStamp - solution time stamp
     * @param solLat - solution lattitude
     * @param solLon - solution longtitude
     */
    protected void resetRouteIndicies(int solRouteIndex, int solSegIndex, int solEdgeIndex, int shapeTailIndex, int solDeltaR,
            long solTimeStamp, int solLat, int solLon)
    {
        try
        {
            // get the seg index, shape index and range that server thinks we're on
            // and reset out position to that.
            /*
             * //1. segment index byte solSegIndex = Byte.parseByte((String) data.elementAt(0)); //1.5 edge index //2.
             * shape point index from segments TAIL byte shapeTailIndex = Byte.parseByte((String) data.elementAt(1));
             * //3. deltaR from the solution point to the point we're going to long solDeltaR = Long.parseLong((String)
             * data.elementAt(2)); //4. solution time stamp long solTimeStamp = Long.parseLong((String)
             * data.elementAt(3)); //5. nominal solution lat long solLat = Long.parseLong((String) data.elementAt(4));
             * //6. nominal solution lon long solLon = Long.parseLong((String) data.elementAt(5));
             */
            Vector routes = RouteWrapper.getInstance().getRoutes();
            
            if (routes == null || solRouteIndex >= routes.size() || solRouteIndex < 0)
                return;

            Route solRoute = (Route)routes.elementAt(solRouteIndex);
            if (solRoute.segmentsSize() == 0)
                return;

            if (solSegIndex >= solRoute.segmentsSize())
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "DONT HAVE " + solSegIndex + " SEG YET!!!");
                }
                return;
            }

            if (solRoute.segmentAt(solSegIndex) == null)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "2DONT HAVE " + solSegIndex + " SEG YET!!!");
                }

                return;
            }
            // trace.trace("RRI: sh good" + " len = " +
            // routes[solRouteIndex].segmentAt(solSegIndex).edgeAt(0).pointsSize(), loginfo);

            Segment solSeg = solRoute.segmentAt(solSegIndex);
            RouteEdge solEdge = solSeg.getEdge(solEdgeIndex);
            if (solEdge == null)
                return;

            // IEdge solEdge = solSeg.edgeAt(solEdgeIndex);

            // calculate the HEAD index for the point we're going to
            // int shapeHeadIndex = solSeg.shapePoints.length - 1 - shapeTailIndex;
            int shapeHeadIndex = solEdge.numPoints() - 1 - shapeTailIndex;

            if (shapeHeadIndex > 0 && shapeHeadIndex < solEdge.numPoints())
            {
                // calculate the real range from the current point:
                // its a difference from current point's deltaR and the deltaR from the solution
                // to the next point

                int realRange = solEdge.getDeltaRs(shapeHeadIndex - 1) - solDeltaR;

                resetRoutePosition(solRouteIndex, solSegIndex, solEdgeIndex, shapeHeadIndex - 1, realRange, solLat, solLon, solTimeStamp);
            }
            else
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "INVALID SHAPE INDEX = " + shapeHeadIndex);
                }
            }
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    private void resetRoutePosition(int routeIndex, int segIndex, int edgeIndex, int pointIndex, int range, int lat, int lon, long timeStamp)
            throws Exception
    {
        // reset accumulated range
        accumulatedRange = 0;
        lastRange = -1;

        this.navUtil.resetRangeDevThreshold();

        Vector routes = RouteWrapper.getInstance().getRoutes();
        // if we're switching routes, need to reset onRoute
        if (((Route)routes.elementAt(routeIndex)).getRouteID() != this.onRoute.getRoute())
        {
            onRoute.setRoute(((Route)routes.elementAt(routeIndex)).getRouteID());
            
            if(!isCancelled)
            {
                this.navEngineJob.postEvent(new NavRouteChangeEvent(this.onRoute.getRoute()));
            }
        }

        // now reset:
        onRoute.set(segIndex, edgeIndex, pointIndex, range);
        onRoute.getPosition().setLatitude(lat);
        onRoute.getPosition().setLongitude(lon);
        onRoute.setTimeStamp(timeStamp);
        // route.segmentIndex = solSegIndex;
        // route.shapeIndex = shapeHeadIndex - 1;
        // route.range = realRange;

        // restart navigation loop
        resetDeviationCounters();

        // process the history of fixes:
        // processBufferedFixes();
    }

    protected int[] getDeviationCount()
    {
        return devCounter;
    }

    protected int getLastRouteID()
    {
        return onRoute.getRoute();
    }
    
    private int getVelocityNorth(int speed, int heading)
    {
    	return (int) DataUtil.xCosY( speed, heading);
    }
    
    private int getVelocityEast(int speed, int heading)
    {
    	return (int) DataUtil.xSinY(speed, heading); 
    }
    
    public void pauseHeadingTest()
    {
        this.pauseHeadingTest = true;
        ignoreHeadingCounter = 0;
    }
}
