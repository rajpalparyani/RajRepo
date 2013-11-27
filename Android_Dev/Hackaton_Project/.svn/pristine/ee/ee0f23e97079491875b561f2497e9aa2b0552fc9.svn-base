/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimDatagramConnection.java
 *
 */
package com.telenav.network.rim;

import java.io.IOException;

import javax.microedition.io.Connector;

import net.rim.device.api.io.DatagramConnectionBase;

import com.telenav.network.TnDatagram;
import com.telenav.network.TnDatagramConnection;
import com.telenav.network.TnNetworkManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimDatagramConnection implements TnDatagramConnection
{
    private DatagramConnectionBase datagramConnection;
    
    public RimDatagramConnection(String address, int mode, boolean timeout) throws IOException
    {
        int nativeMode = Connector.READ;
        switch(mode)
        {
            case TnNetworkManager.READ:
                nativeMode = Connector.READ;
                break;
            case TnNetworkManager.WRITE:
                nativeMode = Connector.WRITE;
                break;
            case TnNetworkManager.READ_WRITE:
                nativeMode = Connector.READ_WRITE;
                break;
        }
        
        datagramConnection = (DatagramConnectionBase) Connector.open(address, nativeMode, timeout);
        if(timeout)
        {
            datagramConnection.setTimeout(30 * 1000);
        }
    }

    public String getLocalAddress() throws IOException
    {
        return this.datagramConnection.getLocalAddress();
    }

    public int getLocalPort() throws IOException
    {
        return this.datagramConnection.getLocalPort();
    }

    public TnDatagram newDatagram(byte[] buf, int size) throws IOException
    {
        return new RimDatagram(buf, size);
    }

    public TnDatagram newDatagram(int size) throws IOException
    {
        return new RimDatagram(new byte[size], size);
    }

    public void receive(TnDatagram pack) throws IOException
    {
        this.datagramConnection.receive(((RimDatagram)pack).datagramPacket);
    }

    public void send(TnDatagram pack) throws IOException
    {
        this.datagramConnection.send(((RimDatagram)pack).datagramPacket);
    }

    public void close() throws IOException
    {
        this.datagramConnection.close();
    }
}
