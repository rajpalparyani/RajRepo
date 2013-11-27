/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AbstractController.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module;

import com.telenav.mvc.Parameter;


/**
 *@author xiangli
 *@date 2012-2-22
 */
public abstract class AbstractController extends Parameter
{
    protected AbstractBaseModel model;
    protected AbstractBaseView view;
//    private static AbstractController currentController;
    
    public void init(Parameter parameter)
    {
        if (this.model == null)
            this.model = createModel();
        if (this.view == null)
            this.view = createView();
        
        if (view!=null)
            view.setModel(model);
        if (model!=null)
        {
            model.controller = this;
            model.setParameter(parameter);
        }
        
//        currentController = this;
    }
    
    public void start(int actionId)
    {
        model.handleEvent(actionId);
    }
    
//    public static AbstractController getCurrentController()
//    {
//        return currentController;
//    }
    
    protected abstract AbstractBaseModel createModel();
    protected abstract AbstractBaseView createView();
    public abstract void postModelEvent(int eventType);
}
