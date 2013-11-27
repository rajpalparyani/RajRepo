/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogProgressBox.java
 *
 */
package com.telenav.ui.frogui.widget;

import java.util.Vector;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;

/**
 * ProgressBox class which shows progress in front of the screen
 * 
 * @author jshjin (jshjin@telenav.cn)
 * @date 2010-7-2
 */
public class FrogProgressBox extends TnPopupContainer
{
    public final static int KEY_MULTILINE_WIDTH = 1000001;
    
    public final static int KEY_MULTILINE_HEIGHT = 1000002;
    
    protected Vector timerEventListeners;
    
    protected FrogLabel multilineMsg;
    
    protected AbstractFrogAnimation animationComponent;

    /**
     * Constructs a new Progress box
     * 
     * @param id
     * @param animationComponent animation of the component
     */
    public FrogProgressBox(int id, AbstractFrogAnimation animationComponent)
    {
        super(id);
        this.contentContainer = new TnLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        this.animationComponent = animationComponent;
        this.setContent(contentContainer);
        initAnimation (animationComponent);
    }
    
    protected void initAnimation(AbstractFrogAnimation animationComponent)
    {
        this.contentContainer.add(animationComponent);  
    }

    /**
     * Set the command will be trigger when cancel this progress box.
     * 
     * @param cancelMenu
     */
    public void setCancelMenu(TnMenu cancelMenu)
    {
        this.setMenu(cancelMenu, AbstractTnComponent.TYPE_BACK);
    }

    /**
     * set the string and properties for the msg.
     * 
     * @param message the msg to be shown.
     * @param font the msg's font.
     * @param color the msg's color.
     * @param align the msg's align type, must be one of AbstractTnGraphics.Left, AbstractTnGraphics.HCENTER etc.
     */
    public void setMessage(String message, AbstractTnFont font, int color, int align)
    {
        if (message == null || message.trim().length() == 0)
            return;

        if (multilineMsg == null)
        {
            multilineMsg = new FrogLabel(0, "");
            multilineMsg.setFocusable(false);
            TnUiArgAdapter widthAdpater = this.getTnUiArgs().get(KEY_MULTILINE_WIDTH);
            TnUiArgAdapter heightAdapter = this.getTnUiArgs().get(KEY_MULTILINE_HEIGHT);
            if(widthAdpater != null)
            {
                multilineMsg.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, widthAdpater);
            }
            if(heightAdapter != null)
            {
                multilineMsg.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, heightAdapter);
            }
           
            multilineMsg.setText(message);
            multilineMsg.setStyle(align);
            multilineMsg.setFont(font);
            multilineMsg.setForegroundColor(color, color);
            contentContainer.add(multilineMsg, 0);
            multilineMsg.requestLayout();
            multilineMsg.requestPaint();
        }
        else
        {
            multilineMsg.setText(message);
            multilineMsg.requestLayout();
            multilineMsg.requestPaint();
        }
        

    }
    
    protected void addTimerEventListener(AbstractTnComponent component)
    {
        if (timerEventListeners == null)
        {
            timerEventListeners = new Vector();
        }
        if (!timerEventListeners.contains(component))
        {
            timerEventListeners.addElement(component);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        this.requestLayout();
        if(multilineMsg != null)
        {
            multilineMsg.requestLayout();
            multilineMsg.requestPaint();
        }
    }
    
    protected boolean handleUiEvent(TnUiEvent event)
    {
        switch (event.getType())
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                switch (event.getPrivateEvent().getAction())
                {
                    case TnPrivateEvent.ACTION_TIMER:
                    {
                        if (this.timerEventListeners != null && timerEventListeners.size() > 0)
                        {
                            for (int i = 0; i < timerEventListeners.size(); i++)
                            {
                                AbstractTnComponent component = (AbstractTnComponent) timerEventListeners.elementAt(i);
                                component.dispatchUiEvent(event);
                            }
                        }
                        contentContainer.requestPaint();
                        return true;
                    }
                }
                break;
            }
            case TnUiEvent.TYPE_KEY_EVENT:
            {
                if (event.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP && event.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
                {
                    TnMenu cancelMenu = getMenu(AbstractTnComponent.TYPE_BACK);
                    if (cancelMenu != null && cancelMenu.size() > 0)
                    {
                        TnCommandEvent commandEvent = new TnCommandEvent(cancelMenu.getItem(0).getId());
                        event = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                        event.setCommandEvent(commandEvent);
                        if (this.commandListener != null)
                        {
                            this.commandListener.handleUiEvent(event);
                            return true;
                        }
                    }
                }
                break;
            }
        }

        return false;
    }

    public void setPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding)
    {
        super.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        boolean isNeedRelayout = false;
        if (this.leftPadding != leftPadding || this.rightPadding != rightPadding || this.topPadding != topPadding
                || this.bottomPadding != bottomPadding)
        {
            isNeedRelayout = true;
        }

        if (isNeedRelayout)
        {
            this.multilineMsg.requestLayout();
        }
    }
    
    protected void reset()
    {
        TnUiTimer.getInstance().removeReceiver(this);
        
        if(timerEventListeners != null)
        {
            timerEventListeners.removeAllElements();
            timerEventListeners = null;
        }
        
        if(animationComponent != null)
        {
            animationComponent.reset();
        }
    }
}
