/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Test.java
 *
 */
package com.telenav.navservice.util;


import com.telenav.logger.DefaultLoggerListener;
import com.telenav.logger.Logger;

/**
 *@author qli
 *@date 2010-10-29
 */
public class TrackingLogger extends DefaultLoggerListener
{
    public static boolean isLogEnabled;
    
    private String INFO = "[I]";

    private String WARNING = "[W]";

    private String EXCEPTION = "[E]";

    private String ERROR = "[R]";

    public void log(int level, String clazz, String message, Object[] params)
    {
        if (!isLogEnabled)
            return;
        
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

        logMessage += "[" + getClass(clazz) + "]  " + message;

        
        print(level, logMessage);

//        if( (Boolean) params[1] )
//        {//file log
//            printToFile(logMessage);
//        }
//        if( (Boolean) params[2] )
//        {//network log
//            printToServer(logMessage);
//        }
    }
    
    public void print(int level, String logMessage)
    {
    }
    
    public void printToFile(String message)
    {
    }
    
    public void printToServer(String message)
    {
    }
    
}
