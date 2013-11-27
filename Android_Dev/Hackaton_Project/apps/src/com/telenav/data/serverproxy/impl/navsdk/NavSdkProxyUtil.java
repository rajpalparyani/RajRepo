/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkProxyUtil.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.List;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Ad;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.log.mis.log.DefaultMisLog;
import com.telenav.logger.Logger;
import com.telenav.navsdk.events.MISLogData.MISLogItem;
import com.telenav.navsdk.events.MISLogData.MISLogPriority;
import com.telenav.navsdk.events.MISLogEvents.SendMISLog;
import com.telenav.navsdk.events.PointOfInterestData.StringKeyValuePair;
import com.telenav.res.ResourceManager;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2011-12-7
 */
public class NavSdkProxyUtil
{
    private final static int COMPLETE_LIST_ID = -1;

    public static OneBoxSearchBean convertSearchBean(
            com.telenav.navsdk.events.PointOfInterestEvents.SearchSuggestion protoSuggestion)
    {
        OneBoxSearchBean suggestion = new OneBoxSearchBean();
        suggestion.setKey(protoSuggestion.getDisplayLabel());
        suggestion.setContent(protoSuggestion.getSearchRequest().getQueryString());
        return suggestion;
    }

    public static Address convertSponsoredResult(com.telenav.navsdk.events.PointOfInterestData.SponsoredResult sponsoredResult)
    {
        if (sponsoredResult == null)
        {
            return null;
        }
        Address address = convertAddress(sponsoredResult.getPoi(), true);

        if (address != null && address.getPoi() != null)
        {
            if (sponsoredResult.hasHasCoupon())
            {
                address.getPoi().setHasCoupon(sponsoredResult.getHasCoupon());
            }
            if (sponsoredResult.hasHasMenu())
            {
                address.getPoi().setIsHasPoiMenu(sponsoredResult.getHasMenu());
            }
        }
        
        Ad ad = new Ad();
        ad.setAdID(String.valueOf(sponsoredResult.getAdId()));
        ad.setAdSource(sponsoredResult.getAdSource());
        ad.setAdLine(sponsoredResult.getShortMessage());
        if (address.getPoi() != null)
        {
            address.getPoi().setAd(ad);
        }
        return address;
    }

    public static Address convertSearchResult(com.telenav.navsdk.events.PointOfInterestData.PoiSearchResult poiSearchResult)
    {
        if (!poiSearchResult.hasPoi())
        {
            return null;
        }

        Address address = convertAddress(poiSearchResult.getPoi(), false);
        if (address != null && address.getPoi() != null)
        {
            if (poiSearchResult.hasHasCoupon())
            {
                address.getPoi().setHasCoupon(poiSearchResult.getHasCoupon());
            }
            if (poiSearchResult.hasHasMenu())
            {
                address.getPoi().setIsHasPoiMenu(poiSearchResult.getHasMenu());
            }
        }
        return address;
    }

    public static Address convertAddress(com.telenav.navsdk.events.PointOfInterestData.PointOfInterest protoPoi,
            boolean isSponsored)
    {
        if (protoPoi == null)
            return null;

        Address address = new Address();
        if (protoPoi != null)
        {
            if (protoPoi.hasCreateTime())
            {
                address.setCreateTime(protoPoi.getCreateTime());
            }
            if (protoPoi.hasUpdateTime())
            {
                address.setUpdateTime(protoPoi.getUpdateTime());
            }

            Poi poi = convertPoi(protoPoi, isSponsored);
            address.setPoi(poi);

            if (protoPoi.hasName())
            {
                address.setLabel(protoPoi.getName());
            }
            if (address.getLabel() == null || address.getLabel().trim().length() <= 0)
            {
                if (address.getStop() != null && address.getStop().getLabel() != null)
                {
                    address.setLabel(address.getStop().getLabel());
                }
            }
            if (address.getLabel() == null || address.getLabel().trim().length() <= 0)
            {
                if (address.getPoi() != null && address.getPoi().getBizPoi() != null)
                {
                    address.setLabel(address.getPoi().getBizPoi().getBrand());
                }
            }
            if (poi.getBizPoi().getBrand() == null || poi.getBizPoi().getBrand().trim().length() == 0)
            {
                poi.getBizPoi().setBrand(address.getLabel());
            }
        }
        if (protoPoi.hasAddress())
        {
            Stop stop = convertStop(protoPoi);
            if (stop != null)
            {
                if(protoPoi.getAddress().hasMatchingScore())
                {
                    stop.setScore((int)(protoPoi.getAddress().getMatchingScore() * 100));
                }
                address.setStop(stop);
                if (address.getPoi() != null)
                {
                    address.getPoi().setStop(stop);
                }
            }
        }
        return address;
    }

