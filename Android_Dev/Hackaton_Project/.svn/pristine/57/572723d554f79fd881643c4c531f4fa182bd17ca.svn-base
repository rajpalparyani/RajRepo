/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidUtil.java
 *
 */
package com.telenav.navservice.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import com.telenav.logger.DefaultLoggerFilter;
import com.telenav.logger.Logger;
import com.telenav.navservice.util.TrackingLogger;

/**
 * AndroidTrackingLogger provide platform console log method,  file log class.
 * 
 *@author qli
 *@date 2010-11-19
 */
public class AndroidTrackingLogger extends TrackingLogger
{
    protected final String logFileName = "navservice";//log filename start with "navservice"
    protected final String logFileDir = "navservice_log";
    protected static String TAG = "GPS_Tracker";
    
    private final boolean append = false;//close append log
    private boolean fileLogOpen = false;
    private boolean logIsRun = false;
    private boolean fileLogException = false;
    private PrintWriter writer = null;
    
    public void initLogger()
    {
        String enable = null;
        
        enable = "*";
        logIsRun = true;
        fileLogException = false;
        Logger.start();
        Logger.add(this, new DefaultLoggerFilter(Logger.INFO, enable));//FYI:don't print log, just use null instead of "*"
        Logger.log(Logger.INFO, this.toString(), " Logger is ok");
    }

    public void print(int level, String message)
    {
        switch (level)
        {
            case Logger.INFO:
                Log.i(TAG, message);
                break;
            case Logger.WARNING:
                Log.w(TAG, message);
                break;
            case Logger.EXCEPTION:
                Log.e(TAG, message);
                break;
            case Logger.ERROR:
                Log.e(TAG, message);
                break;
        }
    }

    public void printToFile(String message)
    {
        try
        {
            if( !fileLogException )
            {
                open();
            }
        }
        catch (Exception e)
        {
            fileLogException = true;
            Log.e("AndroidTrackingLogger"," Can't log to file. Reason: "+e.toString(), e);
        }
        if ((fileLogOpen) && (writer != null))
        {
            writer.println(message);
        }
    }
    
    protected void close()
    {
        Logger.shutdown();
        try
        {
            if (writer != null)
            {
                writer.close();
            }
        }
        catch( Exception e)
        {
        }
        finally
        {
            fileLogOpen = false;
            writer = null;
            fileLogException = false;
        }
    }
    
    protected boolean isRun()
    {
        return logIsRun;
    }

    protected void open() throws IOException
    {
        if( fileLogOpen && writer != null )
        {
            return;
        }
        File logFile = getSDCardFile(logFileName+getDatetime()+".log");
            
        if (logFile != null) 
        {
            Log.i("AndroidTrackingLogger", "getSDCardFile!!!  "+logFile.getPath());
            if ((!(logFile.exists())) && 
                    (!(logFile.createNewFile())))
            {
                Log.e("AndroidTrackingLogger", "Unable to create new log file");
            }
            Log.i("AndroidTrackingLogger", "create file!!!");
            FileOutputStream fileOutputStream = new FileOutputStream(logFile, append);
            Log.i("AndroidTrackingLogger", "fileOutputStream!!!");
            if (fileOutputStream != null) {
                writer = new PrintWriter(fileOutputStream);
                fileLogOpen = true;
            } else {
                Log.e("AndroidTrackingLogger", "Failed to create the log file");
            }
        }
        else
        {
            Log.i("AndroidTrackingLogger", "getSDCardFile is null !!! ");
        }
    }
    
    private File getSDCardFile(String fileName) 
    {
        String externalStorageState = Environment.getExternalStorageState();
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = null;

        if ((externalStorageState.equals("mounted")) && (externalStorageDirectory != null))
        {
            File dir = new File(externalStorageDirectory, logFileDir);
            if( !dir.exists() )
            {
                dir.mkdir();
            }
            file = new File(dir, fileName);
        }

        if (file == null) {
            Log.e("AndroidTrackingLogger", "Unable to open log file from external storage");
        }

        return file;
    }
    
    private String getDatetime()
    {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date(System.currentTimeMillis()));

      return (calendar.get(Calendar.YEAR) + "" +
              toFormat(calendar.get(Calendar.MONTH) + 1)) + 
              toFormat(calendar.get(Calendar.DAY_OF_MONTH)) +
              toFormat(calendar.get(Calendar.HOUR_OF_DAY)) +
              toFormat(calendar.get(Calendar.MINUTE)) + 
              toFormat(calendar.get(Calendar.SECOND));
    }
    
    private String toFormat(int time)
    {
        String format = "";
        if( time < 10 )
        {
            format += "0";
        }
        format += time;
        return format;
    }

}
