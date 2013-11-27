/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Comm.java
 *
 */
package com.telenav.comm;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.network.TnNetworkManager;
import com.telenav.threadpool.ThreadPool;

/**
 * The network communication manager for the whole network requests at tn projects.
 * <br />
 * will provide thread pool and backup thread pool to send the network request.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
public class Comm implements Runnable
{
    public static final String LOG_NETWORK_FINIHSED               =   "marts_network_finished";
    public static final String LOG_NETWORK_HANDLE_CHILD           =   "marts_handle_child";
    public static final String LOG_NETWORK_NEW_COMM               =   "marts_new_comm";
    public static final String LOG_BEFORE_EXECUTE_REQUEST_JOB     =   "marts_before_execute_job";
    public static final String LOG_STREAM_HANDLER_RECEIVE         =   "marts_stream_handler_receive";

    public static int REQUEST_JOB_RETRY_WAIT_TIME                 =    1000;

    protected TnNetworkManager networkManager;
    
    protected TnIoManager ioManager;
    
    protected ThreadPool commPool;

    protected ThreadPool backupCommPool;
    
    protected HostProvider hostProvider;

    protected long lastNotifyTimestamp = -1;
    
    protected ICommListener commListener;
    
    protected Thread timeoutThread;
    protected Object timeoutMutex = new Object();
    

    /**
     * construct a comm object.
     * 
     * @param networkManager will provide the network operation from native.
     * @param ioManager will provide unzip operation when unzip the response data.
     * @param commPool the main thread pool.
     * @param backupCommPool the backup thread pool.
     * @param notifier the notifier will provide the timeout mechanism of network request job.
     */
    public Comm(TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool)
    {
        if (networkManager == null)
        {
            throw new IllegalArgumentException("The networkManager is null.");
        }
        if (ioManager == null)
        {
            throw new IllegalArgumentException("The ioManager is null.");
        }
        if (commPool == null)
        {
            throw new IllegalArgumentException("The commPool is null.");
        }

        this.networkManager = networkManager;
        this.commPool = commPool;
        this.backupCommPool = backupCommPool;

        Logger.log(Logger.INFO, this.getClass().getName(), "", null, new Object[]{LOG_NETWORK_NEW_COMM}, true);
//        Logger.log(Logger.INFO, LOG_NETWORK_NEW_COMM, "", null, null, true);
    }

    /**
     * Set the host provider.
     * 
     * @param hostProvider a host provider.
     */
    public void setHostProvider(HostProvider hostProvider)
    {
        this.hostProvider = hostProvider;
    }
    
    /**
     * Retrieve the host provider.
     * 
     * @return the host provider.
     */
    public HostProvider getHostProvider()
    {
        if(this.hostProvider == null)
        {
            this.hostProvider = new HostProvider();
        }
        
        return this.hostProvider;
    }
    
    /**
     * set the comm listener.
     * 
     * @param commListener the comm listener.
     */
    public void setCommListener(ICommListener commListener)
    {
        this.commListener = commListener;
    }
    
    /**
     * Retrieve the comm listener.
     * 
     * @return the comm listener.
     */
    public ICommListener getCommListener()
    {
        return this.commListener;
    }
    
    /**
     * send data with a job which will be used in the thread pool.
     * 
     * @param requestId request id of the network connection.
     * @param host the host of the network connection.
     * @param request the request data.
     * @param retryTimes retry times of this network connection.
     * @param timeout timeout of this network connection.
     * @param commListener call back of this network connection.
     * @return the request id of this network connection.
     */
    public String sendData(String requestId, Host host, byte[] request, int retryTimes, long timeout, ICommCallback commCallBack)
    {
        return this.sendData(requestId, host, request, null, null, retryTimes, timeout, commCallBack);
    }
    
    /**
     * send data with a job which will be used in the thread pool.
     * 
     * @param requestId request id of the network connection.
     * @param host the host of the network connection.
     * @param streamHandler the stream data handler.
     * @param retryTimes retry times of this network connection.
     * @param timeout timeout of this network connection.
     * @param commListener call back of this network connection.
     * @return the request id of this network connection.
     */
    public String sendData(String requestId, Host host, ICommStreamHandler streamHandler, int retryTimes, long timeout, ICommCallback commCallBack)
    {
        return this.sendData(requestId, host, null, streamHandler, retryTimes, timeout, commCallBack);
    }
    
    /**
     * send data with a job which will be used in the thread pool.
     * 
     * @param requestId request id of the network connection.
     * @param host the host of the network connection.
     * @param request the request data.
     * @param streamHandler the stream data handler.
     * @param retryTimes retry times of this network connection.
     * @param timeout timeout of this network connection.
     * @param commListener call back of this network connection.
     * @return the request id of this network connection.
     */
    public String sendData(String requestId, Host host, byte[] request, ICommStreamHandler streamHandler, int retryTimes, long timeout, ICommCallback commCallBack)
    {
        return this.sendData(requestId, host, request, streamHandler, null, retryTimes, timeout, commCallBack);
    }
    
    /**
     * send data with a job which will be used in the thread pool.
     * 
     * @param requestId request id of the network connection.
     * @param host the host of the network connection.
     * @param request the request data.
     * @param jobHandler the job handler.
     * @param retryTimes retry times of this network connection.
     * @param timeout timeout of this network connection.
     * @param commListener call back of this network connection.
     * @return the request id of this network connection.
     */
    public String sendData(String requestId, Host host, byte[] request, IRequestJobHandler jobHandler, int retryTimes, long timeout,
            ICommCallback commCallBack)
    {
        return this.sendData(requestId, host, request, null, jobHandler, retryTimes, timeout, commCallBack);
    }
    
    /**
     * send data with a job which will be used in the thread pool.
     * 
     * @param requestId request id of the network connection.
     * @param host the host of the network connection.
     * @param request the request data.
     * @param streamHandler the stream data handler.
     * @param jobHandler the job handler.
     * @param retryTimes retry times of this network connection.
     * @param timeout timeout of this network connection.
     * @param commListener call back of this network connection.
     * @return the request id of this network connection.
     */
    public String sendData(String requestId, Host host, byte[] request, ICommStreamHandler streamHandler, IRequestJobHandler jobHandler, int retryTimes, long timeout,
            ICommCallback commCallBack)
    {
        return this.sendData(requestId, host, request, streamHandler, jobHandler, retryTimes, timeout, commCallBack, null);
    }
    
    /**
     * send data with a job which will be used in the thread pool.
     * 
     * @param requestId request id of the network connection.
     * @param host the host of the network connection.
     * @param request the request data.
     * @param streamHandler the stream data handler.
     * @param jobHandler the job handler.
     * @param retryTimes retry times of this network connection.
     * @param timeout timeout of this network connection.
     * @param commListener call back of this network connection.
     * @param requestHeaders additional request headers to send to server when send request to server. (only applicable for http/https protocol for now)
     * @return the request id of this network connection.
     */
    public String sendData(String requestId, Host host, byte[] request, ICommStreamHandler streamHandler, IRequestJobHandler jobHandler, int retryTimes, long timeout,
            ICommCallback commCallBack, Hashtable requestHeaders)
    {
        if(requestId == null || requestId.length() == 0)
        {
            requestId = "comm_" + System.currentTimeMillis();
        }
        else
        {
            requestId = requestId + "_" + System.currentTimeMillis();
        }
        
        RequestJob job = new RequestJob(requestId, host, request, streamHandler, jobHandler, retryTimes, timeout, commCallBack, this, requestHeaders);

        boolean needSendWithPrimaryThread = true;
        if (this.backupCommPool != null)
        {
            if (this.commPool.getQueueSize() == 0 && !this.commPool.isIdle())
            {
                needSendWithPrimaryThread = false;
            }
        }

        addJob(job, needSendWithPrimaryThread, false);

        return job.requestId;
    }

    /**
     * cancel the whole network jobs in this network communication manager.
     */
    public void cancelAll()
    {
        Vector jobs = this.commPool.getCurrentJobs();
        cancelJob(jobs, null);

        jobs = this.commPool.getPendingJobs();
        cancelJob(jobs, null);

        if (this.backupCommPool != null)
        {
            jobs = this.backupCommPool.getCurrentJobs();
            cancelJob(jobs, null);

            jobs = this.backupCommPool.getPendingJobs();
            cancelJob(jobs, null);
        }
    }
    
    /**
     * cancel all the job start with special request.
     * FIXME: please remove it in Arlington. It was to make safe but ugly fix.
     */
    public void cancelAllJobs(String requestId)
    {
        Vector jobs = this.commPool.getCurrentJobs();
        
        cancelSpecificJob(jobs, requestId);

        jobs = this.commPool.getPendingJobs();
        cancelSpecificJob(jobs, requestId);

        if (this.backupCommPool != null)
        {
            jobs = this.backupCommPool.getCurrentJobs();
            cancelSpecificJob(jobs, requestId);

            jobs = this.backupCommPool.getPendingJobs();
            cancelSpecificJob(jobs, requestId);
        }
    }

    /**
     * cancel the network job with special id.
     * 
     * @param requestId the request id.
     */
    public void cancelJob(String requestId)
    {
        Vector jobs = this.commPool.getCurrentJobs();
        if (cancelJob(jobs, requestId))
        {
            return;
        }

        jobs = this.commPool.getPendingJobs();
        if (cancelJob(jobs, requestId))
        {
            return;
        }

        if (this.backupCommPool != null)
        {
            jobs = this.backupCommPool.getCurrentJobs();
            if (cancelJob(jobs, requestId))
            {
                return;
            }

            jobs = this.backupCommPool.getPendingJobs();
            if (cancelJob(jobs, requestId))
            {
                return;
            }
        }
    }

    void addJob(RequestJob job, boolean needSendWithPrimaryThread, boolean isInFront)
    {
        if (this.backupCommPool != null && !needSendWithPrimaryThread)
        {
            if(isInFront)
            {
                this.backupCommPool.insertJobInFront(job);
            }
            else
            {
                this.backupCommPool.addJob(job);
            }
        }
        else
        {
            if(isInFront)
            {
                this.commPool.insertJobInFront(job);
            }
            else
            {
                this.commPool.addJob(job);
            }
        }
        
        if(this.timeoutThread == null)
        {
            this.timeoutThread = new Thread(this, "comm-timeout-thread");
            this.timeoutThread.start();
        }
        
        synchronized (timeoutMutex)
        {
            timeoutMutex.notifyAll();
        }
    }
    
    private boolean cancelSpecificJob(Vector jobs, String requestId)
    {
        for (int i = 0; i < jobs.size(); i++)
        {
            Object jobObj = jobs.elementAt(i);
            if (jobObj instanceof RequestJob)
            {
                RequestJob job = (RequestJob) jobObj;
                if(requestId == null)
                {
                    job.cancel();
                }
                else if (job.requestId.equals(requestId) || job.requestId.indexOf(requestId) != -1)
                {
                    job.cancel();
                }
            }
        }

        return false;
    }
    
    private boolean cancelJob(Vector jobs, String requestId)
    {
        for (int i = 0; i < jobs.size(); i++)
        {
            Object jobObj = jobs.elementAt(i);
            if (jobObj instanceof RequestJob)
            {
                RequestJob job = (RequestJob) jobObj;
                if(requestId == null)
                {
                    job.cancel();
                }
                else if (job.requestId.equals(requestId) || job.requestId.indexOf(requestId) != -1)
                {
                    job.cancel();
                    return true;
                }
            }
        }

        return false;
    }
    
    public void run()
    {
        while (true)
        {
            try
            {
                handleTimeoutJobs(commPool.getCurrentJobs());

                if (backupCommPool != null)
                {
                    handleTimeoutJobs(backupCommPool.getCurrentJobs());
                }
                synchronized (timeoutMutex)
                {
                    if (commPool.getCurrentJobs().size() > 0 || commPool.getPendingJobs().size() > 0
                            || (backupCommPool != null && (backupCommPool.getCurrentJobs().size() > 0 || backupCommPool.getPendingJobs().size() > 0)))
                    {
                        timeoutMutex.wait(1000);
                    }
                    else
                    {
                        timeoutMutex.wait(6000000);
                    }
                }
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }

    private void handleTimeoutJobs(Vector jobs)
    {
        for (int i = 0; i < jobs.size(); i++)
        {
            Object jobObj = jobs.elementAt(i);
            if(jobObj instanceof RequestJob)
            {
                RequestJob job = (RequestJob)jobObj;
                
                if(job.executeTime <= 0)
                {
                    continue;
                }
                
                long waitTime = (job.executeTime + job.timout) - System.currentTimeMillis();

                if (!job.isCancelled() && waitTime <= 0)
                {
                    job.timeout();
                }
            }
        }
    }
}
