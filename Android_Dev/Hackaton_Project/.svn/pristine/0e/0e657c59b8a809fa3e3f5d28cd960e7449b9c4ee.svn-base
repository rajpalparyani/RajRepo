/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * SyncStopsExecuter.java
 *
 */
package com.telenav.module.ac;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.IAddressProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;

/**
 *@author yning
 *@date 2012-8-10
 */
public class SyncStopsExecutor implements IServerProxyListener
{
    private boolean isSyncing = false;
    
    private static SyncStopsExecutor instance = new SyncStopsExecutor();
    
    private Vector listeners = new Vector();
    
    private SyncStopsExecutor()
    {
        
    }
    
    public static SyncStopsExecutor getInstance()  
    {
        return instance;
    }
    
    public synchronized void syncStop(IServerProxyListener listener, IUserProfileProvider userProfileProvider)
    {
        listeners.addElement(listener);
        
        if(isSyncing)
        {
            return;
        }
        
        isSyncing = true;
        IAddressProxy proxy = ServerProxyFactory.getInstance().createAddressProxy(null, CommManager.getInstance().getComm(), this, userProfileProvider, false);
        proxy.syncStops(DaoManager.getInstance().getAddressDao().getSyncTime(true), DaoManager.getInstance().getAddressDao()
                .getRecentAddresses(), DaoManager.getInstance().getAddressDao().getFavorateAddresses(), DaoManager.getInstance()
                .getAddressDao().getFavoriteCatalog(), DaoManager.getInstance().getAddressDao().getReceivedAddresses());
    }
    
    public synchronized void cancel()
    {
        CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_SYNC_STOPS);
        isSyncing = false;
        listeners.removeAllElements();
    }

    public synchronized void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        isSyncing = false;
        for(int i = 0; i < listeners.size(); i++)
        {
            IServerProxyListener listener = (IServerProxyListener)listeners.elementAt(i);
            if(listener != null)
            {
                listener.transactionFinished(proxy, jobId);
            }
        }
        listeners.removeAllElements();
    }
    
    public boolean isSyncing()
    {
        return this.isSyncing;
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }

    public synchronized void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        isSyncing = false;
        for(int i = 0; i < listeners.size(); i++)
        {
            IServerProxyListener listener = (IServerProxyListener)listeners.elementAt(i);
            listener.networkError(proxy, statusCode, jobId);
        }
        listeners.removeAllElements();
    }

    public synchronized void transactionError(AbstractServerProxy proxy)
    {
        isSyncing = false;
        for(int i = 0; i < listeners.size(); i++)
        {
            IServerProxyListener listener = (IServerProxyListener)listeners.elementAt(i);
            listener.transactionError(proxy);
        }
        listeners.removeAllElements();
    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        if (!NetworkStatusManager.getInstance().isConnected())
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NO_CELL_COVERAGE, IStringCommon.FAMILY_COMMON);
            proxy.setErrorMsg(errorMessage);
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    public synchronized void removeListener(IServerProxyListener listener)
    {
        int index = listeners.indexOf(listener);
        if(index != -1)
        {
            listeners.removeElementAt(index);
        }
    }
}
