/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ModuleFactory.java
 *
 */
package com.telenav.module;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.carconnect.host.CarConnectHostManager;
import com.telenav.comm.Host;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.datatypes.nav.NavState;
import com.telenav.location.TnLocation;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.about.AboutController;
import com.telenav.module.ac.AcController;
import com.telenav.module.ac.addressValidator.AddressValidatorController;
import com.telenav.module.ac.airport.AirportController;
import com.telenav.module.ac.contacts.ContactsController;
import com.telenav.module.ac.favorite.FavoriteController;
import com.telenav.module.ac.favorite.editcategory.EditCategoryController;
import com.telenav.module.ac.favorite.favoriteeditor.FavoriteEditorController;
import com.telenav.module.ac.home.HomeController;
import com.telenav.module.ac.place.addplace.AddPlaceController;
import com.telenav.module.ac.recent.RecentController;
import com.telenav.module.ac.stopsselection.StopsSelectionController;
import com.telenav.module.browsersdk.BrowserSdkController;
import com.telenav.module.browsersdk.IBrowserSdkConstants;
import com.telenav.module.dashboard.DashboardController;
import com.telenav.module.drivingshare.DrivingShareController;
import com.telenav.module.dsr.DsrController;
import com.telenav.module.dwf.DwfUtil;
import com.telenav.module.dwf.DriveWithFriendsController;
import com.telenav.module.entry.EntryController;
import com.telenav.module.entry.secretkey.SecretKeyController;
import com.telenav.module.feedback.FeedbackController;
import com.telenav.module.login.LoginController;
import com.telenav.module.map.MapController;
import com.telenav.module.mapdownload.MapDownLoadController;
import com.telenav.module.nativeshare.NativeShareController;
import com.telenav.module.nav.ITrafficCallback;
import com.telenav.module.nav.NavController;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.movingmap.MovingMapController;
import com.telenav.module.nav.routeplanning.RoutePlanningController;
import com.telenav.module.nav.routesummary.RouteSummaryController;
import com.telenav.module.nav.summarycontrol.SummaryControlController;
import com.telenav.module.nav.traffic.TrafficController;
import com.telenav.module.nav.turnmap.TurnMapController;
import com.telenav.module.oneboxsearch.OneBoxSearchController;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.detail.PoiDetailController;
import com.telenav.module.poi.localevent.LocalEventController;
import com.telenav.module.poi.result.PoiResultController;
import com.telenav.module.poi.search.PoiSearchController;
import com.telenav.module.preference.PreferenceController;
import com.telenav.module.preference.language.SwitchLanguageController;
import com.telenav.module.preference.routesetting.RouteSettingController;
import com.telenav.module.region.RegionUtil;
import com.telenav.module.sync.SyncResController;
import com.telenav.module.testproxy.TestProxyController;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.module.upsell.UpSellController;
import com.telenav.module.vbb.VbbDetailController;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.maitai.module.MaiTaiController;
import com.telenav.sdk.maitai.view.MaiTaiViewController;
import com.telenav.sdk.plugin.PluginManager;
import com.telenav.sdk.plugin.module.PluginController;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public class ModuleFactory
{
    protected static ModuleFactory instance; 
    
    public ModuleFactory()
    {
    }
    
    public static ModuleFactory getInstance()
    {
        return instance;
    }
    
    public static void init(ModuleFactory factory)
    {
        instance = factory;
    }
    
    public void startAppFlow()
    {
        EntryController entryController = new EntryController(null);
        entryController.init();
    }
    
    public void startMain()
    {
        PluginManager.getInstance().setFromPlugin(false);
        EntryController entryController = new EntryController(null);
        entryController.init(ICommonConstants.EVENT_CONTROLLER_GO_TO_MAIN, null);
    }
    
    /**
     * Add for plugin and maitai. See NavController for detail.
     * @param controllerEvent
     */
    public void startMain(int controllerEvent, Hashtable pluginRequest)
    {
        Parameter param = new Parameter();
        if(pluginRequest != null)
        {
            param.put(ICommonConstants.KEY_O_PLUGIN_REQUEST, pluginRequest);
        }
        EntryController entryController = new EntryController(null);
        entryController.init(controllerEvent, param);
    }
    
    public void startMainAndGoPurchaseNav(IUserProfileProvider userProfileProvider)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        MaiTaiManager.getInstance().setFromMaiTai(false);
        PluginManager.getInstance().setFromPlugin(false);
        EntryController entryController = new EntryController(null);
        entryController.init(ICommonConstants.EVENT_CONTROLLER_GO_TO_MAIN_PURCHASE_NAV, param);
    }
    
    public void startPreferenceController(AbstractCommonController controller, IUserProfileProvider userProfileProvider)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        PreferenceController preferenceController = new PreferenceController(controller);
        preferenceController.init(param);
    }
    
    /**
     * 
     * @param controller
     * @param acType
     * @param searchType
     * @param inputType
     * @param sortType
     * @param alongRouteType
     * @param anchorAddr
     * @param destAddr
     * @param navState
     * @param userProfileProvider TODO
     * @param isSearchNearBy TODO
     */
    public void startCategorySearchController(
            AbstractCommonController controller, int acType, int searchType,
            int inputType, int sortType, int alongRouteType,
            Address anchorAddr, Address destAddr, NavState navState, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        startCategorySearchController(controller, acType, searchType, inputType, sortType, alongRouteType, false, anchorAddr, destAddr, navState, userProfileProvider, isSearchNearBy);
    }
    
    public void startCategorySearchController(
            AbstractCommonController controller, int acType, int searchType,
            int inputType, int sortType, int alongRouteType, boolean isChangeLocation,
            Address anchorAddr, Address destAddr, NavState navState, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        startCategorySearchController(controller, acType, searchType, inputType, sortType, alongRouteType, -1, isChangeLocation, anchorAddr, destAddr, navState, userProfileProvider, isSearchNearBy);
    }
    
    /**
     * 
     * @param controller
     * @param acType
     * @param searchType
     * @param inputType
     * @param sortType
     * @param alongRouteType
     * @param catId
     * @param isChangeLocation
     * @param anchorAddr
     * @param destAddr
     * @param navState
     * @param userProfileProvider TODO
     * @param isSearchNearBy TODO
     */
    public void startCategorySearchController(
            AbstractCommonController controller, int acType, int searchType,
            int inputType, int sortType, int alongRouteType, int catId, boolean isChangeLocation,
            Address anchorAddr, Address destAddr, NavState navState, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        PoiSearchController poiSearchController = new PoiSearchController(
                controller);
        if(searchType < 0)
        {
            searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        }
        if(alongRouteType < 0 && searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            alongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD; 
        }
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
        data.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
        if(anchorAddr != null)
        {
            data.put(ICommonConstants.KEY_O_ADDRESS_ORI, anchorAddr);
        }
        
        if (catId != -1)
        {
            data.put(ICommonConstants.KEY_I_CATEGORY_ID, catId);
        }
        
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddr);
        data.put(ICommonConstants.KEY_O_NAVSTATE, navState);
        data.put(ICommonConstants.KEY_I_SEARCH_FROM_TYPE, inputType);
        data.put(ICommonConstants.KEY_I_SEARCH_SORT_TYPE, sortType);
        data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChangeLocation);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
        poiSearchController.init(data);
    }
   
    /**
     * 
     * @param controller
     * @param acType
     * @param searchType
     * @param alongRouteType
     * @param initText
     * @param isSearchDirectly
     * @param anchorAddr
     * @param destAddr
     * @param navState
     * @param userProfileProvider TODO
     * @param isSearchNearBy TODO
     * @param isFromMap to index come from map or not
     */
    public void startOneBoxController(AbstractCommonController controller,
            int acType, int searchType, int alongRouteType, String initText, boolean isSearchDirectly, boolean isChangeLocation, Address anchorAddr,
            Address destAddr, NavState navState, IUserProfileProvider userProfileProvider, boolean isSearchNearBy,boolean needCurrentLocation, boolean isFromMap)
    {
        OneBoxSearchController oneBoxSearchController = new OneBoxSearchController(controller);
        if(searchType < 0)
        {
            searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        }
        if(alongRouteType < 0 && searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            alongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD; 
        }
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
        data.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
        data.put(ICommonConstants.KEY_S_COMMON_SEARCH_TEXT, initText);
        data.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, initText);
        if(anchorAddr != null)
        {
            data.put(ICommonConstants.KEY_O_ADDRESS_ORI, anchorAddr);
        }
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddr);
        data.put(ICommonConstants.KEY_O_NAVSTATE, navState);
        data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChangeLocation);
        data.put(ICommonConstants.KEY_B_IS_SEARCHING_DIRECTLY, isSearchDirectly);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
        data.put(ICommonConstants.KEY_B_NEED_CURRENT_LOCATION, needCurrentLocation);
        data.put(ICommonConstants.KEY_B_IS_FROM_MAP, isFromMap);
        oneBoxSearchController.init(data);
    }
    
    public void startPoiResultController(AbstractCommonController controller,
            int selectedIndex, int categoryId, int acType, int searchType, int sortType,
           int alongRouteType, String showText, String searchText, Address anchorAddr,
           Address destAddr, NavState navState, PoiDataWrapper dataWrapper, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        startPoiResultController(controller, selectedIndex, categoryId, acType,
            searchType, sortType, alongRouteType, false, showText, searchText,
            anchorAddr, destAddr, navState, dataWrapper, userProfileProvider, isSearchNearBy);
    }
    
   /**
    * 
    * @param controller
 * @param selectedIndex
 * @param categoryId
 * @param acType
 * @param searchType
 * @param sortType
 * @param alongRouteType
 * @param isChangelocation
 * @param showText
 * @param searchText
 * @param anchorAddr
 * @param destAddr
 * @param navState
 * @param dataWrapper
 * @param userProfileProvider TODO
 * @param isSearchNearBy TODO
    */
    public void startPoiResultController(AbstractCommonController controller,
             int selectedIndex, int categoryId, int acType, int searchType, int sortType,
            int alongRouteType, boolean isChangelocation, String showText, String searchText, Address anchorAddr,
            Address destAddr, NavState navState, PoiDataWrapper dataWrapper, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        PoiResultController poiResultController = new PoiResultController(
                controller);
        Parameter data = new Parameter();
        if(searchType < 0)
        {
            searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        }
        if(alongRouteType < 0 && searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            alongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD; 
        }
        data.put(ICommonConstants.KEY_I_CATEGORY_ID, categoryId);
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
        data.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
        if(anchorAddr != null)
        {
            data.put(ICommonConstants.KEY_O_ADDRESS_ORI, anchorAddr);
        }
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddr);
        data.put(ICommonConstants.KEY_O_NAVSTATE, navState);
        data.put(ICommonConstants.KEY_S_COMMON_SEARCH_TEXT, searchText);
        data.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, showText);
        data.put(ICommonConstants.KEY_I_SEARCH_SORT_TYPE, sortType);
        data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
        data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChangelocation);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
        poiResultController.init(data);
    }

    public void startSecretKeyController(AbstractCommonController controller)
    {
        SecretKeyController secretKeyController = new SecretKeyController(controller);
        secretKeyController.init();
    }
    
