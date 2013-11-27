/**
 *
 * Copyright 2009 TeleNav, Inc. All rights reserved.
 * PluginDataProvider.java
 *
 */
package com.telenav.sdk.plugin;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.logger.Logger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.maitai.impl.MaiTaiParameter;
import com.telenav.util.StringTokenizer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2009-7-17
 *
 *Porting from PluginDataProvider.java & AndroidPluginDataProvider.java in tn62
 *@modifier qli (qli@telenav.cn)
 *@date 2010-12-12
 */
public class PluginDataProvider implements IMaiTai
{
    /**
     * Get a Route
     */
    public static final String ACTION_NAV_VALUE = "NAV";

    /**
     * The old "Get a route". This is only for backward compatible
     */
    public static final String ACTION_AC_VALUE = "AC";

    /**
     * Get static directions
     */
    public static final String ACTION_STATIC_NAV_VALUE = "STATIC_NAV";

    /**
     * Show Maps page
     */
    public static final String ACTION_MAP_VALUE = "MAP";
    
    /**
     * Show Maps of current location
     */
    public static final String ACTION_CURRENT_MAP_VALUE = "CURRENT_MAP";

    /**
     * Show Search page
     */
    public static final String ACTION_BIZ_VALUE = "BIZ";

    /**
     * Perform search for passed in keyword around passed in location
     */
    public static final String ACTION_BIZ_KEYWORD_VALUE = "BIZ_KEYWORD";

    /**
     * Show share and More page
     */
    public static final String ACTION_SHOW_SHAREMORE_VALUE = "SHARE_MORE";

    /**
     * Share Address
     */
    public static final String ACTION_SHAREADDRESS_VALUE = "SHA";

    /**
     * Get Directions
     */
    public static final String ACTION_GET_DIRECTIOSN_VALUE = "GetDirections";
    
    /**
     * Add Address to Favorites
     */
    public static final String ACTION_ADD_FAVORITE_VALUE = "FAV";

    /**
     * Show Weather of GPS location
     */
    public static final String ACTION_WEATHER_VALUE = "WEATHER";
    
    /**
     * Show Weather of current location
     */
    public static final String ACTION_CURRENT_WEATHER_VALUE = "CURRENT_WEATHER";
    
    public static final String ACTION_COMMUTE_ALERT_VALUE = "COMMUTE_ALERT";
    
    public static final String ACTION_MOVIES_VALUE = "MOVIES";
    
    public final static int KEY_CMD = 0;
    public final static int KEY_ADDRESS = 1;
    public final static int KEY_APARTMENT = 2;
    public final static int KEY_CITY = 3;
    public final static int KEY_ZIPCODE = 4;
    public final static int KEY_STATE = 5;
    public final static int KEY_LABEL = 6;
    public final static int KEY_COUNTRY = 7;
    public final static int KEY_LAT = 8;
    public final static int KEY_LON = 9;
    public final static int KEY_SMS_PAYLOAD = 10;
    public final static int KEY_CONVENIENCE_FLAG = 11;
    public final static int KEY_COMMUTE_ALERT_FLAG = 12;
    public final static int KEY_BIZ_KEYWORD = 13;
    public final static int KEY_FROM_APP = 14;
    public final static int KEY_BACK_ACTION = 15;
    
    public final static String BACK_ACTION_MAIN_SCREEN = "main_screen";
    public final static String BACK_ACTION_EXIT_APP = "exit_app";
    
    // Keys used by MaiTai to pass data to TN. Make sure they match the values defines in 
    // MaiTai Adapter
    private static final int KEY_DEVELOPER_KEY = 31;
    private static final int KEY_VERSION = 32;
    private static final int KEY_PROMPT = 34;
    private static final int KEY_CALLING_PID = 35;
    private static final int KEY_GUID = 36;
    private static final int KEY_SEARCH_TERM = 37;
    private static final int KEY_ONEBOX_ADDRESS = 41;
    private static final int KEY_ONEBOX_ADDRESS2 = 42; //For originalAddress
    // End MaiTai
    
