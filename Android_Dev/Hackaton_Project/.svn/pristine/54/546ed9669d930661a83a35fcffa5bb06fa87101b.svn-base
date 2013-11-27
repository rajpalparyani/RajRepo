/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StartUpController.java
 *
 */
package com.telenav.module.sync;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author bduan
 *@date 2010-12-2
 */
public class SyncResController extends AbstractCommonController implements ISyncResConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_CHECK_TYPE }, 
        
        { STATE_INIT, EVENT_MODEL_DO_FRESH_SYNC, STATE_FRESH_SYNC, ACTION_CHECK_REGION_DETECT_STATUS }, 
        { STATE_INIT, EVENT_MODEL_DO_STARTUP_SYNC, STATE_STARTUP_SYNC, ACTION_STARTUP_SYNC }, 
        { STATE_INIT, EVENT_MODEL_DO_EXIT_SYNC, STATE_EXIT_SYNC, ACTION_EXIT_SYNC }, 
        
        { STATE_FRESH_SYNC, EVENT_MODEL_DETECTING_REGION, STATE_FRESH_SYNC, ACTION_NONE }, 
        { STATE_FRESH_SYNC, EVENT_MODEL_DO_FRESH_SYNC, STATE_FRESH_SYNC, ACTION_FRESH_SYNC }, 
        
        //handle error for fresh login.
        { STATE_COMMON_ERROR, CMD_COMMON_OK, STATE_PREV, ACTION_HANDLE_ERROR},
        { STATE_COMMON_ERROR, CMD_COMMON_BACK, STATE_PREV, ACTION_HANDLE_ERROR},
        { STATE_COMMON_ERROR, CMD_COMMON_CANCEL, STATE_PREV, ACTION_HANDLE_ERROR},
        
        { STATE_ANY, EVENT_MODEL_SYNC_FINISH, STATE_SCOUT_GO, ACTION_CLOSE_MENU },
        { STATE_SCOUT_GO, CMD_SCOUT_GO, STATE_BACK_TO_MAIN, ACTION_CLOSE_MENU }, 
        { STATE_ANY, CMD_COMMON_BACK, STATE_ANY, ACTION_NONE },    
    };
    
    
    public SyncResController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_BACK_TO_MAIN:
            {
                Parameter param = new Parameter();
                param.put(KEY_B_IS_NEED_PAUSE_LATER, true);
                this.postControllerEvent(EVENT_CONTROLLER_SYNC_FINISH, param);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new SyncResModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new SyncResViewTouch(new SyncResUiDecorator());
    }

}
