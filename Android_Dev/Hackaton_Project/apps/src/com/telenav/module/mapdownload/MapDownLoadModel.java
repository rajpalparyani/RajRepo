/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * MapDownLoadModel.java
 *
 */
package com.telenav.module.mapdownload;

import java.util.List;

import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapDownloadProxy;
import com.telenav.logger.Logger;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler.IDownloadedMapStatusChangeListener;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler.IMapDownloadProgressListener;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapDownloadData.DownloadableMapRegion;
import com.telenav.navsdk.events.MapDownloadData.MapRegionErrorCode;
import com.telenav.navsdk.events.MapDownloadEvents.DeleteMapRegionStatus;
import com.telenav.navsdk.events.MapDownloadEvents.MapRegionDownloadProgress;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadError;
import com.telenav.navsdk.events.MapDownloadEvents.StartMapRegionDownloadStatus;
import com.telenav.radio.TnRadioManager;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringMapDownload;
import com.telenav.res.ResourceManager;

/**
 *@author yren
 *@date 2012-12-11
 */
class MapDownLoadModel extends AbstractCommonNetworkModel implements IMapDownLoadConstants, IDownloadedMapStatusChangeListener, IMapDownloadProgressListener
{
    NavSdkMapDownloadProxy mapDownLoadProxy = new NavSdkMapDownloadProxy(this);

    MapDownloadManager mapDownloadManager = new MapDownloadManager();
    
    private Object replaceLock = new Object();

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (!this.isActivated())
        {
            return;
        }
        String action = proxy.getRequestAction();
        if (INavSdkProxyConstants.ACT_QUERY_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            List<DownloadableMapRegion> downloadableRegionList = ((NavSdkMapDownloadProxy) proxy).getDownloadableRegionList();
            synchronized (replaceLock)
            {
                replaceLock.notifyAll();
            }
            //Update the status.
            MapDownloadOnBoardDataStatusManager.getInstance().checkStatus(downloadableRegionList);
            
            for (int i = 0; i < downloadableRegionList.size(); i++)
            {
                mapDownloadManager.addRegion(downloadableRegionList.get(i));
            }
            
            mapDownloadManager.checkRegionSequence();
            
            if(mapDownloadManager.isStarted())
            {
                postModelEvent(EVENT_MODEL_SHOW_DOWNLOADING);
            }
            else
            {
                postModelEvent(EVENT_MODEL_SHOW_DOWNLOADABLE_REGION);
            }
        }
        else if (INavSdkProxyConstants.ACT_ON_REGION_DOWNLOAD_PROGRESS.equalsIgnoreCase(action))
        {
            MapRegionDownloadProgress progress = ((NavSdkMapDownloadProxy) proxy).getMapRegionDownloadProgress();
            mapDownloadManager.setDownloadProgress(progress);
            if (progress.getPercentDownloaded() == 1.0f)
            {
                postModelEvent(EVENT_MODEL_DOWNLOAD_COMPLETE);
            }
            else
            {
                postModelEvent(EVENT_MODEL_UPDATE_VIEW);
            }
        }
        else if (INavSdkProxyConstants.ACT_START_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            mapDownloadManager.startDownload();
            StartMapRegionDownloadStatus status = ((NavSdkMapDownloadProxy)proxy).getStartDownloadStatus();
            if (status !=  null)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("start download ");
                sb.append(status.getRegionId());
                sb.append(" map data, status is ");
                sb.append(status.getStatusString());
                Logger.log(Logger.INFO, getClass().getSimpleName(), sb.toString());
            }
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            postModelEvent(EVENT_MODEL_SHOW_DOWNLOADING);
            
