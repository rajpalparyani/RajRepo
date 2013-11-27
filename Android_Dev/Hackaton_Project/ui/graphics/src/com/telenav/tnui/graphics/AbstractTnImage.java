/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnImage.java
 *
 */
package com.telenav.tnui.graphics;

/**
 * Encapsulates an image graphic usable for display on the device. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public abstract class AbstractTnImage
{
    protected int width;
    
    protected int height;
    
    protected boolean isStretchable = true;
    
    protected TnDrawable drawable;
    
    /**
     * Retrieve the native image object.
     * <br />
     * For example: at RIM platform, will be Bitmap.
     * 
     * @return Object
     */
    public abstract Object getNativeImage();
    
    /**
     * Retrieve the width of image.
     * 
     * @return int
     */
    public int getWidth()
    {
        return this.width;
    }
    
    /**
     * Set the width of image, for byte-stream/pixel image, will not affect.
     * <br />
     * will affect the nine-patch image.
     * 
     * @param width the width of image.
     */
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    /**
     * Retrieve the height of image.
     * 
     * @return int
     */
    public int getHeight()
    {
        return this.height;
    }
    
    /**
     * Set the height of image, for byte-stream/pixel image, will not affect.
     * <br />
     * will affect the nine-patch image.
     * 
     * @param height the height of image.
     */
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    /**
     * set if this image is able to stretched or not.
     * 
     * @param isNeedStretch
     */
    public void setIsStretchable(boolean isStretchable)
    {
        this.isStretchable = isStretchable;
    }
    
    /**
     * return if this image is stretchable or not.
     * 
     * @return
     */
    public boolean isStretchable()
    {
        return this.isStretchable;
    }
    
    /**
     * Fills the bitmap's pixels with the specified color.
     * 
     * @param color
     */
    public abstract void clear(int color);
    
    /**
     * Returns true if the bitmap is marked as mutable (i.e. can be drawn into) 
     * 
     * @return boolean
     */
    public abstract boolean isMutable();
    
    /**
     * Retrieve the graphics object.
     * 
     * @return {@link AbstractTnGraphics}
     */
    public abstract AbstractTnGraphics getGraphics();
    
    /**
     * Gets raw ARGB data from region of this Bitmap and stores it in the provided array. Alpha of 0xFF is opaque, 0x00 is transparent. 
     * 
     * @param argbData Array of data where the ARGB data will be stored. Each pixel is stored in 0xAARRGGBB format.
     * @param offset Offset into the data to start writing to.
     * @param scanLength Width of a scanline within the data array.
     * @param x Left edge of rectangle to copy from.
     * @param y Top edge of the rectangle to copy from.
     * @param width Width of the rectangle to copy from.
     * @param height Height of the rectangle to copy from. 
     */
    public abstract void getARGB(int[] argbData, int offset, int scanLength, int x, int y, int width, int height);
    
    /**
     * Free up the memory associated with this image's pixels, and mark the image as "dead", meaning it will throw an
     * exception if draw it again.
     */
    public void release()
    {
        
    }
    
    /**
     * Retrieve if the image is released.
     * 
     * @return boolean
     */
    public boolean isRelease()
    {
        return false;
    }

    /**
     * Set a drawable for this image object. if set this object, the display style will be controlled in drawble, not in
     * image.
     * 
     * @param drawable a drawble for this image.
     */
    public void setDrawable(TnDrawable drawable)
    {
        this.drawable = drawable;
    }
    
    /**
     * Retrieve the drawable for this image object.
     * 
     * @return the drawble for this image.
     */
    public TnDrawable getDrawable()
    {
        return this.drawable;
    }
    
    /**
     * Scales this image.
     * 
     * @param dstWidth The destination Bitmap's width
     * @param dstHeight The destination Bitmap's height
     * 
     * @return the destination image.
     */
    public AbstractTnImage createScaledImage(int dstWidth, int dstHeight)
    {
        return this;
    }
}
