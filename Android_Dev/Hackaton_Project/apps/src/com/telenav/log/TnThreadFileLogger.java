package com.telenav.log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.logger.ILoggerListener;
import com.telenav.logger.Logger;
import com.telenav.logger.LoggerFilter;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;

public class TnThreadFileLogger extends LoggerFilter implements ILoggerListener, ILogConstants
{
    
    // a flag to indicate to save log to file system immediately
    public static final Object[] SAVE_LOG_IMMEDIATELY      = new Object[0];       

    public static String LOG_FILE_DIR                      = "/logs/"; 
    
    // default maximum log file size (try to avoid too much memory usage)
    public static int MAX_LOG_SIZE                         = 40 * 1024;   
    
    private static StringBuffer summaryLog                 = new StringBuffer();
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    protected String thread;
    
    protected int maxLogSize;
    
    protected StringBuffer log;
    
    public TnThreadFileLogger(String thread, int maxLogSize) 
    {
        super(Logger.INFO);
        
        this.thread = thread;
        this.maxLogSize = maxLogSize;
        buildLogBuffer();
    }
    
    protected void buildLogBuffer()
    {
        int size = maxLogSize > MAX_LOG_SIZE ? MAX_LOG_SIZE : maxLogSize + 1024;
        log = new StringBuffer(size);
    }

    /**
     * @see LoggerFilter :: isLoggable(..)
     */
    public boolean isLoggable(int level, String clazz, String message, Throwable t, Object[] params) 
    {
        if (clazz == RESUME_APPLICATION || clazz == LAUNCH_APPLICATION ||
            clazz == EXIT_APPLICATION   || clazz == HIDE_TO_BACKGROUND ||
            clazz.equals(thread))
            return true;
            
        return false;
    }

    /**
     * @see ILoggerListener :: log(..)
     */
    public void log(int level, String clazz, String message, Throwable t, Object[] params) 
    {
        boolean needSaveLog = (SAVE_LOG_IMMEDIATELY == params);
        if (clazz.equals(thread) && message != null && message.length() > 0)
        {    
            log.append(message + "\r\n");
        }
        
        if ((clazz == EXIT_APPLICATION ||
             clazz == HIDE_TO_BACKGROUND) && 
             thread.equals(LOG_SUMMARY))
        {
            log.append(summaryLog);
            summaryLog = new StringBuffer();
            
            needSaveLog = true;
        }
        else if (log.length() > maxLogSize || 
                 clazz.equals(EXIT_APPLICATION) ||
                 clazz.equals(HIDE_TO_BACKGROUND))
        {
            needSaveLog = true;
        }
        
        if (needSaveLog)
        {
            saveLog();
            buildLogBuffer();
        }
    }
    
    /**
     *  save log to file system
     */
    protected void saveLog()
    {
        try
        {
            if (log.length() == 0) return;
            
            TnPersistentManager manager = TnPersistentManager.getInstance();
            
            if(manager.getExternalStorageState() != TnPersistentManager.MEDIA_MOUNTED)
                return;
            
            int storageType = TnPersistentManager.FILE_SYSTEM_EXTERNAL;
            
            Calendar calenda = Calendar.getInstance();
            calenda.setTime(new Date());
            int month  = calenda.get(Calendar.MONTH) + 1;
            int date   = calenda.get(Calendar.DATE);
            int hour   = calenda.get(Calendar.HOUR);
            int minute = calenda.get(Calendar.MINUTE);
            int second = calenda.get(Calendar.SECOND);

            String recordPath = FileStoreManager.TELENAV_DIRECTORY_PATH + LOG_FILE_DIR  + thread + "/" +  month;
            TnFileConnection recordDir = 
                    manager.openFileConnection(recordPath, date + "", storageType);
             
            if (! recordDir.exists())
            {
                recordDir.mkdirs();     
            }
            
            recordDir.close();
            
            recordPath += ("/" + date);
            String fileName = hour + "-" + minute + "-" + second + ".txt";
            
            TnFileConnection file = 
                TnPersistentManager.getInstance().openFileConnection(recordPath, fileName, storageType);
            
            if (!file.exists()) file.create();
            
            OutputStream os = file.openOutputStream();
            os.write(log.toString().getBytes());
            
            os.close();
            file.close();
            
            if (! thread.equals(LOG_SUMMARY))
            {
                JSONObject o = new JSONObject();
                o.put(TAG_LOG_TYPE, LOG_TYPE_THREAD_RECORD);
                o.put(TAG_THREAD, thread);
                o.put(TAG_THREAD_FILE, recordPath + "/" + fileName);
                
                summaryLog.append(o.toString() + "\r\n");
            }
            
        }
        catch (IOException ex)
        {
            Logger.log(getClass().getName(), ex);
        }
        catch (JSONException e)
        {
            Logger.log(getClass().getName(), e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
}
