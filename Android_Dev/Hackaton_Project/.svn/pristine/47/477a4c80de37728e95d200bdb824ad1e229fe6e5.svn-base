/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * RouteWrapperTest.java
 *
 */
package com.telenav.datatypes.route;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *@author yning
 *@date 2011-7-8
 */
public class RouteWrapperTest
{
    RouteWrapper wrapper = RouteWrapper.getInstance();

    @Before
    public void setup()
    {
        RouteWrapper.getInstance().clear();
    }

    @Test
    public void testAddRoute()
    {
        Vector routes = wrapper.getRoutes();
        assertEquals(0, routes.size());
        Route route = new Route();
        wrapper.addRoute(route);
        assertEquals(1, routes.size());
        assertEquals(route, routes.elementAt(0));
    }

    @Test
    public void testAddRoutes()
    {
        Vector routes = wrapper.getRoutes();
        assertEquals(0, routes.size());

        Route[] addedRoutes = new Route[3];
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
        }

        wrapper.addRoutes(addedRoutes);
        assertEquals(3, routes.size());
        for (int i = 0; i < routes.size(); i++)
        {
            assertEquals(addedRoutes[i], routes.elementAt(i));
        }
    }

    @Test
    public void testClear()
    {
        Vector routes = wrapper.getRoutes();
        Route[] addedRoutes = new Route[3];
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
            addedRoutes[i].setRouteID(i);
        }

        wrapper.addRoutes(addedRoutes);
        wrapper.setCurrentRouteId(0);

        assertEquals(3, routes.size());
        for (int i = 0; i < routes.size(); i++)
        {
            assertEquals(addedRoutes[i], routes.elementAt(i));
        }

        assertEquals(addedRoutes[0], wrapper.getCurrentRoute());

        wrapper.clear();

        assertEquals(0, routes.size());
        assertNull(wrapper.getCurrentRoute());
    }
    
    @Test
    public void testGetCurrentRoute()
    {
        assertNull(wrapper.getCurrentRoute());
        Route[] addedRoutes = new Route[3];
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
            addedRoutes[i].setRouteID(i);
        }

        wrapper.addRoutes(addedRoutes);
        wrapper.setCurrentRouteId(0);
        
        assertEquals(addedRoutes[0], wrapper.getCurrentRoute());
    }
    
    public void testGetCurrentRouteId()
    {
        assertEquals(-1, wrapper.getCurrentRouteId());
        Route[] addedRoutes = new Route[3];
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
            addedRoutes[i].setRouteID(i);
        }

        wrapper.addRoutes(addedRoutes);
        wrapper.setCurrentRouteId(1);
        
        assertEquals(1, wrapper.getCurrentRouteId());
    }
    
    @Test
    public void testGetRouteNull()
    {
        assertNull(wrapper.getRoute(0));
        Route[] addedRoutes = new Route[3];
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
            addedRoutes[i].setRouteID(i);
        }

        wrapper.addRoutes(addedRoutes);
        wrapper.setCurrentRouteId(1);
        
        assertNull(wrapper.getRoute(9));
    }
    
    @Test
    public void testGetRouteNotNull()
    {
        Route[] addedRoutes = new Route[3];
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
            addedRoutes[i].setRouteID(i);
        }

        wrapper.addRoutes(addedRoutes);
        wrapper.setCurrentRouteId(1);
        
        assertEquals(addedRoutes[1], wrapper.getRoute(1));
        assertEquals(addedRoutes[0], wrapper.getRoute(0));
    }
    
    @Test
    public void testGetRoutes()
    {
        Vector routes = wrapper.getRoutes();
        assertEquals(0, routes.size());
        
        Route[] addedRoutes = new Route[3];
        Vector expected = new Vector();
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
            addedRoutes[i].setRouteID(i);
            expected.addElement(addedRoutes[i]);
        }
        wrapper.addRoutes(addedRoutes);
        
        assertEquals(expected, wrapper.getRoutes());
    }
    
    @Test
    public void testRouteStyle()
    {
        wrapper.setRouteStyle(Route.ROUTE_FASTEST);
        assertEquals(Route.ROUTE_FASTEST, wrapper.getRouteStyle());
        wrapper.setRouteStyle(Route.ROUTE_PEDESTRIAN);
        assertEquals(Route.ROUTE_PEDESTRIAN, wrapper.getRouteStyle());
    }
    
    @Test
    public void testSetCurrentRouteId()
    {
        Route[] addedRoutes = new Route[3];
        for (int i = 0; i < addedRoutes.length; i++)
        {
            addedRoutes[i] = new Route();
            addedRoutes[i].setRouteID(i);
        }

        wrapper.addRoutes(addedRoutes);
        wrapper.setCurrentRouteId(1);
        assertEquals(1, wrapper.getCurrentRouteId());
        assertEquals(addedRoutes[1], wrapper.getCurrentRoute());
        wrapper.setCurrentRouteId(1);
        assertEquals(1, wrapper.getCurrentRouteId());
        assertEquals(addedRoutes[1], wrapper.getCurrentRoute());
        wrapper.setCurrentRouteId(2);
        assertEquals(2, wrapper.getCurrentRouteId());
        assertEquals(addedRoutes[2], wrapper.getCurrentRoute());
        
        
    }
}
