/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * JsonPoiSerializable.java
 *
 */
package com.telenav.data.serializable.json;

import java.util.Vector;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;
import org.json.tnme.JSONTokener;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Ad;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Coupon;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.datatypes.poi.OpenTableInfo;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.poi.ReviewDetail;
import com.telenav.data.datatypes.poi.SupplimentInfo;
import com.telenav.data.serializable.IPoiSerializable;
import com.telenav.j2me.framework.protocol.ProtoLocalAppInfo;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-25
 */
class JsonPoiSerializable implements IPoiSerializable
{
    // key for poi
    public static final String KEY_POI_RATED = "rated";

    public static final String KEY_POI_RATING = "rating";

    public static final String KEY_POI_PRICE_RANGE = "priceRange";

    public static final String KEY_POI_RATENUM = "rateNumber";

    public static final String KEY_POI_POPULARITY = "popularity";

    public static final String KEY_POI_USE_PREVIOUS_RATING = "usePreviousRating";

    public static final String KEY_POI_IS_RATING_ENABLE = "isRatingEnable";

    public static final String KEY_POI_MENU = "menu";

    public static final String KEY_POI_TYPE = "type";
    
    public static final String KEY_POI_SOURCE_AD_ID = "adsId";
    
    public static final String KEY_POI_BIZ_POI = "bizPoi";
    
    public static final String KEY_POI_STOP = "stop";
    
    public static final String KEY_POI_AD = "ad";
    public static final String KEY_POI_OPENTABLE = "openTable";
    public static final String KEY_POI_REVIEWDETAILS = "reviewDetails";
    public static final String KEY_POI_SUPPLIMENTINFOS = "supplimentInfos";
    public static final String KEY_POI_COUPONS = "coupons";
    public static final String KEY_POI_LOCAL_APP_INFOS = "localAppInfos";

    // key for biz poi
    public static final String KEY_BIZ_POI_DISTANCE = "distance";
    public static final String KEY_BIZ_POI_DISTANCE_WITH_UNIT = "distanceWithUnit";

    public static final String KEY_BIZ_POI_PHONE_NUMBER = "phoneNumber";

    public static final String KEY_BIZ_POI_PRICE = "price";

    public static final String KEY_BIZ_POI_BRAND = "brand";

    public static final String KEY_BIZ_POI_POI_ID = "poiId";

    public static final String KEY_BIZ_POI_CATEGORY_ID = "categoryId";

    public static final String KEY_BIZ_POI_CATEGORY_NAME = "categoryName";

    public static final String KEY_BIZ_POI_STOP = "stop";
    
    //key for ad
    public static final String KEY_AD_TAG = "adTag";
    public static final String KEY_AD_LINE = "adLine";
    public static final String KEY_AD_SOURCE = "adSource";
    public static final String KEY_AD_ID = "adID";
    
    //key for coupon
    public static final String KEY_COUPON_DESCRIPTION = "description";
    public static final String KEY_COUPON_END_DATE = "endDate";
    public static final String KEY_COUPON_IMAGE = "imageData";
    
    //key for opentable
    public static final String KEY_OPENTABLE_PARTNER = "partner";
    public static final String KEY_OPENTABLE_PARTNER_POIID = "partnerPoiId";
    public static final String KEY_OPENTABLE_ISRESERVABLE = "isReservable";
    
    //key for review detail
    public static final String KEY_REVIEWDETAIL_RATING = "rating";
    public static final String KEY_REVIEWDETAIL_POIID = "poiId";
    public static final String KEY_REVIEWDETAIL_REVIEWID = "reviewId";
    public static final String KEY_REVIEWDETAIL_REVIEWER_NAME = "reviewerName";
    public static final String KEY_REVIEWDETAIL_REVIEW_TEXT = "reviewText";
    public static final String KEY_REVIEWDETAIL_UPDATE_DATE = "updateDate";
    public static final String KEY_REVIEWDETAIL_TYPE = "type";
    
    //key for supplimentInfo
    public static final String KEY_SUPPLIMENTINFO_PRICE = "price";
    public static final String KEY_SUPPLIMENTINFO_SUPPORTINFO = "supportInfo";
    
