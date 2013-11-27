/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidLocationUtil.java
 *
 */
package com.telenav.searchwidget.gps.android;



import android.location.Location;
import android.location.LocationManager;

import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.searchwidget.data.Node;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
class AndroidLocationUtil
{
    static final byte TYPE_POSITION  = 0;
    static final byte TYPE_CELLSITE  = 1;
    static final byte TYPE_FAKE      = 2;
    
    public static TnLocation convert(Location location)
    {
        if(location == null)
            return null;
        
        TnLocation tnLocation = new TnLocation(convertProvider(location.getProvider()));
        tnLocation.setAccuracy((int)location.getAccuracy() * 7358 >> 13);
        tnLocation.setAltitude((int)location.getAltitude());
        tnLocation.setHeading((int)location.getBearing());
        tnLocation.setLatitude((int)(location.getLatitude() * 100000));
        tnLocation.setLongitude((int)(location.getLongitude() * 100000));
        tnLocation.setSpeed((int)location.getSpeed()* 9);
        tnLocation.setTime(location.getTime()/10);
        tnLocation.setValid(true);
        
        return tnLocation;
    }    
    
    public static TnLocation fromBytes(byte[] data)
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
    
    public static byte[] toBytes(TnLocation location)
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

    private static String convertProvider(String nativeProvider)
    {
        if(nativeProvider == null)
            return null;
        
        if(LocationManager.GPS_PROVIDER.equals(nativeProvider))
        {
            return TnLocationManager.GPS_179_PROVIDER;
        }
        else if(LocationManager.NETWORK_PROVIDER.equals(nativeProvider))
        {
            return TnLocationManager.NETWORK_PROVIDER;
        }
        
        return null;
    }
    
    private static String getProvider(int type)
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

    private static int getFixType(TnLocation location)
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
