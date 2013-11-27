/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMisLogConstants.java
 *
 */
package com.telenav.log.mis;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-1
 */
public interface IMisLogConstants
{
    /** The string token that indicates this is a mislog */
    public static final String PROCESS_MISLOG = "process_mis_log";

    /** Store ready-to-go event as soon as it appears in stat logger */
    public static final int PRIORITY_1 = 1;

    /** Store ready-to-go events if its was retrieved from stat logger and was not acknowledged */
    public static final int PRIORITY_2 = 2;

    /** Store at application exit */
    public static final int PRIORITY_3 = 3;

    /** Do not store event */
    public static final int PRIORITY_4 = 4;

    // 4XX inner log only for internal use
    public static final int TYPE_INNER_APP_STATUS           = 400;
    
    
    /** Whenever impress multiple address from onebox. See TrasactionId integration Release FSD v1 4.doc 2.4.2 */
    public static final int TYPE_ONEBOX_ADDRESS_IMPRESSION  = 610;

    /** Whenever select one address from multiple result of onebox. See TrasactionId integration Release FSD v1 4.doc 2.4.2 */
    public static final int TYPE_ONEBOX_ADDRESS_SELECTION   = 611;
    
    /** Whenever only return one result from onebox. According to Vlad and Maneesha*/
    public static final int TYPE_ONEBOX_ADDRESS_ONE_RESULT  = 612;
    
    /** Whenever impress suggestions from onebox. See Onebox Logging FSD v1.7.doc*/
    public static final int TYPE_ONEBOX_SUGGESTIONS_IMPRESSION  = 613;
    
    /** Whenever click one suggestions from onebox. See Onebox Logging FSD v1.7.doc*/
    public static final int TYPE_ONEBOX_SUGGESTIONS_CLICK  = 614;
    
    /** Whenever impress multiple address from twobox. See TrasactionId integration Release FSD v1 4.doc 2.5.3 */
    public static final int TYPE_TWOBOX_ADDRESS_IMPRESSION  = 620;

    /** Whenever select one address from multiple result of twobox. See TrasactionId integration Release FSD v1 4.doc 2.5.3 */
    public static final int TYPE_TWOBOX_ADDRESS_SELECTION   = 621;
    
    /** Whenever only return one result from twobox. According to Vlad and Maneesha*/
    public static final int TYPE_TWOBOX_ADDRESS_ONE_RESULT  = 622;

    /** Car connect session started */
    public static final int TYPE_HEAD_UNIT_CAR_CONNECT = 650;
    
    /** Car Connect session ended */
    public static final int TYPE_HEAD_UNIT_CAR_DISCONNECT = 651;
    
    /** POI search performed when car connect is active */
    public static final int TYPE_HEAD_UNIT_CONNECTED_POI_SEARCH = 652;
    
    /** Drive to and navigation is requested when car connect is active */
    public static final int TYPE_HEAD_UNIT_CONNECTED_DRIVE_TO = 653;

    /**Requirement from 5.x + extras nn. 2.2.1.6 (CL3) */
    public static final int TYPE_POI_IMPRESSION             = 700;

    public static final int TYPE_POI_DETAILS                = 701;

    public static final int TYPE_POI_VIEW_MAP               = 702;
    
    public static final int TYPE_POI_DRIVE_TO               = 703;

    public static final int TYPE_POI_CALL_TO                = 704;

    public static final int TYPE_POI_VIEW_MERCHANT          = 705;

    public static final int TYPE_POI_VIEW_COUPON            = 706;

    public static final int TYPE_POI_VIEW_MENU              = 707;

    public static final int TYPE_POI_MAP_ALL                = 708;

    public static final int TYPE_RTT                        = 710;
    
    public static final int TYPE_POI_ADD_PLACE              = 726;
    
    public static final int TYPE_POI_SHARE                  = 727;

    
    /** Yelp Integration Logging */
    public static final int TYPE_POI_REVIEW_TAB_CLICK       = 760;
  
    public static final int TYPE_POI_REVIEW_LINK_OUT_IMPRESSION  = 761;
    
    public static final int TYPE_POI_REVIEW_LINK_OUT_CLICK  = 762;
    
    
    /** generic event with sessionid/device/ptn/client version, etc. See nn. 2.1 */
    public static final int TYPE_SESSION_STARTUP            = 720;

    /** How TN was started. See nn. 2.2.1.2 (CL9) */
    public static final int TYPE_STARTUP_INFO               = 721;
    // if it is after "soft" exit then these events should be initialized again
    // composite event for app session summary
    /** Session summary. See nn. 2.2.1.4 (CL2) */
    public static final int TYPE_APP_SESSION_SUMMARY        = 722;

