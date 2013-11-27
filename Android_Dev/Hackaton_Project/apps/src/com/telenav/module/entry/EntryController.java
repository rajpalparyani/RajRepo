/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * EntryController.java
 *
 */
package com.telenav.module.entry;

import java.util.Hashtable;

import android.os.RemoteException;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.ShortcutManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.NavExitAbnormalDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.logger.Logger;
import com.telenav.module.ModuleFactory;
import com.telenav.module.dwf.DwfSliderPopup;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.HomeScreenManager;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.plugin.PluginManager;

/**
 * @author fqming (fqming@telenav.cn)
 * @date Jul 21, 2010
 */
public class EntryController extends AbstractCommonEntryController implements IEntryConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        //start splash job and init madantory node.
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
        { STATE_VIRGIN, EVENT_CONTROLLER_GO_TO_MAIN, STATE_GOTO_MAIN, ACTION_NONE },
        { STATE_VIRGIN, EVENT_CONTROLLER_GO_TO_MAIN_PURCHASE_NAV, STATE_GOTO_MAIN, ACTION_GO_TO_PURCHASE_NAV },
    
        { STATE_ANY, EVENT_MODEL_GOTO_LOCATION_SETTING, STATE_GOTO_LOCATION_SETTING, ACTION_NONE },
        { STATE_GOTO_LOCATION_SETTING, CMD_COMMON_OK, STATE_GOTO_LOCATION_SETTING_OK, ACTION_LOCATION_SETTING_OK },
        { STATE_GOTO_LOCATION_SETTING, CMD_COMMON_BACK, STATE_GOTO_LOCATION_SETTING_CANCEL, ACTION_START_FLOW },
        { STATE_GOTO_LOCATION_SETTING, CMD_COMMON_CANCEL, STATE_GOTO_LOCATION_SETTING_CANCEL, ACTION_START_FLOW },
          
        // re-sync resource(airport,cache city, fav/recent)
        { STATE_ANY, EVENT_MODEL_DATASET_SWITCH_RESYNC, STATE_DATASET_SWITCH_RESYNC, ACTION_DATASET_SWITCH_RESYNC },
        { STATE_DATASET_SWITCH_RESYNC, CMD_COMMON_BACK, STATE_DATASET_SWITCH_RESYNC, ACTION_NONE },
        
        // show the main screen while account & service locator is ready.
        { STATE_ANY, EVENT_MODEL_CHECK_UPGRADE, STATE_CHECK_UPGRADE, ACTION_CHECK_UPGRADE },
        { STATE_CHECK_UPGRADE, EVENT_MODEL_LAUNCH_MAIN, STATE_GOTO_MAIN, ACTION_BG_STARTUP },
        { STATE_CHECK_UPGRADE, EVENT_MODEL_GOTO_UPGRADE, STATE_UPGRADE_SELECTION, ACTION_NONE },
        { STATE_UPGRADE_SELECTION, CMD_UPGRADE_NOW, STATE_UPGRADE_SELECTION, ACTION_GOTO_OTA },
        { STATE_UPGRADE_SELECTION, CMD_REMIND_LATER, STATE_GOTO_MAIN, ACTION_BG_STARTUP },
        { STATE_UPGRADE_SELECTION, CMD_DONOT_ASK, STATE_GOTO_MAIN, ACTION_DONOT_ASK_FOR_UPGRADE },
        
        // show sync service locator & sdp while haven't got from server at the moment splash done.
        { STATE_ANY, EVENT_MODEL_SYNC_SERVICELOCATOR_SDP, STATE_SYNC_SERVICELOCATOR_SDP, ACTION_NONE },
        
        // go to login while ptn has been got at the moment splash done.
        // or the ptn has been got, just account is not available.
        { STATE_ANY, EVENT_MODEL_LOGIN, STATE_GOTO_LOGIN, ACTION_NONE },
        
        //secret key screen.
        { STATE_ANY, CMD_SECRET_KEY, STATE_LAUNCH_SECRET_FUNCTIONS, ACTION_NONE },
        
        //service locator & sdp return.
        { STATE_SYNC_SERVICELOCATOR_SDP, EVENT_MODEL_SYNC_SERVICELOCATOR_SDP_SUCC, STATE_SYNC_SERVICELOCATOR_SDP, ACTION_START_FLOW },
        { STATE_SYNC_SERVICELOCATOR_SDP, EVENT_MODEL_SYNC_SERVICELOCATOR_SDP_FAIL, STATE_SYNC_SERVICELOCATOR_SDP_ERROR_MSG, ACTION_NONE },
        
        //sync purchase
        { STATE_SYNC_PURCHASE, EVENT_MODEL_SYNC_PURCHASE_FAIL, STATE_SYNC_PURCHASE_FAIL_MSG, ACTION_NONE },
        
        //disable cancel event.
        { STATE_SYNC_SERVICELOCATOR_SDP, CMD_COMMON_BACK, STATE_SYNC_SERVICELOCATOR_SDP, ACTION_NONE },
        { STATE_SYNC_SERVICELOCATOR_SDP, CMD_COMMON_CANCEL, STATE_SYNC_SERVICELOCATOR_SDP, ACTION_NONE },
        
        //exit app after show get ptn error msg.
        { STATE_SYNC_SERVICELOCATOR_SDP_ERROR_MSG, CMD_COMMON_BACK, STATE_EXIT_APP, ACTION_EXIT_APP },
        { STATE_SYNC_SERVICELOCATOR_SDP_ERROR_MSG, CMD_COMMON_OK, STATE_EXIT_APP, ACTION_EXIT_APP },
        { STATE_SYNC_SERVICELOCATOR_SDP_ERROR_MSG, CMD_COMMON_CANCEL, STATE_EXIT_APP, ACTION_EXIT_APP },
        
        //exit app after show sync purchase error msg.
        { STATE_SYNC_PURCHASE_FAIL_MSG, CMD_COMMON_BACK, STATE_EXIT_APP, ACTION_EXIT_APP },
        { STATE_SYNC_PURCHASE_FAIL_MSG, CMD_COMMON_OK, STATE_EXIT_APP, ACTION_EXIT_APP },
        { STATE_SYNC_PURCHASE_FAIL_MSG, CMD_COMMON_CANCEL, STATE_EXIT_APP, ACTION_EXIT_APP },
         
        //back to Entry
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_ENTRY, STATE_SYNC_PURCHASE, ACTION_NONE },
        
        //login process finish, check the status.
        { STATE_GOTO_LOGIN, EVENT_CONTROLLER_LOGIN_FINISH, STATE_CHECK_ACCOUNT_STATUS, ACTION_CHECK_ACCOUNT_STATUS },
        
        //if login not finish, exit app.
        { STATE_CHECK_ACCOUNT_STATUS, EVENT_MODEL_LOGIN_FAIL, STATE_EXIT_APP, ACTION_NONE },
        
        //for the case that sync res process is interruptted.
        { STATE_ANY, EVENT_MODEL_SYNC_RES, STATE_GOTO_SYNC_RES, ACTION_NONE },
        
        //sync res finish.
        { STATE_GOTO_SYNC_RES, EVENT_CONTROLLER_SYNC_FINISH, STATE_UPLOAD_PREFERENCE, ACTION_UPLOAD_LOGIN_INFO },
        
        { STATE_ANY, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_CHECK_AC_TYPE, ACTION_NONE },
        
        //for link button
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_MAP, STATE_DO_LINK, ACTION_LINK_TO_MAP },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_AC, STATE_DO_LINK, ACTION_LINK_TO_AC },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_POI, STATE_DO_LINK, ACTION_LINK_TO_POI },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_EXTRA, STATE_DO_LINK, ACTION_LINK_TO_EXTRA },
        
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_MAP, STATE_LINK_TO_MAP, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_AC, STATE_LINK_TO_AC, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_POI, STATE_LINK_TO_POI, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_EXTRA, STATE_LINK_TO_EXTRA, ACTION_NONE },
        
        { STATE_ANY, EVENT_CONTROLLER_GOTO_DASHBOARD_FROM_WIDGET_HOMEWORK, STATE_GOTO_MAIN, ACTION_NONE },
        
        //plugin
        { STATE_ANY, EVENT_CONTROLLER_GOTO_PLUGIN, STATE_START_PLUGIN, ACTION_NONE },
        
        //upsell
        { STATE_ANY, EVENT_MODEL_GOTO_PURCHASE_NAV, STATE_GOTO_PURCHASE_NAV, ACTION_NONE },
        
        //purchase
        { STATE_ANY, EVENT_MODEL_GO_PURCHASE, STATE_UP_SELL, ACTION_NONE }, 
        { STATE_UP_SELL, EVENT_CONTROLLER_UPSELL_PURCHASE_FINISH, STATE_PURCHASE_FINISH, ACTION_SYNC_RES_AND_CHECK_UPGRADE }, 
        { STATE_ANY, EVENT_MODEL_SYNC_RES_AND_CHECK_UPGRADE, STATE_SYNC_RES_AND_CHECK_UPGRADE, ACTION_SYNC_RES_AND_CHECK_UPGRADE },
        
        
    };

    public EntryController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_VIRGIN:
            {
                TeleNavDelegate.getInstance().exitApp();
                break;
            }
            case STATE_LAUNCH_SECRET_FUNCTIONS:
            {
                model.put(KEY_B_IS_SECRET_STARTED, true);
                ModuleFactory.getInstance().startSecretKeyController(this);
                break;
            }
            case STATE_GOTO_PURCHASE_NAV:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startUpSellController(this, FeaturesManager.FEATURE_CODE_DYNAMIC_ROUTE, false, userProfileProvider);
                break;
            }
            case STATE_GOTO_LOGIN:
            {
                model.put(KEY_I_SYNC_TYPE, TYPE_FRESH_SYNC);
                ModuleFactory.getInstance().startLoginController(this, ICommonConstants.EVENT_CONTROLLER_START, false, TYPE_LOGIN_FROM_ENTRY);
                break;
            }
            //plugin
            case STATE_START_PLUGIN:
            {
                ModuleFactory.getInstance().startPluginController(this, (Hashtable)model.fetch(ICommonConstants.KEY_O_PLUGIN_REQUEST));
                break;
            }
            case STATE_GOTO_MAIN:
            {
                DwfSliderPopup.getInstance().hide();
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

                if (dwfAidl != null)
                {
                    try
                    {
                        dwfAidl.setShareLocationInterval(10000);
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
                
                if(!model.getBool(KEY_B_IS_SECRET_STARTED))
                {
                    DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_HOME_LAUCHED, true);
                    if( MaiTaiManager.getInstance().isFromMaiTai() )
                    {
                        MaiTaiManager.getInstance().launchMaiTai(this);
                    }
                    else if( PluginManager.getInstance().isFromPlugin() )
                    {
                        PluginManager.getInstance().startPlugin();
                    }
                    else
                    {
                     // Fix TNANDROID-904
                        boolean doLastAbnormalExitTrip = false;
                        int intervalTime = ((DaoManager)DaoManager.getInstance()).getServerDrivenParamsDao().getIntValue(ServerDrivenParamsDao.LAST_ABNORMAL_EXIT_INTERVALS);
                        if (intervalTime > 0)
                        {
                            NavExitAbnormalDao navExitAbnormalDao = DaoManager.getInstance().getNavExitAbnormalDao();
                            boolean isLastNavExitAbnormal = navExitAbnormalDao.isNavRunning();
                            if (isLastNavExitAbnormal)
                            {
                                long lastNavExitAbnormalTime = navExitAbnormalDao.getTripTimeDot();
                                ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_NAV_EXIT_ABNORMAL_STAMP, String.valueOf(lastNavExitAbnormalTime));
                                ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
                                long currentTime = System.currentTimeMillis();
                                if (lastNavExitAbnormalTime > 0)
                                {
                                    doLastAbnormalExitTrip = ((currentTime - lastNavExitAbnormalTime) < (intervalTime * 60 * 1000L)) ? true : false;
                                }
                            }
                        }
                        if (doLastAbnormalExitTrip)
                        {
                            ModuleFactory.getInstance().startNavController(this, null, DaoManager.getInstance().getTripsDao().getLastTrip(), model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS),
                                false, true, true);
                        }
                        else
                        {
                            if (ShortcutManager.getInstance().isFromShortcut() && ShortcutManager.getInstance().getAction() != null)
                            {
                                goToModule();
                            }
                            else
                            {
                                HomeScreenManager.goToHomeScreen(this, true, model.fetchBool(KEY_B_IS_NEED_PAUSE_LATER));
                            }
                            
                        }
                    }
                    
                    try
                    {
                        TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.C2DM_FEATURE_APP_PRELOAD, null);
                    }
                    catch (Exception e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }
                break;
            }
            case STATE_GOTO_SYNC_RES:
            {
                int type = model.getInt(KEY_I_SYNC_TYPE);
                ModuleFactory.getInstance().startSyncResController(this, type);
                break;
            }
            case STATE_CHECK_AC_TYPE:
            {
                Address address = (Address) model.fetch(KEY_O_SELECTED_ADDRESS);
                if (address != null)
                {
                    ModuleFactory.getInstance().startNavController(this, null, address,
                        model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), false, false);
                    break;
                }
                else
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startMapController(this, ICommonConstants.TYPE_MAP_FROM_ENTRY, null, null, userProfileProvider);
                }
                break;
            }
            case STATE_UP_SELL:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startUpSellController(this, null, true, userProfileProvider);
                break;
            }
			
        }
        super.postStateChangeDelegate(currentState, nextState);
    }
    
    protected AbstractModel createModel()
    {
        return new EntryModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
    
	protected AbstractView createView()
    {
	    return new EntryViewTouch(new EntryUiDecorator());
    }

    private void goToModule()
    {
        String action = ShortcutManager.getInstance().fetchAction();
        
        if (ShortcutManager.SHORTCUT_MYMAP.equals(action))
        {
            ModuleFactory.getInstance().startMapController(this, HomeScreenManager.getHomeMapFromType(), null, null, null);
        }
        else if (ShortcutManager.SHORTCUT_MYPLACE.equals(action))
        {
            ModuleFactory.getInstance().startCategorySearchController(this, ICommonConstants.TYPE_AC_FROM_ENTRY, -1, -1, -1, -1, null, null, null, null, false);
        }
        else if (ShortcutManager.SHORTCUT_MYDRIVE.equals(action))
        {            
            ModuleFactory.getInstance().startMyNav(this);
        }
    }

    protected void checkState()
    {
        if(STATE_GOTO_MAIN == model.getState())
        {
            HomeScreenManager.goToHomeScreen(this, false, false);
        }
    }
}
