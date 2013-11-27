/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnConnectionNotFoundException.java
 *
 */
package com.telenav.network;

import java.io.IOException;

/**
 * This class is used to signal that a connection target cannot be found.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public class TnConnectionNotFoundException extends IOException
{
    protected Exception e;
    
    /**
     * 
     */
    private static final long serialVersionUID = 10009934234L;

    /**
     * Constructs a ConnectionNotFoundException with the specified detail message. A detail message is a String that
     * describes this particular exception.
     * 
     * @param msg the detail message.
     */
    public TnConnectionNotFoundException(String msg)
    {
        super(msg);
    }
    
    /**
     * Constructs a ConnectionNotFoundException with the specified detail message. A detail message is a String that
     * describes this particular exception.
     * 
     * @param msg the detail message.
     */
    public TnConnectionNotFoundException(Exception e)
    {
        super(e.getMessage());
        
        this.e = e;
    }
}
