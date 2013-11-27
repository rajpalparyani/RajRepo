/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * HttpStreamHandler.java
 *
 */
package com.telenav.comm;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;

import com.telenav.datatypes.DataUtil;
import com.telenav.logger.Logger;
import com.telenav.network.TnHttpConnection;

/**
 * Http stream handler.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
abstract class HttpStreamHandler
{
    public static boolean handle(TnHttpConnection httpConnection, RequestJob job, ICommStreamHandler streamHandler)
    {
        OutputStream os = null;
        InputStream is = null;
        long startTime = 0L;
        long endTime = 0L;
        try
        {
            job.response.isChunked = false;
            job.response.progress = ICommCallback.PROGRESS_SENDING;
            job.notifyProgress();
            
            httpConnection.setRequestMethod(TnHttpConnection.POST);
            httpConnection.setRequestProperty("isTnApp", "true");//FIXME this should be for browser server.
            
            //add additional request headers
            if (job.requestHeaders != null)
            {
                for (Enumeration em = job.requestHeaders.keys(); em.hasMoreElements(); )
                {
                    String header = (String) em.nextElement();
                    String value = (String) job.requestHeaders.get(header);
                    httpConnection.setRequestProperty(header, value);
                }
            }
            
            if (job.response.isCanceled)
                return false;

            startTime = System.currentTimeMillis();
            Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "Start Open Connection :" + startTime); 
            os = httpConnection.openOutputStream();
            endTime = System.currentTimeMillis();
            Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "  End Open Connection :" +  endTime + " , Elapse Time : " + (endTime - startTime)); 
            job.os = os;
            
            
            startTime = endTime;
            Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "Start Write Data :" + startTime); 
            if(job.response.requestData != null)
            {
                os.write(job.response.requestData);
            }
            else if(streamHandler != null)
            {
                boolean isSuccessful = streamHandler.send(os, job.response, job.commCallBack);
                if(!isSuccessful)
                {
                    return false;
                }
            }
            endTime = System.currentTimeMillis();
            Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "  End Write Data :" +  endTime + " , Elapse Time : " + (endTime - startTime)); 
        }
        catch (Throwable e)
        {
            Logger.log(HttpStreamHandler.class.getName(), e);
            
            job.response.status = ICommCallback.EXCEPTION_SEND;
            job.response.errorMessage = e.getMessage();
            job.response.exception = e;
            
            return false;
        }
        
        try
        {
            if (job.response.isCanceled)
                return false;

            job.response.progress = ICommCallback.PROGRESS_RECEIVING_WAIT;
            job.notifyProgress();
            
            startTime = System.currentTimeMillis();
            Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "Start Read Data :" + startTime);
            int respCode = httpConnection.getResponseCode();
            long len = httpConnection.getLength();
                   
            //Logger.log(Logger.INFO, "Network Profiling", HttpHeader.getResponseHeader(httpConnection));
            Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "Length : " + String.valueOf(len));
            
            if (respCode == TnHttpConnection.HTTP_OK)
            {
                is = httpConnection.openInputStream();
                job.is = is;
                
                job.response.progress = ICommCallback.PROGRESS_RECEIVING;
                job.notifyProgress();
                
                try
                {
                    boolean isChunked = false;
                    try
                    {
                        String chunked = httpConnection.getHeaderField("tn-cs-chunked"); 
                        Logger.log(Logger.INFO, "Network Profiling", "chunked:" + chunked);
                        isChunked = chunked != null && chunked.equalsIgnoreCase("true");
                    }
                    catch (Throwable t)
                    {
                        Logger.log(HttpStreamHandler.class.getName(), t);    
                    }
                    job.response.isChunked = isChunked;
                    
                    boolean isSuccessful = false;
                    if(streamHandler != null)
                    {
                        isSuccessful = streamHandler.receive(is, len, job.response, job.commCallBack);
                    }
                    else
                    {
                        if (len < 0)
                        {
                            Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "  len is < 0");
                        }
                        if (len >= 0 && !isChunked)
                        {
                            isSuccessful = readHttpContent(is, len, job);
                        }
                        else if (len < 0 && is instanceof GZIPInputStream && !isChunked)
                        {
                            isSuccessful = readGZipContent(is, job);
                        }
                        else
                        {
                            isSuccessful = readHttpChunkedContent(is, job);
                        }
                    }
                    endTime = System.currentTimeMillis();
                    Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "  End Read Data :" +  endTime + " , Elapse Time : " + (endTime - startTime));
                    return isSuccessful;
                }
                catch (Throwable e)
                {
                    Logger.log(HttpStreamHandler.class.getName(), e);
                    Logger.log(Logger.INFO, "Network Profiling", job.requestId + " -- " + "  Exception :" +  e);
                    
                    job.response.status = ICommCallback.EXCEPTION_PARSE;
                    job.response.errorMessage = e.getMessage();
                    job.response.exception = e;
                    
                    return false;
                }
            }
            else if(respCode == TnHttpConnection.HTTP_MOVED_TEMP)
            {
                String newUrl = httpConnection.getHeaderField("location");
                
                return job.redirectUrl(newUrl);
            }
            else
            {
                job.response.status = ICommCallback.RESPONSE_ERROR;
                job.response.errorMessage = httpConnection.getResponseMessage();
                
                return false;
            }
        }
        catch (Throwable e)
        {
            Logger.log(HttpStreamHandler.class.getName(), e);
            e.printStackTrace();
            
            job.response.status = ICommCallback.EXCEPTION_RECEIVE;
            job.response.errorMessage = e.getMessage();
            job.response.exception = e;
            
            return false;
        }
    }
    
    private static boolean readGZipContent(InputStream is, RequestJob job) throws Exception
    {
        byte[] buffer = new byte[4 * 1024];

        // not compressed input stream
        DataInputStream dis = null;
        dis = new DataInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // dis.avaiable does not return the length of the stream!!
        int length = 0;
        while ((length = dis.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }

        byte[] totalBytes = baos.toByteArray();
        baos.close();
        dis.close();

        return readHttpContent(totalBytes, job);
    }

    private static boolean readHttpChunkedContent(InputStream is, RequestJob job) throws Exception
    {
        byte[] lenBuf = new byte[4];
        while (true)
        {   
            // read the length of chunk
            int nRet = StreamHandler.readBytes(is, lenBuf, 0, 4);
            if (nRet != lenBuf.length)
            {   
                throw new RuntimeException("read http streaming :: chunk length error [nRet = " + nRet + "]");
            }   
            
            // check length of this chunk, if length is zero, it means the end of the response 
            int len = DataUtil.readInt(lenBuf, 0);
            if (len <= 0) return true;
            
            // read response mode
            nRet = StreamHandler.readBytes(is, lenBuf, 0, 4);
            if (nRet != lenBuf.length)
            {   
                throw new RuntimeException("read http streaming :: chunk response code error [nRet = " + nRet + "]");
            }
            
            int dataLen = len - 4;
            int chunkRespMode = DataUtil.readInt(lenBuf, 0);
            
            if (chunkRespMode == StreamHandler.RESPONSE_STREAMING || chunkRespMode == StreamHandler.RESPONSE_STREAMING_COMPRESSED)
            {
                // read chunk n content
                byte[] data = new byte[dataLen];
                nRet = StreamHandler.readBytes(is, data, 0, dataLen);
                if (nRet != dataLen)
                {   
                    throw new RuntimeException("read http streaming chunk data failed [nRet = " + nRet + "]");
                }
                
                if(job.response.isCanceled)
                    return false;
                
                job.response.responseData = data;
                if (job.commCallBack != null)
                {
                    job.commCallBack.handleChild(job.response);
                }
            }
            else
            {
                throw new RuntimeException("chunk response code is wrong :: " + chunkRespMode);
            }
        }
    }
    
    private static boolean readHttpContent(InputStream is, long len, RequestJob job) throws Exception
    {
        if (len == -1)
            return false;
        byte[] totalBytes = new byte[(int) len];
        int nRet = StreamHandler.readBytes(is, totalBytes, 0, (int) len);
        if (nRet == -1)
            return false;

        return readHttpContent(totalBytes, job);
    }

    private static boolean readHttpContent(byte[] totalBytes, RequestJob job) throws Exception
    {
        if (totalBytes == null)
            return false;

        int len = totalBytes.length;
        int offset = 0;
        if ((offset + 3) >= len)
            return false;
        byte[] lenBuf = new byte[4];
        lenBuf[0] = totalBytes[offset];
        lenBuf[1] = totalBytes[offset + 1];
        lenBuf[2] = totalBytes[offset + 2];
        lenBuf[3] = totalBytes[offset + 3];
        int chunkRespCode = DataUtil.readInt(lenBuf, 0);
        boolean isCompressed = (chunkRespCode == StreamHandler.RESPONSE_STREAMING_COMPRESSED || chunkRespCode == StreamHandler.RESPONSE_COMPRESSED);
        if (chunkRespCode == StreamHandler.RESPONSE_STREAMING || chunkRespCode == StreamHandler.RESPONSE_STREAMING_COMPRESSED)
        {
            offset = 4;
            while (offset < len)
            {
                if (job.response.isCanceled)
                    break;

                if ((offset + 4) > totalBytes.length)
                    return false;
                lenBuf = new byte[4];
                lenBuf[0] = totalBytes[offset];
                lenBuf[1] = totalBytes[offset + 1];
                lenBuf[2] = totalBytes[offset + 2];
                lenBuf[3] = totalBytes[offset + 3];
                int buffLen = DataUtil.readInt(lenBuf, 0);
                offset += 4;
                if (buffLen <= 0)
                    continue;
                if ((offset + buffLen) > totalBytes.length)
                    return false;
                int uncompressedLen = 0;
                if (isCompressed)
                {
                    if ((offset + 4) > totalBytes.length)
                        return false;
                    lenBuf = new byte[4];
                    lenBuf[0] = totalBytes[offset];
                    lenBuf[1] = totalBytes[offset + 1];
                    lenBuf[2] = totalBytes[offset + 2];
                    lenBuf[3] = totalBytes[offset + 3];
                    uncompressedLen = DataUtil.readInt(lenBuf, 0);
                    offset += 4;
                }

                byte[] data = new byte[buffLen];
                System.arraycopy(totalBytes, offset, data, 0, buffLen);
                offset += buffLen;

                if (isCompressed)
                {
                    data = StreamHandler.uncompress(data, uncompressedLen, job.comm.ioManager);
                }

                if(job.response.isCanceled)
                    return false;
                
                job.response.responseData = data;
                
                if (job.commCallBack != null)
                {
                    job.commCallBack.handleChild(job.response);
                }
            }
        }
        else
        // only for map tile and traffic tile action process
        {
            if (isCompressed)
            {
                offset = 4;
                if ((offset + 4) > totalBytes.length)
                    return false;
                lenBuf = new byte[4];
                lenBuf[0] = totalBytes[offset];
                lenBuf[1] = totalBytes[offset + 1];
                lenBuf[2] = totalBytes[offset + 2];
                lenBuf[3] = totalBytes[offset + 3];
                offset += 4;
                int uncompressedLen = DataUtil.readInt(lenBuf, 0);
                int compressedLen = (int) len - offset;

                byte[] compressData = new byte[compressedLen];
                System.arraycopy(totalBytes, offset, compressData, 0, compressedLen);

                byte[] data = StreamHandler.uncompress(compressData, uncompressedLen, job.comm.ioManager);

                if(job.response.isCanceled)
                    return false;
                
                job.response.responseData = data;
            }
            else
            {
                if(job.response.isCanceled)
                    return false;
                
                job.response.responseData = totalBytes;
            }
            
            if (job.commCallBack != null)
            {
                //log network response for regression test purpose
                Logger.log(Logger.INFO, HttpStreamHandler.class.getName(), "", null, new Object[]{Comm.LOG_NETWORK_HANDLE_CHILD, job.response}, true);
//                Object[] logParams = new Object[] {job.response};
//                Logger.log(Logger.INFO, Comm.LOG_NETWORK_HANDLE_CHILD, "", null, logParams, true);

                job.commCallBack.handleChild(job.response);
            }
        }
        
        if (job.response.isCanceled)
            return false;
        
        return true;
    }
}
