/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * UpSellView.java
 *
 */
package com.telenav.module.upsell;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.cache.ImageCacheManager;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringLogin;
import com.telenav.res.IStringPreference;
import com.telenav.res.IStringTouring;
import com.telenav.res.IStringUpSell;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.android.ImageIndicator;
import com.telenav.ui.android.OneStepGallery;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-20
 */
class UpSellViewTouch extends AbstractCommonView implements IUpSellConstants
{
    protected Button backBtn;
    private ImageAdapter imageAdapter;
    private ImageIndicator imageIndexer;
    
    public UpSellViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        switch (state)
        {
            case STATE_LEARN_LIST:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
                    int commandId = commandEvent.getCommand();
                    if (commandId >= IUpSellConstants.CMD_SELECT_LEARN_LIST
                            && commandId < IUpSellConstants.CMD_SELECT_LEARN_LIST + 10)
                    {
                        return CMD_SELECT_LEARN_LIST;
                    }
                }
                break;
            }
        }
        return CMD_NONE;
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();
        
        switch(state)
        {
            case STATE_LEARN_LIST:
            {
                if(tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    int commandId = tnUiEvent.getCommandEvent().getCommand();
                    if(commandId >= CMD_SELECT_LEARN_LIST && commandId < CMD_SELECT_LEARN_LIST + 10)
                    {
                        this.model.put(KEY_I_LEARN_ITEM_INDEX, commandId - CMD_SELECT_LEARN_LIST);
                        this.handleViewEvent(CMD_SELECT_LEARN_LIST);
                        return true;
                    }
                }
                break;
            }
            case STATE_UP_SELL:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                {
                    TnCommandEvent commandEvent = tnUiEvent.getCommandEvent();
                    int commandId = commandEvent.getCommand();
                    if (commandId == IUpSellConstants.CMD_FIRST_OPTION_SELECT)
                    {
                        UpsellOption upsellOption = ((UpsellOption) this.model.getVector(KEY_V_UPSELL_OPTIONS).get(0));
                        triggerViewEvent(upsellOption);
                        return true;
                    }
                    else if (commandId == IUpSellConstants.CMD_SECOND_OPTION_SELECT)
                    {
                        UpsellOption upsellOption=((UpsellOption) this.model.getVector(KEY_V_UPSELL_OPTIONS).get(1));
                        triggerViewEvent(upsellOption);
                        return true;
                    }
                    else if (commandId == IUpSellConstants.CMD_THIRD_OPTION_SELECT)
                    {
                        UpsellOption upsellOption=((UpsellOption) this.model.getVector(KEY_V_UPSELL_OPTIONS).get(2));
                        triggerViewEvent(upsellOption);
                        return true;
                    }
                    
                } 
                break;
            }
            
        }
        return super.preProcessUIEvent(tnUiEvent);
    }
    
    private void triggerViewEvent( UpsellOption upsellOption)
    {
        this.model.put(KEY_O_UPSELL_OPTIONS_SELECT, upsellOption);
        if (UpsellOption.CARRIER_BILLING_SOURCE_TYPE.equalsIgnoreCase(upsellOption.getSourceTypeCode()))
        {
            this.handleViewEvent(CMD_UPSELL_OPTION_CONFIRM);
        }
        else
        {
            this.handleViewEvent(CMD_UPSELL_OPTION_SUBMIT);
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_UPGRADE,
                KontagentLogger.UPGRADE_BUY_CLICKED);
        }
    }
    
    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        
        switch(state)
        {
            case STATE_INIT:
            {
                String str = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringCommon.RES_LABEL_LOADING, IStringCommon.FAMILY_COMMON);
                popup = UiFactory.getInstance().createProgressBox(0, str);
                break;
            }
            case STATE_UPSELL_OPTION_SUBMIT:
            {
                String sendRequestStr = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringLogin.RES_LABLE_SENDING_PIN, IStringLogin.FAMILY_LOGIN);
                popup = UiFactory.getInstance().createProgressBox(0, sendRequestStr);
                break;
            }
            case STATE_NON_SCOUT_UPSELL_PURCHASE_SUCCESS:
            {
                UpsellOption upsellOption = (UpsellOption) this.model.get(KEY_O_UPSELL_OPTIONS_SELECT);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(
                    ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringCommon.RES_BTTN_DONE, IStringCommon.FAMILY_COMMON), CMD_NON_SCOUT_UPSELL_SUCCESS);
                String message;
                if (upsellOption != null && upsellOption.getPriceValue() > 0)
                {
                    String unit = null;
                    if (DOLLAR_ABBR.equalsIgnoreCase(upsellOption.getPriceUnit()))
                        unit = DOLLAR_SIGN;
                    else
                        unit = upsellOption.getPriceUnit();
                    String offerValue = unit + upsellOption.getPriceValue();
                    message = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringUpSell.RES_NON_SCOUT_PURCHASE_SUCCESS_CHANGE_MSG, IStringUpSell.FAMILY_UPSELL);
                    StringConverter converter = ResourceManager.getInstance().getStringConverter();
                    message = converter.convert(message, new String[]
                    { offerValue });
                }
                else
                    message = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringUpSell.RES_NON_SCOUT_PURCHASE_SUCCESS_NO_CHANGE_MSG, IStringUpSell.FAMILY_UPSELL);
                popup = UiFactory.getInstance().createMessageBox(state, message, menu);
                popup.setCommandEventListener(this);
                break;
            }
            case STATE_CANCELLING_SUBSCRIPTION:
            {
                String sendRequestStr = ResourceManager
                        .getInstance()
                        .getCurrentBundle()
                        .getString(IStringPreference.PREFERENCE_STR_CANCELLING_SUBSCRIPTION,
                            IStringPreference.FAMILY_PREFERENCE);
                popup = UiFactory.getInstance().createProgressBox(state, sendRequestStr);
                break;
            }
            case STATE_CANCEL_EXCEPTION:
            {
                String errorMsg = model.getString(KEY_S_ERROR_MESSAGE);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
                    IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
                popup = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
                popup.setCommandEventListener(this);
                break;
            }
            case STATE_GET_UPSELL_OPTION_ERROR:
            {
                String errorMsg = model.getString(KEY_S_ERROR_MESSAGE);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
                    IStringCommon.FAMILY_COMMON), CMD_COMMON_BACK);
                popup = UiFactory.getInstance().createMessageBox(state, errorMsg, menu);
                popup.setCommandEventListener(this);
                break;
            }
        }
        
        return popup;
    }

    protected TnScreen createUpSellScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        Vector optionVector = this.model.getVector(KEY_V_UPSELL_OPTIONS);
        int optionSize = optionVector.size();
        View layoutView = null;
        if (optionSize == UPSELL_TWO_OPTIONS)
        {
            layoutView = AndroidCitizenUiHelper.addContentView(screen, R.layout.upsell_welcome_twooptions);
        }
        else if (optionSize == UPSELL_THREE_OPTIONS)
        {
            layoutView = AndroidCitizenUiHelper.addContentView(screen, R.layout.upsell_welcome_threeoptions);
        }
        if (layoutView != null)
        {
            Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
            layoutView.setBackgroundDrawable(bgImage);
            
            TextView upsellTitle = (TextView) layoutView.findViewById(R.id.commonTitle0TextView);
            String upgradeStr = null;
            if (isCarConnectFeature())
            {    
                upgradeStr = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringUpSell.RES_TITLE_CAR_CONNECT, IStringUpSell.FAMILY_UPSELL);
            }
            else
            {
                if (hasFree())
                {
                    upgradeStr = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringUpSell.RES_TITLE_LIMIT_OFFER, IStringUpSell.FAMILY_UPSELL);
                }
                else
                {
                    upgradeStr = ResourceManager
                            .getInstance()
                            .getCurrentBundle()
                            .getString(IStringPreference.PREFERENCE_STR_UPGRADE_TO_SCOUNT_PLUS,
                                IStringPreference.FAMILY_PREFERENCE);
                }
            }

            upsellTitle.setText(upgradeStr);

            ImageView splitlineImageView = (ImageView) layoutView.findViewById(R.id.screensplitline);

            int key = (Integer) NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getKey();
            ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));

            AssetsImageDrawable imageDrawable = new AssetsImageDrawable(
                    NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());
            splitlineImageView.setBackgroundDrawable(imageDrawable);

            TextView upsellSubTitle = (TextView) layoutView.findViewById(R.id.upsellsubtitle);
            String upsellDescStr = null;
            if (isCarConnectFeature())
            {
                upsellDescStr = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringUpSell.RES_SUBTITLE_CAR_CONNECT, IStringUpSell.FAMILY_UPSELL);
            }
            else
            {
                upsellDescStr = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringUpSell.RES_UPSELL_DESCRIPTION, IStringUpSell.FAMILY_UPSELL);
            }
            upsellSubTitle.setText(upsellDescStr);

            final TextView upsellMore = (TextView) layoutView.findViewById(R.id.upselllearnmore);
            String learnMore = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringUpSell.RES_UPSELL_LEARN_MORE, IStringUpSell.FAMILY_UPSELL);
            upsellMore.setText(Html.fromHtml("<u>" + learnMore + "</u>"));
            upsellMore.setOnTouchListener(new LinkTouchListener(UiStyleManager.getInstance().getColor(
                UiStyleManager.UPSELL_LEARNING_ITEM_FOCUSED_COLOR), UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_COLOR_WH), upsellMore));
            AndroidCitizenUiHelper.setOnClickCommand(this, upsellMore, CMD_LEARN_MORE);
            
            final View imageContainer = (View) layoutView.findViewById(R.id.car_connect);
            if(this.isCarConnectFeature())
            {
                imageContainer.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) layoutView.findViewById(R.id.car_connect_image);
                imageView.setImageBitmap((Bitmap)(ImageDecorator.GALERY_CAR_CONNECT.getImage()).getNativeImage());
                View gapView = (View) layoutView.findViewById(R.id.gap_view);
                int gapPixel = AndroidCitizenUiHelper.getPixelsByDip(18);
                gapView.setMinimumHeight(gapPixel);
            }
            else
            {   
                imageContainer.setVisibility(View.INVISIBLE);
            }
            for (int index = 0; index < optionSize; index++)
            {
                UpsellOption upsellOption = (UpsellOption) optionVector.get(index);
                String btnText = upsellOption.getDisplayName();
                if (btnText == null)
                {
                    btnText = "";
                }
                Button option = null;
                int commandId = 0;
                if (index == 0)
                {
                    option = (Button) layoutView.findViewById(R.id.firstchoice);
                    commandId = CMD_FIRST_OPTION_SELECT;
                }
                else if (index == 1)
                {
                    option = (Button) layoutView.findViewById(R.id.secondchoice);
                    commandId = CMD_SECOND_OPTION_SELECT;
                }
                else if (index == 2)
                {
                    option = (Button) layoutView.findViewById(R.id.thirdchoice);
                    commandId = CMD_THIRD_OPTION_SELECT;
                }

                if (option != null)
                {
                    option.setText(Html.fromHtml(btnText));
                    setNativeButtonBg(option);
                    option.setTextColor(BTN_TOUCH_DOWN_COLOR);
                    AndroidCitizenUiHelper.setOnClickCommand(this, option, commandId);
                }
            }

            final TextView thanksLeft = (TextView) layoutView.findViewById(R.id.thanksleft);
            String thanksText = ResourceManager.getInstance().getCurrentBundle()
                    .getString(IStringUpSell.RES_UPSELL_NO_THANKS, IStringUpSell.FAMILY_UPSELL);
            thanksLeft.setText(thanksText);

            final ImageView arrowImage = (ImageView) layoutView.findViewById(R.id.thanksright);
            final AssetsImageDrawable arrowFocus = new AssetsImageDrawable(ImageDecorator.UPSELL_ARROW_FOCUSED);
            final AssetsImageDrawable arrowUnFocus = new AssetsImageDrawable(ImageDecorator.UPSELL_ARROW_UNFOCUSED);
            arrowImage.setImageDrawable(arrowUnFocus);

            LinearLayout thankyouLinearLayout = (LinearLayout) layoutView.findViewById(R.id.thankyou);
            AndroidCitizenUiHelper.setOnClickCommand(this, thankyouLinearLayout, CMD_COMMON_BACK);

            thankyouLinearLayout.setOnTouchListener(new OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    int action = event.getAction();
                    switch (action)
                    {
                        case MotionEvent.ACTION_DOWN:
                        {
                            thanksLeft.setTextColor(UiStyleManager.getInstance().getColor(
                                UiStyleManager.UPSELL_LEARNING_ITEM_FOCUSED_COLOR));
                            arrowImage.setImageDrawable(arrowFocus);
                            arrowImage.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                        {
                            thanksLeft.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
                            arrowImage.setImageDrawable(arrowUnFocus);
                            arrowImage.invalidate();
                            break;
                        }
                        default:
                            break;
                    }
                    return false;
                }
            });

            TextView recurringInfo = (TextView) layoutView.findViewById(R.id.upsellrecurringinfo);

            String recurringInfoStr = null;
            if (gotoAndroidMarket())
            {
                recurringInfoStr = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringUpSell.RES_RECURRING_IN_AMP, IStringUpSell.FAMILY_UPSELL);
            }
            else
            {
                if (hasFree())
                {
                    recurringInfoStr = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringUpSell.RES_RECURRING_HAS_FREE, IStringUpSell.FAMILY_UPSELL);
                }
                else
                {
                    recurringInfoStr = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringUpSell.RES_RECURRING_NO_FREE, IStringUpSell.FAMILY_UPSELL);
                }
            }
            recurringInfo.setText(recurringInfoStr);
        }
        return screen;
    }
    

    private void setNativeButtonBg(Button button)
    {
        int key = (Integer) NinePatchImageDecorator.FTUE_BUTTON_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        key = (Integer) NinePatchImageDecorator.FTUE_BUTTON_FOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(key-1);
        AssetsImageDrawable normalDrawable = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BUTTON_UNFOCUSED);
        AssetsImageDrawable pressedDrawable = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BUTTON_FOCUSED);
        StateListDrawable stateListDrawable = ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).createStateListDrawable(normalDrawable,
            pressedDrawable, normalDrawable, normalDrawable);
        button.setBackgroundDrawable(stateListDrawable);
    }
    
    private void setUpgradeButtonBg(Button button)
    {
        int key = (Integer) NinePatchImageDecorator.FTUE_BUTTON_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        key = (Integer) NinePatchImageDecorator.FTUE_BUTTON_FOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        AssetsImageDrawable normalDrawable = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BUTTON_UNFOCUSED);
        AssetsImageDrawable pressedDrawable = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BUTTON_FOCUSED);
        StateListDrawable stateListDrawable = ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).createStateListDrawable(normalDrawable,
            pressedDrawable, normalDrawable, normalDrawable);
        button.setBackgroundDrawable(stateListDrawable);
    }
    
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch (state)
        {
            case STATE_UP_SELL:
            {               
                screen = createUpSellScreen(state);
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_UPGRADE,
                    KontagentLogger.UPGRADE_UPSELL_PAGE_DISPLAYED);
                break;
            }
            case STATE_LEARN_LIST:
            {
                screen = createScoutPlusListScreen(state);
                break;
            }
            case STATE_LEARN_GALERY:
            {
                screen = createScoutPlusGaleryScreen(state);
                break;
            }
            case STATE_UPGRADE:
            {
                screen = createUpgradeScreen(state);
                break;
            }
        }
        return screen;
    }

    protected TnScreen createScoutPlusListScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        View learningListParentView = AndroidCitizenUiHelper.addContentView(screen, R.layout.upsell_learning_list);
        
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        learningListParentView.setBackgroundDrawable(bgImage);
        
        TextView titleView=(TextView)learningListParentView.findViewById(R.id.commonTitle0TextView);
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.RES_ABOUT_SCOUT_PLUS, IStringUpSell.FAMILY_UPSELL);    
        titleView.setText(title);   
        
        ImageView splitlineImageView=(ImageView)learningListParentView.findViewById(R.id.screensplitline);
        
        int key = (Integer) NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        
        AssetsImageDrawable imageDrawable = new AssetsImageDrawable(NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());        
        splitlineImageView.setBackgroundDrawable(imageDrawable);
        
        ListView listView = (ListView)learningListParentView.findViewById(R.id.learning_list);
        LearningListAdapter adapter = new LearningListAdapter(AndroidPersistentContext.getInstance().getContext(), IUpSellConstants.LEARN_ITEM_TITLE_IDS, IUpSellConstants.LEARN_ITEM_DESC_IDS, 
            IUpSellConstants.LEARN_ITEM_UNFOCUSED_ICON_ADAPTERS, IUpSellConstants.LEARN_ITEM_FOCUSED_ICON_ADAPTERS, this, true);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        return screen;
    }
    
    protected TnScreen createScoutPlusGaleryScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);

        View galleryParentView = AndroidCitizenUiHelper.addContentView(screen, R.layout.upsell_learning_gallery);
        
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        galleryParentView.setBackgroundDrawable(bgImage);
        
        TextView titleView=(TextView)galleryParentView.findViewById(R.id.commonTitle0TextView);
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.RES_LEARN_MORE, IStringUpSell.FAMILY_UPSELL);    
        titleView.setText(title);   
        
        ImageView splitlineImageView=(ImageView)galleryParentView.findViewById(R.id.screensplitline);
        
        int key = (Integer) NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        
        AssetsImageDrawable imageDrawable = new AssetsImageDrawable(NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());        
        splitlineImageView.setBackgroundDrawable(imageDrawable);
        
        int index = model.getInt(KEY_I_LEARN_ITEM_INDEX);
        
        initGallery(galleryParentView, index);
        
        Button upgradeBtn=(Button)galleryParentView.findViewById(R.id.upgrade_now);
        setNativeButtonBg(upgradeBtn);
        String upgradeNowStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.RES_UPGRADE_NOW, IStringUpSell.FAMILY_UPSELL);       
        upgradeBtn.setText(upgradeNowStr);
        upgradeBtn.setTextColor(BTN_TOUCH_DOWN_COLOR);
        AndroidCitizenUiHelper.setOnClickCommand(this, upgradeBtn, CMD_UPGRADE);
        return screen;
    }

    protected TnScreen createUpgradeScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);

        View mainView =  AndroidCitizenUiHelper.addContentView(screen, R.layout.upsell_upgrade);
        
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        mainView.setBackgroundDrawable(bgImage);
        
        TextView titleView=(TextView)mainView.findViewById(R.id.commonTitle0TextView);
        String title = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.RES_UPGRADE_SCOUT, IStringUpSell.FAMILY_UPSELL);    
        titleView.setText(title);   
        
        ImageView splitlineImageView=(ImageView)mainView.findViewById(R.id.screensplitline);
        
        int key = (Integer) NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getNinePatchImageCache().remove(PrimitiveTypeCache.valueOf(key - 1));
        
        AssetsImageDrawable imageDrawable = new AssetsImageDrawable(NinePatchImageDecorator.SCREEN_SPLITE_LINE_UNFOCUSED.getImage());        
        splitlineImageView.setBackgroundDrawable(imageDrawable);
        
        TextView description = (TextView)mainView.findViewById(R.id.upgrade_description);
        
        UpsellOption upsellOption = ((UpsellOption) model.get(KEY_O_UPSELL_OPTIONS_SELECT));
        String content = "";
        if(upsellOption != null)
        {
            OfferTerm offerTerm = upsellOption.getOfferTerm();
            if(offerTerm != null)
            {
                int payPeriod = offerTerm.getPayPeriod();
                int payPeriodUnit = offerTerm.getPayPeriodUnit();
                int promoPeriod = offerTerm.getPromoPeriod();
                
                if(payPeriod == 1 && payPeriodUnit == OfferTerm.PERIOD_YEAR_UNIT) //means 1 year
                {
                    content = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.PURCHASECONFIRM_COMMENT_YEAR, IStringUpSell.FAMILY_UPSELL);
                    
                }
                else if(promoPeriod > 0) //means hasFree
                {
                    
                    content = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.PURCHASECONFIRM_COMMENT_MONTHFREE, IStringUpSell.FAMILY_UPSELL); 
                }
                else
                {
                    content = ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.PURCHASECONFIRM_COMMENT_MONTH, IStringUpSell.FAMILY_UPSELL);
                }
            }
        }
        description.setText(Html.fromHtml(content));
        
        Button upgradeBtn=(Button)mainView.findViewById(R.id.accept_button);
        setUpgradeButtonBg(upgradeBtn);
        upgradeBtn.setText(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_ACCEPT,
            IStringCommon.FAMILY_COMMON));
        upgradeBtn.setTextColor(BTN_TOUCH_DOWN_COLOR);
        AndroidCitizenUiHelper.setOnClickCommand(this, upgradeBtn, CMD_UPGRADE);
        
        backBtn=(Button)mainView.findViewById(R.id.back_button);
        setUpgradeButtonBg(backBtn);
        backBtn.setText(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_BACK,
            IStringCommon.FAMILY_COMMON));
        backBtn.setTextColor(BTN_TOUCH_DOWN_COLOR);
        AndroidCitizenUiHelper.setOnClickCommand(this, backBtn, CMD_COMMON_BACK);
        return screen;
    }
    
    protected boolean updateScreen(int state, TnScreen screen)
    {   
        switch(state)
        {
            case STATE_UPGRADE:
            {
                if(backBtn != null)
                {
                    setUpgradeButtonBg(backBtn);
                }
                return true;
            }
        }
        return false;
    }

    protected void activate()
    {
        super.activate();
        MapContainer.getInstance().resume();       
    }

    protected void deactivateDelegate()
    {
        super.deactivateDelegate();
        if(imageAdapter !=null)
            imageAdapter.destroy();
        if(imageIndexer !=null)
            imageIndexer.destroy();
        removeImageCache();   
        TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
    }

    private void removeImageCache()
    {
        String key = (String) ImageDecorator.IMG_FTUE_DOT_ICON_UNFOCUSED.getKey();
        ImageCacheManager.getInstance().getImageCache().remove(key); 
        key = (String) ImageDecorator.IMG_FTUE_DOT_ICON_FOCUSED.getKey();
        ImageCacheManager.getInstance().getImageCache().remove(key);
    }
      
    private void initGallery(View mainView, int position)
    {
        OneStepGallery mGallery = (OneStepGallery) mainView.findViewById(R.id.upsell_learn_gallery);
        imageIndexer = (ImageIndicator) mainView.findViewById(R.id.upsell_learn_imageindexer);
        imageAdapter =new ImageAdapter(AndroidPersistentContext.getInstance().getContext(), true);
        mGallery.setAdapter(imageAdapter);
        mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                imageIndexer.setSelection(position);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        mGallery.setSelection(position);

        int pixel = AndroidCitizenUiHelper.getPixelsByDip(10);
        mGallery.setSpacing(pixel);

        Drawable dotIconUnfocused = new AssetsImageDrawable(ImageDecorator.IMG_FTUE_DOT_ICON_UNFOCUSED);
        Drawable dotIconFocused = new AssetsImageDrawable(ImageDecorator.IMG_FTUE_DOT_ICON_FOCUSED);
        imageIndexer.initView(mGallery.getCount(), dotIconUnfocused, dotIconFocused);
        imageIndexer.setVisibility(View.VISIBLE);
    }
    
    protected static class ImageAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater;
        final static int lineGap = 10;
        boolean hasCarConnectFeature = false;
        int imageGapPixel = 0;
        
        private TnUiArgAdapter[] adapters =
        {
            ImageDecorator.GALERY_NO_ADS,
            ImageDecorator.GALERY_REAL_TIME_TRAFFIC, 
            ImageDecorator.GALERY_SPEED_CAMERA_ALERTS,
            ImageDecorator.GALERY_LANE_ASSIST,
            ImageDecorator.GALERY_ALWAYS_THERE,
            ImageDecorator.GALERY_CAR_CONNECT,
        };

        public ImageAdapter(Context context, boolean hasCarConnectFeature)
        {
            this.mInflater = LayoutInflater.from(context);
            this.hasCarConnectFeature = hasCarConnectFeature;
            
            int imageGapDip = 0;
            if(AppConfigHelper.getMaxDisplaySize() <= 480)
            {
                imageGapDip = 2; //dp
            }
            else if(AppConfigHelper.getMaxDisplaySize() <= 850)
            {
                imageGapDip = 6; //dp
            }
            else
            {
                imageGapDip = 15; //dp
            }
            imageGapPixel = AndroidCitizenUiHelper.getPixelsByDip(imageGapDip);
        }

        public int getCount()
        {
            if(hasCarConnectFeature)
            {
                return adapters.length;
            }
            else
            {
                return adapters.length-1; 
            }
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            GalleryItem item = null;
            if (convertView == null)
			{  
                item = new GalleryItem();  

                convertView = mInflater.inflate(R.layout.upsell_gallery_item, null);  
                item.titleView = (TextView) convertView.findViewById(R.id.title);  
                item.contentView = (TextView) convertView.findViewById(R.id.content); 
                item.imageView = (ImageView) convertView.findViewById(R.id.image); 
                item.appendContentView =  (TextView) convertView.findViewById(R.id.append_content);
                item.topGapView = (View) convertView.findViewById(R.id.top_gap);
                item.bottomGapView = (View) convertView.findViewById(R.id.bottom_gap);
                convertView.setTag(item);  
            }  
            else
			{  
                item = (GalleryItem) convertView.getTag();  
            }  

            String title = ResourceManager.getInstance().getCurrentBundle().getString(LEARN_ITEM_TITLE_IDS[position], IStringTouring.FAMILY_TOURING);
            String content = ResourceManager.getInstance().getCurrentBundle().getString(LEARN_ITEM_DESC_ON_GALLERY_IDS[position], IStringTouring.FAMILY_TOURING);
            item.titleView.setText(title);
            item.contentView.setText(content);
            item.imageView.setImageBitmap((Bitmap)adapters[position].getImage().getNativeImage());
            item.topGapView.setMinimumHeight(imageGapPixel);
            item.bottomGapView.setMinimumHeight(imageGapPixel);
            
            if(position == adapters.length - 1 && hasCarConnectFeature) //car connect
            {
                item.appendContentView.setVisibility(View.VISIBLE);
                
                StringBuffer appendContent = new StringBuffer();
                for(int i=0; i<CAR_CONNECT_DESC.length; i++)
                {
                    appendContent.append(CAR_CONNECT_DESC[i]);
                    appendContent.append("\n");
                }
                
                item.appendContentView.setText(appendContent.toString());
                item.appendContentView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)); 
            }
            else
            {
                item.appendContentView.setVisibility(View.INVISIBLE);
                item.appendContentView.setHeight(0);
                item.appendContentView.setText("");
            }
            

            return convertView;
        }
        
        final static class GalleryItem
        {
             public ImageView imageView;
             public TextView titleView;
             public TextView contentView;
             public TextView appendContentView;
             public View topGapView;
             public View bottomGapView;
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
    
    private static class LinkTouchListener implements View.OnTouchListener
    {
        private int downColor;

        private int upColor;

        private TextView myView;

        public LinkTouchListener(int downColor, int upColor, TextView myView)
        {
            super();
            this.downColor = downColor;
            this.upColor = upColor;
            this.myView = myView;
        }

        public boolean onTouch(View v, MotionEvent event)
        {
            int action = event.getAction();
            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                {
                    myView.setTextColor(downColor);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                {
                    myView.setTextColor(upColor);
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    }
    
    private boolean hasFree()
    {
        boolean hasFree= false;
        Vector mVector=this.model.getVector(KEY_V_UPSELL_OPTIONS);
        int size = mVector.size();
        for(int index=0;index<size;index++)
        {
            UpsellOption upsellOption = (UpsellOption) mVector.get(index);
            OfferTerm offerTerm = upsellOption.getOfferTerm();
            if (offerTerm != null)
            {
                int promotePeriod = offerTerm.getPromoPeriod();
                if (promotePeriod > 0)
                {
                    hasFree = true;
                    break;
                }
            }
        }
        return hasFree;
    }
    
    private boolean isCarConnectFeature()
    {
        boolean isCarConnectFeature = false;
        String featurCode = model.getString(ICommonConstants.KEY_S_FEATURE_CODE);
        isCarConnectFeature = FeaturesManager.FEATURE_CODE_CAR_CONNECT.equalsIgnoreCase(featurCode);
        return isCarConnectFeature;   
    }
    
    private boolean gotoAndroidMarket()
    {
        boolean gotoAmp= false;
        Vector mVector=this.model.getVector(KEY_V_UPSELL_OPTIONS);
        int size = mVector.size();
        for(int index=0;index<size;index++)
        {
            UpsellOption upsellOption = (UpsellOption) mVector.get(index);
            String sourceTypeCode = upsellOption.getSourceTypeCode();
            if(UpsellOption.ANDROID_MARKET_BILLING_SOURCE_TYPE.equalsIgnoreCase(sourceTypeCode))
            {
                gotoAmp = true;
                break;
            }
        }
        return gotoAmp;
    }
  
    public void onScreenUiEngineAttached(TnScreen screen, int attached)
    {
        if (screen != null && screen.getId() == this.STATE_UP_SELL)
        {
            if(attached == ITnScreenAttachedListener.BEFORE_ATTACHED)
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
            }
        }
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        return super.handleUiEvent(tnUiEvent);
    }
    
    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null) && model.getState() == STATE_UPGRADE)
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            if(command == CMD_UPGRADE)
            {
                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_UPGRADE,
                    KontagentLogger.UPGRADE_ACCEPT_CLICKED);

            }
        }
    }
}