    public static Poi convertPoi(com.telenav.navsdk.events.PointOfInterestData.PointOfInterest protoPoi, boolean isSponsored)
    {
        if (protoPoi == null)
        {
            return null;
        }

        Poi poi = new Poi();
        if (isSponsored)
        {
            poi.setType(Poi.TYPE_SPONSOR_POI);
        }
        else
        {
            poi.setType(Poi.TYPE_POI);
        }
        if (protoPoi.hasRating())
        {
            poi.setRating((int) (protoPoi.getRating())); // Rating of this POI, generally 0.0 to 5.0
        }
        if (protoPoi.hasPopularity())
        {
            poi.setPopularity((int) (protoPoi.getPopularity() * 10)); // Need to change int float? //FIXME
        }

        if (protoPoi.hasAdID() && !"".equals(protoPoi.getAdID().trim()))
        {
            Ad ad = new Ad();
            ad.setAdID(String.valueOf(protoPoi.getAdID()));
            List<StringKeyValuePair> list = protoPoi.getExtraAttributesList();
            if (list != null && list.size() > 0)
            {
                for (StringKeyValuePair keyValue : list)
                {
                    if (keyValue.getKey().equalsIgnoreCase("shortMessage"))
                    {
                        ad.setAdLine(keyValue.getValue());
                        break;
                    }
                }
            }
            poi.setAd(ad);
            poi.setIsHasPoiSourceAdId(protoPoi.hasAdID());
            poi.setSourceAdId(protoPoi.getAdID());
        }

        BizPoi bizPoi = convertBizPoi(protoPoi);
        if(protoPoi.getCategoryIdsCount() > 0)
        {
            bizPoi.setCategoryId(protoPoi.getCategoryIds(0) + "");
        }

        poi.setBizPoi(bizPoi);

        return poi;
    }

    private static BizPoi convertBizPoi(com.telenav.navsdk.events.PointOfInterestData.PointOfInterest protoBizPoi)
    {
        BizPoi bizPoi = new BizPoi();
        bizPoi.setPoiId(protoBizPoi.getPoiId() + "");
        if (protoBizPoi.hasPhone())
        {
            bizPoi.setPhoneNumber(protoBizPoi.getPhone());
        }
        if (protoBizPoi.hasPriceRange())
        {
            bizPoi.setPrice(protoBizPoi.getPriceRange());
        }
        return bizPoi;
    }
    
    public static String POI_NO_CATEGORY_ICON = "list_icon_see_all_unfocused";
    public static boolean fillCategory(BizPoi bizPoi)
    {
        if (bizPoi == null)
        {
            return false;
        }
        int categoryId = -1;
        try
        {
            categoryId = Integer.parseInt(bizPoi.getCategoryId());
        }
        catch (Exception e)
        {
        }
        PoiCategory category = findCategoryById(categoryId);
        if (categoryId <= 0 || category == null)
        {
            return false;
        }
        bizPoi.setCategoryName(category.getName());
        String listIcon = category.getUnfocusedImagePath();
        PoiCategory tmpCategory = category;
        while ((listIcon == null || listIcon.length() == 0) && tmpCategory.getParent() != null)
        {
            tmpCategory = tmpCategory.getParent();
            listIcon = tmpCategory.getUnfocusedImagePath();
        }
        if (listIcon == null || listIcon.length() == 0)
        {
            listIcon = POI_NO_CATEGORY_ICON;
        }
        else
        {
            listIcon = listIcon.replace("icon_", "");
            listIcon = listIcon.replace("nearby", "list_icon");
        }
        bizPoi.setCategoryLogo(listIcon);
        return true;
    }

    public static PoiCategory findCategoryById(int id)
    {
        String region = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region;
        //Search hot brand first
        PoiCategory category = getCategory(DaoManager.getInstance().getResourceBarDao().getHotPoiNode(region), id);
        //Then search biz category
        if (category == null)
        {
            category = getCategory(DaoManager.getInstance().getResourceBarDao().getCategoryNode(region), id);
        }
        return category;
    }
    
