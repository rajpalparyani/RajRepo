/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidFrogUiBinder.java
 *
 */
package com.telenav.ui.frogui.widget.android;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.widget.android.AndroidUiBinder;
import com.telenav.ui.frogui.widget.FrogFloatPopup;

/**
 * UI Binder for all Frog UI
 * 
 *@author jshjin (jshjin@telenav.cn)
 *@date 2010-6-30
 */
public class AndroidFrogUiBinder extends AndroidUiBinder
{   
    public AndroidFrogUiBinder()
    {
        
    }
    
    /**
     * Bind native UI for Frog UI
     */
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        if(component instanceof FrogFloatPopup)
        {
            return new AndroidFrogFloatPopup(context, (FrogFloatPopup)component);
        }
        else
        {
            return super.bindNativeUiComponent(component);
        }
    }
}
