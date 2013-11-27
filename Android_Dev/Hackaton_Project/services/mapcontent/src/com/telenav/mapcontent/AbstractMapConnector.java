/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractMapConnector.java
 *
 */
package com.telenav.mapcontent;


/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 18, 2010
 */
public abstract class AbstractMapConnector
{
    protected IConnectorCallback connectorCallback;
    
    protected abstract void cancel();
    
    public abstract void send(byte[] request, IConnectorCallback callback);
    
    public void setConnectorCallback(IConnectorCallback callback)
    {
        this.connectorCallback = callback;
    }
    
    protected void onCancelled()
    {
        connectorCallback.onCancelled();
    }
    
    protected void onDataReceived(byte[] response)
    {
        connectorCallback.onDataReceived(response);
    }
    
    protected void onFinished()
    {
        connectorCallback.onFinished();
    }
    
    protected void onError(String error, Throwable exception)
    {
        connectorCallback.onError(error, exception);
    }
    
    public interface IConnectorCallback
    {
        public void onCancelled();
        
        public void onDataReceived(byte[] response);
        
        public void onFinished();
        
        public void onError(String error, Throwable exception);
    }
}
