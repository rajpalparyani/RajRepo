/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractMisLog.java
 *
 */
package com.telenav.log.mis.log;

import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.AbstractMisLogHelper;
import com.telenav.logger.Logger;
import com.telenav.sort.IComparable;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-24
 */
public abstract class AbstractMisLog implements IMisLogConstants, IComparable
{
    private int type;

    protected int priority;

    private String id;

    private long timeStamp = -1;

    private Hashtable attributes;

    protected AbstractMisLogHelper helper;
    
    public AbstractMisLog(String id, int eventType, int priority)
    {
        this.id = id;
        this.type = eventType;
        this.priority = priority;
        attributes = new Hashtable();
    }

    public AbstractMisLog(String id, int eventType, int priority, AbstractMisLogHelper helper)
    {
        this(id, eventType, priority);
        this.helper = helper;
    }
    
    /**
     * Returns log id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set log id.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Returns type of the log. Identifier value corresponds to metric id (log type) of business event. For example: 701
     * - POI View Details
     */
    public int getType()
    {
        return type;
    }

    /**
     * Return priority for an log. Advisory number for stat logger to store ready-to-go events.
     * 
     * @see {@link #P_PRIORITY_1}, {@link #P_PRIORITY_2}, {@link #P_PRIORITY_3}, {@link #P_PRIORITY_4}
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Returns if stat event is can be logged.
     * 
     * @return false if event is part of composite event.
     */
    public boolean isLoggable()
    {
        if(helper != null)
        {
            return helper.isLogEnable(this);
        }
        return true;
    }
    
    /**
     * Sets event time stamp.
     */
    public void setTimestamp(long timestamp)
    {
        timeStamp = timestamp;
    }

    /**
     * Returns event time stamp. For duration events it should return end of the event time stamp
     */
    public long getTimestamp()
    {
        return timeStamp;
    }


    public int compareTo(Object another)
    {
        if (another != null && another instanceof AbstractMisLog)
        {
            long anotherTimeStamp = ((AbstractMisLog) another).getTimestamp();
            if (getTimestamp() > anotherTimeStamp)
            {
                return 1;
            }
            else if (getTimestamp() < anotherTimeStamp)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            throw new ClassCastException("Cannot compare!");
        }
    }
    
    public void setAddressType(byte type)
    {
        this.setAttribute(IMisLogConstants.INNER_ATTR_ADDRESS_TYPE, String.valueOf(type));
    }
    
    public void setAddressSource(byte source)
    {
        this.setAttribute(IMisLogConstants.INNER_ATTR_ADDRESS_SOURCE, String.valueOf(source));
    }
    
    public void setzCounter(int counter)
    {
        this.setAttribute(IMisLogConstants.ATTR_EVENT_COUNTER, String.valueOf(counter));
    }
    
    public void setzDurationLength(long durationLength)
    {
        this.setAttribute(IMisLogConstants.ATTR_EVENT_DURATION, String.valueOf(durationLength));
    }
    
