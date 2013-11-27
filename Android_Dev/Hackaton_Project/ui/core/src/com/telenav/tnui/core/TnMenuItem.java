/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMenuItem.java
 *
 */
package com.telenav.tnui.core;

import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * Interface for direct access to a previously created menu item. 
 * <br />
 * An Item is returned by calling one of the add(int)  methods.
 *  For a feature set of specific menu types, see Menu. 
 *  
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-29
 */
public class TnMenuItem
{
    private String title;
    private int id;
    private boolean isSeparator;
    private AbstractTnImage icon;
    
    /**
     * Construct a separator.
     */
    TnMenuItem()
    {
        isSeparator = true;
    }
    
    /**
     * Construct a menu item.
     * 
     * @param title
     * @param id
     */
    TnMenuItem(String title, int id)
    {
        this(title, id, null);
    }
    
    /**
     * Construct a menu item.
     * 
     * @param title
     * @param id
     * @param icon
     */
    TnMenuItem(String title, int id, AbstractTnImage icon)
    {
        this.title = title;
        this.id = id;
        this.icon = icon;
    }
    
    /**
     * Retrieve the title of this menu item.
     * 
     * @return String
     */
    public String getTitle()
    {
        return this.title;
    }
    
    /**
     * Retrieve the id of this menu item.
     * 
     * @return int
     */
    public int getId()
    {
        return this.id;
    }
    
    /**
     * Retrieve the icon of this menu item.
     * 
     * @return {@link AbstractTnImage}
     */
    public AbstractTnImage getIcon()
    {
        return this.icon;
    }
    
    /**
     * Retrieve if this menu item is a separator.
     * 
     * @return boolean
     */
    public boolean isSeparator()
    {
        return this.isSeparator;
    }
}
