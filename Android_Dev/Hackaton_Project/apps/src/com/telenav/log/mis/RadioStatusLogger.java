/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RadioStatusLogger.java
 *
 */
package com.telenav.log.mis;

import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.radio.ITnCoverageListener;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-1
 */
public class RadioStatusLogger implements ITnCoverageListener
{
    private static RadioStatusLogger instance = new RadioStatusLogger();

    private AppStatusMisLog statuslog;

    private int signalStrengh = 0;

    private boolean isNetworkLost = false;

    public static RadioStatusLogger getInstance()
    {
        return instance;
    }

    public int getSignalStrengh()
    {
        return signalStrengh;
    }

    public void onSignalStrengthChanged(int strength)
    {
        signalStrengh = strength;

        if (statuslog == null)
        {
            statuslog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_INNER_APP_STATUS);
        }

        if (strength < -120)
        {
            // before it was ok now we lost it
            if (!isNetworkLost)
            {
                isNetworkLost = true;
                statuslog.networkLose();
            }
        }
        else
        {
            // it has been lost now we got it
            if (isNetworkLost)
            {
                isNetworkLost = false;
                statuslog.networkResume();
            }
        }
    }

    public void onServiceStateChanged(int serviceState)
    {

    }
}
