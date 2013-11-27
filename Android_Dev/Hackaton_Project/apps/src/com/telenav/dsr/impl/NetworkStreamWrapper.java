/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NetorkStreamWrapper.java
 *
 */
package com.telenav.dsr.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.telenav.comm.ICommCallback;
import com.telenav.comm.ICommCallback.CommResponse;
import com.telenav.comm.ICommStreamHandler;
import com.telenav.dsr.DsrManager;
import com.telenav.dsr.IMetaInfoProvider;
import com.telenav.dsr.IRecordEventListener;
import com.telenav.dsr.util.DsrUtil;
import com.telenav.dsr.util.MetaUtil;
import com.telenav.dsr.util.StreamEncoder;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;
import com.telenav.util.Queue;

/**
 *@author bduan
 *@date 2010-10-28
 */
public class NetworkStreamWrapper extends ByteArrayOutputStream implements IRecordDataHandler, ICommStreamHandler, IRecordEventListener
{
    protected static int BUFFER_SIZE = 128;
    
    protected static int MAX_CHUNK = 4096 * 8;
    
    protected static long queueReadTimeOut = 1500;
    
    protected OutputStream requestOutputStream;
    
    protected InputStream inputStreamForResponse;
    
    protected boolean isRecordStarted = false;
    
    protected boolean isRecordStoped = false;
    
    protected boolean isRecordCancelled = false;
    
    protected boolean isUsePreviousCache = false;
    
    protected long timeout;
    
    protected int currentOffset = 0;

    protected Queue queue;
    
    protected StreamEncoder encoder;
    
    protected ByteArrayOutputStream bos;
    
    protected IMetaInfoProvider metaProvider;
    
    protected String currentRquestType;
    
    protected long currentSession;

    private ThreadPool threadPool;
    
    /**
     * constructor of Network Streamer.
     * @param provider
     * @param timeout
     */
    public NetworkStreamWrapper(IMetaInfoProvider provider, ThreadPool pool, long timeout)
    {
        queue = new Queue();
        bos = new ByteArrayOutputStream();
        encoder = new StreamEncoder(bos);
        metaProvider = provider;
        this.threadPool = pool;
        this.timeout = timeout;
    }
    
    public void writeData(byte[] bytes)
    {
        super.write(bytes, 0, bytes.length);

        if (!isRecordStoped && this.count - currentOffset >= 32)
        {
            int tmpLen = ((this.count - currentOffset) >> 5) << 5;
            PendingRequest request = new PendingRequest(this.buf, currentOffset, tmpLen);

            currentOffset += tmpLen;
            queue.push(request);
        }
    
    }

    public void writeHeader(int b)
    {
        writeData(new byte[]{ (byte) b });
    }

    public synchronized void resetQueue()
    {
        currentOffset = 0;

        // Q: why we synchronized queue here?
        // A: to make sure there is queue action between "queue.reset()" and "queue.push(request)"
        synchronized (queue)
        {
            queue.reset();
            int tmpLen = ((this.count - currentOffset) >> 5) << 5;
            if (tmpLen > 0)
            {
                PendingRequest request = new PendingRequest(this.buf, currentOffset, tmpLen);

                currentOffset += tmpLen;
                queue.push(request);
            }
        }

    }
    
    public boolean receive(InputStream is, long length, CommResponse response, ICommCallback callback) throws IOException
    {
        this.inputStreamForResponse = is;

        if (isRecordCancelled || currentSession != metaProvider.getRequestSession())
        {
            return true;
        }

        try
        {
            //Node resNode = null;
            byte[] lenB = new byte[4];
            readBytes(is, lenB, 0, 4);
            
            if(isRecordCancelled)
                return true;
            
            length = StreamEncoder.getInt(lenB, 0);
            byte[] responseBytes = new byte[(int) length];
            
            if(isRecordCancelled)
                return true;
            readBytes(is, responseBytes, 0, (int) length);
            
            if(isRecordCancelled)
                return true;
            
            DsrManager.getInstance().handleRespNode(responseBytes);
        }
        catch (Exception exception)
        {
            if (!isRecordCancelled)
            {
                if (exception instanceof IOException)
                {
                    throw (IOException) exception;
                }
            }
        }

        return true;
    }

