/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiMisLogHelper.java
 *
 */
package com.telenav.log.mis.helper;

import java.util.Hashtable;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.log.mis.log.PoiMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.res.ResourceManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author Casper (pwang@telenav.cn)
 * @date 2011-1-11
 */
public class PoiMisLogHelper extends AbstractMisLogHelper
{
    public static String EXCEPTION_INVALID_OPERATION = "MisLog Exception: Invalid operation";

    public static String EXCEPTION_INVALID_INITIAlIZATION = "MisLog Exception: Poi data have not been initialized";

    public static String EXCEPTION_INVALID_POI_DATA = "MisLog Exception: Cannot find corresponding poi data";
    
    public static final byte SHOW_TYPE_UNDEFINED = 0;

    public static final byte SHOW_TYPE_BY_INTENTION = 1;

    public static final byte SHOW_TYPE_BY_DEFAULT = 2;

    public static final byte SHOW_TYPE_BY_CLICK = 3;

    public static final byte SHOW_TYPE_BY_ARROW = 4;

    private static PoiMisLogHelper instance;

    private static Integer FLAG_IMPRESSION = PrimitiveTypeCache.valueOf(1);

    private Hashtable impressionTable;

    private PoiDataWrapper poiDataWrapper;
    
    private boolean isMapResultsMode;

    public PoiMisLogHelper(PoiDataWrapper poiDataWrapper)
    {
        this.poiDataWrapper = poiDataWrapper;
    }

    public static PoiMisLogHelper newInstance(PoiDataWrapper poiDataWrapper)
    {
        instance = new PoiMisLogHelper(poiDataWrapper);
        return instance;
    }

    public static PoiMisLogHelper getInstance()
    {
        if (instance == null)
        {
            return newInstance(null);
        }
        return instance;
    }

