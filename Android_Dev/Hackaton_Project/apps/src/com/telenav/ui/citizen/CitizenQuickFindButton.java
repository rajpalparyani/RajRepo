/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenQuickFindButton.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-10-21
 */
public class CitizenQuickFindButton extends FrogButton
{

    private AbstractTnImage unfocusedSympolImage;
    private AbstractTnImage focusedSympolImage;
	private int shadow;
	private int backgroundCircleFocusedColor;
	private boolean isEvent = false;
	private String eventId;

    public CitizenQuickFindButton(int id, String text)
    {
        super(id, text);
        this.style = AbstractTnGraphics.TOP;
    }
    

    public void setSympolImage(AbstractTnImage unfocusedSympolImage, AbstractTnImage focusedSympolImage)
    {
        this.unfocusedSympolImage = unfocusedSympolImage;
        this.focusedSympolImage = focusedSympolImage;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        AbstractTnImage sympolImage;
        int y = 0;
        AbstractTnImage buttonIcon;
        if (isFocused())
        {
            graphics.setColor(focusColor);
            buttonIcon = iconFocus;
            sympolImage = focusedSympolImage;
        }
        else
        {
            graphics.setColor(unfocusColor);
            buttonIcon = iconUnfocus;
            sympolImage = unfocusedSympolImage;
        }
        graphics.setFont(font);
        int fontHeight = font.getHeight() < boldFont.getHeight() ? boldFont
                .getHeight() : font.getHeight();
        int left;
        if (buttonIcon != null)
        {
            left = (this.getPreferredWidth()>> 1) ;
            if (this.textLine != null)
            {

                y = topPadding
                        + (this.getHeight() - topPadding - bottomPadding
                                - buttonIcon.getHeight() - fontHeight - gap) / 2;
                if (y < 0)
                    y = 0;

                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.TOP);
                if (sympolImage != null)
                {
                    int sympolImageX = (this.getPreferredWidth() - shadow)>> 1;
                    if (this.textLine != null)
                    {
                        
                        int sympolImageY = topPadding
                                + (this.getHeight() - topPadding - bottomPadding
                                        - sympolImage.getHeight() - fontHeight - gap) / 2;
                        if(AppConfigHelper.BRAND_SPRINT.equals(AppConfigHelper.brandName) ||
                                AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName)||
                                AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
                        {
                            if (isFocused())
                            {
                                int sympolImageWidth = sympolImage.getWidth();
                                int sympolImageHeight = sympolImage.getHeight();
                                
                                int cx = sympolImageX;
                                int cy = sympolImageY + sympolImageHeight / 2;
                                int r = Math.max(sympolImageWidth, sympolImageHeight) / 2;
                                
                                graphics.setAntiAlias(true);
                                int origColor = graphics.getColor();
                                graphics.setColor(backgroundCircleFocusedColor);
                                graphics.fillCircle(cx , cy, r, false);
                                graphics.setColor(origColor);
                            }
                        }
                        
                        graphics.drawImage(sympolImage, sympolImageX, sympolImageY, AbstractTnGraphics.TOP
                                | AbstractTnGraphics.HCENTER);
                    }
                }
                left = (this.getWidth() - FrogTextHelper.getWidth(this.textLine,
                    this.font, this.boldFont)) / 2;
                if (left < leftPadding)
                {
                    left = leftPadding;
                }
                y += buttonIcon.getHeight() + gap;
                int scrollWidth = this.getWidth() - leftPadding - rightPadding;
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine,
                    font, boldFont, scrollWidth, !isFocused || !isScrollable);
                graphics.popClip();
            }
            else
            {
                y = topPadding
                        + (this.getHeight() - topPadding - bottomPadding - buttonIcon
                                .getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                left = (this.getWidth() - buttonIcon.getWidth()) / 2;
                if (left < leftPadding)
                    left = leftPadding;
                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT
                        | AbstractTnGraphics.TOP);
                if (sympolImage != null)
                {
                    int sympolImageX = (this.getPreferredWidth() - shadow)>> 1;
                    int sympolImageY = topPadding
                            + (this.getHeight() - topPadding - bottomPadding
                                    - sympolImage.getHeight() - gap) / 2;
                    if (sympolImageY < topPadding)
                        sympolImageY = topPadding;
                    graphics.drawImage(sympolImage, sympolImageX, sympolImageY,
                        AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
                }
            }
        }
        else
        {
            y = topPadding
                    + (this.getHeight() - topPadding - bottomPadding) / 2;
            if (y < topPadding)
                y = topPadding;
            left = this.getWidth() / 2;
            if (left < leftPadding)
                left = leftPadding;
            if (sympolImage != null)
            {
                int sympolImageX = (this.getPreferredWidth() - shadow)>> 1;
                int sympolImageY = topPadding
                        + (this.getHeight() - topPadding - bottomPadding
                                - sympolImage.getHeight() - gap) / 2;
                if(this.textLine != null)
                {
                    sympolImageY -= fontHeight / 2;
                }
                graphics.drawImage(sympolImage, sympolImageX, sympolImageY,
                    AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
            }
        }
        
    }

    
    public void setShadow(int shadow)
    {
    	this.shadow = shadow;
    }
    
    public void setBackgroundCircleFocusedColor(int focused)
    {
        backgroundCircleFocusedColor = focused;
    }
    
    public void setIsEvent(boolean isEvent)
    {
        this.isEvent = isEvent;
    }
    
    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }
    
    public boolean isEvent()
    {
        return isEvent;
    }
    
    public String getEventId()
    {
        return eventId;
    }
}
