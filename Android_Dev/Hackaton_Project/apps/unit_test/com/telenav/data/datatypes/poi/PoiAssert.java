/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiAssert.java
 *
 */
package com.telenav.data.datatypes.poi;

import com.telenav.data.datatypes.address.StopAssert;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-20
 */
public class PoiAssert
{
    public static void assertPoi(Poi expected, Poi actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
//        TestCase.assertEquals(expected.getBrandLogo(), actual.getBrandLogo());
//        TestCase.assertEquals(expected.getCategoryLogo(), actual.getCategoryLogo());
        TestCase.assertEquals(expected.hasAdsMenu(), actual.hasAdsMenu());
        TestCase.assertEquals(expected.isHasCoupon(), actual.isHasCoupon());
        TestCase.assertEquals(expected.hasPoiDetail(), actual.hasPoiDetail());
        TestCase.assertEquals(expected.hasPoiExtraAttributes(), actual.hasPoiExtraAttributes());
        TestCase.assertEquals(expected.hasPoiMenu(), actual.hasPoiMenu());
        TestCase.assertEquals(expected.hasReviews(), actual.hasReviews());
        TestCase.assertEquals(expected.isAdsPoi(), actual.isAdsPoi());
        TestCase.assertEquals(expected.getIsRated(), actual.getIsRated());
        TestCase.assertEquals(expected.getIsRatingEnable(), actual.getIsRatingEnable());
        TestCase.assertEquals(expected.getMenu(), actual.getMenu());
//        TestCase.assertEquals(expected.getPoiLogo(), actual.getPoiLogo());
        TestCase.assertEquals(expected.getPopularity(), actual.getPopularity());
        TestCase.assertEquals(expected.getPriceRange(), actual.getPriceRange());
        TestCase.assertEquals(expected.getRating(), actual.getRating());
        TestCase.assertEquals(expected.getRatingNumber(), actual.getRatingNumber());
        TestCase.assertEquals(expected.getType(), actual.getType());
        TestCase.assertEquals(expected.getUsePreviousRating(), actual.getUsePreviousRating());
        TestCase.assertEquals(expected.getReviewDetails(), actual.getReviewDetails());
        TestCase.assertEquals(expected.getCoupons(), actual.getCoupons());
        TestCase.assertEquals(expected.getSupplimentInfos(), actual.getSupplimentInfos());
        assertOpenTableInfo(expected.getOpenTableInfo(), actual.getOpenTableInfo());
        assertBizPoi(expected.getBizPoi(), actual.getBizPoi());
        StopAssert.assertStop(expected.getStop(), actual.getStop());
        assertAd(expected.getAd(), actual.getAd());
    }
    
    public static void assertBizPoi(BizPoi expected, BizPoi actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.getBrand(), actual.getBrand());
        TestCase.assertEquals(expected.getCategoryId(), actual.getCategoryId());
        TestCase.assertEquals(expected.getCategoryName(), actual.getCategoryName());
        TestCase.assertEquals(expected.getDistance(), actual.getDistance());
        TestCase.assertEquals(expected.getDistanceWithUnit(), actual.getDistanceWithUnit());
        TestCase.assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        TestCase.assertEquals(expected.getPoiId(), actual.getPoiId());
        TestCase.assertEquals(expected.getPrice(), actual.getPrice());
        StopAssert.assertStop(expected.getStop(), actual.getStop());
    }
    
    public static void assertAd(Ad expected, Ad actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.getAdID(), actual.getAdID());
        TestCase.assertEquals(expected.getAdLine(), actual.getAdLine());
        TestCase.assertEquals(expected.getAdSource(), actual.getAdSource());
        TestCase.assertEquals(expected.getAdTag(), actual.getAdTag());
    }
    
    public static void assertCoupon(Coupon expected, Coupon actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.getDescription(), actual.getDescription());
        TestCase.assertEquals(expected.getEndDate(), actual.getEndDate());
        TestCase.assertEquals(expected.getImageData(), actual.getImageData());
    }
    
    public static void assertOneBoxSearchBean(OneBoxSearchBean expected, OneBoxSearchBean actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.getContent(), actual.getContent());
        TestCase.assertEquals(expected.getKey(), actual.getKey());
    }
    
    public static void assertOpenTableInfo(OpenTableInfo expected, OpenTableInfo actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.getIsReservable(), actual.getIsReservable());
        TestCase.assertEquals(expected.getPartner(), actual.getPartner());
        TestCase.assertEquals(expected.getPartnerPoiId(), actual.getPartnerPoiId());
    }
    
    public static void assertSupplimentInfo(SupplimentInfo expected, SupplimentInfo actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.getPrice(), actual.getPrice());
        TestCase.assertEquals(expected.getSupportInfo(), actual.getSupportInfo());
    }
    
    public static void assertPoiCategory(PoiCategory expected, PoiCategory actual)
    {
        if(expected == null)
        {
            TestCase.assertNull(actual);
            return;
        }
        else
        {
            TestCase.assertNotNull(actual);
        }
        TestCase.assertEquals(expected.getCategoryId(), actual.getCategoryId());
        TestCase.assertEquals(expected.getFocusedImagePath(), actual.getFocusedImagePath());
        TestCase.assertEquals(expected.getUnfocusedImagePath(), actual.getUnfocusedImagePath());
        TestCase.assertEquals(expected.getName(), actual.getName());
        TestCase.assertEquals(expected.getChildrenSize(), actual.getChildrenSize());
        for(int i = 0; i < expected.getChildrenSize(); i++)
        {
            assertPoiCategory(expected.getChildAt(i), actual.getChildAt(i));
        }
        assertPoiCategory(expected.getParent(), actual.getParent());
    }
}
