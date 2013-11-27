/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddressDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.ac.AddressListFilter;
import com.telenav.module.dashboard.DashboardManager;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.persistent.TnStore;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sort.IComparable;
import com.telenav.sort.SortAlgorithm;
import com.telenav.util.PrimitiveTypeCache;

/**
 * This is porting from core-ri.
 * 
 */
public class AddressDao extends AbstractDao
{
    public static final String RECEIVED_ADDRESSES_CATEGORY = "Received Addresses";
    public static final String FAVORITE_ROOT_CATEGORY = "My Favorites";

    final static private int KEY_FAVORITES = 1;
    final static private int KEY_RECENTS = 2;
    final static private int KEY_FAVORITE_CATALOG = 3;
    final static private int KEY_RATED_POI = 4;
    final static private int KEY_DEFAULT_ORIGIN = 5;
    final static private int KEY_STOPS_SYNC_TIME = 6;
    final static private int KEY_RECENT_SEARCH = 7;
    final static private int KEY_RECENT_ADDRESS_KEYWORD = 8;
    final static private int KEY_RECENT_ADDRESS_KEYWORD_INITIAL = 9;
	final static private int KEY_B_IS_MIGRATION_SUCC = 10;
	final static private int KEY_B_IS_MIGRATION_IN_PROGRESS = 11;
	
	
    final static private int MAX_FAV_SIZE = 200;// Fix bug 41654, 41655.
    final static private int MAX_RECENT_SIZE = 200; //Fix bug TNANDROID-5157
    // synchronize favorites and recent address in one shot
    public static boolean isaddressSynchronized = false;

    protected int favoritesSize = 200;

    protected int recentSize = 100;

    protected TnStore cache;

    protected Vector favoriteAddress = new Vector();

    protected Vector recentAddress = new Vector();

    protected Vector favoriteCatalog = new Vector();

    protected Vector recentSearch = new Vector();
    
    protected Vector recentAddressKeyword = new Vector();
    
    protected Vector recentAddressKeywordInitial = new Vector();

    protected StringList ratedPoiIdNode; // xubin fix 9110

    protected Address defualtOrigin;

    protected StringList stopsSyncTimeNode;
    
    protected Object sortMutex = new Object();

    protected boolean isLoaded = false;
    
    private Vector airportsName = new Vector();

    private int airportTextFieldMaxLength = 100;

    private static Object airportMutex = new Object();

    private boolean isAirPortListInitialized = false;

    private TnLocation lastLocation;

    private int minimumDistance = 1000;
    
    private ComparableAddress[] airportStops;
    
    private boolean isHomeUpdate = false;
    private boolean isOfficeupdate = false;
    
    private boolean isMigrationSucc = false;
    private boolean isMigrationInProgress = false;

    public AddressDao(TnStore cache)
    {
        this.cache = cache;

        init();
    }

    private void init()
    {
        byte[] stopsSyncTimeData = this.cache.get(KEY_STOPS_SYNC_TIME);
        if (stopsSyncTimeData != null)
        {
            stopsSyncTimeNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(stopsSyncTimeData);
        }
    }

    public long getSyncTime(boolean isFavorites)
    {
        if (stopsSyncTimeNode != null && stopsSyncTimeNode.size() > 0)
        {
            if (isFavorites)
            {
                return Long.parseLong(stopsSyncTimeNode.elementAt(0));
            }
            else
            {
                if(stopsSyncTimeNode.size() > 1)
                {
                    return Long.parseLong(stopsSyncTimeNode.elementAt(1));
                }
            }
        }
        
        // return 0;
        // modify by zdong
        return -1;
    }

    public void storeSyncTime(boolean isFavorites, long syncTime)
    {
        long tFav = 0;
        long tRs = 0;
        if (stopsSyncTimeNode != null)
        {
            if (isFavorites)
            {
                if(stopsSyncTimeNode.size() > 0)
                {
                    tFav = Long.parseLong(stopsSyncTimeNode.elementAt(0));
                }
            }
            else
            {
                if(stopsSyncTimeNode.size() > 1)
                {
                    tRs = Long.parseLong(stopsSyncTimeNode.elementAt(1));
                }
            }

        }

        if (isFavorites)
        {
            tFav = syncTime;
        }
        else
        {
            tRs = syncTime;
        }

        stopsSyncTimeNode = new StringList();
        stopsSyncTimeNode.add(tFav + "");
        stopsSyncTimeNode.add(tRs + "");

        this.cache.put(KEY_STOPS_SYNC_TIME, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(stopsSyncTimeNode));
    }

    public void setHomeAddress(Stop stop)
    {
        setHomeAddress(stop, false);
    }
    
    public void setHomeAddress(Stop stop, boolean isFromCloud)
    {
        if (!isFromCloud)// if it's from cloud we do not need upload it. 
        {
            //[DB] It should be a reset method when home/work is uploaded.
            //However, the case user change home/work is not common.
            //Add clear logic need to consider network status.
            //I would like to keep the logic simply.
            isHomeUpdate = true;
        }
        Stop homeAddress = getHomeAddress();
        DaoManager.getInstance().getExpressAddressDao().setHomeAddress(stop);
        if(!isSameStop(homeAddress, stop))
        {
            DashboardManager.getInstance().notifyUpdateEta();
        }
    }

    public Stop getHomeAddress()
    {
        Stop stop = DaoManager.getInstance().getExpressAddressDao().getHomeAddress();
        return stop;
    }

    public void setOfficeAddress(Stop stop)
    {
        setOfficeAddress(stop, false);
    }

    
    public void setOfficeAddress(Stop stop, boolean isFromCloud)
    {
        if (!isFromCloud)// if it's from cloud we do not need upload it. 
        {
            //[DB] It should be a reset method when home/work is uploaded.
            //However, the case user change home/work is not common.
            //Add clear logic need to consider network status.
            //I would like to keep the logic simply.
            isOfficeupdate = true;
        }
        
        Stop officeStop = getOfficeAddress();
        DaoManager.getInstance().getExpressAddressDao().setOfficeAddress(stop);
        if(!isSameStop(officeStop, stop))
        {
            DashboardManager.getInstance().notifyUpdateEta();
        }
    }
    
    public void clearOfficeAddress()
    {
        DaoManager.getInstance().getExpressAddressDao().clearOffice();
        DashboardManager.getInstance().notifyUpdateEta();
    }
    
    public void clearHomeAddress()
    {
        DaoManager.getInstance().getExpressAddressDao().clearHome();
        DashboardManager.getInstance().notifyUpdateEta();
    }

    
    public void setHomeOfficeAddress(Stop homeStop, boolean isHomeFromCloud, Stop officeStop, boolean isOfficeFromCloud, boolean isBackupDao)
    {
        boolean needNotifyEta = false;
        if(homeStop != null)
        {
            if (!isHomeFromCloud)// if it's from cloud we do not need upload it. 
            {
                //[DB] It should be a reset method when home/work is uploaded.
                //However, the case user change home/work is not common.
                //Add clear logic need to consider network status.
                //I would like to keep the logic simply.
                isHomeUpdate = true;
            }
            Stop currentHomeStop = getHomeAddress();
            DaoManager.getInstance().getExpressAddressDao().setHomeAddress(homeStop);
            if(!isSameStop(currentHomeStop, homeStop))
            {
                needNotifyEta = true;
            }
        }

        if(officeStop != null)
        {
            if (!isOfficeFromCloud)// if it's from cloud we do not need upload it. 
            {
                //[DB] It should be a reset method when home/work is uploaded.
                //However, the case user change home/work is not common.
                //Add clear logic need to consider network status.
                //I would like to keep the logic simply.
                isOfficeupdate = true;
            }
            Stop currentOfficeStop = getOfficeAddress();
            DaoManager.getInstance().getExpressAddressDao().setOfficeAddress(officeStop);
            if(!isSameStop(currentOfficeStop, officeStop))
            {
                needNotifyEta = true;
            }
        }
        
        if(needNotifyEta && !isBackupDao)
        {
            DashboardManager.getInstance().notifyUpdateEta();
        }
    }
    
    public Stop getOfficeAddress()
    {
        Stop stop = DaoManager.getInstance().getExpressAddressDao().getOfficeAddress();

        return stop;
    }
    
    public boolean isOfficeUpdated()
    {
        return isOfficeupdate;
    }
    
    public boolean isHomeUpdated()
    {
        return isHomeUpdate;
    }

    public void setHomeWorkNeedSync(boolean needSync)
    {
        isHomeUpdate = needSync;
        isOfficeupdate = needSync;
    }
    
    public void addRecentSearch(String recentString)
    {
        if (recentString == null || recentString.trim().length() == 0)
            return;

        for(int i = recentSearch.size()-1; i >= 0; i--)
        {
            if(recentString.equalsIgnoreCase((String)recentSearch.get(i)))
            {
                recentSearch.remove(i);
            }
        }
        recentSearch.insertElementAt(recentString, 0);
    }
    
