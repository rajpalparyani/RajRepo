/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2sePersistentManager.java
 *
 */
package com.telenav.persistent.j2se;

import java.io.File;
import java.io.IOException;

import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.TnFileConnection;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 * Provides access to the device's persistent information at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
public class J2sePersistentManager extends TnPersistentManager
{
    protected String baseDir;
    
    protected String fileBaseDir;
    protected String sdCardBaseDir;
    protected String rmsBaseDir;
    
    /**
     * Construct the persistent manager at j2se platform.
     * 
     * @param baseDir The base directory of whole application.
     */
    public J2sePersistentManager(String baseDir)
    {
        this.baseDir = baseDir;
        
        this.fileBaseDir = this.baseDir + "/" + "file";
        this.sdCardBaseDir = this.baseDir + "/" + "sdcard";
        this.rmsBaseDir = this.baseDir + "/" + "rms";
        J2seFilePersistentUtil.mkDir(this.fileBaseDir);
        J2seFilePersistentUtil.mkDir(this.sdCardBaseDir);
        J2seFilePersistentUtil.mkDir(this.rmsBaseDir);
        
        J2seRmsPersistentUtil.setBaseRmsDir(this.rmsBaseDir);
    }
    
    protected TnStore createStoreDelegate(int storeType, String name)
    {
        TnStore store = null;

        switch (storeType)
        {
            case DATABASE:
            {
                store = new J2seRmsStore(name, storeType, ((IJ2sePersistentContext) this.getPersistentContext())
                    .createPersistentObject(IJ2sePersistentContext.TYPE_VECTOR));
                break;
            }
            case RMS_CHUNK:
            {
                store = new J2seFileStore(this.fileBaseDir, name, storeType, ((IJ2sePersistentContext) this.getPersistentContext())
                    .createPersistentObject(IJ2sePersistentContext.TYPE_VECTOR));
                break;
            }
            case RMS_CRUMB:
            {
                store = new J2seRmsStore(name, storeType, ((IJ2sePersistentContext) this.getPersistentContext())
                        .createPersistentObject(IJ2sePersistentContext.TYPE_HASHTABLE));
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
                rootStorage = new File(this.sdCardBaseDir);
                break;
            }
            case FILE_SYSTEM_INTERNAL:
            {
                rootStorage = new File(this.fileBaseDir);
                break;
            }
        }
        
        if(rootStorage == null)
        {
            throw new IOException("root storage is null.");
        }
        
        String directoryUrl = rootStorage.getAbsolutePath() + "/" + directory;
        
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
        
        return new J2seFileConnection(file);
    }

    public void addExternalFileListener(ITnExternalStorageListener listener)
    {
        
    }

    public int getExternalStorageState()
    {
        return MEDIA_MOUNTED;
    }

    public void removeExternalFileListener(ITnExternalStorageListener listener)
    {
        
    }

    public void removeAllExternalFileListeners()
    {
        
    }
}
