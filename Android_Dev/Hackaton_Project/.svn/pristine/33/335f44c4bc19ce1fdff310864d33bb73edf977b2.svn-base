/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * NavDataFactory.java
 *
 */
package com.telenav.datatypes.nav;

import com.telenav.location.TnLocation;


/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 20, 2010
 */
public class NavDataFactory
{
    protected static NavDataFactory instance = new NavDataFactory();

    protected NavUtil navUtil;
    
    private static int initCount;
    
    protected NavDataFactory()
    {

    }

    public static NavDataFactory getInstance()
    {
        return instance;
    }

    public synchronized static void init(NavDataFactory factory)
    {
        if (initCount >= 1)
            return;

        instance = factory;
        initCount++;
    }
    
    public NavState createNavState(int routeId)
    {
        return new NavState(routeId);
    }
    
    public NavState createNavState(int routeId, int segIndex, int edgeIndex, int shapePointIndex, int range)
    {
        return new NavState(routeId, segIndex, edgeIndex, shapePointIndex, range);
    }
    
    public synchronized NavUtil getNavUtil()
    {
        if(navUtil == null)
        {
            navUtil = new NavUtil();
        }
        
        return navUtil;
    }
}
