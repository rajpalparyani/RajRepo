/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RequestJob.java
 *
 */
package com.telenav.comm;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import com.telenav.comm.ICommCallback.CommResponse;
import com.telenav.logger.Logger;
import com.telenav.network.TnConnection;
import com.telenav.network.TnDatagram;
import com.telenav.network.TnDatagramConnection;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnSocketConnection;
import com.telenav.threadpool.IJob;

/**
 * the network request job.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
class RequestJob implements IJob
{
    String requestId;
    Host host;
    int retryTimes;
    long timout;
    ICommCallback commCallBack;
    Comm comm;
    
    ICommStreamHandler streamHandler;
    IRequestJobHandler jobHandler;
    CommResponse response;
    long pendingTime;
    long executeTime;
    boolean isCancelled = false;
    boolean isRunning = false;
    int retry = 0;
    Hashtable requestHeaders = null;
    
    TnConnection connection;
    OutputStream os;
    InputStream is;
    
    boolean isSocketAlive = false;
    
    private Object waitObj = new Object();
    
    public RequestJob(String requestId, Host host, byte[] request, ICommStreamHandler streamHandler, IRequestJobHandler jobHandler, int retryTimes, long timeout,
            ICommCallback commCallBack, Comm comm, Hashtable requestHeaders)
    {
        pendingTime = System.currentTimeMillis();
        
        this.requestId = requestId;
        this.host = host;
        this.retryTimes = retryTimes;
        this.timout = timeout;
        this.commCallBack = commCallBack;
        this.comm = comm;
        this.streamHandler = streamHandler;
        this.jobHandler = jobHandler;
        this.requestHeaders = requestHeaders;
        
        
        if(Host.SOCKET.equals(host.protocol))//if socket, will use long socket, so add 2 more retry times.
        {
            this.retryTimes += 2;
        }
        
        response = new CommResponse(this.requestId, ICommCallback.NO_DATA);
        
        response.jobId = this.requestId;
        response.requestData = request;
        
        response.progress = ICommCallback.PROGRESS_INIT;
        notifyProgress();
    }

    public void cancel()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "request job " + this.requestId + " is cancelled.");
        
        if (isCancelled)
        {
            return;
        }

        response.isCanceled = true;
        isCancelled = true;
        
        if(response.status != ICommCallback.TIMEOUT)
        {
            response.progress = ICommCallback.PROGRESS_CANCEL;
            notifyProgress();
        }
    }

    public void execute(int handlerID)
    {
        isRunning = true;
        
        if (retry == 0)
        {
            response.pendingCostTime = System.currentTimeMillis() - pendingTime;
            
            executeTime = System.currentTimeMillis();
        }
    
        if (this.jobHandler != null && this.jobHandler.execute(requestId, host, response, streamHandler, handlerID, timout, commCallBack, comm))
        {
            return;
        }
    
        try
        {
            if(this.comm.commListener != null)
            {
                this.comm.commListener.updateNetworkStatus(ICommListener.STATUS_START, null);
            }
            
            if (isCancelled)
                return;
            
            if(this.commCallBack != null && !this.commCallBack.isAllowNetworkRequest(response))
            {
                cancel();
                this.commCallBack.networkError(response);
                return;
            }
            
            try
            {
                response.progress = ICommCallback.PROGRESS_OPENING;
                notifyProgress();
                
                connection = this.comm.networkManager.openConnection(this.comm.getHostProvider().getUrl(host), TnNetworkManager.READ_WRITE, true);
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
                
                response.status = ICommCallback.EXCEPTION_OPEN;
                response.errorMessage = e.getMessage();
                this.response.exception = e;
                
                return;
            }
            
            if(connection == null)
            {
                response.status = ICommCallback.EXCEPTION_OPEN;
                return;
            }
            
            if(connection instanceof TnHttpConnection)
            {
                TnHttpConnection httpConnection = (TnHttpConnection) connection;

                boolean isSuccessful = HttpStreamHandler.handle(httpConnection, this, this.streamHandler);
                if (!isSuccessful)
                {
                    return;
                }
                else
                {
                    response.status = ICommCallback.SUCCESS;
                }
            }
            else if(connection instanceof TnSocketConnection)
            {
                TnSocketConnection socketConnection = (TnSocketConnection) connection;
                
                boolean isSuccessful = SocketStreamHandler.handle(socketConnection, this, this.streamHandler);
                if (!isSuccessful)
                {
                    return;
                }
                else
                {
                    response.status = ICommCallback.SUCCESS;
                }
            }
            else if(connection instanceof TnDatagramConnection)
            {
                TnDatagramConnection datagramConnection = (TnDatagramConnection) connection;
                
                try
                {
                    TnDatagram datagram = datagramConnection.newDatagram(this.response.requestData, this.response.requestData.length);
                    datagramConnection.send(datagram);
                    
                    response.status = ICommCallback.SUCCESS;
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                    
                    this.response.status = ICommCallback.EXCEPTION_SEND;
                    this.response.errorMessage = e.getMessage();
                    this.response.exception = e;
                }
            }
        }
        finally
        {
            if (!isCancelled)
            {
                Logger.log(Logger.INFO, this.getClass().getSimpleName(),"response.status:"+ response.status);
                if (response.status != ICommCallback.SUCCESS)
                {
                    if (retry < retryTimes)
                    {
                        retry++;

                        try
                        {
                            // Using exponential backoff when doing retry.
                            Thread.sleep(Comm.REQUEST_JOB_RETRY_WAIT_TIME * (1 << (retry - 1)));
                        }
                        catch (Exception ex)
                        {
                        }

                        this.comm.addJob(this, false, true);
                        
                        this.close();
                        return;
                    }
                }
            }
            
            response.executeCostTime = System.currentTimeMillis() - executeTime;
            
            if (!isCancelled)
            {
                response.progress = ICommCallback.PROGRESS_DONE;
                notifyProgress();
                
                if (response.status == ICommCallback.SUCCESS)
                {
                    this.commCallBack.networkFinished(response);
                }
                else
                {
                    this.commCallBack.networkError(response);
                }
            }
            
            close();
            
            if(this.comm.commListener != null)
            {
                this.comm.commListener.updateNetworkStatus(ICommListener.STATUS_STOP, null);
            }
            
            isRunning = false;
            
            synchronized (waitObj)
            {
                waitObj.notify();
            }

        }
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    public boolean isRunning()
    {
        return isRunning;
    }
    
    boolean redirectUrl(String url)
    {
        try
        {
            response.progress = ICommCallback.PROGRESS_OPENING;
            notifyProgress();
            
            connection = this.comm.networkManager.openConnection(url, TnNetworkManager.READ_WRITE, true);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
            
            response.status = ICommCallback.EXCEPTION_OPEN;
            response.errorMessage = e.getMessage();
            this.response.exception = e;
            
            return false;
        }
        
        if(connection == null)
        {
            response.status = ICommCallback.EXCEPTION_OPEN;
            return false;
        }
        
        if(connection instanceof TnHttpConnection)
        {
            TnHttpConnection httpConnection = (TnHttpConnection) connection;

            return HttpStreamHandler.handle(httpConnection, this, this.streamHandler);
        }
        else if(connection instanceof TnSocketConnection)
        {
            TnSocketConnection socketConnection = (TnSocketConnection) connection;
            
            return SocketStreamHandler.handle(socketConnection, this, this.streamHandler);
        }
        else if(connection instanceof TnDatagramConnection)
        {
            TnDatagramConnection datagramConnection = (TnDatagramConnection) connection;
            
            try
            {
                TnDatagram datagram = datagramConnection.newDatagram(this.response.requestData, this.response.requestData.length);
                datagramConnection.send(datagram);
                
                return true;
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
                
                this.response.status = ICommCallback.EXCEPTION_SEND;
                this.response.errorMessage = e.getMessage();
                this.response.exception = e;
                
                return false;
            }
        }
        else
        {
            return true;
        }
    }
    
    void notifyProgress()
    {
        if(this.commCallBack != null)
        {
            try
            {
                this.commCallBack.updateProgress(response);
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }

    void timeout()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "request job " + this.requestId + " is timeout.");
        
        if(this.isCancelled())
        {
            return;
        }
        
        response.status = ICommCallback.TIMEOUT;
        
        cancel();
        
        if(this.commCallBack != null)
        {
            try
            {
                this.commCallBack.networkError(response);
            }
            catch(Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }
    
    void close()
    {
        if(isSocketAlive && Host.SOCKET.equals(host.protocol) && !host.isStandaloneSocketServer)
        {
            return;
        }
        
        try
        {
            if (os != null)
            {
                os.close();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            os = null;
        }

        try
        {
            if (is != null)
            {
                is.close();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            is = null;
        }
        
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            connection = null;
        }
    }

    /*public void networkFallbacked()
    {
        if (this.isCancelled() || !isRunning)
        {
            return;
        }
        
        this.cancel();
        
        try
        {
            if (isRunning)
            {
                synchronized (waitObj)
                {
                    waitObj.wait(15000);
                }
            }
        }
        catch (Exception e)
        {
            Logger.log(getClass().getName(), e);
        }
        
        if (dataHandled)
        {
            return;
        }
        
        this.isCancelled = false;
        
        retry = 0;
        
        this.comm.addJob(this, false, true);
        
        this.close();
    }*/
}
