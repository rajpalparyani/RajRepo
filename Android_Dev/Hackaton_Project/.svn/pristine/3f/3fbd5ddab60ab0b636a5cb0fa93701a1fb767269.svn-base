/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * INativeUiComponent.java
 *
 */
package com.telenav.tnui.core;

/**
 * Interface of native ui component.
 * <br />
 * All kinds of native component need implement this interface. We suggest that don't change this class anymore.
 * If for some special ui component need some special native ui method, you need use {@link #callUiMethod(int, Object[])} to implement it.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public interface INativeUiComponent
{
    /**
     * Invalidate the whole component. If the component is visible, native paint method will be 
     * called at some point in the future.
     */
    public void requestNativePaint();
    
    /**
     * Set the enabled state of this component.
     * 
     * @param isVisible
     */
    public void setNativeVisible(boolean isVisible);
    
    /**
     * Retrieves the enabled state of this component.
     * 
     * @return boolean
     */
    public boolean isNativeVisible();
    
    /**
     * Set whether this view can receive the focus.
     * 
     * @param isFocusable
     */
    public void setNativeFocusable(boolean isFocusable);
    
    /**
     * Retrieves whether this view can receive the focus.
     * 
     * @return boolean
     */
    public boolean isNativeFocusable();
    
    /**
     * Call this to try to give focus to a specific component or to one of its descendants. 
     * A component will not actually take focus if it is not focusable {@link #isFocusable()}.
     * 
     * @return boolean Whether this view or one of its descendants actually took focus.
     */
    public boolean requestNativeFocus();
    
    /**
     * Retrieves this component's width.
     * 
     * @return int
     */
    public int getNativeWidth();
    
    /**
     * Retrieves this component's height.
     * 
     * @return int
     */
    public int getNativeHeight();
    
    /**
     * Retrieves this component's left-offset position.
     * 
     * @return int
     */
    public int getNativeX();
    
    /**
     * this component's top-offset position.
     * @return Y offset
     */
    public int getNativeY();
    
    /**
     * call native ui component's method.
     * 
     * @param eventMethod - the method name of native component.
     * @param args - the arguments of the method.
     * @return return value of the method.
     */
    public Object callUiMethod(int eventMethod, Object[] args);
    
    /**
     * Retrieves the tn ui component.
     * 
     * @return {@link AbstractTnComponent}
     */
    public AbstractTnComponent getTnUiComponent();
}
