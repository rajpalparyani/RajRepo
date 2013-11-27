/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ShareController.java
 *
 */
package com.telenav.module.feedback;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
public class FeedbackController extends AbstractCommonController implements IFeedbackConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
    
        { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE },
    
        { STATE_MAIN, CMD_SUMBIT, STATE_SUBMIT_FEEDBACK, ACTION_SUBMIT },
        
        { STATE_MAIN, CMD_COMMON_BACK, STATE_PREV, ACTION_NONE },
    
        { STATE_SUBMIT_FEEDBACK, CMD_COMMON_OK, STATE_VIRGIN, ACTION_NONE }
    };

    public FeedbackController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {

    }

    protected AbstractView createView()
    {
        return new FeedbackViewTouch(new FeedbackUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new FeedbackModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

}
