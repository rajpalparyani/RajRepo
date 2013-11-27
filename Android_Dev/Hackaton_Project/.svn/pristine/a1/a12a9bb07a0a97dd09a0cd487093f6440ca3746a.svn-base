/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimNetworkManager.java
 *
 */
package com.telenav.network.rim;

import java.io.IOException;

import com.telenav.network.TnConnection;
import com.telenav.network.TnConnectionNotFoundException;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnProxy;

/**
 * Factory class for creating new Connection objects at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public class RimNetworkManager extends TnNetworkManager
{
    /**
     * construct a new network manager at rim platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_INTERNET;#PERMISSION_INTERNAL_CONNECTIONS;#PERMISSION_WIFI;
     * <br />
     * 
     */
    public RimNetworkManager()
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
                return new RimSocketConnection(name, mode, timeout);
            }
            catch(Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
        else if(name.startsWith("datagram://"))
        {
            try
            {
                return new RimDatagramConnection(name, mode, timeout);
            }
            catch(Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
        else if(name.startsWith("http://"))
        {
            try
            {

                return new RimHttpConnection(name, mode, timeout);
            }
            catch(Exception e)
            {
                throw new TnConnectionNotFoundException(e);
            }
        }
        else
        {
            throw new IOException("Currently don't support this protocol.");
        }
    }

    public TnConnection openConnection(String name, int mode, boolean timeout, TnProxy proxy) throws IOException
    {
        return null;
    }

}