    /** Whenever preferences are changed. See nn. 2.2.1.14 (CL11) */
    public static final int TYPE_PREFERENCE_CHANGE          = 723;

    /** Whenever feedback request is sent. See nn. 2.2.1.7 (CL14) */
    public static final int TYPE_FEEDBACK                   = 724;

    /** Route request. See nn. 2.2.1.5 (CL4) */
    public static final int TYPE_ROUTE_REQUEST              = 730;

    /** Trip summary. See nn. 2.2.1.3 (CL1) */
    public static final int TYPE_TRIP_SUMMARY               = 731;

    /** Total map update time. See nn. 2.2.1.12 (CL7) */
    public static final int TYPE_MAP_UPDATE_TIME            = 732;

    /** Total map update time. See nn. 2.2.1.11 */
    public static final int TYPE_MAP_DISPLAY_TIME           = 733;

    /** Whenever speed camera is shown. See nn. 2.2.1.16 (CL10) */
    public static final int TYPE_SPEED_CAMERA_IMPRESSION    = 734;

    /** Whenever speed limit is shown. See nn. 2.2.1.17 (CL13) */
    public static final int TYPE_SPEED_LIMIT_IMPRESSION     = 735;
    
    /** Arrival_Confirmation */
    public static final int TYPE_ARIVALl_CONFIRMATION    = 736;
    
    /** Total home screen time. See nn. 2.2.1.10 (CL6) */
    public static final int TYPE_HOME_SCREEN_TIME           = 740;

    // initial click-stream event till home page.
    // mostly used if TeleNav is started through MaiTai or external apps to certain entry point
    // generic click-stream starts and ends at home page through Fscript
    /** Clickstream before request. See nn. 2.2.1.13 (CL8) */
    public static final int TYPE_CLICK_STREAM               = 741;

    /** Generic logging event for client action without invoking server. See nn. 2.2.1.15 (CL12) */
    public static final int TYPE_UI_USAGE_REPORT            = 742;

    /** Once in lifetime event. See nn. 2.2.1.9 (CL5) */
    public static final int TYPE_FIRST_TIME_LOGIN           = 743;

    /** Whenever DSR is used. See nn. 2.2.1.8 (CL15) */
    public static final int TYPE_DSR_GENERIC                = 750;
  
    /** Whenever POI search is used. See nn. 2.2.1.6 (CL3) */
    public static final int TYPE_POI_SEARCH_REQUEST         = 751;
    
    /** Whenever the sort action took place. see 11.8 in [Log Parsing ID Specification.doc]*/
    public static final int TYPE_POI_SORT_REQUEST           = 752;
    
    /**
     * Billboard initial view start
     */
    public static final int TYPE_BILLBOARD_INITIAL_VIEW_START = 780;

    /**
     * Billboard initial view end
     */
    public static final int TYPE_BILLBOARD_INITIAL_VIEW_END = 781;

    public static final int TYPE_BILLBOARD_INITIAL_VIEW_CLICK = 783;

    /**
     * Billboard detail view start
     */
    public static final int TYPE_BILLBOARD_DETAIL_VIEW_START = 784;

    /**
     * Billboard detail view end
     */
    public static final int TYPE_BILLBOARD_DETAIL_VIEW_END = 785;

    /**
     * Billboard detail view drive to
     */
    public static final int TYPE_BILLBOARD_DETAIL_VIEW_DRIVE_TO = 788;

    /**
     * Billboard detail view more
     */
    public static final int TYPE_BILLBOARD_DETAIL_MORE = 789;
    
    
    /**
     * Billboard poi view start
     */
    public static final int TYPE_BILLBOARD_POI_VIEW_START = 790;

    /**
     * Billboard poi view end
     */
    public static final int TYPE_BILLBOARD_POI_VIEW_END = 791;

    public static final int TYPE_BILLBOARD_POI_VIEW_DRIVE_TO = 792;
    public static final int TYPE_BILLBOARD_POI_VIEW_CALL_TO = 793;
    public static final int TYPE_BILLBOARD_POI_VIEW_DEAL_TAB = 794;
    public static final int TYPE_BILLBOARD_POI_VIEW_MENU_TAB = 795;
    public static final int TYPE_BILLBOARD_POI_VIEW_MAP = 796;
       
    public static final int TYPE_ON_BOARD_MAP_DISPLAY        = 31;
    public static final int TYPE_ON_BOARD_STARTUP_INFO       = 600;
    public static final int TYPE_ON_BOARD_SET_DESTINATION    = 10;
    public static final int TYPE_ON_BOARD_VALIDATA_ADDR      = 100;

    
    public static final int TYPE_MAP_DOWNLOAD_START = 900;
    public static final int TYPE_MAP_DOWNLOAD_COMPLETE = 902;
    
    
    public static final int INNER_ATTR_POI_INDEX              = -10001;
    
