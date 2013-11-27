/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * FeedbackAndUpgradeComponent.java
 *
 */
package com.telenav.module.dashboard;

import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.citizen.CitizenButton;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 * @author yhzhou
 * @date 2012-2-16
 */
public class ScoutMultiLineButton extends CitizenButton
{
    private TnTextLine content;

    private AbstractTnFont contentFont;

    private int contentUnfocusColor;

    private int contentFocusColor;
    
    private TnTextLine time;
    
    private AbstractTnFont timeFont;

    private int timeUnfocusColor;
    
    private int timeFocusColor;
    
    public ScoutMultiLineButton(int id, String title, AbstractTnImage buttonIconFocus,
            AbstractTnImage buttonIconBlur, String content, String time)
    {
        super(id, title, buttonIconFocus, buttonIconBlur);
        this.content = TnTextParser.parse(content);
        if(time != null && time.length() != 0)
        {
            this.time = TnTextParser.parse(time);
        }
    }

    public ScoutMultiLineButton(int id, String text, AbstractTnImage buttonIconFocus,
            AbstractTnImage buttonIconBlur)
    {
        super(id, text, buttonIconFocus, buttonIconBlur);
    }

    public void setContentFont(AbstractTnFont contentFont)
    {
        this.contentFont = contentFont;
    }

    public void setContentForegroundColor(int focusColor, int unfocusColor)
    {
        this.contentUnfocusColor = unfocusColor;
        this.contentFocusColor = focusColor;
    }
    
    public void setTimeText(String time)
    {
        if(time != null && time.length() != 0)
        {
            this.time = TnTextParser.parse(time);
        }
    }
    
    public void setTimeFont(AbstractTnFont timeFont)
    {
        this.timeFont = timeFont;
    }
    
    public void setTimeForegroundColor(int focusColor, int unfocusColor)
    {
        this.timeUnfocusColor = unfocusColor;
        this.timeFocusColor = focusColor;
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        AbstractTnImage buttonIcon = isFocused() ? iconFocus : iconUnfocus;
        int x = this.getWidth() - rightPadding;
        int y = 0;
        int verticalGap = 2 * gap;
        int lineGap = (this.getHeight() - font.getHeight() - contentFont.getHeight() - verticalGap) / 2;
        if(buttonIcon.getWidth() != 0 && buttonIcon.getHeight() != 0)
        {
            x -=  (buttonIcon.getWidth() + gap); 
            y = (this.getHeight() - buttonIcon.getHeight()) / 2;
            if (y < topPadding)
                y = topPadding;
            graphics.drawImage(buttonIcon, x, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
        
        if(time != null)
        {
            int timeLineGap = (this.getHeight() - timeFont.getHeight()) / 2;
            int timeWidth = FrogTextHelper.getWidth(this.time, this.timeFont, this.timeFont);
            x -=  (gap + timeWidth); 
            graphics.setFont(timeFont);
            graphics.setColor(isFocused() ? timeFocusColor : timeUnfocusColor);
            y = timeLineGap;
            FrogTextHelper.paint(graphics, x - this.scrollX, y, time, timeFont, timeFont, timeWidth, !isFocused || !isScrollable);
        }
        
        int width = x - leftPadding;
        
        x = leftPadding;
        y = lineGap;
        graphics.pushClip(x, 0, width, this.getHeight());
        
        graphics.setFont(font);
        graphics.setColor(isFocused() ? focusColor : unfocusColor);
        FrogTextHelper.paint(graphics, x - this.scrollX, y, textLine, font, boldFont, width, !isFocused || !isScrollable);
        
        graphics.setFont(contentFont);
        graphics.setColor(isFocused() ? contentFocusColor : contentUnfocusColor);
        y += (font.getHeight() + verticalGap);
        FrogTextHelper.paint(graphics, x - this.scrollX, y, content, contentFont, contentFont, width, !isFocused || !isScrollable);
        
        graphics.popClip();
    }
}
