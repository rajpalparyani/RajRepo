/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteController.java
 *
 */
package com.telenav.module.ac.airport;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
public class AirportController extends AbstractCommonController implements IAirportConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {

    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },

    { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE },

    { STATE_MAIN, CMD_SUBMIT, STATE_VALIDATE_AIRPORT, ACTION_VALIDATE_AIRPORT },

    { STATE_MAIN, CMD_COMMON_BACK, STATE_MAIN, ACTION_HIDE_DROPDOWN },
    
    { STATE_MAIN, EVENT_MODEL_HIDE_DROPDOWN, STATE_PREV, ACTION_NONE},
    
    { STATE_MAIN, CMD_SEARCH_AIRPORT, STATE_MAIN, ACTION_SEARCH_AIRPORT},
    
    { STATE_MAIN, CMD_SELECT_AIRPORT, STATE_RETURN_AIRPORT, ACTION_NONE},
    
    { STATE_VALIDATE_AIRPORT, EVENT_MODEL_RETURN_AIRPORT, STATE_RETURN_AIRPORT, ACTION_NONE },

    { STATE_VALIDATE_AIRPORT, EVENT_MODEL_CHOOSE_AIRPORT, STATE_CHOOSE_AIRPORT, ACTION_NONE },
    
    { STATE_VALIDATE_AIRPORT, CMD_COMMON_BACK, STATE_PREV, ACTION_CANCEL_VALIDATING_AIRPORT },

    { STATE_CHOOSE_AIRPORT, EVENT_CONTROLLER_STOP_SELECTED, STATE_RETURN_AIRPORT, ACTION_NONE },

    };

    public AirportController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_RETURN_AIRPORT:
            {
                Parameter data = new Parameter();
                Address airportAddress = (Address) model.fetch(KEY_O_SELECTED_ADDRESS);
                airportAddress.setSource(Address.SOURCE_TYPE_IN);
                data.put(KEY_O_SELECTED_ADDRESS, airportAddress);
                postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                break;
            }
            case STATE_CHOOSE_AIRPORT:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startStopsSecelctionController(this, model.getVector(KEY_V_ALTERNATIVE_ADDRESSES), -1, "", userProfileProvider);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new AirportViewTouch(new AirportUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new AirportModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

}
