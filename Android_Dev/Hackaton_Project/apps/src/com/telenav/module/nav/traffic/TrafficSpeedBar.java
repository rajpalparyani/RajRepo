/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TrafficSpeedBar.java
 *
 */
package com.telenav.module.nav.traffic;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.datatypes.traffic.TrafficSegment;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 * 
 * Traffic speed bar in traffic detail screen.
 * 
 *@author zhdong@telenav.cn
 *@date 2011-1-11
 */
public class TrafficSpeedBar extends AbstractTnComponent
{
    int gap = 2;
    int padding = 20;
    TrafficSegment seg;
    AbstractTnFont font;
    AbstractTnFont fontPlain;
    AbstractTnImage trafficSlider;

    public TrafficSpeedBar(int id, TrafficSegment seg)
    {
        super(id);
        this.seg = seg;
        this.font = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_AVERAGE_SPEED);
        this.fontPlain = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TRAFFIC_UPDATE_TITLE);
        trafficSlider = ImageDecorator.IMG_TRAFFIC_SLIDER.getImage();

        this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {

                int height = font.getHeight() * 2 + gap + trafficSlider.getHeight() + gap;

                return PrimitiveTypeCache.valueOf(height);
            }
        }));

    }

    protected void paint(AbstractTnGraphics g)
    {
        AbstractTnFont oldFont = g.getFont();
        int oldColor = g.getColor();
        
        AbstractTnImage trafficBar = null;
        String zeroSpeed = null;
        String postedSpeed = null;
        AbstractTnImage trendImage = null;

        StringConverter converter = ResourceManager.getInstance().getStringConverter();

        int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);

        String avgSpeed = converter.convertSpeed(seg.getAvgSpeed(), systemUnits);
        String avgSpeedLabel = "Avg: " + avgSpeed;
        int orientation = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation();
        if (seg.getAvgSpeed() >= 0 && seg.getPostedSpeed() > 0)
        {
            if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            {
                trafficBar = ImageDecorator.IMG_TRAFFIC_COLOR_BAR.getImage();
            }
            else
            {
                trafficBar = ImageDecorator.IMG_TRAFFIC_COLOR_BAR_LANDSCAPE.getImage();
            }
            zeroSpeed = converter.convertSpeed(0, systemUnits);
            postedSpeed = converter.convertSpeed(seg.getPostedSpeed(), systemUnits);
        }
        else
        {
            if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
            {
                trafficBar = ImageDecorator.IMG_TRAFFIC_GRAY_BAR.getImage();
            }
            else
            {
                trafficBar = ImageDecorator.IMG_TRAFFIC_GRAY_BAR_LANDSCAPE.getImage();
            }
        }
        //This is for stretching the image again because the size of the image is not right after the first stretch.
        if (this.getWidth() - trafficBar.getWidth() < padding)
        {
            trafficBar = trafficBar.createScaledImage(this.getWidth() - padding, trafficBar.getHeight());
        }
        
        if (seg.getJamTrend() != 0)
        {
            if (seg.getJamTrend() > 0)
            {
                // Getting worse, put arrow at the left of the slider
                trendImage = ImageDecorator.IMG_TRAFFIC_TREND_DOWN.getImage();
            }
            else
            {
                // Getting better, put arrow at the right of the slider
                trendImage = ImageDecorator.IMG_TRAFFIC_TREND_UP.getImage();
            }
        }
        
        int translateX = (this.getWidth() - trafficBar.getWidth()) >> 1;
        
        g.translate(translateX, 0);
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        
        g.setFont(fontPlain);
        if (zeroSpeed != null)
        {
            g.drawString(zeroSpeed, 0, 0, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }
        if (postedSpeed != null)
        {
            g.drawString(postedSpeed, trafficBar.getWidth(), 0, AbstractTnGraphics.RIGHT | AbstractTnGraphics.TOP);
        }
        g.setFont(font);    
        g.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_DA_GR));
        int sliderX = (trafficBar.getWidth() - trafficSlider.getWidth()) >> 1;
        if( seg.getAvgSpeed() >= 0 && seg.getPostedSpeed() > 0 )
            sliderX = (trafficBar.getWidth() - trafficSlider.getWidth()) * seg.getAvgSpeed() / seg.getPostedSpeed();
        sliderX = (sliderX + trafficSlider.getWidth()) > trafficBar.getWidth() ? (trafficBar.getWidth() - trafficSlider.getWidth()) : sliderX;
        int sliderY = font.getHeight() + gap;
        int trafficBarX = 0;
        int trafficBarY = sliderY + (trafficSlider.getHeight() - trafficBar.getHeight()) / 2;
        
        g.drawImage(trafficBar, trafficBarX, trafficBarY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        g.drawImage(trafficSlider, sliderX, sliderY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        
        if(trendImage != null)
        {
            int trendX = sliderX  + trafficSlider.getWidth() + gap;
            if( seg.getJamTrend() > 0)
                trendX = sliderX - trendImage.getWidth() - gap;
            int trendY = (trafficBar.getHeight() - trendImage.getHeight()) / 2;
            trendY = trendY < 0 ? 0 : trendY;
            trendY += trafficBarY;
            g.drawImage(trendImage, trendX, trendY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        }

        int avgSpeedLabelX = sliderX - trafficSlider.getWidth();

        int maxAvgSpeedLabelX = trafficBar.getWidth() - font.stringWidth(avgSpeedLabel);

        if (avgSpeedLabelX > maxAvgSpeedLabelX)
        {
            avgSpeedLabelX = maxAvgSpeedLabelX;
        }
        if (avgSpeedLabelX < 0)
        {
        	avgSpeedLabelX = 0;
        }

        int avgSpeedLabelY = font.getHeight() + gap + trafficSlider.getHeight() + gap;
        g.drawString(avgSpeedLabel, avgSpeedLabelX, avgSpeedLabelY, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);

        g.translate(-translateX, 0);
        
        g.setFont(oldFont);
        g.setColor(oldColor);
    }

}
