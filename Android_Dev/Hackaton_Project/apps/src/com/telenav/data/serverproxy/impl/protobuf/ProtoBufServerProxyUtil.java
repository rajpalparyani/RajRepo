/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficProxyUtil.java
 *
 */
package com.telenav.data.serverproxy.impl.protobuf;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Ad;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Coupon;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.poi.SupplimentInfo;
import com.telenav.j2me.datatypes.ProtoObjectType;
import com.telenav.j2me.framework.protocol.ProtoAddressItem;
import com.telenav.j2me.framework.protocol.ProtoAdvertisement;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBasePoiExtraInfo;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoCoupon;
import com.telenav.j2me.framework.protocol.ProtoFavoriteCategory;
import com.telenav.j2me.framework.protocol.ProtoGpsFix;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.protocol.ProtoStopPoiWrapper;
import com.telenav.j2me.framework.protocol.ProtoSuggestionItem;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-20
 */
class ProtoBufServerProxyUtil
{
    public static final byte TYPE_POSITION  = 0;
    public static final byte TYPE_CELLSITE  = 1;
    public static final byte TYPE_FAKE      = 2;
    public static final String PLUS_SIGN = "+";
    
//    public static TrafficSegment convertTrafficSegment(ProtoTrafficSegment protoSeg)
//    {
//        if (protoSeg == null)
//            return null;
//        
//        TrafficSegment trafficSeg = null;
//
//        trafficSeg = TrafficDataFactory.getInstance().createTrafficSegment();
//        trafficSeg.setLength(protoSeg.getLength());
//        trafficSeg.setPostedSpeed(protoSeg.getPostedSpeed());
//        trafficSeg.setDelay(protoSeg.getDelay());
//        trafficSeg.setTravelTime(protoSeg.getTravelTime());
//        trafficSeg.setAvgSpeed(protoSeg.getAvgSpeed());
//        trafficSeg.setSlowestSpeed(protoSeg.getSlowestSpeed());
//        trafficSeg.setJamTrend(protoSeg.getJamTrend());
//        trafficSeg.setTime(protoSeg.getTime());
//
//        trafficSeg.setName(protoSeg.getName());
//        Vector v = protoSeg.getTmcIDs();
//        if (v != null && v.size() > 0)
//        {
//            String[] tmcIDs = new String[v.size()];
//            for( int i = 0; i < v.size(); i++ )
//            {
//                tmcIDs[i] = (String)v.elementAt(i);
//            }
//            trafficSeg.setTmcIDs(tmcIDs);
//        }
//        
//        boolean hasData = false;
//        TrafficIncident[] incidents = null;
//        v = protoSeg.getIncidents();
//        if (v != null && v.size() > 0)
//        {
//            incidents = new TrafficIncident[v.size()];
//            for( int i = 0; i < incidents.length; i++ )
//            {
//                incidents[i] = convertTrafficIncident((ProtoTrafficIncident)v.elementAt(i));
//                if(incidents[i] != null)
//                {
//                    hasData = true;
//                }
//            }
//        }
//        if(!hasData)
//        {
//            incidents = new TrafficIncident[0];
//        }
//        
//        trafficSeg.setIncidents(incidents);
//        
//        return trafficSeg;
//    }
    
//    public static TrafficIncident convertTrafficIncident(ProtoTrafficIncident protoIncident)
//    {
//        if (protoIncident == null)
//            return null;
//        
//        TrafficIncident incident = null;
//
//        incident = TrafficDataFactory.getInstance().createTrafficIncident();
//        incident.setIncidentType((byte)protoIncident.getType());
//        incident.setSeverity((byte)protoIncident.getSeverity());
//        incident.setLaneClosed((byte)protoIncident.getLaneClosed());
//        incident.setId(protoIncident.getId());
//        incident.setLat(protoIncident.getLat());
//        incident.setLon(protoIncident.getLon());
//        incident.setEdgeID(protoIncident.getEdgeID());
//        incident.setDelay(protoIncident.getDelay());
//        
//        incident.setMsg(protoIncident.getMsg());
//        incident.setCrossSt(protoIncident.getCrossSt());
//        if (protoIncident.getStreetAudio() != null)
//        {
//            incident.setStreetAudio(createAudioDataNodes(protoIncident.getStreetAudio()));
//        }
//        else if (protoIncident.getLocationAudio() != null)
//        {
//            incident.setLocationAudio(createAudioDataNodes(protoIncident.getLocationAudio()));
//        }
//        // incident.incidentTypeString = res.getString( IResourceConstants.RES_TRAFFIC_ACCIDENT +
//        // incident.incidentType -1);
//        return incident;
//    }
    
//    public static AudioDataNode createAudioDataNodes(Vector audios)
//    {
//        if (audios == null || audios.size() == 0)
//            return null;
//        
//        AudioDataNode audioNodes = AudioDataFactory.getInstance().createAudioDataNode(null);
//        if(audios != null && audios.size() > 0)
//        {
//            for (int i = 0; i < audios.size(); i++)
//            {
//                ProtoAudioData node = (ProtoAudioData)audios.elementAt(i);
//                AudioDataNode audioNode = createAudioDataNode(node);
//                if(audioNode != null)
//                {
//                    audioNodes.addChild(audioNode);
//                }
//            }
//        }
//        
//        return audioNodes;
//    }
    
//    private static AudioDataNode createAudioDataNode(ProtoAudioData audio)
//    {
//        if (audio == null)
//            return null;
//        
//        int audioId = audio.getId();
//        AudioData audioData = AudioDataFactory.getInstance().createAudioData(audioId);
//        if (audio.getData() != null)
//            audioData.setAudio(audio.getData().toByteArray());
//        
//        AudioDataNode audioDataNode = AudioDataFactory.getInstance().createAudioDataNode(audioData);
//        return audioDataNode;
//    }
    
