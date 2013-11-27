/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TestProxyController.java
 *
 */
package com.telenav.module.testproxy;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 *@author yning
 *@date 2012-11-21
 */
public class TestProxyController extends AbstractCommonController implements ITestProxyConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_MAIN, ACTION_NONE },
    { STATE_MAIN, CMD_LOGIN, STATE_REQUESTING, ACTION_DO_LOGIN },
    { STATE_MAIN, CMD_SYNC_PURCHASE, STATE_REQUESTING, ACTION_DO_SYNC_PURCHASED },
    { STATE_MAIN, CMD_SYNC_PREFERENCE, STATE_REQUESTING, ACTION_DO_SYNC_PREFERENCE },
    { STATE_MAIN, CMD_SYNC_SETTING_DATA, STATE_REQUESTING, ACTION_DO_SYNC_SETTING_DATA },
    { STATE_REQUESTING, EVENT_MODEL_SHOW_RESULT, STATE_RESULT, ACTION_NONE },
    { STATE_MAIN, CMD_SHOW_UPSELL_MAIN, STATE_UPSELL_MAIN, ACTION_NONE },
    { STATE_UPSELL_MAIN, CMD_UPSELL_SUBMIT, STATE_REQUESTING, ACTION_DO_GET_UPSELL_INFO },
    { STATE_REQUESTING, EVENT_MODEL_SHOW_UPSELL_OPTIONS, STATE_UPSELL_SHOW_OPTIONS, ACTION_NONE },
    { STATE_UPSELL_SHOW_OPTIONS, CMD_UPSELL_PURCHASE, STATE_REQUESTING, ACTION_DO_PURCHASE },
    

    };

    public TestProxyController(AbstractController superController)
    {
        super(superController);
    }

    @Override
    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected AbstractView createView()
    {
        // TODO Auto-generated method stub
        return new TestProxyViewTouch(new TestProxyUiDecorator());
    }

    @Override
    protected AbstractModel createModel()
    {
        // TODO Auto-generated method stub
        return new TestProxyModel();
    }

    @Override
    protected StateMachine createStateMachine()
    {
        // TODO Auto-generated method stub
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

}
