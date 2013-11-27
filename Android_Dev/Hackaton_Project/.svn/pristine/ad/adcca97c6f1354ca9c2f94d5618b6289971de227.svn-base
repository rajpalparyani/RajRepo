/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimFont.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;

import com.telenav.logger.Logger;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.TnRect;


/**
 * Encapsulates a specific set of character font. 
 * <br />
 * 
 * Font sizes always refer to the font's block height (in the case of the shown example, 10), and are always measured in pixels.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-8
 */
class RimFont extends AbstractTnFont
{
    protected Font nativeFont;
    private int maxWidth;
    
    public RimFont(int family, int style, int size)
    {
        super(family, style, size);

        nativeFont = Font.getDefault();
        String fontFamilyName = null;
        switch (family)
        {
            case FAMILY_DEFAULT:
            {
                nativeFont = Font.getDefault();
                break;
            }
            case FAMILY_MONOSPACE:
            {
                fontFamilyName = "Andale Mono";
                break;
            }
            case FAMILY_SANS_SERIF:
            {
                fontFamilyName = "BBAlpha Sans";
                break;
            }
            case FAMILY_SERIF:
            {
                fontFamilyName = "BBAlpha Serif";
                break;
            }
        }

        try
        {
            if (fontFamilyName != null)
            {
                FontFamily fontFamily = FontFamily.forName(fontFamilyName);
                nativeFont = fontFamily.getFont(FontFamily.TRUE_TYPE_FONT, size);
            }
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            nativeFont.derive(style, size);
        }
    }

    public int charWidth(char ch)
    {
        return nativeFont.getAdvance(ch);
    }

    public int charsWidth(char[] ch, int offset, int length)
    {
        return nativeFont.getAdvance(ch, offset, length);
    }

    public int getHeight()
    {
        return nativeFont.getHeight();
    }
    
    //For height is the sum of ascent and descent in android, but for rim it also contains leading so we add it into ascent.
    public int getAscent()
    {
        return nativeFont.getAscent() + nativeFont.getLeading();
    }
    
    public int getDescent()
    {
        return nativeFont.getDescent();
    }

    public int getMaxWidth()
    {
        if(maxWidth > 0)
            return maxWidth;
        maxWidth = charWidth('W');
        return maxWidth;
    }

    public Object getNativeFont()
    {
        return nativeFont;
    }

    public Object getNativePaint()
    {
        return nativeFont;
    }

    public int getSize()
    {
        return nativeFont.getHeight();
    }

    public boolean isBold()
    {
        return nativeFont.isBold();
    }

    public boolean isItalic()
    {
        return nativeFont.isItalic();
    }

    public boolean isPlain()
    {
        return nativeFont.isPlain();
    }

    public boolean isUnderlined()
    {
        return nativeFont.isUnderlined();
    }

    public int stringWidth(String str)
    {
        return nativeFont.getAdvance(str);
    }

    public int substringWidth(String str, int offset, int len)
    {
        return nativeFont.getAdvance(str, offset, len);
    }

    public TnRect getTextBounds(String text)
	{
		return null;
	}
}
