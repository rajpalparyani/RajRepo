/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ISpecialImageRes.java
 *
 */
package com.telenav.searchwidget.ui;

import com.telenav.searchwidget.app.AppConfigHelper;
import com.telenav.tnui.core.AbstractTnUiHelper;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 9, 2010
 */
public class ISpecialImageRes
{
    // =====================================below are icon's id constraint=====================================//
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
    public final static String CLOSE_ICON_BLUE = "close_blur.png";
    public final static String CLOSE_ICON_FOCUS = "close_focus.png";
    
    public final static String HOME_ICON_FOCUSED = "home_icon_focused.png";
    public final static String HOME_ICON_UNFOCUSED = "home_icon_unfocused.png";
    
    public final static String MIC_ICON_LANDSCAPE_UNFOCUS = "huge_mic_icon_landscape_unfocused.png";
    public final static String MIC_ICON_UNFOCUS = "huge_mic_icon_unfocused.png";
    
    public final static String WAVE_ICON_LOUD_LEFT_UNFOCUS = "huge_mic_wave_icon_loud_unfocused_left.png";
    public final static String WAVE_ICON_LOUD_RIGHT_UNFOCUS = "huge_mic_wave_icon_loud_unfocused_right.png";
    public final static String WAVE_ICON_LOW_LEFT_UNFOCUS = "huge_mic_wave_icon_low_unfocused_left.png";
    public final static String WAVE_ICON_LOW_RIGHT_UNFOCUS = "huge_mic_wave_icon_low_unfocused_right.png";
    public final static String WAVE_ICON_NORMAL_LEFT_UNFOCUS = "huge_mic_wave_icon_normal_unfocused_left.png";
    public final static String WAVE_ICON_NORMAL_RIGHT_UNFOCUS = "huge_mic_wave_icon_normal_unfocused_right.png";
    
    public final static String HOME_ICON_FOCUS = "home_icon_focused.png";
    public final static String HOME_ICON_UNFOCUS = "home_icon_unfocused.png";
    
    public final static String AC_ICON_FIND_UNFOCUSED = "searchbox_icon_search_unfocused.png";
    public final static String AC_ICON_FIND_FOCUSED = "searchbox_icon_search_focused.png";
    public final static String AC_ICON_BACKSPACE = "searchbox_icon_clear_unfocused.png";
    public final static String FAV_ICON_ADD_FAVORITE_UNFOCUS = "buttonicon_addfavorate_unfocused.png";
    public final static String FAV_ICON_ADD_CATEGORY_UNFOCUS = "buttonicon_addcategory_unfocused.png";
    public final static String FAV_ICON_SYNC_UNFOCUS = "buttonicon_refresh_unfocused.png";
    public final static String FAV_ICON_LIST_CATEGORY_ICON = "icon_ac_favorites.png";
    public final static String FAV_ICON_EDIT_UNFOCUS = "buttonicon_edit_unfocused.png";
    public final static String IMAGE_TEST_GALLERY = "test_gallery_focus.png";
    public final static String FAV_ICON_EDIT_FAVORITE_SIGN = "favority_added.png";
    public final static String CHECK_BOX_ICON_ON = "check_box_focused.png";
    public final static String CHECK_BOX_ICON_OFF = "check_box_unfocused.png";
    public final static String CHECK_BOX_ICON_DISABLE = "check_box_disabled.png";
    
    public final static String ZOOM_IN_VERTICAL_ICON_OFF = "vertical_zoom_in_button_unfocused.png";
    public final static String ZOOM_IN_VERTICAL_ICON_ON = "vertical_zoom_in_button_focused.png";
    public final static String ZOOM_IN_VERTICAL_DISABLED = "vertical_zoom_in_button_disabled.png";
    public final static String ZOOM_OUT_VERTICAL_ICON_OFF = "vertical_zoom_out_button_unfocused.png";
    public final static String ZOOM_OUT_VERTICAL_ICON_ON = "vertical_zoom_out_button_focused.png";
    public final static String ZOOM_OUT_VERTICAL_DISABLED = "vertical_zoom_out_button_disabled.png";
    
