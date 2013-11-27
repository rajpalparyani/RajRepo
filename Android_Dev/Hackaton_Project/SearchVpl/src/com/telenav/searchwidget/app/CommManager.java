/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CommManager.java
 *
 */
package com.telenav.searchwidget.app;

import com.telenav.comm.Comm;
import com.telenav.io.TnIoManager;
import com.telenav.network.TnNetworkManager;

/**
 *@author hchai
 *@date 2011-7-21
 */
public class CommManager
{
    private static CommManager commManager = new CommManager();
    
    protected Comm defaultComm;
    
    public CommManager()
    {
    }
    
    public static CommManager getInstance()
    {
        return commManager;
    }
    
    public Comm getComm()
    {
        if (this.defaultComm == null)
        {
            this.defaultComm = new Comm(TnNetworkManager.getInstance(), TnIoManager.getInstance(), ThreadManager
                    .getPool(ThreadManager.TYPE_COMM_REQUEST), ThreadManager.getPool(ThreadManager.TYPE_COMM_BACKUP_REQUEST));
        }

        return this.defaultComm;
    }
    
    public void initializeServiceLocator()
    {
        
    }
}
