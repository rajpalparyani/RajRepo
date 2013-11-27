/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DashboardViewTouch.java
 *
 */
package com.telenav.module.dashboard;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telenav.app.CommManager;
import com.telenav.app.IApplicationListener;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.cache.WebViewCacheManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.i18n.ResourceBundle;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.FriendlyUserRatingUtil;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.browsersdk.IBrowserSdkConstants;
import com.telenav.module.entry.SplashScreenInflater;
import com.telenav.module.mapdownload.IMapDownloadStatusListener;
import com.telenav.module.mapdownload.IOnBoardDataAvailabilityListener;
import com.telenav.module.mapdownload.MapDownloadOnBoardDataStatusManager;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractModel;
import com.telenav.res.IStringAc;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDashboard;
import com.telenav.res.IStringMap;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCircleAnimation;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.citizen.map.DayNightService;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2012-2-3
 */
public class DashboardViewTouch extends AbstractCommonMapView implements IDashboardConstants , IBrowserSdkConstants, /*IGLSnapBitmapCallBack,*/ITnScreenAttachedListener, IApplicationListener, IAccountTypeChangeListener, IOnBoardDataAvailabilityListener, IMapDownloadStatusListener
{
    // used in those screen with 320dp width
    private static final int DROPDOWN_TEXT_SIZE_SMALL = 14;

    private boolean islocationExpanded = false;

    private boolean isFirstTimeDropdownShownPortrait = false;
    private boolean isFirstTimeDropdownShownLandscape = false;
    
    private boolean needHandleDropdownAnimation = false;
    private boolean isMockDropdownAnimationRunning = false;
    
    private int oldOrientation = 0;
    
    private int miniMapHeight = 0;
    
    private int mapContainerHeight = 0;
    
    private int miniMapWidth = 0;
    
    private int mapContainerHeightLandscape = 0;
    
    private int miniMapHeightLandscape = 0;
    
    private int miniMapWidthLandscape = 0;
    
    private String weatherIconFile = null;
    
    private String weatherIconFileLandscape = null;
    
    private View portraitView = null;
    
    private View landscapeView = null;
    
    private boolean registeredDropdownNotifyListener;

    public DashboardViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
        
