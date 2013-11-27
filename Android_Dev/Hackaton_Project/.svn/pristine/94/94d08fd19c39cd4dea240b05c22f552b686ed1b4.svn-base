/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BizPoi.java
 *
 */
package com.telenav.data.datatypes.poi;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkProxyUtil;

/**
 *@author bduan
 *@date 2010-11-11
 */
public class BizPoi
{
    protected String distance;
    protected String distanceUnit;
    protected String phoneNumber;
    protected String price;
    protected String brand;
    protected String poiId;
    protected String categoryId;
    protected String categoryName;
    protected String categoryLogo;
    protected String poiLogo;
    protected String brandLogo;
    protected Stop stop;
    
    
    public void setDistanceWithUnit(String distance)
    {
        this.distanceUnit = distance;
    }
    
    public String getDistanceWithUnit()
    {
        return this.distanceUnit;
    }
    
    public String getDistance()
    {
        return distance;
    }
    
    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getBrand()
    {
        return brand;
    }
    
    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getPoiId()
    {
        return poiId;
    }
    
    public void setPoiId(String poiId)
    {
        this.poiId = poiId;
    }

    public String getCategoryId()
    {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
        NavSdkProxyUtil.fillCategory(this);
    }

    public String getCategoryName()
    {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }
    
    public void setStop(Stop stop)
    {
        this.stop = stop;
    }
    
    public Stop getStop()
    {
        return this.stop;
    }
    
    public void setPoiLogo(String poiLogo)
    {
        this.poiLogo = poiLogo;
    }
    
    public String getPoiLogo()
    {
        return this.poiLogo;
    }
    
    public void setBrandLogo(String brandLogo)
    {
        this.brandLogo = brandLogo;
    }
    
    public String getBrandLogo()
    {
        return this.brandLogo;
    }
    
    public void setCategoryLogo(String categoryLogo)
    {
        this.categoryLogo = categoryLogo;
    }
    
    public String getCategoryLogo()
    {
        return this.categoryLogo;
    }
    
}
