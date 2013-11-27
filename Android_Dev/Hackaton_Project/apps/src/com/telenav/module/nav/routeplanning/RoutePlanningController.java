/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RoutePlanningController.java
 *
 */
package com.telenav.module.nav.routeplanning;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;
import com.telenav.module.ModuleFactory;
import com.telenav.module.upsell.FeaturesManager;
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
public class RoutePlanningController extends AbstractCommonController implements IRoutePlanningConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
        { STATE_ANY, EVENT_MODEL_SHOW_ROUTE_PLANNING, STATE_ROUTE_PLANNING, ACTION_NONE },
        
        //no GPS handling.
        { STATE_ROUTE_PLANNING, EVENT_MODEL_NO_GPS_TIMEOUT, STATE_NO_GPS_TIMEOUT, ACTION_NONE },
        { STATE_NO_GPS_TIMEOUT, CMD_RETRY_GPS, STATE_ROUTE_PLANNING, ACTION_ROUTE_PLANNING_RETRY_GPS },
        { STATE_NO_GPS_TIMEOUT, CMD_COMMON_BACK, STATE_RETURN_TO_DASHBOARD, ACTION_NONE },
        
        //Route Plan screen
        { STATE_ROUTE_PLANNING, CMD_NAVIGATE, STATE_CHECK_TINY_URL_QEQUIRED, ACTION_CHECK_SHARE_ETA },
        { STATE_CHECK_TINY_URL_QEQUIRED, EVENT_MODEL_NEED_SHARE, STATE_REQUEST_TINY_URL, ACTION_REQUEST_TINY_URL},
        { STATE_CHECK_TINY_URL_QEQUIRED, EVENT_MODEL_NO_SHARE, STATE_CHECK_ACCOUNT_STATUS, ACTION_CHECK_ACCOUNT_STATUS},
        { STATE_REQUEST_TINY_URL, EVENT_MODEL_SHARE_GOT_TINY_URL, STATE_CHECK_SHARE_ETA, ACTION_ETA_SHARE},
        { STATE_REQUEST_TINY_URL, EVENT_MODEL_SHARE_GOT_TINY_URL_FAIL, STATE_CHECK_ACCOUNT_STATUS, ACTION_CHECK_ACCOUNT_STATUS},
        { STATE_CHECK_SHARE_ETA, EVENT_MODEL_SHARE_ETA_FINISH, STATE_CHECK_ACCOUNT_STATUS, ACTION_CHECK_ACCOUNT_STATUS},

        { STATE_CHECK_ACCOUNT_STATUS, EVENT_MODEL_GO_PURCHASE, STATE_GOTO_PURCHASE, ACTION_NONE },
        { STATE_CHECK_ACCOUNT_STATUS, EVENT_MODEL_GO_NAVIGATE, STATE_GOTO_NAVIGATE, ACTION_NONE },
        
        { STATE_ROUTE_PLANNING, CMD_DIRECTIONS, STATE_GOTO_DIRECTIONS, ACTION_NONE },
        { STATE_ROUTE_PLANNING, CMD_UPDATE_PLAN_INFO, STATE_ROUTE_PLANNING, ACTION_NONE },
        { STATE_ROUTE_PLANNING, CMD_CHANGE_SETTING, STATE_CHANGE_SETTING_SCREEN, ACTION_CANCEL_CURRENT_REQUEST },
        { STATE_ROUTE_PLANNING, CMD_COMMON_BACK, STATE_VIRGIN, ACTION_PREFENCE_RESET },
        
        //change setting screen
        { STATE_CHANGE_SETTING_SCREEN, CMD_GET_ROUTE, STATE_CHECK_MULTIROUTE_SERVER_DRIVEN_VALUE, ACTION_CHECK_MULTI_ROUTE_VALUE },
        { STATE_CHECK_MULTIROUTE_SERVER_DRIVEN_VALUE, EVENT_MODEL_START_ROUTE_PLANNING, STATE_INIT, ACTION_INIT },
        { STATE_CHECK_MULTIROUTE_SERVER_DRIVEN_VALUE, EVENT_MODEL_START_GET_ROUTE, STATE_START_GETTING_ROUTE, ACTION_NONE },
        
        { STATE_CHANGE_SETTING_SCREEN, CMD_CHANGE_ORIGIN, STATE_GO_TO_AC, ACTION_NONE },
        { STATE_CHANGE_SETTING_SCREEN, CMD_CHANGE_DESTINATION, STATE_GO_TO_AC, ACTION_NONE },
        { STATE_CHANGE_SETTING_SCREEN, CMD_CHANGE_ROUTE_SETTING, STATE_GO_TO_ROUTE_SETTING, ACTION_NONE },
        { STATE_CHANGE_SETTING_SCREEN, CMD_CHANGE_VOICE_SETTING, STATE_GO_TO_VOICE_SETTING, ACTION_NONE },
        { STATE_CHANGE_SETTING_SCREEN, CMD_COMMON_BACK, STATE_ROUTE_PLANNING, ACTION_BACK_TO_ROUTE_PLANNING},
        { STATE_GO_TO_AC, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_CHANGE_SETTING_SCREEN, ACTION_CHECK_RETURN_ADDRESS },
        { STATE_ANY, EVENT_CONTROLLER_PREFERENCE_SAVED, STATE_PREV, ACTION_NONE },
        
        //common network related error.
        { STATE_COMMON_ERROR, CMD_COMMON_OK, STATE_COMMOM_ERROR_BACK_TO_DASHBOARD, ACTION_NONE},
        { STATE_COMMON_ERROR, CMD_COMMON_BACK, STATE_COMMOM_ERROR_BACK_TO_DASHBOARD, ACTION_NONE},
        
        { STATE_VIRGIN, EVENT_CONTROLLER_START_ROUTE_PLANNING_SETTING, STATE_CHANGE_SETTING_SCREEN, ACTION_INIT_ROUTE_SETTING },
    };

    public RoutePlanningController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_GOTO_NAVIGATE:
            {
                int routeId = -1;
                Object obj = model.get(KEY_A_ROUTE_CHOICES);
                if(obj != null)
                {
                    Route[] choices = (Route[])obj;
                    int choiceIndex = model.getInt(KEY_I_SELECTED_PLAN);
                    if(choiceIndex < choices.length)
                    {
                        Route selectedRoute = choices[choiceIndex];
                        routeId = selectedRoute.getRouteID();
                        TnLocation currentLocation = (TnLocation)model.get(KEY_O_CURRENT_LOCATION);
                        Parameter param = new Parameter();
                        param.put(KEY_O_ADDRESS_DEST, model.get(KEY_O_ADDRESS_DEST));
                        param.put(KEY_S_TINY_URL_ETA, model.get(KEY_S_TINY_URL_ETA));
                        param.put(KEY_I_ROUTE_ID, routeId);
                        param.put(KEY_B_IS_FROM_SEARCH_ALONG, model.get(KEY_B_IS_FROM_SEARCH_ALONG));
                        param.put(KEY_O_CURRENT_LOCATION, currentLocation);
                        int[] lengths = (int[])model.get(KEY_A_ROUTE_CHOICES_LENGTH);
                        KontagentAssistLogger.ktLogNavigationDistance(lengths[choiceIndex]);
                        postControllerEvent(EVENT_CONTROLLER_GO_TO_MOVING_MAP, param);
                        break;
                    }
                }
                throw new IllegalArgumentException("Route ID is not valid");
            }
            case STATE_GOTO_PURCHASE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startUpSellController(this, FeaturesManager.FEATURE_CODE_DYNAMIC_ROUTE, false, userProfileProvider);
                break;
            }
            case STATE_GOTO_DIRECTIONS:
            {
                Address origin = (Address)model.get(KEY_O_ADDRESS_ORI);
                Address dest = (Address)model.get(KEY_O_ADDRESS_DEST);
                int routeId = -1;
                Object obj = model.get(KEY_A_ROUTE_CHOICES);
                if(obj != null)
                {
                    Route[] choices = (Route[])obj;
                    int choiceIndex = model.getInt(KEY_I_SELECTED_PLAN);
                    if(choiceIndex < choices.length)
                    {
                        Route selectedRoute = choices[choiceIndex];
                        routeId = selectedRoute.getRouteID();
                        Parameter param = new Parameter();
                        param.put(KEY_O_ADDRESS_ORI, origin);
                        param.put(KEY_O_ADDRESS_DEST, dest);
                        param.put(KEY_I_ROUTE_ID, routeId);
                        postControllerEvent(EVENT_CONTROLLER_GO_TO_TURN_MAP, param);
                        break;
                    }
                }
                
                throw new IllegalArgumentException("Route ID is not valid");
            }
            case STATE_GO_TO_AC:
            {
                boolean isCurrentLocationNeeded = false;
                if(model.getBool(KEY_B_IS_EDITING_ORIGIN))
                {
                    Address origin = (Address)model.get(KEY_O_ADDRESS_ORI);
                    Stop originStop = origin.getStop();
                    if (originStop != null && originStop.getType() == Stop.STOP_CURRENT_LOCATION)
                    {
                        isCurrentLocationNeeded = false;
                    }
                    else
                    {
                        isCurrentLocationNeeded = true;
                    }
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAcController(this, TYPE_AC_FROM_ROUTE_PLAN, true, isCurrentLocationNeeded, false, userProfileProvider);
                break;
            }
            case STATE_GO_TO_ROUTE_SETTING:
            {
                boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
                ModuleFactory.getInstance().startRouteSettingController(this, EVENT_CONTROLLER_GO_TO_ROUTE_SETTING, true, isOnboard);
                break;
            }
            case STATE_GO_TO_VOICE_SETTING:
            {
                boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
                ModuleFactory.getInstance().startRouteSettingController(this, EVENT_CONTROLLER_GO_TO_VOICE_SETTING, true, isOnboard);
                break;
            }
            case STATE_VIRGIN:
            {
                if(this.model.getBool(KEY_B_IS_FROM_DWF))
                {
                    postControllerEvent(HomeScreenManager.getHomeScreenEventID(), null);
                }
                break;
            }
            case STATE_COMMOM_ERROR_BACK_TO_DASHBOARD:
            {
            	postControllerEvent(HomeScreenManager.getHomeScreenEventID(), null);
                break;
            }
            case STATE_START_GETTING_ROUTE:
            {
                Parameter param = new Parameter();
                Address origin = (Address)model.get(KEY_O_ADDRESS_ORI);
                Address dest = (Address)model.get(KEY_O_ADDRESS_DEST);
                param.put(KEY_O_ADDRESS_ORI, origin);
                param.put(KEY_O_ADDRESS_DEST, dest);
                if(origin != null && origin.getStop() != null && origin.getStop().getType() == Stop.STOP_CURRENT_LOCATION)
                {
                    if (FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_DYNAMIC_ROUTE) == FeaturesManager.FE_UNPURCHASED)
                    {
                        IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                        ModuleFactory.getInstance().startUpSellController(this, FeaturesManager.FEATURE_CODE_DYNAMIC_ROUTE, false, userProfileProvider);
                    }
                    else
                    {
                        postControllerEvent(EVENT_CONTROLLER_GO_TO_MOVING_MAP, param);
                    }
                }
                else
                {
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_TURN_MAP, param);
                }
                break;
            }
            case STATE_RETURN_TO_DASHBOARD:
            {
                releaseAll();
                ModuleFactory.getInstance().startMain();
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new RoutePlanningModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new RoutePlanningViewTouch(new RoutePlanningUiDecorator());
    }

}
