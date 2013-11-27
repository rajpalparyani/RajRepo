/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * DimProvider.java
 *
 */
package com.telenav.module.entry;

import com.telenav.app.CommManager;
import com.telenav.app.ThreadManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.data.serverproxy.impl.json.JsonDimProxy;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-25
 */
public class DimProvider implements IJob, IServerProxyListener
{
    private static class InnterDimProvider
    {
        private static DimProvider instance = new DimProvider();
    }
    
    protected static final String NULL_RESPONSE = "null";
    protected static final int DEFAULT_TIMEOUT = 15000;
    protected static final int MIN_TIMEOUT = 5000;
    
    protected Object mutex_overall = new Object();
    protected Object mutex_get_ptn = new Object();
    protected Object mutex_retry = new Object();
    
    protected String ptn;
    protected String port;
    protected String token;
    protected String carrier;
    protected String destination;
    protected String errorMessage;
    
    protected boolean isAllowFallback = true;
    protected boolean isPtnEncrypted = false;
    protected boolean isCancelled = false;
    protected boolean isRunning = false;
    
    protected int[] retryIntervals = null;
    protected int retryCount;
    protected int timeout = 0;
    
    protected IDimListner listner;
    protected JsonDimProxy dimProxy;
    
    private DimProvider()
    {
        
    }
    
    public static DimProvider getInstance()
    {
        return InnterDimProvider.instance;
    }
    
    public void retrieveDimPtn()
    {
        isCancelled = true;
        notifyDimFinish();
        isCancelled = false;
        ptn = null;
        carrier = null;
        errorMessage = null;
        token = null;
        destination = null;
        port = null;
        retryCount = 0;
        isRunning = true;
        ThreadPool threadPool = ThreadManager.getPool(ThreadManager.TYPE_APP_ACTION);
        threadPool.addJob(this);
    }
    
    public void setDimListner(IDimListner listner)
    {
        this.listner = listner;
    }

    public String getPtn()
    {
        return this.ptn;
    }
    
    public String getCarrier()
    {
        return this.carrier;
    }
    
    public boolean isAllowFallback()
    {
        return true;
    }
    
    public boolean isPtnEncrypted()
    {
        return this.isPtnEncrypted;
    }
    
    public void execute(int handlerID)
    {
        try
        {
            isRunning = true;
            
            retrieve();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            isRunning = false;
            if (null != listner)
            {
                listner.dimFinished();
            }
        }
    }
    
    public void cancel()
    {
        if (null != dimProxy)
        {
            dimProxy.cancel();
        }
        notifyDimFinish();
    }
    
    public boolean isCancelled()
    {
        return isCancelled;
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
    
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        handleError(proxy);
    }

    public void transactionError(AbstractServerProxy proxy)
    {
        handleError(proxy);
    }

    public void transactionFinished(AbstractServerProxy proxy , String jobId)
    {
        if(proxy instanceof JsonDimProxy)
        {
            JsonDimProxy dimProxy = (JsonDimProxy) proxy;
            int status = dimProxy.getStatus();
            String action = dimProxy.getRequestAction();
            
            if (action.equals(IServerProxyConstants.ACT_GET_DIM_TOKEN))
            {
                if(status == IServerProxyConstants.SUCCESS)
                {
                    this.token = dimProxy.getToken();
                    this.destination = dimProxy.getDestination();
                    this.port = dimProxy.getPort();
                    retryIntervals = dimProxy.getRetryIntervals();
                    if(retryIntervals == null)
                    {
                        //retryIntervals unit is ms, we retry ptn 3 times and every time use 15s
                        retryIntervals = new int[]{8000, 5000, 5000};
                    }
                    
                    int serverDrivenTimeout = dimProxy.getTimeout();
                    
                    if (serverDrivenTimeout > MIN_TIMEOUT)
                    {
                        this.timeout = serverDrivenTimeout;
                    }
                    else
                    {
                        this.timeout = DEFAULT_TIMEOUT;
                    }
                    
                    isAllowFallback = dimProxy.isAllowFallback();
                    isPtnEncrypted = dimProxy.isPtnEncrypted();
                    
                    synchronized (mutex_overall)
                    {
                        mutex_overall.notifyAll();
                    }
                }
                else
                {
                    this.ptn = "";
                    notifyDimFinish();
                }

                writeLog("DimProvider - action: " + action + " status:" + status
                        + " token:" + token + " destination:" + destination + " port:"
                        + port + " timeout:" + timeout + " isAllowFallback:"
                        + isAllowFallback + " isPtnEncrypted:" + isPtnEncrypted);
                
            }
            else if(action.equals(IServerProxyConstants.ACT_GET_DIM_PTN))
            {
                if(status == IServerProxyConstants.SUCCESS)
                {
                    this.errorMessage = null;
                    this.ptn = dimProxy.getPTN();
                    this.carrier = dimProxy.getCarrier();
                    //FIX ME: Remove this hardcode after dim server support retrieving carrier.
                    if(AppConfigHelper.BRAND_BELL.equals(AppConfigHelper.brandName))
                    {
                        this.carrier = "70";
                    }
                    if(this.ptn == null || this.ptn.trim().equalsIgnoreCase(NULL_RESPONSE))
                        this.ptn = "";
                    
                    if(this.carrier == null || this.carrier.trim().equalsIgnoreCase(NULL_RESPONSE))
                        this.carrier = "";
                    
                    if (ptn != null && ptn.trim().length() > 0 && carrier != null && carrier.trim().length() > 0)
                    {
                        notifyDimFinish();
                    }
                    else
                    {
                        notifyGetPtnMutex();
                    }
                }
                else
                {
                    this.ptn = "";
                    this.carrier = "";
                    
                    notifyGetPtnMutex();
                }
                
                writeLog("DimProvider - action: "
                    + action + " status:" + status + " ptn:" + ptn + " carrier:" + carrier);
            }
        }

        
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }
    
