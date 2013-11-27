/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnScreen.java
 *
 */
package com.telenav.tnui.core;

import java.util.Vector;


/**
 * Base class for all screens. Each native ui engine presents an interface to the user by pushing screens onto its display stack, 
 * and popping them off when interaction with the managed components on that screen is finished. 
 * <br />
 * The screen will own one root container to store kinds of components, and the screen will not store the components directly.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-29
 */
public abstract class TnScreen
{
    protected int screenId;
    protected AbstractTnContainer rootContainer;
    protected Vector attachedListeners;
    protected AbstractTnComponent backgroundComponent;
    protected AbstractTnComponent currentPopup;
    
    /**
     * Construct a screen object.
     * 
     * @param screenId the id of this screen
     */
    public TnScreen(int screenId)
    {
        this.attachedListeners = new Vector();
        
        this.screenId = screenId;
        
        this.rootContainer = createRootContainer();
        
        this.rootContainer.setRoot(this);
    }
    
    /**
     * Retrieve the id of this screen.
     * 
     * @return the id of this screen
     */
    public int getId()
    {
        return this.screenId;
    }
    
    /**
     * Set the id of this screen.
     * 
     * @param id the id of this screen
     */
    public void setId(int id)
    {
        this.screenId = id;
    }
   
    /**
     * Create the root container, which will be override by children of screen.
     * 
     * @return {@link AbstractTnContainer}
     */
    protected abstract AbstractTnContainer createRootContainer();
    
    /**
     * Retrieve the root container of this screen.
     * 
     * @return {@link AbstractTnContainer}
     */
    public final AbstractTnContainer getRootContainer()
    {
        return this.rootContainer;
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
        return this.rootContainer.getComponentById(id);
    }
    
    /**
     * show this screen on the native ui engine.
     */
    public final void show()
    {
        ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).showScreen(this);
    }
    
    /**
     * Registers a ITnScreenAttachedListener event listener for this screen. 
     * 
     * @param listener ITnScreenAttachedListener event listener
     */
    public void addScreenAttachedListener(ITnScreenAttachedListener listener)
    {
        if(listener == null)
            return;
        
        attachedListeners.addElement(listener);
    }
    
    /**
     * Removes a ScreenUiEngineAttached event listener from this screen. 
     * 
     * @param listener ITnScreenAttachedListener event listener
     */
    public void removeScreenAttachedListener(ITnScreenAttachedListener listener)
    {
        if(listener == null)
            return;
        
        attachedListeners.removeElement(listener);
    }
    
    /**
     * Returns true if this screen contains the specified listener.
     * 
     * @param listener
     * @return true if this screen contains the specified listener.
     */
    public boolean containScreenAttachedListener(ITnScreenAttachedListener listener)
    {
        if(listener == null)
            return false;
        
        return attachedListeners.contains(listener);
    }
    
    /**
     * Retrieve the count of the whole screen attached listeners;
     * @return Vector
     */
    public int getScreenAttachedListenerSize()
    {
        return attachedListeners.size();
    }
    
    /**
     * Returns the ITnScreenAttachedListener at the specified index.
     * 
     * @param index an index into listeners.
     * @return the listener at the specified index
     */
    public ITnScreenAttachedListener getScreenAttachedListener(int index)
    {
        return (ITnScreenAttachedListener)attachedListeners.elementAt(index);
    }
    
    /**
     * set current popup Dialog.
     * 
     * @param popupComponent popup dialog.
     */
    public void setCurrentPopup(AbstractTnComponent popupComponent)
    {
        this.currentPopup = popupComponent;
    }
    
    /**
     * Retrieve the current popup Dialog.
     * 
     * @return popup dialog.
     */
    public AbstractTnComponent getCurrentPopup()
    {
        return this.currentPopup;
    }
    
    /**
     * set the background component for this screen. <br/>
     * <b>Please don't use this method if it's not necessary. </b>
     * @param backgroundComponent
     */
    public void setBackgroundComponent(AbstractTnComponent backgroundComponent)
    {
        this.backgroundComponent = backgroundComponent;
        
        this.backgroundComponent.setRoot(this);
    }
    
    /**
     * Retrieve the background component for this screen.
     * 
     * @return AbstractTnComponent component
     */
    public AbstractTnComponent getBackgroundComponent()
    {
        return this.backgroundComponent;
    }
}
