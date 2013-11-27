/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AndroidSdLogUploader.java
 *
 */
package com.telenav.app.android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.telenav.app.TnSdLogCollector;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;

/**
 *@author yning
 *@date 2012-7-2
 */
public class AndroidSdLogCollector extends TnSdLogCollector
{
    private Process process;
    public byte[] collectClientLogger()
    {
        ByteArrayOutputStream allLogOutputStream = new ByteArrayOutputStream();
        try
        {
            InputStream logcatStream = getLogcatLogStream();
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
                    allLogOutputStream));
            zipStream(logcatStream, zos, "logcat.txt");
           
        
            String[] sentLogName = new String[2];
            TnFileConnection[] fileConnections = getTwoOfTwoDayLatestLog(sentLogName);
            if (fileConnections != null)
            {
                for (int i = 0; i < fileConnections.length; i++)
                {
                    InputStream zipInput = null;
                    if(fileConnections[i] != null)
                    {
                        zipInput = fileConnections[i].openInputStream();
                    }
                    zipStream(zipInput, zos, sentLogName[i] + ".txt");
                }
            }
            zos.close();
        }
        catch (IOException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        //saveZippedDataToSDforTest(allLogOutputStream.toByteArray());
        return allLogOutputStream.toByteArray();

    }
    
     private void saveZippedDataToSDforTest(byte[] logArray)
    {
        try
        {
            byte[] buffer = new byte[4096];
            TnPersistentManager manager = TnPersistentManager.getInstance();

            if (manager.getExternalStorageState() != TnPersistentManager.MEDIA_MOUNTED)
                return ;

            int storageType = TnPersistentManager.FILE_SYSTEM_EXTERNAL;

            String recordPath = FileStoreManager.TELENAV_DIRECTORY_PATH + "logzip";
            TnFileConnection recordDir = manager.openFileConnection(recordPath, "logzip"
                    + "", storageType);

            if (!recordDir.exists())
            {
                recordDir.mkdirs();
            }

            recordDir.close();

            TnFileConnection file = TnPersistentManager.getInstance().openFileConnection(
                recordPath, "log.zip", storageType);

            if (!file.exists())
                file.create();

            OutputStream os = file.openOutputStream();
            os.write(logArray);

            os.close();
            file.close();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(logArray);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
                    byteArrayInputStream));

            ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
            int count2;
            while ((count2 = zis.read(buffer, 0, buffer.length)) != -1)
            {
                byteArrayOutStream.write(buffer, 0, count2);
            }
           
        }
        catch (IOException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }
    
    private InputStream getLogcatLogStream()
    {
        try
        {
            FutureTask<Process> task = new FutureTask<Process>(new Callable<Process>()
            {
                public Process call() throws Exception
                {
                    Logger.log(Logger.INFO, AndroidSdLogCollector.class.getName(), " Before get logcat log ");
                    process = Runtime.getRuntime().exec("logcat -d -v time");
                    Logger.log(Logger.INFO, AndroidSdLogCollector.class.getName(), " Get logcat log succeed");
                    return process;
                }
            });
             
            try
            {
                Executors.newSingleThreadExecutor().execute(task);
                process = task.get(5, TimeUnit.SECONDS);
                
            }
            catch (Throwable t)
            {
                task.cancel(true);
                if(process != null)
                process.destroy();
                Logger.log(Logger.INFO, AndroidSdLogCollector.class.getName(), " Get logcat log failed in 20 seconds ");
                return null;
            }
          
            return process.getInputStream();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            
        }
        return null;
    }
    
    
    private void zipStream(InputStream zipInput, ZipOutputStream zipOutput,String filename) 
    {
        if(zipInput == null || zipOutput == null || filename == null)
            return;
        
        BufferedInputStream bis = new BufferedInputStream(zipInput);
       
        ZipEntry entry = new ZipEntry(filename);
        try
        {
            zipOutput.putNextEntry(entry);
            byte[] buffer = new byte[4096];
            int count;
            while ((count = bis.read(buffer, 0, buffer.length)) != -1)
            {
                zipOutput.write(buffer, 0, count);
            }
            //zipOutput.close();
        }
        catch (IOException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            try
            {
                zipOutput.closeEntry();
                bis.close();
            }
            catch (IOException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    } 
    
    public TnFileConnection[] getTwoOfTwoDayLatestLog(String[] sentLogName)
    {
        TnPersistentManager manager = TnPersistentManager.getInstance();

        if (manager.getExternalStorageState() != TnPersistentManager.MEDIA_MOUNTED)
            return null;

        int storageType = TnPersistentManager.FILE_SYSTEM_EXTERNAL;

        String recordPath = FileStoreManager.TELENAV_DIRECTORY_PATH;
        try
        {
            TnFileConnection[] fileConnections = new TnFileConnection[2]; 
            TnFileConnection recordDir = manager.openFileConnection(recordPath,
                AndroidLoggerListener.LOG_FILE_DIR, storageType);
            if(recordDir == null)
            {
                return fileConnections;
            }
            String[] recordsMonths = recordDir.list();
            Vector allDaysLog = new Vector();
            if (recordsMonths != null)
            {
                for (int i = 0; i < recordsMonths.length; i++)
                {
                    TnFileConnection recordMonthDir = manager.openFileConnection(
                        recordPath + AndroidLoggerListener.LOG_FILE_DIR,
                        recordsMonths[i], storageType);
                    String[] recordsDays = recordMonthDir.list();
                    if (recordsDays != null)
                    {
                        for (int j = 0; j < recordsDays.length; j++)
                        {
                            TnFileConnection recordDayDir = manager.openFileConnection(
                                recordPath + AndroidLoggerListener.LOG_FILE_DIR
                                        + recordsMonths[i], recordsDays[j], storageType);
                            String[] recordsHours = recordDayDir.list();
                            if (recordsHours != null)
                            {
                                for (int k = 0; k < recordsHours.length; k++)
                                {
                                    try
                                    {
                                        if (recordsHours[k].length() == 14)
                                        {
                                            String yyyymmddhhString = recordsHours[k]
                                                    .substring(0, 10);
                                            Long yyyymmddhh = Long
                                                    .parseLong(yyyymmddhhString);
                                            allDaysLog.add(yyyymmddhh);
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        Logger.log(this.getClass().getName(), e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort(allDaysLog,Collections.reverseOrder());
           
            Vector sendLogNames = new Vector();
            if(allDaysLog !=null && allDaysLog.size()>0)
            {
                Long latestLogTime = (Long)allDaysLog.elementAt(0);
                String latestLogTimeString = latestLogTime.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
                try
                {
                    Date lastestLogDate = sdf.parse(latestLogTimeString);
                    Long lastestLogDateTimeStamp = lastestLogDate.getTime();
                    if ((System.currentTimeMillis() - lastestLogDateTimeStamp) > 3600 * 48 * 1000)
                    {
                        return null;
                    }

                }catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
            for (int i = 0; i < allDaysLog.size(); i++)
            {
                if (i == 2)
                    break;
                sendLogNames.add(allDaysLog.elementAt(i).toString());
                
            }
            for (int i = 0; i < sendLogNames.size(); i++)
            {
                String monthString = ((String) (sendLogNames.elementAt(i)))
                        .substring(4, 6);
                String dayString = ((String) (sendLogNames.elementAt(i))).substring(6, 8);
                
                sentLogName[i] = (String) (sendLogNames.elementAt(i));
                int monthInt = Integer.parseInt(monthString);
                int dayInt = Integer.parseInt(dayString);
                fileConnections[i] = manager.openFileConnection(recordPath
                        + AndroidLoggerListener.LOG_FILE_DIR + monthInt + "/" + dayInt,
                    sendLogNames.elementAt(i) + ".txt", storageType);
            }
            return fileConnections;
            
        }
        catch (IOException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return null;
    }
}
