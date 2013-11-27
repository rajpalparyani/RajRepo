package com.telenav.navservice.util;

import com.telenav.radio.TnCellInfo;

public class Constants
{
    //platform
    public static final byte OS_UNKNOWN = 0;
    public static final byte OS_ANDROID = 1;
    public static final byte OS_BREW = 2;
    public static final byte OS_IPHONE = 3;
    public static final byte OS_J2ME = 4;
    public static final byte OS_RIM = 5;
    public static final byte OS_SYMBIAN = 6;
    public static final byte OS_WEBOS = 7;
    public static final byte OS_WIN_MOBILE = 8;
    public static final byte OS_LINUX = 9;
    public static final byte OS_DANGER = 10;
    public static final byte OS_CELLTOP = 11;

    public static final byte RADIO_TYPE_UNKNOWN = 0;
    public static final byte RADIO_TYPE_CDMA = 1;
    public static final byte RADIO_TYPE_GSM = 2;
    
    public static byte convertRadioType(int radioType)
    {
        switch(radioType)
        {
            case TnCellInfo.MODE_CDMA:
                return RADIO_TYPE_CDMA;
            case TnCellInfo.MODE_GSM:
                return RADIO_TYPE_GSM;
            default:
                return RADIO_TYPE_UNKNOWN;
        }
    }

    public static final byte NETWORK_TYPE_UNKNOWN = 0;
    public static final byte NETWORK_TYPE_1xRTT = 7; // 2g
    public static final byte NETWORK_TYPE_CDMA = 4; // 2g
    public static final byte NETWORK_TYPE_EDGE = 2; // 2g
    public static final byte NETWORK_TYPE_EHRPD = 14; // 3g
    public static final byte NETWORK_TYPE_EVDO_0 = 5; // 3g
    public static final byte NETWORK_TYPE_EVDO_A = 6; // 3g
    public static final byte NETWORK_TYPE_EVDO_B = 12; // 3g
    public static final byte NETWORK_TYPE_GPRS = 1; // 2g
    public static final byte NETWORK_TYPE_HSDPA = 8; // 3g
    public static final byte NETWORK_TYPE_HSPA = 10;// 3g
    public static final byte NETWORK_TYPE_HSUPA = 9;// 3g
    public static final byte NETWORK_TYPE_IDEN = 11;// 2g
    public static final byte NETWORK_TYPE_LTE = 13;// 4g
    public static final byte NETWORK_TYPE_UMTS = 3;// 3g
    
    public static byte convertNetworkType(String type)
    {
        //the network type definitions are same as Android's
        try{
            return Byte.parseByte(type);
        }
        catch(Exception e)
        {
        }
        return NETWORK_TYPE_UNKNOWN;
    }

    public static final String NETWORK_GEN_1G = "1G";
    public static final String NETWORK_GEN_2G = "2G";
    public static final String NETWORK_GEN_3G = "3G";
    public static final String NETWORK_GEN_4G = "4G";
    public static final String NETWORK_GEN_UNKNOWN = "UNKNOWN";

    
    //carrier
    public static final byte UNKNOWN_CARRIER = 0;

    public static final byte ALLTEL = 12;
    public static final byte ATT = 5;
    public static final byte BELL_MOBILITY = 26;
    public static final byte BOOST = 3;
    public static final byte CELLULAR_ONE = 47;
    public static final byte CINCINNATI_BELL = 14;
    public static final byte NEXTEL = 8;
    public static final byte ROGERS = 29;
    public static final byte SPRINT = 4;
    public static final byte T_MOBILE = 6;
    public static final byte TELCEL = 44;
    public static final byte TELUS = 9;
    public static final byte US_CELLULAR = 49;
    public static final byte VERIZON = 1;
    public static final byte VIRGIN_MOBILE = 25;
    public static final byte VIVO = 7;

    public static final byte JASPER = 126;
    public static final byte VODAFONE = 127;
    
    public static final byte conviertCarrier(String carrier)
    {
        try{
            return Byte.parseByte(carrier);
        }catch(Exception e)
        {
            
        }
        return UNKNOWN_CARRIER;
    }

    //application
    public static final byte UNKNOWN_APP = 0;
    public static final byte EVO = 1;
    public static final byte MAPS = 2;
    public static final byte NAVIGATOR = 3;
    public static final byte ON_MY_WAY = 4;
    public static final byte TNT = 5;
    public static final byte TRACKING_ENGINE = 6;
    public static final byte WHEREBOUTZ = 7;

}
