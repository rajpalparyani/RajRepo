/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractHandler.java
 *
 */
package com.telenav.searchwidget.flow.android;

import com.telenav.searchwidget.framework.android.WidgetParameter;
import com.telenav.searchwidget.serverproxy.AbstractServerProxy;
import com.telenav.searchwidget.serverproxy.IServerProxyListener;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Aug 1, 2011
 */

public abstract class AbstractHandler implements IServerProxyListener
{
    private boolean isCancelled;
    
    public final void execute(WidgetParameter wp)
    {
        isCancelled = false;
        
        executeDelegate(wp);
    }
    
    protected abstract void executeDelegate(WidgetParameter wp);
    
    public void cancel()
    {
        isCancelled = true;
    }
    
    public boolean isCancelled()
    {
        return this.isCancelled;
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy)
    {        
    }
    
    protected void networkErrorDelegate(AbstractServerProxy proxy, byte statusCode)
    {
        
    }
    
    protected void transactionErrorDelegate(AbstractServerProxy proxy)
    {
        
    }
    
    public final void transactionFinished(AbstractServerProxy proxy)
    {
        if (!isCancelled)
        {
            transactionFinishedDelegate(proxy);
        }
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
    }

    public final void networkError(AbstractServerProxy proxy, byte statusCode)
    {
        if (!isCancelled)
        {
            networkErrorDelegate(proxy, statusCode);
        }
    }

    public final void transactionError(AbstractServerProxy proxy)
    {
        if (!isCancelled)
        {
            transactionErrorDelegate(proxy);
        }
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }    
}
