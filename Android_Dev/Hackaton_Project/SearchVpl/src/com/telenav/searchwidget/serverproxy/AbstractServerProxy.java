/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractServerProxy.java
 *
 */
package com.telenav.searchwidget.serverproxy;

import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public abstract class AbstractServerProxy
{
    protected ServerProxyCommCallback serverProxyCommCallback;
    
    protected Host host;

    protected Comm comm;

    protected IServerProxyListener listener;

    protected String actionId;

    protected int status;

    protected String errorMessage;

    private Object jobMutex = new Object();

    private Vector jobPool = new Vector();
    
    
    public AbstractServerProxy(Host host, Comm comm, IServerProxyListener serverProxyListener)
    {
        this.host = host;
        this.comm = comm;
        this.listener = serverProxyListener;
        
        serverProxyCommCallback = new ServerProxyCommCallback();
    }
    
    public void setHost(Host host)
    {
        this.host = host;
    }
    
    public void setComm(Comm comm)
    {
        this.comm = comm;
    }

    public int cancel()
    {
        synchronized (jobMutex)
        {
            for (int i = 0; i < jobPool.size(); i++)
            {
                this.comm.cancelJob((String)jobPool.elementAt(i));
            }
            jobPool.removeAllElements();
        }

        return IServerProxyConstants.SUCCESS;
    }

    public void reset()
    {
        this.cancel();

        this.status = -1;
        this.errorMessage = "";
    }

    public String getErrorMsg()
    {
        return this.errorMessage;
    }

    public void setErrorMsg(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
    public int getStatus()
    {
        return this.status;
    }
    
    public String getRequestAction()
    {
        return this.actionId;
    }

    /**
     * Clear last request cache.
     */
    protected void clearLastRequest()
    {
        // the sub-class should override this method if they cache previous
        // request
    }

    /**
     * Authenticate failed, we need to check account status.
     */
    protected void authenticateFailed()
    {
        // the sub-class should override this method if they want impl logic for
        // authenticate failed
    }

    protected Host getHostByAction(String action)
    {
        Host requestHost = null;
        if (action != null && action.length() > 0)
        {
            requestHost = this.comm.getHostProvider().createHost(action);
            host = requestHost;
        }

        if(requestHost == null)
            requestHost = this.host;
        
        return requestHost;
    }
    
    protected String sendData(byte[] request, String action, byte retryTimes, int timeout)
    {
        this.actionId = action;
        this.errorMessage = null;
        this.status = -1;
        
        // create a new host based on action
//        Host requestHost = getHostByAction(action);

        String jobId = this.comm.sendData(this.actionId, host, request, retryTimes, timeout, serverProxyCommCallback);

        addJobToList(jobId);

        return jobId;
    }

    protected abstract void handleResponse(byte[] response);
    
    protected void addJobToList(String jobId)
    {
        synchronized (jobMutex)
        {
            this.jobPool.addElement(jobId);
        }
    }

    protected void removeJobFromList(String jobId)
    {
        synchronized (jobMutex)
        {
            this.jobPool.removeElement(jobId);
        }
    }
    
    public class ServerProxyCommCallback implements ICommCallback
    {
        public void handleChild(ICommCallback.CommResponse response)
        {
            handleResponse(response.responseData);
//            if(MisLogManager.getInstance().getFilter().isTypeEnable(IMisLogConstants.TYPE_RTT))
//            {
//                RttMisLog log = MisLogManager.getInstance().getFactory().createRttMisLog();
//                log.setRequestID(response.jobId);
//                log.setRequestFile(host.file);
//                log.setRequestSize(response.requestData == null ? 0 : response.requestData.length);
//                log.setResponseSize(response.responseData.length);
//                log.setRoundTripTime(response.executeCostTime);
//                Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]{ log });
//            }
            response.responseData = null;
        }

        public boolean isAllowNetworkRequest(ICommCallback.CommResponse response)
        {
            if (AbstractServerProxy.this.listener != null)
                return AbstractServerProxy.this.listener.isAllowNetworkRequest(AbstractServerProxy.this);

            return true;
        }

        public void networkError(ICommCallback.CommResponse response)
        {
            removeJobFromList(response.jobId);

            // show standard error message in case of network error
            if(response.errorMessage != null &&
                   response.errorMessage.length() > 0 &&
                      response.exception == null)
            {
                AbstractServerProxy.this.errorMessage = response.errorMessage;
            }
            
            AbstractServerProxy.this.status = IServerProxyConstants.FAILED;

            if (AbstractServerProxy.this.listener != null)
            {
                AbstractServerProxy.this.listener.networkError(AbstractServerProxy.this, response.status);
            }
        }

        public void networkFinished(ICommCallback.CommResponse response)
        {
            removeJobFromList(response.jobId);

            if (AbstractServerProxy.this.listener != null && AbstractServerProxy.this.status != IServerProxyConstants.FAILED
                    && AbstractServerProxy.this.status != IServerProxyConstants.EXCEPTION)
            {
                if (AbstractServerProxy.this.status == IServerProxyConstants.INVALID_IDENTITY)
                {
                    authenticateFailed();
                }
                else
                {
                    AbstractServerProxy.this.listener.transactionFinished(AbstractServerProxy.this);
                    clearLastRequest();
                }
            }
        }

        public void updateProgress(ICommCallback.CommResponse response)
        {
            if (AbstractServerProxy.this.listener != null)
            {
                AbstractServerProxy.this.listener.updateTransactionStatus(AbstractServerProxy.this, response.progress);
            }
        }
    }
}
