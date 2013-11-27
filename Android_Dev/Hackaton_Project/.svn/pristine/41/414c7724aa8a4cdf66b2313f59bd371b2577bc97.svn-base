/**
 * 
 */
package com.telenav.sdk.maitai.impl;

import java.util.Vector;

import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.location.LocationProvider;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.maitai.IMaiTaiStatusConstants;
import com.telenav.util.StringTokenizer;


/**
 * 
 *@author Casper (pwang@telenav.cn)
 *@date 2009-11-16
 *@modifier qli  2010-12-01
 * porting from MaiTaiParameter.java in tn6.2
 */
public class MaiTaiParameter implements IMaiTaiParameter
{
    private static final String KEY_ACTION            = "action";
    
    private static final String KEY_VERSION           = "v";
    private static final String KEY_CONTEXT           = "c";
    private static final String KEY_DEVELOPER_KEY     = "k";
    private static final String KEY_ADDR              = "addr";
    private static final String KEY_CB                = "cb";
    private static final String KEY_LABEL             = "lbl";
    private static final String KEY_ADDRESS_ID        = "addrId";
    private static final String KEY_PROMPT            = "prompt";
    private static final String KEY_ADDR1             = "addr1";
    private static final String KEY_ADDR2             = "addr2";
    private static final String KEY_LAT               = "lat";
    private static final String KEY_LON               = "lon";
    private static final String KEY_LOCATION_ERR_SIZE = "err";
    private static final String KEY_TERM              = "term";
    private static final String KEY_CAT               = "cat";
    
    private static final String KEY_LAT1               = "lat1";
    private static final String KEY_LON1               = "lon1";    
    private static final String KEY_LAT2               = "lat2";
    private static final String KEY_LON2               = "lon2";    
    
    //below are for MaiTai 2.0
    private static final String KEY_NAMEDADDR          = "namedAddr";
    private static final String KEY_NAMEDADDR1         = "namedAddr1";
    private static final String KEY_NAMEDADDR2         = "namedAddr2";
    private static final String KEY_COORD              = "coord";
    private static final String KEY_COORD1             = "coord1";
    private static final String KEY_COORD2             = "coord2";
    private static final String KEY_NAV_TYPE           = "type";
    private static final String KEY_MARKERS            = "markers";
    private static final String KEYWORD_ADDRESS_TYPE   = "addressType";
    
    private static final String KEY_AUTHORITY          = "authority";
    private static final String KEY_PATH           	   = "path";
    private static final String KEY_QUERY              = "query";
    
    private static final String KEYWORD_CURRENT        = "CURRENT";
    public static final String KEYWORD_HOME           = "HOME";
    public static final String KEYWORD_OFFICE           = "OFFICE";
     
    private static final String KEY_WIDGET_TYPE   = "widgettype";
    
    private static MaiTaiParameter instance = null;
    
    private MaiTaiAuthenticate authenticator;
    private MaiTaiHandler maiTaiHandler;
    private MaiTaiUtil util;
    
    private String callback;
    private String context;
    private String action;
    private String address;
    private String origAddress;
    private String destAddress;
    private int lat;
    private int lon;    
    private String addrId;
    
    private int lat1;
    private int lon1;    
    private int lat2;
    private int lon2;
    
    private String term;
    private String label;
    private String cat;
    private int locationErrorSize;
    private boolean needPrompt;
    
    private Vector markersStops;
    
    private String strUrl;
    
    private String authority;
    private String path;
    private String query;
    private boolean isHomeAddress;
    private String widgetType;
    
    public String getWidgetType()
    {
        return widgetType;
    }


