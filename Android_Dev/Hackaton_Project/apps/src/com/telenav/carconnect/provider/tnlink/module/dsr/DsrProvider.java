/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DsrProvider.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.dsr;

import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.LogUtil;
import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.navsdk.events.DigitalSpeechRecognitionData.RecType;
import com.telenav.navsdk.events.DigitalSpeechRecognitionEvents.DsrBeginRecordingRequest;
import com.telenav.navsdk.events.DigitalSpeechRecognitionEvents.DsrEndRecordingRequest;

/**
 *@author tpeng
 *@date 2012-4-23
 */
public class DsrProvider extends AbstractProvider
{

    @Override
    public void handle(String eventType, Object eventData)
    {
        // TODO Auto-generated method stub
        if(eventType.equals("DsrBeginRecordingRequest")){
            LogUtil.logInfo("Start to handle DsrBeginRecordingRequest!");
            DsrBeginRecordingRequest request = (DsrBeginRecordingRequest) eventData;
            RecType type = request.getRecType();
            CarConnectManager.setDsrRequestFromCar(true);
            Parameter parameter = new Parameter();
            /*
            parameter.put(ICommonConstants.KEY_I_SEARCH_TYPE, searchType);
            parameter.put(ICommonConstants.KEY_O_ADDRESS_DEST, dest);
            parameter.put(ICommonConstants.KEY_O_NAVSTATE, navState);
            parameter.put(ICommonConstants.KEY_I_SEARCH_ALONG_TYPE, searchAlongType);
            parameter.put(ICommonConstants.KEY_I_AC_TYPE, acType);
            parameter.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
            */
            AbstractController controller=new TnlinkDsrController(AbstractController.getCurrentController());
            controller.init(parameter);
        }else if(eventType.equals("DsrCancelRequest")){
            LogUtil.logInfo("Start to handle DsrCancelRequest!");
            AbstractController controller=AbstractController.getCurrentController();
            if(controller!=null)
            {
                String controllerClassName=controller.getClass().getName();
                LogUtil.logInfo("Current Controller:"+controllerClassName);
                if("com.telenav.carconnect.provider.tnlink.module.dsr.TnlinkDsrController".equals(controllerClassName))
                {
                    LogUtil.logInfo("CancelDsrRequest");
                    TnlinkDsrController dsrController=(TnlinkDsrController) controller;
                    dsrController.handleCommand(ICommonConstants.CMD_COMMON_BACK);
                    CarConnectManager.setDsrRequestFromCar(false);
                }
            }
        }
    }

    @Override
    public void register()
    {
        // TODO Auto-generated method stub
        CarConnectManager.getEventBus().subscribe("DsrBeginRecordingRequest", this);
        CarConnectManager.getEventBus().subscribe("DsrCancelRequest", this);
    }

    @Override
    public void unregister()
    {
        // TODO Auto-generated method stub
        CarConnectManager.getEventBus().unsubscribe("DsrBeginRecordingRequest", this);
        CarConnectManager.getEventBus().unsubscribe("DsrCancelRequest", this);
    }
    
}
