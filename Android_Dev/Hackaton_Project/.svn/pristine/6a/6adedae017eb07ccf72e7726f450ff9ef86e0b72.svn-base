/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * ResUtil.java
 *
 */
package com.telenav.searchwidget.res;

import com.telenav.searchwidget.data.datatypes.address.Stop;
import com.telenav.searchwidget.gps.android.LocationDao;
import com.telenav.searchwidget.serverproxy.data.GeocodeBean;
import com.telenav.searchwidget.serverproxy.data.ReverseGeocodeBean;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Jul 27, 2011
 */

public class ResUtil
{
    public static String getapproximateAddress(ReverseGeocodeBean rgcBean)
    {
    	StringBuilder address = new StringBuilder("");
    	if (rgcBean.city != null && rgcBean.city.trim().length() > 0)
    	{
    		address.append(rgcBean.city);
    	}
    	if (LocationDao.getInstance().isOneBoxStandard())
    	{
    		if (rgcBean.state != null && rgcBean.state.trim().length() > 0)
    		{
    			if(address.length() > 0)
    			{
    				address.append(", ");
    			}
    			address.append(rgcBean.state);
    		}
    	}
    	else
    	{
    		if (rgcBean.country != null && rgcBean.country.trim().length() > 0)
    		{
    			if(address.length() > 0)
    			{
    				address.append(", ");
    			}
    			address.append(rgcBean.country);
    		}
    	}
		return address.toString();
    }
    
    public static String getOnelineAddress(GeocodeBean.MatchedAddress addr)
    {
        StringBuffer sb = new StringBuffer();
        
        String firstLine = addr.streetAddress;
        if (firstLine == null)
        {   
            if (addr.streetNumber != null && addr.streetNumber.length() > 0)
            {
                firstLine = addr.streetNumber + ", " + addr.streetName;
            }
            else
            {
                firstLine = addr.streetName;
            }
        }
        
        if (firstLine != null)
        {
            sb.append(firstLine);
        }
        
        String lastLine = addr.city;
        if (lastLine != null && lastLine.length() > 0)
        {
            if (addr.state != null && addr.state.length() > 0)
            {
                lastLine = lastLine + ", " + addr.state;
            }
        }
        
        if (lastLine != null && lastLine.length() > 0)
        {
            if (firstLine != null && firstLine.length() > 0)
            {
                sb.append(", ").append(lastLine);
            }
            else
            {
                sb.append(lastLine);
            }
        }
        
        if (addr.postalCode != null && addr.postalCode.length() > 0)
        {
            if (sb.length() > 0)
            {
                sb.append(", ").append(addr.postalCode);
            }
            else
            {
                sb.append(addr.postalCode);
            }
        }
        
//        if (addr.country != null && addr.country.length() > 0)
//        {
//            if (sb.length() > 0)
//            {
//                sb.append(", ").append(addr.country);
//            }
//            else
//            {
//                sb.append(addr.country);
//            }
//        }
        
        return sb.toString();
    }
    
    public static String getOnelineAddress(Stop addr)
    {
        StringBuffer sb = new StringBuffer();
        
        String firstLine = addr.getFirstLine();
        
        if (firstLine != null)
        {
            sb.append(firstLine);
        }
        
        String lastLine = addr.getCity();
        if (lastLine != null && lastLine.length() > 0)
        {
            if (addr.getProvince() != null && addr.getProvince().length() > 0)
            {
                lastLine = lastLine + ", " + addr.getProvince();
            }
        }
        
        if (lastLine != null && lastLine.length() > 0)
        {
            if (firstLine != null && firstLine.length() > 0)
            {
                sb.append(", ").append(lastLine);
            }
            else
            {
                sb.append(lastLine);
            }
        }
        
        if (addr.getPostalCode() != null && addr.getPostalCode().length() > 0)
        {
            if (sb.length() > 0)
            {
                sb.append(", ").append(addr.getPostalCode());
            }
            else
            {
                sb.append(addr.getPostalCode());
            }
        }
        
//        if (addr.country != null && addr.country.length() > 0)
//        {
//            if (sb.length() > 0)
//            {
//                sb.append(", ").append(addr.country);
//            }
//            else
//            {
//                sb.append(addr.country);
//            }
//        }
        
        return sb.toString();
    }

	public static String getOneboxHint() {
		String hint;
	    if (LocationDao.getInstance().isOneBoxStandard())
	    {
	    	hint = ResourceManager.getInstance().getCurrentBundle().getString(IStringSearchWidget.RES_ONE_BOX_HINT, 
	                IStringSearchWidget.FAMILY_SEARCHWIDGET);
	    }
	    else
	    {
	    	hint = ResourceManager.getInstance().getCurrentBundle().getString(IStringSearchWidget.RES_ONE_BOX_INTERNATIONAL_HINT, 
	                IStringSearchWidget.FAMILY_SEARCHWIDGET);
	    }
		return hint;
	}
}
