/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MaiTaiController.java
 *
 */
package com.telenav.sdk.maitai.view;


import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 *@author gbwang
 *@date 2011-8-5
 */
public class MaiTaiViewController extends BrowserSdkController implements IMaiTaiViewConstants
{
    
    protected final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN,         EVENT_CONTROLLER_START,             STATE_INIT,                 IMaiTaiViewConstants.ACTION_INIT },
        { STATE_INIT,           EVENT_MODEL_LAUCH_MAITAI_VIEW,      STATE_MAITAI_VIEW,          ACTION_NONE },
        { STATE_ANY,            EVENT_MODEL_GOTO_NAV,               STATE_BROWSER_GOTO_NAV,     ACTION_NONE },
        { STATE_ANY,            CMD_COMMON_BACK,                    STATE_PREV,                 ACTION_NONE },
    };

    public MaiTaiViewController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        super.postStateChangeDelegate(currentState, nextState);
    }

    protected AbstractModel createModel()
    {
        return new MaiTaiViewModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new MaiTaiViewViewTouch(new MaiTaiViewUiDecorator());
    }

}
