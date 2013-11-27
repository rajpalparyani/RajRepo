/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CommonDBdata.java
 *
 */
package com.telenav.data.datatypes.address;

/**
 *@author dzhao
 *@date 2011-12-15
 */

public class CommonDBdata
{
    //status definition
    public static final byte UNCHANGED = 0;
    public static final byte ADDED     = 1;
    public static final byte DELETED   = 2;
    public static final byte UPDATED   = 3; 
    
    protected byte status = UNCHANGED;
    protected long id ;
    
    public long getId() 
    {
        return this.id;
    }
    
    public void setId(long id) 
    {
        this.id = id;
    }
    
    public void setStatus(int status) 
    {
        this.status = (byte)status ;
    }
    
    public byte getStatus() 
    {
        return this.status;
    }
}
