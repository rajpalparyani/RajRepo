/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidHttpConnection.java
 *
 */
package com.telenav.network.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.telenav.network.TnHttpConnection;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
class AndroidHttpConnection implements TnHttpConnection
{
    private HttpURLConnection httpUrlConnection;
    
    AndroidHttpConnection(HttpURLConnection httpUrlConnection)
    {
        this.httpUrlConnection = httpUrlConnection;
    }
    
    public long getDate() throws IOException
    {
        return this.httpUrlConnection.getDate();
    }

    public long getExpiration() throws IOException
    {
        return this.httpUrlConnection.getExpiration();
    }

    public String getFile()
    {
        return this.httpUrlConnection.getURL().getFile();
    }

    public String getHeaderField(int n) throws IOException
    {
        return this.httpUrlConnection.getHeaderField(n);
    }

    public String getHeaderField(String name) throws IOException
    {
        return this.httpUrlConnection.getHeaderField(name);
    }

    public long getHeaderFieldDate(String name, long def) throws IOException
    {
        return this.httpUrlConnection.getHeaderFieldDate(name, def);
    }

    public int getHeaderFieldInt(String name, int def) throws IOException
    {
        return this.httpUrlConnection.getHeaderFieldInt(name, def);
    }

    public String getHeaderFieldKey(int n) throws IOException
    {
        return this.httpUrlConnection.getHeaderFieldKey(n);
    }

    public String getHost()
    {
        return this.httpUrlConnection.getURL().getHost();
    }

    public long getLastModified() throws IOException
    {
        return this.httpUrlConnection.getLastModified();
    }

    public int getPort()
    {
        return this.httpUrlConnection.getURL().getPort();
    }

    public String getProtocol()
    {
        return this.httpUrlConnection.getURL().getProtocol();
    }

    public String getQuery()
    {
        return this.httpUrlConnection.getURL().getQuery();
    }

    public String getRef()
    {
        return this.httpUrlConnection.getURL().getRef();
    }

    public String getRequestMethod()
    {
        return this.httpUrlConnection.getRequestMethod();
    }

    public String getRequestProperty(String key)
    {
        return this.httpUrlConnection.getRequestProperty(key);
    }

    public int getResponseCode() throws IOException
    {
        return this.httpUrlConnection.getResponseCode();
    }

    public String getResponseMessage() throws IOException
    {
        return this.httpUrlConnection.getResponseMessage();
    }

    public String getURL()
    {
        return this.httpUrlConnection.getURL().toString();
    }

    public void setRequestMethod(String method) throws IOException
    {
        this.httpUrlConnection.setRequestMethod(method);
    }

    public void setRequestProperty(String key, String value) throws IOException
    {
        this.httpUrlConnection.setRequestProperty(key, value);
    }

    public String getEncoding()
    {
        return this.httpUrlConnection.getContentEncoding();
    }

    public long getLength()
    {
        return this.httpUrlConnection.getContentLength();
    }

    public String getType()
    {
        return this.httpUrlConnection.getContentType();
    }

    public InputStream openInputStream() throws IOException
    {
        return this.httpUrlConnection.getInputStream();
    }

    public void close() throws IOException
    {
        this.httpUrlConnection.disconnect();
    }

    public OutputStream openOutputStream() throws IOException
    {
        return this.httpUrlConnection.getOutputStream();
    }

}
