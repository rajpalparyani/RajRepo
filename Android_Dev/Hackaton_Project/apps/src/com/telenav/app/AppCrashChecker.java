/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ScoutCrashChecker.java
 *
 */
package com.telenav.app;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;

/**
 *@author yren
 *@date 2013-7-26
 */
public class AppCrashChecker
{
    private static AppCrashChecker instance = null;

    public static AppCrashChecker getInstance()
    {
        if (instance == null)
        {
            instance = new AppCrashChecker();
        }
        return instance;
    }
    
    public void checkCrashState()
    {
        boolean isExitAppNormally = true;
        String strValue = DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_EXIT_APP_NORMALLY);
        if (strValue != null)
        {
            isExitAppNormally = DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_EXIT_APP_NORMALLY);
            if (!isExitAppNormally)
            {
                SimpleConfigDao simpleDao = DaoManager.getInstance().getSimpleConfigDao();
                int times = simpleDao.get(SimpleConfigDao.KEY_CRASH_TIMES);
                if (times < 0)
                {
                    times = 0;
                }
                times++;
                simpleDao.put(SimpleConfigDao.KEY_CRASH_TIMES, times);
                simpleDao.store();
            }
        }
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_EXIT_APP_NORMALLY, false);
        DaoManager.getInstance().getSimpleConfigDao().store();
    }
    
    public void exitAppNormally()
    {
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_EXIT_APP_NORMALLY, true);
        DaoManager.getInstance().getSimpleConfigDao().store();
    }
}
