/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ICommCallback.java
 *
 */
package com.telenav.comm;

/**
 * Call back of network communication. <br />
 * <br />
 * Need register network communication with this listener when receive the status of network communication.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public interface ICommCallback
{
    // response status codes
    /**
     * success status.
     */
    public final static byte SUCCESS = 0;

    /**
     * No data response from server.
     */
    public final static byte NO_DATA = 1;
    
    /**
     * network timeout.
     */
    public final static byte TIMEOUT = 2;
    
    /**
     * response error message from server.
     */
    public final static byte RESPONSE_ERROR = 3;

    /**
     * exception when open connection.
     */
    public final static byte EXCEPTION_OPEN = 11;

    /**
     * exception when send the data.
     */
    public final static byte EXCEPTION_SEND = 12;

    /**
     * exception when receive the data.
     */
    public final static byte EXCEPTION_RECEIVE = 13;

    /**
     * exception when parse the response.
     */
    public final static byte EXCEPTION_PARSE = 14;

    //progress codes
    /**
     * init the network.
     */
    public final static byte PROGRESS_INIT = 0;

    /**
     * opening the network.
     */
    public final static byte PROGRESS_OPENING = 1;

    /**
     * sending data.
     */
    public final static byte PROGRESS_SENDING = 2;

    /**
     * wait for response.
     */
    public final static byte PROGRESS_RECEIVING_WAIT = 3;

    /**
     * receiving data.
     */
    public final static byte PROGRESS_RECEIVING = 4;

    /**
     * finish.
     */
    public final static byte PROGRESS_DONE = 5;

    /**
     * cancel.
     */
    public final static byte PROGRESS_CANCEL = 6;
    
    /**
     * Check the network if it's ready before send the request, it's the network is not ready, will invoke {@link #networkError(CommResponse)} directly.
     * 
     * @param response the response data including error message.
     * @return true means that network is ready, false means currently we can't send the network.
     */
    public boolean isAllowNetworkRequest(ICommCallback.CommResponse response);
    
    /**
     * call back when network error including the whole status except successful.
     * 
     * @param response the response data including error message.
     */
    public void networkError(CommResponse response);

    /**
     * call back when network is finished with successful status.
     * 
     * @param response the response data including successful status.
     */
    public void networkFinished(CommResponse response);

    /**
     * parse the response data when receive from server.
     * 
     * @param response the response data including response data.
     */
    public void handleChild(CommResponse response);

    /**
     * update the progress status of current communication of network.
     * 
     * @param response the response data including status flag.
     */
    public void updateProgress(CommResponse response);

    /**
     * Response data including status, request data, response data, job id, error message and progress etc.
     * 
     * @author fqming
     *
     */
    public class CommResponse
    {
        /**
         * construct a response with special status.
         * 
         * @param jobId the job id of current network communication in the thread pool.
         * @param status current status.
         */
        public CommResponse(String jobId, byte status)
        {
            this.jobId = jobId;
            this.status = status;
        }
        
        /**
         * current status of network communication.
         */
        public byte status;

        /**
         * current progress of network communication.
         */
        public byte progress;

        /**
         * response data from server.
         */
        public byte[] responseData;

        /**
         * request data.
         */
        public byte[] requestData;

        /**
         * the job id of current network communication in the thread pool.
         */
        public String jobId;

        /**
         * error message from network interface or server.
         */
        public String errorMessage;
        
        /**
         * error exception from network interface or server.
         */
        public Throwable exception;
        
        /**
         * wait time before executing the network job in the thread pool.
         */
        public long pendingCostTime;
        
        /**
         * cost time for the whole network communication, this will not include the pending data.
         */
        public long executeCostTime;
        
        /**
         * check if current network communication job is canceled.
         */
        public boolean isCanceled;
        
        /**
         * check if current network communication job is chunk.
         */
        public boolean isChunked;
    }
}
