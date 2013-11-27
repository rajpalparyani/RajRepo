package com.telenav.app.android;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Vector;

import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.logger.ILoggerListener;
import com.telenav.logger.Logger;
import com.telenav.logger.LoggerFilter;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;

public class AndroidExceptionFileLogger extends LoggerFilter implements ILoggerListener
{
    public static String EXCEPTION_LOG_FILE_DIR          = "/logs/exception/"; 
    
    private Vector stackTraces = new Vector();
    
    public AndroidExceptionFileLogger()
    {
        super(Logger.EXCEPTION);
    }

    public boolean isLoggable(int level, String clazz, String message,Throwable t, Object[] params) 
    {
        return (level == Logger.EXCEPTION && t != null);
    }

    public void log(int level, String clazz, String message, Throwable t,Object[] params) 
    {
        logError(t);
    }
    
    private void logError(Throwable error)
    {
        try
        {
            StringWriter trace = new StringWriter();
            PrintWriter pw = new PrintWriter(trace);
            
            error.printStackTrace(pw);
            String stackTrace = trace.toString();
            pw.close();
            trace.close();
            
            // avoid to record the same kind of exception for a lot of times
            for (int i = 0; i < stackTraces.size(); i ++)
            {
                String err = (String)stackTraces.elementAt(i);
                if (err.equals(stackTrace))
                {
                    return;
                }
            }
            
            stackTraces.addElement(stackTrace);
            saveStackTrace(stackTrace, error);
        }
        catch (Exception ex)
        {

        }    
    }
    
    private void saveStackTrace(String stackTrace, Throwable error) throws Exception
    {
        TnPersistentManager manager = TnPersistentManager.getInstance();
        
        if(manager.getExternalStorageState() != TnPersistentManager.MEDIA_MOUNTED)
            return;
        
        int storageType = TnPersistentManager.FILE_SYSTEM_EXTERNAL;
        
        Date date= new Date(System.currentTimeMillis()); 
        int month = (date.getMonth() + 1);
         
        String recordPath = FileStoreManager.TELENAV_DIRECTORY_PATH + EXCEPTION_LOG_FILE_DIR  + month;
        TnFileConnection recordDir = 
                manager.openFileConnection(recordPath, date.getDate() + "", storageType);
         
        if (! recordDir.exists())
        {
            recordDir.mkdirs();     
        }
        
        recordDir.close();
        
        recordPath += ("/" + date.getDate());
        String fileName = date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() + ".txt";
        
        TnFileConnection file = 
            TnPersistentManager.getInstance().openFileConnection(recordPath, fileName, storageType);
        
        if (!file.exists()) file.create();
        
        OutputStream os = file.openOutputStream();
        os.write(stackTrace.getBytes());
        
        os.close();
        file.close();
    }
}
