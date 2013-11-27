/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LoginController.java
 *
 */
package com.telenav.module.login;

import com.telenav.app.TeleNavDelegate;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author bduan
 *@date 2010-11-26
 */
public class LoginController extends AbstractCommonController implements ILoginConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_INIT, ACTION_CHECK_CONTEXT }, 
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_FTUE_MAIN, STATE_FTUE_WELCOME, ACTION_SET_MAYBELAYTER_DISABLE}, 
        { STATE_ANY, EVENT_MODEL_LAUNCH_FTUE, STATE_FTUE_WELCOME, ACTION_NONE }, 
        
        //when click below buttons go to separately pages.
        { STATE_FTUE_WELCOME, CMD_FTUE_TOS, STATE_GOTO_TOS, ACTION_NONE }, 
        { STATE_FTUE_WELCOME, CMD_FTUE_SIGN_UP, STATE_FTUE_SHOW_SIGN_UP, ACTION_NONE }, 
        { STATE_FTUE_WELCOME, CMD_FTUE_SIGN_IN, STATE_FTUE_SHOW_SIGN_IN, ACTION_NONE }, 
        //when click maybe later, check the dim process firstly.
        { STATE_FTUE_WELCOME, CMD_FTUE_MAYBE_LATER, STATE_FTUE_MAYBE_LATER, ACTION_MAYBE_LATER },
        
        { STATE_FTUE_SHOW_SIGN_IN, CMD_FTUE_LOGIN_FORGET_PASSWD, STATE_FORGET_PASSWORD, ACTION_NONE },
        
        //after ptn confirmed. back to corresponding page firstly.
        { STATE_ANY, EVENT_MODEL_FTUE_SIGN_UP, STATE_FTUE_SHOW_SIGN_UP, ACTION_SIGN_UP_SUMMIT }, 
        { STATE_ANY, EVENT_MODEL_FTUE_SIGN_IN, STATE_FTUE_SHOW_SIGN_IN, ACTION_SIGN_IN_SUMMIT }, 
        
        //check the ptn;
        { STATE_ANY, CMD_FTUE_SIGN_UP_SUBMIT, STATE_ANY, ACTION_SIGN_UP_PREPARE }, 
        { STATE_ANY, CMD_FTUE_SIGN_IN_SUBMIT, STATE_ANY, ACTION_SIGN_IN_PREPARE },
        
        { STATE_ANY, EVENT_MODEL_WAIT_DIM_PROCESS, STATE_WAIT_DIM_PROCESS, ACTION_NONE }, 
        { STATE_WAIT_DIM_PROCESS, EVENT_MODEL_DIM_FINISH, STATE_CHECK_DIM_PTN, ACTION_CHECK_DIM_PTN }, 
        
        //block back action.
        { STATE_WAIT_DIM_PROCESS, CMD_COMMON_BACK, STATE_WAIT_DIM_PROCESS, ACTION_NONE }, 
        { STATE_ANY, EVENT_MODEL_RESTORE_PROCESS, STATE_ANY, ACTION_FTUE_RESTORE_PROCESS }, 
        
        { STATE_ANY, EVENT_MODEL_CHECK_EMAIL_ERROR, STATE_FTUE_CHECK_EMAIL_ERROR, ACTION_NONE }, 
        { STATE_ANY, EVENT_MODEL_CHECK_PASSWORD_ERROR, STATE_FTUE_VALIDATION_PASSWORD_ERROR, ACTION_NONE },         
        { STATE_ANY, EVENT_MODEL_EMAIL_NOT_REGISTERED, STATE_FTUE_LOGIN_EMAIL_NOT_REGISTERED, ACTION_NONE }, 
        { STATE_ANY, EVENT_MODEL_PASSWARD_INCORRECT, STATE_FTUE_LOGIN_PASSWORD_INCORRECT, ACTION_NONE }, 
        { STATE_ANY, EVENT_MODEL_EMAIL_ALREADY_REGISTERED, STATE_FTUE_LOGIN_EMAIL_ALREADY_REGISTERED, ACTION_NONE }, 
        
        //type in flow
        { STATE_ANY, EVENT_MODEL_TYPE_IN, STATE_TYPEIN_PTN, ACTION_NONE },
        { STATE_TYPEIN_PTN, CMD_TYPEIN_CONTINUE, STATE_CHECK_TYPE_PTN, ACTION_CHECK_TYPE_IN_PTN },
        { STATE_TYPEIN_PTN, CMD_COMMON_EXIT, STATE_EXIT_APP, ACTION_NONE }, 
        { STATE_TYPEIN_PTN, CMD_FORGET_PIN, STATE_SENDING_FORGET_REQUESET, ACTION_FORGET_PIN},
        { STATE_TYPEIN_PTN, CMD_COMMON_BACK, STATE_PREV, ACTION_CLEAR_TYPE_IN},
        
        //verification flow
        { STATE_ANY, EVENT_MODEL_SEND_VERIFICATION_CODE, STATE_REQUEST_VERIFICATION_CODE, ACTION_REQUEST_VERIFICATION_CODE }, 
        
        //block back action
        { STATE_REQUEST_VERIFICATION_CODE, CMD_COMMON_BACK, STATE_REQUEST_VERIFICATION_CODE, ACTION_NONE }, 
        { STATE_REQUEST_VERIFICATION_CODE, EVENT_MODEL_VERI_CODE_SENT, STATE_INPUT_VERIFICATION_CODE, ACTION_NONE },
        { STATE_INPUT_VERIFICATION_CODE, CMD_COMMON_EXIT, STATE_EXIT_APP, ACTION_NONE }, 
        { STATE_INPUT_VERIFICATION_CODE, CMD_TYPEIN_CONTINUE, STATE_CHECK_VERIFICATION_CODE, ACTION_CHECK_VERIFICATION_CODE }, 
        { STATE_CHECK_VERIFICATION_CODE, EVENT_MODEL_VERI_CODE_AVAILABLE, STATE_CHECK_VERIFICATION_CODE, ACTION_LOGIN_WITH_VERIFICATION_CODE }, 
        { STATE_CHECK_VERIFICATION_CODE, EVENT_MODEL_VERI_CODE_UNAVAILABLE, STATE_COMMON_ERROR, ACTION_NONE }, 
        
        //if verification code error.
        { STATE_ANY, EVENT_MODEL_VERIFY_PTN_FAIL, STATE_PTN_VERIFY_FAIL, ACTION_NONE }, 
        { STATE_PTN_VERIFY_FAIL, CMD_COMMON_OK, STATE_INPUT_VERIFICATION_CODE, ACTION_NONE},
        { STATE_PTN_VERIFY_FAIL, CMD_COMMON_BACK, STATE_INPUT_VERIFICATION_CODE, ACTION_NONE},
        { STATE_ANY, EVENT_MODEL_FTUE_LOADING, STATE_FTUE_LORDING, ACTION_NONE },
        
        //block back action
        { STATE_FTUE_LORDING, CMD_COMMON_BACK, STATE_FTUE_LORDING, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_FTUE_SUCCESS, STATE_FTUE_SUCCESS, ACTION_FTUE_SUCCESS_GO }, 
        
        { STATE_ANY, EVENT_MODEL_LOGIN_FINISH, STATE_BACK_TO_MAIN, ACTION_NONE }, 
        
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_FTUE_SIGNUP, STATE_FTUE_SHOW_SIGN_UP, ACTION_NONE },
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_FTUE_SIGNIN, STATE_FTUE_SHOW_SIGN_IN, ACTION_NONE },
        { STATE_VIRGIN, EVENT_CONTROLLER_GOTO_FTUE_EDIT_ACCOUNT, STATE_FTUE_SHOW_EDIT_ACCOUNT, ACTION_NONE },
        { STATE_FTUE_SHOW_EDIT_ACCOUNT, CMD_FTUE_EDIT_ACCOUNT, STATE_FTUE_EDIT_ACCOUNT, ACTION_EDIT_ACCOUNT },
        { STATE_FTUE_EDIT_ACCOUNT, EVENT_MODEL_EDIT_ACCOUNT_DONE, STATE_BACK_TO_LAUNCH, ACTION_NONE },
        { STATE_FTUE_SHOW_SIGN_UP, CMD_COMMON_BACK, STATE_FTUE_SHOW_SIGN_UP, ACTION_ACCOUNT_BACK_CHECK },
        { STATE_FTUE_SHOW_SIGN_IN, CMD_COMMON_BACK, STATE_FTUE_SHOW_SIGN_IN, ACTION_ACCOUNT_BACK_CHECK },
        { STATE_FTUE_SHOW_EDIT_ACCOUNT, CMD_COMMON_BACK, STATE_FTUE_SHOW_EDIT_ACCOUNT, ACTION_ACCOUNT_BACK_CHECK },
        { STATE_FTUE_WELCOME, CMD_COMMON_BACK, STATE_FTUE_WELCOME, ACTION_ACCOUNT_BACK_CHECK },
        { STATE_ANY, EVENT_MODEL_BACK_TO_PREV, STATE_PREV, ACTION_NONE },
        { STATE_ANY, EVENT_MODEL_BACK_TO_LAUNCH, STATE_BACK_TO_LAUNCH, ACTION_NONE }, 
        { STATE_ANY, EVENT_MODEL_UPGRADE_LOGIN_FAILED, STATE_UPGRADE_LOGIN_FAILED, ACTION_NONE }, 
        { STATE_UPGRADE_LOGIN_FAILED, CMD_UPGRADE_BACK_TO_FTUE, STATE_FTUE_WELCOME, ACTION_NONE }, 
    };

    public LoginController(AbstractController superController)
    {
        super(superController);
        
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_VIRGIN:
            {
                TeleNavDelegate.getInstance().exitApp();
                break;
            }
            case STATE_EXIT_APP:
            {
                DaoManager.getInstance().getMandatoryNodeDao().clear();
                DaoManager.getInstance().getBillingAccountDao().clear();
                TeleNavDelegate.getInstance().exitApp();
                break;
            }
            case STATE_BACK_TO_MAIN:
            {
                this.postControllerEvent(EVENT_CONTROLLER_LOGIN_FINISH, null);
                TeleNavDelegate.getInstance().setBackgroundDrawable(null);
                break;
            }
            case STATE_BACK_TO_LAUNCH:
            {
                Parameter data = new Parameter();
                boolean fromKeyBack = this.model.fetchBool(KEY_B_TRIGGER_BY_KEY_BACK);
                // TO INDEX UPDATE VIEW TO  BE REFRESHED, IF TRUE, TRANSACTION FINISHED, UPDATE
                // PREVIOUS SCREEN, FALSE BACK KEY PRESS, NO NEED UPDATE PREVIOUS VIEW
                if (fromKeyBack)
                {
                    data.put(KEY_B_IS_START_BY_OTHER_CONTROLLER, false);
                }
                else
                {
                    data.put(KEY_B_IS_CREATE_ACCOUNT, model.fetch(KEY_B_IS_CREATE_ACCOUNT));
                    data.put(KEY_B_IS_START_BY_OTHER_CONTROLLER, true);
                }
                this.postControllerEvent(EVENT_CONTROLLER_BACK_TO_PREV, data);
                TeleNavDelegate.getInstance().setBackgroundDrawable(null);
                break;
            }
            case  STATE_GOTO_TOS:
            {
                ModuleFactory.getInstance().startAboutController(EVENT_CONTROLLER_GOTO_TOS, this);
                break;
            }
        }
    }
    
    public static class UserLoginData
    {
        public PtnData ptnData = null;
        public CredentialInfo credentialInfo = null;
    }
    
    public static class PtnData
    {
        public String ptn = null;
        public String ptn_type = null;
        public String carrier = null;
    }

    protected AbstractModel createModel()
    {
        return new LoginModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new LoginViewTouch(new LoginUiDecorator());
    }

}