    public static ProtoGpsList convertGpsList(TnLocation[] gpsFixes, int fixCount)
    {
        if (gpsFixes == null)
            return null;
        
        ProtoGpsList.Builder listBuilder = ProtoGpsList.newBuilder();
        
        for (int i=0; i<gpsFixes.length && i<fixCount; i++)
        {
            if (gpsFixes[i] != null)
                listBuilder.addElementGpsfix(convertGpsFix(gpsFixes[i]));
        }
        
        return listBuilder.build();
    }
    
    public static ProtoGpsFix convertGpsFix(TnLocation fix)
    {
        if (fix == null)
            return null;
        
        ProtoGpsFix.Builder fixBuilder = ProtoGpsFix.newBuilder();
        fixBuilder.setType(getFixType(fix));
        fixBuilder.setTimeTag(fix.getTime());
        fixBuilder.setLatitude(fix.getLatitude());
        fixBuilder.setLontitude(fix.getLongitude());
        fixBuilder.setSpeed(fix.getSpeed());
        fixBuilder.setHeading(fix.getHeading());
        fixBuilder.setErrorSize(fix.getAccuracy());
        return fixBuilder.build();
    }

    public static int getFixType(TnLocation location)
    {
        String provider = location.getProvider();
        if (provider == null || provider.trim().length() <= 0)
            return TYPE_POSITION;

        if (provider.trim().equals(TnLocationManager.GPS_179_PROVIDER))
        {
            return TYPE_POSITION;
        }
        else if (provider.trim().equals(TnLocationManager.ALONGROUTE_PROVIDER) || provider.trim().equals(TnLocationManager.MVIEWER_PROVIDER))
        {
            return TYPE_FAKE;
        }
        else if (provider.trim().equals(TnLocationManager.NETWORK_PROVIDER) || provider.trim().equals(TnLocationManager.TN_NETWORK_PROVIDER))
        {
            return TYPE_CELLSITE;
        }

        return TYPE_POSITION;
    }
    
    public static ProtoStopPoiWrapper convertProtoStopPoiWrapper(Address address)
    {
        ProtoStopPoiWrapper.Builder protoStopPoiWrapper = ProtoStopPoiWrapper.newBuilder();

        protoStopPoiWrapper.setType(address.getType());// TODO TYPE_FAVORITE?TYPE_RECENT
        protoStopPoiWrapper.setStatus(address.getStatus());
        protoStopPoiWrapper.setId(address.getId());
        protoStopPoiWrapper.setLabel(address.getLabel());
        protoStopPoiWrapper.setSharedFromUser(address.getSharedFromUser());
        protoStopPoiWrapper.setSharedFromPTN(address.getSharedFromPTN());
        protoStopPoiWrapper.setCreateTime(address.getCreateTime());
        protoStopPoiWrapper.setUpdateTime(address.getUpdateTime());

        Vector categories = address.getCatagories();
        if(categories != null)
        {
            for (int i = 0; i < categories.size(); i++)
            {
                protoStopPoiWrapper.addElementCategories((String) categories.elementAt(i));
            }
        }

        if (address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_RECENT_POI)
        {
            Poi poi = address.getPoi();
            if (poi != null)
            {
                ProtoBasePoi protoPoi = converPoi(address.getPoi());
                protoStopPoiWrapper.setPoi(protoPoi);
            }
        }
        else
        {
            Stop stop = address.getStop();
            if (stop != null)
            {
                ProtoStop protoStop = convertStop(stop);
                protoStopPoiWrapper.setStop(protoStop);
            }
        }
        
        protoStopPoiWrapper.setEventId(address.getEventId());
        return protoStopPoiWrapper.build();
    }
    