    public final static String NAV_ZOOM_OUT_ICON_UNFOCUSED = "nav_zoom_out_icon_unfocused.png";
    public final static String NAV_ZOOM_IN_ICON_UNFOCUSED = "nav_zoom_in_icon_unfocused.png";
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
    
    
    public final static String MY_LOCATION_ICON_OFF = "my_location_icon_unfocused.png";
    public final static String MY_LOCATION_ICON_ON = "my_location_icon_focused.png";
//    public final static int MY_LOCATION_OUT_ICON_DISABLE = 128;
    
    public final static String POI_BUBBLE_DOWN_ARROW = "poi_bubble_bg_unfocused_arrow.png";
    
    public final static String MAP_POI = "poi_small_icon_unfocused.png";
    public final static String SELECTED_MAP_POI = "poi_small_icon_focused.png";
    
    public final static String BIG_MAP_POI  = "poi_big_icon_unfocused.png";
    public final static String BIG_SELECTED_MAP_POI = "poi_big_icon_focused.png";
    
    public final static String PREV_POI_UNFOCUS = "poi_bubble_previous_button_unfocused.png";
    public final static String PREV_POI_FOCUSED = "poi_bubble_previous_button_focused.png";
    
    public final static String NEXT_POI_UNFOCUS = "poi_bubble_next_button_unfocused.png";
    public final static String NEXT_POI_FOCUSED = "poi_bubble_next_button_focused.png";
    
    public final static String BOTTOM_BAR_DRIVE_UNFOCUS = "bottom_bar_icon_driveto_unfocused.png";
    public final static String BOTTOM_BAR_DRIVE_FOCUS = "bottom_bar_icon_driveto_focused.png";
    
    public final static String BOTTOM_BAR_EXTRA_UNFOCUS = "bottom_bar_icon_extras_unfocused.png";
    public final static String BOTTOM_BAR_EXTRA_FOCUS = "bottom_bar_icon_extras_focused.png";
    
    public final static String BOTTOM_BAR_MAP_UNFOCUS = "bottom_bar_icon_maps_unfocused.png";
    public final static String BOTTOM_BAR_MAP_FOCUS = "bottom_bar_icon_maps_focused.png";
    
    public final static String BOTTOM_BAR_LIST_UNFOCUS = "bottom_bar_icon_route_unfocused.png";
    public final static String BOTTOM_BAR_LIST_FOCUS = "bottom_bar_icon_route_focused.png";
    
    public final static String BOTTOM_MIC_ICON_UNFOCUS = "mic_icon_unfocused.png";
    
    public final static String SEARCHBOX_BOOKMARK_UNFOCUS = "searchbox_icon_bookmarks_unfocused.png";
    public final static String SEARCHBOX_CLEAR_UNFOCUS = "searchbox_icon_clear_unfocused.png";
    public final static String SEARCHBOX_POIMAP_UNFOCUS = "searchbox_icon_poimap_unfocused.png";
    public final static String SEARCHBOX_POILIST_UNFOCUS = "searchbox_icon_poilist_unfocused.png";
    public final static String SEARCHBOX_SEARCH_UNFOCUS = "searchbox_icon_search_unfocused.png";
    
    public final static String ADD_FAV_UNFOCUS = "buttonicon_addfavorate_unfocused.png";
    public final static String ADD_CAT_UNFOCUS = "buttonicon_addcategory_unfocused.png";
    public final static String EDIT_UNFOCUS = "buttonicon_edit_unfocused.png";
    
    public final static String POI_BUBBLE_BUTTON_MORE_ARROW_UNFOCUS = "poi_bubble_details_icon_unfocused.png";
    public final static String POI_BUBBLE_BUTTON_DRIVETO_UNFOCUS = "poi_bubble_driveto_icon_unfocused.png";
    public final static String POI_BUBBLE_BUTTON_DRIVETO_FOCUSED = "poi_bubble_driveto_icon_focused.png";
    public final static String POI_BUBBLE_BUTTON_CALL_UNFOCUS = "poi_bubble_call_icon_unfocused.png";
    public final static String POI_BUBBLE_BUTTON_CALL_FOCUSED = "poi_bubble_call_icon_focused.png";
    public final static String POI_BUBBLE_BUTTON_SHARE_UNFOCUS = "poi_bubble_share_icon_unfocused.png";
    public final static String POI_BUBBLE_BUTTON_SHARE_FOCUSED = "poi_bubble_share_icon_focused.png";
    
