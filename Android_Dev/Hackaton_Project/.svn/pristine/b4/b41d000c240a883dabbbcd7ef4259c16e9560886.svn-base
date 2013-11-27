/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiMisLogHelperTest.java
 *
 */
package com.telenav.log.mis.helper;

import java.util.Hashtable;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.MisLogDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.poi.Ad;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogFactory;
import com.telenav.log.mis.MisLogFilter;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.log.mis.log.PoiMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.persistent.TnStoreMock;
import com.telenav.res.ResourceManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yren (yren@telenav.cn)
 *@date 2011-11-4
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.telenav.module.AppConfigHelper")
@PrepareForTest({Logger.class, AbstractDaoManager.class,DaoManager.class,MisLogManager.class,PoiMisLogHelper.class})
public class PoiMisLogHelperTest extends TestCase
{
    PoiDataWrapper mockDataWrapper;
    PoiMisLogHelper poiMisLogHelper;
    DaoManager daoManager;
    
    protected void setUp() //throws Exception
    {        
        mockDataWrapper = PowerMock.createMock(PoiDataWrapper.class);
        
        PowerMock.mockStatic(AbstractDaoManager.class);
        daoManager = PowerMock.createPartialMock(DaoManager.class, "getInstance", "getMisLogDao" , "getPreferenceDao");
        EasyMock.expect(AbstractDaoManager.getInstance()).andReturn(daoManager).anyTimes();
    }
   
    public void testNewInstance()
    {
        PoiMisLogHelper instance = PoiMisLogHelper.newInstance(mockDataWrapper);
        assertTrue(instance instanceof PoiMisLogHelper);
    }
    
    public void testIsLogEnable_CASE1()
    {
        poiMisLogHelper = new PoiMisLogHelper(null);
        PoiMisLog mockLog = PowerMock.createMock(PoiMisLog.class);
        EasyMock.expect(mockLog.getPoi()).andReturn(null);
        EasyMock.expect(mockLog.getIndex()).andReturn(0);
        PowerMock.replayAll();
        assertFalse(poiMisLogHelper.isLogEnable(mockLog)); 
        PowerMock.verifyAll();
    }
    
    public void testIsLogEnable_CASE2()
    {
        PowerMock.resetAll();
        PoiMisLog mockLog = PowerMock.createMock(PoiMisLog.class);
        Address mockAddress = PowerMock.createMock(Address.class);
        
        EasyMock.expect(mockLog.getPoi()).andReturn(null);
        EasyMock.expect(mockLog.getIndex()).andReturn(0).times(2);
        EasyMock.expect(mockAddress.getPoi()).andReturn(null);
        EasyMock.expect(mockDataWrapper.getAddress(0)).andReturn(mockAddress);
        
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PowerMock.replayAll();
        assertFalse(poiMisLogHelper.isLogEnable(mockLog)); 
        PowerMock.verifyAll();
    }
    
