/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seSocketConnection.java
 *
 */
package com.telenav.network.j2se;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.telenav.network.TnSocketConnection;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
class J2seSocketConnection implements TnSocketConnection
{
    private Socket socket;
    private InetSocketAddress address; 
    private boolean timeout;
    
    J2seSocketConnection(InetSocketAddress address, boolean timeout)
    {
        socket = new Socket();
        this.address = address;
        this.timeout = timeout;
    }
    
    public String getAddress() throws IOException
    {
        return this.socket.getInetAddress().getHostName();
    }

    public String getLocalAddress() throws IOException
    {
        return this.socket.getLocalAddress().getHostName();
    }

    public int getLocalPort() throws IOException
    {
        return this.socket.getLocalPort();
    }

    public int getPort() throws IOException
    {
        return this.socket.getPort();
    }

    public int getSocketOption(byte option) throws IllegalArgumentException, IOException
    {
        try
        {
            switch (option)
            {
                case DELAY:
                    return this.socket.getTcpNoDelay() ? 1 : 0;
                case LINGER:
                    return this.socket.getSoLinger();
                case KEEPALIVE:
                    return this.socket.getKeepAlive() ? 1 : 0;
                case RCVBUF:
                    return this.socket.getReceiveBufferSize();
                case SNDBUF:
                    return this.socket.getSendBufferSize();
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
                    this.socket.setTcpNoDelay(value > 0 ? true : false);
                    break;
                case LINGER:
                    this.socket.setSoLinger(value > 0 ? true : false, value);
                    break;
                case KEEPALIVE:
                    this.socket.setKeepAlive(value > 0 ? true : false);
                    break;
                case RCVBUF:
                    this.socket.setReceiveBufferSize(value);
                    break;
                case SNDBUF:
                    this.socket.setSendBufferSize(value);
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
        return this.socket.getInputStream();
    }

    public void close() throws IOException
    {
        this.socket.shutdownInput();
        this.socket.shutdownOutput();
        
        this.socket.close();
    }

    public OutputStream openOutputStream() throws IOException
    {
        if(timeout)
        {
            socket.setSoTimeout(30 * 1000);
            socket.connect(address, 30 * 1000);
        }
        else
        {
            socket.connect(address);
        }
        
        return this.socket.getOutputStream();
    }

}
