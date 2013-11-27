/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SocketStreamHandler.java
 *
 */
package com.telenav.comm;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.datatypes.DataUtil;
import com.telenav.io.TnIoManager;
import com.telenav.logger.Logger;
import com.telenav.network.TnSocketConnection;

/**
 * Socket stream handler.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 12, 2010
 */
abstract class SocketStreamHandler
{
    public static boolean handle(TnSocketConnection socketConnection, RequestJob job, ICommStreamHandler streamHandler)
    {
        if(!job.host.isStandaloneSocketServer)
        {
            return handleLongSocket(socketConnection, job, streamHandler);
        }
        else
        {
            return handleStandaloneSocket(socketConnection, job, streamHandler);
        }
    }
    
    private static boolean handleStandaloneSocket(TnSocketConnection socketConnection, RequestJob job, ICommStreamHandler streamHandler)
    {
        OutputStream os = null;
        InputStream is = null;
        try
        {
            job.response.progress = ICommCallback.PROGRESS_SENDING;
            job.notifyProgress();
            
            if (job.response.isCanceled)
                return false;

            os = socketConnection.openOutputStream();
            job.os = os;

            if(job.response.requestData != null)
            {
                int contentLen = job.response.requestData.length;
                
                String strApps = "/" + job.host.file;
    
                String sb = "POST " + strApps + " HTTP/1.1\n" + "Host: "
                        + job.host.host + "\n" + "Content-Length: " + contentLen
                        + "\n\n";
    
                byte[] header = sb.getBytes();
    
                os.write(header);
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
        }
        catch (Throwable e)
        {
            Logger.log(SocketStreamHandler.class.getName(), e);
            
            job.response.status = ICommCallback.EXCEPTION_SEND;
            job.response.errorMessage = e.getMessage();
            return false;
        }
        
        try
        {
            if (job.response.isCanceled)
                return false;

            job.response.progress = ICommCallback.PROGRESS_RECEIVING_WAIT;
            job.notifyProgress();
            
            is = socketConnection.openInputStream();
            if (is != null)
            {
                job.is = is;

                job.response.progress = ICommCallback.PROGRESS_RECEIVING;
                job.notifyProgress();
                
                try
                {
                    if(streamHandler != null)
                    {
                        return streamHandler.receive(is, -1, job.response, job.commCallBack);
                    }
                    else
                    {
                        return readStandAlone(is, job);
                    }
                }
                catch (Throwable e)
                {
                    Logger.log(SocketStreamHandler.class.getName(), e);
                    
                    job.response.status = ICommCallback.EXCEPTION_PARSE;
                    job.response.errorMessage = e.getMessage();
                    return false;
                }
            }
            else
            {
                job.response.status = ICommCallback.RESPONSE_ERROR;
                job.response.errorMessage = socketConnection.toString();
            }
        }
        catch (Throwable e)
        {
            Logger.log(SocketStreamHandler.class.getName(), e);
            
            job.response.status = ICommCallback.EXCEPTION_RECEIVE;
            job.response.errorMessage = e.getMessage();
            return false;
        }
        
        return true;
    }
    
    private static boolean readStandAlone(InputStream is, RequestJob job) throws Exception
    {
        StringBuffer buff = new StringBuffer();
        String LEN_TOK = "Content-Length:";

        StringBuffer strLen = new StringBuffer();
        boolean isLen = false;
        boolean isDigit = false;

        int index = 0;
        int counter = 0;
        int len = 0;
        int emptyLineCounter = 0;

        while (true)
        {
            int n = is.read();

            if (n == -1)
            {
                return false;
            }

            char ch = (char) n;

            counter++;
            buff.append(ch);

            if (isLen)
            {
                if (Character.isDigit(ch))
                {
                    isDigit = true;
                    strLen.append(ch);
                }
                else
                {
                    if (isDigit)
                    {
                        if (n == 10)
                        {
                            emptyLineCounter++;
                        }
                        else
                        {
                            if (n != 13)
                                emptyLineCounter = 0;
                        }
                        if (emptyLineCounter == 2)
                            break;
                    }
                }
            }
            else
            {
                if (ch == LEN_TOK.charAt(index))
                {
                    index++;
                }
                else
                {
                    index = 0;
                }

                if (index == LEN_TOK.length())
                {
                    isLen = true;
                }
            }
        }

        // check for 200 OK
        if (buff.toString().indexOf("200 OK") == -1)
        {
            return false;
        }
        len = Integer.parseInt(strLen.toString().trim());

        return readContent(is, len, job);
    }

    protected static int readBytes(InputStream is, byte[] buff, int offset, int len) throws Exception
    {
        int bytesRead = 0;
        int MAX_CHUNK = 4096 * 8;
        while (bytesRead < len)
        {
            int nextChunk = len - bytesRead;
            if (nextChunk > MAX_CHUNK)
                nextChunk = MAX_CHUNK;
            int count = is.read(buff, offset, nextChunk);

            if (count < 0)
                break;
            bytesRead += count;
            offset += count;
        }
        return bytesRead;
    }
    
    private static boolean handleLongSocket(TnSocketConnection socketConnection, RequestJob job, ICommStreamHandler streamHandler)
    {
        OutputStream os = null;
        InputStream is = null;
        try
        {
            job.response.progress = ICommCallback.PROGRESS_SENDING;
            job.notifyProgress();
            
            if (job.response.isCanceled)
                return false;

            os = socketConnection.openOutputStream();
            job.os = os;
            job.isSocketAlive = true;
            
            if(job.response.requestData != null)
            {
                writeLongLive(os, job.response.requestData);
            }
            else if(streamHandler != null)
            {
                boolean isSuccessful = streamHandler.send(os, job.response, job.commCallBack);
                if(!isSuccessful)
                {
                    return false;
                }
            }
        }
        catch (Throwable e)
        {
            Logger.log(SocketStreamHandler.class.getName(), e);
            
            job.isSocketAlive = false;
            job.close();//will retry the network.
            
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
            
            is = socketConnection.openInputStream();
            if (is != null)
            {
                job.is = is;

                job.response.progress = ICommCallback.PROGRESS_RECEIVING;
                job.notifyProgress();
                
                try
                {
                    if(streamHandler != null)
                    {
                        return streamHandler.receive(is, -1, job.response, job.commCallBack);
                    }
                    else
                    {
                        return readLongLive(is, job);
                    }
                }
                catch (Throwable e)
                {
                    Logger.log(SocketStreamHandler.class.getName(), e);
                    
                    job.response.status = ICommCallback.EXCEPTION_PARSE;
                    job.response.errorMessage = e.getMessage();
                    job.response.exception = e;
                    
                    return false;
                }
            }
            else
            {
                job.response.status = ICommCallback.RESPONSE_ERROR;
                job.response.errorMessage = socketConnection.toString();
            }
        }
        catch (Throwable e)
        {
            Logger.log(SocketStreamHandler.class.getName(), e);
            
            job.isSocketAlive = false;
            job.close();//will retry the network.
            
            job.response.status = ICommCallback.EXCEPTION_RECEIVE;
            job.response.errorMessage = e.getMessage();
            job.response.exception = e;
            
            return false;
        }

        return true;
    }
    
    private static int getInt(byte[] buf, int offset)
    {
        if (buf == null || offset + 4 > buf.length)
        {
            return -1;
        }

        // int value
        int ret = 0;
        int tmp = 0;
        for (int i = 3; i >= 0; i--)
        {
            tmp = buf[i + offset];
            if (tmp < 0)
            {
                tmp += 128;
                tmp |= 0x80;
            }

            ret |= (tmp << (3 - i) * 8);
        }
        return ret;
    }
    
    private static void writeLongLive(OutputStream os, byte[] buff) throws Exception
    {
        // header - length
        int x = buff.length;

        // [XR]: the highest byte is the protocol flag
        // x x x x x x 1 1 (bit sequence from high to low)
        // if bit 0 == 1: UTF-8 encoding protocol
        // if bit 1 == 1: attach time stamp after total length
        x |= 0x03000000;

        // write length and flag
        for (int i = 0; i < 4; i++)
        {
            int b = x & 0xFF;
            os.write(b);
            x >>= 8;
        }

        // write timestamp
        long timestamp = System.currentTimeMillis();
        for (int i = 0; i < 8; i++)
        {
            int b = (int) timestamp & 0xFF;
            os.write(b);
            timestamp >>= 8;
        }

        os.write(buff);
        os.flush();
    }

    protected static boolean readLongLive(InputStream is, RequestJob job) throws Exception
    {
        byte[] lenBuf = new byte[4];
        int nRet = StreamHandler.readBytes(is, lenBuf, 0, 4);
        if (nRet != 4)
            return false;
        int len = DataUtil.readInt(lenBuf, 0);

        boolean b = readContent(is, len, job);

        return b;
    }

    protected static boolean readContent(InputStream is, int len, RequestJob job) throws Exception
    {
        if (len == -1)
            return false;

        byte[] lenBuf = new byte[4];
        int nRet = StreamHandler.readBytes(is, lenBuf, 0, 4);
        if (nRet == -1)
            return false;

        int chunkRespCode = DataUtil.readInt(lenBuf, 0);

        boolean isCompressed = (chunkRespCode == StreamHandler.RESPONSE_STREAMING_COMPRESSED || chunkRespCode == StreamHandler.RESPONSE_COMPRESSED);

        if (chunkRespCode == StreamHandler.RESPONSE_STREAMING || chunkRespCode == StreamHandler.RESPONSE_STREAMING_COMPRESSED)
        {
            int offset = 4;

            while (offset < len)
            {
                if (job.response.isCanceled)
                    break;

                // read chunk n length
                nRet = StreamHandler.readBytes(is, lenBuf, 0, 4);
                if (nRet == -1)
                    return false;

                int buffLen = DataUtil.readInt(lenBuf, 0);
                offset += 4;
                if (buffLen <= 0)
                    continue;

                int uncompressedLen = 0;
                if (isCompressed)
                {
                    nRet = StreamHandler.readBytes(is, lenBuf, 0, 4);
                    if (nRet == -1)
                        return false;
                    uncompressedLen = DataUtil.readInt(lenBuf, 0);
                    offset += 4;

                }

                // read chunk n content
                byte[] data = new byte[buffLen];
                nRet = StreamHandler.readBytes(is, data, 0, buffLen);
                if (nRet == -1)
                    return false;
                offset += buffLen;

                if (isCompressed)
                {
                    data = StreamHandler.uncompress(data, uncompressedLen, job.comm.ioManager);
                }

                if (job.response.isCanceled)
                    return false;

                job.response.responseData = data;

                if (job.response.isCanceled)
                    break;

                if (job.commCallBack != null)
                {
                    job.commCallBack.handleChild(job.response);
                }
            }
        }
        else
        {
            byte[] data;
            if (isCompressed)
            {
                nRet = StreamHandler.readBytes(is, lenBuf, 0, 4);
                if (nRet == -1)
                    return false;
                int uncompressedLen = DataUtil.readInt(lenBuf, 0);
                int compressedLen = len - 8;

                byte[] compressData = new byte[compressedLen];
                nRet = StreamHandler.readBytes(is, compressData, 0, compressedLen);
                if (nRet == -1)
                    return false;

                data = StreamHandler.uncompress(compressData, uncompressedLen, job.comm.ioManager);

            }
            else
            {
                int bytesRead = nRet;
                data = new byte[len];
                System.arraycopy(lenBuf, 0, data, 0, bytesRead);

                nRet = StreamHandler.readBytes(is, data, bytesRead, len - bytesRead);
                if (nRet == -1)
                    return false;
            }

            job.response.responseData = data;

            if (job.response.isCanceled)
                return false;

            if (job.commCallBack != null)
            {
                job.commCallBack.handleChild(job.response);
            }
        }

        if (job.response.isCanceled)
            return false;

        return true;
    }
}