    public void setzLat(long lat)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_LAT, String.valueOf(lat));
    }
    
    public void setzLon(long lon)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_LON, String.valueOf(lon));
    }
    
    public void setzEndLat(long lat)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_END_LAT, String.valueOf(lat));
    }
    
    public void setzEndLon(long lon)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_END_LON, String.valueOf(lon));
    }
    
    public void setzBatteryStart(int level)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_BATTERY_START, String.valueOf(level));
    }
    
    public void setzBatteryEnd(int level)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_BATTERY_END, String.valueOf(level));
    }
    
    public void setzStreet(String street)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_STREET, street);
    }
    
    public void setzCity(String city)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_CITY, city);
    }
    
    public void setzZip(String zip)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_ZIP, zip);
    }
    
    public void setzState(String state)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_STATE, state);
    }
    
    public void setzCountryCode(String code)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_COUNTRY_CODE, code);
    }
    
    public void setzAttrAddresType(String type)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_ADDRESS_TYPE, type);
    }
    
    public void setzAttrAddressSource(String source)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_ADDRESS_SOURCE, source);
    }
    
    public void setzRouteID(int id)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_ROUTE_ID, String.valueOf(id));
    }
    
    public void setzDisplayString(String string)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_DISPLAY_STRING, string);
    }
    
    public void setzSignalLevel(int level)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_SIGNAL_LEVEL, String.valueOf(level));
    }
    
    public void setzNetworkType(String type)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_NETWORK_TYPE, type);
    }
    
    public void setzGpsLossCount(int count)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_GPS_LOSS_COUNT, String.valueOf(count));
    }

    public void setzGpsLossTime(long time)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_GPS_LOSS_TIME, String.valueOf(time));
    }

    public void setzNetworkLossCount(int count)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_NETWORK_LOSS_COUNT, String.valueOf(count));
    }

    public void setzNetworkLossTime(long time)
    {
        this.setAttribute(IMisLogConstants.ATTR_GENERIC_NETWORK_LOSS_TIME, String.valueOf(time));
    }
    
    public void setSessionId(String sessionId)
    {
        this.setAttribute(IMisLogConstants.ATTR_SESSION_ID, sessionId);
    }
    
    /**
     * Sets event attribute.
     * 
     * @param name attribute name
     * @param value attribute value
     */
    
    public void setAttribute(long attributeId, String value)
    {
        if (attributeId == IMisLogConstants.ATTR_EVENT_TIMESTAMP)
        {
            try
            {
                setTimestamp(Long.parseLong(value));
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        else if (value != null && !"".equals(value))
        {
            attributes.put(PrimitiveTypeCache.valueOf(attributeId), value);
        }
    }
    
    /**
     * Returns attribute keys or null if no attributes.
     */
    public Enumeration getAttributeKeys()
    {
        if (attributes == null)
            return null;
        return attributes.keys();
    }
    
    

    /**
     * Returns event attribute value
     * 
     * @param name attribute name
     */
    public String getAttribute(long attributeId)
    {
        if (attributes == null)
            return null;
        return (String) attributes.get(PrimitiveTypeCache.valueOf(attributeId));
    }
    
    /**
     * Returns event attribute int value, if it does not exit then return default value
     * 
     * @param name attribute name
     */
    public int optAttributeInt(long attributeId, int defaultValue)
    {
        String value = getAttribute(attributeId);
        if (value != null && value.length() > 0)
        {
            try
            {
                return Integer.parseInt(value);
            }
            catch (Exception ex)
            {
                Logger.log(this.getClass().getName(), ex);
            }
        }
        return defaultValue;
    }

    /**
     * Returns event attribute long value, if it does not exit then return default value
     * 
     * @param name attribute name
     */
    public long optAttributeLong(long attributeId, long defaultValue)
    {
        String value = getAttribute(attributeId);
        if (value != null && value.length() > 0)
        {
            try
            {
                return Long.parseLong(value);
            }
            catch (Exception ex)
            {
                Logger.log(this.getClass().getName(), ex);
            }
        }
        return defaultValue;
    }
    
    /**
     * Returns event attribute byte value, if it does not exit then return default value
     * 
     * @param name attribute name
     */
    public byte optAttributeByte(long attributeId, byte defaultValue)
    {
        String value = getAttribute(attributeId);
        if (value != null && value.length() > 0)
        {
            try
            {
                return Byte.parseByte(value);
            }
            catch (Exception ex)
            {
                Logger.log(this.getClass().getName(), ex);
            }
        }
        return defaultValue;
    }
    
    public void processLog()
    {
        if (helper != null)
        {
            helper.fillAttrbute(this);
        }
    }

    protected void processGenericAttr()
    {
        if (optAttributeByte(INNER_ATTR_ADDRESS_TYPE, (byte) -1) != -1)
        {
            byte type = optAttributeByte(INNER_ATTR_ADDRESS_TYPE, (byte) -1);

            switch (type)
            {
                case Stop.STOP_CURRENT_LOCATION:
                {
                    this.setzAttrAddresType(IMisLogConstants.VALUE_AT_CURRENT_LOCATION);
                    break;
                }
                case Stop.STOP_CITY:
                {
                    this.setzAttrAddresType(IMisLogConstants.VALUE_AT_CITY);
                    break;
                }
                case Stop.STOP_POI:
                {
                    this.setzAttrAddresType(IMisLogConstants.VALUE_AT_POI);
                    break;
                }
                case Stop.STOP_AIRPORT:
                {
                    this.setzAttrAddresType(IMisLogConstants.VALUE_AT_AIRPORT);
                    break;
                }
                default:
                {
                    this.setzAttrAddresType(IMisLogConstants.VALUE_AT_ADDRESS);
                    break;
                }
            }
        }

        if (optAttributeByte(INNER_ATTR_ADDRESS_SOURCE, (byte) -1) != -1)
        {
            byte source = optAttributeByte(INNER_ATTR_ADDRESS_SOURCE, (byte) -1);
            switch (source)
            {
                case Address.SOURCE_RECENT_PLACES:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_RECENT_PLACES);
                    break;
                }
                case Address.SOURCE_RESUME_TRIP:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_RESUME_TRIP);
                    break;
                }
                case Address.SOURCE_FAVORITES:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_FAVORITES);
                    break;
                }
                case Address.SOURCE_RECEIVED_ADDRESS:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_RECEIVED_ADDRESS);
                    break;
                }
                case Address.SOURCE_SEARCH:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_SEARCH_POI);
                    break;
                }
                case Address.SOURCE_MAP:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_MAP);
                    break;
                }
                case Address.SOURCE_EMAIL:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_EMAIL);
                    break;
                }
                case Address.SOURCE_CALENDAR:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_CALENDAR);
                    break;
                }
                case Address.SOURCE_API:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_API);
                    break;
                }
                case Address.SOURCE_UNKOWN:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_OTHER);
                    break;
                }
                default:
                {
                    this.setzAttrAddressSource(IMisLogConstants.VALUE_AS_OTHER);
                    break;
                }
            }
        }
    }
}
