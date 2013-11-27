package com.telenav.navservice;

import com.telenav.logger.DefaultLoggerFilter;
import com.telenav.logger.ILoggerListener;
import com.telenav.logger.Logger;

public class TestUtil
{
    private static boolean isLogInit;

    public static void initLog()
    {
        if (!isLogInit)
        {
            isLogInit = true;
            Logger.start();
            Logger.add(new ILoggerListener()
            {
                public void log(int level, String clazz, String message, Throwable t,
                        Object[] params)
                {
                    if (t != null)
                        t.printStackTrace();
                }
            }, new DefaultLoggerFilter(Logger.INFO, "*"));
        }
    }
}