    protected String selectedAction = null;
    protected String fromApp = null;
    protected String keyword = null;
    protected Stop[] stops;
    protected Hashtable pluginDatas = null;
    protected Stop stop;
    protected boolean isStartSayCommand = false;
    
    private static PluginDataProvider instance = new PluginDataProvider();
    
    private PluginDataProvider()
    {
        
    }
    
    public static PluginDataProvider getInstance()
    {
        return instance;
    }
    
    public static void init(PluginDataProvider provider)
    {
        instance = provider;
    }
    
    public void clear()
    {
        selectedAction = null;
        keyword = null;
        pluginDatas = null;
        stops = null;
        stop = null;
    }
    
    public void setParameter(Hashtable parameter)
    {
        stop = null;
        stops = null;
        selectedAction = null;
        keyword = null;
        this.pluginDatas = parameter;
        
        constructAddress();
    }
    
    public Stop[] getStop()
    {
        return stops;
    }
    
    public Stop getOrigStop()
    {
        return stop;
    }
    
//    public String getSelectedAction()
//    {
//        if (pluginDatas != null)
//            selectedAction = (String) pluginDatas.get("" + KEY_CMD);
//        return selectedAction;
//    }
    
    public String getBackAction()
    {
        String backAction = null;
        if (pluginDatas != null && pluginDatas.containsKey("" + KEY_BACK_ACTION))
            backAction = (String) pluginDatas.get("" + KEY_BACK_ACTION);

        if (this.isFromMaiTai())
        {
            return BACK_ACTION_EXIT_APP;
        }

        if (backAction == null || backAction.length() == 0)
        {
            return BACK_ACTION_MAIN_SCREEN;
        }

        return backAction;
    }
    
    /**
     * Returns application identifier from which TeleNav was invoked.
     * The following values come from MCD_TN_6 0_Logging v1c.doc
     * 1 (Using Convenience Key )
     * 2 (From Calendar)
     * 3 (From Email)
     * 4 (Commute Alert)
     * 5 (From address book)
     * 6 (Share Address)
     * 7 (Manual)
     * 8 (Other)
     */
    public String getFromApp()
    {
    	if (pluginDatas != null)
    		fromApp = (String) pluginDatas.get("" + KEY_FROM_APP);
    	return fromApp;
    }
    
    public void setFromApp(String fromApp){
    	this.fromApp = fromApp;
    }
    
    public String getKeyword()
    {
        if (pluginDatas != null)
            keyword = (String) pluginDatas.get("" + KEY_BIZ_KEYWORD);
        return keyword;
    }
    
