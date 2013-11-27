/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * InviteFriendResponse.java
 *
 */
package com.telenav.dwf.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class InviteFriendResponse extends NewSessionResponse
{
    public InviteFriendResponse()
    {

    }

    public static final Parcelable.Creator<InviteFriendResponse> CREATOR = new Parcelable.Creator<InviteFriendResponse>()
    {
        public InviteFriendResponse createFromParcel(Parcel in)
        {
            return new InviteFriendResponse(in);
        }

        public InviteFriendResponse[] newArray(int size)
        {
            return new InviteFriendResponse[size];
        }
    };

    private InviteFriendResponse(Parcel in)
    {
        super(in);
    }
}
