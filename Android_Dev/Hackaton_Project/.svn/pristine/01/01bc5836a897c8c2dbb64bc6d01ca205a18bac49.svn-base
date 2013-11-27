/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DashboardController.java
 *
 */
package com.telenav.module.dashboard;

import android.os.RemoteException;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.datatypes.nav.NavState;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.module.ModuleFactory;
import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.IMaiTai;

/**
 *@author qli
 *@date 2012-2-3
 */
public class DashboardController extends BrowserSdkController implements IDashboardConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN,             EVENT_CONTROLLER_START,             STATE_INIT,                 IDashboardConstants.ACTION_INIT },
        { STATE_INIT,               EVENT_MODEL_LAUNCH_DASHBOARD,       STATE_DASHBOARD,            ACTION_NONE },        
        
        //update shared address badge
        { STATE_DASHBOARD,          EVENT_MODEL_UPDATE_SHARED_ADDRESS_BADGE,     STATE_DASHBOARD,   ACTION_NONE},
        
        { STATE_DASHBOARD,          EVENT_MODEL_CHECK_NAVSDK_DELAY_POPUP,     STATE_DASHBOARD,   ACTION_CHECK_NAVSDK_DELAY_POP_UP},
        
        { STATE_DASHBOARD,          CMD_CHECK_REGION_DETECT_STATUS,       STATE_DASHBOARD,            ACTION_CHECK_REGION_DETECT_STATUS },        
        
        { STATE_DETECTING_REGION,   EVENT_MODEL_CHECK_REGION,           STATE_DASHBOARD,            ACTION_REGION_DETECTION },        
        
        { STATE_DASHBOARD,          EVENT_MODEL_CHECK_REGION,           STATE_DASHBOARD,            ACTION_REGION_DETECTION },        
        { STATE_SWITCHING_REGION,   EVENT_MODEL_SWITCHING_FAIL,         STATE_SWITCHING_FAIL,       ACTION_NONE },        
        { STATE_SWITCHING_REGION,   EVENT_MODEL_SWITCHING_SUCC_FROM_SERVER,         STATE_PREV,       ACTION_CHECK_MAP_DOWNLOADED_STATUS },        
        { STATE_SWITCHING_FAIL,     CMD_COMMON_BACK,                    STATE_PREV,                 ACTION_CHECK_MAP_DOWNLOADED_STATUS },
        { STATE_ANY,     EVENT_MODEL_CHECK_FRIENDLY_USER,                STATE_ANY,                 ACTION_CHECK_FRIENDLY_USER_STATUS },
        
        { STATE_DASHBOARD,          CMD_COMMON_BACK,                    STATE_DASHBOARD,            ACTION_BACK_CHECK },    
        
        { STATE_DASHBOARD,          EVENT_MODEL_BACK_TO_PRE,            STATE_PREV,            ACTION_NONE }, 
        //After user clicks home button, we will check whether the home address is set or not
        { STATE_DASHBOARD,          CMD_HOME,                           STATE_CHECK_HOME_EXIST,     ACTION_CHECK_HOME_EXIST },

        //After user clicks work button, we will check whether the work address is set or not
        { STATE_DASHBOARD,          CMD_WORK,                           STATE_CHECK_WORK_EXIST,     ACTION_CHECK_WORK_EXIST },
        
        //User edit home by context menu
        { STATE_DASHBOARD,          CMD_EDIT_HOME,                 STATE_EDIT_HOME_WORK,       ACTION_NONE },
        //User edit work by context menu
        { STATE_DASHBOARD,          CMD_EDIT_WORK,                 STATE_EDIT_HOME_WORK,       ACTION_NONE },
        
        { STATE_DASHBOARD,          CMD_MAP_HOME,                       STATE_MAP_HOME,        ACTION_NONE},
        
        { STATE_DASHBOARD,          CMD_MAP_WORK,                       STATE_MAP_WORK,        ACTION_NONE},
        
        { STATE_DASHBOARD,          CMD_DRIVETO_HOME,                   STATE_DRIVE_HOME,      ACTION_NONE},
        
        { STATE_DASHBOARD,          CMD_DRIVETO_WORK,                   STATE_DRIVE_WORK,      ACTION_NONE},
        
        //friendly user
        { STATE_DASHBOARD, EVENT_MODEL_SHOW_FRIEND_USER_POPUP, STATE_SHOW_FRIEND_USER_POPUP, ACTION_NONE },
        
        { STATE_SHOW_FRIEND_USER_POPUP, CMD_RATE_APP, STATE_PREV, ACTION_RATE_APP },
        
        { STATE_SHOW_FRIEND_USER_POPUP, CMD_REMIND_ME_LATER, STATE_PREV, ACTION_REMIND_LATER },
        
        { STATE_SHOW_FRIEND_USER_POPUP, CMD_NO_THANKS, STATE_PREV, ACTION_NO_THANKS },
        
        { STATE_SHOW_FRIEND_USER_POPUP, CMD_COMMON_BACK, STATE_PREV, ACTION_CANCEL_POPUP },
        
        //if home exists, return home, else let user set home
        { STATE_CHECK_HOME_EXIST, EVENT_MODEL_RETURN_HOME, STATE_RETURN_HOME, ACTION_NONE },

        { STATE_CHECK_HOME_EXIST, EVENT_MODEL_SET_HOME_WORK, STATE_SET_HOME_WORK, ACTION_NONE },

        //if work exists, return work, else let user set work
        { STATE_CHECK_WORK_EXIST, EVENT_MODEL_RETURN_WORK, STATE_RETURN_WORK, ACTION_NONE },

        { STATE_CHECK_WORK_EXIST, EVENT_MODEL_SET_HOME_WORK, STATE_SET_HOME_WORK, ACTION_NONE },
        
        
        { STATE_CHECK_HOME_WORK_EXIST, EVENT_MODEL_SET_HOME_WORK,       STATE_SET_HOME_WORK,        ACTION_NONE },

        
        //User clicks places button, enter poi module
        { STATE_DASHBOARD, CMD_PLACES, STATE_GOTO_POI, ACTION_NONE },
        
        //User click drive to button
        { STATE_DASHBOARD, CMD_DRIVE_TO, STATE_DRIVE_TO, ACTION_NONE},

        
        { STATE_DASHBOARD, CMD_POI_CATEGORY_SELECTED, STATE_POI_SEARCHING, ACTION_POI_SEARCH },
        
        { STATE_POI_SEARCHING, EVENT_MODEL_GOTO_POI_LIST, STATE_GOTO_RESULT_LIST, ACTION_NONE },
        
        //Test Case
        { STATE_DASHBOARD, CMD_VIEW_TEST_CASE, STATE_VIEW_TEST_CASE, ACTION_NONE },
        
