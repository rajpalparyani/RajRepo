/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidUtil.java
 *
 */
package com.telenav.log.mis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import android.os.Environment;
import android.util.Log;

import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.logger.Logger;

/**
 * @author jyxu
 * @date 2013-7-29
 */

public class LocalMislogLogger
{
    protected final String logFileDir = "misLog";

    private final boolean append = false;// close append log

    private boolean fileLogOpen = false;

    private boolean fileLogException = false;

    private PrintWriter writer = null;
    
    public void printToFile(String message)
    {
        try
        {
            if (!fileLogException)
            {
                open();
            }
        }
        catch (Exception e)
        {
            fileLogException = true;
            Log.e("MisTrackingLogger", " Can't log to file. Reason: " + e.toString(), e);
        }
        if ((fileLogOpen) && (writer != null))
        {
            writer.println(message);
        }
    }

    public void logMisLogMessage(AbstractMisLog log, String logTypeStr)
    {
        if(log == null)
            return;
        
        StringBuffer msg = new StringBuffer();
        msg.append(log.getType() + "(" + logTypeStr + ") "+ "timestamp="+getDatetime() + ";");
        Enumeration e = log.getAttributeKeys();
        if(e != null)
        {
            while(e.hasMoreElements())
            {
                long key = ((Long)e.nextElement()).longValue();
                String value = (String)log.getAttribute(key);
                msg.append(key+"="+value + "; ");
            }
        }
        printToFile(msg.toString());
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
        catch (Exception e)
        {
        }
        finally
        {
            fileLogOpen = false;
            writer = null;
            fileLogException = false;
        }
    }

    protected void open() throws IOException
    {
        if (fileLogOpen && writer != null)
        {
            return;
        }
        
        File logFile = getSDCardFile(getDatetime()+".log");

        if (logFile != null)
        {
            Log.i("MisTrackingLogger", "getSDCardFile!!!  " + logFile.getPath());
            if ((!(logFile.exists())) && (!(logFile.createNewFile())))
            {
                Log.e("MisTrackingLogger", "Unable to create new log file");
            }
            
            Log.i("MisTrackingLogger", "create file!!!");
            FileOutputStream fileOutputStream = new FileOutputStream(logFile, append);
            
            Log.i("MisTrackingLogger", "fileOutputStream!!!");
            if (fileOutputStream != null)
            {
                writer = new PrintWriter(fileOutputStream);
                fileLogOpen = true;
            }
            else
            {
                Log.e("MisTrackingLogger", "Failed to create the log file");
            }
        }
        else
        {
            Log.i("MisTrackingLogger", "getSDCardFile is null !!! ");
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
            if (!dir.exists())
            {
                dir.mkdir();
            }
            file = new File(dir, fileName);
        }

        if (file == null)
        {
            Log.e("MisTrackingLogger", "Unable to open log file from external storage");
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
        if (time < 10)
        {
            format += "0";
        }
        format += time;
        return format;
    }

}
