/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DashboardController.java
 *
 */
package com.telenav.module.drivingshare;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 *@author qli
 *@date 2012-2-3
 */
public class DrivingShareController extends AbstractCommonController implements IDrivingShareConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN,             EVENT_CONTROLLER_START,             STATE_INIT,                 ACTION_INIT },
        { STATE_INIT,               EVENT_MODEL_LAUNCH_MAIN,            STATE_MAIN,                 ACTION_NONE},
        { STATE_MAIN,               CMD_COMMON_BACK,                    STATE_PREV,                 ACTION_SAVE },
    };

    public DrivingShareController(AbstractController superController)
    {
        super(superController);
    }

    protected AbstractView createView()
    {
        return new DrivingShareViewTouch(new DrivingShareUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new DrivingShareModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
        }
    }
    
}
