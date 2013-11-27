/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Coupon.java
 *
 */
package com.telenav.data.datatypes.poi;

//import com.telenav.datatypes.Node;

/**
 *@author bduan
 *@date 2010-11-11
 */
public class Coupon
{
    protected String description;
    protected String endDate;
    protected byte[] imageData;
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public byte[] getImageData()
    {
        return imageData;
    }
    
    public void setImageData(byte[] imageData)
    {
        this.imageData = imageData;
    }
}
