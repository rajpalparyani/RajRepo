/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidLoggerListener.java
 *
 */
package com.telenav.app.android;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Build;
import android.util.Log;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.logger.DefaultLoggerListener;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.telephony.TnTelephonyManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-27
 */
public class AndroidLoggerListener extends DefaultLoggerListener
{
    public static String LOG_FILE_DIR          = "/sdlogs/";    
    
    public void log(int level, String clazz, String message,  Throwable t, Object[] params)
    {
		if(!AppConfigHelper.isLoggerEnable && level < Logger.EXCEPTION)
            return;
			
        if (t != null)
        {
            t.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        String logMessage = getDate();

        switch (level)
        {
            case Logger.INFO:
                logMessage += INFO;
                break;
            case Logger.WARNING:
                logMessage += WARNING;
                break;
            case Logger.EXCEPTION:
                logMessage += EXCEPTION;
                break;
            case Logger.ERROR:
                logMessage += ERROR;
                break;
        }

        logMessage += "[" + getClass(clazz) + "]" + message;

        switch (level)
        {
            case Logger.INFO:
                logMessage += INFO;
                if (message != null && !("").equals(message.trim()))
                {//android.Log cannot print empty message(such as "")
                    Log.i("TELENAV_" + AppConfigHelper.mandatoryClientVersion + "." + AppConfigHelper.buildNumber, message);
                }
                break;
            case Logger.WARNING:
                logMessage += WARNING;
                if (message != null && !("").equals(message.trim()))
                {
                    Log.w("TELENAV_" + AppConfigHelper.mandatoryClientVersion + "." + AppConfigHelper.buildNumber, message);
                }
                break;
            case Logger.EXCEPTION:
                logMessage += EXCEPTION;
                if (message != null && !("").equals(message.trim()))
                {
                    Log.w("TELENAV_" + AppConfigHelper.mandatoryClientVersion + "." + AppConfigHelper.buildNumber, message);
                }
                break;
            case Logger.ERROR:
                logMessage += ERROR;
                if (message != null && !("").equals(message.trim()))
                {
                    Log.e("TELENAV_" + AppConfigHelper.mandatoryClientVersion + "." + AppConfigHelper.buildNumber, message);
                }
                break;
        }
        String outputToSdcardString = ("[" + "TELENAV_" + AppConfigHelper.mandatoryClientVersion + "." + AppConfigHelper.buildNumber + "] " + logMessage + "\n");
        if (AppConfigHelper.isOutputSdCardEnabled)
        {
            try
            {
                saveToSdCard(outputToSdcardString);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void saveToSdCard(String outputToSdcardString) throws Exception
    {
        TnPersistentManager manager = TnPersistentManager.getInstance();

        if (manager.getExternalStorageState() != TnPersistentManager.MEDIA_MOUNTED)
            return;

        int storageType = TnPersistentManager.FILE_SYSTEM_EXTERNAL;

        Date date = new Date(System.currentTimeMillis());
        int month = (date.getMonth() + 1);

        String recordPath = FileStoreManager.TELENAV_DIRECTORY_PATH + LOG_FILE_DIR + month;
        TnFileConnection recordDir = manager.openFileConnection(recordPath, date.getDate() + "", storageType);

        if (!recordDir.exists())
        {
            recordDir.mkdirs();
        }

        recordDir.close();

        recordPath += ("/" + date.getDate());
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        
//        String fileName = date.getHours() + ".txt";
        String fileName = sdf.format(date) + ".txt";

        TnFileConnection file = TnPersistentManager.getInstance().openFileConnection(recordPath, fileName, storageType);

        String logTitle = "";
        if (!file.exists())
        {
            file.create();
            logTitle = "Telenav build#" + AppConfigHelper.buildNumber + "\n";
            logTitle += "Android build#" + Build.VERSION.SDK + "\n";
            logTitle += "Android Firmware#" + Build.VERSION.RELEASE + "\n";
            logTitle += "Device Type#" + Build.MANUFACTURER + "-" + Build.MODEL + "\n";
            
            try {
                String phoneNumber = TnTelephonyManager.getInstance().getPhoneNumber();
                logTitle += "PTN#" + phoneNumber + "\n";
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            
            if (DaoManager.getInstance() != null && DaoManager.getInstance().getMandatoryNodeDao() != null
                    && DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode() != null)

            {
                if (DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo() != null)
                {
                    logTitle += "Encrypted PTN#" + DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().phoneNumber + "\n";
                }
            }
        }

        OutputStream os = file.openOutputStream(true);
        if(logTitle.trim().length() > 0)
        {
            os.write(logTitle.getBytes());
        }
        os.write(outputToSdcardString.getBytes());

        os.close();
        file.close();
    }
}
