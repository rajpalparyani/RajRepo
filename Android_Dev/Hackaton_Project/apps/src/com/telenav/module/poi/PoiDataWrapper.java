/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiDataWrapper.java
 *
 */
package com.telenav.module.poi;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.datatypes.nav.NavState;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-12-28
 */
public class PoiDataWrapper
{
    protected Vector addressesData = new Vector(); 
    protected Vector backupAddressesData = new Vector(); //backup data while do network requesting/handling.
    protected Vector sponsoredAddressesData = new Vector();
    protected Vector normalAddressesData = new Vector();

    //for did you mean flow.
    protected Vector multiMatchResults = new Vector();
    
    Hashtable poiId2IndexMapping = new Hashtable();
    
    protected int currNormalPoiIndex;
    protected int currSearchType;
    protected int currSearchFromType;
    protected int currAlongRouteType;
    protected int currSortType;
    protected int currCategoryId;
    protected int currPageNumber;
    protected int currPageSize;
    
    protected int originSelectedIndex = -1;
    protected int selectedIndex;
    protected int totalCount;
    protected int totalCountServerReturned;
    protected int inputType;
    protected int sponsorNum = 1; //the number of sponsor returned when make the POI request. default to 1.
    
    protected String searchUid;
    protected String currKeyword = "";
    protected String showText = "";
    protected Stop currAnchorStop;
    protected Stop currDestStop;
    protected NavState currNavState;
    
    protected boolean hasMorePoi;
    protected boolean isDoingRequest;
    protected boolean isNeedUserSelect;
    protected boolean isMostPopularSearch = false;
    protected Hashtable normalIndex = new Hashtable();

    public PoiDataWrapper(String searchUid)
    {
        this.searchUid = searchUid;
    }
    
    public String getSearchUid()
    {
        return this.searchUid;
    }
    
    public Address getAddress(int index)
    {
        Address addr = null;
        Vector poiDataVector = this.getAvaliableData();
        
        if (index >= 0 && poiDataVector != null && index < poiDataVector.size())
        {
            addr = (Address) poiDataVector.elementAt(index);
        }
       
        return addr;
    }
    
    public int getAddressSize()
    {
        int size = 0;
        
        Vector poiDataVector = this.getAvaliableData();
        if(poiDataVector != null)
            size = poiDataVector.size();
        
        return size;
    }
    
    public int getOriginSelectedIndex()
    {
        return originSelectedIndex;
    }

