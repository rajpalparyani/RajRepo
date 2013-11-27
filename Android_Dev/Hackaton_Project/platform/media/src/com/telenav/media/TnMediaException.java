/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMediaException.java
 *
 */
package com.telenav.media;

/**
 * A TnMediaException indicates an unexpected error condition in a method.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 26, 2010
 */
public class TnMediaException extends Exception
{
    /**
     * serial version UID
     */
    private static final long serialVersionUID = -4547257155367636747L;

    /**
     * Constructs a MediaException with the specified detail message. The error message string s can later be retrieved
     * by the Throwable.getMessage() method of class java.lang.Throwable.
     * 
     * @param msg the detail message.
     */
    public TnMediaException(String msg)
    {
        super(msg);
    }
}
