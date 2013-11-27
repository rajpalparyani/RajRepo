/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidGLSurfaceField.java
 *
 */
package com.telenav.tnui.widget.android;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.view.ScaleGestureDetector;
import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnGLSurfaceField;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Sep 7, 2010
 */
class AndroidGLSurfaceField extends GLSurfaceView implements INativeUiComponent, View.OnLongClickListener, ScaleGestureDetector.OnScaleGestureListener
{
    protected TnGLSurfaceField tnComponent;
    
    private boolean isReallySurfaceDestroyed;
    
    private ScaleGestureDetector scaleGestureDetector = null;
    
    public AndroidGLSurfaceField(Context context, TnGLSurfaceField tnComponent)
    {
        super(context);
        
        this.setBackgroundColor(0x00000000);
        
        this.tnComponent = tnComponent;
        
        this.setOnLongClickListener(this);
        
        this.scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    private int mSurfaceWidth;
    private int mSurfaceHeight;
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
        if(mSurfaceWidth != w || mSurfaceHeight != h)
        {
            super.surfaceChanged(holder, format, w, h);
        }
        
        mSurfaceWidth = w;
        mSurfaceHeight = h;
    }
    
    public boolean onLongClick(View v)
    {
        return AndroidUiEventHandler.onLongClick(this.tnComponent);
    }
    
    public void surfaceCreated(SurfaceHolder holder)
    {
        isReallySurfaceDestroyed = false;
        super.surfaceCreated(holder);
    }

