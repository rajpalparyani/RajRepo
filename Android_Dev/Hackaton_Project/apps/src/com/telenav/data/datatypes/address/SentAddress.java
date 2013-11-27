/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SentAddress.java
 *
 */
package com.telenav.data.datatypes.address;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-1-24
 */
public class SentAddress
{
    public static final int TYPE_SENT_ADDRESS = 144;

    protected long createdTime;
    
    protected String label;
    
    protected String street;
    
    protected String city;
    
    protected String state;
    
    protected String zip;
    
    protected String country;
    
    protected AddressRecevier receiver;

    public long getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime(long createdTime)
    {
        this.createdTime = createdTime;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public AddressRecevier getReceiver()
    {
        return receiver;
    }

    public void setReceiver(AddressRecevier receiver)
    {
        this.receiver = receiver;
    }
    
}
