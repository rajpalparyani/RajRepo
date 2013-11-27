/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * JsonAddressSerializable.java
 *
 */
package com.telenav.data.serializable.json;

import java.util.Vector;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;
import org.json.tnme.JSONTokener;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.SentAddress;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serializable.IAddressSerializable;
import com.telenav.logger.Logger;
import com.telenav.util.URLDecoder;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-25
 */
class JsonAddressSerializable implements IAddressSerializable
{
    // keys for address
    public static final String KEY_ADDR_ID = "id";

    public static final String KEY_ADDR_TYPE = "type";

    public static final String KEY_ADDR_PHONE_NUM = "phoneNumber";

    public static final String KEY_ADDR_LABEL = "label";

    public static final String KEY_ADDR_STATUS = "status";

    public static final String KEY_ADDR_SHARED_FROM_PTN = "sharedFromPTN";

    public static final String KEY_ADDR_SHARED_FROM_USER = "sharedFromUser";

    public static final String KEY_ADDR_EXISTED_IN_FAVORITE = "existedInFavorite";
    
    public static final String KEY_ADDR_SELECTED_INDEX = "selectedIndex";
    
    public static final String KEY_ADDR_CATEGORY = "category";

    public static final String KEY_ADDR_STOP = "stop";

    public static final String KEY_ADDR_POI = "poi";
    
    //For Goby Events
    public static final String KEY_ADDR_EVENT_ID = "eventId";
    
    public static final String KEY_ADDR_EVENT_START_TIME = "startDateTime";
    
    public static final String KEY_ADDR_EVENT_END_TIME = "endDateTime";
    
    public static final String KEY_ADDR_USE_EVENT_START_TIME = "useStartTime";
    
    public static final String KEY_ADDR_USE_EVENT_END_TIME = "useEndTime";
    
    public static final String KEY_ADDR_EVENT_VENUE = "venue";
    
    public static final String KEY_ADDR_EVENT_DETAIL_AVAILABLE = "poiEventAvailable";
    
    public static final String KEY_IS_SPONSOR_POI = "isSponsorPoi";
    
    public static final String KEY_IS_ADS_POI = "isAdsPoi";
    
    public static final String KEY_IS_HAS_ADS_MENU = "hasAdsMenu";
    
    public static final String KEY_IS_HAS_DEAL = "hasDeals";
    
    public static final String KEY_IS_HAS_REVIEW = "hasReviews";
    
    public static final String KEY_IS_HAS_POI_MENU = "hasPoiMenu";
    
    public static final String KEY_IS_HAS_POI_EXTR_ATTR = "hasPoiExtraAttributes";
    
    public static final String KEY_IS_HAS_POI_DETAIL = "hasPoiDetails";
  
    public static final String KEY_S_POI_LOGO = "poiLogo";
    
    public static final String KEY_S_BRAND_LOGO = "brandLogo";
    
    public static final String KEY_S_CATEGORY_LOGO = "categoryLogo";
    
    public static final String KEY_S_MAP_PROVIDER = "mapProvider";
    
    // keys for stop
    public static final String KEY_STOP_TYPE = "type";

    public static final String KEY_STOP_LAT = "lat";

    public static final String KEY_STOP_LON = "lon";

    public static final String KEY_STOP_FIRSTLINE = "firstLine";

    public static final String KEY_STOP_LASTLINE = "lastLine";

    public static final String KEY_STOP_STATUS = "status";

    public static final String KEY_STOP_ID = "stopId";

    public static final String KEY_STOP_CITY = "city";

    public static final String KEY_STOP_ZIP = "zip";

    public static final String KEY_STOP_COUNTRY = "country";

    public static final String KEY_STOP_GEOCODED = "isGeocoded";

    public static final String KEY_STOP_PROVINCE = "province";

    public static final String KEY_STOP_STREETNAME = "streetName";

    public static final String KEY_STOP_LABEL = "label";

    public static final String KEY_STOP_CROSS_STREET = "crossStreet";
    
    public static final String KEY_STOP_HOUSE_NUMBER = "houseNumber";
    