    public static Address convertAddress(ProtoStopPoiWrapper protoStopPoiWrapper)
    {
        if (protoStopPoiWrapper == null)
            return null;
        
        Address address = new Address();
        address.setType(protoStopPoiWrapper.getType());//TODO  TYPE_FAVORITE?TYPE_RECENT
        if (protoStopPoiWrapper.hasStatus())
        {
            address.setStatus(protoStopPoiWrapper.getStatus());
        }
        if (protoStopPoiWrapper.hasId())
        {
            address.setId(protoStopPoiWrapper.getId());
        }
        if (protoStopPoiWrapper.hasLabel())
        {
            address.setLabel(protoStopPoiWrapper.getLabel());
        }
        if (protoStopPoiWrapper.hasSharedFromUser())
        {
            address.setSharedFromUser(protoStopPoiWrapper.getSharedFromUser());
        }
        if (protoStopPoiWrapper.hasSharedFromPTN())
        {
            address.setSharedFromPTN(protoStopPoiWrapper.getSharedFromPTN());
        }
        if (protoStopPoiWrapper.hasCreateTime())
        {
            address.setCreateTime(protoStopPoiWrapper.getCreateTime());
        }
        if (protoStopPoiWrapper.hasUpdateTime())
        {
            address.setUpdateTime(protoStopPoiWrapper.getUpdateTime());
        }
        
        Vector categories = protoStopPoiWrapper.getCategories();
        if(categories!=null && categories.size()>0)
        {
            address.setCatagories(categories);
        }

        if (address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_RECENT_POI )
        {
            if(protoStopPoiWrapper.hasPoi())
            {
                ProtoBasePoi protoPoi = protoStopPoiWrapper.getPoi();
                address.setPoi(convertPoi(protoPoi)); 
            }
        }
        else
        {
            if(protoStopPoiWrapper.hasStop())
            {
                ProtoStop protoStop = protoStopPoiWrapper.getStop();
                address.setStop(convertStop(protoStop));
            }
        }
        
        if (protoStopPoiWrapper.hasEventId())
        {
            address.setEventId(protoStopPoiWrapper.getEventId());
        }
        return address;
    }
    
    public static ProtoStop convertStop(Stop stop)
    {
        if (stop == null)
            return null;
        
        ProtoStop.Builder builder = ProtoStop.newBuilder();
        builder.setFirstLine(stop.getFirstLine());
        builder.setIsGeocoded(stop.isGeocoded());
        builder.setIsShareAddress(stop.isSharedAddress());
        builder.setLabel(stop.getLabel());
        builder.setStopId(""+stop.getId());
        builder.setStopStatus(stop.getStatus());
        builder.setStopType(stop.getType());
        
        //new added fields for global address
        builder.setDoor(stop.getHouseNumber());
        builder.setSuite(stop.getSuite());
        builder.setStreetName(stop.getStreetName());
        builder.setCrossStreetName(stop.getCrossStreetName());
        builder.setCity(stop.getCity());
        builder.setCounty(stop.getCounty());
        builder.setState(stop.getProvince());
        builder.setCountry(stop.getCountry());
        builder.setZip(stop.getPostalCode());
        builder.setLat(stop.getLat());
        builder.setLon(stop.getLon());
        builder.setSublocality(stop.getSubLocality());
        builder.setLocality(stop.getLocality());
        builder.setLocale(stop.getLocale());
        builder.setSubStreet(stop.getSubStreet());
        builder.setBuildingName(stop.getBuildingName());
        builder.setAddressId(stop.getAddressId());
        return builder.build();
    }
    
    public static Stop convertStop(ProtoStop protoStop)
    {
        if (protoStop == null)
            return null;
        
        Stop stop = new Stop();
        stop.setFirstLine(protoStop.getFirstLine());
        if(protoStop.hasStopId())
        {
            try
            {
                stop.setId(Long.parseLong(protoStop.getStopId()));
            }
            catch (Exception e)
            {
            }
        }
        stop.setIsGeocoded(protoStop.getIsGeocoded());
        stop.setLabel(protoStop.getLabel());
        stop.setSharedAddress(protoStop.getIsShareAddress());
        stop.setStatus((byte)protoStop.getStopStatus());
        stop.setType((byte)protoStop.getStopType());
        stop.setCounty(protoStop.getCounty());
        //new added fields for global address
        stop.setHouseNumber(protoStop.getDoor());
        stop.setSuite(protoStop.getSuite());
        stop.setStreetName(protoStop.getStreetName());
        if(protoStop.hasCrossStreetName())
        {
            stop.setCrossStreetName(protoStop.getCrossStreetName());
            String result = stop.getCombinedCrossStreetName();
            if(result != null)
            {
                stop.setStreetName(result);
                stop.setCrossStreetName("");
            }
        }
        stop.setCity(protoStop.getCity());
        stop.setCounty(protoStop.getCounty());
        stop.setProvince(protoStop.getState());
        stop.setCountry(protoStop.getCountry());
        stop.setPostalCode(protoStop.getZip());
        stop.setLat(protoStop.getLat());
        stop.setLon(protoStop.getLon());
        stop.setSubLocality(protoStop.getSublocality());
        stop.setLocality(protoStop.getLocality());
        stop.setLocale(protoStop.getLocale());
        stop.setSubStreet(protoStop.getSubStreet());
        stop.setBuildingName(protoStop.getBuildingName());
        stop.setAddressId(protoStop.getAddressId());
        stop.setScore((int) (protoStop.getAddressScore() * 100));
        return stop;
    }
    
