/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PreloadJob.java
 *
 */
package com.telenav.module.entry;

import com.crashlytics.android.Crashlytics;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.TnBacklightManagerImpl;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.preference.guidetone.GuideToneManager;
import com.telenav.module.region.RegionUtil;
import com.telenav.threadpool.IJob;

/**
 * @author yning
 * @date 2010-11-30
 */
public class PreloadJob implements IJob
{
    protected boolean isRunning;

    protected boolean isCancelled;
    
    protected boolean hasLogin;
    
    public PreloadJob(boolean hasLogin)
    {
        this.hasLogin = hasLogin;
    }

    public void cancel()
    {
        isCancelled = true;
    }

    public void execute(int n)
    {
        isRunning = true;

        try
        {
            TnBacklightManagerImpl.getInstance().enable();
            if (hasLogin)
            {
                if (!DaoManager.getInstance().getAddressDao().isAirPortListInitialized())
                {
                    DaoManager.getInstance().getAddressDao().parseAirports(DaoManager.getInstance().getResourceBarDao().getAirportNode());
                    Logger.log(Logger.INFO, this.getClass().getName(), "==Preload Airport==    done!!!");
                }
                
                DaoManager.getInstance().getAddressDao().load();
                DaoManager.getInstance().getPreferenceDao().load(RegionUtil.getInstance().getCurrentRegion());
            }
            else
            {
                DaoManager.getInstance().getResourceBarDao().setAudioTimestamp(AppConfigHelper.audioFileTimestamp);
                prepareStaticAudioData();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        isRunning = false;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    private void prepareStaticAudioData()
    {
        GuideToneManager.getInstance().setDefaultAudioFamily(AppConfigHelper.audioFamily);
        GuideToneManager.getInstance().loadDefaultGuideTone();
    }
}
