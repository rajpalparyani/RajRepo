/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AdDoubleBuffer.java
 *
 */
package com.telenav.ui;

import android.view.ViewGroup;

import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenWebView;

/**
 *@author bduan
 *@date 2012-7-9
 */
public class DualCacheWebview 
{
    private int cacheSize = 2;
    private final static int LOAD_TIMEOUT = 60 * 1000;
    
    private int bgColor = 0;
    private int cacheIndex = -1;
    
    private String url = null;
    private IHtmlSdkServiceHandler handler = null;
    private Object mutex = new Object();
    private CacheReadyCallback cacheReadyCallback = null;
    private boolean cacheReadySent = false;
    
    private CitizenWebComponent [] cache = null;
    private boolean[] hasPageError = null;
    private boolean[] isPageLoading = null;
    private long[] lastLoadTime = null;
    private boolean isReleased = false;
    private boolean isDualMode = true;
    
    public DualCacheWebview(IHtmlSdkServiceHandler handler, String url, int bgColor, boolean isDualMode)
    {
        this(handler, url, bgColor);
        this.isDualMode = isDualMode;
        if(isDualMode)
        {
            cacheSize = 2;
        }
        else
        {
            cacheSize = 1;
        }
    }
    
    public DualCacheWebview(IHtmlSdkServiceHandler handler, String url, int bgColor)
    {
        this.url = url;
        this.bgColor = bgColor;
        this.handler = handler;
        
        init();
    }
    
    public static interface CacheReadyCallback
    {
        public void cacheReady(DualCacheWebview dualWebView, CitizenWebComponent readyWebView);
    }
    
    public boolean isCacheReady()
    {
        return cacheIndex >= 0;
    }
    
    /**
     * this method should be invoked in UI thread
     */
    public void release()
    {
        if (null == cache)
        {
            return;
        }
        for(int i = 0; i < cache.length ; i ++)
        {
            CitizenWebComponent webComponent = (CitizenWebComponent) cache[i];
            AndroidCitizenWebView webView = (AndroidCitizenWebView)webComponent.getNativeUiComponent();
            ViewGroup parent = null;
            if(webView.getParent() instanceof ViewGroup)
            {
                parent = (ViewGroup)webView.getParent();
            }
            if(parent != null)
            {
                parent.removeView(webView);
            }
            //webComponent.freeMemory();
            //webComponent.destroy();
            webComponent = null;
        }
        cache = null;
        
        isReleased = true;
    }
    
    public void setCacheReadyCallback(CacheReadyCallback cacheReadyCallback)
    {
        this.cacheReadyCallback = cacheReadyCallback;
    }
    
    public CitizenWebComponent getCachedWebComponent()
    {
        CitizenWebComponent tmp = null;
        
        if(cacheIndex < 0)
        {
            loadUrlWithNetworkCheck(0, url);
            tmp = cache[0];
        }
        else
        {
            tmp = cache[cacheIndex];
            
            if(isDualMode)
            {
                for(int i = 0; i < cache.length ; i ++)
                {
                    if(i != cacheIndex)
                    {                  
                        loadUrlWithNetworkCheck(i, url);
                    }
                }
            }
            else
            {
                loadUrlWithNetworkCheck(cacheIndex, url);
            }
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "Adjuggler db return cache : " + cacheIndex + " , " + System.currentTimeMillis());
        
        if(tmp.getParent() != null)
        {
            ((AbstractTnContainer)tmp.getParent()).remove(tmp);
        }
        
        return tmp;
    }
    
    protected boolean isLoadingURL(int index)
    {
        synchronized (mutex)
        {
            if(isPageLoading[index])
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Adjuggler db page " + index + " is loading ... "  + " , " + System.currentTimeMillis());
                if(lastLoadTime[index] > 0 && System.currentTimeMillis() - lastLoadTime[index] < LOAD_TIMEOUT)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "Adjuggler db loading time out , lastLoadTime : " + lastLoadTime[index] + " , " + System.currentTimeMillis());
                    return true;
                }
            }
            return false;
        }
    }
    
    protected void loadUrlWithNetworkCheck(final int index, final String url)
    {
        synchronized (mutex)
        {
            if (!isLoadingURL(index) && (NetworkStatusManager.getInstance().isConnected()))
            {
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "Adjuggler db start load : " + index + " , " + System.currentTimeMillis() + " url=" + url);
                if (!cache[index].isUpdateContent(url))
                {
                    lastLoadTime[index] = System.currentTimeMillis();
                    hasPageError[index] = false;
                    isPageLoading[index] = true;
                }
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        cache[index].loadUrl(url);
                    }
                });

            }
        }        
    }
    
    protected void init()
    {
        cache = new CitizenWebComponent[cacheSize];
        hasPageError = new boolean[cacheSize];
        isPageLoading = new boolean[cacheSize];
        lastLoadTime = new long[cacheSize];
        
        for(int i = 0 ; i < cache.length ; i ++)
        {
            cache[i] = createWebComponent(i);
            hasPageError[i] = false;
            isPageLoading[i] = false;
            lastLoadTime[i] = -1L;
        }
    }

    protected CitizenWebComponent createWebComponent(final int index)
    {
        CitizenWebComponent adWebComponent = UiFactory.getInstance().createCitizenWebComponent(null, 0, null, false);
        adWebComponent.setBackgroundColor(bgColor);
        adWebComponent.setFocusable(false);
        adWebComponent.setWebBrowserEventListener(new TnWebBrowserField.WebBrowserEventListener()
        {
            public void onPageError(TnWebBrowserField browserField, String errorMsg)
            {
                synchronized (mutex)
                {
                    hasPageError[index] = true;
                    isPageLoading[index] = false;
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "Adjuggler db upgrade page error : " + errorMsg + " index = " + index + " , " + System.currentTimeMillis());
            }

            public void onPageFinished(final TnWebBrowserField browserField, String url)
            {
                //Don't set isPageError to false, since onPageFinish will be called twice when met page error.
                Logger.log(Logger.INFO, this.getClass().getName(), "Adjuggler db " + index + " load finish isPageError = " + hasPageError[index] + " , " + System.currentTimeMillis());
                isPageLoading[index] = false;
                
                if(!hasPageError[index])
                {
                    synchronized (mutex)
                    {
                        
                        if(isReleased)
                        {
                            return;
                        }
                        
                        ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if(isReleased)
                                {
                                    return;
                                }
                                
                                if (index == 0 && !cacheReadySent)
                                {
                                    // cache ready callback is not sent yet, send it
                                    cacheReadySent = true;
                                    if (cacheReadyCallback != null)
                                    {
                                        cacheReadyCallback.cacheReady(DualCacheWebview.this,
                                            (CitizenWebComponent) browserField);
                                    }
                                    
                                    //try to load new content that will be ready for nex use if the content is loaded successfully for the first time.
                                    if(isDualMode)
                                    {
                                        loadUrlWithNetworkCheck(1, DualCacheWebview.this.url);
                                    }
                                }
                                
                                Logger.log(Logger.INFO, this.getClass().getName(), "Adjuggler db " + index + " is ready to fire " + System.currentTimeMillis());
                                cacheIndex = index;
                            }
                        });
                    }
                }
            }
        });
        adWebComponent.setHtmlSdkServiceHandler(handler);
        
        return adWebComponent;
    }
}
