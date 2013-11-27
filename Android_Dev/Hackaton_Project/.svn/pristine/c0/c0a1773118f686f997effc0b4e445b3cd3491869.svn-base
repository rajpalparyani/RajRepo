/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * GetSessionDetailRequest.java
 *
 */
package com.telenav.dwf.vo;

import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import com.telenav.dwf.aidl.DwfLogger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class GetSessionDetailRequest extends DwfRequest
{
    private String sessionId;

    private String userKey;

    private String userId;
    
    public GetSessionDetailRequest()
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

    public String getUserKey()
    {
        return userKey;
    }

    public void setUserKey(String userKey)
    {
        this.userKey = userKey;
    }

    
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);

        dest.writeString(this.sessionId);
        dest.writeString(this.userKey);
        dest.writeString(this.userId);
    }

    public static final Parcelable.Creator<GetSessionDetailRequest> CREATOR = new Parcelable.Creator<GetSessionDetailRequest>()
    {
        public GetSessionDetailRequest createFromParcel(Parcel in)
        {
            return new GetSessionDetailRequest(in);
        }

        public GetSessionDetailRequest[] newArray(int size)
        {
            return new GetSessionDetailRequest[size];
        }
    };

    private GetSessionDetailRequest(Parcel in)
    {
        super(in);

        this.sessionId = in.readString();
        this.userKey = in.readString();
        this.userId = in.readString();
    }
    
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        
        try
        {
            jsonObject.append("sessionId", sessionId);
            jsonObject.append("userKey", userKey);
            jsonObject.append("userId", userId);
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