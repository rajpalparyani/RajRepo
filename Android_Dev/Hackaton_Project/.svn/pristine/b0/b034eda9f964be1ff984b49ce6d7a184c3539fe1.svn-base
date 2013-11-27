/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ITnCoverageStatusListener.java
 *
 */
package com.telenav.radio;

/**
 * The listener interface for receiving notifications of changes in coverage status, such as service state's change and
 * signal strength's change. [This listener will not contain the wifi's status change]
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 11, 2010
 */
public interface ITnCoverageListener
{
    /**
     * Radio of telephony is explictly powered off.
     */
    public final static int STATE_OFF = 0;

    /**
     * Phone is not registered with any operator, the phone can be currently searching a new operator to register to, or
     * not searching to registration at all, or registration is denied, or radio signal is not available.
     */
    public final static int STATE_OUT_OF_COVERAGE = 1;

    /**
     * The phone is registered and locked. Only emergency numbers are allowed.
     */
    public final static int STATE_EMERGENCY = 2;

    /**
     * Normal operation condition, the phone is registered with an operator either in home network or in roaming.
     */
    public final static int STATE_IN_SERVICE = 3;

    /**
     * Callback invoked when device service state changes.
     * 
     * @param serviceState {@link #STATE_OFF}, {@link #STATE_OUT_OF_COVERAGE}, {@link #STATE_EMERGENCY},
     *            {@link #STATE_IN_SERVICE}
     */
    public void onServiceStateChanged(int serviceState);

    /**
     * Callback invoked when network signal strengths changes.
     * 
     * @param strength For GSM and CDMA, the strength is different.
     */
    public void onSignalStrengthChanged(int strength);
}
