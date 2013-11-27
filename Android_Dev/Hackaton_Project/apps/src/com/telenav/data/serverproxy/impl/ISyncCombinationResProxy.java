/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ISyncCombinationResProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2011-2-23
 */
public interface ISyncCombinationResProxy
{

    public static final String SYNC_SERVER_DRIVEN_PARAMETER = "SERVER_DRIVEN_PARAMETER";
    
    public static final String SYNC_SERVICE_LOCATOR = "SERVICE_LOCATOR";
    
    public static final String SYNC_RESOURCE_FORMAT = "RESOURCE_FORMAT";
    
    public static final String SYNC_POI_CATEGORY_TREE = "POI_CATEGORY_TREE";
    
    public static final String SYNC_AIR_PROT = "AIR_PORT";
    
    public static final String SYNC_HOT_POI_CATEGORY = "HOT_POI_CATEGORY";
    
    public static final String SYNC_HOT_BRAND = "HOT_BRAND";
    
    public static final String SYNC_MAP_DATA_UPGRADE = "MAP_DATA_UPGRADE";
    
    public static final String SYNC_AUDIO_AND_RULE = "AUDIO_AND_RULE";
    
    public static final String SYNC_CAR_MODEL = "CAR_MODEL";
    
    public static final String SYNC_DSR_SERVER_DRIVEN_PARAMETER = "DSR_SDP";
    
    public static final String SYNC_PREFERENCE_SETTING = "CLIENT_SETTING";
    
    public static final String SYNC_REGION_CENTER_POINT = "REGION_CENTER_POINT";
    
    public static final String SYNC_REGION_MATRIX = "REGION_MATRIX";
    
    public static final String SYNC_GOBY_EVENT = "GOBY_EVENT_CATEGORY";
    
    public static final int TYPE_SERVICE_LOCATOR_INFO = 0;
    
    public static final int TYPE_SERVER_DRIVEN_PARAMETERS = 1;
    
    public static final int TYPE_RESOURCE_FORMAT = 2;
    
    public static final int TYPE_POI_BRAND_NAME = 3;
    
    public static final int TYPE_HOT_POI_CATEGORY = 4;
    
    public static final int TYPE_CATEGORY_TREE = 5;
    
    public static final int TYPE_AIRPORT = 6;
    
    public static final int TYPE_AUDIO_AND_RULE = 7;
    
    public static final int TYPE_MAP_DATA_UPGRADE = 8;
    
    public static final int TYPE_CAR_MODEL = 9;
    
    public static final int TYPE_DSR_SDP_NODE  = 10;
    
    public static final int TYPE_PREFERENCE_SETTING  = 11;
    
    public static final int TYPE_REGION_CENTER_POINT  = 12;
    
    public static final int TYPE_GOBY_EVENT  = 13;
    
    public static final int STEP_SYNC_START = 0;
    public static final int STEP_SYNC_FINISH = 1;
    
    /**
     * Sync resource according to the combination of requests
     * @param combination
     */
    public String syncCombinationRes(Vector combination);
    
    /**
     * 
     * @param combination
     * @param isNeedBackup
     */
    public String syncCombinationRes(Vector combination, boolean isNeedBackup);
    
    public int getStep();
    
    public void cleanupSyncResourceRequest();
}
