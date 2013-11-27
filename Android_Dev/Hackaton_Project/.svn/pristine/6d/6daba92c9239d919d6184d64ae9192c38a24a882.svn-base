/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MaiTaiController.java
 *
 */
package com.telenav.sdk.maitai.module;


import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IOneBoxSearchProxy;
import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.module.ModuleFactory;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.entry.AbstractCommonEntryController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.HomeScreenManager;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.StateMachine;
import com.telenav.sdk.maitai.IMaiTaiParameter;
import com.telenav.sdk.maitai.impl.MaiTaiHandler;
import com.telenav.sdk.maitai.impl.MaiTaiManager;

/**
 *@author qli
 *@date 2010-12-2
 */
public class MaiTaiController extends AbstractCommonEntryController implements IMaiTaiConstants
{
    
    protected final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN,         EVENT_CONTROLLER_START,             STATE_INIT,                 ACTION_CHECK_REGION_DETECTION_STATUS },
        { STATE_ANY,         EVENT_MODEL_CHECK_REGION,             STATE_ANY,                 ACTION_CHECK_REGION_CHANGE },
        { STATE_INIT,           EVENT_MODEL_INIT_MAITAI,            STATE_START_MAITAI,         ACTION_START_MAITAI },
        { STATE_START_MAITAI,   EVENT_MODEL_RGC_ADDRESS,          STATE_MAITAI_RGC_ADDRESS,   ACTION_RGC_ADDRESS },
        { STATE_MAITAI_RGC_ADDRESS,   EVENT_MODEL_HANDLE_MAITAI,          STATE_MAITAI_GOTO_MODULE,   ACTION_NONE },
        { STATE_START_MAITAI,   EVENT_MODEL_HANDLE_MAITAI,          STATE_MAITAI_GOTO_MODULE,   ACTION_NONE },
        { STATE_ANY,            EVENT_MODEL_VALIDATE_ADDRESS,       STATE_GOTO_VALIDATE,        ACTION_NONE },
        { STATE_GOTO_VALIDATE,  EVENT_CONTROLLER_ADDRESS_VALIDATED, STATE_START_MAITAI,         ACTION_GOTO_MODULE },
        
        { STATE_ANY,            EVENT_MODEL_GET_ADDRESS_BY_ID,       STATE_START_MAITAI,        ACTION_GOTO_GET_ADDRESS_BY_ID },
        
        { STATE_ANY,            EVENT_CONTROLLER_EXIT_MAITAI,       STATE_VIRGIN,               ACTION_NONE },
        { STATE_ANY,            CMD_COMMON_BACK,                    STATE_VIRGIN,               ACTION_JUMP_BACKGROUND },
        
        //for link button
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_MAP, STATE_DO_LINK, ACTION_LINK_TO_MAP },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_AC, STATE_DO_LINK, ACTION_LINK_TO_AC },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_POI, STATE_DO_LINK, ACTION_LINK_TO_POI },
        { STATE_ANY, EVENT_CONTROLLER_LINK_TO_EXTRA, STATE_DO_LINK, ACTION_LINK_TO_EXTRA },
        
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_MAP, STATE_LINK_TO_MAP, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_AC, STATE_LINK_TO_AC, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_POI, STATE_LINK_TO_POI, ACTION_NONE },
        { STATE_DO_LINK, EVENT_MODEL_LINK_TO_EXTRA, STATE_LINK_TO_EXTRA, ACTION_NONE },
        
        { STATE_ANY,   EVENT_MODEL_SWITCHING_FAIL,         STATE_SWITCHING_FAIL,       ACTION_NONE },        
        { STATE_ANY,   EVENT_MODEL_SWITCHING_SUCC_FROM_SERVER,         STATE_START_MAITAI,       ACTION_START_MAITAI },        
        { STATE_SWITCHING_FAIL,     CMD_COMMON_BACK,                    STATE_START_MAITAI,                 ACTION_START_MAITAI },        
        //{ STATE_SWITCHING_SUCC,     CMD_COMMON_BACK,                    STATE_START_MAITAI,                 ACTION_START_MAITAI },   
        { STATE_ANY,   EVENT_MODEL_CONTINUE,                           STATE_START_MAITAI,       ACTION_START_MAITAI },  
        { STATE_ANY,   EVENT_MODEL_START_MAITAI_ERROR,                 STATE_START_MAITAI_ERROR,       ACTION_NONE },  
        
    };

    public MaiTaiController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch (nextState)
        {
            case STATE_VIRGIN:
            {
                MaiTaiManager.getInstance().finish();
                break;
            }
            case STATE_MAITAI_GOTO_MODULE:
            {
                this.model.put(KEY_B_MAITAI_GOTO_MODULE, true);
                handleModule();
                break;
            }
            case STATE_GOTO_VALIDATE:
            {
                String addr = this.model.getString(KEY_S_VALIDATE_ADDRESS);
                if( addr != null && addr.trim().length() > 0 )
                {
                    IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                    ModuleFactory.getInstance().startAddressValidatorController(this, addr, "", TYPE_FROM_MAITAI, null, userProfileProvider, null);
                }
                else
                {
                    MaiTaiManager.getInstance().finish();
                }
                break;
            }
            case STATE_START_MAITAI_ERROR:
            {
                MaiTaiHandler.getInstance().clear();
                MaiTaiManager.getInstance().setFromMaiTai(false);
                releaseAll();
                ModuleFactory.getInstance().startMain();
                break;
            }
        }
        super.postStateChangeDelegate(currentState, nextState);
    }

    protected void handleModule()
    {
        String action =  (String)this.model.fetch(KEY_S_MAITAI_ACTION);
        if( IMaiTaiParameter.ACTION_NAVTO.equalsIgnoreCase(action) )
        {
            ModuleFactory.getInstance().startNavController(this, null, (Address) this.model.fetch(KEY_O_SELECTED_ADDRESS),
                model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), false, false);
        }
        else if( IMaiTaiParameter.ACTION_DIRECTIONS.equalsIgnoreCase(action) )
        {
            ModuleFactory.getInstance().startNavController(this, (Address) this.model.fetch(KEY_O_ADDRESS_ORI),
                (Address) this.model.fetch(KEY_O_ADDRESS_DEST), model.fetchVector(ICommonConstants.KEY_V_STOP_AUDIOS), false, false);
        }
        else if( IMaiTaiParameter.ACTION_MAP.equalsIgnoreCase(action) )
        {
            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
            Object addr = this.model.fetch(KEY_O_SELECTED_ADDRESS);
            if( null == addr )
            {     
                ModuleFactory.getInstance().startMapController(this, 
                    HomeScreenManager.getHomeMapFromType(), null, null, userProfileProvider);
            }
            else if( addr instanceof Address )
            {
                ModuleFactory.getInstance().startMapController(this, 
                    TYPE_FROM_MAITAI, (Address)addr, null, userProfileProvider);
            }
            else if( addr instanceof Address[] )
            {
                ModuleFactory.getInstance().startMapController(this, 
                    TYPE_MAP_FROM_MAITAI_POI, (Address[])addr, null, userProfileProvider);
            }
        }
        else if( IMaiTaiParameter.ACTION_SEARCH.equalsIgnoreCase(action) )
        {
            Object objId = model.fetch(KEY_I_CATEGORY_ID);
            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
            if (objId instanceof Integer)
            {
                int catId = ((Integer) objId).intValue();
                ModuleFactory.getInstance().startCategorySearchController(this, TYPE_AC_FROM_MAP, -1, -1, -1, -1, catId, false,
                    null, null, null, userProfileProvider, false); 
            }
            else
            {
                if(userProfileProvider == null)
                {
                    userProfileProvider = new UserProfileProvider();
                }
                String oneboxSearch = DaoManager.getInstance().getServerDrivenParamsDao().getBoxType(this.getRegion());
                boolean isOneBoxSupport = ServerDrivenParamsDao.ONE_BOX_TYPE_STANDARD.equals(oneboxSearch);
                int  searchType = IPoiSearchProxy.TYPE_SEARCH_ADDRESS;
                if (isOneBoxSupport)
                {
                    searchType = IOneBoxSearchProxy.TYPE_SEARCH_AROUND_ME;
                }
                ModuleFactory.getInstance().startOneBoxController(this, TYPE_AC_FROM_MAP, 
                    searchType, -1, 
                    (String)this.model.fetch(KEY_S_COMMON_SEARCH_TEXT), true, false,  (Address)this.model.fetch(KEY_O_SELECTED_ADDRESS), 
                    null, null, userProfileProvider, false, false, false);
            }
            
        }
        else if( IMaiTaiParameter.ACTION_VIEW.equalsIgnoreCase(action))
        {
        	ModuleFactory.getInstance().startMaiTaiViewController(this);
        }
        else if ( IMaiTaiParameter.ACTION_SET_ADDRESS.equals(action))
        {
            int type = model.getBool(KEY_B_IS_SET_HOME) ? TYPE_HOME_ADDRESS : TYPE_WORK_ADDRESS;
            ModuleFactory.getInstance().startSetHomeController(this, type, new UserProfileProvider());
        }
    }

    protected AbstractModel createModel()
    {
        return new MaiTaiModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new MaiTaiViewTouch(new MaiTaiUiDecorator());
    }

}
