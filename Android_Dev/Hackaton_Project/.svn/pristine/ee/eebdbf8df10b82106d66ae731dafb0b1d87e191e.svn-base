/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentController.java
 *
 */
package com.telenav.module.ac.home;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.module.dashboard.DashboardController;
import com.telenav.module.oneboxsearch.OneBoxSearchController;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.maitai.module.MaiTaiController;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-3
 */
public class HomeController extends AbstractCommonController implements IHomeConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
    
        { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE },
        
        { STATE_MAIN, CMD_SELECT_ADDRESS, STATE_CHECK_ADDRESS_TYPE, ACTION_CHECK_ADDRESS_TYPE },
        
        { STATE_MAIN, CMD_SELECT_CITY, STATE_CHECK_CITY, ACTION_CHECK_CITY },
        
        { STATE_MAIN, CMD_SAVE, STATE_VALIDATE_INPUT, ACTION_VALIDATE_INPUT },
        
        { STATE_CHECK_CITY, EVENT_MODEL_RETURN_CITY_ADDRESS, STATE_CHECK_ADDRESS_TYPE, ACTION_CHECK_ADDRESS_TYPE },
        
        { STATE_CHECK_CITY, EVENT_MODEL_GO_TO_VALIDATE_ADDRESS, STATE_VALIDATE_INPUT, ACTION_VALIDATE_INPUT },
        
        { STATE_VALIDATE_INPUT, EVENT_MODEL_VALIDATE_LIST_HOME, STATE_CHECK_ADDRESS_TYPE, ACTION_CHECK_ADDRESS_TYPE },
    
        { STATE_VALIDATE_INPUT,  EVENT_MODEL_VALIDATE_HOME, STATE_VALIDATE_HOME, ACTION_NONE },
       
        { STATE_VALIDATE_HOME, EVENT_CONTROLLER_ADDRESS_VALIDATED, STATE_CHECK_ADDRESS_TYPE, ACTION_CHECK_ADDRESS_TYPE},
        
        { STATE_CHECK_ADDRESS_TYPE, EVENT_MODEL_SAVE_HOME, STATE_SAVE_ADDRESS, ACTION_SAVE_HOME},
        
        { STATE_CHECK_ADDRESS_TYPE, EVENT_MODEL_SAVE_WORK, STATE_SAVE_ADDRESS, ACTION_SAVE_WORK},
        
        { STATE_CHECK_ADDRESS_TYPE, EVENT_RETURN_ADDRESS, STATE_ADDRESS_SELECTED, ACTION_NONE},
        
        { STATE_COMMON_TIMEOUT_MESSAGEBOX, CMD_COMMON_OK, STATE_RETURN_TO_OTHERS, ACTION_NONE },
    };

    public HomeController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_VALIDATE_HOME:
            {
              IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
              ModuleFactory.getInstance().startAddressValidatorController(this, model.getString(KEY_S_ADDRESS_LINE),  model.getString(KEY_S_CITY), model.getString(KEY_S_MESSAGE_ADDRESS), userProfileProvider, null);
                break;
            }
            case STATE_ADDRESS_SELECTED:
            {
                getAddressFromAC();
                break;
            }
            case STATE_RETURN_TO_OTHERS:
            {
                AbstractController controller = this.getSuperController();
                if (controller instanceof DashboardController)
                {
                    postControllerEvent(EVENT_CONTROLLER_BACK_TO_DASHBOARD, null);
                }
                else if (controller instanceof OneBoxSearchController)
                {
                    postControllerEvent(EVENT_CONTROLLER_BACK_TO_ONEBOX_SEARCH, null);
                }
                else if(controller instanceof MaiTaiController)
                {
                    MaiTaiManager.getInstance().startMaiTai();
                }
                else
                {
                    getAddressFromAC();
                }
                break;
            }
        }
    }

    private void getAddressFromAC()
    {
    	Address address = (Address) model.fetch(KEY_O_VALIDATED_ADDRESS);
    	address.setSource(Address.SOURCE_PREDEFINED);
        Parameter data = new Parameter();
        data.put(KEY_O_SELECTED_ADDRESS, address);
        postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
    }

    private String composeOneLineString(String firstLine, String secondLine)
    {
        if(firstLine == null)
        {
            firstLine = "";
        }
        
        if(secondLine == null)
        {
            secondLine = "";
        }
        
        String oneLineString = "<HOUSE_NUMBER,STREET>=" + firstLine.trim() + ";<CITY,STATE,POSTAL_CODE>=" + secondLine.trim();
        
        return oneLineString;
    }

    protected AbstractView createView()
    {
        return new HomeViewTouch(new HomeUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new HomeModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

}
