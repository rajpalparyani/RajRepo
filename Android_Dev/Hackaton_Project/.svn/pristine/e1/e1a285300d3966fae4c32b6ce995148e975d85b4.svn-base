/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenButton.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogButton;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-17
 */
public class CitizenButton extends FrogButton
{

    /**
     * right icon of the component when it is focused
     */
    protected AbstractTnImage rightButtonIconFocus;

    /**
     * right icon of the component when it is not focused
     */
    protected AbstractTnImage rightButtonIconBlur;

    /**
     * constructs a citizen button
     * @param id
     * @param text
     * @param buttonIconFocus
     * @param buttonIconBlur
     */
    public CitizenButton(int id, String text, AbstractTnImage buttonIconFocus, AbstractTnImage buttonIconBlur)
    {
        super(id, text);
        this.iconFocus = buttonIconFocus;
        this.iconUnfocus = buttonIconBlur;
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        int y = 0;
        AbstractTnImage buttonIcon;
        AbstractTnImage rightButtonIcon;
        if (isFocused())
        {
            graphics.setColor(focusColor);
            buttonIcon = iconFocus;
            rightButtonIcon = rightButtonIconFocus;
        }
        else
        {
            graphics.setColor(unfocusColor);
            buttonIcon = iconUnfocus;
            rightButtonIcon = rightButtonIconBlur;
        }
        graphics.setFont(font);
        int left;

        if((style & AbstractTnGraphics.LEFT) != 0)
        {
            int buttonIconWidth = 0;
            if (buttonIcon != null)
            {
                buttonIconWidth = buttonIcon.getWidth();
                left = leftPadding;
                y = (this.getHeight() - buttonIcon.getHeight()) / 2;
                graphics.drawImage(buttonIcon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
            left = leftPadding + buttonIconWidth + gap;
            if (rightButtonIcon != null)
            {
                y = (this.getHeight() - font.getHeight()) / 2;
                int scrollWidth = this.getWidth() - buttonIconWidth - leftPadding - rightPadding - gap
                        - rightButtonIcon.getWidth();
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                        || !isScrollable);
                graphics.popClip();
                y = (this.getHeight() - rightButtonIcon.getHeight()) / 2;
                graphics.drawImage(rightButtonIcon, getWidth() - rightPadding - rightButtonIconFocus.getWidth(), y,
                    AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
            }
            else
            {
                y = (this.getHeight() - font.getHeight()) / 2;
                int scrollWidth = this.getWidth() - buttonIconWidth - leftPadding - rightPadding - gap;
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                        || !isScrollable);
                graphics.popClip();
            }
        }
        else
        {
            super.paint(graphics);
        }
    }

    /**
     * layout the component by default
     */
    public void sublayout(int width, int height)
    {
        if (iconFocus == null || iconUnfocus == null)
        {
            super.sublayout(width, height);
        }
        else
        {
            if (rightButtonIconFocus == null && rightButtonIconBlur == null)
            {
                int textWidth = FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont);
                preferWidth = textWidth + leftPadding + rightPadding + gap + iconFocus.getWidth();
                int iconHeight = iconFocus.getHeight();
                if (iconHeight > font.getHeight())
                {
                    preferHeight = iconHeight + topPadding + bottomPadding;
                }
                else
                {
                    preferHeight = font.getHeight() + topPadding + bottomPadding;
                }
            }
            else
            {
                int textWidth = FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont);
                int iconHeight = iconFocus.getHeight();
                int rightIconHeight = 0;
                if ((style & AbstractTnGraphics.LEFT) != 0)
                {
                    AbstractTnImage image = rightButtonIconFocus != null ? rightButtonIconFocus : rightButtonIconBlur;

                    preferWidth = iconFocus.getWidth() + textWidth + leftPadding + rightPadding + gap + image.getWidth();
                    rightIconHeight = image.getHeight();
                    if (rightIconHeight > iconHeight)
                    {
                        iconHeight = rightIconHeight;
                    }

                    if (iconHeight > font.getHeight())
                    {
                        preferHeight = iconHeight + topPadding + bottomPadding;
                    }
                    else
                    {
                        preferHeight = font.getHeight() + topPadding + bottomPadding;
                    }
                }
            }
        }

    }

    public void setStyle(int style)
    {
        throw new IllegalArgumentException("This component does not support style-seting.");
    }

    /**
     * set the right icon of the component
     * @param buttonIconFocus
     * @param buttonIconBlur
     */
    public void setRightIcon(AbstractTnImage buttonIconFocus, AbstractTnImage buttonIconBlur)
    {
        this.rightButtonIconFocus = buttonIconFocus;
        this.rightButtonIconBlur = buttonIconBlur;
        requestLayout();
        requestPaint();
    }
}
