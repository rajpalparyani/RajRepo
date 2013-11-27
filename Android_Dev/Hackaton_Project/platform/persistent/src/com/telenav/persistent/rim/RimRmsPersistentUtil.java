/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimRmsPersistentUtil.java
 *
 */
package com.telenav.persistent.rim;

import net.rim.device.api.crypto.SHA1Digest;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import com.telenav.logger.Logger;
import com.telenav.persistent.rim.IRimPersistentContext.IRimPersistentObject;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
class RimRmsPersistentUtil
{
    static void deleteData(long storeKey)
    {
        try
        {
            synchronized (PersistentStore.getSynchObject())
            {
                PersistentStore.destroyPersistentObject(storeKey);
            }
        }
        catch (Throwable e)
        {
            Logger.log(RimRmsPersistentUtil.class.getName(), e);
        }
    }
    
    static IRimPersistentObject loadData(long storeKey)
    {
        try
        {
            synchronized (PersistentStore.getSynchObject())
            {
                PersistentObject store = PersistentStore.getPersistentObject(storeKey);
                Object contentObj = store.getContents();
                if (contentObj instanceof IRimPersistentObject)
                {
                    return (IRimPersistentObject) contentObj;
                }
                else
                {
                    return null;
                }
            }
        }
        catch (Throwable e)
        {
            Logger.log(RimRmsPersistentUtil.class.getName(), e);
            
            return null;
        }
    }
    
    static void storeData(long storeKey, IRimPersistentObject persistentObject)
    {
        try
        {
            synchronized (PersistentStore.getSynchObject())
            {
                PersistentObject store = PersistentStore.getPersistentObject(storeKey);
                store.setContents(persistentObject);
                store.commit();
            }
        }
        catch (Throwable e)
        {
            Logger.log(RimRmsPersistentUtil.class.getName(), e);
        }
    }
    
    static long stringToLong(String str)
    {
        SHA1Digest sha1Digest = new SHA1Digest();
        sha1Digest.update(str.getBytes());
        byte[] hashValBytes = sha1Digest.getDigest();
        long hashValLong = 0;
        for(int i = 0; i < 8; i++)
        {
            hashValLong |= ((long)(hashValBytes[i] & 0x0FF) << (8 * i));
        }
        
        return hashValLong;
    }
}
