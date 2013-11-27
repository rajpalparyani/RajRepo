/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimDialog.java
 *
 */
package com.telenav.tnui.widget.rim;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.FullScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.Border;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.rim.RimUiApplication;
import com.telenav.tnui.core.rim.RimUiEventHandler;
import com.telenav.tnui.core.rim.RimUiHelper;
import com.telenav.tnui.widget.TnPopupContainer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-11
 */
public class RimDialog extends PopupScreen implements INativeUiComponent
{
    protected RimUiApplication uiApplication;
    protected TnPopupContainer popupContainer;
    protected boolean isShown;
    protected FullScreen blackScreen;
    
    /**
     * constructor for AndroidDialog.
     * @param context the context
     * @param popupContainer the outer container.
     */
    public RimDialog(RimUiApplication uiApplication, TnPopupContainer popupContainer)
    {
        super(new VerticalFieldManager(VerticalFieldManager.NO_VERTICAL_SCROLL | VerticalFieldManager.NO_VERTICAL_SCROLLBAR));

        this.uiApplication = uiApplication;
        this.popupContainer = popupContainer;

        this.initBackGroundDrawable();
    }
    
    protected void initBackGroundDrawable()
    {
        this.setBackground(new Background()
        {

            public boolean isTransparent()
            {
                return true;
            }

            public void draw(Graphics g, XYRect rect)
            {
            }
        });
        
        this.setBorder(new Border(new XYEdges(0, 0, 0, 0), Border.STYLE_TRANSPARENT)
        {
            public void paint(Graphics graphics, XYRect rect)
            {
            }
        });
    }
    

    public Object callUiMethod(int eventMethod, Object[] args)
    { 
        switch (eventMethod)
        {
            case TnPopupContainer.METHOD_SHOW:
            {
                isShown = true;
                ((RimUiHelper)RimUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        show();
                    }
                });
                break;
            }
            case TnPopupContainer.METHOD_HIDE:
            {
                if(!this.isShown())
                    return null;
                
                isShown = false;
                ((RimUiHelper)RimUiHelper.getInstance()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        hide();
                    }
                });
                break;
            }
            case TnPopupContainer.METHOD_SET_CONTENT:
            {
                synchronized (UiApplication.getEventLock())
                {
                    this.deleteAll();
                    this.add((Field) ((AbstractTnComponent) args[0]).getNativeUiComponent());
                }
                break;
            }
        }

        return null;
    }

    public int getNativeHeight()
    {
        return this.popupContainer.getHeight();
    }

    public int getNativeWidth()
    {
        return this.popupContainer.getWidth();
    }

    public int getNativeX()
    {
        return 0;
    }

    public int getNativeY()
    {
        return 0;
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.popupContainer;
    }

    public boolean isNativeFocusable()
    {
        return true;
    }

    public boolean isNativeVisible()
    {
        return this.isShown();
    }

    public boolean requestNativeFocus()
    {
        return true;
    }

    public void requestNativePaint()
    {
        
    }

    public void setNativeFocusable(boolean isFocusable)
    {
           
    }

    public void setNativeVisible(boolean isVisible)
    {
            
    }
    
    protected void show()
    {
        synchronized (UiApplication.getEventLock())
        {
            blackScreen = new FullScreen()
            {
                protected void paintBackground(Graphics graphics)
                {
                    int tempColor = graphics.getColor();
                    int tempAlpha = graphics.getGlobalAlpha();
                    graphics.setColor(Color.BLACK);
                    graphics.setGlobalAlpha(0x90);// alpha value
                    graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                    graphics.setColor(tempColor);
                    graphics.setGlobalAlpha(tempAlpha);
                }
            };
            blackScreen.setBackground(new Background()
            {

                public boolean isTransparent()
                {
                    return true;
                }

                public void draw(Graphics g, XYRect rect)
                {
                }
            });
            UiApplication.getUiApplication().pushScreen(blackScreen);
            UiApplication.getUiApplication().pushScreen(this);
        }
    }
    
    protected void hide()
    {
        synchronized (UiApplication.getEventLock())
        {
            UiApplication.getUiApplication().popScreen(blackScreen);
            UiApplication.getUiApplication().popScreen(this);
        }
    }
    
    protected boolean isShown()
    {
        return isShown;
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

        return this.popupContainer;
    }
}
