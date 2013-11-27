/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MovingMapController.java
 *
 */
package com.telenav.module.nav.movingmap;

import android.os.RemoteException;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.module.ModuleFactory;
import com.telenav.module.browsersdk.IBrowserSdkConstants;
import com.telenav.module.dashboard.DashboardManager;
import com.telenav.module.nav.ITrafficCallback;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.maitai.IMaiTai;

/**
 * @author yning (yning@telenav.cn)
 * @date 2010-11-5
 */
public class MovingMapController extends AbstractCommonController implements IMovingMapConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {
            // normal start
            { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_MOVING_MAP, ACTION_INIT_MAP },
            { STATE_ANY, EVENT_MODEL_DRIVE_TO_AD, STATE_DRIVING_TO_AD, ACTION_GET_AD_ROUTE },
            { STATE_ANY, EVENT_CONTROLLER_DRIVE_TO_VBB, STATE_DRIVING_TO_AD, ACTION_GET_AD_ROUTE },
            { STATE_ANY, EVENT_CONTROLLER_BACK_FROM_VBB_DETAIL, STATE_MOVING_MAP, ACTION_BACK_FROM_VBB_DETAIL },
            { STATE_ANY, EVENT_MODEL_EXIT_NAV, STATE_START_MAIN, ACTION_STOP_NAVIGATION },
            { STATE_ANY, EVENT_MODEL_EXIT_AND_PURCHASE, STATE_START_MAIN_GO_PURCHASE, ACTION_STOP_NAVIGATION },

            // -------------------------------------------------------------
            // model events
            // -------------------------------------------------------------
            // Location data retrieved, start requesting route.
            { STATE_ANY, EVENT_MODEL_LOCATION_DATA_RETRIEVED, STATE_MOVING_MAP, ACTION_GET_ROUTE },
            // location data can't be retrieved, back to main.
            { STATE_MOVING_MAP, EVENT_MODEL_LOCATION_DATA_FAILED, STATE_START_MAIN, ACTION_STOP_NAVIGATION },
            // to update screen.
            { STATE_MOVING_MAP, EVENT_MODEL_UPDATE_VIEW, STATE_MOVING_MAP, ACTION_NONE },
            // show turn map screen. For screen jumping.
            { STATE_ANY, EVENT_MODEL_SHOW_NAV_MAP, STATE_MOVING_MAP, ACTION_NONE },
            // show end trip screen.
            { STATE_MOVING_MAP, EVENT_MODEL_SHOW_END_TRIP, STATE_END_TRIP_MODE, ACTION_SHOW_END_TRIP },
            // goto dwf status list.
            { STATE_END_TRIP_MODE, EVENT_MODEL_GOTO_DWF, STATE_GOTO_DWF, ACTION_NONE },

            // show AD detail
            { STATE_MOVING_MAP, EVENT_MODEL_AD_DETAIL, STATE_AD_DETAIL, ACTION_NONE },
            { STATE_END_TRIP_MODE, EVENT_MODEL_AD_DETAIL, STATE_AD_DETAIL, ACTION_NONE },

            // handle ssoToken expired cases
            { STATE_ANY, EVENT_MODEL_INVALID_IDENTITY, STATE_SYNC_PURCHASE, ACTION_SYNC_PURCHASE },
            { STATE_SYNC_PURCHASE, EVENT_MODEL_CONTINUE_GET_ROUTE, STATE_MOVING_MAP, ACTION_CONTINUE_GET_ROUTE },

            { STATE_ANY, EVENT_CONTROLLER_BACK_TO_MOVING_MAP, STATE_MOVING_MAP, ACTION_NONE },
            { STATE_ANY, EVENT_MODEL_BACK_TO_MOVING_MAP, STATE_MOVING_MAP, ACTION_NONE },
            { STATE_ANY, EVENT_MODEL_REQUEST_DEVIATION, STATE_REQUESTING_DEVIATION, ACTION_NONE },
            { STATE_REQUESTING_DEVIATION, CMD_COMMON_BACK, STATE_REQUESTING_DEVIATION, ACTION_NONE },
            { STATE_REQUESTING_DEVIATION, EVENT_MODEL_POST_ERROR, STATE_DEVIATION_FAIL, ACTION_NONE },
            { STATE_DEVIATION_FAIL, CMD_COMMON_OK, STATE_START_MAIN, ACTION_STOP_NAVIGATION },
            { STATE_DEVIATION_FAIL, CMD_COMMON_BACK, STATE_START_MAIN, ACTION_STOP_NAVIGATION },

