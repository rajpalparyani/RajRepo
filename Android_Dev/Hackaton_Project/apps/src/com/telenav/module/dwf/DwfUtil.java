/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AddressJsonUtil.java
 *
 */
package com.telenav.module.dwf;

import org.json.JSONException;
import org.json.JSONObject;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.module.login.AccountFetcher;

/**
 *@author fangquanm
 *@date Jul 17, 2013
 */
public class DwfUtil
{
    public static Address jsonToAddress(String addressDt)
    {
        if(addressDt != null && addressDt.length() > 0)
        {
            Address address = new Address();
            Stop stop = new Stop();
            address.setStop(stop);
            address.setLabel("");
            try
            {
                JSONObject addressJson = new JSONObject(addressDt);
                
                if (addressJson.has("label"))
                {
                    stop.setLabel(addressJson.getString("label"));
                    address.setLabel(stop.getLabel());
                }
                if (addressJson.has("lat"))
                {
                    stop.setLat((int)(addressJson.getDouble("lat") * 100000));
                }
                if (addressJson.has("lon"))
                {
                    stop.setLon((int)(addressJson.getDouble("lon") * 100000));
                }
                if (addressJson.has("city"))
                {
                    stop.setCity(addressJson.getString("city"));
                }
                if (addressJson.has("province"))
                {
                    stop.setProvince(addressJson.getString("province"));
                }
                if (addressJson.has("zip"))
                {
                    stop.setPostalCode(addressJson.getString("zip"));
                }
                if (addressJson.has("country"))
                {
                    stop.setCountry(addressJson.getString("country"));
                }
                if (addressJson.has("street"))
                {
                    stop.setFirstLine(addressJson.getString("street"));
                    stop.setStreetName(addressJson.getString("street"));
                }
                if(addressJson.has("cross_street"))
                {
                    stop.setCrossStreetName(addressJson.getString("cross_street"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            
            return address;
        }
        
        return null;
    }
    
    public static JSONObject addressToJson(Address dest)
    {
        JSONObject addressJson = new JSONObject();
        try
        {
            addressJson.put("lat", (double) dest.getStop().getLat() / 100000d);

            addressJson.put("lon", (double) dest.getStop().getLon() / 100000d);
            addressJson.put("label", dest.getStop().getLabel() == null ? "" : dest.getStop().getLabel());
            addressJson.put("street", dest.getStop().getFirstLine() == null ? "" : dest.getStop().getFirstLine());
            addressJson.put("city", dest.getStop().getCity() == null ? "" : dest.getStop().getCity());
            addressJson.put("province", dest.getStop().getProvince() == null ? "" : dest.getStop().getProvince());
            addressJson.put("zip", dest.getStop().getPostalCode() == null ? "" : dest.getStop().getPostalCode());
            addressJson.put("cross_street", dest.getStop().getCrossStreetName() == null ? "" : dest.getStop()
                    .getCrossStreetName());
            addressJson.put("country", dest.getStop().getCountry() == null ? "" : dest.getStop().getCountry());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return addressJson;
    }
    
    public static String getUserName()
    {
        String userName = null;
        if (userName == null || userName.length() == 0)
        {
            PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
            userName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_USERNAME);
            if (userName == null || userName.length() == 0)
            {
                userName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME);
            }
        }
        if (userName == null || userName.length() == 0)
        {
            userName = AccountFetcher.getUserName(AndroidPersistentContext.getInstance().getContext());
        }
        if (userName == null || userName.length() == 0)
        {
            userName = AccountFetcher.getEmail(AndroidPersistentContext.getInstance().getContext());
        }
        
        return userName;
    }
}
