/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkSettingDataProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.List;

import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkMapDownloadProxyHelper;
import com.telenav.navsdk.events.MapDownloadData.DownloadableMapRegion;
import com.telenav.navsdk.events.MapDownloadEvents.CancelMapRegionDownloadRequest;
import com.telenav.navsdk.events.MapDownloadEvents.CancelMapRegionDownloadStatus;
import com.telenav.navsdk.events.MapDownloadEvents.DeleteMapRegionRequest;
import com.telenav.navsdk.events.MapDownloadEvents.DeleteMapRegionStatus;
import com.telenav.navsdk.events.MapDownloadEvents.DownloadableMapRegionsRequest;
import com.telenav.navsdk.events.MapDownloadEvents.MapRegionDownloadProgress;
import com.telenav.navsdk.events.MapDownloadEvents.PauseMapRegionDownloadRequest;
import com.telenav.navsdk.events.MapDownloadEvents.PauseMapRegionDownloadStatus;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadError;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadRequest;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadStatus;

/**
 * @author jyxu
 * @date 2012-4-10
 */
public class NavSdkMapDownloadProxy extends AbstractNavSdkServerProxy
{
    private NavSdkMapDownloadProxyHelper helper;

    private List<DownloadableMapRegion> downloadableRegionList;

    private MapRegionDownloadProgress mapRegionDownloadProgress;

    private StartMapRegionDownloadStatus startDownloadStatus;
    
    private StartMapRegionDownloadError startDownloadError;

    private PauseMapRegionDownloadStatus pauseDownloadStatus;

    private CancelMapRegionDownloadStatus cancelDownloadStatus;

    private DeleteMapRegionStatus deleteDownloadStatus;

    public NavSdkMapDownloadProxy(IServerProxyListener listener)
    {
        super(listener);
        helper = NavSdkMapDownloadProxyHelper.getInstance();
    }

    protected void handleResponse(byte[] response)
    {
        // TODO Auto-generated method stub

    }

    /**
     * don't concurrent sendGetAllPreferencesRequest and sendGetPreferencesRequest caused by them have same register
     * listener name
     */
    public void sendQueryMapDownloadRequest()
    {
        DownloadableMapRegionsRequest.Builder builder = DownloadableMapRegionsRequest.newBuilder();
        helper.sendQueryMapDownloadRequest(builder.build(), this);
    }

    public void sendStartMapDownloadRequest(String regionId)
    {
        StartMapRegionDownloadRequest.Builder builder = StartMapRegionDownloadRequest.newBuilder();
        builder.setRegionId(regionId);
        helper.sendStartMapDownloadRequest(builder.build(), this);
    }

    public void sendPauseMapDownloadRequest(String regionId)
    {
        PauseMapRegionDownloadRequest.Builder builder = PauseMapRegionDownloadRequest.newBuilder();
        builder.setRegionId(regionId);
        helper.sendPauseMapDownloadRequest(builder.build(), this);
    }

    public void sendCancelMapDownloadRequest(String regionId)
    {
        CancelMapRegionDownloadRequest.Builder builder = CancelMapRegionDownloadRequest.newBuilder();
        builder.setRegionId(regionId);
        helper.sendCancelMapDownloadRequest(builder.build(), this);
    }

    public void sendDeleteMapDownloadRequest(String regionId)
    {
        DeleteMapRegionRequest.Builder builder = DeleteMapRegionRequest.newBuilder();
        builder.setRegionId(regionId);
        helper.sendDeleteMapDownloadRequest(builder.build(), this);
    }

    public int cancel()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public List<DownloadableMapRegion> getDownloadableRegionList()
    {
        return downloadableRegionList;
    }

    public void setDownloadableRegionList(List<DownloadableMapRegion> downloadableRegionList)
    {
        this.downloadableRegionList = downloadableRegionList;
    }

    public void setMapRegionDownloadProgress(MapRegionDownloadProgress progress)
    {
        this.mapRegionDownloadProgress = progress;
    }

    public MapRegionDownloadProgress getMapRegionDownloadProgress()
    {
        return this.mapRegionDownloadProgress;
    }

    public void registerDownloadProgress()
    {
        this.helper.registerRequestCallback(INavSdkProxyConstants.ACT_ON_REGION_DOWNLOAD_PROGRESS, this);
    }

    public StartMapRegionDownloadStatus getStartDownloadStatus()
    {
        return startDownloadStatus;
    }

    public StartMapRegionDownloadError getStartDownloadError()
    {
        return startDownloadError;
    }
    
    public void setStartDownloadStatus(StartMapRegionDownloadStatus startDownloadStatus)
    {
        this.startDownloadStatus = startDownloadStatus;
    }
    
    public void setStartDownloadError(StartMapRegionDownloadError startDownloadError)
    {
        this.startDownloadError = startDownloadError;
    }

    public PauseMapRegionDownloadStatus getPauseDownloadStatus()
    {
        return pauseDownloadStatus;
    }

    public void setPauseDownloadStatus(PauseMapRegionDownloadStatus pauseDownloadStatus)
    {
        this.pauseDownloadStatus = pauseDownloadStatus;
    }

    public CancelMapRegionDownloadStatus getCancelDownloadStatus()
    {
        return cancelDownloadStatus;
    }

    public void setCancelDownloadStatus(CancelMapRegionDownloadStatus cancelDownloadStatus)
    {
        this.cancelDownloadStatus = cancelDownloadStatus;
    }

    public DeleteMapRegionStatus getDeleteDownloadStatus()
    {
        return deleteDownloadStatus;
    }

    public void setDeleteDownloadStatus(DeleteMapRegionStatus deleteDownloadStatus)
    {
        this.deleteDownloadStatus = deleteDownloadStatus;
    }

}
