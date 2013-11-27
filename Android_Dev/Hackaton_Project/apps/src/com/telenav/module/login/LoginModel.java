/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LoginModel.java
 *
 */
package com.telenav.module.login;

import java.util.Hashtable;

import com.telenav.ads.MobileAppConversionTracker;
import com.telenav.app.CommManager;
import com.telenav.app.NavServiceManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.ILoginProxy;
import com.telenav.data.serverproxy.impl.IStartupProxy;
import com.telenav.data.serverproxy.impl.IToolsProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.navsdk.service.NavsdkUserManagementService;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AppStatusMisLog;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.UserProfileProvider;
import com.telenav.module.dashboard.AccountChangeListener;
import com.telenav.module.entry.DimProvider;
import com.telenav.module.entry.IDimListner;
import com.telenav.module.login.LoginController.PtnData;
import com.telenav.module.login.LoginController.UserLoginData;
import com.telenav.module.sync.SyncResExecutor;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navservice.NavServiceParameter;
import com.telenav.res.IStringLogin;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.telephony.TnSimCardInfo;
import com.telenav.telephony.TnTelephonyManager;
/**
 *@author bduan
 *@date 2010-11-26
 */
class LoginModel extends AbstractCommonNetworkModel implements ILoginConstants, IDimListner
{
    protected PtnData latestPtnData = null;
    protected PtnData dimPtnData = null;
    protected PtnData simPtnData = null;
    protected PtnData existPtnData = null;
    protected PtnData typeInPtnData = null;
    protected TransactionLocker transactionLocker=null;
    
