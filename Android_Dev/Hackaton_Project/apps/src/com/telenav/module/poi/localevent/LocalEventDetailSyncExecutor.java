/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * LocalEventDetailSyncExecutor.java
 *
 */
package com.telenav.module.poi.localevent;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.json.JsonGobyProxy;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;

/**
 *@author yning
 *@date 2013-5-17
 */
public class LocalEventDetailSyncExecutor implements IServerProxyListener
{
    private static LocalEventDetailSyncExecutor instance = new LocalEventDetailSyncExecutor();
    
    private Vector<IServerProxyListener> LocalEventDetailRefreshListeners = new Vector<IServerProxyListener>();
    
    private LocalEventDetailSyncExecutor()
    {
        
    }
    
    public static LocalEventDetailSyncExecutor getInstance()
    {
        return instance;
    }
    
    public void addGobyEventRefreshListener(IServerProxyListener listener)
    {
        LocalEventDetailRefreshListeners.removeElement(listener);
        LocalEventDetailRefreshListeners.addElement(listener);
    }
    
    public void removeGobyEventRefreshListener(IServerProxyListener listener)
    {
        LocalEventDetailRefreshListeners.removeElement(listener);
    }
    
    public void requestLocalEventDetail()
    {
        Hashtable favoriteNoDetail = DaoManager.getInstance().getAddressDao().checkNoDetailEvents(true);
        Hashtable recentNoDetail = DaoManager.getInstance().getAddressDao().checkNoDetailEvents(false);
        
        if(!favoriteNoDetail.isEmpty() || !recentNoDetail.isEmpty())
        {
            JsonGobyProxy jsonGobyProxy = new JsonGobyProxy(null, CommManager.getInstance().getComm(), this, null);
            jsonGobyProxy.requestGobyEventDetail(favoriteNoDetail, recentNoDetail);
        }
    }

    @Override
    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
        String action = proxy.getRequestAction();
        
        if(IServerProxyConstants.ACT_GET_EVENT_DETAIL.equals(action))
        {
            Vector<IServerProxyListener> listeners = (Vector<IServerProxyListener>)LocalEventDetailRefreshListeners.clone();
            
            for(int i = 0; i < listeners.size(); i++)
            {
                IServerProxyListener listener = listeners.elementAt(i);
                listener.transactionFinished(proxy, jobId);
            }
        }
    }

    @Override
    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        
    }

    @Override
    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {
        
    }

    @Override
    public void transactionError(AbstractServerProxy proxy)
    {
        
    }

    @Override
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
}
