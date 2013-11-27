/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnWebBrowserField.java
 *
 */
package com.telenav.tnui.widget;

import java.util.Hashtable;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * The BrowserField class is used to embed web content within a Java application. This field will take up the dimensions
 * of web content rendered within it. However, it does not provide the ability to scroll implicitly. Instead it is the
 * TnWebBrowserField's container's responsibility to implement scrolling.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-1
 */
public class TnWebBrowserField extends AbstractTnComponent
{
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_LOAD_DATA = 50000001;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_LOAD_URL = 50000002;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_RELOAD = 50000003;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_GET_URL = 50000004;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_GET_CONFIG = 50000005;
    
    /**
     * <b>Call Native Method</b> <br />
     */
    public final static int METHOD_SET_CONFIG = 50000006;
    
    protected WebBrowserEventListener browserEventlistener;
    
    /**
     * Creates a new TnWebBrowserField.
     *  
     * @param id
     */
    public TnWebBrowserField(int id)
    {
        super(id);
    }

    /**
     * This method will display the provided HTML content in this TnWebBrowserField instance. Note: this call does not
     * affect the TnWebBrowserField's history.
     * 
     * @param html The HTML content to be displayed
     * @param baseUrl The URL identifying this content. This will be used to resolve relative urls.
     */
    public void loadData(String html, String baseUrl)
    {
        this.nativeUiComponent.callUiMethod(METHOD_LOAD_DATA, new Object[]{html, baseUrl});
    }

    /**
     * This method will display the byte[] content in this TnWebBrowserField instance using the specified content type.
     * Note: this call does not affect the TnWebBrowserField's history.
     * 
     * @param data The content to be displayed
     * @param contentType The MIMEType of the data. i.e. text/html, image/jpeg
     * @param baseUrl The URL identifying this content. This will be used to resolve relative urls.
     */
    public void loadData(byte[] data, String contentType, String baseUrl)
    {
        this.nativeUiComponent.callUiMethod(METHOD_LOAD_DATA, new Object[]{data, contentType, baseUrl});
    }

    /**
     * Load the given url.
     * 
     * @param url The url of the resource to load. 
     */
    public void loadUrl(String url)
    {
        this.nativeUiComponent.callUiMethod(METHOD_LOAD_URL, new Object[]{url});
    }

    /**
     * Load the given url with the extra headers.
     * 
     * @param url The url of the resource to load.
     * @param headers The extra headers sent with this url. This should not include the common headers like "user-agent".
     */
    public void loadUrl(String url, Hashtable headers)
    {
        this.nativeUiComponent.callUiMethod(METHOD_LOAD_URL, new Object[]{url, headers});
    }

    /**
     * Load the url with postData using "POST" method into the WebView.
     * 
     * @param url The url of the resource to load.
     * @param postData The data will be passed to "POST" request. 
     */
    public void loadUrl(String url, byte[] postData)
    {
        this.nativeUiComponent.callUiMethod(METHOD_LOAD_URL, new Object[]{url, postData});
    }

    /**
     * Reload the current url. 
     */
    public void reload()
    {
        this.nativeUiComponent.callUiMethod(METHOD_RELOAD, null);
    }

    /**
     * Get the url for the current page.
     * 
     * @return The url for the current page. 
     */
    public String getUrl()
    {
        
        return (String)this.nativeUiComponent.callUiMethod(METHOD_GET_URL, null);
    }
    
    /**
     * Return the TnWebBrowserFieldConfig object used to control the settings for this component.
     * 
     * @return TnWebBrowserFieldConfig
     */
    public TnWebBrowserFieldConfig getConfig()
    {
        return (TnWebBrowserFieldConfig)this.nativeUiComponent.callUiMethod(METHOD_GET_CONFIG, null);
    }

