/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenProgressBar.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.frogui.widget.AbstractFrogAnimation;

/**
 *@author bduan
 *@date 2010-12-6
 */
public class CitizenProgressBar extends AbstractFrogAnimation
{
    public static final int KEY_PROGRESSBAR_IMAGE_FOCUS_END = 11023;

    protected int intervalStart = 0;
    protected int intervalEnd = 100;
    protected int intervalProgress = 0;
    protected int movingSpeed = 0;
    protected int progress = 0;
    
    protected int counter = 0;
    protected int mod = 1;
    
    /**
     * font of the text
     */
    protected AbstractTnFont font;
    
    public CitizenProgressBar(int id)
    {
        super(id);
    }
    
    public void setProgress(int progress)
    {
        if(progress >= intervalStart && progress <= intervalEnd)
        {
            this.progress = progress;
        }
    }
    
    /**
     * retrieve the font of the text
     * 
     * @return font of the text
     */
    public AbstractTnFont getFont()
    {
        return font;
    }

    /**
     * set the font of the component
     * 
     * @param font font of the text(@link AbstractTnFont)
     */
    public void setFont(AbstractTnFont font)
    {
        this.font = font;
        if (nativeUiComponent != null)
        {
            requestLayout();
            requestPaint();
        }
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        if(progress <= intervalStart || progress > intervalEnd)
            return;
        
        drawProgress(graphics);
    }
    
    protected void paintBackground(AbstractTnGraphics graphics)
    {
        //override super method.
    }
    
    protected void drawProgress(AbstractTnGraphics graphics)
    {
        if (this.tnUiArgs != null)
        {
            int width = this.getWidth();
            int height = this.getHeight();
            
            if(leftPadding + rightPadding > width)
            {
                leftPadding = 0;
                rightPadding = 0;
            }
            
            if(topPadding + bottomPadding > height)
            {
                topPadding = 0;
                bottomPadding = 0;
            }
            
            int progressWidth = progress * ( width - leftPadding - rightPadding ) / (intervalEnd - intervalStart);
            int color = graphics.getColor();
            graphics.setFont(font);
            
            TnUiArgAdapter uiArgAdapter = null;
            if(progress < intervalEnd)
            {
                uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
                drawContent(graphics, uiArgAdapter, new TnRect(progressWidth + leftPadding, topPadding, width - rightPadding, height - bottomPadding), TnColor.BLACK);
            }
            
            if(progress > intervalStart)
            {
                if(progress == intervalEnd && this.tnUiArgs.get(KEY_PROGRESSBAR_IMAGE_FOCUS_END) != null)
                {
                    int endWidth = progressWidth / 10;
                    uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
                    drawContent(graphics, uiArgAdapter, new TnRect(leftPadding, topPadding, (progressWidth - endWidth) + leftPadding, height - bottomPadding), TnColor.WHITE);
                
                    uiArgAdapter = this.tnUiArgs.get(KEY_PROGRESSBAR_IMAGE_FOCUS_END);
                    drawContent(graphics, uiArgAdapter, new TnRect((progressWidth - endWidth) + leftPadding, topPadding, progressWidth + leftPadding, height - bottomPadding), TnColor.WHITE);
                }
                else
                {
                    uiArgAdapter = this.tnUiArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
                    drawContent(graphics, uiArgAdapter, new TnRect(leftPadding, topPadding, progressWidth + leftPadding, height - bottomPadding), TnColor.WHITE);
                }
                
            }
            
            graphics.setColor(color);
        }
    }
    
    private void drawContent(AbstractTnGraphics graphics, TnUiArgAdapter imageAdapter, TnRect rect, int color)
    {
        if (imageAdapter != null)
        {
            AbstractTnImage bgImage = imageAdapter.getImage();
            if(bgImage != null)
            {
                int width = getWidth();
                int height = getHeight();
                
                bgImage.setWidth(width - leftPadding - rightPadding);
                bgImage.setHeight(height - topPadding - bottomPadding);
                graphics.pushClip(rect);
                graphics.drawImage(bgImage, leftPadding, topPadding, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                graphics.setColor(color);
                graphics.drawString(""+ progress + "%", leftPadding + ( width - leftPadding - rightPadding) /2 , topPadding -1 + ( height - topPadding - bottomPadding) / 2, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.popClip();
            }
        }
    }

    protected void reset()
    {
        progress = 0;
        TnUiTimer.getInstance().removeReceiver(this);
    }

    protected void updateFrame()
    {
        if ((counter % mod) == 0)
        {
            int add = 1;
            int left = 100 - this.intervalProgress;
            if (left > 0)
            {
                mod = 100 / left;
                counter = 0;
            }
            else
            {
            }
            if (mod > 20)
                mod = 20;

            this.intervalProgress += add;
            if (intervalProgress > 100)
                intervalProgress = 100;
            this.progress = this.intervalStart + this.intervalProgress * (this.intervalEnd - this.intervalStart) / 100;

            this.requestPaint();
        }
        counter++;
    }

}
