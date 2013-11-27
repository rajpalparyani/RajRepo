/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DriveWithFriendsController.java
 *
 */
package com.telenav.module.dwf;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author fangquanm
 * @date Jul 1, 2013
 */
public class DriveWithFriendsController extends AbstractCommonController implements IDriveWithFriendsConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
        { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_NEW_DWF, ACTION_NONE }, 
        { STATE_ANY, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_NEW_DWF, ACTION_SELECT_ADDRESS },
        { STATE_ANY, CMD_SELECT_FROM_CONTACTS, STATE_SELECT_FROM_CONTACT, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_CONTACT_SELECTED, STATE_NEW_DWF, ACTION_NONE },
        { STATE_ANY, CMD_INVITE, STATE_REQUEST_SESSION_LIST, ACTION_REQUEST_SESSION },
        { STATE_ANY, EVENT_MODEL_LAUNCH_SESSION_LIST, STATE_SESSION_LIST, ACTION_LAUNCH_SESSION_LIST },
        
        { STATE_SESSION_LIST, EVENT_MODEL_UPDATE_SESSION_LIST, STATE_SESSION_LIST, ACTION_NONE },
        { STATE_SESSION_LIST, EVENT_CHECK_EXPIRATION, STATE_CHECK_EXPIRATION, ACTION_NONE },
        { STATE_SESSION_LIST, CMD_DROP_MAP, STATE_SESSION_LIST, ACTION_NONE },
        { STATE_SESSION_LIST, CMD_INVITE_NEW, STATE_NEW_DWF, ACTION_INVITE_NEW },
        { STATE_SESSION_LIST, CMD_LEAVE_GROUP, STATE_LEAVE_GROUP, ACTION_LEAVE_GROUP },
        { STATE_SESSION_LIST, CMD_COMMON_CANCEL, STATE_VIRGIN, ACTION_NONE},
        
        { STATE_ANY, EVENT_MODEL_LAUNCH_ROUTE_PLAN, STATE_LAUNCH_ROUTE_PLAN, ACTION_NONE },
        { STATE_ANY, CMD_DRIVE, STATE_LAUNCH_ROUTE_PLAN, ACTION_NONE},
        
        { STATE_ANY, EVENT_CONTROLLER_FROM_PLUGIN_REQUEST, STATE_SESSION_LIST, ACTION_FROM_PLUGIN},
        
        { STATE_SESSION_LIST, CMD_BACK_TO_MOVING_MAP, STATE_BACK_TO_MOVING_MAP, ACTION_NONE},
        { STATE_SESSION_LIST, CMD_COMMON_BACK, STATE_VIRGIN, ACTION_NONE},
        { STATE_SESSION_LIST, CMD_START_MAIN, STATE_START_MAIN, ACTION_NONE},
        
        { STATE_CHECK_EXPIRATION, CMD_OK_EXPIRATION, STATE_VIRGIN, ACTION_LEAVE_GROUP},
    };

    public DriveWithFriendsController(AbstractController superController)
    {
        super(superController);
    }

    @Override
    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_VIRGIN:
            {
                boolean launchApp = this.model.getBool(KEY_B_DWF_LAUNCH_APP);
                if(launchApp && !TeleNavDelegate.getInstance().isActivate())
                {
                    releaseAll();
                    ModuleFactory.getInstance().startMain();
                }
                break;
            }
            case STATE_SELECT_FROM_CONTACT:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startContactsController(this, TYPE_AC_FROM_SHARE, userProfileProvider);
                break;
            }
            case STATE_LAUNCH_ROUTE_PLAN:
            {
                Address dest = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
                
                ModuleFactory.getInstance().startNavController(this, null, dest, null, false, null, false, false, false, true);
                break;
            }
            case STATE_LEAVE_GROUP:
            {
                boolean launchApp = this.model.getBool(KEY_B_DWF_LAUNCH_APP);
                if(launchApp)
                {
                    releaseAll();
                    ModuleFactory.getInstance().startMain();
                }
                else
                {
                    if (NavSdkNavEngine.getInstance().isRunning())
                    {
                        this.postControllerEvent(EVENT_CONTROLLER_BACK_TO_MOVING_MAP, null);
                    }
                    else
                    {
                        releaseAll();
                        ModuleFactory.getInstance().startMain();
                    }
                }
                
                break;
            }
            case STATE_BACK_TO_MOVING_MAP:
            {
                Parameter para = new Parameter();
                para.put(KEY_B_IS_FROM_DWF, true);
                this.postControllerEvent(EVENT_CONTROLLER_BACK_TO_MOVING_MAP, para);
                break;
            }
            case STATE_START_MAIN:
            {
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                releaseAll();
                ModuleFactory.getInstance().startMain();
                break;
            }
        }
    }

    @Override
    protected AbstractView createView()
    {
        return new DriveWithFriendsViewTouch(new DriveWithFriendsUiDecorator(this.model));
    }

    @Override
    protected AbstractModel createModel()
    {
        return new DriveWithFriendsModel();
    }

    @Override
    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

}
