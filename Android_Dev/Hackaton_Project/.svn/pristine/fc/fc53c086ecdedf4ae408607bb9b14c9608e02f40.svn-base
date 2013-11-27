/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MicComponent.java
 *
 */
package com.telenav.module.dsr;

import com.telenav.dsr.IVolumeChangeListener;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;

/**
 *@author bduan
 *@date 2011-1-20
 */
public class MicrophoneComponent extends AbstractTnComponent implements IVolumeChangeListener
{
    protected int lastVolumeLevel = 0;
    protected boolean lastMicStatus = false;//true for bright
    protected TnRect clip;
    protected int lowLeft = 1;
    protected int lowRight = 2;
    protected int normalLeft = 3;
    protected int normalRight = 4;
    protected int loudLeft = 5;
    protected int loudRight = 6;
    
    
    public MicrophoneComponent(int id)
    {
        super(id);
    }
    
    public void sublayout(int width, int height)
    {
        if (getMicIcon() == null || getWave(loudLeft, true) == null || getWave(loudRight, true) == null || getWave(lowLeft, true) == null
                || getWave(lowRight, true) == null || getWave(normalLeft, true) == null || getWave(normalRight, true) == null
                || (preferHeight > 0 && preferWidth > 0))
            return;

        preferHeight = getMicIcon().getHeight();
        preferWidth = getMicIcon().getWidth();

        int waveH = getWave(loudLeft, true).getHeight();
        if (waveH > preferHeight)
        {
            preferHeight = waveH;
        }
        
        preferWidth = getMicIcon().getWidth() + 2 * getWave(lowLeft, true).getWidth() + 2 * getWave(normalLeft, true).getWidth() + 2
                * getWave(loudLeft, true).getWidth();

    }
    
    protected AbstractTnImage getMicIcon()
    {
        if(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            return ImageDecorator.IMG_MIC_ICON_UNFOCUS.getImage();
        }
        else
        {
            return ImageDecorator.IMG_MIC_ICON_LANDSCAPE_UNFOCUS.getImage();
        }
    }
    
    protected AbstractTnImage getWave(int position, boolean isFocus)
    {
        AbstractTnImage image;
        switch (position)
        {
            case 1:
            {
                image = isFocus? ImageDecorator.IMG_WAVE_ICON_LOW_LEFT_FOCUS.getImage() : ImageDecorator.IMG_WAVE_ICON_LOW_LEFT_UNFOCUS.getImage();
                break;
            }
            case 2:
            {
                image = isFocus? ImageDecorator.IMG_WAVE_ICON_LOW_RIGHT_FOCUS.getImage() : ImageDecorator.IMG_WAVE_ICON_LOW_RIGHT_UNFOCUS.getImage();
                break;
            }
            case 3:
            {
                image = isFocus? ImageDecorator.IMG_WAVE_ICON_NORMAL_LEFT_FOCUS.getImage() : ImageDecorator.IMG_WAVE_ICON_NORMAL_LEFT_UNFOCUS.getImage();
                break;
            }
            case 4:
            {
                image = isFocus? ImageDecorator.IMG_WAVE_ICON_NORMAL_RIGHT_FOCUS.getImage() : ImageDecorator.IMG_WAVE_ICON_NORMAL_RIGHT_UNFOCUS.getImage();
                break;
            }
            case 5:
            {
                image = isFocus? ImageDecorator.IMG_WAVE_ICON_LOUD_LEFT_FOCUS.getImage() : ImageDecorator.IMG_WAVE_ICON_LOUD_LEFT_UNFOCUS.getImage();
                break;
            }
            case 6:
            {
                image = isFocus? ImageDecorator.IMG_WAVE_ICON_LOUD_RIGHT_FOCUS.getImage() : ImageDecorator.IMG_WAVE_ICON_LOUD_RIGHT_UNFOCUS.getImage();
                break;
            }
            default:
            {
                image = null;
                break;
            }
        }
        return image;
    }
      