    protected void constructAddressData()
    {
        if (isFromMaiTai())
        {
            //ParametersProxy parametersProxy = ParametersProxy.getInstance();
            // IStop stop = parametersProxy.getStopsFactory().createStop();
            this.stops = new Stop[1];
            String firstLine = (String)getOneBoxAddressVector().elementAt(0);
            if (isHome(firstLine))
            {
                this.stops[0] = DaoManager.getInstance().getAddressDao().getHomeAddress();
            }
            else if (isOffice(firstLine))
            {
                this.stops[0] = DaoManager.getInstance().getAddressDao().getOfficeAddress();
            }
            else
            {
                this.stops[0] = new Stop();
                this.stops[0].setFirstLine(firstLine);
                this.stops[0].setLabel(MaiTaiManager.getInstance().getMaiTaiParameter().getLabel());
                if (this.getLat() != null && this.getLon() != null)
                {
                    try{
                        this.stops[0].setLat(Integer.parseInt(this.getLat()[0]));
                        this.stops[0].setLon(Integer.parseInt(this.getLon()[0]));
                    }
                    catch (NumberFormatException e)
                    {
                        
                    }
                }
            }
            
            String originAddress = MaiTaiManager.getInstance().getMaiTaiParameter().getOrigAddress();
            if (isHome(originAddress))
            {
                this.stop = DaoManager.getInstance().getAddressDao().getHomeAddress();
            }
            else if(isOffice(originAddress))
            {
                this.stop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
            }
            else
            {
                this.stop = new Stop();
                this.stop.setFirstLine(originAddress);
            }
            return;
        }
        
        if(this.getLat() != null && this.getLon() != null )
        {
            this.stops = new Stop[this.getLat().length];
        }
        else if(this.getFirstLine() != null )
        {
            this.stops = new Stop[this.getFirstLine().length];
        }
        
        if(this.stops == null || this.stops.length == 0)
            return;
        
        for(int i = 0; i < this.stops.length; i++)
        {
            Stop stop = new Stop();
            this.stops[i] = stop;
        }
        
        if(this.getLabel() != null )
        {
            for(int i = 0; i < this.stops.length; i++)
            {
                if(this.getLabel()[i] != null)
                {
                    this.stops[i].setLabel(this.getLabel()[i].toUpperCase());
                }
            }
        }
        
        if(this.getFirstLine() != null )
        {
            for(int i = 0; i < this.stops.length; i++)
            {
                String firstLine = this.getFirstLine()[i];
                if(firstLine != null)
                {
                    if (isHome(firstLine))
                    {
                        this.stops[i] = DaoManager.getInstance().getAddressDao().getHomeAddress();
                        return;
                    }
                    else if (isOffice(firstLine))
                    {
                        this.stops[i] = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                        return;
                    }
                    else
                    {
                        this.stops[i].setFirstLine(firstLine.toUpperCase());
                    }
                }
            }
        }
        
        if(this.getCity() != null )
        {
            for(int i = 0; i < this.stops.length; i++)
            {
                if(this.getCity()[i] != null)
                {
                    this.stops[i].setCity(this.getCity()[i].toUpperCase());
                }
            }
        }
        
        if(this.getZipcode() != null )
        {
            for(int i = 0; i < this.stops.length; i++)
            {
                if(this.getZipcode()[i] != null)
                {
                    this.stops[i].setPostalCode(this.getZipcode()[i].toUpperCase());
                }
            }
        }
        
        if(this.getState() != null )
        {
            for(int i = 0; i < this.stops.length; i++)
            {
                if(this.getState()[i] != null)
                {
                    this.stops[i].setProvince(this.getState()[i].toUpperCase());
                }
            }
        }
        
        if(this.getCountry() != null )
        {
            for(int i = 0; i < this.stops.length; i++)
            {
                if(this.getCountry()[i] != null)
                {
                    this.stops[i].setCountry(this.getCountry()[i].toUpperCase());
                }
            }
        }
        
        if(this.getLat() != null && this.getLon() != null )
        {
            try
            {
                if(this.getLat() != null )
                {
                    for(int i = 0; i < this.stops.length; i++)
                    {
                        if(this.getLat()[i] != null && this.getLat()[i].length() > 0)
                        {
                            this.stops[i].setLat(Integer.parseInt(this.getLat()[i]));
                        }
                    }
                }
                
                if(this.getLon() != null )
                {
                    for(int i = 0; i < this.stops.length; i++)
                    {
                        if(this.getLon()[i] != null && this.getLon()[i].length() > 0)
                        {
                            this.stops[i].setLon(Integer.parseInt(this.getLon()[i]));
                            this.stops[i].setIsGeocoded(true);
                        }
                    }
                }
            }
            catch(Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
                
                for(int i = 0; i < this.stops.length; i++)
                {
                    this.stops[i].setLat(3737923);
                    this.stops[i].setLon(-12201068);
                    this.stops[i].setIsGeocoded(true);
                }
                
            }
        }
        
        for(int i = 0; i < this.stops.length; i++)
        {
            checkStop(this.stops[i]);
        }
    }
    
