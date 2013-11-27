/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogNullField.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * 
 * FrogNullField is a component to separate each other components.
 * 
 *@author chgong (chgong@telenav.cn)
 *@date 2010-7-20
 *
 */
public class FrogNullField extends AbstractTnComponent
{

    /**
     * Construct a FrogNullField
     * 
     * @param id
     */
    public FrogNullField(int id)
    {
        super(id);
        this.setFocusable(false);
    }

    protected void initDefaultStyle()
    {
        
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        
    }
    
    /**
     * Implements custom layout features for this component.
     * 
     * @param width
     * @param height
     */
    public void sublayout(int width, int height)
    {
        if (this.getPreferredHeight() <= 0)
        {
            this.setPreferredHeight(1);
        }
        if (this.getPreferredWidth() <= 0)
        {
            this.setPreferredWidth(1);
        }
    }
    
}
