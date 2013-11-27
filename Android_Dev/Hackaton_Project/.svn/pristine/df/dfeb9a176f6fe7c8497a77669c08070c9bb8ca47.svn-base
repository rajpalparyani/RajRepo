package com.telenav.module.location;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.Friend;
import com.telenav.gps.IGpsListener;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.dwf.DwfUtil;
import com.telenav.sdk.maitai.IMaiTai;

/**
 *@author bduan
 *@date Nov 23, 2010
 */
public class LocationProvider implements IGpsListener
{
    private static final String LAT_LON_SEPERATOR = ":";

    public static final int TYPE_GPS = 1;
    
    public static final int TYPE_NETWORK = 2;

    public static int STATUS_SUCCESS = 0;

    public static int STATUS_TIMEOUT = 1;

    public static int NETWORK_LOCATION_VALID_TIME = 20 * 60 * 1000;
    
    public static int DAY_MODE_CHECK_INTERVAL = 5 * 60 * 1000;

    public static int GPS_VALID_TIME = 10 * 1000;

    protected static int DEFAULT_LAT_US = 3709021;
    protected static int DEFAULT_LON_US = -9571289;
    
    protected static int DEFAULT_LAT_UK = 5150834;//FIXME, for Telematics demo
    protected static int DEFAULT_LON_UK = -12816;//FIXME, for Telematics demo
    
    protected static int DEFAULT_LAT_BR = -1574996;
    protected static int DEFAULT_LON_BR = -4792374;

    protected static int DEFAULT_LAT_CN = 3120605;
    protected static int DEFAULT_LON_CN = 12139875;
    
    protected static int DEFAULT_LAT_MX = 1943281;
    protected static int DEFAULT_LON_MX = -9913316;
    
    
    protected static int DEFAULT_LAT_CA = 4542992;
    protected static int DEFAULT_LON_CA = -7569531;
    
    protected static int DEFAULT_LAT_IN = 2861855;
    protected static int DEFAULT_LON_IN = 7724195;
    
    protected static int INIT_LAT_US = 3737890;
    protected static int INIT_LON_US = -12201074;

    protected static int INIT_LAT_UK = DEFAULT_LAT_UK;
    protected static int INIT_LON_UK = DEFAULT_LON_UK;
    
    protected static int INIT_LAT_BR = DEFAULT_LAT_BR;
    protected static int INIT_LON_BR = DEFAULT_LON_BR;
      
    protected static int INIT_LAT_MX = DEFAULT_LAT_MX;
    protected static int INIT_LON_MX = DEFAULT_LON_MX;
    
    protected static int INIT_LAT_CA = DEFAULT_LAT_CA;
    protected static int INIT_LON_CA = DEFAULT_LON_CA;

    protected static int INIT_LAT_CN = DEFAULT_LAT_CN;
    protected static int INIT_LON_CN = DEFAULT_LON_CN;
    
    protected final static long MIN_NOTIFY_INTERVAL = 1000;
    
    protected ITnLocationProvider tnLocationProvider;
        

    protected Vector listeners = new Vector();

    protected Object loadMutex = new Object();
    
    protected Object callBackMutex = new Object();
    
    protected long lastDayModeCheckTime = -1L;
        
    volatile boolean callBackCanceled = false;

    private static class InnerLocationProvider
    {
        private static LocationProvider instance = new LocationProvider();
    }
    
    private LocationProvider()
    {
    }
    
    public static LocationProvider getInstance()
    {
        return InnerLocationProvider.instance;
    }
    
    public void init(ITnLocationProvider tnLocationProvider)
    {  
        this.tnLocationProvider = tnLocationProvider;
        this.tnLocationProvider.setListener(this);
        this.tnLocationProvider.start();        
    }
    
    /**
     * Get gpsType, it could be one of <br>
     * 
     * TnLocationManager.ALONGROUTE_PROVIDER<br>
     * 
     * TnLocationManager.GPS_179_PROVIDER<br>
     * 
     * etc... <br>
     * 
     * 
     * @return type
     */
    public String getGpsServiceType()
    {
        return tnLocationProvider.getGpsServiceType();
    }
    
