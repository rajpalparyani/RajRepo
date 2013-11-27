/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MovingMapModel.java
 *
 */
package com.telenav.module.nav.movingmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.json.tnme.JSONArray;
import org.json.tnme.JSONException;
import org.json.tnme.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.WindowManager;

import com.telenav.app.CommManager;
import com.telenav.app.IApplicationListener;
import com.telenav.app.IScreenStateListener;
import com.telenav.app.NavServiceManager;
import com.telenav.app.NotificationManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.TnBacklightManagerImpl;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.audio.AudioPlayer;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.misc.TripsDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.nav.NavSdkNavState;
import com.telenav.data.datatypes.poi.BillboardAd;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.route.NavSdkRoute;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IS4AProxy;
import com.telenav.data.serverproxy.impl.ISyncPurchaseProxy;
import com.telenav.data.serverproxy.impl.ITrafficIncidentReportProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.INavSdkProxyConstants;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.data.serverproxy.impl.navsdk.service.ITurnListener;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkAudioService;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkBacklightService;
import com.telenav.datatypes.DataUtil;
import com.telenav.datatypes.audio.AbstractRule;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.nav.NavState;
import com.telenav.datatypes.nav.TnNavLocation;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.Segment;
import com.telenav.datatypes.traffic.TrafficIncident;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.MainAppStatus;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.htmlsdk.IHtmlSdkServiceListener;
import com.telenav.htmlsdk.ResultGenerator;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.log.ILogConstants;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.ArrivalConfirmationMisLog;
import com.telenav.log.mis.log.TripSummaryMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.RouteUiHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.dwf.DwfUtil;
import com.telenav.module.dwf.DriveWithFriendsController;
import com.telenav.module.dwf.DwfSliderPopup;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.media.AudioComposer;
import com.telenav.module.media.MediaManager;
import com.telenav.module.media.rule.AudioRuleManager;
import com.telenav.module.media.rule.IRuleParameter;
import com.telenav.module.nav.INavEngineListener;
import com.telenav.module.nav.ITrafficCallback;
import com.telenav.module.nav.NavAdManager;
import com.telenav.module.nav.NavBottomStatusBarHelper;
import com.telenav.module.nav.NavParameter;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.module.nav.trafficengine.ITrafficAlertEngineListener;
import com.telenav.module.nav.trafficengine.TrafficAlertEvent;
import com.telenav.module.nav.trafficengine.TrafficAudioEvent;
import com.telenav.module.region.IRegionChangeListener;
import com.telenav.module.region.RegionResSwitcher;
import com.telenav.module.region.RegionUtil;
import com.telenav.module.share.ShareManager;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.NavigationData.RouteTurnType;
import com.telenav.navsdk.events.NavigationEvents.VehiclePosition;
import com.telenav.navsdk.events.NavigationEvents.VoiceGuidancePlayNotification;
import com.telenav.navsdk.nav.event.NavSdkNavEvent;
import com.telenav.res.IAudioRes;
import com.telenav.res.ISpecialImageRes;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringEntry;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.IVechiclePositionCallback;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.citizen.map.MapVehiclePositionService;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author yning (yning@telenav.cn)
 * @date 2010-11-5
 */
class MovingMapModel extends BrowserSdkModel implements IMovingMapConstants, LocationListener, INavEngineListener,
        ITrafficCallback, ITrafficAlertEngineListener, INetworkStatusListener, IScreenStateListener, IRegionChangeListener,
        IVechiclePositionCallback, ITurnListener, INotifierListener, IApplicationListener

