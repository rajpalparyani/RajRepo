/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTnAddressListItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.frogui.widget.FrogListItem;

/**
 * @author jyxu (jyxu@telenav.cn)
 * @date 2012-11-21
 */
public class CitizenProfileListItem extends FrogListItem
{
    protected String title = "";
    protected String value = "";
    protected int titleFocusedColor = TnColor.WHITE;
    protected int titleUnFocuedColor = TnColor.BLACK;
    protected int valueFocusedColor = TnColor.WHITE;
    protected int valueUnFocusedColor = TnColor.BLACK;
    protected int bgFocusedColor = TnColor.BLACK;
    protected int bgUnFocusedColor = TnColor.WHITE;
    protected int topGap = 0;
    protected static final int LINE_GAP  = 4;

    /**
     * Create a FrogPoiListItem with specific Id.
     * <br>
     * <b>This method won't realize the item instantly.</b>
     * 
     * @param id
     */
    public CitizenProfileListItem(int id)
    {
        super(id);
    }
    
    /**
     * Set title for poiListItem.
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * Get the title of profile list item
     * @return
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * set title text color
     * @param focusedColor
     * @param unFocusedColor
     */
    public void setTitleColor(int focusedColor, int unFocusedColor)
    {
        this.titleFocusedColor = focusedColor;
        this.titleUnFocuedColor = unFocusedColor;
    }
    
    /**
     * Retrieve the title text color
     * @param isFocused
     * @return
     */
    public int getTitleColor(final boolean isFocused)
    {
        return isFocused? titleFocusedColor : titleUnFocuedColor;
    }
    
    /**
     * Set Address for poiListItem.
     * 
     * @param value
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Get value of profile list item (second line)
     * @return
     */
    public String getValue()
    {
        return value;
    }
    
    public void setValueColor(int focusedColor, int unFocusedColor)
    {
        this.valueFocusedColor = focusedColor;
        this.valueUnFocusedColor = unFocusedColor;
    }
    
    public void setBackgroundColor(int bgFocusedColor, int bgUnFocusedColor)
    {
        this.bgFocusedColor = bgFocusedColor;
        this.bgUnFocusedColor = bgUnFocusedColor;
    }

    /**
     * Implements custom layout features for this component. 
     * 
     * @param width the width of the component
     * @param height the height of the component
     */
    public void sublayout(int width, int height)
    {
        if (font != null && boldFont != null)
        {
            topGap = (this.getPreferredHeight() - font.getHeight() - boldFont.getHeight()- LINE_GAP)/2; 
        }
    }
    
    public AbstractTnFont getTitleFont()
    {
        if(boldFont != null)
        {
            return boldFont;
        }
        else
        {
            return font;
        }
    }
    
    public AbstractTnFont getValueFont()
    {
        return font;
    }

    protected void paint(AbstractTnGraphics g)
    {
        // g.setColor(isFocused ? bgFocusedColor : bgUnFocusedColor);
        // g.fillRect(0, 0, this.getPreferredWidth(), this.getPreferredHeight());
        super.paint(g);
        boolean isTitleAvailable = (title != null && title.length() > 0);
        boolean isValueAvailable = (value != null && value.length() > 0);

        int titleY = topGap;

        AbstractTnFont titleFont = getTitleFont();
        if (titleFont == null)
        {
            titleFont = g.getFont();
        }

        AbstractTnFont valueFont = getValueFont();
        if (valueFont == null)
        {
            valueFont = g.getFont();
        }

        if (isTitleAvailable)
        {
            if (isValueAvailable)
            {
                titleY = topGap;
            }
            else
            {
                titleY = (this.getPreferredHeight() - titleFont.getHeight()) / 2;
            }
            g.setColor(isFocused ? titleFocusedColor : this.titleUnFocuedColor);
            g.setFont(titleFont);
            g.drawString(title, this.getLeftPadding(), titleY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }

        if (isValueAvailable)
        {
            int ValueY;
            if (isTitleAvailable)
            {
                ValueY = titleY + LINE_GAP + titleFont.getHeight();
            }
            else
            {
                ValueY = (this.getPreferredHeight() - valueFont.getHeight()) / 2;
            }

            g.setColor(isFocused ? valueFocusedColor : this.valueUnFocusedColor);
            g.setFont(valueFont);
            if (valueFont.stringWidth(value) < this.getPreferredWidth() - 2 * leftPadding)
            {
                g.drawString(value, this.getLeftPadding(), ValueY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
            else
            {
                String tempValue = value;
                while (valueFont.stringWidth(tempValue + "...") > this.getPreferredWidth() - 2 * this.getLeftPadding())
                {
                    tempValue = tempValue.substring(0, tempValue.length() - 1);
                }
                g.drawString(tempValue + "...", this.getLeftPadding(), ValueY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
        }
    }
  
}
