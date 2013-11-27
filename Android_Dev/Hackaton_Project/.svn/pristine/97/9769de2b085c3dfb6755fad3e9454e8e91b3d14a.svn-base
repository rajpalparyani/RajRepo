/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiResultController.java
 *
 */
package com.telenav.module.poi.result;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.ModuleFactory;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.map.MapController;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-9-25
 */
public class PoiResultController extends AbstractCommonController implements IPoiResultConstants
{
    
    private final static int[][] STATE_TABLE = new int[][]{
        {STATE_VIRGIN,              EVENT_CONTROLLER_START,             STATE_POI_RESULT_STARTING,      ACTION_POI_RESULT_START_INIT},
        {STATE_POI_RESULT_STARTING, EVENT_MODEL_POI_RESULT_LIST,        STATE_POI_RESULT_LIST,          ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_POI_RESULT_SELECT,              STATE_POI_ENTRY_CHECK,          ACTION_ENTRY_CHECKING},
        {STATE_POI_RESULT_LIST,     CMD_GOTO_POI_MAP,                   STATE_POI_GOTO_MAP,             ACTION_MAP_RESULTS},
        {STATE_POI_RESULT_LIST,     CMD_HIT_BOTTOM,                     STATE_POI_RESULT_LIST,          ACTION_GETING_MORE},
        {STATE_POI_RESULT_LIST,     CMD_COMMON_GOTO_ONEBOX,             STATE_COMMON_GOTO_ONEBOX,       ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_SEARCH_NEAR_DEST,               STATE_POI_CHECK_ALONGTYPE_CHANGE,       ACTION_CHECK_SEARCHALONG},
        {STATE_POI_RESULT_LIST,     CMD_SEARCH_NEAR_ROUTE,              STATE_POI_CHECK_ALONGTYPE_CHANGE,       ACTION_CHECK_SEARCHALONG},
        {STATE_POI_RESULT_LIST,     CMD_SEARCH_NEAR_ME,                 STATE_POI_CHECK_ALONGTYPE_CHANGE,       ACTION_CHECK_SEARCHALONG},
        {STATE_POI_RESULT_LIST,     CMD_DRIVE_TO,                       STATE_POI_GOTO_NAV,             ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_CALL,                           STATE_POI_RESULT_LIST,          ACTION_CALL_POI},
        {STATE_POI_RESULT_LIST,     CMD_MAP_IT,                         STATE_POI_GOTO_MAP,             ACTION_MAP_IT},
        {STATE_POI_RESULT_LIST,     CMD_SAVE_TO_FAV,                    STATE_POI_GOTO_FAV_EDIT,          ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_RATE_IT,                        STATE_POI_GOTO_RATING,          ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_SHARE_IT,                       STATE_POI_GOTO_SHARE,           ACTION_NONE},
        {STATE_POI_GOTO_SHARE,      EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV,                   ACTION_NONE },
        {STATE_POI_GOTO_SHARE,      EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV,                   ACTION_SHOW_TIMEOUT_MESSAGE },
        {STATE_POI_RESULT_LIST,     CMD_GOTO_CHANGE_SORT,               STATE_POI_SORT_BY,              ACTION_NONE},
        {STATE_POI_RESULT_LIST,     EVENT_MODEL_POI_RESULT_LIST,        STATE_POI_RESULT_LIST,          ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_GOTO_CHANGE_LOCATION,           STATE_POI_GOTO_CHANGE_LOCATION, ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_CHECK_SEARCH,                   STATE_POI_CHECK_INPUT_TEXT,     ACTION_CHECK_TEXT_INPUT},
        {STATE_POI_CHECK_INPUT_TEXT,     EVENT_MODEL_REDO_SEARCH,       STATE_POI_REDO_SEARCHING,       ACTION_SEARCH_BASE_NEW_LOCATION},
        {STATE_POI_CHECK_INPUT_TEXT,     EVENT_MODEL_GOTO_ONE_BOX,      STATE_COMMON_GOTO_ONEBOX,       ACTION_NONE},
    
        {STATE_POI_RESULT_LIST,         CMD_SEARCH_BY_BEST_MATCH,              STATE_POI_REDO_SEARCHING,       ACTION_SORT_CHANGE_SEARCH},
        {STATE_POI_RESULT_LIST,         CMD_SEARCH_BY_DISTANCE,            STATE_POI_REDO_SEARCHING,       ACTION_SORT_CHANGE_SEARCH},
        {STATE_POI_REDO_SEARCHING, CMD_COMMON_BACK,                  STATE_PREV,                 ACTION_CANCEL_SEARCH},
        {STATE_POI_REDO_SEARCHING,  EVENT_MODEL_GOTO_NO_GPS_WARNING,  STATE_NO_GPS_WARNING,       ACTION_NONE},
        {STATE_POI_CHECK_ALONGTYPE_CHANGE, EVENT_MODEL_CHANGE_ALONGROUTE_TYPE,  STATE_POI_REDO_SEARCHING,    ACTION_SEARCH_ALONG},
        {STATE_POI_CHECK_ALONGTYPE_CHANGE, EVENT_MODEL_ALONGROUTE_NO_CHANGE,  STATE_POI_RESULT_LIST,    ACTION_NONE},
        {STATE_POI_ENTRY_CHECK,     EVENT_MODEL_GOTO_DETAIL,            STATE_GOTO_POI_DETAIL,          ACTION_NONE},
        {STATE_POI_ENTRY_CHECK,     EVENT_MODEL_RETURN_POIS,            STATE_GOT_ADDRESS,              ACTION_NONE},
        {STATE_POI_REDO_SEARCHING,  EVENT_MODEL_SHOW_RESEARCH_LIST,     STATE_POI_RESULT_LIST,          ACTION_NONE},
        {STATE_POI_CHECK_ADDRESS_GOT, EVENT_MODEL_POI_RESULT_LIST,      STATE_POI_RESULT_LIST,          ACTION_CHANGE_LOCATION},
        {STATE_POI_CHECK_ADDRESS_GOT, EVENT_MODEL_POST_ADDRESS_TO_SUPER, STATE_GOT_ADDRESS,             ACTION_NONE},
        {STATE_POI_GOTO_FAV_EDIT, EVENT_CONTROLLER_FAVORITE_EDITED, STATE_PREV, ACTION_NONE},
        {STATE_ANY,                 EVENT_CONTROLLER_ADDRESS_SELECTED,  STATE_POI_CHECK_ADDRESS_GOT,     ACTION_CHECK_ADDRESS_GOT},
        {STATE_ANY,                 EVENT_CONTROLLER_SHOW_POI_LIST,     STATE_POI_RESULT_LIST,           ACTION_UPDATE_SEARCH_CENTER},
        {STATE_ANY,                 EVENT_CONTROLLER_MAP_TO_POI_LIST,   STATE_POI_RESULT_LIST,          ACTION_NONE},
        {STATE_POI_RESULT_LIST,     CMD_FEEDBACK,   STATE_POI_SEARCH_FEEDBACK,          ACTION_NONE},  
        {STATE_POI_SEARCH_FEEDBACK,     EVENT_MODEL_EXIT_BROWSER,   STATE_PREV,          ACTION_NONE},  
        {STATE_ANY,                 EVENT_MODEL_REDO_SEARCH, STATE_POI_REDO_SEARCHING, ACTION_SEARCH_BASE_NEW_LOCATION},
        {STATE_ANY,                 CMD_REDO_SEARCH, STATE_ANY, ACTION_SEARCH_BASE_NEW_LOCATION},
        {STATE_ANY,                 EVENT_CONTROLLER_GO_BACK_TO_POI_RESULT,  STATE_POI_RESULT_STARTING,      ACTION_POI_RESULT_START_INIT},
        {STATE_ANY, EVENT_MODEL_POST_SEARCH_ANCHOR, STATE_SEARCH_ANCHOR_SELECT,             ACTION_NONE},
        {STATE_POI_RESULT_LIST, CMD_COMMON_BACK,    STATE_POI_RESULT_BACK_CHECKING,             ACTION_BACK_CHECKING},
        {STATE_POI_RESULT_BACK_CHECKING, EVENT_MODEL_COMMON_BACK,    STATE_VIRGIN,             ACTION_NONE},
        {STATE_POI_RESULT_BACK_CHECKING, EVENT_MODEL_GOTO_POI_MAP,    STATE_POI_GOTO_MAP,             ACTION_MAP_RESULTS},

    };
    public PoiResultController(AbstractController superController)
    {
        super(superController);
    }

