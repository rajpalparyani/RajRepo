/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AcController.java
 *
 */
package com.telenav.module.ac;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.datatypes.nav.NavState;
import com.telenav.location.TnLocation;
import com.telenav.module.ModuleFactory;
import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-13
 */
public class AcController extends BrowserSdkController implements IAcConstants
{

    private final static int[][] STATE_TABLE = new int[][]
    {
    { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, IAcConstants.ACTION_INIT },

    { STATE_INIT, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE },
    { STATE_MAIN, CMD_COMMON_BACK, STATE_MAIN, ACTION_BACK_CHECK },
//    { STATE_MAIN, EVENT_MODEL_BACK_TO_DASHBOARD, STATE_BACK_TO_DASHBOARD, ACTION_NONE },

    //After user clicks home button, we will check whether the home address is set or not
    { STATE_MAIN, CMD_HOME, STATE_CHECK_HOME_EXIST, ACTION_CHECK_HOME_EXIST },

    //After user clicks work button, we will check whether the work address is set or not
    { STATE_MAIN, CMD_WORK, STATE_CHECK_WORK_EXIST, ACTION_CHECK_WORK_EXIST },

    //User clicks address button
    { STATE_MAIN, CMD_ADDRESS, STATE_ADDRESS, ACTION_NONE },

    //User clicks recent button
    { STATE_MAIN, CMD_RECENT, STATE_GOTO_RECENT_MAIN, ACTION_NONE },

    //User clicks favorite button
    { STATE_MAIN, CMD_FAVORITES, STATE_GOTO_FAVORITE, ACTION_NONE },

    //User clicks airport button
    { STATE_MAIN, CMD_AIRPORTS, STATE_AIRPORT, ACTION_NONE },

    //User clicks places button, enter poi module
    { STATE_MAIN, CMD_PLACES, STATE_GOTO_POI, ACTION_NONE },

    //User edit home by context menu
    { STATE_MAIN, CMD_EDIT_HOME, STATE_EDIT_HOME, ACTION_NONE },

    //User edit work by context menu
    { STATE_MAIN, CMD_EDIT_WORK, STATE_EDIT_WORK, ACTION_NONE },

    //User click current location button
    { STATE_MAIN, CMD_CURRENT_LOACTION, STATE_FIND_LOCATION, ACTION_CHECK_CURRENT_LOCATION},
    
    //User click DSR button in bottom bar
    { STATE_MAIN, CMD_COMMON_DSR, STATE_DSR_GET_ADDRESS, ACTION_NONE},
    
    //User enter map to home by context menu
    { STATE_MAIN, CMD_MAP_HOME, STATE_MAP_HOME, ACTION_NONE},
    
    //User enter map to work by context menu
    { STATE_MAIN, CMD_MAP_WORK, STATE_MAP_WORK, ACTION_NONE},
    
    //User enter drive to home by context menu
    { STATE_MAIN, CMD_DRIVETO_HOME, STATE_DRIVE_HOME, ACTION_NONE},
    
    //User enter drive to work by context menu
    { STATE_MAIN, CMD_DRIVETO_WORK, STATE_DRIVE_WORK, ACTION_NONE},
    
    { STATE_MAIN, EVENT_MODEL_RETURN_ADDRESS, STATE_RETURN_ADDRESS, ACTION_NONE},
    
    //refresh
    { STATE_MAIN, EVENT_MODEL_REFRESH, STATE_MAIN, ACTION_NONE},
    
    //get current location, if user is adding favorite, then do RGC
    { STATE_FIND_LOCATION, EVENT_MODEL_GETTING_CURRENT_LOCATION, STATE_GET_CURRENT_LOCATION, ACTION_GET_CURRENT_LOCATION },
    
    { STATE_FIND_LOCATION, EVENT_MODEL_LOCATION_GOT, STATE_CHECK_RETURNED_ADDRESS_REGION, ACTION_CHECK_RETURNED_ADDRESS_REGION},
    
    { STATE_FIND_LOCATION, EVENT_MODEL_RGC, STATE_RGC, ACTION_RGC},
    
    { STATE_GET_CURRENT_LOCATION, EVENT_MODEL_LOCATION_GOT, STATE_CHECK_RETURNED_ADDRESS_REGION, ACTION_CHECK_RETURNED_ADDRESS_REGION},
    
    { STATE_GET_CURRENT_LOCATION, EVENT_MODEL_RGC, STATE_RGC, ACTION_RGC},
    
    { STATE_RGC, EVENT_MODEL_RGC_FINISHED, STATE_RETURN_CURRENT_LOCATION, ACTION_NONE},

    //if home exists, return home, else let user set home
    { STATE_CHECK_HOME_EXIST, EVENT_MODEL_RETURN_HOME, STATE_RETURN_HOME, ACTION_NONE },

    { STATE_CHECK_HOME_EXIST, EVENT_MODEL_SET_HOME, STATE_SET_HOME, ACTION_NONE },

    //if work exists, return work, else let user set work
    { STATE_CHECK_WORK_EXIST, EVENT_MODEL_RETURN_WORK, STATE_RETURN_WORK, ACTION_NONE },

    { STATE_CHECK_WORK_EXIST, EVENT_MODEL_SET_WORK, STATE_SET_WORK, ACTION_NONE },
    
    { STATE_ANY, EVENT_MODEL_LAUNCH_LOCALAPP, STATE_BROWSER_GOTO_LAUNCH_LOCALAPP, ACTION_NONE },
    
    //-----------------------------------  switch region by address  ------------------------------
    { STATE_ANY, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_CHECK_RETURNED_ADDRESS_REGION, ACTION_CHECK_RETURNED_ADDRESS_REGION},
    
    { STATE_CHECK_RETURNED_ADDRESS_REGION, EVENT_MODEL_DETECTING_REGION, STATE_DETECTING_REGION, ACTION_NONE},
    
    { STATE_CHECK_RETURNED_ADDRESS_REGION, EVENT_MODEL_RETURN_ADDRESS, STATE_RETURN_ADDRESS, ACTION_NONE},
    
    { STATE_CHECK_RETURNED_ADDRESS_REGION, EVENT_MODEL_SWITCHING_REGION, STATE_SWITCHING_REGION, ACTION_NONE},
    
    { STATE_DETECTING_REGION, EVENT_MODEL_SWITCHING_REGION, STATE_SWITCHING_REGION, ACTION_NONE},
    
    { STATE_DETECTING_REGION, EVENT_MODEL_RETURN_ADDRESS, STATE_RETURN_ADDRESS, ACTION_NONE},
    
    { STATE_DETECTING_REGION, EVENT_MODEL_REGION_SWITCH_FAILED, STATE_REGION_SWITCH_FAILED, ACTION_NONE},
    
    { STATE_DETECTING_REGION, CMD_COMMON_BACK, STATE_MAIN, ACTION_REGION_DETECT_CANCELLED},
    
    //User select change region in change location page.
    { STATE_MAIN, CMD_CHANGE_REGION, STATE_CHANGE_REGION, ACTION_NONE},
    
    { STATE_CHANGE_REGION, CMD_CHANGE_REGION_SAVE, STATE_CHECK_REGION_CHANGE, ACTION_CHECK_REGION_CHANGE},
    
    { STATE_CHECK_REGION_CHANGE, EVENT_MODEL_SWITCHING_REGION, STATE_SWITCHING_REGION, ACTION_NONE},
    
    { STATE_CHECK_REGION_CHANGE, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE},
    
    { STATE_SWITCHING_REGION, EVENT_MODEL_LAUNCH_MAIN, STATE_MAIN, ACTION_NONE},
    
    { STATE_SWITCHING_REGION, IAcConstants.EVENT_MODEL_REGION_SWITCH_FAILED, STATE_REGION_SWITCH_FAILED, ACTION_NONE},
    
    { STATE_SWITCHING_REGION, EVENT_MODEL_RETURN_ADDRESS, STATE_RETURN_ADDRESS, ACTION_NONE},
    
    { STATE_SWITCHING_REGION, CMD_COMMON_BACK, STATE_MAIN, ACTION_REGION_SWITCH_CANCELLED},
    
    { STATE_REGION_SWITCH_FAILED, CMD_COMMON_OK, STATE_MAIN, ACTION_NONE},
    };

