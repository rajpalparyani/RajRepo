/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogLabel.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.ui.frogui.text.FrogTextHelper;

/**
 * 
 * A component for label. <br />
 * <br />
 * You can set the font, the size, the color and the background of the label by corresponding setXX() methods.
 * 
 * 
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-6-30
 */
public class FrogLabel extends AbstractFrogLabel
{

    /**
     * style of the layout of the component
     */
    protected int style = AbstractTnGraphics.LEFT;

    /**
     * construct a FrogLabel object
     * 
     * @param id id of the component
     * @param text text of the component
     */
    public FrogLabel(int id, String text)
    {
        this(id, text, false);

    }

    /**
     * construct a FrogLabel object
     * 
     * @param id id of the component
     * @param text text of the component
     */
    protected FrogLabel(int id, String text, boolean lazyBind)
    {
        super(id, text, lazyBind);

    }

    /**
     * Paint the component to screen
     * 
     * @param graphics (@link AbstractTnGraphics)
     */
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

        int y = (this.getHeight() - topPadding - bottomPadding + 1) / 2;
        y += topPadding;
        int anchor;
        if(anchorFont != null)
        {
            y += anchorFont.getHeightOfCenterAboveBaseline(((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCommonBaseLineAnchor());
            anchor = AbstractTnGraphics.BASE_LINE;
        }
        else
        {
            anchor = AbstractTnGraphics.FONT_VISUAL_VCENTER;
        }
        int scrollWidth = this.getWidth() - leftPadding - rightPadding;
        graphics.pushClip(left, 0, scrollWidth, this.getHeight());
        FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused || !isScrollable, anchor);
        graphics.popClip();
    }

    /**
     * retrieve the style of the component
     * 
     * @return the style of the component
     */
    public int getStyle()
    {
        return style;
    }

    /**
     * set the style of the component
     * 
     * @param style AbstractTnGraphics.LEFT,AbstractTnGraphics.RIGHT,AbstractTnGraphics.HCENTER
     */
    public void setStyle(int style)
    {
        if (this.style != style)
        {
            this.style = style;
            this.requestLayout();
            this.requestPaint();
        }
    }
}
