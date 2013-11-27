/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnMenu.java
 *
 */
package com.telenav.tnui.core;

import java.util.Vector;

import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * Interface for managing the items in a menu. 
 * <br />
 * Below actions support menu:
 * 1)Click, 2) Long Click, 3) Menu Key, 4) Back Key etc.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-29
 */
public class TnMenu
{
    private Vector menuItems;
    private AbstractTnImage headerIcon;
    private String headerTitle;
    
    /**
     * construct a menu.
     */
    public TnMenu()
    {
        menuItems = new Vector();
    }
    
    /**
     * Sets the menu header's icon.
     * 
     * @param icon The image used for the icon.
     */
    public void setHeaderIcon(AbstractTnImage icon)
    {
        this.headerIcon = icon;
    }
    
    /**
     * Retrieve the header icon of title.
     * 
     * @return the header icon of title
     */
    public AbstractTnImage getHeaderIcon()
    {
        return this.headerIcon;
    }
    
    /**
     * Sets the menu header's title to the title given in title.
     * 
     * @param title The character sequence used for the title.
     */
    public void setHeaderTitle(String title)
    {
        this.headerTitle = title;
    }
    
    /**
     * Retrieve the header title of title.
     * 
     * @return the header title of title
     */
    public String getHeaderTitle()
    {
        return this.headerTitle;
    }
    
    /**
     * Add a new item to the menu. This item displays the given title for its label.
     * 
     * @param title
     * @param id
     */
    public void add(String title, int id)
    {
        menuItems.addElement(new TnMenuItem(title, id));
    }
    
    /**
     * Add a new item to the menu. This item displays the given title for its label.
     * 
     * @param title
     * @param id
     * @param index
     */
    public void add(String title, int id, int index)
    {
        menuItems.insertElementAt(new TnMenuItem(title, id), index);
    }
    
    /**
     * Add a new item to the menu. This item displays the given title for its label.
     * 
     * @param title
     * @param id
     * @param icon
     */
    public void add(String title, int id, AbstractTnImage icon)
    {
        menuItems.addElement(new TnMenuItem(title, id, icon));
    }
    
    /**
     * Add a new item to the menu. This item displays the given title for its label.
     * 
     * @param title
     * @param id
     * @param icon
     * @param index
     */
    public void add(String title, int id, AbstractTnImage icon, int index)
    {
        menuItems.insertElementAt(new TnMenuItem(title, id, icon), index);
    }
    
    /**
     * add a separator for the menu group.
     */
    public void addSeparator()
    {
        menuItems.addElement(new TnMenuItem());
    }
    
    /**
     * Retrieve the item by item's id.
     * 
     * @param id
     * @return {@link TnMenuItem}
     */
    public TnMenuItem findItem(int id)
    {
        for(int i = 0; i < menuItems.size(); i++)
        {
            TnMenuItem item = (TnMenuItem)menuItems.elementAt(i);
            if(item.getId() == id)
            {
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @param id
     * @return index
     */
    public int findItemIndex(int id)
    {
        for(int i = 0; i < menuItems.size(); i++)
        {
            TnMenuItem item = (TnMenuItem)menuItems.elementAt(i);
            if(item.getId() == id)
            {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Retrieve the item by item's index.
     * 
     * @param index
     * @return {@link TnMenuItem}
     */
    public TnMenuItem getItem(int index)
    {
        if(index < menuItems.size())
        {
            return (TnMenuItem)menuItems.elementAt(index);
        }
        
        return null;
    }
    
    /**
     * Remove the item with the index.
     * 
     * @param index
     */
    public void removeWithIndex(int index)
    {
        if(index < menuItems.size())
        {
            menuItems.removeElementAt(index);
        }
    }
    
    /**
     * Remove the item with the given identifier.
     * 
     * @param id
     */
    public void remove(int id)
    {
        for(int i = 0; i < menuItems.size(); i++)
        {
            TnMenuItem item = (TnMenuItem)menuItems.elementAt(i);
            if(item.getId() == id)
            {
                menuItems.removeElement(item);
                break;
            }
        }
    }
    
    /**
     * Get the number of items in the menu. Note that this will change any times items are added or removed from the menu.
     * 
     * @return int
     */
    public int size()
    {
        return menuItems.size();
    }
}
