/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidDatagram.java
 *
 */
package com.telenav.network.android;

import java.net.DatagramPacket;

import com.telenav.network.TnDatagram;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 13, 2010
 */
class AndroidDatagram implements TnDatagram
{
    DatagramPacket datagramPacket;
    
    AndroidDatagram(byte[] buffer, int len)
    {
        datagramPacket = new DatagramPacket(buffer, len);
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
