/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MaiTaiViewTouch.java
 *
 */
package com.telenav.sdk.maitai.view;

import com.telenav.app.TeleNavDelegate;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.maitai.impl.MaiTaiParameter;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenWebComponent;

/**
 *@author gbwang
 *@date 2011-8-5
 */
class MaiTaiViewViewTouch extends AbstractCommonView implements IMaiTaiViewConstants
{
    public MaiTaiViewViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }
    
    protected TnScreen createMaiTaiViewScreen(int state)
    {
    	CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        CitizenWebComponent webComponent = UiFactory.getInstance().createCitizenWebComponent(this, 0);
               
        MaiTaiParameter maiTaiParameter = MaiTaiManager.getInstance().getMaiTaiParameter();
        String authority = maiTaiParameter.getAuthority();
        String path = maiTaiParameter.getPath();
        String query = maiTaiParameter.getQuery();
        String url = "http://";
        if(authority != null)
        {
        	url = url + authority;
        }
        if(path != null)
        {
        	url = url + path;
        }
        url = url  + "?";
        if(query != null)
        {
        	url = url + query;
        }
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        url = BrowserSdkModel.addEncodeTnInfo(url, "");
        url = BrowserSdkModel.appendWidthHeightToUrl(url);
        url = url + "&view=" + (orientation == AbstractTnUiHelper.ORIENTATION_LANDSCAPE ? "l" :"p");
        
        webComponent.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler)model);
        webComponent.loadUrl(url);
        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        screen.getContentContainer().add(webComponent);
        return screen;
    	
    }

    protected TnScreen createScreen(int state)
    {
        switch(state)
        {
            case STATE_MAITAI_VIEW:
            {
            	return createMaiTaiViewScreen(state);
            }
        }
        return null;
        
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;
        
        //TODO:
        
        return cmd;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

	protected TnPopupContainer createPopup(int state)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
    public void onScreenUiEngineAttached(final TnScreen screen, int attached)
    {
        if (screen != null && attached == ITnScreenAttachedListener.AFTER_ATTACHED)
        {
            TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        }
    }
    
}
