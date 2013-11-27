/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfServiceTaskCallback.java
 *
 */
package com.telenav.module.dwf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.widget.Toast;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.misc.DwfContactsDao.Group;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.json.JsonS4AProxy;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.DwfUpdateListener;
import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.dwf.aidl.Friend.FriendType;
import com.telenav.dwf.task.DwfServiceTask;
import com.telenav.dwf.task.IDwfServiceTaskCallback;
import com.telenav.dwf.vo.DwfResponse;
import com.telenav.dwf.vo.DwfResponseStatus;
import com.telenav.dwf.vo.GetSessionDetailRequest;
import com.telenav.dwf.vo.GetSessionDetailResponse;
import com.telenav.dwf.vo.InviteFriendRequest;
import com.telenav.dwf.vo.InviteFriendResponse;
import com.telenav.dwf.vo.NewSessionRequest;
import com.telenav.dwf.vo.NewSessionResponse;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.login.AccountFetcher;
import com.telenav.mvc.AbstractMvcMonitor;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author fangquanm
 *@date Jul 18, 2013
 */
public class DwfServiceTaskCallback extends DwfUpdateListener.Stub implements IDwfServiceTaskCallback
{
    public interface DwfServiceTaskCallbackListener
    {
        public void postDwfError(String message);
        public void postDwfEvent(int modelEvent);
        public void updateDwf(List<Friend> friends);
    }
    
    private DwfServiceTaskCallbackListener listener;
    
    public DwfServiceTaskCallback(DwfServiceTaskCallbackListener listener)
    {
        this.listener = listener;
    }
    
    private String sessionId;
    private Friend host;
    private Address dest;
    private ArrayList<Friend> friends;
    private String message;
    private String userKey;
    private ArrayList<Friend> resendSmsfriends;
    
    public String getSessionId()
    {
        return sessionId;
    }

    public Friend getHost()
    {
        return host;
    }

    public Address getDest()
    {
        return dest;
    }

    public ArrayList<Friend> getFriends()
    {
        return friends;
    }

    public String getMessage()
    {
        return message;
    }

    public String getUserKey()
    {
        return userKey;
    }
    
    @Override
    public void updateDwf(List<Friend> friends) throws RemoteException
    {
        this.listener.updateDwf(friends);
    }

