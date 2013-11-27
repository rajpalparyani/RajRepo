/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * PreferenceCoverageListener.java
 *
 */
package com.telenav.module.preference;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.logger.Logger;
import com.telenav.module.sync.SyncResExecutor;

/**
 *@author yning
 *@date 2013-2-21
 */
public class PreferenceCoverageListener implements INetworkStatusListener
{
    @Override
    public void statusUpdate(boolean isConnected)
    {
        if (isConnected)
        {
            boolean isNeedUpload = DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SETTING_DATA_NEED_UPLOAD);
            
            if(isNeedUpload)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Preference delay sync: send request", null, null, true);
                SyncResExecutor.getInstance().syncSettingData(null, null, null, ISettingDataProxy.SYNC_TYPE_UPLOAD);
            }
            
            NetworkStatusManager.getInstance().removeStatusListener(this);
        }
    }
    
}
