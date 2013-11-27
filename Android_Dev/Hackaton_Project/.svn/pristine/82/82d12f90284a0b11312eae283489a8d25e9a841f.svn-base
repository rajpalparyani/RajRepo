/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfReceiverGridContainer.java
 *
 */
package com.telenav.ui.android;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.BaseAdapter;

import com.telenav.app.android.scout_us.R;
import com.telenav.module.dwf.ContactUtil;
import com.telenav.sdk.kontagent.IKontagentConstants;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.ui.android.TnAutoCompleteTextView.TextCompletionListener;
import com.telenav.ui.android.TnAutoCompleteTextView.TnSuggestionBean;

/**
 * @author fangquanm
 * @date Jul 1, 2013
 */
public class DwfReceiverGridContainer extends FlowLayout implements TextWatcher, OnClickListener, OnKeyListener,
        TextCompletionListener, OnItemClickListener, OnEditorActionListener
{
    public interface IDwfReceiverGridListener
    {
        public void updateGridItems(ArrayList<TnSuggestionBean> items);
    }
    
    private String textBeforeChanged = "";

    private TnAutoCompleteTextView textView;

    private IDwfReceiverGridListener listener;
    
    public DwfReceiverGridContainer(Context context)
    {
        super(context);

        init(context);
    }

    public DwfReceiverGridContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context);
    }

    private void init(Context context)
    {
        this.setClickable(true);
    }

    public void setListener(IDwfReceiverGridListener listener)
    {
        this.listener = listener;
    }
    
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        if (textView == null)
        {
            textView = (TnAutoCompleteTextView) this.getChildAt(0);

            textView.addTextChangedListener(this);
            textView.setOnKeyListener(this);
            textView.setTextCompletionListener(this);
            textView.setOnItemClickListener(this);
            textView.setOnClickListener(this);
            textView.setClickable(true);
            textView.setOnEditorActionListener(this);
            textView.setOnFocusChangeListener(new OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View v, boolean hasFocus)
                {
                    if (!hasFocus)
                    {
                        String text = ((TnAutoCompleteTextView)v).getText().toString();
                        addItem(text, text, null);
                        textView.replaceText("");
                    }
                    else
                    {
                        textView.performFiltering("", 0);
                        textView.showDropDown();
                    }
                }
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        this.textBeforeChanged = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        String text = s.toString();
        if (text.length() > 2 && isNumeric(text.substring(0, text.length() - 1).trim())
                && (text.endsWith(" ") || text.endsWith(",") || text.endsWith(";")))
        {
            this.textView.removeTextChangedListener(this);
            this.textView.setText("");
            this.textView.addTextChangedListener(this);

            text = text.substring(0, text.length() - 1).trim();
            addItem(text, text, null);
            showDropDown();
        }
        else if (isNumeric(text.trim()))
        {
            notifyItemChange();
        }
    }

    private static boolean isNumeric(String str)
    {
        return str.matches("[+-]?\\d+");
    }
    
    public void addItem(String label, String desc, Bitmap b)
    {
        if(label == null || label.trim().length() == 0)
            return;
        this.textBeforeChanged = "";
        int index = this.indexOfChild(this.textView);
        DwfReceiverGridItem item = new DwfReceiverGridItem(this.getContext());
        item.setItem(label.trim(), desc.trim(), b);
        this.addView(item, index);
        
        notifyItemChange();
    }

    public void removeViewAt(int index)
    {
        super.removeViewAt(index);
        
        notifyItemChange();
    }
    
    public void removeView(View view)
    {
        super.removeView(view);
        
        notifyItemChange();
    }
    
    public void removeAllViews()
    {
        ArrayList<View> deleteViews = new ArrayList<View>();
        int index = this.indexOfChild(this.textView);
        for(int i = 0; i < this.getChildCount(); i++)
        {
            if(i == index)
                continue;
            deleteViews.add(this.getChildAt(i));
        }
        
        for(View v : deleteViews)
        {
            removeView(v);
        }
        
        notifyItemChange();
    }
    
    private void notifyItemChange()
    {
        ArrayList<TnSuggestionBean> items = new ArrayList<TnSuggestionBean>();
        for(int i = 0; i < this.getChildCount(); i++)
        {
            View v = this.getChildAt(i);
            if(v instanceof DwfReceiverGridItem)
            {
                DwfReceiverGridItem vItem = (DwfReceiverGridItem)v;
                TnSuggestionBean item = new TnSuggestionBean();
                
                item.label = vItem.getLabel();
                item.desc = vItem.getDesc();
                item.image = vItem.getBitmap();
                
                items.add(item);
            }
        }
        
        if(ContactUtil.isNumeric(this.textView.getText().toString()))
        {
            TnSuggestionBean item = new TnSuggestionBean();
            item.label = this.textView.getText().toString();
            item.desc = this.textView.getText().toString();
            items.add(item);
        }
        
        if(items.size() > 0)
        {
            textView.setHint("");
        }
        else 
        {
            textView.setHint(R.string.dwfInviteFriendsHint);
        }
        if(this.listener != null)
        {
            this.listener.updateGridItems(items);
        }
    }
    
    @Override
    public void afterTextChanged(Editable s)
    {

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        showDropDown();
        return super.onTouchEvent(ev);
    }

    private void showDropDown()
    {
        if(textView != null)
        {
            this.textView.post(new Runnable()
            {
                public void run()
                {
                    textView.requestFocus();
                   
                    if(textView.getAdapter().getCount() > 0)
                    {
                        textView.showDropDown();
                    }
                    InputMethodManager lManager = (InputMethodManager) textView.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(textView, 0);
                }
            });
        }
    }
    
    private void hideDropDown()
    {
        if (textView != null)
        {
            this.textView.post(new Runnable()
            {
                public void run()
                {
                    textView.dismissDropDown();
                    
                    InputMethodManager lManager = (InputMethodManager) textView.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                    lManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                }
            });
        }
    }
    

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_DEL))
        {
            if (textView.getText().toString().length() == 0)
            {
                if (this.textBeforeChanged.length() > 0)
                {
                    this.textBeforeChanged = "";
                }
                else
                {
                    for (int i = 0; i < this.getChildCount(); i++)
                    {
                        View child = this.getChildAt(i);
                        if (child instanceof DwfReceiverGridItem)
                        {
                            DwfReceiverGridItem gridItem = (DwfReceiverGridItem) child;
                            if (gridItem.isShowPopup())
                            {
                                gridItem.hidePopup();
                                return true;
                            }
                        }
                    }
                    if (textView.getText().toString().length() == 0)
                    {
                        int index = this.indexOfChild(this.textView);
                        if (index > 0)
                        {
                            this.removeViewAt(index - 1);

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String onCompletion(int viewId, String text)
    {
        return "";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Object o = ((BaseAdapter) parent.getAdapter()).getItem(position);
        if (o instanceof TnSuggestionBean)
        {
            TnSuggestionBean bean = (TnSuggestionBean) o;
            if (bean != null)
            {
                if(bean.isGroup)
                {
                    KontagentLogger.getInstance().addCustomEvent(IKontagentConstants.CATEGORY_DWF, IKontagentConstants.DWF_GROUP_INVITE_CLICKED);
                }
                if(bean.subBeans != null && !bean.subBeans.isEmpty())
                {
                    for(TnSuggestionBean subBean : bean.subBeans)
                    {
                        addItem(subBean.label, subBean.desc, subBean.image);
                    }
                }
                else
                {
                    addItem(bean.label, bean.desc, bean.image);
                }
            }
        }
        textView.performFiltering("", 0);
//        showDropDown();
    }

    @Override
    public void onClick(View v)
    {
        showDropDown();
    }

    /**
     * From Android documentation, onKey() method is not reliable for soft keyboard.
     * For the action button, we should use onEditorAction() method.
     * See {@link OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if(v == textView && actionId == EditorInfo.IME_ACTION_DONE)
        {
            hideDropDown();
            String text = v.getText().toString();
            if (text.length() > 2 && isNumeric(text.substring(0, text.length()).trim()))
            {
                this.textView.removeTextChangedListener(this);
                this.textView.setText("");
                this.textView.addTextChangedListener(this);

                text = text.substring(0, text.length()).trim();
                addItem(text, text, null);
                
                return true;
            }
            else if(text.length() > 0 && !isNumeric(text.substring(0, text.length()).trim()))
            {
                this.textView.removeTextChangedListener(this);
                this.textView.setText("");
                this.textView.addTextChangedListener(this);
                
                if(this.textView.getAdapter() != null && this.textView.getAdapter().getCount() > 0)
                {
                    Object o = ((BaseAdapter) this.textView.getAdapter()).getItem(0);
                    if (o instanceof TnSuggestionBean)
                    {
                        TnSuggestionBean bean = (TnSuggestionBean) o;
                        if (bean != null)
                        {
                            if(bean.isGroup)
                            {
                                KontagentLogger.getInstance().addCustomEvent(IKontagentConstants.CATEGORY_DWF, IKontagentConstants.DWF_GROUP_INVITE_CLICKED);
                            }
                            if(bean.subBeans != null && !bean.subBeans.isEmpty())
                            {
                                for(TnSuggestionBean subBean : bean.subBeans)
                                {
                                    addItem(subBean.label, subBean.desc, subBean.image);
                                }
                            }
                            else
                            {
                                addItem(bean.label, bean.desc, bean.image);
                            }
                        }
                    }
                    textView.performFiltering("", 0);
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void onDetachedFromWindow()
    {
        if(textView != null)
        {
            InputMethodManager lManager = (InputMethodManager) textView.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
            lManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
        super.onDetachedFromWindow();
    }
}