//    public void startLoginController(AbstractCommonController controller)
//    {
//        LoginController loginController = new LoginController(controller);
//        loginController.init();
        //it is used by entry 
//        startLoginController(controller, ICommonConstants.EVENT_CONTROLLER_START, false);
//    }
   
//    public void startLoginController(AbstractCommonController controller, Parameter data)
//    {
//        LoginController loginController = new LoginController(controller);
//        loginController.init(data);
//    }
    
    public void startLoginController(AbstractCommonController controller,int event, boolean isNotFromEntry, int loginFrom)
    {
        LoginController loginController = new LoginController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_B_IS_START_BY_OTHER_CONTROLLER, isNotFromEntry);
        data.put(ICommonConstants.KEY_I_LOGIN_FROM, loginFrom);
        loginController.init(event, data);
    }
    
    public void startDsrController(AbstractCommonController controller, int acType, int searchType, int searchAlongType, Address dest, NavState navState, IUserProfileProvider userProfileProvider)
    {
        if (FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_DSR) == FeaturesManager.FE_UNPURCHASED)
        {
            startUpSellController(controller, FeaturesManager.FEATURE_CODE_DSR, false, userProfileProvider);
        }
        else
        {
            Parameter parameter = new Parameter();
            parameter.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
            parameter.put(ICommonConstants.KEY_O_ADDRESS_DEST, dest);
            parameter.put(ICommonConstants.KEY_O_NAVSTATE, navState);
            parameter.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, searchAlongType);
            parameter.put(ICommonConstants.KEY_I_AC_TYPE, acType);
            parameter.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
            
            int dsrLevel = 0;
            if(NavRunningStatusProvider.getInstance().isNavRunning())
            {
                dsrLevel = 1;
            }
            else
            {
                if(controller instanceof DashboardController)
                {
                    dsrLevel = 2; 
                }
                else if(controller instanceof PoiResultController)
                {
                    dsrLevel = 4; 
                }
                else if(controller instanceof MapController)
                {
                    dsrLevel = 3; 
                }
            }
            if(dsrLevel > 0)
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DSR,
                    KontagentLogger.DSR_MANUAL_START, dsrLevel);
            }
            else
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DSR,
                    KontagentLogger.DSR_MANUAL_START);
            }
            DsrController dsrController = new DsrController(controller);
            dsrController.init(parameter);
        }
    }
    
    /**
     * start AC controller
     * @param controller
     * @param acType
     * @param isChoosingLocation
     * @param isCurrentLocationNeeded TODO
     * @param isNeedPauseLater TODO
     * @param userProfileProvider TODO
     */
    public void startAcController(AbstractCommonController controller, int acType, boolean isChoosingLocation, boolean isCurrentLocationNeeded, boolean isNeedPauseLater, IUserProfileProvider userProfileProvider)
    {
        AcController acController = new AcController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChoosingLocation);
		data.put(ICommonConstants.KEY_B_IS_CURRENT_LOCATION_NEEDED, isCurrentLocationNeeded);
        data.put(ICommonConstants.KEY_B_IS_NEED_PAUSE_LATER, isNeedPauseLater);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        acController.init(data);
    }
    
    /**
     * 
     * @param controller    super controller
     * @param event  controller event
     * @param originAddress  the origin address to get route choices. Can be null.
     * @param destAddress    the destination address to get route choices.
     * @param isResumeTrip  whether is from resume trip
     *//*
    public void startRoutePlanningController(AbstractCommonController controller, int event, Address originAddress, Address destAddress, Vector audioList, boolean isFromSearchAlong, boolean isResumeTrip)
    {
        RoutePlanningController routePlanningController = new RoutePlanningController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_ADDRESS_ORI, originAddress);
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        data.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        data.put(ICommonConstants.KEY_V_STOP_AUDIOS, audioList);
        data.put(ICommonConstants.KEY_B_IS_RESUME_TRIP, isResumeTrip);
        routePlanningController.init(event, data);
    }*/
    
    /**
     * 
     * @param controller    super controller
     * @param event  controller event
     * @param originAddress  the origin address to get route choices. Can be null.
     * @param destAddress    the destination address to get route choices.
     * @param isResumeTrip  whether is from resume trip
     */
    public void startRoutePlanningController(AbstractCommonController controller, int event, Address originAddress, Address destAddress, Vector audioList, boolean isFromSearchAlong, boolean isResumeTrip, boolean isFromDSR, boolean needShareEta)
    {
        startRoutePlanningController(controller, event, originAddress, destAddress, audioList, isFromSearchAlong, isResumeTrip, isFromDSR, needShareEta, false);
    }
    
    /**
     * 
     * @param controller    super controller
     * @param event  controller event
     * @param originAddress  the origin address to get route choices. Can be null.
     * @param destAddress    the destination address to get route choices.
     * @param isResumeTrip  whether is from resume trip
     */
    public void startRoutePlanningController(AbstractCommonController controller, int event, Address originAddress, Address destAddress, Vector audioList, boolean isFromSearchAlong, boolean isResumeTrip, boolean isFromDSR, boolean needShareEta, boolean isFromDwf)
    {
        RoutePlanningController routePlanningController = new RoutePlanningController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_ADDRESS_ORI, originAddress);
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        data.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        data.put(ICommonConstants.KEY_V_STOP_AUDIOS, audioList);
        data.put(ICommonConstants.KEY_B_IS_RESUME_TRIP, isResumeTrip);
        data.put(ICommonConstants.KEY_B_IS_FROM_DSR, isFromDSR);
        data.put(ICommonConstants.KEY_B_NEED_SHARE_ETA, needShareEta);
        data.put(ICommonConstants.KEY_B_IS_FROM_DWF, isFromDwf);
        routePlanningController.init(event, data);
    }
    
    /**
     * Route summary can be start by both Moving Map module and Directions module. If it's Directions module, it must
     * pass the index of current segment to display on the title.
     * 
     * @param controller super controller
     * @param destAddress destination address.
     * @param trafficCallback trafficCallback is useful for deviation.
     * @param currentSegmentIndex If it's from Directions, it means the current segment index when you leave Directions
     *            module. If isStaticRoute is false, it's of no use.
     * @param isFromSearchAlong tell me if this trip is from search along. Because we have detour function.
     * @param acType TODO
     * 
     */
    public void startRouteSummaryController(AbstractCommonController controller, Address origAddress, Address destAddress, ITrafficCallback trafficCallback, int currentSegmentIndex, boolean isFromSearchAlong, int acType)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_O_ADDRESS_ORI, origAddress);
        param.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        param.put(ICommonConstants.KEY_O_TRAFFIC_CALLBACK, trafficCallback);
        param.put(ICommonConstants.KEY_I_CURRENT_SEGMENT_INDEX, currentSegmentIndex);
        param.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        RouteSummaryController routeSummaryController = new RouteSummaryController(controller);
        routeSummaryController.init(param);
    }
    
