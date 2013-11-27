/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavController.java
 *
 */
package com.telenav.module.nav;

import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.TripsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.location.TnLocation;
import com.telenav.module.ModuleFactory;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.HomeScreenManager;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.kontagent.KontagentAssistLogger;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-2
 */
public class NavController extends AbstractCommonController implements INavConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
         { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
         { STATE_INIT, EVENT_MODEL_START_ROUTE_PLANNING, STATE_GO_TO_ROUTE_PLANNING, ACTION_NONE },
         { STATE_INIT, EVENT_MODEL_START_ROUTE_PLANNING_SETTING, STATE_GO_TO_ROUTE_PLANNING_SETTING, ACTION_NONE },
         { STATE_INIT, EVENT_MODEL_START_MOVING_MAP, STATE_GO_TO_MOVING_MAP, ACTION_START_MOVING_MAP },
         { STATE_ANY, EVENT_CONTROLLER_GO_TO_MOVING_MAP, STATE_GO_TO_MOVING_MAP, ACTION_START_MOVING_MAP },
         { STATE_ANY, EVENT_CONTROLLER_GO_TO_TURN_MAP, STATE_GO_TO_TURN_MAP, ACTION_START_TURN_MAP },
         { STATE_ANY, EVENT_CONTROLLER_LINK_TO_MAP, STATE_GO_HOME, ACTION_NONE },
         { STATE_ANY, EVENT_CONTROLLER_LINK_TO_AC, STATE_GO_HOME, ACTION_NONE },
         
         //plugin
         { STATE_ANY, EVENT_CONTROLLER_GOTO_PLUGIN, STATE_GO_TO_PLUGIN, ACTION_NONE },
         
         //Ford Applink (CarConnect)
         {STATE_ANY, EVENT_MODEL_START_CAR_CONNECT_NAV, STATE_GO_HOME, ACTION_LAUNCH_CAR_CONNECT_NAV},
    };
    
    
    public NavController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_GO_TO_ROUTE_PLANNING:
            {
                Address origAddress = (Address) model.fetch(KEY_O_ADDRESS_ORI);
                Address destAddress = (Address) model.fetch(KEY_O_ADDRESS_DEST); 
                boolean isFromDSR = (Boolean) model.fetch(KEY_B_IS_FROM_DSR);
                boolean needShareEta = model.fetchBool(KEY_B_NEED_SHARE_ETA);
                ModuleFactory.getInstance().startRoutePlanningController(this, EVENT_CONTROLLER_START, origAddress,
                    destAddress, model.fetchVector(KEY_V_STOP_AUDIOS), model.fetchBool(KEY_B_IS_FROM_SEARCH_ALONG), model.fetchBool(KEY_B_IS_RESUME_TRIP), isFromDSR, needShareEta, model.fetchBool(KEY_B_IS_FROM_DWF));
                break;
            }
            case STATE_GO_TO_ROUTE_PLANNING_SETTING:
            {
                Address origAddress = (Address) model.fetch(KEY_O_ADDRESS_ORI);
                Address destAddress = (Address) model.fetch(KEY_O_ADDRESS_DEST);   
                boolean isFromDSR = (Boolean) model.fetch(KEY_B_IS_FROM_DSR);
                boolean needShareEta = model.fetchBool(KEY_B_NEED_SHARE_ETA);
                ModuleFactory.getInstance().startRoutePlanningController(this, EVENT_CONTROLLER_START_ROUTE_PLANNING_SETTING, origAddress,
                    destAddress, model.fetchVector(KEY_V_STOP_AUDIOS), model.fetchBool(KEY_B_IS_FROM_SEARCH_ALONG), model.fetchBool(KEY_B_IS_RESUME_TRIP), isFromDSR, needShareEta);
                break;
            }
            case STATE_GO_TO_MOVING_MAP:
            {
                Address destAddress = (Address)model.fetch(KEY_O_ADDRESS_DEST);
                String tinyUrl = (String)model.fetch(KEY_S_TINY_URL_ETA);
                int routeId = model.fetchInt(KEY_I_ROUTE_ID);
                boolean isSearchAlongRoute = model.fetchBool(KEY_B_IS_FROM_SEARCH_ALONG);
                TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
                if (isSearchAlongRoute)
                {
                    tripsDao.setDetourTrip(destAddress);
                }
                else
                {
                    tripsDao.setNormalTrip(destAddress);
                }
                tripsDao.store();
                
                TnLocation currentLocation = (TnLocation)model.get(KEY_O_CURRENT_LOCATION);
                 
                NavRunningStatusProvider.getInstance().setNavRunningStart(NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE);
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
            	// end changed
                ModuleFactory.getInstance().startMovingMapController(this, destAddress, poiDataWrapper, tinyUrl, routeId, isSearchAlongRoute, currentLocation);
                break;
            }
            case STATE_GO_TO_TURN_MAP:
            {
                Address origin = (Address)model.fetch(KEY_O_ADDRESS_ORI);
                Address dest = (Address)model.fetch(KEY_O_ADDRESS_DEST);
                int routeId = model.fetchInt(KEY_I_ROUTE_ID);
                int currentSegIndex = 0;
                NavRunningStatusProvider.getInstance().setNavRunningStart(NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE);
                ModuleFactory.getInstance().startTurnMapController(this, origin, dest, routeId, currentSegIndex);
                break;
            }
            case STATE_GO_HOME:
            {
                if(superController == null)
                {
                    releaseAll();
                    NavRunningStatusProvider.getInstance().setNavRunningEnd();
                    ModuleFactory.getInstance().startMain();
                }
                else
                {
                    postControllerEvent(HomeScreenManager.getHomeScreenEventID(), null);
                }
                break;
            }
            case STATE_GO_TO_PLUGIN:
            {
                Parameter data = new Parameter();
                Hashtable plugRequest = (Hashtable)model.get(KEY_O_PLUGIN_REQUEST);
                data.put(KEY_O_PLUGIN_REQUEST, plugRequest);
                if(superController == null)
                {
                    ModuleFactory.getInstance().startMain(EVENT_CONTROLLER_GOTO_PLUGIN, plugRequest);
                    this.release();
                }
                else
                {
                    postControllerEvent(EVENT_CONTROLLER_GOTO_PLUGIN, data);
                }
                
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new NavModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new NavViewTouch(new NavUiDecorator());
    }

}
