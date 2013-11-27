/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnColor.java
 *
 */
package com.telenav.tnui.graphics;

/**
 * The Color class defines methods for creating and converting color ints. 
 * Colors are represented as packed ints, made up of 4 bytes: alpha, red, green, blue. 
 * The values are unpremultiplied, meaning any transparency is stored solely in the alpha component, 
 * and not in the color components. The components are stored as follows (alpha << 24) | (red << 16) | (green << 8) | blue. 
 * Each component ranges between 0..255 with 0 meaning no contribution for that component, and 255 meaning 100% contribution. 
 * Thus opaque-black would be 0xFF000000 (100% opaque but no contributes from red, gree, blue, and opaque-white would be 0xFFFFFFFF. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class TnColor
{
    /**
     * black color.
     */
    public static final int BLACK = 0xFF000000;

    /**
     * dark gray color.
     */
    public static final int DKGRAY = 0xFF444444;

    /**
     * gray color.
     */
    public static final int GRAY = 0xFF888888;

    /**
     * light gray color.
     */
    public static final int LTGRAY = 0xFFCCCCCC;

    /**
     * white color.
     */
    public static final int WHITE = 0xFFFFFFFF;

    /**
     * red color.
     */
    public static final int RED = 0xFFFF0000;

    /**
     * green color.
     */
    public static final int GREEN = 0xFF00FF00;

    /**
     * blue color.
     */
    public static final int BLUE = 0xFF0000FF;

    /**
     * yellow color.
     */
    public static final int YELLOW = 0xFFFFFF00;

    /**
     * cyan color.
     */
    public static final int CYAN = 0xFF00FFFF;

    /**
     * magenta color.
     */
    public static final int MAGENTA = 0xFFFF00FF;

    /**
     * light blue.
     */
    public static final int LIGHT_BLUE = 0xFF3AA5DC;
    
    /**
     * transparent.
     */
    public static final int TRANSPARENT = 0;

    private int color;
    
    /**
     * create a color.
     * 
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public TnColor(int color)
    {
        this.color = color;
    }
    
    /**
     * Retrieve red value.
     * 
     * @return red value.
     */
    public int getRed()
    {
        return red(this.color);
    }
    
    /**
     * Retrieve green value.
     * 
     * @return green value.
     */
    public int getGreen()
    {
        return green(this.color);
    }
    
    /**
     * Retrieve blue value.
     * 
     * @return blue value.
     */
    public int getBlue()
    {
        return blue(this.color);
    }
    
    /**
     * Retrieve alpha value.
     * 
     * @return alpha value.
     */
    public int getAlpha()
    {
        return alpha(this.color);
    }
    
    /**
     * Retrieve the color.
     * 
     * @return the color.
     */
    public int getColor()
    {
        return this.color;
    }
    
    /**
     * retrieve the alpha component of a color int.
     * 
     * @param color
     * @return
     */
    public static int alpha(int color)
    {
        return color >>> 24;
    }

    /**
     * retrieve the red component of a color int.
     * 
     * @param color
     * @return
     */
    public static int red(int color)
    {
        return (color >> 16) & 0xFF;
    }

    /**
     * retrieve the green component of a color int.
     * 
     * @param color
     * @return
     */
    public static int green(int color)
    {
        return (color >> 8) & 0xFF;
    }

    /**
     * retrieve the blue component of a color int.
     * 
     * @param color
     * @return
     */
    public static int blue(int color)
    {
        return color & 0xFF;
    }

    /**
     * return a color-int from red, green, blue components.
     * 
     * @param red - Red component [0..255] of the color.
     * @param green - Green component [0..255] of the color.
     * @param blue - Blue component [0..255] of the color.
     * @return
     */
    public static int rgb(int red, int green, int blue)
    {
        return (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Return a color-int from alpha, red, green, blue components. These component values should be [0..255], 
     * but there is no range check performed, so if they are out of range, the returned color is undefined.
     * 
     * @param alpha - Alpha component [0..255] of the color
     * @param red - Red component [0..255] of the color.
     * @param green - Green component [0..255] of the color.
     * @param blue - Blue component [0..255] of the color.
     * @return
     */
    public static int argb(int alpha, int red, int green, int blue)
    {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
