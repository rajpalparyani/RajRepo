/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AdJuggerManager.java
 *
 */
package com.telenav.module.dashboard;

import android.view.View;
import android.view.ViewGroup;

import com.telenav.app.CommManager;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.DualCacheWebview;
import com.telenav.ui.DualCacheWebview.CacheReadyCallback;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenWebView;

/**
 *@author bduan
 *@date 2012-7-11
 */
public class AdJuggerManager extends BrowserSdkModel implements CacheReadyCallback
{
    private int screenBackgroundColor = UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR);
    private DualCacheWebview dualCacheWebview = null;
        
    private static class InnerAdJuggerManager
    {
        static AdJuggerManager instance = new AdJuggerManager();
    }
    
    private AdJuggerManager()
    {
        dualCacheWebview  = new DualCacheWebview(this, getAdJugglerUrl(), 0x00000000, false);
    }
    
    public static AdJuggerManager getInstance()
    {
        return InnerAdJuggerManager.instance;
    }
    
    public void addAdjugglerView(AbstractTnContainer container, IHtmlSdkServiceHandler handler)
    {
        CitizenWebComponent component = dualCacheWebview.getCachedWebComponent();
        //for some device such as LG LS970 ECLIPSE, os will occured a null point exception when deattched from window
        //disable the long click for ad juggler
        component.setDisableLongClick(true);
        component.setHtmlSdkServiceHandler(handler);
        component.setLayerType(CitizenWebComponent.LAYER_TYPE_SOFTWARE);
        container.add(component);
        
        if (dualCacheWebview.isCacheReady())
        {
            container.setVisible(true);
            dualCacheWebview.setCacheReadyCallback(null);
        }
        else
        {
            container.setVisible(false);
            dualCacheWebview.setCacheReadyCallback(this);
        }
    }   
    
    public void addAdjugglerView(ViewGroup viewGroup, IHtmlSdkServiceHandler handler)
    {
        CitizenWebComponent component = dualCacheWebview.getCachedWebComponent();
        //for some device such as LG LS970 ECLIPSE, os will occured a null point exception when deattched from window
        //disable the long click for ad juggler
        component.setDisableLongClick(true);
        component.setHtmlSdkServiceHandler(handler);
        component.setLayerType(CitizenWebComponent.LAYER_TYPE_SOFTWARE);
        
        AndroidCitizenWebView webView = (AndroidCitizenWebView)component.getNativeUiComponent();
        
        if(webView != null)
        {
            if(webView.getParent() instanceof ViewGroup)
            {
                ((ViewGroup)webView.getParent()).removeView(webView);
            }
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            viewGroup.addView(webView, layoutParam);
            webView.setBackgroundColor(0x00000000);
            
            if (dualCacheWebview.isCacheReady())
            {
                webView.setVisibility(View.VISIBLE);
                dualCacheWebview.setCacheReadyCallback(null);
            }
            else
            {
                webView.setVisibility(View.GONE);
                dualCacheWebview.setCacheReadyCallback(this);
            }
        }
    }  
    
    public void reload()
    {
        dualCacheWebview.getCachedWebComponent();
    }
    
    public void reset()
    {
        if (null != dualCacheWebview)
        {
            dualCacheWebview.release();
        }
        dualCacheWebview = new DualCacheWebview(this, getAdJugglerUrl(), screenBackgroundColor, false);
    }
    
    private String getAdJugglerUrl()
    {
        BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.AD_JUGGLER_DOMAIN_ALIAS);
        String url = sessionArgs.getUrl();
        if(url == null || url.length() == 0)
        {
            return "";
        }
        url = BrowserSdkModel.addEncodeTnInfo(sessionArgs.getUrl(), "");
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        return url;
    }

    public void cacheReady(DualCacheWebview dualWebView, CitizenWebComponent readyWebView)
    {
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        
        if(!isOnboard)
        {
            final AndroidCitizenWebView webView = (AndroidCitizenWebView) readyWebView.getNativeUiComponent();

            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    webView.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    
    public boolean isCacheReady()
    {
        if (dualCacheWebview != null)
        {
            return dualCacheWebview.isCacheReady();
        }
        
        return false;
    }
}
