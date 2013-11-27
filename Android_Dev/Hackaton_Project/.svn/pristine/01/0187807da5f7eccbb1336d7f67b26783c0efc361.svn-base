/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BrowserSdkViewTouch.java
 *
 */
package com.telenav.module.browsersdk;


import com.telenav.data.datatypes.browser.BrowserSessionArgs;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.preference.guidetone.GuideToneManager;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author qli
 *@date 2010-12-29
 */
public class BrowserSdkViewTouch extends AbstractCommonView implements IBrowserSdkConstants
{
	CitizenWebComponent webComponent;
	boolean bottomBarShown;

    public BrowserSdkViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        return cmd;
    }

    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        
        switch(state)
        {
            case STATE_BROWSER_SYNC_PURCHASE_STATUS:
            {
                popup = UiFactory.getInstance().createProgressBox(
                    0, ""
                    
                    //Fix bug http://jira.telenav.com:8080/browse/PAYNET-137
                    /*ResourceManager.getInstance().getCurrentBundle().getString(IStringUpSell.RES_LABEL_SYNC_PURCHASE,
                        IStringUpSell.FAMILY_UPSELL)*/);
                break;
            }
        }
        
        return popup;
    }

    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_BROWSER_MAIN:
            {
                return createBrowserScreen(state);
            }
        }
        return null;
    }
    
    protected BrowserSessionArgs getBrowserSessionArgs()
    {
        BrowserSessionArgs sessionArgs = (BrowserSessionArgs)this.model.get(ICommonConstants.KEY_O_BROWSER_SESSION_ARGS);
        
        return sessionArgs;
    }
    
    protected TnScreen createBrowserScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        webComponent = UiFactory.getInstance().createCitizenWebComponent(this, 0);
        String url = getBrowserSessionArgs().getUrl();
        url = BrowserSdkModel.addEncodeTnInfo(url, "");
        
        //per Zhangpan, we fix the height be the max
        //TODO how to resolve the differences in portrait and landscape 
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        
        webComponent.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler)model);
        webComponent.initDefaultZoomDensity();
        webComponent.setDisableLongClick(false);
        webComponent.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, 
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
        {
             public Object decorate(TnUiArgAdapter args)
             {
                 int height = 0;
                 if(bottomBarShown)
                 {
                     height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight() 
                         - uiDecorator.BOTTOM_BAR_HEIGHT.getInt() + buttonContainer.getComponentById(CMD_COMMON_LINK_TO_AC).getTopPadding();
                 }
                 else
                 {
                     height = AppConfigHelper.getDisplayHeight() - AppConfigHelper.getStatusBarHeight();
                 }
                 return PrimitiveTypeCache.valueOf(height);
             }
        }));
        
        webComponent.loadUrl(url);
        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        screen.getContentContainer().add(webComponent);
        addBottomContainer(screen, getDefaultBottomBarArgs(CMD_COMMON_LINK_TO_EXTRAS));
        
        if(model.getBool(KEY_B_FROM_APPSTORE))
        {
            bottomBarShown = true;
            showBottomContainer();
        }
        else
        {
            bottomBarShown = false;
            hideBottomContainer();
        }
        return screen;
    }
    
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_BROWSER_MAIN:
            {
                if( screen != null && screen instanceof CitizenScreen )
                {
//                    int bottom_bar_height = uiDecorator.BOTTOM_BAR_HEIGHT.getInt();
                    String mode = this.model.getString(KEY_S_WINDOW_MODE);
                    if( "appstore".equalsIgnoreCase(mode) )
                    {
                    	bottomBarShown = true;
                    	showBottomContainer();
                    	webComponent.requestLayout();
                    }
                    else if( "app".equalsIgnoreCase(mode) )
                    {
                    	bottomBarShown = false;
                        hideBottomContainer();
                        webComponent.requestLayout();
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        switch (commandId)
        {
            case CMD_COMMON_BACK:
            {
                if( state == STATE_BROWSER_SYNC_PURCHASE_STATUS )
                {
                    GuideToneManager.getInstance().notifyGuideTone();
//                    CarModelManager.getInstance().cancelLoadCarModel();
                    return true;
                }
                break;
            }
        }
        return super.prepareModelData(state, commandId);
    }
    
    protected boolean isNeedDelayClearWebView()
    {
        return true;
    }

}
