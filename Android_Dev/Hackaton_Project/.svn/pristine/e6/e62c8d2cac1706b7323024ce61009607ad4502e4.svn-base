/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractI18nFile.java
 *
 */
package com.telenav.module.sync.apache;

import java.util.Vector;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-4-20
 */
class I18nFile
{
    public final static int FAIL = 0;
    
    public final static int SUCCESS = 1;
    
    public final static int SENDING = 2;
    
    public final static int RECEIVED = 3;
    
    private String name;
    
    private int status;
    
    private I18nFile parent;
    
    private Vector children;
    
    private byte[] md5;
    
    public I18nFile(String name, int status)
    {
        this.name = name;
        this.status = status;
    };
    
    public int getStatus()
    {
        return this.status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public I18nFile getParent()
    {
        return this.parent;
    }
    
    public void setMd5(byte[] md5)
    {
        this.md5 = md5;
    }
    
    public byte[] getMd5()
    {
        return this.md5;
    }
    
    public int getChildrenSize()
    {
        return children == null ? 0 : children.size();
    }

    public I18nFile getChildAt(int index)
    {
        return children == null ? null : (I18nFile) children.elementAt(index);
    }

    public void addChild(I18nFile file)
    {
        if (file == null)
            return;

        if (this.children == null)
        {
            this.children = new Vector();
        }

        file.parent = this;

        this.children.addElement(file);
    }

    public I18nFile searchChildByName(String path)
    {
        return searchChildByName(this, path);
    }
    
    private I18nFile searchChildByName(I18nFile parent, String name)
    {
        if(name.equals(parent.getName()))
            return parent;
        
        if(parent.getChildrenSize() == 0)
            return null;
        
        for (int i = 0; i < parent.getChildrenSize(); i++)
        {
            I18nFile file = (I18nFile) parent.children.elementAt(i);
            file = searchChildByName(file, name);
        
            if(file != null)
            {
                return file;
            }
        }
        
        return null;
    }
}
