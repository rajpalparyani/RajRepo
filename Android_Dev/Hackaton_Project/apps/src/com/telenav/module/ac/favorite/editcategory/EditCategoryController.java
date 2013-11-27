/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteController.java
 *
 */
package com.telenav.module.ac.favorite.editcategory;

import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
public class EditCategoryController extends AbstractCommonController implements IEditCategoryConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {

    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },

    { STATE_INIT, EVENT_MODEL_LAUNCH_EDIT_CATEGORY, STATE_MAIN, ACTION_NONE },
    
    { STATE_INIT, EVENT_MODEL_LAUNCH_NEW_CATEGORY, STATE_NEW_CATEGORY, ACTION_NONE},

    { STATE_MAIN, CMD_RENAME_CATEGORY, STATE_MAIN, ACTION_CHECK_CATEGORY },

    { STATE_MAIN, EVENT_MODEL_RENAME_CATEGORY, STATE_UPDATE_CATEGORY, ACTION_RENAME_CATEGORY },

    { STATE_MAIN, CMD_DELETE_CATEGORY, STATE_DELETE_CATEGORY, ACTION_NONE },
    
    { STATE_UPDATE_CATEGORY, CMD_COMMON_OK, STATE_CATEGORY_UPDATED, ACTION_NONE},

    { STATE_DELETE_CATEGORY, CMD_COMMON_OK, STATE_DELETE_CATEGORY_NOTIFICATION, ACTION_DELETE_CATEGORY },

    { STATE_DELETE_CATEGORY, CMD_COMMON_CANCEL, STATE_VIRGIN, ACTION_NONE },
    
    { STATE_DELETE_CATEGORY, CMD_COMMON_BACK, STATE_VIRGIN, ACTION_NONE },
    
    { STATE_DELETE_CATEGORY_NOTIFICATION, CMD_COMMON_OK, STATE_CATEGORY_DELETED, ACTION_NONE},
    
    { STATE_DELETE_CATEGORY_NOTIFICATION, CMD_COMMON_BACK, STATE_CATEGORY_DELETED, ACTION_NONE},
    
    { STATE_ANY, EVENT_CONTROLLER_DELETE_CATEGORY, STATE_DELETE_CATEGORY,ACTION_NONE},
    
    { STATE_ANY, EVENT_CONTROLLER_RENAME_CATEGORY, STATE_MAIN,ACTION_NONE},
    
    { STATE_NEW_CATEGORY, CMD_ADD_CATEGORY, STATE_CHECK_CATEGORY_EXIST, ACTION_CHECK_CATEGORY_EXIST },

    { STATE_NEW_CATEGORY, CMD_COMMON_CANCEL, STATE_PREV, ACTION_NONE },
    
    { STATE_CHECK_CATEGORY_EXIST, EVENT_MODEL_ADD_CATEGORY, STATE_ADD_CATEGORY, ACTION_ADD_CATEGORY },
    
    { STATE_ADD_CATEGORY, CMD_COMMON_OK, STATE_CATEGORY_ADDED, ACTION_NONE},
    
    { STATE_ADD_CATEGORY, CMD_COMMON_BACK, STATE_CATEGORY_ADDED, ACTION_NONE},

    };

    public EditCategoryController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_CATEGORY_DELETED:
            {
                Parameter parameter = new Parameter();
                parameter.put(KEY_O_CATEGORY, model.fetch(KEY_O_CATEGORY));
                this.postControllerEvent(EVENT_CONTROLLER_CATEGORY_DELETED, parameter);
                break;
            }
            case STATE_CATEGORY_UPDATED:
            {
                Parameter parameter = new Parameter();
                parameter.put(KEY_O_CATEGORY, model.fetch(KEY_O_CATEGORY));
                this.postControllerEvent(EVENT_CONTROLLER_CATEGORY_RENAMED, parameter);
                break;
            }
            case STATE_CATEGORY_ADDED:
            {
                Parameter data = new Parameter();
                data.put(KEY_S_ADDED_CATEGORY, model.getString(KEY_S_NEW_CATEGROY_NAME));
                this.postControllerEvent(EVENT_CONTROLLER_CATEGORY_ADDED,data);
                break;
            }
        }
    }

    protected AbstractView createView()
    {
        return new EditCategoryViewTouch(new EditCategoryUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new EditCategoryModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

}
