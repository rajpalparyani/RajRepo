/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogButton.java
 *
 */
package com.telenav.ui.citizen;

import android.view.View;
import android.view.ViewGroup;

import com.telenav.mvc.ICommonConstants;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;


/**
 * A component for profile switcher
 * 
 * 
 * @author jyxu (jyxu@telenav.cn)
 * @date 2010-7-2
 */
public class CitizenProfileSwitcher extends AbstractTnComponent
{
    /**
     * icon of the component when it is focused
     */
    protected AbstractTnImage bgImage = ImageDecorator.PROFILE_SWITCH_BG.getImage();
    private boolean isSwitchOn = false;
    private String itemName;
    private AbstractTnFont font;
    private int focusedColor;
    private int unfocusedColor;
    private int lastTouchX = 0;
    private int downTouchX = 0;
    private int lastTouchY = 0;
    private static int X_DISTANCE_THRESHOLD = 1;
    private static int START_DRAGGING_DISTANCE_THRESHOLD = 5;
    private boolean isInDragging = false;
    private int switchImageXPos = 0;
    int imageLeft = 0;
    int imageTop = 0;
    int imageBottom = 0;
    int imageRight = 0;
    /**
     * construct a FrogButton object
     * 
     * @param id id of the component
     * @param text text of the component
     */
    public CitizenProfileSwitcher(int id)
    {
        super(id);
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }
    
    public void setFont(AbstractTnFont font)
    {
        this.font = font;
    }
    
    public void setForegroundColor(int focusedColor, int unfocusedColor)
    {
        this.focusedColor = focusedColor;
        this.unfocusedColor = unfocusedColor;
    }
    
    public void sublayout(int width, int height)
    {
        initializeBound();
    }
    
    public void setSwitchOn(boolean isSwitchOn)
    {
        this.isSwitchOn = isSwitchOn;
        this.requestPaint();
    }
    
    public boolean isSwitchOn()
    {
        return isSwitchOn;
    }
    
    private boolean isFocusedImage()
    {
        return isFocused && isInsideBoundingBox(imageLeft, imageTop, imageRight, imageBottom, lastTouchX, lastTouchY)
                || isInDragging;
    }
    
    private boolean isSwitchOnImage(boolean isTouchEvent)
    {
        if (isTouchEvent)
        {
            return lastTouchX > (imageLeft + (imageRight - imageLeft) / 2);
        }
        return isSwitchOn;
    }
    
    private AbstractTnImage getImage(boolean isTouchEvent)
    {
        //Focused Image
        if (isFocusedImage())
        {
            if (isSwitchOnImage(isTouchEvent))
            {
                return ImageDecorator.PROFILE_SWITCH_ON_FOCUSED.getImage();
            }
            else
            {
                return ImageDecorator.PROFILE_SWITCH_OFF_FOCUSED.getImage();
            }
        }
        else
        {
            if (isSwitchOnImage(isTouchEvent))
            {
                return ImageDecorator.PROFILE_SWITCH_ON_UNFOCUSED.getImage();
            }
            else
            {
                return ImageDecorator.PROFILE_SWITCH_OFF_UNFOCUSED.getImage();
            }
        }
    }
    
    /**
     * Paint the component to screen
     * 
     * @param graphics (@link AbstractTnGraphics)
     */
    protected void paint(AbstractTnGraphics graphics)
    {
        super.paintBackground(graphics);
        paintItem(graphics);
        if (!isInDragging)
        {
            paintSwitchCommonState(graphics);
        }
        else
        {
            paintSwitchDraggingState(graphics);
        }
    }
    
    //Item with name
    //Ex: Speed Limit, Lane Assist, etc
    private void paintItem(AbstractTnGraphics graphics)
    {
        graphics.setColor(this.isFocused ? focusedColor : unfocusedColor);
        graphics.setFont(font);
        graphics.drawString(itemName, getRightPadding(), this.getPreferredHeight() / 2, AbstractTnGraphics.VCENTER
                | AbstractTnGraphics.LEFT);

        graphics.drawImage(bgImage, this.getPreferredWidth() - getRightPadding(), this.getPreferredHeight() / 2,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.RIGHT);
    }
    
