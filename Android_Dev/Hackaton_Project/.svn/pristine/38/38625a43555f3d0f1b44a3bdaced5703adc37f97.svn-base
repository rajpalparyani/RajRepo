/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfResponseAidl.java
 *
 */
package com.telenav.dwf.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import com.telenav.dwf.vo.DwfResponse;

/**
 *@author fangquanm
 *@date Jul 9, 2013
 */
public class DwfResponseAidl implements Parcelable
{
    private DwfResponse response;

    public DwfResponseAidl()
    {
        
    }

    public DwfResponse getResponse()
    {
        return response;
    }

    public void setResponse(DwfResponse response)
    {
        this.response = response;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.response.getClass().getName());
        dest.writeParcelable(this.response, flags);
    }

    public static final Parcelable.Creator<DwfResponseAidl> CREATOR = new Parcelable.Creator<DwfResponseAidl>()
    {
        public DwfResponseAidl createFromParcel(Parcel in)
        {
            return new DwfResponseAidl(in);
        }

        public DwfResponseAidl[] newArray(int size)
        {
            return new DwfResponseAidl[size];
        }
    };

    private DwfResponseAidl(Parcel in)
    {
        String className = in.readString();
        try
        {
            this.response = in.readParcelable(Class.forName(className).getClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
