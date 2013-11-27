/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RouteDataFactory.java
 *
 */
package com.telenav.datatypes.route;


/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 20, 2010
 */
public class RouteDataFactory
{
    protected static RouteDataFactory instance = new RouteDataFactory();

    private static int initCount;
    
    protected RouteDataFactory()
    {

    }

    public static RouteDataFactory getInstance()
    {
        return instance;
    }

    public synchronized static void init(RouteDataFactory factory)
    {
        if (initCount >= 1)
            return;

        instance = factory;
        initCount++;
    }
    
    public DecimatedRoute createDecimatedRoute(byte[] buff)
    {
        return new DecimatedRoute(buff);
    }
    
    public Route createRoute()
    {
        return new Route();
    }
    
    public RouteEdge createRouteEdge()
    {
        return new RouteEdge();
    }
    
    public Segment createsSegment()
    {
        return new Segment();
    }
}
