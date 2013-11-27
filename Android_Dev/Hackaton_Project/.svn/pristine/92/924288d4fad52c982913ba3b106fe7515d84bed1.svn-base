/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimDatabaseStore.java
 *
 */
package com.telenav.persistent.rim;

import java.util.Vector;

import net.rim.device.api.database.Database;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.rim.IRimPersistentContext.IRimPersistentObject;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public class RimDatabaseStore extends TnStore
{
    protected IRimPersistentObject persistentObject;
    protected long storeIndexKey;
    
    public RimDatabaseStore(String name, int type, IRimPersistentObject persistentObject)
    {
        super(name, type);
        
        this.persistentObject = persistentObject;
        this.storeIndexKey = RimRmsPersistentUtil.stringToLong(getBaseStoreName() + ".index");
    }

    protected void clearDelegate()
    {
        RimDatabasePersistentUtil.deleteTable(getSQLiteDatabase(), this.name);
        
        RimRmsPersistentUtil.deleteData(storeIndexKey);
    }

    protected byte[] getDelegate(String id)
    {
        return RimDatabasePersistentUtil.readData(getSQLiteDatabase(), this.name, id);
    }

    protected void loadDelegate()
    {
        RimDatabasePersistentUtil.createTable(getSQLiteDatabase(), this.name);
        
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
        RimDatabasePersistentUtil.saveData(getSQLiteDatabase(), this.name, id, data);
    }

    protected void removeDelegate(String id)
    {
        RimDatabasePersistentUtil.deleteData(getSQLiteDatabase(), this.name, id);
    }

    protected void saveDelegate()
    {
        this.persistentObject.setContents(this.ids);
    }

    private String getBaseStoreName()
    {
        return TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_" + this.name;
    }
    
    private Database getSQLiteDatabase()
    {
        return ((IRimDatabasePersistentContext) TnPersistentManager.getInstance().getPersistentContext()).getApplicationSQLiteDatabase();
    }
}
