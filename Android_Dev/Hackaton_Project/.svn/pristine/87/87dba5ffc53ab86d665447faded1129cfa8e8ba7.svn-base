/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IValidateAddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IValidateAddressProxy
{
    //input type
    public static final int TYPE_INPUTTYPE_ANY = 0;
    public static final int TYPE_INPUTTYPE_CHN = 1;
    public static final int TYPE_INPUTTYPE_PY = 2;
    
    public void validateAddress(String firstLine, String lastLine, String searchUid);
    
    public Vector getSimilarAddresses();

    public void validateAddress(String firstLine, String lastLine, String searchUid, Stop city,int inputType);
}
