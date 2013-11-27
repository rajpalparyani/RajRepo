/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfNetworkUtil.java
 *
 */
package com.telenav.dwf.request;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.telenav.dwf.aidl.DwfLogger;
import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.dwf.aidl.Friend.FriendType;
import com.telenav.dwf.vo.DwfResponseStatus;
import com.telenav.dwf.vo.GetSessionDetailRequest;
import com.telenav.dwf.vo.GetSessionDetailResponse;
import com.telenav.dwf.vo.InviteFriendRequest;
import com.telenav.dwf.vo.InviteFriendResponse;
import com.telenav.dwf.vo.NewSessionRequest;
import com.telenav.dwf.vo.NewSessionResponse;
import com.telenav.dwf.vo.UpdateSessionRequest;
import com.telenav.dwf.vo.UpdateSessionResponse;
import com.telenav.module.dwf.ContactUtil;
import com.telenav.module.login.AccountFetcher;

/**
 * @author fangquanm
 * @date Jul 12, 2013
 */
public class DwfNetworkUtil
{
    public static NewSessionResponse requestNewSession(String url, NewSessionRequest newSessionRequest)
    {
        NewSessionResponse response = new NewSessionResponse();

        try
        {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("token", newSessionRequest.getToken()));
            nameValuePairs.add(new BasicNameValuePair("dt", newSessionRequest.getDt()));
            nameValuePairs.add(new BasicNameValuePair("formated_dt", newSessionRequest.getFormattedDt()));
            if (newSessionRequest.getName() != null && newSessionRequest.getName().length() > 0)
            {
                nameValuePairs.add(new BasicNameValuePair("name", newSessionRequest.getName()));
            }
            Friend hostFriend = null;
            for (Friend f : newSessionRequest.getFriends())
            {
                if (f.getType() == FriendType.HOST)
                {
                    hostFriend = f;
                    nameValuePairs.add(new BasicNameValuePair("me", friendToJson(f).toString()));
                    break;
                }
            }

            if (hostFriend == null)
            {
                response.setStatus(DwfResponseStatus.EXCEPTION);
                return response;
            }

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/x-www-form-urlencoded");

            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);

            JSONObject json = DwfNetwork.getInstance().post(url + "/session/create", headers, entity);

