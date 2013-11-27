/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnNinePatchImage.java
 *
 */
package com.telenav.tnui.graphics;

import java.util.Vector;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnDrawable;
import com.telenav.tnui.graphics.TnRect;

/**
 * 
 * The TnNinePatchImage class permits drawing an image in nine sections with some radiant. <br />
 * <br />
 * The four corners are unscaled; the four edges are scaled in one axis, and the middle is scaled in both axes.
 * Normally, the middle is transparent so that the patch can provide a selection about a rectangle. Essentially, it
 * allows the creation of custom graphics that will scale the way that you define, when content added within the image
 * exceeds the normal bounds of the graphic.
 * <br />
 * <br />
 * Below is the template:<br /><br />
 * LEFT_TOP***********TOP*********RIGHT_TOP<br />
 * *****************************************<br />
 * *****************************************<br />
 * LEFT*************CENTER*************RIGHT<br />
 * *****************************************<br />
 * *****************************************<br />
 * LEFT_BOTTOM*****BOTTOM******RIGHT_BTTOM<br />
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-13
 */
public class TnNinePatchImage extends AbstractTnImage
{
    /**
     * Draw nine patch with carvel mode.
     */
    public final static int DRAW_CARVEL = 1;
    
    /**
     * Draw nine patch with scale mode.
     */
    public final static int DRAW_SCALE = 2;
    
    /**
     * Draw the round corners of the image. Such as (DRAW_SCALE | DRAW_ROUNDCORNER)
     */
    public final static int DRAW_ROUNDCORNER = 4;
    
    /**
     * left position.
     */
    public final static int LEFT = 0;

    /**
     * top position.
     */
    public final static int TOP = 1;

    /**
     * right position.
     */
    public final static int RIGHT = 2;
    
    /**
     * bottom position.
     */
    public final static int BOTTOM = 3;
    
    /**
     * center position.
     */
    public final static int CENTER = 4;
    
    /**
     * left top position.
     */
    public final static int LEFT_TOP = 5;

    /**
     * right top position.
     */
    public final static int RIGHT_TOP = 6;

    /**
     * right bottom position.
     */
    public final static int RIGHT_BOTTOM = 7;
    
    /**
     * left bottom position.
     */
    public final static int LEFT_BOTTOM = 8;
    
    
    /**
     * nine patch images.
     */
    private AbstractTnImage[] ninePatchImages;

    /**
     * radiant images.
     */
    private AbstractTnImage[] radiantImages;
    
    private int drawMode = DRAW_CARVEL;
	
	StretchImageSpliter spliter = null;

    Vector splitedRects = new Vector();

    /**
     * construct a nine patch image object with 9 images.
     * 
     * @param left the image at left
     * @param top the image at top
     * @param right the image at right
     * @param bottom the image at bottom
     * @param center the image at center
     * @param leftTop the image at left top
     * @param rightTop the image at right top
     * @param rightBottom the image at right bottom
     * @param leftBottom the image at left bottom
     */
    public TnNinePatchImage(AbstractTnImage left, AbstractTnImage top, AbstractTnImage right, AbstractTnImage bottom,
            AbstractTnImage center, AbstractTnImage leftTop, AbstractTnImage rightTop, AbstractTnImage rightBottom,
            AbstractTnImage leftBottom)
    {
        ninePatchImages = new AbstractTnImage[9];
        ninePatchImages[LEFT] = left;
        ninePatchImages[TOP] = top;
        ninePatchImages[RIGHT] = right;
        ninePatchImages[BOTTOM] = bottom;
        ninePatchImages[CENTER] = center;
        ninePatchImages[LEFT_TOP] = leftTop;
        ninePatchImages[RIGHT_TOP] = rightTop;
        ninePatchImages[RIGHT_BOTTOM] = rightBottom;
        ninePatchImages[LEFT_BOTTOM] = leftBottom;

        /**
         * Set the default nine patch drawable.
         */
        this.drawable = new TnDrawable()
        {
            public void draw(AbstractTnGraphics g)
            {
                if (this.bounds == null)
                {
                    this.bounds = new TnRect(0, 0, TnNinePatchImage.this.getWidth(), TnNinePatchImage.this.getHeight());
                }
                
                TnNinePatchPainter.drawNinePatchBitmap(g, TnNinePatchImage.this, bounds);
            }
        };
    }
	
