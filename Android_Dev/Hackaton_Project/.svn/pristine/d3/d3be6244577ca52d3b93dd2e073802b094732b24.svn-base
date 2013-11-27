/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractTnMockLocationProvider.java
 *
 */
package com.telenav.location;

import com.telenav.logger.Logger;

/**
 * This provide a simple to create a mock test location provider. <br />
 * You can extend this class when create MViewer, AlongRoute etc. location provider.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
public abstract class AbstractTnMockLocationProvider extends TnLocationProvider implements Runnable
{
    private Object mutex = new Object();

    protected boolean isRunning;

    protected boolean isCancelled;

    protected long minTime;

    protected ITnLocationListener listener;

    protected int currentStatus = TEMPORARILY_UNAVAILABLE;

    protected TnLocation lastKnownLocation;

    /**
     * construct the mock location provider.
     * 
     * @param name
     */
    public AbstractTnMockLocationProvider(String name)
    {
        super(name);
    }

    /**
     * Get the last known location.
     * 
     * For the mock location provider, the timeout of last location is 30 minutes.
     */
    protected TnLocation getLastKnownLocation() throws TnLocationException
    {
        if (lastKnownLocation != null && (System.currentTimeMillis() - lastKnownLocation.getTime()) < 30 * 60 * 1000)
        {
            return lastKnownLocation;
        }

        lastKnownLocation = getLocation(30);

        return lastKnownLocation;
    }

    protected void requestLocationUpdates(long minTime, float minDistance, int timeout, int maxAge, ITnLocationListener listener)
    {
        this.minTime = minTime;
        this.listener = listener;

        if (this.minTime < 1000)
        {
            this.minTime = 1000;
        }

        if (!isRunning)
        {
            Thread t = new Thread(this, "-MockLocationProvider-");
            t.start();
        }
    }

    protected void reset()
    {
        isCancelled = true;
        this.listener = null;

        synchronized (mutex)
        {
            mutex.notify();
        }
    }

    /**
     * the delegate method of {@link #getLocation(int)}.
     * 
     * @param timeout a timeout value in seconds.
     * @return TnLocation a TnLocation object
     * 
     * @throws TnLocationException if the location couldn't be retrieved or if the timeout period expired.
     */
    protected abstract TnLocation getLocationDelegate(int timeout) throws TnLocationException;
    
    /**
     * Retrieves a Location associated with this class. If no result could be retrieved, a LocationException is thrown.
     * If the location can't be determined within the timeout period specified in the parameter, the method shall throw
     * a LocationException.
     * 
     * @param timeout a timeout value in seconds.
     * @return TnLocation a TnLocation object
     * 
     * @throws TnLocationException if the location couldn't be retrieved or if the timeout period expired.
     */
    protected final TnLocation getLocation(int timeout) throws TnLocationException
    {
        return getLocationDelegate(30 * 1000);
    }

    public void run()
    {
        isRunning = true;

        notifyStatus(TEMPORARILY_UNAVAILABLE);

        while (!isCancelled)
        {
            TnLocation location = null;

            try
            {
                location = getLocation(30);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            
            synchronized (mutex)
            {
                try
                {
                    if (location == null)
                    {
                        notifyStatus(OUT_OF_SERVICE);
                    }
                    else
                    {
                        location.setValid(true);
                        notifyStatus(AVAILABLE);

                        this.lastKnownLocation = location;

                        if (this.listener != null)
                        {
                            this.listener.onLocationChanged(this, location);
                        }
                    }
                }
                catch (Throwable e)
                {
                    Logger.log(this.getClass().getName(), e);

                    notifyStatus(TEMPORARILY_UNAVAILABLE);
                }

                try
                {
                    mutex.wait(this.minTime);
                }
                catch (Throwable e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
        }

        notifyStatus(OUT_OF_SERVICE);

        isRunning = false;
    }

    /**
     * notify the status change of location provider.
     * 
     * @param status - current status.
     */
    private void notifyStatus(int status)
    {
        if (this.currentStatus != status)
        {
            if (this.listener != null)
            {
                try
                {
                    this.listener.onStatusChanged(this, status);
                }
                catch (Throwable e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
        }

        this.currentStatus = status;
    }
}
