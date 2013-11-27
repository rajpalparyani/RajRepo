/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidUtil.java
 *
 */
package com.telenav.sdk.kontagent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import com.telenav.logger.Logger;

/**
 * @author jyxu
 * @date 2013-5-8
 */

public class KontagentLocalLogger
{
    public static int FIELD_WIDTH = 15;
    public static int BIG_FIELD_WIDTH = 25;
    
    protected final String logFileDir = "kontagentLog";

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
            Log.e("KontagentTrackingLogger", " Can't log to file. Reason: " + e.toString(), e);
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
        
        File logFile = getSDCardFile(getDatetime()+"_"+KontagentLogger.getInstance().getSenderId() + ".log");

        if (logFile != null)
        {
            Log.i("KontagentTrackingLogger", "getSDCardFile!!!  " + logFile.getPath());
            if ((!(logFile.exists())) && (!(logFile.createNewFile())))
            {
                Log.e("KontagentTrackingLogger", "Unable to create new log file");
            }
            
            Log.i("KontagentTrackingLogger", "create file!!!");
            FileOutputStream fileOutputStream = new FileOutputStream(logFile, append);
            
            Log.i("KontagentTrackingLogger", "fileOutputStream!!!");
            if (fileOutputStream != null)
            {
                writer = new PrintWriter(fileOutputStream);
                fileLogOpen = true;
            }
            else
            {
                Log.e("KontagentTrackingLogger", "Failed to create the log file");
            }
        }
        else
        {
            Log.i("KontagentTrackingLogger", "getSDCardFile is null !!! ");
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
            Log.e("KontagentTrackingLogger", "Unable to open log file from external storage");
        }

        return file;
    }

    protected String getKtEventLogTitle()
    {
        StringBuffer logBuffer = new StringBuffer();

        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,"category(st1)"));
        logBuffer.append("  ");

        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,"event(st2)"));
        logBuffer.append("  ");

        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,"subEvent(st3)"));
        logBuffer.append("  ");
        
        logBuffer.append(alignCenterWithSpace(BIG_FIELD_WIDTH,"eventName(n)"));
        logBuffer.append("  ");

        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,"level"));
        logBuffer.append("  ");

        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,"timestamp(ts)"));
        logBuffer.append("  ");
        
        return logBuffer.toString();
    }
    
    protected String getKtEventLog(String category, String event, String subEvent, String eventName, int level, int v)
    {
        StringBuffer logBuffer = new StringBuffer();

        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,category != null ? category:"--"));
        logBuffer.append("  ");

        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,event != null ? event:"--"));
        logBuffer.append("  ");
        
        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,subEvent != null ? subEvent:"--"));
        logBuffer.append("  ");
        
        logBuffer.append(alignCenterWithSpace(BIG_FIELD_WIDTH,eventName != null ? eventName:"--"));
        logBuffer.append("  ");
              
        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,level > 0 ? ""+level:"--"));
        logBuffer.append("  ");
        
        logBuffer.append(alignCenterWithSpace(FIELD_WIDTH,""+System.currentTimeMillis()));
        logBuffer.append("  ");
        return logBuffer.toString();
    }

    private String alignCenterWithSpace(int width, String str)
    {
        if(str == null || str.length() == 0)
        {
            return "";
        }
        if(str.length() > width)
        {
            return str;
        }
        int strLen = str.length();
        int leftSpaceWidth = (width-strLen)/2;
        int rightSpaceWidth = width - strLen - leftSpaceWidth;
        StringBuffer buffer = new StringBuffer();
        for(int i=0; i<leftSpaceWidth; i++)
        {
            buffer.append(' ');
        }
        buffer.append(str);
        for(int i=0; i<rightSpaceWidth; i++)
        {
            buffer.append(' ');
        }
        return buffer.toString();
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
