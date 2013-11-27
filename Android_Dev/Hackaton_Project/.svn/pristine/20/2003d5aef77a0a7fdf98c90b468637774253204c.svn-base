/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteSettingModel.java
 *
 */
package com.telenav.module.preference.routesetting;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.mvc.AbstractCommonNetworkModel;

/**
 *@author yning
 *@date 2011-3-3
 */
public class RouteSettingModel extends AbstractCommonNetworkModel implements IRouteSettingConstants
{

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        // TODO Auto-generated method stub
        
    }

    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_SAVE_PREFERENCE:
            {
                savePreference();
                break;
            }
        }
    }
    
    protected void savePreference() 
    {
        boolean hasDataChanged= false;
        
        boolean isRouteSession = getBool(KEY_B_IS_NAV_SESSION);
        
        Preference routeTypePref;
        Preference routeSettingPref;
        Preference audioPref;
        
        if(isRouteSession)
        {
            routeTypePref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
            routeSettingPref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
            audioPref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        }
        else
        {
            routeTypePref = DaoManager.getInstance().getPreferenceDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
            routeSettingPref = DaoManager.getInstance().getPreferenceDao().getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
            audioPref = DaoManager.getInstance().getPreferenceDao().getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        }
        
        //save audio setting
        int selected = fetchInt(KEY_I_SELECTED_VOICE_CHOICE);
        int[] values = audioPref.getOptionValues();
        if(selected >= 0 && selected < values.length)
        {
            int value = values[selected];
            if(value != audioPref.getIntValue())
            {
                audioPref.setIntValue(value);
                
                String[] optionNames = audioPref.getOptionNames();
                if(optionNames != null && optionNames.length > selected)
                {
                    audioPref.setStrValue(optionNames[selected]);
                }
            }
        }
        
        //save route setting
        
        int routeTypeIndex = fetchInt(KEY_I_SELECTED_ROUTE_TYPE_CHOICE);
        boolean[] avoidChoicesIndex = (boolean[]) fetch(KEY_A_SELECTED_AVOID);
        if(routeTypeIndex != -1)
        {
            int[] types = routeTypePref.getOptionValues();
            if(routeTypeIndex < types.length)
            {
                int newValue = types[routeTypeIndex];
                if(routeTypePref.getIntValue() != newValue)
                {
                    routeTypePref.setIntValue(newValue);
                    String[] optionNames = routeTypePref.getOptionNames();
                    if(optionNames != null && optionNames.length > routeTypeIndex)
                    {
                        routeTypePref.setStrValue(optionNames[routeTypeIndex]);
                    }
                    hasDataChanged = true;
                }
            }
        }

        int index = -1;        
        int optionVals[] = routeSettingPref.getOptionValues();
		int len = optionVals.length;
        for (int i = 0; i < len; i++)
        {
            if (optionVals[i] == Preference.USE_CARPOOL_LANES)
            {
                index = i;
				break;
            }
        }

        if (avoidChoicesIndex != null)
        {
            int value = 0;;
            if(index >= 0)
            {
                value = avoidChoicesIndex[index] ? Preference.USE_CARPOOL_LANES : Preference.AVOID_CARPOOL_LANES;
            }
            else
            {
                value = Preference.USE_CARPOOL_LANES;
            }
            
            for (int i = 0; i < avoidChoicesIndex.length; i++)
            {
                if (i == index || !avoidChoicesIndex[i])
                    continue;
                value |= optionVals[i];
            }
            if (value != routeSettingPref.getIntValue())
            {
                hasDataChanged = true;
                routeSettingPref.setIntValue(value);
            }
        }
        this.put(KEY_B_ROUTE_SETTING_CHANGED, hasDataChanged);
    }
}
