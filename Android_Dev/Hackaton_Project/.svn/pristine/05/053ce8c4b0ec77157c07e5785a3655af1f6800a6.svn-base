/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seRmsStore.java
 *
 */
package com.telenav.persistent.j2se;

import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.j2se.IJ2sePersistentContext.IJ2sePersistentObject;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
class J2seRmsStore extends TnStore
{
    protected IJ2sePersistentObject persistentObject;
    protected Hashtable crumbData;
    
    public J2seRmsStore(String name, int type, IJ2sePersistentObject persistentObject)
    {
        super(name, type);
        
        this.persistentObject = persistentObject;
    }

    protected void clearDelegate()
    {
        this.crumbData.clear();
        
        J2seRmsPersistentUtil.deleteData(getBaseStoreName());
    }

    protected byte[] getDelegate(String id)
    {
        return (byte[])this.crumbData.get(id);
    }

    protected void loadDelegate()
    {
        crumbData = new Hashtable();
        IJ2sePersistentObject obj = J2seRmsPersistentUtil.loadData(getBaseStoreName());
        if (obj != null)
        {
        	persistentObject = obj;
            crumbData = (Hashtable)persistentObject.getContents();
        }

        Enumeration keys = crumbData.keys();
        while (keys.hasMoreElements())
        {
            ids.addElement(keys.nextElement());
        }
    }

    protected void putDelegate(String id, byte[] data)
    {
        crumbData.put(id, data);
    }

    protected void removeDelegate(String id)
    {
        crumbData.remove(id);
    }

    protected void saveDelegate()
    {
        persistentObject.setContents(crumbData);
        J2seRmsPersistentUtil.storeData(getBaseStoreName(), persistentObject);
    }

    private String getBaseStoreName()
    {
        return TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_" + this.name;
    }
}
