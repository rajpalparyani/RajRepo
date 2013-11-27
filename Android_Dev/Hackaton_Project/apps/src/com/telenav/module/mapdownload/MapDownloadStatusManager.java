/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * MapDownloadStatusManager.java
 *
 */
package com.telenav.module.mapdownload;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;

/**
 *@author yning
 *@date 2013-3-25
 */
public class MapDownloadStatusManager
{
    protected boolean isUpdateAvailable = false;
    protected boolean isDownloading = false;
    protected String downloadedRegionName = "";
    protected String mapDataVersion = "";
    
    private static MapDownloadStatusManager instance = new MapDownloadStatusManager();
    
    private Vector<IMapDownloadStatusListener> listeners = new Vector<IMapDownloadStatusListener>();
    private MapDownloadStatusManager()
    {
        
    }
    
    public static MapDownloadStatusManager getInstance()
    {
        return instance;
    }
    
    public boolean isOnBoardDataAvailable()
    {
        return MapDownloadOnBoardDataStatusManager.getInstance().isOnBoardDataAvailable();
    }
    
    public void setIsUpdateAvailable(boolean isUpdateAvailable)
    {
        if(this.isUpdateAvailable != isUpdateAvailable)
        {
            this.isUpdateAvailable = isUpdateAvailable;
            for(int i = 0; i < listeners.size(); i++)
            {
                listeners.elementAt(i).onMapDownloadStatusChanged();
            }
        }
    }
    
    public boolean isUpdateAvailable()
    {
        return this.isUpdateAvailable;
    }
    
    public boolean isBadgeNeeded()
    {
        boolean isPremiumUser = DaoManager.getInstance().getBillingAccountDao().isPremiumAccount();
        boolean isLocalDataAvailable = isOnBoardDataAvailable();

        if (isDownloading)
        {
            return false;
        }
        if (!isLocalDataAvailable && isPremiumUser)
        {
            return true;
        }
        else if (isLocalDataAvailable && isUpdateAvailable)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void setIsDownloading(boolean isDownloading)
    {
        if(this.isDownloading != isDownloading)
        {
            this.isDownloading = isDownloading;
            for(int i = 0; i < listeners.size(); i++)
            {
                listeners.elementAt(i).onMapDownloadStatusChanged();
            }
        }
    }
    
    public void addDownloadStatusChangeListener(IMapDownloadStatusListener listener)
    {
        listeners.removeElement(listener);
        listeners.addElement(listener);
    }
    
    public void removeDownloadStatusChangeListener(IMapDownloadStatusListener listener)
    {
        listeners.removeElement(listener);
    }
    
    public void resetStatus()
    {
        MapDownloadOnBoardDataStatusManager.getInstance().handleStatusChanged(MapDownloadOnBoardDataStatusManager.STATUS_ONBOARD_DATA_UNAVAILABLE);
        setIsDownloading(false);
        setIsUpdateAvailable(false);
        setDownloadedRegionName("");
    }
    
    public String getDownloadedRegionName()
    {
        return downloadedRegionName;
    }
    
    public void setDownloadedRegionName(String name)
    {
        this.downloadedRegionName = name;
    }
    
    public void setMapDataVersion(String version)
    {
        mapDataVersion = version;
    }
    
    public String getMapDataVersion()
    {
        return mapDataVersion;
    }
}
