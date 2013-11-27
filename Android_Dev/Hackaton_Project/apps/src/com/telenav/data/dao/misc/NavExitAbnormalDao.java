/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GuideToneDao.java
 *
 */
package com.telenav.data.dao.misc;

import com.telenav.data.dao.AbstractDao;
import com.telenav.persistent.TnStore;

/**
 * Resources which from AppStore will be stored here.
 * include GuideTone, CarModel ...
 * 
 *@author jxue
 *@date 2012-5-30
 */
public class NavExitAbnormalDao extends AbstractDao
{
    final static protected int KEY_TRIP_TIME_DOT = 1;
    
    final static protected int KEY_IS_NAV_RUNNING = 2;
    
    private TnStore navExitAbnormalDao;
    
    private long tripTimeDot = -1;
    
    private boolean isNavRunning = false;

    public NavExitAbnormalDao(TnStore store)
    {
        this.navExitAbnormalDao = store;
        byte[] data = navExitAbnormalDao.get(KEY_IS_NAV_RUNNING);
        if(data != null)
        {
            String s = new String(data);
            isNavRunning = Boolean.parseBoolean(s);
        }

        data = navExitAbnormalDao.get(KEY_TRIP_TIME_DOT);
        if(data != null)
        {
            String s = new String(data);
            try
            {
                tripTimeDot = Long.parseLong(s);
            }
            catch(Exception e)
            {
                tripTimeDot = -1;
            }
        }
    }
    
    public void setIsNavRunning(boolean isNavRunning)
    {
        this.isNavRunning = isNavRunning;
        navExitAbnormalDao.put(KEY_IS_NAV_RUNNING, ("" + isNavRunning).getBytes());
        
        store();
    }
    
    public void setTripTimeDot(long time)
    {
        this.tripTimeDot = time;
        if(tripTimeDot == -1)
        {
            navExitAbnormalDao.remove(KEY_TRIP_TIME_DOT);           
        }
        else
        {
            navExitAbnormalDao.put(KEY_TRIP_TIME_DOT, ("" + tripTimeDot).getBytes());
        }
        store();
    }
    
    public boolean isNavRunning()
    {
        return isNavRunning;
    }
    
    public long getTripTimeDot()
    {
        return tripTimeDot;
    }
    
    public void store()
    {
        navExitAbnormalDao.save();
    }

    public void clear()
    {
        navExitAbnormalDao.clear();
    }
    
}