    public TnNinePatchImage(AbstractTnImage image)
    {
        this(image, null);
    }

    /**
     * construct a nine patch image object with 9 images.
     * 
     * @param image the image
     * @param splitColor the spliteColor of line
     */
    public TnNinePatchImage(AbstractTnImage image, Integer splitColor)
    {
        ninePatchImages = new AbstractTnImage[1];
        spliter = new StretchImageSpliter(image);
        if (splitColor != null)
        {
            spliter.setSpliterColor(splitColor.intValue());
        }
        splitedRects = spliter.executeSpliter();

        ninePatchImages[0] = spliter.image;
        this.setDrawMode(DRAW_SCALE);
        /**
         * Set the default nine patch drawable.
         */
        this.drawable = new TnDrawable()
        {
            public void draw(AbstractTnGraphics g)
            {
                if (this.bounds == null)
                {
                    this.bounds = new TnRect(0, 0, TnNinePatchImage.this.getWidth(), TnNinePatchImage.this.getHeight());
                }

                TnStretchPatchPainter.drawStretchBitmap(g, TnNinePatchImage.this, bounds, spliter);
            }
        };
    }
    
    /**
     * set the draw mode of nine-patch image.
     * 
     * @param drawMode {@link #DRAW_CARVEL}, {@link #DRAW_SCALE}
     */
    public void setDrawMode(int drawMode)
    {
        this.drawMode = drawMode;
    }
    
    /**
     * Get the draw mode of nine-patch image.
     * 
     * @return the draw mode.
     */
    public int getDrawMode()
    {
        return this.drawMode;
    }

    /**
     * set the radiant of the patch image.
     * 
     * @param patchPostion the patch's position
     * @param radiant the radiant image
     */
    public void setRadiant(int patchPostion, AbstractTnImage radiant)
    {
        if (patchPostion < 0 || patchPostion > 8)
        {
            throw new IllegalArgumentException("the patch's postion is wrong.");
        }

        if (radiantImages == null)
        {
            radiantImages = new AbstractTnImage[9];
        }

        radiantImages[patchPostion] = radiant;
    }

    /**
     * get the patch image.
     * 
     * @param patchPostion the patch's position
     * @return the patch image
     */
    public AbstractTnImage getPatch(int patchPostion)
    {
        if (patchPostion < 0 || patchPostion > 8)
        {
            throw new IllegalArgumentException("the patch's postion is wrong.");
        }

        return ninePatchImages[patchPostion];
    }

    /**
     * get the radiant on the patch image.
     * 
     * @param patchPostion the patch's position
     * @return the radiant image
     */
    public AbstractTnImage getRadiant(int patchPostion)
    {
        if (patchPostion < 0 || patchPostion > 8)
        {
            throw new IllegalArgumentException("the patch's postion is wrong.");
        }

        return radiantImages == null ? null : radiantImages[patchPostion];
    }

    public void clear(int color)
    {
        for(int i = 0; i < ninePatchImages.length; i++)
        {
            if(ninePatchImages[i] != null)
            {
                ninePatchImages[i].clear(color);
            }
        }
        
        if(radiantImages == null)
            return;
        
        for(int i = 0; i < radiantImages.length; i++)
        {
            if(radiantImages[i] != null)
            {
                radiantImages[i].clear(color);
            }
        }
    }
    
    public void release()
    {
        for(int i = 0; i < ninePatchImages.length; i++)
        {
            ninePatchImages[i].release();
        }
        
        if(radiantImages == null)
            return;
        
        for(int i = 0; i < radiantImages.length; i++)
        {
            if(radiantImages[i] != null)
            {
                radiantImages[i].release();
            }
        }
    }
    
    public boolean isRelease()
    {
        return ninePatchImages[0].isRelease();
    }

    public AbstractTnGraphics getGraphics()
    {
        return null;
    }

    public Object getNativeImage()
    {
        return null;
    }

    public boolean isMutable()
    {
        return false;
    }

    public void getARGB(int[] argbData, int offset, int scanLength, int x, int y, int width, int height)
    {
        // TODO Auto-generated method stub
        
    }	
}

class StretchImageSpliter
{
    private static final int blackColor = 0xFF000000;

    AbstractTnImage image;

    private int verticalSplitedAreaCount = 1;

    private int horizonSplitedAreaCount = 1;

    int spliteColor = 0xFFFF00E4;

