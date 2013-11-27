package com.telenav.module.preference;

import java.util.Hashtable;

import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.module.dashboard.DashboardController;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.StateMachine;

public class PreferenceController extends AbstractCommonController implements IPreferenceConstants
{
	private final static int[][] STATE_TABLE = new int[][]{
		 {STATE_VIRGIN,                   EVENT_CONTROLLER_START,                 STATE_PREFERENCE_INIT,          ACTION_PREFERENCE_INIT },
		 {STATE_PREFERENCE_INIT,          EVENT_MODEL_PREFERENCE_GOTO_ROOT,   STATE_PREFERENCE_ROOT,          ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_COMMON_OK,                          STATE_PREFERENCE_LAYER1,        ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_COMMON_BACK,                       STATE_BACK_TO_DASHBOARD,        ACTION_NONE},
		 {STATE_PREFERENCE_LAYER1,        CMD_COMMON_BACK,                        STATE_PREFERENCE_CHANGING_CHECK, ACTION_PREFERENCE_CHANGING_CHECK},
		 {STATE_PREFERENCE_LAYER1,        CMD_SAVE_UPLOAD_PREFERENCE,             STATE_SAVING_UPLOAD_PREFENCE,   ACTION_SAVING_UPLOAD},
		 {STATE_PREFERENCE_LAYER1,        CMD_SWITCH_LANGUAGE,                    STATE_CHECK_LANGUAGE,           ACTION_CHECK_LANGUAGE_CHANGED},
		 {STATE_PREFERENCE_CHANGING_CHECK, EVENT_MODEL_PREFERENCE_CHANGED,        STATE_SAVING_UPLOAD_PREFENCE,   ACTION_SAVING_UPLOAD},
		 {STATE_PREFERENCE_CHANGING_CHECK, EVENT_MODEL_BACK_TO_ROOT,              STATE_PREFERENCE_ROOT,          ACTION_NONE},
		 {STATE_SAVING_UPLOAD_PREFENCE,   EVENT_MODEL_BACK_TO_ROOT,               STATE_PREFERENCE_ROOT,          ACTION_NONE},
		 {STATE_SAVING_UPLOAD_PREFENCE,   EVENT_MODEL_VALID_FAIL,                 STATE_PREFERENCE_VALID_FAIL,    ACTION_NONE},
		 {STATE_PREFERENCE_VALID_FAIL,    CMD_COMMON_OK,                          STATE_PREFERENCE_LAYER1,        ACTION_PREFENCE_RESET},
		 {STATE_CHECK_LANGUAGE,           EVENT_MODEL_LANGUAGE_CHANGE,            STATE_PREFERENCE_CHANGING_LANGUAGE, ACTION_NONE},
		 {STATE_PREFERENCE_LAYER1,        CMD_GO_TO_ROUTE_SETTING,                STATE_GO_TO_ROUTE_SETTING,      ACTION_NONE},
		 {STATE_ANY,                      EVENT_CONTROLLER_PREFERENCE_SAVED,      STATE_PREV,                     ACTION_CHECK_RETURNED_PREFERENCE_VALUE},
		 {STATE_PREFERENCE_ROOT,          CMD_GO_TO_SET_HOME,                     STATE_GO_TO_SET_HOME,        	  ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_GO_TO_SET_WORK,                     STATE_GO_TO_SET_WORK,           ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_DELETE_HOME,                     STATE_PREFERENCE_ROOT,        	  ACTION_DELETE_HOME},
		 {STATE_PREFERENCE_ROOT,          CMD_DELETE_WORK,                     STATE_PREFERENCE_ROOT,           ACTION_DELETE_WORK},
		 {STATE_PREFERENCE_ROOT,          CMD_ABOUT_PAGE,                         STATE_GOTO_ABOUT_PAGE,            ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_SHARE_SCOUT,                         STATE_GOTO_SHARE_SCOUT,            ACTION_NONE},
		 {STATE_GOTO_SHARE_SCOUT,         EVENT_CONTROLLER_NATIVE_SHARE_CANCEL,    STATE_PREFERENCE_ROOT,             ACTION_NONE },
		 {STATE_PREFERENCE_ROOT,          CMD_RATE_SCOUT,                         STATE_PREFERENCE_ROOT,            ACTION_RATE_APP},
         {STATE_PREFERENCE_ROOT,          CMD_SUPPORT_INFO,                     STATE_GOTO_SUPPORT_INFO,           ACTION_NONE},
         {STATE_PREFERENCE_ROOT,          CMD_MAP_NAV_SETTING,   STATE_PREFERENCE_LAYER1,           ACTION_NONE},
         {STATE_PREFERENCE_ROOT,          CMD_TERMS_CONDITIONS,   STATE_GOTO_TOS,           ACTION_NONE},
         {STATE_PREFERENCE_ROOT,          CMD_AUDIO,   STATE_PREFERENCE_LAYER1,           ACTION_NONE},
         {STATE_PREFERENCE_ROOT,          CMD_DRIVING_SHARE_SETTING,   STATE_GOTO_DRIVING_SHARE_SETTING,           ACTION_NONE},
         {STATE_PREFERENCE_ROOT,          CMD_MY_CAR,   STATE_MY_CAR,           ACTION_NONE},
         {STATE_PREFERENCE_ROOT,          CMD_MAP_DOWNLOAD,   STATE_GOTO_MAP_DOWNLOAD,           ACTION_NONE},
         {STATE_PREFERENCE_ROOT,          CMD_GOTO_UPSELL,   STATE_GOTO_UPSELL,           ACTION_NONE},
         { STATE_ANY, EVENT_CONTROLLER_NATIVE_SHARE_FINISH, STATE_PREV, ACTION_NONE },
         {STATE_MY_CAR,     CMD_COMMON_BACK,  STATE_CAR_CHANGING_CHECK, ACTION_CAR_CHANGING_CHECK},
         {STATE_CAR_CHANGING_CHECK,  EVENT_MODEL_CAR_CHANGED,        STATE_PREFERENCE_CHANGING_CAR,   ACTION_CHANGE_CAR},
         {STATE_CAR_CHANGING_CHECK,  EVENT_MODEL_BACK_TO_ROOT,        STATE_PREFERENCE_ROOT,   ACTION_NONE},
         {STATE_PREFERENCE_CHANGING_CAR, EVENT_MODDEL_CAR_CHANGED_FINISH,  STATE_PREFERENCE_ROOT,  ACTION_NONE},
         {STATE_MY_CAR,     CMD_SELECT_CAR,  STATE_MY_CAR,           ACTION_NONE},
		 {STATE_GO_TO_SET_HOME,          EVENT_CONTROLLER_ADDRESS_SELECTED,                     STATE_PREV,           ACTION_NONE},
		 {STATE_GO_TO_SET_WORK,          EVENT_CONTROLLER_ADDRESS_SELECTED,                     STATE_PREV,           ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_CREATE_ACCOUNT,                     STATE_GOTO_FTUE,           ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_LOGIN,                     STATE_GOTO_FTUE,           ACTION_NONE},
		 {STATE_PREFERENCE_ROOT,          CMD_EDIT_ACCOUNT,   STATE_GOTO_FTUE,  ACTION_NONE},
		 {STATE_ANY, EVENT_CONTROLLER_BACK_TO_PREV, STATE_PREFERENCE_ROOT, ACTION_CHECK_MIGRATION },
		 {STATE_PREFERENCE_ROOT,          CMD_SHOW_SUBSCRIPTION,   STATE_SHOW_SUBSCRIPTION,  ACTION_NONE},
		   
		 
		 {STATE_SHOW_SUBSCRIPTION,  CMD_CANCEL_SUBSCRIPTION_CONFIRM, STATE_CANCEL_SUBSCRIPTION_CONFIRM, ACTION_NONE},
		 {STATE_CANCEL_SUBSCRIPTION_CONFIRM,  CMD_CANCEL_SUBSCRIPTION, STATE_GOTO_UPSELL_CANCEL_SUBSCRIPTION, ACTION_NONE},
		 {STATE_ANY,  EVENT_CONTROLLER_CANCEL_SUBSCRIPTION_SUCCESS, STATE_SHOW_SUBSCRIPTION, ACTION_SET_CANCEL_SUBSCRIPTION_FLAG},
		 {STATE_SHOW_SUBSCRIPTION,  EVENT_MODEL_SUBSCRIPTION_CANCEL_SUCCESS, STATE_SHOW_CANCEL_SUBSCRIPTION_SUCCESS, ACTION_NONE},
		 {STATE_SHOW_CANCEL_SUBSCRIPTION_SUCCESS,  CMD_COMMON_OK, STATE_PREFERENCE_ROOT, ACTION_NONE},
		 {STATE_SHOW_CANCEL_SUBSCRIPTION_SUCCESS,  CMD_COMMON_BACK, STATE_PREFERENCE_ROOT, ACTION_NONE},
		 {STATE_ANY, EVENT_CONTROLLER_BACK_TO_PREFERENCE, STATE_PREFERENCE_ROOT, ACTION_NONE },
	};
	                                                     
	                                                    

