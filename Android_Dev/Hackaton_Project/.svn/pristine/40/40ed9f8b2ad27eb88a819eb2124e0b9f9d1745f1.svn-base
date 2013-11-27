/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiSearchArgs.java
 *
 */
package com.telenav.module.poi;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.nav.NavState;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-9-23
 */
public class PoiSearchArgs
{
    //Search distance constants
    public static final int INITIAL_SEARCH_RADIUS_CAT          = 5;    //mile
    public static final int MAXIMUM_SEARCH_RADIUS_CAT          = 15;   //mile
    public static final int SEARCH_RADIUS_INCREMENT_CAT        = 5;    //mile
    public static final int MILE_TO_DM5                        = 1446; //1 mile = 1446 dm5
    public static final int DEFAULT_RADIOUS = INITIAL_SEARCH_RADIUS_CAT * MILE_TO_DM5; //5 miles as init dist to search poi
    
    //result type
    public static final int TYPE_NO_RETURN = -2;
    public static final int TYPE_NO_RESULTS = -1;
    public static final int TYPE_POI_RESULTS = 0;
    public static final int TYPE_SUGGESTS_RESULTS = 1;
    public static final int TYPE_STOPS_RESULTS = 2;
    
    public static final int TYPE_NO_CATEGORY_ID = -1;
    public static final int TYPE_ONE_BOX_SEARCH = Integer.MIN_VALUE;
    public static final int TYPE_RGC = TYPE_ONE_BOX_SEARCH + 1;
    public static final int TYPE_AUTOSUGGEST = TYPE_ONE_BOX_SEARCH + 2;
    public static final int TYPE_VALIDATE_AIRPORT = TYPE_ONE_BOX_SEARCH + 3;
    
    //sort type
    public final static int TYPE_SORT_BY_DISTANCE = 0;
    public final static int TYPE_SORT_BY_RATING = 1;
    public final static int TYPE_SORT_BY_POPULAR = 2;
    public final static int TYPE_SORT_BY_RELEVANCE = 3;
    public final static int TYPE_SORT_BY_PRICE = 4;
    
    //search from type
    public final static int TYPE_SEARCH_FROM_TYPEIN = 1;
    public final static int TYPE_SEARCH_FROM_SPEAKIN = 2;
    public final static int TYPE_SEARCH_FROM_TYPEIN_ALONG = 3;
    public final static int TYPE_SEARCH_FROM_SPEAKIN_ALONG = 4;
    
    //search type
    public static final int TYPE_SEARCH_AROUND_ME = 0;
    public final static int TYPE_SEARCH_ADDRESS = 5;
    public final static int TYPE_SEARCH_ALONG_ROUTE = 7;
//    public static final int TYPE_SEARCH_RECENT_STOPS = 1;
//    public static final int TYPE_SEARCH_AIRPORT = 2;
//    public static final int TYPE_SEARCH_CITY = 3;
//    public static final int TYPE_SEARCH_ZIP = 4;
//    public static final int TYPE_SEARCH_ADDRESS = 5;
//    public static final int TYPE_SEARCH_WAYPOINTS = 6;
    
    //along route  type
    public final static int TYPE_SEARCH_ALONG_UPHEAD = 0;
    public final static int TYPE_SEARCH_AROUND_DESTINATION = 1;
    
    //Define max result number of earch result page.
    public static final int MAX_NUM_PER_PAGE = 10;
    
    //input type
    public static final int TYPE_INPUTTYPE_ANY = 0;
    public static final int TYPE_INPUTTYPE_CHN = 1;
    public static final int TYPE_INPUTTYPE_PY = 2;
    
    private int currPageSize;
    
    private int searchType;
    private int fromType;
    private int alongRouteType;
    private int sortType;
    private int categoryId;
    private int inputType;
    
//    private boolean useInstantNavState = false;
    
    private boolean sortEnable = true;
    private String keyword = "";
    private String showText = "";
    private Address anchorAddress;
    private Address destAddress;
    private NavState currNavState;
    
    private String errorMessage = "";// used for initForSearch;
    
    public PoiSearchArgs()
    {
        this(TYPE_SEARCH_ADDRESS, TYPE_SEARCH_FROM_TYPEIN, -1, -1, -3, MAX_NUM_PER_PAGE, "", "", null,
                null, null);
    }

    public PoiSearchArgs(int searchType, int searchFromType)
    {
        this(searchType, searchFromType, -1, -1, -3, MAX_NUM_PER_PAGE, "", "", null, null, null);
    }

