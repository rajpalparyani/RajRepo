/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestHost.java
 *
 */
package com.telenav.comm;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-16
 */
public class HostTest extends TestCase
{
    public void testGetUrl()
    {
        Host host = new Host();
        host.protocol = Host.HTTP;
        host.host = "www.baidu.com";
        host.port = 80;
        host.file = "s?wd=ss&inputT=552";
        
        assertEquals("http://www.baidu.com:80/s?wd=ss&inputT=552", host.getUrl());
    }
}
