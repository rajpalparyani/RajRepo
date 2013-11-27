/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * RegionUtil.java
 *
 */
package com.telenav.module.region;

import java.util.Enumeration;
import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.module.AppConfigHelper;

/**
 * @author chrbu
 * @modifier bduan
 * @date 2012-2-16
 */
public class RegionUtil
{
    protected String targetRegion = "";
    
    protected IRegionChangeListener regionChangeListener = null;
    
    private static final String EMPTY_REGION = "";
    
    private static final int REGION_CODE_LENGTH = 2;
    
    
    private static class RegionUtilInstance
    {
        static RegionUtil instance = new RegionUtil();
    }
    
    private RegionUtil()
    {
    }

    public static RegionUtil getInstance()
    {
        return RegionUtilInstance.instance;
    }

    public String getCurrentRegion()
    {
        return DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region;
    }

    public String getdefaultRegion()
    {
        return AppConfigHelper.defaultRegion;
    }
    
    public void setRegionChangeListener(IRegionChangeListener regionChangeListener)
    {
        this.regionChangeListener = regionChangeListener;
    }
    
    public boolean isNewRegion(String newDetectedRegion)
    {
        return !getCurrentRegion().equalsIgnoreCase(newDetectedRegion);
    }

    public void setCurrentRegion(String region)
    {
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().region = region;
        DaoManager.getInstance().getMandatoryNodeDao().store();
    }

    public boolean isRegionCached(String region)
    {
        if(!isRegionSupported(region))
        {
            return false;
        }
        
        boolean isCached = false;
        Vector<String> regions = DaoManager.getInstance().getSimpleConfigDao()
                .getVector(SimpleConfigDao.KEY_CACHED_REGION);
        int size = regions.size();
        if (regions != null && size > 0)
        {
            for (int index = 0; index < size; index++)
            {
                String tmpRegion = regions.get(index);
                if (tmpRegion != null && tmpRegion.equalsIgnoreCase(region))
                {
                    isCached = true;
                    break;
                }
            }
        }
        return isCached;
    }

    public boolean isRegionSupported(String region)
    {
        boolean isSupported = false;
        
        if(!isLegalRegion(region))
        {
            return false;
        }
        
        StringMap regionMaps = DaoManager.getInstance().getBillingAccountDao()
                .getSupportedRegion();
        if (regionMaps != null)
        {
            Enumeration<String>keys = regionMaps.keys();
            while (keys.hasMoreElements())
            {
                String key = keys.nextElement();
                if (key.equalsIgnoreCase(region))
                {
                    isSupported = true;
                    break;
                }

            }
        }
        
        return isSupported;
    }

    public void setTargetRegion(String region)
    {
        if(isRegionSupported(region))
        {
            this.targetRegion = region;
            if(regionChangeListener != null)
            {
                regionChangeListener.regionChanged(region);
            }
        }
    }
    
    public String getTargetRegion()
    {
        return this.targetRegion;
    }
    
    public void clearTargetRegion(String region)
    {
        if(region != null && region.equals(this.targetRegion))
        {
            this.targetRegion = "";
        }
    }
    
    public boolean isLegalRegion(String region)
    {
        boolean isLegal = false;
        if (region != null && !EMPTY_REGION.equalsIgnoreCase(region) && region.trim().length() == REGION_CODE_LENGTH)
            isLegal = true;
        return isLegal;
    }
}
