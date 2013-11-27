package com.telenav.mvc;

import java.util.Vector;

import com.telenav.logger.Logger;


/**
 * The abstract controller. It should be the super class of all controllers.
 * 
 * @author zhdong
 * @modifer fqming
 */
public abstract class AbstractController implements IModelListener, IViewListener
{
    /**
     * Super controller.
     */
    protected AbstractController superController;
    
    /**
     * The view for current controller
     */
    protected AbstractView view;
    
    /**
     * The model for current controller
     */
    protected AbstractModel model;
    
    /**
     * The state machine
     */
    protected StateMachine stateMachine;

    private boolean isStopped = false;
    
    private static AbstractController currentController;
    
    /**
     * Get super controller
     * 
     * @return
     */
    public AbstractController getSuperController()
    {
        return superController;
    }
    
    /**
     * The default constructor<br>
     * 
     * @param superController
     */
    public AbstractController(AbstractController superController)
    {
        this.superController = superController;
    }

    /**
     * Create view for current controller
     * 
     * @return view
     */
    protected abstract AbstractView createView();

    /**
     * Create model for current controller
     * 
     * @return model
     */
    protected abstract AbstractModel createModel();
    
    /**
     * Create state machine for current controller
     * 
     * @return state machine
     */
    protected abstract StateMachine createStateMachine();
    
    /**
     * Initialize a controller with given parameter and controllerEvent.
     * 
     * 
     * @param controllerEvent
     * @param parameter
     * 
     * a controller should always start temporarily at STATE_VIRGIN, and then triggered by controllerEvent to another
     * state.
     * 
     * @return true -> success; false -> failed
     */
    public final boolean init(int controllerEvent, Parameter parameter)
    {
        addControllerMonitor(AbstractMvcMonitor.CONTROLLER_START_INIT, controllerEvent, parameter, null);
        
        if (superController != null)
        {
            superController.deactivate();
        }
		if (this.stateMachine == null)
		{
		    this.stateMachine = createStateMachine();
		}
		if (this.model == null) 
		{
			this.model = createModel();
			//this.model.add(parameter);
		}

        if (this.view == null)
            this.view = createView();
        
        if (view != null)
        {
            view.addListener(this);
            view.setModel(model);            
        }

        if (model != null)
        {
            model.setState(stateMachine.getCurrentState());
            model.addListener(this);
            model.controller = this;
            if (view != null)
                model.addListener(view);
        }
        else
        {
            addControllerMonitor(AbstractMvcMonitor.CONTROLLER_EXIT_INIT, controllerEvent, parameter, Boolean.FALSE);
            return false;
        }
        
        this.isStopped = false;
        
        boolean result = handleControllerEvent(controllerEvent, parameter);
        
        addControllerMonitor(AbstractMvcMonitor.CONTROLLER_EXIT_INIT, controllerEvent, parameter, new Boolean(result));
        
        return result;
    }

    /**
     * Activate controller actively.<br>
     * 
     * Q: When will this function be used?<br>
     * 
     * A: If the controller is managed by application layer (NOT inside a controller chain)<br>
     * and we want to switch controllers inside controllers manager.<br>
     * For this case, we just activate one controller w/o change it's state<br>
     * 
     */
    public void activate()
    {
        backToLastStableState();
        activate(true);
    }

    
    /**
     * Activate controller actively.<br>
     * 
     * Q: When will this function be used?<br>
     * 
     * A: If the controller is managed by application layer (NOT inside a controller chain)<br>
     * and we want to switch controllers inside controllers manager.<br>
     * For this case, we will pass in a controllerEvent, which will change this controller's state.<br>
     * 
     * @param controllerEvent
     * @param data
     */
    public void activate(int controllerEvent, Parameter data)
    {
        // copy data to model first
        this.model.add(data);
        
        //fix bug 46691
        //sometime we might activate a dead controller, this controller will have no listeners, we had to register back.
        isStopped = false;
        Vector listeners = this.model.listeners;        
        if (listeners.size() == 0)
        {
            listeners.addElement(this);
            listeners.addElement(view);
        }
        
        // before controller event is handled, activate without update view.
        activate(false);
        // handle controller event
        handleTrigger(controllerEvent);
    }
    
    /**
     * Initialize a controller with default controller event EVENT_CONTROLLER_START
     * 
     * @param parameter
     * @return true -> success; false -> failed
     */
    public final boolean init(Parameter parameter)
    {
        return init(ITriggerConstants.EVENT_CONTROLLER_START, parameter);
    }

    /**
     * Initialize a controller with default controller event EVENT_CONTROLLER_START and without parameter.
     * 
     * @return true -> success; false -> failed
     */
    public final boolean init()
    {
        return init(ITriggerConstants.EVENT_CONTROLLER_START, null);
    }        