    public PoiSearchArgs(int searchType, int searchFromType, int alongRouteType, Address anchorStop, Address destStop, NavState navState)
    {
        this(searchType, searchFromType, alongRouteType, -1, -3, MAX_NUM_PER_PAGE, "", "", anchorStop, destStop, navState);
    }

    public PoiSearchArgs(int searchType, int searchFromType, int alongRouteType, int sortType, int categoryId, int pageSize,
            String searchKeyWord, String showText, Address anchorStop, Address destStop, NavState navState)
    {
        this.searchType = searchType;
        if (searchType < 0)
        {
            searchType = TYPE_SEARCH_ADDRESS;
        }
        this.fromType = searchFromType;
        this.alongRouteType = alongRouteType;
        if (alongRouteType < 0 && searchType == TYPE_SEARCH_ALONG_ROUTE)
        {
            alongRouteType = TYPE_SEARCH_ALONG_UPHEAD;
        }
        this.sortType = sortType;
        this.categoryId = categoryId;
        this.currPageSize = pageSize;
        this.keyword = searchKeyWord;
        this.showText = showText;
        this.anchorAddress = anchorStop;
        this.destAddress = destStop;
        this.currNavState = navState;
        this.inputType = PoiSearchArgs.TYPE_INPUTTYPE_ANY;
    }
    
    public PoiSearchArgs(int searchType, int searchFromType, int alongRouteType, int sortType, int categoryId, int pageSize,
            String searchKeyWord, String showText, Address anchorStop, Address destStop, NavState navState, int inputType)
    {
        this(searchType, searchFromType, alongRouteType, sortType, categoryId, pageSize, searchKeyWord, showText, anchorStop, destStop,
                navState);
        this.inputType = inputType;
    }
    
    public boolean initForSearch(boolean isOnebox, boolean isSearchAlong, boolean isSpeakIn, boolean useDefaultLocation)
    {
        if (isOnebox)
        {
            setCategoryId(TYPE_ONE_BOX_SEARCH);
            setSearchType(TYPE_SEARCH_AROUND_ME);
            setSortEnable(false);
            if(getKeyWord() == null || getKeyWord().trim().length() == 0)
            {
                errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.MSG_SEARCH_PARAM_ERR,
                    IStringPoi.FAMILY_POI);
                return false;
            }
        }
        else
        {
            if (getCategoryId() < 1)
            {
                setCategoryId(TYPE_NO_CATEGORY_ID);
            }
            if (getSortType() == -1)
            {
                setSortType(TYPE_SORT_BY_RELEVANCE);
            }
            setSearchType(TYPE_SEARCH_ADDRESS);
            setSortEnable(true);
            if(!isSpeakIn)
            {
                setKeyWord("");
            }
        }
        setFromType(TYPE_SEARCH_FROM_TYPEIN);
        if (isSearchAlong)
        {
            if (currNavState == null)
            {
                currNavState = NavSdkNavEngine.getInstance().getCurrentNavState();
                if (currNavState == null)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringPoi.MSG_SEARCH_PARAM_ERR, IStringPoi.FAMILY_POI);
                    return false;
                }
            }
            setSearchType(TYPE_SEARCH_ALONG_ROUTE);
            setFromType(TYPE_SEARCH_FROM_TYPEIN_ALONG);
            if (alongRouteType == -1)
            {
                setAlongRouteType(TYPE_SEARCH_ALONG_UPHEAD);
            }
        }
        else
        {
            setAlongRouteType(-1);
            setDestAddress(null);
            setNavState(null);
        }
        if (isSpeakIn)
        {
            if (isSearchAlong)
            {
                setFromType(TYPE_SEARCH_FROM_SPEAKIN_ALONG);
            }
            else
            {
                setFromType(TYPE_SEARCH_FROM_SPEAKIN);
            }
        }


        setPageSize(MAX_NUM_PER_PAGE);

        if (getAnchorAddress() == null)
        {
            Stop anchorStop = this.getAnchor(useDefaultLocation);
            if(anchorStop == null)
            {
                setAnchorAddress(null);
                return true;
            }
            else
            {
                Address anchor = new Address();
                anchor.setStop(anchorStop);
                setAnchorAddress(anchor);
            }
        }

        return true;
    }
    
