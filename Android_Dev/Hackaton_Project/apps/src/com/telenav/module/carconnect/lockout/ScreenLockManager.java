/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ScreenLockManager.java
 *
 */
package com.telenav.module.carconnect.lockout;

import com.telenav.logger.Logger;
import com.telenav.mvc.AbstractController;

/**
 *@author chihlh
 *@date Mar 15, 2012
 */
public class ScreenLockManager
{
    private final static ScreenLockManager sInstance = new ScreenLockManager();
    
    public final static ScreenLockManager getInstance()
    {
        return sInstance;
    }
    
    private ScreenLockManager()
    {
    }
    
    private ILockoutScreenUnlockListener unlockListener = null;
    
    public synchronized void lockScreen()
    {
        if (unlockListener == null)
        {
            // only show screen if it is not there
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: LockScreenManager do real screen lock");
            LockoutController lockoutController = new LockoutController(AbstractController.getCurrentController());
        
            //Parameter data = new Parameter();
            lockoutController.init();
            unlockListener = (LockoutModel)lockoutController.getModel();
        }
    }
    
    public synchronized void unlockScreen()
    {
        if (unlockListener != null)
        {
            //only release if already has lock screen
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: LockScreenManager do real screen unlock");
            
            unlockListener.unlockScreen();
            unlockListener = null;
        }
    }
}
