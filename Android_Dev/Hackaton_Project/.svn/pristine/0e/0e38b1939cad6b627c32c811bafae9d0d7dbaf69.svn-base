/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CaptureHTMLInputViaJavascript.java
 *
 */
package com.telenav.demo.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-12-31
 */
public class CaptureHTMLInputViaJavascript
{
    protected Activity activity = null;
    
    public CaptureHTMLInputViaJavascript(Activity activity)
    {
        this.activity = activity;
        
        load();
    }
    
    protected void load()
    {
        WebView webview = new WebView(activity);
        webview.loadUrl("file:///android_asset/webview/test_js.html");
        
        //must set javascript enabled
        webview.getSettings().setJavaScriptEnabled(true);

        activity.setContentView(webview);
        
        webview.setWebChromeClient(new WebChromeClient() {
            
            public boolean onConsoleMessage(ConsoleMessage consoleMessage)
            {
                clientHandle(consoleMessage.message());
                return true;
            }
            
        });
        
    }
    
    protected void clientHandle(String message)
    {
        Builder builder = new Builder(activity);
        builder.setTitle("Capture Input via Javascript");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }
}
