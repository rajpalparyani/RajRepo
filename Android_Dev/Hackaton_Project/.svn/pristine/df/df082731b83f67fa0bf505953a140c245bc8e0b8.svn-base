/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MisLogFactory.java
 *
 */
package com.telenav.log.mis;

import com.telenav.log.mis.helper.AbstractMisLogHelper;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.log.mis.helper.RTTMisLogHelper;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.log.mis.log.AddressMisLog;
import com.telenav.log.mis.log.AppSessionMisLog;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.log.mis.log.ArrivalConfirmationMisLog;
import com.telenav.log.mis.log.BillboardMisLog;
import com.telenav.log.mis.log.ClickStreamMisLog;
import com.telenav.log.mis.log.DefaultMisLog;
import com.telenav.log.mis.log.DsrGenericMisLog;
import com.telenav.log.mis.log.FeedbackMisLog;
import com.telenav.log.mis.log.FirstTimeLoginMisLog;
import com.telenav.log.mis.log.HomeScreenTimeMisLog;
import com.telenav.log.mis.log.MapDisplayMisLog;
import com.telenav.log.mis.log.MapDownloadMisLog;
import com.telenav.log.mis.log.MapUpdateMisLog;
import com.telenav.log.mis.log.NavsdkDefaultMisLog;
import com.telenav.log.mis.log.OnboardMapDisplayMisLog;
import com.telenav.log.mis.log.OnboardStartupInfoMisLog;
import com.telenav.log.mis.log.PoiMisLog;
import com.telenav.log.mis.log.PreferenceChangeMisLog;
import com.telenav.log.mis.log.RouteRequestMisLog;
import com.telenav.log.mis.log.RttMisLog;
import com.telenav.log.mis.log.SearchRequestMisLog;
import com.telenav.log.mis.log.SessionStartupMisLog;
import com.telenav.log.mis.log.SortRequestMislog;
import com.telenav.log.mis.log.SpeedCameraMisLog;
import com.telenav.log.mis.log.SpeedLimitMisLog;
import com.telenav.log.mis.log.StartupInfoMisLog;
import com.telenav.log.mis.log.TripSummaryMisLog;
import com.telenav.log.mis.log.UiUsageMisLog;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-1
 */
public class MisLogFactory
{
    private AbstractMisLogHelper rttHelper = null;
    private long lastTimeMillis = -1;
    private long offset = 0;

    public MisLogFactory()
    {

    }
    
