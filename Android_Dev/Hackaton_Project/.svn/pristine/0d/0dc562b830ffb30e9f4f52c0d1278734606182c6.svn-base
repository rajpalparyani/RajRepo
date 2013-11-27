/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DownloadedMapRegionStatusChangeHandler.java
 *
 */
package com.telenav.module.mapdownload;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.widget.Toast;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapDownloadProxy;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.MapDownloadMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.entry.EntryController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapDownloadData.DownloadableMapRegion;
import com.telenav.navsdk.events.MapDownloadData.DownloadedMapRegionStatusChangeCode;
import com.telenav.navsdk.events.MapDownloadData.MapRegionErrorCode;
import com.telenav.navsdk.events.MapDownloadEvents.DownloadedMapRegionStatusChangeNotification;
import com.telenav.navsdk.events.MapDownloadEvents.MapRegionDownloadProgress;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.res.IStringMapDownload;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author yning
 *@date 2013-2-26
 */
public class MapDownLoadMessageHandler implements IServerProxyListener
{
    private static MapDownLoadMessageHandler instance = new MapDownLoadMessageHandler();
    
    private IDownloadedMapStatusChangeListener downloadedMapStatusChangeListener;
    
    private NavSdkMapDownloadProxy proxy;
    
    private MapDownloadManager mapDownloadManager;
    
    private MapRegionDownloadProgress progress;
    
    private Vector<IMapDownloadProgressListener> mapDownloadProgressListeners = new Vector<IMapDownloadProgressListener>();
    
    boolean isHandleErrorDuringDownload = false;
    
    boolean isCurrentlyNotWifi = false;
    
    boolean isDeleting = false;
    
    public boolean isMapIncompatible = false;
    
    public String mapIncompatibleMessage = "";
    
    private IServerProxyListener mapDownloadOperationListener;
    
    private MapDownLoadMessageHandler()
    {
        
    }
    
    public void init()
    {
        if(proxy == null)
        {
            proxy = new NavSdkMapDownloadProxy(this);
            mapDownloadManager = new MapDownloadManager();
            proxy.registerDownloadProgress();
        }
    }
    
    public static MapDownLoadMessageHandler getInstance()
    {
        return instance;
    }
    
    public void setDownloadedMapStatusChangeListener(IDownloadedMapStatusChangeListener listener)
    {
        this.downloadedMapStatusChangeListener = listener;
    }
    
    public void removeDownloadedMapStatusChangeListener()
    {
        this.downloadedMapStatusChangeListener = null;
    }
    
    public synchronized void addMapDownloadProgressListener(IMapDownloadProgressListener listener)
    {
        mapDownloadProgressListeners.removeElement(listener);
        mapDownloadProgressListeners.addElement(listener);
    }
    
    public synchronized void removeMapDownloadProgressListener(IMapDownloadProgressListener listener)
    {
        mapDownloadProgressListeners.removeElement(listener);
    }
    
    public void handleDownloadedMapStatusChange(DownloadedMapRegionStatusChangeNotification event)
    {
        DownloadedMapRegionStatusChangeCode status = event.getStatus();
        switch(status.getNumber())
        {
            case DownloadedMapRegionStatusChangeCode.MapRegionStatus_ForceDeleted_VALUE:
            case DownloadedMapRegionStatusChangeCode.MapRegionStatus_IncompatibleData_VALUE:
            {
                MapDownloadOnBoardDataStatusManager.getInstance().queryMapDownloadStatus();
                String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_DOWNLOADED_DATA_NOT_VALID, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                handleStatusChanged(message);
                break;
            }
            case DownloadedMapRegionStatusChangeCode.MapRegionStatus_UpdateAvailable_VALUE:
            {
                MapDownloadOnBoardDataStatusManager.getInstance().queryMapDownloadStatus();
                String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_DOWNLOADED_DATA_UPDATE_AVAILABLE, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                handleStatusChanged(message);
                break;
            }
            case DownloadedMapRegionStatusChangeCode.MapRegionStatus_InstallFailed_VALUE:
            {
                MapDownloadStatusManager.getInstance().resetStatus();
                String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_INSTALL_FAIL, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                handleStatusChanged(message);
                break;
            }
        };
    }
    
