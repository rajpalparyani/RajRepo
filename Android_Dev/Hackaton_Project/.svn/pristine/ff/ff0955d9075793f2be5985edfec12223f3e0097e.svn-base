/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkLocationProviderProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkLocationProviderProxyHelper;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.logger.Logger;
import com.telenav.navsdk.events.LocationData.Position;
import com.telenav.navsdk.events.LocationEvents;
import com.telenav.navsdk.events.LocationData.ProviderType;

/**
 * @author yren
 * @date 2012-11-23
 */
public class NavSdkLocationProviderProxy extends AbstractNavSdkServerProxy
{
    protected NavSdkLocationProviderProxyHelper helper;

    public NavSdkLocationProviderProxy(IServerProxyListener listener)
    {
        super(listener);
        helper = NavSdkLocationProviderProxyHelper.getInstance();
    }

    public void updateLocation(TnLocation location)
    {
        LocationEvents.LocationUpdatedRequest.Builder builder = LocationEvents.LocationUpdatedRequest.newBuilder();
        Position.Builder locationBuilder = Position.newBuilder();
        locationBuilder.setAltitude(location.getAltitude());
        locationBuilder.setHeading(location.getHeading());
        locationBuilder.setLatitude(location.getLatitude() / 100000d);
        locationBuilder.setLongitude(location.getLongitude() / 100000d);
        locationBuilder.setVerAccuracy(location.getAccuracy());
        locationBuilder.setHorAccuracy(location.getAccuracy());
        int speed = location.getSpeed();
        float speedInMs = speed * 0.111316f;
        locationBuilder.setSpeedInMS(speedInMs);
        // since in AndroidLocationUtil.convert(Location location), time has been divided by 10,
        // before we send it to navsdk, we should multiply 10
        locationBuilder.setTimestamp(location.getTime() * 10);
        locationBuilder.setValid(location.isValid());
        if (TnLocationManager.NETWORK_PROVIDER.equalsIgnoreCase(location.getProvider()))
        {
            builder.setProvider(ProviderType.ProviderType_Celltower);
        }
        else
        {
            builder.setProvider(ProviderType.ProviderType_Gps);
        }

        builder.setLocation(locationBuilder.build());

        if (Logger.DEBUG)
        {
            Logger.log(
                Logger.INFO,
                this.getClass().getName(),
                "DB-Test --> Navsdk update location --- lat: " + location.getLatitude() + ", lon: " + location.getLongitude()
                        + ", heading: " + location.getHeading() + ", time: " + location.getTime() + ", localTime: "
                        + location.getLocalTimeStamp() + ", speed: " + location.getSpeed() + ", accuracy: "
                        + location.getAccuracy() + ", provider: " + location.getProvider());
        }
        helper.updateLocation(builder.build());
    }

}
