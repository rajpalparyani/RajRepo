/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPreferenceConstants.java
 *
 */
package com.telenav.module.preference;

import java.util.Hashtable;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.TnBacklightManagerImpl;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.PreferenceDao;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.mandatory.MandatoryProfile.CredentialInfo;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.datatypes.preference.PreferenceGroup;
import com.telenav.datatypes.route.Route;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.dashboard.AccountChangeListener;
import com.telenav.module.dashboard.IAccountTypeChangeListener;
import com.telenav.module.entry.SecretKeyListener;
import com.telenav.module.mapdownload.IMapDownloadStatusListener;
import com.telenav.module.mapdownload.IOnBoardDataAvailabilityListener;
import com.telenav.module.mapdownload.MapDownloadOnBoardDataStatusManager;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringLogin;
import com.telenav.res.IStringPreference;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenCarListItem;
import com.telenav.ui.citizen.CitizenProfileListItem;
import com.telenav.ui.citizen.CitizenProfileListItemWithBadge;
import com.telenav.ui.citizen.CitizenProfileSwitcher;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogDropDownField;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;
import com.telenav.util.PrimitiveTypeCache;
/**
 *@author jyxu (jyxu@telenav.cn)
 *@date 2012-11-18
 */
class PreferenceViewTouch extends AbstractCommonView implements
        IPreferenceConstants, IAccountTypeChangeListener, IOnBoardDataAvailabilityListener, IMapDownloadStatusListener
{
    protected static String APPEND_CHAR = "+";
    protected static int VIRGIN = -1;
   
    public PreferenceViewTouch(AbstractCommonUiDecorator decorator)
    {
        super(decorator);
    }
    
    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_SAVING_UPLOAD_PREFENCE:
            {
                popup = createSaveUpLoadProgress(state);
                break;
            }
            case STATE_PREFERENCE_VALID_FAIL:
            {
                popup = createValidFailPopup(state);
                break;
            }
            case STATE_CANCEL_SUBSCRIPTION_CONFIRM:
            {
                popup =  createAskingCancelSubscription(state);
                break;
            }
            case STATE_SHOW_CANCEL_SUBSCRIPTION_SUCCESS:
            {
                popup =  createCancelSubscriptionSuccess(state);
                break;
            }
            default:
                break;
        }
        return popup;
    }

    protected TnPopupContainer createSaveUpLoadProgress(int state)
    {
        FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(state, 
            ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_STR_SAVING, IStringPreference.FAMILY_PREFERENCE));
        return progressBox;
    }

    protected TnPopupContainer createAskingSavePopup(int state)
    {
        TnMenu menus = UiFactory.getInstance().createMenu();
        menus.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_STR_SAVE, IStringPreference.FAMILY_PREFERENCE), CMD_SAVE_UPLOAD_PREFERENCE);
        menus.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_STR_CANCEL, IStringPreference.FAMILY_PREFERENCE), CMD_BACK_TO_ROOT);

        return UiFactory.getInstance().createMessageBox(state,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_STR_ASKING_INFO, IStringPreference.FAMILY_PREFERENCE), menus);
    }
    
    protected TnPopupContainer createAskingCancelSubscription(int state)
    {
        TnMenu menus = UiFactory.getInstance().createMenu();
        menus.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_NO, IStringCommon.FAMILY_COMMON), CMD_COMMON_BACK);
        menus.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_YES, IStringCommon.FAMILY_COMMON), CMD_CANCEL_SUBSCRIPTION);

        return UiFactory.getInstance().createMessageBox(state,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.CANCEL_SUBSCRIPTION_CONFIRM, IStringPreference.FAMILY_PREFERENCE), menus);
    }

    protected TnPopupContainer createCancelSubscriptionSuccess(int state)
    {
        String message = ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.CANCEL_SUBSCRIPTION_SUCCESS, IStringPreference.FAMILY_PREFERENCE);
        TnMenu menus = UiFactory.getInstance().createMenu();
        menus.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        return UiFactory.getInstance().createMessageBox(state,
            message, menus, false);
    }
    
    protected TnPopupContainer createValidFailPopup(int state)
    {
        TnMenu menus = UiFactory.getInstance().createMenu();
        menus.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        model.put(KEY_B_NEED_REVERT_USER_INFO, true);
        return UiFactory.getInstance().createMessageBox(state, this.model.getString(KEY_S_VALID_FAIL), menus);
    }
    

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch(state)
        {
            case STATE_PREFERENCE_ROOT:
            {
                screen = createMyProfileRootScreen();
                break;
            }
            case STATE_PREFERENCE_LAYER1:
            {
                int  groupId =  this.model.getInt(KEY_I_SELECTED_PREFERENCE_GROUP_ID);
                screen = createRootLayerScreen(groupId);// 3 map nav style setting 4 audio setting
                break;
            }
            case STATE_GENERAL_FEEDBACK:
            {
                screen = createGeneralFeedbackScreen(state,  UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR));
                break;
            }
            case STATE_MY_CAR:
            {
                screen = createMyCar();
                break;
            }
            case STATE_SHOW_SUBSCRIPTION:
            {
                screen = createSubscription();
                break;
            }
        }
        
        if (screen != null)
        {
            removeMenuById(screen, ICommonConstants.CMD_COMMON_PREFERENCE);
        }
        return screen;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch(state)
        {
            case STATE_PREFERENCE_LAYER1:
            {
                if( model.fetchBool(KEY_B_NEED_REVERT_USER_INFO) )
                {
                    int groupId = model.getInt(KEY_I_SELECTED_PREFERENCE_GROUP_ID);
                    int[] ids = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreferenceIdsByGroup(groupId, this.getRegion());
                    Hashtable userInfo = (Hashtable)model.get(KEY_O_USER_INFO);
                    for(int i = 0; i < ids.length; i++)
                    {
                        if( ids[i] == Preference.ID_PREFERENCE_PHONE_NUMBER )
                        {
                            continue;
                        }
                        
                        Preference pref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(ids[i]);
                        if(pref == null)
                            continue;
                        
                        CitizenTextField textField = (CitizenTextField)screen.getComponentById(ids[i] + ID_PREFERENCE_ITEM_BASE);
                        textField.setText(pref.getStrValue());
                        
                        if (ids[i] == Preference.ID_PREFERENCE_TYPE_FIRSTNAME
                                || ids[i] == Preference.ID_PREFERENCE_TYPE_LASTNAME
                                || ids[i] == Preference.ID_PREFERENCE_TYPE_USERNAME
                                || ids[i] == Preference.ID_PREFERENCE_TYPE_EMAIL)
                        {
                            if (userInfo != null)
                            {
                                Object str = userInfo.get(PrimitiveTypeCache.valueOf(ids[i]));
                                if (str instanceof String)
                                {
                                    textField.setText((String) str);
                                }
                            }
                        }
                        
                        textField.requestPaint();
                    }
                    if (userInfo != null)
                    {
                    	userInfo.clear();
                    }                    
                    break;
                }
                AbstractTnComponent component = screen.getComponentById(Preference.ID_PREFERENCE_ROUTETYPE + ID_PREFERENCE_ITEM_BASE);
                if(component != null)
                {
                    PreferenceComboBox item = (PreferenceComboBox)component;
                    item.setOptions(new String[]{getRouteSettingString(false)});
                    item.setSelectedIndex(0);
                    item.requestPaint();
                }
                break;
            }
            case STATE_PREFERENCE_ROOT:
            {    
                // update my account
                ((Hashtable) model.get(KEY_O_USER_INFO)).clear();
                
                boolean isAccountTypeChanged = isAccountTypeChanged();
                boolean isBackToMyProfile = this.model.fetchBool(KEY_B_IS_START_BY_OTHER_CONTROLLER);
                boolean isFromSubscription = this.model.fetchBool(KEY_B_IS_FROM_SUBSCRIPTION);
                AbstractTnContainer bodyContainer = (AbstractTnContainer) screen.getComponentById(ID_BODY_CONTAINER);
                AbstractTnContainer myAccountContainer = (AbstractTnContainer) screen
                        .getComponentById(ID_MYACCOUNT_CONTAINER);
                myAccountContainer.removeAll();
                bodyContainer.remove(myAccountContainer);
                addMyAccountItems(bodyContainer, myAccountContainer);
                if ((isBackToMyProfile && !isFromSubscription) || isAccountTypeChanged)
                {
                    int event = model.getInt(KEY_I_FTUE_LAUNCH_EVENT);
                    if (isCredentialInfoExisted() && event == ICommonConstants.EVENT_CONTROLLER_GOTO_FTUE_SIGNUP)
                    {
                        showNotificationContainer(STATE_PREFERENCE_ROOT);
                    }
                }
                
                //update address
                CitizenCarListItem carItem = (CitizenCarListItem)screen.getComponentById(ID_COMPONENT_MY_CAR);
                int carIndex = ((PreferenceModel)model).getStoredCarIndex();
                if(carIndex < 0) carIndex = 0;
                carItem.setCarName(carNames[carIndex]);
                carItem.setCarImage(carImageAdapters[carIndex].getImage());
                carItem.requestPaint();
                
                //update home
                AbstractTnContainer addressesContainer =  (AbstractTnContainer)screen.getComponentById(ID_COMPONENT_HOMEWORK_CONTAINER);
                addressesContainer.removeAll();
                String title =  ResourceManager
                        .getInstance()
                        .getCurrentBundle()
                        .getString(IStringPreference.PREFERENCE_STR_ADDRESSES,
                        IStringPreference.FAMILY_PREFERENCE);
                addProfileItemTitle(addressesContainer, title);
                addTitleSplitLine(addressesContainer); 
                if (DaoManager.getInstance().getAddressDao().getHomeAddress() != null)
                {
                    String homeStr =  ResourceManager
                            .getInstance()
                            .getCurrentBundle()
                            .getString(IStringPreference.PREFERENCE_STR_EDIT_HOME,
                            IStringPreference.FAMILY_PREFERENCE);
                    Stop stop = DaoManager.getInstance().getAddressDao().getHomeAddress();
                    addDoubleLinesItem(addressesContainer, homeStr, ResourceManager.getInstance().getStringConverter().convertAddress(stop, false), CMD_GO_TO_SET_HOME, homeStr);
                }
                else
                {
                    String homeStr =  ResourceManager
                            .getInstance()
                            .getCurrentBundle()
                            .getString(IStringPreference.PREFERENCE_STR_SET_UP_HOME,
                            IStringPreference.FAMILY_PREFERENCE);
                    addSingleItem(addressesContainer, homeStr, CMD_GO_TO_SET_HOME, homeStr);
                }
                addItemSplitLine(addressesContainer);
                
                if (DaoManager.getInstance().getAddressDao().getOfficeAddress() != null)
                {
                    String workStr =  ResourceManager
                            .getInstance()
                            .getCurrentBundle()
                            .getString(IStringPreference.PREFERENCE_STR_EDIT_WORK,
                            IStringPreference.FAMILY_PREFERENCE);
                    Stop stop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
                    addDoubleLinesItem(addressesContainer, workStr, ResourceManager.getInstance().getStringConverter().convertAddress(stop, false), CMD_GO_TO_SET_WORK, workStr);
                }
                else
                {
                    String workStr =  ResourceManager
                            .getInstance()
                            .getCurrentBundle()
                            .getString(IStringPreference.PREFERENCE_STR_SET_UP_WORK,
                            IStringPreference.FAMILY_PREFERENCE);
                    addSingleItem(addressesContainer, workStr, CMD_GO_TO_SET_WORK, workStr);
                }
                
                updateShareRateItem(bodyContainer);
                
                updateMapDownloadBadge(bodyContainer);
                
                updateMapDownloadItem(bodyContainer);
                break;
            }
            case STATE_MY_CAR:
            {
                FrogList list = (FrogList)screen.getComponentById(ID_COMPONENT_CAR_LIST);
                list.requestPaint();
                break;
            }
            
        }
        return false;
    }
    
    
    protected boolean isAccountTypeChanged()
    {
        String oldAccountType = model.getString(KEY_S_ACCOUNT_TYPE);
        String currentAccountType = DaoManager.getInstance().getBillingAccountDao().getAccountType();

        if (oldAccountType == null)
        {
            oldAccountType = "";
        }

        if (currentAccountType == null)
        {
            currentAccountType = "";
        }

        if (currentAccountType.equalsIgnoreCase(oldAccountType))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    protected TnScreen createMyProfileRootScreen()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_PREFERENCE_ROOT);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        
        AbstractTnContainer titleContainer = screen.getTitleContainer();
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        
        String preferenceStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_TITLE, IStringPreference.FAMILY_PREFERENCE);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, preferenceStr);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) this.uiDecorator).SCREEN_WIDTH);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);
        titleContainer.setTouchEventListener(new SecretKeyListener(this,ICommonConstants.CMD_SECRET_KEY));
        
        AbstractTnContainer myProfileContainer = UiFactory.getInstance().createLinearContainer(0, true,
            AbstractTnGraphics.HCENTER);
        myProfileContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).SCREEN_WIDTH);
        myProfileContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).SCREEN_HEIGHT);
        TnScrollPanel scrollPanel = UiFactory.getInstance().createScrollPanel(0, true);
        myProfileContainer.add(scrollPanel);
        screen.getContentContainer().add(myProfileContainer);

        AbstractTnContainer bodyContainer = UiFactory.getInstance().createLinearContainer(ID_BODY_CONTAINER, true);
        bodyContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).SCREEN_WIDTH);
        bodyContainer.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_CONTAINER_PADDING.getInt(), 0,
            ((PreferenceUiDecorator) uiDecorator).PROFILE_CONTAINER_PADDING.getInt(), 0);
        scrollPanel.set(bodyContainer);

        addMyAccountItems(bodyContainer, createMyAccountContainer());
        addAddressItems(bodyContainer);
        addSettingItems(bodyContainer);
        addMapDownloadItem(bodyContainer);
        addMyCarItem(bodyContainer);
        addScountInfos(bodyContainer);
