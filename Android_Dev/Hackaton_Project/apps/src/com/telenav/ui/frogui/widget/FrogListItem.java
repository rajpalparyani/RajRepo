/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogListItem.java
 *
 */
package com.telenav.ui.frogui.widget;

/**
 *@author jwchen (jwchen@telenav.cn)
 *@date 2010-7-29
 */
public class FrogListItem extends FrogLabel
{
    protected FrogListItem mItem; 
    
    /**
     * Create listItem with specific Id. 
     * <br>
     * <b>This method won't realize the item instantly.</b>
     * 
     * @param id
     */
    public FrogListItem(int id)
    {
        super(id, null, false);        
        this.setFocusable(true);
        this.mItem = this;
    }
    
    /**
     * layout the component by default
     */
    public void sublayout(int width, int height)
    {
        preferWidth = width;
        preferHeight = font.getHeight() + font.getHeight() / 2 + topPadding + bottomPadding;
    }
    
    /**
     * return the component foreground color .
     * 
     * @param hasFocus the status of the component whether focus or not
     * @return the component foreground color .
     */
    public int getForegroundColor(boolean hasFocus)
    {
        if (hasFocus)
        {
            return this.focusColor;
        }
        else
        {
            return this.unfocusColor;
        }
    }
    
    protected void updateItemData(FrogListItem item)
    {
        if(this.mItem != item)
        {
            this.reset();
            this.mItem = item;
            this.initItemData(item);
        }
    }
    
    protected void reset()
    {
        this.isFocused = false;
        this.menus = null;
        this.tnUiArgs = null;
        this.id = 0;
        this.commandListener = null;
        this.keyEventListener = null;
        this.textLine = null;
    }
    
    protected FrogListItem getUiComponent()
    {
        if(this.nativeUiComponent == null)
        {
            this.bind();
        }

        return this;
    } 
    
    protected void initItemData(FrogListItem item)
    {
        if(item.font != null)
        {
            this.font = item.font;
        }
        if(item.boldFont != null)
        {
            this.boldFont = item.boldFont;
        }
        
        this.focusColor = item.focusColor;
        this.unfocusColor = item.unfocusColor;
        this.textLine = item.textLine;
        this.tnUiArgs = item.tnUiArgs;
        this.menus = item.menus;
        this.id = item.id;
        this.commandListener = item.commandListener;
        this.keyEventListener = item.keyEventListener;
    }
}
