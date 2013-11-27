/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MyLog.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module;

import android.util.Log;
/**
 *@author xiangli
 *@date 2011-5-16
 */
public class MyLog {
	private static boolean openLog = true;
	private MyLog() {
		
	}
	
	public static void setLog(String logName, String logContent) {
		if (!openLog)
			return;
		
		Log.i(logName, logContent);
	}
}
