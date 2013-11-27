package com.telenav.demo;

import java.util.ArrayList;
import java.util.List;

import com.telenav.demo.webview.CaptureHTMLInputViaJavascript;
import com.telenav.demo.webview.CaptureHTMLInputViaLocalJavascript;
import com.telenav.demo.webview.CaptureHTMLInputViaWebViewJavascriptInterface;
import com.telenav.demo.webview.CaptureHTMLInput;
import com.telenav.demo.webview.WebViewTest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Demo extends Activity {
    
    protected ListView listview = null;
    protected boolean isRootScreen = false;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRootScreen = true;
        listview = new ListView(this);
        final List<String> listItem = getData();
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listItem));
        setContentView(listview);
        
        listview.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                String item = (String) listItem.get(position);
//                setTitle("Click " + item);
                isRootScreen = false;
                if ("CaptureHTMLInput".equals(item))
                {
                    new CaptureHTMLInput(Demo.this);
                }
                else if ("CaptureHTMLInputViaJavascript".equals(item))
                {
                    new CaptureHTMLInputViaJavascript(Demo.this);
                }
                else if ("CaptureHTMLInputViaLocalJavascript".equals(item))
                {
                    new CaptureHTMLInputViaLocalJavascript(Demo.this);
                }
                else if ("CaptureHTMLInputViaWebViewJavascriptInterface".equals(item))
                {
                    new CaptureHTMLInputViaWebViewJavascriptInterface(Demo.this);
                }
            }
        });
        
        //System.out.println("Qli -----> "+System.getProperty("user.dir"));

    }
    
    public void onBackPressed()
    {
        if (!isRootScreen)
        {
            isRootScreen = true;
            setContentView(listview);
            return;
        }
        super.onBackPressed();
    }
    
    
    protected List<String> getData()
    {
        List<String> data = new ArrayList<String>();
        
        data.add("CaptureHTMLInput");
        data.add("CaptureHTMLInputViaJavascript");
        data.add("CaptureHTMLInputViaLocalJavascript");
        data.add("CaptureHTMLInputViaWebViewJavascriptInterface");
        
        return data;
    }
}