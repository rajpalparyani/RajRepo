/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * RoutePlanController.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.nav.routeplan;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseModel;
import com.telenav.carconnect.provider.tnlink.module.AbstractBaseView;
import com.telenav.carconnect.provider.tnlink.module.AbstractController;
import com.telenav.carconnect.provider.tnlink.module.MyLog;
import com.telenav.carconnect.provider.tnlink.module.nav.NavCommonConvert;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.route.Route;
import com.telenav.module.nav.routeplanning.IRoutePlanningConstants;
import com.telenav.mvc.Parameter;
import com.telenav.navsdk.events.NavigationData;
import com.telenav.navsdk.events.NavigationEvents;

/**
 *@author xiangli
 *@date 2012-2-23
 */
public class RoutePlanController extends AbstractController implements IRoutePlanConstants
{
    private Parameter navParameter;
    public RoutePlanController(Parameter parameter)
    {
        this.navParameter = parameter;
    }
    
    @Override
    protected AbstractBaseModel createModel()
    {
        // TODO Auto-generated method stub
        return new RoutePlanModel();
    }

    @Override
    protected AbstractBaseView createView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void postModelEvent(int eventType)
    {
        MyLog.setLog("handler", "response event: " + eventType);
        // TODO Auto-generated method stub
        switch (eventType) 
        {
            case EVENT_MODEL_RESULT:
            {
                Route[] choices = (Route[])model.get(IRoutePlanningConstants.KEY_A_ROUTE_CHOICES);
                int[] length = (int[])model.get(IRoutePlanningConstants.KEY_A_ROUTE_CHOICES_LENGTH);
                int[] eta = (int[])model.get(IRoutePlanningConstants.KEY_A_ROUTE_CHOICES_ETA);
                
                if (null==choices)
                    return;
                
                Stop destStop = (Stop)model.get(KEY_O_DEST_STOP);
                put(KEY_O_DEST_STOP, destStop);
                
                int[] routeId = new int[choices.length];
                for (int i=0; i<routeId.length; ++i)
                    routeId[i]=choices[i].getRouteID();
                put(KEY_O_ROUTE_ID, routeId);
                
                if (navParameter != null)
                {
                    navParameter.put(KEY_O_DEST_STOP, destStop);
                    navParameter.put(KEY_O_ROUTE_ID, routeId);
                }
                NavigationEvents.RoutePlanResponse response = constructRoutePlanResponse(choices, length, eta);
                MyLog.setLog("Route", "route choices: " + choices.length);
                
                CarConnectManager.getEventBus().broadcast(CarConnectEvent.ROUTE_PLAN_RESPONSE, response);
                break;
            }
            case EVENT_MODEL_RESULT_ERROR:
            {
                int errorCode = model.getInt(KEY_I_ERROR_CODE);
                String errorMsg = model.getString(KEY_S_ERROR_MSG);
                NavigationEvents.RoutePlanError response = constructRouteError(errorCode, errorMsg);
                CarConnectManager.getEventBus().broadcast(CarConnectEvent.ROUTE_PLAN_ERROR, response);
                break;
            }
        }
    }
    
    private NavigationEvents.RoutePlanResponse constructRoutePlanResponse(
            Route[] choices, int[] length, int[] eta)
    {
        NavigationEvents.RoutePlanResponse.Builder builder = NavigationEvents.RoutePlanResponse
                .newBuilder();
        NavigationData.RoutePlan.Builder routeBuilder = NavigationData.RoutePlan.newBuilder();
        
        for (int i=0;i<choices.length;++i)
        {
            if (null==choices[i])
                continue;
            
            NavigationData.RouteSummary.Builder summaryBuilder = NavCommonConvert.convertRouteToRouteSummary(choices[i],0,null, true);
            if (length!=null)
                summaryBuilder.setTotalDistanceInMeters(length[i]);
            if (eta!=null)
                summaryBuilder.setTotalTimeInSeconds(eta[i]);
            
            routeBuilder.addRoutes(summaryBuilder);
        }
        
        builder.setPlan(routeBuilder);
        builder.setStatus(NavigationData.NavigationStatusCode.NavigationStatus_OK);
        return builder.build();
    }
    
    private NavigationEvents.RoutePlanError constructRouteError(int errorCode, String errorMsg)
    {
        NavigationEvents.RoutePlanError.Builder builder = NavigationEvents.RoutePlanError.newBuilder();
        if (errorMsg!=null)
            builder.setErrorString(errorMsg);
        builder.setError(NavigationData.NavigationErrorCode.valueOf(errorCode));
        return builder.build();
    }
    
}