    public void addRecentAddressKeyword(String recentString, boolean isInitial)
    {
        if (recentString == null || recentString.trim().length() == 0)
            return;
        
        String formatString = recentString.toLowerCase();
        Vector keywords = null;
        if(isInitial)
        {
            keywords = recentAddressKeywordInitial;
        }
        else
        {
            keywords = recentAddressKeyword;
        }
        if (keywords.contains(recentString) || keywords.contains(formatString))
        {
            keywords.removeElement(recentString);/////?????
        }
        keywords.insertElementAt(recentString, 0);
    }

    public Vector getRecentSearch()
    {
        return this.recentSearch;
    }
    
    public Vector getRecentAddressKeyword(boolean isInitial)
    {
        if(isInitial)
        {
            return this.recentAddressKeywordInitial;
        }
        else
        {
            return this.recentAddressKeyword;
        }
    }
    
    protected void toRecentSearchNode(StringList node)
    {
        if (node == null)
            return;
        int size = this.recentSearch.size();
        for (int i = 0; i < size; i++)
        {
            node.add((String) recentSearch.elementAt(i));
        }

    }
    
    protected void toRecentAddressKeywordNode(StringList node, boolean isInitial)
    {
        if (node == null)
            return;
        Vector keywords = null;
        if(isInitial)
        {
            keywords = recentAddressKeywordInitial;
        }
        else
        {
            keywords = recentAddressKeyword;
        }
        int size = keywords.size();
        for (int i = 0; i < size; i++)
        {
            node.add((String) keywords.elementAt(i));
        }
        
    }

    protected Vector fromRecentSearchNode(StringList recentSearchNode)
    {
        if (recentSearchNode != null)
        {
            int size = recentSearchNode.size();
            for (int i = size - 1; i >= 0; i--)
            {
                String recentSearchText = recentSearchNode.elementAt(i).toLowerCase();
                recentSearch.addElement(recentSearchText);
            }
        }
        return recentSearch;
    }
    
