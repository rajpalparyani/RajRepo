/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimFrogUiBinder.java
 *
 */
package com.telenav.ui.frogui.widget.rim;

import com.telenav.frogui.widget.FrogFloatPopup;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.widget.rim.RimUiBinder;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-11
 */
public class RimFrogUiBinder extends RimUiBinder
{
    public RimFrogUiBinder()
    {
        
    }
    
    /**
     * Bind native UI for Frog UI
     */
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        if(component instanceof FrogFloatPopup)
        {
            return new RimFrogFloatPopup(component);
        }
        else
        {
            return super.bindNativeUiComponent(component);
        }
    }
}
