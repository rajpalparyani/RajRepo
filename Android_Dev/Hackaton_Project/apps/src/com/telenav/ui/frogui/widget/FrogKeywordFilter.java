/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogFilter.java
 *
 */
package com.telenav.ui.frogui.widget;

import java.util.Vector;

/**
 * It support filter mechanism for List or some other components
 * 
 *@author bduan
 *@date 2010-10-12
 */
public abstract class FrogKeywordFilter
{
    protected Vector requests;
    
    private final Object requestQueueLock = new Object();
    private final Object requestThreadLock = new Object();
    
    private static final String THREAD_NAME = "Filter";
    private static final int MAX_WAIT_TIME = 10 * 1000;
    private static final int MIN_INTERVAL_TIME = 500;
    
    private RequestThread requestHandler;
    private boolean isGotNewRequest = false;
    private int minInterval = MIN_INTERVAL_TIME;
    private long start = -1L;
    
    /**
     * Filter the keyword in a thread.
     * 
     * @param keyword
     */
    public final void filter(String keyword)
    {
        start = System.currentTimeMillis();
        if (requestHandler == null || !requestHandler.isAlive())
        {
            requestHandler = new RequestThread(THREAD_NAME);
            requestHandler.start();
        }
        
        if(requests == null)
            requests = new Vector();
        
        FilterRequest request = new FilterRequest();
        request.keyword = keyword;
        request.id = System.currentTimeMillis();
        
        synchronized (requestQueueLock)
        {
            requests.removeAllElements();
            requests.addElement(request);
        }
        
        isGotNewRequest = true;
        synchronized (requestThreadLock)
        {
            requestThreadLock.notifyAll();
        }

    }
    
    /**
     * If you want to constraint the search frequency, 
     * such as only start search 500 millisecond after user's last char input.
     * please set the interval to constraint. The default interval is 500 millisecond.
     * @param minInterval interval(MUST be positive number).
     */
    public void setMinInterval(int minInterval)
    {
        if(minInterval > 0)
        {
            this.minInterval = minInterval;
        }
    }
    
    /**
     * Get the minInterval value, 
     * please see {@link #setMinInterval(int interval)} for detail.
     * @return minimum interval value.
     */
    public int getMinInterval()
    {
        return minInterval;
    }
    
    /**
     * reset the keyword filter.
     */
    public void reset()
    {
        isGotNewRequest = false;
        if(requestHandler != null)
        {
            requestHandler.reset();
            synchronized (requestThreadLock)
            {
                requestThreadLock.notifyAll();
            }
        }
    }
    
    /**
     * Please block this method if can't get results immediately.
     * 
     * @param constraint
     * @return filtered results
     */
    protected abstract FilterResults performFilter(FilterRequest constraint);
    
    protected abstract void publishResults(FilterResults results);
    
    protected static class FilterRequest
    {
        public String keyword;
        
        public long id;
    }
    
    protected static class FilterResults
    {
        public FilterResults()
        {
            // nothing to see here
        }
        
        public String constraint;
        
        /**
         * <p>
         * Contains all the values computed by the filtering operation.
         * </p>
         */
        public Object values;

        /**
         * <p>
         * Contains the number of values computed by the filtering operation.
         * </p>
         */
        public int count;
    }
    
    private class RequestThread extends Thread
    {
        boolean isStop = false;
        
        RequestThread(String name)
        {
            super(name);
        }
        
        public void reset()
        {
            isStop = true;
        }
        
        public void run()
        {
            while(!isStop)
            {
                if(isStop)
                    break;
                
                if (requests == null || requests.size() <= 0)
                {
                    synchronized (requestThreadLock)
                    {
                        try
                        {
                            requestThreadLock.wait(MAX_WAIT_TIME);
                        }
                        catch (InterruptedException e)
                        {

                        }
                    }
                }
                
                if(isStop)
                    break;

                if(System.currentTimeMillis() - start > MAX_WAIT_TIME)
                {
                    break;
                }
                
                while (isGotNewRequest && minInterval > 0)
                {
                    synchronized (requestThreadLock)
                    {
                        isGotNewRequest = false;
                        try
                        {
                            requestThreadLock.wait(minInterval);
                        }
                        catch (InterruptedException e)
                        {

                        }
                    }
                }
                
                FilterRequest requestData = null;
                
                synchronized (requestQueueLock)
                {
                    int size = requests.size();
                    if(size <= 0)
                        continue;
                    requestData = (FilterRequest) requests.elementAt(size - 1);
                }
                
                FilterResults filterResult = performFilter(requestData);
                
                if(isStop)
                    break;
                
                synchronized (requestQueueLock)
                {
                    int size = requests.size();
                    if(size <= 0)
                        continue;
                    
                    FilterRequest latestRequest = (FilterRequest) requests.elementAt(size - 1);
                    if(requestData.id == latestRequest.id)
                    {
                        requests.removeAllElements();
                    }
                    else
                    {
                        continue;
                    }
                }
                
                if(isStop)
                    break;
                
                if(filterResult == null)
                    continue;
                
                filterResult.constraint = requestData.keyword;
                publishResults(filterResult);
                
                if(isStop)
                    break;
            }
        }
    }
}
