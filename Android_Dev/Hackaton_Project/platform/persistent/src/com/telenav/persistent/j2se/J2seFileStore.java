/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seFileStore.java
 *
 */
package com.telenav.persistent.j2se;

import java.util.Vector;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.j2se.IJ2sePersistentContext.IJ2sePersistentObject;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
class J2seFileStore extends TnStore
{
    protected String baseDir;
    protected IJ2sePersistentObject persistentObject;
    
    public J2seFileStore(String baseDir, String name, int type, IJ2sePersistentObject persistentObject)
    {
        super(name, type);
        
        this.baseDir = baseDir;
        this.persistentObject = persistentObject;
    }

    protected void clearDelegate()
    {
        J2seFilePersistentUtil.deleteDir(getFileBaseStoreName());
        J2seRmsPersistentUtil.deleteData(getRmsBaseStoreName());
    }

    protected byte[] getDelegate(String id)
    {
        return J2seFilePersistentUtil.readData(getFileBaseStoreName() + "/" + id + ".bin");
    }

    protected void loadDelegate()
    {
    	IJ2sePersistentObject obj = J2seRmsPersistentUtil.loadData(getRmsBaseStoreName());
        if (obj != null)
        {
        	persistentObject = obj;
            ids = (Vector)persistentObject.getContents();
        }
    }

    protected void putDelegate(String id, byte[] data)
    {
    	// make sure directory is already created before save data to a new binary file
    	J2seFilePersistentUtil.mkDir(getFileBaseStoreName());
    	
        J2seFilePersistentUtil.saveData(getFileBaseStoreName() + "/" + id + ".bin", data);
    }

    protected void removeDelegate(String id)
    {
        J2seFilePersistentUtil.deleteData(getFileBaseStoreName() + "/" + id + ".bin");
    }

    protected void saveDelegate()
    {
        persistentObject.setContents(this.ids);
        J2seRmsPersistentUtil.storeData(getRmsBaseStoreName(), persistentObject);
    }

    private String getFileBaseStoreName()
    {
        return baseDir + "/" + TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_" + this.name;
    }
    
    private String getRmsBaseStoreName()
    {
    	return TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_" + this.name;
    }
}
