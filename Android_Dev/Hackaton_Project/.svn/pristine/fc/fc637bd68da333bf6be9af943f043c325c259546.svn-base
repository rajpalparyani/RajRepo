/**
 * 
 */
package com.telenav.ui.citizen.android;


import java.util.Hashtable;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.htmlsdk.ILogDelegate;
import com.telenav.htmlsdk.TNWebView;
import com.telenav.htmlsdk.TNWebViewClient;
import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.IGestureListener;
import com.telenav.util.PrimitiveTypeCache;


/**
 * @author jbtian
 *
 */
public class AndroidCitizenWebView extends TNWebView implements INativeUiComponent, INetworkStatusListener 
{

    CitizenWebComponent tnComponent;
    AndroidWebViewCache tempCache;
    protected int loadStatus;
    protected boolean isDestroyed = false;
    protected boolean isScrollable = true;
    private int       webViewPreviousState;
    private static final int PAGE_STARTED    = 0x1;
    private static final int PAGE_REDIRECTED = 0x2;
    
    private boolean shouldLockOrientation; 
    
    private IGestureListener gestureListener;
    
    /**
     * @param context
     */
    public AndroidCitizenWebView(Context context, CitizenWebComponent frogWebComponent) 
    {
        super(context);
        tnComponent = frogWebComponent;
        this.setLogDelegate(LogDelegate.getInstance());
        this.setWebViewClient(new GapViewClientDelegate(this, context));
        this.setLayoutParams( new LinearLayout.LayoutParams(      
            LinearLayout.LayoutParams.FILL_PARENT,      
            LinearLayout.LayoutParams.FILL_PARENT ));
    }
    
    protected void onTimeOut()
    {
        loadStatus = CitizenWebComponent.LOAD_FAILED;
        tnComponent.updateLoadStatus(loadStatus);
        
        if(tnComponent.getWebBrowserEventListener() != null)
        {
            tnComponent.getWebBrowserEventListener().onPageError(tnComponent, "Timeout");
        }
    
    }
    
