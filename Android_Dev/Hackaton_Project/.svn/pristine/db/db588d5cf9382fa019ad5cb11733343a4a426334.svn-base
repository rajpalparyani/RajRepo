/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ImageDecorator.java
 *
 */
package com.telenav.ui;

import java.util.Hashtable;

import com.telenav.data.cache.ImageCacheManager;
import com.telenav.i18n.ResourceBundle;
import com.telenav.res.INinePatchImageRes;
import com.telenav.res.ISpecialImageRes;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.tnui.graphics.AbstractTnImage;

/**
 *@author bduan
 *@date Aug 19, 2010
 */
public class ImageDecorator implements ITnUiArgsDecorator
{
    public static Hashtable iconMapping = new Hashtable();
    public static ImageDecorator instance = new ImageDecorator();
    
//    public static TnUiArgAdapter IMG_CLOSE_BLUR = new TnUiArgAdapter(ISpecialImageRes.CLOSE_ICON_BLUE, instance);
//    public static TnUiArgAdapter IMG_CLOSE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.CLOSE_ICON_FOCUS, instance);

    public static TnUiArgAdapter IMG_MIC_ICON_LANDSCAPE_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.MIC_ICON_LANDSCAPE_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_MIC_ICON_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.MIC_ICON_UNFOCUS, instance);
    
    public static TnUiArgAdapter IMG_WAVE_ICON_LOUD_LEFT_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOUD_LEFT_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_LOUD_LEFT_FOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOUD_LEFT_FOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_LOUD_RIGHT_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOUD_RIGHT_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_LOUD_RIGHT_FOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOUD_RIGHT_FOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_LOW_LEFT_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOW_LEFT_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_LOW_LEFT_FOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOW_LEFT_FOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_LOW_RIGHT_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOW_RIGHT_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_LOW_RIGHT_FOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_LOW_RIGHT_FOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_NORMAL_LEFT_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_NORMAL_LEFT_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_NORMAL_LEFT_FOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_NORMAL_LEFT_FOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_NORMAL_RIGHT_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_NORMAL_RIGHT_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_WAVE_ICON_NORMAL_RIGHT_FOCUS = new TnUiArgAdapter(ISpecialImageRes.WAVE_ICON_NORMAL_RIGHT_FOCUS, instance);
    
    public static TnUiArgAdapter IMG_AC_ICON_HOME_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_HOME_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_HOME_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_HOME_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_AC_ICON_RECNET_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_RECNET_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_AC_ICON_FAVORITES_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_FAVORITES_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_AC_ICON_CONTACTS_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_CONTACTS_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_AC_ICON_BUSSINESSES_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_BUSINESSES_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_AC_ICON_AIRPORTS_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_AIRPORTS_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_AC_ICON_ARROW_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_ARROW_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_AC_ICON_ADDRESS_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_ADDRESS_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_PLACES_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_PLACES_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_AC_ICON_RECNET_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_RECNET_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_FAVORITES_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_FAVORITES_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_CONTACTS_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_CONTACTS_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_BUSSINESSES_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_BUSINESSES_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_AIRPORTS_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_AIRPORTS_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_ARROW_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_ARROW_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_ADDRESS_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_ADDRESS_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_PLACES_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_PLACES_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_LIST_ICON_ARROW_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_ICON_ARROW_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_ICON_ARROW_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.LIST_ICON_ARROW_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_FAV_ICON_FOLDER_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.LIST_FAVORITE_FOLDER_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_FAV_ICON_LIST_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.LIST_FAVORITE_ICON_UNFOCUS, instance);
//    public static TnUiArgAdapter IMG_UPSELL_IMAGE_LABEL = new TnUiArgAdapter(ISpecialImageRes.LABEL_UPSELL, instance);
    
//    public static TnUiArgAdapter IMG_AC_ICON_CONTACTS_FILTER_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.CONTACTS_FILTER_LIST_ICON_FOCUSED, instance);
//    public static TnUiArgAdapter IMG_AC_ICON_HOME_FILTER_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.HOME_FILTER_LIST_ICON_FOCUSED, instance);
//    public static TnUiArgAdapter IMG_AC_ICON_RECENT_FILTER_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.RECENT_FILTER_LIST_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_FAV_ICON_FOLDER_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_FAVORITE_FOLDER_FOCUSED, instance);
    public static TnUiArgAdapter IMG_FAV_ICON_LIST_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_FAVORITE_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_HISTORY_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_HISTORY_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_HISTORY_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_HISTORY_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_RECEIVED_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_RECEIVED_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_RECEIVED_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_RECEIVED_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_WORK_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_WORK_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_HOME_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_HOME_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_CURRENT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_CURRENT_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_AC_FIND_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_FIND_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_FIND_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_FIND_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_AC_BACKSPACE = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_BACKSPACE, instance);
    
    public static TnUiArgAdapter IMG_FAV_ADD_FAVORITE_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_ADD_FAVORITE_UNFOCUS, instance);
    
    public static TnUiArgAdapter IMG_FAV_ADD_FAVORITE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_ADD_FAVORITE_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_FAV_ADD_CATEGORY_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_ADD_CATEGORY_UNFOCUS, instance);
    
    public static TnUiArgAdapter IMG_FAV_ADD_CATEGORY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_ADD_CATEGORY_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_FAV_SYNC_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_SYNC_UNFOCUS, instance);
    
    public static TnUiArgAdapter IMG_FAV_SYNC_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_SYNC_FOCUSED, instance);
    
//    public static TnUiArgAdapter IMG_FAV_LIST_CATEGORY_ICON = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_LIST_CATEGORY_ICON, instance);
    
    public static TnUiArgAdapter IMG_FAV_ICON_EDIT_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_EDIT_UNFOCUS, instance);
    
    public static TnUiArgAdapter IMG_FAV_ICON_EDIT_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_EDIT_FOCUSED, instance);
    
//    public static TnUiArgAdapter IMG_FAV_EDIT_CATEGORY_SIGN = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_EDIT_FAVORITE_SIGN, instance);
    
    public static TnUiArgAdapter IMG_FAV_EDIT_FAVORITE_PIN = new TnUiArgAdapter(ISpecialImageRes.FAV_ICON_EDIT_FAVORITE_PIN, instance);
    
    public static TnUiArgAdapter IMG_CHECK_BOX_ON = new TnUiArgAdapter(ISpecialImageRes.CHECK_BOX_ICON_ON, instance);
    
    public static TnUiArgAdapter IMG_CHECK_BOX_OFF = new TnUiArgAdapter(ISpecialImageRes.CHECK_BOX_ICON_OFF, instance);
    
    public static TnUiArgAdapter IMG_CHECK_BOX_DISABLE = new TnUiArgAdapter(ISpecialImageRes.CHECK_BOX_ICON_DISABLE, instance);
    
    public static TnUiArgAdapter IMG_RADIO_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.RADIO_ICON_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_RADIO_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.RADIO_ICON_UNFOCUSED, instance);
    
//    public static TnUiArgAdapter IMG_TEST_PAGE_IMAGE = new TnUiArgAdapter(ISpecialImageRes.IMAGE_TEST_GALLERY, instance);
    
