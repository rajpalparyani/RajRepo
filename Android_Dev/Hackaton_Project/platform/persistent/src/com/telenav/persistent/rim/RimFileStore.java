/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimFileStore.java
 *
 */
package com.telenav.persistent.rim;

import java.util.Vector;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.rim.IRimPersistentContext.IRimPersistentObject;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimFileStore extends TnStore
{
    protected IRimPersistentObject persistentObject;
    protected long storeIndexKey;
    
    public RimFileStore(String name, int type, IRimPersistentObject persistentObject)
    {
        super(name, type);
        
        this.persistentObject = persistentObject;
        this.storeIndexKey = RimRmsPersistentUtil.stringToLong(getBaseStoreName() + ".index");
    }

    protected void clearDelegate()
    {
        RimFilePersistentUtil.deleteData(getBaseStoreName());

        RimRmsPersistentUtil.deleteData(storeIndexKey);
    }

    protected byte[] getDelegate(String id)
    {
        return RimFilePersistentUtil.readData(getBaseStoreName() + "/" + id + ".bin");
    }

    protected void loadDelegate()
    {
        RimFilePersistentUtil.setBaseDir(RimPersistentManager.RIM_INTERNAL_FILE_PATH);
        RimFilePersistentUtil.mkDir(TnPersistentManager.getInstance().getPersistentContext().getApplicationName());
        RimFilePersistentUtil.setBaseDir(RimPersistentManager.RIM_INTERNAL_FILE_PATH + TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "/");
        RimFilePersistentUtil.mkDir(getBaseStoreName());
        
        IRimPersistentObject tmpPersistentObject = RimRmsPersistentUtil.loadData(this.storeIndexKey);
        if(tmpPersistentObject != null)
        {
            this.persistentObject = tmpPersistentObject;
        }
        if (this.persistentObject != null)
        {
            this.ids = (Vector) this.persistentObject.getContents();
        }
    }

    protected void putDelegate(String id, byte[] data)
    {
        RimFilePersistentUtil.saveData(getBaseStoreName() + "/" + id + ".bin", data);
    }

    protected void removeDelegate(String id)
    {
        RimFilePersistentUtil.deleteData(getBaseStoreName() + "/" + id + ".bin");
    }

    protected void saveDelegate()
    {
        this.persistentObject.setContents(this.ids);
        RimRmsPersistentUtil.storeData(this.storeIndexKey, this.persistentObject);
    }

    private String getBaseStoreName()
    {
        return TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_" + this.name;
    }
}
