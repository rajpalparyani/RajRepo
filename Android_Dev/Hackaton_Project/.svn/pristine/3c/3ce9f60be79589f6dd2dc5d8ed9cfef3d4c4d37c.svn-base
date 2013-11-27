/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * WebViewDemo.java
 *
 */
package com.telenav.demo.webview;

import java.net.URLDecoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-12-29
 */
public class CaptureHTMLInput
{
    protected Activity activity = null;
    
    public CaptureHTMLInput(Activity activity)
    {
        this.activity = activity;
        
        load();
    }
    
    protected void load()
    {
        WebView webview = new WebView(activity);
        webview.loadUrl("file:///android_asset/webview/test.html");

        activity.setContentView(webview);

        webview.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                url = URLDecoder.decode(url);
                parseInputText(url);
                return true; // then it is not handled by default action
            }

        });
    }
    
    void parseInputText(String text)
    {
        StringBuffer message = new StringBuffer();
        
        String temp = text.substring(text.indexOf("?") + 1);
        
        String[] params = temp.split("&");
        if (params != null && params.length > 0)
        {
            for (int i = 0; i < params.length; i ++)
            {
                message.append(params[i]);
                message.append('\n');
            }
        }
        
        Builder builder = new Builder(activity);
        builder.setTitle("Capture HTML Input");
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
