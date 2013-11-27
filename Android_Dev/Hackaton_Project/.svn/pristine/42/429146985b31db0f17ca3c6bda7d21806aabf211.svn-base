/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CaptureHTMLInputViaWebViewJavascriptInterface.java
 *
 */
package com.telenav.demo.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Handler;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 *@author qli (qli@telenav.cn)
 *@date 2011-12-31
 *
 *
 *public void addJavascriptInterface (Object obj, String interfaceName)
 *
 *Use this function to bind an object to JavaScript so that the methods can be accessed from JavaScript.
 *
 *IMPORTANT:
 *
 * a) Using addJavascriptInterface() allows JavaScript to control your application. This can be a very useful feature or a dangerous security issue. When the HTML in the WebView is untrustworthy (for example, part or all of the HTML is provided by some person or process), then an attacker could inject HTML that will execute your code and possibly any code of the attacker's choosing.
 *    Do not use addJavascriptInterface() unless all of the HTML in this WebView was written by you.
 * b) The Java object that is bound runs in another thread and not in the thread that it was constructed in.
 *
 *Parameters
 *  obj               The class instance to bind to JavaScript, null instances are ignored.
 *  interfaceName     The name to used to expose the instance in JavaScript. 
 */
public class CaptureHTMLInputViaWebViewJavascriptInterface
{
    protected Activity activity = null;
    
    protected WebView webview = null;
    
    protected Handler handler = null;
    
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


    
    public CaptureHTMLInputViaWebViewJavascriptInterface(Activity activity)
    {
        this.activity = activity;
        this.handler = new Handler();
        load();
    }
    
    protected void load()
    {
        webview = new WebView(activity);
        webview.loadUrl("file:///android_asset/webview/test_js_interface.html");

        activity.setContentView(webview);

        webview.getSettings().setJavaScriptEnabled(true);
        
        
        webview.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");

        
        webview.setWebChromeClient(new WebChromeClient() {
            
            public boolean onConsoleMessage(ConsoleMessage consoleMessage)
            {
                clientHandle(consoleMessage.message());
                return true;
            }
            
        });
    }
    
    final class DemoJavaScriptInterface {

        DemoJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        public void clickOnAndroid() {
            handler.post(new Runnable() {
                public void run() {
                    webview.loadUrl(jsCode);
                }
            });

        }
    }
    

    protected void clientHandle(String message)
    {
        Builder builder = new Builder(activity);
        builder.setTitle("Capture Input via Javascript Interface");
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
