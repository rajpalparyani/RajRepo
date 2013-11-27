/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnUiContext.java
 *
 */
package com.telenav.tnui.core;

/**
 * This class will bind tn ui with native ui.
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
 * <br />
 * So, For default, we should only bind container, edit view, base view with the native ui component.
 * But still support for some other special native ui component if the project required.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-2
 */
public abstract class AbstractTnUiBinder
{
    /**
     * the instance of this class.
     */
    static AbstractTnUiBinder instance;
    
    /**
     * Retrieve the instance of helper.
     * 
     * @return {@link AbstractTnUiBinder}
     */
    public static AbstractTnUiBinder getInstance()
    {
        return instance;
    }
    
    public static void init(AbstractTnUiBinder binder)
    {
        instance = binder;
    }
    
    /**
     * Init this class. Context maybe is Context at Android platform, Midlet at J2ME platform etc.
     * 
     * @param context
     */
    public abstract void init(Object context);
    
    /**
     * Bind tn ui component with native ui component.
     * 
     * @param component {@link AbstractTnComponent}
     * @return {@link INativeUiComponent}
     */
    public abstract INativeUiComponent bindNativeUiComponent(AbstractTnComponent component);
    
    /**
     * Bind tn ui animation context with native ui animation.
     *  
     * @param animationContext tn ui animation context
     * @return The native ui animation
     */
    public abstract INativeUiAnimation bindNativeUiAnimation(TnUiAnimationContext animationContext);
}
