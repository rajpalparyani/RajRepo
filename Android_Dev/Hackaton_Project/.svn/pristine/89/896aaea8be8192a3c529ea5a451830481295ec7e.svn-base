/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TurnMapController.java
 *
 */
package com.telenav.module.nav.turnmap;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.module.ModuleFactory;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author yning
 *@date 2010-12-13
 */
public class TurnMapController extends AbstractCommonController implements ITurnMapConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {
        //normal start
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
        
        //-------------------------------------------------------------
        // model events
        //-------------------------------------------------------------
        //If it's from route planning, we should request route first.
        { STATE_INIT, EVENT_MODEL_GET_TURN_MAP, STATE_GETTING_TURN_MAP, ACTION_GETTING_TURN_MAP },
        { STATE_GETTING_TURN_MAP, CMD_COMMON_BACK, STATE_START_MAIN, ACTION_NONE },  
        //Handle EVENT_MODEL_POST_ERROR, if it handled by common state_table, it will go pre-state, but there is no pre-state
        { STATE_GETTING_TURN_MAP, EVENT_MODEL_POST_ERROR, STATE_GET_TURN_MAP_ERROR, ACTION_NONE},
        { STATE_GET_TURN_MAP_ERROR, CMD_COMMON_OK, STATE_START_MAIN, ACTION_NONE},
        { STATE_GET_TURN_MAP_ERROR, CMD_COMMON_BACK, STATE_START_MAIN, ACTION_NONE},
         
        //else if it's from route summary, we just need to show it.
        { STATE_ANY, EVENT_MODEL_SHOW_TURN_MAP, STATE_TURN_MAP, ACTION_NONE },
        //Edges can be lacked when go through turns. Request it.
        { STATE_ANY, EVENT_MODEL_REQUEST_ROUTE_EDGE, STATE_GET_ROUTE_EDGE, ACTION_GET_ROUTE_EDGE },
        //note here we cancel the network.
        { STATE_GET_ROUTE_EDGE, CMD_COMMON_BACK, STATE_PREV, ACTION_CANCEL_NETWORK },
        { STATE_GET_EXTRA_ROUTE_EDGE, CMD_COMMON_BACK, STATE_PREV, ACTION_CANCEL_NETWORK },
        
        //-------------------------------------------------------------
        // commands and buttons
        //-------------------------------------------------------------
        //The event when pressing the two arrows.
        { STATE_TURN_MAP, CMD_CANVAS_NEXT, STATE_TURN_MAP, ACTION_CANVAS_NEXT},
        { STATE_TURN_MAP, CMD_CANVAS_PREVIOUS, STATE_TURN_MAP, ACTION_CANVAS_PREVIOUS},
        //for the bottom commands.
        { STATE_TURN_MAP, CMD_ROUTE_SUMMARY, STATE_GO_TO_ROUTE_SUMMARY, ACTION_NONE },
        { STATE_TURN_MAP, CMD_TRAFFIC_SUMMARY, STATE_GO_TO_TRAFFIC_SUMMARY, ACTION_NONE },
        { STATE_TURN_MAP, CMD_MAP_SUMMARY, STATE_GO_TO_MAP_SUMMARY, ACTION_NONE },
        { STATE_TURN_MAP, CMD_COMMON_LINK_TO_SEARCH, STATE_GO_TO_POI, ACTION_NONE },
        //Per UI-FLOW, there should be a menu item to go to navigation.
        { STATE_TURN_MAP, CMD_NAVIGATE, STATE_GO_TO_NAV, ACTION_NONE },
        //The event when pinch up to zoom out
        { STATE_ANY, CMD_REQUEST_EXTRA_ROUTE, STATE_TURN_MAP, ACTION_GET_EXTRA_ROUTE_EDGE },
        
