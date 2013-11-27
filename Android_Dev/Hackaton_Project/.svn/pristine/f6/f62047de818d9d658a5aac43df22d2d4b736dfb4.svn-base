/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IApplication.java
 *
 */
package com.telenav.app;

import android.graphics.drawable.Drawable;

import com.telenav.tnui.core.AbstractTnComponent;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Sep 17, 2010
 */
public interface IApplication
{
    /**
     * exit the system.
     * 
     * @param params
     * @param exitMsg 
     */
    public void exit(Object params, String exitMsg);
    
    /**
     * move the system into background.
     * 
     * @param params
     */
    public void jumpToBackground(Object params);
    
    /**
     * preload app native feature.
     * 
     * @param color
     */
    public Object callAppNativeFeature(String feature, Object[] params, INativeAppCallBack nativeAppCallback);
    
    public Object getDataFromNativeApp(String key);
    
    public void setRequestedOrientation(int orientation);
    
    public int getRequestedOrientation();
    
    /**
     * Close the virtual keyboard
     * 
     * @param component
     */
    public void closeVirtualKeyBoard(AbstractTnComponent component);
    
    public boolean isES2Supported();
    
    public void setBackgroundDrawable(Drawable drawable);

    public void setBackgroundDrawableResource(int resid);
    
    public void setBrightness(float brightness);
}
