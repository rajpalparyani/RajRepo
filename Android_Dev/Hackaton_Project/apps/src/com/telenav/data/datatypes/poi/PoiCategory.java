/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiCategory.java
 *
 */
package com.telenav.data.datatypes.poi;

import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public class PoiCategory
{
    protected String name;
    protected String focusedImagePath;
    protected String unfocusedImagePath;
    protected int categoryId;
    protected int flags;
    protected String localEventId;
    protected boolean isEvent = false;
    
    //the meaning of flags value
    private static final byte MASK_MOST_POPULAR = 0x8;
    
    protected Vector children;
    
    protected PoiCategory parent;
    
    public void addChild(PoiCategory category)
    {
        if(children == null)
        {
            children = new Vector();
        }
        
        children.addElement(category);
        category.parent = this;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setFocusedImagePath(String path)
    {
        this.focusedImagePath = path;
    }
    
    public String getFocusedImagePath()
    {
        return this.focusedImagePath;
    }
    
    public void setUnfocusedImagePath(String path)
    {
        this.unfocusedImagePath = path;
    }
    
    public String getUnfocusedImagePath()
    {
        return this.unfocusedImagePath;
    }
    
    public void setCategoryId(int id)
    {
        this.categoryId = id;
    }
    
    public int getCategoryId()
    {
        return this.categoryId;
    }
    
    public void setFlags(int flag)
    {
    	this.flags = flag;
    }
    
    public int getFlags()
    {
    	return this.flags;
    }
    
    public boolean isMostPopular()
    {
    	return (flags & MASK_MOST_POPULAR) == MASK_MOST_POPULAR ? true : false;
    }
    
    public PoiCategory getParent()
    {
        return this.parent;
    }
    
    public PoiCategory getChildAt(int index)
    {
        if(this.children == null)
            return null;
        
        return (PoiCategory)this.children.elementAt(index);
    }
    
    public Vector getChilds()
    {
        return children;
    }
    
    public int getChildrenSize()
    {
        if(this.children == null)
            return 0;
        
        return this.children.size();
    }
    
    public boolean equals(Object o) 
    {
        if(o instanceof PoiCategory)
        {
            if(isEvent)
            {
                if(localEventId == null)
                {
                    return ((PoiCategory)o).localEventId == null;
                }
                else
                {
                    return this.localEventId.equals(((PoiCategory)o).localEventId);
                }
            }
            else
            {
                return this.categoryId == ((PoiCategory)o).categoryId;
            }
        }
        return false;
    }
    
    public int hashCode()
    {
        if(isEvent)
        {
            return this.categoryId;
        }
        else
        {
            return this.localEventId.hashCode();
        }
    }

    public void setEventId(String id)
    {
        localEventId = id;
    }

    public String getEventId()
    {
        return localEventId;
    }

    public void setIsEvent(boolean isEvent)
    {
        this.isEvent = isEvent;
    }
    
    public boolean isEvent()
    {
        return isEvent;
    }
}
