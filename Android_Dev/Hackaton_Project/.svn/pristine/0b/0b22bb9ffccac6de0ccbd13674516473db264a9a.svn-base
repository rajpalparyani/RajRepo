/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfAidl.java
 *
 */
package com.telenav.dwf.aidl;

import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.DwfRequestAidl;
import com.telenav.dwf.aidl.DwfResponseAidl;
import com.telenav.dwf.aidl.DwfUpdateListener;

/** Example service interface */
interface DwfAidl
{
	void setDwfServiceUrl(String url);
	
	void setDwfWebUrl(String url);
	
	void setShareLocationInterval(in long interval);
	
	void startShareLocation(String sessionId, in Friend host, String formattedDt);
	
	void stopShareLocation();
	
	void pauseShareLocation();
	
	void resumeShareLocation();
	
	Intent getSharingIntent();
	
	void updateStatus(long eta, String status, double lat, double lon);
	
    DwfResponseAidl request(in DwfRequestAidl request);
    
    void addUpdateListener(DwfUpdateListener listener, String key);
    
    void clearUpdateListener();
    
    void removeUpdateListener(String key);
    
    void setMainAppStatus(String status);
    
    void enableNotification(boolean enable, String status);
    
    void debugLog(int level, String message);
}