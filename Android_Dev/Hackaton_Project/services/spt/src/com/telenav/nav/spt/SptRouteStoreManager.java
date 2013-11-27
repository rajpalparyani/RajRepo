package com.telenav.nav.spt;

import java.util.Enumeration;
import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.datatypes.route.ISptDataSerializable;
import com.telenav.datatypes.route.SptTree;
import com.telenav.datatypes.route.SptRouteStoreInfo;
import com.telenav.datatypes.route.SptTreeStoreInfo;
import com.telenav.location.TnLocation;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

class SptRouteStoreManager
{
	static int ROUTE_DATA_KEY       = 100;
	static int ROUTE_DATA_INDEX_KEY = 500;
	
    TnStore routeStore; 
    TnStore routeIndexStore;
    
    Object storeMutex = new Object();
    
    Vector routeStoreInfos = null;
    
    boolean isRouteStoreLoaded = false;
    
    SptRouteStoreInfo forwardRouteStoreInfo = new SptRouteStoreInfo();
    SptRouteStoreInfo reverseRouteStoreInfo = new SptRouteStoreInfo();
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
  
    SptRouteStoreManager()
    {
    }

    void clearAllRAM()
    {
    	synchronized (storeMutex)
    	{
    		if (forwardRouteStoreInfo != null) forwardRouteStoreInfo.clear();
    		if (reverseRouteStoreInfo != null) reverseRouteStoreInfo.clear();
    		
    		routeStoreInfos = null;
    		isRouteStoreLoaded = false;
    	}
    }
    
    private SptRouteStoreInfo getRouteStoreInfo(boolean isForwardRoute)
    {
    	return isForwardRoute ? forwardRouteStoreInfo : reverseRouteStoreInfo;
    }
    
    void saveRouteData(byte[] routeData, boolean isForwardRoute)
    {
    	synchronized (storeMutex)
    	{
    		loadRouteDataStore();
    		SptRouteStoreInfo storeInfo = getRouteStoreInfo(isForwardRoute);
    		
    		int routeKey = createNewRouteDataKey();
            routeStore.put(new Integer(routeKey), routeData);
            storeInfo.setMultiRouteStoreKey(routeKey);
    	}
    }
    
    void saveMapSectionNode(byte[] mapSectionData, boolean isForwardRoute)
    {
        synchronized (storeMutex)
        {
        	loadRouteDataStore();
        	SptRouteStoreInfo storeInfo = getRouteStoreInfo(isForwardRoute);
        	
            int mapSectionKey = createNewRouteDataKey();
            
            Integer key = new Integer(mapSectionKey);
            routeStore.put(key,mapSectionData);
            storeInfo.getMapSectionStoreKeys().addElement(key);
        }
    }
    
    void saveSPTTree(SptTree pathTree, boolean isForwardRoute)
    {
    	if (pathTree.getBinData() == null) return;
        
    	synchronized (storeMutex)
    	{
    		loadRouteDataStore();
    		SptRouteStoreInfo storeInfo = getRouteStoreInfo(isForwardRoute);
    		
    		int keySPT = createNewRouteDataKey();
            Integer key = new Integer(keySPT);
            routeStore.put(key,pathTree.getBinData());
              
            SptTreeStoreInfo treeInfo = new SptTreeStoreInfo();
            treeInfo.setStartEdgeIndex(pathTree.getStartEdgeIndex());
            treeInfo.setEndEdgeIndex(pathTree.getEndEdgeIndex());
            treeInfo.setStoreeIndex(keySPT);
            treeInfo.setUncompressedLen(pathTree.getUncompressedLen());
            treeInfo.setBoundingBox(pathTree.getBoundingBox());
                  
            storeInfo.getPathTreeStoreInfos().addElement(treeInfo);
    	}
    }
    
    void clearRouteStoreInfo(boolean isForwardRoute)
    {
        synchronized (storeMutex)
        {
        	if (routeStore == null) return;
        	
        	SptRouteStoreInfo storeInfo = getRouteStoreInfo(isForwardRoute);
        	
        	if (storeInfo.getMultiRouteStoreKey() != -1)
    			routeStore.remove(new Integer(storeInfo.getMultiRouteStoreKey()));
    		
    		for (int i = 0; i < storeInfo.getMapSectionStoreKeys().size(); i++)
    		{
    		    Integer key = (Integer)storeInfo.getMapSectionStoreKeys().elementAt(i);
    		    routeStore.remove(key);
    		}
    		
    		for (int i = 0; i < storeInfo.getPathTreeStoreInfos().size(); i++)
    		{
    			SptTreeStoreInfo treeStoreInfo = (SptTreeStoreInfo)storeInfo.getPathTreeStoreInfos().elementAt(i);
    			routeStore.remove(new Integer(treeStoreInfo.getStoreIndex()));
    		}
    		
    		storeInfo.clear();
        }
    }
    
