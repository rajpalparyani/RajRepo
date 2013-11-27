/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * PoiDetailViewTouch.java
 *
 */
package com.telenav.module.poi.detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.telenav.app.CommManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.cache.WebViewCacheManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.helper.PoiMisLogHelper;
import com.telenav.logger.Logger;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.browsersdk.BrowserSdkViewTouch;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.module.poi.PoiDataWrapper;
import com.telenav.module.poi.localevent.ILocalEventConstants;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.citizen.CitizenGallery;
import com.telenav.ui.citizen.CitizenGallery.ICitizenGalleryAdapter;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-24
 */
class PoiDetailViewTouch extends BrowserSdkViewTouch implements IPoiDetailConstants
{
    private CitizenGallery gallery;
    String rawUserAgent = null;
    
    public PoiDetailViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }
    
    protected boolean isShownTransientView(int state)
    {
        return super.isShownTransientView(state);
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = super.createPopup(state);
        return popup;
    }
    
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return super.updatePopup(state, popup);
    }

    private void clearHistoryCache(CitizenGallery gallery, int selectedIndex)
    {
        if (gallery != null)
        {
            CitizenGallery galleryContainer = gallery;
            CitizenWebComponent currentWebComponent = null;

            int gallerySize = galleryContainer.getGallerySize();
            for (int i = gallerySize - 1; i >= 0; i--)
            {
                AbstractTnComponent component = galleryContainer.getGallery(i);
                if (component instanceof AbstractTnContainer)
                {
                    AbstractTnComponent webComponent = ((AbstractTnContainer) component)
                            .getComponentById(ID_WEB_VIEW_COMPONENT);
                    if (webComponent instanceof CitizenWebComponent)
                    {
                        if (((CitizenWebComponent) webComponent).getUrl() != null
                                && ((CitizenWebComponent) webComponent).getUrl().length() > 0)
                        {
                            String value = ((CitizenWebComponent) webComponent)
                                    .getDataFromTempCache("poikey");
                            if (value != null && value.trim().length() > 0)
                            {
                                int poiStrIndex = -1;
                                try
                                {
                                    poiStrIndex = Integer.parseInt(value);
                                }
                                catch (NumberFormatException e)
                                {

                                }
                                if (poiStrIndex >= 0)
                                {

                                    if (poiStrIndex == selectedIndex)
                                    {
                                        currentWebComponent = (CitizenWebComponent) webComponent;
                                        break;
                                    }

                                }
                            }
                        }
                    }
                }
            }
            if (currentWebComponent != null)
            {
                currentWebComponent.reset();
            }
        }
        
    }
    
    protected TnScreen createScreen(int state)
    {
        switch(state)
        {
            case STATE_POI_DETAIL_FEEDBACK :
            {
                return createFeedbackPoiSearch(state);
            }
            case STATE_POI_DETAIL:
            {
                return createPoiDetail(state);
            }

        }
        return super.createScreen(state);
    }
    
    protected TnScreen createFeedbackPoiSearch(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        screen.getRootContainer().setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        AbstractTnContainer contentContainer = screen.getContentContainer();        
        contentContainer.add(createFeedbackComponent());
        TeleNavDelegate.getInstance().closeVirtualKeyBoard(gallery);
        return screen;
    }

    private CitizenWebComponent createFeedbackComponent()
    {
        CitizenWebComponent poiSearchFeedbackWebView = UiFactory.getInstance().createCitizenWebComponent(this, ID_WEB_VIEW_FEEDBACK_COMPONENT);
        BrowserSessionArgs session = new BrowserSessionArgs(CommManager.POI_DETAIL_FEEDBACK_DOMAIN_ALIAS);
        String url = session.getUrl();
   
        poiSearchFeedbackWebView.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler) model);
        url = BrowserSdkModel.addEncodeTnInfo(url, this.getRegion());
        
        url = BrowserSdkModel.appendWidthHeightToUrl(url);      
        
        poiSearchFeedbackWebView.loadUrl(url);
        poiSearchFeedbackWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        poiSearchFeedbackWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.uiDecorator.SCREEN_HEIGHT);
        return poiSearchFeedbackWebView;
    }
    
    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch(state)
        {
            case STATE_POI_DETAIL_FEEDBACK :
            {
                return updateFeedbackPoiSearch(screen);
            }
            case STATE_POI_DETAIL:
            {
                if (this.model.fetchBool(KEY_IS_NEED_CLEAR_HISTORY))
                {
                    clearHistoryCache(gallery, this.model.fetchInt(KEY_POI_INDEX));
                    return true;
                }
                ((PoiDetailPage) gallery.getCurrentGallery()).setFeedBackBtnEnable(!this.model.getBool(KEY_B_IS_POPUP_SHOW));
                
                if (this.model.fetchBool(KEY_IS_NEED_RELOAD_GALLERY))
                {
                    reloadGallery((CitizenScreen) screen);
                }
                return true;
            }

        }
        return super.updateScreen(state, screen);
    }

    private boolean updateFeedbackPoiSearch(TnScreen screen)
    {
        AbstractTnContainer contentContainer = ((CitizenScreen) screen).getContentContainer();       
        CitizenWebComponent feedback = (CitizenWebComponent) contentContainer.getComponentById(ID_WEB_VIEW_FEEDBACK_COMPONENT);
        if (null != feedback)
        {
            contentContainer.remove(feedback);
            contentContainer.add(createFeedbackComponent());
        }
        TeleNavDelegate.getInstance().closeVirtualKeyBoard(gallery);
        return true;
    }

    private TnScreen createPoiDetail(int state)
    {
        model.put(KEY_B_IS_FIRST_TIME_SHOW, true);
        
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        screen.getRootContainer().setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        AbstractTnContainer contentContainer = screen.getContentContainer();
        int selectedIndex = this.model.getInt(KEY_I_POI_SELECTED_INDEX);
        if (selectedIndex < 0)
        {
            selectedIndex = 0;
        }
        
        gallery = UiFactory.getInstance().createCitizenGalleryContainer(0, selectedIndex);
        
        PoiDetailPageAdapter poiDetailAdapter = new PoiDetailPageAdapter();
        PoiMisLogHelper.getInstance().clearOldMisLog(IMisLogConstants.TYPE_POI_DETAILS);
        PoiMisLogHelper.getInstance().storePendingPoiMislog(IMisLogConstants.TYPE_POI_DETAILS, selectedIndex);
        gallery.setAdapter(poiDetailAdapter);
        gallery.setCommandEventListener(this);
        contentContainer.add(gallery);
        return screen;
    }
    
    private void reloadGallery(CitizenScreen screen)
    {
        AbstractTnContainer contentContainer = screen.getContentContainer();
        int selectedIndex = this.model.getInt(KEY_I_POI_SELECTED_INDEX);
        if (selectedIndex < 0)
        {
            selectedIndex = 0;
        }
        if (gallery != null)
        {
            contentContainer.remove(gallery);
        }
        
        gallery = UiFactory.getInstance().createCitizenGalleryContainer(0, selectedIndex);
        
        PoiDetailPageAdapter poiDetailAdapter = new PoiDetailPageAdapter();
        PoiMisLogHelper.getInstance().clearOldMisLog(IMisLogConstants.TYPE_POI_DETAILS);
        PoiMisLogHelper.getInstance().storePendingPoiMislog(IMisLogConstants.TYPE_POI_DETAILS, selectedIndex);
        gallery.setAdapter(poiDetailAdapter);
        gallery.setCommandEventListener(this);
        contentContainer.add(gallery);
    }
    
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        switch(model.getState())
        {
            case STATE_POI_DETAIL:
            {
                if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                        && tnUiEvent.getCommandEvent() != null
                        && (tnUiEvent.getCommandEvent().getCommand() == CitizenGallery.CMD_PAGE_INDEX_DECREASE || tnUiEvent
                                .getCommandEvent().getCommand() == CitizenGallery.CMD_PAGE_INDEX_INCREASE)
                        && tnUiEvent.getComponent() instanceof CitizenGallery)
                {
                    final CitizenGallery gallery = (CitizenGallery) tnUiEvent.getComponent();
                    TeleNavDelegate.getInstance().closeVirtualKeyBoard(gallery);
                    gallery.requestLayout();
                    int index = gallery.getCurrentGalleryIndex();
                    this.model.put(KEY_I_POI_SELECTED_INDEX, index);
                    PoiMisLogHelper.getInstance().clearOldMisLog(IMisLogConstants.TYPE_POI_DETAILS);
                    PoiMisLogHelper.getInstance().storePendingPoiMislog(IMisLogConstants.TYPE_POI_DETAILS, gallery.getCurrentGalleryIndex());
                    if (this.model.getBool(KEY_B_FIRST_FLUSH_SUCCESS))
                    {
                        try
                        {
                            PoiMisLogHelper.getInstance().flushPendingPoiMislog(IMisLogConstants.TYPE_POI_DETAILS, Integer.valueOf(index).intValue());
                        }
                        catch (NumberFormatException e)
                        {
                            Logger.log(this.getClass().getName(), e);
                        }
                    }
                    
                    CitizenWebComponent webComponent = WebViewCacheManager.getInstance().getPoiDetailWebview(index);
                    if(webComponent != null)
                    {
                        webComponent.notifyWebViewShow();
                    }
                    
                    return true;
                }
                else if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && tnUiEvent.getCommandEvent() != null
                        && tnUiEvent.getCommandEvent().getCommand() == CitizenWebComponent.CMD_LOAD_STATUS_CHANGE
                        && tnUiEvent.getComponent() instanceof CitizenWebComponent)
                {
                    if(tnUiEvent.getComponent() != null && tnUiEvent.getComponent() instanceof CitizenWebComponent)
                    {
                        String index = ((CitizenWebComponent)tnUiEvent.getComponent()).getDataFromTempCache("poikey");
                        if(index != null && index.length() > 0)
                        {
                            try
                            {
                                PoiMisLogHelper.getInstance().flushPendingPoiMislog(IMisLogConstants.TYPE_POI_DETAILS, Integer.valueOf(index).intValue());
                                this.model.put(KEY_B_FIRST_FLUSH_SUCCESS, true);
                            }
                            catch (NumberFormatException e)
                            {
                                Logger.log(this.getClass().getName(), e);
                            }
                        }
                    }
                }
                else if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT
                        && tnUiEvent.getCommandEvent() != null
                        && (tnUiEvent.getCommandEvent().getCommand() == CitizenGallery.CMD_PAGE_INDEX_REACH_END)
                        && tnUiEvent.getComponent() instanceof CitizenGallery)
                {
                    tnUiEvent.setCommandEvent(new TnCommandEvent(CMD_MAP_POI_PAGE_NEXT_NETWORK));
                    handleUiEvent(tnUiEvent);
                    return true;
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
                case CMD_POI_DETAIL_FEEDBACK:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS,
                        KontagentLogger.POIINFO_FEEDBACK_CLICKED);
                    break;
                }
            }
        }