    public static OneBoxSearchBean covertProtoSuggestionItem(ProtoSuggestionItem item)
    {
        if (item == null)
            return null;
        
        OneBoxSearchBean result = new OneBoxSearchBean();
        result.setKey(item.getDisplayLabel());
        result.setContent(item.getQueryStr());
        return result;
    }
    
    public static Stop convertAddressItem(ProtoAddressItem protoAddressItem)
    {
        if (protoAddressItem == null)
            return null;
        
        Stop stop = new Stop();
        stop.setCity(protoAddressItem.getCity());
        stop.setCountry(protoAddressItem.getCountry());
        stop.setFirstLine(protoAddressItem.getFirstLine());
        stop.setIsGeocoded(protoAddressItem.getIsGeocoded());
        stop.setLabel(protoAddressItem.getLabel());
        stop.setStreetName(protoAddressItem.getStreet());
        stop.setLat(protoAddressItem.getLatitude());
        stop.setLon(protoAddressItem.getLotitude());
        stop.setPostalCode(protoAddressItem.getZipCode());
        stop.setProvince(protoAddressItem.getState());
        stop.setType((byte)protoAddressItem.getStopType());
        
        return stop;
    }
    
    public static ProtoBasePoi converPoi(Poi poi)
    {
        if (poi == null)
            return null;
        
        Stop stop = null;
        if(poi.getStop() != null)
        {
            stop = poi.getStop();
        }
        else if(poi.getBizPoi() != null)
        {
            stop = poi.getBizPoi().getStop();
        }
        
        if(stop == null)
        {
            return null;
        }
        
        BizPoi bizPoi = poi.getBizPoi();
        ProtoBasePoi.Builder builder = ProtoBasePoi.newBuilder();
        if(bizPoi != null && bizPoi.getPoiId() != null && !"".equals(bizPoi.getPoiId()))
        {
            builder.setPoiId(Long.parseLong(bizPoi.getPoiId()));
        }
        if(poi.getLocalAppInfos() != null)
        {
            builder.setLocalAppInfos(poi.getLocalAppInfos());
        }
        ProtoBizPoi.Builder protoBizPoiBuilder = ProtoBizPoi.newBuilder();
        
        ProtoStop.Builder protoStopBuilder = ProtoStop.newBuilder();
        protoStopBuilder.setFirstLine(stop.getFirstLine());
        protoStopBuilder.setIsGeocoded(stop.isGeocoded());
        protoStopBuilder.setIsShareAddress(stop.isSharedAddress());
        protoStopBuilder.setLabel(stop.getLabel());
        protoStopBuilder.setStopId(""+stop.getId());
        protoStopBuilder.setStopStatus(stop.getStatus());
        protoStopBuilder.setStopType(stop.getType());
        //new added fields for global address
        protoStopBuilder.setDoor(stop.getHouseNumber());
        protoStopBuilder.setSuite(stop.getSuite());
        protoStopBuilder.setStreetName(stop.getStreetName());
        protoStopBuilder.setCrossStreetName(stop.getCrossStreetName());
        protoStopBuilder.setCity(stop.getCity());
        protoStopBuilder.setCounty(stop.getCounty());
        protoStopBuilder.setState(stop.getProvince());
        protoStopBuilder.setCountry(stop.getCountry());
        protoStopBuilder.setZip(stop.getPostalCode());
        protoStopBuilder.setLat(stop.getLat());
        protoStopBuilder.setLon(stop.getLon());
        protoStopBuilder.setSublocality(stop.getSubLocality());
        protoStopBuilder.setLocality(stop.getLocality());
        protoStopBuilder.setLocale(stop.getLocale());
        protoStopBuilder.setSubStreet(stop.getSubStreet());
        protoStopBuilder.setBuildingName(stop.getBuildingName());
        protoStopBuilder.setAddressId(stop.getAddressId());
        
        protoBizPoiBuilder.setAddress(protoStopBuilder.build());
        if(bizPoi != null)
        {
            protoBizPoiBuilder.setBrand(bizPoi.getBrand());
            protoBizPoiBuilder.setCategoryId(bizPoi.getCategoryId());
            if(bizPoi.getDistance() != null)
            {
                try
                {
                    protoBizPoiBuilder.setDistance(Integer.parseInt(bizPoi.getDistance()));
                }
                catch(Exception e)
                {
                    Logger.log(ProtoBufServerProxyUtil.class.getName(), e, "biz poi distance is not integer");
                }
            }
            protoBizPoiBuilder.setPhoneNumber(bizPoi.getPhoneNumber());
            protoBizPoiBuilder.setPrice(bizPoi.getPrice());
        }
        builder.setBizPoi(protoBizPoiBuilder.build());
        return builder.build();
    }
    
//    public static Poi convertPoi(ProtoPoiDetail protoPoiDetail)
//    {
//        if(protoPoiDetail == null)
//        {
//            return null;
//        }
//        
//        Poi poi = new Poi();
//        poi.setType(Poi.TYPE_SPONSOR_POI);
//        poi.setRating(protoPoiDetail.getAvgRating());
//        poi.setPopularity(protoPoiDetail.getPopularity());
//        poi.setIsRatingEnable(protoPoiDetail.getIsRatingEnable());
//        Vector couponList = convertCouponList(protoPoiDetail.getCouponList()); 
//        poi.setCoupons(couponList);
//        poi.setMenu(protoPoiDetail.getMenu());
//        poi.setOpenTableInfo(convertOpenTable(protoPoiDetail.getOpenTableInfo()));
//        poi.setAd(convertAd(protoPoiDetail.getAd()));
//        poi.setRatingNumber(protoPoiDetail.getRatingNumber());
//        if(poi.getAd() != null && poi.getAd().getAdID() != null)
//        {
//            poi.setIsHasPoiSourceAdId(true);
//            poi.setSourceAdId(poi.getAd().getAdID());
//        }
//        Vector extraProperties = protoPoiDetail.getExtraProperties();
//        ProtoBizPoi protoBizPoi = protoPoiDetail.getBizPoi();
//        BizPoi bizPoi = null;
//        if (protoBizPoi != null)
//        {
//            Vector supplimentVector = convertSupplimentInfos(protoBizPoi.getSupplementalInfos());
//            poi.setSupplimentInfos(supplimentVector);
//            bizPoi = convertBizPoi(protoBizPoi);
//            bizPoi.setPoiId(protoPoiDetail.getPoiId() + "");
//            poi.setBizPoi(bizPoi);
//            poi.setStop(bizPoi.getStop());
//        }
//        for (int i = 0; i < extraProperties.size(); i++)
//        {
//            ProtoProperty property = (ProtoProperty) extraProperties.elementAt(i);
//            if (ProtoObjectType.POI_EXTRA_HAS_ADS_MENU
//                    .equalsIgnoreCase(property.getKey()))
//            {
//                poi.setIsHasAdsMenu(Boolean.parseBoolean(property.getValue()));
//            }
//            else if (ProtoObjectType.POI_EXTRA_IS_ADS_POI.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                poi.setIsAdsPoi(Boolean.parseBoolean(property.getValue()));
//            }
//            else if (ProtoObjectType.POI_EXTRA_HAS_DEALS.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                poi.setHasCoupon(Boolean.parseBoolean(property.getValue()));
//            }
//            else if (ProtoObjectType.POI_EXTRA_HAS_REVIEWS.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                poi.setIsHasReviews(Boolean.parseBoolean(property.getValue()));
//            }
//            else if (ProtoObjectType.POI_EXTRA_HAS_POI_MENU.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                poi.setIsHasPoiMenu(Boolean.parseBoolean(property.getValue()));
//            }
//            else if (ProtoObjectType.POI_EXTRA_HAS_EXTRA_ATTRIBUTES
//                    .equalsIgnoreCase(property.getKey()))
//            {
//                poi.setIsHasPoiExtraAttributes(Boolean.parseBoolean(property.getValue()));
//            }
//            else if (ProtoObjectType.POI_EXTRA_HAS_POI_DETAILS.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                poi.setIsHasPoiDetail(Boolean.parseBoolean(property.getValue()));
//            }
//            else if (ProtoObjectType.POI_EXTRA_POI_LOGO.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                if(bizPoi != null)
//                {
//                    bizPoi.setPoiLogo(property.getValue());
//                }
//            }
//            else if (ProtoObjectType.POI_EXTRA_BRAND_LOGO.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                if(bizPoi != null)
//                {
//                    bizPoi.setBrandLogo(property.getValue());
//                }
//            }
//            else if (ProtoObjectType.POI_EXTRA_CATEGORY_LOGO.equalsIgnoreCase(property
//                    .getKey()))
//            {
//                if(bizPoi != null)
//                {
//                    bizPoi.setCategoryLogo(property.getValue());
//                }
//            }
//        }
//
//        Vector localAppInfos = protoPoiDetail.getLocalAppInfos();
//        if(localAppInfos != null && localAppInfos.size() > 0)
//        {
//            poi.setLocalAppInfos(localAppInfos);
//        }
//        
//        return poi;
//    }
    
