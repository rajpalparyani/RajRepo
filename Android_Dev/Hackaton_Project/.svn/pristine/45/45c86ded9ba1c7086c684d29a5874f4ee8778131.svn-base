/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogAdapter.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;

/**
 * FrogAdapter is the bridge between a component and the data.<br><br> 
 * It can be used for some cases:<br>
 * 1. the data set is too large.<br>
 * 2. the data may not ready now.<br>
 * 3. enable step load base on user's action.<br>
 * 
 *@author bduan
 *@date 2010-9-29
 */
public interface FrogAdapter
{
    /**
     * Return the component specific for given position.
     * 
     * If convert component is provided, please reset the data into it
     * to optimize the performance. 
     * 
     * @param position
     * @param convertComponent
     * @param parent
     * @return component
     */
    public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent);
    
    /**
     * return the count of the data.
     * 
     * @return count
     */
    public int getCount();
    
    /**
     * return the item type of specific type.
     * @param position
     * @return item's type
     */
    public int getItemType(int position);
}