    public static final int INNER_ATTR_ADDRESS_TYPE           = -10002;
    
    public static final int INNER_ATTR_ADDRESS_SOURCE         = -10003;


    // standard attributes for all events 10001-10010
    public static final int ATTR_EVENT_TYPE_ID                = 10001;

    public static final int ATTR_EVENT_ID                     = 10002;

    public static final int ATTR_EVENT_TIMESTAMP              = 10003;
    
    public static final int ATTR_EVENT_COUNTER                = 10011;// event counter
    
    public static final int ATTR_EVENT_DURATION               = 10012;// duration event
    
    public static final int ATTR_HOME_CARRIER                 = 10210;// home carrier

    // //////////////////////// business specific attribute ids //////////////

    // generic attributes for all events
    // start lat/lon or just lat and lon
    // lat and lon are optional for an event
    public static final int ATTR_GENERIC_LAT                  = 10101;

    public static final int ATTR_GENERIC_LON                  = 10102;

    public static final int ATTR_GENERIC_END_LAT              = 10103;

    public static final int ATTR_GENERIC_END_LON              = 10104;

    public static final int ATTR_GENERIC_BATTERY_START        = 10105;

    public static final int ATTR_GENERIC_BATTERY_END          = 10106;

    // everything is optional if ZIP is provided
    public static final int ATTR_GENERIC_STREET               = 10107;

    public static final int ATTR_GENERIC_CITY                 = 10108;

    public static final int ATTR_GENERIC_ZIP                  = 10109;

    public static final int ATTR_GENERIC_STATE                = 10110;

    public static final int ATTR_GENERIC_COUNTRY_CODE         = 10111;

    public static final int ATTR_GENERIC_ADDRESS_TYPE         = 10112;

    public static final int ATTR_GENERIC_ADDRESS_SOURCE       = 10113;

    public static final int ATTR_GENERIC_ROUTE_ID             = 10114;    

    public static final int ATTR_GENERIC_SIGNAL_LEVEL         = 10115;

    public static final int ATTR_GENERIC_NETWORK_TYPE         = 10116;
    
    public static final int ATTR_GENERIC_DISPLAY_STRING       = 10117;
 
    public static final int ATTR_GENERIC_DEVICE_ID            = 10118;
    
    public static final int ATTR_GENERIC_USER_ID              = 10119;    

    public static final int ATTR_GENERIC_GPS_LOSS_COUNT       = 400;/*10120*/
    
    public static final int ATTR_GENERIC_GPS_LOSS_TIME        = 401;/*10121*/
    
    public static final int ATTR_GENERIC_NETWORK_LOSS_COUNT   = 402;/*10122*/
    
    public static final int ATTR_GENERIC_NETWORK_LOSS_TIME    = 403;/*10123*/
    
    //below are car cnnect specific attri butes
    
    /** Session ID for each car connect connection */
    public static final int ATTR_CAR_CONNECT_SESSION_ID         = 10300;
    
    /** car connect product category Product Category: Production OEM=1, Aftermarket device=2, Embedded Connected=3*/
    public static final int ATTR_CAR_CONNECT_PRODUCT_CATEGORY   = 10302; // supplied by car connect lib
    
    /** OEM/brand of the head unit the make car connect request */
    public static final int ATTR_CAR_CONNECT_OEM                = 10303; // supplied by car connect lib
    
    /** car connect connection technology between head unit and phone, Ford Applink, TN Link, etc.*/
    public static final int ATTR_CAR_CONNECT_CONNECTION_TECHNOLOGY  = 10304;    //supplied by car connect lib
    
    /** car connect connection medium between head unit & phone, USB, WIFI, BT */
    public static final int ATTR_CAR_CONNECT_CONNCTION_MEDIUM   =10305; //supplied by car connect lib
    
    /** car connect head unit device model number or type */
    public static final int ATTR_CAR_CONNECT_DEVICE_TYPE        = 10306; // supplied by car connect lib
    
    /** vehicle VIN number of the car making car connection */
    public static final int ATTR_CAR_CONNECT_VIN_NUM            = 10307; //supplied by car connect lib
    
    /** unique ID of car connect head unit */
    public static final int ATTR_CAR_CONNECT_HU_ID              = 10308; // supplied by car connect lib
    
    /** Car connect Hybrid flag: on-board=1; off-board=0 (on-board means without data connection). Optional - if missing, assume off-board */
    public static final int ATTR_CAR_CONNECT_HYBRID_FLAG        = 10309; // supplied by car connect lib
    
