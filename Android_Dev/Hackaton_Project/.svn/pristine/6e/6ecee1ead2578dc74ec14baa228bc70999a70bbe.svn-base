/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SummaryModel.java
 *
 */
package com.telenav.module.nav.summarycontrol;

import com.telenav.mvc.AbstractCommonModel;
import com.telenav.mvc.AbstractController;

/**
 *@author yning
 *@date 2010-12-29
 */
public class SummaryControlModel extends AbstractCommonModel implements ISummaryControlConstants
{

    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_START_CONTROLLER:
            {
                AbstractController lastController = (AbstractController)fetch(KEY_O_LAST_CONTROLLER);
                if(lastController != null)
                {
                    lastController.release();
                    lastController = null;
                }
                
                AbstractController currentController = (AbstractController)fetch(KEY_O_CURRENT_CONTROLLER);
                if(currentController != null)
                {
                    put(KEY_O_LAST_CONTROLLER, currentController);
                }
                
                break;
            }
            case ACTION_HANDLE_BACK:
            {
                if(get(KEY_O_LAST_CONTROLLER) != null)
                {
                    postModelEvent(EVENT_MODEL_BACK_BETWEEN_SUMMARY);
                }
                else
                {
                    postModelEvent(EVENT_MODEL_EXIT_SUMMARYS);
                }
                break;
            }
        }
    }
}
