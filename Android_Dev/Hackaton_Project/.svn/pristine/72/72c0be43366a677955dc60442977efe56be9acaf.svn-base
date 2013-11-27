/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnScrollPanel.java
 *
 */
package com.telenav.tnui.widget;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * Layout container for a view hierarchy that can be scrolled by the user, allowing it to be larger than the physical display.
 * A ScrollPanel is a Component, meaning you should place one child in it containing the entire contents to scroll;
 * <br />
 * supports vertical scrolling and horizontal scrolling.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-19
 */
public class TnScrollPanel extends AbstractTnComponent
{
    /**
     * <b>Call Native Method</b>
     * <br />
     * Set the native component.
     */
    public final static int METHOD_SET = 10000001;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * Get the native component.
     */
    public final static int METHOD_GET = 10000002;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * Remove the native component.
     */
    public final static int METHOD_REMOVE = 10000003;
    
    
    /**
     * <b>Scroll to </b>
     * <br />
     * Scroll to 
     */
    public final static int METHOD_SCROLL_TO = 10000004;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * Set scrollBar visible.
     */
    public final static int METHOD_SET_SCROLLBAR_VISIBLE = 10000005;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * scroll to s.p. smoothly.
     */
    public final static int METHOD_SMOOTH_SCROLL_TO = 10000006;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * set the Vertical fading edge enabled.
     */
    public final static int METHOD_SET_VERTICAL_FADING_EDGE_ENABLED = 10000007;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * set the Horizontal fading edge enabled.
     */
    public final static int METHOD_SET_HORIZONTAL_FADING_EDGE_ENABLED = 10000008;
    
    protected boolean isVertical;
    protected TnScrollPanel internalScrollPanel;
    protected boolean isDisableInterceptTouch = false;
    
    /**
     * Construct a scrollPanel.
     * 
     * @param id
     * @param isVertical
     */
    public TnScrollPanel(int id, boolean isVertical)
    {
        super(id, true);
        
        this.isVertical = isVertical;
        
        super.bind();
		
		initDefaultStyle();
    }
    
    /**
     * Place the component which need scroll.
     * 
     * @param tnComponent
     */
    public void set(AbstractTnComponent tnComponent)
    {
        tnComponent.setParent(this);
        
        this.nativeUiComponent.callUiMethod(METHOD_SET, new Object[]{tnComponent});
    }
    
    /**
     * Retrieve the component.
     * 
     * @return {@link AbstractTnComponent}
     */
    public AbstractTnComponent get()
    {
        return (AbstractTnComponent)this.nativeUiComponent.callUiMethod(METHOD_GET, null);
    }
    
    /**
     * Remove all Views.
     */
    public void removeAllViews()
    {
        this.nativeUiComponent.callUiMethod(METHOD_REMOVE, null);
    }
    
    /**
     * Should vertical scrolling or horizontal scrolling.
     * @return isVertical
     */
    public boolean isVertical()
    {
        return this.isVertical;
    }

    /**
     * Pass scroll changed event to this component.
     * 
     * @param l Current horizontal scroll origin.
     * @param t Current vertical scroll origin.
     * @param oldl Previous horizontal scroll origin.
     * @param oldt Previous vertical scroll origin.
     */
    public final void dispatchScrollChanged(int l, int t, int oldl, int oldt)
    {
        onScrollChanged(l, t, oldl, oldt);
    }
    
    /**
     * Scroll changed event.
     * 
     * @param l     Current horizontal scroll origin.
     * @param t     Current vertical scroll origin.
     * @param oldl  Previous horizontal scroll origin.
     * @param oldt  Previous vertical scroll origin. 
     */
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {
        //No need impl here.
    }
    
    protected void initDefaultStyle()
    {
        //no need to init common style for this component.
    }
    
    /**
     * set the verticalFadingEdgeEnabled
     * 
     * @param isEnable whether the vertical edges should be faded when this view is scrolled vertically.
     */
    public void setVerticalFadingEdgeEnabled(boolean isEnable)
    {
        this.nativeUiComponent.callUiMethod(METHOD_SET_VERTICAL_FADING_EDGE_ENABLED, new Object[]
        { new Boolean(isEnable) });
    }

    /**
     * set the horizontal FadingEdgeEnabled
     * 
     * @param isEnable whether the horizontal edges should be faded when this view is scrolled horizontally.
     */
    public void setHorizontalFadingEdgeEnabled(boolean isEnable)
    {
        this.nativeUiComponent.callUiMethod(METHOD_SET_HORIZONTAL_FADING_EDGE_ENABLED, new Object[]
        { new Boolean(isEnable) });
    }
    
    public void setInternalScrollPanel(TnScrollPanel internalScrollPanel)
    {
        this.internalScrollPanel = internalScrollPanel;
    }
    
    public TnScrollPanel getInternalScrollPanel()
    {
        return this.internalScrollPanel;
    }
    
    public boolean isDisableInterceptTouch()
    {
        return isDisableInterceptTouch;
    }

    public void setDisableInterceptTouch(boolean isDisableInterceptTouch)
    {
        this.isDisableInterceptTouch = isDisableInterceptTouch;
    }
}
