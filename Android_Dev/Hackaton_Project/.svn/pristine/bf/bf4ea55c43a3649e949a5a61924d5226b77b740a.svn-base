/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogMessageBox.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.util.PrimitiveTypeCache;

/**
 * MessageBox class which provides message only box and button selection box.
 *@author jshjin (jshjin@telenav.cn)
 *@date 2010-6-30
 */
public class FrogMessageBox extends TnPopupContainer implements ITnUiEventListener
{
    private static final int FROG_MESSAGE_BOX_WIDTH = 1;
    
    protected TnLinearContainer messageContainer;
    protected TnLinearContainer buttonContainer;
    
    protected AbstractTnComponent title;
    protected FrogMultiLine messageMultiLine;
    
    protected boolean isButtonContainerVertical = false;
    protected int buttonCount = 0;
    protected int messageBoxTimeout;
    protected int timeOutCommand = -1;
    protected long startTime;
    
    protected int messageKey;
    protected String messageFamily;
    
    private static ITnUiArgsDecorator FROG_MESSAGE_BOX_DECORATOR = new ITnUiArgsDecorator()
    {
        public Object decorate(TnUiArgAdapter args)
        {
            int key = ((Integer)args.getKey()).intValue();
            return PrimitiveTypeCache.valueOf(getDimenison(key));
        }
        
        public int getDimenison(int id)
        {
            int result = 0;
            boolean isPortarit = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation() == 
                AbstractTnUiHelper.ORIENTATION_PORTRAIT;
            switch(id)
            {
                case FROG_MESSAGE_BOX_WIDTH:
                {
                    if(isPortarit)
                    {
                        result = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth() * 19 / 20;
                    }
                    else
                    {
                        result = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth() * 6 / 10;
                    }
                    break;
                }
            }
            return result;
        }
    };
    
    /**
     * construct a message box,  usually with message and buttons.
     * Default horizontal layout.
     * @param id
     * @param message
     */
    public FrogMessageBox(int id, String message)
    {
        this(id, message, false);
    }
    