    /** Car connect session length in millisecond */
    public static final int ATTR_CAR_CONNECT_SESSION_LENGTH     = 10310;
    
    /** car connect connectivity protocol version */
    public static final int ATTR_CAR_CONNECT_CONNECTIVITY_PROTOCOL_VERSION  = 10311; // supplied by car connect lib
    
    
    
    
    
            
    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_POI_IMPRESSION
     */
    // 700-708 have same structure
    public static final int ATTR_POI_ID                       = 70001;

    public static final int ATTR_ADS_ID                       = 70002;

    public static final int ATTR_AD_SOURCE                    = 70003;

    public static final int ATTR_SEARCH_UID                   = 70004;

    public static final int ATTR_PAGE_NAME                    = 70005;

    public static final int ATTR_PAGE_INDEX                   = 70006;

    public static final int ATTR_POI_RATING                   = 70007;

    public static final int ATTR_POI_DISTANCE                 = 70008;

    public static final int ATTR_POI_SORTING                  = 70009;

    public static final int ATTR_PAGE_NUMBER                  = 70010;

    public static final int ATTR_PAGE_SIZE                    = 70011;

    public static final int ATTR_POI_TYPE                     = 70012;
    
    public static final int ATTR_VIEW_TIME                    = 70013;
    
    public static final int ATTR_RESULT_NUMBER                = 70014;
    
    public static final int ATTR_SEARCH_TYPE                  = 70015;

    public static final int ATTR_POI_ELAPSED_TIME             = 70016;
    
    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_ONEBOX_SUGGESTIONS_CLICK
     */
    public static final int ATTR_ONEBOX_SUGGESTION_KEYWORD    = 11077;
    
    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_RTT
     */
    public static final int ATTR_RTT_ACTION_ID                = 70030;

    public static final int ATTR_RTT_ACTION_NAME              = 70031;

    public static final int ATTR_REQUEST_SIZE                 = 70032;

    public static final int ATTR_RESPONSE_SIZE                = 70033;

    public static final int ATTR_ROUNDTRIP_TIME               = 70034;
    
    /**
     * Exit Type, 1 -> Natural exit; 2-> Forced Exit; 3->Minimize Exit
     */
    public static final int ATTR_BILLBOARD_EXIT_TYPE          = 70037;
    
    public static final String VALUE_BILLBOARD_EXIT_TYPE_NATURAL = "1";

    public static final String VALUE_BILLBOARD_EXIT_TYPE_FORCED = "2";

    public static final String VALUE_BILLBOARD_EXIT_TYPE_MINIMIZE = "3";

    /**
     * Elapsed time for billboard ad
     */
    public static final int ATTR_BILLBOARD_ELAPSED_TIME       = 70038;
    

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_SESSION_STARTUP
     */
    public static final int ATTR_SESSION_ID                   = 10200;

    public static final int ATTR_PTN                          = 10201;

    public static final int ATTR_PROGRAM_CODE                 = 10202;

    public static final int ATTR_DEVICE_MAKE                  = 10203;

    public static final int ATTR_DEVICE_MODEL                 = 10204;

    public static final int ATTR_DEVICE_OS                    = 10205;

    public static final int ATTR_APP_VERSION                  = 10206;

    public static final int ATTR_CLIENT_VERSION               = 10207;
    
    public static final int ATTR_DEVICE_OS_VERSION            = 10208;
    
    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_STARTUP_INFO
     */
    public static final int ATTR_STARTED_BY                   = 10220;

    public static final int ATTR_STARTUP_ENTRY_POINT          = 10221;

    public static final int ATTR_STARTED_FROM                 = 10222;
    
    public static final int ATTR_START_TIME                   = 10223;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_APP_SESSION_SUMMARY
     */
    public static final int ATTR_APP_SESSION_SEARCH_COUNT     = 10230;
    
    public static final int ATTR_APP_SESSION_TIME_TO_SHOW_HOME= 10231;
    
    public static final int ATTR_APP_SESSION_EXIT_PAGE        = 10232;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_PREFERENCE_CHANGE
     */
    public static final int ATTR_PREFERENCE_ID                = 10240;

    public static final int ATTR_PREF_OLD_VALUE               = 10241;

    public static final int ATTR_PREF_NEW_VALUE               = 10242;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_FEEDBACK
     */
    public static final int ATTR_FEEDBACK_APP_CONTEXT         = 10250;

    public static final int ATTR_FEEDBACK_USE_CASE            = 10251;

    public static final int ATTR_FEEDBACK_INVOKED             = 10252;
    
