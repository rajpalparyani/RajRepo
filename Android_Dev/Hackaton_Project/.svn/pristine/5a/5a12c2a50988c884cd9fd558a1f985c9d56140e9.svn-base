/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TripSummaryMisLog.java
 *
 */
package com.telenav.log.mis.log;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.INavsdkMisLog;
import com.telenav.log.mis.MisLogManager;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationProvider;
import com.telenav.navsdk.events.MISLogData.MISLogItem;
import com.telenav.navsdk.events.MISLogEvents.SendMISLog;
import com.telenav.navsdk.nav.event.NavSdkNavEvent;
import com.telenav.telephony.TnBatteryManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class TripSummaryMisLog extends AbstractMisLog implements INavsdkMisLog
{
    public static int ZERO_SPEED_THRESHOLD = 0;

    public static int ZERO_SPPED_COUNTER_THRESHOLD = 3;

    private int noDevCount = 0;

    private int newRouteDevCount = 0;

    private int allDevCount = 0;

    private int resumeCount = 0;

    private int zeroSpeedCounter = 0;
    
    private int noneZeroSpeedCounter = 0;
    
    private long firstZeroSpeedTimeStamp;
    
    private long firstNoneZeroSpeedTimeStamp;

    private int stopCount = 0;

    private long stopTime = 0;

    private long trafficDelayTime = 0;
    
    private long actualTripDistance = 0;
    
    private long distToDest = 0;

    private boolean trafficDelayFlag = false;

    private boolean isTripPaused = false;
    
    private boolean isEndedByDetour = false;
    
    private boolean autoEnded = false;
    
    private boolean isEndedByForceQuit = false;
    
    private boolean isArrival = false;

    private Address destination;

    private TnLocation lastLocation;

    private Vector devLoc = new Vector();

    private long[] startSnapshot = new long[6];

    private Hashtable trafficIncidentRecords = new Hashtable();

    private Hashtable speedTrapRecords = new Hashtable();

    private Hashtable cameraRecords = new Hashtable();

    public TripSummaryMisLog(String id, int priority)
    {
        super(id, TYPE_TRIP_SUMMARY, priority);
    }

    public void startTrip(Address dest)
    {
        AppStatusMisLog statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);

        this.destination = dest;
        startSnapshot[0] = System.currentTimeMillis();
        startSnapshot[1] = statuslog.getSearchCount();
        startSnapshot[2] = statuslog.getGpsLossCount();
        startSnapshot[3] = statuslog.getGpsLossTime();
        startSnapshot[4] = statuslog.getNetworkLossCount();
        startSnapshot[5] = statuslog.getNetworkLossTime();

        setzBatteryStart(TnBatteryManager.getInstance().getBatteryLevel());

        TnLocation data = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS);
        if (data != null)
        {
            setzLat(data.getLatitude());
            setzLon(data.getLongitude());
        }

    }

    public void endTrip()
    {
        if (isArrival)
        {
            return;
        }

        if (isTripPaused)
        {
            resumeTrip(System.currentTimeMillis());
        }
        AppStatusMisLog statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);

        setNavSessionTime(System.currentTimeMillis() - startSnapshot[0]);
        setSearchCount(statuslog.getSearchCount() - (int) startSnapshot[1]);
        setzGpsLossCount(statuslog.getGpsLossCount() - (int) startSnapshot[2]);
        setzGpsLossTime(statuslog.getGpsLossTime() - startSnapshot[3]);
        setzNetworkLossCount(statuslog.getNetworkLossCount() - (int) startSnapshot[4]);
        setzNetworkLossTime(statuslog.getNetworkLossTime() - startSnapshot[5]);

        setzBatteryEnd(TnBatteryManager.getInstance().getBatteryLevel());
        
        lastLocation = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS);
