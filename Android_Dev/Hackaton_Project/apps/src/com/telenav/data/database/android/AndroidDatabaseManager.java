/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidDatabaseManager.java
 *
 */
package com.telenav.data.database.android;

import android.content.Context;

import com.telenav.data.database.IDatabase;
import com.telenav.data.database.TnDatabaseManager;

/**
 *@author yxyao
 *@date 2011-8-23
 */
public class AndroidDatabaseManager extends TnDatabaseManager
{
    private Context context;
    
    /**
     * 
     */
    public AndroidDatabaseManager(Context context)
    {
        this.context = context;
    }

    public IDatabase createDatabase(String dbPath, String dbName)
    {
        return new AndroidDataBase(dbPath+"/"+dbName+".db");
    }

    public IDatabase createDatabase(String dbName)
    {
        String dbPath = context == null ? "" : context.getFilesDir().getAbsolutePath();
        return  new AndroidDataBase(dbPath+"/"+dbName+".db");
    }

}
