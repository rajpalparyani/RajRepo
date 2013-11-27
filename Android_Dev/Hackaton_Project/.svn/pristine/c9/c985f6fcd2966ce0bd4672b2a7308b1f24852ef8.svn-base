/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnMviewerLocationProvider.java
 *
 */
package com.telenav.location;

import com.telenav.logger.Logger;

/**
 * This is a common class for Mviewer Location Provider.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public abstract class AbstractTnMviewerLocationProvider extends AbstractTnMockLocationProvider
{
    protected String socketHost;
    protected int socketPort;
    
    /**
     * Construct the MViewer location provider.
     * @param name
     */
    public AbstractTnMviewerLocationProvider(String name)
    {
        super(name);
    }

    /**
     * set mview's socket host.
     * 
     * @param host host name.
     * @param port host port.
     */
    public void setSocketHost(String host, int port)
    {
        this.socketHost = host;
        this.socketPort = port;
    }
    
    /**
     * Get the MViewer data from socket.
     * 
     * @return String
     */
    protected abstract String getMviewerData(int timeout) throws TnLocationException;
    
    /**
     * Get the passive location continuously. The default interval is 1000 ms.
     * 
     * @return {@link TnLocation}
     */
    protected TnLocation getLocationDelegate(int timeout) throws TnLocationException
    {
        TnLocation location = null;

        String s = getMviewerData(timeout);
        
        if (s == null || s.length() == 0)
        {
            throw new TnLocationException("can't retrieve mviewer gps data.");
        }

        location = new TnLocation(this.name);
        
        int indexStr = s.indexOf(",");
        if (indexStr >= 0)
        {
            long time = Long.parseLong(s.substring(0, indexStr)) / 10;
            location.setTime(time);
			
            //set the timestamp of real device or simulator.
            //location.setTime(System.currentTimeMillis()/10);
            
            s = s.substring(indexStr + 1);
        }
        indexStr = s.indexOf(",");
        if (indexStr >= 0)
        {
            int latDM5 = Integer.parseInt(s.substring(0, indexStr));
            location.setLatitude(latDM5);
            
            s = s.substring(indexStr + 1);
        }
        indexStr = s.indexOf(",");
        if (indexStr >= 0)
        {
            int lonDM5 = Integer.parseInt(s.substring(0, indexStr));
            location.setLongitude(lonDM5);
            
            s = s.substring(indexStr + 1);
        }
        indexStr = s.indexOf(",");
        if (indexStr >= 0)
        {
            int speed = Integer.parseInt(s.substring(0, indexStr));
            //should calc speed from DM6 to meter
            location.setSpeed(speed);
            
            s = s.substring(indexStr + 1);
        }
        indexStr = s.indexOf(",");
        if (indexStr >= 0)
        {
            int heading = Integer.parseInt(s.substring(0, indexStr));
            location.setHeading(heading);
            
            s = s.substring(indexStr + 1);
        }
        indexStr = s.indexOf(",");
        if (indexStr >= 0)
        {
            int errSize = Integer.parseInt(s.substring(0, indexStr));
            location.setAccuracy(errSize);
            
            s = s.substring(indexStr + 1);
            int numSats = (byte) Integer.parseInt(s);
            
            location.setSatellites(numSats);
        }
        else
        {
            int errSize = Integer.parseInt(s);
            location.setAccuracy(errSize);
        }

        //always set valid to true for MViewer.
        location.setValid(true);
        
        location.setEncrypt(true);
        
        Logger.log(Logger.INFO, 
            this.getClass().getName(), 
            location.getLocalTimeStamp() + "," + location.getTime()      + "," +
            location.getLatitude()       + "," + location.getLongitude() + "," +
            location.getSpeed()          +  "," + location.getHeading()  + "," +
            location.getAccuracy()       + "," + location.getSatellites(), new Object[]{TnLocation.LOG_GPS_LOCATION, location});
        
        return location;
    }

}