    protected void paint(AbstractTnGraphics graphics)
    {
        if(getMicIcon() == null || getWave(loudLeft, true) == null || getWave(loudRight, true) == null || getWave(lowLeft, true) == null || getWave(lowRight, true) == null || getWave(normalLeft, true) == null || getWave(normalRight, true) == null
                || getWave(loudLeft, false) == null || getWave(loudRight, false) == null || getWave(lowLeft, false) == null || getWave(lowRight, false) == null || getWave(normalLeft, false) == null || getWave(normalRight, false) == null)
            return;

        int width = this.getWidth();
        int height = this.getHeight();
        int x = width / 2;
        int y = height / 2;
       
        graphics.drawImage(getMicIcon(), x, y, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        
        int waveY = y - (getMicIcon().getHeight() / 2 - getWave(loudRight, true).getHeight() / 2);
        
        switch (lastVolumeLevel)
        {
            case 1:
            {
                graphics.drawImage(getWave(lowLeft, true), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, true).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(lowRight, true), x + getMicIcon().getWidth() / 2 + getWave(lowRight, true).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(normalLeft, false), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, false).getWidth()
                        - getWave(normalLeft, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(normalRight, false), x + getMicIcon().getWidth() / 2 + getWave(lowRight, false).getWidth()
                        + getWave(normalRight, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(loudLeft, false), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, false).getWidth()
                        - getWave(normalLeft, false).getWidth() - getWave(loudLeft, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(loudRight, false), x + getMicIcon().getWidth() / 2 + getWave(lowRight, false).getWidth()
                        + getWave(normalRight, false).getWidth() + getWave(loudRight, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                break;
            }
            case 2:
            {
                graphics.drawImage(getWave(lowLeft, true), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, true).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(lowRight, true), x + getMicIcon().getWidth() / 2 + getWave(lowRight, true).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(normalLeft, true), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, true).getWidth()
                        - getWave(normalLeft, true).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(normalRight, true), x + getMicIcon().getWidth() / 2 + getWave(lowRight, true).getWidth()
                        + getWave(normalRight, true).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(loudLeft, false), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, false).getWidth()
                        - getWave(normalLeft, false).getWidth() - getWave(loudLeft, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(loudRight, false), x + getMicIcon().getWidth() / 2 + getWave(lowRight, false).getWidth()
                        + getWave(normalRight, false).getWidth() + getWave(loudRight, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                break;
            }
            case 3:
            {
                graphics.drawImage(getWave(lowLeft, true), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, true).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(lowRight, true), x + getMicIcon().getWidth() / 2 + getWave(lowRight, true).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(normalLeft, true), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, true).getWidth()
                        - getWave(normalLeft, true).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(normalRight, true), x + getMicIcon().getWidth() / 2 + getWave(lowRight, true).getWidth()
                        + getWave(normalRight, true).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(loudLeft, true), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, true).getWidth()
                        - getWave(normalLeft, true).getWidth() - getWave(loudLeft, true).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(loudRight, true), x + getMicIcon().getWidth() / 2 + getWave(lowRight, true).getWidth()
                        + getWave(normalRight, true).getWidth() + getWave(loudRight, true).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                break;

            }
            default:
            {
                graphics.drawImage(getWave(lowLeft, false), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, false).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(lowRight, false), x + getMicIcon().getWidth() / 2 + getWave(lowRight, false).getWidth() / 2, waveY,
                    AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(normalLeft, false), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, false).getWidth()
                        - getWave(normalLeft, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(normalRight, false), x + getMicIcon().getWidth() / 2 + getWave(lowRight, false).getWidth()
                        + getWave(normalRight, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);

                graphics.drawImage(getWave(loudLeft, false), x - getMicIcon().getWidth() / 2 - getWave(lowLeft, false).getWidth()
                        - getWave(normalLeft, false).getWidth() - getWave(loudLeft, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                graphics.drawImage(getWave(loudRight, false), x + getMicIcon().getWidth() / 2 + getWave(lowRight, false).getWidth()
                        + getWave(normalRight, false).getWidth() + getWave(loudRight, false).getWidth() / 2, waveY, AbstractTnGraphics.HCENTER
                        | AbstractTnGraphics.VCENTER);
                break;
            }
        }
    }
    
    private void setVolumeLevel(int volume)
    {
        boolean isMicOn = false;
        int currentVolumeLevel;
        if (volume < 0)
        {
            isMicOn = false;
            currentVolumeLevel = 0;
        }
        else if (volume < 20)
        {
            isMicOn = true;
            currentVolumeLevel = 0;

        }
        else if (volume < 40)
        {
            isMicOn = true;
            currentVolumeLevel = 1;
        }
        else if (volume < 60)
        {
            isMicOn = true;
            currentVolumeLevel = 2;
        }
        else
        {
            isMicOn = true;
            currentVolumeLevel = 3;
        }

        if (isMicOn != lastMicStatus || currentVolumeLevel != lastVolumeLevel)
        {
            lastVolumeLevel = currentVolumeLevel;
            lastMicStatus = isMicOn;
            this.requestPaint();
        }
    }

    public void volumeChanged(int volume)
    {
        setVolumeLevel(volume);
    }

}
