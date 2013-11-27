/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenAbstractSummaryItem.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.UiStyleManager;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-12-2
 */
public abstract class CitizenSummaryItem extends AbstractTnComponent
{
    /**
     * color of the text when the component is focused
     */
    protected int focusColor = TnColor.BLACK;

    /**
     * color of the text when the component is not focused
     */
    protected int unfocusColor = TnColor.BLACK;
    
    protected boolean isCarShown;
    protected CitizenSummaryItem(int id)
    {
        super(id);
    }
    
    public void setIsShowCar(boolean isShowCar)
    {
        this.isCarShown = isShowCar;
    }
    
    public boolean isCarShown()
    {
        return this.isCarShown;
    }
    
    protected void drawLine(AbstractTnGraphics g, int x, int y, int x1, int y1)
    {
        int oldColor = g.getColor();
        
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        g.drawLine(x, y, x1, y1);
        
        g.setColor(oldColor);
    }
    
    public String getLabelForPaint(String label, int labelW, AbstractTnFont font, boolean isScrolling)
    {
        String tempLabel = label;
        if (!isScrolling)
        {
            if (font.stringWidth(tempLabel) > labelW)
            {
                int len = font.stringWidth("...");
                int i;
                for (i=0; i<label.length(); i++)
                {
                    len += font.charWidth(label.charAt(i));
                    if (len >= labelW)
                    {
                        break;
                    }
                }
                tempLabel = label.substring(0, i);
                tempLabel += "...";
            }
        }
        
        return tempLabel;
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
}
