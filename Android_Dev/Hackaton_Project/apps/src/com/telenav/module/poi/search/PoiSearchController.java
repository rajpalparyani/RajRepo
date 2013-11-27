/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceConstants.java
 *
 */
package com.telenav.module.poi.search;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.module.ModuleFactory;
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
 *@date 2010-8-18
 */
public class PoiSearchController extends AbstractCommonController implements IPoiSearchConstants
{
    private final static int[][] STATE_TABLE = new int[][]{
        {STATE_VIRGIN,            EVENT_CONTROLLER_START,           STATE_SEARCH_QUICK_FIND,    ACTION_CHECK_CATEGORY},
        //do search poi
        {STATE_SEARCH_QUICK_FIND, CMD_COMMON_DSR,                   STATE_GOTO_DSR,             ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_POISEARCH_REMOVE_TEXT,        STATE_SEARCH_QUICK_FIND,    ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_QUICK_BUTTON_SELECTED,        STATE_SEARCH_QUICK_FIND,    ACTION_DOING_SEARCH},
        {STATE_SEARCH_QUICK_FIND, EVENT_MODEL_SHOW_PROGRESS,        STATE_POI_SEARCHING,        ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_GOTO_SEARCH_CATEGORIES,       STATE_SEARCH_CATEGORIES,    ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_GOTO_SUB_CATELOG,             STATE_SUB_CATELOG,          ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_GOTO_ROUTE_SUMMARY,           STATE_GOTO_ROUTE_SUMMARY,     ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_GOTO_NAV,                     STATE_GO_TO_MOVING_MAP,             ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_GOTO_TURN_MAP,                STATE_GO_TO_TURN_MAP,             ACTION_NONE},
        {STATE_SEARCH_QUICK_FIND, CMD_EVENT_SELECTED,               STATE_CHECK_ANCHOR_BEFORE_SEARCH_EVENT,       ACTION_CHECK_ANCHOR},
        {STATE_CHECK_ANCHOR_BEFORE_SEARCH_EVENT, EVENT_MODEL_GO_TO_LOCAL_EVENT,               STATE_GO_TO_LOCAL_EVENT,       ACTION_NONE},
        {STATE_CHECK_ANCHOR_BEFORE_SEARCH_EVENT, EVENT_MODEL_SHOW_PROGRESS,               STATE_POI_SEARCHING,       ACTION_NONE},
        {STATE_POI_SEARCHING, EVENT_MODEL_GO_TO_LOCAL_EVENT,               STATE_GO_TO_LOCAL_EVENT,       ACTION_NONE},
        {STATE_POI_SEARCHING,     EVENT_MODEL_GOTO_POI_LIST,        STATE_GOTO_RESULT_LIST,     ACTION_NONE},
        {STATE_POI_SEARCHING,     EVENT_MODEL_BACK,                 STATE_PREV,                 ACTION_NONE},
        {STATE_POI_SEARCHING,     CMD_COMMON_BACK,                  STATE_PREV,                 ACTION_CANCEL_SEARCH},
        {STATE_SEARCH_CATEGORIES, CMD_SELECT_CATEGORY_LIST,         STATE_POI_SEARCHING,        ACTION_DOING_SEARCH},
        {STATE_SUB_CATELOG,       CMD_SELECT_CATEGORY_LIST,         STATE_POI_SEARCHING,        ACTION_DOING_SEARCH},
        {STATE_POI_SEARCHING,     EVENT_MODEL_GOTO_NO_GPS_WARNING,  STATE_NO_GPS_WARNING,       ACTION_NONE},
    };
    
    public PoiSearchController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {   
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
                
                Vector results = getControllerInStack(PoiResultController.class.getName());

                boolean isFromPoiList = false;
                if (results != null && results.size() > 0)
                {
                    isFromPoiList = true;
                }
                
                boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                
                if(isFromPoiList && !isChangeLocation)
                {
                    Parameter data = new Parameter();
                    if(searchType < 0)
                    {
                        searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
                    }
                    if(alongRouteType < 0 && searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
                    {
                        alongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD; 
                    }
                    data.put(ICommonConstants.KEY_I_POI_SELECTED_INDEX, selectedIndex);
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
                    data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChangeLocation);
                    data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                    this.postControllerEvent(EVENT_CONTROLLER_SHOW_POI_LIST, data);
                }
                else
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startPoiResultController(this, selectedIndex, categoryId,
                        acType, searchType, sortType, alongRouteType, isChangeLocation, showText, 
                        searchString, anchorAddr, destAddr, navState, dataWrapper, userProfileProvider, isSearchNearBy);
                }
                break;
            }
            case STATE_GOTO_DSR:
            {
                int acType = TYPE_AC_FROM_POI;
                int searchType = this.model.getInt(KEY_I_SEARCH_TYPE);
                int searchAlongType = this.model.getInt(KEY_I_SEARCH_ALONG_TYPE);
                Address destAddr = (Address)this.model.get(KEY_O_ADDRESS_DEST);
                NavState navState = (NavState)this.model.get(KEY_O_NAVSTATE);
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startDsrController(this, acType, searchType, searchAlongType, destAddr, navState, userProfileProvider);
                break;
            }
            case STATE_GOTO_ROUTE_SUMMARY:
            {
                postControllerEvent(EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, null);
                break;
            }
            case STATE_GO_TO_MOVING_MAP:
            {
                postControllerEvent(EVENT_CONTROLLER_BACK_TO_MOVING_MAP, null);
                break;
            }
            case STATE_GO_TO_TURN_MAP:
            {
                postControllerEvent(EVENT_CONTROLLER_BACK_TO_TURN_MAP, null);
                break;
            }
            case STATE_GO_TO_LOCAL_EVENT:
            {
                BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.LOCAL_EVENTS_MAIN_DOMAIN_ALIAS);
                String eventId = model.fetchString(KEY_S_LOCAL_EVENT_ID);
                String eventName = model.fetchString(KEY_S_LOCAL_EVENT_NAME);
                ModuleFactory.getInstance().startLocalEventController(this, sessionArgs, eventId, eventName);
                break;
            }
            default:
                break;
        }
    }

    protected AbstractView createView()
    {
        PoiSearchUiDecorator uiDecorator = new PoiSearchUiDecorator();
        return new PoiSearchViewTouch(uiDecorator);
    }

    protected AbstractModel createModel()
    {
        PoiSearchModel poiSearchModel = new PoiSearchModel();
        return poiSearchModel;
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

}
