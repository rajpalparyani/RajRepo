/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * UpdateSessionResponse.java
 *
 */
package com.telenav.dwf.vo;

import java.util.ArrayList;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import com.telenav.dwf.aidl.DwfLogger;
import com.telenav.dwf.aidl.Friend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *@author fangquanm
 *@date Jul 9, 2013
 */
public class UpdateSessionResponse extends DwfResponse
{
    private String sessionId;

    private ArrayList<Friend> friends = new ArrayList<Friend>();

    public UpdateSessionResponse()
    {

    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public ArrayList<Friend> getFriends()
    {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends)
    {
        this.friends = friends;
    }

    public void addFriend(Friend f)
    {
        this.friends.add(f);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);

        dest.writeString(this.sessionId);
        dest.writeList(this.friends);
    }

    public static final Parcelable.Creator<UpdateSessionResponse> CREATOR = new Parcelable.Creator<UpdateSessionResponse>()
    {
        public UpdateSessionResponse createFromParcel(Parcel in)
        {
            return new UpdateSessionResponse(in);
        }

        public UpdateSessionResponse[] newArray(int size)
        {
            return new UpdateSessionResponse[size];
        }
    };

    private UpdateSessionResponse(Parcel in)
    {
        super(in);
        this.sessionId = in.readString();
        in.readList(this.friends, Friend.class.getClassLoader());
    }
    
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = super.toJsonObject();
        
        try
        {
            jsonObject.append("sessionId", sessionId);
            jsonObject.put("friends", getFriendsJsonArray());
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
