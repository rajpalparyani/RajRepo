/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TnStoreDelegate.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.module.region.RegionUtil;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 * @author chrbu
 * @date 2012-2-13
 */
public class TnRegionDependentStoreProvider
{

    private static final String SEPERATOR = "_";

    private Hashtable<Integer, TnStore> tnStores = new Hashtable<Integer, TnStore>();

    private String storeName;

    private int storeType;

    public TnRegionDependentStoreProvider(String storeOriginalName, int storeType)
    {
        this.storeName = storeOriginalName;
        this.storeType = storeType;
    }

    public TnStore getStore(String region)
    {
        String targetRegion;
        TnStore store = null;
        targetRegion = checkRegion(region);
        int hashCode = targetRegion.hashCode();
        store = (TnStore) tnStores.get(hashCode);
        if (store == null)
        {
            store = createTnStore(composeFileName(targetRegion));
            tnStores.put(hashCode, store);
        }
        return store;
    }

    private String checkRegion(String region)
    {
        if (region == null || region.length() == 0)
        {
            return RegionUtil.getInstance().getCurrentRegion();
        }
        return region;
    }

    private String composeFileName(String region)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(storeName);
        sb.append(SEPERATOR);
        sb.append(region);
        return sb.toString();
    }

    private TnStore createTnStore(String fileName)
    {
        TnStore store = TnPersistentManager.getInstance()
                .createStore(storeType, fileName);
        store.load();
        return store;
    }

    public void clear()
    {
        Vector<String> regions = DaoManager.getInstance().getSimpleConfigDao()
                .getVector(SimpleConfigDao.KEY_CACHED_REGION);
        int size = regions.size();
        if (size > 0)
        {
            for (int index = 0; index < size; index++)
            {
                String region = regions.get(index);
                TnStore store = this.getStore(region);
                if (store != null)
                    store.clear();
            }
        }
        tnStores.clear();
    }

    public void save()
    {
        for (Iterator itr = tnStores.keySet().iterator(); itr.hasNext();)
        {
            Integer key = (Integer) itr.next();
            TnStore store = (TnStore) tnStores.get(key);
            store.save();
        }
    }

}