    public AppStatusMisLog createAppStatusMisLog()
    {
        AppStatusMisLog log = new AppStatusMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public SessionStartupMisLog createSessionStartupMisLog()
    {
        SessionStartupMisLog log = new SessionStartupMisLog("", IMisLogConstants.PRIORITY_1);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public StartupInfoMisLog createStartupInfoMisLog()
    {
        StartupInfoMisLog log = new StartupInfoMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public OnboardStartupInfoMisLog createOnboardStartupInfoMisLog()
    {
        OnboardStartupInfoMisLog log = new OnboardStartupInfoMisLog("", IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public OnboardMapDisplayMisLog createOnboardMapDisplayMisLog()
    {
        OnboardMapDisplayMisLog log = new OnboardMapDisplayMisLog("", IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public AppSessionMisLog creatAppSessionMisLog()
    {
        AppSessionMisLog log = new AppSessionMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public RttMisLog createRttMisLog()
    {
        RttMisLog log = new RttMisLog("", IMisLogConstants.PRIORITY_3, getMisLogHelper(IMisLogConstants.TYPE_RTT));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public AddressMisLog createOneBoxAddressImpressionMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_ONEBOX_ADDRESS_IMPRESSION, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public AddressMisLog createOneBoxAddressSelectionMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_ONEBOX_ADDRESS_SELECTION, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public AddressMisLog createOneBoxAddressOneResultMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_ONEBOX_ADDRESS_ONE_RESULT, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public AddressMisLog createOneBoxSuggestionsImpressionMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_IMPRESSION, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public AddressMisLog createOneBoxSuggestionsClickMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_CLICK, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public AddressMisLog createTwoBoxAddressImpressionMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_TWOBOX_ADDRESS_IMPRESSION, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }  
    
    public AddressMisLog createTwoBoxAddressSelectionMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_TWOBOX_ADDRESS_SELECTION, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public AddressMisLog createTwoBoxAddressOneResultMisLog()
    {
        AddressMisLog log = new AddressMisLog("", IMisLogConstants.TYPE_TWOBOX_ADDRESS_ONE_RESULT, IMisLogConstants.PRIORITY_2);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public PoiMisLog createPoiImpressionMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_IMPRESSION, IMisLogConstants.PRIORITY_1,
                getMisLogHelper(IMisLogConstants.TYPE_POI_IMPRESSION));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiDetailMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_DETAILS, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_DETAILS));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiViewMapMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_VIEW_MAP, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_VIEW_MAP));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiDriveToMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_DRIVE_TO, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_DRIVE_TO));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiShareMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_SHARE, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_SHARE));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public PoiMisLog createPoiCallToMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_CALL_TO, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_CALL_TO));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiMerchantMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_VIEW_MERCHANT, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_VIEW_MERCHANT));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiViewCoupnMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_VIEW_COUPON, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_VIEW_COUPON));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiViewMenuMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_VIEW_MENU, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_VIEW_MENU));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiMapAllMisLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_MAP_ALL, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_MAP_ALL));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public PoiMisLog createPoiAddPlaceMislog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_ADD_PLACE, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_ADD_PLACE));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public PoiMisLog createPoiReviewTabClickLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_REVIEW_TAB_CLICK, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_REVIEW_TAB_CLICK));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PoiMisLog createPoiReviewLinkOutImpressionLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_IMPRESSION, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_IMPRESSION));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    
    public PoiMisLog createPoiReviewLinkOutClickLog()
    {
        PoiMisLog log = new PoiMisLog("", IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_CLICK, IMisLogConstants.PRIORITY_1,
            getMisLogHelper(IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_CLICK));
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public RouteRequestMisLog createRouteRequestMisLog()
    {
        RouteRequestMisLog log = new RouteRequestMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public TripSummaryMisLog createTripSummaryMisLog()
    {
        TripSummaryMisLog log = new TripSummaryMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public UiUsageMisLog createUiUsageMisLog()
    {
        UiUsageMisLog log = new UiUsageMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public ArrivalConfirmationMisLog createArrivalConfirmationMisLog()
    {
        ArrivalConfirmationMisLog log = new ArrivalConfirmationMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public ClickStreamMisLog createClickStreamMisLog()
    {
        ClickStreamMisLog log = new ClickStreamMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public FirstTimeLoginMisLog createFirstTimeLoginMisLog()
    {
        FirstTimeLoginMisLog log = new FirstTimeLoginMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public HomeScreenTimeMisLog createHomeScreenTimeMisLog()
    {
        HomeScreenTimeMisLog log = new HomeScreenTimeMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public DsrGenericMisLog createDsrGenericMisLog()
    {
        DsrGenericMisLog log = new DsrGenericMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public FeedbackMisLog createFeedbackMisLog()
    {
        FeedbackMisLog log = new FeedbackMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public PreferenceChangeMisLog createPreferenceChangeMisLog()
    {
        PreferenceChangeMisLog log = new PreferenceChangeMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public MapDisplayMisLog createMapDisplayMisLog()
    {
        MapDisplayMisLog log = new MapDisplayMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public MapUpdateMisLog createMapUpdateMisLog()
    {
        MapUpdateMisLog log = new MapUpdateMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }


    public SearchRequestMisLog createSearchRequestMisLog()
    {
        SearchRequestMisLog log = new SearchRequestMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public SortRequestMislog createSortRequestMislog()
    {
        SortRequestMislog log = new SortRequestMislog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public BillboardMisLog createBillboardMisLog(int type)
    {
        BillboardMisLog log = new BillboardMisLog("", type, IMisLogConstants.PRIORITY_1);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public SpeedCameraMisLog createSpeedCameraMisLog()
    {
        SpeedCameraMisLog log = new SpeedCameraMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }

    public SpeedLimitMisLog createSpeedLimitMisLog()
    {
        SpeedLimitMisLog log = new SpeedLimitMisLog("", IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public DefaultMisLog createDefaultMislog(int type)
    {
        DefaultMisLog log = new DefaultMisLog("", type, IMisLogConstants.PRIORITY_3);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public NavsdkDefaultMisLog createNavsdkDefaultMisLog(int type)
    {
        int mislogPriority = IMisLogConstants.PRIORITY_3;
        if (IMisLogConstants.TYPE_ON_BOARD_SET_DESTINATION == type || IMisLogConstants.TYPE_ON_BOARD_VALIDATA_ADDR == type)
        {
            mislogPriority =  IMisLogConstants.PRIORITY_2;
        }
        NavsdkDefaultMisLog log = new NavsdkDefaultMisLog("", type, mislogPriority);
        log.setId(String.valueOf(log.getType() + "_" + getId()));
        return log;
    }
    
    public MapDownloadMisLog createMapDownloadMisLog(int type)
    {
        MapDownloadMisLog log = new MapDownloadMisLog("", type, IMisLogConstants.PRIORITY_2);
        
        return log;
    }
    
    protected String getId()
    {
        long time = System.currentTimeMillis();
        if(time <= lastTimeMillis + offset)
        {
            offset  = (lastTimeMillis + offset - time) + 1;
        }
        else
        {
            offset = 0;
            lastTimeMillis = time;
        }
        return String.valueOf(lastTimeMillis + "" + offset); 
    }

    public AbstractMisLog createMisLog(int type)
    {
        AbstractMisLog log;
        switch (type)
        {
            case IMisLogConstants.TYPE_INNER_APP_STATUS:
            {
                log = createAppStatusMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ONEBOX_ADDRESS_IMPRESSION:
            {
                log = createOneBoxAddressImpressionMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ONEBOX_ADDRESS_SELECTION:
            {
                log = createOneBoxAddressSelectionMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ONEBOX_ADDRESS_ONE_RESULT:
            {
                log = createOneBoxAddressOneResultMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_IMPRESSION:
            {
                log = createOneBoxSuggestionsImpressionMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ONEBOX_SUGGESTIONS_CLICK:
            {
                log = createOneBoxSuggestionsClickMisLog();
                break;
            }
            case IMisLogConstants.TYPE_TWOBOX_ADDRESS_IMPRESSION:
            {
                log = createTwoBoxAddressImpressionMisLog();
                break;
            }
            case IMisLogConstants.TYPE_TWOBOX_ADDRESS_SELECTION:
            {
                log = createTwoBoxAddressSelectionMisLog();
                break;
            }
            case IMisLogConstants.TYPE_TWOBOX_ADDRESS_ONE_RESULT:
            {
                log = createTwoBoxAddressOneResultMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_IMPRESSION:
            {
                log = createPoiImpressionMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_DETAILS:
            {
                log = createPoiDetailMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_VIEW_MAP:
            {
                log = createPoiViewMapMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_DRIVE_TO:
            {
                log = createPoiDriveToMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_SHARE:
            {
                log = createPoiShareMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_CALL_TO:
            {
                log = createPoiCallToMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_VIEW_MERCHANT:
            {
                log = createPoiMerchantMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_VIEW_COUPON:
            {
                log = createPoiViewCoupnMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_VIEW_MENU:
            {
                log = createPoiViewMenuMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_MAP_ALL:
            {
                log = createPoiMapAllMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_ADD_PLACE:
            {
                log = createPoiAddPlaceMislog();
                break;
            }
            case IMisLogConstants.TYPE_RTT:
            {
                log = createRttMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_REVIEW_TAB_CLICK:
            {
                log = createPoiReviewTabClickLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_IMPRESSION:
            {
                log = createPoiReviewLinkOutImpressionLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_CLICK:
            {
                log = createPoiReviewLinkOutClickLog();
                break;
            }
            case IMisLogConstants.TYPE_SESSION_STARTUP:
            {
                log = createSessionStartupMisLog();
                break;
            }
            case IMisLogConstants.TYPE_STARTUP_INFO:
            {
                log = createStartupInfoMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ON_BOARD_STARTUP_INFO:
            {
                log = createOnboardStartupInfoMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ON_BOARD_MAP_DISPLAY:
            {
                log = createOnboardMapDisplayMisLog();
                break;
            }
            case IMisLogConstants.TYPE_APP_SESSION_SUMMARY:
            {
                log = creatAppSessionMisLog();
                break;
            }
            case IMisLogConstants.TYPE_PREFERENCE_CHANGE:
            {
                log = createPreferenceChangeMisLog();
                break;
            }
            case IMisLogConstants.TYPE_FEEDBACK:
            {
                log = createFeedbackMisLog();
                break;
            }
            case IMisLogConstants.TYPE_ROUTE_REQUEST:
            {
                log = createRouteRequestMisLog();
                break;
            }
            case IMisLogConstants.TYPE_TRIP_SUMMARY:
            {
                log = createTripSummaryMisLog();
                break;
            }
            // added for Arrival Confirmation
            case IMisLogConstants.TYPE_ARIVALl_CONFIRMATION:
            {
                log = createArrivalConfirmationMisLog();
                break;
            }
            case IMisLogConstants.TYPE_MAP_UPDATE_TIME:
            {
                log = createMapUpdateMisLog();
                break;
            }
            case IMisLogConstants.TYPE_MAP_DISPLAY_TIME:
            {
                log = createMapDisplayMisLog();
                break;
            }
            case IMisLogConstants.TYPE_SPEED_CAMERA_IMPRESSION:
            {
                log = createSpeedCameraMisLog();
                break;
            }
            case IMisLogConstants.TYPE_SPEED_LIMIT_IMPRESSION:
            {
                log = createSpeedLimitMisLog();
                break;
            }
            case IMisLogConstants.TYPE_HOME_SCREEN_TIME:
            {
                log = createHomeScreenTimeMisLog();
                break;
            }
            case IMisLogConstants.TYPE_CLICK_STREAM:
            {
                log = createClickStreamMisLog();
                break;
            }
            case IMisLogConstants.TYPE_UI_USAGE_REPORT:
            {
                log = createUiUsageMisLog();
                break;
            }
            case IMisLogConstants.TYPE_FIRST_TIME_LOGIN:
            {
                log = createFirstTimeLoginMisLog();
                break;
            }
            case IMisLogConstants.TYPE_DSR_GENERIC:
            {
                log = createDsrGenericMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_SEARCH_REQUEST:
            {
                log = createSearchRequestMisLog();
                break;
            }
            case IMisLogConstants.TYPE_POI_SORT_REQUEST:
            {
                log = createSortRequestMislog();
                break;
            }
            case IMisLogConstants.TYPE_BILLBOARD_DETAIL_MORE:
            case IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_DRIVE_TO:
            case IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_END:
            case IMisLogConstants.TYPE_BILLBOARD_DETAIL_VIEW_START:
            case IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_CLICK:
            case IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_END:
            case IMisLogConstants.TYPE_BILLBOARD_INITIAL_VIEW_START:
            case IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_CALL_TO:
            case IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_DEAL_TAB:
            case IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_DRIVE_TO:
            case IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_END:
            case IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_MAP:
            case IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_MENU_TAB:
            case IMisLogConstants.TYPE_BILLBOARD_POI_VIEW_START:
            {
                log = createBillboardMisLog(type);
                break;
            }
            case IMisLogConstants.TYPE_MAP_DOWNLOAD_START:
            case IMisLogConstants.TYPE_MAP_DOWNLOAD_COMPLETE:
            {
                log = createMapDownloadMisLog(type);
                break;
            }
            case IMisLogConstants.TYPE_ON_BOARD_SET_DESTINATION:
            case IMisLogConstants.TYPE_ON_BOARD_VALIDATA_ADDR:
            {
                log = createNavsdkDefaultMisLog(type);
                break;
            }
            default:
            {
                log = createDefaultMislog(type);
                break;
            }
        }
        return log;
    }
    
    public INavsdkMisLog createNavsdkMisLog(int type)
    {
        INavsdkMisLog log;
        switch (type)
        {
            case IMisLogConstants.TYPE_RTT:
            {
                log = createRttMisLog();
                break;
            }
            case IMisLogConstants.TYPE_TRIP_SUMMARY:
            {
                log = createTripSummaryMisLog();
                break;
            }
            default:
            {
                log = createNavsdkDefaultMisLog(type);
                break;
            }
        }
        return log;
    }

    public AbstractMisLogHelper getMisLogHelper(int type)
    {
        AbstractMisLogHelper helper = null;
        switch (type)
        {
            case IMisLogConstants.TYPE_RTT:
            {
                if (rttHelper == null)
                {
                    rttHelper = new RTTMisLogHelper();
                }
                helper = rttHelper;
                break;
            }
            case IMisLogConstants.TYPE_POI_IMPRESSION:
            case IMisLogConstants.TYPE_POI_DETAILS:
            case IMisLogConstants.TYPE_POI_VIEW_MAP:
            case IMisLogConstants.TYPE_POI_DRIVE_TO:
            case IMisLogConstants.TYPE_POI_SHARE:
            case IMisLogConstants.TYPE_POI_CALL_TO:
            case IMisLogConstants.TYPE_POI_VIEW_MERCHANT:
            case IMisLogConstants.TYPE_POI_VIEW_COUPON:
            case IMisLogConstants.TYPE_POI_VIEW_MENU:
            case IMisLogConstants.TYPE_POI_MAP_ALL:
            case IMisLogConstants.TYPE_POI_ADD_PLACE:
            case IMisLogConstants.TYPE_POI_REVIEW_TAB_CLICK:
            case IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_IMPRESSION:   
            case IMisLogConstants.TYPE_POI_REVIEW_LINK_OUT_CLICK:
            {
                helper = PoiMisLogHelper.getInstance();
                break;
            }
            default:
            {

            }
        }
        return helper;
    }
    

    public void resetAllMisLogHelper()
    {
        if (rttHelper != null)
        {
            rttHelper.reset();
        }
    }

}
