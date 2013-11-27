/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimSocketConnection.java
 *
 */
package com.telenav.network.rim;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnSocketConnection;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimSocketConnection implements TnSocketConnection
{
    private SocketConnection socketConnection;
    
    public RimSocketConnection(String address, int mode, boolean timeout) throws IOException
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
        
        socketConnection = (SocketConnection) Connector.open(address, nativeMode, timeout);
    }
    
    public String getAddress() throws IOException
    {
        return socketConnection.getAddress();
    }

    public String getLocalAddress() throws IOException
    {
        return socketConnection.getLocalAddress();
    }

    public int getLocalPort() throws IOException
    {
        return socketConnection.getLocalPort();
    }

    public int getPort() throws IOException
    {
        return socketConnection.getPort();
    }

    public int getSocketOption(byte option) throws IllegalArgumentException, IOException
    {
        try
        {
            switch (option)
            {
                case DELAY:
                    return this.socketConnection.getSocketOption(SocketConnection.DELAY);
                case LINGER:
                    return this.socketConnection.getSocketOption(SocketConnection.LINGER);
                case KEEPALIVE:
                    return this.socketConnection.getSocketOption(SocketConnection.KEEPALIVE);
                case RCVBUF:
                    return this.socketConnection.getSocketOption(SocketConnection.RCVBUF);
                case SNDBUF:
                    return this.socketConnection.getSocketOption(SocketConnection.SNDBUF);
                default:
                    throw new IllegalArgumentException("the option is invalid.");
            }
        }
        catch(Exception e)
        {
            throw new IOException(e.getMessage());
        }
    }

    public void setSocketOption(byte option, int value) throws IllegalArgumentException, IOException
    {
        try
        {
            switch (option)
            {
                case DELAY:
                    this.socketConnection.setSocketOption(SocketConnection.DELAY, value);
                    break;
                case LINGER:
                    this.socketConnection.setSocketOption(SocketConnection.LINGER, value);
                    break;
                case KEEPALIVE:
                    this.socketConnection.setSocketOption(SocketConnection.KEEPALIVE, value);
                    break;
                case RCVBUF:
                    this.socketConnection.setSocketOption(SocketConnection.RCVBUF, value);
                    break;
                case SNDBUF:
                    this.socketConnection.setSocketOption(SocketConnection.SNDBUF, value);
                    break;
                default:
                    throw new IllegalArgumentException("the option is invalid.");
            }
        }
        catch(Exception e)
        {
            throw new IOException(e.getMessage());
        }
    }

    public InputStream openInputStream() throws IOException
    {
        return socketConnection.openInputStream();
    }

    public void close() throws IOException
    {
        socketConnection.close();
    }

    public OutputStream openOutputStream() throws IOException
    {
        return socketConnection.openOutputStream();
    }

}
