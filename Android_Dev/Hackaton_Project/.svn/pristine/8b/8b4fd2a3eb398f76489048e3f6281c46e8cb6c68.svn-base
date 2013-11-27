/**
 *
 * Copyright 2012 Telenav, Inc. All rights reserved.
 * EmailFilterArrayAdapter.java
 *
 */
package com.telenav.module.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * @author Fangquan Ming
 * @date Dec 18, 2012
 */
public class EmailFilterArrayAdapter extends BaseAdapter implements Filterable
{
    private static final String[] EMAIL_PROVIDERS = new String[]
            { "@gmail.com", "@yahoo.com", "@hotmail.com", "@aol.com"};
    
    private List<String> mObjects;

    private final Object mLock = new Object();

    private int mResource;

    private int mDropDownResource;

    private int mFieldId = 0;

    private ArrayList<String> mOriginalValues;

    private ArrayFilter mFilter;

    private LayoutInflater mInflater;

    public EmailFilterArrayAdapter(Context context, int textViewResourceId)
    {
        init(context, textViewResourceId, 0, Arrays.asList(EMAIL_PROVIDERS));
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    private void init(Context context, int resource, int textViewResourceId, List<String> objects)
    {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
    }

    public int getCount()
    {
        return mObjects.size();
    }

    public String getItem(int position)
    {
        return mObjects.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource)
    {
        View view;
        TextView text;

        if (convertView == null)
       {
            view = mInflater.inflate(resource, parent, false);
        }
        else
        {
            view = convertView;
        }

        try
        {
            if (mFieldId == 0)
            {
                // If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            }
            else
            {
                // Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        }
        catch (ClassCastException e)
        {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
        }

        String item = getItem(position);
        if (item instanceof CharSequence)
        {
            text.setText((CharSequence) item);
        }
        else
        {
            text.setText(item.toString());
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    public Filter getFilter()
    {
        if (mFilter == null)
        {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence prefix)
        {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null)
            {
                synchronized (mLock)
                {
                    mOriginalValues = new ArrayList<String>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0)
            {
                ArrayList<String> list;
                synchronized (mLock)
                {
                    list = new ArrayList<String>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            }
            else
            {
                String prefixString = prefix.toString();

                ArrayList<String> values;
                synchronized (mLock)
                {
                    values = new ArrayList<String>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<String> newValues = new ArrayList<String>();

                for (int i = 0; i < count; i++)
                {
                    final String valueText = values.get(i);

                    for (int j = 0; j < valueText.length(); j++)
                    {
                        if (prefixString.endsWith(valueText.substring(0, j + 1)))
                        {
                            newValues.add(prefixString + valueText.substring(j + 1));
                            break;
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            mObjects = (List<String>) results.values;
            if (results.count > 0)
            {
                notifyDataSetChanged();
            }
            else
            {
                notifyDataSetInvalidated();
            }
        }
    }
}