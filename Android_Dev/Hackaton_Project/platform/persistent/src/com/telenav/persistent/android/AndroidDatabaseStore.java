/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidDatabaseStore.java
 *
 */
package com.telenav.persistent.android;

import android.database.sqlite.SQLiteDatabase;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
class AndroidDatabaseStore extends TnStore
{

    public AndroidDatabaseStore(String name, int type)
    {
        super(name, type);
    }

    protected void clearDelegate()
    {
        AndroidDatabasePersistentUtil.deleteTable(getSQLiteDatabase(), this.name);
    }

    protected byte[] getDelegate(String id)
    {
        return AndroidDatabasePersistentUtil.readData(getSQLiteDatabase(), this.name, id);
    }

    protected void loadDelegate()
    {
        AndroidDatabasePersistentUtil.createTable(getSQLiteDatabase(), this.name);
        this.ids = AndroidDatabasePersistentUtil.readIndexes(getSQLiteDatabase(), this.name);
    }
    
    protected void saveDelegate()
    {
        //no need to do any operation.
    }

    protected void putDelegate(String id, byte[] data)
    {
        AndroidDatabasePersistentUtil.saveData(getSQLiteDatabase(), this.name, id, data);
    }

    protected void removeDelegate(String id)
    {
        AndroidDatabasePersistentUtil.deleteData(getSQLiteDatabase(), this.name, id);
    }

    private SQLiteDatabase getSQLiteDatabase()
    {
        return ((IAndroidDatabasePersistentContext) TnPersistentManager.getInstance().getPersistentContext())
                .getApplicationSQLiteDatabase();
    }
}