    public static final String KEY_STOP_SUITE = "suite";
    
    public static final String KEY_STOP_COUNTY = "county";
    
    public static final String KEY_STOP_SUB_LOCALITY = "subLocality";
    
    public static final String KEY_STOP_LOCALITY = "locality";
    
    public static final String KEY_STOP_LOCALE = "locale";
    
    public static final String KEY_STOP_SUB_STREET = "subStreet";
    
    public static final String KEY_STOP_BUILDING_NAME = "buildingName";
    
    public static final String KEY_STOP_ADDRESS_ID = "addressId";
    
    public static final String KEY_STOP_IS_SHARED_ADDRESS = "isSharedAddress";
    
    public static final String KEY_ENTITY_TYPE = "entityType";
    
    public static final int EVENT_ID_DEFAULT_VALUE = -1;
    
    public Address createAddress(byte[] data)
    {
        try
        {
            String dataString = new String(data);
            try
            {
                dataString = URLDecoder.decode(dataString, "UTF-8");
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                Logger.log(this.getClass().getName(), e);
            }
            JSONObject jsonObjectAddress = new JSONObject(new JSONTokener(dataString));

            boolean isEvent = false;
            if (jsonObjectAddress.get(KEY_ADDR_POI) != null)
            {
                JSONObject poiJson = jsonObjectAddress.getJSONObject(KEY_ADDR_POI);
                long eventId = poiJson.optLong(KEY_ADDR_EVENT_ID, EVENT_ID_DEFAULT_VALUE);
                if (eventId > 0)
                {
                    isEvent = true;
                }
            }
            
            Address result = new Address();

            long addressId = jsonObjectAddress.optLong(KEY_ADDR_ID);
            
            if (addressId <= MOCK_POI_ID_TOP)
            {
                addressId = 0;
            }
            result.setId(addressId);
            String label = jsonObjectAddress.getString(KEY_ADDR_LABEL);
            if (label != null)
            {
                result.setLabel(label);
            }

            if (jsonObjectAddress.get(KEY_ADDR_TYPE) != null)
            {
                result.setType(jsonObjectAddress.optInt(KEY_ADDR_TYPE));
            }

            String phoneNumber = jsonObjectAddress.getString(KEY_ADDR_PHONE_NUM);
            if (phoneNumber != null)
            {
                result.setPhoneNumber(phoneNumber);
            }

            if (jsonObjectAddress.get(KEY_ADDR_STATUS) != null)
            {
                result.setStatus(jsonObjectAddress.optInt(KEY_ADDR_STATUS));
            }

            String sharedFromPTN = jsonObjectAddress.getString(KEY_ADDR_SHARED_FROM_PTN);
            if (sharedFromPTN != null)
            {
                result.setSharedFromPTN(sharedFromPTN);
            }

            String sharedFromUser = jsonObjectAddress.getString(KEY_ADDR_SHARED_FROM_USER);
            if (sharedFromUser != null)
            {
                result.setSharedFromUser(sharedFromUser);
            }
            
            int selectedIndex = jsonObjectAddress.optInt(KEY_ADDR_SELECTED_INDEX);
            result.setSelectedIndex(selectedIndex);
            
            if (jsonObjectAddress.get(KEY_ADDR_CATEGORY) != null)
            {
                Vector category = convertJSONToVector(jsonObjectAddress.getJSONArray(KEY_ADDR_CATEGORY));
                if (category != null)
                {
                    result.setCatagories(category);
                }
            }

            if (jsonObjectAddress.get(KEY_ADDR_STOP) != null)
            {
                Stop stop = createStop(jsonObjectAddress.getJSONObject(KEY_ADDR_STOP).toString().getBytes());
                if (stop != null)
                {
                    if(isEvent)
                    {
                        checkStopFirstLine(stop);
                    }
                    result.setStop(stop);
                }
            }

            if (jsonObjectAddress.get(KEY_ADDR_POI) != null)
            {
                JSONObject poiJson = jsonObjectAddress.getJSONObject(KEY_ADDR_POI);
                
                long eventId = poiJson.optLong(KEY_ADDR_EVENT_ID, EVENT_ID_DEFAULT_VALUE);
                
                result.setEventId(eventId);
                
                long eventStartTime = poiJson.optLong(KEY_ADDR_EVENT_START_TIME);
                
                result.setEventStartTime(eventStartTime);
                
                long eventEndTime = poiJson.optLong(KEY_ADDR_EVENT_END_TIME);
                
                result.setEventEndTime(eventEndTime);
                
                boolean useStartTime = poiJson.optBoolean(KEY_ADDR_USE_EVENT_START_TIME);
                
                result.setUseEventStartTime(useStartTime);
                
                boolean useEndTime = poiJson.optBoolean(KEY_ADDR_USE_EVENT_END_TIME);
                
                result.setUseEventEndTime(useEndTime);
                
                String eventVenue = poiJson.getString(KEY_ADDR_EVENT_VENUE);
                
                result.setEventVenue(eventVenue);
                
                boolean isEventDetailAvailable = poiJson.optBoolean(KEY_ADDR_EVENT_DETAIL_AVAILABLE);
                
                result.setIsEventDataAvailable(isEventDetailAvailable);
                
                Poi poi = JsonPoiSerializable.createPoi(poiJson);
                
                if (poi != null)
                {
                    if (isEvent)
                    {
                        Stop stop = result.getStop();

                        if (stop == null && poi.getStop() != null)
                        {
                            stop = poi.getStop();
                        }

                        if (stop == null && poi.getBizPoi() != null && poi.getBizPoi().getStop() != null)
                        {
                            stop = poi.getBizPoi().getStop();
                        }

                        if (stop != null)
                        {
                            checkStopFirstLine(stop);
                            result.setStop(stop);
                        }
                    }
                }
                
                boolean isFakePoi = false;
                if (poi.getBizPoi() != null && poi.getBizPoi().getPoiId() != null)
                {
                    String str_id = poi.getBizPoi().getPoiId();
                    if (str_id != null && str_id.length() < 4)
                    {
                        try
                        {
                            int id = Integer.valueOf(str_id).intValue();
                            if (id <= MOCK_POI_ID_TOP)
                            {
                                isFakePoi = true;
                            }
                        }
                        catch (Exception e)
                        {
                            
                        }
                    }
                }
                
                if (poi != null && !isFakePoi && !isEvent)
                {
                    result.setPoi(poi);
                } 
            }
            
            return result;

        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return null;
    }

    protected void checkStopFirstLine(Stop stop)
    {
        if (stop.getFirstLine() != null && stop.getFirstLine().trim().length() > 0)
        {
            stop.setStreetName(stop.getFirstLine());
            stop.setHouseNumber("");
        }
    }
    
    public FavoriteCatalog createFavoriteCatalog(byte[] data)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Stop createStop(byte[] data)
    {
        try
        {
            JSONObject jsonObjectStop = new JSONObject(new String(data));

            return createStop(jsonObjectStop);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return null;
    }

    static Stop createStop(JSONObject jsonObjectStop)
    {
        try
        {
            Stop result = new Stop();

            if (jsonObjectStop.get(KEY_STOP_TYPE) != null)
            {
                result.setType((byte) jsonObjectStop.optInt(KEY_STOP_TYPE));
            }
            if (jsonObjectStop.get(KEY_STOP_ID) != null)
            {
                result.setId(jsonObjectStop.optLong(KEY_STOP_ID));
            }

            if (jsonObjectStop.get(KEY_STOP_LAT) != null)
            {
                String latString = jsonObjectStop.getString(KEY_STOP_LAT);
                
                if (latString.indexOf(".") != -1)
                {
                    double latD = jsonObjectStop.optDouble(KEY_STOP_LAT, 0);
                    int lat = (int)(latD * 100000);
                    result.setLat(lat);
                }
                else
                {
                    result.setLat(jsonObjectStop.optInt(KEY_STOP_LAT));
                }
            }
            if (jsonObjectStop.get(KEY_STOP_LON) != null)
            {
                String lonString = jsonObjectStop.getString(KEY_STOP_LON);
                
                if (lonString.indexOf(".") != -1)
                {
                    double lonD = jsonObjectStop.optDouble(KEY_STOP_LON, 0);
                    int lon = (int)(lonD * 100000);
                    result.setLon(lon);
                }
                else
                {
                    result.setLon(jsonObjectStop.optInt(KEY_STOP_LON));
                }
            }

            String label = jsonObjectStop.getString(KEY_STOP_LABEL);
            if (label != null)
            {
                result.setLabel(label);
            }

            String cityString = jsonObjectStop.getString(KEY_STOP_CITY);
            if (cityString != null)
            {
                result.setCity(cityString);
            }

            String firstLineString = jsonObjectStop.getString(KEY_STOP_FIRSTLINE);
            if (firstLineString != null)
            {
                result.setFirstLine(firstLineString);
            }
            String lastLineAudio = jsonObjectStop.getString(KEY_STOP_LASTLINE);
            if (lastLineAudio != null)
            {
                result.setLastLineAudio(lastLineAudio.getBytes());
            }
            String countryString = jsonObjectStop.getString(KEY_STOP_COUNTRY);
            if (countryString != null)
            {
                result.setCountry(countryString);
            }
            String CrossStreetName = jsonObjectStop.getString(KEY_STOP_CROSS_STREET);
            if (CrossStreetName != null)
            {
                result.setCrossStreetName(CrossStreetName);
            }
            String PostalCode = jsonObjectStop.getString(KEY_STOP_ZIP);
            if (PostalCode != null)
            {
                result.setPostalCode(PostalCode);
            }
            String Province = jsonObjectStop.getString(KEY_STOP_PROVINCE);
            if (Province != null)
            {
                result.setProvince(Province);
            }
            String StreetName = jsonObjectStop.getString(KEY_STOP_STREETNAME);
            if (StreetName != null)
            {
                result.setStreetName(StreetName);
            }

            if (jsonObjectStop.get(KEY_STOP_GEOCODED) != null)
            {
                result.setIsGeocoded(jsonObjectStop.optBoolean(KEY_STOP_GEOCODED));
            }
            
            String houseNumber = jsonObjectStop.getString(KEY_STOP_HOUSE_NUMBER);
            if (houseNumber != null)
            {
                result.setHouseNumber(houseNumber);
            }

            String suite = jsonObjectStop.getString(KEY_STOP_SUITE);
            if (suite != null)
            {
                result.setSuite(suite);
            }

            String county = jsonObjectStop.getString(KEY_STOP_COUNTY);
            if (county != null)
            {
                result.setCounty(county);
            }

            String subLocality = jsonObjectStop.getString(KEY_STOP_SUB_LOCALITY);
            if (subLocality != null)
            {
                result.setSubLocality(subLocality);
            }

            String locality = jsonObjectStop.getString(KEY_STOP_LOCALITY);
            if (locality != null)
            {
                result.setLocality(locality);
            }

            String locale = jsonObjectStop.getString(KEY_STOP_LOCALE);
            if (locale != null)
            {
                result.setLocale(locale);
            }

            String subStreet = jsonObjectStop.getString(KEY_STOP_SUB_STREET);
            if (subStreet != null)
            {
                result.setSubStreet(subStreet);
            }

            String buildingName = jsonObjectStop.getString(KEY_STOP_BUILDING_NAME);
            if (buildingName != null)
            {
                result.setBuildingName(buildingName);
            }

            String addressId = jsonObjectStop.getString(KEY_STOP_ADDRESS_ID);
            if (addressId != null)
            {
                result.setAddressId(addressId);
            }

            String isSharedAddressStr = jsonObjectStop
                    .getString(KEY_STOP_IS_SHARED_ADDRESS);
            if (isSharedAddressStr != null)
            {
                boolean isSharedAddress = jsonObjectStop
                        .optBoolean(KEY_STOP_IS_SHARED_ADDRESS);
                result.setSharedAddress(isSharedAddress);
            }

            return result;
        }
        catch (JSONException e)
        {
            Logger.log(JsonAddressSerializable.class.getName(), e);
        }

        return null;
    }

    public byte[] toBytes(Address address)
    {
        JSONObject result = new JSONObject();

        try
        {
            result.put(KEY_ADDR_TYPE, address.getType());
            result.put(KEY_ADDR_ID, address.getId());
            result.put(KEY_ENTITY_TYPE, address.getEntityType());
            result.put(KEY_ADDR_LABEL, address.getLabel());
            result.put(KEY_ADDR_PHONE_NUM, address.getPhoneNumber());
            result.put(KEY_ADDR_STATUS, address.getStatus());
            result.put(KEY_ADDR_SHARED_FROM_PTN, address.getSharedFromPTN());
            result.put(KEY_ADDR_SHARED_FROM_USER, address.getSharedFromUser());
            result.put(KEY_ADDR_EXISTED_IN_FAVORITE, address.isExistedInFavorite());
            result.put(KEY_ADDR_SELECTED_INDEX, address.getSelectedIndex());
            JSONArray category = convertArrayToJSON(address.getCatagories());
            result.put(KEY_ADDR_CATEGORY, category);

            if(address.getEventId() > 0)
            {
                Poi poi = new Poi();
                BizPoi bizpoi = new BizPoi();
                
                poi.setBizPoi(bizpoi);
                
                bizpoi.setStop(address.getStop());
                
                JSONObject poiJSON = new JSONObject();
                JsonPoiSerializable.toBytes(poi, poiJSON);
                result.put(KEY_ADDR_POI, poiJSON);
                
                poiJSON.put(KEY_ADDR_EVENT_ID, address.getEventId());
                poiJSON.put(KEY_ADDR_EVENT_START_TIME, address.getEventStartTime());
                poiJSON.put(KEY_ADDR_EVENT_END_TIME, address.getEventEndTime());
                poiJSON.put(KEY_ADDR_USE_EVENT_START_TIME, address.isUseEventStartTime());
                poiJSON.put(KEY_ADDR_USE_EVENT_END_TIME, address.isUseEventEndTime());
                poiJSON.put(KEY_ADDR_EVENT_VENUE, address.getEventVenue());
                poiJSON.put(KEY_ADDR_EVENT_DETAIL_AVAILABLE, address.isEventDataAvailable());
            }
            else
            {
                if(address.getStop() != null)
                {
                    JSONObject stopJSON = new JSONObject();
                    toBytes(address.getStop(), stopJSON);
                    result.put(KEY_ADDR_STOP, stopJSON);
                }
                
                if(address.getPoi() != null)
                {
                    JSONObject poiJSON = new JSONObject();
                    JsonPoiSerializable.toBytes(address.getPoi(), poiJSON);
                    result.put(KEY_ADDR_POI, poiJSON);
                    boolean isSponsor = address.getPoi().getType() == Poi.TYPE_SPONSOR_POI? true: false;
                   
                    poiJSON.put(KEY_IS_SPONSOR_POI, isSponsor);
                    poiJSON.put(KEY_IS_ADS_POI, address.getPoi().isAdsPoi());
                    poiJSON.put(KEY_IS_HAS_POI_MENU, address.getPoi().hasPoiMenu());
                    poiJSON.put(KEY_IS_HAS_POI_EXTR_ATTR, address.getPoi().hasPoiExtraAttributes());
                    poiJSON.put(KEY_IS_HAS_REVIEW, address.getPoi().hasReviews());
                    poiJSON.put(KEY_IS_HAS_ADS_MENU, address.getPoi().hasAdsMenu());
                    poiJSON.put(KEY_IS_HAS_DEAL, address.getPoi().isHasCoupon());
                    poiJSON.put(KEY_IS_HAS_POI_DETAIL, address.getPoi().hasPoiDetail());
                    
                    BizPoi bizPoi = address.getPoi().getBizPoi();
                    if(bizPoi != null)
                    {
                        if(bizPoi.getPoiLogo() != null && bizPoi.getPoiLogo().trim().length() > 0)
                        {
                            poiJSON.put(KEY_S_POI_LOGO, bizPoi.getPoiLogo());
                        }
                        if(bizPoi.getBrandLogo() != null && bizPoi.getBrandLogo().trim().length() > 0)
                        {
                            poiJSON.put(KEY_S_BRAND_LOGO, bizPoi.getBrandLogo());
                        }
                        if(bizPoi.getCategoryLogo() != null && bizPoi.getCategoryLogo().trim().length() > 0)
                        {
                            poiJSON.put(KEY_S_CATEGORY_LOGO, bizPoi.getCategoryLogo());
                        }
                    }
                    String mapProvider = DaoManager.getInstance().getStartupDao().getMapDataset();
                    result.put(KEY_S_MAP_PROVIDER, mapProvider);
                }
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return result.toString().getBytes();
    }

    public byte[] toBytes(FavoriteCatalog catalog)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public byte[] toBytes(Stop stop)
    {
        JSONObject result = new JSONObject();
        toBytes(stop, result);
        
        return result.toString().getBytes();
    }
    
    static void toBytes(Stop stop, JSONObject result)
    {
        try
        {
            result.put(KEY_STOP_TYPE, stop.getType());
            result.put(KEY_STOP_ID, stop.getId());
            result.put(KEY_STOP_LAT, stop.getLat());
            result.put(KEY_STOP_LON, stop.getLon());
            result.put(KEY_STOP_LABEL, stop.getLabel());
            result.put(KEY_STOP_CITY, stop.getCity());
            result.put(KEY_STOP_FIRSTLINE, stop.getFirstLine());
            result.put(KEY_STOP_LASTLINE, stop.getLastLineAudio());
            result.put(KEY_STOP_COUNTRY, stop.getCountry());
            result.put(KEY_STOP_CROSS_STREET, stop.getCrossStreetName());
            result.put(KEY_STOP_ZIP, stop.getPostalCode());
            result.put(KEY_STOP_PROVINCE, stop.getProvince());
            result.put(KEY_STOP_STREETNAME, stop.getStreetName());
            result.put(KEY_STOP_GEOCODED, stop.isGeocoded());
            
            result.put(KEY_STOP_HOUSE_NUMBER, stop.getHouseNumber());
            result.put(KEY_STOP_SUITE, stop.getSuite());
            result.put(KEY_STOP_COUNTY, stop.getCounty());
            result.put(KEY_STOP_SUB_LOCALITY, stop.getSubLocality());
            result.put(KEY_STOP_LOCALITY, stop.getLocality());
            result.put(KEY_STOP_LOCALE, stop.getLocale());
            result.put(KEY_STOP_SUB_STREET, stop.getSubStreet());
            result.put(KEY_STOP_BUILDING_NAME, stop.getBuildingName());
            result.put(KEY_STOP_ADDRESS_ID, stop.getAddressId());
            result.put(KEY_STOP_IS_SHARED_ADDRESS, stop.isSharedAddress());
        }
        catch (Exception e)
        {
            Logger.log(JsonAddressSerializable.class.getName(), e);
        }
    }

    private JSONArray convertArrayToJSON(Vector array)
    {
        if (array == null)
        {
            return null;
        }

        JSONArray result = new JSONArray();

        try
        {
            for (int index = 0; index < array.size(); index++)
            {
                result.put(array.elementAt(index));
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return result;
    }

    private Vector convertJSONToVector(JSONArray jsonArrayCategory)
    {
        if (jsonArrayCategory == null)
        {
            return null;
        }
        Vector result = new Vector();
        try
        {
            for (int i = 0; i < jsonArrayCategory.length(); i++)
            {
                result.addElement(jsonArrayCategory.get(i));
            }
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            Logger.log(this.getClass().getName(), e);
        }
        return result;
    }

    public SentAddress createSentAddress(byte[] data)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public byte[] toBytes(SentAddress sentAddress)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
