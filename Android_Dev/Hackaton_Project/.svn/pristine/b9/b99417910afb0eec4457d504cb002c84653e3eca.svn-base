/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidBacklightManager.java
 *
 */
package com.telenav.backlight.android;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

import com.telenav.backlight.TnBacklightManager;
import com.telenav.logger.Logger;

/**
 * Provides access to the device's backlight at Android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public class AndroidBacklightManager extends TnBacklightManager
{
    protected PowerManager.WakeLock wakeLock;
    protected KeyguardManager.KeyguardLock keyguardLock;
    
    protected Context context;
    
    /**
     * Construct the backlight manager at android platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
     * <br />
     * Also need grant below class's permission when invoke keyguard:
     * <br />
     * KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
     * <br />
     * 
     * @param context
     */
    public AndroidBacklightManager(Context context)
    {
        this.context = context;
    }
    
    /**
     * Check if the backlight is on.
     * 
     * @return boolean
     * 
     * @exception IllegalStateException when the api doesn't support this function.
     */
    public boolean isOn()
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Integer.parseInt(Build.VERSION.SDK) >= 7)
        {
            return AndroidBacklightUtil.isOn(pm);
        }
        
        throw new IllegalStateException("current api doesn't support this function.");
    }

    /**
     * Turn off the backlight.
     * <br />
     * At android platform, It may turn off shortly after you release it, or it may not if there are other wake locks held. 
     */
    public void turnOff()
    {
        try
        {
            if (wakeLock != null && wakeLock.isHeld())
            {
                wakeLock.release();
                wakeLock = null;
            }
        }
        catch(Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    /**
     * The delegate method of {@link #turnOn(long)}, this method will be override by children.
     * 
     * @param timeout
     */
    protected void turnOnDelegate(final long timeout)
    {
        try
        {
            //if screen is off and wakelock is held, we need to release it before acquiring it again.
            if(!isOn())
                turnOff();
            
            if (wakeLock == null)
            {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "TeleNavApp-backlight");
            }
            
            wakeLock.acquire(timeout);
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }
    
    /**
     * Allows to disable / enable the keyguard.
     * 
     * @param isOn true means enable the keyguard, false otherwise.
     */
    public void enableKeyguard(final boolean isOn)
    {
        //This method maybe will block the main thread, so we use a new temp thread to invoke.
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                if (keyguardLock == null)
                {
                    KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

                    keyguardLock = km.newKeyguardLock("TeleNavApp-keyguard");
                }
                
                if (isOn)
                {
                    keyguardLock.reenableKeyguard();
                }
                else
                {
                    keyguardLock.disableKeyguard();
                }
            }
        });
       
        t.start();
    }
}
