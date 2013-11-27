/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DefaultLoggerListener.java
 *
 */
package com.telenav.logger;

import java.util.Calendar;
import java.util.Date;

/**
 * the default implementation of logger listener.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 22, 2010
 */
public class DefaultLoggerListener implements ILoggerListener
{
    protected String INFO = "[I]";

    protected String WARNING = "[W]";

    protected String EXCEPTION = "[E]";

    protected String ERROR = "[R]";

    public void log(int level, String clazz, String message, Throwable t, Object[] params)
    {
        if(t != null)
        {
            t.printStackTrace();
        }
        
        log(level, clazz, message, params);
    }
    
    public void log(int level, String clazz, String message, Object[] params)
    {
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
        
        logDelegate(logMessage);
    }
    
    protected void logDelegate(String logMessage)
    {
        
    }

    protected String getDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        return (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }

    protected String getClass(String clazz)
    {
        int lastDotIndex = clazz.lastIndexOf('.');
        lastDotIndex = lastDotIndex + 1;
        
        return clazz.substring(lastDotIndex);
    }
}
