/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * PoiSearchResponseConvertor.java
 *
 */
package com.telenav.carconnect.provider;

import java.util.List;

import android.util.Base64;

import com.telenav.carconnect.provider.abbrev.SegmentName;
import com.telenav.carconnect.provider.abbrev.SegmentNameParser;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.navsdk.events.PointOfInterestData;
import com.telenav.navsdk.events.PointOfInterestData.PoiSearchResult;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.navsdk.events.PointOfInterestData.StringKeyValuePair;
import com.telenav.navsdk.events.PointOfInterestEvents.PoiSearchResponse;
import com.telenav.res.ResourceManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author tpeng
 * @date 2012-2-2
 */
public class ProtocolConvertor
{

    public static final double LAT_AND_LON_CONVERT_RATE = 100000d;

    private static final String SEARCHID_KEY = "SEARCHID";

    private static final String ADDRESS_KEY = "ADDRESS";

    public static PoiSearchResponse convertPoiDataWrapperToPoiSearchResponse(PoiDataWrapper poiDataWrapper)
    {
        if (poiDataWrapper == null || poiDataWrapper.getAddressSize() <= 0)
        {
            return null;
        }

        PoiSearchResponse.Builder poiSearchResponseBuilder = PoiSearchResponse.newBuilder();

        String searchId = poiDataWrapper.getSearchUid();
        int searchIdInt = 0;
        try
        {
            if (searchId != null)
                searchIdInt = Integer.parseInt(searchId);
        }
        catch (Exception e)
        {
            // TODO - log exception
        }
        poiSearchResponseBuilder.setRequestId(searchIdInt);

        int size = poiDataWrapper.getNormalAddressSize();
        if (size > 0)
        {
            for (int index = 0; index < size; ++index)
            {
                Address address = (Address) poiDataWrapper.getNormalAddress(index);
                if (address == null || !address.isValid() || (address.getPoi() == null))
                    continue;

                String phone = address.getPhoneNumber();
                String label = address.getLabel();
                long createTime = address.getCreateTime();
                long updateTime = address.getUpdateTime();

                Poi poi = address.getPoi();
                int popularity = poi.getPopularity();
                int rating = poi.getRating();

                if (poi.getStop() != null)
                {
                    Stop stop = poi.getStop();
                    PointOfInterest.Builder pointOfInterestBuilder = convertStopToPointOfInterest(stop, false);
                    if (label != null)
                        pointOfInterestBuilder.setName(label);
                    if (phone != null)
                        pointOfInterestBuilder.setPhone(phone);
                    pointOfInterestBuilder.setCreateTime(createTime);
                    pointOfInterestBuilder.setUpdateTime(updateTime);
                    pointOfInterestBuilder.setRating(rating);
                    pointOfInterestBuilder.setPopularity(popularity);

                    BizPoi bizPoi = poi.getBizPoi();
                    if (bizPoi != null)
                    {
                        int distInt = bizPoi.getDistance() != null ? Integer.parseInt(bizPoi.getDistance()) : 0;
                        if (distInt > 0)
                        {
                            Preference distanceUnit = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
                                Preference.ID_PREFERENCE_DISTANCEUNIT);
                            int distanceUnitValue = Preference.UNIT_METRIC;
                            if (distanceUnit != null)
                            {
                                distanceUnitValue = distanceUnit.getIntValue();
                            }
                            String distStr = ResourceManager.getInstance().getStringConverter()
                                    .convertDistanceMeterToMile(distInt, distanceUnitValue);
                            StringKeyValuePair.Builder stringKeyValuePairBuilder = createStringKeyValuePairBuilder("Distance",
                                distStr);
                            pointOfInterestBuilder.addExtraAttributes(stringKeyValuePairBuilder);
                        }
                    }

                    // add the search Id/original address to result.

                    if (searchId != null)
                        pointOfInterestBuilder.addExtraAttributes(createStringKeyValuePairBuilder(SEARCHID_KEY, searchId));
                    String addrData = serializeAddress(address);
                    pointOfInterestBuilder.addExtraAttributes(createStringKeyValuePairBuilder(ADDRESS_KEY, addrData));

                    PoiSearchResult.Builder poiSearchResultBuilder = PoiSearchResult.newBuilder();
                    poiSearchResultBuilder.setPoi(pointOfInterestBuilder);
                    poiSearchResponseBuilder.addResults(poiSearchResultBuilder);
                }
            }
            PoiSearchResponse response = poiSearchResponseBuilder.build();
            return response;
        }

