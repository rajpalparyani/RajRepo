/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractRimUiBinder.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.ui.UiApplication;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiBinder;
import com.telenav.tnui.core.INativeUiAnimation;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiAnimationContext;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * This class will bind tn ui with native ui of android.
 * <br />
 * At the most time, we should not implement too many native ui component, for that the native UI framework will
 * provide a base ui component class, we only need bind most of tn ui with this base ui component class.
 * <br />
 * <br />
 * For example:
 * <br /> 
 * At Android platform, View will be the base ui component.
 * <br /> 
 * At RIM platform, Field will be the base ui component.
 * <br />
 * At J2se platform, Component will be the base ui component.
 * <br />
 * So, For default, we should only bind container, edit view, base view with the native ui component.
 * But still support for some other special native ui component if the project required.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-8
 */
public abstract class AbstractRimUiBinder extends AbstractTnUiBinder
{
    protected RimUiApplication context;
    
    /**
     * Init this class. Context maybe is Context at Android platform, Midlet at J2ME platform etc.
     * 
     * @param context - Interface to global information about an application environment.
     */
    public void init(Object context)
    {
        this.context = (RimUiApplication)context;
        
        if(AbstractTnGraphics.getInstance() == null)
        {
            AbstractTnGraphics.init(new RimGraphics());
        }
    }
    
    /**
     * Bind tn ui component with native ui component of rim.
     * 
     * @param component {@link AbstractTnComponent}
     * @return {@link INativeUiComponent}
     */
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        return new RimField(component);
    }
    
    public INativeUiAnimation bindNativeUiAnimation(TnUiAnimationContext animation)
    {
        return null;
    }
}
