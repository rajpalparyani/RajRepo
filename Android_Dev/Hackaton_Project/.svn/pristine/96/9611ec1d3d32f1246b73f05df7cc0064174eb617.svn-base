/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnStore.java
 *
 */
package com.telenav.persistent;

import java.util.Enumeration;
import java.util.Vector;

import com.telenav.logger.Logger;

/**
 * Store for persistent objects. 
 * <br />
 * The persistent store provides a means for objects to persist across device resets. A persistent object contains kinds of key-value pair data.
 * <br />
 * Create an index to improve the performance of access persistent data.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public abstract class TnStore
{
    protected String name;

    protected int type;
    
    protected Vector ids;

    protected boolean isLoaded;
    
    protected boolean isDirty = false;;

    /**
     * construct a store object with special name.
     * 
     * @param name
     */
    public TnStore(String name, int type)
    {
        this.name = name;
        this.type = type;
    }

    /**
     * load the store object from hardware.
     */
    public final void load()
    {
        ids = new Vector();

        loadDelegate();

        isLoaded = true;
        isDirty = false;
    }

    /**
     * Retrieve the name of this store object.
     * 
     * @return the store's name
     */
    public final String getName()
    {
        return this.name;
    }
    
    /**
     * @return storeType using {@link TnPersistentManager#RMS_STORE}, {@link TnPersistentManager#DATABASE_STORE}, {@link TnPersistentManager#SDCARD_STORE} etc.
     */
    public final int getType()
    {
        return this.type;
    }
    
    /**
     * the delegate of {@link #load()} method.
     */
    protected abstract void loadDelegate();

    /**
     * save the store object into hardware.
     */
    public final void save()
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }
        
//        Logger.log(Logger.INFO, this.getClass().getName(), "TnStore.save() isDirty = " + isDirty);
        if (!isDirty)
            return;
        
//        long time = System.currentTimeMillis();
        saveDelegate();
//        time = System.currentTimeMillis() - time;
//        System.out.println("-------------------- TnStore: save "+name+",  use "+time);
        isDirty = false;
    }
    
    /**
     * the delegate of {@link #save()} method.
     */
    protected abstract void saveDelegate();
    
    /**
     * put the data into store object.
     * Most times, will store into hardware directly.
     * 
     * @param id the id of data.
     * @param data the data information.
     */
    public final void put(int id, byte[] data)
    {
        put(id + "", data);
    }
    
    /**
     * put the data into store object.
     * Most times, will store into hardware directly.
     * 
     * @param id the id of data.
     * @param data the data information.
     */
    public final void put(String id, byte[] data)
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }
        
        if(data == null)
        {
            throw new IllegalArgumentException("This data is null.");
        }
        
        isDirty = true;
        
        putDelegate(id, data);
        
        if (!ids.contains(id))
        {
            ids.addElement(id);
        }
    }

    /**
     * the delegate of {@link #put(int, byte[])} method.
     * 
     * @param id the id of data.
     * @param data the data information.
     */
    protected abstract void putDelegate(String id, byte[] data);
    
    /**
     * Retrieve the data from store object.
     * 
     * @param id the id of data.
     * @return byte[]
     */
    public final byte[] get(int id)
    {
        return get(id + "");
    }
    
    /**
     * Retrieve the data from store object.
     * 
     * @param id the id of data.
     * @return byte[]
     */
    public final byte[] get(String id)
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }
        
        if (!ids.contains(id))
        {
//            Logger.log(Logger.INFO, this.getClass().getName(), "TnStore.get no data for id = " + id + " refer to index ids, length : " + ids.size());
            return null;
        }

        return getDelegate(id);
    }
    
    /**
     * Tests if the specified object is a component in this store.
     * 
     * @param id the id of data.
     * @return true if the specified object is a component in this store; false otherwise.
     */
    public final boolean contains(int id)
    {
        return contains(id + "");
    }
    
    /**
     * Tests if the specified object is a component in this store.
     * 
     * @param id the id of data.
     * @return true if the specified object is a component in this store; false otherwise.
     */
    public final boolean contains(String id)
    {
        return ids.contains(id);
    }

    /**
     * the delegate of {@link #get(int)} method.
     * 
     * @param id the id of data.
     * @return byte[]
     */
    protected abstract byte[] getDelegate(String id);
    
    /**
     * remove the special data with the id.
     * 
     * @param id the id of data.
     */
    public final void remove(int id)
    {
        remove(id + "");
    }
    
    /**
     * remove the special data with the id.
     * 
     * @param id the id of data.
     */
    public final void remove(String id)
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }

        if (!ids.contains(id))
        {
            return;
        }

        isDirty = true;
        
        removeDelegate(id);
        
        ids.removeElement(id);
    }
    
    /**
     * the delegate of {@link #remove(int)} method.
     * 
     * @param id the id of data.
     */
    protected abstract void removeDelegate(String id);

    /**
     * clear the whole store object.
     */
    public final void clear()
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }

        clearDelegate();
        
        ids.removeAllElements();
        isDirty = false;
    }
    
    /**
     * the delegate of {@link #clear()} method.
     */
    protected abstract void clearDelegate();

    /**
     * return the size of the store object.
     * 
     * @return int
     */
    public final int size()
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }

        return ids.size();
    }

    /**
     * return the key set of the store object.
     * 
     * @return Enumeration
     */
    public final Enumeration keys()
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }

        return ids.elements();
    }
    
    /**
     * return the size for specified value, without loading the value in some implementation.
     * @param id
     * @return the size for specified
     */
    public final int getSize(int id)
    {
        return getSize("" + id);
    }
    
    /**
     * return the size for specified value, without loading the value in some implementation.
     * @param id
     * @return the size for specified
     */
    public final int getSize(String id)
    {
        if (!isLoaded)
        {
            throw new IllegalStateException("This store is not loaded.");
        }
        return getSizeDelegate(id);
    }
    
    /**
     * measure and return size of value buffer as default implementation</br>
     * subclass should override this if there's a way to get size without load buffer.
     * @param id
     * @return
     */
    protected int getSizeDelegate(String id)
    {
        byte[] buff = getDelegate(id);
        return buff == null? 0: buff.length;
    }
}