        mapContainerHeight = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT);
        miniMapHeight = mapContainerHeight
                - (int) (AndroidCitizenUiHelper.getResourceDimension(R.dimen.dashboard0LocationLinearLayoutLayoutHeight));
        miniMapWidth = DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH);
        mapContainerHeightLandscape = (int) (DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_HEIGHT_LAND) 
                - (((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getStatusBarHeight(0)));
        miniMapHeightLandscape = mapContainerHeightLandscape - (int)(AndroidCitizenUiHelper.getResourceDimension(R.dimen.dashboard0LocationLinearLayoutLayoutHeightLandscape));
        
        MapContainer.getInstance().setMapUIEventListener(this);
        miniMapWidthLandscape= DaoManager.getInstance().getSimpleConfigDao().get(SimpleConfigDao.KEY_MINI_MAP_WIDTH_LAND);
    }
    
    protected TnPopupContainer createPopup(int state)
    {
        switch (state)
        {
            case STATE_SHOW_FRIEND_USER_POPUP:
            {
                return FriendlyUserRatingUtil.createFriendlyRating();
            }
            case STATE_POI_SEARCHING:
            {
                return createSearchProgressPopup();
            }
            case STATE_BROWSER_GOTO_LAUNCH_LOCALAPP:
            {
                CitizenScreen dashboardScreen = (CitizenScreen) this.getScreenByState(STATE_DASHBOARD);
                if (null != dashboardScreen)
                {
                    AbstractTnContainer adjugllerContainer = (AbstractTnContainer) dashboardScreen.getComponentById(ID_DASHBOARD_ADJUGGLER_CONTAINER);
                    if (null != adjugllerContainer)
                    {
                        ((AbstractTnContainer)adjugllerContainer.getParent()).remove(adjugllerContainer);
                    }
                }
                return null;
            }
        }
        return null;
    }
    
    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_DASHBOARD:
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_GLOBAL, KontagentLogger.GLOBAL_DASHBOARD_SCREEN_DISPLAYED);
                return createDashboardScreen(state);
            }
            case STATE_VIEW_TEST_CASE:
            {
                DashboardViewTestCase viewTestCase = new DashboardViewTestCase((DashboardUiDecorator)uiDecorator);
                return viewTestCase.createTestCaseScreen(state);
            }
            case STATE_GENERAL_FEEDBACK:
            {
                return createGeneralFeedbackScreen(state, UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR));
            }
            case STATE_WEATHER_DETAIL:
            {
                return createWeatherScreen(state);
            }
        }
        return null;
    }

    public TnScreen createDashboardScreen(int state)
    {
        boolean isPreload = !isDashboardState(state);
        oldOrientation = ((AbstractTnUiHelper) AndroidUiHelper.getInstance()).getOrientation();
        CitizenScreen mainScreen = null;
        if (isPreload)
        {
            mainScreen = new CitizenScreen(state);
        }
        else
        {
            mainScreen = UiFactory.getInstance().createScreen(state);
        }
        // *******************************************************************************************
        // Debug code here, to see the background map
        // *******************************************************************************************
        // TnMenu menu = mainScreen.getRootContainer().getMenu(AbstractTnComponent.TYPE_MENU);
        // menu.add("Text Hide All", CMD_TEST_HIDE_ALL);
        // mainScreen.getRootContainer().setMenu(menu, AbstractTnComponent.TYPE_MENU);
        // *******************************************************************************************
        createDashboardScreenContainer(mainScreen, oldOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT);
        return mainScreen;
    }
    
    private void showPortraitView()
    {
        if (portraitView != null)
        {
            portraitView.setVisibility(View.VISIBLE);
        }
    }
    
    private void showLandscapeView()
    {
        if (landscapeView != null)
        {
            landscapeView.setVisibility(View.VISIBLE);
        }        
    }
    
    private void hidePortraitView()
    {
        if (portraitView != null)
        {
            portraitView.setVisibility(View.GONE);
        }
    }

    private void hideLandscapeView()
    {
        if (landscapeView != null)
        {
            landscapeView.setVisibility(View.GONE);
        }        
    }

  
    private void createDashboardScreenContainer(CitizenScreen mainScreen, boolean isPortrait)
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String mapCopyright = DaoManager.getInstance().getStartupDao().getMapCopyright();
        if (isPortrait)
        {
            hideLandscapeView();
            portraitView = AndroidCitizenUiHelper.addContentView(mainScreen, R.layout.dashboard);
            portraitView.setVisibility(View.VISIBLE);
            ImageView DSRimage = (ImageView) portraitView.findViewById(R.id.commonOneboxDsrButton);
            View oneBoxDSR = (View) portraitView.findViewById(R.id.commonOneboxDsrButton);
            TextView searchBoxHint = (TextView) portraitView.findViewById(R.id.commonOneboxTextView);
            TextView locationCity = (TextView) portraitView.findViewById(R.id.dashboard0LocationCityFieldExpand);
            TextView locationAddress = (TextView) portraitView.findViewById(R.id.dashboard0LocationAddressFieldExpand);
            ImageView locationWeatherImage = (ImageView) portraitView
                    .findViewById(R.id.dashboard0LocationWeatherImageFieldExpand);
            TextView location_weather = (TextView) portraitView.findViewById(R.id.dashboard0LocationWeatherTextFieldExpand);
            ImageView location_scout_icon_expand = (ImageView) portraitView
                    .findViewById(R.id.dashboard0LocationScout_expand);
            TextView mapProvider = (TextView) portraitView.findViewById(R.id.dashboard0mapProvider);
            if (mapProvider != null)
            {
                mapProvider.setText(mapCopyright);
            }
            View mapContainerExpand = (View) portraitView.findViewById(R.id.dashboard0expand);
            View driveHomeWorkContainer = (View) portraitView.findViewById(R.id.dashboard0DriveHomeWorkContainer);
            View placeContainer = (View) portraitView.findViewById(R.id.dashboard0placeContainer);
            View driveResumeContainer = (View) portraitView.findViewById(R.id.dashboard0DriveResumeContainer);
            View dropDownView = portraitView.findViewById(R.id.dashboard0DropDown);
            updateMiniMap(mapContainerExpand, isPortrait);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapContainerExpand.getLayoutParams();
            params.height = mapContainerHeight;
           // params.width = miniMapWidth;
            mapContainerExpand.setLayoutParams(params);

            mapContainerExpand.setVisibility(View.VISIBLE);
            ImageView ivExpand = (ImageView) portraitView.findViewById(R.id.dashboard0LocationAddressActionButtonIconExpand);
            boolean shouldHideDropdown = DashboardManager.getInstance().getCurrentAddress() == null;
            if (shouldHideDropdown)
            {
                islocationExpanded = false;
                ivExpand.setVisibility(View.INVISIBLE);
            }
            isFirstTimeDropdownShownPortrait = islocationExpanded;
            if (islocationExpanded)
            {
                if (android.os.Build.VERSION.SDK_INT >= 11)
                {
                    // use the system transformation directly
                    ivExpand.setRotation(180);
                }
                else
                {
                    // work around for pre-honeycomb devices
                    float size = AndroidCitizenUiHelper
                            .getResourceDimension(R.dimen.dashboard0LocationAddressActionButtonSize);
                    Matrix matrix = new Matrix();
                    matrix.setScale(1f, -1f);
                    matrix.postTranslate(0, size);
                    ivExpand.setScaleType(ScaleType.MATRIX);
                    ivExpand.setImageMatrix(matrix);
                }
            }
            View shareLocationBtn = dropDownView.findViewById(R.id.dashboard0LocationShare);
            View addPlaceBtn = dropDownView.findViewById(R.id.dashboard0LocationAdd);
            if (!NetworkStatusManager.getInstance().isConnected())
            {
                // disable the drop down buttons
                shareLocationBtn.setEnabled(false);
                addPlaceBtn.setEnabled(false);
            }
            else
            {
                shareLocationBtn.setEnabled(true);
                addPlaceBtn.setEnabled(true);
            }
            dropDownView.setVisibility(!islocationExpanded ? View.GONE : View.VISIBLE);
            if (DashboardManager.getInstance().hasResumeTrip())
            {
                driveHomeWorkContainer.setVisibility(View.GONE);
                driveResumeContainer.setVisibility(View.VISIBLE);
                updateResumeTrip(driveResumeContainer, isPortrait);
            }
            else
            {
                driveHomeWorkContainer.setVisibility(View.VISIBLE);
                driveResumeContainer.setVisibility(View.GONE);
                updateWorkHome(driveHomeWorkContainer, isPortrait);
            }
            
            // location&weather
            updateLocationWeather(locationCity, locationAddress, location_weather, locationWeatherImage, isPortrait);
            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
            this.model.put(KEY_B_IS_ONBOARD, isOnboard);
            int dsrImageId = isOnboard ? R.drawable.inputbox_mic_icon_disabled : R.drawable.inputbox_mic_icon;
            DSRimage.setImageResource(dsrImageId);
            if(isOnboard)
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
            }
            else
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
            }
            int scoutIconId;
            int mapColor = DayNightService.getInstance().getMapColor(false);
            if (mapColor != IMapContainerConstants.MAP_DAY_COLOR)
            {
                scoutIconId = R.drawable.scout_logo_night_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderNightTextColor));
            }
            else
            {
                scoutIconId = R.drawable.scout_logo_daytime_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderDayTextColor));
            }
            location_scout_icon_expand.setImageResource(scoutIconId);
            updatePlaces(placeContainer, isOnboard, isPortrait);
            AndroidCitizenUiHelper.setOnClickCommand(this, oneBoxDSR, isOnboard ? CMD_NONE : CMD_COMMON_DSR);
            AndroidCitizenUiHelper.setOnClickCommand(this, portraitView, UI_COMMAND_TABLE);
            
            LinearLayout webViewContainer = (LinearLayout)portraitView.findViewById(R.id.dashboard0adButton);
            createAdjuggler(webViewContainer, model.getState());
        }
        else
        {
            hidePortraitView();
            
            landscapeView = AndroidCitizenUiHelper.addContentView(mainScreen, R.layout.dashboard_landscape);
            landscapeView.setVisibility(View.VISIBLE);

            ImageView DSRimage = (ImageView) landscapeView.findViewById(R.id.commonOneboxDsrButton_landscape);
            View oneBoxDSR = (View) landscapeView.findViewById(R.id.commonOneboxDsrButton_landscape);
            TextView searchBoxHint = (TextView) landscapeView.findViewById(R.id.commonOneboxTextView_landscape);
            TextView location_city = (TextView) landscapeView
                    .findViewById(R.id.dashboard0LocationCityFieldExpand_landscape);
            TextView location_Address = (TextView) landscapeView
                    .findViewById(R.id.dashboard0LocationAddressFieldExpand_landscape);
            ImageView location_weather_image = (ImageView) landscapeView
                    .findViewById(R.id.dashboard0LocationWeatherImageFieldExpand_landscape);
            TextView location_weather = (TextView) landscapeView
                    .findViewById(R.id.dashboard0LocationWeatherTextFieldExpand_landscape);
            ImageView location_scout_icon_expand = (ImageView) landscapeView
                    .findViewById(R.id.dashboard0LocationScout_landscape);
            TextView mapProvider = (TextView) landscapeView.findViewById(R.id.dashboard0mapProvider_landscape);
            if(mapProvider!=null)
            {
                mapProvider.setText(mapCopyright);
            }
            View mapContainerExpand = (View) landscapeView.findViewById(R.id.dashboard0location_landscape);
            View placeContainer = (View) landscapeView.findViewById(R.id.dashboard0placeContainer_landscape);
            View driveHomeWorkContainer = (View) landscapeView
                    .findViewById(R.id.dashboard0DriveHomeWorkContainer_landscape);
            View driveResumeContainer = (View) landscapeView.findViewById(R.id.dashboard0DriveResumeContainer_landscape);
            View dropDownView = landscapeView.findViewById(R.id.dashboard0DropDown_landscape);
            TextView shareLocationText = (TextView) landscapeView.findViewById(R.id.dashboard0LocationShareText_landscape);
            TextView addLocationText = (TextView) landscapeView.findViewById(R.id.dashboard0LocationAddText_landscape);
            updateMiniMap(mapContainerExpand, isPortrait);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapContainerExpand.getLayoutParams();
            params.height = mapContainerHeightLandscape;
            mapContainerExpand.setLayoutParams(params);

            mapContainerExpand.setVisibility(View.VISIBLE);
            ImageView ivExpand = (ImageView) landscapeView
                    .findViewById(R.id.dashboard0LocationAddressActionButtonIconExpand_landscape);
            boolean shouldHideDropdown = DashboardManager.getInstance().getCurrentAddress() == null;
            if (shouldHideDropdown)
            {
                islocationExpanded = false;
                ivExpand.setVisibility(View.INVISIBLE);
            }
            isFirstTimeDropdownShownLandscape = islocationExpanded;
            if (islocationExpanded)
            {
                if (android.os.Build.VERSION.SDK_INT >= 11)
                {
                    // use the system transformation directly
                    ivExpand.setRotation(180);
                }
                else
                {
                    // work around for pre-honeycomb devices
                    float size = AndroidCitizenUiHelper
                            .getResourceDimension(R.dimen.dashboard0LocationAddressActionButtonSize);
                    Matrix matrix = new Matrix();
                    matrix.setScale(1f, -1f);
                    matrix.postTranslate(0, size);
                    ivExpand.setScaleType(ScaleType.MATRIX);
                    ivExpand.setImageMatrix(matrix);
                }
            }
            float minScreenSize = AndroidCitizenUiHelper
                    .getResourceDimension(R.dimen.common_min_screen_width);
            if (AppConfigHelper.getMinDisplaySize() <= minScreenSize)
            {
                // the height in landscape mode is limited, so we use a smaller text size
                shareLocationText.setTextSize(DROPDOWN_TEXT_SIZE_SMALL);
                addLocationText.setTextSize(DROPDOWN_TEXT_SIZE_SMALL);
            }
            View shareLocationBtn = dropDownView.findViewById(R.id.dashboard0LocationShare_landscape);
            View addPlaceBtn = dropDownView.findViewById(R.id.dashboard0LocationAdd_landscape);
            if (!NetworkStatusManager.getInstance().isConnected())
            {
                // disable the drop down buttons
                shareLocationBtn.setEnabled(false);
                addPlaceBtn.setEnabled(false);
            }
            else
            {
                shareLocationBtn.setEnabled(true);
                addPlaceBtn.setEnabled(true);
            }
            dropDownView.setVisibility(!islocationExpanded ? View.GONE : View.VISIBLE);
            if (DashboardManager.getInstance().hasResumeTrip())
            {
                driveHomeWorkContainer.setVisibility(View.GONE);
                driveResumeContainer.setVisibility(View.VISIBLE);
                updateResumeTrip(driveResumeContainer, isPortrait);
            } 
            else
            {
                driveHomeWorkContainer.setVisibility(View.VISIBLE);
                driveResumeContainer.setVisibility(View.GONE);
                updateWorkHome(driveHomeWorkContainer, isPortrait);
            }
            // location&weather
            updateLocationWeather(location_city, location_Address, location_weather, location_weather_image, isPortrait);
            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
            this.model.put(KEY_B_IS_ONBOARD, isOnboard);
            int DSRImageId = isOnboard ? R.drawable.inputbox_mic_icon_disabled : R.drawable.inputbox_mic_icon;
            DSRimage.setImageResource(DSRImageId);
            if(isOnboard)
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
            }
            else
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
            }
            int scoutIconId;
            int mapColor = DayNightService.getInstance().getMapColor(false);
            if (mapColor != IMapContainerConstants.MAP_DAY_COLOR)
            {
                scoutIconId = R.drawable.scout_logo_night_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderNightTextColor));
            }
            else
            {
                scoutIconId = R.drawable.scout_logo_daytime_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderDayTextColor));
            }
            location_scout_icon_expand.setImageResource(scoutIconId);
            updatePlaces(placeContainer, isOnboard, isPortrait);
            AndroidCitizenUiHelper.setOnClickCommand(this, oneBoxDSR, isOnboard ? CMD_NONE : CMD_COMMON_DSR);
            AndroidCitizenUiHelper.setOnClickCommand(this, landscapeView, UI_COMMAND_TABLE_LANDSCAPE);
            LinearLayout webViewContainer = (LinearLayout)landscapeView.findViewById(R.id.dashboard0adButton_landscape);
            createAdjuggler(webViewContainer, model.getState());
        }
        
        updateBadge();
    }
    
    protected void updateLocationWeather(TextView cityLabel, TextView streetLabel, TextView weatherLabel, ImageView weatherImage, boolean isPortrait)
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String weatherIconFocusedFile = "dashboard_weather_icon_$_focused";
        String weatherIconUnfocusedFile = "dashboard_weather_icon_$_unfocused";
        String weatherIconFileNew = null;

        String temperatureInFahrenheit = DashboardManager.getInstance().getWeatherTempInFahrenheit();
        String temperatureInCelsius = DashboardManager.getInstance().getWeatherTempInCelsius();
        String weatherCode = DashboardManager.getInstance().getWeatherCode();
        boolean isWeatherFocused = weatherImage.isFocused();
        final boolean hasWeather = (temperatureInFahrenheit != null && temperatureInFahrenheit.trim().length() > 0) || (weatherCode != null
                && weatherCode.length() > 0);
        if (hasWeather)
        {
            if (isWeatherFocused)
            {
                weatherIconFocusedFile = weatherIconFocusedFile.replace("$", weatherCode);
                weatherIconFileNew = weatherIconFocusedFile;
            }
            else
            {
                weatherIconUnfocusedFile = weatherIconUnfocusedFile.replace("$", weatherCode);
                weatherIconFileNew = weatherIconUnfocusedFile;
            }
        }
        String degreeSymbol = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringDashboard.RES_STRING_DEGREE_SYMBOL, IStringDashboard.FAMILY_DASHBOARD);
        int systemUnits = ((DaoManager) DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_DISTANCEUNIT);
        if (systemUnits == Preference.UNIT_USCUSTOM)
        {
            weatherLabel.setText(temperatureInFahrenheit + degreeSymbol);
        }
        if (systemUnits == Preference.UNIT_METRIC)
        {
            weatherLabel.setText(temperatureInCelsius + degreeSymbol);
        }
        if(isPortrait)
        {
            boolean iconChanged = weatherIconFileNew != null && weatherIconFileNew.length() > 0
                    && !weatherIconFileNew.equals(weatherIconFile);
            if (iconChanged)
            {
                weatherIconFile = weatherIconFileNew;

                int id = AndroidPersistentContext
                        .getInstance()
                        .getContext()
                        .getResources()
                        .getIdentifier(weatherIconFile, "drawable",
                            AndroidPersistentContext.getInstance().getContext().getPackageName());
                weatherImage.setImageResource(id);
            }
        }
        else
        {
            boolean iconChanged = weatherIconFileNew != null && weatherIconFileNew.length() > 0
                    && !weatherIconFileNew.equals(weatherIconFileLandscape);
            if (iconChanged)
            {
                weatherIconFileLandscape = weatherIconFileNew;

                int id = AndroidPersistentContext
                        .getInstance()
                        .getContext()
                        .getResources()
                        .getIdentifier(weatherIconFileLandscape, "drawable",
                            AndroidPersistentContext.getInstance().getContext().getPackageName());
                weatherImage.setImageResource(id);
            }
        }
        String city = DashboardManager.getInstance().getCityName();
        String street = DashboardManager.getInstance().getStreetName();
        Address currentAddress =  DashboardManager.getInstance().getCurrentAddress();
        boolean hasLocation = currentAddress != null;
        boolean cityChanged = !city.equals(cityLabel.getText());
        boolean streetChanged =  !street.equals(streetLabel.getText());
        // boolean locationChanged = cityChanged || streetChanged;
        if (cityChanged)
        {
            cityLabel.setText(city);
        }
        if (streetChanged)
        {
            streetLabel.setText(street);
        }
        
        if (!hasLocation)
        {
            int rgcStatus = DashboardManager.getInstance().getRgcStatus();
            if(rgcStatus == DashboardManager.STATUS_RGC_REQUESTING)
            {
                cityLabel.setText(bundle.getString(IStringDashboard.RES_STRING_LOCATING, IStringDashboard.FAMILY_DASHBOARD));
            }
            else if(rgcStatus == DashboardManager.STATUS_RGC_NOT_GOT)
            {
                cityLabel.setText(bundle.getString(IStringDashboard.RES_STRING_UNKNOWN, IStringDashboard.FAMILY_DASHBOARD));
                
                //This indicate that this is an address with unknown label.
                Stop anchor = new Stop();
                anchor.setType(Stop.STOP_CURRENT_LOCATION);
                TnLocation loc = DashboardManager.getInstance().getCurrentLocation();
                if(loc!= null)
                {
                    anchor.setLat(loc.getLatitude());
                    anchor.setLon(loc.getLongitude());
                }
                currentAddress = new Address();
                currentAddress.setStop(anchor);
            }
            streetLabel.setVisibility(View.INVISIBLE);
            
//            View dropDownView = null;
//            View ivExpand = null;
//            if(isPortrait())
//            {
//                dropDownView = portraitView.findViewById(R.id.dashboard0DropDown);
//                ivExpand = (ImageView) portraitView.findViewById(R.id.dashboard0LocationAddressActionButtonIconExpand);
//            }
//            else
//            {
//                dropDownView = landscapeView.findViewById(R.id.dashboard0DropDown_landscape);
//                ivExpand = (ImageView) landscapeView.findViewById(R.id.dashboard0LocationAddressActionButtonIconExpand_landscape);
//               
//            }
//           
//            if(dropDownView != null)
//            {
//                dropDownView.setVisibility(View.INVISIBLE);
//            }
//            
//            if(ivExpand != null)
//            {
//                ivExpand.setVisibility(View.INVISIBLE);
//            }
            
        }
        else
        {
            cityLabel.setVisibility(View.VISIBLE);
            streetLabel.setVisibility(View.VISIBLE);
        }
        this.model.put(KEY_O_CURRENT_ADDRESS, currentAddress);
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        if (!hasWeather || isOnboard )
        {
            weatherLabel.setVisibility(View.INVISIBLE);
            weatherImage.setVisibility(View.INVISIBLE);
        }
        else
        {
            if (temperatureInFahrenheit != null && temperatureInFahrenheit.trim().length() > 0)
            {
                weatherLabel.setVisibility(View.VISIBLE);
            }
            else
            {
                weatherLabel.setVisibility(View.INVISIBLE);
            }
            weatherImage.setVisibility(View.VISIBLE);
        }
    }
    
    protected CitizenCircleAnimation createCircleAnimation()
    {
        CitizenCircleAnimation circleAnimation = UiFactory.getInstance()
                .createCircleAnimation(ID_DASHBOARD_CIRCLE_ANNIMATION, false);
        circleAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((DashboardUiDecorator) uiDecorator).CIRCLE_RADIUS);
        circleAnimation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((DashboardUiDecorator) uiDecorator).CIRCLE_RADIUS);
        int dropSize = AppConfigHelper.getMinDisplaySize() / 100;
        if (dropSize <= 2)
        {
            dropSize = 3;
        }
        int[] dropSizes =
        { dropSize, dropSize - 1, dropSize - 2 };
        circleAnimation.setDropSizes(dropSizes);
        return circleAnimation;
    }
    
    private TnScreen createWeatherScreen(int state)
    {
        CitizenScreen weatherScreen = UiFactory.getInstance().createScreen(state);
        weatherScreen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR));
        AbstractTnContainer container = weatherScreen.getContentContainer();
        CitizenWebComponent weatherWebComponent = UiFactory.getInstance().createCitizenWebComponent(this, 0, null, false);
        weatherWebComponent.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler)model);
        
        BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.WEATHER_DOMAIN_ALIAS);
        String url = sessionArgs.getUrl();
        
        url = BrowserSdkModel.addEncodeTnInfo(url, "");
        
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        weatherWebComponent.loadUrl(url);
        container.add(weatherWebComponent);
        return weatherScreen;
    }
    
    protected void createAdjuggler(CitizenScreen citizenScreen, int state, boolean isPortrait)
    {
        View view = (View)citizenScreen.getContentContainer().getNativeUiComponent();
        
        LinearLayout webViewContainer;
        if(isPortrait)
        {
            webViewContainer = (LinearLayout)view.findViewById(R.id.dashboard0adButton);
        }
        else
        {
            webViewContainer = (LinearLayout)view.findViewById(R.id.dashboard0adButton_landscape);
        }
        
        createAdjuggler(webViewContainer, state);
    }
    
    protected void createAdjuggler(ViewGroup parent, int state)
    {
        if(isDashboardState(state))
        {
            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
            parent.removeAllViews();
            if (!isOnboard)
            {
                AdJuggerManager.getInstance().addAdjugglerView(parent, (IHtmlSdkServiceHandler) model);
            }
        }
    }
    
    protected void createTestCase(AbstractTnContainer parent)
    {
        FrogButton testcase = new FrogButton(0, "TestCase");
        testcase.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((DashboardUiDecorator) uiDecorator).TOP_CONTAINER_WIDTH);
        testcase.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).SCOUT_WEBVIEW_HEIGHT);
        testcase.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        testcase.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_HIGHLIGHT_FOCUSED_COLOR), UiStyleManager.getInstance()
                .getColor(UiStyleManager.DASHBOARD_NORMAL_STRING_COLOR));
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_VIEW_TEST_CASE);
        testcase.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        parent.add(testcase);
    }
    
    private void updateResumeTrip(View driveResumeContainer, boolean isPortrait)
    {
        if(driveResumeContainer == null)
            return;
        TextView resumeAddrLabelText = null;
        TextView resumeDescText = null;
        
        if(isPortrait)
        {
            resumeAddrLabelText = (TextView) driveResumeContainer.findViewById(R.id.dashboardResumeAddrLabel);
            resumeDescText = (TextView) driveResumeContainer.findViewById(R.id.dashboardResumeDescText);
        }
        else
        {
            resumeAddrLabelText = (TextView) driveResumeContainer.findViewById(R.id.dashboardResumeAddrLabel_landscape);
            resumeDescText = (TextView) driveResumeContainer.findViewById(R.id.dashboardResumeDescText_landscape);
        }
        Address lastTrip = DaoManager.getInstance().getTripsDao().getLastTrip();
        String lastTripString = "";
        if(lastTrip != null)
        {
            if(lastTrip.getLabel() != null && lastTrip.getLabel().trim().length() != 0)
            {
                lastTripString = lastTrip.getLabel();
            }
            else
            {
                lastTripString = ResourceManager.getInstance().getStringConverter().convertAddress(lastTrip.getStop(), false);
            }
        }

        ColorStateList resumeTimeColorTransparent = null;
        ColorStateList resumeTimeColor = null;
        try
        {
            XmlResourceParser xrpTransparent = AndroidCitizenUiHelper.getResources().getXml(R.color.dashboard_drive_resume_time_trasparent_font);
            resumeTimeColorTransparent = ColorStateList.createFromXml(AndroidCitizenUiHelper.getResources(), xrpTransparent);
            XmlResourceParser xrp = AndroidCitizenUiHelper.getResources().getXml(R.color.dashboard_drive_resume_time_font);
            resumeTimeColor = ColorStateList.createFromXml(AndroidCitizenUiHelper.getResources(), xrp);
        }
        catch (Exception e)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "ColorStateList.createFromXml get error!");
        }

        int status = DashboardManager.getInstance().getLastTripEtaStatus();
        if(status == DashboardManager.STATUS_ETA_TOO_CLOSE)
        { 
            if (resumeTimeColorTransparent != null)
            {
                resumeDescText.setTextColor(resumeTimeColorTransparent);
            }
            AndroidCitizenUiHelper.setOnClickCommand(this, driveResumeContainer, CMD_NONE); 
        }
        else if (status == DashboardManager.STATUS_ETA_NOT_GOT)
        {
            if (resumeTimeColorTransparent != null)
            {
                resumeDescText.setTextColor(resumeTimeColorTransparent);
            }
            AndroidCitizenUiHelper.setOnClickCommand(this, driveResumeContainer, CMD_RESUME_TRIP); 
        }
        else if(status == DashboardManager.STATUS_ETA_REQUESTING)
        {
            if (resumeTimeColor != null)
            {
                resumeDescText.setTextColor(resumeTimeColor);
            }
            AndroidCitizenUiHelper.setOnClickCommand(this, driveResumeContainer, CMD_RESUME_TRIP);
        }
        else if(status == DashboardManager.STATUS_ETA_NORMAL)
        {
            if (resumeTimeColor != null)
            {
                resumeDescText.setTextColor(resumeTimeColor);
            }
            AndroidCitizenUiHelper.setOnClickCommand(this, driveResumeContainer, CMD_RESUME_TRIP);
        }
        String etaText = DashboardManager.getInstance().getLastTripEtaStr();
        if(etaText.trim().length() != 0 && etaText.toLowerCase().startsWith("s"))
        {
            resumeDescText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        else
        {
            resumeDescText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
        }
        resumeDescText.setText(DashboardManager.getInstance().getLastTripEtaStr());
        resumeAddrLabelText.setText(lastTripString);
    }
    
    private void updateWorkHome(View driveHomeWorkContainer, boolean isPortrait)
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        boolean isValidHome = isValidHome();
        boolean isValidWork = isValidWork();
        View homeButton = null;
        View workButton = null;
        TextView homeDescription = null;
        TextView homeMain =  null;
        TextView workDescription = null;
        TextView workMain = null;
        if(isPortrait)
        {
            homeButton = (View)driveHomeWorkContainer.findViewById(R.id.dashboard0DriveHomeButton);
            workButton = (View)driveHomeWorkContainer.findViewById(R.id.dashboard0DriveWorkButton);
            if(homeButton != null)
            {
                homeDescription = (TextView)homeButton.findViewById(R.id.dashboard0HomeDesc);
                homeMain = (TextView)homeButton.findViewById(R.id.dashboard0HomeMain);
            }
            if(workButton != null)
            {
                workDescription = (TextView)workButton.findViewById(R.id.dashboard0WorkDesc); 
                workMain = (TextView)workButton.findViewById(R.id.dashboard0WorkMain);
            }                     
        }
        else
        {
            homeButton = (View)driveHomeWorkContainer.findViewById(R.id.dashboard0DriveHomeButton_landscape);
            workButton = (View)driveHomeWorkContainer.findViewById(R.id.dashboard0DriveWorkButton_landscape);
            if(homeButton != null)
            {
                homeDescription = (TextView)homeButton.findViewById(R.id.dashboard0HomeDesc_landscape);   
                homeMain = (TextView)homeButton.findViewById(R.id.dashboard0HomeMain_landscape);
            }
            if(workButton != null)
            {
                workDescription = (TextView)workButton.findViewById(R.id.dashboard0WorkDesc_landscape);
                workMain = (TextView)workButton.findViewById(R.id.dashboard0WorkMain_landscape);
            }            
        }
        if(homeButton != null && homeDescription != null)
        {
            if(isValidHome)
            {
                int status = DashboardManager.getInstance().getHomeEtaStatus();
                String etaText = DashboardManager.getInstance().getHomeEtaStr();
                if (status == DashboardManager.STATUS_ETA_TOO_CLOSE)
                {
                    homeDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveHomeButtonDescTransparentTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, homeButton, CMD_NONE);
                }
                else if (status == DashboardManager.STATUS_ETA_NOT_GOT)
                {
                    homeDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveHomeButtonDescTransparentTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, homeButton, CMD_HOME);
                }
                else if (status == DashboardManager.STATUS_ETA_REQUESTING)
                {
                    homeDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveHomeButtonDescTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, homeButton, CMD_HOME);
                }
                else if (status == DashboardManager.STATUS_ETA_NORMAL)
                {
                    homeDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveHomeButtonDescTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, homeButton, CMD_HOME);
                }
                if(etaText.trim().length() != 0 && etaText.toLowerCase().startsWith("s"))
                {
                    homeDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                }
                else
                {
                    homeDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                }
                
                homeDescription.setText(etaText);
                homeMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                homeMain.setText(bundle.getString(IStringDashboard.RES_STRING_TO_HOME, IStringDashboard.FAMILY_DASHBOARD));
            }
            else
            {
                homeDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                homeDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveHomeButtonDescTextColor));
                homeDescription.setText(bundle.getString(IStringDashboard.RES_STRING_HOME, IStringDashboard.FAMILY_DASHBOARD));
                
                homeMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                homeMain.setText(bundle.getString(IStringDashboard.RES_STRING_SET_UP, IStringDashboard.FAMILY_DASHBOARD));
                
                AndroidCitizenUiHelper.setOnClickCommand(this, homeButton, CMD_HOME);
            }
            
        }
        if(workButton != null && workDescription != null)
        {
            if(isValidWork)
            {
                int status = DashboardManager.getInstance().getWorkEtaStatus();
                String etaText = DashboardManager.getInstance().getWorkEtaStr();
                if (status == DashboardManager.STATUS_ETA_TOO_CLOSE)
                {
                    workDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveWorkButtonDescTransparentTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, workButton, CMD_NONE);
                }
                else if (status == DashboardManager.STATUS_ETA_NOT_GOT)
                {
                    workDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveWorkButtonDescTransparentTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, workButton, CMD_WORK);
                }
                else if (status == DashboardManager.STATUS_ETA_REQUESTING)
                {
                    workDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveWorkButtonDescTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, workButton, CMD_WORK);
                }
                else if (status == DashboardManager.STATUS_ETA_NORMAL)
                {
                    workDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveWorkButtonDescTextColor));
                    AndroidCitizenUiHelper.setOnClickCommand(this, workButton, CMD_WORK);
                }
                if(etaText.trim().length() != 0 && etaText.toLowerCase().startsWith("s"))
                {
                    workDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                }
                else
                {
                    workDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                }
                workDescription.setText(etaText);
                workMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                workMain.setText(bundle.getString(IStringDashboard.RES_STRING_TO_WORK, IStringDashboard.FAMILY_DASHBOARD));
            }
            else
            {
                workDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                workDescription.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0DriveWorkButtonDescTextColor));
                workDescription.setText(bundle.getString(IStringDashboard.RES_STRING_WORK, IStringDashboard.FAMILY_DASHBOARD));
                
                workMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                workMain.setText(bundle.getString(IStringDashboard.RES_STRING_SET_UP, IStringDashboard.FAMILY_DASHBOARD));
                
                AndroidCitizenUiHelper.setOnClickCommand(this, workButton, CMD_WORK);
            }
            
        }  
    }
    
    protected TnMenu createHomeWorkContextMenu(boolean isHome, boolean isClose)
    {
        int contextMenuTitle;
        int driveToId = -1;
        int mapId;
        int editId;
        if(isHome)
        {
            contextMenuTitle = IStringAc.RES_BTTN_HOME;
            if(!isClose)
            {
                driveToId = CMD_DRIVETO_HOME;
            }
            mapId = CMD_MAP_HOME;
            editId = CMD_EDIT_HOME;
        }
        else
        {
            contextMenuTitle = IStringAc.RES_BTN_WORK;
            if(!isClose)
            {
                driveToId = CMD_DRIVETO_WORK;
            }
            mapId = CMD_MAP_WORK;
            editId = CMD_EDIT_WORK;
        }
        TnMenu contextMenu = UiFactory.getInstance().createMenu();
        contextMenu
                .setHeaderTitle(ResourceManager.getInstance().getCurrentBundle().getString(contextMenuTitle, IStringAc.FAMILY_AC));
        if(driveToId != -1)
        {
            contextMenu.add(
                ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_DRIVETO, IStringCommon.FAMILY_COMMON),
                driveToId);
        }
        contextMenu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringAc.RES_CONTEXT_EDIT, IStringAc.FAMILY_AC),
            editId);
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_MENU_MAP, IStringCommon.FAMILY_COMMON),
            mapId);
        return contextMenu;
    }
    
    protected boolean isValidHome()
    {
        Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
        return ((DashboardModel)model).isValidAddress(home);
    }
    
    protected boolean isValidWork()
    {
        Stop office = DaoManager.getInstance().getAddressDao().getOfficeAddress();
        return ((DashboardModel)model).isValidAddress(office);
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_DASHBOARD:
            {
                switch (commandId)
                {
                    case CMD_HOME:
                    case CMD_EDIT_HOME:
                    {
                        model.put(KEY_I_HOME_WORK_TYPE, TYPE_HOME_ADDRESS);
                        break;
                    }
                    case CMD_WORK:
                    case CMD_EDIT_WORK:
                    {
                        model.put(KEY_I_HOME_WORK_TYPE, TYPE_WORK_ADDRESS);
                        break;
                    }
                }
                break;
            }
            case STATE_VIEW_TEST_CASE:
            {
                if (commandId == CMD_COMMON_BACK)
                {
                    CitizenScreen currentScreen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance())
                            .getCurrentScreen();
                    if (currentScreen != null)
                    {
                        AbstractTnContainer container = currentScreen.getContentContainer();
                        CitizenTextField cityTextField = (CitizenTextField)container.get(0);
                        CitizenTextField streetTextField = (CitizenTextField)container.get(1);
                        CitizenTextField tempTextField = (CitizenTextField)container.get(2);
                        CitizenTextField homeTextField = (CitizenTextField)container.get(4);
                        CitizenTextField workTextField = (CitizenTextField)container.get(5);
                        String city = cityTextField.getText();
                        String street = streetTextField.getText();
                        String temp = tempTextField.getText();
                        String home = homeTextField.getText();
                        String work = workTextField.getText();
                        if (city != null && city.length() > 0)
                        {
                            model.put(KEY_S_DASHBOARD_CITY_NAME, city);
                        }
                        if (street != null && street.length() > 0)
                        {
                            model.put(KEY_S_DASHBOARD_STREET_NAME, street);
                        }
                        if (temp != null && temp.length() > 0)
                        {
                            String degreeSymbol = ResourceManager.getInstance().getCurrentBundle().getString(IStringDashboard.RES_STRING_DEGREE_SYMBOL, IStringDashboard.FAMILY_DASHBOARD);
                            model.put(KEY_S_DASHBOARD_WEATHER_TEMP, temp + degreeSymbol);
                        }
                        if (home != null && home.length() > 0)
                        {
                            model.put(KEY_S_HOME_DYNAMIC_ETA, home);
                        }
                        if (work != null && work.length() > 0)
                        {
                            model.put(KEY_S_WORK_DYNAMIC_ETA, work);
                        }
                    }
                }
                else if (commandId == DashboardViewTestCase.CMD_TEST_CASE_SELECT_NEARBY_HOME)
                {
                    model.put(KEY_B_IS_CLOSE_TO_HOME, true);
                }
                else if (commandId == DashboardViewTestCase.CMD_TEST_CASE_SELECT_NEARBY_WORK)
                {
                    model.put(KEY_B_IS_CLOSE_TO_WORK, true);
                }
                else
                {
                    model.put(KEY_S_DASHBOARD_WEATHER_CODE, String.valueOf(commandId - DashboardViewTestCase.CMD_TEST_CASE_SELECT_ICON_BASE_ID));
                }
                break;
            }
        }
        return super.prepareModelData(state, commandId);
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();

        switch (state)
        {
            case STATE_DASHBOARD:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    if(tnUiEvent.getCommandEvent() != null && tnUiEvent.getCommandEvent().getCommand() == CMD_HIDE_NOTIFICATION)
                    {
                        notificationContainer = null;
                        return true;
                    }
                    // *******************************************************************************************
                    // Debug code here
                    // *******************************************************************************************
                    // else if (tnUiEvent.getCommandEvent().getCommand() == CMD_TEST_HIDE_ALL)
                    // {
                    // CitizenScreen screen = (CitizenScreen) this.getScreenByState(STATE_DASHBOARD);
                    // View backgroundView = (View) screen.getBackgroundComponent().getNativeUiComponent();
                    // ViewGroup rootView = (ViewGroup) backgroundView.getParent();
                    // rootView.removeViewAt(1);
                    // return true;
                    // }
                    // *******************************************************************************************
                    else if (tnUiEvent.getCommandEvent().getCommand() == CMD_COMMON_GOTO_ONEBOX)
                    {
                        if (tnUiEvent.getComponent() instanceof FrogTextField)
                        {
                            FrogTextField textField = (FrogTextField) tnUiEvent.getComponent();
                            String text = textField.getText();
                            this.model.put(KEY_S_COMMON_SEARCH_TEXT, text);
                            textField.setText("");
                        }
                    }
                    else if (tnUiEvent.getCommandEvent().getCommand() == CMD_POI_CATEGORY_SELECTED)
                    {
                        if (tnUiEvent.getComponent() instanceof FrogButton)
                        {
                            FrogButton button = (FrogButton) tnUiEvent.getComponent();
                            int id = button.getId();
                            if(id != model.getInt(KEY_I_CATEGORY_ID))
                            {
                                model.remove(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                                this.model.put(KEY_I_CATEGORY_ID, id);
                            }
                        }
                        else if(tnUiEvent.getComponent() != null && tnUiEvent.getComponent().getCookie() instanceof View)
                        {
                            View cView = (View)tnUiEvent.getComponent().getCookie();
                            model.remove(KEY_S_COMMON_SHOW_SEARCH_TEXT);
                            this.model.put(KEY_I_CATEGORY_ID, cView.getTag());
                        }
                    }
                    else if (tnUiEvent.getCommandEvent().getCommand() == CMD_CLOSE_RESUME)
                    {
                        if(tnUiEvent.getComponent() != null && tnUiEvent.getComponent().getCookie() instanceof View)
                        {
                            CitizenScreen dashboardScreen = (CitizenScreen) this.getScreenByState(STATE_DASHBOARD);
                            View view = (View)dashboardScreen.getContentContainer().getNativeUiComponent();
                            int orientation = ((AbstractTnUiHelper) AndroidUiHelper.getInstance()).getOrientation();
                            
                            View driveHomeWorkContainer;
                            View driveResumeContainer;
                            if(orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
                            {
                                driveHomeWorkContainer = (View) view.findViewById(R.id.dashboard0DriveHomeWorkContainer);
                                driveResumeContainer = (View) view.findViewById(R.id.dashboard0DriveResumeContainer);
                            }
                            else
                            {
                                driveHomeWorkContainer = (View) view.findViewById(R.id.dashboard0DriveHomeWorkContainer_landscape);
                                driveResumeContainer = (View) view.findViewById(R.id.dashboard0DriveResumeContainer_landscape);
                            }
                            driveHomeWorkContainer.setVisibility(View.VISIBLE);
                            driveResumeContainer.setVisibility(View.GONE);
                            DaoManager.getInstance().getTripsDao().clear();
                            DashboardManager.getInstance().notifyUpdateEta();
                        }
                    }
                    else if (tnUiEvent.getCommandEvent().getCommand() == CMD_FOLD_LOCATION)
                    {
                        needHandleDropdownAnimation = true;
                    }
                }
                break;
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    private void handleDropDownAnimation(final View indicator, final View dropdown, boolean isPotrait)
    {
        boolean firstTimeDropdownShown;
        if (isPotrait)
        {
            firstTimeDropdownShown = isFirstTimeDropdownShownPortrait;
        }
        else
        {
            firstTimeDropdownShown = isFirstTimeDropdownShownLandscape;
        }
        if (islocationExpanded)
        {
            // hide the view
            if(model.fetchBool(KEY_B_MANUAL_OPRERATE_SHARE_ADD_PANEL))
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_SAVESHARE_PANEL_CLOSED);
            }
            Animation outAnim = AnimationUtils
                    .loadAnimation(AndroidPersistentContext.getInstance().getContext(), R.anim.up_out);

            outAnim.setAnimationListener(new Animation.AnimationListener()
            {

                @Override
                public void onAnimationStart(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    islocationExpanded = false;
                    dropdown.setVisibility(View.GONE);
                    isMockDropdownAnimationRunning = false;

                }
            });
            Animation rollBackAnim = AnimationUtils.loadAnimation(AndroidPersistentContext.getInstance().getContext(),
                firstTimeDropdownShown ? R.anim.anticlockwise_180_mirror : R.anim.anticlockwise_180);

            rollBackAnim.setAnimationListener(new Animation.AnimationListener()
            {

                @Override
                public void onAnimationStart(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    // do nothing
                }
            });
            dropdown.startAnimation(outAnim);
            indicator.startAnimation(rollBackAnim);
            this.model.put(KEY_B_HAS_HIDDEN_DROPDOWN, true);

        }
        else if (DashboardManager.getInstance().getCurrentAddress() != null)
        {
            // show the view

            Animation inAnim = AnimationUtils
                    .loadAnimation(AndroidPersistentContext.getInstance().getContext(), R.anim.down_in);
            inAnim.setAnimationListener(new Animation.AnimationListener()
            {

                @Override
                public void onAnimationStart(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    islocationExpanded = true;
                    isMockDropdownAnimationRunning = false;
                }
            });
            Animation rollAnim = AnimationUtils.loadAnimation(AndroidPersistentContext.getInstance().getContext(),
                firstTimeDropdownShown ? R.anim.clockwise_180_mirror : R.anim.clockwise_180);
            rollAnim.setAnimationListener(new Animation.AnimationListener()
            {

                @Override
                public void onAnimationStart(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {
                    // do nothing
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    // do nothing
                }
            });
            dropdown.setVisibility(View.VISIBLE);
            dropdown.startAnimation(inAnim);
            indicator.startAnimation(rollAnim);
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.KEY_HAS_SHOWN_ADD_SHARE_PANEL, true);
            ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().store();
            if(model.fetchBool(KEY_B_MANUAL_OPRERATE_SHARE_ADD_PANEL))
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD, KontagentLogger.DASHBOARD_SAVESHARE_PANEL_OPENED);
            }
        }
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_DASHBOARD:
            {
                CitizenScreen citizenScreen = (CitizenScreen)screen;
                
                boolean isUpdateMiniMapOnly = this.model.fetchBool(KEY_B_UPDATE_MINI_MAP_ONLY);
                if (isUpdateMiniMapOnly)
                {
                    int currentOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                    boolean isPortrait = currentOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                    View view = (View)citizenScreen.getContentContainer().getNativeUiComponent(); 
                    View mapContainerExpand = null;
                    if (isPortrait)
                    {
                        mapContainerExpand = (View) view.findViewById(R.id.dashboard0expand);
                    }
                    else
                    {
                        mapContainerExpand = (View) view.findViewById(R.id.dashboard0location_landscape);
                    }
                    
                    if (mapContainerExpand != null)
                    {
                        updateMiniMap(mapContainerExpand, isPortrait);
                    }
                    return false;
                }
                else
                {
                    boolean isBackToMyProfile = this.model.fetchBool(KEY_B_IS_START_BY_OTHER_CONTROLLER);
                    if (isBackToMyProfile && isCredentialInfoExisted())
                    {
                        showNotificationContainer(STATE_DASHBOARD);
                    }
                    boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
                    int currentOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                    if (oldOrientation != currentOrientation)
                    {
                        oldOrientation = currentOrientation;
                    }
                    boolean isPortrait = oldOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                    
                    if(checkDashboardContainer(isPortrait))
                    {
                        updateDashboardScreenContainer(citizenScreen, isPortrait, isOnboard);
                    }
                    else
                    {
                        createDashboardScreenContainer(citizenScreen, isPortrait);
                    }
                    return true;
                }
            }
            case STATE_DRIVE_TO:
            {
                return false;
            }
            case STATE_VIEW_TEST_CASE:
            {
                return true;
            }
        }
        return false;
    }
    
    private void updateDashboardScreenContainer(CitizenScreen citizenScreen, boolean isPortrait, boolean isOnboard)
    {
        if (this.model.fetchBool(KEY_B_MOCK_LOCATION_BAR_CLICK))
        {
            this.model.put(KEY_B_MANUAL_OPRERATE_SHARE_ADD_PANEL, false);
            needHandleDropdownAnimation = true;
            isMockDropdownAnimationRunning = true;
        }
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String mapCopyright = DaoManager.getInstance().getStartupDao().getMapCopyright();
        if(isPortrait)
        {
            hideLandscapeView();
            showPortraitView();
            
            View view = (View)citizenScreen.getContentContainer().getNativeUiComponent();
            
            ImageView DSRimage = (ImageView) view.findViewById(R.id.commonOneboxDsrButton);
            View oneBoxDSR = (View) view.findViewById(R.id.commonOneboxDsrButton);
            TextView searchBoxHint = (TextView) portraitView.findViewById(R.id.commonOneboxTextView);
            int DSRImageId = isOnboard ? R.drawable.inputbox_mic_icon_disabled : R.drawable.inputbox_mic_icon;
            DSRimage.setImageResource(DSRImageId);
            if(isOnboard)
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
            }
            else
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
            }
            TextView location_city_expand = (TextView) view.findViewById(R.id.dashboard0LocationCityFieldExpand);
            TextView location_Address_expand = (TextView) view.findViewById(R.id.dashboard0LocationAddressFieldExpand);
            ImageView location_weather_image_expand = (ImageView) view.findViewById(R.id.dashboard0LocationWeatherImageFieldExpand);
            TextView location_weather_expand = (TextView) view.findViewById(R.id.dashboard0LocationWeatherTextFieldExpand);
            ImageView location_scout_icon_expand = (ImageView) view.findViewById(R.id.dashboard0LocationScout_expand);
            TextView mapProvider = (TextView) view.findViewById(R.id.dashboard0mapProvider);
            if (mapProvider != null)
            {
                mapProvider.setText(mapCopyright);
            }
            View mapContainerExpand = (View) view.findViewById(R.id.dashboard0expand);
            View dropDownView = view.findViewById(R.id.dashboard0DropDown);
            View dropDownIndicator = view.findViewById(R.id.dashboard0LocationAddressActionButtonIconExpand);
            View driveHomeWorkContainer = (View) view.findViewById(R.id.dashboard0DriveHomeWorkContainer);
            View driveResumeContainer = (View) view.findViewById(R.id.dashboard0DriveResumeContainer);
            View placeContainer = (View) view.findViewById(R.id.dashboard0placeContainer);
            boolean shouldHideDropdown = DashboardManager.getInstance().getCurrentAddress() == null;
            dropDownIndicator.setVisibility(shouldHideDropdown ? View.INVISIBLE : View.VISIBLE);
            View shareLocationBtn = dropDownView.findViewById(R.id.dashboard0LocationShare);
            View addPlaceBtn = dropDownView.findViewById(R.id.dashboard0LocationAdd);
            if (isOnboard)
            {
                // disable the drop down buttons
                shareLocationBtn.clearFocus();
                shareLocationBtn.setPressed(false);
                shareLocationBtn.setEnabled(false);
                addPlaceBtn.clearFocus();
                addPlaceBtn.setPressed(false);
                addPlaceBtn.setEnabled(false);
            }
            else
            {
                shareLocationBtn.setEnabled(true);
                addPlaceBtn.setEnabled(true);
            }
            
            if (shouldHideDropdown)
            {
                dropDownIndicator.setAnimation(null);
            }
            if (dropDownIndicator.getVisibility() != View.VISIBLE)
            {
                dropDownView.setVisibility(View.GONE);
                islocationExpanded = false;
            }
            if (needHandleDropdownAnimation)
            {
                handleDropDownAnimation(dropDownIndicator, dropDownView, true);
                needHandleDropdownAnimation = false;
            }
            else if (!isMockDropdownAnimationRunning)
            {
                dropDownView.setVisibility(!islocationExpanded ? View.GONE : View.VISIBLE);
                if (dropDownIndicator.getVisibility() == View.VISIBLE)
                {
                    int size = (int) (AndroidCitizenUiHelper.getResourceDimension(R.dimen.dashboard0LocationAddressActionButtonSize) + 0.5f);
                    float half = size / 2.f;
                    float angle = ((!islocationExpanded) ^ isFirstTimeDropdownShownPortrait) ? 0 : 180;
                    // Initialize the Animation object
                    Animation animation = new RotateAnimation(angle, angle, half, half);
                    // "Save" the results of the animation
                    animation.setFillAfter(true);
                    // Set the animation duration to zero, just in case
                    animation.setDuration(0);
                    // Assign the animation to the view
                    dropDownIndicator.setAnimation(animation);
                }
            }

            updateLocationWeather(location_city_expand, location_Address_expand, location_weather_expand, location_weather_image_expand, isPortrait);
            updatePlaces(placeContainer, isOnboard, isPortrait);

            if (DashboardManager.getInstance().hasResumeTrip())
            {   
                driveHomeWorkContainer.setVisibility(View.GONE);
                driveResumeContainer.setVisibility(View.VISIBLE);
                updateResumeTrip(driveResumeContainer, isPortrait);              
            }
            else
            {
                driveHomeWorkContainer.setVisibility(View.VISIBLE);
                driveResumeContainer.setVisibility(View.GONE);
                updateWorkHome(driveHomeWorkContainer, isPortrait);                      
            }
            
            int scoutIconId;
            int mapColor = DayNightService.getInstance().getMapColor(false);
            if (mapColor != IMapContainerConstants.MAP_DAY_COLOR)
            {
                scoutIconId = R.drawable.scout_logo_night_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderNightTextColor));
            }
            else
            {
                scoutIconId = R.drawable.scout_logo_daytime_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderDayTextColor));
            }
            location_scout_icon_expand.setImageResource(scoutIconId);
            
            if (model.fetchBool(KEY_B_NEED_REFRESH_MINI_MAP))
            {
                updateMiniMap(mapContainerExpand, isPortrait);
            }
            
            AndroidCitizenUiHelper.setOnClickCommand(this, oneBoxDSR, isOnboard ? CMD_NONE : CMD_COMMON_DSR);
        }
        else
        {
            hidePortraitView();
            showLandscapeView();
            
            View view = (View)citizenScreen.getContentContainer().getNativeUiComponent();
            
            ImageView DSRimage = (ImageView) view.findViewById(R.id.commonOneboxDsrButton_landscape);
            View oneBoxDSR = (View) view.findViewById(R.id.commonOneboxDsrButton_landscape);
            TextView searchBoxHint = (TextView) view.findViewById(R.id.commonOneboxTextView_landscape);
            int DSRImageId = isOnboard ? R.drawable.inputbox_mic_icon_disabled : R.drawable.inputbox_mic_icon;
            DSRimage.setImageResource(DSRImageId);
            if(isOnboard)
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
            }
            else
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
            }
            TextView location_city_expand = (TextView) view.findViewById(R.id.dashboard0LocationCityFieldExpand_landscape);
            TextView location_Address_expand = (TextView) view.findViewById(R.id.dashboard0LocationAddressFieldExpand_landscape);
            ImageView location_weather_image_expand = (ImageView) view.findViewById(R.id.dashboard0LocationWeatherImageFieldExpand_landscape);
            TextView location_weather_expand = (TextView) view.findViewById(R.id.dashboard0LocationWeatherTextFieldExpand_landscape);
            ImageView location_scout_icon_expand = (ImageView) view.findViewById(R.id.dashboard0LocationScout_landscape);
            TextView mapProvider = (TextView) view.findViewById(R.id.dashboard0mapProvider_landscape);
            if (mapProvider != null)
            {
                mapProvider.setText(mapCopyright);
            }
            View mapContainerExpand = (View) view.findViewById(R.id.dashboard0location_landscape);
            View dropDownView = view.findViewById(R.id.dashboard0DropDown_landscape);
            View dropDownIndicator = view.findViewById(R.id.dashboard0LocationAddressActionButtonIconExpand_landscape);
            View driveHomeWorkContainer = (View) view.findViewById(R.id.dashboard0DriveHomeWorkContainer_landscape);
            View driveResumeContainer = (View) view.findViewById(R.id.dashboard0DriveResumeContainer_landscape);  
            View placeContainer = (View) view.findViewById(R.id.dashboard0placeContainer_landscape);
            boolean shouldHideDropdown = DashboardManager.getInstance().getCurrentAddress() == null;
            dropDownIndicator.setVisibility(shouldHideDropdown ? View.INVISIBLE : View.VISIBLE);

            View shareLocationBtn = dropDownView.findViewById(R.id.dashboard0LocationShare_landscape);
            View addPlaceBtn = dropDownView.findViewById(R.id.dashboard0LocationAdd_landscape);
            if (isOnboard)
            {
                // disable the drop down buttons
                shareLocationBtn.clearFocus();
                shareLocationBtn.setPressed(false);
                shareLocationBtn.setEnabled(false);
                addPlaceBtn.clearFocus();
                addPlaceBtn.setPressed(false);
                addPlaceBtn.setEnabled(false);
            }
            else
            {
                shareLocationBtn.setEnabled(true);
                addPlaceBtn.setEnabled(true);
            }
            if (shouldHideDropdown)
            {
                dropDownIndicator.setAnimation(null);
            }
            if (dropDownIndicator.getVisibility() != View.VISIBLE)
            {
                dropDownView.setVisibility(View.GONE);
                islocationExpanded = false;
            }
            if (needHandleDropdownAnimation)
            {
                handleDropDownAnimation(dropDownIndicator, dropDownView, false);
                needHandleDropdownAnimation = false;
            }
            else if (!isMockDropdownAnimationRunning)
            {
                dropDownView.setVisibility(!islocationExpanded ? View.GONE : View.VISIBLE);
                if (dropDownIndicator.getVisibility() == View.VISIBLE)
                {
                    int size = (int) (AndroidCitizenUiHelper.getResourceDimension(R.dimen.dashboard0LocationAddressActionButtonSize) + 0.5f);
                    float half = size / 2.f;
                    float angle = ((!islocationExpanded) ^ isFirstTimeDropdownShownLandscape) ? 0 : 180;
                    // Initialize the Animation object
                    Animation animation = new RotateAnimation(angle, angle, half, half);
                    // "Save" the results of the animation
                    animation.setFillAfter(true);
                    // Set the animation duration to zero, just in case
                    animation.setDuration(0);
                    // Assign the animation to the view
                    dropDownIndicator.setAnimation(animation);
                }
            }

            updateLocationWeather(location_city_expand, location_Address_expand, location_weather_expand, location_weather_image_expand, isPortrait);
            updatePlaces(placeContainer, isOnboard, isPortrait);

            if (DashboardManager.getInstance().hasResumeTrip())
            {   
                driveHomeWorkContainer.setVisibility(View.GONE);
                driveResumeContainer.setVisibility(View.VISIBLE);
                updateResumeTrip(driveResumeContainer, isPortrait);              
            }
            else
            {
                driveHomeWorkContainer.setVisibility(View.VISIBLE);
                driveResumeContainer.setVisibility(View.GONE);
                updateWorkHome(driveHomeWorkContainer, isPortrait);                      
            }
            
            int scoutIconId;
            int mapColor = DayNightService.getInstance().getMapColor(false);
            if (mapColor != IMapContainerConstants.MAP_DAY_COLOR)
            {
                scoutIconId = R.drawable.scout_logo_night_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderNightTextColor));
            }
            else
            {
                scoutIconId = R.drawable.scout_logo_daytime_unfocused;
                mapProvider.setTextColor(AndroidCitizenUiHelper.getResourceColor(R.color.dashboard0MapProviderDayTextColor));
            }
            location_scout_icon_expand.setImageResource(scoutIconId);
            
            if (model.fetchBool(KEY_B_NEED_REFRESH_MINI_MAP))
            {
                 updateMiniMap(mapContainerExpand, isPortrait);
			}
            AndroidCitizenUiHelper.setOnClickCommand(this, oneBoxDSR, isOnboard ? CMD_NONE : CMD_COMMON_DSR);
        }
        
        if (DashboardManager.getInstance().getCurrentAddress() != null && NetworkStatusManager.getInstance().isConnected())
        {
            boolean hasShownDropdown = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_HAS_SHOWN_ADD_SHARE_PANEL);
            if (!hasShownDropdown && !registeredDropdownNotifyListener)
            {
                registeredDropdownNotifyListener = true;
                Notifier.getInstance().addListener((INotifierListener) this.model.get(KEY_O_DROPDOWN_HINT_LISTENER));

            }
        }
        
        updateBadge();
    }

    private void updatePlaces(View placeContainer, boolean isOnboard, boolean isPortrait)
    {
        boolean hasGps = DashboardManager.getInstance().getCurrentLocation() != null;
        if (isPortrait)
        {
            View coffeeButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceCoffeeButton);
            ImageView coffeeIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesCoffeeImage);
            View foodButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceFoodButton);
            ImageView foodIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesFoodImage);
            View gasButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceGasButton);
            ImageView gasIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesGasImage);
