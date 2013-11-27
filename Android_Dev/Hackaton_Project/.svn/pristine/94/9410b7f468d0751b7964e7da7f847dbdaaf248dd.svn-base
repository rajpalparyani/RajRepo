package com.telenav.mvc;

import java.util.Vector;

import com.telenav.logger.Logger;

/**
 * The abstract model. It should be the super class of all models.
 * 
 * @author zhdong
 * 
 */
public abstract class AbstractModel extends AbstractModelParameter
{
    /**
     * All model listeners
     */
    Vector listeners = new Vector();
    
    /**
     * A spy here, only for postModelEvent.
     */
    AbstractController controller;
    
    /**
     * Resume the controller
     */
    public void resume()
    {
        addModelMonitor(AbstractMvcMonitor.MODEL_START_RESUME, Integer.MIN_VALUE, null, null);
        
        this.controller.activate();
        
        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_RESUME, Integer.MIN_VALUE, null, null);
    }
    
    /**
     * Add model listener
     * 
     * @param listener
     */
    void addListener(IModelListener listener)
    {
        listeners.addElement(listener);
    }
    
    /**
     * Change model state
     * 
     * @param state
     */
    final void changeState(int state)
    {        
        this.preState = this.state;
        this.state = state;        
    }

    /**
     * Post model event
     * 
     * @param modelEvent
     */
    protected final void postModelEvent(final int modelEvent)
    {
        if (isActivated())
        {
            for (int i = 0; i < listeners.size(); i++)
            {
                final IModelListener listener = (IModelListener) listeners
                        .elementAt(i);
                if(listener instanceof AbstractView)
                {
                    Runnable runnable = new Runnable()
                    {
                        public void run()
                        {
                            try {
                                addModelMonitor(AbstractMvcMonitor.MODEL_START_POST_MODEL_EVENT, modelEvent, null, null);
                                listener.handleModelEvent(modelEvent);
                                addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_POST_MODEL_EVENT, modelEvent, null, null);
                            } catch (Throwable t)
                            {
                                Logger.log(this.getClass().getName(), t);
                            }
                        }
                    };
                    
                    if(MvcContext.getInstance().uiThreadHelper != null)
                    {
                        MvcContext.getInstance().uiThreadHelper.runInUiThread(runnable);
                    }
                    else
                    {
                        ((AbstractView)listener).runInUiThread(runnable);
                    }
                }
                else
                {
                    try {
                        addModelMonitor(AbstractMvcMonitor.MODEL_START_POST_MODEL_EVENT, modelEvent, null, null);
                        listener.handleModelEvent(modelEvent);
                        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_POST_MODEL_EVENT, modelEvent, null, null);
                    } catch (Throwable t)
                    {
                        Logger.log(this.getClass().getName(), t);
                    }
                }
            }
        }        
   }

    /**
     * Release parameters and other resources, such as memory, store, daemon thread etc.
     */
    final void release()
    {
        addModelMonitor(AbstractMvcMonitor.MODEL_START_RELEASE, Integer.MIN_VALUE, null, null);
        
        super.deactivate();
        removeAll();
        listeners.removeAllElements();
        releaseDelegate();
        
        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_RELEASE, Integer.MIN_VALUE, null, null);
    }

    /**
     * Release all model relative resources except parameters.
     */
    protected void releaseDelegate()
    {
        
    }

    
    /**
     * Activate current controller.<br>
     * 
     * 1) Resume daemon back end job.<br>
     * 
     * 2) Notify to update view.<br>
     * 
     * This is a final method. We should override activateDelegate().<br>
     * 
     * @param isUpdateView
     */
    final void activate(boolean isUpdateView)
    {
        addModelMonitor(AbstractMvcMonitor.MODEL_START_ACTIVATE, isUpdateView ? 1: 0, null, null);
        
        activateDelegate(isUpdateView);
        super.activate();
        if (isUpdateView)
        {
            // notify that state changed.
            changeState(state);
            postModelEvent(ITriggerConstants.EVENT_MODEL_STATE_CHANGE);
        }
        
        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_ACTIVATE, isUpdateView ? 1: 0, null, null);
    }
    
    /**
     * Deactivate current model.<br>
     * 
     * 1) Pause unnecessary daemon back end job.<br>
     * 
     * 2) Stop update view.<br>
     * 
     * This is a final method. We should override deactivateDelegate().<br>
     * 
     */
    final void deactivate()
    {        
        addModelMonitor(AbstractMvcMonitor.MODEL_START_DEACTIVATE, Integer.MIN_VALUE, null, null);
        
        super.deactivate();
        deactivateDelegate();
        
        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_DEACTIVATE, Integer.MIN_VALUE, null, null);
    }

    /**
     * Activate current controller.<br>
     * 
     * For example,resume daemon back end job.<br>
     * 
     * @param isUpdateView
     */
    protected void activateDelegate(boolean isUpdateView)
    {
    }

    /**
     * Deactivate current model.<br>
     * 
     * For example, pause unnecessary daemon back end job.<br>
     * 
     */
    protected void deactivateDelegate()
    {
    }
    
    final void xdoAction(int actionId)
    {
        addModelMonitor(AbstractMvcMonitor.MODEL_START_DO_ACTION, actionId, null, null);
        
        doAction(actionId);
        
        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_DO_ACTION, actionId, null, null);
    }
    
    /**
     * Business action according to action id.
     * 
     * @param actionId
     */
    protected abstract void doAction(int actionId);
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     *  Overide fetch method to record model change. <br>
     */
    public synchronized Object fetch(Integer key)
    {
        if(key == null)
            return null;
        
        addModelMonitor(AbstractMvcMonitor.MODEL_START_FETCH, key.intValue(), null, null);
        
        Object value = super.fetch(key);
        
        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_FETCH, key.intValue(), null, value);
        
        return value;
    }
    
    /**
     *  Overide put method to record model change. <br>
     */
    public synchronized void put(Integer key, Object value)
    {
        if(key == null)
            return;
        
        addModelMonitor(AbstractMvcMonitor.MODEL_START_PUT, key.intValue(), value, null);
        
    	super.put(key, value);
    	
    	addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_PUT, key.intValue(), value, null);
    }
    
    /**
     *  Overide get method to record model change. <br>
     */
    public synchronized Object get(Integer key)
    {
        if(key == null)
            return null;
        
        addModelMonitor(AbstractMvcMonitor.MODEL_START_GET, key.intValue(), null, null);
        
        Object value = super.get(key);
        
        addModelMonitor(AbstractMvcMonitor.MODEL_EXIT_GET, key.intValue(), null, value);
        
        return value;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void addModelMonitor(String method, int event, Object arg, Object returnResult)
    {
        int size = MvcContext.getInstance().monitors.size();
        if(size < 0)
        {
            return;
        }
        
        try
        {
            for (int i = 0; i < size; i++)
            {
                AbstractMvcMonitor monitor = (AbstractMvcMonitor) MvcContext.getInstance().monitors.elementAt(i);
                monitor.watchModel(method, this, event, arg, returnResult);
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }
}
