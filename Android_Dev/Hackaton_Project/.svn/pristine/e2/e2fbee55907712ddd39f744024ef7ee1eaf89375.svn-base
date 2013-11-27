/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * UpsellDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.persistent.TnStore;

/**
 *@author bduan
 *@date 2011-2-24
 */
public class UpsellDao extends AbstractDao
{
    protected final static int UPSELL_INFO_INDEX = 600001;
    protected final static int DISABLE_FEATURES_VERSION = 600003;
    
    protected StringMap upsellMap;
    
    protected TnStore upsellStore;

    protected TnRegionDependentStoreProvider upsellStoreProvider;
    
    public UpsellDao(TnStore startupStore)
    {
        this.upsellStore = startupStore;
        this.upsellStoreProvider = new TnRegionDependentStoreProvider(startupStore.getName(),startupStore.getType());
        init();
    }
    
    protected void init()
    {
        byte[] upsellInfoData = this.upsellStore.get(UPSELL_INFO_INDEX);
        if(upsellInfoData != null)
        {
            upsellMap = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(upsellInfoData); 
        }
    }
    
    public void setUpsellData(StringMap upsellMap)
    {
        if(upsellMap != null)
        {
            this.upsellMap = upsellMap;
            
            if(this.upsellStore != null)
            {
                this.upsellStore.put(UPSELL_INFO_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(upsellMap));
            }
        }
    }
    
    public StringMap getFeatureList()
    {
        if(upsellMap == null)
            init();
        
        return upsellMap;
    }
    
    public String getValue(String key)
    {
        if(upsellMap == null)
            init();
        
        if(upsellMap != null)
        {
            String value = (String)upsellMap.get(key);
            if(value != null && value.length() > 0)
                return value;
        }
        
        return null;
    }

    public void store()
    {
        this.upsellStore.save();
        upsellStoreProvider.save();
    }
    
    public void clear()
    {
        this.upsellMap = null;
        this.upsellStore.clear();
        upsellStoreProvider.clear();
    }
}
