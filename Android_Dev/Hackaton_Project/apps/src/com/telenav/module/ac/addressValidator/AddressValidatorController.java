/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentController.java
 *
 */
package com.telenav.module.ac.addressValidator;

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
 * @date 2010-12-26
 */
public class AddressValidatorController extends AbstractCommonController implements IAddressValidatorConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_MAIN, ACTION_VALIDATE_HOME },
    
    { STATE_MAIN, EVENT_MODEL_CHOOSE_ADDRESS, STATE_CHOOSE_ADDRESS, ACTION_NONE},
    
    { STATE_MAIN, CMD_COMMON_BACK, STATE_PREV, ACTION_CANCEL_VALIDATING},
    
    { STATE_MAIN, EVENT_MODEL_RETURN_ADDRESS, STATE_RETURN_ADDRESS, ACTION_NONE},

    { STATE_CHOOSE_ADDRESS, EVENT_CONTROLLER_STOP_SELECTED, STATE_RETURN_ADDRESS, ACTION_NONE },

    { STATE_COMMON_TIMEOUT_MESSAGEBOX, CMD_COMMON_OK, STATE_VIRGIN, ACTION_NONE },

    };

    public AddressValidatorController(AbstractController superController)
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
                data.put(KEY_O_VALIDATED_ADDRESS, (Address) model.fetch(KEY_O_SELECTED_ADDRESS));
                postControllerEvent(EVENT_CONTROLLER_ADDRESS_VALIDATED, data);
                break;
            }
            case STATE_CHOOSE_ADDRESS:
            {
                String transactionId = this.model.getString(KEY_S_TRANSACTION_ID);
                if(transactionId == null)
                {
                    transactionId = "";
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startStopsSecelctionController(this, model.getVector(KEY_V_ALTERNATIVE_ADDRESSES), model.getInt(KEY_I_TYPE_ADDRESS_VALIDATOR_FROM), transactionId, userProfileProvider);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new AddressValidatorViewTouch(new AddressValidatorUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new AddressValidatorModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

}
