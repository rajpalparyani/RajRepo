/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * TnListAdapter.java
 *
 */
package com.telenav.ui.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.telenav.app.android.scout_us.R;

/**
 * @author jpwang
 * @date 2013-2-28
 */
public abstract class TnListAdapter extends BaseAdapter
{
    protected LayoutInflater mInflater;
    
    public TnListAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    public final View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = getItemView(position, convertView, parent);
        if (convertView != null)
        {
            convertView.setTag(R.id.TAG_ITEM_INDEX, position);
            View selectedView = getItemViewAfterSelected(position, convertView, parent);
            View unselectedView = getItemViewBeforeSelected(position, convertView, parent);
            //While the list view is scrolling, the new item view will reuse the old converView,
            // so we need to make sure the new one is in unselected status. 
            // And when rolling back, the origin selected view should still be the selected status.
            if (selectedView != null && unselectedView != null)
            {
                if (Integer.valueOf(position).equals(parent.getTag(R.id.TAG_SELECTED_ITEM_INDEX)))
                {
                    unselectedView.setVisibility(View.GONE);
                    selectedView.setVisibility(View.VISIBLE);
                    parent.setTag(R.id.TAG_LAST_SELECTED_ITEM_VIEW, convertView);
                }
                else
                {
                    selectedView.setVisibility(View.GONE);
                    unselectedView.setVisibility(View.VISIBLE);
                }
                convertView.setTag(R.id.TAG_UNSELECTED_VIEW, unselectedView);
                convertView.setTag(R.id.TAG_SELECTED_VIEW, selectedView);
            }
        }
        return convertView;
    }

    protected abstract View getItemView(int position, View convertView, ViewGroup parent);

    protected abstract View getItemViewBeforeSelected(int position, View convertView, ViewGroup parent);

    protected abstract View getItemViewAfterSelected(int position, View convertView, ViewGroup parent);
}
