/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DaoManager.java
 *
 */
package com.telenav.data.dao.misc;

import java.util.Enumeration;

import com.telenav.cache.AbstractCache;
import com.telenav.cache.LRUCache;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressBackupDao;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.dao.serverproxy.DsrServerDrivenParamsDao;
import com.telenav.data.dao.serverproxy.ExpressAddressDao;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.MisLogDao;
import com.telenav.data.dao.serverproxy.NearCitiesDao;
import com.telenav.data.dao.serverproxy.ResourceBackupDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.dao.serverproxy.ServiceLocatorDao;
import com.telenav.data.dao.serverproxy.StartupDao;
import com.telenav.data.dao.serverproxy.UpsellDao;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.media.rule.AudioRuleManager;
import com.telenav.module.region.RegionUtil;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 21, 2010
 */

public class DaoManager extends AbstractDaoManager
{
    protected AddressDao addressDao;
    
    protected AddressBackupDao backupAddressDao;
    
    protected ExpressAddressDao expressAddressDao;
    
    protected MisLogDao misLogDao;
    
    protected NearCitiesDao nearCitiesDao;
    
    protected ResourceBarDao resourceBarDao;
    
    protected ServerDrivenParamsDao serverDrivenParamsDao;
    
    protected ServerDrivenParamsDao dsrServerDrivenParamsDao;
    
    protected ServiceLocatorDao serverMappingDao;
    
    protected SimpleConfigDao simpleConfigDao;
    
    protected SecretSettingDao secretSettingDao;
    
    protected CommHostDao commHostDao;
    
    protected PreferenceDao prefDao;
    
    protected AppStoreDao appStoreDao;
    
    protected MiniMapDao miniMapDao;
    
    protected LocationCacheDao locationCacheDao;
    
    protected AudioRuleManager ruleManager;
    
    protected BrowserSdkServiceDao browserSdkServiceDao;
    
    protected TripsDao tripsDao;
    
    protected TnStore ruleStore;
    
    protected UpsellDao upsellDao;
    
    protected ResourceBackupDao resourceBackupDao;
    
    protected ApacheResouceDao apacheResouceDao;
    
    protected ApacheResouceDao apacheIndexDao;
    
    protected ApacheResouceDao apacheIndexBackupDao;
    
    protected AndroidBillingDao androidBillingDao;
    
    protected NavExitAbnormalDao navExitAbnormalDao;
    
    protected TnStore audioRulePreloadDao;
    
    protected UpgradeDao upgradeDao;
    
    protected DwfContactsDao dwfContactsDao;

    public DaoManager()
    {
    }
    
    public MandatoryNodeDao getMandatoryNodeDao()
    {
        return MandatoryNodeDao.getInstance();
    }
    
    public synchronized AddressDao getAddressDao()
    {
        if(addressDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "address");
            store.load();
            
            addressDao = new AddressDao(store);
        }
        
