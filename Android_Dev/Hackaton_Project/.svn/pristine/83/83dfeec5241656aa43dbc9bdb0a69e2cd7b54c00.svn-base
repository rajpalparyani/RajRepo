/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfRequestAidl.java
 *
 */
package com.telenav.dwf.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import com.telenav.dwf.vo.DwfRequest;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class DwfRequestAidl implements Parcelable
{
    private DwfRequest request;
    
    public DwfRequestAidl()
    {
        
    }

    public DwfRequest getRequest()
    {
        return request;
    }

    public void setRequest(DwfRequest request)
    {
        this.request = request;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.request.getClass().getName());
        dest.writeParcelable(this.request, flags);
    }

    public static final Parcelable.Creator<DwfRequestAidl> CREATOR = new Parcelable.Creator<DwfRequestAidl>()
    {
        public DwfRequestAidl createFromParcel(Parcel in)
        {
            return new DwfRequestAidl(in);
        }

        public DwfRequestAidl[] newArray(int size)
        {
            return new DwfRequestAidl[size];
        }
    };

    private DwfRequestAidl(Parcel in)
    {
        String className = in.readString();
        try
        {
            this.request = in.readParcelable(Class.forName(className).getClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
