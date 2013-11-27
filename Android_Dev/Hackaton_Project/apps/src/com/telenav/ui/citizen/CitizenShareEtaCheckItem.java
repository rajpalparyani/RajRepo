/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogButton.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.mvc.ICommonConstants;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnNinePatchImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;


/**
 * A component for profile switcher
 * 
 * 
 * @author jyxu (jyxu@telenav.cn)
 * @date 2013-2-20
 */
public class CitizenShareEtaCheckItem extends AbstractTnComponent implements INetworkStatusListener
{
    /**
     * icon of the component when it is focused
     */
    protected AbstractTnImage onImage = ImageDecorator.ROUTE_PLANNING_CHECKBOX_FOCUSED.getImage();
    protected AbstractTnImage offImage = ImageDecorator.ROUTE_PLANNING_CHECKBOX_UNFOCUSED.getImage();
    protected AbstractTnImage disabledImage = ImageDecorator.ROUTE_PLANNING_CHECKBOX_DISABLED.getImage();
    private boolean isChecked = false;
    private String[] texts;
    private AbstractTnFont font;
    private int focusedColor;
    private int unfocusedColor;
    private int disabledColor;
    private int iconTextGap = 12;
    private int textLineGap = 0;
    private boolean isPressed = false;
    
    /**
     * construct a FrogButton object
     * 
     * @param id id of the component
     * @param text text of the component
     */
    public CitizenShareEtaCheckItem(int id)
    {
        super(id);
    }

    public void setTexts(String[] texts)
    {
        this.texts = texts;
    }
    
    public void setFont(AbstractTnFont font)
    {
        this.font = font;
    }
    
    public void setForegroundColor(int focusedColor, int unfocusedColor, int disabledColor)
    {
        this.focusedColor = focusedColor;
        this.unfocusedColor = unfocusedColor;
        this.disabledColor = disabledColor;
    }
    
