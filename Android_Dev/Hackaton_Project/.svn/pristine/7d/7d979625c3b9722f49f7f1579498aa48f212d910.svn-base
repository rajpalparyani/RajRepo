/**
 * 
 */
package com.telenav.data.database;

/**
 * @author yxyao
 *
 */
public interface IDatabase {

    public void update(String table, IRow contents, String whereClause, String[] whereArgs);

    public ICursor query(boolean distinct,String table, String[] columns, String whereClause, String[] args, String groupBy, String orderBy);

    public IRow createPendingRow();

    public void rollback();
    
    public void commit();

    public void beginTransaction();

    public void insert(String table, IRow contents);

    public void release();

    public void execSQL(String sql, String[] args);

}
