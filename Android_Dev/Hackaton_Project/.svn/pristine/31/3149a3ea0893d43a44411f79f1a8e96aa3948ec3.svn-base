/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * INativeUiContainer.java
 *
 */
package com.telenav.tnui.core;

/**
 * Interface of native ui container.
 * <br />
 * All kinds of native container need implement this interface. We suggest that don't change this class anymore.
 * If for some special ui container need some special native ui method, you need use {@link INativeUiComponent#callUiMethod(int, Object[])} to implement it.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public interface INativeUiContainer extends INativeUiComponent
{
    /**
     * add a component to this container.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     */
    public void addNativeComponent(AbstractTnComponent uiComponent);
    
    /**
     * insert a component with the special index. 
     * 
     * @param uiComponent {@link AbstractTnComponent}
     * @param index
     */
    public void addNativeComponent(AbstractTnComponent uiComponent, int index);
    
    /**
     * remove a component from this container.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     */
    public void removeNativeComponent(AbstractTnComponent uiComponent);
    
    /**
     * remove a component from this container without re-layout.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     */
    public void removeNativeComponentInLayout(AbstractTnComponent uiComponent);
    
    /**
     * remove the whole components in this container. 
     */
    public void removeAllNativeComponents();
    
    /**
     * remove the components at the special index.
     * 
     * @param index
     */
    public void removeNativeComponent(int index);
    
    /**
     * retrieve the component at the special index.
     * 
     * @param index
     * @return {@link AbstractTnComponent}
     */
    public AbstractTnComponent getNativeComponent(int index);
    
    /**
     * retrieve the index of the component.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     * @return the index of component
     */
    public int indexOfComponent(AbstractTnComponent uiComponent);
    
    /**
     * retrieve children size in the container.
     * 
     * @return children's size.
     */
    public int getChildrenSize();
    
    /**
     * Returns the focused child of this container, if any. The child may have focus or contain focus.
     * 
     * @return the focused child.
     */
    public AbstractTnComponent getFocusedComponent();
}
