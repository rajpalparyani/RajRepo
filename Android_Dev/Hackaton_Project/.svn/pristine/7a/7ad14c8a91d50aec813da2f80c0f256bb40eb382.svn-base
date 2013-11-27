/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteSettingController.java
 *
 */
package com.telenav.module.preference.routesetting;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author yning
 *@date 2011-3-3
 */
public class RouteSettingController extends AbstractCommonController implements IRouteSettingConstants
{

    private final static int[][] STATE_TABLE = new int[][]{
        { STATE_VIRGIN,                  EVENT_CONTROLLER_GO_TO_ROUTE_SETTING,   STATE_ROUTE_SETTING,          ACTION_NONE },
        { STATE_VIRGIN,                  EVENT_CONTROLLER_GO_TO_VOICE_SETTING,   STATE_VOICE_SETTING,          ACTION_NONE },
        { STATE_ROUTE_SETTING,           CMD_SAVE,                               STATE_RETURN_VALUE,           ACTION_SAVE_PREFERENCE },
        // { STATE_ROUTE_SETTING,           CMD_COMMON_BACK,                        STATE_RETURN_VALUE,           ACTION_NONE },
        { STATE_VOICE_SETTING,           CMD_SAVE,                               STATE_RETURN_VALUE,           ACTION_SAVE_PREFERENCE },
        // { STATE_VOICE_SETTING,           CMD_COMMON_BACK,                        STATE_RETURN_VALUE,           ACTION_NONE },
   };
    
    public RouteSettingController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_RETURN_VALUE:
            {
                Parameter parameter =new Parameter();
                parameter.put(ICommonConstants.KEY_B_ROUTE_SETTING_CHANGED, this.model.get(KEY_B_ROUTE_SETTING_CHANGED));
                postControllerEvent(EVENT_CONTROLLER_PREFERENCE_SAVED, parameter);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        RouteSettingModel routeSettingModel = new RouteSettingModel();
        return routeSettingModel;
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(ICommonConstants.STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        RouteSettingUiDecorator uiDecorator = new RouteSettingUiDecorator(model.getBool(KEY_B_IS_NAV_SESSION));
        return new RouteSettingViewTouch(uiDecorator);
    }

}
