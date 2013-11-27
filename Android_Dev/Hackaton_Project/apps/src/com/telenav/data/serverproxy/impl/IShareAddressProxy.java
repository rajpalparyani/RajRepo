/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IShareAddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.data.datatypes.address.Address;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IShareAddressProxy
{
    public String shareAddress(Address address, Vector contacts, String label, String phoneNumber);
}
