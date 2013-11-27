/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnPersistentManager.java
 *
 */
package com.telenav.persistent;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Provides access to the device's persistent information.
 * <br />
 * For different platform, and different persistent type, maybe you need use different {@link ITnPersistentContext}.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public abstract class TnPersistentManager
{
    /**
     * Store the data into RMS system. At android, it's file system, At RIM, it's persistent system etc.
     * <br />
     * <br />
     * This RMS type will store the crumb data, every data object should < 1k, and the whole store data size should < 10k.
     * <br />
     * will cache every data into memory, so the speed is faster than any other store type.
     */
    public final static int RMS_CRUMB = 0;

    /**
     * Store the data into RMS system. At android, it's file system, At RIM, it's persistent system etc.
     * <br />
     * <br />
     * This RMS type will store the chunk data, every data object should > 1k, and the whole store data size should > 10k.
     */
    public final static int RMS_CHUNK = 1;
    
    /**
     * Store the data into database. Currently native API 1.0+ support this at android platform, but native API 5.0+ support this at RIM platform.
     * <br />
     * <br />
     * Suggest that if the data size is not very large, please use {@link #RMS_CRUMB}
     */
    public final static int DATABASE = 2;

    /**
     * The sdcard file system.
     */
    public final static int FILE_SYSTEM_EXTERNAL = 0;
    
    /**
     * The RAM file system.
     */
    public final static int FILE_SYSTEM_INTERNAL = 1;
    
    public final static int MEDIA_MOUNTED = 0;
    public final static int MEDIA_MOUNTED_READ_ONLY = 1;
    public final static int MEDIA_UNMOUNTED = 2;
    public final static int MEDIA_CHECKING = 3;
     
    private static TnPersistentManager persistentManager;
    private static int initCount;
    
    private static ITnPersistentContext persistentContext;

    /**
     * Retrieve the instance of persistent manager.
     * 
     * @return {@link TnPersistentManager}
     */
    public static TnPersistentManager getInstance()
    {
        return persistentManager;
    }

    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param persistentMngr This manager is native manager of platforms. Such as {@link AndroidPersistentManager} etc.
     * @param persistentCtxt This will provide some necessary information of persistent object at this platform.
     */
    public synchronized static void init(TnPersistentManager persistentMngr, ITnPersistentContext persistentCtxt)
    {
        if(initCount >= 1)
            return;
        
        persistentManager = persistentMngr;
        persistentContext = persistentCtxt;
        initCount++;
    }

    /**
     * Get current persistent context object.
     * 
     * @return {@link ITnPersistentContext}
     */
    public final ITnPersistentContext getPersistentContext()
    {
        return persistentContext;
    }

    /**
     * create a store object according to the store type and name.
     * 
     * @param storeType using {@link #RMS_STORE}, {@link #DATABASE_STORE}, {@link #SDCARD_STORE} etc.
     * @param name the name of store object.
     * @return {@link TnStore}
     */
    public final TnStore createStore(int storeType, String name)
    {
        if (storeType < 0 || storeType > 2)
        {
            throw new IllegalArgumentException("Doesn't support this store type currently.");
        }
        else if(name == null || name.trim().length() == 0)
        {
            throw new IllegalArgumentException("The store name is empty.");
        }
        
        return createStoreDelegate(storeType, name);
    }

    /**
     * the delegate of createStore() method.
     * 
     * @param storeType using {@link #RMS_STORE}, {@link #DATABASE_STORE}, {@link #SDCARD_STORE} etc.
     * @param name the name of store object.
     * @return {@link TnStore}
     */
    protected abstract TnStore createStoreDelegate(int storeType, String name);
    
    /**
     * File connection is different from other Generic Connection Framework connections in that a connection object can
     * be successfully returned from the TnPersistentManager.openFileConnection() method without actually referencing an existing entity (in
     * this case, a file or directory). This behavior allows the creation of new files and directories on a file system.
     * 
     * @param directory the path of directory.
     * @param fileName the file's name.
     * @param fileSystem {@link #FILE_SYSTEM_EXTERNAL}, {@link #FILE_SYSTEM_INTERNAL} etc.
     * @return a file connection
     * 
     * @throws IOException an IO exception
     */
    public abstract TnFileConnection openFileConnection(String directory, String fileName, int fileSystem) throws IOException;
    
    /**
     * Gets the current state of the external storage device.
     * 
     * @return {@link #MEDIA_MOUNTED}, {@link #MEDIA_MOUNTED_READ_ONLY}, {@link #MEDIA_UNMOUNTED}
     */
    public abstract int getExternalStorageState();
    
    /**
     * This method is used to register a ITnExternalStorageListener that is notified in case of adding and removing a new file system root.
     * @param listener storage listener
     */
    public abstract void addExternalFileListener(ITnExternalStorageListener listener);
    
    /**
     * This method is used to unregister a ITnExternalStorageListener that is notified in case of adding and removing a new file system root.
     * @param listener storage listener
     */
    public abstract void removeExternalFileListener(ITnExternalStorageListener listener);
    
    /**
     * remove the whole ITnExternalStorageListener.
     */
    public abstract void removeAllExternalFileListeners();
}
