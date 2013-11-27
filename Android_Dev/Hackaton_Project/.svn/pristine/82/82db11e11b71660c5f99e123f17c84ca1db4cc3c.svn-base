/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NativeShareManager.java
 *
 */
package com.telenav.module.nativeshare;

import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author pwang
 * @date 2013-2-28
 */
public class NativeShareController extends AbstractController implements INativeShareConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
    { STATE_ANY, EVENT_MODEL_NATIVE_SHARE_REQUEST_TINY_URL, STATE_NATIVE_SHARE_REQUEST_TINY_URL, ACTION_NATIVE_SHARE_REQUEST_TINY_URL },
    { STATE_ANY, EVENT_MODEL_START_NATIVE_SHARE, STATE_NATIVE_SHARE, ACTION_NATIVE_SHARE },
    { STATE_NATIVE_SHARE, EVENT_MODEL_NATIVE_SHARE_FINISH, STATE_NATIVE_SHARE_FINISH, ACTION_NONE },
    { STATE_NATIVE_SHARE, EVENT_MODEL_NATIVE_SHARE_CANCEL, STATE_NATIVE_SHARE_CANCEL, ACTION_NONE },
    };

    public NativeShareController(AbstractController superController)
    {
        super(superController);
    }

    @Override
    protected AbstractView createView()
    {
        return new NativeShareViewTouch(new NativeShareUiDecorator());
    }

    @Override
    protected AbstractModel createModel()
    {
        return new NativeShareModel();
    }

    @Override
    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    @Override
    protected void postStateChange(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_NATIVE_SHARE_FINISH:
            {
                Parameter data = new Parameter();
                data.put(KEY_S_COMMON_MESSAGE, this.model.get(KEY_S_COMMON_MESSAGE));
                postControllerEvent(EVENT_CONTROLLER_NATIVE_SHARE_FINISH, data);
                break;
            }
            case STATE_NATIVE_SHARE_CANCEL:
            {
                Parameter data = new Parameter();
                data.put(KEY_S_COMMON_MESSAGE, this.model.get(KEY_S_COMMON_MESSAGE));
                postControllerEvent(EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, data);
                break;
            }
        }
    }

}
