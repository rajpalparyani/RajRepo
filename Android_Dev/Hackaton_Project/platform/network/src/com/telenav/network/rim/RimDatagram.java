/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimDatagram.java
 *
 */
package com.telenav.network.rim;

import net.rim.device.api.io.DatagramBase;

import com.telenav.network.TnDatagram;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimDatagram implements TnDatagram
{
    DatagramBase datagramPacket;
    
    RimDatagram(byte[] buffer, int len)
    {
        datagramPacket = new DatagramBase(buffer, 0, len);
    }
    
    public byte[] getData()
    {
        return datagramPacket.getData();
    }

    public int getLength()
    {
        return datagramPacket.getLength();
    }

    public int getOffset()
    {
        return datagramPacket.getOffset();
    }

    public void setData(byte[] buffer, int offset, int len)
    {
        datagramPacket.setData(buffer, offset, len);
    }

    public void setLength(int len)
    {
        datagramPacket.setLength(len);
    }

}
