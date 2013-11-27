/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnPopupContainer.java
 *
 */
package com.telenav.tnui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * Provides fundamental container for popup. 
 * <br />
 * <br />
 * <b>Implementing your own PopupContainer</b>
 * <br />
 * Mostly we suggest that bind the native container directly such as Dialog at Android platform.
 * So when you override this class, please make sure your popup is suitable for kinds of platform, not only for special platform.
 * <br />
 * If you think that sometimes the native Dialog can't fit your requirement, for this case, we suggest that you can find a similar
 * native Dialog and extends the native Dialog and then bind with your custom Dialog.
 * 
 *@author jshjin (jshjin@telenav.cn)
 *@date 2010-6-29
 */
public class TnPopupContainer extends AbstractTnComponent
{   
    /**
     * <b>Call Native Method</b>
     * <br />
     * Show this popup in front of current scree. 
     */
    public final static int METHOD_SHOW = 10000001;
    /**
     * <b>Call Native Method</b>
     * <br />
     * Hide this popup. 
     */
    public final static int METHOD_HIDE = 10000002;
    /**
     * <b>Call Native Method</b>
     * <br />
     * Set the pop up content to the native Dialog. 
     */
    public final static int METHOD_SET_CONTENT = 10000003;
    /**
     * <b>Call Native Method</b>
     * <br />
     * Set the pop up content to the native Dialog. 
     */
    public final static int METHOD_SIZE_CHANGE = 10000004;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * Set the window type of the content container
     */
    public final static int METHOD_SET_WINDOW_TYPE = 10000005;
    
    public final static int WINDOW_TYPE_APPLICATION_PANEL = 10001;
    
    public final static int WINDOW_TYPE_APPLICATION = 10002;
    
    protected AbstractTnContainer contentContainer;
    
    /**
     * Constructs a new PopupContainer instance with special id without lazy bind with native component.
     * 
     * @param id
     */
    public TnPopupContainer(int id)
    {
        this(id, false);
    }

	protected TnPopupContainer(int id, boolean lazyBind)
    {
        super(id, lazyBind);
        initDefaultStyle();
    }
    
    protected void initDefaultStyle()
    {
        //no need to init common style for this component.
    }
    
    /**
     * Set content to current pop up.
     * @param container
     */
    public void setContent(AbstractTnContainer container)
    {
        if(container != null)
        {
            this.contentContainer = container;
            nativeUiComponent.callUiMethod(METHOD_SET_CONTENT, new Object[]{container});
        }
    }
    
    /**
     * get the content of pop up.
     * 
     * @return content container.
     */
    public AbstractTnContainer getContent()
    {
        return this.contentContainer;
    }
    
    protected void prepare()
    {
        
    }
    
    /**
     * Show this Popup in the front of the screen.
     */
    public final void show()
    {
        TnScreen screen = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen();
        if(screen != null)
        {
            screen.setCurrentPopup(this);
        }
        
        prepare();
        
        nativeUiComponent.callUiMethod(METHOD_SHOW, null);
    }
    
    protected void reset()
    {
        
    }
    
    /**
     * Hide this Popup.
     */
    public final void hide()
    {
        nativeUiComponent.callUiMethod(METHOD_HIDE, null);
        
        reset();
        
        TnScreen screen = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getCurrentScreen();
        if(screen != null)
        {
            screen.setCurrentPopup(null);
        }
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        nativeUiComponent.callUiMethod(METHOD_SIZE_CHANGE, null);
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        
    }
    
    public int getHeight()
    {
        if(this.contentContainer != null)
        {
            return contentContainer.getHeight();
        }
        
        return 0;
           
    }
    
    public int getWidth()
    {
        if(this.contentContainer != null)
        {
            return contentContainer.getWidth();
        }
        
        return 0;
    }

    public void setWindowType(int windowType)
    {
        nativeUiComponent.callUiMethod(METHOD_SET_WINDOW_TYPE, new Object[]{new Integer(windowType)});
    }
}