//    public void startMapController(AbstractCommonController controller, int mapFromType)
//    {
//        Parameter param = new Parameter();
//        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
//        MapController mapController = new MapController(controller);
//        mapController.init(param);
//    }
    
//    public void startMapController(AbstractCommonController controller, int mapFromType, Address address)
//    {
//        Parameter param = new Parameter();
//        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
//        param.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, address);
//        MapController mapController = new MapController(controller);
//        mapController.init(param);
//    }
    
    public void startMapController(AbstractCommonController controller, int detialFromType, int mapFromType, Address selectedAddress, PoiDataWrapper dataWrapper, String searchText, int acType, IUserProfileProvider userProfileProvider, boolean isSearchBy)
    {
    	Parameter param = new Parameter();
    	param.put(ICommonConstants.KEY_I_TYPE_DETAIL_FROM, detialFromType);
        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
        param.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, selectedAddress);
        param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        param.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchBy);
        MapController mapController = new MapController(controller);
        mapController.init(param);
    }
    
    public void startMapController(AbstractCommonController controller, int mapFromType, PoiDataWrapper dataWrapper, String searchText, int acType, IUserProfileProvider userProfileProvider, boolean isSearchBy)
    {
        startMapController(controller, ICommonConstants.TYPE_DETAIL_FROM_POI, mapFromType, null, dataWrapper, searchText, acType, userProfileProvider, isSearchBy);
    }
    
    public void startMapController(AbstractCommonController controller, int mapFromType, PoiDataWrapper dataWrapper, String searchText, int acType, Object originAddress, Object destAddress, int searchType, IUserProfileProvider userProfileProvider)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
        param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        param.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        param.put(ICommonConstants.KEY_O_ADDRESS_ORI, originAddress);
        param.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        MapController mapController = new MapController(controller);
        mapController.init(param);
    }
    
