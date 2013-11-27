/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnCellInfo.java
 *
 */
package com.telenav.radio;

/**
 * Contains the radio information both for CDMA/GSM.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 11, 2010
 */
public class TnCellInfo
{
    /**
     * GSM radio mode
     */
    public final static int MODE_GSM = 0;
    
    /**
     * CDMA radio mode
     */
    public final static int MODE_CDMA = 1;
    
    /**
     * Retrieves the network Code.
     */
    public String networkCode;

    /**
     * Retrieves the country code (i.e. country initials) of the specified network.
     */
    public String countryCode;

    /**
     * Retrieves the network type.
     */
    public String networkType;

    /**
     *  Retrieves the Base Station Identity Code.
     */
    public String baseStationId;

    /**
     * Retrieves the current cell ID.
     */
    public String cellId;

    /**
     * Retrieves the Location Area Code.
     */
    public String locationAreaCode;
    
    /**
     * Retrieves the alphabetic name of current registered operator.
     */
    public String networkOperatorName;
    
    /**
     * Retrieves the network radio mode.
     */
    public int networkRadioMode;
}
