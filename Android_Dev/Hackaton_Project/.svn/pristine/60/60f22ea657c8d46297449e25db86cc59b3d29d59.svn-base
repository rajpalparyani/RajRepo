/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LockoutController.java
 *
 */
package com.telenav.module.carconnect.lockout;

import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 *@author chihlh
 *@date Mar 15, 2012
 */
public class LockoutController extends AbstractCommonController implements ILockoutConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
            { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
            { STATE_INIT, EVENT_MODEL_LOCK_SCREEN, STATE_SCREEN_LOCKED, ACTION_NONE },
            { STATE_ANY, EVENT_MODEL_UNLOCK_SCREEN, STATE_SCREEN_UNLOCKED, ACTION_NONE }
    };
    
    public LockoutController(AbstractController superController)
    {
        super(superController);
    }

    @Override
    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_SCREEN_UNLOCKED:
            {
                release();
                if (superController != null)
                {
                    superController.activate();
                }
                else
                {
                    ModuleFactory.getInstance().startMain();
                }
                break;
            }
        }
    }

    @Override
    protected AbstractView createView()
    {
        return new LockoutViewTouch(new LockoutUiDecorator());
    }

    @Override
    protected AbstractModel createModel()
    {
        return new LockoutModel();
    }

    @Override
    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

}
