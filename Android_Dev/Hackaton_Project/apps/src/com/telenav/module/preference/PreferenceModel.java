/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceConstants.java
 *
 */
package com.telenav.module.preference;

import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.ISettingDataProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.logger.Logger;
import com.telenav.module.FriendlyUserRatingUtil;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.dashboard.DashboardManager;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.ui.citizen.map.MapContainer;
/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-18
 */
class PreferenceModel extends BrowserSdkModel implements
        IPreferenceConstants
{
    
    protected String allValidEmailChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&'*+-./=?^_{|}~";
    
    PreferenceModel()
    {
       
    }
    
	protected void doActionDelegate(int actionId)
	{
	    switch(actionId)
	    {
	        case ACTION_PREFERENCE_INIT:
	        {
	            Hashtable hash = new Hashtable();
	            put(KEY_O_USER_INFO, hash);
	            postModelEvent(EVENT_MODEL_PREFERENCE_GOTO_ROOT);
	            break;
	        }
	        case ACTION_PREFERENCE_CHANGING_CHECK:
	        {
	            boolean isChanged = isChangePreferences();
	            if(isChanged)
	            {
	                postModelEvent(EVENT_MODEL_PREFERENCE_CHANGED);
	            }else
	            {
	                postModelEvent(EVENT_MODEL_BACK_TO_ROOT);
	            }
	            break;
	        }
	        case ACTION_CAR_CHANGING_CHECK:
	        {
	            int selectedCarIndex = this.getInt(KEY_I_CAR_MODEL_VALUE);
	            int currentCarIndex = getStoredCarIndex();
	            if(currentCarIndex < 0)currentCarIndex = 0;
	            if(selectedCarIndex != currentCarIndex)
	            {
	                this.put(KEY_B_IS_PREFERENCE_CHANGE, true);
	                postModelEvent(EVENT_MODEL_CAR_CHANGED);
	            }
	            else
	            {
	                postModelEvent(EVENT_MODEL_BACK_TO_ROOT);
	            }
	            break;
	        }
	        case ACTION_SAVING_UPLOAD:
	        {
	            boolean isNeedStore = this.getBool(KEY_B_IS_PREFERENCE_CHANGE);
	            if(isNeedStore)
	            {
	                DaoManager.getInstance().getPreferenceDao().store(this.getRegion());
	                boolean isRouteStyleChanged = this.fetchBool(KEY_B_IS_ROUTE_STYLE_CHANGE);
	                if(isRouteStyleChanged)
	                {
	                    DashboardManager.getInstance().notifyUpdateEta();
	                }
	                
	                DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SETTING_DATA_NEED_UPLOAD, true);
	                DaoManager.getInstance().getSimpleConfigDao().store();
	                
	                boolean isOnBoardMode = !NetworkStatusManager.getInstance().isConnected();
	                
	                if(isOnBoardMode)
	                {
	                    Logger.log(Logger.INFO, this.getClass().getName(), "Preference delay sync: onboard mode", null, null, true);
	                    // no network connection
	                    
                        NetworkStatusManager.getInstance().addStatusListener(new PreferenceCoverageListener());
	                }
	                else
	                {
	                    uploadSettingData();
	                }
	                
	            }
	            postModelEvent(EVENT_MODEL_BACK_TO_ROOT);
	            break;
	        }
	        case ACTION_PREFENCE_RESET:
	        {
	            remove(KEY_B_IS_PREFERENCE_CHANGE);
	            remove(KEY_B_IS_NEED_UPLOAD);
	            ((DaoManager)DaoManager.getInstance()).getPreferenceDao().resetTempPreference();
	            ((DaoManager)DaoManager.getInstance()).getPreferenceDao().load(true, this.getRegion());
	            break;
	        }
	        case ACTION_CHECK_RETURNED_PREFERENCE_VALUE:
	        {
	            PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
	            int oldRouteTypeValue = fetchInt(KEY_I_OLD_ROUTE_STYLE_VALUE);
	            int oldRouteSettingValue = fetchInt(KEY_I_OLD_ROUTE_SETTING_VALUE);
	            int currentRouteTypeValue = preferenceDao.getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
	            int currentRouteSettingValue = preferenceDao.getIntValue(Preference.ID_PREFERENCE_ROUTE_SETTING);
	            if(oldRouteTypeValue != currentRouteTypeValue || oldRouteSettingValue != currentRouteSettingValue)
	            {
	                put(KEY_B_IS_ROUTE_STYLE_CHANGE, true);
	                put(KEY_B_IS_PREFERENCE_CHANGE, true);
	            }
	            break;
	        }
	        case ACTION_CHECK_LANGUAGE_CHANGED:
	        {
	            if(this.fetchBool(KEY_B_IS_LANGUAGE_CHANGE))
	            {
	                postModelEvent(EVENT_MODEL_LANGUAGE_CHANGE);
	            }
	            else
	            {
	                postModelEvent(EVENT_MODEL_COMMON_BACK);
	            }
	            break;
	        }
	        case ACTION_CHANGE_CAR:
	        {
	            int selectedCarIndex = this.getInt(KEY_I_CAR_MODEL_VALUE);
                PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
                preferenceDao.setStrValue(Preference.ID_PREFERENCE_CAR_MODEL, carModFileNames[selectedCarIndex]);
                preferenceDao.store(this.getRegion());
                postModelEvent(EVENT_MODDEL_CAR_CHANGED_FINISH);
                MapContainer.getInstance().addCarModel(null,carModFileNames[selectedCarIndex]+".mod");
                break;
	        }
	        case ACTION_CHECK_MIGRATION:
	        {
	            if(MigrationExecutor.getInstance().isSyncEnabled())
	            {
	                MigrationExecutor.getInstance().doMigration(null);
	            }
	            break;
	        }
            case ACTION_SET_CANCEL_SUBSCRIPTION_FLAG:
	        {
	            if(this.fetchBool(KEY_B_IS_CANCEL_SUBSCRIPTION_SUCC))
	            {
	                put(KEY_B_IS_FROM_SUBSCRIPTION, true);
	                postModelEvent(EVENT_MODEL_SUBSCRIPTION_CANCEL_SUCCESS);
	            }
	            break;
	        }
            case ACTION_DELETE_HOME:
            {
                DaoManager.getInstance().getAddressDao().setHomeAddress(null);
                DaoManager.getInstance().getAddressDao().store();
                backgroundSync();
                break;
            }
            case ACTION_DELETE_WORK:
            {
                DaoManager.getInstance().getAddressDao().setOfficeAddress(null);
                DaoManager.getInstance().getAddressDao().store();
                backgroundSync();
                break;
            }
            case ACTION_RATE_APP:
            {
                FriendlyUserRatingUtil.doRateApp();               
                DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_USED_RATE_SCOUT, true);
                DaoManager.getInstance().getSimpleConfigDao().store();
                break;
            }
	    }
	}
	
	private void backgroundSync()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        SyncResExecutor.getInstance().syncPreference(null, IToolsProxy.SYNC_TYPE_UPLOAD, this, userProfileProvider, IToolsProxy.UPLOAD_TYPE_HOME_WORK);
    }
	
	private void uploadSettingData()
	{
	    IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
	    SyncResExecutor.getInstance().syncSettingData(null, userProfileProvider, this, ISettingDataProxy.SYNC_TYPE_UPLOAD);
	}
	
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
    }
    
    protected boolean isChangePreferences()
    {
        boolean isChange = this.getBool(KEY_B_IS_PREFERENCE_CHANGE);
        return isChange;
    }

    protected int getStoredCarIndex()
    {
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        String currentCarValue = preferenceDao.getStrValue(Preference.ID_PREFERENCE_CAR_MODEL);
        int index= -1;
        for(int i =0 ; i < carModFileNames.length; i++)
        {
            if(carModFileNames[i].equalsIgnoreCase(currentCarValue))
            {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if(proxy instanceof ISettingDataProxy)
        {
            return;
        }
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        postErrorMessage(errorMessage);
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if(proxy instanceof ISettingDataProxy || proxy instanceof IToolsProxy)
        {
            return;
        }
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        postErrorMessage(errorMessage);
    }
}