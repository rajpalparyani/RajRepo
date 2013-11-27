/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * RegionDetector.java
 *
 */
package com.telenav.module.region;

import com.telenav.app.CommManager;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.polygon.PolygonManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;

/**
 * @author chrbu
 * @modifier bduan
 * @date 2012-2-13
 */
public class RegionDetector implements IServerProxyListener
{
    public static final int SUCCESS = 0;

    public static final int FAIL = 1;
    
    
    protected IRegionDetectorListener listener = null;
    
    protected static final int RGC_TIMEOUT = 30 * 1000;
    
    /**
     * Constructor
     * @param listener listener for getting result.
     */
    public RegionDetector(IRegionDetectorListener listener)
    {
        this.listener = listener;
    }

    /**
     * If the location exists in a cached polygon, return the region name; else return empty.
     * 
     * @param tnLocation the location to check
     * @return the region if found in cache; "" if need to do RGC
     */
    public String detectRegion(TnLocation tnLocation)
    {
        if(tnLocation == null || !tnLocation.isValid())
        {
            return "";
        }
        
        String region = getRegionByPolygon(tnLocation);
        
        if (region == null || region.trim().length() == 0)
        {
            requestRGC(tnLocation);
        }
        else
        {
            return region;
        }

        return "";
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {

    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if (proxy instanceof IRgcProxy)
        {
            proxy.cancel();
            upadateRegion("", FAIL);
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IRgcProxy)
        {
            proxy.cancel();
            upadateRegion("", FAIL);
        }
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }

    public void transactionFinished(AbstractServerProxy serverproxy, String jobId)
    {
        if (serverproxy instanceof IRgcProxy && ((IRgcProxy) serverproxy).getAddress() != null)
        {
            IRgcProxy rgcProxy = (IRgcProxy) serverproxy;
            Stop mStop = rgcProxy.getAddress().getStop();
            if (mStop != null)
            {
                String region = mStop.getCountry();
                upadateRegion(region, SUCCESS);
            }
            else
            {
                upadateRegion("", FAIL);
            }
        }
    }
    
    /**
     * cancel the detecting act.
     */
    public static void cancelDetection()
    {
        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_RGC);
    }

    private void upadateRegion(String region, int resultType)
    {
        if (listener != null)
        {
            listener.onRegionDetected(region, resultType);
        }
    }
    
    private String getRegionByPolygon(TnLocation tnLocation)
    {
        String region = "";
        region = PolygonManager.getInstance().getRegion(tnLocation);
        return region;
    }
    
    private void requestRGC(TnLocation tnLocation)
    {
        if (tnLocation != null)
        {
            IRgcProxy proxy = (IRgcProxy) ServerProxyFactory.getInstance().createRgcProxy(
                null, CommManager.getInstance().getComm(), this);
            proxy.requestRgc(tnLocation.getLatitude(), tnLocation.getLongitude(), 0, RGC_TIMEOUT);
        }
    }
    
    public interface IRegionDetectorListener
    {
        void onRegionDetected(String region,int resultType);
    }
}