    void clearAllData()
    {
        synchronized (storeMutex)
        {
        	if (routeStore != null) routeStore.clear();
        	if (routeIndexStore != null) routeIndexStore.clear();
        	
        	forwardRouteStoreInfo.clear();
        	reverseRouteStoreInfo.clear();
        	
            isRouteStoreLoaded = false;
        }
    }
   
    //////////////////////////////////////////////////////////////////////////////////////////////
    
    private boolean storeRouteInfo(SptRouteInfo routeInfo, boolean isForwardRoute)
    {
        // check route data integrity
        if (routeInfo == null || routeInfo.dest == null || !routeInfo.isRouteCompleted())
        {
        	clearRouteStoreInfo(isForwardRoute);
            return false;
        }
        
        SptRouteStoreInfo storeInfo = getRouteStoreInfo(isForwardRoute);
            
        storeInfo.setDest(routeInfo.dest);
        if (routeInfo.dest != null)
        {
            storeInfo.setLatDest(routeInfo.dest.getLat());
            storeInfo.setLonDest(routeInfo.dest.getLon());
        }
            
        storeInfo.setLatOrigin(routeInfo.latFrom);
        storeInfo.setLonOrigin(routeInfo.lonFrom);
        
        // save route store summary info
        loadRouteIndexStore();

        int routeStoreIndexKey = createNewRouteIndexKey();
        storeInfo.setRouteIndexKey(routeStoreIndexKey);
        routeIndexStore.put(new Integer(routeStoreIndexKey),
        		            SerializableManager.getInstance().getSptDataSerializable().toBytes(storeInfo));
        
        return true;
    }
    
    void storeRoutesInfo(SptRouteInfo forwardRouteInfo, SptRouteInfo reverseRouteInfo)
    {
        synchronized (storeMutex)
        {  
            // if route ID < 0, should return 
            
            // avoid store cached route data more than once
            if (forwardRouteInfo == null && reverseRouteInfo == null)
                return;    
            
            routeIndexStore.clear();
            
            boolean isForwardRouteStored = storeRouteInfo(forwardRouteInfo, true);
            boolean isReverseRouteStored = storeRouteInfo(reverseRouteInfo, false);
            
            if (isForwardRouteStored || isReverseRouteStored)
            {
                routeStore.save();
                routeIndexStore.save();
            }
            else
            {
                routeStore.clear();
                routeIndexStore.clear();
            }
            
            // clean up
            this.isRouteStoreLoaded = false;
            this.routeStoreInfos = null;
        }
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    void loadRouteDataStore()
    {
    	if (! isRouteStoreLoaded)
    	{	
	        synchronized (storeMutex)
	        {
	            if (! isRouteStoreLoaded)
	            {
	            	if (routeStore == null)
	            		routeStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, 
	            				                                                   "route_data");
	            	
	                routeStore.load();
	                isRouteStoreLoaded = true;
	            }
	        }
    	}    
    }
    
    void loadRouteIndexStore()
    {
    	if (routeStoreInfos == null)
    	{	
	        synchronized (storeMutex)
	        {
	            if (routeStoreInfos == null)
	            {    
	            	if (routeIndexStore == null)
	            	    routeIndexStore = TnPersistentManager.getInstance().createStore(TnPersistentManager.RMS_CRUMB, 
	            	    		                                                        "route_index_data");
	            		
	                routeIndexStore.load();
	                
	                routeStoreInfos = new Vector();
	                Enumeration keys = routeIndexStore.keys();
	                while (keys.hasMoreElements())
	                {
	                    Integer key = (Integer)keys.nextElement();
	                    byte[] data = routeIndexStore.get(key);
	                    
	                    SptRouteStoreInfo info = getRouteSerilizable().createRouteStoreInfo(data);
	                    info.setRouteIndexKey(key.intValue());
	                    routeStoreInfos.addElement(info);
	                }
	            }
	        }
    	}//end synchronized  
    }
    
