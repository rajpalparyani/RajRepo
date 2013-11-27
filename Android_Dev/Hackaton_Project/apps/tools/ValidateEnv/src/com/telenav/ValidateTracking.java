/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ValidateTracking.java
 *
 */
package com.telenav;

import java.util.Calendar;
import java.util.Date;


import com.telenav.logger.DefaultLoggerListener;
import com.telenav.logger.Logger;

/**
 *@author qli
 *@date 2011-4-11
 */
public class ValidateTracking extends DefaultLoggerListener
{
    public void log(int level, String clazz, String message,  Throwable t, Object[] params)
    {
            
        if(t != null)
        {
            t.printStackTrace();
        }
        
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        String logMessage = getDate();

        switch (level)
        {
            case Logger.INFO:
                logMessage += INFO;
                break;
            case Logger.WARNING:
                logMessage += WARNING;
                break;
            case Logger.EXCEPTION:
                logMessage += EXCEPTION;
                break;
            case Logger.ERROR:
                logMessage += ERROR;
                break;
        }

        logMessage += "[" + getClass(clazz) + "]" + message;

        switch (level)
        {
            case Logger.INFO:
                logMessage += INFO;
                System.out.println(message);
                break;
            case Logger.WARNING:
                logMessage += WARNING;
                System.out.println(message);
                break;
            case Logger.EXCEPTION:
                logMessage += EXCEPTION;
                System.out.println(message);
                break;
            case Logger.ERROR:
                logMessage += ERROR;
                System.out.println(message);
                break;
        }
    }
}
