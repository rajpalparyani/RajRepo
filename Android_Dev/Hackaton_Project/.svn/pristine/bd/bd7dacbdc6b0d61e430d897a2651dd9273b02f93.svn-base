/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfResponse.java
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
public class DwfResponse implements Parcelable
{
    private DwfResponseStatus status = DwfResponseStatus.OK;

    private String errorMessage;
    
    public DwfResponse()
    {
    }
    
    public DwfResponseStatus getStatus()
    {
        return status;
    }

    public void setStatus(DwfResponseStatus status)
    {
        this.status = status;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.status.name());
        dest.writeString(this.errorMessage);
    }

    protected DwfResponse(Parcel in)
    {
        this.status = DwfResponseStatus.valueOf(in.readString());
        this.errorMessage = in.readString();
    }
    
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        
        try
        {
            jsonObject.append("status", status);
            jsonObject.append("errorMessage", errorMessage);
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
