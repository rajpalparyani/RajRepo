/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * FeaturesManager.java
 *
 */
package com.telenav.module.upsell;

import java.util.Enumeration;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.primitive.StringMap;

/**
 *@author bduan
 *@date 2011-2-23
 */
public class FeaturesManager
{
    public final static String APP_CODE = "Navigation";
    
    
    public final static String FEATURE_CODE_MAP = "FE_MAPS"; //basic map view.
    
    public final static String FEATURE_CODE_DSR = "FE_DSR"; //DSR feature
    
    public final static String FEATURE_CODE_DYNAMIC_ROUTE = "FE_DYNAMIC_ROUTE"; // request dynamic route
    
    public final static String FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW = "FE_MAP_LAYER_TRAFFIC_FLOW";
    
    public final static String FEATURE_CODE_MAP_LAYER_CAMERA = "FE_MAP_LAYER_CAMERA";
    
    public final static String FEATURE_CODE_MAP_LAYER_SATELLITE = "FE_MAP_LAYER_SATELLITE";
    
    public final static String FEATURE_CODE_NAV_SPEED_LIMIT = "FE_NAV_SPEED_LIMIT";
    
    public final static String FEATURE_CODE_ROUTE_PLANNING_MULTI_ROUTE = "FE_ROUTE_PLANNING_MULTI_ROUTE";
    
    public final static String FEATURE_CODE_NAV_LANE_ASSIST = "FE_NAV_LANE_ASSIST";
    
    public final static String FEATURE_CODE_NAV_JUNCTION_VIEW = "FE_NAV_JUNCTION_VIEW";
    
    public final static String FEATURE_CODE_TRAFFIC = "FE_TRAFFIC";
    
    public final static String FEATURE_CODE_NAV_SPEED_TRAP = "FE_NAV_SPEED_TRAP";
    
    public final static String FEATURE_CODE_NAV_TRAFFIC_CAMERA = "FE_NAV_TRAFFIC_CAMERA";
    
    public final static String FEATURE_CODE_CAR_CONNECT = "FE_CAR_CONNECT";
    
    public final static String FEATURE_CODE_TRAFFIC_INCIDENT = "FE_TRAFFIC_INCIDENT";
    
    public final static String FEATURE_CODE_VISUAL_NAVIGATION_AUDIO = "FE_VISUAL_NAVIGATION_AUDIO";
    
    public final static String FEATURE_CODE_HYBRID = "FE_HYBRID";
    
    public final static String FEATURE_CODE_LAYER_TRAFFIC_INCIDENT = "FE_MAP_LAYER_TRAFFIC_INCIDENT";
    
    public final static String FEATURE_CODE_VISUAL_NAVIGATION_AUTO_REROUTE = "FE_VISUAL_NAVIGATION_AUTO_REROUTE";
    
    public final static String FEATURE_CODE_VISUAL_NAVIGATION_DYNAMIC_GUIDANCE = "FE_VISUAL_NAVIGATION_DYNAMIC_GUIDANCE";
    
    public final static String FEATURE_CODE_MAP_DOWNLOAD_PATH_AVAILABLE = "FE_MAP_DATA_PATH";
    
    public static final int FE_DISABLED = 0;
    public static final int FE_UNPURCHASED = 1;
    public static final int FE_PURCHASED = 2;
    public static final int FE_ENABLED = 3;
    
    private static class InnerFeaturesManager
    {
        static FeaturesManager featuresManager = new FeaturesManager();
    }
    
    private FeaturesManager()
    {
        
    }
    
    public static FeaturesManager getInstance() 
    {
        return InnerFeaturesManager.featuresManager;
    }
    
    
    public int getStatus(String featureName)
    {
        try
        {
            String value = ((DaoManager) DaoManager.getInstance()).getUpsellDao().getValue(featureName);
            if (value != null && value.length() > 0)
                return Integer.valueOf(value);
        }
        //If not found in feature, it is disabled by default
        catch (Exception e)
        {
            return FE_DISABLED;
        }
        return FE_DISABLED;
    }
    
    
    public int getStatus(String featureName, String region)
    {
        try
        {
            String value = ((DaoManager) DaoManager.getInstance()).getUpsellDao().getValue(featureName);
            if (value != null && value.length() > 0)
                return Integer.valueOf(value);
           
        }
        catch (Exception e)
        {
            return FE_DISABLED;
        }
        return FE_DISABLED;
    }
    
    public boolean isAllFeaturePurchased()
    {
        boolean isAllFeaturePurchased = false;
        StringMap featureMapping = ((DaoManager) DaoManager.getInstance()).getUpsellDao().getFeatureList();
        if (featureMapping != null)
        {
            Enumeration keyEnum = featureMapping.keys();
            while (keyEnum.hasMoreElements())
            {
                String featureName = (String) keyEnum.nextElement();
                int featureValue = FeaturesManager.getInstance().getStatus(featureName);
                if (featureValue == FeaturesManager.FE_PURCHASED)
                {
                    isAllFeaturePurchased = true;
                }
                else
                {
                    isAllFeaturePurchased = false;
                    break;
                }

            }
        }
        return isAllFeaturePurchased;
    }
}
