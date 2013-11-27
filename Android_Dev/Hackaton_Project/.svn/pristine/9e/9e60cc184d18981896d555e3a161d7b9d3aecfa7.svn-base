/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ResourceBackupDao.java
 *
 */
package com.telenav.data.dao.serverproxy;


import java.util.Enumeration;

import com.telenav.persistent.TnStore;

/**
 *@author qli
 *@date 2011-3-7
 */
public class ResourceBackupDao extends ResourceBarDao
{

    public ResourceBackupDao(TnStore persistableAudioStore, TnStore audioRuleStore,
            TnStore resourceBarVersionStore, TnStore cserverNodeStore , TnStore backupPreferenceStore)
    {
        super(persistableAudioStore, audioRuleStore, resourceBarVersionStore, cserverNodeStore,
                backupPreferenceStore, null, null, null, null ,null);
    }
    
    public synchronized void backupDone(String region)
    {
        ResourceBarDao resourceBar = AbstractDaoManager.getInstance().getResourceBarDao();
        
        if(resourceBar.persistableAudioStore != null)
        {
            resourceBar.persistableAudioStore.clear();
        }
        
        if(resourceBar.audioRuleStore != null)
        {
            resourceBar.audioRuleStore.clear();
        }
        
        if(resourceBar.resourceBarVersionStore != null)
        {
            resourceBar.resourceBarVersionStore.clear();
        }
        
        if(resourceBar.cserverNodeStoreProvider != null)
        {
            resourceBar.cserverNodeStore.clear();
            resourceBar.cserverNodeStoreProvider.clear();
        }
        
        
        if(resourceBar.backupPreferenceStoreProvider != null)
        {
            resourceBar.backupPreferenceStoreProvider.clear();
        }
        
        copyStore(persistableAudioStore, resourceBar.persistableAudioStore);
        copyStore(audioRuleStore, resourceBar.audioRuleStore);
        copyStore(resourceBarVersionStore, resourceBar.resourceBarVersionStore);
        if (cserverNodeStoreProvider != null)
        {
            copyStore(cserverNodeStore,
                resourceBar.cserverNodeStore);
            copyStore(cserverNodeStoreProvider.getStore(region),
                resourceBar.cserverNodeStoreProvider.getStore(region));
        }
        if (backupPreferenceStoreProvider != null)
        {
            copyStore(backupPreferenceStoreProvider.getStore(region),
                resourceBar.backupPreferenceStoreProvider.getStore(region));
        }
        resourceBar.setIsResourceSyncFinish(true);
        
        resourceBar.store();
        this.clear();
    }
    
    protected synchronized void copyStore(TnStore origStore, TnStore destStore)
    {
        if( origStore == null || destStore == null )
        {
            return;
        }
        
        Enumeration keys = origStore.keys();
        while( keys.hasMoreElements() )
        {
            String key = (String)keys.nextElement();
            destStore.put(key, origStore.get(key));
        }
    }
    
}
