/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PluginManager.java
 *
 */
package com.telenav.module.dashboard;

import java.util.Arrays;
import java.util.Vector;

import android.graphics.Bitmap;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.weather.WeatherInfo;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.IWeatherProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkLocationProviderProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.map.MapConfig;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.entry.SplashScreenJob;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.widget.android.IGLSnapBitmapCallBack;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.map.MapContainer;

/**
 * @author jyxu
 * @date 2013-1-22
 */
public class DashboardManager implements Runnable, IServerProxyListener, INetworkStatusListener, IGLSnapBitmapCallBack
{
    private static DashboardManager instance = new DashboardManager();
    
    public static final int STATUS_ETA_REQUESTING = 0;//scouting...
    public static final int STATUS_ETA_NOT_GOT = 1;//--:--
    public static final int STATUS_ETA_NORMAL = 2;
    public static final int STATUS_ETA_TOO_CLOSE = 3;//00:00
    
    public static final int STATUS_RGC_REQUESTING = 0;//Locating you...
    public static final int STATUS_RGC_NOT_GOT = 1;//Unknown
    public static final int STATUS_RGC_NORMAL = 2; //Street/city
     
    protected static final int TIME_INTERVAL_MIN = 1 * 1000;
    protected static final int TIME_INTERVAL_MAX = 8 * 1000;
    protected static final int PAUSE_TIME_INTERVAL = 60 * 1000;
    protected static final int WEATHER_GPS_VALID_RANGE = 10 * 1000;
    protected static final int REQUEST_VALID_TIME = 5 * 1000;
    protected static final int WEATHER_VALID_TIME = 60 * 60 * 1000;
    protected static final int ETA_GPS_VALID_RANGE = 500;
    protected static final int ETA_VALID_TIME = 10 * 60 * 1000;
    protected static final int REQUEST_TIMEOUT = 30 * 1000;
    protected static final int RGC_GPS_VALID_RANGE = 1000;
    protected static final int VECTOR_MAP_GPS_VALID_RANGE = 3 * 1000;
    protected static final int DASHBOARD_MANAGERF_GPS_VALID_TIME = 24 * 60 * 60 * 1000;
    protected static final int ETA_REQUEST_DISTANCE_THRESHOLD = 1000;
    protected static final int ETA_HOME = 1;
    protected static final int ETA_WORK = 2;
    protected static final int ETA_LAST_TRIP = 3;
    protected static final int DEFAULT_SNAPPED_ZOOMLEVEL = 7;
    
    //US center location
    protected static double US_LAT = 0.0d; //center US location
    protected static double US_LON = 0.0d;

    static
    {
        initUSCerter();
    }
    
    protected Object mutex = new Object();
    protected Vector etaVector = new Vector();
    
    protected long mapResizeTimestamp = -1L;
    protected long lastCheckTimestamp = -1L;
    
    protected int systemUnits = -1;
    protected int homeDelayColor;
    protected int workDelayColor;
    protected int lastTripDelayColor;

    protected int homeEtaStatus;
    protected int workEtaStatus;
    protected int lastTripEtaStatus;
    
    protected int rgcStatus;
    protected int miniMapHeight = 0;
    protected int mapContainerHeight = 0;
    protected int miniMapWidth = 0;
    protected int mapContainerHeightLandscape = 0;
    protected int miniMapHeightLandscape = 0;
    protected int miniMapWidthLandscape = 0;
    
    protected boolean needUpdateEta = false;
    protected boolean needUpdateRgc = false;
    protected boolean isRunning = false;
    protected boolean isPaused = false;
    protected boolean isGoodGpsForSnap;
    protected boolean hasUpdateNoGpsView;
    protected boolean isGoodGpsLocation;
    protected boolean isFirstRound;
    
    protected String homeEtaStr;
    protected String workEtaStr;
    protected String lastTripEtaStr;
    protected String weatherCode = "";
    protected String weatherTemp = "";
    protected String cityName = "";
    protected String streetName = "";
    
    protected TnLocation currentLocation;
    protected TnLocation snappedLocation;
    
    protected Address latestRGCAddress;
    protected IDashboardInfoChangeListener listener;
    protected IUserProfileProvider userProfileProvider;
    protected NavSdkLocationProviderProxy navSdkLocationProvider = new NavSdkLocationProviderProxy(null);
    
    protected RequestInfo etaRequestInfo;
    protected RequestInfo rgcRequestInfo;
    protected RequestInfo weatherRequestInfo;
    protected DualBufferImageManager dualBufferManager;
    
    protected boolean isMapResizing;
    
    class RequestInfo
    {
        TnLocation location;
        int retryTimes;
        int requestId;
        long lastRequestTimestamp;
        long responseTimestamp;
        boolean isSucc;
        
        boolean isRequesting()
        {
            return lastRequestTimestamp > responseTimestamp;
        }
        
        void reset()
        {
            location = null;
            retryTimes = 0;
            requestId = 0;
            lastRequestTimestamp = 0;
            responseTimestamp = 0;
            isSucc = false;
        }
    }
    
    public DashboardManager()
    {
        isGoodGpsLocation = false;
        this.currentLocation = getGpsLocation();
        hasUpdateNoGpsView = false;
        getMinMapWidthHeight();
        dualBufferManager = new DualBufferImageManager();
        isRunning = false;
        isFirstRound = true;
    }

    private static void initUSCerter()
    {
        TnLocation defaultLocation = LocationProvider.getInstance().getDefaultLocation();
        
        US_LAT = (double)defaultLocation.getLatitude() / 100000.0d;
        US_LON = (double)defaultLocation.getLongitude() / 100000.0d;
    }

    public static DashboardManager getInstance()
    {
        return instance;
    }

    public void setUserProfileProvider(IUserProfileProvider userProfileProvider)
    {
        this.userProfileProvider = userProfileProvider;
    }
    
    public void start()
    {
        if (!NetworkStatusManager.getInstance().isListenerExist(this))
        {
            NetworkStatusManager.getInstance().addStatusListener(this);
        }
        
        if(!isRunning)
        {
            isRunning = true;
			isPaused = false;
            Thread t = new Thread(this);
            t.start();
        }
    }
    
