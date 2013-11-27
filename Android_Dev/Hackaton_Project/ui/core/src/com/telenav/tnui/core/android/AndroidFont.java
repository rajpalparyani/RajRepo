/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidFont.java
 *
 */
package com.telenav.tnui.core.android;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.TnRect;

/**
 * Encapsulates a specific set of character font. 
 * <br />
 * 
 * Font sizes always refer to the font's block height (in the case of the shown example, 10), and are always measured in pixels.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
class AndroidFont extends AbstractTnFont
{
    protected Paint paint;
    protected Typeface typeface;
    private char[] measureChar = new char[1];
    private int maxWidth;
	
    public AndroidFont(Context context, int family, int style, int size)
    {
        super(family, style, size);
        
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        
        Typeface defaultFont = Typeface.DEFAULT;
        switch (family)
        {
            case FAMILY_DEFAULT:
            {
                defaultFont = Typeface.DEFAULT;
                break;
            }
            case FAMILY_MONOSPACE:
            {
                defaultFont = Typeface.MONOSPACE;
                break;
            }
            case FAMILY_SANS_SERIF:
            {
                defaultFont = Typeface.SANS_SERIF;
                break;
            }
            case FAMILY_SERIF:
            {
                defaultFont = Typeface.SERIF;
                break;
            }
            case CLEAR_VIEW_ATT:
            {
                try
                {
                    defaultFont = Typeface.createFromAsset(context.getAssets(), "i18n/generic/binaries/ClvATT-Book.otf");
                }
                catch (Exception e)
                {
                    defaultFont = Typeface.DEFAULT;
                }
            }
        }
        
        if ((style & FONT_STYLE_BOLD) != 0 && (style & FONT_STYLE_ITALIC) != 0)
        {
            defaultFont = Typeface.create(defaultFont, Typeface.BOLD_ITALIC);
			//this.paint.setFakeBoldText(true);
        }
        else if ((style & FONT_STYLE_BOLD) != 0)
        {
            if(family == CLEAR_VIEW_ATT)
            {
                try
                {
                    defaultFont = Typeface.createFromAsset(context.getAssets(), "i18n/generic/binaries/ClvATT-Bold.otf");
                }
                catch (Exception e)
                {
                    defaultFont = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                }
            }
            else
            {
                defaultFont = Typeface.create(defaultFont, Typeface.BOLD);
            }
			//this.paint.setFakeBoldText(true);
        }
        else if ((style & FONT_STYLE_ITALIC) != 0)
        {
            defaultFont = Typeface.create(defaultFont, Typeface.ITALIC);
        }
        this.typeface = defaultFont;
        
        this.paint.setTypeface(this.typeface);
        
        if ((style & FONT_STYLE_UNDERLINED) != 0)
        {
            this.setUnderlined(true);
        }
        
        if(size > 0)
        {
            this.setSize(size);
        }
    }
    
    public int charWidth(char ch)
    {
        measureChar[0] = ch;
        return (int) this.paint.measureText(
                measureChar, 0, 1);
    }

    public int charsWidth(char[] ch, int offset, int length)
    {
        return (int) this.paint.measureText(ch, offset, length);
    }

    public Object getFont()
    {
        return paint.getTypeface();
    }

    public int getHeight()
    {
        return paint.getFontMetricsInt(paint.getFontMetricsInt());
    }
    
    //System will return a negative value, so we need to convert it to positive.
    public int getAscent()
    {
        return -paint.getFontMetricsInt().ascent;
    }

    public int getDescent()
    {
        return paint.getFontMetricsInt().descent;
    }

    public int getMaxWidth()
    {
        if(maxWidth > 0)
            return maxWidth;
        maxWidth = charWidth('W');
        return maxWidth;
    }

    public int getSize()
    {
        return (int) this.paint.getTextSize();
    }

    public void setSize(int size)
    {
        this.paint.setTextSize(size);
    }

    public boolean isBold()
    {
        return this.paint.getTypeface().isBold();
    }

    public boolean isItalic()
    {
        return this.paint.getTypeface().isItalic();
    }

    public boolean isPlain()
    {
        return this.paint.getTypeface().getStyle() == Typeface.NORMAL;
    }

    public boolean isUnderlined()
    {
        return this.paint.isUnderlineText();
    }

    public void setUnderlined(boolean isUnderlined)
    {
        this.paint.setUnderlineText(isUnderlined);
    }
    
    public int stringWidth(String str)
    {
        if (str == null || str.length() == 0)
            return 0;

        return (int) this.paint.measureText(str);
    }

    public int substringWidth(String str, int offset, int len)
    {
        return (int) this.paint.measureText(str, offset, offset + len);
    }

    public Object getNativeFont()
    {
        return typeface;
    }
    
    public Object getNativePaint()
    {
        return paint;
    }
    
    public TnRect getTextBounds(String text)
    {
        if(text != null && text.trim().length() > 0)
        {
            Rect bounds = new Rect();
            this.paint.getTextBounds(text, 0, text.length(), bounds);
            return new TnRect(bounds.left, bounds.top, bounds.right, bounds.bottom);
        }
        return new TnRect();
    }

}
