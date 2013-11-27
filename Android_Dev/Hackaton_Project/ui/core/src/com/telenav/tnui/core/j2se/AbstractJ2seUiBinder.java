/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractJ2seUiBinder.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.Graphics2D;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiBinder;
import com.telenav.tnui.core.INativeUiAnimation;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiAnimationContext;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * This class will bind tn ui with native ui of j2se.
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
 *@date Aug 3, 2010
 */
public abstract class AbstractJ2seUiBinder extends AbstractTnUiBinder
{
    protected J2seRootPaneContainer rootPaneContainer;
    
    public void init(Object context)
    {
        this.rootPaneContainer = (J2seRootPaneContainer) context;

        if (AbstractTnGraphics.getInstance() == null)
        {
            Graphics2D g = (Graphics2D)this.rootPaneContainer.getRootPane().getGraphics();
            AbstractTnGraphics.init(new J2seGraphics(g));
        }
    }
    
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        return new J2seComponent(component);
    }

    public INativeUiAnimation bindNativeUiAnimation(TnUiAnimationContext animation)
    {
        return null;
    }
}
