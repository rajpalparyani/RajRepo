/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * FrogShortcutContainer.java
 *
 */
package com.telenav.ui.citizen;

import java.util.Vector;

import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnUiAnimationContext;
import com.telenav.tnui.core.TnUiAnimationContext.ITnUiAnimationListener;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.frogui.widget.FrogFloatPopup;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yning
 *@date 2011-1-10
 */
public class CitizenSlidableContainer extends FrogFloatPopup implements ITnUiAnimationListener, INotifierListener
{

    public static final int TIMEOUT_NEVER_HIDE = -1;
    protected int timeout;
    boolean isShown;
    protected long lastTimeStamp = -1;
    protected boolean isInAnimation = false;
    protected int timeOutCommand = -1;
    
    protected long durationShow = 350;
    protected long durationHide = 500;
    
    public CitizenSlidableContainer(int id)
    {
        super(id);
    }
    
    public void setAnimationDuration(long durationShow, long durationHide)
    {
        this.durationShow = durationShow;
        this.durationHide = durationHide;
    }
    
    protected void onDisplay()
    {
        isShown = true;
        lastTimeStamp = System.currentTimeMillis();
    }
    

    
    public void hide()
    {
        TnUiAnimationContext outAnimationContext = new TnUiAnimationContext(TnUiAnimationContext.TRANSITION_WIPE);
        outAnimationContext.setAttribute(TnUiAnimationContext.ATTR_DIRECTION, TnUiAnimationContext.DIRECTION_DOWN);
        outAnimationContext.setAttribute(TnUiAnimationContext.ATTR_DURATION, PrimitiveTypeCache.valueOf(durationHide));
        outAnimationContext.setAttribute(TnUiAnimationContext.ATTR_KIND, TnUiAnimationContext.KIND_OUT);
        outAnimationContext.setAttribute(TnUiAnimationContext.ATTR_BOUND, new TnRect(0, 0, getPreferredWidth(), getPreferredHeight()));
        outAnimationContext.setAnimationListener(this);
        this.setAnimationContext(outAnimationContext);
        super.hide();
    }
    
    public void hideImmediately()
    {
        isShown = false;
        isInAnimation = false;
        this.setAnimationContext(null);
        this.commandListener = null;
        Notifier.getInstance().removeListener(this);
        super.hide();
    }
    
    //the own method of this component.
    public void setTimeout(int timeout, int timeoutCommand)
    {
        if(timeout <= 0)
        {
            this.timeout = TIMEOUT_NEVER_HIDE;
        }
        else
        {
            this.timeout = timeout;
        }
        this.timeOutCommand = timeoutCommand;
    }
    
    public void showAt(AbstractTnComponent anchor, int xOff , int yOff, int w, int h, boolean isDropDown)
    {
        TnUiAnimationContext inAnimationContext = new TnUiAnimationContext(TnUiAnimationContext.TRANSITION_WIPE);
        inAnimationContext.setAttribute(TnUiAnimationContext.ATTR_DIRECTION, TnUiAnimationContext.DIRECTION_UP);
        inAnimationContext.setAttribute(TnUiAnimationContext.ATTR_DURATION, PrimitiveTypeCache.valueOf(durationShow));
        inAnimationContext.setAttribute(TnUiAnimationContext.ATTR_KIND, TnUiAnimationContext.KIND_IN);
        inAnimationContext.setAttribute(TnUiAnimationContext.ATTR_BOUND, new TnRect(0, 0, getPreferredWidth(), getPreferredHeight()));
        inAnimationContext.setAnimationListener(this);
        this.setAnimationContext(inAnimationContext);
        super.showAt(anchor, xOff, yOff, w, h, isDropDown);
    }
    
    public void updateTimeStamp()
    {
        lastTimeStamp = System.currentTimeMillis();
    }
    
    public boolean isInAnimation()
    {
        return isInAnimation;
    }
    
    public boolean isShown()    
    {
        return isShown;
    }
    
    public void setShown(boolean isShown)
    {
        this.isShown = isShown;
    }
    
    public void onAnimationEnd(TnUiAnimationContext animation)
    {
        if (animation == null)
        {
            return;
        }
        Integer direction = (Integer)animation.getAttribute(TnUiAnimationContext.ATTR_DIRECTION);
        
        if(direction != null && direction.intValue() == TnUiAnimationContext.DIRECTION_UP)
        {
            isShown = true;
            if(timeout > 0)
            {
                lastTimeStamp = System.currentTimeMillis();
                Notifier.getInstance().addListener(this);
            }
        }
        else if(direction != null && direction.intValue() == TnUiAnimationContext.DIRECTION_DOWN)
        {
            isShown = false;
        }
        isInAnimation = false;
    }

    public void onAnimationRepeat(TnUiAnimationContext animation)
    {
        
    }

    public void onAnimationStart(TnUiAnimationContext animation)
    {
        isInAnimation = true;
    }

    public long getLastNotifyTimestamp()
    {
        return lastTimeStamp;
    }

    public long getNotifyInterval()
    {
        return timeout;
    }

    public void notify(long timestamp)
    {
        Runnable run = new Runnable()
        {
            public void run()
            {
                Vector listeners = Notifier.getInstance().getAllListeners();

                if(!isShown)
                {
                    listeners.remove(CitizenSlidableContainer.this);
                    return;
                }
                
                for (int i = listeners.size() - 1; i > 0; i--)
                {
                    Object listener = listeners.elementAt(i);
                    if (listener instanceof CitizenSlidableContainer)
                    {
                        CitizenSlidableContainer popup = (CitizenSlidableContainer) listener;
                        popup.hide();
                        listeners.removeElement(popup);
                    }
                }
                
                if (commandListener != null)
                {
                    TnUiEvent commandEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, CitizenSlidableContainer.this);
                    commandEvent.setCommandEvent(new TnCommandEvent(timeOutCommand));
                    commandListener.handleUiEvent(commandEvent);
                }
            }
        };

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        this.lastTimeStamp = timestamp;
    }    
}