    ISptDataSerializable getRouteSerilizable()
    {
    	return SptManager.getInstance().getSptDataSerializable();
    }
    
    boolean loadRouteInfo(Stop dest, TnLocation[] fixes)
    {
        synchronized (storeMutex)
        {
            loadRouteIndexStore();
            loadRouteDataStore();
        
            if (dest == null) return false;
            
            for (int i=0; i < this.routeStoreInfos.size(); i++)
            {
                SptRouteStoreInfo info = (SptRouteStoreInfo)routeStoreInfos.elementAt(i);
                if (info.getLatDest() == dest.getLat() && info.getLonDest() == dest.getLon())
                {
                    return true;   
                }
            }
        }
                 
        return false;
    }
    
    byte[] loadRouteData(Stop dest, TnLocation[] fixes)
    {
        synchronized (storeMutex)
        {
            loadRouteIndexStore();
            loadRouteDataStore();
            
            if (dest == null) return null;
            
            for (int i=0; i < this.routeStoreInfos.size(); i++)
            {
                SptRouteStoreInfo info = (SptRouteStoreInfo)routeStoreInfos.elementAt(i);
                
                if (info.getLatDest() == dest.getLat() && info.getLonDest() == dest.getLon())
                {
                    int key = info.getMultiRouteStoreKey();
                    return routeStore.get(new Integer(key));
                }
            }
        }
                 
        return null;
    }
    
    Vector loadRouteMapSections(Stop dest, TnLocation[] fixes)
    {
        synchronized (storeMutex)
        {
            loadRouteIndexStore();
            loadRouteDataStore();
            
            if (dest == null) return null;
            
            for (int i=0; i < this.routeStoreInfos.size(); i++)
            {
                SptRouteStoreInfo info = (SptRouteStoreInfo)routeStoreInfos.elementAt(i);
                
                if (info.getLatDest() == dest.getLat() && info.getLonDest() == dest.getLon() &&
                        info.getMapSectionStoreKeys().size() > 0)
                {
                    Vector mapSections = new Vector();
                    for (int j=0; j <info.getMapSectionStoreKeys().size(); j++)
                    {
                        Integer key = (Integer)info.getMapSectionStoreKeys().elementAt(j);
                        
                        byte[] data = routeStore.get(key);
                        mapSections.addElement(data);
                    }
                    
                    return mapSections;
                } 
             }
         }
                   
         return null;       
    }
    
    Vector loadShortestPathTrees(Stop dest, TnLocation[] fixes)
    {
        synchronized (storeMutex)
        {
            loadRouteIndexStore();
            loadRouteDataStore();
            
            if (dest == null) return null;
            
            for (int i=0; i < this.routeStoreInfos.size(); i++)
            {
                SptRouteStoreInfo info = (SptRouteStoreInfo)routeStoreInfos.elementAt(i);
                
                if (info.getLatDest() == dest.getLat() && info.getLonDest() == dest.getLon() &&
                        info.getPathTreeStoreInfos().size() > 0)
                {
                    Vector trees = new Vector();
                    for (int j=0; j <info.getPathTreeStoreInfos().size(); j++)
                    {
                        SptTreeStoreInfo treeInfo = (SptTreeStoreInfo)info.getPathTreeStoreInfos().elementAt(j);
                        byte[] data = routeStore.get(new Integer(treeInfo.getStoreIndex()));
                        if (data != null)
                        {
                            treeInfo.setBinData(data);
                            trees.addElement(treeInfo);
                        }
                    }
                    
                    return trees;
                } //end if 
            }//end for 
         }
                  
         return null;
    }
    
    private int createNewRouteIndexKey()
    {
        synchronized (storeMutex)
        {
        	return ROUTE_DATA_INDEX_KEY ++;
        }
    }
    
    private int createNewRouteDataKey()
    {
        synchronized (storeMutex)
        {
            return ROUTE_DATA_KEY ++;
        }
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    
    Vector getNavigatableAddress()
    {
        Vector addresses = new Vector();
        
        synchronized (storeMutex)
        {
            loadRouteIndexStore();
            loadRouteDataStore();
            
            for (int i=0; i < this.routeStoreInfos.size(); i++)
            {
                SptRouteStoreInfo info = (SptRouteStoreInfo)routeStoreInfos.elementAt(i);
            
                if (info.getDest() != null)
                {
                    addresses.addElement(info.getDest());
                }    
            }
        }
        
        return addresses;
    }  
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
}
