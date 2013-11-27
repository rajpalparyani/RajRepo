package com.telenav.app.android;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Debug;

import com.telenav.logger.ILoggerListener;
import com.telenav.logger.Logger;
import com.telenav.telephony.TnBatteryManager;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;

public class RuntimeStatusLogger extends AndroidLoggerListener implements ILoggerListener, INotifierListener
{
    public static final int DEFAULT_RUNTIME_LOG_INTERVAL =  10 * 1000;
    
    protected long logInterval = DEFAULT_RUNTIME_LOG_INTERVAL;

    public static String RUNTIME_STATUS_INFO = "Runtime status info: ";
    
    private BufferedOutputStream bufeBufferedOutputStream = null;
    
    private FileOutputStream fileOutputStream = null;
    
    protected long lastNotifyTimestamp = -1L;

    String path = "";
    
    private static RuntimeStatusLogger instance = new RuntimeStatusLogger();
    
    public static RuntimeStatusLogger getInstance()
    {
        return instance;
    }
    
    public void start(long timeInterval)
    {
        if(timeInterval <= 0)
        {
            timeInterval = DEFAULT_RUNTIME_LOG_INTERVAL;
        }
        this.logInterval = timeInterval;
        Logger.log(Logger.INFO, RuntimeStatusLogger.RUNTIME_STATUS_INFO, "Runtime status start :" + timeInterval);
        Notifier.getInstance().addListener(this);
    }
    
    public void stop()
    {
        Notifier.getInstance().removeListener(this);
    }
    
    private RuntimeStatusLogger()
    {
        super();
        path = AndroidPersistentContext.getInstance().getContext().getCacheDir().getPath();
        File logFile = new File(path + "//" + "MemoryInfo-" + System.currentTimeMillis() + ".txt");
        try
        {
            logFile.delete();
            if(!logFile.exists())
            {
                logFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(logFile);
            bufeBufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        }
        catch (Exception e)
        {
        }
    }

    public boolean isLoggable(int level, String clazz, String message,Throwable t, Object[] params) 
    {
        return (level == Logger.WARNING && clazz.equals(RUNTIME_STATUS_INFO));
    }

    public void log(int level, String clazz, String message, Throwable t,Object[] params) 
    {
        logInfo(message);
    }
    
    private void logInfo(String message)
    {
        try
        {
            bufeBufferedOutputStream.write((System.currentTimeMillis() + ":" + message + "\r\n").getBytes());
            bufeBufferedOutputStream.flush();
        }
        catch (IOException e)
        {
        }
    }
    
    public void close()
    {
        try
        {
            bufeBufferedOutputStream.close();
            fileOutputStream.close();
            File logFile = new File(path + "//" + "MemoryInfo-" + System.currentTimeMillis() + ".txt");
            logFile.delete();
            if (!logFile.exists())
            {
                logFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(logFile);
            bufeBufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        }
        catch (IOException e)
        {
        }
    }
    
    public void printMemoryInfo()
    {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        long time = System.currentTimeMillis();
        int totalMemory = (memoryInfo.getTotalPrivateDirty()>>10) + (memoryInfo.getTotalSharedDirty()>>10);
        int nativeMemory = (int) (Debug.getNativeHeapAllocatedSize()>>20);
        Logger.log(Logger.INFO, RuntimeStatusLogger.RUNTIME_STATUS_INFO, "Runtime status(" + time + ")-Battery: " + TnBatteryManager.getInstance().getBatteryLevel());
        Logger.log(Logger.INFO, RuntimeStatusLogger.RUNTIME_STATUS_INFO, "Runtime status(" + time + ")-Memory: " + totalMemory + " MB(Native:" + nativeMemory + "MB)");
    }

    

    @Override
    public long getNotifyInterval()
    {
        return logInterval;
    }

    @Override
    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    @Override
    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestamp = timestamp;
    }

    @Override
    public void notify(long timestamp)
    {
        this.printMemoryInfo();
    }
}
