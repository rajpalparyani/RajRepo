/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimHttpConnection.java
 *
 */
package com.telenav.network.rim;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimHttpConnection implements TnHttpConnection
{
    private HttpConnection httpConnection;
    
    public RimHttpConnection(String address, int mode, boolean timeout) throws IOException
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
        
        httpConnection = (HttpConnection) Connector.open(address, nativeMode, timeout);
    }
    
    public long getDate() throws IOException
    {
        return httpConnection.getDate();
    }

    public long getExpiration() throws IOException
    {
        return httpConnection.getExpiration();
    }

    public String getFile()
    {
        return httpConnection.getFile();
    }

    public String getHeaderField(int n) throws IOException
    {
        return httpConnection.getHeaderField(n);
    }

    public String getHeaderField(String name) throws IOException
    {
        return httpConnection.getHeaderField(name);
    }

    public long getHeaderFieldDate(String name, long def) throws IOException
    {
        return httpConnection.getHeaderFieldDate(name, def);
    }

    public int getHeaderFieldInt(String name, int def) throws IOException
    {
        return httpConnection.getHeaderFieldInt(name, def);
    }

    public String getHeaderFieldKey(int n) throws IOException
    {
        return httpConnection.getHeaderFieldKey(n);
    }

    public String getHost()
    {
        return httpConnection.getHost();
    }

    public long getLastModified() throws IOException
    {
        return httpConnection.getLastModified();
    }

    public int getPort()
    {
        return httpConnection.getPort();
    }

    public String getProtocol()
    {
        return httpConnection.getProtocol();
    }

    public String getQuery()
    {
        return httpConnection.getQuery();
    }

    public String getRef()
    {
        return httpConnection.getRef();
    }

    public String getRequestMethod()
    {
        return httpConnection.getRequestMethod();
    }

    public String getRequestProperty(String key)
    {
        return httpConnection.getRequestProperty(key);
    }

    public int getResponseCode() throws IOException
    {
        return httpConnection.getResponseCode();
    }

    public String getResponseMessage() throws IOException
    {
        return httpConnection.getResponseMessage();
    }

    public String getURL()
    {
        return httpConnection.getURL();
    }

    public void setRequestMethod(String method) throws IOException
    {
        httpConnection.setRequestMethod(method);
    }

    public void setRequestProperty(String key, String value) throws IOException
    {
        httpConnection.setRequestProperty(key, value);
    }

    public String getEncoding()
    {
        return httpConnection.getEncoding();
    }

    public long getLength()
    {
        return httpConnection.getLength();
    }

    public String getType()
    {
        return httpConnection.getType();
    }

    public InputStream openInputStream() throws IOException
    {
        return httpConnection.openInputStream();
    }

    public void close() throws IOException
    {
        httpConnection.close();
    }

    public OutputStream openOutputStream() throws IOException
    {
        return httpConnection.openOutputStream();
    }

}
