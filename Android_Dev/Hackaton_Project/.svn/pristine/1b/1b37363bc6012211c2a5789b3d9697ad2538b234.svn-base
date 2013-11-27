/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAddressSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.SentAddress;
import com.telenav.data.datatypes.address.Stop;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public interface IAddressSerializable
{
    public Address createAddress(byte[] data);
    
    public byte[] toBytes(Address address);
    
    public FavoriteCatalog createFavoriteCatalog(byte[] data);
    
    public byte[] toBytes(FavoriteCatalog catalog);
    
    public Stop createStop(byte[] data);
    
    public byte[] toBytes(Stop stop);
    
    public SentAddress createSentAddress(byte[] data);
    
    public byte[] toBytes(SentAddress sentAddress);
    
    public final static int MOCK_POI_ID_TOP = -10;
}
