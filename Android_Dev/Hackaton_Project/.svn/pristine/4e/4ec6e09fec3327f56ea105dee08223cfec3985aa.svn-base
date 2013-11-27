/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * NewSessionRequest.java
 *
 */
package com.telenav.dwf.vo;

import java.util.ArrayList;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.telenav.dwf.aidl.DwfLogger;
import com.telenav.dwf.aidl.Friend;
import com.telenav.logger.Logger;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class NewSessionRequest extends DwfRequest
{
    public NewSessionRequest()
    {
    }

    private String token;

    private String dt;

    private String formattedDt;

    private String name;

    private ArrayList<Friend> friends = new ArrayList<Friend>();

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getDt()
    {
        return dt;
    }

    public void setDt(String dt)
    {
        this.dt = dt;
    }


    public String getFormattedDt()
    {
        return formattedDt;
    }

    public void setFormattedDt(String formattedDt)
    {
        this.formattedDt = formattedDt;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<Friend> getFriends()
    {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends)
    {
        this.friends = friends;
    }

    public void addFriend(Friend friend)
    {
        if (friend == null || friends.contains(friend))
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "-- DWF -- addFriend fail, null ? " + (friend == null));
            return;
        }

        this.friends.add(friend);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);

        dest.writeString(this.token);
        dest.writeString(this.dt);
        dest.writeString(this.formattedDt);
        dest.writeString(this.name);
        dest.writeList(this.friends);
    }

    public static final Parcelable.Creator<NewSessionRequest> CREATOR = new Parcelable.Creator<NewSessionRequest>()
    {
        public NewSessionRequest createFromParcel(Parcel in)
        {
            return new NewSessionRequest(in);
        }

        public NewSessionRequest[] newArray(int size)
        {
            return new NewSessionRequest[size];
        }
    };

    protected NewSessionRequest(Parcel in)
    {
        super(in);

        this.token = in.readString();
        this.dt = in.readString();
        this.formattedDt = in.readString();
        this.name = in.readString();
        in.readList(this.friends, Friend.class.getClassLoader());
    }
    
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        
        try
        {
            jsonObject.append("name", name);
            jsonObject.append("dt", dt);
            jsonObject.append("formattedDt", formattedDt);
            jsonObject.append("token", token);
            jsonObject.append("friends", getFriendsJsonArray());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return jsonObject;
    }
    
    protected JSONArray getFriendsJsonArray()
    {
        JSONArray jsonArray = new JSONArray();
        
        int size = friends == null ? 0 : friends.size();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                jsonArray.put(friends.get(i).toJsonObject());
            }
        }
        
        return jsonArray;
    }
    
    public String toString()
    {
        String output = null;
        try
        {
            output = toJsonObject().toString(DwfLogger.DWF_LOG_INDENT);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return output;
    }
}
