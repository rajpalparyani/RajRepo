/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestComm.java
 *
 */
package com.telenav.comm;

import org.powermock.api.easymock.PowerMock;

import com.telenav.comm.ICommListener;
import com.telenav.io.TnIoManager;
import com.telenav.network.TnNetworkManager;
import com.telenav.threadpool.IJob;
import com.telenav.threadpool.ThreadPool;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-16
 */
public class CommTest extends TestCase
{
    public void testCommConstruct()
    {
        String requestId = "unit_test";
        Host host = new Host();
        byte[] request = new byte[50];
        
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        Comm comm  = null;
        try
        {
            comm = new Comm(null, mockIoManager, commPool, backupCommPool);
        }
        catch(Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }
        try
        {
            comm = new Comm(mockNetworkManager, null, commPool, backupCommPool);
        }
        catch(Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }
        try
        {
            comm = new Comm(mockNetworkManager, mockIoManager, null, backupCommPool);
        }
        catch(Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    
    public void testSetHostProvider()
    {
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        HostProvider hostProvider = new HostProvider();
        comm.setHostProvider(hostProvider);
        assertSame(hostProvider, comm.getHostProvider());
     }
    
    public void testSetCommListener()
    {
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        ICommListener commListenerMock = PowerMock.createMock(ICommListener.class);
        comm.setCommListener(commListenerMock);
        assertSame(commListenerMock, comm.getCommListener());
    }
    
    public void testAddJob()
    {
        String requestId = "unit_test";
        Host host = new Host();
        byte[] request = new byte[50];
        
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        RequestJob requestJob = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        RequestJob requestJob2 = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        
        //test backup comm pool. Add job by sequence.
        comm.backupCommPool.cancelAll();
        assertEquals(null, comm.backupCommPool.getNextJob());
        comm.addJob(requestJob, false, false);
        comm.addJob(requestJob2, false, false);
        IJob result = comm.backupCommPool.getNextJob();
        assertEquals(result, requestJob);
        
        //test backup comm pool. Add job in front.
        comm.backupCommPool.cancelAll();
        assertEquals(null, comm.backupCommPool.getNextJob());
        comm.addJob(requestJob, false, true);
        comm.addJob(requestJob2, false, true);
        result = comm.backupCommPool.getNextJob();
        assertEquals(result, requestJob2);
        
        //test comm pool. Add job by sequence.
        comm.commPool.cancelAll();
        assertEquals(null, comm.commPool.getNextJob());
        comm.addJob(requestJob, true, false);
        comm.addJob(requestJob2, true, false);
        result = comm.commPool.getNextJob();
        assertEquals(result, requestJob);
        
        //test comm pool. Add job in front.
        comm.commPool.cancelAll();
        assertEquals(null, comm.commPool.getNextJob());
        comm.addJob(requestJob, true, true);
        comm.addJob(requestJob2, true, true);
        result = comm.commPool.getNextJob();
        assertEquals(result, requestJob2);
    }
    
    public void testCancelAll()
    {
        String requestId = "unit_test";
        Host host = new Host();
        byte[] request = new byte[50];
        
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        RequestJob requestJob = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        RequestJob requestJob2 = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        RequestJob requestJob3 = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        RequestJob requestJob4 = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        
        assertFalse(requestJob.isCancelled);
        assertFalse(requestJob2.isCancelled);
        assertFalse(requestJob3.isCancelled);
        assertFalse(requestJob4.isCancelled);
        
        comm.addJob(requestJob, true, false);
        comm.addJob(requestJob2, true, true);
        comm.addJob(requestJob3, false, false);
        comm.addJob(requestJob4, false, true);
        comm.cancelAll();
        
        assertTrue(requestJob.isCancelled);
        assertTrue(requestJob2.isCancelled);
        assertTrue(requestJob3.isCancelled);
        assertTrue(requestJob4.isCancelled);
    }
    
    public void testCancelJob()
    {
        String requestId1 = "unit_test";
        String requestId2 = "comm_test";
        Host host = new Host();
        byte[] request = new byte[50];
        
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        RequestJob requestJob = new RequestJob(requestId1, host, request, null, null, 3, 500000, null, comm, null);
        RequestJob requestJob2 = new RequestJob(requestId2, host, request, null, null, 3, 500000, null, comm, null);
        RequestJob requestJob3 = new RequestJob(requestId1, host, request, null, null, 3, 500000, null, comm, null);
        RequestJob requestJob4 = new RequestJob(requestId2, host, request, null, null, 3, 500000, null, comm, null);
        
        assertFalse(requestJob.isCancelled);
        assertFalse(requestJob2.isCancelled);
        assertFalse(requestJob3.isCancelled);
        assertFalse(requestJob4.isCancelled);
        
        comm.addJob(requestJob, true, false);
        comm.addJob(requestJob2, true, true);
        comm.addJob(requestJob3, false, false);
        comm.addJob(requestJob4, false, true);
        
        comm.cancelJob(requestId1);
        
        assertTrue(requestJob.isCancelled);
        assertFalse(requestJob2.isCancelled);
        assertFalse(requestJob3.isCancelled);
        assertFalse(requestJob4.isCancelled);
        
        comm.cancelJob(requestId2);
        
        assertTrue(requestJob.isCancelled);
        assertTrue(requestJob2.isCancelled);
        assertFalse(requestJob3.isCancelled);
        assertFalse(requestJob4.isCancelled);
        
        comm.commPool.cancelAll();
        comm.cancelJob(requestId1);
        
        assertTrue(requestJob3.isCancelled);
        assertFalse(requestJob4.isCancelled);
        
        comm.cancelJob(requestId2);
        
        assertTrue(requestJob4.isCancelled);
    }
    
    public void testGetHostProvider()
    {
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        assertNull(comm.hostProvider);
        
        HostProvider provider = comm.getHostProvider();
        assertNotNull(provider);
    }
    
    public void testSendData()
    {
        Host host = new Host();
        byte[] request = new byte[20];
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, null);
        comm.sendData("", host, request, null, null, 3, 10000, null);
        
        IJob result = comm.commPool.getNextJob();
        assertNotNull(result);
    }
    
    public void testSendDataWithBackupPool()
    {
        Host host = new Host();
        byte[] request = new byte[20];
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, null);
        comm.backupCommPool = new ThreadPool(1, false, 1);
        comm.sendData("", host, request, null, null, 3, 10000, null);
        
        IJob result = comm.backupCommPool.getNextJob();
        assertNotNull(result);
    }

}
