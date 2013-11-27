/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * OnTnItemLongClickListener.java
 *
 */
package com.telenav.ui.android;

import com.telenav.app.android.scout_us.R;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * @author jpwang
 * @date 2013-3-12
 */
public abstract class OnTnItemLongClickListener implements OnItemLongClickListener
{
    private OnItemLongClickListener listener;

    private boolean isHandling = false;

    public OnTnItemLongClickListener()
    {
        this(null);
    }

    public OnTnItemLongClickListener(OnItemLongClickListener listener)
    {
        this.listener = listener;
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (isHandling)
        {
            return false;
        }
        isHandling = true;
        if (view.getTag(R.id.TAG_ITEM_INDEX) instanceof Integer)
        {
            position = (Integer) view.getTag(R.id.TAG_ITEM_INDEX);
        }
        boolean isSuccessful = onTnItemLongClick(parent, view, position, id);
        if (listener != null)
        {
            isSuccessful |= listener.onItemLongClick(parent, view, position, id);
        }
        isHandling = false;
        return isSuccessful;
    }

    protected abstract boolean onTnItemLongClick(AdapterView<?> parent, View view, int position, long id);
}
