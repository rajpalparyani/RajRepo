/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * GeoProviderHandler.java
 *
 */
package com.telenav.searchwidget.serverproxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telenav.searchwidget.serverproxy.data.EtaBean;
import com.telenav.searchwidget.serverproxy.data.GeocodeBean;
import com.telenav.searchwidget.serverproxy.data.MapBean;
import com.telenav.searchwidget.serverproxy.data.ReverseGeocodeBean;


/**
 * The Geocoding's response parser.
 * It generates useful bean object by parsing
 * 
 *@author hchai
 *@date 2011-7-22
 */
public class GeocodeHandler extends DefaultHandler
{

	private static final List<String> errors = new ArrayList<String>();

    private EtaCallback etaBean;

	private GeocodeCallback geocodeBean;

	private ReverseGeocodeCallback reverseGeocodeBean;

	private MapCallback mapBean;

	private String tagName;

	private IGeocodeCallback callback;
	
	static 
	{
	    //see http://developer.telenav.com/sdkweb/geocoding?app=geocoding&page=errorHandling
	    //Service errors
	    errors.add("ERR_SERVICE_ERROR"); //General service internal error
	    errors.add("SERVICE_ERROR");     //General service internal error
	    errors.add("SERVICE_INVALID_CREDENTIALS");//Client passes invalid credential data. This could include invalid apiKey, invalid Authentication Token, invalid user ID or password.
	    errors.add("SERVICE_UNAUTHORIZED_USAGE");//Server rejects client's request
	    errors.add("SERVICE_INVALID_DATA");  //Invalid user input data
	    errors.add("SERVICE_INVALID_DATA_SIZE");//Invalid user input data size
	    errors.add("SERVICE_INVALID_PTN");  //  Invalid Public Phone Number
	    errors.add("SERVICE_INVALID_USER");  //  Invalid user name or user ID
	    errors.add("SERVICE_MISSING_DATA");  //  Missing required data
	    errors.add("SERVICE_TIMEOUT");       //  Operation timeout
	    errors.add("SERVICE_UNSUPPORTED_OPERATION");  //  Operation not supported
	    errors.add("SERVICE_DEPRECATED");  //  Deprecated service
	    
	    //System errors
	    errors.add("GENERAL_OUT_OF_MEMORY");  //  Server "out of memory" error
	    errors.add("GENERAL_SYSTEM_ERROR");  //  General system failure
	    
	    //Database errors
	    errors.add("DB_CONNECTION_ERROR");  //  Database connection error
	    errors.add("DB_DATA_NOT_FOUND");  //  Given data not found in database
	    errors.add("DB_DUPLICATE_ENTRY");  //  Duplicated entry found in database
	    errors.add("DB_ERROR");  //  General database failure
	    
	}

	public GeocodeHandler(int providerType)
	{
		switch (providerType)
		{
		case GeocodeProvider.TYPE_GEOCODE:
		{
			geocodeBean = new GeocodeCallback();
			callback = geocodeBean;
			break;
		}

		case GeocodeProvider.TYPE_GET_ETA:
		{
			etaBean = new EtaCallback();
			callback = etaBean;
			break;
		}

		case GeocodeProvider.TYPE_GET_MAP:
		{
			mapBean = new MapCallback();
			callback = mapBean;
			break;
		}
		case GeocodeProvider.TYPE_REVERSE_GEOCODE:
		{
			reverseGeocodeBean = new ReverseGeocodeCallback();
			callback = reverseGeocodeBean;
			break;
		}
		default:
			break;
		}
	}
	
	public boolean isRespnseError()
	{
	    return errors.contains(callback.getCode());
	}

	public EtaBean getEtaBean()
	{
		return etaBean;
	}

	public GeocodeBean getGeocodeBean()
	{
		return geocodeBean;
	}

	public ReverseGeocodeBean getReverseGeocodeBean()
	{
		return reverseGeocodeBean;
	}

	public MapBean getMapBean()
	{
		return mapBean;
	}

	public void startDocument() throws SAXException
	{
		super.startDocument();
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		tagName = "";
		callback.endElement(uri, localName, qName);
		super.endElement(uri, localName, qName);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		tagName = qName;
		callback.startElement(uri, localName, qName, attributes);
		super.startElement(uri, localName, qName, attributes);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		callback.characters(ch, start, length);
		super.characters(ch, start, length);
	}

	class EtaCallback extends EtaBean implements IGeocodeCallback
	{

		public void characters(char[] ch, int start, int length)
		{
			if ("code".equalsIgnoreCase(tagName))
			{
				code = new String(ch, start, length);
			}
			else if ("travelTime".equalsIgnoreCase(tagName))
			{
				String travelTimeStr = new String(ch, start, length);
				try
				{
				    travelTime = Long.valueOf(travelTimeStr);
				}
				catch(Exception e)
				{				    
				}
			}
			else if ("distance".equalsIgnoreCase(tagName))
			{
				String distanceStr = new String(ch, start, length);
				try
				{
				    distance = Float.valueOf(distanceStr);
				}
				catch(Exception e)
				{				    
				}
			}
			else if ("trafficTime".equalsIgnoreCase(tagName))
			{
                String delayTimeStr = new String(ch, start, length);
                try
                {
                    trafficTime = Long.valueOf(delayTimeStr);
                }
                catch(Exception e)
                {                   
                }			    
			}
		}

