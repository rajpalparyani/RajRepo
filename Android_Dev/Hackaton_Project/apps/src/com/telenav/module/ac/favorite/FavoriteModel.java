/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FavorviteModel.java
 *
 */
package com.telenav.module.ac.favorite;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.sort.util.SortUtil;
import com.telenav.logger.Logger;
import com.telenav.module.ac.AddressListFilter;
import com.telenav.module.ac.SyncStopsExecutor;
import com.telenav.module.mapdownload.IOnBoardDataAvailabilityListener;
import com.telenav.module.mapdownload.MapDownloadOnBoardDataStatusManager;
import com.telenav.module.poi.localevent.LocalEventDetailSyncExecutor;
import com.telenav.module.sync.IMigrationListener;
import com.telenav.module.sync.MigrationExecutor;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.sort.SortAlgorithm;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.UiFactory;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-26
 */
class FavoriteModel extends AbstractCommonNetworkModel implements IFavoriteConstants, IMigrationListener, IOnBoardDataAvailabilityListener, INetworkStatusListener
{
    protected boolean isForegroundSync = false;

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                MapDownloadOnBoardDataStatusManager.getInstance().addStatusChangeListener(this);
                NetworkStatusManager.getInstance().addStatusListener(this);
                put(KEY_S_SEARCH_TEXT, "");
                DaoManager.getInstance().getAddressDao().setLocalUnreviewedAddressSize(0);
                cloneVector(KEY_V_ALL_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao()
                        .getFavoriteCatalog()));
                cloneVector(KEY_V_ALL_FAVORITES, DaoManager.getInstance().getAddressDao().getRootFavorites());
                sortData(SORT_TYPE_ALPHABET);
                this.postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_SEARCH_FAVORITE:
            {
                Vector list = DaoManager.getInstance().getAddressDao().getDisplayFavorateAddress();
                put(KEY_V_SEARCH_FAVORITE_RESULT, AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), list, false));
                sortData(SORT_TYPE_CURRENT_ORDER);
                Vector categories = DaoManager.getInstance().getAddressDao().getFavoriteCatalog();
                put(KEY_V_SEARCH_CATEGORY_RESULT, getExistCategory(AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), categories, false)));
                break;
            }
            case ACTION_SEARCH_SUBCATEGORY:
            {
                FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
                String categoryName = category.getName();
                Vector list = DaoManager.getInstance().getAddressDao().getFavorateAddresses(categoryName, false);
                put(KEY_V_SEARCH_FAVORITE_RESULT, AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), list, false));
                sortData(SORT_TYPE_CURRENT_ORDER);
                break;
            }
            case ACTION_CLEAR_DATA:
            {
                put(KEY_S_SEARCH_TEXT, "");
                break;
            }
            case ACTION_UPDATE_DATA:
            {
                this.put(KEY_S_SEARCH_TEXT, "");
                this.put(KEY_B_NEED_RESORT, true);
                updateData();
                isForegroundSync = false;
                checkIsCategoryEmpty();
                break;
            }
            case ACTION_CATEGORY_DELETED:
            {
                cloneVector(KEY_V_ALL_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog()));
                cloneVector(KEY_V_ALL_FAVORITES, DaoManager.getInstance().getAddressDao().getRootFavorites());
                sortData(SORT_TYPE_CURRENT_ORDER);
                put(KEY_S_SEARCH_TEXT, "");
                syncStops();
                break;
            }
            case ACTION_SYN_FAVORITE:
            {
                isForegroundSync = true;
                syncStops();
                break;
            }
            case ACTION_BACKGROUND_SYNC:
            {
                isForegroundSync = false;
                syncStops();
                break;
            }
            case ACTION_NEXT_CATEGORY:
            {
                put(KEY_B_IS_SUBCATEGORY, true);
                FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
                String key = category.getName();
                put(KEY_V_SUB_CATEGORY_FAVORITES, DaoManager.getInstance().getAddressDao().getFavorateAddresses(key, false));
                sortData(SORT_TYPE_CURRENT_ORDER);
                isForegroundSync = false;
                syncStops();
                break;
            }
            case ACTION_CALL:
            {
                TnTelephonyManager.getInstance().startCall(getString(KEY_S_POI_PHONENUMBER));
                break;
            }
            case ACTION_CHECK_MIGRATION:
            {
                if(MigrationExecutor.getInstance().isSyncEnabled())
                {
                    MigrationExecutor.getInstance().doMigration(this);
                }
                break;
            }
            case ACTION_SORT_BY_ALPHABET:
            {
                updateUsingVectorData();
                sortData(SORT_TYPE_ALPHABET);
                break;
            }
            case ACTION_SORT_BY_DISTANCE:
            {
                updateUsingVectorData();
                sortData(SORT_TYPE_DISTANCE);
                break;
            }
            case ACTION_SORT_BY_DATE:
            {
                updateUsingVectorData();
                sortData(SORT_TYPE_DATE);
                break;
            }
            case ACTION_BACK_FROM_SUBCATEGORY:
            {
                this.put(KEY_B_IS_SUBCATEGORY, false);
                this.put(KEY_O_CATEGORY, null);
                this.put(KEY_S_SEARCH_TEXT, "");
                if (fetchBool(KEY_B_NEED_RESORT))
                {
                    updateUsingVectorData();
                    sortData(SORT_TYPE_CURRENT_ORDER);
                }
                break;
            }
        }
    }

    private void setAddressesDistance()
    {
        Vector favorites = (Vector) this.getVector(KEY_V_ALL_FAVORITES);
        Address address = null;
        if (favorites != null)
        {
            for (int i = 0; i < favorites.size(); i++)
            {
                address = (Address) favorites.elementAt(i);
                if (address != null)
                {
                    address.setDistance(UiFactory.getInstance().getDistance(address.getStop(), null));
                }
            }
        }
    }
    
    protected void prepareTextFilterSortVector()
    {
        Vector list = null;
        if (this.getState() == STATE_MAIN || this.getState() == STATE_MAIN_EDIT_MODE)
        {
            list = DaoManager.getInstance().getAddressDao().getDisplayFavorateAddress();
        }
        else
        {
            FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
            String categoryName = category.getName();
            list = DaoManager.getInstance().getAddressDao().getFavorateAddresses(categoryName, false);
        }
        put(KEY_V_SEARCH_FAVORITE_RESULT, AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), list, false));
    }
    
    protected void updateUsingVectorData()
    {
        boolean isSubCategory = this.getBool(KEY_B_IS_SUBCATEGORY);
        if ("".equals(this.getString(KEY_S_SEARCH_TEXT)))
        {
            if (isSubCategory)
            {
                FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
                String key = category.getName();
                put(KEY_V_SUB_CATEGORY_FAVORITES, DaoManager.getInstance().getAddressDao().getFavorateAddresses(key, false));
            }
            else
            {
                cloneVector(KEY_V_ALL_FAVORITES, DaoManager.getInstance().getAddressDao().getRootFavorites());
            }
        }
        else
        {
            Vector list = null;
            if (isSubCategory)
            {
                FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
                String categoryName = category.getName();
                list = DaoManager.getInstance().getAddressDao().getFavorateAddresses(categoryName, false);
            }
            else
            {
                list = DaoManager.getInstance().getAddressDao().getDisplayFavorateAddress();
            }
            put(KEY_V_SEARCH_FAVORITE_RESULT, AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), list, false));
        }
    }
    
    protected Integer getSortVectorKey()
    {
        Integer currentDataVectorKey = null;
        boolean isSubCategory = this.getBool(KEY_B_IS_SUBCATEGORY);
        if("".equals(this.getString(KEY_S_SEARCH_TEXT)))
        {
            if(isSubCategory)
            {
                currentDataVectorKey = KEY_V_SUB_CATEGORY_FAVORITES;
            }
            else
            {
                currentDataVectorKey = KEY_V_ALL_FAVORITES;
            }
        }
        else
        {
            currentDataVectorKey = KEY_V_SEARCH_FAVORITE_RESULT;
        }
        return currentDataVectorKey;
    }
    
    protected synchronized void sortData(final int sortType)
    {
        final Integer sortVectorKey = getSortVectorKey();
        int tempSortType;
        if (sortType != SORT_TYPE_CURRENT_ORDER)
        {
            if (getInt(KEY_I_SORT_TYPE) == sortType)
            {
                return;
            }
            FavoriteModel.this.put(KEY_B_NEED_RESORT, true);
            FavoriteModel.this.put(KEY_I_SORT_TYPE, sortType);
            tempSortType = sortType;
        }
        else
        {
            tempSortType = FavoriteModel.this.getInt(KEY_I_SORT_TYPE);
        }

        switch (tempSortType)
        {
            case SORT_TYPE_ALPHABET:
            {
                SortUtil.getInstance().sortByAlphabet(FavoriteModel.this.getVector(sortVectorKey), SortAlgorithm.MERGE_SORT, true);
                break;
            }
            case SORT_TYPE_DISTANCE:
            {
                SortUtil.getInstance().sortByDistance(FavoriteModel.this.getVector(sortVectorKey));
                break;
            }
            case SORT_TYPE_DATE:
            {
                SortUtil.getInstance().sortByTimeStamp(FavoriteModel.this.getVector(sortVectorKey));
                break;
            }
            default:
            {
                SortUtil.getInstance().sortByAlphabet(FavoriteModel.this.getVector(sortVectorKey), SortAlgorithm.MERGE_SORT, true);
                break;
            }
        }
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "FavoriteModel", "=== Request Update List === ");
        }
        FavoriteModel.this.put(KEY_B_NEED_REFRESH_LIST, true);
        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }
    
    protected void cloneVector(Integer key, Vector vector)
    {
        Vector oldVec = this.getVector(key);
        if (oldVec != null)
        {
            oldVec.removeAllElements();
        }
        this.remove(key);
        
        if (vector == null || vector.size() == 0)
        {
            return;
        }
        
        Vector vCopy = new Vector();
        for(int i = 0 ; i < vector.size(); i++)
        {
            vCopy.addElement(vector.elementAt(i));
        }
        put(key, vCopy);
    }
    
    protected Vector getExistCategory(Vector categories)
    {
        Vector vector = new Vector();
        for(int i = 0; i < categories.size(); i ++)
        {
            FavoriteCatalog catelog = (FavoriteCatalog) categories.elementAt(i);
            if(catelog.getStatus() != CommonDBdata.DELETED && catelog.getStatus() != FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN && catelog.getStatus() != FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
            {
                boolean isReceivedAddress = AddressDao.RECEIVED_ADDRESSES_CATEGORY
                        .equalsIgnoreCase(catelog.getName());
                if (isReceivedAddress)
                {
                    vector.insertElementAt(catelog, 0);
                }
                else
                {
                    vector.addElement(catelog);
                }
            }
        }
        return vector;
    }

    protected void updateData()
    {
        FavoriteCatalog category = (FavoriteCatalog)get(KEY_O_CATEGORY);
        if(category != null)
        {
            String key = category.getName();
            cloneVector(KEY_V_SUB_CATEGORY_FAVORITES, DaoManager.getInstance().getAddressDao().getFavorateAddresses(key, false));
        }
        cloneVector(KEY_V_ALL_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog()));
        cloneVector(KEY_V_ALL_FAVORITES, DaoManager.getInstance().getAddressDao().getRootFavorites());
        sortData(SORT_TYPE_CURRENT_ORDER);
        put(KEY_S_SEARCH_TEXT, "");
    }
    
    protected void checkIsCategoryEmpty()
    {
        FavoriteCatalog category = (FavoriteCatalog)get(KEY_O_CATEGORY);
        if(category != null)
        {
            Vector favorites = getVector(KEY_V_SUB_CATEGORY_FAVORITES);
            if(favorites != null && favorites.size() == 0)
               postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        processSyncStopsError(proxy, (byte)-1, "", false);
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        processSyncStopsError(proxy, statusCode, jobId, true);
    }
    
    protected void processSyncStopsError(AbstractServerProxy proxy, byte statusCode, String jobId, boolean isNetworkError)
    {
        this.put(KEY_B_REFRESH_FROM_NETWORK, true);
        if (isForegroundSync)
        {
            String errorMessage = proxy == null ? "" : proxy.getErrorMsg();
            if (errorMessage == null || errorMessage.length() == 0)
            {
                errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
            }
            postErrorMessage(errorMessage);
        }
        else
        {
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
    protected void releaseDelegate()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeContextMenu();
        SyncStopsExecutor.getInstance().removeListener(this);
        NetworkStatusManager.getInstance().removeStatusListener(this);
        MapDownloadOnBoardDataStatusManager.getInstance().removeStatusChangeListener(this);
        super.releaseDelegate();
    }
    
    protected void activateDelegate(boolean isUpdateView)
    {
        refreshData(true);
        LocalEventDetailSyncExecutor.getInstance().addGobyEventRefreshListener(this);
    }
    
    @Override
    protected void deactivateDelegate()
    {
        LocalEventDetailSyncExecutor.getInstance().removeGobyEventRefreshListener(this);
    }

    public void onMigrationFinished()
    {
        boolean isSyncSucc = DaoManager.getInstance().getAddressDao().isMigrationSucc();
        if(isSyncSucc)
        {
            processSyncStopsFinished();
        }
        else
        {
            processSyncStopsError(null, (byte)-1, "", false);
        }
    }
    
    public void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        
        if(IServerProxyConstants.ACT_GET_EVENT_DETAIL.equals(action))
        {
            processGobyDetailFinished();
        }
        else
        {
            processSyncStopsFinished();
        }
    }
    
    protected void processGobyDetailFinished()
    {
        DaoManager.getInstance().getAddressDao().store();
        
        if(this.isActivated())
        {
            this.put(KEY_B_REFRESH_FROM_NETWORK, true);
            refreshData(true);
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
    protected void processSyncStopsFinished()
    {
        DaoManager.getInstance().getAddressDao().setLocalUnreviewedAddressSize(0);
        DaoManager.getInstance().getAddressDao().store();
        
        if(this.isActivated())
        {
            this.put(KEY_B_REFRESH_FROM_NETWORK, true);
            refreshData(false);
            postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
        
    //Fix data inconsistant between device and website
    private void refreshData(boolean isRefreshAll)
    {
        cloneVector(KEY_V_ALL_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog()));
        cloneVector(KEY_V_ALL_FAVORITES, DaoManager.getInstance().getAddressDao().getRootFavorites());
        setAddressesDistance();
        
        if(isRefreshAll || this.getState()==STATE_MAIN)
        {
            Vector list = DaoManager.getInstance().getAddressDao().getRootFavorites();
            put(KEY_V_SEARCH_FAVORITE_RESULT, AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), list, false));
            Vector categories = DaoManager.getInstance().getAddressDao().getFavoriteCatalog();
            put(KEY_V_SEARCH_CATEGORY_RESULT, getExistCategory(AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), categories, false)));
        }
        
        if(isRefreshAll || this.getState()==STATE_SUBCATEGORY)
        {
            FavoriteCatalog category = (FavoriteCatalog)get(KEY_O_CATEGORY);
            if(category != null)
            {
                String text = getString(KEY_S_SEARCH_TEXT);
                if ("".equalsIgnoreCase(text))
                {
                    String key = category.getName();
                    put(KEY_V_SUB_CATEGORY_FAVORITES, DaoManager.getInstance().getAddressDao().getFavorateAddresses(key, false));
                }
                else
                {
                    String categoryName = category.getName();
                    Vector list = DaoManager.getInstance().getAddressDao().getFavorateAddresses(categoryName, false);
                    put(KEY_V_SEARCH_FAVORITE_RESULT, AddressListFilter.getFilteredList(getString(KEY_S_SEARCH_TEXT), list, false));
                }
            }
        }
        sortData(SORT_TYPE_CURRENT_ORDER);
        this.put(KEY_B_NEED_RESORT, true);
    }

    @Override
    public void onLocalMapDataAvailabilityChanged(boolean isAvailable)
    {
        FavoriteModel.this.put(KEY_B_NEED_REFRESH_LIST, true);
        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        FavoriteModel.this.put(KEY_B_NEED_REFRESH_LIST, true);
        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }
}
