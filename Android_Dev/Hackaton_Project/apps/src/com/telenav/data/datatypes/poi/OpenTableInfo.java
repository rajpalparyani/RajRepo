/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * OpenTableInfo.java
 *
 */
package com.telenav.data.datatypes.poi;

//import com.telenav.datatypes.Node;

/**
 *@author bduan
 *@date 2010-11-11
 */
public class OpenTableInfo
{
    protected String partner;
    protected String partnerPoiId;
    protected boolean isReservable;
    
    public String getPartner()
    {
        return partner;
    }
    
    public void setPartner(String partner)
    {
        this.partner = partner;
    }

    public String getPartnerPoiId()
    {
        return partnerPoiId;
    }
    
    public void setPartnerPoiId(String partnerPoiId)
    {
        this.partnerPoiId = partnerPoiId;
    }

    public boolean getIsReservable()
    {
        return isReservable;
    }
    
    public void setIsReservable(boolean isReservable)
    {
        this.isReservable = isReservable;
    }
}