    public final static String POI_BUBBLE_NEARBY_FOCUSED = "poi_bubble_nearby_icon_focused.png";
    public final static String POI_BUBBLE_NEARBY_UNFOCUSED = "poi_bubble_nearby_icon_unfocused.png";
    public final static String POI_BUBBLE_SAVE_ICON_FOCUSED = "poi_bubble_save_icon_focused.png";
    public final static String POI_BUBBLE_SAVE_ICON_UNFOCUSED = "poi_bubble_save_icon_unfocused.png";
    
    
    public final static String AC_ICON_HOME_DISABLED = "ac_main_home_icon_disabled.png";
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
    public final static String LIST_FAVORITE_ICON_UNFOCUS = "list_favorites_icon_unfocused.png";
    public final static String AC_ICON_ADDRESS_FOCUSED = "ac_main_address_icon_focused.png";
    public final static String AC_ICON_ADDRESS_UNFOCUSED = "ac_main_address_icon_unfocused.png";
    public final static String AC_ICON_PLACES_FOCUSED = "ac_main_places_icon_focused.png";
    public final static String AC_ICON_PLACES_UNFOCUSED = "ac_main_places_icon_unfocused.png";
    public final static String AC_ICON_CURRENT_LOCATION_FOCUSED = "ac_main_current_location_icon_focused.png";
    public final static String AC_ICON_CURRENT_LOCATION_UNFOCUSED = "ac_main_current_location_icon_unfocused.png";
    
    public final static String POI_QUICK_SEARCH_1_ICON = "search_panel_atm_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_2_ICON = "search_panel_car_repair_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_3_ICON = "search_panel_car_rental_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_4_ICON = "search_panel_food_coffee_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_5_ICON = "search_panel_gas_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_6_ICON = "search_panel_gas_by_price_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_7_ICON = "search_panel_grocery_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_8_ICON = "search_panel_lodging_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_9_ICON = "search_panel_medical_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_10_ICON = "search_panel_movies_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_11_ICON = "search_panel_nightlife_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_12_ICON = "search_panel_parking_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_13_ICON = "search_panel_shopping_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_14_ICON = "search_panel_taxis_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_15_ICON = "search_panel_wifi_icon_unfocused.png";
    public final static String POI_QUICK_SEARCH_16_ICON = "search_panel_more_icon_unfocused.png";
    public final static String POI_SEARCH_RESULT_INDEX_ICON    = "search_results_number_icon_unfocused.png";

    public final static String POI_REVIEW_BIGSTAR_FULL = "full_big_star_icon_unfocused.png";
    public final static String POI_REVIEW_BIGSTAR_VACANCY = "vacancy_big_star_icon_unfocused.png";
    public final static String POI_REVIEW_MIDSTAR_FULL = "full_medium_star_icon_unfocused.png";
    public final static String POI_REVIEW_MIDSTAR_VACANCY = "vacancy_medium_star_icon_unfocused.png";
    public final static String POI_REVIEW_SMALLSTAR_FULL = "full_small_star_icon_unfocused.png";
    public final static String POI_REVIEW_SMALLSTAR_VACANCY = "vacancy_small_star_icon_unfocused.png";
    
    public final static String NAV_ICON_HEADER_ARC = "nav_header_bar_blue_bg_unfocused.png";
    
    public final static String FAV_ICON_ADD_CATEGORY_FOCUSED = "buttonicon_addcategory_focused.png";
    public final static String FAV_ICON_ADD_FAVORITE_FOCUSED = "buttonicon_addfavorate_focused.png";
    public final static String FAV_ICON_SYNC_FOCUSED = "buttonicon_refresh_focused.png";
    public final static String FAV_ICON_EDIT_FOCUSED = "buttonicon_edit_focused.png";
    
    public final static String LIST_FAVORITE_FOLDER_UNFOCUS = "list_favorites_folder_icon_unfocused.png";
    public final static String LABEL_UPSELL = "att_nav_branding_unfocused.png";
    public final static String LIST_HISTORY_ICON_FOCUSED = "list_history_icon_focused.png";
    public final static String LIST_HISTORY_ICON_UNFOCUSED = "list_history_icon_unfocused.png";
    public final static String LIST_RECEIVED_ICON_UNFOCUSED = "list_received_icon_unfocused.png";
    public final static String LIST_RECEIVED_ICON_FOCUSED = "list_received_icon_focused.png";
    
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
    
