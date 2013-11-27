/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AddPlaceModel.java
 *
 */
package com.telenav.module.ac.place.addplace;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.CommonDBdata;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringEditFavorite;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;

/**
 *@author Casper(pwang@telenav.cn)
 *@date 2013-2-21
 */
public class AddPlaceModel extends EditPlaceModel implements IAddPlaceConstants, PoiDataListener, LocationListener
{
    public static final long NO_GPS_TIME_OUT = 12*1000;

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_RETRIEVE_PLACES_LIST:
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
                if(poiDataWrapper.getAnchorStop() == null)
                {
                    this.postModelEvent(EVENT_MODEL_NO_GPS_WARNING);
                }
                else
                {
                    requestPoiSearch(poiDataWrapper);
                }
                break;
            }
            case ACTION_GETING_MORE:
            {
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)this.get(KEY_O_POI_DATA_WRAPPER);
                if (poiDataWrapper.getAddressSize() == 0 || poiDataWrapper.isHasMorePoi() )
                {
                    if (!poiDataWrapper.isDoingRequest())
                    {
                        poiDataWrapper.setNormalPoiStartIndex(poiDataWrapper.getNormalAddressSize());
                        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                        PoiDataRequester poiDataRequester = new PoiDataRequester(userProfileProvider);
                        poiDataRequester.doRequestPoi(poiDataWrapper, this);
                    }
                }
                break;
            }
            case ACTION_CHECK_SELECT:
            {

                int selectType = this.getInt(KEY_I_PLACE_OPERATION_TYPE);
                switch (selectType)
                {
                    case PLACE_OPERATION_TYPE_ADD:
                    {
                        this.postModelEvent(EVENT_MODEL_CUSTOM_PLACE);
                        break;
                    }
                    case PLACE_OPERATION_TYPE_SHARE:
                    {
                        this.postModelEvent(EVENT_MODEL_SHARE_PLACE);
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
            {
                super.doActionDelegate(actionId);
                break;
            }
        }
    }
    
    @Override
    protected void init()
    {
        Stop anchor = checkCurrentLocation();
        FavoriteCatalog catelog = (FavoriteCatalog) this.get(ICommonConstants.KEY_O_CATEGORY);
        String categoryName = catelog != null ? catelog.getName() : null;
        put(KEY_V_ALL_CATEGORIES, getExistCategory(DaoManager.getInstance().getAddressDao().getFavoriteCatalog(), categoryName));
        if( this.get(KEY_O_SELECTED_ADDRESS) == null)
        {
            initSearchArgs(anchor);
            this.postModelEvent(EVENT_MODEL_START_MAIN);
        }
        else
        {
            this.postModelEvent(EVENT_MODEL_CUSTOM_PLACE);
        }
    }
    
    private Stop checkCurrentLocation()
    {
        Address address = (Address) this.get(KEY_O_CURRENT_ADDRESS);
        Stop anchor = address != null ? address.getStop() : null;
        if(anchor == null)
        {
            anchor = this.getAnchor();
            if(anchor != null)
            {
                requestRgc(anchor.getLat(), anchor.getLon());
            }
        }
        if(anchor == null)
        {
            requestCurrentLocation();
        }
        return anchor;
    }
    
    private void requestCurrentLocation()
    {
        LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
            LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1);        
    }

    
    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            TnLocation loc = locations[0];
            if (loc != null)
            {
                requestRgc(loc.getLatitude(), loc.getLongitude());
                Stop anchor = new Stop();
                anchor.setType(Stop.STOP_CURRENT_LOCATION);
                anchor.setLat(loc.getLatitude());
                anchor.setLon(loc.getLongitude());
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
                poiDataWrapper.setAnchorStop(anchor);
                this.postModelEvent(EVENT_MODEL_START_MAIN);
            }
            else
            {
                requestCurrentLocation();
            }
        }
        else
        {
            requestCurrentLocation();
        }
    }
    
    @Override
    protected void checkPlaceLabel()
    {
        String newName = getString(KEY_S_PLACE_LABEL);
        Address selectedAddress = (Address) get(KEY_O_SELECTED_ADDRESS);
        if ("".equals(newName) || newName.trim().length() == 0)
        {
            put(KEY_S_COMMON_MESSAGE,
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEditFavorite.RES_MESSAGEBOX_EMPTY_LABEL, IStringEditFavorite.FAMILY_EDIT_FAVORITE));
            postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
        }
        else
        {
            boolean isSame = false;
            Vector allFavorites = DaoManager.getInstance().getAddressDao().getFavorateAddresses();
            Address temp = null;
            for (int i = 0; i < allFavorites.size(); i++)
            {
                temp = (Address) allFavorites.elementAt(i);
                if ((CommonDBdata.DELETED == temp.getStatus()))
                    continue;
                if (newName.trim().equalsIgnoreCase(temp.getLabel()))
                {
                    int type = selectedAddress.getType();
                    if (type == Address.TYPE_FAVORITE_POI || type == Address.TYPE_RECENT_POI)
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
            put(KEY_S_PLACE_LABEL, newName);
            postModelEvent(EVENT_MODEL_SAVE_PLACE);
        }
    }
    
    @Override
    protected void saveFavorite()
    {
        Address selectedAddress = (Address) get(KEY_O_SELECTED_ADDRESS);
        String newName = getString(KEY_S_PLACE_LABEL);
        // need creating a new address and add it to Dao
        byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(selectedAddress);
        Address address = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
        if (address.getPoi() != null)
        {
            address.setType(Address.TYPE_FAVORITE_POI);
        }
        else
        {
            address.setType(Address.TYPE_FAVORITE_STOP);
        }
        address.setSource(Address.SOURCE_FAVORITES);
        address.setLabel(newName);
        address.setCreateTime(System.currentTimeMillis());
        address.setStatus(-1);
        
        setAddressBrand(address,newName);
        
        DaoManager.getInstance().getAddressDao().addAddress(address, false);
        DaoManager.getInstance().getAddressDao().store();
        logAddPlaceMisLog(address);
        String selectedCategoryName = getSelectedCategoryName();
        if (address.getCatagories() != null && selectedCategoryName != null)
        {
            address.getCatagories().addElement(selectedCategoryName);
        }
    }
    
    
    
    private void logAddPlaceMisLog(Address address)
    {
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        if(address.getPoi() != null)
        {
            int index = -1;
            if(address.getPoi().getBizPoi() != null && poiDataWrapper != null)
            {
                index= poiDataWrapper.getIndexOfMixedListByPoiId(address.getPoi().getBizPoi().getPoiId());
            }
            if(index >= 0)
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_ADD_PLACE, index);
            }
            else
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_ADD_PLACE, address.getPoi());
            }
        }        
    }
    
    private void initSearchArgs(Stop anchor)
    {
        int categoryId = 50000;
        int searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        int searchFromType = IPoiSearchProxy.TYPE_SEARCH_FROM_TYPEIN;
        int sortType = IPoiSearchProxy.TYPE_SORT_BY_DISTANCE;
        int pageNumber = 0;
        int pageSize = MAX_NUM_PER_PAGE;
        PoiDataWrapper dataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
        dataWrapper.setSearchArgs(0, searchType, searchFromType, -1, sortType, categoryId, pageNumber, pageSize, "", "", anchor, null, null, IOneBoxSearchProxy.TYPE_INPUTTYPE_ANY, 0) ;
        dataWrapper.setIsMostPopularSearch(false);
        this.put(KEY_O_POI_DATA_WRAPPER, dataWrapper);
    }

    private void requestPoiSearch(PoiDataWrapper poiDataWrapper)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        PoiDataRequester poiDataRequester = new PoiDataRequester(userProfileProvider);
        poiDataRequester.doRequestPoi(poiDataWrapper, this);
    }

    protected void requestRgc(int lat, int lon)
    {
        IRgcProxy proxy = ServerProxyFactory.getInstance().createRgcProxy(null, CommManager.getInstance().getComm(), this);
        proxy.requestRgc(lat, lon);
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof IRgcProxy)
        {
            Address address = ((IRgcProxy) proxy).getAddress();
            if(address != null && address.getStop() != null)
            {
                this.put(KEY_O_CURRENT_ADDRESS, address);
                this.postModelEvent(EVENT_MODEL_REFRESH_PLACES_LIST);
            }
            else
            {
                handleRGCError();
            }
        }
    }
    

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if (proxy instanceof IRgcProxy)
        {
            handleRGCError();
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IRgcProxy)
        {
            handleRGCError();
        }
    }
    
    private void handleRGCError()
    {
        Address address = new Address();
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.get(KEY_O_POI_DATA_WRAPPER);
        address.setStop(poiDataWrapper.getAnchorStop());
        this.put(KEY_O_CURRENT_ADDRESS, address);
        this.postModelEvent(EVENT_MODEL_REFRESH_PLACES_LIST);
    }



    public void poiResultUpdate(int resultValue, int resultType, String errorMsg, PoiDataWrapper poiDataWrapper)
    {
        String errorMessage = errorMsg;
        this.put(KEY_B_FORBID_LIST_HITBOTTOM, true);
        if(resultValue != PoiDataRequester.TYPE_SUCCESS && this.getState() != STATE_MAIN)
        {
            return;
        }
        switch(resultValue)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            {
                
                if (errorMessage == null || errorMessage.length() == 0)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
                }
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            {
                this.put(KEY_B_FORBID_LIST_HITBOTTOM, false);
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }
                
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_BAD_ARGS:
            {
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }
                
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS:
            {
                this.put(KEY_B_FORBID_LIST_HITBOTTOM, false);
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                postModelEvent(EVENT_MODEL_REFRESH_PLACES_LIST);
                break;
            }
        }
    }
    
    private Stop getAnchor()
    {
        Stop anchor = null;
        // 1. Get  gps or network fix
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
         LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, true);
        if (location == null)
        {
            // 2. Get  last-known gps or network fix
            location = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
            if (location != null)
            {
                anchor = new Stop();
                anchor.setType(Stop.STOP_CURRENT_LOCATION);
                anchor.setLat(location.getLatitude());
                anchor.setLon(location.getLongitude());
            }
        }
        else
        {
            anchor = new Stop();
            anchor.setType(Stop.STOP_CURRENT_LOCATION);
            anchor.setLat(location.getLatitude());
            anchor.setLon(location.getLongitude());
        }        
        return anchor;
    }


}
