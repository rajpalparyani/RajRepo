/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPoiSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.data.datatypes.poi.Ad;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Coupon;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.datatypes.poi.OpenTableInfo;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.poi.ReviewDetail;
import com.telenav.data.datatypes.poi.SupplimentInfo;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public interface IPoiSerializable
{
    public Ad createAd(byte[] data);
    
    public byte[] toBytes(Ad ad);
    
    public BizPoi createBizPoi(byte[] data);
    
    public byte[] toBytes(BizPoi bizPoi);
    
    public Coupon createCoupon(byte[] data);
    
    public byte[] toBytes(Coupon coupon);
    
    public OpenTableInfo createOpenTableInfo(byte[] data);
    
    public byte[] toBytes(OpenTableInfo openTableInfo);
    
    public ReviewDetail createReviewDetail(byte[] data);
    
    public byte[] toBytes(ReviewDetail reviewDetail);
    
    public SupplimentInfo createSupplimentInfo(byte[] data);
    
    public byte[] toBytes(SupplimentInfo supplimentInfo);
    
    public Poi createPoi(byte[] data);
    
    public byte[] toBytes(Poi poi);
    
    public PoiCategory createPoiCategory(byte[] poiCategory);
    
    public byte[] toBytes(PoiCategory category);
    
    public OneBoxSearchBean createOneBoxSearchBean(byte[] data);
    
    public byte[] toBytes(OneBoxSearchBean bean);
}
