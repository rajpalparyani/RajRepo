/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AcModel.java
 *
 */
package com.telenav.module.ac;

import com.telenav.app.CommManager;
import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IRgcProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.region.RegionDetector;
import com.telenav.module.region.RegionResSwitcher;
import com.telenav.module.region.RegionUtil;
import com.telenav.res.IStringAc;
import com.telenav.res.ResourceManager;
import com.telenav.ui.citizen.map.MapContainer;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-8-17
 */
class AcModel extends BrowserSdkModel implements IAcConstants, LocationListener, IApplicationListener , RegionResSwitcher.IRegionResSwitcherListener, RegionDetector.IRegionDetectorListener, INetworkStatusListener
{
    protected void init()
    {
        /**
         * for lat/lon used by Adjuggler url parameter
         * if no gps, default lat/lon = 0
         */
        this.put(KEY_I_CURRENT_LOCATION_LAT, 0);
        this.put(KEY_I_CURRENT_LOCATION_LON, 0);
        
        NetworkStatusManager.getInstance().addStatusListener(this);
    }
    
    public void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        if(proxy instanceof IRgcProxy)
        {
            Address address = (((IRgcProxy)proxy).getAddress());
            put(KEY_O_SELECTED_ADDRESS, address);
            postModelEvent(EVENT_MODEL_RGC_FINISHED);
        }
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case IAcConstants.ACTION_INIT:
            {
                init();
                this.postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_CHECK_HOME_EXIST:
            {
                AddressDao addressDao = DaoManager.getInstance().getAddressDao();
                Stop stop = addressDao.getHomeAddress();
                if ( isValidAddress(stop) )
                    postModelEvent(EVENT_MODEL_RETURN_HOME);
                else
                    postModelEvent(EVENT_MODEL_SET_HOME);
                break;
            }
            case ACTION_CHECK_CURRENT_LOCATION:
            {
                TnLocation currentLocation = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, this.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_FAV ? true : false);
                if (currentLocation == null)
                {
                    postModelEvent(EVENT_MODEL_GETTING_CURRENT_LOCATION);
                }
                else
                {
                    checkLocation(currentLocation);
                }
                break;
            }
            case ACTION_GET_CURRENT_LOCATION:
            {
                LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME, LocationProvider.NETWORK_LOCATION_VALID_TIME, 10 * 1000, 
                    this, LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1, this.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_FAV ? true : false);
                break;
            }
            case ACTION_CHECK_WORK_EXIST:
            {
                AddressDao addressDao = DaoManager.getInstance().getAddressDao();
                Stop stop = addressDao.getOfficeAddress();
                if ( isValidAddress(stop) )
                    postModelEvent(EVENT_MODEL_RETURN_WORK);
                else
                    postModelEvent(EVENT_MODEL_SET_WORK);
                break;
            }
            case ACTION_RGC:
            {
                IRgcProxy proxy = (IRgcProxy) ServerProxyFactory.getInstance().createRgcProxy(null, CommManager.getInstance().getComm(), this);
                TnLocation currentLocation = (TnLocation) get(IAcConstants.KEY_O_CURRENT_LOCATION);
                if(currentLocation != null)
                {
                    proxy.requestRgc(currentLocation.getLatitude(), currentLocation.getLongitude());
                }
                break;
            }
            case ACTION_BACK_CHECK:
            {
	            int acType = getInt(KEY_I_AC_TYPE);
	            if (acType == TYPE_AC_FROM_ENTRY || acType == -1)
	            {
	                TeleNavDelegate.getInstance().registerApplicationListener(this);
	            	TeleNavDelegate.getInstance().jumpToBackground();
	            }
//	            else if(acType == TYPE_AC_FROM_DASHBOARD)
//	            {
//	                this.postModelEvent(EVENT_MODEL_BACK_TO_DASHBOARD);
//	            }
	            else
                {
	                boolean useRegionAnchor = isInternationalAddressCapture() && getBool(KEY_B_IS_REGION_SWITCHED_DIRECTLY);
                    if (useRegionAnchor)
                    {
                        TnLocation tnLocation = AbstractDaoManager.getInstance()
                                .getResourceBarDao().getRegionAnchor(this.getRegion());

                        Stop stop = new Stop();
                        stop.setLat(tnLocation.getLatitude());
                        stop.setLon(tnLocation.getLongitude());
                        stop.setType(Stop.STOP_ANCHOR);
                        Address address = new Address();
                        address.setStop(stop);

                        put(KEY_O_SELECTED_ADDRESS, address);

                        this.postModelEvent(EVENT_MODEL_RETURN_ADDRESS);
                    }
                    else
                    {
                        this.postModelEvent(EVENT_MODEL_COMMON_BACK);
                    }
                }
	            break;
            }
            case ACTION_CHECK_REGION_CHANGE:
            {
                String selectedRegion = getString(KEY_S_CURRENT_REGION);
                boolean isRegionChanged = isNewRegionDetected(selectedRegion);
                if(isRegionChanged)
                {
                    RegionResSwitcher switcher = new RegionResSwitcher(this);
                    put(KEY_I_SWITCH_REGION_TYPE, SWITCH_REGION_TYPE_SELECT_REGION_FROM_LIST);
                    int type = switcher.switchRegion(selectedRegion);
                    if (type == RegionResSwitcher.SUCCESS_FROM_SERVER)
                    {
                        postModelEvent(EVENT_MODEL_SWITCHING_REGION);
                    }
                    else
                    {
                        regionSwitchFinished(RegionUtil.getInstance().isNewRegion(selectedRegion));
                    }
                }
                else
                {
                    postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                    remove(KEY_S_CURRENT_REGION);
                }
                break;
            }
            case ACTION_CHECK_RETURNED_ADDRESS_REGION:
            {
                boolean isNeedCheckRegion = isInternationalAddressCapture();
                if (isNeedCheckRegion)
                {
                    put(KEY_B_IS_REGION_SWITCHED_DIRECTLY, false);
                    Address selectedAddress = (Address) get(KEY_O_SELECTED_ADDRESS);

                    boolean isSwitchRegion = false;
                    if (selectedAddress != null)
                    {
                        Stop stop = selectedAddress.getStop();
                        if (stop != null)
                        {
                            int lat = stop.getLat();
                            int lon = stop.getLon();

                            TnLocation location = new TnLocation("");
                            location.setLatitude(lat);
                            location.setLongitude(lon);
                            RegionDetector regionDetector = new RegionDetector(this);
                            String regionFromCache = regionDetector.detectRegion(location);
                            if (regionFromCache == null || regionFromCache.trim().length() == 0)
                            {
                                postModelEvent(EVENT_MODEL_DETECTING_REGION);
                            }
                            else
                            {
                                if(isNewRegionDetected(regionFromCache))
                                {
                                    isSwitchRegion = true;
                                    put(KEY_S_CURRENT_REGION, regionFromCache);
                                    RegionResSwitcher switcher = new RegionResSwitcher(this);
                                    put(KEY_I_SWITCH_REGION_TYPE, SWITCH_REGION_TYPE_SELECT_ADDRESS);
                                    int type = switcher.switchRegion(regionFromCache);
                                    if (type == RegionResSwitcher.SUCCESS_FROM_CACHE)
                                    {
                                        regionSwitchFinished(RegionUtil.getInstance().isNewRegion(regionFromCache));
                                    }
                                    else
                                    {
                                        postModelEvent(EVENT_MODEL_SWITCHING_REGION);
                                    }
                                }
                            }
                        }
                    }

                    if (!isSwitchRegion)
                    {
                        postModelEvent(EVENT_MODEL_RETURN_ADDRESS);
                    }
                }
                else
                {
                    postModelEvent(EVENT_MODEL_RETURN_ADDRESS);
                }

                break;
            }
            case ACTION_REGION_SWITCH_CANCELLED:
            {
                remove(KEY_S_CURRENT_REGION);
                put(KEY_B_IS_REGION_OPERATION_CANCELLED, true);
                RegionResSwitcher.cancelRegionSwitching();
                break;
            }
            case ACTION_REGION_DETECT_CANCELLED:
            {
                remove(KEY_S_CURRENT_REGION);
                put(KEY_B_IS_REGION_OPERATION_CANCELLED, true);
                RegionDetector.cancelDetection();
                break;
            }
        }
    }

    private void checkLocation(TnLocation currentLocation)
    {
        if(isInternationalAddressCapture())
        {
            put(KEY_B_IS_CURRENT_LOCATION_NEEDED, false);
        }
        if(this.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_FAV)
        {
            put(IAcConstants.KEY_O_CURRENT_LOCATION, currentLocation);
            postModelEvent(EVENT_MODEL_RGC);
        }
        else
        {
            Stop stop = new Stop();
            stop.setLat(currentLocation.getLatitude());
            stop.setLon(currentLocation.getLongitude());
            stop.setType(Stop.STOP_CURRENT_LOCATION);
            Address address = new Address();
            address.setLabel(ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_BTN_CURRENT_LOCATION, IStringAc.FAMILY_AC));
            address.setStop(stop);
            put(KEY_O_SELECTED_ADDRESS, address);
            postModelEvent(EVENT_MODEL_LOCATION_GOT);
        }
    }
    
    protected void stop()
    {
    }
    
    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        if (locations == null || locations.length == 0)
        {
            postErrorMessage(ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringAc.RES_MSG_CURRENT_LOCATION_NOT_FOUND,
                        IStringAc.FAMILY_AC));
        }
        else
        {
            if (isInternationalAddressCapture())
            {
                put(KEY_B_IS_CURRENT_LOCATION_NEEDED, false);
            }
            TnLocation currentLocation = locations[0];
            checkLocation(currentLocation);
        }
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
    	if( isNeedPostErrorMsg(proxy) )
		{
			super.networkError(proxy, statusCode, jobId);
		}
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
    	if( isNeedPostErrorMsg(proxy) )
		{
			super.transactionError(proxy);
		}
    }
    
    protected boolean isNeedPostErrorMsg(AbstractServerProxy proxy)
    {
    	if(proxy instanceof IRgcProxy)
		{
    		if(this.getInt(KEY_I_AC_TYPE) == TYPE_AC_FROM_FAV)
    		{
	    		Address address = new Address();
	    		Stop stop = new Stop();
	    		TnLocation currentLocation = (TnLocation) get(IAcConstants.KEY_O_CURRENT_LOCATION);
	    		stop.setLat(currentLocation.getLatitude());
	    		stop.setLon(currentLocation.getLongitude()); 
	    		stop.setType(Stop.STOP_CURRENT_LOCATION);
	    		address.setStop(stop); 
	            put(KEY_O_SELECTED_ADDRESS, address);
	            postModelEvent(EVENT_MODEL_RGC_FINISHED);
	            return false;
    		}
    		else
    		{
    			return true;
    		}
		}
    	return true;
    }
    
    protected void activateDelegate(boolean isUpdateView)
    {
        if ( getBool(KEY_B_IS_NEW_DRIVE_TO_PAGE_NEED_RELOAD) )
        {
            init();
            this.postModelEvent(IAcConstants.EVENT_MODEL_UPDATE_VIEW);
        }
        
        if (getBool(KEY_B_IS_NEED_PAUSE_LATER))
        {
            // Fix issue: go to ac from sync res will white screen.
            // sync res finish will do a lot of things.
            // And AC screen construct need quite a lot of times.(more than 600ms)
            // So pause later to avoid this.
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    MapContainer.getInstance().pause();
                }
            });
            t.start();
        }
        else
        {
            MapContainer.getInstance().pause();
        }
    }
    
    protected void releaseDelegate()
    {
        stop();
        NetworkStatusManager.getInstance().removeStatusListener(this);
        super.releaseDelegate();
    }
    
    protected void deactivateDelegate()
    {
        stop();
        TeleNavDelegate.getInstance().unregisterApplicationListener(this);
    }
    
    protected boolean isValidAddress(Stop stop)
    {
        //verify the stop is valid.
        if ( stop != null && stop.getLat() != 0 && stop.getLon() != 0 )
        {
            return true;
        }
        return false;
    }

    public void appActivated(String[] params)
    {
        this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }

    public void appDeactivated(String[] params)
    {
        //do nothing.
    }
    

    private void regionSwitchFinished(boolean isNewRegion)
    {
        String region = fetchString(KEY_S_CURRENT_REGION);
        UserProfileProvider userProfileProvider = getUserProfileProvider();
        userProfileProvider.setRegion(region);
        if (getInt(KEY_I_SWITCH_REGION_TYPE) == SWITCH_REGION_TYPE_SELECT_REGION_FROM_LIST)
        {
            // used for anchor address setting.
            put(KEY_B_IS_REGION_SWITCHED_DIRECTLY, true);
            // used for AC screen.
            put(KEY_B_IS_CURRENT_LOCATION_NEEDED, isNewRegion);
            postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
        }
        else
        {
            postModelEvent(EVENT_MODEL_RETURN_ADDRESS);
        }
    }

    public void onRegionSwitched(String message, int resultType)
    {
        // If user canceled during region switching, do nothing.
        if (!fetchBool(KEY_B_IS_REGION_OPERATION_CANCELLED))
        {
            if (resultType == RegionResSwitcher.SUCCESS_FROM_SERVER)
            {
                regionSwitchFinished(true);
            }
            else
            {
                remove(KEY_S_CURRENT_REGION);
                postModelEvent(IAcConstants.EVENT_MODEL_REGION_SWITCH_FAILED);
            }
        }
    }

    public void onRegionDetected(String region, int resultType)
    {
        // If user canceled during region detecting, do nothing.
        if (!fetchBool(KEY_B_IS_REGION_OPERATION_CANCELLED))
        {
            if (resultType == RegionDetector.SUCCESS)
            {
                boolean isNewRegion = isNewRegionDetected(region);

                if (isNewRegion)
                {
                    RegionResSwitcher switcher = new RegionResSwitcher(this);
                    put(KEY_I_SWITCH_REGION_TYPE, SWITCH_REGION_TYPE_SELECT_ADDRESS);
                    put(KEY_S_CURRENT_REGION, region);
                    int type = switcher.switchRegion(region);
                    if (type == RegionResSwitcher.SUCCESS_FROM_SERVER)
                    {
                        postModelEvent(EVENT_MODEL_SWITCHING_REGION);
                    }
                    else
                    {
                        regionSwitchFinished(RegionUtil.getInstance().isNewRegion(region));
                    }
                }
                else
                {
                    postModelEvent(EVENT_MODEL_RETURN_ADDRESS);
                }
            }
            else
            {
                // error handling
                postModelEvent(IAcConstants.EVENT_MODEL_REGION_SWITCH_FAILED);
            }
        }
    }
    
    boolean isInternationalAddressCapture()
    {
        boolean isInternationalAc = false;
        int acType = getInt(KEY_I_AC_TYPE);
        boolean isChooseLocation = getBool(KEY_B_IS_CHOOSING_LOCATION);

        if (acType == TYPE_AC_FROM_ONE_BOX && isChooseLocation)
        {
            isInternationalAc = true;
        }

        return isInternationalAc;
    }
    
    protected boolean isNewRegionDetected(String region)
    {
        UserProfileProvider userProfileProvider = (UserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        if(userProfileProvider != null)
        {
            String currentRegion = userProfileProvider.getMandatoryNode().getUserInfo().region;
            return !currentRegion.equals(region);
        }
        else
        {
            return RegionUtil.getInstance().isNewRegion(region);
        }
    }
    
    private UserProfileProvider getUserProfileProvider()
    {
        UserProfileProvider userProfileProvider = (UserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        if(userProfileProvider == null)
        {
            userProfileProvider = new UserProfileProvider();
            put(KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        }
        return userProfileProvider;
    }

    public void statusUpdate(boolean isConnected)
    {
        if (isConnected)
        {
            this.put(KEY_B_IS_ONBOARD, false);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
        else
        {
            this.put(KEY_B_IS_ONBOARD, true);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
}
