package com.telenav.carconnect.provider;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.navsdk.events.PointOfInterestData;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchRequest;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchResponse;

public class CarConnectAddressProvider extends AbstractProvider
{
    private static Hashtable<Long, Address> savedPois = new Hashtable<Long, Address>();

    public void handle(String eventType, Object eventData)
    {
        // TODO Auto-generated method stub
        if (eventType.equals(CarConnectEvent.POI_SEARCH_REQUEST))
        {
            PoiSearchRequest request = (PoiSearchRequest) eventData;
            int categoryID = request.getCategoryToSearch();
            int searchId = request.getRequestId();

            if (categoryID == CarConnectEvent.WellKnownCategory_Work)
            {
                handleGetOfficeRequest(searchId);
            }
            else if (categoryID == CarConnectEvent.WellKnownCategory_Home)
            {
                handleGetHomeRequest(searchId);
            }
            else if (categoryID == CarConnectEvent.WellKnownCategory_Favorites)
            {
                handleGetFavoritesRequest(searchId);
            }
            else if (categoryID == CarConnectEvent.WellKnownCategory_Recents)
            {
                handleGetRecentsRequest(searchId);
            }
        }
    }

    @Override
    public void register()
    {
        getEventBus().subscribe(CarConnectEvent.POI_SEARCH_REQUEST, this);
    }

    public void unregister()
    {
        getEventBus().unsubscribe(CarConnectEvent.POI_SEARCH_REQUEST, this);
    }

    private void handleGetOfficeRequest(int searchId)
    {
        Stop officeStop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
        PointOfInterest office = (officeStop == null) ? null : ProtocolConvertor.convertStopToPointOfInterest(officeStop, true)
                .build();
        PoiSearchResponse.Builder builder = PoiSearchResponse.newBuilder();
        builder.setRequestId(searchId);

        if (office != null)
        {
            PointOfInterestData.PoiSearchResult.Builder resultBuilder = PointOfInterestData.PoiSearchResult.newBuilder();
            resultBuilder.setPoi(office);
            builder.addResults(resultBuilder);
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_ExactMatch);
        }
        else
        {
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_NoMatch);
        }
        getEventBus().broadcast(CarConnectEvent.POI_SEARCH_RESPONSE, builder.build());
    }

    private void handleGetHomeRequest(int searchId)
    {
        Stop homeStop = DaoManager.getInstance().getAddressDao().getHomeAddress();
        PointOfInterest home = (homeStop == null) ? null : ProtocolConvertor.convertStopToPointOfInterest(homeStop, true)
                .build();
        PoiSearchResponse.Builder builder = PoiSearchResponse.newBuilder();
        builder.setRequestId(searchId);
        if (home != null)
        {
            PointOfInterestData.PoiSearchResult.Builder resultBuilder = PointOfInterestData.PoiSearchResult.newBuilder();
            resultBuilder.setPoi(home);
            builder.addResults(resultBuilder);
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_ExactMatch);
        }
        else
        {
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_NoMatch);
        }
        getEventBus().broadcast(CarConnectEvent.POI_SEARCH_RESPONSE, builder.build());
    }

    private void handleGetFavoritesRequest(int searchId)
    {
        PoiSearchResponse.Builder builder = PoiSearchResponse.newBuilder();
        builder.setRequestId(searchId);
        Vector<?> vec = DaoManager.getInstance().getAddressDao().getFavorateAddresses();
        int size = vec.size();
        if (size > 0)
        {
            for (int index = 0; index < size; ++index)
            {
                Address address = (Address) vec.get(index);
                PointOfInterest poi = ProtocolConvertor.convertAddressToPointOfInterest(address).build();
                PointOfInterestData.PoiSearchResult.Builder resultBuilder = PointOfInterestData.PoiSearchResult.newBuilder();
                resultBuilder.setPoi(poi);
                builder.addResults(resultBuilder);
                // save it to keep track what is sent to HU so it can be deleted from HU
                savedPois.put(Long.valueOf(address.getId()), address);
            }
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_ExactMatch);
        }
        else
        {
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_NoMatch);
        }
        getEventBus().broadcast(CarConnectEvent.POI_SEARCH_RESPONSE, builder.build());
    }

    private void handleGetRecentsRequest(int searchId)
    {
        PoiSearchResponse.Builder builder = PoiSearchResponse.newBuilder();
        builder.setRequestId(searchId);
        Vector<?> vec = DaoManager.getInstance().getAddressDao().getRecentAddresses();
        int size = vec.size();
        if (size > 0)
        {
            for (int index = 0; index < size; ++index)
            {
                Address address = (Address) vec.get(index);
                PointOfInterest poi = ProtocolConvertor.convertAddressToPointOfInterest(address).build();
                PointOfInterestData.PoiSearchResult.Builder resultBuilder = PointOfInterestData.PoiSearchResult.newBuilder();
                resultBuilder.setPoi(poi);
                builder.addResults(resultBuilder);
            }
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_ExactMatch);
        }
        else
        {
            builder.setStatus(PointOfInterestData.SearchStatusCode.SearchStatus_NoMatch);
        }
        getEventBus().broadcast(CarConnectEvent.POI_SEARCH_RESPONSE, builder.build());
    }

}
