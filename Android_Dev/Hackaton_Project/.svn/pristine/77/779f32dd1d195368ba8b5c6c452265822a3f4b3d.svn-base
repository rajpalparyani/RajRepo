/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ServerMappingDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class ServiceLocatorDao extends AbstractDao
{
    //version
    private final static int SERVER_MAPPINGS_VERSION_INDEX = 500002;
    
    //server driven.
    private final static int ALIAS_URL_MAP_INDEX = 500001;
    
    private final static int ACTION_ALIAS_MAP_INDEX = 500003;
    
    private final static int ALIAS_SUFFIX_MAP_INDEX = 500004;
    
    
    //for secret setting.
    private final static int SECRET_ACTION_URL_MAP_INDEX = 500005;
    
    private final static int SECRET_ALIAS_URL_MAP_INDEX = 500005;    
        
    private ServiceLocator locator;

    private TnStore serverMappingStore;

    
    public ServiceLocatorDao(TnStore serverMappingStore)
    {
        this.serverMappingStore = serverMappingStore;
    }

    public void setServiceLocator(ServiceLocator locator)
    {
        this.locator = locator;
    }
    
//    public String getVersion()
//    {
//        if(version != null && version.length() > 0)
//            return this.version;
//        
//        if (this.serverMappingStore != null)
//        {
//            byte[] data = this.serverMappingStore.get(SERVER_MAPPINGS_VERSION_INDEX);
//            if (data != null)
//            {
//                version = new String(data);
//            }
//        }
//
//        return version;
//    }

    public StringMap getAliasUrlMap()
    {
        if (this.serverMappingStore != null)
        {
            byte[] data = this.serverMappingStore.get(ALIAS_URL_MAP_INDEX);
            if (data != null)
            {
                StringMap serverMappings = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data);

                return serverMappings;
            }
        }

        return null;
    }
    
    public StringMap getAliasSuffixMap()
    {
        if (this.serverMappingStore != null)
        {
            byte[] data = this.serverMappingStore.get(ALIAS_SUFFIX_MAP_INDEX);
            if (data != null)
            {
                StringMap serviceUrlMappings = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data);
                
                return serviceUrlMappings;
            }
        }
        
        return null;
    }
    
    public StringMap getActionAliasMap()
    {
        if (this.serverMappingStore != null)
        {
            byte[] data = this.serverMappingStore.get(ACTION_ALIAS_MAP_INDEX);
            if (data != null)
            {
                StringMap actionMap = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data);

                return actionMap;
            }
        }

        return null;
    }
    
    public StringMap getSecretAliasUrlMap()
    {
        if (this.serverMappingStore != null)
        {
            byte[] data = this.serverMappingStore.get(SECRET_ALIAS_URL_MAP_INDEX);
            if (data != null)
            {
                StringMap stringMap = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data);

                return stringMap;
            }
        }

        return null;
    }
    
    public StringMap getSecretActionUrlMap()
    {
        if (this.serverMappingStore != null)
        {
            byte[] data = this.serverMappingStore.get(SECRET_ACTION_URL_MAP_INDEX);
            if (data != null)
            {
                StringMap stringMap = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(data);
                
                return stringMap;
            }
        }
        
        return null;
    }
    
    public void setSecretAliasUrlMap(StringMap aliasUrlMap)
    {
        if(aliasUrlMap != null)
        {
            this.serverMappingStore.put(SECRET_ALIAS_URL_MAP_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(aliasUrlMap));
        }
    }
    
    public void setSecretActionUrlMap(StringMap actionUrlMap)
    {
        if(actionUrlMap != null)
        {
            this.serverMappingStore.put(SECRET_ACTION_URL_MAP_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(actionUrlMap));
        }
    }

    public void setServerMappings(StringMap aliasUrlMap, StringMap aliasSuffixMap, StringMap actionAliasMap, String version)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "server locator version: " + version);
        
        if(aliasUrlMap != null)
        {
            this.serverMappingStore.put(ALIAS_URL_MAP_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(aliasUrlMap));
        }
        
        if(aliasSuffixMap != null)
        {
            this.serverMappingStore.put(ALIAS_SUFFIX_MAP_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(aliasSuffixMap));
        }
        
        if(actionAliasMap != null)
        {
            this.serverMappingStore.put(ACTION_ALIAS_MAP_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(actionAliasMap));
        }
        
        if(this.locator != null)
        {
            this.locator.setServerConfigServiceLocator(aliasUrlMap, aliasSuffixMap, actionAliasMap);
        }
        if (version != null)
        {
            setVersion(version);
        }
        
    }
    
    public synchronized void store()
    {
        this.serverMappingStore.save();
    }
    
    public synchronized void clear()
    {
        this.locator = null;
        this.serverMappingStore.clear();
    }
    

    private void setVersion(String version)
    {
        if (serverMappingStore != null)
        {
            serverMappingStore.put(
                SERVER_MAPPINGS_VERSION_INDEX, version.getBytes());
        }
    }

    public String getVersion()
    {
        byte[] version = serverMappingStore.get(SERVER_MAPPINGS_VERSION_INDEX);
        if (version != null)
        {
            return new String(version);
        }
        return "";
    }
}
