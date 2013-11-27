/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidXmlNetworkManager.java
 *
 */
package com.telenav.network.android;

import java.io.IOException;

import com.telenav.network.TnConnection;
import com.telenav.network.android.AndroidNetworkManager;

/**
 *@author hchai
 *@date 2011-7-21
 */
public class AndroidXmlNetworkManager extends AndroidNetworkManager
{
    public TnConnection openConnection(String name, int mode, boolean timeout) throws IOException
    {
        AndroidHttpConnection conn = (AndroidHttpConnection) super.openConnection(name, mode, timeout);
        conn.setRequestMethod(AndroidHttpConnection.POST);
        conn.setRequestProperty("content-type", "application/xml");
        return conn;
    }
}