    Vector verticalSplitedPoints = new Vector();

    Vector horizonSplitedPoints = new Vector();

    // the area which can be streteched by horizon
    Vector mainHorizonStretechableRects = new Vector();

    // the area which can be streteched by vertical
    Vector mainVerticalStretechableRects = new Vector();

    public StretchImageSpliter(AbstractTnImage image)
    {
        this.image = image;
    }

    protected int getVerticalSplitedAreaCount()
    {
        return this.verticalSplitedAreaCount;
    }

    protected int getHorizonSplitedAreaCount()
    {
        return this.horizonSplitedAreaCount;
    }

    protected TnStretchRect getMainVerticalStretechableRect(int index)
    {
        if (index > mainVerticalStretechableRects.size() - 1 || index < 0)
        {
            return null;
        }
        else
        {
            return (TnStretchRect) this.mainVerticalStretechableRects.elementAt(index);
        }
    }

    protected TnStretchRect getMainHorizonStretechableRect(int index)
    {
        if (index > mainHorizonStretechableRects.size() - 1 || index < 0)
        {
            return null;
        }
        else
        {
            return (TnStretchRect) this.mainHorizonStretechableRects.elementAt(index);
        }
    }

    protected int getTotalVerticalStrechableAreaHeight()
    {
        int result = 0;
        for (int i = 0; i < this.mainVerticalStretechableRects.size(); i++)
        {
            result += ((TnStretchRect) mainVerticalStretechableRects.elementAt(i)).height();
        }
        return result;
    }

    protected int getTotalHorizonStrechableAreaWidth()
    {
        int result = 0;
        for (int i = 0; i < this.mainHorizonStretechableRects.size(); i++)
        {
            result += ((TnStretchRect) mainHorizonStretechableRects.elementAt(i)).width();
        }
        return result;
    }
    
    void setSpliterColor(int color)
    {
        this.spliteColor = color;
    }

