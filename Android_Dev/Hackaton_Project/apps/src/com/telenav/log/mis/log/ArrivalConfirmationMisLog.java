/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ArrivalConfirmationMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.logger.Logger;
import com.telenav.navsdk.nav.event.NavSdkNavEvent;


/**
 *@author Alberta (dzhao@telenavsoftware.com)
 *@date 2011-10-13
 */
public class ArrivalConfirmationMisLog extends AbstractMisLog
{

    private boolean isArrival = false;
    private long distanceToDest;
    private String genFenseDistance = "";
    private static String ARRIVAL = "1";
    private static String INCOMPLETE = "0";
    
    private boolean isLogGenFense50 = true;
    private boolean isLogGenFense100 = true;
    private boolean isLogGenFense150 = true;
    private boolean isLogGenFense200 = true;
    private boolean isLogGenFense250 = true;
    private boolean isLogGenFense300 = true;
    private boolean isLogGenFense350 = true;
    private boolean isLogGenFense400 = true;
    private boolean isLogGenFense500 = true;
    private boolean isLogGenFense1609 = true;

    private static final long GENFENSE_DISTANCE_50 = 50;
    private static final long GENFENSE_DISTANCE_100 = 100;
    private static final long GENFENSE_DISTANCE_150 = 150;
    private static final long GENFENSE_DISTANCE_200 = 200;
    private static final long GENFENSE_DISTANCE_250 = 250;
    private static final long GENFENSE_DISTANCE_300 = 300;
    private static final long GENFENSE_DISTANCE_350 = 350 ;
    private static final long GENFENSE_DISTANCE_400 = 400;
    private static final long GENFENSE_DISTANCE_500 = 500;
    private static final long GENFENSE_DISTANCE_1609 = 1609;
    
    public ArrivalConfirmationMisLog(String id, int priority)
    {
        super(id, TYPE_ARIVALl_CONFIRMATION, priority);
    }
    
    public void processLog()
    {
        super.processLog();
        setArrivalFlag(isArrival()? ARRIVAL : INCOMPLETE);
        setRemainingDistanceToTarget(distanceToDest);
        setArrivalGeofenceDistance(getGenFenseDistance());
    }
    
    public String getGenFenseDistance()
    {
        return genFenseDistance;
    }

    public void setGenFenseDistance(String genFenseDistance)
    {
        this.genFenseDistance = genFenseDistance;
        Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : " + "(GenFenseDistance) : " + genFenseDistance);
    }
    
    public void setDistanceToDest(long distanceToDest)
    {
        this.distanceToDest = distanceToDest;
        Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : " + "(DistanceToDest) : " + distanceToDest);
    }
   
    public void setIsArrival(boolean isArrival)
    {
        this.isArrival = isArrival;
        Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : " + "(IsArrival) : " + isArrival);
    }
    
    public boolean isArrival()
    {
        return isArrival;
    }
    
    public void setArrivalFlag(String flag)
    {
        this.setAttribute(ATTR_ARRIVAL_FLAG, flag);
    }
    
    public void setArrivalGeofenceDistance(String distance)
    {
        this.setAttribute(ATTR_ARRIVAL_GEOFENCE_DISTANCE, distance);
    }
    
    public void setRemainingDistanceToTarget(long distance)
    {
        this.setAttribute(ATTR_REMAINING_DISTANCE_TO_TARGET_ON_EXIT, String.valueOf(distance));
    }
   
    public void updateNavInfo(boolean isArrival, long distToDest, String genFenseDistance)
    {
        setIsArrival(isArrival);
        setDistanceToDest(distToDest);
        setGenFenseDistance(genFenseDistance);
    }
    
    public void logArrivalConfirmation(Address destination, NavSdkNavEvent navEvent, boolean isArrival)
    {
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
        if (dest == null)
        {
            dest = destination.getStop();
        }
        
        //TODO
        TnLocation lastLocation = new TnLocation(null);
        
        int dist = 0;
        if (dest != null && lastLocation != null)
        {
            dist = DataUtil.gpsDistance(lastLocation.getLatitude() - dest.getLat(), lastLocation.getLongitude() - dest.getLon(),
                DataUtil.getCosLat(dest.getLat()));
            dist = (dist * 9119 / 8192);
        }

        long distToDest = navEvent.getDistanceToDest() * 9119 / 8192;
        
        if (dist > GENFENSE_DISTANCE_500 && dist <= GENFENSE_DISTANCE_1609)
        {
            if(isLogGenFense1609)
            {
                logGenFenseDist(distToDest,  IMisLogConstants.VALUE_DISTANCE_1609);
                isLogGenFense1609 = false;
            }
        }
        
        else if (dist > GENFENSE_DISTANCE_400 && dist <= GENFENSE_DISTANCE_500)
        {
            if(isLogGenFense500)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_500);
                isLogGenFense500 = false;
            }
        }
        
        else if (dist > GENFENSE_DISTANCE_350 && dist <= GENFENSE_DISTANCE_400)
        {
            if(isLogGenFense400)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_400);
                isLogGenFense400 = false;
                
            }
        }
        
        else if (dist >= GENFENSE_DISTANCE_300 && dist <= GENFENSE_DISTANCE_350)
        {
            if(isLogGenFense350)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_350);
                isLogGenFense350 = false;
            }
        }
        
        else if (dist > GENFENSE_DISTANCE_250 && dist <= GENFENSE_DISTANCE_300)
        {
            if(isLogGenFense300)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_300);
                isLogGenFense300 = false;
            }
        }
        
        else if (dist > GENFENSE_DISTANCE_200 && dist <= GENFENSE_DISTANCE_250)
        {
            if(isLogGenFense250)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_250);
                isLogGenFense250 = false;
            }
        }
        
        else if (dist > GENFENSE_DISTANCE_150 && dist <= GENFENSE_DISTANCE_200)
        {
            if(isLogGenFense200)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_200);
                isLogGenFense200 = false;
            }
        }
        
        else if (dist > GENFENSE_DISTANCE_100 && dist <= GENFENSE_DISTANCE_150)
        {
            if(isLogGenFense150)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_150);
                isLogGenFense150 = false;
            }
        }
        
        else if (dist > GENFENSE_DISTANCE_50 && dist <= GENFENSE_DISTANCE_100)
        {
            if(isLogGenFense100)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_100);
                isLogGenFense100 = false;
            }
        }
        
        else if (dist <= GENFENSE_DISTANCE_50)
        {
            if(isLogGenFense50)
            {
                logGenFenseDist(distToDest, IMisLogConstants.VALUE_DISTANCE_50);
                isLogGenFense50 = false;
            }
        }
    }
    
    private void logGenFenseDist(long distToDest, String arrivalDistType)
    {
        MisLogManager mislogManager =  MisLogManager.getInstance();
        ArrivalConfirmationMisLog arrivalConfirmationMisLog = (ArrivalConfirmationMisLog)mislogManager.getFactory().createArrivalConfirmationMisLog();
        arrivalConfirmationMisLog.updateNavInfo(isArrival, distToDest, arrivalDistType);
        Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, null, new Object[]{ arrivalConfirmationMisLog });
    }
}
