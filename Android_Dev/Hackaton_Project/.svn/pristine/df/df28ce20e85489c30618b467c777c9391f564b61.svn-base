/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ISpecialImageRes.java
 *
 */
package com.telenav.res;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public class ISpecialImageRes
{
    // =====================================below are icon's id constraint=====================================//
	//for 240 (small)
    public final static String FAMILY_TINY = "tiny";
    
	//for 320 (small)
    public final static String FAMILY_SMALL = "small";
    
    //for 480 (medium)
    public final static String FAMILY_MEDIUM = "medium";
    
    //for 640 (large)
    public final static String FAMILY_LARGE = "large";
    
    //for 800 (huge)
    public final static String FAMILY_HUGE = "huge";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
    
    //for 960 (vast)
    public final static String FAMILY_VAST = "vast";
    
    //vast
    public final static int VAST_WIDTH = 540;
    public final static int VAST_HEIGHT = 960;
    
    //medium
    public final static int MEDIUM_WIDTH = 320;
    public final static int MEDIUM_HEIGHT = 480;
    
    //--------------------------------------------------------
    public static boolean isNeedStretch = false;
    
    public static int numerator;
    
    public static int denominator;
    
    //=================================variable==================================================================//
    private static String iconFamily;
    
    private static String openglFamily; //the opengl definition is diffrent from Android definition
    
    //====================== below are special icon id ============================
//    public final static String CLOSE_ICON_BLUE = "close_blur.png";
//    public final static String CLOSE_ICON_FOCUS = "close_focus.png";
    
    public final static String MIC_ICON_LANDSCAPE_UNFOCUS = "huge_mic_icon_landscape_unfocused.png";
    public final static String MIC_ICON_UNFOCUS = "huge_mic_icon_unfocused.png";
    
    public final static String WAVE_ICON_LOUD_LEFT_UNFOCUS = "huge_mic_wave_icon_loud_unfocused_left.png";
    public final static String WAVE_ICON_LOUD_LEFT_FOCUS = "huge_mic_wave_icon_loud_focused_left.png";
    public final static String WAVE_ICON_LOUD_RIGHT_UNFOCUS = "huge_mic_wave_icon_loud_unfocused_right.png";
    public final static String WAVE_ICON_LOUD_RIGHT_FOCUS = "huge_mic_wave_icon_loud_focused_right.png";
    public final static String WAVE_ICON_LOW_LEFT_UNFOCUS = "huge_mic_wave_icon_low_unfocused_left.png";
    public final static String WAVE_ICON_LOW_LEFT_FOCUS = "huge_mic_wave_icon_low_focused_left.png";
    public final static String WAVE_ICON_LOW_RIGHT_UNFOCUS = "huge_mic_wave_icon_low_unfocused_right.png";
    public final static String WAVE_ICON_LOW_RIGHT_FOCUS = "huge_mic_wave_icon_low_focused_right.png";
    public final static String WAVE_ICON_NORMAL_LEFT_UNFOCUS = "huge_mic_wave_icon_normal_unfocused_left.png";
    public final static String WAVE_ICON_NORMAL_LEFT_FOCUS = "huge_mic_wave_icon_normal_focused_left.png";
    public final static String WAVE_ICON_NORMAL_RIGHT_UNFOCUS = "huge_mic_wave_icon_normal_unfocused_right.png";
    public final static String WAVE_ICON_NORMAL_RIGHT_FOCUS = "huge_mic_wave_icon_normal_focused_right.png";
    
    public final static String AC_ICON_FIND_UNFOCUSED = "searchbox_icon_search_unfocused.png";
    public final static String AC_ICON_FIND_FOCUSED = "searchbox_icon_search_focused.png";
    public final static String AC_ICON_BACKSPACE = "searchbox_icon_clear_unfocused.png";
    public final static String FAV_ICON_ADD_FAVORITE_UNFOCUS = "buttonicon_addfavorate_unfocused.png";
    public final static String FAV_ICON_ADD_CATEGORY_UNFOCUS = "buttonicon_addcategory_unfocused.png";
    public final static String FAV_ICON_SYNC_UNFOCUS = "buttonicon_refresh_unfocused.png";
//    public final static String FAV_ICON_LIST_CATEGORY_ICON = "icon_ac_favorites.png";
    public final static String FAV_ICON_EDIT_UNFOCUS = "buttonicon_edit_unfocused.png";
//    public final static String IMAGE_TEST_GALLERY = "test_gallery_focus.png";
//    public final static String FAV_ICON_EDIT_FAVORITE_SIGN = "favority_added.png";
    public final static String FAV_ICON_EDIT_FAVORITE_PIN = "favorite_pin_icon_unfocused.png";
    public final static String CHECK_BOX_ICON_ON = "check_box_focused.png";
    public final static String CHECK_BOX_ICON_OFF = "check_box_unfocused.png";
    public final static String CHECK_BOX_ICON_DISABLE = "check_box_disabled.png";
    
    public final static String NAV_ZOOM_OUT_ICON_UNFOCUSED = "nav_zoom_out_icon_unfocused.png";
    public final static String NAV_ZOOM_OUT_ICON_FOCUSED = "nav_zoom_out_icon_focused.png";
    public final static String NAV_ZOOM_IN_ICON_UNFOCUSED = "nav_zoom_in_icon_unfocused.png";
    public final static String NAV_ZOOM_IN_ICON_FOCUSED = "nav_zoom_in_icon_focused.png";
    public final static String NAV_ZOOM_OUT_ICON_DISABLE = "nav_zoom_out_icon_disabled.png";
    public final static String NAV_ZOOM_IN_ICON_DISABLE = "nav_zoom_in_icon_disabled.png";
    
    public final static String ZOOM_IN_DAY_ICON_UNFOCUS = "map_zoom_in_icon_unfocused.png";
    public final static String ZOOM_IN_NIGHT_ICON_UNFOCUS = "map_zoom_in_icon_night_unfocused.png";
    public final static String ZOOM_IN_ICON_FOCUS = "map_zoom_in_icon_focused.png";
    public final static String ZOOM_IN_DAY_ICON_DISABLE = "map_zoom_in_icon_disabled.png";
    public final static String ZOOM_IN_NIGHT_ICON_DISABLE = "map_zoom_in_icon_night_disabled.png";
    
    public final static String ZOOM_OUT_DAY_ICON_UNFOCUS = "map_zoom_out_icon_unfocused.png";
    public final static String ZOOM_OUT_NIGHT_ICON_UNFOCUS = "map_zoom_out_icon_night_unfocused.png";
    public final static String ZOOM_OUT_ICON_FOCUS = "map_zoom_out_icon_focused.png";
    public final static String ZOOM_OUT_DAY_ICON_DISABLE = "map_zoom_out_icon_disabled.png";
    public final static String ZOOM_OUT_NIGHT_ICON_DISABLE = "map_zoom_out_icon_night_disabled.png";
    
    
//    public final static String MY_LOCATION_ICON_OFF = "my_location_icon_unfocused.png";
//    public final static String MY_LOCATION_ICON_ON = "my_location_icon_focused.png";
//    public final static int MY_LOCATION_OUT_ICON_DISABLE = 128;
    
    public final static String POI_BUBBLE_DOWN_ARROW = "poi_bubble_bg_unfocused_arrow.png";
    
    public final static String MAP_POI = "poi_small_icon_unfocused.png";
    public final static String SELECTED_MAP_POI = "poi_small_icon_focused.png";
    public final static String IMG_MAP_AD_POI = "poi_small_icon_ad_unfocused.png";
    
    
//    public final static String BIG_MAP_POI  = "poi_big_icon_unfocused.png";
//    public final static String BIG_SELECTED_MAP_POI = "poi_big_icon_focused.png";
    
//    public final static String BIG_MAP_POI_WITH_AD_UNFOCUSED  = "poi_big_icon_unfocused.png";
//    public final static String BIG_SELECTED_MAP_POI_WITH_AD_UNFOCUSED = "poi_big_icon_ad_unfocused.png";
    
    public final static String MAP_ADDRESS = "poi_small_icon_for_address_unfocused.png";
    public final static String SELECTED_MAP_ADDRESS = "poi_small_icon_for_address_focused.png";
    
//    public final static String BIG_MAP_ADDRESS = "poi_big_icon_for_address_unfocused.png";
//    public final static String BIG_SELECTED_MAP_ADDRESS = "poi_big_icon_for_address_focused.png";
    
    public final static String PREV_POI_UNFOCUS = "poi_bubble_previous_button_unfocused.png";
    public final static String PREV_POI_FOCUSED = "poi_bubble_previous_button_focused.png";
    
    public final static String NEXT_POI_UNFOCUS = "poi_bubble_next_button_unfocused.png";
    public final static String NEXT_POI_FOCUSED = "poi_bubble_next_button_focused.png";
    
    public final static String BOTTOM_BAR_DASHBOARD_UNFOCUS = "bottom_bar_icon_dash_unfocused.png";
    public final static String BOTTOM_BAR_DASHBOARD_FOCUS = "bottom_bar_icon_dash_focused.png";
    
    public final static String BOTTOM_BAR_DRIVE_UNFOCUS = "bottom_bar_icon_driveto_unfocused.png";
    public final static String BOTTOM_BAR_DRIVE_FOCUS = "bottom_bar_icon_driveto_focused.png";
    
    public final static String BOTTOM_BAR_EXTRA_UNFOCUS = "bottom_bar_icon_extras_unfocused.png";
    public final static String BOTTOM_BAR_EXTRA_FOCUS = "bottom_bar_icon_extras_focused.png";
    
    public final static String BOTTOM_BAR_MAP_UNFOCUS = "bottom_bar_icon_maps_unfocused.png";
    public final static String BOTTOM_BAR_MAP_FOCUS = "bottom_bar_icon_maps_focused.png";
    
    public final static String BOTTOM_BAR_LIST_UNFOCUS = "bottom_bar_icon_route_unfocused.png";
    public final static String BOTTOM_BAR_LIST_FOCUS = "bottom_bar_icon_route_focused.png";
    
    public final static String BOTTOM_MIC_ICON_UNFOCUS = "mic_icon_unfocused.png";
    public final static String BOTTOM_MIC_OFFLINE_ICON_UNFOCUS = "mic_offline_icon_unfocused.png";
    
//    public final static String SEARCHBOX_CLEAR_UNFOCUS = "searchbox_icon_clear_unfocused.png";
    public final static String SEARCHBOX_POILIST_UNFOCUS = "searchbox_icon_poilist_unfocused.png";
//    public final static String SEARCHBOX_SEARCH_UNFOCUS = "searchbox_icon_search_unfocused.png";
    
//    public final static String ADD_FAV_UNFOCUS = "buttonicon_addfavorate_unfocused.png";
//    public final static String ADD_CAT_UNFOCUS = "buttonicon_addcategory_unfocused.png";
//    public final static String EDIT_UNFOCUS = "buttonicon_edit_unfocused.png";
    
    public final static String POI_BUBBLE_DETAIL_ARROW_UNFOCUS = "poi_bubble_details_icon_unfocused.png";
    public final static String POI_BUBBLE_DETAIL_ARROW_FOCUSED = "poi_bubble_details_icon_focused.png";
    public final static String POI_BUBBLE_BUTTON_DRIVETO_UNFOCUS = "poi_bubble_driveto_icon_unfocused.png";
    public final static String POI_BUBBLE_BUTTON_DRIVETO_FOCUSED = "poi_bubble_driveto_icon_focused.png";
    public final static String POI_BUBBLE_BUTTON_CALL_UNFOCUS = "poi_bubble_call_icon_unfocused.png";
    public final static String POI_BUBBLE_BUTTON_CALL_FOCUSED = "poi_bubble_call_icon_focused.png";
    public final static String POI_BUBBLE_BUTTON_CALL_DISABLED = "poi_bubble_call_icon_disabled.png";
    public final static String POI_BUBBLE_BUTTON_SHARE_UNFOCUS = "poi_bubble_share_icon_unfocused.png";
    public final static String POI_BUBBLE_BUTTON_SHARE_FOCUSED = "poi_bubble_share_icon_focused.png";
    public final static String POI_BUBBLE_BUTTON_SHARE_DISABLED = "poi_bubble_share_icon_disabled.png";
    
    
    public final static String POI_BUBBLE_NEARBY_FOCUSED = "poi_bubble_nearby_icon_focused.png";
    public final static String POI_BUBBLE_NEARBY_UNFOCUSED = "poi_bubble_nearby_icon_unfocused.png";
    public final static String POI_BUBBLE_NEARBY_DISABLED = "poi_bubble_nearby_icon_disabled.png";
    public final static String POI_BUBBLE_SAVE_ICON_FOCUSED = "poi_bubble_save_icon_focused.png";
    public final static String POI_BUBBLE_SAVE_ICON_UNFOCUSED = "poi_bubble_save_icon_unfocused.png";
    public final static String POI_BUBBLE_SAVE_ADDED_ICON_UNFOCUSED = "poi_bubble_save_added_icon_unfocused.png";
    
    
    public final static String AC_ICON_HOME_UNFOCUS = "ac_main_home_icon_unfocused.png";
    public final static String AC_ICON_RECNET_UNFOCUS = "ac_main_recent_icon_unfocused.png";
    public final static String AC_ICON_FAVORITES_UNFOCUS = "ac_main_favorites_icon_unfocused.png";
    public final static String AC_ICON_CONTACTS_UNFOCUS = "ac_main_contacts_icon_unfocused.png";
    public final static String AC_ICON_BUSINESSES_UNFOCUS = "ac_main_businesses_icon_unfocused.png";
    public final static String AC_ICON_AIRPORTS_UNFOCUS = "ac_main_airports_icon_unfocused.png";
    public final static String AC_ICON_ARROW_UNFOCUS = "ac_main_arrow_icon_unfocused.png";
    public final static String AC_ICON_HOME_FOCUSED = "ac_main_home_icon_focused.png";
    public final static String AC_ICON_RECNET_FOCUSED = "ac_main_recent_icon_focused.png";
    public final static String AC_ICON_FAVORITES_FOCUSED = "ac_main_favorites_icon_focused.png";
    public final static String AC_ICON_CONTACTS_FOCUSED = "ac_main_contacts_icon_focused.png";
    public final static String AC_ICON_BUSINESSES_FOCUSED = "ac_main_businesses_icon_focused.png";
    public final static String AC_ICON_AIRPORTS_FOCUSED = "ac_main_airports_icon_focused.png";
    public final static String AC_ICON_ARROW_FOCUSED = "ac_main_arrow_icon_focused.png";
    public final static String LIST_ICON_ARROW_FOCUSED = "list_arrow_icon_focused.png";
    public final static String LIST_ICON_ARROW_UNFOCUSED = "list_arrow_icon_unfocused.png";
    public final static String LIST_FAVORITE_ICON_UNFOCUS = "list_favorites_icon_unfocused.png";
    public final static String AC_ICON_ADDRESS_FOCUSED = "ac_main_address_icon_focused.png";
    public final static String AC_ICON_ADDRESS_UNFOCUSED = "ac_main_address_icon_unfocused.png";
    public final static String AC_ICON_PLACES_FOCUSED = "ac_main_places_icon_focused.png";
    public final static String AC_ICON_PLACES_UNFOCUSED = "ac_main_places_icon_unfocused.png";
    public final static String AC_ICON_CURRENT_LOCATION_FOCUSED = "ac_main_current_location_icon_focused.png";
    public final static String AC_ICON_CURRENT_LOCATION_UNFOCUSED = "ac_main_current_location_icon_unfocused.png";
    public final static String AC_MAIN_REGION_UNFOCUSED = "ac_main_region_icon_unfocused.png";
    
    public final static String POI_QUICK_SEARCH_PARKING_UNFOCUSED_ICON = "search_panel_parking_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_PARKING_FOCUSED_ICON = "search_panel_parking_icon_focused.png";
    public final static String POI_SEARCH_RESULT_INDEX_ICON    = "search_results_number_icon_unfocused.png";

    public final static String POI_REVIEW_BIGSTAR_FULL = "full_big_star_icon_unfocused.png";
    public final static String POI_REVIEW_BIGSTAR_VACANCY = "vacancy_big_star_icon_unfocused.png";
    public final static String POI_REVIEW_MIDSTAR_FULL = "full_medium_star_icon_unfocused.png";
    public final static String POI_REVIEW_MIDSTAR_VACANCY = "vacancy_medium_star_icon_unfocused.png";
    public final static String POI_REVIEW_SMALLSTAR_FULL = "full_small_star_icon_unfocused.png";
    public final static String POI_REVIEW_SMALLSTAR_VACANCY = "vacancy_small_star_icon_unfocused.png";
    
    public final static String FAV_ICON_ADD_CATEGORY_FOCUSED = "buttonicon_addcategory_focused.png";
    public final static String FAV_ICON_ADD_FAVORITE_FOCUSED = "buttonicon_addfavorate_focused.png";
    public final static String FAV_ICON_SYNC_FOCUSED = "buttonicon_refresh_focused.png";
    public final static String FAV_ICON_EDIT_FOCUSED = "buttonicon_edit_focused.png";
    
    public final static String LIST_FAVORITE_FOLDER_UNFOCUS = "list_favorites_folder_icon_unfocused.png";
//    public final static String LABEL_UPSELL = "att_nav_branding_unfocused.png";
    public final static String LIST_HISTORY_ICON_FOCUSED = "list_history_icon_focused.png";
    public final static String LIST_HISTORY_ICON_UNFOCUSED = "list_history_icon_unfocused.png";
    public final static String LIST_RECEIVED_ICON_UNFOCUSED = "list_received_icon_unfocused.png";
    public final static String LIST_RECEIVED_ICON_FOCUSED = "list_received_icon_focused.png";
    public final static String LIST_HOME_ICON_UNFOCUSED = "list_home_icon_unfocused.png";
    public final static String LIST_WORK_ICON_UNFOCUSED = "list_work_icon_unfocused.png";
    public final static String LIST_CURRENT_ICON_UNFOCUSED = "list_current_icon_unfocused.png";
    
    //for big turn icon.
    public final static String TURN_ICON_BIG_UTURN_RIGHT_UNFOCUSED = "turn_icon_big_uturn_right_unfocused.png"; 
    public final static String TURN_ICON_BIG_UTURN_LEFT_UNFOCUSED = "turn_icon_big_uturn_left_unfocused.png";
    public final static String TURN_ICON_BIG_START_UNFOCUSED = "turn_icon_big_start_unfocused.png";
    public final static String TURN_ICON_BIG_SLIDE_RIGHT_UNFOCUSED = "turn_icon_big_slide_right_unfocused.png";
    public final static String TURN_ICON_BIG_SLIDE_LEFT_UNFOCUSED = "turn_icon_big_slide_left_unfocused.png";
    public final static String TURN_ICON_BIG_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED = "turn_icon_big_roundabout_entertoright_unfocused.png";
    public final static String TURN_ICON_BIG_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED = "turn_icon_big_roundabout_entertoleft_unfocused.png";
    public final static String TURN_ICON_BIG_RIGHT_UNFOCUSED = "turn_icon_big_right_unfocused.png";
    public final static String TURN_ICON_BIG_ON_RIGHT_UNFOCUSED = "turn_icon_big_on_right_unfocused.png";
    public final static String TURN_ICON_BIG_ON_LEFT_UNFOCUSED = "turn_icon_big_on_left_unfocused.png";
    public final static String TURN_ICON_BIG_MERGE_RIGHT_UNFOCUSED = "turn_icon_big_merge_right_unfocused.png";
    public final static String TURN_ICON_BIG_MERGE_LEFT_UNFOCUSED = "turn_icon_big_merge_left_unfocused.png";
    public final static String TURN_ICON_BIG_LEFT_UNFOCUSED = "turn_icon_big_left_unfocused.png";
    public final static String TURN_ICON_BIG_HARD_RIGHT_UNFOCUSED = "turn_icon_big_hard_right_unfocused.png";
    public final static String TURN_ICON_BIG_HARD_LEFT_UNFOCUSED = "turn_icon_big_hard_left_unfocused.png";
    public final static String TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED = "turn_icon_big_exit_roundabout_topright_unfocused.png";
    public final static String TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED = "turn_icon_big_exit_roundabout_topleft_unfocused.png";
    public final static String TURN_ICON_BIG_EXIT_RIGHT_UNFOCUSED = "turn_icon_big_exit_right_unfocused.png";
    public final static String TURN_ICON_BIG_EXIT_LEFT_UNFOCUSED = "turn_icon_big_exit_left_unfocused.png";
    public final static String TURN_ICON_BIG_CONTINUE_UNFOCUSED = "turn_icon_big_continue_unfocused.png";
    public final static String TURN_ICON_BIG_BEAR_RIGHT_UNFOCUSED = "turn_icon_big_bear_right_unfocused.png";
    public final static String TURN_ICON_BIG_BEAR_LEFT_UNFOCUSED = "turn_icon_big_bear_left_unfocused.png";
    public final static String TURN_ICON_BIG_AHEAD_UNFOCUSED = "turn_icon_big_ahead_unfocused.png";
    
    //for small turn icon.
    public final static String TURN_ICON_SMALL_AHEAD_UNFOCUSED = "turn_icon_small_ahead_unfocused.png";
    public final static String TURN_ICON_SMALL_BEAR_LEFT_UNFOCUSED = "turn_icon_small_bear_left_unfocused.png";
    public final static String TURN_ICON_SMALL_BEAR_RIGHT_UNFOCUSED = "turn_icon_small_bear_right_unfocused.png";
    public final static String TURN_ICON_SMALL_CONTINUE_UNFOCUSED = "turn_icon_small_continue_unfocused.png";
    public final static String TURN_ICON_SMALL_EXIT_LEFT_UNFOCUSED = "turn_icon_small_exit_left_unfocused.png";
    public final static String TURN_ICON_SMALL_EXIT_RIGHT_UNFOCUSED = "turn_icon_small_exit_right_unfocused.png";
    public final static String TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED = "turn_icon_small_exit_roundabout_topleft_unfocused.png";
    public final static String TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED = "turn_icon_small_exit_roundabout_topright_unfocused.png";
    public final static String TURN_ICON_SMALL_HARD_LEFT_UNFOCUSED = "turn_icon_small_hard_left_unfocused.png";
    public final static String TURN_ICON_SMALL_HARD_RIGHT_UNFOCUSED = "turn_icon_small_hard_right_unfocused.png";
    public final static String TURN_ICON_SMALL_LEFT_UNFOCUSED = "turn_icon_small_left_unfocused.png";
    public final static String TURN_ICON_SMALL_MERGE_LEFT_UNFOCUSED = "turn_icon_small_merge_left_unfocused.png";
    public final static String TURN_ICON_SMALL_MERGE_RIGHT_UNFOCUSED = "turn_icon_small_merge_right_unfocused.png";
    public final static String TURN_ICON_SMALL_ON_LEFT_UNFOCUSED = "turn_icon_small_on_left_unfocused.png";
    public final static String TURN_ICON_SMALL_ON_RIGHT_UNFOCUSED = "turn_icon_small_on_right_unfocused.png";
    public final static String TURN_ICON_SMALL_RIGHT_UNFOCUSED = "turn_icon_small_right_unfocused.png";
    public final static String TURN_ICON_SMALL_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED = "turn_icon_small_roundabout_entertoleft_unfocused.png";
    public final static String TURN_ICON_SMALL_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED = "turn_icon_small_roundabout_entertoright_unfocused.png";
    public final static String TURN_ICON_SMALL_SLIDE_LEFT_UNFOCUSED = "turn_icon_small_slide_left_unfocused.png";
    public final static String TURN_ICON_SMALL_SLIDE_RIGHT_UNFOCUSED = "turn_icon_small_slide_right_unfocused.png";
    public final static String TURN_ICON_SMALL_START_UNFOCUSED = "turn_icon_small_start_unfocused.png";
    public final static String TURN_ICON_SMALL_UTURN_LEFT_UNFOCUSED = "turn_icon_small_uturn_left_unfocused.png";
    public final static String TURN_ICON_SMALL_UTURN_RIGHT_UNFOCUSED = "turn_icon_small_uturn_right_unfocused.png";
    
    //for small turn icon.
    public final static String TIGHT_TURN_ICON_AHEAD_UNFOCUSED = "tight_turn_icon_ahead_unfocused.png";
    public final static String TIGHT_TURN_ICON_BEAR_LEFT_UNFOCUSED = "tight_turn_icon_bear_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_BEAR_RIGHT_UNFOCUSED = "tight_turn_icon_bear_right_unfocused.png";
    public final static String TIGHT_TURN_ICON_CONTINUE_UNFOCUSED = "tight_turn_icon_continue_unfocused.png";
    public final static String TIGHT_TURN_ICON_EXIT_LEFT_UNFOCUSED = "tight_turn_icon_exit_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_EXIT_RIGHT_UNFOCUSED = "tight_turn_icon_exit_right_unfocused.png";
    public final static String TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED = "tight_turn_icon_exit_roundabout_topleft_unfocused.png";
    public final static String TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED = "tight_turn_icon_exit_roundabout_topright_unfocused.png";
    public final static String TIGHT_TURN_ICON_HARD_LEFT_UNFOCUSED = "tight_turn_icon_hard_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_HARD_RIGHT_UNFOCUSED = "tight_turn_icon_hard_right_unfocused.png";
    public final static String TIGHT_TURN_ICON_LEFT_UNFOCUSED = "tight_turn_icon_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_MERGE_LEFT_UNFOCUSED = "tight_turn_icon_merge_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_MERGE_RIGHT_UNFOCUSED = "tight_turn_icon_merge_right_unfocused.png";
    public final static String TIGHT_TURN_ICON_ON_LEFT_UNFOCUSED = "tight_turn_icon_on_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_ON_RIGHT_UNFOCUSED = "tight_turn_icon_on_right_unfocused.png";
    public final static String TIGHT_TURN_ICON_RIGHT_UNFOCUSED = "tight_turn_icon_right_unfocused.png";
    public final static String TIGHT_TURN_ICON_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED = "tight_turn_icon_roundabout_entertoleft_unfocused.png";
    public final static String TIGHT_TURN_ICON_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED = "tight_turn_icon_roundabout_entertoright_unfocused.png";
    public final static String TIGHT_TURN_ICON_SLIDE_LEFT_UNFOCUSED = "tight_turn_icon_slide_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_SLIDE_RIGHT_UNFOCUSED = "tight_turn_icon_slide_right_unfocused.png";
    public final static String TIGHT_TURN_ICON_START_UNFOCUSED = "tight_turn_icon_start_unfocused.png";
    public final static String TIGHT_TURN_ICON_UTURN_LEFT_UNFOCUSED = "tight_turn_icon_uturn_left_unfocused.png";
    public final static String TIGHT_TURN_ICON_UTURN_RIGHT_UNFOCUSED = "tight_turn_icon_uturn_right_unfocused.png";
    
    //CN specified turn icons
    public final static String TURN_ICON_BIG_ENTER_FERRY_UNFOCUSED      = "turn_icon_big_enter_ferry_unfocused.png";
    public final static String TURN_ICON_BIG_EXIT_FERRY_UNFOCUSED       = "turn_icon_big_exit_ferry_unfocused.png";
    public final static String TURN_ICON_BIG_STAY_MIDDLE_UNFOCUSED      = "turn_icon_big_stay_middle_unfocused.png";
    public final static String TURN_ICON_BIG_VIA_POINT_AHEAD_UNFOCUSED  = "turn_icon_big_via_point_ahead_unfocused.png";
    public final static String TURN_ICON_BIG_VIA_POINT_LEFT_UNFOCUSED   = "turn_icon_big_via_point_left_unfocused.png";
    public final static String TURN_ICON_BIG_VIA_POINT_RIGHT_UNFOCUSED  = "turn_icon_big_via_point_right_unfocused.png";
    public final static String TURN_ICON_SMALL_ENTER_FERRY_UNFOCUSED    = "turn_icon_small_enter_ferry_unfocused.png";
    public final static String TURN_ICON_SMALL_EXIT_FERRY_UNFOCUSED     = "turn_icon_small_exit_ferry_unfocused.png";
    public final static String TURN_ICON_SMALL_STAY_MIDDLE_UNFOCUSED    = "turn_icon_small_stay_middle_unfocused.png";
    public final static String TURN_ICON_SMALL_VIA_POINT_AHEAD_UNFOCUSED= "turn_icon_small_via_point_ahead_unfocused.png";
    public final static String TURN_ICON_SMALL_VIA_POINT_LEFT_UNFOCUSED = "turn_icon_small_via_point_left_unfocused.png";
    public final static String TURN_ICON_SMALL_VIA_POINT_RIGHT_UNFOCUSED= "turn_icon_small_via_point_right_unfocused.png";
    
    public final static String MENU_ICON_UNFOCUSED = "search_results_menu_icon_unfocused.png";
    public final static String DEAL_ICON_UNFOCUSED = "search_results_deals_icon_unfocused.png";
    
    
//    public final static String GL_ROAD_TEXTURE_UNFOCUSED = "gl_road_texture_unfocused.png";
//    public final static String GL_TRAFFIC_TEXTURE_UNFOCUSED = "gl_traffic_texture_unfocused.png";
//    public final static String GL_CAR_TEXTURE_UNFOCUSED = "gl_car_texture_unfocused.png";
//    public final static String GL_ROAD_SIGN_TEXTURE_UNFOCUSED = "gl_road_sign_unfocused.png";
//    public final static String GL_MAP_BAR_TEXTURE_UNFOCUSED = "gl_map_bar_unfocused.png";
//    public final static String GL_ARIAL_BLACK_UNFOCUSED = "gl_arial_black_unfocused.png";
//    public final static String GL_SKY_DOME_UNFOCUSED = "gl_skydome_unfocused.png";
    
    public final static String ORIGIN_ICON_UNFOCUSED = "origin_icon_unfocused.png";
    public final static String DESTINATION_ICON_UNFOCUSED = "destination_icon_unfocused.png";
    
    public final static String ORIGIN_ICON_SMALL_UNFOCUSED = "origin_small_icon_unfocused.png";
    public final static String DESTINATION_ICON_SMALL_UNFOCUSED = "destination_small_icon_unfocused.png";
    
    public final static String MAP_CURRENT_LOCATION_UNFOCUSED = "map_current_location_icon_unfocused.png";
    public final static String MAP_CURRENT_LOCATION_NIGHT_UNFOCUSED = "map_current_location_icon_night_unfocused.png";
    public final static String MAP_CURRENT_LOCATION_FOCUSED = "map_current_location_icon_focused.png";
    
    public final static String MAP_LAYER_ICON_FOCUSED  = "map_map_layer_icon_focused.png";
    public final static String MAP_LAYER_ICON_UNFOCUSED = "map_map_layer_icon_unfocused.png";
    public final static String MAP_LAYER_ICON_DISABLE  = "map_map_layer_icon_disabled.png";
    public final static String MAP_LAYER_ICON_NIGHT_UNFOCUSED = "map_map_layer_icon_night_unfocused.png";
    
//    public final static String SPLASH_GRAPHICS_UNFOCUSED_PORTRAIT = "splash_graphic_unfocused_portrait.png";
//    public final static String SPLASH_YPMOBILE_UNFOCUSED = "splash_ypmobile_unfocused.png";
//    public final static String SPLASH_NAVTEQ_UNFOCUSED = "splash_navteq_unfocused.png";
//    public final static String SPLASH_TEALALIAS_UNFOCUSED = "splash_tealalias_unfocused.png";
    public final static String PROFILE_ABOUT_SCOUT_LOGO = "profile_about_scout_logo_unfocused.png";
    
    public final static String MAP_CAMERA_ICON_FOCUSED = "map_camera_icon_focused.png";
    public final static String MAP_CAMERA_ICON_UNFOCUSED = "map_camera_icon_unfocused.png";
    public final static String MAP_CAMERA_ICON_DISABLED = "map_camera_icon_disabled.png";
    public final static String MAP_SATELLITE_ICON_FOCUSED = "map_GPS_icon_focused.png";
    public final static String MAP_SATELLITE_ICON_UNFOCUSED = "map_GPS_icon_unfocused.png";
    public final static String MAP_SATELLITE_ICON_DISABLED = "map_GPS_icon_disabled.png";
    public final static String MAP_TRAFFIC_ICON_FOCUSED = "map_traffic_icon_focused.png";
    public final static String MAP_TRAFFIC_ICON_UNFOCUSED = "map_traffic_icon_unfocused.png";
    public final static String MAP_TRAFFIC_ICON_DISABLED = "map_traffic_icon_disabled.png";
    public final static String MAP_SMALL_MAP_LAYER_ICON_FOCUSED = "map_small_map_layer_icon_focused.png";
    public final static String MAP_SMALL_MAP_LAYER_ICON_UNFOCUSED = "map_small_map_layer_icon_unfocused.png";
    public final static String SUMMARY_CURRENT_SEGMENT_UNFOCUSED = "current_segment_icon_unfocused.png";

    public final static String DROP_DOWN_COMBOBOX_INDICATOR_FOCUSED = "sort_type_arrow_unfocused.png";
    public final static String RADIO_ICON_FOCUSED = "radio_button_focused.png";
    public final static String RADIO_ICON_UNFOCUSED = "radio_button_unfocused.png";
    public final static String SPONSORED_INDEX_ICON_UNFOCUSED = "search_results_ad_icon_unfocused.png";
    
    public final static String CONTACTS_FILTER_LIST_ICON_FOCUSED = "contacts_filter_list_icon_focused.png";
    public final static String HOME_FILTER_LIST_ICON_FOCUSED = "home_filter_list_icon_focused.png";
    public final static String RECENT_FILTER_LIST_ICON_FOCUSED = "recent_filter_list_icon_focused.png";
    public final static String LIST_FAVORITE_FOLDER_FOCUSED = "list_favorites_folder_icon_focused.png";
    public final static String LIST_FAVORITE_ICON_FOCUSED = "list_favorites_icon_focused.png";
    
    
    public final static String SEARCHBOX_POILIST_FOCUS = "searchbox_icon_poilist_focused.png";
    
    public final static String LANE_ASSIST_LEFT_TURN_UNFOCUSED = "lane_assist_left_turn_unfocused.png";
    public final static String LANE_ASSIST_RIGHT_TURN_UNFOCUSED = "lane_assist_right_turn_unfocused.png";
    public final static String LANE_ASSIST_AHEAD_UNFOCUSED = "lane_assist_ahead_unfocused.png";
    public final static String LANE_ASSIST_LEFT_TURN_FOCUSED = "lane_assist_left_turn_focused.png";
    public final static String LANE_ASSIST_RIGHT_TURN_FOCUSED = "lane_assist_right_turn_focused.png";
    public final static String LANE_ASSIST_AHEAD_FOCUSED = "lane_assist_ahead_focused.png";
    
//    public final static String LANE_ASSIST_SMALL_LEFT_TURN_UNFOCUSED = "lane_assist_small_left_turn_unfocused.png";
//    public final static String LANE_ASSIST_SMALL_RIGHT_TURN_UNFOCUSED = "lane_assist_small_right_turn_unfocused.png";
//    public final static String LANE_ASSIST_SMALL_AHEAD_UNFOCUSED = "lane_assist_small_ahead_unfocused.png";
//    public final static String LANE_ASSIST_SMALL_LEFT_TURN_FOCUSED = "lane_assist_small_left_turn_focused.png";
//    public final static String LANE_ASSIST_SMALL_RIGHT_TURN_FOCUSED = "lane_assist_small_right_turn_focused.png";
//    public final static String LANE_ASSIST_SMALL_AHEAD_FOCUSED = "lane_assist_small_ahead_focused.png";
    
    public final static String TRAFFIC_COLOR_BAR = "traffic_color_bar_unfocused.png";
    public final static String TRAFFIC_COLOR_BAR_LANDSCAPE = "traffic_color_bar_landscape_unfocused.png";
    public final static String TRAFFIC_GRAY_BAR = "traffic_gray_bar_unfocused.png";
    public final static String TRAFFIC_GRAY_BAR_LANDSCAPE = "traffic_gary_bar_landscape_unfocused.png";
    public final static String TRAFFIC_SLIDER = "traffic_slider_unfocused.png";
    
    public final static String TRAFFIC_TREND_UP = "traffic_trend_up_unfocused.png";
    public final static String TRAFFIC_TREND_DOWN = "traffic_trend_down_unfocused.png";
    
//    public final static String TRAFFIC_INCIDENT = "traffic_incident_unfocused.png";
//    public final static String TRAFFIC_ACCIDENT = "traffic_accident_unfocused.png";
//    public final static String TRAFFIC_CONJESTION = "traffic_conjestion_unfocused.png";
//    public final static String TRAFFIC_CONSTRUCTION = "traffic_construction_unfocused.png";
    
    public final static String LIST_CONTACTS_ICON_FOCUSED = "list_contacts_icon_focused.png";
    public final static String LIST_CONTACTS_ICON_UNFOCUSED = "list_contacts_icon_unfocused.png";
    public final static String LIST_CALL_ICON_FOCUSED = "list_call_icon_focused.png";
    public final static String LIST_CALL_ICON_UNFOCUSED = "list_call_icon_unfocused.png";
    public final static String LIST_AIRPORT_ICON_FOCUSED = "list_airport_icon_focused.png";
    public final static String LIST_AIRPORT_ICON_UNFOCUSED = "list_airport_icon_unfocused.png";
    public final static String SEARCHBOX_ICON_CONTACT_FOCUSED = "searchbox_icon_contact_focused.png";
    public final static String SEARCHBOX_ICON_CONTACT_UNFOCUSED = "searchbox_icon_contact_unfocused.png";
    
    
    public final static String MOVING_MAP_ICON_UNFOCUSED = "nav_center_location_icon_unfocused.png";
    public final static String MOVING_MAP_ICON_FOCUSED = "nav_center_location_icon_focused.png";
    
    public final static String MAP_POI_BAR_LEFT_ARROW_FOCUSED = "previous_poi_icon_focused.png";
    public final static String MAP_POI_BAR_LEFT_ARROW_UNFOCUSED = "previous_poi_icon_unfocused.png";
    public final static String MAP_POI_BAR_LEFT_ARROW_DISABLED = "previous_poi_icon_disabled.png";
    public final static String MAP_POI_BAR_RIGHT_ARROW_FOCUSED = "next_poi_icon_focused.png";
    public final static String MAP_POI_BAR_RIGHT_ARROW_UNFOCUSED = "next_poi_icon_unfocused.png";
    public final static String MAP_POI_BAR_RIGHT_ARROW_DISABLED = "next_poi_icon_disabled.png";
    
    public final static String BOTTOM_BAR_END_TRIP_UNFOCUS = "bottom_bar_icon_exit_unfocused.png";
    public final static String BOTTOM_BAR_END_TRIP_FOCUS = "bottom_bar_icon_exit_focused.png";
    
    public final static String BOTTOM_BAR_PLACES_UNFOCUS = "bottom_bar_icon_places_unfocused.png";
    public final static String BOTTOM_BAR_PLACES_FOCUS = "bottom_bar_icon_places_focused.png";
    public final static String BOTTOM_BAR_PLACES_DISABLE = "bottom_bar_icon_places_disabled.png";

    public final static String SUMMARY_ITEM_SOUND_ICON_UNFOCUSED = "summary_item_sound_icon_unfocused.png";
    
    public final static String TRAFFIC_ALERT_SMALL_YELLOW_ICON_UNFOCUSED = "traffic_alert_small_yellow_icon_unfocused.png";
    
    public final static String TRAFFIC_ALERT_SMALL_ORANGE_ICON_UNFOCUSED = "traffic_alert_small_orange_icon_unfocused.png";
    public final static String TRAFFIC_ALERT_SMALL_RED_ICON_UNFOCUSED = "traffic_alert_small_red_icon_unfocused.png";
    public final static String TRAFFIC_ALERT_SMALL_YELLOW_EMPTY_ICON_UNFOCUSED = "traffic_alert_small_yellow_empty_icon_unfocused.png";
    public final static String TRAFFIC_ALERT_SMALL_ORANGE_EMPTY_ICON_UNFOCUSED = "traffic_alert_small_orange_empty_icon_unfocused.png";
    public final static String TRAFFIC_ALERT_SMALL_RED_EMPTY_ICON_UNFOCUSED = "traffic_alert_small_red_empty_icon_unfocused.png";
    
    public final static String BOTTOM_BAR_ICON_DIRECTIONS_FOCUSED = "bottom_bar_icon_directions_focused.png";
    public final static String BOTTOM_BAR_ICON_DIRECTIONS_UNFOCUSED = "bottom_bar_icon_directions_unfocused.png";
    
    public final static String MAP_FIT_ICON_FOCUSED = "map_fit_icon_focused.png";
    public final static String MAP_FIT_ICON_UNFOCUSED = "map_fit_icon_unfocused.png";
    public final static String MAP_FIT_ICON_NIGHT_UNFOCUSED = "map_fit_icon_night_unfocused.png";
    
    public final static String POI_LIST_MAP_BUTTON_FOCUSED = "searchbox_icon_map_focused.png";
    public final static String POI_LIST_MAP_BUTTON_UNFOCUSED = "searchbox_icon_map_unfocused.png";
    
    public final static String COMPASS_E = "compass_e.png";
    public final static String COMPASS_N = "compass_n.png";
    public final static String COMPASS_NE = "compass_ne.png";
    public final static String COMPASS_NW = "compass_nw.png";
    public final static String COMPASS_S = "compass_s.png";
    public final static String COMPASS_SE = "compass_se.png";
    public final static String COMPASS_SW = "compass_sw.png";
    public final static String COMPASS_W = "compass_w.png";
    public final static String COMPASS_2D = "compass_2d.png";
    
    public final static String LIST_CREATE_CATEGORY_ICON_UNFOCUSED = "list_create_category_icon_unfocused.png";
    public final static String LIST_CREATE_CATEGORY_ICON_FOCUSED = "list_create_category_icon_focused.png";
    
    public final static String DIRECTIONS_ICON_FOCUSED = "directions_icon_focused.png";
    public final static String DIRECTIONS_ICON_UNFOCUSED = "directions_icon_unfocused.png";
    public final static String NAVIGATE_ICON_FOCUSED = "navigate_icon_focused.png";
    public final static String NAVIGATE_ICON_UNFOCUSED = "navigate_icon_unfocused.png";
    
    public final static String RECEIVE_ALARM_CIRCLE_UNFOCUSED = "receive_alarm_circle_unfocused.png";
    public final static String RECEIVE_ALARM_SMALL_CIRCLE_UNFOCUSED = "receive_alarm_small_circle_unfocused.png";
    
    public final static String SHADOW_TOP_UNFOCUSED = "shadow_top_unfocused.png";
    public final static String SHADOW_BOTTOM_UNFOCUSED = "shadow_bottom_unfocused.png";
    public final static String SHADOW_LEFT_UNFOCUSED = "nav_left_bar_shadow_unfocused.png";
    public final static String BOTTOM_BAR_TOP_UNFOCUSED = "bottom_bar_bg_line_unfocused.png";
    
    public final static String ROUTE_CURRENT_ICON_UNFOCUSED = "route_current_icon_unfocused.png";
    public final static String ROUTE_NEW_ICON_UNFOCUSED = "route_new_icon_unfocused.png";
    
    public final static String DAY_LOGO_ON_MAP = "telenav_watermark_logo_unfocused.png";
    public final static String NIGHT_LOGO_ON_MAP = "telenav_watermark_logo_night_unfocused.png";
    
    public final static String FAVORITES_ICON_FOCUSED = "favorites_icon_focused.png";
    public final static String FAVORITES_ICON_UNFOCUSED = "favorites_icon_unfocused.png";
    public final static String FAVORITES_ADD_ICON_FOCUSED = "favorites_add_icon_focused.png";
    public final static String FAVORITES_ADD_ICON_UNFOCUSED = "favorites_add_icon_unfocused.png";
    
    
//    public final static String NAV_MY_LOCATION_ICON_WEAKGPS_UNFOCUSED = "nav_my_location_icon_weakgps_unfocused.png";
    
    public final static String NAV_TRAFFIC_ICON_UNFOCUSED = "nav_traffic_icon_unfocused.png";
    public final static String NAV_TRAFFIC_ICON_FOCUSED = "nav_traffic_icon_focused.png";
    
    public final static String SEARCH_RESULTS_NUMBER_ELLIPSE_ICON_UNFOCUSED = "search_results_number_ellipse_icon_unfocused.png";
    
    public final static String SPEED_CAMERA_ICON_UNFOCUSED = "speed_camera_icon_unfocused.png";
    public final static String SPEED_TRAP_ICON_UNFOCUSED = "speed_trap_icon_unfocused.png";
    
    public final static String SEARCHBOX_ICON_SELECT_LOCATION_FOCUSED = "searchbox_icon_select_location_focused.png";
    public final static String SEARCHBOX_ICON_SELECT_LOCATION_UNFOCUSED = "searchbox_icon_select_location_unfocused.png";
    
    public final static String DASHBOARD_ARROW_ICON_FOCUSED = "dashboard_list_arrow_focused.png";
    public final static String DASHBOARD_ARROW_ICON_UNFOCUSED = "dashboard_list_arrow_unfocused.png";
    public final static String DASHBOARD_ARROW_ICON_GRAY_UNFOCUSED = "dashboard_list_arrow_grey_unfocused.png";

    public final static String DASHBOARD_TIME_SPLIT_CIRCLE_UNFOCUSED = "dashboard_time_split_circle_unfocused.png";
    
    public final static String DASHBOARD_RESUME_TRIP_CLOSE_ICON_UNFOCUSED = "dashboard_close_icon_unfocused.png";
    public final static String DASHBOARD_RESUME_TRIP_CLOSE_ICON_FOCUSED = "dashboard_close_icon_focused.png";
    
//    public final static String ATT_DASHBOARD_ICON_AIRPORT_UNFOCUSED = "drive_to_banner_airports_icon_unfocused.png";
//    public final static String ATT_DASHBOARD_ICON_AIRPORT_FOCUSED = "drive_to_banner_airports_icon_focused.png";
//    public final static String ATT_DASHBOARD_ICON_CONTACTS_UNFOCUSED = "drive_to_banner_contacts_icon_unfocused.png";
//    public final static String ATT_DASHBOARD_ICON_CONTACTS_FOCUSED = "drive_to_banner_contacts_icon_focused.png";
//    public final static String ATT_DASHBOARD_ICON_FAVORITES_UNFOCUSED = "drive_to_banner_favorites_icon_unfocused.png";
//    public final static String ATT_DASHBOARD_ICON_FAVORITES_FOCUSED = "drive_to_banner_favorites_icon_focused.png";
//    public final static String ATT_DASHBOARD_ICON_RECENTS_UNFOCUSED = "drive_to_banner_recents_icon_unfocused.png";
//    public final static String ATT_DASHBOARD_ICON_RECENTS_FOCUSED = "drive_to_banner_recents_icon_focused.png";
//    public final static String ATT_DASHBOARD_ARROW_UNFOCUSED = "drive_to_banner_arrow_icon_unfocused.png";
//    public final static String ATT_DASHBOARD_ARROW_FOCUSED = "drive_to_banner_arrow_icon_focused.png";
//    public final static String ATT_DASHBOARD_ROUTE_PICTURE_UNFOCUSED = "drive_to_route_picture_unfocused.png";
//    public final static String ATT_DASHBOARD_MAP_OFFLINE_UNFOCUSED = "drive_to_offline_map_icon_unfocused.png";
    
    public final static String INCIDENT_ACCIDENT = "accident.png";
    
    public final static String INCIDENT_CONGESTION = "congestion.png";
    
    public final static String INCIDENT_CONSTRUCTION = "construction.png";
    
    public final static String INCIDENT_DEFAULT = "incident_default.png";
    
    public final static String TRAFFIC_CAMERA_ON_NAV = "traffic_cam.png";
    
    public final static String SPEED_TRAP_ON_NAV = "traffic_police.png";
    
//    public final static String POI_DETAIL_FEEDBACK_BUTTON_FOCUSED = "feedback_icon_focused.png";
    
    public final static String POI_DETAIL_FEEDBACK_BUTTON_UNFOCUSED = "title_icon_feedback_unfocused.png";
    
    public final static String POI_BUBBLE_TITLE_CAR_ICON_FOCUSED = "poi_bubble_title_driveto_icon_focused.png";
    
    public final static String POI_BUBBLE_TITLE_CAR_ICON_UNFOCUSED = "poi_bubble_title_driveto_icon_unfocused.png";
    
    public final static String FTUE_LANDING_LOGO = "native_ftue_landing_scout_loge_unfocused.png";
    
    public final static String FTUE_01_DRIVE = "native_ftue_01_drive.png";
    
    public final static String FTUE_02_FAVS = "native_ftue_02_favs.png";
    
    public final static String FTUE_03_PLACES = "native_ftue_03_places.png";
    
    public final static String FTUE_04_TRAFFIC = "native_ftue_04_traffic.png";
    
    public final static String FTUE_SUCCESS = "native_ftue_thankyou_unfocused.png";
    
    public final static String FTUE_DOT_ICON_UNFOCUSED = "ftue_dot_icon_unfocused.png";
    
    public final static String FTUE_DOT_ICON_FOCUSED = "ftue_dot_icon_focused.png";
    
    public final static String MAP_2D_DOT = "dot.png";

    public final static String PROFILE_SWITCH_OFF_FOCUSED = "profile_switch_off_focused.png";
    
    public final static String PROFILE_SWITCH_ON_FOCUSED = "profile_switch_on_focused.png";
    
    public final static String PROFILE_SWITCH_OFF_UNFOCUSED = "profile_switch_off_unfocused.png";
    
    public final static String PROFILE_SWITCH_ON_UNFOCUSED = "profile_switch_on_unfocused.png";
    
    public final static String PROFILE_SWITCH_BG = "profile_switch_bg_unfocused.png";
    
    public final static String EDIT_HOME_DELETE = "title_icon_delete_unfocused.png";
    
    public final static String EDIT_HOME_ADD_CONTACT = "list_icon_add_unfocused.png";
    
    //For Fordapplink lockout screen
    public final static String LOCKOUT_IMAGE = "lockout_image.png";
    public final static String LOCKOUT_LOGO = "lockout_logo.png";
    public final static String LOCKOUT_BG_IMAGE = "lockout_bg_image.png";
    public final static String LOCKOUT_BG_IMAGE_LANDSCAPE = "lockout_bg_image_landscape.png";
    
    //car icon
    public final static String CAR_ARROW = "profile_mycar_default_icon_unfocused.png";
    public final static String CAR_SPORTS = "profile_mycar_sportscar_icon_unfocused.png";
    public final static String CAR_CLASSIC = "profile_mycar_classic_icon_unfocused.png";
    public final static String CAR_COMPACT = "profile_mycar_smallcar_icon_unfocused.png";
    public final static String CAR_CONVERTIBLE = "profile_mycar_convertable_icon_unfocused.png";
    public final static String CAR_BATTLE_TANK = "profile_mycar_tank_icon_unfocused.png";
    public final static String CAR_MINIVAN = "profile_mycar_minivan_icon_unfocused.png";
    public final static String CAR_MONSTER_TRUCK = "profile_mycar_monstertruck_icon_unfocused.png";
    public final static String CAR_MOTORCYCLE = "profile_mycar_motorcycle_icon_unfocused.png";
    public final static String CAR_MUSCLE = "profile_mycar_muscle_icon_unfocused.png";
    public final static String CAR_OLD_SCHOOL = "profile_mycar_oldschool_icon_unfocused.png";
    public final static String CAR_SUV = "profile_mycar_suv_icon_unfocused.png";
    public final static String CAR_SPACE_FIGHTER = "profile_mycar_spaceship_icon_unfocused.png";

    public final static String SEARCHBOX_MIC_UNFOCUSED = "searchbox_icon_mic_unfocused.png";
    public final static String SEARCHBOX_MIC_FOCUSED = "searchbox_icon_mic_focused.png";
    public final static String SEARCHBOX_MIC_DISABLED = "searchbox_icon_mic_disabled.png";
    public final static String DASHBOARD_FAVORITE_ICON_FOCUSED = "dashboard_favorite_icon_focused.png";
    public final static String DASHBOARD_FAVORITE_ICON_UNFOCUSED = "dashboard_favorite_icon_unfocused.png";
    
    public final static String SEARCHBOX_ICON_HINT_UNFOCUSED = "searchbox_icon_hint_search_unfocused.png";
    
    // map download
    public final static String US_CENTER = "us_central.png";
    public final static String US_EAST = "us_east.png";
    public final static String US_WEST = "us_west.png";
    public final static String TAB_FINISHED_ICON = "map_download_tab_finished_icon_unfocused.png";
    
    public final static String SEARCHBOX_MIC = "searchbox_icon_mic_unfocused.png";
    
    //Upsell
    public final static String ICON_NO_ADS_FOCUSED= "ftue_learn_more_icon_no_ads_focused.png";
    public final static String ICON_NO_ADS_UNFOCUSED= "ftue_learn_more_icon_no_ads_unfocused.png";
    public final static String ICON_ALWAYS_THERE_FOCUSED= "ftue_learn_more_icon_always_there_focused.png";
    public final static String ICON_ALWAYS_THERE_UNFOCUSED= "ftue_learn_more_icon_always_there_unfocused.png";
    public final static String ICON_REAL_TIME_TRAFFIC_FOCUSED= "ftue_learn_more_icon_traffic_focused.png";
    public final static String ICON_REAL_TIME_TRAFFIC_UNFOCUSED= "ftue_learn_more_icon_traffic_unfocused.png";
    public final static String ICON_RED_LIGHT_CAMERA_ALERTS_FOCUSED="ftue_learn_more_icon_camera_focused.png";
    public final static String ICON_RED_LIGHT_CAMERA_ALERTS_UNFOCUSED="ftue_learn_more_icon_camera_unfocused.png";
//    public final static String ICON_SPEED_TRAPS_FOCUSED="ftue_learn_more_icon_speed_trap_focused.png";
//    public final static String ICON_SPEED_TRAPS_UNFOCUSED="ftue_learn_more_icon_speed_trap_unfocused.png";
//    public final static String ICON_POSTED_SPEED_LIMITS_FOCUSED= "ftue_learn_more_icon_speed_limit_focused.png";
//    public final static String ICON_POSTED_SPEED_LIMITS_UNFOCUSED= "ftue_learn_more_icon_speed_limit_unfocused.png";    
    public final static String ICON_LANE_ASSIST_FOCUSED= "ftue_learn_more_icon_lane_assist_focused.png";
    public final static String ICON_LANE_ASSIST_UNFOCUSED= "ftue_learn_more_icon_lane_assist_unfocused.png";
    public final static String ICON_CAR_CONNECT_FOCUSED = "ftue_learn_more_icon_car_connect_focused.png";
    public final static String ICON_CAR_CONNECT_UNFOCUSED = "ftue_learn_more_icon_car_connect_unfocused.png";  

    public final static String GALERY_REAL_TIME_TRAFFIC= "pic_traffic.png";
    public final static String GALERY_RED_LIGHT_CAMERA_ALERTS="pic_camera.png";
    public final static String GALERY_LANE_ASSIST="pic_speed_limits.png";
    public final static String GALERY_ALWAYS_THERE="pic_always_there.png";
    public final static String GALERY_NO_ADS ="pic_no_ads.png";
    public final static String GALERY_CAR_CONNECT="logo_connect.png";

    public final static String UPSELL_ARROW_UNFOCUSED = "ftue_arrow_unfocused.png";
    public final static String UPSELL_ARROW_FOCUSED = "ftue_arrow_focused.png";
            
    //portrait route planning
    public final static String ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_FOCUSED = "route_planning_1A_panel_green_list_summary_focused.png";
    public final static String ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_FOCUSED = "route_planning_1A_panel_green_map_summary_focused.png";
    public final static String ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_FOCUSED = "route_planning_2A_panel_green_list_summary_focused.png";
    public final static String ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_FOCUSED = "route_planning_2A_panel_green_map_summary_focused.png";
    public final static String ROUTE_PLANNING_2A_PANEL_GREEN_UNFOCUSED = "route_planning_2A_panel_green_unfocused.png";
    public final static String ROUTE_PLANNING_2B_PANEL_GREEN_LIST_SUMMARY_FOCUSED = "route_planning_2B_panel_blue_list_summary_focused.png";
    public final static String ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_FOCUSED = "route_planning_2B_panel_blue_map_summary_focused.png";
    public final static String ROUTE_PLANNING_2B_PANEL_BLUE_UNFOCUSED = "route_planning_2B_panel_blue_unfocused.png";
    public final static String ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_FOCUSED = "route_planning_3A_panel_green_list_summary_focused.png";
    public final static String ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_FOCUSED = "route_planning_3A_panel_green_map_summary_focused.png";
    public final static String ROUTE_PLANNING_3A_PANEL_GREEN_UNFOCUSED = "route_planning_3A_panel_green_unfocused.png";
    public final static String ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_FOCUSED = "route_planning_3B_panel_blue_list_summary_focused.png";
    public final static String ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_FOCUSED = "route_planning_3B_panel_blue_map_summary_focused.png";
    public final static String ROUTE_PLANNING_3B_PANEL_BLUE_UNFOCUSED = "route_planning_3B_panel_blue_unfocused.png";
    public final static String ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_FOCUSED = "route_planning_3C_panel_red_list_summary_focused.png";
    public final static String ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_FOCUSED = "route_planning_3C_panel_red_map_summary_focused.png";
    public final static String ROUTE_PLANNING_3C_PANEL_RED_UNFOCUSED = "route_planning_3C_panel_red_unfocused.png";
    
    //landscape route planning
    public final static String ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_1A_panel_green_list_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_1A_panel_green_map_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_2A_panel_green_list_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_2A_panel_green_map_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_2A_PANEL_GREEN_LANDSCAPE_UNFOCUSED = "route_planning_2A_panel_green_landscape_unfocused.png";
    public final static String ROUTE_PLANNING_2B_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_2B_panel_blue_list_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_2B_panel_blue_map_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_2B_PANEL_BLUE_LANDSCAPE_UNFOCUSED = "route_planning_2B_panel_blue_landscape_unfocused.png";
    public final static String ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_3A_panel_green_list_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_3A_panel_green_map_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_3A_PANEL_GREEN_LANDSCAPE_UNFOCUSED = "route_planning_3A_panel_green_landscape_unfocused.png";
    public final static String ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_3B_panel_blue_list_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_3B_panel_blue_map_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_3B_PANEL_BLUE_LANDSCAPE_UNFOCUSED = "route_planning_3B_panel_blue_landscape_unfocused.png";
    public final static String ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_3C_panel_red_list_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_LANDSCAPE_FOCUSED = "route_planning_3C_panel_red_map_summary_landscape_focused.png";
    public final static String ROUTE_PLANNING_3C_PANEL_RED_LANDSCAPE_UNFOCUSED = "route_planning_3C_panel_red_landscape_unfocused.png";
    
    //routeplanning checkbox & navigate
    public final static String ROUTE_PLANNING_CHECKBOX_FOCUSED = "route_planning_check_box_focused.png";
    public final static String ROUTE_PLANNING_CHECKBOX_UNFOCUSED = "route_planning_check_box_unfocused.png";
    public final static String ROUTE_PLANNING_CHECKBOX_DISABLED = "route_planning_check_box_disabled.png";
    public final static String ROUTE_PLANNING_NAVIGAGE_UNFOCUSED = "route_planning_drive_icon_unfocused.png";
    public final static String ROUTE_PLANNING_STYLE_SETTING = "title_icon_options_unfocused.png";
    
    public final static String HOME_SETTING_TITLE_DELETE_UNFOCUSED = "title_icon_delete_unfocused.png";
    
    //dwf
    public final static String DWF_MAP_OPEN_UNFOCUSED = "drive_with_friends_map_icon_open_unfocused.png";
    public final static String DWF_MAP_OPEN_FOCUSED = "drive_with_friends_map_icon_open_focused.png";
    public final static String DWF_MAP_FOLD_UNFOCUSED = "drive_with_friends_map_icon_fold_unfocused.png";
    public final static String DWF_MAP_FOLD_FOCUSED = "drive_with_friends_map_icon_fold_focused.png";
    public final static String DWF_MAP_OPEN_LANDSCAPE_UNFOCUSED = "drive_with_friends_map_icon_open_landscape_unfocused.png";
    public final static String DWF_MAP_OPEN_LANDSCAPE_FOCUSED = "drive_with_friends_map_icon_open_landscape_focused.png";
    public final static String DWF_MAP_FOLD_LANDSCAPE_UNFOCUSED = "drive_with_friends_map_icon_fold_landscape_unfocused.png";
    public final static String DWF_MAP_FOLD_LANDSCAPE_FOCUSED = "drive_with_friends_map_icon_fold_landscape_focused.png";

    public static String getSpecialImageFamily()
    {
        if(iconFamily != null)
            return iconFamily;
        
        int maxDisplaySize = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
        if(maxDisplaySize <= 320)
        {
            iconFamily = ISpecialImageRes.FAMILY_MEDIUM;
        }
        else if(maxDisplaySize <= 480)
        {
            iconFamily = ISpecialImageRes.FAMILY_MEDIUM;
        }
        /*else if(maxDisplaySize <= 640)
        {
            iconFamily = ISpecialImageRes.FAMILY_HUGE;
        }*/
        if( iconFamily == null )
        {
            iconFamily = ISpecialImageRes.FAMILY_VAST;
        }
        getStretchRatio();
        
        return iconFamily;
    }
    
    protected static void getStretchRatio() 
    {
        if(!isNeedStretch)
        {
            isNeedStretch = true;
            computeStretchRatio();
        }
	}

    public static void computeStretchRatio()
    {
        int w1 = AppConfigHelper.getMinDisplaySize();
        int h1 = AppConfigHelper.getMaxDisplaySize();
        int w2 = -1;
        int h2 = -1;
        if(ISpecialImageRes.FAMILY_MEDIUM.equalsIgnoreCase(iconFamily))
        {
            w2 = MEDIUM_WIDTH;
            h2 = MEDIUM_HEIGHT;
        }
        else if(ISpecialImageRes.FAMILY_VAST.equalsIgnoreCase(iconFamily))
        {
            w2 = VAST_WIDTH;
            h2 = VAST_HEIGHT;
        }
        else
        {
            return;
        }
        
        numerator = w1 * h2 + h1 * w2;
        denominator = 2 * w2 * h2;
    }
	//Per PM, the family definitions are
    //Small: 480x320 and below
    //Medium: 800x480, 854x480
    //Large: 960x540 and above
    public static String getOpenglImageFamily()
    {
        if(openglFamily != null)
            return openglFamily;
        
        int maxDisplaySize = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
        
        if(maxDisplaySize <= 400)
        {
            openglFamily = ISpecialImageRes.FAMILY_TINY;
        }
        else if(maxDisplaySize <= 480)
        {
            openglFamily = ISpecialImageRes.FAMILY_SMALL;
        }
        else if(maxDisplaySize <= 854)
        {
            openglFamily = ISpecialImageRes.FAMILY_MEDIUM;
        }
        else
        {
            openglFamily = ISpecialImageRes.FAMILY_LARGE;
        }
        
        return openglFamily;
    }
}
