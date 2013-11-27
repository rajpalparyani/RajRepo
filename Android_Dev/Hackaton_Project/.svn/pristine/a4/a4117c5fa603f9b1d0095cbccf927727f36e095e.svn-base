/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DashboardModel.java
 *
 */
package com.telenav.module.dashboard;

import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.ShortcutManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.FriendlyUserRatingUtil;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.location.LocationListener;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler;
import com.telenav.module.mapdownload.MapDownLoadMessageHandler.IDownloadedMapStatusChangeListener;
import com.telenav.module.poi.PoiDataListener;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.result.PoiResultHelper;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDashboard;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.citizen.map.MapContainer;

/**
 *@author qli
 *@date 2012-2-3
 */
public class DashboardModel extends BrowserSdkModel implements IDashboardConstants, IApplicationListener, LocationListener, PoiDataListener, INotifierListener, DashboardManager.IDashboardInfoChangeListener, INetworkStatusListener, IDownloadedMapStatusChangeListener
{
    protected static final long NO_GPS_TIME_OUT = 12 * 1000;
    private long lastNotifyTimestamp;
    private long checkReceivedAddressStartTime;
    private int oldUnreviewedSize;
    protected int systemUnits = -1;
    
    private INotifierListener showDropdownHintListener;
    private long lastNotifyTimeStampDropdown;
    private int notifyStepDropdown = -1;
    
    protected void init()
    {
        oldUnreviewedSize = DaoManager.getInstance().getAddressDao().getUnreviewedAddressSize();
        Notifier.getInstance().addListener(this);
        
        this.put(KEY_B_IS_APP_ACTIVATED, true);
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        systemUnits = prefDao.getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        TeleNavDelegate.getInstance().registerApplicationListener(this);
        NetworkStatusManager.getInstance().addStatusListener(this);
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        DashboardManager.getInstance().setUserProfileProvider(userProfileProvider);
        DashboardManager.getInstance().setDashboardInfoChangeListener(this);
        DashboardManager.getInstance().start();
        MapContainer.getInstance().setMapTransitionTime(0);
        
        showDropdownHintListener = new INotifierListener()
        {

            @Override
            public long getNotifyInterval()
            {
                if (notifyStepDropdown == 1)
                {
                    return 3000;
                }
                else
                {
                    return 1000;
                }
            }

            @Override
            public long getLastNotifyTimestamp()
            {
                return lastNotifyTimeStampDropdown;
            }

            @Override
            public void setLastNotifyTimestamp(long timestamp)
            {
                lastNotifyTimeStampDropdown = timestamp;
            }

            @Override
            public void notify(long timestamp)
            {
                if (notifyStepDropdown == -1)
                {
                    notifyStepDropdown = 0;
                    return;
                }
                else if (notifyStepDropdown == 0)
                {
                    boolean hasShownDropdown = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(
                        SimpleConfigDao.KEY_HAS_SHOWN_ADD_SHARE_PANEL);
                    if (!hasShownDropdown)
                    {
                        put(KEY_B_MOCK_LOCATION_BAR_CLICK, true);
                        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                    }
                    notifyStepDropdown = 1;
                }
                else if (notifyStepDropdown == 1)
                {
                    boolean hasHiddenDropdown = getBool(KEY_B_HAS_HIDDEN_DROPDOWN);
                    if (!hasHiddenDropdown)
                    {
                        put(KEY_B_MOCK_LOCATION_BAR_CLICK, true);
                        postModelEvent(EVENT_MODEL_UPDATE_VIEW);
                    }
                    Notifier.getInstance().removeListener(showDropdownHintListener);
                }

            }
        };
        this.put(KEY_O_DROPDOWN_HINT_LISTENER, showDropdownHintListener);
    }