            checkRegionStatus();
        }
        else if (INavSdkProxyConstants.ACT_PAUSE_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            mapDownloadManager.pauseDownload();
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
        else if (INavSdkProxyConstants.ACT_CANCEL_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            mapDownloadManager.cancelDownload();
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            checkRegionStatus();
        }
        else if (INavSdkProxyConstants.ACT_DELETE_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            mapDownloadManager.deleteDownload();
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            postModelEvent(EVENT_MODEL_SET_BACK_BUTTON);
            checkRegionStatus();
            DeleteMapRegionStatus status = ((NavSdkMapDownloadProxy)proxy).getDeleteDownloadStatus();
            if (status !=  null)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("delete ");
                sb.append(status.getRegionId());
                sb.append(" map data, status is ");
                sb.append(status.getStatusString());
                Logger.log(Logger.INFO, getClass().getSimpleName(), sb.toString());
            }
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        String action = proxy.getRequestAction();
        if (INavSdkProxyConstants.ACT_START_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
            StartMapRegionDownloadError error = ((NavSdkMapDownloadProxy)proxy).getStartDownloadError();
            
            if(error != null)
            {
                int number = error.getError().getNumber();
                
                if(number == MapRegionErrorCode.MapRegionError_NotEnoughDiskSpace_VALUE)
                {
                    //ignore it because we will handle this type in general place.
                    return;
                }
            }
        }
        else if (INavSdkProxyConstants.ACT_DELETE_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            postModelEvent(EVENT_MODEL_SET_BACK_BUTTON);
            checkRegionStatus();
        }
        else if (INavSdkProxyConstants.ACT_PAUSE_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
        else if (INavSdkProxyConstants.ACT_CANCEL_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
            checkRegionStatus();
        }
        else if (INavSdkProxyConstants.ACT_QUERY_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            MapDownloadStatusManager.getInstance().resetStatus();
        }
        
        String errorMessage = proxy.getErrorMsg();
        if (errorMessage == null || errorMessage.length() == 0)
        {
            errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
        }
        put(ICommonConstants.KEY_S_ERROR_MESSAGE, errorMessage);
        postModelEvent(ICommonConstants.EVENT_MODEL_MAP_DOWNLOAD_ERROR);
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_CHECK_DELETING:
            {
                boolean isDeleting = MapDownLoadMessageHandler.getInstance().isDeleting();
                if(isDeleting)
                {
                    String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_STILL_DELETING, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                    put(KEY_S_ERROR_MESSAGE, errorMessage);
                    postModelEvent(EVENT_MODEL_STILL_DELETING);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_NOT_DELETING);
                }
                
                break;
            }
            case ACTION_INIT:
            {
                this.put(KEY_O_MAP_DOWNLOAD_MANAGER, mapDownloadManager);
                checkRegionStatus();
                
                MapDownLoadMessageHandler.getInstance().setDownloadedMapStatusChangeListener(this);
                MapDownLoadMessageHandler.getInstance().addMapDownloadProgressListener(this);
                
                break;
            }
            case ACTION_CHECK_STATUS_CHANGED:
            {
                put(KEY_B_IS_MODULE_READY_TO_SHOW_MAP_NOTIFICATION, true);
                checkMapDownloadStatus();
                break;
            }
            case ACTION_START_DOWNLOAD:
            {
                startMapDownloadRequest();
                break;
            }
            case ACTION_CHECK_WIFI:
            {
                boolean isWifiNetWorkConnected = TnRadioManager.getInstance().isWifiConnected();
                if (isWifiNetWorkConnected)
                {
                    int cmdId = this.fetchInt(KEY_I_CMD_TO_CHECK_WIFI);
                    switch (cmdId)
                    {
                        case CMD_START_DOWNLOAD:
                        case CMD_RESUME_DOWNLOAD:
                        {
                            postModelEvent(EVENT_MODEL_START_DOWNLOAD);
                            break;
                        }
                        case CMD_UPGRADE:
                        {
                            postModelEvent(EVENT_MODEL_UPGRADE);
                            break;
                        }
                        case CMD_REPLACE_DOWNLOAD:
                        {
                            postModelEvent(EVENT_MODEL_REPLACE_DOWNLOAD);
                            break;
                        }
                        default:
                        {
                            put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
                            this.postModelEvent(EVENT_MODEL_WIFI_DISCONNECTED);
                            break;
                        }
                    }
                }
                else
                {
                    put(KEY_B_IS_NEED_SET_BACK_BUTTON, true);
                    this.postModelEvent(EVENT_MODEL_WIFI_DISCONNECTED);
                }
                break;
            }
            case ACTION_CANCEL_DOWNLOAD:
            {
                cancelMapDownloadRequest();
//                checkRegionStatus();
                break;
            }
            case ACTION_PAUSE_DOWNLOAD:
            {
                pauseMapDownloadRequest();
                break;
            }
            case ACTION_CHECK_REGION_STATUS:
            {
                checkRegionStatus();
                break;
            }
            case ACTION_UPGRADE:
            {
                upgrade();
                break;
            }
            case ACTION_REPLACE:
            {
                Thread thread = new Thread()
                {
                    public void run()
                    {
                        replace();
                    }
                };
                thread.start();
                break;
            }
            case ACTION_DELETE_DOWNLOAD:
            {
                deleteMapDownloadRequest(false);
//                checkRegionStatus();
                break;
            }
            case ACTION_CHECK_UPSELL_RETURN:
            {
                boolean isUpsellFinish = isFeatureEnabled(); //if the feature becomes enabled, it means upsell finished.
                if(isUpsellFinish)
                {
                    int upsellOperation = fetchInt(KEY_I_CMD_TO_TRIGGER_UPSELL);
                    switch(upsellOperation)
                    {
                        case CMD_START_DOWNLOAD:
                        case CMD_REPLACE_DOWNLOAD:
                        case CMD_UPGRADE:
                        {
                            put(KEY_I_CMD_TO_CHECK_WIFI, upsellOperation);
                            postModelEvent(EVENT_MODEL_DO_ACTION_AFTER_UP_SELL);
                            break;
                        }
                        default:
                        {
                            postModelEvent(EVENT_MODEL_DO_NOTHING_AFTER_UP_SELL);
                        }
                    }
                }
                else
                {
                    postModelEvent(EVENT_MODEL_DO_NOTHING_AFTER_UP_SELL);
                }
                
                break;
            }
            case ACTION_CHECK_MODULE_READY:
            {
                checkMapDownloadStatus();
                break;
            }
            default:
            {
                break;
            }
        }
    }

    @Override
    protected void activateDelegate(boolean isUpdateView)
    {
        checkMapDownloadStatus();
    }
    
    protected void pauseMapDownloadRequest()
    {
        String regionId = mapDownloadManager.getMapRegionId();
        mapDownLoadProxy.sendPauseMapDownloadRequest(regionId);
    }

    protected void cancelMapDownloadRequest()
    {
        String regionId = mapDownloadManager.getMapRegionId();
        mapDownLoadProxy.sendCancelMapDownloadRequest(regionId);
    }

    protected void startMapDownloadRequest()
    {
        String regionId = mapDownloadManager.getMapRegionId();
        mapDownLoadProxy.sendStartMapDownloadRequest(regionId);
        Logger.log(Logger.INFO, getClass().getSimpleName(), "try to start download " + regionId + " map data");
    }
    
    protected void startMapDownloadRequest(String regionId)
    {
        mapDownLoadProxy.sendStartMapDownloadRequest(regionId);
        Logger.log(Logger.INFO, getClass().getSimpleName(), "try to start download " + regionId + " map data");
    }
    
    protected void deleteMapDownloadRequest(boolean isReplace)
    {
        MapDownLoadMessageHandler.getInstance().setOperateListener(this);
        String regionId = isReplace ? mapDownloadManager.getMapRegionId(mapDownloadManager.getFullyDownloadedRegionIndex())
                : mapDownloadManager.getMapRegionId();
        MapDownLoadMessageHandler.getInstance().deleteRegion(regionId);
        Logger.log(Logger.INFO, getClass().getSimpleName(), "try to delete " + regionId + " map data");
    }

    protected void checkRegionStatus()
    {
        mapDownloadManager.removeAllRegions();
        mapDownLoadProxy.sendQueryMapDownloadRequest();
    }
    
    protected void upgrade()
    {
        deleteMapDownloadRequest(false);
        startMapDownloadRequest();
    }
    
    protected void replace()
    {
        String regionId = mapDownloadManager.getMapRegionId();
        deleteMapDownloadRequest(true);
        synchronized (replaceLock)
        {
            try
            {
                replaceLock.wait(5000);
            }
            catch (InterruptedException e)
            {
                
            }
        }
        startMapDownloadRequest(regionId);
    }
    
    protected boolean isFeatureEnabled()
    {
        int status = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_HYBRID);
        if(status == FeaturesManager.FE_ENABLED || status == FeaturesManager.FE_PURCHASED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    protected synchronized void checkMapDownloadStatus()
    {
        // For some modules, they may have very important flow at the initialization stage.
        // So we need to make sure the module is ready for showing the popup.
        boolean isModuleReady = getBool(ICommonConstants.KEY_B_IS_MODULE_READY_TO_SHOW_MAP_NOTIFICATION);
        if(isModuleReady)
        {
            boolean isDownloadedMapStatusChanged = MapDownLoadMessageHandler.getInstance().isMapIncompatible;
            if(isDownloadedMapStatusChanged)
            {
                Thread t = new Thread() 
                {
                    public void run()
                    {
                        postModelEvent(ICommonConstants.EVENT_MODEL_SHOW_MAP_DOWNLOADED_STATUS_CHANGED_NOTIFICATION);
                    }
                };
                
                t.start();
            }
        }
    }

    @Override
    public void onMapDownloadStatusChanged()
    {
        postModelEvent(EVENT_MODEL_MAP_DOWNLOADED_STATUS_CHANGED);
    }
    
    @Override
    protected void releaseDelegate()
    {
        MapDownLoadMessageHandler.getInstance().removeDownloadedMapStatusChangeListener();
        MapDownLoadMessageHandler.getInstance().removeMapDownloadProgressListener(this);
        MapDownLoadMessageHandler.getInstance().removeOperateListener();
    }

    @Override
    public void onMapProgressUpdate(MapRegionDownloadProgress progress)
    {
        mapDownloadManager.setDownloadProgress(progress);
        
        if (progress.getPercentDownloaded() == 1.0f)
        {
            postModelEvent(EVENT_MODEL_DOWNLOAD_COMPLETE);
        }
        else
        {
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
}
