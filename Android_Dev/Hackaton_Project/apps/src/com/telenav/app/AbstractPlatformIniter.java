/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractPlatformIniter.java
 *
 */
package com.telenav.app;

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
    
    public abstract void initBacklight();
    
    public abstract void initIo();
    
    public abstract void initNio();
    
    public abstract void initLocation();
    
    public abstract void initMedia();
    
    public abstract void initNetwork();
    
    public abstract void initPersistent();
    
    public abstract void initRadio();
    
    public abstract void initTelephony();
    
    public abstract void initBattery();
    
    public abstract void initContactPrvoider();
    
    public abstract void initLocationProvider();
    
    public abstract void initSdLogCollector();
    
    public abstract void initNavSdk();
    
    public abstract void initSensor();
    
    public void initAudioEncoder()
    {
        
    }
}
