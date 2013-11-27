/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WebViewCacheManager.java
 *
 */
package com.telenav.data.cache;

import java.util.Vector;

import com.telenav.cache.AbstractCache;
import com.telenav.cache.LRUCache;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenWebComponent;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-3-2
 */
public class WebViewCacheManager
{
    private static WebViewCacheManager instance = new WebViewCacheManager();
    
    private AbstractCache webViewCache = new LRUCache(10);
    
    private CitizenWebComponent[] poiDetailWebView = new CitizenWebComponent[3];
    
    private boolean isLoaded = false;
    
    private WebViewCacheManager()
    {
        initPoiDetailView();
    }
    
    private synchronized void initPoiDetailView()
    {
        for(int i = 0; i < poiDetailWebView.length; i++)
        {
            poiDetailWebView[i] = UiFactory.getInstance().createCitizenWebComponent(null, 0, false);
        }
    }
    
    public static WebViewCacheManager getInstance()
    {
        return instance;
    }
    
    public boolean containsKey(String key)
    {
        return webViewCache.containsKey(key);
    }
    
    public CitizenWebComponent getWebView(String key, int id)
    {
       
        CitizenWebComponent webComponent = null;

        if (webViewCache.containsKey(key))
        {
            webComponent = (CitizenWebComponent) webViewCache.get(key);
            webComponent.setId(id);
        }
        else
        {
            webComponent = UiFactory.getInstance().createCitizenWebComponent(null, id, false);
            webComponent.initDefaultZoomDensity();
        }
		
        if (webComponent.getParent() instanceof AbstractTnContainer)
        {
            AbstractTnContainer parent = (AbstractTnContainer) webComponent.getParent();
            parent.remove(webComponent);
        }
        
        return webComponent;
    }
    
    public CitizenWebComponent getWebView(String key, int id, boolean isFreeMemory)
    {
        CitizenWebComponent webComponent =  getWebView(key, id);
        if(isFreeMemory)
        {
            webComponent.freeMemory();
        }
        return webComponent;
    }
    
    public Object remove(String key)
    {
    	return webViewCache.remove(key);
    }
    
    public void add(String key, CitizenWebComponent webComponent)
    {
        webViewCache.put(key, webComponent);
    }
    
    public void clearAll()
    {
        Vector v = webViewCache.elements();
        if(v != null && v.size() > 0)
        {
            for(int i = 0; i < v.size(); i++)
            {
                CitizenWebComponent webComponent = (CitizenWebComponent) v.elementAt(i);
                if(webComponent.getParent() != null)
                {
                    ((AbstractTnContainer)webComponent.getParent()).remove(webComponent);
                }
                webComponent.freeMemory();
                webComponent.destroy();
            }
        }
        webViewCache.clear();
    }
    
    public synchronized CitizenWebComponent getPoiDetailWebview(int index, int id)
    {
        if(index < 0)
        {
            return null;
        }
        CitizenWebComponent webView = poiDetailWebView[index % poiDetailWebView.length];
        webView.setId(id);
        if(webView.getParent() instanceof AbstractTnContainer)
        {
            ((AbstractTnContainer)webView.getParent()).remove(webView);
        }
        
        return webView;
    }
    
    public synchronized CitizenWebComponent getPoiDetailWebview(int index)
    {
        if(index < 0)
        {
            return null;
        }
        
        CitizenWebComponent webView = poiDetailWebView[index % poiDetailWebView.length];
        
        return webView;
    }
    
    public synchronized void clearPoiDetailWebview(int index)
    {
        if(index < 0)
        {
            return;
        }
        
        poiDetailWebView[index % poiDetailWebView.length] = UiFactory.getInstance().createCitizenWebComponent(null, 0, false);
    }
    
    public synchronized void clearPoiDetailWebview()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                final int length = poiDetailWebView.length;
                
                final CitizenWebComponent[] tmp = new CitizenWebComponent[length];
                for(int i = 0; i < length; i++)
                {
                    tmp[i] = poiDetailWebView[i];
                }
                
                initPoiDetailView();
                
                for(int i = 0; i < length; i++)
                {
                    try
                    {
                        if(tmp[i].getParent() instanceof AbstractTnContainer)
                        {
                            ((AbstractTnContainer)tmp[i].getParent()).remove(tmp[i]);
                        }
                        //tmp[i].freeMemory();
                        //tmp[i].destroy();
                        tmp[i] = null;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    public void setIsLoaded(boolean isLoaded)
    {
        this.isLoaded = isLoaded;
    }
    
    public boolean isLoaded()
    {
        return isLoaded;
    }
}
