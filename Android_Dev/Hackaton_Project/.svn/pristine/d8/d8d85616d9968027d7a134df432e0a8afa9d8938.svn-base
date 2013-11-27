/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentController.java
 *
 */
package com.telenav.module.ac.recent;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-27
 */
public class RecentController extends AbstractCommonController implements IRecentConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
            { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
        
            { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_BACKGROUND_SYNC },

            // search address by typing address
            { STATE_MAIN, CMD_SEARCH_ADDRESS, STATE_MAIN, ACTION_SEARCH_RECENT },

            // select address in list and return the address to super
            { STATE_MAIN, CMD_SELECT_ADDRESS, STATE_ADDRESS_SELECTED, ACTION_NONE },

            // sync the recent addresses with server
            { STATE_MAIN, CMD_SYNC_RECENT, STATE_MAIN, ACTION_SYN_RECENT },

            // user click the delete_all button, and a confirmation popup will be shown
            { STATE_MAIN, CMD_DELETE_RECENT, STATE_DELETE_CHECK, ACTION_DELETE_CHECK },

            { STATE_DELETE_CHECK, EVENT_MODEL_DELETE_SINGLE_ADDRESS, STATE_DELETE_RECENT, ACTION_NONE },

            { STATE_DELETE_CHECK, EVENT_MODEL_DELETE_ALL_ADDRESS, STATE_DELETE_ALL_CONFIRM, ACTION_NONE },

            // after the sync finishes, update the main screen
            { STATE_MAIN, EVENT_MODEL_SYNC_FINISHED, STATE_MAIN, ACTION_NONE },

            // user enter map to address by context menu
            { STATE_MAIN, CMD_MAP_ADDRESS, STATE_GO_TO_MAP, ACTION_NONE },

            // user enter drive to address by context menu
            { STATE_MAIN, CMD_DRIVETO_ADDRESS, STATE_GO_TO_NAV, ACTION_NONE },

            // user enter save favorite by context menu
            { STATE_MAIN, CMD_COMMON_ADD_TO_FAVORITES, STATE_SAVE_RECENT, ACTION_NONE },

            // user enter delete address by context menu
            { STATE_MAIN, CMD_COMMON_DELETE, STATE_DELETE_RECENT, ACTION_NONE },

            // user enter call poi by context menu
            { STATE_MAIN, CMD_COMMON_CALL, STATE_MAIN, ACTION_CALL },

            // user cancel the delete action
            { STATE_DELETE_RECENT, CMD_COMMON_CANCEL, STATE_PREV, ACTION_NONE },

            // delete the recent place
            { STATE_DELETE_RECENT, CMD_COMMON_OK, STATE_PREV, ACTION_DELETE_RECENT },

            { STATE_COMMON_ERROR, CMD_TIME_OUT, STATE_MAIN, ACTION_NONE },

            // User confirms the delete action
            { STATE_DELETE_ALL_CONFIRM, CMD_COMMON_OK, STATE_PREV, ACTION_DELETE_ALL_RECENT },

            // User cancel the delete action
            { STATE_DELETE_ALL_CONFIRM, CMD_COMMON_CANCEL, STATE_PREV, ACTION_NONE },

            // Save the recent to favorite
            { STATE_SAVE_RECENT, EVENT_CONTROLLER_FAVORITE_EDITED, STATE_PREV, ACTION_NONE },

            // back from Login
            { STATE_ANY, EVENT_CONTROLLER_BACK_TO_PREV, STATE_PREV, ACTION_CHECK_MIGRATION },
            
            // back from share
            { STATE_ANY, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
            { STATE_ANY, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },

    };

    public RecentController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_ADDRESS_SELECTED:
            {
                if (model.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_ONE_BOX)
                {
                    Parameter data = new Parameter();
                    data.put(KEY_O_SELECTED_ADDRESS, model.fetch(KEY_O_SELECTED_ADDRESS));
                    postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                }
                else
                {
                    PoiDataWrapper poiDataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
                    poiDataWrapper.addStopAddress((Address) model.get(KEY_O_SELECTED_ADDRESS));
                    poiDataWrapper.addNormalAddr((Address) model.get(KEY_O_SELECTED_ADDRESS));
                    ModuleFactory.getInstance().startDetailController(this, TYPE_DETAIL_FROM_RECENT, model.getInt(KEY_I_AC_TYPE), 0, poiDataWrapper,
                        (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER), model.getBool(KEY_B_IS_SEARCH_NEAR_BY));
                }
                break;
            }
            case STATE_GO_TO_MAP:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_RECENT,
                    (Address) model.get(KEY_O_SELECTED_ADDRESS), null, userProfileProvider);
                break;
            }
            case STATE_GO_TO_NAV:
            {
                ModuleFactory.getInstance().startNavController(this, null, (Address) model.get(KEY_O_SELECTED_ADDRESS),
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), false, false);
                break;
            }
            case STATE_SAVE_RECENT:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAddPlaceController(this, ICommonConstants.PLACE_OPERATION_TYPE_ADD,
                    (Address) model.get(KEY_O_SELECTED_ADDRESS), null,  null, null, userProfileProvider);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new RecentViewTouch(new RecentUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new RecentModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

}
