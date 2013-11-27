/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogComponent.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.frogui.text.FrogTextHelper;
import com.telenav.ui.tnui.text.TnTextLine;
import com.telenav.ui.tnui.text.TnTextParser;

/**
 * An abstract class for other component such as label and button to extend.
 * 
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-7-5
 */
public abstract class AbstractFrogLabel extends AbstractTnComponent
{
    /**
     * color of the text when the component is focused
     */
    protected int focusColor = TnColor.WHITE;

    /**
     * color of the text when the component is not focused
     */
    protected int unfocusColor = TnColor.BLACK;

    protected TnTextLine textLine;

    /**
     * Reference this font to locate the position for paint the target.
     */
    protected AbstractTnFont anchorFont;
    
    /**
     * font of the text
     */
    protected AbstractTnFont font;
    
    /**
     * font of the text
     */
    protected AbstractTnFont boldFont;


    /**
     * if the text needs scrolling
     */
    protected boolean isScrollable;

    /**
     * interval for timer callback which is used for Scroll.
     */
    protected final static int SCROLL_INTERVAL = 80;
    
    protected int textKey;
    protected String textFamily;

    /**
     * Constructs a new component instance with special id and text
     * 
     * @param id the id of the component
     * @param text the text of the component
     */
    public AbstractFrogLabel(int id, String text)
    {
        this(id, text, false);
    }

    /**
     * Constructs a new component instance with special id and text
     * 
     * @param id the id of the component
     * @param text the text of the component
     */
    protected AbstractFrogLabel(int id, String text, boolean lazyBind)
    {
        super(id, lazyBind);
        if (text != null && text.length() > 0)
            this.setText(text);
        initDefaultStyle();
    }

    protected void initDefaultStyle()
    {
        if (font == null)
        {
            this.font = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).createDefaultFont();
        }
        if (boldFont == null)
        {
            boldFont = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).createDefaultBoldFont();
        }

        leftPadding = font.getMaxWidth() / 2;
        rightPadding = leftPadding;
        topPadding = font.getHeight() / 4;
        bottomPadding = topPadding;

    }

    /**
     * layout the component by default
     */
    public void sublayout(int width, int height)
    {
        preferWidth = FrogTextHelper.getWidth(this.textLine, font, boldFont) + leftPadding + rightPadding;
        preferHeight = font.getHeight() + topPadding + bottomPadding;
    }

    /**
     * set the focusColor and blurColor of the text
     * 
     * @param focusColor color of the text when the component is focused
     * @param unfocusColor color of the text when the component is not focused
     */
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
     * retrieve the foreground color of the component
     * @param isFocus true if the component is focused, otherwise false.
     * @return foreground color of the component
     */
    public int getForegroundColor(boolean isFocus)
    {
        return isFocus ? this.focusColor : this.unfocusColor;
    }

    /**
     * retrieve the text of the component
     * 
     * @return text of the component
     */
    public String getText()
    {
        return this.textLine == null ? null : this.textLine.getText();
    }

    /**
     * set the text of the component.
     * 
     * @param text text of the component
     */
    public void setText(String text)
    {
        this.textLine = TnTextParser.parse(text);
        
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }
    
    /**
     * set the text of the component.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     */
    public void setText(int key, String familyName)
    {
        this.textKey = key;
        this.textFamily = familyName;
        setText(this.getText(key, familyName));
    }

    /**
     * retrieve the font of the text
     * 
     * @return font of the text
     */
    public AbstractTnFont getFont()
    {
        return font;
    }
    
    /**
     * set the anchor font of the component
     * 
     * @param anchor font font for reference(@link AbstractTnFont)
     */
    public void setAnchorFont(AbstractTnFont font)
    {
        this.anchorFont = font;
    }

    /**
     * set the font of the component
     * 
     * @param font font of the text(@link AbstractTnFont)
     */
    public void setFont(AbstractTnFont font)
    {
        this.font = font;
        
        if(textLine != null)
        {
            textLine.width = 0;
        }
        
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }

    /**
     * set the bold font of the component
     * 
     * @param boldFont
     */
    public void setBoldFont(AbstractTnFont boldFont)
    {
        this.boldFont = boldFont;
        
        if(textLine != null)
        {
            textLine.width = 0;
        }
        
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }

    /**
     * retrieve the bold font of the component
     * 
     * @return the bold font of the component
     */
    public AbstractTnFont getBoldFont()
    {
        return this.boldFont;
    }
    
    protected void reloadI18nResource()
    {
        super.reloadI18nResource();
        
        if(this.textFamily != null)
        {
            this.setText(this.textKey, this.textFamily);
        }
    }

    protected int getTimerInterval()
    {
        return SCROLL_INTERVAL;
    }

    /**
     * handle the UI event such as scrolling
     */
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        boolean isHandled = false;
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                switch (tnUiEvent.getPrivateEvent().getAction())
                {
                    case TnPrivateEvent.ACTION_TIMER:
                    {
                        handleScrollEvent(FrogTextHelper.getWidth(this.textLine, font, boldFont), isScrollable);
                        isHandled = true;
                        break;
                    }
                }
                break;
            }
        }
        return isHandled;
    }

    /**
     * set the scrolling status of the text
     * 
     * @param isScrollable true if the text needs scrolling, otherwise false
     */
    public void setScrollable(boolean isScrollable)
    {
        if(isScrollable)
        {
            int scrollWidth = getWidth() - getLeftPadding() - getRightPadding();
            int labelWidth = FrogTextHelper.getWidth(this.textLine, font, boldFont);
            if (labelWidth <= scrollWidth)
            {
                isScrollable = false;
            }
        }
        
        if(this.isScrollable != isScrollable)
        {
            this.isScrollable = isScrollable;
            if (nativeUiComponent != null)
            {
                requestLayout();
                requestPaint();
            }
        }
    }

    /**
     * retrieve the scrollable status of the component
     * 
     * @return true if the component is scrollable, otherwise false.
     */
    public boolean isScrollable()
    {
        return isScrollable;
    }
}
