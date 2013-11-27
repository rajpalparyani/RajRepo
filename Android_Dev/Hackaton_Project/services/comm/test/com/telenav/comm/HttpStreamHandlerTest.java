/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * HttpStreamHandler.java
 *
 */
package com.telenav.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.comm.StreamHandler;
import com.telenav.comm.ICommCallback.CommResponse;
import com.telenav.datatypes.DataUtil;
import com.telenav.io.TnIoManager;
import com.telenav.network.TnHttpConnection;
import com.telenav.network.TnNetworkManager;
import com.telenav.threadpool.ThreadPool;

/**
 * Socket stream handler.
 * 
 *@author jyxu
 *@date Jul 5, 2011
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpStreamHandler.class,StreamHandler.class})
public class HttpStreamHandlerTest extends TestCase
{
    SocketStreamHandler handle;
    RequestJob requestJob;
    Host host;
    String requestId;
    boolean actualIsSuccess;
    InputStream is;
    OutputStream os;
    ICommStreamHandler streamHandler;
    TnHttpConnection httpConnectionMock;
    HttpStreamHandler httpStreamHandler;
    byte[] data;
    
    public void setUp()
    {
        requestId = "unit_test";
        host = new Host();
        byte[] request = new byte[50];
        
        httpStreamHandler = new HttpStreamHandler(){};
        
        TnNetworkManager mockNetworkManager = PowerMock.createMock(TnNetworkManager.class);
        TnIoManager mockIoManager = PowerMock.createMock(TnIoManager.class);
        ThreadPool commPool = new ThreadPool(1, false, 0);
        ThreadPool backupCommPool = new ThreadPool(1, false, 0);
        Comm comm = new Comm(mockNetworkManager, mockIoManager, commPool, backupCommPool);
        requestJob = new RequestJob(requestId, host, request, null, null, 3, 500000, null, comm, null);
        actualIsSuccess = false;
        
        httpConnectionMock = PowerMock.createMock(TnHttpConnection.class);
        streamHandler = PowerMock.createMock(ICommStreamHandler.class);

       
    }

    public void testHandleWithSteamHandlerFailWithResponseDataNull()
    {
        String text = "This is the HttpStreamHandlerTest.";
        data = text.getBytes();
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        
        requestJob.response.requestData = null;

        try
        {
            httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
            EasyMock.expectLastCall();
            httpConnectionMock.setRequestProperty("isTnApp", "true");
            EasyMock.expectLastCall();
            EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        PowerMock.replayAll();
        actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, streamHandler);
        assertFalse(actualIsSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithSteamHandlerWithException()
    {
        String text = "This is the HttpStreamHandlerTest.";
        data = text.getBytes();
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();

        try
        {
            httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
            EasyMock.expectLastCall();
            httpConnectionMock.setRequestProperty("isTnApp", "true");
            EasyMock.expectLastCall();
            EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
            EasyMock.expect(httpConnectionMock.getLength()).andReturn((long)data.length);
            EasyMock.expect(httpConnectionMock.getResponseCode()).andReturn(TnHttpConnection.HTTP_OK);
            EasyMock.expect(httpConnectionMock.openInputStream()).andReturn(is);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       
        PowerMock.replayAll();
        actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, streamHandler);
        assertFalse(actualIsSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithSteamHandlerWithSuccess()
    {
        String text = "This is the HttpStreamHandlerTest.";
        data = text.getBytes();
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();

        try
        {
            httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
            EasyMock.expectLastCall();
            httpConnectionMock.setRequestProperty("isTnApp", "true");
            EasyMock.expectLastCall();
            EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
            EasyMock.expect(httpConnectionMock.getLength()).andReturn((long)data.length);
            EasyMock.expect(httpConnectionMock.getResponseCode()).andReturn(TnHttpConnection.HTTP_OK);
            EasyMock.expect(httpConnectionMock.openInputStream()).andReturn(is);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            EasyMock.expect(streamHandler.receive(is, data.length, requestJob.response, requestJob.commCallBack)).andReturn(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        PowerMock.replayAll();
        actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, streamHandler);
        assertTrue(actualIsSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithSteamHandlerWithFail()
    {
        String text = "This is the HttpStreamHandlerTest.";
        data = text.getBytes();
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();

        try
        {
            httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
            EasyMock.expectLastCall();
            httpConnectionMock.setRequestProperty("isTnApp", "true");
            EasyMock.expectLastCall();
            EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
            EasyMock.expect(httpConnectionMock.getResponseCode()).andReturn(httpConnectionMock.HTTP_NO_CONTENT);
            EasyMock.expect(httpConnectionMock.getLength()).andReturn((long)data.length);
            EasyMock.expect(httpConnectionMock.getResponseMessage()).andReturn("test");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        PowerMock.replayAll();
        actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, streamHandler);
        assertFalse(actualIsSuccess);
        PowerMock.verifyAll();
    }

    
    public void testHandleWithUncompressedNotChunkedContent() throws Exception
    {
        String text = "This is the HttpStreamHandlerTest.";
        byte[] contentData = text.getBytes();
        data = new byte[4+4+contentData.length];
        DataUtil.writeInt(data, StreamHandler.RESPONSE_STREAMING, 0); //resp code
        DataUtil.writeInt(data, contentData.length, 4); //buf len
        System.arraycopy(contentData, 0, data, 8, contentData.length);
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        

        httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
        EasyMock.expectLastCall();
        httpConnectionMock.setRequestProperty("isTnApp", "true");
        EasyMock.expectLastCall();
        EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
        EasyMock.expect(httpConnectionMock.getLength()).andReturn((long)data.length);
        EasyMock.expect(httpConnectionMock.getResponseCode()).andReturn(TnHttpConnection.HTTP_OK);
        EasyMock.expect(httpConnectionMock.openInputStream()).andReturn(is);

        
        EasyMock.expect(httpConnectionMock.getHeaderField("tn-cs-chunked")).andReturn("null");
        PowerMock.replayAll();
        
        actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, null);
        assertTrue(actualIsSuccess);
        PowerMock.verifyAll();
    }
    
    public void testHandleWithCompressedNotChunkedContent_A() throws Exception
    {
        String text = "This is the HttpStreamHandlerTest.";
        byte[] contentData = text.getBytes();
        data = new byte[4+4+4+contentData.length];
        DataUtil.writeInt(data, StreamHandler.RESPONSE_STREAMING_COMPRESSED , 0); //resp code
        DataUtil.writeInt(data, contentData.length, 4); //buf len
        int uncompressLength = contentData.length + 10;
        DataUtil.writeInt(data, uncompressLength, 8); //uncompress data length
        System.arraycopy(contentData, 0, data, 12, contentData.length);
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        

        httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
        EasyMock.expectLastCall();
        httpConnectionMock.setRequestProperty("isTnApp", "true");
        EasyMock.expectLastCall();
        EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
        EasyMock.expect(httpConnectionMock.getLength()).andReturn((long)data.length);
        EasyMock.expect(httpConnectionMock.getResponseCode()).andReturn(TnHttpConnection.HTTP_OK);
        EasyMock.expect(httpConnectionMock.openInputStream()).andReturn(is);
        
  
        
        byte[] uncompressedData = "I am uncompressed data from server!".getBytes();
        PowerMock.mockStaticPartial(StreamHandler.class, "uncompress");
        
        requestJob.comm.ioManager = PowerMock.createMock(TnIoManager.class);
        EasyMock.expect(StreamHandler.uncompress(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyObject(TnIoManager.class))).andReturn(uncompressedData);
        EasyMock.expect(httpConnectionMock.getHeaderField("tn-cs-chunked")).andReturn("null");
        PowerMock.replayAll();
        
        actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, null);
        assertTrue(actualIsSuccess);
        PowerMock.verifyAll();
    }

    public void testHandleWithCompressedNotChunkedContent_B()  throws Exception
    {
          String text = "This is the HttpStreamHandlerTest.";
          byte[] contentData = text.getBytes();
          data = new byte[4+4+4+contentData.length];
          DataUtil.writeInt(data, StreamHandler.RESPONSE_COMPRESSED , 0); //resp code
          DataUtil.writeInt(data, contentData.length, 4); //buf len
          int uncompressLength = contentData.length + 10;
          DataUtil.writeInt(data, uncompressLength, 8); //uncompress data length
          System.arraycopy(contentData, 0, data, 12, contentData.length);
    
          is = new ByteArrayInputStream(data);
          os = new ByteArrayOutputStream();

          httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
          EasyMock.expectLastCall();
          httpConnectionMock.setRequestProperty("isTnApp", "true");
          EasyMock.expectLastCall();
          
          EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
          EasyMock.expect(httpConnectionMock.getLength()).andReturn((long)data.length);
          EasyMock.expect(httpConnectionMock.getResponseCode()).andReturn(TnHttpConnection.HTTP_OK);
          EasyMock.expect(httpConnectionMock.openInputStream()).andReturn(is);

          byte[] uncompressedData = "I am uncompressed data from server!".getBytes();
          PowerMock.mockStaticPartial(StreamHandler.class, "uncompress");
          
          requestJob.comm.ioManager = PowerMock.createMock(TnIoManager.class);
          EasyMock.expect(StreamHandler.uncompress(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyObject(TnIoManager.class))).andReturn(uncompressedData);
          EasyMock.expect(httpConnectionMock.getHeaderField("tn-cs-chunked")).andReturn("null");
          
          ICommCallback commCallBackMock = PowerMock.createMock(ICommCallback.class);
          requestJob.commCallBack = commCallBackMock;
          commCallBackMock.updateProgress(EasyMock.isA(CommResponse.class));
          EasyMock.expectLastCall().anyTimes();
          commCallBackMock.handleChild(EasyMock.isA(CommResponse.class));
          EasyMock.expectLastCall();
          PowerMock.replayAll();
          
          actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, null);
          assertTrue(actualIsSuccess);
          PowerMock.verifyAll();
    }
    
    public void testHandleWithChunckedContent()  throws Exception
    {
        String text = "This is the HttpStreamHandlerTest.";
        byte[] contentData = text.getBytes();
        data = new byte[4+4+contentData.length+4];
        DataUtil.writeInt(data, contentData.length+4, 0); //buf len
        DataUtil.writeInt(data, StreamHandler.RESPONSE_STREAMING, 4); //resp code
        System.arraycopy(contentData, 0, data, 8, contentData.length);
        byte[] endTag = new byte[]{0x00,0x00,0x00,0x00};
        System.arraycopy(endTag, 0, data, 4+4+contentData.length, 4);
 
        is = new ByteArrayInputStream(data);
        os = new ByteArrayOutputStream();
        

        httpConnectionMock.setRequestMethod(TnHttpConnection.POST);
        EasyMock.expectLastCall();
        httpConnectionMock.setRequestProperty("isTnApp", "true");
        EasyMock.expectLastCall();
        EasyMock.expect(httpConnectionMock.openOutputStream()).andReturn(os);
        EasyMock.expect(httpConnectionMock.getLength()).andReturn((long)data.length);
        EasyMock.expect(httpConnectionMock.getResponseCode()).andReturn(TnHttpConnection.HTTP_OK);
        EasyMock.expect(httpConnectionMock.openInputStream()).andReturn(is);

        EasyMock.expect(httpConnectionMock.getHeaderField("tn-cs-chunked")).andReturn("true");
        PowerMock.replayAll();
        
        actualIsSuccess = HttpStreamHandler.handle(httpConnectionMock, requestJob, null);
        assertTrue(actualIsSuccess);
        PowerMock.verifyAll();
    }
    
   
}
