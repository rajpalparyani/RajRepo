/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DashboardModel.java
 *
 */
package com.telenav.module.drivingshare;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.mvc.AbstractCommonModel;

/**
 *@author qli
 *@date 2012-2-3
 */
public class DrivingShareModel extends AbstractCommonModel implements IDrivingShareConstants
{
    protected static int SWITCH_VIRGIN = -1;
    protected static int SWITCH_ON = 1;
    protected static int SWITCH_OFF = 1;
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                initDefaultEnbaleValue();
                postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_SAVE:
            {
                SimpleConfigDao simpleConfigDao = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao();
                boolean realTimeShareValue = this.getBool(KEY_B_SHARE_REAL_TIME_ENABLE);
                simpleConfigDao.put(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_SETTED, true);
                simpleConfigDao.put(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_ENABLED, realTimeShareValue);
                simpleConfigDao.store();
                break;
            }
        }
    }
    
    protected void initDefaultEnbaleValue()
    {
        SimpleConfigDao simpleConfigDao = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao();
        boolean isRealTimeShareSet = simpleConfigDao.getBoolean(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_SETTED);
        boolean boolDefalutSwitchValue = false;
        if (!isRealTimeShareSet)
        {
            boolDefalutSwitchValue = true;
        }
        else
        {
            boolDefalutSwitchValue = simpleConfigDao.getBoolean(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_ENABLED);
        }
        this.put(KEY_B_SHARE_REAL_TIME_SET, isRealTimeShareSet);
        this.put(KEY_B_SHARE_REAL_TIME_ENABLE, boolDefalutSwitchValue);
    }
    
}
