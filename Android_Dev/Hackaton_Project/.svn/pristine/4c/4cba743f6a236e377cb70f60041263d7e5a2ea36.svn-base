/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * MapDownloadExternalStorageStatusListener.java
 *
 */
package com.telenav.module.mapdownload;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapDownloadProxy;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler.IMapDownloadProgressListener;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.navsdk.events.MapDownloadData.DownloadableMapRegion;
import com.telenav.navsdk.events.MapDownloadEvents.MapRegionDownloadProgress;
import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.TnPersistentManager;

/**
 *@author yning
 *@date 2013-5-24
 */
public class MapDownloadOnBoardDataStatusManager implements ITnExternalStorageListener, IServerProxyListener, IMapDownloadProgressListener
{
    private int status = -1;
    
    public static final int STATUS_ONBOARD_DATA_AVAILABLE = 1;
    public static final int STATUS_ONBOARD_DATA_UNAVAILABLE = 2;
    private Vector<IOnBoardDataAvailabilityListener> statusListeners = new Vector<IOnBoardDataAvailabilityListener>();
    
    public static MapDownloadOnBoardDataStatusManager instance = new MapDownloadOnBoardDataStatusManager();
    
    private MapDownloadOnBoardDataStatusManager()
    {
        init();
    }
    
    public static MapDownloadOnBoardDataStatusManager getInstance()
    {
        return instance;
    }
    
    protected void init()
    {
        MapDownLoadMessageHandler.getInstance().addMapDownloadProgressListener(this);
        TnPersistentManager.getInstance().addExternalFileListener(this);
        queryMapDownloadStatus();
    }
    
    @Override
    public void updateStorageState(int state)
    {
        triggerNavSdkCheckMapDownload(state);
    }
    
    public void triggerNavSdkCheckMapDownload(int state)
    {
        StringMap featureList = ((DaoManager) DaoManager.getInstance()).getUpsellDao().getFeatureList();
        
        if(featureList != null)
        {
            String value = "";
            if(state == TnPersistentManager.MEDIA_MOUNTED)
            {
                value = FeaturesManager.FE_ENABLED + "";
                featureList.put(FeaturesManager.FEATURE_CODE_MAP_DOWNLOAD_PATH_AVAILABLE, value);
                NavsdkUserManagementService.getInstance().featureListUpdate();
                
                queryMapDownloadStatus();
            }
            else
            {
                value = FeaturesManager.FE_DISABLED + "";
                featureList.put(FeaturesManager.FEATURE_CODE_MAP_DOWNLOAD_PATH_AVAILABLE, value);
                
                MapDownloadStatusManager.getInstance().resetStatus();
            }
        }
    }
    
    public void queryMapDownloadStatus()
    {
        NavSdkMapDownloadProxy proxy = new NavSdkMapDownloadProxy(this);
        proxy.sendQueryMapDownloadRequest();
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        handleStatusChanged(status);
    }

    protected void handleStatusChanged(int currentStatus)
    {
        int oldStatus = this.status;
        this.status = currentStatus;
        if(oldStatus != currentStatus)
        {
            boolean isAvailable = (currentStatus == STATUS_ONBOARD_DATA_AVAILABLE);
            for(int i = 0; i < statusListeners.size(); i++)
            {
                IOnBoardDataAvailabilityListener listener = statusListeners.elementAt(i);
                listener.onLocalMapDataAvailabilityChanged(isAvailable);
            }
        }
    }
    
    public void addStatusChangeListener(IOnBoardDataAvailabilityListener listener)
    {
        statusListeners.removeElement(listener);
        statusListeners.addElement(listener);
    }
    
    public void removeStatusChangeListener(IOnBoardDataAvailabilityListener listener)
    {
        statusListeners.removeElement(listener);
    }
    
    protected synchronized void checkStatus(List<DownloadableMapRegion> list)
    {
        boolean isFullyDownloaded = false;
        boolean isDownloading = false;
        boolean isUpdateAvailable = false;
        String downloadedName = "";
        String description = "";
        for(int i = 0; i < list.size(); i++)
        {
            DownloadableMapRegion region = list.get(i);
            if(region.getFullyDownloaded())
            {
                isFullyDownloaded = true;
                downloadedName = region.getRegionId();
                if(region.getUpgradeAvailable())
                {
                    isUpdateAvailable = true;
                }
            }
            else if(region.getCurrentlyScheduledForDownload())
            {
                isDownloading = true;
            }
            else if(region.getPercentDownloaded() > 0 && region.getPercentDownloaded() != 1)
            {
                isDownloading = true;
            }
            
            //This version is included in each region.
            //So we only need to get it from the first one.
            if (i == 0)
            {
                description = region.getDescription();
            }
        }
        
        if(isFullyDownloaded)
        {
            isDownloading = false;
            handleStatusChanged(STATUS_ONBOARD_DATA_AVAILABLE);
        }
        else
        {
            handleStatusChanged(STATUS_ONBOARD_DATA_UNAVAILABLE);
        }
        
        String version = getDataVersion(description);
        
        MapDownloadStatusManager.getInstance().setDownloadedRegionName(downloadedName);
        MapDownloadStatusManager.getInstance().setIsDownloading(isDownloading);
        MapDownloadStatusManager.getInstance().setIsUpdateAvailable(isUpdateAvailable);
        if(version != null && version.trim().length() > 0)
        {
            MapDownloadStatusManager.getInstance().setMapDataVersion(version);
        }
    }
    
    private String getDataVersion(String description)
    {
        if(description == null)
        {
            return "";
        }
        
        Hashtable table = new Hashtable();

        String[] parts = description.split(";");

        for (int i = 0; i < parts.length; i++)
        {
            String part = parts[i];
            String[] subParts = part.split("=");
            if (subParts.length > 1)
            {
                table.put(subParts[0], subParts[1]);
            }
        }

        String version = (String) table.get("DataVersion");

        if (version == null)
        {
            version = "";
        }

        return version;
    }
    
    public boolean isOnBoardDataAvailable()
    {
        return status == STATUS_ONBOARD_DATA_AVAILABLE;
    }
    
    @Override
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        
        if(INavSdkProxyConstants.ACT_QUERY_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            NavSdkMapDownloadProxy mapDownloadProxy = (NavSdkMapDownloadProxy)proxy;
            List<DownloadableMapRegion> list = mapDownloadProxy.getDownloadableRegionList();
            checkStatus(list);
        }
    }
    
    @Override
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void transactionError(AbstractServerProxy proxy)
    {
        String action = proxy.getRequestAction();
        
        if(INavSdkProxyConstants.ACT_QUERY_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            MapDownloadStatusManager.getInstance().resetStatus();
        }
    }
    
    @Override
    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void onMapProgressUpdate(MapRegionDownloadProgress progress)
    {
        if(progress.getPercentDownloaded() == 1.0f)
        {
            queryMapDownloadStatus();
        }
    }
}