        int addressSize = poiDataWrapper.getAddressSize();
        if (addressSize > 0)
        {
            for (int i = 0; i < addressSize; ++i)
            {
                Address address = poiDataWrapper.getAddress(i);
                if (address != null)
                {
                    Poi poi = address.getPoi();
                    if (poi != null && poi.isAdsPoi())
                    {
                        continue;
                    }
                    PointOfInterest.Builder pointOfInterestBuilder = convertAddressToPointOfInterest(address);
                    PoiSearchResult.Builder poiSearchResultBuilder = PoiSearchResult.newBuilder();
                    poiSearchResultBuilder.setPoi(pointOfInterestBuilder);
                    poiSearchResponseBuilder.addResults(poiSearchResultBuilder);
                }
            }
        }
        return poiSearchResponseBuilder.build();
    }

    public static PointOfInterest.Builder convertStopToPointOfInterest(Stop stop, boolean storeAddress)
    {
        PointOfInterest.Builder pointOfInterestBuilder = PointOfInterest.newBuilder();
        if (stop == null)
            return pointOfInterestBuilder;

        String country = stop.getCountry();
        String province = stop.getProvince();
        String city = stop.getCity();
        String streetName = stop.getStreetName();
        String crossStreetName = stop.getCrossStreetName();
        String streetNumber = stop.getHouseNumber();
        String postalCode = stop.getPostalCode();

        PointOfInterestData.Address.Builder addressBuilder = PointOfInterestData.Address.newBuilder();
        if (country != null)
            addressBuilder.setCountry(country);
        if (province != null)
            addressBuilder.setProvince(province);
        if (city != null)
            addressBuilder.setCity(city);
        if (streetName != null)
            addressBuilder.setStreetName(streetName);
        if (crossStreetName != null)
            addressBuilder.setCrossStreetName(crossStreetName);
        if (streetNumber != null)
            addressBuilder.setDoorNumber(streetNumber);
        if (postalCode != null)
            addressBuilder.setPostalCode(postalCode);
        String formattedAddress = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
        if (formattedAddress != null)
            addressBuilder.setFormattedAddress(formattedAddress);

        pointOfInterestBuilder.setAddress(addressBuilder);

        int lat = stop.getLat();
        int lon = stop.getLon();
        PointOfInterestData.Location.Builder locationBuilder = PointOfInterestData.Location.newBuilder();
        locationBuilder.setLatitude(((double) lat) / LAT_AND_LON_CONVERT_RATE);
        locationBuilder.setLongitude(((double) lon) / LAT_AND_LON_CONVERT_RATE);
        pointOfInterestBuilder.setLocation(locationBuilder);
        if (streetName != null)
            pointOfInterestBuilder.setName(streetName);

        if (storeAddress)
        {
            Address addr = new Address(Address.SOURCE_PREDEFINED);
            addr.setStop(stop);
            StringKeyValuePair.Builder bu = createStringKeyValuePairBuilder(ADDRESS_KEY, serializeAddress(addr));
            pointOfInterestBuilder.addExtraAttributes(bu);
        }

        return pointOfInterestBuilder;
    }

    public static PointOfInterest.Builder convertAddressToPointOfInterest(Address address)
    {
        PointOfInterest.Builder pointOfInterestBuilder;

        Stop stop = address.getStop();
        if (stop != null)
        {
            pointOfInterestBuilder = convertStopToPointOfInterest(stop, false);
        }
        else
        {
            pointOfInterestBuilder = PointOfInterest.newBuilder();
        }
        long id = address.getId();
        pointOfInterestBuilder.setPoiId(PrimitiveTypeCache.valueOf(id));

        String name = address.getLabel();
        String phone = address.getPhoneNumber();

        Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
        Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();

        if (stop != null)
        {
            if (stop.equalsIgnoreCase(home))
            {
                name = "home";
            }
            else if (stop.equalsIgnoreCase(work))
            {
                name = "work";
            }
        }

        if ((name == null || name.trim().length() == 0) && stop != null)
        {
            name = ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
        }

        if (address.getPoi() == null)
        {
            // We only need to expand abbrev. for non POI type.
            name = expandLabelAbbrev(name, stop);
        }

        pointOfInterestBuilder.setName(name);

        if (phone != null)
            pointOfInterestBuilder.setPhone(phone);

        String addrData = serializeAddress(address);
        pointOfInterestBuilder.addExtraAttributes(createStringKeyValuePairBuilder(ADDRESS_KEY, addrData));

        return pointOfInterestBuilder;
    }

    private static String expandLabelAbbrev(String name, Stop stop)
    {
        String firstline = stop.getFirstLine();
        if (firstline == null || name == null || !firstline.contains(name))
        {
            return name;
        }
        SegmentNameParser sp = new SegmentNameParser();
        SegmentName segment = sp.parse(name);

        return segment.getExpandedName();
    }

    public static Address convertPointOfInterestToAddress(PointOfInterest poi)
    {
        Address addr = new Address();
        addr.setLabel(poi.getName());
        addr.setPhoneNumber(poi.getPhone());
        addr.setCreateTime(poi.getCreateTime());
        addr.setUpdateTime(poi.getUpdateTime());

        Stop stop = new Stop();
        if (poi.hasAddress())
        {
            PointOfInterestData.Address poiAddr = poi.getAddress();
            stop.setCity(poiAddr.getCity());
            stop.setCountry(poiAddr.getCountry());
            stop.setCounty(poiAddr.getCounty());
            stop.setCrossStreetName(poiAddr.getCrossStreetName());
            stop.setPostalCode(poiAddr.getPostalCode());
            stop.setProvince(poiAddr.getProvince());
            stop.setStreetName(poiAddr.getStreetName());
            stop.setHouseNumber(poiAddr.getDoorNumber());
        }
        if (poi.hasLocation())
        {
            PointOfInterestData.Location location = poi.getLocation();
            stop.setLat((int) (location.getLatitude() * 100000));
            stop.setLon((int) (location.getLongitude() * 100000));
        }

        Poi poiData = new Poi();
        poiData.setStop(stop);
        poiData.setPopularity((int) poi.getPopularity());
        poiData.setPriceRange(poi.getPriceRange());
        poiData.setRating((int) poi.getRating());

        BizPoi bizPoi = new BizPoi();
        bizPoi.setPoiId(String.valueOf(poi.getPoiId()));
        bizPoi.setStop(stop);
        bizPoi.setPhoneNumber(poi.getPhone());

        poiData.setBizPoi(bizPoi);
        addr.setPoi(poiData);
        return addr;
    }

    public static String retrieveSearchId(PointOfInterest poi)
    {
        return retreiveValue(poi.getExtraAttributesList(), SEARCHID_KEY);
    }

    public static Address reitriveAddres(PointOfInterest poi)
    {
        String data = retreiveValue(poi.getExtraAttributesList(), ADDRESS_KEY);
        Address ret = null;
        if (data != null)
        {
            byte[] barray = null;
            barray = Base64.decode(data, Base64.DEFAULT);
            ret = SerializableManager.getInstance().getAddressSerializable().createAddress(barray);
        }
        return ret;
    }

    private static StringKeyValuePair.Builder createStringKeyValuePairBuilder(String key, String value)
    {
        StringKeyValuePair.Builder kvbuilder = StringKeyValuePair.newBuilder();
        kvbuilder.setKey(key);
        kvbuilder.setValue(value);
        return kvbuilder;
    }

    private static String retreiveValue(List<StringKeyValuePair> extraAttributesList, String key)
    {
        String ret = null;
        if (extraAttributesList != null && key != null)
        {
            for (StringKeyValuePair s : extraAttributesList)
            {
                if (key.equals(s.getKey()))
                {
                    ret = s.getValue();
                    break;
                }
            }
        }

        return ret;
    }

    private static String serializeAddress(Address addr)
    {
        byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(addr);
        byte[] base64Data = Base64.encode(data, Base64.DEFAULT);
        String ret = null;
        ret = new String(base64Data);
        return ret;
    }
}
