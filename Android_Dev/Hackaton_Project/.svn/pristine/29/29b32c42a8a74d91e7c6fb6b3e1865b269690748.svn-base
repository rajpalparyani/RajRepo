/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenCheckItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.AbstractFrogLabel;

/**
 * A single CheckBox component. It has two status: selected and not selected.
 * 
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-9-9
 */
public class CitizenCheckItem extends AbstractFrogLabel
{
    protected final static int CMD_ID_ITEM_CHECKED = -1201;

    protected AbstractTnImage selectedIconOn;

    protected AbstractTnImage selectedIconOff;

    protected AbstractTnImage itemIconOn;

    protected AbstractTnImage itemIconOff;  
    
    protected AbstractTnImage checkableIconOff;

    protected boolean isSelected;
    
    protected int style = AbstractTnGraphics.LEFT;
    
    protected boolean isCheckedAsWholeLable;

    protected int[] gap =
    { 0, 0 };

    /**
     * constructor
     * 
     * @param id
     * @param text
     * @param selectedIconOn
     * @param selectedIconOff
     */
    public CitizenCheckItem(int id, String text, AbstractTnImage selectedIconOn, AbstractTnImage selectedIconOff)
    {
        super(id, text);
        if (selectedIconOn == null || selectedIconOff == null)
        {
            throw new IllegalArgumentException("The selectedIcon cannot be null!");
        }
        this.isCheckedAsWholeLable = true;
        this.selectedIconOn = selectedIconOn;
        this.selectedIconOff = selectedIconOff;
    }

