/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenCategoryListItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.frogui.widget.FrogListItem;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-23
 */
public class CitizenCategoryListItem extends FrogListItem
{
    protected AbstractTnImage rightIndicatorImgUnFocus;
    protected AbstractTnImage rightIndicatorImgFocus;
    protected boolean isShowRightIndicator;
    public CitizenCategoryListItem(int id)
    {
        super(id);
    }

    public void setRightIndicator(AbstractTnImage rightImageFocus, AbstractTnImage rightImageUnFocus)
    {
        rightIndicatorImgFocus = rightImageFocus;
        rightIndicatorImgUnFocus = rightImageUnFocus;
    }
    
    public void setIsShowRightIndicator(boolean isShowRightIndicator)
    {
        this.isShowRightIndicator = isShowRightIndicator;
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
        int imgWidth = 0;
        AbstractTnImage rightImage = this.rightIndicatorImgUnFocus;
        if(this.isFocused)
        {
            rightImage = this.rightIndicatorImgFocus;
        }
        
        if (rightImage != null && isShowRightIndicator)
        {
            imgWidth = rightImage.getWidth();
            int imageHeight = rightImage.getHeight();
            int imageX = this.getWidth() - rightPadding - imgWidth;
            int imageY = (this.getHeight() - imageHeight) >> 1;
            graphics.drawImage(rightImage, imageX, imageY, AbstractTnGraphics.LEFT
                    | AbstractTnGraphics.TOP);
        }
        int scrollWidth = this.getWidth() - leftPadding - rightPadding - imgWidth;
        graphics.pushClip(left, y, scrollWidth, this.getHeight());
        FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused || !isScrollable);
        
        graphics.popClip();
    }
}
