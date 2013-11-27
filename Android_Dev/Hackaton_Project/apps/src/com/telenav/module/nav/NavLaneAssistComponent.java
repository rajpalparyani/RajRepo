/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavLaneInfoContainer.java
 *
 */
package com.telenav.module.nav;

import java.util.Vector;

import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;

/**
 * @deprecated combined with CurrentStreetNameComponent
 * 
 *@author yning
 *@date 2011-1-5
 */
public class NavLaneAssistComponent extends AbstractTnComponent implements INotifierListener
{
    int[] laneInfos = null;

    int[] laneTypes = null;

    public final static int LANE_TYPE_CONTINUE = 0;

    public final static int LANE_TYPE_CONTINUE_LEFT = 1;

    public final static int LANE_TYPE_CONTINUE_RIGHT = 2;

    public final static int LANE_TYPE_TURN_LEFT = 3;

    public final static int LANE_TYPE_TURN_RIGHT = 4;

    public final static int LANE_TYPE_UTURN_LEFT = 5;

    public final static int LANE_TYPE_UTURN_RIGHT = 6;
    
    static int ANIMATION_STEPS = 10;
    
    public NavLaneAssistComponent(int id)
    {
        super(id);
    }

    protected void paint(AbstractTnGraphics graphics)
    {
        NavBottomStatusBarHelper.getInstance().drawBackground(graphics, this, true);
        
        if (laneInfos != null && laneTypes != null)
        {
            int width = this.getWidth();
            int height = this.getHeight();
            int imageHeight = ImageDecorator.IMG_LANE_ASSIST_AHEAD_FOCUSED.getImage().getHeight();
            int imageWidth = ImageDecorator.IMG_LANE_ASSIST_AHEAD_FOCUSED.getImage().getWidth();            
            int imageX = (width - imageWidth * laneInfos.length) / 2;
            int imageY = (height - imageHeight) / 2;

            imageY += timerStep * height / ANIMATION_STEPS;
            
            for (int i = 0; i < laneInfos.length; i++)
            {
                AbstractTnImage turnImage = null;
                if (laneTypes[i] == LANE_TYPE_TURN_LEFT || laneTypes[i] == LANE_TYPE_UTURN_LEFT)
                {
                    if (laneInfos[i] == 1)
                    {
                        turnImage = ImageDecorator.IMG_LANE_ASSIST_LEFT_TURN_FOCUSED.getImage();
                    }
                    else
                    {
                        turnImage = ImageDecorator.IMG_LANE_ASSIST_LEFT_TURN_UNFOCUSED.getImage();
                    }
                }
                else if (laneTypes[i] == LANE_TYPE_TURN_RIGHT || laneTypes[i] == LANE_TYPE_UTURN_RIGHT)
                {
                    if (laneInfos[i] == 1)
                    {
                        turnImage = ImageDecorator.IMG_LANE_ASSIST_RIGHT_TURN_FOCUSED.getImage();
                    }
                    else
                    {
                        turnImage = ImageDecorator.IMG_LANE_ASSIST_RIGHT_TURN_UNFOCUSED.getImage();
                    }
                }
                else if (laneTypes[i] == LANE_TYPE_CONTINUE || laneTypes[i] == LANE_TYPE_CONTINUE_LEFT
                        || laneTypes[i] == LANE_TYPE_CONTINUE_RIGHT)
                {
                    if (laneInfos[i] == 1)
                    {
                        turnImage = ImageDecorator.IMG_LANE_ASSIST_AHEAD_FOCUSED.getImage();
                    }
                    else
                    {
                        turnImage = ImageDecorator.IMG_LANE_ASSIST_AHEAD_UNFOCUSED.getImage();
                    }
                }

                if (turnImage != null)
                {
                    graphics.drawImage(turnImage, imageX, imageY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                    imageX += turnImage.getWidth();
                }
            }
        }
    }

    boolean isDisappearing;

    public void update(int[] laneInfos, int[] laneTypes)
    {
        if (laneInfos != null && laneTypes != null)
        {
            this.laneInfos = laneInfos;
            this.laneTypes = laneTypes;
            
            if (!this.isVisible())
            {
                timerStep = ANIMATION_STEPS;
                isDisappearing = false;
                setVisible(true);
                Vector listeners = Notifier.getInstance().getAllListeners();
                for (int i = listeners.size() - 1; i > 0; i--)
                {
                    Object listener = listeners.elementAt(i);
                    if (listener instanceof NavLaneAssistComponent)
                    {
                        listeners.removeElement(listener);
                    }
                }
                Notifier.getInstance().addListener(this);
            }
        }
        else
        {
            isDisappearing = true;
        }
        
        requestPaint();
    }

    /**
     * From ANIMATION_STEPS to 0
     */
    int timerStep = ANIMATION_STEPS;
   
    long lastNotifyTimestamp;

    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    public long getNotifyInterval()
    {
        if (timerStep <= 0)
        {
            // The popup animation is stopped, we don't have to notify too frequent.
            return 1000;
        }
        return 100;
    }

    public void notify(long timestamp)
    {
        if (isDisappearing)
        {
            if (timerStep >= ANIMATION_STEPS)
            {
                Notifier.getInstance().removeListener(this);
                this.laneInfos = null;
                this.laneTypes = null;
                this.setVisible(false);
                return;
            }
            timerStep++;
        }
        else
        {
            if (timerStep <= 0)
            {
                return;
            }
            timerStep--;
        }
        requestPaint();
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestamp = timestamp;
    }
    
    protected void onUndisplay()
    {
        Notifier.getInstance().removeListener(this);
        super.onUndisplay();
    }
    
}
