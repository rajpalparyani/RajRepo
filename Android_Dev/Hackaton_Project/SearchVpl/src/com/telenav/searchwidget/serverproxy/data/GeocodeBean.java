/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GeocodeBean.java
 *
 */
package com.telenav.searchwidget.serverproxy.data;

import java.util.Vector;

/**
 *@author hchai
 *@date 2011-7-25
 */
public class GeocodeBean extends AbstractBean
{
	public Vector addresses;

	public String toString()
    {
	    StringBuffer sb = new StringBuffer();
	    
	    sb.append("GeocodeBean [");
	    if (addresses != null)
	    {
	        for (int i = 0; i < addresses.size(); i ++)
	        {
	            MatchedAddress addr = (MatchedAddress)addresses.elementAt(i);
	            sb.append("Matched address[" + i + "]: \r\n");
	            sb.append(addr.toString());
	        }
	    }
	    else
	    {
	        sb.append("No matched address!");
	    }
	    sb.append("]");
	    
        return sb.toString();
    }
	
	public static class MatchedAddress
	{
	    public String streetNumber;
	    public String streetName;
	    public String streetAddress;
	    public String crossStreetName;
	    public String city;
	    public String state;
	    public String postalCode;
	    public String country;
	    public int lat;
	    public int lon;
	    
	    public String toString()
	    {
	        StringBuffer sb = new StringBuffer();
	        sb.append("streetNumber=" + streetNumber).
	            append(", streetName=" + streetName).
	            append(", streetAddress=" + streetAddress).
	            append(", crossStreetName=" + crossStreetName).
	            append(", city=" + city).
	            append(", state=" + state).
	            append(", postalCode=" + postalCode).
	            append(", country=" + country).
	            append(", lat=" + lat).
	            append(", lon=" + lon).
	            append("\r\n");
	        
	        return sb.toString();
	    }
	}

}

