/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * WeatherComponent.java
 *
 */
package com.telenav.module.dashboard;

import com.telenav.res.IStringDashboard;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.frogui.widget.FrogButton;

/**
 *@author qli
 *@date 2012-1-19
 */
class WeatherComponent extends FrogButton
{
    protected int iconX = 0;
    protected int iconY = 0;
    protected String temp = null;
    protected String tempCode = null;
    protected String weatherIconFocusedFile = "dashboard_weather_icon_$_focused.png";
    protected String weatherIconUnfocusedFile = "dashboard_weather_icon_$_unfocused.png";
    private int rectWidth;
    private int rectHeight;
    
    public WeatherComponent(int id, String text)
    {
        super(id, text);
        this.gap = - font.getMaxWidth() * 2 / 3; // gap between temperature and icon, negative denote they are overlapped.
    }
    
    public void setTemperature(String temp)
    {
        if (temp.equals(this.temp))
        {
            return;
        }
        this.temp = temp;
    }
    
    public String getTemperature()
    {
        return this.temp;
    }
    
    public int getTempMaxWidth()
    {
        String degreeSymbol = ResourceManager.getInstance().getCurrentBundle().getString(IStringDashboard.RES_STRING_DEGREE_SYMBOL, IStringDashboard.FAMILY_DASHBOARD);
        return this.font.stringWidth("-888" + degreeSymbol);
    }
    
    public void setWeatherIconImage(String code)
    {
        if (code.equals(tempCode))
        {
            return;
        }
        tempCode = code;
        
        String weatherIconFocusedFileName = weatherIconFocusedFile;
        weatherIconFocusedFileName = weatherIconFocusedFileName.replace("$", tempCode);
        
        String weatherIconUnfocusedFileName = weatherIconUnfocusedFile;
        weatherIconUnfocusedFileName = weatherIconUnfocusedFileName.replace("$", tempCode);
        
        iconFocus = new TnUiArgAdapter(weatherIconFocusedFileName, ImageDecorator.instance).getImage();
        iconUnfocus = new TnUiArgAdapter(weatherIconUnfocusedFileName, ImageDecorator.instance).getImage();
        
    }
    
    public String getWeatherIconImage()
    {
        return tempCode;
    }
    
    public int[] getWeatherIconWidthAndHeight()
    {
        int[] widthAndHeight = new int[2];
        if (iconFocus != null)
        {
            widthAndHeight[0] = iconFocus.getWidth();
            widthAndHeight[1] = iconFocus.getHeight();
        }
        else
        {
            AbstractTnImage image = new TnUiArgAdapter("dashboard_weather_icon_1_focused.png", ImageDecorator.instance).getImage();
            widthAndHeight[0] = image.getWidth();
            widthAndHeight[1] = image.getHeight();
        }
        
        return widthAndHeight;
    }
    
    public int getWeatherIconHeight()
    {
        if (iconFocus != null)
        {
            return iconFocus.getHeight();
        }
        return 0;
    }
    
    public void setIcon(AbstractTnImage iconFocus, AbstractTnImage iconBlur, int style)
    {
        //do nothing
    }
    
    /**
     * layout the component by default
     */
    public void sublayout(int width, int height)
    {
        preferWidth = getComponentWidth();
    }
    
    public int getComponentWidth()
    {
        int tempWidth =  (temp != null && temp.length() > 0) ? this.font.stringWidth(temp) : this.getTempMaxWidth();
        int[] weatherIconWidthAndHeight = this.getWeatherIconWidthAndHeight();
        return tempWidth + weatherIconWidthAndHeight[0] + gap;
    }
    
    
    public void paint(AbstractTnGraphics graphics)
    {
        paintIcon(graphics);
        paintText(graphics);
    }
    
    protected void paintText(AbstractTnGraphics graphics)
    {
        graphics.setColor(getForegroundColor(isFocused()));
        graphics.setFont(font);
        iconX -= gap;
   //     iconY -= font.getHeight() / 2;
        graphics.drawString(temp, iconX, iconY, AbstractTnGraphics.RIGHT | AbstractTnGraphics.TOP);
    }
    
    protected void paintIcon(AbstractTnGraphics graphics)
    {
        AbstractTnImage weatherIcon = getWeatherIcon();
        if (weatherIcon == null)
        {
            return;
        }
        iconX = this.getPreferredWidth();
        iconY = this.getPreferredHeight() - font.getHeight() / 4;
        if (rectWidth > 0 && rectHeight > 0)
        {
            graphics.drawImage(weatherIcon, null, new TnRect(iconX - rectWidth, iconY - rectHeight, iconX, iconY));
            iconX -= rectWidth;
            iconY -= rectHeight;
        }
        else
        {
            graphics.drawImage(weatherIcon, iconX, iconY, AbstractTnGraphics.RIGHT | AbstractTnGraphics.BOTTOM);
            iconX -= weatherIcon.getWidth();
            iconY -= weatherIcon.getHeight();
        }
      
    }
    
    public void setRect(int width, int height)
    {
        this.rectWidth =width;
        this.rectHeight=height;
    }

    protected AbstractTnImage getWeatherIcon()
    {
        AbstractTnImage weatherIcon = null;
        if ( isFocused() )
        {
            weatherIcon = iconFocus;
        }
        else
        {
            weatherIcon = iconUnfocus;
        }
        
        return weatherIcon;
    }
}