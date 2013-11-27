/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * NavAdHelper.java
 *
 */
package com.telenav.module.nav;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.poi.BillboardAd;
import com.telenav.data.datatypes.poi.GeoFence;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationProvider;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;

/**
 * 
 * The NavAdManager manages virtual bill board during navigation.
 * 
 * It will do these tasks.
 * 
 * 1) It will poll AD along route timely
 * 2) It will choose the best suitable AD for displaying
 * 
 *@author zhdong@telenav.cn
 *@date 2011-3-8
 */
public class NavAdManager implements INotifierListener ,IServerProxyListener
{
    private static NavAdManager helper = new NavAdManager();
    
    long pollingInterval = 180000;

    NavSdkNavigationProxy billBoardAdsProxy;
    
    private NavAdManager()
    {
    }

    public static NavAdManager getInstance()
    {
        return helper;
    }

    public void start()
    {
        lastNotifyTimestamp = 0L;
        pollingInterval = DaoManager.getInstance().getServerDrivenParamsDao().getIntValue(
            ServerDrivenParamsDao.BILLBOARD_AD_POLLING_INTERVAL) * 1000;
        if (pollingInterval > 0)
        {
            Notifier.getInstance().addListener(this);
        }
    }

    public void stop()
    {
        Notifier.getInstance().removeListener(this);
    }

    /**
     * Get current AD for display. <br>
     * 
     * If there is no any AD or the current AD is out of date, we return null.<br>
     * 
     * @return
     */
    public BillboardAd getCurrentAd()
    {
        // return "http://172.16.10.96:8080/poi_service/html/adsinfo.do?adsId=1";

        if(DaoManager.getInstance().getBillingAccountDao().isPremiumAccount())
        {
            return null;
        }
        
        if (billBoardAdsProxy != null)
        {
            Vector<BillboardAd> ads = billBoardAdsProxy.getBillboardAds();
            TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS);

            if (ads != null && location != null)
            {
                int lat = location.getLatitude();
                int lon = location.getLongitude();

                for (int i = 0; i < ads.size(); i++)
                {
                    BillboardAd ad = ads.elementAt(i);
                    GeoFence geoFence = ad.getGeoFence();

                    if (!ad.isExpired() && geoFence != null)
                    {
                        int adLat = geoFence.getLat();
                        int adLon = geoFence.getLon();
                        double distance = geoFence.getDistance();
                        int cosLat = DataUtil.getCosLat(adLat);
                        int currentDistance = DataUtil.gpsDistance(adLat - lat, adLon - lon, cosLat);

                        // 1 dm5 is around 1.19M, so ...
                        if (currentDistance <= distance)
                        {
                            return ad;
                        }
                    }
                }
            }
        }

        return null;
    }

    long lastNotifyTimestamp;

    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    public long getNotifyInterval()
    {
        return pollingInterval;
    }

    public void notify(long timestamp)
    {
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        
        if (!isOnboard)
        {
            billBoardAdsProxy = new NavSdkNavigationProxy(this);
            billBoardAdsProxy.requestBillBoardAds(NavSdkRouteWrapper.getInstance().getCurrentRouteId() + "");
        }
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestamp = timestamp;
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return NavSdkNavEngine.getInstance().isRunning();
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
    }

    public void transactionError(AbstractServerProxy proxy)
    {
    }


    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
    }


    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
    }
    
    public void logImpressionStart(BillboardAd billboardAd)
    {
        int state = billboardAd.getState();
        AbstractMisLog log = null;
        switch (state)
        {
            case BillboardAd.STATE_INITIAL_VIEW:
            {
                if (!billboardAd.isInitialViewStopped())
                {
                    log = MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_START);
                }
                break;
            }
            case BillboardAd.STATE_DETAIL_VIEW:
            {
                if (!billboardAd.isDetailViewStopped())
                {
                    log = MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_START);
                }
                break;
            }
            case BillboardAd.STATE_POI_VIEW:
            {
                if (!billboardAd.isPoiViewStopped())
                {
                    log = MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_START);
                }
                break;
            }
        }
        if (log != null)
        {
            log.setAttribute(IMisLogConstants.ATTR_ADS_ID, billboardAd.getAdsId() + "");
            log.setAttribute(IMisLogConstants.ATTR_AD_SOURCE, billboardAd.getAdsSource() + "");
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }        
    }

    public void logImpressionEnd(BillboardAd billboardAd, String exitType)
    {
        int state = billboardAd.getState();
        AbstractMisLog log = null;
        switch (state)
        {
            case BillboardAd.STATE_INITIAL_VIEW:
            {
                if (!billboardAd.isInitialViewStopped())
                {
                    log = MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_END);
                }
                break;
            }
            case BillboardAd.STATE_DETAIL_VIEW:
            {
                if (!billboardAd.isDetailViewStopped())
                {
                    log = MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_END);
                }
                break;
            }
            case BillboardAd.STATE_POI_VIEW:
            {
                if (!billboardAd.isPoiViewStopped())
                {
                    log = MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_END);
                }
                break;
            }
        }
        if (log != null)
        {
            log.setAttribute(IMisLogConstants.ATTR_ADS_ID, billboardAd.getAdsId() + "");
            log.setAttribute(IMisLogConstants.ATTR_AD_SOURCE, billboardAd.getAdsSource() + "");
            log.setAttribute(IMisLogConstants.ATTR_BILLBOARD_EXIT_TYPE, exitType);
            log.setAttribute(IMisLogConstants.ATTR_BILLBOARD_ELAPSED_TIME, billboardAd.getElapsedTime() + "");
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }
    }

}