{
    protected static final long GPS_STATUS_CHANGE_DELAY_TIME = 30 * 1000; // 30 seconds

    protected static final long TRIP_TIME_DOT_INTERVAL = 60 * 1000; // 1 min
    
    protected static final long BRIGHTNESS_KICK_IN_TIME = 5 * 60 * 1000; // 5 min
    
    protected static final long BRIGHTNESS_DISTANCE_THRESHOLD = 10 * 1609; // 10 miles
    
    protected long notifyIntervalForShareETA = 30 * 1000;
    
    protected long lastNotifyTimestampForShareETA = -1;

    protected static final float JUNCTION_VIEW_CAMERA_DECLINATION = -53.0f;

    private static float TINY_CAMERA_DECLINATION_LANDSCAP = -15.0f;
    
    private static float SMALL_CAMERA_DECLINATION_LANDSCAP = -16.0f;

    private static float MEDIUM_CAMERA_DECLINATION_LANDSCAP = -16.0f;
   
    private static float LARGE_CAMERA_DECLINATION_LANDSCAP = -18.0f;

    private static float TINY_CAMERA_DECLINATION_PORTRAIT = -22.0f;

    private static float SMALL_CAMERA_DECLINATION_PORTRAIT = -23.0f;

    private static float MEDIUM_CAMERA_DECLINATION_PORTRAIT = -26.0f;

    private static float LARGE_CAMERA_DECLINATION_PORTRAIT = -30.0f;
    
    
    private static final int DECREASE_BACKLIGHT_DISALBE = -1;
    
    private static final int DECREASE_BACKLIGHT_LATER = 0;
    
    private static final int DECREASE_BACKLIGHT_NOW = 1;
    
    float zoomBeforeDeactivate = IMovingMapConstants.ZOOM_LEVEL_DEFAULT_NAV;
    
    float zoomBeforeJunctionView = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_LANDSCAPE ? ZOOM_LEVEL_DEFAULT_NAV_LANDSCAPE
            : ZOOM_LEVEL_DEFAULT_NAV;
    
    protected boolean isReachEndTrip = false;

    protected boolean isFirstTimeToHandleNavEvent = true;

    protected boolean isEndTripShown;

    protected boolean isLoged = false;

    protected boolean isFirstTimeLocated = false;

    protected boolean isCurrentlyNoGPS = false;

    protected boolean isShownNoGPSIcon = false;

    protected boolean isModelReleased = false;
    
    protected boolean isInTurn = false;
    
    protected boolean isNeedCheckDecreaseLater = false;
    
    protected boolean isBackToHome = false;
    
    protected float cameraDeclinationBeforeJunctionViewPortrait = 0;

    protected float cameraDeclinationBeforeJunctionViewLandscap = 0;
    
    protected long startTime = -1L;
    
    protected long endTripTime = -1L;

    protected long lastSendingTrafficIncidentTime = -1;

    protected long lastShowBacklightTime = -1L;

    protected long lastTripTimeDot = -1L;
    
    protected long lastGPSStatusChangeTime;

    protected long appStopTimeStamp;

    protected int avoidIncidentRequestId = -1;
    
    protected int backlightPreference = -1;

    protected TripSummaryMisLog tripLog = null;
    
    protected ArrivalConfirmationMisLog arrivalConfirmationMisLog = null;
    
    protected MapTileAdiStatusJudger mapTileAdiStatusJudger;
    
    protected String targetRegion = null;
    
    protected TnLocation lastVehicleLocation = null;

    protected NavSdkNavigationProxy navigationProxy = null;
    
    protected boolean isCurrentConnected;
    
    protected Timer networkTimer;
    
    protected boolean isDriving = false;
    
    protected long shareETA;
    
    long activateTime;
    
    private int isRoundAbout = 0;
    
    private boolean isAutoSwitchAtTurn = false;
    
    boolean isInJunctionViewState;
    
    private boolean isGoToDwf = false;
    
    private boolean hasLogNavArrival = false;

    MovingMapModel()
    {
        TeleNavDelegate.getInstance().registerApplicationListener(this);
        navigationProxy = ServerProxyFactory.getInstance().createNavigationProxy(this);
        mapTileAdiStatusJudger = new MapTileAdiStatusJudger();

        TeleNavDelegate.getInstance().registerScreenStateListener(this);
        RegionUtil.getInstance().setRegionChangeListener(this);
        
        backlightPreference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
            Preference.ID_PREFERENCE_BACKLIGHT);
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_LOG_EXIT:
            {
                NavSdkNavEvent navInfo = (NavSdkNavEvent) this.get(KEY_O_NAVEVENT);
                if (navInfo != null)
                {
                    long distance = navInfo.getDistanceToDest();
                    if (distance != 0)
                    {
                        MisLogManager misLogManager = MisLogManager.getInstance();
                        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ARIVALl_CONFIRMATION))
                        {
                            if (arrivalConfirmationMisLog != null)
                            {
                                arrivalConfirmationMisLog.setDistanceToDest(distance);
                                Logger.log(Logger.INFO, this.getClass().getName(), "Mislog : " + "(ACTION_LOG_EXIT) : "
                                        + distance);
                                Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, null,
                                    new Object[]
                                    { arrivalConfirmationMisLog });
                            }
                        }
                    }
                }
                stopNavigation();
                break;
            }
            case ACTION_INIT_MAP:
            {
                getNormalRoute();
                break;
            }
            case ACTION_GET_ROUTE:
            {
                getNormalRoute();
                break;
            }
            case ACTION_GET_AD_ROUTE:
            {
                Address adAddress = (Address) get(KEY_O_AD_ADDRESS);
                doGetRoute(true, adAddress);
                DaoManager.getInstance().getAddressDao().addToRecent(adAddress);
                Logger.log(Logger.INFO, this.getClass().getName(), "adAddress label: " + adAddress.getStop().getLabel()
                        + ",lat:" + adAddress.getStop().getLat() + ",lon:" + adAddress.getStop().getLon());
                break;
            }
            case ACTION_CONTINUE_GET_ROUTE:
            {
                int type = this.fetchInt(KEY_I_GET_ROUTE_TYPE);
                if (type == ID_TYPE_GET_ROUTE)
                {
                    getNormalRoute();
                }
                else if (type == ID_TYPE_GET_AD_ROUTE)
                {
                    Address adAddress = (Address) get(KEY_O_AD_ADDRESS);
                    doGetRoute(true, adAddress);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_EXIT_NAV);
                }
                break;
            }
            case ACTION_AVOID_INCIDENT:
            {
                doAvoidIncident();
                break;
            }
            case ACTION_CANCEL_AVOID_INCIDENT:
            {
                doCancelAvoidIncident();
                break;
            }

            case ACTION_RENEW_DYNAMIC_ROUTE:
            {
                startNav();
                break;
            }
            case ACTION_REPORT_SPEED_TRAP:
            {
                sendTrafficIncidentReport(false);
                break;
            }
            case ACTION_RESUME_TRIP:
            {
                doResumeRoute();
                break;
            }
            case ACTION_PLAY_AUDIO:
            {
                doPlayAudio();
                break;
            }
            case ACTION_SHOW_END_TRIP:
            {
                if (!isEndTripShown)
                {
                    isEndTripShown = true;
//                    recordDestTime();
                    TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
                    tripsDao.endTrip();
                    tripsDao.store();
                }
                break;
            }
            case ACTION_CHECK_TRIP:
            {
                if (this.getBool(KEY_B_IS_ARRIVE_DESTINATION))
                {
                    TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
                    tripsDao.endTrip();
                    tripsDao.store();
                }
                stopNavigation();
                break;
            }
            case ACTION_END_TRIP_CHECK:
            {

                if (getBool(KEY_B_IS_FROM_SEARCH_ALONG))
                {
                    // detour end trip
                    this.postModelEvent(EVENT_MODEL_DETOUR_CONFIRM);
                }
                else
                {
                    // normal end trip
                    this.postModelEvent(EVENT_MODEL_EXIT_NAV);
                }

                break;
            }
            case ACTION_CHECK_BACK:
            {
                if (this.fetchBool(KEY_B_GOTO_EXIT_CONFIRM))
                {
                    this.postModelEvent(EVENT_MODEL_EXIT_CONFIRM);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_COMMON_BACK);
                }
                break;
            }
            case ACTION_SYNC_PURCHASE:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(KEY_O_USER_PROFILE_PROVIDER);
                ISyncPurchaseProxy syncPurchaseProxy = ServerProxyFactory.getInstance().createSyncPurchaseProxy(null,
                    CommManager.getInstance().getComm(), this, userProfileProvider);
                syncPurchaseProxy.sendSyncPurchaseRequest(FeaturesManager.APP_CODE);
                break;
            }
            case ACTION_EXIT_BROWER:
            {
                if (getState() == STATE_GENERAL_FEEDBACK)
                {
                    ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).closeOptionsMenu();
                }
                else
                {
                    NavParameter param = getNavParameter();

                    param.hideVBB = true;
                }
                break;
            }
            case ACTION_ROUTE_SETTING_CHECKING:
            {
                boolean isRouteSettingChanged = this.fetchBool(ICommonConstants.KEY_B_ROUTE_SETTING_CHANGED);
                if (isRouteSettingChanged)
                    postModelEvent(EVENT_MODEL_ROUTE_SETTING_CHANGED);
                else
                    postModelEvent(EVENT_MODEL_ROUTE_SETTING_UNCHANGED);
                break;
            }
            case ACTION_CHECKING_ERROR_CONTEXT:
            {
                boolean isFromAvoidIncident = fetchBool(KEY_B_IS_AVOID_INCIDENT);
                if (isFromAvoidIncident)
                {
                    postModelEvent(EVENT_MODEL_SHOW_NAV_MAP);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_BACK_TO_PRE);
                }
                break;
            }
            case ACTION_ACCOUNT_FATAL:
            {
                handleAccountFatalError();
                break;
            }
            case ACTION_BACK_FROM_VBB_DETAIL:
            {
                put(KEY_B_IS_BACK_FROM_VBB_DETAIL, true);
                break;
            }
            case ACTION_CHECK_DETOUR:
            {
                if (this.fetchBool(KEY_B_IS_DETOURE_ERROR))
                {
                    postModelEvent(EVENT_MODEL_GOTO_DETOUR);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_GOTO_NON_DETOUR);
                }
                break;
            }
            case ACTION_STOP_NAVIGATION:
            {
                stopNavigation();
                break;
            }
            default:
            {
                break;
            }
        }
    }
    
    public void stopNavigation()
    {
        if (this.navigationProxy != null)
        {
            navigationProxy.sendStopNavigationRequest();
        }
    }

    protected void requestUpdateETA(String status, TnLocation currentLocation, String eta)
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
        if(currentLocation != null)
        {
            IS4AProxy s4Is4aProxy = ServerProxyFactory.getInstance().createS4AProxy(null, CommManager.getInstance().getComm(),
                this, null);
            float lat = (float) currentLocation.getLatitude() / 100000;
            float lon = (float) currentLocation.getLongitude() / 100000;
            float speed = (float) currentLocation.getSpeed() / 6;
            float accuracy = (float)((int) currentLocation.getAccuracy() << 13) / 7358;
            int routeID = getInt(KEY_I_ROUTE_ID);
            String tinyUrl = this.getString(KEY_S_TINY_URL_ETA);
            s4Is4aProxy.updateETA(tinyUrl, status, name, eta, String.valueOf(lat), String.valueOf(lon), String.valueOf(speed), String.valueOf(accuracy), String.valueOf(routeID));
        }
    }
    
    protected void getNormalRoute()
    {
        try
        {
            doGetRoute(false, null);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            postModelEvent(EVENT_MODEL_EXIT_NAV);
        }
    }

    protected void doPlayAudio()
    {
        if (!MediaManager.getInstance().getAudioPlayer().isPlaying() && !isAudioDisabled())
        {
            NavState navState = NavSdkNavEngine.getInstance().getCurrentNavState();
            Preference audioTypePref = DaoManager.getInstance().getTripsDao()
                    .getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
            int audioPreference = -1;
            if (audioTypePref != null)
            {
                audioPreference = audioTypePref.getIntValue();
            }
            if (navState != null)
            {
                if (audioPreference == Preference.AUDIO_DIRECTIONS_ONLY
                        || audioPreference == Preference.AUDIO_DIRECTIONS_TRAFFIC)
                {
                    navigationProxy.sendGuidanceRequest();
                }

                if (audioPreference == Preference.AUDIO_TRAFFIC_ONLY || audioPreference == Preference.AUDIO_DIRECTIONS_TRAFFIC)
                {
                    // navigationProxy.sendTrafficInfoRequest(-1);
                    // TODO:
                    // NavSDK is adding API for replay traffic ahead audio.
                    // When it's finished, we will integrate with it.
                }
            }
        }
    }

    private void doCancelAvoidIncident()
    {
        if (avoidIncidentRequestId != -1)
        {
            navigationProxy.sendCancelAvoidIncidentRequest(avoidIncidentRequestId);
            avoidIncidentRequestId = -1;
        }
    }

    private void doAvoidIncident()
    {
        // perform traffic reroute
        TnLocation[] locations = new TnLocation[3];
        for (int i = 0; i < locations.length; i++)
        {
            locations[i] = new TnLocation("");
        }
        int locationCount = LocationProvider.getInstance().getCurrentLocation(locations, 3,
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if (locationCount == 0)
        {
            TnLocation lastKnownLocation = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);

            // last known location will never be null at this time.
            // Or how can user enter navigation?
            for (int i = 0; i < locations.length; i++)
            {
                locations[i].set(lastKnownLocation);
            }
        }
        else if (locationCount < 3)
        {
            for (int i = locationCount; i < 3; i++)
            {
                locations[i].set(locations[0]);
            }
        }

        locationCount = 3;
        put(KEY_B_IS_AVOID_INCIDENT, true);
        //use the routeId as the requestId for avoid incident
        avoidIncidentRequestId = NavSdkRouteWrapper.getInstance().getCurrentRouteId();
        navigationProxy.sendAvoidIncidentRequest(avoidIncidentRequestId);
    }

    protected void doGetRoute(boolean isAd, Address adAddress)
    {
        NavsdkAudioService.getInstance().resume();
        
        int routeID = getInt(KEY_I_ROUTE_ID);
        this.put(KEY_I_GET_ROUTE_TYPE, ID_TYPE_GET_ROUTE);
        if ((isAd && adAddress != null))
        {
            this.put(KEY_O_ADDRESS_DEST, adAddress);
            this.put(KEY_I_GET_ROUTE_TYPE, ID_TYPE_GET_AD_ROUTE);
            if(isEndTripShown && !getBool(KEY_B_IS_FROM_SEARCH_ALONG))
            {
                TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
                tripsDao.setNormalTrip(adAddress);
                tripsDao.store();
            }
            else
            {
                this.put(KEY_B_IS_FROM_SEARCH_ALONG, true);

                TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
                tripsDao.setDetourTrip(adAddress);
                tripsDao.store();
                
                if (tripLog != null)
                {
                    tripLog.setIsEndedByDetour(true);
                }
            }
            
            routeID = -1;
        }
        if (routeID == -1)
        {
            int routeStyle = DaoManager.getInstance().getTripsDao().getIntValue(Preference.ID_PREFERENCE_ROUTETYPE);
            int avoidSetting;
            if (routeStyle == Route.ROUTE_PEDESTRIAN)
            {
                avoidSetting = Preference.AVOID_CARPOOL_LANES;
            }
            else
            {
                avoidSetting = DaoManager.getInstance().getTripsDao().getIntValue(Preference.ID_PREFERENCE_ROUTE_SETTING);
            }
            navigationProxy.requestDynamicRoute((Address) this.get(KEY_O_ADDRESS_DEST), routeStyle, avoidSetting);
        }
        else
        {
            navigationProxy.requestRouteChoicesSelection("" + routeID, false);
        }
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_NAV_ROUTE_SUMMARY);
    }

    protected void releaseDelegate()
    {
        isModelReleased = true;
        NavSdkNavEvent navInfo = (NavSdkNavEvent) get(KEY_O_NAVEVENT);
        if (arrivalConfirmationMisLog != null && navInfo != null)
        {
            arrivalConfirmationMisLog.setDistanceToDest(navInfo.getDistanceToDest());
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, null, new Object[]
            { arrivalConfirmationMisLog });
        }

        if (tripLog != null)
        {
            tripLog.endTrip();
            tripLog = null;
        }

        stop();

        NavAdManager.getInstance().stop();

        enableDwfNotification(true);
        NotificationManager.getInstance().cancelNotification();

        TeleNavDelegate.getInstance().unregisterApplicationListener(this);
        TeleNavDelegate.getInstance().unregisterScreenStateListener(this);
        NavBottomStatusBarHelper.getInstance().setIsNoGps(false);
        NavBottomStatusBarHelper.getInstance().setIsOutofCoverage(false);
        NavBottomStatusBarHelper.getInstance().setIsOverSpeedLimit(false);
        NavRunningStatusProvider.getInstance().setIsOnBoard(false);
        NavServiceManager.getNavService().setRouteId(null);

        RegionUtil.getInstance().setRegionChangeListener(null);
        
        stopBacklight();
        
        Runnable run = new Runnable()
        {
            public void run()
            {
                final Context context = AndroidPersistentContext.getInstance().getContext();
                Activity activity = (Activity) context;
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            }
        };

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
        
        //disable the dimming of the screen per Matt. Enable it once get a better idea.
        /*Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                synchronized (this)
                {
                    try
                    {
                        this.wait(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                TnBacklightManagerImpl.getInstance().setDefaultBrightness(0);
            }
        });
        thread.start();*/
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        NavSdkNavEngine navEngine = NavSdkNavEngine.getInstance();
        if (navEngine.isRunning())
        {
            long t = System.currentTimeMillis();
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_NET_ERROR_STAMP,
                String.valueOf(t));
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
        }

        String action = proxy.getRequestAction();
        if (IServerProxyConstants.ACT_SYNC_PURCHASE.equalsIgnoreCase(action))
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringEntry.RES_LABEL_SYNC_PURCHASE_ERROR_MESSAGE, IStringEntry.FAMILY_ENTRY);
            showErrorMessage(errorMessage, EVENT_MODEL_ACCOUNT_ERROR);
        }
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        NavSdkNavEngine navEngine = NavSdkNavEngine.getInstance();
        if (navEngine.isRunning())
        {
            long t = System.currentTimeMillis();
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_TANS_ERROR_STAMP,
                String.valueOf(t));
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
        }

        if (proxy.getStatus() == IServerProxyConstants.INVALID_IDENTITY)
        {
            this.postModelEvent(EVENT_MODEL_INVALID_IDENTITY);
            return;
        }

        if (IServerProxyConstants.ACT_SYNC_PURCHASE.equalsIgnoreCase(proxy.getRequestAction()))
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy) proxy;
            if (syncPurchaseProxy.isNeedLogin())
            {
                handleAccountFatalError();
            }
            else
            {
                postModelEvent(EVENT_MODEL_EXIT_NAV);
            }
            return;
        }

        this.fetch(KEY_O_AD_ADDRESS);
        String action = proxy.getRequestAction();
        if (IServerProxyConstants.ACT_INCIDENT_REPORT.equals(action)
                || IServerProxyConstants.ACT_GET_MISSING_AUDIO.equals(action))
        {
            return;
        }

        if (action.equalsIgnoreCase(IServerProxyConstants.ACT_SET_ROUTE_CHOICES_SELECTION)
                || action.equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_SET_ROUTE_CHOICES_SELECTION)
                || action.equalsIgnoreCase(IServerProxyConstants.ACT_CHUNKED_DYNAMIC_ROUTE))
        {
            if (tripLog != null)
            {
                tripLog.setIsEndedByDetour(false);
            }
            if (getBool(KEY_B_IS_FROM_SEARCH_ALONG))
            {
                this.put(KEY_B_IS_DETOURE_ERROR, true);
            }
        }

        if (INavSdkProxyConstants.ACT_NAV_NAVIGATION_DEVIATION.equalsIgnoreCase(action))
        {
            String errorMessage = proxy.getErrorMsg();
            if (errorMessage == null || errorMessage.length() == 0)
            {
                errorMessage = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
            }
            if (getBool(KEY_B_IS_FROM_SEARCH_ALONG))
            {
                this.put(KEY_B_IS_DETOURE_ERROR, true);
            }

            showErrorMessage(errorMessage, EVENT_MODEL_ROUTING_ERROR);
            doProcessBeforeForceClose(action);
            return;
        }
        
        if(INavSdkProxyConstants.ACT_NAV_AVOID_TRAFFIC_INCIDENT.equals(action))
        {
		    avoidIncidentRequestId = -1;
            avoidIncidentError();
            return;
        }
        
        if (INavSdkProxyConstants.ACT_NAV_NAVIGATE_USING_ROUTE.equals(action))
        {
            String errorMessage = proxy.getErrorMsg();
            if (errorMessage == null || errorMessage.length() == 0)
            {
                errorMessage = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
            }
            showErrorMessage(errorMessage, EVENT_MODEL_ROUTING_ERROR);
            return;
        }

        MapVehiclePositionService.getInstance().setVehiclePositionCallback(null);
        MapVehiclePositionService.getInstance().pause();
        super.transactionError(proxy);
    }

    protected void doProcessBeforeForceClose(String action)
    {
        if (IServerProxyConstants.ACT_SET_ROUTE_CHOICES_SELECTION.equalsIgnoreCase(action)
                || IServerProxyConstants.ACT_CHUNKED_SET_ROUTE_CHOICES_SELECTION.equalsIgnoreCase(action)
                || IServerProxyConstants.ACT_CHUNKED_DYNAMIC_ROUTE.equalsIgnoreCase(action))
        {
            if (tripLog != null)
            {
                tripLog.setIsEndedByDetour(false);
            }
        }

        this.fetch(KEY_O_AD_ADDRESS);
        MapVehiclePositionService.getInstance().setVehiclePositionCallback(null);
        MapVehiclePositionService.getInstance().pause();
    }

    protected void showErrorMessage(String errorMessage, int modelEvent)
    {
        this.put(ICommonConstants.KEY_S_ERROR_MESSAGE, errorMessage);
        this.postModelEvent(modelEvent);
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        // model already released, no need to handle network response
        if (isModelReleased)
            return;

        String action = proxy.getRequestAction();

        if (action.equalsIgnoreCase(INavSdkProxyConstants.ACT_NAV_NAVIGATE_USING_ROUTE)
                || action.equalsIgnoreCase(INavSdkProxyConstants.ACT_NAV_START_NAVIGATION))
        {
            int routeID = getInt(KEY_I_ROUTE_ID);
            if(routeID == -1)
            {
                boolean isOnBoard = !NetworkStatusManager.getInstance().isConnected();
                NavRunningStatusProvider.getInstance().setIsOnBoard(isOnBoard);
            }
            // Comment for navsdk
            MisLogManager mislogManager = MisLogManager.getInstance();
            if (tripLog != null)
            {
                tripLog.endTrip();
                tripLog = null;
            }

            if(NavSdkRouteWrapper.getInstance().getCurrentRoute() != null && this.getBool(KEY_B_IS_FROM_SEARCH_ALONG))
            {
                //This will have a condition that detour destination is not too far because if it is too long, the distance is not valid
                KontagentAssistLogger.ktLogNavigationDistance(NavSdkRouteWrapper.getInstance().getCurrentRoute().getLength());
            }
            if (mislogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_TRIP_SUMMARY)
                    && NavSdkRouteWrapper.getInstance().getCurrentRouteId() != -1)
            {
                tripLog = mislogManager.getFactory().createTripSummaryMisLog();
                tripLog.setzRouteID(NavSdkRouteWrapper.getInstance().getCurrentRouteId());
                tripLog.startTrip((Address) get(KEY_O_ADDRESS_DEST));
                // TODO use route planing data
                // tripLog.setInitialEta(currentRoute.calcETA(false, 0));
                // tripLog.setInitialTripDistance(currentRoute.getLength() * 9119 / 8192);
                tripLog.setTimestamp(System.currentTimeMillis());
                mislogManager.storeMisLog(tripLog);
            }
            if (mislogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_ARIVALl_CONFIRMATION))
            {
                arrivalConfirmationMisLog = (ArrivalConfirmationMisLog) mislogManager.getFactory()
                        .createArrivalConfirmationMisLog();
                mislogManager.storeMisLog(arrivalConfirmationMisLog);
            }
            
            //to avoid blocking event bus thread
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    startNav();
                }
            });
            t.start();
        }
        else if (action.equalsIgnoreCase(INavSdkProxyConstants.ACT_NAV_NAVIGATION_STATUS))
        {
            NavSdkNavEngine.getInstance().handleEvent(((NavSdkNavigationProxy) proxy).getNavEvent());
        }
        else if (action.equalsIgnoreCase(INavSdkProxyConstants.ACT_NAV_SPEED_LIMIT_EXCEEDED))
        {
            // show the speed limit using the NavSpeedLimitComponent
            NavSdkNavEvent navEvent = new NavSdkNavEvent(NavSdkNavEvent.TYPE_NAVSDK_SPEED_LIMIT_EXCEEDED);
            NavSdkNavEngine.getInstance().handleEvent(navEvent);
        }
        else if (action.equalsIgnoreCase(INavSdkProxyConstants.ACT_NAV_CANCEL_LIMIT_EXCEEDED))
        {
            // hide the speed limit using the NavSpeedLimitComponent
            NavSdkNavEvent navEvent = new NavSdkNavEvent(NavSdkNavEvent.TYPE_NAVSDK_CANCEL_LIMIT_EXCEEDED);
            NavSdkNavEngine.getInstance().handleEvent(navEvent);
        }
        else if (action.equalsIgnoreCase(INavSdkProxyConstants.ACT_NAV_VEHICLE_POSITION))
        {
            handleVehiclePostion(((NavSdkNavigationProxy) proxy).getVehiclePoistion());
        }
        else if (action.equalsIgnoreCase(INavSdkProxyConstants.ACT_VOICE_GUIDANCE))
        {
            handleVoiceGuidance(((NavSdkNavigationProxy) proxy).getVoiceGuidance());
        }
        else if (INavSdkProxyConstants.ACT_NAV_NAVIGATION_DEVIATION.equalsIgnoreCase(action))
        {
            boolean isOnBoard = !NetworkStatusManager.getInstance().isConnected();
            NavRunningStatusProvider.getInstance().setIsOnBoard(isOnBoard);
            
            if (!isActivated())
            {
                AbstractController currentController = AbstractController.getCurrentController();
                if (currentController != null)
                {
                    // @deprecated: hack
                    currentController.handleModelEvent(EVENT_MODEL_BACK_TO_MOVING_MAP);
                }
            }
            else if (!isNavMapScreenShow())
            {
                postModelEvent(EVENT_MODEL_BACK_TO_MOVING_MAP);
            }
            if (tripLog != null)
            {
                tripLog.increaseNewRouteDevCount();
            }

            checkInteractionMode();
        }
        else if (IServerProxyConstants.ACT_SYNC_PURCHASE.equalsIgnoreCase(action))
        {
            ISyncPurchaseProxy syncPurchaseProxy = (ISyncPurchaseProxy) proxy;
            if (syncPurchaseProxy.isNeedLogin())
            {
                String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEntry.RES_LABEL_SYNC_PURCHASE_ERROR_MESSAGE, IStringEntry.FAMILY_ENTRY);
                doProcessBeforeForceClose(action);
                showErrorMessage(errorMessage, EVENT_MODEL_ACCOUNT_ERROR_FATAL);
                return;
            }

            if (FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_DYNAMIC_ROUTE) == FeaturesManager.FE_UNPURCHASED)
            {
                this.postModelEvent(EVENT_MODEL_EXIT_AND_PURCHASE);
            }
            else
            {
                int type = this.getInt(KEY_I_GET_ROUTE_TYPE);
                if (type == ID_TYPE_GET_ROUTE || type == ID_TYPE_GET_AD_ROUTE)
                {
                    this.postModelEvent(EVENT_MODEL_CONTINUE_GET_ROUTE);
                }
                else
                {
                    String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringEntry.RES_LABEL_SYNC_PURCHASE_ERROR_MESSAGE, IStringEntry.FAMILY_ENTRY);
                    showErrorMessage(errorMessage, EVENT_MODEL_ACCOUNT_ERROR);
                }
            }
        }
        else if (INavSdkProxyConstants.ACT_NAV_TRAFFIC_INCIDENTS_AHEAD.equals(action))
        {
            this.handleTrafficAlert(((NavSdkNavigationProxy) proxy).getTrafficAlertEvent());
        }
        else if(INavSdkProxyConstants.ACT_NAV_AVOID_TRAFFIC_INCIDENT.equals(action))
        {
       		avoidIncidentRequestId = -1;
            remove(KEY_B_IS_AVOID_INCIDENT);
            checkInteractionMode();
            if(getState() != STATE_MOVING_MAP)
            {
                postModelEvent(EVENT_MODEL_SHOW_NAV_MAP);
            }
        }
    }

    private void avoidIncidentError()
    {
        this.put(KEY_S_COMMON_MESSAGE,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_NO_BETTER_ROUTE, IStringNav.FAMILY_NAV));
        this.postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
    }

    private void checkInteractionMode()
    {
        if (MapContainer.getInstance().getInteractionMode() != IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE)
        {
            MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getFasterTransitionTime());
            MapContainer.getInstance().followVehicle();
            MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getTransitionTime());
        }
    }

    private synchronized void startNav()
    {
        // model is already release, no need to start navigation
        if (isModelReleased)
            return;


        Runnable run = new Runnable()
        {
            public void run()
            {
               
                final Context context = AndroidPersistentContext.getInstance().getContext();
                Activity activity = (Activity) context;
               
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            }
        };
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(run);
       
        getNavParameter().alertEvent = null;

        DynamicNavInfoRequester.getInstance().reset();

        isEndTripShown = false;
        put(KEY_B_IS_ARRIVE_DESTINATION, false);
        NavSdkNavEngine navEngine = NavSdkNavEngine.getInstance();

        AudioPlayer player = MediaManager.getInstance().getAudioPlayer();
        // FIXME: it's a little ugly.
        long timeout = 15000;
        startTime = System.currentTimeMillis();
        while (player.isPlaying() && (System.currentTimeMillis() - startTime) < timeout)
        {
            try
            {
                this.wait(100);
            }
            catch (InterruptedException e)
            {

            }
        }
        
        if (isModelReleased)
            return;

        NavServiceManager.getNavService().setRouteId("" + getInt(KEY_I_ROUTE_ID));

        if (navEngine.isRunning())
        {
            navEngine.stop();
        }
        navEngine.addListener(this);
        
        if (!DaoManager.getInstance().getBillingAccountDao().isPremiumAccount())
        {
            NavAdManager.getInstance().start();
        }

        // model is already release, no need to start navigation
        if (isModelReleased)
        {
            stop();
            return;
        }
        
        navEngine.start();

        // Add lister for nav status update.
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_NAV_NAVIGATION_STATUS);
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_NAV_SPEED_LIMIT_EXCEEDED);
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_NAV_VEHICLE_POSITION);
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_VOICE_GUIDANCE);
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_NAV_CANCEL_LIMIT_EXCEEDED);
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_NAV_NAVIGATION_DEVIATION);
        navigationProxy.registerListener(INavSdkProxyConstants.ACT_NAV_TRAFFIC_INCIDENTS_AHEAD);

        isCurrentConnected = NetworkStatusManager.getInstance().isConnected();
        
        if(!isCurrentConnected)
        {
            handleNoCellCoverage();
        }
        
        NetworkStatusManager.getInstance().addStatusListener(this);
        SimpleConfigDao simpleConfigDao = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao();
        boolean enableSharing = simpleConfigDao.getBoolean(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_ENABLED);
        if(enableSharing && !TextUtils.isEmpty(this.getString(KEY_S_TINY_URL_ETA)))
        {
            Notifier.getInstance().addListener(this);
        }
        MapVehiclePositionService.getInstance().setVehiclePositionCallback(this);

        if (this.cameraDeclinationBeforeJunctionViewLandscap == 0 || this.cameraDeclinationBeforeJunctionViewPortrait == 0)
        {
            initCameraDeclination();
        }

        if (getState() != STATE_MOVING_MAP)
        {
            postModelEvent(EVENT_MODEL_SHOW_NAV_MAP);
        }
        
        /*if (backlightPreference != Preference.BACKLIGHT_OFF)
        {
            backlighManager = TnBacklightManagerImpl.getInstance();
            NavsdkBacklightService.getInstance().setTurnListener(this);
            //disable the dimming of the screen per Matt. Enable it once get a better idea.
            TnBacklightManagerImpl.getInstance().setDefaultBrightness();
        }*/
    }

    private void initCameraDeclination()
    {
        this.cameraDeclinationBeforeJunctionViewPortrait = getDefaultCameraDeclination(true);
        this.cameraDeclinationBeforeJunctionViewLandscap = getDefaultCameraDeclination(false);
        Vector defaultDeclinationBeforeJunctionView = new Vector();
        defaultDeclinationBeforeJunctionView.add(cameraDeclinationBeforeJunctionViewPortrait);
        defaultDeclinationBeforeJunctionView.add(cameraDeclinationBeforeJunctionViewLandscap);
        this.put(KEY_V_DEFAULT_DECLINATION_BEFORE_JUNCTION_VIEW, defaultDeclinationBeforeJunctionView);
        MapContainer.getInstance().setCameraDeclination(getCameraDeclinationBeforeJuncitionView());
    }
    
    private float getCameraDeclinationBeforeJuncitionView()
    {
        boolean isPortrait = (((AbstractTnUiHelper) (AbstractTnUiHelper
                .getInstance())).getOrientation()) == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
        
        if(isPortrait)
        {
            return cameraDeclinationBeforeJunctionViewPortrait;
        }
        else
        {
            return cameraDeclinationBeforeJunctionViewLandscap;
        }
    }

    public void eventUpdate(NavSdkNavEvent event)
    {
        int eventType = event.getEventType();
        if (Logger.DEBUG)
            Logger.log(Logger.INFO, this.getClass().getName(), "handle nav event, type = " + eventType);

        switch (eventType)
        {
            case NavSdkNavEvent.TYPE_NAVSDK_ON_ROUTE:
            case NavSdkNavEvent.TYPE_NAVSDK_OFF_ROUTE: // ADI event is also a info event
            {
                NavSdkNavEvent navInfoEvent = (NavSdkNavEvent) event;
                handleNavEvent(navInfoEvent);
                break;
            }
            case NavSdkNavEvent.TYPE_NAVSDK_SPEED_LIMIT_EXCEEDED:
            {
                NavParameter navParam = getNavParameter();
                navParam.isSpeedLimitExceeded = true;
                this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                break;
            }
            case NavSdkNavEvent.TYPE_NAVSDK_CANCEL_LIMIT_EXCEEDED:
            {
                NavParameter navParam = getNavParameter();
                navParam.isSpeedLimitExceeded = false;
                this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                break;
            }
            case NavSdkNavEvent.TYPE_NAVSDK_NO_DESTINATION:
            {
                break;
            }
            case NavSdkNavEvent.TYPE_NAVSDK_ROUTING:
            {
                break;
            }
            case NavSdkNavEvent.TYPE_NAVSDK_SHOULD_REROUTE:
            {
                break;
            }
            default:
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "unhandled event, type = " + eventType);
            }
        }
    }

    /**
     * NavState might be null in runtime, so we have to add this flag
     */
    private void handleGpsEvent(int gpsCount)
    {
        if (gpsCount >= 0)
        {
            if (isFirstTimeLocated)
            {
                isCurrentlyNoGPS = false;
                isShownNoGPSIcon = true;
                lastGPSStatusChangeTime = -1;
            }

            if (this.isCurrentlyNoGPS)
            {
                // set last status change time
                lastGPSStatusChangeTime = System.currentTimeMillis();
            }

            this.isCurrentlyNoGPS = false;
            if (isShownNoGPSIcon && System.currentTimeMillis() - this.lastGPSStatusChangeTime > GPS_STATUS_CHANGE_DELAY_TIME)
            {
                isShownNoGPSIcon = false;

                NavParameter param = getNavParameter();
                //
                param.isNoSatellite = false;
                this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                // http://jira.telenav.com:8080/browse/TNANDROID-367
                // we will not play the gps regain audio at firstime when first gps is network or lastknown
                if (!isFirstTimeLocated)
                {
                    // play audio "You now have GPS signal."
                    playStaticAudioInSequence(IAudioRes.JINGLES_FRIENDLY_BEEP);
                    playStaticAudioInSequence(IAudioRes.NEW_GPS_SIGNAL);
                }
            }
            isFirstTimeLocated = false;
        }
        else
        {
            if (!this.isCurrentlyNoGPS)
            {
                // set last status change time
                lastGPSStatusChangeTime = System.currentTimeMillis();
            }

            this.isCurrentlyNoGPS = true;
            if (!isShownNoGPSIcon && System.currentTimeMillis() - this.lastGPSStatusChangeTime > GPS_STATUS_CHANGE_DELAY_TIME)
            {
                isShownNoGPSIcon = true;

                NavParameter param = getNavParameter();
                //
                param.isNoSatellite = true;
                this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                // http://jira.telenav.com:8080/browse/TNANDROID-367
                // we will not play the gps lost audio at firstime when first gps is network or lastknown
                if (!isFirstTimeLocated)
                {
                    // play audio "Cannot get GPS signal in this area."
                    playStaticAudioInSequence(IAudioRes.JINGLES_WARNING_BEEP);
                    playStaticAudioInSequence(IAudioRes.NEW_NO_GPS_SIGNAL);
                    long t = System.currentTimeMillis();
                    ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_LOST_GPS_STAMP,
                        String.valueOf(t));
                    ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
                }
            }
        }

    }

    protected void playStaticAudioInSequence(int staticAudioId)
    {
        if (!isAudioDisabled())
        {
            Vector audioList = new Vector();
            AudioData audioData = AudioDataFactory.getInstance().createAudioData(staticAudioId);
            if (audioData != null)
            {
                AudioDataNode staticAudioNode = AudioDataFactory.getInstance().createAudioDataNode(audioData);
                audioList.addElement(staticAudioNode);

                MediaManager.getInstance().playAudio(audioList);
            }
        }
    }


    private boolean handleEndTripEvent(NavSdkNavEvent navEvent)
    {
        if (navEvent == null)
            return true;

//        if (isFirstTimeToHandleNavEvent)
//        {
//            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
//            String errMsg = bundle.getString(IStringNav.RES_ORIG_DEST_TOO_CLOSE, IStringNav.FAMILY_NAV);
//
//            if (getBool(KEY_B_IS_FROM_SEARCH_ALONG))
//            {
//                this.put(KEY_B_IS_DETOURE_ERROR, true);
//                showErrorMessage(errMsg, EVENT_MODEL_ROUTING_ERROR);
//            }
//            else
//            {
//                this.put(KEY_B_IS_VALID_SERVICE, false);
//                this.put(KEY_S_ERROR_MESSAGE, errMsg);
//                this.put(KEY_B_IS_CRITICAL_ERROR, true);
//                doProcessBeforeForceClose("");
//                this.postModelEvent(EVENT_MODEL_RTS_FAILED);
//            }
//
//            return false;
//        }

        NavParameter navParameter = getNavParameter();

        navParameter.currStreetName = "";
        navParameter.distanceToTurn = 0;
        navParameter.isAdi = false;
        navParameter.nextStreetAlias = navEvent.getNextStreetAlias();
        navParameter.nextStreetName = navEvent.getNextStreetName();

        navParameter.tripDist = navEvent.getDistanceToDest();
        navParameter.turnType = navEvent.getTurnType();
        navParameter.totalToDest = navEvent.getDistanceToDest();

        this.put(KEY_B_IS_ARRIVE_DESTINATION, true);

        if (!isActivated() && !ShareManager.getInstance().isSharing())
        {
            AbstractController currentController = AbstractController.getCurrentController();
            if (currentController != null && !(currentController instanceof DriveWithFriendsController))
            {
                currentController.handleModelEvent(EVENT_MODEL_BACK_TO_MOVING_MAP);
            }
        }

        updateDWFStatus(0, Friend.FriendStatus.ARRIVED.name(), 0, 0);
        logKtArrived();
        return true;
    }
    
    private void logKtArrived()
    {
        if(!hasLogNavArrival)
        {
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_NAVIGATION,
                KontagentLogger.NAVIGATION_ARRIVAL);
            KontagentAssistLogger.endLogNavigationDuration();
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
            try
            {
                if (dwfAidl != null && dwfAidl.getSharingIntent() != null)
                {
                    String userKey = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_KEY);
                    ArrayList<Friend> friends = DwfSliderPopup.getInstance().getFriends();
                    if (friends != null && friends.size() > 0)
                    {
                        float arrivedCount = 0f;
                        int total = friends.size();
                        for (int i = 0; i < total; i++)
                        {
                            Friend friend = friends.get(i);
                            //if friend is user, it should be thought as arrived even though his status is not updated
                            if((friend.getKey() != null && friend.getKey().equalsIgnoreCase(userKey)) || friend.getStatus() == FriendStatus.ARRIVED)
                            {
                                arrivedCount++;
                            }
                        }
                        float percent = arrivedCount / total;
                        KontagentAssistLogger.ktLogDwfArrivedStatus(percent);
                    }
                }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            hasLogNavArrival = true;
        }
    }
    
    private void updateDWFStatus(long eta, String status, double lat, double lon)
    {
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
        if (dwfAidl != null)
        {
            try
            {
                if (dwfAidl.getSharingIntent() != null)
                {
                    String addressDt = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
                    if(addressDt != null)
                    {
                        Address address = DwfUtil.jsonToAddress(addressDt);
                        
                        Address dest = (Address)this.get(KEY_O_ADDRESS_DEST);
                        if(address.getStop().getLat() == dest.getStop().getLat() && address.getStop().getLon() == dest.getStop().getLon())
                        {
                            dwfAidl.updateStatus(eta, status, lat, lon);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDriving(NavSdkNavEvent navEvent)
    {
        NavSdkRoute route = NavSdkRouteWrapper.getInstance().getCurrentRoute();

        /**
         * if route hasn't been retrieved from navsdk, needn't do the arrival check, the distance to dest is 0 !!!
         */
        if (route == null || route.isPartial())
        {
            return false;
        }

        boolean isArriveDest = this.getBool(KEY_B_IS_ARRIVE_DESTINATION);
        if (!isArriveDest)
        {
            if (navEvent != null && arrivalConfirmationMisLog != null)
            {
                arrivalConfirmationMisLog.logArrivalConfirmation((Address) get(KEY_O_ADDRESS_DEST), navEvent, false);
            }
        }
        if (navEvent != null)
            Logger.log(Logger.INFO, this.getClass().getName(), "dist to dest = " + navEvent.getDistanceToDest());

        if (navEvent != null && navEvent.getDistanceToDest() > 400 && navEvent.isHasDistantToDest())
        {
            if (tripLog != null)
            {
                tripLog.updateNavInfo(navEvent, false);
            }

            if (isArriveDest)
            {
                put(KEY_B_IS_ARRIVE_DESTINATION, false);
            }
        }
        else
        {
            if (tripLog != null)
            {
                tripLog.updateNavInfo(navEvent, isArriveDest);
            }

            boolean isAdi = false;
            
            TnLocation adiLocation = navEvent.getAdiLocation();
            if(adiLocation != null && adiLocation.getLatitude() != 0 && adiLocation.getLongitude() != 0)
            {
                isAdi = true;
            }
            
            boolean isEndTrip = false;
            
            if(navEvent != null && navEvent.getDistanceToDest() < 100 && navEvent.isHasDistantToDest())
            {
                isEndTrip = true;
            }
            else if(navEvent != null && navEvent.getDistanceToDest() < 200
                    && isAdi && navEvent.isHasDistantToDest())
            {
                isEndTrip = true;
            }
            
            if (isEndTrip)
            {
                if (Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "dist to dest = " + navEvent.getDistanceToDest());
                }

                isReachEndTrip = true;
                if (!isArriveDest)
                {
                    boolean isContinued = handleEndTripEvent(navEvent);
                    if (!isContinued)
                        return false;
                }
            }

            if (isArriveDest)
            {
                if (!isLoged)
                {
                    if (MisLogManager.getInstance().getFilter().isTypeEnable(IMisLogConstants.TYPE_ARIVALl_CONFIRMATION))
                    {
                        if (arrivalConfirmationMisLog != null)
                        {
                            arrivalConfirmationMisLog.setIsArrival(true);
                            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, null,
                                new Object[]
                                { arrivalConfirmationMisLog });
                            isLoged = true;
                        }
                    }
                }

                if (isNavMapScreenShow())
                {
                    Logger.log(Logger.INFO, ILogConstants.LOG_NAVIGATION, "MovingMapModel :: Show End Trip");

                    if (navEvent != null)
                    {
                        Logger.log(Logger.INFO, ILogConstants.LOG_NAVIGATION,
                            "distance to dest = " + navEvent.getDistanceToDest());
                    }

                    postModelEvent(EVENT_MODEL_SHOW_END_TRIP);
                }

                TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS);
                String drivingStatus = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_SHARE_ETA_ARRIVED, IStringCommon.FAMILY_COMMON);
                SimpleConfigDao simpleConfigDao = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao();
                boolean enableSharing = simpleConfigDao.getBoolean(SimpleConfigDao.KEY_IS_REAL_TIME_SHARE_ENABLED);
                if(enableSharing)
                {
                    requestUpdateETA(drivingStatus, location, String.valueOf(0));
                }
                int speed = 0;
                if (location != null)
                {
                    speed = location.getSpeed();
                }
                speed *= StringConverter.DM6TOSPEED[1]; // US unit
                speed >>= StringConverter.DM6TOSPEED_SHIFT;

                // changed from 5 to 20 to fix bug TNSIXTWO-935
                int speedThreshold = 20; // mile

                if (endTripTime <= 0 || speed > speedThreshold || ShareManager.getInstance().isSharing())
                {
                    endTripTime = System.currentTimeMillis();
                }
                else if (speed <= speedThreshold)
                {
                    DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
                    if (dwfAidl != null)
                    {
                        try
                        {
                            if (dwfAidl.getSharingIntent() != null)
                            {
                                stop();
                                if (!isGoToDwf && DwfSliderPopup.getInstance().isShowing() /**&& (System.currentTimeMillis() - endTripTime) >= 60 * 1000*/)
                                {
                                    isGoToDwf = true;
                                    doGoToDwf();
                                }
                            }
                        }
                        catch (RemoteException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    
                    if ((System.currentTimeMillis() - endTripTime) >= 5 * 60 * 1000)
                    {
                        doBackHome();
                    }
                }

                return false;
            }
        }

        return true;
    }
    
    private void doGoToDwf()
    {
        postModelEvent(EVENT_MODEL_GOTO_DWF);
    }

    private void doBackHome()
    {
        isBackToHome = true; 
        if (!TeleNavDelegate.getInstance().isActivate())
        {
            NavSdkNavEngine navEngine = NavSdkNavEngine.getInstance();
            if (navEngine.isRunning())
            {
                navEngine.stop();
            }
            LocationProvider.getInstance().stop();
        }
        if (!isActivated() && !ShareManager.getInstance().isSharing())
        {
            AbstractController currentController = AbstractController.getCurrentController();
            if (currentController != null)
            {
                currentController.handleModelEvent(EVENT_MODEL_BACK_TO_MOVING_MAP);
            }
        }
        postModelEvent(EVENT_MODEL_EXIT_NAV);
    }

    private void handleNavEvent(NavSdkNavEvent navEvent)
    {
        this.put(KEY_O_NAVEVENT, navEvent);
        // isReachEndTrip = false;
        isDriving = checkDriving(navEvent);
        // fix TNANDROID-184, nav param should pass through even is end trip mode.
        // if (!isContinue)
        // return;
        NavParameter navParameter = getNavParameter();
        navParameter.distanceToTurn = navEvent.getDistanceToTurn();
        navParameter.nextStreetName = navEvent.getNextStreetName();
        navParameter.currStreetName = navEvent.getCurrentStreetName();
        navParameter.turnType = navEvent.getTurnType();
        navParameter.eta = navEvent.getEstimatedTime() + System.currentTimeMillis();
        
        this.updateDWFStatus(navEvent.getEstimatedTime() / 1000, Friend.FriendStatus.DRIVING.name(), 0, 0);
        
        navParameter.totalToDest = navEvent.getDistanceToDest();
        navParameter.nextStreetAlias = navEvent.getNextStreetAlias();
        shareETA = navEvent.getEstimatedTime()/1000;
        TnNavLocation adiLocation = navEvent.getAdiLocation();
        if (adiLocation == null || adiLocation.getLatitude() == 0 || adiLocation.getLongitude() == 0)
        {
            navParameter.isAdi = false;
        }
        else
        {
            navParameter.isAdi = true;
            navParameter.adiLat = adiLocation.getLatitude();
            navParameter.adiLon = adiLocation.getLongitude();
        }

        navParameter.speedLimit = navEvent.getSpeedLimit();
        navParameter.exitName = navEvent.getExitName();
        navParameter.exitNumber = (byte) navEvent.getExitNum();

        NavRunningStatusProvider.getInstance().setDistanceToDestination(navEvent.getDistanceToDest());
        
        if (navEvent.getLaneTypes() != null)
        {
            int[] currentLaneInfo = navEvent.getLaneInfos();
            RouteTurnType[] currentLaneTypes = navEvent.getLaneTypes();

            Preference laneAssistPreference = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getPreference(
                Preference.ID_PREFERENCE_LANE_ASSIST);

            boolean isLaneAssistEnabled = false;
            if (laneAssistPreference != null)
            {
                isLaneAssistEnabled = laneAssistPreference.getIntValue() == Preference.LANE_ASSIST_ON;
            }

            int laneAssistValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_NAV_LANE_ASSIST);
            boolean laneAssistFeatureOn = (laneAssistValue == FeaturesManager.FE_ENABLED || laneAssistValue == FeaturesManager.FE_PURCHASED);

            if (!laneAssistFeatureOn)
            {
                isLaneAssistEnabled = false;
            }

            if (isLaneAssistEnabled && currentLaneInfo != null && currentLaneInfo.length > 1)
            {
                navParameter.laneInfos = currentLaneInfo;
                navParameter.laneTypes = currentLaneTypes;
            }
            else
            {
                navParameter.laneInfos = null;
                navParameter.laneTypes = null;
            }
        }
        else
        {
            navParameter.laneInfos = null;
            navParameter.laneTypes = null;
        }

        navParameter.nextSegmentIndex = navEvent.getNextTurnIndex();
        
        navParameter.nextNextSteetName = null;
        navParameter.nextNextExitName = null;
        navParameter.nextNextExitNumber = 0;
        navParameter.nextNextTurnType = -1;
        NavSdkRoute route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
        if (route != null)
        {
            Segment[] segments = route.getSegments();
            if (segments != null && segments.length > 0)
            {
                int nextNextTurnIndex = navParameter.nextSegmentIndex + 1;
                if (nextNextTurnIndex >= 0 && nextNextTurnIndex < segments.length)
                {
                    Segment nextNextSegment = segments[nextNextTurnIndex];
                    if (nextNextSegment != null
                            && !isDestinationTurnType(nextNextSegment.getTurnType()))
                    {
                        navParameter.nextNextTurnType = nextNextSegment.getTurnType();
                        navParameter.nextNextSteetName = nextNextSegment.getStreetName();
                        navParameter.nextNextExitName = nextNextSegment.getExitName();
                        navParameter.nextNextExitNumber = nextNextSegment.getExitNumber();
                    }
                }
            }
        }
        if(isBackToHome)
            return;
        if (!TeleNavDelegate.getInstance().isActivate())
        {
            Logger.log(Logger.INFO, this.getClass().getName(),
                "telenavDelegate is not active, turntype = " + navEvent.getTurnType());
            if (navEvent.getTurnType() > -1)
            {
                enableDwfNotification(false);
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
                    Preference.ID_PREFERENCE_DISTANCEUNIT);
                String distToTurn = converter.convertDistanceMeterToMile(navEvent.getDistanceToTurn(), systemUnits);
                String promptStr = getTurnDiscription(navEvent.getTurnType());
                promptStr = distToTurn + " " + promptStr + " " + navEvent.getNextStreetName();
                NotificationManager.getInstance().showNotification(getDestAddressStr(), promptStr);
            }
        }
        else
        {
            enableDwfNotification(false);
            String title = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringNav.RES_BACK_TO_NAV, IStringNav.FAMILY_NAV);
            NotificationManager.getInstance().showNotification(title, "");
        }

        int tempDistToTurn = navEvent.getDistanceToTurn();
        int sysOfUnits = 1; // feet
        // convert 1e-5 degrees to short units
        tempDistToTurn *= StringConverter.DEG2SHORT[sysOfUnits];
        tempDistToTurn = tempDistToTurn >> DataUtil.SHIFT;

        long tempDistToDest = navEvent.getDistanceToDest();
        // convert 1e-5 degrees to short units
        tempDistToDest *= StringConverter.DEG2SHORT[sysOfUnits];
        tempDistToDest = tempDistToDest >> DataUtil.SHIFT;
        
        int junctionViewValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_NAV_JUNCTION_VIEW);
        boolean isJunctionViewEnabled = (junctionViewValue == FeaturesManager.FE_ENABLED || junctionViewValue == FeaturesManager.FE_PURCHASED);               
        if (isJunctionViewEnabled && !navParameter.isAdi)
        {
            handleMapModeAtTurn(tempDistToTurn, tempDistToDest, navParameter, navEvent.isHasDistantToDest());
        }

        long currentTime = System.currentTimeMillis();
        // Fix TNANDROID-904
        if ((currentTime - lastTripTimeDot) > TRIP_TIME_DOT_INTERVAL)
        {
            DaoManager.getInstance().getNavExitAbnormalDao().setTripTimeDot(currentTime);
            lastTripTimeDot = currentTime;
        }

        int backlightPref = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
            Preference.ID_PREFERENCE_BACKLIGHT);
        if (backlightPref == Preference.BACKLIGHT_TURNS)
        {
            if (tempDistToTurn < 500 && (((currentTime - lastShowBacklightTime) > 20000)))// fix bug 15760...
            {
                // startBacklight();
                lastShowBacklightTime = currentTime;
            }
        }

        isFirstTimeToHandleNavEvent = false;

        this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }
    
    private void handleMapModeAtTurn(long tempDistToTurn, long tempDistToDest, NavParameter navParameter, boolean hasDistanceToDest)
    {
        int turnType = navParameter.turnType;

        if (turnType == Route.TURN_TYPE_ROUNDABOUT_EXIT)
        {
            isRoundAbout = 1;
        }
        else if (isRoundAbout == 1)
        {
            isRoundAbout = 2;
        }

        if (!this.isActivated())
        {
            return;
        }

        if (tempDistToTurn < 400 && (tempDistToDest > 400 || !hasDistanceToDest))
        {
            if (System.currentTimeMillis() - activateTime < 2500)
            {
                return;
            }
            else
            {
                isRoundAbout = 0;
                if(turnType == Route.TURN_TYPE_ROUNDABOUT_EXIT)
                {
                    isRoundAbout = 1;
                }
                enterJunctionView();
                isAutoSwitchAtTurn = true;
            }
        }
        else if (isAutoSwitchAtTurn)
        {
            int length = 0;
            NavSdkRoute route = NavSdkRouteWrapper.getInstance().getCurrentRoute();
            int segmentLength = 0;
            int currentSegmentIndex = -1;
            if (route.getSegments().length > navParameter.nextSegmentIndex && navParameter.nextSegmentIndex > 0)
            {
                currentSegmentIndex = navParameter.nextSegmentIndex - 1;
            }

            if (currentSegmentIndex > -1)
            {
                segmentLength = route.getSegments()[currentSegmentIndex].getLength();
            }

            length = segmentLength - navParameter.distanceToTurn;

            if (isRoundAbout == 2 || length > 400)
            {
                exitJunctionView();
                isAutoSwitchAtTurn = false;
            }
        }
    }
    
    private void enterJunctionView()
    {
        if (IMapContainerConstants.INTERACTION_MODE_FOLLOW_VEHICLE == MapContainer.getInstance().getInteractionMode())
        {
            // we only show junction view when follow vehicle mode.
            if (!isInJunctionViewState && getBool(KEY_B_IS_MAP_INITIALIZED))
            {
                isInJunctionViewState = true;
                MovingMapModel.this.put(KEY_B_IS_JUNCTION_VIEW, isInJunctionViewState);
                zoomBeforeJunctionView = MapContainer.getInstance().getZoomLevel();
                MapContainer.getInstance().setZoomLevel(ZOOM_LEVEL_JUNCTION_VIEW);
                MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getTransitionTime());
                MapContainer.getInstance().setCameraDeclination(JUNCTION_VIEW_CAMERA_DECLINATION);
            }
        }
    }
    
    private void exitJunctionView()
    {
        if (isInJunctionViewState)
        {
            MapContainer.getInstance().setZoomLevel(zoomBeforeJunctionView);
            MapContainer.getInstance().setMapTransitionTime(MapContainer.getInstance().getTransitionTime());
            MapContainer.getInstance().setCameraDeclination(getCameraDeclinationBeforeJuncitionView());
            isInJunctionViewState = false;
            MovingMapModel.this.put(KEY_B_IS_JUNCTION_VIEW, isInJunctionViewState);
        }
    }

    private boolean isDestinationTurnType(byte turnType)
    {
        return turnType == Route.TURN_TYPE_DESTINATION_AHEAD || turnType == Route.TURN_TYPE_DESTINATION_LEFT
                || turnType == Route.TURN_TYPE_DESTINATION_RIGHT;
    }
    
    private float getDefaultCameraDeclination(boolean isPortrait)
    {
        float defaultCameraDeclination = 0;

        if (isPortrait)
        {
            if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_TINY)
            {
                defaultCameraDeclination = TINY_CAMERA_DECLINATION_PORTRAIT;
            }
            else if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
            {
                defaultCameraDeclination = SMALL_CAMERA_DECLINATION_PORTRAIT;
            }
            else if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
            {
                defaultCameraDeclination = MEDIUM_CAMERA_DECLINATION_PORTRAIT;
            }
            else
            {
                defaultCameraDeclination = LARGE_CAMERA_DECLINATION_PORTRAIT;
            }
        }
        else
        {
            if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_TINY)
            {
                defaultCameraDeclination = TINY_CAMERA_DECLINATION_LANDSCAP;
            }
            else if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_SMALL)
            {
                defaultCameraDeclination = SMALL_CAMERA_DECLINATION_LANDSCAP;
            }
            else if (ISpecialImageRes.getOpenglImageFamily() == ISpecialImageRes.FAMILY_MEDIUM)
            {
                defaultCameraDeclination = MEDIUM_CAMERA_DECLINATION_LANDSCAP;
            }
            else
            {
                defaultCameraDeclination = LARGE_CAMERA_DECLINATION_LANDSCAP;
            }
        }

        return defaultCameraDeclination;
    }

    public int getFixes(int numFixes, TnLocation[] data)
    {
        if (data == null || data.length < 1)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "getFixes(): data is null or empty");
            return 0;
        }
        int numFix = LocationProvider.getInstance().getGpsLocations(data, numFixes, true);

        Logger.log(Logger.INFO, this.getClass().getName(), "numFix:" + numFix);

        return numFix;
    }

    public int getLastKnownHeading()
    {
        TnLocation lastKnownData = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS);
        if (lastKnownData != null)
        {
            return lastKnownData.getHeading();
        }
        return 0;
    }

    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
            postModelEvent(EVENT_MODEL_LOCATION_DATA_RETRIEVED);
        }
        else
        {
            postModelEvent(EVENT_MODEL_LOCATION_DATA_FAILED);
        }
    }

    public NavSdkNavState getCurrentNavState()
    {
        NavParameter params = getNavParameter();
        NavSdkNavState navState = new NavSdkNavState();
        navState.setSegmentIndex(params.nextSegmentIndex - 1);
        navState.setDistanceToTurn(params.distanceToTurn);
        navState.setDistanceToDest(params.totalToDest);
        return navState;
    }

    public void handleTrafficAlert(TrafficAlertEvent alertEvent)
    {
        if (alertEvent == null)
        {
            NavParameter navParameter = getNavParameter();
            navParameter.alertEvent = null;
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
            return;
        }
        
        DaoManager daoManager = (DaoManager) DaoManager.getInstance();
        PreferenceDao preferenceDao = daoManager.getPreferenceDao();

        switch (alertEvent.getIncidentType())
        {
            case TrafficIncident.TYPE_CAMERA:
            {
                if (!isAlertNeeded(Preference.ID_PREFERENCE_TRAFFIC_CAMERA_ALERT))
                {
                    return;
                }
                break;
            }
            case TrafficIncident.TYPE_SPEED_TRAP:
            {
                if (!isAlertNeeded(Preference.ID_PREFERENCE_SPEED_TRAP_ALERT))
                {
                    return;
                }
                break;
            }
            default:
            {
                Preference trafficAlert = preferenceDao.getPreference(Preference.ID_PREFERENCE_TRAFFICALERT);
                if (trafficAlert == null || !isAlertNeeded(Preference.ID_PREFERENCE_TRAFFICALERT))
                {
                    return;
                }
                break;
            }
        }
        NavParameter navParameter = getNavParameter();
        navParameter.alertEvent = alertEvent;

        this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
    }
    
    private void handleVehiclePostion(VehiclePosition vehiclePos)
    {
        this.put(KEY_O_VEHICLE_POSITION, vehiclePos);
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
        if (dwfAidl != null)
        {
            try
            {
                if (dwfAidl.getSharingIntent() != null)
                {
                    dwfAidl.updateStatus(Integer.MIN_VALUE, "", (double)vehiclePos.getMatchedLocation().getLatitude(), (double)vehiclePos.getMatchedLocation().getLongitude());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void enableDwfNotification(boolean enable)
    {
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
        if (dwfAidl != null)
        {
            try
            {
                if (dwfAidl.getSharingIntent() != null)
                {
                    dwfAidl.enableNotification(enable, MainAppStatus.foreground.name());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void handleVoiceGuidance(VoiceGuidancePlayNotification voiceGuidance)
    {
        this.put(KEY_O_VOICE_GUIDANCE, voiceGuidance);
    }

    public void handleTrafficAudio(TrafficAudioEvent trafficAudioEvent)
    {
        Preference audioPreference = DaoManager.getInstance().getTripsDao()
                .getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        if (audioPreference != null
                && (audioPreference.getIntValue() == Preference.AUDIO_DIRECTIONS_ONLY || audioPreference.getIntValue() == Preference.AUDIO_NONE))
        {
            return;
        }

        if (trafficAudioEvent.getAudioType() == TrafficAudioEvent.TRAFFIC_AUDIO_TYPE_INCIDENT)
        {
            TrafficIncident incident = trafficAudioEvent.getTrafficIncident();
            this.playIncidentAudio(incident, trafficAudioEvent);
        }
    }

    protected boolean isAlertNeeded(short preferenceType)
    {
        String featureType = "";
        int preferenceOnValue = 0;
        boolean isFeatureEnabled = false;
        switch (preferenceType)
        {
            case Preference.ID_PREFERENCE_TRAFFIC_CAMERA_ALERT:
            {
                featureType = FeaturesManager.FEATURE_CODE_NAV_TRAFFIC_CAMERA;
                preferenceOnValue = Preference.TRAFFIC_CAMERA_ALERT_ON;
                break;
            }
            case Preference.ID_PREFERENCE_SPEED_TRAP_ALERT:
            {
                featureType = FeaturesManager.FEATURE_CODE_NAV_SPEED_TRAP;
                preferenceOnValue = Preference.SPEED_TRAP_ALERT_ON;
                break;
            }
            case Preference.ID_PREFERENCE_TRAFFICALERT:
            {
                preferenceOnValue = Preference.TRAFFIC_ALERT_ON;
                break;
            }
            default:
            {
                return false;
            }
        }

        if (featureType.trim().length() == 0)
        {
            isFeatureEnabled = true;
        }
        else
        {
            int featureValue = FeaturesManager.getInstance().getStatus(featureType);
            isFeatureEnabled = featureValue == FeaturesManager.FE_ENABLED || featureValue == FeaturesManager.FE_PURCHASED;
        }

        int preferenceValue = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(preferenceType);
        boolean result = (preferenceValue == preferenceOnValue) && isFeatureEnabled;

        return result;
    }

    private void playIncidentAudio(TrafficIncident incident, TrafficAudioEvent trafficAudioEvent)
    {
        if (incident == null)
            return;

        switch (incident.getIncidentType())
        {
            case TrafficIncident.TYPE_CAMERA:
            {

                if (!isAlertNeeded(Preference.ID_PREFERENCE_TRAFFIC_CAMERA_ALERT) || trafficAudioEvent == null)
                {
                    break;
                }
                if (!MediaManager.getInstance().getAudioPlayer().isPlaying() && !isAudioDisabled())
                {
                    MediaManager.getInstance().playStaticAudio(new int[]
                    { IAudioRes.JINGLES_WARNING_BEEP, IAudioRes.TRAFFIC_CAMERA_AHEAD });
                }
                break;
            }
            case TrafficIncident.TYPE_SPEED_TRAP:
            {

                if (!isAlertNeeded(Preference.ID_PREFERENCE_SPEED_TRAP_ALERT) || trafficAudioEvent == null)
                {
                    break;
                }

                if (MediaManager.getInstance().getAudioPlayer().isPlaying() || isAudioDisabled())
                {
                    break;
                }
                DaoManager daoManager = (DaoManager) DaoManager.getInstance();
                PreferenceDao preferenceDao = daoManager.getPreferenceDao();

                Preference distanceUnit = preferenceDao.getPreference(Preference.ID_PREFERENCE_DISTANCEUNIT);
                int sysOfUnits = Preference.UNIT_METRIC;
                if (distanceUnit != null)
                {
                    sysOfUnits = distanceUnit.getIntValue();
                }

                AudioRuleManager ruleManager = ((DaoManager) AbstractDaoManager.getInstance()).getRuleManager();
                AbstractRule speedTrapRule = (AbstractRule) ruleManager
                        .getAudioRule(IRuleParameter.RULE_TYPE_SPEED_TRAP_WARNING_AUDIO);

                Hashtable param = new Hashtable();
                param.put(IRuleParameter.DISTANCE, PrimitiveTypeCache.valueOf(trafficAudioEvent.getDistance()));
                param.put(IRuleParameter.DIST_UNIT, PrimitiveTypeCache.valueOf(sysOfUnits));
                AudioData[] audioData = speedTrapRule.createAudioData(param);

                MediaManager.getInstance().getAudioPlayer().play("", audioData, -1);

                break;
            }
            default:
            {
                if (!isAlertNeeded(Preference.ID_PREFERENCE_TRAFFICALERT))
                {
                    return;
                }
                NavState navState = NavSdkNavEngine.getInstance().getCurrentNavState();
                if (navState != null)
                {
                    int distance = navState.getDistanceToEdge(incident.getEdgeID(), 200000);

                    Logger.log(Logger.INFO, ILogConstants.LOG_NAVIGATION, new Date() + ":: play traffic incident audio");

                    Logger.log(Logger.INFO, ILogConstants.LOG_NAVIGATION, "distance = " + distance + ", incident id = "
                            + incident.getId() + ", edge id = " + incident.getEdgeID() + ", position :: " + incident.getLat()
                            + "," + incident.getLon());

                    // no need to play traffic audio if too far away or already pass the incident
                    if (distance < 0)
                        return;

                    AudioData[] playList = AudioComposer.getInstance().createIncidentPrompt(distance,
                        incident.getIncidentType(), incident.getStreetAudio(), incident.getLocationAudio(),
                        incident.getLaneClosed());
                    MediaManager.getInstance().getAudioPlayer().play("", playList, -1);

                    if (isNavMapScreenShow())
                    {
                        // do not use AudioManager::playStaticAudio as it will cancel all previous audio jobs
                        byte[] avoidAudio = AbstractDaoManager.getInstance().getResourceBarDao()
                                .getAudio(IAudioRes.TRAFFIC_TOUCH_ICON_TO_AVOID);
                        if (avoidAudio != null)
                        {
                            AudioData avoidAudioData = AudioDataFactory.getInstance().createAudioData(avoidAudio);
                            MediaManager.getInstance().getAudioPlayer()
                                    .play("audio_" + IAudioRes.TRAFFIC_TOUCH_ICON_TO_AVOID, avoidAudioData, -1);
                        }
                    }
                }
            }
        }
    }

    private boolean isNavMapScreenShow()
    {
        int state = getState();
        return state == STATE_MOVING_MAP;
    }

    private void stop()
    {
        System.out.println("WJP: Stop");
        NavsdkAudioService.getInstance().pause();
        if (navigationProxy != null)
        {
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_NAV_ROUTE_SUMMARY);
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_NAV_NAVIGATION_STATUS);
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_NAV_SPEED_LIMIT_EXCEEDED);
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_NAV_VEHICLE_POSITION);
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_VOICE_GUIDANCE);
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_NAV_CANCEL_LIMIT_EXCEEDED);
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_NAV_NAVIGATION_DEVIATION);
            navigationProxy.removeListener(INavSdkProxyConstants.ACT_NAV_TRAFFIC_INCIDENTS_AHEAD);
            navigationProxy = null;
        }

        MapVehiclePositionService.getInstance().setVehiclePositionCallback(null);
        if(Notifier.getInstance().isRunning())
        {
            Notifier.getInstance().removeListener(this);
        }
        NetworkStatusManager.getInstance().removeStatusListener(this);

        NavSdkNavEngine navEngine = NavSdkNavEngine.getInstance();

        // remove the listener before stop the nav engine.
        // then app will stop to receive the nav event immediately.
        navEngine.removeListener(this);

        if (navEngine.isRunning())
        {
            navEngine.stop();
        }

        MediaManager.getInstance().getAudioPlayer().cancelAll();
    }

    protected NavParameter getNavParameter()
    {
        NavParameter navParameter = (NavParameter) this.get(KEY_O_NAV_PARAMETER);
        if (navParameter == null)
        {
            navParameter = new NavParameter();
            this.put(KEY_O_NAV_PARAMETER, navParameter);
        }
        return navParameter;
    }

    private void sendTrafficIncidentReport(boolean isTrafficCamera)
    {
        long now = System.currentTimeMillis();

        if ((now - lastSendingTrafficIncidentTime) < 5000)
        {
            return;
        }

        lastSendingTrafficIncidentTime = now;

        int incidentType = isTrafficCamera ? ITrafficIncidentReportProxy.INCIDENT_TYPE_TRAFFIC_CAMERA
                : ITrafficIncidentReportProxy.INCIDENT_TYPE_SPEED_TRAP;

        ITrafficIncidentReportProxy incidentReportProxy = ServerProxyFactory.getInstance().createTrafficIncidentReportProxy(
            null, CommManager.getInstance().getComm(), this);

        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if (location == null)
        {
            location = new TnLocation("");
        }
        incidentReportProxy.sendIncidentNavReport(incidentType, location);

        // we don't show message, playing audio instead.
        // this.put(KEY_S_ERROR_MESSAGE, isTrafficCamera ? "Traffic camera added successfully!" :
        // "Speed trap reported successfully!");

        if (!MediaManager.getInstance().getAudioPlayer().isPlaying())
        {
            int audioId = isTrafficCamera ? IAudioRes.TRAFFIC_CAMERA_REPORTED : IAudioRes.TRAFFIC_SPEED_TRAP_REPORTED;

            AudioData[] audioData = new AudioData[]
            { AudioDataFactory.getInstance().createAudioData(audioId) };
            MediaManager.getInstance().getAudioPlayer().play(ID_NAV_AUDIO, audioData, 1);
        }
    }

    private void doResumeRoute()
    {
        if (tripLog != null)
        {
            tripLog.endTrip();
            tripLog = null;
        }

        this.fetch(KEY_B_IS_FROM_SEARCH_ALONG);

        TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
        tripsDao.setDetourTrip(null);
        tripsDao.store();

        Address address = tripsDao.getLastTrip();
        put(KEY_O_ADDRESS_DEST, address);
    }

    /**
     * @see INavDataProvider::getDestination()
     */
    public String getDestination()
    {
        Stop destStop = null;
        Address address = (Address) get(KEY_O_ADDRESS_DEST);
        if (address != null)
        {
            destStop = address.getStop();

            if (destStop != null)
            {
                String destination = destStop.getFirstLine();
                if (destination == null || destination.length() == 0)
                {
                    destination = destStop.getCity();
                }
                return destination;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * @see INavDataProvider::isOnRoad()
     */
    public boolean isOnRoad(TnNavLocation gpsFix, int[] thresholds, boolean ignoreHeading)
    {
        switch (mapTileAdiStatusJudger.isOnRoad(gpsFix, thresholds, ignoreHeading))
        {
            case MapTileAdiStatusJudger.STATUS_ADI:
            {
                return false;
            }
            default:
            {
                // hold on in ADI mode in case of out of network coverage
                if (!NetworkStatusManager.getInstance().isConnected())
                {
                    return false;
                }

                return true;
            }
        }
    }

    protected void deactivateDelegate()
    {
        RouteUiHelper.resetCurrentRoute();
        if (!(getState() == STATE_GO_TO_ROUTE_SETTING))
        {
            MapContainer.getInstance().pause();
        }
        // disable the dimming of the screen per Matt. Enable it once get a better idea.
        /* TnBacklightManagerImpl.getInstance().setIsSavingMode(false); */
        zoomBeforeDeactivate = MapContainer.getInstance().getZoomLevel();
        super.deactivateDelegate();
    }

    private String getDestAddressStr()
    {
        String address = "";
        Address addr = (Address) get(KEY_O_ADDRESS_DEST);
        if (addr != null)
        {
            Stop destStop = addr.getStop();
            address = MovingMapHelper.getDestAddressStr(destStop);
        }
        return address;
    }

    private String getTurnDiscription(int turnIndex)
    {
        return MovingMapHelper.getTurnDescription(turnIndex, MovingMapHelper.textIdForType);
    }

    private IBillboardModeChangeListener billboardModeChangeListener;

    public void setBillboardModeChangeListener(IBillboardModeChangeListener billboardModeChangeListener)
    {
        this.billboardModeChangeListener = billboardModeChangeListener;
    }


    /**
     * The only case we invoke doNav is from billboard
     */
    public JSONObject doNav(JSONArray args, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        JSONObject ret = null;

        try
        {
            serviceId = args.getInt(2);

            if (args.get(1) != null)
            {
                Address adAddress = convertAddress(args.getJSONObject(1).toString().getBytes(), Address.TYPE_RECENT_STOP);
                this.put(KEY_O_AD_ADDRESS, adAddress);
                this.postModelEvent(EVENT_MODEL_DRIVE_TO_AD);

                ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            ret = ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }

        return ret;
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
        // disable the dimming of the screen per Matt. Enable it once get a better idea.
        /*
         * TnBacklightManagerImpl.getInstance().setIsSavingMode(true); if (!isInTurn && getDecreaseBacklightOption() ==
         * 1) { TnBacklightManagerImpl.getInstance().decreaseBrightness(); }
         */
        activateTime = System.currentTimeMillis();
        MapContainer.getInstance().resume();
        MapContainer.getInstance().setZoomLevel(zoomBeforeDeactivate);
    }

    public void onScreenOff()
    {
        // [QY]Only press home or back when backlight is On. App should be treated as paused.
        // Press power off in Tablet api above 11 this method will be called for stop(). But app is foreground it will
        // turn off backlight
        // isBackLightOFF is True. App is not paused.
        if (System.currentTimeMillis() - appStopTimeStamp < 500)
        {
            // isAppPaused = false;
        }
    }

    public void onScreenOn()
    {

    }

    private void stopBacklight()
    {
        NavsdkBacklightService.getInstance().setTurnListener(null);
        
        TnBacklightManagerImpl.getInstance().stop();

        //TnBacklightManager.getInstance().enableKeyguard(true);
    }

    private boolean isAudioDisabled()
    {
        Preference audioTypePref = DaoManager.getInstance().getTripsDao()
                .getPreference(Preference.ID_PREFERENCE_AUDIO_GUIDANCE);
        int audioPreference = -1;
        if (audioTypePref != null)
        {
            audioPreference = audioTypePref.getIntValue();
        }

        return audioPreference == Preference.AUDIO_NONE;

    }

    public void regionChanged(final String newRegion)
    {
        // if the newDetected region is just the region on-going switching. ignore it.
        if (targetRegion != null && targetRegion.equals(newRegion))
        {
            return;
        }

        targetRegion = newRegion;

        RegionResSwitcher regionSwitcher = new RegionResSwitcher(new RegionResSwitcher.IRegionResSwitcherListener()
        {
            public void onRegionSwitched(String message, int resultType)
            {
                if (resultType == RegionResSwitcher.SUCCESS_FROM_SERVER)
                {
                    if (newRegion.equals(targetRegion))
                    {
                        RegionUtil.getInstance().setCurrentRegion(newRegion);
                        RegionUtil.getInstance().clearTargetRegion(newRegion);
                    }
                }
            }
        });

        int type = regionSwitcher.switchRegion(newRegion);
        if (type == RegionResSwitcher.SUCCESS_FROM_CACHE)
        {
            if (newRegion.equals(targetRegion))
            {
                RegionUtil.getInstance().setCurrentRegion(newRegion);
                RegionUtil.getInstance().clearTargetRegion(newRegion);
            }
        }
    }

    public JSONObject doNativeService(JSONObject request, IHtmlSdkServiceListener listener)
    {
        int serviceId = ResultGenerator.SERVICE_ID_ZERO;
        try
        {
            String serviceName = null;
            if (request != null)
            {
                serviceId = request.getInt(IHtmlSdkServiceHandler.SERVICE_ID);
                serviceName = request.getString(IHtmlSdkServiceHandler.SERVICE_NAME);
            }
            else
            {
                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }

            if ("ChangeBillboardMode".equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                int mode = serviceData.getInt("mode");
                if (billboardModeChangeListener != null)
                {
                    billboardModeChangeListener.billboardModeChanged(mode);
                }

                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
            else if ("GetBillboardDistance".equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                int lat = serviceData.getInt("lat");
                int lon = serviceData.getInt("lon");

                TnLocation location = LocationProvider.getInstance().getCurrentLocation(LocationProvider.TYPE_GPS);

                if (location != null)
                {

                    int currentLat = location.getLatitude();
                    int currentLon = location.getLongitude();

                    int cosLat = DataUtil.getCosLat(currentLat);
                    int distance = DataUtil.gpsDistance(currentLat - lat, currentLon - lon, cosLat);

                    int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(
                        Preference.ID_PREFERENCE_DISTANCEUNIT);
                    String distanceStr = ResourceManager.getInstance().getStringConverter()
                            .convertDistance(distance, systemUnits);

                    JSONObject result = new JSONObject();
                    result.put("distance", distanceStr);

                    return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId, result.toString());
                }

                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
            }
            else if ("DisplayBillboardDetail".equals(serviceName))
            {
                JSONObject serviceData = request.getJSONObject(IHtmlSdkServiceHandler.SERVICE_DATA);
                String url = serviceData.getString("url");
                if (url != null && url.length() > 0)
                {

                    BillboardAd billboardAd = (BillboardAd) this.get(KEY_O_BILLBOARD_AD);
                    if (billboardAd != null)
                    {
                        NavAdManager.getInstance().logImpressionEnd(billboardAd,
                            IMisLogConstants.VALUE_BILLBOARD_EXIT_TYPE_FORCED);
                        billboardAd.updateState(BillboardAd.STATE_POI_VIEW);
                        NavAdManager.getInstance().logImpressionStart(billboardAd);
                    }

                    url = addEncodeTnInfo(url, "");
                    url = appendWidthHeightToUrl(url);

                    this.put(KEY_S_AD_DETAIL_URL, url);
                    this.postModelEvent(EVENT_MODEL_AD_DETAIL);
                }

                return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_SUCCESS, serviceId);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return ResultGenerator.generateJsonResult(ResultGenerator.STATUS_FAIL, serviceId);
        }

        return super.doNativeService(request, listener);
    }

    @Override
    public void updateVechiclePostion(TnLocation location)
    {
        boolean isGpsAvailable = true;
        if (location == null)
        {
            isGpsAvailable = false;
        }
        else if (location.getProvider().equals(TnLocationManager.NETWORK_PROVIDER)
                || location.getProvider().equals(TnLocationManager.TN_NETWORK_PROVIDER))
        {
            isGpsAvailable = false;
        }
        else if (System.currentTimeMillis() - location.getLocalTimeStamp() > LocationProvider.GPS_VALID_TIME)
        {
            isGpsAvailable = false;
        }
        
        int gpsCount = -1;
        if (isGpsAvailable)
        {
            gpsCount = location.getSatellites();
        }
        handleGpsEvent(gpsCount);

        if (isNeedCheckDecreaseLater)
        {
            System.out.println("DB-Test --> isNeedCheckDecreaseLater : " + isNeedCheckDecreaseLater);
            checkDecrease();
        }
    }


    private void checkDecrease()
    {
        int decreaseOption = getDecreaseBacklightOption();
        
        switch(decreaseOption)
        {
            case DECREASE_BACKLIGHT_LATER:
            {
                isNeedCheckDecreaseLater = true;
                //System.out.println("DB-Test --> backlight decrease check later");
                break;
            }
            case DECREASE_BACKLIGHT_NOW:
            {
                isNeedCheckDecreaseLater = false;
                //System.out.println("DB-Test --> backlight decrease");
                TnBacklightManagerImpl.getInstance().decreaseBrightness();
                break;
            }
            default:
            {
                isNeedCheckDecreaseLater = false;
                break;
            }
        }        
    }

    @Override
    public void noGpsTimeout()
    {

    }
    
    public void gpsWeak()
    {
        handleGpsEvent(-1);
    }

    public void statusUpdate(boolean isConnected)
    {
        if(networkTimer != null)
        {
            networkTimer.cancel();
        }
        
        networkTimer = new Timer();
        
        NetworkStatusChangeTask task = new NetworkStatusChangeTask(isConnected);
        
        long delay;
        
        if(isConnected)
        {
            delay = 2000;
        }
        else
        {
            delay = 5000;
        }
        
        networkTimer.schedule(task, delay);
    }

    public void enterTurn()
    {
        /*//System.out.println("DB-Test --> backlight enter trun");
        if (backlightPreference != Preference.BACKLIGHT_OFF && backlighManager != null)
        {
            backlighManager.enable();
            backlighManager.start();
            //disable the dimming of the screen per Matt. Enable it once get a better idea.
            TnBacklightManagerImpl.getInstance().setDefaultBrightness();
        }*/
        isInTurn = true;
    }

    public void exitTurn()
    {
        /*//System.out.println("DB-Test --> backlight exit turn");
        if (needTurnOffBacklight())
        {
            backlighManager.turnOff();
        }
        else
        {
            checkDecrease();
        }
        */
        isInTurn = false;
    }

    protected int getDecreaseBacklightOption()
    {
        if (backlightPreference != Preference.BACKLIGHT_ON)
        {
            return DECREASE_BACKLIGHT_DISALBE;
        }
        
        int needDerease = DECREASE_BACKLIGHT_DISALBE;
        
        //System.out.println("DB-Test --> backlight getNextTurnDistance : " + getNextTurnDistance());
        
        if (getNextTurnDistance() > BRIGHTNESS_DISTANCE_THRESHOLD) // 10 miles
        {
            needDerease = DECREASE_BACKLIGHT_LATER;
        }
        else
        {
            return needDerease;
        }
        
        //System.out.println("DB-Test --> backlight time past : " + (System.currentTimeMillis() - startTime));
        
        if (System.currentTimeMillis() - startTime > BRIGHTNESS_KICK_IN_TIME)
        {
            needDerease = DECREASE_BACKLIGHT_NOW;
        }
        
        return needDerease;
    }
    
    protected int getNextTurnDistance()
    {
        int distance = 0;
        
        NavParameter navParameter = (NavParameter) this.get(KEY_O_NAV_PARAMETER);
        if (navParameter == null)
        {
            navParameter = new NavParameter();
            this.put(KEY_O_NAV_PARAMETER, navParameter);
        }
    
        if (navParameter != null)
        {
            distance = navParameter.distanceToTurn;
        }
        
        return distance;
    }

    protected boolean needTurnOffBacklight()
    {
        if (backlightPreference != Preference.BACKLIGHT_ON)
        {
            return true;
        }
        
        return false;
    }
    
    class NetworkStatusChangeTask extends TimerTask
    {
        private boolean isConnected = false;
        
        NetworkStatusChangeTask(boolean isConnected)
        {
            this.isConnected = isConnected;
        }
        
        @Override
        public void run()
        {
            if(isConnected == NetworkStatusManager.getInstance().isConnected())
            {
                if (isConnected)
                {
                    if(!isCurrentConnected)
                    {
                        handleEnterCellCoverage();
                        isCurrentConnected = isConnected;
                    }
                }
                else
                {
                    if(isCurrentConnected)
                    {
                        handleNoCellCoverage();
                        isCurrentConnected = isConnected;
                    }
                }
            }
        }
    }
    
    protected void handleNoCellCoverage()
    {
        NavParameter param = getNavParameter();
        param.isOutOfCoverage = true;
        MovingMapModel.this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        playStaticAudioInSequence(IAudioRes.JINGLES_WARNING_BEEP);
        playStaticAudioInSequence(IAudioRes.NEW_NO_CELL_COVERAGE);
    }
    
    protected void handleEnterCellCoverage()
    {
        NavParameter param = getNavParameter();
        param.isOutOfCoverage = false;
        MovingMapModel.this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        playStaticAudioInSequence(IAudioRes.JINGLES_FRIENDLY_BEEP);
        playStaticAudioInSequence(IAudioRes.NEW_CELL_COVERAGE);
    }

    @Override
    public long getNotifyInterval()
    {
        return notifyIntervalForShareETA;
    }

    @Override
    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestampForShareETA;
    }

    @Override
    public void setLastNotifyTimestamp(long timestamp)
    {
        lastNotifyTimestampForShareETA = timestamp;
        
    }

    @Override
    public void notify(long timestamp)
    {
        String drivingStatus = ResourceManager.getInstance().getCurrentBundle()
        .getString(IStringCommon.RES_SHARE_ETA_DRIVING, IStringCommon.FAMILY_COMMON);
        if(isDriving)
        {
            TnLocation location = LocationProvider.getInstance().getCurrentLocation(
                LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
            requestUpdateETA(drivingStatus, location, String.valueOf(shareETA));
        }
        
    }

    @Override
    public void appActivated(String[] params)
    {
        if(params != null && params.length > 0)
        {
            String type = params[0];
            if(type.equals(TeleNavDelegate.ACTIVIATE_TYPE_RESTART) || type.equals(TeleNavDelegate.ACTIVIATE_TYPE_BACKLIGHT_ON))
            {
                int backlightPref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_BACKLIGHT);
                if (Preference.BACKLIGHT_ON == backlightPref)
                {
                    TnBacklightManagerImpl.getInstance().enable();
                    TnBacklightManagerImpl.getInstance().start();
                }
                else if (Preference.BACKLIGHT_TURNS == backlightPref)
                {
                    TnBacklightManagerImpl.getInstance().enable();
                }
                
            }
        }
        
    }

    @Override
    public void appDeactivated(String[] params)
    {
        if(params != null && params.length > 0)
        {
            String type = params[0];
            if(type.equals(TeleNavDelegate.DEACTIVIATE_TYPE_STOP) || type.equals(TeleNavDelegate.DEACTIVIATE_TYPE_BACKLIGHT_OFF)) 
            {  
                TnBacklightManagerImpl.getInstance().stop();
                //TnBacklightManager.getInstance().enableKeyguard(true);
            }
        } 
        
    }
}
