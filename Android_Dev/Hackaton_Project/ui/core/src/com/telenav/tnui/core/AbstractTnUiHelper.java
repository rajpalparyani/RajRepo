/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnUiHelper.java
 *
 */
package com.telenav.tnui.core;

import java.io.ByteArrayOutputStream;

import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * This class provider some common utility functions of ui relative functions.
 * <br />
 * Don't write the paint relative codes of ui here.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public abstract class AbstractTnUiHelper extends AbstractTnGraphicsHelper
{
    public final static String DEFAULT_COMMON_BASELINE_ANCHOR = "0123456789ABCDEFGHIKLMNOPRSTUVWXYZ";//No 'J' and 'Q', they have part below the base line for default font.

    protected AbstractTnI18nProvider i18nProvider;
    
    protected String commonBaseLineAnchor;
    
    /**
     * Unspecified mode of display
     */
    public static final int ORIENTATION_UNSPECIFIED     = -1;
    
    /**
     * portrait mode of display.
     */
    public static final int ORIENTATION_PORTRAIT        = 1;
    
    /**
     * landscape mode of display.
     */
    public static final int ORIENTATION_LANDSCAPE       = 2; 
    
    /**
     * status bar at the top
     */
    public static final int STATUS_BAR_TOP = 0;
    
    /**
     * status bar at the bottom
     */
    public static final int STATUS_BAR_BOTTOM = 1;
    
    public static final int BASE64_DEFAULT = 0;
    public static final int BASE64_NO_PADDING = 1;
    public static final int BASE64_NO_WRAP = 2;
    public static final int BASE64_CRLF = 4;
    public static final int BASE64_URL_SAFE = 8;
    public static final int BASE64_NO_CLOSE = 16;
    
    public static final int IMAGE_FORMAT_PNG = 1;
    public static final int IMAGE_FORMAT_JPEG = 2;
    
    /**
     * return the orientation({@link #ORIENTATION_PORTRAIT}, {@link #ORIENTATION_LANDSCAPE}) of display.
     * 
     * @return orientation
     */
    public abstract int getOrientation();
    
    /**
     * return the width of display, in pixels. Note that this should not generally be 
     * used for computing layouts, since a device will typically have screen decoration 
     * (such as a status bar) along the edges of the display that reduce the amount of 
     * application space available from the raw size returned here. This value is adjusted for you 
     * based on the current rotation of the display. 
     * 
     * @return displayWidth
     */
    public abstract int getDisplayWidth();
    
    /**
     * return the height of display, in pixels. Note that this should not generally be 
     * used for computing layouts, since a device will typically have screen decoration 
     * (such as a status bar) along the edges of the display that reduce the amount of 
     * application space available from the raw size returned here. This value is adjusted for you 
     * based on the current rotation of the display. 
     * 
     * @return displayHeight
     */
    public abstract int getDisplayHeight();
    
    /**
     * Runs the specified action on the UI threadIf the current thread is the UI thread, then the action is executed immediately. 
     * If the current thread is not the UI thread, the action is posted to the event queue of the UI thread.
     * 
     * @param runnable
     */
    public abstract void runOnUiThread(Runnable runnable);
    
    /**
     * Show the screen at display.
     * 
     * @param screen {@link TnScreen}
     */
    public abstract void showScreen(TnScreen screen);
    
    /**
     * Retrieve the current screen at display.
     * 
     * @return TnScreen
     */
    public abstract TnScreen getCurrentScreen();
    
    /**
     * Retrieve density of current device.
     * @return float
     */
    public abstract float getDensity();
    
    /**
     * The exact physical pixels per inch of the screen in the X dimension.
     *  
     * @return physical pixels per inch
     */
    public abstract float getXdpi();
    
    /**
     * The exact physical pixels per inch of the screen in the Y dimension. 
     * 
     * @return physical pixels per inch
     */
    public abstract float getYdpi();
    
    /**
     * Retrieve the height of status bar. The status bar contains the time, and Radio signal info etc.
     * @return statusBarHeight
     */
    public abstract int getStatusBarHeight(int position);
    
    /**
     * Retrieve i18n provider.
     * 
     * @return i18n provider
     */
    public final AbstractTnI18nProvider getI18nProvider()
    {
        return i18nProvider;
    }
    
    final void setI18nProvider(AbstractTnI18nProvider provider)
    {
        this.i18nProvider = provider;
    }
    
    public void setCommonBaseLineAnchor(String anchor)
    {
        this.commonBaseLineAnchor = anchor;
    }
    
    public String getCommonBaseLineAnchor()
    {
        if(commonBaseLineAnchor != null && commonBaseLineAnchor.trim().length() > 0)
        {
            return this.commonBaseLineAnchor;
        }
        else
        {
            return DEFAULT_COMMON_BASELINE_ANCHOR;
        }
    }
    
    /**
     * Set the orientation of screen.
     * @param requestedOrientation
     */
    public abstract void setRequestedOrientation(int requestedOrientation);
    
    public abstract String base64EncodingToString(byte[] input , int flg);
    
    public abstract void compressImage(AbstractTnImage tnImage, int format , int quality , ByteArrayOutputStream bstream);

	public abstract void closeContextMenu();

	public abstract void closeOptionsMenu();
}
