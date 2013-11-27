/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SecretKeyModel.java
 *
 */
package com.telenav.module.entry.secretkey;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.telenav.app.CommManager;
import com.telenav.app.HostList;
import com.telenav.app.android.AndroidPersistentContext;
//import com.telenav.app.android.c2dm.C2DMessaging;
import com.telenav.data.dao.misc.CommHostDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.ServiceLocator;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.ISyncCombinationResProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.entry.SplashScreenJob;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.navsdk.NavsdkFileUtil;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author jshchen 
 *@date 2010-8-19
 */
class SecretKeyModel extends AbstractCommonNetworkModel implements ISecretKeyConstants
{
    final String[] regionAnchors = {"GB-London,51.511307,-0.127716", "FR-Paris,48.859068,2.351074", "IT-Rome,41.901255,12.494202", "DE-Berlin, 52.525413,13.406067", "SE-Stockholm,59.331789,18.064957", "PR-SanJuan,18.46463,-66.096497", "VI-CharlotteAmalie,18.342207,-64.93084"};
    private static final String LAT_LON_SEPERATOR = ":";
    private DumpFileManager manager = DumpFileManager.getInstance();
    final String[] preloadFiles = {"TN70_scout_us_audio_rule_preload.bin",
            "TN70_scout_us_backup_preference_static_data_US.bin",
            "TN70_scout_us_bar_version.bin",
            "TN70_scout_us_cserver_node.bin",
            "TN70_scout_us_cserver_node_US.bin",
            "TN70_scout_us_dsr_dsp.bin",
            "TN70_scout_us_carrier_mapping.bin"
            };
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                init();
                break;
            }
            case ACTION_URL_GROUP:
            {
                initUrlGroup();
                break;
            }
            case ACTION_GPS:
            {
                initGps();
                break;
            }
            case ACTION_DATASOURCE_MODE:
            {
                initDataSourceMode();
                break;
            }
            case ACTION_NETWORK:
            {
                initNetwork();
                break;
            }
            case ACTION_CLEAR_RMS:
            {
                doClearRMS();
                break;
            }
            case ACTION_CLEAR_STATIC_AUDIO:
            {
                DaoManager.getInstance().getResourceBarDao().clearStaticAudio();
                DaoManager.getInstance().getResourceBarDao().store();
                break;
            }
            case ACTION_CHANGE_CELL:
            {
                changeCell();
                break;
            }
            case ACTION_SET_PTN:
            {
                setPtn();
                break;
            }
            case ACTION_SET_MVIEWER_IP:
            {
                setMViewerIp();
                break;
            }
            case ACTION_SET_ALONGROUTE_SPEED:
            {
                String speed = this.getString(KEY_S_ALONGROUTE_SPEED);
                this.put(KEY_S_SET_DONE_TITLE, "Set Along Route Speed Done");
                this.put(KEY_S_SET_DONE_INFO_MSG, "Set Along Route Speed : " + speed);
                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_ALONG_ROUTE_SPEED, speed);
                ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                break;
            }
            case ACTION_SET_MAP_DATASET:
            {
                setMapDataset();
                break;
            }
            case ACTION_SET_LOGGER:
            {
                setLoggerSetting();
                break;
            }
            case ACTION_DWF_SETTING:
            {
                setDwfSetting();
                break;
            }
            case ACTION_SET_RESOURCE_PATH_ENABLE:
            {
                setResourcePathSetting();
                break;
            }
            case ACTION_SET_BILLBOARD:
            {
                setBillboardHost();
                break;
            }
            case ACTION_CHANGE_HOST:
            {
                checkUpgradeHost();
                break;
            }
            case ACTION_SHOW_DIRECTORY:
            {
                String selectFilePath = this.getString(KEY_S_SELECTED_FILE_PATH);
                String appPath = manager.getAppPath();
                String targetPath = selectFilePath == null || "".equalsIgnoreCase(selectFilePath) ? appPath : selectFilePath;
                initDirectory(targetPath);
                break;
            }
            case ACTION_SHOW_FILE_CONTENT:
            {
                String selectFilePath = this.getString(KEY_S_SELECTED_FILE_PATH);
                File targetFile = new File(selectFilePath);
                if (targetFile.isFile())
                {
                    this.put(KEY_S_SELECTED_FILE_CONTENT, manager.getFileContent(selectFilePath));
                }
                break;
            }
            case ACTION_DUMP_LOCAL_FILE:
            {
                File sourceDirectory = new File(manager.getAppPath());
                String dumpFilePath = manager.getSdcardPath(true);
                File targetDirectory = new File(dumpFilePath);
                manager.copyDirectory(sourceDirectory, targetDirectory);
                File dumpFile = new File(dumpFilePath);
                boolean isSuccess = manager.getFileSize(dumpFile) > 0 ? true : false;
                String result;
                if (isSuccess)
                {
                    result = "Dump File Success, the file path is" + dumpFilePath;
                }
                else
                {
                    result = "Dump  Fails";
                }
                this.put(KEY_S_DUMPFILE_RESULT, result);
                break;
            }
            case ACTION_PRELOAD_FILES:
            {
                boolean isConvertSuccess = ((DaoManager)AbstractDaoManager.getInstance()).convertAudioRuleDatabaseToFile();
                String result;
                Logger.log(Logger.INFO, "Preload file", "JY----------isConvertSuccess="+isConvertSuccess);
                if(!isConvertSuccess)
                {
                    result = "You must login first to prepare preload files!";
                }
                else
                {
                    TnStore carrierMappingStoreBackup = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, "carrier_mapping_backup");
                    carrierMappingStoreBackup.load();
                    DaoManager.getInstance().getSimpleConfigDao().initCarrierMapping(carrierMappingStoreBackup);
                    
                    File sourceDirectory = new File(manager.getAppPath()+"/files/");
                    String dumpFilePath = manager.getSdcardPreloadFilePath(true);
                    File targetDirectory = new File(dumpFilePath);
                    
                    Vector preloadFilesVec = new Vector();
                    for(int i=0; i<preloadFiles.length; i++)
                    {
                        preloadFilesVec.addElement(preloadFiles[i]);
                    }
                    manager.copyDirectoryWithSpecificFileTable(sourceDirectory, targetDirectory, preloadFilesVec);
                    File dumpFile = new File(dumpFilePath);
                    boolean isSuccess = manager.getFileSize(dumpFile) > 0 ? true : false;
                   
                    if (isSuccess)
                    {
                        result = "Dump Preload Files Success, the file path is" + dumpFilePath;
                    }
                    else
                    {
                        result = "Dump Preload Files Fails";
                    }
                    
                    DaoManager.getInstance().getSimpleConfigDao().restoreKeyMapping(carrierMappingStoreBackup);
                    carrierMappingStoreBackup.clear();
                }
                this.put(KEY_S_DUMPFILE_RESULT, result);
                break;
            }
            case ACTION_REGION_INIT:
            {
                String[] labelAnchors = new String[regionAnchors.length];
                Hashtable<String, TnLocation> defaultRegions = new Hashtable<String, TnLocation>();
                for (int i = 0; i < regionAnchors.length; i++)
                {
                    String[] splitStr = regionAnchors[i].split(",");
                    String lable = splitStr[0];
                    labelAnchors[i] = lable;
                    int lat = (int) (Location.convert(splitStr[1]) * 100000);
                    int lon = (int) (Location.convert(splitStr[2]) * 100000);
                    TnLocation location = new TnLocation("");
                    location.setLatitude(lat);
                    location.setLongitude(lon);
                    defaultRegions.put(lable, location);
                }
                put(KEY_ARRAY_REGION_LABLES, labelAnchors);
                put(KEY_O_DEFAULT_REGIONS, defaultRegions);
                break;
            }
            case ACTION_SET_REGION:
            {
                try
                {
                    int lat = Integer.parseInt(this.getString(KEY_S_REGION_LAT));
                    int lon = Integer.parseInt(this.getString(KEY_S_REGION_LON));
                    String latAndLon = lat + LAT_LON_SEPERATOR + lon;
                    (DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_REGION_SETTING, latAndLon);
                    (DaoManager.getInstance()).getSimpleConfigDao().store();
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_REMOVE_OUTPUT:
            {
                this.put(KEY_B_NEED_KEEP_BUTTON, true);
                String dumpFilePath = manager.getSdcardPath(false);
                File dumpFileFolder = new File(dumpFilePath);
                if (dumpFileFolder != null && dumpFileFolder.exists())
                {
                    if (dumpFileFolder.isDirectory())
                    {
                        NavsdkFileUtil.deleteDirectory(dumpFilePath);
                    }
                    else
                    {
                        NavsdkFileUtil.deleteFile(dumpFilePath);
                    }
                }
                break;
            }
            case ACTION_SET_MAPDOWNLOAD_CN_ENABLE:
            {
                setMapdownloadCNEnable();
                break;
            }
            case ACTION_SET_STUCK_MONITOR_ENABLE:
            {
                setStuckMonitorEnable();
                break;
            }
            case ACTION_SET_MAP_DISK_CACHE_DISABLE:
            {
                setMapDiskCacheDisable();
                break;
            }
            case ACTION_SWITCH_AIRPLANE_MODE:
            {
                setSwitchAirplaneModeEnable();
                break;
            }
            case ACTION_FETCH_PRELOAD_RES:
            {
                prefetchPreloadRes();
                break;
            }
        }
    }

    private void setDwfSetting()
    {
        boolean isEnableGcm = this.getBool(KEY_B_SET_LOGGER);
        boolean isDisableSms = this.getBool(KEY_B_SET_SDCARD_OUTPUT_LOGGER);

        this.put(KEY_S_SET_DONE_TITLE, "Set Send Profile Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Enable GCM ? " + (isEnableGcm) + " , isDisableSms : " + isDisableSms);

        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_GCM_ENABLE, isEnableGcm ? 1: 0);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_SMS_DISABLE, isDisableSms ? 1: 0);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
        
        SharedPreferences pre = AndroidPersistentContext.getInstance().getContext().getSharedPreferences("scout.dwf.preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("scout.dwf.preference.location", isEnableGcm);
        editor.commit();
    }

    private void prefetchPreloadRes()
    {
        doClearRMS();
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
        ISyncCombinationResProxy combinationResProxy = ServerProxyFactory.getInstance().createSyncCombinationResProxy(null,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        Vector combination = new Vector();
        
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_SERVER_DRIVEN_PARAMETERS));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_DSR_SDP_NODE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_CATEGORY_TREE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AIRPORT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_POI_BRAND_NAME));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_HOT_POI_CATEGORY));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_GOBY_EVENT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_PREFERENCE_SETTING));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_MAP_DATA_UPGRADE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_SERVICE_LOCATOR_INFO));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_RESOURCE_FORMAT));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_AUDIO_AND_RULE));
        combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_CAR_MODEL));
        
        combinationResProxy.syncCombinationRes(combination);
    }

    private void setSwitchAirplaneModeEnable()
    {
        boolean isEnable = this.getBool(KEY_B_SWITCH_AIRPLANE_MODE_ENABLE);

        this.put(KEY_S_SET_DONE_TITLE, "Enable Switch Airplane Mode Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Enable SwitchAirplaneMode : " + (isEnable ? "true" : "false"));
        
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SWITCH_AIRPLANE_MODE_ENABLE, isEnable ? 1: 0);
        DaoManager.getInstance().getSimpleConfigDao().store();
        
    }

    private void initDirectory(String path)
    {
        File file = new File(path);
        if (file.isDirectory())
        {
            String[][] directoryInfo = manager.getDirectoryInfo(path);
            this.put(KEY_ARRAY_FILE_NAMES, directoryInfo[0]);
            this.put(KEY_ARRAY_FILE_PATHS, directoryInfo[1]);
        }
        else
        {
            if (file.isFile())
            {
                long size = file.length();
                boolean isLegalFile = size > 0 && size < 10 * 1024 ? true : false;
                if (isLegalFile)
                    this.postModelEvent(CMD_SHOW_FILE_CONTENT);
            }
        }

    }

    private void setBillboardHost()
    {
        String billboardHost = this.getString(KEY_S_BILLBOARD_HOST);
        
        this.put(KEY_S_SET_DONE_TITLE, "Set Billboard Host Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Set Billboard Host to : " + billboardHost);
        
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_BILLBOARD_HOST, billboardHost);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    private void setLoggerSetting()
    {
        boolean isLoggerEnable = this.getBool(KEY_B_SET_LOGGER);
        boolean isSdCardOuptEnable = this.getBool(KEY_B_SET_SDCARD_OUTPUT_LOGGER);
        boolean isLocalMislogEnable = this.getBool(KEY_B_LOCAL_MISLOG_LOGGER);
        boolean isRuntimeStatusLogEnable = this.getBool(KEY_B_RUNTIME_STATUS_ENABLE);
        String runtimeStatusLogInterval = this.getString(KEY_S_RUNTIME_STATUS_INTERVAL);

        this.put(KEY_S_SET_DONE_TITLE, "Set Logger Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Set Logger to : " + (isLoggerEnable ? "true" : "false") + "\n" 
                + "Output Logger to Sdcard : " + (isSdCardOuptEnable ? "true": "false") + "\n" 
                + "Enable Local Mislog: " + (isLocalMislogEnable ? "true" : "false") + "\n" 
                + "Enable Runtime status Log: " + (isRuntimeStatusLogEnable ? "true" : "false"));
        
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_LOGGER_ENABLE, isLoggerEnable ? 1: 0);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_SDCARD_OUTPUT_LOGGER_ENABLE, isSdCardOuptEnable ? 1: 0);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_LOCAL_MISLOG_ENABLE, isLocalMislogEnable ? 1: 0);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_ENABLE, isRuntimeStatusLogEnable ? 1: 0);
        try
        {
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().putInteger(SimpleConfigDao.KEY_RUNTIME_STATUS_LOG_INTERVAL, Integer.parseInt(runtimeStatusLogInterval));
        }
        catch (NumberFormatException e)
        {
        }
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    private void setResourcePathSetting()
    {
        boolean isEnableSetResourcePath = this.getBool(KEY_B_SET_RESOURCE_PATH_ENABLE);

        this.put(KEY_S_SET_DONE_TITLE, "Enable Set Resource Path Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Enable Set Resource Path : " + (isEnableSetResourcePath ? "true" : "false"));
        
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SET_RESOURCE_PATH_ENABLE, isEnableSetResourcePath ? 1: 0);
        DaoManager.getInstance().getSimpleConfigDao().store();
    }
    
    private void setMapdownloadCNEnable()
    {
        boolean isCNServerEnable = this.getBool(KEY_B_SET_MAPDOWNLOAD_CN_ENABLE);

        String url = this.getString(KEY_S_SET_MAP_DOWNLOAD_CN_URL);
        
        this.put(KEY_S_SET_DONE_TITLE, "Enable MapDownload CN Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Enable MapDownload CN Server : " + (isCNServerEnable ? "true" : "false") + ", MapDownload CN URL : " + url);
        
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_URL, url);
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SET_MAP_DOWNLOAD_CN_ENABLE, isCNServerEnable ? 1: 0);
        DaoManager.getInstance().getSimpleConfigDao().store();
    }
    
    private void setStuckMonitorEnable()
    {
        boolean isStuckMonitorEnable = this.getBool(KEY_B_SET_STUCK_MONITOR_ENABLE);

        this.put(KEY_S_SET_DONE_TITLE, "Enable Stuck Monitor Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Enable Stuck Monitor : " + (isStuckMonitorEnable ? "true" : "false"));
        
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SET_STUCK_MONITOR_ENABLE, isStuckMonitorEnable ? 1: 0);
        DaoManager.getInstance().getSimpleConfigDao().store();
    }
    
    private void setMapDiskCacheDisable()
    {
        boolean isMapDiskCacheDisable = this.getBool(KEY_B_SET_MAP_DISK_CACHE_DISABLE);

        this.put(KEY_S_SET_DONE_TITLE, "Disable Map Disk Cache Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Disable Map Disk Cache : " + (isMapDiskCacheDisable ? "true" : "false"));
        
        DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_SET_MAP_DISK_CACHE_DISABLE, isMapDiskCacheDisable ? 1: 0);
        DaoManager.getInstance().getSimpleConfigDao().store();
    }

    private void setMapDataset()
    {
        String dataset = this.getString(KEY_S_NEW_DATASET);
        
        this.put(KEY_S_SET_DONE_TITLE, "Set Map Dataset Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Set Map Dataset to : " + dataset);
        
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_SWITCHED_DATASET, dataset);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    private void setMViewerIp()
    {
        String mviewerIp = this.getString(KEY_S_MVIEWER_IP);
        
        this.put(KEY_S_SET_DONE_TITLE, "Set MViewer Ip Done");
        this.put(KEY_S_SET_DONE_INFO_MSG, "Set MViewer Ip to : " + mviewerIp);
        
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_MVIEWER_HOST_IP, mviewerIp);
        ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
    }

    private void setPtn()
    {
        String ptn = this.getString(KEY_S_PTN);
        
        if(ptn != null) 
        {
            ptn = ptn.trim();
        }
        
        if (AppConfigHelper.isGlobalPtn())
        {
            AppConfigHelper.removePtnGlobalHead(ptn);
        }
        
        this.put(KEY_S_SET_DONE_TITLE, "Set Ptn Done");
        StringBuffer msg = new StringBuffer();
        msg.append("Set Ptn to : ");
        msg.append(ptn);
        msg.append(" \n");
        msg.append(" \n");
        msg.append("<bold><red>App will ignore local ptn and send the new ptn for login</red></bold>");
        this.put(KEY_S_SET_DONE_INFO_MSG, msg.toString());
        
        DaoManager.getInstance().getBillingAccountDao().setPtn(ptn);
        DaoManager.getInstance().getBillingAccountDao().setHasHackPtn(true);
        DaoManager.getInstance().getBillingAccountDao().store();
    }

    private void init()
    {
        String[] labels = new String[]{"Change Url", "Change Gps", "Clear RMS", "Change DataSource Mode", "Change Network Type", "Change Cell Source", "Set PTN", "Set MViewer Ip", "Set Along Route Speed", "Set Logger", "Enable SetResourcePath", "Ptn & UserId", "Dump File", "Clear Static Audio", "Set Billboard Host","Set Region","Test Map Dataset Change","Test Proxy", "MapDownLoad CN", /*"Enable Switch Airplane mode", */"Fetch Preload Res","Stuck Monitor", "Kontagent", "Preload", "Map Disk Cache", "Show Satellite During Nav", "DWF send setting"};
        this.put(KEY_ARRAY_MAIN_LABELS, labels);
        
        int[] actions = new int[]{CMD_CHANGE_URL, CMD_CHANGE_GPS, CMD_CLEAR_RMS, CMD_CHANGE_DATASOURCE_MODE, CMD_CHANGE_NETWORK, CMD_CHANGE_CELL, CMD_SET_PTN, CMD_SET_MVIEWER_IP, CMD_SET_ALONGROUTE_SPEED, CMD_SET_LOGGER,CMD_SET_RESOURCE_PATH_ENABLE, CMD_PTN_USERID, CMD_SHOW_DIRECTORY, CMD_STATIC_AUDIO, CMD_SET_BILLBOARD,CMD_SHOW_REGION,CMD_SET_MAP_DATASET,CMD_TEST_PROXY,CMD_MAP_DOWNLOAD_CN, /*CMD_SWITCH_AIRPLANE_MODE, */CMD_FETCH_PRELOAD_RES, CMD_STUCK_MONITOR, CMD_KONTAGENT,CMD_PRELOAD_FILES, CMD_MAP_DISK_CACHE, CMD_SHOW_SATELLITE_DURING_NAV, CMD_DWF_SEND_SETTING};
        this.put(KEY_ARRAY_MAIN_ACTIONS, actions);
        resetFile();
    }

    private void resetFile()
    {
        this.remove(KEY_ARRAY_FILE_NAMES);
        this.remove(KEY_ARRAY_FILE_PATHS);
        this.remove(KEY_S_SELECTED_FILE_CONTENT);
        this.remove(KEY_S_SELECTED_FILE_PATH);
    }
    
    private void initUrlGroup()
    {
        this.put(KEY_ARRAY_URL_GROUP_LABELS, CommManager.getInstance().getUrlGroups());
    }
    
    private void initGps()
    {
        String[] labels = new String[]{"Bluetooth", "Internal", "AlongRoute", "Mviewer"};
        this.put(KEY_ARRAY_GPS_LABELS, labels);
    }
    
    private void initDataSourceMode()
    {
        String[] labels = new String[]{"Auto(Network determine)", "Always Onboard", "Always Offboard"};
        this.put(KEY_ARRAY_DATASOURCE_MODE_LABELS, labels);        
    }
    
    private void initNetwork()
    {
        String[] labels = new String[]{"Automatically", "MDS", "BIS", "TCP"};
//        CommManager.getInstance().parseServiceBooks();
        this.put(KEY_ARRAY_NETWORK_LABELS, labels);
    }
    
    private void changeCell()
    {
        int sourceType = this.getInt(KEY_INT_LOCATION_CELL);
        if (((DaoManager) DaoManager.getInstance()).getSimpleConfigDao() != null)
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(
                SimpleConfigDao.KEY_LOCATION_SOURCE, sourceType);
    }
    
    private void checkUpgradeHost()// check is need request hostList from server
    {
    	boolean needRequest = this.getBool(KEY_B_GET_LOCATOR);
		if (needRequest) 
		{
			doClearRMS();//if do it when receive data, next time start app, it will re-send request and cover the user's set
			this.resetServiceLocator();
			IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
			ISyncCombinationResProxy combinationResProxy = ServerProxyFactory
			    .getInstance().createSyncCombinationResProxy(null,CommManager.getInstance().getComm(), this, userProfileProvider);
			Vector combination = new Vector();
			combination.addElement(PrimitiveTypeCache.valueOf(ISyncCombinationResProxy.TYPE_SERVICE_LOCATOR_INFO));
			combinationResProxy.syncCombinationRes(combination);
		}
		else
		{
			this.postModelEvent(EVENT_MODEL_EVENT_LOCATOR_GOT);
		}
    }
    
    protected  void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
    	 if (proxy instanceof ISyncCombinationResProxy)
         {
             if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_COMBINATION_SYNC_RESOURCE))
             {
                 if (getState() == STATE_GETTING_LOCATOR)
                 {
                	 int selectedIndex = this.getInt(KEY_I_SELECTHOST_INDEX);
                	 CommHostDao commHostDao = ((DaoManager) DaoManager.getInstance()).getCommHostDao();
                	 commHostDao.setDefaultSelectedIndex(selectedIndex);
                	 commHostDao.store();
                	 this.postModelEvent(EVENT_MODEL_EVENT_LOCATOR_GOT);   	 
                 }
                 else if (getState() == STATE_GETTING_PRELOAD_RES)
                 {
                     this.postModelEvent(EVENT_MODEL_EVENT_LOCATOR_GOT);   	 
                 }
             }
         }
    }
    
    private void doClearRMS()
    {
    	DaoManager.getInstance().clearInternalRMS();//clear RMS 	
        SplashScreenJob.initMadantoryNode();
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().locale="";
        DaoManager.getInstance().getMandatoryNodeDao().store();
        DaoManager.getInstance().getStartupDao().setCurrentAppVersion(AppConfigHelper.mandatoryClientVersion);
        DaoManager.getInstance().getStartupDao().setBuildNumber(AppConfigHelper.buildNumber);
        DaoManager.getInstance().getStartupDao().store();
        
        //clear storage used by C2DM
//        C2DMessaging.clearFlags(AndroidPersistentContext.getInstance().getContext());
    }
    
    private void resetServiceLocator()
    {
    	((ServiceLocator)CommManager.getInstance().getComm().getHostProvider()).clear(); //clear the service locator's content
    	int selectedIndex = this.getInt(KEY_I_SELECTHOST_INDEX);
    	CommHostDao commHostDao = ((DaoManager) DaoManager.getInstance()).getCommHostDao();
    	commHostDao.setDefaultSelectedIndex(selectedIndex);
   	 	commHostDao.store();
    	
   	 	String[][] urls = HostList.getHostUrls();
 	    if(selectedIndex != -1 && HostList.MANUAL_SERVER_NAME.equals(urls[selectedIndex][HostList.GROUP_NAME]))
        {
 	       String manualUrl = this.getString(KEY_S_MANUAL_SERVER_URL);
 	       urls[selectedIndex][HostList.GROUP_RES_URL] = manualUrl;
 	       ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_MANUAL_SERVER_URL, manualUrl);
 	       ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
        }
 	    
    	CommManager.getInstance().initHostUrls(urls);
    	
    	CommManager.getInstance().initializeServiceLocator();
    }

}
