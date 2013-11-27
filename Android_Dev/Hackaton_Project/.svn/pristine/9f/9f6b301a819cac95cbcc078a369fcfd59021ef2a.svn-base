/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * LockoutModel.java
 *
 */
package com.telenav.module.carconnect.lockout;

import com.telenav.mvc.AbstractCommonModel;

/**
 *@author chihlh
 *@date Mar 15, 2012
 */
class LockoutModel extends AbstractCommonModel implements ILockoutConstants, ILockoutScreenUnlockListener
{

    @Override
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                postModelEvent(EVENT_MODEL_LOCK_SCREEN);
                break;
            }
        }
    }

    @Override
    public void unlockScreen()
    {
        postModelEvent(EVENT_MODEL_UNLOCK_SCREEN);       
    }

}
