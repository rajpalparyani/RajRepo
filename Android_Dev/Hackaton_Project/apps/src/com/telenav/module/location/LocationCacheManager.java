package com.telenav.module.location;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.location.TnLocation;
import com.telenav.radio.TnCellInfo;
import com.telenav.radio.TnRadioManager;

public class LocationCacheManager
{
    public static int CELL_LOCATION_CACHE_MAX_SIZE  =  10;  
    
    public static long CELL_LOCATION_CACHE_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours 
    
    protected int cellLocationCacheSize = CELL_LOCATION_CACHE_MAX_SIZE;
    
    private Vector cellTowerMappingInfos = new Vector();
    
    private TnLocation lastCellLocation;
    
    private TnLocation lastGpsLocation;
    
    private Object mutex = new Object();
    
    private static class InnterLocationCacheManager
    {
        private static LocationCacheManager instance = new LocationCacheManager();
    }
    
    private LocationCacheManager()
    {
        load();
    }

    public static LocationCacheManager getInstance()
    {
        return InnterLocationCacheManager.instance;
    }
    
    protected void setCellLocMaxSize(int maxSize)
    {
        this.cellLocationCacheSize = maxSize;
    }
    
    protected int getCellLocMaxSize()
    {
        return this.cellLocationCacheSize;
    }
    
    public void setLastGpsLocation(TnLocation location)
    {
        this.lastGpsLocation = location;
    }

    protected TnLocation getLastGpsLocation()
    {
        return lastGpsLocation;
    }
    
    public void setLastCellLocation(TnLocation cellLocation)
    {
        this.lastCellLocation = cellLocation;
    }
    
    protected TnLocation getLastCellLocation()
    {
        return lastCellLocation;
    }
    
    protected void load()
    {
        cellTowerMappingInfos = ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().getCellTowerCache();
        lastCellLocation = ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().getLastCellLocation();
        lastGpsLocation = ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().getLastGpsLocation();
        
        if (cellTowerMappingInfos == null) 
            cellTowerMappingInfos = new Vector();
        
        }
    
    public void save()
    {
        ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().setLastCellLocation(lastCellLocation);
        ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().setLastGpsLocation(lastGpsLocation);
        
        synchronized (mutex)
        {
            ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().setCellTowerCache(cellTowerMappingInfos);
        }
        
        ((DaoManager)DaoManager.getInstance()).getLocationCacheDao().store();
    }
    	
    protected TnLocation getLocationByCellTowerInfo()
    {
        TnCellInfo cellInfo = TnRadioManager.getInstance().getCellInfo();
        return getLocationByCellTowerInfo(cellInfo);
    }
    
    protected TnLocation getLocationByCellTowerInfo(TnCellInfo cellTowerInfo)
    {
        if (cellTowerInfo == null) return null;
        
        synchronized (mutex)
        {
            int index = findCellTowerInfoIndex(cellTowerInfo);
            if (index == -1) return null;
            
            CellTowerMapping mapping = (CellTowerMapping)cellTowerMappingInfos.elementAt(index);
            TnLocation location = mapping.location;
            cellTowerMappingInfos.removeElementAt(index);
            
            if (System.currentTimeMillis() > location.getLocalTimeStamp() + CELL_LOCATION_CACHE_EXPIRATION_TIME)
            {
                return null;    
            }
            
            cellTowerMappingInfos.insertElementAt(mapping, 0);

            TnLocation clone = new TnLocation("");
            clone.set(location);
            clone.setLocalTimeStamp(System.currentTimeMillis());
            clone.setTime(System.currentTimeMillis() / 10);
            
            return clone;
        }   
    }
    
    protected void addCellLocationMapping(TnCellInfo cellTowerInfo, TnLocation locationData)
    {
        if (cellTowerInfo == null || locationData == null)
            return;
        
        lastCellLocation = locationData;
        synchronized (mutex)
        {
            int index = findCellTowerInfoIndex(cellTowerInfo);
            if (index != -1)
            {
                cellTowerMappingInfos.removeElementAt(index);
            }
            
            CellTowerMapping mapping = new CellTowerMapping();
            mapping.cellInfo = cellTowerInfo;
            mapping.location = locationData;
            
            cellTowerMappingInfos.insertElementAt(mapping, 0);
            
            while(cellTowerMappingInfos.size() > CELL_LOCATION_CACHE_MAX_SIZE)
            {
                cellTowerMappingInfos.removeElementAt(cellTowerMappingInfos.size() - 1);
            }
        }
        save();
    }
    
    protected int findCellTowerInfoIndex(TnCellInfo cellTowerInfo)
    {
    	if(cellTowerMappingInfos != null)
    	{
	        for (int i = 0; i < cellTowerMappingInfos.size(); i++)
	        {
	            CellTowerMapping mapping = (CellTowerMapping)cellTowerMappingInfos.elementAt(i);
	            if (isCellTowerInfoEqual(mapping.cellInfo, cellTowerInfo))
	            {
	                return i;
	            }
	        }
    	}
        
        return - 1;
    }
    
    protected boolean isCellTowerInfoEqual(TnCellInfo info1, TnCellInfo info2)
    {
        if (info1 == null || info2 == null)
            return false;

        return isValueEqual(info1.cellId, info2.cellId) 
                && isValueEqual(info1.baseStationId, info2.baseStationId)
                && isValueEqual(info1.locationAreaCode, info2.locationAreaCode)
                && isValueEqual(info1.countryCode, info2.countryCode)
                && isValueEqual(info1.networkCode, info2.networkCode)
                && info1.networkRadioMode == info2.networkRadioMode;
    }
    
    protected boolean isValueEqual(String v1, String v2)
    {
        if (v1 == null || v2 == null || v1.length() == 0 || v2.length() == 0)
        {    
            return false;
        }    
        else
        {    
            return v1.equalsIgnoreCase(v2);
        }  
    }
    
    public static class CellTowerMapping
    {
        public TnCellInfo cellInfo;
        public TnLocation location;
    }
}