    public int getGpsServiceStatus()
    {
        return tnLocationProvider.getGpsServiceStatus();
    }
    
    /**
     * get last known location base on the type
     * you specific. could be TYPE_GPS | TYPE_NETWORK
     * 
     * @param type
     * @return lastknownlocation.
     */
    public TnLocation getLastKnownLocation(int type)
    {
        return getLastKnownLocation(type, true);
    }
    
    /**
     * if (isSearchCurrent == true) max valid time for local GPS / local network
     * else device LK GPS --> local cached LK GPS 
     *      device LK network --> local cached LK network
     * @param type
     * @param isSearchCurrent
     * @return
     */
    protected TnLocation getLastKnownLocation(int type, boolean isSearchCurrent)
    {
        if(this.tnLocationProvider == null)
        {
            return null;
        }
        
        if(isSearchCurrent)
        {
            TnLocation currentLocation = getCurrentLocation(Integer.MAX_VALUE, Integer.MAX_VALUE, type);
            if (currentLocation != null)
            {
                return currentLocation;
            }
        }
        
        TnLocation lastGpsLocation = null;
        TnLocation lastNetworkLocation = null;

        if ((type & TYPE_GPS) != 0)
        {
            // for most device, we should be able to get it per this native api.
            lastGpsLocation = tnLocationProvider
                    .getLastKnownLocation(tnLocationProvider.convertToNativeProvider(TYPE_GPS));

            if (lastGpsLocation == null)
            {
                lastGpsLocation = LocationCacheManager.getInstance().getLastGpsLocation();
            }
        }

        if ((type & TYPE_NETWORK) != 0)
        {
            // for most device, we should be able to get it per this native api.
            lastNetworkLocation = tnLocationProvider
                    .getLastKnownLocation(tnLocationProvider.convertToNativeProvider(TYPE_NETWORK));

            if (lastNetworkLocation == null)
            {
                lastNetworkLocation = LocationCacheManager.getInstance()
                        .getLastCellLocation();
            }
        }

        TnLocation lastLoc = getLatestLocation(lastGpsLocation, lastNetworkLocation);

        return lastLoc;
    }

	protected TnLocation getLatestLocation(TnLocation LocationA,
            TnLocation locationB)
    {
        if(LocationA == null && locationB == null)
        {
            return null;
        }
        else if(LocationA != null && locationB != null)
        {
            if(LocationA.getLocalTimeStamp() >= locationB.getLocalTimeStamp())
            {
                return LocationA;
            }
            else
            {
                return locationB;
            }
        }
        else
        {
            return LocationA == null ? locationB : LocationA;
        }
    }

