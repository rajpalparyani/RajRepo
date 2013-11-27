/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PluginController.java
 *
 */
package com.telenav.sdk.plugin.module;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.module.ModuleFactory;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.entry.AbstractCommonEntryController;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.plugin.PluginDataProvider;
import com.telenav.sdk.plugin.PluginManager;

/**
 *@author qli
 *@date 2011-2-24
 */
public class PluginController extends AbstractCommonEntryController implements IPluginConstants 
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN,         EVENT_CONTROLLER_START,             STATE_INIT,                 ACTION_CHECK_REGION_DETECTION_STATUS },
        { STATE_ANY,         EVENT_MODEL_CHECK_REGION,             STATE_ANY,                 ACTION_CHECK_REGION_CHANGE },
        
        { STATE_INIT,           EVENT_MODEL_INIT_PLUGIN,            STATE_PLUGIN_CHOICES,         ACTION_NONE },
        { STATE_INIT,           EVENT_MODEL_START_PLUGIN,           STATE_START_PLUGIN,         ACTION_START_PLUGIN },
        { STATE_INIT,           EVENT_MODEL_DONE_PLUGIN,            STATE_START_PLUGIN,         ACTION_GOTO_MODULE },
        { STATE_INIT,           EVENT_MODEL_DO_ONEBOX_RGC,            STATE_DO_ONEBOX_RGC,         ACTION_DO_ONEBOX_RGC },
        { STATE_INIT,           EVENT_MODEL_RGC_ADDRESS,          STATE_PLUGIN_RGC_ADDRESS,   ACTION_RGC_ADDRESS },
        { STATE_DO_ONEBOX_RGC,  EVENT_MODEL_HANDLE_PLUGIN,            STATE_PLUGIN_GOTO_MODULE,         ACTION_NONE },
        { STATE_DO_ONEBOX_RGC,  EVENT_MODEL_DONE_PLUGIN,            STATE_START_PLUGIN,         ACTION_GOTO_MODULE },
        { STATE_PLUGIN_CHOICES,   CMD_START_PLUGIN,                   STATE_START_PLUGIN,         ACTION_START_PLUGIN },
        { STATE_START_PLUGIN,   EVENT_MODEL_RGC_ADDRESS,            STATE_PLUGIN_RGC_ADDRESS,   ACTION_RGC_ADDRESS },
        { STATE_START_PLUGIN,   EVENT_MODEL_HANDLE_PLUGIN,          STATE_PLUGIN_GOTO_MODULE,   ACTION_NONE },
        { STATE_PLUGIN_RGC_ADDRESS,   EVENT_MODEL_HANDLE_PLUGIN,    STATE_PLUGIN_GOTO_MODULE,   ACTION_NONE },
        { STATE_ANY,            EVENT_MODEL_VALIDATE_ADDRESS,       STATE_GOTO_VALIDATE,        ACTION_NONE },
        { STATE_GOTO_VALIDATE,  EVENT_CONTROLLER_ADDRESS_VALIDATED, STATE_START_PLUGIN,         ACTION_GOTO_MODULE },
        { STATE_ANY,            EVENT_MODEL_DO_ONEBOX_SEARCH,       STATE_GOTO_ONEBOX_SEARCH,        ACTION_NONE },
        { STATE_GOTO_ONEBOX_SEARCH,   EVENT_CONTROLLER_ADDRESS_SELECTED,       STATE_CHECK_ONE_BOX_RETURN,        ACTION_CHECK_ONE_BOX_RETURN },
        { STATE_CHECK_ONE_BOX_RETURN,   EVENT_MODEL_START_PLUGIN,       STATE_START_PLUGIN,        ACTION_GOTO_MODULE },
        { STATE_ANY,            EVENT_CONTROLLER_EXIT_MAITAI,       STATE_VIRGIN,               ACTION_NONE },
        { STATE_ANY,            CMD_COMMON_BACK,                    STATE_VIRGIN,               ACTION_JUMP_BACKGROUND },
    
        //for link button
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_MAP, STATE_DO_LINK, ACTION_LINK_TO_MAP },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_AC, STATE_DO_LINK, ACTION_LINK_TO_AC },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_POI, STATE_DO_LINK, ACTION_LINK_TO_POI },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_EXTRA, STATE_DO_LINK, ACTION_LINK_TO_EXTRA },
        
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_MAP, STATE_LINK_TO_MAP, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_AC, STATE_LINK_TO_AC, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_POI, STATE_LINK_TO_POI, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_EXTRA, STATE_LINK_TO_EXTRA, ACTION_NONE },
        
        //for new plugin
        { STATE_ANY, EVENT_CONTROLLER_GOTO_PLUGIN, STATE_INIT, ACTION_CHECK_REGION_DETECTION_STATUS},
        
        { STATE_ANY,   EVENT_MODEL_SWITCHING_FAIL,         STATE_SWITCHING_FAIL,       ACTION_NONE },        
        { STATE_ANY,   EVENT_MODEL_SWITCHING_SUCC_FROM_SERVER,         STATE_INIT,       ACTION_INIT },        
        { STATE_SWITCHING_FAIL,     CMD_COMMON_BACK,                    STATE_INIT,                 ACTION_INIT },        
        //{ STATE_SWITCHING_SUCC,     CMD_COMMON_BACK,                    STATE_INIT,                 ACTION_INIT },  
        { STATE_ANY,   EVENT_MODEL_CONTINUE,                           STATE_INIT,       ACTION_INIT }, 
        
        // for native share
        { STATE_PLUGIN_GOTO_MODULE, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },
        { STATE_PLUGIN_GOTO_MODULE, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_COMMON_TIMEOUT_MESSAGEBOX, ACTION_NONE },
        { STATE_COMMON_TIMEOUT_MESSAGEBOX, CMD_COMMON_OK, STATE_SHARE_SUCCESS, ACTION_NONE },
        { STATE_COMMON_TIMEOUT_MESSAGEBOX, CMD_COMMON_BACK, STATE_SHARE_SUCCESS, ACTION_NONE },
        
    };

    public PluginController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_VIRGIN:
            {
                this.model.removeAll();
                PluginManager.getInstance().finish();
                break;
            }
            case STATE_PLUGIN_GOTO_MODULE:
            {
                this.model.put(KEY_B_PLUGIN_GOTO_MODULE, true);
                handleModule();
                break;
            }
            case STATE_GOTO_VALIDATE:
            {
                String addr = this.model.getString(KEY_S_VALIDATE_ADDRESS);
                String country = this.model.getString(KEY_S_VALIDATE_COUNTRY);
                if( addr != null )
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startAddressValidatorController(this, addr, "", TYPE_FROM_MAITAI, null, userProfileProvider, country);
                }
                break;
            }
            case STATE_GOTO_ONEBOX_SEARCH:
            {
                String oneboxSearch = DaoManager.getInstance().getServerDrivenParamsDao().getBoxType(this.getRegion());
                boolean isOneBoxSupport = ServerDrivenParamsDao.ONE_BOX_TYPE_STANDARD.equals(oneboxSearch);
                int  searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
                if (isOneBoxSupport)
                {
                    searchType = IOneBoxSearchProxy.TYPE_SEARCH_AROUND_ME;
                }
                ModuleFactory.getInstance().startOneBoxController(this, -1, searchType, -1, model.getString(KEY_S_VALIDATE_ADDRESS), true, true, null, null, null, null, false, false, false);
                break;
            }
            case STATE_SHARE_SUCCESS:
            {
                releaseAll();
                ModuleFactory.getInstance().startMain();
                break;
            }
        }
        super.postStateChangeDelegate(currentState, nextState);
    }
    
    protected void handleModule()
    {
        String action = (String)this.model.fetchString(KEY_S_PLUGIN_ACTION);
        action = convertActionName(action);
        if( IMaiTai.MENU_ITEM_DRIVE_TO.equalsIgnoreCase(action) )
        {
            ModuleFactory.getInstance().startNavController(this, null, 
                (Address) this.model.fetch(KEY_O_SELECTED_ADDRESS),
                model.fetchVector(KEY_V_STOP_AUDIOS), false, false);
        }
        else if( IMaiTai.MENU_ITEM_DIRECTIONS.equalsIgnoreCase(action) )
        {
//            ModuleFactory.getInstance().startNavController(this, (Address) this.model.fetch(KEY_O_ADDRESS_ORI),
//                (Address) this.model.fetch(KEY_O_ADDRESS_DEST), model.fetchVector(KEY_V_STOP_AUDIOS), false, false);
            ModuleFactory.getInstance().startNavController(this, null, (Address) this.model.fetch(KEY_O_ADDRESS_DEST),
                model.fetchVector(KEY_V_STOP_AUDIOS), false, false);
        }
        else if( IMaiTai.MENU_ITEM_SHARE_ADDRESS.equalsIgnoreCase(action) )
        {
            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
            ModuleFactory.getInstance().startNativeShareController(this, 
                (Address) this.model.fetch(KEY_O_SELECTED_ADDRESS), userProfileProvider);
        }
        else if( IMaiTai.MENU_ITEM_MAP_VALUE.equalsIgnoreCase(action) )
        {
            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
            Object addr = this.model.fetch(KEY_O_SELECTED_ADDRESS);
            if( addr instanceof Address )
            {
                ModuleFactory.getInstance().startMapController(this, 
                    TYPE_FROM_MAITAI, (Address)addr, null, userProfileProvider);
            }
            else if( addr instanceof Address[] )
            {
                ModuleFactory.getInstance().startMapController(this, 
                    TYPE_MAP_FROM_MAITAI_POI, (Address[])addr, null, userProfileProvider);
            }
            else if(addr instanceof PoiDataWrapper)
            {
                ModuleFactory.getInstance().startMapController(this, 
                    TYPE_MAP_FROM_POI, (PoiDataWrapper)addr, null, 1, userProfileProvider);
            }    
        }
        else if( IMaiTai.MENU_ITEM_BIZ_VALUE.equalsIgnoreCase(action) )
        {
            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
            if(userProfileProvider == null)
            {
                userProfileProvider = new UserProfileProvider();
            }
            String oneboxSearch = DaoManager.getInstance().getServerDrivenParamsDao().getBoxType(this.getRegion());
            boolean isOneBoxSearch=ServerDrivenParamsDao.ONE_BOX_TYPE_STANDARD.equals(oneboxSearch);
            int  searchType=isOneBoxSearch?IOneBoxSearchProxy.TYPE_SEARCH_AROUND_ME: IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
            ModuleFactory.getInstance().startOneBoxController(this, -1, 
                searchType, -1, 
                (String)this.model.fetch(KEY_S_COMMON_SEARCH_TEXT), true, false, 
                (Address)this.model.fetch(KEY_O_SELECTED_ADDRESS), 
                null, null, userProfileProvider, false, false, false);
        }
    }
    
    protected String convertActionName(String actionName)
    {
        String newName = null;
        if (actionName.equalsIgnoreCase(PluginDataProvider.ACTION_NAV_VALUE)
                || actionName.equalsIgnoreCase(PluginDataProvider.ACTION_AC_VALUE))
        {
            newName = IMaiTai.MENU_ITEM_DRIVE_TO;
        }
        else if(actionName.equalsIgnoreCase(PluginDataProvider.ACTION_MAP_VALUE))
        {
            newName = IMaiTai.MENU_ITEM_MAP_VALUE;
        }
        else if(actionName.equalsIgnoreCase(PluginDataProvider.ACTION_BIZ_VALUE))
        {
            newName = IMaiTai.MENU_ITEM_BIZ_VALUE;
        }
        else if(actionName.equalsIgnoreCase(PluginDataProvider.ACTION_SHAREADDRESS_VALUE))
        {
            newName = IMaiTai.MENU_ITEM_SHARE_ADDRESS;
        }
        else
        {
            newName = actionName;
        }
        return newName;
    }

    protected AbstractView createView()
    {
        return new PluginViewTouch(new PluginUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new PluginModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
    
    protected int backToLastStableState()
    {
        int state = super.backToLastStableState();
        if(STATE_VIRGIN == state)
        {
            this.model.removeAll();
            PluginManager.getInstance().finish();
        }
        return state;
    }

}
