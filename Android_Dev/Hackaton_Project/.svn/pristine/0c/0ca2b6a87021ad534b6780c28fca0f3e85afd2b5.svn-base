/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * GpsStatusLogger.java
 *
 */
package com.telenav.log.mis;

import com.telenav.gps.IGpsListener;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.log.AppStatusMisLog;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-1
 */
public class GpsStatusLogger implements IGpsListener
{
    private static GpsStatusLogger instance = new GpsStatusLogger();

    private AppStatusMisLog statuslog;

    public static GpsStatusLogger getInstance()
    {
        return instance;
    }

    private boolean isGpsLost = false;

    public void gpsDataArrived(TnLocation data)
    {
        if (statuslog == null)
        {
            statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
        }

        if ((data.getLatitude() == 0 && data.getLongitude() == 0) || (data.getLatitude() == -1 && data.getLongitude() == -1))
        {
            // before it was ok now we lost it
            if (!isGpsLost)
            {
                isGpsLost = true;
                statuslog.gpsSingalLose();
            }
        }
        else
        {
            // it has been lost now we got it
            if (isGpsLost)
            {
                isGpsLost = false;
                statuslog.gpsSingalResume();
            }
        }
    }

}
