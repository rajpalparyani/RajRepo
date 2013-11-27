/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FavoriteController.java
 *
 */
package com.telenav.module.ac.favorite;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
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
 * @date 2010-8-26
 */
public class FavoriteController extends AbstractCommonController implements IFavoriteConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {

    { STATE_ANY, CMD_SORT, STATE_SORT_SELECT, ACTION_NONE },

    { STATE_SORT_SELECT, CMD_SORT_BY_ALPHABET, STATE_PREV, ACTION_SORT_BY_ALPHABET },

    { STATE_SORT_SELECT, CMD_SORT_BY_DATE, STATE_PREV, ACTION_SORT_BY_DATE },

    { STATE_SORT_SELECT, CMD_SORT_BY_DISTANCE, STATE_PREV, ACTION_SORT_BY_DISTANCE },
	
    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },

    { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_BACKGROUND_SYNC },

    { STATE_SUBCATEGORY, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE },

    { STATE_SUBCATEGORY_EDIT_MODE, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE },

    { STATE_MAIN, CMD_NEXT_CATEGORY, STATE_SUBCATEGORY, ACTION_NEXT_CATEGORY },

    { STATE_MAIN, CMD_SEARCH_FAVORITE, STATE_MAIN, ACTION_SEARCH_FAVORITE },

    { STATE_MAIN, CMD_SYNC, STATE_MAIN, ACTION_SYN_FAVORITE },

    { STATE_MAIN, EVENT_MODEL_SYNC_FINISHED, STATE_MAIN, ACTION_NONE },

    { STATE_ANY, CMD_ADD_TO_FOLDER, STATE_ADD_TO_FOLDER, ACTION_NONE },
    
    { STATE_ANY, CMD_COMMON_EDIT, STATE_EDIT_FAVORITE, ACTION_NONE },

    { STATE_MAIN, CMD_COMMON_DELETE, STATE_DELETE_FAVORITE, ACTION_NONE },

    { STATE_MAIN, CMD_DELETE_CATEGORY, STATE_DELETE_CATEGORY_BY_MENU, ACTION_NONE },

    { STATE_MAIN, CMD_RENAME_CATEGORY, STATE_RENAME_CATEGORY_BY_MENU, ACTION_NONE },

    { STATE_MAIN, CMD_ENTER_EDIT_MODE, STATE_MAIN_EDIT_MODE, ACTION_NONE },

    { STATE_MAIN, CMD_DRIVETO_ADDRESS, STATE_GOTO_NAV, ACTION_NONE },

    { STATE_MAIN, CMD_MAP_ADDRESS, STATE_GOTO_MAP, ACTION_NONE },

    { STATE_MAIN, CMD_COMMON_CALL, STATE_MAIN, ACTION_CALL },

    { STATE_MAIN_EDIT_MODE, CMD_EDIT_CATEGORY, STATE_EDIT_CATEGORY, ACTION_NONE },

    { STATE_MAIN_EDIT_MODE, CMD_NEW_CATEGORY, STATE_ADD_CATEGORY, ACTION_NONE },

    { STATE_MAIN_EDIT_MODE, CMD_SEARCH_FAVORITE, STATE_MAIN_EDIT_MODE, ACTION_SEARCH_FAVORITE },
    
    { STATE_MAIN_EDIT_MODE, CMD_COMMON_DELETE, STATE_DELETE_FAVORITE, ACTION_NONE },
    
    { STATE_MAIN_EDIT_MODE, CMD_DELETE_CATEGORY, STATE_DELETE_CATEGORY_BY_MENU, ACTION_NONE },
    
    { STATE_SUBCATEGORY, CMD_COMMON_BACK, STATE_PREV, ACTION_BACK_FROM_SUBCATEGORY},
    
    { STATE_SUBCATEGORY, CMD_ENTER_EDIT_MODE, STATE_MAIN_EDIT_MODE, ACTION_NONE },

    { STATE_SUBCATEGORY, CMD_SEARCH_FAVORITE, STATE_SUBCATEGORY, ACTION_SEARCH_SUBCATEGORY },

    { STATE_SUBCATEGORY, EVENT_MODEL_SYNC_FINISHED, STATE_SUBCATEGORY, ACTION_NONE },

    { STATE_SUBCATEGORY, CMD_SYNC, STATE_SUBCATEGORY, ACTION_SYN_FAVORITE },

    { STATE_SUBCATEGORY, CMD_COMMON_DELETE, STATE_DELETE_FAVORITE, ACTION_NONE },

    { STATE_SUBCATEGORY, CMD_COMMON_CALL, STATE_SUBCATEGORY, ACTION_CALL },

    { STATE_SUBCATEGORY, CMD_MAP_ADDRESS, STATE_GOTO_MAP, ACTION_NONE },

    { STATE_SUBCATEGORY, CMD_DRIVETO_ADDRESS, STATE_GOTO_NAV, ACTION_NONE },

    { STATE_SUBCATEGORY_EDIT_MODE, CMD_SEARCH_FAVORITE, STATE_SUBCATEGORY_EDIT_MODE, ACTION_SEARCH_SUBCATEGORY },

    { STATE_ANY, EVENT_CONTROLLER_CATEGORY_DELETED, STATE_PREV, ACTION_CATEGORY_DELETED },

    { STATE_ANY, EVENT_CONTROLLER_CATEGORY_RENAMED, STATE_PREV, ACTION_UPDATE_DATA },

    { STATE_ANY, CMD_FAVORITE_SELECTED, STATE_ADDRESSES_SELECTED, ACTION_NONE },

    { STATE_ANY, EVENT_CONTROLLER_CATEGORY_ADDED, STATE_PREV, ACTION_UPDATE_DATA },

    { STATE_ANY, CMD_QUIT_EDIT_MODE, STATE_PREV, ACTION_BACKGROUND_SYNC },

    { STATE_ANY, CMD_ADD_FAVORITE, STATE_ADD_FAVORITE, ACTION_NONE },
    
    { STATE_ANY, EVENT_CONTROLLER_FAVORITE_EDITED, STATE_PREV, ACTION_UPDATE_DATA},
    
    { STATE_ANY, EVENT_CONTROLLER_BACK_TO_PREV, STATE_PREV, ACTION_CHECK_MIGRATION},
    
    { STATE_ANY, CMD_SELECT_ADDRESS, STATE_ADDRESS_SELECTED, ACTION_NONE },

    { STATE_ANY, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
    
    { STATE_ANY, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },
    
    };

    public FavoriteController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_ADDRESS_SELECTED:
            {
                PoiDataWrapper poiDataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
                poiDataWrapper.addStopAddress((Address) model.get(KEY_O_SELECTED_ADDRESS));
                poiDataWrapper.addNormalAddr((Address) model.get(KEY_O_SELECTED_ADDRESS));
                ModuleFactory.getInstance().startDetailController(this, TYPE_DETAIL_FROM_FAVORITE, model.getInt(KEY_I_AC_TYPE), 0, poiDataWrapper,
                    (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER), model.getBool(KEY_B_IS_SEARCH_NEAR_BY));
                break;
            }
            case STATE_ADD_CATEGORY:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditCategoryController(this, null, userProfileProvider);
                break;
            }
            case STATE_EDIT_CATEGORY:
            case STATE_EDIT_SUBCATEGORY:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditCategoryController(this, (FavoriteCatalog) model.get(KEY_O_CATEGORY), userProfileProvider);
                break;
            }
            case STATE_ADD_FAVORITE:
            {
                FavoriteCatalog category = null;
                if (currentState == STATE_SUBCATEGORY_EDIT_MODE || currentState == STATE_SUBCATEGORY)
                {
                    category =  (FavoriteCatalog) model.get(KEY_O_CATEGORY);
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAddPlaceController(this, ICommonConstants.PLACE_OPERATION_TYPE_ADD, null, null, null, category, userProfileProvider);
                break;
            }
            case STATE_EDIT_FAVORITE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditPlaceController(this, (Address) model.fetch(KEY_O_SELECTED_ADDRESS), ICommonConstants.PLACE_OPERATION_TYPE_EDIT, userProfileProvider);
                break;
            }
            case STATE_ADD_TO_FOLDER:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditPlaceController(this, (Address) model.fetch(KEY_O_SELECTED_ADDRESS), ICommonConstants.PLACE_OPERATION_TYPE_ADD_TO_FOLDER, userProfileProvider);
                break;
            }
            case STATE_DELETE_FAVORITE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditFavoriteController(this, (Address) model.fetch(KEY_O_SELECTED_ADDRESS),
                    EVENT_CONTROLLER_DELETE_FAVORITE, userProfileProvider);
                break;
            }
            case STATE_DELETE_CATEGORY_BY_MENU:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditCategoryController(this, (FavoriteCatalog) model.get(KEY_O_CATEGORY),
                    EVENT_CONTROLLER_DELETE_CATEGORY, userProfileProvider);
                break;
            }
            case STATE_RENAME_CATEGORY_BY_MENU:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditCategoryController(this, (FavoriteCatalog) model.get(KEY_O_CATEGORY),
                    EVENT_CONTROLLER_RENAME_CATEGORY, userProfileProvider);
                break;
            }
            case STATE_ADDRESSES_SELECTED:
            {
                Parameter data = new Parameter();
                data.put(KEY_O_SELECTED_ADDRESS, model.fetch(KEY_O_SELECTED_ADDRESS));
                postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                break;
            }
            case STATE_GOTO_NAV:
            {
                ModuleFactory.getInstance().startNavController(this, null, (Address) model.fetch(KEY_O_SELECTED_ADDRESS), model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS),
                    false, false);
                break;
            }
            case STATE_GOTO_MAP:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_FAVORITE, (Address) model.get(KEY_O_SELECTED_ADDRESS), null, userProfileProvider);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new FavoriteViewTouch(new FavoriteUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new FavoriteModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
}
