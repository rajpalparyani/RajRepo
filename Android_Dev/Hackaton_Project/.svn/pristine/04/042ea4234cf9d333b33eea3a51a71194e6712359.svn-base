/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMetaProvider.java
 *
 */
package com.telenav.dsr;


/**
 * The class who impl this interface is responsible for 
 * provide the Context info which may be used in DSR recognize request.
 * 
 *@author bduan
 *@date 2010-10-26
 */
public interface IMetaInfoProvider
{
    /**
     * get Stop byte[] base on the serialize approach.
     * 
     * @return byte[]
     */
    public byte[] getAnchorStopData();
    
    /**
     * get Madantory node byte[] base on the serialize approach.
     * 
     * @return byte[] madantory data
     */
    public byte[] getMadantoryData();
    
    /**
     * get log data byte[] base on the serialize approach.
     * 
     * @return byte[] log data
     */
    public byte[] getLogData();
    
    /**
     * Get current requestType
     * 
     * @return
     */
    public String getRequestType();
    
    /**
     * get current request session.
     * 
     * @return
     */
    public long getRequestSession();
    
    /**
     * return the record audio format.
     * @return audio format.
     */
    public int getFormat();

}