    protected Vector fromRecentAddressKeywordNode(StringList recentAddressKeywordNode, boolean isInitial)
    {
        Vector keywords = null;
        if(isInitial)
        {
            keywords = recentAddressKeywordInitial;
        }
        else
        {
            keywords = recentAddressKeyword;
        }
        if (recentAddressKeywordNode != null)
        {
            int size = recentAddressKeywordNode.size();
            for (int i = size - 1; i >= 0; i--)
            {
                String recentAddressKeywordText = recentAddressKeywordNode.elementAt(i).toLowerCase();
                
                keywords.addElement(recentAddressKeywordText);
            }
        }
        return keywords;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.framework.address.IAddressManager#getAddresses(int)
     */
    public Vector getAddresses(int type)
    {
        Vector v = null;
        switch (type)
        {
            case Address.TYPE_FAVORITE_POI:
            case Address.TYPE_FAVORITE_STOP:
            {
                if (favoriteAddress != null)
                {
                    v = new Vector();
                    for (int i = 0; i < favoriteAddress.size(); i++)
                    {
                        Address addr = (Address) favoriteAddress.elementAt(i);
                        if (addr.getType() == type)
                        {
                            v.addElement(addr);
                        }

                    }
                }
                break;
            }
            case Address.TYPE_RECENT_STOP:
            case Address.TYPE_RECENT_POI:
            {
                v = new Vector();
                for (int i = 0; i < recentAddress.size(); i++)
                {
                    Address addr = (Address) recentAddress.elementAt(i);
                    if (addr.getType() == type)
                    {
                        v.addElement(addr);
                    }

                }
                break;
            }

        }

        return v;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.framework.address.AddressManager#getaddress(int)
     */
    public Vector getFavorateAddresses()
    {
        if(favoriteAddress == null)
            return null;
        Vector v = new Vector();
        for(int i = 0 ; i < favoriteAddress.size(); i++)
        {
            Address favorite = (Address) favoriteAddress.elementAt(i);
            if(favorite.getCatagories() != null && !favorite.getCatagories().contains(RECEIVED_ADDRESSES_CATEGORY))
            {
                v.addElement(favorite);
            }
        }
        return v;
    }
    
    public Vector getDisplayFavorateAddress()
    {
        boolean isSyncEnabled = MigrationExecutor.getInstance().isSyncEnabled();
        if (favoriteAddress == null)
            return null;
        Vector v = new Vector();
        for (int i = 0; i < favoriteAddress.size(); i++)
        {
            Address favorite = (Address) favoriteAddress.elementAt(i);
            if (favorite.getStatus() != CommonDBdata.DELETED)
            {
                if (!isSyncEnabled && favorite.getCatagories() != null && favorite.getCatagories().contains(RECEIVED_ADDRESSES_CATEGORY))
                {
                    continue;
                }
                v.addElement(favorite);
            }
        }
        return v;
    }
    
    public Address getMatchedFavoriteAddress(Address address, boolean isStrict)
    {
        if (favoriteAddress == null || address == null || address.getStop() == null)
        {
            return null;
        }
        
        for (int i = 0; i < favoriteAddress.size(); i++)
        {
            Address temp = (Address) favoriteAddress.elementAt(i);
//            if (temp.getType() != address.getType())
//                continue;

            if (temp.getStatus() == CommonDBdata.DELETED)
                continue;
            
            //judge if an address is in favorite,
            //ignore the address in received address category.
            if(isFavoriteInCategory(temp, RECEIVED_ADDRESSES_CATEGORY))
                continue;
            
            boolean isSameAddress = isSameAddress(address, temp, isStrict);
            
            if(isSameAddress)
            {
                return temp;
            }
        }
        return null;
    }
    
    public boolean isSameStop(Stop tempStop, Stop newStop)
    {
        if(tempStop != null && newStop != null)
        {
            if (tempStop.getLat() == newStop.getLat()
                    && tempStop.getLon() == newStop.getLon())
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
            if(tempStop == null && newStop == null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    public static boolean isSameAddress(Address address, Address temp, boolean isStrict)
    {
        switch (address.getType())
        {
            case Address.TYPE_FAVORITE_POI:
            case Address.TYPE_RECENT_POI:
            {
                boolean hasSameLabel = false;
                if (((temp.getLabel() == null || temp.getLabel().trim().length() == 0) && (address.getLabel() == null || address.getLabel().trim().length() == 0))
                        || (temp.getLabel() != null && temp.getLabel().equalsIgnoreCase(address.getLabel())))
                {
                    hasSameLabel = true;
                }
                
                boolean hasPoiIdToCompare = address.getPoi() != null
                        && address.getPoi().getBizPoi() != null
                        && address.getPoi().getBizPoi().getPoiId() != null;
                
                Poi poi = temp.getPoi();
                
                boolean hasSamePoiId = false;
                if (hasPoiIdToCompare)
                {
                    boolean isFavoriteAddressHasPoiId = poi != null && poi.getBizPoi() != null
                            && poi.getBizPoi().getPoiId() != null;
                    if (isFavoriteAddressHasPoiId)
                    {
                        hasSamePoiId = poi.getBizPoi().getPoiId().equals(address.getPoi().getBizPoi().getPoiId());
                    }
                }
                
                if (hasSameLabel)
                {
                    if (hasSamePoiId)
                    {
                        return true;
                    }
                    else if (!isStrict || !hasPoiIdToCompare)
                    {
                        // check if they have the same lat/lon
                        if (poi != null && poi.getBizPoi() != null && address.getPoi() != null && address.getPoi().getBizPoi() != null)
                        {
                            Stop tempStop = poi.getBizPoi().getStop();
                            Stop newStop = address.getPoi().getBizPoi().getStop();

                            if (tempStop != null && newStop != null && tempStop.getLat() == newStop.getLat()
                                    && tempStop.getLon() == newStop.getLon())
                            {
                                return true;
                            }
                        }
                    }
                }
                break;
            }
            case Address.TYPE_FAVORITE_STOP:
            case Address.TYPE_RECENT_STOP:
            case Address.TYPE_AIRPORT:
            {
                Stop stop = temp.getStop();
                if(!isStrict && (address.getLabel() == null || address.getLabel().trim().length() == 0))
                {
                    if(stop.equalsIgnoreCase(address.getStop()))
                    {
                        return true;
                    }
                }
                else if (temp.getLabel().equals(address.getLabel()) && stop.equalsIgnoreCase(address.getStop()))
                {
                    return true;
                }
                break;
            }
        }
        
        return false;
    }
    
    public boolean isExistInFavoriteAddress(Address address, boolean isStrict)
    {
        return getMatchedFavoriteAddress(address, isStrict) != null;
    }

    public Vector getFavorateAddresses(String catName, boolean isRoot)
    {
        Vector elements = new Vector();
        if (isRoot)
        {
            // catName = FAVORITE_ROOT_CATEGORY ;
            Vector categories = this.favoriteCatalog;
            for (int i = 0; i < categories.size(); i++)
            {
                FavoriteCatalog cat = (FavoriteCatalog) categories.elementAt(i);
                if (cat.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN
                        || cat.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
                    continue;

                elements.addElement(cat);
            }
        }
        for (int i = 0; i < favoriteAddress.size(); i++)
        {
            Address addr = (Address) favoriteAddress.elementAt(i);
            checkAddressLabel(addr);

            if (addr.getStatus() != CommonDBdata.DELETED && (isFavoriteInCategory(addr, catName) || isRoot))
            {
                elements.addElement(favoriteAddress.elementAt(i));
            }
        }

        return elements;
    }

    public Vector getRootFavorites()
    {
        Vector vector = new Vector();
        for(int i = 0; i < favoriteAddress.size(); i++)
        {
            Address address = (Address) favoriteAddress.elementAt(i);
            Vector category = address.getCatagories();
            boolean isFav=address.getStatus() != CommonDBdata.DELETED && category != null && category.size() == 1 && category.elementAt(0).equals(FAVORITE_ROOT_CATEGORY);
            if(isFav)
                vector.addElement(address);
        }
        return vector;
    }
    
    public void checkAddressLabel(Address addr)
    {
        if(addr == null)
        {
            return;
        }
        
        if ((addr.getLabel() == null || addr.getLabel().length() == 0) && addr.getStop() != null)
        {
            addr.setLabel(addr.getDisplayedText());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.framework.address.AddressManager#getaddress(int)
     */
    public Vector getRecentAddresses()
    {
        return this.recentAddress;
    }

    public Vector getCities(int type)
    {
        Vector address = null;
        switch (type)
        {
            case Address.TYPE_FAVORITE_POI:
            case Address.TYPE_FAVORITE_STOP:
            {
                if (favoriteAddress != null)
                {
                    address = new Vector();
                    for (int i = 0; i < favoriteAddress.size(); i++)
                    {
                        Address addr = (Address) favoriteAddress.elementAt(i);
                        if (addr.getType() == type)
                        {
                            address.addElement(addr.getStop());
                        }

                    }
                }
                break;
            }
            case Address.TYPE_FAVORITE:
            {
                if (favoriteAddress != null)
                {
                    address = new Vector();
                    for (int i = 0; i < favoriteAddress.size(); i++)
                    {
                        Address addr = (Address) favoriteAddress.elementAt(i);
                        address.addElement(addr.getStop());

                    }
                }
                break;
            }
            case Address.TYPE_RECENT_STOP:
            case Address.TYPE_RECENT_POI:
            {
                if (recentAddress != null)
                {
                    address = new Vector();
                    for (int i = 0; i < recentAddress.size(); i++)
                    {
                        Address addr = (Address) recentAddress.elementAt(i);
                        if (addr.getType() == type)
                        {
                            address.addElement(addr.getStop());
                        }

                    }
                }
                break;
            }
            case Address.TYPE_RECENT:
            {
                if (recentAddress != null)
                {
                    address = new Vector();
                    for (int i = 0; i < recentAddress.size(); i++)
                    {
                        Address addr = (Address) recentAddress.elementAt(i);
                        address.addElement(addr.getStop());
                    }
                }
                break;
            }
            case Address.TYPE_ALL:
            {
                address = new Vector();
                if (favoriteAddress != null)
                {
                    for (int i = 0; i < favoriteAddress.size(); i++)
                    {
                        Address addr = (Address) favoriteAddress.elementAt(i);
                        address.addElement(addr.getStop());
                    }
                }
                if (recentAddress != null)
                {
                    for (int i = 0; i < recentAddress.size(); i++)
                    {
                        Address addr = (Address) recentAddress.elementAt(i);
                        address.addElement(addr.getStop());
                    }
                }
                break;
            }

        }

        Vector cities = new Vector();
        if (address != null)
        {
            for (int i = 0; i < address.size(); i++)
            {
                Stop stop = (Stop) address.elementAt(i);
                if (stop.getCity() != null && stop.getProvince() != null)
                {
                    addRecentCity(cities, stop.getCity() + ", " + stop.getProvince());
                }
            }

        }

        return cities;
    }

    public int getAddressSize(int type)
    {

        int size = 0;

        switch (type)
        {
            case Address.TYPE_FAVORITE_POI:
            case Address.TYPE_FAVORITE_STOP:
            {
                if (favoriteAddress != null)
                {
                    for (int i = 0; i < favoriteAddress.size(); i++)
                    {
                        Address addr = (Address) favoriteAddress.elementAt(i);
                        if (addr.getType() == type)
                        {
                            size++;
                        }

                    }
                }
                break;
            }
            case Address.TYPE_FAVORITE:
            {
                if (favoriteAddress != null)
                {
                    size = favoriteAddress.size();
                }
                break;
            }

            case Address.TYPE_RECENT_POI:
            case Address.TYPE_RECENT_STOP:
            {
                if (recentAddress != null)
                {
                    for (int i = 0; i < recentAddress.size(); i++)
                    {
                        Address addr = (Address) recentAddress.elementAt(i);
                        if (addr.getType() == type)
                        {
                            size++;
                        }

                    }
                }
                break;
            }
            case Address.TYPE_RECENT:
            {
                if (recentAddress != null)
                {
                    size = recentAddress.size();
                }
                break;
            }
            case Address.TYPE_ALL:
            {
                if (favoriteAddress != null)
                {
                    size = favoriteAddress.size();
                }
                if (recentAddress != null)
                {
                    size += recentAddress.size();
                }
                break;
            }

        }
        return size;
    }

    public Vector getReceivedAddresses()
    {
        Vector result = new Vector();
        if(favoriteAddress != null)
        {
            for(int i = 0; i < favoriteAddress.size(); i++)
            {
                Address addr = (Address)favoriteAddress.elementAt(i);
                if (isFavoriteInCategory(addr, RECEIVED_ADDRESSES_CATEGORY))
                {
                    result.addElement(addr);
                }
            }
        }
        
        return result;
    }
    
    public void setUserHasRateThePoi(Address address)
    {
        if (this.ratedPoiIdNode == null)
            return;

        Poi poi = address.getPoi();
        if (poi != null && poi.getBizPoi() != null && poi.getBizPoi().getPoiId() != null)
        {
            String poiId = poi.getBizPoi().getPoiId();
            for (int i = 0; i < this.ratedPoiIdNode.size(); ++i)
            {
                if (poiId.equals(this.ratedPoiIdNode.elementAt(i)))
                {
                    poi.setIsRated(true);
                    break;
                }
            }
        }
    }

    public void setUserHasRateThePoi(Vector addresses)
    {
        for (int j = 0; j < addresses.size(); j++)
        {
            Address addr = (Address) addresses.elementAt(j);
            setUserHasRateThePoi(addr);
        }
    }

    public void addAddress(Address address, boolean isReceivedCategoryNeeded)
    {
        synchronized (sortMutex)
        {
            if (address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_RECENT_POI)
                setUserHasRateThePoi(address); // fix 13640
    
            if (!isReceivedCategoryNeeded && isFavoriteInCategory(address, RECEIVED_ADDRESSES_CATEGORY))
            {
                removeAddressFromCategory(address, RECEIVED_ADDRESSES_CATEGORY);
            }
            Vector v = null;
            int maxSize = 0;
    
            switch (address.getType())
            {
                case Address.TYPE_FAVORITE_POI:
                case Address.TYPE_FAVORITE_STOP:
                {
                    int serverDrivenMaxFavSize  = DaoManager.getInstance().getServerDrivenParamsDao().getIntValue(ServerDrivenParamsDao.MAX_FAV_SIZE);
                    if(serverDrivenMaxFavSize  > 0)
                    {
                        maxSize = serverDrivenMaxFavSize ;
                    }
                    else
                    {
                        maxSize = MAX_FAV_SIZE;
                    }
                    v = this.favoriteAddress;
                    break;
                }
                case Address.TYPE_RECENT_POI:
                case Address.TYPE_RECENT_STOP:
                {
                    int serverDrivenMaxRecentSize = DaoManager.getInstance().getServerDrivenParamsDao().getIntValue(ServerDrivenParamsDao.MAX_RECENT_SIZE);
                    if(serverDrivenMaxRecentSize > 0)
                    {
                        maxSize = serverDrivenMaxRecentSize;
                    }
                    else
                    {
                        maxSize = MAX_RECENT_SIZE;
                    }
                    v = this.recentAddress;
    
                    // xubin fix 9032
                    // if( null != address.getStop() && address.getStop().getType() == Stop.STOP_CURSOR_ADDRESS )
                    // {
                    // address.getStop().setType(Stop.STOP_GENERIC);
                    // }
                    // remove Stop.STOP_CURSOR_ADDRESS
                    break;
                }
            }
        // this.updatePoiRatingInfo( address.getPoi() );

        if(v == null)
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "Cannot add the address");
            return;
        }
        boolean isContinue = true;
        for (int i = 0; i < v.size() && isContinue; i++)
        {
    
                Address addr = (Address) v.elementAt(i);
                if (addr.getType() != address.getType())
                    continue;
    
                if (addr.getStatus() == CommonDBdata.DELETED)
                    continue;
    
                switch (addr.getType())
                {
                    case Address.TYPE_FAVORITE_POI:
                        isContinue = false;
                        break;
                    case Address.TYPE_RECENT_POI:
                    {
                        Poi poi = addr.getPoi();
                        if (poi != null && poi.getBizPoi() != null && poi.getBizPoi().getPoiId() != null
                                && address.getPoi() != null && address.getPoi().getBizPoi() != null
                                && address.getPoi().getBizPoi().getPoiId() != null)
                        {
                            boolean hasSameLabel = false;
                            if ((addr.getLabel() == null && address.getLabel() == null)
                                    || (addr.getLabel() != null && addr.getLabel().equalsIgnoreCase(address.getLabel())))
                            {
                                hasSameLabel = true;
                            }
                            if (hasSameLabel && poi.getBizPoi().getPoiId().equals(address.getPoi().getBizPoi().getPoiId()))
                                // fix the bug: 72978 we can not save different poi they have same address in 72978.
                                /*
                                 * || (poi.getAddress() != null // fix bug 29171 && address.getPoi().getAddress() != null && poi
                                 * .getAddress().equalsIgnoreCase( address.getPoi().getAddress()))
                                 */
                            {
                                addr.setCreateTime(address.getCreateTime());
                                v.remove(addr);
                                v.insertElementAt(addr, 0);
                                Logger.log(Logger.INFO, this.getClass().getName(), "RECENT_POI: return here because current address already existed in recentAddress vector with ID :"+ addr.getId());
                                return;
                            }
                        }
                        
                        if (isRecentHomeWorkAddress(address, addr))
                        {
                            addr.setCreateTime(address.getCreateTime());
                            v.remove(addr);
                            v.insertElementAt(addr, 0);
                            return;
                        }
                        
                        break;
                    }
                    case Address.TYPE_FAVORITE_STOP:
                        isContinue = false;
                        break;
                    case Address.TYPE_RECENT_STOP:
                    {
                        Stop stop = addr.getStop();
                        Stop recentStop = address.getStop();
                        if (stop != null && stop.equalsIgnoreCase(recentStop) )
                        {
                            if(addr.getLabel() == null && address.getLabel() == null)
                            {
                                v.remove(addr);
                                v.insertElementAt(addr, 0);
                                Logger.log(
                                    Logger.INFO,
                                    this.getClass().getName(),
                                    "RECENT_STOP: return here because current address already existed in recentAddress vector with ID:"
                                            + address.getId()
                                            + " , lable of two stops are both null");
                                return;
                            }
                            
                            if(addr.getLabel() != null && addr.getLabel().equalsIgnoreCase(address.getLabel()))
                            {
                                addr.setCreateTime(address.getCreateTime());
                                v.remove(addr);
                                v.insertElementAt(addr, 0);
                                Logger.log(Logger.INFO, this.getClass().getName(), "RECENT_STOP: return here because current address already existed in recentAddress vector with ID:"+ address.getId());
                                return;
                            }
                        }
                        
                        if (isRecentHomeWorkAddress(address, addr))
                        {
                            addr.setCreateTime(address.getCreateTime());
                            v.remove(addr);
                            v.insertElementAt(addr, 0);
                            return;
                        }
                        
                        break;
                    }
                }
            }
    
            // add new address
            if (address.getStatus() != CommonDBdata.UNCHANGED)
                address.setStatus(CommonDBdata.ADDED);
            
            if (address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_FAVORITE_STOP)
            {
                if (address.getCatagories() == null || address.getCatagories().size() == 0)
                {
                    Vector vec = new Vector();
                    vec.addElement(FAVORITE_ROOT_CATEGORY);
                    address.setCatagories(vec);
                }
                insertAddressByAlphabeticaOrder(address);
    
                // // rename recent address name
                // for( int i=0; i< recentAddress.size(); ++i )
                // {
                // Address addr = ((Address)recentAddress.elementAt(i));
                // if( addr.getId() == address.getId() )
                // {
                // addr.setLabel(address.getLabel());
                // break;
                // }
                // }
            }
            else
            {
                v.insertElementAt(address, 0);
            }
    
            // check for overflow
            if (v.size() > maxSize)
            {
                v.removeElementAt(v.size() - 1);
            }
        }
    }

    private boolean isRecentHomeWorkAddress(Address from, Address to)
    {
        boolean isMatch = AddressListFilter.isHome(from) || AddressListFilter.isWork(from);
        isMatch = isMatch && from.getStop().equalsIgnoreCase(to.getStop());
        return isMatch;
    }
    
    
    public void load()
    {
        if (isLoaded)
            return;

        byte[] buff = null;

        buff = cache.get(KEY_FAVORITES);
        if (buff != null)
        {
            BytesList list = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(buff);
            this.favoriteAddress = fromNode(list, false, true);
        }

        buff = cache.get(KEY_RECENTS);
        if (buff != null)
        {
            BytesList list = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(buff);
            this.recentAddress = fromNode(list, false, false);
        }

        buff = cache.get(KEY_FAVORITE_CATALOG);
        if (buff != null)
        {
            BytesList list = SerializableManager.getInstance().getPrimitiveSerializable().createBytesList(buff);
            this.favoriteCatalog = fromCatNode(list);
        }

        buff = cache.get(KEY_RATED_POI);
        if (buff != null)
        {
            this.ratedPoiIdNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(buff);
        }

        buff = cache.get(KEY_DEFAULT_ORIGIN);
        if (buff != null)
        {
            this.defualtOrigin = SerializableManager.getInstance().getAddressSerializable().createAddress(buff);
        }

        buff = cache.get(KEY_RECENT_SEARCH);
        if (buff != null)
        {
            StringList recentSearchNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(buff);
            recentSearch = fromRecentSearchNode(recentSearchNode);
        }
        
        buff = cache.get(KEY_RECENT_ADDRESS_KEYWORD);
        if (buff != null)
        {
            StringList recentAddressKeywordNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(buff);
            recentAddressKeyword = fromRecentAddressKeywordNode(recentAddressKeywordNode, false);
        }
        
        buff = cache.get(KEY_RECENT_ADDRESS_KEYWORD_INITIAL);
        if (buff != null)
        {
            StringList recentAddressKeywordNode = SerializableManager.getInstance().getPrimitiveSerializable().createStringList(buff);
            recentAddressKeywordInitial = fromRecentAddressKeywordNode(recentAddressKeywordNode, true);
        }
        
        buff = cache.get(KEY_B_IS_MIGRATION_SUCC);
        if (buff != null)
        {
            isMigrationSucc = Boolean.valueOf(new String(buff));
        }
        
        createReceivedCatalog();

        this.isLoaded = true;
    }

    public void createReceivedCatalog()
    {
        if (!containReceivedCategory())
        {
            this.favoriteCatalog.addElement(createRecivedAdressesCatalog(RECEIVED_ADDRESSES_CATEGORY));
        }
    }
    
    private boolean containReceivedCategory()
    {
        boolean isContained = false;
        int size = favoriteCatalog.size();
        for (int i = 0; i < size; i++)
        {
            FavoriteCatalog catalog = (FavoriteCatalog) favoriteCatalog.get(i);
            if (catalog.getName().equalsIgnoreCase(RECEIVED_ADDRESSES_CATEGORY))
            {
                isContained = true;
                break;
            }
        }
        return isContained;
    }
    
    private FavoriteCatalog createRecivedAdressesCatalog(String receivedCatName)
    {
        FavoriteCatalog cat = new FavoriteCatalog();
        cat.setCreateTime(System.currentTimeMillis());
        cat.setCatType(FavoriteCatalog.TYPE_RESERVERD_CATEGORY);
        cat.setStatus(CommonDBdata.UNCHANGED);
        cat.setName(receivedCatName);
        cat.setId(0);
        return cat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.framework.address.AddressManager#store()
     */
    public void store()
    {
        byte[] buff;
        BytesList node;

        // fix bug 14666
        // if(this.favoriteAddress != null && this.favoriteAddress.size() > 0)
        if (this.favoriteAddress != null)
        {
            node = toNode(this.favoriteAddress);
            buff = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(node);
            cache.put(KEY_FAVORITES, buff);
        }
        // if(this.recentAddress != null && this.recentAddress.size() > 0)
        if (this.recentAddress != null)
        {
            node = toNode(this.recentAddress);
            buff = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(node);
            cache.put(KEY_RECENTS, buff);
        }
        if (this.favoriteCatalog != null && this.favoriteCatalog.size() > 0)
        {
            node = toCatNode(this.favoriteCatalog);
            buff = SerializableManager.getInstance().getPrimitiveSerializable().toBytes(node);
            cache.put(KEY_FAVORITE_CATALOG, buff);
        }
        if (this.ratedPoiIdNode != null)
        {
            cache.put(KEY_RATED_POI, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(this.ratedPoiIdNode));
        }

        if (this.defualtOrigin != null)
        {
            byte[] defaultOriginData = SerializableManager.getInstance().getAddressSerializable().toBytes(this.defualtOrigin);
            cache.put(KEY_DEFAULT_ORIGIN, defaultOriginData);
        }
        else if (this.defualtOrigin == null)
        {
            // when clean the defaultOrigin
            if (cache.get(KEY_DEFAULT_ORIGIN) != null)
            {
                cache.remove(KEY_DEFAULT_ORIGIN);
            }
        }

        if (this.recentSearch != null)
        {
            StringList recentSearchNode = new StringList();
            this.toRecentSearchNode(recentSearchNode);
            cache.put(KEY_RECENT_SEARCH, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(recentSearchNode));
        }
        
        if (this.recentAddressKeyword != null)
        {
            StringList recentAddressKeywordNode = new StringList();
            this.toRecentAddressKeywordNode(recentAddressKeywordNode, false);
            cache.put(KEY_RECENT_ADDRESS_KEYWORD, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(recentAddressKeywordNode));
        }
        
        if (this.recentAddressKeywordInitial != null)
        {
            StringList recentAddressKeywordNode = new StringList();
            this.toRecentAddressKeywordNode(recentAddressKeywordNode, true);
            cache.put(KEY_RECENT_ADDRESS_KEYWORD_INITIAL, SerializableManager.getInstance().getPrimitiveSerializable().toBytes(recentAddressKeywordNode));
        }

        this.cache.save();
        
        DaoManager.getInstance().getExpressAddressDao().store();
    }

    public void clear()
    {
        clear(true);
    }

    public void clear(boolean includeHomeWork)
    {
        if (this.favoriteAddress != null)
        {
            this.favoriteAddress.removeAllElements();
            // this.favoriteAddress = null;
        }

        if (this.favoriteCatalog != null)
        {
            this.favoriteCatalog.removeAllElements();
            // this.favoriteCatalog = null;
            createReceivedCatalog();
        }

        if (this.recentAddress != null)
        {
            this.recentAddress.removeAllElements();
        }

        if (this.recentSearch != null)
        {
            this.recentSearch.removeAllElements();
        }
        
        if (this.recentAddressKeyword != null)
        {
            this.recentAddressKeyword.removeAllElements();
        }
        
        if (this.recentAddressKeywordInitial != null)
        {
            this.recentAddressKeywordInitial.removeAllElements();
        }
        this.favoritesSize = 0;
        this.recentSize = 0;
        this.isLoaded = false;

        this.cache.clear();
        
        stopsSyncTimeNode = null;
        
        if(includeHomeWork)
        {
            DaoManager.getInstance().getExpressAddressDao().clear();
        }
    }
    
    public void addRecentCity(Vector rc, String city)
    {
        for (int i = 0; i < rc.size(); i++)
        {
            String prevCity = (String) rc.elementAt(i);
            if (prevCity.equals(city))
            {
                return;
            }
        }
        rc.addElement(city);
    }

    public Vector getFavoriteCatalog()
    {
        boolean isSyncEnabled = MigrationExecutor.getInstance().isSyncEnabled();
        // we should remove received catalog first
        boolean isReceivedCategoryEmpty =DaoManager.getInstance().getAddressDao(). getReceivedAddresses().size()==0;       
        Vector vec = new Vector();
        if (this.favoriteCatalog != null && this.favoriteCatalog.size() > 0)
        {
            for (int i = 0; i < this.favoriteCatalog.size(); i++)
            {
                FavoriteCatalog cat = (FavoriteCatalog) this.favoriteCatalog.elementAt(i);
                if (!isSyncEnabled && cat.getName().equals(RECEIVED_ADDRESSES_CATEGORY))
                {
                    continue;
                }
                // fix bug http://jira.telenav.com:8080/browse/TNANDROID-5131
                if (cat.getName().equals(RECEIVED_ADDRESSES_CATEGORY) && isReceivedCategoryEmpty)
                {
                    continue;
                }
                vec.addElement(cat);
            }

        }
        return vec;
    }

    private Vector fromNode(BytesList node, boolean isFromServer, boolean isFavorite)
    {
        Vector v = new Vector();

        for (int i = 0; i < node.size(); i++)
        {
            byte[] child = node.elementAt(i);
            Address address = SerializableManager.getInstance().getAddressSerializable().createAddress(child);

            // if the node is from server , need to set the address status to STOP_POI_UNCHANGED
            if (isFromServer)
                address.setStatus(CommonDBdata.UNCHANGED);
            if (isFavorite)
            {
                address.setSource(Address.SOURCE_FAVORITES);
            }
            else
            {
                address.setSource(Address.SOURCE_RECENT_PLACES);
            }
            v.addElement(address);
        }

        return v;
    }

    protected BytesList toNode(Vector v)
    {
        BytesList node = new BytesList();

        for (int i = 0; i < v.size(); i++)
        {
            Address address = (Address) v.elementAt(i);
            node.add(SerializableManager.getInstance().getAddressSerializable().toBytes(address));
        }

        return node;
    }

    protected Vector fromCatNode(BytesList node)
    {
        Vector v = new Vector();

        for (int i = 0; i < node.size(); i++)
        {
            byte[] child = node.elementAt(i);
            FavoriteCatalog cat = SerializableManager.getInstance().getAddressSerializable().createFavoriteCatalog(child);
            v.addElement(cat);
        }

        return v;
    }

    protected BytesList toCatNode(Vector v)
    {
        BytesList node = new BytesList();

        for (int i = 0; i < v.size(); i++)
        {
            FavoriteCatalog cat = (FavoriteCatalog) v.elementAt(i);
            node.add(SerializableManager.getInstance().getAddressSerializable().toBytes(cat));
        }

        return node;
    }

    public int getFavoritesSize()
    {
        return favoritesSize;
    }

    public void setFavoritesSize(int favoritesSize)
    {
        this.favoritesSize = favoritesSize;
    }

    public int getRecentSize()
    {
        return recentSize;
    }

    public void setRecentSize(int recentaddressSize)
    {
        this.recentSize = recentaddressSize;
    }

    public boolean deleteAddress(Address address)
    {
        synchronized (sortMutex)
        {
            Vector v = null;
    
            boolean isReceivedAddress = isFavoriteInCategory(address, RECEIVED_ADDRESSES_CATEGORY);
            
            switch (address.getType())
            {
                case Address.TYPE_FAVORITE_POI:
                case Address.TYPE_FAVORITE_STOP:
                {
                    v = this.favoriteAddress;
                    break;
                }
                case Address.TYPE_RECENT_POI:
                case Address.TYPE_RECENT_STOP:
                    v = this.recentAddress;
                    break;
                default:
                    return false;
    
            }
    
            boolean isContinue = true;
            for (int i = 0; i < v.size() && isContinue; i++)
            {
                Address addr = (Address) v.elementAt(i);
                if (addr.getType() != address.getType())
                    continue;
    
                if (addr.getStatus() == CommonDBdata.DELETED)
                    continue;
                
                boolean isMatch = false;
                if (isReceivedAddress)
                {
                    if(isFavoriteInCategory(addr, RECEIVED_ADDRESSES_CATEGORY))
                    {
                        if (address.getId() == addr.getId())
                        {
                            isMatch = true;
                        }
                    }
                    else
                    {
                        continue;
                    }
                }
                else
                {
                    if(isFavoriteInCategory(addr, RECEIVED_ADDRESSES_CATEGORY))
                    {
                        continue;
                    }
                    
                    isMatch = isSameAddress(address, addr, true);
                }
                
                if (isMatch)
                {
                    if (addr.getStatus() == CommonDBdata.ADDED)
                    {
                        v.removeElementAt(i);
                    }
                    else
                    {
                        addr.setStatus(CommonDBdata.DELETED);
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public void addFavoriteCatalog(FavoriteCatalog catalog)
    {
        catalog.setStatus(CommonDBdata.ADDED);
        insertCatalogByAlphabeticaOrder(catalog);
    }

    public void addFavoriteCatalog(Vector vecCat)
    {

        this.favoriteCatalog = null;
        this.favoriteCatalog = new Vector();
        for (int i = 0; i < vecCat.size(); i++)
        {
            insertCatalogByAlphabeticaOrder((FavoriteCatalog) vecCat.elementAt(i));
        }
    }

    private void insertCatalogByAlphabeticaOrder(FavoriteCatalog cat)
    {
        String catName = cat.getName().toLowerCase();

        for (int j = 0; j < this.favoriteCatalog.size(); j++)
        {
            FavoriteCatalog tmp = (FavoriteCatalog) favoriteCatalog.elementAt(j);
            if (catName.compareTo(tmp.getName().toLowerCase()) <= 0)
            {
                favoriteCatalog.insertElementAt(cat, j);
                return;
            }
        }

        favoriteCatalog.addElement(cat);
    }

    private void insertAddressByAlphabeticaOrder(Address address, Vector order)
    {
        if(address.getLabel() != null)
        {
            String addressLabel = address.getLabel().toLowerCase();
            for (int i = 0; i < order.size(); i++)
            {
                Address tmp = (Address) order.elementAt(i);
                
                if ((tmp.getType() == Address.TYPE_FAVORITE_POI || tmp.getType() == Address.TYPE_FAVORITE_STOP) && tmp.getCatagories().contains(RECEIVED_ADDRESSES_CATEGORY))
                    continue;
                
                if(tmp.getLabel() == null)
                    continue;
                
                if (addressLabel.compareTo(tmp.getLabel().toLowerCase()) <= 0)
                {
                    order.insertElementAt(address, i);
                    return;
                }
    
            }
        }
        
        order.addElement(address);
    }

    private void insertAddressByAlphabeticaOrder(Address address)
    {
        Vector v = null;
        switch (address.getType())
        {
            case Address.TYPE_FAVORITE_POI:
            case Address.TYPE_FAVORITE_STOP:
            {
                v = this.favoriteAddress;
                break;
            }
            case Address.TYPE_RECENT_POI:
            case Address.TYPE_RECENT_STOP:
            {
                v = this.recentAddress;
                break;
            }
        }
        insertAddressByAlphabeticaOrder(address, v);
        // for(int i = 0 ; i< v.size() ; i++)
        // {
        // Address tmp = (Address)v.elementAt(i);
        // if (addressLabel.compareTo(tmp.getLabel().toLowerCase()) <= 0)
        // {
        // v.insertElementAt(address,i);
        // return;
        // }
        //
        // }
        // v.addElement(address);
    }

    // use to favorite catalog

    public boolean isExistInFavoriteCatalog(String label)
    {
        // Vector categories = DataManager.getFavoriteCategories();
        for (int i = 0; i < this.favoriteCatalog.size(); i++)
        {
            FavoriteCatalog category = (FavoriteCatalog) this.favoriteCatalog.elementAt(i);
            if (category.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN
                    || category.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
                continue;

            if (label.equalsIgnoreCase(category.getName()))
                return true;
        }

        return false;
    }

    /**
     * rename a favoirte's content (not including categories)
     * 
     * return the Address in cache after rename
     */
    public Address renameFavoriteAddress(Address favoriteAddr, String newLabel)
    {
        // [MRD]Label is a required field that must be unique.
        for (int i = 0; i < this.favoriteAddress.size(); i++)
        {
            Address reNamedAddr = (Address) favoriteAddress.elementAt(i);
            if (reNamedAddr.getStatus() != CommonDBdata.DELETED)
            {
                boolean isMatch = false;
                
                // for the new added (haven't sync with backend) addresses, the id is -1.
                // So any two such addresses will match in old logic and it will cause problem.
                if(favoriteAddr.getId() > 0)
                {
                    if(reNamedAddr.getId() == favoriteAddr.getId())
                    {
                        isMatch = true;
                    }
                }
                else
                {
                    if(isSameAddress(favoriteAddr, reNamedAddr, true))
                    {
                        isMatch = true;
                    }
                }
                
                if (isMatch)
                {
                    reNamedAddr.setStatus(CommonDBdata.UPDATED);
                    reNamedAddr.setLabel(newLabel);
                    
                    if(reNamedAddr.getType() == Address.TYPE_FAVORITE_STOP)
                    {
                        Stop stop = reNamedAddr.getStop();
                        if(stop != null)
                        {
                            stop.setLabel(newLabel);
                        }
                    }
                    
                    reNamedAddr.setUpdateTime(System.currentTimeMillis());
                    // delete it
                    favoriteAddress.removeElementAt(i);
                    // sort the renamed address
                    insertAddressByAlphabeticaOrder(reNamedAddr, favoriteAddress);
                    return reNamedAddr;
                }
            }
        }

        return null;
    }

    public boolean renamefavoriteCatalog(FavoriteCatalog catalog, String newName)
    {

        for (int i = 0; i < this.favoriteAddress.size(); i++)
        {
            Address addr = (Address) favoriteAddress.elementAt(i);
            if (addr.getStatus() == CommonDBdata.DELETED)
                continue;

            Vector addrCatVec = addr.getCatagories();
            for (int j = 0; j < addrCatVec.size(); j++)
            {
                String name = (String) addrCatVec.elementAt(j);
                if (name.equalsIgnoreCase(catalog.getName()))
                {
                    addrCatVec.removeElementAt(j);
                    // need order
                    insertNameByAlphabeticaOrder(newName, addrCatVec);
                    if (addr.getStatus() != CommonDBdata.ADDED)
                        addr.setStatus(CommonDBdata.UPDATED);
                    break;
                }
            }
        }

        for (int i = 0; i < this.favoriteCatalog.size(); i++)
        {
            FavoriteCatalog temp = (FavoriteCatalog) favoriteCatalog.elementAt(i);
            if (temp.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN
                    || temp.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
                continue;

            if (temp.getName().equalsIgnoreCase(catalog.getName()))
            {
                favoriteCatalog.removeElementAt(i);

                temp.setName(newName);
                temp.setUpdateTime(System.currentTimeMillis());
                insertCatalogByAlphabeticaOrder(temp);

                if (temp.getStatus() != CommonDBdata.ADDED)
                    temp.setStatus(CommonDBdata.UPDATED);
                return true;
                // need order
            }// end if
        }// end for
        return false;
    }

    private void insertNameByAlphabeticaOrder(String name, Vector names)
    {
        String lowcaseName = name.toLowerCase();
        for (int j = 0; j < names.size(); j++)
        {
            String tmp = (String) names.elementAt(j);
            if (lowcaseName.compareTo(tmp.toLowerCase()) <= 0)
            {
                names.insertElementAt(name, j);
                return;
            }
        }

        names.addElement(name);
    }

    public void removefavoriteCatalog(FavoriteCatalog catalog, boolean isWithContents)
    {

        for (int i = 0; i < this.favoriteCatalog.size(); i++)
        {
            FavoriteCatalog temp = (FavoriteCatalog) favoriteCatalog.elementAt(i);
            if (temp.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN
                    || temp.getStatus() == FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
                continue;

            if (temp.getName().equalsIgnoreCase(catalog.getName()))
            {
                if (temp.getId() < 0)
                {
                    favoriteCatalog.removeElementAt(i);
                }
                else
                {
                    if (isWithContents)
                        temp.setStatus(FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN);
                    else
                        temp.setStatus(FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN);

                }
                break;

                // need order
            }// end if
        }// end for

        for (int i = 0; i < this.favoriteAddress.size(); i++)
        {
            Address addr = (Address) favoriteAddress.elementAt(i);
            if (addr.getStatus() == CommonDBdata.DELETED)
                continue;

            Vector addrCatVec = addr.getCatagories();
            for (int j = 0; j < addrCatVec.size(); j++)
            {
                String name = (String) addrCatVec.elementAt(j);
                if (name.equalsIgnoreCase(catalog.getName()))
                {
                    addrCatVec.removeElementAt(j);
                    if (isWithContents)
                    {
                        // xubin fix 9091,if has another cater, do not remove current fav
                        if (addr.getCatagories().size() <= 1) // root all favorite at least belong to root category
                        {
                            // if addr id less than zero, not sync else synced
                            if (addr.getId() < 0)
                            {
                                favoriteAddress.removeElement(addr);
                            }
                            else
                            {
                                addr.setStatus(CommonDBdata.DELETED);
                            }

                        }// end if
                    }
                    else
                    {
                        if (addr.getStatus() != CommonDBdata.ADDED)
                            addr.setStatus(CommonDBdata.UPDATED);

                    }// end else
                     // xubin fix 9091, if return, another address has this cate would not be delete
                     // return true;
                }
            }
        }
        // return false;
    }

    // public void updatePoiRatingInfo(Poi poi,int type)
    // {
    // Vector vecAddr = getAddresses(type);
    //
    // for (int i=0; i< vecAddr.size(); i++)
    // {
    // Address addr = (Address)vecAddr.elementAt(i);
    // if (addr.getStatus() != Address.STOP_POI_DELETED )
    // {
    // // we can create sevral favorites with the same POI
    // if (addr.getPoi().getPoiId() == poi.getPoiId())
    // {
    // addr.getPoi().setAvgRating(poi.getAvgRating());
    // addr.getPoi().setUserRatedThisPoi(poi.hasUserRatedThisPoi());
    // }
    // }// end if
    // }// end for
    //
    // return ;
    //
    // }

    public void synchronizeAddresses(Vector addedAddresses, Vector deletedAddresses, boolean isFavorite)
    {
        Hashtable localEventTable = prepareLocalEventTable(isFavorite);
        
        Vector addresses = isFavorite ? this.favoriteAddress : this.recentAddress;

        Vector deletedStopIds = new Vector();
        Vector stopIds = new Vector();

        // delete address
        for (int i = 0; i < deletedAddresses.size(); i++)
        {
            Address tmp = (Address) deletedAddresses.elementAt(i);
            Long id = PrimitiveTypeCache.valueOf(tmp.getId());
            deletedStopIds.addElement(id);
        }

        for (int i = addresses.size() - 1; i >= 0; i--)
        {
            Address tmp = (Address) addresses.elementAt(i);
            Long id = PrimitiveTypeCache.valueOf(tmp.getId());

            // remove local stuff and deleted address
            if (tmp.getStatus() != CommonDBdata.UNCHANGED || deletedStopIds.contains(id))
            {
                if (isFavorite || tmp.getStatus() == CommonDBdata.DELETED || deletedStopIds.contains(id))
                {
                    addresses.removeElementAt(i);
                }
            }
            else
            {
                stopIds.addElement(id);
            }
        }

        // add address // fix bug 15535 ,reverse the order make sure the newest addr is on the top        
        if (isFavorite)
        {
        	for (int i = addedAddresses.size() - 1; i >= 0; i--)
            {
        	    Address tmp = (Address) addedAddresses.elementAt(i);
                tmp.setStatus(CommonDBdata.UNCHANGED);
                Long id = PrimitiveTypeCache.valueOf(tmp.getId());
                if (!stopIds.contains(id))
                {
                    checkEventInfo(localEventTable, tmp);
                    // this.updatePoiRatingInfo( tmp.getPoi() );                 
	                if (!isFavoriteInCategory(tmp, FAVORITE_ROOT_CATEGORY))
	                {
	                    //tmp.getCatagories().addElement(FAVORITE_ROOT_CATEGORY);
	                    if(tmp.getCatagories() == null)
                        {
                            Vector cat = new Vector();
                            cat.addElement(FAVORITE_ROOT_CATEGORY);
                            tmp.setCatagories(cat);
                        }
                        else if(!tmp.getCatagories().contains(FAVORITE_ROOT_CATEGORY))
                        {
                            tmp.getCatagories().addElement(FAVORITE_ROOT_CATEGORY);
                        }
	                }	
	                if (tmp.getCatagories().contains(RECEIVED_ADDRESSES_CATEGORY))
	                {
	                    addresses.insertElementAt(tmp, 0);
	                }
	                else
	                {
	                    insertAddressByAlphabeticaOrder(tmp);
	                }
                }
            }
        }
        else
        {
        	for (int i = 0; i < addedAddresses.size(); i++)
            {
                Address tmp = (Address) addedAddresses.elementAt(i);
                tmp.setStatus(CommonDBdata.UNCHANGED);
                Long id = PrimitiveTypeCache.valueOf(tmp.getId());
                if (!stopIds.contains(id))
               {
                    checkEventInfo(localEventTable, tmp);
                    boolean isReplace = false;
                    for (int j = 0; j < addresses.size(); j++)
                    {
                        Address address = (Address) addresses.elementAt(j);
                        if (address.getStatus() != CommonDBdata.UNCHANGED)
                        {
                            if (isSameAddress(tmp, address, true))
                            {
                                addresses.setElementAt(tmp, j);
                                isReplace = true;
                                break;
                            }
                        }
                    }
                    if (!isReplace)
                    {
                        addresses.insertElementAt(tmp, 0);
                    }
                }
            }
        }
        
        localEventTable.clear();
    }

    protected Hashtable prepareLocalEventTable(boolean isFavorite)
    {
        Hashtable table = new Hashtable();
        
        Vector addresses = isFavorite ? this.favoriteAddress : this.recentAddress;
        
        for(int i = 0; i < addresses.size(); i++)
        {
            Address tmp = (Address)addresses.elementAt(i);
            if(tmp.getEventId() > 0)
            {
                table.put(tmp.getEventId(), tmp);
            }
        }
        
        return table;
    }
    
    public Hashtable checkNoDetailEvents(boolean isFavorite)
    {
        Hashtable table = new Hashtable();
        
        Vector addresses = isFavorite ? this.favoriteAddress : this.recentAddress;
        
        for(int i = 0; i < addresses.size(); i++)
        {
            Address tmp = (Address)addresses.elementAt(i);
            if(tmp.getEventId() > 0 && !tmp.isEventDataAvailable())
            {
                table.put(tmp.getEventId(), tmp);
            }
        }
        
        return table;
    }
    
    public void checkEventInfo(Hashtable eventTable, Address address)
    {
        if(address == null || eventTable == null || eventTable.isEmpty())
        {
            return ;
        }
        
        long eventId = address.getEventId();
        if (eventId <= 0)
        {
            Enumeration<Long> keys = eventTable.keys();
            while(keys.hasMoreElements())
            {
                Long key = (Long)keys.nextElement();
                Address tmp = (Address) eventTable.get(key);
                
                if (tmp != null && address.getType() == tmp.getType())
                {
                    if(isSameAddress(address, tmp, true))
                    {
                        address.setEventId(tmp.getEventId());
                        if(tmp.isEventDataAvailable())
                        {
                            checkEventInfo(address, tmp);
                        }
                        else
                        {
                            address.setIsEventDataAvailable(false);
                        }
                        break;
                    }
                }
            }
        }
        else
        {
            Address checkItem = (Address) eventTable.get(eventId);
            
            checkEventInfo(address, checkItem);
        }
    }
    
    
    public void checkEventInfo(Address targetAddress, Address srcAddress)
    {
        if (targetAddress == null || srcAddress == null)
        {
            return;
        }

        targetAddress.setEventStartTime(srcAddress.getEventStartTime());
        targetAddress.setEventEndTime(srcAddress.getEventEndTime());
        targetAddress.setUseEventStartTime(srcAddress.isUseEventStartTime());
        targetAddress.setUseEventEndTime(srcAddress.isUseEventEndTime());
        targetAddress.setEventVenue(srcAddress.getEventVenue());

        targetAddress.setIsEventDataAvailable(true);
    }
    
    public boolean isFavoriteInCategory(Address address, String categoryName)
    {
        if (address != null && address.getCatagories() != null)
        {
            for (int i = 0; i < address.getCatagories().size(); i++)
            {
                String catName = (String) address.getCatagories().elementAt(i);
                if (catName.equalsIgnoreCase(categoryName))
                    return true;
            }
        }
        return false;
    }

    public void removeAddressFromCategory(Address address, String categoryName)
    {
        Vector vec = address.getCatagories();
        if (vec == null)
        {
            return;
        }
        for (int i = 0; i < vec.size(); i++)
        {
            String name = (String) vec.elementAt(i);
            if (name.equalsIgnoreCase(categoryName))
            {
                vec.removeElementAt(i);
                if (address.getStatus() != CommonDBdata.ADDED)
                    address.setStatus(CommonDBdata.UPDATED);

                break;
            }
        }// end for

    }

    public void synchronizeFavoriteCatalog(Vector addedFavoriteCategories, Vector deletedFavoriteCategories)
    {
        Hashtable categoryIds = new Hashtable();
        Hashtable deletedCategoryIds = new Hashtable();

        for (int i = 0; i < deletedFavoriteCategories.size(); i++)
        {
            FavoriteCatalog cat = (FavoriteCatalog) deletedFavoriteCategories.elementAt(i);
            Long id = PrimitiveTypeCache.valueOf(cat.getId());
            deletedCategoryIds.put(id, id);
        }

        for (int i = this.favoriteCatalog.size() - 1; i >= 0; i--)
        {
            FavoriteCatalog cat = (FavoriteCatalog) favoriteCatalog.elementAt(i);
            Long id = PrimitiveTypeCache.valueOf(cat.getId());
            if (cat.getStatus() != CommonDBdata.UNCHANGED || deletedCategoryIds.containsKey(id))
                favoriteCatalog.removeElementAt(i);
            else
                categoryIds.put(id, id);
        }

        for (int i = 0; i < addedFavoriteCategories.size(); i++)
        {
            FavoriteCatalog cat = (FavoriteCatalog) addedFavoriteCategories.elementAt(i);
            cat.setStatus(CommonDBdata.UNCHANGED);
            if (!categoryIds.containsKey(PrimitiveTypeCache.valueOf(cat.getId())))
                insertCatalogByAlphabeticaOrder(cat);
        }
    }

    /*
     * if this poi has been rated, set user rated flag true , if not store this rated poi, save its poi id if not rated,
     * set flag false
     */
    public void setPoiRated(Poi poi)
    {
        if (poi != null && poi.getBizPoi() != null && poi.getBizPoi().getPoiId() != null && this.ratedPoiIdNode != null)
        {
            long poiId = Long.parseLong(poi.getBizPoi().getPoiId());
            String poiIdStr = poiId + "";
            for (int i = 0; i < this.ratedPoiIdNode.size(); ++i)
            {
                if (poiIdStr.equals(this.ratedPoiIdNode.elementAt(i)))
                {
                    poi.setIsRated(true);
                    return;
                }
            }

            poi.setIsRated(true);
            this.ratedPoiIdNode.add(poiIdStr);
        }
    }

    public Address getDefualtOrigin()
    {
        return defualtOrigin;
    }

    public void setDefualtOrigin(Address defualtOrigin)
    {
        this.defualtOrigin = defualtOrigin;
    }
    
    public void parseAirports(Vector node)
    {
        if (node == null)
            return;
        Vector airports = new Vector(node.size());
        for (int i = 0; i < node.size(); i++)
        {
            Stop stop = (Stop)node.elementAt(i);
            stop.setType(Stop.STOP_AIRPORT);
            String codeAndName = AddressDao.getAirportShowLabel(stop);
            airportsName.addElement(codeAndName);
            if (codeAndName.length() > airportTextFieldMaxLength)
                airportTextFieldMaxLength = codeAndName.length();

            ComparableAddress comparableObject = new ComparableAddress();
            comparableObject.object = stop;
            comparableObject.str = codeAndName;
            String airportName = new StringBuffer(" ").append(stop.getFirstLine().toUpperCase()).append(" ").toString().toUpperCase();
            String cityName = new StringBuffer(" ").append(stop.getCity()).append(" ").toString().toUpperCase();
            comparableObject.containCityName = airportName.indexOf(cityName) > 0;
            airports.addElement(comparableObject);
        }

        synchronized (airportMutex)
        {
            airportStops = new ComparableAddress[airports.size()];
            for (int i = 0; i < airports.size(); i++)
            {
                airportStops[i] = (ComparableAddress) airports.elementAt(i);
            }
            this.isAirPortListInitialized = true;
            lastLocation = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            if (lastLocation == null)
            {
                lastLocation = LocationProvider.getInstance().getDefaultLocation();
            }
            sortAirport(airportStops, lastLocation);
        }
    }
    
    public static String getAirportShowLabel(Stop stop)
    {
        if(stop == null)
        {
            return "";
        }
        String code = stop.getLabel();
        String codeAndName = stop.getFirstLine();
        if(code != null && !"".equals(code))
        {
            codeAndName = code + ": " + codeAndName;
        }
        codeAndName = ResourceManager.getInstance().getStringConverter().converAirportLabel(codeAndName);
        return codeAndName;
    }

    public Stop getAirportByName(String airportName)
    {

        for (int i = 0; this.airportStops != null && i < this.airportStops.length; i++)
        {
            Stop stop = (Stop) airportStops[i].object;
            if (airportName.startsWith(stop.getLabel()))
                return stop;
        }
        for (int i = 0; this.airportStops != null && i < this.airportStops.length; i++)
        {
            Stop stop = (Stop) airportStops[i].object;
            if (airportName.indexOf(stop.getFirstLine()) != -1)
                return stop;
        }
        return null;
    }

    public Vector getAirports(String prefix, int maxSize)
    {
        TnLocation data = LocationProvider.getInstance().getLastKnownLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if (data == null)
        {
            data = LocationProvider.getInstance().getDefaultLocation();
        }
        return getAirports(prefix, maxSize, data);

    }

    
    public Vector getAirports(String prefix, int maxSize, TnLocation data)
    {
        Vector v = new Vector();

        synchronized (airportMutex)
        {
            if (!this.isAirPortListInitialized)
            {
                // Return with a empty vector, if airport list is not initialized
                return v;
            }
        }

        if (airportStops != null && airportStops.length > 0)
        {
            if (DataUtil.distance(data.getLatitude() - lastLocation.getLatitude(), data.getLongitude() - lastLocation.getLongitude()) >= minimumDistance)
            {
                TnLocation[] gpsData = new TnLocation[1];
                gpsData[0] = data;
                sortAirport(airportStops, gpsData[0]);
                lastLocation = data;
            }
            
            StringConverter converter = ResourceManager.getInstance().getStringConverter();
            if(converter == null)
            {
                return v;
            }
            for (int i = 0; i < airportStops.length; i++)
            {
                String codeAndName = airportStops[i].str;
                Stop stop = (Stop) airportStops[i].object;
                if (null == codeAndName)
                    continue;

                if (airportStops[i].containCityName)
                {
                    String cityName = stop.getCity();
                    if (converter.isWordMatched(prefix, cityName))
                    {
                        v.addElement(stop);
                        if (maxSize > 0 && v.size() >= maxSize)
                        {
                            break;
                        }
                        else
                        {
                            continue;
                        }
                    }
                }

                if (converter.isWordMatched(prefix, codeAndName))
                {
                    v.addElement(stop);
                    if (maxSize > 0 && v.size() >= maxSize)
                        break;
                }
            }
        }

        return v;
    }
    
    private void sortAirport(ComparableAddress[] airportStops, TnLocation lastKnownLocation)
    {
        if (lastKnownLocation.getLatitude() != 0 && lastKnownLocation.getLongitude() != 0)
        {
            for (int i = 0; i < airportStops.length; i++)
            {
                Stop stop = (Stop) airportStops[i].object;
                airportStops[i].compareValue = DataUtil.distance(stop.getLat() - lastKnownLocation.getLatitude(), stop.getLon()
                        - lastKnownLocation.getLongitude());
            }
            SortAlgorithm.sort(airportStops, SortAlgorithm.MERGE_SORT);
        }
    }
    
    public void initAirport()
    {
        airportsName = new Vector();
        airportStops = null;
        lastLocation = null;
    }

    static class ComparableAddress implements IComparable
    {

        public Object object;

        public String str;

        public int compareValue;

        public boolean containCityName;

        public int getId()
        {
            return 0;
        }

        public int compareTo(Object another)
        {
            if (another instanceof ComparableAddress)
            {
                if (this.compareValue > ((ComparableAddress) another).compareValue)
                    return 1;
                else if (this.compareValue < ((ComparableAddress) another).compareValue)
                    return -1;
                else
                    return 0;
            }
            else
            {
                throw new ClassCastException("Cannot compare!");
            }

        }
    }
    
    public boolean isAirPortListInitialized()
    {
        return isAirPortListInitialized;
    }
    
    public int getUnreviewedAddressSize()
    {
        int unReviewedAddressSize = 0;
        boolean isSyncEnabled = MigrationExecutor.getInstance().isSyncEnabled();
        if (!isSyncEnabled)
        {
            return unReviewedAddressSize;
        }
        
        int local = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_LOCAL_UNREVIEWED_ADDRESS_NUMBER);
        int network = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().get(SimpleConfigDao.KEY_NETWORK_UNREVIEWED_ADDRESS_NUMBER); 
        
        if(local > 0)
        {
            unReviewedAddressSize += local;
        }
        
        if(network > 0)
        {
            unReviewedAddressSize += network;
        }
        
        return unReviewedAddressSize;
    }

    public void setLocalUnreviewedAddressSize(int unreviewedAddressSize)
    {
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_LOCAL_UNREVIEWED_ADDRESS_NUMBER, unreviewedAddressSize);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    public void setNetworkUnrevieweAddressSize(int networkShareAddressSzie)
    {
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_NETWORK_UNREVIEWED_ADDRESS_NUMBER, networkShareAddressSzie);
        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
    }
    
    public void clearRecentSearch()
    {
        this.recentSearch.removeAllElements();
        this.cache.remove(KEY_RECENT_SEARCH);
        this.cache.save();
    }
    
    public void clearRecentAddressKeywork(boolean isInitial)
    {
        Vector keywords = null;
        if(isInitial)
        {
            keywords = recentAddressKeywordInitial;
        }
        else
        {
            keywords = recentAddressKeyword;
        }
        keywords.removeAllElements();
        if(isInitial)
        {
            this.cache.remove(KEY_RECENT_ADDRESS_KEYWORD_INITIAL);
        }
        else
        {
            this.cache.remove(KEY_RECENT_ADDRESS_KEYWORD);
        }
        this.cache.save();
    }
    
    public Vector getAirportsName()
    {
        return airportsName;
    }
    
    public boolean isMigrationSucc()
    {
        return isMigrationSucc;
    }
    
    public boolean isMigrationInProgress()
    {
        return isMigrationInProgress;
    }
    
    public void setIsMigrationSucc(boolean value)
    {
        byte[] buff = (value + "").getBytes();
        cache.put(KEY_B_IS_MIGRATION_SUCC, buff);
        isMigrationSucc = value;
        cache.save();
    }
    
    public void setIsMigrationInProgress(boolean value)
    {
        byte[] buff = (value + "").getBytes();
        cache.put(KEY_B_IS_MIGRATION_IN_PROGRESS, buff);
        isMigrationInProgress = value;
        cache.save();
    }
    
    public void sortRecentAddresses()
    {
        synchronized (sortMutex)
        {
            if(this.recentAddress == null || this.recentAddress.size() <= 1)
            {
                return;
            }
            
            Address [] addressArray = vecToArray(this.recentAddress);
            
            SortAlgorithm.sort(addressArray, SortAlgorithm.INSERTION_SORT);
            
            Vector temp = new Vector();
            
            for (int i = 0 ; i < addressArray.length ; i ++)
            {
                temp.addElement(addressArray[i]);
            }
            
            Vector old = this.recentAddress;
            
            this.recentAddress = temp;
            
            old.removeAllElements();
            
            old = null;
        }
    }
    
    protected Address[] vecToArray(Vector addressVector)
    {
        if(addressVector == null || addressVector.size() == 0)
        {
            return null;
        }
        
        int size = addressVector.size();
        
        Address[] adderssArray = new Address[size];
        
        for (int i = 0 ; i < size ; i ++)
        {
            adderssArray[i] = (Address)addressVector.elementAt(i);
        }
        
        return adderssArray;
    }
    
    public void addToRecent(Address address)
    {
        if (address != null)
        { 
            Stop stop = address.getStop();
            if (stop != null && stop.getType() == Stop.STOP_CURRENT_LOCATION)
            {
                return;
            }
            byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(address);
            Address newAddress = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
            newAddress.setSource(Address.SOURCE_RECENT_PLACES);
            
            if(address.getType() == Address.TYPE_FAVORITE_POI || address.getType() == Address.TYPE_FAVORITE_STOP)
            {
                newAddress.setStatus(CommonDBdata.ADDED);
            }
            
            if (newAddress.getPoi() != null)
            {
                newAddress.setType(Address.TYPE_RECENT_POI);
            }
            else
            {
                newAddress.setType(Address.TYPE_RECENT_STOP);
            }
           
            AddressDao addressDao = DaoManager.getInstance().getAddressDao();
            if (addressDao != null)
            {
                newAddress.setCreateTime(System.currentTimeMillis());
                addressDao.addAddress(newAddress, true);
                addressDao.store();
            }
        }
    }

}