    private void paintSwitchDraggingState(AbstractTnGraphics graphics)
    {
        int imageX, imageY;
        imageY = this.getPreferredHeight() / 2;
        imageX = switchImageXPos;
        graphics.drawImage(getImage(true), imageX, imageY, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);
    }
    
    private void paintSwitchCommonState(AbstractTnGraphics graphics)
    {
        if (isSwitchOn)
        {
            graphics.drawImage(getImage(false), this.getPreferredWidth() - getRightPadding(), this.getPreferredHeight() / 2,
                AbstractTnGraphics.VCENTER | AbstractTnGraphics.RIGHT);
        }
        else
        {
            graphics.drawImage(getImage(false), this.getPreferredWidth() - getRightPadding() - bgImage.getWidth(),
                this.getPreferredHeight() / 2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);
        }
    }
    
    private void initializeBound()
    {
        imageLeft = this.getPreferredWidth() - getRightPadding() - bgImage.getWidth();
        imageTop = 0;
        imageBottom = this.getPreferredHeight();
        imageRight = this.getPreferredWidth() - getRightPadding();
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();

                int action = motionEvent.getAction();
                int x = motionEvent.getX() < 1 ? 0 : motionEvent.getX();
                int y = motionEvent.getY() < 1 ? 0 : motionEvent.getY();
                switch (action)
                {
                    case TnMotionEvent.ACTION_DOWN:
                    {
                        downTouchX = x;
                        break;
                    }
                    case TnMotionEvent.ACTION_MOVE:
                    {
                        if (!isInsideBoundingBox(imageLeft, Integer.MIN_VALUE, imageRight, Integer.MAX_VALUE, x, y))
                        {
                            lastTouchX = x;
                            lastTouchY = y;
                            return false;
                        }
                        
                        if (Math.abs(x - lastTouchX) < X_DISTANCE_THRESHOLD)
                        {
                            return false;
                        }
                        
                        if (!isInDragging
                                && (Math.abs(x - downTouchX) > START_DRAGGING_DISTANCE_THRESHOLD))
                        {
                            disableInterceptTouch(true);
                            isInDragging = true;
                        }
                        
                        if (isDragToSwitchOff(x))
                        {
                            switchImageXPos = Math.max(imageLeft, x - getImage(true).getWidth() / 2);
                        }
                        else
                        {
                            switchImageXPos = Math.min(imageRight - getImage(true).getWidth(), x - getImage(true).getWidth() / 2);
                        }
                        
                        this.requestPaint();
                        break;
                    }
                    case TnMotionEvent.ACTION_UP:
                    case TnMotionEvent.ACTION_CANCEL:
                    {
                        if (isInDragging)
                        {
                            switchChanged(false, x);
                        }
                        else if (isInsideBoundingBox(imageLeft, imageTop, imageRight, imageBottom, x, y))
                        {
                            switchChanged(true, x);
                        }
                        switchImageXPos = 0;
                        isInDragging = false;
                        disableInterceptTouch(false);
                        break;
                    }
                }
                lastTouchX = x;
                lastTouchY = y;
            }
        }
        return false;
    }
    
    private void switchChanged(boolean isClick, int xPos)
    {
        if (isClick)
        {
            this.setSwitchOn(!this.isSwitchOn);
        }
        else
        {
            this.setSwitchOn(!isDragToSwitchOff(xPos));
        }
        
        if (commandListener != null)
        {
            TnUiEvent switchUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
            switchUiEvent.setCommandEvent(new TnCommandEvent(ICommonConstants.CMD_CHANGE_SWITCH));
            this.commandListener.handleUiEvent(switchUiEvent);
        }
    }
    
    private boolean isDragToSwitchOff(int xPos)
    {
        return xPos < imageLeft + bgImage.getWidth() / 2;
    }
    
    protected void disableInterceptTouch(boolean isDisable)
    {
            View nativeView = (View) this.getNativeUiComponent();
            View parent = (View) nativeView.getParent();
            if (parent instanceof ViewGroup)
            {
                ((ViewGroup) parent).requestDisallowInterceptTouchEvent(isDisable);
            }
    }
    
    public boolean isInsideBoundingBox(int x0, int y0, int x1, int y1, int x, int y)
    {
        return x >= x0 && x < x1 && y >= y0 && y < y1;
    }
}
