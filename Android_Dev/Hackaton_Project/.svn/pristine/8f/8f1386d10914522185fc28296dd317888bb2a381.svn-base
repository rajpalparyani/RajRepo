/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentController.java
 *
 */
package com.telenav.module.ac.stopsselection;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-26
 */
public class StopsSelectionController extends AbstractCommonController implements IStopsSelectionConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_CHOOSE_ADDRESS, ACTION_NONE },
    
    { STATE_CHOOSE_ADDRESS, CMD_SELECT_ADDRESS, STATE_RETURN_ADDRESS, ACTION_NONE },

    };

    public StopsSelectionController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_VIRGIN:
            {
                if( this.model.getInt(KEY_I_TYPE_ADDRESS_VALIDATOR_FROM) == TYPE_FROM_MAITAI)
                {
                    postControllerEvent(EVENT_CONTROLLER_EXIT_MAITAI,null);
                }
                break;
            }
            
            case STATE_RETURN_ADDRESS:
            {
                Parameter data = new Parameter();
                data.put(KEY_O_SELECTED_ADDRESS, model.get(KEY_O_SELECTED_ADDRESS));
                postControllerEvent(EVENT_CONTROLLER_STOP_SELECTED, data);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new StopsSelectionViewTouch(new StopsSelectionUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new StopsSelectionModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

}
