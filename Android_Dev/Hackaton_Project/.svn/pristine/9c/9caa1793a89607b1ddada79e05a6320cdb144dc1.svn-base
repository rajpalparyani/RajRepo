/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CarConnectUpsellController.java
 *
 */
package com.telenav.module.carconnect.upsell;

import com.telenav.carconnect.host.CarConnectHostManager;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.module.ModuleFactory;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.AbstractView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.StateMachine;

/**
 *@author chihlh
 *@date Mar 20, 2012
 */
public class CarConnectUpsellController extends AbstractCommonController implements ICarConnectUpsellConstants
{
    public CarConnectUpsellController(AbstractController superController)
    {
        super(superController);
    }

    private final static int[][] STATE_TABLE = new int[][]
    {
            {STATE_VIRGIN, EVENT_CONTROLLER_START, STATE_WAIT_UPSELL_RETURN, ACTION_NONE },
            {STATE_ANY, EVENT_MODEL_EXIT, STATE_VIRGIN, ACTION_NONE},
            { STATE_ANY, EVENT_CONTROLLER_UPSELL_PURCHASE_FINISH, STATE_UPSELL_PURCHASE_FINISH, ACTION_EXIT }
    };

    @Override
    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_WAIT_UPSELL_RETURN:
            {
                IUserProfileProvider userProfileProvider = (IUserProfileProvider)getModel().get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
                //TODO-Need to change feature code
                ModuleFactory.getInstance().startUpSellController(this, FeaturesManager.FEATURE_CODE_CAR_CONNECT, false, userProfileProvider);
                break;
            }
            case STATE_VIRGIN:
            {
                if (currentState == STATE_UPSELL_PURCHASE_FINISH)
                {
                    //Coming back with the back controller event of upsell MVC
                    this.postControllerEvent(EVENT_CONTROLLER_UPSELL_PURCHASE_FINISH, model); // pass back the controller event
                    afterUpsellReturn();
                }
                break;
            }
        }
        
    }
    
    private void afterUpsellReturn()
    {
        boolean navPaused = model.getBool(KEY_B_IS_NAV_PAUSED);
        
        CarConnectHostManager cch = CarConnectHostManager.getInstance();
        if (!cch.checkAccount() && navPaused)
        {
            //upsell is triggered when in navigation, and user does not have account
            // post controller event to resume nav.
            //this will overwrite the previous controller event, but it should be fines since nav controller does not process those events
            //TODO - find reliable way to resume the navigation
            postControllerEvent(ICommonConstants.EVENT_CONTROLLER_RENEW_DYNAMIC_ROUTE, null);
        }
        cch.upsellCallBack(navPaused);
    }
    
    @Override
    protected void activateDelegate(boolean isUpdateView)
    {
        super.activateDelegate(isUpdateView);
        //check if it is coming back from upsell page
        if (model.getState() == STATE_WAIT_UPSELL_RETURN && isUpdateView)
        {
            // if isUpdateView is true, it means the child MVC does not have back controller event. We will force the app to exit through state change.
            afterUpsellReturn();
            this.handleModelEvent(EVENT_MODEL_EXIT);
        }
    }

    @Override
    protected AbstractView createView()
    {
         return null;
    }

    @Override
    protected AbstractModel createModel()
    {
         return new CarConnectUpsellModel();
    }

    @Override
    protected StateMachine createStateMachine()
    {
        return new StateMachine(STATE_TABLE_COMMON, STATE_TABLE);
    }
}
