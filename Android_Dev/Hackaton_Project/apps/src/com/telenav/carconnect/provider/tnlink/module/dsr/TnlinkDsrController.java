/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TnlinkDsrController.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.dsr;

import android.util.Log;

import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.LogUtil;
import com.telenav.carconnect.provider.ProtocolConvertor;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.location.TnLocation;
import com.telenav.module.dsr.DsrController;
import com.telenav.module.location.LocationProvider;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.AbstractModel;
import com.telenav.mvc.ICommonConstants;
import com.telenav.nav.NavEngine;
import com.telenav.navsdk.events.DigitalSpeechRecognitionEvents.DsrCommandDriveTo;
import com.telenav.navsdk.events.DigitalSpeechRecognitionEvents.DsrReady;
import com.telenav.navsdk.events.NavigationData.NavigationParameters;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;

/**
 *@author chihlh
 *@date Aug 20, 2012
 */
public class TnlinkDsrController extends DsrController
{

    public TnlinkDsrController(AbstractController superController)
    {
        super(superController);
    }
    
    protected void postStateChangeDelegate(int currentState, int nextState)
    {
        switch(nextState)
        {
            case STATE_VIRGIN:
            {
                CarConnectManager.setDsrRequestFromCar(false);
                DsrReady.Builder dsrReadyBuilder=DsrReady.newBuilder();
                CarConnectManager.getEventBus().broadcast("DsrReady", dsrReadyBuilder.build());
                break;
            }
            case STATE_GO_TO_NAV:
            {
                if(NavEngine.getInstance().isRunning())
                {
                    this.backToLastStableState();
                }
                else
                {
                    Log.i("AppLink","navigation request");
                   
                    Address address=(Address)model.fetch(KEY_O_SELECTED_ADDRESS);
                    String name=address.getLabel();
                    if(name!=null) Log.i("AppLink","navigation destination name!");
                    
                    PointOfInterest destPoi=ProtocolConvertor.convertAddressToPointOfInterest(address).build();
                    TnLocation loc = LocationProvider.getInstance().getCurrentLocation(
                        LocationProvider.TYPE_GPS | LocationProvider.TYPE_NETWORK);
                    if(loc!=null)
                    {
                        Stop stop=new Stop();
                        stop.setLat(loc.getLatitude());
                        stop.setLon(loc.getLongitude());
                        PointOfInterest origPoi=ProtocolConvertor.convertStopToPointOfInterest(stop,false).build();
                        NavigationParameters.Builder navBuilder=NavigationParameters.newBuilder();
                        navBuilder.addWaypoints(origPoi);
                        navBuilder.addWaypoints(destPoi);
                        DsrCommandDriveTo.Builder driveToBuilder=DsrCommandDriveTo.newBuilder();
                        driveToBuilder.setParameters(navBuilder);
                        CarConnectManager.getEventBus().broadcast("DsrCommandDriveTo", driveToBuilder.build());
                    }
                    Log.i("AppLink","set dsr request to false in navigation!");
                    CarConnectManager.setDsrRequestFromCar(false);
                    handleCommand(ICommonConstants.CMD_COMMON_BACK);
                    return;
                }
            
              //Added By tpeng
                LogUtil.logInfo("DsrController STATE_GO_TO_NAV");
                return;
            }
        }
        super.postStateChangeDelegate(currentState, nextState);
    }
    
    protected AbstractModel createModel()
    {
        return new TnlinkDsrModel();
    }
        
}
