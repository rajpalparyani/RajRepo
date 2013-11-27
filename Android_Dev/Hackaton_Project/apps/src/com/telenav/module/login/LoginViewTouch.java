/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * LoginViewTouch.java
 *
 */
package com.telenav.module.login;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.cache.ImageCacheManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.serverproxy.BillingAccountDao;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.entry.SecretKeyListener;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringLogin;
import com.telenav.res.IStringNav;
import com.telenav.res.IStringPreference;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.android.ImageIndicator;
import com.telenav.ui.android.OneStepGallery;
import com.telenav.ui.android.RatioSolidImageView;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.util.PrimitiveTypeCache;


/**
 *@author bduan
 *@date 2010-11-26
 */
class LoginViewTouch extends AbstractCommonView implements ILoginConstants
{
    protected OneStepGallery mGallery = null;

    protected ImageIndicator imageIndexer = null;
    
    private View  welcomeView;

    private View  ftueSignupView;
    
    private View  ftueSignInView;
    
    private View  ftueptnInputView;
    
    private View  ftueVarificationInputView;
    
    private View  ftueEditAccountView;
    
    public LoginViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_WAIT_DIM_PROCESS:
            {
                String strTip = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABLE_SENDING_PIN, IStringLogin.FAMILY_LOGIN);
                FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, strTip);
                popup=progressBox;
                break;
            }
            case STATE_SENDING_FORGET_REQUESET:
            case STATE_REQUEST_VERIFICATION_CODE:
            {
            	String strTip = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABLE_SENDING_PIN, IStringLogin.FAMILY_LOGIN);
            	FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, strTip);
            	popup=progressBox;
            	break;
            }
            case STATE_PIN_SENT:
            {
            	popup = createPinSentMsg(state);
            	break;
            }
            case STATE_FTUE_LORDING:
            {
                int accountEntryType = model.getInt(KEY_I_LOGIN_TYPE);
                boolean isCreateAccount= (accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP);
                String str;
                if(isCreateAccount)
                {
                  str = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_MSG_CREATING_ACCOUNT, IStringLogin.FAMILY_LOGIN);
                }
                else
                {
                  str = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_LOGIN_POPUP_MSG, IStringLogin.FAMILY_LOGIN);  
                }
                FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(state, str);
                popup = progressBox;
                break;
            }
            case STATE_FTUE_CHECK_EMAIL_ERROR:
            case STATE_FTUE_VALIDATION_PASSWORD_ERROR:
            case STATE_FTUE_LOGIN_EMAIL_NOT_REGISTERED:
            case STATE_FTUE_LOGIN_PASSWORD_INCORRECT:
            case STATE_FTUE_LOGIN_EMAIL_ALREADY_REGISTERED:
            {
                resetView(state);
                popup = createFTUEValidationErrorMsgBox(state, CMD_COMMON_BACK);
                break;
            }
            case STATE_PTN_VERIFY_FAIL:
            {
                EditText ptninputEditText = (EditText) ftueVarificationInputView.findViewById(R.id.ptn);
                ptninputEditText.setText("");
                popup = createFTUEValidationErrorMsgBox(state, CMD_COMMON_BACK);
                break;
            }
            case STATE_FTUE_EDIT_ACCOUNT:
            {
                FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(state, 
                    ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_STR_SAVING, IStringPreference.FAMILY_PREFERENCE));
                popup = progressBox;
                break;
            }
            case STATE_UPGRADE_LOGIN_FAILED:
            {
                popup = createFTUEValidationErrorMsgBox(state, CMD_UPGRADE_BACK_TO_FTUE);
                break;
            }
        }
        return popup;
    }

    protected TnPopupContainer createPinSentMsg(int state)
    {
    	TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
             IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        
        String strMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_PIN_SENT, IStringLogin.FAMILY_LOGIN);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, strMessage, menu);
    	return messageBox;
    }
    
    protected TnPopupContainer createFTUEValidationErrorMsgBox(int state, int command)
    {    
        TnMenu menu = UiFactory.getInstance().createMenu();
        String infoStr = this.model.fetchString(KEY_S_ERROR_MESSAGE);
        String tryAgain = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_TRY_AGAIN, IStringLogin.FAMILY_LOGIN);
        menu.add(tryAgain, command);     
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, infoStr, menu);
        return messageBox; 
    }
    
    private void resetView(int state)
    {
        int accountEntryType = model.getInt(KEY_I_LOGIN_TYPE);
        boolean isCreatingAccount = (accountEntryType == BillingAccountDao.ACCOUNT_ENTRY_TYPE_SIGN_UP);
        
        View view;
        
        if (isCreatingAccount)
        {
            view = ftueSignupView;
        }
        else
        {
            view = ftueSignInView;
        }
        
        if (view == null)
        {
            return;
        }
        
        EditText clearedViewEmail = (EditText)view.findViewById(R.id.emailaddress);
        EditText clearedViewPassword = (EditText)view.findViewById(R.id.password);
        
        if(clearedViewPassword != null)
        {
            clearedViewPassword.setText("");
        }
        
        if (state == STATE_FTUE_CHECK_EMAIL_ERROR && clearedViewEmail != null)
        {
            clearedViewEmail.setText("");
        }
        
        if (state == STATE_FTUE_CHECK_EMAIL_ERROR || state == STATE_FTUE_LOGIN_EMAIL_NOT_REGISTERED || state == STATE_FTUE_LOGIN_EMAIL_ALREADY_REGISTERED)
        {
            clearedViewEmail.requestFocus();  
        }
        else
        {
            clearedViewPassword.requestFocus();
        }
    }
    
    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        
        switch(state)
        {
            case STATE_FTUE_WELCOME:
            {
                screen = createFtueWelcomeScreen(state);
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_FTUE, KontagentLogger.FTUE_LANDING_PAGE_DISPLAYED);
                break;
            }
            case STATE_FTUE_SHOW_SIGN_UP:
            {
                screen = createNativeSignUpScreen(state);
                KontagentLogger.getInstance().addCustomEvent(((LoginModel)model).getCategoryByFrom(), KontagentLogger.CREATE_ACCOUNT_PAGE_DISPLAYED);
                break;
            }
            case STATE_FTUE_SHOW_SIGN_IN:
            {
                screen = createNativeSignInScreen(state);
                KontagentLogger.getInstance().addCustomEvent(((LoginModel)model).getCategoryByFrom(), KontagentLogger.LOGIN_PAGE_DISPLAYED);
                break;
            }
            case STATE_TYPEIN_PTN:
            {
                screen = createNativePTNInput(state, false);
                break;
            }
            case STATE_INPUT_VERIFICATION_CODE:
            {
                screen = createNativePTNInput(state, true);
                break;
            }
            case STATE_FTUE_SHOW_EDIT_ACCOUNT:
            {
                screen = createNativeEditAccountScreen(state);
                break;
            }
            case STATE_FORGET_PASSWORD:
            {
                screen = createForgetPasswordPage(state);
                break;
            }
            
        }
        
        return screen;
    }
    
   
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (tnUiEvent.getCommandEvent() != null)
                {
                    int command = tnUiEvent.getCommandEvent().getCommand();
                    if(command == CMD_FTUE_SIGN_UP_SUBMIT)
                    {
                        if (ftueSignupView != null)
                        {
                            EditText firstnameEditText = (EditText) ftueSignupView
                                    .findViewById(R.id.firstname);
                            EditText lastnameEditText = (EditText) ftueSignupView
                                    .findViewById(R.id.lastname);
                            EditText emailAddressEditText = (EditText) ftueSignupView
                                    .findViewById(R.id.emailaddress);
                            EditText passwdEditText = (EditText) ftueSignupView
                                    .findViewById(R.id.password);
                            String firstName = firstnameEditText.getText().toString();
                            String lastName = lastnameEditText.getText().toString();
                            String emailAddress = emailAddressEditText.getText().toString();
                            String password = passwdEditText.getText().toString();
                            model.put(KEY_S_FIRSTNAME, firstName);
                            model.put(KEY_S_LASTNAME, lastName);
                            model.put(KEY_S_EMAILADDRESS, emailAddress);
                            model.put(KEY_S_PASSWORD, password);
                            TeleNavDelegate.getInstance().closeVirtualKeyBoard(null);
                        }
                    }
                    else if(command == CMD_FTUE_SIGN_IN_SUBMIT)
                    {
                        if (ftueSignInView != null)
                        {
                            EditText emailAddressEditText = (EditText) ftueSignInView.findViewById(R.id.emailaddress);
                            EditText passwdEditText = (EditText) ftueSignInView.findViewById(R.id.password);
                            String emailAddress=emailAddressEditText.getText().toString();
                            String password=passwdEditText.getText().toString();
                            model.put(KEY_S_EMAILADDRESS, emailAddress);
                            model.put(KEY_S_PASSWORD, password);
                            TeleNavDelegate.getInstance().closeVirtualKeyBoard(getKeyBoardAttachedComponent());
                        }
                    }
                    else if(command== CMD_COMMON_HOME)
                    {
                        TeleNavDelegate.getInstance().closeVirtualKeyBoard(getKeyBoardAttachedComponent());
                    }
                    else if(command==CMD_TYPEIN_CONTINUE)
                    {
                        TeleNavDelegate.getInstance().closeVirtualKeyBoard(getKeyBoardAttachedComponent());
                    }
                    else if(command==CMD_FTUE_EDIT_ACCOUNT)
                    {
                        if (ftueEditAccountView != null)
                        {
                            EditText firstnameEditText = (EditText) ftueEditAccountView
                                    .findViewById(R.id.firstname);
                            EditText lastnameEditText = (EditText) ftueEditAccountView
                                    .findViewById(R.id.lastname);
                            EditText emailAddressEditText = (EditText) ftueEditAccountView
                                    .findViewById(R.id.emailaddress);
                            String firstName = firstnameEditText.getText().toString();
                            String lastName = lastnameEditText.getText().toString();
                            String emailAddress = emailAddressEditText.getText().toString();
                            model.put(KEY_S_FIRSTNAME, firstName);
                            model.put(KEY_S_LASTNAME, lastName);
                            model.put(KEY_S_EMAILADDRESS, emailAddress);
                            TeleNavDelegate.getInstance().closeVirtualKeyBoard(getKeyBoardAttachedComponent());
                        }
                    }  
                }
                break;
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }
    
    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            if(command == CMD_FTUE_SIGN_UP)
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_FTUE, KontagentLogger.FTUE_CREATE_ACCOUNT_CLICKED);
            }
            else if(command == CMD_FTUE_SIGN_IN)
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_FTUE, KontagentLogger.FTUE_LOGIN_BUTTON_CLICKED);
            }
            else if(command == CMD_FTUE_MAYBE_LATER)
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_FTUE, KontagentLogger.FTUE_MAYBE_LATER_CLICKED);
            }
        }
    }
    
    private AbstractTnComponent  getKeyBoardAttachedComponent()
    {
        return ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getCurrentScreen().getRootContainer();
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch(state)
        {
            case STATE_TYPEIN_PTN:
            {
                if(commandId == CMD_TYPEIN_CONTINUE)
                {
                    if (ftueptnInputView != null)
                    {
                        EditText ptninputEditText = (EditText) ftueptnInputView.findViewById(R.id.ptn);
                        if(ptninputEditText != null)
                        {   
                            String ptn = ptninputEditText.getText().toString();
                            model.put(KEY_S_INPUT_STR, ptn);
                        }
                    }
                }
                break;
            }
            case STATE_INPUT_VERIFICATION_CODE:
            {
                if (commandId == CMD_TYPEIN_CONTINUE)
                {
                    EditText ptninputEditText = (EditText) ftueVarificationInputView.findViewById(R.id.ptn);
                    if(ptninputEditText != null)
                    {   
                        String verifyCode = ptninputEditText.getText().toString();
                        model.put(KEY_S_VERIFY_CODE, verifyCode);
                    }
                }
                break;
            }
            case STATE_FTUE_CHECK_EMAIL_ERROR:
            case STATE_FTUE_VALIDATION_PASSWORD_ERROR:
            case STATE_FTUE_LOGIN_EMAIL_NOT_REGISTERED:
            case STATE_FTUE_LOGIN_PASSWORD_INCORRECT:
            {
                resetView(state);
                break;
            }
            default:
                break;
        }
        
        return true;
    }
    
    protected TnScreen createNativePTNInput(int state, boolean isVerificationCode)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        View ftueptnView = AndroidCitizenUiHelper.addContentView(screen, R.layout.ftue_ptn_input);
        
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        ftueptnView.setBackgroundDrawable(bgImage);
        
        TextView titleTextView=(TextView)ftueptnView.findViewById(R.id.commonTitle0TextView);
        String loginTitle = AppConfigHelper.appName;
        titleTextView.setText(loginTitle);   
        
        ImageView splitlineImageView=(ImageView)ftueptnView.findViewById(R.id.screensplitline);
        AssetsImageDrawable imageDrawable = new AssetsImageDrawable(NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());        
        splitlineImageView.setBackgroundDrawable(imageDrawable);
          
        TextView declarationTextView=(TextView)ftueptnView.findViewById(R.id.declaration);
        TextView phoneNumberTextView=(TextView)ftueptnView.findViewById(R.id.phonenumber);
        final Button continueBtn=(Button)ftueptnView.findViewById(R.id.submit);
        String btnStr="", typeInStr = "",phoneNumStr = "";
        if (isVerificationCode)
        {
            typeInStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_INFO_INPUT_VERIFICATION_CODE, IStringLogin.FAMILY_LOGIN);
            String ptn = model.getString(KEY_S_INPUT_STR);
            
            //format the ptn to be like (xxx)xxx-xxxx 
            ptn = ResourceManager.getInstance().getStringConverter().convertPhoneNumber(ptn);
            
            //TODO: need to add color label for the ptn number display. Waiting for Duan Bo's change for PUSU.
            StringConverter converter = ResourceManager.getInstance().getStringConverter();
            typeInStr = converter.convert(typeInStr, new String[]{ptn});
            declarationTextView.setText(typeInStr);   
            
            phoneNumberTextView.setVisibility(View.GONE);
            btnStr = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringNav.RES_BTTN_CONTINUE, IStringNav.FAMILY_NAV);
            continueBtn.setText(btnStr);
        }
        else
        {
            typeInStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_INFO_TYPE_IN_PTN, IStringLogin.FAMILY_LOGIN);
            declarationTextView.setText(typeInStr);   
            
            phoneNumStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABLE_PTN_INPUT, IStringLogin.FAMILY_LOGIN);
            phoneNumberTextView.setVisibility(View.VISIBLE);
            phoneNumberTextView.setText(phoneNumStr);
            phoneNumberTextView.setShadowLayer(2, 0, 0, Color.BLACK);
            
            btnStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_INPUT_PTN, IStringLogin.FAMILY_LOGIN);
            continueBtn.setText(btnStr);
        }
        
        continueBtn.setTextColor(BTN_DISABLE_COLOR);
        
        final EditText ptnInput=(EditText)ftueptnView.findViewById(R.id.ptn);
        setLastTextFieldEnterFunction(ptnInput, CMD_TYPEIN_CONTINUE);
        
        int height = AndroidCitizenUiHelper.getPixelsByDip(81);
        ptnInput.addTextChangedListener(new LoginTextWatcher(ptnInput, null, continueBtn, height , isVerificationCode ? TYPE_VERIFY_CODE : TYPE_PTN));
        
        AndroidCitizenUiHelper.setOnClickCommand(this, continueBtn,  CMD_TYPEIN_CONTINUE);
        if(isVerificationCode)
            ftueVarificationInputView=ftueptnView;
        else
            ftueptnInputView=ftueptnView;
        return screen;
    }
    
    protected TnScreen createFtueWelcomeScreen(int state)
    {
        TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);

        TnMenu screenMenu = UiFactory.getInstance().createMenu();
        String exitStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_EXIT,
            IStringCommon.FAMILY_COMMON);        
            screenMenu.add(exitStr, ICommonConstants.CMD_COMMON_EXIT);
            screen.getRootContainer().setMenu(screenMenu, AbstractTnComponent.TYPE_MENU);
        
        welcomeView = AndroidCitizenUiHelper.addContentView(screen, R.layout.ftue_welcome);
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        welcomeView.setBackgroundDrawable(bgImage);
        welcomeView.setOnTouchListener(new SecretKeyListener(this,ICommonConstants.CMD_SECRET_KEY));
        initGallery(welcomeView);
        
        Button createAccoutbtn=(Button)welcomeView.findViewById(R.id.createaccout);
        String createAccountStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_CREATE_ACCOUNT, IStringLogin.FAMILY_LOGIN);       
        createAccoutbtn.setText(createAccountStr);
        createAccoutbtn.setTextColor(getColorState(R.color.login_button_font));
          
        Button loginbtn=(Button)welcomeView.findViewById(R.id.login);
        
        String loginStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_LOGIN, IStringLogin.FAMILY_LOGIN);      
        loginbtn.setText(loginStr);
        loginbtn.setTextColor(getColorState(R.color.login_button_font));
        
        final TextView laterTextView = (TextView) welcomeView.findViewById(R.id.later);
        
        boolean hideMaybeLaterDisabled = model.getBool(KEY_B_MEYBELATER_DISABLE);
        if (hideMaybeLaterDisabled)
        {
            laterTextView.setVisibility(View.INVISIBLE);
        }
        else
        {
            laterTextView.setVisibility(View.VISIBLE);
            String laterStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_LATER, IStringLogin.FAMILY_LOGIN);    
            laterTextView.setText(Html.fromHtml("<u>"+laterStr+"</u>"));
            laterTextView.setTextColor(LINK_TOUCH_UP_COLOR);
        }
        
        final TextView tosLeftTextView=(TextView)welcomeView.findViewById(R.id.tosleft);
        String tosLeftStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_TOS_ABOVE, IStringLogin.FAMILY_LOGIN);                   
        tosLeftTextView.setText(tosLeftStr+" ");
        
        final TextView tosRightTextView=(TextView)welcomeView.findViewById(R.id.tosright);
        String tosRightStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_TOS, IStringLogin.FAMILY_LOGIN);                    
        tosRightTextView.setText(Html.fromHtml("<u>"+tosRightStr+"</u>"));   
        tosRightTextView.setTextColor(LINK_TOUCH_UP_COLOR);
 
        AndroidCitizenUiHelper.setOnClickCommand(this, createAccoutbtn, CMD_FTUE_SIGN_UP);
        AndroidCitizenUiHelper.setOnClickCommand(this, loginbtn, CMD_FTUE_SIGN_IN);
        AndroidCitizenUiHelper.setOnClickCommand(this, laterTextView,   CMD_FTUE_MAYBE_LATER);
        AndroidCitizenUiHelper.setOnClickCommand(this, tosRightTextView,  CMD_FTUE_TOS);
        
       
        return screen;
    }

    protected void initGallery(View mainView)
    {
        mGallery = (OneStepGallery)mainView.findViewById(R.id.gallery);
        mGallery.setAdapter(new ImageAdapter(AndroidPersistentContext.getInstance().getContext()));
        mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {        
                imageIndexer.setSelection(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                
            }
        });
        
        int pixel = AndroidCitizenUiHelper.getPixelsByDip(10);
        mGallery.setSpacing(pixel);
        imageIndexer = (ImageIndicator) mainView.findViewById(R.id.imageindexer);
               
        Drawable dotIconUnfocused = new AssetsImageDrawable(ImageDecorator.IMG_FTUE_DOT_ICON_UNFOCUSED);
        Drawable dotIconFocused = new AssetsImageDrawable(ImageDecorator.IMG_FTUE_DOT_ICON_FOCUSED); 
        imageIndexer.initView(mGallery.getCount(), dotIconUnfocused,dotIconFocused);
        imageIndexer.setVisibility(View.VISIBLE);
    }
    
    protected TnScreen createForgetPasswordPage(int state)
    {
        BrowserSessionArgs args = new BrowserSessionArgs(CommManager.FORGOT_PASSWORD_URL_DOMAIN_ALIAS);
        
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        screen.getRootContainer().setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        AbstractTnContainer contentContainer = screen.getContentContainer();

        final CitizenWebComponent forgetPasswordWebView = UiFactory.getInstance().createCitizenWebComponent(this, 0, false);
        forgetPasswordWebView.reset();
        forgetPasswordWebView.enableAnimation(true);
        
        String url = args.getUrl();
        url = BrowserSdkModel.addEncodeTnInfo(url, "");
                
        //TODO how to resolve the differences in portrait and landscape 
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        
        forgetPasswordWebView.loadUrl(url);
        forgetPasswordWebView.setHtmlSdkServiceHandler(new BrowserSdkModel());
        forgetPasswordWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        forgetPasswordWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.uiDecorator.SCREEN_HEIGHT);
        contentContainer.add(forgetPasswordWebView);

        return screen;
    }
    
    protected TnScreen createNativeEditAccountScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        ftueEditAccountView = AndroidCitizenUiHelper.addContentView(screen, R.layout.ftue_edit_account);
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        ftueEditAccountView.setBackgroundDrawable(bgImage);
        
        TextView createAccoutTextView=(TextView)ftueEditAccountView.findViewById(R.id.commonTitle0TextView);
        String editAccountStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_EDIT_ACCOUNT_TITLE, IStringLogin.FAMILY_LOGIN);    
        createAccoutTextView.setText(editAccountStr);
          
        ImageView splitlineImageView=(ImageView)ftueEditAccountView.findViewById(R.id.screensplitline);
        int key = (Integer) NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        
        AssetsImageDrawable imageDrawable = new AssetsImageDrawable(NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());        
        splitlineImageView.setBackgroundDrawable(imageDrawable);
        
        TextView declareTextView=(TextView)ftueEditAccountView.findViewById(R.id.declaration);
        String declareStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_EDIT_ACCOUNT_DESC, IStringLogin.FAMILY_LOGIN);  
        declareTextView.setText(declareStr);
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao(); 
        
        EditText firstNameEditText=(EditText)ftueEditAccountView.findViewById(R.id.firstname);
        String firstNameHit = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_FIRSTNAME, IStringLogin.FAMILY_LOGIN);   
        firstNameEditText.setHint(firstNameHit);
        
        String firstName = preferenceDao.getStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME);
        if (firstName != null && firstName.length() > 0)
        {
            firstNameEditText.setText(firstName);
        }
        else
        {
            firstNameEditText.setText("");
        }
       
        EditText lastNameEditText=(EditText)ftueEditAccountView.findViewById(R.id.lastname);
        String lastNameHit =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_LASTNAME, IStringLogin.FAMILY_LOGIN); 
        lastNameEditText.setHint(lastNameHit);
        String lastName =preferenceDao.getStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME);
        if (lastName != null && lastName.length() > 0)
        {
            lastNameEditText.setText(lastName);
        }
        else
        {
            lastNameEditText.setText("");
        }
        setLastTextFieldEnterFunction(lastNameEditText, CMD_FTUE_EDIT_ACCOUNT);
          
        EditText emailAddressEditText=(EditText)ftueEditAccountView.findViewById(R.id.emailaddress);
        String emailAddressStr = null;
        CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
        if(credentialInfo != null && credentialInfo.credentialValue != null && credentialInfo.credentialValue.trim().length() > 0)
        {
            emailAddressStr = credentialInfo.credentialValue;
        }
        
        if(emailAddressStr != null)
        {
            emailAddressEditText.setText(emailAddressStr);
        }
        
        Button editAccountBtn=(Button)ftueEditAccountView.findViewById(R.id.submit);
        String finishStr =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_FINISH, IStringLogin.FAMILY_LOGIN);    
        editAccountBtn.setText(finishStr);
        editAccountBtn.setEnabled(true);
        editAccountBtn.setTextColor(BTN_ENABLED_COLOR);
        editAccountBtn.setTextColor(getColorState(R.color.login_button_font));
        firstNameEditText.addTextChangedListener(new LoginTextWatcher(
            firstNameEditText, lastNameEditText, editAccountBtn, 0, TYPE_FIRSTNAME));
        
        lastNameEditText.addTextChangedListener(new LoginTextWatcher(
            lastNameEditText, firstNameEditText, editAccountBtn, 0, TYPE_LASTNAME));
        
        AndroidCitizenUiHelper.setOnClickCommand(this, editAccountBtn,  CMD_FTUE_EDIT_ACCOUNT);
        return screen;
    }
    
    protected TnScreen createNativeSignUpScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        ftueSignupView = AndroidCitizenUiHelper.addContentView(screen, R.layout.ftue_sign_up);
     
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        ftueSignupView.setBackgroundDrawable(bgImage);
        
        TextView createAccoutTextView=(TextView)ftueSignupView.findViewById(R.id.commonTitle0TextView);
        String createAccoutStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_CREATE_ACCOUNT, IStringLogin.FAMILY_LOGIN);    
        createAccoutTextView.setText(createAccoutStr);
          
        ImageView splitlineImageView=(ImageView)ftueSignupView.findViewById(R.id.screensplitline);
        int key = (Integer) NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        
        AssetsImageDrawable imageDrawable = new AssetsImageDrawable(NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());        
        splitlineImageView.setBackgroundDrawable(imageDrawable);
        
        TextView declareTextView=(TextView)ftueSignupView.findViewById(R.id.declaration);
        String declareStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_DECLARATION, IStringLogin.FAMILY_LOGIN);  
        declareTextView.setText(declareStr);
      
        EditText firstnameEditText=(EditText)ftueSignupView.findViewById(R.id.firstname);
        String firstNameHit = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_FIRSTNAME, IStringLogin.FAMILY_LOGIN);   
        firstnameEditText.setHint(firstNameHit);
        
        String firstNamePreStr = model.getString(KEY_S_FIRSTNAME);
        if (firstNamePreStr != null && firstNamePreStr.trim().length() > 0)
        {
            firstnameEditText.setText(firstNamePreStr);
        }
        else
        {
            String prefetchFirstNamePreStr = model.getString(KEY_S_PREFETCH_FIRSTNAME);
            if (prefetchFirstNamePreStr != null && prefetchFirstNamePreStr.trim().length() > 0)
            {
                firstnameEditText.setText(prefetchFirstNamePreStr);
            }
        }
      
        EditText lastNameEditText=(EditText)ftueSignupView.findViewById(R.id.lastname);
        String lastNameHit =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_LASTNAME, IStringLogin.FAMILY_LOGIN);;;    
        lastNameEditText.setHint(lastNameHit);
        
        String lastNamePreStr = model.getString(KEY_S_LASTNAME);
        if (lastNamePreStr != null && lastNamePreStr.trim().length() > 0)
        {
            lastNameEditText.setText(lastNamePreStr);
        }
        else
        {
            String prefetchLastNamePreStr = model.getString(KEY_S_PREFETCH_LASTNAME);
            if (prefetchLastNamePreStr != null && prefetchLastNamePreStr.trim().length() > 0)
            {
                lastNameEditText.setText(prefetchLastNamePreStr);
            }
        }
        
        final Button finishBtn=(Button)ftueSignupView.findViewById(R.id.submit);
        String finishStr =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_FINISH, IStringLogin.FAMILY_LOGIN);    
        finishBtn.setText(finishStr);
        finishBtn.setTextColor(BTN_DISABLE_COLOR); 
        
        final AutoCompleteTextView emailAddressEditText=(AutoCompleteTextView)ftueSignupView.findViewById(R.id.emailaddress);
        final EditText passwdEditText=(EditText)ftueSignupView.findViewById(R.id.password);
        
        String passwdStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_PASSWORD, IStringLogin.FAMILY_LOGIN);     
        passwdEditText.setHint(passwdStr);
        int height = AndroidCitizenUiHelper.getPixelsByDip(132);
        passwdEditText.addTextChangedListener(new LoginTextWatcher(passwdEditText,
                emailAddressEditText, finishBtn, height, TYPE_PASSWORD));
        setLastTextFieldEnterFunction(passwdEditText, CMD_FTUE_SIGN_UP_SUBMIT);

        String emailAddressStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_EMAIL_ADDRESS, IStringLogin.FAMILY_LOGIN);     
        emailAddressEditText.setHint(emailAddressStr);
        height = AndroidCitizenUiHelper.getPixelsByDip(193);
        emailAddressEditText.addTextChangedListener(new LoginTextWatcher(emailAddressEditText, passwdEditText, finishBtn, height,TYPE_EMAIL));
        
        String emailPreStr = model.getString(KEY_S_EMAILADDRESS);
        if(emailPreStr != null && emailPreStr.trim().length() > 0)
        {
            emailAddressEditText.setText(emailPreStr);
        }
        else
        {
            String prefetchEmailPreStr = model.getString(KEY_S_PREFETCH_EMAILADDRESS);
            if (prefetchEmailPreStr != null && prefetchEmailPreStr.trim().length() > 0)
            {
                emailAddressEditText.setText(prefetchEmailPreStr);
            }
        }
        
        EmailFilterArrayAdapter adapter = new EmailFilterArrayAdapter((Context) AndroidPersistentContext.getInstance().getContext(), R.layout.scout_dropdown_item_1line);
        emailAddressEditText.setAdapter(adapter);
          
        AndroidCitizenUiHelper.setOnClickCommand(this, finishBtn,  CMD_FTUE_SIGN_UP_SUBMIT);
        
        return screen;
    }
    
    protected TnScreen createNativeSignInScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        ftueSignInView = AndroidCitizenUiHelper.addContentView(screen, R.layout.ftue_sign_in);
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        ftueSignInView.setBackgroundDrawable(bgImage);
        
        TextView loginTitleTextView=(TextView)ftueSignInView.findViewById(R.id.commonTitle0TextView);
        String loginTitle = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_LOGIN_TITLE, IStringLogin.FAMILY_LOGIN);    
        loginTitleTextView.setText(loginTitle);   
        
        ImageView splitlineImageView=(ImageView)ftueSignInView.findViewById(R.id.screensplitline);
        
        int key = (Integer) NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        
        AssetsImageDrawable imageDrawable = new AssetsImageDrawable(NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());        
        splitlineImageView.setBackgroundDrawable(imageDrawable);
        
        final Button loginBtn=(Button)ftueSignInView.findViewById(R.id.submit);
        String loginStr =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_BTN_FINISH, IStringLogin.FAMILY_LOGIN);    
        loginBtn.setText(loginStr);
        loginBtn.setTextColor(BTN_DISABLE_COLOR);     
        
        final AutoCompleteTextView emailAddressEditText=(AutoCompleteTextView)ftueSignInView.findViewById(R.id.emailaddress);
        final EditText passwdEditText=(EditText)ftueSignInView.findViewById(R.id.password);
        
        String emailAddressStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_EMAIL_ADDRESS, IStringLogin.FAMILY_LOGIN);     
        emailAddressEditText.setHint(emailAddressStr);
        int height = AndroidCitizenUiHelper.getPixelsByDip(37);
        emailAddressEditText.addTextChangedListener(new LoginTextWatcher(
                emailAddressEditText, passwdEditText, loginBtn, height, TYPE_EMAIL));

        String emailPreStr = model.getString(KEY_S_EMAILADDRESS);
        if(emailPreStr != null && emailPreStr.trim().length() > 0)
        {
            emailAddressEditText.setText(emailPreStr);
        }
        
        EmailFilterArrayAdapter adapter = new EmailFilterArrayAdapter((Context) AndroidPersistentContext.getInstance().getContext(), R.layout.scout_dropdown_item_1line);
        emailAddressEditText.setAdapter(adapter);
        
        String passwdStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_PASSWORD, IStringLogin.FAMILY_LOGIN);     
        passwdEditText.setHint(passwdStr);
        height = AndroidCitizenUiHelper.getPixelsByDip(98);
        passwdEditText.addTextChangedListener(new LoginTextWatcher(passwdEditText,emailAddressEditText, loginBtn, height,TYPE_PASSWORD));
        setLastTextFieldEnterFunction(passwdEditText, CMD_FTUE_SIGN_IN_SUBMIT);
        
        TextView forgetpasswdTextview=(TextView)ftueSignInView.findViewById(R.id.forgetpassword);
        String forgetpasswd = ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_LOGIN_FORGET_PASSWORD, IStringLogin.FAMILY_LOGIN);    
        forgetpasswdTextview.setText(Html.fromHtml("<u>"+forgetpasswd+"</u>"));
        forgetpasswdTextview.setTextColor(LINK_TOUCH_UP_COLOR);
        
        AndroidCitizenUiHelper.setOnClickCommand(this, forgetpasswdTextview,  CMD_FTUE_LOGIN_FORGET_PASSWD);
        AndroidCitizenUiHelper.setOnClickCommand(this, loginBtn,  CMD_FTUE_SIGN_IN_SUBMIT);
        
        return screen;
    }
    
    private LoginModel getLoginModel()
    {
       return (LoginModel)this.model;
    }
    
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int command = CMD_NONE;
        return command;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        if(state == STATE_FTUE_WELCOME)
        {
            if(welcomeView != null)
            {
                Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
                welcomeView.setBackgroundDrawable(bgImage);
            }
            return true;
        }
        else if(state == STATE_FTUE_SHOW_SIGN_IN)
        {
            if(ftueSignInView != null)
            {
                Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
                ftueSignInView.setBackgroundDrawable(bgImage);
            }
            return true;
        }
        else
        {
            TeleNavDelegate.getInstance().setBackgroundDrawableResource(R.drawable.background_shelf);
        }
        return false;
    }
    
    private void setLastTextFieldEnterFunction(EditText editText, final int command)
    {
        editText.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        {
                            TeleNavDelegate.getInstance().closeVirtualKeyBoard(getKeyBoardAttachedComponent());                                                   
                            AndroidCitizenUiHelper.triggerCommand(LoginViewTouch.this, v, command);
                            return true;
                        }
                        default:
                        {
                            break;
                        }
                    }
                }
                return false;
            }
        });
       
    }

    protected static class ImageAdapter extends BaseAdapter
    {
        private Context mContext = null;
        
        private TnUiArgAdapter[] adapters =
        {
            ImageDecorator.IMG_FTUE_LANDING_LOGO, 
            ImageDecorator.IMG_FTUE_01_DRIVE, 
            ImageDecorator.IMG_FTUE_02_FAVS, 
            ImageDecorator.IMG_FTUE_03_PLACES, 
            ImageDecorator.IMG_FTUE_04_TRAFFIC, 
        };

        public ImageAdapter(Context context)
        {
            this.mContext = context;
        }

        public int getCount()
        {
            return adapters.length;
        }

        public Object getItem(int position)
        {
            //no data bound here, just return the position.
            return position;
        }

        public long getItemId(int position)
        {
            //no data bound here, just return the position.
            return position;
        }

        //I believe we should leverage the convertView here.
        public View getView(int position, View convertView, ViewGroup parent)
        {
            RatioSolidImageView imageView = null;
            if(convertView instanceof ImageView)
            {
                imageView = (RatioSolidImageView)convertView;
            }
            else
            {
                imageView = new RatioSolidImageView (mContext);
                Gallery.LayoutParams params = new Gallery.LayoutParams(
                    Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(params);
            }
            
            AssetsImageDrawable imageDrawable = new AssetsImageDrawable(adapters[position]);
            imageView.setBackgroundDrawable(imageDrawable);
            return imageView;
        }

        public void destroy()
        {
            int length = this.getCount();
            for (int start = 0; start < length; start++)
            {
                TnUiArgAdapter adapter = adapters[start];
                String key = (String) adapter.getKey();
                AbstractTnImage image = (AbstractTnImage) ImageCacheManager.getInstance().getImageCache().remove(key);
                if (image != null)
                    image.release();
            }
        }
    };
    
    protected void activate()
    {
        TeleNavDelegate.getInstance().callAppNativeFeature(
            TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, new Object[]{PrimitiveTypeCache.valueOf(false) });
        TeleNavDelegate.getInstance().setBackgroundDrawableResource(R.drawable.background_shelf);
    }
    
    protected void deactivateDelegate()
    {
        super.deactivateDelegate();
        if (this.model.getState() != STATE_GOTO_TOS)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
    }
    
    protected void releaseImageRes()
    {
        if (mGallery != null && mGallery.getAdapter() != null)
        {
            ((ImageAdapter) mGallery.getAdapter()).destroy();
        }
        if (imageIndexer != null)
        {
            imageIndexer.destroy();
        }
        removeImageCache();
        
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), " CR recycle image!!!");
        }
        
    }

    private void removeImageCache()
    {
        String key = (String) ImageDecorator.IMG_FTUE_DOT_ICON_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getImageCache().remove(key); 
        key = (String) ImageDecorator.IMG_FTUE_DOT_ICON_FOCUSED.getKey();
        ImageCacheManager.getInstance().getImageCache().remove(key);
    }

    private class LoginTextWatcher implements TextWatcher
    {
        private EditText mEditText;
        private EditText relatedEditText;
        private Button mButton;
        private int textFieldType = 0;
        
        public LoginTextWatcher(EditText mEditText, EditText relatedEditText, Button mButton,
                int height, int textFieldType)
        {
            super();
            this.mEditText = mEditText;
            this.mButton = mButton;
            this.textFieldType = textFieldType;
            this.relatedEditText = relatedEditText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
          
            
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            boolean isLegalEmail = false;
            boolean isLegalPassword = false;
            boolean isLegalPTN = false;
            if(textFieldType == TYPE_EMAIL || textFieldType == TYPE_PASSWORD)
            {
               if (textFieldType == TYPE_EMAIL)
               {
                      isLegalEmail = getLoginModel().checkEmail(mEditText.getText().toString());
                      isLegalPassword = getLoginModel().checkPassword(relatedEditText.getText().toString());
               }
               else
                {
                      isLegalPassword = getLoginModel().checkPassword(mEditText.getText().toString());
                      isLegalEmail = getLoginModel().checkEmail(relatedEditText.getText().toString());
                }
                if (isLegalEmail && isLegalPassword)
                {
                    mButton.setEnabled(true);
                    mButton.setTextColor(BTN_ENABLED_COLOR);
                }
                else
                {
                    mButton.setEnabled(false);
                    mButton.setTextColor(BTN_DISABLE_COLOR);
                }
            }
            else if(textFieldType == TYPE_PTN)
            {
                isLegalPTN = getLoginModel().checkLength(mEditText.getText().toString());
                if(isLegalPTN)
                {
                    mButton.setEnabled(true);
                    mButton.setTextColor(BTN_ENABLED_COLOR);
                }
                else
                {
                    mButton.setEnabled(false);
                    mButton.setTextColor(BTN_DISABLE_COLOR);
                }
            }
            else if(textFieldType == TYPE_VERIFY_CODE)
            {
                if(mEditText.getText().toString().length() > 0)
                {
                    mButton.setEnabled(true);
                    mButton.setTextColor(BTN_ENABLED_COLOR);
                }
                else
                {
                    mButton.setEnabled(false);
                    mButton.setTextColor(BTN_DISABLE_COLOR);
                }
            }
        }

        public void afterTextChanged(Editable s)
        {
            //nothing to do.
        }
    }
    
    public void onScreenUiEngineAttached(TnScreen screen, int attached)
    {
        if (screen != null)
        {
            if(attached == ITnScreenAttachedListener.BEFORE_ATTACHED)
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
            }
            
            if (attached == ITnScreenAttachedListener.DETTACHED)
            {
                if (this.model.getState() == STATE_BACK_TO_MAIN || this.model.getState() == STATE_BACK_TO_LAUNCH)
                {
                    releaseImageRes();
                }
                TeleNavDelegate.getInstance().closeVirtualKeyBoard(null);
            }
            
        }
    }
}