    private static Ad convertAd(ProtoAdvertisement protoAd)
    {
        if(protoAd == null)
        {
            return null;
        }
        Ad ad = new Ad();
        ad.setAdSource(protoAd.getAdSource());
        ad.setAdID(protoAd.getSourceAdId());
        ad.setAdLine(protoAd.getMessage());
        ad.setAdShortLine(protoAd.getShortMessage());
        return ad;
    }

//    private static OpenTableInfo convertOpenTable(ProtoOpenTableInfo protoOpenTableInfo)
//    {
//        if(protoOpenTableInfo == null)
//        {
//            return null;
//        }
//        OpenTableInfo openTableInfo = new OpenTableInfo();
//        openTableInfo.setIsReservable(protoOpenTableInfo.hasIsReservable());
//        openTableInfo.setPartner(protoOpenTableInfo.getPartner());
//        openTableInfo.setPartnerPoiId(protoOpenTableInfo.getPartnerPoiId());
//        return openTableInfo;
//    }

//    private static Vector convertCouponList(Vector protoCouponList)
//    {
//        Vector couponList = new Vector();
//        if (protoCouponList != null)
//        {
//            int size = protoCouponList.size();
//            for (int i = 0; i < size; i++)
//            {
//                ProtoCoupon protoCoupon = (ProtoCoupon) protoCouponList.elementAt(i);
//                Coupon coupon = covertCoupon(protoCoupon);
//                if(coupon != null)
//                {
//                    couponList.addElement(coupon);
//                }
//            }
//        }
//        
//        return couponList;
//    }

