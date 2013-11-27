/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndoidPhoneListener.java
 *
 */
package com.telenav.telephony.android;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.telenav.telephony.ITnPhoneListener;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
class AndoidPhoneListener extends PhoneStateListener
{
    private ITnPhoneListener tnPhoneListener;

    private boolean isPhoneConnected = false;

    AndoidPhoneListener(ITnPhoneListener tnPhoneListener)
    {
        this.tnPhoneListener = tnPhoneListener;
    }

    public void onCallStateChanged(int state, String incomingNumber)
    {
        switch (state)
        {
            case TelephonyManager.CALL_STATE_IDLE:
            {
                if (isPhoneConnected)
                {
                    isPhoneConnected = false;
                    this.tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, incomingNumber, "end by user.");
                }
                else
                {
                    this.tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, incomingNumber, "declined.");
                }
                break;
            }
            case TelephonyManager.CALL_STATE_RINGING:
            {
                this.tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_RINGING, incomingNumber, null);
                break;
            }
            case TelephonyManager.CALL_STATE_OFFHOOK:
            {
                if (!isPhoneConnected)
                {
                    isPhoneConnected = true;
                    this.tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_CONNECTED, incomingNumber, null);
                }
                break;
            }
            default:
                break;
        }
    }
}
