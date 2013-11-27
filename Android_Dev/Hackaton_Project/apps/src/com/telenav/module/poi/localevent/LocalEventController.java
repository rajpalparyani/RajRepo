/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * LocalEventController.java
 *
 */
package com.telenav.module.poi.localevent;

import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 *@author yning
 *@date 2013-5-6
 */
public class LocalEventController extends BrowserSdkController implements ILocalEventConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_BROWSER_INIT, ACTION_INIT }, 
        { STATE_BROWSER_INIT, EVENT_MODEL_LAUNCH_BROWSER, STATE_LOCAL_EVENT_MAIN, ACTION_NONE },
        { STATE_LOCAL_EVENT_MAIN, EVENT_MODEL_GOTO_NAV, STATE_BROWSER_GOTO_NAV, ACTION_NONE },
        { STATE_ANY,              EVENT_MODEL_GOTO_SHARE_ADDR,        STATE_BROWSER_GOTO_SHARE_ADDR,      ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
    };
    
    public LocalEventController(AbstractController superController)
    {
        super(superController);
    }

    @Override
    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
    
    @Override
    protected AbstractModel createModel()
    {
        return new LocalEventModel();
    }
    
    @Override
    protected AbstractView createView()
    {
        return new LocalEventViewTouch(new LocalEventUiDecorator());
    }
}