    public final static String CURRENT_LOCATION_ICON_UNFOCUSED = "current_location_icon_unfocused.png";
    
    public final static String MAP_CURRENT_LOCATION_UNFOCUSED = "map_current_location_icon_unfocused.png";
    public final static String MAP_CURRENT_LOCATION_NIGHT_UNFOCUSED = "map_current_location_icon_night_unfocused.png";
    public final static String MAP_CURRENT_LOCATION_FOCUSED = "map_current_location_icon_focused.png";
    
    public final static String MAP_LAYER_ICON_FOCUSED  = "map_map_layer_icon_focused.png";
    public final static String MAP_LAYER_ICON_UNFOCUSED = "map_map_layer_icon_unfocused.png";
    public final static String MAP_LAYER_ICON_NIGHT_UNFOCUSED = "map_map_layer_icon_night_unfocused.png";
    
    public final static String SPLASH_GRAPHICS_UNFOCUSED_PORTRAIT = "splash_graphic_unfocused_portrait.png";
    public final static String SPLASH_GRAPHICS_UNFOCUSED_LANDSCAPE = "splash_graphic_unfocused_landscape.png";
    public final static String SPLASH_YPMOBILE_UNFOCUSED = "splash_ypmobile_unfocused.png";
    public final static String SPLASH_NAVTEQ_UNFOCUSED = "splash_navteq_unfocused.png";
    public final static String SPLASH_TEALALIAS_UNFOCUSED = "splash_tealalias_unfocused.png";
//    public final static String SPLASH_TELENAV_UNFOCUSED = "splash_telenav_unfocused.png";
    
    public final static String MAP_CAMERA_ICON_FOCUSED = "map_camera_icon_focused.png";
    public final static String MAP_CAMERA_ICON_UNFOCUSED = "map_camera_icon_unfocused.png";
    public final static String MAP_SATELLITE_ICON_FOCUSED = "map_GPS_icon_focused.png";
    public final static String MAP_SATELLITE_ICON_UNFOCUSED = "map_GPS_icon_unfocused.png";
    public final static String MAP_TRAFFIC_ICON_FOCUSED = "map_traffic_icon_focused.png";
    public final static String MAP_TRAFFIC_ICON_UNFOCUSED = "map_traffic_icon_unfocused.png";
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
    
    public final static String LANE_ASSIST_SMALL_LEFT_TURN_UNFOCUSED = "lane_assist_small_left_turn_unfocused.png";
    public final static String LANE_ASSIST_SMALL_RIGHT_TURN_UNFOCUSED = "lane_assist_small_right_turn_unfocused.png";
    public final static String LANE_ASSIST_SMALL_AHEAD_UNFOCUSED = "lane_assist_small_ahead_unfocused.png";
    public final static String LANE_ASSIST_SMALL_LEFT_TURN_FOCUSED = "lane_assist_small_left_turn_focused.png";
    public final static String LANE_ASSIST_SMALL_RIGHT_TURN_FOCUSED = "lane_assist_small_right_turn_focused.png";
    public final static String LANE_ASSIST_SMALL_AHEAD_FOCUSED = "lane_assist_small_ahead_focused.png";
    
    public final static String TRAFFIC_COLOR_BAR = "traffic_color_bar_unfocused.png";
    public final static String TRAFFIC_COLOR_BAR_LANDSCAPE = "traffic_color_bar_landscape_unfocused.png";
    public final static String TRAFFIC_GRAY_BAR = "traffic_gray_bar_unfocused.png";
    public final static String TRAFFIC_GRAY_BAR_LANDSCAPE = "traffic_gary_bar_landscape_unfocused.png";
    public final static String TRAFFIC_SLIDER = "traffic_slider_unfocused.png";
    
    public final static String TRAFFIC_TREND_UP = "traffic_trend_up_unfocused.png";
    public final static String TRAFFIC_TREND_DOWN = "traffic_trend_down_unfocused.png";
    
