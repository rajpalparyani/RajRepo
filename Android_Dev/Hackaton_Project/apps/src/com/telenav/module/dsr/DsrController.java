/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DSRController.java
 *
 */
package com.telenav.module.dsr;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.module.ModuleFactory;
import com.telenav.module.map.MapController;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.result.PoiResultController;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author bduan
 *@date Aug 23, 2010
 */
public class DsrController extends AbstractCommonController implements IDsrConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_CHECK_NETWORK, ACTION_CHECK_NETWORK },
        { STATE_CHECK_NETWORK, EVENT_MODEL_DO_INIT, STATE_INIT, ACTION_INIT },
        { STATE_INIT, CMD_DO_FINISH, STATE_INIT, ACTION_MANNUAL_STOP }, 
        { STATE_INIT, EVENT_MODEL_DO_THINKING, STATE_INIT, ACTION_AUTO_STOP }, 
        { STATE_INIT, EVENT_MODEL_ERROR_TIMES_EXCEED, STATE_VIRGIN, ACTION_NONE }, 
        { STATE_INIT, EVENT_MODEL_RECO_ERROR, STATE_INIT, ACTION_PLAY_ERROR_AUDIO }, 
        { STATE_INIT, EVENT_MODEL_PLAY_ERROR_FINISH, STATE_INIT, ACTION_INIT }, 
        
        //for unit test
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_UNIT_TEST, ACTION_NONE },
        { STATE_UNIT_TEST, CMD_DO_TEST, STATE_TEST_THINKING, ACTION_SEND_DATA },
        
        //go to different modules
        { STATE_ANY, EVENT_MODEL_SET_HOME, STATE_GO_TO_HOME, ACTION_NONE },
        { STATE_GO_TO_HOME, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_GO_TO_NAV, ACTION_NONE },
        
        { STATE_ANY, EVENT_MODEL_SET_OFFICE, STATE_GO_TO_OFFICE, ACTION_NONE },
        { STATE_GO_TO_OFFICE, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_GO_TO_NAV, ACTION_NONE },
        
        //search is handled by dsr, just show list in poiResultController.
        { STATE_ANY, EVENT_MODEL_GO_TO_SEARCH, STATE_DO_SEARCH, ACTION_DO_SEARCH },
        { STATE_ANY, EVENT_MODEL_ADDRESS_GOT, STATE_RETURN_ADDRESSS, ACTION_NONE },
        { STATE_DO_SEARCH, EVENT_MODEL_GOTO_POI_LIST, STATE_GO_TO_POI_LIST, ACTION_NONE },
        { STATE_DO_SEARCH, EVENT_MODEL_MAP_POI_LIST, STATE_GO_TO_POI_MAP, ACTION_NONE },
        
        { STATE_ANY, EVENT_MODEL_MULTI_MATCH, STATE_GO_TO_MULTI_MATCH, ACTION_NONE },
        { STATE_GO_TO_MULTI_MATCH, EVENT_CONTROLLER_STOP_SELECTED, STATE_STOP_SELECTED, ACTION_PREPARE_AUDIO },
        
        { STATE_ANY, EVENT_MODEL_GO_TO_AC, STATE_COMMON_LINK_TO_AC, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_GO_TO_MAP, STATE_GO_TO_MAP, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_GO_TO_NAV, STATE_GO_TO_NAV, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_RESUME_TRIP, STATE_GO_RESUME_TRIP, ACTION_NONE },
    };

    public DsrController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_VIRGIN:
            {
            	MediaManager.getInstance().stopPlayAudio();
            	break;
            }
            case STATE_RETURN_ADDRESSS:
            {
                Parameter data = new Parameter();
                data.put(KEY_O_SELECTED_ADDRESS, model.get(KEY_O_SELECTED_ADDRESS));
                data.put(KEY_V_STOP_AUDIOS, model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS));
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                break;
            }
            case STATE_STOP_SELECTED:
            {
                Address selectAddress = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                Parameter data = new Parameter();
                data.put(KEY_O_SELECTED_ADDRESS, selectAddress);
                data.put(KEY_V_STOP_AUDIOS, model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS));
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                break;
            }
            case STATE_GO_TO_MAP:
            {
                if(NavSdkNavEngine.getInstance().isRunning())
                {
                    this.backToLastStableState();
                }
                else
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    //Fix bug TN-1850.
                    ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_DSR, (Address) model.get(KEY_O_SELECTED_ADDRESS), null, userProfileProvider);
                    break;
