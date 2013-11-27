/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecognizeRequester.java
 *
 */
package com.telenav.dsr.impl;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.comm.ICommCallback;
import com.telenav.dsr.DsrManager;
import com.telenav.dsr.IMetaInfoProvider;
import com.telenav.dsr.IRecordEventListener;
import com.telenav.dsr.util.DsrUtil;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-10-29
 */
public class RecognizeRequester implements ICommCallback
{
    public final static String DSR_REQUEST = "dsr_request";
    
    protected NetworkStreamWrapper wrapper;
    
    protected int retryTimes;
    
    protected long currentSession;
    
    protected long lastRequestTime;
    
    protected IMetaInfoProvider metaProvider;
    
    protected ThreadPool threadPool;
    
    public RecognizeRequester(NetworkStreamWrapper wrapper, ThreadPool pool, IMetaInfoProvider provider, int retryTimes)
    {
        this.wrapper = wrapper;
        this.retryTimes = retryTimes;
        this.metaProvider = provider;
        this.threadPool = pool;
    }
    
    public void sendRequest()
    {
        lastRequestTime = System.currentTimeMillis();
        Comm comm = DsrManager.getInstance().getDsrComm();
        Host host = DsrManager.getInstance().getDsrHost();
        comm.sendData(DSR_REQUEST, host, null, wrapper, retryTimes, 45000, this);
    }
    
    public void handleChild(CommResponse response)
    {
        
    }

    public boolean isAllowNetworkRequest(CommResponse response)
    {
        return true;
    }

    public void networkError(CommResponse response)
    {
        if (currentSession == metaProvider.getRequestSession())
        {
            DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_RECO_FAIL, PrimitiveTypeCache.valueOf(IRecordEventListener.EVENT_STOP_TYPE_NETWORK_ERROR));
        }
    }

    public void networkFinished(CommResponse response)
    {

    }

    public void updateProgress(CommResponse response)
    {

    }
    
    public void cancel()
    {
        DsrManager.getInstance().getDsrComm().cancelJob(DSR_REQUEST);
        
        if(wrapper != null)
        {
            wrapper.recordCanceled();
        }
    }

}
