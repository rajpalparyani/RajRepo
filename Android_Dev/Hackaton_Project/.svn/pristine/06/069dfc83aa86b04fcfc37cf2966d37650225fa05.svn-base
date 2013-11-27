/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnHorizontalContainer.java
 *
 */
package com.telenav.tnui.widget;

import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.graphics.AbstractTnGraphics;

/**
 * A Layout that arranges its children in a single column or a single row. The default orientation is horizontal.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class TnLinearContainer extends AbstractTnContainer
{
    /**
     * <b>Call Native Method</b>
     * <br />
     * Change whether this view is one of the set of scrollable containers in its window. 
     * This will be used to determine whether the window can resize or must pan when a soft input area is open 
     * -- scrollable containers allow the window to use resize mode since the container will appropriately shrink. 
     */
    public final static int METHOD_SCROLLABLE = 50000001;
    
    /**
     * <b>Call Native Method</b>
     * <br />
     * Should the layout be a column or a row.
     */
    public final static int METHOD_ORIENTATION = 50000002;
    /**
     * <b>Call Native Method</b>
     * <br />
     * Set anchor for this container.
     */
    public final static int METHOD_SET_ANCHOR = 50000003;
    
    protected boolean isVertical;
    
    protected int anchor;
    
    /**
     * Construct a LinearContainer.
     * 
     * @param id
     * @param isVertical
     */
    public TnLinearContainer(int id, boolean isVertical)
    {
        this(id, isVertical, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
        initDefaultStyle();
    }
    
    protected void initDefaultStyle()
    {
        //no need to init common style for this component.
    }
    
    /**
     * Construct a LinearContainer with specified anchor
     * @param id
     * @param isVertical
     * @param anchor
     */
    public TnLinearContainer(int id, boolean isVertical, int anchor)
    {
        super(id, true);
        
        this.isVertical = isVertical;
        this.anchor = anchor;
        
        super.bind();
        this.nativeUiComponent.callUiMethod(METHOD_SET_ANCHOR, null);
        
        this.nativeUiComponent.callUiMethod(METHOD_SCROLLABLE, new Object[]{Boolean.TRUE});
        this.nativeUiComponent.callUiMethod(METHOD_ORIENTATION, null);
        
        
    }
    
    /**
     * Should the layout be a column or a row.
     * 
     * @return boolean
     */
    public boolean isVertical()
    {
        return this.isVertical;
    }
    
    public int getAnchor()
    {
        return this.anchor;
    }
}
