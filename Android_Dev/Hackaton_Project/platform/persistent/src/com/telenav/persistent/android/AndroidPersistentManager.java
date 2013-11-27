/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidPersistentManager.java
 *
 */
package com.telenav.persistent.android;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import com.telenav.logger.Logger;
import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 * Provides access to the device's persistent information at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public class AndroidPersistentManager extends TnPersistentManager
{
    static BroadcastReceiver mExternalStorageReceiver;
    protected Vector externalListeners;
    
    /**
     * Construct the persistent manager at android platform.
     */
    public AndroidPersistentManager()
    {
        externalListeners = new Vector();
    }
    
    protected TnStore createStoreDelegate(int storeType, String name)
    {
        TnStore store = null;
        
        switch(storeType)
        {
            case DATABASE:
            {
                store = new AndroidDatabaseStore(name, storeType);
                break;
            }
            case RMS_CHUNK:
            {
                store = new AndroidRmsStore(name, false, storeType);
                break;
            }
            case RMS_CRUMB:
            {
                store = new AndroidRmsStore(name, true, storeType);
                break;
            }
        }
        
        return store;
    }

    public TnFileConnection openFileConnection(String directory, String fileName, int fileSystem) throws IOException
    {
        File rootStorage = null;
        switch(fileSystem)
        {
            case FILE_SYSTEM_EXTERNAL:
            {
                rootStorage = Environment.getExternalStorageDirectory();
                break;
            }
            case FILE_SYSTEM_INTERNAL:
            {
                rootStorage = new File(((IAndroidPersistentContext)this.getPersistentContext()).getContext().getFilesDir().getAbsolutePath());
                break;
            }
        }
        
        if(rootStorage == null)
        {
            throw new IOException("root storage is null.");
        }
        
        String directoryUrl = rootStorage.getAbsolutePath() + "/" + directory;
        
        if(directory.length() > 0)
        {
            File folder = new File(directoryUrl);
            if(!folder.exists())
            {
                boolean isCreated = folder.mkdirs();
                if(!isCreated)
                {
                    throw new IOException("can't create the folder.");
                }
            }
        }
        
        File file = null;
        if(fileName != null && fileName.length() > 0)
        {
            file = new File(directoryUrl, fileName);
        }
        else
        {
            file = new File(directoryUrl);
        }
        
        if(file == null)
        {
            throw new IOException("can't create the file.");
        }
        return new AndroidFileConnection(file);
    }
    
    public int getExternalStorageState()
    {
        int tnState = MEDIA_UNMOUNTED;
        
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            tnState = MEDIA_MOUNTED;
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            tnState = MEDIA_MOUNTED_READ_ONLY;
        }
        else if(Environment.MEDIA_CHECKING.equals(state))
        {
        	tnState = MEDIA_CHECKING;
        }
        return tnState;
    }
    
    public synchronized void addExternalFileListener(ITnExternalStorageListener listener)
    {
        if (listener != null && !externalListeners.contains(listener))
        {
            externalListeners.addElement(listener);
        }

        if (mExternalStorageReceiver == null)
        {
            mExternalStorageReceiver = new BroadcastReceiver()
            {
                public void onReceive(Context context, Intent intent)
                {
                    updateExternalStorageState();
                }
            };
            
            final Context context = ((IAndroidPersistentContext) this.getPersistentContext()).getContext();
            if(context instanceof Activity)
            {
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        IntentFilter filter = new IntentFilter();
                        
                        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
                        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
                        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
                        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
                        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
                        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
                        filter.addAction(Intent.ACTION_MEDIA_EJECT);
                        filter.addDataScheme("file");
                        context.registerReceiver(mExternalStorageReceiver, filter);
                    }
                });
            }
            else
            {
                IntentFilter filter = new IntentFilter();
                
                filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
                filter.addAction(Intent.ACTION_MEDIA_CHECKING);
                filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
                filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
                filter.addAction(Intent.ACTION_MEDIA_REMOVED);
                filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
                filter.addAction(Intent.ACTION_MEDIA_EJECT);
                filter.addDataScheme("file");
                context.registerReceiver(mExternalStorageReceiver, filter);
            }
        }
    }
    
    public synchronized void removeExternalFileListener(ITnExternalStorageListener listener)
    {
        if(listener != null)
        {
            externalListeners.removeElement(listener);
        }
        
        if(mExternalStorageReceiver != null && externalListeners.isEmpty())
        {
            final Context context = ((IAndroidPersistentContext) this.getPersistentContext()).getContext();
            if(context instanceof Activity)
            {
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            context.unregisterReceiver(mExternalStorageReceiver);
                        }
                        catch(Throwable e)
                        {
                            Logger.log(AndroidPersistentManager.class.getName(), e);
                        }
                        mExternalStorageReceiver = null;
                    }
                });
            }
            else
            {
                try
                {
                    context.unregisterReceiver(mExternalStorageReceiver);
                }
                catch(Throwable e)
                {
                    Logger.log(AndroidPersistentManager.class.getName(), e);
                }
                mExternalStorageReceiver = null;
            }
        }
    }
    
    public synchronized void removeAllExternalFileListeners()
    {
        externalListeners.removeAllElements();
        removeExternalFileListener(null);
    }
    
    protected void updateExternalStorageState()
    {
        int tnState = getExternalStorageState();
        
        for(int i = 0; i < externalListeners.size(); i++)
        {
            ITnExternalStorageListener listener = (ITnExternalStorageListener)externalListeners.elementAt(i);
            try
            {
                listener.updateStorageState(tnState);
            }
            catch(Throwable e)
            {
                Logger.log(AndroidPersistentManager.class.getName(), e);
            }
        }
    }
}
