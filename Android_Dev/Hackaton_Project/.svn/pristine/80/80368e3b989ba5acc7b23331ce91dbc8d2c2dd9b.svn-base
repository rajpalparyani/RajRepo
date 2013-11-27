/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiMisLog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.data.datatypes.poi.Poi;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.AbstractMisLogHelper;
import com.telenav.logger.Logger;
import com.telenav.util.PrimitiveTypeCache;


/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-24
 */
public class PoiMisLog extends AbstractMisLog
{
    /**
     * The index of the corresponding poi in the poi results.
     */
    private int index;
    
    private Poi poi;
    
    private String brand;
    
    public PoiMisLog(String id, int eventType, int priority, AbstractMisLogHelper helper)
    {
        super(id, eventType, priority, helper);
    }
    
    public void processLog()
    {
        super.processLog();
        helper = null;
    }
    
    public void setIndex(int index)
    {
        this.index = index;
    }
    
    public int getIndex()
    {
        return this.index;
    }
    
    public void setPoi(Poi poi)
    {
        this.poi = poi;
    }
    
    public Poi getPoi()
    {
        return this.poi;
    }
    
    public void setAttribute(long attributeId, String value)
    {
        if (attributeId == IMisLogConstants.INNER_ATTR_POI_INDEX)
        {
            try
            {
                setIndex(Integer.valueOf(value).intValue());
            }
            catch (NumberFormatException ex)
            {
                Logger.log(this.getClass().getName(), new Exception("MIS for POI. Invalid Index"));
            }
        }
        else
        {
            super.setAttribute(PrimitiveTypeCache.valueOf(attributeId), value);
        }
    }

    public void setPoiId(String poiId)
    {
        this.setAttribute(ATTR_POI_ID, poiId);
    }

    public void setAdsId(String adsId)
    {
        this.setAttribute(ATTR_ADS_ID, adsId);
    }

    public void setAdsSource(String source)
    {
        this.setAttribute(ATTR_AD_SOURCE, source);
    }

    public void setSearchUid(String uid)
    {
        this.setAttribute(ATTR_SEARCH_UID, uid);
    }

    public void setPageName(String name)
    {
        this.setAttribute(ATTR_PAGE_NAME, name);
    }

    public void setPageIndex(int index)
    {
        this.setAttribute(ATTR_PAGE_INDEX, String.valueOf(index));
    }
    
    public void setPageNumber(int number)
    {
        this.setAttribute(ATTR_PAGE_NUMBER, String.valueOf(number));
    }

    public void setPageSize(int size)
    {
        this.setAttribute(ATTR_PAGE_SIZE, String.valueOf(size));
    }

    public void setPoiRating(int rating)
    {
        this.setAttribute(ATTR_POI_RATING, String.valueOf(rating));
    }

    public void setPoiDistance(String dis)
    {
        this.setAttribute(ATTR_POI_DISTANCE, dis);
    }

    public void setPoiSorting(String sorting)
    {
        this.setAttribute(ATTR_POI_SORTING, sorting);
    }

    public void setPoiType(String type)
    {
        this.setAttribute(ATTR_POI_TYPE, type);
    }
    
    public void setSearchType(String type)
    {
        this.setAttribute(ATTR_SEARCH_TYPE, type);
    }

    public void setElapsedTime(long elapsed)
    {
        this.setAttribute(ATTR_POI_ELAPSED_TIME, String.valueOf(elapsed));
    }
    
    public void setViewTime(long viewTime)
    {
        this.setAttribute(ATTR_VIEW_TIME, String.valueOf(viewTime));
    }

}
