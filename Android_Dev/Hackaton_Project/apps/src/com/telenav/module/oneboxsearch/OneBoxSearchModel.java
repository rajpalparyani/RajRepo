/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * OneBoxSearchModel.java
 *
 */
package com.telenav.module.oneboxsearch;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.CitiesDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AddressMisLog;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.dashboard.DashboardManager;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.result.PoiResultHelper;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPoi;
import com.telenav.res.IStringSetHome;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-3
 */
public class OneBoxSearchModel extends BrowserSdkModel implements PoiDataListener, IOneBoxSearchConstants, LocationListener
{
    public static final long NO_GPS_TIME_OUT = 12*1000;
    protected static final int ID_MOST_POPULAR_CATEGORY = 2041;
    boolean needCurrentLocation = false;
    
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_ONEBOX_SEARCH:
            {
                doOneBoxSearch();
                break;
            }
            case IOneBoxSearchConstants.ACTION_INIT:
            {
                init();
                boolean isSearchDirectly = this.fetchBool(KEY_B_IS_SEARCHING_DIRECTLY);
                String keyWords = this.getString(KEY_S_COMMON_SEARCH_TEXT);
                if(isSearchDirectly && keyWords != null && keyWords.trim().length() > 0 )
                {
                    postModelEvent(EVENT_SEARCHING_DIRECTLY);
                }
                else
                {
                    postModelEvent(EVENT_GOTO_ONE_BOX_MAIN);
                }
                break;
            }
            case ACTION_PREPARE_SUGGESTION:
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                Vector suggestions = poiDataWrapper.getMultiMatchResults();
                this.put(KEY_V_RESULT_SUGGESTIONS, suggestions);
                break;
            }
            case ACTION_SET_LOCATION_CHANGE:
            {
                Object selectedAddrObj = this.get(KEY_O_SELECTED_ADDRESS);
                if(selectedAddrObj != null)
                {
                    this.put(KEY_O_ADDRESS_ORI, selectedAddrObj);
                }
                break;
            }
            case ACTION_CHECK_ADDRESS_GOT:
            {
                if(this.getPreState() != STATE_GOTO_CHANGE_LOCATION && this.get(KEY_O_SELECTED_ADDRESS) != null)
                {
                    int addressValidationfrom = getInt(KEY_I_TYPE_ADDRESS_VALIDATOR_FROM);
                    if (addressValidationfrom == TYPE_FROM_CONTACTS || addressValidationfrom == TYPE_FROM_POI )
                    {
                        boolean isChoosingLocation = this.getBool(KEY_B_IS_CHOOSING_LOCATION);
                        if (isChoosingLocation)
                            this.postModelEvent(EVENT_MODEL_RETURN_ADDRESS_TO_SUPER);
                        else
                            this.postModelEvent(EVENT_MODEL_ONE_ADDRESS);
                    }
                    else
                        this.postModelEvent(EVENT_MODEL_RETURN_ADDRESS_TO_SUPER);
                }
                else
                {
                    this.postModelEvent(EVENT_GOTO_ONE_BOX_MAIN);
                }
                break;
            }
            case ACTION_CLEAR_HISTORY:
            {
                DaoManager.getInstance().getAddressDao().clearRecentSearch();
                put(KEY_B_CLEAR_SEARCH_HISTORY, true);

                break;
            }
            case ACTION_CLEAR_MULTI_RESULTS:
            {
                this.remove(KEY_B_IS_FROM_SUGGEST_SELECTION);
                this.remove(KEY_V_RESULT_SUGGESTIONS);
                break;
            }
            case ACTION_VALIDATE_INPUT:
            {
                doValidate();
                break;
            }
            case ACTION_CHECK_CITY:
            {
                if (this.get(KEY_O_VALIDATED_ADDRESS) == null)
                {
                    this.postModelEvent(EVENT_MODEL_GO_TO_VALIDATE_ADDRESS);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_RETURN_CITY_ADDRESS);
                }
                break;
            }
            case ACTION_REFRESH_AUTO_SUGGESTION:
            {
                this.put(KEY_B_NEED_REFRESH_AUTO_SUGGESTION, true);
                break;
            }
            case ACTION_VALIDATED_ADDRESS_RETURNED:
            {
                boolean isFromCantacts =   this.getBool(KEY_B_DETAIL_FROM_CONTACT);
                if(isFromCantacts)
                {
                    Address address = (Address)get(KEY_O_VALIDATED_ADDRESS);
                    address.setSource(Address.SOURCE_PREDEFINED);
                    address.setLabel(this.getString(KEY_S_LABEL_FROM_CONTACT));
                    address.setPhoneNumber(this.getString(KEY_S_PTN_FROM_CONTACT));
                    if(address.getStop()!=null)
                    {
                        address.getStop().setLabel(this.getString(KEY_S_LABEL_FROM_CONTACT));
                    }              
                    PoiDataWrapper poiDataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
                    poiDataWrapper.addStopAddress(address);
                    poiDataWrapper.addNormalAddr(address);
                    this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                    put(KEY_O_SELECTED_ADDRESS, address);
                    put(KEY_I_TYPE_ADDRESS_VALIDATOR_FROM, TYPE_FROM_CONTACTS);
                    postModelEvent(EVENT_CONTROLLER_ADDRESS_SELECTED);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_DO_VALIDATE_ADDRESS);
                }
                break;
            }
        }
    }
    
    
    protected boolean isOneBoxSupported()
    {
        String oneboxSearch = DaoManager.getInstance().getServerDrivenParamsDao().getBoxType(this.getRegion());
        boolean isOneBoxSupport = ServerDrivenParamsDao.ONE_BOX_TYPE_STANDARD.equals(oneboxSearch);
        return isOneBoxSupport && NetworkStatusManager.getInstance().isConnected();
    }
    
    protected void init()
    {
        needCurrentLocation = this.getBool(KEY_B_NEED_CURRENT_LOCATION);
        put(KEY_B_ONEBOX_SEARCH, isOneBoxSupported());
        Vector favorites = DaoManager.getInstance().getAddressDao().getDisplayFavorateAddress();
        Vector recents = DaoManager.getInstance().getAddressDao().getRecentAddresses();
        Vector searchList = new Vector();
        Vector helpVector = new Vector();
        for(int i = 0; i < favorites.size(); i++)
        {
            Address favorite = (Address) favorites.elementAt(i);
            if(!helpVector.contains(ResourceManager.getInstance().getStringConverter().convertAddress(favorite.getStop(), false)))
            {
                searchList.addElement(favorite);
                helpVector.addElement(ResourceManager.getInstance().getStringConverter().convertAddress(favorite.getStop(), false));
            }
        }

        for(int i = 0 ; i < recents.size(); i++)
        {
            Address recent = (Address) recents.elementAt(i);
            if(!helpVector.contains(ResourceManager.getInstance().getStringConverter().convertAddress(recent.getStop(), false)))
            {
                searchList.addElement(recent);
                helpVector.addElement(ResourceManager.getInstance().getStringConverter().convertAddress(recent.getStop(), false));
            }
        }
        put(KEY_V_SEARCH_LIST, searchList);
        put(KEY_V_FILTER_SEARCH_LIST, searchList);
        if (!getBool(KEY_B_ONEBOX_SEARCH))
        {
            put(KEY_V_NEAR_CITIES, DaoManager.getInstance().getNearCitiesDao().getNearCities(getRegion()));
        }
        if(needCurrentLocation)
        {
            checkCurrentLocation();
        }
    }
    
    private void checkCurrentLocation()
    {
        Address currentAddress = DashboardManager.getInstance().getCurrentAddress();
        if (currentAddress != null && currentAddress.getStop() != null)
        {
            this.put(KEY_O_ANCHOR_CURRENT_ADDRESS, currentAddress);
        }
        else
        {
            Stop anchor = this.getAnchor();
            if (anchor != null)
            {
                requestRgc(anchor.getLat(), anchor.getLon());
                Address address = new Address();
                address.setStop(anchor);
                this.put(KEY_O_ANCHOR_CURRENT_ADDRESS, address);
            }
            else
            {
                requestCurrentLocation();
            }
        }
    }
    
    protected void requestRgc(int lat, int lon)
    {
        IRgcProxy proxy = ServerProxyFactory.getInstance().createRgcProxy(null, CommManager.getInstance().getComm(), this);
        proxy.requestRgc(lat, lon);
    }
    
    private void requestCurrentLocation()
    {
        LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
            LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1);        
    }
    
    protected void doOneBoxSearch()
    {
        Stop anchorStop = null;
        String showText = this.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        int searchType = IOneBoxSearchProxy.TYPE_SEARCH_AROUND_ME;
        if (-1 != this.getInt(KEY_I_SEARCH_TYPE))
        {
            searchType = this.getInt(KEY_I_SEARCH_TYPE);
        }
        if(this.fetchBool(KEY_B_IS_SAVE_TEXT_RECENT_SEARCH))
        {
            DaoManager.getInstance().getAddressDao().addRecentSearch(showText);
            DaoManager.getInstance().getAddressDao().store();
        }
     
        if (searchType == IOneBoxSearchProxy.TYPE_SEARCH_CITY)
        {
            anchorStop = ((CitiesDao) DaoManager.getInstance().getNearCitiesDao()).getDefaultCity();
            remove(KEY_I_CATEGORY_ID);
        }
        else
        {
            if (this.get(KEY_O_ADDRESS_ORI) instanceof Address)
            {
                Address oriAddr = (Address) this.get(KEY_O_ADDRESS_ORI);
                anchorStop = oriAddr.getStop();
            }
            else
            {
                anchorStop = this.getAnchor();
            }
            //we will use lastknown location as anchor.
            //Fix http://jira.telenav.com:8080/browse/TNANDROID-6952
//            if (anchorStop == null)
//            {
//                LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
//                    LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
//                    LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1, true);
                

//                return;
//            }
        }
        String searchText = (String) this.get(KEY_S_COMMON_SEARCH_TEXT);
        int categoryId = this.getInt(KEY_I_CATEGORY_ID);
        if ((searchText == null || searchText.trim().length() == 0 )
                && categoryId < 0)
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.MSG_SEARCH_PARAM_ERR,
                IStringPoi.FAMILY_POI);
            postErrorMessage(errorMessage);
            return;
        }

       
        int pageNumber = 0;
        int pageSize = MAX_NUM_PER_PAGE;

        int searchAlongRouteType = -1;
        Stop destStop = null;
        if (searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            if (this.get(KEY_O_ADDRESS_DEST) instanceof Address)
            {
                Address destAddr = (Address) this.get(KEY_O_ADDRESS_DEST);
                destStop = destAddr.getStop();
                searchAlongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD;
                if (-1 != this.getInt(KEY_I_SEARCH_ALONG_TYPE))
                {
                    searchAlongRouteType = this.getInt(KEY_I_SEARCH_ALONG_TYPE);
                }
            }
            else
            {
                String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.MSG_SEARCH_PARAM_ERR,
                    IStringPoi.FAMILY_POI);
                postErrorMessage(errorMessage);
                return;
            }
        }

        //NY: Fix category search no response bug.
        //FIXME: can categoryId be -1?
        
        int sortType = -1;
        if(categoryId > 0)
        {
            sortType = PoiResultHelper.getDefaultSortType(categoryId);
        }
        else
        {
            if (getBool(KEY_B_ONEBOX_SEARCH))
            {
                categoryId = PoiDataRequester.TYPE_ONE_BOX_SEARCH;
            }
            else
            {
                categoryId = PoiDataRequester.TYPE_NO_CATEGORY_ID;
                sortType = IPoiSearchProxy.TYPE_SORT_BY_RELEVANCE;
            }
        }
        this.put(KEY_I_SEARCH_SORT_TYPE, sortType);
        this.put(KEY_I_CATEGORY_ID, categoryId);
        
        String transactionId = this.fetchString(KEY_S_TRANSACTION_ID);
        if(transactionId == null || transactionId.length() == 0)
        {
            transactionId = System.currentTimeMillis() + "";
        }
        
        Object listAdapter = get(KEY_O_LIST_ADAPTER);
        boolean isFromSuggestSelection = this.fetchBool(KEY_B_IS_FROM_SUGGEST_SELECTION);
        if(!isFromSuggestSelection && listAdapter != null && listAdapter instanceof AutoSuggestListAdapter)
        {
            AutoSuggestListAdapter autoSuggestListAdapter = (AutoSuggestListAdapter)listAdapter;
            String newTransactionId = System.currentTimeMillis() + "";
            autoSuggestListAdapter.setTransactionId(newTransactionId);
            this.put(KEY_S_TRANSACTION_ID, newTransactionId);
        }
        
        PoiDataWrapper dataWrapper = new PoiDataWrapper(transactionId);
        if(categoryId >= 0)
        {
            searchText = "";
            put(KEY_S_COMMON_SEARCH_TEXT, searchText);
        }
        int inputType = getInt(KEY_I_INPUT_TYPE);
        if(inputType >=0)
        {
            dataWrapper.setSearchArgs(0, searchType, -1, searchAlongRouteType, sortType, categoryId, pageNumber, pageSize,
                    searchText, showText, anchorStop, destStop, null, inputType);
        }
        else{
            dataWrapper.setSearchArgs(0, searchType, -1, searchAlongRouteType, sortType, categoryId, pageNumber, pageSize,
                    searchText, showText, anchorStop, destStop, null);
        }
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        PoiDataRequester poiDataRequester = new PoiDataRequester(userProfileProvider);
        poiDataRequester.doRequestPoi(dataWrapper, this);
        
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.setRouteRequestBy(IMisLogConstants.VALUE_ADDRESS_INPUT_TYPE_TYPE_IN);
    
    }

    private void doValidate()
    {
        boolean fromFroglist = this.fetchBool(KEY_B_FROM_LIST);
        if(fromFroglist)
        {
            postModelEvent(EVENT_MODEL_VALIDATE_LIST_HOME);
        }
        else
        {
            String address = getString(KEY_S_ADDRESS_LINE);
            String district = getString(KEY_S_CITY);
            
            if ((address == null || address.trim().length() == 0)
                    && (district == null || district.trim().length() == 0))
            {
                put(KEY_S_ERROR_MESSAGE,
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringSetHome.RES_LABEL_ADDRESS_NOT_SET, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_POST_ERROR);
            }
            else if(district == null || district.trim().length() == 0)
            {
                put(KEY_S_ERROR_MESSAGE,
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringSetHome.RES_LABEL_CITY_NOT_SET, IStringSetHome.FAMILY_SET_HOME));
                postModelEvent(EVENT_MODEL_POST_ERROR);
            }
            else
            {
                postModelEvent(EVENT_MODEL_VALIDATE_HOME);
            }
        }
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.setRouteRequestBy(IMisLogConstants.VALUE_ADDRESS_INPUT_TYPE_TYPE_IN);
    }
    
    private Stop getAnchor()
    {
        Stop anchor = null;
        // 1. Get precisely gps fix
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS, false);
        // 2. Get network fix
        if (location == null)
        {
            location = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_NETWORK, false);
        }

        // To fix bug http://jira.telenav.com:8080/browse/TNANDROID-4961
