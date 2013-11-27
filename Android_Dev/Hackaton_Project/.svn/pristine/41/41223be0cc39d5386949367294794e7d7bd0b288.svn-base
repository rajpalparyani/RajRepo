/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CitizenClockCircleAnimation.java
 *
 */

package com.telenav.ui.citizen;

import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;


/**
 *@author qyweng
 *@date Nov 21, 2011
 */
public class CitizenClockCircleAnimation extends CitizenCircleAnimation 
{
    protected final static int START_TRANSPARENT = 0xA3;

    protected final static int END_TRANSPARENT = 0x00;

    protected final static int DEFAULT_DROP_COLOR =  0x000000FF/* 0x003AA5DC */;

    protected int upNeedleColor;

    protected int downNeedleColor;
    
    protected float needleStepDegree = 10;
    
    protected int needleStep = 0;
    
    protected AbstractTnImage image;
    
    protected Object mutex = new Object();

    /**
      
     * [QY]Current color is blue for test for while color couldn't be shown on some of the component
        with white background 
     * @param id
     * @param isMiniMode
     */
    
    public CitizenClockCircleAnimation(int id, boolean isMiniMode)
    {
        super(id, isMiniMode);
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        if (!isEnable)
            return;
        int width = 0, height = 0;
        if (x == 0 || y == 0)
        {
            width = this.getWidth();
            if (width <= 0)
            {
                width = this.getPreferredWidth();
            }
            height = this.getHeight();
            if (height <= 0)
            {
                height = this.getPreferredHeight();
            }
            x = width / 2;
            y = height / 2;
            if (this.radius <= 0)
            {
                if (height < width)
                    radius = height / 2;
                else
                    radius = width / 2;
                
                initParameters();
            }
            radius -= radiusDelta;
        }
        
        if (x == 0 || y == 0)
            return;

        synchronized(mutex)
        {
            if (dropPosition != null)
            {
                if(image == null || image.getNativeImage() == null)
                {
                    image = AbstractTnUiHelper.getInstance().createImage(2 * x, 2 * y);
                }
                
                if(image == null || image.getNativeImage() == null)
                    return;
                
                image.clear(0x0);
                int length = dropPosition.length;
                if (length > 0)
                {
                    AbstractTnGraphics g = image.getGraphics();
                    g.setAntiAlias(true);
                    for (int i = 0; i < length; i++)
                    {
                        int color = getDropColor(getStep(), i, length);
                        g.setColor(TnColor.alpha(color), TnColor.red(color), TnColor.green(color), TnColor.blue(color));
                        float degreeInterval = 360f / length;
                        float pointDegree = 360 - degreeInterval * i;
                        float dropDegree = degreeInterval * 3 / 5;
                        g.fillArcs(x, y, radius + dropSizes[0], pointDegree - dropDegree / 2, dropDegree);
                    }
                    g.fillCircle(x, y, radius, true);
                    int r = dropSizes[0] * 2 / 3;
                    int blank = r / 2;
                    g.translate(x, y);
                    float degree = getNeedleStepDegree()* (needleStep % (int)(360.0/getNeedleStepDegree()));
                    g.rotate((int) degree, 0, 0);
                    g.setAntiAlias(true);
                    g.setColor(TnColor.alpha(upNeedleColor), TnColor.red(upNeedleColor), TnColor.green(upNeedleColor), TnColor.blue(upNeedleColor));
                    g.fillTriangle(0, 0 - radius + r, 0 - r, 0 - blank, 0 + r, 0 - blank);
                    g.setColor(TnColor.alpha(downNeedleColor), TnColor.red(downNeedleColor), TnColor.green(downNeedleColor), TnColor.blue(downNeedleColor));
                    g.fillTriangle(0, 0 + radius - r, 0 - r, 0 + blank, 0 + r, 0 + blank);
                    g.setColor(0xff, 0, 0, 0);
                    graphics.drawImage(image, x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                    g.rotate((int) -degree, 0, 0);
                    g.translate(-x, -y);
                    g.setAntiAlias(false);
                }
            }
        }
    }
    
    protected void initDropColors()
    {
        if (dropColors != null)
        {
            return; // doesn't override user's setting.
        }

        dropColors = new int[16];

        int perChange = (START_TRANSPARENT - END_TRANSPARENT) / dropColors.length;
        for (int i = 0; i < dropColors.length; i++)
        {
            int transparent = START_TRANSPARENT - perChange * i;
            dropColors[i] = (transparent << 24) | starndardColor;
        }
        upNeedleColor = START_TRANSPARENT << 24 | starndardColor;
        downNeedleColor =START_TRANSPARENT/6 << 24 | starndardColor;
    }
    
    public int getDropColor(int step, int i, int length)
    {
        if (dropColors == null)
            initParameters();

        int color = dropColors[dropColors.length - 1];
        int span = step - i;

        if (span < 0 && !isFirstRound)
        {
            span += length;
        }
     
        if (span >= 0 && span < dropColors.length)
        {
            color = dropColors[span];
        }

        return color;
    }

    protected void initDropPositions()
    {
        if (dropPosition != null)
            return;

        dropPosition = new int[16][2];
    }
    
    public int getStarndardColor()
    {
        return starndardColor;
    }
    
    public int getCirclePointX()
    {
        return x;    
    }
    
    public int getCirclePointY()
    {
        return y;
    }

    public void setStarndardColor(int starndardColor)
    {
        this.starndardColor = starndardColor;
    }

    public int getUpNeedleColor()
    {
        return upNeedleColor;
    }

    public void setUpNeedleColor(int upNeedleColor)
    {
        this.upNeedleColor = upNeedleColor;
    }

    public int getDownNeedleColor()
    {
        return downNeedleColor;
    }

    public void setDownNeedleColor(int downNeedleColor)
    {
        this.downNeedleColor = downNeedleColor;
    }

    public float getNeedleStepDegree()
    {
        return needleStepDegree;
    }

    public void setNeedleStepDegree(float needleStepDegree)
    {
        this.needleStepDegree = needleStepDegree;
    }
    
    public boolean getIsEnable()
    {
        return isEnable;
    }

    public int[][] getDropPosition()
    {
        return dropPosition;
    }

    public void setDropPosition(int[][] dropPosition)
    {
        this.dropPosition = dropPosition;
    }

    public int[] getDropColors()
    {
        return dropColors;
    }

    public void setDropColors(int[] dropColors)
    {
        this.dropColors = dropColors;
    }

    public int[] getDropSizes()
    {
        return dropSizes;
    }

    public void setDropSizes(int[] dropSizes)
    {
        this.dropSizes = dropSizes;
    }

    public int getRadius()
    {
        return radius;
    }
    
    public int getRadiusDelta()
    {
        return radiusDelta;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }
    
    public int getNeedleStep()
    {
        return needleStep;
    }

    protected void updateFrame()
    {
        if (dropPosition != null && dropPosition.length > 0)
        {
            needleStep++;
            if(needleStep%2==0)
            {
                step++;
            }
            if (needleStep >= 36)
            {
                needleStep = 0;
                    
            }
            if (step >= dropPosition.length)
            {
                isFirstRound = false;
                step = 0;
            }
            this.requestPaint();
        }
    }
    public int getStep()
    {
         return step;
    }

    protected void onDisplay()
    {
        TnUiTimer.getInstance().addReceiver(this, 20);
    }

    @Override
    public void reset()
    {
        synchronized(mutex)
        {
            if (image != null)
            {
                image.release();
                image = null;
            }
            //fix bug  http://jira.telenav.com:8080/browse/TNANDROID-4236
            //super.reset();
        }
    }
}
