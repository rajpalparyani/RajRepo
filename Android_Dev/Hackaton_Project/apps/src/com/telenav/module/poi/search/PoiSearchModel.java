/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiSearchModel.java
 *
 */
package com.telenav.module.poi.search;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.result.PoiResultHelper;
import com.telenav.mvc.AbstractCommonModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-18
 */
class PoiSearchModel extends AbstractCommonModel implements IPoiSearchConstants, PoiDataListener, LocationListener, INetworkStatusListener
{
    public static final long NO_GPS_TIME_OUT = 12*1000;
    private PoiDataRequester poiDataRequester;
    
    public PoiSearchModel()
    {
        NetworkStatusManager.getInstance().addStatusListener(this);
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_DOING_SEARCH:
            {
                doCategorySearch();
                break;
            }
            case ACTION_CHECK_CATEGORY:
            {
                checkCategorySearch();
                break;
            }
            case ACTION_CANCEL_SEARCH:
            {
                if (null != poiDataRequester)
                {
                    poiDataRequester.cancel();
                }
                
                remove(KEY_S_LOCAL_EVENT_ID);
                break;
            }
            case ACTION_CHECK_ANCHOR:
            {
                checkAnchor();
                break;
            }
        }
    }
    
    protected void checkAnchor()
    {
        Stop anchorStop = this.getAnchor();
        if (anchorStop == null)
        {
            LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
                LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1, true);
            postModelEvent(EVENT_MODEL_SHOW_PROGRESS);
        }
        else
        {
            postModelEvent(EVENT_MODEL_GO_TO_LOCAL_EVENT);
        }
    }
    
    protected void releaseDelegate()
    {
        super.releaseDelegate();
        NetworkStatusManager.getInstance().removeStatusListener(this);
    }

    protected void checkCategorySearch()
    {
        Object objId = this.get(KEY_I_CATEGORY_ID);
        if (objId != null)
        {
            postModelEvent(CMD_QUICK_BUTTON_SELECTED);
        }
    }
    
    protected void doCategorySearch()
    {
        int categoryId = this.getInt(KEY_I_CATEGORY_ID);
        String showText =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        if (null == showText)
        {
            String name = getCategoryNameByID(categoryId, DaoManager.getInstance().getResourceBarDao().getHotPoiNode(this.getRegion()));
            
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            switch (categoryId)
            {
                case CATEGORY_ID_FOOD:
                    name = bundle.getString(IStringCommon.RES_STRING_FOOD, IStringCommon.FAMILY_COMMON);
                    break;
                case CATEGORY_ID_GAS_REGULAR:
                    name = bundle.getString(IStringCommon.RES_STRING_GAS_REGULAR, IStringCommon.FAMILY_COMMON);
                    break;
                default:
                    break;
            }
            
            if (null != name)
            {
            	put(KEY_S_COMMON_SHOW_SEARCH_TEXT, name);
            	showText = name;
            }
        }
        int searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        if (-1 != this.getInt(KEY_I_SEARCH_TYPE))
        {
            searchType = this.getInt(KEY_I_SEARCH_TYPE);
        }
        int searchFromType = IPoiSearchProxy.TYPE_SEARCH_FROM_TYPEIN;
        if (-1 != this.getInt(KEY_I_SEARCH_FROM_TYPE))
        {
            searchFromType = IPoiSearchProxy.TYPE_SEARCH_FROM_TYPEIN;
        }
       
        int sortType = PoiResultHelper.getDefaultSortType(categoryId);

        if (-1 != this.getInt(KEY_I_SEARCH_SORT_TYPE))
        {
            sortType = this.getInt(KEY_I_SEARCH_SORT_TYPE);
        }

        this.put(KEY_I_SEARCH_SORT_TYPE, sortType);

        int pageNumber = 0;
        
        int pageSize = MAX_NUM_PER_PAGE;
        Stop anchorStop = null;
        Stop destStop = null;
        int searchAlongRouteType = -1;
        if (this.get(KEY_O_ADDRESS_ORI) instanceof Address)
        {
            Address oriAddr = (Address)this.get(KEY_O_ADDRESS_ORI);
            anchorStop = oriAddr.getStop();
        }
        else
        {
            anchorStop = this.getAnchor();
            if(anchorStop == null)
            {
                LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
                    LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
                    LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1, true);
                postModelEvent(EVENT_MODEL_SHOW_PROGRESS);
                return;
            }
        }
        
        if (searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            Address destAddr = (Address)this.get(KEY_O_ADDRESS_DEST);
            destStop = destAddr.getStop();
            searchAlongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD;
            if(-1 != this.getInt(KEY_I_SEARCH_ALONG_TYPE))
            {
                searchAlongRouteType = this.getInt(KEY_I_SEARCH_ALONG_TYPE);
            }
        }
        PoiDataWrapper dataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
        dataWrapper.setSearchArgs(0, searchType, searchFromType, searchAlongRouteType, sortType, categoryId, pageNumber, pageSize, "", showText, anchorStop, destStop, null) ;
        dataWrapper.setIsMostPopularSearch(this.fetchBool(KEY_B_IS_MOST_PUPOLAR_SEARCH));
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        poiDataRequester = new PoiDataRequester(userProfileProvider);
        poiDataRequester.doRequestPoi(dataWrapper, this);
        postModelEvent(EVENT_MODEL_SHOW_PROGRESS);
    }
    
    private String getCategoryNameByID(int catID, PoiCategory parent)
    {
    	if (null == parent) return null;
    	if (parent.getCategoryId() == catID) return parent.getName();
    	if (parent.getChildrenSize() == 0) return null;
    	String name = null;
    	for (int i = 0; i < parent.getChildrenSize(); i++)
        {
    		PoiCategory child = parent.getChildAt(i);
    		name = getCategoryNameByID(catID, child);
    		if (null != name) return name;
        }
    	return null;
    }

    private Stop getAnchor()
    {
        Stop anchor = null;
        // 1. Get  gps or network fix
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
         LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, true);
        // Fix bug http://jira.telenav.com:8080/browse/TNANDROID-4961
//        if (location == null)
//        {
//            // 2. Get  last-known gps or network fix
//            location = LocationProvider.getInstance().getLastKnownLocation(
//                LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
//            if (location != null)
//            {
//                anchor = new Stop();
//                anchor.setType(Stop.STOP_CURRENT_LOCATION);
//                anchor.setLat(location.getLatitude());
//                anchor.setLon(location.getLongitude());
//            }
//        }
//        else
        if (location != null)
        {
            anchor = new Stop();
            anchor.setType(Stop.STOP_CURRENT_LOCATION);
            anchor.setLat(location.getLatitude());
            anchor.setLon(location.getLongitude());
        }        
        return anchor;
    }

    

    public void poiResultUpdate(int resultValue, int resultType, String errorMsg, PoiDataWrapper poiDataWrapper)
    {
        String errorMessage = errorMsg;
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
                postModelEvent(EVENT_MODEL_GOTO_POI_LIST);
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

    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            TnLocation loc = locations[0];
            if (loc != null)
            {
                if(getString(KEY_S_LOCAL_EVENT_ID) != null)
                {
                    postModelEvent(EVENT_MODEL_GO_TO_LOCAL_EVENT);
                }
                else
                {
                    doCategorySearch();
                }
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
   
    @Override
    public void statusUpdate(boolean isConnected)
    {
        if (isConnected)
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
