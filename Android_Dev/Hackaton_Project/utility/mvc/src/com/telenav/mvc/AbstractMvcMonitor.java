/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractMvcMonitor.java
 *
 */
package com.telenav.mvc;

/**
 * The monitor for MVC framework. You can watch every method from this monitor of M, V, C class.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2011-3-7
 */
public abstract class AbstractMvcMonitor
{
    public static final String VIEW_EXIT_IS_SHOW_TRANSIENT_VIEW = "mvc_exit_isShownTransientView";
    public static final String VIEW_START_PREPARE_MODEL_DATA = "mvc_start_prepareModelData";
    public static final String VIEW_EXIT_PREPARE_MODEL_DATA = "mvc_exit_prepareModelData";
    public static final String VIEW_START_HANDLE_VIEW_EVENT = "mvc_start_handleViewEvent";
    public static final String VIEW_EXIT_HANDLE_VIEW_EVENT = "mvc_exit_handleViewEvent";
    public static final String VIEW_START_SHOW_TRANSIENT_VIEW = "mvc_start_showTransientView";
    public static final String VIEW_EXIT_SHOW_TRANSIENT_VIEW = "mvc_exit_showTransientView";
    public static final String VIEW_START_SHOW_VIEW = "mvc_start_showView";
    public static final String VIEW_EXIT_SHOW_VIEW = "mvc_exit_showView";
    public static final String VIEW_START_POP_ALL_VIEWS = "mvc_start_popAllViews";
    public static final String VIEW_EXIT_POP_ALL_VIEWS = "mvc_exit_popAllViews";

    public static final String MODEL_START_RESUME = "mvc_start_resume";
    public static final String MODEL_EXIT_RESUME = "mvc_exit_resume";
    public static final String MODEL_START_POST_MODEL_EVENT = "mvc_start_postModelEvent";
    public static final String MODEL_EXIT_POST_MODEL_EVENT = "mvc_exit_postModelEvent";
    public static final String MODEL_START_RELEASE = "mvc_start_release";
    public static final String MODEL_EXIT_RELEASE = "mvc_exit_release";
    public static final String MODEL_START_ACTIVATE = "mvc_start_activate";
    public static final String MODEL_EXIT_ACTIVATE = "mvc_exit_activate";
    public static final String MODEL_START_DEACTIVATE = "mvc_start_deactivate";
    public static final String MODEL_EXIT_DEACTIVATE = "mvc_exit_deactivate";
    public static final String MODEL_START_DO_ACTION = "mvc_start_doAction";
    public static final String MODEL_EXIT_DO_ACTION = "mvc_exit_doAction";
    public static final String MODEL_START_FETCH = "mvc_start_fetch";
    public static final String MODEL_EXIT_FETCH = "mvc_exit_fetch";
    public static final String MODEL_START_PUT = "mvc_start_put";
    public static final String MODEL_EXIT_PUT = "mvc_exit_put";
    public static final String MODEL_START_GET = "mvc_start_get";
    public static final String MODEL_EXIT_GET = "mvc_exit_get";
    
    public static final String CONTROLLER_START_INIT = "mvc_start_init";
    public static final String CONTROLLER_EXIT_INIT = "mvc_exit_init";
    public static final String CONTROLLER_START_POST_STATE_CHANGE = "mvc_start_postStateChange";
    public static final String CONTROLLER_EXIT_POST_STATE_CHANGE = "mvc_exit_postStateChange";
    public static final String CONTROLLER_START_RELEASE = "mvc_start_release";
    public static final String CONTROLLER_EXIT_RELEASE = "mvc_exit_release";
    public static final String CONTROLLER_HANDLE_TRIGGER = "mvc_handle_trigger";
    
    /**
     * watch the methods of model.
     * 
     * @param method the name of method
     * @param model the model object
     * @param event the event id
     * @param arg the event's argument
     * @param returnResult the method's return value
     */
    protected void watchModel(String method, AbstractModel model, int event, Object arg, Object returnResult)
    {
        
    }
    
    /**
     * watch the methods of view.
     * 
     * @param method the name of method
     * @param view the view object
     * @param model the model object
     * @param state the current view's state
     * @param command the view's command
     * @param returnResult the method's return value
     */
    protected void watchView(String method, AbstractView view, AbstractModelParameter model, int state, int command, Object returnResult)
    {
        
    }
    
    /**
     * watch the methods of controller.
     * 
     * @param method the name of method
     * @param controller the controller object
     * @param model the model object
     * @param event the event id
     * @param param the event's parameter
     * @param returnResult the method's return value
     */
    protected void watchController(String method, AbstractController controller, AbstractModelParameter model, int event, Object param, Object returnResult)
    {
        
    }
    
    /**
     *   A helper method to simulate handle view event
     *   
     * @param view
     * @param commandId
     */
    protected void handleViewEvent(AbstractView view, int commandId)
    {
    	view.handleViewEvent(commandId);
    }
    
    /**
     *  A helper method to get UI thread helper to run regression tests inside UI thread 
     * 
     * @return
     */
    protected IMvcUiThreadHelper getMvcUiThreadHelper()
    {
    	return MvcContext.getInstance().getMvcUiThreadHelper();
    }
    
    /**
     * Retrieve current controller
     * 
     * @return controller
     */
    protected AbstractController getCurrentController()
    {
        return AbstractController.getCurrentController();
    }
    
    /**
     * Retrieve current view
     * 
     * @return view
     */
    protected AbstractView getCurrentView()
    {
        AbstractController currentController = getCurrentController();
        
        return currentController == null ? null : currentController.view;
    }
    
    /**
     * Retrieve current model
     * 
     * @return model
     */
    protected AbstractModel getCurrentModel()
    {
        AbstractController currentController = getCurrentController();
        
        return currentController == null ? null : currentController.model;
    }
}
