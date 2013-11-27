/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * BrowserSessionArgsTest.java
 *
 */
package com.telenav.data.datatypes.browser;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.comm.Host;
import com.telenav.comm.HostProvider;

import junit.framework.TestCase;

/**
 *@author hchai
 *@date 2011-6-21
 */
public class BrowserSessionArgsTest extends TestCase
{
    HostProvider hostProvider;

    @Override
    protected void setUp() throws Exception
    {
        hostProvider = new HostProvider();

        super.setUp();
    }

    public void testGetUrl()
    {
        // case 1, file is start with '/'
        Host h = hostProvider.createHost("http", "qa-unit03.telenav.com", 8080,
            "/resource-cserver/telenav-server-pb");
        BrowserSessionArgs browserSessionArgs = new BrowserSessionArgs(h);
        assertEquals(browserSessionArgs.getUrl(),
            "http://qa-unit03.telenav.com:8080/resource-cserver/telenav-server-pb");

        // case 2, file isn't start with '/'
        hostProvider.createHost("http", "qa-unit03.telenav.com", 8080,
            "resource-cserver/telenav-server-pb");
        browserSessionArgs = new BrowserSessionArgs(h);
        assertEquals(browserSessionArgs.getUrl(),
            "http://qa-unit03.telenav.com:8080/resource-cserver/telenav-server-pb");
    }
}