    public static final int ATTR_FEEDBACK_TYPE                = 10253;
    
    public static final int ATTR_FEEDBACK_SECONDARY_CONTEXT   = 10254;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_ROUTE_REQUEST
     */
    public static final int ATTR_ADDRESS_INPUT                = 11001;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_TRIP_SUMMARY
     */
    public static final int ATTR_INITIAL_TRIP_DIST            = 11011;

    public static final int ATTR_ACTUAL_TRIP_DIST             = 11012;

    public static final int ATTR_INITIAL_ETA                  = 11013;

    public static final int ATTR_DEVCNT_NODEV                 = 11014;

    public static final int ATTR_DEVCNT_NEWROUTE              = 11015;

    public static final int ATTR_DEVCNT_ALL                   = 11016;

    public static final int ATTR_DEVLOC                       = 11017;

    public static final int ATTR_TRIP_END_METHOD              = 11018;

    public static final int ATTR_TMC_SEGMENTS                 = 11019;

    public static final int ATTR_TRAFFIC_DELAY_FLAG           = 11020;

    public static final int ATTR_TRAFFIC_DELAY_TIME           = 11021;

    public static final int ATTR_TRAFFIC_INCIDENTS_CNT        = 11022;

    public static final int ATTR_SPEED_TRAP_CNT               = 11023;

    public static final int ATTR_REDLIGHT_CAMERA_CNT          = 11024;

    //Obsolete
    public static final int ATTR_CPD_DISTANCE                 = 11025;
    
    public static final int ATTR_NAVIGATION_EXIT_REASON       = 11026;
    
    public static final int ATTR_ARRIVAL_FLAG                 = 11027;
    
    public static final int ATTR_ARRIVAL_GEOFENCE_DISTANCE    = 11028;
    
    public static final int ATTR_REMAINING_DISTANCE_TO_TARGET_ON_EXIT = 11029;
    
    public static final int ATTR_TOTAL_STOP_COUNT             = 404;/*11030*/
    
    public static final int ATTR_TOTAL_STOP_TIME              = 405;/*11031*/
    
    public static final int ATTR_TOTAL_SEARCH_COUNT           = 408;/*11032*/
    
    public static final int ATTR_TOTAL_RESUME_COUNT           = 409;/*11033*/

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_MAP_UPDATE_TIME
     */
    public static final int ATTR_MAP_UPDATE_MIN               = 11041;

    public static final int ATTR_MAP_UDATE_AVG                = 11042;

    public static final int ATTR_MAP_UDATE_MAX                = 11043;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_MAP_DISPLAY_TIME
     */
    public static final int ATTR_MAP_DISPLAY_TIME             = 11051;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_SPEED_CAMERA_IMPRESSION
     */
    public static final int ATTR_SPEED_TRAP_ID                = 11061;

    public static final int ATTR_SPEED_TRAP_APP_SCREEN        = 11062;

    public static final int ATTR_SPEED_TRAP_ALERT             = 11063;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_SPEED_LIMIT_IMPRESSION
     */
    public static final int ATTR_SPEED_LIMIT_CNT              = 11071;

    public static final int ATTR_SPEED_AVG                    = 11072;

    public static final int ATTR_SPEED_MAX                    = 11073;

    public static final int ATTR_SPEED_LIMIT                  = 11074;

    public static final int ATTR_ALERT_AVG_TIME               = 11075;

    public static final int ATTR_ALERT_AVG_MAX                = 11076;
    
    public static final int ATTR_SPEED_LIMIT_SEGMENT_ID       = 11070;
    
    public static final int ATTR_SPEED_LIMIT_START_TIME       = 11069;
    
    public static final int ATTR_SPEED_LIMIT_END_TIME         = 11068;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_HOME_SCREEN_TIME
     */
    public static final int ATTR_TIME_1                       = 13001;

    public static final int ATTR_TIME_2                       = 13002;

    public static final int ATTR_IS_FIRST_TIME_LOGIN          = 13003;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_CLICK_STREAM
     */
    //

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_FIRST_TIME_LOGIN
     */
    // same as 740

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_UI_USAGE_REPORT
     */
    public static final int ATTR_UI_ID                        = 13011;

    public static final int ATTR_UI_CONTAINER                 = 13012;

    public static final int ATTR_UI_USE_CASE                  = 13013;

    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_DSR_GENERIC
     */
    public static final int ATTR_DSR_USE_CASE                 = 13031;
    
    public static final int ATTR_DSR_ATTEMPT_COUNT            = 13032;
    
    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_POI_SEARCH_REQUEST
     */
    public static final int ATTR_SEARCH_AREA                  = 70023;
    
    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_POI_SORT_REQUEST
     */
    public static final int ATTR_DSR_INITIATED_FLAG           = 70015;
    