//    public void startMapController(AbstractCommonController controller, int mapFromType, Address address, String searchText)
//    {
//    	Parameter param = new Parameter();
//        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
//        param.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, address);
//        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
//        MapController mapController = new MapController(controller);
//        mapController.init(param);
//    }
    
    public void startMapController(AbstractCommonController controller, int mapFromType, PoiDataWrapper poiDataWrapper, String searchText, int acType, IUserProfileProvider userProfileProvider)///*Address[] address*/
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
        param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        MapController mapController = new MapController(controller);
        mapController.init(param);
    }
    
    public void startMapController(AbstractCommonController controller, int mapFromType, Object address, String searchText, IUserProfileProvider userProfileProvider)///*Address[] address*/
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
        if(address instanceof Address[])
        {
            param.put(ICommonConstants.KEY_O_OPERATED_ADDRESS_LIST, address);
        }
        else if(address instanceof Address)
        {
            param.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, address);
        }
        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        MapController mapController = new MapController(controller);
        mapController.init(param);
    }
    
    public void startMapController(AbstractCommonController controller, int mapFromType, Object address, Object destAddress, String searchText, IUserProfileProvider userProfileProvider, String vbbUrl)///*Address[] address*/
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
        if(address instanceof Address[])
        {
            param.put(ICommonConstants.KEY_O_OPERATED_ADDRESS_LIST, address);
        }
        else if(address instanceof Address)
        {
            param.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, address);
        }
        param.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        param.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, searchText);
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        param.put(ICommonConstants.KEY_S_AD_DETAIL_URL, vbbUrl);
        MapController mapController = new MapController(controller);
        mapController.init(param);
    }
    
    public void startMapController(AbstractCommonController controller, int mapFromType, int controllerEvent, ITrafficCallback trafficCallback, boolean isFromSearchAlong, int acType, Object destAddress, IUserProfileProvider userProfileProvider)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
        param.put(ICommonConstants.KEY_O_TRAFFIC_CALLBACK, trafficCallback);
        param.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        param.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        MapController mapController = new MapController(controller);
        mapController.init(controllerEvent, param);
    }
    
    public void startMapController(AbstractCommonController controller, int mapFromType,  int selectedIndex, int categoryId, int acType, int searchType, int sortType,
            int alongRouteType, boolean isChangelocation, String showText, String searchText, Address anchorAddr,
            Address destAddr, NavState navState, PoiDataWrapper dataWrapper, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        Parameter data = new Parameter();
       
        data.put(ICommonConstants.KEY_I_TYPE_MAP_FROM, mapFromType);
        if(searchType < 0)
        {
            searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
        }
        if(alongRouteType < 0 && searchType == IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE)
        {
            alongRouteType = IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD; 
        }
        data.put(ICommonConstants.KEY_I_CATEGORY_ID, categoryId);
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
        data.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, alongRouteType);
        if(anchorAddr != null)
        {
            data.put(ICommonConstants.KEY_O_ADDRESS_ORI, anchorAddr);
        }
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddr);
        data.put(ICommonConstants.KEY_O_NAVSTATE, navState);
        data.put(ICommonConstants.KEY_S_COMMON_SEARCH_TEXT, searchText);
        data.put(ICommonConstants.KEY_S_COMMON_SHOW_SEARCH_TEXT, showText);
        data.put(ICommonConstants.KEY_I_SEARCH_SORT_TYPE, sortType);
        data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
        data.put(ICommonConstants.KEY_B_IS_CHOOSING_LOCATION, isChangelocation);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
        MapController mapController = new MapController(controller);
        mapController.init(data);
    }
    
    public void startSummaryControlController(AbstractCommonController controller, int controllerEvent, Address origAddress, Address destAddress, ITrafficCallback trafficCallback, int currentSegmentIndex, boolean isFromSearchAlong, int acType)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        param.put(ICommonConstants.KEY_O_ADDRESS_ORI, origAddress);
        param.put(ICommonConstants.KEY_O_TRAFFIC_CALLBACK, trafficCallback);
        param.put(ICommonConstants.KEY_I_CURRENT_SEGMENT_INDEX, currentSegmentIndex);
        param.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        
        SummaryControlController summaryControlController = new SummaryControlController(controller);
        summaryControlController.init(controllerEvent, param);
    }
    
    public void startFavoriteController(AbstractCommonController controller, int acType, IUserProfileProvider userProfileProvider)
    {
        FavoriteController favoriteController = new FavoriteController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        favoriteController.init(data);
    }
    
    public void startTrafficController(AbstractCommonController controller, int routeId, ITrafficCallback trafficCallback,Address orig ,Address dest, boolean isFromSearchAlong, int acType)
    {
		//FIXME: db, disable it temporary.
//        if (FeaturesManager.getInstance().isEnable(FeaturesManager.APP_CODE_TRAFFIC)
//                && FeaturesManager.getInstance().getStatus(FeaturesManager.APP_CODE_TRAFFIC) != FeaturesManager.PURCHARSED)
//        {
//            startUpSellController(controller, FeaturesManager.APP_CODE_TRAFFIC);
//        }
//        else
        {
            Parameter param = new Parameter();
            param.put(ICommonConstants.KEY_I_ROUTE_ID, routeId);
            param.put(ICommonConstants.KEY_O_TRAFFIC_CALLBACK, trafficCallback);
            param.put(ICommonConstants.KEY_O_ADDRESS_ORI, orig);
            param.put(ICommonConstants.KEY_O_ADDRESS_DEST, dest);
            param.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
            param.put(ICommonConstants.KEY_I_AC_TYPE, acType);
            TrafficController trafficController = new TrafficController(controller);
            trafficController.init(param);
        }
    }
    
    public void startUpSellController(AbstractCommonController controller, String featureCode, boolean isFirstLogin, IUserProfileProvider userProfileProvider)
    {
        startUpSellController(controller,  featureCode,  isFirstLogin,  userProfileProvider,  ICommonConstants.EVENT_CONTROLLER_START);
    }
    
    public void startUpSellController(AbstractCommonController controller, String featureCode, boolean isFirstLogin, IUserProfileProvider userProfileProvider, int  controllerEvent)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_S_FEATURE_CODE, featureCode);
        data.put(ICommonConstants.KEY_S_APP_CODE, FeaturesManager.APP_CODE);
        data.put(ICommonConstants.KEY_B_IS_START_UP, isFirstLogin);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        UpSellController upsellController = new UpSellController(controller);
        upsellController.init(controllerEvent, data);
    }
    