            // -------------------------------------------------------------
            // commands and buttons
            // -------------------------------------------------------------
            { STATE_MOVING_MAP, CMD_COMMON_LINK_TO_SEARCH, STATE_GOTO_POI, ACTION_NONE },
            { STATE_MOVING_MAP, CMD_COMMON_DSR, STATE_GOTO_DSR, ACTION_NONE },
            { STATE_END_TRIP_MODE, CMD_COMMON_LINK_TO_SEARCH, STATE_END_TRIP_MODE, ACTION_NONE },
            { STATE_END_TRIP_MODE, CMD_COMMON_DSR, STATE_END_TRIP_MODE, ACTION_NONE },
            { STATE_END_TRIP_MODE, CMD_COMMON_BACK, STATE_END_TRIP_MODE, ACTION_END_TRIP_CHECK },
            { STATE_END_TRIP_MODE, EVENT_MODEL_DETOUR_CONFIRM, STATE_DETOUR_CONFIRM, ACTION_NONE },
            { STATE_DETOUR_CONFIRM, CMD_COMMON_BACK, STATE_START_MAIN, ACTION_STOP_NAVIGATION },
            { STATE_DETOUR_CONFIRM, CMD_RESUME_TRIP, STATE_RESUME_TRIP, ACTION_RESUME_TRIP },

            { STATE_MOVING_MAP, CMD_ROUTE_SUMMARY, STATE_GOTO_ROUTE_SUMMARY, ACTION_NONE },
            { STATE_MOVING_MAP, CMD_TRAFFIC_SUMMARY, STATE_GOTO_TRAFFIC_SUMMARY, ACTION_NONE },
            { STATE_TRAFFIC_ALERT_DETAIL, CMD_TRAFFIC_SUMMARY, STATE_GOTO_TRAFFIC_SUMMARY, ACTION_NONE },
            { STATE_MOVING_MAP, CMD_MAP_SUMMARY, STATE_GOTO_MAP_SUMMARY, ACTION_NONE },
            { STATE_MOVING_MAP, CMD_COMMON_BACK, STATE_CHECKING_BACK, ACTION_CHECK_BACK },
            { STATE_CHECKING_BACK, EVENT_MODEL_COMMON_BACK, STATE_MOVING_MAP, ACTION_NONE },
            { STATE_CHECKING_BACK, EVENT_MODEL_EXIT_CONFIRM, STATE_EXIT_CONFIRM, ACTION_NONE },

            { STATE_MOVING_MAP, CMD_SHOW_INCIDENT_DETAIL, STATE_TRAFFIC_ALERT_DETAIL, ACTION_NONE },
            // click traffic alert message box at right bottom.
            { STATE_TRAFFIC_ALERT_DETAIL, CMD_AVOID, STATE_AVOID_INCIDENT, ACTION_AVOID_INCIDENT },
            { STATE_AVOID_INCIDENT, CMD_COMMON_BACK, STATE_PREV, ACTION_CANCEL_AVOID_INCIDENT },
            { STATE_EXIT_CONFIRM, CMD_COMMON_BACK, STATE_PREV, ACTION_NONE },
            { STATE_EXIT_CONFIRM, CMD_END_TRIP, STATE_START_MAIN, ACTION_CHECK_TRIP },
            { STATE_EXIT_CONFIRM, CMD_RESUME_TRIP, STATE_RESUME_TRIP, ACTION_RESUME_TRIP },
            { STATE_END_TRIP_MODE, CMD_ADD_TO_FAVORITE, STATE_END_TRIP_MODE, ACTION_NONE },
            { STATE_MOVING_MAP, CMD_REPORT_SPEED_TRAP, STATE_MOVING_MAP, ACTION_REPORT_SPEED_TRAP },
            { STATE_MOVING_MAP, CMD_PLAY_AUDIO, STATE_MOVING_MAP, ACTION_PLAY_AUDIO },
            { STATE_MOVING_MAP, CMD_END_TRIP, STATE_EXIT_CONFIRM, ACTION_NONE },
            { STATE_MOVING_MAP, CMD_GO_TO_ROUTE_SETTING, STATE_GO_TO_ROUTE_SETTING, ACTION_NONE },
            { STATE_END_TRIP_MODE, CMD_END_TRIP, STATE_END_TRIP_MODE, ACTION_END_TRIP_CHECK },
            // -------------------------------------------------------------
            // controller events
            // -------------------------------------------------------------
            { STATE_ANY, EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, STATE_GOTO_ROUTE_SUMMARY, ACTION_NONE },
            { STATE_ANY, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, STATE_GOTO_MAP_SUMMARY, ACTION_NONE },
            { STATE_ANY, EVENT_CONTROLLER_GOTO_POI, STATE_GOTO_POI, ACTION_NONE },
            { STATE_ANY, EVENT_CONTROLLER_GO_TO_MOVING_MAP, STATE_PREV, ACTION_NONE },
            { STATE_ANY, EVENT_CONTROLLER_EXIT_NAV, STATE_START_MAIN, ACTION_STOP_NAVIGATION },
            { STATE_ANY, EVENT_CONTROLLER_RESUME_TRIP, STATE_RESUME_TRIP, ACTION_RESUME_TRIP },
            { STATE_ANY, EVENT_CONTROLLER_PREFERENCE_SAVED, STATE_ROUTE_SETTING_CHECKING, ACTION_ROUTE_SETTING_CHECKING },
            { STATE_ROUTE_SETTING_CHECKING, EVENT_MODEL_ROUTE_SETTING_CHANGED, STATE_UPDATE_ROUTE, ACTION_NONE },
            { STATE_ROUTE_SETTING_CHECKING, EVENT_MODEL_ROUTE_SETTING_UNCHANGED, STATE_PREV, ACTION_NONE },

