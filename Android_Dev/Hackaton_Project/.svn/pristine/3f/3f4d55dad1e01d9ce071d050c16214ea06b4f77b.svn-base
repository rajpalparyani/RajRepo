/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnDatabaseManager.java
 *
 */
package com.telenav.data.database;

/**
 *@author yxyao
 *@date 2011-8-23
 */
public abstract class TnDatabaseManager
{

    private static TnDatabaseManager instance; 
    
    public static TnDatabaseManager getInstance()
    {
        return instance;
    }
    
    public static void init(TnDatabaseManager databaseManagerImpl)
    {
        instance = databaseManagerImpl;
    }
    
    public abstract IDatabase createDatabase(String dbPath, String dbName);

    
    public abstract IDatabase createDatabase(String dbName);
}
