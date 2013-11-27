/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidRadioStatusListener.java
 *
 */
package com.telenav.radio.android;

import com.telenav.radio.ITnCoverageListener;

import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 11, 2010
 */
class AndroidRadioStatusListener extends PhoneStateListener
{
    private ITnCoverageListener coverageListener;

    AndroidRadioStatusListener(ITnCoverageListener coverageListener)
    {
        this.coverageListener = coverageListener;
    }

    public void onSignalStrengthChanged(int asu)
    {
        int dBm = -113 + 2 * asu;
        this.coverageListener.onSignalStrengthChanged(dBm);
    }

    public void onServiceStateChanged(ServiceState serviceState)
    {
        int tnState = ITnCoverageListener.STATE_IN_SERVICE;
        
        switch(serviceState.getState())
        {
            case ServiceState.STATE_EMERGENCY_ONLY:
                tnState = ITnCoverageListener.STATE_EMERGENCY;
                break;
            case ServiceState.STATE_IN_SERVICE:
                tnState = ITnCoverageListener.STATE_IN_SERVICE;
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                tnState = ITnCoverageListener.STATE_OUT_OF_COVERAGE;
                break;
            case ServiceState.STATE_POWER_OFF:
                tnState = ITnCoverageListener.STATE_OFF;
                break;
        }
        
        this.coverageListener.onServiceStateChanged(tnState);
    }
}
