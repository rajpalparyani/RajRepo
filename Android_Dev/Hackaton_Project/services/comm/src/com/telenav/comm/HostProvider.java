/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * HostProvider.java
 *
 */
package com.telenav.comm;


/**
 * This class this provide the Host object.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 11, 2010
 */
public class HostProvider
{
    /**
     * create a host.
     * 
     * @param protocol protocol of the network connection.
     * @param hostName host of the network server.
     * @param port port of the network server.
     * @return a host.
     */
    public Host createHost(String protocol, String hostName, int port)
    {
        Host host = createHost();
        host.protocol = protocol;
        host.host = hostName;
        host.port = port;
        
        return host;
    }
    
    /**
     * create a host.
     * 
     * @param protocol protocol of the network connection.
     * @param hostName host of the network server.
     * @param port port of the network server.
     * @param file file of the network connection, including scheme, parameters etc.
     * @return a host.
     */
    public Host createHost(String protocol, String hostName, int port, String file)
    {
        Host host = createHost();
        host.protocol = protocol;
        host.host = hostName;
        host.port = port;
        host.file = file;
        
        return host;
    }
    
    /**
     * create a host.
     * 
     * @param action the request action of server.
     * 
     * @return a host.
     */
    public Host createHost(String action)
    {
        return null;
    }
    
    protected Host createHost()
    {
        return new Host();
    }
    
    /**
     * Retrieve the string of url.
     * 
     * @param host the host object.
     * @return the string of url.
     */
    public String getUrl(Host host)
    {
        return host.getUrl();
    }
}
