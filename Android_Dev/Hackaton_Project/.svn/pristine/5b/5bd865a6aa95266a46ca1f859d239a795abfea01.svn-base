/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CitizenButtonWithBadge.java
 *
 */
package com.telenav.ui.citizen;


import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-2-17
 */
public class CitizenButtonWithBadge extends CitizenButton
{

    protected AbstractTnImage badgeImage;
    
    protected TnTextLine badge;
    
    protected int badgeFocusedColor;
    
    protected int badgeBlurColor;
    
    protected AbstractTnFont badgeFont;
    
    protected int badgePosition = AbstractTnGraphics.RIGHT;
    
    public final static int SAME_Y_AS_ICON = 0; 
    
    protected int badgeWidth;
    
    protected int badgeHeight;
    
    /**
     * constructor 
     * @param id
     * @param text
     * @param buttonIconFocus
     * @param buttonIconBlur
     * @param badge
     * @param badgeImage
     */
    public CitizenButtonWithBadge(int id, String text, AbstractTnImage buttonIconFocus, AbstractTnImage buttonIconBlur, AbstractTnImage badgeImage)
    {
        super(id, text, buttonIconFocus, buttonIconBlur);
    }

    /**
     * constructor
     * @param id
     * @param text
     * @param buttonIconFocus
     * @param buttonIconBlur
     */
    public CitizenButtonWithBadge(int id, String text, AbstractTnImage buttonIconFocus, AbstractTnImage buttonIconBlur)
    {
        super(id, text, buttonIconFocus, buttonIconBlur);
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        super.paint(graphics);
        if( badgeImage != null)
        {
            int oldBadgeImageWidth = badgeImage.getWidth();
            int oldBadgeImageHeight = badgeImage.getHeight();
            badgeImage.setWidth(badgeWidth);
            badgeImage.setHeight(badgeHeight);
            
            int left = 0;
            int y = 0;
            if((this.badgePosition & AbstractTnGraphics.RIGHT) != 0)
            {
                int rightIconWidht = rightButtonIconFocus == null ? 0: rightButtonIconFocus.getWidth();
                left = this.getWidth() - this.getRightPadding() - rightIconWidht - this.getGap() - badgeWidth;
                y = (this.getHeight() - badgeHeight) / 2;
                if(y < this.getTopPadding())
                    y = this.getTopPadding();
                graphics.drawImage(badgeImage, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
            else if ((this.badgePosition & AbstractTnGraphics.TOP) != 0)
            {
                // left = this.getWidth() - this.getRightPadding() - badgeImage.getWidth();
                left = this.getWidth() / 2 + this.getFocusedIcon().getWidth() / 4;
                int fontHeight = font.getHeight() < boldFont.getHeight() ? boldFont.getHeight() : font.getHeight();
                int iconY = topPadding + (this.getHeight() - topPadding - bottomPadding - getFocusedIcon().getHeight() - fontHeight - gap) / 2;
                int badageTopHeight =  badgeHeight / 3; 
                if(iconY < badageTopHeight)
                {
                    y = iconY;
                }
                else 
                {
                    y = iconY- badageTopHeight;
                }
                graphics.drawImage(badgeImage, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
            else if(this.badgePosition == this.SAME_Y_AS_ICON)
            {
                left = this.getFocusedIcon().getWidth() * 5 / 8;
                y = topPadding;
                graphics.drawImage(badgeImage, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
                
                
            left += (badgeWidth - FrogTextHelper.getWidth(this.badge, badgeFont, this.boldFont)) / 2;
            y += (badgeHeight + 1) / 2;
            if(isFocused())
            {
                graphics.setColor(badgeFocusedColor);
            }
            else
            {
                graphics.setColor(badgeBlurColor);
            }
            if(!"".equalsIgnoreCase(badge.getText()))
            {
                graphics.setFont(badgeFont);
                graphics.drawString(badge.getText(), left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.FONT_ABSOLUTE_VCENTER);
            }
            
            badgeImage.setWidth(oldBadgeImageWidth);
            badgeImage.setHeight(oldBadgeImageHeight);
        }
    }

    /**
     * retrieve the badge image
     * @return
     */
    public AbstractTnImage getBadgeImage()
    {
        return badgeImage;
    }

    /**
     * set the badge image
     * @param badgeImage
     */
    public void setBadgeImage(AbstractTnImage badgeImage)
    {
        this.badgeImage = badgeImage;
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }
    
    /**
     * retrieve the badge
     * @return
     */
    public String getBadge()
    {
        return this.badge == null ? null : this.badge.getText();
    }

    /**
     * set the badge
     * @param badge
     */
    public void setBadge(int badgeCount)
    {
        this.setBadge(badgeCount + "");
    }
    
    /**
     * set the badge
     * @param badge
     */
    public void setBadge(String badge)
    {
        if(badge == null || badge.length() == 0 || badge.equals("0"))
        {
            this.badge = TnTextParser.parse("");
            this.setBadgeImage(null);
        }
        else
        {
            this.badge = TnTextParser.parse(badge);
            this.setBadgeImage(ImageDecorator.IMG_RECEIVE_ALARM_CIRCLE_UNFOCUSED.getImage());
            if(badge.length() > 1)
            {
                this.setBadgeFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SMALL_BADGE));
            }
            else
            {
                this.setBadgeFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BADGE));
            }
        }
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }
    
    /**
     * set the color of the badge
     * @param focusedColor
     * @param blurColor
     */
    public void setBadgeColor(int focusedColor, int blurColor)
    {
       badgeFocusedColor = focusedColor;
       badgeBlurColor = blurColor;
       if (nativeUiComponent != null)
       {
           requestLayout();
           requestPaint();
       }
    }
    
    /**
     * set the font of the badge
     * @param badgeFont
     */
    public void setBadgeFont(AbstractTnFont badgeFont)
    {
        this.badgeFont = badgeFont;
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }

    /**
     * retrieve the position of the badge
     * @return
     */
    public int getBadgePosition()
    {
        return badgePosition;
    }

    /**
     * set the position of the badge
     * @param badgePosition
     */
    public void setBadgePosition(int badgePosition)
    {
        this.badgePosition = badgePosition;
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }
    
    public void setBadgeWidth(int width)
    {
        badgeWidth = width;
    }
    
    public void setBadgeHeight(int height)
    {
        badgeHeight = height;
    }
}
