/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seFrogUiBinder.java
 *
 */
package com.telenav.ui.frogui.widget.j2se;

import com.telenav.frogui.widget.FrogFloatPopup;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.widget.j2se.J2seUiBinder;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 5, 2010
 */
public class J2seFrogUiBinder extends J2seUiBinder
{
    public J2seFrogUiBinder()
    {
        
    }
    
    /**
     * Bind native UI for Frog UI
     */
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        if(component instanceof FrogFloatPopup)
        {
            return new J2seFrogFloatPopup(this.rootPaneContainer);
        }
        else
        {
            return super.bindNativeUiComponent(component);
        }
    }
}
