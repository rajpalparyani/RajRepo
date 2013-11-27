/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StartupInfoMislog.java
 *
 */
package com.telenav.log.mis.log;

import android.util.Log;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.plugin.PluginDataProvider;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-27
 */
public class StartupInfoMisLog extends AbstractMisLog
{

    public StartupInfoMisLog(String id, int priority)
    {
        super(id, TYPE_STARTUP_INFO, priority);
    }

    public void processLog()
    {
        super.processLog();
        
        AppStatusMisLog statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
        if(statuslog != null)
        {
            setStartTime(statuslog.getAppStartTime());
        }

        String startedBy = PluginDataProvider.getInstance().getFromApp();
        if (startedBy == null)
        {
            if (PluginDataProvider.getInstance().isFromMaiTai())
            {
                startedBy=MaiTaiManager.getInstance().getStartByInMaiTai();
            }
            else
                startedBy = IMisLogConstants.VALUE_BY_MANUAL;
        }
        
        Log.d("startedBy", "startedBy === "+startedBy);
        setStartedBy(startedBy);
        
        String entryPoint = getEntryPointValue(
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().productType, PluginDataProvider.getInstance()
                    .getFromApp(), PluginDataProvider.getInstance().getSelectedAction());
        setStartupEntryPoint(entryPoint);
        
//It seems that app will not startup from background now.        
//        if (TeleNavDelegate.getInstance().isFromSoftExit)
//        {
//            // started from background after "soft" exit
//            setStartedFrom(IMisLogConstants.VALUE_FROM_BACKGROUND);
//            TeleNavDelegate.getInstance().isFromSoftExit = false;
//        }
//        else
//        {
//            setStartedFrom(IMisLogConstants.VALUE_FROM_FOREGROUND);
//        }
        
    }
    
    public void setStartedBy(String startedBy)
    {
        this.setAttribute(ATTR_STARTED_BY, startedBy);
    }

    public void setStartupEntryPoint(String entryPoint)
    {
        this.setAttribute(ATTR_STARTUP_ENTRY_POINT, entryPoint);
    }

    public void setStartedFrom(String startedFrom)
    {
        this.setAttribute(ATTR_STARTED_FROM, startedFrom);
    }
    
    public void setStartTime(long startTime)
    {
        this.setAttribute(ATTR_START_TIME, String.valueOf(startTime));
    }
    
    public static String getEntryPointValue(String productType, String startedBy, String actionName)
    {
        // if manual start, check product type, for att_maps - map_it action
        if (actionName == null)
        {
            if (startedBy == null || IMisLogConstants.VALUE_BY_MANUAL.equals(startedBy))
            {
                if ("ATT_MAPS".equals(productType))
                {
                    return IMisLogConstants.VALUE_ENTRY_POINT_MAP_IT;
                }
                return IMisLogConstants.VALUE_ENTRY_POINT_HOME;
            }
            // apps where startedBy corresponds to only one action
            if (IMisLogConstants.VALUE_BY_COMMUTE_ALERT.equals(startedBy))
            {
                return IMisLogConstants.VALUE_ENTRY_POINT_TRAFFIC_SUMMARY;
            }
            else if(IMisLogConstants.VALUE_BY_MAPS_SHORTCUT.equals(startedBy))
            {
                return VALUE_ENTRY_POINT_MAP_IT;
            }
            else if(IMisLogConstants.VALUE_BY_DRIVE_SHORTCUT.equals(startedBy))
            {
                return VALUE_ENTRY_POINT_DRIVE_TO;
            }
            else if(IMisLogConstants.VALUE_BY_PLACES_SHORTCUT.equals(startedBy))
            {
                return VALUE_ENTRY_POINT_SEARCH;
            }
            return IMisLogConstants.VALUE_ENTRY_POINT_OTHER;
        }

        if (actionName.equals(PluginDataProvider.getInstance().ACTION_NAV_VALUE))
        {
            return IMisLogConstants.VALUE_ENTRY_POINT_DRIVE_TO;
        }
        else if (actionName.equals(PluginDataProvider.getInstance().ACTION_MAP_VALUE))
        {
            return IMisLogConstants.VALUE_ENTRY_POINT_MAP_IT;
        }
        else if (actionName.equalsIgnoreCase(PluginDataProvider.getInstance().ACTION_BIZ_VALUE))
        {
            return IMisLogConstants.VALUE_ENTRY_POINT_SEARCH;
        }
        else if (actionName.equalsIgnoreCase(PluginDataProvider.getInstance().ACTION_SHAREADDRESS_VALUE))
        {
            return IMisLogConstants.VALUE_ENTRY_POINT_SHARE_ADDR;
        }
        else if (actionName.equalsIgnoreCase(PluginDataProvider.getInstance().ACTION_GET_DIRECTIOSN_VALUE))
        {
            return IMisLogConstants.VALUE_ENTRY_POINT_GET_DIRECTIONS;
        }

        return IMisLogConstants.VALUE_ENTRY_POINT_OTHER;
    }
}