    public void startShare(String sessionId, Friend host, Address dest)
    {
        this.sessionId = sessionId;
        this.host = host;
        this.dest = dest;
        
        if (sessionId == null || sessionId.length() == 0)
            return;

        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

        if (dwfAidl != null)
        {
            try
            {
                if(host != null)
                {
                    host.setStatus(FriendStatus.JOINED);
                }
                dwfAidl.setShareLocationInterval(10000);

                JSONObject addressJson = DwfUtil.addressToJson(dest);

                dwfAidl.startShareLocation(sessionId, host, addressJson.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            // TODO need retry....
        }
    }

    public void stopShare()
    {
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

        if (dwfAidl != null)
        {
            try
            {
                dwfAidl.updateStatus(-1, FriendStatus.END.name(), 0, 0);
                dwfAidl.stopShareLocation();
                DwfSliderPopup.getInstance().hide();
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            // TODO need retry....
        }
    }

    public void launchSession(Address dest, ArrayList<TnContact> contacts, String message, String userId)
    {
        this.dest = dest;
        this.message = message;

        Group g = new Group();
        g.groupName = "";
        g.contacts = new ArrayList<TnContact>();
        g.contacts.addAll(contacts);

        ((DaoManager) DaoManager.getInstance()).getDwfContactsDao().addGroup(g);
        ((DaoManager) DaoManager.getInstance()).getDwfContactsDao().store();

        DwfServiceTask dwfServiceTask = new DwfServiceTask(IDriveWithFriendsConstants.ACTION_REQUEST_SESSION, this);

        NewSessionRequest request = new NewSessionRequest();

        String dt = ResourceManager.getInstance().getStringConverter().convertAddress(dest.getStop(), false);
        dt += "@" + ((double) dest.getStop().getLat() / 100000d) + "," + ((double) dest.getStop().getLon() / 100000d);
        JSONObject addressJson = DwfUtil.addressToJson(dest);
        request.setDt(dt);
        request.setFormattedDt(addressJson.toString());
        request.setName(dest.getLabel());
        request.setToken(JsonS4AProxy.PRODUCTION_TOKEN);

        Friend host = new Friend();
        host.setType(FriendType.HOST);
        host.setStatus(FriendStatus.JOINED);

        String userName = DwfUtil.getUserName();
        if (userName == null || userName.length() == 0)
        {
            userName = AndroidPersistentContext.getInstance().getContext().getString(R.string.dwfFriendHost);
        }
        host.setName(userName);
        host.setImage(AccountFetcher.getUserPhoto(AndroidPersistentContext.getInstance().getContext()));

        String phoneNumber = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().plainPhoneNumber;
        host.setPhone(phoneNumber == null ? "" : phoneNumber);

        host.setUid(userId);

        TnLocation latestLocation = LocationProvider.getInstance().getLatestLocation(false);
        if (latestLocation != null)
        {
            host.setLat(latestLocation.getLatitude() / 100000.0d);
            host.setLon(latestLocation.getLongitude() / 100000.0d);
        }
        request.addFriend(host);

        for (TnContact contact : contacts)
        {
            Friend f = new Friend();
            f.setName(contact.name);
            f.setPhone(contact.phoneNumber);
            f.setType(FriendType.GUEST);

            if (contact.b != null)
            {
                try
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    contact.b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    f.setImage(byteArray);

                    stream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            request.addFriend(f);
        }

        dwfServiceTask.execute(request);
    }

    public void launchInviteNew(String sessionId, Friend host, ArrayList<TnContact> contacts, String userKey, String message, ArrayList<Friend> friends)
    {
        this.sessionId = sessionId;
        this.host = host;
        this.userKey = userKey;
        this.message = message;
        this.resendSmsfriends = friends;
        
        Group g = new Group();
        g.groupName = "";
        g.contacts = new ArrayList<TnContact>();
        g.contacts.addAll(contacts);

        ((DaoManager) DaoManager.getInstance()).getDwfContactsDao().addGroup(g);
        ((DaoManager) DaoManager.getInstance()).getDwfContactsDao().store();

        DwfServiceTask dwfServiceTask = new DwfServiceTask(IDriveWithFriendsConstants.ACTION_INVITE_NEW, this);

        InviteFriendRequest inviteFriendRequest = new InviteFriendRequest();
        inviteFriendRequest.setSessionId(sessionId);
        inviteFriendRequest.setUserKey(host.getKey());
        inviteFriendRequest.setUserId(host.getUid());

        for (TnContact contact : contacts)
        {
            Friend f = new Friend();
            f.setName(contact.name);
            f.setType(FriendType.GUEST);
            f.setPhone(contact.phoneNumber);
            
            if (contact.b != null)
            {
                try
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    contact.b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    f.setImage(byteArray);

                    stream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            inviteFriendRequest.addFriend(f);
        }
        dwfServiceTask.execute(inviteFriendRequest);
    }

    public void requestSessionDetail(String sessionId, String userKey, String userId)
    {
        DwfServiceTask dwfServiceTask = new DwfServiceTask(IDriveWithFriendsConstants.ACTION_REQUEST_SESSION_DETAIL, this);

        GetSessionDetailRequest request = new GetSessionDetailRequest();

        this.sessionId = sessionId;
        this.userKey = userKey;

        request.setSessionId(sessionId);
        request.setUserKey(userKey);
        request.setUserId(userId);

        dwfServiceTask.execute(request);
    }

    @Override
    public boolean onPreExecute(int action)
    {
        return NetworkStatusManager.getInstance().isConnected();
    }

    @Override
    public void doInBackground(int action, DwfResponse response)
    {
        if (!NetworkStatusManager.getInstance().isConnected())
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_NO_CELL_COVERAGE, IStringCommon.FAMILY_COMMON);
            response.setErrorMessage(errorMessage);
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        else if (action == IDriveWithFriendsConstants.ACTION_REQUEST_SESSION)
        {
            if (response instanceof NewSessionResponse)
            {
                NewSessionResponse newSessionResponse = (NewSessionResponse) response;

                this.sessionId = newSessionResponse.getSessionId();

                if (newSessionResponse.getFriends() != null)
                {
                    for (Friend f : newSessionResponse.getFriends())
                    {
                        if (f.getType() == FriendType.HOST)
                        {
                            this.host = f;
                            break;
                        }
                    }

                    this.friends = newSessionResponse.getFriends();
                    sendSms(this.friends);
                   
                    this.startShare(newSessionResponse.getSessionId(), host, this.dest);
                    try
                    {
                        DwfSliderPopup.getInstance().updateDwf(newSessionResponse.getFriends());
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (action == IDriveWithFriendsConstants.ACTION_INVITE_NEW)
        {
            if (response instanceof InviteFriendResponse)
            {
                InviteFriendResponse inviteFriendResponse = (InviteFriendResponse) response;

                this.sessionId = inviteFriendResponse.getSessionId();

                if (inviteFriendResponse.getFriends() != null)
                {
                    for (Friend f : inviteFriendResponse.getFriends())
                    {
                        if (f.getKey().equals(userKey))
                        {
                            this.host = f;
                            break;
                        }
                    }
                    ArrayList<Friend> oldFriends = this.friends;//(ArrayList<Friend>) this.get(KEY_V_FRIENDS);
                    ArrayList<Friend> needSmsFriends = new ArrayList<Friend>();
                    for (Friend friendNew : inviteFriendResponse.getFriends())
                    {
                        boolean isNeedAdd = true;
                        for (Friend friendOld : oldFriends)
                        {
                            if (friendNew.getKey().equals(friendOld.getKey()))
                            {
                                isNeedAdd = false;
                                break;
                            }
                        }
                        if (isNeedAdd)
                            needSmsFriends.add(friendNew);
                    }

                    this.friends = inviteFriendResponse.getFriends();
                    if(this.resendSmsfriends != null && resendSmsfriends.size() > 0)
                    {
                        needSmsFriends.addAll(resendSmsfriends);
                        resendSmsfriends.clear();
                    }
                    sendSms(needSmsFriends);
                    try
                    {
                        DwfSliderPopup.getInstance().updateDwf(inviteFriendResponse.getFriends());
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (action == IDriveWithFriendsConstants.ACTION_REQUEST_SESSION_DETAIL)
        {
            if (response instanceof GetSessionDetailResponse)
            {
                GetSessionDetailResponse getSessionDetailResponse = (GetSessionDetailResponse) response;
                this.sessionId = getSessionDetailResponse.getSessionId();
                if (getSessionDetailResponse.getFriends() != null)
                {
                    for (Friend f : getSessionDetailResponse.getFriends())
                    {
                        if (f.getKey().equals(this.userKey))
                        {
                            this.host = f;
                            break;
                        }
                    }
                    this.friends = getSessionDetailResponse.getFriends();

                    try
                    {
                        DwfSliderPopup.getInstance().updateDwf(getSessionDetailResponse.getFriends());
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (getSessionDetailResponse.getFormattedDt() != null)
                {
                    this.dest = DwfUtil.jsonToAddress(getSessionDetailResponse.getFormattedDt());
                }
            }
        }
    }

    @Override
    public void onPostExecute(int action, DwfResponse response)
    {
        switch (action)
        {
            case IDriveWithFriendsConstants.ACTION_REQUEST_SESSION_DETAIL:
            {
                if(response.getStatus() == DwfResponseStatus.OK)
                {
                    this.listener.postDwfEvent(IDriveWithFriendsConstants.EVENT_MODEL_UPDATE_SESSION_LIST);
                }
                break;
            }
            case IDriveWithFriendsConstants.ACTION_REQUEST_SESSION:
            case IDriveWithFriendsConstants.ACTION_INVITE_NEW:
            {
                Context context = (Context) AndroidPersistentContext.getInstance().getContext();
                if (response.getErrorMessage() != null && response.getErrorMessage().length() > 0)
                {
                    Toast.makeText(context, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    this.listener.postDwfError(response.getErrorMessage());
                }
                else if(response.getStatus() == DwfResponseStatus.SESSION_NOT_EXIST)
                {
                    this.listener.postDwfEvent(IDriveWithFriendsConstants.EVENT_CHECK_EXPIRATION);
                }
                else if (response.getStatus() != DwfResponseStatus.OK)
                {
                    Toast.makeText(context, R.string.dwfFailErrorMessage, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    this.listener.postDwfEvent(IDriveWithFriendsConstants.EVENT_MODEL_UPDATE_SESSION_LIST);
                }
                break;
            }
            default:
                break;
        }
    }

    public void sendSms(ArrayList<Friend> newFriends)
    {
        if (newFriends == null || newFriends.size() == 0)
            return;


        JSONArray friendsArray = null;
        
        boolean isGcmEnable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_GCM_ENABLE);
        boolean isSmsDisable = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SMS_DISABLE);
        
        String msg = this.message;
        for (Friend f : newFriends)
        {
            
            String newUrl = f.getUrl();

            if (f.getKey().equals(host.getKey()) || f.getPhone() == null || f.getPhone().trim().length() == 0)
                continue;
            if(newUrl == null || newUrl.length() == 0)
                continue;
            
            boolean isSendUrl = msg.lastIndexOf("http://") != -1;
            if (isSendUrl)
            {
                String replacedUrl = msg.substring(msg.lastIndexOf("http://"), msg.length());
                if (replacedUrl.indexOf(" ") != -1)
                {
                    replacedUrl = replacedUrl.substring(0, replacedUrl.indexOf(" "));
                }
                msg = msg.replace(replacedUrl, newUrl);
            }
            else
            {
                if (msg.lastIndexOf("Tap") != -1)
                {
                    msg = msg.substring(0, msg.lastIndexOf("Tap"));
                }
            }
            if (!isSmsDisable)
            {
                TnTelephonyManager.getInstance().startMMSAtBackground(f.getPhone(), msg);
            }
            
            if (isGcmEnable)
            {
                msg = msg.replace('@', ' ');
                msg = msg.replace('&', ' ');
                
                if (friendsArray == null)
                {
                    friendsArray = new JSONArray();
                }
                
                JSONObject friend = new JSONObject();
                try
                {
                    friend.put("ptn", f.getPhone());
                    friend.put("url", newUrl);
                    friend.put("msg", msg);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                friendsArray.put(friend);
            }
        }
        
        if (isGcmEnable && friendsArray != null && friendsArray.length() > 0)
        {
            ServerUtilities.sendUrls(friendsArray);
        }
    }
}
