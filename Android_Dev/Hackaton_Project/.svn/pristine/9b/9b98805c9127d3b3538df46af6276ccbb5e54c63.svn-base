package com.telenav.carconnect.provider;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.navsdk.events.PointOfInterestData.SortBy;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchRequest;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchResponse;

public class CarConnectPoiSearchProvider extends AbstractProvider implements PoiDataListener
{

    private static final int DEFAULT_SEARCH_TYPE = 5;

    private static final int DEFAULT_SEARCH_FROM_TYPE = 1;

    private static final int DEFAULT_ALONG_ROUTE = -1;

    public void handle(String eventType, Object eventData)
    {
        if (eventType.equals(CarConnectEvent.POI_SEARCH_REQUEST))
        {
            PoiSearchRequest poiSearchRequest = (PoiSearchRequest) eventData;
            int categoryID = poiSearchRequest.getCategoryToSearch();
            if (categoryID == CarConnectEvent.WellKnownCategory_Favorites
                    || categoryID == CarConnectEvent.WellKnownCategory_Home
                    || categoryID == CarConnectEvent.WellKnownCategory_Recents
                    || categoryID == CarConnectEvent.WellKnownCategory_Work)
            {
                return;
            }

            // TODO - check the searchId stuff.
            String searchId = String.valueOf(poiSearchRequest.getRequestId());
            int categoryId = poiSearchRequest.getCategoryToSearch();
            int firstResultIndex = poiSearchRequest.getFirstResultIndex();
            int maxPageSize = poiSearchRequest.getMaxNumberOfResults();
            String queryString = poiSearchRequest.getQueryString();
            SortBy sortBy = poiSearchRequest.getSortBy();
            int sortType = 0;
            if (sortBy != null)
                sortType = sortBy.getNumber();
            int searchFromType = DEFAULT_SEARCH_FROM_TYPE;

            if (categoryId == -2000)
            {
                categoryId = -2;
                sortType = -1;
                searchFromType = -1;
            }

            Stop stop = null;

            if (poiSearchRequest.hasNearPoi())
            {
                PointOfInterest poi = poiSearchRequest.getNearPoi();
                if (poi != null)
                {
                    stop = ProtocolConvertor.convertPointOfInterestToAddress(poi).getStop();
                }
            }

            SearchLC slc = new SearchLC(searchId, DEFAULT_SEARCH_TYPE, searchFromType, DEFAULT_ALONG_ROUTE, sortType,
                    categoryId, firstResultIndex, maxPageSize, queryString, "", stop, null, null,
                    IOneBoxSearchProxy.TYPE_INPUTTYPE_ANY, 0);

            if (stop != null)
            {
                slc.sendRequest();
            }
            else
            {
                LocationRequester lr = new LocationRequester(slc);
                lr.requestLocation();
            }
        }
    }

    public void poiResultUpdate(int status, int resultType, String msg, PoiDataWrapper poiDataWrapper)
    {
        if (status == PoiDataRequester.TYPE_SUCCESS)
        {
            PoiSearchResponse response = ProtocolConvertor.convertPoiDataWrapperToPoiSearchResponse(poiDataWrapper);
            getEventBus().broadcast(CarConnectEvent.POI_SEARCH_RESPONSE, response);
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: PoiSearchProvider - search succeed, send response");
        }
        else
        {
            sendErrorResponse();
        }
    }

    private class SearchLC implements ILocationCallback
    {
        int searchType;

        int searchFromType;

        int alongRouteType;

        int sortType;

        int categoryId;

        int pageNum;

        int pageSize;

        String searchKeyWord;

        String showText;

        Stop anchorStop;

        Stop destStop;

        NavState navState;

        int inputType;

        int sponsorNum;

        PoiDataWrapper poiDataWrapper;

        public SearchLC(String searchId, int searchType, int searchFromType, int alongRouteType, int sortType, int categoryId,
                int pageNum, int pageSize, String searchKeyWord, String showText, Stop anchorStop, Stop destStop,
                NavState navState, int inputType, int sponsorNum)
        {
            this.searchType = searchType;
            this.searchFromType = searchFromType;
            this.alongRouteType = alongRouteType;
            this.sortType = sortType;
            this.categoryId = categoryId;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.searchKeyWord = searchKeyWord;
            this.showText = showText;
            this.anchorStop = anchorStop;
            this.destStop = destStop;
            this.navState = navState;
            this.inputType = inputType;
            this.sponsorNum = sponsorNum;
            this.poiDataWrapper = new PoiDataWrapper(searchId);
        }

        private void sendRequest()
        {
            poiDataWrapper.setSearchArgs(0, searchType, searchFromType, alongRouteType, sortType, categoryId, pageNum,
                pageSize, searchKeyWord, showText, anchorStop, destStop, navState, inputType, sponsorNum);
            PoiDataRequester requester = new PoiDataRequester(new UserProfileProvider());
            requester.doRequestPoi(poiDataWrapper, CarConnectPoiSearchProvider.this);
        }

        @Override
        public void onSuccess(TnLocation loc)
        {
            Stop stop = new Stop();
            stop.setLat(loc.getLatitude());
            stop.setLon(loc.getLongitude());
            anchorStop = stop;
            sendRequest();
        }

        @Override
        public void onError()
        {
            sendErrorResponse();
        }
    }

    @Override
    public void register()
    {
        getEventBus().subscribe(CarConnectEvent.POI_SEARCH_REQUEST, this);

    }

    @Override
    public void unregister()
    {
        getEventBus().unsubscribe(CarConnectEvent.POI_SEARCH_REQUEST, this);

    }

    private void sendErrorResponse()
    {
        // TODO - follow NAVSDK error protocol
        PoiSearchResponse.Builder poiSearchResponseBuilder = PoiSearchResponse.newBuilder();
        PoiSearchResponse response = poiSearchResponseBuilder.build();
        getEventBus().broadcast(CarConnectEvent.POI_SEARCH_RESPONSE, response);
        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: PoiSearchProvider - error detect, send error response");
    }
}
