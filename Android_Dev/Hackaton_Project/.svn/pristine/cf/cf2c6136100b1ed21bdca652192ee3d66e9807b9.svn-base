/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 *
 */
package com.telenav.module.map;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.logger.Logger;
import com.telenav.module.ModuleFactory;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.entry.EntryController;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.detail.PoiDetailController;
import com.telenav.module.poi.result.PoiResultController;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author JY Xu
 *@date Aug 21, 2010
 */

public class MapController extends AbstractCommonController implements IMapConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
            // init map
            { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_MAIN, ACTION_MAP_INIT },
            { STATE_MAIN, CMD_COMMON_BACK, STATE_MAIN, ACTION_BACK_CHECK },

            { STATE_MAIN, CMD_POI_LIST, STATE_GOTO_POI_LIST, ACTION_NONE },
            { STATE_MAIN, CMD_MAP_POI_GOTO_NAV, STATE_GOTO_NAV, ACTION_NONE },
            { STATE_MAIN, CMD_MAP_POI_GOTO_POI_DETAIL, STATE_GOTO_POI_DETAIL, ACTION_NONE },
            { STATE_MAIN, CMD_MAP_POI_GOTO_SHARE, STATE_GOTO_SHARE, ACTION_NONE },
            { STATE_GOTO_SHARE, EVENT_CONTROLLER_NATIVE_SHARE_CANCEL, STATE_PREV, ACTION_NONE },
            { STATE_GOTO_SHARE, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_SHOW_TIMEOUT_MESSAGE },

            { STATE_MAIN, CMD_MAP_POI_GOTO_CALL, STATE_MAIN, ACTION_CALL },
            { STATE_MAIN, CMD_ROUTE_SUMMARY, STATE_GOTO_ROUTE_SUMMARY, ACTION_NONE },
            { STATE_MAIN, CMD_DIRECTIONS, STATE_LINK_TO_DIRECTIONS, ACTION_NONE },
            { STATE_MAIN, CMD_NAVIGATION, STATE_LINK_TO_NAVIGATION, ACTION_NONE },
            { STATE_MAIN, CMD_MAP_RGC, STATE_GETTING_RGC, ACTION_GET_RGC },
            { STATE_GETTING_RGC, EVENT_MODEL_GOT_RGC, STATE_MAIN, ACTION_NONE},
            
            { STATE_MAIN, CMD_MAP_ADDRESS_GOTO_ONE_SEARCH_BOX, STATE_GOTO_ONE_SEARCH_BOX, ACTION_NONE },
            { STATE_MAIN, CMD_COMMON_GOTO_ONEBOX, STATE_GOTO_ONE_SEARCH_BOX, ACTION_NONE },
            { STATE_MAIN, CMD_MAP_ADDRESS_GOTO_SAVE_FAVORITE, STATE_GOTO_SAVE_FAVORITE, ACTION_NONE },
            { STATE_GOTO_SAVE_FAVORITE, CMD_COMMON_OK, STATE_PREV, ACTION_NONE},
            { STATE_MAIN, CMD_MAP_ADDRESS_GOTO_SHARE, STATE_GOTO_SHARE, ACTION_NONE },
            { STATE_MAIN, CMD_MAP_ADDRESS_GOTO_NAV, STATE_GOTO_NAV, ACTION_NONE },
            
            { STATE_MAIN, CMD_SHOW_TRAFFIC_DETAIL, STATE_SHOW_TRAFFIC_DETAIL, ACTION_NONE },
            { STATE_SHOW_TRAFFIC_DETAIL, CMD_TRAFFIC_DETAIL_OK, STATE_PREV, ACTION_NONE},
            { STATE_MAIN, EVENT_MODEL_POI_MAP_BACK, STATE_BACK_TO_POI_DETAIL, ACTION_NONE},
            
            { STATE_MAIN, CMD_COMMON_CHANGE_LOCATION, STATE_CHANGE_LOCATION, ACTION_NONE },
            
            { STATE_MAIN, CMD_MAP_POI_PAGE_NEXT_NETWORK, STATE_GETTING_MORE_POIS, ACTION_GETTING_MORE_POIS },
            
            { STATE_MAIN, CMD_CLEAR, STATE_CLEAR, ACTION_CLEAR },
            { STATE_CLEAR, EVENT_MODEL_CLEAR, STATE_MAIN, ACTION_NONE },
            
            { STATE_ANY, CMD_SHOW_MAP_LAYER, STATE_ANY, ACTION_CHECK_LAYER },
            { STATE_ANY, EVENT_MODEL_GOTO_TRAFFIC_UPSELL, STATE_UPSELL, ACTION_NONE },

            { STATE_CHANGE_LOCATION, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_MAIN, ACTION_ADDRESS_SELECTED },
            { STATE_COMMON_GOTO_ONEBOX, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_MAIN, ACTION_ADDRESS_SELECTED },
            //fix bug http://jira.telenav.com:8080/browse/TNANDROID-96
            { STATE_GOTO_ONE_SEARCH_BOX, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_MAIN, ACTION_ADDRESS_SELECTED },
            //fix bug http://jira.telenav.com:8080/browse/TNANDROID-2849
            { STATE_GOTO_POI_LIST, EVENT_CONTROLLER_ADDRESS_SELECTED, STATE_MAIN, ACTION_ADDRESS_SELECTED },
            
            // map summary part
            { STATE_ANY, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, STATE_INIT_MAP_SUMMARY, ACTION_INIT_MAP_SUMMARY },
            { STATE_INIT_MAP_SUMMARY, EVENT_MODEL_GET_DECIMATE_ROUTE, STATE_GETTING_DECIMATE_ROUTE, ACTION_NONE },
            { STATE_GETTING_DECIMATE_ROUTE, EVENT_MODEL_SHOW_SUMMARY, STATE_MAP_SUMMARY, ACTION_NONE },
            { STATE_GETTING_DECIMATE_ROUTE, CMD_COMMON_BACK, STATE_BACK_TO_SUMMARY_CONTROLLER, ACTION_NONE },
            
            { STATE_GETTING_MORE_POIS, EVENT_MODEL_GET_MORE_POIS, STATE_MAIN, ACTION_NONE },
            { STATE_GETTING_MORE_POIS, CMD_COMMON_BACK, STATE_PREV, ACTION_CANCEL_GETTING_MORE_POIS },
            { STATE_GETTING_MORE_POIS, EVENT_MODEL_BACK_TO_MAIN, STATE_PREV, ACTION_NONE },
            
            { STATE_INIT_MAP_SUMMARY, EVENT_MODEL_SHOW_SUMMARY, STATE_MAP_SUMMARY, ACTION_CHECK_ROUTE_INTEGRITY },
            { STATE_MAP_SUMMARY, EVENT_MODEL_GET_MORE_ROUTE, STATE_GETTING_MORE_ROUTE, ACTION_NONE },
            { STATE_GETTING_MORE_ROUTE, EVENT_MODEL_GET_MORE_ROUTE_FINISHED, STATE_MAP_SUMMARY, ACTION_NONE },
            { STATE_MAP_SUMMARY, CMD_ROUTE_SUMMARY, STATE_GOTO_ROUTE_SUMMARY, ACTION_NONE },
            { STATE_MAP_SUMMARY, CMD_TRAFFIC_SUMMARY, STATE_GOTO_TRAFFIC_SUMMARY, ACTION_NONE },
            { STATE_MAP_SUMMARY, CMD_COMMON_LINK_TO_SEARCH, STATE_GOTO_SEARCH, ACTION_NONE },
            { STATE_MAP_SUMMARY, CMD_DIRECTIONS, STATE_VIRGIN, ACTION_NONE },
            { STATE_MAP_SUMMARY, CMD_NAVIGATION, STATE_VIRGIN, ACTION_NONE },
            
            { STATE_MAP_SUMMARY, CMD_SHOW_TRAFFIC_DETAIL, STATE_SHOW_TRAFFIC_DETAIL, ACTION_NONE },
            
            // override default flow for map from dsr.
            { STATE_ANY, EVENT_CONTROLLER_DSR_MAP, STATE_MAIN, ACTION_MAP_ADDRESS }, 
            { STATE_ANY, EVENT_CONTROLLER_GO_TO_POI_MAP, STATE_MAIN, ACTION_NONE }, 
            { STATE_ANY, EVENT_CONTROLLER_MAP_VBB, STATE_MAIN, ACTION_NONE }, 
            { STATE_ANY, EVENT_CONTROLLER_BACK_FROM_VBB_DETAIL, STATE_MAIN, ACTION_NONE }, 
            
            //JY: only state main can trigger upsell?
            { STATE_ANY, EVENT_CONTROLLER_UPSELL_PURCHASE_FINISH, STATE_MAIN, ACTION_CHECK_UPSELL_SUCCESS}, 
            { STATE_MAIN, EVENT_MODEL_POI_LIST, STATE_GOTO_POI_LIST, ACTION_NONE },        
    };

    public MapController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_GOTO_SEARCH:
            {
                postControllerEvent(EVENT_CONTROLLER_GOTO_POI, null);
                break;
            }
            case STATE_GOTO_NAV:
            {
                int searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
                boolean isFromSearchAlong = false;
                PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                if (poiDataManager != null)
                {
                    searchType = poiDataManager.getSearchType();
                }
                
                if(searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
                {
                    isFromSearchAlong = true;
                }
                
                int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);

                if (mapFromType == TYPE_MAP_FROM_BILLBOARD)
                {
                    isFromSearchAlong = true;
                }
                
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_DRIVE_TO,
                    this.model.getInt(KEY_I_POI_SELECTED_INDEX));
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
                ModuleFactory.getInstance().startNavController(this, null, (Address) model.get(KEY_O_SELECTED_ADDRESS), poiDataWrapper, false, 
                    model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), isFromSearchAlong, false);
                break;
            }
            case STATE_GOTO_POI_DETAIL:
            {
                Vector poiDetails = getControllerInStack(PoiDetailController.class.getName());

                boolean isFromPOI = false;
                if (poiDetails != null)
                {
                    for (int i = 0; i < poiDetails.size(); i++)
                    {
                        PoiDetailController poiDetailController = (PoiDetailController) poiDetails.elementAt(i);
                        int detailFromType = poiDetailController.getModel().getInt(
                            KEY_I_TYPE_DETAIL_FROM);
                        if (detailFromType == TYPE_DETAIL_FROM_POI)
                        {
                            isFromPOI = true;
                            break;
                        }
                    }
                }
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                if (dataWrapper != null)
                {
                    int acType = TYPE_AC_FROM_MAP;
                    this.model.put(KEY_B_NEED_RESTORE_POI_ANNOTATION, true);
                    this.model.put(KEY_B_NEED_UPDATE_FLAGS, true);
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                    if (isFromPOI)
                    {
                        Parameter param = new Parameter();
                        param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
                        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
                        param.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
                        param.put(ICommonConstants.KEY_I_POI_SELECTED_INDEX, dataWrapper.getSelectedIndex());
                        this.postControllerEvent(EVENT_CONTROLLER_SHOW_POI_DETAIL, param);
                    }
                    else
                    {
                        ModuleFactory.getInstance().startDetailController(this, acType, dataWrapper.getSelectedIndex(), dataWrapper, userProfileProvider, isSearchNearBy);
                    }
                }
                else
                {
                    if (model.getInt(KEY_I_TYPE_MAP_FROM) == TYPE_MAP_FROM_BILLBOARD)
                    {
                        Address destAddr = (Address)this.model.get(KEY_O_ADDRESS_DEST);
                        String vbbUrl = model.getString(KEY_S_AD_DETAIL_URL);
                        ModuleFactory.getInstance().startVbbDetailController(this, vbbUrl, destAddr);
                    }
                    else
                    {
                        Logger.log(Logger.ERROR, this.getClass().getName(), "==== MapController ==== GOTO POI DETAIL " +
                        		"[KEY_I_TYPE_MAP_FROM=" + model.getInt(KEY_I_TYPE_MAP_FROM) + "] " +
                        				"==== no handle here in this case !!!");
                    }
                }
                break;
            }
            case STATE_GOTO_SHARE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startNativeShareController(this, (Address) this.model.get(KEY_O_SELECTED_ADDRESS), userProfileProvider);
                break;
            }
            case STATE_GOTO_ROUTE_SUMMARY:
            {
//              boolean isSearchAlongMap = false;
//              int searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
//              PoiDataWrapper poiDataManager = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
//              if (poiDataManager != null)
//              {
//                  searchType = poiDataManager.getSearchType();
//              }
//              int acType = model.getInt(KEY_I_AC_TYPE);
//              if(acType == TYPE_AC_FROM_TURN_MAP || searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
//              {
//                  isSearchAlongMap = true;
//              }
//              
//              if(isSearchAlongMap)
//              {
//                  postControllerEvent(EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, null);
//              }
//              else
//              {
//                  if (superController != null)
//                  {
//                      Parameter param = new Parameter();
//                      param.put(KEY_O_CURRENT_CONTROLLER, this);
//                      superController.activate(EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, param);
//                      this.deactivate();
//                  }
//              }
//              break;
              int mapFromType = model.getInt(KEY_I_TYPE_MAP_FROM);
              if (superController != null && mapFromType == TYPE_MAP_FROM_SUMMARY)
              {
                  Parameter param = new Parameter();
                  param.put(KEY_O_CURRENT_CONTROLLER, this);
                  superController.activate(EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, param);
                  this.deactivate();
              }
              else
              {
                  postControllerEvent(EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, null);
              }
              break;
            }
               
            case STATE_GOTO_TRAFFIC_SUMMARY:
            {
                if (superController != null)
                {
                    Parameter param = new Parameter();
                    param.put(KEY_O_CURRENT_CONTROLLER, this);
                    superController.activate(EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY, param);
                }
                break;
            }
            case STATE_BACK_TO_SUMMARY_CONTROLLER:
            {
                postControllerEvent(EVENT_CONTROLLER_BACK_TO_SUMMARY_CONTROLLER, null);
                break;
            }
            case STATE_CHANGE_LOCATION:
            {
                AbstractController controller = this.getSuperController();
                while (!(controller instanceof MapController))
                {
                    controller = controller.getSuperController();
                    if(controller == null)
                    {
                        break;
                    }
                }
                if(controller instanceof MapController)
                {
                    controller = this.getSuperController();
                    while (!(controller instanceof MapController))
                    {
                        controller = controller.getSuperController();
                        controller.release();
                    }
                    this.superController = controller.getSuperController();
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startAcController(this, TYPE_AC_FROM_MAP, true, !model.fetchBool(KEY_B_IS_CURRENT_LOCATION), false, userProfileProvider);
                break;
            }
            case STATE_GOTO_POI_LIST:
            {
                Vector results = getControllerInStack(PoiResultController.class.getName());

                boolean isFromPoiList = false;
                if (results != null && results.size() > 0)
                {
                    isFromPoiList = true;
                }
                
                int categoryId = this.model.getInt(KEY_I_CATEGORY_ID);
                int sortType = this.model.getInt(KEY_I_SEARCH_SORT_TYPE);
                int selectedIndex = this.model.getInt(KEY_I_POI_SELECTED_INDEX);
                int acType = TYPE_AC_FROM_MAP;
                int searchType = this.model.getInt(KEY_I_SEARCH_TYPE);
                int alongRouteType = this.model.getInt(KEY_I_SEARCH_ALONG_TYPE);
                String showText = this.model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                String searchString = this.model.getString(KEY_S_COMMON_SEARCH_TEXT);
                Address anchorAddr = (Address)this.model.get(KEY_O_ADDRESS_ORI);
                Address destAddr = (Address)this.model.get(KEY_O_ADDRESS_DEST);
                NavState navState = (NavState)this.model.get(KEY_O_NAVSTATE);
                boolean isChangeLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
                PoiDataWrapper dataWrapper = (PoiDataWrapper)this.model.get(KEY_O_POI_DATA_WRAPPER);
//                String searchText = this.model.getString(KEY_S_COMMON_SEARCH_TEXT);
                boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                if(isFromPoiList)
                {
                    Parameter data = new Parameter();
//                    if(searchType < 0)
//                    {
//                        searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
//                    }
//                    if(alongRouteType < 0 && searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
//                    {
//                        alongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD; 
//                    }
//                    data.put(ICommonConstants.KEY_I_POI_SELECTED_INDEX, selectedIndex);
//                    data.put(ICommonConstants.KEY_I_CATEGORY_ID, categoryId);
//                    data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
//                    data.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
//                    data.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
//                    data.put(ICommonConstants.KEY_O_ADDRESS_ORI, anchorAddr);
//                    data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddr);
//                    data.put(ICommonConstants.KEY_O_NAVSTATE, navState);
                    data.put(ICommonConstants.KEY_S_COMMON_SEARCH_TEXT, searchString);
                    data.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, showText);
//                    data.put(ICommonConstants.KEY_I_SEARCH_SORT_TYPE, sortType);
                    data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
                    data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
//                    data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChangeLocation);
                    postControllerEvent(EVENT_CONTROLLER_MAP_TO_POI_LIST, data);
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
            case STATE_UPSELL:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startUpSellController(this, this.model.fetchString(KEY_S_FEATURE_CODE), false, userProfileProvider);
                break;
            }
            case STATE_GOTO_ONE_SEARCH_BOX:
            {
                int acType = ICommonConstants.TYPE_AC_FROM_MAP;
                int searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
                boolean isSearchNearByClicked = model.fetchBool(KEY_B_IS_SEARCH_NEAR_BY_CLICKED);
                int alongRouteType = -1;
                boolean isChangeLocation = false;
                String initText = "";
                Address anchorAddr;
                if (isSearchNearByClicked)
                {
                    anchorAddr = (Address) model.get(KEY_O_SELECTED_ADDRESS);
                }
                else
                {
                    anchorAddr = (Address) model.get(KEY_O_ADDRESS_ORI);
                }
                Address destAddr = null;
                NavState navState = null;
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                if(userProfileProvider == null)
                {
                    userProfileProvider = new UserProfileProvider();
                }
                boolean isSearchNearBy = isSearchNearByClicked | model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
                ModuleFactory.getInstance().startOneBoxController(this, acType, searchType, alongRouteType, initText, false, isChangeLocation,  anchorAddr,
                    destAddr, navState, userProfileProvider, isSearchNearBy, false, true);
                break;
            }
            case STATE_GOTO_SAVE_FAVORITE:
            {
                Address anchorAddr = (Address) model.get(KEY_O_SELECTED_ADDRESS);                
                if(anchorAddr != null)
                {
                    if (anchorAddr.getStatus() == CommonDBdata.DELETED)
                    {
                        anchorAddr.setStatus(CommonDBdata.UNCHANGED);
                        return;
                    }
                
                
                	byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(anchorAddr);
                	Address newAddress = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
                	if(newAddress.getPoi() != null)
                	{
                	    newAddress.setType(Address.TYPE_FAVORITE_POI);
                	}
                	else
                	{
                	    newAddress.setType(Address.TYPE_FAVORITE_STOP);
                	}
                	
                	newAddress.setId(0);
                	
                	DaoManager.getInstance().getAddressDao().checkAddressLabel(newAddress);
                	
                	boolean isFavorite = DaoManager.getInstance().getAddressDao().isExistInFavoriteAddress(newAddress, true);
                	if(!isFavorite)
                	{
                	    newAddress.setStatus(CommonDBdata.ADDED);
                	    newAddress.setCreateTime(System.currentTimeMillis());
	                    DaoManager.getInstance().getAddressDao().addAddress(newAddress, false);
	                    DaoManager.getInstance().getAddressDao().store();
                	}
                	else
                	{
                	    DaoManager.getInstance().getAddressDao().removeAddressFromCategory(newAddress, AddressDao.RECEIVED_ADDRESSES_CATEGORY);
                		DaoManager.getInstance().getAddressDao().deleteAddress(newAddress);
	                    DaoManager.getInstance().getAddressDao().store();
                	}
                }
                break;
            }
            case STATE_LINK_TO_DIRECTIONS:
            {
                postControllerEvent(EVENT_CONTROLLER_GO_TO_TURN_MAP, null);
                break;
            }
            case STATE_LINK_TO_NAVIGATION:
            {
                postControllerEvent(EVENT_CONTROLLER_GO_TO_MOVING_MAP, null);
                break;
            }
            case STATE_CLEAR:
            {
                AbstractController controller = this.getSuperController();
                while (controller != null)
                {
                    if(controller instanceof EntryController)
                    {
                        model.put(KEY_I_TYPE_MAP_FROM, TYPE_MAP_FROM_ENTRY);
                        break;
                    }
                    if(controller.getSuperController() != null)
                    {
                        if(!(controller instanceof MapController))
                        {    
                            controller.release();
                        }
                        controller = controller.getSuperController();
                    }
                }
                this.superController = controller;
                break;
            }
//            case STATE_BACK_TO_POI_DETAIL:
//            {
//                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
//                Parameter param = new Parameter();
//                param.put(KEY_O_POI_DATA_WRAPPER, dataWrapper);
//                param.put(KEY_I_POI_SELECTED_INDEX, dataWrapper.getSelectedIndex());
//                postControllerEvent(EVENT_CONTROLLER_SHOW_POI_DETAIL, param);
//                break;
//            }
        }
    }

    protected Address getCopyOfDestAddress(Address destAddress)
    {
        if(destAddress == null)
        {
            return null;
        }
        byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(destAddress);
        Address newAddress = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
        newAddress.setType(Address.TYPE_FAVORITE_STOP);
        newAddress.setSource(destAddress.getSource());
        return newAddress;
    }
    
    protected AbstractModel createModel()
    {
        return new MapModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new MapViewTouch(new MapUiDecorator());
    }

}
