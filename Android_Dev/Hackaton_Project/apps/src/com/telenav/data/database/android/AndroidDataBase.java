/**
 * 
 */
package com.telenav.data.database.android;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.telenav.data.database.ICursor;
import com.telenav.data.database.IDatabase;
import com.telenav.data.database.IRow;
import com.telenav.logger.Logger;

/**
 * @author yxyao
 *
 */
public class AndroidDataBase implements IDatabase {

    private static final String TAG = "AndroidDatabase";
    
    private SQLiteDatabase mDatabase;
    
    private boolean mPrivateDb;
    
    /**
     * 
     */
    public AndroidDataBase(String dbPath) {
        mDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        mPrivateDb = true;
    }
    
    public AndroidDataBase(SQLiteDatabase database) {
        mDatabase = database;
        mPrivateDb = false;
    }
    
    public ICursor query(boolean distinct,String table,String[] columns, String whereClause, String[] args,String groupBy, String orderBy)
    {
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return null;
        }
        Cursor cursor = mDatabase.query(distinct,table, columns, whereClause, args, groupBy, null, orderBy,null);
        return new AndroidCursor(cursor);
    }
    
    public void execSQL(String sql,String[] args)
    {
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return;
        }
        if(args == null){
            mDatabase.execSQL(sql);
        }
        else{
            mDatabase.execSQL(sql, args);
        }
    }
    
    public void update(String table, IRow contents, String whereClause, String[] whereArgs){
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return;
        }
        ContentValues cv = (ContentValues)contents.getNativeRow();
        mDatabase.update(table, cv, whereClause, whereArgs);
    }
    
    public void insert(String table, IRow contents)
    {
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return;
        }
        ContentValues cv = (ContentValues)contents.getNativeRow();
        mDatabase.insert(table, null, cv);
    }
    
    public void beginTransaction(){
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return;
        }
        mDatabase.beginTransaction();
    }
    
    public void rollback() {
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return;
        }
        //XXX do nothing just end transaction
        mDatabase.endTransaction();
    }
    
    public void commit() {
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return;
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }
    
    public IRow createPendingRow()
    {
        return new AndroidRow();
    }
    
    public void release(){
        releaseInner();
    }
    
    private void releaseInner(){
        //XXX db should be closed by who opened, so do not close non-private db.
        if(mDatabase == null)
        {
            Logger.log(Logger.ERROR, TAG, "null database impl");
            return;
        }
        if(mDatabase.isOpen() && mPrivateDb){
            mDatabase.close();
        }
    }
    
    protected void finalize() throws Throwable {
        super.finalize();
        releaseInner();
    }
}