//    public static TnUiArgAdapter IMG_MY_LOCATION_OFF = new TnUiArgAdapter(ISpecialImageRes.MY_LOCATION_ICON_OFF , instance);
//    public static TnUiArgAdapter IMG_MY_LOCATION_ON = new TnUiArgAdapter(ISpecialImageRes.MY_LOCATION_ICON_ON , instance);
    
    public static TnUiArgAdapter IMG_CURRENT_LOCATION_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_CURRENT_LOCATION_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_CURRENT_LOCATION_NIGHT_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_CURRENT_LOCATION_NIGHT_UNFOCUSED, instance);    
    public static TnUiArgAdapter IMG_CURRENT_LOCATION_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_CURRENT_LOCATION_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_MAP_LAYER_ICON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_LAYER_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_LAYER_ICON_NIGHT_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_LAYER_ICON_NIGHT_UNFOCUSED, instance);    
    public static TnUiArgAdapter IMG_MAP_LAYER_ICON_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_LAYER_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_LAYER_ICON_DISABLE  = new TnUiArgAdapter(ISpecialImageRes.MAP_LAYER_ICON_DISABLE, instance);
    
    public static TnUiArgAdapter IMG_POI_BUBBLE_DETAIL_ARROW_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_DETAIL_ARROW_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_DETAIL_ARROW_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_DETAIL_ARROW_FOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_DRIVETO_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_DRIVETO_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_DRIVETO_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_DRIVETO_FOCUSED, instance); 
    
    public static TnUiArgAdapter IMG_POI_BUBBLE_CALL_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_CALL_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_CALL_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_CALL_FOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_CALL_DISABLED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_CALL_DISABLED, instance);
    
    public static TnUiArgAdapter IMG_POI_BUBBLE_SHARE_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_SHARE_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_SHARE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_SHARE_FOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_SHARE_DISABLED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_BUTTON_SHARE_DISABLED, instance);

    public static TnUiArgAdapter IMG_POI_BUBBLE_NEARBY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_NEARBY_FOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_NEARBY_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_NEARBY_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_NEARBY_DISABLED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_NEARBY_DISABLED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_SAVE_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_SAVE_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_SAVE_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_SAVE_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_SAVE_ADDED_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_SAVE_ADDED_ICON_UNFOCUSED, instance);
    
    
    public static TnUiArgAdapter IMG_DOWN_ARROW = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_DOWN_ARROW, instance);
    
    public static TnUiArgAdapter IMG_MAP_POI = new TnUiArgAdapter(ISpecialImageRes.MAP_POI, instance);
    public static TnUiArgAdapter IMG_SELECTED_MAP_POI = new TnUiArgAdapter(ISpecialImageRes.SELECTED_MAP_POI, instance);
    public static TnUiArgAdapter IMG_MAP_AD_POI = new TnUiArgAdapter(ISpecialImageRes.IMG_MAP_AD_POI, instance);

//    public static TnUiArgAdapter IMG_BIG_MAP_POI = new TnUiArgAdapter(ISpecialImageRes.BIG_MAP_POI, instance);
//    public static TnUiArgAdapter IMG_BIG_SELECTED_MAP_POI = new TnUiArgAdapter(ISpecialImageRes.BIG_SELECTED_MAP_POI, instance);
   
//    public static TnUiArgAdapter IMG_BIG_MAP_POI_WITH_AD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.BIG_MAP_POI_WITH_AD_UNFOCUSED, instance);
//    public static TnUiArgAdapter IMG_BIG_SELECTED_MAP_POI_WITH_AD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.BIG_SELECTED_MAP_POI_WITH_AD_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_MAP_ADDRESS = new TnUiArgAdapter(ISpecialImageRes.MAP_ADDRESS, instance);
    public static TnUiArgAdapter IMG_SELECTED_MAP_ADDRESS = new TnUiArgAdapter(ISpecialImageRes.SELECTED_MAP_ADDRESS, instance);
    
//    public static TnUiArgAdapter IMG_BIG_MAP_ADDRESS = new TnUiArgAdapter(ISpecialImageRes.BIG_MAP_ADDRESS, instance);
//    public static TnUiArgAdapter IMG_BIG_SELECTED_MAP_ADDRESS = new TnUiArgAdapter(ISpecialImageRes.BIG_SELECTED_MAP_ADDRESS, instance);
    
    
    public static TnUiArgAdapter IMG_PREV_POI_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.PREV_POI_UNFOCUS , instance);
    public static TnUiArgAdapter IMG_PREV_POI_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.PREV_POI_FOCUSED , instance);
    
    public static TnUiArgAdapter IMG_NEXT_POI_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.NEXT_POI_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_NEXT_POI_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.NEXT_POI_FOCUSED, instance);

    //for bottom nav bar icons
    public static TnUiArgAdapter IMG_BOTTOM_BAR_DASHBOARD_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_DASHBOARD_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_DASHBOARD_FOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_DASHBOARD_FOCUS, instance);
    
    public static TnUiArgAdapter IMG_BOTTOM_BAR_DRIVE_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_DRIVE_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_DRIVE_FOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_DRIVE_FOCUS, instance);
    
    public static TnUiArgAdapter IMG_BOTTOM_BAR_EXTRA_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_EXTRA_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_EXTRA_FOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_EXTRA_FOCUS, instance);
    
    public static TnUiArgAdapter IMG_BOTTOM_BAR_MAP_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_MAP_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_MAP_FOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_MAP_FOCUS, instance);
    
    //for top bar icons.
    public static TnUiArgAdapter IMG_TOP_BAR_LIST_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_LIST_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_TOP_BAR_LIST_FOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_LIST_FOCUS, instance);
    
    public static TnUiArgAdapter IMG_BUTTON_MIC_ICON = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_MIC_ICON_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_BUTTON_MIC_OFFLINE_ICON = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_MIC_OFFLINE_ICON_UNFOCUS, instance);
    
//    public static TnUiArgAdapter IMG_SEARCHBOX_CANCEL_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_CLEAR_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_SEARCHBOX_POILIST_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_POILIST_UNFOCUS, instance);
//    public static TnUiArgAdapter IMG_SEARCHBOX_SEARCH_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_SEARCH_UNFOCUS, instance);
    
