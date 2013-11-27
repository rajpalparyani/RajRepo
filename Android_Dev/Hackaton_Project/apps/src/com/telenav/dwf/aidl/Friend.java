/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * Friend.java
 *
 */
package com.telenav.dwf.aidl;

import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class Friend implements Parcelable
{
    public enum FriendType
    {
        HOST, GUEST
    };

    public enum FriendStatus
    {
        INIT, JOINED, DRIVING, ARRIVED, END
    };

    private String uid;

    private String phone;
    
    private String facebookId;
    
    
    private String name;

    private String key;

    private byte[] image;

    private FriendType type = FriendType.GUEST;

    private double lat;

    private double lon;

    private FriendStatus status = FriendStatus.INIT;

    private long eta = -1;
    
    private String url;
    
    private long updateTime;

    public Friend()
    {

    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public FriendType getType()
    {
        return type;
    }

    public void setType(FriendType type)
    {
        this.type = type;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public FriendStatus getStatus()
    {
        return status;
    }

    public void setStatus(FriendStatus status)
    {
        this.status = status;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public long getEta()
    {
        return eta;
    }

    public void setEta(long eta)
    {
        this.eta = eta;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(long updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getPhone()
    {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getFacebookId()
    {
        return facebookId == null ? "" : facebookId;
    }

    public void setFacebookId(String facebookId)
    {
        this.facebookId = facebookId;
    }
    
    public Friend copy()
    {
        Friend f = new Friend();
        f.setEta(this.getEta());
        f.setFacebookId(this.getFacebookId());
        f.setImage(this.getImage());
        f.setKey(this.getKey());
        f.setLat(this.getLat());
        f.setLon(this.getLon());
        f.setName(this.getName());
        f.setPhone(this.getPhone());
        f.setStatus(this.getStatus());
        f.setType(this.getType());
        f.setUid(this.getUid());
        f.setUpdateTime(this.getUpdateTime());
        f.setUrl(this.getUrl());
        
        return f;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.uid);
        dest.writeString(this.name);
        dest.writeString(this.type.name());
        dest.writeString(this.key);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeString(this.status.name());
        dest.writeLong(this.eta);
        dest.writeString(this.url);
        dest.writeLong(this.updateTime);
        dest.writeByteArray(this.image);
        dest.writeString(this.facebookId);
        dest.writeString(this.phone);
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>()
    {
        public Friend createFromParcel(Parcel in)
        {
            return new Friend(in);
        }

        public Friend[] newArray(int size)
        {
            return new Friend[size];
        }
    };

    private Friend(Parcel in)
    {
        this.uid = in.readString();
        this.name = in.readString();
        this.type = FriendType.valueOf(in.readString());
        this.key = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.status = FriendStatus.valueOf(in.readString());
        this.eta = in.readLong();
        this.url = in.readString();
        this.updateTime = in.readLong();
        this.image = in.createByteArray();
        this.facebookId = in.readString();
        this.phone = in.readString();
    }
    
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        
        try
        {
            jsonObject.append("name", name);
            jsonObject.append("uid", uid);
            jsonObject.append("type", type);
            jsonObject.append("key", key);
            jsonObject.append("lat", lat);
            jsonObject.append("lon", lon);
            jsonObject.append("status", status);
            jsonObject.append("eta", eta);
            jsonObject.append("url", url);
            jsonObject.append("image", String.valueOf(image == null || image.length == 0));
            jsonObject.append("facebookId", facebookId);
            jsonObject.append("phone", phone);
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
