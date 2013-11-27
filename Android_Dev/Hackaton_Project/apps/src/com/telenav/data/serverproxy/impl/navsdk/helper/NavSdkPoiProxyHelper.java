/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkPointOfInterestProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.navsdk.AbstractNavSdkServerProxy;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkProxyUtil;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkValidateAddressProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkNavigationProxyHelper.NavSdkNavigationUtil;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.poi.PoiSearchArgs;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.navsdk.events.PointOfInterestData.SearchEntryMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchMode;
import com.telenav.navsdk.events.PointOfInterestData.SearchStatusCode;
import com.telenav.navsdk.events.PointOfInterestData.SearchType;
import com.telenav.navsdk.events.PointOfInterestData.SortBy;
import com.telenav.navsdk.events.PointOfInterestEvents.AutocompleteAddressError;
import com.telenav.navsdk.events.PointOfInterestEvents.AutocompleteAddressRequest;
import com.telenav.navsdk.events.PointOfInterestEvents.AutocompleteAddressResponse;
import com.telenav.navsdk.events.PointOfInterestEvents.CancelPoiSearchRequest;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchError;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchRequest;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchResponse;
import com.telenav.navsdk.events.PointOfInterestEvents.ReverseGeocodeError;
import com.telenav.navsdk.events.PointOfInterestEvents.ReverseGeocodeRequest;
import com.telenav.navsdk.events.PointOfInterestEvents.ReverseGeocodeResponse;
import com.telenav.navsdk.events.PointOfInterestEvents.SearchSuggestion;
import com.telenav.navsdk.services.PointOfInterestListener;
import com.telenav.navsdk.services.PointOfInterestServiceProxy;

/**
 * Don't put any business data or class member into the helper class.
 * 
 * @author hchai
 * @date 2011-11-28
 */
public class NavSdkPoiProxyHelper implements PointOfInterestListener, INavSdkProxyConstants
{
    private static NavSdkPoiProxyHelper instance;
    
    private PointOfInterestServiceProxy serverProxy;

    private HashMap<String, AbstractNavSdkServerProxy> listeners = new HashMap<String, AbstractNavSdkServerProxy>();
    
    private HashMap<Integer, String> actionTable = new HashMap<Integer, String>();
    
    private int searchId = 0;
    
