/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IAndroidDatabasePersistentContext.java
 *
 */
package com.telenav.persistent.android;

import android.database.sqlite.SQLiteDatabase;

/**
 * This class provider some necessary database information at android platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public interface IAndroidDatabasePersistentContext extends IAndroidPersistentContext
{
    /**
     * Open sqlite database.
     */
    public void openSQLiteDatabase();

    /**
     * close sqlite database.
     */
    public void closeSQLiteDatabase();

    /**
     * Retrieve the sqlite database. You can open the database by {@link Context#openOrCreateDatabase(name, mode, factory)}.
     * 
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getApplicationSQLiteDatabase();
}
