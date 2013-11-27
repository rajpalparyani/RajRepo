/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SecretKeyController.java
 *
 */
package com.telenav.module.entry.secretkey;

import com.telenav.app.TeleNavDelegate;
import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.StateMachine;

/**
 *@author jshchen 
 *@date 2010-8-19
 */
public class SecretKeyController extends AbstractCommonController implements ISecretKeyConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        //init
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_INIT },
        
        //go to the secret items
        { STATE_INIT, CMD_CHANGE_URL, STATE_URL_GROUP, ACTION_URL_GROUP },
        
        //get locator from server
        { STATE_URL_GROUP, CMD_COMMON_OK, STATE_GETTING_LOCATOR, ACTION_CHANGE_HOST},
        
        { STATE_URL_GROUP, CMD_SHOW_SET_URL_SCREEN, STATE_SHOW_SET_URL_SCREEN, ACTION_NONE},
        
        { STATE_SHOW_SET_URL_SCREEN, CMD_COMMON_OK, STATE_GETTING_LOCATOR, ACTION_CHANGE_HOST},
        
		//{ STATE_URL_GROUP, EVENT_CHANGEURL, STATE_URL, ACTION_NONE}, //not change URL group
		
        //set the URL
        { STATE_GETTING_LOCATOR, EVENT_MODEL_EVENT_LOCATOR_GOT, STATE_URL_SETTING, ACTION_NONE},
        
        //got preloaded res
        { STATE_GETTING_PRELOAD_RES, EVENT_MODEL_EVENT_LOCATOR_GOT, STATE_PRELOAD_FILES, ACTION_PRELOAD_FILES},
        
        //go to change gps
        { STATE_INIT, CMD_CHANGE_GPS, STATE_GPS, ACTION_GPS },
        
        //go to change datasource mode
        { STATE_INIT, CMD_CHANGE_DATASOURCE_MODE, STATE_DATASOURCE_MODE, ACTION_DATASOURCE_MODE },
        
        //go to clear RMS
        { STATE_INIT, CMD_CLEAR_RMS, STATE_RMS, ACTION_CLEAR_RMS },
        
        //go to clear RMS
        { STATE_INIT, CMD_STATIC_AUDIO, STATE_CLEAR_STATIC_AUDIO, ACTION_CLEAR_STATIC_AUDIO },
        
        //change network type
        { STATE_INIT, CMD_CHANGE_NETWORK, STATE_NETWORK, ACTION_NETWORK },
        
        //change cell source.
        { STATE_INIT, CMD_CHANGE_CELL, STATE_CHANGE_CELL, ACTION_NONE },
        
        //set PTN.
        { STATE_INIT, CMD_SET_PTN, STATE_SET_PTN, ACTION_NONE },
        
        //set MViewer Ip.
        { STATE_INIT, CMD_SET_MVIEWER_IP, STATE_SET_MVIEWER_IP, ACTION_NONE },
        
        //set alongroute speed.
        { STATE_INIT, CMD_SET_ALONGROUTE_SPEED, STATE_SET_ALONGROUTE_SPEED, ACTION_NONE },
        
        
        //set send type.
        { STATE_INIT, CMD_DWF_SEND_SETTING, STATE_DWF_SEND_SETTING, ACTION_NONE },
        
        //set logger type.
        { STATE_INIT, CMD_SET_LOGGER, STATE_SET_LOGGER, ACTION_NONE },
        
        //set Resourcepath Enable.
        { STATE_INIT, CMD_SET_RESOURCE_PATH_ENABLE, STATE_SET_RESOURCE_PATH_ENABLE, ACTION_NONE },
        
        //enable switch airplane mode
        { STATE_INIT, CMD_SWITCH_AIRPLANE_MODE, STATE_SWITCH_AIRPLANE_MODE, ACTION_NONE },
        
        //goto fetch preload res
        { STATE_INIT, CMD_FETCH_PRELOAD_RES, STATE_FETCH_PRELOAD_RES, ACTION_NONE },
        
        //show Ptn & UserId.
        { STATE_INIT, CMD_PTN_USERID, STATE_PTN_USERID, ACTION_NONE },
        
        //set Billboard host.
        { STATE_INIT, CMD_SET_BILLBOARD, STATE_SET_BILLBOARD, ACTION_NONE },
        
        //set Billboard host.
        { STATE_INIT, CMD_SET_MAP_DATASET, STATE_SET_MAP_DATASET, ACTION_NONE },
        { STATE_SET_MAP_DATASET, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_MAP_DATASET },
        
        // exit the app when click back.
        { STATE_INIT, CMD_COMMON_CANCEL, STATE_EXIT, ACTION_NONE },
        { STATE_INIT, CMD_COMMON_BACK, STATE_EXIT, ACTION_NONE },
        { STATE_INIT, CMD_TEST_PROXY, STATE_TEST_PROXY, ACTION_NONE },
        { STATE_INIT, CMD_MAP_DOWNLOAD_CN, STATE_MAP_DOWNLOAD_CN, ACTION_NONE},
        { STATE_INIT, CMD_STUCK_MONITOR, STATE_STUCK_MONITOR, ACTION_NONE},
        { STATE_INIT, CMD_MAP_DISK_CACHE, STATE_MAP_DISK_CACHE, ACTION_NONE},
        { STATE_INIT, CMD_SHOW_SATELLITE_DURING_NAV, STATE_SHOW_SATELLITE_DURING_NAV, ACTION_NONE},
        
        //goto fetch preload res
        { STATE_INIT, CMD_PRELOAD_FILES, STATE_PRELOAD_FILES, ACTION_PRELOAD_FILES },
        
        { STATE_INIT, CMD_KONTAGENT, STATE_KONTAGENT_INFO, ACTION_NONE },
        
        //change the cell source.
        { STATE_CHANGE_CELL, CMD_CHANGE_DONE, STATE_CHANGE_DONE, ACTION_CHANGE_CELL },
        
        //set PTN done.
        { STATE_SET_PTN, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_PTN },
        
        //set MViewer Ip Done.
        { STATE_SET_MVIEWER_IP, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_MVIEWER_IP },
        
        //set along route speed Done.
        { STATE_SET_ALONGROUTE_SPEED, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_ALONGROUTE_SPEED },
        
        //set MViewer Ip Done.
        { STATE_SET_LOGGER, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_LOGGER },
        
        //save dwf seeting.
        { STATE_DWF_SEND_SETTING, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_DWF_SETTING },
       
        //set ResourcePathEnable.
        { STATE_SET_RESOURCE_PATH_ENABLE, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_RESOURCE_PATH_ENABLE },
        
        //set Billboard host Done.
        { STATE_SET_BILLBOARD, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_BILLBOARD },
        
        //set MapDownload CN Enable.
        { STATE_MAP_DOWNLOAD_CN, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_MAPDOWNLOAD_CN_ENABLE },
        //set stuck monitor Enable.
        { STATE_STUCK_MONITOR, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_STUCK_MONITOR_ENABLE },
        // set map disk cache disable.
        { STATE_MAP_DISK_CACHE, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SET_MAP_DISK_CACHE_DISABLE },
        
        //set SwitchAirplaneMode enable.
        { STATE_SWITCH_AIRPLANE_MODE, CMD_CHANGE_DONE, STATE_SET_DONE, ACTION_SWITCH_AIRPLANE_MODE },
        
        //prefetch
        { STATE_FETCH_PRELOAD_RES, CMD_CHANGE_DONE, STATE_GETTING_PRELOAD_RES, ACTION_FETCH_PRELOAD_RES },
        
        // select to change the item
        { STATE_ANY, CMD_COMMON_OK, STATE_INIT, ACTION_INIT },
        
        // back to the init from the url
        { STATE_URL_SETTING, CMD_COMMON_BACK, STATE_INIT, ACTION_INIT },
        { STATE_URL_SETTING, CMD_ALIAS_URL_SETTING, STATE_SET_ALIAS_URL, ACTION_NONE },
        { STATE_URL_SETTING, CMD_ACTION_URL_SETTING, STATE_SET_ACTION_URL, ACTION_NONE },
        { STATE_SET_DONE, CMD_COMMON_BACK, STATE_INIT, ACTION_INIT },
        { STATE_ANY, CMD_SHOW_DIRECTORY, STATE_SHOW_DIRECTORY, ACTION_SHOW_DIRECTORY },
        { STATE_ANY, CMD_REMOVE_OUT_PUT, STATE_ANY, ACTION_REMOVE_OUTPUT },
        { STATE_SHOW_DIRECTORY, CMD_SHOW_FILE_CONTENT, STATE_SHOW_FILE_CONTENT, ACTION_SHOW_FILE_CONTENT },
        { STATE_SHOW_DIRECTORY, CMD_DUMP_LOCAL_FILE, STATE_DUMP_LOCAL_FILE, ACTION_DUMP_LOCAL_FILE },
        { STATE_SHOW_DIRECTORY, CMD_COMMON_BACK, STATE_INIT, ACTION_INIT },  
        { STATE_DUMP_LOCAL_FILE, CMD_COMMON_BACK, STATE_INIT, ACTION_INIT }, 
        { STATE_SHOW_FILE_CONTENT, CMD_COMMON_BACK, STATE_INIT, ACTION_INIT }, 
        { STATE_ANY, CMD_SET_REGION, STATE_INIT, ACTION_SET_REGION },
        { STATE_INIT, CMD_SHOW_REGION, STATE_SHOW_REGION, ACTION_REGION_INIT },
        { STATE_SHOW_REGION, CMD_COMMON_OK, STATE_SHOW_REGION, ACTION_NONE},
        { STATE_SHOW_REGION, CMD_SET_REGION, STATE_INIT, ACTION_SET_REGION },
        

    };
    
    public SecretKeyController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_EXIT:
            {
                TeleNavDelegate.getInstance().exitApp();
                break;
            }
            case STATE_TEST_PROXY:
            {
                ModuleFactory.getInstance().startTestProxyController(this);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new SecretKeyModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new SecretKeyViewTouch(new SecretKeyUiDecorator());
    }

}