    protected void checkStop(Stop stop)
    {
        //TODO find country info from currentlocale, jansen check it in tn70
//        String inputCountry = null;
//        if (stop.getCountry() != null && stop.getCountry().trim().length() != 0) 
//        {
//            inputCountry = stop.getCountry().toUpperCase();
//        } 
//        else 
//        {
//            inputCountry = "";
//        }
        
        
//        String mappingCountryIso = null;
//        
//        String currentLocale = AbstractDaoManager.
//                                getInstance().
//                                getMandatoryNodeDao().
//                                getMandatoryProperty(
//                                    MandatoryNodeDao.MANDATORY_NODE_USER_INFO, 
//                                    MandatoryNodeDao.USER_INFO_LOCALE);
//        
//        if(currentLocale != null && currentLocale.length() != 0 )
//        {
//            Vector countryIsoVector = ParametersProxy.getInstance().getAcTemplateManager()
//                    .getCountryIsoList(currentLocale);
//            if(countryIsoVector != null)
//            {
//                for (int i = 0; i < countryIsoVector.size(); i++)
//                {
//                    String iso = (String) countryIsoVector.elementAt(i);
//                    IAcTemplate acTemplate = ParametersProxy.getInstance().getAcTemplateManager()
//                            .getAcTemplate(currentLocale, iso);
//                    if (inputCountry.equals(acTemplate.getCountryIsoCode()))
//                    {
//                        mappingCountryIso = acTemplate.getCountryIsoCode();
//                        break;
//                    }
//                    else if(inputCountry.equals(acTemplate.getCountryName().toUpperCase()))
//                    {
//                        mappingCountryIso = acTemplate.getCountryIsoCode();
//                        break;
//                    }
//                    if (mappingCountryIso != null && mappingCountryIso.length() != 0)
//                        break;
//                    Vector countryAliasVector = acTemplate.getCountryAlias();
//                    if (countryAliasVector != null)
//                    {
//                        for (int j = 0; j < countryAliasVector.size(); j++)
//                        {
//                            String countryAlias = (String) countryAliasVector
//                                    .elementAt(j);
//                            if (inputCountry.equals(countryAlias.toUpperCase()))
//                            {
//                                mappingCountryIso = acTemplate.getCountryIsoCode();
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        if(mappingCountryIso != null && mappingCountryIso.length() != 0)
//        {
//            stop.setCountry(mappingCountryIso);
//        }
    }
    
//    protected String[] getFirstLine()
//    {
//        String[] address = null;
//        if (pluginDatas != null)
//            address = (String[]) pluginDatas.get("" + KEY_ADDRESS);
//        return address;
//    }
    
    private String[] getCity()
    {
        String[] city = null;
        if (pluginDatas != null)
            city = (String[]) pluginDatas.get("" + KEY_CITY);
        return city;
    }
    
    private String[] getZipcode()
    {
        String[] zipcode = null;
        if (pluginDatas != null)
            zipcode = (String[]) pluginDatas.get("" + KEY_ZIPCODE);
        return zipcode;
    }
    
    private String[] getState()
    {
        String[] state = null;
        if (pluginDatas != null)
            state = (String[]) pluginDatas.get("" + KEY_STATE);
        return state;
    }
    
    protected String[] getLabel()
    {
        String[] label = null;
        if (pluginDatas != null)
            label = (String[]) pluginDatas.get("" + KEY_LABEL);
        return label;
    }
    
    private String[] getCountry()
    {
        String[] country = null;
        if (pluginDatas != null)
            country = (String[]) pluginDatas.get("" + KEY_COUNTRY);
        return country;
    }
    
    private String[] getLat()
    {
        String[] lat = null;
        if (pluginDatas != null)
            lat = (String[]) pluginDatas.get("" + KEY_LAT);
        return lat;
    }
    
    private String[] getLon()
    {
        String[] lon = null;
        if (pluginDatas != null)
            lon = (String[]) pluginDatas.get("" + KEY_LON);
        return lon;
    }
    
