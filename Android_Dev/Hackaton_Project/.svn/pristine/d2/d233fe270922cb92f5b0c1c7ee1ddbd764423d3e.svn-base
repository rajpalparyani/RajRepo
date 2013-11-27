/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidMediaRecorder.java
 *
 */
package com.telenav.media.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Build;

import com.telenav.logger.Logger;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaException;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaRecorder;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 26, 2010
 */
class AndroidMediaRecorder extends TnMediaRecorder implements OnErrorListener, OnInfoListener
{
    protected Context context;
    
    protected String fileName;
    protected RandomAccessFile mediaTempFile;
    
    protected MediaRecorder mediaRecorder;
    
    protected RecorderTask recorderTask;
    
    public AndroidMediaRecorder(Context context, String format) throws TnMediaException
    {
        super(format);
        
        this.context = context;
        
        this.mediaRecorder = new MediaRecorder();
    }

    protected void setRecordSizeLimitDelegate() throws TnMediaException
    {
        try
        {
            this.mediaRecorder.setMaxFileSize(this.recordSizeLimit);
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void close() throws TnMediaException
    {
        try
        {
            this.mediaTempFile.close();
            
            this.mediaRecorder.release();
            
            this.mediaRecorder = null;
            this.mediaTempFile = null;
            
            this.recorderTask.cancel();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void prefetch() throws TnMediaException
    {
        try
        {
            this.mediaRecorder.setOnErrorListener(this);
            this.mediaRecorder.setOnInfoListener(this);

            this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            
            if (TnMediaManager.FORMAT_MP3.equals(this.audioFormat))
            {
                this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            }
            else if (TnMediaManager.FORMAT_3GPP.equals(this.audioFormat))
            {
                this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            }
            else
            {
                this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            }

            this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            this.mediaRecorder.setOutputFile(this.fileName);

            this.mediaRecorder.prepare();
            
            this.recorderTask = new RecorderTask();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void realize() throws TnMediaException
    {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8)
        {
            AndroidMediaUtil.requestAudioFocus(this.context);
        }
        
        FileOutputStream fos = null;
        try
        {
            this.fileName = "temp_media" + ".recorder";
            this.context.deleteFile(this.fileName);
            fos = this.context.openFileOutput(this.fileName, Context.MODE_WORLD_READABLE);
            this.fileName = this.context.getFilesDir().getAbsolutePath() + "/" + this.fileName;
            
            if (fos != null)
            {
                fos.close();
                fos = null;
            }
            
            File tmpFile = new File(this.fileName);
            this.mediaTempFile = new RandomAccessFile(tmpFile, "rw");
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void reset() throws TnMediaException
    {
        try
        {
            this.mediaRecorder.reset();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void start() throws TnMediaException
    {
        try
        {
            this.mediaRecorder.start();
            
            Thread t = new Thread(this.recorderTask);
            t.start();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void stop() throws TnMediaException
    {
        try
        {
            this.mediaRecorder.stop();
            
            if (Integer.parseInt(Build.VERSION.SDK) >= 8)
            {
                AndroidMediaUtil.abandonAudioFocus(this.context);
            }
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }

    public void commit() throws TnMediaException
    {
        try
        {
            this.os.flush();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);

            throw new TnMediaException(e.getMessage());
        }
    }
    
    public void onError(MediaRecorder mr, int what, int extra)
    {
        if(this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_ERROR, what + "," + extra);
        }
    }

    public void onInfo(MediaRecorder mr, int what, int extra)
    {
        if (this.mediaListener != null)
        {
            this.mediaListener.mediaUpdate(this, ITnMediaListener.ON_INFO, what + "," + extra);
        }
    }
    
    class RecorderTask implements Runnable
    {
        private final static int MAX_RECORD_TIME = 30 * 1000;
        
        private Object mutex = new Object();

        private boolean isCancelled = false;
        private long startTime;
        private byte[] buffer;
        
        public RecorderTask()
        {
            buffer = new byte[256];
        }

        public void cancel()
        {
            synchronized (mutex)
            {
                isCancelled = true;
                mutex.notify();
            }
        }

        public void run()
        {
            this.startTime = System.currentTimeMillis();
            
            if(mediaListener != null)
            {
                mediaListener.mediaUpdate(AndroidMediaRecorder.this, ITnMediaListener.ON_PREPARE, null);
            }

            while (!isCancelled && (System.currentTimeMillis() - this.startTime) < MAX_RECORD_TIME)
            {
                synchronized (mutex)
                {
                    try
                    {
                        if(mediaTempFile == null)
                            break;
                        
                        long length = mediaTempFile.length();
                        long position = mediaTempFile.getFilePointer();
                        if(length > position)
                        {
                            int len = mediaTempFile.read(buffer);
                            os.write(buffer, 0, len);
                            continue;
                        }
                        
                        mutex.wait(50);
                    }
                    catch (Exception e)
                    {
                        Logger.log(this.getClass().getName(), e, "AndroidMediaRecorder#RecorderTask");
                        break;
                    }
                }
            }
            
            if(mediaListener != null)
            {
                mediaListener.mediaUpdate(AndroidMediaRecorder.this, ITnMediaListener.ON_COMPLETION, null);
            }
            
            if(mediaListener != null)
            {
                mediaListener.mediaUpdate(AndroidMediaRecorder.this, ITnMediaListener.ON_CLOSE, null);
            }
        }
    }

    @Override
    public void abandonAudioFocus() throws TnMediaException
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAllowAbandon()
    {
        // TODO Auto-generated method stub
        return false;
    }
}