    protected LoginModel()
    {
        prefetchUserInfo();
    }
    
    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_CHECK_CONTEXT:
            {
                checkContext();
                break;
            }
            case ACTION_SET_MAYBELAYTER_DISABLE:
            {
                put(KEY_B_MEYBELATER_DISABLE, true);
                break;
            }
            case ACTION_CHECK_DIM_PTN:
            {
                checkPtnStatus();
                break;
            }
            case ACTION_SIGN_IN_SUMMIT:
            {
                checkPreparedData();
                break;
            }
            case ACTION_SIGN_UP_SUMMIT:
            {
                checkPreparedData();
                break;
            }
            case ACTION_MAYBE_LATER:
            {
                put(KEY_I_LOGIN_TYPE, BillingAccountDao.ACCOUNT_ENTRY_TYPE_MAYBE_LATER);
                checkPreparedData();
                break;
            }
            case ACTION_SIGN_IN_PREPARE:
            {
                put(KEY_I_LOGIN_TYPE, BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_IN);
                checkMailPwd(EVENT_MODEL_FTUE_SIGN_IN);
                break;
            }
            case ACTION_SIGN_UP_PREPARE:
            {
                put(KEY_I_LOGIN_TYPE, BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP);
                checkMailPwd(EVENT_MODEL_FTUE_SIGN_UP);
                break;
            }
            case ACTION_FTUE_RESTORE_PROCESS:
            case ACTION_LOGIN_WITH_VERIFICATION_CODE:
            {
                restoreProcess();
                break;
            }
            case ACTION_CHECK_TYPE_IN_PTN:
            {
                String typePtn = this.getString(KEY_S_INPUT_STR);
                PtnData tmpData = new PtnData();
                
                tmpData.ptn = typePtn;
                tmpData.ptn_type = BillingAccountDao.PTN_TYPE_TYPEIN;
                tmpData.carrier = "";
                
                if(checkPtn(tmpData))
                {
                    typeInPtnData = tmpData;
                    checkPtnStatus();
                }
                else
                {
                    String errorMsg = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringLogin.RES_LABEL_NO_TYPEIN, IStringLogin.FAMILY_LOGIN);
                    this.put(KEY_S_ERROR_MESSAGE, errorMsg);
                    this.postModelEvent(EVENT_MODEL_POST_ERROR);
                }
                break;
            }
            case ACTION_REQUEST_VERIFICATION_CODE:
            {
                doRequestVerificationCode();
                break;
            }
            case ACTION_FTUE_SUCCESS_GO:
            {
                this.postModelEvent(EVENT_MODEL_LOGIN_FINISH);
                break;
            }
            case ACTION_FORGET_PIN:
            {
            	doForgotPin();
            	break;
            }
            case ACTION_CHECK_VERIFICATION_CODE:
            {
                String verificationCode = this.getString(KEY_S_VERIFY_CODE);
                if(verificationCode == null || verificationCode.trim().length() == 0)
                {
                    String errorMsg = ResourceManager.getInstance().getCurrentBundle().getString(
                        IStringLogin.RES_INFO_VERIFICATION_CODE_ERROR, IStringLogin.FAMILY_LOGIN);
                    this.put(KEY_S_ERROR_MESSAGE, errorMsg);
                    this.postModelEvent(EVENT_MODEL_VERI_CODE_UNAVAILABLE);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_VERI_CODE_AVAILABLE);
                }
                break;
            }
            case ACTION_JUMP_TO_BACKGROUND:
            {
                TeleNavDelegate.getInstance().jumpToBackground();
                break;
            }
            case ACTION_ACCOUNT_BACK_CHECK:
            {
                if (isStartedByOtherController())
                {
                    if (this.getBool(KEY_B_MEYBELATER_DISABLE))
                    {
                        if (this.getState() == STATE_FTUE_WELCOME)
                        {
                            this.put(KEY_B_TRIGGER_BY_KEY_BACK, true);
                            this.postModelEvent(EVENT_MODEL_BACK_TO_LAUNCH);
                        }
                        else
                        {
                            this.postModelEvent(EVENT_MODEL_BACK_TO_PREV);
                        }
                    }
                    else
                    {
                        this.put(KEY_B_TRIGGER_BY_KEY_BACK, true);
                        this.postModelEvent(EVENT_MODEL_BACK_TO_LAUNCH);
                    }
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_BACK_TO_PREV);
                }
                
                break;
            }
            case ACTION_EDIT_ACCOUNT:
            {
                saveUserInfo();
                syncPreference(IToolsProxy.SYNC_TYPE_UPLOAD);
                this.postModelEvent(EVENT_MODEL_BACK_TO_LAUNCH);
                break;
            }
            case ACTION_CLEAR_TYPE_IN:
            {
                typeInPtnData = null;
                break;
            }
            default: 
            {
                break;
            }
        }
    }
    
    protected void checkSyncType()
    {
        int loginInType = DaoManager.getInstance().getBillingAccountDao().getLoginType();
        if(loginInType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
        {
            this.put(KEY_B_IS_CREATE_ACCOUNT, true);
            
            PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
            String firstName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME);
            String lastName = prefDao.getStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME);
            
            if((checkName(firstName)) || checkName(lastName))
            {
                syncPreference(IToolsProxy.SYNC_TYPE_UPLOAD);
            }
            else
            {
                checkBack();
            }
        }
        else
        {
            syncPreference(IToolsProxy.SYNC_TYPE_DOWNLOAD);
        }
    }
    
    private void savePreference(IToolsProxy toolsProxy)
    {
        if(toolsProxy == null)
            return;
        
        int syncType = toolsProxy.getPreferenceSyncType();
        if(syncType == IToolsProxy.SYNC_TYPE_DOWNLOAD)
        {
            Hashtable prefers = ((IToolsProxy)toolsProxy).getUserPrefers();
            if(prefers != null)
            {
                int loginType = DaoManager.getInstance().getBillingAccountDao().getLoginType();
                
                String firstNameOnWeb = (String)prefers.get(IToolsProxy.KEY_FIRSTNAME);
                String lastNameOnWeb = (String)prefers.get(IToolsProxy.KEY_LASTNAME);
                String emailOnWeb = (String)prefers.get(IToolsProxy.KEY_EMAIL);
                String usernameOnWeb = (String)prefers.get(IToolsProxy.KEY_USERNAME);
                String csrId= (String)prefers.get(IToolsProxy.KEY_CSRID);
                PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
                
                if(loginType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_IN)
                {
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME, firstNameOnWeb);
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME, lastNameOnWeb);
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_EMAIL, emailOnWeb);
                    prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_USERNAME, usernameOnWeb);
                }
                
                prefDao.setStrValue(Preference.ID_PREFERENCE_CSRID, csrId);
                prefDao.store(getRegion());
            }
        }
    }

    protected void saveUserInfo()
    {
        String first = checkName(this.getString(KEY_S_FIRSTNAME))?this.getString(KEY_S_FIRSTNAME).trim():"";
        String last = checkName(getString(KEY_S_LASTNAME))?this.getString(KEY_S_LASTNAME).trim():"";

        PreferenceDao prefDao = ((DaoManager)DaoManager.getInstance()).getPreferenceDao();
        prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME,first);
        prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME,last);
        prefDao.store(this.getRegion());
    }
    
    protected void syncPreference(byte type)
    {
        int uploadType = -1;
        if(type == IToolsProxy.SYNC_TYPE_UPLOAD)
        {
            uploadType = IToolsProxy.UPLOAD_TYPE_USER_INFO;
        }
        SyncResExecutor.getInstance().syncPreference(null, type, this, null, uploadType);
    }
    
    protected void checkPreparedData()
    {
        checkDimProcess();
    }

    protected void checkMailPwd(int modelEvent)
    {
        String emailAddress = this.getString(KEY_S_EMAILADDRESS);
        String password = this.getString(KEY_S_PASSWORD);

        boolean isLegalEmail = checkEmail(emailAddress);
        boolean isLegalPassword = checkPassword(password);
        
        if (isLegalEmail && isLegalPassword)
        {
            this.postModelEvent(modelEvent);
        }
        else
        {
            if (!isLegalEmail)
            {
                String error = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_MSG_EMAIL_ADDRESS_ERROR, IStringLogin.FAMILY_LOGIN);             
                postError(error, EVENT_MODEL_CHECK_EMAIL_ERROR);
            }
            else
            {
                String error=ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_MSG_PASSWORD_ERROR, IStringLogin.FAMILY_LOGIN);
                StringConverter converter = ResourceManager.getInstance().getStringConverter();
                error = converter.convert(error, new String[]{String.valueOf(PASSWORD_MIN_LENGTH),String.valueOf(PASSWORD_MAX_LENGTH)});
                postError(error, EVENT_MODEL_CHECK_PASSWORD_ERROR);
            }
        }
    }
    
    protected void checkPtnStatus()
    {
        dimPtnData = readPtnDataFromDim();
        simPtnData = readPtnFromSimcard();
        
        String verifyCode = this.getString(KEY_S_VERIFY_CODE);

        if (isStartedByOtherController())
        {
            reuseCurrentData();
            this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
        }
        else if (checkPtn(existPtnData))
        {
            latestPtnData = existPtnData;
            this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
        }
        else if (checkPtn(dimPtnData))
        {
            latestPtnData = dimPtnData;
            this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
        }
        else if(DimProvider.getInstance().isAllowFallback())
        {
            if(checkPtn(simPtnData))
            {
                latestPtnData = simPtnData;
                this.put(KEY_S_INPUT_STR, latestPtnData.ptn);
                
                //TNANDROID-3821, Per PM, for SIM card, we do not do verification flow.
                /*if(AppConfigHelper.isVerificaitonEnable())
                {
                    if(verifyCode != null && verifyCode.trim().length() > 0)
                    {
                        this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
                    }
                    else
                    {
                        this.postModelEvent(EVENT_MODEL_SEND_VERIFICATION_CODE);
                    }
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
                }*/
                
                this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
            }
            else if(checkPtn(typeInPtnData))
            {
                latestPtnData = typeInPtnData;
                if(AppConfigHelper.isVerificaitonEnable())
                {
                    if(verifyCode != null && verifyCode.trim().length() > 0)
                    {
                        this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
                    }
                    else
                    {
                        this.postModelEvent(EVENT_MODEL_SEND_VERIFICATION_CODE);
                    }
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_RESTORE_PROCESS);
                }
            }
            else
            {
                    this.postModelEvent(EVENT_MODEL_TYPE_IN);
            }
        }
        else
        {
            String errorMsg = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringLogin.RES_LABEL_LOGIN_FAIL_MSG, IStringLogin.FAMILY_LOGIN);

            this.put(KEY_S_ERROR_MESSAGE, errorMsg);

            this.postModelEvent(EVENT_MODEL_POST_ERROR);
        }
    }

    private void reuseCurrentData()
    {
        PtnData dimPtnData = new PtnData();        
        dimPtnData.ptn = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().phoneNumber;
        dimPtnData.ptn_type = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().ptnType; 
        dimPtnData.carrier = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().deviceCarrier;
        latestPtnData = dimPtnData;
        
    }
    
    protected void checkDimProcess()
    {
        if(DimProvider.getInstance().isRunning())
        {
            this.postModelEvent(EVENT_MODEL_WAIT_DIM_PROCESS);
        }
        else
        {
            checkPtnStatus();
        }
    }

    protected void postError(String errorStr, int event)
    {
        this.put(KEY_S_ERROR_MESSAGE, errorStr);
        this.postModelEvent(event);
    }

    protected UserProfileProvider composeUserProfile(String emailAddress, String password)
    {
        UserProfileProvider userProfileProvider = new UserProfileProvider();
        userProfileProvider.getMandatoryNode().getCredentialInfo().credentialType = ILoginProxy.FUTE_CREDENTIAL_EMAIL;
        userProfileProvider.getMandatoryNode().getCredentialInfo().credentialValue = emailAddress;
        userProfileProvider.getMandatoryNode().getCredentialInfo().credentialPassword = password;
        return userProfileProvider;
    }

    protected void sendLoginRequest()
    {
        String verificationCode = this.getString(KEY_S_VERIFY_CODE);
        sendLoginRequest(verificationCode);
    }
    
    protected void sendLoginRequest(String verificationCode)
    {
        clearMadantoryNode();
        getTransactionLocker().setTransactionStatus(TransactionLocker.TRANSACTION_INIT);
        IUserProfileProvider userProfileProvider = null;
        
        int accountEntryType = getInt(KEY_I_LOGIN_TYPE);
        if(accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_IN 
                || accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
        {
            String emailAddress = getString(KEY_S_EMAILADDRESS);
            String password = getString(KEY_S_PASSWORD);
            userProfileProvider = composeUserProfile(emailAddress, password);
        }
        
        if(userProfileProvider == null)
        {
            userProfileProvider = new UserProfileProvider();
        }
        
        userProfileProvider.getMandatoryNode().getUserInfo().plainPhoneNumber = latestPtnData.ptn;
        userProfileProvider.getMandatoryNode().getUserInfo().phoneNumber = latestPtnData.ptn;
        userProfileProvider.getMandatoryNode().getUserInfo().ptnType = latestPtnData.ptn_type;
        userProfileProvider.getMandatoryNode().getClientInfo().deviceCarrier = latestPtnData.carrier;
        
        int loginType = -1;
        
        if(accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
        {
            loginType = ILoginProxy.FUTE_CREATE_ACCOUNT;
        }
        else
        {
            loginType = ILoginProxy.FUTE_SIGN_IN;
        }
        
        boolean needHttpsLoginRequest = latestPtnData.ptn_type.equals(BillingAccountDao.PTN_TYPE_ENCRYPTED) ? false : true;
        
        ILoginProxy loginProxy = ServerProxyFactory.getInstance().createLoginProxy(null,CommManager.getInstance().getComm(), this,userProfileProvider);
        loginProxy.sendLogin(loginType, verificationCode, needHttpsLoginRequest);
        
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.startLogin();
        this.postModelEvent(EVENT_MODEL_FTUE_LOADING);
    }
    
    protected void saveSimcardId()
    {
        TnSimCardInfo simCardInfo = TnTelephonyManager.getInstance().getSimCardInfo();
        if(simCardInfo != null)
        {
            DaoManager.getInstance().getBillingAccountDao().setSimCardId(simCardInfo.simCardId);
        }
    }
    
    protected void doRequestVerificationCode()
    {
        clearMadantoryNode();
        
        IUserProfileProvider userProfileProvider = new UserProfileProvider();;
        
        userProfileProvider.getMandatoryNode().getUserInfo().plainPhoneNumber = latestPtnData.ptn;
        userProfileProvider.getMandatoryNode().getUserInfo().phoneNumber = latestPtnData.ptn;
        userProfileProvider.getMandatoryNode().getUserInfo().ptnType = latestPtnData.ptn_type;
        
        ILoginProxy loginProxy = ServerProxyFactory.getInstance().createLoginProxy(null,
            CommManager.getInstance().getComm(), this, userProfileProvider);
        loginProxy.requestVerificationCode();
    }
    
    protected void doForgotPin()
    {
        ILoginProxy loginProxy = ServerProxyFactory.getInstance().createLoginProxy(null,
            CommManager.getInstance().getComm(), this, null);
        loginProxy.sendForgetPin();
    }
    
    protected void checkContext()
    {
        LoginController.UserLoginData existUserLoginData = ((DaoManager)DaoManager.getInstance()).getUpgradeDao().getUserLoginData();
        
        if (DaoManager.getInstance().getBillingAccountDao().isLogin())
        {
            this.postModelEvent(EVENT_MODEL_LOGIN_FINISH);
        }
        else if (isUseExistLoginData(existUserLoginData))
        {
            CredentialInfo credentialInfo = existUserLoginData.credentialInfo;
            
            put(KEY_I_LOGIN_TYPE, BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_IN);
            
            if (credentialInfo.credentialValue != null && credentialInfo.credentialValue.trim().length() > 0)
            {
                put(KEY_S_EMAILADDRESS, credentialInfo.credentialValue);
            }

            if (credentialInfo.credentialPassword != null && credentialInfo.credentialPassword.trim().length() > 0)
            {
                put(KEY_S_PASSWORD, credentialInfo.credentialPassword);
            }
            
            latestPtnData = existUserLoginData.ptnData;
            
            sendLoginRequest();
            
            this.put(KEY_B_UPGRADE_LOGIN, true);
        }
        else
        {
            if (existUserLoginData != null && existUserLoginData.ptnData != null)
            {
                existPtnData = existUserLoginData.ptnData;
            }
            
            DaoManager.getInstance().getBillingAccountDao().setLoginType(BillingAccountDao.ACCOUNT_ENTRY_TYPE_UNKNOWN);
            put(KEY_I_LOGIN_TYPE, BillingAccountDao.ACCOUNT_ENTRY_TYPE_UNKNOWN);
            
            clearMadantoryNode();
            
//            startDimProcess();
            
            this.postModelEvent(EVENT_MODEL_LAUNCH_FTUE);
        }
    }
    
    //so far we only use the exist login Data for 'User has signed in Scout in Titan'
    protected boolean isUseExistLoginData(UserLoginData existUserLoginData)
    {
        if (existUserLoginData != null)
        {
            CredentialInfo credentialInfo = existUserLoginData.credentialInfo;
            if (credentialInfo != null && credentialInfo.credentialType != null
                    && credentialInfo.credentialType.trim().length() > 0 && credentialInfo.credentialID != null
                    && credentialInfo.credentialID.trim().length() > 0 && credentialInfo.credentialValue != null
                    && credentialInfo.credentialValue.trim().length() > 0 && credentialInfo.credentialPassword != null
                    && credentialInfo.credentialPassword.trim().length() > 0)
            {
                return true;
            }
        }
        
        return false;
    }
    
    protected void prefetchUserInfo()
    {
       String userName = AccountFetcher.getUserName(AndroidPersistentContext.getInstance().getContext());
       String primaryEmail = AccountFetcher.getEmail(AndroidPersistentContext.getInstance().getContext());
       if(userName != null && userName.matches("\\w+\\s\\w+"))
       {
           if( userName.split("\\s")[0] != null && userName.split("\\s")[0].trim().length() != 0)
           {
               this.put(KEY_S_PREFETCH_FIRSTNAME, userName.split("\\s")[0]);
           }
           if( userName.split("\\s")[1] != null && userName.split("\\s")[1].trim().length() != 0)
           {
               this.put(KEY_S_PREFETCH_LASTNAME, userName.split("\\s")[1]);
           }
       }
       if(primaryEmail != null && primaryEmail.trim().length() != 0)
       {
           this.put(KEY_S_PREFETCH_EMAILADDRESS, primaryEmail);
       }
    }
    
    protected void startDimProcess()
    {
        DimProvider.getInstance().setDimListner(this);
        
        DimProvider.getInstance().retrieveDimPtn();
    }
    
    protected void clearMadantoryNode()
    {       
        if (!isStartedByOtherController())
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                    .getUserInfo().phoneNumber = "";
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode()
                    .getUserInfo().userId = "";
        }
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().pin = "";
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().eqpin = "";
        
        CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
        credentialInfo.credentialPassword = "";
        credentialInfo.credentialType = "";
        credentialInfo.credentialValue = "";
        credentialInfo.credentialID = "";
    }

    private boolean isStartedByOtherController()
    {
        return this.getBool(ICommonConstants.KEY_B_IS_START_BY_OTHER_CONTROLLER);
    }
    
    protected void restoreProcess()
    {
        int accountEntryType = getInt(KEY_I_LOGIN_TYPE);
        if (accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_MAYBE_LATER)
        {
            this.remove(KEY_S_FIRSTNAME);
            this.remove(KEY_S_LASTNAME);
            this.remove(KEY_S_EMAILADDRESS);
            sendLoginRequest();
        }
        else if (accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
        {
            if(getState() != STATE_FTUE_SHOW_SIGN_UP)
            {
                this.postModelEvent(EVENT_MODEL_FTUE_SIGN_UP);
            }
            else
            {
                sendLoginRequest();
            }
        }
        else if (accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_IN)
        {
            if(getState() != STATE_FTUE_SHOW_SIGN_IN)
            {
                this.postModelEvent(EVENT_MODEL_FTUE_SIGN_IN);
            }
            else
            {
                sendLoginRequest();
            }
        }
    }
    
    protected void saveLoginStatus()
    {
        DaoManager.getInstance().getBillingAccountDao().setAccountStatus(BillingAccountDao.ACCOUNT_STATUS_NORMAL);
        
        AppStatusMisLog appStatusMisLog = (AppStatusMisLog) MisLogManager.getInstance().getMisLog(
            IMisLogConstants.TYPE_INNER_APP_STATUS);
        appStatusMisLog.finishLogin();
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if(proxy instanceof ILoginProxy)
        {
            ((DaoManager)DaoManager.getInstance()).getUpgradeDao().clear();
            
            ILoginProxy loginProxy = (ILoginProxy)proxy;
            int status = loginProxy.getAccountStatus();
            String errMsg = proxy.getErrorMsg();
            if (errMsg == null || errMsg.trim().length()<=0)
            {
                int loginInType = getInt(KEY_I_LOGIN_TYPE);
                if (loginInType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
                {
                    errMsg = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringLogin.RES_LABEL_CREATE_ACCOUNT_EXCEPTION_MSG, IStringLogin.FAMILY_LOGIN);
                }
                else
                {
                    errMsg = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringLogin.RES_LABEL_LOGIN_EXCEPTION_MSG, IStringLogin.FAMILY_LOGIN);
                }
            }
            
            boolean isLoginRequest =  IServerProxyConstants.ACT_LOGIN.equals(proxy.getRequestAction()) || IServerProxyConstants.ACT_ENCRYPT_LOGIN.equals(proxy.getRequestAction());
            if (isLoginRequest)
            {
                TransactionLocker locker = getTransactionLocker();
                if (locker.getTransactionStatus() == TransactionLocker.TRANSACTION_INIT)
                {
                    locker.setTransactionStatus(TransactionLocker.TRANSACTION_ERROR);
                }
                else
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "TransactionLocker : " + locker.transactionStatus);
                }
            }
            
            if(status != BillingAccountDao.ACCOUNT_STATUS_NORMAL)
            {    
                int eventId;
                switch (status)
                {
                    case BillingAccountDao.ACCOUNT_STATUS_EMAIL_MISMATCH:
                    {
                        eventId = EVENT_MODEL_EMAIL_NOT_REGISTERED;
                        postError(errMsg, eventId);
                        break;
                    }
                    case BillingAccountDao.ACCOUNT_STATUS_PASSWORD_MISMATCH:
                    {
                    
                        if (this.fetchBool(KEY_B_UPGRADE_LOGIN))
                            eventId = EVENT_MODEL_UPGRADE_LOGIN_FAILED;
                        else
                            eventId = EVENT_MODEL_PASSWARD_INCORRECT;                          
                        this.postError(errMsg,eventId);
                        break;
                    }
                    case BillingAccountDao.ACCOUNT_STATUS_EMAIL_REGISTERED:
                    {
                        eventId = EVENT_MODEL_EMAIL_ALREADY_REGISTERED;
                        postError(errMsg, eventId);
                        break;
                    }
                    default:
                    {
                        String productType = loginProxy.getAccountType();
                        this.put(KEY_S_PRODUCT_TYPE, productType);
                        this.postErrorMessage(errMsg);
                        break;
                    }
                }
            }
            else
            {
                this.postErrorMessage(errMsg);
            }
        }
        else if (proxy instanceof IToolsProxy)
        {
            checkBack();
        }
        else
        {
             super.transactionError(proxy);
        }
    }
    
    protected void saveLoginType()
    {
        int loginType = fetchInt(KEY_I_LOGIN_TYPE);
        DaoManager.getInstance().getBillingAccountDao().setLoginType(loginType);
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        if(proxy instanceof IToolsProxy)
        {
            checkBack();
        }
        else
        {
            boolean isLoginRequest=(proxy instanceof ILoginProxy) && (proxy.getRequestAction().equals(IServerProxyConstants.ACT_LOGIN)
            || proxy.getRequestAction().equals(IServerProxyConstants.ACT_ENCRYPT_LOGIN));
            if (isLoginRequest)
            {
                TransactionLocker locker= getTransactionLocker();
                if(locker.getTransactionStatus()==TransactionLocker.TRANSACTION_INIT)
                {
                    locker.setTransactionStatus(TransactionLocker.TRANSACTION_NETWORK_ERROR);
                    super.networkError(proxy, statusCode, jobId);
                }
                else
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "TransactionLocker : " + locker.transactionStatus);
                }
            }
            else
            {
                super.networkError(proxy, statusCode, jobId);
            }
        }
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy , String jobId)
    {
        if(proxy instanceof ILoginProxy)
        {
            ILoginProxy loginProxy = (ILoginProxy)proxy;
            
            if(proxy.getStatus() == STATUS_VERICATION_CODE_ERROR)
            {
                String errorMsg = ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringLogin.RES_INFO_VERIFICATION_CODE_ERROR, IStringLogin.FAMILY_LOGIN);
                this.put(KEY_S_ERROR_MESSAGE, errorMsg);
                this.remove(KEY_S_VERIFY_CODE);
                this.postModelEvent(EVENT_MODEL_VERIFY_PTN_FAIL);
            }
            else if(proxy.getStatus() == IServerProxyConstants.SUCCESS) // handle login
            {
                ((DaoManager)DaoManager.getInstance()).getUpgradeDao().clear();
                
            	if(proxy.getRequestAction().equals(IServerProxyConstants.ACT_FORGET_PIN))
                {
                    this.postModelEvent(EVENT_MODEL_PIN_SENT);
                }
            	else if(proxy.getRequestAction().equals(IServerProxyConstants.ACT_REQUEST_VERIFICATION_CODE))
            	{
            	    this.postModelEvent(EVENT_MODEL_VERI_CODE_SENT);
            	}
                else if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_LOGIN)
                        || proxy.getRequestAction().equals(IServerProxyConstants.ACT_ENCRYPT_LOGIN))
            	{
                    int accountEntryType = getInt(KEY_I_LOGIN_TYPE);
                    if(accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP)
                    {
                        KontagentLogger.getInstance().addCustomEvent(getCategoryByFrom(), KontagentLogger.EMAIL_CREATE_ACCT_FINISHED);
                        MobileAppConversionTracker.getInstance().trackAction(MobileAppConversionTracker.ACTION_REGISTRATION);
                    }
                    else if(accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_IN)
                    {
                        KontagentLogger.getInstance().addCustomEvent(getCategoryByFrom(), KontagentLogger.EMAIL_SIGNIN_SUCCESSFUL);
                    }
                    TransactionLocker locker= getTransactionLocker();
                    if (locker.getTransactionStatus() == TransactionLocker.TRANSACTION_INIT)
                    {
                        locker.setTransactionStatus(TransactionLocker.TRANSACTION_SUCCESS);
                        saveUserProfile(loginProxy);
                        setupNavService();
                        saveLoginStatus();
                        saveSimcardId();
                        saveLoginType();

                        DaoManager.getInstance().getBillingAccountDao().store();
                        DaoManager.getInstance().getMandatoryNodeDao().store();
                        DaoManager.getInstance().getPreferenceDao().store(getRegion());

                        if (isStartedByOtherController())
                        {
                            AccountChangeListener.getInstance().accountChanged();
                        }
                        
                        checkSyncType();
                    }
                    else
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "TransactionLocker : " + locker.transactionStatus);
                    }
            	}
            }
        }
        else if (proxy instanceof IToolsProxy)
        {
            savePreference((IToolsProxy) proxy);
            checkBack();
        }
    }
    
    protected void checkBack()
    {
        if (isStartedByOtherController())
        {
            synchronizeStartUp();
            this.postModelEvent(EVENT_MODEL_BACK_TO_LAUNCH);
        }
        else
        {
            this.postModelEvent(EVENT_MODEL_FTUE_SUCCESS);
        }
    }
   
    protected void synchronizeStartUp()
    {
        IStartupProxy startupProxy = ServerProxyFactory.getInstance().createStartupProxy(null, CommManager.getInstance().getComm(), null);

        long lastSyncTime = DaoManager.getInstance().getAddressDao().getSyncTime(false);
        startupProxy.synchronizeStartUp(lastSyncTime);
    }
    
    /**
     * when transaction finish, save user profile.
     */
    protected void saveUserProfile(ILoginProxy loginProxy)
    {
        if(loginProxy == null)
        {
            return;
        }
        
        String ptn = loginProxy.getPtn();
        
        if(AppConfigHelper.isLoggerEnable && ( ptn == null || ptn.trim().length() == 0 ))
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "Login model transactionFinishedDelegate -- Empty ptn : " + ptn);
        }
        
        if(ptn != null && ptn.length() > 0)
        {
            DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().phoneNumber = ptn;
        }
        
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().pin = loginProxy.getPin();
        
        if(AppConfigHelper.isLoggerEnable && ( loginProxy.getUserId() == null || loginProxy.getUserId().trim().length() == 0 ))
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "Login model transactionFinishedDelegate -- Empty userId : " + loginProxy.getUserId());
        }
        
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId = loginProxy.getUserId();
        DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().eqpin = (loginProxy.getEqPin() == null ? "" : loginProxy.getEqPin());
        DaoManager.getInstance().getBillingAccountDao().setPtn(ptn);
        DaoManager.getInstance().getBillingAccountDao().setSocType(loginProxy.getSoc_code());
        saveCredentialInfo(loginProxy.getCredentialId());
        NavsdkUserManagementService.getInstance().userProfileUpdate();
        
        DaoManager.getInstance().getBillingAccountDao().store();
        DaoManager.getInstance().getMandatoryNodeDao().store();
    }
    
    /**
     * userId is required by NavService.
     */
    protected void setupNavService()
    {
        NavServiceParameter param = new NavServiceParameter();
        String userId = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getUserInfo().userId;
        if(userId != null && userId.trim().length() > 0)
        {
            param.setUserId(userId);
        }
        NavServiceManager.getNavService().setNavServiceParameters(param);
        
        Host host = CommManager.getInstance().getComm().getHostProvider().createHost(IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR);
        if (host != null && host.host != null)
        {
            String url = "http://" + host.host;
            if(host.port > 0)
            {
                url += ":" + host.port;
            }
            param.setServerUrl(url + "/resource-cserver/telenav-server-pb");
        }
        param.setForeground(true);
        NavServiceManager.getNavService().startNavService(param);
    }
    
    
    /**
     * when transaction finish, save credential. 
     */
    protected void saveCredentialInfo(String credentialId)
    {
        PreferenceDao prefDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        String firstName = this.fetchString(KEY_S_FIRSTNAME);
        String lastName = this.fetchString(KEY_S_LASTNAME);
        String email = this.fetchString(KEY_S_EMAILADDRESS);
        if (checkName(firstName))
        {
            prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME, firstName);
        }

        if (checkName(lastName))
        {
            prefDao.setStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME, lastName);
        }

        CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
        credentialInfo.credentialPassword = this.fetchString(KEY_S_PASSWORD);

        int accountEntryType = getInt(KEY_I_LOGIN_TYPE);
        if (accountEntryType != BillingAccountDao.ACCOUNT_ENTRY_TYPE_MAYBE_LATER)
        {
            credentialInfo.credentialType = ILoginProxy.FUTE_CREDENTIAL_EMAIL;
        }

        credentialInfo.credentialValue = email;
        credentialInfo.credentialID = credentialId;
    }
    
    /**
     * Be called when dimFinish.
     */
    public void dimFinished()
    {
        DimProvider.getInstance().setDimListner(null);
        
        postModelEvent(EVENT_MODEL_DIM_FINISH);
    }
    
    /**
     * check if the input string is full of number.
     * if so, return true; else, return false.
     * @param number
     * @return true -- legal, false -- illegal
     */
    protected boolean checkNum(String number)
    {
        if(number == null)
        {
            return false;
        }
        
        if(number.trim().length() == 0)
        {
            return false;
        }
        
        for(int i = 0; i < number.length(); i++)
        {
            char c = number.charAt(i);
            if(c < '0' || c > '9')
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * check the length of the PTN 
     * base on different situation.
     * @param ptn
     * @return true -- legal, false -- illegal
     */
    protected boolean checkLength(String ptn)
    {
        if(ptn == null || ptn.trim().length() == 0)
        {
            return false;
        }
        
        int ptnLength = PTN_TYPEIN_LENGTH;
        
        if(ptn.length() == ptnLength)
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * check if the email string is legal
     * @param email
     * @return true -- legal, false -- illegal
     */
    protected boolean checkEmail(String email)
    {
        boolean isMatch = false;
        if (email != null)
        {
            isMatch = EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        }
        return isMatch;
    }

    /**
     * check if the name string is legal 
     * @param name
     * @return true -- legal, false -- illegal
     */
    protected boolean checkName(String name)
    {
        boolean isLegal = false;
        if (name != null && name.trim().length() > 0)
            isLegal = true;
        return isLegal;
    }

    /**
     * check if the pwd string is legal
     * @param password
     * @return true -- legal, false -- illegal
     */
    protected boolean checkPassword(String password)
    {
        boolean isLegal = false;
        if (password != null)
        {
            int length = password.trim().length();
            if (length >= PASSWORD_MIN_LENGTH && length <= PASSWORD_MAX_LENGTH)
                isLegal = true;
        }
        return isLegal;
    }
    
    /**
     * check if the ptnData string is legal depending on
     * the type of the ptn and the international context
     * @param data
     * @return true -- legal, false -- illegal
     */
    protected boolean checkPtn(PtnData data)
    {
        if(data == null || data.ptn == null || data.ptn.trim().length() == 0)
        {
            return false;
        }
        
        if(data.ptn_type.equalsIgnoreCase(BillingAccountDao.PTN_TYPE_ENCRYPTED))
        {
            return true;
        }
        else
        {
            boolean needCheckLength = true;
            
            if(AppConfigHelper.isGlobalPtn())
            {
                needCheckLength = false;
                data.ptn = AppConfigHelper.removePtnGlobalHead(data.ptn);
            }
            
            boolean isAllNum = checkNum(data.ptn);
            boolean isLengthLegal = needCheckLength ? checkLength(data.ptn) : true;
            
            if(isAllNum && isLengthLegal)
            {
                return true;
            }
        }
        
        return false;
    }
    
    public String getCategoryByFrom()
    {
        String categoryType = "";
        int loginFrom = getInt(ICommonConstants.KEY_I_LOGIN_FROM);
        switch(loginFrom)
        {
            case TYPE_LOGIN_FROM_ENTRY:
                categoryType = KontagentLogger.CATEGORY_FTUE;
                break;
            case TYPE_LOGIN_FROM_DASHBOARD:
                categoryType = KontagentLogger.CATEGORY_DASHBOARD;
                break;
            case TYPE_LOGIN_FROM_RECENT:
                categoryType = KontagentLogger.CATEGORY_RECENTS;
                break;
            case TYPE_LOGIN_FROM_FAVORITE:
                categoryType = KontagentLogger.CATEGORY_MY_PLACES;
                break;
            case TYPE_LOGIN_FROM_PREFERENCE:
                categoryType = KontagentLogger.CATEGORY_MYPROFILE;
                break;
        }
        return categoryType;
    }
    
//  if (typePtn == null || typePtn.length() == 0 || (needCheckLength && typePtn.length() < ptnLength))
//  {
//      String errorMsg; 
//      if ((typePtn == null || typePtn.length() == 0) && AppConfigHelper.BRAND_TTX.equals(AppConfigHelper.brandName))
//      {
//          //FIX CANNON-228
//          errorMsg = ResourceManager.getInstance().getCurrentBundle()
//                  .getString(IStringLogin.RES_LABEL_NO_TYPEIN, IStringLogin.FAMILY_LOGIN);
//      }
//      else
//      {
//          errorMsg = ResourceManager.getInstance().getCurrentBundle()
//                  .getString(IStringLogin.RES_LABEL_LOGIN_FAIL_MSG, IStringLogin.FAMILY_LOGIN);
//      }
//      this.put(KEY_S_ERROR_MESSAGE, errorMsg);
//      this.postModelEvent(EVENT_MODEL_PTN_UNAVAILABLE);
//  }
//  else
//  {
//      boolean isValid = checkNum(typePtn);
//      if(isValid)
//      {
//          checkEntry();
//      }
//      else
//      {
//          String errorMsg = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_LOGIN_FAIL_MSG,
//              IStringLogin.FAMILY_LOGIN);
//          this.put(KEY_S_ERROR_MESSAGE, errorMsg);
//          this.postModelEvent(EVENT_MODEL_PTN_UNAVAILABLE);
//      }
//  }
    
    /**
     * read PTN data from sim card.
     * @return
     */
    protected PtnData readPtnFromSimcard()
    {
        String ptn = "";
        if(!AppConfigHelper.isSkipSmsPtnDiscovery())
        {
            if (!TnTelephonyManager.getInstance().getDeviceInfo().isSimulator)
            {
                ptn = TnTelephonyManager.getInstance().getPhoneNumber();
            }

            if (ptn != null)
            {
                ptn = ptn.trim();
            }

            if (ptn == null || ptn.startsWith("00000")) // FIXME
            {
                ptn = "";
            }
            
            boolean needCheckLength = true;
            
            if(AppConfigHelper.isGlobalPtn())
            {
                needCheckLength = false;
            }
            
            if(needCheckLength && ptn != null && ptn.length() > PTN_TYPEIN_LENGTH)
            {
                ptn = ptn.substring(ptn.length() - PTN_TYPEIN_LENGTH);
            }
        }
        
        PtnData simPtnData = new PtnData();
        
        simPtnData.ptn = ptn;
        simPtnData.ptn_type = BillingAccountDao.PTN_TYPE_SIM; 
        simPtnData.carrier = "";
        
        return simPtnData;

    }
    
    /**
     * read PTN data from dim process
     * @return
     */
    protected PtnData readPtnDataFromDim()
    {
        PtnData dimPtnData = new PtnData();
        
        dimPtnData.ptn = DimProvider.getInstance().getPtn();
        dimPtnData.ptn_type = DimProvider.getInstance().isPtnEncrypted() ? BillingAccountDao.PTN_TYPE_ENCRYPTED : BillingAccountDao.PTN_TYPE_SIM; 
        dimPtnData.carrier = DimProvider.getInstance().getCarrier();
        
        return dimPtnData;
    }
    
    protected TransactionLocker getTransactionLocker()
    {
        if(transactionLocker==null)
            transactionLocker = new TransactionLocker();
        return transactionLocker;
    }
    
    protected static class TransactionLocker
    {
        private int transactionStatus=TRANSACTION_INIT;

        final static int TRANSACTION_INIT = 0;

        final static int TRANSACTION_SUCCESS = 1;

        final static int TRANSACTION_ERROR = 2;

        final static int TRANSACTION_NETWORK_ERROR = 3;

        public synchronized void setTransactionStatus(int status)
        {
            transactionStatus = status;
        }
        
        public synchronized int getTransactionStatus()
        {
            return transactionStatus;
        }
    }
    
}
