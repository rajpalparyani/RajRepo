/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavModel.java
 *
 */
package com.telenav.module.nav;

import android.content.Context;
import android.widget.Toast;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.TripSummaryMisLog;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCarConnect;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.carconnect.host.CarConnectHostManager;
import com.telenav.ui.citizen.map.MapVehiclePositionService;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-2
 */
class NavModel extends AbstractCommonNetworkModel implements INavConstants
{
    
//    boolean isRoutePlanningStarted = false;

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
//                initMapArrow();
                MapVehiclePositionService.getInstance().resume(false);
            	if(!NavRunningStatusProvider.getInstance().isNavRunning())
            	{
            		DaoManager.getInstance().getTripsDao().initTripPreference();
            	}
                int multiRouteServerDrivenValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_ROUTE_PLANNING_MULTI_ROUTE);
                boolean isMultiRouteEnabled = multiRouteServerDrivenValue == FeaturesManager.FE_ENABLED || multiRouteServerDrivenValue == FeaturesManager.FE_PURCHASED;

                boolean isIgnoreRoutePlanning = this.getBool(KEY_B_IS_IGNORE_ROUTE_PLANNING);
                boolean isCarConnectMode = this.getBool(KEY_B_IS_CAR_CONNECT_MODE);
                boolean isNavRunning=NavRunningStatusProvider.getInstance().isNavRunning();
                int navRouteType = NavRunningStatusProvider.getInstance().getNavType();
                if (isCarConnectMode)
                {
                    postModelEvent(EVENT_MODEL_START_CAR_CONNECT_NAV);
                    storeAddress();
                }
                if ((isNavRunning && navRouteType == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE) || isIgnoreRoutePlanning)
                {
                    postModelEvent(EVENT_MODEL_START_MOVING_MAP);
                    storeAddress();
                }
                else
                {
                    if(isMultiRouteEnabled)
                    {
                        postModelEvent(EVENT_MODEL_START_ROUTE_PLANNING);
                        storeAddress();
                    }
                    else
                    {
                        postModelEvent(EVENT_MODEL_START_ROUTE_PLANNING_SETTING);
                    }
                }
                break;
            }
            case ACTION_START_MOVING_MAP:
            case ACTION_START_TURN_MAP:
            {
                boolean isSearchAlongRoute = getBool(KEY_B_IS_FROM_SEARCH_ALONG);
                if (isSearchAlongRoute)
                {
                    TripSummaryMisLog tripSummaryMisLog = (TripSummaryMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_TRIP_SUMMARY);
                    if(tripSummaryMisLog != null)
                    {
                        tripSummaryMisLog.setIsEndedByDetour(true);
                    }
                }
                postModelEvent(EVENT_MODEL_RELEASE_ALL_PREVIOUS_MODULES);
                
                break;
            }
            //Added for Ford Applink (CarConnect
            case ACTION_LAUNCH_CAR_CONNECT_NAV:
            {
                Address destination = (Address) get(KEY_O_ADDRESS_DEST);
                if (destination != null)
                {
                    CarConnectHostManager.getInstance().requestNav(destination);
                    //show toast message to inform user 
                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Context ctx=AndroidPersistentContext.getInstance().getContext();
                            String text = ResourceManager.getInstance().getCurrentBundle().getString(IStringCarConnect.GETTING_ROUTE, IStringCarConnect.FAMILY_CARCONNECT);
                            Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
            }
        }
    }
    
    protected void initMapArrow()
    {
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        String currentCarValue = preferenceDao.getStrValue(Preference.ID_PREFERENCE_CAR_MODEL);
        if(currentCarValue == null || currentCarValue.length() == 0)
        {
            currentCarValue = "arrow";
        }
        
        MapContainer.getInstance().addCarModel(null,currentCarValue+".mod");
    }

    protected void storeAddress()
    {
        Address originAddress = (Address) get(KEY_O_ADDRESS_ORI);
        Address destAddress = (Address) get(KEY_O_ADDRESS_DEST);
        NavRunningStatusProvider.getInstance().setDestination(destAddress);
        DaoManager.getInstance().getAddressDao().addToRecent(originAddress);
        DaoManager.getInstance().getAddressDao().addToRecent(destAddress);
    }

    public void noGpsTimeout()
    {
        // TODO Auto-generated method stub
        
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        // TODO Auto-generated method stub
        
    }
}
