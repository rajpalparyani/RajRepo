package com.telenav.log;

public interface ILogConstants 
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final String MARTS_PREFIX                        = "marts";

    public static final String LOG_LOCATION                        = "marts_loc";
    
    public static final String LOG_NAVIGATION                      = "marts_nav";
    
    public static final String LOG_COMM                            = "marts_comm";
    
    public static final String LOG_SUMMARY                         = "marts_summary";
    
    public static final String LOG_NETWORK_LOCATION                = "marts_netloc";
    
    public static final String LOG_LOCATION_CACHE                  = "marts_loccache";
    
    public static final String LOG_DSR                             = "marts_dsr";
    
    public static final String LOG_MVC                             = "marts_mvc";
    
    public static final String LOG_UI_EVENT                        = "marts_uiEvent";
    
    public static final String LOG_WEB_UI_EVENT                    = "marts_web_ui_event";
    
    public static final String LOG_AUDIO                           = "marts_audio";
    
    public static final String LOG_GPS                             = "marts_gps";
    
    public static final String LOG_UI_LAYOUT                       = "marts_ui_layout";
    
    /////////////////////////////////////// ////////////////////////////////////////////////////////////////

    public static final String RESUME_APPLICATION                  = "marts_resume_app";

    public static final String LAUNCH_APPLICATION                  = "marts_launch_app";
    
    public static final String EXIT_APPLICATION                    = "marts_exit_app";
    
    public static final String HIDE_TO_BACKGROUND                  = "marts_hide_to_background";
    
    public static final String BEFORE_START_GPS_SERVICE            = "marts_before_start_gps";
    
    public static final String AFTER_START_GPS_SERVICE             = "marts_after_start_gps";
    
    public static final String BEFORE_LOAD_LOCATION_CACHE          = "marts_before_load_loc_cache";
    
    public static final String AFTER_LOAD_LOCATION_CACHE           = "marts_after_load_loc_cache";
    
    public static final String DSR_BEFORE_INIT_RECORD_STREAMER     = "marts_before_init_record_streamer";

    public static final String DSR_START_RECORD_STREAMER           = "marts_start_record_streamer";
    
    public static final String DSR_FINISH_RECORDER                 = "marts_finish_recorder";
    
    public static final String DSR_ADD_FRAME_BUFFER                = "marts_add_frame_buffer";
    
    // save to network transaction log
    public static final String DSR_STREAM_HANDLER_RECEIVE          = "marts_dsr_stream_handler_receive";
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final String TAG_LOG_TYPE                        =  "log_type";
    
    public static final String LOG_TYPE_BEFORE_START_GPS_SERVICE   =  "marts_before_start_gps_service";
    
    public static final String LOG_TYPE_THREAD_RECORD              =  "marts_thread_record";
    
    public static final String TAG_TIMESTAMP                       =  "timestamp";
    
    public static final String TAG_THREAD                          =  "thread";
    
    public static final String TAG_THREAD_FILE                     =  "file";
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static String LOG_FILE_DIR                              =  "telenav/logs/"; 
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /*                                          event log type                                           */
    
    public static final String ON_KEY_DOWN_EVENT                   = "onKeyDown";
    
    public static final String ON_KEY_UP_EVENT                     = "onKeyUp";
    
    public static final String ON_TOUCH_EVENT                      = "onTouchEvent";
    
    public static final String ON_CLICK                            = "onClick";
    
    public static final String ON_TYPE_IN                          = "onTypeIn";
    
    public static final String ON_SCROLL_EVENT                     = "onScrollEvent";
    
    public static final String ON_MENU_ITEM_SELECTED               = "onMenuItemSelected";
    
    public static final String ON_MENU_CLOSED                      = "onMenuClosed";
    
    public static final String ON_KEY_PRE_IME                      = "onKeyPreIme";
    
    public static final String ON_MODULE_SWITCH                    = "onModuleSwitch";
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /*                      for Comm                                                                    */
    public static final String LOG_NETWORK_FINIHSED               =   "marts_network_finished";
    public static final String LOG_NETWORK_HANDLE_CHILD           =   "marts_handle_child";
    public static final String LOG_NETWORK_NEW_COMM               =   "marts_new_comm";
    public static final String LOG_BEFORE_EXECUTE_REQUEST_JOB     =   "marts_before_execute_job";
    
}
