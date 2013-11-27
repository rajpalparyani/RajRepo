/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MisLogFilter.java
 *
 */
package com.telenav.log.mis;

import java.util.Hashtable;

import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.logger.DefaultLoggerFilter;
import com.telenav.logger.Logger;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-1
 */
public class MisLogFilter extends DefaultLoggerFilter
{
    private Hashtable enabledIds = new Hashtable();

    private static Integer dummy = PrimitiveTypeCache.valueOf(-1);
    
    private static String enableClasses = "*";
    
    public MisLogFilter()
    {
        this(Logger.INFO, enableClasses);
    }
    
    public MisLogFilter(int level, String enableClasses)
    {
        super(level, enableClasses);
    }
    
    public boolean isLoggable(int level, String clazz, String message, Throwable t, Object[] params)
    {
        if (IMisLogConstants.PROCESS_MISLOG.equals(message))
        {
            if (params.length == 1 && params[0] instanceof AbstractMisLog)
            {
                AbstractMisLog log = (AbstractMisLog)params[0];
                return isTypeEnable(log.getType()) && log.isLoggable();
            }
        }
        return false;
    }
    
    public boolean isTypeEnable(int type)
    {
        return enabledIds.containsKey(PrimitiveTypeCache.valueOf(type));
    }
    
    public void enableTypes(int[] type)
    {
        if (type != null && type.length != 0)
        {
            for (int i = 0; i < type.length; i++)
            {
                enableType(type[i]);
            }
        }
    }

    public void enableType(int type)
    {
        enabledIds.put(PrimitiveTypeCache.valueOf(type), dummy);
    }
    
    public void reset()
    {
        enabledIds.clear();
    }


}
