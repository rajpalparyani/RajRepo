/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CitizenRadioButton.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2011-2-15
 */
public class CitizenRadioButton extends FrogButton
{

    private boolean isHighLight;

    public CitizenRadioButton(int id, String text)
    {
        super(id, text);
    }
    
    public void setIsHighLight(boolean isHighLight)
    {
        this.isHighLight = isHighLight;
        this.requestPaint();
    }
    
    public boolean isHighLight()
    {
        return this.isHighLight;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        int y = 0;
        AbstractTnImage buttonIcon;
        if (isFocused() || isHighLight)
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
                        || !isScrollable || !isHighLight);
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
                        || !isScrollable || !isHighLight);
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
                    y = topPadding;
                    graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    left = (this.getWidth() - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont)) / 2;
                    if (left < leftPadding)
                    {
                        left = leftPadding;
                    }
                    y = buttonIcon.getHeight() + topPadding + gap;
                    int scrollWidth = this.getWidth() - leftPadding - rightPadding;
                    graphics.pushClip(left, y, scrollWidth, this.getHeight());
                    FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                            || !isScrollable || !isHighLight);
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
        }
        else
        {
            y = (this.getHeight() - font.getHeight()) / 2;
            if (y < topPadding)
                y = topPadding;
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
    
    protected void paintBackground(AbstractTnGraphics graphics)
    {
        int oldColor = graphics.getColor();

        if (this.backgroundDrawable != null)
        {
            if (this.backgroundDrawable.getBounds() == null)
            {
                this.backgroundDrawable.setBounds(new TnRect(0, 0, this.getWidth(), this.getHeight()));
            }
            this.backgroundDrawable.draw(graphics);
        }
        else if (this.tnUiArgs != null)
        {
            TnUiArgAdapter uiArgAdapter = null;
            if (isFocused() || isHighLight)
                uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
            else
                uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
            if (uiArgAdapter != null)
            {
                AbstractTnImage bgImage = uiArgAdapter.getImage();
                if(bgImage != null)
                {
                    bgImage.setWidth(getWidth());
                    bgImage.setHeight(getHeight());
                    graphics.drawImage(bgImage, 0, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                }
            }
            else if (this.backgroundColor != TnColor.TRANSPARENT)
            {
                graphics.setColor(this.backgroundColor);
                graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                graphics.setColor(oldColor);
            }
        }
        else if (this.backgroundColor != TnColor.TRANSPARENT)
        {
            graphics.setColor(this.backgroundColor);
            graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            graphics.setColor(oldColor);
        }
    }

}