//        { STATE_DASHBOARD, CMD_FEEDBACK, STATE_GENERAL_FEEDBACK, ACTION_NONE },
        
        { STATE_DASHBOARD, CMD_WEATHER, STATE_WEATHER_DETAIL, ACTION_NONE },
        
        { STATE_ANY, EVENT_MODEL_GOTO_AC, STATE_BROWSER_GOTO_AC, ACTION_NONE },
        
        { STATE_ANY, EVENT_MODEL_LAUNCH_LOCALAPP, STATE_BROWSER_GOTO_LAUNCH_LOCALAPP, ACTION_NONE },
        
        { STATE_ANY, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_CHECK_ADDRESS_SELECTED, ACTION_ADDRESS_SELECTED_CHECK },
        { STATE_CHECK_ADDRESS_SELECTED, EVENT_MODEL_ADDRESS_RETURN, STATE_ADDRESS_RETURN, ACTION_NONE },
        { STATE_CHECK_ADDRESS_SELECTED , EVENT_BACK_TO_BROWSER , STATE_PREV ,ACTION_AC_ADDRESS},     
        
        { STATE_DASHBOARD, CMD_CLOSE_RESUME, STATE_DASHBOARD, ACTION_NONE },

        { STATE_DASHBOARD, CMD_FOLD_LOCATION, STATE_DASHBOARD, ACTION_NONE },
        
        { STATE_DASHBOARD, CMD_RESUME_TRIP, STATE_RESUME_TRIP, ACTION_NONE },
        
        {STATE_DASHBOARD,  EVENT_CONTROLLER_GOTO_FTUE_MAIN,  STATE_GOTO_FTUE,    ACTION_NONE},
        {STATE_DASHBOARD,  EVENT_CONTROLLER_GOTO_UPSELL,  STATE_GOTO_UPSELL,    ACTION_NONE},
        
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_PREV, STATE_DASHBOARD, ACTION_NONE },
        
        { STATE_DASHBOARD, CMD_GOTO_FAVORITE, STATE_GOTO_FAVORITE, ACTION_NONE },
        

        { STATE_DASHBOARD, CMD_GOTO_RECENT, STATE_GOTO_RECENT, ACTION_NONE },
        
        { STATE_DASHBOARD, CMD_GOTO_DWF, STATE_GOTO_DWF, ACTION_NONE},
        
        { STATE_DASHBOARD, CMD_SHARE_LOCATION, STATE_GOTO_SHARE, ACTION_NONE },
        
        { STATE_DASHBOARD, CMD_ADD_PLACE, STATE_ADD_PLACE, ACTION_NONE },
        
        { STATE_ANY, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
		
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_DASHBOARD, STATE_DASHBOARD,  ACTION_NONE },
        
        { STATE_MAP_DOWNLOADED_STATUS_CHANGED, CMD_COMMON_OK, STATE_GOTO_MAP_DOWNLOAD, ACTION_NONE },
        
        { STATE_MAP_DOWNLOADED_STATUS_CHANGED, CMD_COMMON_CANCEL, STATE_PREV, ACTION_NONE },
        
        { STATE_ANY, EVENT_MODEL_CONTINUE, STATE_DASHBOARD, ACTION_CHECK_MAP_DOWNLOADED_STATUS },
        
        { STATE_ANY, CMD_CONTINUE, STATE_DASHBOARD, ACTION_CHECK_MAP_DOWNLOADED_STATUS },
        
        { STATE_ANY, EVENT_MODEL_MAP_DOWNLOAD_ERROR, STATE_CHECK_NAVSDK_POPUP, ACTION_CHECK_FRIENDLY_POPUP_SHOW },
        
        { STATE_CHECK_NAVSDK_POPUP, EVENT_MODEL_SHOW_NAVSDK_POPUP, STATE_COMMON_MAP_DOWNLOAD_ERROR, ACTION_NONE },
        
        
    };

    public DashboardController(AbstractController superController)
    {
        super(superController);
    }

    protected AbstractView createView()
    {
        return new DashboardViewTouch(new DashboardUiDecorator());
    }

    protected AbstractModel createModel()
    {
        return new DashboardModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_RETURN_HOME:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_HOME_CLICKED);
                Stop homeStop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                Address homeAddress = new Address(Address.SOURCE_PREDEFINED);
                homeAddress.setStop(homeStop);
                Parameter parameter = new Parameter();
                parameter.put(KEY_O_SELECTED_ADDRESS, homeAddress);
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, parameter);
                break;
            }
            case STATE_RETURN_WORK:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_WORK_CLICKED);
                Stop workStop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                Address workAddress = new Address(Address.SOURCE_PREDEFINED);
                workAddress.setStop(workStop);
                Parameter parameter = new Parameter();
                parameter.put(KEY_O_SELECTED_ADDRESS, workAddress);
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, parameter);
                break;
            }
            case STATE_SET_HOME_WORK:
            case STATE_EDIT_HOME_WORK:
            {
                int type = model.getInt(KEY_I_HOME_WORK_TYPE);
                if(type == TYPE_HOME_ADDRESS)
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_SETUP_HOME_CLICKED);
                }
                else
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_SETUP_WORK_CLICKED);
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, model.getInt(KEY_I_HOME_WORK_TYPE), userProfileProvider);
                break;
            }
            case STATE_MAP_HOME:
            {
                Stop stop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                Address address = new Address(Address.SOURCE_PREDEFINED);
                address.setStop(stop);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_ADDRESS, address, null, userProfileProvider);
                break;
            }
            case STATE_MAP_WORK:
            {
                Stop stop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                Address address = new Address(Address.SOURCE_PREDEFINED);
                address.setStop(stop);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_ADDRESS, address, null, userProfileProvider);
                break;
            }
            case STATE_DRIVE_HOME:
            {
                Stop stop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                Address address = new Address(Address.SOURCE_PREDEFINED);
                address.setStop(stop);
                ModuleFactory.getInstance().startNavController(this, null, address, null, false, false);
                break;
            }
            case STATE_DRIVE_WORK:
            {
                Stop stop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                Address address = new Address(Address.SOURCE_PREDEFINED);
                address.setStop(stop);
                ModuleFactory.getInstance().startNavController(this, null, address, null, false, false);
                break;
            }
            case STATE_GOTO_POI:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_MORE_CLICKED);

                Address oriAddr = null;
                Address destAddr = null;
                NavState navState = null;
                Object oriObj = this.model.get(KEY_O_ADDRESS_ORI);
                if (oriObj instanceof Address)
                {
                    oriAddr = (Address) destAddr;
                }
                Object destObj = this.model.get(KEY_O_ADDRESS_DEST);
                if (destObj instanceof Address)
                {
                    destAddr = (Address) destObj;
                }
                Object navStatObj = this.model.get(KEY_O_NAVSTATE);
                if (navStatObj instanceof NavState)
                {
                    navState = (NavState) navStatObj;
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startCategorySearchController(this, model.getInt(KEY_I_AC_TYPE),
                    model.getInt(KEY_I_SEARCH_TYPE), -1, -1, -1, false, oriAddr, destAddr, navState, userProfileProvider, false);
                break;
            }
            case STATE_GOTO_RESULT_LIST:
            {
                int categoryId = this.model.fetchInt(KEY_I_CATEGORY_ID);
                int sortType = this.model.fetchInt(KEY_I_SEARCH_SORT_TYPE);
                int selectedIndex = this.model.fetchInt(KEY_I_POI_SELECTED_INDEX);
                int acType = this.model.getInt(KEY_I_AC_TYPE);
                int searchType = this.model.getInt(KEY_I_SEARCH_TYPE);
                int alongRouteType = this.model.getInt(KEY_I_SEARCH_ALONG_TYPE);
                String showText = this.model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                String searchString = this.model.getString(KEY_S_COMMON_SEARCH_TEXT);
                Address anchorAddr = (Address)this.model.get(KEY_O_ADDRESS_ORI);
                Address destAddr = (Address)this.model.get(KEY_O_ADDRESS_DEST);
                NavState navState = (NavState)this.model.get(KEY_O_NAVSTATE);
                boolean isChangeLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
                PoiDataWrapper dataWrapper = (PoiDataWrapper)this.model.get(KEY_O_POI_DATA_WRAPPER);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                
                String category = "";
                if(showText.startsWith("Gas"))
                {
                    category = "Gas";
                }
                else
                {
                    category = showText;
                }
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,  category + "_Clicked");
                
                ModuleFactory.getInstance().startPoiResultController(this, selectedIndex, categoryId,
                    acType, searchType, sortType, alongRouteType, isChangeLocation, showText, 
                    searchString, anchorAddr, destAddr, navState, dataWrapper, userProfileProvider, false);
                break;
            }
            case STATE_DRIVE_TO:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAcController(this, TYPE_AC_FROM_DASHBOARD, false, false, false, userProfileProvider);
                break;
            }
            case STATE_ADDRESS_RETURN:
            {
                if(model.fetchBool(KEY_B_IS_FROM_ONE_BOX))
                {
                    Address address = (Address) model.fetch(KEY_O_SELECTED_ADDRESS);
                    if (address != null)
                    {                      
                        PoiDataWrapper poiDataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
                        poiDataWrapper.addStopAddress(address);
                        poiDataWrapper.addNormalAddr(address);
                        ModuleFactory.getInstance().startDetailController(this, TYPE_DETAIL_FROM_FAVORITE, model.getInt(KEY_I_AC_TYPE), 0, poiDataWrapper,
                            (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER), model.getBool(KEY_B_IS_SEARCH_NEAR_BY));                       
                    }
                }
                else
                {
                    Parameter data = new Parameter();
                    data.put(KEY_O_SELECTED_ADDRESS, model.get(KEY_O_SELECTED_ADDRESS));
                    data.put(KEY_I_AC_TYPE, model.fetch(KEY_I_AC_TYPE));
                    postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                }

                break;
            }
            case STATE_RESUME_TRIP:
            {
                ModuleFactory.getInstance().startNavController(this, null, DaoManager.getInstance().getTripsDao().getLastTrip(),
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), false, true);
                break;
            }
            case STATE_GOTO_FTUE:
            {          
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_HOUSE_AD_CLICKED);
                ModuleFactory.getInstance().startLoginController(this, EVENT_CONTROLLER_GOTO_FTUE_MAIN, true, TYPE_LOGIN_FROM_DASHBOARD);
                break;
            }
            case STATE_GOTO_UPSELL:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_HOUSE_AD_CLICKED);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startUpSellController(this, null, false, userProfileProvider);
                break;
            }
            case STATE_GOTO_FAVORITE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startFavoriteController(this, TYPE_AC_FROM_DASHBOARD, userProfileProvider);
                break;
            }
            case STATE_GOTO_RECENT:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startRecentController(this, model.getInt(KEY_I_AC_TYPE), userProfileProvider);
                break;
            }
            case STATE_GOTO_DWF:
            {
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                
                try
                {
                    if(dwfAidl != null && dwfAidl.getSharingIntent() != null)
                    {
                        String sessionId = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_SESSION_ID);
                        String userKey = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_KEY);
                        String userId = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_ID);
                        String addressDt = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
                        
                        ModuleFactory.getInstance().startDriveWithFriendsController(this, userProfileProvider, sessionId, userKey, userId, addressDt, false);
                    }
                    else
                    {
                        ModuleFactory.getInstance().startDriveWithFriendsController(this, userProfileProvider, null, null, null, null, false);
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                break;
            }
            case STATE_ADD_PLACE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAddPlaceController(this, ICommonConstants.PLACE_OPERATION_TYPE_ADD, null, null, (Address) this.model.get(KEY_O_CURRENT_ADDRESS), null, userProfileProvider);
                break;
            }
            case STATE_GOTO_SHARE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAddPlaceController(this, ICommonConstants.PLACE_OPERATION_TYPE_SHARE, null, null, (Address) this.model.get(KEY_O_CURRENT_ADDRESS),  null, userProfileProvider);
                break;
            }
            case STATE_GOTO_MAP_DOWNLOAD:
            {
                ModuleFactory.getInstance().startMapDownLoadController(this);
                break;
            }
            default:
            {
                super.postStateChangeDelegate(currentState, nextState);
            }
        }
    }
    
    protected void setOrientationUnspecificed()
    {
        //override parent method here, because for Dashboard
        //we need do this later.
    }

}
