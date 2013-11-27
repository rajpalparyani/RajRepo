/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimFrogFloatPopup.java
 *
 */
package com.telenav.ui.frogui.widget.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.rim.RimUiHelper;
import com.telenav.tnui.widget.TnPopupContainer;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-11
 */
public class RimFrogFloatPopup extends PopupScreen implements INativeUiComponent
{
    protected AbstractTnComponent popupContainer;
    protected boolean isShown;
    
    /**
     * constructor for AndroidDialog.
     * @param context the context
     * @param popupContainer the outer container.
     */
    public RimFrogFloatPopup(AbstractTnComponent popupContainer)
    {
        super(new VerticalFieldManager(VerticalFieldManager.NO_VERTICAL_SCROLL | VerticalFieldManager.NO_VERTICAL_SCROLLBAR));
        
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
                this.getManager().deleteAll();
                this.getManager().add((Field)((AbstractTnComponent)args[0]).getNativeUiComponent());
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
        UiApplication.getUiApplication().pushScreen(this);
    }
    
    protected void hide()
    {
        UiApplication.getUiApplication().popScreen(this);
    }
    
    protected boolean isShown()
    {
        return isShown;
    }
}