    public void setIsStartSayCommand(boolean isStartSayCommand)
    {
        this.isStartSayCommand = isStartSayCommand;
    }
    
    public boolean isStartSayCommand()
    {
        return isStartSayCommand;
    }
    
    public long getCallingGuid()
    {
        String guid = null;
        long longGuid = -1;
        if (pluginDatas != null) 
        {
            guid = (String)pluginDatas.get("" + KEY_GUID);
            try 
            {
                longGuid = Long.parseLong(guid);
            } catch (Exception ex) {}
        }
        return longGuid; 
    }

    public int getCallingPID()
    {
        String pid = null;
        int intPid = -1;
        if (pluginDatas != null) 
        {
            pid = (String)pluginDatas.get("" + KEY_CALLING_PID);
            try 
            {
                intPid = Integer.parseInt(pid);
            } catch (Exception ex) {}
        }
        return intPid;
    }

//    public String getDeveloperKey()
//    {
//        String devKey = null;
//        if (pluginDatas != null) 
//        {
//            devKey = (String)pluginDatas.get("" + KEY_DEVELOPER_KEY);
//        }
//        return devKey;
//    }

//    public String getOneBoxAddress()
//    {
//        String addr = null;
//        if (pluginDatas != null) 
//        {
//            addr = (String)pluginDatas.get("" + KEY_ONEBOX_ADDRESS);
//        }
//        return addr;
//    }
    
//    public Vector getOneBoxAddressVector()
//    {
//        return null;
//    }
    
//    public String getOriginalAddress()
//    {
//        String addr = null;
//        if (pluginDatas != null) 
//        {
//            addr = (String)pluginDatas.get("" + KEY_ONEBOX_ADDRESS2);
//        }
//        return addr;
//    }

//    public String getSearchTerm()
//    {
//        String addr = null;
//        if (pluginDatas != null) 
//        {
//            addr = (String)pluginDatas.get("" + KEY_SEARCH_TERM);
//        }
//        return addr;
//    }

//    public boolean isFromMaiTai()
//    {
//        if (getCallingGuid() == -1 || getCallingPID() == -1)
//        {
//            return false;
//        }
//        return true;
//    }

//    public boolean isMaiTaiNeedLogin()
//    {
//        return false;
//    }
    
    
    
    

    private Hashtable data = null;
    
    
    private boolean isVaildAddress(String address, int lat, int lon)
    {
        return address != null && address.length() > 0? true : (lat != 0 && lon != 0)? true : false;
    }
    
    protected void constructMenuItemAddress()
    {
        String[] firstLines = (String[])data.get(IMaiTai.KEY_ADDRESS_FIRST_LINE);
        String[] cities = (String[])data.get(IMaiTai.KEY_ADDRESS_CITY);
        String[] states = (String[])data.get(IMaiTai.KEY_ADDRESS_STATE);
        String[] zips = (String[])data.get(IMaiTai.KEY_ADDRESS_ZIP);
        String[] countries = (String[])data.get(IMaiTai.KEY_ADDRESS_COUNTRY);
        String[] lats = (String[])data.get(IMaiTai.KEY_ADDRESS_LAT);
        String[] lons = (String[])data.get(IMaiTai.KEY_ADDRESS_LON);
        String[] labels = (String[])data.get(IMaiTai.KEY_ADDRESS_LABEL);
//            String[] routeStyles = (String[])data.get("route_style");
        
        String firstLine = firstLines == null ? null : firstLines[0];
        String city = cities == null ? null : cities[0];
        String state = states == null ? null : states[0];
        String zip = zips == null ? null : zips[0];
        String country = countries == null ? null : countries[0];
        String lat = lats == null ? null : lats[0];
        String lon = lons == null ? null : lons[0];
        String label = labels == null ? null : labels[0];
        Stop dest = composeOneBoxAddress(firstLine, city, state, zip, country, lat, lon, label);
        
        this.stops = new Stop[1];
        this.stops[0] = dest;
    }
    
