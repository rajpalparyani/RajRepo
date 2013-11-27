/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidGLRenderer.java
 *
 */
package com.telenav.tnui.widget.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;

import com.telenav.tnui.opengles.TnGLRenderer;

/**
 * @author fqming (fqming@telenav.cn)
 * @date Sep 7, 2010
 */
public class AndroidGLRenderer implements Renderer
{
    private TnGLRenderer renderer;

    private AndroidGL10 androidGL10;
    
    private boolean isScreenshot = false;
    
    private SnapShotHandler handler;
    
    AndroidGLRenderer(TnGLRenderer renderer)
    {
        this.renderer = renderer;
    }
    
    public void requestScreenShot(int x, int y, int width, int height, IGLSnapBitmapCallBack callback, long transactionId)
    {
        isScreenshot = true;
        handler = new SnapShotHandler(x, y, width, height, callback, transactionId);
    }

    public void onDrawFrame(GL10 gl)
    {
        this.renderer.render(androidGL10);
        
        if (isScreenshot)
        {
            if(handler != null)
            {
                handler.doSnapShot(gl);
            }
            
            handler = null;
            isScreenshot = false;
        }
    }
    
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        this.renderer.onSurfaceChanged(this.androidGL10, width, height);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        if (gl instanceof GL11)
        {
            if (Integer.parseInt(Build.VERSION.SDK) >= 4)
            {
                this.androidGL10 = new AndroidGLES11();
            }
            else
            {
                this.androidGL10 = new AndroidGL11();
            }
        }
        else
        {
            if (Integer.parseInt(Build.VERSION.SDK) >= 4)
            {
                this.androidGL10 = new AndroidGLES10();
            }
            else
            {
                this.androidGL10 = new AndroidGL10();
            }
        }

        this.androidGL10.setGL(gl);
        this.renderer.onSurfaceCreated(this.androidGL10);
        this.renderer.setNativeRenderer(this);
    }

}
