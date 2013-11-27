/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenCircleAnimation.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.frogui.widget.AbstractFrogAnimation;

/**
 *@author bduan
 *@date Aug 23, 2010
 */
public class CitizenCircleAnimation extends AbstractFrogAnimation
{
    protected final static int HUGE_DROP_RADIUS = 11;
    protected final static int BIG_DROP_RADIUS = 10;
    protected final static int MIDDLE_DROP_RADIUS = 9;
    protected final static int SMALL_DROP_RADIUS = 8;
    
    protected final static int MINI_MODE_DROP_RADIUS = 2;
    
    protected final static int START_TRANSPARENT = 0xCA;
    protected final static int END_TRANSPARENT = 0x10;
    protected final static int DEFAULT_DROP_COLOR = 0x003AA5DC;
    
    protected int starndardColor = DEFAULT_DROP_COLOR;
    
    protected final static int X_POS = 0;
    protected final static int Y_POS = 1;
    
    protected int[][] dropPosition;
    protected int [] dropColors;
    protected int [] dropSizes;
    protected int x, y, radius;
    protected int radiusDelta;
    
    protected boolean isFirstRound = true;
    protected boolean isEnable = true;
    protected boolean isMiniMode = false;
    
    /**
     * create a CircleAnimation.
     * you could set the mode. If mini, there
     * will be 8 points per circle, else 12 points.
     * 
     * Normally, 12 points is ok, but too many for very
     * small circle. Please set mini to true for small one 
     * such as the progress in TextField.
     * 
     * @param id
     * @param isMiniMode
     */
    public CitizenCircleAnimation(int id, boolean isMiniMode)
    {
        super(id);
        this.isMiniMode = isMiniMode;
        starndardColor = UiStyleManager.getInstance().getColor(
            UiStyleManager.CLOCK_ANIMATION_DEFAULT_COLOR);
        ;
    }

    /**
     * Enable/Disable the circle animation.
     * 
     * @param isEnable
     */
    public void enable(boolean isEnable)
    {
        this.isEnable = isEnable;
        if (!isEnable)
        {
            TnUiTimer.getInstance().removeReceiver(this);
            reset();
        }
    }
    
    /**
     * Set the drop size. The size defined in array will be used while
     * painting drops. If the length of the dropSizes is shorter
     * than drops' real length, it will use dropSizes first, and left
     * drop's size equal to dropSizes[dropSizes.length - 1]
     * 
     * @param dropSizes dropSizes array
     */
    public void setDropSizes(int[] dropSizes)
    {
        if(dropSizes != null && dropSizes.length > 0)
        {
            for (int i = 0; i < dropSizes.length; i++)
            {
                if(dropSizes[i] == 0) dropSizes[i]=1;
            }
            this.dropSizes = dropSizes;
        }
        
    }
    
    /**
     * Set the drop color. The color defined in array will be used while
     * painting drops. If the length of the dropColors is shorter
     * than drops' real length, it will use dropColors first, and left
     * drop's color equal to dropColors[dropColors - 1]
     * 
     * @param dropColors dropColors array
     */
    public void setDropColors(int[] dropColors)
    {
        if(dropColors != null && dropColors.length > 0)
        {
            this.dropColors = dropColors;
        }
    }
    
    public void setStarndardColor(int color)
    {
        this.starndardColor = color;
    }
    
    public void setRadiusDelta(int radiusDelta)
    {
        this.radiusDelta = radiusDelta;
    }
    
    public void setMiniMode(boolean isMiniMode)
    {
        this.isMiniMode = isMiniMode;
    }
    
