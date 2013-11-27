/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PluginModel.java
 *
 */
package com.telenav.sdk.plugin.module;

import java.util.Hashtable;
import java.util.Vector;

import android.location.Location;

import com.telenav.app.CommManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.datatypes.DataUtil;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.entry.AbstractCommonEntryModel;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.plugin.PluginAddress;
import com.telenav.sdk.plugin.PluginManager;

/**
 *@author qli
 *@date 2011-2-24
 */
public class PluginModel extends AbstractCommonEntryModel implements IPluginConstants, PoiDataListener
{
    public static final int MAX_NUM_PER_PAGE = 10;

    private Object mutex = new Object();
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_CHECK_REGION_DETECTION_STATUS:
            {
                checkRegionDetectStatus();
                break;
            }
            case ACTION_CHECK_REGION_CHANGE:
            {
                checkRegionChange();
                break;
            }
            case ACTION_INIT:
            {
                doInit();
                if(this.getString(KEY_S_PLUGIN_ACTION) != null && this.getString(KEY_S_PLUGIN_ACTION).length() > 0)
                {
                    Address address = (Address) get(KEY_O_VALIDATED_ADDRESS);
                    if ( address != null )
                    {
                        Stop stop = address.getStop();
                        if (stop != null && stop.getFirstLine() != null && stop.getFirstLine().length() > 0)
                        {
                            this.postModelEvent(EVENT_MODEL_DO_ONEBOX_RGC);
                        }
                        else
                        {
                            boolean needRGC = stop != null && stop.isValid() && Stop.isEmpty(stop.getFirstLine());
                            if (needRGC)
                            {
                                this.put(KEY_O_PLUGIN_RGC_ADDRESS, address);
                                this.postModelEvent(EVENT_MODEL_RGC_ADDRESS);
                            }
                            else
                                this.postModelEvent(EVENT_MODEL_DONE_PLUGIN);
                        }
                    }
                    else
                    {
                        this.postModelEvent(EVENT_MODEL_START_PLUGIN);
                    }
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_INIT_PLUGIN);
                }
                break;
            }
            case ACTION_START_PLUGIN:
            {
                preparePluginData();
                break;
            }
            case ACTION_GOTO_MODULE:
            {
                boolean validate_directions = this.fetchBool(KEY_B_VALIDATE_TWO_ADDRESSES);
                boolean directions_orig = this.fetchBool(KEY_B_VALIDATE_DIRECTIONS_ORIG);
                
                Object addr = this.fetch(KEY_O_VALIDATED_ADDRESS);
                if( addr != null && addr instanceof Address )
                {
                    Address address = (Address)addr;
                    if( validate_directions )
                    {
                        this.put(KEY_O_ADDRESS_ORI, address);
                        PluginAddress destAddr = (PluginAddress)this.fetch(KEY_S_VALIDATE_DIRECTIONS_DEST);
                        this.put(KEY_S_VALIDATE_ADDRESS, destAddr.getAddressLine());
                        this.put(KEY_S_VALIDATE_COUNTRY, destAddr.getCountry());
                        this.postModelEvent(EVENT_MODEL_VALIDATE_ADDRESS);
                    }
                    else
                    {
                        if( directions_orig )
                        {
                            this.put(KEY_O_ADDRESS_ORI, address);
                        }
                        else if(IMaiTai.MENU_ITEM_DIRECTIONS.equals((String) get(KEY_S_PLUGIN_ACTION)))
                        {
                            this.put(KEY_O_ADDRESS_DEST, address);
                        }
                        else
                        {
                            this.put(KEY_O_SELECTED_ADDRESS, address);
                        }
                        this.postModelEvent(EVENT_MODEL_HANDLE_PLUGIN);
                    }
                    break;
                }
                this.removeAll();
                PluginManager.getInstance().finish();
                break;
            }
            case ACTION_JUMP_BACKGROUND:
            {
                doCancel();
                break;
            }
            case ACTION_RGC_ADDRESS:
            {
                Address address = (Address) get(KEY_O_PLUGIN_RGC_ADDRESS);
                if(address != null && address.getStop() != null)
                {
                    int lat = address.getStop().getLat();
                    int lon = address.getStop().getLon();
                    doRGC(lat, lon);
                }
                break;
            }
            case ACTION_DO_ONEBOX_RGC:
            {
                Object addr = this.get(KEY_O_VALIDATED_ADDRESS);
                if( addr instanceof Address )
                {
                    Stop stop = ((Address)addr).getStop();
                    String addressLine = stop.getFirstLine();
                    int lat = stop.getLat();
                    int lon = stop.getLon();
                    
                    put(KEY_B_IS_ONEBOX_RGC, true);
                    doOneBoxSearch(addressLine, stop);
                    doRGC(lat, lon);
                }
                break;
            }
            case ACTION_CHECK_ONE_BOX_RETURN:
            {
                put(KEY_O_VALIDATED_ADDRESS, fetch(KEY_O_SELECTED_ADDRESS));
                postModelEvent(EVENT_MODEL_START_PLUGIN);
                break;
            }
        }
        super.doActionDelegate(actionId);
    }
    
    protected void doOneBoxSearch(String addressLine, Stop anchorStop)
    {
        PoiDataWrapper poiDataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
        poiDataWrapper.setSearchArgs(0, IPoiSearchProxy.TYPE_SEARCH_ADDRESS, IPoiSearchProxy.TYPE_SEARCH_FROM_TYPEIN, -1, IPoiSearchProxy.TYPE_SORT_BY_DISTANCE, PoiDataRequester.TYPE_NO_CATEGORY_ID, 0, MAX_NUM_PER_PAGE, addressLine, addressLine, anchorStop, null, null, -1, 0);
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        PoiDataRequester poiDataRequester = new PoiDataRequester(userProfileProvider);
        poiDataRequester.doRequestPoi(poiDataWrapper, this);
    }
    
    protected void doRGC(int lat, int lon)
    {
        IRgcProxy proxy = (IRgcProxy) ServerProxyFactory.getInstance().createRgcProxy(null, CommManager.getInstance().getComm(), this);
        proxy.requestRgc(lat, lon);
    }
    
    protected void doInit()
    {
        String action = "";
        Vector addresses = null;
        Stop stop = null;
        String searchItem = "";
        Object request = this.fetch(KEY_O_PLUGIN_REQUEST);
        if( request instanceof Hashtable )
        {
            Hashtable param = ((Hashtable)request);
            try
            {
                action = (String)param.get(IMaiTai.KEY_SELECTED_MENU_ITEM);
            }catch( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            try
            {
                addresses = (Vector)param.get(IMaiTai.KEY_ONE_BOX_ADDRESS);
            }catch( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            try
            {
                Object obj = param.get(IMaiTai.KEY_SELECTED_ADDRESS);
                if ( obj instanceof Stop[] )
                {
                    Stop[] stops = (Stop[])obj;
                    stop = stops[0];
                }
                else if(obj instanceof Stop)
                {
                    stop = (Stop) obj;
                }
            }catch( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            try
            {
                searchItem = (String)param.get(IMaiTai.KEY_SEARCH_ITEM);
            }catch( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            this.put(KEY_S_PLUGIN_ACTION, action);
            this.put(KEY_S_COMMON_SEARCH_TEXT, searchItem);
            if ( stop != null )
            {
                Address address = new Address();
                address.setStop(stop);
                this.put(KEY_O_VALIDATED_ADDRESS, address);
            }
            else
            {
                this.put(KEY_O_PLUGIN_ADDRESS, addresses);
            }
        }
    }
    
    protected void activateDelegate(boolean isUpdateView)
    {
        boolean isHandle = this.fetchBool(KEY_B_PLUGIN_GOTO_MODULE);
        if( isHandle && isUpdateView)
        {
            this.removeAll();
            PluginManager.getInstance().finish();
            return;
        }
    }
    
    protected void doCancel()
    {
        this.put(KEY_B_PLUGIN_GOTO_MODULE, true);
    }
    
    protected void preparePluginData()
    {
        String action = this.getString(KEY_S_PLUGIN_ACTION);
        boolean validate_address = false;
        String addressLine = null;
        String country = null;
        boolean needRrgcAaddress = false;
        boolean useOneBox = false;
        if( IMaiTai.MENU_ITEM_DRIVE_TO.equals(action) )
        {
            try
            {
                Vector addr = this.fetchVector(KEY_O_PLUGIN_ADDRESS);
                PluginAddress pluginAddr = (PluginAddress) addr.elementAt(0);
                addressLine = pluginAddr.getAddressLine();
                country = pluginAddr.getCountry();
                useOneBox = pluginAddr.useOneBox();
                
                Address address = null;
                address = parseLatLon(addressLine);
                if ( address == null || (address != null && address.getStop() != null && (address.getStop().getLat() == 0 || address.getStop().getLon() == 0)))
                {
                    validate_address = true;
                }
                else
                {
                    Stop stop = address.getStop();
                    if(stop != null && stop.getFirstLine() != null && stop.getFirstLine().trim().length() > 0)
                    {
                        this.put(KEY_O_SELECTED_ADDRESS, address);
                    }
                    else
                    {
                        this.put(KEY_O_PLUGIN_RGC_ADDRESS, address);
                        needRrgcAaddress = true;
                    }
                }
            }
            catch(Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
            
        }
        else if( IMaiTai.MENU_ITEM_DIRECTIONS.equals(action) )
        {
            try
            {
                Address address = null;
                Vector addr = this.fetchVector(KEY_O_PLUGIN_ADDRESS);
                PluginAddress pluginAddr = (PluginAddress) addr.elementAt(0);
                addressLine = pluginAddr.getAddressLine();
                country = pluginAddr.getCountry();
                address = parseLatLon(addressLine);
                if ( address != null && address.getStop() != null && address.getStop().getLat() != 0 && address.getStop().getLon() != 0)
                {
                    
                    this.put(KEY_O_ADDRESS_ORI, address);
                }
                else
                {
                    this.put(KEY_B_VALIDATE_DIRECTIONS_ORIG, true);
                    validate_address = true;
                }
                
                PluginAddress destAddr = (PluginAddress) addr.elementAt(1);
                String daddr = destAddr.getAddressLine();
                address = parseLatLon(daddr);
                if ( address != null && address.getStop() != null && address.getStop().getLat() != 0 && address.getStop().getLon() != 0)
                {
                    this.put(KEY_O_ADDRESS_DEST, address);
                }
                else
                {
                    if ( validate_address )
                    {
                        this.put(KEY_B_VALIDATE_TWO_ADDRESSES, true);
                    }
                    else
                    {
                        addressLine = daddr;
                        country = destAddr.getCountry();
                    }
                    this.put(KEY_S_VALIDATE_DIRECTIONS_DEST, destAddr);
                    validate_address = true;
                }
            }
            catch(Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        else if( IMaiTai.MENU_ITEM_SHARE_ADDRESS.equals(action) )
        {
            try
            {
                Vector addr = this.fetchVector(KEY_O_PLUGIN_ADDRESS);
                PluginAddress pluginAddr = (PluginAddress)addr.elementAt(0);
                addressLine = pluginAddr.getAddressLine();
                country = pluginAddr.getCountry();
                Address address = null;
                address = parseLatLon(addressLine);
                if ( address == null || (address != null && address.getStop() != null && (address.getStop().getLat() == 0 || address.getStop().getLon() == 0)) )
                {
                    validate_address = true;
                }
                else
                {
                    Stop stop = address.getStop();
                    if(stop != null && stop.getFirstLine() != null && stop.getFirstLine().trim().length() > 0)
                    {
                        this.put(KEY_O_SELECTED_ADDRESS, address);
                    }
                    else
                    {
                        this.put(KEY_O_PLUGIN_RGC_ADDRESS, address);
                        needRrgcAaddress = true;
                    }
                }
            }
            catch(Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        else if( IMaiTai.MENU_ITEM_MAP_VALUE.equals(action) )
        {
            try
            {
                Vector addr = this.fetchVector(KEY_O_PLUGIN_ADDRESS);
                PluginAddress pluginAddr = (PluginAddress)addr.elementAt(0);
                addressLine = pluginAddr.getAddressLine();
                country = pluginAddr.getCountry();
                if(addressLine == null)
                {
                    addressLine = setLocation();
                }
                Address address = null;
                address = parseLatLon(addressLine);
                if ( address == null  || (address != null && address.getStop() != null && (address.getStop().getLat() == 0 || address.getStop().getLon() == 0)))
                {
                    validate_address = true;
                }
                else
                {
                    Stop stop = address.getStop();
                    if(stop != null && stop.getFirstLine() != null && stop.getFirstLine().trim().length() > 0)
                    {
                        this.put(KEY_O_SELECTED_ADDRESS, address);
                    }
                    else
                    {
                        this.put(KEY_O_PLUGIN_RGC_ADDRESS, address);
                        needRrgcAaddress = true;
                    }
                }
            }
            catch(Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        else if( IMaiTai.MENU_ITEM_BIZ_VALUE.equals(action) )
        {
            try
            {
                Vector addr = this.fetchVector(KEY_O_PLUGIN_ADDRESS);
                PluginAddress pluginAddr = (PluginAddress)addr.elementAt(0);
                addressLine = pluginAddr.getAddressLine();
                country = pluginAddr.getCountry();
                Address address = null;
                address = parseLatLon(addressLine);
                if ( address == null  || (address != null && address.getStop() != null && (address.getStop().getLat() == 0 || address.getStop().getLon() == 0)))
                {
                    validate_address = true;
                }
                else
                {
                    Stop stop = address.getStop();
                    if(stop != null && stop.getFirstLine() != null && stop.getFirstLine().trim().length() > 0)
                    {
                        this.put(KEY_O_SELECTED_ADDRESS, address);
                    }
                    else
                    {
                        this.put(KEY_O_PLUGIN_RGC_ADDRESS, address);
                        needRrgcAaddress = true;
                    }
                }
            }
            catch(Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        if( validate_address )
        {
            if( addressLine != null )
            {
                this.put(KEY_S_VALIDATE_ADDRESS, addressLine);
                this.put(KEY_S_VALIDATE_COUNTRY, country);
            }
            
            if(useOneBox)
            {
                this.postModelEvent(EVENT_MODEL_DO_ONEBOX_SEARCH);
            }
            else
            {
                this.postModelEvent(EVENT_MODEL_VALIDATE_ADDRESS);
            }
        }
        else
        {
            if(needRrgcAaddress)
            {
                this.postModelEvent(EVENT_MODEL_RGC_ADDRESS);
            }
            else
            {
                this.postModelEvent(EVENT_MODEL_HANDLE_PLUGIN);
            }
        }
    }
    
    protected Address parseLatLon(String addressLine)
    {
        int index = -1;
        index = addressLine.indexOf(",");
        if(index == -1)
        {
            index = addressLine.indexOf("+");
        }
        if(index == -1)
        {
            index = addressLine.indexOf(" ");
        }
        if ( index >= 0 )
        {
            String strLat = addressLine.substring(0, index);
            int nIndex = -1;
            nIndex = strLat.toLowerCase().indexOf("n");
            if(nIndex != -1)
            {
                strLat = strLat.substring(0, nIndex);
            }
            
            nIndex = strLat.toLowerCase().indexOf("s");
            if(nIndex != -1)
            {
                strLat = "-" + strLat.substring(0, nIndex);
            }
            
            String strLon = addressLine.substring(index + 1);
            int wIndex = -1;
            wIndex = strLon.toLowerCase().indexOf("w");
            if(wIndex != -1)
            {
                strLon = "-" + strLon.substring(0, wIndex);
            }
            
            wIndex = strLon.toLowerCase().indexOf("e");
            if(wIndex != -1)
            {
                strLon = strLon.substring(0, wIndex);
            }
            
            if(strLat.indexOf(" ") != -1 || strLon.indexOf(" ") != -1)
            {
                double convertedLat = 0;
                double convertedLon = 0;
                if(strLat.indexOf(" ") != -1)
                {
                    convertedLat = covertDegrees(strLat);
                }
                if(strLon.indexOf(" ") != -1)
                {
                    convertedLon = covertDegrees(strLon);
                }
                strLat = String.valueOf(convertedLat);
                strLon = String.valueOf(convertedLon);
            }
            // fix bug http://jira.telenav.com:8080/browse/TNANDROID-5233
            // as the address line format is:(36.150604,-95.990759)
            int parenleft = strLat.indexOf("(");
            int parenRight = strLon.indexOf(")");
            if (parenleft > -1 && parenRight > -1)
            {
                strLat = strLat.substring(parenleft+1);
                strLon = strLon.substring(0, parenRight);
            }            
            try
            {
                int lat = Integer.parseInt(strLat.trim());
                int lon = Integer.parseInt(strLon.trim());
                Stop saddr = new Stop();
                saddr.setLat(lat);
                saddr.setLon(lon);
                Address address = new Address();
                address.setStop(saddr);
                return address;
            }
            catch(NumberFormatException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            try
            {
                double lat = Location.convert(strLat);
                double lon = Location.convert(strLon);
                
                Stop saddr = new Stop();
                saddr.setLat((int) (lat * 100000));
                saddr.setLon((int) (lon * 100000));
                Address address = new Address();
                address.setStop(saddr);
                return address;
            }
            catch ( Exception e )
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        return null;
    }
    
    protected boolean isValidAddress(String address, int lat, int lon)
    {
        return address != null && address.length() > 0? true : (lat != 0 && lon != 0)? true : false;
    }
    
    private double covertDegrees(String str)
    {
        double convertedLat = 0;
        try
        {
            int dotIndex = str.indexOf(".");
            String tempStr = str.substring(0, dotIndex);
            int tempIndex = -1;
            boolean isPositive = true;
            tempIndex = tempStr.indexOf("-");
            if(tempIndex != -1)
            {
                isPositive = false;
                tempStr = tempStr.substring(tempIndex + 1);
            }
            
            String[] splitStrLon = tempStr.split(" ");
            if(splitStrLon != null && splitStrLon.length >= 1)
            {
                int[] splitLon = new int[splitStrLon.length];
                
                for(int i = 0; i < splitStrLon.length; i++)
                {
                    splitLon[i] = Integer.parseInt(splitStrLon[i]);
                    convertedLat += (splitLon[i] * 1.0d /(Math.pow(60, i)) );
                }
            }
            String dotStr = str.substring(dotIndex + 1);
            convertedLat += Integer.parseInt(dotStr) * 1.0d /(10*3600);
            
            if(!isPositive)
            {
                convertedLat = -convertedLat;
            }
        }
        catch ( Exception e )
        {
            Logger.log(this.getClass().getName(), e);
        }
        return convertedLat;
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if(proxy instanceof IRgcProxy)
        {
            Address address = ((IRgcProxy) proxy).getAddress();
            address.setSource(Address.SOURCE_API);
            
            if(getBool(KEY_B_IS_ONEBOX_RGC))
            {
                synchronized(mutex)
                {
                    put(KEY_O_PLUGIN_RGC_ADDRESS, address);
                    put(KEY_B_IS_RGC_FINISHED, true);
                    
                    if(getBool(KEY_B_IS_ONEBOX_FAILED))
                    {
                        checkRgcAddress();
                    }
                }
            }
            else
            {
                put(KEY_O_SELECTED_ADDRESS, address);
                this.postModelEvent(EVENT_MODEL_HANDLE_PLUGIN);
            }
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        if (proxy instanceof IRgcProxy)
        {
            if(getBool(KEY_B_IS_ONEBOX_RGC))
            {
                synchronized(mutex)
                {
                    put(KEY_B_IS_RGC_FINISHED, true);
                    
                    if(getBool(KEY_B_IS_ONEBOX_FAILED))
                    {
                        checkRgcAddress();
                    }
                }
            }
            else
            {
                Address address = (Address) get(KEY_O_PLUGIN_RGC_ADDRESS);
                put(KEY_O_SELECTED_ADDRESS, address);
                this.postModelEvent(EVENT_MODEL_HANDLE_PLUGIN);
            }
        }
        else
        {
            super.transactionError(proxy);
        }
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if (proxy instanceof IRgcProxy)
        {
            if(getBool(KEY_B_IS_ONEBOX_RGC))
            {
                synchronized(mutex)
                {
                    put(KEY_B_IS_RGC_FINISHED, true);
                    
                    if(getBool(KEY_B_IS_ONEBOX_FAILED))
                    {
                        checkRgcAddress();
                    }
                }
            }
        }
        else
        {
            super.networkError(proxy, statusCode, jobId);
        }
    }
    private String setLocation()
    {
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
        if(location == null)
        {
            location = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        }
        if (location == null)
        {
            location = LocationProvider.getInstance().getDefaultLocation();
        } 
        int lat1 = location.getLatitude();
        int lon1 = location.getLongitude();
        return lat1 + "," + lon1;
    }

    public void poiResultUpdate(int status, int resultType, String msg,
            PoiDataWrapper poiDataWrapper)
    {
        boolean isSuccess = false;
        switch (status)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            case PoiDataRequester.TYPE_BAD_ARGS:
            {
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS:
            {
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                switch (resultType)
                {
                    case PoiDataRequester.TYPE_RESULT_SUGGESTION_DID_U_MEAN:
                    {
                        Stop stop = ((Address)this.get(KEY_O_VALIDATED_ADDRESS)).getStop();
                        Vector suggestions = poiDataWrapper.getMultiMatchResults();
                        OneBoxSearchBean suggestion = (OneBoxSearchBean)suggestions.elementAt(0);
                        doOneBoxSearch(suggestion.getContent(), stop);
                        return;
                    }
                    case PoiDataRequester.TYPE_RESULT_ADDRESS:
                    {
                        int size = poiDataWrapper.getAddressSize();
                        if (size > 0)
                        {
                            for(int i = 0; i < size; i++)
                            {
                                Address addr = poiDataWrapper.getAddress(i);
                                if (addr != null)
                                {
                                    switch (addr.getType())
                                    {
                                        case Address.TYPE_RECENT_STOP:
                                        {
                                            continue;
                                        }
                                        case Address.TYPE_RECENT_POI:
                                        {
                                            if(isOneBoxRgcAddressOk(addr))
                                            {
                                                //oneBoxRgcAddressGot(addr);
                                                poiDataWrapper.setSelectedIndex(i);
                                                oneBoxRgcAddressGotPOI(poiDataWrapper);
                                                isSuccess = true;
                                            }
                                            break;
                                        }
                                    }
                                    
                                    if(isSuccess)
                                    {
                                        break;
                                    }
                                }//end if
                            }//end for
                        }//end if
                        
                        break;

                    }
                    default:
                    {
                        break;
                    }
                }

                break;
            }
        }
        
        if (!isSuccess)
        {
            synchronized(mutex)
            {
                put(KEY_B_IS_ONEBOX_FAILED, true);
                checkRgcAddress();
            }
        }
    }
    
    protected void checkRgcAddress()
    {
        if(getBool(KEY_B_IS_RGC_FINISHED))
        {
            Address rgcAddress = (Address) get(KEY_O_PLUGIN_RGC_ADDRESS);
            if(rgcAddress != null)
            {
                oneBoxRgcAddressGot(rgcAddress);
            }
            else
            {
                postModelEvent(EVENT_MODEL_DONE_PLUGIN);
            }
        }
    }
    
    protected void oneBoxRgcAddressGot(Address address)
    {
        put(KEY_O_SELECTED_ADDRESS, address);
        postModelEvent(EVENT_MODEL_HANDLE_PLUGIN);
    }
    
    
    protected void oneBoxRgcAddressGotPOI(PoiDataWrapper poiDataWrapper)
    {
        put(KEY_O_SELECTED_ADDRESS, poiDataWrapper);
        postModelEvent(EVENT_MODEL_HANDLE_PLUGIN);
    }
    
    protected boolean isOneBoxRgcAddressOk(Address address)
    {
        Stop stop = ((Address)get(KEY_O_VALIDATED_ADDRESS)).getStop();
        
        String keyWord = stop.getFirstLine();
        
        String brand = "";
        
        boolean isSameBrand = false;
        
        if(address != null && address.getPoi() != null && address.getPoi().getBizPoi() != null)
        {
            brand = address.getPoi().getBizPoi().getBrand();
            
            if(brand != null && brand.trim().length() > 0)
            {
                if(brand.equalsIgnoreCase(keyWord) || brand.startsWith(keyWord) || keyWord.startsWith(brand))
                {
                    isSameBrand = true;
                }
            }
        }
        
        if(isSameBrand)
        {
            Stop returnedStop = address.getStop();
            // calculate the actual distance in meters
            //long distance = Math.abs(cur.lat - last.lat) + DataUtil.xCosY(Math.abs(cur.lon - last.lon), ((21 * last.lat) >> 21));
            // [DW]: Calculate the distance between two fixes using the RSS approach.
            long distance = 0;
            try
            {
                 distance = DataUtil.gpsDistance(stop.getLat() - returnedStop.getLat(),
                                                 stop.getLon() - returnedStop.getLon(),
                                                 DataUtil.getCosLat(stop.getLat())); 
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            if(distance < 500)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
}
