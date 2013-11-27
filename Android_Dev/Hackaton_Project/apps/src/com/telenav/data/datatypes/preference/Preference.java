/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Preference.java
 *
 */
package com.telenav.data.datatypes.preference;

import java.util.Date;

import com.telenav.logger.Logger;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-8-11
 */
public class Preference
{
    public static final short ID_PREFERENCE_ROUTETYPE                            = 0;
    public static final short ID_PREFERENCE_DISTANCEUNIT                         = 1;
    public static final short ID_PREFERENCE_MAPSTYLE                             = 2;
    public static final short ID_PREFERENCE_AUDIO_GUIDANCE                       = 3;
    public static final short ID_PREFERENCE_GUIDE_TONE                           = 4;
    public static final short ID_PREFERENCE_HELPCARD                             = 5;
    public static final short ID_PREFERENCE_PHONENUM                             = 6;//
    public static final short ID_PREFERENCE_TRAFFICALERT                         = 7;
    public static final short ID_PREFERENCE_LANGUAGE                             = 9;
    public static final short ID_PREFERENCE_REGION                               = 10;
    public static final short ID_PREFERENCE_NETWORK                              = 11;
    public static final short ID_PREFERENCE_GPS_SOURCE                           = 12;
    public static final short ID_PREFERENCE_BACKLIGHT                            = 13;
    public static final short ID_PREFERENCE_DEFAULT_ORIGIN                       = 14;
    public static final short ID_PREFERENCE_ROUTE_SETTING                        = 15;
    public static final short ID_PREFERENCE_SUSPEND_TIMEOUT                      = 16;
    public static final short ID_PREFERENCE_INIT_VOLUME                          = 17;
    public static final short ID_PREFERENCE_INPHONE_AUDIO                        = 18;
    
    public static final short ID_PREFERENCE_SPEECH_RCOGNITION_INPUT_METHOD            = 19; // Speak In Directly,Call In(Toll Free),Call In(Non-Toll Free)
    public static final short ID_PREFERENCE_SPEECH_RCOGNITION_ADDRESS_INPUT           = 20;//Always Ask ,Always Speak In, Always Type In.
    public static final short ID_PREFERENCE_SPEECH_RCOGNITION_SEARCH                  = 21;//Always Ask ,Always Speak In, Always Type In.
    
    public static final short ID_PREFERENCE_MAP_COLORS                           = 23; //Preference to store map colors
    public static final short ID_PREFERENCE_TYPE_EMAIL                                = 24;
    
    public static final short ID_PREFERENCE_TYPE_FIRSTNAME                       = 25;
    public static final short ID_PREFERENCE_TYPE_LASTNAME                        = 26; 

    public static final short ID_PREFERENCE_PHONE_NUMBER                            = 27;
    public static final short ID_PREFERENCE_CONVENIENCE_KEY                           = 28;
    
    public static final short ID_PREFERENCE_ROAMING_ALERT                        = 29;
    
    public static final short ID_PREFERENCE_ADS_MERCHANT                        = 30;
    public static final short ID_PREFERENCE_ADS_DEALS                           = 31;
    public static final short ID_PREFERENCE_ADS_MENU                            = 32;
    public static final short ID_PREFERENCE_ADS_SPONSORED                       = 33;
    
    //preference for TN6.0 start
    public static final short ID_PREFERENCE_TRAFFIC_CAMERA_ALERT                 = 100;
    
    public static final short ID_PREFERENCE_SPEED_TRAP_ALERT                     = 101;
    
    public static final short ID_PREFERENCE_ANNOUNCE_SEARCH_RESULTS              = 102;
    
    public static final short ID_PREFERENCE_LANE_ASSIST                          = 103;
    
    public static final short ID_PREFERENCE_SPEED_LIMITS                         = 104;
    
    public static final short ID_FAVORITES_ICONS_ON_MAPS                         = 105;
    
    //preference for TN7.0 start
    public static final short ID_PREFERENCE_TYPE_USERNAME                       = 120;
    
    public static final short ID_PREFERENCE_CAR_MODEL                           = 121;
    
    //adapt unused number to save in DAO, avoid conflict with c-serfer
    public static final short ID_PREFERENCE_CSRID                           = 1000;
    
    public static final short ID_PREFERENCE_SHARING                         = 2000;
    
    //=============================================================================================//
    
    //--------------  distance unit -------------------
    public static final byte UNIT_METRIC                = 0;
    public static final byte UNIT_USCUSTOM              = 1;
    
    //--------------  map styles  ---------------------
//    public static final byte MAP_TYPE_NONE              = 0;
    public static final byte MAP_TYPE_2D                = 1;
    public static final byte MAP_TYPE_3D                = 2;
//    public static final byte MAP_TYPE_2D_3D_AT_TURNS    = 3;
    
