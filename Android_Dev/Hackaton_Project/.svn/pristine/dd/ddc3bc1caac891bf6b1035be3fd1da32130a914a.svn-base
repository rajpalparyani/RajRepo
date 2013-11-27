/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CaptureHTMLInputViaLocalJavascript.java
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
import android.webkit.WebViewClient;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-12-31
 *
 */
public class CaptureHTMLInputViaLocalJavascript
{
    protected Activity activity = null;
    
    protected String jsCode = "javascript:"
            + "var input = document.getElementsByName(\"fname\")[0].value;"
            + "input += \"\\n\";"
            + "input += document.getElementsByName(\"lname\")[0].value;"
            + "input += \"\\n\";"
            + "input += document.getElementsByName(\"psw\")[0].value;"
            + "input += \"\\n\";"
            + "input += document.getElementsByName(\"Sex\")[0].value;"
            + "var boxes = document.getElementsByName(\"checkBoxName\");"
            + "for (var i = 0; i < boxes.length; i++)" 
            + "{"
            + "    if (boxes[i].checked)" 
            + "    {" 
            + "        input += \"\\n\";"
            + "        input += boxes[i].value;" 
            + "    }" 
            + "}" 
            + "input += \"\\n\";"
            + "input += document.getElementsByName(\"cars\")[0].value;"
            + "console.log(input);";

    
    public CaptureHTMLInputViaLocalJavascript(Activity activity)
    {
        this.activity = activity;
        
        load();
    }
    
    protected void load()
    {
        final WebView webview = new WebView(activity);
        webview.loadUrl("file:///android_asset/webview/test.html");

        activity.setContentView(webview);

        webview.getSettings().setJavaScriptEnabled(true);
//        webview.addJavascriptInterface(null, "jsi");

        webview.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                webview.loadUrl(jsCode);
                return true; // then it is not handled by default action
            }

        });
        
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
