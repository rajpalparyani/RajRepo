/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimBacklightManager.java
 *
 */
package com.telenav.backlight.rim;

import net.rim.device.api.system.Backlight;

import com.telenav.backlight.TnBacklightManager;
import com.telenav.logger.Logger;

/**
 * Provides access to the device's backlight at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public class RimBacklightManager extends TnBacklightManager
{
    private final static int BACKLIGHT_LOWEST_BRIGHTNESS    = 45;
    
    /**
     * Construct the backlight manager at rim platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_DEVICE_SETTINGS;
     * <br />
     * 
     * @param context
     */
    public RimBacklightManager()
    {
        
    }
    
    public void enableKeyguard(boolean isOn)
    {
        // TODO Auto-generated method stub

    }

    /**
     * Check if the backlight is on.
     * 
     * @return boolean
     * 
     */
    public boolean isOn()
    {
        return Backlight.isEnabled();
    }

    /**
     * Turn off the backlight.
     * <br />
     */
    public void turnOff()
    {
        Backlight.enable(false);
    }

    /**
     * The delegate method of {@link #turnOn(long)}, this method will be override by children.
     * 
     * @param timeout
     */
    protected void turnOnDelegate(long timeout)
    {
        try
        {
            if (Backlight.isBrightnessConfigurable())
            {
                int brightness = Backlight.getBrightness();
                Backlight.setBrightness(brightness < BACKLIGHT_LOWEST_BRIGHTNESS ? BACKLIGHT_LOWEST_BRIGHTNESS : brightness);
            }

            Backlight.enable(true);
            Backlight.setTimeout((int) (timeout / 1000));
        }
        catch (Exception ex)
        {
            Logger.log(this.getClass().getName(), ex);
        }
    }

}