//    public static TnUiArgAdapter IMG_ADD_FAV = new TnUiArgAdapter(ISpecialImageRes.ADD_FAV_UNFOCUS, instance);
//    public static TnUiArgAdapter IMG_ADD_CAT = new TnUiArgAdapter(ISpecialImageRes.ADD_CAT_UNFOCUS, instance);
//    public static TnUiArgAdapter IMG_EDIT = new TnUiArgAdapter(ISpecialImageRes.EDIT_UNFOCUS, instance);
    
    public static TnUiArgAdapter IMG_POI_BIG_STAR_FULL = new TnUiArgAdapter(ISpecialImageRes.POI_REVIEW_BIGSTAR_FULL, instance);

    public static TnUiArgAdapter IMG_POI_BIG_STAR_VACANCY = new TnUiArgAdapter(ISpecialImageRes.POI_REVIEW_BIGSTAR_VACANCY, instance);

    public static TnUiArgAdapter IMG_POI_MID_STAR_FULL = new TnUiArgAdapter(ISpecialImageRes.POI_REVIEW_MIDSTAR_FULL, instance);

    public static TnUiArgAdapter IMG_POI_MID_STAR_VACANCY = new TnUiArgAdapter(ISpecialImageRes.POI_REVIEW_MIDSTAR_VACANCY, instance);

    public static TnUiArgAdapter IMG_POI_SMALL_STAR_FULL = new TnUiArgAdapter(ISpecialImageRes.POI_REVIEW_SMALLSTAR_FULL, instance);

    public static TnUiArgAdapter IMG_POI_SMALL_STAR_VACANCY = new TnUiArgAdapter(ISpecialImageRes.POI_REVIEW_SMALLSTAR_VACANCY, instance);

    public static TnUiArgAdapter IMG_POI_SEARCH_RESULT_INDEX = new TnUiArgAdapter(ISpecialImageRes.POI_SEARCH_RESULT_INDEX_ICON,
            instance);

    //for big turn icon.
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_UTURN_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_UTURN_RIGHT_UNFOCUSED, instance); 
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_UTURN_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_UTURN_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_START_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_START_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_SLIDE_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_SLIDE_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_SLIDE_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_SLIDE_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_ON_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_ON_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_ON_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_ON_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_MERGE_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_MERGE_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_MERGE_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_MERGE_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_HARD_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_HARD_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_HARD_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_HARD_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_EXIT_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_EXIT_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_EXIT_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_EXIT_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_CONTINUE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_CONTINUE_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_BEAR_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_BEAR_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_BEAR_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_BEAR_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_AHEAD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_AHEAD_UNFOCUSED, instance);
    
    //for small turn icon.
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_AHEAD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_AHEAD_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_BEAR_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_BEAR_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_BEAR_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_BEAR_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_CONTINUE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_CONTINUE_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_EXIT_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_EXIT_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_EXIT_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_EXIT_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_HARD_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_HARD_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_HARD_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_HARD_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_MERGE_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_MERGE_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_MERGE_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_MERGE_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_ON_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_ON_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_ON_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_ON_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_SLIDE_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_SLIDE_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_SLIDE_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_SLIDE_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_START_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_START_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_UTURN_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_UTURN_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_UTURN_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_UTURN_RIGHT_UNFOCUSED, instance);
    
    // for tight turn icons.
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_AHEAD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_AHEAD_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_BEAR_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_BEAR_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_BEAR_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_BEAR_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_CONTINUE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_CONTINUE_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_EXIT_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_EXIT_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_EXIT_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_EXIT_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPLEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_EXIT_ROUNDABOUT_TOPRIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_HARD_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_HARD_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_HARD_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_HARD_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_MERGE_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_MERGE_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_MERGE_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_MERGE_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_ON_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_ON_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_ON_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_ON_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_ROUNDABOUT_ENTERTOLEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_ROUNDABOUT_ENTERTORIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_SLIDE_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_SLIDE_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_SLIDE_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_SLIDE_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_START_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_START_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_UTURN_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_UTURN_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TIGHT_TURN_ICON_UTURN_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TIGHT_TURN_ICON_UTURN_RIGHT_UNFOCUSED, instance);
    //for CN specified turn icons. 
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_ENTER_FERRY_UNFOCUSED       = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_ENTER_FERRY_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_EXIT_FERRY_UNFOCUSED        = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_EXIT_FERRY_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_STAY_MIDDLE_UNFOCUSED       = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_STAY_MIDDLE_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_VIA_POINT_AHEAD_UNFOCUSED   = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_VIA_POINT_AHEAD_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_VIA_POINT_LEFT_UNFOCUSED    = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_VIA_POINT_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_BIG_VIA_POINT_RIGHT_UNFOCUSED   = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_BIG_VIA_POINT_RIGHT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_ENTER_FERRY_UNFOCUSED     = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_ENTER_FERRY_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_EXIT_FERRY_UNFOCUSED      = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_EXIT_FERRY_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_STAY_MIDDLE_UNFOCUSED     = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_STAY_MIDDLE_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_VIA_POINT_AHEAD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_VIA_POINT_AHEAD_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_VIA_POINT_LEFT_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_VIA_POINT_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TURN_ICON_SMALL_VIA_POINT_RIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TURN_ICON_SMALL_VIA_POINT_RIGHT_UNFOCUSED, instance);
                                 
    public static TnUiArgAdapter IMG_MENU_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.MENU_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_DEAL_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DEAL_ICON_UNFOCUSED, instance);
                                 
                                 
//    public static TnUiArgAdapter GL_ROAD_TEXTURE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.GL_ROAD_TEXTURE_UNFOCUSED, instance);
//    public static TnUiArgAdapter GL_TRAFFIC_TEXTURE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.GL_TRAFFIC_TEXTURE_UNFOCUSED, instance);
//    public static TnUiArgAdapter GL_CAR_TEXTURE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.GL_CAR_TEXTURE_UNFOCUSED, instance);
//    public static TnUiArgAdapter GL_ROAD_SIGN_TEXTURE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.GL_ROAD_SIGN_TEXTURE_UNFOCUSED, instance);
//    public static TnUiArgAdapter GL_MAP_BAR_TEXTURE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.GL_MAP_BAR_TEXTURE_UNFOCUSED, instance);
//    public static TnUiArgAdapter GL_ARIAL_BLACK_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.GL_ARIAL_BLACK_UNFOCUSED, instance);
//    public static TnUiArgAdapter GL_SKY_DOME_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.GL_SKY_DOME_UNFOCUSED, instance);
    
    public static TnUiArgAdapter ORIGIN_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ORIGIN_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter DESTINATION_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DESTINATION_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter ORIGIN_ICON_SMALL_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ORIGIN_ICON_SMALL_UNFOCUSED, instance);
    public static TnUiArgAdapter DESTINATION_ICON_SMALL_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DESTINATION_ICON_SMALL_UNFOCUSED, instance);
    