    public Ad createAd(byte[] data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new String(data)));
            
            return createAd(jsonObject);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        return null;
    }
    
    static Ad createAd(JSONObject jsonObject)
    {
        try
        {
            Ad result = new Ad();

            if (jsonObject.get(KEY_AD_TAG) != null)
            {
                result.setAdTag(jsonObject.getString(KEY_AD_TAG));
            }
            if (jsonObject.get(KEY_AD_LINE) != null)
            {
                result.setAdLine(jsonObject.getString(KEY_AD_LINE));
            }
            if (jsonObject.get(KEY_AD_SOURCE) != null)
            {
                result.setAdSource(jsonObject.getString(KEY_AD_SOURCE));
            }
            if (jsonObject.get(KEY_AD_ID) != null)
            {
                result.setAdID(jsonObject.getString(KEY_AD_ID));
            }
            
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public BizPoi createBizPoi(byte[] data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new String(data)));
            
            return createBizPoi(jsonObject);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return null;
    }
    
    static BizPoi createBizPoi(JSONObject jsonObject)
    {
        try
        {
            BizPoi result = new BizPoi();

            if (jsonObject.get(KEY_BIZ_POI_DISTANCE) != null)
            {
                result.setDistance(jsonObject.getString(KEY_BIZ_POI_DISTANCE));
            }
            if (jsonObject.get(KEY_BIZ_POI_PHONE_NUMBER) != null)
            {
                result.setPhoneNumber(jsonObject.getString(KEY_BIZ_POI_PHONE_NUMBER));
            }
            if (jsonObject.get(KEY_BIZ_POI_PRICE) != null)
            {
                result.setPrice(jsonObject.getString(KEY_BIZ_POI_PRICE));
            }
            if (jsonObject.get(KEY_BIZ_POI_BRAND) != null)
            {
                result.setBrand(jsonObject.getString(KEY_BIZ_POI_BRAND));
            }
            if (jsonObject.get(KEY_BIZ_POI_POI_ID) != null)
            {
                result.setPoiId(jsonObject.getString(KEY_BIZ_POI_POI_ID));
            }
            if (jsonObject.get(KEY_BIZ_POI_CATEGORY_ID) != null)
            {
                result.setCategoryId(jsonObject.getString(KEY_BIZ_POI_CATEGORY_ID));
            }
            if (jsonObject.get(KEY_BIZ_POI_CATEGORY_NAME) != null)
            {
                result.setCategoryName(jsonObject.getString(KEY_BIZ_POI_CATEGORY_NAME));
            }
            if (jsonObject.get(KEY_BIZ_POI_DISTANCE_WITH_UNIT) != null)
            {
                result.setDistanceWithUnit(jsonObject.getString(KEY_BIZ_POI_DISTANCE_WITH_UNIT));
            }
            if (jsonObject.get(KEY_BIZ_POI_STOP) != null)
            {
                Stop stop = JsonAddressSerializable.createStop(jsonObject.getJSONObject(KEY_BIZ_POI_STOP));
                if (stop != null)
                {
                    result.setStop(stop);
                }
            }
            
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Coupon createCoupon(byte[] data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new String(data)));
            
            return createCoupon(jsonObject);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return null;
    }
    
    static Coupon createCoupon(JSONObject jsonObject)
    {
        try
        {
            Coupon result = new Coupon();

            if (jsonObject.get(KEY_COUPON_DESCRIPTION) != null)
            {
                result.setDescription(jsonObject.getString(KEY_COUPON_DESCRIPTION));
            }
            if (jsonObject.get(KEY_COUPON_END_DATE) != null)
            {
                result.setEndDate(jsonObject.getString(KEY_COUPON_END_DATE));
            }
            
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public OneBoxSearchBean createOneBoxSearchBean(byte[] data)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OpenTableInfo createOpenTableInfo(byte[] data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new String(data)));

            return createOpenTableInfo(jsonObject);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        return null;
    }
    
    static OpenTableInfo createOpenTableInfo(JSONObject jsonObject)
    {
        try
        {
            OpenTableInfo result = new OpenTableInfo();

            if (jsonObject.get(KEY_OPENTABLE_PARTNER) != null)
            {
                result.setPartner(jsonObject.getString(KEY_OPENTABLE_PARTNER));
            }
            if (jsonObject.get(KEY_OPENTABLE_PARTNER_POIID) != null)
            {
                result.setPartnerPoiId(jsonObject.getString(KEY_OPENTABLE_PARTNER_POIID));
            }
            if (jsonObject.get(KEY_OPENTABLE_ISRESERVABLE) != null)
            {
                result.setIsReservable(jsonObject.optBoolean(KEY_OPENTABLE_ISRESERVABLE));
            }

            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public Poi createPoi(byte[] data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new String(data)));
            
            return createPoi(jsonObject);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }

        return null;
    }

    static Poi createPoi(JSONObject jsonObject )
    {
        try
        {
            Poi result = new Poi();

            if (jsonObject.get(KEY_POI_RATED) != null)
            {
                result.setIsRated(jsonObject.optBoolean(KEY_POI_RATED));
            }
            if (jsonObject.get(KEY_POI_RATING) != null)
            {
                result.setRating(jsonObject.optInt(KEY_POI_RATING));
            }
            if (jsonObject.get(KEY_POI_PRICE_RANGE) != null)
            {
                result.setPriceRange(jsonObject.getString(KEY_POI_PRICE_RANGE));
            }
            if (jsonObject.get(KEY_POI_RATENUM) != null)
            {
                result.setRatingNumber(jsonObject.optInt(KEY_POI_RATENUM));
            }
            if (jsonObject.get(KEY_POI_POPULARITY) != null)
            {
                result.setPopularity(jsonObject.optInt(KEY_POI_POPULARITY));
            }
            if (jsonObject.get(KEY_POI_USE_PREVIOUS_RATING) != null)
            {
                result.setUsePreviousRating(jsonObject.optInt(KEY_POI_USE_PREVIOUS_RATING));
            }
            if (jsonObject.get(KEY_POI_IS_RATING_ENABLE) != null)
            {
                result.setIsRatingEnable(jsonObject.optBoolean(KEY_POI_IS_RATING_ENABLE));
            }
            if (jsonObject.get(KEY_POI_MENU) != null)
            {
                result.setMenu(jsonObject.getString(KEY_POI_MENU));
            }

            if (jsonObject.get(KEY_POI_TYPE) != null)
            {
                result.setType(jsonObject.optInt(KEY_POI_TYPE));
            }
            
            if (jsonObject.get(KEY_POI_SOURCE_AD_ID) != null)
            {
                result.setSourceAdId(jsonObject.getString(KEY_POI_SOURCE_AD_ID));
                result.setIsHasPoiSourceAdId(true);
            }
            
            if (jsonObject.get(KEY_POI_OPENTABLE) != null)
            {
                result.setOpenTableInfo(createOpenTableInfo(jsonObject.getJSONObject(KEY_POI_OPENTABLE)));
            }
            
            if (jsonObject.get(KEY_POI_BIZ_POI) != null)
            {
                result.setBizPoi(createBizPoi(jsonObject.getJSONObject(KEY_POI_BIZ_POI)));
            }
            
            if (jsonObject.get(KEY_POI_STOP) != null)
            {
                result.setStop(JsonAddressSerializable.createStop(jsonObject.getJSONObject(KEY_POI_STOP)));
            }
            
            if (jsonObject.get(KEY_POI_AD) != null)
            {
                result.setAd(createAd(jsonObject.getJSONObject(KEY_POI_AD)));
            }
            
            if (jsonObject.get(KEY_POI_REVIEWDETAILS) != null)
            {
                JSONArray array = jsonObject.getJSONArray(KEY_POI_REVIEWDETAILS);
                Vector reviewDetails = new Vector();
                for(int i = 0; i < array.length(); i++)
                {
                    reviewDetails.addElement(createReviewDetail(array.getJSONObject(i)));
                }
                result.setReviewDetails(reviewDetails);
            }
            
            if (jsonObject.get(KEY_POI_SUPPLIMENTINFOS) != null)
            {
                JSONArray array = jsonObject.getJSONArray(KEY_POI_SUPPLIMENTINFOS);
                Vector supplimentInfos = new Vector();
                for(int i = 0; i < array.length(); i++)
                {
                    supplimentInfos.addElement(createSupplimentInfo(array.getJSONObject(i)));
                }
                result.setSupplimentInfos(supplimentInfos);
            }
            
            if (jsonObject.get(KEY_POI_COUPONS) != null)
            {
                JSONArray array = jsonObject.getJSONArray(KEY_POI_COUPONS);
                Vector coupons = new Vector();
                for(int i = 0; i < array.length(); i++)
                {
                    coupons.addElement(createCoupon(array.getJSONObject(i)));
                }
                result.setCoupons(coupons);
            }
            
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public PoiCategory createPoiCategory(byte[] poiCategory)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ReviewDetail createReviewDetail(byte[] data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new String(data)));
            
            return createReviewDetail(jsonObject);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        return null;
    }
    
    static ReviewDetail createReviewDetail(JSONObject jsonObject)
    {
        try
        {
            ReviewDetail result = new ReviewDetail();

            if (jsonObject.get(KEY_REVIEWDETAIL_RATING) != null)
            {
                result.setRating(jsonObject.getString(KEY_REVIEWDETAIL_RATING));
            }
            if (jsonObject.get(KEY_REVIEWDETAIL_POIID) != null)
            {
                result.setPoiId(jsonObject.optInt(KEY_REVIEWDETAIL_POIID));
            }
            if (jsonObject.get(KEY_REVIEWDETAIL_REVIEWID) != null)
            {
                result.setReviewId(jsonObject.optInt(KEY_REVIEWDETAIL_REVIEWID));
            }
            if (jsonObject.get(KEY_REVIEWDETAIL_REVIEWER_NAME) != null)
            {
                result.setReviewerName(jsonObject.getString(KEY_REVIEWDETAIL_REVIEWER_NAME));
            }
            if (jsonObject.get(KEY_REVIEWDETAIL_REVIEW_TEXT) != null)
            {
                result.setReviewText(jsonObject.getString(KEY_REVIEWDETAIL_REVIEW_TEXT));
            }
            if (jsonObject.get(KEY_REVIEWDETAIL_UPDATE_DATE) != null)
            {
                result.setUpdateDate(jsonObject.getString(KEY_REVIEWDETAIL_UPDATE_DATE));
            }
            if (jsonObject.get(KEY_REVIEWDETAIL_TYPE) != null)
            {
                result.setType(jsonObject.optInt(KEY_REVIEWDETAIL_TYPE));
            }
            
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public SupplimentInfo createSupplimentInfo(byte[] data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new JSONTokener(new String(data)));
            
            return createSupplimentInfo(jsonObject);
        }
        catch (JSONException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        return null;
    }

    static SupplimentInfo createSupplimentInfo(JSONObject jsonObject)
    {
        try
        {
            SupplimentInfo result = new SupplimentInfo();

            if (jsonObject.get(KEY_SUPPLIMENTINFO_PRICE) != null)
            {
                result.setPrice(jsonObject.getString(KEY_SUPPLIMENTINFO_PRICE));
            }
            if (jsonObject.get(KEY_SUPPLIMENTINFO_SUPPORTINFO) != null)
            {
                result.setSupportInfo(jsonObject.getString(KEY_SUPPLIMENTINFO_SUPPORTINFO));
            }
            
            return result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] toBytes(Ad ad)
    {
        JSONObject result = new JSONObject();
        toBytes(ad, result);
        
        return result.toString().getBytes();
    }
    
    
    
    static void toBytes(Ad ad, JSONObject result)
    {
        try
        {
            if(ad.getAdTag() != null && ad.getAdTag().trim().length() != 0)
            {
                result.put(KEY_AD_TAG, ad.getAdTag());
            }
            if(ad.getAdLine() != null && ad.getAdLine().trim().length() != 0)
            {
                result.put(KEY_AD_LINE, ad.getAdLine());
            }
            if(ad.getAdSource() != null && ad.getAdSource().trim().length() != 0)
            {
                result.put(KEY_AD_SOURCE, ad.getAdSource());
            }
            if(ad.getAdID() != null && ad.getAdID().trim().length() != 0)
            {
                result.put(KEY_AD_ID, ad.getAdID());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] toBytes(BizPoi bizPoi)
    {
        JSONObject result = new JSONObject();
        toBytes(bizPoi, result);
        
        return result.toString().getBytes();
    }
    
    static void toBytes(BizPoi bizPoi, JSONObject result)
    {
        try
        {
            result.put(KEY_BIZ_POI_DISTANCE, bizPoi.getDistance());
            result.put(KEY_BIZ_POI_DISTANCE_WITH_UNIT, bizPoi.getDistanceWithUnit());
            result.put(KEY_BIZ_POI_PHONE_NUMBER, bizPoi.getPhoneNumber());
            result.put(KEY_BIZ_POI_PRICE, bizPoi.getPrice());
            result.put(KEY_BIZ_POI_BRAND, bizPoi.getBrand());
            result.put(KEY_BIZ_POI_POI_ID, bizPoi.getPoiId());
            result.put(KEY_BIZ_POI_CATEGORY_ID, bizPoi.getCategoryId());
            result.put(KEY_BIZ_POI_CATEGORY_NAME, bizPoi.getCategoryName());
            if(bizPoi.getStop() != null)
            {
                JSONObject stopObject = new JSONObject();
                JsonAddressSerializable.toBytes(bizPoi.getStop(), stopObject);
                result.put(KEY_BIZ_POI_STOP, stopObject);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public byte[] toBytes(Coupon coupon)
    {
        JSONObject result = new JSONObject();
        
        toBytes(coupon, result);
        
        return result.toString().getBytes();
    }

    static void localAppInfosToBytes(Vector localAppInfos, JSONObject result)
    {
        if(localAppInfos != null && localAppInfos.size() > 0)
        {
            try
            {
                JSONArray array = new JSONArray();
                for(int i = 0; i < localAppInfos.size(); i++)
                {
                    JSONObject localAppInfoObject = new JSONObject();
                    ProtoLocalAppInfo protoLocalAppInfo = (ProtoLocalAppInfo)localAppInfos.elementAt(i);
                    
                    JSONArray propertyArray = new JSONArray();
                    for(int m = 0; m < protoLocalAppInfo.getProps().size(); m++)
                    {
                        JSONObject propertyObj = new JSONObject();
                        ProtoProperty property = (ProtoProperty)protoLocalAppInfo.getProps().elementAt(m);
                        propertyObj.put(property.getKey(), property.getValue());
                        propertyArray.put(m, propertyObj);
                    }
                    localAppInfoObject.put(protoLocalAppInfo.getName(), propertyArray);
                    
                    array.put(i, localAppInfoObject);
                }
                result.put(KEY_POI_LOCAL_APP_INFOS, array);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    static void toBytes(Coupon coupon, JSONObject result)
    {
        try
        {
            result.put(KEY_COUPON_DESCRIPTION, coupon.getDescription());
            result.put(KEY_COUPON_END_DATE, coupon.getEndDate());
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static void toBytes(OpenTableInfo openTableInfo, JSONObject result)
    {
        try
        {
            result.put(KEY_OPENTABLE_PARTNER, openTableInfo.getPartner());
            result.put(KEY_OPENTABLE_PARTNER_POIID, openTableInfo.getPartnerPoiId());
            result.put(KEY_OPENTABLE_ISRESERVABLE, openTableInfo.getIsReservable());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public byte[] toBytes(OpenTableInfo openTableInfo)
    {
        JSONObject result = new JSONObject();

        toBytes(openTableInfo, result);

        return result.toString().getBytes();
    }

    public byte[] toBytes(ReviewDetail reviewDetail)
    {
        JSONObject result = new JSONObject();
        
        toBytes(reviewDetail, result);
        
        return result.toString().getBytes();
    }
    
    static void toBytes(ReviewDetail reviewDetail, JSONObject result)
    {
        try
        {
            result.put(KEY_REVIEWDETAIL_RATING, reviewDetail.getRating());
            result.put(KEY_REVIEWDETAIL_POIID, reviewDetail.getPoiId());
            result.put(KEY_REVIEWDETAIL_REVIEWID, reviewDetail.getReviewId());
            result.put(KEY_REVIEWDETAIL_REVIEWER_NAME, reviewDetail.getReviewerName());
            result.put(KEY_REVIEWDETAIL_REVIEW_TEXT, reviewDetail.getReviewText());
            result.put(KEY_REVIEWDETAIL_UPDATE_DATE, reviewDetail.getUpdateDate());
            result.put(KEY_REVIEWDETAIL_TYPE, reviewDetail.getType());
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }
    
    public byte[] toBytes(SupplimentInfo supplimentInfo)
    {
        JSONObject result = new JSONObject();
        
        toBytes(supplimentInfo, result);
        
        return result.toString().getBytes();
    }

    static void toBytes(SupplimentInfo supplimentInfo, JSONObject result)
    {
        try
        {
            result.put(KEY_SUPPLIMENTINFO_PRICE, supplimentInfo.getPrice());
            result.put(KEY_SUPPLIMENTINFO_SUPPORTINFO, supplimentInfo.getSupportInfo());
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }

    public byte[] toBytes(Poi poi)
    {
        JSONObject result = new JSONObject();
        
        toBytes(poi, result);
        
        return result.toString().getBytes();
    }
    
    static void toBytes(Poi poi, JSONObject result)
    {
        try
        {
            result.put(KEY_POI_RATED, poi.getIsRated());
            result.put(KEY_POI_RATING, poi.getRating());
            result.put(KEY_POI_PRICE_RANGE, poi.getPriceRange());
            result.put(KEY_POI_RATENUM, poi.getRatingNumber());
            result.put(KEY_POI_POPULARITY, poi.getPopularity());
            result.put(KEY_POI_USE_PREVIOUS_RATING, poi.getUsePreviousRating());
            result.put(KEY_POI_IS_RATING_ENABLE, poi.getIsRatingEnable());
            result.put(KEY_POI_MENU, poi.getMenu());
            result.put(KEY_POI_TYPE, poi.getType());
            
            if (poi.hasPoiSourceAdId())
            {
                result.put(KEY_POI_SOURCE_AD_ID, poi.getSourceAdId());
            }
            
            if(poi.getOpenTableInfo() != null)
            {
                JSONObject openTableObject = new JSONObject();
                toBytes(poi.getOpenTableInfo(), openTableObject);
                result.put(KEY_POI_OPENTABLE, openTableObject);
            }
            
            if(poi.getBizPoi() != null)
            {
                JSONObject bizPoiObject = new JSONObject();
                toBytes(poi.getBizPoi(), bizPoiObject);
                result.put(KEY_POI_BIZ_POI, bizPoiObject);
            }
            
            if(poi.getStop() != null)
            {
                JSONObject stopObject = new JSONObject();
                JsonAddressSerializable.toBytes(poi.getStop(), stopObject);
                result.put(KEY_POI_STOP, stopObject);
            }
            
            if(poi.getAd() != null)
            {
                JSONObject adObject = new JSONObject();
                toBytes(poi.getAd(), adObject);
                result.put(KEY_POI_AD, adObject);
            }
            
            if(poi.getReviewDetails() != null && poi.getReviewDetails().size() > 0)
            {
                JSONArray array = new JSONArray();
                for(int i = 0; i < poi.getReviewDetails().size(); i++)
                {
                    JSONObject adObject = new JSONObject();
                    toBytes((ReviewDetail)poi.getReviewDetails().elementAt(i), adObject);
                    array.put(i, adObject);
                }
                result.put(KEY_POI_REVIEWDETAILS, array);
            }
            
            if(poi.getSupplimentInfos() != null && poi.getSupplimentInfos().size() > 0)
            {
                JSONArray array = new JSONArray();
                for(int i = 0; i < poi.getSupplimentInfos().size(); i++)
                {
                    JSONObject adObject = new JSONObject();
                    SupplimentInfo supplimentInfo = (SupplimentInfo)poi.getSupplimentInfos().elementAt(i);
                    toBytes(supplimentInfo, adObject);
                    array.put(i, adObject);
                }
                result.put(KEY_POI_SUPPLIMENTINFOS, array);
            }
            
            if(poi.getCoupons() != null && poi.getCoupons().size() > 0)
            {
                JSONArray array = new JSONArray();
                for(int i = 0; i < poi.getCoupons().size(); i++)
                {
                    JSONObject adObject = new JSONObject();
                    toBytes((Coupon)poi.getCoupons().elementAt(i), adObject);
                    array.put(i, adObject);
                }
                result.put(KEY_POI_COUPONS, array);
            }
            
            if(poi.getLocalAppInfos() != null && poi.getLocalAppInfos().size() > 0)
            {
                localAppInfosToBytes(poi.getLocalAppInfos(), result);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] toBytes(PoiCategory category)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public byte[] toBytes(OneBoxSearchBean bean)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
