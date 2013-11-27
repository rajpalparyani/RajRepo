/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * MvcControllerHelper.java
 *
 */
package com.telenav.mvc;

/**
 *@author yning
 *@date 2011-6-22
 */
public class MvcControllerHelper
{
    /**
     * you can set the view and model object into the controller you get.
     * Usually used in App layer.
     * @param view
     * @param model
     * @return
     */
    public static AbstractControllerMock getMockController(AbstractView view, AbstractModel model)
    {
        AbstractControllerMock controller = new AbstractControllerMock(null);
        controller.setModel(model);
        controller.setView(view);
        controller.init();
        return controller;
    }
    
    /**
     * you can get a mock controller with mock model and mock view.
     * Usually used in PUSU layer.
     * @return
     */
    public static AbstractControllerMock getMockController()
    {
        AbstractControllerMock controller = new AbstractControllerMock(null);
        return controller;
    }
    
    public static void set(AbstractController controller, AbstractView view, AbstractModel model)
    {
        controller.view = view;
        controller.model = model;
    }
    
    public static void setSuperController(AbstractController controller, AbstractController superController)
    {
        controller.superController = superController;
    }
    
    public static int getControllerEventId(AbstractController controller)
    {
        if(controller.controllerBackEvent == null)
        {
            return -1;
        }
        return controller.controllerBackEvent.controllerEventId;
    }
    
    public static Parameter getControllerEventParam(AbstractController controller)
    {
        if(controller.controllerBackEvent == null)
        {
            return null;
        }
        return controller.controllerBackEvent.parameter;
    }
}