//        else if(tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_KEY_EVENT)
//        {
//            if (tnUiEvent.getKeyEvent()!= null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
//                    && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK && this.model.getState() == STATE_POI_DETAIL)
//            {
//                KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_POI_DETAILS, KontagentLogger.POIINFO_BACK_CLICKED);
//            }
//        } 
    }

    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(tnComponent, w, h, oldw, oldh);
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        this.model.put(KEY_O_FLIPPING_TIME_STAMP,Long.valueOf(System.currentTimeMillis()));
    }

    class PoiDetailPageAdapter implements ICitizenGalleryAdapter
    {
        private int gallerySize = Integer.MIN_VALUE;

        public AbstractTnComponent getView(int positionIndex)
        {
            Address selectAddr = getAddress(positionIndex);
            if (selectAddr == null)
            {
                return null;
            }
            boolean isNotOneBoxSearch = false;
            String searchContent = null;
            PoiDataWrapper poiDataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
            if(poiDataWrapper != null)
            {
                isNotOneBoxSearch = (poiDataWrapper != null && (poiDataWrapper.getCategoryId() == PoiDataRequester.TYPE_NO_CATEGORY_ID || poiDataWrapper
                        .getCategoryId() > 0));
                searchContent = poiDataWrapper.getShowText();
            }
            /*int normalPoiSize = model.getInt(KEY_I_SHOWN_POI_SIZE);
            if (poiDataWrapper != null && normalPoiSize <= 0)
            {
                normalPoiSize = poiDataWrapper.getNormalAddressSize();
                model.put(KEY_I_SHOWN_POI_SIZE, normalPoiSize);
            }
            int pageNumOnTitle = this.calculateIndexOfAddress(poiDataWrapper, positionIndex, selectAddr);*/
            
            PoiDetailPage container = new PoiDetailPage(positionIndex);            
            container.generateDetailPage(positionIndex, selectAddr, searchContent, !isNotOneBoxSearch);

            return container;
        }

        public int getGallerySize()
        {
            if (gallerySize == Integer.MIN_VALUE)
            {
                gallerySize = 0;
                PoiDataWrapper poiDataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
                if (poiDataWrapper != null)
                {
                    gallerySize = poiDataWrapper.getAddressSize();
                }
            }
            return gallerySize;
        }
        
        private Address getAddress(int index)
        {
            Address addr = null;
            PoiDataWrapper poiDataWrapper = (PoiDataWrapper)model.get(KEY_O_POI_DATA_WRAPPER);
            if (poiDataWrapper != null)
            {
                addr = poiDataWrapper.getAddress(index);
            }

            return addr;
        }
        
        private int calculateIndexOfAddress(PoiDataWrapper poiDataWrapper, int positionIndex, Address address)
        {
            int poiIndexForNormal = -1;
            if (address != null && address.getPoi() != null)
            {
                if (address.getPoi().getType() == Poi.TYPE_SPONSOR_POI)
                {
                    return  poiDataWrapper.getIndexOfNormalList(address);
                }
                else
                {
                    poiIndexForNormal = poiDataWrapper.getIndexOfNormalList(address) + 1;
                }

            }
            
            return poiIndexForNormal;
        }


        
    }
    
    protected void activate()
    {
        // FIXME: adapter the font size of native XML configuration
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        super.activate();
    }

    protected synchronized void deactivateDelegate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        super.deactivateDelegate();
    }
    
    class PoiDetailPage extends TnLinearContainer
    {
        CitizenWebComponent webComponent;
        ImageView feedbackView;
        
        public void setFeedBackBtnEnable(boolean isEnable)
        {
            if(feedbackView != null)
            {
                feedbackView.setEnabled(isEnable);
            }
        }
        
        public PoiDetailPage(int id)
        {
            super(id, true, AbstractTnGraphics.HCENTER);
            
            this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((PoiDetailUiDecorator) uiDecorator).POI_GALLERY_WIDTH);
            this.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((PoiDetailUiDecorator) uiDecorator).POI_GALLERY_HEIGHT);
            this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
            this.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
            this.setPadding(0, 0, 0, 0);

        }
        
        public void generateDetailPage(int index, Address selectAddr, String searchContent, boolean isSearchFromOnebox)
        {
            AbstractTnContainer titleBarContainer = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            
            boolean isEvent = false;
            
            if (selectAddr != null && selectAddr.getEventId() > 0)
            {
                isEvent = true;
            }
                    
            titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.COMMON_TITLE_BUTTON_HEIGHT);
            titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
            View titlebarView = AndroidCitizenUiHelper.addContentView(titleBarContainer, R.layout.common_title);       
            TextView titleTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);

            this.add(titleBarContainer);
            feedbackView = (ImageView)titlebarView.findViewById(R.id.commonTitle0IconButton);
            feedbackView.setVisibility(View.VISIBLE);
            feedbackView.setImageDrawable(new AssetsImageDrawable(ImageDecorator.POI_DETAIL_FEEDBACK_BUTTON_UNFOCUSED));
            AndroidCitizenUiHelper.setOnClickCommand(PoiDetailViewTouch.this, feedbackView,  CMD_POI_DETAIL_FEEDBACK);

            int detailFromType = model.getInt(KEY_I_TYPE_DETAIL_FROM);
            if (detailFromType == TYPE_DETAIL_FROM_RECENT || detailFromType == TYPE_DETAIL_FROM_FAVORITE || detailFromType == TYPE_DETAIL_FROM_CONTACT)
            {
                String titleStr = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_RESULT, IStringPoi.FAMILY_POI);
                titleTextView.setText(titleStr);
            }
            else
            {
                String initString = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringPoi.LABEL_SEARCH, IStringPoi.FAMILY_POI);
                if (!isSearchFromOnebox && searchContent != null && searchContent.trim().length()>0 )
                {
                    titleTextView.setText(searchContent);
                }
                else
                {
                    titleTextView.setText(initString);
                }
                boolean isSponserPoi = selectAddr != null && selectAddr.getPoi() != null
                        && selectAddr.getPoi().getType() == Poi.TYPE_SPONSOR_POI;
                if (isSponserPoi)
                {
                    searchContent = ResourceManager.getInstance().getCurrentBundle()
                            .getString(IStringPoi.TITLE_SPONSORED_ITEM, IStringPoi.FAMILY_POI);
                    titleTextView.setText(searchContent);
                }
            }
            
            // title line
            AbstractTnContainer poiDetailContainer = UiFactory.getInstance().createLinearContainer(0, true);
            
            //FIXME AlbertMa Hack to fix the poiDetail show issue of sponsored poi.
            String id = "0";
            if(selectAddr.getPoi() != null && selectAddr.getPoi().getBizPoi() != null)
            {
                id = selectAddr.getPoi().getBizPoi().getPoiId();
            }
            
            String url = null;
            if (isEvent)
            {
                url = new BrowserSessionArgs(CommManager.LOCAL_EVENTS_MAIN_DOMAIN_ALIAS).getUrl();
                url = composeEventDetailUrl(url, selectAddr.getEventId());
            }
            else
            {
                url = getBrowserSessionArgs().getUrl();
                url = BrowserSdkModel.addEncodeTnInfo(url, "");
                // per Zhangpan, we fix the height be the max
                url = BrowserSdkModel.appendWidthHeightToUrl(url);
            }
            
            if(isEvent)
            {
                webComponent = WebViewCacheManager.getInstance().getWebView("0", ID_WEB_VIEW_COMPONENT);
                setUserAgent(webComponent);
            }
            else
            {
                webComponent = WebViewCacheManager.getInstance().getPoiDetailWebview(index, ID_WEB_VIEW_COMPONENT);
                
                String currentUrl = webComponent.getUrl();
                
                if(!url.equals(currentUrl))
                {
                    WebViewCacheManager.getInstance().clearPoiDetailWebview(index);
                    
                    webComponent = WebViewCacheManager.getInstance().getPoiDetailWebview(index, ID_WEB_VIEW_COMPONENT);
                }
            }

            webComponent.setDisableLongClick(true);
            webComponent.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler) model);
            webComponent.setCommandEventListener(PoiDetailViewTouch.this);
            if (gallery != null)
            {
                webComponent.setGestureListener(gallery);                
            }
            if(id.equals("0") /*|| webComponent.getId() == 0*/)
            {
                webComponent.reset();
            }

            webComponent.enableTempCache(true);
            webComponent.setDataToTempCache("poikey", String.valueOf(index));
            webComponent.loadUrl(url);                     
            
            poiDetailContainer.add(webComponent);

            this.add(poiDetailContainer);
            poiDetailContainer.setPadding(0, 0, 0, 0);
            
            boolean isFirstTimeShow = model.fetchBool(KEY_B_IS_FIRST_TIME_SHOW);
            int selectedIndex = model.getInt(KEY_I_POI_SELECTED_INDEX);
            if (isFirstTimeShow && selectedIndex == index)
            {
                webComponent.notifyWebViewShow();
            }
        }
        
        protected String composeEventDetailUrl(String url, long eventId)
        {
            StringBuilder sb = new StringBuilder(url);

            sb.append("/e-").append(eventId);

            return sb.toString();
        }
        
        protected int getWebLoadStatus()
        {
            if(webComponent != null)
            {
                return webComponent.getStatus();
            }
            else
            {
                return  -1;
            }
        }
    }
    
    @Override
    public void onScreenUiEngineAttached(TnScreen screen, int attached)
    {
        if(screen.getId() == STATE_POI_DETAIL)
        {
            if(attached == AFTER_ATTACHED)
            {
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE,
                    new Object[]
                            { PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(true) });
            }
            else if(attached == DETTACHED)
            {
                TeleNavDelegate.getInstance().callAppNativeFeature(TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, null);
            }
        }
        super.onScreenUiEngineAttached(screen, attached);
    }
    
    protected void setUserAgent(CitizenWebComponent webComponent)
    {
        if (webComponent != null)
        {
            String ua = webComponent.getUserAgent();
            
            if(ua != null && ua.trim().length() > 0)
            {
                int index = ua.indexOf(ILocalEventConstants.USER_AGENT_GOBY_SUFFIX);

                if (index == -1)
                {
                    rawUserAgent = ua;

                    ua += ILocalEventConstants.USER_AGENT_GOBY_SUFFIX;
                    
                    webComponent.setUserAgent(ua);
                    
                    Logger.log(Logger.INFO, this.getClass().getName(), "Goby Page : user agent = " + ua);
                }
            }
        }
    }
    
    protected void revertUserAgent()
    {
        //getWebView will create another webview if it hasn't be cached
        //creating webview must be invoked in UI main thread!!!
        if (WebViewCacheManager.getInstance().containsKey("0") && rawUserAgent != null && rawUserAgent.trim().length() > 0)
        {
            CitizenWebComponent webComponent = null;
            webComponent = WebViewCacheManager.getInstance().getWebView("0", ID_WEB_VIEW_COMPONENT);
            if (webComponent != null)
            {
                webComponent.setUserAgent(rawUserAgent);
            }
        }
    }
    
    @Override
    protected void popAllViews()
    {
        revertUserAgent();
        
        super.popAllViews();
    }
    
}
