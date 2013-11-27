/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidWebView.java
 *
 */
package com.telenav.tnui.widget.android;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.tnui.widget.TnWebBrowserField.TnWebBrowserFieldConfig;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-1
 */
class AndroidWebView extends WebView implements INativeUiComponent
{
    protected TnWebBrowserField tnComponent;
    protected TnWebBrowserFieldConfig config;
    
    public AndroidWebView(Context context, TnWebBrowserField browserField)
    {
        super(context);
        
        this.tnComponent = browserField;
        this.config = new TnWebBrowserFieldConfig(this.tnComponent);
        this.callUiMethod(TnWebBrowserField.METHOD_GET_CONFIG, null);
        this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        init();
    }
    
    private void init()
    {
        this.setWebChromeClient(new WebChromeClient()
        {
            public void onReceivedTitle(WebView view, String title)
            {
                if(tnComponent.getWebBrowserEventListener() != null)
                {
                    tnComponent.getWebBrowserEventListener().onReceiveTitle(tnComponent, title);
                }
            }
        });
        
        this.setWebViewClient(new WebViewClient()
        {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                if(tnComponent.getWebBrowserEventListener() != null)
                {
                    tnComponent.getWebBrowserEventListener().onPageError(tnComponent, errorCode + ", "+ description);
                }
            }
            
            public void onPageFinished(WebView view, String url)
            {
                if(tnComponent.getWebBrowserEventListener() != null)
                {
                    tnComponent.getWebBrowserEventListener().onPageFinished(tnComponent, url);
                }
            }
        });
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
        
        if (prefWidth < 0 || (prefWidth > widthSize && (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY)))
        {
            prefWidth = widthSize;
        }
        
        if (prefHeight < 0  || (prefHeight > heightSize && (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY)))
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
    
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        
        AbstractTnGraphics.getInstance().setGraphics(canvas);
        
        canvas.save();
        
        tnComponent.draw(AbstractTnGraphics.getInstance());
        
        canvas.restore();
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
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = AndroidUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

        if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;
        
        switch(eventMethod)
        {
            case TnWebBrowserField.METHOD_GET_URL:
            {
                return this.getUrl();
            }
            case TnWebBrowserField.METHOD_LOAD_DATA:
            {
                if(args[0] instanceof String)
                {
                    this.loadDataWithBaseURL((String)args[1], (String)args[0], "text/html", "utf-8", null);
                }
                else
                {
                    this.loadDataWithBaseURL((String)args[2], new String((byte[])args[0]), (String)args[1], "utf-8", null);
                }
                break;
            }
            case TnWebBrowserField.METHOD_LOAD_URL:
            {
                if(args.length == 1)
                {
                    this.loadUrl((String)args[0]);
                }
                else if(args[1] instanceof Hashtable)
                {
                    this.loadUrl((String)args[0], (Hashtable)args[1]);
                }
                else
                {
                    this.postUrl((String)args[0], (byte[])args[1]);
                }
                break;
            }
            case TnWebBrowserField.METHOD_RELOAD:
            {
                this.reload();
                break;
            }
            case TnWebBrowserField.METHOD_GET_CONFIG:
            {
                this.config.setJavaScriptEnabled(this.getSettings().getJavaScriptEnabled());
                this.config.setSupportZoom(this.getSettings().supportZoom());
                this.config.setUserAgent(this.getSettings().getUserAgentString());
                
                return this.config;
            }
            case TnWebBrowserField.METHOD_SET_CONFIG:
            {
                this.getSettings().setJavaScriptEnabled(this.config.isJavaScriptEnabled());
                this.getSettings().setSupportZoom(this.config.isSupportZoom());
                this.getSettings().setUserAgentString(this.config.getUserAgent());
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
    
    public boolean onLongClick(View v)
    {
        return AndroidUiEventHandler.onLongClick(this.tnComponent);
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
        
        return isHandled ? true : super.onTouchEvent(event);
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
    
    public void createContextMenu(ContextMenu menu)
    {
        super.createContextMenu(menu);
        
        AndroidUiEventHandler.onCreateContextMenu(this.tnComponent, menu);
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
}