    public final static String TRAFFIC_INCIDENT = "traffic_incident_unfocused.png";
    public final static String TRAFFIC_ACCIDENT = "traffic_accident_unfocused.png";
    public final static String TRAFFIC_CONJESTION = "traffic_conjestion_unfocused.png";
    public final static String TRAFFIC_CONSTRUCTION = "traffic_construction_unfocused.png";
    
    public final static String LIST_CONTACTS_ICON_FOCUSED = "list_contacts_icon_focused.png";
    public final static String LIST_CONTACTS_ICON_UNFOCUSED = "list_contacts_icon_unfocused.png";
    public final static String LIST_CALL_ICON_FOCUSED = "list_call_icon_focused.png";
    public final static String LIST_CALL_ICON_UNFOCUSED = "list_call_icon_unfocused.png";
    public final static String LIST_AIRPORT_ICON_FOCUSED = "list_airport_icon_focused.png";
    public final static String LIST_AIRPORT_ICON_UNFOCUSED = "list_airport_icon_unfocused.png";
    
    public final static String MOVING_MAP_ICON_UNFOCUSED = "nav_center_location_icon_unfocused.png";
    
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
    
    public final static String ROUTE_CURRENT_ICON_UNFOCUSED = "route_current_icon_unfocused.png";
    public final static String ROUTE_NEW_ICON_UNFOCUSED = "route_new_icon_unfocused.png";
    
    public final static String SUGGEST_ROUTE_HEADER_ARC = "suggest_route_header_bar_blue_bg_unfocused.png";
    
    public final static String AD_RIGHT_TOP_CONNER_ICON = "list_ad_sign_unfocused.png";
    
    public final static String DAY_LOGO_ON_MAP = "telenav_watermark_logo_unfocused.png";
    public final static String NIGHT_LOGO_ON_MAP = "telenav_watermark_logo_night_unfocused.png";
    
    public final static String FAVORITES_ICON_FOCUSED = "favorites_icon_focused.png";
    public final static String FAVORITES_ICON_UNFOCUSED = "favorites_icon_unfocused.png";
    public final static String FAVORITES_ADD_ICON_FOCUSED = "favorites_add_icon_focused.png";
    public final static String FAVORITES_ADD_ICON_UNFOCUSED = "favorites_add_icon_unfocused.png";
    
    public final static String ARRIVAL_POP_TRANSPARENT_BG_UNFOCUSED = "arrival_pop_transparent_bg_unfocused.png";
    
    public final static String CURRENT_LOCATION_SHADOW_ICON_1_UNFOCUSED = "current_location_shadow_icon_1_unfocused.png";
    public final static String CURRENT_LOCATION_SHADOW_ICON_2_UNFOCUSED = "current_location_shadow_icon_2_unfocused.png";
    public final static String CURRENT_LOCATION_SHADOW_ICON_3_UNFOCUSED = "current_location_shadow_icon_3_unfocused.png";
    public final static String CURRENT_LOCATION_SHADOW_ICON_4_UNFOCUSED = "current_location_shadow_icon_4_unfocused.png";
    public final static String CURRENT_LOCATION_SHADOW_ICON_5_UNFOCUSED = "current_location_shadow_icon_5_unfocused.png";
    public final static String CURRENT_LOCATION_SHADOW_ICON_6_UNFOCUSED = "current_location_shadow_icon_6_unfocused.png";
    public final static String CURRENT_LOCATION_SHADOW_ICON_7_UNFOCUSED = "current_location_shadow_icon_7_unfocused.png";
    public final static String CURRENT_LOCATION_SHADOW_ICON_8_UNFOCUSED = "current_location_shadow_icon_8_unfocused.png";
    
    public final static String NAV_AD_DOWN_ARROW_FOCUSED = "nav_ad_down_arrow_focused.png";
    public final static String NAV_AD_DOWN_ARROW_UNFOCUSED = "nav_ad_down_arrow_unfocused.png";
    public final static String NAV_AD_UP_ARROW_FOCUSED = "nav_ad_up_arrow_focused.png";
    public final static String NAV_AD_UP_ARROW_UNFOCUSED = "nav_ad_up_arrow_unfocused.png";
    