//                    Parameter parameter = new Parameter();
//                    parameter.put(KEY_I_AC_TYPE, TYPE_AC_FROM_DSR);
//                    parameter.put(KEY_O_SELECTED_ADDRESS, model.fetch(KEY_O_SELECTED_ADDRESS));
//                    parameter.put(KEY_V_STOP_AUDIOS, model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS));
//                    postControllerEvent(EVENT_CONTROLLER_DSR_MAP, parameter);
                }
                break;
            }
            case STATE_GO_TO_NAV:
            {
                if(NavSdkNavEngine.getInstance().isRunning())
                {
                    this.backToLastStableState();
                }
                else
                {
                    Parameter parameter = new Parameter();
                    parameter.put(KEY_I_AC_TYPE, TYPE_AC_FROM_DSR);
                    parameter.put(KEY_O_SELECTED_ADDRESS, model.fetch(KEY_O_SELECTED_ADDRESS));
                    parameter.put(KEY_V_STOP_AUDIOS, model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS));
                    postControllerEvent(EVENT_CONTROLLER_DSR_NAV, parameter);
                }
                break;
            }
            case STATE_GO_TO_POI_MAP:
            {
                Vector maps = getControllerInStack(MapController.class.getName());

                boolean isFromPoiMap = false;
                if (maps != null)
                {
                    for (int i = 0; i < maps.size(); i++)
                    {
                        MapController mapController = (MapController) maps.elementAt(i);
                        int tmpMapFromType = mapController.getModel().getInt(
                            KEY_I_TYPE_MAP_FROM);
                        if (tmpMapFromType == TYPE_MAP_FROM_POI
                                || tmpMapFromType == TYPE_MAP_FROM_SPECIFIC_POI
                                || tmpMapFromType == TYPE_MAP_FROM_BROWSER
                                || tmpMapFromType == TYPE_MAP_FROM_ONEBOX_POI)
                        {
                            isFromPoiMap = true;
                            break;
                        }
                    }
                }
                
                getSelectAddress();
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                int acType = model.getInt(KEY_I_AC_TYPE);
                int mapFromType = this.model.fetchInt(KEY_I_TYPE_MAP_FROM);
                if(mapFromType == -1)
                {
                    mapFromType = TYPE_MAP_FROM_POI;
                }
                
                boolean isSearchNearBy = true;
                if(isFromPoiMap)
                {
                    Parameter param = new Parameter();
                    param.put(ICommonConstants.KEY_B_IS_DSR_FROM_POI_MAP, true);
                    param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
                    param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
                    param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
                    param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
                    param.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_POI_MAP, param);
                }
                else
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startMapController(this, mapFromType, dataWrapper,searchText, acType, userProfileProvider, isSearchNearBy);
                }
                break;
            }
            case STATE_GO_TO_POI_LIST:
            {
                int acType = this.model.getInt(KEY_I_AC_TYPE);
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                int categoryId = this.model.getInt(KEY_I_CATEGORY_ID);
                int sortType = this.model.getInt(KEY_I_SEARCH_SORT_TYPE);
                int selectedIndex = this.model.getInt(KEY_I_POI_SELECTED_INDEX);
                poiDataWrapper.setSelectedIndex(selectedIndex);
                int searchType = this.model.getInt(KEY_I_SEARCH_TYPE);
                int alongRouteType = this.model.getInt(KEY_I_SEARCH_ALONG_TYPE);
                String showText = this.model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                String searchString = this.model.getString(KEY_S_COMMON_SEARCH_TEXT);
                boolean isSearchNearBy = true;
                Address anchorAddr = null;
                if(searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
                {
                    anchorAddr = (Address) this.model.get(KEY_O_ADDRESS_ORI);
                    isSearchNearBy = false;
                }
                else
                	anchorAddr = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
                
                Address destAddr = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                NavState navState = (NavState) this.model.get(KEY_O_NAVSTATE);
                PoiDataWrapper dataWrapper = (PoiDataWrapper)this.model.get(KEY_O_POI_DATA_WRAPPER);
                
                Vector results = getControllerInStack(PoiResultController.class.getName());

                boolean isFromPoiList = false;
                if (results != null && results.size() > 0)
                {
                    isFromPoiList = true;
                }
                
                if (!isFromPoiList)
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startPoiResultController(this, selectedIndex, categoryId, acType, searchType, sortType,
                        alongRouteType, showText, searchString, null, destAddr, navState, dataWrapper, userProfileProvider, isSearchNearBy);
                }
                else
                {
                	Parameter data = new Parameter();
                	data.put(ICommonConstants.KEY_I_CATEGORY_ID, categoryId);
                    data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
                    data.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
                    data.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
                    if(anchorAddr != null)
                    {
                        data.put(ICommonConstants.KEY_O_ADDRESS_ORI, anchorAddr);
                    }
                    data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddr);
                    data.put(ICommonConstants.KEY_O_NAVSTATE, navState);
                    data.put(ICommonConstants.KEY_S_COMMON_SEARCH_TEXT, searchString);
                    data.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, showText);
                    data.put(ICommonConstants.KEY_I_SEARCH_SORT_TYPE, sortType);
                    data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
                    data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, false);
                    data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                    data.put(ICommonConstants.KEY_B_NO_NEED_UPDATE_SCROLL, true);
                    this.postControllerEvent(EVENT_CONTROLLER_GO_BACK_TO_POI_RESULT, data);
                }
                break;
            }
            case STATE_GO_TO_HOME:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_HOME_ADDRESS, userProfileProvider);
                break;
            }
            case STATE_GO_TO_OFFICE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_WORK_ADDRESS, userProfileProvider);
                break;
            }
            case STATE_GO_RESUME_TRIP:
            {
                ModuleFactory.getInstance().startNavController(this, null, DaoManager.getInstance().getTripsDao().getLastTrip(),
                    model.fetchVector(KEY_V_STOP_AUDIOS), false, true);
                break;
            }
            case STATE_GO_TO_MULTI_MATCH:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startStopsSecelctionController(this, model.getVector(KEY_V_ALTERNATIVE_ADDRESSES), -1, "", userProfileProvider);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new DsrModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new DsrViewTouch(new DsrUiDecorator());
    }
    
    protected Address getSelectAddress()
    {
        PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
        int selectedIndex = this.model.fetchInt(KEY_I_POI_SELECTED_INDEX);
        if(selectedIndex < 0)
        {
            Address address = dataWrapper.getAddress(0);
            if(address.getPoi() != null && address.getPoi().getType() == Poi.TYPE_SPONSOR_POI)
            {
                dataWrapper.setSelectedIndex(1);
            }
            else
            {
                dataWrapper.setSelectedIndex(0);
            }
        }
        else
        {
            dataWrapper.setSelectedIndex(selectedIndex);
        }
        Address address = dataWrapper.getSelectedAddress();
       
        return address;
    }

}
