/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SortRequestMislog.java
 *
 */
package com.telenav.log.mis.log;

import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.poi.PoiDataWrapper;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-21
 */
public class SortRequestMislog extends AbstractMisLog
{
    private PoiDataWrapper poiDataWrapper;
    
    public SortRequestMislog(String id, int priority)
    {
        super(id, TYPE_POI_SORT_REQUEST, priority);
    }
    
    public void setDatawrapper(PoiDataWrapper poiDataWrapper)
    {
        this.poiDataWrapper = poiDataWrapper;
    }
    
    public void processLog()
    {
        super.processLog();
        this.setResultType(IMisLogConstants.VALUE_POI_SEARCH_TYPE_POI);
        this.setCategoryId(poiDataWrapper.getCategoryId());
        this.setSearchKeyword(poiDataWrapper.getQueryWord());
        this.setPageNumber(0);
        this.setPoiSorting(getPoiSorting());
        this.setDsrInitiatedFlag(checkDsrInitiatedFlag());
        this.setResultNumber(poiDataWrapper.getTotalCountServerReturned());
        this.setSearchUid(poiDataWrapper.getSearchUid());
        this.poiDataWrapper = null;
    }
    
    public String checkDsrInitiatedFlag()
    {
        if (poiDataWrapper.getSearchFromType() == IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN
                || poiDataWrapper.getSearchFromType() == IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN_ALONG)
        {
            return IMisLogConstants.VALUE_DSR_INITIATED_FLAG_TRUE;
        }
        else
        {
            return IMisLogConstants.VALUE_DSR_INITIATED_FLAG_FALSE;
        }
    }
    
    public String getPoiSorting()
    {
        return PoiMisLogHelper.transformPoiSortType(poiDataWrapper.getSortType());
    }
    
    public void setResultType(String type)
    {
        this.setAttribute(ATTR_RESULT_TYPE, type);
    }
    
    public void setCategoryId(int id)
    {
        this.setAttribute(ATTR_CATEGORY_ID, String.valueOf(id));
    }
    
    public void setPageNumber(int number)
    {
        this.setAttribute(ATTR_PAGE_NUMBER, String.valueOf(number));
    }
    
    public void setSearchKeyword(String keyword)
    {
        this.setAttribute(ATTR_SEARCH_KEYWORD, keyword);
    }
    
    public void setPoiSorting(String sorting)
    {
        this.setAttribute(ATTR_POI_SORTING, sorting);
    }
    
    public void setDsrInitiatedFlag(String type)
    {
        this.setAttribute(ATTR_DSR_INITIATED_FLAG, type);
    }
    
    public void setSearchUid(String uid)
    {
        this.setAttribute(ATTR_SEARCH_UID, uid);
    }
    
    public void setResultNumber(int number)
    {
        this.setAttribute(ATTR_RESULT_NUMBER, String.valueOf(number));
    }
    
}