    /**
     * Handle command.<br>
     * 
     * Q: When this method will be invoked?<br>
     * 
     * A: Here are the typical steps.<br>
     * 
     * 1) AbstractView accept a UI event.<br>
     * 
     * 2) AbstractView try to handle this UI event. If it is handled, stop.<br>
     * 
     * 3) AbstractView can not handle it. Convert this event to a commandId.<br>
     * 
     * 4) Notify controller to handle this command.<br>
     * 
     * @param commandId
     * @return true -> success; false -> failed
     */
    public final boolean handleCommand(int commandId)
    {
        if (isStopped || !model.isActivated())
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "not activated, " + "handleCommand(" + commandId + "): is ignored. isStopped: " + isStopped);
            }
            
            return false;
        }
        
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "handleCommand(" + commandId + ").");
        }
        return handleTrigger(commandId);
    }

    /**
     * Handle event sent from model.<br>
     * 
     * Q: When this method will be invoked?<br>
     * 
     * A: Here are the typical steps.<br>
     * 
     * 1) Model start an ASYNCHRONISM action such as network request and return immediately. (Showing progress bar).<br>
     * 
     * 2) When network finished, model will post a event such as EVENT_MODEL_NETWORK_OK to notify controller. (Hide
     * progress bar and do action according to this event)<br>
     * 
     * Basically synchronous action does NOT need to post model event!<br>
     * 
     * @param eventID
     */
    public final boolean handleModelEvent(int eventID)
    {
        if (isStopped || !model.isActivated())
        {
            if(Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "not activated, " + "handleModelEvent(" + eventID + "): is ignored.");
            }
            
            return false;
        }

        if (eventID == ITriggerConstants.EVENT_MODEL_STATE_CHANGE)
        {
            return true;
        }
        else if (eventID == ITriggerConstants.EVENT_MODEL_RELEASE_ALL_PREVIOUS_MODULES)
        {
            if (this.superController != null)
            {
                // clean current screens
                if (view != null)
                {
                    view.xpopAllViews();
                }
                
                // release all previous controllers
                AbstractController ctrl = superController;
                while (ctrl != null)
                {
                    ctrl.release();
                    ctrl = ctrl.superController;
                }
                
                this.superController = null;
            }
            return true;
        }
        
        if (isHandleTrigger)
        {
            // asynchronous handle model event!
            // FIXME: in some extreme case, we might lost the event.But it is very very low probability.
            // The only way to solve this problem is to using daemon thread. But it will be much heavier.
            pendingModelEvents.addElement(new Integer(eventID));
            return true;
        }
        else
        {
            return handleTrigger(eventID);
        }
    }
    
    /**
     * Retrieve the current controller in the MVC chain.
     * 
     * @return AbstractController
     */
    public static AbstractController getCurrentController()
    {
        return currentController;
    }

    /**
     * Handle event sent from other controller.<br>
     * 
     * Q: When this method will be invoked?<br>
     * 
     * A: It will be invoked in 2 cases<br>
     * 
     * 1) When we want to start a new controller. If we start a new controller with no parameters, the default event is
     * EVENT_CONTROLLER_START.<br>
     * 
     * 2) When we exit current controller and back to previous controller, we want to pass in some parameters.<br>
     * 
     * Q: If the previous controller can not handle this event, what will happen?<br>
     * 
     * A: It will ask his parent controller to handle it. Meanwhile it release itself.<br>
     * 
     * 
     * @param eventID
     * @param data
     */
    final boolean handleControllerEvent(int eventID, Parameter data)
    {
        if (!stateMachine.isTriggerExist(eventID))
        {
            // current controller can not handle this event
            this.release();

            // ask for parent for help.
            if (superController != null)
            {
                return superController.handleControllerEvent(eventID, data);
            }

            return false;
        }

        // copy data to model
        this.model.add(data);

        // before controller event is handled, activate without update view.
        activate(false);
        boolean result = handleTrigger(eventID);
        return result;
    }
    
    /**
     * If we current handle trigger or not.
     */
    private boolean isHandleTrigger = false;
    
    /**
     * The pending model event id.
     */
    private Vector pendingModelEvents = new Vector();
    
    /**
     * Run an action according to a trigger.<br>
     * 
     * The trigger might be one of:<br>
     * 
     * 1) Command id<br>
     * 
     * 2) Controller event id<br>
     * 
     * 3) Model event id<br>
     * 
     * @param trigger
     * @return
     */
    private final boolean handleTrigger(int trigger)
    {        
        int currentState;
        int actionId;
        int nextState;        
        isHandleTrigger = true;
        
        synchronized (stateMachine) // more robust
        {
            currentState = stateMachine.getCurrentState();
            actionId = stateMachine.fetchNextAction(trigger);
            nextState = stateMachine.getCurrentState();
            model.changeState(nextState);
        }

        if(Logger.DEBUG)
        {
            String action = "";
            if (trigger > ITriggerConstants.EVENT_CONTROLLER_BASE)
            {
                action = "Controller";
            }
            else if (trigger > ITriggerConstants.EVENT_MODEL_BASE)
            {
                action = "Model";
            }
            Logger.log(Logger.INFO, this.getClass().getName(), "handle" + action + "(" + trigger + "). current state: "
                    + StateMachine.toReadable(currentState) + ", next state: " + StateMachine.toReadable(nextState) + ", action: "
                    + actionId);
        }
        
        if (actionId != IActionConstants.ACTION_NONE)
        {
            try
            {
                doAction(actionId);
            }
            catch(Throwable ex)
            {
                Logger.logCriticalError(this.getClass().getName(), ex);
                
                if(Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "handleTrigger(" + trigger + "), doAction(" + actionId + "): activate controller caused by exception.");
                }
                
                this.activate();
                isHandleTrigger = false;
                return false;
            }
        }
        
        addControllerMonitor(AbstractMvcMonitor.CONTROLLER_HANDLE_TRIGGER, currentState, new Object[]
        { new int[]
        { currentState, trigger, nextState, actionId }, superController == null ? "" : superController.getClass().getName(),
                pendingModelEvents, view == null ? "" : view.getClass().getName(), model == null ? "" : model.getClass().getName() }, null);

        if (nextState == IStateConstants.STATE_VIRGIN)
        {
            // Before go back to previous controller, give a chance to post a controller event.
            if (currentState != nextState)
            {
                try
                {
                    xpostStateChange(currentState, nextState);
                }
                catch(Throwable ex)
                {
                    Logger.logCriticalError(this.getClass().getName(), ex);
                    
                    if(Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "handleTrigger(" + trigger + "), postStateChange(" + currentState + "," + nextState + "): activate controller caused by exception.");
                    }
                    
                    this.activate();
                    isHandleTrigger = false;
                    return false;
                }
            }
            handleControllerBack();
            isHandleTrigger = false;
            return true;
        }
        else
        {
            model.postModelEvent(ITriggerConstants.EVENT_MODEL_STATE_CHANGE);
            if (currentState != nextState)
            {
                try
                {
                    xpostStateChange(currentState, nextState);
                }
                catch(Throwable ex)
                {
                    Logger.logCriticalError(this.getClass().getName(), ex);
                    
                    if(Logger.DEBUG)
                    {
                        Logger.log(Logger.INFO, this.getClass().getName(), "handleTrigger(" + trigger + "), postStateChange(" + currentState + "," + nextState + "): activate controller caused by exception.");
                    }
                    
                    this.activate();
                    isHandleTrigger = false;
                    return false;
                }
            }
        }

        if (controllerBackEvent != null)
        {
            handleControllerBack();
        }
        else if (pendingModelEvents.size() > 0 && !isStopped)
        {
            int modelEvent = ((Integer) pendingModelEvents.firstElement()).intValue();
            pendingModelEvents.removeElementAt(0);
            handleTrigger(modelEvent);
        }

        isHandleTrigger = false;
        return true;
    }
    
    private void handleControllerBack()
    {
        // Anyway, stop activate view.
        deactivate();

        // pop all current screens.
        if (view != null)
            view.xpopAllViews();
        
        if (controllerBackEvent == null)
        {
            // Get last stable controller. We will ignore all controller which have no stable states.
            AbstractController superCtrl = getLastStableController();
            if (superCtrl != null)
            {
                superCtrl.activate(true);
                superCtrl.checkState();
            }            
        }
        else
        {
            release();
            
            // We want to pass data to previous controller
            if (superController != null)
            {
                superController.handleControllerEvent(
                    controllerBackEvent.controllerEventId, controllerBackEvent.parameter);
            }
            controllerBackEvent = null;
        }
    }
    
    protected void checkState()
    {
        
    }
    
    /**
     * Get last stable controller.
     * 
     * @return
     */
    private AbstractController getLastStableController()
    {
        AbstractController ctrl = this;
        int state = IStateConstants.STATE_VIRGIN;
        while (ctrl != null && state == IStateConstants.STATE_VIRGIN)
        {
            ctrl.release();
            ctrl = ctrl.superController;
            if (ctrl != null)
                state = ctrl.backToLastStableState();
        }
        return ctrl;
    }
    
    /**
     * Post controller event. The event will not be handled until exit current controller.
     * 
     * @param eventID
     * @param data
     */
    public void postControllerEvent(int eventID, Parameter data)
    {
        controllerBackEvent = new ControllerBackEvent(eventID, data);
    }
    
    /**
     * This function is invoked after state change. We could start a new controller according to state change if
     * necessary.
     * 
     * 
     * @param currentState
     * @param nextState
     * 
     */
    protected void xpostStateChange(int currentState, int nextState)
    {
        addControllerMonitor(AbstractMvcMonitor.CONTROLLER_START_POST_STATE_CHANGE, currentState, new Integer(nextState), null);
        
        postStateChange(currentState, nextState);
        
        addControllerMonitor(AbstractMvcMonitor.CONTROLLER_EXIT_POST_STATE_CHANGE, currentState, new Integer(nextState), null);
    }
    
    protected abstract void postStateChange(int currentState, int nextState);
    
    /**
     * Notify model to do the action.
     * 
     * @param actionId
     */
    private void doAction(int actionId)
    {
        if (model != null)
        {
            model.xdoAction(actionId);
        }
    }

      
    /**
     * Release all model relative resources, memory, store, daemon thread etc.<br>
     */
    public final void release()
    {   
        addControllerMonitor(AbstractMvcMonitor.CONTROLLER_START_RELEASE, Integer.MIN_VALUE, null, null);
        
        try
        {
            releaseDelegate();
        }
        catch(Throwable ex)
        {
            Logger.logCriticalError(this.getClass().getName(), ex);
        }
        
        pendingModelEvents.removeAllElements();
        
        isStopped = true;
        
        try
        {
            if (view != null)
            {
                view.xpopAllViews();
            }
            
            if (model != null)
            {
                model.release();
            }
        }
        catch(Throwable ex)
        {
            Logger.logCriticalError(this.getClass().getName(), ex);
        }
        
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "controller is released.");
        }
        
        addControllerMonitor(AbstractMvcMonitor.CONTROLLER_EXIT_RELEASE, Integer.MIN_VALUE, null, null);
    }
    
    /**
     * delegate method of release.
     */
    protected void releaseDelegate()
    {
        
    }
    
    /**
     * Release all modules in the controller chain. <br>
     * 
     * Q: When will this method be invoked? <br>
     * 
     * A: If you want to stop all the current modules and start a totally new one. <br>
     * 
     */
    protected final void releaseAll()
    {
        AbstractController controller = this;
        while (controller != null)
        {
            controller.release();
            controller = controller.superController;
        }
    }
    
    /**
     * Deactivate current controller.<br>
     * 
     * 1) Pause unnecessary daemon back end job.<br>
     * 
     * 2) Stop update view.<br>
     */
    public final void deactivate()
    {
        deactivateDelegate();
    }
    
    protected void deactivateDelegate()
    {
        if (model != null)
        {
            model.deactivate();
        }
        
        if(view != null)
        {
            view.deactivate();
        }
        
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "controller is deactivated.");
        }
    }

    /**
     * Activate current controller.<br>
     * 
     * 1) Resume daemon back end job.<br>
     * 
     * 2) Allow to activate view.<br>
     * 
     * @param isUpdateView
     * 
     */
    public final void activate(boolean isUpdateView)
    {
        activateDelegate(isUpdateView);
    }
    
    protected void activateDelegate(boolean isUpdateView)
    {
        if (model != null)
        {
            model.activate(isUpdateView);
        }
        
        if(view != null)
        {
            view.activate();
        }
        
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "controller is activated.");
        }
        
        currentController = this;
    }
    
    /**
     * retrieve the model, mainly used for unit test.
     * @return the model
     */
    public AbstractModel getModel()
    {
        return model;
    }
    
    /**
     * Back to last stable state
     * didn't propose invoke it directly in child controller
     * 
     * @return last stable state
     */
    protected int backToLastStableState()
    {
        this.stateMachine.backToLastStableState();
        model.setState(stateMachine.getCurrentState());
        return model.state;
    }
    
    /**
     * The controller back event which will be used when exit current controller.
     */
    ControllerBackEvent controllerBackEvent;

    /**
     * Inner class for controller back event
     * @author zhdong
     *
     */
    static class ControllerBackEvent
    {
        Parameter parameter;

        int controllerEventId;

        ControllerBackEvent(int controllerEventId, Parameter parameter)
        {
            this.controllerEventId = controllerEventId;
            this.parameter = parameter;
        }
        
        public String toString()
        {
            return "controler event: " + controllerEventId + parameter;
        }
    }
    
    private void addControllerMonitor(String method, int state, Object param, Object returnResult)
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
                monitor.watchController(method, this, this.model, state, param, returnResult);
            }
        }
        catch(Exception e)
        {
            Logger.logCriticalError(this.getClass().getName(), e);
        }
    }
    
}
