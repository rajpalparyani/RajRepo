/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidUiHelper.java
 *
 */
package com.telenav.tnui.core.android;

import java.io.ByteArrayOutputStream;
import java.nio.ShortBuffer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;

/**
 * This class provider some common utility functions of ui relative functions.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class AndroidUiHelper extends AbstractTnUiHelper
{
    protected Context context;
    
    protected WindowManager windowManager;
    
    public final static int MEDIUM_WIDTH = 320;
    public final static int MEDIUM_HEIGHT = 480;
    
    float visionRatio = 0;
    float stretchRatio = 0;
    
    private float defaultScaledDensity;
    
    protected float getVisionRatio(int width)
    {
        if (visionRatio == 0)
        {
            int w1 = width;
            
            int w2 = MEDIUM_WIDTH;
            
            visionRatio = 1.0f * w1 / w2;
        }
        return visionRatio;
    }
    
    protected float getStretchRatio(int width, int height)
    {
        if (stretchRatio == 0)
        {
            int w1 = width;
            int h1 = height;
            
            int w2 = MEDIUM_WIDTH;
            int h2 = MEDIUM_HEIGHT;
            
            float numerator = w1 * h2 + h1 * w2;
            float denominator = 2 * w2 * h2;
            return numerator / denominator;
        }
        
        return stretchRatio;
    }
    
    public float getVisionRatio()
    {
        float ratio1 = getVisionRatio(Math.min(getDisplayHeight(), getDisplayWidth()));
        float ratio2 = getStretchRatio(Math.min(getDisplayHeight(), getDisplayWidth()), 
            Math.max(getDisplayHeight(), getDisplayWidth()));
        return ratio1 > ratio2 ? ratio2 : ratio1;
    }
    
    public AndroidUiHelper()
    {

    }

    public void init(Object context)
    {
        this.context = (Context)context;
        
        this.windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        
        defaultScaledDensity = this.context.getResources().getDisplayMetrics().scaledDensity;
    }
    
    public void setFontDensity(boolean isDefault)
    {
        //Scale the font size using our PUSU algorithm, firstly, calculate the exact size if in 320X480 size, then plus the
        //ratio of current screen size and 320X480
        if (isDefault)
        {
            this.context.getResources().getDisplayMetrics().scaledDensity = defaultScaledDensity;
        }
        else
        {
            this.context.getResources().getDisplayMetrics().scaledDensity = getVisionRatio() * 2 * MEDIUM_WIDTH/720;
        }
    }

    public AbstractTnFont createFont(int family, int style, int size)
    {
        return new AndroidFont(context, family, style, size);
    }
    
    public AbstractTnImage createImage(Object nativeBitmap)
    {
        return new AndroidImage(nativeBitmap);
    }
    
    public AbstractTnImage createImage(int width, int height)
    {
        return new AndroidImage(width, height);
    }

    public AbstractTnImage createImage(byte[] data)
    {
        return new AndroidImage(data);
    }

    public AbstractTnImage createImage(int[] pixels, int width, int height)
    {
        return new AndroidImage(pixels, width, height);
    }
    
    public AbstractTnImage createImage(AbstractTnImage image, int x, int y, int width, int height)
    {
        return new AndroidImage(image, x, y, width, height);
    }

    public int getOrientation()
    {
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            return ORIENTATION_LANDSCAPE;
        }
        else
        {
            return ORIENTATION_PORTRAIT;
        }
    }
    
    public int getDisplayWidth()
    {
        return this.windowManager.getDefaultDisplay().getWidth();
    }
    
    public int getDisplayHeight()
    {
        return this.windowManager.getDefaultDisplay().getHeight();
    }

    public void runOnUiThread(Runnable runnable)
    {
        if(this.context instanceof Activity)
        {
            Activity activity = (Activity)this.context;
            activity.runOnUiThread(runnable);
        }
    }
    
    public void showScreen(TnScreen screen)
    {
        if(this.context instanceof IAndroidBaseActivity)
        {
            IAndroidBaseActivity activity = (IAndroidBaseActivity)this.context;
            
            activity.showScreen(screen);
        }
    }
    
    public TnScreen getCurrentScreen()
    {
        if(this.context instanceof IAndroidBaseActivity)
        {
            IAndroidBaseActivity activity = (IAndroidBaseActivity)this.context;
            
            return activity.getCurrentScreen();
        }
        
        return null;
    }

    public float getDensity()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        this.windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    public int getStatusBarHeight(int position)
    {
        if(this.context instanceof IAndroidBaseActivity)
        {
            IAndroidBaseActivity activity = (IAndroidBaseActivity)this.context;
            
            return activity.getStatusBarHeight(position);
        }
        
        return 0;
    }

    public float getXdpi()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        this.windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.xdpi;
    }

    public float getYdpi()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        this.windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.ydpi;
    }

    public void setRequestedOrientation(int requestedOrientation)
    {
        if(this.context instanceof Activity)
        {
            switch(requestedOrientation)
            {
                case ORIENTATION_UNSPECIFIED:
                {
                    ((Activity)this.context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    break;
                }
                case ORIENTATION_LANDSCAPE:
                {
                    ((Activity)this.context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                }
                case ORIENTATION_PORTRAIT:
                {
                    ((Activity)this.context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                }
            }
        }
    }
    
    public String base64EncodingToString(byte[] input, int flg)
    {
        String result = Base64.encodeToString(input, flg);
        return result;
    }

    public void compressImage(AbstractTnImage tnImage, int format, int quality, ByteArrayOutputStream bstream)
    {
        if (tnImage == null || tnImage.getNativeImage() == null)
            return;

        if (format != IMAGE_FORMAT_JPEG && format != IMAGE_FORMAT_PNG)
        {
            throw new IllegalArgumentException("Should be JPEG or PNG");
        }

        CompressFormat cFormat = null;
        switch (format)
        {
            case IMAGE_FORMAT_PNG:
            {
                cFormat = CompressFormat.PNG;
                break;
            }
            case IMAGE_FORMAT_JPEG:
            {
                cFormat = CompressFormat.JPEG;
                break;
            }
        }
        
        ((Bitmap) tnImage.getNativeImage()).compress(cFormat, quality, bstream);
    }

	public void closeContextMenu()
	{
		if(this.context instanceof Activity)
        {
            Activity activity = (Activity)this.context;
            
            activity.closeContextMenu();
        }
	}

	public void closeOptionsMenu()
	{
		if(this.context instanceof Activity)
        {
            Activity activity = (Activity)this.context;
            
            activity.closeOptionsMenu();
        }
	}
	
	public  StateListDrawable createStateListDrawable(int idNormal,
            int idPressed, int idFocused, int disabled)
    {
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(
            idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(
            idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(
            idFocused);
        Drawable unable = disabled == -1 ? null : context.getResources().getDrawable(
            disabled);
        
        return createStateListDrawable(normal,
            pressed, focused, unable);
    }
	
	public StateListDrawable createStateListDrawable(Drawable normal,
	        Drawable pressed, Drawable focused, Drawable disabled)
	{
	    StateListDrawable bg = new StateListDrawable();

        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
       
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);
        
        // View.ENABLED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_enabled }, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_focused }, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_window_focused }, disabled);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]
                            {}, normal);
        return bg;
	}
	
	public StateListDrawable createStateListDrawable(Drawable normal,
	        Drawable pressed, Drawable focused, Drawable disabled, Drawable selected)
	{
        StateListDrawable bg = new StateListDrawable();

        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        
        // View.SELECTED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_selected, android.R.attr.state_enabled }, selected);
       
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);
        
        // View.ENABLED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_enabled }, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_focused }, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]
                            { android.R.attr.state_window_focused }, disabled);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]
                            {}, normal);
        return bg;
	}
	
	public Bitmap createImage(Bitmap bitmap, int width, int height, int[] buffer)
    {
        if (width * height != buffer.length)
        {
            return null;
        }
        
        bitmap.setPixels(buffer, buffer.length - width, -width, 0, 0, width, height);

        short sBuffer[] = new short[buffer.length];
        ShortBuffer sb = ShortBuffer.wrap(sBuffer);
        bitmap.copyPixelsToBuffer(sb);

        // Making created bitmap (from OpenGL points) compatible with Android bitmap
        for (int i = 0; i < buffer.length; ++i)
        {
            short v = sBuffer[i];
            sBuffer[i] = (short) (((v & 0x1f) << 11) | (v & 0x7e0) | ((v & 0xf800) >> 11));
        }
        sb.rewind();
        bitmap.copyPixelsFromBuffer(sb);

        boolean isBlack = checkBitmap(bitmap);
        if (isBlack)
        {
            return null;
        }
        else
        {
            return bitmap;
        }
    }
    
    private boolean checkBitmap(Bitmap bitmap)
    {
        if (bitmap != null)
        {
            int color_left_top = bitmap.getPixel(0, 0);
            int color_right_top = bitmap.getPixel(bitmap.getWidth() - 1, 0);
            int color_left_bottom = bitmap.getPixel(0, bitmap.getHeight() - 1);
            int color_right_bottom = bitmap.getPixel(bitmap.getWidth() - 1, bitmap.getHeight() - 1);

            if (color_left_top == TnColor.BLACK && color_left_bottom == TnColor.BLACK && color_right_top == TnColor.BLACK
                    && color_right_bottom == TnColor.BLACK)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> got color all black");
                }
                return true;
            }
        }
        return false;
    }
}
