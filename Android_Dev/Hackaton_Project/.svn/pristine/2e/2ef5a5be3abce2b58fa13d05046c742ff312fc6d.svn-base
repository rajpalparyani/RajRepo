/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidDatabasePersistentUtil.java
 *
 */
package com.telenav.persistent.android;

import java.util.Vector;

import com.telenav.logger.Logger;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
class AndroidDatabasePersistentUtil
{
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATA = "data";
    
    static void createTable(SQLiteDatabase database, String tableName)
    {
        try
        {
            database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" + COLUMN_ID + " varchar(32) PRIMARY KEY, " + COLUMN_DATA + " byte[])");
        }
        catch (Throwable e)
        {
            Logger.log(AndroidDatabasePersistentUtil.class.getName(), e);
        }
    }
    
    static void deleteTable(SQLiteDatabase database, String tableName)
    {
        try
        {
            database.delete(tableName, null, null);
        }
        catch (Throwable e)
        {
            Logger.log(AndroidDatabasePersistentUtil.class.getName(), e);
        }
    }
    
    static void saveData(SQLiteDatabase database, String tableName, String id, byte[] data)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_DATA, data);
        try
        {
            database.replace(tableName, "", contentValues);
        }
        catch (Throwable e)
        {
            Logger.log(AndroidDatabasePersistentUtil.class.getName(), e);
        }
    }
    
    static byte[] readData(SQLiteDatabase database, String tableName, String id)
    {
        byte[] data = null;
        Cursor c = null;
        try
        {
            c = database.query(tableName, new String[]{COLUMN_DATA}, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
            if(c != null && c.moveToFirst())
            {
                data = c.getBlob(0);
            }
        }
        catch (Throwable e)
        {
            Logger.log(AndroidDatabasePersistentUtil.class.getName(), e);
        }
        finally
        {
            if(c != null)
            {
                c.close();
            }
            
            c = null;
        }
        
        return data;
    }
    
    static Vector readIndexes(SQLiteDatabase database, String tableName)
    {
        Cursor c = null;
        Vector indexes = new Vector();
        try
        {
            c = database.query(tableName, new String[]{COLUMN_ID}, null, null, null, null, null);
            if(c != null)
            {
                int count = c.getCount();
                if(count > 0)
                {
                    for(int i = 0 ; i < count ; i ++)
                    {
                        if(c.moveToPosition(i))
                        {
                            String id = c.getString(0);
                            indexes.add(id);
                        }
                    }
                }
            }
        }
        catch (Throwable e)
        {
            Logger.log(AndroidDatabasePersistentUtil.class.getName(), e);
        }
        finally
        {
            if(c != null)
            {
                c.close();
            }
            
            c = null;
        }
        
        return indexes;
    }
    
    static void deleteData(SQLiteDatabase database, String tableName, String id)
    {
        try
        {
            database.delete(tableName, COLUMN_ID + " = ?", new String[]{id});
        }
        catch (Throwable e)
        {
            Logger.log(AndroidDatabasePersistentUtil.class.getName(), e);
        }
    }
}