    /**
     * This method will register a IWebBrowserEventListener to be notified for various events pertaining to this
     * instance of a TnWebBrowserField.
     * 
     * @param listener the IWebBrowserEventListener instance that will be notified when various events occur in this
     *            TnWebBrowserField
     */
    public void setWebBrowserEventListener(WebBrowserEventListener listener)
    {
        this.browserEventlistener = listener;
    }
    
    /**
     * This method will return the listener registered on this TnWebBrowserField. 
     * 
     * @return the listener.
     */
    public WebBrowserEventListener getWebBrowserEventListener()
    {
        return this.browserEventlistener;
    }
    
    protected void initDefaultStyle()
    {

    }

    protected void paint(AbstractTnGraphics graphics)
    {

    }

    /**
     * Manages settings state for a TnWebBrowserField. If a TnWebBrowserField has been destroyed, any method call on
     * WebSettings will throw an IllegalStateException.
     * 
     * @author fqming
     * 
     */
    public static class TnWebBrowserFieldConfig
    {
        private String userAgent;
        private boolean supportZoom;
        private boolean jsEnabled;
        private TnWebBrowserField field;
        
        /**
         * create a config.
         * 
         * @param field TnWebBrowserField
         */
        public TnWebBrowserFieldConfig(TnWebBrowserField field)
        {
            this.field = field;
        }
        
        /**
         * Set the TnWebBrowserField's user-agent string. If the string "userAgent" is null or empty, it will use the system default user-agent string. 
         * 
         * @param userAgent
         */
        public void setUserAgent(String userAgent)
        {
            this.userAgent = userAgent;
            update();
        }

        /**
         * Return the TnWebBrowserField's user-agent string.
         * 
         * @return user-agent
         */
        public String getUserAgent()
        {
            return this.userAgent;
        }

        /**
         * Set whether the TnWebBrowserField supports zoom 
         * 
         * @param supportZoom true support, otherwise false.
         */
        public void setSupportZoom(boolean supportZoom)
        {
            this.supportZoom = supportZoom;
            update();
        }
        
        /**
         * Returns whether the TnWebBrowserField supports zoom 
         * 
         * @return true support, otherwise false.
         */
        public boolean isSupportZoom()
        {
            return this.supportZoom;
        }
        
        /**
         * Tell the WebView to enable javascript execution.
         * 
         * @param flag True if the WebView should execute javascript. 
         */
        public void setJavaScriptEnabled(boolean flag)
        {
            this.jsEnabled = flag;
            update();
        }
        
        /**
         * Return true if javascript is enabled. Note: The default is false.
         * 
         * @return True if javascript is enabled. 
         */
        public boolean isJavaScriptEnabled()
        {
            return this.jsEnabled;
        }
        
        private void update()
        {
            if(this.field.getNativeUiComponent() != null)
            {
                this.field.getNativeUiComponent().callUiMethod(METHOD_SET_CONFIG, null);
            }
        }
    }
    
    /**
     * Callback to handle kinds of the events of TnWebBrowserField.
     * 
     * @author fqming
     *
     */
    public static class WebBrowserEventListener
    {
        /**
         * Notify the host application of a change in the document title.
         * 
         * @param browserField The TnWebBrowserField that initiated the callback.
         * @param title A String containing the new title of the document.
         */
        public void onReceiveTitle(TnWebBrowserField browserField, String title)
        {
            
        }

        /**
         * Report an error to the host application. These errors are unrecoverable (i.e. the main resource is
         * unavailable).
         * 
         * @param browserField The TnWebBrowserField that initiated the callback.
         * @param errorMsg A String containing the error information.
         */
        public void onPageError(TnWebBrowserField browserField, String errorMsg)
        {
            
        }

        /**
         * Notify the host application that a page has finished loading. This method is called only for main frame. When
         * onPageFinished() is called, the rendering picture may not be updated yet. To get the notification for the
         * 
         * @param browserField browserField The TnWebBrowserField that initiated the callback.
         * @param url The url of the page.
         */
        public void onPageFinished(TnWebBrowserField browserField, String url)
        {
            
        }
    }
}
