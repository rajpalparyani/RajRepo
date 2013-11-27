/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seNetworkManager.java
 *
 */
package com.telenav.network.j2se;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

import com.telenav.network.TnConnection;
import com.telenav.network.TnConnectionNotFoundException;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnProxy;

/**
 * Factory class for creating new Connection objects at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
public class J2seNetworkManager extends TnNetworkManager
{
    /**
     * construct a new network manager at j2se platform.
     */
    public J2seNetworkManager()
    {
    }

    public TnConnection openConnection(String name) throws IOException
    {
        return openConnection(name, READ_WRITE);
    }

    public TnConnection openConnection(String name, int mode) throws IOException
    {
        return openConnection(name, mode, false);
    }

    public TnConnection openConnection(String name, int mode, boolean timeout) throws IOException
    {
        if (name == null || name.trim().length() == 0)
        {
            throw new IllegalArgumentException("The url is empty.");
        }

        if (name.startsWith("socket://"))
        {
            try
            {
                int lastIndex = name.lastIndexOf(":");
                String dstName = name.substring("socket://".length(), lastIndex);
                int dstPort = Integer.parseInt(name.substring(lastIndex + 1).trim());
                InetSocketAddress address = new InetSocketAddress(dstName, dstPort);

                return new J2seSocketConnection(address, timeout);
            }
            catch (Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
        else if (name.startsWith("datagram://"))
        {
            try
            {
                int lastIndex = name.lastIndexOf(":");
                String dstName = name.substring("datagram://".length(), lastIndex);
                int dstPort = Integer.parseInt(name.substring(lastIndex + 1).trim());
                InetSocketAddress address = new InetSocketAddress(dstName, dstPort);

                return new J2seDatagramConnection(address, timeout);
            }
            catch (Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
        else
        {
            try
            {
                name = name.replaceAll(" ", "%20");

                URL url = new URL(name);
                URLConnection urlConnection = url.openConnection();
                if (urlConnection instanceof HttpURLConnection)
                {
                    HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
                    if (mode == READ)
                    {
                        httpUrlConnection.setDoInput(true);
                    }
                    else if (mode == WRITE)
                    {
                        httpUrlConnection.setDoOutput(true);
                    }
                    else
                    {
                        httpUrlConnection.setDoInput(true);
                        httpUrlConnection.setDoOutput(true);
                    }

                    if (timeout)
                    {
                        httpUrlConnection.setConnectTimeout(30 * 1000);
                        httpUrlConnection.setReadTimeout(30 * 1000);
                    }

                    return new J2seHttpConnection(httpUrlConnection);
                }
                else
                {
                    throw new IOException("Currently don't support this protocol.");
                }
            }
            catch (Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
    }

    public TnConnection openConnection(String name, int mode, boolean timeout, TnProxy proxy) throws IOException
    {
        return null;
    }

}
