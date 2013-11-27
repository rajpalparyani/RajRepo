/**
 *
 * Copyright 2013 Telenav, Inc. All rights reserved.
 * TnAutoCompleteTextView.java
 *
 */
package com.telenav.ui.android;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * @author Fangquan Ming
 * @version Jun 13, 2013
 */
public class TnAutoCompleteTextView extends AutoCompleteTextView
{
    public static class TnSuggestionBean
    {
        public String label;

        public String search;

        public String desc;

        public Bitmap image;
        
        public int type;
        
        public boolean isGroup;
        
        public ArrayList<TnSuggestionBean> subBeans = new ArrayList<TnAutoCompleteTextView.TnSuggestionBean>();

        public boolean equals(Object o)
        {
            if (o instanceof TnSuggestionBean)
            {
                if (this.label.equals(((TnSuggestionBean) o).label) && this.desc.equals(((TnSuggestionBean) o).desc))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public interface TextCompletionListener
    {
        public String onCompletion(int viewId, String text);
    }

    private TextCompletionListener listener;

    public TnAutoCompleteTextView(Context context)
    {
        super(context);
    }

    public TnAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public TnAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setTextCompletionListener(TextCompletionListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void performFiltering(CharSequence text, int keyCode)
    {
        super.performFiltering(text, keyCode);
    }

    @Override
    public void replaceText(CharSequence text)
    {
        String t = text.toString();

        if (this.listener != null)
        {
            t = this.listener.onCompletion(this.getId(), t);
        }

        super.replaceText(t);
    }

    @Override
    public boolean enoughToFilter()
    {
        return true;
    }
}
