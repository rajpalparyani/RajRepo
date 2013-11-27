/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteSummaryController.java
 *
 */
package com.telenav.module.nav.routesummary;

import com.telenav.data.datatypes.address.Address;
import com.telenav.module.ModuleFactory;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-4
 */
public class RouteSummaryController extends AbstractCommonController implements IRouteSummaryConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
         //normal start.
         { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_ROUTE_SUMMARY, ACTION_INIT },
         
         //-------------------------------------------------------------
         // model events
         //-------------------------------------------------------------
         { STATE_ROUTE_SUMMARY, EVENT_MODEL_UPDATE_VIEW, STATE_ROUTE_SUMMARY, ACTION_NONE},
         //-------------------------------------------------------------
         // commands and buttons
         //-------------------------------------------------------------
         { STATE_ROUTE_SUMMARY, CMD_TRAFFIC_SUMMARY, STATE_GO_TO_TRAFFIC_SUMMARY, ACTION_NONE},
         { STATE_ROUTE_SUMMARY, CMD_MAP_SUMMARY, STATE_GO_TO_MAP_SUMMARY, ACTION_NONE},
         { STATE_ROUTE_SUMMARY, CMD_ROUTE_ITEM_SELECTED, STATE_GO_TO_TURN_MAP, ACTION_NONE},
         { STATE_ROUTE_SUMMARY, CMD_COMMON_LINK_TO_SEARCH, STATE_GO_TO_POI, ACTION_NONE},
         { STATE_ROUTE_SUMMARY, CMD_COMMON_BACK, STATE_VIRGIN, ACTION_NONE},
         { STATE_ROUTE_SUMMARY, CMD_NAVIGATION, STATE_VIRGIN, ACTION_NONE},
         { STATE_ROUTE_SUMMARY, CMD_DIRECTIONS, STATE_VIRGIN, ACTION_NONE},
         
         //-------------------------------------------------------------
         // controller events
         // they are all from turn map controller(child controller).
         //-------------------------------------------------------------
         { STATE_ANY, EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, STATE_ROUTE_SUMMARY, ACTION_INIT},
         { STATE_ANY, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, STATE_GO_TO_MAP_SUMMARY, ACTION_NONE},
         { STATE_ANY, EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY, STATE_GO_TO_TRAFFIC_SUMMARY, ACTION_NONE},
         { STATE_ANY, EVENT_CONTROLLER_GOTO_POI, STATE_GO_TO_POI, ACTION_NONE},
    };

    public RouteSummaryController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_GO_TO_TRAFFIC_SUMMARY:
            {
                if(superController != null)
                {
                    Parameter param = new Parameter();
                    param.put(KEY_O_CURRENT_CONTROLLER, this);
                    superController.activate(EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY, param);
                }
                break;
            }
            case STATE_GO_TO_MAP_SUMMARY:
            {
                if(superController != null)
                {
                    Parameter param = new Parameter();
                    param.put(KEY_O_CURRENT_CONTROLLER, this);
                    superController.activate(EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, param);
                }
                break;
            }
            case STATE_GO_TO_TURN_MAP:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                int currentSegIndex = model.getInt(KEY_I_CURRENT_SEGMENT_INDEX);
                if(isDynamicRoute)
                {
                    Address destAddress = (Address)model.get(KEY_O_ADDRESS_DEST);
                    ModuleFactory.getInstance().startTurnMapController(this, (Address)model.get(KEY_O_ADDRESS_ORI), destAddress, NavSdkRouteWrapper.getInstance().getCurrentRoute().getRouteID(), currentSegIndex);
                }
                else
                {
                    Parameter param = new Parameter();
                    param.put(KEY_I_CURRENT_SEGMENT_INDEX, currentSegIndex);
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_TURN_MAP, param);
                }
                break;
            }
            case STATE_GO_TO_POI:
            {
                postControllerEvent(EVENT_CONTROLLER_GOTO_POI, null);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new RouteSummaryModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new RouteSummaryViewTouch(new RouteSummaryUiDecorator());
    }

}