    protected void requestLocation()
    {
        LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.GPS_VALID_TIME,
            LocationProvider.NETWORK_LOCATION_VALID_TIME, 30 * 1000, this,
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1, false);
    }

    public boolean isValidAddress(Stop stop)
    {
        //verify the stop is valid.
        if ( stop != null && stop.getLat() != 0 && stop.getLon() != 0 )
        {
            return true;
        }
        return false;
    }

    
    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case IDashboardConstants.ACTION_INIT:
            {
                init();
                this.postModelEvent(EVENT_MODEL_LAUNCH_DASHBOARD);
                break;
            }
            case ACTION_BACK_CHECK:
            {
                boolean isStartupMap = DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
                if (isStartupMap)
                {
                	this.postModelEvent(EVENT_MODEL_BACK_TO_PRE);
                }
                else
                {
                    TeleNavDelegate.getInstance().jumpToBackground();
                }
                break;
            }
            case ACTION_CHECK_HOME_EXIST:
            {
                AddressDao addressDao = DaoManager.getInstance().getAddressDao();
                Stop stop = addressDao.getHomeAddress();
                if ( isValidAddress(stop) )
                    postModelEvent(EVENT_MODEL_RETURN_HOME);
                else
                    postModelEvent(EVENT_MODEL_SET_HOME_WORK);
                break;
            }
            case ACTION_CHECK_WORK_EXIST:
            {
                AddressDao addressDao = DaoManager.getInstance().getAddressDao();
                Stop stop = addressDao.getOfficeAddress();
                if ( isValidAddress(stop) )
                    postModelEvent(EVENT_MODEL_RETURN_WORK);
                else
                    postModelEvent(EVENT_MODEL_SET_HOME_WORK);
                break;
            }
            case ACTION_POI_SEARCH:
            {
                doCategorySearch();
                break;
            }