            response.setSessionId(json.getString("session_id"));
            hostFriend.setKey(json.getString("user_key"));
            response.setStatus(DwfResponseStatus.valueOf(json.getString("status")));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            
            response.setStatus(DwfResponseStatus.EXCEPTION);
        }

        return response;
    }

    public static InviteFriendResponse requestInviteFriend(String url, InviteFriendRequest inviteFriendRequest)
    {
        InviteFriendResponse response = new InviteFriendResponse();

        try
        {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("session_id", inviteFriendRequest.getSessionId()));
            nameValuePairs.add(new BasicNameValuePair("user_key", inviteFriendRequest.getUserKey()));

            JSONArray friendsArray = new JSONArray();
            ArrayList<Friend> requestFriends = new ArrayList<Friend>();
            for (Friend f : inviteFriendRequest.getFriends())
            {
                if (f.getKey() != null && f.getKey().length() > 0)
                    continue;

                requestFriends.add(f);

                friendsArray.put(friendToJson(f));
            }
            nameValuePairs.add(new BasicNameValuePair("friends", friendsArray.toString()));

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/x-www-form-urlencoded");

            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);

            JSONObject json = DwfNetwork.getInstance().post(url + "/session/invite", headers, entity);

            response.setSessionId(json.getString("session_id"));
            response.setStatus(DwfResponseStatus.valueOf(json.getString("status")));
            response.setFriends(inviteFriendRequest.getFriends());

            if (json.has("friends"))
            {
                JSONArray jsonArray = json.getJSONArray("friends");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject userIdJson = jsonArray.getJSONObject(i);
                    Friend f = requestFriends.get(i);
                    f.setKey(userIdJson.getString("user_key"));
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            
            response.setStatus(DwfResponseStatus.EXCEPTION);
        }

        return response;
    }

    public static GetSessionDetailResponse requestSessionDetail(String url, GetSessionDetailRequest request, Context ct)
    {
        GetSessionDetailResponse response = new GetSessionDetailResponse();

        try
        {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("session_id", request.getSessionId()));
            nameValuePairs.add(new BasicNameValuePair("uid", request.getUserId()));
            nameValuePairs.add(new BasicNameValuePair("user_key", request.getUserKey()));

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/x-www-form-urlencoded");

            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);

            JSONObject json = DwfNetwork.getInstance().post(url + "/session/detail", headers, entity);

            if(json.getString("status").equals(DwfResponseStatus.SESSION_NOT_EXIST.name()))
            {
                response.setStatus(DwfResponseStatus.valueOf(json.getString("status")));
            }
            else
            {
                response.setSessionId(json.getString("session_id"));
                response.setStatus(DwfResponseStatus.valueOf(json.getString("status")));
                if (json.has("formated_dt"))
                {
                    response.setFormattedDt(json.getString("formated_dt"));
                }
                if (json.has("dt"))
                {
                    response.setDt(json.getString("dt"));
                }
                if (json.has("friends"))
                {
                    JSONArray friendArray = json.getJSONArray("friends");
                    for (int i = 0; i < friendArray.length(); i++)
                    {
                        JSONObject jsonObj = friendArray.getJSONObject(i);
                        Friend f = toFriend(jsonObj, ct, request.getUserKey());
                        response.addFriend(f);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            
            response.setStatus(DwfResponseStatus.EXCEPTION);
        }

        return response;
    }

    public static UpdateSessionResponse requestSessionUpdate(String url, UpdateSessionRequest request, Friend host, Context ct)
    {
        UpdateSessionResponse response = new UpdateSessionResponse();

        try
        {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("session_id", request.getSessionId()));
            nameValuePairs.add(new BasicNameValuePair("uid", request.getFriend().getUid()));
            nameValuePairs.add(new BasicNameValuePair("user_key", request.getFriend().getKey()));
            if(request.getFriend().getLat() !=0 && request.getFriend().getLon() != 0)
            {
                nameValuePairs.add(new BasicNameValuePair("lat", request.getFriend().getLat() + ""));
                nameValuePairs.add(new BasicNameValuePair("lon", request.getFriend().getLon() + ""));
            }
            else
            {
                return null;
            }
            nameValuePairs.add(new BasicNameValuePair("user_status", request.getFriend().getStatus().name()));
            nameValuePairs.add(new BasicNameValuePair("eta", request.getFriend().getEta() + ""));
            nameValuePairs.add(new BasicNameValuePair("me", friendToJson(host).toString()));
            
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/x-www-form-urlencoded");

            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);

            JSONObject json = DwfNetwork.getInstance().post(url + "/session/update", headers, entity);

            response.setSessionId(json.getString("session_id"));
            response.setStatus(DwfResponseStatus.valueOf(json.getString("status")));

            if (json.has("friends"))
            {
                JSONArray friendArray = json.getJSONArray("friends");
                for (int i = 0; i < friendArray.length(); i++)
                {
                    JSONObject jsonObj = friendArray.getJSONObject(i);
                    Friend f = toFriend(jsonObj, ct, request.getFriend().getKey());
                    response.addFriend(f);
                }
            }

            return response;
        }
        catch (Throwable e)
        {
            DwfLogger.getInstance().log(DwfLogger.ERROR, e.getMessage());
            
            e.printStackTrace();
            
            return null;
        }
    }

    public static ArrayList<Friend> requestTinyUrl(String url, ArrayList<Friend> friends)
    {
        try
        {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            JSONArray urlsArray = new JSONArray();
            for (Friend f : friends)
            {
                JSONObject json = new JSONObject();
                json.put("url", f.getUrl());
                urlsArray.put(json);
            }
            nameValuePairs.add(new BasicNameValuePair("original_urls", urlsArray.toString()));
            
            DwfLogger.getInstance().log(DwfLogger.INFO, "RequestTinyUrl : \n" + urlsArray.toString(DwfLogger.DWF_LOG_INDENT));

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("content-type", "application/x-www-form-urlencoded");

            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);

            JSONObject json = DwfNetwork.getInstance().post(url + "/tinyurl", headers, entity);

            DwfLogger.getInstance().log(DwfLogger.INFO, "ResponseTinyUrl : \n" + json.toString(DwfLogger.DWF_LOG_INDENT));
            
            if (json.has("short_urls"))
            {
                JSONArray jsonArray = new JSONArray(json.getString("short_urls"));
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject urlObj = jsonArray.getJSONObject(i);
                    friends.get(i).setUrl(urlObj.getString("url"));
                }
            }

            return friends;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            
            return null;
        }
    }

    private static JSONObject friendToJson(Friend f)
    {
        JSONObject json = new JSONObject();
        try
        {
            if (f.getName() != null)
            {
                json.put("name", f.getName());
            }
            json.put("uid", f.getUid());
            if (f.getPhone() != null)
            {
                json.put("phone", f.getPhone());
            }
            json.put("fbuid", f.getFacebookId());
            if (f.getImage() != null && f.getImage().length > 0)
            {
                json.put("pic_base64", Base64.encodeToString(f.getImage(), Base64.DEFAULT));
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        return json;
    }
    
    private static Friend toFriend(JSONObject json, Context ct, String hostUserKey)
    {
        Friend f = new Friend();
        try
        {
            f.setKey(json.has("user_key") ? json.getString("user_key") : "");
            f.setUid(json.has("uid") ? json.getString("uid") : "");
            f.setName(json.has("name") ? json.getString("name") : "");
            f.setLat(json.has("lat") ? json.getDouble("lat") : 0);
            f.setLon(json.has("lon") ? json.getDouble("lon") : 0);
            f.setEta(json.has("eta") ? json.getLong("eta") : 0);
            f.setPhone(json.has("phone") ? json.getString("phone") : "");
            f.setFacebookId(json.has("fbuid") ? json.getString("phone") : "");
            f.setUpdateTime(json.has("update_time") ? json.getLong("update_time") : 0);
            f.setType(json.has("type") ? FriendType.valueOf(json.getString("type")) : FriendType.GUEST);
            String pic = json.has("pic") ? json.getString("pic") : null;
            
            if (!"".equals(f.getKey()))
            {
                if (imageCache.containsKey(f.getKey()))
                {
                    f.setImage(imageCache.get(json.getString("user_key")));
                }
                else 
                {
                    if (f.getKey().equals(hostUserKey))
                    {
                        byte[] byteArray = AccountFetcher.getUserPhoto(ct);
                        if (byteArray != null)
                        {
                            f.setImage(byteArray);
                            imageCache.put(f.getKey(), byteArray);
                        }
                    }
                    else if (f.getImage() == null)
                    {
                        Bitmap bitmap = null;
                        if (!"".equals(f.getPhone()))
                        {
                            bitmap = ContactUtil.loadContaBitmapByPhoneNumber(ct, f.getPhone());
                        }
                        if (bitmap == null && pic != null && pic.length() > 0)
                        {
                            InputStream is = (InputStream) new URL(pic).getContent();
                            bitmap = BitmapFactory.decodeStream(is);
                            is.close();
                        }
                        if (bitmap != null)
                        {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            stream.close();
                            f.setImage(byteArray);
                            imageCache.put(f.getKey(), byteArray);
                        }
                    }
                }
            }
            
            if(json.has("user_status"))
            {
                f.setStatus(FriendStatus.valueOf(json.getString("user_status")));
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        return f;
    }

    private static Hashtable<String, byte[]> imageCache = new Hashtable<String, byte[]>();
}