    protected void handleStatusChanged(String message)
    {
        isMapIncompatible = true;
        mapIncompatibleMessage = message;
        if(downloadedMapStatusChangeListener != null)
        {
            downloadedMapStatusChangeListener.onMapDownloadStatusChanged();
        }
    }
    
    public void setMapIncompatibleMessageDone()
    {
        isMapIncompatible = false;
    }
    
    /**
     * test this function
     * @param eventType
     */
    public void handleDownloadedMapStatusChange(int eventType)
    {
        switch(eventType)
        {
            case DownloadedMapRegionStatusChangeCode.MapRegionStatus_ForceDeleted_VALUE:
            case DownloadedMapRegionStatusChangeCode.MapRegionStatus_IncompatibleData_VALUE:
            {
                String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_DOWNLOADED_DATA_NOT_VALID, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                handleStatusChanged(message);
                break;
            }
            case DownloadedMapRegionStatusChangeCode.MapRegionStatus_UpdateAvailable_VALUE:
            {
                String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_DOWNLOADED_DATA_UPDATE_AVAILABLE, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                handleStatusChanged(message);
                break;
            }
        };
    }

    @Override
    public synchronized void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        if(INavSdkProxyConstants.ACT_ON_REGION_DOWNLOAD_PROGRESS.equalsIgnoreCase(action))
        {
            MapRegionDownloadProgress progress = ((NavSdkMapDownloadProxy) proxy).getMapRegionDownloadProgress();
            mapDownloadManager.setDownloadProgress(progress);
            this.progress = progress;
            
            if (Logger.DEBUG)
            {
                float percent = -99;
                if(progress != null)
                {
                    percent = progress.getPercentDownloaded();
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "MapDownload === " + "progress received : "
                        + percent);
            }
            
            if(mapDownloadProgressListeners != null && mapDownloadProgressListeners.size() > 0)
            {
                for(int i = 0; i < mapDownloadProgressListeners.size(); i++)
                {
                    IMapDownloadProgressListener listener = mapDownloadProgressListeners.elementAt(i);
                    listener.onMapProgressUpdate(progress);
                }
            }
            
            if(progress.getPercentDownloaded() == 1.0f)
            {
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Context mContext = AndroidPersistentContext.getInstance().getContext();
                        String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_MAP_DOWNLOAD_SUCC, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                MapDownloadMisLog mislog = (MapDownloadMisLog)MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_MAP_DOWNLOAD_COMPLETE);
                mislog.setRegionId(progress.getRegionId());
                mislog.setLocation();
                mislog.setTimestamp(System.currentTimeMillis());
                Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                { mislog });
            }
            
            int number = progress.getStatusCode().getNumber();
            
            if (number == MapRegionErrorCode.MapRegionError_NotEnoughDiskSpace_VALUE)
            {
                isCurrentlyNotWifi = false;
                if(isSDCardAvailable())
                {
                    isHandleErrorDuringDownload = true;
                    ((NavSdkMapDownloadProxy)proxy).sendQueryMapDownloadRequest();
                }
                else
                {
                    String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_NO_SD_CARD, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                    postErrorMessage(errorMessage);
                }
            }
            else if (number == MapRegionErrorCode.MapRegionError_MustUseWiFi_VALUE)
            {
                // Introduce this flag, because NavSDK will give us this event whenever we call sendDisconnectedMessage() in NavsdkNetworkService.
                // e.g., we lose wifi for first time, and the device fall back to 3G. Then we we lose 3G, we will call sendDisconnectedMessage() again.
                // NavSDK will give this event when we lose 3G but we shouldn't show message at that time.
                if(!isCurrentlyNotWifi)
                {
                    String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_NO_WIFI, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                    postErrorMessage(errorMessage);
                }
                isCurrentlyNotWifi = true;
            }
            else
            {
                isCurrentlyNotWifi = false;
            }
        }
        else if(INavSdkProxyConstants.ACT_QUERY_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            List<DownloadableMapRegion> regions = ((NavSdkMapDownloadProxy)proxy).getDownloadableRegionList();
            
            if(isHandleErrorDuringDownload)
            {
                String errorMessage = "";
                String regionId = progress.getRegionId();
                
                if(regionId == null)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_NOT_ENOUGH_SPACE, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                }
                else
                {
                    double size = 0;
                    for(int i = 0; i < regions.size(); i++)
                    {
                        DownloadableMapRegion region = regions.get(i);
                        if(regionId.equalsIgnoreCase(region.getRegionId()))
                        {
                            size = region.getRegionSizeInBytes() * (1 - region.getPercentDownloaded());
                            break;
                        }
                    }
                    
                    long byteSize = (long)(size + 0.5);
                    
                    if(byteSize == 0)
                    {
                        byteSize = 1;
                    }
                    
                    String displayedSize = ResourceManager.getInstance().getStringConverter().convertDataSizeString(byteSize, 2, true);
                    
                    String pattern = ResourceManager.getInstance().getCurrentBundle().getString(IStringMapDownload.RES_ERROR_NOT_ENOUGH_SPACE_DETAILED, IStringMapDownload.FAMILY_MAP_DOWNLOAD);
                    
                    errorMessage = ResourceManager.getInstance().getStringConverter().convert(pattern, new String[]{displayedSize});
                }
                
                postErrorMessage(errorMessage);
                
                isHandleErrorDuringDownload = false;
            }
        }
        else if(INavSdkProxyConstants.ACT_DELETE_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            isDeleting = false;
            if(mapDownloadOperationListener != null)
            {
                mapDownloadOperationListener.transactionFinished(proxy, jobId);
            }
        }
    }

    public void postErrorMessage(String errorMessage)
    {
        AbstractController controller = AbstractController.getCurrentController();
        
        if(controller != null)
        {
            AbstractModel model = controller.getModel();
            if (controller instanceof EntryController)  //Avoid show the pop up in the splash screen, delay to show it on dashboard
            {
                ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_IS_NAVSDK_POPUP_DELAY, true);
                ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_NAVSDK_POPUP_ERROR_MSG, errorMessage);
            }
            else
            {
                model.put(ICommonConstants.KEY_S_ERROR_MESSAGE, errorMessage);
                controller.handleModelEvent(ICommonConstants.EVENT_MODEL_MAP_DOWNLOAD_ERROR);
            }
        }
    }
    
    @Override
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }

    @Override
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        
    }

    @Override
    public void transactionError(AbstractServerProxy proxy)
    {
        String action = proxy.getRequestAction();
        if(INavSdkProxyConstants.ACT_DELETE_MAP_DOWNLOAD.equalsIgnoreCase(action))
        {
            isDeleting = false;
            if(mapDownloadOperationListener != null)
            {
                mapDownloadOperationListener.transactionError(proxy);
            }
        }
    }

    @Override
    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }
    
    public interface IDownloadedMapStatusChangeListener
    {
        public void onMapDownloadStatusChanged();
    }
    
    public interface IMapDownloadProgressListener
    {
        public void onMapProgressUpdate(MapRegionDownloadProgress progress);
    }
    
    public MapRegionDownloadProgress getDownloadProgress()
    {
        return progress;
    }
    
    private boolean isSDCardAvailable()
    {
        return TnPersistentManager.getInstance().getExternalStorageState() == TnPersistentManager.MEDIA_MOUNTED;
    }
    
    public void setOperateListener(IServerProxyListener listener)
    {
        this.mapDownloadOperationListener = listener;
    }
    
    public void deleteRegion(String regionId)
    {
        isDeleting = true;
        proxy.sendDeleteMapDownloadRequest(regionId);
    }
    
    public boolean isDeleting()
    {
        return isDeleting;
    }
    
    public void removeOperateListener()
    {
        this.mapDownloadOperationListener = null;
    }
}
