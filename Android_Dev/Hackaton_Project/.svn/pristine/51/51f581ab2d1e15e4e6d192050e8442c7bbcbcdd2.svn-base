/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimUiHelper.java
 *
 */
package com.telenav.tnui.core.rim;

import java.io.ByteArrayOutputStream;
import net.rim.device.api.system.Display;

import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * This class provider some common utility functions of ui relative functions.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-10
 */
public class RimUiHelper extends AbstractTnUiHelper
{
    protected RimUiApplication uiApplication;
    
    protected int statusBarHeight;
    
    public RimUiHelper()
    {
    }

    public void init(Object context)
    {
        uiApplication = (RimUiApplication)context;
    }

    public AbstractTnFont createFont(int family, int style, int size)
    {
        return new RimFont(family, style, size);
    }
    
    public AbstractTnImage createImage(Object nativeImage)
    {
        return new RimImage(nativeImage);
    }
    
    public AbstractTnImage createImage(int width, int height)
    {
        return new RimImage(width, height);
    }

    public AbstractTnImage createImage(byte[] data)
    {
        return new RimImage(data);
    }

    public AbstractTnImage createImage(int[] pixels, int width, int height)
    {
        return new RimImage(pixels, width, height);
    }
    
    public AbstractTnImage createImage(AbstractTnImage image, int x, int y, int width, int height)
    {
        return new RimImage(image, x, y, width, height);
    }
    
    public int getOrientation()
    {
        switch (Display.getOrientation())
        {
            case Display.ORIENTATION_LANDSCAPE:
                return ORIENTATION_LANDSCAPE;
            case Display.ORIENTATION_PORTRAIT:
                return ORIENTATION_PORTRAIT;
            default:
            {
                if(this.getDisplayWidth() > this.getDisplayHeight())
                {
                    return ORIENTATION_LANDSCAPE;
                }
                else
                {
                    return ORIENTATION_PORTRAIT;
                }
            }
        }
    }
    
    public int getDisplayWidth()
    {
        return Display.getWidth();
    }
    
    public int getDisplayHeight()
    {
        return Display.getHeight();
    }

    public void runOnUiThread(Runnable runnable)
    {
        uiApplication.invokeLater(runnable);
    }
    
    public void showScreen(TnScreen screen)
    {
        uiApplication.showScreen(screen);
    }
    
    public TnScreen getCurrentScreen()
    {
        return uiApplication.currentScreen;
    }

    public float getDensity()
    {
        return 1.0f;
    }

    public int getStatusBarHeight(int position)
    {
        if(position == STATUS_BAR_TOP)
            return this.statusBarHeight;
        return 0;
    }
    
    public float getXdpi()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public float getYdpi()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public void setRequestedOrientation(int requestedOrientation)
    {
    	//TODO 
    }
    
    public String base64EncodingToString(byte[] input, int flg)
    {
        return "";
    }
    
    public void compressImage(AbstractTnImage tnImage, int format, int quality, ByteArrayOutputStream bstream)
    {
    }

	public void closeContextMenu()
	{
	}

	public void closeOptionsMenu()
	{
	}
}