//    public void startShareController(AbstractCommonController controller, Address address, IUserProfileProvider userProfileProvider)
//    {
//        Parameter data = new Parameter();
//        data.put(ICommonConstants.KEY_O_SHARED_ADDRESS, address);
//        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
//        ShareController shareController = new ShareController(controller);
//        shareController.init(data);
//    }
    
    public void startDriveWithFriendsController(AbstractCommonController controller, IUserProfileProvider userProfileProvider,
            String sessionId, String userKey, String userId, String formatedDt, boolean isExitDireclty)
    {
        DriveWithFriendsController dwfController = new DriveWithFriendsController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        data.put(ICommonConstants.KEY_S_DWF_SESSION_ID, sessionId);
        data.put(ICommonConstants.KEY_S_DWF_USER_KEY, userKey);
        data.put(ICommonConstants.KEY_S_DWF_USER_ID, userId);
        data.put(ICommonConstants.KEY_B_DWF_LAUNCH_APP, isExitDireclty);
        
        if(formatedDt != null && formatedDt.length() > 0)
        {
            data.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, DwfUtil.jsonToAddress(formatedDt));
        }
        
        dwfController.init(data);
    }
    
    public void startRecentController(AbstractCommonController controller, int acType, IUserProfileProvider userProfileProvider)
    {
        RecentController recentController = new RecentController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        recentController.init(data);
    }
    
    public void startEditCategoryController(AbstractCommonController controller, FavoriteCatalog category, IUserProfileProvider userProfileProvider)
    {
        EditCategoryController editCategoryController = new EditCategoryController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_CATEGORY, category);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        editCategoryController.init(data);
    }
    
    public void startEditCategoryController(AbstractCommonController controller, FavoriteCatalog category, int controllerEvent, IUserProfileProvider userProfileProvider)
    {
        EditCategoryController editCategoryController = new EditCategoryController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_CATEGORY, category);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        editCategoryController.init(controllerEvent, data);
    }
    
    //For delete
    public void startEditFavoriteController(AbstractCommonController controller, Address operatedAddress, int controllerEvent, IUserProfileProvider userProfileProvider)
    {
        FavoriteEditorController editFavoriteController = new FavoriteEditorController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, operatedAddress);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        editFavoriteController.init(controllerEvent, data);
    }
    
    public void startEditFavoriteController(AbstractCommonController controller, boolean isCreagingFavorite, Address operatedAddress, IUserProfileProvider userProfileProvider)
    {
        FavoriteEditorController editFavoriteController = new FavoriteEditorController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, operatedAddress);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        editFavoriteController.init(data);
    }

    
    public void startEditPlaceController(AbstractCommonController controller, Address editAddress, int editMode, IUserProfileProvider userProfileProvider)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, editAddress);
        data.put(ICommonConstants.KEY_I_PLACE_OPERATION_TYPE, editMode);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        AddPlaceController addPlaceController = new AddPlaceController(controller, true);
        addPlaceController.init(data);
    }
    
    public void startAddPlaceController(AbstractCommonController controller, int selectType, Address addAddress, PoiDataWrapper poiDataWrapper, Address currentAddress, FavoriteCatalog catelog, IUserProfileProvider userProfileProvider)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_PLACE_OPERATION_TYPE, selectType);
        data.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, addAddress);
        data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
        data.put(ICommonConstants.KEY_O_CURRENT_ADDRESS, currentAddress);
        data.put(ICommonConstants.KEY_O_CATEGORY, catelog);
        AddPlaceController addPlaceController = new AddPlaceController(controller, false);
        addPlaceController.init(data);
    }
    
    /**
     * 
     * @param superController
     * @param originAddress  origin address. Optional.
     * @param destAddress    destination address. Must have.
     * @param isFromSearchAlong
     * @param isResumeTrip whether is from resume trip
     */
    public void startNavController(AbstractCommonController superController, Address originAddress, Address destAddress, Vector audioList, boolean isFromSearchAlong, boolean isResumeTrip)
    {
        this.startNavController(superController, originAddress, destAddress, null, false, audioList, isFromSearchAlong, isResumeTrip, true, false);
    }
    
    public void startNavController(AbstractCommonController superController, Address originAddress, Address destAddress, PoiDataWrapper poiDataWrapper, boolean isFromDSR, Vector audioList, boolean isFromSearchAlong, boolean isResumeTrip)
    {
        this.startNavController(superController, originAddress, destAddress, poiDataWrapper, isFromDSR, audioList, isFromSearchAlong, isResumeTrip, true, false);
    }
    /**
     * 
     * @param superController
     * @param originAddress  origin address. Optional.
     * @param destAddress    destination address. Must have.
     * @param isFromSearchAlong
     * @param isResumeTrip whether is from resume trip
     */
    public void startNavController(AbstractCommonController superController, Address originAddress, Address destAddress, PoiDataWrapper poiDataWrapper, boolean isFromDSR, Vector audioList, boolean isFromSearchAlong, boolean isResumeTrip, boolean needShareEta, boolean isFromDwf)
    {
        NavController navController = new NavController(superController);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_ADDRESS_ORI, originAddress);
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
        data.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        data.put(ICommonConstants.KEY_V_STOP_AUDIOS, audioList);
        data.put(ICommonConstants.KEY_B_IS_RESUME_TRIP, isResumeTrip);
        data.put(ICommonConstants.KEY_B_IS_CAR_CONNECT_MODE, CarConnectHostManager.getInstance().isConnected());
        data.put(ICommonConstants.KEY_B_IS_FROM_DSR, isFromDSR);
        data.put(ICommonConstants.KEY_B_NEED_SHARE_ETA, needShareEta);
        data.put(ICommonConstants.KEY_B_IS_FROM_DWF, isFromDwf);
        navController.init(data);
    }
    
    /**
     * 
     * @param superController
     * @param originAddress  origin address. Optional.
     * @param destAddress    destination address. Must have.
     * @param isFromSearchAlong
     * @param isResumeTrip whether is from resume trip
     * @param isIgnoreRoutePlanning
     */
    public void startNavController(AbstractCommonController superController, Address originAddress, Address destAddress, Vector audioList,
            boolean isFromSearchAlong, boolean isResumeTrip, boolean isIgnoreRoutePlanning)
    {
        NavController navController = new NavController(superController);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_ADDRESS_ORI, originAddress);
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        data.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        data.put(ICommonConstants.KEY_V_STOP_AUDIOS, audioList);
        data.put(ICommonConstants.KEY_B_IS_RESUME_TRIP, isResumeTrip);
        data.put(ICommonConstants.KEY_B_IS_IGNORE_ROUTE_PLANNING, isIgnoreRoutePlanning);
        data.put(ICommonConstants.KEY_B_IS_CAR_CONNECT_MODE, CarConnectHostManager.getInstance().isConnected());
        navController.init(data);
    }
    
    /**
     * 
     * @param controller  super controller.
     * @param destAddress  destination address.
     * @param routeId  the id of route you selected in route planning part.
     * @param isFromSearchAlong
     * @param routePlanningLocation
     */
    public void startMovingMapController(AbstractCommonController controller, Address destAddress, PoiDataWrapper poiDataWrapper, String tinyUrl, int routeId, boolean isFromSearchAlong, TnLocation routePlanningLocation)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_O_ADDRESS_DEST, destAddress);
        param.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, poiDataWrapper);
        param.put(ICommonConstants.KEY_S_TINY_URL_ETA, tinyUrl);
        param.put(ICommonConstants.KEY_I_ROUTE_ID, routeId);
        param.put(ICommonConstants.KEY_B_IS_FROM_SEARCH_ALONG, isFromSearchAlong);
        param.put(ICommonConstants.KEY_I_AC_TYPE, ICommonConstants.TYPE_AC_FROM_NAV);
        param.put(ICommonConstants.KEY_O_CURRENT_LOCATION, routePlanningLocation);
        MovingMapController movingMapController = new MovingMapController(controller);
        movingMapController.init(param);
    }
    
    public void startFeedbackController(AbstractCommonController controller, IUserProfileProvider userProfileProvider)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        FeedbackController feedbackController = new FeedbackController(controller);
        feedbackController.init(param);
    }

    public void startSyncResController(AbstractCommonController controller, int syncType)
    {
        SyncResController syncResController = new SyncResController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_SYNC_TYPE, syncType);
        syncResController.init(data);
    }
    
    
    public void startAirportController(AbstractCommonController controller, int acType, IUserProfileProvider userProfileProvider)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        AirportController airportController = new AirportController(controller);
        airportController.init(data);
    }
    
    public void startSetHomeController(AbstractCommonController controller, int addressType, IUserProfileProvider userProfileProvider)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_ADDRESS_TYPE, addressType);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        HomeController setHomeController = new HomeController(controller);
        setHomeController.init(data);
    }
    
    public void startContactsController(AbstractCommonController controller, int acType, IUserProfileProvider userProfileProvider)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        ContactsController contactsController = new ContactsController(controller);
        contactsController.init(data);
        PluginManager.getInstance().setStartContactsFromApp(true);
    }
    
    /**
     * 
     * @param controller super controller.
     * @param origin The origin address of the static route.
     * @param dest The destination address of the static route.
     * @param routeId The id of the route you've selected in route planning part.
     * @param startSegmentIndex From which segment to start. If it comes from route planning part, this should be 0. And
     *            if it's from route summary, it's the segment index you've selected.
     */
    public void startTurnMapController(AbstractCommonController controller, Address origin, Address dest, int routeId, int startSegmentIndex)
    {
        Parameter param = new Parameter();
        param.put(ICommonConstants.KEY_O_ADDRESS_ORI, origin);
        param.put(ICommonConstants.KEY_O_ADDRESS_DEST, dest);
        param.put(ICommonConstants.KEY_I_ROUTE_ID, routeId);
        param.put(ICommonConstants.KEY_I_CURRENT_SEGMENT_INDEX, startSegmentIndex);
        boolean isStaticRoute = NavRunningStatusProvider.getInstance().getNavType() == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE;
        if(isStaticRoute)
        {
            param.put(ICommonConstants.KEY_I_AC_TYPE, ICommonConstants.TYPE_AC_FROM_TURN_MAP);
        }
        else
        {
            param.put(ICommonConstants.KEY_I_AC_TYPE, ICommonConstants.TYPE_AC_FROM_NAV);
        }
        TurnMapController turnMapController = new TurnMapController(controller);
        turnMapController.init(param);
    }
    
    public void startDetailController(AbstractCommonController controller, int detailFromType, int acType, int selectedIndex, PoiDataWrapper dataWrapper, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.POI_DETAIL_DOMAIN_ALIAS);
        
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_I_TYPE_DETAIL_FROM, detailFromType);
        data.put(ICommonConstants.KEY_I_AC_TYPE, acType);
        data.put(ICommonConstants.KEY_I_POI_SELECTED_INDEX, selectedIndex);
        data.put(ICommonConstants.KEY_O_POI_DATA_WRAPPER, dataWrapper);
        data.put(ICommonConstants.KEY_O_BROWSER_SESSION_ARGS, sessionArgs);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        data.put(ICommonConstants.KEY_B_IS_SEARCH_NEAR_BY, isSearchNearBy);
        
        PoiDetailController poiDetailController = new PoiDetailController(controller);
        poiDetailController.init(data);
        
    }
    
    public void startDetailController(AbstractCommonController controller, int acType, int selectedIndex, PoiDataWrapper dataWrapper, IUserProfileProvider userProfileProvider, boolean isSearchNearBy)
    {
        startDetailController(controller, ICommonConstants.TYPE_DETAIL_FROM_POI, acType, selectedIndex, dataWrapper, userProfileProvider, isSearchNearBy);
    }
    
    public void startLaunchLocalApp(AbstractCommonController controller, String url)
    {
        int index = url.indexOf("://");
        String networkType = url.substring(0, index).trim();
        String hostName = url.substring(index + 3);
        
        Host host = CommManager.getInstance().getComm().getHostProvider().createHost(networkType,hostName,0);
        BrowserSessionArgs sessionArgs = new BrowserSessionArgs(host);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_BROWSER_SESSION_ARGS, sessionArgs);
        
        BrowserSdkController localController = new BrowserSdkController(controller);
        localController.init(data);
        
    }
    
    public void startAddressValidatorController(AbstractCommonController controller, String firstLine, String secondLine, String addressMessage, IUserProfileProvider userProfileProvider, String country)
    {
        startAddressValidatorController(controller, firstLine, secondLine, -1, addressMessage, userProfileProvider, country);
    }
    
    public void startAddressValidatorController(AbstractCommonController controller, String firstLine, String secondLine, int fromType, String addressMessage, IUserProfileProvider userProfileProvider, String country)
    {
        if (country == null || country.trim().length() == 0)
        {
            if(userProfileProvider != null)
            {
                country = userProfileProvider.getMandatoryNode().getUserInfo().region;
            }
            else
            {
                country = RegionUtil.getInstance().getCurrentRegion();
            }
        }
        
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_S_ADDRESS_LINE, firstLine);
        data.put(ICommonConstants.KEY_S_SECOND_LINE, secondLine);
        data.put(ICommonConstants.KEY_I_TYPE_ADDRESS_VALIDATOR_FROM, fromType);
        data.put(ICommonConstants.KEY_S_MESSAGE_ADDRESS, addressMessage);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        data.put(ICommonConstants.KEY_S_ADDRESS_VALIDATE_COUNTRY, country);
        AddressValidatorController addressValidatorController = new AddressValidatorController(controller);
        addressValidatorController.init(data);
    }
    
    public void startBrowserSdkController(AbstractCommonController controller, BrowserSessionArgs sessionArgs, boolean fromAppStore, IUserProfileProvider userProfileProvider)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_BROWSER_SESSION_ARGS, sessionArgs);
        data.put(IBrowserSdkConstants.KEY_B_FROM_APPSTORE, fromAppStore);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        
        BrowserSdkController browserSdk = new BrowserSdkController(controller);
        browserSdk.init(data);
    }

    public void startLocalEventController(AbstractCommonController controller, BrowserSessionArgs sessionArgs, String eventId, String eventName)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_BROWSER_SESSION_ARGS, sessionArgs);
        data.put(ICommonConstants.KEY_S_LOCAL_EVENT_ID, eventId);
        data.put(ICommonConstants.KEY_S_LOCAL_EVENT_NAME, eventName);
        
        LocalEventController localEventController = new LocalEventController(controller);
        localEventController.init(data);
    }


    
    public void startAboutController(AbstractCommonController controller)
    {
    	AboutController aboutController = new AboutController(controller);
    	aboutController.init();
    }
    
    public void startAboutController(int controllerEvent, AbstractCommonController controller)
    {
        AboutController aboutController = new AboutController(controller);
        aboutController.init(controllerEvent, new Parameter());
    }
    
    public void startMaiTaiController(AbstractCommonController controller)
    {
        MaiTaiController maitaiController = new MaiTaiController(controller);
        maitaiController.init();
    }
    
    public void startMaiTaiViewController(AbstractCommonController controller)
    {
    	MaiTaiViewController maiTaiViewController = new MaiTaiViewController(controller);
    	maiTaiViewController.init();
    }
    
    public void startPluginController(AbstractCommonController controller, Hashtable requestParam)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_PLUGIN_REQUEST, requestParam);
        PluginController pluginController = new PluginController(controller);
        pluginController.init(data);
    }
    
    public void startStopsSecelctionController(AbstractCommonController controller, Vector addresses, int addressValidatorFrom, String transactionId, IUserProfileProvider userProfileProvider)
    {
        StopsSelectionController stopsSecelctionController = new StopsSelectionController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_V_ALTERNATIVE_ADDRESSES, addresses);
        data.put(ICommonConstants.KEY_I_TYPE_ADDRESS_VALIDATOR_FROM, addressValidatorFrom);
        data.put(ICommonConstants.KEY_S_TRANSACTION_ID, transactionId);
        data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
        stopsSecelctionController.init(data);
    }
    
    public void startRouteSettingController(AbstractCommonController controller, int controllerEvent, boolean isRouteSession, boolean isOnboard)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_B_IS_NAV_SESSION, isRouteSession);
        data.put(ICommonConstants.KEY_B_IS_ONBOARD, isOnboard);
        RouteSettingController routeSettingController = new RouteSettingController(controller);
        routeSettingController.init(controllerEvent, data);
    }
    
    public void startSwitchLangController(AbstractCommonController controller, String locale, int prefValue)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_S_CHANGED_LANGUAGE, locale);
        data.put(ICommonConstants.KEY_I_CHANGED_LANGUAGE, prefValue);
        SwitchLanguageController langController = new SwitchLanguageController(controller);
        langController.init(data);
    }
    
    public void startDashboardController(AbstractCommonController controller, boolean isFirstTime)
    {
        DashboardController dashboardController = new DashboardController(controller);
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_IS_START_APP, isFirstTime);
        dashboardController.init(data);
    }
    
    public void startMyNav(AbstractCommonController controller)
    {
        startAcController(controller, ICommonConstants.TYPE_AC_FROM_DASHBOARD, false, false, false, null );
    }
    
    public void startTestProxyController(AbstractCommonController controller)
    {
        TestProxyController testProxyController = new TestProxyController(controller);
        testProxyController.init();
    }
    
    public void startMapDownLoadController(AbstractCommonController controller)
    {
        Parameter param = new Parameter();
        MapDownLoadController mapDownLoadController = new MapDownLoadController(controller);
        mapDownLoadController.init(param);
    }
    
    public void startVbbDetailController(AbstractCommonController controller, String vbbUrl, Address addressDest)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_S_AD_DETAIL_URL, vbbUrl);
        data.put(ICommonConstants.KEY_O_ADDRESS_DEST, addressDest);
        VbbDetailController vbbDetailController = new VbbDetailController(controller);
        vbbDetailController.init(data);
    }
    
    
    public void startNativeShareController(AbstractCommonController controller, Address address, IUserProfileProvider userProfileProvider)
    {
        Parameter data = new Parameter();
        data.put(ICommonConstants.KEY_O_SELECTED_ADDRESS, address);
        if (address != null)
        {
            int type = address.getType();
            if (type == Address.TYPE_FAVORITE_POI || type == Address.TYPE_RECENT_POI)
            {
                PoiMisLogHelper.getInstance().recordPoiMisLog(IMisLogConstants.TYPE_POI_SHARE, address.getPoi());
            }
        }
        NativeShareController nativeShareManager = new NativeShareController(controller);
        nativeShareManager.init(data);
    }
    
    public void startDrvingShareSettingController(AbstractCommonController controller)
    {
        Parameter param = new Parameter();
        DrivingShareController drivingShareController = new DrivingShareController(controller);
        drivingShareController.init(param);
    }
}
