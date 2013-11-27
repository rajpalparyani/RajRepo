/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRimDatabasePersistentContext.java
 *
 */
package com.telenav.persistent.rim;

import net.rim.device.api.database.Database;
import net.rim.device.api.database.DatabaseFactory;
import net.rim.device.api.io.URI;

/**
 * This class provider some necessary database information at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public interface IRimDatabasePersistentContext extends IRimPersistentContext
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
     * Retrieve the database. You can open the database by {@link DatabaseFactory#openOrCreate(URI uri)}.
     * 
     * @return SQLiteDatabase
     */
    public Database getApplicationSQLiteDatabase();
}
