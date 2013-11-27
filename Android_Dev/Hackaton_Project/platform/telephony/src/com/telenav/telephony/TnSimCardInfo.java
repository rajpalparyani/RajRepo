/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnSimCardInfo.java
 *
 */
package com.telenav.telephony;

/**
 * Provides information about the SIM Card.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 11, 2010
 */
public class TnSimCardInfo
{
    /**
     * Retrieves the unique subscriber ID, for example, the IMSI for a GSM phone.
     */
    public String simCardId;

    /**
     * Returns the ISO country code equivalent for the SIM provider's country code.
     */
    public String countryIso;

    /**
     * Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal
     * digits.
     */
    public String operator;

    /**
     * Returns the Service Provider Name (SPN). 
     */
    public String operatorName;
}