    private Stop composeOneBoxAddress(String firstLine, String city, String state, String zip, String country, String lat, String lon, String label)
    {
        String separator = ", ";
        
        StringBuffer sb = new StringBuffer();
        
        if (firstLine != null && firstLine.trim().length() > 0)
        {
            sb.append(firstLine);
        }
        
        if (city != null && city.trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(city);
        }
        
        if (state != null && state.trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(state);
        }
        
        if (zip != null && zip.trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(zip);
        }
        
        if (country != null && country.trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(country);
        }
        
        Stop stop = new Stop();
        stop.setFirstLine(sb.toString());
        
        try
        {
            stop.setLat(Integer.parseInt(lat));
            stop.setLon(Integer.parseInt(lon));
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        stop.setLabel(label);
        
        return stop;
    }

    protected void constructAddress()
    {
        if (isFromMaiTai())
        {
            //ParametersProxy parametersProxy = ParametersProxy.getInstance();
            // IStop stop = parametersProxy.getStopsFactory().createStop();
            String firstLine = null;
            int lat = 0;
            int lon = 0;
            MaiTaiParameter maitaiParameter = MaiTaiManager.getInstance().getMaiTaiParameter();
            
            if (IMaiTaiParameter.ACTION_DIRECTIONS.equals(maitaiParameter.getAction()))
            {
                firstLine = maitaiParameter.getDestAddress();
                lat = maitaiParameter.getLatDest();
                lon = maitaiParameter.getLonDest();
            }
            else
            {
                firstLine = maitaiParameter.getAddress();
                lat = maitaiParameter.getLat();
                lon = maitaiParameter.getLon();
            }
            
            Vector markersStops = maitaiParameter.getMarkersStops();
            if(this.isVaildAddress(firstLine, lat, lon))
            {
                this.stops = new Stop[1];
                
                if (isHome(firstLine))
                {
                    this.stops[0] = DaoManager.getInstance().getAddressDao()
                            .getHomeAddress();
                }
                else if (isOffice(firstLine))
                {
                    this.stops[0] = DaoManager.getInstance().getAddressDao()
                            .getOfficeAddress();
                }
                else
                {
                    Stop stop = new Stop();
                    stop.setFirstLine(firstLine);
                    if(lat != 0 && lon != 0)
                    {
                        stop.setLat(lat);
                        stop.setLon(lon);
                    }
                    stop.setLabel(maitaiParameter.getLabel()); 
                    if(maitaiParameter.isDestIsCurrentLocation())
                    {
                        stop.setType(Stop.STOP_CURRENT_LOCATION);
                    }
                    
                    this.stops[0] = stop;
                }
            }
            else if (IMaiTaiParameter.ACTION_MAP.equals(maitaiParameter.getAction())
                    && markersStops != null && markersStops.size() > 0 )
            {
                int size = markersStops.size();
                
                this.stops = new Stop[size];
                for (int i = 0; i < size; i ++)
                {
                    this.stops[i] = (Stop)markersStops.elementAt(i);
                }
            }
            else if (IMaiTaiParameter.ACTION_SEARCH.equals(maitaiParameter.getAction()))
            {        
                if (this.stops == null)
                {
                    this.stops = new Stop[1];
                    this.stops[0] = new Stop();
                }
            }
            
            this.stop = new Stop();
            if(maitaiParameter.isOriIsCurrentLocation())
            {
                stop.setType(Stop.STOP_CURRENT_LOCATION);
            }
            
            if (IMaiTaiParameter.ACTION_DIRECTIONS.equals(maitaiParameter.getAction()))
            {
                String originAddress = maitaiParameter.getOrigAddress();
                if(isHome(originAddress))
                {
                    this.stop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                }
                else if(isOffice(originAddress))
                {
                    this.stop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                }
                else
                {
                    this.stop.setFirstLine(originAddress);
                    if (maitaiParameter.getLatOrig() != 0 && maitaiParameter.getLonOrig() != 0)
                    {
                        try
                        {
                            this.stop.setLat(maitaiParameter.getLatOrig());
                            this.stop.setLon(maitaiParameter.getLonOrig());
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
            }
            else
            {
                String address = maitaiParameter.getAddress();
                if(isHome(address))
                {
                    this.stop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                }
                else if(isOffice(address))
                {
                    this.stop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                }
                else
                {
                    this.stop.setFirstLine(address);
                    if (maitaiParameter.getLat() != 0 && maitaiParameter.getLon() != 0)
                    {
                        try
                        {
                            this.stop.setLat(maitaiParameter.getLat());
                            this.stop.setLon(maitaiParameter.getLon());
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
            }
            return;
        }
        else
        {
            String[] firstLine = this.getFirstLine();

            int[] latLon;
            this.stops = new Stop[1];
            this.stops[0] = new Stop();
            if (firstLine != null && firstLine.length == 2)
            {
                latLon = parseLatLon(firstLine[1]);
                if(latLon == null)
                {
                    this.stops[0].setFirstLine(firstLine[1].toUpperCase());
                }
                else
                {
                    this.stops[0].setLat(latLon[0]);
                    this.stops[0].setLon(latLon[1]);
                }
                this.stop = new Stop();
                stop.setFirstLine(firstLine[0].toUpperCase());
            }
            else if(firstLine != null && firstLine.length == 1 && parseLatLon(firstLine[0]) != null)
            {
                if(isHome(firstLine[0]))
                {
                    this.stops[0] = DaoManager.getInstance().getAddressDao().getHomeAddress();
                }
                else if(isOffice(firstLine[0]))
                {
                    this.stops[0] = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                }
                else
                {
                    latLon = parseLatLon(firstLine[0]);
                    this.stops[0].setLat(latLon[0]);
                    this.stops[0].setLon(latLon[1]);
                }
            }
            else
            {
                constructAddressData();
            }
        }
    }
    
    public String getSelectedAction()
    {
        if(isFromMaiTai())
        {
            return MaiTaiManager.getInstance().getMaiTaiParameter().getAction();
        }
        else if(data != null)
        {
            Object obj = data.get(KEY_SELECTED_MENU_ITEM);
            if (obj instanceof String)
            {
                return (String) obj;
            }
        }
        if (pluginDatas != null)
            selectedAction = (String) pluginDatas.get("" + KEY_CMD);
        
        return selectedAction;
    }
    
    
    public void initialize(Hashtable data)
    {
        if (data != null)
        {
            this.data = data;
            Object itemValue = data.get(KEY_SELECTED_MENU_ITEM);//"selected_menu_item"
            if (itemValue instanceof String)
            {
                //this is the logic compatible with AT&T maps 2.0
                //see bug 1234
                String str = (String)itemValue;
                if ("AC".equalsIgnoreCase(str))
                {
                    constructMenuItemAddress();
                    return;
                }
            }
            constructAddress();
        }
        else
        {
            this.data  = new Hashtable();
        }
    }
    
    //for rim, not available for android.
//    public long getCallingGuid()
//    {
//        return -1;
//    }

    //for rim, not available for android.
//    public int getCallingPID()
//    {
//        return -1;
//    }

    public Vector getOneBoxAddressVector()
    {
        Vector address = new Vector();
        if(isFromMaiTai())
        {
            address.addElement(MaiTaiManager.getInstance().getMaiTaiParameter().getAddress());
        }
        else if(data != null)
        {
//            Object obj = data.get(KEY_ONE_BOX_ADDRESS);
//            if (obj instanceof ArrayList)
//            {
//                Logger.log(Logger.INFO, this.toString(), "%%%%%%%%%%%%%%%%% KEY_ONE_BOX_ADDRESS instanceof ArrayList");
//                ArrayList list = (ArrayList)obj;
//                Vector address = new Vector();
//                for (int i = 0; i < list.size(); i++)
//                {
//                    address.add(list.get(i));
//                }
//                return address;
//            }
        }
        
        return address;
    }

    protected String[] getFirstLine()
    {
        Vector address = getOneBoxAddressVector();
        String[] addresses = null;
        if (pluginDatas != null)
            addresses = (String[]) pluginDatas.get("" + KEY_ADDRESS);
        if(addresses == null && address != null)
        {
            addresses = new String[address.size()];
            for (int i = 0; i < address.size(); i++)
            {
                addresses[i] = (String)address.elementAt(i);
            }
        }
        return addresses;
    }
    
//    public String getOriginalAddress()
//    {
//        if(isFromMaiTai())
//        {
//            return MaiTaiParameter.getInstance().getOrigAddress();
//        }
//        else
//        {
//            return null;
//        }
//    }

    public String getSearchTerm()
    {
        if(isFromMaiTai())
        {
            return MaiTaiManager.getInstance().getMaiTaiParameter().getSearchTerm();
        }
        else
        {
            String addr = null;
            if (data != null)
            {
                addr = (String) data.get(KEY_SEARCH_ITEM);
            }
            return addr;
        }
    }
    
    public String getSearchCat()
    {
        if(isFromMaiTai())
        {
            return MaiTaiManager.getInstance().getMaiTaiParameter().getSearchCat();
        }
        else
        {
            return "";
        }
    }

    public boolean isFromMaiTai()
    {
        if(data == null)
        {
            return false;
        }
        
        Object obj = this.data.get(KEY_IS_MAITAI_CALL);
        if (obj != null && "true".equalsIgnoreCase((String) obj))
        {
            return true;
        }
        
        return false;
    }

    public boolean isMaiTaiNeedLogin()
    {
        if(data == null)
            return false;
        
        Object obj = this.data.get(KEY_MAITAI_NEED_LOGIN);
        if (obj != null && "true".equalsIgnoreCase((String) obj))
        {
            return true;
        }
        
        return false;
    }
    
    public boolean isCommuteAlert()
    {
        if (data == null)
            return false;

        Object obj = this.data.get(KEY_IS_COMMUTE_ALERT);
        if (obj != null && "true".equalsIgnoreCase((String) obj))
        {
            return true;
        }

        return false;
    }
    
    public String getSMS()
    {
        if (data == null)
            return "";
        return (String) this.data.get(KEY_SMS_MESSAGE);
    }
    
    public void cleanupCommuteAlert()
    {
        if (data != null)
        {
            data.remove(KEY_IS_COMMUTE_ALERT);
            data.remove(KEY_SMS_MESSAGE);
        }
    }
    
    public int[] parseLatLon(String address)
    {
        String[] string = getSplit(address, ",");
        if(string.length != 2)
        {
            return null;
        }
        
        try
        {
            int[] result = new int[2];
            result[0] = Integer.parseInt(string[0].trim());
            result[1] = Integer.parseInt(string[1].trim());
            return result;
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return null;
    }

    private String[] getSplit(String source, String token)
    {
        StringTokenizer tokenizer = new StringTokenizer(source, token);
        Vector vector = new Vector();
        while( tokenizer.hasMoreTokens() )
        {
            vector.addElement(tokenizer.nextToken());
        }
        String[] address = new String[vector.size()];
        for( int i=0 ; i<vector.size() ; i++ )
        {
            address[i] = (String)vector.elementAt(i);
        }
        return  address;
    }
    
    private boolean isHome(String firstLine)
    {
        if (MaiTaiParameter.KEYWORD_HOME.equals(firstLine))
        {
            return true;
        }
        return false;
    }
    
    private boolean isOffice(String firstLine)
    {
        if (MaiTaiParameter.KEYWORD_OFFICE.equals(firstLine))
        {
            return true;
        }
        return false;
    }
}
