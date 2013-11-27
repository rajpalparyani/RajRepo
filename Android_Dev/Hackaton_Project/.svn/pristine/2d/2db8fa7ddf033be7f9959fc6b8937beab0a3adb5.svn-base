/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 *
 */
package com.telenav.module.about;

import java.util.Vector;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;
import com.telenav.data.cache.WebViewCacheManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.browsersdk.BrowserSdkModel;
import com.telenav.module.mapdownload.MapDownloadStatusManager;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringAbout;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringLogin;
import com.telenav.res.ResourceManager;
import com.telenav.res.converter.StringConverter;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.tnui.widget.TnWebBrowserField.WebBrowserEventListener;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenWebComponent;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogImageComponent;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.ui.frogui.widget.FrogProgressBox;

/**
 *@author dfqin (dfqin@telenav.cn)
 *@date 2011-1-20
 */
class AboutViewTouch extends AbstractCommonView implements IAboutConstants
{

	public AboutViewTouch(AbstractCommonUiDecorator decorator)
	{
		super(decorator);
	}
	
	protected TnPopupContainer createPopup(int state)
    {
		switch(state)
		{
			case STATE_INFO_COLLECTING:
			{
			    String strGetGps = ResourceManager.getInstance().getCurrentBundle().getString(IStringAbout.RES_GETTING_GPS, IStringAbout.FAMILY_ABOUT);
				FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, strGetGps);
				return progressBox;
			}
			case STATE_GETTING_PIN:
			{
			    String strTip = ResourceManager.getInstance().getCurrentBundle().getString(IStringAbout.RES_SENDING_REQ, IStringAbout.FAMILY_ABOUT);
                FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0, strTip);
                return progressBox;
			}
			case STATE_PIN_GOT:
            {
                TnPopupContainer popup = createPinGotMsg(state);
                return popup;
            }
			case STATE_TOOLS_REFRESH:
			{
                String strRefresh = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringAbout.RES_REFRESHING,
                            IStringAbout.FAMILY_ABOUT);
                FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(
                    0, strRefresh);
                return progressBox;
			}
		}
		
		return null;
    }
	
	protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        // TODO Auto-generated method stub
        return false;
    }
	
	protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch (state)
        {
            case STATE_ROOT:
                screen = createRootScreen();
                break;
            case STATE_TOOLS:
                screen = createToolScreen();
                break;
            case STATE_ABOUT_PAGE:
                screen = createAboutPage(state);
                break;
            case STATE_SUPPORT_INFO:
                screen = createSupportInfoScreen(state);
                break;
            case STATE_TOS:
                screen = createNativeTosScreen(state);
                break;
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
            case STATE_TOOLS:
            {
                AbstractTnContainer contentContainer = (AbstractTnContainer) screen
                        .getComponentById(ID_TOOLS_CONTENT_CONTAINER);
                contentContainer.removeAll();
                FrogNullField nullField = UiFactory.getInstance().createNullField(0);
                nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                    ((AboutUiDecorator) uiDecorator).NULL_FIELD_HEIGHT);
                contentContainer.add(nullField);
                Vector vector = model.getVector(KEY_V_DIAGNOSTIC_VECTOR);
                for (int i = 0; i < vector.size(); i++)
                {
                    DiagnosticItem item = (DiagnosticItem) vector.elementAt(i);
                    String strLabel = "<bold>" + item.getName() + "</bold>:  "
                            + item.getValue();
                    FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0,
                        strLabel);
                    multiLine.setForegroundColor(UiStyleManager.getInstance().getColor(
                        UiStyleManager.TEXT_COLOR_ME_GR));
                    multiLine.setFont(UiStyleManager.getInstance().getFont(
                        UiStyleManager.FONT_LABEL));
                    multiLine.setBoldFont(UiStyleManager.getInstance().getFont(
                        UiStyleManager.FONT_LIST_SINGLE));
                    contentContainer.add(multiLine);
                    if (item.getDetailLines() != null)
                    {
                        String strGpsDetail = item.getDetailLines();
                        FrogMultiLine multiLineDetail = UiFactory.getInstance()
                                .createMultiLine(0, strGpsDetail);
                        multiLineDetail.setForegroundColor(UiStyleManager.getInstance()
                                .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                        multiLineDetail.setFont(UiStyleManager.getInstance().getFont(
                            UiStyleManager.FONT_LABEL));
                        contentContainer.add(multiLineDetail);
                    }
                    else
                    {
                        FrogNullField tailNullField = UiFactory.getInstance()
                                .createNullField(0);
                        tailNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                            ((AboutUiDecorator) uiDecorator).NULL_FIELD_HEIGHT);
                        contentContainer.add(tailNullField);
                    }
                }
                
            }
            break;
        }
        return true;
    }
	
	protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }
	
	protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
		return super.preProcessUIEvent(tnUiEvent);
    }
	
    private TnScreen createRootScreen()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_ROOT);
        screen.getRootContainer().setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        AbstractTnContainer contentContainer = screen.getContentContainer();
        AbstractTnContainer titleContainer = screen.getTitleContainer();

        TnLinearContainer subTitleContainer = UiFactory.getInstance()
                .createLinearContainer(0, false,
                    AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        titleContainer.add(subTitleContainer);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_ME_GR));
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((AboutUiDecorator) uiDecorator).TITLE_HEIGHT);

        String strTitle = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringAbout.RES_ABOUT_TITLE_ABOUT, IStringAbout.FAMILY_ABOUT);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, strTitle);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_WH));
        subTitleContainer.add(titleLabel);

        FrogList list = UiFactory.getInstance().createList(0);
        contentContainer.add(list);

        final int listSize = 3; // to-do, hard code
        final String[] strListItem = new String[4];
        strListItem[0] = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringAbout.RES_ABOUT_ABOUT_ATT, IStringAbout.FAMILY_ABOUT);
        strListItem[1] = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringAbout.RES_ABOUT_SUPPORT, IStringAbout.FAMILY_ABOUT);
        //TN-3325 remove PIN
