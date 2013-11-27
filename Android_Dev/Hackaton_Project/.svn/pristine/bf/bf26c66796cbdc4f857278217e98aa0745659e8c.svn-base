/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Poi.java
 *
 */
package com.telenav.data.datatypes.poi;

import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;

/**
 *@author bduan
 *@date 2010-11-11
 */
public class Poi
{
    public static final int TYPE_POI                    = 196;
    public static final int TYPE_SPONSOR_POI            = 127;
    public static final int TYPE_DUMMY_POI              = -100;
    
    public static final int TYPE_STOP                   = 28;
    public static final int TYPE_BUSINESS_DETAIL        = 30;
    public static final int TYPE_SUPPLIMENT_INFO        = 122;
    public static final int TYPE_REVIEW_DETAIL          = 123;
    public static final int TYPE_AD                     = 124;
    public static final int TYPE_OPENTABLEINFO          = 125;
    public static final int TYPE_COUPON                 = 126;
    
    protected int type;
    protected boolean isRated = false;
    
    protected int rating;
    protected String priceRange;
    protected int popularity;
    protected int ratingNumber;
    protected int userPreviousRating;
    protected boolean isRatingEnable;
    
    protected OpenTableInfo openTableInfo;
    protected BizPoi bizPoi;
    protected Stop stop;
    protected Ad ad;
    
    //may exist multi-infos for below type.
    protected Vector reviewDetails;
    protected Vector coupons;
    protected Vector supplimentInfos;
    protected Vector localAppInfos;
    protected String menu;
    
    protected boolean hasCoupon;
    protected boolean isAdsPoi;
    protected boolean hasAdsMenu;
    protected boolean hasPoiDetail;
    protected boolean hasPoiExtraAttributes;
    protected boolean hasReviews;
    protected boolean hasPoiMenu;
    protected boolean hasPoiSourceAdId = false;
    
    protected String sourceAdId;
    
    public boolean isHasCoupon()
    {
        return hasCoupon;
    }

    public void setHasCoupon(boolean hasCoupon)
    {
        this.hasCoupon = hasCoupon;
    }

    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public boolean getIsRated()
    {
        return isRated;
    }
    
    public void setIsRated(boolean isRated)
    {
        this.isRated = isRated;
    }
    
    public int getRating()
    {
        return rating;
    }
    
    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public String getPriceRange()
    {
        return priceRange;
    }
    
    public void setPriceRange(String priceRange)
    {
        this.priceRange = priceRange;
    }

    public int getPopularity()
    {
        return popularity;
    }
    
    public void setPopularity(int popularity)
    {
        this.popularity = popularity;
    }

    public int getRatingNumber()
    {
        return ratingNumber;
    }
    
    public void setRatingNumber(int ratingNumber)
    {
        this.ratingNumber = ratingNumber;
    }

    public int getUsePreviousRating()
    {
        return userPreviousRating;
    }
    
    public void setUsePreviousRating(int userPreviousRating)
    {
        this.userPreviousRating = userPreviousRating;
    }

    public boolean getIsRatingEnable()
    {
        return isRatingEnable;
    }
    
    public void setIsRatingEnable(boolean isRatingEnable)
    {
        this.isRatingEnable = isRatingEnable;
    }

    public OpenTableInfo getOpenTableInfo()
    {
        return openTableInfo;
    }
    
    public void setOpenTableInfo(OpenTableInfo openTableInfo)
    {
        this.openTableInfo = openTableInfo;
    }

    public BizPoi getBizPoi()
    {
        return bizPoi;
    }
    
    public void setBizPoi(BizPoi bizPoi)
    {
        this.bizPoi = bizPoi;
    }

    public Stop getStop()
    {
        return stop;
    }
    
    public void setStop(Stop stop)
    {
        this.stop = stop;
    }

    public Ad getAd()
    {
        return ad;
    }
    
    public void setAd(Ad ad)
    {
        this.ad = ad;
    }
    
    public Vector getReviewDetails()
    {
        return reviewDetails;
    }
    
    public void setReviewDetails(Vector reviewDetails)
    {
        this.reviewDetails = reviewDetails;
    }

    public Vector getCoupons()
    {
        return coupons;
    }
    
    public void setCoupons(Vector coupons)
    {
        this.coupons = coupons;
    }

    public Vector getSupplimentInfos()
    {
        return supplimentInfos;
    }
    
    public void setSupplimentInfos(Vector supplimentInfos)
    {
        this.supplimentInfos = supplimentInfos;
    }
    
    public Vector getLocalAppInfos()
    {
        return localAppInfos;
    }
    
    public void setLocalAppInfos(Vector localAppInfos)
    {
        this.localAppInfos = localAppInfos;
    }
    
    public String getMenu()
    {
        return menu;
    }
    
    public void setMenu(String menu)
    {
        this.menu = menu;
    }
    
    public boolean isAdsPoi()
    {
        return this.isAdsPoi;
    }
    
    public void setIsAdsPoi(boolean isAdsPoi)
    {
        this.isAdsPoi = isAdsPoi;
    }
    
    public boolean hasAdsMenu()
    {
        return this.hasAdsMenu;
    }
    
    public void setIsHasAdsMenu(boolean hasAdsMenu)
    {
        this.hasAdsMenu = hasAdsMenu;
    }
    
    public boolean hasPoiDetail()
    {
        return this.hasPoiDetail;
    }
    
    public void setIsHasPoiDetail(boolean isHasPoiDetail)
    {
        this.hasPoiDetail = isHasPoiDetail;
    }
    
    public boolean hasPoiExtraAttributes()
    {
        return this.hasPoiExtraAttributes;
    }
    
    public void setIsHasPoiExtraAttributes(boolean isHasPoiExtraAttributes)
    {
        this.hasPoiExtraAttributes = isHasPoiExtraAttributes;
    }
    
    public boolean hasReviews()
    {
        return this.hasReviews;
    }
    
    public void setIsHasReviews(boolean hasReviews)
    {
        this.hasReviews = hasReviews;
    }
    
    public void setIsHasPoiMenu(boolean hasPoiMenu)
    {
        this.hasPoiMenu = hasPoiMenu;
    }
    
    public boolean hasPoiMenu()
    {
        return this.hasPoiMenu;
    }
    
    public void setIsHasPoiSourceAdId(boolean hasPoiSourceAdId)
    {
        this.hasPoiSourceAdId = hasPoiSourceAdId;
    }
    
    public boolean hasPoiSourceAdId()
    {
        return this.hasPoiSourceAdId;
    }
    
    public void setSourceAdId(String sourceAdId)
    {
        this.sourceAdId = sourceAdId;
    }
    
    public String getSourceAdId()
    {
        return this.sourceAdId;
    }
}
