/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * LocalEventViewTouch.java
 *
 */
package com.telenav.module.poi.localevent;

import android.view.View;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;
import com.telenav.data.cache.WebViewCacheManager;
import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.location.TnLocation;
import com.telenav.logger.Logger;
import com.telenav.module.browsersdk.BrowserSdkViewTouch;
import com.telenav.module.location.LocationProvider;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 *@author yning
 *@date 2013-5-6
 */
public class LocalEventViewTouch extends BrowserSdkViewTouch implements ILocalEventConstants
{
    protected String rawUserAgent = null;
    protected CitizenWebComponent webComponent = null;
    
    public LocalEventViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    @Override
    protected TnScreen createScreen(int state)
    {
        switch(state)
        {
            case STATE_LOCAL_EVENT_MAIN:
            {
                return createLocalEventMainScreen(state);
            }
        }
        return super.createScreen(state);
    }
    
    protected TnScreen createLocalEventMainScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        String eventName = model.getString(KEY_S_LOCAL_EVENT_NAME);
        
        //hardcode for text change
        if(eventName.equalsIgnoreCase("Weekend"))
        {
            eventName = "This Weekend";
        }
        addTitle(screen.getTitleContainer(), eventName);
        
        webComponent = WebViewCacheManager.getInstance().getWebView("localEvent", 0);
//        webComponent = UiFactory.getInstance().createCitizenWebComponent(this, 0);
        String url = getBrowserSessionArgs().getUrl();
        
        String eventId = model.getString(KEY_S_LOCAL_EVENT_ID);
        url = composeLocalEventUrl(eventId, url);
        
        webComponent.setHtmlSdkServiceHandler((IHtmlSdkServiceHandler)model);
        webComponent.initDefaultZoomDensity();
        webComponent.setDisableLongClick(false);
        setUserAgent();
        webComponent.loadUrl(url);
        screen.getContentContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        screen.getContentContainer().add(webComponent);
        return screen;
    }
    
    private void addTitle(AbstractTnContainer titleBarContainer,String titleName)
    {
        titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, uiDecorator.COMMON_TITLE_BUTTON_HEIGHT);
        titleBarContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, uiDecorator.SCREEN_WIDTH);
        View titlebarView = AndroidCitizenUiHelper.addContentView(titleBarContainer, R.layout.common_title);       
        TextView tosTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);
        tosTextView.setText(titleName);
    }
    
    protected void setUserAgent()
    {
        if (webComponent != null)
        {
            String ua = webComponent.getUserAgent();
            
            if(ua != null && ua.trim().length() > 0)
            {
                int index = ua.indexOf(USER_AGENT_GOBY_SUFFIX);

                if (index == -1)
                {
                    rawUserAgent = ua;

                    ua += USER_AGENT_GOBY_SUFFIX;
                    
                    webComponent.setUserAgent(ua);
                    
                    Logger.log(Logger.INFO, this.getClass().getName(), "Goby Page : user agent = " + ua);
                }
            }
        }
    }
    
    protected void revertUserAgent()
    {
        if (webComponent != null && rawUserAgent != null && rawUserAgent.trim().length() > 0)
        {
            webComponent.setUserAgent(rawUserAgent);
        }
    }
    
    protected String composeLocalEventUrl(String eventId, String url)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        TnLocation location = LocationProvider.getInstance().getCurrentLocation(
            LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if (location != null)
        {
            double lat = location.getLatitude() / 100000.0;
            double lon = location.getLongitude() / 100000.0;
            if (EVENT_ID_DATE_THIS_WEEKEND.equals(eventId))
            {
                sb.append("/events--near--").append(lat).append("x").append(lon);
                sb.append("/").append(eventId);
            }
            else
            {
                sb.append("/");
                sb.append(eventId);
                sb.append("--near--");
                sb.append(lat).append("x").append(lon);
            }
        }
        return sb.toString();
    }
    
    @Override
    protected void popAllViews()
    {
        revertUserAgent();
        super.popAllViews();
    }
    
    @Override
    protected void activate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        super.activate();
    }
    
    @Override
    protected void deactivateDelegate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        super.deactivateDelegate();
    }
}
