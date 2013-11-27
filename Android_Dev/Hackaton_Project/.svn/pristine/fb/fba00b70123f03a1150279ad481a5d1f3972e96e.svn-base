/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogButton.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.frogui.text.FrogTextHelper;

/**
 * A component for button
 * 
 * You can set the gap and the icon of the button by corresponding setXX() methods.
 * 
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-7-2
 */
public class FrogButton extends AbstractFrogLabel
{
    /**
     * style for the component
     */
    protected int style = AbstractTnGraphics.LEFT;

    /**
     * the gap between icon and text
     */
    protected int gap;

    /**
     * icon of the component when it is focused
     */
    protected AbstractTnImage iconFocus;

    /**
     * icon of the component when it is not focused
     */
    protected AbstractTnImage iconUnfocus;

    /**
     * construct a FrogButton object
     * 
     * @param id id of the component
     * @param text text of the component
     */
    public FrogButton(int id, String text)
    {
        super(id, text);
        this.setFocusable(true);
    }

    /**
     * construct a FrogButton object
     * 
     * @param id id of the component
     */
    public FrogButton(int id)
    {
        this(id, null);
    }

    /**
     * Paint the component to screen
     * 
     * @param graphics (@link AbstractTnGraphics)
     */
    protected void paint(AbstractTnGraphics graphics)
    {
        int y = 0;
        AbstractTnImage buttonIcon;
        if (isFocused())
        {
            graphics.setColor(focusColor);
            buttonIcon = iconFocus;
        }
        else
        {
            graphics.setColor(unfocusColor);
            buttonIcon = iconUnfocus;
        }
        graphics.setFont(font);
        int left;
        if (buttonIcon != null)
        {
            if ((style & AbstractTnGraphics.LEFT) != 0)
            {
                left = leftPadding;
                y = (this.getHeight() - buttonIcon.getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                left = leftPadding + buttonIcon.getWidth() + gap;
                if ((style & AbstractTnGraphics.HCENTER) != 0)
                {
                    int textTypingWidth = this.getWidth() - leftPadding - buttonIcon.getWidth() - gap - rightPadding;
                    left = buttonIcon.getWidth() + gap + leftPadding
                            + (textTypingWidth - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont)) / 2;
                    if (left < buttonIcon.getWidth() + gap + leftPadding)
                        left = buttonIcon.getWidth() + gap + leftPadding;
                }
                y = (this.getHeight() - font.getHeight()) / 2;
                int scrollWidth = this.getWidth() - buttonIcon.getWidth() - leftPadding - rightPadding - gap;
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                        || !isScrollable);
                graphics.popClip();
            }
            else if ((style & AbstractTnGraphics.RIGHT) != 0)
            {
                left = leftPadding;
                if ((style & AbstractTnGraphics.HCENTER) != 0)
                {
                    int textTypingWidth = this.getWidth() - leftPadding - buttonIcon.getWidth() - gap - rightPadding;
                    left = leftPadding + (textTypingWidth - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont)) / 2;
                    if (left < leftPadding)
                        left = leftPadding;
                }
                y = (this.getHeight() - font.getHeight()) / 2;
                int scrollWidth = this.getWidth() - buttonIcon.getWidth() - leftPadding - rightPadding - gap;
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                        || !isScrollable);
                graphics.popClip();
                y = (this.getHeight() - buttonIcon.getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                left = this.getWidth() - rightPadding - buttonIcon.getWidth();
                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                left = this.getWidth() - rightPadding - buttonIcon.getWidth() - gap;
            }
            else if ((style & AbstractTnGraphics.TOP) != 0)
            {
                left = (this.getWidth() - buttonIcon.getWidth()) / 2;
                if (this.textLine != null)
                {
                    int fontHeight = font.getHeight() < boldFont.getHeight() ? boldFont.getHeight() : font.getHeight();
                    
                    y = topPadding + (this.getHeight() - topPadding - bottomPadding - buttonIcon.getHeight() - fontHeight - gap) / 2;
                    if(y < 0)
                        y = 0;
                    
                    graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    left = (this.getWidth() - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont)) / 2;
                    if (left < leftPadding)
                    {
                        left = leftPadding;
                    }
                    y += buttonIcon.getHeight() + gap;
                    int scrollWidth = this.getWidth() - leftPadding - rightPadding;
                    graphics.pushClip(left, y, scrollWidth, this.getHeight());
                    FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                            || !isScrollable);
                    graphics.popClip();
                }
                else
                {
                    y = topPadding + (this.getHeight() - topPadding - bottomPadding - buttonIcon.getHeight()) / 2;
                    if (y < topPadding)
                        y = topPadding;
                    left = (this.getWidth() - buttonIcon.getWidth()) / 2;
                    if (left < leftPadding)
                        left = leftPadding;
                    graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
            }
            else if ((style & AbstractTnGraphics.BOTTOM) != 0)
            {
                if (this.textLine != null)
                {
                    left = (this.getWidth() - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont)) / 2;
                    if (left < leftPadding)
                    {
                        left = leftPadding;
                    }
                    y = topPadding;
                    int scrollWidth = this.getWidth() - leftPadding - rightPadding;
                    graphics.pushClip(left, y, scrollWidth, this.getHeight());
                    FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                            || !isScrollable);
                    graphics.popClip();
                    y = topPadding + gap + font.getHeight();
                    left = (this.getWidth() - buttonIcon.getWidth()) / 2;
                    graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
                else
                {
                    y = topPadding + (this.getHeight() - topPadding - bottomPadding - buttonIcon.getHeight()) / 2;
                    if (y < topPadding)
                        y = topPadding;
                    left = (this.getWidth() - buttonIcon.getWidth()) / 2;
                    if (left < leftPadding)
                        left = leftPadding;
                    graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
            }
            else
            {
                int leftStart = ( getWidth() - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont) - gap - buttonIcon.getWidth() ) / 2;
                if(leftStart < leftPadding)
                    leftStart = leftPadding;
                left = leftStart;
                y = topPadding + (this.getHeight() - topPadding - bottomPadding - buttonIcon.getHeight()) / 2;
               
                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                left = left + buttonIcon.getWidth() + gap;
                y = (this.getHeight() - font.getHeight()) / 2;
                int scrollWidth = this.getWidth() - leftStart - buttonIcon.getWidth() - gap - rightPadding;
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                        || !isScrollable);
                graphics.popClip();
            }
        }
        else
        {
            y = topPadding + (this.getHeight() - bottomPadding - topPadding - font.getHeight()) / 2;
            int textTypingWidth = this.getWidth() - leftPadding - rightPadding;
            left = leftPadding + (textTypingWidth - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont)) / 2;
            if (left < leftPadding)
                left = leftPadding;
            int scrollWidth = this.getWidth() - leftPadding - rightPadding;
            graphics.pushClip(left, y, scrollWidth, this.getHeight());
            FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused || !isScrollable);
            graphics.popClip();
        }
    }

    /**
     * retrieve the gap between the icon and text
     * 
     * @return gap between the icon and text
     */
    public int getGap()
    {
        return gap;
    }

    /**
     * set the gap between icon and text
     * 
     * @param gap gap between icon and text
     */
    public void setGap(int gap)
    {
        this.gap = gap;
        requestLayout();
        requestPaint();
    }

    /**
     * Retrieve focused image.
     * 
     * @return focused image
     */
    public AbstractTnImage getFocusedIcon()
    {
        return this.iconFocus;
    }
    
    /**
     * Retrieve unfocused image.
     * 
     * @return unfocused image
     */
    public AbstractTnImage getUnfocusedIcon()
    {
        return this.iconUnfocus;
    }
    
    /**
     * Retrieve icon align style.
     * 
     * @return icon align style
     */
    public int getIconStyle()
    {
        return this.style;
    }
    
    /**
     * set the icon and its style of the component
     * 
     * @param iconFocus icon of the component when it is focused
     * @param iconBlur icon of the component when it is not focused
     * @param style style of the icon of the component
     */
    public void setIcon(AbstractTnImage iconFocus, AbstractTnImage iconBlur, int style)
    {
        setIcon(iconFocus, iconBlur, style, true);
    }
    
    
    /**
     * set the icon and its style of the component
     * 
     * @param iconFocus icon of the component when it is focused
     * @param iconBlur icon of the component when it is not focused
     * @param style style of the icon of the component
     */
    public void setIcon(AbstractTnImage iconFocus, AbstractTnImage iconBlur, int style, boolean needLayout)
    {
        this.iconFocus = iconFocus;
        this.iconUnfocus = iconBlur;
        this.style = style;
        
        if(needLayout)
        {
            requestLayout();
            requestPaint();
        }
    }

    /**
     * layout the component by default
     */
    public void sublayout(int width, int height)
    {
        if (iconFocus == null && iconUnfocus == null)
        {
            super.sublayout(width, height);
        }
        else
        {
            int iconHeight = 0;
            AbstractTnImage tmpImage = iconFocus != null ? iconFocus : iconUnfocus;
            if ((style & AbstractTnGraphics.LEFT) != 0 || (style & AbstractTnGraphics.RIGHT) != 0)
            {
                int textWidth = FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont);
                preferWidth = tmpImage.getWidth() + textWidth + leftPadding + rightPadding + gap;
                iconHeight = tmpImage.getHeight();
                if (iconHeight > font.getHeight())
                {
                    preferHeight = iconHeight + topPadding + bottomPadding;
                }
                else
                {
                    preferHeight = font.getHeight() + topPadding + bottomPadding;
                }
            }
            else if ((style & AbstractTnGraphics.TOP) != 0 || (style & AbstractTnGraphics.BOTTOM) != 0)
            {
                preferHeight = tmpImage.getHeight() + topPadding + bottomPadding;
                if (this.textLine != null)
                    preferHeight += font.getHeight() + gap;
                int textWidth = FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont);
                if (tmpImage.getWidth() > textWidth)
                {
                    preferWidth = tmpImage.getWidth() + leftPadding + rightPadding;
                }
                else
                {
                    preferWidth = textWidth + leftPadding + rightPadding;
                }
            }
        }
    }
}
