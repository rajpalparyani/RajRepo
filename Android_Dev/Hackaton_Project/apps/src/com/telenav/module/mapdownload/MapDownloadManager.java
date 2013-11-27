/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MapDownloadManager.java
 *
 */
package com.telenav.module.mapdownload;

import java.util.Vector;

import android.text.TextUtils;

import com.telenav.navsdk.events.MapDownloadData.DownloadableMapRegion;
import com.telenav.navsdk.events.MapDownloadData.MapRegionErrorCode;
import com.telenav.navsdk.events.MapDownloadEvents.MapRegionDownloadProgress;

/**
 *@author yren
 *@date 2012-12-13
 */
public class MapDownloadManager
{
    private Vector<DownloadableMapRegion> downloadableRegions = new Vector<DownloadableMapRegion>();
    private MapRegionDownloadProgress downloadProgress = MapDownLoadMessageHandler.getInstance().getDownloadProgress();
    private boolean isRunning  = false;
    private boolean isStarted  = false;
    private int selectedRegionIndex = -1;
    private int fullyDownloadedRegionIndex = -1;
    
    //FIXME: temporarily hardcode the region names on client side.
    //Because c-server will give us regions out of order.
    public static final String REGION_ID_WESTERN = "Western US";
    public static final String REGION_ID_EASTERN = "Eastern US";
    public static final String REGION_ID_CENTRAL = "Central US";
    
    public synchronized void addRegion(DownloadableMapRegion downloadableRegion)
    {
        if (selectedRegionIndex == -1) {
            selectedRegionIndex = 0;
        }
        boolean isRegionStarted = downloadableRegion.getCurrentlyScheduledForDownload();
        if (isRegionStarted)
        {
            this.isRunning = true;
            this.isStarted = true;
            selectedRegionIndex = downloadableRegions.size();
        }
        
        if(downloadableRegion.getFullyDownloaded())
        {
            this.isRunning = false;
            this.isStarted = false;
            selectedRegionIndex = downloadableRegions.size();
            fullyDownloadedRegionIndex = downloadableRegions.size();
        }
        
        if(downloadableRegion.getPercentDownloaded() > 0 && downloadableRegion.getPercentDownloaded() != 1)
        {
            this.isStarted = true;
            selectedRegionIndex = downloadableRegions.size();
        }
        this.downloadableRegions.add(downloadableRegion);
    }
    
    public synchronized void removeRegion(int index)
    {
        if (index < 0 || index > downloadableRegions.size() - 1)
        {
            return;
        }
        this.downloadableRegions.remove(index);
    }
    
    public synchronized void removeRegion(String regionId)
    {
        for (int i = 0; i < downloadableRegions.size(); i++)
        {
            if (downloadableRegions.elementAt(i).getRegionId().equals(regionId))
            {
                removeRegion(i);
            }
        }
    }
    
    public synchronized String getDisplayName()
    {
        if(selectedRegionIndex == -1)
        {
            return "";
        }
        return downloadableRegions.elementAt(selectedRegionIndex).getRegionDisplayName();
    }
    
    public synchronized String getDescription()
    {
        if(selectedRegionIndex == -1)
        {
            return "";
        }
        return downloadableRegions.elementAt(selectedRegionIndex).getDescription();
    }
    
    public synchronized String getDisplayName(int index)
    {
        return downloadableRegions.elementAt(index).getRegionDisplayName();
    }
    
    public synchronized String getMapName(int index)
    {
        String toParse = downloadableRegions.elementAt(index).getRegionDisplayName();
        if (!TextUtils.isEmpty(toParse))
        {
            String[] result = toParse.split("@");
            if (result.length > 1)
            {
                return result[0];
            }
        }
        return "";
    }
    
    public synchronized String getMapDataInfo(int index)
    {
        String toParse = downloadableRegions.elementAt(index).getRegionDisplayName();
        if (!TextUtils.isEmpty(toParse))
        {
            String[] result = toParse.split("@");
            if (result.length > 1)
            {
                String info= result[1];
                
                info = info.replace(",", ", ");
                return info;
            }
        }
        return "";
    }
    
    public synchronized String getMapRegionId(int index)
    {
        return downloadableRegions.elementAt(index).getRegionId();
    }
    
    public synchronized String getMapRegionId()
    {
        if (selectedRegionIndex == -1)
        {
            return "";
        }
        return downloadableRegions.elementAt(selectedRegionIndex).getRegionId();
    }

    public synchronized boolean getFullyDownloaded(int index)
    {
        if (index == -1)
        {
            if (selectedRegionIndex == -1)
            {
                return false;
            }
            else
            {
                index = selectedRegionIndex;
            }
        }
        
        return downloadableRegions.elementAt(index).getFullyDownloaded();
    }

    public boolean isFullyDownloaded()
    {
        return getFullyDownloaded(-1);
    }
    
    public float getPercentDownloaded()
    {
        if (selectedRegionIndex == -1)
        {
            return 0;
        }
        float percent = 0;
        if (isRunning || getDownloadProgressPercent() > 0)
        {
            percent = getDownloadProgressPercent();
        }
        else
        {
            percent = getDownloadedRegionPercent(selectedRegionIndex);
        }
        
        return percent * 100;
    }
    
    public synchronized long getDownloadSize()
    {
        if(selectedRegionIndex == -1)
        {
            return 0;
        }
        return (int) downloadableRegions.elementAt(selectedRegionIndex).getRegionSizeInBytes();
    }
    
    public boolean isRunning()
    {
       return isRunning;
    }
    
    public synchronized void removeAllRegions()
    {
        downloadableRegions.clear();
    }
    
