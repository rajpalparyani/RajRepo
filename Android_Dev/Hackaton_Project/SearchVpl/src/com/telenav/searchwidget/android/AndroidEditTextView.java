/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TnEditTextView.java
 *
 */
package com.telenav.searchwidget.android;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author bmyang
 * @date 2011-8-22
 */
public class AndroidEditTextView extends EditText
{

    /**
     * @param context
     */
    public AndroidEditTextView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AndroidEditTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    /**
     * @param context
     * @param attrs
     */
    public AndroidEditTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

  
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        // TODO Auto-generated method stub
        showVirtualKeyBoard();
        return super.onCreateInputConnection(outAttrs);
    }

    private void showVirtualKeyBoard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        try
        {
            inputMethodManager.showSoftInput(this,0); 
        }
        catch (Throwable t)
        {
            Log.w("set keyboard", "error!!!");
        }
    }

    
    
}