    /**
     * This method is part of the SurfaceHolder.Callback interface, and is not normally called or subclassed by clients
     * of GLSurfaceView.
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // Surface will be destroyed when we return
        isReallySurfaceDestroyed = true;
        super.surfaceDestroyed(holder);
    }
    
    public void onPause()
    {
        Class gLSurfaceViewClass = GLSurfaceView.class;
        Field field = null;
        Method method = null;
        Class glThreadClass = null;
        Object o = null;
        try
        {
            field = gLSurfaceViewClass.getDeclaredField("mGLThread");
            field.setAccessible(true);
            try
            {
                o = field.get(this);
            }
            catch (IllegalArgumentException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (IllegalAccessException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            glThreadClass = field.getType();
            try
            {
                method = glThreadClass.getDeclaredMethod("surfaceDestroyed", null);
            }
            catch (NoSuchMethodException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (field != null && method != null)
        {
            if (!isReallySurfaceDestroyed)
            {
                try
                {
                    method.setAccessible(true);
                    method.invoke(o, null);
                }
                catch (IllegalArgumentException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
         super.onPause();
    }

    /**
     * Inform the view that the activity is resumed. The owner of this view must call this method when the activity is
     * resumed. Calling this method will recreate the OpenGL display and resume the rendering thread. Must not be called
     * before a renderer has been set.
     */
    public void onResume()
    {
        super.onResume();
        Class gLSurfaceViewClass = GLSurfaceView.class;
        Field field = null;
        Method method = null;
        Class glThreadClass = null;
        Object o = null;
        try
        {
            field = gLSurfaceViewClass.getDeclaredField("mGLThread");
            field.setAccessible(true);
            try
            {
                o = field.get(this);
            }
            catch (IllegalArgumentException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (IllegalAccessException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            glThreadClass = field.getType();
            try
            {
                method = glThreadClass.getDeclaredMethod("surfaceCreated", null);
            }
            catch (NoSuchMethodException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (field != null && method != null)
        {
            if (!isReallySurfaceDestroyed)
            {
                try
                {
                    method.setAccessible(true);
                    method.invoke(o, null);
                }
                catch (IllegalArgumentException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        this.tnComponent.sublayout(widthSize, heightSize);
        
        int prefWidth = this.tnComponent.getPreferredWidth();
        int prefHeight = this.tnComponent.getPreferredHeight();
        
        if (prefWidth <= 0 || (prefWidth > widthSize && (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY)))
        {
            prefWidth = widthSize;
        }
        
        if (prefHeight <= 0  || (prefHeight > heightSize && (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY)))
        {
            prefHeight = heightSize;
        }
        
        setMeasuredDimension(prefWidth, prefHeight);
    }
    
    public void setSelected(boolean selected)
    {
        super.setSelected(selected);
        dispatchSetSelected(selected);
    }
    
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        
        boolean gainFocus = this.isFocused() || this.isPressed();
        if(this.tnComponent.isFocused() != gainFocus)
        {
            this.tnComponent.dispatchFocusChanged(gainFocus);
            invalidate();
        }
    }
    
    protected void dispatchSetSelected(boolean selected) 
    {
        super.dispatchSetSelected(selected);
        
        if(selected != tnComponent.isFocused())
            this.tnComponent.dispatchFocusChanged(selected);
    }
    
    protected void onDraw(Canvas canvas)
    {
        AbstractTnGraphics.getInstance().setGraphics(canvas);
        
        canvas.save();
        
        tnComponent.draw(AbstractTnGraphics.getInstance());
        
        canvas.restore();
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;
        
        switch(eventMethod)
        {
            case TnGLSurfaceField.METHOD_REQUEST_RENDER:
            {
                this.requestRender();
                break;
            }
            case TnGLSurfaceField.METHOD_SET_RENDER_MODE:
            {
                switch(tnComponent.getRenderMode())
                {
                    case TnGLSurfaceField.RENDERMODE_CONTINUOUSLY:
                    {
                        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
                        break;
                    }
                    case TnGLSurfaceField.RENDERMODE_WHEN_DIRTY:
                    {
                        this.setRenderMode(RENDERMODE_WHEN_DIRTY);
                        break;
                    }
                }
                break;
            }
            case TnGLSurfaceField.METHOD_SET_RENDERER:
            {
                this.setRenderer(new AndroidGLRenderer(tnComponent.getRenderer()));
                break;
            }
            case TnGLSurfaceField.METHOD_POST_GL_EVENT:
            {
                this.queueEvent((Runnable)args[0]);
                break;
            }
            case TnGLSurfaceField.METHOD_SETBACKGROUND_COLOR:
            {
                this.setBackgroundColor(((Integer)args[0]).intValue());
                break;
            }
            case TnGLSurfaceField.METHOD_ON_PAUSE:
            {
                this.onPause();
                break;
            }
            case TnGLSurfaceField.METHOD_ON_RESUME:
            {
                this.onResume();
                break;
            }
            case TnGLSurfaceField.METHOD_SET_OGLES_CLIENT_VERSION:
            {
                this.setEGLContextClientVersion(((Integer)args[0]).intValue());
                break;
            }            
        }
        return null;
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean isNativeVisible()
    {
        return this.getVisibility() == VISIBLE;
    }

    public boolean requestNativeFocus()
    {
        return this.requestFocus();
    }

    public void requestNativePaint()
    {
        this.postInvalidate();
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public void setNativeVisible(boolean isVisible)
    {
        this.setVisibility(isVisible ? VISIBLE : GONE);
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

    public void onClick(View v)
    {
        if(this.tnComponent.isFocused() != false)
        {
            this.tnComponent.dispatchFocusChanged(false);
            invalidate();
        }
        
        AndroidUiEventHandler.onClick(this.tnComponent);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyDown(this.tnComponent, keyCode, event);
        
        return isHandled ? true : super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onKeyUp(this.tnComponent, keyCode, event);
        
        return isHandled ? true : super.onKeyUp(keyCode, event);
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTouch(this.tnComponent, event);
        
		
        if(isHandled)
        {
            return true;
        }
        
        this.scaleGestureDetector.onTouchEvent(event);
		
        return super.onTouchEvent(event);
    }
    
    public boolean onTrackballEvent(MotionEvent event)
    {
        boolean isHandled = AndroidUiEventHandler.onTrackball(this.tnComponent, event);

        return isHandled ? true : super.onTrackballEvent(event);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        this.tnComponent.dispatchSizeChanged(w, h, oldw, oldh);
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        
        this.tnComponent.dispatchDisplayChanged(true);
    }
    
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        
        this.tnComponent.dispatchDisplayChanged(false);
    }

    public boolean onScale(ScaleGestureDetector detector)
    {
        AndroidUiEventHandler.onScale(this.tnComponent, detector);
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector detector)
    {
        AndroidUiEventHandler.onScaleBegin(this.tnComponent, detector);
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector)
    {
        AndroidUiEventHandler.onScaleEnd(this.tnComponent, detector);
    }
}
