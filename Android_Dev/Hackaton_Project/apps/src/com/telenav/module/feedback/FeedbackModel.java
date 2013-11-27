/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ShareModel.java
 *
 */
package com.telenav.module.feedback;

import com.telenav.app.CommManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IFeedbackProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.location.TnLocation;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.AbstractCommonNetworkModel;

/**
 *@author wzhu (wzhu@telenav.cn)
 *@date 2010-10-20
 */
class FeedbackModel extends AbstractCommonNetworkModel implements IFeedbackConstants
{

    public FeedbackModel()
    {
    }

    protected void doActionDelegate(int actionId)
    {
        switch(actionId)
        {
            case ACTION_INIT:
            {
                this.postModelEvent(EVENT_MODEL_LAUNCH_MAIN);
                break;
            }
            case ACTION_SUBMIT:
            {
                doSubmit();
                break;
            }
        }
    }

    private void doSubmit()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        IFeedbackProxy feedbackProxy = ServerProxyFactory.getInstance().createFeedbackProxy(null, CommManager.getInstance().getComm(),this, userProfileProvider);
        String feedback = fetchString(KEY_S_FEEDBACK);
        //TODO wzhu will make clear the data to send(need check the new flow)
        TnLocation location = LocationProvider.getInstance().getLastKnownLocation(LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
        if(location == null)
        {
            location = LocationProvider.getInstance().getDefaultLocation();
        }
        if(NavSdkNavEngine.getInstance().isRunning())
        {
//            Route route = RouteWrapper.getInstance().getCurrentRoute();
        }
        else
        {
            feedbackProxy.sendFeedback(0, "", feedback, System.currentTimeMillis(), new TnLocation[]{location}, 1);
        }
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
//        if(proxy instanceof IFeedbackProxy)
//        {
//            put(KEY_S_COMMON_MESSAGE, ResourceManager.getInstance().getCurrentBundle()
//                .getString(IStringFeedback.RES_MESSAGE_BOX_SUCCESS, IStringFeedback.FAMILY_FEEDBACK));
//            postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
//        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        
    }
}
