/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IToolsProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Hashtable;

import com.telenav.log.mis.log.AbstractMisLog;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IToolsProxy
{
    public static final byte SYNC_TYPE_DOWNLOAD = 0;
    public static final byte SYNC_TYPE_UPLOAD = 1;
    public static final int UPLOAD_TYPE_USER_INFO = 1;
    public static final int UPLOAD_TYPE_HOME_WORK = 2;
    
    public static final String KEY_FIRSTNAME = "FIRSTNAME";
    public static final String KEY_LASTNAME = "LASTNAME";
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_CSRID = "CSRID";
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_GUIDETONE = "GUIDETONE";
    
    public String syncPreference(byte syncType, Hashtable prefers, int uploadType);
    
    public int getPreferenceSyncType();
    
    public String syncBillingAccount();
    
    public Hashtable getUserPrefers();
    
    public String sendMISReports(AbstractMisLog[] log, String sessionID);
    
    public String sendC2DMRegistrationId(String registrationId);
}
