/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SwitchLangModel.java
 *
 */
package com.telenav.module.preference.language;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.ApacheResourceSaver;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.UserInfo;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.i18n.ResourceBundle;
import com.telenav.logger.Logger;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.dashboard.AdJuggerManager;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.module.sync.apache.I18nRequestManager;
import com.telenav.module.sync.apache.Ii18nRequestListener;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPreference;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@modifier wzhu
 *@date 2011-3-9
 */
class SwitchLanguageModel extends AbstractCommonNetworkModel implements ISwitchLanguageConstants, Ii18nRequestListener
{
    private final static boolean needBackupResourceBarDao = true;
    
    private final static int SYNC_SUCCESS = 1;
    private final static int SYNC_FAILED = 2;
    
    private int syncCserverStatus;
    private int syncI18nStatus;
    private boolean isCancelled;
    
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_SWITCHLANG_SYNC:
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage ACTION_SWITCHLANG_SYNC start");
                handleSyncRes();
                Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage ACTION_SWITCHLANG_SYNC end");
                break;
            }
            case ACTION_SWITCHLANG_CANCEL:
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage ACTION_SWITCHLANG_CANCEL start");
                isCancelled = true;
                I18nRequestManager.getInstance().stop();
                CommManager.getInstance().getComm().cancelAll();
                
                rollBack();
                
                MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
                mandatoryNodeDao.store();
                
                DaoManager.getInstance().getResourceBackupDao().clear();
                Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage ACTION_SWITCHLANG_CANCEL end");
                break;
            }
            case ACTION_CHECK_NAV_IS_RUNNING:
            {
            	boolean navIsRuning = NavSdkNavEngine.getInstance().isRunning();
            	if(navIsRuning)
            	{
            		postModelEvent(EVENT_MODEL_NAV_IS_RUNNING);
            	}
            	else
            	{
            		postModelEvent(EVENT_MODEL_NAV_IS_NOT_RUNNING);
            	}
            	break;            	
            }
        }
    }
    
    protected void handleSyncRes()
    {
        isCancelled = false;
        syncCserverStatus = 0;
        syncI18nStatus = 0;
        
        clearVersion();
        Vector combination = new Vector();
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_POI_BRAND_NAME));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AIRPORT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_CATEGORY_TREE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_HOT_POI_CATEGORY));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_GOBY_EVENT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_MAP_DATA_UPGRADE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AUDIO_AND_RULE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING));
        MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
        MandatoryProfile profile = mandatoryNodeDao.getMandatoryNode();
        UserInfo userInfo = profile.getUserInfo();
        this.put(KEY_S_LAST_LOCALE, userInfo.locale);
        userInfo.locale = getString(KEY_S_CHANGED_LANGUAGE);
        //hard code, need confirm with c-server