		public void endElement(String uri, String localName, String qName)
		{
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes)
		{
		}

	}

	class GeocodeCallback extends GeocodeBean implements IGeocodeCallback
	{
	    private GeocodeBean.MatchedAddress current;
	    
		public void characters(char[] ch, int start, int length)
		{
//            System.out.println("#S, geocode, characters, tagName = " + tagName + ", val = " + new String(ch, start, length));
            
			if ("code".equalsIgnoreCase(tagName))
			{
				code = new String(ch, start, length);
			}
			else if ("streetNumber".equalsIgnoreCase(tagName))
			{
			    current.streetNumber = new String(ch, start, length);
			}
			else if ("streetName".equalsIgnoreCase(tagName))
			{
			    current.streetName = new String(ch, start, length);
			}
			else if ("streetAddress".equalsIgnoreCase(tagName))
			{
			    current.streetAddress = new String(ch, start, length);
			}
			else if ("crossStreetName".equalsIgnoreCase(tagName))
			{
			    current.crossStreetName = new String(ch, start, length);
			}
			else if ("city".equalsIgnoreCase(tagName))
			{
			    current.city = new String(ch, start, length);
			}
			else if ("state".equalsIgnoreCase(tagName))
			{
			    current.state = new String(ch, start, length);
			}
			else if ("postalCode".equalsIgnoreCase(tagName))
			{
			    current.postalCode = new String(ch, start, length);
			}
			else if ("country".equalsIgnoreCase(tagName))
			{
			    current.country = new String(ch, start, length);
			}
		}

		public void endElement(String uri, String localName, String qName)
		{
		    if ("matchFound".equalsIgnoreCase(qName))
		    {
//	            System.out.println("#S, geocode, endElement, " + this.toString());
		    }
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes)
		{
//		    System.out.println("#S, geocode, startElement, tagName = " + tagName);
		    if ("matchFound".equalsIgnoreCase(tagName))
		    {
		        current = new GeocodeBean.MatchedAddress();
		        if (this.addresses == null)
		        {
		            this.addresses = new Vector();
		        }
		        this.addresses.addElement(current);
		    }
		    
			if ("geoCode".equalsIgnoreCase(tagName))
			{
				String latString = attributes.getValue("lat");
				String lonString = attributes.getValue("lon");

				try
				{
					float lat = Float.valueOf(latString);
					float lon = Float.valueOf(lonString);
					
					current.lat = (int)(lat * 100000);
					current.lon = (int)(lon * 100000);
				}
				catch (NumberFormatException e)
				{
				}
			}
		}

	}

	class MapCallback extends MapBean implements IGeocodeCallback
	{

		public void characters(char[] ch, int start, int length)
		{
			if ("image".equalsIgnoreCase(tagName))
			{
				imageString.append(new String(ch, start, length));
			}
			else if ("code".equalsIgnoreCase(tagName))
			{
				code = new String(ch, start, length);
			}
		}

		public void endElement(String uri, String localName, String qName)
		{
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes)
		{
	        if ("image".equalsIgnoreCase(tagName))
	        {
	            imageString = new StringBuffer();
	        }
		}

	}

	class ReverseGeocodeCallback extends ReverseGeocodeBean implements
			IGeocodeCallback
	{

		public void characters(char[] ch, int start, int length)
		{
			if ("code".equalsIgnoreCase(tagName))
			{
				code = new String(ch, start, length);
			}
			else if ("distance".equalsIgnoreCase(tagName))
			{
				String distanceString = new String(ch, start, length);
				try
				{
					distance = Float.valueOf(distanceString);
				}
				catch (Exception e)
				{
				}
			}
			else if ("streetNumber".equalsIgnoreCase(tagName))
			{
			    String streetNumberStr = new String(ch, start, length);
			    try
			    {
			        streetNumber = Integer.valueOf(streetNumberStr);
			    }
			    catch (Exception e)
			    {			        
			    }
			}
			else if ("streetName".equalsIgnoreCase(tagName))
			{
			    streetName = new String(ch, start, length);
			}
			else if ("crossStreetName".equalsIgnoreCase(tagName))
			{
			    crossStreetName = new String(ch, start, length);
			}
			else if ("streetAddress".equalsIgnoreCase(tagName))
			{
			    streetAddress = new String(ch, start, length);
			}
			else if ("city".equalsIgnoreCase(tagName))
			{
			    city = new String(ch, start, length);
			}
			else if ("country".equalsIgnoreCase(tagName))
			{
				country = new String(ch, start, length);
			}
			else if ("state".equalsIgnoreCase(tagName))
			{
				state = new String(ch, start, length);
			}
			else if ("postalCode".equalsIgnoreCase(tagName))
			{
				postalCode = new String(ch, start, length);
			}
			
		}

		public void endElement(String uri, String localName, String qName)
		{
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes)
		{
		    if ("distance".equalsIgnoreCase(qName))
            {
                unitString = attributes.getValue("unit");
            }
		}

	}
}
