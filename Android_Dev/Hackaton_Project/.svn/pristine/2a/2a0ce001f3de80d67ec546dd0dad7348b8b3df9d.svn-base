/**
 * 
 */
package com.telenav.data.database.android;

import android.content.ContentValues;

import com.telenav.data.database.IRow;

/**
 * @author yxyao
 *
 */
public class AndroidRow implements IRow {
    
    private ContentValues mContentValues;

    /**
     * 
     */
    AndroidRow() {
        this.mContentValues = new ContentValues();
    }
    
    public Object getNativeRow() {
        return mContentValues;
    }
    
    public void put(String key, long value){
        mContentValues.put(key, value);
    }

    public void put(String key, double value)
    {
        mContentValues.put(key, value);
    }
    
    public void put(String key, String value)
    {
        mContentValues.put(key, value);
    }
    
    public void put(String key, byte[] value)
    {
        mContentValues.put(key, value);
    }
    
    public void clear(){
        mContentValues.clear();
    }
    
    public boolean containsKey(String key)
    {
        return mContentValues.containsKey(key);
    }
}
