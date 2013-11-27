/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AndroidTranslateAnimation.java
 *
 */
package com.telenav.tnui.core.android;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

import com.telenav.tnui.core.INativeUiAnimation;
import com.telenav.tnui.core.TnUiAnimationContext;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-12
 */
class AndroidTranslateAnimation extends TranslateAnimation implements INativeUiAnimation, AnimationListener
{
    protected TnUiAnimationContext context;
    
    public AndroidTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, TnUiAnimationContext context)
    {
        super(fromXDelta, toXDelta, fromYDelta, toYDelta);
        
        this.context = context;
        
        if(context.getAnimationListener() != null)
        {
            this.setAnimationListener(this);
        }
    }
    
    public TnUiAnimationContext getTnUiAnimation()
    {
        return this.context;
    }

    public void onAnimationEnd(Animation animation)
    {
        this.context.getAnimationListener().onAnimationEnd(this.context);
    }

    public void onAnimationRepeat(Animation animation)
    {
        this.context.getAnimationListener().onAnimationRepeat(this.context);
    }

    public void onAnimationStart(Animation animation)
    {
        this.context.getAnimationListener().onAnimationStart(this.context);
    }

}
