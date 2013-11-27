/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IServerProxyListener.java
 *
 */
package com.telenav.data.serverproxy;

import com.telenav.comm.ICommCallback;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public interface IServerProxyListener
{
 // response status codes
    /**
     * success status.
     */
    public final static byte SUCCESS = ICommCallback.SUCCESS;

    /**
     * No data response from server.
     */
    public final static byte NO_DATA = ICommCallback.NO_DATA;
    
    /**
     * network timeout.
     */
    public final static byte TIMEOUT = ICommCallback.TIMEOUT;
    
    /**
     * response error message from server.
     */
    public final static byte RESPONSE_ERROR = ICommCallback.RESPONSE_ERROR;

    /**
     * exception when open connection.
     */
    public final static byte EXCEPTION_OPEN = ICommCallback.EXCEPTION_OPEN;

    /**
     * exception when send the data.
     */
    public final static byte EXCEPTION_SEND = ICommCallback.EXCEPTION_SEND;

    /**
     * exception when receive the data.
     */
    public final static byte EXCEPTION_RECEIVE = ICommCallback.EXCEPTION_RECEIVE;

    /**
     * exception when parse the response.
     */
    public final static byte EXCEPTION_PARSE = ICommCallback.EXCEPTION_PARSE;
    
  //progress codes
    /**
     * init the network.
     */
    public final static byte PROGRESS_INIT = ICommCallback.PROGRESS_INIT;

    /**
     * opening the network.
     */
    public final static byte PROGRESS_OPENING = ICommCallback.PROGRESS_OPENING;

    /**
     * sending data.
     */
    public final static byte PROGRESS_SENDING = ICommCallback.PROGRESS_SENDING;

    /**
     * wait for response.
     */
    public final static byte PROGRESS_RECEIVING_WAIT = ICommCallback.PROGRESS_RECEIVING_WAIT;

    /**
     * receiving data.
     */
    public final static byte PROGRESS_RECEIVING = ICommCallback.PROGRESS_RECEIVING;

    /**
     * finish.
     */
    public final static byte PROGRESS_DONE = ICommCallback.PROGRESS_DONE;
    
    /**
     * Called when server proxy finish parse data
     */
    public void transactionFinished(AbstractServerProxy proxy, String jobId);

    /**
     * Update the caller th transaction process
     * @param proxy     server proxy instance
     * @param state     transaction process state:IDLE,OPENING_SOCKET,SENDING,RECEIVING,DONE
     * @param percentage    data download percentage
     */
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress);
    
    /**
     * called when network error
     * @param proxy server proxy instance
     * @param statusCode    SUCCESS,FAILED,TIMEOUT,SEND_ERROR,RECEIVE_ERROR,NO_DATA,EXCEPTION,
     * @param jobId TODO
     * @param requestID     corresponding to cserver action id
     */
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId);

    /**
     *  called when protocol is failed
     * @param proxy server proxy instance
     * @param serviceType
     */
    public void transactionError(AbstractServerProxy proxy);
    
    /**
     * check if currently allow the network transaction.
     * 
     * @param proxy server proxy instance
     * @return true means allowed.
     */
    public boolean isAllowNetworkRequest(AbstractServerProxy proxy);
}
