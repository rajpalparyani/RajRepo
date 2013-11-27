/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteController.java
 *
 */
package com.telenav.module.ac.favorite.favoriteeditor;

import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
public class FavoriteEditorController extends AbstractCommonController implements IFavoriteEditorConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {

    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },

    { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE },
    
    { STATE_MAIN, CMD_NEW_CATEGORY, STATE_ADD_CATEGORY, ACTION_NONE },

    { STATE_MAIN, CMD_UPDATE_FAVORITE, STATE_CHECK_LABEL, ACTION_CHECK_NAME_EXIST },

    { STATE_MAIN, CMD_DELETE_FAVORITE, STATE_DELETE_FAVORITE, ACTION_NONE },

    { STATE_CHECK_LABEL, EVENT_MODEL_SAVE_FAVORITE, STATE_FAVORITE_UPDATED, ACTION_SAVE_FAVORITE },
    
    { STATE_CHECK_LABEL, EVENT_MODEL_NOT_SAVE_FAVORITE, STATE_FAVORITE_EDITED, ACTION_NONE },
    
    { STATE_COMMON_TIMEOUT_MESSAGEBOX, CMD_COMMON_OK, STATE_MAIN, ACTION_NONE},
    
    { STATE_FAVORITE_UPDATED, CMD_COMMON_OK, STATE_FAVORITE_EDITED, ACTION_NONE},

    { STATE_DELETE_FAVORITE, CMD_COMMON_OK, STATE_FAVORITE_DELETED, ACTION_DELETE_FAVORITE },

    { STATE_DELETE_FAVORITE, CMD_COMMON_CANCEL, STATE_PREV, ACTION_NONE },
    
    { STATE_FAVORITE_DELETED, CMD_COMMON_OK, STATE_FAVORITE_EDITED, ACTION_NONE},

    { STATE_ANY, EVENT_CONTROLLER_CATEGORY_ADDED, STATE_PREV, ACTION_SET_SELECTED_CATEGORY },
    
    { STATE_ANY, EVENT_CONTROLLER_DELETE_FAVORITE, STATE_DELETE_FAVORITE, ACTION_NONE}

    };

    public FavoriteEditorController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_ADD_CATEGORY:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startEditCategoryController(this, null, userProfileProvider);
                break;
            }
            case STATE_FAVORITE_EDITED:
            {
                postControllerEvent(EVENT_CONTROLLER_FAVORITE_EDITED, null);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new FavoriteEditorViewTouch(new FavoriteEditorUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new FavoriteEditorModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

}