    protected void onDraw(Canvas canvas)
    {
        try
        {
            if( isDestroyed )
            {
                return;
            }
            super.onDraw(canvas);
            
            if(loadStatus <= 0)
            {
                AbstractTnGraphics.getInstance().setGraphics(canvas);
    
                canvas.save();
    
                tnComponent.draw(AbstractTnGraphics.getInstance());
    
                canvas.restore();
            }
        }
        catch(Exception e)
        {
            Logger.log(Logger.EXCEPTION, this.getClass().getName(), e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#requestNativePaint()
     */
    public void requestNativePaint() {
        this.postInvalidate();
    }

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#setNativeVisible(boolean)
     */
    public void setNativeVisible(boolean isVisible) {
        this.setVisibility(isVisible ? VISIBLE : GONE);

    }

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#isNativeVisible()
     */
    public boolean isNativeVisible() {
        return this.getVisibility() == VISIBLE;
    }

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#setNativeFocusable(boolean)
     */
    public void setNativeFocusable(boolean isFocusable) {
        this.setFocusable(isFocusable);

    }

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#isNativeFocusable()
     */
    public boolean isNativeFocusable() {
        
        return this.isFocusable();
    }

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#requestNativeFocus()
     */
    public boolean requestNativeFocus() {
        this.requestFocus();
        return true;
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

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#callUiMethod(int, java.lang.Object[])
     */
     public Object callUiMethod(int eventMethod, Object[] args)
    {
         Object obj = AndroidUiMethodHandler.callUiMethod(tnComponent, this, eventMethod, args);

         if (!AndroidUiMethodHandler.NO_HANDLED.equals(obj))
             return obj;

        switch(eventMethod)
        {
//            case TnWebBrowserField.METHOD_GET_URL:
//            {
//                return this.getUrl();
//            }
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
            case TnWebBrowserField.METHOD_RELOAD:
            {
                this.reload();
                break;
            }
//            case TnWebBrowserField.METHOD_GET_CONFIG:
//            {
//                this.config.setJavaScriptEnabled(this.getSettings().getJavaScriptEnabled());
//                this.config.setSupportZoom(this.getSettings().supportZoom());
//                this.config.setUserAgent(this.getSettings().getUserAgentString());
//                
//                return this.config;
//            }
//            case TnWebBrowserField.METHOD_SET_CONFIG:
//            {
//                this.getSettings().setJavaScriptEnabled(this.config.isJavaScriptEnabled());
//                this.getSettings().setSupportZoom(this.config.isSupportZoom());
//                this.getSettings().setUserAgentString(this.config.getUserAgent());
//                break;
//            }
            case CitizenWebComponent.RESET_LOAD_STATUS:
            {
                loadStatus = 0;
                break;
            }
            case CitizenWebComponent.SET_BACKGROUND_COLOR:
            {
                if(args[0] instanceof Integer)
                {
                    int color = ((Integer)args[0]).intValue();
                    this.setBackgroundColor(color);
                }
                break;
            }
            case CitizenWebComponent.METHOD_LOAD_URL:
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
            case CitizenWebComponent.SET_HANDLER_LOAD_URL:
            {
                this.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler)args[0]);
                break;
            }
            case CitizenWebComponent.METHOD_GET_URL:
            {
                return this.getUrl();
            }
            case CitizenWebComponent.RESET:
            {
                this.clearHistory();
                return null;
            }
            case CitizenWebComponent.METHOD_REQUEST_RELAYOUT:
            {
                Runnable run = new Runnable()
                {
                    public void run()
                    {
                        requestLayout();
                    }
                };
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
                break;
            }
            case CitizenWebComponent.ENABLE_TEMP_CACHE:
            {
                boolean isEnabled = ((Boolean)args[0]).booleanValue();
                
                if(isEnabled){
                    if(tempCache == null){
                        tempCache = new AndroidWebViewCache();
                    }
                    else{
                        tempCache.clear();
                    }
                    
                    this.addJavascriptInterface(tempCache, "TempCache");
                }
                break;
            }
            case CitizenWebComponent.SET_DATA_TO_TEMP_CACHE:
            {
                if(tempCache != null){
                    if(args.length >= 2 && args[0] != null && args[1] != null){
                        String key = (String)args[0];
                        String value = (String)args[1];
                        tempCache.put(key, value);
                    }
                }
                break;
            }
            case CitizenWebComponent.GET_DATA_TO_TEMP_CACHE:
            {
                if (tempCache != null)
                {
                    if (args.length == 1 && args[0] != null)
                    {
                        String value = (String) tempCache.get(args[0]);
                        return value;
                    }
                }
                break;
            }
            case CitizenWebComponent.INIT_DEFAULT_ZOOM_DENSITY:
            {
//                initDefaultZoomDensity();
                break;
            }
            case CitizenWebComponent.SET_GESTURE_LISTENER:
            {
                this.gestureListener = (IGestureListener)args[0];
                break;
            }
            case CitizenWebComponent.DESTROY:
            {
                isDestroyed = true;
                this.onDestroy();
                break;
            }
            case CitizenWebComponent.SET_SCROLLABLE:
            {
                isScrollable =  ((Boolean)args[0]).booleanValue();
                setVerticalScrollBarEnabled(isScrollable);
                setHorizontalScrollBarEnabled(isScrollable);
                break;
            }
            case CitizenWebComponent.FREE_MEMORY:
            {
                this.freeMemory();
                break;
            }
            case CitizenWebComponent.SET_ERROR_MESSAGE:
            {
                final String okBtnStr = (String)args[0];
                final String errorMessage = (String)args[1];
                
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        setWatcherLanguage(okBtnStr, errorMessage);
                    }
                });
                break;
            }
            case CitizenWebComponent.SET_LAYER_TYPE:
            {
                if (Integer.parseInt(Build.VERSION.SDK) >= 11)
                {
                    int layerType = ((Integer) args[0]).intValue();
                    int androidLayerType = -1;
                    if (layerType == CitizenWebComponent.LAYER_TYPE_NONE)
                    {
                        androidLayerType = LAYER_TYPE_NONE;
                    }
                    else if (layerType == CitizenWebComponent.LAYER_TYPE_SOFTWARE)
                    {
                        androidLayerType = LAYER_TYPE_SOFTWARE;
                    }
                    else if (layerType == CitizenWebComponent.LAYER_TYPE_HARDWARE)
                    {
                        androidLayerType = LAYER_TYPE_HARDWARE;
                    }
                    
                    if (androidLayerType != -1)
                    {
                        final int realLayerType = androidLayerType;
                        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                        .runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                AndroidCitizenWebView.this.setLayerType(realLayerType, null);
                            }
                        });
                    }
                }

                break;
            }
            case CitizenWebComponent.GET_DEVICE_USER_AGENT:
            {
                return this.getSettings().getUserAgentString();
            }
            case CitizenWebComponent.IS_UPDATE_CONTENT:
            {
                String url = (String)args[0];
                return PrimitiveTypeCache.valueOf(this.isUpdateContent(url));
            }
            case CitizenWebComponent.SET_DEVICE_USER_AGENT:
            {
                String url = (String)args[0];
                if(url != null && url.trim().length() > 0)
                {
                    this.getSettings().setUserAgentString(url);
                }
                break;
            }
            case CitizenWebComponent.WEB_VIEW_SHOW:
            {
                this.didWebViewShow();
                break;
            }
        }
        
        return null;
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
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
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
//        boolean isHandled = AndroidUiEventHandler.onKeyDown(this.tnComponent, keyCode, event);
//        
//        return isHandled ? true : super.onKeyDown(keyCode, event);
        
        //I think we should not intercept the keyDown event like other TN components
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
//        boolean isHandled = AndroidUiEventHandler.onKeyUp(this.tnComponent, keyCode, event);
//        
//        return isHandled ? true : super.onKeyUp(keyCode, event);
        
        //I think we should not intercept the keyUp event like other TN components
        return super.onKeyUp(keyCode, event);
    }
    
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (tnComponent.isVisible()) {
                loadStatus = CitizenWebComponent.LOAD_SUCCESSFULLY;

                tnComponent.updateLoadStatus(loadStatus);

                postInvalidate();
            }
        }
        else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {//fix bug TNANDROID-2270, browser needn't handle menu event by itself.
            return false;
        }
        
        return super.dispatchKeyEvent(event);
    }
    public boolean onTouchEvent(MotionEvent event)
    {
        //the switch block is used to avoid android's bug, or the virtual keyboard might not be shown
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            //Don't call requestFocus when ACTION_UP to improve the scrolling performance.
            //case MotionEvent.ACTION_UP:
            if (!this.hasFocus()) {
                this.requestFocus();
            }
            break;
        }

        AndroidUiEventHandler.onTouch(this.tnComponent, event);
        
        if (this.gestureListener != null)
        {
            this.gestureListener.onTouchEvent(event);
        } 
               
        if (!isScrollable && event.getAction() == MotionEvent.ACTION_MOVE)
        {
            return true;
        }
        boolean isHandled = super.onTouchEvent(event); 
        
        return isHandled;
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        this.tnComponent.dispatchSizeChanged(w, h, oldw, oldh);
    }
    
    //don't call super.onScrollChanged to improve the performance
    protected void onScrollChanged(int l, int t, int oldl, int oldt) 
    {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /* (non-Javadoc)
     * @see com.telenav.tnui.core.INativeUiComponent#getTnUiComponent()
     */
    public AbstractTnComponent getTnUiComponent() {
        return tnComponent;
    }
    
    public void createContextMenu(ContextMenu menu)
    {
        super.createContextMenu(menu);
        
        AndroidUiEventHandler.onCreateContextMenu(this.tnComponent, menu);
    }

    public boolean performLongClick()
    {
        if(this.tnComponent.isDisableLongClick())
        {
            return true;
        }
        else
        {
            return super.performLongClick();
        }
    }
    
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        
        if (shouldLockOrientation)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
        }
        
        NetworkStatusManager.getInstance().addStatusListener(this);
        this.tnComponent.dispatchDisplayChanged(true);
    }
    
    protected void onDetachedFromWindow()
    {
        if (shouldLockOrientation)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
        super.onDetachedFromWindow();     
        
        this.tnComponent.dispatchDisplayChanged(false);
        NetworkStatusManager.getInstance().removeStatusListener(this);
    }

    public class GapViewClientDelegate extends TNWebViewClient
    {
        static final String ANIMATION_FLAG = "animation=true";
        
        Context context;
        
        public GapViewClientDelegate(TNWebView tnWebView, Context context)
        {
            super(tnWebView);
            this.context = context;
        }
        
        public boolean shouldOverrideUrlLoading(WebView view, String url) 
        {
            webViewPreviousState = PAGE_REDIRECTED;
            if ((url.startsWith("http://") || url.startsWith("https://")
                    || url .startsWith("file://"))
                    && url.indexOf("nativebrowser") != -1)
            {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url.substring(0, url.indexOf("nativebrowser")-1)));
                    this.context.startActivity(intent);
                    return true;
                }
                catch (android.content.ActivityNotFoundException e)
                {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
            else if(url.startsWith(WebView.SCHEME_MAILTO))
            {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Logger.log(Logger.EXCEPTION, "TNWebView", "Error " + url + ": " + e.toString());
                }
                return true;
            }
            
            return super.shouldOverrideUrlLoading(view, url);
        }
        
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            try
            {
                loadStatus = CitizenWebComponent.LOAD_FAILED;
                tnComponent.updateLoadStatus(loadStatus);
                
                postInvalidate();
                
                if(tnComponent.getWebBrowserEventListener() != null)
                {
                    tnComponent.getWebBrowserEventListener().onPageError(tnComponent, errorCode + ", "+ description);
                }
                
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
        
        public void onPageStarted (WebView view, final String url, Bitmap favicon)
        {
            try
            {
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if(checkNeedAnimation(url)){
                            //System.out.println("-----start animation, url: " + url);
                            loadStatus = 0;
                            tnComponent.enableAnimation(true);
                            postInvalidate();
                        }
                        
                    }
                });
                webViewPreviousState = PAGE_STARTED;
                super.onPageStarted(view, url, favicon);
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
        
        public void onPageFinished(final WebView view, final String url)
        {
            try
            {
                final String newUrl = url;
                Thread pool = new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            if (tnComponent.needWaitProgress())
                            {
                                Thread.sleep(100);
                            }
                        }
                        catch (InterruptedException e)
                        {
                            Logger.log(this.getClass().getName(), e);
                        }
    
                        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                //String originalUrl = view.getOriginalUrl();
                                
                                if(android.os.Build.VERSION.SDK_INT < 11)
                                {
                                    if(url != null)
                                    {
                                        removeProgressBar();
                                    }
                                    else
                                    {
                                        //System.out.println("------Redirect request, do not remove progress bar!");                            
                                    }
                                }
                                //for Android tablet os version greater than 2.x;
                                else
                                {
                                    if(webViewPreviousState == PAGE_STARTED)
                                    {
                                        removeProgressBar();
                                    }
    
                                }
    
                            }
                            public void removeProgressBar()
                            {
                                loadStatus = CitizenWebComponent.LOAD_SUCCESSFULLY;
                                tnComponent.updateLoadStatus(loadStatus);        
                                //System.out.println("-------Disable animation, url: " + url);
                                tnComponent.enableAnimation(false);
                                tnComponent.requestLayout();
                                postInvalidate();
                            }
                        });
                        if (tnComponent.getWebBrowserEventListener() != null)
                        {
                            tnComponent.getWebBrowserEventListener().onPageFinished(tnComponent, newUrl);
                        }
                    }
                });
                pool.start();
                
                super.onPageFinished(view, url);
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
        
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) 
        {  
            try
            {
                handler.proceed();  
            }
            catch(Throwable t)
            {
                Logger.log(this.getClass().getName(), t);
            }
        }
        
        private boolean checkNeedAnimation(String url)
        {
            int index = url.indexOf('?');
            
            if(index != -1){
                String queryString = url.substring(index + 1);
                
                int animationIndex = queryString.indexOf(ANIMATION_FLAG);
                
                if(animationIndex != -1){
                    return true;
                }
            }
            
            return false;
        }
    }

    public void loadUrl(String url, Map<String, String> extraHeaders)
    {
        if (url != null && url.toLowerCase().indexOf("orientation=portrait") != -1)
        {
            shouldLockOrientation = true;
            if (isShown())
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
            }
        }
        
        super.loadUrl(url, extraHeaders);
    }
    
    public void loadUrl(String url)
    {
        if (url != null && url.toLowerCase().indexOf("orientation=portrait") != -1)
        {
            shouldLockOrientation = true;
            if (isShown())
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
            }
        }
        
        super.loadUrl(url);
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        if (isConnected)
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    setNetworkAvailable(true);
                }
            });
        }
        else
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    setNetworkAvailable(false);
                }
            });
        }
    }
}

class LogDelegate implements ILogDelegate{
    
    private static final LogDelegate logDelegate = new LogDelegate();
    
    private LogDelegate(){
    }
    
    public static LogDelegate getInstance(){
        return logDelegate;
    }
    
    public void logInfoDelegate(String tag, String message, Throwable t) {
        Logger.log(Logger.INFO, tag, message, t, null);
    }
    
    public void logDebugDelegate(String tag, String message, Throwable t) {
        Logger.log(Logger.INFO, tag, message, t, null);
        
    }
    
    public void logWarnDelegate(String tag, String message, Throwable t) {
        Logger.log(Logger.WARNING, tag, message, t, null);
    }
    
    public void logErrorDelegate(String tag, String message, Throwable t) {
        Logger.log(Logger.ERROR, tag, message, t, null);
    }
}
