/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficController.java
 *
 */
package com.telenav.module.nav.traffic;

import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-10-26
 */
public class TrafficController extends AbstractCommonController implements ITrafficConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {
        //normal start
        {STATE_VIRGIN,      EVENT_CONTROLLER_START,         STATE_GET_SUMMARY,  ACTION_GET_TRAFFIC_SUMMARY}, 
        
        //-------------------------------------------------------------
        // model events
        //-------------------------------------------------------------
        {STATE_GET_SUMMARY,      EVENT_MODEL_SHOW_SUMMARY,         STATE_SHOW_SUMMARY,  ACTION_NONE},
        {STATE_GET_SUMMARY,      CMD_COMMON_BACK,         STATE_BACK_TO_SUMMARY_CONTROLLER,  ACTION_NONE},
        
        // Check passed, show traffic detail for segment.
        {STATE_CHECK_SEGMENT, EVENT_MODEL_SHOW_TRAFFIC_SEGMENT,  STATE_SHOW_TRAFFIC_SEGMENT, ACTION_SHOW_TRAFFIC_SEGMENT},
        
        // Check Failed, go back.
        {STATE_CHECK_SEGMENT, EVENT_MODEL_TRAFFIC_SEGMENT_ERROR, STATE_PREV, ACTION_NONE},
        
        // Got alternate route and show it.
        { STATE_ANY, EVENT_MODEL_ALTERNATE_ROUTE, STATE_ALTERNATE_ROUTE, ACTION_NONE },
        
        //-------------------------------------------------------------
        // commands and buttons
        //-------------------------------------------------------------
        {STATE_SHOW_SUMMARY,      CMD_ROUTE_SUMMARY,         STATE_GO_TO_ROUTE_SUMMARY,  ACTION_NONE},
        
        {STATE_SHOW_SUMMARY,      CMD_MAP_SUMMARY,         STATE_GO_TO_MAP_SUMMARY,  ACTION_NONE},
        
        {STATE_SHOW_SUMMARY,      CMD_COMMON_LINK_TO_SEARCH,         STATE_GO_TO_POI,  ACTION_NONE},
        
        {STATE_SHOW_SUMMARY,      CMD_DIRECTIONS,         STATE_VIRGIN,  ACTION_NONE},
        
        {STATE_SHOW_SUMMARY,      CMD_NAVIGATION,         STATE_VIRGIN,  ACTION_NONE},
        
        {STATE_SHOW_SUMMARY, CMD_MINIMIZE_ALL_DELAY, STATE_SEND_AVOID, ACTION_MINIMIZE_ALL_DELAY },
        
        {STATE_SHOW_SUMMARY, CMD_PLAY_AUDIO, STATE_SHOW_SUMMARY, ACTION_PLAY_AUDIO},
        // In traffic summary screen, before view detail, we should check traffic segment first.
        {STATE_SHOW_SUMMARY, CMD_VIEW_DETAIL, STATE_CHECK_SEGMENT, ACTION_CHECK_TRAFFIC_SEGMENT},
        
        // Any time we get avoid selected request ...
        { STATE_ANY, CMD_AVOID_SELECTED, STATE_SEND_AVOID, ACTION_AVOID_SELECTED },
        
        // In alternate route screen, accept reroute.
        { STATE_ALTERNATE_ROUTE, CMD_ACCEPT_REROUTE, STATE_SEND_REROUTE, ACTION_REROUTE },
        { STATE_ALTERNATE_ROUTE, CMD_COMMON_BACK, STATE_GO_BACK, ACTION_NONE },
        
        {STATE_SHOW_SUMMARY, CMD_COMMON_BACK, STATE_VIRGIN, ACTION_NONE},
        {STATE_SEND_AVOID, CMD_COMMON_BACK, STATE_PREV, ACTION_RESUME_CALLBACK_DEVIATION},
        
        // any time we get renew dynamic route request, go back to moving map model
        { STATE_ANY, EVENT_MODEL_RENEW_DYNAMIC_ROUTE, STATE_RENEW_DYNAMIC_ROUTE, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_RENEW_STATIC_ROUTE, STATE_RENEW_STATIC_ROUTE, ACTION_NONE },
    };
    
    public TrafficController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_GO_TO_ROUTE_SUMMARY:
            {
                if(superController != null)
                {
                    Parameter param = new Parameter();
                    param.put(KEY_O_CURRENT_CONTROLLER, this);
                    superController.activate(EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, param);
                }
                break;
            }
            case STATE_GO_TO_POI:
            {
                postControllerEvent(EVENT_CONTROLLER_GOTO_POI, null);
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
            case STATE_BACK_TO_SUMMARY_CONTROLLER:
            {
                postControllerEvent(EVENT_CONTROLLER_BACK_TO_SUMMARY_CONTROLLER, null);
                break;
            }
            case STATE_GO_BACK:
            {
                boolean isDynamicRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE;
                if(isDynamicRoute)
                {
                    postControllerEvent(EVENT_CONTROLLER_BACK_TO_MOVING_MAP, null);
                }
                else
                {
                    postControllerEvent(EVENT_CONTROLLER_BACK_TO_TURN_MAP, null);
                }
                break;
            }
            case STATE_RENEW_DYNAMIC_ROUTE:
            {
                postControllerEvent(EVENT_CONTROLLER_RENEW_DYNAMIC_ROUTE, null);
                break;
            }
            case STATE_RENEW_STATIC_ROUTE:
            {
                Parameter para = new Parameter();
                para.put(KEY_I_ROUTE_ID, model.get(KEY_I_NEW_ROUTE_ID));
                postControllerEvent(EVENT_CONTROLLER_RENEW_STATIC_ROUTE, para);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        TrafficModel trafficModel = new TrafficModel();
        return trafficModel;
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        TrafficUiDecorator uiDecorator = new TrafficUiDecorator();
        return new TrafficViewTouch(uiDecorator);
    }

}