//        addFeedbackItem(bodyContainer);
        return screen;
    }

    private TnScreen createMyCar()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_MY_CAR);
        AbstractTnContainer titleContainer = screen.getTitleContainer();
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        String myCarTitleStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_TITLE_MYCAR, IStringPreference.FAMILY_PREFERENCE);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, myCarTitleStr);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);

        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        screen.getContentContainer().setPadding(((PreferenceUiDecorator) this.uiDecorator).PROFILE_CONTAINER_PADDING.getInt(),
            0, 0, 0);
        FrogList list = new FrogList(ID_COMPONENT_CAR_LIST);
        list.setCommandEventListener(this);

        if (model.getInt(this.KEY_I_CAR_MODEL_VALUE) < 0)
        {
            model.put(this.KEY_I_CAR_MODEL_VALUE, 0);
        }

        FrogAdapter carListAdapter = new CarListAdapter(carNames, carImageAdapters, (PreferenceUiDecorator) uiDecorator,
                model.getInt(KEY_I_CAR_MODEL_VALUE));
        list.setAdapter(carListAdapter);
        screen.getContentContainer().add(list);
        return screen;
    }
    
    private TnScreen createSubscription()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_MY_CAR);
        AbstractTnContainer titleContainer = screen.getTitleContainer();
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        String mySubscriptionTitle = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_SUBSCRIPTION_TITLE, IStringPreference.FAMILY_PREFERENCE);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, mySubscriptionTitle);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);

        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        int horizontalPadding=((PreferenceUiDecorator) this.uiDecorator).PROFILE_CONTAINER_PADDING.getInt();
        screen.getContentContainer().setPadding(horizontalPadding,0, horizontalPadding, 0);

        AbstractTnContainer contentContainer = screen.getContentContainer();

        addNullField(contentContainer, 40);

        if (DaoManager.getInstance().getBillingAccountDao().isPremiumAccount())
        {
            String message = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_SCOUNT_PLUS, IStringPreference.FAMILY_PREFERENCE);
            FrogLabel subscriptionType = createSubscriptionLabel(message,
                UiStyleManager.getInstance().getColor(UiStyleManager.MY_PROFILE_SUBSCRIPTIONINFO_COLOR), UiStyleManager
                        .getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
            contentContainer.add(subscriptionType);
        }

        String offerCurrency = DaoManager.getInstance().getBillingAccountDao().getOfferCurrency();
        if (offerCurrency != null && offerCurrency.indexOf("USD") > -1)
        {
            addNullField(contentContainer, 20);
            offerCurrency = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_SUBSCRIPTION_DOLLAR_SYMBOL, IStringPreference.FAMILY_PREFERENCE);
            String fee = DaoManager.getInstance().getBillingAccountDao().getOfferPrize();
            String subscriptionfeeStr = "";
            if (Float.parseFloat(fee) == 0)
            {
                subscriptionfeeStr = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPreference.FREE_USER, IStringPreference.FAMILY_PREFERENCE);
            }
            else
            {
                subscriptionfeeStr = offerCurrency + fee;
            }
            FrogLabel subscriptionfee = createSubscriptionLabel(subscriptionfeeStr,
                UiStyleManager.getInstance().getColor(UiStyleManager.MY_PROFILE_SUBSCRIPTIONINFO_COLOR), UiStyleManager
                .getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_VALUE));
            contentContainer.add(subscriptionfee);
        }

        String expiredatePrefix = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_SUBSCRIPTION_EXPIRE_DATE_PREFIX, IStringPreference.FAMILY_PREFERENCE);
        String expiredate = DaoManager.getInstance().getBillingAccountDao().getOfferExpireDate();

        boolean needButton = DaoManager.getInstance().getBillingAccountDao().getSubscriptionCancellable();
        
        //TNANDROID-4043 we don't show Expires on dd-mm-9999 for Recurring subscription users
        if ((expiredate != null && expiredate.length() > 0) && !needButton)
        {
            expiredate = expiredatePrefix + expiredate;
            FrogLabel subscriptionExpiredate = createSubscriptionLabel(expiredate,
                UiStyleManager.getInstance().getColor(UiStyleManager.MY_PROFILE_SUBSCRIPTIONINFO_COLOR), UiStyleManager
                        .getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_VALUE));
            contentContainer.add(subscriptionExpiredate);
        }

        if(needButton)
        {   
            addNullField(contentContainer, 40);
            TnLinearContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, true, AbstractTnGraphics.HCENTER);
            FrogButton cancelSubscriptionButton = UiFactory.getInstance().createButton(ID_CANCEL_SUBSCRIPTION_BUTTON,
                ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_STR_CANCEL_SUBSCRIPTION, IStringPreference.FAMILY_PREFERENCE));
           
            cancelSubscriptionButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.BIG_RADIAN_BUTTON_UNFOCUS);
            cancelSubscriptionButton.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.BIG_RADIAN_BUTTON_FOCUS);
            cancelSubscriptionButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
                ((PreferenceUiDecorator) uiDecorator).BUTTON_WIDTH);
            cancelSubscriptionButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                ((PreferenceUiDecorator) uiDecorator).BUTTON_HEIGHT);
            cancelSubscriptionButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BUTTON));
            cancelSubscriptionButton.setForegroundColor(
                UiStyleManager.getInstance().getColor(
                    UiStyleManager.BUTTON_DEFAULT_FOCUSED_COLOR), UiStyleManager
                        .getInstance()
                        .getColor(UiStyleManager.BUTTON_DEFAULT_UNFOCUSED_COLOR));
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", CMD_CANCEL_SUBSCRIPTION_CONFIRM);
            cancelSubscriptionButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            cancelSubscriptionButton.setCommandEventListener(this);
            buttonContainer.add(cancelSubscriptionButton);
            contentContainer.add(buttonContainer);
        }
        return screen;
    }

    private FrogLabel createSubscriptionLabel(String text, int color, AbstractTnFont  font)
    {
        FrogLabel subscriptionLabel = UiFactory.getInstance().createLabel(0, text);
        subscriptionLabel.setForegroundColor(color, color);
        subscriptionLabel.setFont(font);
        return subscriptionLabel;
    }

    private void addAddressItems(AbstractTnContainer container)
    {
        AbstractTnContainer addressesContainer = UiFactory.getInstance().createLinearContainer(ID_COMPONENT_HOMEWORK_CONTAINER, true);
        addressesContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        
       
        String title =  ResourceManager
                .getInstance()
                .getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_ADDRESSES,
                IStringPreference.FAMILY_PREFERENCE);
        addProfileItemTitle(addressesContainer, title);
        addTitleSplitLine(addressesContainer); 
        
        if (DaoManager.getInstance().getAddressDao().getHomeAddress() != null)
        {
            String homeStr =  ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_EDIT_HOME,
                    IStringPreference.FAMILY_PREFERENCE);
            Stop stop = DaoManager.getInstance().getAddressDao().getHomeAddress();
            addDoubleLinesItem(addressesContainer, homeStr, ResourceManager.getInstance().getStringConverter().convertAddress(stop, false), CMD_GO_TO_SET_HOME, homeStr);
        }
        else
        {
            String homeStr =  ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_SET_UP_HOME,
                    IStringPreference.FAMILY_PREFERENCE);
            addSingleItem(addressesContainer, homeStr, CMD_GO_TO_SET_HOME, homeStr);
        }
        addItemSplitLine(addressesContainer);
        
        if (DaoManager.getInstance().getAddressDao().getOfficeAddress() != null)
        {
            String workStr =  ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_EDIT_WORK,
                    IStringPreference.FAMILY_PREFERENCE);
            Stop stop = DaoManager.getInstance().getAddressDao().getOfficeAddress();
            addDoubleLinesItem(addressesContainer, workStr, ResourceManager.getInstance().getStringConverter().convertAddress(stop, false), CMD_GO_TO_SET_WORK,workStr);
        }
        else
        {
            String workStr =  ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_SET_UP_WORK,
                    IStringPreference.FAMILY_PREFERENCE);
            addSingleItem(addressesContainer, workStr, CMD_GO_TO_SET_WORK, workStr);
        }
        container.add(addressesContainer);
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
                    if(command == CMD_CREATE_ACCOUNT)
                    {
                        this.model.put(KEY_I_FTUE_LAUNCH_EVENT, ICommonConstants.EVENT_CONTROLLER_GOTO_FTUE_SIGNUP);
                    }
                    else if(command == CMD_LOGIN)
                    {
                        this.model.put(KEY_I_FTUE_LAUNCH_EVENT, ICommonConstants.EVENT_CONTROLLER_GOTO_FTUE_SIGNIN);
                    }
                    else if(command==CMD_EDIT_ACCOUNT)
                    {
                        this.model.put(KEY_I_FTUE_LAUNCH_EVENT, ICommonConstants.EVENT_CONTROLLER_GOTO_FTUE_EDIT_ACCOUNT);
                    }
                    else if(command == CMD_AUDIO)
                    {
                        this.model.put(KEY_I_SELECTED_PREFERENCE_GROUP_ID, PreferenceGroup.ID_PREFERENCE_GROUP_SPEECH_RCOG);
                    }
                    else if(command == CMD_MAP_NAV_SETTING)
                    {
                        this.model.put(KEY_I_SELECTED_PREFERENCE_GROUP_ID, PreferenceGroup.ID_PREFERENCE_GROUP_NAV_AUDIO);
                    }
                    else if(command == CMD_DRIVING_SHARE_SETTING)
                    {
                        this.model.put(KEY_I_SELECTED_PREFERENCE_GROUP_ID, PreferenceGroup.ID_PREFERENCE_GROUP_SHARING);
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
            switch (command)
            {
                case CMD_GO_TO_SET_HOME:
                {
                    if (DaoManager.getInstance().getAddressDao().getHomeAddress() == null)
                    {
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MYPROFILE,
                            KontagentLogger.MYPROFILE_SET_UP_HOME_CLICKED);
                    }
                    break;
                }
                case CMD_GO_TO_SET_WORK:
                {
                    if (DaoManager.getInstance().getAddressDao().getOfficeAddress() == null)
                    {
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MYPROFILE,
                            KontagentLogger.MYPROFILE_SET_UP_WORK_CLICKED);
                    }
                    break;
                }
                case CMD_MAP_DOWNLOAD:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MYPROFILE,
                        KontagentLogger.MYPROFILE_MAPDOWNLOAD_CLICKED);
                    break;
                }
                case CMD_GOTO_UPSELL:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MYPROFILE,
                        KontagentLogger.MYPROFILE_UPGRADE_CLICKED);
                    break;
                }
                case CMD_MY_CAR:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MYPROFILE,
                        KontagentLogger.MYPROFILE_MYCAR_CLICKED);
                    break;
                }
            }
        }
