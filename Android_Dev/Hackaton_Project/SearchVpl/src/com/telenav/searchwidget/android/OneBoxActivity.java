/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * OneBoxActivity.java
 *
 */
package com.telenav.searchwidget.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.telenav.app.android.cingular.R;
import com.telenav.searchwidget.app.android.SearchApp;
import com.telenav.searchwidget.flow.android.ActionHandler;
import com.telenav.searchwidget.flow.android.IWidgetConstants;
import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.res.ResUtil;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 27, 2011
 */

public class OneBoxActivity extends Activity implements BackEventListener, OnClickListener, TextWatcher, OnEditorActionListener
{
    private EditText inputView;
    private ImageView searchButton;
    private ImageView cancelButton;
    private int widgetId;
    private int layoutId;
    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        SearchApp.getInstance().init(getApplicationContext());

        widgetId = this.getIntent().getIntExtra(IWidgetConstants.KEY_WIDGET_ID, -1);
        layoutId = this.getIntent().getIntExtra(IWidgetConstants.KEY_LAYOUT_ID, -1);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);        
        this.setContentView(R.layout.searchwidget_onebox_search);
        
        CatchBackLineaLayout catchBackLineaLayout = (CatchBackLineaLayout)this.findViewById(R.id.layout);
        catchBackLineaLayout.setBackEventListener(this);

        inputView = (EditText) this.findViewById(R.id.input);
        inputView.setHint(ResUtil.getOneboxHint());
        inputView.addTextChangedListener(this);
        inputView.setOnEditorActionListener(this);
        
        searchButton = (ImageView)this.findViewById(R.id.search);
        searchButton.setEnabled(false);
        searchButton.setOnClickListener(this);
        
        cancelButton = (ImageView)this.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(this);
    }

	public void handleBackEvent(boolean isUp)
    {
        if(isUp)
        {
            finish();
        }
        else
        {
            this.hideInputMethod();
        }
    }
	
	protected void onPause()
    {
        super.onPause();
        hideInputMethod();
    }
    
    protected void hideInputMethod()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
        {
            imm.hideSoftInputFromWindow(inputView.getWindowToken(), 0);
        }
    }

    public void onClick(View v)
    {
        if ( v.getId() == R.id.search)
        {
            finish();
            
            TextView tv = (TextView)inputView;
            String term = tv.getText().toString();
            hideInputMethod();
            WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_ONEBOX_SEARCH);
            wp.putString(IWidgetConstants.KEY_TERM, term);
            new ActionHandler().execute(wp);
        }
        else if (v.getId() == R.id.cancel)
        {
            inputView.setText("");
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

    public void afterTextChanged(Editable s)
    {
        TextView tv = (TextView)inputView;
        String str = tv.getText().toString();
        
        if (str != null && str.length() > 0)
        {
            searchButton.setEnabled(true);
            cancelButton.setVisibility(View.VISIBLE);
        }
        else
        {
            searchButton.setEnabled(false);
            cancelButton.setVisibility(View.GONE);
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (null != event && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
        {
            String str = v.getText().toString();
            if (str != null && str.length() > 0)
            {
                onClick(searchButton);
            }
        }
        return false;
    }

}