    public void testIsLogEnable_CASE3()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PoiMisLog mockLog = PowerMock.createMock(PoiMisLog.class);
        EasyMock.expect(mockLog.getPoi()).andReturn(null);
        EasyMock.expect(mockLog.getIndex()).andReturn(0).times(2);
        EasyMock.expect(mockDataWrapper.getAddress(0)).andReturn(null);
        PowerMock.replayAll();
        assertFalse(poiMisLogHelper.isLogEnable(mockLog)); 
        PowerMock.verifyAll();
    }
    
    public void testIsLogEnable_CASE4()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PoiMisLog mockLog = PowerMock.createMock(PoiMisLog.class);
        Address address = PowerMock.createMock(Address.class);
        Poi mockPoi = PowerMock.createMock(Poi.class);

        EasyMock.expect(mockLog.getPoi()).andReturn(null);
        EasyMock.expect(mockLog.getIndex()).andReturn(0).times(2);
        EasyMock.expect(mockDataWrapper.getAddress(0)).andReturn(address);
        EasyMock.expect(address.getPoi()).andReturn(mockPoi);
        PowerMock.replayAll();
        assertTrue(poiMisLogHelper.isLogEnable(mockLog)); 
        PowerMock.verifyAll();
    }

    public void testIsLogEnable_CASE5()
    {
        poiMisLogHelper = new PoiMisLogHelper(null);
        PoiMisLog mockLog = PowerMock.createMock(PoiMisLog.class);
        Poi mockPoi = PowerMock.createMock(Poi.class);
        EasyMock.expect(mockLog.getPoi()).andReturn(mockPoi);
        PowerMock.replayAll();
        assertTrue(poiMisLogHelper.isLogEnable(mockLog)); 
        PowerMock.verifyAll();
    }
    
    
    public void testFillAttrbute_Poi_Is_Null()
    {
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        EasyMock.expect(mockMisLog.getPoi()).andReturn(null);
        int poiIndex = 0;
        EasyMock.expect(mockMisLog.getIndex()).andReturn(poiIndex).anyTimes();
        Poi mockPoi = null;
        Address address = PowerMock.createMock(Address.class);
        EasyMock.expect(address.getPoi()).andReturn(mockPoi).anyTimes();
        EasyMock.expect(mockDataWrapper.getSortType()).andReturn(IPoiSearchProxy.TYPE_SORT_BY_DISTANCE).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageIndex(poiIndex)).andReturn(0).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageSizeWithAds()).andReturn(0).anyTimes();
        EasyMock.expect(mockDataWrapper.getSearchUid()).andReturn("ddd").anyTimes();
        EasyMock.expect(mockDataWrapper.getAddress(poiIndex)).andReturn(address);
        PowerMock.replay(mockDataWrapper);
        mockMisLog.setSearchUid(mockDataWrapper.getSearchUid());
        mockMisLog.setPageName(IMisLogConstants.VALUE_POI_PAGE_NAME_DETAIL);
        mockMisLog.setPageIndex(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageNumber(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageSize(mockDataWrapper.getPageSizeWithAds());
        mockMisLog.setPoiSorting(IMisLogConstants.VALUE_POI_SORT_BY_DISTANCE);
        
        PowerMock.replayAll();
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        poiMisLogHelper.fillAttrbute(mockMisLog);
        PowerMock.verifyAll();        
    }
    
    public void testFillAttrbute_Poi_Type_Is_TYPE_SPONSOR_POI()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        int poiIndex = 0;
        
        EasyMock.expect(mockMisLog.getPoi()).andReturn(null);

        EasyMock.expect(mockMisLog.getIndex()).andReturn(poiIndex).anyTimes();
        Poi mockPoi = PowerMock.createPartialMock(Poi.class, "getAd", "getBizPoi", "getRating","getType");
        Address address = PowerMock.createMock(Address.class);
        Ad mockAd = PowerMock.createPartialMock(Ad.class,"getAdID","getAdSource");
        BizPoi mockBizPoi = PowerMock.createMock(BizPoi.class);
        
        EasyMock.expect(mockDataWrapper.getSortType()).andReturn(IPoiSearchProxy.TYPE_SORT_BY_DISTANCE).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageIndex(poiIndex)).andReturn(0).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageSizeWithAds()).andReturn(0).anyTimes();
  
        
        EasyMock.expect(address.getPoi()).andReturn(mockPoi).anyTimes();
        EasyMock.expect(mockDataWrapper.getAddress(poiIndex)).andReturn(address);
        EasyMock.expect(mockPoi.getAd()).andReturn(mockAd).anyTimes();
        EasyMock.expect(mockPoi.getBizPoi()).andReturn(mockBizPoi).anyTimes();
        EasyMock.expect(mockPoi.getRating()).andReturn(5).anyTimes();
        EasyMock.expect(mockPoi.getType()).andReturn(Poi.TYPE_SPONSOR_POI).anyTimes();
        EasyMock.expect(mockAd.getAdID()).andReturn("aaa").anyTimes();
        EasyMock.expect(mockAd.getAdSource()).andReturn("bbb").anyTimes();
        EasyMock.expect(mockBizPoi.getPoiId()).andReturn("ccc").anyTimes();
        EasyMock.expect(mockBizPoi.getDistance()).andReturn("1000").anyTimes();
        EasyMock.expect(mockDataWrapper.getSearchUid()).andReturn("ddd").anyTimes();
        PowerMock.replay(mockBizPoi);
        
        int distInt = Integer.parseInt(mockBizPoi.getDistance());
        
        PowerMock.replay(mockAd);
        mockMisLog.setAdsId(mockAd.getAdID());
        mockMisLog.setAdsSource(mockAd.getAdSource());

        PowerMock.replay(mockDataWrapper);
        PowerMock.replay(mockPoi);
        
        mockMisLog.setPoiId(mockPoi.getBizPoi().getPoiId());
        mockMisLog.setSearchUid(mockDataWrapper.getSearchUid());
        mockMisLog.setPoiRating(mockPoi.getRating());
        
        PreferenceDao mockPreferenceDao = PowerMock.createPartialMock(PreferenceDao.class,"getPreference");
        EasyMock.expect(daoManager.getPreferenceDao()).andReturn(mockPreferenceDao).anyTimes();
        
        Preference mockPreference = PowerMock.createPartialMock(Preference.class, "getIntValue");
        EasyMock.expect(mockPreferenceDao.getPreference(Preference.ID_PREFERENCE_DISTANCEUNIT)).andReturn(mockPreference).anyTimes();
        
        int distanceUnitValue = 100;
        EasyMock.expect(mockPreference.getIntValue()).andReturn(distanceUnitValue);
        
        String distStr = ResourceManager.getInstance().getStringConverter().convertDistance(distInt, distanceUnitValue);
        mockMisLog.setPoiDistance(distStr);
        mockMisLog.setPoiSorting(poiMisLogHelper.getPoiSorting());
        
        mockMisLog.setPageName(IMisLogConstants.VALUE_POI_PAGE_NAME_DETAIL);
        mockMisLog.setPageIndex(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageNumber(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageSize(mockDataWrapper.getPageSizeWithAds());
        
        mockMisLog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_SPOSORED);
        
        PowerMock.replayAll();
        poiMisLogHelper.fillAttrbute(mockMisLog);
        PowerMock.verifyAll();
    }
    
    public void testFillAttrbute_Poi_Type_Is_AdsPoi_True()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        int poiIndex = 0;
        
        EasyMock.expect(mockMisLog.getPoi()).andReturn(null);
        EasyMock.expect(mockMisLog.getIndex()).andReturn(poiIndex).anyTimes();
        Poi mockPoi = PowerMock.createPartialMock(Poi.class, "getAd", "getBizPoi", "getRating","getType","isAdsPoi");
        Address address = PowerMock.createMock(Address.class);
        Ad mockAd = PowerMock.createPartialMock(Ad.class,"getAdID","getAdSource");
        BizPoi mockBizPoi = PowerMock.createMock(BizPoi.class);
        
        EasyMock.expect(mockDataWrapper.getSortType()).andReturn(IPoiSearchProxy.TYPE_SORT_BY_DISTANCE).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageIndex(poiIndex)).andReturn(0).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageSizeWithAds()).andReturn(0).anyTimes();
        
        EasyMock.expect(address.getPoi()).andReturn(mockPoi).anyTimes();
        EasyMock.expect(mockDataWrapper.getAddress(poiIndex)).andReturn(address);
        EasyMock.expect(mockPoi.getAd()).andReturn(mockAd).anyTimes();
        EasyMock.expect(mockPoi.getBizPoi()).andReturn(mockBizPoi).anyTimes();
        EasyMock.expect(mockPoi.getRating()).andReturn(5).anyTimes();
        EasyMock.expect(mockPoi.getType()).andReturn(Poi.TYPE_POI).anyTimes();
        EasyMock.expect(mockPoi.isAdsPoi()).andReturn(true).anyTimes();
        EasyMock.expect(mockAd.getAdID()).andReturn("aaa").anyTimes();
        EasyMock.expect(mockAd.getAdSource()).andReturn("bbb").anyTimes();
        EasyMock.expect(mockBizPoi.getPoiId()).andReturn("ccc").anyTimes();
        EasyMock.expect(mockBizPoi.getDistance()).andReturn("1000").anyTimes();
        EasyMock.expect(mockDataWrapper.getSearchUid()).andReturn("ddd").anyTimes();
        PowerMock.replay(mockBizPoi);
        
        int distInt = Integer.parseInt(mockBizPoi.getDistance());
        
        PowerMock.replay(mockAd);
        mockMisLog.setAdsId(mockAd.getAdID());
        mockMisLog.setAdsSource(mockAd.getAdSource());

        PowerMock.replay(mockDataWrapper);
        PowerMock.replay(mockPoi);
        
        mockMisLog.setPoiId(mockPoi.getBizPoi().getPoiId());
        mockMisLog.setSearchUid(mockDataWrapper.getSearchUid());
        mockMisLog.setPoiRating(mockPoi.getRating());
        
        PreferenceDao mockPreferenceDao = PowerMock.createPartialMock(PreferenceDao.class,"getPreference");
        EasyMock.expect(daoManager.getPreferenceDao()).andReturn(mockPreferenceDao).anyTimes();
        
        Preference mockPreference = PowerMock.createPartialMock(Preference.class, "getIntValue");
        EasyMock.expect(mockPreferenceDao.getPreference(Preference.ID_PREFERENCE_DISTANCEUNIT)).andReturn(mockPreference).anyTimes();
        
        int distanceUnitValue = 100;
        EasyMock.expect(mockPreference.getIntValue()).andReturn(distanceUnitValue);
        
        String distStr = ResourceManager.getInstance().getStringConverter().convertDistance(distInt, distanceUnitValue);
        mockMisLog.setPoiDistance(distStr);
        mockMisLog.setPoiSorting(poiMisLogHelper.getPoiSorting());
        
        mockMisLog.setPageName(IMisLogConstants.VALUE_POI_PAGE_NAME_DETAIL);
        mockMisLog.setPageIndex(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageNumber(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageSize(mockDataWrapper.getPageSizeWithAds());
        
        mockMisLog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_WITH_ADD);
        
        PowerMock.replayAll();
        poiMisLogHelper.fillAttrbute(mockMisLog);
        PowerMock.verifyAll();
    }
    
    public void testFillAttrbute_Poi_Type_Is_AdsPoi_False()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        int poiIndex = 0;
        
        EasyMock.expect(mockMisLog.getPoi()).andReturn(null);
        EasyMock.expect(mockMisLog.getIndex()).andReturn(poiIndex).anyTimes();
        Poi mockPoi = PowerMock.createPartialMock(Poi.class, "getAd", "getBizPoi", "getRating","getType","isAdsPoi");
        Address address = PowerMock.createMock(Address.class);
        Ad mockAd = PowerMock.createPartialMock(Ad.class,"getAdID","getAdSource");
        BizPoi mockBizPoi = PowerMock.createMock(BizPoi.class);
        
        EasyMock.expect(mockDataWrapper.getSortType()).andReturn(IPoiSearchProxy.TYPE_SORT_BY_DISTANCE).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageIndex(poiIndex)).andReturn(0).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageSizeWithAds()).andReturn(0).anyTimes();
        
        EasyMock.expect(address.getPoi()).andReturn(mockPoi).anyTimes();
        EasyMock.expect(mockDataWrapper.getAddress(poiIndex)).andReturn(address);
        EasyMock.expect(mockPoi.getAd()).andReturn(mockAd).anyTimes();
        EasyMock.expect(mockPoi.getBizPoi()).andReturn(mockBizPoi).anyTimes();
        EasyMock.expect(mockPoi.getRating()).andReturn(5).anyTimes();
        EasyMock.expect(mockPoi.getType()).andReturn(Poi.TYPE_POI).anyTimes();
        EasyMock.expect(mockPoi.isAdsPoi()).andReturn(false).anyTimes();
        EasyMock.expect(mockAd.getAdID()).andReturn("aaa").anyTimes();
        EasyMock.expect(mockAd.getAdSource()).andReturn("bbb").anyTimes();
        EasyMock.expect(mockBizPoi.getPoiId()).andReturn("ccc").anyTimes();
        EasyMock.expect(mockBizPoi.getDistance()).andReturn("1000").anyTimes();
        EasyMock.expect(mockDataWrapper.getSearchUid()).andReturn("ddd").anyTimes();
        PowerMock.replay(mockBizPoi);
        
        int distInt = Integer.parseInt(mockBizPoi.getDistance());
        
        PowerMock.replay(mockAd);
        mockMisLog.setAdsId(mockAd.getAdID());
        mockMisLog.setAdsSource(mockAd.getAdSource());

        PowerMock.replay(mockDataWrapper);
        PowerMock.replay(mockPoi);
        
        mockMisLog.setPoiId(mockPoi.getBizPoi().getPoiId());
        mockMisLog.setSearchUid(mockDataWrapper.getSearchUid());
        mockMisLog.setPoiRating(mockPoi.getRating());
        
        PreferenceDao mockPreferenceDao = PowerMock.createPartialMock(PreferenceDao.class,"getPreference");
        EasyMock.expect(daoManager.getPreferenceDao()).andReturn(mockPreferenceDao).anyTimes();
        
        Preference mockPreference = PowerMock.createPartialMock(Preference.class, "getIntValue");
        EasyMock.expect(mockPreferenceDao.getPreference(Preference.ID_PREFERENCE_DISTANCEUNIT)).andReturn(mockPreference).anyTimes();
        
        int distanceUnitValue = 100;
        EasyMock.expect(mockPreference.getIntValue()).andReturn(distanceUnitValue);
        
        String distStr = ResourceManager.getInstance().getStringConverter().convertDistance(distInt, distanceUnitValue);
        mockMisLog.setPoiDistance(distStr);
        mockMisLog.setPoiSorting(poiMisLogHelper.getPoiSorting());
        
        mockMisLog.setPageName(IMisLogConstants.VALUE_POI_PAGE_NAME_DETAIL);
        mockMisLog.setPageIndex(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageNumber(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageSize(mockDataWrapper.getPageSizeWithAds());
        
        mockMisLog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_NORMAL);
        
        PowerMock.replayAll();
        poiMisLogHelper.fillAttrbute(mockMisLog);
        PowerMock.verifyAll();
    }
    
    public void testFillAttrbute_Poi_Type_Is_AdsPoi()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        int poiIndex = 0;
        
        EasyMock.expect(mockMisLog.getPoi()).andReturn(null);
        EasyMock.expect(mockMisLog.getIndex()).andReturn(poiIndex).anyTimes();
        Poi mockPoi = PowerMock.createPartialMock(Poi.class, "getAd", "getBizPoi", "getRating","getType","isAdsPoi");
        Address address = PowerMock.createMock(Address.class);
        Ad mockAd = PowerMock.createPartialMock(Ad.class,"getAdID","getAdSource");
        BizPoi mockBizPoi = PowerMock.createMock(BizPoi.class);
        
        EasyMock.expect(mockDataWrapper.getSortType()).andReturn(IPoiSearchProxy.TYPE_SORT_BY_DISTANCE).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageIndex(poiIndex)).andReturn(0).anyTimes();
        EasyMock.expect(mockDataWrapper.getPageSizeWithAds()).andReturn(0).anyTimes();
        
        EasyMock.expect(address.getPoi()).andReturn(mockPoi).anyTimes();
        EasyMock.expect(mockDataWrapper.getAddress(poiIndex)).andReturn(address);
        EasyMock.expect(mockPoi.getAd()).andReturn(mockAd).anyTimes();
        EasyMock.expect(mockPoi.getBizPoi()).andReturn(mockBizPoi).anyTimes();
        EasyMock.expect(mockPoi.getRating()).andReturn(5).anyTimes();
        EasyMock.expect(mockPoi.getType()).andReturn(Poi.TYPE_POI).anyTimes();
        EasyMock.expect(mockPoi.isAdsPoi()).andReturn(true).anyTimes();
        EasyMock.expect(mockAd.getAdID()).andReturn("aaa").anyTimes();
        EasyMock.expect(mockAd.getAdSource()).andReturn("bbb").anyTimes();
        EasyMock.expect(mockBizPoi.getPoiId()).andReturn("ccc").anyTimes();
        EasyMock.expect(mockBizPoi.getDistance()).andReturn("1000").anyTimes();
        EasyMock.expect(mockDataWrapper.getSearchUid()).andReturn("ddd").anyTimes();
        PowerMock.replay(mockBizPoi);
        
        int distInt = Integer.parseInt(mockBizPoi.getDistance());
        
        PowerMock.replay(mockAd);
        mockMisLog.setAdsId(mockAd.getAdID());
        mockMisLog.setAdsSource(mockAd.getAdSource());

        PowerMock.replay(mockDataWrapper);
        PowerMock.replay(mockPoi);
        
        mockMisLog.setPoiId(mockPoi.getBizPoi().getPoiId());
        mockMisLog.setSearchUid(mockDataWrapper.getSearchUid());
        mockMisLog.setPoiRating(mockPoi.getRating());
        
        PreferenceDao mockPreferenceDao = PowerMock.createPartialMock(PreferenceDao.class,"getPreference");
        EasyMock.expect(daoManager.getPreferenceDao()).andReturn(mockPreferenceDao).anyTimes();
        
        Preference mockPreference = PowerMock.createPartialMock(Preference.class, "getIntValue");
        EasyMock.expect(mockPreferenceDao.getPreference(Preference.ID_PREFERENCE_DISTANCEUNIT)).andReturn(mockPreference).anyTimes();
        
        int distanceUnitValue = 100;
        EasyMock.expect(mockPreference.getIntValue()).andReturn(distanceUnitValue);
        
        String distStr = ResourceManager.getInstance().getStringConverter().convertDistance(distInt, distanceUnitValue);
        mockMisLog.setPoiDistance(distStr);
        mockMisLog.setPoiSorting(poiMisLogHelper.getPoiSorting());
        
        mockMisLog.setPageName(IMisLogConstants.VALUE_POI_PAGE_NAME_DETAIL);
        mockMisLog.setPageIndex(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageNumber(mockDataWrapper.getPageIndex(poiIndex));
        mockMisLog.setPageSize(mockDataWrapper.getPageSizeWithAds());
        
        mockMisLog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_WITH_ADD);
        
        PowerMock.replayAll();
        poiMisLogHelper.fillAttrbute(mockMisLog);
        PowerMock.verifyAll();
    }
    
    public void testRecordPoiMisLog_int_int()
    {
        PoiMisLogHelper mockPoiMisLogHelper;
        mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "recordPoiMisLog", int.class, int.class, Poi.class, long.class);
        mockPoiMisLogHelper.recordPoiMisLog(0, 0, null, -1);
        PowerMock.replayAll();
        mockPoiMisLogHelper.recordPoiMisLog(0, 0);
        PowerMock.verifyAll();
    }
    
    public void testRecordPoiMisLog_int_int_long_PoiDataWrapperIsNull()
    {
        PowerMock.mockStatic(Logger.class);
        Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_INITIAlIZATION);
        PowerMock.replayAll();
        poiMisLogHelper = new PoiMisLogHelper(null);
        poiMisLogHelper.recordPoiMisLog(0, 0, null, -1);
        PowerMock.verifyAll();
    }
    
    public void testRecordPoiMisLog_int_int_long_LogFilterIsTypeDisabled()
    {
        int poiIndex = 2;
        int poiType = 1;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);

        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(false);       
        PowerMock.replayAll();
        poiMisLogHelper.recordPoiMisLog(poiType, poiIndex, null, -1);
        PowerMock.verifyAll();        
    }
    
    public void testRecordPoiMisLog_int_int_long_LogFilterIsTypeEnabled_By_SystemCurrentTimeMillis()
    {
        int poiIndex = 2;
        int poiType = 1;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);

        PowerMock.mockStatic(Logger.class);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogFactory mockMisLogFactory = PowerMock.createMock(MisLogFactory.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFactory()).andReturn(mockMisLogFactory).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(true);      
        EasyMock.expect(mockMisLogFactory.createMisLog(poiType)).andReturn(mockMisLog);
        
        mockMisLog.setTimestamp(EasyMock.anyLong());
        mockMisLog.setIndex(poiIndex);
        mockMisLog.setPoi(null);
        Logger.log(EasyMock.eq(Logger.INFO), EasyMock.eq(PoiMisLogHelper.class.getName()), EasyMock.eq(IMisLogConstants.PROCESS_MISLOG),
            EasyMock.aryEq(new Object[]
            { mockMisLog }));


        PowerMock.replayAll();
        poiMisLogHelper.recordPoiMisLog(poiType, poiIndex, null, -1);
        PowerMock.verifyAll();        
    }
    
    public void testRecordPoiMisLog_int_int_long_LogFilterIsTypeEnabled_By_ParamMillis()
    {
        int poiIndex = 2;
        int poiType = 1;
        int timeMillis = 1000;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);

        PowerMock.mockStatic(Logger.class);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogFactory mockMisLogFactory = PowerMock.createMock(MisLogFactory.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFactory()).andReturn(mockMisLogFactory).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(true);      
        EasyMock.expect(mockMisLogFactory.createMisLog(poiType)).andReturn(mockMisLog);
        
        mockMisLog.setTimestamp(timeMillis);
        mockMisLog.setIndex(poiIndex);
        mockMisLog.setPoi(null);
        Logger.log(EasyMock.eq(Logger.INFO), EasyMock.eq(PoiMisLogHelper.class.getName()), EasyMock.eq(IMisLogConstants.PROCESS_MISLOG),
            EasyMock.aryEq(new Object[]
            { mockMisLog }));
        
        PowerMock.replayAll();
        poiMisLogHelper.recordPoiMisLog(poiType, poiIndex, null, timeMillis);
        PowerMock.verifyAll();        
    }
    
    public void testRecordPoiMisLog_int_int_long_LogFilterIsTypeEnabled_By_TypeIsTYPE_POI_DETAILS()
    {
        int poiIndex = 2;
        int poiType = IMisLogConstants.TYPE_POI_DETAILS;
        int timeMillis = 1000;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);

        PowerMock.mockStatic(Logger.class);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogFactory mockMisLogFactory = PowerMock.createMock(MisLogFactory.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        PoiMisLogHelper mockMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "recordMerchantInfo");
        Whitebox.setInternalState(mockMisLogHelper, "poiDataWrapper", mockDataWrapper);
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFactory()).andReturn(mockMisLogFactory).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(true);      
        EasyMock.expect(mockMisLogFactory.createMisLog(poiType)).andReturn(mockMisLog);
        mockMisLog.setTimestamp(timeMillis);
        mockMisLog.setIndex(poiIndex);        
        mockMisLog.setPoi(null);
        Logger.log(EasyMock.eq(Logger.INFO), EasyMock.eq(mockMisLogHelper.getClass().getName()), EasyMock.eq(IMisLogConstants.PROCESS_MISLOG),
          EasyMock.aryEq(new Object[]
          { mockMisLog }));
        mockMisLogHelper.recordMerchantInfo(poiIndex, true);
        
        PowerMock.replayAll();
        mockMisLogHelper.recordPoiMisLog(poiType, poiIndex, null, timeMillis);
        PowerMock.verifyAll();        
    }
    
    public void testStorePendingPoiMislog_PoiDataWrapper_IS_NULL()
    {
        poiMisLogHelper = new PoiMisLogHelper(null);
        PowerMock.mockStatic(Logger.class);
        Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_INITIAlIZATION);
        PowerMock.replayAll();
        poiMisLogHelper.storePendingPoiMislog(0, 0);
        PowerMock.verifyAll();        
    }
    
    public void testStorePendingPoiMislog_LogFilterIsTypeEnabled()
    {
        int poiIndex = 2;
        int poiType = 2;

        poiMisLogHelper = new PoiMisLogHelper(null);
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        MisLogFactory mockMisLogFactory = PowerMock.createMock(MisLogFactory.class);
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "recordMerchantInfo");
        
        Whitebox.setInternalState(mockPoiMisLogHelper, "poiDataWrapper", mockDataWrapper);
        
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFactory()).andReturn(mockMisLogFactory).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(true);
        
        EasyMock.expect(mockMisLogFactory.createMisLog(poiType)).andReturn(mockMisLog);
        mockMisLog.setIndex(poiIndex);
        mockMisLogManager.storeMisLog(mockMisLog);

        PowerMock.replayAll();
        
        mockPoiMisLogHelper.storePendingPoiMislog(poiType, poiIndex);
        PowerMock.verifyAll();
    }
    
    public void testStorePendingPoiMislog_LogFilterIsTypeEnabled_By_TypeIsTYPE_POI_DETAILS()
    {
        int poiIndex = 2;
        int poiType = IMisLogConstants.TYPE_POI_DETAILS;

        poiMisLogHelper = new PoiMisLogHelper(null);
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        MisLogFactory mockMisLogFactory = PowerMock.createMock(MisLogFactory.class);
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "recordMerchantInfo");
        
        Whitebox.setInternalState(mockPoiMisLogHelper, "poiDataWrapper", mockDataWrapper);
        
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFactory()).andReturn(mockMisLogFactory).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(true);
        
        EasyMock.expect(mockMisLogFactory.createMisLog(poiType)).andReturn(mockMisLog);
        mockMisLog.setIndex(poiIndex);
        mockMisLogManager.storeMisLog(mockMisLog);
        mockPoiMisLogHelper.recordMerchantInfo(poiIndex, false);
        PowerMock.replayAll();
        
        mockPoiMisLogHelper.storePendingPoiMislog(poiType, poiIndex);
        PowerMock.verifyAll();
    }
    
    public void testFlushPendingPoiMislog_GetMisLogIsNull()
    {

        int poiIndex = 2;
        int poiType = 2;

        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());

        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(true);
        EasyMock.expect(mockMisLogManager.getMisLog(poiType)).andReturn(null);
        
        PowerMock.replayAll();
        
        poiMisLogHelper.flushPendingPoiMislog(poiType, poiIndex);
        
        PowerMock.verifyAll();
    }
    
    public void testFlushPendingPoiMislog_GetMisLogIndex_Is_Same_With_Param()
    {

        int poiIndex = 2;
        int poiType = 2;

        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        PowerMock.mockStatic(Logger.class);
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "clearOldMisLog");

        Whitebox.setInternalState(mockPoiMisLogHelper, "poiDataWrapper", mockDataWrapper);
        
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter);
        EasyMock.expect(mockLogFilter.isTypeEnable(poiType)).andReturn(true);
        EasyMock.expect(mockMisLogManager.getMisLog(poiType)).andReturn(mockMisLog);
        EasyMock.expect(mockMisLog.getIndex()).andReturn(poiIndex);
        mockMisLog.setTimestamp(EasyMock.anyLong());
        Logger.log(EasyMock.eq(Logger.INFO), EasyMock.eq(mockPoiMisLogHelper.getClass().getName()),
            EasyMock.eq(IMisLogConstants.PROCESS_MISLOG), EasyMock.aryEq(new Object[]
            { mockMisLog }));
        mockPoiMisLogHelper.clearOldMisLog(poiType);
        
        PowerMock.replayAll();
        
        mockPoiMisLogHelper.flushPendingPoiMislog(poiType, poiIndex);
        
        PowerMock.verifyAll();
    }
    
    public void testFlushPendingPoiMislog_GetMisLogIndex_By_TypeIsTYPE_POI_DETAILS()
    {
        int poiIndex = 2;
        int poiType = IMisLogConstants.TYPE_POI_DETAILS;

        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        PowerMock.mockStatic(Logger.class);
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "clearOldMisLog");

        Whitebox.setInternalState(mockPoiMisLogHelper, "poiDataWrapper", mockDataWrapper);

        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFilter()).andReturn(mockLogFilter).anyTimes();
        EasyMock.expect(mockLogFilter.isTypeEnable(EasyMock.anyInt())).andReturn(true).anyTimes();
        EasyMock.expect(mockMisLogManager.getMisLog(EasyMock.anyInt())).andReturn(mockMisLog).anyTimes();
        EasyMock.expect(mockMisLog.getIndex()).andReturn(poiIndex).anyTimes();
        mockMisLog.setTimestamp(EasyMock.anyLong());
        mockMisLog.setTimestamp(EasyMock.anyLong());
        Logger.log(EasyMock.eq(Logger.INFO), EasyMock.eq(mockPoiMisLogHelper.getClass().getName()),
            EasyMock.eq(IMisLogConstants.PROCESS_MISLOG), EasyMock.aryEq(new Object[]
            { mockMisLog }));
        Logger.log(EasyMock.eq(Logger.INFO), EasyMock.eq(mockPoiMisLogHelper.getClass().getName()),
            EasyMock.eq(IMisLogConstants.PROCESS_MISLOG), EasyMock.aryEq(new Object[]
            { mockMisLog }));
        mockPoiMisLogHelper.clearOldMisLog(EasyMock.anyInt());
        mockPoiMisLogHelper.clearOldMisLog(EasyMock.anyInt());

        PowerMock.replayAll();

        mockPoiMisLogHelper.flushPendingPoiMislog(poiType, poiIndex);

        PowerMock.verifyAll();
    }
    
    public void testClearOldMisLog()
    {
        int poiType = IMisLogConstants.TYPE_POI_DETAILS;
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createMock(PoiMisLogHelper.class);
        poiMisLogHelper = new PoiMisLogHelper(null);
        
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        mockMisLogManager.clearStoredMislog(EasyMock.anyInt());
        mockMisLogManager.clearStoredMislog(EasyMock.anyInt());

        PowerMock.replayAll();
        
        poiMisLogHelper.clearOldMisLog(poiType);
        
        PowerMock.verifyAll();
    }

    public void testStartImpression()
    {
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "endImpression");
        PowerMock.mockStatic(Logger.class);

        Whitebox.setInternalState(mockPoiMisLogHelper, "impressionTable", new Hashtable());

        Logger.log(Logger.WARNING, mockPoiMisLogHelper.getClass().getName(), PoiMisLogHelper.EXCEPTION_INVALID_OPERATION);
        mockPoiMisLogHelper.endImpression();
        assertNotNull(Whitebox.getInternalState(mockPoiMisLogHelper, "impressionTable"));
        PowerMock.replayAll();
        mockPoiMisLogHelper.startImpression();
        PowerMock.verifyAll();
    }
    
    public void testImpressPoi_int()
    {
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "impressPoi", int.class, int.class);
        mockPoiMisLogHelper.impressPoi(0, 0);
        PowerMock.replayAll();
        mockPoiMisLogHelper.impressPoi(0);
        PowerMock.verifyAll();
    }
    
    public void testImpressPoi_int_int_PoiIndex_IS_Invalid()
    {
        int startIndex = 1;
        int endIndex = 0;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PowerMock.mockStatic(Logger.class);
        Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_INITIAlIZATION);
        PowerMock.replayAll();
        
        poiMisLogHelper.impressPoi(startIndex, endIndex);
        
        PowerMock.verifyAll();
    }
    
    public void testImpressPoi_int_int_ImpressionTable_Is_Null()
    {
        int startIndex = 0;
        int endIndex = 5;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PowerMock.mockStatic(Logger.class);

        Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_INITIAlIZATION);
        PowerMock.replayAll();

        poiMisLogHelper.impressPoi(startIndex, endIndex);

        PowerMock.verifyAll();
    }
    
    public void testImpressPoi_int_int_Address_Is_Null()
    {
        int startIndex = 0;
        int endIndex = 5;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PowerMock.mockStatic(Logger.class);
        
        Whitebox.setInternalState(poiMisLogHelper, "impressionTable", new Hashtable());

        int count = endIndex - startIndex + 1;

        for (int i = 0; i < count; i++)
        {
            Address address = null;
            EasyMock.expect(mockDataWrapper.getAddress(i)).andReturn(address);
            Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_POI_DATA);
        }

        PowerMock.replayAll();

        poiMisLogHelper.impressPoi(startIndex, endIndex);

        PowerMock.verifyAll();
    }
    
    public void testImpressPoi_int_int_Poi_In_Address_Is_Null()
    {
        int startIndex = 0;
        int endIndex = 5;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PowerMock.mockStatic(Logger.class);
        
        Whitebox.setInternalState(poiMisLogHelper, "impressionTable", new Hashtable());

        int count = endIndex - startIndex + 1;

        for (int i = 0; i < count; i++)
        {
            Address mockAddress = EasyMock.createMock(Address.class);
            EasyMock.expect(mockAddress.getPoi()).andReturn(null);
            EasyMock.expect(mockDataWrapper.getAddress(i)).andReturn(mockAddress);
            Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_POI_DATA);
        }

        PowerMock.replayAll();

        poiMisLogHelper.impressPoi(startIndex, endIndex);

        PowerMock.verifyAll();
    }
    
    public void testImpressPoi_int_int_Poi_In_Address_Is_NOT_Null()
    {
        int startIndex = 0;
        int endIndex = 5;
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "recordPoiMisLog");
        PowerMock.mockStatic(Logger.class);
        Hashtable mockImpressionTable = PowerMock.createPartialMock(Hashtable.class, "containsKey", "put");
        Whitebox.setInternalState(mockPoiMisLogHelper, "impressionTable", mockImpressionTable);
        Whitebox.setInternalState(mockPoiMisLogHelper, "poiDataWrapper", mockDataWrapper);

        Address mockAddress = new Address();
        Poi mockPoi = new Poi();
        mockAddress.setPoi(mockPoi);

        EasyMock.expect(mockDataWrapper.getAddress(EasyMock.anyInt())).andReturn(mockAddress).anyTimes();
        EasyMock.expect(mockImpressionTable.containsKey(EasyMock.anyObject(Integer.class))).andReturn(false).anyTimes();
        mockPoiMisLogHelper.recordPoiMisLog(EasyMock.eq(IMisLogConstants.TYPE_POI_IMPRESSION), EasyMock.anyInt(), EasyMock.anyObject(Poi.class), EasyMock.anyLong());
        PowerMock.expectLastCall().anyTimes();
        EasyMock.expect(
            mockImpressionTable.put(EasyMock.anyInt(), EasyMock.eq(Whitebox.getInternalState(PoiMisLogHelper.class, "FLAG_IMPRESSION"))))
                .andReturn(1).anyTimes();

        PowerMock.replayAll();

        mockPoiMisLogHelper.impressPoi(startIndex, endIndex);

        PowerMock.verifyAll();
    }
    
    public void testEndImpression_ImpressionTable_Is_Null()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PowerMock.mockStatic(Logger.class);
        Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_OPERATION);
        PowerMock.replayAll();
        poiMisLogHelper.endImpression();
        PowerMock.verifyAll();
    }
    
    public void testEndImpression_ImpressionTable_Is_Not_Null()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        Hashtable impressionTable = new Hashtable();
        Whitebox.setInternalState(poiMisLogHelper, "impressionTable", impressionTable);
        
        PowerMock.replayAll();        
        poiMisLogHelper.endImpression();       
        PowerMock.verifyAll();        
        assertNull(Whitebox.getInternalState(poiMisLogHelper, "impressionTable"));
    }
    
    public void testIsMapResultsMode_True()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        Whitebox.setInternalState(poiMisLogHelper, "isMapResultsMode", true);       
        assertTrue(poiMisLogHelper.isMapResultsMode());
    }
    
    public void testIsMapResultsMode_False()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        Whitebox.setInternalState(poiMisLogHelper, "isMapResultsMode", false);       
        assertFalse(poiMisLogHelper.isMapResultsMode());
    }
    
    public void testShowSelectedPoiMap_PoiDataWrapper_Is_Null()
    {
        poiMisLogHelper = new PoiMisLogHelper(null);
        PowerMock.mockStatic(Logger.class);
        Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_INITIAlIZATION);
        PowerMock.replayAll();  
        poiMisLogHelper.showSelectedPoiMap(0, true, 0);
        PowerMock.verifyAll();
    }
    
    public void testShowSelectedPoiMap_PoiDataWrapper_NormalIndex_Is_True()
    {
        int index = 0;
        int wholeIndex = 1;
        boolean isNormalIndex = true;
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "recordPoiMisLog", "getWholeIndex");
        Whitebox.setInternalState(mockPoiMisLogHelper, "poiDataWrapper", mockDataWrapper);
        
        EasyMock.expect(mockPoiMisLogHelper.getWholeIndex(index)).andReturn(index);
        
        mockPoiMisLogHelper.recordPoiMisLog(EasyMock.eq(IMisLogConstants.TYPE_POI_VIEW_MAP), EasyMock.eq(index));
        PowerMock.replayAll();
        mockPoiMisLogHelper.showSelectedPoiMap(index, isNormalIndex, wholeIndex);
        PowerMock.verifyAll();
    }    
    
    public void testShowPoiPage_PoiDataWrapper_Is_Null()
    {
        poiMisLogHelper = new PoiMisLogHelper(null);
        PowerMock.mockStatic(Logger.class);
        Logger.log(Logger.WARNING, PoiMisLogHelper.class.getName(), PoiMisLogHelper.EXCEPTION_INVALID_INITIAlIZATION);
        PowerMock.replayAll();  
        poiMisLogHelper.showPoiPage(0, 0, false);
        PowerMock.verifyAll();
    }
    
    public void testShowPoiPage_PoiDataWrapper_HasAdPoiInMap_Is_True()
    {
        int pageIndex = 0;
        int normalCount = 10;
        boolean hasAdPoiInMap = true;
        
        PowerMock.mockStaticPartial(PoiMisLogHelper.class,"getInstance");
        
        PoiMisLogHelper mockPoiMisLogHelper = PowerMock.createPartialMock(PoiMisLogHelper.class, "getWholeIndex", "recordPoiMisLog");
        
        Whitebox.setInternalState(mockPoiMisLogHelper, "poiDataWrapper", mockDataWrapper);

        int startIndex = 10;
        EasyMock.expect(mockPoiMisLogHelper.getWholeIndex(pageIndex * PoiDataRequester.DEFAULT_PAGE_SIZE)).andReturn(startIndex);

        int newNormalCount = normalCount + 1;     
        int newStartIndex = startIndex - 1;  
        int executeTimes = normalCount + (newNormalCount - normalCount);

        EasyMock.expect(PoiMisLogHelper.getInstance()).andReturn(mockPoiMisLogHelper).times(executeTimes);
        mockPoiMisLogHelper.recordPoiMisLog(EasyMock.eq(IMisLogConstants.TYPE_POI_MAP_ALL), EasyMock.eq(newStartIndex));
        mockPoiMisLogHelper.recordPoiMisLog(EasyMock.eq(IMisLogConstants.TYPE_POI_MAP_ALL), EasyMock.anyInt());
        PowerMock.expectLastCall().times(executeTimes - 1);
        
        PowerMock.replayAll();
        mockPoiMisLogHelper.showPoiPage(pageIndex, normalCount, hasAdPoiInMap);
        PowerMock.verifyAll();
    }  
    
    public void testGetWholeIndex()
    {
        int normalIndex = 39;
        int pageIndex = normalIndex / PoiDataRequester.DEFAULT_PAGE_SIZE;

        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        Address address = new Address();
        Poi mockPoi = PowerMock.createPartialMock(Poi.class, "getType");
        address.setPoi(mockPoi);

        int totalExecuteTimes = pageIndex + 1;
        int typePoiCount = totalExecuteTimes - 1 < 1 ? 1 : totalExecuteTimes - 1;
        int typeDummyCount = totalExecuteTimes - typePoiCount;

        EasyMock.expect(mockDataWrapper.getSponsoredAddress(EasyMock.anyInt())).andReturn(address).times(totalExecuteTimes);
        EasyMock.expect(mockPoi.getType()).andReturn(Poi.TYPE_POI).times(typePoiCount);
        if (typeDummyCount > 0)
        {
            EasyMock.expect(mockPoi.getType()).andReturn(Poi.TYPE_DUMMY_POI).times(typeDummyCount);
        }

        PowerMock.replayAll();
        assertEquals(normalIndex + typePoiCount, poiMisLogHelper.getWholeIndex(normalIndex));
        PowerMock.verifyAll();
    }
    
    public void testRecordMerchantInfo_NeedLogInstantly_Is_True()
    {
        int poiIndex = 1;
        boolean needLogInstantly = true;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        Address address = new Address();
        Poi mockPoi = PowerMock.createPartialMock(Poi.class, "getAd");
        Ad mockAd = PowerMock.createPartialMock(Ad.class, "getAdLine");
        address.setPoi(mockPoi);

        PowerMock.mockStatic(Logger.class);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogFactory mockMisLogFactory = PowerMock.createMock(MisLogFactory.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFactory()).andReturn(mockMisLogFactory).anyTimes();
        EasyMock.expect(mockMisLogFactory.createMisLog(IMisLogConstants.TYPE_POI_VIEW_MERCHANT)).andReturn(mockMisLog);

        EasyMock.expect(mockAd.getAdLine()).andReturn("testAdLine").anyTimes();
        EasyMock.expect(mockPoi.getAd()).andReturn(mockAd).anyTimes();
        EasyMock.expect(mockDataWrapper.getAddress(poiIndex)).andReturn(address);
        
        mockMisLog.setIndex(poiIndex);
        mockMisLog.setTimestamp(EasyMock.anyLong());
        Logger.log(EasyMock.eq(Logger.INFO), EasyMock.eq(PoiMisLogHelper.class.getName()), EasyMock.eq(IMisLogConstants.PROCESS_MISLOG),
          EasyMock.aryEq(new Object[]
          { mockMisLog }));
            
        PowerMock.replayAll();
        poiMisLogHelper.recordMerchantInfo(poiIndex, needLogInstantly);
        PowerMock.verifyAll();
    }
    
    public void testRecordMerchantInfo_NeedLogInstantly_Is_False()
    {
        int poiIndex = 1;
        boolean needLogInstantly = false;
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        Address address = new Address();
        Poi mockPoi = PowerMock.createPartialMock(Poi.class, "getAd");
        Ad mockAd = PowerMock.createPartialMock(Ad.class, "getAdLine");
        address.setPoi(mockPoi);

        PowerMock.mockStatic(Logger.class);
        PoiMisLog mockMisLog = PowerMock.createMock(PoiMisLog.class);
        MisLogFilter mockLogFilter = PowerMock.createMock(MisLogFilter.class);
        MisLogFactory mockMisLogFactory = PowerMock.createMock(MisLogFactory.class);
        MisLogDao misLogDao = new MisLogDao(new TnStoreMock());
        PowerMock.suppressConstructor(MisLogManager.class);
        PowerMock.mockStatic(MisLogManager.class);
        MisLogManager mockMisLogManager = PowerMock.createPartialMockForAllMethodsExcept(MisLogManager.class, "getInstance");
        EasyMock.expect(daoManager.getMisLogDao()).andReturn(misLogDao).anyTimes();
        EasyMock.expect(MisLogManager.getInstance()).andReturn(mockMisLogManager).anyTimes();
        EasyMock.expect(mockMisLogManager.getFactory()).andReturn(mockMisLogFactory).anyTimes();
        EasyMock.expect(mockMisLogFactory.createMisLog(IMisLogConstants.TYPE_POI_VIEW_MERCHANT)).andReturn(mockMisLog);

        EasyMock.expect(mockAd.getAdLine()).andReturn("testAdLine").anyTimes();
        EasyMock.expect(mockPoi.getAd()).andReturn(mockAd).anyTimes();
        EasyMock.expect(mockDataWrapper.getAddress(poiIndex)).andReturn(address);

        mockMisLog.setIndex(poiIndex);
        mockMisLogManager.storeMisLog(mockMisLog);
        
        PowerMock.replayAll();
        poiMisLogHelper.recordMerchantInfo(poiIndex, needLogInstantly);
        PowerMock.verifyAll();
    }
    
    public void testGetPoiSorting()
    {
        poiMisLogHelper = new PoiMisLogHelper(mockDataWrapper);
        PowerMock.mockStaticPartial(PoiMisLogHelper.class,"transformPoiSortType");
        int sortType = IPoiSearchProxy.TYPE_SORT_BY_DISTANCE;
        EasyMock.expect(mockDataWrapper.getSortType()).andReturn(sortType);
        EasyMock.expect(PoiMisLogHelper.transformPoiSortType(IPoiSearchProxy.TYPE_SORT_BY_DISTANCE)).andReturn(IMisLogConstants.VALUE_POI_SORT_BY_DISTANCE);
        
        PowerMock.replayAll();
        poiMisLogHelper.getPoiSorting();
        PowerMock.verifyAll();        
    }
    
    public void testTransformPoiSortType()
    {
        assertEquals(IMisLogConstants.VALUE_POI_SORT_BY_DISTANCE, testTransformPoiSortType(IPoiSearchProxy.TYPE_SORT_BY_DISTANCE));
        assertEquals(IMisLogConstants.VALUE_POI_SORT_BY_RATING, testTransformPoiSortType(IPoiSearchProxy.TYPE_SORT_BY_RATING));
        assertEquals(IMisLogConstants.VALUE_POI_SORT_BY_POPULAR, testTransformPoiSortType(IPoiSearchProxy.TYPE_SORT_BY_POPULAR));
        assertEquals(IMisLogConstants.VALUE_POI_SORT_BY_RELEVANCE, testTransformPoiSortType(IPoiSearchProxy.TYPE_SORT_BY_RELEVANCE));
        assertEquals(IMisLogConstants.VALUE_POI_SORT_BY_GASPRICE, testTransformPoiSortType(IPoiSearchProxy.TYPE_SORT_BY_PRICE));
        assertEquals("", testTransformPoiSortType(999));
    }

    private String testTransformPoiSortType(int sortType)
    {
        return PoiMisLogHelper.transformPoiSortType(sortType);
    }

}
