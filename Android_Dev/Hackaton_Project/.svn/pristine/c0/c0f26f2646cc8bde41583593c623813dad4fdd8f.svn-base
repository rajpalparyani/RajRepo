/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * OnTnListItemClickListener.java
 *
 */
package com.telenav.ui.android;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.telenav.app.android.scout_us.R;

/**
 *@author jpwang
 *@date 2013-2-28
 */
public abstract class OnTnItemClickListener implements OnItemClickListener
{
    private OnItemClickListener itemClickListener;
    
    public OnTnItemClickListener()
    {
        this(null);
    }
    
    public OnTnItemClickListener(OnItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public final void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // This is used to fix the issue that the position is not exact the item you want which is caused by the header and footer in listview.
        // Once the header is added in listview, the first position will be the header not the list item you want.
        // And so does the footer.
        if(view.getTag(R.id.TAG_ITEM_INDEX) instanceof Integer)
        {
            position = (Integer) view.getTag(R.id.TAG_ITEM_INDEX);
        }
        onTnItemClick(parent, view, position, id);
        if (itemClickListener != null)
        {
            itemClickListener.onItemClick(parent, view, position, id);
        }
    }
    
    protected abstract void onTnItemClick(AdapterView<?> parent, View view, int itemIndex, long id);
}