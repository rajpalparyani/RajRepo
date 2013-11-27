/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteSummaryModel.java
 *
 */
package com.telenav.module.nav.routesummary;

import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.module.nav.INavEngineListener;
import com.telenav.module.nav.IRouteSummaryListener;
import com.telenav.module.nav.NavParameter;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.nav.RouteSummaryManager;
import com.telenav.mvc.AbstractCommonModel;
import com.telenav.navsdk.nav.event.NavSdkNavEvent;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-4
 */
class RouteSummaryModel extends AbstractCommonModel implements IRouteSummaryConstants, INavEngineListener, INetworkStatusListener, IRouteSummaryListener
{
    public RouteSummaryModel()
    {
        boolean isOnBoardRoute = NavRunningStatusProvider.getInstance().isOnBoardRoute();
        if(!isOnBoardRoute)
        {
            NetworkStatusManager.getInstance().addStatusListener(this);
        }
    }

    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_INIT:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (isDynamicRoute)
                {
                    addNavEngineListener();
                }
                break;
            }
            case ACTION_GO_TO_TRAFFIC_SUMMARY:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (isDynamicRoute)
                {
                    removeNavEngineListener();
                }
                break;
            }
        }
    }

    protected void addNavEngineListener()   
    {
        NavSdkNavEngine.getInstance().addListener(this);
    }
    
    protected void removeNavEngineListener()
    {
        NavSdkNavEngine.getInstance().removeListener(this);
    }
    
    public void eventUpdate(NavSdkNavEvent navEvent)
    {
        handleNavInfoEvent(navEvent);
    }

    private void handleNavInfoEvent(NavSdkNavEvent navEvent)
    {
        NavParameter navParameter = new NavParameter();
        navParameter.distanceToTurn = navEvent.getDistanceToTurn();
        navParameter.nextStreetName = navEvent.getNextStreetName();
        navParameter.turnType = navEvent.getTurnType();
        navParameter.eta = navEvent.getEstimatedTime();
        navParameter.totalToDest = navEvent.getDistanceToDest();
        
        this.put(KEY_O_NAV_PARAMETER, navParameter);

        this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }
    

    /**
     * Release all model relative resources except parameters.
     */
    protected void releaseDelegate()
    {
        boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
        if (isDynamicRoute)
        {
            removeNavEngineListener();
        }
        NetworkStatusManager.getInstance().removeStatusListener(this);
        RouteSummaryManager.getInstance().removeRouteSummaryListener(this);
        super.releaseDelegate();
    }
    
    protected void activateDelegate(boolean isUpdateView)
    {
        RouteSummaryManager.getInstance().registerRouteSummaryListener(this);
    }
    
    protected void deactivateDelegate()
    {
        RouteSummaryManager.getInstance().removeRouteSummaryListener(this);
    }

    public void onBoard()
    {
        this.put(KEY_B_IS_ONBOARD, true);
        this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }

    public void offBoard()
    {
        this.put(KEY_B_IS_ONBOARD, false);
        this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }

    public void statusUpdate(boolean isConnected)
    {
        if (isConnected)
        {
            this.put(KEY_B_IS_ONBOARD, false);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
        else
        {
            this.put(KEY_B_IS_ONBOARD, true);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }

    public void routeSummaryUpdated()
    {
        this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }
}