            // from suggest route
            { STATE_ANY, EVENT_CONTROLLER_RENEW_DYNAMIC_ROUTE, STATE_MOVING_MAP, ACTION_RENEW_DYNAMIC_ROUTE },

            // -------------------------------------------------------------
            // error handling
            // -------------------------------------------------------------
            { STATE_ANY, EVENT_MODEL_RTS_FAILED, STATE_RTS_FAILED, ACTION_NONE },
            { STATE_RTS_FAILED, CMD_COMMON_BACK, STATE_START_MAIN, ACTION_STOP_NAVIGATION },

            { STATE_ANY, EVENT_MODEL_ROUTING_ERROR, STATE_ROUTING_ERROR, ACTION_NONE },
            { STATE_ROUTING_ERROR, CMD_COMMON_OK, STATE_CHECK_DETOUR_ERROR, ACTION_CHECK_DETOUR },
            { STATE_CHECK_DETOUR_ERROR, EVENT_MODEL_GOTO_NON_DETOUR, STATE_START_MAIN, ACTION_STOP_NAVIGATION },
            { STATE_CHECK_DETOUR_ERROR, EVENT_MODEL_GOTO_DETOUR, STATE_RESUME_TRIP, ACTION_RESUME_TRIP },

            { STATE_ANY, EVENT_MODEL_ACCOUNT_ERROR, STATE_ACCOUNT_ERROR, ACTION_NONE },
            { STATE_ACCOUNT_ERROR, CMD_COMMON_OK, STATE_START_MAIN, ACTION_STOP_NAVIGATION },

            { STATE_ANY, EVENT_MODEL_ACCOUNT_ERROR_FATAL, STATE_ACCOUNT_ERROR_FATAL, ACTION_NONE },
            { STATE_ACCOUNT_ERROR_FATAL, CMD_COMMON_OK, STATE_PREV, ACTION_ACCOUNT_FATAL },

            // common network related error.
            { STATE_COMMON_ERROR, CMD_COMMON_OK, STATE_COMMON_ERROR, ACTION_CHECKING_ERROR_CONTEXT },
            { STATE_COMMON_ERROR, CMD_COMMON_BACK, STATE_COMMON_ERROR, ACTION_CHECKING_ERROR_CONTEXT },
            { STATE_COMMON_ERROR, EVENT_MODEL_BACK_TO_PRE, STATE_PREV, ACTION_NONE },

            // home
            { STATE_ANY, CMD_COMMON_HOME, STATE_COMMON_GOTO_HOME, ACTION_STOP_NAVIGATION },

            { STATE_ANY, EVENT_CONTROLLER_GO_TO_NAV, STATE_PREV, ACTION_NONE },

            { STATE_ANY, CMD_COMMON_EXIT, STATE_COMMON_EXIT, ACTION_NONE },

            { STATE_COMMON_EXIT, CMD_END_TRIP, STATE_EXIT_APPLICATION, ACTION_LOG_EXIT },

            { STATE_COMMON_EXIT, CMD_RESUME_TRIP, STATE_RESUME_TRIP, ACTION_RESUME_TRIP },

            // { STATE_ANY, CMD_FEEDBACK, STATE_FEEDBACK, ACTION_NONE},

