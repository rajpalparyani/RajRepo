/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ContactSuggestionAdapter.java
 *
 */
package com.telenav.module.dwf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.DwfContactsDao.Group;
import com.telenav.ui.android.TnAutoCompleteTextView.TnSuggestionBean;

/**
 *@author fangquanm
 *@date Jul 1, 2013
 */
class ContactSuggestionAdapter extends BaseAdapter implements Filterable
{
    private List<TnSuggestionBean> mObjects = new ArrayList<TnSuggestionBean>();
    
    private ArrayFilter mFilter;

    private LayoutInflater mInflater;

    public ContactSuggestionAdapter(Context context)
    {
        init(context, new ArrayList<TnSuggestionBean>());
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    private void init(Context context, List<TnSuggestionBean> objects)
    {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public int getCount()
    {
        return mObjects == null ? 0 : mObjects.size();
    }

    public TnSuggestionBean getItem(int position)
    {
        return mObjects.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        TnSuggestionBean bean = this.getItem(position);
        if (convertView == null)
        {
            if (bean.isGroup)
            {
                convertView = mInflater.inflate(R.layout.dwf_contact_suggest_list_item_group, parent, false);
            }
            else
            {
                convertView = mInflater.inflate(R.layout.dwf_contact_suggest_list_item, parent, false);
            }
        }

        ((TextView)convertView.findViewById(R.id.dwf0contactSuggestItemName)).setText(bean.label);
        if (!bean.isGroup)
        {
            ((TextView)convertView.findViewById(R.id.dwf0contactSuggestItemPhone)).setText(bean.desc);
            ((TextView)convertView.findViewById(R.id.dwf0contactSuggestItemType)).setText(ContactUtil.getTypeLabelResource(bean.type));
        }
        
        return convertView;
    }
    
    private static int[] viewTypes = new int[] {0, 1};

    @Override
    public int getItemViewType(int position)
    {
        return getItem(position).isGroup? viewTypes[0] : viewTypes[1];
    }

    @Override
    public int getViewTypeCount()
    {
        return viewTypes.length;
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
            String prefixString = prefix == null ? "" : prefix.toString().trim();
            
            ArrayList<Group> groups = ((DaoManager)DaoManager.getInstance()).getDwfContactsDao().getGroups();
            
            ArrayList<TnContact> contacts = new ArrayList<TnContact>();
            if(prefixString.trim().length() > 0)
            {
                contacts = ContactUtil.retreiveContact(prefixString);
            }
            
            ArrayList<TnSuggestionBean> originalObjects = new ArrayList<TnSuggestionBean>();
            ArrayList<TnSuggestionBean> recipients = new ArrayList<TnSuggestionBean>();
            
            //Search group
            for(Group g : groups)
            {
                TnSuggestionBean bean = new TnSuggestionBean();
                String label = "";
                String search = "";
                String desc = "";
                for(int i = 0; i < g.contacts.size(); i++)
                {
                    TnContact c = g.contacts.get(i);
                    label += (i != 0 ? ", " : "") + c.name;
                    desc += (i != 0 ? ", " : "") + c.phoneNumber;
                    search += (i != 0 ? " " : "") + c.name + " " + c.phoneNumber;
                    
                    TnSuggestionBean subBean = new TnSuggestionBean();
                    subBean.label = c.name;
                    subBean.desc = c.phoneNumber;
                    subBean.image = c.b;
                    if(!recipients.contains(subBean))
                    {
                        recipients.add(subBean);
                    }
                    bean.subBeans.add(subBean);
                }
                for(TnSuggestionBean subBean : bean.subBeans)
                {
                    subBean.search = search;
                }
                if (g.contacts.size() > 1)
                {
                    bean.label = label;
                    bean.search = search;
                    bean.desc = desc;
                    bean.isGroup = true;

                    if (!originalObjects.contains(bean))
                    {
                        originalObjects.add(bean);
                    }
                }
            }
            
            // Search each item in Group
            originalObjects.addAll(recipients);
            if (prefix != null && prefix.length() != 0)
            {
                Iterator<TnSuggestionBean> iter = originalObjects.iterator();
                while (iter.hasNext())
                {
                    TnSuggestionBean bean = iter.next();
                    if (bean.search.toLowerCase(Locale.US).indexOf(prefixString.toLowerCase(Locale.US)) == -1)
                    {
                        iter.remove();
                    }
                }
            }
            
            // Search contact
            for (TnContact contact : contacts)
            {
                TnSuggestionBean bean = new TnSuggestionBean();
                bean.label = contact.name;
                ContactUtil.trimPhone(contact);
                bean.search = contact.name + " " + contact.phoneNumber;
                bean.desc = contact.phoneNumber;
                bean.type = contact.phoneNumberType;
                bean.image = ContactUtil.loadContactPhoto(Long.parseLong(contact.id), AndroidPersistentContext.getInstance()
                        .getContext());

                if (!originalObjects.contains(bean))
                {
                    originalObjects.add(bean);
                }
            }

            FilterResults results = new FilterResults();

            ArrayList<TnSuggestionBean> list = new ArrayList<TnSuggestionBean>(originalObjects);
            results.values = list;
            results.count = list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            mObjects = (List<TnSuggestionBean>) results.values;
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
