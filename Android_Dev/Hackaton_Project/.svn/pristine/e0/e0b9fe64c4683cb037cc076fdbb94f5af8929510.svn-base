/**
 * 
 */
package com.telenav.data.database.android;

import android.database.Cursor;

import com.telenav.data.database.ICursor;

/**
 * @author yxyao
 *
 */
public class AndroidCursor implements ICursor  {

    private Cursor mCursor;

    /**
     * @param cursor
     */
    public AndroidCursor(Cursor cursor) {
        super();
        mCursor = cursor;
    }
    
    public int getColumnIndex(String columnName){
        return mCursor.getColumnIndexOrThrow(columnName);
    }
    
    public int getColumnCount(){
        return mCursor.getColumnCount();
    }
    
    public String getColumnName(int index){
        return mCursor.getColumnName(index);
    }
    
    public String getString(int index){
        return mCursor.getString(index);
    }
    
    public byte[] getBlob(int index){
        return mCursor.getBlob(index);
    }
    
    public long getNumber(int index)
    {
        return mCursor.getLong(index);
    }
    
    public double getDouble(int index)
    {
        return mCursor.getDouble(index);
    }
    
    public boolean moveTo(int pos)
    {
        return mCursor.moveToPosition(pos);
    }
    
    public int getRowCount(){
        return mCursor.getCount();
    }
    
    public void close(){
        mCursor.close();
    }
}
