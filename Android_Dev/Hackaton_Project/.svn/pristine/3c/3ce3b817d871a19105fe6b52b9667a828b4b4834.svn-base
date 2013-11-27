/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnProxy.java
 *
 */
package com.telenav.network;

/**
 *@author yxyao
 *@date 2011-10-20
 */
public class TnProxy
{
    public static final byte HTTP = 1;
    
    public static final byte SOCKS = 2;
    
    public static final byte REDIRECT = 3;
    
    private final String address;
    
    private final int port;
    
    private final byte type;

    /**
     * @param address
     * @param port
     * @param type
     */
    public TnProxy(String address, int port, byte type)
    {
        super();
        this.address = address;
        this.port = port;
        this.type = type;
    }
    
    /**
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }
    
    /**
     * @return the type
     */
    public byte getType()
    {
        return type;
    }
}
