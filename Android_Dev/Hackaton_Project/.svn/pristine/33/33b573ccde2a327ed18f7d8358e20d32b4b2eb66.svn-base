package com.telenav.nav;

import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.nav.NavDataFactory;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.NavUtil;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteEdge;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.datatypes.route.Segment;
import com.telenav.logger.Logger;
import com.telenav.nav.event.NavAdiEvent;
import com.telenav.nav.event.NavAudioEvent;
import com.telenav.nav.event.NavEndEvent;
import com.telenav.nav.event.NavGpsEvent;


/*
 * Copyright 2008 TeleNav Inc.
 * @author ruslanm
 *
 */
class Adi
{
    protected int GPS_ADI_RETRIES;

    protected int recurringDelay;

    // add for lineClip method
    public static final int TOP = 0x1;

    public static final int BOTTOM = 0x2;

    public static final int RIGHT = 0x4;

    public static final int LEFT = 0x8;

    // x or y of point
    private static final int POINT_X = 0;

    private static final int POINT_Y = 1;
    
	//http://jira.telenav.com:8080/browse/TNANDROID-2796
    private static final int ADI_AUDIO_PLAY_INTERVAL = 300 * 1000;

    /** map tiles cache, set public for nav core usage */
    protected NavState onRoute; // our status (position) on the route

    protected int gpsNumSats = 0; // gps strength, set by nav part and adi nav code, used for update UI

    protected Regainer regainer; // regainer part

    private int noGpsCycle;

    private TnNavLocation lastfix;
    
    private TnNavLocation routeOrigin;

    private long lastFixTime;

    private boolean isPedestrian;

    // private int[] mapTileRange;
//    protected IMapTile[] mapTiles;

    //http://jira.telenav.com:8080/browse/TNANDROID-2796
    //checkPlayedSegment will not be clear if enter adi again!!!
    private Segment lastPlayedSegment, lastPlayedTurnSegment, checkPlayedSegment;
    private long checkPlayedTimestamp;
    private NavUtil navUtil;
    protected boolean isCancelled;
    protected NavEngineJob navEngineJob;
    
    /** constructor */
    protected Adi(Regainer regainer, int recurringDelay, int gpsRetries, NavUtil navUtil, NavEngineJob navEngineJob)
    {
        this.recurringDelay = recurringDelay;
        this.GPS_ADI_RETRIES = gpsRetries;

        this.routeOrigin = new TnNavLocation("");
        this.regainer = regainer;
        this.navUtil = navUtil;
        this.navEngineJob = navEngineJob;
    }
    
    void cancel()
    {
        isCancelled = true;
    }

    protected void init(boolean isPedestrian)
    {
        Segment seg = RouteWrapper.getInstance().getCurrentRoute().segmentAt(0);
        RouteEdge edge = seg.getEdge(0);
        this.routeOrigin.setLatitude(edge.latAt(0));
        this.routeOrigin.setLongitude(edge.lonAt(0));
        this.isPedestrian = isPedestrian;
    }

