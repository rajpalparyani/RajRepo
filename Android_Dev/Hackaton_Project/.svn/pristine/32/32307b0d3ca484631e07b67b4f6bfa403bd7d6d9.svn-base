/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Logger.java
 *
 */
package com.telenav.logger;

import java.util.Vector;

import com.crashlytics.android.Crashlytics;

/**
 * Loggers are used to log records to certain outputs, including file, console, etc. They use various handlers to
 * actually do the output-dependent operations.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */
public class Logger
{
    /**
     * true means that only log records at the development cycle, will not log records in the QA test builds, otherwise false. <br />
     * The ant build script will automatically change the value as false when release the build.
     */
    public static boolean DEBUG = true;

    /**
     * info level of records.
     */
    public final static int INFO = 0;

    /**
     * warning level of records.
     */
    public final static int WARNING = 1;

    /**
     * exception level of records. the whole throwable, exception should be this level.
     */
    public final static int EXCEPTION = 2;
    
    /**
     * error level of records.
     */
    public final static int ERROR = 3;

    private static int initCount;
    
    private static LoggerJob loggerJob;
    private static Vector logMessages = new Vector();
    private static Object mutex = new Object();
    private static boolean isShutDown;
    
    private static Vector loggerCallbacks = new Vector();
    
    /**
     * start logger.
     */
    public synchronized static void start()
    {
        if (initCount >= 1)
            return;

        initCount++;

        isShutDown = false;
        
        add(new DefaultLoggerListener(), new DefaultLoggerFilter(ERROR + 1, null));
        
        loggerJob = new LoggerJob();
        Thread loggerThread = new Thread(loggerJob);
        loggerThread.start();
    }
    
    /**
     * shutdown logger.
     */
    public synchronized static void shutdown()
    {
        isShutDown = true;
        
        if(loggerJob != null)
        {
            loggerJob.cancel();
        }
        
        initCount = 0;
    }
    
    /**
     * add the logger listener.
     * 
     * @param listener a call back of logger
     * @param filter filter for the logger
     */
    public static void add(ILoggerListener listener, LoggerFilter filter)
    {
        LoggerCallback callback = new LoggerCallback();
        callback.logListener = listener;
        callback.logFilter = filter;
        
        loggerCallbacks.addElement(callback);
    }

    /**
     * Logs a message of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param level the level of the specified message.
     * @param clazz the class of the log caller or some identifier.
     * @param str the message to log.
     */
    public static void log(int level, String clazz, String str)
    {
        log(level, clazz, str, null);
    }
    
    /**
     * Logs an critical excpetion which will cause App hang or crash
     * 
     * @param clazz the class of the log caller or some identifier.
     * @param t the exception.
     */
    public static void logCriticalError(String clazz, Throwable t)
    {
        Crashlytics.logException(t);
        log(clazz, t, null);
    }

    /**
     * Logs an exception of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param clazz the class of the log caller or some identifier.
     * @param t the exception.
     */
    public static void log(String clazz, Throwable t)
    {
        log(clazz, t, null);
    }
    
    /**
     * Logs an exception of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param clazz the class of the log caller or some identifier.
     * @param t the exception.
     * @param message the message to log.
     */
    public static void log(String clazz, Throwable t, String message)
    {
        log(clazz, t, message, null);
    }
    
    /**
     * Logs an exception of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param clazz the class of the log caller or some identifier.
     * @param t the exception.
     * @param message the message to log.
     * @param params the parameter array associated with the event that is logged. 
     */
    public static void log(String clazz, Throwable t, String message, Object[] params)
    {
        log(EXCEPTION, clazz, (message == null || message.length() == 0 ? "" : message + "\n") + t.getMessage(), t, params);
    }
    
    /**
     * Logs a message of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param level the level of the specified message.
     * @param clazz the class of the log caller or some identifier.
     * @param message the message to log.
     * @param params the parameter array associated with the event that is logged. 
     */
    public static void log(int level, String clazz, String message, Object[] params)
    {
        log(level, clazz, message, null, params);
    }
    
    /**
     * Logs a message of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param level the level of the specified message.
     * @param clazz the class of the log caller or some identifier.
     * @param message the message to log.
     * @param t the exception.
     * @param params the parameter array associated with the event that is logged. 
     */
    public static void log(int level, String clazz, String message, Throwable t, Object[] params)
    {
        log(level, clazz, message, t, params, false);
    }
    
    /**
     * Logs a message of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param level the level of the specified message.
     * @param clazz the class of the log caller or some identifier.
     * @param message the message to log.
     * @param t the exception.
     * @param params the parameter array associated with the event that is logged. 
     * @param isSync true means that will not log the message with logger thread.
     */
    public static void log(int level, String clazz, String message, Throwable t, Object[] params, boolean isSync)
    {
        LoggerMessage loggerMessage = new LoggerMessage();
        loggerMessage.level = level;
        loggerMessage.clazz = clazz;
        loggerMessage.message = message;
        loggerMessage.params = params;
        loggerMessage.t = t;
        
        if(isSync)
        {
            log(loggerMessage);
        }
        else
        {
            synchronized (mutex)
            {
                if (isShutDown)
                    return;

                logMessages.addElement(loggerMessage);
                mutex.notify();
            }
        }
    }
    
    static void log(LoggerMessage loggerMessage)
    {
        for( int i = 0; i < loggerCallbacks.size(); i++)
        {
            LoggerCallback callback = (LoggerCallback)loggerCallbacks.elementAt(i);
            
            if (!callback.logFilter.filter(loggerMessage.level, loggerMessage.clazz, loggerMessage.message, loggerMessage.t, loggerMessage.params))
            {
                continue;
            }

            if (callback.logListener != null)
            {
                callback.logListener.log(loggerMessage.level, loggerMessage.clazz, loggerMessage.message, loggerMessage.t, loggerMessage.params);
            }
        }
    }
    
    static class LoggerJob implements Runnable
    {
        private boolean isCancelled;
        
        public void run()
        {
            while(true)
            {
                try
                {
                    if (!logMessages.isEmpty())
                    {
                        LoggerMessage loggerMessage = (LoggerMessage) logMessages.elementAt(0);
                        logMessages.removeElementAt(0);

                        log(loggerMessage);
                    }
                    else
                    {
                        if(isCancelled)
                        {
                            break;
                        }
                        else
                        {
                            synchronized (mutex)
                            {
                                mutex.wait(0);
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            
            logMessages.removeAllElements();
            loggerCallbacks.removeAllElements();
        }
        
        public void cancel()
        {
            synchronized (mutex)
            {
                isCancelled = true;
                mutex.notify();
            }
        }
    }
    
    static class LoggerMessage
    {
        int level;

        String clazz;

        String message;

        Object[] params;
        
        Throwable t;
        
        public String toString()
        {
            return "[" + level  + "]" + "[" + clazz + "] " + message;
        }
    }
    
    static class LoggerCallback
    {
        ILoggerListener logListener;

        LoggerFilter logFilter;
    }
}