//        if (location == null)
//        {
//            // 3. Get last know gps fix
//            location = LocationProvider.getInstance().getLastKnownLocation(
//                LocationProvider.TYPE_GPS);
//            // 4. Get last know network fix
//            if (location == null)
//            {
//                location = LocationProvider.getInstance().getLastKnownLocation(
//                    LocationProvider.TYPE_NETWORK);
//            }
//            // check last know
//            if (location != null)
//            {
//                anchor = new Stop();
//                anchor.setType(Stop.STOP_CURRENT_LOCATION);
//                anchor.setLat(location.getLatitude());
//                anchor.setLon(location.getLongitude());
//            }
//        }
        if (location != null)
        {
            anchor = new Stop();
            anchor.setType(Stop.STOP_CURRENT_LOCATION);
            anchor.setLat(location.getLatitude());
            anchor.setLon(location.getLongitude());
        }

       
        return anchor;
    }
    
    public void poiResultUpdate(int resultValue, int resultType, String msg, PoiDataWrapper poiDataWrapper)
    {
        String errorMessage = msg;
        switch(resultValue)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            {
                
                if (errorMessage == null || errorMessage.length() == 0)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
                }
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            {
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
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
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
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
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                switch(resultType)
                {
                    case PoiDataRequester.TYPE_RESULT_SUGGESTION_DID_U_MEAN:
                    {
                        MisLogManager misLogManager = MisLogManager.getInstance();
                    	Vector suggestions = poiDataWrapper.getMultiMatchResults();
                        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_IMPRESSION))
                        {
                            AddressMisLog log = misLogManager.getFactory().createOneBoxSuggestionsImpressionMisLog();
                            log.setTimestamp(System.currentTimeMillis());
                            if(suggestions != null)
                            {
                                log.setResultNumber(suggestions.size());
                            }
                            else
                            {
                            	log.setResultNumber(0);
                            }
                            log.setSearchUid(poiDataWrapper.getSearchUid());
                            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                            { log });
                        }
                        postModelEvent(EVENT_SHOW_SUGGESTIONS);
                        break;
                    }
                    
                    case PoiDataRequester.TYPE_RESULT_ADDRESS:
                    {
                        int size = poiDataWrapper.getAddressSize();
                        Address addr = null;
                        if(size > 0)
                        {
                            addr = poiDataWrapper.getAddress(0);
                        }
                        if (addr != null)
                        {
                            switch (addr.getType())
                            {
                                case Address.TYPE_RECENT_STOP:
                                {
                                    boolean isStopEnabled = true;
                                    if (!isStopEnabled)
                                    {
                                        errorMessage = ResourceManager.getInstance()
                                        .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                                            IStringPoi.FAMILY_POI);
                                        String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                                        if(name == null)
                                        {
                                            name = getString(KEY_S_COMMON_SEARCH_TEXT);
                                        }
                                        
                                        StringConverter converter = ResourceManager.getInstance().getStringConverter();
                                        errorMessage = converter.convert(errorMessage, new String[]{name});
                                        postErrorMessage(errorMessage);
                                    }
                                    else
                                    {
                                        this.put(KEY_I_POI_SELECTED_INDEX, 0);
                                        
                                        MisLogManager misLogManager = MisLogManager.getInstance();

                                        if (poiDataWrapper.getAddressSize() > 1 || poiDataWrapper.isNeedUserSelection())
                                        {
                                            if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ONEBOX_ADDRESS_IMPRESSION))
                                            {
                                                AddressMisLog log = misLogManager.getFactory().createOneBoxAddressImpressionMisLog();
                                                log.setTimestamp(System.currentTimeMillis());
                                                log.setResultNumber(poiDataWrapper.getAddressSize());
                                                log.setSearchUid(poiDataWrapper.getSearchUid());
                                                Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                                                { log });
                                            }
                                            postModelEvent(EVENT_SHOW_MULISTOPS);
                                        }
                                        else
                                        {
                                            if(poiDataWrapper.getAddressSize() == 1)
                                            {
                                                if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ONEBOX_ADDRESS_ONE_RESULT))
                                                {
                                                    AddressMisLog log = misLogManager.getFactory().createOneBoxAddressOneResultMisLog();
                                                    Address address = poiDataWrapper.getAddress(0);
                                                    if (address.getStop() != null)
                                                    {
                                                        log.setzEndLat(address.getStop().getLat());
                                                        log.setzEndLon(address.getStop().getLon());
                                                        log.setzDisplayString(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
                                                    }
                                                    log.setTimestamp(System.currentTimeMillis());
                                                    log.setSearchUid(poiDataWrapper.getSearchUid());
                                                    log.setPageNumber(0);
                                                    log.setPageIndex(0);
                                                    Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                                                    { log });
                                                }
                                            }
                                            postModelEvent(EVENT_MODEL_RETURN_STOPS);
                                        }
                                    }
                                    break;
                                }
                                case Address.TYPE_RECENT_POI:
                                {
                                    if (size == 1)
                                    {
                                        Address address = poiDataWrapper.getAddress(0);
                                        put(KEY_O_SELECTED_ADDRESS, address);
                                        put(KEY_I_TYPE_ADDRESS_VALIDATOR_FROM, TYPE_FROM_POI);
                                        postModelEvent(EVENT_CONTROLLER_ADDRESS_SELECTED);
                                    }
                                    else
                                        postModelEvent(EVENT_MODEL_POIS_GOT);
                                    break;
                                }
                            }
                        }
                       break;
                       
                    }
                    default:
                    {
                        errorMessage = ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringPoi.LABEL_NO_POIS,
                                    IStringPoi.FAMILY_POI);
                        postErrorMessage(errorMessage);
                        break;
                    }
                }
                
                break;
            }
        }
    }

    protected void postErrorMessage(String errorMessage)
    {
        this.put(ICommonConstants.KEY_S_ERROR_MESSAGE, errorMessage);
        this.postModelEvent(ICommonConstants.EVENT_MODEL_POST_ERROR);
    }

    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        String searchStr = this.getString(KEY_S_COMMON_SEARCH_TEXT);
        boolean hasOneBoxSearchStr = searchStr != null && searchStr.trim().length() != 0;
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            TnLocation loc = locations[0];
            if (loc != null)
            {
                if(needCurrentLocation)
                {
                    requestRgc(loc.getLatitude(), loc.getLongitude());
                    
                    Stop anchor = new Stop();
                    anchor.setLat(loc.getLatitude());
                    anchor.setLon(loc.getLongitude());
                    Address address = new Address();
                    address.setStop(anchor);
                    this.put(KEY_O_ANCHOR_CURRENT_ADDRESS, address);
                    updateAutoSuggestList();
                }
                
                if(hasOneBoxSearchStr)
                {
                    doOneBoxSearch();
                }
            }
            else
            {
                if(hasOneBoxSearchStr)
                {
                    notifySearchNoGpsTimeout();
                }
            }
        }
        else
        {
            if(hasOneBoxSearchStr)
            {
                notifySearchNoGpsTimeout();
            }
        }
    }
    

    private void notifySearchNoGpsTimeout()
    {
        postModelEvent(EVENT_MODEL_GOTO_NO_GPS_WARNING);
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof IRgcProxy)
        {
            Address address = ((IRgcProxy) proxy).getAddress();
            if(address != null && address.getStop() != null)
            {
                this.put(KEY_O_ANCHOR_CURRENT_ADDRESS, address);
                updateAutoSuggestList();
            }
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        
    }
    
    public void networError(AbstractServerProxy proxy)
    {
        
    }
    
    private void updateAutoSuggestList()
    {
        Address address = (Address) this.get(KEY_O_ANCHOR_CURRENT_ADDRESS);
        Object listAdapter = get(KEY_O_LIST_ADAPTER);
        if(listAdapter instanceof AutoSuggestListAdapter)
        {
            AutoSuggestListAdapter autoSuggestListAdapter = (AutoSuggestListAdapter)listAdapter;
            autoSuggestListAdapter.setCurrentAnchorAddress(address);
            this.put(KEY_B_NEED_REFRESH_AUTO_SUGGESTION, true);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
}
