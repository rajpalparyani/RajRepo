/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SupplimentInfo.java
 *
 */
package com.telenav.data.datatypes.poi;

//import com.telenav.datatypes.Node;

/**
 *@author bduan
 *@date 2010-11-11
 */
public class SupplimentInfo
{
    protected String price;
    protected String supportInfo;
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getSupportInfo()
    {
        return supportInfo;
    }
    
    public void setSupportInfo(String supportInfo)
    {
        this.supportInfo = supportInfo;
    }
}
