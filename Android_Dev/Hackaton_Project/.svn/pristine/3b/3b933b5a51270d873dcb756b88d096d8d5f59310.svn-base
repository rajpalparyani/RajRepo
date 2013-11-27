/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DefaultLoggerFilter.java
 *
 */
package com.telenav.logger;

/**
 * the default implementation of logger filter.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 23, 2010
 */
public class DefaultLoggerFilter extends LoggerFilter
{
    protected String enableClasses;

    protected boolean isEnableAll;

    public DefaultLoggerFilter(int level, String enableClasses)
    {
        super(level);

        this.enableClasses = enableClasses;

        if (this.enableClasses != null && this.enableClasses.indexOf("*") != -1)
        {
            isEnableAll = true;
        }
    }

    public boolean isLoggable(int level, String clazz, String message, Throwable t, Object[] params)
    {
        if (isEnableAll)
            return true;

        if (this.enableClasses == null || this.enableClasses.length() == 0)
            return false;

        if (this.enableClasses.indexOf(clazz) != -1)
            return true;

        return false;
    }

}