    public boolean isLogEnable(AbstractMisLog log)
    {
        PoiMisLog misLog = (PoiMisLog) log;
        if(misLog.getPoi() == null)
        {
            if (misLog.getIndex() >=0 && poiDataWrapper == null)
            {
                return false;
            }
            Address addr = poiDataWrapper.getAddress(misLog.getIndex());
            if (addr != null && addr.getPoi() != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public void fillAttrbute(AbstractMisLog log)
    {
        if (log != null && log instanceof PoiMisLog)
        {
            PoiMisLog mislog = (PoiMisLog) log;
            int poiIndex = mislog.getIndex();

            Poi poi = mislog.getPoi();
            if(poi == null)
            {
                //If poi is not null, do not retrieve data from PoiDataWrapper, otherwise need to store the datawrapper info.
                poi =  poiDataWrapper.getAddress(mislog.getIndex()).getPoi();
                mislog.setSearchUid(poiDataWrapper.getSearchUid());
                mislog.setPageName(IMisLogConstants.VALUE_POI_PAGE_NAME_DETAIL);
                mislog.setPageIndex(poiDataWrapper.getPageIndex(poiIndex));
                mislog.setPageNumber(poiDataWrapper.getPageIndex(poiIndex));
                mislog.setPageSize(poiDataWrapper.getPageSizeWithAds());
                mislog.setPoiSorting(getPoiSorting());
            }
            if (poi == null)
            {
                Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_POI_DATA);
                return;
            }

            if (poi.getAd() != null)
            {
                mislog.setAdsId(poi.getAd().getAdID());
                mislog.setAdsSource(poi.getAd().getAdSource());
            }

            if (poi.getBizPoi() != null)
            {
                mislog.setPoiId(poi.getBizPoi().getPoiId());
            }


            mislog.setPoiRating(poi.getRating());
            BizPoi bizPoi = poi.getBizPoi();
            int distInt = 0;
            if (bizPoi != null && bizPoi.getDistance() != null)
            {
                distInt = Integer.parseInt(poi.getBizPoi().getDistance());
            }
            Preference distanceUnit = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
                Preference.ID_PREFERENCE_DISTANCEUNIT);
            int distanceUnitValue = Preference.UNIT_METRIC;
            if (distanceUnit != null)
            {
                distanceUnitValue = distanceUnit.getIntValue();
            }
            String distStr = ResourceManager.getInstance().getStringConverter().convertDistance(distInt, distanceUnitValue);
            mislog.setPoiDistance(distStr);


            if (poi.getType() == Poi.TYPE_SPONSOR_POI)
            {
                mislog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_SPOSORED);
            }
            else
            {
                if (poi.isAdsPoi())
                {
                    mislog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_WITH_ADD);
                }
                else
                {
                    mislog.setPoiType(IMisLogConstants.VALUE_POI_TYPE_NORMAL);
                }
            }
        }
    }

    public void recordPoiMisLog(int type, int poiIndex)
    {
        recordPoiMisLog(type, poiIndex, null, -1);
    }
    
    public void recordPoiMisLog(int type, Poi poi)
    {
        recordPoiMisLog(type, -1, poi, -1);
    }
    
    public void recordPoiMisLog(int type, int poiIndex, Poi poi, long timeMillis)
    {
        if (poiIndex >= 0 && poiDataWrapper == null)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_INITIAlIZATION);
            return;
        }

        if (MisLogManager.getInstance().getFilter().isTypeEnable(type))
        {
            PoiMisLog log = (PoiMisLog) MisLogManager.getInstance().getFactory().createMisLog(type);
            if (timeMillis == -1)
            {
                log.setTimestamp(System.currentTimeMillis());
            }
            else
            {
                log.setTimestamp(timeMillis);
            }
            log.setIndex(poiIndex);
            log.setPoi(poi);
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });

            if (type == IMisLogConstants.TYPE_POI_DETAILS)
            {
                recordMerchantInfo(poiIndex, true);
            }
        }
    }
    
    public void storePendingPoiMislog(int type, int poiIndex)
    {
        if (poiDataWrapper == null)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_INITIAlIZATION);
            return;
        }

        if (MisLogManager.getInstance().getFilter().isTypeEnable(type))
        {
            PoiMisLog log = (PoiMisLog) MisLogManager.getInstance().getFactory().createMisLog(type);
            log.setIndex(poiIndex);

            MisLogManager.getInstance().storeMisLog(log);

            if (type == IMisLogConstants.TYPE_POI_DETAILS)
            {
                recordMerchantInfo(poiIndex, false);
            }
        }
    }
    
    public void flushPendingPoiMislog(int type, int poiIndex)
    {
        if (MisLogManager.getInstance().getFilter().isTypeEnable(type))
        {
            AbstractMisLog log = MisLogManager.getInstance().getMisLog(type);
            if (log == null)
            {
                return;
            }
            if (log instanceof PoiMisLog)
            {
                if (((PoiMisLog) log).getIndex() == poiIndex)
                {
                    log.setTimestamp(System.currentTimeMillis());
                    Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                    { log });
                    
                    if (type == IMisLogConstants.TYPE_POI_DETAILS)
                    {
                        flushPendingPoiMislog(IMisLogConstants.TYPE_POI_VIEW_MERCHANT, poiIndex);
                    }
                    
                    clearOldMisLog(type);
                }
            }
        }
    }
    
    public void clearOldMisLog(int type)
    {
        MisLogManager.getInstance().clearStoredMislog(type);
        if (type == IMisLogConstants.TYPE_POI_DETAILS)
        {
            clearOldMisLog(IMisLogConstants.TYPE_POI_VIEW_MERCHANT);
        }
    }

    public void startImpression()
    {
        if (impressionTable != null)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_OPERATION);
            endImpression();
        }
        impressionTable = new Hashtable();
    }

    public void impressPoi(int poiIndex)
    {
        impressPoi(poiIndex, poiIndex);
    }

    public void impressPoi(int startIndex, int endIndex)
    {
        if (endIndex < startIndex || impressionTable == null)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_INITIAlIZATION);
            return;
        }

        int count = endIndex - startIndex + 1;
        for (int i = 0; i < count; i++)
        {
            Address address = poiDataWrapper.getAddress(i);

            if (address != null && address.getPoi() != null)
            {
                Poi poi = address.getPoi();
                int index = startIndex + i;
                if (!impressionTable.containsKey(PrimitiveTypeCache.valueOf(index)))
                {
                    recordPoiMisLog(IMisLogConstants.TYPE_POI_IMPRESSION, index, null, System.currentTimeMillis());
                    impressionTable.put(PrimitiveTypeCache.valueOf(index), FLAG_IMPRESSION);
                }
            }
            else
            {
                Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_POI_DATA);
            }
        }
    }

    public void endImpression()
    {
        if (impressionTable == null)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_OPERATION);
        }
        impressionTable = null;
    }
    
    public boolean isMapResultsMode()
    {
        return isMapResultsMode;
    }
    
    public void setIsMapResulstMode(boolean isMapResultsMode)
    {
        this.isMapResultsMode = isMapResultsMode;
    }

    public void showSelectedPoiMap(int index, boolean isNormalIndex, int showType)
    {
        if (poiDataWrapper == null)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_INITIAlIZATION);
            return;
        }

        if(isNormalIndex)
        {
            index = getWholeIndex(index);
        }

        recordPoiMisLog(IMisLogConstants.TYPE_POI_VIEW_MAP, index);
    }
    
    public void showPoiPage(int pageIndex, int normalCount, boolean hasAdPoiInMap)
    {
        if (poiDataWrapper == null)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), EXCEPTION_INVALID_INITIAlIZATION);
            return;
        }
        
        int startIndex = getWholeIndex(pageIndex *  PoiDataRequester.DEFAULT_PAGE_SIZE);
        if(hasAdPoiInMap)
        {
            startIndex--;
            normalCount++;
        }
        for (int i = startIndex; i < startIndex + normalCount; i++)
        {
            PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_MAP_ALL, i);
        }
    }
    
    public int getWholeIndex(int normalIndex)
    {
        int pageIndex = normalIndex / PoiDataRequester.DEFAULT_PAGE_SIZE;

        for (int i = 0; i <= pageIndex; i++)
        {
            Address address = poiDataWrapper.getSponsoredAddress(i);
            if (address != null && address.getPoi() != null && address.getPoi().getType() != Poi.TYPE_DUMMY_POI)
            {
                normalIndex++;
            }
        }
        return normalIndex;
    }

    public void recordMerchantInfo(int poiIndex, boolean needLogInstantly)
    {
        Address address = poiDataWrapper.getAddress(poiIndex);
        if (address != null)
        {
            Poi poi = address.getPoi();
            if (poi != null && poi.getAd() != null && poi.getAd().getAdLine() != null && poi.getAd().getAdLine().length() > 0)
            {
                PoiMisLog log = (PoiMisLog) MisLogManager.getInstance().getFactory().createMisLog(IMisLogConstants.TYPE_POI_VIEW_MERCHANT);
                log.setIndex(poiIndex);
                if(needLogInstantly)
                {
                    log.setTimestamp(System.currentTimeMillis());
                    Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                    { log });
                }
                else
                {
                    MisLogManager.getInstance().storeMisLog(log);
                }
            }
        }
    }

    public String getPoiSorting()
    {
        return transformPoiSortType(poiDataWrapper.getSortType());
    }
    
    public static String transformPoiSortType(int sortType)
    {
        String poiSorting = "";
        switch (sortType)
        {
            case IPoiSearchProxy.TYPE_SORT_BY_DISTANCE:
            {
                poiSorting = IMisLogConstants.VALUE_POI_SORT_BY_DISTANCE;
                break;
            }
            case IPoiSearchProxy.TYPE_SORT_BY_RATING:
            {
                poiSorting = IMisLogConstants.VALUE_POI_SORT_BY_RATING;
                break;
            }
            case IPoiSearchProxy.TYPE_SORT_BY_POPULAR:
            {
                poiSorting = IMisLogConstants.VALUE_POI_SORT_BY_POPULAR;
                break;
            }
            case IPoiSearchProxy.TYPE_SORT_BY_RELEVANCE:
            {
                poiSorting = IMisLogConstants.VALUE_POI_SORT_BY_RELEVANCE;
                break;
            }
            case IPoiSearchProxy.TYPE_SORT_BY_PRICE:
            {
                poiSorting = IMisLogConstants.VALUE_POI_SORT_BY_GASPRICE;
                break;
            }
            default:
                break;
        }
        return poiSorting;
    }

    public void addRule(String rule)
    {
    }

    public void reset()
    {
    }

}
