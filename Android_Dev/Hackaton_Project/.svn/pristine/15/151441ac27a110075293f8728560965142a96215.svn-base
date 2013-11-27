/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * GpsReader.java
 *
 */
package com.telenav.gps;

import java.util.Vector;

import com.telenav.location.ITnLocationListener;
import com.telenav.location.TnCriteria;
import com.telenav.location.TnLocation;
import com.telenav.location.TnLocationManager;
import com.telenav.location.TnLocationProvider;
import com.telenav.logger.Logger;

/**
 * <b>
 * The GpsReader is a delegate for read gps from device.
 * </b>
 * 
 *@author bduan
 *@date July 30, 2010
 */
public class GpsService implements ITnLocationListener, Runnable
{
    // status codes :
    public final static int STATUS_SUCCESS                      = 0;
    
    public final static int STATUS_TIMEOUT                      = 1;
    
    final static int DEFAULT_TIMEOUT                            = 32000; //32s
    
    final static int DEFAULT_SAFE_TIME                          = 45000;// RM - default 45 seconds
    
    final static byte DEFAULT_GPS_BUFFER_SIZE                   = 5;
    
    final static int MIN_TIME = 0;
    
    final static int MIN_DISTANCE = 0;
    
    final static int MAX_AGE = 0;
    
    protected int min_time = MIN_TIME;
    
    protected int min_distance = MIN_DISTANCE;
    
    protected int max_age = MAX_AGE;
    
    protected String sourceType = "";

    protected TnLocation[] positionData;

    protected TnLocation tempGpsData;

    protected Object dataMutex = new Object();

    protected Object serviceMutex = new Object();

    protected TnGpsFilter gpsFilter = new TnGpsFilter();

    protected TnCriteria criteria;

    protected int latestFixIndex;
    
    protected int currentStatus = -1;

    protected long lastFixTime = 0;

    protected long safetyTimeout = DEFAULT_SAFE_TIME;
    
    protected Vector gpsListeners;

    protected Vector callbackQueue;
    protected Thread callbackThread;
    protected boolean isStarted;
    
