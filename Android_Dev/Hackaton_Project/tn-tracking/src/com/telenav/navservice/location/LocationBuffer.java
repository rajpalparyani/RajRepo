package com.telenav.navservice.location;

import java.util.Vector;

import com.telenav.location.TnLocation;

public class LocationBuffer
{
    protected Vector buffer = new Vector();
    protected int maxSize;
    protected ILocationListener listener;
    
    public static interface ILocationListener
    {
        public boolean locationArrived(TnLocation location);
    }
    
    public LocationBuffer(int maxSize, ILocationListener lis)
    {
        this.maxSize = maxSize;
        this.listener = lis;
    }
    
    public void addLocation(TnLocation loc)
    {
        boolean isAdded = true;
        if (this.listener != null)
        {
            isAdded = this.listener.locationArrived(loc);
        }
        if (isAdded)
        {
            synchronized(buffer)
            {
                buffer.addElement(loc);
                if (buffer.size() > maxSize)
                    buffer.removeElementAt(0);
            }
        }        
    }
    
    public int size()
    {
        return buffer.size();
    }
    
    public Vector extractAllLocations()
    {
        Vector v = new Vector();
        synchronized(buffer)
        {
            for (int i=0; i<buffer.size(); i++)
                v.addElement(buffer.elementAt(i));
            buffer.removeAllElements();
        }
        return v;
    }
    
    public Vector extractLocations(int count)
    {
        Vector v = new Vector();
        synchronized(buffer)
        {
            for (int i=0; i<count && buffer.size()>0; i++)
            {
                v.addElement(buffer.elementAt(0));
                buffer.removeElementAt(0);
            }
        }
        return v;
    }
    
    public void clear()
    {
        buffer.removeAllElements();
    }
}