    public void sublayout(int width, int height)
    {
       
    }
    
    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
        this.requestPaint();
    }
    
    public boolean isChecked()
    {
        return isChecked;
    }

    /**
     * Paint the component to screen
     * 
     * @param graphics (@link AbstractTnGraphics)
     */
    protected void paint(AbstractTnGraphics graphics)
    {
        int leftPading = (this.getPreferredWidth() - onImage.getWidth() - iconTextGap -  getMaxTextWidth(texts))/2;
        if (!isEnabled())
        {
            // the check item is disabled
            TnNinePatchImage disabledImageBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                    .decorate(NinePatchImageDecorator.ROUTE_PLANNING_SHARE_ETA_BG_UNCHECKED_UNFOCUSED);
            if (disabledImageBg != null)
            {
                disabledImageBg.setWidth(this.getPreferredWidth());
                disabledImageBg.setHeight(this.getPreferredHeight());
                graphics.drawImage(disabledImageBg, 0, 0,
                    AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
           graphics.drawImage(disabledImage, leftPading, this.getPreferredHeight()/2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);
        }
        else if (isPressed)
        {
            if(isChecked)
            {
                TnNinePatchImage checkedImageBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                        .decorate(NinePatchImageDecorator.ROUTE_PLANNING_SHARE_ETA_BG_CHECKED_FOCUSED);
                if (checkedImageBg != null)
                {
                    checkedImageBg.setWidth(this.getPreferredWidth());
                    checkedImageBg.setHeight(this.getPreferredHeight());
                    graphics.drawImage(checkedImageBg, 0, 0,
                        AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
               graphics.drawImage(onImage, leftPading, this.getPreferredHeight()/2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);
            }
            else
            {
                TnNinePatchImage checkedImageBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                        .decorate(NinePatchImageDecorator.ROUTE_PLANNING_SHARE_ETA_BG_UNCHECKED_FOCUSED);
                if (checkedImageBg != null)
                {
                    checkedImageBg.setWidth(this.getPreferredWidth());
                    checkedImageBg.setHeight(this.getPreferredHeight());
                    graphics.drawImage(checkedImageBg, 0, 0,
                        AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
               graphics.drawImage(offImage,leftPading, this.getPreferredHeight()/2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);
            }
        }
        else
        {
            if(isChecked)
            {
                TnNinePatchImage checkedImageBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                        .decorate(NinePatchImageDecorator.ROUTE_PLANNING_SHARE_ETA_BG_CHECKED_UNFOCUSED);
                if (checkedImageBg != null)
                {
                    checkedImageBg.setWidth(this.getPreferredWidth());
                    checkedImageBg.setHeight(this.getPreferredHeight());
                    graphics.drawImage(checkedImageBg, 0, 0,
                        AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
               graphics.drawImage(onImage, leftPading, this.getPreferredHeight()/2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);
            }
            else
            {
                TnNinePatchImage uncheckedImageBg = (TnNinePatchImage) NinePatchImageDecorator.instance
                        .decorate(NinePatchImageDecorator.ROUTE_PLANNING_SHARE_ETA_BG_UNCHECKED_UNFOCUSED);
                if (uncheckedImageBg != null)
                {
                    uncheckedImageBg.setWidth(this.getPreferredWidth());
                    uncheckedImageBg.setHeight(this.getPreferredHeight());
                    graphics.drawImage(uncheckedImageBg, 0, 0,
                        AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
               graphics.drawImage(offImage, leftPading, this.getPreferredHeight()/2, AbstractTnGraphics.VCENTER | AbstractTnGraphics.LEFT);
            }
        }
        graphics.setColor(this.isEnabled() ? (this.isFocused ? focusedColor: unfocusedColor) : disabledColor);
        graphics.setFont(font);
        int topPadding = (this.getPreferredHeight() - font.getHeight()*texts.length - textLineGap*(texts.length -1))/2;
        int textTop = topPadding;
        int textLeft = leftPading + onImage.getWidth() + iconTextGap;
        for (int i = 0; i < texts.length; i++)
        {
            graphics.drawString(texts[i], textLeft, textTop, AbstractTnGraphics.TOP
                    | AbstractTnGraphics.LEFT);
            textTop += (font.getHeight() + textLineGap);
        }
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();

                int action = motionEvent.getAction();
                int x = motionEvent.getX();
                int y = motionEvent.getY();
                switch (action)
                {
                    case TnMotionEvent.ACTION_DOWN:
                    {
                        isPressed = true;
                        this.requestPaint();
                        break;
                    }
                    case TnMotionEvent.ACTION_MOVE:
                    case TnMotionEvent.ACTION_CANCEL:
                    {
                        isPressed = false;
                        this.requestPaint();
                        break;
                    }
                    case TnMotionEvent.ACTION_UP:
                    {
                        isPressed = false;
                        int imageLeft = 0;
                        int imageTop = 0;
                        int imageBottom = this.getPreferredHeight();
                        int imageRight = this.getPreferredWidth();
                        if(isInsideBoundingBox(imageLeft, imageTop,imageRight, imageBottom,  x, y))
                        {
                            if(commandListener != null)
                            {
                                TnUiEvent switchUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                                switchUiEvent.setCommandEvent(new TnCommandEvent(ICommonConstants.CMD_CHECK_SHARE_ETA));
                                this.commandListener.handleUiEvent(switchUiEvent);
                                return false;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isInsideBoundingBox(int x0, int y0, int x1, int y1, int x, int y)
    {
        return x >= x0 && x < x1 && y >= y0 && y < y1;
    }
    
    public int getMaxTextWidth(String[] strs)
    {
        int maxWidth = 0;
        if(strs != null)
        {
            for(int i=0; i<strs.length; i++)
            {
                if(strs[i] != null)
                {
                    maxWidth = Math.max(maxWidth, font.stringWidth(strs[i]));
                }
            }
        }
        return maxWidth;
    }

    @Override
    public void statusUpdate(final boolean isConnected)
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                setEnabled(isConnected);
                requestPaint();
            }
        });
    }
    
    @Override
    protected void onDisplay()
    {
        super.onDisplay();
        NetworkStatusManager.getInstance().addStatusListener(this);
    }
    
    @Override
    protected void onUndisplay()
    {
        super.onUndisplay();
        NetworkStatusManager.getInstance().removeStatusListener(this);
    }
    
}
