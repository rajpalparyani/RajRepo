/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AbstractBaseModule.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module;

import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.mvc.Parameter;

/**
 *@author xiangli
 *@date 2012-2-22
 */
public abstract class AbstractBaseModel extends AbstractCommonNetworkModel
{
    //protected Parameter parameter;
    public AbstractController controller;
    
    public void setParameter(Parameter parameter)
    {
        //this.parameter = parameter;
        this.add(parameter);
    }
    
    protected abstract void handleEvent(int actionId);
}
