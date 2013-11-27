/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Queue.java
 *
 */
package com.telenav.util;

/**
 * A simple thread safe Queue
 * 
 * @author zhdong
 */
public class Queue
{
    private int     size    = 0;

    // Head
    private Entry   head    = new Entry();

    private Entry   tail    = head;

    // Free Head
    private Entry   free    = new Entry();

    /**
     * Constructor
     */
    public Queue()
    {
    }

    /**
     * Check if empty
     * 
     * @return
     */
    public synchronized boolean isEmpty()
    {
        return size() <= 0;
    }

    /**
     * Get queue size
     * 
     * @return
     */
    public int size()
    {
        return size;
    }

    /**
     * Push
     * 
     * @param message
     */
    public synchronized void push(Object message)
    {
        if (message != null)
        {
            push0(message);
            notifyAll();
        }
    }
    
    /**
     * Clear all elements in queue
     */
    public synchronized void reset()
    {
        while (size > 0)
        {
            this.pop0();
        }
    }

    private Entry getFree()
    {
        synchronized (free)
        {
            if (free.next == null)
            {
                return new Entry();
            }
            else
            {
                Entry entry = free.next;
                free.next = entry.next;
                entry.next = null;
                return entry;
            }
        }
    }

    private void recycle(Entry entry)
    {
        synchronized (free)
        {
            entry.recycle();
            entry.next = free.next;
            free.next = entry;
        }
    }

    protected synchronized void push0(Object obj)
    {
        Entry entry = getFree();
        entry.value = obj;
        tail.next = entry;
        tail = entry;
        size++;
    }

    protected synchronized Object pop0()
    {
        Entry entry = head.next;
        Object obj = entry.value;
        head.next = entry.next;
        if (tail == entry)
            tail = head;
        size--;
        recycle(entry);
        return obj;
    }

    protected synchronized Object pop1()
    {
        while (isEmpty())
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                return null;
            }
        }
        return pop0();
    }

    /**
     * Pop, block if the queue is empty
     * 
     * @return
     */
    public synchronized Object pop()
    {
        return pop1();
    }

    /**
     * Pop, block until timeout
     * 
     * @param timeout
     * @return
     */
    public synchronized Object pop(long timeout)
    {
        if (timeout <= 0)
            return pop1();

        long start = System.currentTimeMillis();
        int wait = 500;
        if (timeout < wait)
            wait = 50;

        while (isEmpty())
        {
            try
            {
                wait(wait);
            }
            catch (InterruptedException e)
            {
                return null;
            }
            if (!isEmpty())
                break;
            else if (System.currentTimeMillis() - start > timeout)
            {
                return null;
            }
        }
        return pop0();
    }

    static class Entry
    {

        Entry   next    = null;

        Object  value   = null;

        Entry()
        {
        }

        Entry(Object value, Entry next)
        {
            this.value = value;
            this.next = next;
        }

        void recycle()
        {
            this.next = null;
            this.value = null;
        }

        public String toString()
        {
            if (value == null)
            {
                return "";
            }
            return value.toString();
        }
    }
}
