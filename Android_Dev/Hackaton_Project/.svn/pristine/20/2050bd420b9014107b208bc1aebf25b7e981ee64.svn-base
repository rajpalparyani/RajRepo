/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TxNodeExtraInfoSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import com.telenav.data.datatypes.map.MapDataUpgradeInfo;
import com.telenav.data.serializable.IExtraInfoSerializable;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-2-24
 */
class TxNodeExtraInfoSerializable implements IExtraInfoSerializable
{

    public MapDataUpgradeInfo createMapDataUpgradeInfo(byte[] data)
    {
        if (data == null)
            return null;
        
        Node childNode = new Node(data, 0);
        
        MapDataUpgradeInfo upgradeInfo = new MapDataUpgradeInfo();
        upgradeInfo.setUpgradeMode((int)childNode.getValueAt(0));
        upgradeInfo.setName(childNode.getStringAt(0));
        upgradeInfo.setVersion(childNode.getStringAt(1));
        upgradeInfo.setBuildNumber(childNode.getStringAt(2));
        upgradeInfo.setRegion(childNode.getStringAt(3));
        upgradeInfo.setState(childNode.getStringAt(4));
        upgradeInfo.setUrl(childNode.getStringAt(5));
        upgradeInfo.setSummary(childNode.getStringAt(6));
        upgradeInfo.setMapDataSize(childNode.getStringAt(7));
        
        return upgradeInfo;
    }

    public byte[] toBytes(MapDataUpgradeInfo mapDataUpgradeInfo)
    {
        if(mapDataUpgradeInfo == null)
            return null;
        
        Node node = new Node();
        node.addValue(mapDataUpgradeInfo.getUpgradeMode());
        node.addString(mapDataUpgradeInfo.getName());
        node.addString(mapDataUpgradeInfo.getVersion());
        node.addString(mapDataUpgradeInfo.getBuildNumber());
        node.addString(mapDataUpgradeInfo.getRegion());
        node.addString(mapDataUpgradeInfo.getState());
        node.addString(mapDataUpgradeInfo.getUrl());
        node.addString(mapDataUpgradeInfo.getSummary());
        node.addString(mapDataUpgradeInfo.getMapDataSize());
        
        return node.toBinary();
    }

}