    protected byte handleAdi()
    {
        lastPlayedSegment = null;

        byte returnStatus = NavEndEvent.STATUS_ERROR;
        int findMeCounter = 0; // count findme success times
        onRoute = NavDataFactory.getInstance().createNavState(RouteWrapper.getInstance().getCurrentRouteId());

        TnNavLocation gpsfix = getGPSFix(false);
        if (gpsfix != null)
        {
            boolean isFarFromOrigin = false;
//            boolean isFarFromOrigin = checkFarEnough(routeOrigin.getLat(), routeOrigin
//                    .getLon(), gpsfix.getLat(), gpsfix.getLon());
            if (!isFarFromOrigin)
            {
                if (isOnRoad(gpsfix, false))
                {
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "fix is on surrounding roads");
                    }
                    return NavEndEvent.STATUS_DEVIATED;
                }
            }
            else
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "isFarFromOrigin == true");
                }
                return NavEndEvent.STATUS_DEVIATED;
            }
        }

        lastFixTime = 0;

        boolean isFirstShow = true; // first cycle to show ADI

        while (!isCancelled)
        {
            try
            {
                if (lastFixTime != 0)
                {
                    long delta = System.currentTimeMillis() - lastFixTime;
                    delta = (delta < recurringDelay ? recurringDelay - delta : 0);
                    
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "Adi, delta = "+delta);
                    }
                    
                    if (delta > 0)
                        DataUtil.sleep(delta);
                }
                gpsfix = getGPSFix(false);
                lastFixTime = System.currentTimeMillis();
                if (gpsfix == null)
                {
                    returnStatus = NavEndEvent.STATUS_NO_GPS;
                    
                    if(!isCancelled)
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "Adi, post no gps event");
                        }
                        this.navEngineJob.postEvent(new NavGpsEvent(-1));
                    }
                    
                    gpsfix = getGPSFix(true);
                    if (gpsfix != null)
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "got GPS fix in second try");
                        }
						//http://jira.telenav.com:8080/browse/TNANDROID-2796
                        this.regainer.regain(gpsfix, this.onRoute, false, true, true);
                        onRoute.set(regainer.getOnRoute());
                        showAdi(gpsfix, onRoute, isFirstShow, -1);
                    }
                    // break;
                }
                else
                // good gps fix
                {
                    if(!isCancelled)
                    {
                        this.navEngineJob.postEvent(new NavGpsEvent(gpsNumSats));
                    }

                    int regainerStatus = this.regainer.regain(gpsfix, this.onRoute, false, true, true);
                    // System.out.println( "regainStatus " + regainerStatus);

                    // onRoute.copyFrom(this.regainer.onRoute);
                    onRoute.set(regainer.getOnRoute());

                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "try to show ADI");
                    }
                    showAdi(gpsfix, onRoute, isFirstShow, gpsNumSats); // update ADI UI
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "finish showing ADI");
                    }

                    isFirstShow = false;

                    if (regainerStatus == Regainer.SNAP_TO_SUCCESS)
                    {
                        // regainer buffer should already take care of the 3 times logic
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(), "regain succeed in ADI");
                        }
                        returnStatus = NavEndEvent.STATUS_ON_TRACK;
                        break;
                    }
                    else
                    {
                        boolean isFarFromOrigin = false;
//                        boolean isFarFromOrigin = checkFarEnough(routeOrigin.getLat(),
//                            routeOrigin.getLon(), gpsfix.getLat(), gpsfix.getLon());

                        if (isFarFromOrigin)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, this.getClass().getName(), "far from origin, deviate");
                            }
                            returnStatus = NavEndEvent.STATUS_DEVIATED;
                            break; // get out of while loop
                        }
                        else
                        {
                            if (isOnRoad(gpsfix, true))
                            {
                                findMeCounter++;
                                if (Logger.DEBUG)
                                {
                                    Logger.log(Logger.INFO, this.getClass().getName(), "fix is on surrounding roads, findMeCounter = "+findMeCounter);
                                }
                                
                                if (findMeCounter >= 3)
                                {
                                    if (Logger.DEBUG)
                                    {
                                        Logger.log(Logger.INFO, this.getClass().getName(), "findme on road, deviate");
                                    }
                                    findMeCounter = 0;
                                    returnStatus = NavEndEvent.STATUS_DEVIATED;
                                    break; // get out of while loop
                                }
                            }
                            else
                            {
                                // update onRoute with regainer's onRoute.
//                                onRoute = navFactory.createNavState(gpsFactory);
                                onRoute.set(regainer.getOnRoute());
                                findMeCounter = 0;
                            }
                        }
                    }
                }
            }
            catch (Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
                returnStatus = NavEndEvent.STATUS_DEVIATED; //trigger deviation to recover the exception
                break;
            }
        }
        
        if (isCancelled)
        {
            returnStatus = NavEndEvent.STATUS_STOPPED;
        }
        
        return returnStatus;
    }

    /**
     * update ADI UI, try use regainer counted point first, if no, use the point on route
     * 
     * @param fix
     * @param originRoute
     */
    private void showAdi(TnNavLocation fix, NavState lastOnRoute, boolean isFirstShow, int gpsNum)
    {
        if(!isCancelled)
        {
            this.navEngineJob.postEvent(new NavGpsEvent(gpsNum));
        }
        
        boolean showed = false;
        // try to show ADI point by regainer
        if (fix != null)
            showed = showAdiFromRegainer(fix, isFirstShow);
       
        // if position is not valid, we should not update ADI 
        if (lastOnRoute == null || lastOnRoute.getPosition() == null ||
            lastOnRoute.getPosition().getLatitude() < -9000000  || lastOnRoute.getPosition().getLatitude() >  9000000 ||
            lastOnRoute.getPosition().getLongitude() < -18000000 || lastOnRoute.getPosition().getLongitude() > 18000000 )
        {         
            return;
        }
        
        Segment currSeg = lastOnRoute.getCurrentSegment();
        if (currSeg.getSegmentAudioNode()!= null && currSeg != lastPlayedSegment && currSeg != lastPlayedTurnSegment && 
        		! NavEngine.getInstance().isArriveDestination)
        {
		    //http://jira.telenav.com:8080/browse/TNANDROID-2796
            if (checkPlayedSegment != currSeg
                    || (System.currentTimeMillis() - checkPlayedTimestamp) > ADI_AUDIO_PLAY_INTERVAL)
            {
                //fix bug 56182, change true to false
                if (!isCancelled)
                {
				    lastPlayedSegment = currSeg;
                    checkPlayedSegment = currSeg;
                    checkPlayedTimestamp = System.currentTimeMillis();
                    this.navEngineJob.postEvent(new NavAudioEvent(lastOnRoute.getRoute(),
                            lastOnRoute.getSegmentIndex(), -1, Route.AUDIO_TYPE_ADI, -1,
                            lastOnRoute.getPosition().getHeading(), false));
                }
            }
        }
    }

    /**
     * FIXME why is this needed??? We already regained, why do we need to snap again?? If regainer has the ADI point,
     * use it and update ADI UI
     * 
     * @param fix
     * @return true: if regainer counted the point before false: regainer don't have
     */
    private boolean showAdiFromRegainer(TnNavLocation fix, boolean isFirstShow)
    {
        NavState regainerNavState = regainer.getOnRoute();

        Segment bestSegment = regainerNavState.getCurrentSegment();
        if (bestSegment == null)
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.WARNING, this.getClass().getName(), "showAdiFromRegainer invalid segment");
            }
            return false;
        }
        int bestEdgeIndex = regainerNavState.getEdgeIndex();
        RouteEdge bestEdge = bestSegment.getEdge(bestEdgeIndex);
        if (bestEdge == null)
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.WARNING, this.getClass().getName(), "showAdiFromRegainer invalid edge");
            }
            return false;
        }

        boolean snapped = regainer.snapEdge(bestSegment, bestEdgeIndex, fix);
        if (!snapped)
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.WARNING, this.getClass().getName(), "showAdiFromRegainer snap fail");
            }
            return false;
        }
        
        // begin add by hchai
        int distanceToTurnOnRoute = 0;
        int nonProjDistToTurn = -regainerNavState.getCurrentRange();
        //calculate the distance traversed in this route segment
        distanceToTurnOnRoute = nonProjDistToTurn - (regainerNavState.getPosition().getSpeed() >> 2); // projecting ahead 2.5 secs
        int remainingDist = regainerNavState.getDistanceToTurn();
        distanceToTurnOnRoute += remainingDist;
        // end add by hchai
        
        // update ADI UI
        int adiLat = onRoute.getCurrentShapePointLat();
        int adiLon = onRoute.getCurrentShapePointLon();
        String nextStreetName1 = onRoute.getCurrentSegment().getStreetName();
        String nextStreetName2 = onRoute.getCurrentSegment().getStreetAlias();
        int turnImageIndex;
        int distanceToTurn = DataUtil.gpsDistance(fix.getLatitude() - adiLat, fix.getLongitude()
                - adiLon, DataUtil.getCosLat(adiLat));
        int headingAngle = NavEngine.getInstance().getGpsProvider().getLastKnownHeading();
        while (headingAngle >= 360)
            headingAngle -= 360;
        while (headingAngle < 0)
            headingAngle += 360;

        int adiHeading = DataUtil.bearing(fix.getLatitude(), fix.getLongitude(), adiLat, adiLon);

        int routeHeading = DataUtil.bearing(adiLat, adiLon, onRoute.nextLat(), onRoute
                .nextLon());

        int angle = adiHeading - routeHeading - 22; // 22 is half of 45
        angle = 360 - angle;

        while (angle >= 360)
            angle -= 360;
        while (angle < 0)
            angle += 360;

        turnImageIndex = angle / 45; // angle to image index table see in Storage.java

        int turnType = 0;
        if (turnImageIndex >= 1 && turnImageIndex <= 3)
        {
            turnType = 2;
        }
        else if (turnImageIndex >= 5 && turnType <= 7)
        {
            turnType = 829;
        }
        Segment currSeg = onRoute.getCurrentSegment();
        int distanceToDest = currSeg.getDistanceToDest() - currSeg.getLength()
                + distanceToTurnOnRoute + distanceToTurn;

        // calculate ETA
        // Guoyuan for bug 13671. Force ETA to 0 if distanceToDest is 0
        int eta = 0;
        if (distanceToDest > 0)
        {
            // eta = calETA(route, onRoute);
            Route route = RouteWrapper.getInstance().getRoute(onRoute.getRoute());
            if(route != null)
            {
                eta = route.calcETA(onRoute.getSegmentIndex(), onRoute.getEdgeIndex(), onRoute.getPointIndex(), onRoute.getRange(),
                    isPedestrian, Route.PEDESTRIAN_SPEED);
            }
        }

        if (turnType > 0 && distanceToTurn < fix.getSpeed() + 50
                && Math.abs(adiHeading - fix.getHeading()) < 60)
        {
            if (currSeg != lastPlayedTurnSegment && !NavEngine.getInstance().isArriveDestination)
            {
                lastPlayedTurnSegment = currSeg;
                if(!isCancelled)
                {
                    this.navEngineJob.postEvent(
                        new NavAudioEvent(onRoute.getRoute(), onRoute.getSegmentIndex(), distanceToTurn, Route.AUDIO_TYPE_ADI_TURN, turnType, -1, false));
                }
            }
        }

        if(!isCancelled)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "showAdiFromRegainer, "+nextStreetName1+", "+nextStreetName2+", "+turnImageIndex+", "+distanceToTurn+", "
                        +distanceToDest+", "+headingAngle+", "+eta+", "+isFirstShow);
            }
            this.navEngineJob.postEvent(
                new NavAdiEvent(fix, regainer.getOnRoute(), nextStreetName1, nextStreetName2, turnImageIndex, -1, distanceToTurn, distanceToDest, onRoute, headingAngle, eta,
                        isFirstShow));
        }
        
        return true;
    }
    
    private TnNavLocation getGPSFix(boolean needLastValid)
    {
        TnNavLocation fix = new TnNavLocation("");
        TnNavLocation vFixes[] = new TnNavLocation[]
        { fix };
        if (NavEngine.getInstance().getGpsProvider().getFixes(1, vFixes) == 1)
        {
        	// Guoyuan: Use encrypted GPS fix while doing ADI calculation, 
        	// because route's information is encrypted
			// By the time below code is executed
			// GpsEncrypter instance should have been created (singleton)
			// So just pass in null values
//            if (navController.gpsEncrypter != null)
//                navController.gpsEncrypter.getEncFixes(1, vFixes);

            if (lastfix != null
                    && ((fix.getTime() - lastfix.getTime()) <= 0))
            {
                noGpsCycle++;
            }
            else
            {
                lastfix = fix;
                noGpsCycle = 0;
            }
            gpsNumSats = fix.getSatellites();
        }
        else
        {
            noGpsCycle++;
        }

        if (noGpsCycle > GPS_ADI_RETRIES && !needLastValid)
        {
            return null;
        }
        else
        {
            return lastfix;
        }
    }

