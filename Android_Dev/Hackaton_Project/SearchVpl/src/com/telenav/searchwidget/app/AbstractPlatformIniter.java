/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractPlatformIniter.java
 *
 */
package com.telenav.searchwidget.app;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public abstract class AbstractPlatformIniter
{
    protected static AbstractPlatformIniter instance;
    
    // only used for J2SE to store data
    protected String baseDir;
    
    public static AbstractPlatformIniter getInstance()
    {
        return instance;
    }
    
    public static void init(AbstractPlatformIniter initer)
    {
        instance = initer;
    }
    
    public void setBaseDir(String dir)
    {
    	baseDir = dir;
    }
    
    public String getBaseDir()
    {
    	return baseDir;
    }
    
    public abstract void initUi();
    
    public abstract void initIo();
    
    public abstract void initNio();
    
    public abstract void initLocation();
    
    public abstract void initNetwork();
    
    public abstract void initPersistent();
    
    public abstract void initNetworkConnectivityHandler();
}
