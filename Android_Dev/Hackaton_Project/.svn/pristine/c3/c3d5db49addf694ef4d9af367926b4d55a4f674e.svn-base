/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * UpSellController.java
 *
 */
package com.telenav.module.upsell;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
public class UpSellController extends AbstractCommonController implements IUpSellConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT},
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_UPSELL_CANCEL_SUBSCRIPTION ,STATE_CANCELLING_SUBSCRIPTION, ACTION_CANCEL_SUBSCRIPTION},
        { STATE_CANCELLING_SUBSCRIPTION, EVENT_MODEL_CANCEL_SUBSCRIPTION_SUCCESS ,STATE_CANCEL_SUBSCRIPTION_FINISH, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_CANCEL_EXCEPTION, STATE_CANCEL_EXCEPTION, ACTION_NONE },
        { STATE_CANCEL_EXCEPTION, CMD_COMMON_OK, STATE_CANCEL_SUBSCRIPTION_FINISH, ACTION_NONE },
        { STATE_CANCEL_EXCEPTION, CMD_COMMON_BACK, STATE_CANCEL_SUBSCRIPTION_FINISH, ACTION_NONE },
        
        //block cancel during cancel_subscription
        { STATE_CANCELLING_SUBSCRIPTION, CMD_COMMON_BACK ,STATE_CANCELLING_SUBSCRIPTION, ACTION_NONE}, 
        
        { STATE_INIT, EVENT_MODEL_REQUEST_CARRIER_FINISH, STATE_INIT, ACTION_INIT },       
        { STATE_INIT, EVENT_MODEL_UPSELL_OPTIONS, STATE_UP_SELL, ACTION_NONE },       
        { STATE_UP_SELL, CMD_UPSELL_OPTION_CONFIRM, STATE_UPGRADE, ACTION_NONE },
        { STATE_UP_SELL, CMD_UPSELL_OPTION_SUBMIT, STATE_UPSELL_OPTION_SUBMIT, ACTION_UPSELL_OPTION_SUBMIT },
        //ban cancel during purchase subscription
        { STATE_UPSELL_OPTION_SUBMIT, CMD_COMMON_BACK ,STATE_UPSELL_OPTION_SUBMIT, ACTION_NONE}, 
        
        { STATE_ANY, EVENT_MODEL_EXIT_BROWSER, STATE_VIRGIN, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_NON_SCOUT_UPSELL_PURCHASE_SUCCESS, STATE_NON_SCOUT_UPSELL_PURCHASE_SUCCESS, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_SCOUT_UPSELL_PURCHASE_SUCCESS, STATE_UPSELL_FINISH, ACTION_CHECK_AMP_PROMOTE_TOAST },
        { STATE_NON_SCOUT_UPSELL_PURCHASE_SUCCESS, CMD_NON_SCOUT_UPSELL_SUCCESS, STATE_UPSELL_FINISH, ACTION_NONE },        
        { STATE_UP_SELL, CMD_COMMON_BACK, STATE_UPSELL_FINISH, ACTION_NONE },
        { STATE_UP_SELL, EVENT_MODEL_REGISTRATE_CANCEL, STATE_UP_SELL, ACTION_EXIT_CHECK },
        { STATE_UP_SELL, EVENT_MODEL_GOTO_PREV, STATE_PREV, ACTION_NONE },
        
        { STATE_UPGRADE, CMD_UPGRADE, STATE_UPSELL_OPTION_SUBMIT, ACTION_UPSELL_OPTION_SUBMIT },
        
        { STATE_UP_SELL, CMD_LEARN_MORE, STATE_LEARN_LIST, ACTION_NONE },
        { STATE_LEARN_LIST, CMD_SELECT_LEARN_LIST, STATE_LEARN_GALERY, ACTION_NONE },
        { STATE_LEARN_GALERY, CMD_UPGRADE, STATE_UP_SELL, ACTION_NONE },
        
        { STATE_INIT, EVENT_MODEL_GET_UPSELL_OPTION_ERROR, STATE_GET_UPSELL_OPTION_ERROR, ACTION_NONE }, 
        { STATE_INIT, CMD_COMMON_BACK, STATE_UPSELL_FINISH, ACTION_NONE }, 
        { STATE_GET_UPSELL_OPTION_ERROR, CMD_COMMON_BACK, STATE_UPSELL_FINISH, ACTION_NONE }, 
            
    
    };

    public UpSellController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_UPSELL_FINISH:
            {
                Parameter parameter = new Parameter();
                parameter.put(KEY_B_IS_REGISTRATE_SUCC, this.model.fetch(KEY_B_IS_REGISTRATE_SUCC));
                parameter.put(ICommonConstants.KEY_B_IS_START_BY_OTHER_CONTROLLER, true);
                this.postControllerEvent(EVENT_CONTROLLER_UPSELL_PURCHASE_FINISH, parameter);          
                break;
            }
            case STATE_CANCEL_SUBSCRIPTION_FINISH:
            {
                Parameter parameter = new Parameter();
                parameter.put(ICommonConstants.KEY_B_IS_START_BY_OTHER_CONTROLLER, true);
                parameter.put(ICommonConstants.KEY_B_IS_CANCEL_SUBSCRIPTION_SUCC, model.fetch(KEY_B_IS_CANCEL_SUBSCRIPTION_SUCC));
                
                this.postControllerEvent(EVENT_CONTROLLER_CANCEL_SUBSCRIPTION_SUCCESS, parameter);    
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new UpSellViewTouch(new UpSellUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new UpSellModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

}