	public PreferenceController(AbstractController superController)
    {
	    super(superController);
    }

	protected void postStateChangeDelegate(int currentState, int nextState)
	{
	    switch(nextState)
	    {
	        case STATE_VIRGIN:
	        {
	            break;
	        }
	        case STATE_PREFERENCE_INIT:
	        {
	            break;
	        }
	        case STATE_PREFERENCE_ROOT:
	        {
	            this.model.remove(KEY_B_IS_PREFERENCE_CHANGE);
	            this.model.remove(KEY_B_IS_NEED_UPLOAD);
	            this.model.remove(KEY_S_VALID_FAIL);
	            ((Hashtable)model.get(KEY_O_USER_INFO)).clear();
	            break;
	        }
	        case STATE_GO_TO_ROUTE_SETTING:
	        {
	            ModuleFactory.getInstance().startRouteSettingController(this, EVENT_CONTROLLER_GO_TO_ROUTE_SETTING, false, false);
	            break;
	        }
	        case STATE_GO_TO_SET_HOME:
	        {
	            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
	        	ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_HOME_ADDRESS, userProfileProvider);
	        	break;
	        }
	        case STATE_GO_TO_SET_WORK:
	        {
	            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
	        	ModuleFactory.getInstance().startSetHomeController(this, ICommonConstants.TYPE_WORK_ADDRESS, userProfileProvider);
	        	break;
	        }
	        case STATE_PREFERENCE_CHANGING_LANGUAGE:
	        {
	            ModuleFactory.getInstance().startSwitchLangController(this, model.getString(KEY_S_CHANGED_LANGUAGE), model.getInt(KEY_I_CHANGED_LANGUAGE));
	            break;
	        }
	        case STATE_GOTO_FTUE:
	        {
                int event = model.getInt(KEY_I_FTUE_LAUNCH_EVENT);
                ModuleFactory.getInstance().startLoginController(this, event, true, TYPE_LOGIN_FROM_PREFERENCE);
                break;
	        }
	        case STATE_GOTO_ABOUT_PAGE:
            {
                ModuleFactory.getInstance().startAboutController(EVENT_CONTROLLER_GOTO_ABOUT, this);
                break;
            }
	        case STATE_GOTO_SUPPORT_INFO:
            {
                ModuleFactory.getInstance().startAboutController(EVENT_CONTROLLER_GOTO_SUPPORT, this);
                break;
            }
	        case STATE_GOTO_TOS:
            {
                ModuleFactory.getInstance().startAboutController(EVENT_CONTROLLER_GOTO_TOS, this);
                break;
            }
	        case STATE_GOTO_SHARE_SCOUT:
	        {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider) model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startNativeShareController(this, null, userProfileProvider);
                break;
	        }
	        case STATE_GOTO_MAP_DOWNLOAD:
	        {
                ModuleFactory.getInstance().startMapDownLoadController(this);
                break;
	        }
	        case STATE_GOTO_UPSELL:
	        {
	            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
	            ModuleFactory.getInstance().startUpSellController(this, null, false, userProfileProvider);
	            break;
	        }
	        case STATE_GOTO_UPSELL_CANCEL_SUBSCRIPTION:
	        {
	            IUserProfileProvider userProfileProvider = (IUserProfileProvider)model.get(KEY_O_USER_PROFILE_PROVIDER);
                ModuleFactory.getInstance().startUpSellController(this, null, false, userProfileProvider, EVENT_CONTROLLER_GOTO_UPSELL_CANCEL_SUBSCRIPTION);
                break;
	        }
	        case STATE_BACK_TO_DASHBOARD:
	        {
	            AbstractController controller = this.getSuperController();
	            if(controller instanceof DashboardController)
	            {
	                this.postControllerEvent(EVENT_CONTROLLER_BACK_TO_DASHBOARD, null);
	            }
	            else
	            {
	                this.postControllerEvent(EVENT_CONTROLLER_BACK_TO_PREV, null);
	            }
	            break;
	        }
            case STATE_GOTO_DRIVING_SHARE_SETTING:
            {
                ModuleFactory.getInstance().startDrvingShareSettingController(this);
                break;
            }
	    }
	}

	protected AbstractView createView()
	{
	    PreferenceUiDecorator uiDecorator = new PreferenceUiDecorator();
	    return new PreferenceViewTouch(uiDecorator);
	}

	protected AbstractModel createModel()
	{
		PreferenceModel prefModel = new PreferenceModel();
		
		return prefModel;
	}

	protected StateMachine createStateMachine()
	{
		return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
	}

}
