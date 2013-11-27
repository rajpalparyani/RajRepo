/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SyncResExecutor.java
 *
 */
package com.telenav.module.sync;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IBatchProxy;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.logger.Logger;
import com.telenav.module.sync.apache.I18nRequestManager;
import com.telenav.module.sync.apache.Ii18nRequestListener;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-2-23
 */
public class SyncResExecutor
{
    private static final SyncResExecutor INSTANCE = new SyncResExecutor();
    
    public static SyncResExecutor getInstance()
    {
        return INSTANCE;
    }
    
    public void syncI18nResource(String locale, Ii18nRequestListener listener)
    {
        boolean isSyncI18NEnabled = DaoManager.getInstance().getServerDrivenParamsDao().isI18NSyncEnabled();
        if (isSyncI18NEnabled)
        {
            I18nRequestManager.getInstance().start(locale, listener);
        }
        else
        {
            if (listener != null)
            {
                listener.update(Ii18nRequestListener.SUCCESSFUL, 0);
            }
        }
        
    }
    
    public void syncPreference(Hashtable userPrefers, byte type, IServerProxyListener listener, IUserProfileProvider userProfileProvider, int uploadType)
    {
        if(type == IToolsProxy.SYNC_TYPE_UPLOAD)
        {
            if (uploadType == IToolsProxy.UPLOAD_TYPE_USER_INFO)
            {
                DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_USER_PROFILE_NEED_UPLOAD, true);
            }
            
            if (uploadType == IToolsProxy.UPLOAD_TYPE_HOME_WORK)
            {
                DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_HOME_WORK_NEED_UPLOAD, true);
            }
        }
        
        boolean isUserProfileNeedUpload = DaoManager.getInstance().getSimpleConfigDao()
                .getBoolean(SimpleConfigDao.KEY_USER_PROFILE_NEED_UPLOAD);
        boolean isHomeWorkNeedUpload = DaoManager.getInstance().getSimpleConfigDao()
                .getBoolean(SimpleConfigDao.KEY_HOME_WORK_NEED_UPLOAD);

        boolean forceUpload = (isUserProfileNeedUpload | isHomeWorkNeedUpload);

        if (forceUpload)
        {
            type = IToolsProxy.SYNC_TYPE_UPLOAD;
        }

        // force upload if it's need to be uploaded. No matter what type it is.
        
        if (type == IToolsProxy.SYNC_TYPE_UPLOAD)
        {
            if (!NetworkStatusManager.getInstance().isConnected())
            {
                NetworkStatusManager.getInstance().addStatusListener(SyncPreferenceCoverageListener.getInstance());
                
                return;
            }
            
            if (isUserProfileNeedUpload)
            {
                uploadUserProfile(userPrefers, listener, userProfileProvider);
            }

            if (isHomeWorkNeedUpload)
            {
                uploadHomeWork(listener, userProfileProvider);
            }
        }
        else
        {
            downloadPreference(listener, userProfileProvider);
        }
    }
    
    public void uploadUserProfile(Hashtable userPrefers, IServerProxyListener listener, IUserProfileProvider userProfileProvider)
    {
        if(userPrefers == null)
        {
            userPrefers = new Hashtable();

            PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
            String email = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_EMAIL);
            String firstName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME);
            String lastName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME);

            userPrefers.put(IToolsProxy.KEY_FIRSTNAME, firstName);
            userPrefers.put(IToolsProxy.KEY_LASTNAME, lastName);
            userPrefers.put(IToolsProxy.KEY_EMAIL, email);
            userPrefers.put(IToolsProxy.KEY_USERNAME, "");
        }
        
        String firstName = (String)userPrefers.get(IToolsProxy.KEY_FIRSTNAME);
        String lastName = (String)userPrefers.get(IToolsProxy.KEY_LASTNAME);
        if( firstName == null || firstName.length() <= 0 )
        {
            firstName = "$NULL$";//kewen & junhang told XNAV server requirements code
            userPrefers.put(IToolsProxy.KEY_FIRSTNAME, firstName);
        }
        
        if( lastName == null || lastName.length() <= 0 )
        {
            lastName = "$NULL$";//kewen & junhang told XNAV server requirements code
            userPrefers.put(IToolsProxy.KEY_LASTNAME, lastName);
        }
        
        IToolsProxy toolsProxy = ServerProxyFactory.getInstance().createToolsProxy(null, CommManager.getInstance().getComm(), listener, userProfileProvider);
        toolsProxy.syncPreference(IToolsProxy.SYNC_TYPE_UPLOAD, userPrefers, IToolsProxy.UPLOAD_TYPE_USER_INFO);
    }
    
    public void uploadHomeWork(IServerProxyListener listener, IUserProfileProvider userProfileProvider)
    {
        IToolsProxy toolsProxy = ServerProxyFactory.getInstance().createToolsProxy(null, CommManager.getInstance().getComm(), listener, userProfileProvider);
        toolsProxy.syncPreference(IToolsProxy.SYNC_TYPE_UPLOAD, null, IToolsProxy.UPLOAD_TYPE_HOME_WORK);
    }
    
    public void downloadPreference(IServerProxyListener listener, IUserProfileProvider userProfileProvider)
    {
        IToolsProxy toolsProxy = ServerProxyFactory.getInstance().createToolsProxy(null, CommManager.getInstance().getComm(), listener, userProfileProvider);
        toolsProxy.syncPreference(IToolsProxy.SYNC_TYPE_DOWNLOAD, null, -1);
    }
    
    public void syncCombinationResource(Vector combination, IServerProxyListener listener, IUserProfileProvider userProfileProvider)
    {
        syncCombinationResource(combination, listener, false, userProfileProvider);
    }
    
    public void syncCombinationResource(Vector combination, IServerProxyListener listener, boolean isNeedBackup, IUserProfileProvider userProfileProvider)
    {
        ISyncCombinationResProxy syncCombinationResProxy = ServerProxyFactory.getInstance().createSyncCombinationResProxy(null, CommManager.getInstance().getComm(), listener, userProfileProvider);
        syncCombinationResProxy.syncCombinationRes(combination, isNeedBackup);
    }
    
    public void handlePreferenceResp(IToolsProxy toolsProxy)
    {
        if (toolsProxy == null)
            return;

        int syncType = toolsProxy.getPreferenceSyncType();
        if (syncType == IToolsProxy.SYNC_TYPE_DOWNLOAD)
        {
            int loginType = DaoManager.getInstance().getBillingAccountDao().getLoginType();
            Hashtable prefers = ((IToolsProxy) toolsProxy).getUserPrefers();
            if (prefers != null)
            {
                String firstNameOnWeb = (String) prefers.get(IToolsProxy.KEY_FIRSTNAME);
                String lastNameOnWeb = (String) prefers.get(IToolsProxy.KEY_LASTNAME);
                String emailOnWeb = (String) prefers.get(IToolsProxy.KEY_EMAIL);
                String usernameOnWeb = (String) prefers.get(IToolsProxy.KEY_USERNAME);
                String csrId = (String) prefers.get(IToolsProxy.KEY_CSRID);
                PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
                prefDao.setStrValue(Preference.ID_PREFERENCE_CSRID, csrId);
                if (loginType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_IN
                        || loginType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
                {
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME, firstNameOnWeb);
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME, lastNameOnWeb);
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_EMAIL, emailOnWeb);
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_USERNAME, usernameOnWeb);
                }
                prefDao.store(getRegion());
            }
        }
    }

    protected String getRegion()
    {
        MandatoryNodeDao mandatoryNode = DaoManager.getInstance().getMandatoryNodeDao();
        MandatoryProfile profile = mandatoryNode.getMandatoryNode();
        return profile.getUserInfo().region;
    }
    
    public void syncSettingData(IBatchProxy batchProxy, IUserProfileProvider userProfileProvider, IServerProxyListener listener, int syncType)
    {
        boolean isNeedUpload = DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_SETTING_DATA_NEED_UPLOAD);
    
        if(isNeedUpload && syncType == ISettingDataProxy.SYNC_TYPE_DOWNLOAD)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Preference delay sync: the flag is true, need upload", null, null, true);
            syncType = ISettingDataProxy.SYNC_TYPE_UPLOAD;
        }
        
        switch(syncType)
        {
            case ISettingDataProxy.SYNC_TYPE_UPLOAD:
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Preference delay sync: send upload request", null, null, true);
                break;
            }
            case ISettingDataProxy.SYNC_TYPE_DOWNLOAD:
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Preference delay sync: send download request", null, null, true);
                break;
            }
            case ISettingDataProxy.SYNC_TYPE_FORCE_DOWNLOAD:
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "Preference delay sync: send force download request", null, null, true);
                break;
            }
        }
        
        ISettingDataProxy settingDataProxy = ServerProxyFactory.getInstance().createSettingDataProxy(null, CommManager.getInstance().getComm(), listener, userProfileProvider);
        
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        long timeStamp = 0;
        try
        {
            timeStamp = Long.parseLong(preferenceDao.getSyncPreferenceSettingDataTime(this.getRegion()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String version = ((DaoManager) DaoManager.getInstance()).getResourceBarDao().getPreferenceSettingVersion(this.getRegion());
        version = version == null ? "" : version;
        
        //this logic is mainly used by sync resource.
        if(syncType == ISettingDataProxy.SYNC_TYPE_FORCE_DOWNLOAD)
        {
            // we will for sure download data if these values are empty.
            timeStamp = 0;
            version = "";
        }
        
        Hashtable settingDatas = preferenceDao.getSettingData();
        
        if(batchProxy != null)
        {
            RequestItem requestItem = new RequestItem(IServerProxyConstants.ACT_SYNC_CLIENT_SETTING_DATA,
                (AbstractServerProxy)settingDataProxy);
            requestItem.params = new Vector();
            requestItem.params.addElement(version);
            requestItem.params.addElement(timeStamp);
            if(syncType == ISettingDataProxy.SYNC_TYPE_UPLOAD)
            {
                requestItem.params.addElement(settingDatas);
            }
            batchProxy.addBatchItem(requestItem);
        }
        else
        {
            if(syncType == ISettingDataProxy.SYNC_TYPE_UPLOAD)
            {
                settingDataProxy.syncSettingData(version, timeStamp, settingDatas);
            }
            else
            {
                settingDataProxy.syncSettingData(version, timeStamp);
            }
        }
    }
}
