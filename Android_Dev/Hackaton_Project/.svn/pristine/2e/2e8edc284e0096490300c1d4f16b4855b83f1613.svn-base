/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavSdkSettingDataProxyHelper.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk.helper;

import java.util.HashMap;

import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapDownloadProxy;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.MapDownloadMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler;
import com.telenav.navsdk.eventmanager.EventBus;
import com.telenav.navsdk.events.MapDownloadData.MapRegionErrorCode;
import com.telenav.navsdk.events.MapDownloadEvents.CancelMapRegionDownloadError;
import com.telenav.navsdk.events.MapDownloadEvents.CancelMapRegionDownloadRequest;
import com.telenav.navsdk.events.MapDownloadEvents.CancelMapRegionDownloadStatus;
import com.telenav.navsdk.events.MapDownloadEvents.DeleteMapRegionError;
import com.telenav.navsdk.events.MapDownloadEvents.DeleteMapRegionRequest;
import com.telenav.navsdk.events.MapDownloadEvents.DeleteMapRegionStatus;
import com.telenav.navsdk.events.MapDownloadEvents.DownloadableMapRegionsError;
import com.telenav.navsdk.events.MapDownloadEvents.DownloadableMapRegionsRequest;
import com.telenav.navsdk.events.MapDownloadEvents.DownloadableMapRegionsResponse;
import com.telenav.navsdk.events.MapDownloadEvents.DownloadedMapRegionStatusChangeNotification;
import com.telenav.navsdk.events.MapDownloadEvents.MapRegionDownloadProgress;
import com.telenav.navsdk.events.MapDownloadEvents.PauseMapRegionDownloadError;
import com.telenav.navsdk.events.MapDownloadEvents.PauseMapRegionDownloadRequest;
import com.telenav.navsdk.events.MapDownloadEvents.PauseMapRegionDownloadStatus;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadError;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadRequest;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadStatus;
import com.telenav.navsdk.services.MapDownloadListener;
import com.telenav.navsdk.services.MapDownloadServiceProxy;
import com.telenav.persistent.ITnExternalStorageListener;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.res.IStringMapDownload;
import com.telenav.res.ResourceManager;

/**
 * @author jyxu
 * @date 2011-4-10
 */
public class NavSdkMapDownloadProxyHelper implements MapDownloadListener, INavSdkProxyConstants
{
    private static NavSdkMapDownloadProxyHelper instance;

    private MapDownloadServiceProxy serverProxy;

    private HashMap<String, NavSdkMapDownloadProxy> listeners = new HashMap<String, NavSdkMapDownloadProxy>();

    private INetworkStatusListener networkStatusListener = new INetworkStatusListener()
    {
        
        @Override
        public void statusUpdate(boolean isConnected)
        {
            if(isConnected)
            {
                NavSdkMapDownloadProxy proxy = new NavSdkMapDownloadProxy(null);
                proxy.sendQueryMapDownloadRequest();
                NetworkStatusManager.getInstance().removeStatusListener(this);
            }
        }
    };
    
    ITnExternalStorageListener externalStorageListener = new ITnExternalStorageListener()
    {
        @Override
        public void updateStorageState(int state)
        {
            if(state == TnPersistentManager.MEDIA_MOUNTED)
            {
                NavSdkMapDownloadProxy proxy = new NavSdkMapDownloadProxy(null);
                proxy.sendQueryMapDownloadRequest();
                TnPersistentManager.getInstance().removeExternalFileListener(this);
            }
        }
    };
    
    private NavSdkMapDownloadProxyHelper()
    {
    }

    public synchronized static void init(EventBus bus)
    {
        if (instance == null)
        {
            instance = new NavSdkMapDownloadProxyHelper();
            instance.setEventBus(bus);
        }
    }

    public static NavSdkMapDownloadProxyHelper getInstance()
    {
        return instance;
    }

    public void registerRequestCallback(String action, NavSdkMapDownloadProxy proxy)
    {
        listeners.put(action, proxy);
    }

    private void setEventBus(EventBus bus)
    {
        serverProxy = new MapDownloadServiceProxy(bus);
        serverProxy.addListener(this);
    }

    public void sendQueryMapDownloadRequest(DownloadableMapRegionsRequest request, NavSdkMapDownloadProxy proxy)
    {
        registerRequestCallback(ACT_QUERY_MAP_DOWNLOAD, proxy);
        serverProxy.downloadableMapRegions(request);
    }

    public void sendStartMapDownloadRequest(StartMapRegionDownloadRequest request, NavSdkMapDownloadProxy proxy)
    {
        registerRequestCallback(ACT_START_MAP_DOWNLOAD, proxy);
        serverProxy.startMapRegionDownload(request);
    }

    public void sendPauseMapDownloadRequest(PauseMapRegionDownloadRequest request, NavSdkMapDownloadProxy proxy)
    {
        registerRequestCallback(ACT_PAUSE_MAP_DOWNLOAD, proxy);
        serverProxy.pauseMapRegionDownload(request);
    }

    public void sendCancelMapDownloadRequest(CancelMapRegionDownloadRequest request, NavSdkMapDownloadProxy proxy)
    {
        registerRequestCallback(ACT_CANCEL_MAP_DOWNLOAD, proxy);
        serverProxy.cancelMapRegionDownload(request);
    }

    public void sendDeleteMapDownloadRequest(DeleteMapRegionRequest request, NavSdkMapDownloadProxy proxy)
    {
        registerRequestCallback(ACT_DELETE_MAP_DOWNLOAD, proxy);
        serverProxy.deleteMapRegion(request);
    }

