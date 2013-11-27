/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavRunningStatusProvider.java
 *
 */
package com.telenav.module.nav;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.NavExitAbnormalDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.sdk.kontagent.KontagentLogger;

/**
 *@author yning
 *@date 2012-4-25
 */
public class NavRunningStatusProvider
{
    private static NavRunningStatusProvider instance = new NavRunningStatusProvider();
    
    private boolean isNavRunning = false;
    private int navType = -1;
    
    public static final int NAV_TYPE_DYNAMIC_ROUTE = 1;
    public static final int NAV_TYPE_STATIC_ROUTE = 2;
    
    private boolean isOnBoardRoute = false;
    private long distanceTotal = -1;
    private Address destAddress = null;
    
    private NavRunningStatusProvider()
    {
        
    }
    
    public static NavRunningStatusProvider getInstance()
    {
        return instance;
    }
    
    public void setNavRunningStart(int navType)
    {
        isNavRunning = true;
        this.navType = navType;
        storeNavStatusIntoDao();
        KontagentAssistLogger.startKtLogNavigation();
    }
    
    public void setDistanceToDestination(long dis)
    {
        distanceTotal = dis;
    }
    
    public long getDistanceToDestination()
    {
        return distanceTotal;
    }
    
    public void setDestination(Address addr)
    {
        destAddress = addr;
    }
    
    public Address getDestination()
    {
        return destAddress;
    }
    
    protected void storeNavStatusIntoDao()
    {
     // Fix TNANDROID-904
        if (this.navType == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE)
        {
            DaoManager.getInstance().getNavExitAbnormalDao().setIsNavRunning(true);
        }
    }
    
    public void setNavRunningEnd()
    {
        clearNavStatusFromDao();
        if(isNavRunning)
        {
            KontagentAssistLogger.endLogNavigationDuration();
        };
        isNavRunning = false;
        navType = -1;
    }
    
    protected void clearNavStatusFromDao()
    {
     // Fix TNANDROID-904
        if (this.navType == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE)
        {
            NavExitAbnormalDao navExitAbnormalDao = DaoManager.getInstance().getNavExitAbnormalDao();
            navExitAbnormalDao.setIsNavRunning(false);
            navExitAbnormalDao.clear();
        }
    }
    
    public boolean isNavRunning()
    {
        return isNavRunning;
    }
    
    public int getNavType()
    {
        return navType;
    }
    
    public boolean isOnBoardRoute()
    {
        return isOnBoardRoute;
    }
    
    public void setIsOnBoard(boolean isOnBoard)
    {
        this.isOnBoardRoute = isOnBoard;
    }
}
