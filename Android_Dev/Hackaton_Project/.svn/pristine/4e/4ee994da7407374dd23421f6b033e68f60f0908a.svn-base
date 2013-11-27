/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiResultModel.java
 *
 */
package com.telenav.module.poi.result;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.log.mis.log.SortRequestMislog;
import com.telenav.logger.Logger;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.map.MapController;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-9-25
 */
class PoiResultModel extends BrowserSdkModel implements INetworkStatusListener, IPoiResultConstants, PoiDataListener, LocationListener
{
    private PoiDataRequester poiDataRequester;
    public static final long NO_GPS_TIME_OUT = 12*1000;
    
    protected void init()
    {
        NetworkStatusManager.getInstance().addStatusListener(this); 
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_POI_RESULT_START_INIT:
            {
                this.postModelEvent(EVENT_MODEL_POI_RESULT_LIST);
                this.put(KEY_I_CHANGETO_ALONGROUTE_TYPE, IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD);
                init();
                PoiMisLogHelper.getInstance().startImpression();
                initCurrentStop();
                break;
            }
            case ACTION_ENTRY_CHECKING:
            {
                int acType = this.getInt(KEY_I_AC_TYPE);
                boolean isChangeLocation = this.getBool(KEY_B_IS_CHOOSING_LOCATION);
                // [NingYang]Per 6.2, search along should also go into detail.
                if (isChangeLocation ||(acType != TYPE_AC_FROM_MAP && acType != TYPE_AC_FROM_TURN_MAP
                        && acType != TYPE_AC_FROM_NAV && acType != TYPE_AC_FROM_DSR && acType != TYPE_AC_FROM_ENTRY && acType != TYPE_AC_FROM_ONE_BOX && acType != -1))
                {
                    postModelEvent(EVENT_MODEL_RETURN_POIS);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_GOTO_DETAIL);
                }
                break;
            }
            case ACTION_SORT_CHANGE_SEARCH:
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                poiDataWrapper = poiDataWrapper.cloneInstance(poiDataWrapper.getSearchUid());
                put(KEY_I_LAST_SHOWN_INDEX, 0);
                int changeToSortType = this.getInt(KEY_I_CHANGETO_SORT_TYPE);
                int sortType = this.getInt(KEY_I_SEARCH_SORT_TYPE);
                if (changeToSortType != sortType && changeToSortType != -1)
                {
                    //update the value after resort success.
                    this.put(KEY_B_NO_NEED_UPDATE_SCROLL, true);
                    poiDataWrapper.setSortType(changeToSortType);
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                    poiDataRequester = new PoiDataRequester(userProfileProvider);
                    poiDataRequester.doRequestPoi(poiDataWrapper, this);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_SHOW_RESEARCH_LIST);
                }

                break;
            }
            case ACTION_GETING_MORE:
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                if (poiDataWrapper.isHasMorePoi() )
                {
                    if (!poiDataWrapper.isDoingRequest())
                    {
                        int size = poiDataWrapper.getNormalAddressSize();
                        int index = 0;
                        if (poiDataWrapper.isHasSponsored())
                        {
                            index = size + size / PoiDataRequester.DEFAULT_PAGE_SIZE - 1;
                        }
                        else
                        {
                            index = size - 1;
                        }
                        put(KEY_I_LAST_SHOWN_INDEX, index);
                        poiDataWrapper.setNormalPoiStartIndex(poiDataWrapper.getNormalAddressSize());
                        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                        poiDataRequester = new PoiDataRequester(userProfileProvider);
                        poiDataRequester.doRequestPoi(poiDataWrapper, this);
                        this.put(KEY_B_IS_GET_MORE, true);
                    }
                }
                
                break;
            }
            case ACTION_SEARCH_ALONG:
            {
                int searchAlongType = this.getInt(KEY_I_CHANGETO_ALONGROUTE_TYPE);
                int navRouteType = NavRunningStatusProvider.getInstance().getNavType();
                if(navRouteType == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE 
                        && searchAlongType == IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD)
                {
                    doNearMePoiSearch();
                }
                else
                {
                    PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                    
                    poiDataWrapper = poiDataWrapper.cloneInstance(System.currentTimeMillis() + "");
                    int sortType = this.getInt(KEY_I_SEARCH_SORT_TYPE);
                    poiDataWrapper.setSortType(sortType);
                    put(KEY_I_LAST_SHOWN_INDEX, 0);
                    if(navRouteType == NavRunningStatusProvider.NAV_TYPE_DYNAMIC_ROUTE && (searchAlongType == IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD
                            || searchAlongType == IPoiSearchProxy.TYPE_SEARCH_AROUND_DESTINATION))
                    {
                        poiDataWrapper.setAlongRouteType(searchAlongType);
                        poiDataWrapper.setSearchType(IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE);
                    }
                    else if(navRouteType == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE 
                            && searchAlongType == IPoiSearchProxy.TYPE_SEARCH_AROUND_DESTINATION)
                    {
                        Address destAddress = (Address)this.get(KEY_O_ADDRESS_DEST);
                        if(destAddress != null && destAddress.getStop() != null)
                        {
                            poiDataWrapper.setAnchorStop(destAddress.getStop());
                        }
                        poiDataWrapper.setSearchType(IPoiSearchProxy.TYPE_SEARCH_ADDRESS);
                    }
                    this.put(KEY_B_NO_NEED_UPDATE_SCROLL, true);
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                    poiDataRequester = new PoiDataRequester(userProfileProvider);
                    poiDataRequester.doRequestPoi(poiDataWrapper, this);                
                }
                break;
            }
            case ACTION_CALL_POI:
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                int selectedIndex = this.fetchInt(KEY_I_POI_SELECTED_INDEX);
                if (selectedIndex < 0)
                {
                    selectedIndex = 0;
                }
                poiDataWrapper.setSelectedIndex(selectedIndex);
                Address selectedAddress = poiDataWrapper.getAddress(poiDataWrapper.getSelectedIndex());
                if (selectedAddress != null)
                {
                    PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_CALL_TO, selectedIndex);
                    String phoneNumber = selectedAddress.getPoi().getBizPoi()
                            .getPhoneNumber();
                    TnTelephonyManager.getInstance().startCall(phoneNumber);
                }

                break;
            }
            case ACTION_CHECK_SEARCHALONG:
            {
                int changedAlongRouteType = this.getInt(KEY_I_CHANGETO_ALONGROUTE_TYPE);
                int preAlongRouteType = this.getInt(KEY_I_SEARCH_ALONG_TYPE);
                if (changedAlongRouteType != -1
                        && changedAlongRouteType != preAlongRouteType)
                {
                  
                    this.postModelEvent(EVENT_MODEL_CHANGE_ALONGROUTE_TYPE);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_ALONGROUTE_NO_CHANGE);
                }
                break;
            }
            case ACTION_CHANGE_LOCATION:
            {
                Object objCurrentLocationAddr = this.get(KEY_O_ADDRESS_ORI);
                Object objSelectedAddr = this.get(KEY_O_SELECTED_ADDRESS);
                Stop currentStop = null;
                Stop selectedStop = null;
                boolean isCurrentLocation = true;
                
                if(objCurrentLocationAddr != null)
                {
                    currentStop = ((Address)objCurrentLocationAddr).getStop();
                }
                
                if(objSelectedAddr != null)
                {
                    selectedStop = ((Address)objSelectedAddr).getStop();
                    if(selectedStop != null)
                    {
                        isCurrentLocation = selectedStop.equalsIgnoreCase(currentStop);
                    }
                    this.put(KEY_O_ADDRESS_ORI, objSelectedAddr);
                }
                
                String inputText = this.getString(KEY_S_INPUT_TEXT);
                if(!isCurrentLocation && inputText != null && inputText.trim().length() > 0)
                {
                    postModelEvent(EVENT_MODEL_REDO_SEARCH);
                }
                break;
            }
            case ACTION_CHECK_ADDRESS_GOT: 
            {
                if(this.getPreState() == STATE_POI_GOTO_CHANGE_LOCATION || this.get(KEY_O_SELECTED_ADDRESS) == null)
                {
                    PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
                    if (poiDataWrapper != null)
                    {
                        int categoryId = poiDataWrapper.getCategoryId();
                        if (categoryId == PoiDataRequester.TYPE_ONE_BOX_SEARCH)
                        {
                            postModelEvent(EVENT_MODEL_POST_SEARCH_ANCHOR);
                        }
                        else
                            postModelEvent(EVENT_MODEL_POI_RESULT_LIST);
                    }
                }
                else
                {
                    postModelEvent(EVENT_MODEL_POST_ADDRESS_TO_SUPER);
                }
                break;
            }
            case ACTION_UPDATE_SEARCH_CENTER:
            {
                String showText = this.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                if(poiDataWrapper != null)
                {
                    int categoryId = poiDataWrapper.getCategoryId();
                    if(categoryId > 0 || categoryId == PoiDataRequester.TYPE_NO_CATEGORY_ID)
                    {
                        this.put(KEY_I_CATEGORY_ID, categoryId);
                    }
                    else
                    {
                        this.put(KEY_I_CATEGORY_ID,  PoiDataRequester.TYPE_ONE_BOX_SEARCH);
                    }
                }
                
                if (showText != null && showText.trim().length() > 0)
                {
                    String textFieldText = getCategoryName(this.getInt(KEY_I_CATEGORY_ID));
                    if(textFieldText == null || textFieldText.trim().length() == 0)
                    {
                        textFieldText = showText;
                    }
                    this.put(KEY_S_INPUT_TEXT, textFieldText);
                }
                
                if(this.get(KEY_O_SEARCH_CENTER) != null)
                {
                    this.put(KEY_O_ADDRESS_ORI, this.get(KEY_O_SEARCH_CENTER));
                }
                else
                {
                    this.remove(KEY_O_ADDRESS_ORI);
                }
                //update the value after resort success.
                this.put(KEY_B_NO_NEED_UPDATE_SCROLL, true);
                break;
            }
            case ACTION_CHECK_TEXT_INPUT:
            {
                String inputText = this.getString(KEY_S_INPUT_TEXT);
                if(inputText!= null && inputText.trim().length() > 0)
                {
                    postModelEvent(EVENT_MODEL_REDO_SEARCH);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_GOTO_ONE_BOX);
                }
                break;
            }
            case ACTION_SEARCH_BASE_NEW_LOCATION:
            {
            	this.put(KEY_B_NO_NEED_UPDATE_SCROLL, true);
                Address oriAddress = (Address)this.get(KEY_O_ADDRESS_ORI);
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                poiDataWrapper = poiDataWrapper.cloneInstance(System.currentTimeMillis() + "");
                if(oriAddress != null && oriAddress.getStop() != null)
                {
                    poiDataWrapper.setAnchorStop(oriAddress.getStop());
                }
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                poiDataRequester = new PoiDataRequester(userProfileProvider);
                poiDataRequester.doRequestPoi(poiDataWrapper, this);
                break;
            }
            case ACTION_MAP_RESULTS:
            {
                PoiMisLogHelper.getInstance().setIsMapResulstMode(true);
                break;
            }
            case ACTION_MAP_IT:
            {
                PoiMisLogHelper.getInstance().setIsMapResulstMode(false);
                break;
            }
            case ACTION_CANCEL_SEARCH:
            {
                if (null != poiDataRequester)
                {
                    poiDataRequester.cancel();
                }
                break;
            }
            case ACTION_BACK_CHECKING:
            {
                if (isControllerInStack(MapController.class.getName()))
                    postModelEvent(EVENT_MODEL_GOTO_POI_MAP);
                else
                    postModelEvent(EVENT_MODEL_COMMON_BACK);
                break;
            }
            default:
                break;
        }
    }
    
    private boolean isControllerInStack(String className)
    {
        boolean isFromPoiMap = false;
        Vector matches = getControllerInStack(className);
        if (matches != null && matches.size() > 0)
        {
            for (int i = 0; i < matches.size(); i++)
            {
                MapController mapController = (MapController) matches.elementAt(i);
                int tmpMapFromType = mapController.getModel().getInt(KEY_I_TYPE_MAP_FROM);
                if (tmpMapFromType == TYPE_MAP_FROM_POI || tmpMapFromType == TYPE_MAP_FROM_SPECIFIC_POI
                        || tmpMapFromType == TYPE_MAP_FROM_BROWSER || tmpMapFromType == TYPE_MAP_FROM_ONEBOX_POI)
                {
                    isFromPoiMap = true;
                    break;
                }
            }
        }
        return isFromPoiMap;
    }
    
    private void initCurrentStop()
    {
        if (get(KEY_O_ADDRESS_ORI) != null || get(KEY_O_ADDRESS_DEST) != null)
        {
            return;
        }
        Address address = new Address();
        address.setStop(this.getAnchor());
        this.put(KEY_O_CURRENT_ADDRESS, address);
    }
    
    private void logResortAction(PoiDataWrapper poiDataWrapper)
    {
        MisLogManager misLogManager = MisLogManager.getInstance();
        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_POI_SORT_REQUEST))
        {
            SortRequestMislog log = misLogManager.getFactory().createSortRequestMislog();
            log.setDatawrapper(poiDataWrapper);
            log.setTimestamp(System.currentTimeMillis());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }        
    }

    protected static String getCategoryName(int categoryId)
    {
        String categoryName = "";
        
        switch(categoryId)
        {
            case SEARCH_BY_PRICE_ANY:
                categoryName = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_POI_CATEGORY_SEARCH_ANY, IStringPoi.FAMILY_POI);
                break;
            case SEARCH_BY_PRICE_PLUS:
                categoryName = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_POI_CATEGORY_SEARCH_BY_PRICE_PLUS,  IStringPoi.FAMILY_POI);
                break;
            case SEARCH_BY_PRICE_PREMIUM:
                categoryName = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_POI_CATEGORY_SEARCH_BY_PRICE_PREMIUM,  IStringPoi.FAMILY_POI);
                break;
            case SEARCH_BY_PRICE_DIESEL:
                categoryName = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_POI_CATEGORY_SEARCH_BY_PRICE_DIESEL,  IStringPoi.FAMILY_POI);
                break;
            case SEARCH_BY_PRICE_REGULAR:
                categoryName = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_POI_CATEGORY_SEARCH_BY_PRICE_REGULAR, IStringPoi.FAMILY_POI);
                break;
            default:
                break;
        }
        return categoryName;
    }
    
    protected void activateDelegate(boolean isUpdateView)
    {
        if( getBool(KEY_B_IS_NEED_REFRESH) )
        {
            PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
            poiDataWrapper = poiDataWrapper.cloneInstance(poiDataWrapper.getSearchUid());
            //update the value after resort success.
            int sortType = this.getInt(KEY_I_SEARCH_SORT_TYPE);
            poiDataWrapper.setSortType(sortType);
            IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
            poiDataRequester = new PoiDataRequester(userProfileProvider);
            poiDataRequester.doRequestPoi(poiDataWrapper, this);
        }
    }
    
    protected void releaseDelegate()
    {
        PoiMisLogHelper.getInstance().endImpression();
        NetworkStatusManager.getInstance().removeStatusListener(this); 
        
    }

    public void poiResultUpdate(int resultValue, int resultType, String msg, PoiDataWrapper poiDataWrapper)
    {
        String errorMessage = msg;
        int changeToSortType = this.fetchInt(KEY_I_CHANGETO_SORT_TYPE);
        this.put(KEY_B_FORBID_LIST_HITBOTTOM, true);
        switch (resultValue)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            {

                if (errorMessage == null || errorMessage.trim().length() == 0)
                {
                    errorMessage = ResourceManager.getInstance()
                            .getCurrentBundle().getString(
                                    IStringCommon.RES_SERVER_ERROR,
                                    IStringCommon.FAMILY_COMMON);
                }
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            {
                errorMessage = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_NO_POIS,
                                IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }
                
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_BAD_ARGS:
            {
                errorMessage = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_NO_POIS,
                                IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }
                
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS:
            {
                this.put(KEY_B_FORBID_LIST_HITBOTTOM, false);
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                if(changeToSortType != -1 && changeToSortType != this.getInt(KEY_I_SEARCH_SORT_TYPE))
                {
                    logResortAction(poiDataWrapper);
                    this.put(KEY_I_SEARCH_SORT_TYPE, changeToSortType);
                }
                switch (this.getState())
                {
                    
                    case STATE_POI_RESULT_LIST:
                    {
                        this.put(KEY_I_SEARCH_ALONG_TYPE, this.getInt(KEY_I_CHANGETO_ALONGROUTE_TYPE));
                        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                        break;
                    }
                    case STATE_POI_REDO_SEARCHING:
                    {
                        this.put(KEY_I_SEARCH_ALONG_TYPE, this.getInt(KEY_I_CHANGETO_ALONGROUTE_TYPE));
                        PoiMisLogHelper.getInstance().startImpression();
                        postModelEvent(EVENT_MODEL_SHOW_RESEARCH_LIST);
                        break;
                    }

                }
                break;
            }
            
        }
        poiDataRequester = null;
    }
    
    protected void postErrorMessage(String errorMessage)
    {
        this.put(ICommonConstants.KEY_S_ERROR_MESSAGE, errorMessage);
        this.postModelEvent(ICommonConstants.EVENT_MODEL_POST_ERROR);
    }
    
    public Address getAddress(int index)
    {
        if (index < 0)
        {
            return null;
        }
        Address addr = null;
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        if (poiDataWrapper != null)
            addr = poiDataWrapper.getAddress(index);
        return addr;
    }
    
    public int getSize()
    {
        int size = 0;
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
        
        if(poiDataWrapper != null)
            size = poiDataWrapper.getAddressSize();

        if (hasMorePoi())
        {
            size++;
        }
        
        return size;
    }

    public boolean hasMorePoi()
    {
        boolean hasMorePoi = false;
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
        if(poiDataWrapper != null)
        {
            hasMorePoi = poiDataWrapper.isHasMorePoi();
        }
        return hasMorePoi;
    }
    
    private Stop getAnchor()
    {
        Stop anchor = null;
        // 1. Get precisely gps fix
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS);
        // 2. Get network fix
        if (location == null)
        {
            location = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_NETWORK);
        }

        if (location == null)
        {
            // 3. Get last know gps fix
            location = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS);
            // 4. Get last know network fix
            if (location == null)
            {
                location = LocationProvider.getInstance().getLastKnownLocation(
                    LocationProvider.TYPE_NETWORK);
            }
            // check last know
            if (location != null)
            {
                anchor = new Stop();
                anchor.setType(Stop.STOP_CURRENT_LOCATION);
                anchor.setLat(location.getLatitude());
                anchor.setLon(location.getLongitude());
            }
        }
        else
        {
            anchor = new Stop();
            anchor.setType(Stop.STOP_CURRENT_LOCATION);
            anchor.setLat(location.getLatitude());
            anchor.setLon(location.getLongitude());
        }
        
        return anchor;
    }
    
    private Stop getCurrentStop()
    {
        Stop currentStop = this.getAnchor();
        if(currentStop == null)
        {
            LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
                LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1);
        }
        return currentStop;
    }
    
    private void doNearMePoiSearch()
    {
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);

        poiDataWrapper = poiDataWrapper.cloneInstance(System.currentTimeMillis() + "");
        int sortType = this.getInt(KEY_I_SEARCH_SORT_TYPE);
        poiDataWrapper.setSortType(sortType);
        put(KEY_I_LAST_SHOWN_INDEX, 0);
        Stop currentStop = this.getCurrentStop();
        if(currentStop != null)
        {
            poiDataWrapper.setAnchorStop(currentStop);
            poiDataWrapper.setSearchType(IPoiSearchProxy.TYPE_SEARCH_ADDRESS);
            this.put(KEY_B_NO_NEED_UPDATE_SCROLL, true);
            IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
            poiDataRequester = new PoiDataRequester(userProfileProvider);
            poiDataRequester.doRequestPoi(poiDataWrapper, this);  
        }
    }

    @Override
    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            TnLocation loc = locations[0];
            if (loc != null)
            {
                doNearMePoiSearch();
            }
            else
            {
                notifySearchNoGpsTimeout();
            }
        }
        else
        {
            notifySearchNoGpsTimeout();
        }
    }

    private void notifySearchNoGpsTimeout()
    {
        postModelEvent(EVENT_MODEL_GOTO_NO_GPS_WARNING);
    }
    
    public void statusUpdate(boolean isConnected)
    {
        if(isConnected)
        {
            this.put(KEY_B_IS_ONBOARD, false);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
        else
        {
            this.put(KEY_B_IS_ONBOARD, true);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
   
}
