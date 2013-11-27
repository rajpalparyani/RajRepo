package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;


/**
 * Display an image as a UI-Component.
 * <br /><br />
 * 
 * @author chgong (chgong@telenav.cn)
 * @date 2010-7-6
 */

public class FrogImageComponent extends AbstractTnComponent
{   
    protected AbstractTnImage image;
    protected boolean isStretchable;
    protected boolean needBorder;
    protected int anchor;
    /**
     * Constructs a new image component with id and AbstractTnimage.
     * 
     * @param id
     * @param image {@link AbstractTnImage}
     * 
     */
    public FrogImageComponent(int id, AbstractTnImage image)
    {
        super(id);
        this.image = image;
        initDefaultStyle();
    }
    
    public FrogImageComponent(int id, AbstractTnImage image, int anchor)
    {
        super(id);
        this.image = image;
        this.anchor = anchor;
    }
    
    protected void initDefaultStyle()
    {
        anchor = AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP;
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        int preferredWidth = this.getPreferredWidth();
        int preferredHeight = this.getPreferredHeight();
        int clipWidth = preferredWidth - leftPadding - rightPadding;
        int clipHeight = preferredHeight - topPadding - bottomPadding;
        if(clipWidth <= 0 || clipHeight <= 0)
        {
            return;
        }
        graphics.translate(this.leftPadding, this.topPadding);
        graphics.pushClip(0, 0, clipWidth, clipHeight);
        if(this.isStretchable)
        {
            TnRect destRect = new TnRect();
            destRect.left = 0;
            destRect.top = 0;
            destRect.right = clipWidth;
            destRect.bottom = clipHeight;
            graphics.drawImage(image, null, destRect);
        }
        else
        {
            int x = 0;
            int y = 0;
            if ((anchor & AbstractTnGraphics.HCENTER) != 0)
            {
                x = clipWidth >> 1;
            }
            if ((anchor & AbstractTnGraphics.VCENTER) != 0)
            {
                y = clipHeight >> 1;
            }
            if ((anchor & AbstractTnGraphics.RIGHT)  != 0)
            {
                x = clipWidth;
            }
            if ((anchor & AbstractTnGraphics.BOTTOM) != 0)
            {
                y = clipHeight;
            }
            graphics.drawImage(image, x, y, anchor);
        }
        if(this.needBorder)
        {
            int x = leftPadding;
            int y = topPadding;
            graphics.setColor(this.getBackgroundColor());
            int width = this.getWidth();
            int height = this.getHeight();
            graphics.drawLine(x, y, x, y + height);
            graphics.drawLine(x, y, x + width, y);
            graphics.drawLine(x, y + height -1, x + width -1, y + height);
            graphics.drawLine(x + width -1, y, x + width -1, y + height);
        }
        graphics.popClip();
        graphics.translate(-this.leftPadding, -this.topPadding);
    }
    
    /**
     * Get the AbstractTnImage
     * 
     * @return {@link AbstractTnImage}
     */
    public AbstractTnImage getImage()
    {
        return image;
    }

    /**
     * Replace the AbstractTnImage and re-paint it
     * 
     * @param image {@link AbstractTnImage}
     */
    public void setImage(AbstractTnImage image)
    {
        this.image = image;
        this.requestPaint();
    }
    
    /**
     * Set image if can be stretchable.
     * @param isStretchable
     */
    public void setStretchable(boolean isStretchable)
    {
        if(this.isStretchable != isStretchable)
        {
            this.isStretchable = isStretchable;
            this.requestPaint();
        }
    }
    
    public void setNeedBorder(boolean needBorder)
    {
        this.needBorder = needBorder;
    }
    
    
    /**
     * Retrieve if the image can be stretched.
     * 
     * @return true or false
     */
    public boolean isStretchable()
    {
        return this.isStretchable;
    }

    /**
     * Set anchor of image.
     * 
     * @param anchor {@link AbstractTnGraphics.LEFT, AbstractTnGraphics.RIGHT, AbstractTnGraphics.TOP,
     *            AbstractTnGraphics.BOTTOM, AbstractTnGraphics.VCENTER, AbstractTnGraphics.HCENTER}
     */
    public void setAnchor(int anchor)
    {
        if(this.anchor != anchor)
        {
            this.anchor = anchor;
            this.requestPaint();
        }
    }
    
    /**
     * Retrieve anchor of image.
     * 
     * @return anchor
     */
    public int getAnchor()
    {
        return this.anchor;
    }
    

    /**
     * Implements custom layout features for this component.
     */
    public void sublayout(int width, int height)
    {
        if (image != null)
        {
            if (preferHeight <= 0)
            {
                preferHeight = image.getHeight() + topPadding + bottomPadding;
            }
            
            if (preferWidth <= 0)
            {
                preferWidth = image.getWidth() + leftPadding + rightPadding;
            }
        }
    }
    
}
