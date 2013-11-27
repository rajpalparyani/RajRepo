/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * PluginAddress.java
 *
 */
package com.telenav.sdk.plugin;

import java.io.Serializable;

/**
 *@author yning
 *@date 2012-5-4
 */
public class PluginAddress implements Serializable
{
    private static final long serialVersionUID = -1471986461594554802L;
    private String addressLine;
    private String country;
    private boolean useOneBox = false;
    
    public void setAddressLine(String addressLine)
    {
        this.addressLine = addressLine;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getAddressLine()
    {
        return this.addressLine;
    }
    
    public String getCountry()
    {
        return this.country;
    }
    
    public void setUseOneBox(boolean useOneBox)
    {
        this.useOneBox = useOneBox;
    }
    
    public boolean useOneBox()
    {
        return this.useOneBox;
    }
}
