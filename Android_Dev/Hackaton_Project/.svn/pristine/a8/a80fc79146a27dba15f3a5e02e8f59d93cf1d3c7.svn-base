/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimGLField.java
 *
 */
package com.telenav.tnui.widget.rim;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import net.rim.device.api.opengles.GLField;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.rim.RimUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnGLSurfaceField;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-10
 */
class RimGLField extends GLField implements INativeUiComponent
{
    protected TnGLSurfaceField tnComponent;

    protected boolean isVisible;

    protected boolean isFocusable;

    public RimGLField(TnGLSurfaceField tnComponent)
    {
        super(VERSION_1_1);
        this.tnComponent = tnComponent;
        isVisible = true;
        isFocusable = false;
    }

    protected void onFocus(int direction)
    {
        super.onFocus(direction);
        this.tnComponent.dispatchFocusChanged(true);
    }

    protected void onUnfocus()
    {
        super.onUnfocus();
        this.tnComponent.dispatchFocusChanged(false);
    }

    protected void onDisplay()
    {
        super.onDisplay();
        
        this.tnComponent.dispatchDisplayChanged(true);
    }
    
    protected void onUndisplay()
    {
        super.onUndisplay();
        
        this.tnComponent.dispatchDisplayChanged(false);
    }
    
    protected void drawFocus(Graphics graphics, boolean on)
    {
        return;// if we used the system focus style, it will draw a black rectangle.
    }

    protected void layout(int width, int height)
    {
        this.tnComponent.sublayout(width, height);

        this.setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public int getPreferredWidth()
    {
        if (!isVisible)
            return 0;

        return this.tnComponent.getPreferredWidth();
    }

    public int getPreferredHeight()
    {
        if (!isVisible)
            return 0;

        return this.tnComponent.getPreferredHeight();
    }

    protected void setExtent(int width, int height)
    {
        if (this.getExtent().width != width || this.getExtent().height != height)
        {
            onSizeChanged(width, height, this.getExtent().width, this.getExtent().height);
        }

        super.setExtent(width, height);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        this.tnComponent.dispatchSizeChanged(w, h, oldw, oldh);
    }

    protected void paint(Graphics g)
    {
        AbstractTnGraphics.getInstance().setGraphics(g);

        tnComponent.draw(AbstractTnGraphics.getInstance());
    }

    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = RimUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!RimUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;

        switch (eventMethod)
        {
            case TnGLSurfaceField.METHOD_REQUEST_RENDER:
            {
                this.update();
                break;
            }
            case TnGLSurfaceField.METHOD_SET_RENDER_MODE:
            {
                switch (tnComponent.getRenderMode())
                {
                    case TnGLSurfaceField.RENDERMODE_CONTINUOUSLY:
                    {
                        this.setTargetFrameRate(25);
                        break;
                    }
                    case TnGLSurfaceField.RENDERMODE_WHEN_DIRTY:
                    {
                        this.setTargetFrameRate(0);
                        break;
                    }
                }
                break;
            }
            case TnGLSurfaceField.METHOD_SET_RENDERER:
            {
                break;
            }
            case TnGLSurfaceField.METHOD_POST_GL_EVENT:
            {
                UiApplication.getUiApplication().invokeLater((Runnable)args[0]);
                break;
            }
        }
        return null;
    }

    public int getNativeHeight()
    {
        return this.getHeight();
    }

    public int getNativeWidth()
    {
        return this.getWidth();
    }

    public int getNativeX()
    {
        return this.getLeft();
    }

    public int getNativeY()
    {
        return this.getTop();
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.tnComponent;
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean isNativeVisible()
    {
        return this.isVisible;
    }

    public boolean requestNativeFocus()
    {
        this.setFocus();

        return true;
    }

    public void requestNativePaint()
    {
        this.invalidate();
    }

    public boolean isFocusable()
    {
        return this.isFocusable;
    }

    public void setNativeVisible(boolean isVisible)
    {
        if (this.isVisible != isVisible)
        {
            this.updateLayout();
        }
        this.isVisible = isVisible;
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        if (this.isFocusable != isFocusable)
        {
            this.invalidate();
        }
        this.isFocusable = isFocusable;
    }

    protected void initialize(GL gl)
    {
        RimGL10 rimGL10 = null;
        if (gl instanceof GL10)
        {
            rimGL10 = new RimGL10();
            rimGL10.setGL((GL10) gl);
        }
        else if (gl instanceof GL11)
        {
            rimGL10 = new RimGL11();
            rimGL10.setGL((GL11) gl);
        }
        this.tnComponent.getRenderer().onSurfaceCreated(rimGL10);
    }

    protected void render(GL gl)
    {
        RimGL10 rimGL10 = null;
        if (gl instanceof GL10)
        {
            rimGL10 = new RimGL10();
            rimGL10.setGL((GL10) gl);
        }
        else if (gl instanceof GL11)
        {
            rimGL10 = new RimGL11();
            rimGL10.setGL((GL11) gl);
        }
        this.tnComponent.getRenderer().render(rimGL10);
    }

    protected void sizeChanged(GL gl, int width, int height)
    {
        super.sizeChanged(gl, width, height);

        RimGL10 rimGL10 = null;
        if (gl instanceof GL10)
        {
            rimGL10 = new RimGL10();
            rimGL10.setGL((GL10) gl);
        }
        else if (gl instanceof GL11)
        {
            rimGL10 = new RimGL11();
            rimGL10.setGL((GL11) gl);
        }

        this.tnComponent.getRenderer().onSurfaceChanged(rimGL10, width, height);
    }
}
