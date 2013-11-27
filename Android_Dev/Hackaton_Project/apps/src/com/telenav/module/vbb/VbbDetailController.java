/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * VbbDetailController.java
 *
 */
package com.telenav.module.vbb;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.module.map.MapController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author yning
 *@date 2013-1-6
 */
public class VbbDetailController extends BrowserSdkController implements IVbbDetailConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN,             EVENT_CONTROLLER_START,             STATE_VBB_DETAIL,                 ACTION_NONE },
        { STATE_VBB_DETAIL,         EVENT_MODEL_MAP_AD,                 STATE_GOTO_MAP_AD,                ACTION_NONE },
        { STATE_VBB_DETAIL,         EVENT_MODEL_DRIVE_TO_AD,            STATE_GOTO_DRIVE_TO_AD,           ACTION_NONE },
        { STATE_VBB_DETAIL,         CMD_COMMON_BACK,                    STATE_BACK_TO_INVOKER,            ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_GOTO_SHARE_ADDR, STATE_BROWSER_GOTO_SHARE_ADDR, ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
        { STATE_ANY,                EVENT_MODEL_GOTO_SEARCH_NEARBY,     STATE_BROWSER_GOTO_SEARCH_NEARBY, ACTION_NONE },
        
    };
    
    public VbbDetailController(AbstractController superController)
    {
        super(superController);
    }

    @Override
    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_GOTO_MAP_AD:
            {
                Vector maps = getControllerInStack(MapController.class.getName());
                boolean isFromMap= false;
                if(maps != null && maps.size() > 0)
                {
                    for(int i = 0; i < maps.size(); i++)
                    {
                        MapController mapController = (MapController) maps.elementAt(i);
                        int tmpMapFromType = mapController.getModel().getInt(
                            KEY_I_TYPE_MAP_FROM);
                        if (tmpMapFromType == TYPE_MAP_FROM_BILLBOARD)
                        {
                            isFromMap = true;
                            break;
                        }
                    }
                }
                
                if(isFromMap)
                {
                    Parameter param = new Parameter();
                    param.put(KEY_O_AD_ADDRESS, model.get(KEY_O_AD_ADDRESS));
                    postControllerEvent(EVENT_CONTROLLER_MAP_VBB, param);
                }
                else
                {
                    Address adAddress = (Address) this.model.get(KEY_O_AD_ADDRESS);
                    Address destAddress = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                    String adUrl = model.getString(KEY_S_AD_DETAIL_URL);
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_BILLBOARD, adAddress, destAddress, null, userProfileProvider, adUrl);
                }
                
                break;
            }
            case STATE_GOTO_DRIVE_TO_AD:
            {
                Parameter param = new Parameter();
                param.put(KEY_O_AD_ADDRESS, model.get(KEY_O_AD_ADDRESS));
                postControllerEvent(EVENT_CONTROLLER_DRIVE_TO_VBB, param);
                break;
            }
            case STATE_BACK_TO_INVOKER:
            {
                postControllerEvent(EVENT_CONTROLLER_BACK_FROM_VBB_DETAIL, null);
                break;
            }
        }
        super.postStateChangeDelegate(currentState, nextState);
    }
    
    @Override
    protected AbstractView createView()
    {
        return new VbbDetailViewTouch(new VbbDetailUiDecorator());
    }
    
    @Override
    protected AbstractModel createModel()
    {
        return new VbbDetailModel();
    }
    
    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
}
