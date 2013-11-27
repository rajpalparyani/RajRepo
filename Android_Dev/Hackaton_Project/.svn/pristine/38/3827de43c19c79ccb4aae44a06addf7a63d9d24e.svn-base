/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiResultViewTouch.java
 *
 */
package com.telenav.module.poi.result;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.i18n.ResourceBundle;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.PoiDataWrapper.PoiDataPublisher;
import com.telenav.module.poi.PoiSearchArgs;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.CommonBottom;
import com.telenav.ui.android.OnTnItemClickListener;
import com.telenav.ui.android.OnTnItemLongClickListener;
import com.telenav.ui.android.PlaceListItem;
import com.telenav.ui.android.TnListAdapter;
import com.telenav.ui.android.TnListView;
import com.telenav.ui.android.TnListView.ITnListViewListener;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenSlidableContainer;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogHyperLinkLabel;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author Albert Ma (byma@telenav.cn)
 * @date 2010-9-25
 */
class PoiResultViewTouch extends AbstractCommonView implements IPoiResultConstants, ITnTextChangeListener, ITnListViewListener,
        OnScrollListener
{
    protected static final int ID_MOST_POPULAR_CATEGORY = 2041;

    protected TnUiArgAdapter adsBannerWidthArgs;

    protected TnUiArgAdapter adsBannerHeightArgs;

    protected PoiResultHelper helper;

    protected int oldOrientation = 0;

    protected TnListView poiListView;

    protected PlaceListAdapter placeListAdapter;

    protected TextView[] sortViews;

    protected View placeList;

    private int lastVisibleStartIndex = -1;

    private int lastVisibleEndIndex = -1;

    private int impressionStartIndex = -1;

    private int impressionEndIndex = -1;

    private boolean isSortByContainerSizeInitialed = false;

    private boolean isSearchAlongSizeInitialed = false;

    private CitizenSlidableContainer feedbackContainer;

    public PoiResultViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected void activate()
    {
        // FIXME: adapter the font size of native XML configuration
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        super.activate();
    }

    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if(screen.getId() == STATE_POI_RESULT_LIST)
        {
            if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE,
                    new Object[]
                            { PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(true) });
            }
            else if (attached == DETTACHED)
            {
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, null);
            }
        }
    }

    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();
        switch (state)
        {
            case STATE_POI_RESULT_LIST:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int cmdId = tnUiEvent.getCommandEvent().getCommand();
                    switch (cmdId)
                    {
                        case CMD_SEARCH_NEAR_ME:
                        case CMD_SEARCH_NEAR_ROUTE:
                        {
                            this.model.put(KEY_I_CHANGETO_ALONGROUTE_TYPE, IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD);
                            break;
                        }
                        case CMD_SEARCH_NEAR_DEST:
                        {
                            this.model.put(KEY_I_CHANGETO_ALONGROUTE_TYPE, IPoiSearchProxy.TYPE_SEARCH_AROUND_DESTINATION);
                            break;
                        }
                        case CMD_GOTO_POI_MAP:
                        {
                            PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
                            poiDataWrapper.setOriginSelectedIndex(-1);
                            this.model.put(KEY_I_TYPE_MAP_FROM, TYPE_MAP_FROM_POI);
                            int addressSize = poiDataWrapper.getAddressSize();
                            if (addressSize > 0)
                            {
                                Address address = poiDataWrapper.getAddress(0);
                                if (addressSize > 1 && address.getPoi() != null
                                        && address.getPoi().getType() == Poi.TYPE_SPONSOR_POI)
                                {
                                    this.model.put(KEY_I_POI_SELECTED_INDEX, 1);
                                }
                                else
                                {
                                    this.model.put(KEY_I_POI_SELECTED_INDEX, 0);
                                }
                            }
                            break;
                        }
                        case CMD_MAP_IT:
                        {
                            this.model.put(KEY_I_TYPE_MAP_FROM, TYPE_MAP_FROM_SPECIFIC_POI);
                            break;
                        }
                        case CMD_SEARCH_BY_BEST_MATCH:
                        {
                            PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
                            int sortType = -1;
                            if (poiDataWrapper != null)
                            {
                                sortType = PoiResultHelper.getDefaultSortType(poiDataWrapper.getCategoryId());
                                if (sortType == IPoiSearchProxy.TYPE_SORT_BY_DISTANCE)
                                {
                                    sortType = IPoiSearchProxy.TYPE_SORT_BY_RELEVANCE;
                                }
                                this.model.put(KEY_I_CHANGETO_SORT_TYPE, sortType);
                            }
                            break;
                        }
                        case CMD_SEARCH_BY_DISTANCE:
                        {
                            this.model.put(KEY_I_CHANGETO_SORT_TYPE, IPoiSearchProxy.TYPE_SORT_BY_DISTANCE);
                            break;
                        }
                    }
                }
                break;
            }

        }
        return super.preProcessUIEvent(tnUiEvent);
    }

    protected void popAllViews()
    {
        super.popAllViews();
        // don't forget to close the feedbackContainer if there are any
        closeFeedbackPopup();

        if (helper != null)
        {
            helper.removeNotifierListener();
            helper = null;
        }
    }

    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        switch (model.getState())
        {
            case STATE_POI_RESULT_LIST:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && tnUiEvent.getCommandEvent() != null
                        && tnUiEvent.getCommandEvent().getCommand() == CMD_SHOW_FEEDBACK)
                {
                    showFeedbackPopup();
                    return true;
                }
                else if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && tnUiEvent.getCommandEvent() != null
                        && (tnUiEvent.getCommandEvent().getCommand() == CMD_HIDE_FEEDBACK))
                {
                    closeFeedbackPopup();
                    return true;
                }
                break;
            }
        }

        return super.handleUiEvent(tnUiEvent);
    }

    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
                case CMD_GOTO_POI_MAP:
                {
                    //Level 1: When display is switched from list view to map view
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                        KontagentLogger.SEARCHRESULTS_DISPLAY_CHANGED, 1);
                    break;
                }
                case CMD_SEARCH_BY_BEST_MATCH:
                {
                    //Level 2: when user switches to best match sorting from distance
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                        KontagentLogger.SEARCHRESULTS_SORTING_CLICKED, 2);
                    break;
                }
                case CMD_SEARCH_BY_DISTANCE:
                {
                    //Level 1: when user switches to distance sorting from best match 
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                          KontagentLogger.SEARCHRESULTS_SORTING_CLICKED, 1);
                      break;
                }
                case CMD_HIT_BOTTOM:
                {    
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                          KontagentLogger.SEARCHRESULTS_SCROLLED_TO_BOTTOM);
                    break;
                }
                case CMD_POI_RESULT_SELECT:
                {
                    int itemIndex = model.getInt(KEY_I_POI_SELECTED_INDEX);
                    PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                    if(poiDataWrapper != null && itemIndex >= 0 && itemIndex < poiDataWrapper.getAddressSize())
                    {
                        Address addr = poiDataWrapper.getAddress(itemIndex);
                        if(addr != null && addr.getPoi() != null)
                        {
                            Poi poi = addr.getPoi();
                            if(poi.getType() == Poi.TYPE_SPONSOR_POI)
                            {
                                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                                  KontagentLogger.SEARCHRESULTS_POI_SPONSORED_CLICKED);
                            }
                            else if(poi.isAdsPoi())
                            {
                                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                                    KontagentLogger.SEARCHRESULTS_POI_ORGANICAD_CLICKED);
                            }
                            else
                            {
                                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                                    KontagentLogger.SEARCHRESULTS_POI_ORGANIC_CLICKED);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    
    private synchronized void closeFeedbackPopup()
    {
        if (feedbackContainer != null)
        {
            feedbackContainer.hideImmediately();
            feedbackContainer = null;
        }
    }

    private synchronized void showFeedbackPopup()
    {
        if (!model.isActivated() || feedbackContainer != null)
        {
            return;
        }
        AbstractTnComponent feedback = createFeedbackPopup();
        feedbackContainer = UiFactory.getInstance().createSlidableContainer(IPoiResultConstants.POI_RESULT_FEEDBACK_CONTAINER);
        feedbackContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        feedbackContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((PoiResultUiDecorator) uiDecorator).FEEDBACK_CONTAINER_HEIGHT);
        feedbackContainer.setAnimationDuration(250, 250);
        feedbackContainer.setContent(feedback);
        int x = 0;
        int y = 0;
        int h = ((PoiResultUiDecorator) uiDecorator).FEEDBACK_CONTAINER_HEIGHT.getInt();
        View sortByContainer = placeList.findViewById(R.id.placeList0SortByContainer);
        CommonBottom commonBottomContainer = (CommonBottom) placeList.findViewById(R.id.commonBottomContainer); 
        if (sortByContainer.getVisibility() == View.GONE)
            y = ((PoiResultUiDecorator) uiDecorator).FEEDBACK_CONTAINER_POS_Y.getInt();
        else
            y = ((PoiResultUiDecorator) uiDecorator).FEEDBACK_CONTAINER_POS_Y.getInt()-commonBottomContainer.getRealHeight();       
        int w = AppConfigHelper.getDisplayWidth();
        feedbackContainer.showAt(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen().getRootContainer(),
            x, y, w, h, false);
        feedbackContainer.setSizeChangeListener(this);
        feedbackContainer.setShown(true);
    }

    private AbstractTnComponent createFeedbackPopup()
    {
        TnLinearContainer innerContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        innerContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.POI_RESULT_FEEDBACK_BACKGROUND_COLOR));
        innerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        innerContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((PoiResultUiDecorator) uiDecorator).FEEDBACK_CONTAINER_HEIGHT);
        String text = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPoi.LABEL_FEEDBACK_TEXT, IStringPoi.FAMILY_POI);
        String linkText = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPoi.LABEL_FEEDBACK_TEXT_LINK, IStringPoi.FAMILY_POI);
        FrogLabel textLabel = UiFactory.getInstance().createLabel(0, text);
        textLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        FrogHyperLinkLabel linkLabel = new FrogHyperLinkLabel(IPoiResultConstants.POI_RESULT_FEEDBACK_LABEL, linkText);
        linkLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_FEEDBACK_LINK),
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_FEEDBACK_LINK));
        linkLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LABEL));
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_FEEDBACK);
        linkLabel.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        linkLabel.setCommandEventListener(this);
        innerContainer.add(textLabel);
        innerContainer.add(linkLabel);
        return innerContainer;
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int uiEventType = tnUiEvent.getType();
        switch (uiEventType)
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                if ((tnUiEvent.getComponent() instanceof FrogButton || tnUiEvent.getComponent() instanceof FrogLabel)
                        && tnUiEvent.getComponent().getId() == SORT_BY_COMOBOX_COMPONENT_ID
                        && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_UP)
                {
                    return CMD_GOTO_CHANGE_SORT;
                }
                if (tnUiEvent.getComponent() instanceof FrogTextField
                        && tnUiEvent.getMotionEvent().getAction() == TnMotionEvent.ACTION_UP)
                {
                    this.model.put(KEY_B_IS_SEARCHING_DIRECTLY, false);
                    return CMD_COMMON_GOTO_ONEBOX;
                }
                break;
            }
            case TnUiEvent.TYPE_KEY_EVENT:
            {
                if ((tnUiEvent.getComponent() instanceof FrogButton || tnUiEvent.getComponent() instanceof FrogLabel)
                        && tnUiEvent.getComponent().getId() == SORT_BY_COMOBOX_COMPONENT_ID
                        && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                        && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_PAD_CENTER)
                {
                    return CMD_GOTO_CHANGE_SORT;
                }
                break;
            }
        }
        return CMD_NONE;
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch (state)
        {
            case STATE_POI_REDO_SEARCHING:
            {
                popup = createSearchingProgressBar();
                break;
            }
            case STATE_NO_GPS_WARNING:
            {
                popup = createNoGPSWarning();
                break;
            }
            default:
                break;
        }
        return popup;
    }

    private TnPopupContainer createSearchingProgressBar()
    {
        String search = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_SEARCHING, IStringCommon.FAMILY_COMMON);
        String name = model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);
        if (name == null)
        {
            name = model.getString(KEY_S_COMMON_SEARCH_TEXT);
        }

        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        search = converter.convert(search, new String[]
        { name });

        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, search);
        return progressBox;
    }

    private TnPopupContainer createNoGPSWarning()
    {
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        String noGps = bundle.getString(IStringCommon.RES_GPS_ERROR, IStringCommon.FAMILY_COMMON);

        TnMenu menu = UiFactory.getInstance().createMenu();

        String cancel = bundle.getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON);
        menu.add(cancel, CMD_COMMON_BACK);

        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(STATE_NO_GPS_WARNING, noGps, menu);
        return messageBox;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch (state)
        {
            case STATE_POI_RESULT_LIST:
            {
                screen = createPoiResultListScreen();
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_SEARCH_RESULTS,
                    KontagentLogger.SEARCHRESULTS_PAGE_DISPLAYED);
                break;
            }
            case STATE_POI_SEARCH_FEEDBACK:
            {
                return createFeedbackPoiSearch(state);
            }

        }
        return screen;
    }

    private TnScreen createFeedbackPoiSearch(int state)
    {
        closeFeedbackPopup();
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_POI_SEARCH_FEEDBACK);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        AbstractTnContainer contentContainer = screen.getContentContainer();
        CitizenWebComponent poiSearchFeedbackWebView = UiFactory.getInstance().createCitizenWebComponent(this, 0);

        BrowserSessionArgs sessionArgs = new BrowserSessionArgs(CommManager.POI_LIST_FEEDBACK_DOMAIN_ALIAS);
        String url = sessionArgs.getUrl();

        poiSearchFeedbackWebView.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler) model);
        url = BrowserSdkModel.addEncodeTnInfo(url, this.getRegion());

        url = BrowserSdkModel.appendWidthHeightToUrl(url);

        poiSearchFeedbackWebView.loadUrl(url);
        poiSearchFeedbackWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        poiSearchFeedbackWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.uiDecorator.SCREEN_HEIGHT);

        contentContainer.add(poiSearchFeedbackWebView);

        return screen;
    }

    private boolean isNeedNearMe()
    {
        boolean isChoosingLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
        int navRouteType = NavRunningStatusProvider.getInstance().getNavType();
        boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
        // Fix bug http://jira.telenav.com:8080/browse/TNANDROID-2635
        boolean needNearMeButton = (navRouteType == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE)
                && this.model.getInt(KEY_I_SEARCH_TYPE) == IPoiSearchProxy.TYPE_SEARCH_ADDRESS && !isSearchNearBy
                && !isChoosingLocation;

        return needNearMeButton;
    }

    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        switch (model.getState())
        {
            case STATE_POI_RESULT_LIST:
            case STATE_POI_REDO_SEARCHING:
            case STATE_NO_GPS_WARNING:
            case STATE_POI_SORT_BY:
            case STATE_COMMON_ERROR:
            {
                if (feedbackContainer != null && feedbackContainer.isShown())
                {
                    closeFeedbackPopup();
                    showFeedbackPopup();
                }
                break;
            }
        }

        // FIXME
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        super.onSizeChanged(tnComponent, w, h, oldw, oldh);
    }

    protected TnScreen createPoiResultListScreen()
    {
        oldOrientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();

        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_POI_RESULT_LIST);
        screen.getRootContainer().setSizeChangeListener(this);
        AbstractTnContainer contentContainer = new TnLinearContainer(ID_POI_RESULT_CONTENT_CONTAINER, true);

        placeList = AndroidCitizenUiHelper.addContentView(screen, R.layout.place_list);

        View mapView = placeList.findViewById(R.id.commonTitle0IconButton);
        mapView.setVisibility(View.VISIBLE);

        TextView searchAlongUpAhead = (TextView) placeList.findViewById(R.id.placeList0SearchAlongUpAheadView);
        TextView searchAlongUpDestination = (TextView) placeList.findViewById(R.id.placeList0SearchAlongUpDestinationView);
        int navRouteType = NavRunningStatusProvider.getInstance().getNavType();
        if (navRouteType == NavRunningStatusProvider.NAV_TYPE_STATIC_ROUTE)
        {
            searchAlongUpAhead.setText(ResourceManager.getInstance().getText(IStringPoi.LABEL_SEARCH_NEARME,
                IStringPoi.FAMILY_POI));
        }
        else
        {
            searchAlongUpAhead.setText(ResourceManager.getInstance().getText(IStringPoi.LABEL_SEARCH_UPPERHEAD,
                IStringPoi.FAMILY_POI));
        }
        searchAlongUpDestination.setText(ResourceManager.getInstance().getText(IStringPoi.LABEL_SEARCH_AROUND_DESTIATION,
            IStringPoi.FAMILY_POI));

        checkSearchNearbyAndAlongRoute(placeList);

        poiListView = (TnListView) placeList.findViewById(R.id.placeList0ListView);
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
        updateTitle(poiDataWrapper);
        placeListAdapter = new PlaceListAdapter(poiListView.getContext(), poiDataWrapper.getPublisher());
        poiListView.setAdapter(placeListAdapter);
        poiListView.setPullRefreshEnable(false);
        poiListView.setHitBottomLoadEnable(poiDataWrapper.isHasMorePoi());
        poiListView.setTnListViewListener(this);
        poiListView.setOnScrollListener(this);
        poiListView.setOnTnItemClickListener(new OnTnItemClickListener()
        {

            protected void onTnItemClick(AdapterView<?> parent, View view, int itemIndex, long id)
            {
                model.put(KEY_I_POI_SELECTED_INDEX, itemIndex);
                AndroidCitizenUiHelper.triggerCommand(PoiResultViewTouch.this, view, CMD_POI_RESULT_SELECT);
            }
        });
        poiListView.setOnTnItemLongClickListener(new OnTnItemLongClickListener()
        {

            @Override
            protected boolean onTnItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                PoiDataWrapper dataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                dataWrapper.setOriginSelectedIndex(position);
                model.put(KEY_I_POI_SELECTED_INDEX, position);
                return view.showContextMenu();
            }
        });

        // Change the padding of list footer to avoid be covered by feedback container
        View listFooter = poiListView.findViewById(R.id.tnListFooter0Content);
        listFooter.setPadding(listFooter.getPaddingLeft(), listFooter.getPaddingTop(), listFooter.getPaddingRight(),
            listFooter.getPaddingBottom() + ((PoiResultUiDecorator) uiDecorator).FEEDBACK_CONTAINER_HEIGHT.getInt());

        String[] sortTypes =
        {
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_BEST_MATCHES, IStringPoi.FAMILY_POI),
                ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_SORT_BY_DISTANCE, IStringPoi.FAMILY_POI) };

        sortViews = new TextView[]
        { (TextView) placeList.findViewById(R.id.placeList0SortByBestMatchView),
                (TextView) placeList.findViewById(R.id.placeList0SortByDistanceView) };

        if (sortViews != null && sortViews.length > 0)
        {
            for (int i = 0; i < sortViews.length; i++)
            {
                sortViews[i].setText(sortTypes[i]);
            }
        }

        checkSortByContainer(placeList);

        AndroidCitizenUiHelper.setOnClickCommand(this, placeList, UI_COMMAND_TABLE);

        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.SCREEN_HEIGHT);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            }));
        contentContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(1), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(0);
                }
            }));

        screen.getRootContainer().add(contentContainer);

        MapContainer mapContainer = UiFactory.getInstance()
                .getCleanMapContainer(null, ICommonConstants.ID_MAP_CONTAINER, false);
        if (mapContainer.getParent() != null)
        {
            AbstractTnContainer mapParent = (AbstractTnContainer) mapContainer.getParent();
            mapParent.remove(mapContainer);
        }
        screen.setBackgroundComponent(mapContainer);
        return screen;
    }

    private void updateTitle(PoiDataWrapper poiDataWrapper)
    {

        boolean isNotOneBoxSearch = (poiDataWrapper != null && (poiDataWrapper.getCategoryId() == PoiDataRequester.TYPE_NO_CATEGORY_ID || poiDataWrapper
                .getCategoryId() > 0));
        boolean isOnboard = !NetworkStatusManager.getInstance().isConnected();
        ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
        TextView titleView = (TextView) placeList.findViewById(R.id.commonTitle0TextView);
        TextView searchBoxHint = (TextView) placeList.findViewById(R.id.commonOneboxTextView);
        ImageView DSRimage = (ImageView) placeList.findViewById(R.id.commonOneboxDsrButton);
        View oneBoxDSR = (View) placeList.findViewById(R.id.commonOneboxDsrButton);
        this.model.put(KEY_B_IS_ONBOARD, isOnboard);
        int DSRImageId = isOnboard ? R.drawable.inputbox_mic_icon_disabled : R.drawable.inputbox_mic_icon;
        DSRimage.setImageResource(DSRImageId);
        String initString = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPoi.LABEL_SEARCH, IStringPoi.FAMILY_POI);
        String SearchString = this.model.getString(KEY_S_COMMON_SHOW_SEARCH_TEXT);

        if (!isNotOneBoxSearch)
        {
            this.model.put(KEY_S_INPUT_TEXT, SearchString);
            if (isOnboard)
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
            }
            else
            {
                searchBoxHint.setText(SearchString);
            }
            
            titleView.setText(initString);
        }
        else
        {
            this.model.put(KEY_S_INPUT_TEXT, initString);
            if (isOnboard)
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT_OFFLINE, IStringCommon.FAMILY_COMMON));
            }
            else
            {
                searchBoxHint.setText(bundle.getString(IStringCommon.RES_INPUT_HINT, IStringCommon.FAMILY_COMMON));
            }
            if (poiDataWrapper.getShowText() == null ||poiDataWrapper.getShowText().trim().length()==0)
            {
                titleView.setText(initString);
            }
            else
            {
                titleView.setText(poiDataWrapper.getShowText());
            }
        }
        
        boolean isDsr = poiDataWrapper != null
                && (poiDataWrapper.getSearchFromType() == IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN || poiDataWrapper
                        .getSearchFromType() == IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN_ALONG);
        if (isDsr)
        {
            titleView.setText(initString);
            searchBoxHint.setText(poiDataWrapper.getShowText());
        }
    }

    private void checkSortByContainer(View mainView)
    {
        PoiDataWrapper poiDataWrapper = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
        int categoryId = this.model.getInt(KEY_I_CATEGORY_ID);
        View sortByContainer = mainView.findViewById(R.id.placeList0SortByContainer);
        int selectedIndex = 0;
        int sortByType = this.model.getInt(KEY_I_SEARCH_SORT_TYPE);
        switch (sortByType)
        {
            case IPoiSearchProxy.TYPE_SORT_BY_RELEVANCE:
            case IPoiSearchProxy.TYPE_SORT_BY_RATING:
            case IPoiSearchProxy.TYPE_SORT_BY_PRICE:
            {
                selectedIndex = 0;
                break;
            }
            case IPoiSearchProxy.TYPE_SORT_BY_DISTANCE:
            {
                selectedIndex = 1;
                break;
            }
            default:
                break;
        }
        if (sortViews != null && sortViews.length > 0)
        {
            for (int i = 0; i < sortViews.length; i++)
            {
                sortViews[i].setSelected(i == selectedIndex);
            }
        }
        // it doesn't show the sort by component for onebox , dsr, and search along route
        if (isSortByContainerNeeded(poiDataWrapper))
        {
            sortByContainer.setVisibility(View.VISIBLE);
            if (poiDataWrapper.getCategoryId() != SEARCH_BY_PRICE_ANY
                    && poiDataWrapper.getCategoryId() != SEARCH_BY_PRICE_DIESEL
                    && poiDataWrapper.getCategoryId() != SEARCH_BY_PRICE_PLUS
                    && poiDataWrapper.getCategoryId() != SEARCH_BY_PRICE_PREMIUM
                    && poiDataWrapper.getCategoryId() != SEARCH_BY_PRICE_REGULAR)
            {
                sortViews[0].setText(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_BEST_MATCHES, IStringPoi.FAMILY_POI));
            }
            else
            {
                sortViews[0].setText(ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_SORT_BY_PRICE, IStringPoi.FAMILY_POI));
            }
        }
        else
        {
            sortByContainer.setVisibility(View.GONE);
        }

        // Adapter the dp to meet different screen size, especially for small screen
        if (!isSortByContainerSizeInitialed)
        {
            if (sortViews != null && sortViews.length > 0)
            {
                for (int i = 0; i < sortViews.length; i++)
                {
                    sortViews[i].setPadding(sortViews[i].getPaddingLeft(),
                        AndroidCitizenUiHelper.getPixelsByDensity(sortViews[i].getPaddingTop()),
                        sortViews[i].getPaddingRight(),
                        AndroidCitizenUiHelper.getPixelsByDensity(sortViews[i].getPaddingBottom()));
                }
            }
            isSortByContainerSizeInitialed = true;
        }
    }
    
    private boolean isSortByContainerNeeded(PoiDataWrapper poiDataWrapper)
    {
        return poiDataWrapper != null
                && (poiDataWrapper.getCategoryId() == PoiDataRequester.TYPE_NO_CATEGORY_ID || (poiDataWrapper.getCategoryId() > 0 && PoiResultHelper
                        .getDefaultSortType(poiDataWrapper.getCategoryId()) != IPoiSearchProxy.TYPE_SORT_BY_DISTANCE))
                // && !NavRunningStatusProvider.getInstance().isNavRunning()
                && poiDataWrapper.getTotalCountServerReturned() > 1
                && poiDataWrapper.getSearchFromType() != IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN
                && poiDataWrapper.getSearchFromType() != IPoiSearchProxy.TYPE_SEARCH_FROM_SPEAKIN_ALONG;
    }

    private void checkSearchNearbyAndAlongRoute(View mainView)
    {
        boolean isChoosingLocation = this.model.getBool(KEY_B_IS_CHOOSING_LOCATION);
        boolean isSearchNearBy = model.getBool(KEY_B_IS_SEARCH_NEAR_BY);
        boolean needLocationButton = !isSearchNearBy && !isChoosingLocation
                && this.model.getInt(KEY_I_SEARCH_TYPE) != IPoiSearchProxy.TYPE_SEARCH_ALONG_ROUTE;
        PoiDataWrapper poiData = (PoiDataWrapper) this.model.get(KEY_O_POI_DATA_WRAPPER);
        boolean isNotOneBoxSearch = (poiData != null && (poiData.getCategoryId() == PoiDataRequester.TYPE_NO_CATEGORY_ID || poiData
                .getCategoryId() > 0));
        // Fix bug http://jira.telenav.com:8080/browse/TNANDROID-2635
        View searchAlongContainer = mainView.findViewById(R.id.placeList0SearchAlongContainer);
        CommonBottom placeList0CommonContainer = (CommonBottom)mainView.findViewById(R.id.placeList0CommonContainer);
        
        TextView searchNearLocation = (TextView) mainView.findViewById(R.id.placeList0SearchNearLocation);
        boolean dwfChangeAnchor = isChoosingLocation && this.model.get(KEY_O_ADDRESS_ORI)!=null;
        if (needLocationButton && !isNeedNearMe() || dwfChangeAnchor)
        {
            Stop anchorStop = poiData.getAnchorStop();
            if (poiData.getSearchType() != IOneBoxSearchProxy.TYPE_SEARCH_CITY && anchorStop != null)
            {
                String locationStr = this.getAddressText(anchorStop);
                if (anchorStop.getType() != Stop.STOP_CURRENT_LOCATION && anchorStop.getType() != Stop.STOP_ANCHOR
                        && locationStr != null && locationStr.trim().length() != 0)
                {
                    locationStr = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringPoi.LABEL_SEARCH_ALONG_ADDRESS, IStringPoi.FAMILY_POI)
                            + " " + locationStr;
                    searchNearLocation.setText(locationStr);
                    searchNearLocation.setVisibility(View.VISIBLE);
                }
                else
                {
                    searchNearLocation.setVisibility(View.GONE);
                }
            }
            else
            {
                searchNearLocation.setVisibility(View.GONE);
            }
        }
        else if (((!isChoosingLocation && !isSearchNearBy) || isNeedNearMe()) && isNotOneBoxSearch)
        {
            searchAlongContainer.setVisibility(View.VISIBLE);
            
            placeList0CommonContainer.setHeight((int) placeList0CommonContainer.getResources().getDimension(
                R.dimen.commonBottom0ContainerPortraitLayoutHeightWithoutMargin), (int) placeList0CommonContainer.getResources().getDimension(
                    R.dimen.commonBottom0ContainerlandscapeLayoutHeightWithoutMargin));
            
            TextView searchAlongUpAhead = (TextView) placeList.findViewById(R.id.placeList0SearchAlongUpAheadView);
            TextView searchAlongUpDestination = (TextView) placeList.findViewById(R.id.placeList0SearchAlongUpDestinationView);
            if (this.model.getInt(KEY_I_SEARCH_ALONG_TYPE) == IPoiSearchProxy.TYPE_SEARCH_ALONG_UPHEAD)
            {
                searchAlongUpAhead.setSelected(true);
                searchAlongUpDestination.setSelected(false);
            }
            else if (this.model.getInt(KEY_I_SEARCH_ALONG_TYPE) == IPoiSearchProxy.TYPE_SEARCH_AROUND_DESTINATION)
            {
                searchAlongUpAhead.setSelected(false);
                searchAlongUpDestination.setSelected(true);
            }
            else
            {
                searchAlongUpAhead.setSelected(true);
                searchAlongUpDestination.setSelected(false);
            }
        }
        else
        {
            searchAlongContainer.setVisibility(View.GONE);
            searchNearLocation.setVisibility(View.GONE);
        }

        // Adapter the dp to meet different screen size, especially for small screen
        if (!isSearchAlongSizeInitialed)
        {
            searchNearLocation.setPadding(searchNearLocation.getPaddingLeft(),
                AndroidCitizenUiHelper.getPixelsByDensity(searchNearLocation.getPaddingTop()),
                searchNearLocation.getPaddingRight(),
                AndroidCitizenUiHelper.getPixelsByDensity(searchNearLocation.getPaddingBottom()));

            TextView searchAlongUpAhead = (TextView) placeList.findViewById(R.id.placeList0SearchAlongUpAheadView);
            TextView searchAlongUpDestination = (TextView) placeList.findViewById(R.id.placeList0SearchAlongUpDestinationView);

            searchAlongUpAhead.setPadding(searchAlongUpAhead.getPaddingLeft(),
                AndroidCitizenUiHelper.getPixelsByDensity(searchAlongUpAhead.getPaddingTop()),
                searchAlongUpAhead.getPaddingRight(),
                AndroidCitizenUiHelper.getPixelsByDensity(searchAlongUpAhead.getPaddingBottom()));

            searchAlongUpDestination.setPadding(searchAlongUpDestination.getPaddingLeft(),
                AndroidCitizenUiHelper.getPixelsByDensity(searchAlongUpDestination.getPaddingTop()),
                searchAlongUpDestination.getPaddingRight(),
                AndroidCitizenUiHelper.getPixelsByDensity(searchAlongUpDestination.getPaddingBottom()));

            isSearchAlongSizeInitialed = true;
        }
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_POI_RESULT_LIST:
            {
                PoiDataWrapper poiDataManager = (PoiDataWrapper) model.get(KEY_O_POI_DATA_WRAPPER);
                updateTitle(poiDataManager);
                if (!poiDataManager.isDoingRequest())
                {
                    if (poiListView != null)
                    {
                        placeListAdapter.setPoiDataWrapper(poiDataManager.getPublisher());
                        if (model.fetchBool(KEY_B_NO_NEED_UPDATE_SCROLL))
                        {
                            closeFeedbackPopup();
                            poiListView.setAdapter(placeListAdapter);
                        }
                        placeListAdapter.notifyDataSetChanged();
                        stopLoad();
                        if (model.getBool(KEY_B_FORBID_LIST_HITBOTTOM))
                        {
                            poiListView.setHitBottomLoadEnable(false);
                            poiListView.setPullLoadEnable(true);
                        }
                        else
                        {
                            poiListView.setHitBottomLoadEnable(poiDataManager.isHasMorePoi());
                        }
                        poiListView.requestLayout();
                    }
                }
                if (placeList != null)
                {
                    checkSearchNearbyAndAlongRoute(placeList);
                    checkSortByContainer(placeList);
                }

                return true;
            }
            default:
                break;

        }
        return false;
    }

    private void stopLoad()
    {
        poiListView.stopRefresh();
        poiListView.stopLoadMore();
        poiListView.setRefreshTime(new Date());
    }

    protected String getAddressText(Stop stop)
    {
        return ResourceManager.getInstance().getStringConverter().convertAddress(stop, false);
    }

    public void onTextChange(AbstractTnComponent component, String text)
    {
        if (this.model.getState() == STATE_POI_RESULT_LIST && component instanceof FrogTextField)
        {
            TnScreen tnScreen = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen();
            AbstractTnComponent button = tnScreen.getComponentById(ID_SEARCH_BUTTON_COMP);
            if (button instanceof FrogButton)
            {
                if (text != null && text.trim().length() != 0)
                {
                    ((FrogButton) button).setEnabled(true);
                    ((FrogButton) button).setIcon(ImageDecorator.IMG_AC_FIND_UNFOCUSED.getImage(),
                        ImageDecorator.IMG_AC_FIND_UNFOCUSED.getImage(), AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
                }
                else
                {
                    ((FrogButton) button).setIcon(ImageDecorator.IMG_AC_FIND_FOCUSED.getImage(),
                        ImageDecorator.IMG_AC_FIND_FOCUSED.getImage(), AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
                    ((FrogButton) button).setEnabled(false);
                }
            }
            if (text != null && text.trim().length() > 0)
            {
                FrogTextField frogTextField = (FrogTextField) component;
                AbstractTnComponent textFieldComponent = frogTextField.getParent();
                boolean isTriggerBySetText = false;
                if (textFieldComponent instanceof CitizenTextField)
                {
                    CitizenTextField textField = (CitizenTextField) textFieldComponent;
                    isTriggerBySetText = textField.isTriggerBySetText();
                }

                if (!isTriggerBySetText)
                {
                    TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, component);
                    TnCommandEvent commandEvent = new TnCommandEvent(CMD_COMMON_GOTO_ONEBOX);
                    uiEvent.setCommandEvent(commandEvent);
                    handleUiEvent(uiEvent);
                    frogTextField.setText("");
                }

            }
        }
    }

    class PlaceListAdapter extends TnListAdapter
    {
        private static final int SPONSOR_VIEW = 0;

        private static final int NORMAL_VIEW = 1;

        private int[] itemViewTypes = new int[]
        { SPONSOR_VIEW, NORMAL_VIEW };

        private PoiDataPublisher poiDataPublisher;

        public PlaceListAdapter(Context context, PoiDataPublisher poiDataPublisher)
        {
            super(context);
            this.poiDataPublisher = poiDataPublisher;
        }

        public void setPoiDataWrapper(PoiDataPublisher poiDataPublisher)
        {
            this.poiDataPublisher = poiDataPublisher;
        }

        public int getCount()
        {

            return getSize();
        }

        private int getSize()
        {
            int size = 0;
            if (poiDataPublisher != null)
            {
                size = poiDataPublisher.getAddressSize();
            }

            return size;
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public int getItemViewType(int position)
        {
            Address addr = null;
            addr = poiDataPublisher.getAddress(position);

            if (addr != null && addr.getPoi() != null)
            {
                return addr.getPoi().getType() == Poi.TYPE_SPONSOR_POI ? itemViewTypes[0] : itemViewTypes[1];
            }

            return super.getItemViewType(position);
        }

        public int getViewTypeCount()
        {
            return itemViewTypes.length;
        }

        protected View getItemView(int position, View convertView, ViewGroup parent)
        {
            Address addr = null;
            Poi poi = null;
            Stop stop = null;
            int poiType = Poi.TYPE_POI;

            addr = poiDataPublisher.getAddress(position);
            if (addr != null)
            {
                poi = addr.getPoi();
                stop = addr.getStop();

                poiType = poi.getType();
            }

            PlaceListItem item = null;

            if (convertView == null)
            {
                convertView = mInflater.inflate(poiType == Poi.TYPE_SPONSOR_POI ? R.layout.place_list0adsitem
                        : R.layout.place_list0normalitem, null);
                item = (PlaceListItem) convertView;
                item.init(poiType == Poi.TYPE_SPONSOR_POI ? PlaceListItem.TYPE_SPONSOR : PlaceListItem.TYPE_NORMAL);
            }
            else
            {
                item = (PlaceListItem) convertView;
            }
            if (addr == null)
            {
                convertView.setVisibility(View.GONE);
                return convertView;
            }
            if (poiType != Poi.TYPE_SPONSOR_POI)
            {
                item.setIndicator(poiDataPublisher.getIndexOfNormalList(addr) + 1);
            }
            String shortMessage = null;
            if (poi.getAd() != null)
            {
                shortMessage = poi.getAd().getAdLine();
                item.setIsShowAds(poiType == Poi.TYPE_SPONSOR_POI);
            }

            item.setDescription(shortMessage);

            item.setIsShowCouponImage(poi.isHasCoupon());

            item.setIsShowMenuImage(poi.hasPoiMenu() || poi.hasAdsMenu());

            TnMenu longClickMenu = UiFactory.getInstance().createMenu();

            if (stop != null && addr.isValid())
            {
                String addrText = getAddressText(stop);
                if (addrText != null && addrText.trim().length() > 0)
                {
                    item.setAddress(addrText);

                }
                String driveTo = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_SEARCH_DRIVE_TO, IStringPoi.FAMILY_POI);
                longClickMenu.add(driveTo, CMD_DRIVE_TO);
            }
            else
            {
                item.setAddress("");
            }

            boolean isSeachAlongDest = poiDataPublisher.getSearchType() == PoiSearchArgs.TYPE_SEARCH_ALONG_ROUTE
                    && poiDataPublisher.getSearchAlongType() == PoiSearchArgs.TYPE_SEARCH_AROUND_DESTINATION;

            String distStr = UiFactory.getInstance().getDisplayDistance(addr,
                isSeachAlongDest ? poiDataPublisher.getCurrDestStop() : poiDataPublisher.getAnchorStop(), true);
            item.setDistance(distStr);
            if (poi.getBizPoi() != null)
            {

                if (poi.getBizPoi().getCategoryName() != null && poi.getBizPoi().getCategoryName().trim().length() > 0)
                {
                    item.setLastLine(poi.getBizPoi().getCategoryName());
                }

                int categoryId = poiDataPublisher.getCategoryId();
                if (categoryId == SEARCH_BY_PRICE_ANY || categoryId == SEARCH_BY_PRICE_PLUS
                        || categoryId == SEARCH_BY_PRICE_PREMIUM || categoryId == SEARCH_BY_PRICE_DIESEL
                        || categoryId == SEARCH_BY_PRICE_REGULAR)
                {
                    item.setLastLine(poi.getBizPoi().getPrice());
                }

                if (poi.getBizPoi().getPhoneNumber() != null && poi.getBizPoi().getPhoneNumber().trim().length() > 0)
                {
                    String call = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringPoi.LABEL_SEARCH_CALL, IStringPoi.FAMILY_POI);
                    StringConverter converter = ResourceManager.getInstance().getStringConverter();
                    String callPhoneNo = converter.convert(call, new String[]
                    { poi.getBizPoi().getPhoneNumber() });
                    longClickMenu.add(callPhoneNo, CMD_CALL);
                }
                String title = poi.getBizPoi().getBrand();
                longClickMenu.setHeaderTitle(poi.getBizPoi().getBrand());
                item.setBrandName(title);
            }

            if (stop != null && addr.isValid())
            {
                String mapIt = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_SEARCH_MAP, IStringPoi.FAMILY_POI);
                longClickMenu.add(mapIt, CMD_MAP_IT);

                String saveToFav = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_ADD_TO_FAVORITE, IStringCommon.FAMILY_COMMON);
                longClickMenu.add(saveToFav, CMD_SAVE_TO_FAV);
            }

            if (poi.getStop() != null && addr.isValid())
            {
                String shareIt = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_SHARE_IT, IStringPoi.FAMILY_POI);
                longClickMenu.add(shareIt, CMD_SHARE_IT);
            }
            if (model.getInt(KEY_I_AC_TYPE) != TYPE_AC_FROM_FAV)
            {
                item.setTnMenu(longClickMenu);
            }

            return convertView;
        }

        protected View getItemViewBeforeSelected(int position, View convertView, ViewGroup parent)
        {
            // return convertView.findViewById(R.id.placeList0NormalItem);
            return null;
        }

        protected View getItemViewAfterSelected(int position, View convertView, ViewGroup parent)
        {
            // return convertView.findViewById(R.id.placeList0NormalSelectedItem);
            return null;
        }
    }

    protected synchronized void deactivateDelegate()
    {
        closeFeedbackPopup();
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        super.deactivateDelegate();
    }

    @Override
    public void onRefresh()
    {
        AndroidCitizenUiHelper.triggerCommand(PoiResultViewTouch.this, placeList, IPoiResultConstants.CMD_REDO_SEARCH);
    }

    @Override
    public void onLoadMore()
    {
        AndroidCitizenUiHelper.triggerCommand(PoiResultViewTouch.this, placeList, IPoiResultConstants.CMD_HIT_BOTTOM);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        int curVisibleStartIndex = firstVisibleItem - poiListView.getHeaderViewsCount();
        int curVisibleEndIndex = firstVisibleItem + visibleItemCount - poiListView.getHeaderViewsCount();
        if (curVisibleStartIndex < 0)
        {
            curVisibleStartIndex = 0;
        }
        if (curVisibleEndIndex > totalItemCount - poiListView.getHeaderViewsCount() - poiListView.getFooterViewsCount() - 1)
        {
            curVisibleEndIndex = totalItemCount - poiListView.getHeaderViewsCount() - poiListView.getFooterViewsCount() - 1;
        }
        if (curVisibleEndIndex == totalItemCount - poiListView.getHeaderViewsCount() - poiListView.getFooterViewsCount() - 1)
        {
            AndroidCitizenUiHelper.triggerCommand(this, placeList, CMD_SHOW_FEEDBACK);
        }
        else if (curVisibleEndIndex < lastVisibleEndIndex)
        {
            AndroidCitizenUiHelper.triggerCommand(this, placeList, CMD_HIDE_FEEDBACK);
        }
        if (curVisibleStartIndex > -1 && curVisibleEndIndex >= curVisibleStartIndex)
        {
            checkVisibleIndex(curVisibleStartIndex, curVisibleEndIndex);
            lastVisibleStartIndex = curVisibleStartIndex;
            lastVisibleEndIndex = curVisibleEndIndex;
        }
    }

    private void checkVisibleIndex(int curVisibleStartIndex, int curVisibleEndIndex)
    {
        if (curVisibleStartIndex < lastVisibleStartIndex && curVisibleEndIndex > lastVisibleEndIndex)
        {
            checkVisibleIndex(curVisibleStartIndex, lastVisibleStartIndex);
            checkVisibleIndex(lastVisibleEndIndex, curVisibleEndIndex);
        }
        else
        {
            impressionStartIndex = -1;
            impressionEndIndex = -1;
            PoiResultModel poiResultModel = (PoiResultModel) model;
            if (curVisibleEndIndex > lastVisibleEndIndex && curVisibleStartIndex >= lastVisibleStartIndex)
            {
                impressionStartIndex = lastVisibleEndIndex + 1;
                impressionEndIndex = curVisibleEndIndex;
                if (impressionEndIndex == poiResultModel.getSize() - 1 && poiResultModel.hasMorePoi())
                {
                    if (impressionStartIndex == impressionEndIndex)
                    {
                        impressionStartIndex = -1;
                        impressionEndIndex = -1;
                    }
                    else
                    {
                        impressionEndIndex--;
                    }
                }
            }
            if (curVisibleStartIndex < lastVisibleStartIndex && curVisibleEndIndex <= lastVisibleEndIndex)
            {
                impressionStartIndex = curVisibleStartIndex;
                impressionEndIndex = lastVisibleStartIndex - 1;
            }
            if (impressionStartIndex != -1 && impressionEndIndex != -1)
            {
                PoiMisLogHelper.getInstance().impressPoi(impressionStartIndex, impressionEndIndex);
            }
        }
    }
}