        //-------------------------------------------------------------
        // controller events
        //-------------------------------------------------------------
        //most likely from Route Summary module if it's a child of this controller.
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_TURN_MAP, STATE_TURN_MAP, ACTION_SHOW_TURN_MAP },
        //press search button in summaries.
        { STATE_ANY, EVENT_CONTROLLER_GOTO_POI, STATE_GO_TO_POI, ACTION_NONE},
        //go to route summary.If search can jump to summaries later, we need below lines.
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, STATE_GO_TO_ROUTE_SUMMARY, ACTION_NONE },
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, STATE_GO_TO_MAP_SUMMARY, ACTION_NONE },
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY, STATE_GO_TO_TRAFFIC_SUMMARY, ACTION_NONE },
        
        //-------------------------------------------------------------
        // exit from this module
        //-------------------------------------------------------------
        //if our super is route summary module, just go back
        { STATE_TURN_MAP, CMD_COMMON_BACK, STATE_VIRGIN, ACTION_NONE },
        //else, it means we are in static directions module, need show exit confirm.
        //confirm to exit.
        
        { STATE_ANY, EVENT_CONTROLLER_EXIT_NAV, STATE_START_MAIN, ACTION_NONE},
        
        { STATE_ANY, EVENT_CONTROLLER_RENEW_STATIC_ROUTE, STATE_INIT, ACTION_INIT},
        
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_TURN_MAP, STATE_TURN_MAP, ACTION_NONE},
        
        //handle ssoToken expired cases
        { STATE_ANY, EVENT_MODEL_INVALID_IDENTITY, STATE_SYNC_PURCHASE, ACTION_SYNC_PURCHASE },
        { STATE_SYNC_PURCHASE, EVENT_MODEL_GET_TURN_MAP, STATE_CONTINUE_GETTING_TURN_MAP, ACTION_GETTING_TURN_MAP },
        
    };
    
    public TurnMapController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_START_MAIN:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (!isDynamicRoute)
                {
                    releaseAll();
                    MediaManager.getInstance().getAudioPlayer().cancelAll();
                    NavRunningStatusProvider.getInstance().setNavRunningEnd();
                    ModuleFactory.getInstance().startMain();
                }
                break;
            }
            case STATE_GO_TO_ROUTE_SUMMARY:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (!isDynamicRoute)
                {
                    Address dest = (Address)model.get(KEY_O_ADDRESS_DEST);
                    Address orig = (Address)model.get(KEY_O_ADDRESS_ORI);
                    TurnMapModel turnMapModel = (TurnMapModel) model;
                    TurnMapWrapper wrapper = turnMapModel.getTurnMapWrapper();
                    int currentSegIndex = wrapper.getCurrentSegmentIndex();
                    boolean isFromSearchAlong = false;
                    
                    ModuleFactory.getInstance().startSummaryControlController(this, EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, orig, dest, null,
                        currentSegIndex, isFromSearchAlong, model.getInt(KEY_I_AC_TYPE));
                }
                else
                {
                    Parameter param = new Parameter();
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, param);
                }
                break;
            }
            case STATE_GO_TO_TRAFFIC_SUMMARY:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (!isDynamicRoute)
                {
                    Address dest = (Address)model.get(KEY_O_ADDRESS_DEST);
                    Address orig = (Address)model.get(KEY_O_ADDRESS_ORI);
                    TurnMapModel turnMapModel = (TurnMapModel) model;
                    TurnMapWrapper wrapper = turnMapModel.getTurnMapWrapper();
                    int currentSegIndex = wrapper.getCurrentSegmentIndex();
                    boolean isFromSearchAlong = false;
                    
                    ModuleFactory.getInstance().startSummaryControlController(this, EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY, orig, dest,
                        null, currentSegIndex, isFromSearchAlong, model.getInt(KEY_I_AC_TYPE));
                }
                else
                {
                    Parameter param = new Parameter();
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY, param);
                }
                break;
            }
            case STATE_GO_TO_POI:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (!isDynamicRoute)
                {
                    Address destAddress = (Address)model.get(KEY_O_ADDRESS_DEST);
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startCategorySearchController(this, TYPE_AC_FROM_TURN_MAP, IPoiSearchProxy.TYPE_SEARCH_ADDRESS, -1, -1, -1, null, destAddress, null, userProfileProvider, false);
                }
                else
                {
                    postControllerEvent(EVENT_CONTROLLER_GOTO_POI, null);
                }
                break;
            }
            case STATE_GO_TO_NAV:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (!isDynamicRoute)
                {
                 // fix bug http://jira.telenav.com:8080/browse/TNANDROID-3426
//                  NavRunningStatusProvider.getInstance().setNavRunningEnd();
                    Address dest = (Address) model.get(KEY_O_ADDRESS_DEST);
                    ModuleFactory.getInstance().startNavController(this, null, dest, model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS),
                        false, false);
                }
                else
                {
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_MOVING_MAP, null);
                }
                break;
            }
            case STATE_GO_TO_MAP_SUMMARY:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if (!isDynamicRoute)
                {
                    Address dest = (Address)model.get(KEY_O_ADDRESS_DEST);
                    Address orig = (Address)model.get(KEY_O_ADDRESS_ORI);
                    TurnMapModel turnMapModel = (TurnMapModel) model;
                    TurnMapWrapper wrapper = turnMapModel.getTurnMapWrapper();
                    int currentSegIndex = wrapper.getCurrentSegmentIndex();
                    boolean isFromSearchAlong = false;

                    ModuleFactory.getInstance().startSummaryControlController(this, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, orig, dest, null, currentSegIndex, isFromSearchAlong, model.getInt(KEY_I_AC_TYPE));
                }
                else
                {
                    Parameter param = new Parameter();
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, param);
                }
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new TurnMapModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new TurnMapViewTouch(new TurnMapUiDecorator());
    }

}