    public void sublayout(int width, int height)
    {
        super.sublayout(width, height);
        x = 0;
        y = 0;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        if(!isEnable)
            return;
        
        if(x == 0 || y == 0)
        {
            int width = this.getWidth();
            if(width <= 0)
            {
                width = this.getPreferredWidth();
            }
            int height = this.getHeight();
            if(height <= 0)
            {
                height = this.getPreferredHeight();
            }
            x = width / 2;
            y = height / 2;
            if(this.radius <= 0)
            {
                if (height < width)
                    radius = height / 2;
                else
                    radius = width / 2;
            }
            
            radius -= radiusDelta;
            initParameters();
        }
        
        boolean isAntiAlias = graphics.isAntiAlias();
        graphics.setAntiAlias(true);
        
        if(dropPosition != null)
        {
            try
            {
                int length = dropPosition.length;
                if (length > 0)
                {
                    for (int i = 0; i < length; i++)
                    {
                        int color = getDropColor(step, i, length);
    
                        graphics.setColor(TnColor.alpha(color), TnColor.red(color), TnColor.green(color), TnColor.blue(color));
    
                        int r = getDropSize(step, i, length);
    
                        graphics.fillCircle(dropPosition[i][X_POS], dropPosition[i][Y_POS], r, false);
                    }
                }
            }
            catch(Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        graphics.setAntiAlias(isAntiAlias);
    }

    protected void initParameters()
    {
        initDropSizes();
        radius = radius - dropSizes[0];
        initDropPositions();
        initDropColors();
    }

    protected int getDropColor(int step, int i, int length)
    {
        if(dropColors == null)
            initParameters();
        
        int color = dropColors[dropColors.length - 1];
        int span = step - i;
        
        if(span < 0 && !isFirstRound)
        {
            span += length;
        }
        
        if(span >= 0 && span < dropColors.length)
        {
            color = dropColors[span];
        }
        
        return color;
    }

    protected int getDropSize(int step, int i, int length)
    {
        if(dropSizes == null)
            initParameters();
            
        int size = dropSizes[dropSizes.length - 1];
        int span = step - i;
        
        if(span < 0)
        {
            span += length;
        }
        
        if(span < dropSizes.length)
        {
            size = dropSizes[span];
        }
        
        return size;
    
    }

    protected void initDropSizes()
    {
        if (dropSizes != null)//don't override user's setting.
            return;
        
        if (isMiniMode)
        {
            dropSizes = new int[1];

            dropSizes[0] = MINI_MODE_DROP_RADIUS;
        }
        else
        {
            dropSizes = new int[4];
            
            dropSizes[0] = HUGE_DROP_RADIUS;
            dropSizes[1] = BIG_DROP_RADIUS;
            dropSizes[2] = MIDDLE_DROP_RADIUS;
            dropSizes[3] = SMALL_DROP_RADIUS;
        }
    }
    
    protected void initDropColors()
    {
        if(dropColors != null)
        {
            return; //doesn't override user's setting.
        }
        
        if(isMiniMode)
            dropColors = new int[8];
        else
            dropColors = new int[12];
        
        int perChange = ( START_TRANSPARENT - END_TRANSPARENT ) / dropColors.length;
        for(int i = 0 ; i < dropColors.length ; i ++)
        {
            int transparent = START_TRANSPARENT - perChange * i;
            dropColors[i] =  (transparent << 24 ) | starndardColor;
        }
    }

    protected void initDropPositions()
    {
        if(dropPosition != null)
            return;
        
        if (isMiniMode)
        {
            dropPosition = new int[8][2];

            int delta = (int) (Math.sqrt(2) * 1000 * radius) / (2 * 1000);

            dropPosition[0][X_POS] = x;
            dropPosition[0][Y_POS] = y - radius;

            dropPosition[1][X_POS] = x + delta;
            dropPosition[1][Y_POS] = y - delta;

            dropPosition[2][X_POS] = x + radius;
            dropPosition[2][Y_POS] = y;

            dropPosition[3][X_POS] = x + delta;
            dropPosition[3][Y_POS] = y + delta;

            dropPosition[4][X_POS] = x;
            dropPosition[4][Y_POS] = y + radius;

            dropPosition[5][X_POS] = x - delta;
            dropPosition[5][Y_POS] = y + delta;

            dropPosition[6][X_POS] = x - radius;
            dropPosition[6][Y_POS] = y;

            dropPosition[7][X_POS] = x - delta;
            dropPosition[7][Y_POS] = y - delta;
        }
        else
        {
            dropPosition = new int[12][2];

            double sqrt_3 = Math.sqrt(3);

            int delta_sin30 = radius / 2;
            int delta_sin60 = (int) (sqrt_3 * 1000 * radius) / (2 * 1000);

            dropPosition[0][X_POS] = x;
            dropPosition[0][Y_POS] = y - radius;

            dropPosition[1][X_POS] = x + delta_sin30;
            dropPosition[1][Y_POS] = y - delta_sin60;

            dropPosition[2][X_POS] = x + delta_sin60;
            dropPosition[2][Y_POS] = y - delta_sin30;

            dropPosition[3][X_POS] = x + radius;
            dropPosition[3][Y_POS] = y;

            dropPosition[4][X_POS] = x + delta_sin60;
            dropPosition[4][Y_POS] = y + delta_sin30;

            dropPosition[5][X_POS] = x + delta_sin30;
            dropPosition[5][Y_POS] = y + delta_sin60;

            dropPosition[6][X_POS] = x;
            dropPosition[6][Y_POS] = y + radius;

            dropPosition[7][X_POS] = x - delta_sin30;
            dropPosition[7][Y_POS] = y + delta_sin60;

            dropPosition[8][X_POS] = x - delta_sin60;
            dropPosition[8][Y_POS] = y + delta_sin30;

            dropPosition[9][X_POS] = x - radius;
            dropPosition[9][Y_POS] = y;

            dropPosition[10][X_POS] = x - delta_sin60;
            dropPosition[10][Y_POS] = y - delta_sin30;

            dropPosition[11][X_POS] = x - delta_sin30;
            dropPosition[11][Y_POS] = y - delta_sin60;
        }
    }

    protected void updateFrame()
    {
        if(dropPosition != null && dropPosition.length > 0)
        {
            step ++ ;
            if(step >= dropPosition.length)
            {
                isFirstRound = false;
                step = 0;
            }
            this.requestPaint();
        }
    }
    
    protected void handleUiTimerReceiver()
    {
        //override parent implementation.
    }
    
    protected void onDisplay()
    {
        TnUiTimer.getInstance().addReceiver(this, 130);
    }

    protected void onUndisplay()
    {
        TnUiTimer.getInstance().removeReceiver(this);
    }
    
    public void reset()
    {
        dropPosition = null;
        dropColors = null;
        //dropSizes = null;
        x = 0;
        y = 0;
        step = 0;
        radius = 0;
    }

}
