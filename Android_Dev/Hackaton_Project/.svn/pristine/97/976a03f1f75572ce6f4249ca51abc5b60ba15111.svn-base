/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BrowserSdkController.java
 *
 */
package com.telenav.module.browsersdk;


import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.ModuleFactory;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.kontagent.KontagentLogger;

/**
 *@author qli
 *@date 2010-12-29
 */
public class BrowserSdkController extends AbstractCommonController implements IBrowserSdkConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN,             EVENT_CONTROLLER_START,             STATE_BROWSER_INIT,                 ACTION_INIT },
        { STATE_BROWSER_INIT,       EVENT_MODEL_LAUNCH_BROWSER,         STATE_BROWSER_MAIN,                 ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_GOTO_AC,                STATE_BROWSER_GOTO_AC,              ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_GOTO_MAP,               STATE_BROWSER_GOTO_MAP,             ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_GOTO_NAV,               STATE_BROWSER_GOTO_NAV,             ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_UPDATE_MAIN,            STATE_BROWSER_MAIN,                 ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_GOTO_SHARE_ADDR,        STATE_BROWSER_GOTO_SHARE_ADDR,      ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
        { STATE_ANY,                EVENT_MODEL_GOTO_SEARCH_NEARBY,     STATE_BROWSER_GOTO_SEARCH_NEARBY,   ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_EXIT_BROWSER,           STATE_VIRGIN,                       ACTION_NONE },
        { STATE_BROWSER_GOTO_AC,    EVENT_CONTROLLER_ADDRESS_SELECTED,  STATE_PREV,                         ACTION_AC_ADDRESS },
        { STATE_ANY,                EVENT_MODEL_SYNC_PURCHASE_STATUS,   STATE_BROWSER_SYNC_PURCHASE_STATUS, ACTION_NONE },
        { STATE_BROWSER_SYNC_PURCHASE_STATUS,   EVENT_MODEL_SYNC_PURCHASE_STATUS_DONE,    STATE_PREV,       ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_LAUNCH_LOCALAPP,        STATE_BROWSER_GOTO_LAUNCH_LOCALAPP,   ACTION_NONE },
        { STATE_ANY,                EVENT_MODEL_LAUNCH_SETTING,         STATE_BROWSER_GOTO_LAUNCH_SETTING,      ACTION_NONE },
    };

    public BrowserSdkController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_VIRGIN:
            {
                AbstractController parent = this.getSuperController();
                if ( parent != null )
                {
                    Parameter param = new Parameter();
                    param.put(KEY_B_IS_NEED_REFRESH, model.fetchBool(KEY_B_IS_NEED_REFRESH));
                    parent.getModel().add(param);
                }
                break;
            }
            case STATE_BROWSER_GOTO_AC:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAcController(this, TYPE_AC_FROM_BROWSER, true, true, false, userProfileProvider);
                break;
            }
            case STATE_BROWSER_GOTO_MAP:
            {
                
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                if(dataWrapper != null)
                {
                    int acType = model.getInt(KEY_I_AC_TYPE);
                    String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                    PoiMisLogHelper.getInstance().setIsMapResulstMode(false);
                    boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                    ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_BROWSER, dataWrapper, searchText, acType, userProfileProvider, isSearchNearBy);
                }
                else
                {
                    Address addr = (Address) model.get(ICommonConstants.KEY_O_SELECTED_ADDRESS);
                    String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                    ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_BROWSER, addr,searchText, userProfileProvider);
                }
                break;
            }
            case STATE_BROWSER_GOTO_NAV:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS, KontagentLogger.POIINFO_DRIVE_CLICKED);
                Address oriAddr = (Address) model.get(ICommonConstants.KEY_O_ADDRESS_ORI);
                Address destAddr = (Address) model.get(ICommonConstants.KEY_O_ADDRESS_DEST);
                boolean isFromSearchAlong = NavSdkNavEngine.getInstance().isRunning();
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
                ModuleFactory.getInstance().startNavController(this, oriAddr, destAddr, poiDataWrapper, false,
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), isFromSearchAlong, false);
                break;
            }
            case STATE_BROWSER_GOTO_SHARE_ADDR:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS, KontagentLogger.POIINFO_SHARE_CLICKED);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startNativeShareController(this, (Address) model.get(KEY_O_SELECTED_ADDRESS), userProfileProvider);
                break;
            }
            case STATE_BROWSER_GOTO_SEARCH_NEARBY:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startCategorySearchController(this, model.getInt(KEY_I_AC_TYPE),
                    IPoiSearchProxy.TYPE_SEARCH_ADDRESS, -1, -1, -1, (Address) model.get(KEY_O_SELECTED_ADDRESS), null, null, userProfileProvider, true);
                break;
            }
            case STATE_BROWSER_GOTO_LAUNCH_LOCALAPP:
            {
                ModuleFactory.getInstance().startLaunchLocalApp(this, (String)model.get(KEY_O_NEW_WINDOW_URL));
                break;
            }
            case STATE_BROWSER_GOTO_LAUNCH_SETTING:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startPreferenceController(this, userProfileProvider);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new BrowserSdkModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

    protected AbstractView createView()
    {
        return new BrowserSdkViewTouch(new BrowserSdkUiDecorator());
    }

}
