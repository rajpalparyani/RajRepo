/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SummaryController.java
 *
 */
package com.telenav.module.nav.summarycontrol;

import com.telenav.data.datatypes.address.Address;
import com.telenav.module.ModuleFactory;
import com.telenav.module.nav.ITrafficCallback;
import com.telenav.module.nav.NavSdkRouteWrapper;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.mvc.StateMachine;

/**
 *@author yning
 *@date 2010-12-29
 */
public class SummaryControlController extends AbstractCommonController implements ISummaryControlConstants
{
    private final static int[][] STATE_TABLE = new int[][]
    {
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_ROUTE_SUMMARY, STATE_GO_TO_ROUTE_SUMMARY, ACTION_START_CONTROLLER},
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, STATE_GO_TO_MAP_SUMMARY, ACTION_START_CONTROLLER},
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_TRAFFIC_SUMMARY, STATE_GO_TO_TRAFFIC_SUMMARY, ACTION_START_CONTROLLER},
        { STATE_ANY, EVENT_CONTROLLER_BACK_TO_SUMMARY_CONTROLLER, STATE_CHECK_BACK_STATUS, ACTION_HANDLE_BACK},
        { STATE_CHECK_BACK_STATUS, EVENT_MODEL_BACK_BETWEEN_SUMMARY, STATE_BACK_TO_LAST_CONTROLLER, ACTION_NONE},
        { STATE_CHECK_BACK_STATUS, EVENT_MODEL_EXIT_SUMMARYS, STATE_VIRGIN, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_GOTO_POI, STATE_GO_TO_POI, ACTION_NONE},
        { STATE_ANY, EVENT_CONTROLLER_GO_TO_TURN_MAP, STATE_GO_TO_TURN_MAP, ACTION_NONE},
    };
    
    public SummaryControlController(AbstractController superController)
    {
        super(superController);
    }

    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_GO_TO_ROUTE_SUMMARY:
            {
                Address destAddress = (Address) model.get(KEY_O_ADDRESS_DEST);
                Address origAddress = (Address) model.get(KEY_O_ADDRESS_ORI);
                ITrafficCallback trafficCallback = (ITrafficCallback) model.get(KEY_O_TRAFFIC_CALLBACK);
                int currentSegmentIndex = model.getInt(KEY_I_CURRENT_SEGMENT_INDEX);
                ModuleFactory.getInstance().startRouteSummaryController(this, origAddress, destAddress, trafficCallback, currentSegmentIndex,
                    model.getBool(KEY_B_IS_FROM_SEARCH_ALONG), model.getInt(KEY_I_AC_TYPE));
                break;
            }
            case STATE_GO_TO_MAP_SUMMARY:
            {
                ITrafficCallback trafficCallback = (ITrafficCallback)model.get(KEY_O_TRAFFIC_CALLBACK);
                ModuleFactory.getInstance().startMapController(this, TYPE_MAP_FROM_SUMMARY, EVENT_CONTROLLER_GO_TO_MAP_SUMMARY, trafficCallback, model.getBool(KEY_B_IS_FROM_SEARCH_ALONG), model.getInt(KEY_I_AC_TYPE),  (Address) model.get(KEY_O_ADDRESS_DEST), null);
                break;
            }
            case STATE_GO_TO_TRAFFIC_SUMMARY:
            {
                ITrafficCallback trafficCallback = (ITrafficCallback) model.get(KEY_O_TRAFFIC_CALLBACK);
//                ModuleFactory.getInstance().startTrafficController(this, RouteWrapper.getInstance().getCurrentRouteId(), trafficCallback,
//                    (Address) model.get(ICommonConstants.KEY_O_ADDRESS_ORI), (Address) model.get(ICommonConstants.KEY_O_ADDRESS_DEST),
//                    model.getBool(KEY_B_IS_FROM_SEARCH_ALONG), model.getInt(KEY_I_AC_TYPE));
                ModuleFactory.getInstance().startTrafficController(this, NavSdkRouteWrapper.getInstance().getCurrentRouteId(), trafficCallback,
                    (Address) model.get(ICommonConstants.KEY_O_ADDRESS_ORI), (Address) model.get(ICommonConstants.KEY_O_ADDRESS_DEST),
                    model.getBool(KEY_B_IS_FROM_SEARCH_ALONG), model.getInt(KEY_I_AC_TYPE));
                break;
            }
            case STATE_BACK_TO_LAST_CONTROLLER:
            {
                AbstractController controller = (AbstractController)model.fetch(KEY_O_LAST_CONTROLLER);
                if(controller != null)
                {
                    controller.activate();
                }
                break;
            }
            case STATE_GO_TO_POI:
            {
                postControllerEvent(EVENT_CONTROLLER_GOTO_POI, null);
                break;
            }
            case STATE_GO_TO_TURN_MAP:
            {
                Parameter param = new Parameter();
                param.put(KEY_I_CURRENT_SEGMENT_INDEX, model.getInt(KEY_I_CURRENT_SEGMENT_INDEX));
                postControllerEvent(EVENT_CONTROLLER_GO_TO_TURN_MAP, param);
                break;
            }
        }
    }

    protected AbstractModel createModel()
    {
        return new SummaryControlModel();
    }

    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }

    protected AbstractView createView()
    {
        return new SummaryControlViewTouch(new SummaryControlUiDecorator());
    }
    
    protected void releaseDelegate()
    {
        AbstractController lastController = (AbstractController)this.model.fetch(KEY_O_LAST_CONTROLLER);
        if(lastController != null)
        {
            lastController.release();
            lastController = null;
        }
        
        AbstractController currentController = (AbstractController)this.model.fetch(KEY_O_CURRENT_CONTROLLER);
        if(currentController != null)
        {
            currentController.release();
            currentController = null;
        }
    }

}