    public final static String NAV_MY_LOCATION_ICON_WEAKGPS_UNFOCUSED = "nav_my_location_icon_weakgps_unfocused.png";
    
    public final static String NAV_TRAFFIC_ICON_UNFOCUSED = "nav_traffic_icon_unfocused.png";
    
    public final static String SEARCH_RESULTS_NUMBER_ELLIPSE_ICON_UNFOCUSED = "search_results_number_ellipse_icon_unfocused.png";
    
    public final static String SPEED_CAMERA_ICON_UNFOCUSED = "speed_camera_icon_unfocused.png";
    
    public final static String SEARCHBOX_ICON_SELECT_LOCATION_FOCUSED = "searchbox_icon_select_location_focused.png";
    public final static String SEARCHBOX_ICON_SELECT_LOCATION_UNFOCUSED = "searchbox_icon_select_location_unfocused.png";
    
    public final static String DRIVE_TO_BANNER_AIRPORTS_ICON_FOCUSED = "drive_to_banner_airports_icon_focused.png";
    public final static String DRIVE_TO_BANNER_AIRPORTS_ICON_UNFOCUSED = "drive_to_banner_airports_icon_unfocused.png";
    public final static String DRIVE_TO_BANNER_ARROW_ICON_FOCUSED = "drive_to_banner_arrow_icon_focused.png";
    public final static String DRIVE_TO_BANNER_ARROW_ICON_UNFOCUSED = "drive_to_banner_arrow_icon_unfocused.png";
    public final static String DRIVE_TO_BANNER_CONTACTS_ICON_FOCUSED = "drive_to_banner_contacts_icon_focused.png";
    public final static String DRIVE_TO_BANNER_CONTACTS_ICON_UNFOCUSED = "drive_to_banner_contacts_icon_unfocused.png";
    public final static String DRIVE_TO_BANNER_FAVORITES_ICON_FOCUSED = "drive_to_banner_favorites_icon_focused.png";
    public final static String DRIVE_TO_BANNER_FAVORITES_ICON_UNFOCUSED = "drive_to_banner_favorites_icon_unfocused.png";
    public final static String DRIVE_TO_BANNER_RECENTS_ICON_FOCUSED = "drive_to_banner_recents_icon_focused.png";
    public final static String DRIVE_TO_BANNER_RECENTS_ICON_UNFOCUSED = "drive_to_banner_recents_icon_unfocused.png";
    public final static String DRIVE_TO_BANNER_LINE_UNFOCUSED = "drive_to_banner_line_unfocused.png";
    public final static String DRIVE_TO_RECEIVE_ALARM_CIRCLE_UNFOCUSED = "drive_to_receive_alarm_circle_unfocused.png";
    public final static String DRIVE_TO_ROUTE_PICTURE_UNFOCUSED = "drive_to_route_picture_unfocused.png";


        
    public static String getSpecialImageFamily()
    {
        if(iconFamily != null)
            return iconFamily;
        
        int maxDisplaySize = Math.max(((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayHeight(),
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getDisplayWidth());
        if(maxDisplaySize <= 320)
        {
            iconFamily = ISpecialImageRes.FAMILY_MEDIUM;
            
            getStretchRatio();
        }
        else if(maxDisplaySize <= 480)
        {
            iconFamily = ISpecialImageRes.FAMILY_MEDIUM;
        }
        /*else if(maxDisplaySize <= 640)
        {
            iconFamily = ISpecialImageRes.FAMILY_HUGE;
        }*/
        else if(maxDisplaySize <= 854)
        {
            iconFamily = ISpecialImageRes.FAMILY_VAST;
            
            getStretchRatio();
        }
        else if(maxDisplaySize <= 1024)
        {
            iconFamily = ISpecialImageRes.FAMILY_VAST;
        }
        
        if( iconFamily == null )
        {
            iconFamily = ISpecialImageRes.FAMILY_VAST;
        }
        
        return iconFamily;
    }
    
    protected static void getStretchRatio() 
    {
        if(!isNeedStretch)
        {
            isNeedStretch = true;
            
            int w1 = AppConfigHelper.getDisplayWidth();
            int h1 = AppConfigHelper.getDisplayHeight();
            
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
        
        if(maxDisplaySize <= 480)
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
