/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnGraphicsHelper.java
 *
 */
package com.telenav.tnui.graphics;


/**
 * A helper to create image, font etc.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 9, 2010
 */
public abstract class AbstractTnGraphicsHelper
{
    /**
     * the instance of this class.
     */
    static AbstractTnGraphicsHelper instance;
    
    protected AbstractTnFont defaultFont;
    
    protected AbstractTnFont defaultBoldFont;
    
    /**
     * Retrieve the instance of helper.
     * 
     * @return {@link AbstractTnGraphicsHelper}
     */
    public static AbstractTnGraphicsHelper getInstance()
    {
        return instance;
    }
    
    public static void init(AbstractTnGraphicsHelper helper)
    {
        instance = helper;
    }
    
    /**
     * init this class with platform relative helper.
     * 
     * @param context context maybe is Context at Android platform, Midlet at J2ME platform etc.
     * @param instance platform relative helper.
     */
    public abstract void init(Object context);
    
    /**
     * Create a font object given a family name, and option style information. If null is passed for the name, then the "default" font will be chosen. 
     * The resulting font object can be queried (getStyle()) to discover what its "real" style characteristics are.
     * 
     * @param family - {@link AbstractTnFont} FAMILY_*.
     * @param style - {@link AbstractTnFont} STYLE_*.
     * @param size - the size of font.
     * @return {@link AbstractTnFont}
     */
    public abstract AbstractTnFont createFont(int family, int style, int size);
    
    /**
     * Create a default font object given a family name, and option style information. If null is passed for the name, then the "default" font will be chosen. 
     * The resulting font object can be queried (getStyle()) to discover what its "real" style characteristics are.
     * 
     */
    public AbstractTnFont createDefaultFont()
    {
        if (this.defaultFont == null)
        {
            this.defaultFont = createFont(AbstractTnFont.FAMILY_DEFAULT, AbstractTnFont.FONT_STYLE_PLAIN, 16);
        }
        return this.defaultFont;
    }
    
    /**
     * Create a default font object given a family name, and option style information. If null is passed for the name, then the "default" font will be chosen. 
     * The resulting font object can be queried (getStyle()) to discover what its "real" style characteristics are.
     * 
     */
    public AbstractTnFont createDefaultBoldFont()
    {
        if (this.defaultBoldFont == null)
        {
            this.defaultBoldFont = createFont(createDefaultFont().getFamily(), AbstractTnFont.FONT_STYLE_BOLD, createDefaultFont().getSize());
        }
        return this.defaultBoldFont;
    }
    
    /**
     * Returns a mutable image with the specified native image.
     * 
     * @param nativeImage a native image object
     * @return {@link AbstractTnImage}
     */
    public abstract AbstractTnImage createImage(Object nativeImage);
    
    /**
     * Returns a mutable image with the specified width and height.
     * 
     * @param width - width of image.
     * @param height - height of image.
     * @return {@link AbstractTnImage}
     */
    public abstract AbstractTnImage createImage(int width, int height);
    
    /**
     * Decode an immutable image from the specified byte array.
     * 
     * @param data - byte array of compressed image data
     * @return {@link AbstractTnImage}
     */
    public abstract AbstractTnImage createImage(byte[] data);
    
    /**
     * Returns an immutable image with the specified width and height, 
     * with each pixel value set to the corresponding value in the colors array.
     * 
     * @param pixels - Array of Color used to initialize the pixels. This array must be at least as large as width * height.
     * @param width - The width of the image.
     * @param height - The height of the image.
     * @return {@link AbstractTnImage}
     */
    public abstract AbstractTnImage createImage(int[] pixels, int width, int height);
    
    /**
     * Returns an immutable image from the specified subset of the source image. The new image may be the same object
     * as source, or a copy may have been made. It is initialized with the same density as the original image.
     * 
     * @param image The image we are subsetting
     * @param x The x coordinate of the first pixel in source
     * @param y The y coordinate of the first pixel in source
     * @param width The number of pixels in each row
     * @param height The number of rows 
     * @return an immutable image
     */
    public abstract AbstractTnImage createImage(AbstractTnImage image, int x, int y, int width, int height);
}