    public synchronized int getRegionsCount()
    {
        return downloadableRegions.size();
    }
    
    public synchronized boolean getUpdateAvailable()
    {
        return downloadableRegions.elementAt(selectedRegionIndex).getUpgradeAvailable();
    }
    
    public synchronized void setDownloadProgress(MapRegionDownloadProgress progress)
    {
        this.downloadProgress = progress;
        
        if(progress != null && progress.getStatusCode().getNumber() == MapRegionErrorCode.MapRegionError_NotEnoughDiskSpace_VALUE)
        {
            //In this case NavSDK has paused downloading.
            //So we need to update UI to keep consistent with them.
            pauseDownload();
        }
    }
    
    private synchronized float getDownloadProgressPercent()
    {
        if(downloadProgress == null)
        {
            return 0;
        }
        return downloadProgress.getPercentDownloaded();
    }
    
    private synchronized float getDownloadedRegionPercent(int index)
    {
        if (downloadableRegions == null)
        {
            return 0;
        }
        return downloadableRegions.elementAt(index).getPercentDownloaded();
    }
    
    public int getDownloadSpeed()
    {
        if(!isRunning)
        {
            return 0;
        }
        
        if(downloadProgress == null)
        {
            return 0;
        }
        return downloadProgress.getCurrentBytesPerSecond();
    }
    
    public boolean isWifiLostDuringDownload()
    {
        int statusCode = -1;
        if(downloadProgress != null)
        {
            statusCode = downloadProgress.getStatusCode().getNumber();
        }
        
        if(statusCode == MapRegionErrorCode.MapRegionError_MustUseWiFi_VALUE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isDownloadeProgressValid()
    {
        return downloadProgress != null && isRunning;
    }
    
    public void startDownload()
    {
        this.isStarted = true;        
        this.isRunning = true;
    }
    
    public void pauseDownload()
    {
        this.isRunning = false;
    }
    
    public void cancelDownload()
    {
        this.isStarted = false; 
        this.isRunning = false;
        this.downloadProgress = null;
    }
    
    public void deleteDownload()
    {
        this.isStarted = false; 
        this.isRunning = false;
        this.fullyDownloadedRegionIndex = -1;
        downloadProgress = null;
    }
    
    public void finishDownload()
    {
        this.isStarted = false;
        this.isRunning = false;
    }
    
    public void setSelectedRegionIndex(int index)
    {
        this.selectedRegionIndex = index;
    }
    
    public int getSelectedRegionIndex()
    {
        return this.selectedRegionIndex;
    }
    
    public boolean isStarted()
    {
        return isStarted;
    }
    
    public int getFullyDownloadedRegionIndex()
    {
        return fullyDownloadedRegionIndex;
    }
    
    
    /*
     * **********************************************************************
     * ***********************************************************************
     * ***********************************************************************
     */
             
    //FIXME: temporarily implement the reorder logic here.
    //Because c-server will give us regions out of order.
    //If c-server can add some new fields in the response to indicate the order,
    //then we can remove the ugly logic below.
    public int regionIdToIndex(String regionId)
    {
        if (REGION_ID_WESTERN.equalsIgnoreCase(regionId))
        {
            return 0;
        }
        else if (REGION_ID_CENTRAL.equalsIgnoreCase(regionId))
        {
            return 1;
        }
        else if (REGION_ID_EASTERN.equalsIgnoreCase(regionId))
        {
            return 2;
        }
        else
        {
            return -1;
        }
    }
    
    public boolean isWCERegions()
    {
        int count = getRegionsCount();
        
        if(count == 3)
        {
            int value = 0;
            for(int i = 0; i < count; i++)
            {
                String regionId = getMapRegionId(i);
                
                if(REGION_ID_WESTERN.equalsIgnoreCase(regionId))
                {
                    value += 1;
                }
                else if(REGION_ID_CENTRAL.equalsIgnoreCase(regionId))
                {
                    value += 2;
                }
                else if(REGION_ID_EASTERN.equalsIgnoreCase(regionId))
                {
                    value += 4;
                }
                else
                {
                    return false;
                }
            }
            
            //just in case there is only one west, east and central.
            if(value == 7)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    public synchronized void checkRegionSequence()
    {
        if (isWCERegions())
        {
            boolean isDefaultSelectIndex = false;
            
            if(fullyDownloadedRegionIndex == -1 && !isStarted)
            {
                isDefaultSelectIndex = true;
            }
            
            int newSelectedRegionIndex = selectedRegionIndex;
            int newFullyDownloadRegionIndex = fullyDownloadedRegionIndex;
            DownloadableMapRegion[] regions = new DownloadableMapRegion[downloadableRegions.size()];
            for(int i = 0; i < downloadableRegions.size(); i++)
            {
                DownloadableMapRegion region = downloadableRegions.elementAt(i);
                int newIndex = regionIdToIndex(region.getRegionId());
                regions[newIndex] = region;
                
                if(selectedRegionIndex == i)
                {
                    newSelectedRegionIndex = newIndex;
                }
                if(fullyDownloadedRegionIndex == i)
                {
                    newFullyDownloadRegionIndex = newIndex;
                }
            }
            
            fullyDownloadedRegionIndex = newFullyDownloadRegionIndex;
            
            if(isDefaultSelectIndex)
            {
                selectedRegionIndex = 0;
            }
            else
            {
                selectedRegionIndex = newSelectedRegionIndex;
            }
            
            Vector<DownloadableMapRegion> tmp = new Vector<DownloadableMapRegion>();
            for(int i = 0; i < regions.length; i++)
            {
                tmp.addElement(regions[i]);
            }
            
            downloadableRegions = tmp;
        }
    }
}
