/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnAbstractContainer.java
 *
 */
package com.telenav.tnui.core;

import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnScrollPanel;

/**
 * Provides fundamental functionality for all field containers. 
 * <br />
 * <br />
 * Manager objects are used to contain components. The various container subclasses 
 * handle specific kinds of field layout. 
 * <br />
 * This container class itself deals with scrolling. 
 * <br />
 * <br />
 * <b>Implementing your own layout container</b>
 * <br />
 * Mostly we suggest that bind the native container directly such as LinearLayout at Android platform, or VerticalManager at RIM platform.
 * So when you override this class, please make sure your container is suitable for kinds of platform, not only for special platform.
 * <br />
 * If you think that sometimes the native container can't fit your requirement, for this case, we suggest that you can find a similar
 * native container and extends the native container and then bind with your custom container.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-2
 */
public abstract class AbstractTnContainer extends AbstractTnComponent
{
    /**
     * for container's padding.
     */
    public final static int METHOD_SET_PADDING = -10000100;
    
    /**
     *  Constructs a new container instance with special id without lazy bind with native component.
     *  
     * @param id
     */
    public AbstractTnContainer(int id)
    {
        this(id, false);
    }
    
    /**
     * Constructs a new container instance with special id, which will bind to native component lazily.
     * <br />
     * This constructor should not be visible as public.
     * 
     * @param id
     * @param lazyBind
     */
    protected AbstractTnContainer(int id, boolean lazyBind)
    {
        super(id, lazyBind);
    }

    /**
     * add a component to this container.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     */
    public void add(AbstractTnComponent uiComponent)
    {
        uiComponent.setParent(this);
        
        ((INativeUiContainer)this.nativeUiComponent).addNativeComponent(uiComponent);
    }
    
    /**
     * insert a component with the special index. 
     * 
     * @param uiComponent {@link AbstractTnComponent}
     * @param index
     */
    public void add(AbstractTnComponent uiComponent, int index)
    {
        uiComponent.setParent(this);
        
        ((INativeUiContainer)this.nativeUiComponent).addNativeComponent(uiComponent, index);
    }
    
    /**
     * remove a component from this container.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     */
    public void remove(AbstractTnComponent uiComponent)
    {
        ((INativeUiContainer)this.nativeUiComponent).removeNativeComponent(uiComponent);
    }
    
    /**
     * remove a component from this container without re-layout.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     */
    public void removeNativeComponentInLayout(AbstractTnComponent uiComponent)
    {
        ((INativeUiContainer)this.nativeUiComponent).removeNativeComponentInLayout(uiComponent);
    }
    
    /**
     * remove the whole components in this container. 
     */
    public void removeAll()
    {
        ((INativeUiContainer)this.nativeUiComponent).removeAllNativeComponents();
    }
    
    /**
     * remove the components at the special index.
     * 
     * @param index
     */
    public void remove(int index)
    {
        ((INativeUiContainer)this.nativeUiComponent).removeNativeComponent(index);
    }
    
    /**
     * retrieve the component at the special index.
     * 
     * @param index
     * @return {@link AbstractTnComponent}
     */
    public AbstractTnComponent get(int index)
    {
        return ((INativeUiContainer)this.nativeUiComponent).getNativeComponent(index);
    }
    
    /**
     * retrieve the index of the component.
     * 
     * @param uiComponent {@link AbstractTnComponent}
     * @return index
     */
    public int indexOf(AbstractTnComponent uiComponent)
    {
        return ((INativeUiContainer)this.nativeUiComponent).indexOfComponent(uiComponent);
    }
    
    /**
     * get children size in this container
     * 
     * @return childrenSize
     */
    public int getChildrenSize()
    {
        return ((INativeUiContainer)this.nativeUiComponent).getChildrenSize();
    }

    /**
     * Returns the focused child of this container, if any. The child may have focus or contain focus.
     * 
     * @return the focused child.
     */
    public AbstractTnComponent getFocusedChild()
    {
        return ((INativeUiContainer)this.nativeUiComponent).getFocusedComponent();
    }
    
    public void setPadding(int leftPadding, int topPadding, int rightPadding, int bottomPadding)
    {
        boolean isRepaint = false;
        if (this.leftPadding != leftPadding || this.rightPadding != rightPadding
                || this.topPadding != topPadding || this.bottomPadding != bottomPadding)
            isRepaint = true;

        if (leftPadding >= 0)
            this.leftPadding = leftPadding;

        if (rightPadding >= 0)
            this.rightPadding = rightPadding;

        if (topPadding >= 0)
            this.topPadding = topPadding;

        if (bottomPadding >= 0)
            this.bottomPadding = bottomPadding;

        if (isRepaint)
        {
            nativeUiComponent.callUiMethod(METHOD_SET_PADDING, new Integer[]
            { new Integer(leftPadding), new Integer(topPadding),
              new Integer(rightPadding), new Integer(bottomPadding) });
            nativeUiComponent.requestNativePaint();
        }
    }
    
    /**
     * get component by the component's id.
     * 
     * @param id the component's id.
     * 
     * @return the component which id is the same as the special id.
     */
    public AbstractTnComponent getComponentById(int id)
    {
        return searchComponentById(this, id);
    }
    
    /**
     * Paint the body of this component.
     * 
     * @param graphics {@link AbstractTnGraphics}
     */
    protected void paint(AbstractTnGraphics graphics)
    {
    }
    
    private AbstractTnComponent searchComponentById(AbstractTnContainer container, int id)
    {
        if(container == null)
            return null;
        
        for(int i = container.getChildrenSize() - 1; i >= 0; i--)
        {
            AbstractTnComponent component = container.get(i);
            if(component == null)
				continue;

            if(component.getId() == id)
            {
                return component;
            }
            
            if(component instanceof TnScrollPanel)
            {
                TnScrollPanel scrollPanel = (TnScrollPanel)component;
                component = scrollPanel.get();
            }
            
            if(component != null && component.getId() == id)
            {
                return component;
            }
            
            if(component instanceof AbstractTnContainer)
            {
                component = searchComponentById((AbstractTnContainer)component, id);
            }
            
            if(component != null && component.getId() == id)
            {
                return component;
            }
        }
        
        return null;
    }
}