//             View atmButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceAtmButton);
//             ImageView atmIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesAtmImage);
            View moreButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceMoreButton);
            ImageView moreIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesMoreImage);
            View dwfButton = placeContainer.findViewById(R.id.dashboard0DriveDWFButton);
            View dwfActiveIcon = placeContainer.findViewById(R.id.dashboard0DriveDWFActiveIcon);
            
            if (isOnboard || !hasGps)
            {
                coffeeButton.setBackgroundResource(R.drawable.dashboard_button_left_disabled);
                coffeeIcon.setImageResource(R.drawable.dashboard_poi_icon_coffee_disabled);
                foodButton.setBackgroundResource(R.drawable.dashboard_button_middle_disabled);
                foodIcon.setImageResource(R.drawable.dashboard_poi_icon_food_disabled);
                gasButton.setBackgroundResource(R.drawable.dashboard_button_middle_disabled);
                gasIcon.setImageResource(R.drawable.dashboard_poi_icon_gas_disabled);
//                 atmButton.setBackgroundResource(R.drawable.dashboard_button_middle_disabled);
//                 atmIcon.setImageResource(R.drawable.dashboard_poi_icon_atm_disabled);
                moreButton.setBackgroundResource(R.drawable.dashboard_button_right_disabled);
                moreIcon.setImageResource(R.drawable.dashboard_poi_icon_more_disabled);
                coffeeButton.setEnabled(false);
                foodButton.setEnabled(false);
                 gasButton.setEnabled(false);
//                 atmButton.setEnabled(false);
                moreButton.setEnabled(false);
                dwfButton.setEnabled(false);
            }
            else
            {
                coffeeButton.setBackgroundResource(R.drawable.dashboard_button_background_left);
                coffeeIcon.setImageResource(R.drawable.dashboard_poi_icon_coffee);
                foodButton.setBackgroundResource(R.drawable.dashboard_button_background_center);
                foodIcon.setImageResource(R.drawable.dashboard_poi_icon_food);
                gasButton.setBackgroundResource(R.drawable.dashboard_button_background_center);
                gasIcon.setImageResource(R.drawable.dashboard_poi_icon_gas);
//                 atmButton.setBackgroundResource(R.drawable.dashboard_button_background_center);
//                 atmIcon.setImageResource(R.drawable.dashboard_poi_icon_atm);
                moreButton.setBackgroundResource(R.drawable.dashboard_button_background_right);
                moreIcon.setImageResource(R.drawable.dashboard_poi_icon_more);
                coffeeButton.setEnabled(true);
                foodButton.setEnabled(true);
                gasButton.setEnabled(true);
//                 atmButton.setEnabled(true);
                moreButton.setEnabled(true);
                dwfButton.setEnabled(true);
            }
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
            try
            {
                if(dwfAidl != null && dwfAidl.getSharingIntent() != null)
                {
                    dwfButton.setEnabled(true);
                    dwfActiveIcon.setVisibility(View.VISIBLE);
                }
                else
                {
                    dwfActiveIcon.setVisibility(View.GONE);
                }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            View coffeeButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceCoffeeButton_landscape);
            ImageView coffeeIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesCoffeeImage_landscape);
            View foodButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceFoodButton_landscape);
            ImageView foodIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesFoodImage_landscape);
            View gasButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceGasButton_landscape);
            ImageView gasIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesGasImage_landscape);
