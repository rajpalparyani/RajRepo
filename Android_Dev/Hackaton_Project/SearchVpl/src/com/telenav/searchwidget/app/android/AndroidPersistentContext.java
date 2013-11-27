/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidPersistentContext.java
 *
 */
package com.telenav.searchwidget.app.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.telenav.logger.Logger;
import com.telenav.persistent.android.IAndroidDatabasePersistentContext;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public class AndroidPersistentContext implements IAndroidDatabasePersistentContext
{
    private static AndroidPersistentContext instance = new AndroidPersistentContext();
    
    private Context context;
    private SQLiteDatabase sqliteDatabase;
    
    public static AndroidPersistentContext getInstance()
    {
        return instance;
    }
    
    public void init(Context context)
    {
        this.context = context;
    }
    
    public void closeSQLiteDatabase()
    {
        if(sqliteDatabase == null)
            return;
        
        try
        {
            if(sqliteDatabase != null)
            {
                sqliteDatabase.close();
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            try
            {
                SQLiteDatabase.releaseMemory();
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            sqliteDatabase = null;
        }
    }

    public SQLiteDatabase getApplicationSQLiteDatabase()
    {
        return sqliteDatabase;
    }

    public void openSQLiteDatabase()
    {
        if(sqliteDatabase == null || !sqliteDatabase.isOpen())
        {
            try
            {
                if(sqliteDatabase != null)
                {
                    sqliteDatabase.close();
                }
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            sqliteDatabase = this.context.openOrCreateDatabase(getApplicationName() + ".db", Context.MODE_PRIVATE, null);
        }
    }

    public Context getContext()
    {
        return this.context;
    }

    public String getApplicationName()
    {
        return "TN70";
    }

}