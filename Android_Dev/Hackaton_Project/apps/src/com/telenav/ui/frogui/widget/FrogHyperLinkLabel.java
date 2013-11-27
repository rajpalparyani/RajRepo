/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * FrogHyperLinkLabel.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.ui.frogui.text.FrogTextHelper;

/**
 *@author yhzhou
 *@date 2012-3-27
 */
public class FrogHyperLinkLabel extends FrogLabel
{

    public FrogHyperLinkLabel(int id, String text)
    {
        super(id, text);
        // TODO Auto-generated constructor stub
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        if (this.isFocused())
        {
            graphics.setColor(focusColor);
        }
        else
        {
            graphics.setColor(unfocusColor);
        }
        graphics.setFont(font);
        int left;
        if ((style & AbstractTnGraphics.LEFT) != 0)
        {
            left = leftPadding;
        }
        else if ((style & AbstractTnGraphics.RIGHT) != 0)
        {
            left = this.getWidth() - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont) - rightPadding;
        }
        else
        {
            left = getWidth() / 2 - FrogTextHelper.getWidth(this.textLine, this.font, this.boldFont) / 2;
        }
        if (left < leftPadding)
        {
            left = leftPadding;
        }
        int y = (this.getHeight() - font.getHeight() - topPadding - bottomPadding) / 2;
        y += topPadding;
        int scrollWidth = this.getWidth() - leftPadding - rightPadding;
        graphics.pushClip(left, y, scrollWidth, this.getHeight());
        FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused || !isScrollable);
//        graphics.drawLine(left - this.scrollX, y + font.getHeight() + 1, left - this.scrollX + scrollWidth, y + font.getHeight() + 2);
        graphics.drawLine(left - this.scrollX, y + font.getHeight() - 3, left - this.scrollX + scrollWidth, y + font.getHeight() - 3);
        graphics.popClip();
    }
}
