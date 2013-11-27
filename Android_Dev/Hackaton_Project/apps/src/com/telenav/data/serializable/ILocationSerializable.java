/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ILocationSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.location.TnLocation;
import com.telenav.radio.TnCellInfo;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public interface ILocationSerializable
{
    public TnLocation createTnLocation(byte[] data);

    public byte[] toBytes(TnLocation location);

    public TnCellInfo createTnCellInfo(byte[] data);

    public byte[] toBytes(TnCellInfo cellInfo);
}
