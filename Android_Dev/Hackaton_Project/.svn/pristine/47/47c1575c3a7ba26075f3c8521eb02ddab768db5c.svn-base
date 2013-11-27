/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractAndroidUiBinder.java
 *
 */
package com.telenav.tnui.core.android;

import android.content.Context;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiBinder;
import com.telenav.tnui.core.INativeUiAnimation;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.TnUiAnimationContext;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnRect;

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
 *@date 2010-6-17
 */
public abstract class AbstractAndroidUiBinder extends AbstractTnUiBinder
{
    /**
     * Interface to global information about an application environment. This is an abstract class whose implementation
     * is provided by the Android system. It allows access to application-specific resources and classes, as well as
     * up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.
     */
    protected Context context;
    
    /**
     * Init this class. Context maybe is Context at Android platform, Midlet at J2ME platform etc.
     * 
     * @param context - Interface to global information about an application environment.
     */
    public void init(Object context)
    {
        this.context = (Context)context;
        
        if(AbstractTnGraphics.getInstance() == null)
        {
            AbstractTnGraphics.init(new AndroidGraphics());
        }
    }
    
    /**
     * Bind tn ui component with native ui component of android.
     * 
     * @param component {@link AbstractTnComponent}
     * @return {@link INativeUiComponent}
     */
    public INativeUiComponent bindNativeUiComponent(AbstractTnComponent component)
    {
        return new AndroidView(context, component);
    }

    /**
     * Bind tn ui animation context with native ui animation.
     *  
     * @param animationContext tn ui animation context
     * @return The native ui animation
     */
    public INativeUiAnimation bindNativeUiAnimation(TnUiAnimationContext animation)
    {
        if(TnUiAnimationContext.TRANSITION_WIPE.equals(animation.getId()))
        {
            Long durationLong = (Long)animation.getAttribute(TnUiAnimationContext.ATTR_DURATION);
            Integer directionInteger = (Integer)animation.getAttribute(TnUiAnimationContext.ATTR_DIRECTION);
            Integer kindInteger = (Integer)animation.getAttribute(TnUiAnimationContext.ATTR_KIND);
            TnRect bound = (TnRect)animation.getAttribute(TnUiAnimationContext.ATTR_BOUND);
            
            AndroidTranslateAnimation translateAnimation = null;
            switch(directionInteger.intValue())
            {
                case TnUiAnimationContext.DIRECTION_DOWN:
                {
                    if(kindInteger.intValue() == TnUiAnimationContext.KIND_IN)
                    {
                        translateAnimation = new AndroidTranslateAnimation(0, 0, -bound.height(), 0, animation);
                    }
                    else
                    {
                        translateAnimation = new AndroidTranslateAnimation(0, 0, 0, bound.height(), animation);
                    }
                    break;
                }
                case TnUiAnimationContext.DIRECTION_UP:
                {
                    if(kindInteger.intValue() == TnUiAnimationContext.KIND_IN)
                    {
                        translateAnimation = new AndroidTranslateAnimation(0, 0, bound.height(), 0, animation);
                    }
                    else
                    {
                        translateAnimation = new AndroidTranslateAnimation(0, 0, 0, -bound.height(), animation);
                    }
                    break;
                }
                case TnUiAnimationContext.DIRECTION_LEFT:
                {
                    if(kindInteger.intValue() == TnUiAnimationContext.KIND_IN)
                    {
                        translateAnimation = new AndroidTranslateAnimation(bound.width(), 0, 0, 0, animation);
                    }
                    else
                    {
                        translateAnimation = new AndroidTranslateAnimation(0, -bound.width(), 0, 0, animation);
                    }
                    break;
                }
                case TnUiAnimationContext.DIRECTION_RIGHT:
                {
                    if(kindInteger.intValue() == TnUiAnimationContext.KIND_IN)
                    {
                        translateAnimation = new AndroidTranslateAnimation(-bound.width(), 0, 0, 0, animation);
                    }
                    else
                    {
                        translateAnimation = new AndroidTranslateAnimation(0, bound.width(), 0, 0, animation);
                    }
                    break;
                }
            }
            if(translateAnimation != null)
            {
                translateAnimation.setDuration(durationLong.longValue());
            }
            return translateAnimation;
        }
        
        return null;
    }
}