//Comment for navsdk
//        Route route = RouteWrapper.getInstance().getCurrentRoute();
//        if (route != null && distToDest != -1)
//        {
//            addActualTripDistance(route.getLength() - distToDest);
//        }
    }

    public void processLog()
    {
        super.processLog();
        //Need to check these two attributes is reserved.
//        setTotalStopCount(stopCount);
//        setTotalStopTime(stopTime);

//        Comment for navsdk
//        setActualTripDistance(actualTripDistance * 9119 / 8192);
//        setNoDevCount(noDevCount);
//        setNewRouteDevCount(newRouteDevCount);
//        setAllDevCount(allDevCount);
//        setLastTwoDevLoc(fetchLastDevLoc(), fetchLastDevLoc());
//        setTotalResumeCount(resumeCount);
//        setTotalStopCount(stopCount);
//        setTotalStopTime(stopTime);
//        setTrafficIncidentsCount(trafficIncidentRecords.size());
//        setRedlightCameraCount(cameraRecords.size());
//        setSpeedTrapCount(speedTrapRecords.size());
//        setTrafficDelayTime(trafficDelayTime);
        setTrafficDelayFlag(trafficDelayFlag ? "Yes" : "No");//TODO
//        setRemainingDistanceToTargetOnExit(distToDest);
        
        setEndMethod(getEndMethod());
//        setExitReason(isEndedByDetour() ? VALUE_EXIT_REASON_DETOUR : VALUE_EXIT_REASON_MANUAL_EXIT);
        setArrivalFlag(isAutoEnded() ? "1" : "0");

        if (lastLocation != null)
        {
            setzEndLat(lastLocation.getLatitude());
            setzEndLon(lastLocation.getLongitude());
        }

        Stop dest = null;
        if (destination.getPoi() != null)
        {
            if (destination.getPoi().getBizPoi() != null)
            {
                dest = destination.getPoi().getBizPoi().getStop();
            }
            if (dest == null)
            {
                dest = destination.getPoi().getStop();
            }
        }
//        if (dest == null)
//        {
//            dest = destination.getStop();
//        }
//        if (dest != null && lastLocation != null)
//        {
//            int dist = DataUtil.gpsDistance(lastLocation.getLatitude() - dest.getLat(), lastLocation.getLongitude() - dest.getLon(),
//                DataUtil.getCosLat(dest.getLat()));
//            dist = (dist * 9119 / 8192);
//            setArrivalGeofenceDistance(dist);
//        }

        if (destination.getPoi() != null)
        {
            if (destination.getPoi().getBizPoi() != null)
            {
                setPoiId(destination.getPoi().getBizPoi().getPoiId());
            }
            if (destination.getPoi().getAd() != null)
            {
                setAdsId(destination.getPoi().getAd().getAdID());
                setAdsSource(destination.getPoi().getAd().getAdSource());
            }
        }
    }

    private String getEndMethod()
    {
        String method = VALUE_TRIP_END_METHOD_MANUAL;
        if (isAutoEnded())
        {
            method = VALUE_TRIP_END_METHOD_AUTO;
        }
        else
        {
            if (isEndedByDetour())
                method = VALUE_TRIP_END_METHOD_DETOUR;
            if (isEndedByForceQuit())
                method = VALUE_TRIP_END_METHOD_FORCEQUIT;
        }
        return method;
    }
    
    public void setNavSessionTime(long time)
    {
        this.setzDurationLength(time);
    }

    public void addActualTripDistance(long delta)
    {
        actualTripDistance += delta;
    }
    
    public void updateNavInfo(NavSdkNavEvent navEvent, boolean isArrivalDest)
    {
        if(navEvent != null)
        {
            if(!isArrival)
            {
//                if(navEvent.getSnapPosition() != null)
//                {
//                    this.updateSpeed(navEvent.getSnapPosition().getSpeed());
//                }
                this.setDistToDest(navEvent.getDistanceToDest());
//                this.setLastLocation(navEvent.getSnapPosition());
            }
        }
        
        if(isArrivalDest)
        {
            //Should not switch the sequence.
            this.endTrip();
            this.setIsArrival(true);
            this.setIsAutoEnded(true);
        }
    }
    

    
    public void updateSpeed(int speed)
    {
        if (speed <= ZERO_SPEED_THRESHOLD)
        {
            zeroSpeedCounter++;
            if(zeroSpeedCounter == 1)
            {
                firstZeroSpeedTimeStamp = System.currentTimeMillis();
            }
            noneZeroSpeedCounter = 0;
            if (!isTripPaused && zeroSpeedCounter >= ZERO_SPPED_COUNTER_THRESHOLD)
            {
                pauseTrip(firstZeroSpeedTimeStamp);
            }
        }
        else
        {
            zeroSpeedCounter = 0;
            if (isTripPaused)
            {
                noneZeroSpeedCounter ++;
                if(noneZeroSpeedCounter == 1)
                {
                    firstNoneZeroSpeedTimeStamp = System.currentTimeMillis();
                }
                if(noneZeroSpeedCounter >= ZERO_SPPED_COUNTER_THRESHOLD)
                {
                    resumeTrip(firstNoneZeroSpeedTimeStamp);
                }
            }
        }
    }

    public void pauseTrip(long timestamp)
    {
        isTripPaused = true;
        this.stopCount++;
        this.stopTime -= timestamp;
    }

    public void resumeTrip(long timestamp)
    {
        isTripPaused = false;
        this.stopTime += timestamp;
    }
    
    public void setDistToDest(long dist)
    {
        this.distToDest = dist;
    }
    
    public void setLastLocation(TnLocation loc)
    {
        this.lastLocation = loc;
    }

    public void setIsAutoEnded(boolean autoEnded)
    {
        this.autoEnded = autoEnded;
    }
    
    public boolean isAutoEnded()
    {
        return autoEnded;
    }
    
    public boolean isEndedByForceQuit()
    {
        return isEndedByForceQuit;
    }

    public void setEndedByForceQuit(boolean isEndedByForceQuit)
    {
        this.isEndedByForceQuit = isEndedByForceQuit;
    }
    
    public void setIsEndedByDetour(boolean isEndedByDetour)
    {
        this.isEndedByDetour = isEndedByDetour;
    }
    
    public boolean isEndedByDetour()
    {
        return isEndedByDetour;
    }
    
    public void setIsArrival(boolean isArrival)
    {
        this.isArrival = isArrival;
    }
    
    public boolean isArrival()
    {
        return isArrival;
    }

    public void increaseNoDevCount()
    {
        noDevCount++;
    }

    public void increaseNewRouteDevCount()
    {
        newRouteDevCount++;
    }

    public void setNewRouteDevCount(int count)
    {
        this.setAttribute(ATTR_DEVCNT_NEWROUTE, String.valueOf(count));
    }

    public void increaseAllDevCount()
    {
        allDevCount++;
    }

    public void setAllDevCount(int count)
    {
        this.setAttribute(ATTR_DEVCNT_ALL, String.valueOf(count));
    }

    public void addDeviation(TnLocation data)
    {
        //Record the deviation location.
        devLoc.addElement(data);
        
        //Increase the actual distance.
        Route route = RouteWrapper.getInstance().getCurrentRoute();
        if(route != null && distToDest != -1)
        {
            addActualTripDistance(route.getLength() - distToDest);
            distToDest = -1;
        }
    }

    public TnLocation fetchLastDevLoc()
    {
        if(devLoc.size() > 0)
        {
            TnLocation data = (TnLocation) devLoc.lastElement();
            devLoc.removeElementAt(devLoc.size() - 1);
            return data;
        }
        else
        {
            return null;
        }
    }

    public void increaseTotalResumeCount()
    {
        resumeCount++;
    }

    public void addTrafficIncident(String latlon)
    {
        if (!trafficIncidentRecords.containsKey(latlon))
        {
            trafficIncidentRecords.put(latlon, latlon);
        }
    }

    public void addRedlightCamera(String latlon)
    {
        if (!cameraRecords.containsKey(latlon))
        {
            cameraRecords.put(latlon, latlon);
        }
    }

    public void addSpeedTrap(String latlon)
    {
        if (!speedTrapRecords.containsKey(latlon))
        {
            speedTrapRecords.put(latlon, latlon);
        }
    }

    public void increaseTrafficDelayTime(long delta)
    {
        this.trafficDelayTime += delta;
    }

    public void setTrafficDelayFlag(boolean flag)
    {
        this.trafficDelayFlag = flag;
    }

    public void setInitialTripDistance(int distance)
    {
        this.setAttribute(ATTR_INITIAL_TRIP_DIST, String.valueOf(distance));
    }

    public void setActualTripDistance(long distance)
    {
        this.setAttribute(ATTR_ACTUAL_TRIP_DIST, String.valueOf(distance));
    }

    public void setInitialEta(int eta)
    {
        this.setAttribute(ATTR_INITIAL_ETA, String.valueOf(eta));
    }

    public void setTotalStopCount(int count)
    {
        this.setAttribute(ATTR_TOTAL_STOP_COUNT, String.valueOf(count));
    }

    public void setTotalStopTime(long time)
    {
        this.setAttribute(ATTR_TOTAL_STOP_TIME, String.valueOf(time));
    }

    public void setNoDevCount(int count)
    {
        this.setAttribute(ATTR_DEVCNT_NODEV, String.valueOf(count));
    }

    public void setLastTwoDevLoc(TnLocation data1, TnLocation data2)
    {
        if (data1 != null)
        {
            String value = data1.getLatitude() + "," + data1.getLongitude();
            if (data2 != null)
            {
                value += (";" + data2.getLatitude() + "," + data2.getLongitude());
            }
            this.setAttribute(ATTR_DEVLOC, value);
        }
    }

    public void setEndMethod(String method)
    {
        this.setAttribute(ATTR_TRIP_END_METHOD, method);
    }

    public void setTotalResumeCount(int count)
    {
        this.setAttribute(ATTR_TOTAL_RESUME_COUNT, String.valueOf(count));
    }

    public void setTrafficIncidentsCount(int count)
    {
        this.setAttribute(ATTR_TRAFFIC_INCIDENTS_CNT, String.valueOf(count));
    }

    public void setRedlightCameraCount(int count)
    {
        this.setAttribute(ATTR_REDLIGHT_CAMERA_CNT, String.valueOf(count));
    }

    public void setSpeedTrapCount(int count)
    {
        this.setAttribute(ATTR_SPEED_TRAP_CNT, String.valueOf(count));
    }

    public void setSearchCount(int count)
    {
        this.setAttribute(ATTR_TOTAL_SEARCH_COUNT, String.valueOf(count));
    }

    public void setTrafficDelayFlag(String flag)
    {
        this.setAttribute(ATTR_TRAFFIC_DELAY_FLAG, flag);
    }

    public void setTrafficDelayTime(long time)
    {
        this.setAttribute(ATTR_TRAFFIC_DELAY_TIME, String.valueOf(time));
    }

    public void setTmcSegmentsCount(int count)
    {
        this.setAttribute(ATTR_TMC_SEGMENTS, String.valueOf(count));
    }

    public void setCpdDistance(int distance)
    {
        this.setAttribute(ATTR_CPD_DISTANCE, String.valueOf(distance));
    }

    public void setPoiId(String poiId)
    {
        this.setAttribute(ATTR_POI_ID, poiId);
    }

    public void setAdsId(String adsId)
    {
        this.setAttribute(ATTR_ADS_ID, adsId);
    }

    public void setAdsSource(String source)
    {
        this.setAttribute(ATTR_AD_SOURCE, source);
    }
    
    public void setExitReason(String reason)
    {
        this.setAttribute(ATTR_NAVIGATION_EXIT_REASON, reason);
    }
    
    public void setArrivalFlag(String flag)
    {
        this.setAttribute(ATTR_ARRIVAL_FLAG, flag);
    }
    
    public void setArrivalGeofenceDistance(int distance)
    {
        this.setAttribute(ATTR_ARRIVAL_GEOFENCE_DISTANCE, String.valueOf(distance));
    }
    
    public void setRemainingDistanceToTargetOnExit(long distance)
    {
        this.setAttribute(ATTR_REMAINING_DISTANCE_TO_TARGET_ON_EXIT, String.valueOf(distance));
    }

    public void navsdkPreProcess(SendMISLog navsdkMisLog)
    {
        AbstractMisLog log = (AbstractMisLog) MisLogManager.getInstance().getMisLog(this.getType());
        if (log != null)
        {
            for (int i = 0; i < navsdkMisLog.getLogitemsCount(); i++)
            {
                MISLogItem logItem = navsdkMisLog.getLogitems(i);
                log.setAttribute(logItem.getAttr(), logItem.getValue());
            }
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                    { log });
            MisLogManager.getInstance().removeStoredLog(log);
        }
        else
        {
            Logger.log(Logger.EXCEPTION, this.getClass().getName(), "Fatal error: cannot fint corresponding log with type :" + this.getType());
        }
    }

}
