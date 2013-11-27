/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PersistentResourceBundle.java
 *
 */
package com.telenav.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.logger.Logger;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.util.StringTokenizer;

/**
 * Resource bundles contain locale-specific objects, for this class, will store the resource into persistent system.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 15, 2010
 */
public class PersistentResourceBundle
{
    private static final String DEFAULT_FAMILY = "defaultfamily";
    
    protected Hashtable dataStoreTable;
    
    ResourceBundle resourceBundle;
    
    protected Hashtable textCaches;
    /**
     * construct a persistent resource bundle.
     * @param resourceBundle
     */
    PersistentResourceBundle(ResourceBundle resourceBundle)
    {
        this.resourceBundle = resourceBundle;
        
        dataStoreTable = new Hashtable();
        
        textCaches = new Hashtable();
    }
    
    String getString(int key, String familyName)
    {
        String storeName = getDataPath(ResourceBundle.TEXT_PATH, false) + familyName;
        Hashtable results = (Hashtable) textCaches.get(storeName);
        Integer keyInt = new Integer(key);
        String value = results == null ? null : (String)results.get(keyInt);
        if (value != null)
        {
            return value;
        }
        
        TnStore dataStore = getDataStore(ResourceBundle.TEXT_PATH, familyName, false);
        try
        {
            byte[] data = dataStore.get(key);
            if(data == null)
                return null;
            
            if (results == null)
            {
                results = new Hashtable();
                textCaches.put(storeName, results);
            }
            
            value = new String(dataStore.get(key), "utf-8"); 
            results.put(keyInt, value);
            
            return value;
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.log(this.getClass().getName(), e);
            return null;
        }
    }
    
    /**
     * Update String in persistent resource bundle.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @param value String form of resource object.
     */
    public void putString(int key, String familyName, String value)
    {
        if(familyName == null || familyName.trim().length() == 0)
        {
            throw new IllegalArgumentException("The family name is empty.");
        }
        
        if(value == null || value.trim().length() == 0)
        {
            return;
        }
        
        TnStore dataStore = getDataStore(ResourceBundle.TEXT_PATH, familyName, false);

        String storeName = getDataPath(ResourceBundle.TEXT_PATH, false) + familyName;
        Hashtable results = (Hashtable) textCaches.get(storeName);
        if (results == null)
        {
            results = new Hashtable();
            textCaches.put(storeName, results);
        }
        
        results.put(new Integer(key), value);
        
        try
        {
            dataStore.put(key, value.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }
    
    /**
     * Store data into persistent resource bundle.
     */
    public void store()
    {
        Vector names = getStoreNames();
        Enumeration dataStores = dataStoreTable.elements();
        while (dataStores.hasMoreElements())
        {
            TnStore store = (TnStore) dataStores.nextElement();
            store.save();

            boolean isContained = false;
            for (int i = 0; i < names.size(); i++)
            {
                StoreBean bean = (StoreBean)names.elementAt(i);
                if (store.getName().equals(bean.storeName))
                {
                    isContained = true;
                    break;
                }
            }
            if (!isContained)
            {
                StoreBean bean = new StoreBean();
                bean.storeName = store.getName();
                bean.storeType = store.getType();
            }
        }

        this.resourceBundle.metaStore.put(ResourceBundle.META_STORE_NAMES + Locale.getLocaleId(this.resourceBundle.getLocale()), beansToString(names).getBytes());

        this.resourceBundle.metaStore.save();
    }
    
    private String beansToString(Vector names)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < names.size(); i++)
        {
            StoreBean bean = (StoreBean)names.elementAt(i);
            sb.append(bean.storeName + ":" + bean.storeType + ",");
        }
        
        return sb.toString();
    }
    
    private Vector getStoreNames()
    {
        byte[] nameData = this.resourceBundle.metaStore.get(ResourceBundle.META_STORE_NAMES
                + Locale.getLocaleId(this.resourceBundle.getLocale()));
        if (nameData == null)
            return new Vector();

        String s = new String(nameData);
        StringTokenizer st = new StringTokenizer(s, ",");
        Vector names = new Vector();
        while(st.hasMoreTokens())
        {
            String token = st.nextToken();
            int equalIndex = token.indexOf('=');
            if(equalIndex != -1)
            {
                StoreBean bean = new StoreBean();
                bean.storeName = token.substring(0, equalIndex);
                bean.storeType = Integer.parseInt(token.substring(equalIndex + 1));
                names.addElement(bean);
            }
        }
        return names;
    }
    
    /**
     * Clear data in persistent resource bundle.
     */
    public void clear()
    {
        Vector names = getStoreNames();
        for (int i = 0; i < names.size(); i++)
        {
            StoreBean bean = (StoreBean)names.elementAt(i);
            TnStore store = this.getDataStore(bean.storeType, bean.storeName);
            store.clear();
        }
        dataStoreTable.clear();
        textCaches.clear();
    }
    
