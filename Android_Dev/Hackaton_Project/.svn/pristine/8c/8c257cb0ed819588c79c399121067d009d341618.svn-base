/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnFont.java
 *
 */
package com.telenav.tnui.graphics;

/**
 * Encapsulates a specific set of character font. 
 * <br />
 * 
 * Font sizes always refer to the font's block height (in the case of the shown example, 10), and are always measured in pixels.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public abstract class AbstractTnFont
{
    /**
     * Default family at the platform.
     */
    public static final int FAMILY_DEFAULT = 0;

    /**
     * serif family.
     */
    public static final int FAMILY_SERIF = 1;

    /**
     * sans_serif family.
     */
    public static final int FAMILY_SANS_SERIF = 2;

    /**
     * monospace family.
     */
    public static final int FAMILY_MONOSPACE = 3;

    /**
     * Frog rotation family, and this family will support font's rotation.
     */
    public static final int FAMILY_FROG_ROTATION = 4;
    
    /**
     * ClearviewATT family, this is exclusive for AT&T brand. 
     */
    public static final int CLEAR_VIEW_ATT = 5;

    /**
     * bold.
     */
    public static final int FONT_STYLE_BOLD = 1;

    /**
     * italic.
     */
    public static final int FONT_STYLE_ITALIC = 2;

    /**
     * underlined.
     */
    public static final int FONT_STYLE_UNDERLINED = 4;

    /**
     * plain.
     */
    public static final int FONT_STYLE_PLAIN = 8;
    
    protected int family;
    protected int style;
    
    /**
     * construct a font with special family, style and size.
     * 
     * @param family
     * @param style
     * @param size
     */
    public AbstractTnFont(int family, int style, int size)
    {
        this.family = family;
        this.style = style;
    }

    /**
     * retrieve the font's family. Please see FAMILY_*.
     * 
     * @return int
     */
    public int getFamily()
    {
        return this.family;
    }
    
    /**
     * retrieve the font's style. Please see FONT_STYLE_*.
     * 
     * @return int
     */
    public int getStyle()
    {
        return this.style;
    }
    
    /**
     * retrieve the font's size.
     * 
     * @return int
     */
    public abstract int getSize();
    
    /**
     * Determines if this font is plain.
     * 
     * @return boolean
     */
    public abstract boolean isPlain();
    
    /**
     * Determines if this font is bold.
     * 
     * @return boolean
     */
    public abstract boolean isBold();
    
    /**
     * Determines if this font is italic.
     * 
     * @return boolean
     */
    public abstract boolean isItalic();
    
    /**
     * Determines if this font is underlined.
     * 
     * @return boolean
     */
    public abstract boolean isUnderlined();
    
    /**
     * Retrieves this font's height, it should equals ascent plus descent here.
     * 
     * @return int
     */
    public abstract int getHeight();
    
    /**
     * Retrieves this font's height above the baseline of the font, it is a positive value.
     * 
     * @return int
     */
    public abstract int getAscent();
    
    /**
     * Retrieves this font's height below the baseline of the font, it is a positive value. 
     * 
     * @return int
     */
    public abstract int getDescent();
    
    /**
     * Retrieves this font's max width.
     * 
     * @return int
     */
    public abstract int getMaxWidth();
    
    /**
     *  Determines particular character's width.
     *  
     * @param ch
     * @return int
     */
    public abstract int charWidth(char ch);
    
    /**
     * Determines advance of a character array's substring.
     * 
     * @param ch
     * @param offset
     * @param length
     * @return int
     */
    public abstract int charsWidth(char[] ch, int offset, int length);
    
    /**
     * Determines advance of a text string.
     * 
     * @param str
     * @return int
     */
    public abstract int stringWidth(String str);
    
    /**
     * Determines advance of a text string's substring.
     * 
     * @param str
     * @param offset
     * @param len
     * @return int
     */
    public abstract int substringWidth(String str, int offset, int len);
    
    /**
     * Retrieves the font object at the native platform.
     * <br />
     * For example: Typeface at Android platform, Font at RIM platform.
     * 
     * @return Object
     */
    public abstract Object getNativeFont();
    
    /**
     * Retrieves the paint object at the native platform.
     * <br />
     * For example: Paint at Android platform, NULL(no-need) at RIM platform.
     * 
     * @return
     */
    public abstract Object getNativePaint();
    
    public int getHeightOfCenterAboveBaseline(String text)
    {
        TnRect bounds = getTextBounds(text);
        return - bounds.top - (bounds.height() + 1) / 2;
    }
    
    /**
     * Retrieves the actual paint rect of the text.
     * The rect keep the base line as coordinate axis, positive value above it and negative value below it.
     * <br />
     * 
     * @return
     */
    public abstract TnRect getTextBounds(String text);
    
}