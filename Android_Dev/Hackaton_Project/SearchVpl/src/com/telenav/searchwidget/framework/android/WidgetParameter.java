/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WidgetParameter.java
 *
 */
package com.telenav.searchwidget.framework.android;

import java.util.Hashtable;

import android.os.Bundle;


/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 20, 2011
 */

public class WidgetParameter
{
    private static final String KEY_WIDGET_ID      = "_widgetId_";
    private static final String KEY_LAYOUT_ID      = "_layoutId_";
    private static final String KEY_ACTION         = "_action_";
    
    private int widgetId;
    private int layoutId;
    private int action;
    
    private Bundle bundle = new Bundle();    
    private Hashtable transientTable = new Hashtable();
    
    public WidgetParameter(int widgetId, int layoutId, int action)
    {
        this.widgetId = widgetId;
        this.putInt(KEY_WIDGET_ID, widgetId);
        
        this.layoutId = layoutId;
        this.putInt(KEY_LAYOUT_ID, layoutId);
        
        this.action = action;
        this.putInt(KEY_ACTION, action);
    }
    
    private WidgetParameter(Bundle bundle)
    {
        this.bundle = bundle;
        
        this.widgetId = bundle.getInt(KEY_WIDGET_ID);
        this.layoutId = bundle.getInt(KEY_LAYOUT_ID);
        this.action = bundle.getInt(KEY_ACTION);
    }
    
    public int getWidgetId()
    {
        return this.widgetId;
    }

    public int getLayoutId()
    {
        return this.layoutId;
    }
    
    public int getAction()
    {
        return this.action;
    }
    
    /**
     * This function is used to save the transient object, it will not convert into to Bundle
     * @param key
     * @param val
     */
    public void put(String key, Object val)
    {
        transientTable.put(key, val);
    }

    /**
     * this function is used to get the transient object, it's not coming from the bundle
     * @param key
     * @return
     */
    public Object get(String key)
    {
        return transientTable.get(key);
    }
    
    /**
     * Inserts a Boolean value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a Boolean, or null
     */
    public void putBoolean(String key, boolean value)
    {
        bundle.putBoolean(key, value);
    }

    /**
     * Inserts an int value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value an int, or null
     */
    public void putInt(String key, int value) 
    {
        bundle.putInt(key, value);
    }

    /**
     * Inserts a long value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a long
     */
    public void putLong(String key, long value) 
    {
        bundle.putLong(key, value);
    }

    /**
     * Inserts a float value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a float
     */
    public void putFloat(String key, float value) 
    {
        bundle.putFloat(key, value);
    }

    /**
     * Inserts a String value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a String, or null
     */
    public void putString(String key, String value) 
    {
        bundle.putString(key, value);
    }

    /**
     * Inserts a byte array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a byte array object, or null
     */
    public void putByteArray(String key, byte[] value) 
    {
        bundle.putByteArray(key, value);
    }
    
    /**
     * Inserts an int array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value an int array object, or null
     */
    public void putIntArray(String key, int[] value) 
    {
        bundle.putIntArray(key, value);
    }

    /**
     * Inserts a long array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a long array object, or null
     */
    public void putLongArray(String key, long[] value)
    {
        bundle.putLongArray(key, value);
    }

    /**
     * Inserts a float array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a float array object, or null
     */
    public void putFloatArray(String key, float[] value) 
    {
        bundle.putFloatArray(key, value);
    }

    /**
     * Inserts a String array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a String array object, or null
     */
    public void putStringArray(String key, String[] value) 
    {
        bundle.putStringArray(key, value);
    }
    
    /**
     * Returns the value associated with the given key, or false if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a boolean value
     */
    public boolean getBoolean(String key) 
    {
        return bundle.getBoolean(key);
    }

    /**
     * Returns the value associated with the given key, or 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return an int value
     */
    public int getInt(String key) 
    {
        return bundle.getInt(key);
    }

    /**
     * Returns the value associated with the given key, or 0L if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a long value
     */
    public long getLong(String key) 
    {
        return bundle.getLong(key);
    }

    /**
     * Returns the value associated with the given key, or 0.0f if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a float value
     */
    public float getFloat(String key) 
    {
        return bundle.getFloat(key);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key a String, or null
     * @return a String value, or null
     */
    public String getString(String key) 
    {
        return bundle.getString(key);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key a String, or null
     * @return a byte[] value, or null
     */
    public byte[] getByteArray(String key) 
    {
        return bundle.getByteArray(key);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key a String, or null
     * @return an int[] value, or null
     */
    public int[] getIntArray(String key) 
    {
        return bundle.getIntArray(key);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key a String, or null
     * @return a long[] value, or null
     */
    public long[] getLongArray(String key) 
    {
        return bundle.getLongArray(key);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key a String, or null
     * @return a float[] value, or null
     */
    public float[] getFloatArray(String key) 
    {
        return bundle.getFloatArray(key);
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key a String, or null
     * @return a String[] value, or null
     */
    public String[] getStringArray(String key) 
    {
        return bundle.getStringArray(key);
    }
    
    public static Bundle toBundle(WidgetParameter wp)
    {
        return new Bundle(wp.bundle);
    }
    
    public static WidgetParameter fromBundle(Bundle bundle)
    {
        if (bundle == null)
        {
            throw new IllegalArgumentException("bundle is empty !!!");
        }
        
        WidgetParameter wp = new WidgetParameter(bundle);
        
        return wp;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("\r\n============= Widget parameter =============\r\n");
        sb.append(bundle.toString() + "\r\n");
        
        return sb.toString();
    }

}