    protected Vector executeSpliter()
    {
        // just used for sort the rects
        Vector tempSplitedRects = new Vector();
        
        if (image == null)
        {
            return null;
        }
        
        Vector splitedRects = new Vector();
        // get the identified line position
        int width = image.getWidth();
        int height = image.getHeight();
        boolean splitPointStart = false;
        int[] xArgbData = new int[width];
        int[] yArgbData = new int[height];
        image.getARGB(xArgbData, 0, width, 0, 0, width, 1);
        image.getARGB(yArgbData, 0, 1, 0, 0, 1, height);
        int bitmapColorWithOutArpha = 0;
        for (int x = 0; x < width; x++)
        {
            bitmapColorWithOutArpha = xArgbData[x] | 0xFF000000;
            if ((bitmapColorWithOutArpha == spliteColor || bitmapColorWithOutArpha == blackColor) && !splitPointStart && xArgbData[x] != 0)
            {
                splitPointStart = true;
                int xPos = x == 0 ? 0 : x - 1;
                TnPoint point = new TnPoint(xPos, 0);
                horizonSplitedPoints.add(point);
                horizonSplitedAreaCount += 1;
            }
            else if (((bitmapColorWithOutArpha != spliteColor && bitmapColorWithOutArpha != blackColor) || xArgbData[x] == 0 || x == width - 1) && splitPointStart)
            {
                splitPointStart = false;
                int xPos = x - 1 > 0 ? x - 1 : 0;
                TnPoint point = new TnPoint(xPos, 0);
                horizonSplitedPoints.add(point);
                mainHorizonStretechableRects.add(new TnStretchRect(
                        ((TnPoint) horizonSplitedPoints.elementAt(horizonSplitedPoints.size() - 2)).x, 0, point.x, image.getHeight()));
                horizonSplitedAreaCount += 1;
            }
        }

        for (int y = 0; y < height; y++)
        {
            bitmapColorWithOutArpha = yArgbData[y] | 0xFF000000;
            if ((bitmapColorWithOutArpha == spliteColor || bitmapColorWithOutArpha == blackColor) && !splitPointStart && yArgbData[y] != 0)
            {
                splitPointStart = true;
                int yPos = y == 0 ? 0 : y - 1;
                TnPoint point = new TnPoint(0, yPos);
                verticalSplitedPoints.add(point);
                verticalSplitedAreaCount += 1;
            }
            else if (((bitmapColorWithOutArpha != spliteColor && bitmapColorWithOutArpha != blackColor) || yArgbData[y] == 0 || y == height - 1) && splitPointStart)
            {
                splitPointStart = false;
                int yPos = y - 1 > 0 ? y - 1 : 0;
                TnPoint point = new TnPoint(0, yPos);
                verticalSplitedPoints.add(point);
                mainVerticalStretechableRects.add(new TnStretchRect(0, ((TnPoint) verticalSplitedPoints.elementAt(verticalSplitedPoints
                        .size() - 2)).y, image.getWidth(), point.y));
                verticalSplitedAreaCount += 1;
            }
        }

        //cut the border of the image which contains 1px line                        
        image = AbstractTnGraphicsHelper.getInstance().createImage(image, 1, 1, image.getWidth() - 2, image.getHeight() - 2);
        
        int left = 0;
        int top = 0;
        TnStretchRect stretchRect = null;
        for (int i = 0; i < horizonSplitedPoints.size(); i++)
        {
            left = i > 0 ? ((TnPoint) horizonSplitedPoints.elementAt(i - 1)).x : 0;
            top = 0;
            for (int j = 0; j < verticalSplitedPoints.size(); j++)
            {
                stretchRect = createTnStretchRect(left, top, ((TnPoint) horizonSplitedPoints.elementAt(i)).x,
                    ((TnPoint) verticalSplitedPoints.elementAt(j)).y);
                splitedRects.add(stretchRect);

                if (j == verticalSplitedPoints.size() - 1)
                {
                    stretchRect = createTnStretchRect(left, ((TnPoint) verticalSplitedPoints.elementAt(j)).y,
                        ((TnPoint) horizonSplitedPoints.elementAt(i)).x, image.getHeight());
                    splitedRects.add(stretchRect);
                }

                if (i == horizonSplitedPoints.size() - 1)
                {
                    stretchRect = createTnStretchRect(((TnPoint) horizonSplitedPoints.elementAt(i)).x, top, image.getWidth(),
                        ((TnPoint) verticalSplitedPoints.elementAt(j)).y);
                    tempSplitedRects.add(stretchRect);
                }
                if (i == horizonSplitedPoints.size() - 1 && j == verticalSplitedPoints.size() - 1)
                {
                    stretchRect = createTnStretchRect(((TnPoint) horizonSplitedPoints.elementAt(i)).x,
                        ((TnPoint) verticalSplitedPoints.elementAt(j)).y, image.getWidth(), image.getHeight());
                    tempSplitedRects.add(stretchRect);
                }
                top = ((TnPoint) verticalSplitedPoints.elementAt(j)).y;
            }
        }

        splitedRects.addAll(tempSplitedRects);

        return splitedRects;
    }

    private TnStretchRect createTnStretchRect(int left, int top, int right, int bottom)
    {
        TnStretchRect rect = new TnStretchRect(left, top, right, bottom);
        for (int i = 0; i < mainVerticalStretechableRects.size(); i++)
        {
            if (((TnStretchRect) mainVerticalStretechableRects.elementAt(i)).contains(rect))
            {
                rect.setVerticalStretchable(true);
                break;
            }
        }

        for (int i = 0; i < mainHorizonStretechableRects.size(); i++)
        {
            if (((TnStretchRect) mainHorizonStretechableRects.elementAt(i)).contains(rect))
            {
                rect.setHorizonStretchable(true);
                break;
            }
        }
        rect.set(left, top, right, bottom);
        return rect;
    }
}

class TnStretchRect extends TnRect
{
    private boolean isVerticalStretchable = false;

    private boolean isHorizonStretchable = false;

    public TnStretchRect(int x, int y, int right, int bottom)
    {
        super(x, y, right, bottom);
    }

    protected void setVerticalStretchable(boolean b)
    {
        this.isVerticalStretchable = b;
    }

    protected boolean getVerticalStretchable()
    {
        return this.isVerticalStretchable;
    }

    protected void setHorizonStretchable(boolean b)
    {
        this.isHorizonStretchable = b;
    }

    protected boolean getHorizonStretchable()
    {
        return this.isHorizonStretchable;
    }
    
    /**
     * Compares this instance with the specified object and indicates if they are equal.
     */
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
    
    /**
     * Returns the hashcode for this <code>TnRect</code>.
     */
    public int hashCode()
    {
        return super.hashCode();
    }
}