    protected CitizenCheckItem(int id, String text, boolean lazyBind)
    {
        super(id, text, lazyBind);
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        AbstractTnImage selectedIcon, itemIcon, checkableIcon;
        if (isFocused())
        {
        	graphics.setColor(focusColor);
        	itemIcon = itemIconOn;
        }
        else
        {
        	graphics.setColor(unfocusColor);
        	itemIcon = itemIconOff;
        }
        
        if (isEnabled())
        {
        	if (isSelected)
        	{
        		selectedIcon = selectedIconOn;
        	}
        	else
        	{
        		selectedIcon = selectedIconOff;
        	}
        	
        }
        else
        {
        	graphics.setColor(TnColor.GRAY);
        	selectedIcon = checkableIconOff;
        }
        
        graphics.setFont(font);
        if ((style & AbstractTnGraphics.LEFT) != 0)
        {
            int left, y;
            left = leftPadding;
            y = (getHeight() - selectedIcon.getHeight()) / 2;
            if (y < topPadding)
                y = topPadding;
            graphics.drawImage(selectedIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            left = leftPadding + gap[0] + selectedIcon.getWidth();

            left = selectedIcon.getWidth() + gap[0] + leftPadding;

            y = (this.getHeight() - font.getHeight()) / 2;
            int scrollWidth = this.getWidth() - selectedIcon.getWidth() - leftPadding - rightPadding - gap[0];

            if (itemIcon != null)
            {
                scrollWidth = this.getWidth() - selectedIcon.getWidth() - leftPadding - rightPadding - gap[0] - gap[1]
                        - itemIcon.getWidth();
            }
            graphics.pushClip(left, y, scrollWidth, this.getHeight());
            FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused || !isScrollable);
            graphics.popClip();

            if (itemIcon != null)
            {
                left = getWidth() - rightPadding - itemIcon.getWidth();
                y = (getHeight() - itemIcon.getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                graphics.drawImage(itemIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
        }
        else if ((style & AbstractTnGraphics.RIGHT) != 0)
        {
            int left, y;
            left = leftPadding;
            if (itemIcon != null)
            {
                y = (getHeight() - itemIcon.getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                graphics.drawImage(itemIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                left = leftPadding + gap[0] + itemIcon.getWidth();
            }

            y = (this.getHeight() - font.getHeight()) / 2;
            int scrollWidth = this.getWidth() - selectedIcon.getWidth() - leftPadding - rightPadding - gap[0];
            if (itemIcon != null)
            {
                scrollWidth = this.getWidth() - selectedIcon.getWidth() - leftPadding - rightPadding - gap[0] - gap[1]
                        - itemIcon.getWidth();
            }

            graphics.pushClip(left, y, scrollWidth, this.getHeight());
            FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused || !isScrollable);
            graphics.popClip();

            left = this.getWidth() - rightPadding - selectedIcon.getWidth();
            y = (getHeight() - selectedIcon.getHeight()) / 2;
            if (y < topPadding)
                y = topPadding;
            graphics.drawImage(selectedIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }

    }

    /**
     * layout of the component
     */
    public void sublayout(int width, int height)
    {
        int maxButtonHeight = 0;
        if (itemIconOn != null || itemIconOff != null)
        {
            if (itemIconOn != null)
            {
                preferWidth = selectedIconOn.getWidth() + itemIconOn.getWidth() + FrogTextHelper.getWidth(this.textLine, font, boldFont)
                        + leftPadding + rightPadding;
                if (itemIconOn.getHeight() > selectedIconOn.getHeight())
                    maxButtonHeight = itemIconOn.getHeight();
                else
                    maxButtonHeight = selectedIconOn.getHeight();
            }
            else if (itemIconOff != null)
            {
                preferWidth = selectedIconOn.getWidth() + itemIconOff.getWidth() + FrogTextHelper.getWidth(this.textLine, font, boldFont)
                        + leftPadding + rightPadding;
                if (itemIconOff.getHeight() > selectedIconOff.getHeight())
                    maxButtonHeight = itemIconOff.getHeight();
                else
                    maxButtonHeight = selectedIconOff.getHeight();
            }
            if (font.getHeight() > maxButtonHeight)
                preferHeight = font.getHeight() + font.getHeight() / 2 + topPadding + bottomPadding;
            else
                preferHeight = maxButtonHeight + font.getHeight() / 2 + topPadding + bottomPadding;
        }
        else
        {
            preferWidth = selectedIconOn.getWidth() + FrogTextHelper.getWidth(this.textLine, font, boldFont) + leftPadding + rightPadding;
            if (selectedIconOn.getHeight() > font.getHeight())
                preferHeight = selectedIconOn.getHeight() + font.getHeight() / 2 + topPadding + bottomPadding;
            else
                preferHeight = font.getHeight() + font.getHeight() / 2 + topPadding + bottomPadding;
        }
    }
    
    public void setIsCheckedAsWholeLable(boolean isCheckedAsWholeLable)
    {
        this.isCheckedAsWholeLable = isCheckedAsWholeLable;
    }

    /**
     * set the selectedIcon of the component
     * 
     * @param selectedIconOn
     * @param selectedIconOff
     */
    public void setSelectedIcon(AbstractTnImage selectedIconOn, AbstractTnImage selectedIconOff)
    {
        this.selectedIconOn = selectedIconOn;
        this.selectedIconOff = selectedIconOff;
        requestLayout();
        requestPaint();
    }

    /**
     * set the itemIcon of the component
     * 
     * @param itemIconOn
     * @param itemIconOff
     */
    public void setItemIcon(AbstractTnImage itemIconOn, AbstractTnImage itemIconOff)
    {
        this.itemIconOn = itemIconOn;
        this.itemIconOff = itemIconOff;
        requestLayout();
        requestPaint();
    }
    
    public void setUnCheckableIcon(AbstractTnImage checkableIconOff)
    {
        this.checkableIconOff = checkableIconOff;  
        requestPaint();
    }

    /**
     * return the selected status of the component
     * 
     * @return true if the component is selected, otherwise false.
     */
    public boolean isSelected()
    {
        return isSelected;
    }

    /**
     * set the selected status of the component
     * 
     * @param isSelected
     */
    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
        requestPaint();
    }
    
    
    /**
     * set the gap of the component
     * 
     * @param gap
     */
    public void setGap(int[] gap)
    {
        this.gap = gap;
    }

    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent event = tnUiEvent.getMotionEvent();
                switch (event.getAction())
                {
                    case TnMotionEvent.ACTION_UP:
                    {
                        int xPos = event.getX();
                        int yPos = event.getY();
                        if (isEnabled() && (isCheckedAsWholeLable || isSelectedIconClicked(xPos,  yPos)))
                        {
                            if (isSelected)
                                isSelected = false;
                            else
                                isSelected = true;

                            requestLayout();
                            requestPaint();

                            if (commandListener != null)
                            {
                                TnUiEvent indexChange = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                                indexChange.setCommandEvent(new TnCommandEvent(CMD_ID_ITEM_CHECKED));
                                this.commandListener.handleUiEvent(indexChange);
                            }
                        }
                    }
                }
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }

    private boolean isSelectedIconClicked(int xPos, int yPos)
    {
        if((style & AbstractTnGraphics.LEFT) != 0)
        {
            int left = leftPadding;
            int right = left + selectedIconOn.getWidth();
            int top = (getHeight() - selectedIconOn.getHeight()) / 2;
            if (top < topPadding)
                top = topPadding;
            int bottom = top + selectedIconOn.getHeight();
            if (xPos > left && xPos < right && yPos > top && yPos < bottom)
                return true;
        }
        else if((style & AbstractTnGraphics.RIGHT) != 0)
        {
            int left = this.getWidth() - rightPadding - selectedIconOn.getWidth();
            int right = this.getWidth() - rightPadding;
            int top = (getHeight() - selectedIconOn.getHeight()) / 2;
            if (top < topPadding)
                top = topPadding;
            int bottom = top + selectedIconOn.getHeight();
            if (xPos > left && xPos < right && yPos > top && yPos < bottom)
                return true;
        }
        return false;
    }

    public int getStyle()
    {
        return style;
    }

    public void setStyle(int style)
    {
        if (style != AbstractTnGraphics.LEFT && style != AbstractTnGraphics.RIGHT)
        {
            throw new IllegalArgumentException("Only support horizental orientation");
        }
        this.style = style;
        this.requestLayout();
        this.requestPaint();
    }

}