    //-----map color setting----
    public static final int MAP_COLORS_DAYTIME            = 1;
    public static final int MAP_COLORS_NIGHTTIME          = 2;
    public static final int MAP_COLORS_AUTO               = 3;
    
    //-------------- audio styles ---------------------
    public static final byte AUDIO_NONE                 = 0;
    public static final byte AUDIO_TRAFFIC_ONLY         = 1;
    public static final byte AUDIO_DIRECTIONS_ONLY      = 2;
    public static final byte AUDIO_DIRECTIONS_TRAFFIC   = 3;
    
    //-------------- traffic alert ---------------------
    public static final byte TRAFFIC_ALERT_ON           = 0;
    public static final byte TRAFFIC_ALERT_OFF          = 1;
    
    //-------------- backlight -------------------------
    public static final byte BACKLIGHT_ON       = 0;
    public static final byte BACKLIGHT_OFF      = 1;
    public static final byte BACKLIGHT_TURNS    = 2;
    
    // ------------ route setting ------------------
    public static final int USE_CARPOOL_LANES     = 0;
    public static final int AVOID_CARPOOL_LANES   = 1;
    public static final int AVOID_TOLLS           = 2;
    public static final int AVOID_TRAFFIC_DELAYS  = 4;
    
    //----- traffic camera alert -----------------
    public static final int TRAFFIC_CAMERA_ALERT_ON           = 0;
    public static final int TRAFFIC_CAMERA_ALERT_OFF         = 1; 
    
    //----- speed trap alert -----------------
    public static final int SPEED_TRAP_ALERT_ON           = 0;
    public static final int SPEED_TRAP_ALERT_OFF         = 1;  
    
    //----- speed limit -----------------
    public static final byte SPEED_LIMIT_ON           = 0;
    public static final byte SPEED_LIMIT_OFF          = 1;
    
    //----- lane assist -----------------
    public static final byte LANE_ASSIST_ON = 0;
    public static final byte LANE_ASSIST_OFF = 1;
    
    //----- audio during call ------------
    public static final byte AUDIO_SUSPEND = 0;
    public static final byte AUDIO_STILL_PLAY = 1;
    
    //============================================================================================//
    private int id;
    
    private String name = "";

    private String[] optionNames;

    private String valueStr = "";
    
    private int valueInt;

    private int[] optionValues;
 
    private boolean isUpdated= false;
    
    public boolean isUpdated()
    {
        return isUpdated;
    }


    public void setUpdated(boolean isUpdated)
    {
        this.isUpdated = isUpdated;
    }


    public Preference(int id, String name)
    {
    	this.name = name;
    	this.id = id;
    }
    
    
    public int getId()
    {
        return id;
    }
    
    
    public String getName()
    {
        return name;
    }
    
    public void setStrValue(String valueStr)
    {
        this.valueStr = valueStr;
        setUpdated(true);
    }
    
    public String getStrValue()
    {
        return this.valueStr != null ? valueStr : "";
    }
    
    public void setIntValue(int valueInt)
    {
        if (id == Preference.ID_PREFERENCE_DISTANCEUNIT)
        {
            try
            {
                Logger.log(Logger.INFO, this.getClass().getName(), new Date().toLocaleString()+" Write Value:"+valueInt+"\t"+getStackInfo(this));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        this.valueInt = valueInt;
        setUpdated(true);
    }
    
    static int lastDistanceUnit = -1;
    public int getIntValue()
    {
        if (id == Preference.ID_PREFERENCE_DISTANCEUNIT && valueInt != lastDistanceUnit)
        {
            lastDistanceUnit = valueInt;
            try
            {
                Logger.log(Logger.INFO, this.getClass().getName(), new Date().toLocaleString()+" Read Value:"+valueInt+"\t"+getStackInfo(this));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return this.valueInt;
    }
    
    public static String getStackInfo(Object object)
    {
        StringBuffer info = new StringBuffer(100);
        info.append("BEGIN TNANDROID-2692:\n");
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack)
        {
            info.append(element.toString()).append('\n');
        }
        info.append("END TNANDROID-2692\n");
        return info.toString();
    }
    
    public void setOptionNames(String[] optionNames)
    {
        this.optionNames = optionNames;
    }
    
    public String[] getOptionNames()
    {
        return this.optionNames;
    }
    
    public void setOptionValues(int[] optionValues)
    {
        this.optionValues = optionValues;
    }
    
    public int[] getOptionValues()
    {
        return this.optionValues;
    }
}