//    public static TnUiArgAdapter SPLASH_GRAPHICS_UNFOCUSED_PORTRAIT = new TnUiArgAdapter(ISpecialImageRes.SPLASH_GRAPHICS_UNFOCUSED_PORTRAIT, instance);
//    public static TnUiArgAdapter SPLASH_GRAPHICS_UNFOCUSED_LANDSCAPE = new TnUiArgAdapter(ISpecialImageRes.SPLASH_GRAPHICS_UNFOCUSED_LANDSCAPE, instance);
//    public static TnUiArgAdapter SPLASH_YPMOBILE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SPLASH_YPMOBILE_UNFOCUSED, instance);
//    public static TnUiArgAdapter SPLASH_TELENAV_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SPLASH_TELENAV_UNFOCUSED, instance);
//    public static TnUiArgAdapter SPLASH_NAVTEQ_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SPLASH_NAVTEQ_UNFOCUSED, instance);
    
    public static TnUiArgAdapter PROFILE_ABOUT_SCOUT_LOGO_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.PROFILE_ABOUT_SCOUT_LOGO, instance);
    
    
    public static TnUiArgAdapter SUMMARY_CURRENT_SEGMENT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SUMMARY_CURRENT_SEGMENT_UNFOCUSED, instance);
    
    //Fot map layer menu list
    public static TnUiArgAdapter MAP_CAMERA_ICON_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_CAMERA_ICON_FOCUSED, instance);
    public static TnUiArgAdapter MAP_CAMERA_ICON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_CAMERA_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter MAP_CAMERA_ICON_DISABLED  = new TnUiArgAdapter(ISpecialImageRes.MAP_CAMERA_ICON_DISABLED, instance);
    public static TnUiArgAdapter MAP_SATELLITE_ICON_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_SATELLITE_ICON_FOCUSED, instance);
    public static TnUiArgAdapter MAP_SATELLITE_ICON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_SATELLITE_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter MAP_SATELLITE_ICON_DISABLED  = new TnUiArgAdapter(ISpecialImageRes.MAP_SATELLITE_ICON_DISABLED, instance);
    
    public static TnUiArgAdapter MAP_TRAFFIC_ICON_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_TRAFFIC_ICON_FOCUSED, instance);
    public static TnUiArgAdapter MAP_TRAFFIC_ICON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_TRAFFIC_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter MAP_TRAFFIC_ICON_DISABLED  = new TnUiArgAdapter(ISpecialImageRes.MAP_TRAFFIC_ICON_DISABLED, instance);
    public static TnUiArgAdapter MAP_SMALL_MAP_LAYER_ICON_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_SMALL_MAP_LAYER_ICON_FOCUSED, instance);
    public static TnUiArgAdapter MAP_SMALL_MAP_LAYER_ICON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.MAP_SMALL_MAP_LAYER_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter POI_LIST_DROP_DOWN_COMBOBOX_INDICATOR_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DROP_DOWN_COMBOBOX_INDICATOR_FOCUSED, instance);
    public static TnUiArgAdapter IMG_SPONSORED_INDEX_ICON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.SPONSORED_INDEX_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_SEARCHBOX_POILIST_FOCUS = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_POILIST_FOCUS, instance);
    
    public static TnUiArgAdapter IMG_LANE_ASSIST_LEFT_TURN_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_LEFT_TURN_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LANE_ASSIST_RIGHT_TURN_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_RIGHT_TURN_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LANE_ASSIST_AHEAD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_AHEAD_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LANE_ASSIST_LEFT_TURN_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_LEFT_TURN_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LANE_ASSIST_RIGHT_TURN_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_RIGHT_TURN_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LANE_ASSIST_AHEAD_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_AHEAD_FOCUSED, instance);
    
//    public static TnUiArgAdapter IMG_LANE_ASSIST_SMALL_LEFT_TURN_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_SMALL_LEFT_TURN_UNFOCUSED, instance);
//    public static TnUiArgAdapter IMG_LANE_ASSIST_SMALL_RIGHT_TURN_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_SMALL_RIGHT_TURN_UNFOCUSED, instance);
//    public static TnUiArgAdapter IMG_LANE_ASSIST_SMALL_AHEAD_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_SMALL_AHEAD_UNFOCUSED, instance);
//    public static TnUiArgAdapter IMG_LANE_ASSIST_SMALL_LEFT_TURN_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_SMALL_LEFT_TURN_FOCUSED, instance);
//    public static TnUiArgAdapter IMG_LANE_ASSIST_SMALL_RIGHT_TURN_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_SMALL_RIGHT_TURN_FOCUSED, instance);
//    public static TnUiArgAdapter IMG_LANE_ASSIST_SMALL_AHEAD_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LANE_ASSIST_SMALL_AHEAD_FOCUSED, instance);
    
    
    public static TnUiArgAdapter IMG_TRAFFIC_COLOR_BAR = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_COLOR_BAR, instance);
    public static TnUiArgAdapter IMG_TRAFFIC_COLOR_BAR_LANDSCAPE = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_COLOR_BAR_LANDSCAPE, instance);
    
    public static TnUiArgAdapter IMG_TRAFFIC_GRAY_BAR = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_GRAY_BAR, instance);
    public static TnUiArgAdapter IMG_TRAFFIC_GRAY_BAR_LANDSCAPE = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_GRAY_BAR_LANDSCAPE, instance);
    
    public static TnUiArgAdapter IMG_TRAFFIC_SLIDER = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_SLIDER, instance);
    
    public static TnUiArgAdapter IMG_TRAFFIC_TREND_UP = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_TREND_UP, instance);
    
    public static TnUiArgAdapter IMG_TRAFFIC_TREND_DOWN = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_TREND_DOWN, instance);
    
    
