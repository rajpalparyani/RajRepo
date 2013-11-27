/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DriveWithFriendsModel.java
 *
 */
package com.telenav.module.dwf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.MainAppStatus;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.module.dwf.DwfServiceTaskCallback.DwfServiceTaskCallbackListener;
import com.telenav.module.login.AccountFetcher;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.ui.citizen.map.MapContainer;

/**
 * @author fangquanm
 * @date Jul 1, 2013
 */
class DriveWithFriendsModel extends AbstractCommonNetworkModel implements IDriveWithFriendsConstants,
        DwfServiceTaskCallbackListener
{
    private DwfServiceTaskCallback dwfServiceTaskListener;

    @Override
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                dwfServiceTaskListener = new DwfServiceTaskCallback(this);
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
                if (dwfAidl != null)
                {
                    try
                    {
                        dwfAidl.clearUpdateListener();
                        dwfAidl.addUpdateListener(dwfServiceTaskListener, dwfServiceTaskListener.toString());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                String sessionId = this.getString(KEY_S_DWF_SESSION_ID);
                if (sessionId != null && sessionId.length() > 0)
                {
                    this.requestSessionDetail();
                    this.postModelEvent(EVENT_MODEL_LAUNCH_SESSION_LIST);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                }
                break;
            }
            case ACTION_SELECT_ADDRESS:
            {
                break;
            }
            case ACTION_REQUEST_SESSION:
            {
                String sessionId = this.getString(KEY_S_DWF_SESSION_ID);
                Friend host = (Friend) this.get(KEY_O_HOST);

                @SuppressWarnings("unchecked")
                ArrayList<TnContact> contacts = (ArrayList<TnContact>) this.get(KEY_V_CONTACTS);
                contacts = filterDuplicate(contacts);
                
                String userKey = this.getString(KEY_S_DWF_USER_KEY);
                String message = this.getString(KEY_S_SMS);
                String userId = this.getString(KEY_S_DWF_USER_ID);
                if (userId == null || userId.trim().length() == 0)
                {
                    userId = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId;
                }
                
                boolean isInviteNew = this.getBool(KEY_B_INVITE_NEW);
                if (isInviteNew)
                {
                    ArrayList<TnContact> newContacts = new ArrayList<TnContact>();
                    ArrayList<Friend> duplicatedFriends = new ArrayList<Friend>();
                    ArrayList<Friend> friends = (ArrayList<Friend>)get(KEY_V_FRIENDS);
                    for(int i=0; i<contacts.size(); i++)
                    {
                        TnContact contact = contacts.get(i);
                        if(contact != null)
                        {
                            Friend f = getFriendByPhoneNumber(contact.phoneNumber, friends);
                            if(f == null)
                            {
                                newContacts.add(contact);
                            }
                            else
                            {
                                duplicatedFriends.add(f);
                            }
                        }
                    }
                    if(host == null)
                    {
                        host = new Friend();
                        host.setKey(userKey);
                    }
                    dwfServiceTaskListener.launchInviteNew(sessionId, host, newContacts, userKey, message, duplicatedFriends);
                    this.postModelEvent(EVENT_MODEL_LAUNCH_SESSION_LIST);
                }
                else
                {
                    Address dest = (Address) this.get(KEY_O_SELECTED_ADDRESS);
                    dwfServiceTaskListener.launchSession(dest, contacts, message, userId);
                    KontagentAssistLogger.ktLogDwfInitialFriendsCount(contacts.size());
                    if (!DriveWithFriendsViewTouch.isCurrentLocation(dest))
                    {
                        this.postModelEvent(EVENT_MODEL_LAUNCH_ROUTE_PLAN);
                    }
                    else
                    {
                        this.postModelEvent(EVENT_MODEL_LAUNCH_SESSION_LIST);
                    }
                }
                break;
            }
            case ACTION_LAUNCH_SESSION_LIST:
            {
                MapContainer.getInstance().resume();

                this.fetch(KEY_V_CONTACTS);
                this.fetch(KEY_B_INVITE_NEW);

                String sessionId = this.getString(KEY_S_DWF_SESSION_ID);
                Friend f = (Friend) this.get(KEY_O_HOST);
                Address dest = (Address) this.get(KEY_O_SELECTED_ADDRESS);
                if(f == null)
                {
                    f = new Friend();
                    String userKey = this.getString(KEY_S_DWF_USER_KEY);
                    f.setKey(userKey);
                }
                
                if(f != null)
                {
                    String userId = this.getString(KEY_S_DWF_USER_ID);
                    if (userId == null || userId.trim().length() == 0)
                    {
                        userId = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId;
                    }
                    
                    f.setUid(userId);
                    f.setStatus(FriendStatus.JOINED);
                    if(f.getName() == null || userId.trim().length() == 0)
                    {
                        String userName = DwfUtil.getUserName();
                        if (userName != null && userName.length() > 0)
                        {
                            f.setName(userName);
                        }
                    }
                    if(AccountFetcher.getUserPhoto(AndroidPersistentContext.getInstance().getContext()) != null)
                    {
                        f.setImage(AccountFetcher.getUserPhoto(AndroidPersistentContext.getInstance().getContext()));
                    }
                }
                
                dwfServiceTaskListener.startShare(sessionId, f, dest);
                break;
            }
            case ACTION_LEAVE_GROUP:
            {
                dwfServiceTaskListener.stopShare();
                break;
            }
            case ACTION_INVITE_NEW:
            {
                this.put(KEY_B_INVITE_NEW, true);
                break;
            }
            case ACTION_FROM_PLUGIN:
            {
                this.fetch(KEY_V_FRIENDS);
                
                dwfServiceTaskListener.stopShare();
                
                @SuppressWarnings("rawtypes")
                Hashtable pluginRequest = (Hashtable) this.get(ICommonConstants.KEY_O_PLUGIN_REQUEST);

                String sessionId = (String) pluginRequest.get(IMaiTai.KEY_DWF_SESSION_ID);
                String userKey = (String) pluginRequest.get(IMaiTai.KEY_DWF_USER_KEY);
                String userId = (String) pluginRequest.get(IMaiTai.KEY_DWF_USER_ID);
                String addressDt = (String) pluginRequest.get(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
                boolean isExitDirectly = false;
                if(pluginRequest.containsKey(IMaiTai.KEY_DWF_EXIT_APP))
                {
                    isExitDirectly = (Boolean) pluginRequest.get(IMaiTai.KEY_DWF_EXIT_APP);
                }
                
                this.put(ICommonConstants.KEY_S_DWF_SESSION_ID, sessionId);
                this.put(ICommonConstants.KEY_S_DWF_USER_KEY, userKey);
                this.put(ICommonConstants.KEY_S_DWF_USER_ID, userId);
                this.put(ICommonConstants.KEY_B_DWF_LAUNCH_APP, isExitDirectly);
                this.remove(KEY_S_SMS);
                if(addressDt != null && addressDt.length() > 0)
                {
                    this.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, DwfUtil.jsonToAddress(addressDt));
                }
                else
                {
                    this.remove(ICommonConstants.KEY_O_SELECTED_ADDRESS);
                }
                requestSessionDetail();
                this.postModelEvent(EVENT_MODEL_LAUNCH_SESSION_LIST);
                break;
            }
        }
    }
    
   

    protected void requestSessionDetail()
    {
        String sessionId = this.getString(KEY_S_DWF_SESSION_ID);
        String userKey = this.getString(KEY_S_DWF_USER_KEY);
        String userId = this.getString(KEY_S_DWF_USER_ID);
        if (userId == null || userId.trim().length() == 0)
        {
            userId = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId;
        }
        dwfServiceTaskListener.requestSessionDetail(sessionId, userKey, userId);
    }

    @Override
    protected void deactivateDelegate()
    {
        MapContainer.getInstance().pause();
    }

    @Override
    protected void releaseDelegate()
    {
        super.releaseDelegate();

        if (this.dwfServiceTaskListener != null)
        {
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

            if (dwfAidl != null)
            {
                try
                {
                    dwfAidl.removeUpdateListener(this.dwfServiceTaskListener.toString());
                    dwfAidl.setMainAppStatus(MainAppStatus.foreground.name());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void activateDelegate(boolean isUpdateView)
    {
        if (this.getState() == STATE_SESSION_LIST)
        {
            MapContainer.getInstance().resume();
        }
        
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

        if (dwfAidl != null)
        {
            try
            {
                dwfAidl.setMainAppStatus(MainAppStatus.dwfScreen.name());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
    }

    @Override
    public void updateDwf(List<Friend> friends)
    {
        this.put(KEY_V_FRIENDS, friends);
        this.put(KEY_B_FRIENDS_UPDATED, true);
        this.postModelEvent(EVENT_MODEL_UPDATE_SESSION_LIST);
    }

    @Override
    public void postDwfError(String message)
    {
    }

    @Override
    public void postDwfEvent(int modelEvent)
    {
        this.put(KEY_S_DWF_SESSION_ID, this.dwfServiceTaskListener.getSessionId());
        this.put(KEY_O_HOST, this.dwfServiceTaskListener.getHost());
        this.put(KEY_V_FRIENDS, this.dwfServiceTaskListener.getFriends());
        this.put(KEY_O_SELECTED_ADDRESS, this.dwfServiceTaskListener.getDest());
        this.put(KEY_S_DWF_USER_KEY, this.dwfServiceTaskListener.getUserKey());
        this.put(KEY_S_SMS, this.dwfServiceTaskListener.getMessage());
        
        this.postModelEvent(modelEvent);
    }
    
    private Friend getFriendByPhoneNumber(String phoneNumber,  ArrayList<Friend> friends)
    {
        if(phoneNumber == null || friends == null)
        {
            return null;
        }
        for (int i = 0; i < friends.size(); i++)
        {
            Friend f = friends.get(i);
            if(phoneNumber.equalsIgnoreCase(f.getPhone()))
            {
                return f;
            }
        }
        return null;
    }
    
    private ArrayList<TnContact> filterDuplicate(ArrayList<TnContact> contacts)
    {
        HashMap map = new HashMap();
        for(int i=0; i<contacts.size(); i++)
        {
            TnContact contact = contacts.get(i);
            map.put(contact.phoneNumber, contact);
        }
        Collection collection = map.values();
        ArrayList<TnContact> newContacts = new ArrayList(collection);
        return newContacts;
    }
}
