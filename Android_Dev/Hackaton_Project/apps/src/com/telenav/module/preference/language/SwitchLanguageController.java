/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SwitchLangController.java
 *
 */
package com.telenav.module.preference.language;

import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 *@author qli
 *@modifier wzhu
 *@date 2011-3-9
 */
public class SwitchLanguageController extends AbstractCommonController implements ISwitchLanguageConstants
{
    private final static int[][] STATE_TABLE = new int[][]{
        {STATE_VIRGIN,                   EVENT_CONTROLLER_START,               STATE_SWITCHLANG_SYNC,           ACTION_SWITCHLANG_SYNC },
        {STATE_ANY,        EVENT_MODEL_SWITCHLANG_FINISHED,        STATE_SWITCHLANG_FINISHED,       ACTION_NONE},
        {STATE_SWITCHLANG_SYNC,        CMD_COMMON_CANCEL,                      STATE_SWITCHLANG_FINISHED,           ACTION_SWITCHLANG_CANCEL},
        {STATE_SWITCHLANG_SYNC,        CMD_COMMON_BACK,                        STATE_SWITCHLANG_FINISHED,           ACTION_SWITCHLANG_CANCEL},
        { STATE_COMMON_TIMEOUT_MESSAGEBOX, CMD_COMMON_OK,                      STATE_CHECK_NAV_IS_RUNNING,       ACTION_CHECK_NAV_IS_RUNNING },
        { STATE_CHECK_NAV_IS_RUNNING, EVENT_MODEL_NAV_IS_RUNNING,                      STATE_GO_TO_NAV,       ACTION_NONE },
        { STATE_CHECK_NAV_IS_RUNNING, EVENT_MODEL_NAV_IS_NOT_RUNNING,                      STATE_SWITCHLANG_FINISHED,       ACTION_NONE },
        {STATE_ANY,        EVENT_MODEL_BACK_TO_MOVING_MAP,        STATE_ANY,       ACTION_NONE},
    };

    public SwitchLanguageController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_SWITCHLANG_FINISHED:
            {
                releaseAll();
                ModuleFactory.getInstance().startMain();
                break;
            }
            case STATE_GO_TO_NAV:
            {
            	postControllerEvent(EVENT_CONTROLLER_GO_TO_NAV, null);
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new SwitchLanguageModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new SwitchLanguageViewTouch(new SwitchLanguageUiDecorator());
    }

}
