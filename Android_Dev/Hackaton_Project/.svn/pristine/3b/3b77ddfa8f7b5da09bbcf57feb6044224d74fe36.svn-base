/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AddPlaceController.java
 *
 */
package com.telenav.module.ac.place.addplace;

import android.content.Context;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
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
 *@author Casper(pwang@telenav.cn)
 *@date 2013-2-21
 */
public class AddPlaceController extends AbstractCommonController implements IAddPlaceConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
    { STATE_ANY, EVENT_MODEL_START_MAIN, STATE_MAIN, ACTION_RETRIEVE_PLACES_LIST },
    { STATE_ANY, EVENT_MODEL_NO_GPS_WARNING, STATE_NO_GPS_WARNING, ACTION_NONE },
    { STATE_MAIN, EVENT_MODEL_REFRESH_PLACES_LIST, STATE_MAIN, ACTION_NONE },

    { STATE_MAIN, CMD_GETTING_MORE, STATE_MAIN, ACTION_GETING_MORE },

    { STATE_MAIN, CMD_SELECT_ITEM, STATE_ITEM_CHECK_SELECTED, ACTION_CHECK_SELECT },
    
    { STATE_ANY, EVENT_MODEL_CUSTOM_PLACE, STATE_CUSTOM_PLACE, ACTION_NONE },
    { STATE_CUSTOM_PLACE, CMD_CUSTOM_PLACE_DONE, STATE_CHECK_PLACE_LABEL, ACTION_CHECK_PLACE_LABEL },
    { STATE_CHECK_PLACE_LABEL, EVENT_MODEL_SAVE_PLACE, STATE_SAVE_PLACE, ACTION_SAVE_PLACE },
    { STATE_ANY, EVENT_MODEL_PLACE_ADDED, STATE_PLACE_ADDED, ACTION_NONE },
    { STATE_CHECK_PLACE_LABEL, EVENT_MODEL_NO_NEED_SAVE_AGAIN, STATE_PLACE_ADDED, ACTION_NONE },
    
    
    { STATE_ITEM_CHECK_SELECTED, EVENT_MODEL_SHARE_PLACE, STATE_GOTO_NATIVE_SHARE, ACTION_NONE },
    { STATE_GOTO_NATIVE_SHARE, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_MAIN, ACTION_NONE },
    
    { STATE_ANY, CMD_ADD_CATEGORY, STATE_ADD_CATEGORY, ACTION_NONE },
    { STATE_ANY, EVENT_CONTROLLER_CATEGORY_ADDED, STATE_PREV, ACTION_CATEGORY_UPDATE },

    };

    public AddPlaceController(AbstractController superController)
    {
        this(superController, false);
    }
    
    public AddPlaceController(AbstractController superController, boolean isEdit)
    {
        super(superController);
        this.model = createModel(isEdit);
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
            case STATE_GOTO_NATIVE_SHARE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startNativeShareController(this, (Address) this.model.get(KEY_O_SELECTED_ADDRESS), userProfileProvider);
                break;
            }
            case STATE_PLACE_ADDED:
            {
                Parameter data = new Parameter();
                Address address = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
                data.put(KEY_O_SELECTED_ADDRESS, address);
                Context context = AndroidPersistentContext.getInstance().getContext();
                String label = model.getString(KEY_S_PLACE_LABEL);
                data.put(KEY_S_COMMON_MESSAGE, context.getString(R.string.addplace_palce_added_prompt, label));
                int type = this.model.getInt(KEY_I_PLACE_OPERATION_TYPE);
                if (type == PLACE_OPERATION_TYPE_ADD)
                    postControllerEvent(EVENT_CONTROLLER_PLACE_ADDED, data);
                else
                    postControllerEvent(EVENT_CONTROLLER_PLACE_EDIT, data);                    
                break;
            }
        }
    }
 
    protected AbstractView createView()
    {
        return new AddPlaceViewTouch(new AddPlaceUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return this.createModel(false);
    }
    
    protected AbstractModel createModel(boolean isEdit)
    {
        if(isEdit)
        {
            return new EditPlaceModel();
        }
        else
        {
            return new AddPlaceModel();
        }
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
}
