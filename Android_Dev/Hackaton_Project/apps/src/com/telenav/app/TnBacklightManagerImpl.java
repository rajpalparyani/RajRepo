/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnBacklightManagerImpl.java
 *
 */
package com.telenav.app;

import android.content.Context;
import android.provider.Settings;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.backlight.TnBacklightManager;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;

/**
 *@author yning
 *@date 2011-1-10
 */
public class TnBacklightManagerImpl implements INotifierListener, IApplicationListener
{
    private static class InnerTnBacklightManagerImpl
    {
        private static TnBacklightManagerImpl instance = new TnBacklightManagerImpl();
    }

    protected final static int MAX_TIMEOUT = 45 * 1000; 
    
    protected long notifyInterval = 10 * 1000;
    
    protected long lastNotifyTimestamp = -1;
    
    protected long lastEnableBacklightTime = -1;

    protected long lastEnableDefaultBrightTime = -1;
    
    protected boolean isBacklightOn = false;
    
    protected boolean isNeedDecreaseLater = false;
    
    protected boolean isSavingMode = false;
    
    protected int duration_sec = -1;
    
    
    protected TnBacklightManagerImpl()
    {
        TeleNavDelegate.getInstance().registerApplicationListener(this);
    }
    
    public static TnBacklightManagerImpl getInstance()
    {
        return InnerTnBacklightManagerImpl.instance;
    }
    
    /**
     * add to notifier.
     */
    public void start()
    {
        Notifier.getInstance().addListener(this);
    }
    
    /**
     * remove from notifier.
     */
    public void stop()
    {
        setDefaultBrightness(0);
        Notifier.getInstance().removeListener(this);
    }
    
    public void setIsSavingMode(boolean isSavingMode)
    {
        this.isSavingMode = isSavingMode;
    }
    
    public void setDefaultBrightness()
    {
        setDefaultBrightness(0);
    }
    
    public void setDefaultBrightness(int duration_sec)
    {
        TeleNavDelegate.getInstance().setBrightness(0);
        if (duration_sec > 0 && duration_sec < getDefaultTimeOut())
        {
            lastEnableDefaultBrightTime = System.currentTimeMillis();
            this.isNeedDecreaseLater = true;
            this.duration_sec = duration_sec;
        }
        else
        {
            this.isNeedDecreaseLater = false;
        }
    }
    
    public void decreaseBrightness()
    {
        //System.out.println("DB-Test --> backlight decrease : " + isSavingMode);
        if (isSavingMode)
        {
            TeleNavDelegate.getInstance().setBrightness(10);
        }
    }
    
    /**
     * turn off backlight immediately
     */
    public void turnOff()
    {
        isBacklightOn = false;
        TnBacklightManager.getInstance().turnOff();
    }
    
    
    /**
     * force to turn on back light for 255s.
     * If the mode is not "always on", it will enable key lock after the interval.
     */
    public void enable()
    {
        //System.out.println("DB-Test --> backlight enable...");
        
        isBacklightOn = true;
        
        TnBacklightManager.getInstance().turnOn(getDefaultTimeOut());
        
        /*if (NavSdkNavEngine.getInstance() != null && NavSdkNavEngine.getInstance().isRunning())
        {
            TnBacklightManager.getInstance().enableKeyguard(false);
        }*/

    }
    
    private int getDefaultTimeOut()
    {
        Context mContext = (Context) AndroidPersistentContext.getInstance().getContext();
        int timeout = 0;
        try
        {
            timeout = android.provider.Settings.System
                    .getInt(mContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (timeout == 0)
            timeout = MAX_TIMEOUT;
        return timeout;
    }
    
    /**
     * see {@link INotifierListener}
     */
    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    /**
     * see {@link INotifierListener}
     */
    public long getNotifyInterval()
    {
        return notifyInterval;
    }

    /**
     * see {@link INotifierListener}
     */
    public void notify(long timestamp)
    {
        if (isBacklightOn)
        {
            if (timestamp - lastEnableBacklightTime > duration_sec * 1000)
            {
                enable();
            }
        }
        
        if (isNeedDecreaseLater)
        {
            if (timestamp - lastEnableDefaultBrightTime > duration_sec * 1000)
            {
                isNeedDecreaseLater = false;
                decreaseBrightness();
            }
        }
    }

    /**
     * see {@link INotifierListener}
     */
    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestamp = timestamp;
    }
    
    /**
     * Set notify interval.
     * @param interval
     */
    public void setBacklightInterval(long interval)
    {
        this.notifyInterval = interval;
    }

    /**
     * see {@link IApplicationListener}
     */
    public void appActivated(String[] params)
    {
        enable();
    }

    /**
     * see {@link IApplicationListener}
     */
    public void appDeactivated(String[] params)
    {
        stop();
        
        /*if (NavSdkNavEngine.getInstance() == null || !NavSdkNavEngine.getInstance().isRunning())
        {
            TnBacklightManager.getInstance().enableKeyguard(true);
        }*/
    }
}
