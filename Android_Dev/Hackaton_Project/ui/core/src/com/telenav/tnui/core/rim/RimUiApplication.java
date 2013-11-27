/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimUiApplication.java
 *
 */
package com.telenav.tnui.core.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiTimer;

/**
 * Base class for all device applications that provide a user interface. <br/>
 * A UI application maintains a stack of Screen objects. As it pushes screens onto the stack, it draws them on top of
 * any other screens already on the stack. When the application pops a screen off the stack, it redraws the underlying
 * screens as necessary. Only the screen on the top of the stack receives input events. <br/>
 * Each screen may appear only once in the display stack. The application throws a runtime exception if you attempt to
 * push a single screen onto the stack more than once.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-8
 */
public class RimUiApplication extends UiApplication
{
    /**
     * The top screen of this activity.
     */
    protected TnScreen currentScreen;

    /**
     * @see UiApplication#activate()
     */
    public void activate()
    {
        super.activate();

        TnUiTimer.getInstance().enable(true);
    }

    /**
     * @see UiApplication#deactivate()
     */
    public void deactivate()
    {
        super.deactivate();

        TnUiTimer.getInstance().enable(false);
    }
    
    /**
     * Create the Rim Screen object. Application layer can override this method and provide a new implementation of
     * RimScreen.
     * 
     * @return a RimScreen object
     */
    protected RimScreen newRimScreen()
    {
        return new RimScreen(this);
    }

    /**
     * Set the screen content to an explicit screen. This screen is placed directly into the view hierarchy. It can
     * itself be a complex view hierarchy.
     * 
     * @param screen
     */
    public final void showScreen(TnScreen screen)
    {
        if (screen == null)
            return;

        synchronized (UiApplication.getEventLock())
        {
            Field nativeRootField = (Field) screen.getRootContainer().getNativeUiComponent();

            if (screen.equals(currentScreen))
            {
                currentScreen.getRootContainer().requestPaint();
            }
            else
            {
                currentScreen = screen;

                RimScreen nativeScreen = (RimScreen)nativeRootField.getScreen();
                if(nativeScreen == null)
                {
                    nativeScreen = newRimScreen();
                }
                if (nativeScreen.getTitleBar() != null)
                {
                    ((RimUiHelper)RimUiHelper.getInstance()).statusBarHeight = nativeScreen.getTitleBar().getPreferredHeight();
                }
                
                nativeScreen.add(nativeRootField);
                
                boolean suspendOutside = this.isPaintingSuspended();
                if(!suspendOutside)
                {
                    this.suspendPainting(true);
                }
                Screen activeScreen = this.getActiveScreen();
                if (activeScreen != null)
                {
                    this.popScreen(activeScreen);
                }
                this.pushScreen(nativeScreen);
                if(!suspendOutside)
                {
                    this.suspendPainting(false);
                }
                this.repaint();
            }
        }
    }
}
