/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * BillboardAd.java
 *
 */
package com.telenav.data.datatypes.poi;

import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;

/**
 *@author zhdong@telenav.cn
 *@date 2011-3-21
 */
public class BillboardAd
{
    
    public static final int STATE_INITIAL_VIEW = 1;

    public static final int STATE_DETAIL_VIEW = 2;

    public static final int STATE_POI_VIEW = 3;

    int state;
    
    long adsId;
    String adsSource;
    long detailViewTime;
    long expirationTime;
    long initialViewTime;
    long poiViewTime;
    
    long constructStartTime;
    
    long lastOperationTime;
    boolean isEnded;
    boolean isInitialViewStopped;
    boolean isDetailViewStopped;
    boolean isPoiViewStopped;

    GeoFence geoFence;
    
    TnUiArgAdapter initHeightAdapter;
    TnUiArgAdapter contentHeightAdapter;
    
    String url = "";
    
    public BillboardAd()
    {
        constructStartTime = System.currentTimeMillis();
    }

    public boolean isInitialViewStopped()
    {
        return isInitialViewStopped;
    }

    public boolean isDetailViewStopped()
    {
        return isDetailViewStopped;
    }
    
    public boolean isPoiViewStopped()
    {
        return isPoiViewStopped;
    }
    
    public boolean isExpired()
    {
        return (System.currentTimeMillis() - constructStartTime) > expirationTime;
    }
    
    /**
     * State should be one of STATE_INITIAL_VIEW,STATE_DETAIL_VIEW,STATE_POI_VIEW
     * 
     * @param state
     */
    public void updateState(int state)
    {
        this.lastOperationTime = System.currentTimeMillis();
        int prevState = this.state;
        this.state = state;
        switch (state)
        {
            case STATE_INITIAL_VIEW:
            {
                if (prevState == STATE_DETAIL_VIEW)
                {
                    isDetailViewStopped = true;
                }
                else if (prevState == STATE_POI_VIEW)
                {
                    isPoiViewStopped = true;
                }
                break;
            }
            case STATE_DETAIL_VIEW:
            {
                isInitialViewStopped = true;
                if (prevState == STATE_POI_VIEW)
                {
                    isPoiViewStopped = true;
                }
                break;
            }
            case STATE_POI_VIEW:
            {
                isInitialViewStopped = true;
                isDetailViewStopped = true;
                break;
            }
        }

    }
    
    public long getElapsedTime()
    {
        return System.currentTimeMillis() - lastOperationTime;
    }
    
    public int getState()
    {
        return state;
    }

    public long getAdsId()
    {
        return adsId;
    }

    public void setAdsId(long adsId)
    {
        this.adsId = adsId;
    }
    
    public void setDetailViewTime(long detailViewTime)
    {
        this.detailViewTime = detailViewTime;
    }

    /**
     * The start time of expirationTime should be the moment we got AD from server.
     * 
     * @param expirationTime
     */
    public void setExpirationTime(long expirationTime)
    {
        this.expirationTime = expirationTime;
    }

    public void setInitialViewTime(long initialViewTime)
    {
        this.initialViewTime = initialViewTime;
    }

    public void setPoiViewTime(long poiViewTime)
    {
        this.poiViewTime = poiViewTime;
    }

    public GeoFence getGeoFence()
    {
        return geoFence;
    }

    public void setGeoFence(GeoFence geoFence)
    {
        this.geoFence = geoFence;
    }

    /**
     * Any time the AD is ended, we will not show it again
     * 
     * @return
     */
    public boolean isEnded()
    {
        return isEnded;
    }
    
    public void setEnded(boolean isEnded)
    {
        this.isEnded = isEnded;
    }
    
    public String getAdsSource()
    {
        return adsSource;
    }

    public void setAdsSource(String adsSource)
    {
        this.adsSource = adsSource;
    }
    
    public boolean isDisplayTimeout()
    {
        long now = System.currentTimeMillis();
        switch (state)
        {
            case STATE_INITIAL_VIEW:
            {
                return now - lastOperationTime > initialViewTime;
            }
            case STATE_DETAIL_VIEW:
            {
                return now - lastOperationTime > detailViewTime;
            }
            case STATE_POI_VIEW:
            {
                return now - lastOperationTime > poiViewTime;
            }
        }

        return false;
    }
    
    public void setInitHeight(TnUiArgAdapter initHeightAdapter)
    {
        this.initHeightAdapter = initHeightAdapter;
    }
    
    public void setContentHeight(TnUiArgAdapter contentHeightAdapter)
    {
        this.contentHeightAdapter = contentHeightAdapter;
    }
    
    public int getInitHeight()
    {
        if(initHeightAdapter != null)
        {
            return initHeightAdapter.getInt();
        }
        
        return 0;
    }
    
    public int getContentHeight()
    {
        if(contentHeightAdapter != null)
        {
            return contentHeightAdapter.getInt();
        }
        
        return 0;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getUrl()
    {
        return url;
    }
}