//    public void setUseInstantNavState(boolean useInstantNavState)
//    {
//        this.useInstantNavState = useInstantNavState;
//    }
            
    
    public int setPageSize(int pageSize)
    {
        return this.currPageSize = pageSize;
    }
    
    public int getPageSize()
    {
        return this.currPageSize;
    }
    
    public void setSearchType(int searchType)
    {
        this.searchType = searchType;
        if (alongRouteType < 0 && searchType == TYPE_SEARCH_ALONG_ROUTE)
        {
            alongRouteType = TYPE_SEARCH_ALONG_UPHEAD;
        }
    }
    
    public int getSearchType()
    {
        return this.searchType;
    }
    
    public void setFromType(int searchFromType)
    {
        this.fromType = searchFromType;
    }
    
    public int getSearchFromType()
    {
        return this.fromType; 
    }
    
    public void setAlongRouteType(int alongRouteType)
    {
        this.alongRouteType = alongRouteType;
    }
    
    public int getAlongRouteType()
    {
        return this.alongRouteType;
    }
    
    public void setSortType(int sortType)
    {
        this.sortType = sortType;
    }
    
    public int getSortType()
    {
        return sortType;
    }
    
    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }
    
    public int getCategoryId()
    {
        return this.categoryId;
    }
    
    public void setKeyWord(String keyword)
    {
        this.keyword = keyword;
        if(this.keyword != null && keyword.trim().length() != 0)
        {
            this.categoryId = -2;
        }
    }
    
    public String getKeyWord()
    {
        return this.keyword;
    }
    
    public void setShowText(String showText)
    {
        this.showText = showText;
    }
    
    public String getShowText()
    {
        if (showText != null && showText.length() > 0)
        {
            return this.showText;
        }
        else
        {
            return this.getKeyWord();
        }
    }
    
    public void setAnchorAddress(Address anchor)
    {
        this.anchorAddress = anchor;
    }
    
    public Address getAnchorAddress()
    {
        return anchorAddress;
    }
    
    public void setDestAddress(Address dest)
    {
        this.destAddress = dest;
    }
    
    public Address getDestAddress()
    {
        return destAddress;
    }

    public void setNavState(NavState navState)
    {
        this.currNavState = navState;
    }
    
    public NavState getNavState()
    {
        return this.currNavState;
    }
    
    public void setInputType(int inputType)
    {
        this.inputType = inputType;
    }
    
    public int getInputType()
    {
        return this.inputType;
    }
    
    public void setSortEnable(boolean sortEnable)
    {
        this.sortEnable = sortEnable;
    }
    
    public boolean isSortEnable()
    {
        return this.sortEnable;
    }
    
    public String getErrorMessage()
    {
        return errorMessage;
    }
    
    private Stop getAnchor(boolean useDefaultLocation)
    {
        Stop anchor = null;
//        // 1. Get precisely gps fix
//        TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS);
//        // 2. Get network fix
//        if (location == null)
//        {
//            location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_NETWORK);
//        }
//
//        if (location == null)
//        {
//            // 3. Get last know gps fix
//            location = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS);
//            // 4. Get last know network fix
//            if (location == null)
//            {
//                location = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_NETWORK);
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
//        else
//        {
//            anchor = new Stop();
//            anchor.setType(Stop.STOP_CURRENT_LOCATION);
//            anchor.setLat(location.getLatitude());
//            anchor.setLon(location.getLongitude());
//        }
//
//        if (anchor == null && useDefaultLocation)
//        {
//            // TODO: DB, do we need popup error msg when there's no gps.
//            TnLocation defaultLocation = LocationProvider.getInstance().getDefaultLocation();
//            anchor = new Stop();
//            anchor.setType(Stop.STOP_CURRENT_LOCATION);
//            anchor.setLat(defaultLocation.getLatitude());
//            anchor.setLon(defaultLocation.getLongitude());
//        }
//
        return anchor;
    }
    
    public PoiSearchArgs cloneInstance()
    {
        PoiSearchArgs newArgs = new PoiSearchArgs(getSearchType(), getSearchFromType(), getAlongRouteType(), getSortType(),
                getCategoryId(), getPageSize(), getKeyWord(), getShowText(), getAnchorAddress(), getDestAddress(), getNavState());
        return newArgs;
    }
    
    public void clear()
    {
        currNavState = null;
        destAddress = null;
        anchorAddress = null;
        searchType = 0;
        sortType = 0;
        categoryId = -2;
        fromType = 0;
        alongRouteType = 0;
        keyword = "";
        showText = "";
    }
}
