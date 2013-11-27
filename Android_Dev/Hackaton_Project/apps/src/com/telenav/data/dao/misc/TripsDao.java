/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TripsDao.java
 *
 */
package com.telenav.data.dao.misc;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serializable.IPreferenceSerializable;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.logger.Logger;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.persistent.TnStore;

/**
 * Trips DAO manager.
 * 
 *@author zhdong@telenav.cn
 *@date 2011-1-6
 */
public class TripsDao extends AbstractDao
{
    final static protected int KEY_NORMAL_TRIP = 1;
    final static protected int KEY_DETOUR_TRIP = 2;
    final static protected int KEY_LAST_TRIP_TIME = 3;
    
    /**
     * The normal trip
     */
    private Address trip;
    
    /**
     * The detour trip is the trip we driving to a search along POI.
     */
    private Address detourTrip;
    
    private TnStore store;
    
    private long tripTime = -1;
    
    private Preference routeStylePref = null;
    
    private Preference routeSettingsPref = null;
    
    private Preference audioSettingsPref = null;
    
    private Preference mapStyleSettingPref = null;

    public TripsDao(TnStore store)
	{
		this.store = store;
		byte[] data = store.get(KEY_NORMAL_TRIP);
		if (data != null)
		{
			trip = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
			trip.setSource(Address.SOURCE_RESUME_TRIP);
		}

		data = store.get(KEY_DETOUR_TRIP);
		if (data != null)
		{
            detourTrip = SerializableManager.getInstance().getAddressSerializable().createAddress(data);
            detourTrip.setSource(Address.SOURCE_RESUME_TRIP);
		}
		
		data = store.get(KEY_LAST_TRIP_TIME);
		if (data != null)
		{
			String str = new String(data);
			tripTime = Long.parseLong(str.trim());
		}
	}
    
    public void setNormalTrip(Address trip)
    {
        this.detourTrip = null;
        this.trip = trip;
        if(trip != null)
        {
        	this.tripTime = System.currentTimeMillis();
        }
    }
    
    public void setDetourTrip(Address trip)
    {
        this.detourTrip = trip;
        if(trip != null)
        {
        	this.tripTime = System.currentTimeMillis();
        }
    }
    
    public Address getLastTrip()
    {
        if (detourTrip != null)
        {
            detourTrip.setSource(Address.SOURCE_RESUME_TRIP);
            return detourTrip;
        }
        if(trip != null)
        {
            trip.setSource(Address.SOURCE_RESUME_TRIP);
        }
        return trip;
    }
    
    /**
     * When arrive at destination, we should clean up last trip here.
     */
    public void endTrip()
    {
        if (detourTrip == null)
        {
            // we are in a normal trip
            trip = null;
        }
        else
        {
            // we are in a detour trip
            detourTrip = null;
        }
    }

    public void clear()
    {
        detourTrip = null;
        trip = null;
        tripTime = -1;
        store.clear();
    }

    public void store()
    {
        if (trip == null)
        {
            store.remove(KEY_NORMAL_TRIP);
        }
        else
        {
            byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(trip);
            store.put(KEY_NORMAL_TRIP, data);
        }

        if (detourTrip == null)
        {
            store.remove(KEY_DETOUR_TRIP);
        }
        else
        {
            byte[] data = SerializableManager.getInstance().getAddressSerializable().toBytes(detourTrip);
            store.put(KEY_DETOUR_TRIP, data);
        }
        if(tripTime == -1)
        {
        	store.remove(KEY_LAST_TRIP_TIME);        	
        }
        else
        {
        	store.put(KEY_LAST_TRIP_TIME, ("" + tripTime).getBytes());
        }
        store.save();
    }
    
    public boolean isDetourTripExist()
    {
        if(detourTrip == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public long tripTime()
    {
    	return tripTime;
    }
    
    public void initTripPreference()
    {
        IPreferenceSerializable preferenceSerializable = SerializableManager.getInstance().getPreferenceSerializable();
        Preference defaultRouteStyle = DaoManager.getInstance().getPreferenceDao()
                .getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        if (defaultRouteStyle != null)
        {
            byte[] data = preferenceSerializable.toBytes(defaultRouteStyle);
            routeStylePref = preferenceSerializable.createPreference(data);
        }

        Preference defaultRouteSetting = DaoManager.getInstance().getPreferenceDao()
                .getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
        if (defaultRouteSetting != null)
        {
            byte[] data = preferenceSerializable.toBytes(defaultRouteSetting);
            routeSettingsPref = preferenceSerializable.createPreference(data);
        }

        Preference defaultAudioSetting = DaoManager.getInstance().getPreferenceDao()
                .getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        if (defaultAudioSetting != null)
        {
            byte[] data = preferenceSerializable.toBytes(defaultAudioSetting);
            audioSettingsPref = preferenceSerializable.createPreference(data);
        }
        
        Preference defaultMapStyleSetting = DaoManager.getInstance().getPreferenceDao()
                .getPreference(Preference.ID_PREFERENCE_MAPSTYLE);
        if (defaultMapStyleSetting != null)
        {
            byte[] data = preferenceSerializable.toBytes(defaultMapStyleSetting);
            mapStyleSettingPref = preferenceSerializable.createPreference(data);
        }
    }
    
    public Preference getPreference(int id)
    {
        switch (id)
        {
            case Preference.ID_PREFERENCE_AUDIO_GUIDANCE:
            {
                return audioSettingsPref;
            }
            case Preference.ID_PREFERENCE_ROUTETYPE:
            {
                return routeStylePref;
            }
            case Preference.ID_PREFERENCE_ROUTE_SETTING:
            {
                return routeSettingsPref;
            }
            case Preference.ID_PREFERENCE_MAPSTYLE:
            {
                return mapStyleSettingPref;
            }
            default:
            {
                return null;
            }
        }
    }
    
    public int getIntValue(int id)
    {
        Preference pref = getPreference(id);

        int intVal = PreferenceDao.INVALID_INT_VALUE;

        try
        {
            if (pref != null)
            {
                intVal = pref.getIntValue();
                if (id == Preference.ID_PREFERENCE_ROUTE_SETTING)
                {
                    int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
                    boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED
                            || trafficEnableValue == FeaturesManager.FE_PURCHASED;
                    if (!isTrafficEnabled)
                    {
                        if ((pref.getIntValue() & Preference.AVOID_TRAFFIC_DELAYS) != 0)
                        {
                            intVal = pref.getIntValue() - Preference.AVOID_TRAFFIC_DELAYS;
                        }
                    }
                }
            }
        }
        catch (Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }

        return intVal;
    }
}
