/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * CrashfileCheckJob.java
 *
 */
package com.telenav.module.entry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.telenav.app.CommManager;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.comm.Host;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IClientLoggingProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.module.UserProfileProvider;
import com.telenav.threadpool.IJob;

/**
 *@author yren
 *@date 2013-7-17
 */
public class CrashfileCollectingJob implements IJob, IServerProxyListener
{
    protected boolean isRunning;

    protected boolean isCancelled;

    DmpFilenameFilter fileFilter = new DmpFilenameFilter();
    
    File[] files;
    
    private int index = -1;    
    
    private void uploadNextFile()
    {
        index++;
        
        if(index > files.length - 1)
        {
            isRunning = false;
            return;
        }
        // Send fileBinary to CServer
        IUserProfileProvider userProfileProvider = new UserProfileProvider();
        String fileName = files[index].getName().split(".dmp")[0];
        IClientLoggingProxy clientLoggingProxy = ServerProxyFactory.getInstance().createClientLoggingProxy(null,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(files[index]);
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (fis == null)
        {
            uploadNextFile();
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] tempBytes = new byte[1024];
        int count;
        try
        {
            while ((count = fis.read(tempBytes, 0, 1024)) != -1)
            {
                baos.write(tempBytes, 0, count);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] clientLogBytes = baos.toByteArray();
        clientLoggingProxy.sendClientLogging(clientLogBytes, fileName, "dmp");
    }
    
    public void execute(int handlerID)
    {
        isRunning = true;
        Context context = AndroidPersistentContext.getInstance().getContext();
        File dmpDic = context.getCacheDir();
        files = dmpDic.listFiles(fileFilter);
        if (files != null && files.length > 0 && NetworkStatusManager.getInstance().isConnected())
        {
            uploadNextFile();
        }
        else
        {
            isRunning = false;
            index = -1;
        }
    }

    public void cancel()
    {
        isCancelled = true;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        // TODO Auto-generated method stub
        return isRunning;
    }

    private class DmpFilenameFilter implements FilenameFilter
    {
        public boolean accept(File dir, String filename)
        {
            if (filename.endsWith(".dmp"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof IClientLoggingProxy)
        {
            Throwable t = new Throwable("Native Crashed!!! File Name --- " + files[index].getName());
            Crashlytics.logException(t);
            files[index].delete();
            uploadNextFile();
        }
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        // TODO Auto-generated method stub

    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if (proxy instanceof IClientLoggingProxy)
        {
            uploadNextFile();
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IClientLoggingProxy)
        {
            uploadNextFile();
        }
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }
}
