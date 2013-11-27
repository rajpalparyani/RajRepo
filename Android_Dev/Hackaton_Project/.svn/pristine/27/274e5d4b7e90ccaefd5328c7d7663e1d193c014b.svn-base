/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * UnCaughtExceptionHandler.java
 *
 */
package com.telenav.app;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.TripSummaryMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.nav.NavRunningStatusProvider;

/**
 *@author yning
 *@date 2012-6-25
 */
public class CrashHandler implements UncaughtExceptionHandler 
{
    private static CrashHandler instance = new CrashHandler();
    
    public static CrashHandler getInstance()
    {
        return instance;
    }
    
    public void uncaughtException(Thread thread, Throwable ex)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(bos);
        ex.printStackTrace(writer);
        writer.flush();
        String trace = new String(bos.toByteArray());

        writer.close();

        if (trace != null)
        {
            Logger.log(Logger.ERROR, thread.getName(), trace, null, new Object[]{}, true);
        }
        
        //byte[] logBytes = TnSdLogCollector.getInstance().collectClientLogger();
       // doSendClientLogging(logBytes);
        
        if (NavRunningStatusProvider.getInstance().isNavRunning())
        {
            TripSummaryMisLog tripSummaryMisLog = (TripSummaryMisLog) MisLogManager.getInstance().getMisLog(IMisLogConstants.TYPE_TRIP_SUMMARY);
            if(tripSummaryMisLog != null)
            {
                tripSummaryMisLog.setEndedByForceQuit(true);
            }
        }
        
        logCrash();

        TeleNavDelegate.getInstance().exitApp();
    }

    protected void logCrash()
    {
        SimpleConfigDao simpleDao = DaoManager.getInstance().getSimpleConfigDao();
        int times = simpleDao.get(SimpleConfigDao.KEY_CRASH_TIMES);
        if (times < 0)
        {
            times = 0;
        }
        times ++;
        simpleDao.put(SimpleConfigDao.KEY_CRASH_TIMES, times);
        simpleDao.store();        
    }

    /*private void doSendClientLogging(byte[] clientLog)
    {
        if (clientLog == null || clientLog.length == 0)
        {
            return;
        }
        IClientLoggingProxy clientLoggingProxy = ServerProxyFactory.getInstance().createClientLoggingProxy(null,
            CommManager.getInstance().getComm(), null, null);
        String fileName = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().device + "_"
                + DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().buildNumber;
        clientLoggingProxy.sendClientLogging(clientLog, fileName, "zip");
    }*/

}
