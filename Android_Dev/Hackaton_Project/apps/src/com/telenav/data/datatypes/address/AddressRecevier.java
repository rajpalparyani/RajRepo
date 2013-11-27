/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AddressRecevier.java
 *
 */
package com.telenav.data.datatypes.address;

import java.util.Vector;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-1-24
 */
public class AddressRecevier
{

    public static final int TYPE_ADDRESS_RECEIVER = 145;
    
    protected Vector receivers;

    public Vector getReceivers()
    {
        return receivers;
    }

    public void setReceivers(Vector receivers)
    {
        this.receivers = receivers;
    }
    

}