    protected MaiTaiParameter()
    {
        maiTaiHandler = MaiTaiHandler.getInstance();
        authenticator = new MaiTaiAuthenticate();
    }

    
    protected void notify(String requestUri)
    {
       
        this.strUrl = requestUri;
        util = MaiTaiUtil.getInstance();
        Vector list = util.parseRequestUri(requestUri);

        if (!validateParams(list) || !parseParams(list))
        {
            maiTaiHandler.cancel();
            return;
        }
        
        if (this.getContext() != null)
            maiTaiHandler.addResult(KEY_CONTEXT, this.getContext());
    }
    
    
    private boolean validateParams(Vector list)
    {
        String KEYWORD_V10 = "1.0";
        String KEYWORD_V20 = "2.0";
        String KEYWORD_V21 = "2.1";
        
        String version = util.getValue(list, KEY_VERSION);
        if (!KEYWORD_V10.equals(version) && !KEYWORD_V20.equals(version) && !KEYWORD_V21.equals(version))
        {
            maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_VERSION_UNSUPPORTED);
            return false;
        }
        
        String[] ACTIONS = new String[]
        {
                ACTION_NAVTO,
                ACTION_DIRECTIONS, 
                ACTION_MAP,
                ACTION_SEARCH,     
                ACTION_DRIVETO,
                ACTION_VIEW,
                ACTION_SET_ADDRESS
        };
        
        boolean bFind = false;
        String action = util.getValue(list, KEY_ACTION);
        for (int i = 0; i < ACTIONS.length; i ++)
        {
            if (ACTIONS[i].equalsIgnoreCase(action))
            {
                bFind = true;
                break;
            }
        }
        
