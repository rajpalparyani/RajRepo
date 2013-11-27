/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfRequest.java
 *
 */
package com.telenav.dwf.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *@author fangquanm
 *@date Jul 9, 2013
 */
public abstract class DwfRequest implements Parcelable
{
    public DwfRequest()
    {
        
    }
    
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
    }
    
    protected DwfRequest(Parcel in)
    {
    }
}
