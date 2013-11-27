/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TrafficDataFactory.java
 *
 */
package com.telenav.datatypes.traffic;


/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 20, 2010
 */
public class TrafficDataFactory
{
    protected static TrafficDataFactory instance = new TrafficDataFactory();

    private static int initCount;
    
    protected TrafficDataFactory()
    {

    }

    public static TrafficDataFactory getInstance()
    {
        return instance;
    }

    public synchronized static void init(TrafficDataFactory factory)
    {
        if (initCount >= 1)
            return;

        instance = factory;
        initCount++;
    }
    
    public VectorTrafficEdge createTrafficEdge()
    {
        return new VectorTrafficEdge();
    }
    
    public TrafficIncident createTrafficIncident()
    {
        return new TrafficIncident();
    }
    
    public TrafficSegment createTrafficSegment()
    {
        return new TrafficSegment();
    }
}
