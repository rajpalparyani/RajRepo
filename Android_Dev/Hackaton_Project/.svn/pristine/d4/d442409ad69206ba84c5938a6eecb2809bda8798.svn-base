/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * StartupDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class StartupDao extends AbstractDao
{
    private final static int UPGRADE_INFO_INDEX = 600001;
    private final static int NEWAPP_INFO_INDEX = 600006;
    private final static int CURRENT_APP_VERSION_INDEX = 600007;
    private final static int DATA_PROVIDER_INFO_INDEX = 600008;
    private final static int BUILD_NUMBER_INDEX = 600009;
    private final static int INGNORE_VERSION = 600010;
    private final static int MAP_COPYRIGHT_INFO_INDEX = 600011;
    
    public static final String UPGRADE_TYPE_FORCE = "FORCE";
    public static final String UPGRADE_TYPE_OPTIONAL = "OPTIONAL";
    
    public static final String MAP_DATASET_TA = "Tele Atlas";
    public static final String MAP_DATASET_NT = "Navteq";
    
    private TnStore startupStore;
    
    private String upgradeType;
    private String upgradeFileName;
    private String upgradeAppVersion;
    private String upgradeUrl;
    private String upgradeSummary;
    private String upgradeBuildNumber;
    private String upgradeMinOSVersion;
    private String upgradeMaxOSVersion;

    private String newAppVersion;
    private String currentAppVersion;
    private String ingnoreAppVersion;
    private String mapDataset;
    private String buildNumber;
    private String mapCopyright;
    
    private static class SingletonHolder 
    {
        public static final StartupDao INSTANCE = new StartupDao();
    }

    public static StartupDao getInstance() 
    {
        return SingletonHolder.INSTANCE;
    }
    
    /*
     * this method is for unit test only!
     */
    void setStore(TnStore startupStore)
    {
    	this.startupStore = startupStore;
    }
    
    private StartupDao()
    {
        startupStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.DATABASE, "startup_data");
        startupStore.load();
        init();
    }
    
    public String getUpgradeType()
    {
        return this.upgradeType == null ? "" : this.upgradeType;
    }
    
    public String getUpgradeFileName()
    {
        return this.upgradeFileName == null ? "" : this.upgradeFileName;
    }
    
    public String getUpgradeAppVersion()
    {
        return this.upgradeAppVersion == null ? "" : this.upgradeAppVersion;
    }
    
    public String getUpgradeUrl()
    {
        return this.upgradeUrl == null ? "" : this.upgradeUrl;
    }
    
    public String getUpgradeSummary()
    {
        return this.upgradeSummary == null ? "" : this.upgradeSummary;
    }
    
    public String getUpgradeBuildNumber()
    {
        return this.upgradeBuildNumber == null ? "" : this.upgradeBuildNumber;
    }
    
    public String getNewAppVersion()
    {
        return this.newAppVersion == null ? "" : this.newAppVersion;
    }
    
    public String getCurrentAppVersion()
    {
        return currentAppVersion;
    }
    
    public String getMapDataset()
    {
        if(mapDataset == null)
        {
            return "";
        }
        return mapDataset;
    }
    
    public String getMapCopyright()
    {
        if(mapCopyright == null)
        {
            return "";
        }
        return mapCopyright;
    }

    public String getBuildNumber()
    {
        return buildNumber;
    }
    
    public String getMinOSVersion()
    {
        return this.upgradeMinOSVersion == null ? "" : this.upgradeMinOSVersion;
    }
    
    public String getMaxOSVersion()
    {
        return this.upgradeMaxOSVersion == null ? "" : this.upgradeMaxOSVersion;
    }     
    
    public void setUpgradeInfo(StringList upgradeInfoNode)
    {
        if(upgradeInfoNode == null)
            return;
        
        this.upgradeType = upgradeInfoNode.elementAt(0);
        this.upgradeFileName = upgradeInfoNode.elementAt(1);
        this.upgradeAppVersion = upgradeInfoNode.elementAt(2);
        this.upgradeUrl = upgradeInfoNode.elementAt(3);
        this.upgradeSummary = upgradeInfoNode.elementAt(4);
        this.upgradeBuildNumber = upgradeInfoNode.elementAt(5);
        if(upgradeInfoNode.size() > 6)
        {
        	this.upgradeMinOSVersion = upgradeInfoNode.elementAt(6);
        	this.upgradeMaxOSVersion = upgradeInfoNode.elementAt(7);
        }        
        this.startupStore.put(UPGRADE_INFO_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(upgradeInfoNode));
    }
    
    public void setNewAppInfo(StringList newAppInfo)
    {
        if(newAppInfo == null)
            return;
        
        this.newAppVersion = newAppInfo.elementAt(0);
        
        this.startupStore.put(NEWAPP_INFO_INDEX, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(newAppInfo));
    }
    
    public void setMapDataset(String dataset)
    {
        if (dataset == null)
            return;
        
        this.mapDataset = dataset;
        
        this.startupStore.put(DATA_PROVIDER_INFO_INDEX, this.mapDataset.getBytes());
    }
    
    public void setMapCopyright(String mapCopyright)
    {
        if (mapCopyright == null)
            return;
        
        this.mapCopyright = mapCopyright;
        
        this.startupStore.put(MAP_COPYRIGHT_INFO_INDEX, this.mapCopyright.getBytes());
    }
    
    public void setBuildNumber(String buildnumber)
    {
        if(buildnumber == null)
            return;
        
        if(buildnumber.equals(this.getBuildNumber()))
        {
            return;
        }
        
        this.buildNumber = buildnumber;
        
        this.startupStore.put(BUILD_NUMBER_INDEX, buildnumber.getBytes());
    }
    
    
    public void setIngnoreAppVersion(String clientVersion)
    {
        if (clientVersion == null)
            return;
        
        if(clientVersion.equals(getIngnoreVersion()))
        {
            return;
        }
        
        this.ingnoreAppVersion = clientVersion;
        
        this.startupStore.put(INGNORE_VERSION, clientVersion.getBytes());
    }
    
    public String getIngnoreVersion()
    {
        return ingnoreAppVersion;
    }
    
    public void setCurrentAppVersion(String clientVersion)
    {
        if (clientVersion == null)
            return;
        
        if(clientVersion.equals(getCurrentAppVersion()))
        {
            return;
        }
        
        this.currentAppVersion = clientVersion;
        
        this.startupStore.put(CURRENT_APP_VERSION_INDEX, clientVersion.getBytes());
    }
    
    private void init()
    {
        byte[] upgradeInfoData = this.startupStore.get(UPGRADE_INFO_INDEX);
        if(upgradeInfoData != null)
        {
            StringList upgradeInfoNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(upgradeInfoData);
            this.setUpgradeInfo(upgradeInfoNode);
        }
        
        byte[] newAppInfoData = this.startupStore.get(NEWAPP_INFO_INDEX);
        if(newAppInfoData != null)
        {
            StringList newAppInfoNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(newAppInfoData);
            this.setNewAppInfo(newAppInfoNode);
        }
        
        byte[] appVersionData = this.startupStore.get(CURRENT_APP_VERSION_INDEX);
        if (appVersionData != null)
        {
            this.currentAppVersion = new String(appVersionData);
        }
        
        byte[] buildNumberData = this.startupStore.get(BUILD_NUMBER_INDEX);
        if (buildNumberData != null)
        {
            this.buildNumber = new String(buildNumberData);
        }
        
        byte[] ingnoreVersionData = this.startupStore.get(INGNORE_VERSION);
        if (ingnoreVersionData != null)
        {
            this.ingnoreAppVersion = new String(ingnoreVersionData);
        }
        
        byte[] dataProviderData= this.startupStore.get(DATA_PROVIDER_INFO_INDEX);
        if(dataProviderData != null)
        {
           this.mapDataset = new String(dataProviderData);
        }
        
        byte[] mapCopyrightData= this.startupStore.get(MAP_COPYRIGHT_INFO_INDEX);
        if(mapCopyrightData != null)
        {
           this.mapCopyright = new String(mapCopyrightData);
        }
    }
    
    public synchronized void store()
    {
    	this.startupStore.save();
    }
    
    public synchronized void clear()
    {
    	reset();
        this.startupStore.clear();
    }
    
    private void reset()
    {
    	upgradeType = "";
        upgradeFileName = "";
        upgradeAppVersion = "";
        upgradeUrl = "";
        upgradeSummary = "";
        upgradeBuildNumber = "";
        upgradeMinOSVersion = "";
        upgradeMaxOSVersion = "";

        newAppVersion = "";
        currentAppVersion = "";
        ingnoreAppVersion = "";
        mapDataset = "";
        buildNumber = "";
        mapCopyright = "";
    }
}