//             View atmButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceAtmButton_landscape);
//             ImageView atmIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesAtmImage_landscape);
            View moreButton = (View) placeContainer.findViewById(R.id.dashboard0PlaceMoreButton_landscape);
            ImageView moreIcon = (ImageView) placeContainer.findViewById(R.id.dashboard0PlacesMoreImage_landscape);
            View dwfButton = placeContainer.findViewById(R.id.dashboard0DriveDWFButton);
            View dwfActiveIcon = placeContainer.findViewById(R.id.dashboard0DriveDWFActiveIcon_landscape);
            if (isOnboard || !hasGps)
            {
                coffeeButton.setBackgroundResource(R.drawable.dashboard_button_top_landscape_disabled);
                coffeeIcon.setImageResource(R.drawable.dashboard_poi_icon_coffee_disabled);
                foodButton.setBackgroundResource(R.drawable.dashboard_button_middle_landscape_disabled);
                foodIcon.setImageResource(R.drawable.dashboard_poi_icon_food_disabled);
                gasButton.setBackgroundResource(R.drawable.dashboard_button_middle_landscape_disabled);
                gasIcon.setImageResource(R.drawable.dashboard_poi_icon_gas_disabled);
//                 atmButton.setBackgroundResource(R.drawable.dashboard_button_middle_landscape_disabled);
//                 atmIcon.setImageResource(R.drawable.dashboard_poi_icon_atm_disabled);
                moreButton.setBackgroundResource(R.drawable.dashboard_button_bottom_landscape_disabled);
                moreIcon.setImageResource(R.drawable.dashboard_poi_icon_more_disabled);
                coffeeButton.setClickable(false);
                foodButton.setClickable(false);
                gasButton.setClickable(false);
                // atmButton.setClickable(false);
                moreButton.setClickable(false);
                dwfButton.setEnabled(false);
            }
            else
            {
                coffeeButton.setBackgroundResource(R.drawable.dashboard_button_top_landscape);
                coffeeIcon.setImageResource(R.drawable.dashboard_poi_icon_coffee);
                foodButton.setBackgroundResource(R.drawable.dashboard_button_middle_landscape);
                foodIcon.setImageResource(R.drawable.dashboard_poi_icon_food);
                gasButton.setBackgroundResource(R.drawable.dashboard_button_middle_landscape);
                gasIcon.setImageResource(R.drawable.dashboard_poi_icon_gas);
//                 atmButton.setBackgroundResource(R.drawable.dashboard_button_middle_landscape);
//                 atmIcon.setImageResource(R.drawable.dashboard_poi_icon_atm);
                moreButton.setBackgroundResource(R.drawable.dashboard_button_bottom_landscape);
                moreIcon.setImageResource(R.drawable.dashboard_poi_icon_more);
                coffeeButton.setClickable(true);
                foodButton.setClickable(true);
                gasButton.setClickable(true);
//                 atmButton.setClickable(true);
                moreButton.setClickable(true);
                dwfButton.setEnabled(true);
            }
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
            try
            {
                if(dwfAidl != null && dwfAidl.getSharingIntent() != null)
                {
                    dwfButton.setEnabled(true);
                    dwfActiveIcon.setVisibility(View.VISIBLE);
                }
                else
                {
                    dwfActiveIcon.setVisibility(View.GONE);
                }
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        return cmd;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }
    
    protected int getImageKey(boolean isPortraitRequested)
    {
        return isPortraitRequested ? KEY_PORTRAIT_SNAPPED_IMAGE : KEY_LANDSCAPE_SNAPPED_IMAGE;
    }
    
    protected void updateMiniMap(final View mapContainerExpand, final boolean isPortrait)
    {
        if (isPortrait)
        {
            final ImageView mapExpand = (ImageView) mapContainerExpand.findViewById(R.id.dashboard0MapViewExpand);
            final View mapProgressExpand = (View) mapContainerExpand.findViewById(R.id.minimap_progress_expand);
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Bitmap image = DashboardManager.getInstance().getMiniMap(isPortrait());
                    if (image != null && !image.isRecycled())
                    {
                        mapProgressExpand.setVisibility(View.GONE);
                        mapExpand.setVisibility(View.VISIBLE);
                        mapExpand.setImageBitmap(image);
                        String searchText = ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_DEFAULT_MAP_TITLE, IStringMap.FAMILY_MAP);
                        mapExpand.setContentDescription(searchText);
                        Logger.log(Logger.INFO, "DashboardViewTouch", "=== MiniMap ===: updateMiniMap with image");
                    }
                    else
                    {
                        ((Activity) AndroidPersistentContext.getInstance().getContext()).setProgressBarVisibility(true);
                        mapProgressExpand.setVisibility(View.VISIBLE);
                        Logger.log(Logger.INFO, "DashboardViewTouch", "=== MiniMap ===: updateMiniMap with animation");
                    }
                }
            });
        }
        else
        {
            final ImageView mapExpand = (ImageView) mapContainerExpand.findViewById(R.id.dashboard0MapViewExpand_landscape);
            final View mapProgressExpand = (View) mapContainerExpand.findViewById(R.id.minimap_progress_expand_landscape);
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Bitmap image = DashboardManager.getInstance().getMiniMap(isPortrait());
                    if (image != null && !image.isRecycled())
                    {
                        mapProgressExpand.setVisibility(View.GONE);
                        mapExpand.setVisibility(View.VISIBLE);
                        mapExpand.setImageBitmap(image);
                        String searchText = ResourceManager.getInstance().getCurrentBundle().getString(IStringMap.RES_DEFAULT_MAP_TITLE, IStringMap.FAMILY_MAP);
                        mapExpand.setContentDescription(searchText);
                        Logger.log(Logger.INFO, "DashboardViewTouch", "=== MiniMap ===: updateMiniMap with image");
                    }
                    else
                    {
                        ((Activity) AndroidPersistentContext.getInstance().getContext()).setProgressBarVisibility(true);
                        mapProgressExpand.setVisibility(View.VISIBLE);
                        Logger.log(Logger.INFO, "DashboardViewTouch", "=== MiniMap ===: updateMiniMap with animation");
                    }
                }
            });
        }
    }

    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        DashboardManager.getInstance().mapResizeBegin();
        switch (model.getState())
        {
            case STATE_DASHBOARD:
            {
                if (notificationContainer != null && notificationContainer.isShown())
                {
                    showNotificationContainer(STATE_DASHBOARD);
                }
                break;
            }
        }
        Logger.log(Logger.INFO, "DashboardViewTouch", "=== dashboard view ===: onSizeChanged");
        final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();     
        if (tnComponent instanceof AbstractTnContainer)
        {   
            if (oldOrientation != orientation)
            {
                ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        CitizenScreen screen = (CitizenScreen) ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
                        
                        boolean isPortrait = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                        if(checkDashboardContainer(isPortrait))
                        {
                            if (isMockDropdownAnimationRunning)
                            {
                                islocationExpanded = !islocationExpanded;
                                isMockDropdownAnimationRunning = false;
                            }
                            boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
                            DashboardViewTouch.this.model.put(KEY_B_NEED_REFRESH_MINI_MAP, true);
                            updateDashboardScreenContainer(screen, isPortrait, isOnboard);
                            createAdjuggler(screen, STATE_DASHBOARD, isPortrait);
                        }
                        else
                        {
                            createDashboardScreenContainer(screen, isPortrait);
                        }
                    }
                   
                });
            }
            oldOrientation = orientation;
        }
    }
    
    protected void resizeMap()
    {
        TnUiArgAdapter width = new TnUiArgAdapter(0, new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                final boolean isPortrait = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                return PrimitiveTypeCache.valueOf(isPortrait ? miniMapWidth : miniMapWidthLandscape);
            }
        });
        
        TnUiArgAdapter height = new TnUiArgAdapter(0, new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                final boolean isPortrait = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                return PrimitiveTypeCache.valueOf(isPortrait ? miniMapHeight : miniMapHeightLandscape);
            }
        });
        
        TnUiArgAdapter y = new TnUiArgAdapter(0, new ITnUiArgsDecorator()
        {
            public Object decorate(TnUiArgAdapter args)
            {
                int screenHeight = AppConfigHelper.getDisplayHeight();
                final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
                final boolean isPortrait = orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
                return PrimitiveTypeCache.valueOf(isPortrait ? (screenHeight - miniMapHeight) / 2 : (screenHeight - miniMapHeightLandscape) / 2);
            }
        });
        
        MapContainer.getInstance().setMapRect(((DashboardUiDecorator) uiDecorator).SCOUT_VECTOR_MAP_X, y, width, height);
        DashboardManager.getInstance().mapResizeBegin();
    }
    
    protected void activate()
    {
        final int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();  
        if (oldOrientation != orientation)
        {
            resizeMap();
        }
        MapContainer.getInstance().enableGPSCoarse(false);
        super.activate();
    }
    
    private boolean checkDashboardContainer(boolean isPortrait)
    {
        if(isPortrait)
        {
            if(portraitView != null)
            {
                return true;
            }
        }
        else
        {
            if(landscapeView != null)
            {
                return true;
            }
        }
        return false;
    }

    protected boolean isDashboardState(int state)
    {
        return state == STATE_DASHBOARD;
    }

    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && this.isDashboardState(screen.getId()))
        {
            if(attached == ITnScreenAttachedListener.AFTER_ATTACHED)
            {
                TeleNavDelegate.getInstance().registerApplicationListener(this);
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, null);
                SplashScreenInflater.getInstance().release();
                
                executePoiDetailPreload();
                Thread t = new Thread(new Runnable()
                {
                    public void run()
                    {
                        /*try
                        {
                            Thread.sleep(800);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }*/
                        
                        if(DashboardViewTouch.this.model == null || !DashboardViewTouch.this.model.isActivated())
                        {
                            return;
                        }
                        
                        if (Logger.DEBUG)
                        {
                            Logger.log(Logger.INFO, this.getClass().getName(),
                                "OGLMapService-GetSnapshot onScreenUiEngineAttached ");
                        }
                        resizeMap();
                        DashboardManager.getInstance().initBackgroundMap();
                        
                        if(DashboardViewTouch.this.model == null || !DashboardViewTouch.this.model.isActivated())
                        {
                            return;
                        }

                        if (!DashboardViewTouch.this.model.getBool(KEY_B_IS_CREATE_DASHBOARD_MODULE))
                        {
                            boolean isHomeScreen = !DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
                            if(isHomeScreen)
                            {
                                DashboardViewTouch.this.handleViewEvent(CMD_CHECK_REGION_DETECT_STATUS);
                            }
                            else
                            {
                                DashboardViewTouch.this.handleViewEvent(CMD_CONTINUE);
                            }
                            DashboardViewTouch.this.model.put(KEY_B_IS_CREATE_DASHBOARD_MODULE, true);
                        }
                    
                        DashboardManager.getInstance().resume();
                    }
                });
                t.start();
                Logger.log(Logger.INFO, "DashboardViewTouch", "=== MiniMap ===: onScreenUiEngineAttached");
                CitizenScreen dashboardScreen = (CitizenScreen) this.getScreenByState(STATE_DASHBOARD);
                View view = (View)dashboardScreen.getContentContainer().getNativeUiComponent();
                if(isPortrait())
                {
                    View mapContainerExpand = (View) view.findViewById(R.id.dashboard0expand);
                    updateMiniMap(mapContainerExpand, true);
                    View adButton = view.findViewById(R.id.dashboard0adButton);
                    if (adButton != null)
                    {
                        LinearLayout webViewContainer = (LinearLayout)adButton;
                        createAdjuggler(webViewContainer, STATE_DASHBOARD);
                    }
                }
                else
                {
                    View mapContainerExpand = (View) view.findViewById(R.id.dashboard0location_landscape);
                    updateMiniMap(mapContainerExpand, false);
                    View adButton = view.findViewById(R.id.dashboard0adButton_landscape);
                    if (adButton != null)
                    {
                        LinearLayout webViewContainer = (LinearLayout)adButton;
                        createAdjuggler(webViewContainer, STATE_DASHBOARD);
                    }
                }
                
                AccountChangeListener.getInstance().addListener(this);
                MapDownloadOnBoardDataStatusManager.getInstance().addStatusChangeListener(this);
                MapDownloadStatusManager.getInstance().addDownloadStatusChangeListener(this);
            }
            else if(attached == DETTACHED)
            {
                // hide the mini dropdown
                if (islocationExpanded)
                {
                    islocationExpanded = false;
                }
                TeleNavDelegate.getInstance().unregisterApplicationListener(this);
                Notifier.getInstance().removeListener((INotifierListener) this.model.get(KEY_O_DROPDOWN_HINT_LISTENER));
                registeredDropdownNotifyListener = false;
                AccountChangeListener.getInstance().removeListener(this);
                MapDownloadOnBoardDataStatusManager.getInstance().removeStatusChangeListener(this);
                MapDownloadStatusManager.getInstance().removeDownloadStatusChangeListener(this);
            }
        }
    }
    
    protected void clearPoiDetailPreload()
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                WebViewCacheManager.getInstance().clearPoiDetailWebview();
                WebViewCacheManager.getInstance().setIsLoaded(false);
            }
        });
    }
    
    protected void executePoiDetailPreload()
    {
        if (!WebViewCacheManager.getInstance().isLoaded())
        {
            for(int i = 0; i < 3; i++)
            {
                CitizenWebComponent webComponent = WebViewCacheManager.getInstance().getPoiDetailWebview(i, i);
                webComponent.setDisableLongClick(true);
                webComponent.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler) model);
                webComponent.setCommandEventListener(this);
                
                BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.POI_DETAIL_DOMAIN_ALIAS);

                String url = sessionArgs.getUrl();
                url = BrowserSdkModel.addEncodeTnInfo(url, "");

                // per Zhangpan, we fix the height be the max
                url = BrowserSdkModel.appendWidthHeightToUrl(url);

                webComponent.enableTempCache(true);
                webComponent.setDataToTempCache("poikey", String.valueOf(0));
                webComponent.loadUrl(url);
            }
            
            WebViewCacheManager.getInstance().setIsLoaded(true);
        }
    }
    
    private boolean isPortrait()
    {
        return ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
    }
    
    public void setModelForView(AbstractModel model)
    {
        this.model = model;
    }

    public void appDeactivated(String[] params)
    {
        clearPoiDetailPreload();
    }

    public void appActivated(String[] params)
    {
        if(model.isActivated())
        {
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    AdJuggerManager.getInstance().reset();
                    
                    TnScreen screen = getScreenByState(STATE_DASHBOARD);
                    
                    if(screen instanceof CitizenScreen)
                    {
                        createAdjuggler((CitizenScreen)screen, STATE_DASHBOARD, isPortrait());
                    }
                    
                    executePoiDetailPreload();
                }
            });
        }
    }

    public void eglSizeChanged()
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "OGLMapService-GetSnapshot eglSizeChanged ");
        }
        resizeMap();
    }
    
    public void mapViewSizeChanged()
    {
        DashboardManager.getInstance().mapResizeEnd();
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        return super.handleUiEvent(tnUiEvent);
    }
    
    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
                case IDashboardConstants.CMD_COMMON_PREFERENCE:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_MENU_MYPROFILE_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_COMMON_PREFERENCE_ICON_ON_DASHBOARD:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_PROFILE_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_GOTO_FAVORITE:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_MY_PLACES_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_SHARE_LOCATION:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_SHARE_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_ADD_PLACE:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_SAVE_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_RESUME_TRIP:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_RESUME_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_CLOSE_RESUME:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_RESUME_CLEAR_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_COMMON_LINK_TO_MAP:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_MAP_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_GOTO_RECENT:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_RECENTS_CLICKED);
                    break;
                }
                case IDashboardConstants.CMD_GOTO_DWF:
                {
                    //Level1: button clicked when DWF session is inactive
                    //Level2: button clicked when DWF session is active
                    int level = isInDwfSession() ? 2:1;
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DASHBOARD,
                        KontagentLogger.DASHBOARD_DWF_CLICKED, level);
                    break;
                }
                case CMD_FOLD_LOCATION:
                {
                    model.put(KEY_B_MANUAL_OPRERATE_SHARE_ADD_PANEL, true);
                    break;
                }
                
            }
        }
    }

    private boolean isInDwfSession()
    {
        boolean isDwfActiveIconShow = false;
        int oldOrientation = ((AbstractTnUiHelper) AndroidUiHelper.getInstance()).getOrientation();
        boolean isPortrait =  oldOrientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
        if (isPortrait)
        { 
             View placeContainer = (View) portraitView.findViewById(R.id.dashboard0placeContainer);
             View dwfActiveIcon = placeContainer.findViewById(R.id.dashboard0DriveDWFActiveIcon);
             isDwfActiveIconShow = View.VISIBLE == dwfActiveIcon.getVisibility();
        }
        else
        { 
             View placeContainer = (View) landscapeView.findViewById(R.id.dashboard0placeContainer_landscape);
             View dwfActiveIcon = placeContainer.findViewById(R.id.dashboard0DriveDWFActiveIcon_landscape);
             isDwfActiveIconShow = View.VISIBLE == dwfActiveIcon.getVisibility();
         }
        return isDwfActiveIconShow;
    }
    
    protected void updateBadge()
    {
        final boolean isBadgeNeeded = MapDownloadStatusManager.getInstance().isBadgeNeeded();

        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (isBadgeNeeded)
                {
                    showBadge();
                }
                else
                {
                    hideBadge();
                }
            }
        });
    }
    
    protected View getBadgeItem()
    {
        View badge = null;
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        if (orientation == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            if (portraitView != null)
            {
                badge = portraitView.findViewById(R.id.dashboard0TitlePersonalBadge);
            }
        }
        else
        {
            if (landscapeView != null)
            {
                badge = landscapeView.findViewById(R.id.dashboard0TitlePersonalBadge_landscape);
            }
        }

        return badge;
    }
    
    protected void showBadge()
    {
        TextView badge = (TextView)getBadgeItem();

        if (badge != null)
        {
            badge.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            badge.setVisibility(View.VISIBLE);
        }
    }
    
    protected void hideBadge()
    {
        View badge = getBadgeItem();

        if (badge != null)
        {
            badge.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAccountTypeChanged()
    {
        updateBadge();
    }

    @Override
    public void onMapDownloadStatusChanged()
    {
        updateBadge();
    }

    @Override
    public void onLocalMapDataAvailabilityChanged(boolean isAvailable)
    {
        updateBadge();
    }
}