        return addressDao;
    }
    
    public synchronized AddressBackupDao getBackupAddressDao()
    {
        if(backupAddressDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "address_backup");
            store.load();
            
            backupAddressDao = new AddressBackupDao(store);
        }
        
        return backupAddressDao;
    }
    
    
    public synchronized ExpressAddressDao getExpressAddressDao()
    {
        if(expressAddressDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CHUNK, "express_address");
            store.load();
            
            expressAddressDao = new ExpressAddressDao(store);
        }
        
        return expressAddressDao;
    }

    public BillingAccountDao getBillingAccountDao()
    {
        return BillingAccountDao.getInstance();
    }
    
    public synchronized MisLogDao getMisLogDao()
    {
        if(misLogDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "mislog");
            store.load();
            
            misLogDao = new MisLogDao(store);
        }
        
        return misLogDao;
    }

    public synchronized NearCitiesDao getNearCitiesDao()
    {
        if(nearCitiesDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "near_cities");
            store.load();

            nearCitiesDao = new NearCitiesDao(store);
        }
        
        return nearCitiesDao;
    }
    
    public synchronized DwfContactsDao getDwfContactsDao()
    {
        if(dwfContactsDao == null)
        {
            dwfContactsDao = new DwfContactsDao("dwf_contacts");
        }
        
        return dwfContactsDao;
    }
    
    public synchronized LocationCacheDao getLocationCacheDao()
    {
        if(locationCacheDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "location_cache");
            store.load();

            locationCacheDao = new LocationCacheDao(store);
        }
        
        return locationCacheDao;
    }
    
    public synchronized UpgradeDao getUpgradeDao()
    {
        if(upgradeDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "upgrade_dao");
            store.load();

            upgradeDao = new UpgradeDao(store);
        }
        
        return upgradeDao;
    }

    public synchronized ResourceBarDao getResourceBarDao()
    {
        if(resourceBarDao == null)
        {
            TnStore audioStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CHUNK, "persistent_audio");
            audioStore.load();
            
            TnStore barVersionStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "bar_version");
            barVersionStore.load();
            TnStore cserverNodeStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "cserver_node");
            cserverNodeStore.load();
            TnStore backupPreferenceStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "backup_preference_static_data");
            backupPreferenceStore.load();
            
            AbstractCache dynamicAudioCache = new LRUCache(100);
            AbstractCache audioRuleCache = new LRUCache(100);
            AbstractCache navAudiosCache = new LRUCache(100);
            AbstractCache trafficAudiosCache = new LRUCache(100);

            resourceBarDao = new ResourceBarDao(audioStore, this.getAudioRuleStore(), barVersionStore, cserverNodeStore, backupPreferenceStore,
                    dynamicAudioCache, audioRuleCache, navAudiosCache, trafficAudiosCache,TnIoManager.getInstance());
            
            
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, DaoManager.class.getName(), " ------- persistent_audio --------- : " + audioStore.size());
                int[] ids = resourceBarDao.getStaticAudioIds();
                if(ids == null)
                {
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), " ------- persistent_audio --------- no audio ");
                }
                else
                {
                    for(int i = 0 ; i < ids.length ; i ++)
                    {
                        Logger.log(Logger.INFO, DaoManager.class.getName(), " ------- persistent_audio --------- load id : " + ids[i]);
                    }
                }
            }
        }
        
        return resourceBarDao;
    }

    public synchronized ServerDrivenParamsDao getServerDrivenParamsDao()
    {
        if(serverDrivenParamsDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "server_driven");
            store.load();
            
            serverDrivenParamsDao = new ServerDrivenParamsDao(store);
        }
        
        return serverDrivenParamsDao;
    }
    
    public synchronized ServerDrivenParamsDao getDsrServerDrivenParamsDao()
    {
        if(dsrServerDrivenParamsDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "dsr_dsp");
            store.load();
            
            dsrServerDrivenParamsDao = new DsrServerDrivenParamsDao(store);
        }
        
        return dsrServerDrivenParamsDao;        
    }

    public synchronized ServiceLocatorDao getServiceLocatorDao()
    {
        if(serverMappingDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "server_mapping");
            store.load();
            
            serverMappingDao = new ServiceLocatorDao(store);
        }
        
        return serverMappingDao;
    }

    public StartupDao getStartupDao()
    {
        return StartupDao.getInstance();
    }

    public synchronized SimpleConfigDao getSimpleConfigDao()
    {
        if(simpleConfigDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "simple_config");
            store.load();
            
            TnStore carrierMappingStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "carrier_mapping");
            carrierMappingStore.load();
            
            simpleConfigDao = new SimpleConfigDao(store, carrierMappingStore);
        }
        
        return simpleConfigDao;
    }
    
    public synchronized SecretSettingDao getSecretSettingDao()
    {
        if(secretSettingDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "secret_setting");
            store.load();
            
            secretSettingDao = new SecretSettingDao(store);
        }
        
        return secretSettingDao;
    }
    
    public synchronized CommHostDao getCommHostDao()
    {
        if(commHostDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "comm_host");
            store.load();
            
            commHostDao = new CommHostDao(store);
        }
        
        return commHostDao;
    }
    
    public synchronized PreferenceDao getPreferenceDao()
    {
        if(prefDao == null)
        {
            //use database because it's small and critical
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.DATABASE, "preference_data");
            store.load();
            prefDao = new PreferenceDao(store);
        }
        return prefDao;
    }
    
    public synchronized AppStoreDao getAppStoreDao()
    {
        if(appStoreDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CHUNK, "appstore_resource");
            store.load();
            appStoreDao = new AppStoreDao(store);
        }
        return appStoreDao;
    }
    
    public synchronized MiniMapDao getMiniMapDao()
    {
        if(miniMapDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CHUNK, "miniMap_resource");
            store.load();
            miniMapDao = new MiniMapDao(store);
        }
        return miniMapDao;
    }
    
    public synchronized AudioRuleManager getRuleManager()
    {
        if(ruleManager == null)
        {
            AbstractCache rulesCache = new LRUCache(150);
            
            ruleManager = new AudioRuleManager(this.getAudioRuleStore(), rulesCache);
        }
        
        return ruleManager;
    }
    
    private synchronized TnStore getAudioRulePreloadStore()
    {
        if(audioRulePreloadDao == null)
        {
            //use database because it's small and critical
            audioRulePreloadDao = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "audio_rule_preload");
            audioRulePreloadDao.load();
        }
        
        return audioRulePreloadDao;
    }
    
    private synchronized TnStore getAudioRuleStore()
    {
        if(ruleStore == null)
        {
            //use database because it's small and critical
            ruleStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.DATABASE, "audio_rule");
            ruleStore.load();
        }
        return ruleStore;
    }
    
    public synchronized boolean convertAudioRuleDatabaseToFile()
    {
        ruleStore = getAudioRuleStore();
        if(ruleStore.size() <= 0)
        {
            return false;
        }
        Enumeration keys = ruleStore.keys();
        TnStore fileStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "audio_rule_preload");
        fileStore.load();
        while(keys.hasMoreElements())
        {
            String id= (String)keys.nextElement();
            fileStore.put(id, ruleStore.get(id));
        }
        fileStore.save();
        return true;
    }
    
    public synchronized void preloadAudioRule()
    {
        if (audioRulePreloadDao == null)
        {
            getAudioRulePreloadStore();
        }
        
        if (ruleStore == null)
        {
            getAudioRuleStore();
        }
        
        if (audioRulePreloadDao == null || ruleStore == null)
        {
            return;
        }
        
        Enumeration keys = audioRulePreloadDao.keys();
        while( keys.hasMoreElements() )
        {
            String key = (String)keys.nextElement();
            ruleStore.put(key, audioRulePreloadDao.get(key));
        }
        
        ruleStore.save();
    }

    public synchronized BrowserSdkServiceDao getBrowserSdkServiceDao()
    {
        if(browserSdkServiceDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "browserSdkService_data");
            store.load();
            browserSdkServiceDao = new BrowserSdkServiceDao(store);
        }
        
        return browserSdkServiceDao;
    }
    
    public synchronized TripsDao getTripsDao()
    {
        if (tripsDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "trips");
            store.load();
            tripsDao = new TripsDao(store);
        }
        return tripsDao;
    }
    
    public synchronized UpsellDao getUpsellDao()
    {
        if (upsellDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.DATABASE, "upsell");
            store.load();
            upsellDao = new UpsellDao(store);
        }
        return upsellDao;
    }
    
    public synchronized ResourceBackupDao getResourceBackupDao()
    {
        if(resourceBackupDao == null)
        {
            TnStore audioStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CHUNK, "persistent_audio_backup");
            audioStore.load();
            TnStore ruleStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "audio_rule_backup");
            ruleStore.load();
            TnStore barVersionStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "bar_version_backup");
            barVersionStore.load();
            TnStore cserverNodeStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "cserver_node_backup");
            cserverNodeStore.load();
            TnStore backupPreferenceStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "backup_preference_static_data_backup");
            backupPreferenceStore.load();

            resourceBackupDao = new ResourceBackupDao(audioStore, ruleStore, barVersionStore, cserverNodeStore, backupPreferenceStore);
        }
        
        return resourceBackupDao;
    }  
    
    
    public synchronized ApacheResouceDao getApacheResouceBackupDao()
    {
        if(apacheResouceDao == null)
        {
            TnStore apacheResourceStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CHUNK, "apache_I18n_resource");
            apacheResourceStore.load();
            apacheResouceDao = new ApacheResouceDao(apacheResourceStore);
        }
        return apacheResouceDao;
    }
    
    public synchronized ApacheResouceDao getApacheServerIndexDao()
    {
        if(apacheIndexDao == null)
        {
            TnStore apacheResourceStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "apache_I18n_index");
            apacheResourceStore.load();
            apacheIndexDao = new ApacheResouceDao(apacheResourceStore);
        }
        return apacheIndexDao;
    }
    
    public synchronized ApacheResouceDao getApacheIndexBackupDao()
    {
        if(apacheIndexBackupDao == null)
        {
            TnStore apacheResourceStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "apache_I18n_index_backup");
            apacheResourceStore.load();
            apacheIndexBackupDao = new ApacheResouceDao(apacheResourceStore);
        }
        return apacheIndexBackupDao;
    }
    
    public synchronized AndroidBillingDao getAndroidBillingDao()
    {
        if(androidBillingDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.DATABASE, "market_billing");
            store.load();
            
            androidBillingDao = new AndroidBillingDao(store);
        }
        return androidBillingDao;
    }
    
    public synchronized NavExitAbnormalDao getNavExitAbnormalDao()
    {
        if(navExitAbnormalDao == null)
        {
            TnStore store = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "nav_exit_abnormal_backup");
            store.load();
            
            navExitAbnormalDao = new NavExitAbnormalDao(store);
        }
        return navExitAbnormalDao;
    }
    
    public static void logCallStack(String msg)
    {
        if(AppConfigHelper.isLoggerEnable)
        {
            Logger.log(Logger.ERROR, DaoManager.class.getName(), "------ Call Stack -----" + msg);
            
            Throwable throwable = new Throwable();

            StackTraceElement[] stackElements = throwable.getStackTrace();
           
            if(stackElements != null)
            {
                for(int i = 0; i < stackElements.length; i++)
                {
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), stackElements[i].getClassName());
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), stackElements[i].getFileName());
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), "" + stackElements[i].getLineNumber());
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), stackElements[i].getMethodName());
                    Logger.log(Logger.ERROR, DaoManager.class.getName(), "-----------------------------------");
                }
            }
        }
    }
    
    
    public void clearUserPersonalData()
    {
        logCallStack("clearUserPersonalData");
        
        try
        {
            DaoManager.getInstance().getMandatoryNodeDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getAddressDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getExpressAddressDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getAppStoreDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getMiniMapDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getBillingAccountDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getUpgradeDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getResourceBarDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getPreferenceDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getUpsellDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
    }
    
    public void clearInternalRMS()
    {
        clearInternalRMS(true);
    }
    
    public void clearInternalRMS(boolean needClearBrowserData)
    {
        logCallStack("clearInternalRMS , needClearBrowserData = " + needClearBrowserData);
        
        //clear each dao.
        try
        {
            DaoManager.getInstance().getAddressDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        addressDao = null;
        
        try
        {
            DaoManager.getInstance().getExpressAddressDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        expressAddressDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getAppStoreDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        appStoreDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getMiniMapDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        miniMapDao = null;
        
        try
        {
            DaoManager.getInstance().getBillingAccountDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getUpgradeDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        upgradeDao = null;
        
        try
        {
            if(needClearBrowserData)
            {
                ((DaoManager)DaoManager.getInstance()).getBrowserSdkServiceDao().clear();
            }
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getCommHostDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        commHostDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        locationCacheDao = null;
        
        try
        {
            DaoManager.getInstance().getMandatoryNodeDao().clear();
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        

        try
        {
            ((DaoManager)DaoManager.getInstance()).getMisLogDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        misLogDao = null;
        
        try
        {
            DaoManager.getInstance().getNearCitiesDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        nearCitiesDao = null;

        try
        {
            ((DaoManager)DaoManager.getInstance()).getPreferenceDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        prefDao = null;
        
        try
        {
            DaoManager.getInstance().getResourceBarDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        resourceBarDao = null;
        resourceBackupDao = null;
        
        try
        {
            DaoManager.getInstance().getServerDrivenParamsDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        serverDrivenParamsDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getDsrServerDrivenParamsDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        dsrServerDrivenParamsDao = null;
        
        try
        {
            DaoManager.getInstance().getServiceLocatorDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        serverMappingDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        simpleConfigDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getSecretSettingDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        secretSettingDao = null;
        
        try
        {
            DaoManager.getInstance().getStartupDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getTripsDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        tripsDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getUpsellDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        upsellDao = null;
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getNavExitAbnormalDao().clear();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        navExitAbnormalDao = null;
        
        if(needClearBrowserData)//if so, clear all files.
        {
            //clear the directory directly.
            FileStoreManager.clearFileSystem(TnPersistentManager.FILE_SYSTEM_INTERNAL);
        }
    }
    
    public static void saveInternalRMS()
    {
        //clear each dao.
        try
        {
            DaoManager.getInstance().getAddressDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getExpressAddressDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getAppStoreDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getMiniMapDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getBillingAccountDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getBrowserSdkServiceDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getCommHostDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getMandatoryNodeDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }

        try
        {
            ((DaoManager)DaoManager.getInstance()).getMisLogDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getNearCitiesDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }

        try
        {
            ((DaoManager)DaoManager.getInstance()).getPreferenceDao().store(RegionUtil.getInstance().getCurrentRegion());
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getResourceBarDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getServerDrivenParamsDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getDsrServerDrivenParamsDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            DaoManager.getInstance().getServiceLocatorDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getSecretSettingDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        
        try
        {
            DaoManager.getInstance().getStartupDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getTripsDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
        
        try
        {
            ((DaoManager)DaoManager.getInstance()).getUpsellDao().store();
        }catch(Exception e)
        {
            Logger.log(DaoManager.class.getName(), e);
        }
    }

}
