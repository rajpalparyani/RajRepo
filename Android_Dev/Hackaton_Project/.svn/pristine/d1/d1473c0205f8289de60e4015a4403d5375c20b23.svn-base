/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * HttpController.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module.http;

import com.telenav.carconnect.CarConnectManager;
import com.telenav.navsdk.events.HttpProxyEvents.HttpProxyResponse;

/**
 *@author xiangli
 *@date 2012-3-1
 */
public class HttpController
{
    public static void sendResponse(int id, String headers, byte[] data, int responseCode)
    {
        HttpProxyResponse.Builder builder = HttpProxyResponse.newBuilder();
        builder.setHeaders(headers);
        com.google.protobuf.ByteString payLoad = com.google.protobuf.ByteString.copyFrom(data);
        builder.setPayload(payLoad);
        builder.setRequestId(id);
        builder.setHttpResponseCode(responseCode);
        HttpProxyResponse response = builder.build();
        CarConnectManager.getEventBus().broadcast("HttpProxyResponse", response);
    }
}