//        else if(tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
//        {
//            if (tnUiEvent.getKeyEvent()!= null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
//                    && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK && this.model.getState() == STATE_PREFERENCE_ROOT)
//            {
//                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MYPROFILE, KontagentLogger.MYPROFILE_BACK_CLICKED);
//            }
//        } 
    }
    
    private void addMyAccountItems(AbstractTnContainer bodyContainer, AbstractTnContainer myAccountContainer)
    {
        String emailAccount = null;
        boolean hasSignInScoutDotMe = false;
        
        CredentialInfo credentialInfo = DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getCredentialInfo();
        if(credentialInfo != null && credentialInfo.credentialValue != null && credentialInfo.credentialValue.trim().length() > 0)
        {
            hasSignInScoutDotMe = true;
            emailAccount = credentialInfo.credentialValue;
        }
        
        String myAccountTitle = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_MY_ACCOUNT, IStringPreference.FAMILY_PREFERENCE);
        addProfileItemTitle(myAccountContainer, myAccountTitle);
        addTitleSplitLine(myAccountContainer);
        
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        if (hasSignInScoutDotMe)
        {
            String name = preferenceDao.getStrValue(Preference.ID_PREFERENCE_TYPE_FIRSTNAME) + " "
                    + preferenceDao.getStrValue(Preference.ID_PREFERENCE_TYPE_LASTNAME);
            if (name != null && name.trim().length() > 0)
            {
                addDoubleLinesItem(myAccountContainer, name, emailAccount, CMD_EDIT_ACCOUNT, myAccountTitle);
            }
            else
            {
                addSingleItem(myAccountContainer, emailAccount, CMD_EDIT_ACCOUNT, myAccountTitle);
            }
        }
        else
        {
            AbstractTnContainer buttonContainer = UiFactory.getInstance().createLinearContainer(0, true,
                AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            addNullField(buttonContainer, 40);
            String createAccountStr = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringLogin.RES_BTN_CREATE_ACCOUNT, IStringLogin.FAMILY_LOGIN);
            FrogButton createBtn = createBtn(0, createAccountStr, CMD_CREATE_ACCOUNT);
            createBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FTUE_BUTTON_FOCUSED);
            createBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FTUE_BUTTON_UNFOCUSED);
            createBtn.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TOC_BOTTOM_ACCEPT_BUTTON_TEXT_UNFOCUSED_COLOR), UiStyleManager
                    .getInstance().getColor(UiStyleManager.TOC_BOTTOM_ACCEPT_BUTTON_TEXT_UNFOCUSED_COLOR));
            createBtn.setContentDescription(createAccountStr);
            buttonContainer.add(createBtn);
            addNullField(buttonContainer, 30);

            String loginTitle = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_LOGIN, IStringPreference.FAMILY_PREFERENCE);
            FrogButton loginBtn = createBtn(0, loginTitle, CMD_LOGIN);
            loginBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROFILE_LOGIN_ITEM_FOCUSED);
            loginBtn.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS,
                NinePatchImageDecorator.PROFILE_LOGIN_ITEM_UNFOCUSED);
            loginBtn.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                    .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
            loginBtn.setContentDescription(loginTitle);
            buttonContainer.add(loginBtn);
            addNullField(buttonContainer, 40);
            myAccountContainer.add(buttonContainer);
        }

        addItemSplitLine(myAccountContainer);
        String idTitle = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_ID, IStringPreference.FAMILY_PREFERENCE);
        String csrId = preferenceDao.getStrValue(Preference.ID_PREFERENCE_CSRID);
        addDoubleLinesItem(myAccountContainer, idTitle, csrId, CMD_NONE, true, csrId);
        addItemSplitLine(myAccountContainer);
        
        String accountType = DaoManager.getInstance().getBillingAccountDao().getAccountType();
        model.put(KEY_S_ACCOUNT_TYPE, accountType);
        
        boolean isNeedPurchase = DaoManager.getInstance().getBillingAccountDao().isNeedPurchase();
        if (isNeedPurchase)
        {
            String upgradeStr = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_UPGRADE_TO_SCOUNT_PLUS, IStringPreference.FAMILY_PREFERENCE);
            addSingleItem(myAccountContainer, upgradeStr, CMD_GOTO_UPSELL,upgradeStr);
        }
        else
        {
            String timeSpan = DaoManager.getInstance().getBillingAccountDao().getPurchaseOrderName();
            //hard code in 30 days,as in android market place trial offer, the value of purchase order name is 0, should handle 
            //the same way against IO.
            if(timeSpan==null || timeSpan.trim().length()==0)
            {
                timeSpan=ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPreference.PREFERENCE_SUBSCRIPTION_EXPIRE_IN_THIRTY_DAYS, IStringPreference.FAMILY_PREFERENCE);
            }
            String subscription = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_SUBSCRIPTION, IStringPreference.FAMILY_PREFERENCE);
            String subscriptionToScout = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_SUBSCRIPTION_FOR_SCOUT_PLUS, IStringPreference.FAMILY_PREFERENCE);
            addDoubleLinesItem(myAccountContainer, subscription, subscriptionToScout + " " + timeSpan, CMD_SHOW_SUBSCRIPTION, subscription);

        }
        bodyContainer.add(myAccountContainer, 0);
    }

    private AbstractTnContainer createMyAccountContainer()
    {
        AbstractTnContainer myAccountContainer = UiFactory.getInstance()
                .createLinearContainer(ID_MYACCOUNT_CONTAINER, true);
        myAccountContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        return myAccountContainer;
    }
    
    private void addSettingItems(AbstractTnContainer container)
    {
        AbstractTnContainer settingsContainer = UiFactory.getInstance().createLinearContainer(0, true);
        settingsContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        String title = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_SET_SETTINGS, IStringPreference.FAMILY_PREFERENCE);
        addProfileItemTitle(settingsContainer, title);
        addTitleSplitLine(settingsContainer);
        String audioStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_AUDIO, IStringPreference.FAMILY_PREFERENCE);

        addSingleItem(settingsContainer, audioStr, CMD_AUDIO, audioStr);

        addItemSplitLine(settingsContainer);
        String workStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_MAPS_NAVIGATION, IStringPreference.FAMILY_PREFERENCE);
        addSingleItem(settingsContainer, workStr, CMD_MAP_NAV_SETTING, workStr);
        
        addItemSplitLine(settingsContainer);
        String sharing = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_SHARING, IStringPreference.FAMILY_PREFERENCE);
        addSingleItem(settingsContainer, sharing, CMD_DRIVING_SHARE_SETTING, sharing);
        container.add(settingsContainer);
    }
    
    private void addMyCarItem(AbstractTnContainer container)
    {
        CitizenCarListItem item = new CitizenCarListItem(ID_COMPONENT_MY_CAR);
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).PROFILE_ITEM_CONTENT_HEIGHT);
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROFILE_LIST_ITEM);
        item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, null);

        int carIndex = ((PreferenceModel) model).getStoredCarIndex();
        if (carIndex < 0)
            carIndex = 0;
        model.put(this.KEY_I_CAR_MODEL_VALUE, carIndex);

        item.setCarName(carNames[carIndex]);
        item.setCarImage(carImageAdapters[carIndex].getImage());
        item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
        item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_BL));
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_MY_CAR);
        item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        item.setNeedRadioImage(false);
      
        AbstractTnContainer myCarContainer = UiFactory.getInstance().createLinearContainer(0, true);
        myCarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        String title = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_MYCAR, IStringPreference.FAMILY_PREFERENCE);
        addProfileItemTitle(myCarContainer, title);
        addTitleSplitLine(myCarContainer);
        item.setContentDescription(title);
        myCarContainer.add(item);
        container.add(myCarContainer);
    }
    
    private void addMapDownloadItem(AbstractTnContainer container)
    {
        final AbstractTnContainer mapDownloadContainer = UiFactory.getInstance().createLinearContainer(0, true);
        mapDownloadContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        String label = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_MAPS_ON_MY_DEVICE, IStringPreference.FAMILY_PREFERENCE);
        addProfileItemTitle(mapDownloadContainer, label);
        addTitleSplitLine(mapDownloadContainer);
        String badge = ResourceManager.getInstance().getCurrentBundle().getString(IStringPreference.PREFERENCE_MAP_DOWNLOAD_BADGE, IStringPreference.FAMILY_PREFERENCE);
        
        String download;
        String regionName = MapDownloadStatusManager.getInstance().getDownloadedRegionName();
        if (MapDownloadStatusManager.getInstance().isOnBoardDataAvailable())
        {
            download = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_MAPS_DOWNLOADED, IStringPreference.FAMILY_PREFERENCE);
        }
        else
        {
            download = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_MAPS_DOWNLOAD, IStringPreference.FAMILY_PREFERENCE);
        }
        addBadgeItem(mapDownloadContainer, download, regionName, badge, CMD_MAP_DOWNLOAD, true, true, download);
        container.add(mapDownloadContainer);
        
        updateMapDownloadBadge(mapDownloadContainer);
    }
    
    private void updateMapDownloadBadge(final AbstractTnContainer container)
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                CitizenProfileListItemWithBadge mapDownloadItem = (CitizenProfileListItemWithBadge) container.getComponentById(CMD_MAP_DOWNLOAD);
                if (mapDownloadItem != null)
                {
                    boolean isBadgeNeeded = MapDownloadStatusManager.getInstance().isBadgeNeeded();
                    if (isBadgeNeeded)
                    {
                        AbstractTnImage badgeImage = NinePatchImageDecorator.PROFILE_RED_BADGE_UNFOCUSED.getImage();
                        mapDownloadItem.setBadgeImage(badgeImage);
                    }
                    else
                    {
                        mapDownloadItem.setBadgeImage(null);
                    }
                }
            }
        });
    }
    
    private void updateMapDownloadItem(final AbstractTnContainer container)
    {
        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
        {
            public void run()
            {
                CitizenProfileListItemWithBadge mapDownloadItem = (CitizenProfileListItemWithBadge) container.getComponentById(CMD_MAP_DOWNLOAD);
                if (mapDownloadItem != null)
                {
                    String download;
                    String regionName = MapDownloadStatusManager.getInstance().getDownloadedRegionName();
                    if (MapDownloadStatusManager.getInstance().isOnBoardDataAvailable())
                    {
                        download = ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringPreference.PREFERENCE_STR_MAPS_DOWNLOADED, IStringPreference.FAMILY_PREFERENCE);
                    }
                    else
                    {
                        download = ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringPreference.PREFERENCE_STR_MAPS_DOWNLOAD, IStringPreference.FAMILY_PREFERENCE);
                    }
                    
                    boolean needRepaint = false;
                    
                    if (download == null)
                    {
                        if(mapDownloadItem.getTitle() != null && mapDownloadItem.getTitle().length() > 0)
                        {
                            needRepaint = true;
                        }
                    }
                    else
                    {
                        if (!download.equals(mapDownloadItem.getTitle()))
                        {
                            needRepaint = true;
                        }
                    }
                    
                    if(regionName == null)
                    {
                        if(mapDownloadItem.getValue() != null && mapDownloadItem.getValue().length() > 0)
                        {
                            needRepaint = true;
                        }
                    }
                    else
                    {
                        if (!regionName.equals(mapDownloadItem.getValue()))
                        {
                            needRepaint = true;
                        }
                    }
                    
                    mapDownloadItem.setTitle(download);
                    mapDownloadItem.setValue(regionName);
                    
                    if(needRepaint)
                    {
                        mapDownloadItem.requestPaint();
                    }
                }
            }
        });
    }
    
    private void addFeedbackItem(AbstractTnContainer container)
    {
        AbstractTnContainer feedbackCarContainer = UiFactory.getInstance().createLinearContainer(0, true);
        feedbackCarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        String title = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_FEEDBACK, IStringPreference.FAMILY_PREFERENCE);
        addProfileItemTitle(feedbackCarContainer, title);
        addTitleSplitLine(feedbackCarContainer);
        String feedbackStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_LEAVE_US_FEEDBACK, IStringPreference.FAMILY_PREFERENCE);

        addSingleItem(feedbackCarContainer, feedbackStr, CMD_GENERAL_FEEDBACK, null);
        container.add(feedbackCarContainer);
    }
    
    private void addScountInfos(AbstractTnContainer container)
    {
        AbstractTnContainer scountInfosContainer = UiFactory.getInstance().createLinearContainer(0, true);
        scountInfosContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);

        String title = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_SCOUT_INFO, IStringPreference.FAMILY_PREFERENCE);
        addProfileItemTitle(scountInfosContainer, title);
        addTitleSplitLine(scountInfosContainer);

        String newLabel = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_NEW_ITEM, IStringPreference.FAMILY_PREFERENCE);
        
        
        boolean isSharaed =  DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USED_SHARE_SCOUT);
        boolean isRated =  DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USED_RATE_SCOUT);
        
        
        String shareStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_SHARE, IStringPreference.FAMILY_PREFERENCE);
        addBadgeItem(scountInfosContainer, shareStr, "", newLabel, CMD_SHARE_SCOUT, !isSharaed, false, shareStr);
        addItemSplitLine(scountInfosContainer);
        
        
        String rateStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_RATE, IStringPreference.FAMILY_PREFERENCE);
        addBadgeItem(scountInfosContainer, rateStr, "" ,newLabel, CMD_RATE_SCOUT, !isRated, false, rateStr);
        addItemSplitLine(scountInfosContainer);
        
        String aboutStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_ABOUT, IStringPreference.FAMILY_PREFERENCE);
        addSingleItem(scountInfosContainer, aboutStr, CMD_ABOUT_PAGE, aboutStr);
        addItemSplitLine(scountInfosContainer);

        String feedbackStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_LEAVE_US_FEEDBACK, IStringPreference.FAMILY_PREFERENCE);
        addSingleItem(scountInfosContainer, feedbackStr, CMD_GENERAL_FEEDBACK, feedbackStr);
        addItemSplitLine(scountInfosContainer);
        
        
        String supportInformation = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_SUPPORT_INFORMATION, IStringPreference.FAMILY_PREFERENCE);
        addSingleItem(scountInfosContainer, supportInformation, CMD_SUPPORT_INFO, supportInformation);
        addItemSplitLine(scountInfosContainer);

        String termsConditions = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringPreference.PREFERENCE_STR_TERMS_CONDITIONS, IStringPreference.FAMILY_PREFERENCE);
        addSingleItem(scountInfosContainer, termsConditions, CMD_TERMS_CONDITIONS, termsConditions);
        container.add(scountInfosContainer);
    }
    
    private void addProfileItemTitle(AbstractTnContainer container, String title)
    {
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, title);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.MY_PROFILE_ITEM_TITLE_COLOR),
            UiStyleManager.getInstance().getColor(UiStyleManager.MY_PROFILE_ITEM_TITLE_COLOR));
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM_TITLE));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((PreferenceUiDecorator) uiDecorator).PROFILE_ITEM_TITLE_HEIGHT);
        titleLabel.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0, ((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0);
        addNullField(container, 26);
        container.add(titleLabel);
        addNullField(container, 18);
    }
    
    private void addSingleItem(AbstractTnContainer container, String itemName, int commandId, String contentDesc)
    {
        CitizenProfileListItem item = new CitizenProfileListItemWithBadge(0);
        item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_BL));
        
        item.setValueColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL), UiStyleManager
            .getInstance().getColor(UiStyleManager.TEXT_COLOR_BL));
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", commandId);
        item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        item.setCommandEventListener(this);
        item.setTitle(itemName);
        item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
        
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).PROFILE_ITEM_CONTENT_HEIGHT);
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        item.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0, ((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0);
        item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROFILE_LIST_ITEM);
        if (contentDesc != null && contentDesc.length() > 0)
            item.setContentDescription(contentDesc);
        container.add(item);
    }
    
    private void addBadgeItem(AbstractTnContainer container, String itemName, String itemValue, String badge, int commandId, boolean needBadge, boolean isSquare, String contentDesc)
    {
        CitizenProfileListItemWithBadge item = new CitizenProfileListItemWithBadge(commandId);
        
        item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_BL));
        
        item.setValueColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL), UiStyleManager
            .getInstance().getColor(UiStyleManager.TEXT_COLOR_BL));
        
        AbstractTnFont badgeFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_PROFILE_BADGE);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", commandId);
        item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        item.setCommandEventListener(this);
        item.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
        item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_VALUE));
        item.setTitle(itemName);
        item.setValue(itemValue);
        if (contentDesc != null && contentDesc.length() > 0)
            item.setContentDescription(contentDesc);
        if(needBadge)
        {
            AbstractTnImage badgeImage = NinePatchImageDecorator.PROFILE_RED_BADGE_UNFOCUSED.getImage();
            int badgePadding = (int)(badgeFont.stringWidth("1") * 1.5);
            int badgeHeight = (int)(badgeFont.getHeight() + badgePadding * 0.7 * 2);//HARDCODE FOR¡¡A APPROPRIATE HEIGHT
            int badgeWidth;
            
            if (isSquare)
            {
                badgeWidth = badgeHeight;
            }
            else
            {
                badgeWidth = badgeFont.stringWidth(badge) + badgePadding * 2;
            }
            
            item.setBadgeWidth(badgeWidth);
            item.setBadgeHeight(badgeHeight);
            
            item.setBadgePosition(AbstractTnGraphics.RIGHT);
            item.setBadgeFont(badgeFont);
            item.setBadge(badge);
            item.setBadgeColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                .getColor(UiStyleManager.TEXT_COLOR_WH));
            item.setBadgeImage(badgeImage);
        }
        
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).PROFILE_ITEM_CONTENT_HEIGHT);
        item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        item.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0, ((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0);
        item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROFILE_LIST_ITEM);
        container.add(item);
    }
    
    private void updateShareRateItem(AbstractTnContainer bodyContainer)
    {
        boolean isSharaed =  DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USED_SHARE_SCOUT);
        boolean isRated =  DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USED_RATE_SCOUT);
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        if(isSharaed)
        {
            CitizenProfileListItemWithBadge shareItem = (CitizenProfileListItemWithBadge) bodyContainer.getComponentById(CMD_SHARE_SCOUT);
            shareItem.setBadgeImage(null);
        }
        if(isRated)
        {
            CitizenProfileListItemWithBadge rateItem = (CitizenProfileListItemWithBadge) bodyContainer.getComponentById(CMD_RATE_SCOUT);
            rateItem.setBadgeImage(null);
        }
    }

    
    private AbstractTnComponent createProfileSwitcherItem(String itemName, boolean isSwitchOn, int commandId)
    {
        CitizenProfileSwitcher profileSwitcher = UiFactory.getInstance().createCitizenProfileSwitcher(commandId, isSwitchOn, itemName);
        profileSwitcher.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((PreferenceUiDecorator) uiDecorator).PROFILE_ITEM_CONTENT_HEIGHT);
        profileSwitcher.getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        profileSwitcher.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0,
            ((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0);
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", CMD_NONE);
        profileSwitcher.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        profileSwitcher.setCommandEventListener(this);
        return profileSwitcher;
    }
    
    private FrogButton createBtn(int id ,String itemName, int commandId)
    {
        FrogButton btn = UiFactory.getInstance().createButton(id, itemName);
        btn.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_BUTTON));
            
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", commandId);
        btn.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        btn.setCommandEventListener(this);
        btn.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_BUTTON));  
        btn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).ITEM_BUTTON_HEIGHT);
        btn.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_BUTTON_WIDTH);
        return btn;
    }
    
    private void addNullField(AbstractTnContainer container, int height)
    {
        FrogNullField nullField = new FrogNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, (new NullFieldDecorator(height).NULL_FIELD_HEIGHT));
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        container.add(nullField);
    }

    private void addDoubleLinesItem(AbstractTnContainer container, String itemName,  String value, int commandId, boolean isIdPref, String contentDesc)
    {
        CitizenProfileListItem prefItem = new CitizenProfileListItem(0);
        prefItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).PROFILE_ITEM_CONTENT_HEIGHT);
        prefItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator) uiDecorator).ITEM_CONTAINER_WIDTH);
        prefItem.setBoldFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
        prefItem.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_VALUE));
        
        if(isIdPref)
        {
            prefItem.setTitleColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_ITEM_DISABLE_COLOR), 
                UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_ITEM_DISABLE_COLOR));
            prefItem.setValueColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_ITEM_DISABLE_COLOR), 
                UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_ITEM_DISABLE_COLOR));
            prefItem.setEnabled(false);
            prefItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROFILE_LIST_ITEM);
            prefItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, null);
        }
        else
        {
            prefItem.setTitleColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_COLOR_BL), 
                UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_COLOR_BL));
            prefItem.setValueColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_ITEM_VALUE_COLOR), 
                UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_ITEM_VALUE_COLOR));
            prefItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.PROFILE_LIST_ITEM);
            prefItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, null);
        }

        prefItem.setTitle(itemName);
        prefItem.setValue(value);
        prefItem.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0,
            ((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0);
        if (contentDesc != null && contentDesc.length() > 0)
            prefItem.setContentDescription(contentDesc);
        
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add("", commandId);
        prefItem.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        prefItem.setCommandEventListener(this);
        if (commandId == CMD_GO_TO_SET_HOME)
        {
            prefItem.setMenu(createHomeContextMenu(), AbstractTnComponent.TYPE_CONTEXT);
        }
        else if (commandId == CMD_GO_TO_SET_WORK)
        {
            prefItem.setMenu(createWorkContextMenu(), AbstractTnComponent.TYPE_CONTEXT);
        }
        container.add(prefItem);
    }
    
    private TnMenu createHomeContextMenu()
    {
        TnMenu contextMenu = UiFactory.getInstance().createMenu();
        contextMenu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_EDIT, IStringCommon.FAMILY_COMMON),
            CMD_GO_TO_SET_HOME);
        contextMenu.add(
            ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_DELETE, IStringCommon.FAMILY_COMMON),
            CMD_DELETE_HOME);
        String title = ResourceManager.getInstance().getText(IStringPreference.PREFERENCE_HOME_ADDRESS_TITLE, IStringPreference.FAMILY_PREFERENCE);
        contextMenu.setHeaderTitle(title);
        return contextMenu;
    }

    private TnMenu createWorkContextMenu()
    {
        TnMenu contextMenu = UiFactory.getInstance().createMenu();
        contextMenu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_EDIT, IStringCommon.FAMILY_COMMON),
            CMD_GO_TO_SET_WORK);
        contextMenu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_DELETE, IStringCommon.FAMILY_COMMON),
            CMD_DELETE_WORK);
        String title = ResourceManager.getInstance().getText(IStringPreference.PREFERENCE_WORK_ADDRESS_TITLE, IStringPreference.FAMILY_PREFERENCE);
        contextMenu.setHeaderTitle(title);
        return contextMenu;
    }
    
    private void addDoubleLinesItem(AbstractTnContainer container, String itemName,  String value, int commandId, String contentDesc)
    {
        addDoubleLinesItem(container, itemName,  value, commandId, false, contentDesc);
    }
     
    protected void addTitleSplitLine(AbstractTnContainer parent)
    {
        FrogLabel splitLine = new FrogLabel(0, null);
        splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, parent.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH));
        splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).PROFILE_TITLE_SEPERATOR);
        splitLine.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.MY_PROFILE_TITLE_SEPERATOR_COLOR));
        parent.add(splitLine);
    }
    
    protected void addItemSplitLine(AbstractTnContainer parent)
    {
        FrogLabel splitLine = new FrogLabel(0, null);
        splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, parent.getTnUiArgs().get(TnUiArgs.KEY_PREFER_WIDTH));
        splitLine.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).PROFILE_ITEM_SEPERATOR);
        splitLine.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.MY_PROFILE_ITEM_SEPERATOR_COLOR));
        parent.add(splitLine);
    }
    
    protected TnScreen createRootLayerScreen(int groupId)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_PREFERENCE_LAYER1);
        
        AbstractTnContainer titleContainer = screen.getTitleContainer();
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        String groupName = "";
        if(groupId == PreferenceGroup.ID_PREFERENCE_GROUP_NAV_AUDIO)//map & nav style
        {
            groupName = ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_MAPS_NAVIGATION,
                    IStringPreference.FAMILY_PREFERENCE);
        }
        else if(groupId == PreferenceGroup.ID_PREFERENCE_GROUP_SPEECH_RCOG) //audio
        {
            groupName = ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_STR_AUDIO,
                    IStringPreference.FAMILY_PREFERENCE);
        }
        else if(groupId == PreferenceGroup.ID_PREFERENCE_GROUP_SHARING) //sharing
        {
            groupName = ResourceManager
                    .getInstance()
                    .getCurrentBundle()
                    .getString(IStringPreference.PREFERENCE_SHARING,
                    IStringPreference.FAMILY_PREFERENCE);
        }
       
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, groupName);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), 
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, 
            ((PreferenceUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, 
            this.uiDecorator.SCREEN_WIDTH);
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleContainer.add(titleLabel);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
       
        AbstractTnContainer container = screen.getContentContainer();
        TnScrollPanel listContainer = UiFactory.getInstance().createScrollPanel(0, true);
        container.add(listContainer);
        int horizontalPadding=((PreferenceUiDecorator) uiDecorator).PROFILE_CONTAINER_PADDING.getInt();
        container.setPadding(horizontalPadding, 0, 0, 0);
        
        TnLinearContainer list = UiFactory.getInstance().createLinearContainer(1, true); 
        listContainer.set(list);
        
        int[] ids = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreferenceIdsByGroup(groupId, this.getRegion());
        if(ids != null && groupId == PreferenceGroup.ID_PREFERENCE_GROUP_NAV_AUDIO)
        {
            //Need to add distance Unit and region preference locally
            int[] tempIds = new int[ids.length + 2];
            tempIds[0] = Preference.ID_PREFERENCE_REGION;
            tempIds[1] = Preference.ID_PREFERENCE_DISTANCEUNIT;
            System.arraycopy(ids, 0, tempIds, 2, ids.length);
            ids = tempIds;
        }
        int size = 0;
        if(ids != null)
        {
            size = ids.length;
        }
        
        int maxLabelWidth = calcLabelMaxWidth(ids);
        
        for(int i = 0; i < size; i++)
        {
            Preference pref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(ids[i]);
            if(pref == null)
                continue;
            
            AbstractTnComponent uiComponent = constructPreferenceItem(maxLabelWidth, pref);
            if( uiComponent != null )
            {
                list.add(uiComponent);
            }
        }
        
        if(groupId != PreferenceGroup.ID_PREFERENCE_GROUP_SHARING)
        {
            FrogNullField nullField = UiFactory.getInstance().createNullField(0);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator) uiDecorator).GAP_HEIGHT_ON_SAVE);
            list.add(nullField);
            TnLinearContainer buttonContainer = new TnLinearContainer(0, false, AbstractTnGraphics.HCENTER);
            buttonContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
            buttonContainer.setPadding(0, 0, horizontalPadding, 0);
            list.add(buttonContainer);
        }
        return screen;
    }

    protected AbstractTnComponent constructPreferenceItem(final int maxLabelWidth, Preference pref)
    {
        AbstractTnComponent itemComponent = null;
        
        int id = pref.getId();
        switch(id)
        {
            case Preference.ID_PREFERENCE_ROUTETYPE:
            {
                PreferenceComboBox item = new PreferenceComboBox(id + ID_PREFERENCE_ITEM_BASE, pref.getName());
                TnUiArgs args = item.getTnUiArgs();
                item.setUiDecorator(uiDecorator);
                args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
                args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
                args.put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator)uiDecorator).ITEM_CONTAINER_WIDTH);
                args.put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator)uiDecorator).PROFILE_ITEM_CONTENT_HEIGHT);
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
                item.setForegroundColor(
                    UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL),
                    UiStyleManager.getInstance()
                            .getColor(UiStyleManager.TEXT_COLOR_BL));
                item.setSelectedColor(
                    UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_ITEM_VALUE_COLOR),
                    UiStyleManager.getInstance().getColor(
                        UiStyleManager.TEXT_ITEM_VALUE_COLOR));
                item.setSelectedFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_VALUE));
                item.setOptions(new String[]{getRouteSettingString(false)});
                item.setSelectedIndex(0);
                item.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0, ((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0);
                
                itemComponent = item; 
                
                itemComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,  ((PreferenceUiDecorator)uiDecorator).ITEM_CONTAINER_WIDTH);
                
                itemComponent.setTouchEventListener(this);
                return itemComponent;
            }
            case Preference.ID_FAVORITES_ICONS_ON_MAPS:
            case Preference.ID_PREFERENCE_ROUTE_SETTING:
            {
                return null;
            }
            case Preference.ID_PREFERENCE_TRAFFIC_CAMERA_ALERT:
            case Preference.ID_PREFERENCE_SPEED_TRAP_ALERT:
            case Preference.ID_PREFERENCE_TRAFFICALERT:
            case Preference.ID_PREFERENCE_LANE_ASSIST:
            case Preference.ID_PREFERENCE_SPEED_LIMITS:
            {
                if(!DaoManager.getInstance().getBillingAccountDao().isPremiumAccount())
                {
                    return null;
                }
                String itemName = pref.getName();
                boolean isSwitchOn = (pref.getIntValue() == 0);
                return createProfileSwitcherItem(itemName, isSwitchOn, id + ID_PREFERENCE_ITEM_BASE);
            }
            default:
            {
                PreferenceComboBox item = new PreferenceComboBox(id + ID_PREFERENCE_ITEM_BASE, pref.getName());
                TnUiArgs args = item.getTnUiArgs();
                item.setUiDecorator(uiDecorator);
                args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
                args.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
                args.put(TnUiArgs.KEY_PREFER_WIDTH, ((PreferenceUiDecorator)uiDecorator).ITEM_CONTAINER_WIDTH);
                args.put(TnUiArgs.KEY_PREFER_HEIGHT, ((PreferenceUiDecorator)uiDecorator).PROFILE_ITEM_CONTENT_HEIGHT);
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
                item.setForegroundColor(
                    UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL), 
                    UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_BL));
                item.setSelectedColor(
                    UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_ITEM_VALUE_COLOR), 
                    UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_ITEM_VALUE_COLOR));
                item.setSelectedFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_VALUE));
                
                if(id == Preference.ID_PREFERENCE_REGION)
                {
                    item.setEnabled(false);
                    item.setForegroundColor(
                        UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_ITEM_DISABLE_COLOR), 
                        UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_ITEM_DISABLE_COLOR));
                    item.setSelectedColor(
                        UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_ITEM_DISABLE_COLOR), 
                        UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_ITEM_DISABLE_COLOR));
                    item.setOptions(pref.getOptionNames());
                }
                else if (id == Preference.ID_PREFERENCE_LANGUAGE)
                {
                    String[] displayOptionNames = new String[pref.getOptionNames().length];
                    for (int optionIdx=0; optionIdx<pref.getOptionNames().length; optionIdx++)
                    {
                        int pos = pref.getOptionNames()[optionIdx].indexOf('|');
                        if (pos != -1)
                        {
                            displayOptionNames[optionIdx] = pref.getOptionNames()[optionIdx].substring(0, pos);
                        }
                        else
                        {
                            displayOptionNames[optionIdx] = pref.getOptionNames()[optionIdx];
                        }
                    }
                    item.setOptions(displayOptionNames);
                }
                else
                {
                    item.setOptions(pref.getOptionNames());
                }
                
                int setValue = pref.getIntValue();
                item.setSelectedIndex(PreferenceDao.value2Index(pref, setValue));
                item.setPadding(((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0, ((PreferenceUiDecorator) uiDecorator).PROFILE_TEXT_PADDING.getInt(), 0);
                itemComponent = item;
            }
        }
        TnMenu menu = new TnMenu();
        menu.add("", CMD_NONE);
        itemComponent.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        itemComponent.setCommandEventListener(this);
        return itemComponent;
    }
    
    protected String getRouteSettingString(boolean isFullString)
    {
        StringBuffer result = new StringBuffer();
        PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
        Preference routeTypePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
        Preference routeSettingPref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
        if (routeTypePref != null)
        {
            result.append(getPreferenceString(routeTypePref));
        }
        if (routeSettingPref != null)
        {
        	if (routeTypePref.getIntValue() == Route.ROUTE_PEDESTRIAN)
        	{
        		result.append("");
        	}
        	else
        	{
        		String[] choices = routeSettingPref.getOptionNames();
        		boolean[] selected = getMultiChoices(routeSettingPref);
        		for (int i = 0; i < selected.length; i++)
        		{
        			if (isFullString)
        			{
        				if (choices != null && i < choices.length)
        				{
        					if (selected[i])
        					{
        						result.append(", ").append(choices[i]);
        					}
        				}
        			}
        			else
        			{
        				if (selected[i])
        				{
        					result.append(APPEND_CHAR);
        					break;
        				}
        			}
        		}        		
        	}
            
        }
        return result.toString();
    }
    
    protected String getPreferenceString(Preference pref)
    {
        if (pref == null)
        {
            return "";
        }
        String result = "";

        int choice = pref.getIntValue();
        int[] choices = pref.getOptionValues();
        String[] choiceStrings = pref.getOptionNames();
        if (choices != null && choiceStrings != null)
        {
            for (int i = 0; i < choices.length && i < choiceStrings.length; i++)
            {
                if (choice == choices[i])
                {
                    result = choiceStrings[i];
                    break;
                }
            }
        }

        return result;
    }
    
    protected boolean[] getMultiChoices(Preference pref)
    {
        if (pref != null && pref.getOptionNames() != null)
        {
            boolean[] bSelected = new boolean[pref.getOptionNames().length];
            int remainValue = pref.getIntValue();
            if (pref.getId() == Preference.ID_PREFERENCE_ROUTE_SETTING)
            {
                int trafficEnableValue = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_TRAFFIC);
                boolean isTrafficEnabled = trafficEnableValue == FeaturesManager.FE_ENABLED || trafficEnableValue == FeaturesManager.FE_PURCHASED;
                if (!isTrafficEnabled)
                {
                    if ((pref.getIntValue() & Preference.AVOID_TRAFFIC_DELAYS) != 0)
                    {
                        remainValue = remainValue - Preference.AVOID_TRAFFIC_DELAYS;
                    }
                }
            }
            PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
            Preference routeSettingPref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
            
            if(routeSettingPref != null)
            {
                int[] optionVals = routeSettingPref.getOptionValues();
                int index = -1;            
                int len = optionVals.length;
                for (int i = 0; i < len; i++)
                {
                    if (optionVals[i] == Preference.USE_CARPOOL_LANES)
                    {
                        index = i;
                        break;
                    }
                }
                if(index != -1)
                {
                    bSelected[index] = ((remainValue & Preference.AVOID_CARPOOL_LANES) == 0) ? true : false;
                }
                for (int j = 0; j < optionVals.length; j++)
                {
                    if ((optionVals[j] & remainValue) <= 0 || j == index)
                        continue;
                    bSelected[j] = true;
                }
            }
            
            return bSelected;
        }
        return null;
    }    

    protected int calcLabelMaxWidth(int[] ids)
    {
        int maxWidth = 0;
        if( ids == null )
        {
            return maxWidth;
        }
        for(int i = 0; i < ids.length; i++)
        {
            Preference pref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(ids[i]);
            if( pref != null && pref.getId() != Preference.ID_PREFERENCE_TYPE_LASTNAME )
            {
                int labelWidth = UiStyleManager.getInstance()
                .getFont(UiStyleManager.FONT_LIST_SINGLE)
                .stringWidth(pref.getName()+":mm");
                if( maxWidth < labelWidth )
                {
                    maxWidth = labelWidth;
                }
            }
        }
        return maxWidth;
    }
    
    protected void checkUserInfoChanged()
    {
        int groupId = model.getInt(KEY_I_SELECTED_PREFERENCE_GROUP_ID);
        int[] ids = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreferenceIdsByGroup(groupId, this.getRegion());
        if( ids == null )
        {
            return;
        }
        TnScreen screen = null;
        for(int i = 0; i < ids.length; i++)
        {
            Preference pref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(ids[i]);
            if(pref == null)
                continue;
            
            int prefId = pref.getId();
            switch( prefId )
            {
                case Preference.ID_PREFERENCE_PHONE_NUMBER:
                {
                    break;
                }
                case Preference.ID_PREFERENCE_TYPE_FIRSTNAME:
                case Preference.ID_PREFERENCE_TYPE_LASTNAME:
                case Preference.ID_PREFERENCE_TYPE_EMAIL:
                case Preference.ID_PREFERENCE_TYPE_USERNAME:
                {
                    if( screen == null )
                    {
                        screen = this.getScreenByState(STATE_PREFERENCE_LAYER1);
                    }
                    CitizenTextField textField = 
                        (CitizenTextField)screen.getComponentById(prefId + ID_PREFERENCE_ITEM_BASE);
                    isUserInfoChanged(textField.getText(), pref.getStrValue(), prefId);
                    break;
                }
                default:
                    return;
            }
        }
    }
    
    protected void isUserInfoChanged(String textFieldValue, String prefValue, int prefId)
    {
        String value = textFieldValue.trim();
        if(prefValue == null || !prefValue.equals(value))
        {
            model.put(KEY_B_IS_PREFERENCE_CHANGE, true);
            model.put(KEY_B_IS_NEED_UPLOAD, true);
        }
        ((Hashtable)model.get(KEY_O_USER_INFO)).put(PrimitiveTypeCache.valueOf(prefId), value);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        switch(state)
        {
            case STATE_PREFERENCE_LAYER1:
            {
                if(tnUiEvent.getType() == TnUiEvent.TYPE_TOUCH_EVENT)
                {
                    TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();
                    int action = motionEvent.getAction();
                    if(action == TnMotionEvent.ACTION_UP)
                    {
                        if (tnUiEvent.getComponent().getId() == Preference.ID_PREFERENCE_ROUTETYPE + ID_PREFERENCE_ITEM_BASE
                                && tnUiEvent.getComponent().getId() < ID_PREFERENCE_ITEM_MAX)
                        {
                            PreferenceDao preferenceDao = ((DaoManager) DaoManager.getInstance()).getPreferenceDao();
                            
                            Preference routeTypePref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTETYPE);
                            Preference routeSettingPref = preferenceDao.getPreference(Preference.ID_PREFERENCE_ROUTE_SETTING);
                            
                            int oldRouteTypeValue = -1;
                            if(routeTypePref != null)
                            {
                                oldRouteTypeValue = routeTypePref.getIntValue();
                            }
                            
                            int oldRouteSettingValue = -1;
                            if(routeSettingPref != null)
                            {
                                oldRouteSettingValue = routeSettingPref.getIntValue();
                            }

                            model.put(KEY_I_OLD_ROUTE_STYLE_VALUE, oldRouteTypeValue);
                            model.put(KEY_I_OLD_ROUTE_SETTING_VALUE, oldRouteSettingValue);
                            return CMD_GO_TO_ROUTE_SETTING;
                        }
                    }
                }
                
                break;
            }
        }

        return CMD_NONE;
    }
      
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        switch (model.getState())
        {
            case STATE_PREFERENCE_ROOT:
            {
                if (notificationContainer != null && notificationContainer.isShown())
                {
                    showNotificationContainer(STATE_PREFERENCE_ROOT);
                }
                break;
            }
        }
    }
    
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();
        
        switch(state)
        {
            case STATE_PREFERENCE_LAYER1:
            {
                AbstractTnComponent component = tnUiEvent.getComponent();
                if( component == null )
                {
                    break;
                }
                if( component instanceof PreferenceComboBox )
                {
                    if(component.getId() == Preference.ID_PREFERENCE_ROUTETYPE + ID_PREFERENCE_ITEM_BASE
                            && tnUiEvent.getComponent().getId() < ID_PREFERENCE_ITEM_MAX)
                    {
                        break;
                    }
                    else
                    {
                        int preferenceId = component.getId() - ID_PREFERENCE_ITEM_BASE;
                        if(preferenceId < 0)
                        {
                            break;
                        }
                        Preference preference = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(preferenceId);
                        PreferenceComboBox frogCombo = (PreferenceComboBox)component;
                        int index = frogCombo.getSelectedIndex();
                        int value = PreferenceDao.index2Value(preference, index);
                        if(value != ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getIntValue(preferenceId))
                        {
                            model.put(KEY_B_IS_PREFERENCE_CHANGE, true);
                            if( preferenceId == Preference.ID_PREFERENCE_DISTANCEUNIT )
                            {
                                ((DaoManager)DaoManager.getInstance()).getPreferenceDao().setIntValue(preferenceId, value);
                            }
                            else if( preferenceId == Preference.ID_PREFERENCE_LANGUAGE )
                            {
                                model.put(KEY_B_IS_LANGUAGE_CHANGE, true);
                                model.put(KEY_I_CHANGED_LANGUAGE, value);
                                model.put(KEY_S_CHANGED_LANGUAGE, convertLocale(value));
                                this.handleViewEvent(CMD_SWITCH_LANGUAGE);
                            }                            
                            else
                            {
                                ((DaoManager)DaoManager.getInstance()).getPreferenceDao().setIntValue(preferenceId, value);
                                if(preferenceId == Preference.ID_PREFERENCE_BACKLIGHT && value == Preference.BACKLIGHT_ON)
                                {                                	
                            		TnBacklightManagerImpl.getInstance().enable();
                                    TnBacklightManagerImpl.getInstance().start();                     	
                                }
                            }
                            return true;
                        }
                    }
                }
                else if( component instanceof FrogDropDownField )
                {
                    int preferenceId = component.getId() - ID_PREFERENCE_ITEM_BASE - CitizenTextField.KEY_ID_OPERATOR_NUMBER;
                    if(preferenceId < 0)
                    {
                        break;
                    }
                    FrogDropDownField frogTextField = (FrogDropDownField)component;
                    String value = frogTextField.getText();
                    Preference pref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(preferenceId);
                    isUserInfoChanged(value, pref.getStrValue(), preferenceId);
                }
                else if(component instanceof CitizenProfileSwitcher)
                {
                    CitizenProfileSwitcher switcher = (CitizenProfileSwitcher)component;
                    TnCommandEvent event = tnUiEvent.getCommandEvent();
                    if(switcher.getId() >=  ID_PREFERENCE_ITEM_BASE && event != null && event.getCommand() == CMD_CHANGE_SWITCH)
                    {
                        int prefId = switcher.getId() - ID_PREFERENCE_ITEM_BASE;
                        switch(prefId)
                        {
                            case Preference.ID_PREFERENCE_TRAFFIC_CAMERA_ALERT:
                            case Preference.ID_PREFERENCE_SPEED_TRAP_ALERT:
                            case Preference.ID_PREFERENCE_TRAFFICALERT:
                            case Preference.ID_PREFERENCE_LANE_ASSIST:
                            case Preference.ID_PREFERENCE_SPEED_LIMITS:
                            {
                                Preference preference = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(prefId);
                                int index = switcher.isSwitchOn() ? 0 : 1;
                                int value = PreferenceDao.index2Value(preference, index);
                                if(value != ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getIntValue(prefId))
                                {
                                    model.put(KEY_B_IS_PREFERENCE_CHANGE, true);
                                    ((DaoManager)DaoManager.getInstance()).getPreferenceDao().setIntValue(prefId, value);
                                }
                                break;
                            }
                        }
                    }
                }
                else if( component instanceof TnLinearContainer )
                {
                    TnCommandEvent event = tnUiEvent.getCommandEvent();
                    if(event != null && event.getCommand() == CMD_SAVE_UPLOAD_PREFERENCE)
                    {
                        checkUserInfoChanged();
                    }
                   
                }
                else if( component instanceof FrogButton )
                {
                    checkUserInfoChanged();
                }
				break;
            }
            case STATE_PREFERENCE_ROOT:
            {
                int type = tnUiEvent.getType();
                if(type == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    if(tnUiEvent.getCommandEvent() != null && tnUiEvent.getCommandEvent().getCommand() == CMD_HIDE_NOTIFICATION)
                    {
                        notificationContainer = null;
                        return true;
                    }
                    else
                    {
                        AbstractTnComponent component = tnUiEvent.getComponent();
                        if(component != null)
                        {
                            int id = component.getId();
                            int preferenceGroupId = id - ID_PREFERENCE_GROUP_BASE;
                            PreferenceGroup[] prefGroups = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreferenceGroups(this.getRegion());
                            if (prefGroups != null)
                            {
                                boolean isFound = false;
                                int i = 0;
                                for(; i < prefGroups.length; i ++)
                                {
                                    if(prefGroups[i].getId() == preferenceGroupId)
                                    {
                                        isFound = true;
                                        break;
                                    }
                                }
                                if(isFound)
                                {
                                    model.put(KEY_I_SELECTED_PREFERENCE_GROUP_ID, preferenceGroupId);
                                    model.put(KEY_S_SELECTED_PREFERENCE_GROUP_NAME, prefGroups[i].getName());
                                }
                            }
                        }
                    }
                }
               break;
            }
            case STATE_MY_CAR:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int id = tnUiEvent.getCommandEvent().getCommand();
                    if(id >= CMD_SELECT_CAR_START && id <= CMD_SELECT_CAR_END)
                    {
                        int newCarIndex = id - CMD_SELECT_CAR_START;
                        int lastCarIndex = model.getInt(KEY_I_CAR_MODEL_VALUE);
                        
                        if(newCarIndex != lastCarIndex)
                        {
                            TnScreen screen = this.getScreenByState(STATE_MY_CAR);
                            CitizenCarListItem lastSelectedItem = (CitizenCarListItem)screen.getComponentById(CMD_SELECT_CAR_START + lastCarIndex);
                            CitizenCarListItem newSelectedItem = (CitizenCarListItem)screen.getComponentById(CMD_SELECT_CAR_START + newCarIndex);
                            lastSelectedItem.setSelected(false);
                            newSelectedItem.setSelected(true);
                            model.put(KEY_I_CAR_MODEL_VALUE, newCarIndex);
                            tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_SELECT_CAR));
                        }
                    }
                }
                break;
            }
        }
        
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    
    private String convertLocale(int value)
    {
        String languageCode = "en_US";
        
        Preference languagePref = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getPreference(Preference.ID_PREFERENCE_LANGUAGE);
        if(languagePref != null)
        {
            String[] optionNames = languagePref.getOptionNames();
            int[] optionValues = languagePref.getOptionValues();
            for (int optionIdx = 0; optionIdx < optionNames.length; optionIdx++)
            {
                if (optionValues[optionIdx] == value)
                {
                    int pos = optionNames[optionIdx].indexOf('|');
                    if (pos != -1)
                    {
                        languageCode = optionNames[optionIdx].substring(pos+1); 
                    }
                }
            }
        }
        
        return languageCode;
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch (state)
        {
            case STATE_PREFERENCE_ROOT:
            {
                if (commandId == CMD_SHARE_SCOUT)
                {
                    DaoManager.getInstance().getSimpleConfigDao().put(SimpleConfigDao.KEY_USED_SHARE_SCOUT, true);
                    DaoManager.getInstance().getSimpleConfigDao().store();
                }
                break;
            }
            default:
                break;
        }

        return true;
    }
    
    private static class NullFieldDecorator extends AbstractCommonUiDecorator
    {

        private int unit;
        
        
        private final static int ID_NULL_FIELD_HEIGHT = 1;
        TnUiArgAdapter NULL_FIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_NULL_FIELD_HEIGHT), this);

        
        public NullFieldDecorator(int unit)
        {
            super();
            this.unit = unit;
        }

        @Override
        protected Object decorateDelegate(TnUiArgAdapter args)
        {
            int keyValue = ((Integer) args.getKey()).intValue();
            switch (keyValue)
            {
                case ID_NULL_FIELD_HEIGHT:
                {
                    return PrimitiveTypeCache.valueOf( AppConfigHelper.getMaxDisplaySize()*this.unit * 1 / 1280);
                }
            }
            return null;
        }
        
    }
    
    private static class CarListAdapter implements FrogAdapter
    {
        private String[] carNames;
        private TnUiArgs.TnUiArgAdapter[] carImages;
        PreferenceUiDecorator uiDecorator;
        private int selectedIndex = -1;
   
        public CarListAdapter(String[] carNames, TnUiArgs.TnUiArgAdapter[] carImages, PreferenceUiDecorator uiDecorator, int selectedIndex)
        {
            this.carNames = carNames;
            this.carImages = carImages;
            this.uiDecorator = uiDecorator;
            this.selectedIndex = selectedIndex;
        }

        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            CitizenCarListItem item = null;
            if (convertComponent == null)
            {
                item = new CitizenCarListItem(0);
            }
            else
            {
                item = (CitizenCarListItem) convertComponent;
            }
            
            item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.PROFILE_ITEM_CONTENT_HEIGHT);
            item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.ITEM_CONTAINER_WIDTH);
            item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.LIST_ITEM_FOCUSED);
            item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.LIST_ITEM_UNFOCUS);
            item.setCarName(carNames[position]);
            item.setCarImage(carImages[position].getImage());
            item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_MY_PROFILE_ITEM));
            item.setId(CMD_SELECT_CAR_START + position);
            item.setForegroundColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_COLOR_BL), 
                UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_COLOR_BL));
            if(this.selectedIndex == position)
            {
                item.setSelected(true);
            }
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", CMD_SELECT_CAR_START + position);
            item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            return item;
        }

        public int getCount()
        {
            return carNames.length;
        }

        public int getItemType(int position)
        {
            return 0;
        }
    }

    @Override
    public void onAccountTypeChanged()
    {
        CitizenScreen screen = (CitizenScreen) getScreenByState(STATE_PREFERENCE_ROOT);

        if (screen != null)
        {
            updateMapDownloadBadge(screen.getContentContainer());
        }
    }

    @Override
    public void onScreenUiEngineAttached(TnScreen screen, int attached)
    {
        if(screen.getId() == STATE_PREFERENCE_ROOT)
        {
            if(attached == AFTER_ATTACHED)
            {
                AccountChangeListener.getInstance().addListener(this);
                MapDownloadOnBoardDataStatusManager.getInstance().addStatusChangeListener(this);
                MapDownloadStatusManager.getInstance().addDownloadStatusChangeListener(this);
            }
            else if(attached == DETTACHED)
            {
                AccountChangeListener.getInstance().removeListener(this);
                MapDownloadOnBoardDataStatusManager.getInstance().removeStatusChangeListener(this);
                MapDownloadStatusManager.getInstance().removeDownloadStatusChangeListener(this);
            }
        }
    }

    @Override
    public void onLocalMapDataAvailabilityChanged(boolean isAvailable)
    {
        CitizenScreen screen = (CitizenScreen) getScreenByState(STATE_PREFERENCE_ROOT);

        if (screen != null)
        {
            updateMapDownloadBadge(screen.getContentContainer());
            updateMapDownloadItem(screen.getContentContainer());
        }
    }

    @Override
    public void onMapDownloadStatusChanged()
    {
        CitizenScreen screen = (CitizenScreen) getScreenByState(STATE_PREFERENCE_ROOT);

        if (screen != null)
        {
            updateMapDownloadBadge(screen.getContentContainer());
        }
    }
}