    private NavSdkPoiProxyHelper()
    {
    }
    
    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavSdkPoiProxyHelper();
            instance.setEventBus(bus);
        }
    }
    
    public static NavSdkPoiProxyHelper getInstance()
    {
        return instance;
    }
    
    public void registerRequestCallback(String action, AbstractNavSdkServerProxy proxy)
    {
        listeners.put(action, proxy);
    }
    
    private void setEventBus(EventBus bus)
    {
        serverProxy = new PointOfInterestServiceProxy(bus);
        serverProxy.addListener(this);
    }
    
    private int requestSearchService(String action, PoiSearchRequest request, NavSdkSearchProxy proxy)
    {
        searchId++;
        actionTable.put(searchId, action);
        this.poiSearch(PoiSearchRequest.newBuilder(request).setRequestId(searchId).build(), action, proxy);
        return searchId;
    }
    
    protected int requestRgcService(String action, int lat, int lon, NavSdkSearchProxy proxy)
    {
        com.telenav.navsdk.events.PointOfInterestData.Location.Builder lb = com.telenav.navsdk.events.PointOfInterestData.Location.newBuilder();
        lb.setAltitude(0);
        if(lat !=0 && lon != 0)
        {
            lb.setLatitude(lat /100000.0d);
            lb.setLongitude(lon /100000.0d);
        }
        else
        {
            lb.setLatitude(37.37878);
            lb.setLongitude(-122.00098);
        }
        
        searchId ++;
        ReverseGeocodeRequest.Builder request = ReverseGeocodeRequest.newBuilder();
        request.setLoc(lb);
        request.setRequestId(searchId);
        actionTable.put(searchId, action);
        
        this.reverseGeocode(request.build(), proxy);
        
        return searchId;
    }
    
    private int requestSearchService(String action, String transactionId, String queryString, int firstIndex, int maxResultSize, int categoryId,
            SearchMode searchMode, SortBy sortBy, SearchEntryMode searchEntryMode, SearchType searchType, Address nearPoi, NavSdkSearchProxy proxy)
    {
        
        PoiSearchRequest.Builder builder = PoiSearchRequest.newBuilder();
        
        searchId ++;
        builder.setRequestId(searchId);
        actionTable.put(searchId, action);
        
        builder.setRelatedTransactionId(transactionId);
        if(queryString != null && queryString.trim().length() > 0)
        {
            builder.setQueryString(queryString);
        }
        builder.setFirstResultIndex(firstIndex);
        
        if(maxResultSize > 0)
        {
            builder.setMaxNumberOfResults(maxResultSize);
        }
        
        if(searchType != null)
        {
            builder.setSearchType(searchType);
            if(searchType == SearchType.SearchType_CategorySearch)
            {
                builder.setCategoryToSearch(categoryId);
            }
        }
        if(searchMode != null)
        {
            builder.setSearchMode(searchMode);
        }
        if(sortBy != null)
        {
            builder.setSortBy(sortBy);
        }
        if(searchEntryMode != null)
        {
            builder.setSearchEntryMode(searchEntryMode);
        }
        if(nearPoi != null)
        {
            builder.setNearPoi(NavSdkProxyUtil.convertProtoPointOfInterest(nearPoi));
        }
        else
        {
            com.telenav.navsdk.events.PointOfInterestData.Location.Builder lb = com.telenav.navsdk.events.PointOfInterestData.Location.newBuilder();

            TnLocation currentLocation = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            if (currentLocation != null)
            {
                lb.setAltitude(0);
                lb.setLatitude(NavSdkNavigationUtil.convertCoordinate(currentLocation.getLatitude()));
                lb.setLongitude(NavSdkNavigationUtil.convertCoordinate(currentLocation.getLongitude()));                
            }

            PointOfInterest.Builder pb = PointOfInterest.newBuilder();
            pb.setLocation(lb.build());
            builder.setNearPoi(pb.build());
        }
        
        this.poiSearch(builder.build(), action, proxy);
        
        return searchId;
    }
    
    private void requestCancelAllPoiSearch(NavSdkSearchProxy proxy)
    {
        Iterator it = listeners.values().iterator();
        while (it.hasNext())
        {
            NavSdkSearchProxy p = (NavSdkSearchProxy) it.next();
            requestCancelPoiSearch(p.getSearchId(), null);
        }
    }
    
    private void requestCancelPoiSearch(int searchId, NavSdkSearchProxy proxy)
    {
        CancelPoiSearchRequest.Builder builder = CancelPoiSearchRequest.newBuilder();
        builder.setRequestId(searchId);
        this.cancelPoiSearch(builder.build(), proxy);
        String action = actionTable.remove(searchId);
        listeners.remove(action);
    }
    
    //Unlike others: autosuggest may have two response, so we should not remove it when one response arrive
    private void poiSearch(PoiSearchRequest request, String action, NavSdkSearchProxy proxy) 
    {
        System.out.println("Casper: new search :" + request.getRequestId());
        
        NavSdkSearchProxy oldProxy = (NavSdkSearchProxy) listeners.remove(action);
        if(oldProxy != null)
        {
            requestCancelPoiSearch(oldProxy.getSearchId(), null);
        }

        registerRequestCallback(action, proxy);
        serverProxy.poiSearch(request);
    }
    
    private void cancelPoiSearch(CancelPoiSearchRequest request, NavSdkSearchProxy proxy)
    {
        serverProxy.cancelPoiSearch(request);
    }
    
    public void reverseGeocode(ReverseGeocodeRequest request, NavSdkSearchProxy proxy)
    {
        registerRequestCallback(ACT_POI_REVERSE_GEOCODE, proxy);
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), " CR send rgc!!!");
        }
        serverProxy.reverseGeocode(request);
    }
    
    public void autocompleteAddress(AutocompleteAddressRequest request, NavSdkValidateAddressProxy proxy)
    {
        registerRequestCallback(IServerProxyConstants.ACT_VALIDATE_ADDRESS, proxy);
        serverProxy.autocompleteAddress(request);
    }

    
    @Override
    //052
    public void onPoiSearchResponse(PoiSearchResponse event)
    {
        System.out.println("Casper: Search complete" + event.getResultsCount() + ": id: " + event.getRequestId());
        String action = actionTable.remove(event.getRequestId());
        NavSdkSearchProxy proxy = (NavSdkSearchProxy) listeners.remove(action);
        if (proxy != null)
        {
            if(event.getStatus() == SearchStatusCode.SearchStatus_NoMatch)
            {
                proxy.setResultType(PoiSearchArgs.TYPE_NO_RESULTS);
                proxy.setStatus(IServerProxyConstants.FAILED);
            }
            else
            {
                proxy.setStatus(IServerProxyConstants.SUCCESS);
                if(event.getStatus() == SearchStatusCode.SearchStatus_ExactMatch)
                {
                    proxy.setFuzzyMatched(false);
                }
                else if(event.getStatus() == SearchStatusCode.SearchStatus_FuzzyMatch)
                {
                    proxy.setFuzzyMatched(true);
                }
            }
            
            if(event.hasTotalAvailableResults())
            {
                proxy.setTotalCount(event.getTotalAvailableResults());
            }
//            if(event.hasSearchComplete())
//            {
//                proxy.setHasMore(event.getSearchComplete());//Fix me
//            }
            
            if (event.getResultsCount() > 0)
            {
                Vector<Address> addresses = new Vector<Address>();
                for (int i = 0; i < event.getResultsCount(); i++)
                {
                    Address addr = NavSdkProxyUtil.convertSearchResult(event.getResults(i));
                    addresses.addElement(addr);
                }
                proxy.setAddresses(addresses);
                if (event.getResults(0).getPoi().hasPoiId())
                {
                    proxy.setResultType(PoiSearchArgs.TYPE_POI_RESULTS);
                }
                else
                {
                    proxy.setResultType(PoiSearchArgs.TYPE_STOPS_RESULTS);
                }
            }

            if(event.getSponsoredResultsCount() > 0)
            {
                Vector<Address> sponsoredResults = new Vector<Address>();
                for (int i = 0; i < event.getSponsoredResultsCount(); i++)
                {
                    Address sponsoredResult = NavSdkProxyUtil.convertSponsoredResult(event.getSponsoredResults(i));
                    sponsoredResults.addElement(sponsoredResult);
                }
                proxy.setResultType(PoiSearchArgs.TYPE_POI_RESULTS);
                proxy.setSponsoredPois(sponsoredResults);
            }
            
            if(event.getSuggestionCount() > 0)
            {
                Vector<OneBoxSearchBean> suggestions = new Vector<OneBoxSearchBean>();
                for (int i = 0; i < event.getSuggestionCount(); i++)
                {
                    OneBoxSearchBean suggestion = NavSdkProxyUtil.convertSearchBean(event.getSuggestion(i));
                    suggestions.addElement(suggestion);
                }
                proxy.setResultType(PoiSearchArgs.TYPE_SUGGESTS_RESULTS);
                proxy.setSuggestions(suggestions);
                proxy.setHasSuggestionReturn(true);
                
            }
            else
            {
                proxy.setHasSuggestionReturn(false);
            }
            proxy.transactionFinished(action, proxy);
        }
    }
    

    public void onSearchSuggestion(SearchSuggestion event)
    {
        /***************************************************/
        /******This should be only a data not a event*******/
        /******So It should never be called*****************/
        /***************************************************/
        
        String action = "Fatal Error";
        NavSdkSearchProxy proxy = (NavSdkSearchProxy) listeners.get(action);
        if (proxy != null)
        {
            proxy.setErrorMsg("Fatal Error: please check instantly");
            proxy.transactionError(action, proxy);
        }
    }

    @Override
    public void onPoiSearchError(PoiSearchError event)
    {
        String action = actionTable.remove(event.getRequestId());
        AbstractNavSdkServerProxy proxy = listeners.get(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getError().toString());
            proxy.transactionError(action, proxy);
        }
    }
    

    @Override
    public void onReverseGeocodeResponse(ReverseGeocodeResponse event)
    {
        String action = actionTable.remove(event.getRequestId());
        NavSdkSearchProxy proxy = (NavSdkSearchProxy) listeners.remove(action);
        if(proxy == null)
        {
            proxy =  (NavSdkSearchProxy) listeners.remove(ACT_POI_REVERSE_GEOCODE);
        }
        if (proxy != null)
        {
            Vector matches = new Vector();
            matches.add(NavSdkProxyUtil.convertAddress(event.getPoi(), false));
            proxy.setAddresses(matches);
            proxy.transactionFinished(action, proxy);
        }
    }


    @Override
    public void onReverseGeocodeError(ReverseGeocodeError event)
    {
        String action = actionTable.remove(event.getRequestId());
        NavSdkSearchProxy proxy = (NavSdkSearchProxy) listeners.remove(action);
        if(proxy == null)
        {
            proxy =  (NavSdkSearchProxy) listeners.remove(ACT_POI_REVERSE_GEOCODE);
        }
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getError().toString());
            proxy.transactionError(action, proxy);
        }
    }


    @Override
    public void onAutocompleteAddressResponse(AutocompleteAddressResponse event)
    {
        String action = IServerProxyConstants.ACT_VALIDATE_ADDRESS;
        NavSdkValidateAddressProxy proxy = (NavSdkValidateAddressProxy) listeners.remove(action);
        if (proxy != null)
        {
            if (event.getMatchesCount() > 0)
            {
                Vector matches = new Vector();
                for (int i = 0; i < event.getMatchesCount(); i++)
                {
                    matches.add(NavSdkProxyUtil.convertAddress(event.getMatches(i).getPoi(), false));
                }
                proxy.setSimilarAddresses(matches);
            }
            proxy.transactionFinished(action, proxy);
        }
    }


    @Override
    public void onAutocompleteAddressError(AutocompleteAddressError event)
    {
        String action = IServerProxyConstants.ACT_VALIDATE_ADDRESS;
        AbstractNavSdkServerProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(event.getError().toString());
            proxy.transactionError(action, proxy);
        }
    }
    
    public static class NavSdkSearchProxy extends AbstractNavSdkServerProxy
    {
        protected NavSdkPoiProxyHelper helper;
        
        private int searchId;
        private int totalCount;
        private boolean hasMore;
        private boolean isFuzzyMatched;
        protected int resultType = PoiSearchArgs.TYPE_NO_RETURN;
        private Vector<Address> addresses = new Vector<Address>();
        private Vector<Address> sponsoredResult = new Vector<Address>();
        private Vector<OneBoxSearchBean> suggestions = new Vector<OneBoxSearchBean>();
        private boolean hasSuggestionReturn;
        
        public NavSdkSearchProxy(IServerProxyListener listener)
        {
            super(listener);
            helper = NavSdkPoiProxyHelper.getInstance();
        }
        
        public void setResultType(int type)
        {
            this.resultType = type;
        }
        
        public int getResultType()
        {
            return resultType;
        }
        
        public void setAddresses(Vector addresses)
        {
            this.addresses = addresses;
        }
        
        public Vector getAddresses()
        {
            return addresses;
        }
        
        public void setSponsoredPois(Vector sponsoredPois)
        {
            this.sponsoredResult = sponsoredPois;
        }
        
        public Vector getSponsoredPois()
        {
            return sponsoredResult;
        }
        
        public void setFuzzyMatched(boolean isFuzzyMatched)
        {
            this.isFuzzyMatched = isFuzzyMatched;
        }
        
        public boolean isFuzzyMatched()
        {
            return isFuzzyMatched;
        }
        
        public int getTotalCount()
        {
            return this.totalCount;
        }
        
        public void setTotalCount(int count)
        {
            this.totalCount = count;
        }
        
        public boolean hasMore()
        {
            return this.hasMore;
        }
        
        public void setHasMore(boolean hasMore)
        {
            this.hasMore = hasMore;
        }

        public Vector getSuggestions()
        {
            return suggestions;
        }
        
        public void setHasSuggestionReturn(boolean hasSuggestionReturn)
        {
            this.hasSuggestionReturn = hasSuggestionReturn;
        }
        
        public boolean hasSuggestionReturn()
        {
            return this.hasSuggestionReturn;
        }
        
        public void setSuggestions(Vector suggestions)
        {
            this.suggestions = suggestions;
        }
        
        public void removeSuggestions()
        {
            if (this.suggestions != null)
            {
                this.suggestions.removeAllElements();
            }
        }

        protected void requestSearchService(String action, String transactionId, String queryString, int firstIndex, int maxResultSize, int categoryId,
                SearchMode searchMode, SortBy sortBy, SearchEntryMode searchEntryMode, SearchType searchType, Address nearPoi)
        {
            this.searchId = helper.requestSearchService(action, transactionId, queryString, firstIndex, maxResultSize, categoryId, searchMode, sortBy, searchEntryMode, searchType, nearPoi, this);
        }
        
        protected void requestRgcService(String action, int lat, int lon)
        {
            this.searchId = helper.requestRgcService(action, lat, lon, this);
        }
        
        protected void requestSearchService(String action, PoiSearchRequest request)
        {
            this.searchId = helper.requestSearchService(action, request, this);
        }
        
        public int getSearchId()
        {
            return this.searchId;
        }
        
        public void sendCancelPoiSearchRequest()
        {
            helper.requestCancelAllPoiSearch(this);
        }
        
        public void reset()
        {
            super.reset();
            this.setAddresses(null);
            resultType = PoiSearchArgs.TYPE_NO_RETURN;
        }

    }
}