    /**
     * construct a message box,  usually with message and buttons
     * @param id
     * @param message
     */
    public FrogMessageBox(int id, String message, boolean isButtonContainerVertical)
    {
        super(id);
        this.isButtonContainerVertical = isButtonContainerVertical;
        this.messageMultiLine = new FrogMultiLine(0, message);
        contentContainer = new TnLinearContainer(0, true, AbstractTnGraphics.HCENTER);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(FROG_MESSAGE_BOX_WIDTH), FROG_MESSAGE_BOX_DECORATOR));
        setUpContentContainer();
        this.setContent(contentContainer);
    }
    
    protected void setUpContentContainer()
    {
        messageContainer = new TnLinearContainer(0, true, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        this.messageMultiLine.setTextAlign(FrogMultiLine.TEXT_ALIGN_CENTER);
        this.contentContainer.add(this.messageContainer);
        messageContainer.add(messageMultiLine);
    }
    
    /**
     * Set Message box title
     * @param label
     */
    public void setTitle(AbstractTnComponent label)
    {
        if(label == null )
            return;
        if(title != null && this.contentContainer.get(0).equals(title))
        {
            this.contentContainer.remove(0);
        }
        this.contentContainer.add(label, 0);
        title = label;
        requestLayout();
        requestPaint();
    }
    
    /**
     * Set message alert time out 
     * @param second
	 * @param timeoutCommand the command id for timeout
     */
    public void setTimeout(int second, int timeoutCommand)
    {
        this.messageBoxTimeout = second * 1000;
        this.timeOutCommand = timeoutCommand;
        
        TnUiTimer.getInstance().addReceiver(this);
    }

    
    /**
     * set the padding of this component
     */
    public void setPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding)
    {
        boolean isRepaint = false;
        if(this.leftPadding != leftPadding || this.rightPadding != rightPadding || this.topPadding != topPadding || this.bottomPadding != bottomPadding)
            isRepaint = true;

        if(leftPadding >= 0)
            this.leftPadding = leftPadding;
        
        if(rightPadding >= 0)
            this.rightPadding = rightPadding;
        
        if(topPadding >= 0)
            this.topPadding = topPadding;
        
        if(bottomPadding >= 0)
            this.bottomPadding = bottomPadding;
        
        if(isRepaint)
        {
            contentContainer.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
            if(this.messageMultiLine != null)
            {
                this.messageMultiLine.requestLayout();
            }
        }
    }
    
    
    /**
     * Set the command will be trigger when cancel this progress box.
     * @param cancelMenu
     */
    public void setCancelMenu(TnMenu cancelMenu)
    {
        this.setMenu(cancelMenu, TYPE_BACK);
    }
    
    /**
     *  Add command buttons for message box
     * @param button
     */
    public void addButton(AbstractTnComponent button)
    {
        if (button == null)
            return;
        
        button.setCommandEventListener(this);
        
        if(buttonContainer == null)
        {
            buttonContainer = new TnLinearContainer(0, isButtonContainerVertical, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            this.contentContainer.add(buttonContainer);
        }
        
        buttonContainer.add(button);
        buttonCount ++;
    }

    /**
     * Set message box message.
     * 
     * @param message
     */
    public void setMessage(String message)
    {
        if(message != null && message.length() > 0)
        {  
            this.messageMultiLine.setText(message);
            this.messageMultiLine.requestLayout();
            this.messageMultiLine.requestPaint();
        }
    }
    
    /**
     * Set message box message.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     */
    public void setMessage(int key, String familyName)
    {
        this.messageKey = key;
        this.messageFamily = familyName;
        
        setMessage(this.getText(key, familyName));
    }
    
    /**
     * handle ui event
     */
    public boolean handleUiEvent(TnUiEvent event)
    {
        switch(event.getType())
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                switch(event.getPrivateEvent().getAction())
                {
                    case TnPrivateEvent.ACTION_TIMER:
                    {
                        if (messageBoxTimeout > 0)
                        {
                            if (startTime <= 0)
                            {
                                startTime = System.currentTimeMillis();
                            }
                            else
                            {
                                if ((System.currentTimeMillis() - startTime) >= messageBoxTimeout)
                                {
                                    this.hide();
                                    if(this.commandListener != null)
                                    {
                                        TnUiEvent commandEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, event.getComponent());
                                        commandEvent.setCommandEvent(new TnCommandEvent(timeOutCommand));
                                        
                                        this.commandListener.handleUiEvent(commandEvent);
                                    }
                                    
                                    TnUiTimer.getInstance().removeReceiver(this);
                                }
                            }
                            return true;
                        }
                        break;
                    }
                }
                break;
            }
            case TnUiEvent.TYPE_KEY_EVENT:
            {
                if(event.getKeyEvent().getAction()== TnKeyEvent.ACTION_UP && 
                        event.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
                {
                    TnMenu cancelMenu = this.getMenu(AbstractTnComponent.TYPE_BACK);
                    if(cancelMenu != null && cancelMenu.size() > 0)
                    {
                        TnCommandEvent commandEvent = new TnCommandEvent(cancelMenu.getItem(0).getId());
                        event = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                        event.setCommandEvent(commandEvent);
                        if(this.commandListener != null)
                        {
                            this.commandListener.handleUiEvent(event);
                            return true;
                        }
                    }
                }
                break;
            }
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                this.hide();
                if(this.commandListener != null)
                {
                    this.commandListener.handleUiEvent(event);
                    return true;
                }
            }
        }

        return false;
    }

    protected void prepare()
    {
        this.contentContainer.getTnUiArgs().copy(this.getTnUiArgs());
        if(buttonContainer == null)
        {
            this.messageContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, contentContainer.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH));
            this.messageContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, contentContainer.getTnUiArgs().get(TnUiArgs.KEY_PREFER_HEIGHT));
        }
        if(this.contentContainer != null && this.contentContainer.getTnUiArgs() != null)
        {
            if (tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS) != null)
            {
                this.contentContainer.getTnUiArgs().put(
                    TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                    tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS));
            }
            
            if(tnUiArgs.get(TnUiArgs.KEY_PREFER_HEIGHT) != null)
            {
                this.contentContainer.getTnUiArgs().put(
                    TnUiArgs.KEY_PREFER_HEIGHT,
                    tnUiArgs.get(TnUiArgs.KEY_PREFER_HEIGHT));
            }
            
            if(tnUiArgs.get(TnUiArgs.KEY_PREFER_WIDTH) != null)
            {
                this.contentContainer.getTnUiArgs().put(
                    TnUiArgs.KEY_PREFER_WIDTH,
                    tnUiArgs.get(TnUiArgs.KEY_PREFER_WIDTH));
            }
        }
    }
    
    protected void reloadI18nResource()
    {
        super.reloadI18nResource();
        
        this.setMessage(this.messageKey, this.messageFamily);
    }
    
    public void setMessageFont(AbstractTnFont font)
    {
        messageMultiLine.setFont(font);
        this.messageMultiLine.requestLayout();
        this.messageMultiLine.requestPaint();
    }
}