    void clearCurrentLocaleData()
    {
        Vector keys = new Vector();
        Enumeration dataStores = dataStoreTable.elements();
        while(dataStores.hasMoreElements())
        {
            TnStore store = (TnStore)dataStores.nextElement();
            if(store.getName().indexOf("_" + ResourceBundle.GENERIC_PATH + "_") == -1)
            {
                keys.addElement(store.getName());
            }
        }
        
        for(int i = 0; i < keys.size(); i++)
        {
            dataStoreTable.remove(keys.elementAt(i));
        }
        
        textCaches.clear();
    }
    
    byte[] getImage(String name, String familyName)
    {
        TnStore dataStore = getDataStore(ResourceBundle.IMAGE_PATH, familyName, false);

        byte[] data = dataStore.get(name);
        
        return data;
    }
    
    /**
     * Store image into persistent resource bundle.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @param value image form of resource object.
     */
    public void putImage(String key, String familyName, byte[] data)
    {
        if (data == null)
        {
            return;
        }

        TnStore dataStore = getDataStore(ResourceBundle.IMAGE_PATH, familyName, false);

        dataStore.put(key, data);
    }
    
    byte[] getGenericImage(String name, String familyName)
    {
        TnStore dataStore = getDataStore(ResourceBundle.IMAGE_PATH, familyName, true);

        byte[] data = dataStore.get(name);
        
        return data;
    }
    
    /**
     * Store image into persistent resource bundle.
     * <br />
     * these images are generic, not in any locale group.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @param value image form of resource object.
     */
    public void putGenericImage(String key, String familyName, byte[] data)
    {
        if (data == null)
        {
            return;
        }

        TnStore dataStore = getDataStore(ResourceBundle.IMAGE_PATH, familyName, true);

        dataStore.put(key, data);
    }
    
    byte[] getAudio(int key, String family)
    {
        TnStore dataStore = getDataStore(ResourceBundle.AUDIO_PATH, family, false);

        byte[] data = dataStore.get(key);
        
        return data;
    }
    
    /**
     * Store audio into persistent resource bundle.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @param value audio form of resource object.
     */
    public void putAudio(int key, String familyName, byte[] data)
    {
        if (data == null)
        {
            return;
        }
        
        TnStore dataStore = getDataStore(ResourceBundle.AUDIO_PATH, familyName, false);

        dataStore.put(key, data);
    }
    
    byte[] getBinary(String name, String familyName)
    {
        TnStore dataStore = getDataStore(ResourceBundle.BINARY_PATH, familyName, false);

        byte[] data = dataStore.get(name);
        
        return data;
    }
    
    /**
     * Store binary into persistent resource bundle.
     * <br />
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @param value binary form of resource object.
     */
    public void putBinary(String key, String familyName, byte[] data)
    {
        if (data == null)
        {
            return;
        }

        TnStore dataStore = getDataStore(ResourceBundle.BINARY_PATH, familyName, false);

        dataStore.put(key, data);
    }
    
    byte[] getGenericBinary(String name, String familyName)
    {
        TnStore dataStore = getDataStore(ResourceBundle.BINARY_PATH, familyName, true);

        byte[] data = dataStore.get(name);
        
        return data;
    }
    
    /**
     * Store binary into persistent resource bundle.
     * <br />
     * these binaries are generic, not in any locale group.
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @param value binary form of resource object.
     */
    public void putGenericBinary(String key, String familyName, byte[] data)
    {
        if (data == null)
        {
            return;
        }

        TnStore dataStore = getDataStore(ResourceBundle.BINARY_PATH, familyName, true);

        dataStore.put(key, data);
    }
    
    private TnStore getDataStore(String dataPath, String familyName, boolean isGeneric)
    {
        familyName = familyName == null || familyName.trim().length() == 0 ? DEFAULT_FAMILY : familyName;

        String storeName = getDataPath(dataPath, isGeneric) + familyName;
        
        TnStore dataStore = (TnStore) dataStoreTable.get(storeName);
        if (dataStore == null)
        {
            dataStore = getDataStore(ResourceBundle.TEXT_PATH.equals(dataPath) ? TnPersistentManager.RMS_CRUMB
                    : TnPersistentManager.RMS_CHUNK, storeName);
            
            dataStoreTable.put(storeName, dataStore);
        }

        return dataStore;
    }
    
    private TnStore getDataStore(int storeType, String storeName)
    {
        TnStore dataStore = this.resourceBundle.persistentManager.createStore(storeType, storeName);
        dataStore.load();
        
        return dataStore;
    }
    
    private String getDataPath(String dataPath, boolean isGeneric)
    {
        return ResourceBundle.ROOT_PATH + "_" + (isGeneric ? ResourceBundle.GENERIC_PATH : this.resourceBundle.getLocale()) + "_"
                + dataPath + "_";
    }
    
    static class StoreBean
    {
        public String storeName;
        public int storeType;
    }
}
