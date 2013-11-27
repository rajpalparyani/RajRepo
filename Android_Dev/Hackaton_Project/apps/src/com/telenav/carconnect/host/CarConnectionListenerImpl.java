/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DefaultCarConnectionListener.java
 *
 */
package com.telenav.carconnect.host;

import com.telenav.carconnect.ICarConnectionListener;
import com.telenav.carconnect.events.CarConnectEvents.CarConnectError;
import com.telenav.carconnect.events.CarConnectEvents.ConnectedToCar;
import com.telenav.carconnect.mislog.CarConnectMisLogFactory;
import com.telenav.logger.Logger;

/**
 * @author tpeng
 * @date 2012-2-2
 */
class CarConnectionListenerImpl implements ICarConnectionListener
{
    private CarConnectHostManager cch = CarConnectHostManager.getInstance();

    @Override
    public void onDisconnectedFromCar()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: onDisconnectedFromCar");
        cch.stopCarConnect();
    }

    @Override
    public void lockScreen()
    {
        // Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: lockScreen");
        cch.lockScreen();
    }

    @Override
    public void unlockScreen()
    {
        // Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: unlockScreen");
        cch.unlockScreen();
    }

    @Override
    public void onConnectedToCar(ConnectedToCar event)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: onConnectedToCar");

        // TODO - get mandatory data from event object
        CarConnectMisLogFactory factory = CarConnectMisLogFactory.getInstance();
        factory.setMandatoryData(1, "FORD", "Ford Applink", "BT", event.getDeviceType(), "", event.getDeviceId(), 1, "1.0");

        cch.startCarConnect();
    }

    @Override
    public void onCarConnectError(CarConnectError event)
    {
        // TODO - may need to take action
    }

    @Override
    public void onAttemptToConnect()
    {
        cch.attemptToConnect();
    }

    @Override
    public void onDeviceInRange()
    {
        cch.deviceInRange();
    }

    @Override
    public void onDeviceOutOfRange()
    {
        cch.deviceOutRange();
    }
}
