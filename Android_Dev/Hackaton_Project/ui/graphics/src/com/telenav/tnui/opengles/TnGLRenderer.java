/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnGLRenderer.java
 *
 */
package com.telenav.tnui.opengles;

/**
 * A generic renderer interface.
 * <p>
 * The renderer is responsible for making OpenGL calls to render a frame.
 * <p>
 * GLSurfaceView clients typically create their own classes that implement this interface, and then call
 * {@link TnGLSurfaceField#setRenderer} to register the renderer with the TnGLSurfaceField.
 * <p>
 * <h3>Threading</h3>
 * The renderer will be called on a separate thread, so that rendering performance is decoupled from the UI thread.
 * Clients typically need to communicate with the renderer from the UI thread, because that's where input events are
 * received. Clients can communicate using any of the standard Java techniques for cross-thread communication, or they
 * can use the {@link TnGLSurfaceField#queueEvent(Runnable)} convenience method.
 * <p>
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Sep 3, 2010
 */
public interface TnGLRenderer
{
    /**
     * Called when the surface changed size.
     * <p>
     * Called after the surface is created and whenever
     * the OpenGL ES surface size changes.
     * <p>
     * Typically you will set your viewport here. If your camera
     * is fixed then you could also set your projection matrix here:
     * <pre class="prettyprint">
     * void onSurfaceChanged(TnGL10 gl, int width, int height) {
     *     gl.glViewport(0, 0, width, height);
     *     // for a fixed camera, set the projection too
     *     float ratio = (float) width / height;
     *     gl.glMatrixMode(TnGL10.GL_PROJECTION);
     *     gl.glLoadIdentity();
     *     gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
     * }
     * </pre>
     * @param gl the GL interface. Use <code>instanceof</code> to
     * test if the interface supports GL11 or higher interfaces.
     * @param width
     * @param height
     */
    public void onSurfaceChanged(TnGL10 gl, int width, int height);

    /**
     * Called to draw the current frame.
     * <p>
     * This method is responsible for drawing the current frame.
     * <p>
     * The implementation of this method typically looks like this:
     * <pre class="prettyprint">
     * void render(TnGL10 gl) {
     *     gl.glClear(TnGL10.GL_COLOR_BUFFER_BIT | TnGL10.GL_DEPTH_BUFFER_BIT);
     *     //... other gl calls to render the scene ...
     * }
     * </pre>
     * @param gl the GL interface. Use <code>instanceof</code> to
     * test if the interface supports GL11 or higher interfaces.
     */
    public void render(TnGL10 gl);

    /**
     * Called when the surface is created or recreated.
     * <p>
     * Called when the rendering thread
     * starts and whenever the EGL context is lost. The context will typically
     * be lost when the device awakes after going to sleep.
     * <p>
     * Since this method is called at the beginning of rendering, as well as
     * every time the EGL context is lost, this method is a convenient place to put
     * code to create resources that need to be created when the rendering
     * starts, and that need to be recreated when the EGL context is lost.
     * Textures are an example of a resource that you might want to create
     * here.
     * <p>
     * @param gl the GL interface. Use <code>instanceof</code> to
     * test if the interface supports GL11 or higher interfaces.
     * @param config the EGLConfig of the created surface. Can be used
     * to create matching pbuffers.
     */
    public void onSurfaceCreated(TnGL10 gl);
    
    public void setNativeRenderer(Object renderer);
    
    public Object getNativeRenderer();
}
