/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimDatabasePersistentUtil.java
 *
 */
package com.telenav.persistent.rim;

import net.rim.device.api.database.Cursor;
import net.rim.device.api.database.Database;
import net.rim.device.api.database.Statement;

import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimDatabasePersistentUtil
{
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_DATA = "data";

    static void createTable(Database database, String tableName)
    {
        Statement statement = null;
        try
        {
            statement = database.createStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (" + COLUMN_ID + " varchar(32) PRIMARY KEY, "
                    + COLUMN_DATA + " byte[])");
            statement.prepare();
            statement.execute();
        }
        catch (Throwable e)
        {
            Logger.log(RimDatabasePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (statement != null)
                    statement.close();
            }
            catch (Throwable e)
            {
                Logger.log(RimDatabasePersistentUtil.class.getName(), e);
            }
            finally
            {
                statement = null;
            }
        }
    }

    static void deleteTable(Database database, String tableName)
    {
        Statement statement = null;
        try
        {
            statement = database.createStatement("DROP TABLE " + tableName);
            statement.prepare();
            statement.execute();
        }
        catch (Throwable e)
        {
            Logger.log(RimDatabasePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (statement != null)
                    statement.close();
            }
            catch (Throwable e)
            {
                Logger.log(RimDatabasePersistentUtil.class.getName(), e);
            }
            finally
            {
                statement = null;
            }
        }
    }

    static byte[] readData(Database database, String tableName, String id)
    {
        byte[] data = null;
        Statement statement = null;
        Cursor cursor = null;
        try
        {
            statement = database.createStatement("SELECT * FROM " + tableName + " WHERE " + COLUMN_ID + " = ?");
            statement.prepare();
            statement.bind(1, id);

            cursor = statement.getCursor();
            if (cursor != null && cursor.next())
            {
                data = cursor.getRow().getBlobBytes(0);
            }
        }
        catch (Throwable e)
        {
            Logger.log(RimDatabasePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (cursor != null)
                    cursor.close();
            }
            catch (Throwable e)
            {
                Logger.log(RimDatabasePersistentUtil.class.getName(), e);
            }
            finally
            {
                cursor = null;
            }

            try
            {
                if (statement != null)
                    statement.close();
            }
            catch (Throwable e)
            {
                Logger.log(RimDatabasePersistentUtil.class.getName(), e);
            }
            finally
            {
                statement = null;
            }
        }

        return data;
    }

    static void saveData(Database database, String tableName, String id, byte[] data)
    {
        Statement statement = null;
        try
        {
            statement = database.createStatement("INSERT OR REPLACE INTO " + tableName + " (" + COLUMN_DATA + ") " + " VALUES ( ? ) WHERE "
                    + COLUMN_ID + " = ?");
            statement.prepare();
            statement.bind(1, data);
            statement.bind(2, id);
            statement.execute();
        }
        catch (Throwable e)
        {
            Logger.log(RimDatabasePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (statement != null)
                    statement.close();
            }
            catch (Throwable e)
            {
                Logger.log(RimDatabasePersistentUtil.class.getName(), e);
            }
            finally
            {
                statement = null;
            }
        }
    }

    static void deleteData(Database database, String tableName, String id)
    {
        Statement statement = null;
        try
        {
            statement = database.createStatement("DELETE FROM " + tableName + " WHERE " + COLUMN_ID + " = ?");
            statement.prepare();
            statement.bind(1, id);
            statement.execute();
        }
        catch (Throwable e)
        {
            Logger.log(RimDatabasePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (statement != null)
                    statement.close();
            }
            catch (Throwable e)
            {
                Logger.log(RimDatabasePersistentUtil.class.getName(), e);
            }
            finally
            {
                statement = null;
            }
        }
    }
}
