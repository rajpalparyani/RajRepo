/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimRmsStore.java
 *
 */
package com.telenav.persistent.rim;

import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.persistent.rim.IRimPersistentContext.IRimPersistentObject;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimRmsStore extends TnStore
{
    protected long storeKey;
    protected IRimPersistentObject persistentObject;
    protected Hashtable crumbData;
    
    public RimRmsStore(String name, int type, IRimPersistentObject persistentObject)
    {
        super(name, type);
        
        this.persistentObject = persistentObject;
        this.storeKey = RimRmsPersistentUtil.stringToLong(getBaseStoreName());
    }

    protected void clearDelegate()
    {
        this.crumbData.clear();
        
        RimRmsPersistentUtil.deleteData(this.storeKey);
    }

    protected byte[] getDelegate(String id)
    {
        return (byte[])this.crumbData.get(id);
    }

    protected void loadDelegate()
    {
        this.crumbData = new Hashtable();
        IRimPersistentObject tmpPersistentObject = RimRmsPersistentUtil.loadData(this.storeKey);
        if(tmpPersistentObject != null)
        {
            this.persistentObject = tmpPersistentObject;
        }
        if (this.persistentObject != null)
        {
            this.crumbData = (Hashtable) this.persistentObject.getContents();
        }

        Enumeration keys = this.crumbData.keys();
        while (keys.hasMoreElements())
        {
            this.ids.addElement(keys.nextElement());
        }
    }

    protected void putDelegate(String id, byte[] data)
    {
        this.crumbData.put(id, data);
    }

    protected void removeDelegate(String id)
    {
        this.crumbData.remove(id);
    }

    protected void saveDelegate()
    {
        this.persistentObject.setContents(this.crumbData);
        RimRmsPersistentUtil.storeData(this.storeKey, this.persistentObject);
    }

    private String getBaseStoreName()
    {
        return TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_" + this.name;
    }
}
