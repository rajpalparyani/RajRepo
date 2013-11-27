/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiDetailController.java
 *
 */
package com.telenav.module.poi.detail;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.ModuleFactory;
import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.module.map.MapController;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.kontagent.KontagentLogger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-24
 */
public class PoiDetailController extends BrowserSdkController implements IPoiDetailConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_BROWSER_INIT, ACTION_INIT },
        { STATE_BROWSER_INIT, EVENT_MODEL_LAUNCH_BROWSER, STATE_POI_DETAIL, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_GOTO_AC, STATE_BROWSER_GOTO_AC, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_GOTO_MAP, STATE_BROWSER_GOTO_MAP, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_GOTO_NAV, STATE_BROWSER_GOTO_NAV, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_UPDATE_MAIN, STATE_POI_DETAIL, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_GOTO_SHARE_ADDR, STATE_BROWSER_GOTO_SHARE_ADDR, ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },
        { STATE_BROWSER_GOTO_SHARE_ADDR, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },
        { STATE_ANY, EVENT_MODEL_GOTO_SEARCH_NEARBY, STATE_BROWSER_GOTO_SEARCH_NEARBY, ACTION_NONE },
        { STATE_BROWSER_GOTO_AC, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_PREV, ACTION_AC_ADDRESS },
        { STATE_ANY, EVENT_MODEL_LAUNCH_LOCALAPP, STATE_BROWSER_GOTO_LAUNCH_LOCALAPP, ACTION_NONE },
        { STATE_ANY, EVENT_CONTROLLER_SHOW_POI_DETAIL, STATE_POI_DETAIL, ACTION_RELOAD_GALLERY},
        { STATE_ANY, EVENT_MODEL_LAUNCH_SETTING, STATE_BROWSER_GOTO_LAUNCH_SETTING, ACTION_NONE },
        { STATE_POI_DETAIL, CMD_POI_DETAIL_FEEDBACK, STATE_POI_DETAIL_FEEDBACK, ACTION_NONE },
        { STATE_POI_DETAIL_FEEDBACK, EVENT_MODEL_EXIT_BROWSER, STATE_PREV, ACTION_NONE },
        { STATE_POI_DETAIL, CMD_MAP_POI_PAGE_NEXT_NETWORK, STATE_GETTING_MORE_POIS, ACTION_GETTING_MORE_POIS },
        { STATE_GETTING_MORE_POIS, CMD_COMMON_BACK, STATE_VIRGIN, ACTION_CANCEL_GETTING_MORE_POIS },
        { STATE_GETTING_MORE_POIS, EVENT_MODEL_BACK_TO_MAIN, STATE_PREV, ACTION_NONE },
        { STATE_GETTING_MORE_POIS, EVENT_MODEL_GET_MORE_POIS, STATE_PREV, ACTION_NONE },
        
    };

    public PoiDetailController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_BROWSER_GOTO_MAP:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS, KontagentLogger.POIINFO_MAP_CLICKED);
                Vector maps = getControllerInStack(MapController.class.getName());

                boolean isFromPoiMap = false;
                if (maps != null)
                {
                    for (int i = 0; i < maps.size(); i++)
                    {
                        MapController mapController = (MapController) maps.elementAt(i);
                        int mapFromType = mapController.getModel().getInt(
                            KEY_I_TYPE_MAP_FROM);
                        if (mapFromType == TYPE_MAP_FROM_POI
                                || mapFromType == TYPE_MAP_FROM_SPECIFIC_POI
                                || mapFromType == TYPE_MAP_FROM_BROWSER
                                || mapFromType == TYPE_MAP_FROM_ONEBOX_POI)
                        {
                            isFromPoiMap = true;
                            break;
                        }
                    }
                }
                boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                if(dataWrapper != null)
                {
                    String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                    searchText = searchText == null ? dataWrapper.getShowText() : searchText;
                    int acType = model.getInt(KEY_I_AC_TYPE);
                    PoiMisLogHelper.getInstance().setIsMapResulstMode(false);
                    int mapFromType = TYPE_MAP_FROM_BROWSER;
                    if(isFromPoiMap)
                    {
                        Parameter param = new Parameter();
                        param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
                        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
                        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
                        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
                        param.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                        param.put(ICommonConstants.KEY_B_NEED_RESET_ZOOMLEVEL, true);
                        this.postControllerEvent(EVENT_CONTROLLER_GO_TO_POI_MAP, param);
                    }
                    else
                    {
                        int detailFromType = this.model.getInt(KEY_I_TYPE_DETAIL_FROM);
                        IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                        ModuleFactory.getInstance().startMapController(this, detailFromType, mapFromType,
                            (Address) model.get(ICommonConstants.KEY_O_SELECTED_ADDRESS), dataWrapper, searchText, acType,
                            userProfileProvider, isSearchNearBy);
                    }
                }
                else
                {
                    Address addr = (Address) model.get(ICommonConstants.KEY_O_SELECTED_ADDRESS);
                    String searchText = model.getString(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT);
                    int mapFromType = TYPE_MAP_FROM_BROWSER;
                    if(isFromPoiMap)
                    {
                        Parameter param = new Parameter();
                        param.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, addr);
                        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
						param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
                        this.postControllerEvent(EVENT_CONTROLLER_GO_TO_POI_MAP, param);
                    }
                    else
                    {
//                        PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                        IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
//                        if (dataWrapper != null)
//                        {
//                            ModuleFactory.getInstance().startMapController(this, mapFromType, addr,searchText, userProfileProvider);
//                        }
                        ModuleFactory.getInstance().startMapController(this, mapFromType, addr,searchText, userProfileProvider);
                    }
                }
                break;
            }
            default:
            {
                super.postStateChangeDelegate(currentState, nextState);
            }
        }
    }
    
    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE );
    }

    protected AbstractModel createModel()
    {
        return new PoiDetailModel();
    }

    protected AbstractView createView()
    {
        PoiDetailUiDecorator uiDecorator = new PoiDetailUiDecorator();
        return new PoiDetailViewTouch(uiDecorator);
    }

}