    public boolean send(OutputStream os, CommResponse response, ICommCallback callback) throws IOException
    {
        this.requestOutputStream = os;
        try
        {
            int count = 0;
            while (!isRecordStarted && !isRecordStoped && count < 200)
            {
                count++;

                if(isRecordCancelled)
                {
                    return false;
                }
                
                Thread.sleep(50);
            }
            
            //if the record doesn't start, however has be end. 
            //We'll take it as exception, and return error soon.
            if(!isRecordStarted && isRecordStoped)
            {
                return false;
            }

            currentRquestType = metaProvider.getRequestType();
            currentSession = metaProvider.getRequestSession();
            
            long startTime = System.currentTimeMillis();
            PendingRequest request = null;

            if (isUsePreviousCache)
            {
                if (isRecordStoped)
                {
                    return true;
                }

                request = (PendingRequest) queue.pop(100);

                if (request == null)
                {
                    timeout();
                    return true;
                }
            }
            else
            {
                while (!isRecordStarted && !isRecordStoped && System.currentTimeMillis() - startTime + queueReadTimeOut < timeout)
                {
                    if(isRecordCancelled)
                    {
                        return false;
                    }
                    
                    Thread.sleep(50);
                }
            }

            if (request == null)
            {
                request = getRequest(queueReadTimeOut);
            }

            if (request == null)
            {
                // stop recognizing, timeout
                if (!isRecordStoped)
                {
                    timeout();
                }
                return true;
            }

            bos.reset();
            byte type = Byte.parseByte(metaProvider.getRequestType());
            byte[] meta = MetaUtil.generateMeta(null, type, metaProvider);
            encoder.writeMeta(meta, 0, meta.length);
            encoder.encode(request.bs, request.offset, request.length);

            // go on read
            while (!queue.isEmpty() || !isRecordStoped)
            {
                request = getRequest(queueReadTimeOut);
                if (request != null)
                {
                    encoder.encode(request.bs, request.offset, request.length);

                    if (bos.size() >= BUFFER_SIZE)
                    {
                        os.write(bos.toByteArray());
                        bos.reset();
                    }
                }
                
                if(isRecordCancelled)
                {
//                    if(callback != null)
//                        callback.networkError(null);
                    
                    return false;
                }
            }
            
            // write missing audio part which is not in the queue
            int remainLen = this.count - currentOffset;
            if (remainLen > 0)
            {
                encoder.encode(this.buf, currentOffset, remainLen);
            }
            encoder.endIncompleteAudio();
            
            encoder.endMetaEncoding();
            os.write(bos.toByteArray());
            
            bos.reset();
        }
        catch (Exception e)
        {
            if (!isRecordStoped)
            {
                if (e instanceof IOException)
                    throw (IOException) e;
                
                return false;
            }
        }

        return true;
    }

    protected int readBytes(InputStream is, byte[] buff, int offset, int len) throws Exception
    {
        int bytesRead = 0;

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
    
    protected PendingRequest getRequest(long queueReadTimeOut)
    {
        queueReadTimeOut = queueReadTimeOut / 5;

        for (int i = 0; i < 5; i++)
        {
            PendingRequest request = (PendingRequest) queue.pop(queueReadTimeOut);
            if (request != null)
            {
                return request;
            }
            else if (isRecordStoped)
            {
                return null;
            }
        }

        return null;
    }
    
    protected void timeout()
    {
        recordCanceled();
        DsrUtil.recordStatusUpdate(threadPool, IRecordEventListener.EVENT_TYPE_RECO_FAIL, PrimitiveTypeCache.valueOf(IRecordEventListener.EVENT_STOP_TYPE_TIMEOUT));
    }
    
    static class PendingRequest
    {
        byte[] bs;

        int offset;

        int length;

        PendingRequest(byte[] bs, int offset, int length)
        {
            this.bs = bs;
            this.offset = offset;
            this.length = length;
        }
    }
    
    public void recordCanceled()
    {
        this.isRecordCancelled = true;
    }

    public void recordStatusUpdate(int event, Object eventData)
    {
        if(event == IRecordEventListener.EVENT_TYPE_START)
        {
            this.isRecordStarted = true;
        }
        else if(event == IRecordEventListener.EVENT_TYPE_STOP)
        {
            this.isRecordStoped = true;
        }
    }
}