        if (!bFind)
        {
            maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_BAD_REQUEST);
            return false;
        }

        
        if(MaiTaiHandler.getInstance().isNeedValidate())
        {
            String devKey = util.getValue(list, KEY_DEVELOPER_KEY);
            
            if (devKey == null || !authenticator.validate(devKey))
            {
                int status = authenticator.getLastError();
                maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + status);
                if (authenticator.getErrorMessage() != null)
                    maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, authenticator.getErrorMessage());
                else
                    maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "");
                return false;
            }
        }
        return true;
    }
    
    private boolean parseNavToParams(Vector list)
    {
        this.address = util.getValue(list, KEY_ADDR);
        parseLatAndLon(list);
        if ((address == null || address.length() == 0) && (lat == 0 && lon == 0))
        {
            maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_BAD_REQUEST);
            return false;
        }
        this.label = util.getValue(list, KEY_LABEL);
        String prompt = util.getValue(list, KEY_PROMPT);
        this.needPrompt = true;
        if ("no".equalsIgnoreCase(prompt))
            this.needPrompt = false;

        return true;
    }
    
    private boolean parSetAddressParams(Vector list)
    {
        String addressType = util.getValue(list, KEYWORD_ADDRESS_TYPE);
        this.isHomeAddress = KEYWORD_HOME.equalsIgnoreCase(addressType);
        return true;
    }
    
    private boolean parseDirectionsParams(Vector list)
    {
        this.origAddress = util.getValue(list, KEY_ADDR1);
        this.destAddress = util.getValue(list, KEY_ADDR2);
        parseLatAndLonOrig(list);
        parseLatAndLonDest(list);
        if ((this.destAddress == null || this.destAddress.length() == 0) && (lat2 == 0 && lon2 == 0))
        {
            maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_BAD_REQUEST);
            return false;
        }
        if ((this.origAddress == null || this.origAddress.length() == 0) && (lat1 == 0 && lon1 == 0))
        {
            TnLocation currentGps = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
            lat1 = currentGps.getLatitude();
            lon1 = currentGps.getLongitude();
            isOriIsCurrent = true;
        }
        this.label = util.getValue(list, KEY_LABEL);

        return true;
    }
    
    private boolean isOriIsCurrent = false;
    private boolean isDestIsCurrent = false;
    
    private boolean parseDriveToParams(Vector list)
    {
        String KEYWORD_DIR = "DIR";
        
        String namedAddr1 = util.getValue(list, KEY_NAMEDADDR1);
        String namedAddr2 = util.getValue(list, KEY_NAMEDADDR2);
        Stop homeStop = null;
        if (KEYWORD_HOME.equalsIgnoreCase(namedAddr1) || KEYWORD_HOME.equalsIgnoreCase(namedAddr2))
        {
            AddressDao stopWrapper = AbstractDaoManager.getInstance().getAddressDao();
            if (stopWrapper != null)
            {
                homeStop = stopWrapper.getHomeAddress();
            }
            if (homeStop == null)
            {
                maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_PROCESS_ERROR);
                maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "HOME address is not specified");
                return false;
            }
        }
        Stop officeStop = null;
        if (KEYWORD_OFFICE.equalsIgnoreCase(namedAddr1) || KEYWORD_OFFICE.equalsIgnoreCase(namedAddr2))
        {
            AddressDao stopWrapper = AbstractDaoManager.getInstance().getAddressDao();
            if (stopWrapper != null)
            {
                officeStop = stopWrapper.getOfficeAddress();
            }
            if (officeStop == null)
            {
                maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_PROCESS_ERROR);
                maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "office address is not specified");
                return false;
            }
        }
        
        TnLocation currentGps = null;
        
        isOriIsCurrent = KEYWORD_CURRENT.equalsIgnoreCase(namedAddr1);
        isDestIsCurrent = KEYWORD_CURRENT.equalsIgnoreCase(namedAddr2);
        
        if ( isOriIsCurrent || isDestIsCurrent)
        {
            currentGps = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
        }
        
        if (currentGps == null || (currentGps.getLatitude() == 0 && currentGps.getLongitude() == 0))
        {
            currentGps = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
        }
        
        boolean isDynamicRoute = true;
        String navType = util.getValue(list, KEY_NAV_TYPE);
        if (KEYWORD_DIR.equalsIgnoreCase(navType))
        {
            isDynamicRoute = false;
        }
        
        if (isDynamicRoute)
        {
            this.action = ACTION_NAVTO;
            
            //process destination
            if (KEYWORD_CURRENT.equalsIgnoreCase(namedAddr2) && currentGps != null)
            {
                this.lat = currentGps.getLatitude();
                this.lon = currentGps.getLongitude();
            }
            if (KEYWORD_HOME.equalsIgnoreCase(namedAddr2) && homeStop != null)
            {
                this.lat = homeStop.getLat();
                this.lon = homeStop.getLon();
                this.address = KEYWORD_HOME;
            }
            else if (KEYWORD_OFFICE.equalsIgnoreCase(namedAddr2) && officeStop != null)
            {
                this.lat = officeStop.getLat();
                this.lon = officeStop.getLon();
                this.address = KEYWORD_OFFICE;
            }
            if (lat == 0 || lon == 0)
            {
                this.address = util.getValue(list, KEY_ADDR2);
                if (this.address == null)
                {
                    String coord2 = util.getValue(list, KEY_COORD2);
                    int[] coord = parseCoordinate(coord2);
                    this.lat = coord[0];
                    this.lon = coord[1];
                    if (this.lat == 0 || this.lon == 0)
                    {
                        maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_PROCESS_ERROR);
                        maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "Please specify destination");
                        return false;
                    }
                }
            }            
        }
        else
        {
            this.action = ACTION_DIRECTIONS;
            
            //process destination
            if (KEYWORD_CURRENT.equalsIgnoreCase(namedAddr2) && currentGps != null)
            {
                this.lat2 = currentGps.getLatitude();
                this.lon2 = currentGps.getLongitude();
            }
            if (KEYWORD_HOME.equalsIgnoreCase(namedAddr2) && homeStop != null)
            {
                this.lat2 = homeStop.getLat();
                this.lon2 = homeStop.getLon();
                this.destAddress = KEYWORD_HOME;
            }
            else if (KEYWORD_OFFICE.equalsIgnoreCase(namedAddr2) && officeStop != null)
            {
                this.lat = officeStop.getLat();
                this.lon = officeStop.getLon();
                this.address = KEYWORD_OFFICE;
            }
            if (lat2 == 0 || lon2 == 0)
            {
                this.destAddress = util.getValue(list, KEY_ADDR2);
                if (this.destAddress == null)
                {
                    String coord2 = util.getValue(list, KEY_COORD2);
                    int[] coord = parseCoordinate(coord2);
                    this.lat2 = coord[0];
                    this.lon2 = coord[1];
                    if (this.lat2 == 0 || this.lon2 == 0)
                    {
                        maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_PROCESS_ERROR);
                        maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "Please specify destination");
                        return false;                        
                    }
                }
            }
            
            //process original address
            if (KEYWORD_CURRENT.equalsIgnoreCase(namedAddr1) && currentGps != null)
            {
                this.lat1 = currentGps.getLatitude();
                this.lon1 = currentGps.getLongitude();
            }
            if (KEYWORD_HOME.equalsIgnoreCase(namedAddr1) && homeStop != null)
            {
                this.lat1 = homeStop.getLat();
                this.lon1 = homeStop.getLon();
                this.origAddress = KEYWORD_HOME;
            }
            else if (KEYWORD_OFFICE.equalsIgnoreCase(namedAddr1) && officeStop != null)
            {
                this.lat1 = officeStop.getLat();
                this.lon1 = officeStop.getLon();
                this.origAddress = KEYWORD_OFFICE;
            }
            if (lat1 == 0 || lon1 == 0)
            {
                this.origAddress = util.getValue(list, KEY_ADDR1);
                if (this.origAddress == null)
                {
                    String coord1 = util.getValue(list, KEY_COORD1);
                    int[] coord = parseCoordinate(coord1);
                    this.lat1 = coord[0];
                    this.lon1 = coord[1];
                    if (this.lat1 == 0 || this.lon1 == 0)
                    {
                        if (!KEYWORD_CURRENT.equalsIgnoreCase(namedAddr1))
                        {
                            maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_PROCESS_ERROR);
                            maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "Please specify original address");
                            return false;                        
                        }
                    }
                }
            }
            
        }
        
        this.label = util.getValue(list, KEY_LABEL);

        return true;
    }
    
    private Stop createStop(String label, Stop from)
    {
        Stop stop = new Stop();
        
        stop.setLabel(label);
        stop.setLat(from.getLat());
        stop.setLon(from.getLon());
        stop.setFirstLine(from.getFirstLine());
        stop.setCity(from.getCity());
        stop.setProvince(from.getProvince());
        stop.setCountry(from.getCountry());
        stop.setPostalCode(from.getPostalCode());

        return stop;
    }
    
    private Stop createStop(String label, int lat, int lon)
    {
        Stop stop = new Stop();
        stop.setLabel(label);
        stop.setLat(lat);
        stop.setLon(lon);

        return stop;
    }
    
    private Vector parseMarkers(String markers)
    {
        Vector v = new Vector();
        
        if (markers == null)
            return v;
        
        String label = "";
        int index;
        StringTokenizer st = new StringTokenizer(markers, "|");
        while (st.hasMoreTokens())
        {
            String marker = st.nextToken().trim();
            if (marker.toLowerCase().startsWith("label:"))
            {
                index = marker.indexOf(":");
                label = marker.substring(index + 1);
            }
            if (KEYWORD_CURRENT.equalsIgnoreCase(marker))
            {
                TnLocation currentGps = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
                
                if (currentGps != null)
                {
                    v.addElement(createStop(label, currentGps.getLatitude(), currentGps.getLongitude()));
                }
                else
                {
                    continue;
                }
                
            }
            if (KEYWORD_HOME.equalsIgnoreCase(marker))
            {
                AddressDao stopWrapper = AbstractDaoManager.getInstance().getAddressDao();
                Stop homeStop = null;
                if (stopWrapper != null)
                {
                    homeStop = stopWrapper.getHomeAddress();
                }
                if (homeStop == null)
                {
                    String msg = "HOME address is not specified";
                    maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_PROCESS_ERROR);
                    maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, msg);
                }
                else
                {
                    v.addElement(createStop(label, homeStop));
                }
                
            }
            
            int[] coord = parseCoordinate(marker);
            if (coord[0] != 0 && coord[1] != 0)
            {
                v.addElement(createStop(label, coord[0], coord[1]));
            }
        }
        
        return v;
    }
    
    private boolean parseMapsParams(Vector list)
    {
        this.address = util.getValue(list, KEY_ADDR);
        String namedAddr = util.getValue(list, KEY_NAMEDADDR);
        //parse current location
        if (KEYWORD_CURRENT.equalsIgnoreCase(namedAddr))
        {
            // fix http://jira.telenav.com:8080/browse/TNANDROID-1913 -->comments from PM: Do not show POI bubble when launching from widget. 
//            TnLocation loc = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
//            if (loc == null)
//            {
//                loc = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
//            }
//            if (loc != null)
//            {
//                this.lat = loc.getLatitude();
//                this.lon = loc.getLongitude();
//                return true;
//            }
            return true;
        }
        parseLatAndLon(list);
        
        //parse MaiTai 2.0 feature
        if (this.lat == 0 || this.lon == 0)
        {
            Vector markers = util.getValues(list, KEY_MARKERS);
            
            for (int i = 0; i < markers.size(); i ++)
            {
                Vector v = parseMarkers((String)markers.elementAt(i));
                
                int size = v.size();
                for (int j = 0; j < size; j ++)
                {
                    this.markersStops.addElement(v.elementAt(j));
                }
            }
            if (markers.size() > 0 && this.markersStops.size() == 0)
            {
                maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_BAD_REQUEST);
                maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "Invalid parameter, please check your input");
                return false;                
            }
        }
        
        String err = util.getValue(list, KEY_LOCATION_ERR_SIZE);
        
        try
        {
            if (err != null)
                this.locationErrorSize = Integer.parseInt(err);
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        this.term = util.getValue(list, KEY_TERM);
        this.label = util.getValue(list, KEY_LABEL);       
        this.addrId = util.getValue(list, KEY_ADDRESS_ID);
        
        return true;
    }
    
    private boolean parseSearchParams(Vector list)
    {
        this.address = util.getValue(list, KEY_ADDR);
        
        parseLatAndLon(list);
        
        //parse MaiTai 2.0 feature
        if (this.lat == 0 || this.lon == 0)
        {
            String namedAddr = util.getValue(list, KEY_NAMEDADDR);
            if (KEYWORD_CURRENT.equalsIgnoreCase(namedAddr))
            {
                //if use current, do not specify the location
                TnLocation currentGps = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
                if (currentGps != null)
                {
                    this.lat = currentGps.getLatitude();
                    this.lon = currentGps.getLongitude();
                }
                isOriIsCurrent = true;
            }
            
            if (KEYWORD_HOME.equalsIgnoreCase(namedAddr))
            {
                AddressDao stopWrapper = AbstractDaoManager.getInstance().getAddressDao();
                Stop homeStop = null;
                if (stopWrapper != null)
                {
                    homeStop = stopWrapper.getHomeAddress();
                }
                if (homeStop == null)
                {
                    maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS, "" + IMaiTaiStatusConstants.STATUS_PROCESS_ERROR);
                    maiTaiHandler.addResult(IMaiTaiParameter.KEY_STATUS_MSG, "HOME address is not specified");
                    return false;
                }
                else
                {
                    this.lat = homeStop.getLat();
                    this.lon = homeStop.getLon();
                    this.address = KEYWORD_HOME;
                }
            }
            if (this.lat == 0 || this.lon == 0)
            {
                this.address = util.getValue(list, KEY_ADDR);
                if (this.address == null || this.address.trim().length() == 0)
                {
                    String strCoord = util.getValue(list, KEY_COORD);
                    int[] coord = parseCoordinate(strCoord);
                    this.lat = coord[0];
                    this.lon = coord[1];
                }
            }
        }
        
        String err = util.getValue(list, KEY_LOCATION_ERR_SIZE);
        
        try
        {
            if (err != null)
                this.locationErrorSize = Integer.parseInt(err);
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        this.term = util.getValue(list, KEY_TERM);
        this.cat = util.getValue(list, KEY_CAT);
        
        return true;
    }
    
    private boolean parseViewParams(Vector list)
    {    	
    	this.authority = util.getValue(list, KEY_AUTHORITY);
    	this.path = util.getValue(list, KEY_PATH);
    	this.query = util.getValue(list, KEY_QUERY);
    	
    	return true;    	
    }
    
    
    private boolean parseParams(Vector list)
    {
        this.markersStops = new Vector();
        
        this.action = util.getValue(list, KEY_ACTION);
        this.callback =  util.getValue(list, KEY_CB);
        this.context =  util.getValue(list, KEY_CONTEXT);
        this.widgetType = util.getValue(list, KEY_WIDGET_TYPE);    
        
        if (ACTION_NAVTO.equalsIgnoreCase(action))
        {
            return parseNavToParams(list);
        }
        else if (ACTION_DIRECTIONS.equalsIgnoreCase(action))
        {
            return parseDirectionsParams(list);            
        }
        else if (ACTION_DRIVETO.equalsIgnoreCase(action))
        {
            return parseDriveToParams(list);
        }
        else if (ACTION_MAP.equalsIgnoreCase(action))
        {
            return parseMapsParams(list);
        }
        else if (ACTION_SEARCH.equalsIgnoreCase(action))
        {
            return parseSearchParams(list);            
        }
        else if (ACTION_VIEW.equalsIgnoreCase(action))
        {
            return parseViewParams(list);            
        }
        else if (ACTION_SET_ADDRESS.equalsIgnoreCase(action))
        {
            return parSetAddressParams(list);
        }
        
        return true;
    }
    
    private int[] parseCoordinate(String strCoord)
    {
        int[] coord = new int[2];
        
        int index = strCoord == null ? -1 : strCoord.indexOf(",");
        if (index == -1)
            return coord;
        
        double dLat = 0, dLon = 0;        
        String strLat = strCoord.substring(0, index);
        String strLon = strCoord.substring(index + 1);
        
        try
        {
            if (strLat != null)
                dLat = Double.parseDouble(strLat);
            if (strLon != null)
                dLon = Double.parseDouble(strLon);
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }        

        if(dLat != 0 )
        {
            coord[0] = (int)(dLat * 100000);
        }
        
        if(dLon != 0)
        {
            coord[1] = (int)(dLon * 100000);
        }
        
        return coord;
    }

    private void parseLatAndLon(Vector list)
    {
        double dLat = 0, dLon = 0;
        String strLat = util.getValue(list, KEY_LAT);
        String strLon = util.getValue(list, KEY_LON);
        
        try
        {
            if (strLat != null)
                dLat = Double.parseDouble(strLat);
            if (strLon != null)
                dLon = Double.parseDouble(strLon);
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        
        if(dLat != 0 )
        {
            this.lat = (int)(dLat * 100000);
        }
        
        if(dLon != 0)
        {
            this.lon = (int)(dLon * 100000);
        }
        
//        if(this.lat == 0 && this.lon == 0)
//        {
//            String strCoord = util.getValue(list, KEY_COORD);
//            int[] coord = parseCoordinate(strCoord);
//            this.lat = coord[0];
//            this.lon = coord[1];
//        }
        
    }
    
    private void parseLatAndLonOrig(Vector list)
    {
        double dLat = 0, dLon = 0;
        String strLat = util.getValue(list, KEY_LAT1);
        String strLon = util.getValue(list, KEY_LON1);
        
        try
        {
            if (strLat != null)
                dLat = Double.parseDouble(strLat);
            if (strLon != null)
                dLon = Double.parseDouble(strLon);
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }        

        if(dLat != 0 )
        {
            this.lat1 = (int)(dLat * 100000);
        }
        
        if(dLon != 0)
        {
            this.lon1 = (int)(dLon * 100000);
        }
    }
    
    private void parseLatAndLonDest(Vector list)
    {
        double dLat = 0, dLon = 0;
        String strLat = util.getValue(list, KEY_LAT2);
        String strLon = util.getValue(list, KEY_LON2);
        
        try
        {
            if (strLat != null)
                dLat = Double.parseDouble(strLat);
            if (strLon != null)
                dLon = Double.parseDouble(strLon);
        }
        catch(NumberFormatException e)
        {
            Logger.log(this.getClass().getName(), e);
        }        

        if(dLat != 0 )
        {
            this.lat2 = (int)(dLat * 100000);
        }
        
        if(dLon != 0)
        {
            this.lon2 = (int)(dLon * 100000);
        }
    }

    public int getLocationErrorSize()
    {
        return this.locationErrorSize;
    }

    public String getSearchTerm()
    {
        return this.term;
    }
    
    public String getSearchCat()
    {
        return this.cat;
    }

    public boolean isHomeAddress()
    {
        return isHomeAddress;
    }
    
    public String getAction()
    {
        return this.action;
    }
    public String getCallback()
    {
        return this.callback;
    }
    public String getContext()
    {
        return this.context;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String getDestAddress()
    {
        return this.destAddress;
    }
    
    public boolean isOriIsCurrentLocation()
    {
        return this.isOriIsCurrent;
    }
    
    public boolean isDestIsCurrentLocation()
    {
        return this.isDestIsCurrent;
    }

    public String getLabel()
    {
        return this.label;
    }
    
    public String getUrl()
    {
        return this.strUrl;
    }

    public int getLat()
    {
        return this.lat;
    }

    public int getLon()
    {
        return this.lon;
    }
    
    public String getAddrId()
    {
        return this.addrId;
    }
    
    public int getLatOrig()
    {
        return this.lat1;
    }
    
    public int getLonOrig()
    {
        return this.lon1;
    }
    
    public int getLatDest()
    {
        return this.lat2;
    }

    public int getLonDest()
    {
        return this.lon2;
    }

    public String getOrigAddress()
    {
        return this.origAddress;
    }

    public Vector getMarkersStops()
    {
        return this.markersStops;
    }
    
    public boolean needPrompt()
    {
        return this.needPrompt;
    }

    private String composeStopAddress(Stop stop)
    {
        if (stop == null) return "";
        
        String separator = ", ";
        
        StringBuffer sb = new StringBuffer();
        
        String firstLine = stop.getFirstLine();
        if (firstLine != null && firstLine.trim().length() > 0)
        {
            sb.append(firstLine);
        }
        
        if (stop.getCity() != null && stop.getCity().trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(stop.getCity());
        }
        
        if (stop.getProvince() != null && stop.getProvince().trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(stop.getProvince());
        }
        
        if (stop.getPostalCode() != null && stop.getPostalCode().trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(stop.getPostalCode());
        }
        
        if (stop.getCountry() != null && stop.getCountry().trim().length() > 0)
        {
            if (sb.length() > 0)
                sb.append(separator);
            sb.append(stop.getCountry());
        }
        
        return sb.toString();
    }

    public String getAuthority()
    {
    	return this.authority;
    }
    
    public String getPath()
    {
    	return this.path;
    }
    
    public String getQuery()
    {
    	return this.query;
    }
}

