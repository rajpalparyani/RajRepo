/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AcViewTestCase.java
 *
 */
package com.telenav.module.dashboard;


import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.frogui.widget.FrogButton;

/**
 *@author qli
 *@date 2012-2-1
 */
class DashboardViewTestCase implements IDashboardConstants
{
    protected static final int CMD_TEST_CASE_SELECT_ICON_BASE_ID = 10000;
    protected static final int CMD_TEST_CASE_SELECT_NEARBY_HOME = 20000;
    protected static final int CMD_TEST_CASE_SELECT_NEARBY_WORK = 20001;

    private int weatherIconNumber = 36;
    private String weatherIconFocusedFile = "dashboard_weather_icon_$_focused.png";
    private String weatherIconUnfocusedFile = "dashboard_weather_icon_$_unfocused.png";
    
    
    private DashboardUiDecorator uiDecorator = null;
    private int screenBackgroundColor = UiStyleManager.getInstance().getColor(UiStyleManager.POI_ICON_PANEL_COLOR);

    public DashboardViewTestCase(DashboardUiDecorator uiDecorator)
    {
        this.uiDecorator = uiDecorator;
    }

    
    protected TnScreen createTestCaseScreen(int state)
    {
        CitizenScreen mainScreen = UiFactory.getInstance().createScreen(state);
        mainScreen.getRootContainer().setBackgroundColor(screenBackgroundColor);
        AbstractTnContainer container = mainScreen.getContentContainer();
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((DashboardUiDecorator) uiDecorator).SCREEN_WIDTH);
        
        createCityStreet(container);
        createWeatherInfo(container);
        createHomeWorkTime(container);
        
        
        return mainScreen;
    }
    
    
    protected void createCityStreet(AbstractTnContainer parent)
    {
        CitizenTextField cityText = UiFactory.getInstance().createCitizenTextField("", 200, 0, "Setup City", 0,
            ImageDecorator.IMG_AC_BACKSPACE.getImage());
        cityText.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT);
        int wpad = UiFactory.getInstance().getCharWidth()/2;
        cityText.setPadding(wpad, 5, 5, 5);
        
        CitizenTextField streetText = UiFactory.getInstance().createCitizenTextField("", 200, 0, "Setup Street", 0,
            ImageDecorator.IMG_AC_BACKSPACE.getImage());
        streetText.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT);
        streetText.setPadding(5, 5, 5, 5);
        
        parent.add(cityText);
        parent.add(streetText);
    }
    
    
    protected void createWeatherInfo(AbstractTnContainer parent)
    {
        CitizenTextField tempText = UiFactory.getInstance().createCitizenTextField("", 5, TnTextField.NUMERIC, "Setup Temperature", 0,
            ImageDecorator.IMG_AC_BACKSPACE.getImage());
        tempText.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT);
        int wpad = UiFactory.getInstance().getCharWidth()/2;
        tempText.setPadding(wpad, 5, 5, 5);
        
        TnScrollPanel panel = UiFactory.getInstance().createScrollPanel(0, false);
        AbstractTnContainer container = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        container.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).SCOUT_WEBVIEW_HEIGHT);
        panel.set(container);
        
        createWeatherIcons(container);
        
        parent.add(tempText);
        parent.add(panel);
    }
    
    protected void createWeatherIcons(AbstractTnContainer parent)
    {
        for (int i = 1; i <= weatherIconNumber; i++)
        {
            String weatherIconUnfocusedFileName = weatherIconUnfocusedFile;
            weatherIconUnfocusedFileName = weatherIconUnfocusedFileName.replace("$", String.valueOf(i));
            TnUiArgAdapter uiUnfocused = new TnUiArgAdapter(weatherIconUnfocusedFileName, ImageDecorator.instance);
            
            String weatherIconFocusedFileName = weatherIconFocusedFile;
            weatherIconFocusedFileName = weatherIconFocusedFileName.replace("$", String.valueOf(i));
            TnUiArgAdapter uiFocused = new TnUiArgAdapter(weatherIconFocusedFileName, ImageDecorator.instance);
            
            FrogButton iconButton = new FrogButton(i, null);
            iconButton.setIcon(uiFocused.getImage(), uiUnfocused.getImage(), AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
            TnMenu menu = UiFactory.getInstance().createMenu();
            menu.add("", CMD_TEST_CASE_SELECT_ICON_BASE_ID + i);
            iconButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            
            parent.add(iconButton);
        }
    }
    
    
    protected void createHomeWorkTime(AbstractTnContainer parent)
    {
        CitizenTextField homeText = UiFactory.getInstance().createCitizenTextField("", 6, TnTextField.NUMERIC, "Setup Time to Home", 0,
            ImageDecorator.IMG_AC_BACKSPACE.getImage());
        homeText.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT);
        int wpad = UiFactory.getInstance().getCharWidth()/2;
        homeText.setPadding(wpad, 5, 5, 5);
        
        CitizenTextField workText = UiFactory.getInstance().createCitizenTextField("", 6, TnTextField.NUMERIC, "Setup Time to Work", 0,
            ImageDecorator.IMG_AC_BACKSPACE.getImage());
        workText.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT);
        workText.setPadding(wpad, 5, 5, 5);
        
        AbstractTnContainer container = UiFactory.getInstance().createLinearContainer(0, false, AbstractTnGraphics.LEFT | AbstractTnGraphics.VCENTER);

        FrogButton homeButton = new FrogButton(0, "Nearby Home");
        homeButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT);
        homeButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        homeButton.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_HIGHLIGHT_FOCUSED_COLOR)
            , UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_NORMAL_STRING_COLOR));
        TnMenu homeMenu = UiFactory.getInstance().createMenu();
        homeMenu.add("", CMD_TEST_CASE_SELECT_NEARBY_HOME);
        homeButton.setMenu(homeMenu, AbstractTnComponent.TYPE_CLICK);
        
        FrogButton workButton = new FrogButton(1, "Nearby Work");
        workButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((DashboardUiDecorator) uiDecorator).DASHBOARD_DRIVE_PLACES_LABEL_HEIGHT);
        workButton.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_SCREEN_TITLE));
        workButton.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_HIGHLIGHT_FOCUSED_COLOR)
            , UiStyleManager.getInstance().getColor(UiStyleManager.DASHBOARD_NORMAL_STRING_COLOR));
        TnMenu workMenu = UiFactory.getInstance().createMenu();
        workMenu.add("", CMD_TEST_CASE_SELECT_NEARBY_WORK);
        workButton.setMenu(workMenu, AbstractTnComponent.TYPE_CLICK);
        
        container.add(homeButton);
        container.add(workButton);
        
        parent.add(homeText);
        parent.add(workText);
        parent.add(container);
    }
    
}
