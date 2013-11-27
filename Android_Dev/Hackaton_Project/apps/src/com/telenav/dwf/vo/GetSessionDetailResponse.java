/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * GetSessionDetail.java
 *
 */
package com.telenav.dwf.vo;

import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.telenav.dwf.aidl.DwfLogger;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class GetSessionDetailResponse extends NewSessionResponse
{
    private String formattedDt;
    
    private String dt;
    
    public GetSessionDetailResponse()
    {

    }
    
    public String getFormattedDt()
    {
        return formattedDt;
    }

    public void setFormattedDt(String formattedDt)
    {
        this.formattedDt = formattedDt;
    }
    
    public String getDt()
    {
        return this.dt;
    }
    
    public void setDt(String dt)
    {
        this.dt = dt;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);

        dest.writeString(this.formattedDt);
    }

    public static final Parcelable.Creator<GetSessionDetailResponse> CREATOR = new Parcelable.Creator<GetSessionDetailResponse>()
    {
        public GetSessionDetailResponse createFromParcel(Parcel in)
        {
            return new GetSessionDetailResponse(in);
        }

        public GetSessionDetailResponse[] newArray(int size)
        {
            return new GetSessionDetailResponse[size];
        }
    };

    private GetSessionDetailResponse(Parcel in)
    {
        super(in);
        
        this.formattedDt = in.readString();
    }
    
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = super.toJsonObject();
        
        try
        {
            jsonObject.append("formattedDt", formattedDt);
            jsonObject.append("dt", dt);
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