/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteModel.java
 *
 */
package com.telenav.module.ac.favorite.favoriteeditor;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.module.ac.SyncStopsExecutor;
import com.telenav.module.sync.IMigrationListener;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringEditFavorite;
import com.telenav.res.ResourceManager;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
class FavoriteEditorModel extends AbstractCommonNetworkModel implements IFavoriteEditorConstants, IMigrationListener
{

    public FavoriteEditorModel()
    {
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                put(KEY_V_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog()));
                postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_DELETE_FAVORITE:
            {
                Address currentFavorite = (Address) fetch(KEY_O_SELECTED_ADDRESS);
                DaoManager.getInstance().getAddressDao().deleteAddress(currentFavorite);
                DaoManager.getInstance().getAddressDao().store();
                break;
            }
            case ACTION_SAVE_FAVORITE:
            {
                saveFavorite();
                syncStops();
                break;
            }
            case ACTION_CHECK_NAME_EXIST:
            {
                doCheckName();
                break;
            }
            case ACTION_SET_SELECTED_CATEGORY:
            {
                put(KEY_S_NEW_CATEGORY, getString(KEY_S_ADDED_CATEGORY));
                put(KEY_V_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog()));
                postModelEvent(EVENT_MODEL_SET_SELECTED_CATEGORY);
                break;
            }
        }
    }

    private void saveAddress(Address address, Vector includingCategories, Vector allCategories)
    {
        for (int i = 0; i < allCategories.size(); i++)
        {
            FavoriteCatalog category = (FavoriteCatalog) allCategories.elementAt(i);
            String categoryName = category.getName();
            if (includingCategories.contains(categoryName))
            {
                if (!DaoManager.getInstance().getAddressDao().isFavoriteInCategory(address, categoryName))
                {
                    if (address.getCatagories() != null)
                    {
                        address.getCatagories().addElement(categoryName);
                        address.setStatus(address.UPDATED);
                    }
                }
            }
            else
            {
                if ((address.getCatagories() != null))
                {
                    DaoManager.getInstance().getAddressDao().removeAddressFromCategory(address, categoryName);
                }
            }
        }
    }

    private void saveFavorite()
    {
        Vector allCategories = DaoManager.getInstance().getAddressDao().getFavoriteCatalog();
        Address selectedAddress = (Address) get(KEY_O_SELECTED_ADDRESS);
        String newName = getString(KEY_S_FAV_NAME);
        Vector includingCategories = getVector(KEY_V_SAVED_CATEGORIES);
        saveAddress(selectedAddress, includingCategories, allCategories);
        DaoManager.getInstance().getAddressDao().renameFavoriteAddress(selectedAddress, newName);
        DaoManager.getInstance().getAddressDao().store();
    }
    
    private void doCheckName()
    {
        String newName = getString(KEY_S_FAV_NAME);
        Address selectedAddress = (Address) get(KEY_O_SELECTED_ADDRESS);
        boolean isReceivedAddr = DaoManager.getInstance().getAddressDao()
                .isFavoriteInCategory(selectedAddress, AddressDao.RECEIVED_ADDRESSES_CATEGORY);

        String oldCategory = this.getString(KEY_S_OLD_CATEGORY);
        String newCategory = "";
        Vector includingCategories = getVector(KEY_V_SAVED_CATEGORIES);
        boolean needCheck = true;
        if (includingCategories != null)
        {
            for (int i = 0; i < includingCategories.size(); i++)
            {
                String categoryName = (String) includingCategories.elementAt(i);
                newCategory = categoryName;
                if (AddressDao.RECEIVED_ADDRESSES_CATEGORY.equalsIgnoreCase(categoryName))
                {
                    needCheck = false;
                    break;
                }
            }
        }
        if (!"".equals(newName) && newName.trim().length() != 0)
        {
            if (newCategory.trim().equalsIgnoreCase(oldCategory.trim())
                    && newName.trim().equalsIgnoreCase(selectedAddress.getLabel().trim()))
            {
                postModelEvent(EVENT_MODEL_NOT_SAVE_FAVORITE);
            }
            else if (!needCheck || (!isReceivedAddr && newName.trim().equalsIgnoreCase(selectedAddress.getLabel().trim())))
            {
                postModelEvent(EVENT_MODEL_SAVE_FAVORITE);
            }
            else
            {
                boolean isSame = false;
                Vector allFavorites = DaoManager.getInstance().getAddressDao().getFavorateAddresses();
                Address temp = null;
                for (int i = 0; i < allFavorites.size(); i++)
                {
                    temp = (Address) allFavorites.elementAt(i);
                    if (temp == selectedAddress)
                        continue;
                    if ((CommonDBdata.DELETED == temp.getStatus()))
                        continue;
                    if (newName.trim().equalsIgnoreCase(temp.getLabel()))
                    {
                        int type = selectedAddress.getType();
                        if (type == Address.TYPE_FAVORITE_POI)
                        {
                            Poi selectedPoi = selectedAddress.getPoi();
                            Poi tempPoi = temp.getPoi();
                            if (selectedPoi != null && selectedPoi.getBizPoi() != null
                                    && selectedPoi.getBizPoi().getPoiId() != null && tempPoi != null
                                    && tempPoi.getBizPoi() != null && tempPoi.getBizPoi().getPoiId() != null
                                    && selectedPoi.getBizPoi().getPoiId().equalsIgnoreCase(tempPoi.getBizPoi().getPoiId()))
                            {
                                isSame = true;
                                break;
                            }
                        }
                        else
                        {
                            Stop selectedStop = selectedAddress.getStop();
                            Stop tempStop = temp.getStop();
                            if (selectedStop != null && tempStop != null && selectedStop.equalsIgnoreCase(tempStop))
                            {
                                isSame = true;
                                break;
                            }
                        }
                    }
                }
                if (!isSame)
                {
                    postModelEvent(EVENT_MODEL_SAVE_FAVORITE);
                }
                else
                {
                    if (temp != null)
                    {
                        DaoManager.getInstance().getAddressDao().deleteAddress(temp);
                        DaoManager.getInstance().getAddressDao().store();
                    }
                    put(KEY_S_FAV_NAME, newName);
                    postModelEvent(EVENT_MODEL_SAVE_FAVORITE);
                }
            }
        }
        else
        {
            put(KEY_S_COMMON_MESSAGE,
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEditFavorite.RES_MESSAGEBOX_EMPTY_LABEL, IStringEditFavorite.FAMILY_EDIT_FAVORITE));
            postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
        }
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {

    }
    
    protected Vector getExistCategory(Vector categories)
    {
        Address address = (Address) get(KEY_O_SELECTED_ADDRESS);
        boolean IsSharedAddress=address !=null &&  address.getCatagories() !=null
                                                  && address.getCatagories().contains(AddressDao.RECEIVED_ADDRESSES_CATEGORY);
        Vector vector = new Vector();
        for(int i = 0; i < categories.size(); i ++)
        {
            FavoriteCatalog catelog = (FavoriteCatalog) categories.elementAt(i);
            String name = catelog.getName()==null?"":catelog.getName();
            if(!IsSharedAddress && AddressDao.RECEIVED_ADDRESSES_CATEGORY.equalsIgnoreCase(name))
                continue;
            if(catelog.getStatus() != CommonDBdata.DELETED && catelog.getStatus() != FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN && catelog.getStatus() != FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
                vector.addElement(catelog);
        }
        return vector;
    }

    public void onMigrationFinished()
    {
        
    }
    
    protected void releaseDelegate()
    {
        SyncStopsExecutor.getInstance().removeListener(this);
    }
}
