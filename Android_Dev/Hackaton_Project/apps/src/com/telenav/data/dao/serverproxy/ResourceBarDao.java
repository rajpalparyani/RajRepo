/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ResourceBarDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Vector;

import android.location.Location;

import com.telenav.cache.AbstractCache;
import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.map.MapDataUpgradeInfo;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioData.IAudioDataProvider;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.RuleNode;
import com.telenav.io.TnIoManager;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.preference.guidetone.GuideToneManager;
import com.telenav.navsdk.NavsdkFileUtil;
import com.telenav.persistent.TnStore;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class ResourceBarDao extends AbstractDao implements IAudioDataProvider
{
    public static final int MAX_STATIC_AUDIO_ID = 0xFFFF;
    
    public static final String AUDIO_WHAT_DEFAULT = "default";
    public static final String AUDIO_WHAT_NAVIGATION = "nav";
    public static final String AUDIO_WHAT_TRAFFIC = "traffic";
    
    protected final static int AUDIO_TIMESTAMP_INDEX = 300001;
    protected final static int AUDIO_RULE_TIMESTAMP_INDEX = 300002;
    
    protected final static int COMMON_BAR_VERSIONS_INDEX = 300003;
    protected final static int LOCALE_BAR_VERSIONS_INDEX = 300004;
    protected static final int MAP_COLOR_INDEX = 300005;
    
    protected final static int AUDIO_DEFAULT_TIMESTAMP_INDEX = 300006;
    
    public static final int AIRPORT_NODE = 300100;
    public static final int AIRPORT_VERSION = 300101;
    public static final int CATEGORY_TREE = 300102;
    public static final int CATEGORY_VERSION = 300103;
    public static final int RESOURCE_FORMAT_VERSION = 300104;
    public static final int BRAND_NAME = 300105;
    public static final int BRAND_NAME_VERSION = 300106;
    public static final int HOT_POI = 300107;
    public static final int HOT_POI_VERSION = 300108;
    public static final int MAP_DATA_UPGRADE_INFO = 300109;
    public static final int MAP_DATA_UPGRADE_INFO_VERSION = 300110;
    
    public static final int PREFERENCE_SETTING_VERSION = 300111;
    public static final int PREFERENCE_SETTING_DATA = 3001112;
    public static final int KEY_B_IS_SYNC_FINISH = 3001113;
    public static final int PREFERENCE_SETTING_BACKUP_DATA = 3001114;
    public static final int PREFERENCE_SETTING_SUM = 3001115;
    public static final int REGION_CENTER_POINT = 3001116;
    public static final int LOCAL_EVENTS = 3001117;
    public static final int LOCAL_EVENTS_VERSION = 3001118;
    
    protected TnStore persistableAudioStore;
    protected TnStore audioRuleStore;
    protected TnStore resourceBarVersionStore;
    protected TnStore cserverNodeStore;
    protected TnRegionDependentStoreProvider backupPreferenceStoreProvider;
    protected AbstractCache dynamicAudioCache;
    protected AbstractCache audioRuleCache;
    protected TnIoManager ioManager;
    /**
     * Temp nav audio caches according to AUDIO_CACHE_NAVIGATION
     */
    protected AbstractCache navAudiosCache;
    
    /**
     * Temp traffic audio caches according to AUDIO_CACHE_TRAFFIC
     */
    protected AbstractCache trafficAudiosCache;
    
    protected String audioInventory;
    protected String audioRuleInventory;
    
    protected Vector commonBarVersions;
    protected Vector localeBarVersions;
    
    protected boolean isResourceSyncFinish;
    
    private boolean isInventoryLoadSuccessed = false;
    public static String DEFAULT_INVENTORY_VALUE = "0";
    
    protected TnRegionDependentStoreProvider cserverNodeStoreProvider;
    
    public ResourceBarDao(TnStore persistableAudioStore, TnStore audioRuleStore, TnStore resourceBarVersionStore, TnStore cserverNodeStore,
            TnStore backupPreferenceStore, AbstractCache dynamicAudioCache, AbstractCache audioRuleCache, AbstractCache navAudiosCache, AbstractCache trafficAudiosCache , TnIoManager ioManager)
    {
        this.persistableAudioStore = persistableAudioStore;
        this.audioRuleStore = audioRuleStore;
        this.resourceBarVersionStore = resourceBarVersionStore;
        this.cserverNodeStore = cserverNodeStore;
        this.dynamicAudioCache = dynamicAudioCache;
        this.audioRuleCache = audioRuleCache;
        
        this.navAudiosCache = navAudiosCache;
        this.trafficAudiosCache = trafficAudiosCache;
        this.ioManager = ioManager;
        
        this.backupPreferenceStoreProvider = new TnRegionDependentStoreProvider(backupPreferenceStore.getName(),backupPreferenceStore.getType());;    
        cserverNodeStoreProvider = new TnRegionDependentStoreProvider(cserverNodeStore.getName(),cserverNodeStore.getType());
        this.refreshAudioInventory();
        this.refreshAudioRuleInventory();
    }
    
    public static void copyVersions(ResourceBarDao srcDao, ResourceBarDao destDao, String region)
    {
        destDao.setAirportVersion(srcDao.getAirportVersion());
        destDao.setAudioInventory(srcDao.getAudioInventory());
        destDao.setAudioRuleTimestamp(srcDao.getAudioRuleTimestamp());
        destDao.setAudioTimestamp(srcDao.getAudioTimestamp());
        destDao.setBrandNameVersion(srcDao.getBrandNameVersion(region), region);
        destDao.setCategoryVersion(srcDao.getCategoryVersion(region), region);
        destDao.setHotPoiVersion(srcDao.getHotPoiVersion(region), region);
        destDao.setMapDataUpgradeInfoVersion(srcDao.getMapDataUpgradeInfoVersion());
        destDao.setPreferenceSettingVersion(srcDao.getPreferenceSettingVersion(region), region);
        destDao.setResourceFormatVersion(srcDao.getResourceFormatVersion());
    }
    
    public int[] getStaticAudioIds()
    {
        try
        {
            int[] audioIds = new int[this.persistableAudioStore.size()];
            int index = 0;
            Enumeration e = this.persistableAudioStore.keys();
            while (e.hasMoreElements())
            {
                audioIds[index++] = Integer.parseInt((String)e.nextElement());
            }
            return audioIds;
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return null;
    }
    
    public void putStaticAudio(int audioId, byte[] data)
    {
        this.persistableAudioStore.put(audioId, data);
    }
    
    public void removeStaticAudio(int audioId)
    {
        this.persistableAudioStore.remove(audioId);
    }

    public synchronized void clearStaticAudio()
    {
        this.persistableAudioStore.clear();
    }
    
    public synchronized void clearDynamicAudio()
    {
        if (this.dynamicAudioCache != null)
            this.dynamicAudioCache.clear();
    }
    
    public synchronized void clearStaticAudioRule()
    {
        this.audioRuleStore.clear();
    }
    
    public byte[] getAudio(int audioId)
    {
        Integer key = PrimitiveTypeCache.valueOf(audioId);
        if (audioId > MAX_STATIC_AUDIO_ID)
        {
            return (byte[]) this.dynamicAudioCache.get(key);
        } else
        {
            return this.persistableAudioStore.get(audioId);
        }
    }
    
    /**
     * Put dynamic audio to memory cache
     * 
     * @param audioId
     * @param data
     * @param audioCacheType
     */
    public void putDynamicAudio(int audioId, byte[] data, int version)
    {
        putDynamicAudio(audioId, data);
    }

    public void putDynamicAudio(int audioId, byte[] data)
    {
        try
        {
            this.dynamicAudioCache.put(PrimitiveTypeCache.valueOf(audioId), data);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }
    
    public void putAudioRule(int ruleId, byte[] rule)
    {
        this.audioRuleStore.put(ruleId, rule);
    }
    
    public void removeAudioRule(int ruleId)
    {
        this.audioRuleStore.remove(ruleId);
    }
    
    public boolean isAudioRuleEmpty()
    {
        if (audioRuleStore == null || audioRuleStore.size() == 0)
        {
            return true;
        }
        
        return false;
    }

    public RuleNode getAudioRule(int ruleId)
    {
        Integer key = PrimitiveTypeCache.valueOf(ruleId);
        if (audioRuleCache.containsKey(key))
        {
            return (RuleNode) audioRuleCache.get(key);
        }
        
        if (this.audioRuleStore.contains(ruleId))
        {
            byte[] ruleData = this.audioRuleStore.get(ruleId);
            
            ByteArrayInputStream is = null;
            try
            {
                is = new ByteArrayInputStream(ruleData);
                RuleNode rule = AudioDataFactory.getInstance().createRuleNode(is);
                audioRuleCache.put(key, rule);
                return rule;
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            finally
            {
                try
                {
                    if(is != null)
                    {
                        is.close();
                    }
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
        }
        return null;
    }
    
    public void setAudioTimestamp(String timestamp)
    {
        resourceBarVersionStore.put(AUDIO_TIMESTAMP_INDEX, timestamp.getBytes());
    }

    public String getAudioTimestamp()
    {
        String defaultAudioTimestamp = "0";
        
        byte[] data = resourceBarVersionStore.get(AUDIO_TIMESTAMP_INDEX);
        if (data == null || data.length == 0)
        {
            byte[] defaultTimestampData = resourceBarVersionStore.get(AUDIO_DEFAULT_TIMESTAMP_INDEX);
            if (defaultTimestampData != null && defaultTimestampData.length > 0)
            {
                defaultAudioTimestamp = new String(defaultTimestampData);
            }
        }
        
        return data == null || data.length == 0 ? defaultAudioTimestamp : new String(data);
    }

    public String getAudioInventory()
    {
        return audioInventory == null ? DEFAULT_INVENTORY_VALUE : audioInventory;
    }

    public void setAudioInventory(String audioInventory)
    {
        this.audioInventory = audioInventory;
    }
    
    public void setAudioRuleTimestamp(String timestamp)
    {
        resourceBarVersionStore.put(AUDIO_RULE_TIMESTAMP_INDEX, timestamp.getBytes());
        Logger.log(Logger.INFO, this.getClass().getName(), "setAudioRuleTimestamp : " + timestamp);
    }

    public String getAudioRuleTimestamp()
    {
        byte[] data = resourceBarVersionStore.get(AUDIO_RULE_TIMESTAMP_INDEX);
        return data == null || data.length == 0 ? "0" : new String(data);
    }

    public String getAudioRuleInventory()
    {
        return audioRuleInventory == null ? DEFAULT_INVENTORY_VALUE : audioRuleInventory;
    }

    public synchronized void refreshAudioInventory()
    {
        try
        {
            this.audioInventory = InventoryUtil.getInventory(this.persistableAudioStore);
        }
        catch (Throwable e)
        {
            this.audioInventory = "00";//set default value as 00 to differ with other case.
            Logger.log(Logger.EXCEPTION, this.getClass().getName(), "refreshAudioInventory : Error. " + e.getMessage());
        }
        if (audioInventory.trim().length() > 0 && !audioInventory.equals(DEFAULT_INVENTORY_VALUE))
        {
            this.setInventoryLoadSuccessed(true);
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "refreshAudioInventory : " + audioInventory);
    }
    
    public void setInventoryLoadSuccessed(boolean isSuccessed)
    {
        isInventoryLoadSuccessed = isSuccessed;
    }

    public boolean getInventoryLoadSuccessed()
    {
        return isInventoryLoadSuccessed;
    }

    public synchronized void refreshAudioRuleInventory()
    {
        try
        {
            this.audioRuleInventory = InventoryUtil.getInventory(this.audioRuleStore);
        }
        catch (Throwable e)
        {
            this.audioRuleInventory = "00";//set default value as 00 to differ with other case. 
            Logger.log(Logger.EXCEPTION, this.getClass().getName(), "refreshAudioRuleInventory : Error. " + e.getMessage());
        }
        Logger.log(Logger.INFO, this.getClass().getName(), "refreshAudioRuleInventory : " + this.audioRuleInventory);
    }
    
    public void setDefaultAudioTimestamp(String defaultAudioTimestamp)
    {
        resourceBarVersionStore.put(AUDIO_DEFAULT_TIMESTAMP_INDEX,
            defaultAudioTimestamp.getBytes());
    }
    
    public void setAirportVersion(String version)
    {
        if (version != null)
        {
            cserverNodeStore.put(AIRPORT_VERSION, version.getBytes());
        }
    }
    
    public String getAirportVersion()
    {
        byte[] versionData = cserverNodeStore.get(AIRPORT_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
    
    public void setResourceFormatVersion(String version)
    {
        if (version != null)
        {
            cserverNodeStore.put(RESOURCE_FORMAT_VERSION, version.getBytes());
        }
    }
    
    public String getResourceFormatVersion()
    {
        byte[] versionData = cserverNodeStore.get(RESOURCE_FORMAT_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
    
    public void setLocalEventsVersion(String version, String region)
    {
        if (version != null)
        {
            cserverNodeStoreProvider.getStore(region).put(LOCAL_EVENTS_VERSION, version.getBytes());
        }
    }
    
    public void setLocalEvents(PoiCategory node, String region)
    {
        if (node != null)
        {
            cserverNodeStoreProvider.getStore(region).put(LOCAL_EVENTS, SerializableManager.getInstance().getPoiSerializable().toBytes(node));
        }
    }
    
    public String getLocalEventsVersion(String region)
    {
        byte[] versionData = cserverNodeStoreProvider.getStore(region).get(LOCAL_EVENTS_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
    
    public PoiCategory getLocalEventsNode(String region)
    {
        byte[] localEventsData = cserverNodeStoreProvider.getStore(region).get(LOCAL_EVENTS);
        if (localEventsData != null)
        {
            PoiCategory localEventsNode = SerializableManager.getInstance().getPoiSerializable().createPoiCategory(localEventsData);
            return localEventsNode;
        }
        return null;
    }
    
    public void setHotPoiVersion(String version, String region)
    {
        if (version != null)
        {
            cserverNodeStoreProvider.getStore(region).put(HOT_POI_VERSION, version.getBytes());
        }
    }
    
    public void setHotPoiNode(PoiCategory node, String region)
    {
        if (node != null)
        {
            cserverNodeStoreProvider.getStore(region).put(HOT_POI, SerializableManager.getInstance().getPoiSerializable().toBytes(node));
        }
    }
    
    public String getHotPoiVersion(String region)
    {
        byte[] versionData = cserverNodeStoreProvider.getStore(region).get(HOT_POI_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
    
    public PoiCategory getHotPoiNode(String region)
    {
        byte[] hotPoiData = cserverNodeStoreProvider.getStore(region).get(HOT_POI);
        if (hotPoiData != null)
        {
            PoiCategory hotPoiNode = SerializableManager.getInstance().getPoiSerializable().createPoiCategory(hotPoiData);
            return hotPoiNode;
        }
        return null;
    }
    
    public void setBrandNameVersion(String version, String region)
    {
        if (version != null)
        {
            cserverNodeStoreProvider.getStore(region).put(BRAND_NAME_VERSION, version.getBytes());
        }
    }
    
    public void setBrandNameNode(StringList node, String region)
    {
        if (node != null)
        {
            cserverNodeStoreProvider.getStore(region).put(BRAND_NAME, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(node));
        }
    }
    
    public String getBrandNameVersion(String region)
    {
        byte[] versionData = cserverNodeStoreProvider.getStore(region).get(BRAND_NAME_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
    
    public StringList getBrandNameNode(String region)
    {
        byte[] brandNameData = cserverNodeStoreProvider.getStore(region).get(BRAND_NAME);
        if (brandNameData != null)
        {
            StringList airportNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(brandNameData);
            return airportNode;
        }
        return null;
    }
    
    public void setAirportNode(Vector list)
    {
        if (list != null)
        {
            BytesList node = new BytesList();
            for (int i=0; i<list.size(); i++)
            {
                Stop stop = (Stop)list.elementAt(i);
                node.add(SerializableManager.getInstance().getAddressSerializable().toBytes(stop));
            }
            cserverNodeStore.put(AIRPORT_NODE, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(node));
//            store();
        }

    }
    
    public Vector getAirportNode()
    {
        byte[] airportData = cserverNodeStore.get(AIRPORT_NODE);
        if (airportData != null)
        {
            BytesList airportNode = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(airportData);
            if (airportNode == null)
                return null;
            
            Vector list = new Vector();
            for (int i=0; i<airportNode.size(); i++)
            {
                byte[] b = airportNode.elementAt(i);
                Stop stop = SerializableManager.getInstance().getAddressSerializable().createStop(b);
                list.addElement(stop);
            }
            return list;
        }
        return null;
    }
    
    public void setCategoryNode(PoiCategory node, String region)
    {
        if (node != null)
        {
            convertUnicodeNode(node);
            cserverNodeStoreProvider.getStore(region).put(CATEGORY_TREE, SerializableManager.getInstance().getPoiSerializable().toBytes(node));
        }
    }
    
    private void convertUnicodeNode(PoiCategory node)
    {
        if (node.getName() != null)
        {
            String msg = node.getName();
            if (msg != null)
            {
                node.setName(loadConvert(msg));
            }
        }
        
        for (int i=0; i<node.getChildrenSize(); i++)
        {
            PoiCategory childNode = node.getChildAt(i);
            if (childNode != null)
            {
                convertUnicodeNode(childNode);
            }
        }
    }
    
    public PoiCategory getCategoryNode(String region)
    {
        byte[] categoryData = cserverNodeStoreProvider.getStore(region).get(CATEGORY_TREE);
        if (categoryData != null)
        {
            PoiCategory categoryNode = SerializableManager.getInstance().getPoiSerializable().createPoiCategory(categoryData);
            return categoryNode;
        }

        return null;
    }
    
    public void setCategoryVersion(String version, String region)
    {
        if (version != null)
        {
            cserverNodeStoreProvider.getStore(region).put(CATEGORY_VERSION, version.getBytes());
        }
    }
    
    public String getCategoryVersion(String region)
    {
        byte[] versionData = cserverNodeStoreProvider.getStore(region).get(CATEGORY_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
    
    public Vector getMapDataUpgradeInfo()
    {
        byte[] mapUpgradeInfoData = cserverNodeStore.get(MAP_DATA_UPGRADE_INFO);
        if (mapUpgradeInfoData != null)
        {
            BytesList mapDataUpgradeNode = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(mapUpgradeInfoData);
            if (mapDataUpgradeNode == null)
                return null;
            
            Vector list = new Vector();
            for (int i=0; i<mapDataUpgradeNode.size(); i++)
            {
                byte[] b = mapDataUpgradeNode.elementAt(i);
                MapDataUpgradeInfo mapUpgradeInfo = SerializableManager.getInstance().getExtraInfoSerializable().createMapDataUpgradeInfo(b);
                list.addElement(mapUpgradeInfo);
            }
            return list;
        }
        return null;
    }
    
    public void setMapDataUpgradeInfo(Vector upgradeInfos)
    {
        if (upgradeInfos != null)
        {
            BytesList node = new BytesList();
            for (int i=0; i<upgradeInfos.size(); i++)
            {
                MapDataUpgradeInfo upgradeInfo = (MapDataUpgradeInfo)upgradeInfos.elementAt(i);
                node.add(SerializableManager.getInstance().getExtraInfoSerializable().toBytes(upgradeInfo));
            }
            cserverNodeStore.put(MAP_DATA_UPGRADE_INFO, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(node));
        }
    }
    
    public void setMapDataUpgradeInfoVersion(String version)
    {
        if (version != null)
        {
            cserverNodeStore.put(MAP_DATA_UPGRADE_INFO_VERSION, version.getBytes());
        }
    }
    
    public String getMapDataUpgradeInfoVersion()
    {
        byte[] versionData = cserverNodeStore.get(MAP_DATA_UPGRADE_INFO_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
        
    public String getPreferenceChecksum(String region)
    {
        byte[] data = cserverNodeStoreProvider.getStore(region).get(PREFERENCE_SETTING_SUM);
        String sumValue = new String(data);
        return sumValue;
    }

    public void setPreferenceChecksum(String value, String region)
    {
        byte[] data = value.getBytes();
        if (data != null)
        {
            cserverNodeStoreProvider.getStore(region).put(PREFERENCE_SETTING_SUM, data);
        }
    }
    
    public byte[] getPreferenceSetting(String region)
    {
        byte[] data = cserverNodeStoreProvider.getStore(region).get(PREFERENCE_SETTING_DATA);

        return data;
    }
    
    public void setPreferenceSetting(byte[] data, String region)
    {
        if (data != null)
        {
            cserverNodeStoreProvider.getStore(region).put(PREFERENCE_SETTING_DATA, data);
        }
    }
    
    public byte[] getBackupPreferenceSetting(String region)
    {
        byte[] data = backupPreferenceStoreProvider.getStore(region).get(PREFERENCE_SETTING_BACKUP_DATA);

        return data;
    }
    
    public void setBackupPreferenceSetting(byte[] data, String region)
    {
        if (data != null)
        {
            backupPreferenceStoreProvider.getStore(region).put(PREFERENCE_SETTING_BACKUP_DATA, data);
        }
    }
    
    public void setPreferenceSettingVersion(String version, String region)
    {
        if (version != null)
        {
            cserverNodeStoreProvider.getStore(region).put(PREFERENCE_SETTING_VERSION, version.getBytes());
        }
    }
    
    public String getPreferenceSettingVersion(String region)
    {
        byte[] versionData = cserverNodeStoreProvider.getStore(region).get(PREFERENCE_SETTING_VERSION);
        if (versionData != null)
        {
            return new String(versionData);
        }
        return "";
    }
    
    public void setCenterPoint(String centerPoint, String region)
    {
        if (centerPoint != null)
        {
            cserverNodeStoreProvider.getStore(region).put(REGION_CENTER_POINT,
                centerPoint.getBytes());
        }
    }
    
    public TnLocation getRegionAnchor(String region)
    {
        TnLocation location = null;
        String  target = getCenterPoint(region);
        if (target != null)
        {
            String value = (String) target;
            int index = value.indexOf(",");
            if (index > 0)
            {
                int lat = (int) (Location.convert(value.substring(0, index).trim()) * 100000);
                int lon = (int) (Location.convert(value.substring(index + 1).trim()) * 100000);
                location = new TnLocation("");
                location.setLatitude(lat);
                location.setLongitude(lon);
            }
        }
        
        if (location == null)
        {
            location = LocationProvider.getInstance().getDefaultLocation();
        }
        
        return location;
    }

    private String getCenterPoint(String region)
    {
        byte[] centerPoint = cserverNodeStoreProvider.getStore(region).get(
            REGION_CENTER_POINT);
        if (centerPoint != null)
        {
            return new String(centerPoint);
        }
        return "";
    }
    
    public void setIsResourceSyncFinish(boolean isFinish)
    {
        if(resourceBarVersionStore != null)
        {
            String info = isFinish ? "1" : "0";
            resourceBarVersionStore.put(KEY_B_IS_SYNC_FINISH, info.getBytes());;
        }
        this.isResourceSyncFinish = isFinish;
    }
    
    public boolean isResourceSyncFinish()
    {
        if(resourceBarVersionStore != null)
        {
            byte[] data = resourceBarVersionStore.get(KEY_B_IS_SYNC_FINISH);
            if(data != null)
            {
                String info = new String(data);
                if("1".equals(info))
                {
                    this.isResourceSyncFinish = true;
                }
            }
        }
        
        return this.isResourceSyncFinish;
    }
    
    public synchronized void clear()
    {
        if( navAudiosCache != null )
            this.navAudiosCache.clear();
        if( trafficAudiosCache != null )
            this.trafficAudiosCache.clear();
        if( audioRuleCache != null )
            this.audioRuleCache.clear();
        if( dynamicAudioCache != null )
            this.dynamicAudioCache.clear();
        this.persistableAudioStore.clear();
        this.audioRuleStore.clear();
        this.resourceBarVersionStore.clear();
        cserverNodeStore.clear();
        cserverNodeStoreProvider.clear();
        this.backupPreferenceStoreProvider.clear();
        audioInventory = null;
        audioRuleInventory = null;
    }

    public synchronized void store()
    {
        persistableAudioStore.save();
        saveToFile(persistableAudioStore, NavsdkFileUtil.getNavsdkAudioDirectory(), AbstractDaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserPrefers().audioFormat);
        audioRuleStore.save();
        saveToFile(audioRuleStore, NavsdkFileUtil.getNavsdkAudioRuleDirectory(), "rbn");
        resourceBarVersionStore.save();
        cserverNodeStore.save();
        cserverNodeStoreProvider.save();
        backupPreferenceStoreProvider.save();
    }


    private void saveToFile(TnStore store, String path, String suffix)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), " saveToFile: ");
        NavsdkFileUtil.deleteDirectory(path);
        Enumeration keys = store.keys();
        if (keys != null)
        {
            while (keys.hasMoreElements())
            {
                String id = (String) keys.nextElement();
                Object obj = store.get(id);
                if(obj != null)
                {
                    byte[] buf = (byte[]) obj;
                    NavsdkFileUtil.createFile(path, id + "." + suffix, buf);
                }
            }
        }
    }
    
    public byte[] getData(AudioData clipData) throws Throwable
    {
        if (clipData == null) return null;
        
        if (clipData.getResourceUri() != null)
        {
            byte[] buff = ioManager.openFileBytesFromAppBundle(clipData.getResourceUri());
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- AudioConcatenator::getClipData - clipData.getResourceLocation(): "
                    + clipData.getResourceUri() + " , buff length : " + (buff == null ? 0 : buff.length));
                
                if(buff == null || buff.length == 0)
                {
                    Logger.log(Logger.ERROR, this.getClass().getName(), " -- Audio Profile -- AudioConcatenator::getClipData - clipData.getResourceLocation(): "
                        + clipData.getResourceUri() + "   XXX No Data XXX");
                }
            }
            
            return buff;
        }
        else if (clipData.getResourceId() != -1)
        {
            if(clipData.getResourceId() > MAX_STATIC_AUDIO_ID)
            {
                Object data = null;
                
                if(AUDIO_WHAT_NAVIGATION.equals(clipData.getCategory()))
                {
                    data = this.navAudiosCache.get(PrimitiveTypeCache.valueOf(clipData.getResourceId()));
                    
                    if(Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Provider, get Nav Audio id: "
                            + clipData.getResourceId() + " , data length : " + ( data == null ? 0 : ((byte[])data).length));
                    }
                }
                else if(AUDIO_WHAT_TRAFFIC.equals(clipData.getCategory()))
                {
                    data = this.trafficAudiosCache.get(PrimitiveTypeCache.valueOf(clipData.getResourceId()));
                    
                    if(Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Provider, get Tra Audio id: "
                            + clipData.getResourceId() + " , data length : " + ( data == null ? 0 : ((byte[])data).length));
                    }
                }
                else
                {
                    data = dynamicAudioCache.get(PrimitiveTypeCache.valueOf(clipData.getResourceId()));
                    
                    if(Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Provider, get Dyn Audio id: "
                            + clipData.getResourceId() + " , data length : " + ( data == null ? 0 : ((byte[])data).length));
                    }
                }
                
                if(data == null || ((byte[])data).length == 0)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Provider, get XXX Audio id: "
                        + clipData.getResourceId() + "   XXX No Data XXX");
                }
                
                if (data instanceof byte[])
                {
                    return (byte[]) data;
                }
            }
            
            byte[] data = persistableAudioStore.get(clipData.getResourceId());
            if(data == null || ((byte[])data).length == 0)
            {
                if(Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Provider, get Sta Audio from cache, id: "
                        + clipData.getResourceId() + "   XXX No Data XXX");
                }
                
                data = GuideToneManager.getInstance().getStaticAudioData(clipData.getResourceId());
                
                if(Logger.DEBUG)
                {
                    if(data == null || ((byte[])data).length == 0)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Provider, get Sta Audio from file, id: "
                            + clipData.getResourceId() + "   XXX No Data XXX");
                    }
                }
            }
            
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), " -- Audio Profile -- Provider, get Sta Audio id: "
                    + clipData.getResourceId() + " , data length : " + ( data == null ? 0 : ((byte[])data).length));
            }
            return data;
        }
        else
        {
            return null;
        }
    }
    
    private String loadConvert(String theString) 
    {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;)
        {
            aChar = theString.charAt(x++);
            if (aChar == '\\')
            {
                aChar = theString.charAt(x++);
                if (aChar == 'u')
                {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++)
                    {
                        aChar = theString.charAt(x++);
                        switch (aChar)
                        {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                }
                else
                {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            }
            else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
