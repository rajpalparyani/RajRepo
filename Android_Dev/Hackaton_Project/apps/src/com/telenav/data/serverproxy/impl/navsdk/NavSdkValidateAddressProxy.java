/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * NavSdkValidateAddressProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.IValidateAddressProxy;
import com.telenav.data.serverproxy.impl.navsdk.helper.NavSdkPoiProxyHelper;
import com.telenav.navsdk.events.PointOfInterestEvents.AutocompleteAddressRequest;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2012-1-6
 */
public class NavSdkValidateAddressProxy extends AbstractNavSdkServerProxy implements IValidateAddressProxy
{
    protected NavSdkPoiProxyHelper helper;

    private Vector similarAddresses;

    public NavSdkValidateAddressProxy(IServerProxyListener listener)
    {
        super(listener);
        helper = NavSdkPoiProxyHelper.getInstance();
    }

    public void validateAddress(String firstLine, String lastLine, String searchUid, Stop stop, int inputType)
    {
        AutocompleteAddressRequest.Builder builder = AutocompleteAddressRequest.newBuilder();

        builder.setRequestId(1);// TODO
        builder.setRelatedTransactionId(searchUid);

        if (stop == null)
        {
            stop = new Stop();
        }
        stop.setFirstLine(firstLine);
        if (lastLine != null && lastLine.trim().length() > 0)
        {
            stop.setCity(lastLine);
        }
        builder.setPartialAddress(NavSdkProxyUtil.convertProtoAddress(stop));
        // builder.setIsFinishedInput(true);//FIXME
        helper.autocompleteAddress(builder.build(), this);
    }

    public void validateAddress(String firstLine, String lastLine, String searchUid)
    {
        this.validateAddress(firstLine, lastLine, searchUid, null, 0);
    }

    public void setSimilarAddresses(Vector similarAddresses)
    {
        this.similarAddresses = similarAddresses;
    }

    public Vector getSimilarAddresses()
    {
        return this.similarAddresses;
    }
}
