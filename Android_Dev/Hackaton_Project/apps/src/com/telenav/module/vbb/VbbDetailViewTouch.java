/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * VbbDetailViewTouch.java
 *
 */
package com.telenav.module.vbb;

import com.telenav.data.cache.WebViewCacheManager;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.module.browsersdk.BrowserSdkViewTouch;
import com.telenav.module.poi.detail.PoiDetailUiDecorator;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.res.IStringNav;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.tnui.widget.TnWebBrowserField.WebBrowserEventListener;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.frogui.widget.FrogLabel;

/**
 *@author yning
 *@date 2013-1-6
 */
public class VbbDetailViewTouch extends BrowserSdkViewTouch implements IVbbDetailConstants
{

    public VbbDetailViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    @Override
    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch(state)
        {
            case STATE_VBB_DETAIL:
            {
                popup = createAdDetailPopup(state);
                break;
            }
        }
        
        return popup;
    }
    
    protected TnPopupContainer createAdDetailPopup(int state)
    {
        TnPopupContainer container = UiFactory.getInstance().createPopupContainer(state);

        TnLinearContainer wholeContent = UiFactory.getInstance().createLinearContainer(state, true, AbstractTnGraphics.HCENTER);
        
        TnLinearContainer realContent = UiFactory.getInstance().createLinearContainer(state, true, AbstractTnGraphics.HCENTER);

        PoiDetailUiDecorator detailUiDecorator = new PoiDetailUiDecorator();
        
        realContent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, detailUiDecorator.POI_GALLERY_WIDTH);
        realContent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, detailUiDecorator.POI_GALLERY_HEIGHT);
        realContent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        realContent.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        realContent.setPadding(0, 0, 0, 0);
        wholeContent.add(realContent);
        
        AbstractTnContainer tnTitleContainer = UiFactory.getInstance().createLinearContainer(0, false,
            AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        tnTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.POI_DETAIL_TITLE_BG);
        tnTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.POI_DETAIL_TITLE_BG);
        tnTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, detailUiDecorator.POI_GALLERY_WIDTH);
        tnTitleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, detailUiDecorator.POI_GALLERY_TITLE_HEIGHT);
        tnTitleContainer.setPadding(0, 0, 0, 0);
        
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0,
            ResourceManager.getInstance().getCurrentBundle().getString(IStringNav.RES_ADVERTISEMENT, IStringNav.FAMILY_NAV));
        titleLabel.setForegroundColor(TnColor.WHITE, TnColor.WHITE);
        tnTitleContainer.add(titleLabel);
        realContent.add(tnTitleContainer);
        
        final CitizenWebComponent adDetailWebView = WebViewCacheManager.getInstance().getWebView(KEY_ADDETAIL_WEBVIEW, 0, false);
        adDetailWebView.enableAnimation(true);
        adDetailWebView.setWebBrowserEventListener(new WebBrowserEventListener()
        {
            public void onPageFinished(TnWebBrowserField arg0, String arg1)
            {
                WebViewCacheManager.getInstance().add(KEY_ADDETAIL_WEBVIEW, adDetailWebView);
            }
        });
        
        String detailUrl = model.getString(KEY_S_AD_DETAIL_URL);
        adDetailWebView.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler) model);
        adDetailWebView.loadUrl(detailUrl);
        AbstractTnContainer detailContainer = UiFactory.getInstance().createLinearContainer(0, true);
        detailContainer.add(adDetailWebView);
        detailContainer.setPadding(0, 0, 0, 0);
        realContent.add(detailContainer);
        container.setContent(wholeContent);
        return container;
    }
    
    @Override
    protected boolean isShownTransientView(int state)
    {
        if(state == STATE_VBB_DETAIL)
        {
            return true;
        }
        return super.isShownTransientView(state);
    }
}
