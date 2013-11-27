/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Host.java
 *
 */
package com.telenav.comm;


/**
 * Holds all of the variables needed to describe an network connection to a host. This includes protocol, remote host name, port and
 * scheme.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public class Host
{
    /**
     * http protocol
     */
    public final static String HTTP = "http";
    
    /**
     * https protocol
     */
    public final static String HTTPS = "https";
    
    /**
     * socket protocol
     */
    public final static String SOCKET = "socket";
    
    /**
     * datagram protocol, such as udp etc.
     */
    public final static String DATAGRAM = "datagram";
    
    /**
     * protocol of the network connection.
     */
    public String protocol;
    
    /**
     * host of the network server.
     */
    public String host;

    /**
     * file of the network connection, including scheme, parameters etc.
     */
    public String file;
    
    /**
     * port of the network server.
     */
    public int port;
    
    
    /**
     * means that when connect with socket, the server is a real socket server or a http server such as tomcat etc.
     * For some projects such as TNT, we will use tomcat as the socket server.
     */
    public boolean isStandaloneSocketServer = false;
    
    Host()
    {
        
    }
    
    /**
     * Retrieve the string of url.
     * 
     * @return the url.
     */
    String getUrl()
    {
        String url = this.protocol + "://" + host;
        if(this.port > 0)
        {
            url += ":" + this.port;
        }
        if(this.file != null && this.file.length() > 0)
        {
            url += "/" + this.file;
        }
        
        return url;
    }
}
