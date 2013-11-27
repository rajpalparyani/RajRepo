/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CarConnectPhoneDialProvider.java
 *
 */
package com.telenav.carconnect.provider;

import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.events.PhoneEvents.PhoneCallRequest;
import com.telenav.telephony.TnTelephonyManager;

/**
 * @author tpeng
 * @date 2012-3-13
 */
public class CarConnectPhoneDialProvider extends AbstractProvider
{

    @Override
    public void handle(String eventType, Object eventData)
    {
        // TODO Auto-generated method stub
        if (eventType.equals(CarConnectEvent.PHONE_DIAL))
        {
            PhoneCallRequest phoneDial = (PhoneCallRequest) eventData;
            String phoneNumber = phoneDial.getPhoneNumber();
            TnTelephonyManager.getInstance().startCall(phoneNumber);
        }
    }

    @Override
    public void register()
    {
        // TODO Auto-generated method stub
        getEventBus().subscribe(CarConnectEvent.PHONE_DIAL, this);
    }

    @Override
    public void unregister()
    {
        // TODO Auto-generated method stub
        getEventBus().unsubscribe(CarConnectEvent.PHONE_DIAL, this);
    }

}