    public static final int ATTR_RESULT_TYPE                  = 70016;
    
    public static final int ATTR_SEARCH_KEYWORD               = 70017;
    
    public static final int ATTR_CATEGORY_ID                  = 70018;
    
    
    /**
     * @see com.telenav.log.mis.IMisLogConstants#TYPE_MAP_DOWNLOAD_START
     */
    public static final int ATTR_MAP_DOWNLOAD_REGION_ID       = 11052;
    
    /**
     * Address type n.n. 2.2.1.5
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_GENERIC_ADDRESS_TYPE
     */
    public static final String VALUE_AT_CITY                       = "1";

    public static final String VALUE_AT_POI                        = "2";

    public static final String VALUE_AT_INTERSECTION               = "3";

    public static final String VALUE_AT_AIRPORT                    = "4";

    public static final String VALUE_AT_ADDRESS                    = "5";

    public static final String VALUE_AT_OTHER                      = "6";

    // the following two from search request address type n.n. 2.2.1.6
    public static final String VALUE_AT_CURRENT_LOCATION           = "7";

    public static final String VALUE_AT_ALONG_ROUTE                = "8";
    
    /**
     * Address source types n.n. 2.2.1.5
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_GENERIC_ADDRESS_SOURCE
     */
    public static final String VALUE_AS_RECENT_PLACES              = "1";

    public static final String VALUE_AS_RESUME_TRIP                = "2";

    public static final String VALUE_AS_FAVORITES                  = "3";

    public static final String VALUE_AS_RECEIVED_ADDRESS           = "4";

    public static final String VALUE_AS_SEARCH_POI                 = "5";

    public static final String VALUE_AS_MAP                        = "6";

    public static final String VALUE_AS_EMAIL                      = "7";

    public static final String VALUE_AS_CALENDAR                   = "8";

    public static final String VALUE_AS_CONTACTS                   = "9";

    public static final String VALUE_AS_API                        = "10";

    public static final String VALUE_AS_OTHER                      = "11";
    
    /**
     * Geo fence distance
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_ARRIVAL_DISTANCE
     */
    public static final String VALUE_DISTANCE_50           = "50";
    
    public static final String  VALUE_DISTANCE_100           = "100";
    
    public static final String VALUE_DISTANCE_150           = "150";
    
    public static final String VALUE_DISTANCE_200           = "200";
    
    public static final String VALUE_DISTANCE_250           = "250";
    
    public static final String VALUE_DISTANCE_300          = "300";
    
    public static final String VALUE_DISTANCE_350           = "350" ;
    
    public static final String VALUE_DISTANCE_400           = "400";
    
    public static final String VALUE_DISTANCE_500           = "500";
    
    public static final String VALUE_DISTANCE_1609          = "1609";
    
    /**
     * POI type values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.18
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_PAGE_NAME
     */
    public static final String VALUE_POI_PAGE_NAME_LIST            = "0";
    
    public static final String VALUE_POI_PAGE_NAME_DETAIL          = "1";
    
    public static final String VALUE_POI_PAGE_NAME_MAP             = "3";
    
    public static final String VALUE_POI_PAGE_NAME_COUPON          = "4";
    
    public static final String VALUE_POI_PAGE_NAME_MENU            = "5";
    
    /**
     * POI type values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.18
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_POI_SORTING
     */
    public static final String VALUE_POI_SORT_BY_DISTANCE          = "distance";

    public static final String VALUE_POI_SORT_BY_RATING            = "rating";

    public static final String VALUE_POI_SORT_BY_POPULAR           = "popularity";

    public static final String VALUE_POI_SORT_BY_RELEVANCE         = "relevance";

    public static final String VALUE_POI_SORT_BY_GASPRICE          = "gasbyprice";
    
    /**
     * POI type values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.18
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_SEARCH_TYPE
     */
    public static final String VALUE_POI_SEARCH_TYPE_KO            = "1";
    
    public static final String VALUE_POI_SEARCH_TYPE_CO            = "3";
    
    public static final String VALUE_POI_SEARCH_TYPE_KCS           = "4";
    
    public static final String VALUE_POI_SEARCH_TYPE_TOHER         = "5";
    
    
    /**
     * POI type values from Log Parsing ID Specification.doc.doc n.n. 11.8
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_DSR_INITIATED_FLAG
     */
    public static final String VALUE_DSR_INITIATED_FLAG_FALSE      = "0";
    
    public static final String VALUE_DSR_INITIATED_FLAG_TRUE       = "1";
    
    
    /**
     * POI type values from Log Parsing ID Specification.doc.doc n.n. 11.8
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_RESULT_TYPE
     */
    public static final String VALUE_POI_SEARCH_TYPE_DYM             = "DYM";
    
