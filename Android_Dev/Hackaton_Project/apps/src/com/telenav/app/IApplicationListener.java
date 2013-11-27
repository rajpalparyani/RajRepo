/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IApplication.java
 *
 */
package com.telenav.app;

/**
 * The interface for the main class at different platform. will provide exit, move to background etc.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 20, 2010
 */
public interface IApplicationListener
{
    /**
     * call it when app is deactivated.
     * 
     * @param params
     * 
     */
    public void appDeactivated(String[] params);

    /**
     * call it when app is activated.
     * 
     * @param params
     */
    public void appActivated(String[] params);
}
