/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfLogger.java
 *
 */
package com.telenav.dwf.aidl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.module.AppConfigHelper;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;

/**
 *@author bod
 *@date Aug 15, 2013
 */
public class DwfLogger
{
    public final static String DWF_TAG = "DWF";
    public final static String DWF_LOG_FILE_DIR = "/sdlogs/dwf";
    public final static int DWF_LOG_INDENT = 4;
    
    public final static int INFO = 0;
    public final static int WARNING = 1;
    public final static int ERROR = 2;
    
    protected ThreadPool logHandler;
    
    private static class InnerDwfLogger
    {
        private static DwfLogger instance = new DwfLogger();
    }
    
    private DwfLogger()
    {
        
    }
    
    public static DwfLogger getInstance()
    {
        return InnerDwfLogger.instance;
    }
    
    public void log(int level, String message)
    {
        if (message == null || message.length() == 0)
        {
            return;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        String timeTag = getDate();
        message = timeTag + " , " + message;
        
        logToConsole(level, message);
        logToFile(level, message);
    }
    
    protected String getDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        return (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + ":" + calendar.get(Calendar.MILLISECOND);
    }

    protected void logToFile(int level, String message)
    {
        if (logHandler == null)
        {
            logHandler = new ThreadPool(1, false, 1000);
            logHandler.start();
        }
        
        logHandler.addJob(getJob(message + "\n"));
    }
    
    protected static void logToConsole(int level, String message)
    {
        switch (level)
        {
            case INFO:
            {
                Log.i(DWF_TAG, message);
                break;
            }
            case WARNING:
            {
                Log.w(DWF_TAG, message);
                break;
            }
            case ERROR:
            {
                Log.e(DWF_TAG, message);
                break;
            }
            default:
            {
                Log.i(DWF_TAG, message);
            }
        }        
    }
    
    protected IJob getJob(final String message)
    {
        IJob job = new IJob()
        {
            boolean isRunning = false;
            boolean isCancelled = false;
            
            @Override
            public boolean isRunning()
            {
                return isRunning;
            }
            
            @Override
            public boolean isCancelled()
            {
                return isCancelled;
            }
            
            @Override
            public void execute(int handlerID)
            {
                isRunning = true;
                try
                {
                    saveToSdCard(message);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    isRunning = false;
                }
            }
            
            @Override
            public void cancel()
            {
                isCancelled = true;
            }
        };
        
        return job;
    }
    
    protected void saveToSdCard(String message) throws Exception
    {
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state))
            return;

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileName = sdf.format(date) + ".txt";
        String recordPath = FileStoreManager.TELENAV_DIRECTORY_PATH + DWF_LOG_FILE_DIR;
        File rootStorage = Environment.getExternalStorageDirectory();
        String directoryUrl = rootStorage.getAbsolutePath() + "/" + recordPath;
        
        File folder = new File(directoryUrl);
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        File file = new File(directoryUrl + "/" + fileName);
        
        String logTitle = "";
        if (!file.exists())
        {
            file.createNewFile();
            logTitle = "Telenav build#" + AppConfigHelper.buildNumber + "\n";
            logTitle += "Android build#" + Build.VERSION.SDK + "\n";
            logTitle += "Android Firmware#" + Build.VERSION.RELEASE + "\n";
            logTitle += "Device Type#" + Build.MANUFACTURER + "-" + Build.MODEL + "\n";

            try
            {
                String phoneNumber = TnTelephonyManager.getInstance().getPhoneNumber();
                logTitle += "PTN#" + phoneNumber + "\n";
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (DaoManager.getInstance() != null && DaoManager.getInstance().getMandatoryNodeDao() != null
                    && DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode() != null)

            {
                if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo() != null)
                {
                    logTitle += "Encrypted PTN#"
                            + DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().phoneNumber
                            + "\n";
                }
            }
        }

        OutputStream os = new FileOutputStream(file, true);
        if (logTitle.trim().length() > 0)
        {
            os.write(logTitle.getBytes());
        }
        os.write(message.getBytes());

        os.close();
    }
}
