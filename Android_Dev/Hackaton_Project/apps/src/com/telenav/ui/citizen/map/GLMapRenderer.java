/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnJNIGLRenderer.java
 *
 */
package com.telenav.ui.citizen.map;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.telenav.logger.Logger;
import com.telenav.navsdk.NavigationSDK;
import com.telenav.tnui.opengles.TnGL10;
import com.telenav.tnui.opengles.TnGLRenderer;

/**
 *@author jyxu(jyxu@telenav.cn)
 *@date 2010-11-10
 */
public class GLMapRenderer implements TnGLRenderer
{
    public static final int MAX_FPS = 30;
    public static final int MIN_FPS = 7;
    
    private int pauseWaitCounter;
    
    private long viewId;

    private long confId = -1;
    
//    private long currentTime;
    
    private long minIntervalPerFrame;
    
    private long lastFinishTime;
    
    private boolean isPaused = true;

    private boolean hasNewConfig = false;
    
    private boolean isStarted = false;
    
    private boolean isViewManuallyInitialized = false;
    
    private IMapEGLEventListener listener;
    
    private Object renderer;
    
    private int surfaceWidth = 0;
    
    private int surfaceHeight = 0;
    
//    TnUiArgAdapter mapX;
//    TnUiArgAdapter mapY;
//    TnUiArgAdapter mapWidth;
//    TnUiArgAdapter mapHeight;
    
    private static short build_interval = 100;
    
    NavigationSDK navSdk;
    
    public GLMapRenderer()
    {
        setMaxFps(MIN_FPS);
    }
    
    public void init(String esFileName)
    {
        if(navSdk!=null)
        {
            return;
        }
        navSdk = NavigationSDK.getInstance(null);
        navSdk.InitMapEngine(esFileName);

        navSdk.StartBuildingAtInterval(build_interval);
    }

    public long getViewId()
    {
        return viewId;
    }
    
    public long getConfId()
    {
        return confId;
    }

    public void setMapEGLEventListener(IMapEGLEventListener listener)
    {
        this.listener = listener;
    }
    
    public void removeMapEGLEventListener()
    {
        this.listener = null;
    }

    public void setViewId(long viewId)
    {
        this.viewId = viewId;
    }
    
    public void onSurfaceChanged(TnGL10 gl, final int width, final int height)
    {
        this.surfaceWidth = width;
        this.surfaceHeight = height;
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "OGLMapService-GetSnapshot onSurfaceChanged() width : " + width
                    + " height : " + height);
        }
        
        if (isPaused)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "OGLMapService-GetSnapshot onSurfaceChanged() map is paused!!!");
            }
            return;
        }
        
        if (viewId > 0)
        {
            updateView(width, height);
        }
        else
        {
            if (isViewManuallyInitialized)
            {
                createGlNativeView();
            }
        }
        if (listener != null)
        {
            listener.eglSizeChanged();
        }
    }
    
    public boolean isViewManuallyInitialized()
    {
        return this.isViewManuallyInitialized;
    }
    
    public void setViewManuallyInitialized(boolean isManuallyInitialized)
    {
        this.isViewManuallyInitialized = isManuallyInitialized;
    }

    public void onSurfaceCreated(TnGL10 gl)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "onSurfaceCreated()");
    }
    
    protected void updateView(final int width, final int height)
    {
        if (MapContainer.getInstance().isPaused())
        {
            return;
        }

        MapContainer.getInstance().updateMapView(width, height);
        
    }
    
    protected void createGlNativeView()
    {
        if (viewId == 0)
        {
            if (navSdk != null)
            {
                navSdk.StartBuildingAtInterval(build_interval);
            }
            MapContainer.getInstance().openMapView();
            MapContainer.getInstance().changeToSpriteVehicleAnnotation(); // first time set blue dot
            MapContainer.getInstance().enableRadius(false);
            if(isViewManuallyInitialized)
            {
                MapContainer.getInstance().resetMap();
            }
        }
    }

    public void pause()
    {
        isPaused = true;
    }
    
    public void resume()
    {
        isStarted = true;
        isPaused = false;
        pauseWaitCounter = 0;
        lastFinishTime = 0;
    }

    public void render(TnGL10 gl)
    {
        try
        {
            if (navSdk == null || !isStarted)
            {
                return;
            }

            if (isPaused)
            {
                // fix bug http://jira.telenav.com:8080/browse/TN-2041
                // Root cause is because the map is paused before animation finishes.
                // so add a counter to paint map several seconds longer to finish the animation
                if (pauseWaitCounter > 50)
                {
                    waitForVSync();
                    return;
                }
                pauseWaitCounter++;
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        
        try
        {
            waitForVSync();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        
        if (hasNewConfig)
        {
            updateMapParameters();
            hasNewConfig = false;
        }

        if (navSdk != null)
        {
            gl.glDisable(GL10.GL_SCISSOR_TEST);
            gl.glClearColor(0.94f, 0.94f, 0.94f, 1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glEnable(GL10.GL_SCISSOR_TEST);
            navSdk.Render();
        }
    }
    
    private void waitForVSync() throws Throwable
    {
        long finishTime = System.currentTimeMillis();
        long delta = finishTime - lastFinishTime;
        if (delta > 0)
        {
            long sleep = minIntervalPerFrame - delta;
            if (sleep > 0)
            {
                Thread.sleep(sleep);
            }
        }
        lastFinishTime = finishTime;
    }
    
    public long getIdleTime()
    {
        if (navSdk == null || !isStarted || isPaused || lastFinishTime <= 0)
        {
            return 0;
        }
        return System.currentTimeMillis() - lastFinishTime;
    }
    
    public void debugCrash()
    {
        if (navSdk != null)
        {
            Log.d("Weichao DebugCrash", "DebugCrash");
            navSdk.DebugCrash();
        }
        else
        {
            Log.d("Weichao DebugCrash", "navSdk is null");
        }
    }
    
    public void updateMapParameters()
    {

    }
    
    public void setMaxFps(int fps)
    {
        minIntervalPerFrame = 1000/fps;
    }
    
    public boolean isMapReady()
    {
        return isViewManuallyInitialized;
    }
    
    public void setNativeRenderer(Object renderer)
    {
        this.renderer = renderer;
    }

    public Object getNativeRenderer()
    {
        return this.renderer;
    }
    
    public int getSurfaceWidth()
    {
        return surfaceWidth;
    }

    public void setSurfaceWidth(int surfaceWidth)
    {
        this.surfaceWidth = surfaceWidth;
    }
    
    public int getSurfaceHeight()
    {
        return surfaceHeight;
    }

    public void setSurfaceHeight(int surfaceHeight)
    {
        this.surfaceHeight = surfaceHeight;
    }
}
