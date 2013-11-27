/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteModel.java
 *
 */
package com.telenav.module.ac.airport;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IValidateAirportProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.region.RegionUtil;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringAirport;
import com.telenav.res.ResourceManager;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-2
 */
class AirportModel extends AbstractCommonNetworkModel implements IAirportConstants
{
    protected static final int MAX_SIZE = 5;

    public AirportModel()
    {
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                Vector airports =  DaoManager.getInstance().getAddressDao().getAirportsName();
                put(KEY_S_AIRPORT, "");
                put(KEY_V_DROPDOWN_HINT, airports);
                TnLocation location=getTnLocation();
                put(KEY_O_TNLOCATION, location);
                getMatchedStops("");
                postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_VALIDATE_AIRPORT:
            {
                String keyword = getString(KEY_S_AIRPORT);
                if (keyword.length() < 3)
                {
                    put(KEY_S_ERROR_MESSAGE,
                        ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringAirport.RES_LABEL_LESS_WORDS_MESSAGE, IStringAirport.FAMILY_AIRPROT));
                    postModelEvent(EVENT_MODEL_POST_ERROR);
                }
                else
                {
                    Stop stop = DaoManager.getInstance().getAddressDao().getAirportByName(keyword);
                    if (stop != null)
                    {
                        Address airport = new Address(Address.SOURCE_TYPE_IN);
                        airport.setStop(stop);
                        put(KEY_O_SELECTED_ADDRESS, airport);
                        postModelEvent(EVENT_MODEL_RETURN_AIRPORT);
                    }
                    else
                    {
                        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
                        IValidateAirportProxy proxy = ServerProxyFactory.getInstance().createValidateAirportProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
                        proxy.validateAirport(keyword, "USA");
                    }
                }
                break;
            }
            case ACTION_HIDE_DROPDOWN:
            {
                postModelEvent(EVENT_MODEL_HIDE_DROPDOWN);
                break;
            }
            case ACTION_CANCEL_VALIDATING_AIRPORT:
            {
                CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_VALIDATE_AIRPORT);
                break;
            }
            case ACTION_SEARCH_AIRPORT:
            {
                getMatchedStops(getString(KEY_S_AIRPORT));
                break;
            }
        }
    }

    protected void getMatchedStops(String keyword)
    {
//        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
//       NavSdkValidateAirportProxy proxy = (NavSdkValidateAirportProxy)ServerProxyFactory.getInstance().createValidateAirportProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider);
//        proxy.getAirports(keyword, MAX_SIZE);
        //Disable temporarily until navsdk complete refactor  
        Vector result = DaoManager.getInstance().getAddressDao().getAirports(keyword, MAX_SIZE,(TnLocation)get(KEY_O_TNLOCATION));
        put(KEY_V_MATCHED_STOPS, result);
    }
    
    public TnLocation getTnLocation()
    {
        TnLocation location;
        String region = getRegion();
        if (region.equalsIgnoreCase(RegionUtil.getInstance().getCurrentRegion()))
        {
            location = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            if (location == null)
                location = AbstractDaoManager.getInstance().getResourceBarDao()
                        .getRegionAnchor(this.getRegion());
        }
        else
        {
            location = AbstractDaoManager.getInstance().getResourceBarDao()
                    .getRegionAnchor(this.getRegion());
        }
        if (location == null)
            location = LocationProvider.getInstance().getDefaultLocation();
        return location;
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof IValidateAirportProxy)
        {
            Vector result = ((IValidateAirportProxy) proxy).getSimilarAirports();
            if (result.size() == 0)
            {
                put(KEY_S_ERROR_MESSAGE, ResourceManager.getInstance().getCurrentBundle().getString(IStringAirport.RES_LABEL_NOT_FOUND_MESSAGE, IStringAirport.FAMILY_AIRPROT));
                postModelEvent(EVENT_MODEL_POST_ERROR);
            }
            else if (result.size() == 1)
            {
                put(KEY_O_SELECTED_ADDRESS, result.elementAt(0));
                postModelEvent(EVENT_MODEL_RETURN_AIRPORT);
            }
            else
            {
                put(KEY_V_ALTERNATIVE_ADDRESSES, result);
                postModelEvent(EVENT_MODEL_CHOOSE_AIRPORT);
            }
        }
    }

}
