/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seFont.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.TnRect;

/**
 * Encapsulates a specific set of character font. 
 * <br />
 * 
 * Font sizes always refer to the font's block height (in the case of the shown example, 10), and are always measured in pixels.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 3, 2010
 */
class J2seFont extends AbstractTnFont
{
    private Font font;
    private FontMetrics fontMetrics;
    
    public J2seFont(Graphics graphics, int family, int style, int size)
    {
        super(family, style, size);
        
        String nativeFamily = Font.SANS_SERIF;
        switch (family)
        {
            case FAMILY_DEFAULT:
            {
                nativeFamily = Font.SANS_SERIF;
                break;
            }
            case FAMILY_MONOSPACE:
            {
                nativeFamily = Font.MONOSPACED;
                break;
            }
            case FAMILY_SANS_SERIF:
            {
                nativeFamily = Font.SANS_SERIF;
                break;
            }
            case FAMILY_SERIF:
            {
                nativeFamily = Font.SERIF;
                break;
            }
        }
        
        if ((style & FONT_STYLE_BOLD) != 0 && (style & FONT_STYLE_ITALIC) != 0)
        {
            this.font = new Font(nativeFamily, Font.BOLD | Font.ITALIC, size);
        }
        else if ((style & FONT_STYLE_BOLD) != 0)
        {
            this.font = new Font(nativeFamily, Font.BOLD, size);
        }
        else if ((style & FONT_STYLE_ITALIC) != 0)
        {
            this.font = new Font(nativeFamily, Font.ITALIC, size);
        }
        else
        {
            this.font = new Font(nativeFamily, Font.PLAIN, size);
        }
        
        this.fontMetrics = graphics.getFontMetrics(this.font);
    }

    public int charWidth(char ch)
    {
        return this.fontMetrics.charWidth(ch);
    }

    public int charsWidth(char[] ch, int offset, int length)
    {
        return this.fontMetrics.charsWidth(ch, offset, length);
    }

    public int getHeight()
    {
        return this.fontMetrics.getHeight();
    }

    //For height is the sum of ascent and descent in android, but for j2se it also contains leading so we add it into ascent.
    public int getAscent()
    {
        return this.fontMetrics.getAscent() + this.fontMetrics.getLeading();
    }
    
    public int getDescent()
    {
        return this.fontMetrics.getDescent();
    }

    public int getMaxWidth()
    {
        return this.fontMetrics.getMaxAdvance();
    }

    public Object getNativeFont()
    {
        return this.font;
    }

    public Object getNativePaint()
    {
        return this.fontMetrics;
    }

    public int getSize()
    {
        return this.font.getSize();
    }

    public boolean isBold()
    {
        return this.font.isBold();
    }

    public boolean isItalic()
    {
        return this.font.isItalic();
    }

    public boolean isPlain()
    {
        return this.font.isPlain();
    }

    public boolean isUnderlined()
    {
        if ((style & FONT_STYLE_UNDERLINED) != 0)
        {
            return true;
        }
        
        return false;
    }

    public int stringWidth(String str)
    {
        return this.fontMetrics.stringWidth(str);
    }

    public int substringWidth(String str, int offset, int len)
    {
        return this.fontMetrics.stringWidth(str.substring(offset, offset + len));
    }
	
    public TnRect getTextBounds(String text)
	{
		return null;
	}
}
