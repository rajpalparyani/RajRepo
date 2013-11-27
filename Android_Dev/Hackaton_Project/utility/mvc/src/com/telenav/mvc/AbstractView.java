package com.telenav.mvc;

import java.util.Vector;

import com.telenav.logger.Logger;

/**
 * The abstract view. It should be the super class of all views.
 * 
 * @author zhdong
 * 
 */
public abstract class AbstractView implements IModelListener
{
    /**
     * The data for current model.<br>
     * 
     * Q: Why not use AbstractModel, but it's super class?<br>
     * 
     * A: View can only access data and query model state. View can not access model's business function.<br>
     */
    protected AbstractModelParameter model;
    
    /**
     * All view listeners.
     */
    protected Vector listeners = new Vector();

    protected void activate()
    {
    }

    protected void deactivateDelegate()
    {
    }
    
    /**
     * Check if it is show transient view. This is the default implementation, we assume that if it is in transient state, we
     * should show transient view.<br>
     * 
     * We should override this method if necessary.<br>
     * 
     * @param state
     * @return true -> yes; false -> no
     */
    private boolean xisShownTransientView(int state)
    {
        boolean result = isShownTransientView(state);
        
        this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_IS_SHOW_TRANSIENT_VIEW, state, Integer.MIN_VALUE, new Boolean(result));
        
        return result;
    }
    
    protected boolean isShownTransientView(int state)
    {
        return isTransientState(state);
    }

    /**
     * Check if the state is transient.
     * 
     * @param state the state.
     * @return true if the state is transient, otherwise is false.
     */
    public static final boolean isTransientState(int state)
    {
        return (IStateConstants.MASK_STATE_TRANSIENT & state) == IStateConstants.MASK_STATE_TRANSIENT;
    }
    
    /**
     * Set model for view.
     * 
     * @param model
     */
    void setModel(AbstractModel model)
    {
        this.model = model;
    }

    /**
     * Add view listener
     * 
     * @param listener
     */
    void addListener(IViewListener listener)
    {
        for(int i = 0; i < listeners.size(); i ++)
        {
            if(listeners.elementAt(i).equals(listener))
                return;
        }
        listeners.addElement(listener);
    }

    /**
     * Prepare model data from screen. We should fill AbstractView.model with user input data here.
     * 
     * @param state
     * @param commandId
     * @return true -> success; false -> failed
     */
    private boolean xprepareModelData(int state, int commandId)
    {
        this.addViewMonitor(AbstractMvcMonitor.VIEW_START_PREPARE_MODEL_DATA, state, commandId, null);
        
        boolean result = prepareModelData(state, commandId);
        
        this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_PREPARE_MODEL_DATA, state, commandId, new Boolean(result));
        
        return result;
    }
    
    protected boolean prepareModelData(int state, int commandId)
    {
        return true;
    }
    
    /**
     * Handle UI event which is sent from UI component
     * 
     * @param event
     * 
     * @return true -> success; false -> failed
     */
    protected final boolean handleViewEvent(int commandId)
    {
        this.addViewMonitor(AbstractMvcMonitor.VIEW_START_HANDLE_VIEW_EVENT, Integer.MIN_VALUE, commandId, null);
        
    	int state = model.getState();
        if (commandId == ITriggerConstants.CMD_NONE)
        {
            this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_HANDLE_VIEW_EVENT, Integer.MIN_VALUE, commandId, new Boolean(false));
            
            // this is an invalid UI event, do nothing.
            return false;
        }

        if (!xprepareModelData(state, commandId))
        {
            this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_HANDLE_VIEW_EVENT, Integer.MIN_VALUE, commandId, new Boolean(true));
            
            // there should be something wrong, stop handling
            return true;
        }
        
        boolean isHandled = false;
        for (int i = 0; i < listeners.size(); i++)
        {
            IViewListener listener = (IViewListener) listeners.elementAt(i);
            if (listener.handleCommand(commandId))
            {
                isHandled = true;
            }
        }
        
        finishCommand(commandId, isHandled);
        
        this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_HANDLE_VIEW_EVENT, Integer.MIN_VALUE, commandId, new Boolean(isHandled));
        
        return isHandled;
    }

    /**
     * 
     * After command is handled, you might still want something to be done. We give the opportunity for override.
     * 
     * @param isCommandHandled
     */
    protected void finishCommand(int commandId, boolean isCommandHandled)
    {
        // do nothing        
    }

    private boolean xshowTransientView(int state)
    {
        this.addViewMonitor(AbstractMvcMonitor.VIEW_START_SHOW_TRANSIENT_VIEW, state, Integer.MIN_VALUE, null);
        
        boolean result = showTransientView(state);
        
        this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_SHOW_TRANSIENT_VIEW, state, result? 1: 0, null);
        
        return result;
    }
    
    protected abstract boolean showTransientView(int state);
    
    private boolean xshowView(int state)
    {
        this.addViewMonitor(AbstractMvcMonitor.VIEW_START_SHOW_VIEW, state, Integer.MIN_VALUE, null);
        
        boolean result = showView(state);
        
        this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_SHOW_VIEW, state, result? 1 : 0, null);
        
        return result;
    }
    
    protected abstract boolean showView(int state);
    
    final void xpopAllViews()
    {
        this.addViewMonitor(AbstractMvcMonitor.VIEW_START_POP_ALL_VIEWS, Integer.MIN_VALUE, Integer.MIN_VALUE, null);
        
        popAllViews();
        
        this.addViewMonitor(AbstractMvcMonitor.VIEW_EXIT_POP_ALL_VIEWS, Integer.MIN_VALUE, Integer.MIN_VALUE, null);
    }
    
    protected abstract void popAllViews();

    /**
     * Handle model event. We only handle one kind of event EVENT_MODEL_STATE_CHANGE.<br>
     * 
     * This method can not be override.<br>
     * 
     * @param eventID
     */
    public final boolean handleModelEvent(int eventID)
    {
        if (eventID == ITriggerConstants.EVENT_MODEL_STATE_CHANGE)
        {
            int state = model.getState();

            if (xisShownTransientView(state))
            {
                return xshowTransientView(state);
            }
            else
            {
                return xshowView(state);
            }
        }
        return false;
    }
    
    /**
     * run on ui thread
     * 
     * @param run
     */
    protected void runInUiThread(Runnable run)
    {
        
    }
    
    private void addViewMonitor(String method, int state, int command, Object returnResult)
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
                monitor.watchView(method, this, this.model, state, command, returnResult);
            }
        }
        catch(Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
    }

    final void deactivate()
    {
        deactivateDelegate();
    }
}
