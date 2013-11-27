package com.telenav.sdk.maitai;

/**
 * 
 * The interface for MaiTai
 * 
 * @author zhdong@telenav.cn
 * 
 */
public interface IMaiTai
{
    //for android platform
    public static final String KEY_SMS_MESSAGE          = "sms_message";
    public static final String KEY_IS_COMMUTE_ALERT     = "is_commute_alert";
    public static final String KEY_ADDRESS_FIRST_LINE   = "address_first_line";
    public static final String KEY_APARTMENT            = "apartment";
    public static final String KEY_ADDRESS_CITY         = "address_city";
    public static final String KEY_ADDRESS_STATE        = "address_state";
    public static final String KEY_ADDRESS_ZIP          = "address_zip_code";
    public static final String KEY_ADDRESS_LABEL        = "address_label";
    public static final String KEY_ADDRESS_COUNTRY      = "address_country";
    public static final String KEY_ADDRESS_LAT          = "address_latitude";
    public static final String KEY_ADDRESS_LON          = "address_longitude";
    public static final String KEY_CONVENIENCE_FLAG     = "convenience_flag";
    public static final String KEY_SELECTED_MENU_ITEM   = "selected_menu_item";
    public static final String KEY_SELECTED_ADDRESS     = "selected_address";
    public static final String KEY_ONE_BOX_ADDRESS      = "one_box_address";
    public static final String KEY_SEARCH_ITEM          = "search_item";
    public static final String KEY_IS_MAITAI_CALL       = "maitai";
    public static final String KEY_MAITAI_NEED_LOGIN    = "maitai_need_login";
    public static final String KEY_DWF_SESSION_ID       = "dwf_session_id";
    public static final String KEY_DWF_USER_KEY         = "dwf_user_key";
    public static final String KEY_DWF_USER_ID          = "dwf_user_id";
    public static final String KEY_DWF_TINY_URL         = "dwf_tiny_url";
    public static final String KEY_DWF_ADDRESS_FORMATDT = "dwf_address_formatted_dt";
    public static final String KEY_DWF_EXIT_APP         = "dwf_exit_app";
    
    public final static String MENU_ITEM_DRIVE_TO       = "DriveTo";
    public final static String MENU_ITEM_DIRECTIONS     = "Directions";
    public final static String MENU_ITEM_BIZ_VALUE      = "BIZ";
    public final static String MENU_ITEM_MAP_VALUE      = "MAP";
    public final static String MENU_ITEM_SHARE_ADDRESS  = "SHA";
    public final static String CONVENIENCE_DSR_POI      = "DSR_POI";
    //for android platform


    public boolean isFromMaiTai();

    public boolean isMaiTaiNeedLogin();

//    public String getOneBoxAddress();

//    public String getOriginalAddress();
    
    //public String getDeveloperKey();

    /**
     * Applicable For RIM only
     * 
     * @return
     */
    public int getCallingPID();

    /**
     * Applicable For RIM only
     * 
     * @return
     */
    public long getCallingGuid();

    public String getSearchTerm();
    
    public String getSelectedAction();

}
