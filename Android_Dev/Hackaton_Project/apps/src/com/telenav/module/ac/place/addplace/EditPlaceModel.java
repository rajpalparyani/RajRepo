/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AddPlaceModel.java
 *
 */
package com.telenav.module.ac.place.addplace;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringEditFavorite;
import com.telenav.res.ResourceManager;

/**
 *@author Casper(pwang@telenav.cn)
 *@date 2013-3-06
 */
public class EditPlaceModel extends AbstractCommonNetworkModel implements IAddPlaceConstants
{
    public static final long NO_GPS_TIME_OUT = 12*1000;

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                init();
                break;
            }
            case ACTION_CHECK_PLACE_LABEL:
            {
                checkPlaceLabel();
                break;
            }
            case ACTION_SAVE_PLACE:
            {
                saveFavorite();
                syncStops();
                this.postModelEvent(EVENT_MODEL_PLACE_ADDED);
                break;
            }
            case ACTION_CATEGORY_UPDATE:
            {
                String category = this.getString(KEY_S_ADDED_CATEGORY);
                this.put(KEY_V_ALL_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog(), category));
                break;
            }
        }
    }

    
    protected void init()
    {
        Address address = (Address) get(KEY_O_SELECTED_ADDRESS);
        String oldCategory = "";
        if(address != null)
        {
            Vector addressCategory = address.getCatagories();
            for(int i = 0; i < addressCategory.size(); i++)
            {
                String name = (String)addressCategory.elementAt(i);
                if(!name.equalsIgnoreCase(AddressDao.FAVORITE_ROOT_CATEGORY))
                {
                    oldCategory = name;
                }
            }
        }
        this.put(KEY_S_OLD_CATEGORY, oldCategory);
        put(KEY_V_ALL_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog(), oldCategory));
        this.postModelEvent(EVENT_MODEL_CUSTOM_PLACE);
    }

    protected void checkPlaceLabel()
    {
        String newName = getString(KEY_S_PLACE_LABEL);
        if (newName == null || newName.trim().length() == 0)
        {
            put(KEY_S_COMMON_MESSAGE,
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEditFavorite.RES_MESSAGEBOX_EMPTY_LABEL, IStringEditFavorite.FAMILY_EDIT_FAVORITE));
            postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
            return;
        }

        Address selectedAddress = (Address) get(KEY_O_SELECTED_ADDRESS);
        
        boolean sameLabel = newName.trim().equalsIgnoreCase(selectedAddress.getLabel().trim());
        this.put(KEY_B_LABEL_CHANGED, !sameLabel);
        String newCategory = getSelectedCategoryName();
        String oldCategory = this.getString(KEY_S_OLD_CATEGORY);
        boolean sameCategory = (newCategory != null ? newCategory.equals(oldCategory) : oldCategory == null);
        this.put(KEY_B_CATEGORY_CHANGED, !sameCategory);
        
        boolean saveInReceived = AddressDao.RECEIVED_ADDRESSES_CATEGORY.equalsIgnoreCase(newCategory);
        
        if (sameCategory && sameLabel)
        {
            postModelEvent(EVENT_MODEL_NO_NEED_SAVE_AGAIN);
        }
        // Received addres label changed or other address just category chagned.
        else if (saveInReceived || sameLabel)
        {
            postModelEvent(EVENT_MODEL_SAVE_PLACE);
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
                                && selectedPoi.getBizPoi().getPoiId() != null && tempPoi != null && tempPoi.getBizPoi() != null
                                && tempPoi.getBizPoi().getPoiId() != null
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
            if (isSame && temp != null)
            {
                if (temp.getEventId() > 0)
                {
                    selectedAddress.setEventId(temp.getEventId());
                }
                DaoManager.getInstance().getAddressDao().deleteAddress(temp);
                DaoManager.getInstance().getAddressDao().store();
            }
            postModelEvent(EVENT_MODEL_SAVE_PLACE);
        }
    }

    protected void saveFavorite()
    {
        Address selectedAddress = (Address) get(KEY_O_SELECTED_ADDRESS);
        if(this.getBool(KEY_B_CATEGORY_CHANGED))
        {
            String savedCatgory = getSelectedCategoryName();
            changeAddressCategory(selectedAddress, savedCatgory);
        }
        if(this.getBool(KEY_B_LABEL_CHANGED))
        {
            setAddressBrand(selectedAddress,this.getString(KEY_S_PLACE_LABEL));
            DaoManager.getInstance().getAddressDao().renameFavoriteAddress(selectedAddress, this.getString(KEY_S_PLACE_LABEL));
        }
        
        DaoManager.getInstance().getAddressDao().store();
    }
    
    protected void setAddressBrand(Address address, String label)
    {
        if (label !=null && address != null && address.getPoi() != null && address.getPoi().getBizPoi() != null)
        {
            address.getPoi().getBizPoi().setBrand(label);
            if (address.getPoi().getBizPoi().getStop() != null)
            {
                address.getPoi().getBizPoi().getStop().setLabel(label);
            }
        }
    }
    
    private void changeAddressCategory(Address address, String savedCatgory)
    {
        Vector currentCategories = address.getCatagories();
        for (int i = currentCategories.size() - 1; i >= 0; i--)
        {
            String name = (String) currentCategories.elementAt(i);
            if (name.equalsIgnoreCase(AddressDao.FAVORITE_ROOT_CATEGORY))
            {
                continue;
            }
            else
            {
                currentCategories.removeElementAt(i);
            }
        }
        if(savedCatgory != null && savedCatgory.length() != 0)
        {
            currentCategories.add(savedCatgory);
        }
        if (address.getStatus() != CommonDBdata.ADDED)
            address.setStatus(CommonDBdata.UPDATED);
    }
    
    protected Vector getExistCategory(Vector categories, String oldCategory)
    {
        Vector v = new Vector();
        for(int i = 0; i < categories.size(); i ++)
        {
            FavoriteCatalog catelog = (FavoriteCatalog) categories.elementAt(i);
            String name = catelog.getName() == null ? "" : catelog.getName();
            if(AddressDao.RECEIVED_ADDRESSES_CATEGORY.equalsIgnoreCase(name))
            {
                if(!name.equals(oldCategory))
                {
                    continue;
                }
            }
            if(catelog.getStatus() != CommonDBdata.DELETED && catelog.getStatus() != FavoriteCatalog.CATEGORY_DELETED_WITH_CHILDREN && catelog.getStatus() != FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN)
            {
                v.addElement(catelog);
                if(name.equals(oldCategory))
                {
                    this.put(KEY_I_SELECTED_CATEGORY_INDEX, v.size() - 1);
                }
            }
        }
        return v;
    }
    
    protected String getSelectedCategoryName()
    {
        FavoriteCatalog selectedCatelog = null;
        int selectedCategoryIndex = this.getInt(KEY_I_SELECTED_CATEGORY_INDEX);
        Vector categories = this.getVector(KEY_V_ALL_CATEGORIES);
        if(selectedCategoryIndex > -1 && selectedCategoryIndex < categories.size())
        {
            selectedCatelog = (FavoriteCatalog) categories.elementAt(selectedCategoryIndex);
        }
        if(selectedCatelog == null || selectedCatelog.getName() == null || selectedCatelog.getName().length() == 0)
        {
            return null;
        }
        else
        {
            return selectedCatelog.getName();
        }
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
    }


}
