/**
 * 
 */
package com.telenav.data.database;

/**
 * @author yxyao
 *
 */
public interface ICursor {

    public int getColumnIndex(String columnName);

    public int getColumnCount();

    public String getColumnName(int index);

    public String getString(int index);

    public byte[] getBlob(int index);

    public long getNumber(int index);

    public double getDouble(int index);

    public boolean moveTo(int pos);

    public int getRowCount();

    public void close();

}