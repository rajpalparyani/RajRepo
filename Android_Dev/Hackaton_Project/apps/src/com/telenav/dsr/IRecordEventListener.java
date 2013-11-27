/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ICancelListener.java
 *
 */
package com.telenav.dsr;

/**
 *@author bduan
 *@date 2010-10-28
 */
public interface IRecordEventListener
{
    public static final int EVENT_TYPE_START = 0;
    
    public static final int EVENT_TYPE_STOP = 1;
    
    public static final int EVENT_TYPE_VOLUME_UPDATE = 2;
    
    public static final int EVENT_TYPE_RECO_SUCCESSFUL = 3;
    
    public static final int EVENT_TYPE_RECO_FAIL = 4;
    
    public static final int EVENT_TYPE_SPEECH_STOP = 5;
    
    public static final int EVENT_TYPE_SPEECH_MANNUAL_STOP = 6;
    
    
    public static final int EVENT_STOP_TYPE_NETWORK_ERROR = -1;
    
    public static final int EVENT_STOP_TYPE_TIMEOUT = -2;
    
    public static final int EVENT_STOP_TYPE_SPEECH_END = -3;
    
    public static final int EVENT_STOP_TYPE_USER_STOP = -4;
    
    
    public void recordStatusUpdate(int event, Object eventData);
}
