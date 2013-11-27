/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * UpdateSessionRequest.java
 *
 */
package com.telenav.dwf.vo;

import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.telenav.dwf.aidl.DwfLogger;
import com.telenav.dwf.aidl.Friend;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class UpdateSessionRequest extends DwfRequest
{

    private String sessionId;

    private Friend friend;

    public UpdateSessionRequest()
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


    public Friend getFriend()
    {
        return friend;
    }


    public void setFriend(Friend friend)
    {
        this.friend = friend;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        
        dest.writeString(this.sessionId);
        dest.writeParcelable(this.friend, flags);
    }

    public static final Parcelable.Creator<UpdateSessionRequest> CREATOR = new Parcelable.Creator<UpdateSessionRequest>()
    {
        public UpdateSessionRequest createFromParcel(Parcel in)
        {
            return new UpdateSessionRequest(in);
        }

        public UpdateSessionRequest[] newArray(int size)
        {
            return new UpdateSessionRequest[size];
        }
    };

    private UpdateSessionRequest(Parcel in)
    {
        super(in);
        
        this.sessionId = in.readString();
        this.friend = in.readParcelable(Friend.class.getClassLoader());
    }
    
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        
        try
        {
            jsonObject.append("sessionId", sessionId);
            jsonObject.append("friend", friend.toJsonObject());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return jsonObject;
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
