/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidRmsStore.java
 *
 */
package com.telenav.persistent.android;

import java.util.Enumeration;
import java.util.Hashtable;

import android.content.Context;

import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
class AndroidRmsStore extends TnStore
{
    protected boolean isCrumb;
    
    protected Hashtable crumbData;
    
    public AndroidRmsStore(String name, boolean isCrumb, int type)
    {
        super(name, type);
        
        this.isCrumb = isCrumb;
        
        if(this.isCrumb)
        {
            crumbData = new Hashtable();
        }
    }

    protected void clearDelegate()
    {
        if(this.isCrumb)
        {
            this.crumbData.clear();
            AndroidFilePersistentUtil.deleteData(getContext(), getBaseStoreName() + ".bin");
        }
        else
        {
            for (int i = ids.size() - 1; i >= 0; i--)
            {
                String id = (String) ids.elementAt(i);
                AndroidFilePersistentUtil.deleteData(getContext(), getBaseStoreName() + "/" + id + ".bin");
            }

            AndroidFilePersistentUtil.deleteData(getContext(), getBaseStoreName() + ".index");
        }
    }

    protected byte[] getDelegate(String id)
    {
        if(this.isCrumb)
        {
            return (byte[])crumbData.get(id);
        }
        
        return AndroidFilePersistentUtil.readData(getContext(), getBaseStoreName() + "/" + id + ".bin");
    }

    protected void loadDelegate()
    {
        if(this.isCrumb)
        {
            this.crumbData = (Hashtable)AndroidFilePersistentUtil.loadObject(getContext(), getBaseStoreName() + ".bin");
            if(this.crumbData == null)
            {
                this.crumbData = new Hashtable();
            }
            else
            {
                Enumeration keys = this.crumbData.keys();
                while (keys.hasMoreElements())
                {
                    this.ids.addElement(keys.nextElement());
                }
            }
        }
        else
        {
            AndroidFilePersistentUtil.mkDir(getContext(), getBaseStoreName());
            this.ids = AndroidFilePersistentUtil.readIndexes(getContext(), getBaseStoreName() + ".index");
        }
    }

    protected void saveDelegate()
    {
        if(this.isCrumb)
        {
            AndroidFilePersistentUtil.storeObject(getContext(), getBaseStoreName() + ".bin", this.crumbData);
        }
        else
        {
            AndroidFilePersistentUtil.saveIndexes(getContext(), this.ids, getBaseStoreName() + ".index");
        }
    }
    
    protected void putDelegate(String id, byte[] data)
    {
        if(this.isCrumb)
        {
            this.crumbData.put(id, data);
        }
        else
        {
            AndroidFilePersistentUtil.saveData(getContext(), getBaseStoreName() + "/" + id + ".bin", data);
        }
    }

    protected void removeDelegate(String id)
    {
        if(this.isCrumb)
        {
            this.crumbData.remove(id);
        }
        else
        {
            AndroidFilePersistentUtil.deleteData(getContext(), getBaseStoreName() + "/" + id + ".bin");
        }
    }

    private String getBaseStoreName()
    {
        return TnPersistentManager.getInstance().getPersistentContext().getApplicationName() + "_" + this.name;
    }
    
    private Context getContext()
    {
        return ((IAndroidPersistentContext)TnPersistentManager.getInstance().getPersistentContext()).getContext();
    }
    
    protected int getSizeDelegate(String id)
    {
        if(this.isCrumb)
        {
            return super.getSizeDelegate(id);
        }
        else
        {
            return (int)AndroidFilePersistentUtil.measureData(getContext(), getBaseStoreName() + "/" + id + ".bin");
        }
    }
}