//    /**
//     * check distance between two points is whether far enough
//     * 
//     * @param originLat
//     * @param originLon
//     * @param currentLat
//     * @param currentLon
//     * @return true: distance is far enough false: distance is near
//     */
//    private boolean checkFarEnough(int originLat, int originLon, int currentLat,
//            int currentLon, long[] mapTileRange)
//    {
//        boolean farEnough = true;
//
//        // Need to fix later.
//        // if not set range, return true to deviate.
//
//        // FIXME just use a distance check for now.
//        // farEnough = dataUtil.rss( originLat - currentLat, originLon - currentLon ) > 400;
//
//        if (mapTileRange == null)
//        {
//            if (dataUtil.rss(originLat - currentLat, originLon - currentLon) > 100)
//                return true;
//            else
//                return false;
//        }
//
//        if (currentLat > mapTileRange[0] && currentLat < mapTileRange[2]
//                && currentLon > mapTileRange[1] && currentLon < mapTileRange[3])
//        {
//            farEnough = false;
//        }
//
//        return farEnough;
//    }

    private int calcAdiThreshold(int roadType, TnNavLocation gpsData)
    {
        int[] rules = this.navUtil.getThresholdRules();
        int regainThreshold = (this.navUtil.calcRangeDevThreshold(roadType, gpsData) * 10 * rules[Route.RULE_REGAIN_RANGE]) >> 10;
        return (regainThreshold * 900) >> 10;
    }

    /*
     * cohen-sutherland line clip arithmetric @param p1: [in/out] line start point and end point @param p2: [in/out]
     * line start point and end point @param leftTop: [in] the lefttop clip region @param rightBottom: [in] the
     * rightBottom clip region @return: FALSE if both the p1 and p2 are out of clip region after operation, otherwise
     * TRUE
     */
    private boolean lineClip(int[] p1, int[] p2, int[] leftTop, int[] rightBottom)
    {
        int outcode0, outcode1, outcodeOut;
        boolean accept = false, done = false;

        outcode0 = compOutCode(p1, leftTop, rightBottom);
        outcode1 = compOutCode(p2, leftTop, rightBottom);

        do
        {
            if (0 == (outcode0 | outcode1))
            { // outcode0 == 0 and outcode1 == 0
                // both p1 and p2 are within the clip rectangle
                accept = true;
                done = true;
            }
            else if (0 != (outcode0 & outcode1))
                // outcode0 > 0 and outcode1 > 0
                // both p1 and p2 are out of the clip rectangle, and in the same area
                done = true;
            else
            {
                int x, y;
                boolean bool = (0 != outcode0); // c++ : 0 -> false ; other -> true
                outcodeOut = bool ? outcode0 : outcode1;
                if (0 != (outcodeOut & TOP))
                {
                    x = p1[POINT_X] + (p2[POINT_X] - p1[POINT_X])
                            * (rightBottom[POINT_Y] - p1[POINT_Y])
                            / (p2[POINT_Y] - p1[POINT_Y]);
                    y = rightBottom[POINT_Y];
                }
                else if (0 != (outcodeOut & BOTTOM))
                {
                    x = p1[POINT_X] + (p2[POINT_X] - p1[POINT_X])
                            * (leftTop[POINT_Y] - p1[POINT_Y])
                            / (p2[POINT_Y] - p1[POINT_Y]);
                    y = leftTop[POINT_Y];
                }
                else if (0 != (outcodeOut & RIGHT))
                {
                    y = p1[POINT_Y] + (p2[POINT_Y] - p1[POINT_Y])
                            * (rightBottom[POINT_X] - p1[POINT_X])
                            / (p2[POINT_X] - p1[POINT_X]);
                    x = rightBottom[POINT_X];
                }
                else
                {
                    y = p1[POINT_Y] + (p2[POINT_Y] - p1[POINT_Y])
                            * (leftTop[POINT_X] - p1[POINT_X])
                            / (p2[POINT_X] - p1[POINT_X]);
                    x = leftTop[POINT_X];
                }
                if (outcodeOut == outcode0)
                {
                    p1[POINT_X] = x;
                    p1[POINT_Y] = y;
                    outcode0 = compOutCode(p1, leftTop, rightBottom);
                }
                else
                {
                    p2[POINT_X] = x;
                    p2[POINT_Y] = y;
                    outcode1 = compOutCode(p2, leftTop, rightBottom);
                }
            }
        } while (false == done);

        return accept;

    }

    /*
     * computer out code which is the region where point is at clip rectangle @param p: the point to compute @param
     * leftTop,rightBottom: clip rectangle @return: the combination of LEFT, RIGHT, TOP, BOTTOM say: if the point is at
     * the LEFT of clip rectangle, return LEFT, but if at the TOP of clip rectangle also, return LEFT | TOP;
     */
    private int compOutCode(int[] p, int[] leftTop, int[] rightBottom)
    {
        int outcode = 0;
        if (p[POINT_Y] > rightBottom[POINT_Y])
            outcode |= TOP;
        else if (p[POINT_Y] < leftTop[POINT_Y])
            outcode |= BOTTOM;
        if (p[POINT_X] > rightBottom[POINT_X])
            outcode |= RIGHT;
        else if (p[POINT_X] < leftTop[POINT_X])
            outcode |= LEFT;

        return outcode;
    }

    protected byte checkFindMeOnTile(TnNavLocation gpsfix)
    {
        if (gpsfix != null)
        {
            // determine whether to go into adi or not
            boolean isFarFromOrigin = false;
//            boolean isFarFromOrigin = checkFarEnough(routeOrigin.getLat(), routeOrigin
//                    .getLon(), gpsfix.getLat(), gpsfix.getLon());
            if (!isFarFromOrigin)
            {
                if (isOnRoad(gpsfix, false))
                {
                    return NavEndEvent.STATUS_DEVIATED;
                }
                else
                {
                    return NavEndEvent.STATUS_ADI;
                }
            }
            else
            {
                return NavEndEvent.STATUS_DEVIATED;
            }
        }
        else
        {
            return NavEndEvent.STATUS_NO_GPS;
        }

    }
    
    private boolean isOnRoad(TnNavLocation gpsfix, boolean ignoreHeading)
    {
        int[] adiThreshold = new int[3];
        adiThreshold[0] = calcAdiThreshold(DataUtil.LOCAL_STREET, gpsfix);
        adiThreshold[1] = calcAdiThreshold(DataUtil.ARTERIAL, gpsfix);
        adiThreshold[2] = calcAdiThreshold(DataUtil.HIGHWAY, gpsfix);
        INavDataProvider provider = NavEngine.getInstance().navDataProvider;

        if (provider != null)
        {
            return provider.isOnRoad(gpsfix, adiThreshold, ignoreHeading);
        }

        return false;
    }

}
