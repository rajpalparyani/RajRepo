/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seUiHelper.java
 *
 */
package com.telenav.tnui.core.j2se;

import java.io.ByteArrayOutputStream;
import javax.swing.SwingUtilities;

import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 3, 2010
 */
public class J2seUiHelper extends AbstractTnUiHelper
{
    protected J2seRootPaneContainer rootPaneContainer;
    
    public J2seUiHelper()
    {
        
    }
    
    public void init(Object context)
    {
        this.rootPaneContainer = (J2seRootPaneContainer)context;
    }
    
    public TnScreen getCurrentScreen()
    {
        return this.rootPaneContainer.currentScreen;
    }

    public float getDensity()
    {
        return 0;
    }

    public int getDisplayHeight()
    {
        return this.rootPaneContainer.getRootPane().getHeight();
    }

    public int getDisplayWidth()
    {
        return this.rootPaneContainer.getRootPane().getWidth();
    }

    public int getOrientation()
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

    public void runOnUiThread(Runnable runnable)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            runnable.run();
        }
        else
        {
            SwingUtilities.invokeLater(runnable);
        }
    }

    public void showScreen(TnScreen screen)
    {
        this.rootPaneContainer.showScreen(screen);
    }

    public AbstractTnFont createFont(int family, int style, int size)
    {
        return new J2seFont(this.rootPaneContainer.getRootPane().getGraphics(), family, style, size);
    }

    public AbstractTnImage createImage(int width, int height)
    {
        return new J2seImage(width, height);
    }

    public AbstractTnImage createImage(Object nativeImage)
    {
        return new J2seImage(nativeImage);
    }
    
    public AbstractTnImage createImage(byte[] data)
    {
        return new J2seImage(data);
    }

    public AbstractTnImage createImage(int[] pixels, int width, int height)
    {
        return new J2seImage(pixels, width, height);
    }

    public AbstractTnImage createImage(AbstractTnImage image, int x, int y, int width, int height)
    {
        return new J2seImage(image, x, y, width, height);
    }

    public int getStatusBarHeight(int position)
    {
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