    private static PoiCategory getCategory(PoiCategory searchCategory, int id)
    {
        if (searchCategory != null && searchCategory.getChildrenSize() > 0)
        {
            for (Object o : searchCategory.getChilds())
            {
                PoiCategory childNode;
                if (o instanceof PoiCategory)
                {
                    childNode = (PoiCategory)o;
                    if (childNode.getCategoryId() == id)
                    {
                        return childNode;
                    }
                    else
                    {
                        PoiCategory cate = getCategory(childNode, id);
                        if (cate != null)
                        {
                            return cate;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Stop convertStop(com.telenav.navsdk.events.PointOfInterestData.PointOfInterest protoPoi)
    {
        com.telenav.navsdk.events.PointOfInterestData.Address protoStop = protoPoi.getAddress();

        Stop stop = new Stop();
        if (protoPoi.hasLocation() && protoPoi.getLocation().hasLatitude() && protoPoi.getLocation().hasLongitude())
        {
            stop.setLat((int) (protoPoi.getLocation().getLatitude() * 100000));
            stop.setLon((int) (protoPoi.getLocation().getLongitude() * 100000));// TODO Right?
        }
        else if (protoStop == null)
        {
            return null;
        }

        if (protoStop.hasName())
        {
            stop.setLabel(protoStop.getName());
        }
        else
        {
            if (protoPoi.hasAirportCode())
            {
                stop.setLabel(protoPoi.getAirportCode());
            }
            if (protoPoi.hasName())
            {
                stop.setLabel(protoPoi.getName());
            }
        }
        if (protoStop.hasDoorNumber())
        {
            stop.setHouseNumber(protoStop.getDoorNumber());
        }
        if (protoStop.hasStreetName())
        {
            stop.setStreetName(protoStop.getStreetName());
        }
        if (protoStop.hasCrossStreetName())
        {
            stop.setCrossStreetName(protoStop.getCrossStreetName());
            String result = stop.getCombinedCrossStreetName();
            if (result != null)
            {
                stop.setStreetName(result);
                stop.setCrossStreetName("");
            }
        }
        if (protoStop.hasCity())
        {
            stop.setCity(protoStop.getCity());
        }
        if (protoStop.hasCounty())
        {
            stop.setCounty(protoStop.getCounty());
        }
        if (protoStop.hasProvince())
        {
            stop.setProvince(protoStop.getProvince());
        }
        else if (protoStop.hasStateCode())
        {
            stop.setProvince(protoStop.getStateCode());
        }
        if (protoStop.hasCountry())
        {
            stop.setCountry(protoStop.getCountry());
        }
        if (protoStop.hasPostalCode())
        {
            stop.setPostalCode(protoStop.getPostalCode());
        }

        if (!protoPoi.hasLocation() || protoPoi.getLocation().hasLatitude() && protoPoi.getLocation().hasLongitude())
        {
            if (protoStop.hasFormattedAddress())
            {
                stop.setFirstLine(protoStop.getFormattedAddress());
            }
            else
            {
                stop.setFirstLine(ResourceManager.getInstance().getStringConverter().convertStopFirstLine(stop));
            }
        }
        return stop;
    }

    public static com.telenav.navsdk.events.LocationData.Position convertProtRecentLocation(TnLocation location)
    {
        com.telenav.navsdk.events.LocationData.Position.Builder protoAddress = com.telenav.navsdk.events.LocationData.Position
                .newBuilder();
        protoAddress.setAltitude(location.getAltitude());
        protoAddress.setHeading(location.getHeading());
        protoAddress.setTimestamp(location.getTime());
        protoAddress.setLatitude(location.getLatitude() / 100000.0d);
        protoAddress.setLongitude(location.getLongitude() / 100000.0d);
        int speed = location.getSpeed();
        float speedInMs = speed * 0.111316f;
        protoAddress.setSpeedInMS(speedInMs);
        return protoAddress.build();
    }

    public static com.telenav.navsdk.events.PointOfInterestData.PointOfInterest convertProtoPointOfInterest(Address address)
    {
        com.telenav.navsdk.events.PointOfInterestData.PointOfInterest.Builder protoAddress = com.telenav.navsdk.events.PointOfInterestData.PointOfInterest
                .newBuilder();
        if (address.getId() != 0)
        {
            protoAddress.setPoiId(address.getId());
        }
        if (address.getCreateTime() != 0)
        {
            protoAddress.setCreateTime(address.getCreateTime());
        }
        if (address.getUpdateTime() != 0)
        {
            protoAddress.setUpdateTime(address.getUpdateTime());
        }
        if (address.getLabel() != null && address.getLabel().trim().length() > 0)
        {
            protoAddress.setName(address.getLabel());
        }
        Poi poi = address.getPoi();
        if (poi != null)
        {
            if (poi.getRating() != 0)
            {
                protoAddress.setRating(poi.getRating() / 10.0f);
            }
            if (poi.getPopularity() != 0)
            {
                protoAddress.setPopularity(poi.getPopularity() / 10.0f);// FIXME
            }
            if (address.getPhoneNumber() != null)
            {
                protoAddress.setPhone(address.getPhoneNumber());
            }
            if (poi.getBizPoi() != null && poi.getBizPoi().getPrice() != null)
            {
                protoAddress.setPriceRange(poi.getBizPoi().getPrice());
            }
        }
        Stop stop = address.getStop();
        if (stop != null)
        {
            com.telenav.navsdk.events.PointOfInterestData.Address protoStop = convertProtoAddress(stop);
            protoAddress.setAddress(protoStop);
            if (stop.getLat() != 0 || stop.getLon() != 0)
            {
                com.telenav.navsdk.events.PointOfInterestData.Location.Builder protoLocation = com.telenav.navsdk.events.PointOfInterestData.Location
                        .newBuilder();
                protoLocation.setLatitude(stop.getLat() / 100000.0d);
                protoLocation.setLongitude(stop.getLon() / 100000.0d);
                protoAddress.setLocation(protoLocation.build());
            }
        }

        return protoAddress.build();
    }

    public static com.telenav.navsdk.events.PointOfInterestData.Address convertProtoAddress(Stop stop)
    {
        if (stop == null)
            return null;

        com.telenav.navsdk.events.PointOfInterestData.Address.Builder builder = com.telenav.navsdk.events.PointOfInterestData.Address
                .newBuilder();
        if (stop.getFirstLine() != null)
        {
            builder.setFormattedAddress(stop.getFirstLine());
        }
        if (stop.getLabel() != null)
        {
            builder.setName(stop.getLabel());
        }
        if (stop.getHouseNumber() != null)
        {
            builder.setDoorNumber(stop.getHouseNumber());
        }
        if (stop.getStreetName() != null)
        {
            builder.setStreetName(stop.getStreetName());
        }
        if (stop.getCrossStreetName() != null)
        {
            builder.setCrossStreetName(stop.getCrossStreetName());
        }
        if (stop.getCity() != null)
        {
            builder.setCity(stop.getCity());
        }
        if (stop.getCounty() != null)
        {
            builder.setCounty(stop.getCounty());
        }
        if (stop.getProvince() != null)
        {
            // builder.setProvince(stop.getProvince());//TODO need?
            builder.setStateCode(stop.getProvince());
        }
        if (stop.getCountry() != null)
        {
            // TODO: Hardcode to USA to make onboard mode validate address work.
            builder.setCountry("USA");
        }
        if (stop.getPostalCode() != null)
        {
            builder.setPostalCode(stop.getPostalCode());
        }
        return builder.build();
    }

    public static AbstractMisLog convertMisLog(SendMISLog sendMISLog)
    {
        if (sendMISLog == null)
        {
            return null;
        }
        AbstractMisLog misLog = MisLogManager.getInstance().getFactory().createMisLog(sendMISLog.getType());
        if (misLog instanceof DefaultMisLog)
        {
            ((DefaultMisLog) misLog).setPriority(convertMisLogPriority(sendMISLog.getPriority()));
        }
        else
        {
            if (misLog.getPriority() != convertMisLogPriority(sendMISLog.getPriority()))
            {
                Logger.log(Logger.EXCEPTION, "NavSdkProxyUtil", "Inconsistent MisLog priority received from NavSdk");
            }
        }
        for (int i = 0; i < sendMISLog.getLogitemsCount(); i++)
        {
            MISLogItem logItem = sendMISLog.getLogitems(i);
            misLog.setAttribute(logItem.getAttr(), logItem.getValue());
        }
        return misLog;
    }

    public static int convertMisLogPriority(MISLogPriority mISLogPriority)
    {
        switch (mISLogPriority.getNumber())
        {
            case 1:
            {
                return IMisLogConstants.PRIORITY_1;
            }
            case 2:
            {
                return IMisLogConstants.PRIORITY_2;
            }
            case 3:
            {
                return IMisLogConstants.PRIORITY_3;
            }
            case 4:
            {
                return IMisLogConstants.PRIORITY_4;
            }
            default:
                break;
        }
        return 0;
    }
}
