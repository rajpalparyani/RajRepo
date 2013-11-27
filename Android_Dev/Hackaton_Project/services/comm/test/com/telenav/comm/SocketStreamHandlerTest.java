/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SocketStreamHandler.java
 *
 */
package com.telenav.comm;

/**
 * Socket stream handler.
 * 
 *@author jyxu
 *@date Jul 5, 2011
 */


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.TestCase;

import com.telenav.comm.Comm;
import com.telenav.comm.RequestJob;
import com.telenav.comm.StreamHandler;
import com.telenav.comm.ICommCallback.CommResponse;
import com.telenav.datatypes.DataUtil;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.network.TnNetworkManager;
import com.telenav.network.TnSocketConnection;
import com.telenav.threadpool.ThreadPool;


@RunWith(PowerMockRunner.class)
@PrepareForTest({SocketStreamHandler.class, TnSocketConnection.class, 
    TnIoManager.class, RequestJob.class, ICommStreamHandler.class, StreamHandler.class})
public class SocketStreamHandlerTest extends TestCase
{
    SocketStreamHandler handle;
    RequestJob requestJob;
    Host host;
    String requestId;
    boolean actualIsSuccess;
    SocketStreamHandler socketStreamHandler;
    InputStream is;
    OutputStream os;
    
    public void setUp() throws Exception
    {
        requestId = "unit_test";
        host = new Host();
        byte[] request = new byte[50];
        
        socketStreamHandler = new SocketStreamHandler(){};
        
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        requestJob = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        actualIsSuccess = false;
    }
    
