/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * IBillBoardAdsProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.location.TnLocation;

/**
 *@author zhdong@telenav.cn
 *@date 2011-3-21
 */
public interface IBillBoardAdsProxy
{
    public String requestBillBoardAds(long routeId, TnLocation[] gpsFixes, int fixCount);

    /**
     * Each ad is type of com.telenav.data.datatypes.poi.BillboardAd
     * 
     * @return
     */
    public Vector getBillboardAds();
}