//    public static TnUiArgAdapter IMG_TRAFFIC_INCIDENT = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_INCIDENT, instance);
//    public static TnUiArgAdapter IMG_TRAFFIC_ACCIDENT = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_ACCIDENT, instance);
//    public static TnUiArgAdapter IMG_TRAFFIC_CONJESTION = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_CONJESTION, instance);
//    public static TnUiArgAdapter IMG_TRAFFIC_CONSTRUCTION = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_CONSTRUCTION, instance);
    
    public static TnUiArgAdapter IMG_LIST_CONTACTS_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_CONTACTS_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_CONTACTS_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_CONTACTS_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_CALL_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_CALL_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_CALL_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_CALL_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_AIRPORT_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_AIRPORT_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_AIRPORT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_AIRPORT_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_NAV_ZOOM_OUT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAV_ZOOM_OUT_ICON_UNFOCUSED , instance);    
    public static TnUiArgAdapter IMG_NAV_ZOOM_OUT_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAV_ZOOM_OUT_ICON_FOCUSED , instance); 
    public static TnUiArgAdapter IMG_NAV_ZOOM_IN_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAV_ZOOM_IN_ICON_UNFOCUSED , instance);
    public static TnUiArgAdapter IMG_NAV_ZOOM_IN_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAV_ZOOM_IN_ICON_FOCUSED , instance);
    public static TnUiArgAdapter IMG_NAV_ZOOM_OUT_ICON_DISABLE = new TnUiArgAdapter(ISpecialImageRes.NAV_ZOOM_OUT_ICON_DISABLE , instance);
    public static TnUiArgAdapter IMG_NAV_ZOOM_IN_ICON_DISABLE = new TnUiArgAdapter(ISpecialImageRes.NAV_ZOOM_IN_ICON_DISABLE , instance);
    
    public static TnUiArgAdapter IMG_MAP_ZOOM_IN_DAY_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ZOOM_IN_DAY_ICON_UNFOCUS , instance);
    public static TnUiArgAdapter IMG_MAP_ZOOM_IN_NIGHT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ZOOM_IN_NIGHT_ICON_UNFOCUS , instance); 
    public static TnUiArgAdapter IMG_MAP_ZOOM_IN_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ZOOM_IN_ICON_FOCUS , instance);
    public static TnUiArgAdapter IMG_MAP_ZOOM_IN_DAY_ICON_DISABLE = new TnUiArgAdapter(ISpecialImageRes.ZOOM_IN_DAY_ICON_DISABLE , instance);
    public static TnUiArgAdapter IMG_MAP_ZOOM_IN_NIGHT_ICON_DISABLE = new TnUiArgAdapter(ISpecialImageRes.ZOOM_IN_NIGHT_ICON_DISABLE , instance); 
    
    public static TnUiArgAdapter IMG_MAP_ZOOM_OUT_DAY_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ZOOM_OUT_DAY_ICON_UNFOCUS , instance);
    public static TnUiArgAdapter IMG_MAP_ZOOM_OUT_NIGHT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ZOOM_OUT_NIGHT_ICON_UNFOCUS , instance); 
    public static TnUiArgAdapter IMG_MAP_ZOOM_OUT_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ZOOM_OUT_ICON_FOCUS , instance);
    public static TnUiArgAdapter IMG_MAP_ZOOM_OUT_DAY_ICON_DISABLE = new TnUiArgAdapter(ISpecialImageRes.ZOOM_OUT_DAY_ICON_DISABLE , instance);
    public static TnUiArgAdapter IMG_MAP_ZOOM_OUT_NIGHT_ICON_DISABLE = new TnUiArgAdapter(ISpecialImageRes.ZOOM_OUT_NIGHT_ICON_DISABLE , instance); 
    
        
    public static TnUiArgAdapter IMG_MOVING_MAP_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.MOVING_MAP_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_MOVING_MAP_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.MOVING_MAP_ICON_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_MAP_POI_BAR_LEFT_ARROW_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_POI_BAR_LEFT_ARROW_FOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_POI_BAR_LEFT_ARROW_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_POI_BAR_LEFT_ARROW_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_POI_BAR_LEFT_ARROW_DISABLED = new TnUiArgAdapter(ISpecialImageRes.MAP_POI_BAR_LEFT_ARROW_DISABLED, instance);
    public static TnUiArgAdapter IMG_MAP_POI_BAR_RIGHT_ARROW_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_POI_BAR_RIGHT_ARROW_FOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_POI_BAR_RIGHT_ARROW_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_POI_BAR_RIGHT_ARROW_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_POI_BAR_RIGHT_ARROW_DISABLED = new TnUiArgAdapter(ISpecialImageRes.MAP_POI_BAR_RIGHT_ARROW_DISABLED, instance);
    
    
    public static TnUiArgAdapter IMG_BOTTOM_BAR_PLACES_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_PLACES_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_PLACES_FOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_PLACES_FOCUS, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_PLACES_DISABLE = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_PLACES_DISABLE, instance);

    public static TnUiArgAdapter IMG_BOTTOM_BAR_END_TRIP_UNFOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_END_TRIP_UNFOCUS, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_END_TRIP_FOCUS = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_END_TRIP_FOCUS, instance);
    
    public static TnUiArgAdapter IMG_SUMMARY_ITEM_SOUND_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SUMMARY_ITEM_SOUND_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_TRAFFIC_ALERT_SMALL_YELLOW_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_ALERT_SMALL_YELLOW_ICON_UNFOCUSED, instance);
        
    public static TnUiArgAdapter IMG_TRAFFIC_ALERT_SMALL_ORANGE_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_ALERT_SMALL_ORANGE_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TRAFFIC_ALERT_SMALL_RED_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_ALERT_SMALL_RED_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TRAFFIC_ALERT_SMALL_YELLOW_EMPTY_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_ALERT_SMALL_YELLOW_EMPTY_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TRAFFIC_ALERT_SMALL_ORANGE_EMPTY_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_ALERT_SMALL_ORANGE_EMPTY_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_TRAFFIC_ALERT_SMALL_RED_EMPTY_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.TRAFFIC_ALERT_SMALL_RED_EMPTY_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_ICON_DIRECTIONS_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_ICON_DIRECTIONS_FOCUSED, instance);
    public static TnUiArgAdapter IMG_BOTTOM_BAR_ICON_DIRECTIONS_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_ICON_DIRECTIONS_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_FIT_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_FIT_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_FIT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_FIT_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_MAP_FIT_ICON_NIGHT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_FIT_ICON_NIGHT_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_POI_LIST_MAP_BUTTON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_LIST_MAP_BUTTON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_LIST_MAP_BUTTON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_LIST_MAP_BUTTON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_CREATE_CATEGORY_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_CREATE_CATEGORY_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_LIST_CREATE_CATEGORY_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.LIST_CREATE_CATEGORY_ICON_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_DIRECTIONS_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.DIRECTIONS_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_DIRECTIONS_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DIRECTIONS_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_NAVIGATE_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAVIGATE_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_NAVIGATE_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAVIGATE_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_COMPASS_E = new TnUiArgAdapter(ISpecialImageRes.COMPASS_E, instance);
    public static TnUiArgAdapter IMG_COMPASS_N = new TnUiArgAdapter(ISpecialImageRes.COMPASS_N, instance);
    public static TnUiArgAdapter IMG_COMPASS_NE = new TnUiArgAdapter(ISpecialImageRes.COMPASS_NE, instance);
    public static TnUiArgAdapter IMG_COMPASS_NW = new TnUiArgAdapter(ISpecialImageRes.COMPASS_NW, instance);
    public static TnUiArgAdapter IMG_COMPASS_S = new TnUiArgAdapter(ISpecialImageRes.COMPASS_S, instance);
    public static TnUiArgAdapter IMG_COMPASS_SE = new TnUiArgAdapter(ISpecialImageRes.COMPASS_SE, instance);
    public static TnUiArgAdapter IMG_COMPASS_SW = new TnUiArgAdapter(ISpecialImageRes.COMPASS_SW, instance);
    public static TnUiArgAdapter IMG_COMPASS_W = new TnUiArgAdapter(ISpecialImageRes.COMPASS_W, instance);
    public static TnUiArgAdapter IMG_COMPASS_2D = new TnUiArgAdapter(ISpecialImageRes.COMPASS_2D, instance);
    
    public static TnUiArgAdapter IMG_RECEIVE_ALARM_CIRCLE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.RECEIVE_ALARM_CIRCLE_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_RECEIVE_ALARM_SMALL_CIRCLE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.RECEIVE_ALARM_SMALL_CIRCLE_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_SHADOW_TOP_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SHADOW_TOP_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_SHADOW_BOTTOM_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SHADOW_BOTTOM_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_SHADOW_LEFT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SHADOW_LEFT_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_BOTTOMBAR_TOP_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.BOTTOM_BAR_TOP_UNFOCUSED, instance);
    
    
    public static TnUiArgAdapter IMG_DAY_LOGO_ON_MAP = new TnUiArgAdapter(ISpecialImageRes.DAY_LOGO_ON_MAP, instance);
    public static TnUiArgAdapter IMG_NIGHT_LOGO_ON_MAP = new TnUiArgAdapter(ISpecialImageRes.NIGHT_LOGO_ON_MAP, instance);
    
    public static TnUiArgAdapter ROUTE_CURRENT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_CURRENT_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter ROUTE_NEW_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_NEW_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_FAVORITES_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAVORITES_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_FAVORITES_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAVORITES_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_FAVORITES_ADD_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAVORITES_ADD_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_FAVORITES_ADD_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.FAVORITES_ADD_ICON_UNFOCUSED, instance);
    
