/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MvcContext.java
 *
 */
package com.telenav.mvc;

import java.util.Vector;

import com.telenav.logger.Logger;

/**
 * This is the entry point of MVC container.
 * 
 * Provide start/stop MVC method.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-21
 */
public abstract class MvcContext
{
    private static MvcContext context = new MvcContext()
    {
        //create an anonymous class
    };
    
    IMvcUiThreadHelper uiThreadHelper;
    Vector monitors;
    
    private MvcContext()
    {
        monitors = new Vector();
    }
    
    public static MvcContext getInstance()
    {
        return context;
    }
    
    /**
     * start MVC
     * @param helper
     */
    public final void init(IMvcUiThreadHelper helper)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "****************MVC start******************");
            Logger.log(Logger.INFO, this.getClass().getName(), "State Common Base Id: " + IStateConstants.STATE_COMMON_BASE);
            Logger.log(Logger.INFO, this.getClass().getName(), "State User Base Id: " + IStateConstants.STATE_USER_BASE);

            Logger.log(Logger.INFO, this.getClass().getName(), "Model Common Base Id: " + ITriggerConstants.EVENT_MODEL_COMMON_BASE);
            Logger.log(Logger.INFO, this.getClass().getName(), "Model User Base Id: " + ITriggerConstants.EVENT_MODEL_USER_BASE);

            Logger.log(Logger.INFO, this.getClass().getName(), "Controller Common Base Id: " + ITriggerConstants.EVENT_CONTROLLER_COMMON_BASE);
            Logger.log(Logger.INFO, this.getClass().getName(), "Controller User Base Id: " + ITriggerConstants.EVENT_CONTROLLER_USER_BASE);

            Logger.log(Logger.INFO, this.getClass().getName(), "Action Common Base Id: " + IActionConstants.ACTION_COMMON_BASE);
            Logger.log(Logger.INFO, this.getClass().getName(), "Action User Base Id: " + IActionConstants.ACTION_USER_BASE);
            Logger.log(Logger.INFO, this.getClass().getName(), "********************************************");
        }
        
        uiThreadHelper = helper;
    }
    
    /**
     *  get UI thread helper to run jobs inside UI thread
     * 
     * @return UI thread helper
     */
    protected IMvcUiThreadHelper getMvcUiThreadHelper()
    {
    	return uiThreadHelper;
    }
    
    /**
     * add monitor object.
     * 
     * @param monitor the monitor object
     */
    public void addMonitor(AbstractMvcMonitor monitor)
    {
        if (monitor == null)
            return;

        if (! monitors.contains(monitor))
        	monitors.addElement(monitor);
    }

    /**
     * remove monitor object.
     * 
     * @param monitor the monitor object
     */
    public void removeMonitor(AbstractMvcMonitor monitor)
    {
        if (monitor == null)
            return;

        monitors.removeElement(monitor);
    }
    
    /**
     * remove the whole monitors.
     */
    public void clearMonitors()
    {
        monitors.removeAllElements();
    }
}