    private static Coupon covertCoupon(ProtoCoupon protoCoupon)
    {
        Coupon coupon = null;
        if(protoCoupon != null)
        {
            coupon = new Coupon();
            coupon.setDescription(protoCoupon.getDescription());
            coupon.setEndDate("" +protoCoupon.getEndDate());
            if(protoCoupon.getImage() != null)
            {
                coupon.setImageData(protoCoupon.getImage().toByteArray());
            }
        }
        return coupon;
    }

    public static Poi convertPoi(ProtoBasePoi protoPoi)
    {
        if (protoPoi == null)
            return null;
        
        Poi poi = new Poi();
        poi.setRating(protoPoi.getAvgRating());
        poi.setPopularity(protoPoi.getPopularity());
        poi.setIsRatingEnable(protoPoi.getIsRatingEnable());
        
        ProtoBasePoiExtraInfo protoBasePoiExtraInfo = protoPoi.getBasePoiExtraInfo();
        if (protoBasePoiExtraInfo != null)
        {
            
            poi.setIsHasPoiSourceAdId(protoBasePoiExtraInfo.hasSourceAdId());
            if (protoBasePoiExtraInfo.hasSourceAdId())
            {
                poi.setSourceAdId(protoBasePoiExtraInfo.getSourceAdId());
                Ad ad = new Ad();
                ad.setAdID(protoBasePoiExtraInfo.getSourceAdId());

                if (protoBasePoiExtraInfo.hasShortMessage() && protoBasePoiExtraInfo.getShortMessage() != null)
                {
                    ad.setAdShortLine(protoBasePoiExtraInfo.getShortMessage());
                }
                if (protoBasePoiExtraInfo.hasAdSource() && protoBasePoiExtraInfo.getAdSource() != null)
                {
                    ad.setAdSource(protoBasePoiExtraInfo.getAdSource());
                }
                if (protoBasePoiExtraInfo.hasMessage() && protoBasePoiExtraInfo.getMessage() != null)
                {
                    ad.setAdLine(protoBasePoiExtraInfo.getMessage());
                }
                poi.setAd(ad);
            }
            poi.setRatingNumber(protoBasePoiExtraInfo.getRatingNumber());
            Vector extraProperties = protoBasePoiExtraInfo.getExtraProperties();
            ProtoBizPoi protoBizPoi = protoPoi.getBizPoi();
            BizPoi bizPoi = null;
            if (protoBizPoi != null)
            {
                Vector supplimentVector = convertSupplimentInfos(protoBizPoi.getSupplementalInfos());
                poi.setSupplimentInfos(supplimentVector);
                bizPoi = convertBizPoi(protoBizPoi);
                bizPoi.setPoiId(protoPoi.getPoiId() + "");
                poi.setBizPoi(bizPoi);
                poi.setStop(bizPoi.getStop());
            }
            for(int i = 0; i < extraProperties.size(); i ++)
            {
                ProtoProperty property = (ProtoProperty) extraProperties.elementAt(i);
                if(ProtoObjectType.POI_EXTRA_HAS_ADS_MENU.equalsIgnoreCase(property.getKey()))
                {
                    poi.setIsHasAdsMenu(Boolean.parseBoolean(property.getValue()));
                }
                else if(ProtoObjectType.POI_EXTRA_IS_ADS_POI.equalsIgnoreCase(property.getKey()))
                {
                    poi.setIsAdsPoi(Boolean.parseBoolean(property.getValue()));
                }
                else if(ProtoObjectType.POI_EXTRA_HAS_DEALS.equalsIgnoreCase(property.getKey()))
                {
                    poi.setHasCoupon(Boolean.parseBoolean(property.getValue()));
                }
                else if(ProtoObjectType.POI_EXTRA_HAS_REVIEWS.equalsIgnoreCase(property.getKey()))
                {
                    poi.setIsHasReviews(Boolean.parseBoolean(property.getValue()));
                }
                else if(ProtoObjectType.POI_EXTRA_HAS_POI_MENU.equalsIgnoreCase(property.getKey()))
                {
                    poi.setIsHasPoiMenu(Boolean.parseBoolean(property.getValue()));
                }
                else if(ProtoObjectType.POI_EXTRA_HAS_EXTRA_ATTRIBUTES.equalsIgnoreCase(property.getKey()))
                {
                    poi.setIsHasPoiExtraAttributes(Boolean.parseBoolean(property.getValue()));
                }
                else if(ProtoObjectType.POI_EXTRA_HAS_POI_DETAILS.equalsIgnoreCase(property.getKey()))
                {
                    poi.setIsHasPoiDetail(Boolean.parseBoolean(property.getValue()));
                }
                else if(ProtoObjectType.POI_EXTRA_POI_LOGO.equalsIgnoreCase(property.getKey()))
                {
                    if(bizPoi != null)
                    {
                        bizPoi.setPoiLogo(property.getValue());
                    }
                }
                else if(ProtoObjectType.POI_EXTRA_BRAND_LOGO.equalsIgnoreCase(property.getKey()))
                {
                    if(bizPoi != null)
                    {
                        bizPoi.setBrandLogo(property.getValue());
                    }
                }
                else if(ProtoObjectType.POI_EXTRA_CATEGORY_LOGO.equalsIgnoreCase(property.getKey()))
                {
                    if(bizPoi != null)
                    {
                        bizPoi.setCategoryLogo(property.getValue());
                    }
                }
            }
        }
        
        Vector localAppInfos = protoPoi.getLocalAppInfos();
        if(localAppInfos != null && localAppInfos.size() > 0)
        {
            poi.setLocalAppInfos(localAppInfos);
        }
        
        return poi;
    }
    