    public static final String VALUE_POI_SEARCH_TYPE_POI             = "POI";
    
    public static final String VALUE_POI_SEARCH_TYPE_ADDR            = "ADDR";
    
    /**
     * POI type values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.18
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_POI_TYPE
     */
    public static final String VALUE_POI_TYPE_SPOSORED             = "1";

    public static final String VALUE_POI_TYPE_NORMAL               = "2";

    public static final String VALUE_POI_TYPE_WITH_ADD             = "3";

    public static final String VALUE_POI_TYPE_UPSELL               = "4";

    public static final String VALUE_POI_TYPE_OTHER                = "5";
    
    
    /** Whenever the sort action took place. see 11.8 in []*/

    /**
     * STARTED BY values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.2
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_STARTED_BY
     */
    public static final String VALUE_BY_CONVENIENCE_KEY            = "1";

    public static final String VALUE_BY_CALENDAR                   = "2";

    public static final String VALUE_BY_EMAIL                      = "3";
    
    public static final String VALUE_BY_COMMUTE_ALERT              = "4";

    public static final String VALUE_BY_ADDRESS_BOOK               = "5";

    public static final String VALUE_BY_SHARE_ADDRESS              = "6";

    public static final String VALUE_BY_MANUAL                     = "7";

    public static final String VALUE_BY_OTHER                      = "8";

    public static final String VALUE_BY_MAPS_SHORTCUT              = "13";
    
    public static final String VALUE_BY_DRIVE_SHORTCUT             = "14";
    
    public static final String VALUE_BY_PLACES_SHORTCUT            = "15";
    
    public static final String VALUE_BY_MAPS_MAITAI              = "16";
    
    public static final String VALUE_BY_DRIVE_MAITAI             = "17";
    
    public static final String VALUE_BY_DIRECTION_MAITAI            = "18";
    
    
    /**
     * Start entry point values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.2
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_STARTUP_ENTRY_POINT
     */
    public static final String VALUE_ENTRY_POINT_DRIVE_TO          = "1";

    public static final String VALUE_ENTRY_POINT_SEARCH            = "2";

    public static final String VALUE_ENTRY_POINT_SHARE_ADDR        = "3";

    public static final String VALUE_ENTRY_POINT_MAP_IT            = "4";

    public static final String VALUE_ENTRY_POINT_HOME              = "5";

    public static final String VALUE_ENTRY_POINT_TRAFFIC_SUMMARY   = "6";

    public static final String VALUE_ENTRY_POINT_OTHER             = "7";

    public static final String VALUE_ENTRY_POINT_GET_DIRECTIONS    = "8";

    /**
     * Start entry point values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.2
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_STARTED_FROM
     */
    public static final String VALUE_FROM_FOREGROUND               = "0";

    public static final String VALUE_FROM_BACKGROUND               = "1";
    
    /**
     * Start entry point values from MCD_TN_6 0_Logging v1x.doc n.n. 2.2.1.4
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_APP_SESSION_EXIT_PAGE
     */
    public static final String VALUE_EXIT_PAGE_HOME                = "1";
    
    public static final String VALUE_EXIT_PAGE_NAV                 = "2";
    
    /**
     * Feedback use case attribute n.n. 2.2.1.7
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_FEEDBACK_USE_CASE
     */
    public static final String VALUE_FEEDBACK_USE_CASE_AC          = "1";

    public static final String VALUE_FEEDBACK_USE_CASE_SEARCH_FORM = "2";
    
    public static final String VALUE_FEEDBACK_USE_CASE_POI_RESULTS = "3";
    
    public static final String VALUE_FEEDBACK_USE_CASE_POI_DETAIL  = "4";
    
    public static final String VALUE_FEEDBACK_USE_CASE_POI_MENU    = "5";
    
    public static final String VALUE_FEEDBACK_USE_CASE_POI_COUPON  = "6";

    public static final String VALUE_FEEDBACK_USE_CASE_MAP         = "7";

    public static final String VALUE_FEEDBACK_USE_CASE_FOLLOW_ME   = "8";

    public static final String VALUE_FEEDBACK_USE_CASE_NAV_MAIN    = "9";
    
    public static final String VALUE_FEEDBACK_USE_CASE_TRA_SUMMARY = "10";
    
    public static final String VALUE_FEEDBACK_USE_CASE_ROU_SUMMARY = "11";

    public static final String VALUE_FEEDBACK_USE_CASE_OTHER       = "12";
    
