/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractFrogAnimation.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * It's an abstract Animation component. As an common component,
 * it express the animation with frame updated time by time.
 * 
 * You could implement update frame to get your own animation component.
 * 
 *@author bduan
 *@date 2010-12-3
 */
public abstract class AbstractFrogAnimation extends AbstractTnComponent
{
    /**
     * the step for frame. 
     */
    protected int step = 0;
    
    /**
     * create Animation component.
     * @param id
     */
    public AbstractFrogAnimation(int id)
    {
        super(id);
        initDefaultStyle();
    }

    protected void initDefaultStyle()
    {
        //no need to init common style for this component.
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        
    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                if (tnUiEvent.getPrivateEvent().getAction() == TnPrivateEvent.ACTION_TIMER)
                {
                    updateFrame();
                    return true;
                } 
                break;
            }
        }
        return false;
    }
    
    /**
     * Please implement this method to update your frame. 
     */
    protected abstract void updateFrame();
    
    /**
     * clean up the Animation component, such as images, datas, etc.
     */
    protected abstract void reset();
}
