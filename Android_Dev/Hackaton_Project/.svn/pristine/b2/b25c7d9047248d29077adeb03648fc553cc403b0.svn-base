/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * OneBoxSearchController.java
 *
 */
package com.telenav.module.oneboxsearch;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.module.ModuleFactory;
import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.module.map.MapController;
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
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-3
 */
public class OneBoxSearchController extends BrowserSdkController implements IOneBoxSearchConstants
{
    private final static int[][] STATE_TABLE = new int[][]{
        {STATE_VIRGIN,                      EVENT_CONTROLLER_START,     STATE_CHECK_IS_SEARCH_DIRECTLY,           IOneBoxSearchConstants.ACTION_INIT},
        {STATE_CHECK_IS_SEARCH_DIRECTLY,    EVENT_SEARCHING_DIRECTLY,   STATE_DOING_ONEBOX_SEARCH,       ACTION_ONEBOX_SEARCH},
        {STATE_CHECK_IS_SEARCH_DIRECTLY,    EVENT_GOTO_ONE_BOX_MAIN,    STATE_ONE_BOX_MAIN,                 ACTION_NONE},
        {STATE_ONE_BOX_MAIN,                CMD_SELECT_RECENT_FAV,      STATE_GOT_RECENT_FAV,               ACTION_NONE},
        {STATE_ONE_BOX_MAIN,                CMD_DO_CATELOG_SEARCH,      STATE_DOING_ONEBOX_SEARCH,          ACTION_ONEBOX_SEARCH},
        {STATE_ONE_BOX_MAIN,                CMD_DO_ONEBOX_SEARCH,       STATE_DOING_ONEBOX_SEARCH,       ACTION_ONEBOX_SEARCH},
        {STATE_ONE_BOX_MAIN,                CMD_SELECT_ADDRESS,         STATE_CHECK_VALIDATE_ADDRESS,       ACTION_NONE},
        {STATE_ONE_BOX_MAIN,                EVENT_MODEL_SELECT_ADDRESS, STATE_CHECK_VALIDATE_ADDRESS,       ACTION_NONE},
        {STATE_ONE_BOX_MAIN,                CMD_GOTO_CHANGE_LOCATION,   STATE_GOTO_CHANGE_LOCATION,       ACTION_NONE},
        {STATE_ONE_BOX_MAIN,                CMD_CLEAR_SEARCH_HISTORY,   STATE_QUERY_CLEAR_HISTORY,       ACTION_NONE},
        {STATE_QUERY_CLEAR_HISTORY,         CMD_COMMON_OK,              STATE_ONE_BOX_MAIN,              ACTION_CLEAR_HISTORY},
        {STATE_DOING_ONEBOX_SEARCH,         EVENT_SHOW_SUGGESTIONS,     STATE_DID_U_MEAN,                ACTION_PREPARE_SUGGESTION},
        {STATE_DOING_ONEBOX_SEARCH,         EVENT_MODEL_RETURN_STOPS,   STATE_GOT_STOPS,              ACTION_NONE},
        {STATE_DOING_ONEBOX_SEARCH,         EVENT_MODEL_POIS_GOT,  STATE_POIS_GOT,             ACTION_NONE},
        {STATE_DOING_ONEBOX_SEARCH,         EVENT_SHOW_MULISTOPS,       STATE_DID_U_MEAN_MULTISTOP,      ACTION_NONE},
        {STATE_DOING_ONEBOX_SEARCH,         EVENT_MODEL_GOTO_NO_GPS_WARNING, STATE_NO_GPS_WARNING,       ACTION_NONE},
        {STATE_DID_U_MEAN,                  CMD_POI_SUGGEST_SELECT,     STATE_DOING_ONEBOX_SEARCH,       ACTION_ONEBOX_SEARCH},
        {STATE_DID_U_MEAN,                  CMD_COMMON_GOTO_ONEBOX,     STATE_ONE_BOX_MAIN,              ACTION_NONE},
        {STATE_DID_U_MEAN,                  CMD_COMMON_BACK,            STATE_PREV,                      ACTION_CLEAR_MULTI_RESULTS},
        {STATE_DID_U_MEAN_MULTISTOP,        CMD_STOP_SELECT,            STATE_GOT_STOPS,                 ACTION_NONE},
        {STATE_DID_U_MEAN_MULTISTOP,        CMD_COMMON_GOTO_ONEBOX,     STATE_ONE_BOX_MAIN,              ACTION_NONE},      
        {STATE_CHECK_ADDRESS_GOT,           EVENT_GOTO_ONE_BOX_MAIN,    STATE_ONE_BOX_MAIN,              ACTION_SET_LOCATION_CHANGE},
        {STATE_CHECK_ADDRESS_GOT,           EVENT_MODEL_RETURN_ADDRESS_TO_SUPER,    STATE_POST_ADDRESS_TO_SUPER,     ACTION_NONE},
        {STATE_ANY,                         EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_CHECK_ADDRESS_GOT,  ACTION_CHECK_ADDRESS_GOT},
        {STATE_VALIDATE_ADDRESS,            EVENT_CONTROLLER_ADDRESS_VALIDATED, STATE_VALIDATED_ADDRESS_RETURNED, ACTION_VALIDATED_ADDRESS_RETURNED},
        {STATE_VALIDATED_ADDRESS_RETURNED,            EVENT_MODEL_DO_VALIDATE_ADDRESS, STATE_CHECK_VALIDATE_ADDRESS,       ACTION_NONE},
        
        {STATE_ONE_BOX_MAIN,                EVENT_MODEL_DO_VALIDATE_ADDRESS, STATE_VALIDATE_ADDRESS,       ACTION_NONE},
        {STATE_ONE_BOX_MAIN,                CMD_VALIDATE_CONTACT_ADDRESS, STATE_VALIDATE_ADDRESS,       ACTION_NONE},
        
        { STATE_ONE_BOX_MAIN, CMD_SELECT_ADDRESS, STATE_CHECK_VALIDATE_ADDRESS, ACTION_NONE },
        
        { STATE_ONE_BOX_MAIN, CMD_SELECT_CITY, STATE_CHECK_CITY, ACTION_CHECK_CITY },
        
        { STATE_ONE_BOX_MAIN, CMD_SAVE, STATE_VALIDATE_INPUT, ACTION_VALIDATE_INPUT },
        
        { STATE_CHECK_CITY, EVENT_MODEL_RETURN_CITY_ADDRESS, STATE_CHECK_VALIDATE_ADDRESS, ACTION_NONE },
        
        { STATE_CHECK_CITY, EVENT_MODEL_GO_TO_VALIDATE_ADDRESS, STATE_VALIDATE_INPUT, ACTION_VALIDATE_INPUT },
        
        { STATE_VALIDATE_INPUT, EVENT_MODEL_VALIDATE_LIST_HOME, STATE_CHECK_VALIDATE_ADDRESS, ACTION_NONE },
    
        { STATE_VALIDATE_INPUT,  EVENT_MODEL_VALIDATE_HOME, STATE_VALIDATE_ADDRESS, ACTION_NONE },
        
        {STATE_ANY,  CMD_ONEBOX_GO_TO_SET_HOME,  STATE_ONEBOX_GO_TO_SET_HOME,  ACTION_NONE},
        
        {STATE_ANY,  CMD_ONEBOX_GO_TO_SET_WORK,  STATE_ONEBOX_GO_TO_SET_WORK,  ACTION_NONE},
        
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_ONEBOX_SEARCH, STATE_PREV,  ACTION_REFRESH_AUTO_SUGGESTION },
        
