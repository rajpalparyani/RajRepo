/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimRadioStatusListener.java
 *
 */
package com.telenav.radio.rim;

import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.RadioStatusListener;

import com.telenav.radio.ITnCoverageListener;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimRadioStatusListener implements RadioStatusListener
{
    private ITnCoverageListener coverageListener;

    RimRadioStatusListener(ITnCoverageListener coverageListener)
    {
        this.coverageListener = coverageListener;
    }

    public void baseStationChange()
    {

    }

    public void networkScanComplete(boolean arg0)
    {

    }

    public void networkServiceChange(int networkId, int service)
    {
        if (service == RadioInfo.NETWORK_SERVICE_EMERGENCY_ONLY || service == RadioInfo.NETWORK_SERVICE_E911_CALLBACK_MODE)
        {
            coverageListener.onServiceStateChanged(ITnCoverageListener.STATE_EMERGENCY);
        }
        else
        {
            coverageListener.onServiceStateChanged(ITnCoverageListener.STATE_IN_SERVICE);
        }
    }

    public void networkStarted(int arg0, int arg1)
    {

    }

    public void networkStateChange(int arg0)
    {

    }

    public void pdpStateChange(int arg0, int arg1, int arg2)
    {

    }

    public void radioTurnedOff()
    {
        coverageListener.onServiceStateChanged(ITnCoverageListener.STATE_OFF);
    }

    public void signalLevel(int level)
    {
        coverageListener.onSignalStrengthChanged(level);
        
        if(level == RadioInfo.LEVEL_NO_COVERAGE)
        {
            coverageListener.onServiceStateChanged(ITnCoverageListener.STATE_OUT_OF_COVERAGE);
        }
    }

}