    protected void notifyDimFinish()
    {
        writeLog("DimProvider - get ptn finish, notify all");
        
        isCancelled = true;
        
        synchronized (mutex_overall)
        {
            mutex_overall.notifyAll();
        }
        
        synchronized (mutex_get_ptn)
        {
            mutex_get_ptn.notifyAll();
        }

        synchronized (mutex_retry)
        {
            mutex_retry.notifyAll();
        }
        
    }

    protected void notifyGetPtnMutex()
    {
        writeLog("DimProvider - get ptn fail, notify retry");
        
        synchronized (mutex_retry)
        {
            mutex_retry.notify();
        }
    }
    
    private String retrieve() throws Exception
    {
        //we need always send the DIM request to get the Ptn Encrypted & isAllowFallback arguments.
        dimProxy = (JsonDimProxy) ServerProxyFactory.getInstance().createDimProxy(null, CommManager.getInstance().getComm(), this, null);
        dimProxy.requestGetToken();

        synchronized (mutex_overall)
        {
            mutex_overall.wait(60 * 1000);
        }
        
        dimProxy.cancel();
        
        retrievePTN();
        
        notifyDimFinish();

        if (ptn != null && ptn.length() > 0)
        {
            return ptn;
        }
        else if(this.errorMessage != null && this.errorMessage.length() > 0)
        {
            throw new IllegalStateException(this.errorMessage);
        }
        
        return ptn;
    }
    
    private void handleError(AbstractServerProxy proxy)
    {
        if(proxy instanceof JsonDimProxy)
        {
            this.errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_SERVER_ERROR, IStringCommon.FAMILY_COMMON);
            if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_GET_DIM_TOKEN))
            {
                notifyDimFinish();
            }
            else if (proxy.getRequestAction().equals(IServerProxyConstants.ACT_GET_DIM_PTN))
            {
                notifyGetPtnMutex();
            }
        }
    }
    
    private void retrievePTN()
    {
        if (retryIntervals != null && retryIntervals.length > 0)
        {
            TnTelephonyManager.getInstance().startMMSAtBackground(destination, token);
            
            while (retryCount < retryIntervals.length)
            {
                if(isCancelled)
                    break;
                
                synchronized (mutex_get_ptn)
                {
                    try
                    {
                        writeLog("DimProvider - retry time:" + retryCount + ", interval:" + retryIntervals[retryCount] +"ms");
                        mutex_get_ptn.wait(retryIntervals[retryCount]);
                    }
                    catch (InterruptedException e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }
                
                if(isCancelled)
                    break;
                
                writeLog("DimProvider - retry time:" + retryCount + ", start request PTN !!!");
                
                dimProxy = (JsonDimProxy) ServerProxyFactory.getInstance().createDimProxy(null, CommManager.getInstance().getComm(), this, null);
                dimProxy.requestGetPtn(token, isPtnEncrypted, (byte)0, 30000);
                
                synchronized (mutex_retry)
                {
                    try
                    {
                        writeLog("DimProvider - retry time:" + retryCount + ", request work wait:" + timeout +"ms");
                        mutex_retry.wait(timeout);
                    }
                    catch (InterruptedException e)
                    {
                        Logger.log(this.getClass().getName(), e);
                    }
                }
                
                dimProxy.cancel();
                
                writeLog("DimProvider - retry time:" + retryCount + ", request work wait, wake up now!!!");
                
                retryCount ++;
            }
        }
        notifyDimFinish();
    }
    
    private void writeLog(String logStr)
    {
        if (Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), logStr);
        }
    }
}