//    public static TnUiArgAdapter IMG_NAV_MY_LOCATION_ICON_WEAKGPS_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAV_MY_LOCATION_ICON_WEAKGPS_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_NAV_TRAFFIC_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAV_TRAFFIC_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_NAV_TRAFFIC_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.NAV_TRAFFIC_ICON_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_SEARCH_RESULTS_NUMBER_ELLIPSE_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SEARCH_RESULTS_NUMBER_ELLIPSE_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_SPEED_CAMERA_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SPEED_CAMERA_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_SPEED_TRAP_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SPEED_TRAP_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_AC_ICON_CURRENT_LOCATION_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_CURRENT_LOCATION_FOCUSED, instance);
    public static TnUiArgAdapter IMG_AC_ICON_CURRENT_LOCATION_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_ICON_CURRENT_LOCATION_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_AC_MAIN_REGION_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.AC_MAIN_REGION_UNFOCUSED, instance);

    public static TnUiArgAdapter SEARCHBOX_ICON_SELECT_LOCATION_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_ICON_SELECT_LOCATION_UNFOCUSED, instance);
    public static TnUiArgAdapter SEARCHBOX_ICON_SELECT_LOCATION_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_ICON_SELECT_LOCATION_FOCUSED, instance);
    
    public static TnUiArgAdapter IMG_INCIDENT_ACCIDENT = new  TnUiArgAdapter(ISpecialImageRes.INCIDENT_ACCIDENT , instance);    
    public static TnUiArgAdapter IMG_INCIDENT_CONGESTION = new  TnUiArgAdapter(ISpecialImageRes.INCIDENT_CONGESTION , instance);    
    public static TnUiArgAdapter IMG_INCIDENT_CONSTRUCTION = new  TnUiArgAdapter(ISpecialImageRes.INCIDENT_CONSTRUCTION , instance);    
    public static TnUiArgAdapter IMG_INCIDENT_DEFAULT = new  TnUiArgAdapter(ISpecialImageRes.INCIDENT_DEFAULT , instance);
    public static TnUiArgAdapter IMG_TRAFFIC_CAMERA_ON_NAV = new  TnUiArgAdapter(ISpecialImageRes.TRAFFIC_CAMERA_ON_NAV , instance);
    public static TnUiArgAdapter IMG_SPEED_TRAP_ON_NAV = new  TnUiArgAdapter(ISpecialImageRes.SPEED_TRAP_ON_NAV , instance);
    
    public static TnUiArgAdapter DASHBOARD_ARROW_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_ARROW_ICON_FOCUSED, instance);
    public static TnUiArgAdapter DASHBOARD_ARROW_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_ARROW_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter DASHBOARD_ARROW_ICON_GRAY_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_ARROW_ICON_GRAY_UNFOCUSED, instance);
    public static TnUiArgAdapter DASHBOARD_TIME_SPLIT_CIRCLE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_TIME_SPLIT_CIRCLE_UNFOCUSED, instance);
    public static TnUiArgAdapter DASHBOARD_RESUME_TRIP_CLOSE_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_RESUME_TRIP_CLOSE_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter DASHBOARD_RESUME_TRIP_CLOSE_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_RESUME_TRIP_CLOSE_ICON_FOCUSED, instance);

//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_AIRPORT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_AIRPORT_UNFOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_AIRPORT_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_AIRPORT_FOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_CONTACTS_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_CONTACTS_UNFOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_CONTACTS_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_CONTACTS_FOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_FAVORITES_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_FAVORITES_UNFOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_FAVORITES_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_FAVORITES_FOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_RECENTS_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_RECENTS_UNFOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ICON_RECENTS_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ICON_RECENTS_FOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ARROW_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ARROW_UNFOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ARROW_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ARROW_FOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_ROUTE_PICTURE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_ROUTE_PICTURE_UNFOCUSED, instance);
//    public static TnUiArgAdapter ATT_DASHBOARD_MAP_OFFLINE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ATT_DASHBOARD_MAP_OFFLINE_UNFOCUSED, instance);
    
