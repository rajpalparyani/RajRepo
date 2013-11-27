/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AbstractNavSdkServerProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.navsdk;

import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;

/**
 * @author Casper(pwang@telenav.cn)
 * @date 2012-1-5
 */
public class AbstractNavSdkServerProxy extends AbstractServerProxy
{

    public AbstractNavSdkServerProxy(IServerProxyListener listener)
    {
        super(null, null, listener, null);
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void transactionFinished(String actionId, AbstractNavSdkServerProxy serverProxy)
    {
        this.actionId = actionId;
        if (listener != null)
        {
            listener.transactionFinished(this, "");
        }
    }

    public void transactionError(String actionId, AbstractNavSdkServerProxy serverProxy)
    {
        this.actionId = actionId;
        this.status = IServerProxyConstants.FAILED;
        if (listener != null)
        {
            listener.transactionError(this);
        }
    }

    public int cancel()
    {
        return IServerProxyConstants.SUCCESS;
    }

    protected void handleResponse(byte[] response)
    {
    }

}
