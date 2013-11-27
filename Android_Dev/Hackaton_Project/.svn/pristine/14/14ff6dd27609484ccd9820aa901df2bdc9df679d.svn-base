/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seDatagramConnection.java
 *
 */
package com.telenav.network.j2se;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.telenav.network.TnDatagram;
import com.telenav.network.TnDatagramConnection;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
class J2seDatagramConnection implements TnDatagramConnection
{
    private DatagramSocket datagramSocket;
    
    J2seDatagramConnection(SocketAddress address, boolean timeout) throws SocketException
    {
        this.datagramSocket = new DatagramSocket();
        
        this.datagramSocket.connect(address);
        
        if(timeout)
        {
            this.datagramSocket.setSoTimeout(30 * 1000);
        }
    }
    
    public String getLocalAddress() throws IOException
    {
        InetAddress address = this.datagramSocket.getLocalAddress();
        return address == null ? null : address.getHostAddress();
    }

    public int getLocalPort() throws IOException
    {
        return this.datagramSocket.getLocalPort();
    }

    public TnDatagram newDatagram(byte[] buf, int size) throws IOException
    {
        return new J2seDatagram(buf, size);
    }

    public TnDatagram newDatagram(int size) throws IOException
    {
        return new J2seDatagram(new byte[size], size);
    }

    public void receive(TnDatagram pack) throws IOException
    {
        this.datagramSocket.receive(((J2seDatagram)pack).datagramPacket);
    }

    public void send(TnDatagram pack) throws IOException
    {
        this.datagramSocket.send(((J2seDatagram)pack).datagramPacket);
    }

    public void close() throws IOException
    {
        this.datagramSocket.disconnect();
        this.datagramSocket.close();
    }

}