//    public static TnUiArgAdapter POI_DETAIL_FEEDBACK_BUTTON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_DETAIL_FEEDBACK_BUTTON_FOCUSED, instance);
    public static TnUiArgAdapter POI_DETAIL_FEEDBACK_BUTTON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_DETAIL_FEEDBACK_BUTTON_UNFOCUSED, instance);

    public static TnUiArgAdapter IMG_POI_BUBBLE_TITLE_CAR_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_TITLE_CAR_ICON_FOCUSED, instance);
    public static TnUiArgAdapter IMG_POI_BUBBLE_TITLE_CAR_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.POI_BUBBLE_TITLE_CAR_ICON_UNFOCUSED, instance);
    
    public static TnUiArgAdapter IMG_FTUE_LANDING_LOGO = new TnUiArgAdapter(ISpecialImageRes.FTUE_LANDING_LOGO, instance);
    public static TnUiArgAdapter IMG_FTUE_01_DRIVE = new TnUiArgAdapter(ISpecialImageRes.FTUE_01_DRIVE, instance);
    public static TnUiArgAdapter IMG_FTUE_02_FAVS = new TnUiArgAdapter(ISpecialImageRes.FTUE_02_FAVS, instance);
    public static TnUiArgAdapter IMG_FTUE_03_PLACES = new TnUiArgAdapter(ISpecialImageRes.FTUE_03_PLACES, instance);
    public static TnUiArgAdapter IMG_FTUE_04_TRAFFIC = new TnUiArgAdapter(ISpecialImageRes.FTUE_04_TRAFFIC, instance);
    public static TnUiArgAdapter IMG_FTUE_SUCCESS = new TnUiArgAdapter(ISpecialImageRes.FTUE_SUCCESS, instance);
    
    public static TnUiArgAdapter PROFILE_SWITCH_OFF_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.PROFILE_SWITCH_OFF_FOCUSED, instance);
    public static TnUiArgAdapter PROFILE_SWITCH_ON_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.PROFILE_SWITCH_ON_FOCUSED , instance);
    public static TnUiArgAdapter PROFILE_SWITCH_OFF_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.PROFILE_SWITCH_OFF_UNFOCUSED, instance);
    public static TnUiArgAdapter PROFILE_SWITCH_ON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.PROFILE_SWITCH_ON_UNFOCUSED , instance);
    public static TnUiArgAdapter PROFILE_SWITCH_BG  = new TnUiArgAdapter(ISpecialImageRes.PROFILE_SWITCH_BG , instance);

    public static TnUiArgAdapter US_CENTER  = new TnUiArgAdapter(ISpecialImageRes.US_CENTER , instance);
    public static TnUiArgAdapter US_EAST  = new TnUiArgAdapter(ISpecialImageRes.US_EAST , instance);
    public static TnUiArgAdapter US_WEST  = new TnUiArgAdapter(ISpecialImageRes.US_WEST , instance);
    public static TnUiArgAdapter TAB_FINISHED_ICON  = new TnUiArgAdapter(ISpecialImageRes.TAB_FINISHED_ICON , instance);
    
    public static TnUiArgAdapter IMG_FTUE_DOT_ICON_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.FTUE_DOT_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter IMG_FTUE_DOT_ICON_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.FTUE_DOT_ICON_FOCUSED, instance);
    
    //add for carconnect
    public static TnUiArgAdapter LOCKOUT_IMAGE = new TnUiArgAdapter(ISpecialImageRes.LOCKOUT_IMAGE, instance);
    public static TnUiArgAdapter LOCKOUT_LOGO = new TnUiArgAdapter(ISpecialImageRes.LOCKOUT_LOGO, instance);
    public static TnUiArgAdapter LOCKOUT_BG_IMAGE = new TnUiArgAdapter(ISpecialImageRes.LOCKOUT_BG_IMAGE, instance);
    public static TnUiArgAdapter LOCKOUT_BG_IMAGE_LANDSCAPE = new TnUiArgAdapter(ISpecialImageRes.LOCKOUT_BG_IMAGE_LANDSCAPE, instance);
    
    public static TnUiArgAdapter IMG_MAP_2D_DOT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.MAP_2D_DOT, instance);    

    //car icon
    public static TnUiArgAdapter CAR_ARROW = new TnUiArgAdapter(ISpecialImageRes.CAR_ARROW, instance);
    public static TnUiArgAdapter CAR_SPORTS = new TnUiArgAdapter(ISpecialImageRes.CAR_SPORTS, instance);
    public static TnUiArgAdapter CAR_CLASSIC = new TnUiArgAdapter(ISpecialImageRes.CAR_CLASSIC, instance);
    public static TnUiArgAdapter CAR_COMPACT = new TnUiArgAdapter(ISpecialImageRes.CAR_COMPACT, instance);
    public static TnUiArgAdapter CAR_CONVERTIBLE = new TnUiArgAdapter(ISpecialImageRes.CAR_CONVERTIBLE, instance);
    public static TnUiArgAdapter CAR_BATTLE_TANK = new TnUiArgAdapter(ISpecialImageRes.CAR_BATTLE_TANK, instance);
    public static TnUiArgAdapter CAR_MINIVAN = new TnUiArgAdapter(ISpecialImageRes.CAR_MINIVAN, instance);
    public static TnUiArgAdapter CAR_MONSTER_TRUCK = new TnUiArgAdapter(ISpecialImageRes.CAR_MONSTER_TRUCK, instance);
    public static TnUiArgAdapter CAR_MOTORCYCLE = new TnUiArgAdapter(ISpecialImageRes.CAR_MOTORCYCLE, instance);
    public static TnUiArgAdapter CAR_MUSCLE = new TnUiArgAdapter(ISpecialImageRes.CAR_MUSCLE, instance);
    public static TnUiArgAdapter CAR_OLD_SCHOOL = new TnUiArgAdapter(ISpecialImageRes.CAR_OLD_SCHOOL, instance);
    public static TnUiArgAdapter CAR_SUV = new TnUiArgAdapter(ISpecialImageRes.CAR_SUV, instance);
    public static TnUiArgAdapter CAR_SPACE_FIGHTER = new TnUiArgAdapter(ISpecialImageRes.CAR_SPACE_FIGHTER, instance);
    
    public static TnUiArgAdapter SEARCHBOX_MIC_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_MIC_UNFOCUSED, instance);
    public static TnUiArgAdapter SEARCHBOX_MIC_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_MIC_FOCUSED, instance);
    public static TnUiArgAdapter SEARCHBOX_MIC_DISABLED  = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_MIC_DISABLED, instance);
    public static TnUiArgAdapter DASHBOARD_FAVORITE_ICON_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_FAVORITE_ICON_FOCUSED, instance);
    public static TnUiArgAdapter DASHBOARD_FAVORITE_ICON_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DASHBOARD_FAVORITE_ICON_UNFOCUSED, instance);
    public static TnUiArgAdapter SEARCHBOX_ICON_HINT_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_ICON_HINT_UNFOCUSED, instance);
    
    //Upsell
    public static TnUiArgAdapter  ICON_NO_ADS_FOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_NO_ADS_FOCUSED, instance);
    public static TnUiArgAdapter  ICON_NO_ADS_UNFOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_NO_ADS_UNFOCUSED, instance);
    public static TnUiArgAdapter  ICON_ALWAYS_THERE_FOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_ALWAYS_THERE_FOCUSED, instance);
    public static TnUiArgAdapter  ICON_ALWAYS_THERE_UNFOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_ALWAYS_THERE_UNFOCUSED, instance);
    
    public static TnUiArgAdapter  ICON_REAL_TIME_TRAFFIC_FOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_REAL_TIME_TRAFFIC_FOCUSED, instance);
    public static TnUiArgAdapter  ICON_REAL_TIME_TRAFFIC_UNFOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_REAL_TIME_TRAFFIC_UNFOCUSED, instance);
    public static TnUiArgAdapter  ICON_SPEED_CAMERA_ALERTS_FOCUSED=new TnUiArgAdapter(ISpecialImageRes.ICON_RED_LIGHT_CAMERA_ALERTS_FOCUSED, instance);
    public static TnUiArgAdapter  ICON_SPEED_CAMERA_ALERTS_UNFOCUSED=new TnUiArgAdapter(ISpecialImageRes.ICON_RED_LIGHT_CAMERA_ALERTS_UNFOCUSED, instance);