    public AcController(AbstractController superController)
    {
        super(superController);

    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_RETURN_HOME:
            {
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
                Stop workStop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                Address workAddress = new Address(Address.SOURCE_PREDEFINED);
                workAddress.setStop(workStop);
                Parameter parameter = new Parameter();
                parameter.put(KEY_O_SELECTED_ADDRESS, workAddress);
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, parameter);
                break;
            }
            case STATE_RETURN_ADDRESS:
            {
                Parameter data = new Parameter();
                data.put(KEY_O_SELECTED_ADDRESS, model.get(KEY_O_SELECTED_ADDRESS));
                postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                break;
            }
            case STATE_SET_HOME:
            case STATE_EDIT_HOME:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_HOME_ADDRESS, userProfileProvider);
                break;
            }
            case STATE_SET_WORK:
            case STATE_EDIT_WORK:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_WORK_ADDRESS, userProfileProvider);
                break;
            }
            case STATE_GOTO_FAVORITE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startFavoriteController(this, model.getInt(KEY_I_AC_TYPE), userProfileProvider);
                break;
            }
            case STATE_GOTO_RECENT_MAIN:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startRecentController(this, model.getInt(KEY_I_AC_TYPE), userProfileProvider);
                break;
            }
            case STATE_GOTO_POI:
            {
                Address oriAddr = null;
                Address destAddr = null;
                NavState navState = null;
                
                Object oriObj;
                if(model.getBool(KEY_B_IS_REGION_SWITCHED_DIRECTLY))
                {
                    TnLocation tnLocation = AbstractDaoManager.getInstance()
                            .getResourceBarDao().getRegionAnchor(this.getRegion());

                    Stop stop = new Stop();
                    stop.setLat(tnLocation.getLatitude());
                    stop.setLon(tnLocation.getLongitude());

                    oriObj = new Address();
                    ((Address)oriObj).setStop(stop);
                }
                else
                {
                    oriObj = this.model.get(KEY_O_ADDRESS_ORI);
                }
                
                if (oriObj instanceof Address)
                {
                    oriAddr = (Address) oriObj;
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
                boolean isChangeLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startCategorySearchController(this, model.getInt(KEY_I_AC_TYPE),
                    model.getInt(KEY_I_SEARCH_TYPE), -1, -1, -1, isChangeLocation, oriAddr, destAddr, navState, userProfileProvider, false);
                break;
            }
            case STATE_AIRPORT:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAirportController(this, model.getInt(KEY_I_AC_TYPE), userProfileProvider);
                break;
            }
            case STATE_ADDRESS:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_OTHER_ADDRESS, userProfileProvider);
                break;
            }
            case STATE_RETURN_CURRENT_LOCATION:
            {
                Parameter data = new Parameter();
                data.put(KEY_O_SELECTED_ADDRESS, model.get(KEY_O_SELECTED_ADDRESS));
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                break;
            }
            case STATE_DSR_GET_ADDRESS:
            {
                int acType = TYPE_AC_FROM_AC_MAIN;
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startDsrController(this, acType, -1, -1, null, null, userProfileProvider);
                break;
            }
            case STATE_MAP_HOME:
            {
                Stop homeStop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                Address homeAddress = new Address(Address.SOURCE_PREDEFINED);
                homeAddress.setStop(homeStop);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_AC, homeAddress, null, userProfileProvider);
                break;
            }
            case STATE_MAP_WORK:
            {
                Stop workStop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                Address workAddress = new Address(Address.SOURCE_PREDEFINED);
                workAddress.setStop(workStop);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_AC, workAddress, null, userProfileProvider);
                break;
            }
            case STATE_DRIVE_HOME:
            {
                Stop homeStop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                Address homeAddress = new Address(Address.SOURCE_PREDEFINED);
                homeAddress.setStop(homeStop);
                ModuleFactory.getInstance().startNavController(this, null, homeAddress, null, false, false);
                break;
            }
            case STATE_DRIVE_WORK:
            {
                Stop workStop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                Address workAddress = new Address(Address.SOURCE_PREDEFINED);
                workAddress.setStop(workStop);
                ModuleFactory.getInstance().startNavController(this, null, workAddress, null, false, false);
                break;
            }
//            case STATE_BACK_TO_DASHBOARD:
//            {
//                AbstractController controller = this.getSuperController();
//                if(controller instanceof DashboardController)
//                {
//                    this.postControllerEvent(EVENT_CONTROLLER_BACK_TO_DASHBOARD, null);
//                }
//                else
//                {
//                    this.postControllerEvent(EVENT_CONTROLLER_BACK_TO_PREV, null);
//                }
//                break;
//            }
            default:
            {
                super.postStateChangeDelegate(currentState, nextState);
            }
        }
    }

    protected AbstractView createView()
    {
    	AcUiDecorator uiDecorator = new AcUiDecorator();
    	return new AcViewTouch(uiDecorator);
    }

    protected AbstractModel createModel()
    {
        return new AcModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
}
