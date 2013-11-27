/**
*
* Copyright 2010 TeleNav, Inc. All rights reserved.
* FrogButton.java
*
*/
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;

/**
* A component for button which support multi line text as label
* 
* You can set the gap and the icon of the button by corresponding setXX() methods.
* 
* @author bmyang (bmyang@telenav.cn)
* @date 2011-6-7
*/
public class FrogMultiLineButton extends FrogMultiLine
{
	 /**
     * color of the text when the component is focused
     */
    protected int focusColor = TnColor.WHITE;

    /**
     * color of the text when the component is not focused
     */
    protected int unfocusColor = TnColor.BLACK;
   
    /**
     * style for the component
     */
    protected int style = AbstractTnGraphics.LEFT;

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
    public FrogMultiLineButton(int id, String text)
    {
        super(id, text);
        this.setTextAlign(TEXT_ALIGN_CENTER);
        this.setFocusable(true);
    }

    /**
     * construct a FrogButton object
     * 
     * @param id id of the component
     */
    public FrogMultiLineButton(int id)
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
            this.foregroundColor = focusColor;
        }
        else
        {
            graphics.setColor(unfocusColor);
            buttonIcon = iconUnfocus;
            this.foregroundColor = unfocusColor;
        }
        graphics.setFont(font);
        int left;
  //      setTextAlign(style);
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
                y = (this.getHeight() - realTextHeight) / 2;
                paintText(graphics, buttonIcon.getWidth() + gap, y);
            }
            else if ((style & AbstractTnGraphics.RIGHT) != 0)
            {
                left = leftPadding;
                if ((style & AbstractTnGraphics.HCENTER) != 0)
                {
                    int textTypingWidth = this.getWidth() - leftPadding - buttonIcon.getWidth() - gap - rightPadding;
                    left = leftPadding + (textTypingWidth - measureWidth) / 2;
                    if (left < leftPadding)
                        left = leftPadding;
                }
                y = (this.getHeight() - realTextHeight) / 2;
                paintText(graphics, -buttonIcon.getWidth() - gap, y);
//                int scrollWidth = this.getWidth() - buttonIcon.getWidth() - leftPadding - rightPadding - gap;
//                graphics.pushClip(left, y, scrollWidth, this.getHeight());
//                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
//                        || !isScrollable);
//                graphics.popClip();
                y = (this.getHeight() - realTextHeight) / 2;
                if (y < topPadding)
                    y = topPadding;
                left = this.getWidth() - rightPadding - buttonIcon.getWidth();
                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                left = this.getWidth() - rightPadding - buttonIcon.getWidth() - gap;
            }
            else if ((style & AbstractTnGraphics.TOP) != 0)
            {
                left = (this.getWidth() - buttonIcon.getWidth()) / 2;
                if (this.text != null)
                {
                    int fontHeight = font.getHeight() < boldFont.getHeight() ? boldFont.getHeight() : font.getHeight();
                    
                    y = topPadding + (this.getHeight() - topPadding - bottomPadding - buttonIcon.getHeight() - realTextHeight - gap) / 2;
                    if(y < 0)
                        y = 0;
                    
                    graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    left = (this.getWidth() - measureWidth) / 2;
                    if (left < leftPadding)
                    {
                        left = leftPadding;
                    }
                    y += buttonIcon.getHeight() + gap;
//                    int scrollWidth = this.getWidth() - leftPadding - rightPadding;
//                    graphics.pushClip(left, y, scrollWidth, this.getHeight());
//                    FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
//                            || !isScrollable);
//                    graphics.popClip();
                    paintText(graphics, buttonIcon.getWidth() + gap, y);
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
                if (this.text != null)
                {
                    left = (this.getWidth() - measureWidth) / 2;
                    if (left < leftPadding)
                    {
                        left = leftPadding;
                    }
                    y = topPadding;
//                    int scrollWidth = this.getWidth() - leftPadding - rightPadding;
//                    graphics.pushClip(left, y, scrollWidth, this.getHeight());
//                    FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
//                            || !isScrollable);
//                    graphics.popClip();
                    paintText(graphics, buttonIcon.getWidth() + gap, y);
                    y = topPadding + gap + realTextHeight;
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
                int leftStart = ( getWidth() - measureWidth - gap - buttonIcon.getWidth() ) / 2;
                if(leftStart < leftPadding)
                    leftStart = leftPadding;
                left = leftStart;
                y = (this.getHeight() - buttonIcon.getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                left = left + buttonIcon.getWidth() + gap;
                y = (this.getHeight() - font.getHeight()) / 2;
//                int scrollWidth = this.getWidth() - leftStart - buttonIcon.getWidth() - gap - rightPadding;
//                graphics.pushClip(left, y, scrollWidth, this.getHeight());
//                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
//                        || !isScrollable);
//                graphics.popClip();
                paintText(graphics, buttonIcon.getWidth() + gap, y);
            }
        }
        else
        {
            y = (this.getHeight() - realTextHeight) / 2 + this.topPadding;
            if (y < topPadding)
                y = topPadding;
            paintText(graphics, 0, y);
        }
    }

    public void setForegroundColor(int focusColor, int unfocusColor)
    {
        if(this.focusColor == focusColor && this.unfocusColor == unfocusColor)
            return;
        
        this.focusColor = focusColor;
        this.unfocusColor = unfocusColor;

        if (this.nativeUiComponent != null)
            this.requestPaint();
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

    protected int getLineHeight()
    {
        this.lineHeight = font.getHeight();
        return lineHeight;
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
        this.iconFocus = iconFocus;
        this.iconUnfocus = iconBlur;
        this.style = style;
        requestLayout();
        requestPaint();
    }

    /**
     * layout the component by default
     */
    public void sublayout(int width, int height)
    {
    	super.sublayout(width, height);
        if (iconFocus != null && iconUnfocus != null)
        {
        	int iconHeight = 0;
            AbstractTnImage tmpImage = iconFocus;
            if ((style & AbstractTnGraphics.LEFT) != 0 || (style & AbstractTnGraphics.RIGHT) != 0)
            {
                preferWidth = tmpImage.getWidth() + measureWidth + leftPadding + rightPadding + gap;
                iconHeight = tmpImage.getHeight();
                if (iconHeight > realTextHeight)
                {
                    preferHeight = iconHeight + topPadding + bottomPadding;
                }
                else
                {
                    preferHeight = realTextHeight + topPadding + bottomPadding;
                }
            }
            else if ((style & AbstractTnGraphics.TOP) != 0 || (style & AbstractTnGraphics.BOTTOM) != 0)
            {
                preferHeight = tmpImage.getHeight() + topPadding + bottomPadding;
                if (this.text != null)
                    preferHeight += realTextHeight + gap;
                if (tmpImage.getWidth() > measureWidth)
                {
                    preferWidth = tmpImage.getWidth() + leftPadding + rightPadding;
                }
                else
                {
                    preferWidth = measureWidth + leftPadding + rightPadding;
                }
            }
        }
    }
}