    /**
     * Feedback invoked by attribute n.n. 2.2.1.7
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_FEEDBACK_INVOKED
     */
    public static final String VALUE_FEEDBACK_INVOKED_BY_F_KEY     = "1";

    public static final String VALUE_FEEDBACK_INVOKED_BY_END_ROUTE = "2";

    public static final String VALUE_FEEDBACK_INVOKED_BY_MENU      = "3";

    public static final String VALUE_FEEDBACK_INVOKED_BY_OTHER     = "4";
    
    /**
     * Feedback type attribute n.n. 2.2.1.7
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_FEEDBACK_TYPE
     */
    public static final String VALUE_FEEDBACK_TYPE_AUDIO           = "1";
    
    public static final String VALUE_FEEDBACK_TYPE_TEXT            = "2";


    /**
     * Address input type attribute n.n. 2.2.1.5
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_ADDRESS_INPUT
     */
    public static final String VALUE_ADDRESS_INPUT_TYPE_SPEAK_IN   = "1";

    public static final String VALUE_ADDRESS_INPUT_TYPE_TYPE_IN    = "2";
    
    public static final String VALUE_ADDRESS_INPUT_TYPE_SEARCH    = "3";
    
    /**
     * Trip end method attribute n.n. 2.2.1.3
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_TRIP_END_METHOD
     */
    public static final String VALUE_TRIP_END_METHOD_MANUAL        = "Manual";

    public static final String VALUE_TRIP_END_METHOD_AUTO          = "Auto";

    public static final String VALUE_TRIP_END_METHOD_DETOUR         = "Detour";

    public static final String VALUE_TRIP_END_METHOD_FORCEQUIT     = "ForceQuit";
    
    
    /**
     * Trip end method attribute n.n. 2.2.1.?
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_NAVIGATION_EXIT_REASON
     */
    public static final String VALUE_EXIT_REASON_MANUAL_EXIT       = "1";//Manual
    
    public static final String VALUE_EXIT_REASON_DETOUR            = "2";//Starting a new nav due to detour
    
    /**
     * Dsr use case attribute n.n. 2.2.1.8
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_DSR_USE_CASE
     */
    public static final String VALUE_DSR_SEARCH_NAME_OR_CATEGORY   = "1";

    public static final String VALUE_DSR_SEARCH_ADDRESS            = "2";

    public static final String VALUE_DSR_ADDRESS_ENTRY_DRIVE_TO    = "3";
    
    public static final String VALUE_DSR_ADDRESS_ENTRY_SEARCH      = "4";
    
    public static final String VALUE_DSR_ADDRESS_ENTRY_MAP         = "5";
    
    public static final String VALUE_DSR_ADDRESS_ENTRY_OTHER       = "6";
    
    /**
     * Search area attribute n.n. 2.2.1.6
     * 
     * @see com.telenav.log.mis.IMisLogConstants#ATTR_SEARCH_AREA
     */
    public static final String VALUE_SEARCH_AREA_CURRENT_LOCATION  = "1";

    public static final String VALUE_SEARCH_AREA_ADDRESS           = "2";

    public static final String VALUE_SEARCH_AREA_ALONG_ROUTE       = "3";
    
    
    // POI events have priority P1, so all of them in default list
    public static final int[] defaultEvents =
    { TYPE_SESSION_STARTUP, TYPE_STARTUP_INFO, TYPE_ON_BOARD_STARTUP_INFO, TYPE_APP_SESSION_SUMMARY, TYPE_ROUTE_REQUEST,
            TYPE_TRIP_SUMMARY, TYPE_HOME_SCREEN_TIME, TYPE_CLICK_STREAM, TYPE_FIRST_TIME_LOGIN, TYPE_POI_SEARCH_REQUEST,
            TYPE_POI_IMPRESSION, TYPE_POI_DETAILS, TYPE_POI_VIEW_MAP, TYPE_POI_DRIVE_TO, TYPE_POI_SHARE, TYPE_POI_CALL_TO,
            TYPE_POI_VIEW_MERCHANT, TYPE_POI_VIEW_COUPON, TYPE_POI_VIEW_MENU, TYPE_POI_MAP_ALL, TYPE_POI_REVIEW_TAB_CLICK,
            TYPE_POI_REVIEW_LINK_OUT_IMPRESSION, TYPE_POI_REVIEW_LINK_OUT_CLICK, TYPE_ARIVALl_CONFIRMATION,
            TYPE_HEAD_UNIT_CAR_CONNECT, TYPE_HEAD_UNIT_CAR_DISCONNECT, TYPE_HEAD_UNIT_CONNECTED_POI_SEARCH,
            TYPE_HEAD_UNIT_CONNECTED_DRIVE_TO };

}