//        userInfo.guideTone = "202";
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        userProfileProvider = userProfileProvider==null? new UserProfileProvider():userProfileProvider;
        
        Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage handleSyncRes syncCombinationResource start");
        SyncResExecutor.getInstance().syncCombinationResource(combination, this, needBackupResourceBarDao, userProfileProvider);
        Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage handleSyncRes syncCombinationResource end");
        
        ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance().createSyncPurchaseProxy(null,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
        
        Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage handleSyncRes syncI18nResource start");
        SyncResExecutor.getInstance().syncI18nResource(userInfo.locale, this);
        Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage handleSyncRes syncI18nResource end");
    }

    private void clearVersion()
    {
        if(needBackupResourceBarDao)
        {
            ResourceBarDao.copyVersions(DaoManager.getInstance().getResourceBarDao(), DaoManager.getInstance().getResourceBackupDao(), this.getRegion());
            DaoManager.getInstance().getResourceBackupDao().setAirportVersion("");
            DaoManager.getInstance().getResourceBackupDao().setAudioInventory("");
            DaoManager.getInstance().getResourceBackupDao().setAudioRuleTimestamp("");
            DaoManager.getInstance().getResourceBackupDao().setAudioTimestamp("");
            DaoManager.getInstance().getResourceBackupDao().setBrandNameVersion("", this.getRegion());
            DaoManager.getInstance().getResourceBackupDao().setCategoryVersion("", this.getRegion());
            DaoManager.getInstance().getResourceBackupDao().setHotPoiVersion("", this.getRegion());
            DaoManager.getInstance().getResourceBackupDao().setPreferenceSettingVersion("", this.getRegion());
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage clearVersion needBackupResourceBarDao " + needBackupResourceBarDao);
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if(proxy instanceof ISettingDataProxy)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionFinishedDelegate uploadSettingData(): successful");
        }
        else if(proxy instanceof IToolsProxy && IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(proxy.getRequestAction()))
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionFinishedDelegate uploadUserProfile(): successful");
            return;
        }
        else
        {
            if (proxy instanceof ISyncCombinationResProxy)
            {
                if (((ISyncCombinationResProxy) proxy).getStep() == ISyncCombinationResProxy.STEP_SYNC_FINISH)
                {
                    syncCserverStatus = SYNC_SUCCESS;
                    Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionFinishedDelegate syncCombinationResource: successful");
                }
            }
            
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionFinishedDelegate finishLocaleChanged() start");
            finishLocaleChanged();
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionFinishedDelegate finishLocaleChanged() end");
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if(proxy instanceof ISettingDataProxy)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionError uploadSettingData(): failed");
        }
        else if(proxy instanceof IToolsProxy && IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(proxy.getRequestAction()))
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionError uploadUserProfile(): failed");
            return;
        }
        else
        {
            syncCserverStatus = SYNC_FAILED;
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionError finishLocaleChanged() start");
            finishLocaleChanged();
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage transactionError finishLocaleChanged() end");
        }
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if(proxy instanceof ISettingDataProxy)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage networkError uploadSettingData(): failed");
        }
        else if(proxy instanceof IToolsProxy && IServerProxyConstants.ACT_SYNC_PREFERENCE.equals(proxy.getRequestAction()))
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage networkError uploadUserProfile(): failed");
            return;
        }
        else
        {
            syncCserverStatus = SYNC_FAILED;

            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage networkError finishLocaleChanged() start");
            finishLocaleChanged();
            Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage networkError finishLocaleChanged() end");
        }
    }

    private void rollBack()
    {
        DaoManager.getInstance().getResourceBackupDao().clear();
        MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
        MandatoryProfile profile = mandatoryNodeDao.getMandatoryNode();
        UserInfo userInfo = profile.getUserInfo();
        userInfo.locale = getString(KEY_S_LAST_LOCALE);
        
        Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage rollBack userInfo.locale " + userInfo.locale);
    }
    
	public void update(int status, int syncCount) 
	{
		if(status == Ii18nRequestListener.FAILED)
		{
		    syncI18nStatus = SYNC_FAILED;
		}
		else
		{
		    syncI18nStatus = SYNC_SUCCESS;
		}
		
		Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage update() status: " + status + " finishLocaleChanged start");
		finishLocaleChanged();
	    Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage update() status: " + status + " finishLocaleChanged end");

	}
	
	private void finishLocaleChanged()
	{
	    if(isCancelled)
	        return;
	    
	    Logger.log(Logger.INFO, this.getClass().getName(), "SwitchLanguage finishLocaleChanged(): syncI18nStatus = " + syncI18nStatus + ", syncCserverStatus = " + syncCserverStatus);
	    
	    if(syncI18nStatus > 0 || syncCserverStatus > 0)
        {
            if (syncI18nStatus == SYNC_SUCCESS && syncCserverStatus == SYNC_SUCCESS)
            {
                String changedLocale = getString(KEY_S_CHANGED_LANGUAGE);
                ResourceManager.getInstance().setLocale(changedLocale);
                ((DaoManager)DaoManager.getInstance()).getPreferenceDao().setIntValue(Preference.ID_PREFERENCE_LANGUAGE, getInt(KEY_I_CHANGED_LANGUAGE));
                ((DaoManager)DaoManager.getInstance()).getPreferenceDao().store(this.getRegion());
                ((DaoManager)DaoManager.getInstance()).getRuleManager().clearCache();
                DaoManager.getInstance().getServerDrivenParamsDao().updateLocaleForWidget(changedLocale);
                DaoManager.getInstance().getResourceBackupDao().backupDone(this.getRegion());
                
                MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
                mandatoryNodeDao.store();
                
                ApacheResourceSaver.getInstance().saveFiles();
                
                uploadSettingData();
                uploadUserProfile();
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).setCommonBaseLineAnchor(ResourceManager.getInstance()
                                .getCurrentBundle().getString(IStringCommon.RES_STANDARD_BASELINE_ANCHOR, IStringCommon.FAMILY_COMMON));
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        AdJuggerManager.getInstance().reset();
                    }
                });
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_NOTIFY_REFRESH_WIDGET, null);
                
              //init the exit message
                ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
                if(bundle != null)
                {
                    TeleNavDelegate.getInstance().setExitMsg(bundle.getString(IStringCommon.RES_MSG_APPLICATION_EXITING, IStringCommon.FAMILY_COMMON));
                }
                postModelEvent(EVENT_MODEL_SWITCHLANG_FINISHED);
            }
            else if(syncI18nStatus == SYNC_FAILED || syncCserverStatus == SYNC_FAILED)
            {
                I18nRequestManager.getInstance().stop();
                CommManager.getInstance().getComm().cancelAll();
                
                rollBack();
                
                MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
                mandatoryNodeDao.store();
                
                showErrorMsg();
            }
        }
	}

	private void uploadSettingData()
    {
	    IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
	    SyncResExecutor.getInstance().syncSettingData(null, userProfileProvider, this, ISettingDataProxy.SYNC_TYPE_UPLOAD);
    }
	
	private void uploadUserProfile()
    {
        Hashtable userPrefers = new Hashtable();
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        SyncResExecutor.getInstance().syncPreference(userPrefers, IToolsProxy.SYNC_TYPE_UPLOAD, this, userProfileProvider, IToolsProxy.UPLOAD_TYPE_HOME_WORK);
    }
	
	private void showErrorMsg()
	{
	    String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringPreference.PREFERENCE_STR_ERROR, IStringPreference.FAMILY_PREFERENCE);
	    if(errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
	    
	    
		this.put(KEY_S_COMMON_MESSAGE, errorMessage);
		postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
	}
}
