/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnGLSurfaceField.java
 *
 */
package com.telenav.tnui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.opengles.TnGLRenderer;

/**
 * An implementation of component that uses the dedicated surface for displaying OpenGL rendering. <br />
 * You must call setRenderer(TnGLRenderer) to register a TnGLRenderer. The renderer is responsible for doing the actual
 * OpenGL rendering.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Sep 3, 2010
 */
public class TnGLSurfaceField extends AbstractTnComponent
{
    /**
     * Constant to indicate to use OpenGL ES 1.0 as the version. 
     */
    public static final int VERSION_1_0 = 10;
    
    /**
     * Constant to indicate to use OpenGL ES 1.1 as the version. 
     */
    public static final int VERSION_1_1 = 11;
    
    /**
     * Constant to indicate to use OpenGL ES 2.0 as the version. 
     */
    public static final int VERSION_2_0 = 20;
    
    /**
     * The renderer only renders when the surface is created, or when requestRender() is called.
     */
    public final static int RENDERMODE_WHEN_DIRTY = 0;

    /**
     * The renderer is called continuously to re-render the scene.
     */
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_SET_RENDERER = 40000001;

    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_SET_RENDER_MODE = 40000002;

    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_REQUEST_RENDER = 40000003;

    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_POST_GL_EVENT = 40000004;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_SETBACKGROUND_COLOR = 40000005;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_ON_PAUSE = 40000006;

    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_ON_RESUME = 40000007;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_SET_OGLES_CLIENT_VERSION = 40000008;    
    
    protected TnGLRenderer glRenderer;

    protected int renderMode = RENDERMODE_CONTINUOUSLY;

    protected int eglVersion;
    
    /**
     *  Creates a new TnGLSurfaceField for the specified version of OpenGL ES.
     *  
     * @param id
     * @param version VERSION_1_1, VERSION_2_0
     */
    public TnGLSurfaceField(int id, int version)
    {
        super(id, true);
        
        this.eglVersion = version;
        
        bind();
    }

    /**
     * Set the renderer associated with this field. Also starts the thread that will call the renderer, which in turn
     * causes the rendering to start. <br />
     * 
     * This method should be called once and only once in the life-cycle of a TnGLSurfaceField.
     * 
     * @param glRenderer
     */
    public void setRenderer(TnGLRenderer glRenderer)
    {
        this.glRenderer = glRenderer;

        this.nativeUiComponent.callUiMethod(METHOD_SET_RENDERER, null);
    }

    /**
     * Get the renderer associated with this field.
     * @return the render
     */
    public TnGLRenderer getRenderer()
    {
        return this.glRenderer;
    }

    /**
     * Set the rendering mode. When renderMode is RENDERMODE_CONTINUOUSLY, the renderer is called repeatedly to
     * re-render the scene. When renderMode is RENDERMODE_WHEN_DIRTY, the renderer only rendered when the surface is
     * created, or when requestRender() is called. Defaults to RENDERMODE_CONTINUOUSLY.
     * 
     * @param renderMode RENDERMODE_WHEN_DIRTY, RENDERMODE_CONTINUOUSLY
     */
    public void setRenderMode(int renderMode)
    {
        if (renderMode != RENDERMODE_WHEN_DIRTY && renderMode != RENDERMODE_CONTINUOUSLY)
            throw new IllegalArgumentException("renderMode is wrong.");

        this.renderMode = renderMode;

        this.nativeUiComponent.callUiMethod(METHOD_SET_RENDER_MODE, null);
    }

    /**
     * Get ther render mode.
     * 
     * @return render mode.
     */
    public int getRenderMode()
    {
        return this.renderMode;
    }
    
    /**
     * Get the opengl version.
     * 
     * @return the version information.
     */
    public int getEGLClientVersion()
    {
        return this.eglVersion;
    }

    /**
     * Request that the renderer render a frame. This method is typically used when the render mode has been set to
     * RENDERMODE_WHEN_DIRTY, so that frames are only rendered on demand. May be called from any thread. Must not be
     * called before a renderer has been set.
     */
    public void requestRender()
    {
        this.nativeUiComponent.callUiMethod(METHOD_REQUEST_RENDER, null);
    }

    /**
     * Queue a runnable to be run on the GL rendering thread. This can be used to communicate with the Renderer on the
     * rendering thread. Must not be called before a renderer has been set.
     * 
     * @param runnable the runnable to be run on the GL rendering thread. 
     */
    public void postRenderEvent(Runnable runnable)
    {
        this.nativeUiComponent.callUiMethod(METHOD_POST_GL_EVENT, new Object[]{runnable});
    }
    
    /**
     * Inform the view that the GL Engine is paused. Calling this method will pause the rendering thread. Must not be called before a renderer has been set.
     */
    public void onPause()
    {
        this.nativeUiComponent.callUiMethod(METHOD_ON_PAUSE, null);
    }
    
    /**
     * Inform the view that GL Engine is resumed. Calling this method will recreate the OpenGL display and resume the rendering thread. Must not be called
     * before a renderer has been set.
     */
    public void onResume()
    {
        this.nativeUiComponent.callUiMethod(METHOD_ON_RESUME, null);
    }
    
    public void setBackgroundColor(int color)
    {
        this.nativeUiComponent.callUiMethod(METHOD_SETBACKGROUND_COLOR, new Object[]{new Integer(color)});
        super.setBackgroundColor(color);
    }
    
    protected void initDefaultStyle()
    {

    }

    protected void paint(AbstractTnGraphics graphics)
    {

    }
    
    public void setEGLClientVersion(int version)
    {
        this.nativeUiComponent.callUiMethod(METHOD_SET_OGLES_CLIENT_VERSION, new Object[]{new Integer(version)});
    }    
}