//            case ACTION_LAUNCH_DASHBOARD:
//            {
//                boolean isHomeScreen = !DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
//                if(isHomeScreen)
//                {
//                    postModelEvent(CMD_CHECK_REGION_DETECT_STATUS);
//                }
//                else
//                {
//                    postModelEvent(CMD_CONTINUE);
//                }
//                break;
//            }
            case ACTION_REGION_DETECTION:
            {
                checkRegionChange();
                break;
            }
            case ACTION_ADDRESS_SELECTED_CHECK:
            {
                if(this.getPreState() == STATE_BROWSER_GOTO_AC)
                {
                    postModelEvent(EVENT_BACK_TO_BROWSER);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_ADDRESS_RETURN);
                }
                break;
            }
            case ACTION_CHECK_REGION_DETECT_STATUS:
            {
                checkRegionDetectStatus();
                break;
            }
            case ACTION_RATE_APP:
            {
                FriendlyUserRatingUtil.doRateApp();
                DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_USED_RATE_SCOUT, true);
                DaoManager.getInstance().getSimpleConfigDao().store();
                break;
            }
            case ACTION_REMIND_LATER:
            case ACTION_CANCEL_POPUP:
            {
                FriendlyUserRatingUtil.doRemindLater();
                break;
            }
            case ACTION_NO_THANKS:
            {
                FriendlyUserRatingUtil.doNoThanks();
                break;
            }
            case ACTION_CHECK_MAP_DOWNLOADED_STATUS:
            {
                put(KEY_B_IS_MODULE_READY_TO_SHOW_MAP_NOTIFICATION, true);
                checkMapDownloadStatusChange(true);
                
                break;
            }
            case ACTION_CHECK_MODULE_READY:
            {
                checkMapDownloadStatusChange(false);
                break;
            }
            case ACTION_CHECK_FRIENDLY_USER_STATUS:
            {
                boolean isFirstTimeStartApp = this.fetchBool(ICommonConstants.KEY_IS_START_APP);

                if (isFirstTimeStartApp && AppConfigHelper.BRAND_SCOUT_US.equals(AppConfigHelper.brandName)
                        && FriendlyUserRatingUtil.handleFriendlyUser())
                {
                    MapDownLoadMessageHandler.getInstance().removeDownloadedMapStatusChangeListener();
                    this.put(KEY_B_IS_NAVSDK_POPUP_SHOW, true);
                    postModelEvent(EVENT_MODEL_SHOW_FRIEND_USER_POPUP);
                }
                break;
            }
            case ACTION_CHECK_NAVSDK_DELAY_POP_UP:
            {
                boolean isFirstTimeStartApp = this.fetchBool(ICommonConstants.KEY_IS_START_APP);
                if (isFirstTimeStartApp)
                {
                    boolean isNeedWifiPopup = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_NAVSDK_POPUP_DELAY);
                    if (isNeedWifiPopup)
                    {
                        String errorMessage = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.KEY_NAVSDK_POPUP_ERROR_MSG);
                        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_IS_NAVSDK_POPUP_DELAY);
                        ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().remove(SimpleConfigDao.KEY_NAVSDK_POPUP_ERROR_MSG);
                        this.postErrorMessage(errorMessage);
                    }
                    else
                    {
                        postModelEvent(EVENT_MODEL_CHECK_FRIENDLY_USER);
                    }
                }
                break;
            }
            case ACTION_CHECK_FRIENDLY_POPUP_SHOW:
            {
                boolean isFriendlyUserShow =  this.fetchBool(KEY_B_IS_NAVSDK_POPUP_SHOW); 
                if (!isFriendlyUserShow)
                {
                    postModelEvent(EVENT_MODEL_SHOW_NAVSDK_POPUP);
                }
				break;
            }
            default:
            {
                super.doActionDelegate(actionId);
            }
        }
    }
    
    protected synchronized void checkMapDownloadStatusChange(boolean needCheck)
    {
        //Dashboard has the flow to check region switch which has high priorty.
        //To avoid interrupt that flow, we only set this flag to true after region related flow finished.
        //If this flag is true, then we will show the notification immediately after we 
        //get the downloaded map region status change event.
        boolean isModuleReady = getBool(ICommonConstants.KEY_B_IS_MODULE_READY_TO_SHOW_MAP_NOTIFICATION);
        boolean isMapUpgradeShow = false;
        if(isModuleReady)
        {
            boolean isDownloadedMapStatusChanged = MapDownLoadMessageHandler.getInstance().isMapIncompatible;
            if(isDownloadedMapStatusChanged)
            {
                isMapUpgradeShow = true;
                Thread t = new Thread()
                {
                    public void run()
                    {
                        postModelEvent(ICommonConstants.EVENT_MODEL_SHOW_MAP_DOWNLOADED_STATUS_CHANGED_NOTIFICATION);
                    }
                };
                
                t.start();
            }
        }
        if (needCheck & !isMapUpgradeShow)
        {
            postModelEvent(EVENT_MODEL_CHECK_NAVSDK_DELAY_POPUP);
//            postModelEvent(EVENT_MODEL_CHECK_FRIENDLY_USER);
        }
    }
    
    protected void doCategorySearch()
    {
        int categoryId = this.getInt(KEY_I_CATEGORY_ID);
        String showText =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        if (null == showText)
        {
            //FIXME qli, client need show the searching text in progress animation?
            String name = getCategoryNameByID(categoryId, DaoManager.getInstance().getResourceBarDao().getHotPoiNode(this.getRegion()));
            // Special treatment for these categories in place component
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            switch (categoryId)
            {
                case CATEGORY_ID_FOOD:
                    name = bundle.getString(IStringCommon.RES_STRING_FOOD, IStringCommon.FAMILY_COMMON);
                    break;
                case CATEGORY_ID_GAS_REGULAR:
                    name = bundle.getString(IStringCommon.RES_STRING_GAS_REGULAR, IStringCommon.FAMILY_COMMON);
                    break;
                default:
                    break;
            }
            if (null != name)
            {
                put(KEY_S_COMMON_SHOW_SEARCH_TEXT, name);
                showText = name;
            }
        }
        int searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        if (-1 != this.getInt(KEY_I_SEARCH_TYPE))
        {
            searchType = this.getInt(KEY_I_SEARCH_TYPE);
        }
        int searchFromType = IPoiSearchProxy.TYPE_SEARCH_FROM_TYPEIN;
        if (-1 != this.getInt(KEY_I_SEARCH_FROM_TYPE))
        {
            searchFromType = IPoiSearchProxy.TYPE_SEARCH_FROM_TYPEIN;
        }
       
        int sortType = PoiResultHelper.getDefaultSortType(categoryId);

        if (-1 != this.getInt(KEY_I_SEARCH_SORT_TYPE))
        {
            sortType = this.getInt(KEY_I_SEARCH_SORT_TYPE);
        }

        this.put(KEY_I_SEARCH_SORT_TYPE, sortType);

        int pageNumber = 0;
        
        int pageSize = MAX_NUM_PER_PAGE;
        Stop anchorStop = null;
        NavState navState = null;
        Stop destStop = null;
        int searchAlongRouteType = -1;
        if (this.get(KEY_O_ADDRESS_ORI) instanceof Address)
        {
            Address oriAddr = (Address)this.get(KEY_O_ADDRESS_ORI);
            anchorStop = oriAddr.getStop();
        }
        else
        {
            anchorStop = this.getAnchor();
            if(anchorStop == null)
            {
                LocationProvider.getInstance().getCurrentLocation(LocationProvider.GPS_VALID_TIME,
                    LocationProvider.NETWORK_LOCATION_VALID_TIME, NO_GPS_TIME_OUT, this,
                    LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, 1);
                return;
            }
        }
        
        PoiDataWrapper dataWrapper = new PoiDataWrapper(System.currentTimeMillis() + "");
        dataWrapper.setSearchArgs(0, searchType, searchFromType, searchAlongRouteType, sortType, categoryId, pageNumber, pageSize, "", showText, anchorStop, destStop, navState) ;
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        PoiDataRequester poiDataRequester = new PoiDataRequester(userProfileProvider);
        poiDataRequester.doRequestPoi(dataWrapper, this);
    }
    
    private String getCategoryNameByID(int catID, PoiCategory parent)
    {
        if (null == parent) return null;
        if (parent.getCategoryId() == catID) return parent.getName();
        if (parent.getChildrenSize() == 0) return null;
        String name = null;
        for (int i = 0; i < parent.getChildrenSize(); i++)
        {
            PoiCategory child = parent.getChildAt(i);
            name = getCategoryNameByID(catID, child);
            if (null != name) return name;
        }
        return null;
    }
    
    private Stop getAnchor()
    {
        Stop anchor = null;
        // 1. Get  gps or network fix
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
         LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK, true);
        if (location == null)
        {
            // 2. Get  last-known gps or network fix
            location = LocationProvider.getInstance().getLastKnownLocation(
                LocationProvider.TYPE_GPS|LocationProvider.TYPE_NETWORK);
            if (location != null)
            {
                anchor = new Stop();
                anchor.setType(Stop.STOP_CURRENT_LOCATION);
                anchor.setLat(location.getLatitude());
                anchor.setLon(location.getLongitude());
            }
        }
        else
        {
            anchor = new Stop();
            anchor.setType(Stop.STOP_CURRENT_LOCATION);
            anchor.setLat(location.getLatitude());
            anchor.setLon(location.getLongitude());
        }        
        return anchor;
    }

    public void appDeactivated(String[] params)
    {
        this.put(KEY_B_IS_APP_ACTIVATED, false);
        DashboardManager.getInstance().pause();
    }

    public void appActivated(String[] params)
    {
        if (this.isActivated() && !ShortcutManager.getInstance().isInShortcutScreen())
        {
            this.put(KEY_B_IS_APP_ACTIVATED, true);
            this.put(KEY_B_NEED_REFRESH_MINI_MAP, true);
       		DashboardManager.getInstance().resume();
       		MapContainer.getInstance().setMapTransitionTime(0);
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }

    }
    
    public void requestCompleted(TnLocation[] locations, int statusCode)
    {
        // after get gps, start request Map and ETA
        if (statusCode == LocationProvider.STATUS_SUCCESS)
        {
//            saveWeatherCityLocation(locations[0]);
//            requestBatch();
            
        }
        else
        {
            requestLocation();
        }

    }

    public void poiResultUpdate(int resultValue, int resultType, String errorMsg,
            PoiDataWrapper poiDataWrapper)
    {
        String errorMessage = errorMsg;
        switch(resultValue)
        {
            case PoiDataRequester.TYPE_NETWORK_ERROR:
            {
                if (errorMessage == null || errorMessage.length() == 0)
                {
                    errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
                }
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS_NO_RESULTS:
            {
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }
                
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_BAD_ARGS:
            {
                errorMessage = ResourceManager.getInstance()
                .getCurrentBundle().getString(IStringPoi.LABEL_NO_POIS,
                    IStringPoi.FAMILY_POI);
                String name =  getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                if(name == null)
                {
                    name = getString(KEY_S_COMMON_SEARCH_TEXT);
                }
                
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                errorMessage = converter.convert(errorMessage, new String[]{name});
                postErrorMessage(errorMessage);
                break;
            }
            case PoiDataRequester.TYPE_SUCCESS:
            {
                this.put(KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
                postModelEvent(EVENT_MODEL_GOTO_POI_LIST);
                break;
            }
        }
    }

    protected void activateDelegate(boolean isUpdateView)
    {
        // FIXME: adapter the font size of native XML configuration
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        this.put(KEY_B_NEED_REFRESH_MINI_MAP, true);
        DashboardManager.getInstance().setDashboardInfoChangeListener(this);
        MapContainer.getInstance().resume();
        MapContainer.getInstance().setMapTransitionTime(0);
        checkMapDownloadStatusChange(false);
        MapDownLoadMessageHandler.getInstance().setDownloadedMapStatusChangeListener(this);
    }

    protected void deactivateDelegate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        DashboardManager.getInstance().setDashboardInfoChangeListener(null);
        MapContainer.getInstance().pause();
        MapContainer.getInstance().setMapRect(null, null, null, null);
        DashboardManager.getInstance().pause();
        MapDownLoadMessageHandler.getInstance().removeDownloadedMapStatusChangeListener();
    }

    protected void releaseDelegate()
    {
        super.releaseDelegate();
        AbstractTnImage portraitSnappedImage = (AbstractTnImage) this.get(KEY_O_PORTRAIT_MAP_SNAPPED_IMAGE);
        if (portraitSnappedImage != null && !portraitSnappedImage.isRelease())
        {
            portraitSnappedImage.release();
            this.remove(KEY_O_PORTRAIT_MAP_SNAPPED_IMAGE);
        }

        AbstractTnImage landscapeMapSnappedImage = (AbstractTnImage) this.get(KEY_O_PORTRAIT_MAP_SNAPPED_IMAGE);
        if (landscapeMapSnappedImage != null && !landscapeMapSnappedImage.isRelease())
        {
            landscapeMapSnappedImage.release();
            this.remove(KEY_O_LANDSCAPE_MAP_SNAPPED_IMAGE);
        }
        TeleNavDelegate.getInstance().unregisterApplicationListener(this);
        Notifier.getInstance().removeListener(this);
        NetworkStatusManager.getInstance().removeStatusListener(this);
        
        MapDownLoadMessageHandler.getInstance().removeDownloadedMapStatusChangeListener();
        DashboardManager.getInstance().pause();
    }

    public long getLastNotifyTimestamp()
    {
        return lastNotifyTimestamp;
    }

    public long getNotifyInterval()
    {
        return 400;
    }

    public void notify(long timestamp)
    {
        if(checkReceivedAddressStartTime <= 0)
        {
            checkReceivedAddressStartTime = System.currentTimeMillis();
        }
        
        if((System.currentTimeMillis() - checkReceivedAddressStartTime) > 60000)
        {
            Notifier.getInstance().removeListener(this);
        }
        
        int size = DaoManager.getInstance().getAddressDao().getUnreviewedAddressSize();
        if(size > oldUnreviewedSize && this.getState() == STATE_DASHBOARD)
        {
            this.postModelEvent(EVENT_MODEL_UPDATE_SHARED_ADDRESS_BADGE);
            Notifier.getInstance().removeListener(this);
        }        
    }

    public void setLastNotifyTimestamp(long timestamp)
    {
        this.lastNotifyTimestamp = timestamp;        
    }

    public void onDashboardInfoChanged(boolean isMiniMap)
    {
        if(this.getState() == STATE_DASHBOARD && this.isActivated() && !ShortcutManager.getInstance().isInShortcutScreen())
        {
            if (isMiniMap)
            {
                this.put(KEY_B_UPDATE_MINI_MAP_ONLY, true);
            }
            else
            {
                this.put(KEY_B_NEED_REFRESH_MINI_MAP, false);
            }
            
            this.postModelEvent(EVENT_MODEL_UPDATE_VIEW);
        }
    }
    
    @Override
    public void onMapDownloadStatusChanged()
    {
        postModelEvent(EVENT_MODEL_MAP_DOWNLOADED_STATUS_CHANGED);
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        if(isConnected)
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