    protected AbstractView createView()
    {
        PoiResultUiDecorator uiDecorator = new PoiResultUiDecorator();
        return new PoiResultViewTouch(uiDecorator);
    }

    protected AbstractModel createModel()
    {
        PoiResultModel model = new PoiResultModel();
        
        return model;
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

   
    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_GOTO_POI_DETAIL:
            {
                int acType = model.getInt(KEY_I_AC_TYPE);
                int selectedIndex = getSelectedIndex();
              
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                ModuleFactory.getInstance().startDetailController(this, acType, selectedIndex, dataWrapper, userProfileProvider, isSearchNearBy);
                break;
            }
            case STATE_GOT_ADDRESS:
            {
                Address address;
                if (currentState == STATE_POI_CHECK_ADDRESS_GOT && model.get(KEY_O_SELECTED_ADDRESS) != null)
                {
                    address = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                }
                else
                {
                    address = getSelectAddress();
                }
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_DRIVE_TO, getSelectedIndex());
                
                Parameter parameter = new Parameter();
                parameter.put(KEY_O_SELECTED_ADDRESS, address);
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, parameter);
                break;
            }
            case STATE_SEARCH_ANCHOR_SELECT:
            {   
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                int acType = model.getInt(ICommonConstants.KEY_I_AC_TYPE);
                int searchType = dataWrapper.getSearchType();
                int alongRouteType = dataWrapper.getAlongRouteType();
                boolean isChangeLocation = model.getBool(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION);
                boolean isSearchingDirectly = true;
                String initText = dataWrapper.getShowText();
                Address anchorAddr = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                Address destAddr = (Address) model.get(ICommonConstants.KEY_O_ADDRESS_DEST);
                NavState navState = (NavState) dataWrapper.getNavState();
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                if(userProfileProvider == null)
                {
                    userProfileProvider = new UserProfileProvider();
                }
                boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                ModuleFactory.getInstance().startOneBoxController(this, acType, searchType, alongRouteType, initText, isSearchingDirectly, isChangeLocation,  anchorAddr,
                    destAddr, navState, userProfileProvider, isSearchNearBy, false, false);                
                break;
            }
            case STATE_POI_GOTO_MAP:
            {
                getSelectAddress();
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                boolean isNotOneBoxSearch = (dataWrapper != null && (dataWrapper.getCategoryId() == PoiDataRequester.TYPE_NO_CATEGORY_ID || dataWrapper
                        .getCategoryId() > 0));
                String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                int acType = model.getInt(KEY_I_AC_TYPE);
                int mapFromType = this.model.fetchInt(KEY_I_TYPE_MAP_FROM);
                if(mapFromType == -1)
                {
                    mapFromType = TYPE_MAP_FROM_POI;
                }

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

                int categoryId = model.getInt(KEY_I_CATEGORY_ID);
                int sortType = model.getInt(KEY_I_SEARCH_SORT_TYPE);
                int alongRouteType = model.getInt(KEY_I_SEARCH_ALONG_TYPE);
                NavState navState = (NavState) this.model.get(KEY_O_NAVSTATE);
                Address origAddress = (Address)model.get(KEY_O_ADDRESS_ORI);
                Address destAddress = (Address)model.get(KEY_O_ADDRESS_DEST);
                boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                
                if (isFromPoiMap)
                {
                    Parameter param = new Parameter();
                    param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
                    param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
                    param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
                    param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
                    param.put(KEY_I_SEARCH_SORT_TYPE, sortType);
                    param.put(KEY_I_CATEGORY_ID, categoryId);
                    param.put(KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
                    param.put(KEY_O_NAVSTATE, navState);
                    param.put(KEY_O_ADDRESS_ORI, origAddress);
                    param.put(KEY_O_ADDRESS_DEST, destAddress);
                    param.put(KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                    
                    postControllerEvent(EVENT_CONTROLLER_GO_TO_POI_MAP, param);
                }
                else
                {
                    int searchType = model.getInt(KEY_I_SEARCH_TYPE);
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startMapController(this, mapFromType, 0, categoryId, acType, searchType, sortType, alongRouteType, false, searchText, searchText, origAddress, destAddress, navState, dataWrapper, userProfileProvider, isSearchNearBy);
                }

                break;
            }
            case STATE_POI_GOTO_NAV:
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_DRIVE_TO, getSelectedIndex());

                Address destAddress = getSelectAddress();
                boolean isFromSearchAlong = (this.model.getInt(KEY_I_SEARCH_TYPE) == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE);
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
                ModuleFactory.getInstance().startNavController(this, null, destAddress, poiDataWrapper, false, 
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), isFromSearchAlong, false);
                break;
            }
            case STATE_POI_GOTO_RATING:
            {
//                int selectedIndex = this.model.fetchInt(KEY_I_POI_SELECTED_INDEX);
                //ModuleFactory.getInstance().startPoiReviewController(this, selectedIndex);
                //TODO
                break;
            }
            case STATE_POI_GOTO_SHARE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startNativeShareController(this, getSelectAddress(), userProfileProvider);
                break;
            }
            case STATE_POI_GOTO_CHANGE_LOCATION:
            {
                Object addrOri = this.model.get(KEY_O_ADDRESS_ORI);
                boolean isNeedCurrentLocation = true;
                if(addrOri == null)
                {
                    isNeedCurrentLocation = false;
                }
                else
                {
                    Stop innerStop = ((Address)addrOri).getStop();
                    if(innerStop.getType() == Stop.STOP_CURRENT_LOCATION)
                    {
                        isNeedCurrentLocation = false;
                    }
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAcController(this, TYPE_AC_FROM_CHANGE_LOCATION, true, isNeedCurrentLocation, false, userProfileProvider);
                break;
           }
            case STATE_POI_GOTO_FAV_EDIT:
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.model.get(KEY_O_POI_DATA_WRAPPER);
                poiDataWrapper.setSelectedIndex(getSelectedIndex());
                Address selectedAddress = poiDataWrapper.getAddress(poiDataWrapper.getSelectedIndex());
                if (selectedAddress != null)
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startAddPlaceController(this, ICommonConstants.PLACE_OPERATION_TYPE_ADD,  selectedAddress, poiDataWrapper, null, null, userProfileProvider);               
                }
                break;
            }
        }
        
    }

    protected Address getSelectAddress()
    {
        PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
        int selectedIndex = this.model.getInt(KEY_I_POI_SELECTED_INDEX);
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
 
    protected int getSelectedIndex()
    {
        int selectedIndex = model.getInt(KEY_I_POI_SELECTED_INDEX);
        if (selectedIndex < 0)
        {
            selectedIndex = 0;
        }
        return selectedIndex;
    }
}
