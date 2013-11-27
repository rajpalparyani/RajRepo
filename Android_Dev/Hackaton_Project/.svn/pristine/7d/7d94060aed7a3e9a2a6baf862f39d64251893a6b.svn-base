/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RequestJob.java
 *
 */
package com.telenav.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.network.TnConnection;
import com.telenav.network.TnDatagram;
import com.telenav.network.TnDatagramConnection;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnSocketConnection;
import com.telenav.threadpool.ThreadPool;

/**
 * the network request job.
 * 
 *@author jyxu
 *@date Jul 5, 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TnNetworkManager.class, HttpStreamHandler.class,TnHttpConnection.class, ICommCallback.class, 
       TnDatagramConnection.class, TnDatagram.class,TnSocketConnection.class,SocketStreamHandler.class})
public class RequestJobTest extends TestCase
{
    public RequestJob requestJob;
    String requestId;
    Host host;
    
    public void setUp() throws Exception
    {
        requestId = "unit_test";
        host = new Host();
        byte[] request = new byte[50];
        
        //TnNetworkManager networkManager, TnIoManager ioManager, ThreadPool commPool, ThreadPool backupCommPool
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        requestJob = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
    }

    public void testCancel()
    {
        requestJob.cancel();
        assertTrue(requestJob.isCancelled);
        assertTrue(requestJob.response.isCanceled);
        assertTrue(requestJob.response.progress == ICommCallback.TIMEOUT || requestJob.response.progress == ICommCallback.PROGRESS_CANCEL);
    }
    public void testExecuteWithHttpConnection()
    {
        int handleId = 1;
        requestJob.retry = 0;

        TnNetworkManager networkManagerMock = PowerMock.createMock(TnNetworkManager.class);
        TnConnection connectionMock = PowerMock.createMock(TnHttpConnection.class);
        PowerMock.mockStatic(HttpStreamHandler.class);
        try
        {
            EasyMock.expect(networkManagerMock.openConnection("socket://null", TnNetworkManager.READ_WRITE, true)).andReturn(connectionMock);
            EasyMock.expect(HttpStreamHandler.handle((TnHttpConnection)connectionMock, requestJob, requestJob.streamHandler)).andReturn(true);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ICommCallback commCallBackMock = new CommCallBackMock();
        requestJob.commCallBack = commCallBackMock;
        requestJob.comm.networkManager = networkManagerMock;
        requestJob.isSocketAlive = true;
        requestJob.host.protocol = Host.SOCKET;
        requestJob.host.isStandaloneSocketServer = false;
        PowerMock.replayAll();
        requestJob.execute(handleId);
        assertEquals(ICommCallback.SUCCESS,requestJob.response.status);
        PowerMock.verifyAll();
    }

    public void testExecuteWithDatagramConnection()
    {
        int handleId = 1;
        requestJob.retry = 0;

        TnNetworkManager networkManagerMock = PowerMock.createMock(TnNetworkManager.class);
        TnConnection connectionMock = PowerMock.createMock(TnDatagramConnection.class);
        TnDatagram datagramMock = PowerMock.createMock(TnDatagram.class);
        try
        {
            EasyMock.expect(networkManagerMock.openConnection("socket://null", TnNetworkManager.READ_WRITE, true)).andReturn(connectionMock);
            EasyMock.expect(((TnDatagramConnection)connectionMock).newDatagram(requestJob.response.requestData, requestJob.response.requestData.length)).andReturn(datagramMock);
            ((TnDatagramConnection)connectionMock).send(datagramMock);
            EasyMock.expectLastCall();
            
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ICommCallback commCallBackMock = new CommCallBackMock();
        requestJob.commCallBack = commCallBackMock;
        requestJob.comm.networkManager = networkManagerMock;
        requestJob.isSocketAlive = true;
        requestJob.host.protocol = Host.SOCKET;
        requestJob.host.isStandaloneSocketServer = false;
        PowerMock.replayAll();
        requestJob.execute(handleId);
        assertEquals(ICommCallback.SUCCESS,requestJob.response.status);
        PowerMock.verifyAll();
    }
    
    
    public void testExecuteWithDatagramConnectionException()
    {
        int handleId = 1;
        requestJob.retry = 0;

        TnNetworkManager networkManagerMock = PowerMock.createMock(TnNetworkManager.class);
        TnConnection connectionMock = PowerMock.createMock(TnDatagramConnection.class);
        TnDatagram datagramMock = PowerMock.createMock(TnDatagram.class);
        try
        {
            EasyMock.expect(networkManagerMock.openConnection("socket://null", TnNetworkManager.READ_WRITE, true)).andReturn(connectionMock);
            EasyMock.expect(((TnDatagramConnection)connectionMock).newDatagram(requestJob.response.requestData, requestJob.response.requestData.length)).andThrow(new IOException("exception"));
         }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ICommCallback commCallBackMock = new CommCallBackMock();
        requestJob.commCallBack = commCallBackMock;
        requestJob.comm.networkManager = networkManagerMock;
        requestJob.isSocketAlive = true;
        requestJob.host.protocol = Host.SOCKET;
        requestJob.host.isStandaloneSocketServer = false;
        PowerMock.replayAll();
        requestJob.execute(handleId);
        assertEquals(ICommCallback.EXCEPTION_SEND,requestJob.response.status);
        PowerMock.verifyAll();
    }
    
    public void testExecuteWithSocketConnection()
    {
        int handleId = 1;
        requestJob.retry = 0;

        TnNetworkManager networkManagerMock = PowerMock.createMock(TnNetworkManager.class);
        TnConnection connectionMock = PowerMock.createMock(TnSocketConnection.class);

        PowerMock.mockStatic(SocketStreamHandler.class);
        try
        {
            EasyMock.expect(networkManagerMock.openConnection("socket://null", TnNetworkManager.READ_WRITE, true)).andReturn(connectionMock);
            EasyMock.expect(SocketStreamHandler.handle((TnSocketConnection)connectionMock, requestJob, requestJob.streamHandler)).andReturn(true);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ICommCallback commCallBackMock = new CommCallBackMock();
        requestJob.commCallBack = commCallBackMock;
        requestJob.comm.networkManager = networkManagerMock;
        requestJob.isSocketAlive = true;
        requestJob.host.protocol = Host.SOCKET;
        requestJob.host.isStandaloneSocketServer = false;
        PowerMock.replayAll();
        requestJob.execute(handleId);
        assertEquals(ICommCallback.SUCCESS,requestJob.response.status);
        PowerMock.verifyAll();
    }
    
    public void testExecuteWithException()
    {
        int handleId = 1;
        requestJob.retry = 0;
        TnNetworkManager networkManagerMock = PowerMock.createMock(TnNetworkManager.class);
        ICommCallback commCallBackMock = new CommCallBackMock();
        requestJob.commCallBack = commCallBackMock;
        requestJob.comm.networkManager = networkManagerMock;
        requestJob.isSocketAlive = true;
        requestJob.host.protocol = Host.SOCKET;
        requestJob.host.isStandaloneSocketServer = false;
        PowerMock.replayAll();
        requestJob.execute(handleId);
        assertEquals(ICommCallback.NO_DATA,requestJob.response.status);
        PowerMock.verifyAll();
    }

    public void testExecuteWithNullConnectionException()
    {
        int handleId = 1;
        requestJob.retry = 0;
        TnNetworkManager networkManagerMock = PowerMock.createMock(TnNetworkManager.class);
        try
        {
            EasyMock.expect(networkManagerMock.openConnection(EasyMock.anyObject(String.class), EasyMock.anyInt(), EasyMock.anyBoolean())).andThrow(new IOException("test"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        ICommCallback commCallBackMock = new CommCallBackMock();
        requestJob.commCallBack = commCallBackMock;
        requestJob.comm.networkManager = networkManagerMock;
        requestJob.isSocketAlive = true;
        requestJob.host.protocol = Host.SOCKET;
        requestJob.host.isStandaloneSocketServer = false;
        PowerMock.replayAll();
        requestJob.execute(handleId);
        assertEquals(ICommCallback.EXCEPTION_OPEN,requestJob.response.status);
        PowerMock.verifyAll();
    }
    
    public void testIsCancelled()
    {
        assertEquals(requestJob.isCancelled, requestJob.isCancelled());
    }

    public void testIsRunning()
    {
        assertEquals(requestJob.isRunning, requestJob.isRunning());
    }

    public void testTimeout()
    {
        ICommCallback commCallBackMock = new CommCallBackMock();
        requestJob.commCallBack = commCallBackMock;
        requestJob.timeout();
        assertEquals(ICommCallback.TIMEOUT, requestJob.response.status);
        assertTrue(requestJob.isCancelled);
    }
    
    public void testClose()
    {
        requestJob.os = new ByteArrayOutputStream();
        requestJob.is = new ByteArrayInputStream("test".getBytes());
        TnConnection connectionMock = PowerMock.createMock(TnConnection.class);
        requestJob.connection = connectionMock;
        try
        {
            connectionMock.close();
            EasyMock.expectLastCall().andThrow(new IOException("exception"));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        requestJob.close();
        assertNull(requestJob.os);
        assertNull(requestJob.is);
        assertNull(requestJob.connection);
    }

    static class CommCallBackMock implements ICommCallback
    {

        public void handleChild(CommResponse response)
        {
            // TODO Auto-generated method stub
            
        }

        public boolean isAllowNetworkRequest(CommResponse response)
        {
            return true;
        }


        public void networkError(CommResponse response)
        {
            // TODO Auto-generated method stub
            
        }


        public void networkFinished(CommResponse response)
        {
            // TODO Auto-generated method stub
            
        }


        public void updateProgress(CommResponse response)
        {
            // TODO Auto-generated method stub
            
        }
        
    }
}
