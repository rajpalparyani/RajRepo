/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DriveWithFriendsListAdapter.java
 *
 */
package com.telenav.module.dwf;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.Friend.FriendType;

/**
 * @author fangquanm
 * @date Jul 10, 2013
 */
public class DriveWithFriendsListAdapter extends BaseAdapter implements ListAdapter
{
    private ArrayList<Friend> friends;
    
    private String userKey;

    public DriveWithFriendsListAdapter()
    {
        friends = new ArrayList<Friend>();
    }

    public void setFriends(ArrayList<Friend> fs, String userKey)
    {
        this.friends.clear();
        this.friends.addAll(fs);
        this.userKey = userKey;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return friends == null || friends.isEmpty() ? 1 : friends.size();
    }

    @Override
    public Object getItem(int position)
    {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (friends == null || friends.isEmpty())
        {
            Context context = (Context) AndroidPersistentContext.getInstance().getContext();
            view = View.inflate(context, R.layout.dwf_list_list0item_empty, null);
        }
        else
        {
            if (convertView == null || convertView.findViewById(R.id.dwfListListItemIndicatorView) == null)
            {
                Context context = (Context) AndroidPersistentContext.getInstance().getContext();
                view = View.inflate(context, R.layout.dwf_list_list0item, null);
            }

            View indicatorView = view.findViewById(R.id.dwfListListItemIndicatorView);
            ImageView imageView = (ImageView) view.findViewById(R.id.dwfListListItemImageView);
            TextView nameView = (TextView) view.findViewById(R.id.dwfListListItemNameView);
            TextView statusView = (TextView) view.findViewById(R.id.dwfListListItemStatusView);
            TextView etaView = (TextView) view.findViewById(R.id.dwfListListItemEtaView);

            Friend f = this.friends.get(position);

            if (f.getImage() != null && f.getImage().length > 0)
            {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(f.getImage(), 0, f.getImage().length));
            }
            else
            {
                imageView.setImageResource(R.drawable.drive_with_friends_default_portrait_icon_unfocused);
            }
            
            if (f.getEta() >= 0)
            {
                etaView.setText(parseEtaDisplayTime(f.getEta()));
            }
            else
            {
                etaView.setText(" - - ");
            }
            
            String updateStr = getUpdateTime(f.getUpdateTime());
            
            switch(f.getStatus())
            {
                case ARRIVED:
                {
                    statusView.setText(R.string.dwfFriendArrived);
                    indicatorView.setEnabled(true);
                    etaView.setText(" - - ");
                    break;
                }
                case DRIVING:
                {
                    statusView.setText(updateStr);
                    indicatorView.setEnabled(true);
                    break;
                }
                case END:
                {
                    statusView.setText(updateStr);
                    indicatorView.setEnabled(false);
                    break;
                }
                case INIT:
                {
                    statusView.setEnabled(false);
                    indicatorView.setEnabled(false);
                    break;
                }
                case JOINED:
                {
                    statusView.setText(updateStr);
                    indicatorView.setEnabled(true);
                    break;
                }
                default:
                {
                    statusView.setEnabled(false);
                    indicatorView.setEnabled(false);
                    break;
                }
            }

            Resources res = AndroidPersistentContext.getInstance().getContext().getResources();
            if (f.getKey().equals(this.userKey) || (this.userKey == null && f.getType().name().equals(FriendType.HOST.name())))
            {
                indicatorView.setEnabled(true);
                view.setBackgroundResource(R.drawable.add_place_list_item_background);
                nameView.setText(AndroidPersistentContext.getInstance().getContext().getString(R.string.dwfMe));
            }
            else
            {
                view.setBackgroundResource(R.drawable.place_list_item_background);
                nameView.setText(f.getName());
            }
            view.setPadding(0, res.getDimensionPixelSize(R.dimen.dwf0ListItemPaddingTop)
                , 0, res.getDimensionPixelSize(R.dimen.dwf0ListItemPaddingBottom));
        }
        
        return view;
    }
    
    private String getUpdateTime(long updateTime)
    {
        if(updateTime == 0)
            return "";
        
        long t = System.currentTimeMillis() / 1000 - updateTime;
        t = t / 60;
        
        Context context = AndroidPersistentContext.getInstance().getContext();
        
        String updateTimeStr = "";
        if(t < 60)
        {
            if(t <= 0)
            {
                long sec = System.currentTimeMillis() / 1000 - updateTime;
                if(sec <= 0)
                {
                    sec = 5;
                }
                updateTimeStr = context.getString(R.string.dwfFriendUpdateSeconds);
            }
            else
            {
                updateTimeStr = context.getString(t <= 1 ? R.string.dwfFriendUpdateMinute : R.string.dwfFriendUpdateMinutes, t + "");
            }
        }
        else
        {
            long hour = t / 60;
            updateTimeStr = context.getString(hour <= 1 ? R.string.dwfFriendUpdateHour : R.string.dwfFriendUpdateHours, hour
                    + "");
        }
        return updateTimeStr;
    }
    

    public String parseEtaDisplayTime(long second)
    {
        String etaString = "00:";
        int hour = (int) (second / 3600);
        if (hour > 0 && hour < 10)
        {
            etaString = "0" + Integer.toString(hour) + ":";
        }
        else if (hour >= 10)
        {
            etaString = Integer.toString(hour) + ":";
        }
        int minute = (int) ((second % 3600) / 60);
        if (minute > 0 && minute < 10)
        {
            etaString += "0" + Integer.toString(minute);
        }
        else if (minute >= 10)
        {
            etaString += Integer.toString(minute);
        }
        else
        {
            etaString += "00";
        }
        return etaString;
    }
}