        {STATE_CHECK_ADDRESS_GOT,      EVENT_MODEL_ONE_ADDRESS,    STATE_GOT_ONE_ADDRESS,     ACTION_NONE},
        
    };
    public OneBoxSearchController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        if (currentState == STATE_DOING_ONEBOX_SEARCH)
        {
            Object listAdapter = this.model.get(KEY_O_LIST_ADAPTER);
            if (listAdapter instanceof AutoSuggestListAdapter)
            {
                AutoSuggestListAdapter autoSuggestListAdapter = (AutoSuggestListAdapter) listAdapter;
                autoSuggestListAdapter.setNeedShow(true);
            }
        }
        switch(nextState)
        {
            case STATE_GOT_RECENT_FAV:
            {
                boolean isFromChoosingLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
                Address address = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
                if(isFromChoosingLocation)
                {
                    Parameter parameters = new Parameter();
                    if (address != null)
                    {
                        parameters.put(KEY_O_SELECTED_ADDRESS, address);
                        
                        //FIXME: DB, do we need do this?
//                    PoiDataWrapper.getInstance().clearCache();
//                    PoiDataWrapper.getInstance().addStopAddress(address);
//                    PoiDataWrapper.getInstance().setSelectedIndex(0);
                    }
                    parameters.put(KEY_B_IS_FROM_ONE_BOX, true);
                    this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, parameters);
                }
                else
                {
                    startControllerByAddress(address);
                }
                
                break;
            }
            case STATE_POIS_GOT:
            {
                boolean isChangeLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
                int searchType = this.model.getInt(KEY_I_SEARCH_TYPE);
                int alongRouteType = this.model.getInt(KEY_I_SEARCH_ALONG_TYPE);
                boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                int acType = this.model.getInt(KEY_I_AC_TYPE);
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                int categoryId = this.model.getInt(KEY_I_CATEGORY_ID);
                int sortType = this.model.getInt(KEY_I_SEARCH_SORT_TYPE);
                int selectedIndex = this.model.getInt(KEY_I_POI_SELECTED_INDEX);
                poiDataWrapper.setSelectedIndex(selectedIndex);
                String showText = this.model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                String searchString = this.model.getString(KEY_S_COMMON_SEARCH_TEXT);
                Address anchorAddr = (Address) this.model.get(KEY_O_ADDRESS_ORI);
                Address destAddr = (Address) this.model.get(KEY_O_ADDRESS_DEST);
                NavState navState = (NavState) this.model.get(KEY_O_NAVSTATE);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                if (superController instanceof MapController)
                {
                        Parameter data = new Parameter();
                        
                        data.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, TYPE_MAP_FROM_ONEBOX_POI);
                        if(searchType < 0)
                        {
                            searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
                        }
                        if(alongRouteType < 0 && searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
                        {
                            alongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD; 
                        }
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
                        data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                        data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChangeLocation);
                        data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                        this.postControllerEvent(EVENT_CONTROLLER_GO_TO_POI_MAP, data);
                }
                else
                {
                    Vector results = getControllerInStack(PoiResultController.class.getName());

                    boolean isFromPoiList = false;
                    if (results != null && results.size() > 0)
                    {
                        isFromPoiList = true;
                    }

                    if (!isFromPoiList || isChangeLocation)
                    {
                        ModuleFactory.getInstance().startPoiResultController(this, selectedIndex, categoryId, acType,
                            searchType, sortType, alongRouteType, isChangeLocation, showText, searchString, anchorAddr,
                            destAddr, navState, poiDataWrapper, userProfileProvider, isSearchNearBy);
                    }
                    else
                    {
                        Parameter param = new Parameter();
                        param.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                        param.put(KEY_O_SELECTED_ADDRESS, model.get(KEY_O_SELECTED_ADDRESS));
                        param.put(KEY_S_COMMON_SHOW_SEARCH_TEXT, model.get(KEY_S_COMMON_SHOW_SEARCH_TEXT));
                        param.put(KEY_O_SEARCH_CENTER, model.get(KEY_O_ADDRESS_ORI));
                        param.put(KEY_I_SEARCH_SORT_TYPE, model.get(KEY_I_SEARCH_SORT_TYPE));
                        param.put(KEY_B_NO_NEED_UPDATE_SCROLL, true);
                        param.put(KEY_I_SEARCH_TYPE, searchType);
                        param.put(KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
                        param.put(KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                        this.postControllerEvent(EVENT_CONTROLLER_SHOW_POI_LIST, param);
                    }
                }
                
                break;
            }
            case STATE_GOT_STOPS:
            {
                int selectIndex = model.getInt(KEY_I_POI_SELECTED_INDEX);
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model
                        .get(KEY_O_POI_DATA_WRAPPER);
                poiDataWrapper.setSelectedIndex(selectIndex);
                
                boolean isFromChoosingLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
                Address address = poiDataWrapper.getAddress(poiDataWrapper
                    .getSelectedIndex());
                if (isFromChoosingLocation)
                {
                    Parameter parameter = new Parameter();
                    
                    parameter.put(KEY_O_SELECTED_ADDRESS, address);
                    if(this.model.getInt(KEY_I_AC_TYPE) != TYPE_AC_FROM_CHANGE_LOCATION)
                    {
                        String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                        parameter.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
                    }
                    parameter.put(KEY_B_IS_FROM_ONE_BOX, true);
                    this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED,
                                parameter);
                }
                else
                {
                    startControllerByAddress(address);
                }
                
                break;
            }
            case STATE_GOTO_CHANGE_LOCATION:
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
                ModuleFactory.getInstance().startAcController(this, TYPE_AC_FROM_ONE_BOX, true, isNeedCurrentLocation, false, userProfileProvider);
                break;
            }
            case STATE_POST_ADDRESS_TO_SUPER:
            {
                Address address = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
                Parameter parameters = new Parameter();
                if (address != null)
                {
                    parameters.put(KEY_O_SELECTED_ADDRESS, address);
                }
                parameters.put(KEY_B_IS_FROM_ONE_BOX, true);
                this.postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, parameters);
                break;
            }
            case STATE_CHECK_IS_SEARCH_DIRECTLY:
            {
                AbstractCommonController superController = (AbstractCommonController)this.getSuperController();
                if(superController instanceof MapController)
                {
                    AbstractCommonController supersuperController = (AbstractCommonController)superController.getSuperController();
                    if(supersuperController instanceof OneBoxSearchController
                            && supersuperController.getSuperController() != null)
                    {
                        this.superController = supersuperController.getSuperController();
                        supersuperController.release();
                        superController.release();
                    }
                }
                break;
            }
            case STATE_VALIDATE_ADDRESS:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAddressValidatorController(this, model.getString(KEY_S_ADDRESS_LINE), model.getString(KEY_S_CITY), model.getString(KEY_S_MESSAGE_ADDRESS), userProfileProvider, null);
                break;
            }
            case STATE_CHECK_VALIDATE_ADDRESS:
            {
                Address address = (Address)model.fetch(KEY_O_VALIDATED_ADDRESS);
                address.setSource(Address.SOURCE_PREDEFINED);
                if(this.model.getBool(KEY_B_DETAIL_FROM_CONTACT))
                {
                    address.setLabel(this.model.getString(KEY_S_LABEL_FROM_CONTACT));
                    address.setPhoneNumber(this.model.getString(KEY_S_PTN_FROM_CONTACT));
                    if(address.getStop()!=null)
                    {
                        address.getStop().setLabel(this.model.getString(KEY_S_LABEL_FROM_CONTACT));
                    }              
                    PoiDataWrapper poiDataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
                    poiDataWrapper.addStopAddress(address);
                    poiDataWrapper.addNormalAddr(address);
                    
                    ModuleFactory.getInstance().startDetailController(this, TYPE_DETAIL_FROM_CONTACT, model.getInt(KEY_I_AC_TYPE), 0, poiDataWrapper,
                        (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER), model.getBool(KEY_B_IS_SEARCH_NEAR_BY));
                    break;
                }
                else if (this.model.getInt(KEY_I_AC_TYPE) != TYPE_AC_FROM_MAP && this.model.getInt(KEY_I_AC_TYPE) != TYPE_AC_FROM_NAV)
                {
                    Parameter data = new Parameter();
                    data.put(KEY_O_SELECTED_ADDRESS, address);
                    data.put(KEY_B_IS_FROM_ONE_BOX, true);
                    postControllerEvent(EVENT_CONTROLLER_ADDRESS_SELECTED, data);
                }
                else
                {
                    String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_ADDRESS, address,searchText, userProfileProvider);
                }
                break;
            }
            case STATE_GOT_ONE_ADDRESS:
            {
                Address address =   (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
                startControllerByAddress(address);
                break;
            }
            case STATE_ONEBOX_GO_TO_SET_HOME:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_HOME_ADDRESS, userProfileProvider);
                break;
            }
            case STATE_ONEBOX_GO_TO_SET_WORK:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_WORK_ADDRESS, userProfileProvider);
                break;
            }
        }
    }

    private void startControllerByAddress(Address address)
    {
        if(address ==null)
            return;
        boolean isFromMap = this.model.getBool(ICommonConstants.KEY_B_IS_FROM_MAP);
        if (isFromMap)
        {
            String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
            IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
            int addressValidationfrom = this.model.getInt(KEY_I_TYPE_ADDRESS_VALIDATOR_FROM);
            if (addressValidationfrom == TYPE_FROM_POI)
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_ONEBOX_POI, poiDataWrapper, searchText,
                    model.getInt(KEY_I_AC_TYPE), userProfileProvider);
            }
            else
            {
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_ADDRESS, address, searchText,
                    userProfileProvider);
            }
        }
        else
        {
            PoiDataWrapper newpoiDataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
            newpoiDataWrapper.addStopAddress(address);
            newpoiDataWrapper.addNormalAddr(address);
            ModuleFactory.getInstance().startDetailController(this, TYPE_DETAIL_FROM_FAVORITE,
                model.getInt(KEY_I_AC_TYPE), 0, newpoiDataWrapper,
                (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER),
                model.getBool(KEY_B_IS_SEARCH_NEAR_BY));
        }
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
    
    protected AbstractView createView()
    {
        OneBoxSearchUiDecorator uiDecorator = new OneBoxSearchUiDecorator();
        OneBoxSearchViewTouch oneBoxSearchViewTouch = new OneBoxSearchViewTouch(uiDecorator);
        return oneBoxSearchViewTouch;
    }

    protected AbstractModel createModel()
    {
        OneBoxSearchModel oneBoxSearchModel = new OneBoxSearchModel();
        return oneBoxSearchModel;
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
}
