/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ILoggerListener.java
 *
 */
package com.telenav.logger;

/**
 * Callback of logger.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 22, 2010
 */
public interface ILoggerListener
{
    /**
     * Logs a message of the specified level. The message is transmitted to all subscribed handlers.
     * 
     * @param level the level of the specified message.
     * @param clazz the class of the log caller or some identifier.
     * @param str the message to log.
     * @param params the parameter array associated with the event that is logged.
     */
    public void log(int level, String clazz, String message, Throwable t, Object[] params);
}
