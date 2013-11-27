/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogFloatPopupContainer.java
 *
 */
package com.telenav.ui.frogui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-7-14
 */
public class FrogFloatPopup extends AbstractTnComponent
{
    public final static int METHOD_SET = 10000001;
    
    public final static int METHOD_GET = 10000002;
    
    public final static int METHOD_SET_WDITH = 10000003;
    
    public final static int METHOD_SET_HEIGHT = 10000004;
    
    public final static int METHOD_SHOW = 10000005;
    
    public final static int METHOD_UPDATE = 10000006;
    
    public final static int METHOD_HIDE = 10000007;
    
    /**
     * constructor of FrogFloatPopup
     * @param id
     */
    public FrogFloatPopup(int id)
    {
        super(id);
        initDefaultStyle();
    }

    protected void initDefaultStyle()
    {
        //no need to init common style for this component.
    }
    
    /**
     * set the component inside.
     * @param component {@link AbstractTnComponent}
     */
    public void setContent(AbstractTnComponent component)
    {
        if(component != null)
        {
            nativeUiComponent.callUiMethod(METHOD_SET, new Object[]{component});
        }
    }
    
    /**
     * get the content of the FloatPopup
     */
    public AbstractTnComponent getContent()
    {
        Object object = nativeUiComponent.callUiMethod(METHOD_GET, null);
        if(object instanceof AbstractTnComponent)
            return (AbstractTnComponent)object;
        
        return null;
    }
    
    /**
     * show the popup dialog 
     * @param anchor anchor component of popup.
     * @param xOff x offset of anchor.
     * @param yOff y offset of anchor.
     * @param w width of popup.
     * @param h height of popup.
     * @param isDropDown enable the drop down animation.
     */
    public void showAt(AbstractTnComponent anchor, int xOff , int yOff, int w, int h, boolean isDropDown)
    {
        if(anchor != null)
        {
            nativeUiComponent.callUiMethod(METHOD_SHOW, new Object[]
            { anchor, PrimitiveTypeCache.valueOf(xOff), PrimitiveTypeCache.valueOf(yOff),
                    PrimitiveTypeCache.valueOf(w), PrimitiveTypeCache.valueOf(h),
                    PrimitiveTypeCache.valueOf(isDropDown) });
        }
    }
    
    /**
     * Update the float popup.
     * @param anchor anchor component of popup.
     * @param xOff x offset of anchor.
     * @param yOff y offset of anchor.
     * @param w width of popup.
     * @param h height of popup.
     */
    public void update(AbstractTnComponent anchor, int xOff, int yOff ,int w, int h)
    {
        nativeUiComponent.callUiMethod(METHOD_UPDATE, new Object[]
        { anchor, PrimitiveTypeCache.valueOf(xOff), PrimitiveTypeCache.valueOf(yOff), PrimitiveTypeCache.valueOf(w),
                PrimitiveTypeCache.valueOf(h) });
    }
    
    /**
     * Update the float popup
     * @param anchor anchor component of popup.
     * @param w width of popup.
     * @param h height of popup.
     */
    public void update(AbstractTnComponent anchor, int w, int h)
    {
        nativeUiComponent.callUiMethod(METHOD_UPDATE, new Object[]{anchor, PrimitiveTypeCache.valueOf(w), PrimitiveTypeCache.valueOf(h)});
    }
    
    /**
     * Hide the popup.
     */
    public void hide()
    {
        nativeUiComponent.callUiMethod(METHOD_HIDE, null);
    }

  
    protected void paint(AbstractTnGraphics graphics)
    {
        // TODO Auto-generated method stub
        
    }
}
