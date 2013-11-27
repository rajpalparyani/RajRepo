/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractCommonController.java
 *
 */
package com.telenav.mvc;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.module.ModuleFactory;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.ac.favorite.FavoriteController;
import com.telenav.module.ac.recent.RecentController;
import com.telenav.module.dashboard.DashboardController;
import com.telenav.module.map.MapController;
import com.telenav.module.dwf.DwfUtil;
import com.telenav.module.dwf.DriveWithFriendsController;
import com.telenav.module.dwf.DwfSliderPopup;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.nav.movingmap.MovingMapController;
import com.telenav.module.poi.search.PoiSearchController;
import com.telenav.module.preference.PreferenceController;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.sdk.maitai.IMaiTai;

/**
 * @author fqming (fqming@telenav.cn)
 * @date Jul 20, 2010
 */
public abstract class AbstractCommonController extends AbstractController
{

    public AbstractCommonController(AbstractController superController)
    {
        super(superController);
    }

    protected final void postStateChange(int currentState, int nextState)
    {
        switch (nextState)
        {
            case ICommonConstants.STATE_COMMON_GOTO_SETTINGS:
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
                Vector preferenceControllerInStack = getControllerInStack(PreferenceController.class.getName());
                if (preferenceControllerInStack.size() > 0)
                {
                    postControllerEvent(ICommonConstants.EVENT_CONTROLLER_BACK_TO_PREFERENCE, null);
                }
                else
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                            .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startPreferenceController(this, userProfileProvider);
                }
                break;
            }
            case ICommonConstants.STATE_COMMON_GOTO_FEEDBACK:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startFeedbackController(this, userProfileProvider);
                break;
            }
            case ICommonConstants.STATE_COMMON_DSR:
            {
                int acType = NavRunningStatusProvider.getInstance().isNavRunning()?ICommonConstants.TYPE_AC_FROM_NAV:
                    this.model.getInt(ICommonConstants.KEY_I_AC_TYPE);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                Address destAddr = (Address) this.model.get(ICommonConstants.KEY_O_ADDRESS_DEST);
                ModuleFactory.getInstance().startDsrController(this, acType, -1, -1, destAddr, null, userProfileProvider);
                break;
            }
            case ICommonConstants.STATE_COMMON_GOTO_ONEBOX:
            {
                if (this instanceof DashboardController)
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_ONE_BOX_SEARCH);
                }
                else if (this instanceof PoiSearchController)
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NEARBY,
                        KontagentLogger.NEARBY_ONE_BOX_SEARCH);
                }
                int acType = model.getInt(ICommonConstants.KEY_I_AC_TYPE);
                int searchType = model.getInt(ICommonConstants.KEY_I_SEARCH_TYPE);
                int alongRouteType = model.getInt(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE);
                boolean isChangeLocation = model.getBool(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION);
                boolean needCurrentLocation = model.getBool(ICommonConstants.KEY_B_NEED_CURRENT_LOCATION);
                String initText = model.getString(ICommonConstants.KEY_S_COMMON_SEARCH_TEXT);
                Address anchorAddr = (Address) model.get(ICommonConstants.KEY_O_ADDRESS_ORI);
                Address destAddr = (Address) model.get(ICommonConstants.KEY_O_ADDRESS_DEST);
                NavState navState = (NavState) model.get(ICommonConstants.KEY_O_NAVSTATE);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                if (userProfileProvider == null)
                {
                    userProfileProvider = new UserProfileProvider();
                }
                boolean isSearchNearBy = model.getBool(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY);
                ModuleFactory.getInstance().startOneBoxController(this, acType, searchType, alongRouteType, initText, false,
                    isChangeLocation, anchorAddr, destAddr, navState, userProfileProvider, isSearchNearBy, needCurrentLocation, false);
                break;
            }
            case ICommonConstants.STATE_COMMON_SHARE_ADDRESS:
            {
                Address address = (Address) model.get(ICommonConstants.KEY_O_SHARED_ADDRESS);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startNativeShareController(this, address, userProfileProvider);
                break;
            }
            case ICommonConstants.STATE_COMMON_BACK_TO_MOVING_MAP:
            {
                postControllerEvent(ICommonConstants.EVENT_CONTROLLER_BACK_TO_MOVING_MAP, null);
                break;
            }
            case ICommonConstants.STATE_COMMON_BACK_TO_ENTRY:
            {
                postControllerEvent(ICommonConstants.EVENT_CONTROLLER_BACK_TO_ENTRY, null);
                break;
            }
            case ICommonConstants.STATE_COMMON_DSR_MAP:
            {
                int type = model.fetchInt(ICommonConstants.KEY_I_AC_TYPE);
                Address address = (Address) model.fetch(ICommonConstants.KEY_O_SELECTED_ADDRESS);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, type, address, null, userProfileProvider);
                break;
            }
            case ICommonConstants.STATE_COMMON_DSR_NAV:
            {
                Address address = (Address) model.fetch(ICommonConstants.KEY_O_SELECTED_ADDRESS);
                ModuleFactory.getInstance().startNavController(this, null, address, null, true,
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), false, false);
                break;
            }
            case ICommonConstants.STATE_COMMON_DSR_SEARCH:
            {
                int acType = model.fetchInt(ICommonConstants.KEY_I_AC_TYPE);
                int searchType = model.getInt(ICommonConstants.KEY_I_SEARCH_TYPE);
                int alongRouteType = model.getInt(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE);
                String initText = model.getString(ICommonConstants.KEY_S_COMMON_SEARCH_TEXT);
                Address anchorAddr = (Address) model.fetch(ICommonConstants.KEY_O_SELECTED_ADDRESS);
                Address destAddr = (Address) model.get(ICommonConstants.KEY_O_ADDRESS_DEST);

                NavState navState = null;
                if (NavSdkNavEngine.getInstance().isRunning())
                {
                    navState = NavSdkNavEngine.getInstance().getCurrentNavState();
                }

                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                if (userProfileProvider == null)
                {
                    userProfileProvider = new UserProfileProvider();
                }
                boolean isSearchNearBy = model.getBool(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY);
                boolean needCurrentLocation = model.getBool(ICommonConstants.KEY_B_NEED_CURRENT_LOCATION);

                ModuleFactory.getInstance().startOneBoxController(this, acType, searchType, alongRouteType, initText, true,
                    false, anchorAddr, destAddr, navState, userProfileProvider, isSearchNearBy, needCurrentLocation, false);
                break;
            }
            case ICommonConstants.STATE_COMMON_LINK_TO_MAP:
            {
                Parameter data = new Parameter();
                data.put(ICommonConstants.KEY_I_AC_TYPE, model.getInt(ICommonConstants.KEY_I_AC_TYPE));
                postControllerEvent(ICommonConstants.EVENT_CONTROLLER_LINK_TO_MAP, data);
                break;
            }
            case ICommonConstants.STATE_COMMON_GOTO_HOME:
            {
                Parameter data = new Parameter();
                data.put(ICommonConstants.KEY_I_AC_TYPE, model.getInt(ICommonConstants.KEY_I_AC_TYPE));
                postControllerEvent(HomeScreenManager.getHomeScreenEventID(), data);
                break;
            }
            case ICommonConstants.STATE_COMMON_LINK_TO_AC:
            {
                postControllerEvent(ICommonConstants.EVENT_CONTROLLER_LINK_TO_AC, null);
                break;
            }
            case ICommonConstants.STATE_COMMON_LINK_TO_SEARCH:
            {
                Address centerAnchor = (Address) model.get(ICommonConstants.KEY_O_SELECTED_ADDRESS);
                Parameter parameter = new Parameter();
                parameter.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, centerAnchor);
                this.postControllerEvent(ICommonConstants.EVENT_CONTROLLER_LINK_TO_POI, parameter);
                break;
            }
            case ICommonConstants.STATE_COMMON_LINK_TO_EXTRA:
            {
                this.postControllerEvent(ICommonConstants.EVENT_CONTROLLER_LINK_TO_EXTRA, null);
                break;
            }
            case ICommonConstants.STATE_COMMON_GOTO_ABOUT:
            {
                ModuleFactory.getInstance().startAboutController(this);
                break;
            }
            case ICommonConstants.STATE_COMMON_EXIT_NAV_FROM_PLUGIN:
            {
                Object pluginRequest = model.get(ICommonConstants.KEY_O_PLUGIN_REQUEST);
                AbstractController controller = AbstractController.getCurrentController();
                controller.handleModelEvent(ITriggerConstants.EVENT_MODEL_RELEASE_ALL_PREVIOUS_MODULES);
                controller.release();
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                MediaManager.getInstance().getAudioPlayer().cancelAll();
                if (pluginRequest instanceof Hashtable)
                {
                    ModuleFactory.getInstance().startMain(ICommonConstants.EVENT_CONTROLLER_GOTO_PLUGIN,
                        (Hashtable) pluginRequest);
                }
                else
                {
                    ModuleFactory.getInstance().startMain();
                }
                break;
            }
            case ICommonConstants.STATE_COMMON_DWF_FROM_PLUGIN:
            {
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

                if (dwfAidl != null)
                {
                    try
                    {
                        dwfAidl.updateStatus(-1, FriendStatus.END.name(), 0, 0);
                        dwfAidl.stopShareLocation();
                        DwfSliderPopup.getInstance().hide();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) this.model
                        .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                if (userProfileProvider == null)
                {
                    userProfileProvider = new UserProfileProvider();
                }

                Hashtable pluginRequest = (Hashtable) model.get(ICommonConstants.KEY_O_PLUGIN_REQUEST);
                if (AbstractController.getCurrentController() instanceof DriveWithFriendsController)
                {
                    Parameter para = new Parameter();
                    para.put(ICommonConstants.KEY_O_PLUGIN_REQUEST, pluginRequest);
                    AbstractController.getCurrentController().activate(ICommonConstants.EVENT_CONTROLLER_FROM_PLUGIN_REQUEST,
                        para);
                }
                else
                {
                    String sessionId = (String) pluginRequest.get(IMaiTai.KEY_DWF_SESSION_ID);
                    String userKey = (String) pluginRequest.get(IMaiTai.KEY_DWF_USER_KEY);
                    String userId = (String) pluginRequest.get(IMaiTai.KEY_DWF_USER_ID);
                    String addressDt = (String) pluginRequest.get(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
                    boolean isExitDirectly = false;
                    if(pluginRequest.containsKey(IMaiTai.KEY_DWF_EXIT_APP))
                    {
                        isExitDirectly = (Boolean) pluginRequest.get(IMaiTai.KEY_DWF_EXIT_APP);
                    }
                    
                    
                    ModuleFactory.getInstance().startDriveWithFriendsController(this, userProfileProvider, sessionId, userKey,
                        userId, addressDt, isExitDirectly);
                }
                break;
            }
            case ICommonConstants.STATE_COMMON_EXIT_NAV_FROM_SHORTCUT:
            {
                releaseAll();
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                MediaManager.getInstance().getAudioPlayer().cancelAll();
                ModuleFactory.getInstance().startMain();
                break;
            }
            case ICommonConstants.STATE_LAUNCH_SECRET_FUNCTIONS:
            {
                ModuleFactory.getInstance().startSecretKeyController(this);
                break;
            }
            case ICommonConstants.STATE_GO_TO_SIGN_IN:
            {
                int fromType = -1;// default
                if (this instanceof RecentController)
                {
                    fromType = ICommonConstants.TYPE_LOGIN_FROM_RECENT;
                }
                else if (this instanceof FavoriteController)
                {
                    fromType = ICommonConstants.TYPE_LOGIN_FROM_FAVORITE;
                }
                ModuleFactory.getInstance().startLoginController(this, ICommonConstants.EVENT_CONTROLLER_GOTO_FTUE_SIGNIN,
                    true, fromType);
                break;
            }
            case ICommonConstants.STATE_GO_TO_SIGN_UP:
            {
                int fromType = -1;// default
                if (this instanceof RecentController)
                {
                    fromType = ICommonConstants.TYPE_LOGIN_FROM_RECENT;
                }
                else if (this instanceof FavoriteController)
                {
                    fromType = ICommonConstants.TYPE_LOGIN_FROM_FAVORITE;
                }
                ModuleFactory.getInstance().startLoginController(this, ICommonConstants.EVENT_CONTROLLER_GOTO_FTUE_SIGNUP,
                    true, fromType);
                break;
            }
            case ICommonConstants.STATE_EXIT_NAV:
            {
                if(this instanceof MovingMapController)
                {
                    handleControllerEvent(ICommonConstants.EVENT_CONTROLLER_EXIT_NAV, null);
                }
                else
                {
                    postControllerEvent(ICommonConstants.EVENT_CONTROLLER_EXIT_NAV, null);
                }
                break;
            }
            case ICommonConstants.STATE_END_DETOUR:
            {
                postControllerEvent(ICommonConstants.EVENT_CONTROLLER_RESUME_TRIP, null);
                break;
            }
            case ICommonConstants.STATE_EXIT_APPLICATION:
            {
                releaseAll();
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                TeleNavDelegate.getInstance().exitApp();
				break;
            }
            case ICommonConstants.STATE_GOTO_CONTACTS:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startContactsController(this, model.getInt(ICommonConstants.KEY_I_AC_TYPE), userProfileProvider);
                break;
            }
            default:
            {
                postStateChangeDelegate(currentState, nextState);
                break;
            }
        }

    }

    protected abstract void postStateChangeDelegate(int currentState, int nextState);

    public Vector getControllerInStack(String className)
    {
        Vector matches = new Vector();
        AbstractController controller = superController;
        while (controller != null)
        {
            if (controller.getClass().getName().equalsIgnoreCase(className))
            {
                matches.addElement(controller);
            }

            controller = controller.getSuperController();
        }

        return matches;
    }

    public String getRegion()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) this.model
                .get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
        MandatoryProfile profile;
        if (userProfileProvider != null)
        {
            profile = userProfileProvider.getMandatoryNode();
        }
        else
        {
            MandatoryNodeDao mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao();
            profile = mandatoryNode.getMandatoryNode();
        }
        return profile.getUserInfo().region;
    }
}
