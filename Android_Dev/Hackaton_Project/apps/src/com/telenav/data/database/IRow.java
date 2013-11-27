/**
 * 
 */
package com.telenav.data.database;

/**
 * @author yxyao
 *
 */
public interface IRow {

    public Object getNativeRow();

    public boolean containsKey(String key);

    public void clear();

    public void put(String key, byte[] value);

    public void put(String key, String value);

    public void put(String key, double value);

    public void put(String key, long value);

}
