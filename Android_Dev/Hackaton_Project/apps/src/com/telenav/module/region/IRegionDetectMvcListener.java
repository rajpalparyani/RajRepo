/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * IRegionDetectMvcListener.java
 *
 */
package com.telenav.module.region;

/**
 *@author yning
 *@date 2012-6-7
 */
public interface IRegionDetectMvcListener
{
    public void regionNotDetected();
    
    public void regionAlreadyDetected(String region);
}