    private static Vector convertSupplimentInfos(Vector supplementalStringInfos)
    {
        Vector supplimentInfos = null;
        if (supplementalStringInfos != null && supplementalStringInfos.size() > 0)
        {
            supplimentInfos = new Vector();
            int size = supplementalStringInfos.size();
            for (int i = 0; i < size; i++)
            {
                Object objSupplementalInfo = supplementalStringInfos
                .elementAt(i);
                if (objSupplementalInfo instanceof String)
                {
                    String strSupplementalInfo = (String) objSupplementalInfo;
                    int index = strSupplementalInfo.indexOf(" ");
                    if (index > 0)
                    {
                        SupplimentInfo supplimentInfo = new SupplimentInfo();
                        String priceString = strSupplementalInfo.substring(index + 1);
                        String supplyInfo = strSupplementalInfo.substring(0, index);
                        supplimentInfo.setPrice(priceString);
                        supplimentInfo.setSupportInfo(supplyInfo);
                        supplimentInfos.addElement(supplimentInfo);
                    }
                }
            }
        }
        return supplimentInfos;
    }
    

    private static BizPoi convertBizPoi(ProtoBizPoi protoBizPoi)
    {
        BizPoi bizPoi = new BizPoi();
        bizPoi.setBrand(protoBizPoi.getBrand());
        String strCategoryId = protoBizPoi.getCategoryId();
        bizPoi.setCategoryId(strCategoryId);
        bizPoi.setDistance(protoBizPoi.getDistance()+"");
//        bizPoi.setDistanceWithUnit(protoBizPoi.getDistanceWithUnit());//TODO no protoBizPoi.getDistanceWithUnit
        String phoneNumber = protoBizPoi.getPhoneNumber();
        if(AppConfigHelper.isGlobalPtn())
        {
            if(phoneNumber != null)
            {
                phoneNumber = phoneNumber.trim();
                
                if(phoneNumber.length() > 0 && !phoneNumber.startsWith(PLUS_SIGN))
                {
                    phoneNumber = PLUS_SIGN + phoneNumber;
                }
            }
        }
        bizPoi.setPhoneNumber(phoneNumber);
//        bizPoi.setPoiId(protoBizPoi.getPoiId()); //TODO no protoBizPoi.getPoiId
        bizPoi.setPrice(protoBizPoi.getPrice());
        Stop stop = convertStop(protoBizPoi.getAddress());
        bizPoi.setStop(stop);
        
        return bizPoi;
    }

//    public static DecimatedRoute convertDecimatedRoute(ProtoDecimateRoute protoRoute)
//    {
//        if (protoRoute == null)
//            return null;
//        
//        DecimatedRoute route = RouteDataFactory.getInstance().createDecimatedRoute(protoRoute.getRouteBin().toByteArray());
//        route.setRouteID(protoRoute.getRoutePathId());
//        return route;
//    }
    
//    public static Route convertRoute(ProtoRoute protoRoute)
//    {
//        if (protoRoute == null)
//            return null;
//        
//        Route route = RouteDataFactory.getInstance().createRoute();
//        route.setOriginTimeStamp(protoRoute.getOriginTimeStamp());
//        route.setOriginVe(protoRoute.getOriginVe());
//        route.setOriginVn(protoRoute.getOriginVn());
//        route.setRouteID(protoRoute.getRouteId());
//        Vector v = protoRoute.getSegments();
//        Segment[] segs = new Segment[v.size()];
//        for (int i=0; i<v.size(); i++)
//        {
//            ProtoRouteSegment protoSeg = (ProtoRouteSegment)v.elementAt(i);
//            segs[i] = convertSegment(protoSeg);
//        }
//        route.setSegments(segs);
//        route.setTrafficDelayTime((int)protoRoute.getDelayTime(), route.getLength());
//        
//        return route;
//    }
    
//    public static Segment convertSegment(ProtoRouteSegment protoSeg)
//    {
//        if (protoSeg == null)
//            return null;
//        
//        Segment seg = RouteDataFactory.getInstance().createsSegment();
//        seg.setLength(protoSeg.getLength());
//        seg.setDistanceToDest(protoSeg.getDistanceToDest());
//        seg.setExitName(protoSeg.getExitName());
//        seg.setExitNumber((byte)protoSeg.getExitNumber());
//        seg.setRoadType((byte)protoSeg.getRoadType());
//        seg.setSegmentType((byte)protoSeg.getSegmentType());
//        seg.setSpeedLimit(protoSeg.getSpeedLimit());
//        seg.setStreetAlias(protoSeg.getStreetAlias());
//        seg.setStreetAliasId(protoSeg.getStreetAliasId());
//        seg.setStreetName(protoSeg.getStreetName());
//        seg.setStreetNameId(protoSeg.getStreetNameId());
//        seg.setTurnType((byte)protoSeg.getTurnType());
//        
//        byte[] edgeIdBin = protoSeg.getEdgeID().toByteArray();
//        int len = edgeIdBin.length >> 2;// 4 bytes per int
//        int[] edgeIDs = new int[len];
//        for(int j = 0; j < len; j++)
//        {
//            edgeIDs[j] = DataUtil.readInt(edgeIdBin, j * 4);
//        }
//        seg.setEdgesId(edgeIDs);
//        
//        ProtoLaneInfo laneInfo = protoSeg.getLaneInfo();
//        Vector v = laneInfo.getLanePattens();
//        if (v != null && v.size() > 0)
//        {
//            int laneInfos[] = new int[v.size()];
//            int laneTypes[] = new int[v.size()];
//            for (int i=0; i<v.size(); i++)
//            {
//                ProtoLanPatten lanePatten = (ProtoLanPatten)v.elementAt(i);
//                laneTypes[i] = lanePatten.getLaneType();
//                laneInfos[i] = lanePatten.getBInRoute()? 1 : 0;
//            }
//            seg.setLaneInfos(laneInfos);
//            seg.setLaneTypes(laneTypes);
//        }
//
//        return seg;
//    }
    
//    public static MapTile convertMapTile(ProtoMapTile protoTile)
//    {
//        if (protoTile.getBinData() != null)
//            return parseMapTileBinary(protoTile.getBinData().toByteArray(), protoTile.getTileID(), 
//                    protoTile.getZoom());
//        
//        return null;
//    }
    
//    private static MapTile parseMapTileBinary(byte[] data, long tileID, int zoom)
//    {
//        if(data == null)
//            return null;
//
//        MapTile tile = MapDataFactory.getInstance().createMapTile();
//        tile.setId(tileID);
//        tile.setZoom(zoom);
//        tile.setBinary(data);
//
//        return tile;
//    }
    