//        strListItem[2] = ResourceManager.getInstance().getCurrentBundle().getString(
//            IStringAbout.RES_ABOUT_PIN_NUMBER, IStringAbout.FAMILY_ABOUT);
        strListItem[2] = ResourceManager.getInstance().getCurrentBundle().getString(
            IStringAbout.RES_ABOUT_DIAGNOSTIC_TOOLS, IStringAbout.FAMILY_ABOUT);

        list.setAdapter(new FrogAdapter()
        {
            public int getCount()
            {
                return listSize;
            }

            public AbstractTnComponent getComponent(int position,
                    AbstractTnComponent convertComponent, AbstractTnComponent parent)
            {
                FrogListItem item = null;

                if (convertComponent == null)
                {
                    item = UiFactory.getInstance().createListItem(position);// to-do

                    TnMenu menu = UiFactory.getInstance().createMenu();
                    switch(position)
                    {
                        case 0:
                            menu.add("", CMD_MENU_ABOUT_ATT);
                            break;
                        case 1:
                            menu.add("", CMD_MENU_SUPPORT_INFO);
                            break;
                      //TN-3325 remove PIN
//                        case 2:
//                            menu.add("", CMD_MENU_PIN_NUM);
//                            break;
                        case 2:
                            menu.add("", CMD_MENU_DIAGNOSTIC);
                            break;
                             
                    }
                    item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                    item.setCommandEventListener(AboutViewTouch.this);
                }
                else
                {
                    item = (FrogListItem) convertComponent;
                }

                item.setText(strListItem[position]);
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_LIST_SINGLE));
                item.setForegroundColor(UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_COLOR_ME_GR));

                return item;
            }

            public int getItemType(int position)
            {
                return 0;
            }
        });

        return screen;
    }
    

    private TnScreen createToolScreen()
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(STATE_TOOLS);
        
        /******************** add screen menu item ***********************************/
        screen.getRootContainer().setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        AbstractTnContainer contentContainer = screen.getContentContainer();
        AbstractTnContainer titleContainer = screen.getTitleContainer();

        TnLinearContainer subTitleContainer = UiFactory.getInstance()
                .createLinearContainer(0, false,
                    AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        titleContainer.add(subTitleContainer);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_ME_GR));
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((AboutUiDecorator) uiDecorator).TITLE_HEIGHT);

        String strTitle = ResourceManager.getInstance().getCurrentBundle().getString(
                    IStringAbout.RES_ABOUT_DIAGNOSTIC_TOOLS, IStringAbout.FAMILY_ABOUT);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, strTitle);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(
            UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance().getColor(
            UiStyleManager.TEXT_COLOR_WH));
        subTitleContainer.add(titleLabel);

        TnLinearContainer subContentContainer = UiFactory.getInstance()
                .createLinearContainer(ID_TOOLS_CONTENT_CONTAINER, true);
        TnScrollPanel scrollPanel = UiFactory.getInstance().createScrollPanel(0, true);
        scrollPanel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((AboutUiDecorator) uiDecorator).PANEL_HEIGHT);
        scrollPanel.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((AboutUiDecorator) uiDecorator).PANEL_WIDTH);
        scrollPanel.set(subContentContainer);
        contentContainer.add(scrollPanel);

        FrogNullField nullField = UiFactory.getInstance().createNullField(0);
        nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((AboutUiDecorator) uiDecorator).NULL_FIELD_HEIGHT);
        subContentContainer.add(nullField);

        Vector vector = model.getVector(KEY_V_DIAGNOSTIC_VECTOR);
        for (int i = 0; i < vector.size(); i++)
        {
            DiagnosticItem item = (DiagnosticItem) vector.elementAt(i);
            String strLabel = "<bold>" + item.getName() + "</bold>:  " + item.getValue();
            FrogMultiLine multiLine = UiFactory.getInstance()
                    .createMultiLine(0, strLabel);
            multiLine.setForegroundColor(UiStyleManager.getInstance().getColor(
                UiStyleManager.TEXT_COLOR_ME_GR));
            multiLine.setFont(UiStyleManager.getInstance().getFont(
                UiStyleManager.FONT_LABEL));
            multiLine.setBoldFont(UiStyleManager.getInstance().getFont(
                UiStyleManager.FONT_LIST_SINGLE));
            subContentContainer.add(multiLine);
            if (item.getDetailLines() != null)
            {
                String strGpsDetail = item.getDetailLines();
                FrogMultiLine multiLineDetail = UiFactory.getInstance().createMultiLine(
                    0, strGpsDetail);
                multiLineDetail.setForegroundColor(UiStyleManager.getInstance().getColor(
                    UiStyleManager.TEXT_COLOR_ME_GR));
                multiLineDetail.setFont(UiStyleManager.getInstance().getFont(
                    UiStyleManager.FONT_LABEL));
                subContentContainer.add(multiLineDetail);
            }
            else
            {
                FrogNullField tailNullField = UiFactory.getInstance().createNullField(0);
                tailNullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
                    ((AboutUiDecorator) uiDecorator).NULL_FIELD_HEIGHT);
                subContentContainer.add(tailNullField);
            }
        }

        return screen;
    }
    
    private TnScreen createAboutPage(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        AbstractTnContainer contentContainer = screen.getContentContainer();
        AbstractTnContainer titleContainer = screen.getTitleContainer();

        String strTitle = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringAbout.RES_ABOUT_TITLE_ABOUT, IStringAbout.FAMILY_ABOUT);
        FrogLabel titleLabel = UiFactory.getInstance().createLabel(0, strTitle);
        titleLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        titleLabel.setStyle(AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        titleLabel.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager
                .getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
        titleContainer.add(titleLabel);
        titleContainer.setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_ME_GR));
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((AboutUiDecorator) uiDecorator).TITLE_HEIGHT);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        
        TnLinearContainer localContainer = UiFactory.getInstance().createLinearContainer(0, true);
        localContainer.setPadding(10, 20, 0, 30);
        FrogImageComponent scoutLogo = UiFactory.getInstance().createFrogImageComponent(0,
            ImageDecorator.PROFILE_ABOUT_SCOUT_LOGO_UNFOCUSED.getImage());
        scoutLogo.setPadding(0, 0, 0, 10);

        StringBuffer version = new StringBuffer();
        version.append(AppConfigHelper.appVersion);
        version.append(".");
        version.append(AppConfigHelper.buildNumber);

        String versionStr = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringAbout.RES_ABOUT_VERSION, IStringAbout.FAMILY_ABOUT);
        StringConverter converter = ResourceManager.getInstance().getStringConverter();
        versionStr = converter.convert(versionStr, new String[]{ version.toString() });

        FrogLabel versionLabel = UiFactory.getInstance().createLabel(0, versionStr);
        versionLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DASHBOARD_WEATHER_FONT));
        versionLabel.setPadding(0, 0, 0, 0);
        localContainer.add(scoutLogo);
        localContainer.add(versionLabel);
        contentContainer.add(localContainer);
        
        FrogLabel mapDataVersionLabel = UiFactory.getInstance().createLabel(0, getDisplayDataVersion());
        mapDataVersionLabel.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_DASHBOARD_WEATHER_FONT));
        mapDataVersionLabel.setPadding(0, 0, 0, 0);
        localContainer.add(mapDataVersionLabel);

        String url = DaoManager.getInstance().getServerDrivenParamsDao().getCMSAboutUrl();
        if (url != null)
        {
            final CitizenWebComponent aboutWebView = UiFactory.getInstance().createCitizenWebComponent(this, 0, false);
            aboutWebView.setLayerType(CitizenWebComponent.LAYER_TYPE_SOFTWARE);
            aboutWebView.initDefaultZoomDensity();
            aboutWebView.enableAnimation(true);

            url = BrowserSdkModel.addEncodeTnInfo(url, "");

            // TODO how to resolve the differences in portrait and landscape
            url = BrowserSdkModel.appendWidthHeightToUrl(url);

            aboutWebView.loadUrl(url);
            aboutWebView.setHtmlSdkServiceHandler(new BrowserSdkModel());
            aboutWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
            aboutWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.uiDecorator.SCREEN_HEIGHT);
            contentContainer.add(aboutWebView);
        }

        return screen;
    }
    
    private String getDisplayDataVersion()
    {
        String versionStr = MapDownloadStatusManager.getInstance().getMapDataVersion();
        
        String pattern = ResourceManager.getInstance().getCurrentBundle().getString(IStringAbout.RES_ABOUT_MAP_DATA_VERSION, IStringAbout.FAMILY_ABOUT);
        
        String display = ResourceManager.getInstance().getStringConverter().convert(pattern, new String[]{versionStr});
        
        return display;
    }
    
    private TnScreen createSupportInfoScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        screen.getRootContainer().setBackgroundColor(
            UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));

        AbstractTnContainer contentContainer = screen.getContentContainer();
        
        final CitizenWebComponent surpportWebView = WebViewCacheManager.getInstance().getWebView(KEY_SUPPORTINFO_WEBVIEW, 0, true);
        surpportWebView.enableAnimation(true);
        
        surpportWebView.setWebBrowserEventListener(new WebBrowserEventListener()
        {
            public void onPageFinished(com.telenav.tnui.widget.TnWebBrowserField arg0, String arg1)
            {
                WebViewCacheManager.getInstance().add(KEY_SUPPORTINFO_WEBVIEW, surpportWebView);
            };
        });
        
        String url = getSupportUrl();
        surpportWebView.loadUrl(url);
        surpportWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, this.uiDecorator.SCREEN_WIDTH);
        surpportWebView.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, this.uiDecorator.SCREEN_HEIGHT);
        contentContainer.add(surpportWebView);

        return screen;
    }
    
    protected TnScreen createNativeTosScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        View mainView = AndroidCitizenUiHelper.addContentView(screen, R.layout.ftue_tos,AndroidCitizenUiHelper.WRAP_CONTENT_PARAMS);
        Drawable bgImage = new AssetsImageDrawable(NinePatchImageDecorator.FTUE_BG_UNFOCUSED);
        mainView.setBackgroundDrawable(bgImage);
        
        TextView tosTextView = (TextView) mainView.findViewById(R.id.commonTitle0TextView);
        String tosLabel =ResourceManager.getInstance().getCurrentBundle().getString(IStringLogin.RES_LABEL_TOS, IStringLogin.FAMILY_LOGIN);;       
        tosTextView.setText(tosLabel);
          
        CitizenWebComponent tocBrowserField = UiFactory.getInstance().createCitizenWebComponent(this, 0, false);
        tocBrowserField.setLayerType(CitizenWebComponent.LAYER_TYPE_SOFTWARE);
        String url = getTosUrl();
        tocBrowserField.loadUrl(url);
        tocBrowserField.setWebBrowserEventListener(new WebBrowserEventListener()
        {
            public void onPageError(TnWebBrowserField browserField, String errorMsg)
            {
                Logger.log(Logger.WARNING, AboutViewTouch.class.getName(), errorMsg);
            }

            public void onPageFinished(TnWebBrowserField browserField, String url)
            {
            }
        });

        tocBrowserField.setKeyEventListener(this);
        
        View webView = (View)tocBrowserField.getNativeUiComponent();
        LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        ((LinearLayout)mainView).addView(webView, params);
        return screen;
    }
    
    private String getTosUrl()
    {
        // scout_us<-->http://m.telenav.com/7.x/7.2.0/scout_us/en_US/legal.html
        String tosUrl = ((DaoManager)DaoManager.getInstance()).getServerDrivenParamsDao().getValue(ServerDrivenParamsDao.TOS_URL);
        if(tosUrl == null || tosUrl.length() == 0)
        {
            tosUrl = DEFAULT_TOS_URL;
        }
        return tosUrl;
    }
    
    private TnPopupContainer createPinGotMsg(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
             IStringCommon.FAMILY_COMMON), CMD_COMMON_OK);
        
        String strMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringAbout.RES_SEND_PIN, IStringAbout.FAMILY_ABOUT);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, strMessage, menu);
        return messageBox;
    }
    
    private String getSupportUrl()
    {
        // scout_us<-->http://m.telenav.com/7.x/7.2.0/scout_us/en_US/support.html
        String supportUrl = ((DaoManager) DaoManager.getInstance()).getServerDrivenParamsDao().getValue(
            ServerDrivenParamsDao.SUPPORT_URL);
        if (supportUrl == null || supportUrl.length() == 0)
        {
            supportUrl = DEFAULT_SUPPORT_URL;
        }
        return supportUrl;
    }
    
}