    /**
     * Constructor of gps Service
     * 
     * @param sourceType sourceType
     * @param pool  ThreadPool
     * @param criteria  The criteria of the provider.
     */
    public GpsService(String sourceType, TnCriteria criteria)
    {
        if(sourceType == null || sourceType.length() == 0)
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "No sourceType Set");
        }
        
        this.sourceType = sourceType;
        this.criteria = criteria;
        
        callbackQueue = new Vector();
        
        setBufferSize(DEFAULT_GPS_BUFFER_SIZE);  
    }
    
    /**
     * Get last known fix from location provider.
     * 
     * @return last gps fix.
     */
    public TnLocation getLastKnownFix()
    {
        TnLocation location = null;
        try
        {
            location = TnLocationManager.getInstance().getLastKnownLocation(criteria);
        }catch (Exception e) 
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Get Last known fix failed.");
        }
        
        return location;
    }
    
    public void run()
    {
        while(isStarted)
        {
            try
            {
                if (!this.callbackQueue.isEmpty())
                {
                    for(int i = 0; i < callbackQueue.size(); i++)
                    {
                        GpsServiceCallBackBean bean = (GpsServiceCallBackBean) callbackQueue.elementAt(i);
                        
                        if(!isStarted)
                        {
                            break;
                        }
                        
                        int fixCounter = 0;
                        long currTime = System.currentTimeMillis();
                        if (currTime <= bean.eTime)
                        {
                            fixCounter = getFixes(bean.numFixes, bean.data);
                            if (fixCounter == bean.numFixes)
                            {
                                callbackQueue.removeElementAt(i);
                                bean.gpsCallback.requestCompleted(bean.data, fixCounter, STATUS_SUCCESS);
                            }
                        }
                        else
                        {
                            callbackQueue.removeElementAt(i);
                            bean.gpsCallback.requestCompleted(bean.data, fixCounter, STATUS_TIMEOUT);
                        }
                    }
                    
                    if(!isStarted)
                    {
                        break;
                    }
                    
                    synchronized (serviceMutex)
                    {
                        serviceMutex.wait(250);
                    }
                }
                else
                {
                    // wait for 5 seconds as need to check GPS status for jump start
                    synchronized (serviceMutex)
                    {
                        serviceMutex.wait(5000);
                    }
                }

                if (isStarted) checkStatus();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }    
    
    /**
     * get current status.
     * @return status.
     */
    public int getGpsStatus()
    {
        return this.currentStatus;
    }

    /**
     * Get gpsType
     * 
     * @return type
     */
    public String getSourceType()
    {
        return this.sourceType;
    }
    
    /**
     * start gps service.
     */
    public void start()
    {
        synchronized (serviceMutex)
        {
            if (isStarted)
            {
                return;
            }

            isStarted = true;

            this.lastFixTime = System.currentTimeMillis();

            if (this.callbackThread == null || !this.callbackThread.isAlive())
            {
                this.callbackThread = new Thread(this, "GpsService-Thread");
                this.callbackThread.start();
            }

            if (sourceType.equals(TnLocationManager.ALONGROUTE_PROVIDER) && TnLocationManager.getInstance().getProvider(sourceType) == null)
            {
                TnLocationManager.getInstance().addProvider(sourceType, new AlongRouteLocationProvider(sourceType));
            }

            if (criteria == null)
            {
                TnLocationManager.getInstance().requestLocationUpdates(sourceType, (long) min_time, (float) MIN_DISTANCE, DEFAULT_TIMEOUT, MAX_AGE, this);
            }
            else
            {
                sourceType = TnLocationManager.getInstance().requestLocationUpdates(criteria, (long) min_time, (float) MIN_DISTANCE, DEFAULT_TIMEOUT, MAX_AGE, this);
            }
        }
    }

    /**
     * stop gps service.
     */
    public void stop()
    {
        synchronized (serviceMutex)
        {
            if(!isStarted)
            {
                return;
            }
            
            isStarted = false;
            serviceMutex.notifyAll();
            
            TnLocationManager.getInstance().reset(sourceType);
            
            this.removeAllListeners();
            
            this.callbackQueue.removeAllElements();
        }
    }

    /**
     * Set another criteria, and it will trigger re-init() action.
     * 
     * @param criteria
     */
    public void setCriteria(TnCriteria criteria)
    {
        if (criteria != null && ( this.criteria == null || !this.criteria.equals(criteria)))
        {
            this.criteria = criteria;
            restart(true);
        }
    }
    
    /**
     * get current criteria.
     * @return criteria
     */
    public TnCriteria getCriteria()
    {
        return criteria;
    }
    
    public void onLocationChanged(TnLocationProvider provider, TnLocation location)
    {
        this.currentStatus = TnLocationProvider.AVAILABLE;
        this.addGpsData(location);
    }
    
    /**
     * Get specific numbers fresh gps fix.
     * 
     * It will callback once ready.
     * 
     * @param numFixes
     * @param data
     * @param timeout
     * @param gpsCallback
     */
    public void getFixes(int numFixes, 
            TnLocation [] data, 
            long timeout, 
            IGpsCallback gpsCallback)
    {
        if (numFixes < 1 || numFixes > data.length) return;
        
        GpsServiceCallBackBean bean = new GpsServiceCallBackBean();
        bean.gpsCallback = gpsCallback;
        bean.numFixes = numFixes;
        bean.data = data;
        
        long currTime = System.currentTimeMillis();
        bean.eTime = currTime + timeout;
        
        synchronized (serviceMutex)
        {
            callbackQueue.addElement(bean);
            serviceMutex.notifyAll();
        }
    }

    /**
     * Get specific number of gps fixes which are cached in pool
     * 
     * @param numFixes
     * @param data
     * @return number
     */
    public int getFixes(int numFixes, TnLocation [] data)
    {
        return getFixes(numFixes, data, dataMutex, this.latestFixIndex, getBufferSize(), this.positionData);
    }
    
    public void onStatusChanged(TnLocationProvider provider, int status)
    {
        currentStatus = status;
    }
    
    public void addListener(IGpsListener gpsListener)
    {
        if(this.gpsListeners == null)
        {
            this.gpsListeners = new Vector();
        }
        
        this.gpsListeners.addElement(gpsListener);
    }
    
    public void removeListener(IGpsListener gpsListener)
    {
        if(this.gpsListeners == null)
            return;
        
        this.gpsListeners.removeElement(gpsListener);
    }
    
    public void removeAllListeners()
    {
        if(this.gpsListeners == null)
            return;
        
        this.gpsListeners.removeAllElements();
    }
    
    protected void checkStatus()
    {
        if(!sourceType.equals(TnLocationManager.ALONGROUTE_PROVIDER) && !sourceType.equals(TnLocationManager.MVIEWER_PROVIDER))
        {
            if (System.currentTimeMillis() - lastFixTime  >= safetyTimeout)
            {
                this.restart(false);
                this.lastFixTime = System.currentTimeMillis();
            }
        }
    }
    
    protected void addGpsData(TnLocation data)
    {
        this.lastFixTime = System.currentTimeMillis();
            
        if (gpsFilter.eliminateGpsNoise(data))
        {
            synchronized(dataMutex)
            {
                positionData[latestFixIndex].set(data);
                latestFixIndex ++;
                if (latestFixIndex >= positionData.length)
                    latestFixIndex = 0;
            }
            
            if(gpsListeners != null)
            {
                for (int i = 0; i < gpsListeners.size(); i++)
                {
                    try
                    {
                        IGpsListener gpsListener = (IGpsListener) gpsListeners.elementAt(i);
                        gpsListener.gpsDataArrived(data);
                    }
                    catch (Throwable t)
                    {
                        t.printStackTrace();
                    }
                }
            }
        }
        else
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "GPS data is eliminated");
        }
    }
    
    protected int getBufferSize()
    {
        int size = 0;
        if (this.positionData != null) 
            size = this.positionData.length;
        return size;
    }

    protected void setBufferSize(int size)
    {
        if (getBufferSize() != size)
        {
            synchronized (dataMutex)
            {
                //recreate the buffers
                this.positionData = new TnLocation[size];
                for (int i=0; i<size; i++)
                    this.positionData[i] = new TnLocation("");
                this.latestFixIndex = 0;
            }
        }
    }
    
    protected int getFixes(int numFixes, TnLocation [] data, 
            Object dataMutex, int latestFixIndex, int buffSize, TnLocation[] positionData)
    {
        int fixCounter = 0;
        synchronized (dataMutex)
        {
            for (int i = latestFixIndex - 1; i >= 0; i--)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    data[fixCounter].set(pos);
                    fixCounter++;
                    if (fixCounter >= numFixes || fixCounter >= data.length) return fixCounter;
                }
            }
            
            for (int i = buffSize - 1; i >= latestFixIndex ; i--)
            {
                TnLocation pos = positionData[i];
                if (pos.isValid())
                {
                    data[fixCounter].set(pos);
                    fixCounter++;
                    if (fixCounter >= numFixes || fixCounter >= data.length) return fixCounter;
                }
            }
        }
        
        return fixCounter; 
    }
    
    protected void restart(boolean quickly)
    {
        this.stop();

        if(!quickly)
        {
            try
            {
                Thread.sleep(1500);
            }
            catch (InterruptedException e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }

        this.start();
    }
    
    public void setMinTime(int minTime)
    {
        if (minTime < 0)
        {
            return;
        }
        this.min_time = minTime;
    }

    static class GpsServiceCallBackBean
    {
        public int numFixes;
        public TnLocation [] data; 
        public long eTime;
        public IGpsCallback gpsCallback;
    }
}
