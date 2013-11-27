/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IExtraInfoSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.data.datatypes.map.MapDataUpgradeInfo;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-2-24
 */
public interface IExtraInfoSerializable
{
    public MapDataUpgradeInfo createMapDataUpgradeInfo(byte[] data);

    public byte[] toBytes(MapDataUpgradeInfo mapDataUpgradeInfo);
}