//    public static TnUiArgAdapter  ICON_SPEED_TRAPS_FOCUSED=new TnUiArgAdapter(ISpecialImageRes.ICON_SPEED_TRAPS_FOCUSED, instance);
//    public static TnUiArgAdapter  ICON_SPEED_TRAPS_UNFOCUSED=new TnUiArgAdapter(ISpecialImageRes.ICON_SPEED_TRAPS_UNFOCUSED, instance);
//    public static TnUiArgAdapter  ICON_POSTED_SPEED_LIMITS_FOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_POSTED_SPEED_LIMITS_FOCUSED, instance);
//    public static TnUiArgAdapter  ICON_POSTED_SPEED_LIMITS_UNFOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_POSTED_SPEED_LIMITS_UNFOCUSED, instance);
    public static TnUiArgAdapter  ICON_LANE_ASSIST_FOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_LANE_ASSIST_FOCUSED, instance);
    public static TnUiArgAdapter  ICON_LANE_ASSIST_UNFOCUSED= new TnUiArgAdapter(ISpecialImageRes.ICON_LANE_ASSIST_UNFOCUSED, instance);
    public static TnUiArgAdapter  ICON_CAR_CONNECT_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ICON_CAR_CONNECT_FOCUSED, instance);
    public static TnUiArgAdapter  ICON_CAR_CONNECT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ICON_CAR_CONNECT_UNFOCUSED, instance);

    public static TnUiArgAdapter  GALERY_ALWAYS_THERE = new TnUiArgAdapter(ISpecialImageRes.GALERY_ALWAYS_THERE, instance);
    public static TnUiArgAdapter  GALERY_NO_ADS = new TnUiArgAdapter(ISpecialImageRes.GALERY_NO_ADS, instance);
    public static TnUiArgAdapter  GALERY_REAL_TIME_TRAFFIC= new TnUiArgAdapter(ISpecialImageRes.GALERY_REAL_TIME_TRAFFIC, instance);
    public static TnUiArgAdapter  GALERY_SPEED_CAMERA_ALERTS=new TnUiArgAdapter(ISpecialImageRes.GALERY_RED_LIGHT_CAMERA_ALERTS, instance);
    public static TnUiArgAdapter  GALERY_LANE_ASSIST=new TnUiArgAdapter(ISpecialImageRes.GALERY_LANE_ASSIST, instance);
    public static TnUiArgAdapter  GALERY_CAR_CONNECT=new TnUiArgAdapter(ISpecialImageRes.GALERY_CAR_CONNECT, instance);
    
    public static TnUiArgAdapter  UPSELL_ARROW_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.UPSELL_ARROW_UNFOCUSED, instance);
    public static TnUiArgAdapter  UPSELL_ARROW_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.UPSELL_ARROW_FOCUSED, instance);
    
    //portrait route planning
    public static TnUiArgAdapter  ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_FOCUSED =  new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_FOCUSED =  new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2A_PANEL_GREEN_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2A_PANEL_GREEN_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2B_PANEL_BLUE_LIST_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2B_PANEL_GREEN_LIST_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2B_PANEL_BLUE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2B_PANEL_BLUE_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3A_PANEL_GREEN_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3A_PANEL_GREEN_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3B_PANEL_BLUE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3B_PANEL_BLUE_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3C_PANEL_RED_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3C_PANEL_RED_UNFOCUSED, instance);
    
    //landscape route planning
    public static TnUiArgAdapter  ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED =  new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED =  new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2A_PANEL_GREEN_LANDSCAPE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2A_PANEL_GREEN_LANDSCAPE_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2B_PANEL_BLUE_LIST_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2B_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_2B_PANEL_BLUE_LANDSCAPE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_2B_PANEL_BLUE_LANDSCAPE_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3A_PANEL_GREEN_LANDSCAPE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3A_PANEL_GREEN_LANDSCAPE_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3B_PANEL_BLUE_LANDSCAPE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3B_PANEL_BLUE_LANDSCAPE_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_LANDSCAPE_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_LANDSCAPE_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_3C_PANEL_RED_LANDSCAPE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_3C_PANEL_RED_LANDSCAPE_UNFOCUSED, instance);
    
    //routeplanning checkbox
    public static TnUiArgAdapter  ROUTE_PLANNING_CHECKBOX_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_CHECKBOX_FOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_CHECKBOX_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_CHECKBOX_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_CHECKBOX_DISABLED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_CHECKBOX_DISABLED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_NAVIGAGE_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_NAVIGAGE_UNFOCUSED, instance);
    public static TnUiArgAdapter  ROUTE_PLANNING_STYLE_SETTING = new TnUiArgAdapter(ISpecialImageRes.ROUTE_PLANNING_STYLE_SETTING, instance);
    
    public static TnUiArgAdapter EDIT_HOME_DELETE  = new TnUiArgAdapter(ISpecialImageRes.EDIT_HOME_DELETE , instance);
    public static TnUiArgAdapter EDIT_HOME_ADD_CONTACT  = new TnUiArgAdapter(ISpecialImageRes.EDIT_HOME_ADD_CONTACT , instance);
    
    public static TnUiArgAdapter HOME_SETTING_TITLE_DELETE_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.HOME_SETTING_TITLE_DELETE_UNFOCUSED , instance);
    
    public static TnUiArgAdapter IMG_SEARCHBOX_ICON_CONTACT_FOCUSED = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_ICON_CONTACT_FOCUSED, instance);
    public static TnUiArgAdapter IMG_SEARCHBOX_ICON_CONTACT_UNFOCUSED = new TnUiArgAdapter(ISpecialImageRes.SEARCHBOX_ICON_CONTACT_UNFOCUSED, instance);
 

    
    //DWF
    public static TnUiArgAdapter DWF_MAP_OPEN_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_OPEN_UNFOCUSED , instance);
    public static TnUiArgAdapter DWF_MAP_OPEN_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_OPEN_FOCUSED , instance);
    public static TnUiArgAdapter DWF_MAP_FOLD_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_FOLD_UNFOCUSED , instance);
    public static TnUiArgAdapter DWF_MAP_FOLD_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_FOLD_FOCUSED , instance);
    public static TnUiArgAdapter DWF_MAP_OPEN_LANDSCAPE_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_OPEN_LANDSCAPE_UNFOCUSED , instance);
    public static TnUiArgAdapter DWF_MAP_OPEN_LANDSCAPE_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_OPEN_LANDSCAPE_FOCUSED , instance);
    public static TnUiArgAdapter DWF_MAP_FOLD_LANDSCAPE_UNFOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_FOLD_LANDSCAPE_UNFOCUSED , instance);
    public static TnUiArgAdapter DWF_MAP_FOLD_LANDSCAPE_FOCUSED  = new TnUiArgAdapter(ISpecialImageRes.DWF_MAP_FOLD_LANDSCAPE_FOCUSED , instance);
    
    public Object decorate(TnUiArgAdapter args)
    {
        String key = (String) args.getKey();
        AbstractTnImage image = (AbstractTnImage) ImageCacheManager.getInstance().getImageCache().get(key);
        
        boolean isStretchable = true;
        String imageFamily = ISpecialImageRes.getSpecialImageFamily();
        if(key != null && key.indexOf("native_") != -1)
        {
            isStretchable = false;
            imageFamily = INinePatchImageRes.FAMILY_NINE_PATCH;
        }
        
        if (image == null)
        {
            ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
            byte[] imageData = bundle.getGenericImage(key, imageFamily);
            if (imageData != null)
            {
                image = AbstractTnGraphicsHelper.getInstance().createImage(imageData);
                
                image.setIsStretchable(isStretchable);
                
                image = checkStretchImage(image);
                
                ImageCacheManager.getInstance().getImageCache().put(key, image);
            }
        }

        return image;
    }

    protected static AbstractTnImage checkStretchImage(AbstractTnImage image)
    {
        if (ISpecialImageRes.isNeedStretch && image != null && image.isStretchable())
        {
            image = doStretchImage(image);
        }
        
        return image;
    }
    
    private static AbstractTnImage doStretchImage(AbstractTnImage image)
    {
    	int w = image.getWidth() * ISpecialImageRes.numerator / ISpecialImageRes.denominator;
        int h = image.getHeight() * ISpecialImageRes.numerator / ISpecialImageRes.denominator;
        
        return image.createScaledImage(w > 0 ? w : 1, h > 0 ? h : 1);
    }
    
    public static AbstractTnImage stretchSingleImage(AbstractTnImage image)
    {
    	ISpecialImageRes.computeStretchRatio();
    	return doStretchImage(image);
    }
}
