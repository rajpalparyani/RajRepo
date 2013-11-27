/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimPersistentManager.java
 *
 */
package com.telenav.persistent.rim;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemListener;
import javax.microedition.io.file.FileSystemRegistry;

import com.telenav.logger.Logger;
import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 * Provides access to the device's persistent information at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public class RimPersistentManager extends TnPersistentManager
{
    static final String RIM_INTERNAL_FILE_PATH = "file:///store/home/";
    static final String RIM_EXTERNAL_FILE_PATH = "file:///SDCard/";
    
    protected Hashtable fileListeners;
    
    /**
     * Construct the persistent manager at rim platform.
     * <br />
     * <br />
     * Please make sure that grant below class's permission.
     * <br />
     * ApplicationPermissions#PERMISSION_FILE_API;#PERMISSION_INTER_PROCESS_COMMUNICATION;
     * <br />
     * 
     */
    public RimPersistentManager()
    {
        fileListeners = new Hashtable();
    }
    
    protected TnStore createStoreDelegate(int storeType, String name)
    {
        TnStore store = null;

        switch (storeType)
        {
            case DATABASE:
            {
                store = new RimDatabaseStore(name, storeType, ((IRimDatabasePersistentContext) this.getPersistentContext())
                    .createPersistentObject(IRimDatabasePersistentContext.TYPE_VECTOR));
                break;
            }
            case RMS_CHUNK:
            {
                store = new RimFileStore(name, storeType, ((IRimDatabasePersistentContext) this.getPersistentContext())
                        .createPersistentObject(IRimDatabasePersistentContext.TYPE_VECTOR));
                break;
            }
            case RMS_CRUMB:
            {
                store = new RimRmsStore(name, storeType, ((IRimDatabasePersistentContext) this.getPersistentContext())
                        .createPersistentObject(IRimDatabasePersistentContext.TYPE_HASHTABLE));
                break;
            }
        }

        return store;
    }
    
    public TnFileConnection openFileConnection(String directory, String fileName, int fileSystem) throws IOException
    {
        String rootDirectory = null;
        switch(fileSystem)
        {
            case FILE_SYSTEM_EXTERNAL:
            {
                rootDirectory = RIM_INTERNAL_FILE_PATH;
                break;
            }
            case FILE_SYSTEM_INTERNAL:
            {
                rootDirectory = RIM_EXTERNAL_FILE_PATH;
                break;
            }
        }
        
        String newFileName = rootDirectory;
        if(directory != null && directory.length() > 0)
        {
            newFileName = newFileName + "/" + directory;
        }
        if(fileName != null && fileName.length() > 0)
        {
            newFileName = newFileName + "/" + fileName;
        }
        
        FileConnection fileConnection = (FileConnection)Connector.open(newFileName);
        
        if(fileConnection == null)
        {
            throw new IOException("can't create the file.");
        }
        
        return new RimFileConnection(fileConnection, rootDirectory, directory);
    }
    
    public void addExternalFileListener(ITnExternalStorageListener listener)
    {
        if (listener == null || fileListeners.containsKey(listener))
            return;

        FileSystemListener fileListener = new FileSystemListener()
        {
            public void rootChanged(int state, String rootName)
            {
                if (rootName.indexOf("SDCard") != -1)
                {
                    Enumeration keys = fileListeners.keys();
                    while(keys.hasMoreElements())
                    {
                        ITnExternalStorageListener storageListener = (ITnExternalStorageListener)keys.nextElement();
                        try
                        {
                            storageListener.updateStorageState(state == FileSystemListener.ROOT_ADDED ? MEDIA_MOUNTED : MEDIA_UNMOUNTED);
                        }
                        catch(Throwable e)
                        {
                            Logger.log(RimPersistentManager.class.getName(), e);
                        }
                    }
                }
            }
        };
        fileListeners.put(listener, fileListener);
        FileSystemRegistry.addFileSystemListener(fileListener);
    }

    public int getExternalStorageState()
    {
        Enumeration rootEnum = FileSystemRegistry.listRoots();
        while (rootEnum.hasMoreElements()) {
           String root = (String) rootEnum.nextElement();
           if(root.indexOf("SDCard") != -1)
           {
               return MEDIA_MOUNTED;
           }
        } 

        return MEDIA_UNMOUNTED;
    }

    public void removeExternalFileListener(ITnExternalStorageListener listener)
    {
        if(listener == null || !fileListeners.containsKey(listener))
            return;
        
        FileSystemListener fileListener = (FileSystemListener)fileListeners.remove(listener);
        FileSystemRegistry.removeFileSystemListener(fileListener);
    }

    public void removeAllExternalFileListeners()
    {
        Enumeration keys = fileListeners.keys();
        while(keys.hasMoreElements())
        {
            ITnExternalStorageListener storageListener = (ITnExternalStorageListener)keys.nextElement();
            removeExternalFileListener(storageListener);
        }
    }
}
