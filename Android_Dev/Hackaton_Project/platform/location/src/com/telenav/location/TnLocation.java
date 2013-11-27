/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnLocation.java
 *
 */
package com.telenav.location;

/**
 * A class representing a geographic location sensed at a particular time (a "fix"). A location consists of a latitude
 * and longitude, a UTC timestamp. and optionally information on altitude, speed, and bearing.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public class TnLocation
{
    /**
     *  used to indicate the log is recorded for debug and regression purpose
     */
    public static final String LOG_GPS_LOCATION         =  "marts_gps";

    protected int latitude;

    protected int longitude;

    protected long time;
    
    /** local time (for the device) */
    protected long localTime;

    protected int speed;

    protected float altitude;

    protected int satellites;

    protected boolean isValid;

    protected int heading;

    protected int accuracy;
    
    protected String provider;
    
    // Guoyuan: whether this gps fix has been encrypted?
    protected boolean hasEnc = false ;
    
    public TnLocation(String provider)
    {
        this.provider = provider;
        this.localTime = System.currentTimeMillis();
    }
    
    /**
     * The name of location provider.
     * 
     * @return String
     */
    public String getProvider()
    {
        return this.provider;
    }
    
    /**
     * Returns the UTC time of this fix, in milliseconds since January 1, 1970.
     * 
     * @return long
     */
    public long getTime()
    {
        return time;
    }

    /**
     * Sets the UTC time of this fix, in milliseconds since January 1, 1970. 
     * 
     * @param time milliseconds.
     */
    public void setTime(long time)
    {
        this.time = time;
    }

    /**
     * Returns the speed of the device over ground in DM6/second.
     * <br />
     * convert speed from meters/second to DM6/second: DM6 = meters/second * 6.
     * 
     * @return int
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * Sets the speed of this fix, in DM6/second. 
     * 
     * @param speed DM6 = meters/second * 6.
     */
    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    /**
     * Returns the altitude component of this coordinate. Altitude is defined to mean height above the 
     * WGS84 reference ellipsoid. 0.0 means a location at the ellipsoid surface, negative values mean 
     * the location is below the ellipsoid surface, Float.NaN that no altitude is available.
     * 
     * @return int
     */
    public float getAltitude()
    {
        return altitude;
    }

    /**
     * Sets the altitude of this fix.
     * 
     * @param altitude the geodetic altitude for this point.
     */
    public void setAltitude(float altitude)
    {
        this.altitude = altitude;
    }

    /**
     * Retrieve the numbers of satellites.
     * 
     * @return int
     */
    public int getSatellites()
    {
        return satellites;
    }

    /**
     * Set the numbers of satellites.
     * 
     * @param satellites satellites.
     */
    public void setSatellites(int satellites)
    {
        this.satellites = satellites;
    }

    /**
     * Returns whether this Location instance represents a valid location with coordinates or an 
     * invalid one where all the data, especially the latitude and longitude coordinates, may not be present. 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        return isValid;
    }

    /**
     * set whether this Location instance represents a valid location.
     * 
     * @param isValid a boolean value with true indicating that this Location instance is valid and false indicating an invalid Location instance.
     */
    public void setValid(boolean isValid)
    {
        this.isValid = isValid;
    }

    /**
     * Returns the bearing in degrees relative to true north.
     * 
     * @return int
     */
    public int getHeading()
    {
        return heading;
    }

    /**
     * Sets the bearing of this fix.
     * 
     * @param heading degrees relative to true north.
     */
    public void setHeading(int heading)
    {
        this.heading = heading;
    }

    /**
     * Sets the geodetic latitude for this point. Latitude is given as a double expressing the latitude in degrees in the WGS84 datum.
     * 
     * @param longitude the latitude component of this location in degrees. [DM5 format, DM5 = lat * 100000]
     */
    public void setLatitude(int latitude)
    {
        this.latitude = latitude;
    }
    
    
    /**
     * Sets the geodetic longitude for this point. Longitude is given as a double expressing the longitude in degrees in the WGS84 datum.
     * 
     * @param longitude the longitude of the location in degrees. [DM5 format, DM5 = lat * 100000]
     */
    public void setLongitude(int longitude)
    {
        this.longitude = longitude;
    }

    /**
     * Returns the latitude component of this coordinate. [DM5 format, DM5 = lat * 100000]
     * 
     * @return int
     */
    public int getLatitude()
    {
        return this.latitude;
    }

    /**
     * Returns the longitude component of this coordinate. [DM5 format, DM5 = lat * 100000]
     * 
     * @return int
     */
    public int getLongitude()
    {
        return this.longitude;
    }
    
    /**
     * Returns the accuracy of the fix with DM5. [ DM5 = miles * 7358 >> 13]
     * 
     * @return
     */
    public int getAccuracy()
    {
        return accuracy;
    }

    /**
     * Sets the accuracy of this fix with DM5. [ DM5 = miles * 7358 >> 13]
     * 
     * @param accuracy DM5 format
     */
    public void setAccuracy(int accuracy)
    {
        this.accuracy = accuracy;
    }
    
    /**
     * get if has encrypt.
     * 
     * @return
     */
    public boolean isEncrypt()
    {
        return this.hasEnc;
    }
    
    /**
     * set if has encrypt.
     * 
     * @param hasEncrypt
     */
    public void setEncrypt(boolean hasEncrypt)
    {
        this.hasEnc = hasEncrypt;
    }
    
    /**
     * get local time stamp.
     * 
     * @return
     */
    public long getLocalTimeStamp()
    {
        return this.localTime;
    }
    
    /**
     * set local time stamp
     * 
     * @param time
     */
    public void setLocalTimeStamp(long time)
    {
    	this.localTime = time;
    }
    
    /**
     * copy location data.
     * 
     * @param location
     */
    public void set(TnLocation location)
    {
        if(location == null)
            return;
        
        this.provider = location.provider;
        this.setAccuracy(location.accuracy);
        this.setAltitude(location.altitude);
        this.setHeading(location.heading);
        this.setLatitude(location.latitude);
        this.setLongitude(location.longitude);
        this.setSatellites(location.satellites);
        this.setSpeed(location.speed);
        this.setTime(location.time);
        this.setValid(location.isValid);
        this.setLocalTimeStamp(location.localTime);
        this.setEncrypt(location.hasEnc);
    }
    
    public String toString()
    {
        return provider + "," + time + "," + localTime + "," + latitude + "," + longitude + "," + speed + "," + heading + "," + accuracy + "," + isValid + "," + satellites;
    }
}
