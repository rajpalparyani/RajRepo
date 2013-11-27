/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnBacklightManager.java
 *
 */
package com.telenav.backlight;

/**
 * Provides access to the device's backlight.
 * <br />
 * <br />
 * <b>Device battery life will be significantly affected by the use of this API.</b>
 * <br />
 * <br />
 * The Max timeout of backlight is 255 seconds.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public abstract class TnBacklightManager
{
    private static TnBacklightManager backlightManager;
    private static int initCount;
    
    /**
     * Retrieve the instance of backlight manager.
     * 
     * @return {@link TnBacklightManager}
     */
    public static TnBacklightManager getInstance()
    {
        return backlightManager;
    }
    
    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param backlightMngr This manager is native manager of platforms. Such as {@link AndroidBacklightManager} etc.
     */
    public synchronized static void init(TnBacklightManager backlightMngr)
    {
        if(initCount >= 1)
            return;
        
        backlightManager = backlightMngr;
        initCount++;
    }
    
    /**
     * Turns the backlight on, and will turn off automatically after the timeout time.
     * 
     * @param timeout Turn off the backlight after the give timeout in milliseconds. Larger than 255 seconds are reduced to 255 seconds.
     */
    public final void turnOn(long timeout)
    {
        if(timeout > 255 * 1000)
        {
            timeout = 255 * 1000;
        }
        else if(timeout <= 0)
        {
            return;
        }
        
        turnOnDelegate(timeout);
    }
    
    /**
     * The delegate method of {@link #turnOn(long)}, this method will be override by children.
     * 
     * @param timeout will turn off after the timeout.
     */
    protected abstract void turnOnDelegate(long timeout);
    
    /**
     * Turn off the backlight.
     * <br />
     * At android platform, It may turn off shortly after you release it, or it may not if there are other wake locks held. 
     */
    public abstract void turnOff();
    
    /**
     * Check if the backlight is on.
     * 
     * @return boolean
     */
    public abstract boolean isOn();
    
    /**
     * Allows to disable / enable the keyguard.
     * 
     * @param isOn true means enable the keyguard, false otherwise.
     */
    public abstract void enableKeyguard(boolean isOn);
}
