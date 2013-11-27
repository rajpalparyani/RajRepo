/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogProgressAnimationComponent.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * Progress animation , change the component the background image dynamically.
 * 
 *@author wzhu (wzhu@telenav.cn)
 *@date 2010-10-8
 */
public class FrogImageAnimation extends AbstractFrogAnimation
{
    protected TnUiArgAdapter[] animationImages;
    
    /**
     * Constructs a FrogProgressAnimationComponent with specified id and images
     * @param id
     * @param images
     */
    public FrogImageAnimation(int id, TnUiArgAdapter[] images)
    {
        super(id);
        this.animationImages = new TnUiArgAdapter[images.length];
        System.arraycopy(images, 0, animationImages, 0, images.length);
        
        this.getTnUiArgs().copy(new TnUiArgs());
        initDefaultStyle();
    }

    /**
     * layout the component
     */
    public void sublayout(int width, int height)
    {
        if(animationImages != null)
        {
            AbstractTnImage image = animationImages[0].getImage();
            this.setPreferredHeight(image.getHeight());
            this.setPreferredWidth(image.getWidth());
        }
        else
        {
            this.setPreferredHeight(1);
            this.setPreferredWidth(1);
        }
    }
    
    protected void updateFrame()
    {
        if (nativeUiComponent == null)
            return;
        if (step >= animationImages.length)
            step = 0;
        
        this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, animationImages[step]);
        step++;
    }

    protected void reset()
    {
        animationImages = null;
    }
}