    public void onCancelMapRegionDownloadError(CancelMapRegionDownloadError event)
    {
        String action = ACT_CANCEL_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(getErrorMessage(event.getError(), action));
            proxy.transactionError(action, proxy);
        }
    }

    public void onCancelMapRegionDownloadStatus(CancelMapRegionDownloadStatus event)
    {
        String action = ACT_CANCEL_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setCancelDownloadStatus(event);
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onDeleteMapRegionError(DeleteMapRegionError event)
    {
        String action = ACT_DELETE_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(getErrorMessage(event.getError(), action));
            proxy.transactionError(action, proxy);
        }
    }

    public void onDeleteMapRegionStatus(DeleteMapRegionStatus event)
    {
        String action = ACT_DELETE_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setDeleteDownloadStatus(event);
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onDownloadableMapRegionsError(DownloadableMapRegionsError event)
    {
        String action = ACT_QUERY_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(getErrorMessage(event.getError(), action));
            proxy.transactionError(action, proxy);
        }
    }

    public void onDownloadableMapRegionsResponse(DownloadableMapRegionsResponse event)
    {
        String action = ACT_QUERY_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setDownloadableRegionList(event.getRegionsList());
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onMapRegionDownloadProgress(MapRegionDownloadProgress event)
    {
        String action = ACT_ON_REGION_DOWNLOAD_PROGRESS;
        NavSdkMapDownloadProxy proxy = listeners.get(action);
        
        if (proxy == null)
        {
            return;
        }

        proxy.setMapRegionDownloadProgress(event);
        if (proxy != null)
        {
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onPauseMapRegionDownloadError(PauseMapRegionDownloadError event)
    {
        String action = ACT_PAUSE_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setErrorMsg(getErrorMessage(event.getError(), action));
            proxy.transactionError(action, proxy);
        }
    }

    public void onPauseMapRegionDownloadStatus(PauseMapRegionDownloadStatus event)
    {
        String action = ACT_PAUSE_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setPauseDownloadStatus(event);
            proxy.transactionFinished(action, proxy);
        }
    }

    public void onStartMapRegionDownloadError(StartMapRegionDownloadError event)
    {
        String action = ACT_START_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        
        if (proxy != null)
        {
            proxy.setStartDownloadError(event);
            proxy.setErrorMsg(getErrorMessage(event.getError(), action));
            proxy.transactionError(action, proxy);
        }
    }

    private String getErrorMessage(MapRegionErrorCode errorCode, String action)
    {
        if (errorCode == null)
        {
            return "";
        }
        switch (errorCode)
        {
            case MapRegionError_MustUseWiFi:
            {
                if (ACT_START_MAP_DOWNLOAD.equals(action))
                {
                    return ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringMapDownload.RES_ERROR_NO_WIFI_START, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                }
                else
                {
                    return ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringMapDownload.RES_ERROR_NO_WIFI, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                }
            }
            case MapRegionError_NotEnoughDiskSpace:
            {
                int stringKey = isSDCardAvailable() ? IStringMapDownload.RES_ERROR_NOT_ENOUGH_SPACE
                        : IStringMapDownload.RES_ERROR_NO_SD_CARD;
                return ResourceManager.getInstance().getCurrentBundle()
                        .getString(stringKey, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
            }
            case MapRegionError_MetaDataNotReady:
            {
                if(isSDCardAvailable() && !NetworkStatusManager.getInstance().isConnected())
                {
                    TnPersistentManager.getInstance().removeExternalFileListener(externalStorageListener);
                    NetworkStatusManager.getInstance().removeStatusListener(networkStatusListener);
                    NetworkStatusManager.getInstance().addStatusListener(networkStatusListener);
                }
                else if(!isSDCardAvailable() && NetworkStatusManager.getInstance().isConnected())
                {
                    NetworkStatusManager.getInstance().removeStatusListener(networkStatusListener);
                    TnPersistentManager.getInstance().removeExternalFileListener(externalStorageListener);
                    TnPersistentManager.getInstance().addExternalFileListener(externalStorageListener);
                }
                
                int stringKey = isSDCardAvailable() ? IStringMapDownload.RES_ERROR_CHECK_YOUR_NETWORK : IStringMapDownload.RES_ERROR_NO_SD_CARD;
                return ResourceManager.getInstance().getCurrentBundle()
                        .getString(stringKey, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
            }
            case MapRegionError_UnknownError:
            default:
            {
                return ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringMapDownload.RES_ERROR_UNKNOWN, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
            }
        }
    }

    private boolean isSDCardAvailable()
    {
        return TnPersistentManager.getInstance().getExternalStorageState() == TnPersistentManager.MEDIA_MOUNTED;
    }

    public void onStartMapRegionDownloadStatus(StartMapRegionDownloadStatus event)
    {
        String action = ACT_START_MAP_DOWNLOAD;
        NavSdkMapDownloadProxy proxy = listeners.remove(action);
        if (proxy != null)
        {
            proxy.setStartDownloadError(null);
            proxy.setStartDownloadStatus(event);
            proxy.transactionFinished(action, proxy);
        }
        
        MapDownloadMisLog mislog = (MapDownloadMisLog)MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_MAP_DOWNLOAD_START);
        mislog.setRegionId(event.getRegionId());
        mislog.setLocation();
        mislog.setTimestamp(System.currentTimeMillis());
        Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
        { mislog });
    }

    public void onDownloadedMapRegionStatusChangeNotification(DownloadedMapRegionStatusChangeNotification event)
    {
        MapDownLoadMessageHandler.getInstance().handleDownloadedMapStatusChange(event);
    }
}
