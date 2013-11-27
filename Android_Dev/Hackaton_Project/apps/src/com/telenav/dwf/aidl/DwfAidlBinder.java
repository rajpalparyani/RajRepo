/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfAidlBinder.java
 *
 */
package com.telenav.dwf.aidl;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.impl.json.JsonS4AProxy;
import com.telenav.dwf.aidl.Friend.FriendType;
import com.telenav.dwf.request.DwfNetworkUtil;
import com.telenav.dwf.vo.DwfResponseStatus;
import com.telenav.dwf.vo.GetSessionDetailRequest;
import com.telenav.dwf.vo.GetSessionDetailResponse;
import com.telenav.dwf.vo.InviteFriendRequest;
import com.telenav.dwf.vo.InviteFriendResponse;
import com.telenav.dwf.vo.NewSessionRequest;
import com.telenav.dwf.vo.NewSessionResponse;
import com.telenav.module.dwf.DwfUtil;

/**
 * @author fangquanm
 * @date Jul 9, 2013
 */
public class DwfAidlBinder extends DwfAidl.Stub
{
    private static final String DWF_SHARED_PREFERENCE = "scout.dwf.preference";
    private static final String DWF_PREFERENCE_SERVICE_URL = "scout.dwf.preference.dwf_service_url";
    private static final String DWF_PREFERENCE_DWF_WEB_PAGE_URL = "scout.dwf.preference.dwf_web_page_url";
    
    private Service service;

    private SharedPreferences sp;

    private String dwf_service_url;
    
    private String dwf_web_page_url;

    public DwfAidlBinder(Service service)
    {
        this.service = service;

        this.sp = service.getSharedPreferences(DWF_SHARED_PREFERENCE, Service.MODE_PRIVATE);
        this.dwf_service_url = this.sp.getString(DWF_PREFERENCE_SERVICE_URL, "");
        this.dwf_web_page_url = this.sp.getString(DWF_PREFERENCE_DWF_WEB_PAGE_URL, "");
    }

    @Override
    public void setDwfServiceUrl(String url) throws RemoteException
    {
        this.dwf_service_url = url;
        //this.url = "https://hqs-apps.scout.me/dwf/v1";
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DWF_PREFERENCE_SERVICE_URL, url);
        editor.commit();
        
