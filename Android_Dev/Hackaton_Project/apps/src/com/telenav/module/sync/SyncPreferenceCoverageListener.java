/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * SyncPreferenceCoverageListener.java
 *
 */
package com.telenav.module.sync;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IToolsProxy;

/**
 *@author yning
 *@date 2013-6-5
 */
public class SyncPreferenceCoverageListener implements INetworkStatusListener
{
    private static SyncPreferenceCoverageListener instance = new SyncPreferenceCoverageListener();
    
    public static SyncPreferenceCoverageListener getInstance()
    {
        return instance;
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        if(isConnected)
        {
            boolean isUserProfileNeedUpload = DaoManager.getInstance().getSimpleConfigDao()
                    .getBoolean(SimpleConfigDao.KEY_USER_PROFILE_NEED_UPLOAD);
            boolean isHomeWorkNeedUpload = DaoManager.getInstance().getSimpleConfigDao()
                    .getBoolean(SimpleConfigDao.KEY_HOME_WORK_NEED_UPLOAD);

            boolean isNeedUpload = (isUserProfileNeedUpload | isHomeWorkNeedUpload);
            
            if(isNeedUpload)
            {
                SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_UPLOAD, null, null, -1);
            }
            
            NetworkStatusManager.getInstance().removeStatusListener(this);
        }
    }
    
    
}