    public void mapResizeBegin()
    {
        isMapResizing = true;
        mapResizeTimestamp = System.currentTimeMillis();
    }
    
    public void mapResizeEnd()
    {
        isMapResizing = false;
    }
    
    public void stop()
    {
        isRunning = false;
        NetworkStatusManager.getInstance().removeStatusListener(this);
        synchronized (mutex)
        {
           mutex.notify();
           if (Logger.DEBUG)
           {
               Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> mutex notify 0, " + System.currentTimeMillis());
           }
        }
    }
    
    public void pause()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> DashboardManager pause");
        }
        isPaused = true;
    }
    
    public void resume()
    {
        if (isPaused)
        {
            isPaused = false;
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> resume , " + System.currentTimeMillis());
            }
            synchronized (mutex)
            {
               mutex.notify();
               if (Logger.DEBUG)
               {
                   Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> mutex notify 1, " + System.currentTimeMillis());
               }
            }
        }
    }
    
    @Override
    public void run()
    {
        while (isRunning)
        {
            if (isFirstRound)
            {
                isFirstRound = false;
                
                synchronized (mutex)
                {
                    try
                    {
                        System.out.println("DB-Test startup profile --> wait for display : " + System.currentTimeMillis());
                        mutex.wait(800);
                    }
                    catch (Throwable e)
                    {
                        
                    }
                }
            }
            
            int waitInterval = TIME_INTERVAL_MIN;
            
            if(isPaused)
            {
                waitInterval = PAUSE_TIME_INTERVAL;
            }
            else
            {
                if (System.currentTimeMillis() - lastCheckTimestamp > TIME_INTERVAL_MAX)
                {
                    lastCheckTimestamp = System.currentTimeMillis();
                    this.currentLocation = this.getGpsLocation();
                    if(this.currentLocation == null) //avoid updateView too frequent when no gps
                    {
                        this.handleNoGps();
                        if(!hasUpdateNoGpsView)
                        {
                            updateView();
                            hasUpdateNoGpsView = true;
                        }
                    }
                    else
                    {
                        hasUpdateNoGpsView = false;
                        if(checkEta())
                        {
                            this.requestEta();
                        }
                        
                        if(checkRgc())
                        {
                            this.requestRgc();
                        }
                        
                        if(checkWeather())
                        {
                            this.requestWeather();
                        }
                    }
                }
                              
                boolean isPortrait = this.isPortrait();
                requestSnappingVectorMap(isPortrait);
            }
            
            synchronized (mutex)
            {
                try
                {
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> waiting duration : " + waitInterval);
                    }
                    mutex.wait(waitInterval);
                }
                catch (Throwable e)
                {
                    
                }
            }
        }
    }

    public void setDashboardInfoChangeListener(IDashboardInfoChangeListener listener)
    {
        this.listener = listener;
    }
    
    public IDashboardInfoChangeListener getDashboardInfoChangeListener()
    {
        return this.listener;
    }

    protected void requestRgc()
    {
        cityName = "";
        streetName = "";
        latestRGCAddress = null;
        rgcStatus = STATUS_RGC_REQUESTING;
        needUpdateRgc = false;
        
        if (rgcRequestInfo == null)
        {
            rgcRequestInfo = new RequestInfo();
            rgcRequestInfo.retryTimes = 0;
        }
        
        TnLocation location = new TnLocation("");
        location.set(currentLocation);
        rgcRequestInfo.location = location;
        rgcRequestInfo.lastRequestTimestamp = System.currentTimeMillis();
        
        updateView();
        IRgcProxy proxy = ServerProxyFactory.getInstance().createRgcProxy(null, CommManager.getInstance().getComm(), this);
        proxy.requestRgc(currentLocation.getLatitude(), currentLocation.getLongitude());
    }
    
    protected void startFollowVehicleMode()
    {
        //map center follow with car.
        MapContainer mapContainer = MapContainer.getInstance();
        mapContainer.setRenderingMode(NavSdkMapProxy.RENDERING_MODE_2D_NORTH_UP);
        if (this.currentLocation != null)
        {
            mapContainer.followVehicle();
        }
        mapContainer.clearRoute();
    }
    
    public void initBackgroundMap()
    {
        TnLocation currentLocation = getLastRequestedLocation() == null ? LocationProvider.getInstance().getDefaultLocation()
                : getLastRequestedLocation();
        startFollowVehicleMode();
        moveMapToCurrentLocation(currentLocation);
        MapContainer.getInstance().enableGPSCoarse(false);
        showTrafficLayer(true);
    }

    protected void requestWeather()
    {
        Stop stop = new Stop();
        stop.setLat(currentLocation.getLatitude());
        stop.setLon(currentLocation.getLongitude());
        boolean isCanadianCarrier = false;
        boolean useOriginalPicCode = true;
        
        if (weatherRequestInfo == null)
        {
            weatherRequestInfo = new RequestInfo();
            weatherRequestInfo.retryTimes = 0;
        }
        
        TnLocation location = new TnLocation("");
        location.set(currentLocation);
        weatherRequestInfo.location = location;
        weatherRequestInfo.lastRequestTimestamp = System.currentTimeMillis();
        
        IWeatherProxy proxy = ServerProxyFactory.getInstance().createWeatherProxy(null, CommManager.getInstance().getComm(),
            this, userProfileProvider);
        proxy.requestWeather(stop, isCanadianCarrier, useOriginalPicCode, 0, REQUEST_TIMEOUT);
    }

    protected void requestEta()
    {
        boolean isResumeTrip = hasResumeTrip();
        requestEta(isResumeTrip);
    }
    
    protected boolean hasResumeTrip()
    {
        Address lastTrip = DaoManager.getInstance().getTripsDao().getLastTrip();
        long tripTime = DaoManager.getInstance().getTripsDao().tripTime();
        long currentTime = System.currentTimeMillis();
        boolean doResume = false;
        if (tripTime > 0)
        {
            doResume = ((currentTime - tripTime) < (7 * 24 * 60 * 60 * 1000)) ? true : false;
        }
        if (!doResume)
        {
            DaoManager.getInstance().getTripsDao().clear();
        }

        return doResume && lastTrip != null && !isLastTripHomeOrOffice(lastTrip.getStop());
    }
    
    private boolean isLastTripHomeOrOffice(Stop lastTripStop)
    {
        if (lastTripStop == null)
        {
            return true;
        }
        Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
        if (home != null && home.getLat() == lastTripStop.getLat() && home.getLon() == lastTripStop.getLon())
        {
            return true;
        }
        Stop office = DaoManager.getInstance().getAddressDao().getOfficeAddress();
        if (office != null && office.getLat() == lastTripStop.getLat() && office.getLon() == lastTripStop.getLon())
        {
            return true;
        }
        return false;
    }


    protected void requestEta(boolean isResumeTrip)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", ("MiniMapProfile --> request Eta,  " + System.currentTimeMillis()));
        }
        
        if (etaRequestInfo == null)
        {
            etaRequestInfo = new RequestInfo();
            etaRequestInfo.retryTimes = 0;
        }
        
        int requestId = (int)(Math.random() * 10000);
        
        TnLocation location = new TnLocation("");
        location.set(currentLocation);
        etaRequestInfo.location = location;
        etaRequestInfo.lastRequestTimestamp = System.currentTimeMillis();
        etaRequestInfo.requestId = requestId;
        
        this.needUpdateEta = false;
        etaVector.removeAllElements();
        Stop home = null;
        Stop office = null;
        Stop lastTrip = null;
        int lat = currentLocation.getLatitude();
        int lon = currentLocation.getLongitude();
        if(isResumeTrip)
        {
            lastTrip = DaoManager.getInstance().getTripsDao().getLastTrip() == null ? null : DaoManager.getInstance()
                    .getTripsDao().getLastTrip().getStop();
            if (isValidAddress(lastTrip))
            {
                if (isCloseToDestination(lastTrip.getLat(), lastTrip.getLon(), lat, lon))
                {
                    lastTripEtaStatus = STATUS_ETA_TOO_CLOSE;  
                }
                else
                {
                    lastTripEtaStatus = STATUS_ETA_REQUESTING;
                    etaVector.addElement(ETA_LAST_TRIP);
                }
            }
        }
        else
        {
            home = DaoManager.getInstance().getAddressDao().getHomeAddress();
            if (isValidAddress(home))
            {
                if (isCloseToDestination(home.getLat(), home.getLon(), lat, lon))
                {
                    homeEtaStatus = STATUS_ETA_TOO_CLOSE;  
                }
                else
                {
                    homeEtaStatus = STATUS_ETA_REQUESTING;
                    etaVector.addElement(ETA_HOME);
                }
            }
            office = DaoManager.getInstance().getAddressDao().getOfficeAddress();
            if (isValidAddress(office))
            {
                if (isCloseToDestination(office.getLat(), office.getLon(), lat, lon))
                {
                    workEtaStatus = STATUS_ETA_TOO_CLOSE;  
                }
                else
                {
                    workEtaStatus = STATUS_ETA_REQUESTING;
                    etaVector.addElement(ETA_WORK);
                }
            }
        }

        if (etaVector.size() > 0)
        {
            int[] destLatArray = new int[etaVector.size()];
            int[] destLonArray = new int[etaVector.size()];
            for (int i = 0; i < etaVector.size(); i++)
            {
                int eta = (Integer) etaVector.elementAt(i);
                switch (eta)
                {
                    case ETA_HOME:
                    {
                        destLatArray[i] = home.getLat();
                        destLonArray[i] = home.getLon();
                        break;
                    }
                    case ETA_WORK:
                    {
                        destLatArray[i] = office.getLat();
                        destLonArray[i] = office.getLon();
                        break;
                    }
                    case ETA_LAST_TRIP:
                    {
                        destLatArray[i] = lastTrip.getLat();
                        destLonArray[i] = lastTrip.getLon();
                        break;
                    }
                }
            }
            
            //update ETA status before sending
            sendEta(destLatArray, destLonArray, requestId);
            updateView();
        }
    }

    private void sendEta(int[] destLatArray, int[] destLonArray, int requestId)
    {
        NavSdkNavigationProxy navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(this);
        
        int size = destLatArray.length;
        if(size < 1)
        {
            return;
        }
		
        Address[] orgAddressArray = new Address[size]; 
        Address[] destAddressArray = new Address[size];
        for (int i = 0; i < size; i++)
        {
            Address orgAddress = new Address();
            Stop orgStop = new Stop();
            orgStop.setLat(currentLocation.getLatitude());
            orgStop.setLon(currentLocation.getLongitude());
            orgAddress.setStop(orgStop);
            orgAddressArray[i] = orgAddress;
            
            Address destAddress = new Address();
            Stop destStop = new Stop();
            destStop.setLat(destLatArray[i]);
            destStop.setLon(destLonArray[i]);
            destAddress.setStop(destStop);
            destAddressArray[i] = destAddress;
        }
       
        
        int routeType = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
            Preference.ID_PREFERENCE_ROUTETYPE);
        if (routeType < 0 || routeType == Route.ROUTE_PEDESTRIAN)
        {// for route pedestrian, it is hard to display
            routeType = Route.ROUTE_FASTEST;
        }
        
        int avoidSettings =  ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
            Preference.ID_PREFERENCE_ROUTE_SETTING);
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        if(isOnboard)
        {
            avoidSettings = -1;
        }
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", ("MiniMapProfile --> sending Eta oriSize : " + orgAddressArray.length + " , destSize : " + destAddressArray.length + " , " + System.currentTimeMillis()));
        }
        navigationProxy.requestEta(orgAddressArray, destAddressArray, routeType, avoidSettings, requestId);
    }

    /**
     * calculate the actual distance before ETA request. PRD requirement: If the user is within 1000 feet of home/work,
     * the button will say "Here Now" and will not be clickable.
     * 
     * @param lat1 current lat
     * @param lon1 current lon
     * @param lat2 last lat
     * @param lon2 last lon
     * @return > expect return false, <= expect return true;
     */
    protected boolean isCloseToDestination(int lat1, int lon1, int lat2, int lon2)
    {
        long distMeter = DataUtil.gpsDistance(lat2 - lat1, lon2 - lon1, DataUtil.getCosLat(lat2));
        double distance = distMeter * StringConverter.METER_TO_FT;
        if (distance <= ETA_REQUEST_DISTANCE_THRESHOLD)
        {
            return true;
        }
        return false;
    }

    public boolean isValidAddress(Stop stop)
    {
        // verify the stop is valid.
        if (stop != null && stop.getLat() != 0 && stop.getLon() != 0)
        {
            return true;
        }
        return false;
    }

    private String getEtaDisplayString(int status)
    {
        String str = "";
        switch(status)
        {
            case STATUS_ETA_REQUESTING:
            {
                str = "Scouting...";
                break;
            }
            case STATUS_ETA_NOT_GOT :
            {
                str = "--:--";
                break;
            }
            case STATUS_ETA_TOO_CLOSE:
            {
                str = "00:00";
                break;
            }
        }
        return str;
    }

    protected TnLocation getGpsLocation()
    {
        TnLocation gpsLocation = LocationProvider.getInstance().getCurrentLocation(10 * 60 * 1000,
            -1, LocationProvider.TYPE_GPS, true);
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> mini map gps : " + gpsLocation);
        }
        
        if(gpsLocation == null)
        {
            gpsLocation = LocationProvider.getInstance().getCurrentLocation(DASHBOARD_MANAGERF_GPS_VALID_TIME,
                DASHBOARD_MANAGERF_GPS_VALID_TIME, LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, true);
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> mini map network : " + gpsLocation);
            }
            if (gpsLocation == null)
            {
                gpsLocation = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> mini lastKnown : " + gpsLocation);
                }
            }
            
            if(gpsLocation != null) //need to check time because we maybe get lastKnownGps which is  more than 20min ago
            {
                if(System.currentTimeMillis() - gpsLocation.getLocalTimeStamp() > DASHBOARD_MANAGERF_GPS_VALID_TIME)
                {
                    gpsLocation = null;
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> too old, set to null");
                    }
                }
            }
            this.isGoodGpsLocation = false;
        }
        else
        {
            this.isGoodGpsLocation = true;
        }
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> isGoodGpsLocation : " + isGoodGpsLocation);
        }
        
        return gpsLocation;
    }
    
    public TnLocation getCurrentLocation()
    {
        return this.currentLocation;
    }

    private void updateView()
    {
        updateView(false);
    }
    
    private void updateView(boolean isMiniMap)
    {
        if(listener != null)
        {
            listener.onDashboardInfoChanged(isMiniMap);
        }
    }
    
    protected void handleWeatherResponse(WeatherInfo info)
    {
        // weatherTemp is Fahrenheit degrees
        weatherTemp = info.getTemp();
        weatherCode = String.valueOf(info.getWeatherCode());
        
        if (weatherRequestInfo != null)
        {
            weatherRequestInfo.responseTimestamp = System.currentTimeMillis();
            weatherRequestInfo.retryTimes = 0;
            weatherRequestInfo.isSucc = true;
        }
    }
    
    protected String getWeatherTempInCelsius()
    {
        if(weatherTemp != null)
        {
            try
            {
                int tempF = Integer.parseInt(weatherTemp);
                return Integer.toString((tempF - 32) * 5 / 9);
            }
            catch(Exception e)
            {}
        }
        return null;
    }
    protected void handleWeatherError()
    {
        weatherRequestInfo.responseTimestamp = System.currentTimeMillis();
        weatherTemp = null;
        weatherCode = null;
    }
    
    protected void handleRgcError()
    {
        rgcRequestInfo.responseTimestamp = System.currentTimeMillis();
        rgcStatus = STATUS_RGC_NOT_GOT;
        cityName = "";
        streetName = "";
        latestRGCAddress = null;
    }
    
    private void handleNoGps()
    {
        //eta
        homeEtaStatus = STATUS_ETA_NOT_GOT;
        workEtaStatus = STATUS_ETA_NOT_GOT;
        lastTripEtaStatus = STATUS_ETA_NOT_GOT;
        
        //weater
        weatherTemp = null;
        weatherCode = null;
        
        //rgc
        rgcStatus = STATUS_RGC_NOT_GOT;
        cityName = "";
        streetName = "";
        latestRGCAddress = null;
    }
    
    protected void handleEtaError()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> handle ETA Error! " + System.currentTimeMillis());
        }
        
        etaRequestInfo.responseTimestamp = System.currentTimeMillis();

        for (int i = 0, s = etaVector.size(); i < s; i++)
        {
            int eta = (Integer) etaVector.elementAt(i);
            switch (eta)
            {
                case ETA_HOME:
                {
                    homeEtaStatus = STATUS_ETA_NOT_GOT;
                    break;
                }
                case ETA_WORK:
                {
                    workEtaStatus = STATUS_ETA_NOT_GOT;
                    break;
                }
                case ETA_LAST_TRIP:
                {
                    lastTripEtaStatus = STATUS_ETA_NOT_GOT;
                    break;
                }
            }
        }
    }
    
    protected void handleRgcResponse(AbstractServerProxy proxy)
    {
        if(((IRgcProxy) proxy).getAddress() != null)
        {
            latestRGCAddress =  ((IRgcProxy) proxy).getAddress();
            Stop stop = ((IRgcProxy) proxy).getAddress().getStop();
            if (stop != null)
            {
                cityName = stop.getCity();
                streetName = stop.getFirstLine();
                rgcStatus = STATUS_RGC_NORMAL;
                rgcRequestInfo.responseTimestamp = System.currentTimeMillis();
                rgcRequestInfo.retryTimes = 0;
                rgcRequestInfo.isSucc = true;
                return;
            }
        }
        this.handleRgcError();
    }

    protected void handleEtaResponse(AbstractServerProxy proxy)
    {
        NavSdkNavigationProxy navProxy = (NavSdkNavigationProxy) proxy;
        
        if (navProxy.getRequestId() != etaRequestInfo.requestId)
        {
            return;
        }
        
        Vector staticEta = navProxy.getStaticEta();
        Vector dynmicEta = navProxy.getDynmicEta();
        if (staticEta != null && dynmicEta != null && etaVector.size() > 0 && etaVector.size() == staticEta.size())
        {
            for (int i = 0, s = etaVector.size(); i < s; i++)
            {
                int eta = (Integer) etaVector.elementAt(i);
                long staticEtaValue = (Long)staticEta.elementAt(i);
                long dynmicEtaValue = (Long)dynmicEta.elementAt(i);
                switch (eta)
                {
                    case ETA_HOME:
                    {
                        if(staticEtaValue > 0 && dynmicEtaValue > 0)
                        {
                            homeEtaStatus = STATUS_ETA_NORMAL;
                            homeEtaStr = parseEtaDisplayTime(dynmicEtaValue);
                            homeDelayColor = this.getEtaDelayColor(staticEtaValue, dynmicEtaValue);
                        }
                        else
                        {
                            homeEtaStatus = STATUS_ETA_NOT_GOT;
                        }
                        break;
                    }
                    case ETA_WORK:
                    {
                        if(staticEtaValue > 0 && dynmicEtaValue > 0)
                        {
                            workEtaStatus = STATUS_ETA_NORMAL;
                            workEtaStr = parseEtaDisplayTime(dynmicEtaValue);
                            workDelayColor = this.getEtaDelayColor(staticEtaValue, dynmicEtaValue);
                        }
                        else
                        {
                            workEtaStatus = STATUS_ETA_NOT_GOT;
                        }
                        break;
                    }
                    case ETA_LAST_TRIP:
                    {
                        if(staticEtaValue > 0 && dynmicEtaValue > 0)
                        {
                            lastTripEtaStatus = STATUS_ETA_NORMAL;
                            lastTripEtaStr = parseEtaDisplayTime(dynmicEtaValue);
                            lastTripDelayColor = this.getEtaDelayColor(staticEtaValue, dynmicEtaValue);
                        }
                        else
                        {
                            lastTripEtaStatus = STATUS_ETA_NOT_GOT;
                        }
                        break;
                    }
                }
            }

            etaRequestInfo.responseTimestamp = System.currentTimeMillis();
            etaRequestInfo.retryTimes = 0;
            etaRequestInfo.isSucc = true;
            
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> Eta completed lastEtaUpdatedTimeStamp : " + etaRequestInfo.responseTimestamp);
            }
        }
        else
        {
            handleEtaError();
        }
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "Dashboard, handle ETA finished!");
        }
    }

    private int getEtaDelayColor(long staticEtaValue, long dynamicEtaValue)
    {
        int colorRed = UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_RED_TIME_COLOR);
        int colorOrange = UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_ORANGE_TIME_COLOR);
        int delayTimeColor = UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_NORMAL_STRING_COLOR);
        long delay = dynamicEtaValue - staticEtaValue;
        int percent = (int) (delay * 100 / dynamicEtaValue) ;
        if ( percent < 5 )
        {
            delayTimeColor = UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_NORMAL_STRING_COLOR);
        }
        else if ( percent >= 5 && percent <= 15 )
        {
            delayTimeColor = colorOrange;
        }
        else if ( percent >= 15 )
        {
            delayTimeColor = colorRed;
        }
        return delayTimeColor;
    }
    
    public String parseEtaDisplayTime(long second)
    {
        String etaString = "00:";
        int hour = (int)(second / 3600);
        if ( hour > 0 && hour < 10 )
        {
            etaString = "0" + Integer.toString(hour) + ":";
        }
        else if ( hour >= 10 )
        {
            etaString = Integer.toString(hour) + ":";
        }
        int minute = (int)((second % 3600) / 60);
        if ( minute > 0 && minute < 10 )
        {
            etaString += "0" + Integer.toString(minute);
        }
        else if ( minute >= 10 )
        {
            etaString += Integer.toString(minute);
        }
        else
        {
            etaString += "00";
        }
        return etaString;
    }
    
    protected boolean checkWeather()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> check need weather start");
        }
        //onboard does not support weather
        if(!NetworkStatusManager.getInstance().isConnected())
        {
           return false;  
        }
        
        boolean isNeedRequest = checkRequestInfo(weatherRequestInfo, WEATHER_VALID_TIME, WEATHER_GPS_VALID_RANGE);
        
        return isNeedRequest;
    }

    protected boolean checkRequestInfo(RequestInfo requestInfo, int timeThreshold, int distanceThreshold)
    {
        //if no current location, wouldn't do it.
        if (currentLocation == null)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- no current location");
            }
            return false;
        }
        
        if (requestInfo == null)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- no previous request");
            }
            return true;
        }
        else
        {
            if (requestInfo.isRequesting())
            {
                if (System.currentTimeMillis() - requestInfo.lastRequestTimestamp > REQUEST_TIMEOUT)
                {
                    if (checkDistanceThreshold(requestInfo.location, distanceThreshold))
                    {
                        requestInfo.reset();
                        
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- location changed");
                        }
                        return true;
                    }
                    else if (requestInfo.retryTimes >= 2)
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- retry 2 times");
                        }
                        return false;
                    }
                    else
                    {
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- retry ...");
                        }
                        requestInfo.retryTimes ++;
                        return true;
                    }
                }
                else
                {
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- requesting in 30s ...");
                    }
                    return false;
                }
            }
            else
            {
                if (checkDistanceThreshold(requestInfo.location, distanceThreshold))
                {
                    if (Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- location changed");
                    }
                    requestInfo.reset();
                    return true;
                }
                else
                {
                    if (requestInfo.isSucc)
                    {
                        if (System.currentTimeMillis() - requestInfo.responseTimestamp > timeThreshold)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- time threshold exceed");
                            }
                            requestInfo.reset();
                            return true;
                        }
                    }
                    else
                    {
                        if (requestInfo.retryTimes < 2)
                        {
                            if (Logger.DEBUG)
                            {
                                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkRequestInfo-- last request fail, but need retry ... retryTimes : " + requestInfo.retryTimes);
                            }
                            requestInfo.retryTimes ++;
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    protected boolean checkEta()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> check need ETA start");
        }
        
        if (!NetworkStatusManager.getInstance().isConnected())
        {
            //if there's no network and no ( premium feature && local map date ) available, do not request RGC
            int status = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_HYBRID);
            if (status == FeaturesManager.FE_DISABLED || status == FeaturesManager.FE_UNPURCHASED
                    || !MapDownloadStatusManager.getInstance().isOnBoardDataAvailable())
            {
                return false;
            }
        }
        
        if (currentLocation == null)
        {
            return false;
        }
        
        if (needUpdateEta)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> check ETA yes 0, needUpdateEta : " + needUpdateEta + " , this : " + this + " , " + System.currentTimeMillis());
            }
            return true;
        }
        
        boolean isNeedRequest = checkRequestInfo(etaRequestInfo, ETA_VALID_TIME, ETA_GPS_VALID_RANGE);

        return isNeedRequest;
    }
    
    protected boolean checkRgc()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> check need RGC start");
        }

        if (!NetworkStatusManager.getInstance().isConnected())
        {
            // if there's no network and no ( premium feature && local map date ) available, do not request RGC
            int status = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_HYBRID);
            if (status == FeaturesManager.FE_DISABLED || status == FeaturesManager.FE_UNPURCHASED
                    || !MapDownloadStatusManager.getInstance().isOnBoardDataAvailable())
            {
                return false;
            }
        }
        if (needUpdateRgc)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> check Rgc : " + needUpdateRgc + " , this : " + this + " , " + System.currentTimeMillis());
            }
            return true;
        }
        
        
        boolean isNeedRequest = checkRequestInfo(rgcRequestInfo, Integer.MAX_VALUE, RGC_GPS_VALID_RANGE);

        return isNeedRequest;
    }
    
    protected boolean checkDistanceThreshold(TnLocation lastLocation, int threshold)
    {
        return checkDistanceThreshold(lastLocation, threshold, false);
    }
 
    protected boolean checkDistanceThreshold(TnLocation lastLocation, int threshold, boolean isNeedCheckProvider)
    {
        if (isNeedCheckProvider)
        {
            if (isGoodGpsForSnap != isGoodGpsLocation)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> gps status changed, isHasGpsNow : " + isGoodGpsLocation + " , wasHasGpsBefore : " + isGoodGpsForSnap);
                }
                return true;
            }
            
            if (lastLocation != null && currentLocation != null
                    && !lastLocation.getProvider().equals(currentLocation.getProvider()))
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> gps type changed, need request");
                }
                return true;
            }
        }
        
        int oriLat = 0;
        int oriLon = 0;
        if (lastLocation != null)
        {
            oriLat = lastLocation.getLatitude();
            oriLon = lastLocation.getLongitude();
        }
        
        int currentLat = 0;
        int currentLon = 0;
        if(currentLocation != null)
        {
            currentLat = currentLocation.getLatitude();
            currentLon = currentLocation.getLongitude();
        }
        long distMeter = DataUtil.gpsDistance(currentLat - oriLat, currentLon - oriLon, DataUtil.getCosLat(currentLat));
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> 2 location distance : " + distMeter);
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> 2 location threshold : " + threshold);
        }
        
        if (distMeter > threshold)
        {
            return true;
        }

        return false;
    }

    protected boolean checkMiniMapDistanceThreshold(TnLocation lastSnapLocation)
    {
        if(currentLocation == null)
        {
            //lastSnapLocation is always not null. check if the snap location is center of US
            if (lastSnapLocation.getLatitude() == US_LAT * 100000 && lastSnapLocation.getLongitude() == US_LON * 100000)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkMiniMapDistanceThreshold lastSnapLocation : " + lastSnapLocation);
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> checkMiniMapDistanceThreshold latestLocation  : " + currentLocation);
            }
            
            if (checkDistanceThreshold(lastSnapLocation, VECTOR_MAP_GPS_VALID_RANGE, true))
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        boolean needUpdateView = false;
        if (proxy instanceof IRgcProxy)
        {
            handleRgcResponse(proxy);
            needUpdateView = true;
        }
        else if (proxy instanceof IWeatherProxy)
        {
            IWeatherProxy weatherProxy = (IWeatherProxy) proxy;
            if (weatherProxy.getWeather() != null && weatherProxy.getWeather().getCurrentWeatherInfo() != null)
            {
                handleWeatherResponse(weatherProxy.getWeather().getCurrentWeatherInfo());
                needUpdateView = true;
            }
        }
        else if (proxy instanceof NavSdkNavigationProxy)
        {
            Logger.log(Logger.INFO, "DashboardManager", "Dashboard, requestETA finish: time="+System.currentTimeMillis());
            handleEtaResponse(proxy);
            needUpdateView = true;
        }
        if(needUpdateView)
        {
            updateView();
        }
    }
    
    @Override
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
       
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        boolean needUpdateView = false;
        if (proxy instanceof IRgcProxy)
        {
            handleRgcError();
            needUpdateView = true;
        }
        else if (proxy instanceof NavSdkNavigationProxy)
        {
            handleEtaError();
            needUpdateView = true;
        }
        else if(proxy instanceof IWeatherProxy )
        {
            handleWeatherError();
            needUpdateView = true;
        }
        if(needUpdateView)
        {
            this.updateView();
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        boolean needUpdateView = false;
        if (proxy instanceof IRgcProxy)
        {
            handleRgcError();
            needUpdateView = true;
        }
        else if (proxy instanceof NavSdkNavigationProxy)
        {
            handleEtaError();
            needUpdateView = true;
        }
        else if(proxy instanceof IWeatherProxy )
        {
            handleWeatherError();
            needUpdateView = true;
        }
        if(needUpdateView)
        {
            this.updateView();
        }
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return NetworkStatusManager.getInstance().isConnected();
    }

    public void notifyUpdateEta()
    {
        synchronized (mutex)
        {
           needUpdateEta = true;
           lastCheckTimestamp = -1L;
           mutex.notify();
           
           if (Logger.DEBUG)
           {
               Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> mutex notify 2, " + System.currentTimeMillis());
           }
        }
    }

    public String getHomeEtaStr()
    {
        if(homeEtaStatus == STATUS_ETA_NORMAL)
        {
            return homeEtaStr;
        }
        else
        {
            return getEtaDisplayString(homeEtaStatus);
        }
    }

    public String getWorkEtaStr()
    {
        if(workEtaStatus == STATUS_ETA_NORMAL)
        {
            return workEtaStr;
        }
        else
        {
            return getEtaDisplayString(workEtaStatus);
        }
    }

    public String getLastTripEtaStr()
    {
        if(lastTripEtaStatus == STATUS_ETA_NORMAL)
        {
            return lastTripEtaStr;
        }
        else
        {
            return getEtaDisplayString(lastTripEtaStatus);
        }
    }

    public int getHomeDelayColor()
    {
        return homeDelayColor;
    }

    public int getWorkDelayColor()
    {
        return workDelayColor;
    }

    public int getLastTripDelayColor()
    {
        return lastTripDelayColor;
    }

    public String getWeatherCode()
    {
        return weatherCode;
    }

    public String getWeatherTempInFahrenheit()
    {
        return weatherTemp;
    }

    public String getCityName()
    {
        return cityName;
    }

    public String getStreetName()
    {
        return streetName;
    }
    
    public Address getCurrentAddress()
    {
        return latestRGCAddress;
    }
    
    public int getHomeEtaStatus()
    {
        return homeEtaStatus;
    }

    public int getWorkEtaStatus()
    {
        return workEtaStatus;
    }

    public int getLastTripEtaStatus()
    {
        return lastTripEtaStatus;
    }
    
    public TnLocation getLastRequestedLocation()
    {
        return currentLocation;
    }
    
    public int getRgcStatus()
    {
        return rgcStatus;
    }
    
    public interface IDashboardInfoChangeListener
    {
        public void onDashboardInfoChanged(boolean isMiniMap);
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        this.needUpdateEta = true;
        this.needUpdateRgc = true;
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> networkStatus change , " + System.currentTimeMillis());
        }
        synchronized (mutex)
        {
           mutex.notify();
           if (Logger.DEBUG)
           {
               Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> mutex notify 3, " + System.currentTimeMillis());
           }
        }
    }

    public void snapBitmapCompleted(int buffer[], int width, int height, long transactionId)
    {
        if (isMapResizing)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> Fake Map--------Resize Checking");
            }

            if ((System.currentTimeMillis() - mapResizeTimestamp) > 5 * 1000)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager",
                        "MiniMapProfile --> Fake Map--------Resize Checking Timeout, set snapped image as valid!!!");
                }
                isMapResizing = false;
            }
            else
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager",
                        "MiniMapProfile --> Fake Map--------Resize Checking Failed, ignore the snapped image!!!");
                }
                return;
            }
        }
        else
        {
            if ((transactionId - mapResizeTimestamp) < 1000)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager",
                        "MiniMapProfile --> Fake Map--------Resize Checking Failed, snapped result too close to resizing!!!");
                }
                return;
            }
        }
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> got feedback " + System.currentTimeMillis());
        }
        
        if (isPaused)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> got feedback but pause, return " + System.currentTimeMillis());
            }
            return;
        }
        
        if (buffer == null || buffer.length != width * height)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> got feedback but buffer size wrong, return , " + System.currentTimeMillis());
            }
            return;
        }
        
        //TNANDROID-5177
        //Map becomes partially black when switching portrait/landscape modes
        if (height == miniMapHeight && width == miniMapWidth)
        {
            System.arraycopy(buffer, 0, dualBufferManager.getBuffer(true), 0, buffer.length );
        }
        else if (height == miniMapHeightLandscape && width == miniMapWidthLandscape)
        {
            System.arraycopy(buffer, 0, dualBufferManager.getBuffer(false), 0, buffer.length);
        }
        
        updateView(true);
    }
    
    public Bitmap getMiniMap(boolean isPortrait)
    {
        return dualBufferManager.getMiniMap(isPortrait);
    }
    
    public void snapBitmapCanceled(long transactionId)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> snapBitmapCanceled, " + System.currentTimeMillis());
        }
    }
    
    /**
     * OpenGl snap shot start
     * @param location
     * @param needShowTraffic
     */
    protected synchronized void requestSnappingVectorMap(boolean isPortrait)
    {
        if (isPaused) //JY: Need to Notice about it
        {
            return;
        }

        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> update screen");
        }
        
        if (this.currentLocation != null)
        {
            moveMapToCurrentLocation(this.currentLocation);
            if (this.isGoodGpsLocation) // if good gps, show blue dot
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> isGoodGpsLocation enableVehicleAnnotation" + " , " + System.currentTimeMillis());
                }
                MapContainer.getInstance().changeToSpriteVehicleAnnotation();
            }
            else
            // if bad gps, does not show blue dot, and if network gps, hide the circle
            {
                if (TnLocationManager.NETWORK_PROVIDER.equalsIgnoreCase(currentLocation.getProvider()))
                {
                    currentLocation.setAccuracy(0);
                }
                MapContainer.getInstance().disableVehicleAnnotation();
            }
        }
        else
        // US whole map
        {
            MapContainer.getInstance().setZoomLevel(MapConfig.MAP_WHOLE_US_ZOOM_LEVEL);
            MapContainer.getInstance().disableVehicleAnnotation();
        }
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, "DashboardManager", "MiniMapProfile --> start snap, checking, " + System.currentTimeMillis());
        }

        if(this.currentLocation != null)
        {
            startSnapping(isPortrait, this.currentLocation);
        }
        else
        {
            TnLocation location = new TnLocation("");
            location.setLatitude((int) (US_LAT * 100000));
            location.setLongitude((int) (US_LON * 100000));
            startSnapping(isPortrait, location);
        }
    }
    
    private void moveMapToCurrentLocation(TnLocation currentLocation)
    {
        TnLocation location = new TnLocation(currentLocation.getProvider());
        location.set(currentLocation);
        if (currentLocation != null)
        {
            if (TnLocationManager.NETWORK_PROVIDER.equalsIgnoreCase(currentLocation.getProvider())
                    || TnLocationManager.TN_NETWORK_PROVIDER.equalsIgnoreCase(currentLocation.getProvider()))
            {
                location.setAccuracy(0);
            }
            navSdkLocationProvider.updateLocation(location);
            int currentLat = currentLocation.getLatitude();
            int currentLon = currentLocation.getLongitude();
            MapContainer.getInstance().moveMapTo(currentLat / 100000.0d, currentLon / 100000.0d, 0, 0);
        }
    }

    
    protected void getMinMapWidthHeight()
    {
        mapContainerHeight = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT);
        if (mapContainerHeight < 0)
        {
            mapContainerHeight = SplashScreenJob.mapHeight;
        }

        miniMapHeight = mapContainerHeight - (int) (80 * ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDensity());

        miniMapWidth = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH);
        if (miniMapWidth < 0)
        {
            miniMapWidth = SplashScreenJob.mapWidth;
        }
       
        mapContainerHeightLandscape = (int) (DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND));
        if (mapContainerHeightLandscape < 0) 
        {
            mapContainerHeightLandscape = SplashScreenJob.mapHeightLandscape;
        }
        
        miniMapHeightLandscape = mapContainerHeightLandscape - (((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getStatusBarHeight(0)) - (int)(45 * ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDensity());

		miniMapWidthLandscape= DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND);
        if (miniMapHeightLandscape < 0) 
        {
            miniMapWidthLandscape = SplashScreenJob.mapWidthLandscape;
        }
        Logger.log(Logger.INFO, "DashboardManager", "Fake Map-------getMinMapWidthHeight: miniMapHeight = " + miniMapHeight + " ; miniMapWidth = " + miniMapWidth);
    }
    
    protected void startSnapping(final boolean isPortrait, final TnLocation location)
    {
        /*Thread t = new Thread(new Runnable()
        {
            public void run()
            {*/
                getMinMapWidthHeight();
                int tempZoomLevel = currentLocation == null ? MapConfig.MAP_WHOLE_US_ZOOM_LEVEL : DEFAULT_SNAPPED_ZOOMLEVEL;
                MapContainer.getInstance().setZoomLevel(tempZoomLevel);

                /*while (MapContainer.getInstance().getZoomLevel(true) != tempZoomLevel)
                {
                    synchronized (this)
                    {
                        try
                        {
                            this.wait(50);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }*/                                
                
                boolean currentOrientation = ((((AbstractTnUiHelper) (AbstractTnUiHelper.getInstance())).getOrientation()) == AbstractTnUiHelper.ORIENTATION_PORTRAIT);

                if (isPortrait != currentOrientation || isPaused)
                {
                    return;
                }
                
                int width = 0;
                int height = 0;

                if(isPortrait)
                {
                    width = miniMapWidth;
                    height = miniMapHeight;
                }
                else
                {
                    width = miniMapWidthLandscape;
                    height = miniMapHeightLandscape;
                }
                    
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, "DashboardManager",
                        "MiniMapProfile --> Fake Map--------before requestBitmapSnapShot:  width = " + width
                        + " ; height = " + height);
                }
                
                int snappingY = (AppConfigHelper.getDisplayHeight() - height) / 2;
                
                if (location != null)
                {
                    isGoodGpsForSnap = isGoodGpsLocation;
                }
                MapContainer.getInstance().requestOpenGLScreenShot(0, snappingY, width, height, DashboardManager.this, System.currentTimeMillis());
            /*}
        });
        t.start();*/
    }
    
    private void showTrafficLayer(boolean isTrafficEnable)
    {
        int trafficEnableLayerSetting;
        int trafficFeature = FeaturesManager.getInstance().getStatus(
            FeaturesManager.FEATURE_CODE_MAP_LAYER_TRAFFIC_FLOW);
        if ((trafficFeature == FeaturesManager.FE_ENABLED || trafficFeature == FeaturesManager.FE_PURCHASED)
                && isTrafficEnable)
        {
            trafficEnableLayerSetting = 0x01;
        }
        else
        {
            trafficEnableLayerSetting = 0x00;
        }
        MapContainer.getInstance().showMapLayer(trafficEnableLayerSetting, false);
        MapContainer.getInstance().updateDayNightStatus();
    }
    
    private boolean isPortrait()
    {
        return ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
    }
    
    class DualBufferImageManager
    {
        protected int[] portraitBuffer;
        protected int[] landscapeBuffer;
        
        protected short[] portraitShortBuffer;
        protected short[] landscapeShortBuffer;
        
        protected Bitmap[] dualBufferPortraitImages = new Bitmap[2];
        protected Bitmap[] dualBufferlandscapeImges = new Bitmap[2];
        
        Boolean portraitTag = false;
        Boolean landscapeTag = false;
        
        public int[] getBuffer(boolean isPortrait)
        {
            if (isPortrait)
            {
                if (dualBufferManager.portraitBuffer == null)
                {
                    int screenSize = miniMapWidth * miniMapHeight;
                    dualBufferManager.portraitBuffer = new int[screenSize];
                    dualBufferManager.portraitShortBuffer = new short[screenSize];
                }
                else
                {
                    Arrays.fill(dualBufferManager.portraitBuffer, 0);
                    Arrays.fill(dualBufferManager.portraitShortBuffer, (short)0);
                }
                return portraitBuffer;
            }
            else
            {
                if (dualBufferManager.landscapeBuffer == null)
                {
                    int screenSize = miniMapWidthLandscape * miniMapHeightLandscape;
                    dualBufferManager.landscapeBuffer = new int[screenSize];
                    dualBufferManager.landscapeShortBuffer = new short[screenSize];
                }
                else
                {
                    Arrays.fill(dualBufferManager.landscapeBuffer, 0);
                    Arrays.fill(dualBufferManager.landscapeShortBuffer, (short)0);
                }
                
                return landscapeBuffer;
            }
        }
        
        public Bitmap getMiniMap(boolean isPortrait)
        {
            if (isPortrait)
            {
                if (portraitBuffer == null)
                {
                    return null;
                }
                else
                {
                    int index = portraitTag ? 1 : 0;
                    Bitmap bitmap = dualBufferPortraitImages[index];
                    if (bitmap == null)
                    {
                        bitmap = Bitmap.createBitmap(miniMapWidth, miniMapHeight, Bitmap.Config.RGB_565);
                    }
                    portraitTag = !portraitTag;
                    return ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).createImage(bitmap, miniMapWidth, miniMapHeight, portraitBuffer);
                }
            }
            else
            {
                if (landscapeBuffer == null)
                {
                    return null;
                }
                else
                {
                    int index = landscapeTag ? 1 : 0;
                    Bitmap bitmap = dualBufferlandscapeImges[index];
                    if (bitmap == null)
                    {
                        bitmap = Bitmap.createBitmap(miniMapWidthLandscape, miniMapHeightLandscape, Bitmap.Config.RGB_565);
                    }
                    landscapeTag = !landscapeTag;
                    return ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).createImage(bitmap, miniMapWidthLandscape, miniMapHeightLandscape, landscapeBuffer);
                }
            }
        }

        
    }

}
