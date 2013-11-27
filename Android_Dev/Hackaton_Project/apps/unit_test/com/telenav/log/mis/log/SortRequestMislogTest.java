/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * SortRequestMislogTest.java
 *
 */
package com.telenav.log.mis.log;

import junit.framework.TestCase;

import com.telenav.log.mis.IMisLogConstants;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2011-7-22
 */
public class SortRequestMislogTest extends TestCase
{
    private SortRequestMislog sortRequestMislog;

    public void setUp() throws Exception
    {
        String id = IMisLogConstants.TYPE_POI_SORT_REQUEST + "_" + System.currentTimeMillis();
        sortRequestMislog = new SortRequestMislog(id, IMisLogConstants.PRIORITY_3);
        super.setUp();
    }

    public void testSetSearchType()
    {
        sortRequestMislog.setResultType(IMisLogConstants.VALUE_POI_SEARCH_TYPE_POI);
        assertEquals(IMisLogConstants.VALUE_POI_SEARCH_TYPE_POI, sortRequestMislog.getAttribute(IMisLogConstants.ATTR_RESULT_TYPE));
    }


    public void testSetCategoryId()
    {
        sortRequestMislog.setCategoryId(10001);
        assertEquals(String.valueOf(10001), sortRequestMislog.getAttribute(IMisLogConstants.ATTR_CATEGORY_ID));
    }
    

    public void testSetPageNumber()
    {
    	sortRequestMislog.setPageNumber(15);
    	assertEquals("15", sortRequestMislog.getAttribute(IMisLogConstants.ATTR_PAGE_NUMBER));
    }

    public void testSetSearchKeyword()
    {
        sortRequestMislog.setSearchKeyword("startbucks");
        assertEquals("startbucks", sortRequestMislog.getAttribute(IMisLogConstants.ATTR_SEARCH_KEYWORD));
    }

    public void testSetPoiSorting()
    {
        sortRequestMislog.setPoiSorting(IMisLogConstants.VALUE_POI_SORT_BY_DISTANCE);
        assertEquals(IMisLogConstants.VALUE_POI_SORT_BY_DISTANCE, sortRequestMislog.getAttribute(IMisLogConstants.ATTR_POI_SORTING));
    }

    public void testSetDsrInitiatedFlag()
    {
        sortRequestMislog.setDsrInitiatedFlag(IMisLogConstants.VALUE_DSR_INITIATED_FLAG_FALSE);
        assertEquals(IMisLogConstants.VALUE_DSR_INITIATED_FLAG_FALSE, sortRequestMislog.getAttribute(IMisLogConstants.ATTR_DSR_INITIATED_FLAG));
    }
    
    public void testSetSearchUid()
    {
        String searchUid = System.currentTimeMillis() + "";
        sortRequestMislog.setSearchUid(searchUid);
        assertEquals(searchUid, sortRequestMislog.getAttribute(IMisLogConstants.ATTR_SEARCH_UID));
    }
    
    public void testSetResultNumber()
    {
        sortRequestMislog.setResultNumber(200);
        assertEquals(String.valueOf(200), sortRequestMislog.getAttribute(IMisLogConstants.ATTR_RESULT_NUMBER));
    }


}