        debugLog(DwfLogger.INFO, "dwf_service_url : " + dwf_service_url);
    }
    
    public void setDwfWebUrl(String url) throws RemoteException
    {
        this.dwf_web_page_url = url;
        //this.url = "http://hqs-apps.scout.me/dwf/go";
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DWF_PREFERENCE_DWF_WEB_PAGE_URL, url);
        editor.commit();
        
        debugLog(DwfLogger.INFO, "dwf_web_url : " + dwf_web_page_url);
    }

    @Override
    public void setShareLocationInterval(long interval) throws RemoteException
    {
        DwfShareLocationTask.getInstance().setInteval(interval);
        
        debugLog(DwfLogger.INFO, "setInteval : " + interval);
    }

    @Override
    public void startShareLocation(String sessionId, Friend host, String formattedDt) throws RemoteException
    {
        DwfShareLocationTask.getInstance().start(this.service, this.dwf_service_url, sessionId, host, formattedDt);
    }

    @Override
    public void updateStatus(long eta, String status, double lat, double lon) throws RemoteException
    {
        DwfShareLocationTask.getInstance().updateStatus(eta, status, lat, lon);
    }

    @Override
    public void stopShareLocation() throws RemoteException
    {
        DwfShareLocationTask.getInstance().stop(this.service);
    }

    @Override
    public void addUpdateListener(DwfUpdateListener listener, String key) throws RemoteException
    {
        DwfShareLocationTask.getInstance().addUpdateListener(listener, key);
    }

    public void clearUpdateListener() throws RemoteException
    {
        DwfShareLocationTask.getInstance().clearUpdateListener();
    }
    
    @Override
    public void removeUpdateListener(String key) throws RemoteException
    {
        DwfShareLocationTask.getInstance().removeUpdateLister(key);
    }

    @Override
    public DwfResponseAidl request(DwfRequestAidl request) throws RemoteException
    {
        DwfResponseAidl responseAidl = new DwfResponseAidl();

        if (request.getRequest() instanceof InviteFriendRequest)
        {
            InviteFriendRequest inviteFriendRequest = (InviteFriendRequest) request.getRequest();

            debugLog(DwfLogger.INFO, "InviteFriendRequest : \n" + inviteFriendRequest);
            
            InviteFriendResponse inviteFriendResponse = DwfNetworkUtil.requestInviteFriend(this.dwf_service_url, inviteFriendRequest);
            
            debugLog(DwfLogger.INFO, "InviteFriendResponse : \n" + inviteFriendResponse);
            
            if (inviteFriendResponse.getStatus() == DwfResponseStatus.OK)
            {

                GetSessionDetailRequest getSessionDetailRequest = new GetSessionDetailRequest();
                getSessionDetailRequest.setSessionId(inviteFriendResponse.getSessionId());
                getSessionDetailRequest.setUserId(inviteFriendRequest.getUserId());
                getSessionDetailRequest.setUserKey(inviteFriendRequest.getUserKey());

                DwfRequestAidl detailRequestAidl = new DwfRequestAidl();
                detailRequestAidl.setRequest(getSessionDetailRequest);
                DwfResponseAidl detailResponseAidl = request(detailRequestAidl);

                if (detailResponseAidl.getResponse() instanceof GetSessionDetailResponse)
                {
                    GetSessionDetailResponse sessionDetailResponse = (GetSessionDetailResponse) detailResponseAidl
                            .getResponse();
                    if (sessionDetailResponse.getStatus() == DwfResponseStatus.OK)
                    {
                        String dt = sessionDetailResponse.getDt();
                        String formattedDt = sessionDetailResponse.getFormattedDt();
                        String sessionId = sessionDetailResponse.getSessionId();
                        String name = "";
                        
                        if(formattedDt != null)
                        {
                            Address address = DwfUtil.jsonToAddress(formattedDt);
                            name = address.getLabel();
                        }
                        for (Friend f : sessionDetailResponse.getFriends())
                        {
                            StringBuilder url = new StringBuilder(dwf_web_page_url);
                            url.append("?token=").append(JsonS4AProxy.PRODUCTION_TOKEN);
                            url.append("&name=").append(name);
                            url.append("&dt=").append(dt);
                            url.append("&session_id=").append(sessionId);
                            url.append("&user_key=").append(f.getKey());
                            if(formattedDt != null && formattedDt.length() > 0)
                            {
                                url.append("&formated_dt=").append(formattedDt);
                            }
                            f.setUrl(url.toString());
                        }

                        ArrayList<Friend> fs = DwfNetworkUtil.requestTinyUrl(this.dwf_service_url, sessionDetailResponse.getFriends());
                        if (fs == null)
                        {
                            inviteFriendResponse.setStatus(DwfResponseStatus.PARAMETER_ERROR);
                        }
                        else
                        {
                            for (Friend f : sessionDetailResponse.getFriends())
                            {
                                for (Friend tf : fs)
                                {
                                    if (f.getKey().equals(tf.getKey()))
                                    {
                                        f.setUrl(tf.getUrl());
                                    }
                                }
                            }
                            inviteFriendResponse.setFriends(sessionDetailResponse.getFriends());
                        }
                    }
                    else
                    {
                        inviteFriendResponse.setStatus(sessionDetailResponse.getStatus());
                    }
                }

            }
            responseAidl.setResponse(inviteFriendResponse);

            return responseAidl;
        }
        else if (request.getRequest() instanceof NewSessionRequest)
        {
            NewSessionRequest newSessionRequest = (NewSessionRequest) request.getRequest();
            
            debugLog(DwfLogger.INFO, "NewSessionRequest : \n" + newSessionRequest);

            NewSessionResponse newSessionResponse = DwfNetworkUtil.requestNewSession(this.dwf_service_url, newSessionRequest);
            
            debugLog(DwfLogger.INFO, "NewSessionResponse : \n" + newSessionResponse);

            if (newSessionResponse.getStatus() == DwfResponseStatus.OK && newSessionRequest.getFriends().size() > 1)
            {
                InviteFriendRequest inviteFriendRequest = new InviteFriendRequest();
                inviteFriendRequest.setFriends(newSessionRequest.getFriends());
                inviteFriendRequest.setSessionId(newSessionResponse.getSessionId());
                inviteFriendRequest.setDt(newSessionRequest.getDt());
                inviteFriendRequest.setName(newSessionRequest.getName());
                inviteFriendRequest.setToken(newSessionRequest.getToken());

                Friend hostFriend = null;
                for (Friend f : newSessionRequest.getFriends())
                {
                    if (f.getType() == FriendType.HOST)
                    {
                        hostFriend = f;
                        break;
                    }
                }

                inviteFriendRequest.setUserId(hostFriend.getUid());
                inviteFriendRequest.setUserKey(hostFriend.getKey());

                DwfRequestAidl newRequestAidl = new DwfRequestAidl();
                newRequestAidl.setRequest(inviteFriendRequest);
                DwfResponseAidl inviteResponseAidl = request(newRequestAidl);
                if (inviteResponseAidl.getResponse() instanceof InviteFriendResponse)
                {
                    InviteFriendResponse inviteFriendResponse = (InviteFriendResponse) inviteResponseAidl.getResponse();
                    newSessionResponse.setFriends(inviteFriendResponse.getFriends());
                    newSessionResponse.setStatus(inviteFriendResponse.getStatus());
                }
            }

            responseAidl.setResponse(newSessionResponse);

            return responseAidl;
        }
        else if (request.getRequest() instanceof GetSessionDetailRequest)
        {
            GetSessionDetailRequest getSessionDetailRequest = (GetSessionDetailRequest) request.getRequest();
            
            debugLog(DwfLogger.INFO, "GetSessionDetailRequest : \n" + getSessionDetailRequest);

            GetSessionDetailResponse getSessionResponse = DwfNetworkUtil
                    .requestSessionDetail(this.dwf_service_url, getSessionDetailRequest, this.service);
            
            debugLog(DwfLogger.INFO, "GetSessionDetailResponse : \n" + getSessionResponse);

            if(getSessionResponse.getStatus() == DwfResponseStatus.OK)
                DwfShareLocationTask.getInstance().setFriends(getSessionResponse.getFriends());

            responseAidl.setResponse(getSessionResponse);
            return responseAidl;
        }

        return responseAidl;
    }

    @Override
    public Intent getSharingIntent() throws RemoteException
    {
        return DwfShareLocationTask.getInstance().getNotificationIntent();
    }

    @Override
    public void setMainAppStatus(String status) throws RemoteException
    {
        DwfShareLocationTask.getInstance().setMainAppStatus(status);
    }

    @Override
    public void enableNotification(boolean enable, String status) throws RemoteException
    {
        DwfShareLocationTask.getInstance().enableNotification(enable, status);
    }

    @Override
    public void pauseShareLocation() throws RemoteException
    {
        DwfShareLocationTask.getInstance().pause();
    }

    @Override
    public void resumeShareLocation() throws RemoteException
    {
        DwfShareLocationTask.getInstance().resume();
    }

    @Override
    public void debugLog(int level, String message) throws RemoteException
    {
        DwfLogger.getInstance().log(level, message);
    }
}