    public static FavoriteCatalog convertFavoriteCategory(ProtoFavoriteCategory protoFc)
    {
        if (protoFc == null)
            return null;
        
        FavoriteCatalog fc = new FavoriteCatalog();
        fc.setCatType((byte)protoFc.getCatType());
        fc.setId(protoFc.getId());
        fc.setName(protoFc.getName());
        fc.setStatus((byte)protoFc.getStatus());
        if(protoFc.hasCreateTime())
        {
            fc.setCreateTime(protoFc.getCreateTime());
        }
        if(protoFc.hasUpdateTime())
        {
            fc.setUpdateTime(protoFc.getUpdateTime());
        }
        return fc;
    }
    
    public static ProtoFavoriteCategory convertFavoriteCategory(FavoriteCatalog fc)
    {
        if (fc == null)
            return null;
        
        ProtoFavoriteCategory.Builder builder = ProtoFavoriteCategory.newBuilder();
        builder.setCatType(fc.getCatType());
        builder.setId(fc.getId());
        builder.setName(fc.getName());
        builder.setStatus(fc.getStatus());
        builder.setCreateTime(fc.getCreateTime());
        builder.setUpdateTime(fc.getUpdateTime());
        return builder.build();
    }

//    public static SentAddress convertSendAddress(ProtoSentAddress protoSentAddress )
//    {
//        if (protoSentAddress == null)
//            return null;
//
//        SentAddress sentAddress = new SentAddress();
//        sentAddress.setCreatedTime(protoSentAddress.getCreateTime());
//        sentAddress.setLabel(protoSentAddress.getLabel());
//        sentAddress.setStreet(protoSentAddress.getStreet());
//        sentAddress.setCity(protoSentAddress.getCity());
//        sentAddress.setState(protoSentAddress.getProvince());
//        sentAddress.setZip(protoSentAddress.getPostalCode());
//        sentAddress.setCountry(protoSentAddress.getCountry());
//        AddressRecevier addressRecevier = new AddressRecevier();
//        addressRecevier.setReceivers(protoSentAddress.getReceiverList());
//        sentAddress.setReceiver(addressRecevier);
//        return sentAddress;
//
//    }
}
