/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LoggerFilter.java
 *
 */
package com.telenav.logger;

/**
 * A Filter provides a mechanism for exercising fine-grained control over which records get logged.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 22, 2010
 */
public abstract class LoggerFilter
{
    protected int level;

    /**
     * construct a filter.
     * 
     * @param level the filter level. {@link Logger#INFO}, {@link Logger#WARNING}, {@link Logger#EXCEPTION} etc.
     */
    public LoggerFilter(int level)
    {
        this.level = level;
    }

    /**
     * Retrieve the filter level.
     * 
     * @return {@link Logger#INFO}, {@link Logger#WARNING}, {@link Logger#EXCEPTION} etc.
     */
    public int getLevel()
    {
        return this.level;
    }

    /**
     * Checks log to determine if it should be logged.
     * 
     * @param level
     * @param clazz
     * @param message
     * @param params
     * @return true if the supplied log record needs to be logged, false otherwise. 
     */
    final boolean filter(int level, String clazz, String message, Throwable t, Object[] params)
    {
        if (level < this.level)
            return false;

        return this.isLoggable(level, clazz, message, t, params);
    }

    /**
     * Checks log to determine if it should be logged.
     * 
     * @param level
     * @param clazz
     * @param message
     * @param params
     * @return true if the supplied log record needs to be logged, false otherwise. 
     */
    public abstract boolean isLoggable(int level, String clazz, String message, Throwable t, Object[] params);
}
