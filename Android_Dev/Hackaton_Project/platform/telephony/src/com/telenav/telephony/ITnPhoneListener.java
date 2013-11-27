/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnPhoneListener.java
 *
 */
package com.telenav.telephony;

/**
 * Implement this interface if you intend to listen for and act on phone events.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 11, 2010
 */
public interface ITnPhoneListener
{
    /**
     * A new call arrived and is ringing or waiting.
     */
    public final static int STATE_RINGING = 0;

    /**
     * Invoked when the network indicates a connected event.
     */
    public final static int STATE_CONNECTED = 1;

    /**
     * Invoked when a call is disconnected.
     */
    public final static int STATE_DISCONNECTED = 2;

    /**
     * Callback invoked when device call state changes.
     * 
     * @param state {@link STATE_RINGING}, {@link STATE_CONNECTED}, {@link STATE_DISCONNECTED}
     * @param phoneNumber provide the call phone number if possible, sometimes it will be empty.
     * @param reasonString provide the reason of the state, most times it will be empty.
     */
    public void onCallStateChanged(int state, String phoneNumber, String reasonString);
}