            { STATE_GENERAL_FEEDBACK, IBrowserSdkConstants.EVENT_MODEL_EXIT_BROWSER, STATE_PREV, ACTION_EXIT_BROWER },
            { STATE_ANY, IBrowserSdkConstants.EVENT_MODEL_EXIT_BROWSER, STATE_ANY, ACTION_EXIT_BROWER },

    };

    public MovingMapController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_COMMON_GOTO_HOME:
            {
                releaseAll();
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                ModuleFactory.getInstance().startMain();
                break;
            }
            case STATE_START_MAIN:
            {
                releaseAll();
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                ModuleFactory.getInstance().startMain();

                // JY: This case is for resumetrip's eta on dashboard which needs to update proactively.
                DashboardManager.getInstance().notifyUpdateEta();
                break;
            }
            case STATE_START_MAIN_GO_PURCHASE:
            {
                releaseAll();
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMainAndGoPurchaseNav(userProfileProvider);
                break;
            }
            case STATE_GOTO_DSR:
            {
                int acType = TYPE_AC_FROM_NAV;
                Address destAddr = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                NavState navState = NavSdkNavEngine.getInstance().getCurrentNavState();
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startDsrController(this, acType, -1, -1, destAddr, navState, userProfileProvider);
                break;
            }
            case STATE_GOTO_ROUTE_SUMMARY:
            {
                ModuleFactory.getInstance().startSummaryControlController(this, EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY,
                    (Address) model.get(KEY_O_ADDRESS_ORI), (Address) model.get(KEY_O_ADDRESS_DEST), (ITrafficCallback) model,
                    0, model.getBool(KEY_B_IS_FROM_SEARCH_ALONG), model.getInt(KEY_I_AC_TYPE));
                break;
            }
            case STATE_GOTO_TRAFFIC_SUMMARY:
            {
                ModuleFactory.getInstance().startSummaryControlController(this, EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY,
                    (Address) model.get(KEY_O_ADDRESS_ORI), (Address) model.get(KEY_O_ADDRESS_DEST), (ITrafficCallback) model,
                    0, model.getBool(KEY_B_IS_FROM_SEARCH_ALONG), model.getInt(KEY_I_AC_TYPE));
                break;
            }
            case STATE_GOTO_MAP_SUMMARY:
            {
                ModuleFactory.getInstance().startSummaryControlController(this, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY,
                    (Address) model.get(KEY_O_ADDRESS_ORI), (Address) model.get(KEY_O_ADDRESS_DEST), (ITrafficCallback) model,
                    0, model.getBool(KEY_B_IS_FROM_SEARCH_ALONG), model.getInt(KEY_I_AC_TYPE));
                break;
            }
            case STATE_GOTO_POI:
            {
                Address dest = (Address) model.get(KEY_O_ADDRESS_DEST);
                NavState navState = null;

                if (NavSdkNavEngine.getInstance().isRunning())
                {
                    navState = NavSdkNavEngine.getInstance().getCurrentNavState();
                }

                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);

                ModuleFactory.getInstance().startCategorySearchController(this, TYPE_AC_FROM_NAV,
                    IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE, -1, -1, -1, null, dest, navState, userProfileProvider, false);
                break;
            }
            case STATE_EXIT_APPLICATION:
            {
                releaseAll();
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                TeleNavDelegate.getInstance().exitApp();
                break;
            }
            case STATE_RESUME_TRIP:
            {
                Address dest = (Address) model.get(KEY_O_ADDRESS_DEST);
                Parameter param = new Parameter();
                param.put(KEY_O_ADDRESS_DEST, dest);
                ModuleFactory.getInstance().startNavController(this, null, dest,
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), false, true, true);
                break;
            }
            case STATE_GO_TO_ROUTE_SETTING:
            {
                boolean isOnboard = NavRunningStatusProvider.getInstance().isOnBoardRoute()
                        || !NetworkStatusManager.getInstance().isConnected();;
                ModuleFactory.getInstance().startRouteSettingController(this, EVENT_CONTROLLER_GO_TO_ROUTE_SETTING, true, isOnboard);
                break;
            }
            case STATE_UPDATE_ROUTE:
            {
                Address destAddr = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                boolean isFromSearchAlong = model.getBool(KEY_B_IS_FROM_SEARCH_ALONG);
                ModuleFactory.getInstance().startNavController(this, null, destAddr,
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), isFromSearchAlong, false);
                break;
            }
            case STATE_AD_DETAIL:
            {
                String adUrl = model.getString(KEY_S_AD_DETAIL_URL);
                Address destAddr = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                ModuleFactory.getInstance().startVbbDetailController(this, adUrl, destAddr);
                break;
            }
            case STATE_GOTO_DWF:
            {
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
                
                try
                {
                    String sessionId = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_SESSION_ID);
                    String userKey = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_KEY);
                    String userId = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_ID);
                    String addressDt = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
                    ModuleFactory.getInstance().startDriveWithFriendsController(this, (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER), 
                        sessionId, userKey, userId, addressDt, false);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                break;
            }

        }
    }

    protected AbstractModel createModel()
    {
        return new MovingMapModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new MovingMapViewTouch(new MovingMapUiDecorator());
    }

}
