/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * AbstractDsrJob.java
 *
 */
package com.telenav.dsr.impl;

import java.io.IOException;
import java.util.Vector;

import com.telenav.logger.Logger;

/**
 * base class to handle the end pointer check and encoding.
 * @author xinrongl (xinrongl@telenav.com)
 * @date Feb 14, 2011
 */

public abstract class AbstractDsrJob
{
    protected boolean isStarted;
    protected boolean isCancelled;
    
    private Vector frames = new Vector();
    private Object mutex = new Object();

    public AbstractDsrJob()
    {
    }

    public void addFrame(byte[] buffer)
    {
        synchronized (mutex)
        {
            frames.addElement(buffer);
            
            mutex.notify();
        }
        
        if (!isStarted)
        {
            isStarted = true;
            isCancelled = false;
            new Thread(new DsrTask()).start();            
        }        
    }
    
    public int getFrameSize()
    {
        if(frames == null)
        {
            return 0;
        }
        else
        {
            return frames.size();
        }
    }
    
    public void addFrame(int b)
    {
        addFrame(new byte[]{(byte) b});
    }
    
    public void cancel()
    {
        isCancelled = true;
        
        synchronized (mutex)
        {
            mutex.notify();
        }
        
        isStarted = false;
    }
    
    public boolean isCancelled()
    {
        return isCancelled;
    }
    
    protected abstract void handleFrame(byte[] tmpBuffer, int off, int len) throws IOException;
    
    class DsrTask implements Runnable
    {
        public void run()
        {
            while (true)
            {
                if(frames == null || frames.size() == 0)
                {
                    if(isCancelled)
                    {
                       break; 
                    }
                    
                    synchronized(mutex)
                    {
                        try
                        {
                            mutex.wait(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                
                byte[] frame = null;
                synchronized(mutex)
                {
                    if(frames != null && frames.size() > 0)
                    {
                        frame = (byte[])frames.firstElement();
                        frames.removeElementAt(0);
                    }
                    else if(isCancelled)
                    {
                        break;
                    }
                }
                try
                {
                    if (frame != null)
                    {
                        handleFrame(frame, 0, frame.length);
                    }
                }
                catch (IOException e)
                {
                    Logger.log(this.getClass().getName(), e, "AbstractDsrJob#Run");
                }
            }
            
            synchronized (mutex)
            {
                frames.removeAllElements();
            }
        
        }        
    }
}
