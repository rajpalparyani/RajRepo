/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimScreen.java
 *
 */
package com.telenav.tnui.core.rim;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.StandardTitleBar;
import net.rim.device.api.ui.component.TitleBar;
import net.rim.device.api.ui.container.FullScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-10
 */
public class RimScreen extends FullScreen
{
    protected RimUiApplication uiApplication;
    
    public RimScreen(RimUiApplication uiApplication)
    {
        this(uiApplication, new VerticalFieldManager(VerticalFieldManager.NO_VERTICAL_SCROLL | VerticalFieldManager.NO_VERTICAL_SCROLLBAR));
    }
    
    public RimScreen(RimUiApplication uiApplication, Manager manager)
    {
        super(manager, FullScreen.DEFAULT_MENU);

        this.uiApplication = uiApplication;
        
        this.setTitleBar(createTitleBar());
    }

    protected TitleBar createTitleBar()
    {
        StandardTitleBar titleBar = new StandardTitleBar();
        titleBar.addClock();

        titleBar.addNotifications();
        titleBar.addSignalIndicator();

        titleBar.setPropertyValue(StandardTitleBar.PROPERTY_BATTERY_VISIBILITY, StandardTitleBar.BATTERY_VISIBLE_ALWAYS);
        titleBar.setPropertyValue(StandardTitleBar.PROPERTY_WIFI_VISIBILITY, StandardTitleBar.PROPERTY_VALUE_ON);
        titleBar.setPropertyValue(StandardTitleBar.PROPERTY_CELLULAR_VISIBILITY, StandardTitleBar.PROPERTY_VALUE_ON);

        return titleBar;
    }
    
    protected void paintBackground(Graphics graphics)
    {
        int tempColor = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        graphics.setColor(tempColor);
    }

    protected boolean keyDown(int keycode, int time)
    {
        boolean isHandled = RimUiEventHandler.onKeyCodeEvent(getLeafComponent(), RimUiEventHandler.KEY_EVENT_DOWN, keycode, time);

        return isHandled ? true : super.keyDown(keycode, time);
    }

    protected boolean keyRepeat(int keycode, int time)
    {
        boolean isHandled = RimUiEventHandler.onKeyCodeEvent(getLeafComponent(), RimUiEventHandler.KEY_EVENT_REPEAT, keycode, time);

        return isHandled ? true : super.keyRepeat(keycode, time);
    }

    protected boolean keyUp(int keycode, int time)
    {
        boolean isHandled = RimUiEventHandler.onKeyCodeEvent(getLeafComponent(), RimUiEventHandler.KEY_EVENT_UP, keycode, time);

        return isHandled ? true : super.keyUp(keycode, time);
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time)
    {
        boolean isHandled = RimUiEventHandler.onTrackballEvent(getLeafComponent(), RimUiEventHandler.TRACKBALL_EVENT_MOVE, dx, dy, status, time);

        return isHandled ? true : super.navigationMovement(dx, dy, status, time);
    }

    protected boolean navigationClick(int status, int time)
    {
        boolean isHandled = RimUiEventHandler.onTrackballEvent(getLeafComponent(), RimUiEventHandler.TRACKBALL_EVENT_CLICK, -1, -1, status, time);

        return isHandled ? true : super.navigationClick(status, time);
    }
    
    protected boolean navigationUnclick(int status, int time)
    {
        boolean isHandled = RimUiEventHandler.onTrackballEvent(getLeafComponent(), RimUiEventHandler.TRACKBALL_EVENT_UNCLICK, -1, -1, status, time);

        return isHandled ? true : super.navigationUnclick(status, time);
    }

    protected boolean touchEvent(TouchEvent message)
    {
        return super.touchEvent(message);
    }

    public AbstractTnComponent getLeafComponent()
    {
        Field leafField = this.getLeafFieldWithFocus();
        while (!(leafField instanceof INativeUiComponent))
        {
            if (leafField == null)
                break;
            leafField = leafField.getManager();
        }

        if (leafField != null)
        {
            INativeUiComponent nativeComponent = (INativeUiComponent) leafField;
            return nativeComponent.getTnUiComponent();
        }

        return this.uiApplication.currentScreen.getRootContainer();
    }
}
