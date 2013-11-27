/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RoutePlanningModel.java
 *
 */
package com.telenav.module.nav.routeplanning;

import java.util.Vector;

import android.content.Context;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.telenav.app.CommManager;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.comm.ICommCallback;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IS4AProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.json.JsonS4AProxy;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.route.Route;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.log.mis.log.RouteRequestMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.share.ShareManager;
import com.telenav.module.share.ShareManager.IShareCallback;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IAudioRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDashboard;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author yning (yning@telenav.cn)
 *@date 2010-11-2
 */
class RoutePlanningModel extends AbstractCommonNetworkModel implements IRoutePlanningConstants, LocationListener, IShareCallback, INetworkStatusListener
{
    protected NavSdkNavigationProxy navigationProxy;
    //    protected boolean useCachedRoute = false;
    protected static int MAX_GPS_ERROR_SIZE_4_NAV    = 50;
    // if there is no GPS for 45 seconds, show no GPS error message box with retry option
    public static int NO_GPS_TIME_OUT                   = 45 * 1000;
    
    private String STR_TO = "to "; //add it this to avoid the space remove in String property
    
    public RoutePlanningModel()
    {
        navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(this);
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                cancelCurrentRequest();
                prepareOriginDestStop();
                doInit();
                postModelEvent(EVENT_MODEL_SHOW_ROUTE_PLANNING);
                break;
            }
            case ACTION_INIT_ROUTE_SETTING:
            {
                prepareOriginDestStop();
                break;
            }
            case ACTION_CHECK_RETURN_ADDRESS:
            {
                checkResumeTripFlag();
                boolean isOrigin = fetchBool(KEY_B_IS_EDITING_ORIGIN);
                Address address = (Address)fetch(KEY_O_SELECTED_ADDRESS);
                if (address != null)
                {
                    if (isOrigin)
                    {
                        put(KEY_O_ADDRESS_ORI, address);
                    }
                    else
                    {
                        put(KEY_O_ADDRESS_DEST, address);
                    }
                }
                break;
            }
            case ACTION_CHECK_ACCOUNT_STATUS:
            {
                if (FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_DYNAMIC_ROUTE) == FeaturesManager.FE_UNPURCHASED)
                {
                    this.postModelEvent(EVENT_MODEL_GO_PURCHASE);
                }
                else
                {
                    //first ETA share status: NOT_STARTED
                    SimpleConfigDao simpleConfigDao = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao();
                    boolean enableSharing = simpleConfigDao.getBoolean(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_ENABLED);
                    if(enableSharing && this.getString(KEY_S_TINY_URL_ETA) != null && this.getString(KEY_S_TINY_URL_ETA).trim() != "")
                    {
                        requestUpdateETA();
                    }
                    this.postModelEvent(EVENT_MODEL_GO_NAVIGATE);
                }
                break;
            }
            case ACTION_ROUTE_PLANNING_RETRY_GPS:
            {
//                LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
//                    LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
//                    LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1);
                LocationProvider.getInstance().getReliableCurrentLocation(NO_GPS_TIME_OUT, this);
                break;
            }
            case ACTION_GET_ROUTE:
            {
                doInit();
                storeAddress();
                break;
            }
            case ACTION_CANCEL_CURRENT_REQUEST:
            {
                cancelCurrentRequestWithRouteChoices();
//                cancelCurrentRequest();
                break;
            }
            case ACTION_PREFENCE_RESET:
            {
                ((DaoManager)DaoManager.getInstance()).getTripsDao().initTripPreference();
                clearNavSession();
                break;
            }
            case ACTION_CHECK_MULTI_ROUTE_VALUE:
            {
                storeAddress();
                
                int multiRouteServerDrivenValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_ROUTE_PLANNING_MULTI_ROUTE);
                boolean isMultiRouteEnabled = multiRouteServerDrivenValue == FeaturesManager.FE_ENABLED || multiRouteServerDrivenValue == FeaturesManager.FE_PURCHASED;
                
                if (isMultiRouteEnabled)
                {
                    postModelEvent(EVENT_MODEL_START_ROUTE_PLANNING);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_START_GET_ROUTE);
                    if (this.getBool(KEY_B_IS_RESUME_TRIP) )
                    {
                        playResumeAudio (fetchVector(KEY_V_STOP_AUDIOS));
                    }
                    else
                    {
                        playNavAudio(fetchVector(KEY_V_STOP_AUDIOS));            
                    }
                }
                break;
            }
            case ACTION_BACK_TO_ROUTE_PLANNING:
            {
                ((DaoManager)DaoManager.getInstance()).getTripsDao().initTripPreference();
                put(KEY_B_IS_BACK_FROM_ROUTE_SETTING, true);
                if(get(KEY_A_ROUTE_CHOICES) == null)
                {
                    doInit();
                }
                break;
            }
            case ACTION_CHECK_SHARE_ETA:
            {
                boolean selectedShareEta = this.getBool(KEY_B_IS_SHARE_ETA_SELECTED);
                boolean disabledShareEta = this.getBool(KEY_B_IS_SHARE_ETA_DISABLED);
                if (selectedShareEta && !disabledShareEta)
                {
                    this.postModelEvent(EVENT_MODEL_NEED_SHARE);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_NO_SHARE);
                }
                break;
            }
            case ACTION_REQUEST_TINY_URL:
            {
                Address dest = (Address) this.get(KEY_O_ADDRESS_DEST);
                requestTinyURL(dest);
                break;
            }
            case ACTION_ETA_SHARE:
            {
                String destinationName = "";
                Address dest = (Address) this.get(KEY_O_ADDRESS_DEST);
                Stop destStop = dest.getStop();
                // for destType: normal = 0; home = 1; work = 2
                int destType = -1;

                if (destStop != null)
                {
                    Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                    Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();

                    if (DaoManager.getInstance().getAddressDao().isSameStop(destStop, work))
                    {
                        destinationName = STR_TO
                                + ResourceManager.getInstance().getCurrentBundle()
                                        .getString(IStringDashboard.RES_STRING_WORK, IStringDashboard.FAMILY_DASHBOARD);
                    }
                    else if (DaoManager.getInstance().getAddressDao().isSameStop(destStop, home))
                    {
                        destinationName = ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringDashboard.RES_STRING_HOME, IStringDashboard.FAMILY_DASHBOARD);
                    }
                    else
                    {
                        if(dest.getType() == 1 || dest.getType() == 3)
                        {
                            if (destStop.getLabel() != null && destStop.getLabel().length() > 0)
                            {
                                destinationName = STR_TO + destStop.getLabel();
                            }
                            if(isValidFullAddress(destStop))
                            {
                                destinationName = destinationName + ", " + destStop.getCity() + ", " 
                                        + destStop.getProvince() + " "
                                        + destStop.getPostalCode();
                            }
                        }
                        else
                        {
                            if (destStop.getLabel() != null && destStop.getLabel().length() > 0)
                            {
                                destinationName = STR_TO + destStop.getLabel();
                            }

                            else
                            {
                                destinationName = STR_TO
                                        + ResourceManager.getInstance().getStringConverter().convertAddress(destStop, false);
                            }
                        }
                    }
                }
                destType =  (Integer) this.get(KEY_I_SHARE_ETA_ADDRESS_TYPE);
                String tinyURL = this.getString(KEY_S_TINY_URL_ETA);
                
                int key = -1;
                if (destType == TYPE_HOME_DEST)
                {
                    key = SimpleConfigDao.KEY_AUTO_SHARE_HOME_RECIPIENTS;
                }
                else if (destType == TYPE_WORK_DEST)
                {
                    key = SimpleConfigDao.KEY_AUTO_SHARE_WORK_RECIPIENTS;
                }

                Vector vRecipients = DaoManager.getInstance().getSimpleConfigDao().getContactVecValue(key);
                ShareManager.getInstance().startShareETA(destinationName, tinyURL, destType, this, vRecipients, getArrivalTime());
                break;
            }
            default:
                break;
        }
    }

    private boolean isValidFullAddress(Stop destStop)
    {
        if(destStop.getCity() == null || destStop.getCity().trim().length() == 0)
            return false;
        if(destStop.getProvince() == null || destStop.getProvince().trim().length() == 0)
            return false;
        if(destStop.getPostalCode()== null || destStop.getPostalCode().trim().length() == 0)
            return false;
        return true;
    }

    protected void requestUpdateETA()
    {
        boolean hasSignInScoutDotMe = false;
        String name = null;
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
        if(credentialInfo != null && credentialInfo.credentialValue != null && credentialInfo.credentialValue.trim().length() > 0)
        {
            hasSignInScoutDotMe = true;
        }
        if (hasSignInScoutDotMe)
        {
            name = preferenceDao.getStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME) + " "
                    + preferenceDao.getStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME);
        }
        else
        {
            String friend = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringCommon.RES_SHARE_ETA_FRIEND, IStringCommon.FAMILY_COMMON);
            name = friend;
        }
        IS4AProxy s4Is4aProxy = ServerProxyFactory.getInstance().createS4AProxy(null,
            CommManager.getInstance().getComm(), this, null);
        Address currentAddress = (Address) get(KEY_O_ADDRESS_ORI);
        float lat = (float) currentAddress.getStop().getLat() / 100000;
        float lon = (float) currentAddress.getStop().getLon() / 100000;
        String tinyUrl = this.getString(KEY_S_TINY_URL_ETA);
        String drivingStatus = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_SHARE_ETA_NOT_STARTED, IStringCommon.FAMILY_COMMON);
        s4Is4aProxy.updateETA(tinyUrl, drivingStatus, name, getETA(), String.valueOf(lat), String.valueOf(lon), null, null, null);
    }
    
    protected void requestTinyURL(Address currentAddress)
    {
        if (currentAddress != null)
        {
            IS4AProxy s4Is4aProxy = ServerProxyFactory.getInstance().createS4AProxy(null,
                CommManager.getInstance().getComm(), this, null);

            float lat = (float) currentAddress.getStop().getLat() / 100000;
            float lon = (float) currentAddress.getStop().getLon() / 100000;

            String address = ResourceManager.getInstance().getStringConverter().convertAddress(currentAddress.getStop(), false);
            s4Is4aProxy.requestTinyUrl(address, String.valueOf(lat), String.valueOf(lon),
                currentAddress.getLabel(), false);
        }
    }
    
    private String getArrivalTime()
    {
        String arrivalTimeStr = "";
        Object obj = this.get(KEY_A_ROUTE_CHOICES_ETA);
        int selectedPlan = this.getInt(KEY_I_SELECTED_PLAN);
        if(selectedPlan < 0)
        {
            selectedPlan = 0;
        }
        
        int routeEta = 0;
        int trafficDelay = 0;
        if (obj != null)
        {
            int[] nzRouteEtas = (int[]) obj;
            if(nzRouteEtas != null && nzRouteEtas.length > selectedPlan)
            {
                routeEta = nzRouteEtas[selectedPlan];
            }
        }

        obj = this.get(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY);
        if (obj != null)
        {
            int[] trafficDelays = (int[]) obj;
            if(trafficDelays != null && trafficDelays.length > selectedPlan)
            {
                trafficDelay = trafficDelays[selectedPlan];
            }
        }
        
       
        int costTime = routeEta + trafficDelay;
        long currentTime = System.currentTimeMillis();
        long arrivalTimeMMSec = currentTime + costTime*1000;
        
        arrivalTimeStr = DateUtils.formatDateTime(AndroidPersistentContext.getInstance().getContext(), arrivalTimeMMSec, DateUtils.FORMAT_SHOW_TIME);
        return arrivalTimeStr;
    }
    
    private String getETA()
    {
        Object obj = this.get(KEY_A_ROUTE_CHOICES_ETA);
        int selectedPlan = this.getInt(KEY_I_SELECTED_PLAN);
        if(selectedPlan < 0)
        {
            selectedPlan = 0;
        }
        
        int routeEta = 0;
        int trafficDelay = 0;
        if (obj != null)
        {
            int[] nzRouteEtas = (int[]) obj;
            if(nzRouteEtas != null && nzRouteEtas.length > selectedPlan)
            {
                routeEta = nzRouteEtas[selectedPlan];
            }
        }

        obj = this.get(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY);
        if (obj != null)
        {
            int[] trafficDelays = (int[]) obj;
            if(trafficDelays != null && trafficDelays.length > selectedPlan)
            {
                trafficDelay = trafficDelays[selectedPlan];
            }
        }
        String costTime = String.valueOf(routeEta + trafficDelay);
        return costTime;
    }
    
    private void checkResumeTripFlag()
    {
        boolean isAddressChanged = false;
        boolean isOrigin = getBool(KEY_B_IS_EDITING_ORIGIN);
        Address address = (Address) get(KEY_O_SELECTED_ADDRESS);
        if (isOrigin)
        {
            Address preOriaddress = (Address) get(KEY_O_ADDRESS_ORI);
            if (!preOriaddress.isSameAddress(address))
                isAddressChanged = true;
        }
        else
        {
            Address preDestaddress = (Address) get(KEY_O_ADDRESS_DEST);
            if (!preDestaddress.isSameAddress(address))
                isAddressChanged = true;
        }
        if (isAddressChanged)
            put(KEY_B_IS_RESUME_TRIP, false);
    }

    private boolean isDynamicNav()
    {
        if (get(KEY_O_ADDRESS_ORI) == null)
        {
            return true;
        }
        
        return isCurrentLocation(true);
    }
    
    private boolean isCurrentLocation(boolean isOrigin)
    {
        Object obj = null;
        if(isOrigin)
        {
            obj = get(KEY_O_ADDRESS_ORI);
        }
        else
        {
            obj = get(KEY_O_ADDRESS_DEST);
        }
        
        Stop stop = ((Address)obj).getStop();
        if (stop != null && stop.getType() == Stop.STOP_CURRENT_LOCATION)
        {
            return true;
        }   
        return false;
    }
    
    private void prepareOriginDestStop()
    {
        Address origAddress = (Address) get(KEY_O_ADDRESS_ORI);
        if(origAddress == null)
        {
            Stop origStop = new Stop();
            origStop.setType(Stop.STOP_CURRENT_LOCATION);
            origAddress = new Address();
            origAddress.setStop(origStop);
        }
        put(KEY_O_ADDRESS_ORI, origAddress);

        Address destAddress = (Address) get(KEY_O_ADDRESS_DEST);
        if (destAddress != null)
        {
            put(KEY_O_ADDRESS_DEST, destAddress);
        }
        else
        {
            throw new IllegalArgumentException(getClass().toString() + ": dest stop is null");
        }
    }
    
    protected void requestRouteChoices(boolean isStaticNav)
    {
        LocationProvider.getInstance().cancelAllRequests();
        
        //for static route, we need original address
        //for dynamic route, we need original gps location
        TnLocation[] orgLocations = new TnLocation[3];
        
        if (!isStaticNav)
        {
            TnLocation loc = (TnLocation) get(KEY_O_CURRENT_LOCATION);
            orgLocations[0] = orgLocations[1] = orgLocations[2] = loc;
        }
        
        Address origAddress = (Address)get(KEY_O_ADDRESS_ORI);
        Address destAddress = (Address) get(KEY_O_ADDRESS_DEST);

        int routeStyle = DaoManager.getInstance().getTripsDao().getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
        int routeSetting;
        if (routeStyle == Route.ROUTE_PEDESTRIAN)
        {
        	routeSetting = Preference.AVOID_CARPOOL_LANES;
        }
        else
        {
        	routeSetting = DaoManager.getInstance().getTripsDao().getIntValue(Preference.ID_PREFERENCE_ROUTE_SETTING);        	
        }
//        int heading = origGpsData[0].getHeading();
        put(KEY_B_IS_REQUEST_CANCELLED, false);
        
        MisLogManager misLogManager = MisLogManager.getInstance();
        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ROUTE_REQUEST))
        {
            RouteRequestMisLog log = misLogManager.getFactory().createRouteRequestMisLog();
            log.setTimestamp(System.currentTimeMillis());
            log.setAddressSource(destAddress.getSource());
            log.setAddressType(destAddress.getType());
            AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
                IMisLogConstants.TYPE_INNER_APP_STATUS);
            log.setAddressInput(appStatusMisLog.getRouteRequestBy());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { log });
        }
        
        if (isStaticNav)
        {
            navigationProxy.requestStaticRouteChoices(origAddress, destAddress, routeStyle, routeSetting);
        }
        else
        {
            navigationProxy.requestRouteChoices(orgLocations, destAddress, routeStyle, routeSetting);
        }
        
        clearRouteChoices();
    }
    
    private void clearRouteChoices()
    {
        this.remove(KEY_B_SHOW_ROUTE_LIST);
        this.remove(KEY_A_ROUTE_CHOICES);
        this.remove(KEY_A_ROUTE_CHOICES_ETA);
        this.remove(KEY_A_ROUTE_CHOICES_LENGTH);
        this.remove(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY);
        this.put(KEY_I_SELECTED_PLAN, 0);
    }
    
    protected void releaseDelegate()
    {
        stop();
    }
    
    protected void notifyRoutePlanningNoGpsTimeout()
    {
        postModelEvent(EVENT_MODEL_NO_GPS_TIMEOUT);
    }
    
    protected void requestCellLocationBasedRouteChoices(TnLocation loc)
    {
        Logger.log(Logger.INFO, getClass().toString(), "Route Plan model :: requestCellLocationBasedRouteChoices");

        if (NavSdkNavEngine.getInstance().isRunning())
            NavSdkNavEngine.getInstance().stop();
        
        TnLocation data = new TnLocation(TnLocationManager.NETWORK_PROVIDER);
        data.set(loc);
        data.setAccuracy(Math.min(data.getAccuracy(), MAX_GPS_ERROR_SIZE_4_NAV));

        put(KEY_O_CURRENT_LOCATION, data);
        requestRouteChoices(false);
    }
    
    protected void requestGpsBasedRouteChoices(TnLocation loc)
    {
        Logger.log(Logger.INFO, getClass().toString(), "Route Plan model :: requestGpsBasedRouteChoices");
        // stop previous possible on-going navigation or checking deviation request
        if (NavSdkNavEngine.getInstance().isRunning())
            NavSdkNavEngine.getInstance().stop();

        put(KEY_O_CURRENT_LOCATION, loc);
        requestRouteChoices(false);
    }
    
    private void doInit()
    {
        //static navigation
        if (isDynamicNav())
        {
            remove(KEY_O_CURRENT_LOCATION);
            TnLocation loc = LocationProvider.getInstance().getReliableCurrentLocation();

            if (loc != null)
            {
                updateCurrentLocationStop(loc);
                if (loc.getProvider().equals(TnLocationManager.NETWORK_PROVIDER)
                        || loc.getProvider().equals(TnLocationManager.TN_NETWORK_PROVIDER))
                {
                    requestCellLocationBasedRouteChoices(loc);
                }
                else
                {
                    requestGpsBasedRouteChoices(loc);
                }
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "route planning , first gps fix failed!!! use call back!!");
                LocationProvider.getInstance().getReliableCurrentLocation(NO_GPS_TIME_OUT, this);
            }
        }
        else
        {
            requestRouteChoices(true);
        }
        
        if (this.getBool(KEY_B_IS_RESUME_TRIP))
        {
            playResumeAudio (fetchVector(KEY_V_STOP_AUDIOS));
        }
        else
        {
            playNavAudio(fetchVector(KEY_V_STOP_AUDIOS));            
        }        
    }

    protected void playResumeAudio(Vector audioList)
    {
        if (audioList == null)
        {
            audioList = new Vector();
        }
        else if (audioList.size() > 0)// no need add "to" if there's no audio for destination stop.
        {
            AudioDataNode toAudio = AudioDataFactory.getInstance().createAudioDataNode(
                AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_TO));
            audioList.insertElementAt(toAudio, 0);
        }

        AudioDataNode resumeAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_RESUME_TRIP));
        audioList.insertElementAt(resumeAudio, 0);
        if(isPlayAudioEnabled())
        MediaManager.getInstance().playAudio(audioList);
    }
   
    protected void playNavAudio(Vector audioList)
    {
        Preference routeStylePref = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        
        int routeStyle = Route.ROUTE_FASTEST;
        if(routeStylePref != null)
        {
            routeStyle = routeStylePref.getIntValue();
        }
        
        int audioId = 0;
        switch(routeStyle)
        {
            case Route.ROUTE_FASTEST:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_FASTEST;
                break;
            }
            case Route.ROUTE_SHORTEST:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_SHORTEST;
                break;
            }
            case Route.ROUTE_PEDESTRIAN:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_PEDESTRIAN;
                break;
            }
            case Route.ROUTE_AVOID_HIGHWAY:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_STREET;
                break;  
            }
            default:
            {
                audioId = IAudioRes.STATIC_AUDIO_ROUTESTYLE_FASTEST;
                break;
            }
        }

        if(audioList == null)
        {
            audioList = new Vector();
        }
        else if(audioList.size() > 0)//no need add "to" if there's no audio for destination stop.
        {
            AudioDataNode toAudio = AudioDataFactory.getInstance().createAudioDataNode(
                AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_TO));
            audioList.insertElementAt(toAudio, 0);
        }
        
        AudioDataNode styleAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(audioId));
        audioList.insertElementAt(styleAudio, 0);
        
        AudioDataNode gettingAudio = AudioDataFactory.getInstance().createAudioDataNode(
            AudioDataFactory.getInstance().createAudioData(IAudioRes.STATIC_AUDIO_GETTING));
        audioList.insertElementAt(gettingAudio, 0);
        if(isPlayAudioEnabled())
        MediaManager.getInstance().playAudio(audioList);
    }

    protected void cancelCurrentRequest()
    {
        put(KEY_B_IS_REQUEST_CANCELLED, true);
        clearRouteChoices();
        LocationProvider.getInstance().cancelAllRequests();
        navigationProxy.cancelRoutePlanRequest();
        MediaManager.getInstance().getAudioPlayer().cancelAll();
    }
    
    protected void cancelCurrentRequestWithRouteChoices()
    {
        put(KEY_B_IS_REQUEST_CANCELLED, true);
        LocationProvider.getInstance().cancelAllRequests();
        navigationProxy.cancelRoutePlanRequest();
        MediaManager.getInstance().getAudioPlayer().cancelAll();
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        if (IServerProxyConstants.ACT_GET_TINY_URL.equals(action))
        {
            int status = ((JsonS4AProxy) proxy).getStatus();
            if (status == ICommCallback.SUCCESS)
            {
                // got tiny url
                String tinyUrl = ((JsonS4AProxy) proxy).getTinyUrl();
                this.put(KEY_S_TINY_URL_ETA, tinyUrl);
                this.postModelEvent(EVENT_MODEL_SHARE_GOT_TINY_URL);
            }
            else
            {
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Context ctx=AndroidPersistentContext.getInstance().getContext();
                        String  errorMessage = "Share ETA not available, please try later";
                        Toast.makeText(ctx, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
                this.postModelEvent(EVENT_MODEL_SHARE_GOT_TINY_URL_FAIL);
            }
        }
        
        if (IServerProxyConstants.ACT_UPDATE_ETA.equals(action))
        {
            int status = ((JsonS4AProxy) proxy).getStatus();
            if (status == ICommCallback.SUCCESS)
            {
                //for future use
                Logger.log(Logger.INFO, this.getClass().getName(), "update share ETA success");
            }
            else
            {
               //for future use
                Logger.log(Logger.INFO, this.getClass().getName(), "update share ETA failed");
            }
        }
        
        if (proxy instanceof NavSdkNavigationProxy)
        {
            if(fetchBool(KEY_B_IS_REQUEST_CANCELLED))
            {
                return;
            }
            NavSdkNavigationProxy navProxy = (NavSdkNavigationProxy) proxy;
            if (action.equals(INavSdkProxyConstants.ACT_NAV_ROUTE_PLAN))
            {
                int[] etas = navProxy.getRouteChoiceETA();
                int[] trafficDelays = navProxy.getRouteChoicesTrafficDelay();
                int[] lengths = navProxy.getRouteChoicesLength();
                Route[] choices = navProxy.getChoices();

                put(KEY_A_ROUTE_CHOICES_ETA, etas);
                put(KEY_A_ROUTE_CHOICES, choices);
                put(KEY_A_ROUTE_CHOICES_TRAFFIC_DELAY, trafficDelays);
                put(KEY_A_ROUTE_CHOICES_LENGTH, lengths);

                put(KEY_I_SELECTED_PLAN, 0);
                put(KEY_B_IS_UPDATE_SHOW_ROUTE, true);
                put(KEY_B_SHOW_ROUTE_LIST, false);
                postModelEvent(EVENT_MODEL_SHOW_ROUTE_PLANNING);
                
                boolean isOnBoard = !NetworkStatusManager.getInstance().isConnected();
                NavRunningStatusProvider.getInstance().setIsOnBoard(isOnBoard);
            }
        }
    }

    protected void updateCurrentLocationStop(TnLocation loc)
    {
        Address origAddress = (Address)get(KEY_O_ADDRESS_ORI);
        Address destAddress = (Address)get(KEY_O_ADDRESS_DEST);
        
        if(origAddress != null)
        {
            Stop origStop = origAddress.getStop();
            if(origStop != null && origStop.getType() == Stop.STOP_CURRENT_LOCATION)
            {
                origStop.setLat(loc.getLatitude());
                origStop.setLon(loc.getLongitude());
            }
        }
        
        if(destAddress != null)
        {
            Stop destStop = destAddress.getStop();
            
            if(destStop != null && destStop.getType() == Stop.STOP_CURRENT_LOCATION)
            {
                destStop.setLat(loc.getLatitude());
                destStop.setLon(loc.getLongitude());
            }
        }
    }
    
    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            TnLocation loc = locations[0];
            if (loc != null)
            {
                updateCurrentLocationStop(loc);
                if (TnLocationManager.NETWORK_PROVIDER.equals(loc.getProvider())
                        || TnLocationManager.TN_NETWORK_PROVIDER.equals(loc.getProvider()))
                {
                    requestCellLocationBasedRouteChoices(loc);
                }
                else
                {
                    requestGpsBasedRouteChoices(loc);
                }
            }
            else
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "route planning ,gps call back timeout(no data)");
                notifyRoutePlanningNoGpsTimeout();
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "route planning ,gps call back timeout");
            notifyRoutePlanningNoGpsTimeout();
        }
    }
    
    protected void stop()
    {
        LocationProvider.getInstance().cancelAllRequests();
//        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_GET_ROUTE_CHOICES);
    }
    
    // To clear navigation session ,as navsdk will start it from requestRouteChoices,
    // when back key press in the screen, the navigation session should be cleared
    // please refer to : http://jira.telenav.com:8080/browse/TNANDROID-6566
    public void clearNavSession()
    {
        if (navigationProxy != null)
            navigationProxy.sendStopNavigationRequest();
    }
    
    protected void deactivateDelegate()
    {
        stop();
        RouteUiHelper.resetCurrentRoute();
        MapContainer.getInstance().clearRoute();
        MapContainer.getInstance().pause();
    }
    
    /**
     * Activate current controller.<br>
     * 
     * For example,resume daemon back end job.<br>
     * 
     * @param isUpdateView
     */
    protected void activateDelegate(boolean isUpdateView)
    {
        MapContainer.getInstance().resume();
    }
    
    protected void storeAddress()
    {
        Address originAddress = (Address) get(KEY_O_ADDRESS_ORI);
        Address destAddress = (Address) get(KEY_O_ADDRESS_DEST);
        DaoManager.getInstance().getAddressDao().addToRecent(originAddress);
        DaoManager.getInstance().getAddressDao().addToRecent(destAddress);
    }

    protected boolean isPlayAudioEnabled()
    {
        Preference audioPreference = DaoManager.getInstance().getTripsDao().getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        boolean audioDisable = audioPreference != null && (audioPreference.getIntValue() == Preference.AUDIO_NONE );
        return !audioDisable;
    }

    @Override
    public void onShareResult(boolean success)
    {
        if(success)
        {
            //Do nothing
            this.postModelEvent(EVENT_MODEL_SHARE_ETA_FINISH);
        }
        else
        {
            //Do nothing
            this.postModelEvent(EVENT_MODEL_SHARE_ETA_FINISH);
        }
        
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }

    public String parseEtaDisplayTime(int second)
    {
        String etaString = "00:";
        int hour = (int)(second / 3600);
        if ( hour > 0 && hour < 10 )
        {
            etaString =  Integer.toString(hour) + ":";
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
}
