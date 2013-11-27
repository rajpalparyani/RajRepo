/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TxNodeLocationSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import com.telenav.data.serializable.ILocationSerializable;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.radio.TnCellInfo;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
class TxNodeLocationSerializable implements ILocationSerializable
{
    static final byte TYPE_POSITION  = 0;
    static final byte TYPE_CELLSITE  = 1;
    static final byte TYPE_FAKE      = 2;
    
    public TnCellInfo createTnCellInfo(byte[] data)
    {
        if (data == null)
            return null;
        
        Node node = new Node(data, 0);
        
        TnCellInfo cellInfo = new TnCellInfo();
        cellInfo.networkRadioMode = (int)node.getValueAt(0);
        
        cellInfo.baseStationId = node.getStringAt(0);
        cellInfo.cellId = node.getStringAt(1);
        cellInfo.countryCode = node.getStringAt(2);
        cellInfo.locationAreaCode = node.getStringAt(3);
        cellInfo.networkCode = node.getStringAt(4);
        cellInfo.networkOperatorName = node.getStringAt(5);
        cellInfo.networkType = node.getStringAt(6);
        
        return cellInfo;
    }

    public TnLocation createTnLocation(byte[] data)
    {
        if (data == null)
            return null;
        
        Node node = new Node(data, 0);
        
        int type = (int)node.getValueAt(5);
        TnLocation location = new TnLocation(getProvider(type));
        location.setTime(node.getValueAt(0));
        location.setLatitude((int)node.getValueAt(1));
        location.setLongitude((int)node.getValueAt(2));
        location.setSpeed((int)node.getValueAt(3));
        location.setHeading((int)node.getValueAt(4));
        location.setAccuracy((int)node.getValueAt(6));
        if (node.getValuesSize() > 7)
            location.setLocalTimeStamp(node.getValueAt(7));
        
        location.setValid(true);
        return location;
    }

    public byte[] toBytes(TnLocation location)
    {
        if(location == null)
            return null;
        
        Node node = new Node();
        
        node.addValue(location.getTime());
        node.addValue(location.getLatitude());
        node.addValue(location.getLongitude());
        node.addValue(location.getSpeed());
        node.addValue(location.getHeading());
        node.addValue(getFixType(location));
        node.addValue(location.getAccuracy());
        node.addValue(location.getLocalTimeStamp());
        
        return node.toBinary();
    }

    public byte[] toBytes(TnCellInfo info)
    {
        if(info == null)
            return null;
        
        Node node = new Node();
        node.addValue(info.networkRadioMode);
        node.addString(info.baseStationId);
        node.addString(info.cellId);
        node.addString(info.countryCode);
        node.addString(info.locationAreaCode);
        node.addString(info.networkCode);
        node.addString(info.networkOperatorName);
        node.addString(info.networkType);
        
        return node.toBinary();
    }
    
    String getProvider(int type)
    {
        switch(type)
        {
            case TYPE_POSITION:
            {
                return TnLocationManager.GPS_179_PROVIDER;
            }
            case TYPE_FAKE:
            {
                return TnLocationManager.ALONGROUTE_PROVIDER;
            }
            case TYPE_CELLSITE:
            {
                return TnLocationManager.NETWORK_PROVIDER;
            }
        }
        return TnLocationManager.GPS_179_PROVIDER;
    }
    
    int getFixType(TnLocation location)
    {
        String provider = location.getProvider();
        if (provider == null || provider.trim().length() <= 0)
            return TYPE_POSITION;

        if (provider.trim().equals(TnLocationManager.GPS_179_PROVIDER))
        {
            return TYPE_POSITION;
        }
        else if (provider.trim().equals(TnLocationManager.ALONGROUTE_PROVIDER) || provider.trim().equals(TnLocationManager.MVIEWER_PROVIDER))
        {
            return TYPE_FAKE;
        }
        else if (provider.trim().equals(TnLocationManager.NETWORK_PROVIDER) || provider.trim().equals(TnLocationManager.TN_NETWORK_PROVIDER))
        {
            return TYPE_CELLSITE;
        }

        return TYPE_POSITION;
    }

}