    public void setOriginSelectedIndex(int originSelectedIndex)
    {
        this.originSelectedIndex = originSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        if (selectedIndex < 0)
        {
            Address address = this.getAddress(0);
            if (address.getPoi() != null
                    && address.getPoi().getType() == Poi.TYPE_SPONSOR_POI)
            {
                this.selectedIndex = 1;
            }
            else
            {
                this.selectedIndex = 0;
            }
        }
        else
        {
            this.selectedIndex = selectedIndex;
        }
    }
    
    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }
        
    public int getNormalAddressSize()
    {
        if(normalAddressesData == null)
            return -1;
        
        return normalAddressesData.size();
    }
    
    public Address getNormalAddress(int index)
    {
        Address address = null;
        if(index >= 0 && normalAddressesData != null && index < normalAddressesData.size())
        {
            address = (Address)normalAddressesData.elementAt(index);
        }
        return address;
    }
    
    public Address getSelectedAddress()
    {
        Address selectedAddr = null;
        Vector poiDataVector = this.getAvaliableData();
        if(selectedIndex >= 0 && selectedIndex < poiDataVector.size())
        {
            selectedAddr = (Address)poiDataVector.elementAt(selectedIndex);
        }
        return selectedAddr;
    }
    
    public int getSponsoredAddressSize()
    {
        if(sponsoredAddressesData == null)
        {
            return -1;
        }
        return sponsoredAddressesData.size();
    }
    
    public Address getSponsoredAddress(int index)
    {
        Address address = null;
        if(index >= 0 && sponsoredAddressesData != null && index < sponsoredAddressesData.size())
        {
            address = (Address)sponsoredAddressesData.elementAt(index);
        }
         
        return address;
    }
    
    /**
     * Get index for the mixed index.
     * @param address
     * @return
     */
    public int getIndexOfMixedList(Address address)
    {
        int index = -1;
        index = this.getAvaliableData().indexOf(address);
        return index;
    }
    
    public int getIndexOfMixedListByPoiId(String poiId)
    {
        if(poiId == null || poiId.trim().length() == 0)
        {
            return -1;
        }
        Vector addresses = this.getAvaliableData();
        int index = -1;
        for(int i = 0; i < addresses.size(); i++)
        {
            Address address = (Address) addresses.elementAt(i);
            if (address.getPoi() != null && address.getPoi().getBizPoi() != null
                    && poiId.equals(address.getPoi().getBizPoi().getPoiId()))
            {
                index = i;
                break;
            }
        }
        return index;
    }
    
    /**
     * Get index of selected address in the sponsored address vector or normal address vector.
     * @param address
     * @return
     */
    public int getIndexOfNormalList(Address address)
    {
        int index = -1;
        if(address == null || address.getPoi() == null)
        {
            return index;
        }
        if(normalIndex.get(address) != null)
        {
            index = (Integer)normalIndex.get(address);
        }
        return index;
    }
    
    public int getIndexOfSponsoredList(Address address)
    {
       int index = -1;
       if(address.getPoi().getType() == Poi.TYPE_SPONSOR_POI)
       {
           index = sponsoredAddressesData.indexOf(address);
       }
       return index;
    }
    
    public Vector getMultiMatchResults()
    {
        return multiMatchResults;
    }
    
    public void setNormalPoiStartIndex(int startIndex)
    {
        this.currNormalPoiIndex = startIndex;
        this.currPageNumber = this.currNormalPoiIndex / PoiDataRequester.DEFAULT_PAGE_SIZE;
    }
        
    public int getNormalPoiStartIndex()
    {
        return this.currNormalPoiIndex;
    }
    
    public void setSortType(int sortType)
    {
        this.currSortType = sortType;
    }
    
    public int getSortType()
    {
        return currSortType;
    }
    
    public void setCategoryId(int categoryId)
    {
        this.currCategoryId = categoryId;
    }
    
    public int getCategoryId()
    {
        return this.currCategoryId;
    }
    
    public int getSearchFromType()
    {
        return this.currSearchFromType; 
    }
    
    public void setSearchFromType(int searchFromType)
    {
        this.currSearchFromType = searchFromType;
    }
    
    public int getAlongRouteType()
    {
        return this.currAlongRouteType;
    }
    
    public void setAlongRouteType(int alongRouteType)
    {
        this.currAlongRouteType = alongRouteType;
    }
    
    public String getQueryWord()
    {
        return this.currKeyword;
    }
    
    public String getShowText()
    {
        return this.showText;
    }
    
    public void setQueryWord(String queryWord)
    {
        this.currKeyword = queryWord;
        if(this.currKeyword != null && currKeyword.trim().length() != 0)
        {
            this.currCategoryId = -2;
        }
    }
    
    public Stop getAnchorStop()
    {
        return currAnchorStop;
    }
    
    public void setAnchorStop(Stop anchorStop)
    {
        this.currAnchorStop = anchorStop;
    }
    
    public Stop getDestStop()
    {
        return currDestStop;
    }
    
    public void setDestStop(Stop destStop)
    {
        this.currDestStop = destStop;
    }
    
    public void setNavState(NavState navState)
    {
        this.currNavState = navState;
    }
    
    public NavState getNavState()
    {
        return this.currNavState;
    }
    
    public boolean isHasMorePoi()
    {
        return this.hasMorePoi;
    }
    
    public void setIsHasMorePoi(boolean hasMorePoi)
    {
        this.hasMorePoi = hasMorePoi;
    }
    
    public void addStopAddress(Address addr)
    {
        this.addressesData.addElement(addr);
        this.poiId2IndexMapping.put(getPoiId(addr),addressesData.indexOf(addr));
    }
    
    private String getPoiId(Address addr)
    {
        String id = "";
        if (addr.getPoi() == null)
        {
            return id;
        }

        if (addr.getPoi().getBizPoi() == null)
        {
            return id;
        }

//        id = addr.getPoi().getBizPoi().getPoiId();
        id = String.valueOf(addr.getId());//TODO
        return id;
    }
    
    public void addNormalAddr(Address addr)
    {
        normalIndex.put(addr, this.getNormalAddressSize());
        this.normalAddressesData.addElement(addr);
    }
    
    public void addSponsoredAddr(Address addr)
    {
        this.sponsoredAddressesData.addElement(addr);
    }
    
    public int getSearchType()
    {
        return this.currSearchType;
    }
    
    public void setSearchType(int searchType)
    {
        this.currSearchType = searchType;
    }
    
    public int getPageNumber()
    {
        return this.currPageNumber;
    }
    
    public int getPageSize()
    {
        return this.currPageSize;
    }
    
    public void setSearchArgs(int index, int searchType, int searchFromType, int alongRouteType, int sortType, int categoryId, int pageNum,
            int pageSize, String searchKeyWord, String showText, Stop anchorStop, Stop destStop, NavState navState)
    {
        setSearchArgs(index, searchType, searchFromType, alongRouteType, sortType, categoryId, pageNum,
            pageSize, searchKeyWord, showText, anchorStop, destStop, navState, IOneBoxSearchProxy.TYPE_INPUTTYPE_ANY, 1);
    }
    
    public void setSearchArgs(int index, int searchType, int searchFromType, int alongRouteType, int sortType, int categoryId, int pageNum,
            int pageSize, String searchKeyWord, String showText, Stop anchorStop, Stop destStop, NavState navState, int inputType)
    {
        setSearchArgs(index, searchType, searchFromType, alongRouteType, sortType, categoryId, pageNum,
            pageSize, searchKeyWord, showText, anchorStop, destStop, navState, inputType, 1);
    }
    
    public void setSearchArgs(int index, int searchType, int searchFromType, int alongRouteType, int sortType, int categoryId, int pageNum,
            int pageSize, String searchKeyWord, String showText, Stop anchorStop, Stop destStop, NavState navState, int inputType, int sponsorNum)
    {
        this.currNormalPoiIndex = index;
        this.currSearchType = searchType;
        this.currSearchFromType = searchFromType;
        this.currAlongRouteType = alongRouteType;
        this.currSortType = sortType;
        this.currCategoryId = categoryId;
        this.currPageNumber = pageNum;
        this.currPageSize = pageSize;
        this.currKeyword = searchKeyWord;
        this.showText = showText;
        this.currAnchorStop = anchorStop;
        this.currDestStop = destStop;
        this.currNavState = navState;
        this.inputType = inputType;
        this.sponsorNum = sponsorNum;
    }
    
    public boolean isNeedUserSelection()
    {
        return isNeedUserSelect;
    }
    
    public void setIsNeedUserSelection(boolean isNeedUserSelection)
    {
        this.isNeedUserSelect = isNeedUserSelection;
    }
    
    public void setIsMostPopularSearch(boolean isPopularSearch)
    {
    	isMostPopularSearch = isPopularSearch;
    }
    
    public boolean isMostPopularSearch()
    {
    	return isMostPopularSearch;
    }
    
    public void clearCache()
    {
        addressesData.removeAllElements();
        backupAddressesData.removeAllElements();
        multiMatchResults.removeAllElements();
        normalAddressesData.removeAllElements();
        normalIndex.clear();
        sponsoredAddressesData.removeAllElements();
        poiId2IndexMapping.clear();
    }
    
    public void clearAll()
    {
        clearCache();
        currNavState = null;
        currDestStop = null;
        currAnchorStop = null;
        currNormalPoiIndex = 0;
        currSearchType = 0;
        currSortType = 0;
        currCategoryId = -2;
        currSearchFromType = 0;
        currAlongRouteType = 0;
        currKeyword = "";
        showText = "";
        currPageNumber = 0;
        selectedIndex = 0;
        isMostPopularSearch = false;
    }
    
    public void mixAddress()
    {
        addressesData.removeAllElements();
        int size = normalAddressesData.size();
        for (int i = 0; i < size; i++)
        {
            if (i % PoiDataRequester.DEFAULT_PAGE_SIZE == 0)
            {
                int j = i / PoiDataRequester.DEFAULT_PAGE_SIZE;
                
                if (j >= 0 && j < sponsoredAddressesData.size())
                {
                    Address addr = (Address)sponsoredAddressesData.elementAt(j);
                    Poi poi = addr.getPoi();
                    if(poi.getType() == Poi.TYPE_SPONSOR_POI)
                    {
                        addressesData.addElement(sponsoredAddressesData.elementAt(j));
                        this.poiId2IndexMapping.put(getPoiId((Address) sponsoredAddressesData.elementAt(j)),addressesData.indexOf(sponsoredAddressesData.elementAt(j)));
                    }
                }
            }
            addressesData.addElement(normalAddressesData.elementAt(i));
            this.poiId2IndexMapping.put(getPoiId((Address) normalAddressesData.elementAt(i)),addressesData.indexOf(normalAddressesData.elementAt(i)));
        }
    }

    public boolean isHasSponsored()
    {
        if(sponsoredAddressesData != null && sponsoredAddressesData.size() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isDoingRequest()
    {
        return this.isDoingRequest;
    }
    
    public void setIsDoingRequest(boolean isDoingRequest)
    {
        this.isDoingRequest = isDoingRequest;
    }

    public void backupData()
    {
        this.backupAddressesData.removeAllElements();
        int size = addressesData.size();
        for(int i = 0; i < size; i ++)
        {
            backupAddressesData.addElement(addressesData.elementAt(i));
        }
        
    }
    
    public int getPageNumber(int index)
    {
        if (sponsoredAddressesData.size() > 0)
        {
            return index / (getPageSize() + 1);
        }
        else
        {
            return index / getPageSize();
        }
    }
    
    public int getPageIndex(int index)
    {
        if (sponsoredAddressesData.size() > 0)
        {
            return index % (getPageSize() + 1);
        }
        else
        {
            return index % getPageSize();
        }
    }
    

    public int getPageSizeWithAds()
    {
        if (sponsoredAddressesData.size() > 0)
        {
            return getPageSize() + 1;
        }
        else
        {
            return getPageSize();
        }
    }
    
    protected Vector getAvaliableData()
    {
        Vector poiDataVector = null;
        if (!this.isDoingRequest)
        {
            poiDataVector = addressesData;
        }
        else
        {
            poiDataVector = backupAddressesData;
        }
        return poiDataVector;
    }
    
    /**
     * Get total normal poi count;
     * @return
     */
    public int getTotalNormalCount()
    {
        return this.totalCount; 
    }
    
    /**
     * Set total normal poi count();
     * @param totalCount
     */
    public void setTotalNormalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }
    
    public int getTotalCountServerReturned()
    {
        return this.totalCountServerReturned;
    }

    public void setTotalCountServerReturned(int totalCountServerReturned)
    {
        this.totalCountServerReturned = totalCountServerReturned;
    }
    
    public PoiDataWrapper cloneInstance(String searchUid)
    {
        PoiDataWrapper instance = new PoiDataWrapper(searchUid);
        instance.setSearchArgs(0, currSearchType, currSearchFromType, currAlongRouteType, currSortType, currCategoryId, 0, currPageSize,
            currKeyword, showText, currAnchorStop, currDestStop, currNavState);
        return instance;
    }
    
    /**
     * @return the inputType
     */
    public int getInputType() {
        return inputType;
    }
    
    
    public int getPoiIndexById(String poiId)
    {
        if (this.poiId2IndexMapping.containsKey(poiId))
        {
            return ((Integer) this.poiId2IndexMapping.get(poiId)).intValue();
        }
        
        return -1;
    }
    
    public PoiDataPublisher getPublisher()
    {
        PoiDataPublisher publisher = new PoiDataPublisher();
        Vector temp = (Vector)this.getAvaliableData();
        
        if (temp != null)
        {
            publisher.addressesData = (Vector)this.getAvaliableData().clone();
        }
        
        if (this.normalIndex != null)
        {
            publisher.normalIndex = (Hashtable)this.normalIndex.clone();
        }
        
        publisher.currAnchorStop = this.currAnchorStop;
        publisher.currCategoryId = this.currCategoryId;
        publisher.currDestStop = this.currDestStop;
        publisher.searchType = this.currSearchType;
        publisher.searchAlongType = this.currAlongRouteType;
        
        return publisher;
    }
    
    public class PoiDataPublisher
    {
        protected Vector addressesData;
        protected Hashtable normalIndex;
        protected Stop currAnchorStop;
        protected Stop currDestStop;
        protected int currCategoryId;
        protected int searchType;
        protected int searchAlongType;
        
        public PoiDataPublisher()
        {
            
        }
        
        public Address getAddress(int index)
        {
            Address addr = null;

            if (index >= 0 && addressesData != null && index < addressesData.size())
            {
                addr = (Address) addressesData.elementAt(index);
            }

            return addr;
        }
        
        public int getAddressSize()
        {
            int size = 0;
            
            if(addressesData != null)
            {
                size = addressesData.size();
            }
            
            return size;
        }
        
        public int getIndexOfNormalList(Address address)
        {
            int index = -1;
            if(address == null || address.getPoi() == null)
            {
                return index;
            }
            if(normalIndex.get(address) != null)
            {
                index = (Integer)normalIndex.get(address);
            }
            return index;
        }
        
        public Stop getAnchorStop()
        {
            return currAnchorStop;
        }
        
        public Stop getCurrDestStop()
        {
            return currDestStop;
        }
        
        public int getCategoryId()
        {
            return this.currCategoryId;
        }
        
        public int getSearchType()
        {
            return this.searchType;
        }
        
        public int getSearchAlongType()
        {
            return this.searchAlongType;
        }
    }
}

