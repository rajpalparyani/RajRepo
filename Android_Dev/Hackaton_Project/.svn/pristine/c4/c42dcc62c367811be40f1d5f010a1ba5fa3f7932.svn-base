/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimPhoneListener.java
 *
 */
package com.telenav.telephony.rim;

import com.telenav.telephony.ITnPhoneListener;

import net.rim.blackberry.api.phone.PhoneListener;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimPhoneListener implements PhoneListener
{
    private ITnPhoneListener tnPhoneListener;
    
    RimPhoneListener(ITnPhoneListener tnPhoneListener)
    {
        this.tnPhoneListener = tnPhoneListener;
    }

    public void callAdded(int callId)
    {
        
    }

    public void callAnswered(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_CONNECTED, "" + callId, null);
    }

    public void callConferenceCallEstablished(int callId)
    {
        
    }

    public void callConnected(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_CONNECTED, "" + callId, null);
    }

    public void callDirectConnectConnected(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_CONNECTED, "" + callId, null);
    }

    public void callDirectConnectDisconnected(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, "" + callId, null);
    }

    public void callDisconnected(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, "" + callId, null);
    }

    public void callEndedByUser(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, "" + callId, "end by user.");
    }

    public void callFailed(int callId, int reason)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, "" + callId, "failed.");
    }

    public void callHeld(int callId)
    {
        
    }

    public void callIncoming(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_RINGING, "" + callId, "failed.");
    }

    public void callInitiated(int callId)
    {
        
    }

    public void callRemoved(int callId)
    {
        
    }

    public void callResumed(int callId)
    {
        
    }

    public void callWaiting(int callId)
    {
        
    }

    public void conferenceCallDisconnected(int callId)
    {
        tnPhoneListener.onCallStateChanged(ITnPhoneListener.STATE_DISCONNECTED, "" + callId, null);
    }
}
