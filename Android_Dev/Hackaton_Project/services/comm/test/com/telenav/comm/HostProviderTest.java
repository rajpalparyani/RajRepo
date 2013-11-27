/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestHostProvider.java
 *
 */
package com.telenav.comm;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-16
 */
public class HostProviderTest extends TestCase
{
    public void testCreateHost()
    {
        String protocol = Host.HTTP;
        String hostName = "www.baidu.com";
        int port = 80;
        String file = "s?wd=ss&inputT=552";
        
        HostProvider hostProvider = new HostProvider();
        assertNull(hostProvider.createHost("action"));
        Host host = hostProvider.createHost(protocol, hostName, port);
        assertEquals("http://www.baidu.com:80", host.getUrl());
        host = hostProvider.createHost(protocol, hostName, port, file);
        assertEquals("http://www.baidu.com:80/s?wd=ss&inputT=552", host.getUrl());
    }
    
    public void testGetUrl()
    {
        String protocol = Host.HTTP;
        String hostName = "www.baidu.com";
        int port = 80;
        String file = "s?wd=ss&inputT=552";
        
        HostProvider hostProvider = new HostProvider();
        Host host = new Host();
        host.protocol = protocol;
        host.host = hostName;
        host.port = port;
        host.file = file;
        assertEquals("http://www.baidu.com:80/s?wd=ss&inputT=552", hostProvider.getUrl(host));
    }
}
