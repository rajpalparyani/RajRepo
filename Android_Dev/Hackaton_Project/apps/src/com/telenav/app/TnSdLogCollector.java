/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TnSdLogUploader.java
 *
 */
package com.telenav.app;

/**
 *@author yning
 *@date 2012-7-2
 */
public abstract class TnSdLogCollector
{
    static TnSdLogCollector instance;
    private static int initCount;
    
    public static final TnSdLogCollector getInstance()
    {
        return instance;
    }
    
    public static void init(TnSdLogCollector uploader)
    {
        if(initCount >= 1)
            return;
        
        instance = uploader;
        initCount++;
    }
    
    public abstract byte[] collectClientLogger();
}