    public void testHandleWithStandaloneSocketServerWithStreamHandler()
    {
        requestJob.host.isStandaloneSocketServer = true;
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        ICommStreamHandler commStreamHandlerMock = PowerMock.createMock(ICommStreamHandler.class);
        byte[] data = "This is a SocketStreamHandler test 200 OK".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        try
        {
            EasyMock.expect(socketConnectionMock.openInputStream()).andReturn(is);
            EasyMock.expect(socketConnectionMock.openOutputStream()).andReturn(os);
            EasyMock.expect(commStreamHandlerMock.receive(is, -1, requestJob.response, requestJob.commCallBack)).andReturn(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, commStreamHandlerMock);
        assertTrue(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithStandaloneSocketServerWithStreamHandlerWithException()
    {
        requestJob.host.isStandaloneSocketServer = true;
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        ICommStreamHandler commStreamHandlerMock = PowerMock.createMock(ICommStreamHandler.class);
        byte[] data = "This is a SocketStreamHandler test 200 OK".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        try
        {
            EasyMock.expect(socketConnectionMock.openInputStream()).andReturn(is);
            EasyMock.expect(socketConnectionMock.openOutputStream()).andReturn(os);
//            EasyMock.expect(commStreamHandlerMock.receive(is, -1, requestJob.response, requestJob.commCallBack)).andReturn(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, commStreamHandlerMock);
        assertFalse(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithStandaloneSocketServer()
    {
        requestJob.host.isStandaloneSocketServer = true;
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        byte[] data = "Content-Length:43 This is a SocketStreamHandler test 200 OK\r\n\n".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        try
        {
            EasyMock.expect(socketConnectionMock.openInputStream()).andReturn(is);
            EasyMock.expect(socketConnectionMock.openOutputStream()).andReturn(os);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, null);
        assertTrue(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithStandAloneSocketServerOpenOutputStreamException()
    {
        requestJob.host.isStandaloneSocketServer = true;
        byte[] data = "Content-Length:43 This is a SocketStreamHandler test 200 OK\r\n\n".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        ICommStreamHandler commStreamHandlerMock = PowerMock.createMock(ICommStreamHandler.class);
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, commStreamHandlerMock);
        assertFalse(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithStandAloneSocketServerOpenInputStreamException()
    {
        requestJob.host.isStandaloneSocketServer = true;
        byte[] data = "Content-Length:43 This is a SocketStreamHandler test 200 OK\r\n\n".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        ICommStreamHandler commStreamHandlerMock = PowerMock.createMock(ICommStreamHandler.class);
        try
        {
            EasyMock.expect(socketConnectionMock.openOutputStream()).andReturn(os);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, commStreamHandlerMock);
        assertFalse(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithLongSocketServerWithStreamHandler()
    {
        requestJob.host.isStandaloneSocketServer = false;
        byte[] data = "Content-Length:43 This is a SocketStreamHandler test 200 OK\r\n\n".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        ICommStreamHandler commStreamHandlerMock = PowerMock.createMock(ICommStreamHandler.class);
        try
        {
            EasyMock.expect(socketConnectionMock.openInputStream()).andReturn(is);
            EasyMock.expect(socketConnectionMock.openOutputStream()).andReturn(os);
            EasyMock.expect(commStreamHandlerMock.receive(is, -1, requestJob.response, requestJob.commCallBack)).andReturn(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, commStreamHandlerMock);
        assertTrue(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithLongSocketServerOpenOutputStreamException()
    {
        requestJob.host.isStandaloneSocketServer = false;
        byte[] data = "Content-Length:43 This is a SocketStreamHandler test 200 OK\r\n\n".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        ICommStreamHandler commStreamHandlerMock = PowerMock.createMock(ICommStreamHandler.class);
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, commStreamHandlerMock);
        assertFalse(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithLongSocketServerOpenInputStreamException()
    {
        requestJob.host.isStandaloneSocketServer = false;
        byte[] data = "Content-Length:43 This is a SocketStreamHandler test 200 OK\r\n\n".getBytes();
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        ICommStreamHandler commStreamHandlerMock = PowerMock.createMock(ICommStreamHandler.class);
        try
        {
            EasyMock.expect(socketConnectionMock.openOutputStream()).andReturn(os);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, commStreamHandlerMock);
        assertFalse(isSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithLongSocketServer()
    {
        requestJob.host.isStandaloneSocketServer = false;
        String text = "This is the HttpStreamHandlerTest.";
        byte[] contentData = text.getBytes();
        byte[] data = new byte[4+4+4+4+contentData.length];
        DataUtil.writeInt(data, contentData.length+12 , 0); //total data len
        DataUtil.writeInt(data, StreamHandler.RESPONSE_STREAMING_COMPRESSED, 4); //resp code
        DataUtil.writeInt(data, contentData.length, 8); //resp code
        int uncompressLength = contentData.length + 10;
        DataUtil.writeInt(data, uncompressLength, 12); //uncompress data length
        System.arraycopy(contentData, 0, data, 16, contentData.length);
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        TnSocketConnection socketConnectionMock = PowerMock.createMock(TnSocketConnection.class);
        byte[] uncompressedData = "I am uncompressed data from server!".getBytes();
        requestJob.comm.ioManager = PowerMock.createMock(TnIoManager.class );
        
        PowerMock.mockStaticPartial(StreamHandler.class, "uncompress");
        try
        {
            EasyMock.expect(socketConnectionMock.openInputStream()).andReturn(is);
            EasyMock.expect(socketConnectionMock.openOutputStream()).andReturn(os);
            EasyMock.expect(StreamHandler.uncompress(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyObject(TnIoManager.class))).andReturn(uncompressedData);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        boolean isSuccess = SocketStreamHandler.handle(socketConnectionMock, requestJob, null);
        assertTrue(isSuccess);
        PowerMock.verifyAll();
    }

    public void testReadLongLive()
    {
        String text = "This is SocketStreamHandlerTest";
        byte[] data = text.getBytes();
        byte[] buff = new byte[4 + data.length];
        DataUtil.writeInt(buff, data.length, 0);
        System.arraycopy(data, 0, buff, 4, data.length);
        InputStream is = new ByteArrayInputStream(buff);
        
        ICommCallback commCallBackMock = PowerMock.createMock(ICommCallback.class);
        requestJob.commCallBack = commCallBackMock;
        commCallBackMock.updateProgress(EasyMock.isA(CommResponse.class));
        EasyMock.expectLastCall().anyTimes();
        commCallBackMock.handleChild(EasyMock.isA(CommResponse.class));
        EasyMock.expectLastCall();
        PowerMock.replayAll();
        try
        {
            actualIsSuccess = SocketStreamHandler.readLongLive(is, requestJob);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertTrue(actualIsSuccess);
        PowerMock.verifyAll();
    }

    public void testReadLongLiveWithCompressed()
    {
        requestJob.host.isStandaloneSocketServer = false;
        String text = "This is the HttpStreamHandlerTest.";
        byte[] contentData = text.getBytes();
        byte[] data = new byte[4+4+4+4+contentData.length];
        DataUtil.writeInt(data, contentData.length+12 , 0); //total data len
        DataUtil.writeInt(data, StreamHandler.RESPONSE_STREAMING_COMPRESSED, 4); //resp code
        DataUtil.writeInt(data, contentData.length, 8); //resp code
        int uncompressLength = contentData.length + 10;
        DataUtil.writeInt(data, uncompressLength, 12); //uncompress data length
        System.arraycopy(contentData, 0, data, 16, contentData.length);
        InputStream is = new ByteArrayInputStream(data);
        
        ICommCallback commCallBackMock = PowerMock.createMock(ICommCallback.class);
        requestJob.commCallBack = commCallBackMock;
        requestJob.comm.ioManager = PowerMock.createMock(TnIoManager.class);
        PowerMock.mockStaticPartial(StreamHandler.class, "uncompress");
        try
        {
            EasyMock.expect(StreamHandler.uncompress(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyObject(TnIoManager.class))).andReturn(data);
            commCallBackMock.updateProgress(EasyMock.isA(CommResponse.class));
            EasyMock.expectLastCall().anyTimes();
            commCallBackMock.handleChild(EasyMock.isA(CommResponse.class));
            EasyMock.expectLastCall();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        try
        {
            actualIsSuccess = SocketStreamHandler.readLongLive(is, requestJob);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertTrue(actualIsSuccess);
        PowerMock.verifyAll();
    }
    
    public void testReadContent()
    {
        String text = "This is SocketStreamHandlerTest";
        byte[] data = text.getBytes();
        byte[] buff = new byte[4 + data.length];
        DataUtil.writeInt(buff, data.length, 0);
        System.arraycopy(data, 0, buff, 4, data.length);
        InputStream is = new ByteArrayInputStream(buff);
        try
        {
            actualIsSuccess = SocketStreamHandler.readContent(is, buff.length, requestJob);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertTrue(actualIsSuccess);
     }
}
