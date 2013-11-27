/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnTelephonyManager.java
 *
 */
package com.telenav.telephony;

import java.util.Hashtable;


/**
 * Provides access to the device's SimCard, Call, Mail etc.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public abstract class TnTelephonyManager
{
    private static TnTelephonyManager telephonyManager;
    private static int initCount;
    
    /**
     * Retrieve the instance of telephony manager.
     * 
     * @return {@link TnTelephonyManager}
     */
    public static TnTelephonyManager getInstance()
    {
        return telephonyManager;
    }
    
    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param telephonyMngr This manager is native manager of platforms. Such as {@link AndroidTelephonyManager} etc.
     */
    public synchronized static void init(TnTelephonyManager telephonyMngr)
    {
        if(initCount >= 1)
            return;
        
        telephonyManager = telephonyMngr;
        initCount++;
    }
    
    /**
     * Retrieve the fundamental information about the host device.
     * 
     * @return {@link TnDeviceInfo}
     */
    public abstract TnDeviceInfo getDeviceInfo();
    
    /**
     * Retrieve the information about the SIM Card.
     * 
     * @return {@link TnSimCardInfo}
     */
    public abstract TnSimCardInfo getSimCardInfo();
    
    /**
     * Retrieve the phone number of the host device.
     * 
     * @return the phone number
     */
    public abstract String getPhoneNumber();
    
    /**
     * Retrieve IP address of the host device.
     * 
     * @return the IP address
     */
    public abstract String getIpAddress();
    
    /**
     * call a phone number.
     * 
     * @param phoneNumber the number will be called.
     */
    public abstract void startCall(String phoneNumber);
    
    /**
     * start a browser session and display the url page.
     * 
     * @param url Fully qualified URL of page to retrieve.
     */
    public abstract void startBrowser(String url);
    
    /**
     * start an email session and display the email screen.
     * 
     * @param sentTo The email address for the recipient.
     * @param subject A subject for the email.
     * @param content The email body.
     */
    public abstract void startEmail(String sentTo, String subject, String content);
    
    /**
     * start an MMS session.
     * 
     * @param sentTo The MMS phone number.
     * @param message The MMS message.
     */
    public abstract void startMMS(String sentTo, String message);
    
    /**
     * start an MMS session at background.
     * 
     * @param sentTo The MMS phone number.
     * @param message The MMS message.
     */
    public abstract void startMMSAtBackground(String sentTo, String message);
    
    /**
     * register a phone listener object to receive notification of Call phone state.
     * 
     * @param phoneListener The {@link ITnPhoneListener} object to register.
     */
    public abstract void addListener(ITnPhoneListener phoneListener);
    
    /**
     * UnRegisters a listener object of call phone state service.
     * 
     * @param coverageListener The {@link ITnPhoneListener} object to register.
     */
    public abstract void removeListener(ITnPhoneListener phoneListener);
    
    /**
     * Make vibration for giving duration. 
     * 
     * Duration unit: millisecond.
     * 
     * @param durationTime
     */
    public abstract void vibrate(int durationTime);
    
    /**
     * <b>Get all matched apn.</b><br/>
     * if type is not specified, apns with "mms" type will also be return.
     * if you do not need this type, please ignore it by yourself.
     * @param apn
     * @param type
     */
    public TnApnInfo[] getMatchedApn(String apn, String type){
        String simOperator = getSimOperator();
        String deviceMcc = "";
        String deviceMnc = "";
        if (simOperator != null && simOperator.length() > 3) {
            deviceMcc = simOperator.substring(0, 3);
            deviceMnc = simOperator.substring(3);
        }
        else{
            return null;
        }
        return getMatchedApn(apn, type, deviceMcc, deviceMnc);
    }
    
    /**
     * <b>Get all preferred apn.</b><br/>
     * For most cases, there are only one apn as the preferred one.
     * And we ignored "mms" type even if it is preferred.
     * @param apn
     * @param type
     * @return
     */
    public TnApnInfo getPreferredApn(String apn,String type){
        String simOperator = getSimOperator();
        String deviceMcc = "";
        String deviceMnc = "";
        if (simOperator != null && simOperator.length() > 3) {
            deviceMcc = simOperator.substring(0, 3);
            deviceMnc = simOperator.substring(3);
        }
        else{
            return null;
        }
        return getPreferredApn(apn,type,deviceMcc,deviceMnc);
    }
    
    public abstract void addApnListener(ITnApnListener listener);
    
    public abstract void removeApnListener(ITnApnListener listener);
    
    public String insertApn(TnApnInfo apn)
    {
        return insertApn(apn,null);
    }
    
    /**
     * insert apn object with extra fields
     * @param apn apn object
     * @param extras extra fields for the apn, will override the apn object for duplicated fields.
     * @return apn id.
     */
    public abstract String insertApn(TnApnInfo apn, Hashtable extras);
    
    public abstract void setPrefered(String apnId);
    
    protected abstract TnApnInfo[] getMatchedApn(String apn, String type, String mcc, String mnc); 
    
    protected abstract String getSimOperator();
    
    protected abstract TnApnInfo getPreferredApn(String apn, String type, String mcc, String mnc);
    
}