    public TnLocation getInitialLocation()
    {
        long time = System.currentTimeMillis();
        
        int lat = 0;
        int lon = 0;
        String settedPosition = getSettedLocation();
        if (!"".equalsIgnoreCase(settedPosition))
        {
            int index = settedPosition.indexOf(LAT_LON_SEPERATOR);
            lat = Integer.parseInt(settedPosition.substring(0, index));
            lon = Integer.parseInt(settedPosition.substring(index + 1));
        }
        else
        {
            if (AppConfigHelper.BRAND_TMOBILEUK.equals(AppConfigHelper.brandName))
            {
                lat = INIT_LAT_UK;
                lon = INIT_LON_UK;
            }
            else if (AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
            {
                lat = INIT_LAT_UK;
                lon = INIT_LON_UK;
            }
            else if (AppConfigHelper.BRAND_TELCEL.equals(AppConfigHelper.brandName))
            {
                lat = INIT_LAT_MX;
                lon = INIT_LON_MX;
            }
            else if (AppConfigHelper.BRAND_BELL.equals(AppConfigHelper.brandName)
                    || AppConfigHelper.BRAND_ROGERS.equals(AppConfigHelper.brandName))
            {
                lat = INIT_LAT_CA;
                lon = INIT_LON_CA;
            }
            else if (AppConfigHelper.BRAND_VIVO.equals(AppConfigHelper.brandName))
            {
                lat = INIT_LAT_BR;
                lon = INIT_LON_BR;
            }
            else
            {
                lat = INIT_LAT_US;
                lon = INIT_LON_US;
            }
        }
        int speed = 0;
        int heading = 0;
        int altitude = 0;
        int err = 0;

        TnLocation initLocation = new TnLocation("");
        initLocation.setLatitude(lat);
        initLocation.setLongitude(lon);
        initLocation.setSpeed(speed);
        initLocation.setAltitude(altitude);
        initLocation.setHeading(heading);
        initLocation.setAccuracy(err);
        initLocation.setTime(time);

        return initLocation;
    }
    
    private String getSettedLocation()
    {
        String settedPosition = DaoManager.getInstance().getSimpleConfigDao()
                .getString(SimpleConfigDao.KEY_REGION_SETTING);
        boolean isLegalRegionSetting = settedPosition != null
                && !"".equalsIgnoreCase(settedPosition)
                && settedPosition.indexOf(LAT_LON_SEPERATOR) > 0 ? true : false;
        if (!isLegalRegionSetting)
        {
            settedPosition = "";
        }
        return settedPosition;
    }
    /**
     * get Default location.
     * 
     * @return
     */
    public TnLocation getDefaultLocation()
    {
        long time = System.currentTimeMillis();
        
        int lat = 0;
        int lon = 0;
        String settedLocation = getSettedLocation();
        if (!"".equalsIgnoreCase(settedLocation))
        {
            int index = settedLocation.indexOf(LAT_LON_SEPERATOR);
            lat = Integer.parseInt(settedLocation.substring(0, index));
            lon = Integer.parseInt(settedLocation.substring(index + 1));
        }
        else
        {
            if (AppConfigHelper.BRAND_TMOBILEUK.equals(AppConfigHelper.brandName))
            {
                lat = DEFAULT_LAT_UK;
                lon = DEFAULT_LON_UK;
            }
            else if (AppConfigHelper.BRAND_VIVO.equals(AppConfigHelper.brandName))
            {
                lat = DEFAULT_LAT_BR;
                lon = DEFAULT_LON_BR;
            }

            else if (AppConfigHelper.BRAND_MMI.equals(AppConfigHelper.brandName))
            {
                lat = DEFAULT_LAT_IN;
                lon = DEFAULT_LON_IN;
            }
            else if (AppConfigHelper.BRAND_SCOUT_EU.equals(AppConfigHelper.brandName))
            {
                lat = DEFAULT_LAT_UK;
                lon = DEFAULT_LON_UK;
            } 
            else if (AppConfigHelper.BRAND_TELCEL.equals(AppConfigHelper.brandName))
            {
                lat = DEFAULT_LAT_MX;
                lon = DEFAULT_LON_MX;
            }
            else if (AppConfigHelper.BRAND_BELL.equals(AppConfigHelper.brandName)
                    || AppConfigHelper.BRAND_ROGERS.equals(AppConfigHelper.brandName))
            {
                lat = DEFAULT_LAT_CA;
                lon = DEFAULT_LON_CA;
            }
            else
            {
                lat = DEFAULT_LAT_US;
                lon = DEFAULT_LON_US;
            }
        }
        int speed = 0;
        int heading = 0;
        int altitude = 0;
        int err = 0;

        TnLocation defaultLocation = new TnLocation("");
        defaultLocation.setLatitude(lat);
        defaultLocation.setLongitude(lon);
        defaultLocation.setSpeed(speed);
        defaultLocation.setAltitude(altitude);
        defaultLocation.setHeading(heading);
        defaultLocation.setAccuracy(err);
        defaultLocation.setTime(time);
        defaultLocation.setValid(true);

        return defaultLocation;
    }

    /**
     * Use default valid time
     * allow last known locations
     * 
     * get current location base on the type
     * you specific. could be TYPE_GPS | TYPE_NETWORK
     * 
     * 
     * @param type
     * @return
     */
    public TnLocation getCurrentLocation(int type)
    {
        return getCurrentLocation(GPS_VALID_TIME, NETWORK_LOCATION_VALID_TIME, type);
    }
    
    public TnLocation getCurrentLocation(int type, boolean isStrictValidTime)
    {
        return getCurrentLocation(GPS_VALID_TIME, NETWORK_LOCATION_VALID_TIME, type, isStrictValidTime);
    }
    
    /**
     * get current location base on the type and valid time
     * you specific. could be TYPE_GPS | TYPE_NETWORK
     * 
     * @param type
     * @return
     */
    protected TnLocation getCurrentLocation(int gpsLocationValidTime, int cellLocationValidTime, int type)
    {
        return getCurrentLocation(gpsLocationValidTime, cellLocationValidTime, type, false);
    }
    
    public TnLocation getCurrentLocation(int gpsLocationValidTime, int cellLocationValidTime, int type,
            boolean isStrictValidTime)
    {
        if(gpsLocationValidTime <= 0)
        {
            gpsLocationValidTime  = GPS_VALID_TIME;
        }
        
        if(cellLocationValidTime <= 0)
        {
            cellLocationValidTime = NETWORK_LOCATION_VALID_TIME;
        }
        
        TnLocation[] locations = getLocationArray(1);
        
        int realCount = getAvailableLocation(gpsLocationValidTime, cellLocationValidTime, type, 1, locations, isStrictValidTime);
        return realCount > 0 ? locations[0] : null; 
    }
    
    /**
     * get current locations base on the type, count and valid time
     * you specific. could be TYPE_GPS | TYPE_NETWORK
     * 
     * When the gps with the count you required it ready,
     * it will callback to the location listner.
     * 
     * @param type
     * @return
     */
    public void getCurrentLocation(int gpsLocationValidTime, int cellLocationValidTime, 
            long timeout, LocationListener listener, 
            int type, int count)
    {
        getCurrentLocation(gpsLocationValidTime, cellLocationValidTime, timeout, listener, type, count, false);
    }
    
    //only support 1 fix. And there's no thread cancel this request and start another request right now.
    public void getCurrentLocation(final int gpsLocationValidTime, final int cellLocationValidTime, final long timeout,
            final LocationListener listener, final int type, final int count, final boolean isStrictValidTime)
    {
        if(this.tnLocationProvider == null)
        {
            listener.requestCompleted(null, STATUS_TIMEOUT);
            return;
        }
        
        callBackCanceled = false;
        final TnLocation[] locations = new TnLocation[count];
        for (int i = 0; i < count; i++)
        {
            locations[i] = new TnLocation("");
        }

        int realCount = getAvailableLocation(gpsLocationValidTime, cellLocationValidTime, type, count, locations, isStrictValidTime);

        if (realCount >= count)
        {
            listener.requestCompleted(locations, STATUS_SUCCESS);
            return;
        }
        else
        {
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    // TODO HANDLE THE CALL BACK!!!
                    synchronized (callBackMutex)
                    {
                        try
                        {
                            callBackMutex.wait(timeout);
                        }
                        catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (callBackCanceled)
                        {
                            return;
                        }

                        int realCount = getAvailableLocation(gpsLocationValidTime, cellLocationValidTime, type, count, locations,
                            isStrictValidTime);

                        if (realCount >= count)
                        {
                            listener.requestCompleted(locations, STATUS_SUCCESS);
                            return;
                        }
                        else
                        {
                            listener.requestCompleted(null, STATUS_TIMEOUT);
                        }
                    }
                }
            });
            t.start();
        }
    }
    
    /**
     * Get the specific number TnLocation data from cache.
     * 
     * @param locations The TnLocation array 
     * @param count the request count
     * @param gpsLocationValidTime
     * @param cellLocationValidTime
     * @param type
     * @return the number that got from cache.
     */
    public int getCurrentLocation(TnLocation[] locations, int count, int type)
    {
        if(locations == null || count > locations.length)
        {
            throw new IllegalArgumentException("The locaionts array is less the the count you request... Please fix.");
        }
        
        return getAvailableLocation(GPS_VALID_TIME, NETWORK_LOCATION_VALID_TIME, type, count, locations);
    }
    
    /**
     *  return latest GPS fixes in GPS fixes buffer
     * 
     * @param locations
     * @param count
     * @return  real count of GPS location returned
     */
    public int getGpsLocations(TnLocation[] locations, int count)
    {
        return getGpsLocations(locations, count, false);
    }
    
    /**
     * return latest GPS fixes in GPS fixes buffer
     * 
     * @param locations
     * @param count
     * @param isStrictValidTime
     * @return
     */
    public int getGpsLocations(TnLocation[] locations, int count, boolean isStrictValidTime)
    {
        if(locations == null || count > locations.length)
        {
            throw new IllegalArgumentException("The locaionts array is less the the count you request... Please fix.");
        }
        
        return getAvailableLocation(Integer.MAX_VALUE, 0, TYPE_GPS, count, locations, isStrictValidTime);
    }
        
    public void cancelAllRequests()
    {
        this.callBackCanceled = true;
        synchronized (callBackMutex)
        {
            callBackMutex.notifyAll();
        }
    }
    

    protected int getAvailableLocation(int gpsLocationValidTime, 
            int networkLocationValidTime, int type, 
            int count, TnLocation[] locations)
    {
        return getAvailableLocation(gpsLocationValidTime, networkLocationValidTime, type, count, locations, false);
    }
    
    protected boolean isLocationTimeValid(long currentTime, long gpsLocationValidTime, TnLocation location)
    {
    	if(location == null)
    		return false;
    	
    	long gpsTime = location.getLocalTimeStamp();
        long latestAcceptableTime = currentTime - gpsLocationValidTime;
        if (gpsTime <= latestAcceptableTime)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "location time not valid, currentTime = " + currentTime
                    + ", gpsLocationValidTime = " + gpsLocationValidTime + ", gps local time = " + gpsTime + " , lat : "
                    + location.getLatitude() + " , lon : " + location.getLongitude());
        	return false;
        }
        
        return true;
    }
    
    protected TnLocation[] getLocationArray(int length)
    {
    	if(length <= 0)
    	{
    		return null;
    	}
    	
    	TnLocation[] locations = new TnLocation[length];
    	for(int i = 0 ; i < length ; i ++)
    	{
    		locations[i] = new TnLocation("");
    	}
    	
    	return locations;
    }
    
    /**
     * GPS --> network --> last known
     * 
     * @param gpsLocationValidTime
     * @param networkLocationValidTime
     * @param type
     * @param count
     * @param locations
     * @param isStrictValidTime
     * @return
     */
    protected int getAvailableLocation(int gpsLocationValidTime,
            int networkLocationValidTime, int type, int count, TnLocation[] locations,
            boolean isStrictValidTime)
    {
        if (locations == null || locations.length != count || count <= 0
                || tnLocationProvider == null)
            return 0;

        long currentTime = System.currentTimeMillis();

        Vector tempVec = new Vector();
        if ((type & TYPE_GPS) != 0)
        {
            TnLocation[] gpsLocations = getLocationArray(count);
            int gpsFixNum = tnLocationProvider.getGpsFixes(count, gpsLocations);
            if (gpsFixNum > 0)
            {                
                for (int i = 0; i < gpsFixNum; i++)
                {
                    if (isLocationTimeValid(currentTime, gpsLocationValidTime,
                        gpsLocations[i]))
                    {
                        tempVec.add(gpsLocations[i]);
                    }
                    else
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(),
                            "gps location time not valid");
                    }
                }
                LocationDayModeChecker.checkDayMode(gpsLocations[0]);
            }
        }

        if (tempVec.size() < count && (type & TYPE_NETWORK) != 0)
        {
            TnLocation[] networkLocations = getLocationArray(count);
            int networkFixNum = tnLocationProvider.getNetworkFixes(count,
                networkLocations);
            if (networkFixNum > 0)
            {
                for (int i = 0; i < networkFixNum; i++)
                {
                    if (isLocationTimeValid(currentTime, networkLocationValidTime,
                        networkLocations[i]))
                    {
                        tempVec.add(networkLocations[i]);
                    }
                }
                LocationDayModeChecker.checkDayMode(networkLocations[0]);
            }
        }

        if (tempVec.size() < count && !isStrictValidTime)
        {
            TnLocation lastKnownLocation = this.getLastKnownLocation(type, false);
            if (lastKnownLocation != null)
            {
                tempVec.add(lastKnownLocation);
                LocationDayModeChecker.checkDayMode(lastKnownLocation);
            }
        }

        int fixNum = tempVec.size();
        if (fixNum > count)
        {
            fixNum = count;
        }

        for (int i = 0; i < fixNum; i++)
        {
            TnLocation tempLoc = (TnLocation) tempVec.elementAt(i);
            locations[i].set(tempLoc);
        }

        tempVec.removeAllElements();

        return fixNum;
    }

    public void addListener(IGpsListener gpsListener)
    {
        if(!listeners.contains(gpsListener))
        {
            listeners.add(gpsListener);
        }
    }
    
    public void removeListener(IGpsListener gpsListener)
    {
        if(listeners.contains(gpsListener))
        {
            listeners.remove(gpsListener);
        }
    }      
    
    public void start()
    {
        if (tnLocationProvider != null)
        {
            tnLocationProvider.start();
        }
    }
    
    public void stop()
    {
        if (tnLocationProvider != null)
        {
            tnLocationProvider.stop();
        }
    }

    public void gpsDataArrived(TnLocation data)
    {
        if (data != null)
        {
            synchronized (callBackMutex)
            {
                callBackMutex.notifyAll();
            }
            
            checkDayMode(data);
            
            notifyListeners(data);
            
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
            if (dwfAidl != null)
            {
                try
                {
                    if (dwfAidl.getSharingIntent() != null)
                    {
//                        dwfAidl.updateStatus(Integer.MIN_VALUE, "", (double)data.getLatitude() / 100000d, (double)data.getLongitude() / 100000d);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void checkDayMode(TnLocation data)
    {
        if(data != null && data.getLocalTimeStamp() - lastDayModeCheckTime > DAY_MODE_CHECK_INTERVAL)
        {
            LocationDayModeChecker.checkDayMode(data);
            lastDayModeCheckTime = data.getLocalTimeStamp();
        }
    }
    
    protected void notifyListeners(TnLocation tnLocation)
    {
        if(listeners != null)
        {
            int size = listeners.size();
            for (int i = 0; i < size; i++)
            {
                try
                {
                    IGpsListener gpsListener = (IGpsListener) listeners.elementAt(i);
                    gpsListener.gpsDataArrived(tnLocation);
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }        
        }
    }
    
    /**
     * Get several locations from the location buffer sustained by our native location provider.
     * The count of the locations is specified by the count parameter passed into the method.
     * 
     * @param gpsLocationValidTime    The valid time of GPS fix.
     * @param networkLocationValidTime The valid time of network location.
     * @param type The location type we expected to return. Can be TYPE_GPS or TYPE_NETWORK
     * @param count The location count we expected to return.
     * @param locations The array that will contain the result.
     * @return The real count of the returned locations.
     */
    protected int getLocationFromLocationBuffer(int gpsLocationValidTime,
            int networkLocationValidTime, int type, int count, TnLocation[] locations)
    {
        if (locations == null || locations.length != count || count <= 0
                || tnLocationProvider == null)
            return 0;

        long currentTime = System.currentTimeMillis();

        Vector tempVec = new Vector();
        if ((type & TYPE_GPS) != 0)
        {
            TnLocation[] gpsLocations = getLocationArray(count);
            int gpsFixNum = tnLocationProvider.getGpsFixes(count, gpsLocations);
            if (gpsFixNum > 0)
            {                
                for (int i = 0; i < gpsFixNum; i++)
                {
                    if (isLocationTimeValid(currentTime, gpsLocationValidTime,
                        gpsLocations[i]))
                    {
                        tempVec.add(gpsLocations[i]);
                    }
                    else
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(),
                            "gps location time not valid");
                    }
                }
                LocationDayModeChecker.checkDayMode(gpsLocations[0]);
            }
        }

        if (tempVec.size() < count && (type & TYPE_NETWORK) != 0)
        {
            TnLocation[] networkLocations = getLocationArray(count);
            int networkFixNum = tnLocationProvider.getNetworkFixes(count,
                networkLocations);
            if (networkFixNum > 0)
            {
                for (int i = 0; i < networkFixNum; i++)
                {
                    if (isLocationTimeValid(currentTime, networkLocationValidTime,
                        networkLocations[i]))
                    {
                        tempVec.add(networkLocations[i]);
                    }
                }
                LocationDayModeChecker.checkDayMode(networkLocations[0]);
            }
        }

        int fixNum = tempVec.size();
        if (fixNum > count)
        {
            fixNum = count;
        }

        for (int i = 0; i < fixNum; i++)
        {
            TnLocation tempLoc = (TnLocation) tempVec.elementAt(i);
            locations[i].set(tempLoc);
        }

        tempVec.removeAllElements();

        return fixNum;
    }
    
    /**
     * Get the last known location from the device chipset.
     * 
     * @param type Can be TYPE_GPS or TYPE_NETWORK
     * @return the last known location if exists; else return null
     */
    protected TnLocation getLastKnownLocationFromDevice(int type)
    {
        if(this.tnLocationProvider == null)
        {
            return null;
        }
        
        TnLocation lastGpsLocation = null;
        TnLocation lastNetworkLocation = null;

        if ((type & TYPE_GPS) != 0)
        {
            // for most device, we should be able to get it per this native api.
            lastGpsLocation = tnLocationProvider
                    .getLastKnownLocation(tnLocationProvider.convertToNativeProvider(TYPE_GPS));
        }

        if ((type & TYPE_NETWORK) != 0)
        {
            // for most device, we should be able to get it per this native api.
            lastNetworkLocation = tnLocationProvider
                    .getLastKnownLocation(tnLocationProvider.convertToNativeProvider(TYPE_NETWORK));
        }

        TnLocation lastLoc = getLatestLocation(lastGpsLocation, lastNetworkLocation);

        return lastLoc;
    }
    
    /**
     * Get last known location from the location buffer sustained by our native location provider.
     * The valid time for both GPS and Network location is the max value.
     * 
     * @param type Can be TYPE_GPS or TYPE_NETWORK
     * @return The last known location from buffer. May be null.
     */
    protected TnLocation getLastKnownLocationFromBuffer(int type)
    {
        TnLocation[] locations = getLocationArray(1);
        int length = getLocationFromLocationBuffer(Integer.MAX_VALUE, Integer.MAX_VALUE, type, 1, locations);
        
        return length > 0 ? locations[0] : null;
    }
    
    /**
     * Get the last known location from our LocationCacheManager.
     * This location is updated each time the application deactivates in the location manager.
     * 
     * @param type Can be TYPE_GPS or TYPE_NETWORK
     * @return The location from the LocationCacheManager. May be null.
     */
    protected TnLocation getLastKnownLocationFromCache(int type)
    {
        if (this.tnLocationProvider == null)
        {
            return null;
        }

        TnLocation lastGpsLocation = null;
        TnLocation lastNetworkLocation = null;

        if ((type & TYPE_GPS) != 0)
        {
            lastGpsLocation = LocationCacheManager.getInstance().getLastGpsLocation();
        }

        if ((type & TYPE_NETWORK) != 0)
        {

            lastNetworkLocation = LocationCacheManager.getInstance()
                    .getLastCellLocation();
        }

        TnLocation lastLoc = getLatestLocation(lastGpsLocation, lastNetworkLocation);

        return lastLoc;
    }
    
    /**
     * Get the latest location among the location array.
     * 
     * @param locations
     * @return null if the location array is empty.
     *         else, returns The latest location 
     */
    protected TnLocation getLatestLocation(TnLocation[] locations)
    {
        TnLocation loc = null;
        if(locations != null)
        {
            for(int i = 0; i < locations.length; i++)
            {
                TnLocation location = locations[i];
                
                if(location == null)
                {
                    continue;
                }
                else if(loc == null)
                {
                    loc = location;
                }
                else if(loc.getLocalTimeStamp() < location.getLocalTimeStamp())
                {
                    loc = location;
                }
            }
        }
        
        return loc;
    }
    
    /**
     * some times we need the location be real-time enough.
     * This value is somehow acceptable.
     */
    public static final int RELIABLE_VALID_TIME = 2 * 60 * 1000;
    
    /**
     * A new GPS strategy.
     * Some times we need the location be real-time enough.
     * The location we got from this method is within 2 minutes.
     * 
     * @return location within 2 minutes; or null.
     */
    public TnLocation getReliableCurrentLocation()
    {
        long currentTime = System.currentTimeMillis();
        TnLocation result = null;
        TnLocation[] locations = getLocationArray(1);

        int count = getLocationFromLocationBuffer(RELIABLE_VALID_TIME,
            RELIABLE_VALID_TIME, TYPE_GPS, 1, locations);

        if (count > 0)
        {
            result = locations[0];
        }

        if (result == null)
        {
            TnLocation lastLocation = getLatestLocation(false);

            if (lastLocation != null && currentTime - lastLocation.getLocalTimeStamp() < RELIABLE_VALID_TIME)
            {
                result = lastLocation;
            }
        }

        return result;
    }
    
    /**
     * A new GPS strategy. will return the location data in callback mode.
     * Some times we need the location be real-time enough.
     * The location we got from this method is within 2 minutes.
     * 
     * @param timeout The wait time if currently the location is not available.
     * @param listener location listener
     */
    public void getReliableCurrentLocation(final long timeout, final LocationListener listener)
    {
        if (this.tnLocationProvider == null)
        {
            listener.requestCompleted(null, STATUS_TIMEOUT);
            return;
        }

        callBackCanceled = false;
        final TnLocation[] locations = getLocationArray(1);

        TnLocation location = getReliableCurrentLocation();

        if (location != null)
        {
            locations[0].set(location);
            listener.requestCompleted(locations, STATUS_SUCCESS);
            return;
        }
        else
        {
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    // TODO HANDLE THE CALL BACK!!!
                    synchronized (callBackMutex)
                    {
                        try
                        {
                            callBackMutex.wait(timeout);
                        }
                        catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    
                    if (callBackCanceled)
                    {
                        return;
                    }

                    TnLocation location = getReliableCurrentLocation();

                    if (location != null)
                    {
                        locations[0].set(location);
                        listener.requestCompleted(locations, STATUS_SUCCESS);
                        return;
                    }
                    else
                    {
                        listener.requestCompleted(null, STATUS_TIMEOUT);
                    }
                }
            });
            t.start();
        }
    }
    
    /**
     * Get the latest location from location buffer, device last known location and LocationCacheManager
     * if isNeedDefault is true, and all the locations are not available, will use default location.
     * @param isNeedDefault whether we need default location.
     * @return the latest location
     */
    public TnLocation getLatestLocation(boolean isNeedDefault)
    {
        TnLocation lastGpsLocationFromBuffer = getLastKnownLocationFromBuffer(TYPE_GPS);
        TnLocation lastNetworkLocationFromBuffer = getLastKnownLocationFromBuffer(TYPE_NETWORK);
        TnLocation lastGpsLocationFromDevice = getLastKnownLocationFromDevice(TYPE_GPS);
        TnLocation lastNetworkLocationFromDevice = getLastKnownLocationFromDevice(TYPE_NETWORK);
        TnLocation lastGpsLocationFromCache = getLastKnownLocationFromCache(TYPE_GPS);
        TnLocation lastNetworkLocationFromCache = getLastKnownLocationFromCache(TYPE_NETWORK);

        TnLocation[] locations = new TnLocation[]
        { lastGpsLocationFromBuffer, lastNetworkLocationFromBuffer,
                lastGpsLocationFromDevice, lastNetworkLocationFromDevice,
                lastGpsLocationFromCache, lastNetworkLocationFromCache };
        TnLocation lastLocation = getLatestLocation(locations);
        
        if(lastLocation == null && isNeedDefault)
        {
            lastLocation = getDefaultLocation();
        }
        
        return lastLocation;
    }
    
    /**
     * Get the real time location
     * 
     * @param type Can be TYPE_GPS or TYPE_NETWORK
     * @return the real time location
     */
    public TnLocation getRealTimeLocation(int type)
    {
        TnLocation[] locations = getLocationArray(1);
        int count = getLocationFromLocationBuffer(GPS_VALID_TIME, NETWORK_LOCATION_VALID_TIME, type, 1, locations);
        if(count > 0)
        {
            return locations[0];
        }
        else
        {
            return null;
        }
    }
}
